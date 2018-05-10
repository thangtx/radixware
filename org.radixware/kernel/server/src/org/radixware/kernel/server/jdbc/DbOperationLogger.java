/*
 * Copyright (c) 2008-2018, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.logging.LogFactory;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.trace.IRadixTrace;
import org.radixware.kernel.common.trace.RadixTraceOptions;
import org.radixware.kernel.common.trace.TraceProfile;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.trace.ServerTrace;
import org.radixware.kernel.server.trace.Trace;
import org.radixware.kernel.server.trace.TraceProfiles;
import org.radixware.kernel.server.trace.TraceTarget;

public class DbOperationLogger extends AbstractDbQueries implements DBOperationLoggerInterface {

    private static final boolean GLOBAL_LOG_PLAN = SystemPropUtils.getBooleanSystemProp("rdx.arte.log.sql.plan", false);
    private static final boolean GLOBAL_LOG_STACK = SystemPropUtils.getBooleanSystemProp("rdx.arte.log.sql.stack", false);
    private static final int PLAN_STORE_MILLIS = SystemPropUtils.getIntSystemProp("rdx.sql.plan.store.millis", 5000);
    private static final boolean MARK_ARTE_INACTIVE_ON_DB = SystemPropUtils.getBooleanSystemProp("rdx.mark.arte.inactive.on.db", false);
    private static final String NULL_PLAN = "";

    private String executingSql;
    private Statement currentStatement;
    private long executionStartMillis;
    private long executionEndMillis;
    private String planPart;
    private String operationDescription;

    private static final String getPlanQrySQL = "SELECT * FROM table(DBMS_XPLAN.DISPLAY())";
    private PreparedStatement getPlanQry = null;

    private final Connection connection;
    private boolean logging = false;
    private int nesting = 0;
    private EDbOperationType operationType;

    private final LruRegexCache regexCache = new LruRegexCache();

    private DbOperationLogger() {
        this.connection = null;
    }

    public DbOperationLogger(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void beforeDbOperation(final String sql) {
        beforeDbOperation(sql, EDbOperationType.SELECT);
    }

    @Override
    public void beforeDbOperation(final String sql, final EDbOperationType operationType) {
        try {
            Arte arte = Arte.get();
            if (arte != null) {
                arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_CALL);
            }
        } catch (Exception ex) {
            LogFactory.getLog(DbOperationLogger.class).warn("Exception while entering db_call timing section", ex);
        }
        nesting++;
        if (nesting == 1) {
            logging = ((RadixConnection) connection).isDbLoggingAllowed();
            if (logging) {
                ((RadixConnection) connection).setDbLoggingAllowed(false);
                executingSql = sql;
                executionStartMillis = System.currentTimeMillis();
                executionEndMillis = -1;
                planPart = null;
                this.operationType = operationType;
                ((RadixConnection) connection).setLastSql(sql);
                tryArtePreprocess();
            }
        }
    }

    @Override
    public String getExecutingSql() {
        return executingSql;
    }

    @Override
    public void prepareAll() throws SQLException {
        prepareAll(connection);
    }

    @Override
    public void afterDbOperation() {
        try {
            Arte arte = Arte.get();
            if (arte != null) {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_CALL, arte.getProfiler().isProfilingOn() && nesting == 1 && logging ? compressSql(executingSql) : null);
            }
        } catch (Exception ex) {
            LogFactory.getLog(DbOperationLogger.class).warn("Exception while leaving db_call timing section", ex);
        }

        try {
            if (nesting == 1 && logging) {
                try {
                    operationDescription = ((RadixConnection) connection).getOperationDescription();
                    executionEndMillis = System.currentTimeMillis();
                    tryArtePostprocess();
                    final Arte arte = Arte.get();
                    final Trace arteTrace = arte == null ? null : arte.getTrace();
                    if (arteTrace != null) {
                        final List<TraceProfile> arteTargetProfiles = new ArrayList<>(20);
                        for (TraceTarget target : arteTrace.getTargets()) {
                            if (target.getProfile() != null) {
                                arteTargetProfiles.add(target.getProfile());
                            }
                        }
                        logIfNecessary(null, arteTrace, arteTargetProfiles, null);
                    }

                    final IRadixTrace traceObj = IRadixTrace.Lookup.findInstance(null);//from thread
                    final ServerTrace serverTrace = traceObj instanceof ServerTrace ? (ServerTrace) traceObj : null;
                    if (serverTrace != null) {
                        TraceProfiles serverTraceProfile = serverTrace.getProfiles();
                        logIfNecessary(serverTrace, null, Collections.singletonList(serverTraceProfile.getFileTraceProfileObj()), ServerTrace.ETraceDestination.FILE);
                        if (arteTrace == null) {
                            logIfNecessary(serverTrace, null, Collections.singletonList(serverTraceProfile.getGuiTraceProfileObj()), ServerTrace.ETraceDestination.GUI);
                            logIfNecessary(serverTrace, null, Collections.singletonList(serverTraceProfile.getDbTraceProfileObj()), ServerTrace.ETraceDestination.DATABASE);
                        }
                    }
                } finally {
                    operationDescription = null;
                    ((RadixConnection) connection).setDbLoggingAllowed(true);
                }
            }
        } finally {
            nesting--;
        }
    }

    @Override
    public IRadixTrace getRadixTrace() {
        return Instance.get().getTrace();
    }

    @Override
    public ARTEReducedInterface getArteInterface() {
        return Arte.get();
    }

    @Override
    public void throwDatabaseError(SQLException message) {
        throw new DatabaseError(message);
    }

    @Override
    public void throwDatabaseError(String message, Throwable cause) {
        throw new DatabaseError(message, cause);
    }

    @Override
    public void setOperationDescription(String operationDescription) {
        this.operationDescription = operationDescription;
    }

    private void tryArtePreprocess() {
        final Arte arte = Arte.get();
        if (arte != null) {
            if (MARK_ARTE_INACTIVE_ON_DB) {
                arte.markInactive();
            }
        }
    }

    private void tryArtePostprocess() {
        final Arte arte = Arte.get();
        if (arte != null) {
            if (MARK_ARTE_INACTIVE_ON_DB) {
                arte.markActive();
            }
        }
    }

    private String compressSql(final String sql) {
        if (sql == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder(sql.length());
        for (int i = 0; i < sql.length(); i++) {
            char c = sql.charAt(i);
            if (c != '\n') {
                sb.append(c);
            } else {
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    private void logIfNecessary(final ServerTrace serverTrace, final Trace arteTrace, final List<TraceProfile> profiles, ServerTrace.ETraceDestination destination) {
        boolean needLog = false;
        boolean logPlan = false;
        boolean logStack = false;
        if (profiles != null) {
            for (TraceProfile profile : profiles) {
                boolean needLogByThis = false;
                final EEventSeverity severity = profile.getEventSourceSeverity(EEventSource.ARTE_DB.getValue());
                if (severity == EEventSeverity.DEBUG) {
                    Object minLoggableDurationMillisObj = profile.getOption(EEventSource.ARTE_DB.getValue(), RadixTraceOptions.MIN_LOGGABLE_DB_OPERATION_DURATION_MILLIS);
                    if (minLoggableDurationMillisObj instanceof Long && ((executionEndMillis - executionStartMillis) >= ((Long) minLoggableDurationMillisObj))) {
                        final Object patternObj = profile.getOption(EEventSource.ARTE_DB.getValue(), RadixTraceOptions.DB_QRY_MASK);
                        needLogByThis = qryMatches(executingSql, patternObj == null ? null : patternObj.toString());
                    }
                    if (needLogByThis) {
                        needLog = true;
                        logPlan = logPlan || profile.hasOption(EEventSource.ARTE_DB.getValue(), RadixTraceOptions.LOG_PLAN);
                        logStack = logStack || profile.hasOption(EEventSource.ARTE_DB.getValue(), RadixTraceOptions.LOG_STACK);
                    }
                }
            }
        }

        if (needLog) {
            final String contextsPart = "\n\nTrace contexts: " + (arteTrace != null ? arteTrace.getContextStackAsStr() : serverTrace.getContextStackAsStr());
            final String descriptionPart = operationDescription == null ? "" : "\n\nOperation description: " + operationDescription;

            if ((logPlan || GLOBAL_LOG_PLAN) && planPart == null && (operationType == null || operationType.isExplainPlanPossible())) {
                planPart = "\n\n" + getPlan();
            }
            if (planPart == null || planPart.length() == 2) {
                planPart = "";
            }
            String stackPart = "";
            if (logStack || GLOBAL_LOG_STACK) {
                stackPart = "\n\nThread: '" + Thread.currentThread().getName() + "'\nStack: \n" + ExceptionTextFormatter.getCurrentStackAsStr();
            }
            final String mess = (executionEndMillis - executionStartMillis) + " ms: " + (operationType == EDbOperationType.SELECT || operationType == null ? "" : "[" + operationType.name() + "] ") + executingSql + descriptionPart + contextsPart + planPart + stackPart;
            if (serverTrace != null) {
                serverTrace.put(EEventSeverity.DEBUG, mess, null, null, EEventSource.ARTE_DB.getValue(), executionEndMillis, false, Collections.singletonList(destination));
            } else {
                arteTrace.put(EEventSeverity.DEBUG, null, new ArrStr(mess), EEventSource.ARTE_DB.getValue(), false, executionEndMillis);
            }
        }
    }

    private String getPlan() {
        final String cachedPlan = ((RadixConnection) connection).getObjectCache().get(executingSql, String.class
        );
        if (cachedPlan
                != null) {
            return cachedPlan.equals(NULL_PLAN) ? cachedPlan : cachedPlan + "\n" + "Plan taken from cache";
        }
        final long startMillis = System.currentTimeMillis();
        final boolean oldMode = ((RadixConnection) connection).setDbLoggingAllowed(false);

        try {
            if (getPlanQry == null || getPlanQry.isClosed()) {
                getPlanQry = connection.prepareStatement(getPlanQrySQL);
            }
            try (Statement explainPlanStmt = connection.createStatement()) {
                explainPlanStmt.executeQuery("EXPLAIN PLAN FOR " + executingSql);
                final StringBuilder sb = new StringBuilder();
                try (ResultSet rs = getPlanQry.executeQuery()) {
                    while (rs.next()) {
                        sb.append(rs.getObject(1).toString()).append("\n");
                    }
                }
                ((RadixConnection) connection).getObjectCache().putExpiringSinceCreation(executingSql, sb.toString(), PLAN_STORE_MILLIS / 1000);
                sb.append("\n").append(getComputationTimeString(startMillis));
                return sb.toString();
            }
        } catch (Exception ex) {
            LogFactory.getLog(DbOperationLogger.class).debug("Error on getting sql plan", ex);
            ((RadixConnection) connection).getObjectCache().putExpiringSinceCreation(executingSql, NULL_PLAN, PLAN_STORE_MILLIS / 1000);
            return NULL_PLAN;
        } finally {
            ((RadixConnection) connection).setDbLoggingAllowed(oldMode);
        }
    }

    private boolean qryMatches(final String qry, final String qryMask) {
        if (qry == null) {
            return false;
        }
        if (qryMask == null || qryMask.isEmpty() || "*".equals(qryMask)) {
            return true;
        }
        final String regex = ("\\Q" + qryMask + "\\E").replace("*", "\\E.*\\Q");
        Pattern pattern = regexCache.get(regex);
        if (pattern == null) {
            pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            regexCache.put(regex, pattern);
        }
        return pattern.matcher(qry).matches();
    }

    private String getComputationTimeString(final long startMillis) {
        return "Plan was computed in " + (System.currentTimeMillis() - startMillis) + " ms";

    }

    private static class LruRegexCache extends LinkedHashMap<String, Pattern> {

        private static final int MAX_SIZE = 16;

        public LruRegexCache() {
            super(MAX_SIZE + 1, 0.75f, true);
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Pattern> eldest) {
            return size() > MAX_SIZE;
        }

    }
}
