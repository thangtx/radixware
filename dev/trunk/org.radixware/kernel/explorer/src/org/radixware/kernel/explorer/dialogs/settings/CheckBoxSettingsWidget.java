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

import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.env.IExplorerSettings;
import org.radixware.kernel.explorer.utils.WidgetUtils;

class CheckBoxSettingsWidget extends SettingsWidget {

    protected final QCheckBox bSaveCheckBox;
    private final Boolean defaultvalue;
    protected final QVBoxLayout vertLayout;

    public CheckBoxSettingsWidget(final IClientEnvironment environment, final QWidget parent, final String gr, final String sub, final String n, final String descr) {
        super(environment, parent, gr, sub, n);

        defaultvalue = getDefaultSettings().readBoolean(getSettingCfgName());
        vertLayout = WidgetUtils.createVBoxLayout(this);

        bSaveCheckBox = new QCheckBox(this);
        bSaveCheckBox.setEnabled(true);

        bSaveCheckBox.setText(descr);
        bSaveCheckBox.setChecked(true);
        bSaveCheckBox.clicked.connect(this, "onChangeCheckBox()");

        vertLayout.addWidget(bSaveCheckBox);
    }

    @Override
    public void restoreDefaults() {
        bSaveCheckBox.setChecked(defaultvalue);
    }

    @Override
    public void readSettings(IExplorerSettings src) {
        final boolean value = src.readBoolean(getSettingCfgName(), defaultvalue);
        bSaveCheckBox.setChecked(value);
        onChangeCheckBox();
    }

    @Override
    public void writeSettings(IExplorerSettings dst) {
        dst.writeBoolean(getSettingCfgName(), bSaveCheckBox.isChecked());
    }

    protected void onChangeCheckBox() {
        ;//dummy
    }
}
