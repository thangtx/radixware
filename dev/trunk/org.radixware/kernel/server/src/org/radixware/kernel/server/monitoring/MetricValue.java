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
 * Abstract metric value with begin and end time
 */
public class MetricValue implements IMetricValue {

    private final long begTimeMillis;
    private final long endTimeMillis;


    public MetricValue(long begTimeMillis, long endTimeMillis) {
        this.begTimeMillis = begTimeMillis;
        this.endTimeMillis = endTimeMillis;
    }

    @Override
    public long getBegTimeMillis() {
        return begTimeMillis;
    }

    @Override
    public long getEndTimeMillis() {
        return endTimeMillis;
    }
}
