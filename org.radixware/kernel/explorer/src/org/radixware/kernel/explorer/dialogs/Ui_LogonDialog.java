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
** Form generated from reading ui file 'LogonDialog.jui'
**
** Created: Fri May 7 15:30:47 2010
**      by: Qt User Interface Compiler version 4.5.2
**
** WARNING! All changes made in this file will be lost when recompiling ui file!
********************************************************************************/

package org.radixware.kernel.explorer.dialogs;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

public class Ui_LogonDialog implements com.trolltech.qt.QUiForm<QDialog>
{
    public QFrame contentFrame;
    public QGridLayout gridLayout;
    public QLabel lbUserName;
    public QLineEdit leUserName;
    public QLabel lbPassword;
    public QLineEdit lePassword;
    public QLabel lbConnection;
    public QComboBox cbConnection;
    public QToolButton tbEdit;

    public Ui_LogonDialog() { super(); }

    public void setupUi(QDialog LogonDialog)
    {
        LogonDialog.setObjectName("LogonDialog");
        LogonDialog.resize(new QSize(523, 192).expandedTo(LogonDialog.minimumSizeHint()));
        LogonDialog.setModal(false);
        contentFrame = new QFrame(LogonDialog);
        contentFrame.setObjectName("contentFrame");
        contentFrame.setGeometry(new QRect(10, 10, 500, 151));
        contentFrame.setFrameShape(com.trolltech.qt.gui.QFrame.Shape.StyledPanel);
        contentFrame.setFrameShadow(com.trolltech.qt.gui.QFrame.Shadow.Raised);
        gridLayout = new QGridLayout(contentFrame);
        gridLayout.setMargin(6);
        gridLayout.setObjectName("gridLayout");
        lbUserName = new QLabel(contentFrame);
        lbUserName.setObjectName("lbUserName");
        QSizePolicy sizePolicy = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Maximum, com.trolltech.qt.gui.QSizePolicy.Policy.Preferred);
        sizePolicy.setHorizontalStretch((byte)0);
        sizePolicy.setVerticalStretch((byte)0);
        sizePolicy.setHeightForWidth(lbUserName.sizePolicy().hasHeightForWidth());
        lbUserName.setSizePolicy(sizePolicy);

        gridLayout.addWidget(lbUserName, 0, 0, 1, 1);

        leUserName = new QLineEdit(contentFrame);
        leUserName.setObjectName("leUserName");
        leUserName.setMinimumSize(new QSize(200, 0));
        leUserName.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.StrongFocus);

        gridLayout.addWidget(leUserName, 0, 1, 1, 2);

        lbPassword = new QLabel(contentFrame);
        lbPassword.setObjectName("lbPassword");
        QSizePolicy sizePolicy1 = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Maximum, com.trolltech.qt.gui.QSizePolicy.Policy.Preferred);
        sizePolicy1.setHorizontalStretch((byte)0);
        sizePolicy1.setVerticalStretch((byte)0);
        sizePolicy1.setHeightForWidth(lbPassword.sizePolicy().hasHeightForWidth());
        lbPassword.setSizePolicy(sizePolicy1);

        gridLayout.addWidget(lbPassword, 1, 0, 1, 1);

        lePassword = new QLineEdit(contentFrame);
        lePassword.setObjectName("lePassword");
        lePassword.setMinimumSize(new QSize(200, 0));
        lePassword.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.StrongFocus);
        lePassword.setEchoMode(com.trolltech.qt.gui.QLineEdit.EchoMode.Password);

        gridLayout.addWidget(lePassword, 1, 1, 1, 2);

        lbConnection = new QLabel(contentFrame);
        lbConnection.setObjectName("lbConnection");
        QSizePolicy sizePolicy2 = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Maximum, com.trolltech.qt.gui.QSizePolicy.Policy.Preferred);
        sizePolicy2.setHorizontalStretch((byte)0);
        sizePolicy2.setVerticalStretch((byte)0);
        sizePolicy2.setHeightForWidth(lbConnection.sizePolicy().hasHeightForWidth());
        lbConnection.setSizePolicy(sizePolicy2);

        gridLayout.addWidget(lbConnection, 2, 0, 1, 1);

        cbConnection = new QComboBox(contentFrame);
        cbConnection.setObjectName("cbConnection");
        cbConnection.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.WheelFocus);

        gridLayout.addWidget(cbConnection, 2, 1, 1, 1);

        tbEdit = new QToolButton(contentFrame);
        tbEdit.setObjectName("tbEdit");
        tbEdit.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.TabFocus);

        gridLayout.addWidget(tbEdit, 2, 2, 1, 1);

        lbUserName.setBuddy(leUserName);
        lbPassword.setBuddy(lePassword);
        lbConnection.setBuddy(cbConnection);
        retranslateUi(LogonDialog);

        LogonDialog.connectSlotsByName();
    } // setupUi

    void retranslateUi(QDialog LogonDialog)
    {
        LogonDialog.setWindowTitle(com.trolltech.qt.core.QCoreApplication.translate("LogonDialog", "User Login", null));
        lbUserName.setText(com.trolltech.qt.core.QCoreApplication.translate("LogonDialog", "&User name:", null));
        lbPassword.setText(com.trolltech.qt.core.QCoreApplication.translate("LogonDialog", "&Password:", null));
        lbConnection.setText(com.trolltech.qt.core.QCoreApplication.translate("LogonDialog", "&Connection:", null));
        tbEdit.setText(com.trolltech.qt.core.QCoreApplication.translate("LogonDialog", "...", null));
    } // retranslateUi

}

