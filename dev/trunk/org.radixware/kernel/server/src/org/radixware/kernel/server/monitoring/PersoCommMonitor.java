/*
 * Copyright (c) 2008-2017, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */

package org.radixware.kernel.server.monitoring;

import java.sql.SQLException;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.server.types.ProfileStatisticEntry;
import org.radixware.kernel.server.units.persocomm.MessageSendResult;
import org.radixware.kernel.server.units.persocomm.PersoCommUnit;

public class PersoCommMonitor extends AbstractMonitor {
    
    private static final int MONITORING_SETTINGS_REREAD_PERIOD_MILLIS = 60000;
    private static final int MONITORING_FLUSH_PERIOD_MILLIS = 30000;
    
    private long lastMonitoringSettingsRereadMillis = System.currentTimeMillis();
    private long lastMonitoringFlushMillis = System.currentTimeMillis();
    private long lastMonitoringOutgoingQueueSizeMillis = 0;
    
    final PersoCommUnit unit;

    // Explicit ***Stat and related objects
    private final EMetric[] explicitStatMetrics = { EMetric.UNIT_PC_MESSAGES_PER_SEC_SENT, EMetric.UNIT_PC_MESSAGE_SEND_DURATION,
        EMetric.UNIT_PC_MESSAGES_SENT_CNT, EMetric.UNIT_PC_MESSAGES_NOT_SENT_CNT, EMetric.UNIT_PC_MESSAGES_RECEIVED_CNT, 
        EMetric.UNIT_PC_MESSAGE_RECEIVED_PROC_DURATION
    };
    
    private final MetricDescription[] explicitStatMetricDescriptions = new MetricDescription[explicitStatMetrics.length];
    
    private final MetricParameters[] explicitStatMetricParameters = new MetricParameters[explicitStatMetrics.length];
    
    private final FrequencyPerSecondStat messagesPerSecondSendStat;
    private final DurationStat messageSendDurationStat;
    private final CountStat messagesSentCountStat;
    private final CountStat messagesNotSentCountStat;
    private final CountStat messagesReceivedCountStat;
    private final DurationStat messageReceivedProcDurationStat;
    private final AbstractStat<?>[] explicitStats = new AbstractStat<?>[explicitStatMetrics.length];
    
    // implicit toBeStat objects
    private final MetricDescription outgoingQueueSizeMetricDescription;
    MetricParameters outgoingQueueSizeMetricParameters;
    
    public PersoCommMonitor(PersoCommUnit unit) {
        super(unit.getDbConnection(), unit.getTrace(), EEventSource.PERSOCOMM_UNIT.getValue());
        this.unit = unit;
        
        final SensorCoordinates sensorCoordinates = SensorCoordinates.forUnit(unit.getId());
        this.outgoingQueueSizeMetricDescription = new MetricDescription(EMetric.UNIT_PC_OUTGOING_QUEUE_SIZE, sensorCoordinates);
        
        for (int i = 0; i < explicitStatMetrics.length; ++i) {
            this.explicitStatMetricDescriptions[i] = new MetricDescription(this.explicitStatMetrics[i], sensorCoordinates);
        }
 
        this.explicitStats[0] = this.messagesPerSecondSendStat = new FrequencyPerSecondStat(null, System.currentTimeMillis());
        this.explicitStats[1] = this.messageSendDurationStat = new DurationStat(null, System.currentTimeMillis());
        this.explicitStats[2] = this.messagesSentCountStat = new CountStat(null, System.currentTimeMillis());
        this.explicitStats[3] = this.messagesNotSentCountStat = new CountStat(null, System.currentTimeMillis());
        this.explicitStats[4] = this.messagesReceivedCountStat = new CountStat(null, System.currentTimeMillis());
        this.explicitStats[5] = this.messageReceivedProcDurationStat = new DurationStat(null, System.currentTimeMillis());
    }
    
    public void tic() throws SQLException {
        final long nowMillis = System.currentTimeMillis();
        
        final long nextOutgoingQueueMillis = outgoingQueueSizeMetricParameters == null ? Long.MAX_VALUE
                : lastMonitoringOutgoingQueueSizeMillis + outgoingQueueSizeMetricParameters.getPeriodMillis();
        if (nowMillis > nextOutgoingQueueMillis) {
            long queueSize = unit.getDBQuery().getOutgoingQueueSize(unit.getPrimaryUnitId());
            getWriter().add(new MetricRecord(outgoingQueueSizeMetricParameters, new EventValue(null, (double) queueSize, nowMillis)));
            lastMonitoringOutgoingQueueSizeMillis = System.currentTimeMillis();
        }
        
        if (nowMillis > lastMonitoringFlushMillis + MONITORING_FLUSH_PERIOD_MILLIS) {
            flush();
            lastMonitoringFlushMillis = System.currentTimeMillis();
        }
        
        if (nowMillis > lastMonitoringSettingsRereadMillis + MONITORING_SETTINGS_REREAD_PERIOD_MILLIS) {
            rereadSettings();
            lastMonitoringSettingsRereadMillis = System.currentTimeMillis();
        }
    }
    
    public void registerSentMessage(MessageSendResult result) {
        if (result.messageId == null)
            return;
        
        final long nowMillis = System.currentTimeMillis();
        final long nowNanos = System.nanoTime();
        final long sendDurationNanos = nowNanos - result.sendBeginTimeNanos;
        
        if (messagesPerSecondSendStat.getParameters() != null) {
            messagesPerSecondSendStat.append(new RegisteredItem<>(nowMillis, 1.0));
        }
        if (messageSendDurationStat.getParameters() != null) {
            final ProfileStatisticEntry entry = new ProfileStatisticEntry(sendDurationNanos);
            messageSendDurationStat.append(new RegisteredItem<>(nowMillis, entry));
        }
        if (messagesSentCountStat.getParameters() != null) {
            messagesSentCountStat.append(new RegisteredItem<>(nowMillis, 1.0));
        }
    }
    
    public void registerNotSentMessage() {
        final long nowMillis = System.currentTimeMillis();
        if (messagesNotSentCountStat.getParameters() != null) {
            messagesNotSentCountStat.append(new RegisteredItem<>(nowMillis, 1.0));
        }
    }
    
    public void regiesteReceivedMessage(long procDurationNanos) {
        final long nowMillis = System.currentTimeMillis();
        
        if (messagesReceivedCountStat.getParameters() != null) {
            messagesReceivedCountStat.append(new RegisteredItem<>(nowMillis, 1.0));
        }
        if (messageReceivedProcDurationStat.getParameters() != null) {
            final ProfileStatisticEntry entry = new ProfileStatisticEntry(procDurationNanos);
            messageReceivedProcDurationStat.append(new RegisteredItem<>(nowMillis, entry));
        }
    }

    @Override
    public void rereadSettings() throws SQLException {
        outgoingQueueSizeMetricParameters = getDbQueries().readMetricParameters(outgoingQueueSizeMetricDescription);
        for (int i = 0; i < explicitStatMetricDescriptions.length; ++i) {
            explicitStatMetricParameters[i] = getDbQueries().readMetricParameters(explicitStatMetricDescriptions[i]);
            explicitStats[i].setParameters(explicitStatMetricParameters[i]);
        }
    }
    
    @Override
    public void beforeWriterFlush() {
        final long nowMillis = System.currentTimeMillis();
        for (AbstractStat<?> stat : explicitStats) {
            if (stat.getParameters() != null) {
                stat.processToTime(nowMillis);
                getWriter().add(stat.popRecords());
            }
        }
    }

}
