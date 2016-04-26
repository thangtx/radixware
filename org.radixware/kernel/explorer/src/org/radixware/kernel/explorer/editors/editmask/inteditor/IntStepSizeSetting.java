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

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.editors.editmask.controls.AbstractLabeledEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValIntEditor;



final class IntStepSizeSetting extends AbstractLabeledEditor<Long>
                                implements IEditMaskEditorSetting {
    
    public IntStepSizeSetting(final IClientEnvironment environment, final QWidget parent) {
        super(environment, parent, Qt.Orientation.Horizontal);
        final String title = environment.getMessageProvider().translate("EditMask", "Step size:");
        setLabelText(title);
        setDefaultValue();
    }
    
    
    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.INT_STEP_SIZE;
    }

    @Override
    public void addToXml(final org.radixware.schemas.editmask.EditMask editMask) {
        editMask.getInt().setStepSize( getValue().byteValue() );
    }

    @Override
    public void loadFromXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskInt emi = editMask.getInt();
        if(emi.isSetStepSize()) {
            setValue(emi.getStepSize());
        } else {
            setDefaultValue();
        }
    }

    @Override
    public void setDefaultValue() {
        final EditMaskInt em = new EditMaskInt();
        final Long val = em.getStepSize();
        setValue(val);
    }

    @Override
    protected ValIntEditor initValueEditor(final IClientEnvironment environment) {
        final ValIntEditor editor = new ValIntEditor(environment, this);
        final EditMaskInt emi = new EditMaskInt();
        emi.setMinValue(0L);
        editor.setEditMask(emi);
        return editor;
    }

}