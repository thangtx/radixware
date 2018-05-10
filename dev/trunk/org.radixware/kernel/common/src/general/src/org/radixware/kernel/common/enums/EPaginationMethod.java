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

/**
 * Sort direction
 */
public enum EPaginationMethod implements IKernelStrEnum {

    /**
     * From M to N
     */
    ABSOLUTE("Absolute", "Absolute"),
    /**
     * Relative to last record
     */
    RELATIVE("Relative", "Relative");
    private final String val;
    private final String name;

    private EPaginationMethod(String val, String name) {
        this.val = val;
        this.name = name;
    }

    @Override
    public String getValue() {
        return val;
    }

    @Override
    public String getName() {
        return name;
    }

    public static EPaginationMethod getForValue(final String val) {
        for (EPaginationMethod order : EPaginationMethod.values()) {
            if (order.getValue().equals(val)) {
                return order;
            }
        }
        throw new NoConstItemWithSuchValueError("EPaginationMethod has no item with value: " + String.valueOf(val),val);
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
