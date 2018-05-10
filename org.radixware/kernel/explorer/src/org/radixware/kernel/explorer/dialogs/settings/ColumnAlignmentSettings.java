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

package org.radixware.kernel.explorer.dialogs.settings;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.explorer.env.IExplorerSettings;

/**
 * Class represents a set of alignment options combo boxes
 */
final class ColumnAlignmentSettings extends SettingsWidget implements ISettingsPage{
    
    private final static EnumSet<EValType> valueTypes = EnumSet.of(
            EValType.BIN,
            EValType.BLOB,  EValType.CHAR,
            EValType.CLOB,  EValType.DATE_TIME,
            EValType.INT,   EValType.NUM,
            EValType.STR,   EValType.PARENT_REF);
    
    private final Map<EValType, ColumnAlignmentSetting> vtypesToSettings = new HashMap<>();
    
    public ColumnAlignmentSettings(final IClientEnvironment env, final QWidget parent) {
        super(env, parent, SettingNames.SELECTOR_GROUP, null, "");
    }

    @Override
    public void readSettings(IExplorerSettings src) {
        for(ColumnAlignmentSetting s : vtypesToSettings.values()) {
            s.readSettings(src);
        }
    }

    @Override
    public void writeSettings(IExplorerSettings dst) {
        for(ColumnAlignmentSetting s : vtypesToSettings.values()) {
            s.writeSettings(dst);
        }
    }

    @Override
    public void restoreDefaults() {
        for(ColumnAlignmentSetting s : vtypesToSettings.values()) {
            s.restoreDefaults();
        }
    }

    @Override
    public void open(final IClientEnvironment environment, 
                             final ISettingsProvider settingsProvider,
                             final List<SettingsWidget> settingWidgets) {
        setUpUi();
        settingWidgets.add(this);
        readSettings(settingsProvider.getSettings());
    }        

    private void setUpUi() {
        final QGridLayout layout = new QGridLayout();
        layout.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignLeft, Qt.AlignmentFlag.AlignTop));
        this.setLayout(layout);
        
        int i = 0;
        for(EValType t : valueTypes) {
            final QLabel label = new QLabel(labelText(getEnvironment().getMessageProvider(), t), this);
            layout.addWidget(label, i, 0);
            
            ColumnAlignmentSetting setting = new ColumnAlignmentSetting(getEnvironment(), this, t);
            vtypesToSettings.put(t, setting);
            layout.addWidget(setting, i, 1);
            
            i++;
        }
    }

    private static String labelText(final MessageProvider msgProvider, EValType type) {
        switch(type) {
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
