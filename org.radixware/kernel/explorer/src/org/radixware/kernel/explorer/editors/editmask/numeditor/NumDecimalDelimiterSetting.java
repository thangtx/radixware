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

package org.radixware.kernel.explorer.editors.editmask.numeditor;

import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.editors.editmask.controls.AbstractComplexCheckableSetting;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;


final class NumDecimalDelimiterSetting extends AbstractComplexCheckableSetting implements IEditMaskEditorSetting {
    private final ValStrEditor valueEditor;
    
    public NumDecimalDelimiterSetting(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
        valueEditor = new ValStrEditor(environment, this);
        addWidget(valueEditor, getOption());
        final String labelText = environment.getMessageProvider().translate("EditMask", "Decimal separator:");
        setText(labelText);
        
        final EditMaskStr editMask = new EditMaskStr();
        editMask.setMaxLength(1);
        valueEditor.setEditMask(editMask);
        setDefaultValue();
        enable(false);
    }
    
    @Override
    protected void onCheckBoxStateChanged() {
        final boolean checked = isChecked();
        valueEditor.setEnabled(checked);
        if(!checked) {
           setDefaultValue();
        }
    }

    @Override
    public void addToXml(final org.radixware.schemas.editmask.EditMask editMask) {
        if(isChecked()) {
            editMask.getNum().setDecimalDelimeter(valueEditor.getValue());
        }
    }

    @Override
    public void loadFromXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskNum editMaskNum = editMask.getNum();
        if(editMaskNum.isSetDecimalDelimeter()) {
            valueEditor.setValue(editMaskNum.getDecimalDelimeter());
            enable(true);
        } else {
            setDefaultValue();
            enable(false);
        }
    }

    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.NUM_DECIMAL_DELIMITER;
    }

    @Override
    public void setDefaultValue() {
        valueEditor.setValue("");
    }

    @Override
    public String getValue() {
        return valueEditor.getValue();
    }
    
}
