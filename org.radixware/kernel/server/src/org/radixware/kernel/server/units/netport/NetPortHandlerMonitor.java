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
package org.radixware.kernel.server.units.netport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EMetricKind;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.server.jdbc.DelegateDbQueries;
import org.radixware.kernel.server.jdbc.Stmt;
import org.radixware.kernel.server.jdbc.IDbQueries;
import org.radixware.kernel.server.jdbc.RadixConnection;
import org.radixware.kernel.server.monitoring.*;

public class NetPortHandlerMonitor extends AbstractMonitor {

    private static final String qryReadActiveMetricsStmtSQL = "select m.id, m.kind, m.netChannelId, m.period from rdx_sm_metrictype m, rdx_netchannel n "
            + " where "
            + " ( "
            + " ( n.id = m.netChannelId ) "
            + " or "
            + " ( m.kind in ( '" + EMetricKind.NET_SERVER_CONNECT.getValue()
            + "', '" + EMetricKind.NET_SYNC_BUSY_CONNECTIONS.getValue()
            + "', '" + EMetricKind.NET_CLIENT_CONNECT.getValue()
            + "', '" + EMetricKind.NET_CLIENT_CONNECT_TIME_PERCENT.getValue()
            + "' ) and m.netChannelId is null )"
            + " ) "
            + " and m.enabled = 1 and n.unitId = ?";
    private static final Stmt qryReadActiveMetricsStmt = new Stmt(qryReadActiveMetricsStmtSQL, Types.BIGINT);

    private final NetPortHandlerUnit unit;
    private final Map<Long, IntegralCountStat> channelToConnStat = new HashMap<>();
    private final Map<Long, IntegralCountStat> channelToBusyStat = new HashMap<>();
    private final Map<Long, IntegralCountStat> client2ConnectedPercentStat = new HashMap<>();
    private final Map<Long, MetricParameters> explicitlyMonitoredClients = new HashMap<>();
    private final Map<Long, MetricParameters> explicitlyMonitoredStatClients = new HashMap<>();
    private final Map<Long, MetricParameters> explicitlyMonitoredConnCnt = new HashMap<>();
    private final Map<Long, MetricParameters> explicitlyMonitoredSyncBusy = new HashMap<>();
    private final Set<String> initValueWasWritten = new HashSet<>();
    private MetricParameters openConnGroupParams = null;
    private MetricParameters clientsGroupParams = null;
    private MetricParameters clientsStatGroupParams = null;
    private MetricParameters syncBusyGroupParams = null;
    private final MonitorDbQueries dbQueries;
    private final IDbQueries delegate = new DelegateDbQueries(this, null);
    
    // AAS related metrics
    private final IntegralStatMetricController aasQueueSizeController;
    private final DurationStatMetricController aasQueueWaitDurationController;
    private final DurationStatMetricController aasProcDurationPureController;
    private final DurationStatMetricController aasProcDurationTotalController;
    private final FreqStatController aasInvocationsPerSecController;
    
    private final AbstractMetricController<?>[] metricControllers = new AbstractMetricController[5];
    

    public NetPortHandlerMonitor(NetPortHandlerUnit unit) {
        super(unit.getDbConnection(), unit.getTrace(), unit.getEventSource());
        this.unit = unit;
        dbQueries = new MonitorDbQueries(unit);
        dbQueries.setConnection(unit.getDbConnection());
        
        final SensorCoordinates sensorCoord = SensorCoordinates.forUnit(unit.getId());
        final MetricDescription aasQueueSizeDescr = new MetricDescription(EMetric.NET_AAS_QUEUE_SIZE.getKindName(), EMetricType.STATISTIC, sensorCoord);
        final MetricDescription aasQueueWaitDurationDescr = new MetricDescription(EMetric.NET_AAS_QUEUE_WAIT_DURATION.getKindName(), EMetricType.STATISTIC, sensorCoord);
        final MetricDescription aasProcDurationPureDescr = new MetricDescription(EMetric.NET_AAS_PROC_DURATION_PURE.getKindName(), EMetricType.STATISTIC, sensorCoord);
        final MetricDescription aasProcDurationTotalDescr = new MetricDescription(EMetric.NET_AAS_PROC_DURATION_TOTAL.getKindName(), EMetricType.STATISTIC, sensorCoord);
        final MetricDescription aasInvocationsPerSecDescr = new MetricDescription(EMetric.NET_AAS_INVOCATIONS_PER_SEC.getKindName(), EMetricType.STATISTIC, sensorCoord);
        metricControllers[0] = aasQueueSizeController = new IntegralStatMetricController(aasQueueSizeDescr);
        metricControllers[1] = aasQueueWaitDurationController = new DurationStatMetricController(aasQueueWaitDurationDescr);
        metricControllers[2] = aasProcDurationPureController = new DurationStatMetricController(aasProcDurationPureDescr);
        metricControllers[3] = aasProcDurationTotalController = new DurationStatMetricController(aasProcDurationTotalDescr);
        metricControllers[4] = aasInvocationsPerSecController = new FreqStatController(aasInvocationsPerSecDescr);
    }

    void clientConnected(final NetChannel clientChannel) {
        final long clientId = clientChannel.id;
        MetricParameters params = explicitlyMonitoredClients.get(clientId);
        if (params == null && clientsGroupParams != null) {
            params = getActualChannelParameters(clientId, clientsGroupParams);
        }
        final long currentTimeMillis = System.currentTimeMillis();
        if (params != null) {
            getWriter().add(new MetricRecord(params, new EventValue(0., 1., currentTimeMillis)));
        }
        final IntegralCountStat statController = client2ConnectedPercentStat.get(clientId);
        if (statController == null && clientsStatGroupParams != null) {
            //new channel has been loaded since last rereadParams
            final IntegralCountStat stat = new IntegralCountStat(getActualChannelParameters(clientId, clientsStatGroupParams));
            stat.start(new RegisteredItem<>(currentTimeMillis, 100));
            client2ConnectedPercentStat.put(clientId, stat);
        }
        if (statController != null) {
            statController.append(new RegisteredItem<>(currentTimeMillis, 100));
        }
    }

    void clientDisconnected(final NetChannel clientChannel) {
        final long clientId = clientChannel.id;
        MetricParameters params = explicitlyMonitoredClients.get(clientId);
        if (params == null && clientsGroupParams != null) {
            params = getActualChannelParameters(clientId, clientsGroupParams);
        }
        final long currentTimeMillis = System.currentTimeMillis();
        if (params != null) {
            getWriter().add(new MetricRecord(params, new EventValue(1., 0., currentTimeMillis)));
        }
        final IntegralCountStat statController = client2ConnectedPercentStat.get(clientId);
        if (statController != null) {
            statController.append(new RegisteredItem<>(currentTimeMillis, 0));
        }
    }

    void seanceOpened(final Seance seance) {
        if (seance.channel instanceof NetClient) {
            clientConnected(seance.channel);
        }
        connectionOpened(seance.channel);
        seance.channel.setNeedUpdateDbStats(true);
    }

    void seanceClosed(final Seance seance) {
        if (seance.channel instanceof NetClient) {
            clientDisconnected(seance.channel);
        }
        connectionClosed(seance.channel);
        seance.channel.setNeedUpdateDbStats(true);
        if (seance.channel.getOptions().isSyncMode) {
            updateBusySeancesCount(seance.channel);
        }
    }

    private MetricParameters getActualChannelParameters(final long clientId, final MetricParameters groupParameters) {
        return new MetricParameters(new MetricDescription(EMetric.findForKind(groupParameters.getDescription().getKindName()), SensorCoordinates.forNetChannel(clientId)), groupParameters.getTypeId(), groupParameters.getPeriodMillis());
    }

    void connectionOpened(final NetChannel channel) {
        final IntegralCountStat statController = channelToConnStat.get(channel.id);
        if (statController == null && openConnGroupParams != null) {
            //new channel has been loaded since last rereadParams
            final IntegralCountStat stat = new IntegralCountStat(getActualChannelParameters(channel.id, openConnGroupParams));
            stat.start(new RegisteredItem<>(System.currentTimeMillis(), channel.getActiveSeancesCount()));
            channelToConnStat.put(channel.id, stat);
        }
        if (statController != null) {
            statController.append(new RegisteredItem<>(System.currentTimeMillis(), channel.getActiveSeancesCount()));
        }
    }

    void connectionClosed(final NetChannel channel) {
        final IntegralCountStat statController = channelToConnStat.get(channel.id);
        if (statController != null) {
            statController.append(new RegisteredItem<>(System.currentTimeMillis(), channel.getActiveSeancesCount()));
        }
    }
    
    void updateQueueSize(int queueSize) {
        aasQueueSizeController.set(queueSize);
    }
    
    void aasRequestStarted(long waitedMillis) {
        aasQueueWaitDurationController.register(waitedMillis);
        aasInvocationsPerSecController.tick();
    }
    
    void aasResponseReceived(long pureProcDurationMillis, long totalProcDurationMillis) {
        aasProcDurationPureController.register(pureProcDurationMillis);
        aasProcDurationTotalController.register(totalProcDurationMillis);
    }

    void updateBusySeancesCount(final NetChannel channel) {
        final IntegralCountStat statController = channelToBusyStat.get(channel.id);
        if (statController == null && syncBusyGroupParams != null) {
            //new channel has been loaded since last rereadParams
            final IntegralCountStat stat = new IntegralCountStat(getActualChannelParameters(channel.id, syncBusyGroupParams));
            stat.start(new RegisteredItem<>(System.currentTimeMillis(), channel.getBusySeancesCount()));
            channelToConnStat.put(channel.id, stat);
        }
        if (statController != null) {
            statController.append(new RegisteredItem<>(System.currentTimeMillis(), channel.getBusySeancesCount()));
        }
    }

    @Override
    public void rereadSettings() throws SQLException {
        readParametersFromDb();
        beforeWriterFlush();//flush currently collected data
        applyParamsForConnCnt();
        applyParamsForBusyCnt();
        applyParamsForSingleClients();
        removeDeletedChannelsStats();
    }
    
    private void removeDeletedChannelsStats() {
        final Set<Long> allChannelIds = new HashSet<>();
        allChannelIds.addAll(client2ConnectedPercentStat.keySet());
        allChannelIds.addAll(channelToBusyStat.keySet());
        allChannelIds.addAll(channelToConnStat.keySet());
        for (Long id : allChannelIds) {
            if (unit.getChannels().findChannel(id) == null) {
                client2ConnectedPercentStat.remove(id);
                channelToBusyStat.remove(id);
                channelToConnStat.remove(id);
            }
        }
    }

    private void applyParamsForSingleClients() {
        applyParamsForClientsEventMetric();
        applyParamsForClientsStatMetric();
    }

    private void applyParamsForClientsEventMetric() {
        if (clientsGroupParams == null && explicitlyMonitoredClients.isEmpty()) {
            return;
        }
        for (NetChannel channel : unit.getNetChannelsSnapshot()) {
            if (channel instanceof NetClient) {
                MetricParameters parameters = explicitlyMonitoredClients.get(channel.id);
                if (parameters == null) {
                    parameters = clientsGroupParams;
                }
                if (parameters != null) {
                    final String key = parameters.getTypeId() + "~" + channel.id;
                    if (!initValueWasWritten.contains(key)) {
                        initValueWasWritten.add(key);
                        getWriter().add(new MetricRecord(getActualChannelParameters(channel.id, parameters), new EventValue(channel.getActiveSeancesCount(), channel.getActiveSeancesCount(), System.currentTimeMillis())));
                    }
                }
            }
        }
    }

    private void applyParamsForClientsStatMetric() {
        for (Map.Entry<Long, MetricParameters> entry : explicitlyMonitoredStatClients.entrySet()) {
            final MetricParameters newParameters = entry.getValue();
            final Long netClientId = newParameters.getDescription().getSensorCoordinates().getNetChannelId();
            IntegralCountStat stat = client2ConnectedPercentStat.get(netClientId);
            if (stat != null) {
                stat.setParameters(newParameters);
            } else {
                stat = new IntegralCountStat(newParameters);
                final NetChannel channel = unit.getChannels().findChannel(netClientId, false);
                if (channel != null) {
                    stat.start(new RegisteredItem<>(System.currentTimeMillis(), channel.getActiveSeancesCount()));
                } else {
                    stat.start(new RegisteredItem<>(System.currentTimeMillis(), 0));
                }
                client2ConnectedPercentStat.put(netClientId, stat);
            }
        }

        if (clientsStatGroupParams == null) {
            Iterator<Map.Entry<Long, IntegralCountStat>> iterator = client2ConnectedPercentStat.entrySet().iterator();
            while (iterator.hasNext()) {
                if (!explicitlyMonitoredStatClients.containsKey(iterator.next().getKey())) {
                    iterator.remove();
                }
            }
        } else {
            for (NetChannel client : unit.getNetChannelsSnapshot()) {
                if (client instanceof NetClient && !explicitlyMonitoredStatClients.containsKey(client.id)) {
                    IntegralCountStat stat = client2ConnectedPercentStat.get(client.id);
                    if (stat != null) {
                        stat.setParameters(getActualChannelParameters(client.id, clientsStatGroupParams));
                    } else {
                        stat = new IntegralCountStat(getActualChannelParameters(client.id, clientsStatGroupParams));
                        stat.start(new RegisteredItem<>(System.currentTimeMillis(), client.getActiveSeancesCount()));
                        client2ConnectedPercentStat.put(client.id, stat);
                    }
                }
            }
        }
    }

    private void applyParamsForConnCnt() {
        for (Map.Entry<Long, MetricParameters> entry : explicitlyMonitoredConnCnt.entrySet()) {
            final MetricParameters newParameters = entry.getValue();
            final Long netChannelId = newParameters.getDescription().getSensorCoordinates().getNetChannelId();
            IntegralCountStat stat = channelToConnStat.get(netChannelId);
            if (stat != null) {
                stat.setParameters(newParameters);
            } else {
                stat = new IntegralCountStat(newParameters);
                final NetChannel channel = unit.getChannels().findChannel(netChannelId, false);
                if (channel != null) {
                    stat.start(new RegisteredItem<>(System.currentTimeMillis(), channel.getActiveSeancesCount()));
                } else {
                    stat.start(new RegisteredItem<>(System.currentTimeMillis(), 0));
                }
                channelToConnStat.put(netChannelId, stat);
            }
        }

        if (openConnGroupParams == null) {
            Iterator<Map.Entry<Long, IntegralCountStat>> iterator = channelToConnStat.entrySet().iterator();
            while (iterator.hasNext()) {
                if (!explicitlyMonitoredConnCnt.containsKey(iterator.next().getKey())) {
                    iterator.remove();
                }
            }
        } else {
            for (NetChannel channel : unit.getNetChannelsSnapshot()) {
                if (!explicitlyMonitoredConnCnt.containsKey(channel.id)) {
                    IntegralCountStat stat = channelToConnStat.get(channel.id);
                    if (stat != null) {
                        stat.setParameters(getActualChannelParameters(channel.id, openConnGroupParams));
                    } else {
                        stat = new IntegralCountStat(getActualChannelParameters(channel.id, openConnGroupParams));
                        stat.start(new RegisteredItem<>(System.currentTimeMillis(), channel.getActiveSeancesCount()));
                        channelToConnStat.put(channel.id, stat);
                    }
                }
            }
        }
    }

    private void applyParamsForBusyCnt() {
        for (Map.Entry<Long, MetricParameters> entry : explicitlyMonitoredSyncBusy.entrySet()) {
            final MetricParameters newParameters = entry.getValue();
            final Long netChannelId = newParameters.getDescription().getSensorCoordinates().getNetChannelId();
            IntegralCountStat stat = channelToBusyStat.get(netChannelId);
            if (stat != null) {
                stat.setParameters(newParameters);
            } else {
                stat = new IntegralCountStat(newParameters);
                final NetChannel channel = unit.getChannels().findChannel(netChannelId, false);
                if (channel != null) {
                    stat.start(new RegisteredItem<>(System.currentTimeMillis(), channel.getBusySeancesCount()));
                } else {
                    stat.start(new RegisteredItem<>(System.currentTimeMillis(), 0));
                }
                channelToConnStat.put(netChannelId, stat);
            }
        }

        if (syncBusyGroupParams == null) {
            Iterator<Map.Entry<Long, IntegralCountStat>> iterator = channelToBusyStat.entrySet().iterator();
            while (iterator.hasNext()) {
                if (!explicitlyMonitoredSyncBusy.containsKey(iterator.next().getKey())) {
                    iterator.remove();
                }
            }
        } else {
            for (NetChannel channel : unit.getNetChannelsSnapshot()) {
                if (!explicitlyMonitoredSyncBusy.containsKey(channel.id)) {
                    IntegralCountStat stat = channelToBusyStat.get(channel.id);
                    if (stat != null) {
                        stat.setParameters(getActualChannelParameters(channel.id, syncBusyGroupParams));
                    } else {
                        stat = new IntegralCountStat(getActualChannelParameters(channel.id, syncBusyGroupParams));
                        stat.start(new RegisteredItem<>(System.currentTimeMillis(), channel.getActiveSeancesCount()));
                        channelToBusyStat.put(channel.id, stat);
                    }
                }
            }
        }
    }

    private void readParametersFromDb() throws SQLException {
        final Collection<MetricParameters> activeMetrics = dbQueries.readActiveMetrics();
        explicitlyMonitoredClients.clear();
        explicitlyMonitoredConnCnt.clear();
        explicitlyMonitoredStatClients.clear();
        for (MetricParameters newParameters : activeMetrics) {
            if (EMetric.NET_SERVER_CONNECT.getKindName().equals((newParameters.getDescription().getKindName()))) {
                if (newParameters.getDescription().getSensorCoordinates().isGroup()) {
                    openConnGroupParams = newParameters;
                } else {
                    explicitlyMonitoredConnCnt.put(newParameters.getDescription().getSensorCoordinates().getNetChannelId(), newParameters);
                }
            } else if (EMetric.NET_CLIENT_CONNECT_TIME_PERCENT.getKindName().equals(newParameters.getDescription().getKindName())) {
                if (newParameters.getDescription().getSensorCoordinates().isGroup()) {
                    clientsStatGroupParams = newParameters;
                } else {
                    explicitlyMonitoredStatClients.put(newParameters.getDescription().getSensorCoordinates().getNetChannelId(), newParameters);
                }
            } else if (EMetric.NET_CLIENT_CONNECT.getKindName().equals(newParameters.getDescription().getKindName())) {
                if (newParameters.getDescription().getSensorCoordinates().isGroup()) {
                    clientsGroupParams = newParameters;
                } else {
                    explicitlyMonitoredClients.put(newParameters.getDescription().getSensorCoordinates().getNetChannelId(), newParameters);
                }
            } else if (EMetric.NET_SYNC_BUSY_CONN_CNT.getKindName().equals(newParameters.getDescription().getKindName())) {
                if (newParameters.getDescription().getSensorCoordinates().isGroup()) {
                    syncBusyGroupParams = newParameters;
                } else {
                    explicitlyMonitoredSyncBusy.put(newParameters.getDescription().getSensorCoordinates().getNetChannelId(), newParameters);
                }
            }
        }
        
        for (AbstractMetricController<?> controller: metricControllers) {
            controller.rereadSettings(getDbQueries());
            if (controller instanceof DurationStatMetricController) {
                ((DurationStatMetricController)controller).start();
            }
        }
        
        aasQueueSizeController.set(unit.getQueueSize());
        aasInvocationsPerSecController.tick(0);
    }

    @Override
    public void beforeWriterFlush() {
        final long flushTimeMillis = System.currentTimeMillis();
        final List<MetricRecord> toWrite = new ArrayList<>();
        for (IntegralCountStat stat : channelToConnStat.values()) {
            stat.flush(flushTimeMillis);
            toWrite.addAll(stat.popRecords());
        }
        for (IntegralCountStat stat : client2ConnectedPercentStat.values()) {
            stat.flush(flushTimeMillis);
            toWrite.addAll(stat.popRecords());
        }
        for (IntegralCountStat stat : channelToBusyStat.values()) {
            stat.flush(flushTimeMillis);
            toWrite.addAll(stat.popRecords());
        }
        
        for (AbstractMetricController<?> controller: metricControllers) {
            toWrite.addAll(controller.flush());
        }
        
        getWriter().add(toWrite);
    }

    @Override
    public void setConnection(Connection connection) {
        super.setConnection(connection);
        dbQueries.setConnection(connection);
    }

    private static class MonitorDbQueries {

        private PreparedStatement qryReadSettings = null;
        private Connection connection;
        private final NetPortHandlerUnit unit;

        public MonitorDbQueries(final NetPortHandlerUnit unit) {
            this.unit = unit;
        }

        public Collection<MetricParameters> readActiveMetrics() throws SQLException {
            if (qryReadSettings == null) {
                qryReadSettings = ((RadixConnection) connection).prepareStatement(qryReadActiveMetricsStmt);
                qryReadSettings.setLong(1, unit.getId());
            }
            try (ResultSet rs = qryReadSettings.executeQuery()) {
                final List<MetricParameters> parameters = new ArrayList<>();
                while (rs.next()) {
                    final String kind = rs.getString("kind");
                    final EMetric metric = EMetric.findForKind(kind);
                    if (Utils.<EMetric>in(metric, EMetric.NET_CLIENT_CONNECT, EMetric.NET_SERVER_CONNECT, EMetric.NET_CLIENT_CONNECT_TIME_PERCENT, EMetric.NET_SYNC_BUSY_CONN_CNT)) {
                        final long netChannelId = rs.getLong("netChannelId");
                        final SensorCoordinates coordinates = SensorCoordinates.forNetChannel(rs.wasNull() ? null : netChannelId);
                        parameters.add(new MetricParameters(new MetricDescription(metric, coordinates), rs.getLong("id"), rs.getLong("period") * 1000));
                    } else {
                        unit.getTrace().put(EEventSeverity.DEBUG, "Unsupported metric kind: " + kind, null, null, EEventSource.SYSTEM_MONITORING, false);
                    }
                }
                return parameters;
            }
        }

        public void setConnection(final Connection connection) {
            if (connection != this.connection) {
                close();
                this.connection = connection;
            }
        }

        public void close() {
            if (qryReadSettings != null) {
                try {
                    qryReadSettings.close();
                } catch (SQLException ex) {
                    //do nothing
                } finally {
                    qryReadSettings = null;
                }
            }
        }
    }
}
