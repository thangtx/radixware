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
** Form generated from reading ui file 'GenerateKeyDialog.jui'
**
** Created: Fri Aug 30 21:04:42 2013
**      by: Qt User Interface Compiler version 4.5.2
**
** WARNING! All changes made in this file will be lost when recompiling ui file!
********************************************************************************/

package org.radixware.kernel.explorer.dialogs.certificates;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

public class Ui_GenerateKeyDialog implements com.trolltech.qt.QUiForm<QDialog>
{
    public QWidget layoutWidget;
    public QGridLayout gridLayout;
    public QLabel nameLabel;
    public QLineEdit nameLineEdit;
    public QLabel uidLabel;
    public QLineEdit uidLineEdit;
    public QLabel orgUnitLabel;
    public QLineEdit orgUnitLineEdit;
    public QLabel organizationLabel;
    public QLineEdit organizationLineEdit;
    public QLabel localityLabel;
    public QLineEdit localityLineEdit;
    public QLabel stateLabel;
    public QLineEdit stateLineEdit;
    public QLabel countryLabel;
    public QLineEdit countryLineEdit;
    public QSpacerItem verticalSpacer;
    public QLabel keyLengthLabel;
    public QComboBox keyLengthComboBox;
    public QLabel publicExponentLabel;
    public QComboBox publicExponentComboBox;
    public QLabel keyValidityLabel;
    public QSpinBox keyValiditySpinBox;
    public QSpacerItem horizontalSpacer;
    public QLabel keyPasswordLabel;
    public QLineEdit keyPasswordLineEdit;
    public QLabel passwordConfirmationLabel;
    public QLineEdit passwordConfirmationLineEdit;
    public QLabel aliasLabel;
    public QLineEdit aliasLineEdit;

    public Ui_GenerateKeyDialog() { super(); }

    public void setupUi(QDialog GenerateKeyDialog)
    {
        GenerateKeyDialog.setObjectName("GenerateKeyDialog");
        GenerateKeyDialog.resize(new QSize(373, 419).expandedTo(GenerateKeyDialog.minimumSizeHint()));
        GenerateKeyDialog.setMinimumSize(new QSize(0, 0));
        layoutWidget = new QWidget(GenerateKeyDialog);
        layoutWidget.setObjectName("layoutWidget");
        layoutWidget.setGeometry(new QRect(11, 11, 351, 392));
        gridLayout = new QGridLayout(layoutWidget);
        gridLayout.setObjectName("gridLayout");
        nameLabel = new QLabel(layoutWidget);
        nameLabel.setObjectName("nameLabel");

        gridLayout.addWidget(nameLabel, 0, 0, 1, 1);

        nameLineEdit = new QLineEdit(layoutWidget);
        nameLineEdit.setObjectName("nameLineEdit");

        gridLayout.addWidget(nameLineEdit, 0, 1, 1, 2);

        uidLabel = new QLabel(layoutWidget);
        uidLabel.setObjectName("uidLabel");

        gridLayout.addWidget(uidLabel, 1, 0, 1, 1);

        uidLineEdit = new QLineEdit(layoutWidget);
        uidLineEdit.setObjectName("uidLineEdit");

        gridLayout.addWidget(uidLineEdit, 1, 1, 1, 2);

        orgUnitLabel = new QLabel(layoutWidget);
        orgUnitLabel.setObjectName("orgUnitLabel");

        gridLayout.addWidget(orgUnitLabel, 2, 0, 1, 1);

        orgUnitLineEdit = new QLineEdit(layoutWidget);
        orgUnitLineEdit.setObjectName("orgUnitLineEdit");

        gridLayout.addWidget(orgUnitLineEdit, 2, 1, 1, 2);

        organizationLabel = new QLabel(layoutWidget);
        organizationLabel.setObjectName("organizationLabel");

        gridLayout.addWidget(organizationLabel, 3, 0, 1, 1);

        organizationLineEdit = new QLineEdit(layoutWidget);
        organizationLineEdit.setObjectName("organizationLineEdit");

        gridLayout.addWidget(organizationLineEdit, 3, 1, 1, 2);

        localityLabel = new QLabel(layoutWidget);
        localityLabel.setObjectName("localityLabel");

        gridLayout.addWidget(localityLabel, 4, 0, 1, 1);

        localityLineEdit = new QLineEdit(layoutWidget);
        localityLineEdit.setObjectName("localityLineEdit");

        gridLayout.addWidget(localityLineEdit, 4, 1, 1, 2);

        stateLabel = new QLabel(layoutWidget);
        stateLabel.setObjectName("stateLabel");

        gridLayout.addWidget(stateLabel, 5, 0, 1, 1);

        stateLineEdit = new QLineEdit(layoutWidget);
        stateLineEdit.setObjectName("stateLineEdit");

        gridLayout.addWidget(stateLineEdit, 5, 1, 1, 2);

        countryLabel = new QLabel(layoutWidget);
        countryLabel.setObjectName("countryLabel");

        gridLayout.addWidget(countryLabel, 6, 0, 1, 1);

        countryLineEdit = new QLineEdit(layoutWidget);
        countryLineEdit.setObjectName("countryLineEdit");
        countryLineEdit.setMaxLength(2);

        gridLayout.addWidget(countryLineEdit, 6, 1, 1, 2);

        verticalSpacer = new QSpacerItem(20, 40, com.trolltech.qt.gui.QSizePolicy.Policy.Minimum, com.trolltech.qt.gui.QSizePolicy.Policy.Expanding);

        gridLayout.addItem(verticalSpacer, 7, 0, 1, 1);

        keyLengthLabel = new QLabel(layoutWidget);
        keyLengthLabel.setObjectName("keyLengthLabel");

        gridLayout.addWidget(keyLengthLabel, 8, 0, 1, 1);

        keyLengthComboBox = new QComboBox(layoutWidget);
        keyLengthComboBox.setObjectName("keyLengthComboBox");

        gridLayout.addWidget(keyLengthComboBox, 8, 1, 1, 1);

        publicExponentLabel = new QLabel(layoutWidget);
        publicExponentLabel.setObjectName("publicExponentLabel");

        gridLayout.addWidget(publicExponentLabel, 9, 0, 1, 1);

        publicExponentComboBox = new QComboBox(layoutWidget);
        publicExponentComboBox.setObjectName("publicExponentComboBox");

        gridLayout.addWidget(publicExponentComboBox, 9, 1, 1, 1);

        keyValidityLabel = new QLabel(layoutWidget);
        keyValidityLabel.setObjectName("keyValidityLabel");

        gridLayout.addWidget(keyValidityLabel, 10, 0, 1, 1);

        keyValiditySpinBox = new QSpinBox(layoutWidget);
        keyValiditySpinBox.setObjectName("keyValiditySpinBox");
        keyValiditySpinBox.setMinimum(1);
        keyValiditySpinBox.setMaximum(3650);
        keyValiditySpinBox.setValue(90);

        gridLayout.addWidget(keyValiditySpinBox, 10, 1, 1, 1);

        horizontalSpacer = new QSpacerItem(40, 20, com.trolltech.qt.gui.QSizePolicy.Policy.Expanding, com.trolltech.qt.gui.QSizePolicy.Policy.Minimum);

        gridLayout.addItem(horizontalSpacer, 10, 2, 1, 1);

        keyPasswordLabel = new QLabel(layoutWidget);
        keyPasswordLabel.setObjectName("keyPasswordLabel");

        gridLayout.addWidget(keyPasswordLabel, 12, 0, 1, 1);

        keyPasswordLineEdit = new QLineEdit(layoutWidget);
        keyPasswordLineEdit.setObjectName("keyPasswordLineEdit");
        keyPasswordLineEdit.setEchoMode(com.trolltech.qt.gui.QLineEdit.EchoMode.Password);

        gridLayout.addWidget(keyPasswordLineEdit, 12, 1, 1, 2);

        passwordConfirmationLabel = new QLabel(layoutWidget);
        passwordConfirmationLabel.setObjectName("passwordConfirmationLabel");

        gridLayout.addWidget(passwordConfirmationLabel, 13, 0, 1, 1);

        passwordConfirmationLineEdit = new QLineEdit(layoutWidget);
        passwordConfirmationLineEdit.setObjectName("passwordConfirmationLineEdit");
        passwordConfirmationLineEdit.setEchoMode(com.trolltech.qt.gui.QLineEdit.EchoMode.Password);

        gridLayout.addWidget(passwordConfirmationLineEdit, 13, 1, 1, 2);

        aliasLabel = new QLabel(layoutWidget);
        aliasLabel.setObjectName("aliasLabel");

        gridLayout.addWidget(aliasLabel, 11, 0, 1, 1);

        aliasLineEdit = new QLineEdit(layoutWidget);
        aliasLineEdit.setObjectName("aliasLineEdit");

        gridLayout.addWidget(aliasLineEdit, 11, 1, 1, 2);

        retranslateUi(GenerateKeyDialog);

        GenerateKeyDialog.connectSlotsByName();
    } // setupUi

    void retranslateUi(QDialog GenerateKeyDialog)
    {
        GenerateKeyDialog.setWindowTitle(com.trolltech.qt.core.QCoreApplication.translate("GenerateKeyDialog", "Generate Personal Key", null));
        nameLabel.setText(com.trolltech.qt.core.QCoreApplication.translate("GenerateKeyDialog", "User name:", null));
        uidLabel.setText(com.trolltech.qt.core.QCoreApplication.translate("GenerateKeyDialog", "User ID:", null));
        orgUnitLabel.setText(com.trolltech.qt.core.QCoreApplication.translate("GenerateKeyDialog", "Organization unit:", null));
        organizationLabel.setText(com.trolltech.qt.core.QCoreApplication.translate("GenerateKeyDialog", "Organization name:", null));
        localityLabel.setText(com.trolltech.qt.core.QCoreApplication.translate("GenerateKeyDialog", "Locality:", null));
        stateLabel.setText(com.trolltech.qt.core.QCoreApplication.translate("GenerateKeyDialog", "State:", null));
        countryLabel.setText(com.trolltech.qt.core.QCoreApplication.translate("GenerateKeyDialog", "Country:", null));
        keyLengthLabel.setText(com.trolltech.qt.core.QCoreApplication.translate("GenerateKeyDialog", "Key length:", null));
        keyLengthComboBox.clear();
        keyLengthComboBox.addItem(com.trolltech.qt.core.QCoreApplication.translate("GenerateKeyDialog", "1024", null));
        keyLengthComboBox.addItem(com.trolltech.qt.core.QCoreApplication.translate("GenerateKeyDialog", "2048", null));
        publicExponentLabel.setText(com.trolltech.qt.core.QCoreApplication.translate("GenerateKeyDialog", "Key public exponent:", null));
        publicExponentComboBox.clear();
        publicExponentComboBox.addItem(com.trolltech.qt.core.QCoreApplication.translate("GenerateKeyDialog", "3", null));
        publicExponentComboBox.addItem(com.trolltech.qt.core.QCoreApplication.translate("GenerateKeyDialog", "17", null));
        publicExponentComboBox.addItem(com.trolltech.qt.core.QCoreApplication.translate("GenerateKeyDialog", "65537", null));
        keyValidityLabel.setText(com.trolltech.qt.core.QCoreApplication.translate("GenerateKeyDialog", "Key validity (days):", null));
        keyPasswordLabel.setText(com.trolltech.qt.core.QCoreApplication.translate("GenerateKeyDialog", "Key password:", null));
        passwordConfirmationLabel.setText(com.trolltech.qt.core.QCoreApplication.translate("GenerateKeyDialog", "Password confirmation:", null));
        aliasLabel.setText(com.trolltech.qt.core.QCoreApplication.translate("GenerateKeyDialog", "Certificate alias:", null));
    } // retranslateUi

}

