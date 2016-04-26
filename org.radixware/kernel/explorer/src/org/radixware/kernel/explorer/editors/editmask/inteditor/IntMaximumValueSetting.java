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



final class IntMaximumValueSetting extends AbstractComplexCheckableSetting
                                    implements IEditMaskEditorSetting  {
    private final ValIntEditor valueEditor;
    
    public IntMaximumValueSetting(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
        final String title = environment.getMessageProvider().translate("EditMask", "Maximum value:");
        setText(title);
        valueEditor = new ValIntEditor(environment, this);
        setDefaultValue();
        setFocusProxy(valueEditor);
        addWidget(valueEditor, getOption());
        enable(false);
    }
    
    
    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.INT_MAX_VALUE;
    }

    @Override
    public void addToXml(final org.radixware.schemas.editmask.EditMask editMask) {
        if(isChecked()) {
            editMask.getInt().setMaxValue(valueEditor.getValue());
        }
    }

    @Override
    public void loadFromXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskInt emi = editMask.getInt();
        if(emi.isSetMaxValue()) {
            valueEditor.setValue(emi.getMaxValue());
            enable(true);
        } else {
            setDefaultValue();
            enable(false);
        }
    }

    @Override
    public void setDefaultValue() {
        final EditMaskInt em = new EditMaskInt();
        final long val = em.getMaxValue();
        valueEditor.setValue(val);
    }

    @Override
    public Long getValue() {
        return valueEditor.getValue();
    }

    @Override
    protected void onCheckBoxStateChanged() {
        final boolean checked = isChecked();
        valueEditor.setEnabled(checked);
        if(!checked) {
           setDefaultValue();
        }
    }

}
