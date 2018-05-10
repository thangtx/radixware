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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.ESensorType;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.dbq.OraExCodes;

/**
 * Holds queue of metrics to write and performs writing on
 * {@linkplain flush()}.<br>
 * Methods that work with database ({@linkplain flush()},
 * {@linkplain  setConnection(Connection)}) are not thread safe and should be
 * called from single thread (typically main thread of the unit or instance)
 *
 */
public class MetricRecordWriter {

    private static final long COMPRESSION_PERIOD_MILLIS = 10000;

    public enum ECacheMode {

        ON,
        OFF;
    }

    public enum ECommitPolicy {

        COMMIT_AFTER_FLUSH,
        NO_COMMIT;
    }

    private enum EGetTypeIdSql {

        GET_FROM_PARAMETER("?"),
        GET_BY_UNIT_ID("select typeId from (select id typeId from rdx_sm_metrictype where kind=? and (unitId = ? or unitId is null) and enabled > 0 order by nvl(unitId, -1) desc) where rownum = 1"),
        GET_BY_INSTANCE_ID("select typeId from (select id typeId from rdx_sm_metrictype where kind=? and (instanceId = ? or instanceId is null) and enabled > 0 order by nvl(instanceId, -1) desc) where rownum = 1"),
        GET_BY_NETCHANNEL_ID("select typeId from (select id typeId from rdx_sm_metrictype where kind=? and (netChannelId = ? or netChannelId is null) and enabled > 0 order by nvl(netChannelId, -1) desc) where rownum = 1");
        private final String sql;

        private EGetTypeIdSql(String sql) {
            this.sql = sql;
        }

        public String getSql() {
            return sql;
        }
    }
    private List<MetricRecord> list = new ArrayList<MetricRecord>();
    private final List<FlushListener> flushListeners = new CopyOnWriteArrayList<FlushListener>();
    private final Object listLock = new Object();
    private final Map<String, Long> stateIdCache = new HashMap<String, Long>();
    private Connection connection;
    private PreparedStatement updateStatement;
    private Map<EGetTypeIdSql, PreparedStatement> mergeStatements = new EnumMap<>(EGetTypeIdSql.class);
    private final MonitoringDbQueries dbQueries;
    private final LocalTracer localTracer;
    private final ECommitPolicy commitPolicy;
    private final ECacheMode cacheMode;
    
    public MetricRecordWriter(final Connection connection, final MonitoringDbQueries dbQueries, final LocalTracer localTracer) {
        this(connection, dbQueries, localTracer, ECacheMode.ON, ECommitPolicy.COMMIT_AFTER_FLUSH);
    }

    public MetricRecordWriter(final Connection connection, final MonitoringDbQueries dbQueries, final LocalTracer localTracer, ECacheMode cacheMode, ECommitPolicy commitPolicy) {
        this.connection = connection;
        this.dbQueries = dbQueries;
        this.localTracer = localTracer;
        this.commitPolicy = commitPolicy;
        this.cacheMode = cacheMode;
    }

    /**
     * Write value of the metric with specified kind to the database. Query to
     * database will first determine if according metric is set up in the
     * system, and if so, update it's value
     */
    public void writeUnitStatMetric(final long unitId, final String metricKind, final long begTimeMillis, final long endTimeMillis, final Double minVal, final Double maxVal, final double avgVal) {
        add(new MetricRecord(new MetricParameters(new MetricDescription(metricKind, EMetricType.STATISTIC, SensorCoordinates.forUnit(unitId)), -1, 0), new StatValue(minVal, maxVal, avgVal, begTimeMillis, endTimeMillis)));
    }

    /**
     * Write value of the metric with specified kind to the database. Query to
     * database will first determine if according metric is set up in the
     * system, and if so, update it's value
     */
    public void writeUnitEventMetric(final long unitId, final String metricKind, final long timeMillis, final Double oldValue, final Double newValue) {
        add(new MetricRecord(new MetricParameters(new MetricDescription(metricKind, EMetricType.EVENT, SensorCoordinates.forUnit(unitId)), -1, 0), new EventValue(oldValue, newValue, timeMillis)));
    }

    /**
     * Write value of the metric with specified kind to the database. Query to
     * database will first determine if according metric is set up in the
     * system, and if so, update it's value
     */
    public void writeInstanceStatMetric(final long instanceId, final String metricKind, final long begTimeMillis, final long endTimeMillis, final Double minVal, final Double maxVal, final double avgVal) {
        add(new MetricRecord(new MetricParameters(new MetricDescription(metricKind, EMetricType.STATISTIC, SensorCoordinates.forInstance(instanceId)), -1, 0), new StatValue(minVal, maxVal, avgVal, begTimeMillis, endTimeMillis)));
    }

    /**
     * Write value of the metric with specified kind to the database. Query to
     * database will first determine if according metric is set up in the
     * system, and if so, update it's value
     */
    public void writeInstanceEventMetric(final long instanceId, final String metricKind, final long timeMillis, final Double oldValue, final Double newValue) {
        add(new MetricRecord(new MetricParameters(new MetricDescription(metricKind, EMetricType.EVENT, SensorCoordinates.forInstance(instanceId)), -1, 0), new EventValue(oldValue, newValue, timeMillis)));
    }

    /**
     * Write value of the metric with specified kind to the database. Query to
     * database will first determine if according metric is set up in the
     * system, and if so, update it's value
     */
    public void writeNetChannelStatMetric(final long netChannelId, final String metricKind, final long begTimeMillis, final long endTimeMillis, final Double minVal, final Double maxVal, final double avgVal) {
        add(new MetricRecord(new MetricParameters(new MetricDescription(metricKind, EMetricType.STATISTIC, SensorCoordinates.forNetChannel(netChannelId)), -1, 0), new StatValue(minVal, maxVal, avgVal, begTimeMillis, endTimeMillis)));
    }

    /**
     * Write value of the metric with specified kind to the database. Query to
     * database will first determine if according metric is set up in the
     * system, and if so, update it's value
     */
    public void writeNetChannelEventMetric(final long netChannelId, final String metricKind, final long timeMillis, final Double oldValue, final Double newValue) {
        add(new MetricRecord(new MetricParameters(new MetricDescription(metricKind, EMetricType.EVENT, SensorCoordinates.forNetChannel(netChannelId)), -1, 0), new EventValue(oldValue, newValue, timeMillis)));
    }

    /**
     * Add metric record to write queue. This method is thread safe.
     */
    public void add(final MetricRecord metric) {
        synchronized (listLock) {
            list.add(metric);
        }
    }

    /**
     * Add metric records to write queue. This method is thread safe.
     */
    public void add(final List<MetricRecord> metrics) {
        if (metrics != null && !metrics.isEmpty()) {
            synchronized (listLock) {
                list.addAll(metrics);
            }
        }
    }

    /**
     * Dump all metric records from queue to database. <br> This method is not
     * thread safe and should be called only from unit or instance main thread.
     * <br> Note that this method can do a commit on the supplied connection
     *
     * @throws SQLException
     */
    public void flush() throws SQLException {
        for (final FlushListener flushListener : flushListeners) {
            flushListener.beforeFlush();
        }
        final List<MetricRecord> metrics;
        synchronized (listLock) {
            metrics = list;
            list = new ArrayList<>(list.size() / 2);
        }
        doFlushRecords(compress(metrics));
    }

    private List<MetricRecord> compress(final List<MetricRecord> allRecords) {
        if (allRecords == null || allRecords.isEmpty()) {
            return allRecords;
        }
        final List<MetricRecord> result = new ArrayList<>();
        final Map<String, List<MetricRecord>> recordsByMetric = new HashMap<>();
        for (MetricRecord record : allRecords) {
            if (record.getStatValue() != null) {
                result.add(record);
            } else {
                final String mapKey = getMapKey(record);
                List<MetricRecord> thisMetricRecords = recordsByMetric.get(mapKey);
                if (thisMetricRecords == null) {
                    thisMetricRecords = new ArrayList<>();
                    recordsByMetric.put(mapKey, thisMetricRecords);
                }
                thisMetricRecords.add(record);
            }
        }
        final List<MetricRecord> temp = new ArrayList<>();
        for (List<MetricRecord> recordsList : recordsByMetric.values()) {
            temp.clear();
            result.add(recordsList.get(0));
            MetricRecord lastWritten = recordsList.get(0);
            for (int i = 1; i < recordsList.size(); i++) {
                MetricRecord record = recordsList.get(i);
                if (record.getEventValue().getEndTimeMillis() - lastWritten.getEventValue().getEndTimeMillis() < COMPRESSION_PERIOD_MILLIS) {
                    temp.add(record);
                } else {
                    final List<MetricRecord> compressed = compressIfNecessary(lastWritten, temp);
                    if (compressed.isEmpty()) {
                        result.add(record);
                    } else {
                        result.addAll(compressed);
                        i = i - 1;
                    }
                    lastWritten = result.get(result.size() - 1);
                    temp.clear();
                }
            }
            result.addAll(compressIfNecessary(lastWritten, temp));
        }
        return result;
    }

    private List<MetricRecord> compressIfNecessary(final MetricRecord lastWritten, final List<MetricRecord> newRecords) {
        if (newRecords.size() < 3) {
            return newRecords;
        }
        double min = lastWritten.getEventValue().getNewValue();
        double max = lastWritten.getEventValue().getNewValue();
        final long startTimeMillis = lastWritten.getEventValue().getEndTimeMillis();
        long lastValueStartMillis = lastWritten.getEventValue().getEndTimeMillis();
        double lastValue = lastWritten.getEventValue().getNewValue();
        //if subsequent records have the same time (they happened in one millisecond),
        //we should take average value for that millisecond
        double lastValuesSum = lastValue;
        int lastValuesCount = 1;
        double integralSum = 0;
        for (int i = 0; i < newRecords.size(); i++) {
            min = Math.min(min, newRecords.get(i).getEventValue().getNewValue());
            max = Math.max(max, newRecords.get(i).getEventValue().getNewValue());

            if (lastValueStartMillis == newRecords.get(i).getEventValue().getEndTimeMillis()) {
                lastValuesSum += newRecords.get(i).getEventValue().getNewValue();
                lastValuesCount++;
                if (i < newRecords.size() - 1) {
                    continue;
                }
            }
            integralSum += lastValuesSum / lastValuesCount;
            final long timeExclusivelyInLastValueMillis = newRecords.get(i).getEventValue().getEndTimeMillis() - lastValueStartMillis - 1;
            integralSum += lastValue * timeExclusivelyInLastValueMillis;
            lastValueStartMillis = newRecords.get(i).getEventValue().getEndTimeMillis();
            lastValuesCount = 1;
            lastValue = newRecords.get(i).getEventValue().getNewValue();
            lastValuesSum = lastValue;
        }

        return Collections.singletonList(
                new MetricRecord(lastWritten.getParameters(), new EventValue(
                                lastWritten.getEventValue().getNewValue(),
                                newRecords.get(newRecords.size() - 1).getEventValue().getNewValue(),
                                new StatValue(min, max, integralSum / (newRecords.get(newRecords.size() - 1).getEventValue().getEndTimeMillis() - startTimeMillis + 1)), newRecords.get(newRecords.size() - 1).getEventValue().getEndTimeMillis())));
    }

    private String getMapKey(final MetricRecord record) {
        if (record == null) {
            return null;
        }
        return record.getParameters().getTypeId() + record.getParameters().getDescription().getSensorCoordinates().toString();
    }

    /**
     * This method is not thread safe and should be called only from unit or
     * instance main thread
     *
     * @throws SQLException
     */
    public void setConnection(final Connection connection) {
        this.connection = connection;
        if (updateStatement != null) {
            try {
                updateStatement.close();
            } catch (SQLException ex) {
                //do nothing
            }
        }
        updateStatement = null;
        try {
            for (Map.Entry<EGetTypeIdSql, PreparedStatement> entry : mergeStatements.entrySet()) {
                if (entry.getValue() != null) {
                    try {
                        entry.getValue().close();
                    } catch (SQLException ex) {
                        //do nothing
                    }
                }
            }
        } finally {
            mergeStatements.clear();
        }
    }

    public void addFlushListener(final FlushListener listener) {
        flushListeners.add(listener);
    }

    public void removeFlushListener(final FlushListener listener) {
        flushListeners.remove(listener);
    }

    protected void doFlushRecords(final List<MetricRecord> records) throws SQLException {
        if (connection == null || connection.isClosed()) {
            return;
        }
        for (final MetricRecord metricRecord : records) {
            doFlush(metricRecord);
        }
        if (commitPolicy == ECommitPolicy.COMMIT_AFTER_FLUSH) {
            connection.commit();
        }
    }

    protected void doFlush(final MetricRecord metricRecord) {
        //update existing state record or create new one
        try {
            if (cacheMode == ECacheMode.OFF || metricRecord.getParameters().getTypeId() == -1) {
                createOrUpdateStateRecord(metricRecord);
                return;
            }
            final String metricKey = getStringKey(metricRecord);
            final Long metricStateId = stateIdCache.get(metricKey);//check if there is a cached id of the state
            if (metricStateId == null) {
                stateIdCache.put(metricKey, writeAndGetId(metricRecord));
            } else {
                try {
                    //try to update record with cached id
                    updateStateRecord(metricStateId, metricRecord);
                } catch (SQLException ex) {
                    if (ex.getErrorCode() == OraExCodes.NO_DATA_FOUND) {//state record with cached id could be removed, create or update new one
                        stateIdCache.put(metricKey, writeAndGetId(metricRecord));
                    } else {
                        throw ex;
                    }
                }

            }
        } catch (Exception ex) {
            if (ex instanceof SQLException) {
                if (((SQLException) ex).getErrorCode() == OraExCodes.INTEGRITY_CONSTRAINT_VIOLATED_PARENT_KEY_NOT_FOUND) {
                    localTracer.debug("Unable to flush metric record " + metricRecord.toString() + ", metric was probably removed", false);
                    return;
                }
            }
            final String floodKey = "ErrorWritingMetric[" + metricRecord.getParameters().getTypeId() + "/" + metricRecord.getParameters().getDescription().getSensorCoordinates() + "]";
            localTracer.putFloodControlled(floodKey, EEventSeverity.ERROR, "Error while writing metric record: " + metricRecord.toString() + "\n" + ExceptionTextFormatter.throwableToString(ex), null, null, false);
        }
    }

    private String getStringKey(final MetricRecord metricRecord) {
        return metricRecord.getParameters().getTypeId()
                + metricRecord.getParameters().getDescription().getSensorCoordinates().toString();
    }

    private void updateStateRecord(final long metricStateId, final MetricRecord metricRecord) throws SQLException {
        if (connection == null) {
            return;
        }
        if (updateStatement == null) {
            final String sql = "update rdx_sm_metricstate "
                    + "set (begTime, endTime, begVal, endVal, minVal, maxVal, avgVal) "
                    + "= (select ?, ?, ?, ?, ?, ?, ? from dual) "
                    + "where id = ?";
            updateStatement = connection.prepareStatement(sql);
        }
        updateStatement.clearParameters();
        final MetricDescription description = metricRecord.getParameters().getDescription();

        if (description.getMetricType() == EMetricType.EVENT) {//event metric
            setTime(updateStatement, 1, metricRecord.getEventValue().getBegTimeMillis());
            setTime(updateStatement, 2, metricRecord.getEventValue().getEndTimeMillis());
            if (metricRecord.getEventValue().getOldValue() != null) {
                updateStatement.setDouble(3, metricRecord.getEventValue().getOldValue());
            } else {
                updateStatement.setNull(3, Types.DOUBLE);
            }
            if (metricRecord.getEventValue().getNewValue() != null) {
                updateStatement.setDouble(4, metricRecord.getEventValue().getNewValue());
            } else {
                updateStatement.setNull(4, Types.DOUBLE);
            }
            if (metricRecord.getEventValue().getIntermediateStat() == null) {
                updateStatement.setNull(5, Types.DOUBLE);
                updateStatement.setNull(6, Types.DOUBLE);
                updateStatement.setNull(7, Types.DOUBLE);
            } else {
                updateStatement.setDouble(5, metricRecord.getEventValue().getIntermediateStat().getMin());
                updateStatement.setDouble(6, metricRecord.getEventValue().getIntermediateStat().getMax());
                updateStatement.setDouble(7, metricRecord.getEventValue().getIntermediateStat().getAvg());
            }

        } else {//statistic metric
            setTime(updateStatement, 1, metricRecord.getStatValue().getBegTimeMillis());
            setTime(updateStatement, 2, metricRecord.getStatValue().getEndTimeMillis());
            updateStatement.setNull(3, Types.DOUBLE);
            updateStatement.setNull(4, Types.DOUBLE);
            if (metricRecord.getStatValue().getMin() != null) {
                updateStatement.setDouble(5, metricRecord.getStatValue().getMin());
            } else {
                updateStatement.setNull(5, Types.DOUBLE);
            }
            if (metricRecord.getStatValue().getMax() != null) {
                updateStatement.setDouble(6, metricRecord.getStatValue().getMax());
            } else {
                updateStatement.setNull(6, Types.DOUBLE);
            }
            updateStatement.setDouble(7, metricRecord.getStatValue().getAvg());
        }
        updateStatement.setLong(8, metricStateId);
        if (updateStatement.executeUpdate() == 0) {
            throw new SQLException(null, null, OraExCodes.NO_DATA_FOUND);
        }
    }

    private void setTime(final PreparedStatement statement, final int paramIndex, final long timeMillis) throws SQLException {
        statement.setTimestamp(paramIndex, new Timestamp(timeMillis >= 0 ? timeMillis : 0));
    }

    private Long writeAndGetId(final MetricRecord metricRecord) throws SQLException, IllegalMetricDataException {
        createOrUpdateStateRecord(metricRecord);
        final long newId = dbQueries.getMetricStateId(metricRecord.getParameters());
        final Long result = newId == -1 ? null : newId;
        return result;
    }

    private void createOrUpdateStateRecord(final MetricRecord metricRecord) throws SQLException, IllegalMetricDataException {
        if (connection == null) {
            return;
        }
        if (metricRecord.getParameters().getTypeId() != -1) {
            final PreparedStatement mergeWithKnownTypeIdStatement = getOrCreateMergeStatement(EGetTypeIdSql.GET_FROM_PARAMETER);
            mergeWithKnownTypeIdStatement.clearParameters();
            mergeWithKnownTypeIdStatement.setLong(1, metricRecord.getParameters().getTypeId());
            fillMergeParamsExceptTypeId(mergeWithKnownTypeIdStatement, metricRecord, 2);
            mergeWithKnownTypeIdStatement.executeUpdate();
        } else {
            final PreparedStatement mergeStatement;
            final ESensorType sensorType = metricRecord.getParameters().getDescription().getSensorCoordinates().getSensorType();
            if (sensorType == ESensorType.INSTANCE) {
                mergeStatement = getOrCreateMergeStatement(EGetTypeIdSql.GET_BY_INSTANCE_ID);
            } else if (sensorType == ESensorType.UNIT) {
                mergeStatement = getOrCreateMergeStatement(EGetTypeIdSql.GET_BY_UNIT_ID);
            } else if (sensorType == ESensorType.NET_CHANNEL) {
                mergeStatement = getOrCreateMergeStatement(EGetTypeIdSql.GET_BY_NETCHANNEL_ID);
            } else {
                throw new IllegalMetricDataException("Unsupported sensor type for updating metric with unknown typeId: " + sensorType);
            }
            mergeStatement.clearParameters();
            mergeStatement.setString(1, metricRecord.getParameters().getDescription().getKindName());
            final SensorCoordinates sensorCoordinates = metricRecord.getParameters().getDescription().getSensorCoordinates();
            if (sensorType == ESensorType.INSTANCE) {
                mergeStatement.setLong(2, sensorCoordinates.getInstanceId() == null ? 0 : sensorCoordinates.getInstanceId());
            } else if (sensorType == ESensorType.UNIT) {
                mergeStatement.setLong(2, sensorCoordinates.getUnitId() == null ? 0 : sensorCoordinates.getUnitId());
            } else if (sensorType == ESensorType.NET_CHANNEL) {
                mergeStatement.setLong(2, sensorCoordinates.getNetChannelId() == null ? 0 : sensorCoordinates.getNetChannelId());
            }
            fillMergeParamsExceptTypeId(mergeStatement, metricRecord, 3);
            mergeStatement.executeUpdate();
        }
    }

    private PreparedStatement getOrCreateMergeStatement(final EGetTypeIdSql getTypeIdSql) throws SQLException {
        if (mergeStatements.containsKey(getTypeIdSql)) {
            return mergeStatements.get(getTypeIdSql);
        }
        final PreparedStatement mergeStatement = connection.prepareStatement(buildMergeSql(getTypeIdSql.getSql()));
        mergeStatements.put(getTypeIdSql, mergeStatement);
        return mergeStatement;
    }

    private void fillMergeParamsExceptTypeId(final PreparedStatement createStatement, final MetricRecord metricRecord, final int startIndex) throws SQLException {
        final MetricDescription description = metricRecord.getParameters().getDescription();
        int index = startIndex;
        if (description.getSensorCoordinates().getInstanceId() != null) {
            createStatement.setLong(index++, description.getSensorCoordinates().getInstanceId());
        } else {
            createStatement.setNull(index++, Types.INTEGER);
        }
        if (description.getSensorCoordinates().getUnitId() != null) {
            createStatement.setLong(index++, description.getSensorCoordinates().getUnitId());
        } else {
            createStatement.setNull(index++, Types.INTEGER);
        }
        if (description.getSensorCoordinates().getServiceUri() != null) {
            createStatement.setString(index++, description.getSensorCoordinates().getServiceUri());
        } else {
            createStatement.setNull(index++, Types.VARCHAR);
        }
        if (description.getSensorCoordinates().getNetChannelId() != null) {
            createStatement.setLong(index++, description.getSensorCoordinates().getNetChannelId());
        } else {
            createStatement.setNull(index++, Types.INTEGER);
        }
        if (description.getMetricType() == EMetricType.EVENT) {//event metric
            createStatement.setTimestamp(index++, new Timestamp(metricRecord.getEventValue().getBegTimeMillis()));
            createStatement.setTimestamp(index++, new Timestamp(metricRecord.getEventValue().getEndTimeMillis()));
            if (metricRecord.getEventValue().getOldValue() != null) {
                createStatement.setDouble(index++, metricRecord.getEventValue().getOldValue());
            } else {
                createStatement.setNull(index++, Types.DOUBLE);
            }
            if (metricRecord.getEventValue().getNewValue() != null) {
                createStatement.setDouble(index++, metricRecord.getEventValue().getNewValue());
            } else {
                createStatement.setNull(index++, Types.DOUBLE);
            }
            createStatement.setNull(index++, Types.DOUBLE);
            createStatement.setNull(index++, Types.DOUBLE);
            createStatement.setNull(index++, Types.DOUBLE);
        } else {//statistic metric
            createStatement.setTimestamp(index++, new Timestamp(metricRecord.getStatValue().getBegTimeMillis()));
            createStatement.setTimestamp(index++, new Timestamp(metricRecord.getStatValue().getEndTimeMillis()));
            createStatement.setNull(index++, Types.DOUBLE);
            createStatement.setNull(index++, Types.DOUBLE);
            if (metricRecord.getStatValue().getMin() != null) {
                createStatement.setDouble(index++, metricRecord.getStatValue().getMin());
            } else {
                createStatement.setNull(index++, Types.DOUBLE);
            }
            if (metricRecord.getStatValue().getMax() != null) {
                createStatement.setDouble(index++, metricRecord.getStatValue().getMax());
            } else {
                createStatement.setNull(index++, Types.DOUBLE);
            }
            createStatement.setDouble(index++, metricRecord.getStatValue().getAvg());
        }
    }

    private String buildMergeSql(final String selectTypeExpression) {
        return "merge into rdx_sm_metricstate ms "
                + "using (select (" + selectTypeExpression + ") typeId, ? instanceId, ? unitId, ? serviceUri, ? netChannelId, "
                + "? begTime, ? endTime, ? begVal, ? endVal, ? minVal, ? maxVal, ? avgVal from dual) t "
                + "on (ms.typeId = t.typeId and nvl(ms.instanceId,-1) = nvl(t.instanceId, -1) "
                + "and nvl(ms.unitId, -1) = nvl(t.unitId, -1) and nvl(ms.serviceUri, ' ') = nvl(t.serviceUri, ' ') "
                + "and nvl(ms.netChannelId, -1) = nvl(t.netChannelId, -1)) "
                + "when matched then "
                + "update set ms.begTime = t.begTime, ms.endTime = t.endTime, ms.begVal = t.begVal, "
                + "ms.endVal = t.endVal, ms.minVal = t.minVal, ms.maxVal = t.maxVal, ms.avgVal = t.avgVal "
                + "when not matched then "
                + "insert (ms.id, ms.typeId, ms.instanceId, ms.unitId, ms.serviceUri, ms.netChannelId, "
                + "ms.begTime, ms.endTime, ms.begVal, ms.endVal, ms.minVal, ms.maxVal, ms.avgVal) "
                + "values (SQN_RDX_SM_METRICSTATEID.nextval, t.typeId, t.instanceId, t.unitId, t.serviceUri, t.netChannelId, "
                + "t.begTime, t.endTime, t.begVal, t.endVal, t.minVal, t.maxVal, t.avgVal) "
                + "where t.typeId is not null";

    }

    public static interface FlushListener {

        public void beforeFlush();
    }

    public static class IllegalMetricDataException extends Exception {

        public IllegalMetricDataException(Throwable cause) {
            super(cause);
        }

        public IllegalMetricDataException(String message, Throwable cause) {
            super(message, cause);
        }

        public IllegalMetricDataException(String message) {
            super(message);
        }

        public IllegalMetricDataException() {
        }
    }
}
