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
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.Id;


public enum EStandardButton implements UIEnum {

    NoButton("NO_BUTTON", EDialogButtonType.NO_BUTTON),
    Ok("OK", EDialogButtonType.OK),
    Save("SAVE", EDialogButtonType.SAVE),
    SaveAll("SAVE_ALL", EDialogButtonType.SAVE_ALL),
    Open("OPEN", EDialogButtonType.OPEN),
    Yes("YES", EDialogButtonType.YES),
    YesToAll("YES_TO_ALL", EDialogButtonType.YES_TO_ALL),
    No("NO", EDialogButtonType.NO),
    NoToAll("NO_TO_ALL", EDialogButtonType.NO_TO_ALL),
    Abort("ABORT", EDialogButtonType.ABORT),
    Retry("RETRY", EDialogButtonType.RETRY),
    Ignore("IGNORE", EDialogButtonType.IGNORE),
    Close("CLOSE", EDialogButtonType.CLOSE),
    Cancel("CANCEL", EDialogButtonType.CANCEL),
    Discard("DISCARD", EDialogButtonType.DISCARD),
    Help("HELP", EDialogButtonType.HELP),
    Apply("APPLY", EDialogButtonType.APPLY),
    Reset("RESET", EDialogButtonType.RESET),
    RestoreDefaults("RESTORE_DEFAULTS", EDialogButtonType.RESTORE_DEFAULTS);
    
    private final static String STANDARD_BUTTON = "com.trolltech.qt.gui.QDialogButtonBox.StandardButton";
    private final static String WEB_PACKAGE = "org.radixware.kernel.common.enums.EDialogButtonType";
    
    private final static String EDIALOG_BUTTON_TYPE = "org.radixware.kernel.common.enums.EDialogButtonType";
    private final String webName;
    private final EDialogButtonType type;

    private EStandardButton(String webName, EDialogButtonType type) {
        this.webName = webName;
        this.type = type;
    }

    @Override
    public String getValue() {
        return toString();
    }

    public EDialogButtonType getType() {
        return type;
    }

    @Override
    public String getQualifiedValue() {
        return EDIALOG_BUTTON_TYPE + "." + getType().toString();
    }

    public String getQualifiedWebValue() {
        return WEB_PACKAGE + "." + webName;
    }

    @Override
    public String getQualifiedEnum() {
        return EDIALOG_BUTTON_TYPE;
    }

    @Override
    public String getName() {
        return toString();
    }

    public Id getIconId() {
        return Id.Factory.loadFrom(EDefinitionIdPrefix.IMAGE.getValue() + getValue());
    }

    public static EStandardButton getForValue(final String value) {
        String v = value;
        if (value.startsWith(STANDARD_BUTTON + ".")) {
            v = value.replaceAll(STANDARD_BUTTON, EDIALOG_BUTTON_TYPE);
        }
        v = v.startsWith(EDIALOG_BUTTON_TYPE + ".") ? v.substring(EDIALOG_BUTTON_TYPE.length() + 1) : v;
        for (EStandardButton val : EStandardButton.values()) {
            if (val.getValue().equals(v) || val.getType().toString().equals(v)) {
                return val;
            }
        }
        throw new NoConstItemWithSuchValueError("EStandardButton has no item with value: " + String.valueOf(value), value);
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
