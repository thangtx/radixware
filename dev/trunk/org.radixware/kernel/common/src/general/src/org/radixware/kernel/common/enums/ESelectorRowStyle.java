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


public enum ESelectorRowStyle implements IKernelStrEnum {

    BAD("Bad"),
    FAVORITE("Favorite"),
    GOOD("Good"),
    IMPORTANT("Important"),
    NORMAL("Normal"),
    POOR("Poor"),
    RATHER_GOOD("RatherGood"),
    UNIMPORTANT("Unimportant"),
    VERY_BAD("VeryBad"),
    VERY_GOOD("VeryGood");
    private final String value;

    private ESelectorRowStyle(String style) {
        this.value = style;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name();
    }

    public static ESelectorRowStyle getForValue(final String val) {
        for (ESelectorRowStyle t : ESelectorRowStyle.values()) {
            if (t.getValue() == null && val == null || t.getValue().equals(val)) {
                return t;
            }
        }
        throw new NoConstItemWithSuchValueError("ESelectorRowStyle has no item with value: " + String.valueOf(val),val);
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
