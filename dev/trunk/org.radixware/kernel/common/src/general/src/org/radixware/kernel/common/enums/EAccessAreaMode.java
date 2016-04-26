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
    BOUNDED(new Long(1)),
    PROHIBITED(new Long(2)),
    UNBOUNDED(new Long(0));
    //constructors
    private final Long value;

    private EAccessAreaMode(Long x) {
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
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }
}
