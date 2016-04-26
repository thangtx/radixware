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


public enum EDateTimePrecision implements IKernelIntEnum {

    /** DATE for Oracle */
    NORMAL("Normal", 0),
    /* TIMESTAMP for Oracle*/
    HIGH("High", 1);
    //
    private final String name;
    private final int value;

    private EDateTimePrecision(String name, int value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    public static EDateTimePrecision getForValue(int val){
        for (EDateTimePrecision item : EDateTimePrecision.values()){
            if (item.getValue() == val){
                return item;
            }
        }
        throw new NoConstItemWithSuchValueError("EDateTimePrecision has no item with value: " + String.valueOf(val),val);
    }

    public static EDateTimePrecision getForName(String name){
        for (EDateTimePrecision item : EDateTimePrecision.values()){
            if (item.getName().equals(name)){
                return item;
            }
        }
        return null;
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
}
