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
import java.math.BigDecimal;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.editors.editmask.controls.AbstractCheckableEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValNumEditor;



final class StrNumberMaximumValueSetting extends AbstractCheckableEditor<BigDecimal> implements IEditMaskEditorSetting{
        
    public StrNumberMaximumValueSetting(final IClientEnvironment environment, final QWidget parent) {
        super(environment, parent);
        final String title = environment.getMessageProvider().translate("EditMask", "Maximum:");
        setText(title);
        enable(false);
        setDefaultValue();
        stateChanged.connect((QWidget)this, "onStateChanged()");
    }
    
    @Override
    public void addToXml(final org.radixware.schemas.editmask.EditMask editMask) {
        if(isChecked()) {
            editMask.getStr().setMaxValue(getValue().toString());
        }
    }

    @Override
    public void loadFromXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskStr ems = editMask.getStr();
        if(ems.isSetMaxValue()) {
            final Double d = Double.valueOf(ems.getMaxValue());
            setValue(BigDecimal.valueOf(d));
            enable(true);
        } else {
            setDefaultValue();
            enable(false);
        }
    }

    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.STR_VC_NUMBER_MAXIMUM;
    }

    @Override
    public void setDefaultValue() {
        setValue(BigDecimal.valueOf(Double.MAX_VALUE));
    }

    @Override
    protected ValEditor<BigDecimal> initValueEditor(final IClientEnvironment environment) {
        final ValNumEditor editor = new ValNumEditor(environment, this);
        editor.setSizePolicy(Policy.Minimum, Policy.Fixed);
        return editor;
    }

    private void onStateChanged() {
        enable(isChecked());
        if(!isChecked()) {
            setDefaultValue();
        }
    }
    
}
