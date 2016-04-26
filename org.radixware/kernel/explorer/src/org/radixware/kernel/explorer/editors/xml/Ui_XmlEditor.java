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
** Form generated from reading ui file 'XmlEditor.jui'
**
** Created: Mon Mar 23 10:58:27 2015
**      by: Qt User Interface Compiler version 4.5.2
**
** WARNING! All changes made in this file will be lost when recompiling ui file!
********************************************************************************/

package org.radixware.kernel.explorer.editors.xml;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

public class Ui_XmlEditor implements com.trolltech.qt.QUiForm<QMainWindow>
{
    public QWidget centralwidget;
    public QMenuBar menubar;

    public Ui_XmlEditor() { super(); }

    public void setupUi(QMainWindow XmlEditor)
    {
        XmlEditor.setObjectName("XmlEditor");
        XmlEditor.resize(new QSize(400, 300).expandedTo(XmlEditor.minimumSizeHint()));
        XmlEditor.setMinimumSize(new QSize(300, 300));
        centralwidget = new QWidget(XmlEditor);
        centralwidget.setObjectName("centralwidget");
        XmlEditor.setCentralWidget(centralwidget);
        menubar = new QMenuBar(XmlEditor);
        menubar.setObjectName("menubar");
        menubar.setGeometry(new QRect(0, 0, 400, 21));
        XmlEditor.setMenuBar(menubar);
        retranslateUi(XmlEditor);

        XmlEditor.connectSlotsByName();
    } // setupUi

    void retranslateUi(QMainWindow XmlEditor)
    {
        XmlEditor.setWindowTitle(com.trolltech.qt.core.QCoreApplication.translate("XmlEditor", "Xml Editor", null));
    } // retranslateUi

}

