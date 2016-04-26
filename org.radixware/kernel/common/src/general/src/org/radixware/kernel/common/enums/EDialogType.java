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

public enum EDialogType implements IKernelStrEnum {

    CONFIRMATION("Confirmation", EDialogIconType.QUESTION),
    ERROR("Error", EDialogIconType.CRITICAL),
    INFORMATION("Information", EDialogIconType.INFORMATION),
    WARNING("Warning", EDialogIconType.WARNING);
    private final String val;
    private final EDialogIconType icon;

    private EDialogType(final String val, EDialogIconType icon) {
        this.val = val;
        this.icon = icon;
    }

    @Override
    public String getValue() {
        return val;
    }

    public EDialogIconType getIcon() {
        return icon;
    }

    @Override
    public String getName() {
        return null;
    }

    public static EDialogType getForValue(final String val) {
        for (EDialogType e : EDialogType.values()) {
            if (e.getValue().equals(val)) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EDialogType has no item with value: " + String.valueOf(val), val);
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
