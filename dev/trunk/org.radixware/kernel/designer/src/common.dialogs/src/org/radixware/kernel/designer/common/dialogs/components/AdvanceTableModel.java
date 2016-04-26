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

package org.radixware.kernel.designer.common.dialogs.components;

import java.util.Collections;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;


public abstract class AdvanceTableModel<TModelSource, TRowSource> extends AbstractTableModel {

    public enum ERowScope {

        LOCAL, OVERWRITE, INHERIT
    }

    protected interface IColumnHandler {

        Class<?> getColumnClass(int col);

        Object getCellValue(int row, int col, Object params);

        void setCellValue(Object value, int row, int col, Object params);

        boolean isCellEditable(int row, int col);

        String getColumnName(int col);
    }

    protected static abstract class ColumnHandlerAdapter implements IColumnHandler {

        private final String columnName;

        public ColumnHandlerAdapter(String columnName) {
            this.columnName = columnName;
        }

        @Override
        public Class<?> getColumnClass(int col) {
            return Object.class;
        }

        @Override
        public Object getCellValue(int row, int col, Object params) {
            return null;
        }

        @Override
        public void setCellValue(Object value, int row, int col, Object params) {
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }

        @Override
        public String getColumnName(int col) {
            return columnName;
        }
    }

    private TModelSource modelSource;

    public AdvanceTableModel(TModelSource modelSource) {
        super();
        this.modelSource = modelSource;
    }

    protected boolean validRowIndex(int row) {
        return row >= 0 && row < getRowCount();
    }

    protected boolean validColumnIndex(int col) {
        return col >= 0 && col < getColumnCount();
    }

    public final ERowScope getRowSourceScope(int row) {
        return getRowSourceScope(getRowSource(row));
    }

    public ERowScope getRowSourceScope(TRowSource row) {
        return ERowScope.LOCAL;
    }

    public final TModelSource getModelSource() {
        return modelSource;
    }

    public boolean checkDataChanged() {
        return false;
    }

    public void updateModel() {
    }

    public void addRow(TRowSource row) {
        throw new UnsupportedOperationException();
    }

    public void removeRow(int row) {
        throw new UnsupportedOperationException();
    }

    public void moveRowUp(int row) {
        moveRow(row, -1);
    }

    public void moveRowDown(int row) {
        moveRow(row, 1);
    }

    public void moveRow(int row, int sh) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getValueAt(int row, int col) {
        return getColumnHandler(col).getCellValue(row, col, null);
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        getColumnHandler(col).setCellValue(value, row, col, null);
        fireTableRowsUpdated(row, row);
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return getColumnHandler(col).isCellEditable(row, col);
    }

    @Override
    public String getColumnName(int col) {
        if (validColumnIndex(col)) {
            return getColumnHandler(col).getColumnName(col);
        }
        return "";
    }

    @Override
    public Class<?> getColumnClass(int col) {
        return getColumnHandler(col).getColumnClass(col);
    }

    @Override
    public int getColumnCount() {
        return getColumnHandlers().size();
    }

    /**
     * Gets list of column handlers.
     * Default implementation return <tt>Collections.EMPTY_LIST</tt> for backward compatibility.
     * @return list of column handlers
     */
    public List<IColumnHandler> getColumnHandlers() {
        return Collections.EMPTY_LIST;
    }

    public abstract TRowSource getRowSource(int row);

    protected abstract IColumnHandler getColumnHandler(Object key);

}
