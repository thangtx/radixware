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
import org.radixware.kernel.common.auth.PasswordRequirements;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.auth.ClientAuthUtils;
import org.radixware.kernel.common.client.dialogs.IChangePasswordDialog;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.html.Div;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.AbstractTextField.TextChangeListener;
import org.radixware.wps.rwt.GridLayout.Cell;
import org.radixware.wps.rwt.GridLayout.Row;
import org.radixware.wps.rwt.*;



public class ChangePasswordDialog extends Dialog implements IChangePasswordDialog {

    private boolean checkPasswordRequirements = true;
    private Label lblTitle = new Label();
    private Label lblMessage = new Label();
    private PasswordRequirements requirements;
    private TextField edOldPassword = new TextField("");
    private TextField edNewPassword = new TextField("");
    private TextField edConfirmation = new TextField("");
    private AbstractContainer panel = new AbstractContainer(new Div());
    private GridLayout layout = new GridLayout();
    private Label lblOldPwd = new Label();
    private Label lblNewPwd = new Label();
    private Label lblConf = new Label();

    public ChangePasswordDialog(final WpsEnvironment environment) {
        super(environment.getDialogDisplayer(), null);

        setupUi(environment);
        refresh();

    }

    @Override
    public void setPasswordRequirements(final PasswordRequirements requirements) {
        this.requirements = requirements;
    }
        

    private void setupUi(IClientEnvironment env) {
        lblOldPwd.setText(env.getMessageProvider().translate("ChangePasswordDialog", "Current password:"));
        lblNewPwd.setText(env.getMessageProvider().translate("ChangePasswordDialog", "New password:"));
        lblConf.setText(env.getMessageProvider().translate("ChangePasswordDialog", "Confirmation:"));
        setWindowIcon(env.getApplication().getImageManager().getIcon(ClientIcon.Connection.KEY));
        setWindowTitle(env.getMessageProvider().translate("ChangePasswordDialog", "Change Password"));
        addCloseAction(EDialogButtonType.OK).setDefault(true);        
        
        
        addCloseAction(EDialogButtonType.CANCEL);        

        final TextChangeListener listener = new TextChangeListener() {

            @Override
            public void textChanged(String oldText, String newText) {
                refresh();
            }
        };
        panel.getHtml().setCss("overflow", "auto");
        edOldPassword.addTextListener(listener);
        edNewPassword.addTextListener(listener);
        edConfirmation.addTextListener(listener);
        edOldPassword.setPassword(true);
        edNewPassword.setPassword(true);
        edConfirmation.setPassword(true);
        add(lblTitle);        
        lblTitle.setTop(3);
        lblTitle.getAnchors().setTop(new Anchors.Anchor(0, 3));
        lblTitle.setLeft(1);
        lblTitle.getAnchors().setRight(new Anchors.Anchor(1, -1));
        lblTitle.setAlign(Alignment.CENTER);
        lblTitle.getHtml().setCss("display", "block");
        lblTitle.getHtml().setCss("font-weight", "bold");
        lblTitle.getHtml().setCss("font-size", "12px");        
        add(lblMessage);
        lblMessage.setVisible(false);      
        lblMessage.setAlign(Alignment.CENTER);
        lblTitle.getHtml().setCss("display", "block");        
        lblMessage.getAnchors().setTop(new Anchors.Anchor(1, 5, lblTitle));
        lblMessage.setLeft(1);
        lblMessage.getAnchors().setRight(new Anchors.Anchor(1, -1));
        add(panel);
        panel.getAnchors().setRight(new Anchors.Anchor(1, -1));
        panel.getAnchors().setTop(new Anchors.Anchor(1, 5, lblTitle));
        panel.getAnchors().setBottom(new Anchors.Anchor(1, -1));
        panel.setLeft(1);
        panel.add(layout);
        Row row = new Row();
        Cell cell = new Cell();
        row.add(cell);
        Label label = lblOldPwd;
        label.setTextWrapDisabled(true);
        cell.add(label);
        cell.setFitWidth(true);
        cell = new Cell();
        cell.add(edOldPassword);
        cell.setVCoverage(100);
        row.add(cell);
        layout.add(row);
        row = new Row();
        cell = new Cell();
        cell.setFitWidth(true);
        label = lblNewPwd;
        cell.add(label);
        row.add(cell);
        label.setTextWrapDisabled(true);
        cell = new Cell();
        cell.add(edNewPassword);
        cell.setVCoverage(100);
        row.add(cell);
        layout.add(row);
        row = new Row();
        cell = new Cell();
        cell.setFitWidth(true);
        label = lblConf;
        label.setTextWrapDisabled(true);
        cell.add(label);
        row.add(cell);
        cell = new Cell();
        cell.add(edConfirmation);
        cell.setVCoverage(100);
        row.add(cell);
        layout.add(row);
        edOldPassword.setFocused(true);
        setWidth(370);
        setHeight(160);
    }

    @Override
    public void setTitle(final String title) {        
        lblTitle.setText(title);        
    }

    @Override
    public void setMessage(final String message) {
        lblMessage.setText(message);
        if (message == null || message.isEmpty()) {
            lblMessage.setVisible(false);
            panel.getAnchors().setTop(new Anchors.Anchor(1, 5, lblTitle));
            this.setHeight(160);
        } else {
            lblMessage.setVisible(true);
            panel.getAnchors().setTop(new Anchors.Anchor(1, 5, lblMessage));
            this.setHeight(180);
        }
    }

    public void setCheckPasswordRequirements(final boolean doCheck) {
        checkPasswordRequirements = doCheck;
    }

    @Override
    public String getOldPassword() {
        return edOldPassword.getText();
    }

    @Override
    public String getNewPassword() {
        return edNewPassword.getText();
    }

    @Override
    protected DialogResult onClose(String action, DialogResult actionResult) {
        if (actionResult == DialogResult.ACCEPTED) {
            if (!canChange()) {
                refresh();
                return null;
            }
            final String message;
            final String title = getEnvironment().getMessageProvider().translate("ChangePasswordDialog", "Can`t Change Password");
            if (!edNewPassword.getText().equals(edConfirmation.getText())) {
                message = getEnvironment().getMessageProvider().translate("ChangePasswordDialog", "The new password and confirmation password do not match");
            } else if (checkPasswordRequirements) {
                final PasswordRequirements requirements = getPasswordRequirements(title);
                if (requirements==null){
                    return null;
                }
                message = ClientAuthUtils.checkPassword(edNewPassword.getText(), requirements, getEnvironment());
            } else {
                message = null;
            }
            if (message != null) {
                getEnvironment().messageInformation(title, message);
                edNewPassword.setText("");
                edConfirmation.setText("");
                refresh();
                edNewPassword.setFocused(true);
                return null;
            } else {
                return actionResult;
            }
        } else {
            return actionResult;
        }
    }
    
    private PasswordRequirements getPasswordRequirements(final String title){
        if (requirements==null){
            try {
                return  getEnvironment().getEasSession().getPasswordRequirements();
            } catch (ServiceClientException ex) {
                getEnvironment().messageException(title, ClientException.getExceptionReason(getEnvironment().getMessageProvider(), ex), ex);
                return null;
            } catch (InterruptedException ex) {
                return null;
            }            
        }
        return requirements;
    }    

    @Override
    public void clear() {
        edOldPassword.setText("");
        edNewPassword.setText("");
        edConfirmation.setText("");
        refresh();
    }

    @SuppressWarnings("unused")
    private void refresh() {

        if (edOldPassword.getText() == null || edOldPassword.getText().isEmpty()) {
            lblOldPwd.setForeground(Color.red);
        } else {
            lblOldPwd.setForeground(Color.black);
        }
        if (edNewPassword.getText() == null || edNewPassword.getText().isEmpty()) {
            lblNewPwd.setForeground(Color.red);
        } else {
            lblNewPwd.setForeground(Color.BLACK);
        }
        if (edConfirmation.getText() == null || edConfirmation.getText().isEmpty()) {
            lblConf.setForeground(Color.red);
        } else {
            lblConf.setForeground(Color.black);
        }
    }

    private boolean canChange() {
        return !(edOldPassword.getText() == null || edOldPassword.getText().isEmpty())
                && !(edNewPassword.getText() == null || edNewPassword.getText().isEmpty())
                && !(edConfirmation.getText() == null || edConfirmation.getText().isEmpty());
    }
}
