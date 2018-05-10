/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.arte;

import org.radixware.kernel.server.utils.ProfileStatistic;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EProfileMode;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.server.instance.ArteStateWriter;
import org.radixware.kernel.server.instance.InstanceProfiler;
import org.radixware.kernel.server.monitoring.ArteWaitStats;
import org.radixware.kernel.server.monitoring.MonitoringUtils;

public class ArteProfiler {

    private static final boolean HONEST_CPU_PART = SystemPropUtils.getBooleanSystemProp("rdx.arte.honest.cpu.part", false);
    private static final long WAIT_STATS_FLUSH_INTERVAL_MILLIS = SystemPropUtils.getLongSystemProp("rdx.arte.wait.stats.flush.interval.millis", 5000);
    private static final int REQUEST_PROFILER_LOG_PERCENT = SystemPropUtils.getIntSystemProp("rdx.arte.request.profiler.log.percent", 0);
    private static final int REQUEST_PROFILER_LOG_WRITE_IF_LONGER_MS = SystemPropUtils.getIntSystemProp("rdx.arte.request.profiler.log.write.if.longer.ms", 1000);
    private static final EEventSeverity REQUEST_PROFILER_LOG_WRITE_SEVERITY = EEventSeverity.getForName(SystemPropUtils.getStringSystemProp("rdx.arte.request.profiler.log.write.severity", "EVENT"));

    public enum EProfileTarget {

        NONE,
        PROFILER,
        MONITOR,
        ALL;
    }

    public enum EWaitType {

        CPU,
        DB,
        EXT,
        IDLE,
        QUEUE;

    }

    private enum ESwitchMode {

        FORCE,
        MERGE;
    }

    private static final EProfileTarget[] TARGETS = new EProfileTarget[]{
        EProfileTarget.NONE, EProfileTarget.PROFILER, EProfileTarget.MONITOR, EProfileTarget.ALL};
    private static final int PROFILER_TARGET = 1;
    private static final int MONITOR_TARGET = 2;
    private static Options DEFAULT_OPTIONS = new Options(EProfileMode.OFF, null, null, false);
    private Options options = DEFAULT_OPTIONS;
    private Options schedulledOptions = null;
    private final Arte arte;
    private final StringBuilder logBuilder = new StringBuilder();
    private final List<EWaitType> waitTypeStack = new ArrayList<>();
    private final ProfileStatistic profilerStat = new ProfileStatistic();
    private final ProfileStatistic monitoringStat = new ProfileStatistic();
    private final ArrayList<String> context = new ArrayList<>();
    private final ArrayList<Long> contextEnterNanos = new ArrayList<>();
    private String section = null;
    private String contextStr = "";
    private long sectionEnterNanos = 0;
    private long lastWaitStatsFlushMillis = -1;
    private final Object longWaitSem = new Object();
    private final WaitCounter waitCounter;
    private RequestLog requestLog;
    private final Random requestLogRandom = new Random();

    public ArteProfiler(final Arte arte) {
        this.arte = arte;
        waitCounter = new WaitCounter(arte);
    }

    void setOptions(final Options options) {
        if (section == null) {
            applyOptions(options);
        } else {
            schedulledOptions = options;
        }
    }

    private void applyOptions(final Options options) {
        if (!this.options.equals(options)) {
            this.options = options;
            arte.getTrace().put(EEventSeverity.EVENT, Messages.MLS_ID_PROFILE_OPTIONS_CHANGED, new ArrStr(options.toString()), EEventSource.ARTE_PROFILER.getValue());
        }
    }

    public void onBeginIdle() {
        waitCounter.switchWaitType(EWaitType.IDLE);
    }

    public void onEndIdle() {
        waitCounter.switchWaitType(EWaitType.CPU);
    }

    public void onBeginTransaction() {
        waitTypeStack.clear();
        if (REQUEST_PROFILER_LOG_PERCENT > 0 && requestLogRandom.nextInt(100) < REQUEST_PROFILER_LOG_PERCENT) {
            requestLog = new RequestLog(arte);
        } else {
            requestLog = null;
        }
    }

    public void onEndTransaction() {
        waitTypeStack.clear();
        if (requestLog != null && requestLog.finish(arte) / 1000000 > REQUEST_PROFILER_LOG_WRITE_IF_LONGER_MS) {
            arte.getTrace().put(REQUEST_PROFILER_LOG_WRITE_SEVERITY, requestLog.getAsStr(), EEventSource.ARTE_PROFILER);
        }
    }

    public void enterTimingSection(final ETimingSection timingSectionId) {
        enterTimingSection(timingSectionId.getValue());
    }

    private boolean isRightThread() {
        //dirty workaround 
        return arte.getProcessorThread() == Thread.currentThread();
    }

    public void enterTimingSection(final String timingSectionId) {
        if (!isRightThread()) {
            return;
        }
        arte.yeld();
        if (section != null) {
            context.add(section);
            contextEnterNanos.add(sectionEnterNanos);
            if (context.size() == 1) {
                contextStr = section;
            } else {
                contextStr += ";" + section;
            }
        }
        section = timingSectionId;
        sectionEnterNanos = System.nanoTime();
        if (requestLog != null) {
            requestLog.append(sectionEnterNanos, section, true, null);
        }
        pushWaitType(getWaitType(section));
    }

    public void leaveTimingSection(final ETimingSection timingSection) {
        leaveTimingSection(timingSection.getValue(), null);
    }

    public void leaveTimingSection(final ETimingSection timingSection, final String comment) {
        leaveTimingSection(timingSection.getValue(), comment);
    }

    public void leaveTimingSection(final String timingSectionId) {
        leaveTimingSection(timingSectionId, null);
    }

    public void leaveTimingSection(final String timingSectionId, final String comment) {
        if (!isRightThread()) {
            return;
        }
        assert timingSectionId != null;
        final EProfileTarget target = getProfileTarget(timingSectionId);
        final long leaveTimeNanos = System.nanoTime();
        if (timingSectionId.equals(section)) {
            if (target == EProfileTarget.PROFILER || target == EProfileTarget.ALL) {
                profilerStat.register(contextStr, section, leaveTimeNanos - sectionEnterNanos);
            }
            if (target == EProfileTarget.MONITOR || target == EProfileTarget.ALL) {
                monitoringStat.register(contextStr, section, leaveTimeNanos - sectionEnterNanos);
            }
            if (requestLog != null) {
                requestLog.append(leaveTimeNanos, section, false, comment);
            }
            popWaitType();
        } else {
            arte.getTrace().put(EEventSeverity.WARNING, Messages.MLS_ID_ERR_INVALID_TIMING_SECTION, new ArrStr(section, timingSectionId), EEventSource.ARTE_PROFILER.getValue());
            context.clear();
            contextEnterNanos.clear();
            contextStr = "";
            while (!waitTypeStack.isEmpty()) {
                popWaitType();
            }
        }
        if (context.isEmpty()) {
            section = null;
            if (schedulledOptions != null) {
                applyOptions(schedulledOptions);
                schedulledOptions = null;
            }
        } else {
            section = context.remove(context.size() - 1);
            sectionEnterNanos = contextEnterNanos.remove(contextEnterNanos.size() - 1);
            if (context.isEmpty()) {
                contextStr = "";
            } else {
                contextStr = contextStr.substring(0, contextStr.length() - section.length() - 1);
            }
        }
        if (lastWaitStatsFlushMillis != -1 && System.currentTimeMillis() - lastWaitStatsFlushMillis > WAIT_STATS_FLUSH_INTERVAL_MILLIS) {
            lastWaitStatsFlushMillis = System.currentTimeMillis();
            arte.getInstance().getInstanceMonitor().registerStatistic(arte, waitCounter.flushDiff(), null);
        }
        ArteStateWriter.gatherCurrentThreadState();
    }

    public TimingSection startTimingSection(final ETimingSection timingSection) {
        if (timingSection == null) {
            throw new IllegalArgumentException("timingSection can't be null");
        } else {
            return startTimingSection(timingSection.getValue());
        }
    }

    public TimingSection startTimingSection(final String timingSectionId) {
        if (timingSectionId == null) {
            throw new IllegalArgumentException("timingSectionId can't be null");
        } else {
            enterTimingSection(timingSectionId);
            return new TimingSection() {
                @Override
                public void close() {
                    leaveTimingSection(timingSectionId);
                }
            };
        }
    }

    private void pushWaitType(EWaitType waitType) {
        if (waitType == null) {
            waitType = EWaitType.CPU;
        }
        waitTypeStack.add(waitType);
        waitCounter.switchWaitType(waitType);
    }

    private EWaitType getCurWaitType() {
        if (waitTypeStack.isEmpty()) {
            return EWaitType.CPU;
        } else {
            return waitTypeStack.get(waitTypeStack.size() - 1);
        }
    }

    private void popWaitType() {
        if (!waitTypeStack.isEmpty()) {
            waitTypeStack.remove(waitTypeStack.size() - 1);
        }
        waitCounter.switchWaitType(getCurWaitType());
    }

    public void flushWaitStatsThreadSafe() {
        arte.getInstance().getInstanceMonitor().registerStatistic(arte, waitCounter.flushDiff(), null);
    }

    private EWaitType getWaitType(final String sectionName) {
        if (ETimingSection.RDX_ARTE_WAIT_ACTIVE.getValue().equals(sectionName)) {
            return EWaitType.QUEUE;
        }
        return arte.getDefManager().getSectionType(sectionName);
    }

    private EProfileTarget getProfileTarget(final String timingSectionId) {
        int target = 0;
        if (options.mode != EProfileMode.OFF) {
            if (options.mode == EProfileMode.ALL) {
                target |= PROFILER_TARGET;
            } else if (matchSection(options.profilerPrefixes, timingSectionId)) {
                target |= PROFILER_TARGET;
            }
        }
        if (options.monitoredSections != null && matchSection(options.monitoredSections, timingSectionId)) {
            target |= MONITOR_TARGET;
        }
        return TARGETS[target];
    }
    
    public boolean isProfilingOn() {
        return options.mode != EProfileMode.OFF || requestLog != null;
    }

    private boolean matchSection(final Collection<String> prefixes, final String sectionId) {
        if (prefixes == null || prefixes.isEmpty()) {
            return false;
        }
        for (final String prefix : prefixes) {
            if (prefix.length() == sectionId.length()
                    && sectionId.equals(prefix)) {
                return true;
            } else if (sectionId.length() > prefix.length() + 1
                    && ((sectionId.startsWith(prefix) && sectionId.charAt(prefix.length()) == '.') || (sectionId.startsWith(prefix) && sectionId.charAt(prefix.length()) == ':'))) {
                return true;
            }
        }
        return false;
    }

    public void close() {
        flush();
    }

    public ArteWaitStats getWaitStatsSnapshot() {
        return waitCounter.snapshot();
    }

    public void flush() {
        if (!profilerStat.isEmpty()) {
            final InstanceProfiler profiler = arte.getInstance().getProfiler();
            if (profiler != null) // instance is not stopped
            {
                profiler.register(profilerStat.getResult());
            }
            profilerStat.clear();
        }
        arte.getInstance().getInstanceMonitor().registerStatistic(arte, waitCounter.flushDiff(), monitoringStat.getResult());
        monitoringStat.clear();
    }

    static class Options {

        private final EProfileMode mode;
        private final Collection<String> profilerPrefixes;
        private final Set<String> monitoredSections;
        private final boolean countStatistics;

        public Options(final EProfileMode mode, final String profilerPrefixes, final Collection<String> monitoredSectionNames, final boolean countStatistics) {
            this.mode = mode;
            if (mode == EProfileMode.SPECIFIED && profilerPrefixes != null) {
                final String[] prefArr = profilerPrefixes.split(";");
                this.profilerPrefixes = new ArrayList<String>(prefArr.length);
                for (String pref : prefArr) {
                    if (!pref.isEmpty()) {
                        this.profilerPrefixes.add(pref);
                    }
                }
            } else {
                this.profilerPrefixes = null;
            }
            if (monitoredSectionNames != null && !monitoredSectionNames.isEmpty()) {
                monitoredSections = new HashSet<String>(monitoredSectionNames);
            } else {
                this.monitoredSections = null;
            }
            this.countStatistics = countStatistics;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Options other = (Options) obj;
            if (this.mode != other.mode) {
                return false;
            }
            if (this.countStatistics != other.countStatistics) {
                return false;
            }

            if (this.profilerPrefixes != other.profilerPrefixes && (this.profilerPrefixes == null || !this.profilerPrefixes.equals(other.profilerPrefixes))) {
                return false;
            }

            if (this.monitoredSections != other.monitoredSections && (this.monitoredSections == null || !this.monitoredSections.equals(other.monitoredSections))) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = countStatistics ? 7 : 53;
            hash = 89 * hash + (this.mode != null ? this.mode.hashCode() : 0);
            hash = 89 * hash + (this.profilerPrefixes != null ? this.profilerPrefixes.hashCode() : 0);
            hash = 89 * hash + (this.monitoredSections != null ? this.monitoredSections.hashCode() : 0);
            return hash;
        }

        @Override
        public String toString() {
            return "{" + "mode=" + mode.getName() + (mode == EProfileMode.SPECIFIED ? ", prefixes=" + profilerPrefixes : "") + '}';
        }
    }

    public interface TimingSection extends AutoCloseable {

        @Override
        public void close();
    }

    private static final class WaitCounter {

        private ArteWaitStats waitStats = new ArteWaitStats(0, 0, 0, 0, 0);
        private EWaitType curWaitType = EWaitType.IDLE;
        private long curWaitStartNanos = System.nanoTime();
        private long curWaitStartCpuNanos = MonitoringUtils.getCurrentThreadCpuTime();
        private ArteWaitStats lastFlushedSnapshot;
        private final Arte arte;

        public WaitCounter(Arte arte) {
            this.arte = arte;
        }

        private long getArteThreadCpuTime() {
            return MonitoringUtils.getThreadCpuTime(arte.getProcessorThread().getId());
        }

        public synchronized ArteWaitStats snapshot() {
            if (curWaitType == null) {
                return waitStats;
            }
            return appendCurrentWait(System.nanoTime(), HONEST_CPU_PART ? getArteThreadCpuTime() : 0);
        }

        public synchronized void switchWaitType(final EWaitType waitType) {
            final long curCpuNanos = HONEST_CPU_PART ? getArteThreadCpuTime() : 0;
            final long curNanos = System.nanoTime();
            if (curWaitType != null) {
                waitStats = appendCurrentWait(curNanos, curCpuNanos);
            }
            curWaitStartNanos = curNanos;
            curWaitType = waitType;
            curWaitStartCpuNanos = curCpuNanos;
        }

        public synchronized ArteWaitStats flushDiff() {
            final ArteWaitStats curSnapshot = snapshot();
            if (lastFlushedSnapshot == null) {
                return curSnapshot;
            }
            final ArteWaitStats result = lastFlushedSnapshot.substractFrom(curSnapshot);
            lastFlushedSnapshot = curSnapshot;
            return result;
        }

        private ArteWaitStats appendCurrentWait(final long curNanos, final long curCpuNanos) {
            long cpuDiffNanos = 0;
            long diffNanos = curNanos - curWaitStartNanos;
            if (HONEST_CPU_PART) {
                if (curWaitStartCpuNanos >= 0 && curCpuNanos >= 0) {
                    cpuDiffNanos = Math.min(diffNanos, curCpuNanos - curWaitStartCpuNanos);
                }
                if (curWaitType == EWaitType.CPU) {
                    return waitStats.add(cpuDiffNanos, curWaitType).add(diffNanos - cpuDiffNanos, EWaitType.QUEUE);
                } else {
                    return waitStats.add(cpuDiffNanos, EWaitType.CPU).add(diffNanos - cpuDiffNanos, curWaitType);
                }
            } else {
                return waitStats.add(diffNanos, curWaitType);
            }

        }
    }

    private static class RequestLog {

        private final StringBuilder sb = new StringBuilder();
        private final long startNanos;
        private long lastNanos;
        private int depth = 0;
        private final ArteWaitStats startWaitStats;
        private final Map<String, CommentStats> commentStats = new HashMap<>();

        public RequestLog(final Arte arte) {
            sb.append("RequestLog | Inst#" + arte.getInstance().getId() + "/Arte#" + arte.getSeqNumber() + "/db#" + arte.getDbSid() + " | " + (new Date()));
            sb.append("\n");
            startNanos = System.nanoTime();
            lastNanos = startNanos;
            startWaitStats = arte.getProfiler().getWaitStatsSnapshot();
        }

        public void append(final long nanoTime, final String secName, final boolean enter, final String comment) {

            final long diffUs = (nanoTime - lastNanos) / 1000;
            lastNanos = nanoTime;
            if (enter) {
                depth++;
            }

            sb.append(getMark(diffUs));

            for (int i = 0; i < depth; i++) {
                sb.append("  ");
            }

            sb.append((enter ? "> " : "< ") + "(+ " + diffUs + " us, total " + ((nanoTime - startNanos) / 1000) + " us) " + secName + (comment == null ? "" : " (" + comment + ")"));
            sb.append("\n");

            if (comment != null) {

                CommentStats curStats = commentStats.get(comment);
                if (curStats == null) {
                    curStats = new CommentStats();
                    curStats.count = 1;
                    curStats.durationUs = (int) diffUs;
                    curStats.comment = comment;
                    commentStats.put(comment, curStats);
                } else {
                    curStats.count++;
                    curStats.durationUs += diffUs;
                }
            }

            if (!enter) {
                depth--;
            }
        }

        private String getMark(final long diff) {
            if (diff < 5000) {
                return "  ";
            } else if (diff < 10000) {
                return "! ";
            } else {
                return "!!";
            }
        }

        private long finish(final Arte arte) {
            final long nanoTime = System.nanoTime();
            final ArteWaitStats stats = startWaitStats.substractFrom(arte.getProfiler().getWaitStatsSnapshot());
            sb.insert(13, " | total: " + ((nanoTime - startNanos) / 1000000) + " ms,"
                    + " CPU: " + (stats.getCpuNanos() / 1000000)
                    + " DB: " + (stats.getDbNanos() / 1000000)
                    + " EXT: " + (stats.getExtNanos() / 1000000)
                    + " QUEUE: " + (stats.getOtherNanos() / 1000000)
            );
            sb.append("\n\n");

            final List<CommentStats> list = new ArrayList<>(commentStats.values());
            Collections.sort(list, new Comparator<CommentStats>() {

                @Override
                public int compare(CommentStats o1, CommentStats o2) {
                    return o2.durationUs - o1.durationUs;
                }

            });

            for (CommentStats stat : list) {
                sb.append(stat.durationUs + "us (" + stat.count + ") : " + stat.comment);
                sb.append("\n");
            }

            return nanoTime - startNanos;
        }

        public String getAsStr() {
            return sb.toString();
        }

        private static class CommentStats {

            private int count;
            private int durationUs;
            private String comment;
        }
    }
}
