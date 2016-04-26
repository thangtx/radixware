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


package org.radixware.wps.settings;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.wps.WpsEnvironment;

import org.radixware.wps.WpsSettings;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.rwt.TableLayout;
import org.radixware.wps.rwt.UIObject;


public class ColumnAlignmentSettings extends SettingsWidget {

    private final static EnumSet<EValType> valueTypes = EnumSet.of(
            EValType.BIN,
            EValType.BLOB, EValType.CHAR,
            EValType.CLOB, EValType.DATE_TIME,
            EValType.INT, EValType.NUM,
            EValType.STR, EValType.PARENT_REF);
    private final Map<EValType, ColumnAlignmentSettingsWidget> vtypesToSettings = new HashMap<>();

    public ColumnAlignmentSettings(final WpsEnvironment env, UIObject parent) {
        super(env, parent, SettingNames.SELECTOR_GROUP, null, "", null);
        setUpUi();
    }

    @Override
    public void readSettings(WpsSettings src) {
        for (ColumnAlignmentSettingsWidget s : vtypesToSettings.values()) {
            s.readSettings(src);
        }
    }

    @Override
    public void writeSettings(WpsSettings dst) {
        for (ColumnAlignmentSettingsWidget s : vtypesToSettings.values()) {
            s.writeSettings(dst);
        }
    }

    @Override
    public void restoreDefaults() {
        for (ColumnAlignmentSettingsWidget s : vtypesToSettings.values()) {
            s.restoreDefaults();
        }
    }

    private void setUpUi() {
        TableLayout table = new TableLayout();
        for (EValType t : valueTypes) {
            ColumnAlignmentSettingsWidget widget = new ColumnAlignmentSettingsWidget((WpsEnvironment) e, this, t);
            TableLayout.Row r = table.addRow();
            r.addCell().add(new Label(labelText(getEnvironment().getMessageProvider(), t)));
            r.addCell().add(widget);
            table.addVerticalSpace();
            vtypesToSettings.put(t, widget);
        }
        this.add(table);
    }

    private static String labelText(final MessageProvider msgProvider, EValType type) {
        switch (type) {
            case BIN:
                return msgProvider.translate("ColumnAlignmentSettings", "Binary");
            case BLOB:
                return msgProvider.translate("ColumnAlignmentSettings", "BLOB");
            case CHAR:
                return msgProvider.translate("ColumnAlignmentSettings", "Character");
            case CLOB:
                return msgProvider.translate("ColumnAlignmentSettings", "CLOB");
            case DATE_TIME:
                return msgProvider.translate("ColumnAlignmentSettings", "Date/time");
            case INT:
                return msgProvider.translate("ColumnAlignmentSettings", "Integer");
            case NUM:
                return msgProvider.translate("ColumnAlignmentSettings", "Real number");
            case STR:
                return msgProvider.translate("ColumnAlignmentSettings", "String");
            case PARENT_REF:
                return msgProvider.translate("ColumnAlignmentSettings", "Parent reference");
            default:
                return type.getName();
        }
    }
}