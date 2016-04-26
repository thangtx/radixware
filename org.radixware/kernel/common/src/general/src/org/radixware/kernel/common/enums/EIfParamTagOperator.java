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
 * IfParamTag operator.
 */
public enum EIfParamTagOperator implements IKernelStrEnum {

    /**
     * Parameter is equal to specified value (for simple parameters).
     * Parameter contains specified value (for arrays).
     * Value can be null.
     */
    EQUAL("Equal"),
    /**
     * Parameter is not equal to specified value (for simple parameters).
     * Parameter not contains specified value (for arrays).
     * Value can be null.
     */
    NOT_EQUAL("NotEqual"),
    /**
     * Parameter value is null.
     */
    NULL("Null"),
    /**
     * Parameter value is not null.
     */
    NOT_NULL("NotNull"),
    /**
     * Parameter value is empty list.
     */
    EMPTY("Empty"),
    /**
     * Parameter value is not null and not empty list (selected list of values).
     */
    NOT_EMPTY("NotEmpty");
    private final String val;

    private EIfParamTagOperator(String val) {
        this.val = val;
    }

    @Override
    public String getValue() {
        return val;
    }

    @Override
    public String getName() {
        return val;
    }

    public static EIfParamTagOperator getForValue(final String val) {
        for (EIfParamTagOperator order : EIfParamTagOperator.values()) {
            if (order.getValue().equals(val)) {
                return order;
            }
        }
        throw new NoConstItemWithSuchValueError("EIfParamTagOperator has no item with value: " + String.valueOf(val),val);
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
