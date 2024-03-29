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

import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.editors.editmask.controls.AbstractCheckableEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValIntEditor;



final class StrIntegerMaximumValueSetting extends AbstractCheckableEditor<Long> implements IEditMaskEditorSetting {
    private boolean isChar;
    public StrIntegerMaximumValueSetting(final IClientEnvironment environment, final QWidget parent) {
        super(environment, parent);
        final String title = environment.getMessageProvider().translate("EditMask", "Maximum value:");
        setText(title);
        enable(false);
        setDefaultValue();
        stateChanged.connect((QWidget)this, "onStateChanged()");
        
    }
    @Override
    public void addToXml(final org.radixware.schemas.editmask.EditMask editMask) {
        if(isChecked()) {
            editMask.getStr().setMaxValue(String.valueOf(getValue()));
        }
    }

    @Override
    public void loadFromXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskStr ems = editMask.getStr();
        if(ems.isSetMaxValue()) {
            setValue(Long.valueOf(ems.getMaxValue()));
            enable(true);
        } else {
            setDefaultValue();
            enable(false);
        }
    }

    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.STR_VC_INTEGER_MAXIMUM;
    }

    @Override
    public void setDefaultValue() {
        final Long defaultValue = isChar ? 9L : Long.MAX_VALUE;
        setValue(defaultValue);
    }

    
    @Override
    protected ValEditor<Long> initValueEditor(final IClientEnvironment environment) {
        final ValIntEditor editor = new ValIntEditor(environment, this);
        editor.setSizePolicy(Policy.Minimum, Policy.Fixed);
        return editor;
    }
    
    @SuppressWarnings("unused")
    private void onStateChanged() {
        enable(isChecked());
        if(!isChecked()) {
            setDefaultValue();
        }
    }
    
    public void setIsChar(final boolean isChar) {
        this.isChar = isChar;
        setDefaultValue();
    }
}