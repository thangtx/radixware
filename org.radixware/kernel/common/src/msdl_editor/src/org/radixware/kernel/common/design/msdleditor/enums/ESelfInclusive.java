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


public enum ESelfInclusive {
    NOTDEFINED("<Not defined>"),
    INCLUSIVE("Inclusive"),
    NOTINCLUSIVE("Not Inclusive");

    private String value;

    ESelfInclusive(String value) {
       this.value = value;
    }

    public String getName() {
        return value;
    }

    public Boolean getValue() {
        if (this == NOTDEFINED)
            return null;
        if (this == INCLUSIVE)
            return true;
        else
            return false;
    }

    @Override
    public String toString() {
        return value;
    }
    public static ESelfInclusive getInstance(Boolean value) {
        if (value == null)
            return NOTDEFINED;
        if (value)
            return INCLUSIVE;
        else
            return NOTINCLUSIVE;
    }

}
