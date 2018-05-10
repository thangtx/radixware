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

import java.util.List;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.FormBox;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.views.editors.valeditors.ValListEditorController;
import org.radixware.wps.views.editors.valeditors.ValStrEditorController;

public class CreateUserDialog extends Dialog {

    private final MessageProvider mp;
    private final ValStrEditorController nameEditorController;
    private final ValListEditorController<String> connectionEditorController;
    private final List<String> userNames;
    private final List<String> connectionNames;
    FormBox layout = new FormBox();

    public CreateUserDialog(WpsEnvironment env, List<String> connectionNames, List<String> userNames) {
        super(env, env.getMessageProvider().translate("AdminPanel", "Create new user connection"));
        mp = env.getMessageProvider();
        nameEditorController = new ValStrEditorController(env);
        connectionEditorController = new ValListEditorController<>(env);
        this.userNames = userNames;
        this.connectionNames = connectionNames;
        addCloseAction(EDialogButtonType.OK).setDefault(true);
        addCloseAction(EDialogButtonType.CANCEL);
        setupUI();
    }

    private void setupUI() {
        add(layout);
        nameEditorController.setMandatory(true);
        connectionEditorController.setMandatory(true);
        layout.addLabledEditor(mp.translate("AdminPanel", "User name:"), (UIObject) nameEditorController.getValEditor());
        EditMaskList connectionsEditMask = new EditMaskList();
        for (String connectionName : connectionNames) {
            connectionsEditMask.addItem(connectionName, connectionName);
        }
        connectionEditorController.setEditMask(connectionsEditMask);
        layout.addLabledEditor(mp.translate("AdminPanel", "Connection:"), (UIObject) connectionEditorController.getValEditor());
    }

    public String getUserName() {
        return nameEditorController.getValue();
    }

    public String getConnection() {
        return connectionEditorController.getValue();
    }

    @Override
    protected DialogResult onClose(String action, DialogResult actionResult) {
        if (actionResult == DialogResult.ACCEPTED) {
            String errorMessageTitle = mp.translate("ExplorerDialog", "Value must be defined");
            if (nameEditorController.getValue() == null || nameEditorController.getValue().isEmpty()) {
                String errorMessage = mp.translate("ExplorerDialog", "Please enter connection name");
                getEnvironment().messageError(errorMessageTitle, errorMessage);
            } else if (userNames.contains(nameEditorController.getValue())) {
                String errorMessage = mp.translate("ExplorerDialog", "User list already contains user with name %s. Please choose another.");
                getEnvironment().messageError(errorMessageTitle, String.format(errorMessage, nameEditorController.getValue()));
            } else if (connectionEditorController.getValue() == null) {
                String errorMessage = mp.translate("ExplorerDialog", "Please choose connection");
                getEnvironment().messageError(errorMessageTitle, errorMessage);
            } else {
                return super.onClose(action, actionResult);
            }
            return null;
        } else {
            return super.onClose(action, actionResult);
        } 
    }

}
