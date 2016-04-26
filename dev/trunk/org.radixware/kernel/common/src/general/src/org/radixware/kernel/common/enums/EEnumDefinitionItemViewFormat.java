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
import java.util.Objects;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.IKernelIntEnum;
import org.radixware.kernel.common.types.Id;


public enum EEnumDefinitionItemViewFormat implements IKernelIntEnum {

    DEFAULT(0, "Default"),
    DECIMAL(1, "Decimal"), 
    HEXADECIMAL(2, "Hexadecimal");
        
    private final Long value;
    private final String name;
    
    private EEnumDefinitionItemViewFormat(long value, String name) {
        this.value = value;
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public Long getValue() {
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
    
    public static EEnumDefinitionItemViewFormat getForName(final String name) {
        for (final EEnumDefinitionItemViewFormat i : values()) {
            if (Objects.equals(name, i.getName())) {
                return i;
            }
        }
        throw new NoConstItemWithSuchValueError("EEnumDefinitionItemViewFormat has no item with name: " + String.valueOf(name), name);
    }
    
    public static EEnumDefinitionItemViewFormat getForValue(final Long val) {
        for (final EEnumDefinitionItemViewFormat i : values()) {
            if (Objects.equals(val, i.getValue())) {
                return i;
            }
        }
        throw new NoConstItemWithSuchValueError("EEnumDefinitionItemViewFormat has no item with value: " + String.valueOf(val), val);
    }
}
