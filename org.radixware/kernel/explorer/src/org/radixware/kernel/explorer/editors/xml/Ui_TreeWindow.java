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
** Form generated from reading ui file 'TreeWindow.jui'
**
** Created: ?? ???? 13 14:44:41 2009
**      by: Qt User Interface Compiler version 4.5.0
**
** WARNING! All changes made in this file will be lost when recompiling ui file!
********************************************************************************/

package org.radixware.kernel.explorer.editors.xml;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

public class Ui_TreeWindow implements com.trolltech.qt.QUiForm<QWidget>
{
    public QGridLayout gridLayout;
    public QTreeWidget treeWidget;
    public QWidget widget3;
    public QGridLayout gridLayout1;
    public QTableWidget tableWidget;

    public Ui_TreeWindow() { super(); }

    public void setupUi(QWidget TreeWindow)
    {
        TreeWindow.setObjectName("TreeWindow");
        TreeWindow.resize(new QSize(662, 454).expandedTo(TreeWindow.minimumSizeHint()));
        QSizePolicy sizePolicy = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Expanding, com.trolltech.qt.gui.QSizePolicy.Policy.Expanding);
        sizePolicy.setHorizontalStretch((byte)0);
        sizePolicy.setVerticalStretch((byte)0);
        sizePolicy.setHeightForWidth(TreeWindow.sizePolicy().hasHeightForWidth());
        TreeWindow.setSizePolicy(sizePolicy);
        gridLayout = new QGridLayout(TreeWindow);
        gridLayout.setObjectName("gridLayout");
        treeWidget = new QTreeWidget(TreeWindow);
        treeWidget.setObjectName("treeWidget");
        QSizePolicy sizePolicy1 = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Expanding, com.trolltech.qt.gui.QSizePolicy.Policy.Expanding);
        sizePolicy1.setHorizontalStretch((byte)0);
        sizePolicy1.setVerticalStretch((byte)0);
        sizePolicy1.setHeightForWidth(treeWidget.sizePolicy().hasHeightForWidth());
        treeWidget.setSizePolicy(sizePolicy1);
        treeWidget.setMinimumSize(new QSize(200, 200));
        treeWidget.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.WheelFocus);
        treeWidget.setFrameShape(com.trolltech.qt.gui.QFrame.Shape.Box);
        treeWidget.setProperty("showDropIndicator", true);
        treeWidget.setHorizontalScrollMode(com.trolltech.qt.gui.QAbstractItemView.ScrollMode.ScrollPerPixel);
        treeWidget.setIndentation(20);
        treeWidget.setRootIsDecorated(true);
        treeWidget.setColumnCount(1);

        gridLayout.addWidget(treeWidget, 0, 0, 1, 1);

        widget3 = new QWidget(TreeWindow);
        widget3.setObjectName("widget3");
        gridLayout1 = new QGridLayout(widget3);
        gridLayout1.setObjectName("gridLayout1");
        gridLayout1.setContentsMargins(0, 0, 0, 0);
        tableWidget = new QTableWidget(widget3);
        tableWidget.setObjectName("tableWidget");
        tableWidget.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.WheelFocus);

        gridLayout1.addWidget(tableWidget, 0, 0, 1, 1);


        gridLayout.addWidget(widget3, 0, 1, 1, 1);

        retranslateUi(TreeWindow);

        TreeWindow.connectSlotsByName();
    } // setupUi

    void retranslateUi(QWidget TreeWindow)
    {
        TreeWindow.setWindowTitle("");
        treeWidget.headerItem().setText(0, "");
        tableWidget.clear();
        tableWidget.setColumnCount(0);
        tableWidget.setRowCount(0);
    } // retranslateUi

}

