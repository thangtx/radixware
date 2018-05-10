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
package org.radixware.kernel.server.instance;

import org.radixware.kernel.common.repository.DbConfiguration;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EKeyStoreType;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.kerberos.KrbServiceOptions;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.SrvRunParams;
import org.radixware.kernel.server.jdbc.AbstractDbQueries;
import org.radixware.kernel.server.trace.FileLogOptions;
import org.radixware.kernel.server.trace.TraceProfiles;
import org.radixware.kernel.server.types.ProfileStatisticEntry;
import org.radixware.kernel.server.units.UnitDescription;
import org.radixware.kernel.server.units.job.executor.JobExecutorUnit;
import org.radixware.kernel.server.utils.IPriorityResourceManager;

final public class InstanceDbQueries extends AbstractDbQueries {

    private final Instance instance;

    private final Object qryStartedDuplicatedUnitForPrimarySem = new Object();
    private PreparedStatement qryStartedDuplicatedUnitForPrimary = null;
    private static final String qryStartedDuplicatedUnitForPrimarySQL = "select u.id, i.id, i.title, u.selfchecktime, u.selfCheckTimeMillis, RDX_Utils.getUnixEpochMillis() curMillis from RDX_UNIT u, rdx_instance i where"
            + " u.started = 1"
            //+ " and (sysdate < u.selfchecktime + numtodsinterval(?, 'SECOND'))"
            + " and nvl(u.primaryUnitId, u.id) = ? and u.instanceId = i.id";

    private final Object qryPrimaryUnitSem = new Object();
    private PreparedStatement qryPrimaryUnit = null;
    private static final String qryPrimaryUnitSQL = "select nvl(primaryUnitId, id) from RDX_UNIT where id = ?";

    private final Object qryStartedExecutorSem = new Object();
    private PreparedStatement qryStartedExecutor = null;
    private static final String qryStartedExecutorSQL = "select u.id, i.id, i.title, u.selfchecktime, u.selfCheckTimeMillis, RDX_UTILS.getUnixEpochMillis() from RDX_UNIT u, rdx_instance i where"
            + " u.type = " + EUnitType.JOB_EXECUTOR.getValue()
            + " and u.started = 1 and u.instanceId = i.id"
            //            + " and (sysdate < u.selfchecktime + numtodsinterval(?, 'SECOND')) "
            + " and decode(?, 0, decode(i.targetExecutorId, u.id, 0, 1), decode(i.targetExecutorId, decode(u.id, ?, u.id, -1), 1, 0)) > 0"
            + " and (i.aadcMemberId = ? or ? is null)";

    private PreparedStatement qrySaveProfileLog;
    private static final String qrySaveProfileLogSQL = "insert into rdx_profilerlog "
            + "columns(id, instanceId, periodEndTime, sectionId, context, duration, minduration, maxduration, count) "
            + "values(sqn_rdx_profilerlogid.nextval, ?, ?, ?, ?, ?, ?, ?, ?)";

    private final Object qryStartedUnitExistsSem = new Object();
    private PreparedStatement qryStartedUnits = null;
    private static final String qryStartedUnitsSQL = "select u.id, i.id, i.title, u.selfchecktime, u.selfCheckTimeMillis, RDX_UTILS.getUnixEpochMillis() curMillis from RDX_UNIT u, rdx_instance i where"
            + " i.id = u.instanceId and u.started = 1 and u.type = ?"
            + " and (i.aadcMemberId = ? or ? is null)";
    //"select u.id, i.id, i.title from RDX_UNIT u, rdx_instance i where u.started = 1 and (sysdate < u.selfchecktime + numtodsinterval(?, 'SECOND')) and u.type = ? and i.id = u.instanceId and rownum < 2";

    private final Object qrySetUnitPostponedStateSem = new Object();
    private PreparedStatement qrySetUnitPostponedState = null;
    private static final String qrySetUnitPostponedStateSQL = "update RDX_UNIT set postponed=? where id=?";

    private PreparedStatement qryOptions = null;
    private static final String qryOptionsSQL = "select i.autoActualizeVer, i.memoryCheckPeriodSec, "
            + "i.arteinstlivetime, i.lowarteinstcount, i.higharteinstcount, "
            + "i.arteCntAboveNormal, i.arteCntHigh, i.arteCntVeryHigh, i.arteCntCritical, "
            + "s.profilePeriodSec, s.easKrbPrincName, i.scpName, "
            + "i.sapId, i.httpProxy, i.httpsProxy, i.keyTabPath, RDX_JS_JOB.calcThreadPriorityMap() as tpMap,"
            + "nvl(i.maxActiveArteNormal, i.higharteinstcount) maxActiveArteNormal, "
            + "nvl(i.maxActiveArteAboveNormal, nvl(i.arteCntAboveNormal, 0)) maxActiveArteAboveNormal, "
            + "nvl(i.maxActiveArteHigh, nvl(i.arteCntHigh, 0)) maxActiveArteHigh, "
            + "nvl(i.maxActiveArteVeryHigh, nvl(i.arteCntVeryHigh, 0)) maxActiveArteVeryHigh, "
            + "nvl(i.maxActiveArteCritical, nvl(i.arteCntCritical, 0)) maxActiveArteCritical, i.useActiveArteLimits, "
            + "i.autoRestartDelaySec, "
            + "s.aadcMemberId aadcSysMemberId, s.aadcTestedMemberId aadcSysTestedMemberId, "
            + "i.aadcMemberId aadcInstMemberId, i.aadcDgAddress, "
            + "s.aadcUnlockTables aadcUnlockTables, s.aadcCommitedLockExp aadcCommitedLockExp, "
            + "nvl(decode(i.instStateGatherPeriodSec, -1, s.instStateGatherPeriodSec, i.instStateGatherPeriodSec), 0) instStateGatherPeriodSec, "
            + "nvl(decode(i.instStateForcedGatherPeriodSec, -1, s.instStateForcedGatherPeriodSec, i.instStateForcedGatherPeriodSec), 0) instStateForcedGatherPeriodSec, "
            + "nvl(i.instStateForcedGatherPeriodSec, s.instStateForcedGatherPeriodSec) instStateForcedGatherPeriodSec, "
            + "s.aadcAffinityTimeoutSec "
            + "from rdx_instance i, rdx_system s where i.id = ? and s.id=1";

    private PreparedStatement qrySetDbStartedState = null;
    private static final String qrySetDbStartedStateSQL = "update rdx_instance set started = ?, osPid=?, startTimeMillis=? where id = ?";

    private PreparedStatement qryUpdateAadcState = null;
    private static final String qryUpdateAadcStateSQL = "update rdx_instance set aadcMyScn=(select current_scn from GV$database), aadcMyTime=sysTimestamp where id = ?";
    private PreparedStatement qryAadcLag = null;
    private static final String qryAadcLagSQL = "select extract(day from d)*24*60*60*1000 + extract(hour from d)*60*60*1000 + extract(minute from d)*60*1000 + round(extract(second from d)*1000) from (select systimestamp - max(aadcMyTime) as d from RDX_Instance where aadcMemberId <> ? and started=1)";

    private PreparedStatement qryReadTraceOptions = null;
    private static final String qryReadTraceOptionsSQL = "select "
            + "i.dbTraceProfile, i.fileTraceProfile, i.guiTraceProfile,"
            + "i.traceFilesDir, i.maxTraceFileSizeKb, i.maxTraceFileCnt, i.rotateTraceFilesDaily,"
            + "s.enableSensitiveTrace, s.writeContextToFile "
            + "from rdx_instance i, rdx_system s where i.id  = ? and s.id=1";

    private static final String qryOsCommandSQL = "select startOsCommand, stopOsCommand from rdx_instance where id = ?";

    private static final String qryGetKeystoreTypeSQL = "select keystoretype from rdx_instance where id = ?";

    private static final String qryGetKeystorePathSQL = "select keystorepath from rdx_instance where id = ?";

    private static final String qryGetKerberosOptionsSQL = "select i.keyTabPath, s.easKrbPrincName from rdx_instance i, rdx_system s where i.id = ? and s.id=1";

//    private PreparedStatement qryReadSapId;
    private PreparedStatement qryDbIAmStillAlive = null;
    private static final String qryDbIAmStillAliveSQL = "update rdx_instance set started=1, selfchecktime = systimestamp, selfCheckTimeMillis=RDX_UTILS.getUnixEpochMillis() where id = ?";

    private PreparedStatement qryGetSeverityByCodeMap = null;
    private static final String qryGetSeverityByCodeMapSQL = "select eventCode, eventSeverity from RDX_EventSeverity";

    private PreparedStatement qryReadGgStandbyMode = null;
    private static final String qryReadGgStandbyModeSQL = "select ggStandbyMode from rdx_system where id=1";

    private PreparedStatement qryCurDbTrans = null;
    private static final String qryCurDbTransSql = "select XIDUSN || '.' || XIDSLOT || '.' || XIDSQN from GV$TRANSACTION";
    private PreparedStatement qryCurDbSessions = null;
    private static final String qryCurDbSessionsSql = "select sid || '.' || serial# from V$SESSION";

    private PreparedStatement qryReadDdsVersion = null;
    private static final String qryReadDdsVersionSQL = "select layeruri, version, upgradedate, upgradetoversion, upgradestarttime, prevcompatiblever from rdx_ddsversion";

    private PreparedStatement qryWriteVersionInfo = null;
    private static final String qryWriteVersionInfoSQL = "update rdx_instance set kernelVersion=?, appVersion=?, revision=? where id=?";
    
    private PreparedStatement qryWriteHostInfo = null;
    private static final String qryWriteHostInfoSQL = "update rdx_instance set cpuCoreCount = ?, hostName = ?, hostIpAddresses = ? where id = ?";

    private PreparedStatement qryClearOutdatedUnitStartedState = null;
    private static final String qryClearOutdatedUnitStartedStateSQL = "update rdx_unit set started=0 where id = ? and SELFCHECKTIMEMILLIS=?";

    private InstanceDbQueries() {
        this.instance = null;
    }

    InstanceDbQueries(final Instance instance) {
        this.instance = instance;
    }

    public void writeVersionInfo(final VersionInfo versionInfo) throws SQLException {
        if (versionInfo == null) {
            return;
        }
        final Connection dbConnection = instance.getDbConnection();
        if (dbConnection == null || dbConnection.isClosed()) {
            return;
        }
        if (qryWriteVersionInfo == null || qryWriteVersionInfo.isClosed() || qryWriteVersionInfo.getConnection().isClosed()) {
            qryWriteVersionInfo = dbConnection.prepareStatement(qryWriteVersionInfoSQL);
            qryWriteVersionInfo.setLong(4, instance.getId());
        }

        qryWriteVersionInfo.setString(1, versionInfo.getKernelVersion());
        qryWriteVersionInfo.setString(2, versionInfo.getAppVersion());
        qryWriteVersionInfo.setLong(3, versionInfo.getRevision());
        qryWriteVersionInfo.executeUpdate();
    }
    
    public void writeHostInfo(int cpuCoreCount, String hostName, List<String> hostIpAddresses) throws SQLException {
        final Connection dbConnection = instance.getDbConnection();
        if (dbConnection == null || dbConnection.isClosed()) {
            return;
        }
        if (qryWriteHostInfo == null || qryWriteHostInfo.isClosed() || qryWriteHostInfo.getConnection().isClosed()) {
            qryWriteHostInfo = dbConnection.prepareStatement(qryWriteHostInfoSQL);
        }
        qryWriteHostInfo.setInt(1, cpuCoreCount);
        qryWriteHostInfo.setString(2, hostName);
        qryWriteHostInfo.setString(3, StringUtils.join(hostIpAddresses, ","));
        qryWriteHostInfo.setLong(4, instance.getId());
        qryWriteHostInfo.executeUpdate();
    }

    public DdsVersionInfo readDdsVersionInfo() throws SQLException {
        final Connection dbConnection = instance.getDbConnection();
        if (dbConnection == null || dbConnection.isClosed()) {
            return null;
        }
        if (qryReadDdsVersion == null || qryReadDdsVersion.isClosed() || qryReadDdsVersion.getConnection().isClosed()) {
            qryReadDdsVersion = dbConnection.prepareStatement(qryReadDdsVersionSQL);
        }
        final List<DdsVersionInfoItem> items = new ArrayList<>();
        try (final ResultSet rs = qryReadDdsVersion.executeQuery()) {
            while (rs.next()) {
                items.add(new DdsVersionInfoItem(
                        rs.getString("layeruri"),
                        rs.getString("version"),
                        rs.getTimestamp("upgradedate"),
                        rs.getString("upgradetoversion"),
                        rs.getTimestamp("upgradestarttime"),
                        rs.getString("prevcompatiblever")
                ));
            }
        }
        return new DdsVersionInfo(items);
    }

    public DbConfiguration readDbConfiguration() throws SQLException {
        final Connection dbConnection = instance.getDbConnection();
        if (dbConnection == null) {
            return null;
        }
        return DbConfiguration.read(dbConnection);
    }

    public boolean readGgStandbyMode() throws SQLException {
        if (qryReadGgStandbyMode == null || qryReadGgStandbyMode.isClosed() || qryReadGgStandbyMode.getConnection().isClosed()) {
            qryReadGgStandbyMode = instance.getDbConnection().prepareStatement(qryReadGgStandbyModeSQL);
        }
        try (final ResultSet rs = qryReadGgStandbyMode.executeQuery()) {
            rs.next();
            return rs.getBoolean(1);
        }
    }

    public Set<String> readCurDbTrans() throws SQLException {
        if (qryCurDbTrans == null || qryCurDbTrans.isClosed() || qryCurDbTrans.getConnection().isClosed()) {
            qryCurDbTrans = instance.getDbConnection().prepareStatement(qryCurDbTransSql);
        }
        final Set<String> curDbTrans = new HashSet<>();
        try (ResultSet rs = qryCurDbTrans.executeQuery()) {
            while (rs.next()) {
                curDbTrans.add(rs.getString(1));
            }
        }
        return curDbTrans;
    }

    public Set<String> readCurDbSessions() throws SQLException {
        if (qryCurDbSessions == null || qryCurDbSessions.isClosed() || qryCurDbSessions.getConnection().isClosed()) {
            qryCurDbSessions = instance.getDbConnection().prepareStatement(qryCurDbSessionsSql);
        }
        final Set<String> curDbSessions = new HashSet<>();
        try (ResultSet rs = qryCurDbSessions.executeQuery()) {
            while (rs.next()) {
                curDbSessions.add(rs.getString(1));
            }
        }
        return curDbSessions;
    }

    public void updateAadcState() throws SQLException {
        final Connection dbConnection = instance.getDbConnection();
        if (dbConnection == null) {
            return;
        }
        if (qryUpdateAadcState == null) {
            qryUpdateAadcState = prepareQuery(qryUpdateAadcStateSQL);
            qryUpdateAadcState.setInt(1, instance.getId());
        }
        qryUpdateAadcState.executeUpdate();
        dbConnection.commit();
    }

    public int getAadcLag(int myMemberId) throws SQLException {
        synchronized (qryAadcLagSQL) {
            if (qryAadcLag == null) {
                qryAadcLag = prepareQuery(qryAadcLagSQL);
                qryAadcLag.setInt(1, myMemberId);
            }
            try (ResultSet rs = qryAadcLag.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public Map<String, EEventSeverity> getSeverityByCodeMap() throws SQLException {
        final Connection dbConnection = instance.getDbConnection();

        if (dbConnection == null) {
            return null;
        }
        if (qryGetSeverityByCodeMap == null) {
            qryGetSeverityByCodeMap = prepareQuery(dbConnection, qryGetSeverityByCodeMapSQL);
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
        return getOsCommand("startOsCommand");
    }

    final String getStopOsCommand() throws SQLException {
        return getOsCommand("stopOsCommand");
    }

    private String getOsCommand(final String cmd) throws SQLException {
        final Connection dbConnection = instance.getDbConnection();
        if (dbConnection == null) {
            return null;
        } else {
            try (final PreparedStatement qry = dbConnection.prepareStatement(qryOsCommandSQL)) {
                qry.setInt(1, instance.getId());
                try (final ResultSet rs = qry.executeQuery()) {
                    return rs.next() ? rs.getString(cmd) : null;
                }
            }
        }
    }

    /**
     *
     * @param unitType
     * @return id of the currently running unit of this type, or -1 if there is
     * no such unit.
     * @throws SQLException
     */
    UnitDescription getStartedUnitId(final Long unitType, final Integer aadcMemberId) throws SQLException {
        synchronized (qryStartedUnitExistsSem) {
            if (qryStartedUnits == null) {
                qryStartedUnits = prepareQuery(qryStartedUnitsSQL);
            }
            qryStartedUnits.setObject(1, unitType);
            if (aadcMemberId != null) {
                qryStartedUnits.setInt(2, aadcMemberId);
                qryStartedUnits.setInt(3, aadcMemberId);
            } else {
                qryStartedUnits.setNull(2, java.sql.Types.INTEGER);
                qryStartedUnits.setNull(3, java.sql.Types.INTEGER);
            }
            try (final ResultSet rs = qryStartedUnits.executeQuery()) {
                while (rs.next()) {
                    long unitId = rs.getLong(1);
                    Timestamp checkTime = rs.getTimestamp(4);
                    final long checkTimeMillis = rs.getLong(5);
                    if (!rs.wasNull()) {
                        checkTime = new Timestamp(checkTimeMillis);
                    }
                    if (instance.isUnitRunning(rs.getLong(1), rs.getLong(6), checkTime)) {
                        return new UnitDescription(unitId, rs.getLong(2), rs.getString(3));
                    }
                }
            }
            return null;
        }
    }

    void setDbStartedState(final boolean bStarted) throws SQLException {
        final Connection dbConnection = instance.getDbConnection();
        if (dbConnection == null) {
            return;
        }
        if (qrySetDbStartedState == null) {
            qrySetDbStartedState = prepareQuery(qrySetDbStartedStateSQL);
            qrySetDbStartedState.setInt(4, instance.getId());
        }
        qrySetDbStartedState.setInt(1, bStarted ? 1 : 0);
        qrySetDbStartedState.setLong(2, instance.getPidNumber());
        qrySetDbStartedState.setLong(3, instance.getStartTimeMillis());
        qrySetDbStartedState.executeUpdate();

        if (instance.getAadcManager() != null && instance.getAadcManager().isInAadc()) {
            updateAadcState();
        }

        dbConnection.commit();
    }

    void setUnitStartPostponedInDb(final long unitId, final boolean postponed) throws SQLException {
        synchronized (qrySetUnitPostponedStateSem) {
            if (qrySetUnitPostponedState == null) {
                qrySetUnitPostponedState = prepareQuery(qrySetUnitPostponedStateSQL);
            }
            qrySetUnitPostponedState.setBoolean(1, postponed);
            qrySetUnitPostponedState.setLong(2, unitId);
            qrySetUnitPostponedState.executeUpdate();
            qrySetUnitPostponedState.getConnection().commit();
        }
    }

    void clearOutdatedUnitStartedState(final long unitId, final long lastWrittenSelfCheckTimeMillis) throws SQLException {
        final Connection dbConnection = instance.getDbConnection();
        if (dbConnection == null) {
            return;
        }
        if (qryClearOutdatedUnitStartedState == null) {
            qryClearOutdatedUnitStartedState = prepareQuery(qryClearOutdatedUnitStartedStateSQL);
        }
        qryClearOutdatedUnitStartedState.setLong(1, unitId);
        qryClearOutdatedUnitStartedState.setLong(2, lastWrittenSelfCheckTimeMillis);
        qryClearOutdatedUnitStartedState.executeUpdate();
        qryClearOutdatedUnitStartedState.getConnection().commit();
    }

    UnitDescription getStartedDuplicatedUnitForPrimary(final long primaryUnitId) throws SQLException {
        synchronized (qryStartedDuplicatedUnitForPrimarySem) {
            if (qryStartedDuplicatedUnitForPrimary == null) {
                qryStartedDuplicatedUnitForPrimary = prepareQuery(qryStartedDuplicatedUnitForPrimarySQL);
            }
            qryStartedDuplicatedUnitForPrimary.setLong(1, primaryUnitId);
            try (final ResultSet rs = qryStartedDuplicatedUnitForPrimary.executeQuery()) {
                while (rs.next()) {
                    long unitId = rs.getLong(1);
                    Timestamp checkTimestamp = rs.getTimestamp(4);
                    final long checkMillis = rs.getLong(5);
                    if (rs.wasNull()) {
                        checkTimestamp = new Timestamp(checkMillis);
                    }
                    if (instance.isUnitRunning(rs.getLong(1), rs.getLong(6), checkTimestamp)) {
                        return new UnitDescription(unitId, rs.getLong(2), rs.getString(3));
                    }
                }
            }
            return null;
        }
    }

    UnitDescription getStartedDuplicatedExecutorUnitId(final JobExecutorUnit unit, final Integer aadcMemberId) throws SQLException {
        synchronized (qryStartedExecutorSem) {
            if (qryStartedExecutor == null) {
                qryStartedExecutor = prepareQuery(qryStartedExecutorSQL);
            }
            qryStartedExecutor.setLong(1, unit.isLocal() ? 1 : 0);
            qryStartedExecutor.setLong(2, unit.getId());
            if (aadcMemberId != null) {
                qryStartedExecutor.setInt(3, aadcMemberId);
                qryStartedExecutor.setInt(4, aadcMemberId);
            } else {
                qryStartedExecutor.setNull(3, java.sql.Types.INTEGER);
                qryStartedExecutor.setNull(4, java.sql.Types.INTEGER);
            }
            try (ResultSet rs = qryStartedExecutor.executeQuery()) {
                while (rs.next()) {
                    long unitId = rs.getLong(1);
                    Timestamp checkTime = rs.getTimestamp(4);
                    final long checkMillis = rs.getLong(5);
                    if (!rs.wasNull()) {
                        checkTime = new Timestamp(checkMillis);
                    }
                    if (instance.isUnitRunning(rs.getLong(1), rs.getLong(6), checkTime)) {
                        return new UnitDescription(unitId, rs.getLong(2), rs.getString(3));
                    }
                }
            }
            return null;
        }
    }

    long getPrimaryUnitId(final long unitId) throws SQLException {
        synchronized (qryPrimaryUnitSem) {
            if (qryPrimaryUnit == null) {
                qryPrimaryUnit = prepareQuery(qryPrimaryUnitSQL);
            }
            qryPrimaryUnit.setLong(1, unitId);
            try (ResultSet rs = qryPrimaryUnit.executeQuery()) {
                if (rs.next()) {
                    final long result = rs.getLong(1);

                    return rs.wasNull() ? -1 : result;
                } else {//unit itself
                    return unitId;
                }
            }
        }
    }

    private PreparedStatement prepareQuery(final String query) throws SQLException {
        return prepareQuery(instance.getDbConnection(), query);
    }

    private void closeQry(final PreparedStatement qry) {
        try {
            if (qry != null) {
                qry.close();
            }
        } catch (SQLException e) {
            instance.getTrace().put(EEventSeverity.WARNING, Messages.ERR_ON_DB_CONNECTION_CLOSE + ": \n" + ExceptionTextFormatter.exceptionStackToString(e), null, null, EEventSource.INSTANCE, false);
        }
    }

    @Override
    public void prepareAll() throws SQLException {
        prepareAll(instance.getDbConnection());
    }

    @Override
    public void closeAll() {
        closeQry(qrySetDbStartedState);
        qrySetDbStartedState = null;
        closeQry(qryReadTraceOptions);
        qryReadTraceOptions = null;
        closeQry(qryDbIAmStillAlive);
        qryDbIAmStillAlive = null;
        closeQry(qrySaveProfileLog);
        qrySaveProfileLog = null;
        closeQry(qryOptions);
        qryOptions = null;
        closeQry(qryGetSeverityByCodeMap);
        qryGetSeverityByCodeMap = null;
        closeQry(qryReadGgStandbyMode);
        qryReadGgStandbyMode = null;
        closeQry(qryCurDbTrans);
        qryCurDbTrans = null;
        closeQry(qryReadDdsVersion);
        qryReadDdsVersion = null;
        closeQry(qryUpdateAadcState);
        qryUpdateAadcState = null;
        closeQry(qryAadcLag);
        qryAadcLag = null;
        closeQry(qryClearOutdatedUnitStartedState);
        qryClearOutdatedUnitStartedState = null;
        synchronized (qryStartedUnitExistsSem) {
            closeQry(qryStartedUnits);
            qryStartedUnits = null;
        }
        synchronized (qryStartedExecutorSem) {
            closeQry(qryStartedExecutor);
            qryStartedExecutor = null;
        }
        synchronized (qryStartedDuplicatedUnitForPrimarySem) {
            closeQry(qryStartedDuplicatedUnitForPrimary);
            qryStartedDuplicatedUnitForPrimary = null;
        }
        synchronized (qryPrimaryUnitSem) {
            closeQry(qryPrimaryUnit);
            qryPrimaryUnit = null;
        }
        synchronized (qrySetUnitPostponedStateSem) {
            closeQry(qrySetUnitPostponedState);
            qrySetUnitPostponedState = null;
        }
    }

    TraceOptions readTraceOptions() throws SQLException, InterruptedException {
        final String instanceLogFilePrefix = "instance_#";
        try {
            final Connection dbConnection = instance.getDbConnection();
            if (dbConnection != null) {
                if (qryReadTraceOptions == null) {
                    qryReadTraceOptions = prepareQuery(qryReadTraceOptionsSQL);
                    qryReadTraceOptions.setInt(1, instance.getId());
                }
                try (final ResultSet rs = qryReadTraceOptions.executeQuery()) {
                    if (rs.next()) {
                        final TraceProfiles profiles = new TraceProfiles(rs.getString("dbTraceProfile"), rs.getString("fileTraceProfile"), rs.getString("guiTraceProfile"));
                        final FileLogOptions logOpt = new FileLogOptions(
                                new File(new File(rs.getString("traceFilesDir")), instanceLogFilePrefix + instance.getId()),
                                instanceLogFilePrefix + instance.getId(),
                                rs.getInt("maxTraceFileSizeKb") * 1024,
                                rs.getInt("maxTraceFileCnt"),
                                rs.getBoolean("rotateTraceFilesDaily") || SrvRunParams.isRotateFileLogsDaily(),
                                rs.getBoolean("writeContextToFile"));
                        return new TraceOptions(profiles, logOpt, rs.getBoolean("enableSensitiveTrace"));
                    }
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
                                5, SrvRunParams.isRotateFileLogsDaily(), false),
                        false);
    }
//alive qry	

    void dbIAmStillAlive() throws SQLException {
        final Connection dbConnection = instance.getDbConnection();
        if (dbConnection == null) {
            return;
        }
        if (qryDbIAmStillAlive == null) {
            qryDbIAmStillAlive = prepareQuery(dbConnection, qryDbIAmStillAliveSQL);
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
            try (final PreparedStatement qry = dbConnection.prepareStatement(qryGetKeystoreTypeSQL)) {
                qry.setInt(1, instance.getId());
                try (final ResultSet rs = qry.executeQuery()) {
                    if (rs.next()) {
                        return EKeyStoreType.getForValue(rs.getLong("keystoretype"));
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
            try (final PreparedStatement qry = dbConnection.prepareStatement(qryGetKeystorePathSQL)) {
                qry.setInt(1, instance.getId());
                try (final ResultSet rs = qry.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("keystorepath");
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
            try (PreparedStatement qry = dbConnection.prepareStatement(qryGetKerberosOptionsSQL)) {
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
                    qryOptions = prepareQuery(dbConnection, qryOptionsSQL);
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
                                new IPriorityResourceManager.Options(
                                        rs.getInt("maxActiveArteNormal"),
                                        rs.getInt("maxActiveArteAboveNormal"),
                                        rs.getInt("maxActiveArteHigh"),
                                        rs.getInt("maxActiveArteVeryHigh"),
                                        rs.getInt("maxActiveArteCritical")),
                                rs.getInt("aadcSysMemberId"),
                                rs.getInt("aadcSysTestedMemberId"),
                                rs.getInt("aadcInstMemberId"),
                                rs.getString("aadcDgAddress"),
                                rs.getString("aadcUnlockTables"), rs.getInt("aadcCommitedLockExp"),
                                rs.getInt("autoRestartDelaySec"),
                                rs.getInt("instStateGatherPeriodSec"),
                                rs.getInt("instStateForcedGatherPeriodSec"),
                                rs.getInt("aadcAffinityTimeoutSec")
                        );
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
        return new InstanceOptions(60000, true, Long.valueOf(10000), 2, 10, 0, 0, 0, 0, 60l, null, 0, null, null, new KrbEasOptions(null, null), null, false, null, 0, 0, 0, null, null, 600, 0, 5, 10, 60); //default options
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
                    qrySaveProfileLog = prepareQuery(dbConnection, qrySaveProfileLogSQL);
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

    private static class KrbEasOptions extends KrbServiceOptions {

        public KrbEasOptions(final String keyTabPath, final String principalName) {
            super(keyTabPath, principalName, false);
        }

        @Override
        protected String getDefaultPrincipalName() {
            return "HTTP/eas.radixware.org";
        }
    }
}
