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

/**
 * Inherited attributes group of presentation properties.
 */
public enum EPropAttrInheritance implements IKernelIntEnum {

    TITLE(1, "Title"),
    EDITING(2, "Edit Options"),
    PARENT_TITLE_FORMAT(4, "Parent Title Format"),
    PARENT_SELECT_CONDITION(8, "Parent Select Condition"),
    PARENT_SELECTOR(16, "Parent Selector"),
    HINT(32, "Hint");
    private final long value;
    private final String name;

    private EPropAttrInheritance(long value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public Long getValue() {
        return value;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public static EPropAttrInheritance getForValue(final long val) {
        for (EPropAttrInheritance e : EPropAttrInheritance.values()) {
            if (e.value == val) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError(EPropAttrInheritance.class.getSimpleName() + " has no item with value: " + String.valueOf(val),val);
    }

    public static final long toBitField(EnumSet<EPropAttrInheritance> enumSet) {
        long result = 0;
        for (EPropAttrInheritance e : enumSet) {
            result |= e.value;
        }
        return result;
    }

    public static final EnumSet<EPropAttrInheritance> fromBitField(long field) {
        EnumSet<EPropAttrInheritance> enumSet = EnumSet.noneOf(EPropAttrInheritance.class);

        for (EPropAttrInheritance e : values()) {
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
