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

import com.trolltech.qt.core.Qt.Orientation;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.editors.editmask.controls.AbstractLabeledEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;



final class IntPadCharacterSetting extends AbstractLabeledEditor<String>
                             implements IEditMaskEditorSetting  {
    
    public IntPadCharacterSetting(final IClientEnvironment environment, final QWidget parent) {
        super(environment, parent, Orientation.Horizontal);
        final String title = environment.getMessageProvider().translate("EditMask", "Pad character:");
        setLabelText(title);
        setDefaultValue();
    }
    
    
    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.INT_PAD_CHARACTER;
    }

    @Override
    public void addToXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final String value = getValue();
        if(value != null && !value.isEmpty()) {
            editMask.getInt().setPadChar(value);
        }
    }

    @Override
    public void loadFromXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskInt emi = editMask.getInt();
        if(emi.isSetPadChar()) {
            setValue(editMask.getInt().getPadChar());
        }
        else {
            setDefaultValue();
        }
    }

    @Override
    public void setDefaultValue() {
        final EditMaskInt em = new EditMaskInt();
        String val = String.valueOf(em.getPadChar());
        if(val.equals("0") || val.isEmpty()) {
            val = "";
        }
        setValue(val);
    }
    
    @Override
    protected ValStrEditor initValueEditor(final IClientEnvironment environment) {
        return new ValStrEditor(environment, this);
    }

}
