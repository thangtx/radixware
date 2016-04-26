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
** Form generated from reading ui file 'TextWindow.jui'
**
** Created: ?? ???? 13 14:44:32 2009
**      by: Qt User Interface Compiler version 4.5.0
**
** WARNING! All changes made in this file will be lost when recompiling ui file!
********************************************************************************/

package org.radixware.kernel.explorer.editors.xml;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

public class Ui_TextWindow implements com.trolltech.qt.QUiForm<QWidget>
{
    public QGridLayout gridLayout;
    public QTextEdit textEdit;

    public Ui_TextWindow() { super(); }

    public void setupUi(QWidget TextWindow)
    {
        TextWindow.setObjectName("TextWindow");
        TextWindow.resize(new QSize(416, 296).expandedTo(TextWindow.minimumSizeHint()));
        QSizePolicy sizePolicy = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Expanding, com.trolltech.qt.gui.QSizePolicy.Policy.Expanding);
        sizePolicy.setHorizontalStretch((byte)0);
        sizePolicy.setVerticalStretch((byte)0);
        sizePolicy.setHeightForWidth(TextWindow.sizePolicy().hasHeightForWidth());
        TextWindow.setSizePolicy(sizePolicy);
        gridLayout = new QGridLayout(TextWindow);
        gridLayout.setObjectName("gridLayout");
        textEdit = new QTextEdit(TextWindow);
        textEdit.setObjectName("textEdit");
        textEdit.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.WheelFocus);

        gridLayout.addWidget(textEdit, 0, 0, 1, 1);

        retranslateUi(TextWindow);

        TextWindow.connectSlotsByName();
    } // setupUi

    void retranslateUi(QWidget TextWindow)
    {
        TextWindow.setWindowTitle("");
    } // retranslateUi

}

