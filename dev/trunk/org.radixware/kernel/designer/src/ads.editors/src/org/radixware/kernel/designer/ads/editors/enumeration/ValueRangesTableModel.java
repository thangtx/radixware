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

package org.radixware.kernel.designer.ads.editors.enumeration;

import org.radixware.kernel.common.defs.ads.enumeration.ValueRange;
import org.radixware.kernel.common.defs.ads.enumeration.ValueRanges;
import javax.swing.table.AbstractTableModel;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;

public class ValueRangesTableModel extends AbstractTableModel {

    final private ValueRanges valueRanges;
    final private EValType eValType;

    private ValueRangesTableModel() {
        super();

        this.valueRanges = null;
        this.eValType = null;
    }

    public ValueRangesTableModel(ValueRanges valueRanges, EValType eValType) {
        super();

        this.valueRanges = valueRanges;
        this.eValType = eValType;
    }

    @Override
    public Class getColumnClass(int c) {

        if (eValType == EValType.INT) {
            return Long.class;
        } else if (eValType == EValType.STR) {
            return String.class;
        } else if (eValType == EValType.CHAR) {
            return Character.class;
        } else {
            return Object.class;
        }
    }

    @Override
    public String getColumnName(int c) {
        return columnsNames[c];
    }

    @Override
    public int getColumnCount() {
        return columnsNames.length;
    }

    @Override
    public int getRowCount() {
        return valueRanges.size();
    }

    @Override
    public Object getValueAt(int r, int c) {

        final ValueRange valueRange = valueRanges.get(r);
        if (c == MIN_COLUMN) {
            return valueRange == null || valueRange.getFrom() == null ? "" : valueRange.getFrom().toObject(eValType);
        } else {
            return valueRange == null || valueRange.getTo() == null ? "" : valueRange.getTo().toObject(eValType);
        }
    }

    @Override
    public void setValueAt(Object value, int r, int c) {
       final ValueRange valueRange = valueRanges.get(r);
        if (c == MIN_COLUMN) {
            valueRange.setFrom(ValAsStr.Factory.newInstance(value, eValType));
        } else {
            valueRange.setTo(ValAsStr.Factory.newInstance(value, eValType));
        }
        fireTableCellUpdated(r, c);
    }

    public void clear() {
        valueRanges.clear();
        fireTableDataChanged();
    }

    public void removeRow(int rowIndex) {

        valueRanges.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
    private static final String columnsNames[] = new String[]{
        "Min", "Max"
    };
    public static final int MIN_COLUMN = 0, MAX_COLUMN = 1;
}
