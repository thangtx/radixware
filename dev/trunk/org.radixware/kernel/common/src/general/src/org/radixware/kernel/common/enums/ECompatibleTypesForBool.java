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


public enum ECompatibleTypesForBool implements IKernelIntEnum {

    DEFAULT(0, EEditMaskType.BOOL),
    REALNUM(1, EEditMaskType.NUM),
    INT(2, EEditMaskType.INT),
    STR(3, EEditMaskType.STR);
    
    private final Long index;
    private final EEditMaskType type;

    private ECompatibleTypesForBool(long index, EEditMaskType maskType) {
        this.index = Long.valueOf(index);
        this.type = maskType;
    }

    @Override
    public String getName() {
        return this.type.toString();
    }

    @Override
    public Long getValue() {
        return index;
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    public static ECompatibleTypesForBool getForValue(final long val) {
        for (ECompatibleTypesForBool e : ECompatibleTypesForBool.values()) {
            if (e.getValue().equals(val)) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("ECompatibleTypesForBool has no item with value: " + String.valueOf(val), val);
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }
}
