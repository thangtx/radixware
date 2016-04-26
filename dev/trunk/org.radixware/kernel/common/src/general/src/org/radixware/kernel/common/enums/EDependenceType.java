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


@Deprecated
public enum EDependenceType implements IKernelIntEnum {

    CASCADE_LOOKUP(6),
    DIRECTLY_DERIVED(1),
    INDIRECTLY_DERIVED(2),
    LOOKUP(5),
    REFER(4),
    RESTRICT_LOOKUP(7),
    USES(0);
    private long val;

    private EDependenceType(long val) {
        this.val = val;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public Long getValue() {
        return val;
    }

    public static EDependenceType getForValue(final Long val) {
        for (EDependenceType e : EDependenceType.values()) {
            if (e.getValue().equals(val)) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EDependenceType has no item with value: " + String.valueOf(val),val);
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
