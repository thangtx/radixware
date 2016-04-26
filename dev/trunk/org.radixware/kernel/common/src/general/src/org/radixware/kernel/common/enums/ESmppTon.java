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

public enum ESmppTon implements IKernelIntEnum {

    //constant values for compiling
    ABBREVIATED(new Long(6),"6-ABBREVIATED"),
    ALPHANUMERIC(new Long(5),"5-ALPHANUMERIC"),
    INTERNATIONAL(new Long(1),"1-INTERNATIONAL"),
    NATIONAL(new Long(2),"2-NATIONAL"),
    NETWORK_SPECIFIC(new Long(3),"3-NETWORK_SPECIFIC"),
    SUBSCRIBER_NUMBER(new Long(4),"4-SUBSCRIBER_NUMBER"),
    UNKNOWN(new Long(0),"0-UNKNOWN");
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

    private ESmppTon(Long x, String title) {
        value = x;
        this.title = title;
    }

    public static ESmppTon getForValue(final Long val) {
        for (ESmppTon t : ESmppTon.values()) {
            if (t.getValue() == null && val == null || t.getValue().equals(val)) {
                return t;
            }
        }
        throw new NoConstItemWithSuchValueError("ESmppTon has no item with value: " + String.valueOf(val),val);
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