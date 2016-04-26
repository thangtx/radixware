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

package org.radixware.kernel.server.units.netport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EMetricKind;
import org.radixware.kernel.server.monitoring.*;


public class NetPortHandlerMonitor extends AbstractMonitor {

    private final NetPortHandlerUnit unit;
    private final Map<Long, IntegralCountStat> listener2stat = new HashMap<>();
    private final Map<Long, IntegralCountStat> client2stat = new HashMap<>();
    private final Map<Long, MetricParameters> explicitlyMonitoredClients = new HashMap<>();
    private final Map<Long, MetricParameters> explicitlyMonitoredStatClients = new HashMap<>();
    private final Map<Long, MetricParameters> explicitlyMonitoredListeners = new HashMap<>();
    private final Set<String> initValueWasWritten = new HashSet<>();
    private MetricParameters listenerGroupParams = null;
    private MetricParameters clientsGroupParams = null;
    private MetricParameters clientsStatGroupParams = null;
    private final MonitorDbQueries dbQueries;

    public NetPortHandlerMonitor(NetPortHandlerUnit unit) {
        super(unit.getDbConnection(), unit.getTrace(), unit.getEventSource());
        this.unit = unit;
        dbQueries = new MonitorDbQueries(unit);
        dbQueries.setConnection(unit.getDbConnection());
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
        final IntegralCountStat statController = client2stat.get(clientId);
        if (statController == null && clientsStatGroupParams != null) {
            //new channel has been loaded since last rereadParams
            final IntegralCountStat stat = new IntegralCountStat(getActualChannelParameters(clientId, clientsStatGroupParams));
            stat.start(new RegisteredItem<>(currentTimeMillis, 100));
            client2stat.put(clientId, stat);
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
        final IntegralCountStat statController = client2stat.get(clientId);
        if (statController != null) {
            statController.append(new RegisteredItem<>(currentTimeMillis, 0));
        }
    }

    void seanceOpened(final Seance seance) {
        if (seance.channel instanceof NetClient) {
            clientConnected(seance.channel);
        } else {
            serverConnectionOpened(seance.channel);
        }
        seance.channel.setNeedUpdateDbStats(true);
    }

    void seanceClosed(final Seance seance) {
        if (seance.channel instanceof NetClient) {
            clientDisconnected(seance.channel);
        } else {
            serverConnectionClosed(seance.channel);
        }
        seance.channel.setNeedUpdateDbStats(true);
    }

    private MetricParameters getActualChannelParameters(final long clientId, final MetricParameters groupParameters) {
        return new MetricParameters(new MetricDescription(EMetric.findForKind(groupParameters.getDescription().getKindName()), SensorCoordinates.forNetChannel(clientId)), groupParameters.getTypeId(), groupParameters.getPeriodMillis());
    }

    void serverConnectionOpened(final NetChannel channel) {
        final IntegralCountStat statController = listener2stat.get(channel.id);
        if (statController == null && listenerGroupParams != null) {
            //new channel has been loaded since last rereadParams
            final IntegralCountStat stat = new IntegralCountStat(getActualChannelParameters(channel.id, listenerGroupParams));
            stat.start(new RegisteredItem<>(System.currentTimeMillis(), channel.getActiveSeanceCount()));
            listener2stat.put(channel.id, stat);
        }
        if (statController != null) {
            statController.append(new RegisteredItem<>(System.currentTimeMillis(), channel.getActiveSeanceCount()));
        }
    }

    void serverConnectionClosed(final NetChannel channel) {
        final IntegralCountStat statController = listener2stat.get(channel.id);
        if (statController != null) {
            statController.append(new RegisteredItem<>(System.currentTimeMillis(), channel.getActiveSeanceCount()));
        }
    }

    @Override
    public void rereadSettings() throws SQLException {
        readParametersFromDb();
        beforeWriterFlush();//flush currently collected data
        applyParamsForListeners();
        applyParamsForClients();
    }

    private void applyParamsForClients() {
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
                        getWriter().add(new MetricRecord(getActualChannelParameters(channel.id, parameters), new EventValue(channel.getActiveSeanceCount(), channel.getActiveSeanceCount(), System.currentTimeMillis())));
                    }
                }
            }
        }
    }

    private void applyParamsForClientsStatMetric() {
        for (Map.Entry<Long, MetricParameters> entry : explicitlyMonitoredStatClients.entrySet()) {
            final MetricParameters newParameters = entry.getValue();
            final Long netClientId = newParameters.getDescription().getSensorCoordinates().getNetChannelId();
            IntegralCountStat stat = client2stat.get(netClientId);
            if (stat != null) {
                stat.setParameters(newParameters);
            } else {
                stat = new IntegralCountStat(newParameters);
                final NetChannel channel = unit.getChannels().findChannel(netClientId, false);
                if (channel != null) {
                    stat.start(new RegisteredItem<>(System.currentTimeMillis(), channel.getActiveSeanceCount()));
                } else {
                    stat.start(new RegisteredItem<>(System.currentTimeMillis(), 0));
                }
                client2stat.put(netClientId, stat);
            }
        }

        if (clientsStatGroupParams == null) {
            Iterator<Map.Entry<Long, IntegralCountStat>> iterator = client2stat.entrySet().iterator();
            while (iterator.hasNext()) {
                if (!explicitlyMonitoredStatClients.containsKey(iterator.next().getKey())) {
                    iterator.remove();
                }
            }
        } else {
            for (NetChannel client : unit.getNetChannelsSnapshot()) {
                if (client instanceof NetClient && !explicitlyMonitoredStatClients.containsKey(client.id)) {
                    IntegralCountStat stat = client2stat.get(client.id);
                    if (stat != null) {
                        stat.setParameters(getActualChannelParameters(client.id, clientsStatGroupParams));
                    } else {
                        stat = new IntegralCountStat(getActualChannelParameters(client.id, clientsStatGroupParams));
                        stat.start(new RegisteredItem<>(System.currentTimeMillis(), client.getActiveSeanceCount()));
                        client2stat.put(client.id, stat);
                    }
                }
            }
        }
    }

    private void applyParamsForListeners() {
        for (Map.Entry<Long, MetricParameters> entry : explicitlyMonitoredListeners.entrySet()) {
            final MetricParameters newParameters = entry.getValue();
            final Long netListenerId = newParameters.getDescription().getSensorCoordinates().getNetChannelId();
            IntegralCountStat stat = listener2stat.get(netListenerId);
            if (stat != null) {
                stat.setParameters(newParameters);
            } else {
                stat = new IntegralCountStat(newParameters);
                final NetChannel channel = unit.getChannels().findChannel(netListenerId, false);
                if (channel != null) {
                    stat.start(new RegisteredItem<>(System.currentTimeMillis(), channel.getActiveSeanceCount()));
                } else {
                    stat.start(new RegisteredItem<>(System.currentTimeMillis(), 0));
                }
                listener2stat.put(netListenerId, stat);
            }
        }

        if (listenerGroupParams == null) {
            Iterator<Map.Entry<Long, IntegralCountStat>> iterator = listener2stat.entrySet().iterator();
            while (iterator.hasNext()) {
                if (!explicitlyMonitoredListeners.containsKey(iterator.next().getKey())) {
                    iterator.remove();
                }
            }
        } else {
            for (NetChannel listener : unit.getNetChannelsSnapshot()) {
                if (listener instanceof NetListener && !explicitlyMonitoredListeners.containsKey(listener.id)) {
                    IntegralCountStat stat = listener2stat.get(listener.id);
                    if (stat != null) {
                        stat.setParameters(getActualChannelParameters(listener.id, listenerGroupParams));
                    } else {
                        stat = new IntegralCountStat(getActualChannelParameters(listener.id, listenerGroupParams));
                        stat.start(new RegisteredItem<>(System.currentTimeMillis(), listener.getActiveSeanceCount()));
                        listener2stat.put(listener.id, stat);
                    }
                }
            }
        }
    }

    private void readParametersFromDb() throws SQLException {
        final Collection<MetricParameters> activeMetrics = dbQueries.readActiveMetrics();
        explicitlyMonitoredClients.clear();
        explicitlyMonitoredListeners.clear();
        explicitlyMonitoredStatClients.clear();
        for (MetricParameters newParameters : activeMetrics) {
            if (EMetric.NET_SERVER_CONNECT.getKindName().equals((newParameters.getDescription().getKindName()))) {
                if (newParameters.getDescription().getSensorCoordinates().isGroup()) {
                    listenerGroupParams = newParameters;
                } else {
                    explicitlyMonitoredListeners.put(newParameters.getDescription().getSensorCoordinates().getNetChannelId(), newParameters);
                }
            } else if (EMetric.NET_CLIENT_CONNECT_TIME_PERCENT.getKindName().equals(newParameters.getDescription().getKindName())) {
                if (newParameters.getDescription().getSensorCoordinates().isGroup()) {
                    clientsStatGroupParams = newParameters;
                } else {
                    explicitlyMonitoredStatClients.put(newParameters.getDescription().getSensorCoordinates().getNetChannelId(), newParameters);
                }
            } else {
                if (newParameters.getDescription().getSensorCoordinates().isGroup()) {
                    clientsGroupParams = newParameters;
                } else {
                    explicitlyMonitoredClients.put(newParameters.getDescription().getSensorCoordinates().getNetChannelId(), newParameters);
                }
            }
        }
    }

    @Override
    public void beforeWriterFlush() {
        final long flushTimeMillis = System.currentTimeMillis();
        final List<MetricRecord> toWrite = new ArrayList<>();
        for (IntegralCountStat stat : listener2stat.values()) {
            stat.flush(flushTimeMillis);
            toWrite.addAll(stat.popRecords());
        }
        for (IntegralCountStat stat : client2stat.values()) {
            stat.flush(flushTimeMillis);
            toWrite.addAll(stat.popRecords());
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
                qryReadSettings = connection.prepareStatement("select m.id, m.kind, m.netChannelId, m.period from rdx_sm_metrictype m, rdx_netchannel n "
                        + " where "
                        + " ( "
                        + " ( n.id = m.netChannelId ) "
                        + " or "
                        + " ( (m.kind = '" + EMetricKind.NET_SERVER_CONNECT.getValue() + "' or m.kind = '" + EMetricKind.NET_CLIENT_CONNECT.getValue() + "' or m.kind = '" + EMetricKind.NET_CLIENT_CONNECT_TIME_PERCENT.getValue() + "') and m.netChannelId is null)"
                        + " ) "
                        + " and m.enabled = 1 and n.unitId = ?");
                qryReadSettings.setLong(1, unit.getId());
            }
            try (ResultSet rs = qryReadSettings.executeQuery()) {
                final List<MetricParameters> parameters = new ArrayList<>();
                while (rs.next()) {
                    final String kind = rs.getString("kind");
                    final EMetric metric = EMetric.findForKind(kind);
                    if (metric != null && (metric == EMetric.NET_CLIENT_CONNECT || metric == EMetric.NET_SERVER_CONNECT || metric == EMetric.NET_CLIENT_CONNECT_TIME_PERCENT)) {
                        final SensorCoordinates coordinates;
                        final long netChannelId = rs.getLong("netChannelId");
                        coordinates = SensorCoordinates.forNetChannel(rs.wasNull() ? null : netChannelId);
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
