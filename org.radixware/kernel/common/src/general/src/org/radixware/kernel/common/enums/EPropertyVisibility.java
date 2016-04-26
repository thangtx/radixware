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

public enum EPropertyVisibility implements IKernelIntEnum {

    ALWAYS(0, "Always"),
    NEVER(1, "Never"),
    ONLY_FOR_NEW(2, "Only for new"),
    ONLY_FOR_EXISTENT(3, "Only for existent");

    private Long val;
    private final String name;

    private EPropertyVisibility(long val, String name) {
        this.val = Long.valueOf(val);
        this.name = name;
    }

    @Override
    public Long getValue() {
        return val;
    }

    public static EPropertyVisibility getForValue(final long val) {
        for (EPropertyVisibility e : EPropertyVisibility.values()) {
            if (e.val.longValue() == val) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EPropertyVisibility has no item with value: " + String.valueOf(val),val);
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
