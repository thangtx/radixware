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

package org.radixware.kernel.common.design.msdleditor.enums;



public enum EShieldedFormat {
    NOTDEFINED("None","<Not defined>"),
    ASIS("AsIs","As is"),
    HEX("Hex","Hex");

    private String value;
    private String title;

    EShieldedFormat(String value, String title) {
       this.value = value;
       this.title = title;
    }

    public String getName() {
        return value;
    }

    public String getValue() {
        if (this == NOTDEFINED)
            return null;
        else
            return value;
    }

    @Override
    public String toString() {
        return title;
    }
    public static EShieldedFormat getInstance(String value) {
        if (value == null)
            return NOTDEFINED;
        for (EShieldedFormat field : EShieldedFormat.values())
            if (field.value.equals(value))
                return field;
        return null;
    }

}
