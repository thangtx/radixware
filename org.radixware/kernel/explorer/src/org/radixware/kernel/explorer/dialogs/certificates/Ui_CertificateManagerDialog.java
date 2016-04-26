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
** Form generated from reading ui file 'CertificateManagerDialog.jui'
**
** Created: Wed May 11 16:35:09 2011
**      by: Qt User Interface Compiler version 4.5.2
**
** WARNING! All changes made in this file will be lost when recompiling ui file!
********************************************************************************/

package org.radixware.kernel.explorer.dialogs.certificates;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

public class Ui_CertificateManagerDialog implements com.trolltech.qt.QUiForm<QDialog>
{
    public QTabWidget tabWidget;
    public QWidget tabPersonalKeys;
    public QVBoxLayout verticalLayout_2;
    public QTableWidget personalKeysTable;
    public QWidget widget_2;
    public QHBoxLayout horizontalLayout_2;
    public QPushButton generateKeyButton;
    public QPushButton changePwdButton;
    public QPushButton requestCertificateButton;
    public QPushButton loadKeyCertificateButton;
    public QPushButton importKeyButton;
    public QPushButton deleteKeyButton;
    public QSpacerItem horizontalSpacer_2;
    public QWidget tabTrustedCertificates;
    public QVBoxLayout verticalLayout;
    public QTableWidget trustedCertificatesTable;
    public QWidget widget;
    public QHBoxLayout horizontalLayout;
    public QPushButton importTrustedCertificateButton;
    public QPushButton deleteTrustedCertificateButton;
    public QSpacerItem horizontalSpacer;

    public Ui_CertificateManagerDialog() { super(); }

    public void setupUi(QDialog CertificateManagerDialog)
    {
        CertificateManagerDialog.setObjectName("CertificateManagerDialog");
        CertificateManagerDialog.resize(new QSize(559, 308).expandedTo(CertificateManagerDialog.minimumSizeHint()));
        tabWidget = new QTabWidget(CertificateManagerDialog);
        tabWidget.setObjectName("tabWidget");
        tabWidget.setGeometry(new QRect(10, 0, 541, 291));
        tabPersonalKeys = new QWidget();
        tabPersonalKeys.setObjectName("tabPersonalKeys");
        verticalLayout_2 = new QVBoxLayout(tabPersonalKeys);
        verticalLayout_2.setObjectName("verticalLayout_2");
        personalKeysTable = new QTableWidget(tabPersonalKeys);
        personalKeysTable.setObjectName("personalKeysTable");

        verticalLayout_2.addWidget(personalKeysTable);

        widget_2 = new QWidget(tabPersonalKeys);
        widget_2.setObjectName("widget_2");
        horizontalLayout_2 = new QHBoxLayout(widget_2);
        horizontalLayout_2.setObjectName("horizontalLayout_2");
        generateKeyButton = new QPushButton(widget_2);
        generateKeyButton.setObjectName("generateKeyButton");

        horizontalLayout_2.addWidget(generateKeyButton);

        changePwdButton = new QPushButton(widget_2);
        changePwdButton.setObjectName("changePwdButton");

        horizontalLayout_2.addWidget(changePwdButton);

        requestCertificateButton = new QPushButton(widget_2);
        requestCertificateButton.setObjectName("requestCertificateButton");

        horizontalLayout_2.addWidget(requestCertificateButton);

        loadKeyCertificateButton = new QPushButton(widget_2);
        loadKeyCertificateButton.setObjectName("loadKeyCertificateButton");

        horizontalLayout_2.addWidget(loadKeyCertificateButton);

        importKeyButton = new QPushButton(widget_2);
        importKeyButton.setObjectName("importKeyButton");

        horizontalLayout_2.addWidget(importKeyButton);

        deleteKeyButton = new QPushButton(widget_2);
        deleteKeyButton.setObjectName("deleteKeyButton");

        horizontalLayout_2.addWidget(deleteKeyButton);

        horizontalSpacer_2 = new QSpacerItem(31, 20, com.trolltech.qt.gui.QSizePolicy.Policy.Expanding, com.trolltech.qt.gui.QSizePolicy.Policy.Minimum);

        horizontalLayout_2.addItem(horizontalSpacer_2);


        verticalLayout_2.addWidget(widget_2);

        tabWidget.addTab(tabPersonalKeys, com.trolltech.qt.core.QCoreApplication.translate("CertificateManagerDialog", "Personal Keys", null));
        tabTrustedCertificates = new QWidget();
        tabTrustedCertificates.setObjectName("tabTrustedCertificates");
        verticalLayout = new QVBoxLayout(tabTrustedCertificates);
        verticalLayout.setObjectName("verticalLayout");
        trustedCertificatesTable = new QTableWidget(tabTrustedCertificates);
        trustedCertificatesTable.setObjectName("trustedCertificatesTable");

        verticalLayout.addWidget(trustedCertificatesTable);

        widget = new QWidget(tabTrustedCertificates);
        widget.setObjectName("widget");
        horizontalLayout = new QHBoxLayout(widget);
        horizontalLayout.setObjectName("horizontalLayout");
        importTrustedCertificateButton = new QPushButton(widget);
        importTrustedCertificateButton.setObjectName("importTrustedCertificateButton");

        horizontalLayout.addWidget(importTrustedCertificateButton);

        deleteTrustedCertificateButton = new QPushButton(widget);
        deleteTrustedCertificateButton.setObjectName("deleteTrustedCertificateButton");

        horizontalLayout.addWidget(deleteTrustedCertificateButton);

        horizontalSpacer = new QSpacerItem(206, 20, com.trolltech.qt.gui.QSizePolicy.Policy.Expanding, com.trolltech.qt.gui.QSizePolicy.Policy.Minimum);

        horizontalLayout.addItem(horizontalSpacer);


        verticalLayout.addWidget(widget);

        tabWidget.addTab(tabTrustedCertificates, com.trolltech.qt.core.QCoreApplication.translate("CertificateManagerDialog", "Trusted Certificates", null));
        retranslateUi(CertificateManagerDialog);

        tabWidget.setCurrentIndex(0);


        CertificateManagerDialog.connectSlotsByName();
    } // setupUi

    void retranslateUi(QDialog CertificateManagerDialog)
    {
        CertificateManagerDialog.setWindowTitle(com.trolltech.qt.core.QCoreApplication.translate("CertificateManagerDialog", "Certificate Manager", null));
        personalKeysTable.clear();
        personalKeysTable.setColumnCount(6);

        QTableWidgetItem __colItem = new QTableWidgetItem();
        __colItem.setText(com.trolltech.qt.core.QCoreApplication.translate("CertificateManagerDialog", "Alias", null));
        personalKeysTable.setHorizontalHeaderItem(0, __colItem);

        QTableWidgetItem __colItem1 = new QTableWidgetItem();
        __colItem1.setText(com.trolltech.qt.core.QCoreApplication.translate("CertificateManagerDialog", "Issued To", null));
        personalKeysTable.setHorizontalHeaderItem(1, __colItem1);

        QTableWidgetItem __colItem2 = new QTableWidgetItem();
        __colItem2.setText(com.trolltech.qt.core.QCoreApplication.translate("CertificateManagerDialog", "Issued By", null));
        personalKeysTable.setHorizontalHeaderItem(2, __colItem2);

        QTableWidgetItem __colItem3 = new QTableWidgetItem();
        __colItem3.setText(com.trolltech.qt.core.QCoreApplication.translate("CertificateManagerDialog", "Valid From", null));
        personalKeysTable.setHorizontalHeaderItem(3, __colItem3);

        QTableWidgetItem __colItem4 = new QTableWidgetItem();
        __colItem4.setText(com.trolltech.qt.core.QCoreApplication.translate("CertificateManagerDialog", "Expiration", null));
        personalKeysTable.setHorizontalHeaderItem(4, __colItem4);

        QTableWidgetItem __colItem5 = new QTableWidgetItem();
        __colItem5.setText(com.trolltech.qt.core.QCoreApplication.translate("CertificateManagerDialog", "Algorithm", null));
        personalKeysTable.setHorizontalHeaderItem(5, __colItem5);
        personalKeysTable.setRowCount(0);
        generateKeyButton.setText(com.trolltech.qt.core.QCoreApplication.translate("CertificateManagerDialog", "Generate Key", null));
        changePwdButton.setText(com.trolltech.qt.core.QCoreApplication.translate("CertificateManagerDialog", "Change Key Password", null));
        requestCertificateButton.setText(com.trolltech.qt.core.QCoreApplication.translate("CertificateManagerDialog", "Request Certificate", null));
        loadKeyCertificateButton.setText(com.trolltech.qt.core.QCoreApplication.translate("CertificateManagerDialog", "Load Key Certificate", null));
        importKeyButton.setText(com.trolltech.qt.core.QCoreApplication.translate("CertificateManagerDialog", "Import Key", null));
        deleteKeyButton.setText(com.trolltech.qt.core.QCoreApplication.translate("CertificateManagerDialog", "Delete", null));
        tabWidget.setTabText(tabWidget.indexOf(tabPersonalKeys), com.trolltech.qt.core.QCoreApplication.translate("CertificateManagerDialog", "Personal Keys", null));
        trustedCertificatesTable.clear();
        trustedCertificatesTable.setColumnCount(6);

        QTableWidgetItem __colItem6 = new QTableWidgetItem();
        __colItem6.setText(com.trolltech.qt.core.QCoreApplication.translate("CertificateManagerDialog", "Alias", null));
        trustedCertificatesTable.setHorizontalHeaderItem(0, __colItem6);

        QTableWidgetItem __colItem7 = new QTableWidgetItem();
        __colItem7.setText(com.trolltech.qt.core.QCoreApplication.translate("CertificateManagerDialog", "Issued To", null));
        trustedCertificatesTable.setHorizontalHeaderItem(1, __colItem7);

        QTableWidgetItem __colItem8 = new QTableWidgetItem();
        __colItem8.setText(com.trolltech.qt.core.QCoreApplication.translate("CertificateManagerDialog", "Issued By", null));
        trustedCertificatesTable.setHorizontalHeaderItem(2, __colItem8);

        QTableWidgetItem __colItem9 = new QTableWidgetItem();
        __colItem9.setText(com.trolltech.qt.core.QCoreApplication.translate("CertificateManagerDialog", "Valid From", null));
        trustedCertificatesTable.setHorizontalHeaderItem(3, __colItem9);

        QTableWidgetItem __colItem10 = new QTableWidgetItem();
        __colItem10.setText(com.trolltech.qt.core.QCoreApplication.translate("CertificateManagerDialog", "Expiration", null));
        trustedCertificatesTable.setHorizontalHeaderItem(4, __colItem10);

        QTableWidgetItem __colItem11 = new QTableWidgetItem();
        __colItem11.setText(com.trolltech.qt.core.QCoreApplication.translate("CertificateManagerDialog", "Algorithm", null));
        trustedCertificatesTable.setHorizontalHeaderItem(5, __colItem11);
        trustedCertificatesTable.setRowCount(0);
        importTrustedCertificateButton.setText(com.trolltech.qt.core.QCoreApplication.translate("CertificateManagerDialog", "Import", null));
        deleteTrustedCertificateButton.setText(com.trolltech.qt.core.QCoreApplication.translate("CertificateManagerDialog", "Delete", null));
        tabWidget.setTabText(tabWidget.indexOf(tabTrustedCertificates), com.trolltech.qt.core.QCoreApplication.translate("CertificateManagerDialog", "Trusted Certificates", null));
    } // retranslateUi

}

