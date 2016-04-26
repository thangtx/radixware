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


public enum ESelectorColumnSizePolicy implements IKernelIntEnum{
    
    AUTO(0,RadixResourceBundle.getMessage(ESelectorColumnSizePolicy.class, "SelectorColumnSizePolicy-Auto")),
    MANUAL_RESIZE(1,RadixResourceBundle.getMessage(ESelectorColumnSizePolicy.class, "SelectorColumnSizePolicy-Manual")),
    RESIZE_BY_CONTENT(2,RadixResourceBundle.getMessage(ESelectorColumnSizePolicy.class, "SelectorColumnSizePolicy-By-Content")),
    STRETCH(3,RadixResourceBundle.getMessage(ESelectorColumnSizePolicy.class, "SelectorColumnSizePolicy-Stretch"));
    
    private final Long value;
    private final String title;
    
    private ESelectorColumnSizePolicy(long value, String title){
        this.value = value;
        this.title = title;
    }

    @Override
    public String getName() {
        return title;
    }

    @Override
    public Long getValue() {
        return value;
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }
    
    public static ESelectorColumnSizePolicy getForTitle(String title){
        for (ESelectorColumnSizePolicy v : ESelectorColumnSizePolicy.values()){
            if (v.getName().equals(title)){
                return v;
            }
        }
        return null;
    }

    public static ESelectorColumnSizePolicy getForValue(final Long val) {
        for (ESelectorColumnSizePolicy t : ESelectorColumnSizePolicy.values()) {
            if (t.getValue() == null && val == null || t.getValue().equals(val)) {
                return t;
            }
        }
        throw new NoConstItemWithSuchValueError("ESelectorColumnSizePolicy has no item with value: " + String.valueOf(val),val);
    }   
    
}
