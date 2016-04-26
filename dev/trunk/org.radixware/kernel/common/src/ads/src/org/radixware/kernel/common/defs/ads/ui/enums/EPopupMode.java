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


public enum EPopupMode implements UIEnum {

    DelayedPopup("DelayedPopup"),
    MenuButtonPopup("MenuButtonPopup"),
    InstantPopup("InstantPopup");
    private final static String PACKAGE = "com.trolltech.qt.gui.QToolButton.ToolButtonPopupMode";
    private final String value;

    private EPopupMode(final String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getQualifiedEnum() {
        return PACKAGE;
    }

    @Override
    public String getQualifiedValue() {
        return PACKAGE + "." + getValue();
    }

    @Override
    public String getName() {
        return value;
    }

    public static EPopupMode getForValue(final String value) {
        String v = value.startsWith(PACKAGE + ".") ? value.substring(PACKAGE.length() + 1) : value;
        for (EPopupMode val : EPopupMode.values()) {
            if (val.getValue().equals(v)) {
                return val;
            }
        }
        throw new NoConstItemWithSuchValueError("EPopupMode has no item with value: " + String.valueOf(value),value);
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
