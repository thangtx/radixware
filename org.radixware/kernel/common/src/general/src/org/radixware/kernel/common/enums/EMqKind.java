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


public enum EMqKind implements IKernelStrEnum {

    LOOPBACK("LOOPBACK"),
    JMS("JMS"),
    KAFKA("KAFKA"),
    AMQP("AMQP");
    private final String val;

    private EMqKind(String val) {
        this.val = val;
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

    public static EMqKind getForValue(final String str) {
        for (EMqKind type : EMqKind.values()) {
            if (type.getValue().equals(str)) {
                return type;
            }
        }
        throw new NoConstItemWithSuchValueError("EMqKind has no item with value " + str, str);
    }

    @Override
    public String getName() {
        return null;
    }
}