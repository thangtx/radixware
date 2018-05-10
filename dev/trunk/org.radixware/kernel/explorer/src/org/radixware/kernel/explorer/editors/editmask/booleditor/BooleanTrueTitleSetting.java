/*
 * Copyright (coffee) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.explorer.editors.editmask.booleditor;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.meta.mask.EditMaskBool;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.editors.editmask.controls.AbstractLabeledEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.schemas.editmask.EditMask;

final class BooleanTrueTitleSetting extends AbstractLabeledEditor<String>
        implements IEditMaskEditorSetting {

    public BooleanTrueTitleSetting(final IClientEnvironment environment, final QWidget parent) {
        super(environment, parent, Qt.Orientation.Horizontal);
        final String title = environment.getMessageProvider().translate("EditMask", "Title:");
        setLabelText(title);
        setDefaultValue();
    }
    
    @Override
    public void addToXml(EditMask editMask) {
        editMask.getBoolean().setTrueTitle(getValue());
    }

    @Override
    public void loadFromXml(EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskBool emi = editMask.getBoolean();
        if (emi.isSetTrueTitle()) {
            setValue(emi.getTrueTitle());
        } else {
            setDefaultValue();
        }
    }

    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.BOOLEAN_TRUE_TITLE;
    }

    @Override
    public void setDefaultValue() {
        final EditMaskBool em = new EditMaskBool();
        final String val = em.getTrueTitle(Application.getInstance().getDefManager());
        setValue(val);
    }

    @Override
    protected ValEditor<String> initValueEditor(IClientEnvironment environment) {
        final EditMaskStr emi = new EditMaskStr();
        final ValStrEditor editor = new ValStrEditor(environment, this);
        editor.setEditMask(emi);
        return editor;
    }

}
