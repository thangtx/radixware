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

package org.radixware.kernel.explorer.editors.editmask.inteditor;

import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.editors.editmask.controls.AbstractComplexCheckableSetting;
import org.radixware.kernel.explorer.editors.valeditors.ValIntEditor;

final class IntMinimumLengthSetting extends AbstractComplexCheckableSetting
        implements IEditMaskEditorSetting {

    private IntPadCharacterSetting padChar;
    private final ValIntEditor valueEditor;

    public IntMinimumLengthSetting(final IClientEnvironment environment, final QWidget parent, final IntPadCharacterSetting pad) {
        super(parent);
        final String checkBoxTitle = environment.getMessageProvider().translate("EditMask", "Minimum length:");
        this.setText(checkBoxTitle);
        
        valueEditor = new ValIntEditor(environment, this);
        addWidget(valueEditor, getOption());
        padChar = pad;
        padChar.setParent((QWidget)this);
        addWidget(padChar, padChar.getOption());
        
        final EditMaskInt emi = new EditMaskInt();
        emi.setMinValue(0);
        emi.setMaxValue(255);
        valueEditor.setEditMask(emi);
        setDefaultValue();
        enable(false);
    }

    public void addPadChar(final IntPadCharacterSetting ipcs) {
        padChar = ipcs;
        addWidget(padChar, padChar.getOption());
        enable(false);
    }
    
    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.INT_MIN_LENGTH;
    }

    @Override
    public void addToXml(final org.radixware.schemas.editmask.EditMask editMask) {
        if(isChecked()) {
            editMask.getInt().setMinLength(valueEditor.getValue().byteValue());
        }
    }

    @Override
    public void loadFromXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskInt emi = editMask.getInt();
        if(emi.isSetMinLength() && emi.getMinLength() != 0) {
            valueEditor.setValue(Long.valueOf(editMask.getInt().getMinLength()));
            enable(true);
        } else {
            setDefaultValue();
            enable(false);
        }
        
    }

    @Override
    public void setDefaultValue() {
        final EditMaskInt em = new EditMaskInt();
        final byte val = em.getMinLength();
        valueEditor.setValue((long)val);
    }

    
    @Override
    public Object getValue() {
        return valueEditor.getValue();
    }

    @Override
    protected void onCheckBoxStateChanged() {
        final boolean checked = isChecked();
        valueEditor.setEnabled(checked);
        padChar.setEnabled(checked);
        if(!checked){
            setDefaultValue();
            padChar.setDefaultValue();
        }
    }

}
