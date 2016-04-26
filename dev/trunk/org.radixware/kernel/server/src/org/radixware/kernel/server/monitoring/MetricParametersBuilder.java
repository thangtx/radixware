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


public class MetricParametersBuilder {

    private long periodMillis = 60000;//default value to be > 0
    private MetricDescription description;
    private long typeId;

    public void setPeriodMillis(final long periodMillis) {
        this.periodMillis = periodMillis;
    }

    public void setDescription(final MetricDescription description) {
        this.description = description;
    }

    public void setTypeId(final long typeId) {
        this.typeId = typeId;
    }

    public void fillFrom(final MetricParameters source) {
        description = source.getDescription();
        periodMillis = source.getPeriodMillis();
        typeId = source.getTypeId();
    }

    public MetricParameters getMetricParameters() {
        return new MetricParameters(description,
                typeId,
                periodMillis);
    }
}
