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

public enum EAccessAreaType implements IKernelIntEnum {

    /**
     * Access areas are inherited from defined parent. Access area list can be extended by own areas.
     */
    INHERITED(2),
    /**
     * Class does not support access partioning:
     * access is switch on/off for all entities of this class altogether.
     * Access areas is not defined.
     */
    NONE(0),
    /**
     * Access area options of overwritten class are used. Only for overwriting classes.
     */
    NOT_OVERRIDDEN(-1),
    /**
     * Own access areas are defined.
     */
    OWN(1);
    private Long val;

    private EAccessAreaType(long val) {
        this.val = Long.valueOf(val);
    }

    @Override
    public Long getValue() {
        return val;
    }

    @Override
    public String getName() {
        return this.name();
    }

    public static EAccessAreaType getForValue(final Long val) {
        for (EAccessAreaType e : EAccessAreaType.values()) {
            if (e.val.equals(val)) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EAccessAreaType has no item with value: " + String.valueOf(val),val);
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
