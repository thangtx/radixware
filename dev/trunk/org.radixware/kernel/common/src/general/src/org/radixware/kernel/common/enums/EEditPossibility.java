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


public enum EEditPossibility implements IKernelIntEnum {

    ALWAYS(0, "Always"),
    NEVER(1, "Never"),
    ON_CREATE(2, "On create"),
    ONLY_EXISTING(4, "Only existing"),
    ONLY_IN_EDITOR(3, "Only in editor"),
    PROGRAMMATICALLY(5, "Programmatically");

    private final Long val;
    private final String name;

    private EEditPossibility(long val, String name) {
        this.val = Long.valueOf(val);
        this.name = name;
    }

    @Override
    public Long getValue() {
        return val;
    }

    @Override
    public String getName() {
        return name;
    }

    public static EEditPossibility getForValue(final long val) {
        for (EEditPossibility e : EEditPossibility.values()) {
            if (e.val.longValue() == val) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EEditPossibility has no item with value: " + String.valueOf(val),val);
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
    public String toString() {
        return getName();
    }
}
