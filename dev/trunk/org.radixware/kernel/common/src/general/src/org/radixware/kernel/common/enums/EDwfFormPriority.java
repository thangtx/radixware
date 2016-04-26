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

public enum EDwfFormPriority implements IKernelIntEnum {

    ABOVE_NORMAL(new Long(6)),
    BELOW_NORMAL(new Long(4)),
    CRITICAL(new Long(9)),
    HIGH(new Long(7)),
    IDLE(new Long(1)),
    LOW(new Long(3)),
    NORMAL(new Long(5)),
    VERY_HIGH(new Long(8)),
    VERY_LOW(new Long(2));
    private final Long value;

    @Override
    public Long getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name();
    }
    //constructors   

    private EDwfFormPriority(Long x) {
        value = x;
    }

    public static EDwfFormPriority getForValue(final Long val) {
        for (EDwfFormPriority e : EDwfFormPriority.values()) {
            if (e.value.longValue() == val) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EDwfFormPriority has no item with value: " + String.valueOf(val),val);
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }
}