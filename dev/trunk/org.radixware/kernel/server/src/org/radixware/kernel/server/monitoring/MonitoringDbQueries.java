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

package org.radixware.kernel.server.monitoring;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.trace.IRadixTrace;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.jdbc.AbstractDbQueries;

/**
 * This class is not thread safe and supposed to be used from main thread of the
 * instance or unit
 *
 */
public class MonitoringDbQueries extends AbstractDbQueries {

    private Connection connection;
    private final IRadixTrace trace;
    private final EEventSource eventSource;
    
    private PreparedStatement unitMetricQry = null;
    private static final String unitMetricQrySQL = "select id, period, unitId from RDX_SM_METRICTYPE where kind = ? and ( unitId = ? or unitId is null ) and enabled > 0";
    
    private PreparedStatement unitMetricStateIdQry = null;
    private static final String unitMetricStateIdQrySQL = "select id from RDX_SM_METRICSTATE where typeId = ? and unitId = ?";
    
    private PreparedStatement timingSectionMetricStateIdQry = null;
    private static final String timingSectionMetricStateIdQrySQL = "select RDX_SM_METRICSTATE.id from RDX_SM_METRICSTATE inner join RDX_SM_METRICTYPE "
                                                                 + "on RDX_SM_METRICSTATE.TYPEID = RDX_SM_METRICTYPE.ID "
                                                                 + "where RDX_SM_METRICSTATE.typeId = ? "
                                                                 + "and RDX_SM_METRICSTATE.instanceId = ? "
                                                                 + "and timingSection = ?";
    
    private PreparedStatement instanceMetricQry = null;
    private static final String instanceMetricQrySQL = "select id, period, instanceId from RDX_SM_METRICTYPE where kind = ? and ( instanceId = ? or instanceId is null ) and enabled > 0";
    
    private PreparedStatement instanceMetricStateIdQry = null;
    private static final String instanceMetricStateIdQrySQL = "select id from RDX_SM_METRICSTATE where typeId = ? and instanceId = ?";
    
    private PreparedStatement instServiceMetricStateIdQry = null;
    private static final String instServiceMetricStateIdQrySQL = "select id from RDX_SM_METRICSTATE where typeId = ? and instanceId = ? and serviceUri = ?";
    
    private PreparedStatement netChannelMetricStateIdQry = null;
    private static final String netChannelMetricStateIdQrySQL = "select id from RDX_SM_METRICSTATE where typeId = ? and netChannelId = ?";
    
    private PreparedStatement monitoredSectionsQry = null;
    private static final String monitoredSectionsQrySQL = "select id, kind, period, timingSection from RDX_SM_METRICTYPE "
                                                        + "where kind like 'Profiling%' and timingSection is not null "
                                                        + "and (instanceId is null or instanceId = ?) and enabled > 0";
    
    private PreparedStatement monitoredServicesQry = null;
    private static final String monitoredServicesQrySQL = "select id, serviceUri, period from RDX_SM_METRICTYPE where kind = ? and (instanceId is null or instanceId = ?) and enabled > 0";
    
    private PreparedStatement monitoredSessionUnitsQry = null;
    private static final String monitoredSessionUnitsQrySQL = "select mt.id, mt.kind, mt.period, mt.unitId from RDX_SM_METRICTYPE mt "
                                                            + "where mt.kind = 'Unit.Arte.SessionCnt' "
                                                            + "and (mt.unitId is null or mt.unitId in (select rdx_unit.id from rdx_unit where rdx_unit.instanceId = ?))"
                                                            + "and mt.enabled > 0";

    private PreparedStatement monitoredUnitStatusesQry = null;
    private static final String monitoredUnitStatusesQrySQL = "select unitId, typeId, stateId, checktime, started, hung, unitIdInType, checkTimeMillis, RDX_UTILS.getUnixEpochMillis() curMillis from "
                                                            + "(select u.id unitId, u.instanceId instanceId, t.id typeId, s.id stateId, u.started started, u.selfchecktime checkTime, u.selfCheckTimeMillis checkTimeMillis, s.endval hung, t.unitId unitIdInType, t.enabled enabled "
                                                            + "from RDX_UNIT u left join RDX_SM_METRICTYPE t on t.kind = 'Unit.Hang' and u.id = nvl(t.unitId, u.id) "
                                                            + "left join RDX_SM_METRICSTATE s on s.typeid = t.id) "
                                                            + "where typeId is not null and instanceId = ? and enabled > 0";
    
    private PreparedStatement qrySetArteInstCount = null;
    private static final String qrySetArteInstCountSQL = "update rdx_instance set arteInstCount=? where id=?";
    
    private PreparedStatement qryWriteBasicInstanceStats = null;
    private static final String qryWriteBasicInstanceStatsSQL = "update rdx_instance set arteInstCount=?, avgActiveArteCount=? where id=?";
    
    private PreparedStatement qryGetExistingServices = null;
    private static final String qryGetExistingServicesSQL = "select uri from rdx_service where implementedinarte > 0";
    
    private MonitoringDbQueries() {
        this(null,null,"");
    }
    
    public MonitoringDbQueries(final Connection connection, final IRadixTrace trace, final String eventSource) {
        this.connection = connection;
        this.trace = trace;
        EEventSource source;
        try {
            source = EEventSource.getForValue(eventSource);
        } catch (NoConstItemWithSuchValueError ex) {
            source = EEventSource.SYSTEM_MONITORING;
        }
        this.eventSource = source;
    }

    public void setConnection(final Connection connection) {
        closeAll();
        this.connection = connection;
    }

    public MetricParameters readMetricParameters(final MetricDescription metricDescription) throws SQLException {
        switch (metricDescription.getSensorCoordinates().getSensorType()) {
            case UNIT:
                return readUnitMetricParams(metricDescription);
            case INSTANCE:
                return readInstanceMetricParams(metricDescription);
            default:
                throw new RadixError("Unsupported sensor type: " + metricDescription.getSensorCoordinates().getSensorType());
        }
    }

    public void setArteInstCount(final long instanceId, final int count) {
        try {
            if (qrySetArteInstCount == null) {
                qrySetArteInstCount = prepareQuery(qrySetArteInstCountSQL);
                qrySetArteInstCount.setLong(2, instanceId);
            }
            qrySetArteInstCount.setInt(1, count);
            qrySetArteInstCount.execute();
            connection.commit();
        } catch (SQLException ex) {
            if (trace != null) {
                trace.put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + ExceptionTextFormatter.throwableToString(ex), EEventSource.SYSTEM_MONITORING);
            }
        }
    }

    public void writeBasicInstanceStats(final long instanceId, final int arteInstCount, final double averageActiveArteCount) {
        try {
            if (qryWriteBasicInstanceStats == null) {
                qryWriteBasicInstanceStats = prepareQuery(qryWriteBasicInstanceStatsSQL);
                qryWriteBasicInstanceStats.setLong(3, instanceId);
            }
            qryWriteBasicInstanceStats.setInt(1, arteInstCount);
            qryWriteBasicInstanceStats.setDouble(2, averageActiveArteCount);
            qryWriteBasicInstanceStats.execute();
            connection.commit();
        } catch (SQLException ex) {
            if (trace != null) {
                trace.put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + ExceptionTextFormatter.throwableToString(ex), EEventSource.SYSTEM_MONITORING);
            }
        }

    }

    private MetricParameters readUnitMetricParams(final MetricDescription metricDescription) throws SQLException {
        if (unitMetricQry == null) {
            unitMetricQry = prepareQuery(unitMetricQrySQL);
        }
        unitMetricQry.setString(1, metricDescription.getKindName());
        unitMetricQry.setLong(2, metricDescription.getSensorCoordinates().getUnitId());

        try(final ResultSet rs = unitMetricQry.executeQuery()) {
            if (rs.next()) {
                final MetricParameters params = buildUnitParams(rs, metricDescription);
                rs.getLong(3);
                if (rs.wasNull() && rs.next()) {
                    return buildUnitParams(rs, metricDescription);
                }
                return params;
            }
        }
        return null;
    }

    private MetricParameters buildUnitParams(final ResultSet rs, final MetricDescription metricDescription) throws SQLException {
        final long periodMillis = rs.getLong(2) * 1000;
        final MetricParametersBuilder builder = new MetricParametersBuilder();
        builder.setTypeId(rs.getLong(1));
        builder.setDescription(metricDescription);
        builder.setPeriodMillis(periodMillis);
        return builder.getMetricParameters();
    }

    private MetricParameters readInstanceMetricParams(final MetricDescription metricDescription) throws SQLException {
        if (instanceMetricQry == null) {
            instanceMetricQry = prepareQuery(instanceMetricQrySQL);
        }
        instanceMetricQry.setString(1, metricDescription.getKindName());
        instanceMetricQry.setLong(2, metricDescription.getSensorCoordinates().getInstanceId());

        try(final ResultSet rs = instanceMetricQry.executeQuery()) {
            if (rs.next()) {
                final MetricParameters params = buildInstanceParams(rs, metricDescription);
                rs.getLong(3);
                if (rs.wasNull() && rs.next()) {
                    return buildInstanceParams(rs, metricDescription);
                }
                return params;
            }
        }
        return null;
    }

    private MetricParameters buildInstanceParams(final ResultSet rs, final MetricDescription description) throws SQLException {
        final long periodMillis = rs.getLong(2) * 1000;
        final MetricParametersBuilder builder = new MetricParametersBuilder();
        builder.setTypeId(rs.getLong(1));
        builder.setDescription(description);
        builder.setPeriodMillis(periodMillis);
        return builder.getMetricParameters();
    }

    /**
     *
     * @param metricParameters
     * @return -1 if there is no MetricState with such id
     * @throws SQLException
     */
    public long getMetricStateId(final MetricParameters metricParameters) throws SQLException {
        switch (metricParameters.getDescription().getSensorCoordinates().getSensorType()) {
            case UNIT:
                return getUnitMetricStateId(metricParameters);
            case INSTANCE:
                return getInstanceMetricStateId(metricParameters);
            case TIMING_SECTION:
                return getTimingSectionMetricStateId(metricParameters);
            case INSTANCE_SERVICE:
                return getInstServiceMetricStateId(metricParameters);
            case NET_CHANNEL:
                return getNetChannelMetricStateId(metricParameters);
            default:
                throw new RadixError("Unsupported sensor type");
        }
    }

    private long getNetChannelMetricStateId(final MetricParameters metricParameters) throws SQLException {
        if (netChannelMetricStateIdQry == null) {
            netChannelMetricStateIdQry = prepareQuery(netChannelMetricStateIdQrySQL);
        }
        netChannelMetricStateIdQry.setLong(1, metricParameters.getTypeId());
        netChannelMetricStateIdQry.setLong(2, metricParameters.getDescription().getSensorCoordinates().getNetChannelId());
        try(final ResultSet rs = netChannelMetricStateIdQry.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        return -1;
    }

    private long getUnitMetricStateId(final MetricParameters metricParameters) throws SQLException {
        if (unitMetricStateIdQry == null) {
            unitMetricStateIdQry = prepareQuery(unitMetricStateIdQrySQL);
        }
        unitMetricStateIdQry.setLong(1, metricParameters.getTypeId());
        unitMetricStateIdQry.setLong(2, metricParameters.getDescription().getSensorCoordinates().getUnitId());
        try(final ResultSet rs = unitMetricStateIdQry.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        return -1;
    }

    private long getTimingSectionMetricStateId(final MetricParameters metricParameters) throws SQLException {
        if (timingSectionMetricStateIdQry == null) {
            timingSectionMetricStateIdQry = prepareQuery(timingSectionMetricStateIdQrySQL);
        }
        timingSectionMetricStateIdQry.setLong(1, metricParameters.getTypeId());
        timingSectionMetricStateIdQry.setLong(2, metricParameters.getDescription().getSensorCoordinates().getInstanceId());
        timingSectionMetricStateIdQry.setString(3, metricParameters.getDescription().getSensorCoordinates().getSectionName());
        try(final ResultSet rs = timingSectionMetricStateIdQry.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        return -1;
    }

    private long getInstanceMetricStateId(final MetricParameters metricParameters) throws SQLException {
        if (instanceMetricStateIdQry == null) {
            instanceMetricStateIdQry = prepareQuery(instanceMetricStateIdQrySQL);
        }
        instanceMetricStateIdQry.setLong(1, metricParameters.getTypeId());
        instanceMetricStateIdQry.setLong(2, metricParameters.getDescription().getSensorCoordinates().getInstanceId());
        try(final ResultSet rs = instanceMetricStateIdQry.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        return -1;
    }

    private long getInstServiceMetricStateId(final MetricParameters metricParameters) throws SQLException {
        if (instServiceMetricStateIdQry == null) {
            instServiceMetricStateIdQry = prepareQuery(instServiceMetricStateIdQrySQL);
        }
        instServiceMetricStateIdQry.setLong(1, metricParameters.getTypeId());
        instServiceMetricStateIdQry.setLong(2, metricParameters.getDescription().getSensorCoordinates().getInstanceId());
        instServiceMetricStateIdQry.setString(3, metricParameters.getDescription().getSensorCoordinates().getServiceUri());
        try(final ResultSet rs = instServiceMetricStateIdQry.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        return -1;
    }

    public Collection<MetricParameters> getMonitoredArteUnits(final long instanceId) throws SQLException {
        final MetricParametersBuilder builder = new MetricParametersBuilder();
        final List<MetricParameters> result = new ArrayList<MetricParameters>();
        
        if (monitoredSessionUnitsQry == null) {
            monitoredSessionUnitsQry = prepareQuery(monitoredSessionUnitsQrySQL);
        }
        monitoredSessionUnitsQry.setLong(1, instanceId);
        try(final ResultSet rs = monitoredSessionUnitsQry.executeQuery()) {
            while (rs.next()) {
                builder.setTypeId(rs.getLong(1));
                builder.setPeriodMillis(rs.getLong(3) * 1000);
                Long unitId = rs.getLong(4);
                if (rs.wasNull()) {
                    unitId = null;
                }
                builder.setDescription(new MetricDescription(EMetric.UNIT_ARTE_SESSION_CNT.getKindName(), EMetricType.STATISTIC, SensorCoordinates.forUnit(unitId)));
                result.add(builder.getMetricParameters());
            }
        }
        return result;
    }

    public Collection<MetricParameters> getMonitoredSections(final long instanceId) throws SQLException {
        final List<MetricParameters> parameters = new ArrayList<MetricParameters>();
        final MetricParametersBuilder builder = new MetricParametersBuilder();
        
        if (monitoredSectionsQry == null) {
            monitoredSectionsQry = prepareQuery(monitoredSectionsQrySQL);
        }
        monitoredSectionsQry.setLong(1, instanceId);
        try(final ResultSet rs = monitoredSectionsQry.executeQuery()) {
            while (rs.next()) {
                final String kind = rs.getString(2);
                final EMetric metric = EMetric.findForKind(kind);
                if (metric == null && trace != null) {
                    trace.put(EEventSeverity.ERROR, String.format(Messages.ERR_UNSUPPORTED_METRIC_KIND, kind), eventSource);
                    continue;
                }
                builder.setTypeId(rs.getLong(1));
                builder.setPeriodMillis(rs.getLong(3) * 1000);
                builder.setDescription(new MetricDescription(kind, metric.getMetricType(), SensorCoordinates.forInstanceSection(instanceId, rs.getString(4))));
                parameters.add(builder.getMetricParameters());
            }
        }
        return parameters;
    }

    public Collection<MetricParameters> getMonitoredServices(final long instanceId) throws SQLException {
        final MetricParametersBuilder builder = new MetricParametersBuilder();
        final List<MetricParameters> parameters = new ArrayList<MetricParameters>();
        
        if (monitoredServicesQry == null) {
            monitoredServicesQry = prepareQuery(monitoredServicesQrySQL);
            monitoredServicesQry.setString(1, EMetric.INSTANCE_SERVICE_ARTE_SESS_CNT.getKindName());
        }
        monitoredServicesQry.setLong(2, instanceId);
        try(final ResultSet rs = monitoredServicesQry.executeQuery()) {
            while (rs.next()) {
                builder.setTypeId(rs.getLong(1));
                builder.setPeriodMillis(rs.getLong(3) * 1000);
                builder.setDescription(new MetricDescription(EMetric.INSTANCE_SERVICE_ARTE_SESS_CNT.getKindName(), EMetric.INSTANCE_SERVICE_ARTE_SESS_CNT.getMetricType(), SensorCoordinates.forInstanceService(instanceId, rs.getString(2))));
                parameters.add(builder.getMetricParameters());
            }
        }
        return parameters;
    }

    public Collection<MonitoredUnitStatus> getMonitoredUnitStatuses(final long instanceId) throws SQLException {
        final Collection<MonitoredUnitStatus> result = new ArrayList<MonitoredUnitStatus>();
        
        if (monitoredUnitStatusesQry == null) {
            monitoredUnitStatusesQry = prepareQuery(monitoredUnitStatusesQrySQL);
        }
        monitoredUnitStatusesQry.setLong(1, instanceId);
        try(final ResultSet rs = monitoredUnitStatusesQry.executeQuery()) {
            while (rs.next()) {
                Long stateId = rs.getLong(3);
                if (rs.wasNull()) {
                    stateId = null;
                }
                Boolean hung = rs.getBoolean(6);
                if (rs.wasNull()) {
                    hung = false;
                }
                Timestamp checkTime = rs.getTimestamp(4);
                if (rs.wasNull()) {
                    checkTime = null;
                }
                rs.getLong(7);
                final boolean generic = rs.wasNull();
                final long checkTimeMillis = rs.getLong("checkTimeMillis");
                if (!rs.wasNull()) {
                    checkTime = new Timestamp(checkTimeMillis);
                }
                result.add(new MonitoredUnitStatus(
                        rs.getLong(1),
                        rs.getLong(2),
                        stateId,
                        checkTime,
                        rs.getBoolean(5),
                        hung,
                        generic,
                        rs.getLong("curMillis")
                ));
            }
        }
        return result;
    }
    
    public Collection<String> getExistingServiceUris() throws SQLException {
        final List<String> result = new ArrayList<>();
        if (qryGetExistingServices == null) {
            qryGetExistingServices = prepareQuery(qryGetExistingServicesSQL);
        }
        try (final ResultSet rs = qryGetExistingServices.executeQuery()) {
            while (rs.next()) {
                result.add(rs.getString("uri"));
            }
        }
        return result;
    }

    private void closeQry(final PreparedStatement qry) {
        try {
            if (qry != null && !qry.getConnection().isClosed()) {
                qry.close();
            }
        } catch (SQLException e) {
            if (trace != null) {
                trace.put(EEventSeverity.WARNING, Messages.ERR_ON_DB_QRY_CLOSE + "\n" + ExceptionTextFormatter.exceptionStackToString(e), eventSource);
            }
        }
    }

    @Override
    public void closeAll() {
        if (unitMetricQry != null) {
            closeQry(unitMetricQry);
            unitMetricQry = null;
        }
        if (unitMetricStateIdQry != null) {
            closeQry(unitMetricStateIdQry);
            unitMetricStateIdQry = null;
        }
        if (instanceMetricQry != null) {
            closeQry(instanceMetricQry);
            instanceMetricQry = null;
        }
        if (instanceMetricStateIdQry != null) {
            closeQry(instanceMetricStateIdQry);
            instanceMetricStateIdQry = null;
        }
        if (monitoredSectionsQry != null) {
            closeQry(monitoredSectionsQry);
            monitoredSectionsQry = null;
        }
        if (timingSectionMetricStateIdQry != null) {
            closeQry(timingSectionMetricStateIdQry);
            timingSectionMetricStateIdQry = null;
        }
        if (monitoredServicesQry != null) {
            closeQry(monitoredServicesQry);
            monitoredServicesQry = null;
        }
        if (instServiceMetricStateIdQry != null) {
            closeQry(instServiceMetricStateIdQry);
            instServiceMetricStateIdQry = null;
        }
        if (monitoredSessionUnitsQry != null) {
            closeQry(monitoredSessionUnitsQry);
            monitoredSessionUnitsQry = null;
        }
        if (monitoredUnitStatusesQry != null) {
            closeQry(monitoredUnitStatusesQry);
            monitoredUnitStatusesQry = null;
        }
        if (netChannelMetricStateIdQry != null) {
            closeQry(netChannelMetricStateIdQry);
            netChannelMetricStateIdQry = null;
        }
        if (qrySetArteInstCount != null) {
            closeQry(qrySetArteInstCount);
            qrySetArteInstCount = null;
        }

        if (qryWriteBasicInstanceStats != null) {
            closeQry(qryWriteBasicInstanceStats);
            qryWriteBasicInstanceStats = null;
        }
        
        if (qryGetExistingServices != null) {
            closeQry(qryGetExistingServices);
            qryGetExistingServices = null;
        }
    }

    @Override
    public void prepareAll() throws SQLException {
        prepareAll(connection);
    }

    private PreparedStatement prepareQuery(final String query) throws SQLException {
        return prepareQuery(connection,query);
    }
    
    public class MonitoredUnitStatus {

        private final long unitId;
        private final long typeId;
        private final Long stateId;
        private final Timestamp checkTime;
        private final Boolean started;
        private final Boolean hung;
        private final boolean generic;
        private final long dbCurMillis;

        public MonitoredUnitStatus(final long unitId,
                final long typeId,
                final Long stateId,
                final Timestamp checkTime,
                final Boolean started,
                final Boolean hung,
                final boolean generic,
                final long dbCurMillis) {
            this.unitId = unitId;
            this.typeId = typeId;
            this.stateId = stateId;
            this.checkTime = checkTime;
            this.started = started;
            this.hung = hung;
            this.generic = generic;
            this.dbCurMillis = dbCurMillis;
        }

        public long getDbCurMillis() {
            return dbCurMillis;
        }
        
        public Timestamp getCheckTime() {
            return checkTime;
        }

        public Long getStateId() {
            return stateId;
        }

        public long getTypeId() {
            return typeId;
        }

        public long getUnitId() {
            return unitId;
        }

        public Boolean isHung() {
            return hung;
        }

        public Boolean isStarted() {
            return started;
        }

        public boolean isGeneric() {
            return generic;
        }
    }
}
