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

package org.radixware.kernel.explorer.dialogs;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QLayout.SizeConstraint;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.common.auth.PasswordRequirements;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.auth.ClientAuthUtils;
import org.radixware.kernel.common.client.dialogs.IChangePasswordDialog;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.enums.EDialogButtonType;


public class ChangePasswordDialog extends ExplorerDialog implements IChangePasswordDialog{

    private final Ui_ChangePasswordDialog uiCreator = new Ui_ChangePasswordDialog();
    private boolean checkPasswordRequirements = true;
    private PasswordRequirements requirements;
    private String oldPassword;
    private String newPassword;

    public ChangePasswordDialog(final IClientEnvironment environment, final QWidget parent) {
        super(environment, parent, null);
        setupUi();
        refresh();
    }
    
    private void setupUi() {
        uiCreator.setupUi(this);
        setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose, true);
        setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.Connection.KEY));
        dialogLayout().setSpacing(9);
        dialogLayout().setContentsMargins(6, 9, 6, 9);
        dialogLayout().addWidget(uiCreator.widgetMessage);
        dialogLayout().addWidget(uiCreator.frameContent);
        addButtons(EnumSet.of(EDialogButtonType.OK,EDialogButtonType.CANCEL),true);
        dialogLayout().setSizeConstraint(SizeConstraint.SetFixedSize);

        uiCreator.leOldPassword.textEdited.connect(this, "refresh()");
        uiCreator.leNewPassword.textEdited.connect(this, "refresh()");
        uiCreator.leConfirmation.textEdited.connect(this, "refresh()");
    }

    @Override
    public void setTitle(final String title) {
        uiCreator.lbTitle.setText(title);
    }

    @Override
    public void setMessage(final String message) {
        uiCreator.lbMessage.setText(message);
    }

    public void setCheckPasswordRequirements(final boolean doCheck) {
        checkPasswordRequirements = doCheck;
    }

    @Override
    public void setPasswordRequirements(final PasswordRequirements requirements) {
        this.requirements = requirements;
    }        

    @Override
    public String getOldPassword() {
        return oldPassword;
    }

    @Override
    public String getNewPassword() {
        return newPassword;
    }

    @Override
    public int exec() {
        clear();
        if (uiCreator.lbMessage.text() == null || uiCreator.lbMessage.text().isEmpty()) {
            uiCreator.lbMessage.setVisible(false);
        }
        uiCreator.leOldPassword.setFocus();
        return super.exec();
    }

    @Override
    public void accept() {
        final String message;
        final String title = getEnvironment().getMessageProvider().translate("ChangePasswordDialog", "Can`t Change Password");
        if (!uiCreator.leNewPassword.text().equals(uiCreator.leConfirmation.text())) {
            message = getEnvironment().getMessageProvider().translate("ChangePasswordDialog", "The new password and confirmation password do not match");
        } else if (checkPasswordRequirements) {
            final PasswordRequirements requirements = getPasswordRequirements(title);
            if (requirements==null){
                return;
            }
            message = ClientAuthUtils.checkPassword(uiCreator.leNewPassword.text(), requirements, getEnvironment());
        } else {
            message = null;
        }
        if (message != null) {
            Application.messageInformation(title, message);
            uiCreator.leNewPassword.clear();
            uiCreator.leConfirmation.clear();
            refresh();
            uiCreator.leNewPassword.setFocus();
        } else {
            oldPassword = uiCreator.leOldPassword.text();
            newPassword = uiCreator.leNewPassword.text();
            clear();
            super.accept();
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
        if (!isDisposed()){
            uiCreator.leOldPassword.clear();
            uiCreator.leNewPassword.clear();
            uiCreator.leConfirmation.clear();
            refresh();
        }
    }
    
    private void refresh() {
        final boolean canChange =
                !uiCreator.leOldPassword.text().isEmpty()
                && !uiCreator.leNewPassword.text().isEmpty()
                && !uiCreator.leConfirmation.text().isEmpty();
        getButton(EDialogButtonType.OK).setEnabled(canChange);
    }
}
