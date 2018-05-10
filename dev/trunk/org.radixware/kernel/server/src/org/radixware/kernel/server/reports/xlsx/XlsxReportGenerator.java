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
package org.radixware.kernel.server.reports.xlsx;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsSubReport;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsXlsxReportInfo;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.html.AdjustedCell;
import org.radixware.kernel.common.enums.EReportBandType;
import org.radixware.kernel.common.enums.EReportColumnResizeMode;
import org.radixware.kernel.common.enums.EReportExportFormat;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.types.MultilingualString;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.reports.AbstractReportGenerator;
import org.radixware.kernel.server.reports.DefaultReportFileController;
import org.radixware.kernel.server.reports.IReportFileController;
import org.radixware.kernel.server.reports.RadReportColumnDef;
import org.radixware.kernel.server.reports.ReportGenerationException;
import org.radixware.kernel.server.reports.ReportStateInfo;
import org.radixware.kernel.server.reports.csv.CsvReportGenerator;
import org.radixware.kernel.server.types.Report;

public class XlsxReportGenerator extends AbstractReportGenerator {

    private static final int WORKBOOK_WINDOW_SIZE = -1;
    
    private final AdsXlsxReportInfo xlsxReportInfo;
    private final String sheetName;
    private final Map<String, CellStyle> stylesCache = new HashMap<>();
    private final Map<Integer, Integer> maxColumnsSize = new HashMap<>();

    private SXSSFWorkbook workbook;
    private Sheet spreadsheet;

    boolean isFirstLine = true;
    int rowIndex = 0;

    public XlsxReportGenerator(Report report, ReportStateInfo stateInfo) {
        super(report, EReportExportFormat.XLSX, stateInfo);
        this.xlsxReportInfo = report.getExportXlsxInfo();
        this.workbook = new SXSSFWorkbook(WORKBOOK_WINDOW_SIZE);

        if (xlsxReportInfo != null && xlsxReportInfo.getSheetNameId() != null) {
            sheetName = MultilingualString.get(Arte.get(), report.getId(), xlsxReportInfo.getSheetNameId());
            this.spreadsheet = workbook.createSheet(sheetName);
        } else {
            this.spreadsheet = workbook.createSheet();
            sheetName = spreadsheet.getSheetName();
        }
    }

    @Override
    protected void setIsFirstLine(boolean isFirstLine) {
        this.isFirstLine = isFirstLine;
    }
    
    @Override
    protected boolean isFormattingSupported() {
        return false;
    }

    @Override
    protected boolean isInfiniteHeight() {
        return true;
    }

    @Override
    protected Map<AdsReportCell, AdjustedCell> adjustBand(AdsReportBand band) throws ReportGenerationException {
        return null;
    }

    @Override
    protected void adjustCellsPosition(AdsReportBand container) {
    }

    @Override
    protected void viewBand(final List<ReportGenData> report, AdsReportBand band, Map<AdsReportCell, AdjustedCell> adjustedCellContents, ReportGenData currentGenData) throws ReportGenerationException {
    }

    @Override
    protected boolean shouldSkipSubReport(Report subReport) {
        AdsXlsxReportInfo info = subReport.getExportXlsxInfo();
        if (info == null) {
            return true;
        }
        if (!info.isExportToXlsxEnabled()) {
            return true;
        }
        return super.shouldSkipSubReport(subReport);
    }

    @Override
    public void generateReport(final IReportFileController controller) throws ReportGenerationException {
        final IReportFileController writerManager = new DefaultReportFileController(controller) {

            @Override
            public void afterCreateFile(Report report, File file, OutputStream output) throws ReportGenerationException {
                super.afterCreateFile(report, file, output);
                workbook = new SXSSFWorkbook(WORKBOOK_WINDOW_SIZE);
                spreadsheet = workbook.createSheet(sheetName);
                rowIndex = 0;
                setIsFirstLine(true);
            }

            @Override
            public void beforeCloseFile(Report report, File file, OutputStream output) throws ReportGenerationException {
                for (Map.Entry<Integer, Integer> maxColumnWidthEntry : maxColumnsSize.entrySet()) {
                    spreadsheet.setColumnWidth(maxColumnWidthEntry.getKey(), maxColumnWidthEntry.getValue());
                }

                try {
                    workbook.write(output);
                } catch (IOException ex) {
                    throw new ReportGenerationException(ex);
                } finally {
                    workbook.dispose();
                }
                workbook = null;
                spreadsheet = null;
                
                stylesCache.clear();
                maxColumnsSize.clear();                                
                
                super.beforeCloseFile(report, file, output);
            }

        };

        super.generateReport(writerManager);
    }

    @Override
    public void generateReport(OutputStream stream) throws ReportGenerationException {
        if (xlsxReportInfo == null) {
            return;
        }
        super.generateReport(stream);
    }

    @Override
    protected void buildWithoutBand(final List<ReportGenData> genDataList, EReportBandType bandType) {
        if (bandType == EReportBandType.DETAIL) {
            try {
                Report report = genDataList.get(genDataList.size() - 1).report;
                buildXlsx(report);
            } catch (ReportGenerationException ex) {
                Logger.getLogger(XlsxReportGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    protected void buildBand(List<ReportGenData> genDataList, AdsReportBand band, EReportBandType bandType) throws ReportGenerationException {
        if (band != null) {
            for (AdsSubReport subReport : band.getPreReports()) {
                buildSubReport(genDataList, subReport);
            }
        }

        if (bandType == EReportBandType.DETAIL) {
            Report report = genDataList.get(genDataList.size() - 1).report;
            buildXlsx(report);
        }

        if (band != null) {
            for (AdsSubReport subReport : band.getPostReports()) {
                buildSubReport(genDataList, subReport);
            }
        }
    }

    @Override
    protected void finishPage(List<ReportGenData> genDataList) throws ReportGenerationException {
        Report report = genDataList.get(genDataList.size() - 1).report;
        rowIndex += report.afterLastXlsxRowWrite(spreadsheet);
        super.finishPage(genDataList);
    }

    @Override
    protected void afterSubReportClose(SubReportGenData subReportGenData) {
        Report report = subReportGenData.report;
        rowIndex += report.afterLastXlsxRowWrite(spreadsheet);
        super.afterSubReportClose(subReportGenData);
    }

    private void buildXlsx(Report report) throws ReportGenerationException {        
        List<RadReportColumnDef> reportColumns = getReportColumnsList(report);
        
        if (isFirstLine) {
            rowIndex += report.beforeFirstXlsxRowWrite(spreadsheet);
            if (xlsxReportInfo.isExportColumnName()) {
                rowIndex += report.beforeXlsxRowWrite(spreadsheet);

                Row headerRow = spreadsheet.createRow(rowIndex++);
                if (reportColumns != null && !reportColumns.isEmpty()) {
                    for (int cellnum = 0; cellnum < reportColumns.size(); cellnum++) {
                        RadReportColumnDef column = reportColumns.get(cellnum);
                        String columnName = Utils.emptyOrNull(column.getTitle()) ? column.getName() : column.getTitle();

                        Cell cell = headerRow.createCell(cellnum);
                        cell.setCellValue(columnName);

                        if (column.getXlsxExportParameters() != null) {
                            EReportColumnResizeMode resizeMode = column.getXlsxExportParameters().getResizeMode();
                            if (resizeMode == EReportColumnResizeMode.BY_HEADER) {
                                spreadsheet.autoSizeColumn(cellnum);
                            } else if (resizeMode == EReportColumnResizeMode.MANUAL) {
                                int width = column.getXlsxExportParameters().getWidth();
                                spreadsheet.setColumnWidth(cellnum, width * 256);
                            }
                        }
                    }
                } else {
                    try {
                        int fieldCount = report.getResultSet().getMetaData().getColumnCount();
                        for (int cellnum = 0; cellnum < fieldCount; cellnum++) {
                            String columnName = report.getResultSet().getMetaData().getColumnName(cellnum + 1);

                            Cell cell = headerRow.createCell(cellnum);
                            cell.setCellValue(columnName);

                            spreadsheet.autoSizeColumn(cellnum);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(CsvReportGenerator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                rowIndex += report.afterXlsxRowWrite(headerRow);
            }
            isFirstLine = false;
        }

        Row currentDataRow;
        if (reportColumns != null && !reportColumns.isEmpty()) {
            if (report.isXlsxRowVisible()) {
                rowIndex += report.beforeXlsxRowWrite(spreadsheet);

                currentDataRow = spreadsheet.createRow(rowIndex++);
                for (int cellnum = 0; cellnum < reportColumns.size(); cellnum++) {
                    RadReportColumnDef column = reportColumns.get(cellnum);

                    Id propertyId = column.getPropertyId();
                    RadPropDef property = report.getRadMeta().getPropById(propertyId);

                    Cell cell = currentDataRow.createCell(cellnum);
                    writePropertyValue(report, cell, property);

                    CellStyle style = getColumnStyle(column);
                    if (style != null) {
                        cell.setCellStyle(style);
                    }
                }
                rowIndex += report.afterXlsxRowWrite(currentDataRow);
                updateMaxColumnsWidth(reportColumns);
            }
        } else {
            Object[] cache = report.getCache();
            if (cache != null) {
                rowIndex += report.beforeXlsxRowWrite(spreadsheet);

                currentDataRow = spreadsheet.createRow(rowIndex++);
                String cellValue = null;
                for (int i = 0; i < cache.length; i++) {
                    Object obj = cache[i];
                    if (obj instanceof Clob) {
                        Clob clob = (Clob) obj;
                        try {
                            StringBuilder sb = new StringBuilder();
                            Reader reader = clob.getCharacterStream();
                            char cbuf[] = new char[1024];
                            int offset = 0;
                            while (true) {
                                try {
                                    int curLen = reader.read(cbuf, offset, cbuf.length);
                                    if (curLen < 1) {
                                        break;
                                    }
                                    sb.append(cbuf, 0, curLen);
                                } catch (IOException ex) {
                                    Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            cellValue = sb.toString();
                        } catch (SQLException ex) {
                            Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (obj instanceof Blob) {
                        cellValue = null;
                    } else {
                        cellValue = obj == null ? null : obj.toString();
                    }

                    Cell cell = currentDataRow.createCell(i);
                    cell.setCellValue(cellValue);
                }
                rowIndex += report.afterXlsxRowWrite(currentDataRow);
            }
        }
        try {
            ((SXSSFSheet) spreadsheet).flushRows();
        } catch (IOException ex) {
            throw new ReportGenerationException(ex);
        }
    }

    private void writePropertyValue(Report report, Cell cell, RadPropDef property) {
        Object propValue = getPropertyValueById(report, property.getId());
        switch (property.getValType()) {
            case BOOL:
                if (propValue instanceof Boolean) {
                    Boolean boolPropValue = (Boolean) propValue;
                    cell.setCellValue(boolPropValue);
                    break;
                }
            case INT:
                if (propValue instanceof Long) {
                    Long longPropValue = (Long) propValue;
                    cell.setCellValue(longPropValue.doubleValue());
                    break;
                }
            case NUM:
                if (propValue instanceof BigDecimal) {
                    BigDecimal bigDecimalPropValue = (BigDecimal) propValue;
                    cell.setCellValue(bigDecimalPropValue.doubleValue());
                    break;
                }
            case DATE_TIME:
                if (propValue instanceof Date) {
                    Date datePropValue = (Date) propValue;
                    cell.setCellValue(datePropValue);
                    break;
                }
            default:
                String stringPropValue = formatProperty(report, property.getId(), null);
                cell.setCellValue(stringPropValue == null ? "" : stringPropValue);
        }
    }

    private void updateMaxColumnsWidth(List<RadReportColumnDef> reportColumns) {
        for (int cellnum = 0; cellnum < reportColumns.size(); cellnum++) {
            RadReportColumnDef column = reportColumns.get(cellnum);
            EReportColumnResizeMode resizeMode = column.getXlsxExportParameters() == null ? EReportColumnResizeMode.NONE : column.getXlsxExportParameters().getResizeMode();

            if (maxColumnsSize.containsKey(cellnum)) {
                spreadsheet.autoSizeColumn(cellnum);
                int columnWidth = spreadsheet.getColumnWidth(cellnum);
                if (columnWidth > maxColumnsSize.get(cellnum)) {
                    maxColumnsSize.put(cellnum, columnWidth);
                }
            } else {
                if (resizeMode == EReportColumnResizeMode.BY_CONTENT) {
                    spreadsheet.autoSizeColumn(cellnum);
                    maxColumnsSize.put(cellnum, spreadsheet.getColumnWidth(cellnum));
                }
            }
        }
    }

    private CellStyle getColumnStyle(RadReportColumnDef column) {
        CellStyle style = null;

        if (column.getXlsxExportParameters() != null) {
            String xlsxExportFormat = column.getXlsxExportParameters().getXlsxExportFormat();
            if (!Utils.emptyOrNull(xlsxExportFormat)) {
                if (stylesCache.containsKey(xlsxExportFormat)) {
                    style = stylesCache.get(xlsxExportFormat);
                } else {
                    style = workbook.createCellStyle();
                    style.setDataFormat((short) workbook.createDataFormat().getFormat(xlsxExportFormat));
                    stylesCache.put(xlsxExportFormat, style);
                }
            }
        }

        return style;
    }

    private List<RadReportColumnDef> getReportColumnsList(Report report) {
        List<RadReportColumnDef> result = new ArrayList<>();
        for (RadReportColumnDef column : report.getVisibleColumns()) {
            if (column.getPropertyId() != null) {
                result.add(column);
            }
        }
        
        return result;
    }
}
