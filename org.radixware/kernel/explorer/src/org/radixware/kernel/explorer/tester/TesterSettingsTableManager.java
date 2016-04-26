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

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.CheckState;
import com.trolltech.qt.gui.QAbstractItemView.SelectionMode;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QTableWidgetItem;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.common.client.meta.mask.EditMaskTimeInterval;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskTimeInterval.Scale;
import org.radixware.kernel.explorer.editors.valeditors.ValIntEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValTimeIntervalEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerSettings;



class TesterSettingsTableManager {

    private final int usageColumn = 0;
    private final int typeColumn = 1;
    private final int timeColumn = 2;
    private final int countColumn = 3;
    private final int columnsCount = 4;
    private final int openinngRow = 0;
    private final int insertsRow = 1;
    private final int filtersRow = 2;
    private final int pagesRow = 3;
    private final int propDialogRow = 4;
    private final int creationDialogRow = 5;
    private final int closingRow = 6;
    private final int rowCount = 7;
    private QTableWidget table;
    private TestsOptions options;
    private EditMaskTimeInterval timeInterval;
    private boolean update = false;
    private IClientEnvironment environment;

    TesterSettingsTableManager(IClientEnvironment environment, QTableWidget table, TestsOptions options) {
        assert (table != null && options != null);
        this.environment = environment;
        this.table = table;
        this.options = options;
        this.timeInterval =
                new EditMaskTimeInterval(Scale.MILLIS.longValue(), "hh:mm:ss:zzzz", null, null);

        table.setSelectionMode(SelectionMode.SingleSelection);
        table.setItemDelegateForColumn(usageColumn, new CheckBoxItemDelegate(table, usageColumn));
        table.cellChanged.connect(this, "cellChanged(Integer, Integer)");
        table.itemDoubleClicked.connect(this, "doubleClicked(QTableWidgetItem)");
        table.setColumnCount(columnsCount);
        table.setRowCount(rowCount);
        List<String> heads = new ArrayList<String>();
        heads.add(Application.translate("TesterDialog", "Usage"));
        heads.add(Application.translate("TesterDialog", "Type of Test"));
        heads.add(Application.translate("TesterDialog", "Time Boundary"));
        heads.add(Application.translate("TesterDialog", "Quantity"));
        table.setHorizontalHeaderLabels(heads);

        update = true;
        createItemForOpening();
        createItemForInsertions();
        createItemForFilter();
        createItemForPages();
        createItemForClosing();
        createItemForPropDialog();
        createItemForCreationDialog();
        update = false;

        table.verticalHeader().setVisible(false);
        table.resizeRowsToContents();
        table.resizeColumnsToContents();
        table.setSizePolicy(Policy.Fixed, Policy.Fixed);
    }

    private void cellChanged(Integer r, Integer c) {
        if (c == usageColumn) {
            switch (r) {
                case insertsRow: {
                    onInsertionsUsageChange(table.item(insertsRow, usageColumn).checkState().equals(Qt.CheckState.Checked));
                }
                break;
                case filtersRow: {
                    onFiltersUsageChange(table.item(filtersRow, usageColumn).checkState().equals(Qt.CheckState.Checked));
                }
                break;
                case pagesRow: {
                    onPagesUsageChange(table.item(pagesRow, usageColumn).checkState().equals(Qt.CheckState.Checked));
                }
                break;
                case propDialogRow: {
                    onPropDialogUsageChange(table.item(propDialogRow, usageColumn).checkState().equals(Qt.CheckState.Checked));
                }
                break;
                case creationDialogRow: {
                    onCreationDialogUsageChange(table.item(creationDialogRow, usageColumn).checkState().equals(Qt.CheckState.Checked));
                }
                break;
            }
        }
    }

    private void doubleClicked(QTableWidgetItem item) {
        if (item.column() == typeColumn) {
            switch (item.row()) {
                case insertsRow: {
                    Boolean checkState = table.item(insertsRow, usageColumn).checkState().equals(Qt.CheckState.Checked);
                    table.item(insertsRow, usageColumn).setCheckState(!checkState ? Qt.CheckState.Checked : Qt.CheckState.Unchecked);
                    onInsertionsUsageChange(!checkState);
                }
                break;
                case filtersRow: {
                    Boolean checkState = table.item(filtersRow, usageColumn).checkState().equals(Qt.CheckState.Checked);
                    table.item(filtersRow, usageColumn).setCheckState(!checkState ? Qt.CheckState.Checked : Qt.CheckState.Unchecked);
                    onFiltersUsageChange(!checkState);
                }
                break;
                case pagesRow: {
                    Boolean checkState = table.item(pagesRow, usageColumn).checkState().equals(Qt.CheckState.Checked);
                    table.item(pagesRow, usageColumn).setCheckState(!checkState ? Qt.CheckState.Checked : Qt.CheckState.Unchecked);
                    onPagesUsageChange(!checkState);
                }
                break;
                case propDialogRow: {
                    Boolean checkState = table.item(propDialogRow, usageColumn).checkState().equals(Qt.CheckState.Checked);
                    table.item(propDialogRow, usageColumn).setCheckState(!checkState ? Qt.CheckState.Checked : Qt.CheckState.Unchecked);
                    onPropDialogUsageChange(!checkState);
                }
                break;
                case creationDialogRow: {
                    Boolean checkState = table.item(creationDialogRow, usageColumn).checkState().equals(Qt.CheckState.Checked);
                    table.item(creationDialogRow, usageColumn).setCheckState(!checkState ? Qt.CheckState.Checked : Qt.CheckState.Unchecked);
                    onCreationDialogUsageChange(!checkState);
                }
                break;
            }
        }
    }

    private QTableWidgetItem getLabel(String text) {
        QTableWidgetItem label = new QTableWidgetItem(text);
        label.setFlags(new Qt.ItemFlags(Qt.ItemFlag.ItemIsEnabled, Qt.ItemFlag.ItemIsSelectable));
        return label;
    }

    private ValTimeIntervalEditor getTimeEditor(long initValue) {
        ValTimeIntervalEditor editor = new ValTimeIntervalEditor(environment, table, timeInterval, false, false);
        editor.setValue(initValue);
        editor.changeStateForGrid();
        return editor;
    }

    private ValIntEditor getQuantityEditor(long initValue) {
        EditMaskInt mask = new EditMaskInt();
        mask.setNoValueStr(Application.translate("TesterDialog", "<Use All>"));
        mask.setMinValue(1);
        ValIntEditor editor = new ValIntEditor(environment, table, mask, false, false);
        if (initValue == -1) {
            editor.setValue(null);
        } else {
            editor.setValue(initValue);
        }
        return editor;
    }

    private QTableWidgetItem getUsageEditor(boolean initValue, boolean readonly) {
        QTableWidgetItem editor = new QTableWidgetItem();
        editor.setCheckState(initValue ? CheckState.Checked : CheckState.Unchecked);
        if (readonly) {
            editor.setFlags(new Qt.ItemFlags(Qt.ItemFlag.NoItemFlags));
        } else {
            editor.setFlags(new Qt.ItemFlags(Qt.ItemFlag.ItemIsEditable,
                    Qt.ItemFlag.ItemIsEnabled,
                    Qt.ItemFlag.ItemIsSelectable,
                    Qt.ItemFlag.ItemIsUserCheckable));
        }
        return editor;
    }

    private QTableWidgetItem getEmptyItem() {
        QTableWidgetItem editor = new QTableWidgetItem();
        editor.setFlags(new Qt.ItemFlags(Qt.ItemFlag.NoItemFlags));
        return editor;
    }

    private void createItemForOpening() {
        table.setItem(openinngRow, typeColumn, getLabel(TesterConstants.TEST_OPENING.getTitle()));
        ValTimeIntervalEditor editor = getTimeEditor(options.openingTimeBoundary);
        editor.valueChanged.connect(this, "onOpeningTimeChange(Object)");
        table.setCellWidget(openinngRow, timeColumn, editor);
        QTableWidgetItem countItem = new QTableWidgetItem("1");
        countItem.setFlags(new Qt.ItemFlags(Qt.ItemFlag.NoItemFlags));
        table.setItem(openinngRow, countColumn, countItem);
        table.setItem(openinngRow, usageColumn, getUsageEditor(true, true));
    }

    void importSettings(ExplorerSettings settings) {
        importTimeSettingsForKey(settings, TesterSettingsDialog.OPN_TIME, openinngRow);

        importCountSettingsForKey(settings, TesterSettingsDialog.TEST_INS, TesterSettingsDialog.INS_COUNT, insertsRow);
        importCountSettingsForKey(settings, TesterSettingsDialog.TEST_FLT, TesterSettingsDialog.FLT_COUNT, filtersRow);
        importCountSettingsForKey(settings, TesterSettingsDialog.TEST_PGS, TesterSettingsDialog.PGS_COUNT, pagesRow);

        Boolean testPropDlgs = settings.readBoolean(TesterSettingsDialog.TEST_PROPDLG, false);
        table.item(propDialogRow, usageColumn).setCheckState(testPropDlgs ? CheckState.Checked : CheckState.Unchecked);
        Boolean testCreationDlgs = settings.readBoolean(TesterSettingsDialog.TEST_CREATION, false);
        table.item(creationDialogRow, usageColumn).setCheckState(testCreationDlgs ? CheckState.Checked : CheckState.Unchecked);

        importTimeSettingsForKey(settings, TesterSettingsDialog.INS_TIME, insertsRow);
        importTimeSettingsForKey(settings, TesterSettingsDialog.FLT_TIME, filtersRow);
        importTimeSettingsForKey(settings, TesterSettingsDialog.PGS_TIME, pagesRow);
        importTimeSettingsForKey(settings, TesterSettingsDialog.PROPDLG_TIME, propDialogRow);

        importTimeSettingsForKey(settings, TesterSettingsDialog.CLS_TIME, closingRow);
    }

    private void importTimeSettingsForKey(ExplorerSettings settings, final String key, final int row) {
        if (settings.contains(key)) {
            String timeStr = settings.readString(key, "1000");
            Long time = Long.valueOf(timeStr);
            ((ValTimeIntervalEditor) table.cellWidget(row, timeColumn)).setValue(time);
        } else {
            ((ValTimeIntervalEditor) table.cellWidget(row, timeColumn)).setValue(Long.valueOf("1000"));
        }
    }

    private void importCountSettingsForKey(ExplorerSettings settings, final String usageKey, final String countKey, final int row) {
        if (settings.contains(usageKey)) {
            Boolean testInserts = settings.readBoolean(usageKey, false);
            importForSavedInSettingsTest(settings, countKey, row, testInserts);
        } else {
            importForNotSavedInSettingsTest(settings, countKey, row);
        }
    }

    private void importForSavedInSettingsTest(ExplorerSettings settings, final String key, final int row, final boolean test) {
        table.item(row, usageColumn).setCheckState(test ? CheckState.Checked : CheckState.Unchecked);
        if (!test) {
            ((ValIntEditor) table.cellWidget(row, countColumn)).setValue(Long.valueOf(0));
        } else {
            if (settings.contains(key)) {
                String countStr = settings.readString(key, null);
                Long count = Long.valueOf(countStr);
                ((ValIntEditor) table.cellWidget(row, countColumn)).setValue(count == -1 ? null : count);
            } else {
                ((ValIntEditor) table.cellWidget(row, countColumn)).setValue(null);
            }
        }
    }

    private void importForNotSavedInSettingsTest(ExplorerSettings settings, final String key, final int row) {
        if (settings.contains(key)) {
            String countStr = settings.readString(key, null);
            Long count = Long.valueOf(countStr);
            table.item(row, usageColumn).setCheckState(count != 0 ? CheckState.Checked : CheckState.Unchecked);
            ((ValIntEditor) table.cellWidget(row, countColumn)).setValue(count == -1 ? null : count);
        } else {
            table.item(row, usageColumn).setCheckState(CheckState.Unchecked);
            ((ValIntEditor) table.cellWidget(row, countColumn)).setValue(Long.valueOf(0));
        }
    }

    private void onOpeningTimeChange(Object value) {
        if (!update && value instanceof Long) {
            options.openingTimeBoundary = (Long) value;
        }
    }

    private void createItemForInsertions() {
        table.setItem(insertsRow, typeColumn, getLabel(TesterConstants.TEST_INSERTIONS.getTitle()));
        ValTimeIntervalEditor editor = getTimeEditor(options.insertsTimeBoundary);
        editor.valueChanged.connect(this, "onInsertionTimeChange(Object)");
        table.setCellWidget(insertsRow, timeColumn, editor);
        ValIntEditor countEditor = getQuantityEditor(options.inserts);
        countEditor.setEnabled(options.testInserts);
        countEditor.valueChanged.connect(this, "onInsertionsCountChange(Object)");
        table.setCellWidget(insertsRow, countColumn, countEditor);
        table.setItem(insertsRow, usageColumn, getUsageEditor(options.testInserts, false));
    }

    private void onInsertionTimeChange(Object value) {
        if (!update && value instanceof Long) {
            options.insertsTimeBoundary = (Long) value;
        }
    }

    private void onInsertionsCountChange(Object value) {
        if (!update && (value instanceof Long || value == null)) {
            options.inserts = value == null ? -1 : (Long) value;
        }
    }

    private void onInsertionsUsageChange(Object value) {
        if (!update && value instanceof Boolean) {
            Boolean state = (Boolean) value;
            ((ValIntEditor) table.cellWidget(insertsRow, countColumn)).setEnabled(state);
            options.testInserts = state;
            if (state) {
                options.inserts = -1;
            } else {
                options.inserts = 0;
            }
            ((ValIntEditor) table.cellWidget(insertsRow, countColumn)).setValue(options.inserts == -1 ? null : options.inserts);
        }
    }

    private void createItemForFilter() {
        table.setItem(filtersRow, typeColumn, getLabel(TesterConstants.TEST_FILTERS.getTitle()));
        ValTimeIntervalEditor editor = getTimeEditor(options.filtersTimeBoundary);
        editor.valueChanged.connect(this, "onFiltersTimeChange(Object)");
        table.setCellWidget(filtersRow, timeColumn, editor);
        ValIntEditor countEditor = getQuantityEditor(options.filtersCount);
        countEditor.setEnabled(options.testFilters);
        countEditor.valueChanged.connect(this, "onFiltersCountChange(Object)");
        table.setCellWidget(filtersRow, countColumn, countEditor);
        table.setItem(filtersRow, usageColumn, getUsageEditor(options.testFilters, false));
    }

    private void onFiltersTimeChange(Object value) {
        if (!update && value instanceof Long) {
            options.filtersTimeBoundary = (Long) value;
        }
    }

    private void onFiltersCountChange(Object value) {
        if (!update && (value instanceof Long || value == null)) {
            options.filtersCount = value == null ? -1 : (Long) value;
        }
    }

    private void onFiltersUsageChange(Object value) {
        if (!update && value instanceof Boolean) {
            Boolean state = (Boolean) value;
            ((ValIntEditor) table.cellWidget(filtersRow, countColumn)).setEnabled(state);
            options.testFilters = state;
            if (state) {
                options.filtersCount = -1;
            } else {
                options.filtersCount = 0;
            }
            ((ValIntEditor) table.cellWidget(filtersRow, countColumn)).setValue(options.filtersCount == -1 ? null : options.filtersCount);
        }
    }

    private void createItemForPages() {
        table.setItem(pagesRow, typeColumn, getLabel(TesterConstants.TEST_PAGE.getTitle()));
        ValTimeIntervalEditor editor = getTimeEditor(options.pagesTimeBoundary);
        editor.valueChanged.connect(this, "onPagesTimeChange(Object)");
        table.setCellWidget(pagesRow, timeColumn, editor);
        ValIntEditor countEditor = getQuantityEditor(options.pagesCount);
        countEditor.setEnabled(options.testPages);
        countEditor.valueChanged.connect(this, "onPagesCountChange(Object)");
        table.setCellWidget(pagesRow, countColumn, countEditor);
        table.setItem(pagesRow, usageColumn, getUsageEditor(options.testPages, false));
    }

    private void onPagesTimeChange(Object value) {
        if (!update && value instanceof Long) {
            options.pagesTimeBoundary = (Long) value;
        }
    }

    private void onPagesCountChange(Object value) {
        if (!update && (value instanceof Long || value == null)) {
            options.pagesCount = value == null ? -1 : (Long) value;
        }
    }

    private void onPagesUsageChange(Object value) {
        if (!update && value instanceof Boolean) {
            Boolean state = (Boolean) value;
            ((ValIntEditor) table.cellWidget(pagesRow, countColumn)).setEnabled(state);
            options.testPages = state;
            if (state) {
                options.pagesCount = -1;
            } else {
                options.pagesCount = 0;
            }
            ((ValIntEditor) table.cellWidget(pagesRow, countColumn)).setValue(options.pagesCount == -1 ? null : options.pagesCount);
        }
    }

    private void createItemForClosing() {
        table.setItem(closingRow, typeColumn, getLabel(TesterConstants.TEST_CLOSING.getTitle()));
        ValTimeIntervalEditor editor = getTimeEditor(options.closingTimeBoundary);
        editor.valueChanged.connect(this, "onClosingTimeChange(Object)");
        table.setCellWidget(closingRow, timeColumn, editor);
        QTableWidgetItem countItem = new QTableWidgetItem("1");
        countItem.setFlags(new Qt.ItemFlags(Qt.ItemFlag.NoItemFlags));
        table.setItem(closingRow, countColumn, countItem);
        table.setItem(closingRow, usageColumn, getUsageEditor(true, true));
    }

    private void onClosingTimeChange(Object value) {
        if (!update && value instanceof Long) {
            options.closingTimeBoundary = (Long) value;
        }
    }

    private void onPropDialogTimeChange(Object value) {
        if (!update && value instanceof Long) {
            options.propDialogTimeBoundary = (Long) value;
        }
    }

    private void createItemForPropDialog() {
        table.setItem(propDialogRow, typeColumn, getLabel(TesterConstants.TEST_PROP_DIALOG.getTitle()));
        ValTimeIntervalEditor editor = getTimeEditor(options.propDialogTimeBoundary);
        editor.valueChanged.connect(this, "onPropDialogTimeChange(Object)");
        table.setCellWidget(propDialogRow, timeColumn, editor);
        QTableWidgetItem countItem = new QTableWidgetItem("1");
        countItem.setFlags(new Qt.ItemFlags(Qt.ItemFlag.NoItemFlags));
        table.setItem(propDialogRow, countColumn, countItem);
        table.setItem(propDialogRow, usageColumn, getUsageEditor(options.testPropDialog, false));
    }

    private void onPropDialogUsageChange(Object value) {
        if (!update && value instanceof Boolean) {
            Boolean state = (Boolean) value;
            options.testPropDialog = state;
        }
    }

    private void createItemForCreationDialog() {
        table.setItem(creationDialogRow, typeColumn, getLabel(TesterConstants.TEST_CREATION_DIALOG.getTitle()));
        ValTimeIntervalEditor editor = getTimeEditor(options.creationDialogTimeBoundary);
        editor.valueChanged.connect(this, "onCreationDialogTimeChange(Object)");
        table.setCellWidget(creationDialogRow, timeColumn, editor);
        QTableWidgetItem countItem = new QTableWidgetItem("1");
        countItem.setFlags(new Qt.ItemFlags(Qt.ItemFlag.NoItemFlags));
        table.setItem(creationDialogRow, countColumn, countItem);
        table.setItem(creationDialogRow, usageColumn, getUsageEditor(options.testCreationDialog, false));
    }

    private void onCreationDialogTimeChange(Object value) {
        if (!update && value instanceof Long) {
            options.creationDialogTimeBoundary = (Long) value;
        }
    }

    private void onCreationDialogUsageChange(Object value) {
        if (!update && value instanceof Boolean) {
            Boolean state = (Boolean) value;
            options.testCreationDialog = state;
        }
    }
}
