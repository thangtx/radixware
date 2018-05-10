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

package org.radixware.wps.dialogs;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.client.eas.connections.ConnectionOptions;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ValueFormatter;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.FormBox;
import org.radixware.wps.rwt.ToolButton;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.views.editors.valeditors.ValStrEditorController;


public class ConnectionEditor extends Dialog {

    private final ValStrEditorController nameEditorController;
    private final ValStrEditorController serversEditorController;
    private final ValStrEditorController stationEditorController;
    private final List<String> connectionsNames;
    private final ConnectionOptions options;
    FormBox layout = new FormBox();

    public ConnectionEditor(WpsEnvironment env, final ConnectionOptions options,  List<String> connectionsNames) {
        super(env, "Create new connection dialog");
        this.options = options;
        nameEditorController = new ValStrEditorController(env);
        nameEditorController.setMandatory(true);
        serversEditorController = new ValStrEditorController(env);
        serversEditorController.setMandatory(true);
        stationEditorController = new ValStrEditorController(env);
        this.connectionsNames = connectionsNames;
        addCloseAction(EDialogButtonType.OK).setDefault(true);
        addCloseAction(EDialogButtonType.CANCEL);
        setupUI();
    }

    private void setupUI() {
        add(layout);
        nameEditorController.setValue(options.getName());
        serversEditorController.setValue(options.getInitialAddressesAsStr());
        stationEditorController.setValue(options.getStationName());
        layout.addLabledEditor("Connection name:", (UIObject) nameEditorController.getValEditor());
        layout.addLabledEditor("Servers:", (UIObject) serversEditorController.getValEditor());
        layout.addLabledEditor("Station name:", (UIObject) stationEditorController.getValEditor());
        ToolButton tbEditAddresses = new ToolButton();
        tbEditAddresses.setIcon(super.getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.EDIT));
        tbEditAddresses.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                onEditAddresses();
            }
        });
        serversEditorController.addButton(tbEditAddresses);
    }

    private void onEditAddresses() {
        final String[] arrAddresses = serversEditorController.getValue() == null ? new String[0] : serversEditorController.getValue().split(";");
        ArrStr array = new ArrStr();
        for (String address : arrAddresses) {
            if (!address.trim().isEmpty()) {
                array.add(address.trim());
            }
        }
        final ArrayEditorDialog dialog = new ArrayEditorDialog(super.getEnvironment(), EValType.ARR_STR, ArrStr.class, false, ((WpsEnvironment) super.getEnvironment()).getDialogDisplayer());
        dialog.setMandatory(true);
        dialog.setItemMandatory(true);
        dialog.setWindowTitle(getEnvironment().getMessageProvider().translate("ConnectionEditor", "Servers List"));
        dialog.setCurrentValue(array);
        if (dialog.execDialog() == IDialog.DialogResult.ACCEPTED) {
            array = (ArrStr) dialog.getCurrentValue();
            final StringBuilder addressDataBuilder = new StringBuilder();
            if (array != null) {
                for (String address : array) {
                    if (!address.isEmpty()) {
                        if (addressDataBuilder.length() != 0) {
                            addressDataBuilder.append("; ");
                        }
                        addressDataBuilder.append(address);
                    }
                }
                serversEditorController.setValue(addressDataBuilder.toString());
            }
        }
    }

    @Override
    protected IDialog.DialogResult onClose(String action, IDialog.DialogResult actionResult) {
        if (actionResult == IDialog.DialogResult.ACCEPTED) {
            MessageProvider msgProvider = getEnvironment().getMessageProvider();
            String errorMessageTitle = msgProvider.translate("ExplorerDialog", "Value must be defined");
            if (nameEditorController.getValue() == null || nameEditorController.getValue().isEmpty()) {
                String errorMessage = getEnvironment().getMessageProvider().translate("ExplorerDialog", "Please enter connection name");
                getEnvironment().messageError(errorMessageTitle, errorMessage);
            } else if (connectionsNames.contains(nameEditorController.getValue())) {
                String errorMessage = getEnvironment().getMessageProvider().translate("ExplorerDialog", "Connection name must be unique");
                getEnvironment().messageError(errorMessageTitle, errorMessage);
            } else if (serversEditorController.getValue() == null || serversEditorController.getValue().isEmpty()) {
                String errorMessage = getEnvironment().getMessageProvider().translate("ExplorerDialog", "Please fill in servers");
                getEnvironment().messageError(errorMessageTitle, errorMessage);
            } else {
                final List<InetSocketAddress> addresses = new ArrayList<>();
                final String[] arrAddresses = serversEditorController.getValue().split(";");
                boolean isAddressValid = true;
                for (String address : arrAddresses) {
                    if (!address.trim().isEmpty()) {
                        try {
                            addresses.add(ValueFormatter.parseInetSocketAddress(address.trim()));
                        } catch (IllegalArgumentException ex) {
                            if (arrAddresses.length == 1) {
                                getEnvironment().messageError(msgProvider.translate("ConnectionEditor", "Server Address Is Invalid!"),
                                        msgProvider.translate("ConnectionEditor", "Please enter valid server address"));
                                isAddressValid = false;
                            } else {
                                final String message = msgProvider.translate("ConnectionEditor", "Server Address '%s' Is Invalid!\nDo you want to continue?");
                                final String title = msgProvider.translate("ConnectionEditor", "Confirm to Save Options");
                                if (!getEnvironment().messageConfirmation(title, String.format(message, address))) {
                                    isAddressValid = false;
                                }
                            }
                        }
                    }
                }
                if (addresses.isEmpty()) {
                    getEnvironment().messageError(msgProvider.translate("ConnectionEditor", "No Valid Addresses Was Specified"),
                            msgProvider.translate("ConnectionEditor", "Please enter valid server address"));
                    isAddressValid = false;
                }
                if (isAddressValid) {
                    options.setName(nameEditorController.getValue());
                    options.setInitialAddressesAsStr(serversEditorController.getValue());
                    options.setStationName(stationEditorController.getValue());
                    return super.onClose(action, actionResult);
                }
            }
            return null;
        } else {
            return super.onClose(action, actionResult);
        }
    }
}
