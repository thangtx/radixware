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

import java.util.ArrayList;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.WpsSettings;
import org.radixware.wps.rwt.TableLayout;
import org.radixware.wps.rwt.UIObject;

public class StylePageSettingsWidget extends SettingsWidget {

    private final ArrayList<SettingsWidget> settingsArrayList = new ArrayList<>();

    public StylePageSettingsWidget(WpsEnvironment env, UIObject parent, String group, String subGroup, String name) {
        super(env, parent, group, subGroup, name, null);
        TableLayout box = new TableLayout();
        add(box);

        SelectorSettingWidget w12 = new SelectorSettingWidget(env, this, group, subGroup + "/" + name,
                SettingNames.Properties.READONLY_PROPERTY,
                env.getMessageProvider().translate("Settings Dialog", "Readonly Properties Value"), null);
        box.addRow().addCell().add(w12);
        box.addVerticalSpace();
        settingsArrayList.add(w12);

        PropertySettingsWidget w22 = new PropertySettingsWidget(env, this, group, subGroup + "/" + name,
                SettingNames.Properties.MANDATORY_PROPERTY,
                env.getMessageProvider().translate("Settings Dialog", "Mandatory Properties Value"),
                true, null);
        box.addRow().addCell().add(w22);
        box.addVerticalSpace();
        settingsArrayList.add(w22);

        SelectorSettingWidget w32 = new SelectorSettingWidget(env, this, group, subGroup + "/" + name,
                SettingNames.Properties.OTHER_PROPERTY,
                env.getMessageProvider().translate("Settings Dialog", "Other Properties Value"), null);
        box.addRow().addCell().add(w32);
        settingsArrayList.add(w32);
    }

    @Override
    public void restoreDefaults() {
        for (SettingsWidget w : settingsArrayList) {
            w.restoreDefaults();
        }
    }

    @Override
    public void readSettings(WpsSettings src) {
        for (SettingsWidget w : settingsArrayList) {
            w.readSettings(src);
        }
    }

    @Override
    public void writeSettings(WpsSettings dst) {
        for (SettingsWidget w : settingsArrayList) {
            w.writeSettings(dst);
        }
    }

    public SelectorSettingWidget getReadonlyPropertySettingsWidget() {
        SelectorSettingWidget roProp = (SelectorSettingWidget) settingsArrayList.get(0);
        roProp.setDefaultValue(getDefaultValue());
        return roProp;
    }

    public PropertySettingsWidget getMandatoryPropertySettingsWidget() {
        PropertySettingsWidget mProp = (PropertySettingsWidget) settingsArrayList.get(1);
        mProp.setDefaultValue(getDefaultValue());
        return mProp;
    }

    public SelectorSettingWidget getOtherPropertySettingsWidget() {
        SelectorSettingWidget othProp = (SelectorSettingWidget) settingsArrayList.get(2);
        othProp.setDefaultValue(getDefaultValue());
        return othProp;
    }
}
