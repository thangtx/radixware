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

public enum EAccessAreaMode implements IKernelIntEnum {

    //constant values for compiling
    UNBOUNDED(0L),
    BOUNDED_BY_PART(1L),
    PROHIBITED(2L),
    BOUNDED_BY_GROUP(3L),
    BOUNDED_BY_USER(4L),
    ;
    //constructors
    private final Long value;

    private EAccessAreaMode(final Long x) {
        value = x;
    }

    //public methods
    @Override
    public String getName() {
        return name();
    }

    @Override
    public Long getValue() {
        return value;
    }

    public static EAccessAreaMode getForValue(final Long val) {
        for (EAccessAreaMode e : EAccessAreaMode.values()) {
            if (e.value.equals(val)) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EAccessAreaMode has no item with value: " + String.valueOf(val),val);
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
