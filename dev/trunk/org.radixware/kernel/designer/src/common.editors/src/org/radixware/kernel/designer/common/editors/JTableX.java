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

package org.radixware.kernel.designer.common.editors;

import javax.swing.*;
import javax.swing.table.*;
import java.util.Vector;
import org.radixware.kernel.designer.common.dialogs.components.TunedTable;

/*
 * table allowing to set unique cell editor for each specified table column's cell
 * */
public abstract class JTableX<T extends Object> extends TunedTable {

    protected ColumnEditors<T> columnEditors;

    public JTableX() {
        super();
        columnEditors = null;
    }

    public JTableX(TableModel tm) {
        super(tm);
        columnEditors = null;
    }

    public JTableX(TableModel tm, TableColumnModel cm) {
        super(tm, cm);
        columnEditors = null;
    }

    public JTableX(TableModel tm, TableColumnModel cm, ListSelectionModel sm) {
        super(tm, cm, sm);
        columnEditors = null;
    }

    public JTableX(int rows, int cols) {
        super(rows, cols);
        columnEditors = null;
    }

    public JTableX(final Vector rowData, final Vector columnNames) {
        super(rowData, columnNames);
        columnEditors = null;
    }

    public JTableX(final Object[][] rowData, final Object[] colNames) {
        super(rowData, colNames);
        columnEditors = null;
    }

    // new constructor
    public JTableX(TableModel tm, ColumnEditors<T> columnEditors) {
        super(tm, null, null);
        this.columnEditors = columnEditors;
    }

    public void setRowEditorModel(ColumnEditors<T> columnEditors) {
        this.columnEditors = columnEditors;
    }

    public ColumnEditors<T> getRowEditorModel() {
        return columnEditors;
    }

    protected abstract TableCellEditor getColumnCellEditorByRow(int row, int col);

    @Override
    public TableCellEditor getCellEditor(int row, int col) {

        assert(columnEditors != null);
        final TableCellEditor result = getColumnCellEditorByRow(row, col);
        return result == null ? super.getCellEditor(row, col) : result;
    }
}
