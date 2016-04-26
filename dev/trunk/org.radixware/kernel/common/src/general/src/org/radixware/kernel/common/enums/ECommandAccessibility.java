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
import org.radixware.kernel.common.utils.RadixResourceBundle;


public enum ECommandAccessibility implements IKernelIntEnum {

    ALWAYS(0, RadixResourceBundle.getMessage(ECommandAccessibility.class, "CommandAccessibility-Always")),
    ONLY_FOR_NEW(1, RadixResourceBundle.getMessage(ECommandAccessibility.class, "CommandAccessibility-New")),
    ONLY_FOR_EXISTENT(2, RadixResourceBundle.getMessage(ECommandAccessibility.class, "CommandAccessibility-Existent")),
    ONLY_FOR_FIXED(3, RadixResourceBundle.getMessage(ECommandAccessibility.class, "CommandAccessibility-Fixed"));
    private Long val;
    private String title;

    private ECommandAccessibility(long val, String title) {
        this.val = Long.valueOf(val);
        this.title = title;
    }

    @Override
    public Long getValue() {
        return val;
    }

    @Override
    public String getName() {
        return title;
    }

    public static ECommandAccessibility getForTitle(String title) {
        for (ECommandAccessibility e : ECommandAccessibility.values()) {
            if (e.getName().equals(title)) {
                return e;
            }
        }
        return null;
    }

    public static ECommandAccessibility getForValue(final long val) {
        for (ECommandAccessibility e : ECommandAccessibility.values()) {
            if (e.val.longValue() == val) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("ECommandAccessebility has no item with value: " + String.valueOf(val),val);
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
