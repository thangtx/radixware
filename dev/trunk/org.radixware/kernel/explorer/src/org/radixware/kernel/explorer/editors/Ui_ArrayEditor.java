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
** Form generated from reading ui file 'ArrayEditor.jui'
**
** Created: ?? ????. 27 14:17:42 2012
**      by: Qt User Interface Compiler version 4.5.2
**
** WARNING! All changes made in this file will be lost when recompiling ui file!
********************************************************************************/
package org.radixware.kernel.explorer.editors;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

public class Ui_ArrayEditor implements com.trolltech.qt.QUiForm<QWidget>
{
    public QWidget contentWidget;
    public QVBoxLayout vboxLayout;
    public QFrame frameArray;
    public QHBoxLayout hboxLayout;
    public QStackedWidget stackedWidget;
    public QWidget page;
    public QVBoxLayout vboxLayout1;
    public QTableWidget valuesTable;
    public QWidget errorArea;
    public QHBoxLayout errorAreaLayout;
    public QLabel errorIcon;
    public QLabel errorMessage;
    public QWidget page_2;
    public QVBoxLayout vboxLayout2;
    public QLabel notDefinedLabel;
    public QWidget buttonsWidged;
    public QVBoxLayout vboxLayout3;
    public QToolButton addBtn;
    public QToolButton delBtn;
    public QToolButton clearBtn;
    public QToolButton defineBtn;
    public QWidget deviderWidget;
    public QToolButton upBtn;
    public QToolButton downBtn;
    public QSpacerItem verticalSpacer;

    public Ui_ArrayEditor() { super(); }

    public void setupUi(QWidget ArrayEditor)
    {
        ArrayEditor.setObjectName("ArrayEditor");
        ArrayEditor.resize(new QSize(315, 239).expandedTo(ArrayEditor.minimumSizeHint()));
        ArrayEditor.setMinimumSize(new QSize(0, 0));
        ArrayEditor.setContextMenuPolicy(com.trolltech.qt.core.Qt.ContextMenuPolicy.NoContextMenu);
        contentWidget = new QWidget(ArrayEditor);
        contentWidget.setObjectName("contentWidget");
        contentWidget.setGeometry(new QRect(10, 10, 297, 220));
        vboxLayout = new QVBoxLayout(contentWidget);
        vboxLayout.setMargin(0);
        vboxLayout.setObjectName("vboxLayout");
        frameArray = new QFrame(contentWidget);
        frameArray.setObjectName("frameArray");
        QSizePolicy sizePolicy = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Expanding, com.trolltech.qt.gui.QSizePolicy.Policy.Expanding);
        sizePolicy.setHorizontalStretch((byte)0);
        sizePolicy.setVerticalStretch((byte)0);
        sizePolicy.setHeightForWidth(frameArray.sizePolicy().hasHeightForWidth());
        frameArray.setSizePolicy(sizePolicy);
        frameArray.setFrameShape(com.trolltech.qt.gui.QFrame.Shape.Panel);
        frameArray.setFrameShadow(com.trolltech.qt.gui.QFrame.Shadow.Plain);
        frameArray.setLineWidth(0);
        hboxLayout = new QHBoxLayout(frameArray);
        hboxLayout.setMargin(0);
        hboxLayout.setObjectName("hboxLayout");
        stackedWidget = new QStackedWidget(frameArray);
        stackedWidget.setObjectName("stackedWidget");
        page = new QWidget();
        page.setObjectName("page");
        vboxLayout1 = new QVBoxLayout(page);
        vboxLayout1.setMargin(0);
        vboxLayout1.setObjectName("vboxLayout1");
        valuesTable = new QTableWidget(page);
        valuesTable.setObjectName("valuesTable");
        valuesTable.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.WheelFocus);

        vboxLayout1.addWidget(valuesTable);

        errorArea = new QWidget(page);
        errorArea.setObjectName("errorArea");
        errorAreaLayout = new QHBoxLayout(errorArea);
        errorAreaLayout.setObjectName("hboxLayout1");
        errorIcon = new QLabel(errorArea);
        errorIcon.setObjectName("errorIcon");

        errorAreaLayout.addWidget(errorIcon);

        errorMessage = new QLabel(errorArea);
        errorMessage.setObjectName("errorMessage");

        errorAreaLayout.addWidget(errorMessage);


        vboxLayout1.addWidget(errorArea);

        stackedWidget.addWidget(page);
        page_2 = new QWidget();
        page_2.setObjectName("page_2");
        vboxLayout2 = new QVBoxLayout(page_2);
        vboxLayout2.setMargin(0);
        vboxLayout2.setObjectName("vboxLayout2");
        notDefinedLabel = new QLabel(page_2);
        notDefinedLabel.setObjectName("notDefinedLabel");
        QFont font = new QFont();
        font.setPointSize(12);
        font.setBold(true);
        font.setWeight(75);
        notDefinedLabel.setFont(font);
        notDefinedLabel.setFrameShape(com.trolltech.qt.gui.QFrame.Shape.StyledPanel);
        notDefinedLabel.setFrameShadow(com.trolltech.qt.gui.QFrame.Shadow.Sunken);
        notDefinedLabel.setAlignment(com.trolltech.qt.core.Qt.AlignmentFlag.createQFlags(com.trolltech.qt.core.Qt.AlignmentFlag.AlignCenter));

        vboxLayout2.addWidget(notDefinedLabel);

        stackedWidget.addWidget(page_2);

        hboxLayout.addWidget(stackedWidget);

        buttonsWidged = new QWidget(frameArray);
        buttonsWidged.setObjectName("buttonsWidged");
        vboxLayout3 = new QVBoxLayout(buttonsWidged);
        vboxLayout3.setObjectName("vboxLayout3");
        vboxLayout3.setContentsMargins(-1, -1, 0, -1);
        addBtn = new QToolButton(buttonsWidged);
        addBtn.setObjectName("addBtn");
        addBtn.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.TabFocus);

        vboxLayout3.addWidget(addBtn);

        delBtn = new QToolButton(buttonsWidged);
        delBtn.setObjectName("delBtn");
        delBtn.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.TabFocus);

        vboxLayout3.addWidget(delBtn);

        clearBtn = new QToolButton(buttonsWidged);
        clearBtn.setObjectName("clearBtn");
        clearBtn.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.TabFocus);

        vboxLayout3.addWidget(clearBtn);

        defineBtn = new QToolButton(buttonsWidged);
        defineBtn.setObjectName("defineBtn");
        defineBtn.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.TabFocus);

        vboxLayout3.addWidget(defineBtn);

        deviderWidget = new QWidget(buttonsWidged);
        deviderWidget.setObjectName("deviderWidget");
        deviderWidget.setMinimumSize(new QSize(0, 35));
        deviderWidget.setMaximumSize(new QSize(16777215, 35));

        vboxLayout3.addWidget(deviderWidget);

        upBtn = new QToolButton(buttonsWidged);
        upBtn.setObjectName("upBtn");

        vboxLayout3.addWidget(upBtn);

        downBtn = new QToolButton(buttonsWidged);
        downBtn.setObjectName("downBtn");

        vboxLayout3.addWidget(downBtn);

        verticalSpacer = new QSpacerItem(20, 40, com.trolltech.qt.gui.QSizePolicy.Policy.Minimum, com.trolltech.qt.gui.QSizePolicy.Policy.Expanding);

        vboxLayout3.addItem(verticalSpacer);


        hboxLayout.addWidget(buttonsWidged);


        vboxLayout.addWidget(frameArray);

        retranslateUi(ArrayEditor);

        stackedWidget.setCurrentIndex(1);


        ArrayEditor.connectSlotsByName();
    } // setupUi

    void retranslateUi(QWidget ArrayEditor)
    {
        valuesTable.clear();
        valuesTable.setColumnCount(0);
        valuesTable.setRowCount(0);
        notDefinedLabel.setText(com.trolltech.qt.core.QCoreApplication.translate("ArrayEditor", "<Not defined>", null));
        addBtn.setText("");
        delBtn.setText("");
        clearBtn.setText("");
        defineBtn.setText("");
        upBtn.setText("");
        downBtn.setText("");
    } // retranslateUi

}

