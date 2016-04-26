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

package org.radixware.kernel.common.msdl.enums;


public enum EStrCharSet {

    None("None", "<Not defined>"),
    Any("Any", "Any"),
    XML("XML","XML"),
    User("User","User");

    private String value;
    private String title;

    EStrCharSet(String value, String title) {
       this.value = value;
       this.title = title;
    }

    public String getName() {
        return value;
    }

    public String getValue() {
        return value;
    }

    public static EStrCharSet getInstance(String value) {
        if (value == null) {
            return None;
        }
        for (EStrCharSet field : EStrCharSet.values()) {
            if (field.value.equals(value)) {
                return field;
            }
        }
        return None;
    }

    @Override
    public String toString() {
        return title;
    }
}