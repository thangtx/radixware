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

import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.enums.EFileDialogOpenMode;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.schemas.editmask.EditMask;


public final class FilePathDialogOpenModeSetting extends QWidget implements IEditMaskEditorSetting{

    private final QComboBox cbSelectionMode;
    
    public FilePathDialogOpenModeSetting(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
        final MessageProvider msgProvider = environment.getMessageProvider();
        
        cbSelectionMode = new QComboBox(this);
        String itemText = msgProvider.translate("EditMask", "Load");
        cbSelectionMode.addItem(itemText, EFileDialogOpenMode.LOAD);
        itemText = msgProvider.translate("EditMask", "Save");
        cbSelectionMode.addItem(itemText, EFileDialogOpenMode.SAVE);

        final QHBoxLayout layout = new QHBoxLayout();
        layout.addWidget(cbSelectionMode);
        cbSelectionMode.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Fixed);
        this.setLayout(layout);
        setDefaultValue();
    }    
    
    
    @Override
    public void addToXml(EditMask editMask) {
        editMask.getFilePath().setFileDialogMode(
                (EFileDialogOpenMode) cbSelectionMode.itemData(cbSelectionMode.currentIndex())
        );
    }

    @Override
    public void loadFromXml(EditMask editMask) {
         final org.radixware.schemas.editmask.EditMaskFilePath ems = editMask.getFilePath();
        if (ems.isSetFileDialogMode()) {
            final int index = cbSelectionMode.findData(ems.getFileDialogMode());
            if (index >= 0) {
                cbSelectionMode.setCurrentIndex(index);
            } else {
                setDefaultValue();
            }
        }
    }

    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.FILEPATH_FILE_DIALOG_OPEN_MODE;
    }

    @Override
    public void setDefaultValue() {
        cbSelectionMode.setCurrentIndex(0);
    }

    @Override
    public Object getValue() {
        return cbSelectionMode.itemData(cbSelectionMode.currentIndex());
    }
}