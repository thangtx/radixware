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


public enum EDateTimeFormat {
    NOTDEFINED("<Not defined>"),
    FLORA("Flora"),
    STR("Str"),
    JAVA("Java");

    private String value;

    EDateTimeFormat(String value) {
       this.value = value;
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
        return value;
    }
    public static EDateTimeFormat getInstance(String value) {
        if (value == null)
            return NOTDEFINED;
        for (EDateTimeFormat field : EDateTimeFormat.values())
            if (field.value.equals(value))
                return field;
        return null;
    }
}