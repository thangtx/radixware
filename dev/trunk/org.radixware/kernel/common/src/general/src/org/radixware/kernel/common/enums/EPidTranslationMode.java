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
import org.radixware.kernel.common.types.IKernelStrEnum;
import org.radixware.kernel.common.types.Id;

/**
 * SQML translator can replace PID by PidStr, Coma-separeted primary key properties in parenthesis,
 * Coma-separeted secondary key properties in parenthesis
 */
public enum EPidTranslationMode implements IKernelStrEnum {

    /**
     * Replace PID by PidStr
     */
    AS_STR("AsStr"),
    /**
     * Replace PID by coma-separeted primary key properties in parenthesis
     */
    PRIMARY_KEY_PROPS("PrimaryKeyProps"),
    /**
     * Replace PID by coma-separeted secondary key properties in parenthesis
     */
    SECONDARY_KEY_PROPS("SecondaryKeyProps");
    private final String value;

    private EPidTranslationMode(String x) {
        value = x;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getValue() {
        return value;
    }

    public static EPidTranslationMode getForValue(final String val) {
        for (EPidTranslationMode t : EPidTranslationMode.values()) {
            if (t.value.equals(val)) {
                return t;
            }
        }
        throw new NoConstItemWithSuchValueError("EPidTranslationMode has no item with value: " + String.valueOf(val),val);
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
