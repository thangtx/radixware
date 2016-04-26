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

package org.radixware.kernel.designer.ads.editors.clazz.report;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsExportCsvColumn;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportFormat;
import org.radixware.kernel.designer.common.dialogs.components.CommonParametersEditorCellLib.StringCellValue;


public class CsvExportColumnTableModel extends AbstractTableModel {

    private RadixObjects<AdsExportCsvColumn> csvExportColumns;
    private List<String> columnsNames;
    public final static int ID_COLUMN = 0;
    public final static int EXT_NAME_COLUMN = 1;
    public final static int COLUMN_FORMAT = 2;
    private final static int COLUMN_COUNT = 3;
    private final AdsReportClassDef report_;

    public AdsExportCsvColumn getViewItemByRow(int curRow) {
        return csvExportColumns.get(curRow);
    }

    public int getRowByViewItem(AdsExportCsvColumn item) {
        for (int i = 0, size = getRowCount() - 1; i <= size; i++) {
            if (getViewItemByRow(i).equals(item)) {
                return i;
            }
        }
        return -1;
    }

    CsvExportColumnTableModel(final AdsReportClassDef report) {
        report_ = report;
        csvExportColumns = report.getCsvInfo().getExportCsvColumns();
        columnsNames = new ArrayList<>();
        String propColumn = NbBundle.getMessage(CsvExportColumnTableModel.class, "CsvExportColumnPanel-PropertyLabel");
        String extNameColumn = NbBundle.getMessage(CsvExportColumnTableModel.class, "CsvExportColumnPanel-ExternalName-Label");
        String formatColumn = NbBundle.getMessage(CsvExportColumnTableModel.class, "CsvExportColumnPanel-Format-Label");
        columnsNames.add(propColumn);
        columnsNames.add(extNameColumn);
        columnsNames.add(formatColumn);
    }

    public void moveItem(AdsExportCsvColumn cutItem, int pos) {
        csvExportColumns.remove(cutItem);
        csvExportColumns.add(pos, cutItem);
        if (report_.getEditState() != EEditState.MODIFIED) {
            report_.setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public int getRowCount() {
        return csvExportColumns.size();
    }

    @Override
    public String getColumnName(int c) {
        return columnsNames.get(c);
    }

    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    AdsPropertyDef getProperty(int rowIndex) {
        AdsExportCsvColumn item = csvExportColumns.get(rowIndex);
        return report_.getCsvInfo().findProperty(item.getPropId());
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= csvExportColumns.size()) {
            return "";
        }
        AdsExportCsvColumn item = csvExportColumns.get(rowIndex);
        if (item == null) {
            return "";
        }
        AdsPropertyDef prop = report_.getCsvInfo().findProperty(item.getPropId());
        if (columnIndex == EXT_NAME_COLUMN) {
            String name = item.getExtName();
            return new StringCellValue(name);
        } else if (columnIndex == COLUMN_FORMAT) {
            if (item.getFormat() != null && prop != null) {
                return item.getFormat().getStrValue(prop.getValue().getType().getTypeId());
            } else {
                return "";
            }
        }

        return prop == null ? item.getPropId() : prop.getName();
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        if (row < 0 || row >= csvExportColumns.size()) {
            return;
        }
        AdsExportCsvColumn item = csvExportColumns.get(row);
        if (item == null) {
            return;

        }
        if (column == EXT_NAME_COLUMN) {
            if (value instanceof String) {
                item.setExtName((String) value);
            } else if (value instanceof StringCellValue) {
                item.setExtName(((StringCellValue) value).getName());
            }
            if (report_.getEditState() != EEditState.MODIFIED) {
                report_.setEditState(EEditState.MODIFIED);
            }
            fireTableCellUpdated(row, column);
        } else if (column == COLUMN_FORMAT) {
            item.setFormat((AdsReportFormat) value);
            if (report_.getEditState() != EEditState.MODIFIED) {
                report_.setEditState(EEditState.MODIFIED);
            }
            fireTableCellUpdated(row, column);
        }

    }

    public void addItem(AdsExportCsvColumn item) {
        csvExportColumns.add(item);
        if (report_.getEditState() != EEditState.MODIFIED) {
            report_.setEditState(EEditState.MODIFIED);
        }
        reload();
    }

    public List<AdsPropertyDef> getProps() {
        List<AdsPropertyDef> props = new ArrayList<>();
        for (AdsExportCsvColumn csvExportColumn : csvExportColumns) {
            AdsPropertyDef prop = report_.getCsvInfo().findProperty(csvExportColumn.getPropId());
            props.add(prop);
        }
        return props;
    }

    public void removeRow(int index) {
        final AdsExportCsvColumn item = getViewItemByRow(index);
        csvExportColumns.remove(item);
        if (report_.getEditState() != EEditState.MODIFIED) {
            report_.setEditState(EEditState.MODIFIED);
        }
        reload();
    }

    public final void reload() {
        fireTableDataChanged();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        //if (column == COLUMN_FORMAT) {
        //    return item.getEditPossibility().contains(AdsEnumItemDef.EditPossibility.DOMAIN);
        // }
        return (column == EXT_NAME_COLUMN) || (column == COLUMN_FORMAT);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Class getColumnClass(int c) {
        if (c == EXT_NAME_COLUMN) {
            return StringCellValue.class;
        } else if (c == COLUMN_FORMAT) {
            return String.class;
        } else {
            return Object.class;
        }
    }
}
