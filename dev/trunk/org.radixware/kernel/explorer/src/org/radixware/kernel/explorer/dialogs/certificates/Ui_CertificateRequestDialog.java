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
** Form generated from reading ui file 'CertificateRequestDialog.jui'
**
** Created: Tue Mar 16 18:54:23 2010
**      by: Qt User Interface Compiler version 4.5.2
**
** WARNING! All changes made in this file will be lost when recompiling ui file!
********************************************************************************/

package org.radixware.kernel.explorer.dialogs.certificates;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

public class Ui_CertificateRequestDialog implements com.trolltech.qt.QUiForm<QDialog>
{
    public QWidget gridLayoutWidget;
    public QGridLayout gridLayout;
    public QLabel certificateFileLabel;
    public QLineEdit certificateFileLineEdit;
    public QToolButton certificateFileButton;
    public QLabel keyPasswordLabel;
    public QLineEdit keyPasswordLineEdit;

    public Ui_CertificateRequestDialog() { super(); }

    public void setupUi(QDialog CertificateRequestDialog)
    {
        CertificateRequestDialog.setObjectName("CertificateRequestDialog");
        CertificateRequestDialog.resize(new QSize(354, 106).expandedTo(CertificateRequestDialog.minimumSizeHint()));
        gridLayoutWidget = new QWidget(CertificateRequestDialog);
        gridLayoutWidget.setObjectName("gridLayoutWidget");
        gridLayoutWidget.setGeometry(new QRect(10, 10, 331, 81));
        gridLayout = new QGridLayout(gridLayoutWidget);
        gridLayout.setObjectName("gridLayout");
        certificateFileLabel = new QLabel(gridLayoutWidget);
        certificateFileLabel.setObjectName("certificateFileLabel");

        gridLayout.addWidget(certificateFileLabel, 0, 0, 1, 1);

        certificateFileLineEdit = new QLineEdit(gridLayoutWidget);
        certificateFileLineEdit.setObjectName("certificateFileLineEdit");
        certificateFileLineEdit.setReadOnly(true);

        gridLayout.addWidget(certificateFileLineEdit, 0, 1, 1, 1);

        certificateFileButton = new QToolButton(gridLayoutWidget);
        certificateFileButton.setObjectName("certificateFileButton");

        gridLayout.addWidget(certificateFileButton, 0, 2, 1, 1);

        keyPasswordLabel = new QLabel(gridLayoutWidget);
        keyPasswordLabel.setObjectName("keyPasswordLabel");

        gridLayout.addWidget(keyPasswordLabel, 1, 0, 1, 1);

        keyPasswordLineEdit = new QLineEdit(gridLayoutWidget);
        keyPasswordLineEdit.setObjectName("keyPasswordLineEdit");
        keyPasswordLineEdit.setEchoMode(com.trolltech.qt.gui.QLineEdit.EchoMode.Password);

        gridLayout.addWidget(keyPasswordLineEdit, 1, 1, 1, 2);

        retranslateUi(CertificateRequestDialog);

        CertificateRequestDialog.connectSlotsByName();
    } // setupUi

    void retranslateUi(QDialog CertificateRequestDialog)
    {
        CertificateRequestDialog.setWindowTitle(com.trolltech.qt.core.QCoreApplication.translate("CertificateRequestDialog", "Certificate Request", null));
        certificateFileLabel.setText(com.trolltech.qt.core.QCoreApplication.translate("CertificateRequestDialog", "Certificate File", null));
        certificateFileButton.setText(com.trolltech.qt.core.QCoreApplication.translate("CertificateRequestDialog", "...", null));
        keyPasswordLabel.setText(com.trolltech.qt.core.QCoreApplication.translate("CertificateRequestDialog", "Personal Key Password", null));
    } // retranslateUi

}

