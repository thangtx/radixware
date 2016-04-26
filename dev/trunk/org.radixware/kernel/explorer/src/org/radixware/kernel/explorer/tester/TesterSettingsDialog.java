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

package org.radixware.kernel.explorer.tester;

import com.trolltech.qt.core.QDir;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QSettings.Format;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QLayout.SizeConstraint;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QToolBar;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.ExplorerSettings;


public class TesterSettingsDialog extends ExplorerDialog {

    public static final class Icons extends ExplorerIcon.CommonOperations {

        private Icons(final String fileName) {
            super(fileName, true);
        }
        public static final Icons IMPORT_SETTINGS = new Icons("classpath:images/settings_import.svg");
        public static final Icons EXPORT_SETTINGS = new Icons("classpath:images/settings_export.svg");
    }
    //settings keys
    static final String INS_COUNT = "InsertsCount";
    static final String FLT_COUNT = "FiltersCount";
    static final String PGS_COUNT = "PagesCount";
    //static final String PROPDLG_COUNT = "PropDlgsCount";
    static final String TEST_INS = "TestInsertsBool";
    static final String TEST_FLT = "TestFiltersBool";
    static final String TEST_PGS = "TestPagesBool";
    static final String TEST_PROPDLG = "TestPropDlgBool";
    static final String TEST_CREATION = "TestCreationDialogBool";
    static final String OPN_TIME = "OpeningTimeLimit";
    static final String CLS_TIME = "ClosingTimeLimit";
    static final String INS_TIME = "InsertionTimeLimit";
    static final String FLT_TIME = "FiltersTimeLimit";
    static final String PGS_TIME = "PagesTimeLimit";
    static final String PROPDLG_TIME = "PropDialogsTimeLimit";
    static final String CREATION_TIME = "CreationDialogTimeLimit";
    static final String dialogKey = "TesterDialog-SettingsDialog";
    private static final String GEOMETRY_KEY = "Geometry";
    private TestsOptions options;
    private TesterSettingsTableManager tableManager;
    private QMainWindow window = new QMainWindow();
    private QPushButton pbExportSettings = new QPushButton(this);
    private QPushButton pbImportSettings = new QPushButton(this);

    public TesterSettingsDialog(IClientEnvironment environment, QWidget parent, TestsOptions options) {
        super(environment, parent);
        this.setWindowIcon(ExplorerIcon.getQIcon(TesterWindow.Icons.SETTINGS));

        QToolBar tools = new QToolBar(Application.translate("TesterDialog", "Tool Bar"), window);
        tools.setFloatable(false);
        tools.setObjectName("MainToolBar");
        window.addToolBar(tools);

        pbExportSettings.setToolTip(Application.translate("TesterDialog", "Export Tester Settings"));
        pbExportSettings.clicked.connect(this, "exportSettings()");
        pbExportSettings.setIcon(ExplorerIcon.getQIcon(Icons.EXPORT_SETTINGS));
        pbImportSettings.setToolTip(Application.translate("TesterDialog", "Import Tester Settings"));
        pbImportSettings.clicked.connect(this, "importSettings()");
        pbImportSettings.setIcon(ExplorerIcon.getQIcon(Icons.IMPORT_SETTINGS));

        tools.addWidget(pbExportSettings);
        tools.addWidget(pbImportSettings);

        this.options = options;

        setWindowTitle(Application.translate("TesterDialog", "Test Settings"));
        QVBoxLayout layout = dialogLayout();
        QTableWidget table = setupTable();

        window.setCentralWidget(table);
        window.adjustSize();
        layout.addWidget(window);

        QPushButton button = new QPushButton(Application.translate("TesterDialog", "Close"));
        button.setIcon(ExplorerIcon.getQIcon(ClientIcon.Dialog.EXIT));
        button.clicked.connect(this, "close()");
        layout.addWidget(button);
        layout.setAlignment(button, AlignmentFlag.AlignRight);
        layout.setContentsMargins(2, 2, 2, 2);

        layout.setSizeConstraint(SizeConstraint.SetFixedSize);
    }

    @SuppressWarnings("unused")
    private void exportSettings() {
        QFileDialog dialog = new QFileDialog(this, tr(Application.translate("TesterDialog","Export Tester Settings")), QDir.homePath());
        dialog.setFileMode(QFileDialog.FileMode.AnyFile);
        dialog.setAcceptMode(QFileDialog.AcceptMode.AcceptSave);

        if (QDialog.DialogCode.resolve(dialog.exec()).equals(QDialog.DialogCode.Accepted)) {
            String fileName = dialog.selectedFiles().get(0);
            exportSettings(fileName);
        }
    }

    public void exportSettings(String fileName) {
        ExplorerSettings settings = new ExplorerSettings(getEnvironment(), fileName, Format.IniFormat);
        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(TesterWindow.mainTrKey);
        settings.beginGroup(dialogKey);

        exportToExplorerSettings(settings, options);

        settings.endGroup();
        settings.endGroup();
        settings.endGroup();
    }

    static void exportToExplorerSettings(ExplorerSettings settings, TestsOptions testsOptions) {
        settings.writeBoolean(TEST_INS, testsOptions.testInserts);
        settings.writeBoolean(TEST_FLT, testsOptions.testFilters);
        settings.writeBoolean(TEST_PGS, testsOptions.testPages);
        settings.writeBoolean(TEST_PROPDLG, testsOptions.testPropDialog);
        settings.writeBoolean(TEST_CREATION, testsOptions.testCreationDialog);

        settings.writeString(INS_COUNT, String.valueOf(testsOptions.inserts));
        settings.writeString(FLT_COUNT, String.valueOf(testsOptions.filtersCount));
        settings.writeString(PGS_COUNT, String.valueOf(testsOptions.pagesCount));

        settings.writeString(OPN_TIME, String.valueOf(testsOptions.openingTimeBoundary));
        settings.writeString(INS_TIME, String.valueOf(testsOptions.insertsTimeBoundary));
        settings.writeString(FLT_TIME, String.valueOf(testsOptions.filtersTimeBoundary));
        settings.writeString(PGS_TIME, String.valueOf(testsOptions.pagesTimeBoundary));
        settings.writeString(CLS_TIME, String.valueOf(testsOptions.closingTimeBoundary));
        settings.writeString(PROPDLG_TIME, String.valueOf(testsOptions.propDialogTimeBoundary));
        settings.writeString(CREATION_TIME, String.valueOf(testsOptions.creationDialogTimeBoundary));

    }

    @SuppressWarnings("unused")
    private void importSettings() {
        QFileDialog dialog = new QFileDialog(this, tr(Application.translate("TesterDialog","Import Tester Settings")), QDir.homePath());
        dialog.setAcceptMode(QFileDialog.AcceptMode.AcceptOpen);
        dialog.setFileMode(QFileDialog.FileMode.ExistingFile);

        if (QDialog.DialogCode.resolve(dialog.exec()).equals(QDialog.DialogCode.Accepted)) {
            String fileName = dialog.selectedFiles().get(0);
            importSettings(fileName);
        }
    }

    public void importSettings(String fileName) {
        ExplorerSettings settings = new ExplorerSettings(getEnvironment(), fileName, Format.IniFormat);
        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(TesterWindow.mainTrKey);
        settings.beginGroup(dialogKey);

        tableManager.importSettings(settings);

        settings.endGroup();
        settings.endGroup();
        settings.endGroup();

    }

    private QTableWidget setupTable() {
        QTableWidget table = new QTableWidget(this) {
            @Override
            protected void mouseDoubleClickEvent(QMouseEvent event) {
                QModelIndex current = currentIndex();
                if (current != null && current.column() != 0) {
                    super.mouseDoubleClickEvent(event);
                }
            }

            @Override
            public QSize sizeHint() {
                int width = 0;
                for (int column = 0; column < columnCount(); column++) {
                    width += horizontalHeader().sectionSize(column);
                }
                int height = 10;
                for (int row = 0; row < rowCount(); row++) {
                    height += rowHeight(row);
                }
                height += horizontalHeader().height();
                width += frameWidth();
                width += getContentsMargins().left + getContentsMargins().right;
                width += 5;
                return new QSize(width, height);
            }
        };
        tableManager = new TesterSettingsTableManager(getEnvironment(), table, options);
        return table;
    }

    @Override
    protected void closeEvent(QCloseEvent event) {
        saveTestOptionsToConfig();
        super.closeEvent(event);
    }

    private void saveTestOptionsToConfig() {
        ExplorerSettings settings = (ExplorerSettings) getEnvironment().getConfigStore();
        try {
            settings.beginGroup(SettingNames.SYSTEM);
            settings.beginGroup(TesterWindow.mainTrKey);
            settings.beginGroup(TesterSettingsDialog.dialogKey);
            exportToExplorerSettings(settings, options);
        } finally {
            settings.endGroup();
            settings.endGroup();
            settings.endGroup();
        }
    }
}
