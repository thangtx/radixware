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
package org.radixware.kernel.server.units.mq;

import java.sql.SQLException;
import java.util.*;
import org.radixware.kernel.server.monitoring.*;
import org.radixware.kernel.server.units.Unit;

public class MqUnitMonitor extends AbstractMonitor {

    private final IntegralStatMetricController aasSeancesCountController;
    private final FreqStatController messagesPerSecController;
    private final DurationStatMetricController processingDurationController;

    public MqUnitMonitor(final Unit unit) {
        super(unit.getDbConnection(), unit.getTrace(), unit.getEventSource());
        final MetricDescription aasSeancesDesc = new MetricDescription(EMetric.UNIT_MQ_AAS_SEANCES_COUNT.getKindName(), EMetricType.STATISTIC, SensorCoordinates.forUnit(unit.getId()));
        aasSeancesCountController = new IntegralStatMetricController(aasSeancesDesc);
        final MetricDescription receivedMessagesCountMetricDesc = new MetricDescription(EMetric.UNIT_MQ_MESSAGE_COUNT.getKindName(), EMetricType.STATISTIC, SensorCoordinates.forUnit(unit.getId()));
        messagesPerSecController = new FreqStatController(receivedMessagesCountMetricDesc);
        final MetricDescription processingDurationCountMetircDesc = new MetricDescription(EMetric.UNIT_MQ_PROC_DURATION.getKindName(), EMetricType.STATISTIC, SensorCoordinates.forUnit(unit.getId()));
        processingDurationController = new DurationStatMetricController(processingDurationCountMetircDesc);
    }
    
    public void onProcessStart() {
        aasSeancesCountController.increment();
    }

    public void onProcessFinished(final long durationMs) {
        aasSeancesCountController.decrement();
        messagesPerSecController.tick();
        processingDurationController.register(new RegisteredItem<>(System.currentTimeMillis(), durationMs));
    }
    

    @Override
    public void beforeWriterFlush() {
        final List<MetricRecord> toWrite = new ArrayList<>();
        toWrite.addAll(aasSeancesCountController.flush());
        toWrite.addAll(messagesPerSecController.flush());
        toWrite.addAll(processingDurationController.flush());
        getWriter().add(toWrite);
    }

    @Override
    public void rereadSettings() throws SQLException {
        aasSeancesCountController.rereadSettings(getDbQueries());
        aasSeancesCountController.set(aasSeancesCountController.getCurrentCount());
        messagesPerSecController.rereadSettings(getDbQueries());
        messagesPerSecController.tick(0);
        processingDurationController.rereadSettings(getDbQueries());
        processingDurationController.ensureStarted(new RegisteredItem<>(System.currentTimeMillis(), 0l));
    }
}
