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

package org.radixware.kernel.designer.ads.editors.clazz.simple;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsObjectTitleFormatDef.TitleItem;
import org.radixware.kernel.common.enums.EValType;


class HelpAboutMessageFormatTableModel extends AbstractTableModel {

    private EValType valType;

    private static class RowInfo {

        private String mask, data, result;

        public RowInfo(String mask, String data, String result) {
            this.mask = mask;
            this.data = data;
            this.result = result;
        }

        public String getMask() {
            return mask;
        }

        public String getData() {
            return data;
        }

        public String getResult() {
            return result;
        }
    }
    List<RowInfo> rowsInfo;

    public HelpAboutMessageFormatTableModel(EValType valtype) {

        super();
        this.rowsInfo = new ArrayList<RowInfo>();
        this.valType = valtype;

        if (valType == EValType.BOOL || valType == EValType.CHAR || valType == EValType.STR) {
            rowsInfo.add(new RowInfo("This is {0}", "text", MessageFormat.format("This is {0}", "text")));
        } else if (valType == EValType.INT) {
            rowsInfo.add(new RowInfo("This is {0}", "text", MessageFormat.format("This is {0}", "text")));
            rowsInfo.add(new RowInfo("This is {0, number, integer}", "23", MessageFormat.format("This is {0, number, integer}", 23)));
        } else if (valType == EValType.NUM) {
            rowsInfo.add(new RowInfo("This is {0}", "text", MessageFormat.format("This is {0}", "text")));
            rowsInfo.add(new RowInfo("This is {0, number, #.##}", "45.156", MessageFormat.format("This is {0, number, #.##}", 45.156)));
        } else if (valType == EValType.DATE_TIME) {
            final Date date = new Date();
            final String dateAsStr = date.toString();
            rowsInfo.add(new RowInfo("This is {0, date}", dateAsStr, MessageFormat.format("This is {0, date}", date)));
            rowsInfo.add(new RowInfo("Today is {0, date, short}", dateAsStr, MessageFormat.format("Today is {0, date, short}", date)));
            rowsInfo.add(new RowInfo("Today is {0 ,date, MMM}", dateAsStr, MessageFormat.format("Today is {0, date, MMM}", date)));
            rowsInfo.add(new RowInfo("This is {0, time, h:s}", dateAsStr, MessageFormat.format("This is {0, time, h:s}", date)));
        } else {
            rowsInfo.add(new RowInfo("This is {0}", "text", MessageFormat.format("This is {0}", "text")));
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        final RowInfo rowInfo = rowsInfo.get(rowIndex);
        if (columnIndex == columnMask_Index) {
            return rowInfo.getMask();
        } else if (columnIndex == columnData_Index) {
            return rowInfo.getData();
        } else {
            return rowInfo.getResult();
        }
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getRowCount() {
        return rowsInfo.size();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
    public static final String columnNames[] = new String[]{"Mask", "Data", "Result"};
    private static final int columnMask_Index = 0,  columnData_Index = 1;
}
