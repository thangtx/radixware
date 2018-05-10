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

import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QSpinBox;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QLabel;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.env.IExplorerSettings;

final class IntegerSettingsWidget extends SettingsWidget {

    private final QLabel settingLabel;
    private final QSpinBox settingValue = new QSpinBox(this);
    private final Integer specialValue;
    private final Integer defaultValue;    
    private final QGridLayout parentGridLayout;

    public IntegerSettingsWidget(final IClientEnvironment environment, 
                                 final QWidget parent, 
                                 final String gr, 
                                 final String sub, 
                                 final String n, 
                                 final String descr, 
                                 final QGridLayout parentGridLayout,
                                 final int minValue,
                                 final int maxValue,
                                 final String specialValueText) {
        super(environment, parent, gr, sub, n);
        defaultValue = getDefaultSettings().readInteger(getSettingCfgName());
        this.parentGridLayout = parentGridLayout;

        settingLabel = new QLabel(descr,this);        
        if (minValue>Integer.MIN_VALUE){
            specialValue = minValue - 1;
            settingValue.setRange(specialValue, maxValue);
            settingValue.setSpecialValueText(specialValueText);
        }else{
            settingValue.setRange(minValue, maxValue);
            specialValue = null;
        }                
        
        restoreDefaults();
        
        settingValue.setFixedSize(150, settingValue.sizeHint().height());
    }

    public void addToParent(final int row) {
        parentGridLayout.addWidget(settingLabel, row, 0);
        parentGridLayout.addWidget(settingValue, row, 1);
    }

    @Override
    public final void restoreDefaults() {
        if (defaultValue==null){
            settingValue.setValue(specialValue==null ? settingValue.minimum() : specialValue);
        }else{
            settingValue.setValue(defaultValue);
        }
    }

    @Override
    public void readSettings(final IExplorerSettings src) {
        final int defaultVal;
        if (defaultValue==null){
            defaultVal=specialValue==null ? settingValue.minimum() : specialValue;
        }else{
            defaultVal = defaultValue;
        }
        
        settingValue.setValue(src.readInteger(getSettingCfgName(), defaultVal));
    }

    @Override
    public void writeSettings(final IExplorerSettings dst) {
        dst.writeInteger(getSettingCfgName(), settingValue.value());
    }
}