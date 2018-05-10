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
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportColumnDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportColumns;
import org.radixware.kernel.designer.common.dialogs.components.CommonParametersEditorCellLib.StringCellValue;


public class ReportColumnsTableModel extends AbstractTableModel {
        
    private static final List<String> columnsNames = new ArrayList<>();
    static {
        columnsNames.add("Name");
        columnsNames.add("Property");
    }
    
    private final AdsReportColumns reportColumns;    
    public final static int NAME_COLUMN = 0;
    public final static int PROPERTY_COLUMN = 1;
    private final static int COLUMN_COUNT = 2;
    private final AdsReportClassDef report_;        
    
    private boolean isShowColumnId = false;

    public AdsReportColumnDef getViewItemByRow(int curRow) {
        return reportColumns.get(curRow);
    }

    public int getRowByViewItem(AdsReportColumnDef item) {
        for (int i = 0, size = getRowCount() - 1; i <= size; i++) {
            if (getViewItemByRow(i).equals(item)) {
                return i;
            }
        }
        return -1;
    }

    ReportColumnsTableModel(final AdsReportClassDef report) {
        report_ = report;
        reportColumns = report.getColumns();                
    }

    public void moveItem(AdsReportColumnDef cutItem, int pos) {
        reportColumns.remove(cutItem);
        reportColumns.add(pos, cutItem);
    }

    @Override
    public int getRowCount() {
        return reportColumns.size();
    }

    @Override
    public String getColumnName(int c) {
        return columnsNames.get(c);
    }

    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    public boolean isIsShowColumnId() {
        return isShowColumnId;
    }

    public void setIsShowColumnId(boolean isShowColumnId) {
        this.isShowColumnId = isShowColumnId;
    }

    AdsPropertyDef getProperty(int rowIndex) {
        AdsReportColumnDef item = reportColumns.get(rowIndex);
        return AdsReportClassDef.ReportUtils.findProperty(report_, item.getPropertyId());        
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= reportColumns.size()) {
            return "";
        }
        AdsReportColumnDef item = reportColumns.get(rowIndex);
        if (item == null) {
            return "";
        }
        AdsPropertyDef prop = AdsReportClassDef.ReportUtils.findProperty(report_, item.getPropertyId());
        if (columnIndex == NAME_COLUMN) {
            String name = item.getName();            
            String idString = item.getId() == null || !isShowColumnId ? "" : " (" + item.getId().toString() + ")";
            
            return new StringCellValue(name + idString);
        }
        
        return prop == null ? item.getPropertyId() : prop.getName();
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        if (row < 0 || row >= reportColumns.size()) {
            return;
        }
        AdsReportColumnDef item = reportColumns.get(row);
        if (item == null) {
            return;

        }
        if (column == NAME_COLUMN) {
            if (value instanceof String) {
                item.setName((String) value);
            } else if (value instanceof StringCellValue) {
                item.setName(((StringCellValue) value).getName());
            }
            if (report_.getEditState() != EEditState.MODIFIED) {
                report_.setEditState(EEditState.MODIFIED);
            }
            fireTableCellUpdated(row, column);
        }
    }

    public void addItem(AdsReportColumnDef item) {
        reportColumns.add(item);
        if (report_.getEditState() != EEditState.MODIFIED) {
            report_.setEditState(EEditState.MODIFIED);
        }
        reload();
    }

    public List<AdsPropertyDef> getProps() {
        List<AdsPropertyDef> props = new ArrayList<>();
        for (AdsReportColumnDef column : reportColumns) {
            AdsPropertyDef prop = AdsReportClassDef.ReportUtils.findProperty(report_, column.getPropertyId());
            props.add(prop);
        }
        return props;
    }

    public void removeRow(int index) {
        final AdsReportColumnDef item = getViewItemByRow(index);
        reportColumns.remove(item);
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
        return column == NAME_COLUMN;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Class getColumnClass(int c) {
        if (c == NAME_COLUMN) {
            return StringCellValue.class;
        } else {
            return Object.class;
        }
    }
}
