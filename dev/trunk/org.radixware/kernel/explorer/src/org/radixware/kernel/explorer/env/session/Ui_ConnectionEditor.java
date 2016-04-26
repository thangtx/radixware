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
** Form generated from reading ui file 'ConnectionEditor.jui'
**
** Created: Fri Apr 12 19:33:36 2013
**      by: Qt User Interface Compiler version 4.5.2
**
** WARNING! All changes made in this file will be lost when recompiling ui file!
********************************************************************************/

package org.radixware.kernel.explorer.env.session;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

public class Ui_ConnectionEditor implements com.trolltech.qt.QUiForm<QDialog>
{
    public QTabWidget tabWidget;
    public QWidget tab_options;
    public QGridLayout optionsLayout;
    public QLabel userNameLabel;
    public QLineEdit userNameData;
    public QLabel initialAddressLabel;
    public QWidget manifestWidget;
    public QHBoxLayout hboxLayout;
    public QLineEdit stationNameData;
    public QLabel languageLabel;
    public QComboBox languageData;
    public QLabel eventSeverityLabel;
    public QComboBox eventSeverityData;
    public QLabel explorerRootLabel;
    public QLabel stationNameLabel;
    public QLabel commentLabel;
    public QLabel countryLabel;
    public QWidget tab_encryption;
    public QVBoxLayout verticalLayout_2;
    public QVBoxLayout ltSecurity;
    public QHBoxLayout ltAuthType;
    public QLabel lblAuthType;
    public QComboBox cmbAuthType;
    public QGroupBox sslGroupBox;
    public QVBoxLayout verticalLayout;
    public QGroupBox gbKeyStoreType;
    public QVBoxLayout vlKeyStoreType;
    public QRadioButton rbFileKeyStore;
    public QRadioButton rbPKCS11KeyStore;
    public QGroupBox gbSSLAdvanced;
    public QHBoxLayout hlSSLAdvansed;
    public QPushButton pbCertificateManager;
    public QPushButton pbPkcs11Config;
    public QGroupBox krbGroupBox;
    public QHBoxLayout horizontalLayout;
    public QLabel label_2;
    public QLineEdit leEasPrincipalName;
    public QWidget connectionNameWidget;
    public QHBoxLayout hboxLayout1;
    public QLabel connectionNameLabel;
    public QLineEdit connectionNameData;

    public Ui_ConnectionEditor() { super(); }

    public void setupUi(QDialog ConnectionEditor)
    {
        ConnectionEditor.setObjectName("ConnectionEditor");
        ConnectionEditor.resize(new QSize(358, 413).expandedTo(ConnectionEditor.minimumSizeHint()));
        QSizePolicy sizePolicy = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Fixed, com.trolltech.qt.gui.QSizePolicy.Policy.Fixed);
        sizePolicy.setHorizontalStretch((byte)0);
        sizePolicy.setVerticalStretch((byte)0);
        sizePolicy.setHeightForWidth(ConnectionEditor.sizePolicy().hasHeightForWidth());
        ConnectionEditor.setSizePolicy(sizePolicy);
        ConnectionEditor.setModal(false);
        tabWidget = new QTabWidget(ConnectionEditor);
        tabWidget.setObjectName("tabWidget");
        tabWidget.setGeometry(new QRect(9, 43, 319, 361));
        QSizePolicy sizePolicy1 = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Minimum, com.trolltech.qt.gui.QSizePolicy.Policy.Minimum);
        sizePolicy1.setHorizontalStretch((byte)0);
        sizePolicy1.setVerticalStretch((byte)0);
        sizePolicy1.setHeightForWidth(tabWidget.sizePolicy().hasHeightForWidth());
        tabWidget.setSizePolicy(sizePolicy1);
        tabWidget.setUsesScrollButtons(false);
        tab_options = new QWidget();
        tab_options.setObjectName("tab_options");
        optionsLayout = new QGridLayout(tab_options);
        optionsLayout.setObjectName("optionsLayout");
        userNameLabel = new QLabel(tab_options);
        userNameLabel.setObjectName("userNameLabel");

        optionsLayout.addWidget(userNameLabel, 0, 0, 1, 1);

        userNameData = new QLineEdit(tab_options);
        userNameData.setObjectName("userNameData");
        userNameData.setMinimumSize(new QSize(200, 0));
        userNameData.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.StrongFocus);

        optionsLayout.addWidget(userNameData, 0, 1, 1, 1);

        initialAddressLabel = new QLabel(tab_options);
        initialAddressLabel.setObjectName("initialAddressLabel");

        optionsLayout.addWidget(initialAddressLabel, 1, 0, 1, 1);

        manifestWidget = new QWidget(tab_options);
        manifestWidget.setObjectName("manifestWidget");
        manifestWidget.setMaximumSize(new QSize(16777215, 23));
        hboxLayout = new QHBoxLayout(manifestWidget);
        hboxLayout.setMargin(0);
        hboxLayout.setObjectName("hboxLayout");
        stationNameData = new QLineEdit(manifestWidget);
        stationNameData.setObjectName("stationNameData");
        stationNameData.setMinimumSize(new QSize(200, 0));
        stationNameData.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.StrongFocus);

        hboxLayout.addWidget(stationNameData);


        optionsLayout.addWidget(manifestWidget, 2, 1, 1, 1);

        languageLabel = new QLabel(tab_options);
        languageLabel.setObjectName("languageLabel");

        optionsLayout.addWidget(languageLabel, 5, 0, 1, 1);

        languageData = new QComboBox(tab_options);
        languageData.setObjectName("languageData");
        languageData.setMinimumSize(new QSize(200, 0));
        languageData.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.WheelFocus);

        optionsLayout.addWidget(languageData, 5, 1, 1, 1);

        eventSeverityLabel = new QLabel(tab_options);
        eventSeverityLabel.setObjectName("eventSeverityLabel");

        optionsLayout.addWidget(eventSeverityLabel, 7, 0, 1, 1);

        eventSeverityData = new QComboBox(tab_options);
        eventSeverityData.setObjectName("eventSeverityData");
        eventSeverityData.setMinimumSize(new QSize(200, 0));
        eventSeverityData.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.WheelFocus);

        optionsLayout.addWidget(eventSeverityData, 7, 1, 1, 1);

        explorerRootLabel = new QLabel(tab_options);
        explorerRootLabel.setObjectName("explorerRootLabel");

        optionsLayout.addWidget(explorerRootLabel, 8, 0, 1, 1);

        stationNameLabel = new QLabel(tab_options);
        stationNameLabel.setObjectName("stationNameLabel");

        optionsLayout.addWidget(stationNameLabel, 2, 0, 1, 1);

        commentLabel = new QLabel(tab_options);
        commentLabel.setObjectName("commentLabel");

        optionsLayout.addWidget(commentLabel, 9, 0, 1, 1);

        countryLabel = new QLabel(tab_options);
        countryLabel.setObjectName("countryLabel");

        optionsLayout.addWidget(countryLabel, 6, 0, 1, 1);

        tabWidget.addTab(tab_options, com.trolltech.qt.core.QCoreApplication.translate("ConnectionEditor", "Main Options", null));
        tab_encryption = new QWidget();
        tab_encryption.setObjectName("tab_encryption");
        verticalLayout_2 = new QVBoxLayout(tab_encryption);
        verticalLayout_2.setObjectName("verticalLayout_2");
        ltSecurity = new QVBoxLayout();
        ltSecurity.setObjectName("ltSecurity");
        ltAuthType = new QHBoxLayout();
        ltAuthType.setObjectName("ltAuthType");
        ltAuthType.setSizeConstraint(com.trolltech.qt.gui.QLayout.SizeConstraint.SetMinimumSize);
        lblAuthType = new QLabel(tab_encryption);
        lblAuthType.setObjectName("lblAuthType");
        QSizePolicy sizePolicy2 = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Preferred, com.trolltech.qt.gui.QSizePolicy.Policy.Fixed);
        sizePolicy2.setHorizontalStretch((byte)0);
        sizePolicy2.setVerticalStretch((byte)0);
        sizePolicy2.setHeightForWidth(lblAuthType.sizePolicy().hasHeightForWidth());
        lblAuthType.setSizePolicy(sizePolicy2);

        ltAuthType.addWidget(lblAuthType);

        cmbAuthType = new QComboBox(tab_encryption);
        cmbAuthType.setObjectName("cmbAuthType");
        QSizePolicy sizePolicy3 = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.MinimumExpanding, com.trolltech.qt.gui.QSizePolicy.Policy.Fixed);
        sizePolicy3.setHorizontalStretch((byte)0);
        sizePolicy3.setVerticalStretch((byte)0);
        sizePolicy3.setHeightForWidth(cmbAuthType.sizePolicy().hasHeightForWidth());
        cmbAuthType.setSizePolicy(sizePolicy3);

        ltAuthType.addWidget(cmbAuthType);


        ltSecurity.addLayout(ltAuthType);

        sslGroupBox = new QGroupBox(tab_encryption);
        sslGroupBox.setObjectName("sslGroupBox");
        sslGroupBox.setEnabled(true);
        QSizePolicy sizePolicy4 = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Preferred, com.trolltech.qt.gui.QSizePolicy.Policy.Fixed);
        sizePolicy4.setHorizontalStretch((byte)0);
        sizePolicy4.setVerticalStretch((byte)0);
        sizePolicy4.setHeightForWidth(sslGroupBox.sizePolicy().hasHeightForWidth());
        sslGroupBox.setSizePolicy(sizePolicy4);
        sslGroupBox.setCheckable(true);
        sslGroupBox.setChecked(false);
        verticalLayout = new QVBoxLayout(sslGroupBox);
        verticalLayout.setObjectName("verticalLayout");
        gbKeyStoreType = new QGroupBox(sslGroupBox);
        gbKeyStoreType.setObjectName("gbKeyStoreType");
        vlKeyStoreType = new QVBoxLayout(gbKeyStoreType);
        vlKeyStoreType.setObjectName("vlKeyStoreType");
        rbFileKeyStore = new QRadioButton(gbKeyStoreType);
        rbFileKeyStore.setObjectName("rbFileKeyStore");
        rbFileKeyStore.setChecked(true);

        vlKeyStoreType.addWidget(rbFileKeyStore);

        rbPKCS11KeyStore = new QRadioButton(gbKeyStoreType);
        rbPKCS11KeyStore.setObjectName("rbPKCS11KeyStore");

        vlKeyStoreType.addWidget(rbPKCS11KeyStore);


        verticalLayout.addWidget(gbKeyStoreType);

        gbSSLAdvanced = new QGroupBox(sslGroupBox);
        gbSSLAdvanced.setObjectName("gbSSLAdvanced");
        hlSSLAdvansed = new QHBoxLayout(gbSSLAdvanced);
        hlSSLAdvansed.setObjectName("hlSSLAdvansed");
        pbCertificateManager = new QPushButton(gbSSLAdvanced);
        pbCertificateManager.setObjectName("pbCertificateManager");
        QSizePolicy sizePolicy5 = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Expanding, com.trolltech.qt.gui.QSizePolicy.Policy.Fixed);
        sizePolicy5.setHorizontalStretch((byte)0);
        sizePolicy5.setVerticalStretch((byte)0);
        sizePolicy5.setHeightForWidth(pbCertificateManager.sizePolicy().hasHeightForWidth());
        pbCertificateManager.setSizePolicy(sizePolicy5);

        hlSSLAdvansed.addWidget(pbCertificateManager);

        pbPkcs11Config = new QPushButton(gbSSLAdvanced);
        pbPkcs11Config.setObjectName("pbPkcs11Config");
        QSizePolicy sizePolicy6 = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Expanding, com.trolltech.qt.gui.QSizePolicy.Policy.Fixed);
        sizePolicy6.setHorizontalStretch((byte)0);
        sizePolicy6.setVerticalStretch((byte)0);
        sizePolicy6.setHeightForWidth(pbPkcs11Config.sizePolicy().hasHeightForWidth());
        pbPkcs11Config.setSizePolicy(sizePolicy6);

        hlSSLAdvansed.addWidget(pbPkcs11Config);


        verticalLayout.addWidget(gbSSLAdvanced);


        ltSecurity.addWidget(sslGroupBox);

        krbGroupBox = new QGroupBox(tab_encryption);
        krbGroupBox.setObjectName("krbGroupBox");
        krbGroupBox.setEnabled(false);
        krbGroupBox.setCheckable(false);
        krbGroupBox.setChecked(false);
        horizontalLayout = new QHBoxLayout(krbGroupBox);
        horizontalLayout.setObjectName("horizontalLayout");
        label_2 = new QLabel(krbGroupBox);
        label_2.setObjectName("label_2");

        horizontalLayout.addWidget(label_2);

        leEasPrincipalName = new QLineEdit(krbGroupBox);
        leEasPrincipalName.setObjectName("leEasPrincipalName");

        horizontalLayout.addWidget(leEasPrincipalName);


        ltSecurity.addWidget(krbGroupBox);


        verticalLayout_2.addLayout(ltSecurity);

        tabWidget.addTab(tab_encryption, com.trolltech.qt.core.QCoreApplication.translate("ConnectionEditor", "Security", null));
        connectionNameWidget = new QWidget(ConnectionEditor);
        connectionNameWidget.setObjectName("connectionNameWidget");
        connectionNameWidget.setGeometry(new QRect(9, 9, 359, 23));
        hboxLayout1 = new QHBoxLayout(connectionNameWidget);
        hboxLayout1.setMargin(0);
        hboxLayout1.setObjectName("hboxLayout1");
        connectionNameLabel = new QLabel(connectionNameWidget);
        connectionNameLabel.setObjectName("connectionNameLabel");

        hboxLayout1.addWidget(connectionNameLabel);

        connectionNameData = new QLineEdit(connectionNameWidget);
        connectionNameData.setObjectName("connectionNameData");
        connectionNameData.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.StrongFocus);

        hboxLayout1.addWidget(connectionNameData);

        userNameLabel.setBuddy(userNameData);
        languageLabel.setBuddy(languageData);
        eventSeverityLabel.setBuddy(eventSeverityData);
        stationNameLabel.setBuddy(stationNameData);
        connectionNameLabel.setBuddy(connectionNameData);
        QWidget.setTabOrder(connectionNameData, userNameData);
        QWidget.setTabOrder(userNameData, stationNameData);
        QWidget.setTabOrder(stationNameData, languageData);
        QWidget.setTabOrder(languageData, eventSeverityData);
        retranslateUi(ConnectionEditor);

        tabWidget.setCurrentIndex(0);
        eventSeverityData.setCurrentIndex(5);


        ConnectionEditor.connectSlotsByName();
    } // setupUi

    void retranslateUi(QDialog ConnectionEditor)
    {
        ConnectionEditor.setWindowTitle(com.trolltech.qt.core.QCoreApplication.translate("ConnectionEditor", "Connection Options", null));
        userNameLabel.setText(com.trolltech.qt.core.QCoreApplication.translate("ConnectionEditor", "&User name:", null));
        initialAddressLabel.setText(com.trolltech.qt.core.QCoreApplication.translate("ConnectionEditor", "&Servers:", null));
        languageLabel.setText(com.trolltech.qt.core.QCoreApplication.translate("ConnectionEditor", "&Language:", null));
        eventSeverityLabel.setText(com.trolltech.qt.core.QCoreApplication.translate("ConnectionEditor", "&Trace level:", null));
        eventSeverityData.clear();
        eventSeverityData.addItem(com.trolltech.qt.core.QCoreApplication.translate("ConnectionEditor", "Debug", null));
        eventSeverityData.addItem(com.trolltech.qt.core.QCoreApplication.translate("ConnectionEditor", "Event", null));
        eventSeverityData.addItem(com.trolltech.qt.core.QCoreApplication.translate("ConnectionEditor", "Warning", null));
        eventSeverityData.addItem(com.trolltech.qt.core.QCoreApplication.translate("ConnectionEditor", "Error", null));
        eventSeverityData.addItem(com.trolltech.qt.core.QCoreApplication.translate("ConnectionEditor", "Alarm", null));
        eventSeverityData.addItem(com.trolltech.qt.core.QCoreApplication.translate("ConnectionEditor", "None", null));
        explorerRootLabel.setText(com.trolltech.qt.core.QCoreApplication.translate("ConnectionEditor", "Explorer &root:", null));
        stationNameLabel.setText(com.trolltech.qt.core.QCoreApplication.translate("ConnectionEditor", "St&ation name:", null));
        commentLabel.setText(com.trolltech.qt.core.QCoreApplication.translate("ConnectionEditor", "Comm&ent:", null));
        countryLabel.setText(com.trolltech.qt.core.QCoreApplication.translate("ConnectionEditor", "Cou&ntry:", null));
        tabWidget.setTabText(tabWidget.indexOf(tab_options), com.trolltech.qt.core.QCoreApplication.translate("ConnectionEditor", "Main Options", null));
        lblAuthType.setText(com.trolltech.qt.core.QCoreApplication.translate("ConnectionEditor", "Authentication type:", null));
        sslGroupBox.setTitle(com.trolltech.qt.core.QCoreApplication.translate("ConnectionEditor", "Use TLS &Encryption", null));
        gbKeyStoreType.setTitle(com.trolltech.qt.core.QCoreApplication.translate("ConnectionEditor", "Key Store Type:", null));
        rbFileKeyStore.setText(com.trolltech.qt.core.QCoreApplication.translate("ConnectionEditor", "&File", null));
        rbPKCS11KeyStore.setText(com.trolltech.qt.core.QCoreApplication.translate("ConnectionEditor", "&PKCS#11", null));
        gbSSLAdvanced.setTitle(com.trolltech.qt.core.QCoreApplication.translate("ConnectionEditor", "Advanced:", null));
        pbCertificateManager.setText(com.trolltech.qt.core.QCoreApplication.translate("ConnectionEditor", "Cert&ificates", null));
        pbPkcs11Config.setText(com.trolltech.qt.core.QCoreApplication.translate("ConnectionEditor", "PKCS#11 &Settings", null));
        krbGroupBox.setTitle(com.trolltech.qt.core.QCoreApplication.translate("ConnectionEditor", "Kerberos", null));
        label_2.setText(com.trolltech.qt.core.QCoreApplication.translate("ConnectionEditor", "EAS principal name:", null));
        tabWidget.setTabText(tabWidget.indexOf(tab_encryption), com.trolltech.qt.core.QCoreApplication.translate("ConnectionEditor", "Security", null));
        connectionNameLabel.setText(com.trolltech.qt.core.QCoreApplication.translate("ConnectionEditor", "&Connection name:", null));
    } // retranslateUi

}

