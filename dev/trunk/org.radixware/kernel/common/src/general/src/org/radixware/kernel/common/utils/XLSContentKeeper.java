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

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.radixware.kernel.common.utils.ResultSetFactory.IContentKeeper;

class XLSContentKeeper implements IContentKeeper {

    private final Object[][] content;
    private final int columnCount;

    XLSContentKeeper(final InputStream is) throws IOException {
        final HSSFWorkbook wb = new HSSFWorkbook(is);

        if (wb.getNumberOfSheets() == 0) {
            throw new IOException("XLS content doesn't contain any sheets");
        } else {
            final HSSFSheet sheet = wb.getSheetAt(0);
            final int rows = sheet.getPhysicalNumberOfRows();
            int columnCount = 0;

            for (int r = 0; r < rows; r++) {
                final HSSFRow row = sheet.getRow(r);

                if (row == null) {
                    continue;
                } else if (columnCount < row.getLastCellNum()) {
                    columnCount = row.getLastCellNum();
                }
            }
            this.content = new Object[rows][columnCount];
            this.columnCount = columnCount;

            for (int row = 0; row < rows; row++) {
                final HSSFRow rowContent = sheet.getRow(row);

                if (rowContent != null) {
                    for (int col = 0; col < rowContent.getLastCellNum(); col++) {
                        final HSSFCell cell = rowContent.getCell(col);

                        if (cell != null) {
                            switch (cell.getCellType()) {
                                case Cell.CELL_TYPE_FORMULA:
                                    throw new IOException("Sheet " + sheet.getSheetName() + ", row " + cell.getRowIndex() + ", col " + cell.getColumnIndex() + " contains formula [" + cell.getCellFormula() + "]. This content is not supported for ResultSet");
                                case Cell.CELL_TYPE_NUMERIC:
                                    final HSSFCellStyle style = cell.getCellStyle();

                                    if (style.getDataFormatString().toUpperCase().contains("YY")) {
                                        content[row][col] = cell.getDateCellValue();
                                    } else {
                                        content[row][col] = cell.getNumericCellValue();
                                    }
                                    break;
                                case Cell.CELL_TYPE_STRING:
                                    content[row][col] = cell.getStringCellValue();
                                    break;
                                case Cell.CELL_TYPE_BLANK:
                                    break;
                                case Cell.CELL_TYPE_BOOLEAN:
                                    content[row][col] = "" + cell.getBooleanCellValue();
                                    break;
                                case Cell.CELL_TYPE_ERROR:
                                    throw new IOException("Sheet " + sheet.getSheetName() + ", row " + cell.getRowIndex() + ", col " + cell.getColumnIndex() + " contains error content [" + cell.getErrorCellValue() + "]. This content is not supported for ResultSet");
                                default:
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public int getRowCount() {
        return content.length;
    }

    @Override
    public Object getCell(final int row, final int col) {
        if (row < 0 || row >= getRowCount()) {
            throw new IllegalArgumentException("Row number [" + row + "] our of range 0.." + getRowCount());
        } else if (col < 0 || col >= getColumnCount()) {
            throw new IllegalArgumentException("Column number [" + col + "] our of range 0.." + getColumnCount());
        } else {
            return content[row][col];
        }
    }
}
