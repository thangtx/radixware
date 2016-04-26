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


public enum ESelectorColumnVisibility implements IKernelIntEnum {

    NEVER(0, RadixResourceBundle.getMessage(ESelectorColumnVisibility.class, "SelectorColumnVisibility-Never")),
    INITIAL(1, RadixResourceBundle.getMessage(ESelectorColumnVisibility.class, "SelectorColumnVisibility-Initial")),
    OPTIONAL(2, RadixResourceBundle.getMessage(ESelectorColumnVisibility.class, "SelectorColumnVisibility-Optional"));
    private Long value;
    private String title;

    private ESelectorColumnVisibility(long val, String title) {
        this.value = Long.valueOf(val);
        this.title = title;
    }

    @Override
    public Long getValue() {
        return value;
    }

    @Override
    public String getName() {
        return title;
    }

    public static ESelectorColumnVisibility getForTitle(String title){
        for (ESelectorColumnVisibility v : ESelectorColumnVisibility.values()){
            if (v.getName().equals(title)){
                return v;
            }
        }
        return null;
    }

    public static ESelectorColumnVisibility getForValue(final Long val) {
        for (ESelectorColumnVisibility t : ESelectorColumnVisibility.values()) {
            if (t.getValue() == null && val == null || t.getValue().equals(val)) {
                return t;
            }
        }
        throw new NoConstItemWithSuchValueError("ESelectorColumnVisibility has no item with value: " + String.valueOf(val),val);
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
