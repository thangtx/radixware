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

public enum EDwfCalendarItemKind implements IKernelIntEnum {

    //constant values for compiling
    DAY_OF_MONTH_ABS(new Long(1)),
    DAY_OF_MONTH_REL(new Long(2)),
    DAY_OF_WEEK_ABS(new Long(3)),
    DAY_OF_WEEK_REL(new Long(4)),
    CALENDAR_ABS(new Long(5)),
    CALENDAR_REL(new Long(6)),
    DATE(new Long(7));
    //constructors
    private final Long value;

    private EDwfCalendarItemKind(Long x) {
        value = x;
    }

    @Override
    public Long getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name();
    }

    public static EDwfCalendarItemKind getForValue(final long val) {
        for (EDwfCalendarItemKind e : EDwfCalendarItemKind.values()) {
            if (e.value.longValue() == val) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EDwfCalendarItemKind has no item with value: " + String.valueOf(val),val);
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
