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


public enum EPropNature implements IKernelIntEnum {

    DETAIL_PROP(5),
    DYNAMIC(2),
    EXPRESSION(6),
    FIELD(3),
    FIELD_REF(10),
    //  FORM_PROPERTY(9),
    GROUP_PROPERTY(12),
    SQL_CLASS_PARAMETER(11),
    INNATE(0),
    PARENT_PROP(4),
    PROPERTY_PRESENTATION(8),
    EVENT_CODE(14),
    USER(1),
    VIRTUAL(16);
    
    private final Long val;

    private EPropNature(final long val) {
        this.val = Long.valueOf(val);
    }

    @Override
    public Long getValue() {
        return val;
    }

    @Override
    public String getName() {
        return this.name();
    }

    public static EPropNature getForValue(final long val) {
        if (val == 9) {
            return EPropNature.DYNAMIC;
        }
        for (EPropNature e : EPropNature.values()) {
            if (e.val.longValue() == val) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EPropNature has no item with value: " + String.valueOf(val), val);
    }

    @Override
    public boolean isInDomain(final Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(final List<Id> ids) {
        return false;
    }
}
