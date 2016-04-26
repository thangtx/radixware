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
** Form generated from reading ui file 'ConnectionsManager.jui'
**
** Created: Thu Apr 14 18:38:44 2011
**      by: Qt User Interface Compiler version 4.5.2
**
** WARNING! All changes made in this file will be lost when recompiling ui file!
********************************************************************************/

package org.radixware.kernel.explorer.dialogs;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

public class Ui_ConnectionsDialog implements com.trolltech.qt.QUiForm<QDialog>
{
    public QWidget contentsWidget;
    public QHBoxLayout hboxLayout;
    public QListWidget connectionsList;
    public QWidget commandsWidget;
    public QVBoxLayout vboxLayout;
    public QToolButton addBtn;
    public QToolButton cloneBtn;
    public QToolButton editorBtn;
    public QToolButton removeBtn;
    public QSpacerItem spacerItem;

    public Ui_ConnectionsDialog() { super(); }

    public void setupUi(QDialog ConnectionsDialog)
    {
        ConnectionsDialog.setObjectName("ConnectionsDialog");
        ConnectionsDialog.resize(new QSize(483, 269).expandedTo(ConnectionsDialog.minimumSizeHint()));
        contentsWidget = new QWidget(ConnectionsDialog);
        contentsWidget.setObjectName("contentsWidget");
        contentsWidget.setGeometry(new QRect(-40, 10, 378, 150));
        hboxLayout = new QHBoxLayout(contentsWidget);
        hboxLayout.setMargin(0);
        hboxLayout.setObjectName("hboxLayout");
        connectionsList = new QListWidget(contentsWidget);
        connectionsList.setObjectName("connectionsList");
        connectionsList.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.WheelFocus);
        connectionsList.setFrameShape(com.trolltech.qt.gui.QFrame.Shape.WinPanel);
        connectionsList.setFrameShadow(com.trolltech.qt.gui.QFrame.Shadow.Sunken);

        hboxLayout.addWidget(connectionsList);

        commandsWidget = new QWidget(contentsWidget);
        commandsWidget.setObjectName("commandsWidget");
        vboxLayout = new QVBoxLayout(commandsWidget);
        vboxLayout.setObjectName("vboxLayout");
        vboxLayout.setContentsMargins(0, -1, 0, 0);
        addBtn = new QToolButton(commandsWidget);
        addBtn.setObjectName("addBtn");
        addBtn.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.TabFocus);
        addBtn.setIcon(new QIcon(new QPixmap("../../../../../images/add.svg")));
        addBtn.setIconSize(new QSize(20, 20));

        vboxLayout.addWidget(addBtn);

        cloneBtn = new QToolButton(commandsWidget);
        cloneBtn.setObjectName("cloneBtn");
        cloneBtn.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.TabFocus);
        cloneBtn.setIconSize(new QSize(20, 20));

        vboxLayout.addWidget(cloneBtn);

        editorBtn = new QToolButton(commandsWidget);
        editorBtn.setObjectName("editorBtn");
        editorBtn.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.TabFocus);
        editorBtn.setIcon(new QIcon(new QPixmap("../../../../../images/edit.png")));
        editorBtn.setIconSize(new QSize(20, 20));

        vboxLayout.addWidget(editorBtn);

        removeBtn = new QToolButton(commandsWidget);
        removeBtn.setObjectName("removeBtn");
        removeBtn.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.TabFocus);
        removeBtn.setIcon(new QIcon(new QPixmap("../../../../../images/delete.svg")));
        removeBtn.setIconSize(new QSize(20, 20));
        removeBtn.setArrowType(com.trolltech.qt.core.Qt.ArrowType.NoArrow);

        vboxLayout.addWidget(removeBtn);

        spacerItem = new QSpacerItem(20, 40, com.trolltech.qt.gui.QSizePolicy.Policy.Minimum, com.trolltech.qt.gui.QSizePolicy.Policy.Expanding);

        vboxLayout.addItem(spacerItem);


        hboxLayout.addWidget(commandsWidget);

        retranslateUi(ConnectionsDialog);

        ConnectionsDialog.connectSlotsByName();
    } // setupUi

    void retranslateUi(QDialog ConnectionsDialog)
    {
        ConnectionsDialog.setWindowTitle(com.trolltech.qt.core.QCoreApplication.translate("ConnectionsDialog", "Connections Manager", null));
        addBtn.setToolTip(com.trolltech.qt.core.QCoreApplication.translate("ConnectionsDialog", "Create Connection", null));
        addBtn.setText("");
        addBtn.setShortcut(com.trolltech.qt.core.QCoreApplication.translate("ConnectionsDialog", "Ins", null));
        cloneBtn.setToolTip(com.trolltech.qt.core.QCoreApplication.translate("ConnectionsDialog", "Create a Copy of Connection", null));
        cloneBtn.setText("");
        editorBtn.setToolTip(com.trolltech.qt.core.QCoreApplication.translate("ConnectionsDialog", "Edit Connection", null));
        editorBtn.setText("");
        removeBtn.setToolTip(com.trolltech.qt.core.QCoreApplication.translate("ConnectionsDialog", "Delete Connection", null));
        removeBtn.setText("");
        removeBtn.setShortcut(com.trolltech.qt.core.QCoreApplication.translate("ConnectionsDialog", "Del", null));
    } // retranslateUi

}

