/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.wps.settings;

import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.WpsSettings;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.views.editors.valeditors.ValIntEditorController;


public class IntSettingsWidget extends SettingsWidget{
    
    private final ValIntEditorController editorController;
    private int defaultValue;
    
    public IntSettingsWidget(final WpsEnvironment env, 
                                         final UIObject parent, 
                                         final String gr, 
                                         final String sub, 
                                         final String n,
                                         final int minVal,
                                         final int maxVal){
        super(env, parent, gr, sub, n);
        final EditMaskInt mask = new EditMaskInt();
        mask.setMinValue(minVal);
        mask.setMaxValue(maxVal);
        editorController = new ValIntEditorController(env);
        editorController.setMandatory(true);
        editorController.setEditMask(mask);
        add((UIObject)editorController.getValEditor());
        final String defaultValAsStr = this.readDefaultValue();
        if (defaultValAsStr!=null && !defaultValAsStr.isEmpty()){
            try{
                defaultValue = Integer.parseInt(defaultValAsStr);
            }catch(NumberFormatException exception){
                defaultValue = 0;
            }
        }else{
            defaultValue = 0;
        }
        editorController.setValue(Long.valueOf(defaultValue));
    }

    @Override
    public void readSettings(final WpsSettings src) {
        final String settingName = getSettingCfgName();
        final int value = src.readInteger(settingName, defaultValue);
        editorController.setValue(Long.valueOf(value));
    }

    @Override
    public void writeSettings(final WpsSettings dst) {
        dst.writeInteger(getSettingCfgName(), editorController.getValue().intValue());
    }

    @Override
    public void restoreDefaults() {
        editorController.setValue(Long.valueOf(defaultValue));
    }

}
