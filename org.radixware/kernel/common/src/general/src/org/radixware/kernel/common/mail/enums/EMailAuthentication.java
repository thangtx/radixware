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

package org.radixware.kernel.common.mail.enums;

import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;


public enum EMailAuthentication {
    NONE("None", "None"),
    LOGIN("Login", "Login");

    final String value;
    final String descr;

    private EMailAuthentication(String value, String descr) {
        this.value = value;
        this.descr = descr;
    }

    public String getValue() {
        return value;
    }

    public String getDescr() {
        return descr;
    }

    @Override
    public String toString() {
        return descr;
    }

    public static EMailAuthentication getForValue(final String val) {
        for (EMailAuthentication e : EMailAuthentication.values()) {
            if (e.getValue().equals(val)) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError(EMailAuthentication.class.getSimpleName() + " has no item with value: " + String.valueOf(val),val);
    }

};
