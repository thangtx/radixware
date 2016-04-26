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

package org.radixware.kernel.common.defs.ads.ui.enums;

import java.util.List;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.Id;


public enum EScrollBarPolicy implements UIEnum {

    ScrollBarAsNeeded,
    ScrollBarAlwaysOn,
    ScrollBarAlwaysOff;
    private final static String PACKAGE = "com.trolltech.qt.core.Qt.ScrollBarPolicy";

    @Override
    public String getValue() {
        return toString();
    }

    @Override
    public String getQualifiedValue() {
        return PACKAGE + "." + getValue();
    }

    @Override
    public String getQualifiedEnum() {
        return PACKAGE;
    }

    @Override
    public String getName() {
        return toString();
    }

    public static EScrollBarPolicy getForValue(final String value) {
        String v = value.startsWith(PACKAGE + ".") ? value.substring(PACKAGE.length() + 1) : value;
        for (EScrollBarPolicy val : EScrollBarPolicy.values()) {
            if (val.getValue().equals(v)) {
                return val;
            }
        }
        throw new NoConstItemWithSuchValueError("EScrollBarPolicy has no item with value: " + String.valueOf(value),value);
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
