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
package org.radixware.kernel.common.client.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.DateFormatConverter;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.exceptions.ExcelNumberOutOfBoundsException;
import org.radixware.kernel.common.client.exceptions.ExcelNumberPrecisionTooLargeException;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.types.ArrRef;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Bin;
import org.radixware.kernel.common.utils.Base64;

public class XlsxWriter {

    private final SXSSFWorkbook workBook;
    private final Sheet workSheet;
    private boolean isHeaderFilled = false;
    private final int EXCEL_NUMBER_PRECISION_LIMIT = 15;
    public final static int ROW_ACCESS_WINDOW_SIZE = 1000;
    private final File file;
    private final Map<String, CellStyle> styleMap;
    private final Map<Integer, Integer> adjustedColumnsMaxWidth;

    public XlsxWriter(File file, String sheetName) throws FileNotFoundException, IOException, InvalidFormatException {
        this.file = file;
        workBook = new SXSSFWorkbook(ROW_ACCESS_WINDOW_SIZE);
        workSheet = workBook.createSheet(sheetName);
        styleMap = new HashMap<>();
        adjustedColumnsMaxWidth = new HashMap<>();
    }

    public void flush() throws IOException {
    }

    public void close() throws IOException {
        if (isHeaderFilled) {
            workSheet.createFreezePane(0, 1);
        }
        try (FileOutputStream fos = new FileOutputStream(file);) {
            workBook.write(fos);
        } catch (IOException ex) {
            throw ex;
        }
    }

    public void writeHeaderFields(List<String> headers, String alignmentFlag) throws IOException {
        if (headers != null && !headers.isEmpty()) {
            for (int i = 0; i < headers.size(); i++) {
                writeParametriziedFieldString(headers.get(i), 0, i, alignmentFlag);
                workSheet.autoSizeColumn(i);
            }
            isHeaderFilled = true;
        }
    }

    public void writeStringField(String value, int row, int cell) throws IOException {
        writeParametriziedFieldString(value, row, cell, null);
    }

    private void writeParametriziedFieldString(String value, int row, int cell, String alignment) {
        Cell cellData = getCell(row, cell);
        CellStyle cellStyle;
        if (alignment != null) {
            short align;
            cellStyle = workBook.createCellStyle();
            switch (alignment) {
                case ("AlignLeft"):
                    align = CellStyle.ALIGN_LEFT;
                    break;
                case ("AlignRight"):
                    align = CellStyle.ALIGN_RIGHT;
                    break;
                case ("AlignCenter"):
                    align = CellStyle.ALIGN_CENTER;
                    break;
                default:
                    align = CellStyle.ALIGN_CENTER;
            }
            cellStyle.setAlignment(align);
            cellStyle.setDataFormat(workBook.createDataFormat().getFormat("text"));
        } else {
            cellStyle = getStyle("text");
        }
        if (value != null) {
            cellData.setCellValue(value);
        }
        cellData.setCellStyle(cellStyle);
    }

    public void writeValue(final Object value, final EValType type, int row, int cell) throws IOException {
        Cell cellData = getCell(row, cell);
        CellStyle style;
        switch (type) {
            case INT:
                if (value != null) {
                    cellData.setCellValue((double) ((Long) value).longValue());
                }
                style = getStyle("0");
                cellData.setCellStyle(style);

                break;
            case NUM:
                writeNumValue((BigDecimal) value, row, cell);
                break;
            case BOOL:
                if (value != null) {
                    cellData.setCellValue((boolean) value);
                } else {
                    style = getStyle("0");
                    cellData.setCellStyle(style);
                }
                break;
            case ARR_INT:
            case ARR_NUM:
            case ARR_DATE_TIME:
            case ARR_BOOL:
            case ARR_BIN:
                writeStringField(String.valueOf(value), row, cell);
                break;
            case DATE_TIME:
                if (value != null) {
                    cellData.setCellValue((Timestamp) value);
                }
                style = getStyle("dd.MM.yyyy");
                cellData.setCellStyle(style);
                break;
            case BIN:
                if (value != null) {
                    writeStringField(ValueConverter.arrByte2Str(((Bin) value).get(), ""), row, cell);
                } else {
                    style = getStyle("text");
                    cellData.setCellStyle(style);
                }
                break;
            case PARENT_REF:
            case OBJECT: {
                final Pid pid;
                if (value != null) {
                    if (value instanceof Reference) {
                        pid = ((Reference) value).getPid();
                    } else if (value instanceof Pid) {
                        pid = (Pid) value;
                    } else if (value instanceof EntityModel) {
                        pid = ((EntityModel) value).getPid();
                    } else {
                        throw new IllegalArgumentException("Unsupported type of parent reference value: " + value.getClass().getName());
                    }
                    if (pid == null) {
                        style = getStyle("text");
                        cellData.setCellStyle(style);
                    } else {
                        cellData.setCellValue(pid.toString());
                        style = getStyle("text");
                        cellData.setCellStyle(style);
                    }
                } else {
                    style = getStyle("text");
                    cellData.setCellStyle(style);
                }
                break;
            }
            case ARR_REF: {
                if (value != null) {
                    final ArrRef arrRef = (ArrRef) value;
                    final ArrStr arrStr = new ArrStr(arrRef.size());
                    for (Reference reference : arrRef) {
                        if (reference == null || reference.getPid() == null) {
                            arrStr.add(null);
                        } else {
                            arrStr.add(reference.getPid().toString());
                        }
                    }
                    writeStringField(arrStr.toString(), row, cell);
                } else {
                    style = getStyle("text");
                    cellData.setCellStyle(style);
                }
                break;
            }
            case XML: {
                if (value != null) {
                    final byte[] asBytes;
                    try {
                        asBytes = ((XmlObject) value).xmlText().getBytes("UTF-8");
                    } catch (UnsupportedEncodingException exception) {
                        throw new WrongFormatError("Failed to write xml value", exception);
                    }
                    writeStringField(Base64.encode(asBytes), row, cell);
                } else {
                    style = getStyle("text");
                    cellData.setCellStyle(style);
                }
                break;
            }
            case STR:
            case CHAR:
            case ARR_STR:
            case ARR_CHAR:
                if (value != null) {
                    writeStringField(String.valueOf(value), row, cell);
                } else {
                    style = getStyle("text");
                    cellData.setCellStyle(style);
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported value type " + type.getName());
        }
    }

    public void writeNumValue(BigDecimal value, int row, int cell) {
        Cell cellData = getCell(row, cell);
        CellStyle cellStyle;

        if (value != null) {
            if (value.precision() > 15) {
                value = value.setScale(value.scale() + 15 - value.precision(), RoundingMode.HALF_UP);
            }
            cellData.setCellValue(value.doubleValue()); //can do it cause 15 digits Excel limitation
            if (value.toPlainString().indexOf('.') == -1) {
                cellStyle = getStyle("0");
            } else {
                cellStyle = getStyle("0.##############################"); // max 30 symbols after delimiter 
            }
        } else {
            cellStyle = getStyle("0.##############################");
        }

        cellData.setCellStyle(cellStyle);
    }

    public void checkNumber(BigDecimal value) throws ExcelNumberOutOfBoundsException, ExcelNumberPrecisionTooLargeException {
        if (value != null) {
            BigDecimal absVal = value.abs();
            if (absVal.setScale(0, RoundingMode.DOWN).compareTo(new BigDecimal("1.0E15")) >= 0) {
                throw new ExcelNumberOutOfBoundsException("");
            } else {
                if (absVal.precision() > 15) {
                    throw new ExcelNumberPrecisionTooLargeException("");
                }
            }
        }
    }

    public void writeIntValue(Long value, int row, int cell) throws ExcelNumberOutOfBoundsException {
        Cell cellData = getCell(row, cell);
        CellStyle cellStyle;

        if (value != null) {
            long positiveLongVal = Math.abs(value);
            if (String.valueOf(positiveLongVal).length() > EXCEL_NUMBER_PRECISION_LIMIT) {
                throw new ExcelNumberOutOfBoundsException("");
            }
            cellData.setCellValue((double) value.longValue());
        }
        cellStyle = getStyle("0");
        cellData.setCellStyle(cellStyle);
    }

    public void writeDateValue(final Timestamp timestamp, String dateFormat, Locale locale, int row, int cell) {
        if (dateFormat.contains(".SSS")) {
            dateFormat = dateFormat.replace(".SSS", "");
        } else if (dateFormat.contains(":SSS")) {
            dateFormat = dateFormat.replace(":SSS", "");
        }

        String excelFormatPattern = DateFormatConverter.convert(locale, dateFormat);
        StringBuilder sb = new StringBuilder();
        String formatCode = excelFormatPattern.substring(0, excelFormatPattern.indexOf(']') + 1);
        if ("[$-0419]".equals(formatCode)) { //POI всегда отображает месяцы в именительном падеже
            sb.append("[$-FC19]").append(excelFormatPattern.substring(excelFormatPattern.indexOf(']') + 1));
        } else {
            sb.append(excelFormatPattern);
        }
        Cell cellData = getCell(row, cell);
        CellStyle cellStyle = cellStyle = getStyle(sb.toString());
        cellData.setCellStyle(cellStyle);
        if (timestamp != null) {
            Timestamp copyTimestamp = new Timestamp(timestamp.getTime());
            copyTimestamp.setNanos(0); //Excel doesn't have nanos/ms bultin format
            cellData.setCellValue(copyTimestamp);
        }
    }

    private Cell getCell(int row, int cell) {
        Row rowData = workSheet.getRow(row);
        if (workSheet == null || (rowData = workSheet.getRow(row)) == null) {
            rowData = workSheet.createRow(row);
        }
        Cell cellData = rowData.getCell(cell);
        if (cellData == null) {
            cellData = rowData.createCell(cell);
        }
        return cellData;
    }

    public void countAdjustedColumnWidth(int col) {
        Integer width = adjustedColumnsMaxWidth.get(col);
        if (width == null) {
            adjustedColumnsMaxWidth.put(col, workSheet.getColumnWidth(col));
            width = workSheet.getColumnWidth(col);
        }
        workSheet.autoSizeColumn(col);
        Integer newColWidth = workSheet.getColumnWidth(col);
        if (newColWidth > width) {
            adjustedColumnsMaxWidth.put(col, newColWidth);
        }
    }

    public void applyAdjustedColWidth() {
        for (Map.Entry<Integer, Integer> entry : adjustedColumnsMaxWidth.entrySet()) {
            Integer col = entry.getKey();
            Integer width = entry.getValue();
            workSheet.setColumnWidth(col, width);
        }
    }

    private CellStyle getStyle(String format) {
        CellStyle style = styleMap.get(format);
        if (style == null) {
            style = workBook.createCellStyle();
            style.setDataFormat(workBook.createDataFormat().getFormat(format));
            styleMap.put(format, style);
        }
        return style;
    }

}
