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

public enum ESmppNpi implements IKernelIntEnum {

    //constant values for compiling
    DATA(new Long(3),"3-DATA"),
    ERMES(new Long(10),"10-ERMES"),
    ISDN(new Long(1),"1-ISDN"),
    INTERNET(new Long(14),"14-INTERNET"),
    LAND_MOBILE(new Long(6),"6-LAND_MOBILE"),
    NATIONAL(new Long(8),"8-NATIONAL"),
    PRIVATE(new Long(9),"9-PRIVATE"),
    TELEX(new Long(4),"4-TELEX"),
    UNKNOWN(new Long(0),"0-UNKNOWN"),
    WAPCLIENT_ID(new Long(18),"18-WAPCLIENT_ID");
    private final Long value;
    private final String title;

    @Override
    public Long getValue() {
        return value;
    }

    @Override
    public String getName() {
        return title;
    }
    //constructors   

    private ESmppNpi(Long x, String title) {
        value = x;
        this.title = title;
    }
    //public methods

    public static ESmppNpi getForValue(final Long val) {
        for (ESmppNpi t : ESmppNpi.values()) {
            if (t.getValue() == null && val == null || t.getValue().equals(val)) {
                return t;
            }
        }
        throw new NoConstItemWithSuchValueError("ESmppNpi has no item with value: " + String.valueOf(val),val);
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