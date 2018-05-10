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
package org.radixware.kernel.common.utils;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.radixware.kernel.common.utils.ResultSetFactory.IContentKeeper;

class ExcelResultSet extends FakeResultSet {

    private static final String TABLE_NAME = "ExcelTable";

    private final IContentKeeper content;
    private final boolean useFirstRowAsColumnNames;
    private ColumnDescriptor[] desc = null;

    public ExcelResultSet(final String[] columns, final IContentKeeper content, final boolean useFirstRowAsColumnNames, final IResultSetLobProvider provider) {
        this(columns, ResultSet.TYPE_FORWARD_ONLY, content, useFirstRowAsColumnNames, provider);
    }

    public ExcelResultSet(final String[] columns, final int resultSetType, final IContentKeeper content, final boolean useFirstRowAsColumnNames, final IResultSetLobProvider provider) {
        super(TABLE_NAME, columns, resultSetType, provider);
        if (content == null) {
            throw new IllegalArgumentException("Content keeper can't be null");
        } else {
            this.content = content;
            this.useFirstRowAsColumnNames = useFirstRowAsColumnNames;
        }
    }
    
    @Override
    protected int getResultSetSize() {
        return useFirstRowAsColumnNames ? content.getRowCount() - 1 : content.getRowCount();
    }

    @Override
    protected Object getCell(final int row, final int col) {
        return useFirstRowAsColumnNames ? content.getCell(row + 1, col) : content.getCell(row, col);
    }

    @Override
    protected ColumnDescriptor[] getColumnDescriptors() {
        if (desc == null) {
            final String[] columns;
            
            if (useFirstRowAsColumnNames) {
                columns = extractNames(content);
outer:          for (String awaitedColumn : this.columns) {
                    for (String loadedColumn : columns) {
                        if (awaitedColumn.equalsIgnoreCase(loadedColumn)) {
                            continue outer;
                        }
                    }
                    throw new IllegalArgumentException("Explicitly typed column name ["+awaitedColumn+"] not found in the first row of the Excel sheet content with typed column names");
                }
            }
            else {
                columns = this.columns;
            }

            desc = new ColumnDescriptor[Math.min(columns.length, content.getColumnCount())];
            for (int column = 0; column < desc.length; column++) {
                Object value = null, temp;

                temp = getCell(0, column);
                if (temp != null) {
                    if (temp instanceof Date) {
                        value = temp;
                    } else if ((temp instanceof Double) && !(value instanceof Date)) {
                        value = temp;
                    } else if ((temp instanceof String) && !((value instanceof Date) || (value instanceof Double))) {
                        value = temp;
                    }
                }
                if (value == null) {
                    desc[column] = new ColumnDescriptor(column,columns[column], Types.VARCHAR, "VARCHAR", String.class.getName(), false, false, 0, 0);
                } else if (value instanceof Date) {
                    desc[column] = new ColumnDescriptor(column,columns[column], Types.DATE, "DATE", Date.class.getName(), false, false, 0, 0);
                } else if (value instanceof Double) {
                    desc[column] = new ColumnDescriptor(column,columns[column], Types.NUMERIC, "NUMERIC", Double.class.getName(), true, true, 0, 0);
                } else if (value instanceof String) {
                    desc[column] = new ColumnDescriptor(column,columns[column], Types.VARCHAR, "VARCHAR", String.class.getName(), false, false, 0, 0);
                } else {
                    desc[column] = new ColumnDescriptor(column,columns[column], Types.VARCHAR, "VARCHAR", String.class.getName(), false, false, 0, 0);
                }
            }
            if (this.columns.length > 0) {  // Reduce column list by excluding non-typed column names from the result
                final List<ColumnDescriptor> temp = new ArrayList<>();

loop:           for (String item : this.columns) {
                    for (ColumnDescriptor entity : desc) {
                        if (entity.columnName.equalsIgnoreCase(item)) {
                            temp.add(entity);
                            continue loop;
                        }
                    }
                }
                desc = temp.toArray(new ColumnDescriptor[temp.size()]);
                temp.clear();
            }
        }
        return desc;
    }

    private String[] extractNames(IContentKeeper content) {
        final String[] result = new String[content.getColumnCount()];

        if (content.getRowCount() == 0) {
            for (int index = 0; index < result.length; index++) {
                result[index] = "COLUMN" + (index + 1);
            }
        } else {
            for (int index = 0; index < result.length; index++) {
                final Object    cell = content.getCell(0, index);
                
                if (cell != null && !cell.toString().isEmpty()) {
                    result[index] = cell.toString();
                }
                else {
                    throw new IllegalArgumentException("Column ["+(index+1)+"] in the first row of Excel sheet contains null or empty value insted of column name");
                }
            }
        }
        return result;
    }
}
