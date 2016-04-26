/********************************************************************************
** Form generated from reading ui file 'DateTimeDialog.jui'
**
** Created: Tue Aug 18 16:49:56 2015
**      by: Qt User Interface Compiler version 4.5.2
**
** WARNING! All changes made in this file will be lost when recompiling ui file!
********************************************************************************/

package org.radixware.kernel.explorer.dialogs;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

public class Ui_DateTimeDialog implements com.trolltech.qt.QUiForm<QDialog>
{
    public QGridLayout gridLayout;
    public QGroupBox gbDate;
    public QHBoxLayout hboxLayout;
    public QCalendarWidget calendarWidget;
    public QGroupBox gbTime;
    public QHBoxLayout horizontalLayout;
    public QVBoxLayout vboxLayout;
    public QSpacerItem spacerItem;
    public QDialogButtonBox buttonBox;
    public QPushButton pushButtonClear;
    public QPushButton pushButtonNow;
    public QPushButton pushButtonReset;
    public QSpacerItem spacerItem1;

    public Ui_DateTimeDialog() { super(); }

    public void setupUi(QDialog DateTimeDialog)
    {
        DateTimeDialog.setObjectName("DateTimeDialog");
        DateTimeDialog.resize(new QSize(405, 314).expandedTo(DateTimeDialog.minimumSizeHint()));
        gridLayout = new QGridLayout(DateTimeDialog);
        gridLayout.setObjectName("gridLayout");
        gbDate = new QGroupBox(DateTimeDialog);
        gbDate.setObjectName("gbDate");
        gbDate.setFlat(false);
        gbDate.setCheckable(false);
        hboxLayout = new QHBoxLayout(gbDate);
        hboxLayout.setObjectName("hboxLayout");
        calendarWidget = new QCalendarWidget(gbDate);
        calendarWidget.setObjectName("calendarWidget");
        calendarWidget.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.StrongFocus);

        hboxLayout.addWidget(calendarWidget);


        gridLayout.addWidget(gbDate, 1, 0, 2, 1);

        gbTime = new QGroupBox(DateTimeDialog);
        gbTime.setObjectName("gbTime");
        horizontalLayout = new QHBoxLayout(gbTime);
        horizontalLayout.setObjectName("horizontalLayout");

        gridLayout.addWidget(gbTime, 3, 0, 1, 1);

        vboxLayout = new QVBoxLayout();
        vboxLayout.setMargin(0);
        vboxLayout.setObjectName("vboxLayout");
        spacerItem = new QSpacerItem(20, 40, com.trolltech.qt.gui.QSizePolicy.Policy.Minimum, com.trolltech.qt.gui.QSizePolicy.Policy.Expanding);

        vboxLayout.addItem(spacerItem);

        buttonBox = new QDialogButtonBox(DateTimeDialog);
        buttonBox.setObjectName("buttonBox");
        QSizePolicy sizePolicy = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Expanding, com.trolltech.qt.gui.QSizePolicy.Policy.Preferred);
        sizePolicy.setHorizontalStretch((byte)0);
        sizePolicy.setVerticalStretch((byte)0);
        sizePolicy.setHeightForWidth(buttonBox.sizePolicy().hasHeightForWidth());
        buttonBox.setSizePolicy(sizePolicy);
        buttonBox.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.TabFocus);
        buttonBox.setOrientation(com.trolltech.qt.core.Qt.Orientation.Vertical);
        buttonBox.setStandardButtons(com.trolltech.qt.gui.QDialogButtonBox.StandardButton.createQFlags(com.trolltech.qt.gui.QDialogButtonBox.StandardButton.Cancel,com.trolltech.qt.gui.QDialogButtonBox.StandardButton.Ok));
        buttonBox.setCenterButtons(true);

        vboxLayout.addWidget(buttonBox);

        pushButtonClear = new QPushButton(DateTimeDialog);
        pushButtonClear.setObjectName("pushButtonClear");
        pushButtonClear.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.StrongFocus);

        vboxLayout.addWidget(pushButtonClear);

        pushButtonNow = new QPushButton(DateTimeDialog);
        pushButtonNow.setObjectName("pushButtonNow");
        pushButtonNow.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.StrongFocus);

        vboxLayout.addWidget(pushButtonNow);

        pushButtonReset = new QPushButton(DateTimeDialog);
        pushButtonReset.setObjectName("pushButtonReset");
        pushButtonReset.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.StrongFocus);

        vboxLayout.addWidget(pushButtonReset);

        spacerItem1 = new QSpacerItem(20, 40, com.trolltech.qt.gui.QSizePolicy.Policy.Minimum, com.trolltech.qt.gui.QSizePolicy.Policy.Expanding);

        vboxLayout.addItem(spacerItem1);


        gridLayout.addLayout(vboxLayout, 1, 1, 2, 1);

        retranslateUi(DateTimeDialog);
        buttonBox.accepted.connect(DateTimeDialog, "accept()");
        buttonBox.rejected.connect(DateTimeDialog, "reject()");

        DateTimeDialog.connectSlotsByName();
    } // setupUi

    void retranslateUi(QDialog DateTimeDialog)
    {
        DateTimeDialog.setWindowTitle(com.trolltech.qt.core.QCoreApplication.translate("DateTimeDialog", "Date and Time", null));
        gbDate.setTitle(com.trolltech.qt.core.QCoreApplication.translate("DateTimeDialog", "Date", null));
        gbTime.setTitle(com.trolltech.qt.core.QCoreApplication.translate("DateTimeDialog", "Time", null));
        pushButtonClear.setToolTip(com.trolltech.qt.core.QCoreApplication.translate("DateTimeDialog", "Clear Value and Close Dialog", null));
        pushButtonClear.setText(com.trolltech.qt.core.QCoreApplication.translate("DateTimeDialog", "Clea&r", null));
        pushButtonNow.setToolTip(com.trolltech.qt.core.QCoreApplication.translate("DateTimeDialog", "Set Current Date/Time", null));
        pushButtonNow.setText(com.trolltech.qt.core.QCoreApplication.translate("DateTimeDialog", "&Now", null));
        pushButtonReset.setToolTip(com.trolltech.qt.core.QCoreApplication.translate("DateTimeDialog", "Reset Current Value to Initial", null));
        pushButtonReset.setText(com.trolltech.qt.core.QCoreApplication.translate("DateTimeDialog", "&Reset", null));
    } // retranslateUi

}

