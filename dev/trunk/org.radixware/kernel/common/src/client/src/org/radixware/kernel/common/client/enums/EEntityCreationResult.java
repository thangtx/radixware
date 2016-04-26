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

package org.radixware.kernel.common.client.enums;

import java.util.List;
import java.util.Objects;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.IKernelIntEnum;
import org.radixware.kernel.common.types.Id;


public enum EEntityCreationResult implements IKernelIntEnum{
    
    SUCCESS(0,"Success"),
    CANCELED_BY_CLIENT(1,"CanceledByClient"),
    CANCELED_BY_SERVER(2,"CanceledByServer");
        
    private final int value;
    private final String name;
    
    private EEntityCreationResult(final int value, final String name){
        this.value = value;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Long getValue() {
        return Long.valueOf(value);
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }
    
    public static EEntityCreationResult getForValue(final Long value){
        for (EEntityCreationResult e : EEntityCreationResult.values()) {
            if (Objects.equals(e.value, value)) {
                return e;
            }

        }
        throw new NoConstItemWithSuchValueError("EEntityCreationResult has no item with value: " + String.valueOf(value),value);        
    }
}
