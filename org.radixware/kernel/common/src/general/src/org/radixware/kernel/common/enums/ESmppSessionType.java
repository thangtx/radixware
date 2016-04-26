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

public enum ESmppSessionType implements IKernelIntEnum
{
    //constant values for compiling
    TRX(new Long(0)),
    RX(new Long(1)),
    TX(new Long(2));

    private final Long value;

    @Override
    public Long getValue()
    {
        return value;
    }

    @Override
    public String getName()
    {
        return name();
    }
    //constructors   

    private ESmppSessionType(Long x)
    {
        value = x;
    }
    //public methods

    public static ESmppSessionType getForValue(final Long val)
    {
        for (ESmppSessionType t : ESmppSessionType.values())
            if (t.getValue() == null && val == null || t.getValue().equals(val))
                return t;
        throw new NoConstItemWithSuchValueError("ESmppSessionType has no item with value: " + String.valueOf(
                val),val);
    }

    @Override
    public boolean isInDomain(Id id)
    {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids)
    {
        return false;
    }
}
