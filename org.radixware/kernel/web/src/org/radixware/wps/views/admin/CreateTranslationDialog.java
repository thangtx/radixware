/*
 * Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */

package org.radixware.wps.views.admin;

import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.utils.ValueFormatter;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.FormBox;
import org.radixware.wps.rwt.Grid;
import org.radixware.wps.rwt.Grid.Cell;
import org.radixware.wps.rwt.Grid.Row;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.views.editors.valeditors.ValStrEditorController;

public class CreateTranslationDialog extends Dialog{
    private String name;
    private String originalInetAddress;
    private String newInetAddress;
    private final ValStrEditorController nameEditorController;
    private final ValStrEditorController originalInetAddressEditorController;
    private final ValStrEditorController newInetAddressEditorController;
    private final Grid table;
    private final MessageProvider mp;
    public CreateTranslationDialog(Grid table) {
        super();
        this.table = table;
        mp = getEnvironment().getMessageProvider();
        setWindowTitle(mp.translate("AdminPanel", "Create Translation"));
        nameEditorController = new ValStrEditorController(getEnvironment());
        originalInetAddressEditorController = new ValStrEditorController(getEnvironment());
        newInetAddressEditorController = new ValStrEditorController(getEnvironment());
        setupUI();
    }
    
    private void setupUI() {
        FormBox formBox = new FormBox();
        formBox.addLabledEditor(mp.translate("AdminPanel", "Name:"), (UIObject) nameEditorController.getValEditor());
        formBox.addLabledEditor(mp.translate("AdminPanel", "Original address:"), (UIObject) originalInetAddressEditorController.getValEditor());
        originalInetAddressEditorController.setMandatory(true);
        formBox.addLabledEditor(mp.translate("AdminPanel", "New address:"), (UIObject) newInetAddressEditorController.getValEditor());
        add(formBox);
        addCloseAction(EDialogButtonType.OK, DialogResult.ACCEPTED);
        addCloseAction(EDialogButtonType.CANCEL, DialogResult.REJECTED);
    }
      
    public String getName() {
        return name;
    }

    public String getOriginalInetAddress() {
        return originalInetAddress;
    }

    public String getNewInetAddress() {
        return newInetAddress;
    }

    @Override
    protected DialogResult onClose(String action, DialogResult actionResult) { 
        if (actionResult == DialogResult.ACCEPTED) {
            if (originalInetAddressEditorController.getValue() == null) {
                getEnvironment().messageWarning(mp.translate("AdminPanel", "Invalid value"), mp.translate("AdminPanel", "Original address must be set"));
                return null;
            } else {
                for (int i = 0; i < table.getRowCount(); i++) {
                    Row row = table.getRow(i);
                    Cell nameCell = row.getCell(0);
                    if ((nameCell.getValue() == null && nameEditorController.getValue() == null || nameCell.getValue() != null && 
                            nameCell.getValue().equals(nameEditorController.getValue())) && 
                            originalInetAddressEditorController.getValue().equals(row.getCell(1).getValue())) {
                        getEnvironment().messageWarning(mp.translate("AdminPanel", "Invalid value"), mp.translate("AdminPanel", "Name and original address pair must be unique"));
                        return null;
                    }
                }
            }
            name = nameEditorController.getValue();
            try {
                ValueFormatter.parseInetSocketAddress(originalInetAddressEditorController.getValue());
                originalInetAddress = originalInetAddressEditorController.getValue();
            } catch (IllegalArgumentException ex) {
                 getEnvironment().messageException(mp.translate("AdminPanel", "Invalid value"), mp.translate("AdminPanel", "Invalid original address: ") + originalInetAddressEditorController.getValue(), ex);
                 return null;
            }
            
            try {
                ValueFormatter.parseInetSocketAddress(newInetAddressEditorController.getValue());
                newInetAddress = newInetAddressEditorController.getValue();
            } catch (IllegalArgumentException ex) {
                 getEnvironment().messageException(mp.translate("AdminPanel", "Invalid value"), mp.translate("AdminPanel", "Invalid new address: ") + newInetAddressEditorController.getValue(), ex);
                 return null;
            }
        }
        return super.onClose(action, actionResult); 
    }
    
}
