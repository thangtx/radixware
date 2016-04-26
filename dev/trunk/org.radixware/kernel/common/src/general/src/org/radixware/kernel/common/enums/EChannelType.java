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


public enum EChannelType implements IKernelStrEnum {
    
    TCP("TCP"),
    INTERNAL_PIPE("INTERNAL_PIPE"),
    JMS("JMS");
    
    private final String value;

    private EChannelType(final String value) {
        this.value = value;
    }
    
    @Override
    public String getName() {
        return name();
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
    
    public static EChannelType getForValue(final String value) {
        for(EChannelType type : values()) {
            if(type.value.equals(value)) {
                return type;
            }
        }
        throw new NoConstItemWithSuchValueError("EChannelType has no item with value ", value);
    }
    
}
