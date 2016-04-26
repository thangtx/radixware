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

/**
 * Expired jobs' execution policy
 *  <ul>
 * <li>{@linkplain #NONE do not execute expired jobs}</li>
 * <li>{@linkplain #SINGLE execute only one expired job}</li>
 * <li>{@linkplain #ALL execute all expired jobs}</li>
 * </ul> 
 */
public enum EExpiredJobExecPolicy implements IKernelIntEnum { //RADIX-3442

    /**
     * do not execute expired jobs (value - 0)
     */
    NONE("None", 0),
    /**
     * execute only one expired job (value - 1)
     */
    SINGLE("Single", 1),
    /**
     * execute all expired jobs (value - 2)
     */
    ALL("All", 2);
    private final String name;
    private final long value;

    private EExpiredJobExecPolicy(final String name, final long value) {
        this.name = name;
        this.value = value;
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
    public boolean isInDomain(final Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(final List<Id> ids) {
        return false;
    }

    public static EExpiredJobExecPolicy getForValue(final long val) {
        for (EExpiredJobExecPolicy e : EExpiredJobExecPolicy.values()) {
            if (e.value == val) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError(EExpiredJobExecPolicy.class.getSimpleName() + " has no item with value: " + String.valueOf(val),val);
    }
}
