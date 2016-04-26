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

public enum EMode implements IKernelIntEnum {

    //constant values for compiling
    DEV(3),
    PROD(1),
    TEST(2),
    ZERO_RELEASE(0);
    //constructors
    private Long value;

    private EMode(long x) {
        this.value = Long.valueOf(x);
    }

    //public methods
    public static EMode getForValue(Long val) {
        for (EMode order : EMode.values()) {
            if (order.getValue().equals(val)) {
                return order;
            }
        }
        throw new NoConstItemWithSuchValueError("EMode has no item with value: " + String.valueOf(val),val);
    }

    @Override
    public Long getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name();
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
