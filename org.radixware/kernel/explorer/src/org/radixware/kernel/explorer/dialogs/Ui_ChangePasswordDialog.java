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
** Form generated from reading ui file 'ChangePasswordDialog.jui'
**
** Created: Wed Sep 14 19:20:03 2011
**      by: Qt User Interface Compiler version 4.5.2
**
** WARNING! All changes made in this file will be lost when recompiling ui file!
********************************************************************************/

package org.radixware.kernel.explorer.dialogs;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

public class Ui_ChangePasswordDialog implements com.trolltech.qt.QUiForm<QDialog>
{
    public QFrame frameContent;
    public QGridLayout gridLayout_2;
    public QLabel lbOldPassword;
    public QLineEdit leOldPassword;
    public QLabel lbNewPassword;
    public QLineEdit leNewPassword;
    public QLabel lbConfirmation;
    public QLineEdit leConfirmation;
    public QWidget widgetMessage;
    public QVBoxLayout verticalLayout;
    public QLabel lbTitle;
    public QLabel lbMessage;

    public Ui_ChangePasswordDialog() { super(); }

    public void setupUi(QDialog ChangePasswordDialog)
    {
        ChangePasswordDialog.setObjectName("ChangePasswordDialog");
        ChangePasswordDialog.resize(new QSize(555, 492).expandedTo(ChangePasswordDialog.minimumSizeHint()));
        frameContent = new QFrame(ChangePasswordDialog);
        frameContent.setObjectName("frameContent");
        frameContent.setGeometry(new QRect(20, 120, 511, 171));
        frameContent.setFrameShape(com.trolltech.qt.gui.QFrame.Shape.StyledPanel);
        frameContent.setFrameShadow(com.trolltech.qt.gui.QFrame.Shadow.Raised);
        gridLayout_2 = new QGridLayout(frameContent);
        gridLayout_2.setObjectName("gridLayout_2");
        lbOldPassword = new QLabel(frameContent);
        lbOldPassword.setObjectName("lbOldPassword");
        QFont font = new QFont();
        font.setPointSize(10);
        font.setBold(false);
        font.setWeight(50);
        lbOldPassword.setFont(font);

        gridLayout_2.addWidget(lbOldPassword, 0, 0, 1, 1);

        leOldPassword = new QLineEdit(frameContent);
        leOldPassword.setObjectName("leOldPassword");
        leOldPassword.setMinimumSize(new QSize(200, 0));
        QFont font1 = new QFont();
        font1.setPointSize(10);
        font1.setBold(false);
        font1.setWeight(50);
        leOldPassword.setFont(font1);
        leOldPassword.setEchoMode(com.trolltech.qt.gui.QLineEdit.EchoMode.Password);

        gridLayout_2.addWidget(leOldPassword, 0, 1, 1, 1);

        lbNewPassword = new QLabel(frameContent);
        lbNewPassword.setObjectName("lbNewPassword");
        QFont font2 = new QFont();
        font2.setPointSize(10);
        font2.setBold(false);
        font2.setWeight(50);
        lbNewPassword.setFont(font2);

        gridLayout_2.addWidget(lbNewPassword, 1, 0, 1, 1);

        leNewPassword = new QLineEdit(frameContent);
        leNewPassword.setObjectName("leNewPassword");
        leNewPassword.setMinimumSize(new QSize(200, 0));
        QFont font3 = new QFont();
        font3.setPointSize(10);
        font3.setBold(false);
        font3.setWeight(50);
        leNewPassword.setFont(font3);
        leNewPassword.setEchoMode(com.trolltech.qt.gui.QLineEdit.EchoMode.Password);

        gridLayout_2.addWidget(leNewPassword, 1, 1, 1, 1);

        lbConfirmation = new QLabel(frameContent);
        lbConfirmation.setObjectName("lbConfirmation");
        QFont font4 = new QFont();
        font4.setPointSize(10);
        font4.setBold(false);
        font4.setWeight(50);
        lbConfirmation.setFont(font4);

        gridLayout_2.addWidget(lbConfirmation, 2, 0, 1, 1);

        leConfirmation = new QLineEdit(frameContent);
        leConfirmation.setObjectName("leConfirmation");
        leConfirmation.setMinimumSize(new QSize(200, 0));
        QFont font5 = new QFont();
        font5.setPointSize(10);
        font5.setBold(false);
        font5.setWeight(50);
        leConfirmation.setFont(font5);
        leConfirmation.setEchoMode(com.trolltech.qt.gui.QLineEdit.EchoMode.Password);

        gridLayout_2.addWidget(leConfirmation, 2, 1, 1, 1);

        widgetMessage = new QWidget(ChangePasswordDialog);
        widgetMessage.setObjectName("widgetMessage");
        widgetMessage.setGeometry(new QRect(50, 20, 401, 71));
        verticalLayout = new QVBoxLayout(widgetMessage);
        verticalLayout.setObjectName("verticalLayout");
        lbTitle = new QLabel(widgetMessage);
        lbTitle.setObjectName("lbTitle");
        QFont font6 = new QFont();
        font6.setPointSize(12);
        font6.setBold(true);
        font6.setWeight(75);
        lbTitle.setFont(font6);
        lbTitle.setAlignment(com.trolltech.qt.core.Qt.AlignmentFlag.createQFlags(com.trolltech.qt.core.Qt.AlignmentFlag.AlignCenter));

        verticalLayout.addWidget(lbTitle);

        lbMessage = new QLabel(widgetMessage);
        lbMessage.setObjectName("lbMessage");
        lbMessage.setAlignment(com.trolltech.qt.core.Qt.AlignmentFlag.createQFlags(com.trolltech.qt.core.Qt.AlignmentFlag.AlignCenter));

        verticalLayout.addWidget(lbMessage);

        lbOldPassword.setBuddy(leOldPassword);
        lbNewPassword.setBuddy(leNewPassword);
        lbConfirmation.setBuddy(leConfirmation);
        retranslateUi(ChangePasswordDialog);

        ChangePasswordDialog.connectSlotsByName();
    } // setupUi

    void retranslateUi(QDialog ChangePasswordDialog)
    {
        ChangePasswordDialog.setWindowTitle(com.trolltech.qt.core.QCoreApplication.translate("ChangePasswordDialog", "Change Password", null));
        lbOldPassword.setText(com.trolltech.qt.core.QCoreApplication.translate("ChangePasswordDialog", "&Current password:", null));
        lbNewPassword.setText(com.trolltech.qt.core.QCoreApplication.translate("ChangePasswordDialog", "New &password:", null));
        lbConfirmation.setText(com.trolltech.qt.core.QCoreApplication.translate("ChangePasswordDialog", "C&onfirmation:", null));
        lbTitle.setText("");
        lbMessage.setText("");
    } // retranslateUi

}

