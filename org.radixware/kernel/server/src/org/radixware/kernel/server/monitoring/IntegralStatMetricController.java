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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class IntegralStatMetricController extends AbstractMetricController<Integer> {

    private static final RegisteredItem<Integer> INCREMENT_REQUEST = new RegisteredItem<>(-1, -1);
    private static final RegisteredItem<Integer> DECREMENT_REQUEST = new RegisteredItem<>(-1, -1);
    private final MetricDescription metricDescription;
    private IntegralCountStat stat = null;
    private int currentCount = 0;

    public IntegralStatMetricController(final MetricDescription metricDescription) {
        this.metricDescription = metricDescription;
    }

    @Override
    protected List<MetricRecord> flushInternal() {
        final List<MetricRecord> toWrite = new ArrayList<>();
        if (stat != null) {
            toWrite.addAll(stat.popRecords());
            if (stat.getState() == EMetricState.DISABLED) {
                stat = null;
            }
        }
        return toWrite;
    }

    @Override
    protected void process(final List<RegisteredItem<Integer>> collectedData) {
        if (stat != null && !collectedData.isEmpty()) {
            if (!stat.isStarted()) {
                stat.start(collectedData.get(0));
            }
            stat.appendAll(collectedData);
        }
    }

    @Override
    protected Runnable createSettingsUpdateTask(final MonitoringDbQueries dbQueries) throws SQLException {
        final MetricParameters parameters = dbQueries.readMetricParameters(metricDescription);
        return new Runnable() {
            @Override
            public void run() {
                if (stat != null) {
                    if (parameters == null) {
                        stat.setState(EMetricState.DISABLED);
                    } else {
                        stat.setParameters(parameters);
                    }

                } else if (parameters != null) {
                    stat = new IntegralCountStat(parameters);
                }
            }
        };
    }

    public void increment() {
        register(INCREMENT_REQUEST);
    }

    public void decrement() {
        register(DECREMENT_REQUEST);
    }

    public void set(final int count) {
        register(new RegisteredItem<>(System.currentTimeMillis(), count));
    }

    public int getCurrentCount() {
        return currentCount;
    }

    @Override
    protected RegisteredItem<Integer> beforeRegister(final RegisteredItem<Integer> item) {
        if (item == INCREMENT_REQUEST) {
            return new RegisteredItem<>(System.currentTimeMillis(), ++currentCount);
        } else if (item == DECREMENT_REQUEST) {
            return new RegisteredItem<>(System.currentTimeMillis(), --currentCount);
        } else {
            if (item != null) {
                currentCount = item.val;
            }
            return item;
        }
    }
}
