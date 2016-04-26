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

/**
 * All information required to write metric value to the database
 *
 */
public class MetricRecord {

    private final IStatValue statValue;
    private final IEventValue eventValue;
    private final MetricParameters metricParameters;

    public MetricRecord(final MetricParameters parameters, final IStatValue statValue) {
        this(parameters, statValue, null);
    }

    public MetricRecord(final MetricParameters parameters, final IEventValue eventValue) {
        this(parameters, null, eventValue);
    }

    @Deprecated
    public MetricRecord(final MetricParameters parameters, final IStatValue statValue, final IEventValue eventValue) {
        this.metricParameters = parameters;
        this.statValue = statValue;
        this.eventValue = eventValue;
    }

    public MetricParameters getParameters() {
        return metricParameters;
    }

    public IEventValue getEventValue() {
        return eventValue;
    }

    public IStatValue getStatValue() {
        return statValue;
    }

    @Override
    public String toString() {
        return "metric[type=" + metricParameters.getDescription().getKindName() + " id=" + metricParameters.getTypeId() + " kind=" + metricParameters.getDescription().getKindName() + "] "
                + (metricParameters.getDescription().getMetricType() == EMetricType.STATISTIC ? "min=" + statValue.getMin()
                + " max=" + statValue.getMax() + " avg=" + statValue.getAvg()
                : "oldValue=" + eventValue.getOldValue() + " newValue=" + eventValue.getNewValue() + " time=" + eventValue.getEndTimeMillis());
    }
}
