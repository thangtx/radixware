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

import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.enums.ESelectorColumnAlign;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.WpsSettings;
import org.radixware.wps.rwt.Container;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.ValueEditor;
import org.radixware.wps.views.editors.valeditors.ValListEditorController;


public class ColumnAlignmentSettingsWidget extends SettingsWidget {

    final private ValListEditorController<String> listStringController;
    private ESelectorColumnAlign alignment = ESelectorColumnAlign.DEFAULT;
    final private EValType valType;

    public ColumnAlignmentSettingsWidget(final WpsEnvironment env, final UIObject parent, EValType type) {
                super(env, 
                parent, 
                SettingNames.SELECTOR_GROUP, 
                SettingNames.Selector.COMMON_GROUP + "/" + SettingNames.Selector.Common.BODY_ALIGNMENT, 
                type.getName(), 
                null/*ESelectorColumnAlign.DEFAULT*/);
        final EditMaskList mask = new EditMaskList();
        this.valType = type;
        MessageProvider mp = getEnvironment().getApplication().getMessageProvider();
        for (ESelectorColumnAlign a : ESelectorColumnAlign.values()) {
            if (a != null) {
                String nameTr = mp.translate("Settings Dialog", a.name());
                mask.addItem(nameTr, a.getName().toUpperCase());
            }
        }
        listStringController = new ValListEditorController<>(env, mask);
        listStringController.setMandatory(true);
        listStringController.addValueChangeListener(new ValueEditor.ValueChangeListener<String>() {
            @Override
            public void onValueChanged(String oldValue, String newValue) {
                if (newValue != null) {
                    ESelectorColumnAlign align = ESelectorColumnAlign.getForTitle(newValue);
                    setAlignment(align);
                }
            }
        });
        createUI();
    }

    private void createUI() {
        Container container = new Container();
        this.add(container);
        container.add(((UIObject) listStringController.getValEditor()));
    }

    private void setAlignment(final ESelectorColumnAlign alignment) {
        this.alignment = alignment;
        listStringController.setValue(alignment.name().toUpperCase());
    }

    @Override
    public void readSettings(WpsSettings src) {
        int value = src.readInteger(getSettingCfgName(), alignment.getValue().intValue());
        ESelectorColumnAlign v = ESelectorColumnAlign.getForValue((long) value);
        setAlignment(v);
    }

    @Override
    public void writeSettings(WpsSettings dst) {
        String val = listStringController.getValue().toUpperCase();
        long value = ESelectorColumnAlign.getForTitle(val).getValue();
        dst.writeInteger(getSettingCfgName(), (int) value);
    }

    @Override
    public void restoreDefaults() {
        alignment = defaultValue(valType);
    }

    private static ESelectorColumnAlign defaultValue(final EValType valType) {
        switch (valType) {
            case INT:
            case NUM:
            case BOOL:
            case DATE_TIME:
                return ESelectorColumnAlign.RIGHT;
            case STR:
            case CHAR:
            case PARENT_REF:
                return ESelectorColumnAlign.LEFT;
            default:
                return ESelectorColumnAlign.DEFAULT;
        }
    }
}
