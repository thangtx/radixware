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


public enum EPriority implements IKernelIntEnum {

    IDLE(1),
    VERY_LOW(2),
    LOW(3),
    BELOW_NORMAL(4),
    NORMAL(5),
    ABOVE_NORMAL(6),
    HIGH(7),
    VERY_HIGH(8),
    CRITICAL(9);
    private final long value;

    private EPriority(int value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public Long getValue() {
        return value;
    }

    @Override
    public boolean isInDomain(Id id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static EPriority getForValue(final Long val) {
        for (EPriority e : EPriority.values()) {
            if (e.value == val) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EPriority has no item with value: " + String.valueOf(val), val);
    }
}
