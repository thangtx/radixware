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

import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.IExplorerSettings;


import org.radixware.kernel.explorer.utils.WidgetUtils;


final class InheritablePropertySettingsWidget extends SettingsWidget {

    private final List<SettingsWidget> settingsArrayList = new ArrayList<>();
    private final String foregroundSettingsKey;

    public InheritablePropertySettingsWidget(final IClientEnvironment environment, final QWidget parent, final String gr, final String sub, final String n, final String title) {
        super(environment, parent, gr, sub, n);

        final QVBoxLayout contentLayout = WidgetUtils.createVBoxLayout(this);
        setLayout(contentLayout);
        final QGroupBox groupBox = new QGroupBox(title);
        contentLayout.addWidget(groupBox);

        groupBox.setSizePolicy(Policy.Preferred, Policy.Maximum);

        final QHBoxLayout topLayout = WidgetUtils.createHBoxLayout(null);
        topLayout.setAlignment(new Alignment(AlignmentFlag.AlignLeft, AlignmentFlag.AlignTop));

        final QGridLayout mainLayout = new QGridLayout();
        mainLayout.setContentsMargins(8, 0, 0, 8);
        mainLayout.setWidgetSpacing(8);
        mainLayout.setAlignment(new Alignment(AlignmentFlag.AlignLeft, AlignmentFlag.AlignTop));
        int mainLayoutIdx = 0;

        //Font settings
        final FontSettingsWidget fontSettings = new FontSettingsWidget(getEnvironment(), groupBox, gr, sub, n + "/" + SettingNames.TextOptions.FONT, Application.translate("Settings Dialog", "Font"));
        fontSettings.addToParent(topLayout);
        settingsArrayList.add(fontSettings);
        mainLayout.addLayout(topLayout, mainLayoutIdx++, 0);

        //Background color settings
        final ColorSettingsWidget backgroundSettings = new ColorSettingsWidget(getEnvironment(), groupBox, gr, sub, n + "/" + SettingNames.TextOptions.BCOLOR, Application.translate("Settings Dialog", "Background color"));
        backgroundSettings.addToParent(mainLayoutIdx++, mainLayout);
        settingsArrayList.add(backgroundSettings);

        //Foreground color settings
        foregroundSettingsKey = n + "/" + SettingNames.TextOptions.FCOLOR;
        final QLabel foregroundLabel = new QLabel(groupBox);
        foregroundLabel.setText(Application.translate("Settings Dialog", "Foreground color"));
        mainLayout.addWidget(foregroundLabel, mainLayoutIdx, 0, mainLayoutIdx, 3, new Alignment(AlignmentFlag.AlignTop, AlignmentFlag.AlignHCenter));
        mainLayout.setRowMinimumHeight(mainLayoutIdx, foregroundLabel.sizeHint().height());
        mainLayoutIdx++;

//        final QHBoxLayout foregroundColorsLayout = WidgetUtils.createHBoxLayout(null);
//        foregroundColorsLayout.setSpacing(3);

        final ColorSettingsWidget ownColor = new ColorSettingsWidget(getEnvironment(), groupBox, gr, sub,
                foregroundSettingsKey,
                Application.translate("Settings Dialog", "Own"));
        ownColor.addToParent(mainLayoutIdx++, mainLayout);
        settingsArrayList.add(ownColor);

        final ColorSettingsWidget inheritedColor = new ColorSettingsWidget(getEnvironment(), groupBox, gr, sub,
                foregroundSettingsKey + "/" + SettingNames.Properties.INHERITED,
                Application.translate("Settings Dialog", "Inherited"));
        inheritedColor.addToParent(mainLayoutIdx++, mainLayout);
        settingsArrayList.add(inheritedColor);

        final ColorSettingsWidget overridedColor = new ColorSettingsWidget(getEnvironment(), groupBox, gr, sub,
                foregroundSettingsKey + "/" + SettingNames.Properties.OVERRIDED,
                Application.translate("Settings Dialog", "Overridden"));
        overridedColor.addToParent(mainLayoutIdx++, mainLayout);
        settingsArrayList.add(overridedColor);
        
        final ColorSettingsWidget undefinedColor = new ColorSettingsWidget(getEnvironment(), groupBox, gr, sub,
                foregroundSettingsKey + "/" + SettingNames.Properties.UNDEFINED,
                Application.translate("Settings Dialog", "Undefined"));
        undefinedColor.addToParent(mainLayoutIdx++, mainLayout);
        settingsArrayList.add(undefinedColor);

        groupBox.setLayout(mainLayout);
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

    @Override
    public void restoreDefaults() {
        for (SettingsWidget w : settingsArrayList) {
            w.restoreDefaults();
        }
    }
}
