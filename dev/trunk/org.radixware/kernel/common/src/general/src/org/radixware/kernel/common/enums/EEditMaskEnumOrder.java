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
import org.radixware.kernel.common.types.IKernelStrEnum;
import org.radixware.kernel.common.types.Id;


public enum EEditMaskEnumOrder implements IKernelStrEnum {

    BY_TITLE("Title"), //Name -> Title by vmokhov(yremizov)
    BY_ORDER("Order"),
    BY_VALUE("Value");
    private final String val;

    private EEditMaskEnumOrder(String val) {
        this.val = val;
    }

    @Override
    public String getName() {
        return "By " + val;
    }

    @Override
    public String getValue() {
        return val;
    }

    public String toString() {
        return val;
    }

    public static EEditMaskEnumOrder getForValue(final String val) {
        for (EEditMaskEnumOrder e : EEditMaskEnumOrder.values()) {
            if (e.val.equals(val)) {
                return e;
            }
        }
        if (val.equals("Name"))//by yremizov
            return EEditMaskEnumOrder.BY_TITLE;
        throw new NoConstItemWithSuchValueError("EEditMaskEnumOrder has no item with value: " + String.valueOf(val),val);
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
