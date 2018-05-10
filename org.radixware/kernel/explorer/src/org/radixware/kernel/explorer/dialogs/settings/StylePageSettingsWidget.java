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

import java.util.ArrayList;

import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.IExplorerSettings;

final class StylePageSettingsWidget extends SettingsWidget {

    private final ArrayList<SettingsWidget> settingsArrayList = new ArrayList<>();

    public StylePageSettingsWidget(final IClientEnvironment environment, final QWidget parent, final String group, final String subgroup, final String name) {
        super(environment, parent, group, subgroup, name);

        final QVBoxLayout gridLayout = new QVBoxLayout(this);
        gridLayout.setAlignment(new Alignment(AlignmentFlag.AlignLeft, AlignmentFlag.AlignTop));

        SelectorSettingsWidget w12 = new SelectorSettingsWidget(environment, this, group + "/", subgroup + "/" + name, SettingNames.Properties.READONLY_PROPERTY, Application.translate("Settings Dialog", "Readonly Properties Value"));
        gridLayout.addWidget(w12);
        settingsArrayList.add(w12);

        PropertySettingsWidget w22 = new PropertySettingsWidget(environment, this, group + "/", subgroup + "/" + name, SettingNames.Properties.MANDATORY_PROPERTY, Application.translate("Settings Dialog", "Mandatory Properties Value"), true);
        gridLayout.addWidget(w22);
        settingsArrayList.add(w22);

        SelectorSettingsWidget w32 = new SelectorSettingsWidget(environment, this, group + "/", subgroup + "/" + name, SettingNames.Properties.OTHER_PROPERTY, Application.translate("Settings Dialog", "Other Properties Value"));
        gridLayout.addWidget(w32);
        settingsArrayList.add(w32);
        
        setLayout(gridLayout);
    }

    @Override
    public void restoreDefaults() {
        for (SettingsWidget w : settingsArrayList) {
            w.restoreDefaults();
        }
    }

    @Override
    public void readSettings(IExplorerSettings src) {
        for (SettingsWidget w : settingsArrayList) {
            w.readSettings(src);
        }
    }

    @Override
    public void writeSettings(IExplorerSettings dst) {
        for (SettingsWidget w : settingsArrayList) {
            w.writeSettings(dst);
        }
    }

    public SelectorSettingsWidget getReadonlyPropertySettingsWidget() {
        return (SelectorSettingsWidget) settingsArrayList.get(0);
    }
    
    public PropertySettingsWidget getMandatoryPropertySettingsWidget() {
        return (PropertySettingsWidget) settingsArrayList.get(1);
    }

    public SelectorSettingsWidget getOtherPropertySettingsWidget() {
        return (SelectorSettingsWidget) settingsArrayList.get(2);
    }
}
