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
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;

import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;

/**
 * Диалог импорта ключа из хранилища типа PKCS#12
 *
 */
final class ImportKeyDialog extends ExplorerDialog {

    private final Ui_ImportKeyDialog ui = new Ui_ImportKeyDialog();

    public ImportKeyDialog(final IClientEnvironment environment, final QWidget parent) {
        super(environment, parent, null);
        setupUi();
    }

    private void setupUi() {
        ui.setupUi(this);
        setWindowIcon(ExplorerIcon.getQIcon(CertificateManager.Icons.IMPORT_KEY));
        dialogLayout().addWidget(ui.gridLayoutWidget);
        addButton(EDialogButtonType.OK).setDefault(true);
        addButton(EDialogButtonType.CANCEL);

        acceptButtonClick.connect(this, "checkInputValid()");
        rejectButtonClick.connect(this, "reject()");
        ui.keystoreFileButton.clicked.connect(this, "selectFile()");
        getButton(EDialogButtonType.OK).setEnabled(false);
        ui.keystoreFileLineEdit.textChanged.connect(this, "updateOkButton()");
        ui.personalKeyPasswordLineEdit.textChanged.connect(this, "updateOkButton()");
        ui.keystorePasswordLineEdit.textChanged.connect(this, "updateOkButton()");
        ui.personalKeyPasswordLabel.setVisible(false);
        ui.personalKeyPasswordLineEdit.setVisible(false);
        dialogLayout().setSizeConstraint(QLayout.SizeConstraint.SetFixedSize);
    }

    private void updateOkButton() {
        getButton(EDialogButtonType.OK).setEnabled(!ui.keystoreFileLineEdit.text().isEmpty()
                && !ui.keystorePasswordLineEdit.text().isEmpty()
                /*&& !ui.personalKeyPasswordLineEdit.text().isEmpty()*/);
    }

    @SuppressWarnings("unused")
    private void selectFile() {
        String fileName = QFileDialog.getOpenFileName(this,
                Application.translate("ImportKeyDialog", "Select PKCS#12 Keystore File"),
                "",
                new QFileDialog.Filter(Application.translate("ImportKeyDialog", "PKCS#12 keystore files (*.p12)")));
        if (!fileName.equals("")) {
            ui.keystoreFileLineEdit.setText(fileName);
        }
    }

    @SuppressWarnings("unused")
    private void checkInputValid() {
        if (ui.keystoreFileLineEdit.text().isEmpty()) {
            Application.messageError(Application.translate("ImportKeyDialog", "Input Error"),
                    Application.translate("ImportKeyDialog", "PKCS#12 keystore file is not specified"));
            return;
        }
        accept();
    }

    public String getKeystoreFilePath() {
        return ui.keystoreFileLineEdit.text();
    }

    public char[] getKeystorePassword() {
        return ui.keystorePasswordLineEdit.text().toCharArray();
    }

    public char[] getPersonalKeyPassword() {
        return ui.personalKeyPasswordLineEdit.text().toCharArray();
    }
}
