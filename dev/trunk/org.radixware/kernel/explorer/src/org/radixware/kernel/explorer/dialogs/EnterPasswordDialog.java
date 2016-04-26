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
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLayout.SizeConstraint;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IEnterPasswordDialog;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.env.ExplorerIcon;


public class EnterPasswordDialog extends ExplorerDialog implements IEnterPasswordDialog {

    private final QLineEdit lePassword = new QLineEdit(this);
    private final QLineEdit leConfirmation = new QLineEdit(this);    
    private final QLineEdit leUserName = new QLineEdit(this);
    private final QLabel lbUserName;
    private final QLabel lbPassword;
    private final QLabel lbConfirmation;
    private final QLabel lbMessage;    

    public EnterPasswordDialog(final IClientEnvironment environment, final QWidget parent) {
        super(environment, parent, null);
        lbUserName = new QLabel(environment.getMessageProvider().translate("ExplorerDialog", "&User name:"), this);
        lbPassword = new QLabel(environment.getMessageProvider().translate("ExplorerDialog", "&Password:"), this);
        lbConfirmation = new QLabel(environment.getMessageProvider().translate("ExplorerDialog", "&Confirmation:"), this);
        lbMessage = new QLabel(environment.getMessageProvider().translate("ExplorerDialog", "Please enter your password or press cancel to disconnect"), this);
        setupUi();
    }

    private void setupUi() {
        /*
         * final QFont font = new QFont(lbMessage.font()); font.setBold(true);
         * font.setPointSize(10); font.setWeight(75); lbMessage.setFont(font);
         */
        lePassword.setEchoMode(QLineEdit.EchoMode.Password);
        leConfirmation.setEchoMode(QLineEdit.EchoMode.Password);
        lePassword.setFocus();
        lbPassword.setBuddy(lePassword);
        lbConfirmation.setBuddy(leConfirmation);
        lbConfirmation.setVisible(false);
        leConfirmation.setVisible(false);
        lbUserName.setBuddy(leUserName);
        lbUserName.setVisible(false);
        leUserName.setVisible(false);
        setWindowTitle(getEnvironment().getMessageProvider().translate("ExplorerDialog", "Enter Password"));
        setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.Connection.KEY));
        dialogLayout().setSpacing(9);
        dialogLayout().setContentsMargins(6, 9, 6, 9);
        final QGridLayout gLayout = new QGridLayout(null);        
        gLayout.addWidget(lbUserName, 0, 0);
        gLayout.addWidget(leUserName, 0, 1);        
        gLayout.addWidget(lbPassword, 1, 0);
        gLayout.addWidget(lePassword, 1, 1);
        gLayout.addWidget(lbConfirmation, 2, 0);
        gLayout.addWidget(leConfirmation, 2, 1);
        dialogLayout().addWidget(lbMessage);
        lbMessage.setAlignment(Qt.AlignmentFlag.AlignCenter);
        dialogLayout().addLayout(gLayout);
        addButtons(EnumSet.of(EDialogButtonType.OK,EDialogButtonType.CANCEL), true);
        getButton(EDialogButtonType.OK).setEnabled(false);
        dialogLayout().setSizeConstraint(SizeConstraint.SetFixedSize);

        lePassword.textChanged.connect(this, "refresh()");
        leConfirmation.textChanged.connect(this, "refresh()");
        leUserName.textChanged.connect(this,"refresh()");
    }
    
    public void setPasswordConfirmationRequired(final boolean isRequired){
        lbConfirmation.setVisible(isRequired);
        leConfirmation.setVisible(isRequired);
    }        
    
    public boolean isPasswordConfirmationRequired(){
        return leConfirmation.isVisible();
    }
    
    public void setUserNameRequired(final boolean isRequired){
        lbUserName.setVisible(isRequired);
        leUserName.setVisible(isRequired);
        if (isRequired){
            leUserName.setFocus();
        }
    }
    
    public boolean isUserNameRequired(){
        return lbUserName.isVisible();
    }

    @Override
    public void setMessage(final String message) {
        lbMessage.setText(message);
    }

    @Override
    public String getPassword() {
        return lePassword.text();
    }
    
    public String getUserName(){
        return leUserName.text();
    }

    @SuppressWarnings("unused")
    private void refresh() {       
        getButton(EDialogButtonType.OK).setEnabled(!lePassword.text().isEmpty()
                                                   && (!leConfirmation.isVisible() || !leConfirmation.text().isEmpty())
                                                   && (!leUserName.isVisible() || !leUserName.text().isEmpty())
                                                  );
    }

    @Override
    public void accept() {
        if (leConfirmation.isVisible() && !lePassword.text().equals(leConfirmation.text())) {
            final String title = getEnvironment().getMessageProvider().translate("ExplorerDialog", "Password was not Confirmed");
            final String message = getEnvironment().getMessageProvider().translate("ChangePasswordDialog", "The password and confirmation do not match");
            getEnvironment().messageInformation(title, message);
            lePassword.clear();
            leConfirmation.clear();
            refresh();
            lePassword.setFocus();
            return;
        }
        super.accept();
    }

    @Override
    public int exec() {
        lePassword.clear();
        leConfirmation.clear();
        return super.exec();
    }
}