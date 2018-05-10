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
package org.radixware.kernel.explorer.editors.editmask.filepatheditor;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.meta.mask.EditMaskFilePath;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.editors.editmask.controls.AbstractLabeledEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.schemas.editmask.EditMask;

public final class FilePathDialogTitleSetting extends AbstractLabeledEditor<String>
        implements IEditMaskEditorSetting {

    public FilePathDialogTitleSetting(final IClientEnvironment environment, final QWidget parent) {
        super(environment, parent, Qt.Orientation.Horizontal);
        final String title = environment.getMessageProvider().translate("EditMask", "File dialog title:");
        setLabelText(title);
        setDefaultValue();
    }

    @Override
    protected ValEditor<String> initValueEditor(IClientEnvironment environment) {
        final EditMaskStr emi = new EditMaskStr();
        final ValStrEditor editor = new ValStrEditor(environment, this);
        editor.setEditMask(emi);
        return editor;
    }

    @Override
    public void addToXml(EditMask editMask) {
        editMask.getFilePath().setFileDialogTitle(getValue());
    }

    @Override
    public void loadFromXml(EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskFilePath emi = editMask.getFilePath();
        if (emi.isSetFileDialogTitle()) {
            setValue(emi.getFileDialogTitle());
        } else {
            setDefaultValue();
        }
    }

    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.FILEPATH_FILE_DIALOG_TITLE;
    }

    @Override
    public void setDefaultValue() {
        final EditMaskFilePath em = new EditMaskFilePath();
        final String val = em.getFileDialogTitle(Application.getInstance().getDefManager()); //passing def manager shouldn't be null!!! error
        setValue(val);
    }

}
