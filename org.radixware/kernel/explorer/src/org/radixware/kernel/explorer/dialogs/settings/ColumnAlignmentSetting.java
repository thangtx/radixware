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

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.Arrays;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.enums.ESelectorColumnAlign;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.explorer.env.ExplorerSettings;

/**
 * Class represents combo-box of alignment options.
 */
class ColumnAlignmentSetting extends SettingsWidget {

    private ESelectorColumnAlign alignment = ESelectorColumnAlign.DEFAULT;
    private final EValType valType;
   
    private QComboBox combo;
    
    public ColumnAlignmentSetting(final IClientEnvironment env, final QWidget parent, EValType valType) {
        super(env, parent, SettingNames.SELECTOR_GROUP, SettingNames.Selector.COMMON_GROUP + "/" + SettingNames.Selector.Common.BODY_ALIGNMENT, valType.getName());
        this.valType = valType;
        setLayout(createView());
        
    }
    
    @Override
    public void readSettings(ExplorerSettings src) {
        final int alignInt = src.readInteger(getSettingCfgName(), ESelectorColumnAlign.DEFAULT.getValue().intValue());
        setAlignment(ESelectorColumnAlign.getForValue((long)alignInt));
    }

    @Override
    public void writeSettings(ExplorerSettings dst) {
        dst.writeInteger(getSettingCfgName(), alignment.getValue().intValue());
    }

    @Override
    public void restoreDefaults() {
        alignment = defaultValue(valType);
    }
    
    public ESelectorColumnAlign getAlignment() {
        return alignment;
    }

    private void setAlignment(final ESelectorColumnAlign alignment) {
        this.alignment =  alignment;
        combo.setCurrentIndex(this.alignment.ordinal());
    }
            
    private static ESelectorColumnAlign defaultValue(final EValType valType) {
        switch(valType) {
            case INT: case NUM: case BOOL: case DATE_TIME:
                return ESelectorColumnAlign.RIGHT;
            case STR: case CHAR: case PARENT_REF: 
                return ESelectorColumnAlign.LEFT;
            default:
                return ESelectorColumnAlign.DEFAULT;
        }
        
    }

    private QLayout createView() {
        final QHBoxLayout layout = new QHBoxLayout();
        layout.setMargin(0);
        final List<ESelectorColumnAlign> aligns = Arrays.asList(ESelectorColumnAlign.values());
        
        combo = new QComboBox(this);
        combo.activatedIndex.connect(this, "onChangeAlignment(Integer)");
        for(ESelectorColumnAlign a : aligns){
            combo.addItem(getAlignTitle(a), a.getValue());
        }
        combo.setCurrentIndex(alignment.ordinal());
        layout.addWidget(combo);
         
        return layout;
    }
    
    private String getAlignTitle(final ESelectorColumnAlign align){
        final MessageProvider mp = getEnvironment().getMessageProvider();
        switch (align){
            case CENTER:
                return mp.translate("Settings Dialog", "Center");
            case LEFT:
                return mp.translate("Settings Dialog", "Left");
            case RIGHT:
                return mp.translate("Settings Dialog", "Right");
            case DEFAULT:
                return mp.translate("Settings Dialog", "Default");
            default:
                return align.getName();
        }
    }
    
    @SuppressWarnings("unused")
    private void onChangeAlignment(Integer index) {
        setAlignment(ESelectorColumnAlign.getForValue((Long) combo.itemData(index, Qt.ItemDataRole.UserRole)));
    }
}
