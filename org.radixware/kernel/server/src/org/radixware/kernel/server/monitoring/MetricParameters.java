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
 * Immutable object, that holds all information about metric, including settings
 * from database
 *
 */
public class MetricParameters {

    private final MetricDescription description;
    private final long periodMillis;
    private final long typeId;

    public MetricParameters(
            final MetricDescription description,
            final long typeId,
            final long periodMillis) {
        this.typeId = typeId;
        this.description = description;
        this.periodMillis = periodMillis;
    }

    public long getTypeId() {
        return typeId;
    }

    public long getPeriodMillis() {
        return periodMillis;
    }

    public MetricDescription getDescription() {
        return description;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + Objects.hashCode(this.description);
        hash = 31 * hash + (int) (this.periodMillis ^ (this.periodMillis >>> 32));
        hash = 31 * hash + (int) (this.typeId ^ (this.typeId >>> 32));
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
        final MetricParameters other = (MetricParameters) obj;
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (this.periodMillis != other.periodMillis) {
            return false;
        }
        if (this.typeId != other.typeId) {
            return false;
        }
        return true;
    }
}
