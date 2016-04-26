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
import java.util.MissingResourceException;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.IKernelIntEnum;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixResourceBundle;

public enum EParamDirection implements IKernelIntEnum {

    IN(0),
    OUT(1),
    BOTH(2);

    private final Long val;

    private EParamDirection(long val) {
        this.val = Long.valueOf(val);             
    }

    @Override
    public Long getValue() {
        return val;
    }
    
    @Override
    public String getName() {
        try {
            return RadixResourceBundle.getMessage(EParamDirection.class, "Param-Direction-" + name());
        } catch (MissingResourceException ex) {
            return name();
        }
    }

    @Override
    public String toString() {
        return getName();
    }

    public static EParamDirection getForValue(final long val) {
        for (EParamDirection e : EParamDirection.values()) {
            if (e.val.longValue() == val) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EParamDirection has no item with value: " + String.valueOf(val),val);
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
