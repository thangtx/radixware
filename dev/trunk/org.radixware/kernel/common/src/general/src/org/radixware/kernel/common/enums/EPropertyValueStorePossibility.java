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

public enum EPropertyValueStorePossibility implements IKernelIntEnum {

    NONE(0, "None"),
    FILE_SAVE(0x1, "Save only"),
    FILE_LOAD(0x2, "Load only"),
    FILE_SAVE_AND_LOAD(0x1 | 0x2, "Save and load");
    private final Long value;
    private final String name;
    //constructors

    private EPropertyValueStorePossibility(long x, String name) {
        value = x;
        this.name = name;
    }

    @Override
    public Long getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name;
    }

    public static EPropertyValueStorePossibility getForValue(final Long val) {
        for (EPropertyValueStorePossibility e : EPropertyValueStorePossibility.values()) {
            if (val == null) {
                break;
            }
            if (e.value.longValue() == val.longValue()) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EAccess has no item with value: " + String.valueOf(val), val);
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
