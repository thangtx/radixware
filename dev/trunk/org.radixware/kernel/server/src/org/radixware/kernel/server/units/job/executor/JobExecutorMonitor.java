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

package org.radixware.kernel.server.units.job.executor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.monitoring.AbstractMonitor;
import org.radixware.kernel.server.monitoring.EMetric;
import org.radixware.kernel.server.monitoring.EMetricType;
import org.radixware.kernel.server.monitoring.IStatValue;
import org.radixware.kernel.server.monitoring.IntegralCountStat;
import org.radixware.kernel.server.monitoring.IntegralStatMetricController;
import org.radixware.kernel.server.monitoring.MetricDescription;
import org.radixware.kernel.server.monitoring.MetricParameters;
import org.radixware.kernel.server.monitoring.MetricRecord;
import org.radixware.kernel.server.monitoring.RegisteredItem;
import org.radixware.kernel.server.monitoring.SensorCoordinates;
import org.radixware.kernel.server.units.Unit;


public class JobExecutorMonitor extends AbstractMonitor {

    private final IntegralStatMetricController execJobCountMetricController;
    private final IntegralStatMetricController waitingJobCountMetricController;
    private final IntegralCountStat execJobCountStat = new IntegralCountStat(new MetricParameters(null, -1, Instance.WRITE_BASIC_STATS_PERIOD_MILLIS));
    private final IntegralCountStat waitJobCountStat = new IntegralCountStat(new MetricParameters(null, -1, Instance.WRITE_BASIC_STATS_PERIOD_MILLIS));
    private double avgExecJobCount = 0;
    private double avgWaitJobCount = 0;

    public JobExecutorMonitor(final Unit unit) {
        super(unit.getDbConnection(), unit.getTrace(), unit.getEventSource());
        final MetricDescription jobCountDesc = new MetricDescription(EMetric.UNIT_JOB_EXEC_CNT.getKindName(), EMetricType.STATISTIC, SensorCoordinates.forUnit(unit.getId()));
        execJobCountMetricController = new IntegralStatMetricController(jobCountDesc);
        final MetricDescription waitingJobCountDesc = new MetricDescription(EMetric.UNIT_JOB_WAIT_CNT.getKindName(), EMetricType.STATISTIC, SensorCoordinates.forUnit(unit.getId()));
        waitingJobCountMetricController = new IntegralStatMetricController(waitingJobCountDesc);
        execJobCountStat.start(new RegisteredItem<>(System.currentTimeMillis(), 0));
        waitJobCountStat.start(new RegisteredItem<>(System.currentTimeMillis(), 0));
    }

    public void jobStarted() {
        execJobCountMetricController.increment();
        execJobCountStat.append(new RegisteredItem<>(System.currentTimeMillis(), execJobCountMetricController.getCurrentCount()));
    }

    public void jobDone() {
        execJobCountMetricController.decrement();
        execJobCountStat.append(new RegisteredItem<>(System.currentTimeMillis(), execJobCountMetricController.getCurrentCount()));
    }

    public void setWaitingJobsCount(final int count) {
        waitingJobCountMetricController.set(count);
        waitJobCountStat.append(new RegisteredItem<>(System.currentTimeMillis(), waitingJobCountMetricController.getCurrentCount()));
    }
    
    public double getAvgExecJobCount() {
        final IStatValue newVal = execJobCountStat.flushAndGetLastValue();
        if (newVal != null) {
            avgExecJobCount = newVal.getAvg();
        }
        return avgExecJobCount;
    }
    
    public double getAvgWaitJobCount() {
        final IStatValue newVal = waitJobCountStat.flushAndGetLastValue();
        if (newVal != null) {
            avgWaitJobCount = newVal.getAvg();
        }
        return avgWaitJobCount;
    }

    @Override
    public void beforeWriterFlush() {
        final List<MetricRecord> toWrite = new ArrayList<>();
        toWrite.addAll(execJobCountMetricController.flush());
        toWrite.addAll(waitingJobCountMetricController.flush());
        getWriter().add(toWrite);
    }

    @Override
    public void rereadSettings() throws SQLException {
        execJobCountMetricController.rereadSettings(getDbQueries());
        waitingJobCountMetricController.rereadSettings(getDbQueries());
    }
    
}
