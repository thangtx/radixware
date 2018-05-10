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
 *
 * @author dlastochkin
 */
public enum EDocGroup implements IKernelStrEnum{      
    MODULE("Module"),
    CLASS("Class"),
    ENUM("Enumeration"),
    ENUM_ITEM("Enumeration Item"),
    XSD_ITEM("Xsd Item"),
    XSD("Xsd"),
    PROPERTY("Property"),
    METHOD("Method"),
    METHOD_PARAMETER("Method Parameter"),
    TABLE("Table"),
    TABLE_PROPERTY("Table Property"),
    PACKAGE("Package"),
    PACKAGE_FUNC("Package Function"),
    PACKAGE_FUNC_PARAMETER("Package Function Parameter"),
    VIEW("View"),
    TRIGGER("Trigger"),
    NONE("");
    private String val;
    
    private EDocGroup(final String val) {
        this.val = val;
    }
    
    public static EDocGroup getForValue(final String val) {
        for (EDocGroup e : EDocGroup.values()) {
            if (e.getValue().equals(val)) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EDialogButtonType has no item with value: " + String.valueOf(val), val);
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getValue() {
        return val;
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
