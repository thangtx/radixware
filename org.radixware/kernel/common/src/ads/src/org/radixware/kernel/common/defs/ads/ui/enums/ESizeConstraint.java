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


public enum ESizeConstraint implements UIEnum {

    SetDefaultConstraint("SetDefaultConstraint"),
    SetNoConstraint("SetNoConstraint"),
    SetMinimumSize("SetMinimumSize"),
    SetFixedSize("SetFixedSize"),
    SetMaximumSize("SetFixedSize"),
    SetMinAndMaxSize("SetFixedSize");

    private final static String PACKAGE = "com.trolltech.qt.gui.QLayout.SizeConstraint";
    private final String value;

    private ESizeConstraint(String value) {
        this.value = value;
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
        return getValue();
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

    public static ESizeConstraint getForValue(final String value) {
        final String fullName = value.startsWith(PACKAGE + ".") ? value.substring(PACKAGE.length() + 1) : value;
        for (final ESizeConstraint val : values()) {
            if (val.getValue().equals(fullName)) {
                return val;
            }
        }
        throw new NoConstItemWithSuchValueError("ESizeConstraint has no item with value: " + String.valueOf(value), value);
    }
}
