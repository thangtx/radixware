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

import java.util.List;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.WpsSettings;
import org.radixware.wps.rwt.Alignment;
import org.radixware.wps.rwt.Container;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.views.editors.valeditors.ValListEditorController;


public class AlignmentSettingsWidget extends SettingsWidget {

    final private ValListEditorController<String> listStringController;

    public AlignmentSettingsWidget(final WpsEnvironment env, final UIObject parent, final String gr, final String sub, final String n, List<org.radixware.wps.rwt.Alignment> alignList, Alignment defVal) {
        super(env, parent, gr, sub, n, defVal);
        final EditMaskList mask = new EditMaskList();

        if (defVal != null) {
            defaultValue = (int) defVal.getValue();//getDefaultSettings().readInteger(getSettingCfgName());
        } else {
            defaultValue = Alignment.RIGHT.ordinal();
        }
        MessageProvider messageProvider = e.getApplication().getMessageProvider();
        for (Alignment a : alignList) {
            if (a != null) {
                String aTr  = messageProvider.translate("Settings Dialog", a.name());
                mask.addItem(aTr, a.name());
            }
        }
        listStringController = new ValListEditorController<>(env, mask);
        listStringController.setMandatory(true);
        createUI();
    }

    private void createUI() {
        Container container = new Container();
        this.add(container);
        container.add(((UIObject) listStringController.getValEditor()));
    }

    @Override
    public void readSettings(WpsSettings src) {
        int value = src.readInteger(getSettingCfgName());
        if (src.getValue(getSettingCfgName()) == null || "".equals(src.getValue(getSettingCfgName()))) {
            value = (int)defaultValue;
        }
        Alignment v = Alignment.getForValue(value);
        listStringController.setValue(v.name());
    }

    @Override
    public void writeSettings(WpsSettings dst) {
        String val = listStringController.getValue();
        long value = Alignment.getForName(val);
        dst.writeInteger(getSettingCfgName(), (int) value);
    }

    @Override
    public void restoreDefaults() {
        listStringController.setValue(Alignment.getForValue((int)defaultValue).name());
    }
}
