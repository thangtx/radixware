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

public enum EProfileMode implements IKernelIntEnum {

    OFF(Long.valueOf(0), "Off"),
    SPECIFIED(Long.valueOf(1), "Specified"),
    ALL(Long.valueOf(2), "All");
    private final Long value;
    private final String name;
    //constructors   

    private EProfileMode(final Long x, final String name) {
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
    public static EProfileMode getForValue(final long val) {
        for (EProfileMode e : EProfileMode.values()) {
            if (e.value.longValue() == val) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EProfileMode has no item with value: " + String.valueOf(val),val);
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
