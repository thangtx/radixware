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
package org.radixware.kernel.server.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import org.radixware.kernel.server.trace.ServerTrace;
import org.radixware.kernel.server.trace.Trace;
import org.radixware.kernel.server.trace.TraceProfiles;
import org.radixware.kernel.server.trace.TraceTarget;

public class DbOperationLogger {

    private static final boolean GLOBAL_LOG_PLAN = SystemPropUtils.getBooleanSystemProp("rdx.arte.log.sql.plan", false);
    private static final boolean GLOBAL_LOG_STACK = SystemPropUtils.getBooleanSystemProp("rdx.arte.log.sql.stack", false);
    private static final int PLAN_STORE_MILLIS = SystemPropUtils.getIntSystemProp("rdx.sql.plan.store.millis", 5000);
    private static final boolean MARK_ARTE_INACTIVE_ON_DB = SystemPropUtils.getBooleanSystemProp("rdx.mark.arte.inactive.on.db", false);
    private static final String NULL_PLAN = "";

    private String executingSql;
    private long executionStartMillis;
    private long executionEndMillis;
    private String planPart;
    private PreparedStatement getPlanQry;
    private final RadixConnection connection;
    private boolean logging = false;
    private int nesting = 0;

    public DbOperationLogger(RadixConnection connection) {
        this.connection = connection;
    }

    public void beforeDbOperation(final String sql) {
        nesting++;
        if (nesting == 1) {
            logging = connection.isDbLoggingAllowed();
            if (logging) {
                connection.setDbLoggingAllowed(false);
                executingSql = sql;
                executionStartMillis = System.currentTimeMillis();
                executionEndMillis = -1;
                planPart = null;
                tryArtePreprocess();
            }
        }
    }

    public String getExecutingSql() {
        return executingSql;
    }

    public void afterDbOperation() {
        try {
            if (nesting == 1 && logging) {
                try {
                    executionEndMillis = System.currentTimeMillis();
                    tryArtePostprocess();
                    final Arte arte = Arte.get();
                    final Trace arteTrace = arte == null ? null : arte.getTrace();
                    if (arteTrace != null) {
                        final List<TraceProfile> arteTargetProfiles = new ArrayList<>();
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
                        logIfNecessary(serverTrace, arteTrace, Collections.singletonList(serverTraceProfile.getGuiTraceProfileObj()), ServerTrace.ETraceDestination.GUI);
                        logIfNecessary(serverTrace, arteTrace, Collections.singletonList(serverTraceProfile.getFileTraceProfileObj()), ServerTrace.ETraceDestination.FILE);
                        if (arteTrace == null) {
                            logIfNecessary(serverTrace, arteTrace, Collections.singletonList(serverTraceProfile.getDbTraceProfileObj()), ServerTrace.ETraceDestination.DATABASE);
                        }
                    }
                } finally {
                    connection.setDbLoggingAllowed(true);
                }
            }
        } finally {
            nesting--;
        }
    }

    private void tryArtePreprocess() {
        final Arte arte = Arte.get();
        if (arte != null) {
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_CALL);
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
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_CALL);
        }
    }

    private void logIfNecessary(final ServerTrace serverTrace, final Trace arteTrace, final List<TraceProfile> profiles, ServerTrace.ETraceDestination destination) {
        boolean needLog = false;
        boolean logPlan = false;
        boolean logStack = false;
        if (profiles != null) {
            for (TraceProfile profile : profiles) {
                Object minLoggableDurationMillisObj = profile.getOption(EEventSource.ARTE_DB.getValue(), RadixTraceOptions.MIN_LOGGABLE_DB_OPERATION_DURATION_MILLIS);
                if (minLoggableDurationMillisObj instanceof Long && ((executionEndMillis - executionStartMillis) >= ((Long) minLoggableDurationMillisObj))) {
                    needLog = true;
                }
                logPlan = logPlan || profile.hasOption(EEventSource.ARTE_DB.getValue(), RadixTraceOptions.LOG_PLAN);
                logStack = logStack || profile.hasOption(EEventSource.ARTE_DB.getValue(), RadixTraceOptions.LOG_STACK);
            }
        }
        if (needLog) {
            if ((logPlan || GLOBAL_LOG_PLAN) && planPart == null) {
                planPart = "\n\n" + getPlan();
            }
            if (planPart == null || planPart.length() == 2) {
                planPart = "";
            }
            String stackPart = "";
            if (logStack || GLOBAL_LOG_STACK) {
                stackPart = "\n\nStack: \n" + ExceptionTextFormatter.getCurrentStackAsStr();
            }
            if (serverTrace != null) {
                serverTrace.put(EEventSeverity.DEBUG, (executionEndMillis - executionStartMillis) + " ms: " + executingSql + planPart + stackPart, null, null, EEventSource.ARTE_DB.getValue(), executionEndMillis, false, Collections.singletonList(destination));
            } else {
                arteTrace.put(EEventSeverity.DEBUG, null, new ArrStr((executionEndMillis - executionStartMillis) + " ms: " + executingSql + planPart + stackPart), EEventSource.ARTE_DB.getValue(), false, executionEndMillis);
            }
        }
    }

    private String getPlan() {
        final String cachedPlan = connection.getObjectCache().get(executingSql, String.class);
        if (cachedPlan != null) {
            return cachedPlan.equals(NULL_PLAN) ? cachedPlan : cachedPlan + "\n" + "Plan taken from cache";
        }
        final long startMillis = System.currentTimeMillis();
        final boolean oldMode = connection.setDbLoggingAllowed(false);
        try {
            if (getPlanQry == null || getPlanQry.isClosed()) {
                getPlanQry = connection.prepareStatement("SELECT * FROM table(DBMS_XPLAN.DISPLAY())");
            }
            try (Statement explainPlanStmt = connection.createStatement()) {
                explainPlanStmt.executeQuery("EXPLAIN PLAN FOR " + executingSql);
                final StringBuilder sb = new StringBuilder();
                try (ResultSet rs = getPlanQry.executeQuery()) {
                    while (rs.next()) {
                        sb.append(rs.getObject(1).toString()).append("\n");
                    }
                }
                connection.getObjectCache().putExpiringSinceCreation(executingSql, sb.toString(), PLAN_STORE_MILLIS / 1000);
                sb.append("\n").append(getComputationTimeString(startMillis));
                return sb.toString();
            }
        } catch (Exception ex) {
            LogFactory.getLog(DbOperationLogger.class).debug("Error on getting sql plan", ex);
            connection.getObjectCache().putExpiringSinceCreation(executingSql, NULL_PLAN, PLAN_STORE_MILLIS / 1000);
            return NULL_PLAN;
        } finally {
            connection.setDbLoggingAllowed(oldMode);
        }
    }

    private String getComputationTimeString(final long startMillis) {
        return "Plan was computed in " + (System.currentTimeMillis() - startMillis) + " ms";
    }
}
