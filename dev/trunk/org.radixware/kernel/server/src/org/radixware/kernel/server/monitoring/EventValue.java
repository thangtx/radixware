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

/**
 * NonThreadSafe
 *
 */
public class EventValue extends MetricValue implements IEventValue {

    private final Double oldValue;
    private final Double newValue;
    private final IStatValue intermediateStat;

    public EventValue(final long oldValue, final long newValue, final long endTimeMillis) {
        this(oldValue * 1., newValue * 1., null, endTimeMillis);
    }

    public EventValue(final Double oldValue, final Double newValue, final long endTimeMillis) {
        this(oldValue, newValue, null, endTimeMillis);
    }
    
    public EventValue(final Double oldValue, final Double newValue, final IStatValue intermediateStat, final long endTimeMillis) {
        super(endTimeMillis, endTimeMillis);
        this.newValue = newValue;
        this.oldValue = oldValue;
        this.intermediateStat = intermediateStat;
    }

    @Override
    public Double getNewValue() {
        return newValue;
    }

    @Override
    public Double getOldValue() {
        return oldValue;
    }

    @Override
    public IStatValue getIntermediateStat() {
        return intermediateStat;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.oldValue);
        hash = 71 * hash + Objects.hashCode(this.newValue);
        hash = 71 * hash + Objects.hashCode(this.intermediateStat);
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
        final EventValue other = (EventValue) obj;
        if (!Objects.equals(this.oldValue, other.oldValue)) {
            return false;
        }
        if (!Objects.equals(this.newValue, other.newValue)) {
            return false;
        }
        if (!Objects.equals(this.intermediateStat, other.intermediateStat)) {
            return false;
        }
        return true;
    }
    
    
    
}
