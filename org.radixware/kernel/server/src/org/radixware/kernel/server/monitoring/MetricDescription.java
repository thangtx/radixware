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
 * Database-independent information about metric
 *
 */
public class MetricDescription {

    private final String kindName;
    private final EMetricType metricType;
    private final SensorCoordinates sensorCoordinates;

    public MetricDescription(final EMetric metric, final SensorCoordinates sensorCoordinates) {
        this(metric.getKindName(), metric.getMetricType(), sensorCoordinates);
    }

    public MetricDescription(String kind, EMetricType metricType, SensorCoordinates sensorCoordinates) {
        this.kindName = kind;
        this.metricType = metricType;
        this.sensorCoordinates = sensorCoordinates;
    }

    public String getKindName() {
        return kindName;
    }

    public EMetricType getMetricType() {
        return metricType;
    }

    public SensorCoordinates getSensorCoordinates() {
        return sensorCoordinates;
    }
}
