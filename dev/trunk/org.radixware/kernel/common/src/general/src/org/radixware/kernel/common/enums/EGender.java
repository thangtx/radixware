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


public enum EGender implements IKernelCharEnum {

    MASCULINE("Masculine", new Character('M')),
    FEMININE("Feminine", new Character('F')),
    NEUTER("Neuter", new Character('N'));
    //
    private final String name;
    private final Character value;

    private EGender(final String name, final Character value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Character getValue() {
        return value;
    }

    public static EGender getForValue(final char val) {
        for (EGender e : EGender.values()) {
            if (e.value.charValue() == val) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EGender has no item with value: " + String.valueOf(val),val);
    }

    public static EGender getForValue(final Character val) {
        if (val == null) {
            throw new NoConstItemWithSuchValueError("EGender has no item with null value",val);
        }
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
