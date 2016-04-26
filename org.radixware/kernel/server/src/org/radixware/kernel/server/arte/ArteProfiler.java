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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EProfileMode;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.server.instance.InstanceProfiler;
import org.radixware.kernel.server.monitoring.TransactionTimeStatistic;

public class ArteProfiler {

    private static final boolean BUILD_LOG = false;
    private static final long TRAN_TIME_STAT_FLUSH_INTERVAL_MILLIS = 5000;

    public enum EProfileTarget {

        NONE,
        PROFILER,
        MONITOR,
        ALL;
    }

    public enum EWaitType {

        CPU(false),
        DB(true),
        EXT(true);

        private final boolean longWait;

        private EWaitType(final boolean longWait) {
            this.longWait = longWait;
        }

        public boolean isLongWait() {
            return longWait;
        }
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
    private long dbTimeNanos = 0;
    private long extTimeNanos = 0;
    private long cpuTimeNanos = 0;
    private long curWaitStartNanos = 0;
    //@GuardedBy waitTimeSem, modified only by Arte, can be read by Monitoring data collector thread
    private int longWaits = 0;
    private final List<EWaitType> waitTypeStack = new ArrayList<>();
    private long tranStartNanos = -1;
    private long tranEndNanos = -1;
    private final ProfileStatistic profilerStat = new ProfileStatistic();
    private final ProfileStatistic monitoringStat = new ProfileStatistic();
    private final ArrayList<String> context = new ArrayList<>();
    private final ArrayList<Long> contextEnterNanos = new ArrayList<>();
    private String section = null;
    private String contextStr = "";
    private long sectionEnterNanos = 0;
    private long lastPartialFlushWaitTimeMillis = -1;
    private final Object longWaitSem = new Object();

    public ArteProfiler(final Arte arte) {
        this.arte = arte;
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

    public void onBeginTransaction() {
        if (options.countStatistics) {
            startTranTimeStatCalculation(System.nanoTime());
        }
    }

    private void startTranTimeStatCalculation(final long startTimeNanos) {
        tranStartNanos = startTimeNanos;
        tranEndNanos = -1;
        dbTimeNanos = 0;
        extTimeNanos = 0;
        cpuTimeNanos = 0;
        curWaitStartNanos = startTimeNanos;
        lastPartialFlushWaitTimeMillis = System.currentTimeMillis();
        waitTypeStack.clear();
        ensureNotInLongWait();
        if (BUILD_LOG) {
            logBuilder.setLength(0);
            logBuilder.append("-- started -- /" + tranStartNanos + "\n");
        }
    }

    public void onEndTransaction() {
        if (options.countStatistics) {
            if (tranStartNanos == -1) {
                return;//statistic counting was enabled after transaction start
            }
            tranEndNanos = System.nanoTime();
            lastPartialFlushWaitTimeMillis = -1;

            if (BUILD_LOG) {
                logBuilder.append("-- finished -- /" + tranEndNanos + "\n");
            }
        }
    }

    private void ensureNotInLongWait() {
        if (longWaits != 0) {
            synchronized (longWaitSem) {
                longWaits = 0;
            }
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
        if (!options.countStatistics && getProfileTarget(timingSectionId) == EProfileTarget.NONE) {
            return;
        }
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
        pushWaitType(getWaitType(section), sectionEnterNanos);
        if (BUILD_LOG) {
            logBuilder.append(StringUtils.repeat("  ", context.size()) + ">" + section + "  enter: " + sectionEnterNanos + "\n");
        }
    }

    public void leaveTimingSection(final ETimingSection timingSectionId) {
        leaveTimingSection(timingSectionId.getValue());
    }

    public void leaveTimingSection(final String timingSectionId) {
        if (!isRightThread()) {
            return;
        }
        assert timingSectionId != null;
        final EProfileTarget target = getProfileTarget(timingSectionId);
        if (!options.countStatistics && target == EProfileTarget.NONE) {
            return;
        }
        final long leaveTime = System.nanoTime();
        if (timingSectionId.equals(section)) {
            if (target == EProfileTarget.PROFILER || target == EProfileTarget.ALL) {
                profilerStat.register(contextStr, section, leaveTime - sectionEnterNanos);
            }
            if (target == EProfileTarget.MONITOR || target == EProfileTarget.ALL) {
                monitoringStat.register(contextStr, section, leaveTime - sectionEnterNanos);
            }

            popWaitType(leaveTime);

            if (BUILD_LOG) {
                logBuilder.append(StringUtils.repeat("  ", context.size()) + "<" + section + "  leave: " + leaveTime + "; spent: " + (leaveTime - sectionEnterNanos) + "; type: " + getWaitType(section) + "\n");
            }
        } else {
            arte.getTrace().put(EEventSeverity.WARNING, Messages.MLS_ID_ERR_INVALID_TIMING_SECTION, new ArrStr(section, timingSectionId), EEventSource.ARTE_PROFILER.getValue());
            context.clear();
            contextEnterNanos.clear();
            contextStr = "";
            while (!waitTypeStack.isEmpty()) {
                popWaitType(leaveTime);
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
        if (lastPartialFlushWaitTimeMillis != -1 && System.currentTimeMillis() - lastPartialFlushWaitTimeMillis > TRAN_TIME_STAT_FLUSH_INTERVAL_MILLIS) {
            lastPartialFlushWaitTimeMillis = System.currentTimeMillis();
            final TransactionTimeStatistic stat = flushTranTimeStatFromArteThread();
            if (stat != null) {
                arte.getInstance().getInstanceMonitor().registerStatistic(stat, null);
            }
        }
    }

    private void pushWaitType(final EWaitType waitType, final long nanos) {
        if (longWaits > 0 || waitType.isLongWait()) {
            synchronized (longWaitSem) {
                doPushWaitType(waitType, nanos);
                if (waitType.isLongWait()) {
                    longWaits++;
                }
            }
        } else {
            doPushWaitType(waitType, nanos);
        }
    }

    private void doPushWaitType(final EWaitType waitType, final long nanos) {
        switchWaitType(getCurWaitType(), waitType, nanos, ESwitchMode.MERGE);
        waitTypeStack.add(waitType);
    }

    private EWaitType getCurWaitType() {
        if (waitTypeStack.isEmpty()) {
            return EWaitType.CPU;
        } else {
            return waitTypeStack.get(waitTypeStack.size() - 1);
        }
    }

    private void popWaitType(final long nanos) {
        if (longWaits > 0) {
            synchronized (longWaitSem) {
                if (getCurWaitType().isLongWait()) {
                    longWaits--;
                }
                doPopWaitType(nanos);
            }
        } else {
            doPopWaitType(nanos);
        }
    }

    private void doPopWaitType(final long nanos) {
        final EWaitType oldWaitType = getCurWaitType();
        if (!waitTypeStack.isEmpty()) {
            waitTypeStack.remove(waitTypeStack.size() - 1);
        }
        switchWaitType(oldWaitType, getCurWaitType(), nanos, ESwitchMode.MERGE);
    }

    private void switchWaitType(final EWaitType oldWaitType, final EWaitType newWaitType, final long nanos, final ESwitchMode mode) {
        if (newWaitType != oldWaitType || mode == ESwitchMode.FORCE) {
            if (oldWaitType == EWaitType.CPU) {
                cpuTimeNanos += nanos - curWaitStartNanos;
            } else if (oldWaitType == EWaitType.DB) {
                dbTimeNanos += nanos - curWaitStartNanos;
            } else {
                extTimeNanos += nanos - curWaitStartNanos;
            }
            curWaitStartNanos = nanos;
        }
    }

    public void flushWaitStats() {
        TransactionTimeStatistic stat = null;
        synchronized (longWaitSem) {
            if (longWaits > 0) {
                stat = doFlushTranTimeStat();
            }
        }
        if (stat != null) {
            arte.getInstance().getInstanceMonitor().registerStatistic(stat, null);
        }
    }

    private EWaitType getWaitType(final String sectionName) {
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

    private boolean matchSection(final Collection<String> prefixes, final String sectionId) {
        if (prefixes == null || prefixes.isEmpty()) {
            return false;
        }
        for (final String prefix : prefixes) {
            if (prefix.length() == sectionId.length()
                    && sectionId.equals(prefix)) {
                return true;
            } else if (sectionId.length() > prefix.length() + 1
                    && (sectionId.startsWith(prefix + ".") || sectionId.startsWith(prefix + ":"))) {
                return true;
            }
        }
        return false;
    }

    void close() {
        flush();
    }

    void flush() {
        if (!options.countStatistics && monitoringStat.isEmpty() && profilerStat.isEmpty()) {
            return;
        }
        if (!profilerStat.isEmpty()) {
            final InstanceProfiler profiler = arte.getInstance().getProfiler();
            if (profiler != null) // instance is not stopped
            {
                profiler.register(profilerStat.getResult());
            }
            profilerStat.clear();
        }
        final TransactionTimeStatistic tranTimeStat = flushTranTimeStatFromArteThread();
        arte.getInstance().getInstanceMonitor().registerStatistic(tranTimeStat, monitoringStat.getResult());
        monitoringStat.clear();
    }

    private TransactionTimeStatistic flushTranTimeStatFromArteThread() {
        if (longWaits > 0) {
            synchronized (longWaitSem) {
                return doFlushTranTimeStat();
            }
        } else {
            return doFlushTranTimeStat();
        }
    }

    private TransactionTimeStatistic doFlushTranTimeStat() {
        if (options.countStatistics && tranStartNanos > 0) {
            final long flushNanos = tranEndNanos > 0 ? tranEndNanos : System.nanoTime();
            switchWaitType(getCurWaitType(), getCurWaitType(), flushNanos, ESwitchMode.FORCE);
            final TransactionTimeStatistic tranTimeStat = new TransactionTimeStatistic(
                    cpuTimeNanos,
                    dbTimeNanos,
                    extTimeNanos);
            cpuTimeNanos = 0;
            dbTimeNanos = 0;
            extTimeNanos = 0;
            if (tranTimeStat.getDbNanos() < 0 || tranTimeStat.getExtNanos() < 0 || tranTimeStat.getCpuNanos() < 0
                    || (tranTimeStat.getDbNanos() + tranTimeStat.getExtNanos() + tranTimeStat.getCpuNanos() <= 0)) {
                final String details;
                if (BUILD_LOG) {
                    logBuilder.append(tranTimeStat.toString());
                    details = logBuilder.toString();
                } else {
                    details = tranTimeStat.toString();
                }
                arte.getTrace().put(EEventSeverity.WARNING, "Suspicious profiler stats:\n\n" + details, EEventSource.ARTE_PROFILER.getValue());
            }
            return tranTimeStat;
        }
        return null;
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
}
