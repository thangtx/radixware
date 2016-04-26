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

import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.WpsSettings;
import org.radixware.wps.rwt.CheckBox;
import org.radixware.wps.rwt.Container;
import org.radixware.wps.rwt.UIObject;


public class CheckBoxSettingsWidget extends SettingsWidget {

    private String descr;
    private CheckBox checkBox;

    public CheckBoxSettingsWidget(final WpsEnvironment env, final UIObject parent, final String gr, final String sub, final String n, final String descr, boolean val) {
        super(env, parent, gr, sub, n, val);
        createUI();
        defaultValue = val;//getDefaultSettings().readBoolean(getSettingCfgName());
        this.descr = descr;
    }

    private void createUI() {
        Container container = new Container();
        this.add(container);
        checkBox = new CheckBox();
        checkBox.setParent(container);
        checkBox.setTitle(descr);
        container.add(checkBox);
    }

    @Override
    public void readSettings(WpsSettings src) {
        boolean val = src.readBoolean(getSettingCfgName(), (boolean)defaultValue);
        if (src.getValue(getSettingCfgName()) == null || "".equals(src.getValue(getSettingCfgName()))){
            val = (boolean)defaultValue;
        }
        checkBox.setSelected(val);
    }

    @Override
    public void writeSettings(WpsSettings dst) {
        dst.writeBoolean(getSettingCfgName(), checkBox.isSelected());
    }

    @Override
    public void restoreDefaults() {
        checkBox.setSelected((boolean)defaultValue);
    }
}
