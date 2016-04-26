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

import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.IKernelIntEnum;
import org.radixware.kernel.common.types.Id;


public enum EExplorerItemAttrInheritance implements IKernelIntEnum {

    ICON(16),
    RESTRICTION(32),
    TITLE(128),
    CLASS_CATALOG(16384);
    private final long value;

    private EExplorerItemAttrInheritance(long value) {
        this.value = value;
    }

    @Override
    public Long getValue() {
        return value;
    }

    @Override
    public String getName() {
        return this.name();
    }

    public static EExplorerItemAttrInheritance getForValue(final long val) {
        for (EExplorerItemAttrInheritance e : EExplorerItemAttrInheritance.values()) {
            if (e.value == val) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError(EExplorerItemAttrInheritance.class.getSimpleName() + " has no item with value: " + String.valueOf(val), val);
    }

    public static final long toBitField(EnumSet<EExplorerItemAttrInheritance> enumSet) {
        long result = 0;
        for (EExplorerItemAttrInheritance e : enumSet) {
            result |= e.value;
        }
        return result;
    }

    public static final EnumSet<EExplorerItemAttrInheritance> fromBitField(long field) {
        EnumSet<EExplorerItemAttrInheritance> enumSet = EnumSet.noneOf(EExplorerItemAttrInheritance.class);

        for (EExplorerItemAttrInheritance e : values()) {
            if ((field & e.value) != 0L) {
                enumSet.add(e);
            }
        }

        return enumSet;
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
