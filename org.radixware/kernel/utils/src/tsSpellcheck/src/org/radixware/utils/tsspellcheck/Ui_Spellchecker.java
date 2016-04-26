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
** Form generated from reading ui file 'Spellchecker.jui'
**
** Created: Fri Mar 11 15:40:35 2011
**      by: Qt User Interface Compiler version 4.5.2
**
** WARNING! All changes made in this file will be lost when recompiling ui file!
********************************************************************************/

package org.radixware.utils.tsspellcheck;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

public class Ui_Spellchecker implements com.trolltech.qt.QUiForm<QMainWindow>
{
    public QAction action_Open;
    public QAction actionRefresh;
    public QWidget centralwidget;
    public QVBoxLayout verticalLayout;
    public QWidget contentWidget;
    public QVBoxLayout verticalLayout_4;
    public QSplitter splitter;
    public QListWidget lstSources;
    public QPlainTextEdit teSource;
    public QMenuBar menubar;
    public QStatusBar statusbar;
    public QToolBar ToolBar;
    public QDockWidget treeDockWidget;
    public QWidget dockWidgetContents;
    public QVBoxLayout verticalLayout_3;
    public QTreeWidget treeWidget;
    public QDockWidget translationDockWidget;
    public QWidget translationDockWidgetContents;
    public QVBoxLayout verticalLayout_2;
    public QLabel lbSourceText;
    public QLineEdit leSourceText;
    public QLabel lbTranslation;
    public QLineEdit leTranslation;

    public Ui_Spellchecker() { super(); }

    public void setupUi(QMainWindow Spellchecker)
    {
        Spellchecker.setObjectName("Spellchecker");
        Spellchecker.resize(new QSize(1000, 1014).expandedTo(Spellchecker.minimumSizeHint()));
        Spellchecker.setContextMenuPolicy(com.trolltech.qt.core.Qt.ContextMenuPolicy.NoContextMenu);
        action_Open = new QAction(Spellchecker);
        action_Open.setObjectName("action_Open");
        actionRefresh = new QAction(Spellchecker);
        actionRefresh.setObjectName("actionRefresh");
        actionRefresh.setEnabled(false);
        centralwidget = new QWidget(Spellchecker);
        centralwidget.setObjectName("centralwidget");
        verticalLayout = new QVBoxLayout(centralwidget);
        verticalLayout.setObjectName("verticalLayout");
        contentWidget = new QWidget(centralwidget);
        contentWidget.setObjectName("contentWidget");
        verticalLayout_4 = new QVBoxLayout(contentWidget);
        verticalLayout_4.setObjectName("verticalLayout_4");
        verticalLayout_4.setContentsMargins(0, 0, 1, 0);
        splitter = new QSplitter(contentWidget);
        splitter.setObjectName("splitter");
        splitter.setOrientation(com.trolltech.qt.core.Qt.Orientation.Vertical);
        splitter.setOpaqueResize(false);
        splitter.setChildrenCollapsible(false);
        lstSources = new QListWidget(splitter);
        lstSources.setObjectName("lstSources");
        QSizePolicy sizePolicy = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Expanding, com.trolltech.qt.gui.QSizePolicy.Policy.Maximum);
        sizePolicy.setHorizontalStretch((byte)0);
        sizePolicy.setVerticalStretch((byte)1);
        sizePolicy.setHeightForWidth(lstSources.sizePolicy().hasHeightForWidth());
        lstSources.setSizePolicy(sizePolicy);
        lstSources.setEditTriggers(com.trolltech.qt.gui.QAbstractItemView.EditTrigger.createQFlags(com.trolltech.qt.gui.QAbstractItemView.EditTrigger.NoEditTriggers));
        lstSources.setResizeMode(com.trolltech.qt.gui.QListView.ResizeMode.Fixed);
        splitter.addWidget(lstSources);
        teSource = new QPlainTextEdit(splitter);
        teSource.setObjectName("teSource");
        QSizePolicy sizePolicy1 = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Expanding, com.trolltech.qt.gui.QSizePolicy.Policy.MinimumExpanding);
        sizePolicy1.setHorizontalStretch((byte)0);
        sizePolicy1.setVerticalStretch((byte)5);
        sizePolicy1.setHeightForWidth(teSource.sizePolicy().hasHeightForWidth());
        teSource.setSizePolicy(sizePolicy1);
        teSource.setUndoRedoEnabled(false);
        teSource.setLineWrapMode(com.trolltech.qt.gui.QPlainTextEdit.LineWrapMode.NoWrap);
        teSource.setReadOnly(true);
        teSource.setTextInteractionFlags(com.trolltech.qt.core.Qt.TextInteractionFlag.createQFlags(com.trolltech.qt.core.Qt.TextInteractionFlag.TextSelectableByMouse));
        teSource.setCenterOnScroll(false);
        splitter.addWidget(teSource);

        verticalLayout_4.addWidget(splitter);


        verticalLayout.addWidget(contentWidget);

        Spellchecker.setCentralWidget(centralwidget);
        menubar = new QMenuBar(Spellchecker);
        menubar.setObjectName("menubar");
        menubar.setGeometry(new QRect(0, 0, 1000, 23));
        Spellchecker.setMenuBar(menubar);
        statusbar = new QStatusBar(Spellchecker);
        statusbar.setObjectName("statusbar");
        Spellchecker.setStatusBar(statusbar);
        ToolBar = new QToolBar(Spellchecker);
        ToolBar.setObjectName("ToolBar");
        Spellchecker.addToolBar(com.trolltech.qt.core.Qt.ToolBarArea.TopToolBarArea, ToolBar);
        treeDockWidget = new QDockWidget(Spellchecker);
        treeDockWidget.setObjectName("treeDockWidget");
        treeDockWidget.setFloating(false);
        treeDockWidget.setFeatures(com.trolltech.qt.gui.QDockWidget.DockWidgetFeature.createQFlags(com.trolltech.qt.gui.QDockWidget.DockWidgetFeature.DockWidgetFloatable,com.trolltech.qt.gui.QDockWidget.DockWidgetFeature.DockWidgetMovable));
        dockWidgetContents = new QWidget();
        dockWidgetContents.setObjectName("dockWidgetContents");
        verticalLayout_3 = new QVBoxLayout(dockWidgetContents);
        verticalLayout_3.setMargin(0);
        verticalLayout_3.setObjectName("verticalLayout_3");
        treeWidget = new QTreeWidget(dockWidgetContents);
        treeWidget.setObjectName("treeWidget");
        treeWidget.setHeaderHidden(true);

        verticalLayout_3.addWidget(treeWidget);

        treeDockWidget.setWidget(dockWidgetContents);
        Spellchecker.addDockWidget(com.trolltech.qt.core.Qt.DockWidgetArea.resolve(1), treeDockWidget);
        translationDockWidget = new QDockWidget(Spellchecker);
        translationDockWidget.setObjectName("translationDockWidget");
        QSizePolicy sizePolicy2 = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Preferred, com.trolltech.qt.gui.QSizePolicy.Policy.Fixed);
        sizePolicy2.setHorizontalStretch((byte)0);
        sizePolicy2.setVerticalStretch((byte)0);
        sizePolicy2.setHeightForWidth(translationDockWidget.sizePolicy().hasHeightForWidth());
        translationDockWidget.setSizePolicy(sizePolicy2);
        translationDockWidget.setFeatures(com.trolltech.qt.gui.QDockWidget.DockWidgetFeature.createQFlags(com.trolltech.qt.gui.QDockWidget.DockWidgetFeature.DockWidgetFloatable,com.trolltech.qt.gui.QDockWidget.DockWidgetFeature.DockWidgetMovable));
        translationDockWidgetContents = new QWidget();
        translationDockWidgetContents.setObjectName("translationDockWidgetContents");
        QSizePolicy sizePolicy3 = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Preferred, com.trolltech.qt.gui.QSizePolicy.Policy.Fixed);
        sizePolicy3.setHorizontalStretch((byte)0);
        sizePolicy3.setVerticalStretch((byte)0);
        sizePolicy3.setHeightForWidth(translationDockWidgetContents.sizePolicy().hasHeightForWidth());
        translationDockWidgetContents.setSizePolicy(sizePolicy3);
        verticalLayout_2 = new QVBoxLayout(translationDockWidgetContents);
        verticalLayout_2.setObjectName("verticalLayout_2");
        verticalLayout_2.setSizeConstraint(com.trolltech.qt.gui.QLayout.SizeConstraint.SetDefaultConstraint);
        lbSourceText = new QLabel(translationDockWidgetContents);
        lbSourceText.setObjectName("lbSourceText");
        QFont font = new QFont();
        font.setBold(true);
        font.setWeight(75);
        lbSourceText.setFont(font);

        verticalLayout_2.addWidget(lbSourceText);

        leSourceText = new QLineEdit(translationDockWidgetContents);
        leSourceText.setObjectName("leSourceText");
        leSourceText.setReadOnly(true);

        verticalLayout_2.addWidget(leSourceText);

        lbTranslation = new QLabel(translationDockWidgetContents);
        lbTranslation.setObjectName("lbTranslation");
        QFont font1 = new QFont();
        font1.setBold(true);
        font1.setWeight(75);
        lbTranslation.setFont(font1);

        verticalLayout_2.addWidget(lbTranslation);

        leTranslation = new QLineEdit(translationDockWidgetContents);
        leTranslation.setObjectName("leTranslation");
        leTranslation.setReadOnly(true);

        verticalLayout_2.addWidget(leTranslation);

        translationDockWidget.setWidget(translationDockWidgetContents);
        Spellchecker.addDockWidget(com.trolltech.qt.core.Qt.DockWidgetArea.resolve(8), translationDockWidget);

        ToolBar.addAction(action_Open);
        ToolBar.addAction(actionRefresh);
        retranslateUi(Spellchecker);

        Spellchecker.connectSlotsByName();
    } // setupUi

    void retranslateUi(QMainWindow Spellchecker)
    {
        Spellchecker.setWindowTitle(com.trolltech.qt.core.QCoreApplication.translate("Spellchecker", "Spellchecker", null));
        action_Open.setText(com.trolltech.qt.core.QCoreApplication.translate("Spellchecker", "&Open", null));
        action_Open.setToolTip(com.trolltech.qt.core.QCoreApplication.translate("Spellchecker", "Open Qt Translation File", null));
        action_Open.setShortcut(com.trolltech.qt.core.QCoreApplication.translate("Spellchecker", "Ctrl+O", null));
        actionRefresh.setText(com.trolltech.qt.core.QCoreApplication.translate("Spellchecker", "&Refresh", null));
        actionRefresh.setToolTip(com.trolltech.qt.core.QCoreApplication.translate("Spellchecker", "Refresh", null));
        actionRefresh.setShortcut(com.trolltech.qt.core.QCoreApplication.translate("Spellchecker", "Ctrl+R", null));
        treeDockWidget.setToolTip("");
        treeDockWidget.setAccessibleName("");
        treeDockWidget.setAccessibleDescription("");
        treeDockWidget.setWindowTitle(com.trolltech.qt.core.QCoreApplication.translate("Spellchecker", "Unknown words", null));
        treeWidget.headerItem().setText(0, "");
        translationDockWidget.setWindowTitle(com.trolltech.qt.core.QCoreApplication.translate("Spellchecker", "Translation", null));
        lbSourceText.setText(com.trolltech.qt.core.QCoreApplication.translate("Spellchecker", "Source text:", null));
        lbTranslation.setText(com.trolltech.qt.core.QCoreApplication.translate("Spellchecker", "Translation:", null));
    } // retranslateUi

}

