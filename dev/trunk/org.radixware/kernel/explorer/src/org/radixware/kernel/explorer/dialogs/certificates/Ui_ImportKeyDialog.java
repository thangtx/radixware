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
** Form generated from reading ui file 'ImportKeyDialog.jui'
**
** Created: Tue Mar 16 18:56:18 2010
**      by: Qt User Interface Compiler version 4.5.2
**
** WARNING! All changes made in this file will be lost when recompiling ui file!
********************************************************************************/

package org.radixware.kernel.explorer.dialogs.certificates;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

public class Ui_ImportKeyDialog implements com.trolltech.qt.QUiForm<QDialog>
{
    public QWidget gridLayoutWidget;
    public QGridLayout gridLayout;
    public QLabel keystoreFileLabel;
    public QLabel keystorePasswordLabel;
    public QLineEdit keystoreFileLineEdit;
    public QLineEdit keystorePasswordLineEdit;
    public QToolButton keystoreFileButton;
    public QLineEdit personalKeyPasswordLineEdit;
    public QLabel personalKeyPasswordLabel;
    public QSpacerItem verticalSpacer;

    public Ui_ImportKeyDialog() { super(); }

    public void setupUi(QDialog ImportKeyDialog)
    {
        ImportKeyDialog.setObjectName("ImportKeyDialog");
        ImportKeyDialog.resize(new QSize(400, 132).expandedTo(ImportKeyDialog.minimumSizeHint()));
        gridLayoutWidget = new QWidget(ImportKeyDialog);
        gridLayoutWidget.setObjectName("gridLayoutWidget");
        gridLayoutWidget.setGeometry(new QRect(20, 10, 361, 101));
        gridLayout = new QGridLayout(gridLayoutWidget);
        gridLayout.setObjectName("gridLayout");
        keystoreFileLabel = new QLabel(gridLayoutWidget);
        keystoreFileLabel.setObjectName("keystoreFileLabel");

        gridLayout.addWidget(keystoreFileLabel, 0, 0, 1, 1);

        keystorePasswordLabel = new QLabel(gridLayoutWidget);
        keystorePasswordLabel.setObjectName("keystorePasswordLabel");

        gridLayout.addWidget(keystorePasswordLabel, 1, 0, 1, 1);

        keystoreFileLineEdit = new QLineEdit(gridLayoutWidget);
        keystoreFileLineEdit.setObjectName("keystoreFileLineEdit");

        gridLayout.addWidget(keystoreFileLineEdit, 0, 1, 1, 1);

        keystorePasswordLineEdit = new QLineEdit(gridLayoutWidget);
        keystorePasswordLineEdit.setObjectName("keystorePasswordLineEdit");
        keystorePasswordLineEdit.setEchoMode(com.trolltech.qt.gui.QLineEdit.EchoMode.Password);

        gridLayout.addWidget(keystorePasswordLineEdit, 1, 1, 1, 2);

        keystoreFileButton = new QToolButton(gridLayoutWidget);
        keystoreFileButton.setObjectName("keystoreFileButton");

        gridLayout.addWidget(keystoreFileButton, 0, 2, 1, 1);

        personalKeyPasswordLineEdit = new QLineEdit(gridLayoutWidget);
        personalKeyPasswordLineEdit.setObjectName("personalKeyPasswordLineEdit");
        personalKeyPasswordLineEdit.setEchoMode(com.trolltech.qt.gui.QLineEdit.EchoMode.Password);

        gridLayout.addWidget(personalKeyPasswordLineEdit, 3, 1, 1, 2);

        personalKeyPasswordLabel = new QLabel(gridLayoutWidget);
        personalKeyPasswordLabel.setObjectName("personalKeyPasswordLabel");

        gridLayout.addWidget(personalKeyPasswordLabel, 3, 0, 1, 1);

        verticalSpacer = new QSpacerItem(20, 20, com.trolltech.qt.gui.QSizePolicy.Policy.Minimum, com.trolltech.qt.gui.QSizePolicy.Policy.Expanding);

        gridLayout.addItem(verticalSpacer, 2, 0, 1, 1);

        retranslateUi(ImportKeyDialog);

        ImportKeyDialog.connectSlotsByName();
    } // setupUi

    void retranslateUi(QDialog ImportKeyDialog)
    {
        ImportKeyDialog.setWindowTitle(com.trolltech.qt.core.QCoreApplication.translate("ImportKeyDialog", "Import key from PKCS#12 keystore", null));
        keystoreFileLabel.setText(com.trolltech.qt.core.QCoreApplication.translate("ImportKeyDialog", "PKCS#12 Keystore File", null));
        keystorePasswordLabel.setText(com.trolltech.qt.core.QCoreApplication.translate("ImportKeyDialog", "PKCS#12 Keystore Password", null));
        keystoreFileButton.setText(com.trolltech.qt.core.QCoreApplication.translate("ImportKeyDialog", "...", null));
        personalKeyPasswordLabel.setText(com.trolltech.qt.core.QCoreApplication.translate("ImportKeyDialog", "Personal key password", null));
    } // retranslateUi

}

