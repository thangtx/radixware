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

import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.env.Application;

/**
 * Диалог создания запроса на получение сертификата ключа
 * и загрузки полученного сертификата
 *
 */

public class CertificateRequestDialog extends ExplorerDialog {
    private final Ui_CertificateRequestDialog ui = new Ui_CertificateRequestDialog();

    private boolean isCertificateRequest;

    public CertificateRequestDialog(final IClientEnvironment environment,final QWidget parent, boolean isCertificateRequest){
        super(environment,parent, null);
        this.isCertificateRequest = isCertificateRequest;
        setupUi();
    }

    private void setupUi(){
        ui.setupUi(this);
        dialogLayout().addWidget(ui.gridLayoutWidget);
        addButton(EDialogButtonType.OK).setDefault(true);
        addButton(EDialogButtonType.CANCEL);
        
        final MessageProvider msgProvider = getEnvironment().getMessageProvider();
        if (isCertificateRequest){
            ui.certificateFileLabel.setText(msgProvider.translate("CertificateRequestDialog", "Certificate Request File"));
            this.setWindowTitle(msgProvider.translate("CertificateRequestDialog", "Request Key Certificate"));
        } else{
            ui.certificateFileLabel.setText(msgProvider.translate("CertificateRequestDialog", "Certificate File"));
            this.setWindowTitle(msgProvider.translate("CertificateRequestDialog", "Receive Key Certificate"));
        }

        acceptButtonClick.connect(this, "checkInputValid()");
        rejectButtonClick.connect(this, "reject()");
        ui.certificateFileButton.clicked.connect(this, "selectFile()");
    }

    @SuppressWarnings("unused")
    private void selectFile(){
        final MessageProvider msgProvider = getEnvironment().getMessageProvider();
        final String fileName;
        if (isCertificateRequest){
            final String title = msgProvider.translate("CertificateRequestDialog", "Select Certificate Signing Request File");
            final String filter = msgProvider.translate("CertificateRequestDialog", "Certificate Signing Request files (%s)");
            fileName = QFileDialog.getSaveFileName(this,title,"",new QFileDialog.Filter(String.format(filter, "*.csr")));
        } else{
            final String title = msgProvider.translate("CertificateRequestDialog", "Select Certificate File");
            final String filter = msgProvider.translate("CertificateRequestDialog", "Certificate files (%s)");
            fileName = QFileDialog.getOpenFileName(this,title,"",new QFileDialog.Filter(String.format(filter, "*.cer")));
        }
        if (!fileName.equals(""))
            ui.certificateFileLineEdit.setText(fileName);
    }

    @SuppressWarnings("unused")
    private void checkInputValid(){
        final MessageProvider msgProvider = getEnvironment().getMessageProvider();
        if (ui.certificateFileLineEdit.text().isEmpty()){
            String errorMessage = (isCertificateRequest ?
                msgProvider.translate("CertificateRequestDialog", "Certificate request file is not specified") :
                msgProvider.translate("CertificateRequestDialog", "Certificate file is not specified"));
            Application.messageError(msgProvider.translate("CertificateRequestDialog", "Input Error"), errorMessage);
            return;
        }
        accept();
    }

    public char[] getKeyPassword(){
        return ui.keyPasswordLineEdit.text().toCharArray();
    }

    public String getFilePath(){
        return ui.certificateFileLineEdit.text();
    }
}