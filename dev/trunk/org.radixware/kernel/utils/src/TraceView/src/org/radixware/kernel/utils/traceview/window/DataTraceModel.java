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
package org.radixware.kernel.utils.traceview.window;

import org.radixware.kernel.utils.traceview.TraceEvent;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.radixware.kernel.utils.traceview.TraceViewSettings.EEventColumn;

class DataTraceModel extends AbstractTableModel {

    private TraceEvent[] data;
    private final int columnCount = EEventColumn.values().length;

    public DataTraceModel(List<TraceEvent> data) {
        this.data = new TraceEvent[data.size()];
        this.data = data.toArray(this.data);
    }

    public DataTraceModel(TraceEvent event) {
        this.data = new TraceEvent[1];
        this.data[0] = event;
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public String getColumnName(int column) {
        return EEventColumn.getColumnNameByIndex(column);
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column != 0;
    }

    public void removeRow(int row) {
        //data.remove(row);
        //fireTableRowsDeleted(row, row);
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (column == EEventColumn.SEVERITY.getIndex()) {
            return data[row].getSeverity();
        } else if (column == EEventColumn.DATE.getIndex()) {
            return data[row].getIndexedDate();
        } else if (column == EEventColumn.SOURCE.getIndex()) {
            return data[row].getSource();
        } else if (column == EEventColumn.CONTEXT.getIndex()) {
            return data[row].getContext();
        } else if (column == EEventColumn.MESSAGE.getIndex()) {
            return data[row].getMessage();
        } else if (column == 5){
            return data[row].getContextSet();
        }
        return null;
    }
}
