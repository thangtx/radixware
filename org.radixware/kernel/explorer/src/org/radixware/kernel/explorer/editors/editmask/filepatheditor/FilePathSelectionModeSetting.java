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
import org.radixware.kernel.common.enums.EFileSelectionMode;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.schemas.editmask.EditMask;

final class FilePathSelectionModeSetting extends QWidget implements IEditMaskEditorSetting {

    private final Signal1<EFileSelectionMode> itemChanged = new Signal1<>();
    private final QComboBox cbSelectionMode;

    public FilePathSelectionModeSetting(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
        final MessageProvider msgProvider = environment.getMessageProvider();

        cbSelectionMode = new QComboBox(this);
        String itemText = msgProvider.translate("EditMask", "Select file");
        cbSelectionMode.addItem(itemText, EFileSelectionMode.SELECT_FILE);
        itemText = msgProvider.translate("EditMask", "Select directory");
        cbSelectionMode.addItem(itemText, EFileSelectionMode.SELECT_DIRECTORY);

        cbSelectionMode.currentIndexChanged.connect((QWidget) this, "onItemChanged()");
        final QHBoxLayout layout = new QHBoxLayout();
        layout.addWidget(cbSelectionMode);
        cbSelectionMode.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Fixed);
        this.setLayout(layout);
        setDefaultValue();
    }

    @Override
    public void addToXml(EditMask editMask) {
        editMask.getFilePath().setSelectionMode(
                (EFileSelectionMode) cbSelectionMode.itemData(cbSelectionMode.currentIndex())
        );
    }

    @Override
    public void loadFromXml(EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskFilePath ems = editMask.getFilePath();
        if (ems.isSetSelectionMode()) {
            final int index = cbSelectionMode.findData(ems.getSelectionMode());
            if (index >= 0) {
                cbSelectionMode.setCurrentIndex(index);
            } else {
                setDefaultValue();
            }
        }
    }

    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.FILEPATH_SELECTION_MODE;
    }

    @Override
    public void setDefaultValue() {
        cbSelectionMode.setCurrentIndex(0);
    }

    @Override
    public Object getValue() {
        return cbSelectionMode.itemData(cbSelectionMode.currentIndex());
    }

    @SuppressWarnings("unused")
    private void onItemChanged() {
        final int index = cbSelectionMode.currentIndex();
        final EFileSelectionMode currentSelection = (EFileSelectionMode) cbSelectionMode.itemData(index);
        itemChanged.emit(currentSelection);
    }

    public Signal1<EFileSelectionMode> itemChanged() {
        return itemChanged;
    }
}
