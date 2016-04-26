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
** Form generated from reading ui file 'FindAndReplaceDialog.jui'
**
** Created: Wed Apr 17 14:38:51 2013
**      by: Qt User Interface Compiler version 4.5.2
**
** WARNING! All changes made in this file will be lost when recompiling ui file!
********************************************************************************/

package org.radixware.kernel.explorer.dialogs;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

public class Ui_FindAndReplaceDialog implements com.trolltech.qt.QUiForm<QDialog>
{
    public QHBoxLayout horizontalLayout;
    public QVBoxLayout loContent;
    public QGridLayout loGrid;
    public QLabel lbFindWhat;
    public QLineEdit leFindWhat;
    public QLabel lbReplaceWith;
    public QLineEdit leReplaceWith;
    public QHBoxLayout loOptions;
    public QGroupBox gbOptions;
    public QVBoxLayout _3;
    public QCheckBox checkBoxCase;
    public QCheckBox checkBoxWhole;
    public QGroupBox gbDirection;
    public QVBoxLayout _4;
    public QRadioButton radioForward;
    public QRadioButton radioBackward;
    public QVBoxLayout loButtons;
    public QPushButton pbtFind;
    public QPushButton pbReplace;
    public QPushButton pbReplaceAll;
    public QPushButton pbClose;
    public QSpacerItem verticalSpacer;

    public Ui_FindAndReplaceDialog() { super(); }

    public void setupUi(QDialog FindAndReplaceDialog)
    {
        FindAndReplaceDialog.setObjectName("FindAndReplaceDialog");
        FindAndReplaceDialog.resize(new QSize(340, 164).expandedTo(FindAndReplaceDialog.minimumSizeHint()));
        QSizePolicy sizePolicy = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Preferred, com.trolltech.qt.gui.QSizePolicy.Policy.Maximum);
        sizePolicy.setHorizontalStretch((byte)0);
        sizePolicy.setVerticalStretch((byte)0);
        sizePolicy.setHeightForWidth(FindAndReplaceDialog.sizePolicy().hasHeightForWidth());
        FindAndReplaceDialog.setSizePolicy(sizePolicy);
        FindAndReplaceDialog.setMinimumSize(new QSize(340, 0));
        FindAndReplaceDialog.setMaximumSize(new QSize(540, 164));
        FindAndReplaceDialog.setBaseSize(new QSize(340, 0));
        horizontalLayout = new QHBoxLayout(FindAndReplaceDialog);
        horizontalLayout.setObjectName("horizontalLayout");
        loContent = new QVBoxLayout();
        loContent.setObjectName("loContent");
        loGrid = new QGridLayout();
        loGrid.setObjectName("loGrid");
        lbFindWhat = new QLabel(FindAndReplaceDialog);
        lbFindWhat.setObjectName("lbFindWhat");

        loGrid.addWidget(lbFindWhat, 0, 0, 1, 1);

        leFindWhat = new QLineEdit(FindAndReplaceDialog);
        leFindWhat.setObjectName("leFindWhat");
        leFindWhat.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.StrongFocus);

        loGrid.addWidget(leFindWhat, 0, 1, 1, 1);

        lbReplaceWith = new QLabel(FindAndReplaceDialog);
        lbReplaceWith.setObjectName("lbReplaceWith");

        loGrid.addWidget(lbReplaceWith, 1, 0, 1, 1);

        leReplaceWith = new QLineEdit(FindAndReplaceDialog);
        leReplaceWith.setObjectName("leReplaceWith");
        leReplaceWith.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.StrongFocus);

        loGrid.addWidget(leReplaceWith, 1, 1, 1, 1);


        loContent.addLayout(loGrid);

        loOptions = new QHBoxLayout();
        loOptions.setMargin(0);
        loOptions.setObjectName("loOptions");
        gbOptions = new QGroupBox(FindAndReplaceDialog);
        gbOptions.setObjectName("gbOptions");
        _3 = new QVBoxLayout(gbOptions);
        _3.setSpacing(2);
        _3.setObjectName("_3");
        _3.setContentsMargins(-1, 3, -1, 3);
        checkBoxCase = new QCheckBox(gbOptions);
        checkBoxCase.setObjectName("checkBoxCase");

        _3.addWidget(checkBoxCase);

        checkBoxWhole = new QCheckBox(gbOptions);
        checkBoxWhole.setObjectName("checkBoxWhole");

        _3.addWidget(checkBoxWhole);


        loOptions.addWidget(gbOptions);

        gbDirection = new QGroupBox(FindAndReplaceDialog);
        gbDirection.setObjectName("gbDirection");
        _4 = new QVBoxLayout(gbDirection);
        _4.setSpacing(2);
        _4.setObjectName("_4");
        _4.setContentsMargins(9, 3, 9, 3);
        radioForward = new QRadioButton(gbDirection);
        radioForward.setObjectName("radioForward");
        radioForward.setChecked(true);

        _4.addWidget(radioForward);

        radioBackward = new QRadioButton(gbDirection);
        radioBackward.setObjectName("radioBackward");

        _4.addWidget(radioBackward);


        loOptions.addWidget(gbDirection);


        loContent.addLayout(loOptions);


        horizontalLayout.addLayout(loContent);

        loButtons = new QVBoxLayout();
        loButtons.setObjectName("loButtons");
        pbtFind = new QPushButton(FindAndReplaceDialog);
        pbtFind.setObjectName("pbtFind");
        QSizePolicy sizePolicy1 = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Minimum, com.trolltech.qt.gui.QSizePolicy.Policy.Fixed);
        sizePolicy1.setHorizontalStretch((byte)0);
        sizePolicy1.setVerticalStretch((byte)0);
        sizePolicy1.setHeightForWidth(pbtFind.sizePolicy().hasHeightForWidth());
        pbtFind.setSizePolicy(sizePolicy1);
        pbtFind.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.StrongFocus);
        pbtFind.setDefault(true);

        loButtons.addWidget(pbtFind);

        pbReplace = new QPushButton(FindAndReplaceDialog);
        pbReplace.setObjectName("pbReplace");
        QSizePolicy sizePolicy2 = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Minimum, com.trolltech.qt.gui.QSizePolicy.Policy.Fixed);
        sizePolicy2.setHorizontalStretch((byte)0);
        sizePolicy2.setVerticalStretch((byte)0);
        sizePolicy2.setHeightForWidth(pbReplace.sizePolicy().hasHeightForWidth());
        pbReplace.setSizePolicy(sizePolicy2);
        pbReplace.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.StrongFocus);

        loButtons.addWidget(pbReplace);

        pbReplaceAll = new QPushButton(FindAndReplaceDialog);
        pbReplaceAll.setObjectName("pbReplaceAll");
        QSizePolicy sizePolicy3 = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Minimum, com.trolltech.qt.gui.QSizePolicy.Policy.Fixed);
        sizePolicy3.setHorizontalStretch((byte)0);
        sizePolicy3.setVerticalStretch((byte)0);
        sizePolicy3.setHeightForWidth(pbReplaceAll.sizePolicy().hasHeightForWidth());
        pbReplaceAll.setSizePolicy(sizePolicy3);
        pbReplaceAll.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.StrongFocus);

        loButtons.addWidget(pbReplaceAll);

        pbClose = new QPushButton(FindAndReplaceDialog);
        pbClose.setObjectName("pbClose");
        QSizePolicy sizePolicy4 = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Minimum, com.trolltech.qt.gui.QSizePolicy.Policy.Fixed);
        sizePolicy4.setHorizontalStretch((byte)0);
        sizePolicy4.setVerticalStretch((byte)0);
        sizePolicy4.setHeightForWidth(pbClose.sizePolicy().hasHeightForWidth());
        pbClose.setSizePolicy(sizePolicy4);
        pbClose.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.StrongFocus);

        loButtons.addWidget(pbClose);

        verticalSpacer = new QSpacerItem(0, 0, com.trolltech.qt.gui.QSizePolicy.Policy.Minimum, com.trolltech.qt.gui.QSizePolicy.Policy.Expanding);

        loButtons.addItem(verticalSpacer);


        horizontalLayout.addLayout(loButtons);

        lbFindWhat.setBuddy(leFindWhat);
        lbReplaceWith.setBuddy(leReplaceWith);
        retranslateUi(FindAndReplaceDialog);
        pbClose.clicked.connect(FindAndReplaceDialog, "reject()");

        FindAndReplaceDialog.connectSlotsByName();
    } // setupUi

    void retranslateUi(QDialog FindAndReplaceDialog)
    {
        FindAndReplaceDialog.setWindowTitle(com.trolltech.qt.core.QCoreApplication.translate("FindAndReplaceDialog", "Find", null));
        lbFindWhat.setText(com.trolltech.qt.core.QCoreApplication.translate("FindAndReplaceDialog", "&Find What:", null));
        lbReplaceWith.setText(com.trolltech.qt.core.QCoreApplication.translate("FindAndReplaceDialog", "Rep&lace With:", null));
        gbOptions.setTitle(com.trolltech.qt.core.QCoreApplication.translate("FindAndReplaceDialog", "Options", null));
        checkBoxCase.setText(com.trolltech.qt.core.QCoreApplication.translate("FindAndReplaceDialog", "&Match Case", null));
        checkBoxWhole.setText(com.trolltech.qt.core.QCoreApplication.translate("FindAndReplaceDialog", "&Whole Word", null));
        gbDirection.setTitle(com.trolltech.qt.core.QCoreApplication.translate("FindAndReplaceDialog", "Direction", null));
        radioForward.setText(com.trolltech.qt.core.QCoreApplication.translate("FindAndReplaceDialog", "F&orward", null));
        radioBackward.setText(com.trolltech.qt.core.QCoreApplication.translate("FindAndReplaceDialog", "&Backward", null));
        pbtFind.setText(com.trolltech.qt.core.QCoreApplication.translate("FindAndReplaceDialog", "Fin&d", null));
        pbReplace.setText(com.trolltech.qt.core.QCoreApplication.translate("FindAndReplaceDialog", "&Replace", null));
        pbReplaceAll.setText(com.trolltech.qt.core.QCoreApplication.translate("FindAndReplaceDialog", "Replace &All", null));
        pbClose.setText(com.trolltech.qt.core.QCoreApplication.translate("FindAndReplaceDialog", "&Close", null));
    } // retranslateUi

}

