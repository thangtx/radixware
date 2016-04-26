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

package org.radixware.kernel.explorer.editors.editmask.streditor;

import com.trolltech.qt.core.Qt.CheckState;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.editors.editmask.controls.AbstractComplexCheckableSetting;
import org.radixware.kernel.explorer.editors.valeditors.ValIntEditor;



final class StrMaximumLengthSetting
             extends AbstractComplexCheckableSetting
             implements IEditMaskEditorSetting {
    private final ValIntEditor valueEditor;
    
    public StrMaximumLengthSetting(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
        final String checkBoxTitle = environment.getMessageProvider().translate("EditMask", "Maximum length");
        this.setText(checkBoxTitle);
        
        valueEditor = new ValIntEditor(environment, this);
        //set default value
        setDefaultValue();
        //set edit mask for value editor
        final EditMaskInt emi = new EditMaskInt();
        emi.setMaxValue(valueEditor.getValue());
        emi.setMinValue(0);
        valueEditor.setEditMask(emi);
        addWidget(valueEditor, getOption());
        enable(true);
        setState(CheckState.Unchecked);
    }
    
    @Override
    public void addToXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskStr ems = editMask.getStr();
        if(isChecked()) {
            ems.setMaxLength(valueEditor.getValue().intValue());
        }
    }

    @Override
    public void loadFromXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskStr ems = editMask.getStr();
        if(ems.isSetMaxLength()) {
            valueEditor.setValue((long)ems.getMaxLength());
            enable(true);
        } else {
            setDefaultValue();
            enable(false);
        }
    }

    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.STR_MAX_LENGTH;
    }

    @Override
    public void setDefaultValue() {
        final EditMaskStr ems = new EditMaskStr();
        final int value = ems.getMaxLength();
        valueEditor.setValue((long)value);
    }

    @Override
    public Long getValue() {
        return valueEditor.getValue();
    }

    public void setValue(final Long value) {
        valueEditor.setValue(value);
    }
    
    public Signal1<Integer> getStateChangedSignal() {
        return ((QCheckBox)super.findChild(QCheckBox.class)).stateChanged;
    }
    
    @Override
    protected void onCheckBoxStateChanged() {
        valueEditor.setEnabled(isChecked());
        if(!isChecked()) {
            setDefaultValue();
        }
    }

}
