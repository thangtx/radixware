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


public enum ESoapMessageType implements IKernelStrEnum {

    REQUEST("REQUEST"),
    RESPONSE("RESPONSE"),
    FAULT("FAULT"),
    CALLBACK_RESPONCE("CALLBACK_RESPONCE");
    private final String value;

    private ESoapMessageType(String value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getValue() {
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

    public static ESoapMessageType getForValue(final String str) {
        for (ESoapMessageType type : ESoapMessageType.values()) {
            if (type.getValue().equals(str)) {
                return type;
            }
        }
        throw new NoConstItemWithSuchValueError("ESoapMessageType has no item with value " + str, str);
    }
}
