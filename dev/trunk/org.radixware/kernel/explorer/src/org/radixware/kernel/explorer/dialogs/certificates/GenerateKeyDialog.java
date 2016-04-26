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

package org.radixware.kernel.explorer.dialogs.certificates;

import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.HashMap;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.ssl.CertificateUtils;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;

/**
 * Диалог генерации RSA-ключа с самоподписанным сертификатом
 *
 */
public class GenerateKeyDialog extends ExplorerDialog {

    private final Ui_GenerateKeyDialog ui = new Ui_GenerateKeyDialog();

    public GenerateKeyDialog(final IClientEnvironment environment, final QWidget parent, final boolean isPkcs11) {
        super(environment, parent, null);
        setupUi(isPkcs11);
    }

    private void setupUi(final boolean isPkcs11) {
        ui.setupUi(this);
        dialogLayout().addWidget(ui.layoutWidget);
        addButton(EDialogButtonType.OK).setDefault(true);
        addButton(EDialogButtonType.CANCEL);

        
        acceptButtonClick.connect(this, isPkcs11 ? "onPkcs11Accept()" : "onAccept()");
        rejectButtonClick.connect(this, "reject()");

        String dn = System.getProperty("org.radixware.kernel.explorer.clientCert.dn", "");
        HashMap<String, String> map = CertificateUtils.parseDistinguishedName(dn);
        ui.orgUnitLineEdit.setText(map.get("OU"));
        ui.organizationLineEdit.setText(map.get("O"));
        ui.localityLineEdit.setText(map.get("L"));
        ui.stateLineEdit.setText(map.get("ST"));
        ui.countryLineEdit.setText(map.get("C"));
        String keySize = System.getProperty("org.radixware.kernel.explorer.clientCert.keySize", "1024");
        ui.keyLengthComboBox.setEditText(keySize);
        String keyValidity = System.getProperty("org.radixware.kernel.explorer.clientCert.keyValidity", "90");
        ui.keyValiditySpinBox.setValue(Integer.parseInt(keyValidity));
        ui.keyPasswordLabel.setHidden(isPkcs11);
        ui.keyPasswordLineEdit.setHidden(isPkcs11);
        ui.passwordConfirmationLabel.setHidden(isPkcs11);
        ui.passwordConfirmationLineEdit.setHidden(isPkcs11);
        ui.aliasLabel.setVisible(isPkcs11);
        ui.aliasLineEdit.setVisible(isPkcs11);
        ui.layoutWidget.setMinimumWidth(450);
        dialogLayout().setSizeConstraint(QLayout.SizeConstraint.SetFixedSize);
    }
    
    @SuppressWarnings("unused")
    private void onPkcs11Accept(){
        final String title = getEnvironment().getMessageProvider().translate("GenerateKeyDialog", "Can`t Generate Key");
        if (ui.aliasLineEdit.text().isEmpty()){
            final String message = getEnvironment().getMessageProvider().translate("GenerateKeyDialog", "Please enter certificate alias");
            getEnvironment().messageInformation(title, message);
            ui.aliasLineEdit.setFocus();
            return;
        }
        accept();
    }
    
    @SuppressWarnings("unused")
    private void onAccept(){
        final String title = getEnvironment().getMessageProvider().translate("GenerateKeyDialog", "Can`t Generate Key");
        if (ui.keyPasswordLineEdit.text().isEmpty()){
            final String message = getEnvironment().getMessageProvider().translate("GenerateKeyDialog", "Please enter key password");
            getEnvironment().messageInformation(title, message);
            ui.keyPasswordLineEdit.setFocus();
            return;
        }
        if (!ui.keyPasswordLineEdit.text().equals(ui.passwordConfirmationLineEdit.text())) {
            final String message = getEnvironment().getMessageProvider().translate("GenerateKeyDialog", "Key password and confirmation password do not match");
            getEnvironment().messageInformation(title, message);
            ui.keyPasswordLineEdit.clear();
            ui.passwordConfirmationLineEdit.clear();
            ui.keyPasswordLineEdit.setFocus();
            return;
        }
        accept();
    }

    public int getKeyLength() {
        String value = ui.keyLengthComboBox.currentText();
        return Integer.parseInt(value);
    }

    public int getKeyPublicExponent() {
        String value = ui.publicExponentComboBox.currentText();
        return Integer.parseInt(value);
    }

    public int getKeyValidity() {
        return ui.keyValiditySpinBox.value();
    }

    public char[] getKeyPassword() {
        return ui.keyPasswordLineEdit.text().toCharArray();
    }

    public String getCN() {
        return ui.nameLineEdit.text();
    }

    public String getOU() {
        return ui.orgUnitLineEdit.text();
    }

    public String getO() {
        return ui.organizationLineEdit.text();
    }

    public String getL() {
        return ui.localityLineEdit.text();
    }

    public String getST() {
        return ui.stateLineEdit.text();
    }

    public String getC() {
        return ui.countryLineEdit.text();
    }
    
    public String getUID() {
        return ui.uidLineEdit.text();
    }
    
    public String getCertificateAlias(){
        return ui.aliasLineEdit.text();
    }
    
    public void setCertificateAlias(final String alias){
        ui.aliasLineEdit.setText(alias);
    }
}