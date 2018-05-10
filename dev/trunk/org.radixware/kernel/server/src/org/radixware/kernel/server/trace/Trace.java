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

import org.radixware.kernel.common.defs.utils.MlsProcessor;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.apache.commons.logging.LogFactory;
import org.radixware.kernel.common.defs.utils.ISeverityByCodeCalculator;
import org.radixware.kernel.common.enums.EEventContextType;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.trace.IRadixTrace;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.trace.TraceItem;
import org.radixware.kernel.common.trace.TraceProfile;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.instance.ArteStateWriter;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.instance.TraceProfileInfo;

/**
 * Трасса.
 *
 * Регистрирует подписчиков. Передает им сообщения о событиях в соответсвии с
 * настройками подписки. Реализует механизм регистрации событий в таблице БД
 * RDX_EVENTLOG
 *
 *
 *
 *
 */
public class Trace implements IRadixTrace {

    private static final boolean RECORD_CONTEXT_ENTER_STACK = SystemPropUtils.getBooleanSystemProp("rdx.trace.record.context.enter.stack", false);

    protected final MlsProcessor mlsProcessor;
    protected final Arte arte;
    protected final Instance instance;
    protected DbLog dbLog;
    private final List<TraceTarget> targets = new ArrayList<>(); // <Target>
    private long minSeverity = Long.MAX_VALUE;
    private final Stack<TraceContext> contextStack = new Stack<>();
    private String contextTypes = "";
    private String contextIds = "";
    private String normalizedContextStackAsStr = null;
    private final Map<String, TraceProfileInfo> desc2TraceProfileInfo = new HashMap<>();

    public static class Factory {

        public static Trace newInstance(final Instance instance, final Connection dbConnection, final MlsProcessor mlsProcessor, final LocalTracer tracer) {
            final Trace trace = new Trace(instance, mlsProcessor);
            trace.initDbLog(dbConnection, tracer);
            return trace;
        }
    }

    protected Trace(final Instance instance, final MlsProcessor mlsProcessor) {
        this.mlsProcessor = mlsProcessor;
        this.arte = null;
        this.instance = instance;
    }

    protected Trace(final Arte arte) {
        this.mlsProcessor = arte.getMlsProcessor();
        this.arte = arte;
        this.instance = arte.getInstance();
    }

    protected void initDbLog(final Connection dbConnection, final LocalTracer tracer) {
        dbLog = new DbLog(dbConnection, tracer, arte);
    }

    public DbLog getDbLog() {
        return dbLog;
    }

//************************************* TARGETS *******************************************
    @Deprecated
    /**
     * Use prototype with description
     */
    public Object addTargetBuffer(final String profile, final TraceBuffer buffer) {
        return addTargetBuffer(profile, buffer, null, false);
    }

    @Deprecated
    /**
     * Use prototype with description
     */
    public Object addTargetBuffer(final String profile, final TraceBuffer buffer, final boolean passive) {
        return addTargetBuffer(profile, buffer, null, passive);
    }

    public Object addTargetBuffer(final String profile, final TraceBuffer buffer, final String description) {
        return addTargetBuffer(profile, buffer, description, false);
    }

    public Object addTargetBuffer(final String profile, final TraceBuffer buffer, final String description, final boolean passive) {
        final String effectiveDesc = description == null ? ExceptionTextFormatter.getCurrentStackAsStr() : description;
        final TraceTarget t = new TraceTarget(new TraceProfile(profile), buffer, effectiveDesc, passive);
        if (!passive && minSeverity > t.getProfile().getMinSeverity()) {
            minSeverity = t.getProfile().getMinSeverity();

        }
        if (buffer == null && !passive) {
            registerTraceProfileInfo(profile, effectiveDesc, System.currentTimeMillis());
        }
        targets.add(t);
        return t;
    }

    private void registerTraceProfileInfo(final String profile, final String description, final long touchTimeMillis) {
        if (profile != null) {
            desc2TraceProfileInfo.put(description, new TraceProfileInfo(description, profile, touchTimeMillis));
        }
    }

    public List<TraceTarget> getTargets() {
        return targets;
    }

    @Deprecated
    /**
     * Use prototype with description
     */
    public Object addTargetLog(final String profile) {
        return addTargetLog(profile, null);
    }

    public Object addTargetLog(final String profile, final String description) {
        final String effectiveDesc = description == null ? ExceptionTextFormatter.getCurrentStackAsStr() : description;
        registerTraceProfileInfo(profile, effectiveDesc, System.currentTimeMillis());
        final TraceTarget t = new TraceTarget(new TraceProfile(profile), null, effectiveDesc, false);
        if (minSeverity > t.getProfile().getMinSeverity()) {
            minSeverity = t.getProfile().getMinSeverity();
        }
        targets.add(t);
        return t;
    }

    public void changeTargetProfile(final Object handle, final String profile) {
        if (targets.indexOf(handle) < 0) {
            throw new IllegalArgumentError("Unknown trace target");
        }
        ((TraceTarget) handle).setProfile(new TraceProfile(profile));
        registerTraceProfileInfo(profile, ((TraceTarget) handle).getDescription(), System.currentTimeMillis());
        calcMinSeverity();
    }

    public String getTargetProfile(final Object handle) {
        if (targets.indexOf(handle) < 0) {
            throw new IllegalArgumentError("Unknown trace target");
        }
        return ((TraceTarget) handle).getProfile().toString();
    }

    public boolean delTarget(final Object handle) {
        if (targets.remove((TraceTarget) handle)) {
            calcMinSeverity();
            return true;
        }
        return false;
    }

//************************************* PUT ************************************************	
//  For Arte trace
    public void put(final String code, final List<String> words) {
        put(code, words, false);
    }

    public void put(final String code, final List<String> words, final long timeMillis) {
        put(code, words, false, timeMillis);
    }

    public void put(final String code, final List<String> words, final boolean isSensitive) {
        put(code, words, isSensitive, -1);
    }

    public void put(final String code, final List<String> words, final boolean isSensitive, final long timeMillis) {
        put(mlsProcessor.getEventSeverityByCode(code), code, words, mlsProcessor.getEventSourceByCode(code), isSensitive, timeMillis);
    }

    @Override
    public void put(final EEventSeverity severity, final String mess, final EEventSource source) {
        put(severity, mess, source.getValue());
    }

    public void put(final Long severity, final String mess, final String source) {
        put(EEventSeverity.getForValue(severity), mess, source);
    }

    public void put(final EEventSeverity severity, final String mess, final String source) {
        put(severity, mess, source, false);
    }

    public void put(final EEventSeverity severity, final String mess, final String source, final boolean isSensitive) {
        put(severity, null, new ArrStr(mess), source, isSensitive);
    }

    @Override
    public void put(final EEventSeverity severity, final String code, final List<String> words, final String source) {
        put(severity, code, words, source, false);
    }

    public void put(final EEventSeverity severity, final String code, final List<String> words, final String source, final boolean isSensitive) {
        put(severity, code, words, source, isSensitive, -1);
    }

    public void put(final EEventSeverity severity, final String code, final List<String> words, final String source, final boolean isSensitive, final long timeMillis) {
        put(severity, code, words, source, isSensitive, timeMillis, false);
    }

    public void put(EEventSeverity severity, final String code, final List<String> words, final String source, final boolean isSensitive, final long timeMillis, final boolean flushToDbNow) {
        put(severity, code, words, source, isSensitive, timeMillis, flushToDbNow, null);
    }

    public void put(EEventSeverity severity, final String code, final List<String> words, final String source, final boolean isSensitive, final long timeMillis, final boolean flushToDbNow, final List<TraceContext> additionalContexts) {
        if (arte != null) {
            arte.yeld();
        }
        ArteStateWriter.gatherCurrentThreadState();
        if (targets.isEmpty()) {
            return;
        }
        final ISeverityByCodeCalculator severityPreprocessor = instance.getSeverityPreprocessor();
        if (severityPreprocessor != null && code != null) {
            EEventSeverity redefinedSeverity = null;
            try {
                redefinedSeverity = severityPreprocessor.getEventSeverityByCode(code);
            } catch (RuntimeException ex) {
                put(EEventSeverity.WARNING, "Error on event severity recalculation: " + ExceptionTextFormatter.throwableToString(ex), EEventSource.ARTE);
            }
            if (redefinedSeverity != null && !(source != null && source.startsWith(EEventSource.APP_AUDIT.getValue()) && redefinedSeverity.getValue() <= EEventSeverity.DEBUG.getValue())) {
                severity = redefinedSeverity;
            }
        }
        final long eventTimeMillis = timeMillis == -1 ? System.currentTimeMillis() : timeMillis;
        final TraceItem it = new TraceItem(mlsProcessor, severity, code, words, source, getNormalizedContextStackAsStr(additionalContexts), isSensitive, eventTimeMillis);
        boolean bDbLogIsOn = false;
        for (TraceTarget t : targets) {
            if (t.getProfile().itemMatch(it)) {
                if (t.buffer != null) {
                    t.buffer.put(it);
                } else {
                    bDbLogIsOn = true;
                }
            }
        }
        if (bDbLogIsOn && (!isSensitive || eventTimeMillis < instance.getSensitiveTracingFinishMillis())) {
            dbLog.put(severity, code, words, source, concatContextTypes(contextTypes, additionalContexts), concatContextIds(contextIds, additionalContexts), isSensitive, eventTimeMillis, flushToDbNow);
        }
    }

    private String concatContextTypes(String baseTypes, final List<TraceContext> additionalContexts) {
        if (additionalContexts == null || additionalContexts.isEmpty()) {
            return baseTypes;
        }

        for (TraceContext tc : additionalContexts) {
            baseTypes = appendContextType(baseTypes, tc.type);
        }
        return baseTypes;
    }

    private String concatContextIds(String baseIds, final List<TraceContext> additionalContexts) {
        if (additionalContexts == null || additionalContexts.isEmpty()) {
            return baseIds;
        }

        for (TraceContext tc : additionalContexts) {
            baseIds = appendContextType(baseIds, tc.id);
        }
        return baseIds;
    }

//************************************* CONTEXT ************************************************
    public void enterContext(final EEventContextType type, final Long id) {
        enterContext(type, id.toString());
    }

    public void enterContext(final String type, final Long id) {
        enterContext(type, id.toString());
    }

    public void enterContext(final EEventContextType type, final String id) {
        enterContext(type.getValue(), id);
    }

    public void enterContext(final String type, final String id) {
        normalizedContextStackAsStr = null;
        contextStack.push(new TraceContext(type, id, RECORD_CONTEXT_ENTER_STACK ? ExceptionTextFormatter.getCurrentStackAsStr() : null));
        contextTypes = appendContextType(contextTypes, type);
        contextIds = appendContextId(contextIds, id);
    }

    private String appendContextType(final String typeList, final String type) {
        return type + String.valueOf('\0') + typeList;
    }

    private String appendContextId(final String idList, final String id) {
        return id + String.valueOf('\0') + idList;
    }

    public void leaveContext(final EEventContextType type, final Long id) throws IllegalUsageError {
        leaveContext(type, id.toString());
    }

    public void leaveContext(final String type, final Long id) throws IllegalUsageError {
        leaveContext(type, id.toString());
    }

    public void leaveContext(final EEventContextType type, final String id) throws IllegalUsageError {
        leaveContext(type.getValue(), id);
    }

    public List<TraceContext> getContextStack() {
        return new ArrayList<>(contextStack);
    }

    public void leaveContext(final String type, final String id) throws IllegalUsageError {
        normalizedContextStackAsStr = null;
        if (contextStack.isEmpty()) {
            LogFactory.getLog(Trace.class).warn("trace context is empty on leave context [type=" + type + ", id=" + id + "]");
            return;
        }

        for (int i = contextStack.size() - 1; i >= 0; i--) {
            final TraceContext context = contextStack.get(i);
            if (context.type.equals(type) && context.id.equals(id)) {
                if (i != contextStack.size() - 1) {
                    LogFactory.getLog(Trace.class).warn("Leaving from not the top trace context: " + (new TraceContext(type, id)).toString() + ", top is " + contextStack.peek());
                }
                while (contextStack.size() > i) {
                    final TraceContext removedContext = contextStack.pop();
                    contextTypes = contextTypes.substring(removedContext.type.length() + 1);
                    contextIds = contextIds.substring(removedContext.id.length() + 1);
                }
                break;
            } else if (i == 0) {
                LogFactory.getLog(Trace.class).warn("Unable to leave trace context because it's not found in stack: " + (new TraceContext(type, id)).toString());
            }
        }
    }

    public String getNormalizedContextStackAsStr() {
        return getNormalizedContextStackAsStr(null);
    }

    public String getNormalizedContextStackAsStr(final List<TraceContext> additionalContexts) {
        if (normalizedContextStackAsStr == null) {
            final List<TraceContext> normalizedContext = new ArrayList<>(contextStack.size() + (additionalContexts == null ? 0 : additionalContexts.size()));
            appendContextsNormalized(normalizedContext, contextStack);
            appendContextsNormalized(normalizedContext, additionalContexts);
            final StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (TraceContext context : normalizedContext) {
                if (sb.length() > 1) {
                    sb.append(",");
                }
                sb.append(context.type).append("~").append(context.id);
            }
            sb.append("]");
            normalizedContextStackAsStr = sb.toString();
        }
        return normalizedContextStackAsStr;
    }

    private void appendContextsNormalized(final List<TraceContext> base, final List<TraceContext> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (TraceContext context : list) {
            if (!base.contains(context)) {
                base.add(context);
            }
        }
    }

    public String getContextStackAsStr() {
        if (!contextStack.isEmpty()) {
            final StringBuilder ctxStr = new StringBuilder();
            boolean bFirst = true;
            for (TraceContext ctxItem : contextStack) {
                if (bFirst) {
                    bFirst = false;
                } else {
                    ctxStr.append("->");
                }
                ctxStr.append(ctxItem.toString());
            }
            return ctxStr.toString();
        }
        return null;
    }

    public void clearContextStack() {
        contextStack.clear();
        contextTypes = "";
        contextIds = "";
        normalizedContextStackAsStr = null;
    }

    public void setDbConnection(final Connection connection) {
        dbLog.setDbConnection(connection);
    }

    public long getMinSeverity() {
        return minSeverity;
    }

    public final long getMinSeverity(final EEventSource eventSource) {
        return getMinSeverity(eventSource == null ? (String) null : eventSource.getValue());
    }

    public final long getMinSeverity(final String eventSource) {
        long ms = Long.MAX_VALUE;
        for (TraceTarget target : targets) {
            if (!target.isPassive()) {
                ms = Math.min(target.getProfile().getMinSeverity(eventSource), ms);
            }
        }
        return ms;
    }

    public void flush() {
        dbLog.flush();
        flushProfileInfos();
    }

    private void flushProfileInfos() {
        instance.registerTraceProfileInfos(desc2TraceProfileInfo.values());
        desc2TraceProfileInfo.clear();
    }

    private void calcMinSeverity() {
        minSeverity = Long.MAX_VALUE;
        for (TraceTarget target : targets) {
            if (!target.isPassive()) {
                minSeverity = Math.min(target.getProfile().getMinSeverity(), minSeverity);
            }
        }
    }
}
