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
import org.radixware.kernel.common.types.IKernelCharEnum;
import org.radixware.kernel.common.types.Id;


public enum EServiceAccessibility implements IKernelCharEnum {

    //constant values for compiling
    INTRA_SYSTEM(new Character('I')),
    INTER_SYSTEM(new Character('E')),
    BOTH(new Character('B'));

    private final Character value;
    //constructors   

    private EServiceAccessibility(final Character x) {
        value = x;
    }

    @Override
    public Character getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name();
    }
    //public methods

    public static EServiceAccessibility getForValue(final char val) {
        for (EServiceAccessibility e : EServiceAccessibility.values()) {
            if (e.value.charValue() == val) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EServiceAccessability has no item with value: " + String.valueOf(val),val);
    }

    public static EServiceAccessibility getForValue(final Character val) {
        if (val == null)
            throw new NoConstItemWithSuchValueError("EServiceAccessability has no item with null value",val);
        return getForValue(val.charValue());
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