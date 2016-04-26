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
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.editors.editmask.controls.AbstractCheckableEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValIntEditor;
import org.radixware.schemas.editmask.EditMask;


final class StrNumberPrecisionSetting extends AbstractCheckableEditor<Long> implements IEditMaskEditorSetting {
    
    public StrNumberPrecisionSetting(final IClientEnvironment environment, final QWidget parent) {
        super(environment, parent);
        final String title = environment.getMessageProvider().translate("EditMask", "Precision:");
        setDefaultValue();
        setText(title);
        enable(false);
        stateChanged.connect((QWidget)this, "onStateChanged()");
    }
    
    @Override
    public void addToXml(final EditMask editMask) {
        if(isChecked()) {
            editMask.getStr().setPrecision(getValue().intValue());
        }
    }

    @Override
    public void loadFromXml(final EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskStr ems = editMask.getStr();
        if(ems.isSetPrecision()) {
            setValue((long)ems.getPrecision());
            enable(true);
        } else {
            setDefaultValue();
            enable(false);
        }
    }

    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.STR_VC_NUMBER_PRECISION;
    }

    @Override
    public void setDefaultValue() {
        setValue(0L);
    }

    @SuppressWarnings("unused")
    private void onStateChanged() {
        final boolean checked = isChecked();
        enable(checked);
        if(!checked) {
            setDefaultValue();
        }
    }

    @Override
    protected ValIntEditor initValueEditor(final IClientEnvironment environment) {
        final ValIntEditor editor = new ValIntEditor(environment, this);
        final EditMaskInt emi = new EditMaskInt();
        emi.setMinValue(0L);
        emi.setMaxValue(Integer.MAX_VALUE);
        editor.setEditMask(emi);
        //editor.setFixedWidth(50);
        return editor;
    }
}
