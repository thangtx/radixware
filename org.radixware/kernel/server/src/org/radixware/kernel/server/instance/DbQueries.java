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

package org.radixware.kernel.server.instance;

import org.radixware.kernel.common.repository.DbConfiguration;
import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EKeyStoreType;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.kerberos.KrbServiceOptions;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.IDbQueries;
import org.radixware.kernel.server.SrvRunParams;
import org.radixware.kernel.server.trace.FileLogOptions;
import org.radixware.kernel.server.trace.TraceProfiles;
import org.radixware.kernel.server.types.ProfileStatisticEntry;
import org.radixware.kernel.server.units.Unit;
import org.radixware.kernel.server.units.UnitDescription;
import org.radixware.kernel.server.units.job.executor.JobExecutorUnit;
import org.radixware.kernel.server.utils.PriorityResourceManager;


final class DbQueries implements IDbQueries {

    private static class KrbEasOptions extends KrbServiceOptions {

        public KrbEasOptions(final String keyTabPath, final String principalName) {
            super(keyTabPath, principalName, false);
        }

        @Override
        protected String getDefaultPrincipalName() {
            return "HTTP/eas.radixware.org";
        }
    }
    private final Object qryStartedMqUnitSem = new Object();
    private PreparedStatement qryStartedMqUnit;
    private final Instance instance;
    private final Object qryParentMqUnitSem = new Object();
    private PreparedStatement qryParentMqUnit;
    private final Object qryParentSchedulerSem = new Object();
    private PreparedStatement qryParentScheduler;
    private final Object qryStartedSchedulerSem = new Object();
    private PreparedStatement qryStartedScheduler;
    private final Object qryStartedExecutorSem = new Object();
    private PreparedStatement qryStartedExecutor;
    private PreparedStatement qrySaveProfileLog;
    private final Object qryStartedUnitExistsSem = new Object();
    private PreparedStatement qryStartedUnitExists;
    private PreparedStatement qryOptions;
    private PreparedStatement qrySetDbStartedState;
    private PreparedStatement qryReadTraceOptions;
    private PreparedStatement qryReadSapId;
    private CallableStatement qryDbIAmStillAlive;
    private PreparedStatement qryGetSeverityByCodeMap;
    private PreparedStatement qryReadDbConfiguration;

    DbQueries(final Instance instance) {
        this.instance = instance;
    }

    public DbConfiguration readDbConfiguration() throws SQLException {
        final Connection dbConnection = instance.getDbConnection();
        if (dbConnection == null) {
            return null;
        }
        return DbConfiguration.read(dbConnection);
    }

    public Map<String, EEventSeverity> getSeverityByCodeMap() throws SQLException {
        final Connection dbConnection = instance.getDbConnection();
        if (dbConnection == null) {
            return null;
        }
        if (qryGetSeverityByCodeMap == null) {
            qryGetSeverityByCodeMap = dbConnection.prepareStatement("select eventCode, eventSeverity from RDX_EventSeverity");
        }
        final Map<String, EEventSeverity> codeToSeverity = new HashMap<>();
        try (ResultSet rs = qryGetSeverityByCodeMap.executeQuery()) {
            while (rs.next()) {
                final String severityStr = rs.getString("eventSeverity");
                try {
                    codeToSeverity.put(rs.getString("eventCode"), EEventSeverity.getForValue(Long.valueOf(severityStr)));
                } catch (NoConstItemWithSuchValueError ex) {
                    instance.getTrace().put(EEventSeverity.WARNING, "Illegal value of the severity field in EventSource to EventCode mapping table: " + severityStr, EEventSource.INSTANCE);
                }
            }
        }
        return codeToSeverity;
    }

    final String getStartOsCommand() throws SQLException {
        return getOsCommand("start");
    }

    final String getStopOsCommand() throws SQLException {
        return getOsCommand("stop");
    }

    private String getOsCommand(final String cmd) throws SQLException {
        final Connection dbConnection = instance.getDbConnection();
        if (dbConnection == null) {
            return null;
        }
        final PreparedStatement qry = dbConnection.prepareStatement("select " + cmd + "OsCommand from rdx_instance where id = ?");
        try {
            qry.setInt(1, instance.getId());
            final ResultSet rs = qry.executeQuery();
            try {
                if (rs.next()) {
                    return rs.getString(1);
                }
                return null;
            } finally {
                rs.close();
            }
        } finally {
            qry.close();
        }
    }

    /**
     *
     * @param unitType
     * @return id of the currently running unit of this type, or -1 if there is
     * no such unit.
     * @throws SQLException
     */
    UnitDescription getStartedUnitId(final Long unitType) throws SQLException {
        synchronized (qryStartedUnitExistsSem) {
            if (qryStartedUnitExists == null) {
                final Connection dbConnection = instance.getDbConnection();
                if (dbConnection == null) {
                    throw new SQLException("Database connection is not established");
                }

                qryStartedUnitExists = dbConnection.prepareStatement("select u.id, i.id, i.title from RDX_UNIT u, rdx_instance i where u.started = 1 and (sysdate < u.selfchecktime + numtodsinterval(?, 'SECOND')) and u.type = ? and i.id = u.instanceId and rownum < 2");
                final int threeChecksSeconds = Unit.DB_I_AM_ALIVE_TIMEOUT_MILLIS / 1000;
                qryStartedUnitExists.setInt(1, threeChecksSeconds);
            }
            qryStartedUnitExists.setObject(2, unitType);
            final ResultSet rs = qryStartedUnitExists.executeQuery();
            try {
                if (rs.next()) {
                    return new UnitDescription(rs.getLong(1), rs.getLong(2), rs.getString(3));
                } else {
                    return null;
                }
            } finally {
                rs.close();
            }
        }
    }

    void setDbStartedState(final boolean bStarted) throws SQLException {
        final Connection dbConnection = instance.getDbConnection();
        if (dbConnection == null) {
            return;
        }
        if (qrySetDbStartedState == null) {
            qrySetDbStartedState = dbConnection.prepareStatement("update rdx_instance set started = ? where id = ?");
            qrySetDbStartedState.setInt(2, instance.getId());
        }
        qrySetDbStartedState.setInt(1, bStarted ? 1 : 0);
        qrySetDbStartedState.executeUpdate();
        dbConnection.commit();
        //if (qrySetDbStartedState.executeUpdate() == 0)
        //	instance.traceError(Messages.ERR_IN_DB_QRY);
    }

    UnitDescription getStartedDuplicatedScheduler(final long mainSchedulerId) throws SQLException {
        synchronized (qryStartedSchedulerSem) {
            if (qryStartedScheduler == null) {
                final Connection dbConnection = instance.getDbConnection();
                if (dbConnection == null) {
                    throw new SQLException("Database connection is not established");
                }
                qryStartedScheduler = dbConnection.prepareStatement("select u.id, i.id, i.title from RDX_UNIT u left join RDX_JS_JOBSCHEDULERUNIT js on u.id = js.id, rdx_instance i where u.type = " + EUnitType.JOB_SCHEDULER.getValue() + " and u.started = 1 and (sysdate < u.selfchecktime + numtodsinterval(?, 'SECOND')) and nvl(js.parentId, u.id) = ? and u.instanceId = i.id");
                final int threeChecksSeconds = Unit.DB_I_AM_ALIVE_TIMEOUT_MILLIS / 1000;
                qryStartedScheduler.setInt(1, threeChecksSeconds);
            }
            qryStartedScheduler.setLong(2, mainSchedulerId);
            try (ResultSet rs = qryStartedScheduler.executeQuery()) {
                if (rs.next()) {
                    return new UnitDescription(rs.getLong(1), rs.getLong(2), rs.getString(3));
                } else {
                    return null;
                }
            }
        }
    }

    UnitDescription getStartedDuplicatedMqUnit(final long mqUnitId) throws SQLException {
        synchronized (qryStartedMqUnitSem) {
            if (qryStartedMqUnit == null) {
                final Connection dbConnection = instance.getDbConnection();
                if (dbConnection == null) {
                    throw new SQLException("Database connection is not established");
                }
                qryStartedMqUnit = dbConnection.prepareStatement("select u.id, i.id, i.title from RDX_UNIT u left join RDX_FALLBACKMQHANDLER fmq on u.id = fmq.unitId, rdx_instance i where u.type = " + EUnitType.MQ_HANDLER.getValue() + " and u.started = 1 and (sysdate < u.selfchecktime + numtodsinterval(?, 'SECOND')) and nvl(fmq.mainUnitId, u.id) = ? and u.instanceId = i.id");
                final int threeChecksSeconds = Unit.DB_I_AM_ALIVE_TIMEOUT_MILLIS / 1000;
                qryStartedMqUnit.setInt(1, threeChecksSeconds);
            }
            qryStartedMqUnit.setLong(2, mqUnitId);
            try (ResultSet rs = qryStartedMqUnit.executeQuery()) {
                if (rs.next()) {
                    return new UnitDescription(rs.getLong(1), rs.getLong(2), rs.getString(3));
                } else {
                    return null;
                }
            }
        }
    }
    
    UnitDescription getStartedDuplicatedExecutorUnitId(final JobExecutorUnit unit) throws SQLException {
        synchronized (qryStartedExecutorSem) {
            if (qryStartedExecutor == null) {
                final Connection dbConnection = instance.getDbConnection();
                if (dbConnection == null) {
                    throw new SQLException("Database connection is not established");
                }
                qryStartedExecutor = dbConnection.prepareStatement("select u.id, i.id, i.title from RDX_UNIT u, rdx_instance i where u.type = " + EUnitType.JOB_EXECUTOR.getValue() + " and u.started = 1 and (sysdate < u.selfchecktime + numtodsinterval(?, 'SECOND')) and u.instanceId = i.id and decode(?, 0, decode(i.targetExecutorId, u.id, 0, 1), decode(i.targetExecutorId, decode(u.id, ?, u.id, -1), 1, 0)) > 0 order by u.selfchecktime desc");
                final int threeChecksSeconds = Unit.DB_I_AM_ALIVE_TIMEOUT_MILLIS / 1000;
                qryStartedExecutor.setInt(1, threeChecksSeconds);
            }
            qryStartedExecutor.setLong(2, unit.isLocal() ? 1 : 0);
            qryStartedExecutor.setLong(3, unit.getId());
            try (ResultSet rs = qryStartedExecutor.executeQuery()) {
                if (rs.next()) {
                    return new UnitDescription(rs.getLong(1), rs.getLong(2), rs.getString(3));
                } else {
                    return null;
                }
            }
        }
    }

    long getParentSchedulerId(final long schedulerId) throws SQLException {
        synchronized (qryParentSchedulerSem) {
            if (qryParentScheduler == null) {
                final Connection dbConnection = instance.getDbConnection();
                if (dbConnection == null) {
                    throw new SQLException("Database connection is not established");
                }
                qryParentScheduler = dbConnection.prepareStatement("select parentId from RDX_JS_JOBSCHEDULERUNIT where id=?");
            }
            qryParentScheduler.setLong(1, schedulerId);
            try (ResultSet rs = qryParentScheduler.executeQuery()) {
                if (rs.next()) {//this is a fallback scheduler
                    final long result = rs.getLong(1);
                    if (rs.wasNull()) {
                        return -1;
                    }
                    return result;
                } else {//main scheduler itself
                    return schedulerId;
                }
            }
        }
    }

    long getParentMqUnitId(final long mqUnitId) throws SQLException {
        synchronized (qryParentMqUnitSem) {
            if (qryParentMqUnit == null) {
                final Connection dbConnection = instance.getDbConnection();
                
                if (dbConnection == null) {
                    throw new SQLException("Database connection is not established");
                }
                else {
                    qryParentMqUnit = dbConnection.prepareStatement("select mainUnitId from RDX_FALLBACKMQHANDLER where unitId = ?");
                }
            }
            qryParentMqUnit.setLong(1, mqUnitId);
            try (ResultSet rs = qryParentMqUnit.executeQuery()) {
                if (rs.next()) {//this is a fallback MqUnit
                    final long result = rs.getLong(1);
                    
                    if (rs.wasNull()) {
                        return -1;
                    }
                    else {
                        return result;
                    }
                } else {//main scheduler itself
                    return mqUnitId;
                }
            }
        }
    }
    
    private void closeQry(final PreparedStatement qry) {
        try{if (qry != null) {
                qry.close();
            }
        } catch (SQLException e) {
            instance.getTrace().put(EEventSeverity.WARNING, Messages.ERR_ON_DB_CONNECTION_CLOSE + ": \n" + ExceptionTextFormatter.exceptionStackToString(e), null, null, EEventSource.INSTANCE, false);
        }
    }

    @Override
    public void closeAll() {
        closeQry(qrySetDbStartedState);
        qrySetDbStartedState = null;
        closeQry(qryReadTraceOptions);
        qryReadTraceOptions = null;
        closeQry(qryDbIAmStillAlive);
        qryDbIAmStillAlive = null;
        closeQry(qryReadSapId);
        qryReadSapId = null;
        closeQry(qrySaveProfileLog);
        qrySaveProfileLog = null;
        closeQry(qryOptions);
        qryOptions = null;
        closeQry(qryGetSeverityByCodeMap);
        qryGetSeverityByCodeMap = null;
        closeQry(qryReadDbConfiguration);
        qryReadDbConfiguration = null;
        synchronized (qryParentMqUnitSem) {
            closeQry(qryParentMqUnit);
            qryParentMqUnit = null;
        }
        synchronized (qryParentSchedulerSem) {
            closeQry(qryParentScheduler);
            qryParentScheduler = null;
        }
        synchronized (qryStartedUnitExistsSem) {
            closeQry(qryStartedUnitExists);
            qryStartedUnitExists = null;
        }
        synchronized (qryStartedSchedulerSem) {
            closeQry(qryStartedScheduler);
            qryStartedScheduler = null;
        }
        synchronized (qryStartedExecutorSem) {
            closeQry(qryStartedExecutor);
            qryStartedExecutor = null;
        }
        synchronized (qryStartedMqUnitSem) {
            closeQry(qryStartedMqUnit);
            qryStartedMqUnit = null;
        }
    }

    TraceOptions readTraceOptions() throws SQLException, InterruptedException {
        final String instanceLogFilePrefix = "instance_#";
        try {
            final Connection dbConnection = instance.getDbConnection();
            if (dbConnection != null) {
                if (qryReadTraceOptions == null) {
                    qryReadTraceOptions = dbConnection.prepareStatement(
                            "select "
                            + "i.dbTraceProfile, i.fileTraceProfile, i.guiTraceProfile,"
                            + "i.traceFilesDir, i.maxTraceFileSizeKb, i.maxTraceFileCnt, i.rotateTraceFilesDaily,"
                            + "s.enableSensitiveTrace "
                            + "from rdx_instance i, rdx_system s where i.id  = ? and s.id=1");
                    qryReadTraceOptions.setInt(1, instance.getId());
                }
                final ResultSet rs = qryReadTraceOptions.executeQuery();
                try {
                    if (rs.next()) {
                        final TraceProfiles profiles = new TraceProfiles(rs.getString("dbTraceProfile"), rs.getString("fileTraceProfile"), rs.getString("guiTraceProfile"));
                        final FileLogOptions logOpt = new FileLogOptions(
                                new File(new File(rs.getString("traceFilesDir")), instanceLogFilePrefix + instance.getId()),
                                instanceLogFilePrefix + instance.getId(),
                                rs.getInt("maxTraceFileSizeKb") * 1024,
                                rs.getInt("maxTraceFileCnt"),
                                rs.getBoolean("rotateTraceFilesDaily") || SrvRunParams.isRotateFileLogsDaily());
                        return new TraceOptions(profiles, logOpt, rs.getBoolean("enableSensitiveTrace"));
                    }
                } finally {
                    rs.close();
                }
            }
        } catch (Throwable e) {
            if (e instanceof InterruptedException || Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            if (e instanceof SQLException) {
                throw (SQLException) e;
            }
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            instance.getTrace().put(EEventSeverity.ERROR, Messages.ERR_ON_TRACE_OPTION_READING + ": \n" + exStack, Messages.MLS_ID_ERR_ON_TRACE_OPTION_READING, new ArrStr(instance.getFullTitle(), exStack), EEventSource.INSTANCE, false);
        }
        return //default options
                new TraceOptions(
                TraceProfiles.DEFAULT,
                new FileLogOptions(
                new File(new File("./logs"), instanceLogFilePrefix + instance.getId()),
                instanceLogFilePrefix + instance.getId(),
                1024 * 1024,
                5, SrvRunParams.isRotateFileLogsDaily()),
                false);
    }
//alive qry	

    void dbIAmStillAlive() throws SQLException {
        final Connection dbConnection = instance.getDbConnection();
        if (dbConnection == null) {
            return;
        }
        if (qryDbIAmStillAlive == null) {
            qryDbIAmStillAlive = dbConnection.prepareCall("update rdx_instance set started=1, selfchecktime = systimestamp where id = ?");
            qryDbIAmStillAlive.setInt(1, instance.getId());
        }
        qryDbIAmStillAlive.execute();
        dbConnection.commit();
    }

    final EKeyStoreType getKeystoreType() throws SQLException, InterruptedException {
        try {
            final Connection dbConnection = instance.getDbConnection();
            if (dbConnection == null) {
                return null;
            }
            final PreparedStatement qry = dbConnection.prepareStatement("select keystoretype from rdx_instance where id = ?");
            try {
                qry.setInt(1, instance.getId());
                final ResultSet rs = qry.executeQuery();
                try {
                    if (rs.next()) {
                        return EKeyStoreType.getForValue(rs.getLong("keystoretype"));
                    }
                    throw new IllegalUsageError("Unknown instance #" + String.valueOf(instance.getId()));
                } finally {
                    rs.close();
                }
            } finally {
                qry.close();
            }
        } catch (Throwable e) {
            if (e instanceof InterruptedException || Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            if (e instanceof SQLException) {
                throw (SQLException) e;
            }
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            instance.getTrace().put(EEventSeverity.ERROR, Messages.ERR_CANT_READ_SERVICE_OPTIONS + ": \n" + exStack, Messages.MLS_ID_ERR_CANT_READ_SERVICE_OPTIONS, new ArrStr(instance.getFullTitle(), exStack), EEventSource.INSTANCE, false);
        }
        return null;
    }

    final String getKeystorePath() throws SQLException, InterruptedException {
        try {
            final Connection dbConnection = instance.getDbConnection();
            if (dbConnection == null) {
                return null;
            }
            final PreparedStatement qry = dbConnection.prepareStatement("select keystorepath from rdx_instance where id = ?");
            try {
                qry.setInt(1, instance.getId());
                final ResultSet rs = qry.executeQuery();
                try {
                    if (rs.next()) {
                        return rs.getString("keystorepath");
                    }
                    throw new IllegalUsageError("Unknown instance #" + String.valueOf(instance.getId()));
                } finally {
                    rs.close();
                }
            } finally {
                qry.close();
            }
        } catch (Throwable e) {
            if (e instanceof InterruptedException || Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            if (e instanceof SQLException) {
                throw (SQLException) e;
            }
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            instance.getTrace().put(EEventSeverity.ERROR, Messages.ERR_CANT_READ_SERVICE_OPTIONS + ": \n" + exStack, Messages.MLS_ID_ERR_CANT_READ_SERVICE_OPTIONS, new ArrStr(instance.getFullTitle(), exStack), EEventSource.INSTANCE, false);
        }
        return null;
    }

    final KrbServiceOptions readKerberosOptions() throws InterruptedException {
        try {
            final Connection dbConnection = instance.getDbConnection();
            if (dbConnection == null) {
                return null;
            }
            final String queryText =
                    "select i.keyTabPath, s.easKrbPrincName from rdx_instance i, rdx_system s where i.id = ? and s.id=1";
            try (PreparedStatement qry = dbConnection.prepareStatement(queryText)) {
                qry.setInt(1, instance.getId());
                try (ResultSet rs = qry.executeQuery()) {
                    if (rs.next()) {
                        final String keyTabPath = rs.getString("keyTabPath");
                        final String spn = rs.getString("easKrbPrincName");
                        return new KrbEasOptions(keyTabPath, spn);
                    }
                    throw new IllegalUsageError("Unknown instance #" + String.valueOf(instance.getId()));
                }
            }
        } catch (Throwable e) {
            if (e instanceof InterruptedException || Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            instance.getTrace().put(EEventSeverity.ERROR, Messages.ERR_CANT_READ_KERBEROS_OPTIONS + ": \n" + exStack, Messages.MLS_ID_ERR_CANT_READ_KERBEROS_OPTIONS, new ArrStr(instance.getFullTitle(), exStack), EEventSource.INSTANCE, false);
        }
        return null;
    }

    final InstanceOptions readOptions() throws SQLException, InterruptedException {
        try {
            final Connection dbConnection = instance.getDbConnection();
            if (dbConnection != null) {
                if (qryOptions == null) {
                    qryOptions = dbConnection.prepareStatement(
                            "select i.autoActualizeVer, i.memoryCheckPeriodSec, "
                            + "i.arteinstlivetime, i.lowarteinstcount, i.higharteinstcount, "
                            + "i.arteCntAboveNormal, i.arteCntHigh, i.arteCntVeryHigh, i.arteCntCritical, "
                            + "s.profilePeriodSec, s.easKrbPrincName, i.scpName, "
                            + "i.sapId, i.httpProxy, i.httpsProxy, i.keyTabPath, RDX_JS_JOB.calcThreadPriorityMap() as tpMap,"
                            + "nvl(i.maxActiveArteNormal, i.higharteinstcount) maxActiveArteNormal, "
                            + "nvl(i.maxActiveArteAboveNormal, nvl(i.arteCntAboveNormal, 0)) maxActiveArteAboveNormal, "
                            + "nvl(i.maxActiveArteHigh, nvl(i.arteCntHigh, 0)) maxActiveArteHigh, "
                            + "nvl(i.maxActiveArteVeryHigh, nvl(i.arteCntVeryHigh, 0)) maxActiveArteVeryHigh, "
                            + "nvl(i.maxActiveArteCritical, nvl(i.arteCntCritical, 0)) maxActiveArteCritical, "
                            + "i.useActiveArteLimits "
                            + "from rdx_instance i, rdx_system s where i.id = ? and s.id=1");
                    qryOptions.setInt(1, instance.getId());
                }
                try (ResultSet rs = qryOptions.executeQuery()) {
                    if (rs.next()) {
                        final long memCheckSec = rs.getLong("memoryCheckPeriodSec");
                        final Long memCheckMillis = rs.wasNull() ? null : Long.valueOf(memCheckSec * 1000);
                        Long arteInstLiveTimeMin = rs.getLong("arteinstlivetime");
                        if (rs.wasNull()) {
                            arteInstLiveTimeMin = null;
                        }
                        int minArteInstCount = rs.getInt("lowarteinstcount");
                        final int maxTotalArteCount = rs.getInt("higharteinstcount")
                                + rs.getInt("arteCntAboveNormal")
                                + rs.getInt("arteCntHigh")
                                + rs.getInt("arteCntVeryHigh")
                                + rs.getInt("arteCntCritical");
                        if (minArteInstCount > maxTotalArteCount) {
                            minArteInstCount = maxTotalArteCount;
                        }
                        return new InstanceOptions(
                                rs.getLong("profilePeriodSec") * 1000,
                                rs.getBoolean("autoActualizeVer"),
                                memCheckMillis,
                                minArteInstCount,
                                rs.getInt("higharteinstcount"),
                                rs.getInt("arteCntAboveNormal"),
                                rs.getInt("arteCntHigh"),
                                rs.getInt("arteCntVeryHigh"),
                                rs.getInt("arteCntCritical"),
                                arteInstLiveTimeMin,
                                rs.getString("scpName"),
                                rs.getLong("sapId"),
                                rs.getString("httpProxy"),
                                rs.getString("httpsProxy"),
                                new KrbEasOptions(rs.getString("keyTabPath"),
                                rs.getString("easKrbPrincName")),
                                rs.getString("tpMap"),
                                rs.getBoolean("useActiveArteLimits"),
                                new PriorityResourceManager.Options(
                                rs.getInt("maxActiveArteNormal"),
                                rs.getInt("maxActiveArteAboveNormal"),
                                rs.getInt("maxActiveArteHigh"),
                                rs.getInt("maxActiveArteVeryHigh"),
                                rs.getInt("maxActiveArteCritical")));
                    }
                    throw new IllegalUsageError("Unknown instance #" + String.valueOf(instance.getId()));
                }
            }
        } catch (Throwable e) {
            if (e instanceof InterruptedException || Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            if (e instanceof SQLException) {
                throw (SQLException) e;
            }
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            instance.getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + ": \n" + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(exStack), EEventSource.INSTANCE, false);
        }
        return new InstanceOptions(60000, true, Long.valueOf(10000), 2, 10, 0, 0, 0, 0, 60l, null, 0, null, null, new KrbEasOptions(null, null), null, false, null); //default options
    }

    /**
     * @NotThreadsafe("Should be called from instance thread only")
     *
     * @param e
     */
    void save(final Collection<ProfileStatisticEntry> lst) {
        final Timestamp time = new Timestamp(System.currentTimeMillis());
        while (!instance.isShuttingDown()) {
            try {
                final Connection dbConnection = instance.getDbConnection();
                if (dbConnection == null) {
                    return;
                }
                if (qrySaveProfileLog == null) {
                    qrySaveProfileLog = dbConnection.prepareStatement(
                            "insert into rdx_profilerlog "
                            + "columns(id, instanceId, periodEndTime, sectionId, context, duration, minduration, maxduration, count) "
                            + "values(sqn_rdx_profilerlogid.nextval, ?, ?, ?, ?, ?, ?, ?, ?)");
                    qrySaveProfileLog.setLong(1, instance.getId());
                }
                qrySaveProfileLog.setTimestamp(2, time);
                for (ProfileStatisticEntry e : lst) {
                    qrySaveProfileLog.setString(3, e.getSectionId());
                    qrySaveProfileLog.setString(4, e.getContext());
                    qrySaveProfileLog.setLong(5, e.getDurationNanos());
                    qrySaveProfileLog.setLong(6, e.getMinDurationNanos());
                    qrySaveProfileLog.setLong(7, e.getMaxDurationNanos());
                    qrySaveProfileLog.setLong(8, e.getCount());
                    qrySaveProfileLog.executeUpdate();
                }
                dbConnection.commit();
                return;
            } catch (SQLException ex) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(ex);
                instance.getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + ":\n" + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(exStack), EEventSource.INSTANCE, false);
                try {
                    instance.restoreDbConnection();
                } catch (InterruptedException ex1) {
                    return;
                }
            }
        }
    }
}
