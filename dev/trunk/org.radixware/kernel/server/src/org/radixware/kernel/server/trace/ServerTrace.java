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
package org.radixware.kernel.server.trace;

import java.sql.Connection;
import java.util.*;
import org.radixware.kernel.common.defs.utils.ISeverityByCodeCalculator;
import org.radixware.kernel.common.enums.EEventContextType;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.trace.IRadixTrace;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.trace.TraceItem;
import org.radixware.kernel.common.trace.TraceProfile;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.CircularBuffer;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.server.SrvRunParams;
import org.radixware.kernel.server.instance.Instance;

public class ServerTrace implements IRadixTrace {

    public enum ETraceDestination {

        FILE,
        DATABASE,
        GUI;
    }
    private Trace eventLog;
    protected TraceBuffer view;
    private TraceProfile guiProfile;//cache for optimization
    private TraceProfile fileProfile;//cache for optimization
    private TraceProfiles profiles;
    private Object dbLogHandl = null;
    private final CircularBuffer<TraceItem> undeliveredToView;
    private TraceProfile guiProfileOvr = null;
    private final Stack<String> viewContextStack;
    private FileLog log = null;
    private final FloodController floodController = new FloodController();
    private final List<ITraceFilter> traceFilters = new ArrayList<>();
    private volatile String ownerDescription;

    public static final class Factory {

        private static volatile Factory FACTORY = new Factory();

        @Deprecated
        /**
         * Use prototype with description
         */
        public ServerTrace createNewInstance(final Instance instance) {
            return newInstance(instance, ServerTrace.class, null);
        }

        @Deprecated
        /**
         * Use prototype with description
         */
        public <T extends ServerTrace> T createNewInstance(final Instance instance, final Class<T> c) {
            return newInstance(instance, c, null);
        }

        public <T extends ServerTrace> T createNewInstance(final Instance instance, final Class<T> c, final String ownerDescription) {
            final T trace;
            try {
                    trace = c.newInstance();
            } catch (InstantiationException ex) {
                throw new RadixError(ex.getMessage(), ex);
            } catch (IllegalAccessException ex) {
                throw new RadixError(ex.getMessage(), ex);
            }
            final LocalTracer traceErrorTracer = new LocalTracer() {
                @Override
                public void put(final EEventSeverity severity, final String localizedMess, final String code, final List<String> words, final boolean isSensitive) {
                    final ArrStr w = new ArrStr();
                    if (words != null) {
                        w.addAll(words);
                    }
                    trace.put(severity, localizedMess, code, w, EEventSource.INSTANCE.getValue(), -1, isSensitive);
                }

                @Override
                public long getMinSeverity() {
                    return trace.getMinSeverity();
                }

                @Override
                public long getMinSeverity(final String eventSource) {
                    return trace.getMinSeverity(eventSource);
                }
            };
            trace.setOwnerDescription(ownerDescription);
            trace.initLogs(instance, traceErrorTracer);
            return trace;
        }

        public static void setDelegate(final ServerTrace.Factory delegate) {
            FACTORY = delegate;
        }

        @Deprecated
        /**
         * Use prototype with description
         */
        public static ServerTrace newInstance(final Instance instance) {
            return FACTORY.createNewInstance(instance);
        }

        @Deprecated
        /**
         * Use prototype with description
         */
        public static <T extends ServerTrace> T newInstance(final Instance instance, final Class<T> c) {
            return FACTORY.createNewInstance(instance, c);
        }

        public static ServerTrace newInstance(final Instance instance, final String ownerDescription) {
            return FACTORY.createNewInstance(instance, ServerTrace.class, ownerDescription);
        }

        public static <T extends ServerTrace> T newInstance(final Instance instance, final Class<T> c, final String ownerDescription) {
            return FACTORY.createNewInstance(instance, c, ownerDescription);
        }
    }

    protected ServerTrace() {
        profiles = TraceProfiles.DEFAULT;
        guiProfile = new TraceProfile(profiles.getGuiTraceProfile());
        fileProfile = new TraceProfile(profiles.getFileTraceProfile());
        view = null;
        undeliveredToView = new CircularBuffer<>(SystemPropUtils.getIntSystemProp("rdx.undelivered.to.view.buffer.size", 200));
        viewContextStack = new Stack<>();
    }

    /**
     *
     * @return null if is not overridden
     */
    public synchronized String getOverridenGuiProfile() {
        return guiProfileOvr == null ? null : guiProfileOvr.toString();
    }

    protected synchronized void initLogs(final Instance instance, final LocalTracer traceErrorTracer) {
        eventLog = createDbTrace(instance, traceErrorTracer);
        dbLogHandl = eventLog.addTargetLog(profiles.getDbTraceProfile(), ownerDescription);
    }

    public void setOwnerDescription(final String description) {
        this.ownerDescription = description;
    }

    public String getOwnerDescription() {
        return ownerDescription;
    }

    protected Trace createDbTrace(final Instance instance, final LocalTracer traceErrorTracer) {
        return Trace.Factory.newInstance(instance, null, null, traceErrorTracer);
    }

    public synchronized final void registerView(final TraceBuffer view) {
        if (this.view != null) {
            throw new IllegalUsageError("Trace already has view");
        }
        this.view = view;
        for (int i = 0; i < undeliveredToView.size(); i++) {
            view.put(undeliveredToView.get(i));
        }
        undeliveredToView.clear();
    }

    public synchronized final void unregisterView(final TraceBuffer view) {
        if (this.view != view) {
            throw new IllegalUsageError("View is not registered");
        }
        this.view = null;
    }

    public synchronized void setDefaultFloodPeriod(final long periodMillis) {
        floodController.setDefaultFloodPeriod(periodMillis);
    }

    public synchronized void setFloodPeriod(final String floodKey, final long periodMillis) {
        floodController.setFloodPeriod(floodKey, periodMillis);
    }

    public synchronized void clearFloodSettingsAndStats() {
        floodController.clearFloodSettingsAndStats();
    }

    public synchronized void clearLastFloodMessageTime(final String key) {
        floodController.clearLastFloodMessageTime(key);
    }

    public synchronized void addTraceFilter(final ITraceFilter filter) {
        traceFilters.add(filter);
    }

    public synchronized void removeTraceFilter(final ITraceFilter filter) {
        traceFilters.remove(filter);
    }

    @Override
    public void put(EEventSeverity severity, String localizedMess, EEventSource source) {
        put(severity, localizedMess, null, null, source, false);
    }

    @Override
    public void put(EEventSeverity severity, String code, List<String> words, String source) {
        put(severity, null, code, words, source, false);
    }

    public final void put(
            final Long severity,
            final String localizedMessage,
            final String mlsId,
            final String[] mlsArgs,
            final String source,
            final boolean isSensetive) {
        put(EEventSeverity.getForValue(severity), localizedMessage, mlsId, new ArrStr(mlsArgs), source, -1, isSensetive);
    }

    public final void put(
            final EEventSeverity severity,
            final String localizedMessage,
            final String mlsId,
            final List<String> mlsArgs,
            final EEventSource source,
            final boolean isSensetive) {
        put(severity, localizedMessage, mlsId, mlsArgs, source.getValue(), -1, isSensetive);
    }

    public final void put(
            final EEventSeverity severity,
            final String localizedMessage,
            final String mlsId,
            final List<String> mlsArgs,
            final String source,
            final boolean isSensetive) {
        put(severity, localizedMessage, mlsId, mlsArgs, source, -1, isSensetive);
    }

    public final void put(
            final EEventSeverity severity,
            final String localizedMessage,
            final String mlsId,
            final List<String> mlsArgs,
            final String source,
            final long millisOrMinusOne,
            final boolean isSensitive) {
        put(severity, localizedMessage, mlsId, mlsArgs, source, millisOrMinusOne, isSensitive, Arrays.asList(ETraceDestination.values()));
    }

    public void put(
            EEventSeverity severity,
            final String localizedMessage,
            final String mlsId,
            final List<String> mlsArgs,
            final String source,
            final long millisOrMinusOne,
            final boolean isSensitive,
            final Collection<ETraceDestination> targetDestinations) {
        doPut(severity, localizedMessage, mlsId, mlsArgs, source, eventLog == null ? null : eventLog.getNormalizedContextStackAsStr(), millisOrMinusOne, isSensitive, targetDestinations, null, null);
    }

    public void put(
            EEventSeverity severity,
            final String localizedMessage,
            final String mlsId,
            final List<String> mlsArgs,
            final String source,
            final long millisOrMinusOne,
            final boolean isSensitive,
            final Collection<ETraceDestination> targetDestinations,
            final List<TraceContext> additionalContexts) {
        doPut(severity, localizedMessage, mlsId, mlsArgs, source, eventLog == null ? null : eventLog.getNormalizedContextStackAsStr(additionalContexts), millisOrMinusOne, isSensitive, targetDestinations, null, additionalContexts);
    }

    public void put(
            EEventSeverity severity,
            final String localizedMessage,
            final String mlsId,
            final List<String> mlsArgs,
            final String source,
            final String contextView,//for including in file trace and viewing in GUI
            final long millisOrMinusOne,
            final boolean isSensitive,
            final Collection<ETraceDestination> targetDestinations) {
        doPut(severity, localizedMessage, mlsId, mlsArgs, source, contextView, millisOrMinusOne, isSensitive, targetDestinations, null, null);
    }

    public void putFloodControlled(
            final String floodKey,
            EEventSeverity severity,
            final String localizedMessage,
            final String mlsId,
            final List<String> mlsArgs,
            final String source,
            final long millisOrMinusOne,
            final boolean isSensitive,
            final Collection<ETraceDestination> targetDestinations) {
        doPut(severity, localizedMessage, mlsId, mlsArgs, source, eventLog.getNormalizedContextStackAsStr(), millisOrMinusOne, isSensitive, targetDestinations, floodKey, null);
    }

    public void put(final TraceItem item, final Collection<ETraceDestination> targetDestinations, final String floodKey) {
        doPut(item.severity, item.getMess(), item.code, item.words, item.source, item.context, item.time, item.isSensitive, targetDestinations, floodKey, null);
    }

    public void put(final TraceItem item, final Collection<ETraceDestination> targetDestinations, final String floodKey, final List<TraceContext> additionalContexts) {
        doPut(item.severity, item.getMess(), item.code, item.words, item.source, item.context, item.time, item.isSensitive, targetDestinations, floodKey, additionalContexts);
    }

    private synchronized boolean doPut(
            EEventSeverity severity,
            final String localizedMessage,
            final String mlsId,
            final List<String> mlsArgs,
            final String source,
            final String context,
            final long millisOrMinusOne,
            final boolean isSensitive,
            final Collection<ETraceDestination> targetDestinations,
            final String floodKey,
            final List<TraceContext> additionalContexts) {

        if (floodKey != null && !floodController.canPut(floodKey)) {
            return false;
        }

        for (ITraceFilter filter : traceFilters) {
            try {
                if (!filter.canPut(severity, localizedMessage, mlsId, mlsArgs, source, context, millisOrMinusOne, isSensitive, targetDestinations, floodKey)) {
                    return false;
                }
            } catch (Exception ex) {
                //ignore
            }
        }

        ISeverityByCodeCalculator severityPreprocessor = getSeverityPreprocessor();
        if (severityPreprocessor != null && mlsId != null) {
            EEventSeverity redefinedSeverity = null;
            try {
                redefinedSeverity = severityPreprocessor.getEventSeverityByCode(mlsId);
            } catch (RuntimeException ex) {
                put(EEventSeverity.WARNING, "Error on event severity recalculation: " + ExceptionTextFormatter.throwableToString(ex), EEventSource.ARTE);
            }
            if (redefinedSeverity != null && !(source != null && source.startsWith(EEventSource.APP_AUDIT.getValue()) && redefinedSeverity.getValue() <= EEventSeverity.DEBUG.getValue())) {
                severity = redefinedSeverity;
            }
        }
        final long timeMillis = millisOrMinusOne == -1 ? System.currentTimeMillis() : millisOrMinusOne;
        final ViewTraceItem it = new ViewTraceItem(severity, localizedMessage, source, context, timeMillis, isSensitive);
        if (targetDestinations == null || targetDestinations.contains(ETraceDestination.FILE)) {
            if (log != null && it.match(fileProfile)) //RADIX-2123
            {
                log.log(it);
            }
        }
        if (targetDestinations == null || targetDestinations.contains(ETraceDestination.GUI)) {
            if (SrvRunParams.getIsGuiOn() && it.match(guiProfile)) {
                if (view != null) {
                    view.put(it);
                } else {
                    undeliveredToView.add(it);
                }
            }
        }
        if (targetDestinations == null || targetDestinations.contains(ETraceDestination.DATABASE) && eventLog != null) {
            eventLog.put(severity, mlsId, mlsId == null ? new ArrStr(localizedMessage) : mlsArgs, source, isSensitive, timeMillis, false, additionalContexts);
        }
        return true;
    }

    private ISeverityByCodeCalculator getSeverityPreprocessor() {
        return eventLog != null && eventLog.instance != null ? eventLog.instance.getSeverityPreprocessor() : null;
    }

    public synchronized final void debug(final String message, final EEventSource source, final boolean isSensitive) {
        put(EEventSeverity.DEBUG, message, null, null, source, isSensitive);
    }

    public synchronized final void debug(final String message, final String source, final boolean isSensitive) {
        put(EEventSeverity.DEBUG, message, null, null, source, -1, isSensitive);
    }

    public synchronized void setDbConnection(final Connection dbConnection) {
        eventLog.setDbConnection(dbConnection);
    }

    public synchronized TraceProfiles getProfiles() {
        return profiles;
    }

    public synchronized void setProfiles(final TraceProfiles profiles) {
        if (guiProfileOvr == null) {
            this.profiles = profiles;
        } else {
            final String guiProfileOvrStr = guiProfileOvr.toString();
            this.profiles = new TraceProfiles(profiles.getDbTraceProfile(), profiles.getFileTraceProfile(), guiProfileOvrStr);
        }
        eventLog.changeTargetProfile(dbLogHandl, profiles.getDbTraceProfile());
        guiProfile = (guiProfileOvr == null ? new TraceProfile(profiles.getGuiTraceProfile()) : guiProfileOvr);
        fileProfile = new TraceProfile(profiles.getFileTraceProfile());
    }

    public synchronized void overrideGuiProfile(final String profile) {
        if (Objects.equals(guiProfile, profile) && Objects.equals(guiProfileOvr, profile)) {
            return;
        }
        final String changeInProfiles = "\"" + guiProfile + "\"" + "->\"" + profile + "\"";
        put(EEventSeverity.EVENT, Messages.GUI_TRACE_PROFILE_OVERRIDEN + " " + changeInProfiles, Messages.MLS_ID_GUI_TRACE_PROFILE_OVERRIDEN, new ArrStr(changeInProfiles), EEventSource.INSTANCE, false);
        guiProfile = guiProfileOvr = new TraceProfile(profile);
        this.profiles = new TraceProfiles(profiles.getDbTraceProfile(), profiles.getFileTraceProfile(), profile);
    }

    public synchronized void enterContext(final EEventContextType contextType, final Long contextId, final String viewContext) {
        eventLog.enterContext(contextType, contextId);
    }

    public synchronized void enterContext(final EEventContextType contextType, final String contextId, final String viewContext) {
        eventLog.enterContext(contextType, contextId);
    }

    public synchronized String getContextStackAsStr() {
        return eventLog.getContextStackAsStr();
    }

    public synchronized void leaveContext(final EEventContextType contextType, final Long contextId, final String viewContext) {
        eventLog.leaveContext(contextType, contextId);
    }

    public synchronized void leaveContext(final EEventContextType contextType, final String contextId, final String viewContext) {
        eventLog.leaveContext(contextType, contextId);
    }

    public synchronized void clearContextStack() {
        eventLog.clearContextStack();
        viewContextStack.clear();
    }

    public synchronized Object addTargetBuffer(final String profile, final TraceBuffer buffer) {
        return addTargetBuffer(profile, buffer, false);
    }

    public synchronized Object addTargetBuffer(final String profile, final TraceBuffer buffer, final boolean passive) {
        return eventLog.addTargetBuffer(profile, buffer, passive);
    }

    public synchronized void delTarget(final Object traceHandler) {
        eventLog.delTarget(traceHandler);
    }

    public synchronized List<TraceTarget> getTargets() {
        return new ArrayList<>(eventLog.getTargets());
    }

    public synchronized long getMinSeverity() {
        return Math.min(
                Math.min(guiProfile.getMinSeverity(), fileProfile.getMinSeverity()),
                eventLog.getMinSeverity());
    }

    public synchronized long getMinSeverity(final EEventSource eventSource) {
        return Math.min(
                Math.min(guiProfile.getMinSeverity(eventSource), fileProfile.getMinSeverity(eventSource)),
                eventLog.getMinSeverity(eventSource));
    }

    public synchronized long getMinSeverity(final String eventSource) {
        return Math.min(
                Math.min(guiProfile.getMinSeverity(eventSource), fileProfile.getMinSeverity(eventSource)),
                eventLog.getMinSeverity(eventSource));
    }

    public synchronized void flush() {
        eventLog.flush();
    }

    public synchronized void startFileLogging(final FileLogOptions options) {
        try {
            log = FileLog.create(options);
        } catch (RadixError e) {
            log = null;
            put(EEventSeverity.ERROR, ExceptionTextFormatter.exceptionStackToString(e), null /*
                     * TODO MLS
                     */, null, EEventSource.INSTANCE, false);
        }
    }

    public synchronized void stopFileLogging() {
        if (log != null) {
            log.close();
            log = null;
        }
    }

    ;
    /**
     * Change logging options if logging is started
     * @param options
     */
    public synchronized void changeFileLoggingOptions(final FileLogOptions options) {
        if (log != null) {
            log.logOptionsChanged(options);
            stopFileLogging();
            startFileLogging(options);
        }//else
        //logging is not started yet there is no use to change options
    }

    public final LocalTracer newTracer(final String eventSource) {
        return newTracer(eventSource, null);
    }

    public final LocalTracer newTracer(final String eventSource, final String plainMessPrefix) {
        return new LocalTracer() {
            private final String prefix = plainMessPrefix == null || plainMessPrefix.isEmpty() ? "" : plainMessPrefix + ": ";

            @Override
            public long getMinSeverity() {
                return ServerTrace.this.getMinSeverity();
            }

            @Override
            public long getMinSeverity(final String eventSource) {
                return ServerTrace.this.getMinSeverity(eventSource);
            }

            @Override
            public void put(final EEventSeverity sev, final String localizedMess, final String code, final List<String> words, final boolean isSensitive) {
                ServerTrace.this.put(sev, (code == null ? prefix + localizedMess : localizedMess), code, words, eventSource, isSensitive);
            }

            @Override
            public void putFloodControlled(String floodKey, EEventSeverity severity, String localizedMess, String code, List<String> words, boolean isSensitive) {
                ServerTrace.this.putFloodControlled(floodKey, severity, (code == null ? prefix + localizedMess : localizedMess), code, words, eventSource, -1, isSensitive, null);
            }
        };
    }

    private static class FloodController {

        private static final long DEFAULT_DEFAULT_PERIOD_MILLIS = 10000;
        // 
        private final Map<String, Long> lastMessageByKeyMillis = new HashMap<>();
        private final Map<String, Long> intervalByKey = new HashMap<>();
        private long defaultPeriodMillis;

        public FloodController() {
            this.defaultPeriodMillis = DEFAULT_DEFAULT_PERIOD_MILLIS;
        }

        public void setDefaultFloodPeriod(final long periodMillis) {
            this.defaultPeriodMillis = periodMillis;
        }

        public void setFloodPeriod(final String keyPrefix, final long periodMillis) {
            if (keyPrefix != null) {
                intervalByKey.put(keyPrefix, periodMillis);
            }
        }

        public void clearFloodSettingsAndStats() {
            lastMessageByKeyMillis.clear();
            intervalByKey.clear();
        }

        public void clearLastFloodMessageTime(final String floodKey) {
            if (floodKey != null) {
                lastMessageByKeyMillis.remove(floodKey);
            }
        }

        public boolean canPut(final String key) {
            final Long lastMillis = lastMessageByKeyMillis.get(key);
            final long curTimeMillis = System.currentTimeMillis();
            if (lastMillis == null || curTimeMillis - lastMillis > getIntervalMillisForKey(key)) {
                lastMessageByKeyMillis.put(key, curTimeMillis);
                return true;
            } else {
                return false;
            }
        }

        private long getIntervalMillisForKey(final String key) {
            if (key == null) {
                return defaultPeriodMillis;
            }
            Long intervalMillis = intervalByKey.get(key);
            if (intervalMillis == null) {
                int longestPrefixLen = 0;
                for (Map.Entry<String, Long> entry : intervalByKey.entrySet()) {
                    if (key.startsWith(entry.getKey()) && longestPrefixLen < entry.getKey().length()) {
                        longestPrefixLen = entry.getKey().length();
                        intervalMillis = entry.getValue();
                    }
                }
            }
            return intervalMillis == null ? defaultPeriodMillis : intervalMillis;
        }
    }
}
