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

import java.util.Objects;


public class StatValue extends MetricValue implements IStatValue {

    private final Double min;
    private final Double max;
    private final double avg;

    public StatValue(final StatValue value, final long begTimeMillis, final long endTimeMillis) {
        this(value.min, value.max, value.avg, begTimeMillis, endTimeMillis);
    }

    public StatValue(final Double min, final Double max, final double avg, final long begTimeMillis, final long endTimeMillis) {
        super(begTimeMillis, endTimeMillis);
        this.min = min;
        this.max = max;
        this.avg = avg;
    }

    public StatValue(final Double min, final Double max, final double avg) {
        super(-1, -1);
        this.min = min;
        this.max = max;
        this.avg = avg;
    }

    @Override
    public Double getMin() {
        return min;
    }

    @Override
    public Double getMax() {
        return max;
    }

    @Override
    public double getAvg() {
        return avg;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.min);
        hash = 79 * hash + Objects.hashCode(this.max);
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.avg) ^ (Double.doubleToLongBits(this.avg) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StatValue other = (StatValue) obj;
        if (!Objects.equals(this.min, other.min)) {
            return false;
        }
        if (!Objects.equals(this.max, other.max)) {
            return false;
        }
        if (Double.doubleToLongBits(this.avg) != Double.doubleToLongBits(other.avg)) {
            return false;
        }
        return true;
    }
}
