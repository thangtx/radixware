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

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.editors.editmask.controls.AbstractLabeledEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValIntEditor;


final class NumScaleSetting extends AbstractLabeledEditor<Long> implements IEditMaskEditorSetting {
    
    public NumScaleSetting(final IClientEnvironment environment, final QWidget parent) {
        super(environment, parent, Qt.Orientation.Horizontal);
        final String labelText = environment.getMessageProvider().translate("EditMask", "Scale:");
        setLabelText(labelText);
        setDefaultValue();
    }
    
    @Override
    protected ValIntEditor initValueEditor(final IClientEnvironment environment) {
        final ValIntEditor editor = new ValIntEditor(environment, this);
        final EditMaskInt editMask = new EditMaskInt();
        editMask.setMinValue(Integer.MIN_VALUE);
        editMask.setMaxValue(Integer.MAX_VALUE);
        editor.setEditMask(editMask);
        return editor;
    }

    @Override
    public void addToXml(final org.radixware.schemas.editmask.EditMask editMask) {
        editMask.getNum().setScale(getValue());
    }

    @Override
    public void loadFromXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskNum editMaskNum = editMask.getNum();
        if(editMaskNum.isSetScale()) {
            setValue(editMaskNum.getScale());
        } else {
            setDefaultValue();
        }
    }

    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.NUM_SCALE;
    }

    @Override
    public void setDefaultValue() {
        setValue(0L);
    }
    
}
