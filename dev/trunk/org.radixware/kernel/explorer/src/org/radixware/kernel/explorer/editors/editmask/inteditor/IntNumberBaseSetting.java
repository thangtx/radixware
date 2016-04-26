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


final class IntNumberBaseSetting extends AbstractLabeledEditor<Long>
                             implements IEditMaskEditorSetting  {
        
    public IntNumberBaseSetting(final IClientEnvironment environment, final QWidget parent) {
        super(environment, parent, Qt.Orientation.Horizontal);
        final String title = environment.getMessageProvider().translate("EditMask", "Number base:");
        setLabelText(title);
        setDefaultValue();
    }
    
    
    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.INT_NUMBER_BASE;
    }

    @Override
    public void addToXml(final org.radixware.schemas.editmask.EditMask editMask) {
        
        editMask.getInt().setNumberBase(getValue().byteValue());
    }

    @Override
    public void loadFromXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskInt emi = editMask.getInt();
        if(emi.isSetNumberBase()) {
            setValue(Long.valueOf(emi.getNumberBase()));
        } else {
            setDefaultValue();
        }
        
    }

    @Override
    public void setDefaultValue() {
        final EditMaskInt em = new EditMaskInt();
        final byte val = em.getNumberBase();
        setValue((long)val);
    }
    
    @Override
    protected ValIntEditor initValueEditor(final IClientEnvironment environment) {
        final EditMaskInt emi = new EditMaskInt();
        emi.setMaxValue(Character.MAX_RADIX);
        emi.setMinValue(Character.MIN_RADIX);
        final ValIntEditor editor = new ValIntEditor(environment, this);
        editor.setEditMask(emi);
        return editor;
    }
}
