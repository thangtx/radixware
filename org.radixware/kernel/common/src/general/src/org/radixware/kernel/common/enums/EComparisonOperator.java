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


public enum EComparisonOperator implements IKernelStrEnum {

    LESS_THAN("<", "Less Than"),
    LESS_OR_EQUALS("<=", "Less or Equals"),
    EQUALS("==", "Equals"),
    NOT_EQUALS("!=", "Not Equals"),
    GREATER_THEN(">", "Greater Than"),
    GREATER_OR_EQUALS(">=", "Greater or Equals");
    private final String value;
    private final String name;

    private EComparisonOperator(final String value, final String name) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }
    
    public static EComparisonOperator getForValue(final String value)  {
        for (EComparisonOperator op : values()) {
            if (op.getValue().equals(value)) {
                return op;
            }
        }
        throw new NoConstItemWithSuchValueError("EComparisonOperator has no item with value", value);
    }
}
