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

package org.radixware.kernel.common.enums;

import java.util.List;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.IKernelIntEnum;
import org.radixware.kernel.common.types.Id;


public enum ESensorType implements IKernelIntEnum {

    NONE(0, "None"),
    INSTANCE(1, "Instance"),
    UNIT(2, "Unit"),
    TIMING_SECTION(3, "TimingSection"),
    INSTANCE_SERVICE(4, "InstanceService"),
    NET_CHANNEL(5, "NetListener");
    private final long value;
    private final String name;

    private ESensorType(final long value, final String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Long getValue() {
        return value;
    }

    @Override
    public boolean isInDomain(final Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(final List<Id> ids) {
        return false;
    }

    public static ESensorType getForValue(final Long val) {
        for (ESensorType t : ESensorType.values()) {
            if (t.getValue() == null && val == null || t.getValue().equals(val)) {
                return t;
            }
        }
        throw new NoConstItemWithSuchValueError("ESensorType has no item with value: " + String.valueOf(val), val);
    }
}
