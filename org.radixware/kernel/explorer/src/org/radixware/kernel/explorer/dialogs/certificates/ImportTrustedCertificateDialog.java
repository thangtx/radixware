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

import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.env.Application;

/**
 * Диалог импорта доверенного сертификата из файла
 *
 */
public class ImportTrustedCertificateDialog extends ExplorerDialog {

    private final Ui_ImportTrustedCertificateDialog ui = new Ui_ImportTrustedCertificateDialog();

    public ImportTrustedCertificateDialog(final IClientEnvironment environment, final QWidget parent) {
        super(environment, parent, null);
        setupUi();
    }

    private void setupUi() {
        ui.setupUi(this);
        dialogLayout().addWidget(ui.gridLayoutWidget);
        addButton(EDialogButtonType.OK).setDefault(true);
        addButton(EDialogButtonType.CANCEL);

        acceptButtonClick.connect(this, "checkInputValid()");
        rejectButtonClick.connect(this, "reject()");
        ui.certificateFileButton.clicked.connect(this, "selectFile()");
    }

    @SuppressWarnings("unused")
    private void selectFile() {
        final String title = Application.translate("ImportTrustedCertificateDialog", "Select Trusted Certificate File");
        final String filter = Application.translate("ImportTrustedCertificateDialog", "Certificate files (%s)");
        String fileName = QFileDialog.getOpenFileName(this, title, "", new QFileDialog.Filter(String.format(filter, "*.cer *.crt *.pem")));
        if (!fileName.equals("")) {
            ui.certificateFileLineEdit.setText(fileName);
        }
    }

    @SuppressWarnings("unused")
    private void checkInputValid() {
        if (ui.aliasLineEdit.text().isEmpty()) {
            Application.messageError(Application.translate("ImportTrustedCertificateDialog", "Input Error"),
                    Application.translate("ImportTrustedCertificateDialog", "Certificate alias is not specified"));
            return;
        }
        if (ui.certificateFileLineEdit.text().isEmpty()) {
            Application.messageError(Application.translate("ImportTrustedCertificateDialog", "Input Error"),
                    Application.translate("ImportTrustedCertificateDialog", "Certificate file is not specified"));
            return;
        }
        accept();
    }

    public String getAlias() {
        return ui.aliasLineEdit.text();
    }

    public String getCertificateFilePath() {
        return ui.certificateFileLineEdit.text();
    }
}
