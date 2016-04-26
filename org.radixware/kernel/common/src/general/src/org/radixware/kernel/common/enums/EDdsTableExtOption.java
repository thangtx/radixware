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

public enum EDdsTableExtOption implements IKernelIntEnum {

    SUPPORT_USER_PROPERTIES(32),
    USE_AS_USER_PROPERTIES_OBJECT(4),
    ENABLE_APPLICATION_CLASSES(65536),
    CHECK_USER_CONSTRAINTS_ON_DELETION(128);
    private final long value;

    private EDdsTableExtOption(long value) {
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

    public static EDdsTableExtOption getForValue(final long value) {
        for (EDdsTableExtOption e : EDdsTableExtOption.values()) {
            if (e.value == value) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError(EDdsTableExtOption.class.getSimpleName() + " has no item with value: " + String.valueOf(value), value);
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }

    public String getAsStr() {
        switch (this) {
            case SUPPORT_USER_PROPERTIES:
                return "Support User Properties";
            case USE_AS_USER_PROPERTIES_OBJECT:
                return "Use As User Properties Object";
            case ENABLE_APPLICATION_CLASSES:
                return "Enable Application Classes";
            case CHECK_USER_CONSTRAINTS_ON_DELETION:
                return "Check User Constraints On Deletion";
            default:
                return "not defined";
        }
    }
}