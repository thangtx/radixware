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

//import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.env.IExplorerSettings;

final class SwitcherSettingsWidget extends CheckBoxSettingsWidget {

    private final ArrayList<SettingsWidget> firstGroupList = new ArrayList<>();
    private final ArrayList<SettingsWidget> secondGroupList = new ArrayList<>();

    public SwitcherSettingsWidget(final IClientEnvironment environment, final QWidget parent, final String gr, final String sub, final String n, final String descr, final boolean show) {
        super(environment, parent, gr, sub, n, descr);
    }

    public void addInFirstGroup(final SettingsWidget sw) {
        firstGroupList.add(sw);
    }

    public void addInSecondGroup(final SettingsWidget sw) {
        secondGroupList.add(sw);
    }

    @Override
    public void restoreDefaults() {
        super.restoreDefaults();

        for (SettingsWidget w : firstGroupList) {
            w.restoreDefaults();
        }
        for (SettingsWidget w : secondGroupList) {
            w.restoreDefaults();
        }

        updateWidgets();
    }

    @Override
    public void readSettings(IExplorerSettings src) {

        super.readSettings(src);
        for (SettingsWidget w : firstGroupList) {
            w.readSettings(src);
        }
        for (SettingsWidget w : secondGroupList) {
            w.readSettings(src);
        }
    }

    @Override
    public void writeSettings(IExplorerSettings dst) {

        super.writeSettings(dst);
        for (SettingsWidget w : firstGroupList) {
            w.writeSettings(dst);
        }
        for (SettingsWidget w : secondGroupList) {
            w.writeSettings(dst);
        }
    }

    @Override
    protected void onChangeCheckBox() {
        //enabled <----> disabled
        updateWidgets();
    }

    private void updateWidgets() {
        if (bSaveCheckBox.isChecked()) {
            for (SettingsWidget w : firstGroupList) {
                w.setEnabled(true);
            }
            for (SettingsWidget w : secondGroupList) {
                w.setEnabled(false);
            }
        } else {
            for (SettingsWidget w : firstGroupList) {
                w.setEnabled(false);
            }
            for (SettingsWidget w : secondGroupList) {
                w.setEnabled(true);
            }
        }
    }
}