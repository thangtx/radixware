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

/********************************************************************************
** Form generated from reading ui file 'ImportTrustedCertificateDialog.jui'
**
** Created: Tue Mar 16 18:57:01 2010
**      by: Qt User Interface Compiler version 4.5.2
**
** WARNING! All changes made in this file will be lost when recompiling ui file!
********************************************************************************/

package org.radixware.kernel.explorer.dialogs.certificates;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

public class Ui_ImportTrustedCertificateDialog implements com.trolltech.qt.QUiForm<QDialog>
{
    public QWidget gridLayoutWidget;
    public QGridLayout gridLayout;
    public QLineEdit certificateFileLineEdit;
    public QLineEdit aliasLineEdit;
    public QToolButton certificateFileButton;
    public QLabel certificateFileLabel;
    public QLabel aliasLabel;

    public Ui_ImportTrustedCertificateDialog() { super(); }

    public void setupUi(QDialog ImportTrustedCertificateDialog)
    {
        ImportTrustedCertificateDialog.setObjectName("ImportTrustedCertificateDialog");
        ImportTrustedCertificateDialog.resize(new QSize(295, 71).expandedTo(ImportTrustedCertificateDialog.minimumSizeHint()));
        gridLayoutWidget = new QWidget(ImportTrustedCertificateDialog);
        gridLayoutWidget.setObjectName("gridLayoutWidget");
        gridLayoutWidget.setGeometry(new QRect(10, 10, 271, 48));
        gridLayout = new QGridLayout(gridLayoutWidget);
        gridLayout.setObjectName("gridLayout");
        certificateFileLineEdit = new QLineEdit(gridLayoutWidget);
        certificateFileLineEdit.setObjectName("certificateFileLineEdit");

        gridLayout.addWidget(certificateFileLineEdit, 0, 2, 1, 1);

        aliasLineEdit = new QLineEdit(gridLayoutWidget);
        aliasLineEdit.setObjectName("aliasLineEdit");

        gridLayout.addWidget(aliasLineEdit, 1, 2, 1, 2);

        certificateFileButton = new QToolButton(gridLayoutWidget);
        certificateFileButton.setObjectName("certificateFileButton");

        gridLayout.addWidget(certificateFileButton, 0, 3, 1, 1);

        certificateFileLabel = new QLabel(gridLayoutWidget);
        certificateFileLabel.setObjectName("certificateFileLabel");

        gridLayout.addWidget(certificateFileLabel, 0, 1, 1, 1);

        aliasLabel = new QLabel(gridLayoutWidget);
        aliasLabel.setObjectName("aliasLabel");

        gridLayout.addWidget(aliasLabel, 1, 1, 1, 1);

        retranslateUi(ImportTrustedCertificateDialog);

        ImportTrustedCertificateDialog.connectSlotsByName();
    } // setupUi

    void retranslateUi(QDialog ImportTrustedCertificateDialog)
    {
        ImportTrustedCertificateDialog.setWindowTitle(com.trolltech.qt.core.QCoreApplication.translate("ImportTrustedCertificateDialog", "Import Trusted Certificate", null));
        certificateFileButton.setText(com.trolltech.qt.core.QCoreApplication.translate("ImportTrustedCertificateDialog", "...", null));
        certificateFileLabel.setText(com.trolltech.qt.core.QCoreApplication.translate("ImportTrustedCertificateDialog", "Certificate File", null));
        aliasLabel.setText(com.trolltech.qt.core.QCoreApplication.translate("ImportTrustedCertificateDialog", "Alias", null));
    } // retranslateUi

}

