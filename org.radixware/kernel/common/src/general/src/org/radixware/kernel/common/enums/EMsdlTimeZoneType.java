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

import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;

/**
 *
 * @author npopov
 */
public enum EMsdlTimeZoneType {

    LOCAL("Local"),
    UTC("Utc"),
    SPECIFIED("Specified");

    private final String value;

    private EMsdlTimeZoneType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static EMsdlTimeZoneType getForValue(String val) {
        for (EMsdlTimeZoneType t : values()) {
            if (t.getValue().equals(val)) {
                return t;
            }
        }
        throw new NoConstItemWithSuchValueError(EMsdlTimeZoneType.class.getSimpleName() + " has no item with value: " + String.valueOf(val), val);
    }
}
