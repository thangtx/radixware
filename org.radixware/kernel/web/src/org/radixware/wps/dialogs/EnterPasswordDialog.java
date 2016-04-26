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

package org.radixware.wps.dialogs;

import java.awt.Color;
import org.radixware.kernel.common.client.dialogs.IEnterPasswordDialog;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.AbstractTextField.TextChangeListener;
import org.radixware.wps.rwt.*;
import org.radixware.wps.rwt.InputBox.InvalidStringValueException;


public class EnterPasswordDialog extends Dialog implements IEnterPasswordDialog {

    private final InputBox<String> lePassword = new InputBox<>(new InputBox.ValueController<String>() {
        @Override
        public String getValue(String text) throws InvalidStringValueException {
            return text;
        }
    });
    private final Label lbMessage;    
    private final VerticalBox content = new VerticalBox();

    public EnterPasswordDialog(final WpsEnvironment environment) {
        super(environment, null, false);
        lbMessage = new Label(environment.getMessageProvider().translate("ExplorerDialog", "Please enter your password or press cancel to disconnect"));
        setupUi();

    }

    private void setupUi() {
        html.setAttr("dlgId", "enter-password");        
        setWindowTitle(getEnvironment().getMessageProvider().translate("ExplorerDialog", "Enter Password"));
        setWindowIcon(getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.Connection.KEY));
        lbMessage.setTextWrapDisabled(true);
        lbMessage.setAlign(Alignment.CENTER);
        content.add(lbMessage);
        content.addSpace();        
        lePassword.setPassword(true);
        lePassword.setFocused(true);
        lePassword.setValue("");
        lePassword.setLabelVisible(true);
        lePassword.setLabel(getEnvironment().getMessageProvider().translate("ExplorerDialog", "Password:"));
        content.add(lePassword);
        content.setSizePolicy(SizePolicy.MINIMUM_EXPAND, SizePolicy.MINIMUM_EXPAND);        
        add(content);
        content.setTop(3);
        content.setLeft(3);
        content.getAnchors().setRight(new Anchors.Anchor(1, -3));
        content.getAnchors().setBottom(new Anchors.Anchor(1, -3));        
        setAutoWidth(true);
        setAutoHeight(true);
        setResizable(false);
        addCloseAction(EDialogButtonType.OK).setDefault(true);
        addCloseAction(EDialogButtonType.CANCEL);
        lePassword.addValueChangeListener(new InputBox.ValueChangeListener<String>() {
            @Override
            public void onValueChanged(String oldValue, String newValue) {
                refresh();
            }
        });
    }

    @Override
    public void setMessage(final String message) {
        lbMessage.setText(message);
    }

    @Override
    public String getPassword() {
        return lePassword.getValue();
    }

    @SuppressWarnings("unused")
    private void refresh() {
        if (lePassword.getValue() == null || lePassword.getValue().isEmpty()) {
            lePassword.setLabelColor(Color.red);
        } else {
            lePassword.setLabelColor(Color.black);
        }
    }

    @Override
    protected DialogResult onClose(String action, DialogResult actionResult) {
        if (actionResult == DialogResult.ACCEPTED) {
            if (lePassword.getValue() == null || lePassword.getValue().isEmpty()) {
                refresh();
                return null;
            }
        }
        return actionResult;
    }
}
