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
package org.radixware.kernel.server.reports.csv;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsCsvReportInfo;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsCsvReportInfo.CsvExportColumns;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsExportCsvColumn;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsSubReport;
import org.radixware.kernel.common.enums.EReportBandType;
import org.radixware.kernel.common.enums.EReportExportFormat;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.reports.AbstractReportGenerator;
import org.radixware.kernel.server.reports.DefaultReportFileController;
import org.radixware.kernel.server.reports.IReportFileController;
import org.radixware.kernel.server.reports.ReportGenerationException;
import org.radixware.kernel.server.reports.ReportStateInfo;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.html.AdjustedCell;
import org.radixware.kernel.server.types.Report;

public class CsvReportGenerator extends AbstractReportGenerator {

    CsvReportWriter.Writer writer = null;
    boolean isFirstLine = true;
    AdsCsvReportInfo csvReportInfo;
    private final String encoding;

    public CsvReportGenerator(Report report, String encoding, ReportStateInfo stateInfo) {
        super(report, EReportExportFormat.CSV, stateInfo);
        this.encoding = encoding;        
        csvReportInfo = report.getExportCsvInfo();
    }

    @Override
    protected void setIsFirstLine(boolean isFirstLine) {
        this.isFirstLine = isFirstLine;
    }
    
    @Override
    protected boolean isInfiniteHeight() {
        return true;
    }

    @Override
    protected boolean isFormattingSupported() {
        return true;
    }

    @Override
    protected Map<AdsReportCell, AdjustedCell> adjustBand(AdsReportBand band) throws ReportGenerationException {
        // NOTHING - XML doesn't required any formatting
        return null;
    }

    @Override
    protected void adjustCellsPosition(AdsReportBand container) {
        // NOTHING - csv doesn't required any formatting
    }

    @Override
    protected void viewBand(final List<ReportGenData> report, AdsReportBand band, Map<AdsReportCell, AdjustedCell> adjustedCellContents, ReportGenData currentGenData) throws ReportGenerationException {

    }

    private void buildCsv(Report report) {
        final AdsCsvReportInfo localCsvReportInfo = report.getExportCsvInfo();
        final CsvExportColumns exportCsvColumns = localCsvReportInfo.getExportCsvColumns();        
        
        //print fields header
        if (isFirstLine) {
            if (localCsvReportInfo.isExportColumnName()) {
                if (exportCsvColumns != null && !exportCsvColumns.isEmpty()) {
                    for (AdsExportCsvColumn csvColumn : exportCsvColumns) {
                        Id propId = csvColumn.getPropId(); //extNames
                        String extName = csvColumn.getExtName();
                        if (extName != null && !extName.isEmpty()) {
                            writer.value(extName);
                        } else {
                            final RadPropDef prop = report.getRadMeta().getPropById(propId);
                            writer.value(prop.getName());
                        }
                    }
                    writer.newLine();
                } else {

                    try {
                        int fieldCount = report.getResultSet().getMetaData().getColumnCount();
                        for (int i = 0; i < fieldCount; i++) {
                            writer.value(report.getResultSet().getMetaData().getColumnName(i + 1));
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(CsvReportGenerator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    writer.newLine();
                }
            }
            isFirstLine = false;
        }
        //print data
        if (exportCsvColumns != null && !exportCsvColumns.isEmpty()) {
            if (report.isCsvRowVisible()) {
                for (AdsExportCsvColumn csvColumn : exportCsvColumns) {
                    Id propId = csvColumn.getPropId();
                    final String value = formatProperty(report, propId, csvColumn.getFormat());
                    //final Object value = getPropertyValueById(report, propId);
                    writer.value(value != null ? value : null);
                }
                writer.newLine();
            }
        } else {
            Object[] cache = report.getCache();
            if (cache != null) {
                for (int i = 0, n = cache.length; i < n; i++) {
                    Object obj = cache[i];
                    //if (obj!=null)
                    {
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
                                writer.value(sb.toString());
                            } catch (SQLException ex) {
                                Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
                            }
//                            } else if (obj instanceof oracle.sql.ANYDATA
//                                    || obj instanceof oracle.sql.BFILE
//                                    || obj instanceof oracle.sql.BLOB
//                                    || obj instanceof oracle.sql.RAW) {
//                                writer.value(null);
//  TODO: How detect large data?  
                        } else if (obj instanceof Blob) {
                            writer.value(null);
                        } else {
                            writer.value(obj == null ? null : obj.toString());
                        }
                    }
                }
                writer.newLine();
            }
        }
    }
        
    @Override
    protected void buildBand(List<ReportGenData> genDataList, AdsReportBand band, EReportBandType bandType) throws ReportGenerationException {
        if (band!=null){
            for (AdsSubReport subReport : band.getPreReports()) {
                buildSubReport(genDataList, subReport);
            }
        }
        if (bandType == EReportBandType.DETAIL) {//called for each row in resultset
            Report report = genDataList.get(genDataList.size() - 1).report;
            buildCsv(report);
        }
        if (band!=null){
            for (AdsSubReport subReport : band.getPostReports()) {
                buildSubReport(genDataList, subReport);
            }
        }
    }

    @Override
    protected boolean shouldSkipSubReport(Report subReport) {
        AdsCsvReportInfo info = subReport.getExportCsvInfo();
        if (info == null) {
            return true;
        }
        if (!info.isExportToCsvEnabled()) {
            return true;
        }
        return super.shouldSkipSubReport(subReport);
    }

    @Override
    public void generateReport(final IReportFileController controller) throws ReportGenerationException {
        final IReportFileController wrapper = new DefaultReportFileController(controller) {

            @Override
            public void afterCreateFile(Report report, File file, OutputStream output) throws ReportGenerationException {
                super.afterCreateFile(report, file, output);
                isFirstLine = true;
                writer = new CsvReportWriter.Writer(report, output, encoding);
                if (report.getExportCsvInfo() != null) {
                    writer.setDelimiter(report.getExportCsvInfo().getCharDelimiter());
                }
                report.configureCsvWriter(writer);
            }

            @Override
            public void beforeCloseFile(Report report, File file, OutputStream output) throws ReportGenerationException {
                writer.flush();
                super.beforeCloseFile(report, file, output);
            }
        };

        super.generateReport(wrapper);
    }

    @Override
    public void generateReport(OutputStream stream) throws ReportGenerationException {
        if (csvReportInfo == null) {
            return;
        }
        super.generateReport(stream);
//        AdsCsvReportInfo csvReportInfo = report.getExportCsvInfo();
//        if (csvReportInfo != null) {
//            writer = new CsvReportWriter.Writer(stream, encoding);
//            char delimiter = csvReportInfo.getCharDelimiter();
//            writer.setDelimiter(delimiter);
//            CsvExportColumns eportCsvColumns = csvReportInfo.getExportCsvColumns();
//            if (eportCsvColumns != null && !eportCsvColumns.isEmpty()) {
//                if (csvReportInfo.isExportColumnName()) {
//                    for (AdsExportCsvColumn csvColumn : eportCsvColumns) {
//                        Id propId = csvColumn.getPropId(); //extNames
//                        String extName = csvColumn.getExtName();
//                        if (extName != null && !extName.isEmpty()) {
//                            writer.value(extName);
//                        } else {
//                            final RadPropDef prop = report.getRadMeta().getPropById(propId);
//                            writer.value(prop.getName());
//                        }
//                    }
//                    writer.newLine();
//                }
//                while (report.next()) {      //values  
//                    if (report.isCsvRowVisible()) {
//                        for (AdsExportCsvColumn csvColumn : eportCsvColumns) {
//                            Id propId = csvColumn.getPropId();
//                            final String value = formatProperty(report, propId, csvColumn.getFormat());
//                            //final Object value = getPropertyValueById(report, propId);
//                            writer.value(value != null ? value : null);
//                        }
//                        writer.newLine();
//                    }
//                }
//            } else {
//                try {
//                    int fieldCount = report.getResultSet().getMetaData().getColumnCount();
//                    for (int i = 0; i < fieldCount; i++) {
//                        writer.value(report.getResultSet().getMetaData().getColumnName(i + 1));
//                    }
//                } catch (SQLException ex) {
//                    Logger.getLogger(CsvReportGenerator.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                writer.newLine();
//
//                Object[] cache = report.getCache();
//                boolean isRowVisible = cache == null ? false : report.isCsvRowVisible();
//                while (true) {
//                    report.next();
//                    if (cache == null) {
//                        cache = report.getCache();
//                    }
//
//                    if (cache != null && isRowVisible) {
//                        for (int i = 0, n = cache.length; i < n; i++) {
//                            Object obj = cache[i];
//                            //if (obj!=null)
//                            {
//                                if (obj instanceof oracle.sql.CLOB) {
//                                    oracle.sql.CLOB clob = (oracle.sql.CLOB) obj;
//                                    try {
//                                        StringBuilder sb = new StringBuilder();
//                                        Reader reader = clob.getCharacterStream();
//                                        char cbuf[] = new char[1024];
//                                        int offset = 0;
//                                        while (true) {
//                                            try {
//                                                int curLen = reader.read(cbuf, offset, cbuf.length);
//                                                if (curLen < 1) {
//                                                    break;
//                                                }
//                                                sb.append(cbuf, 0, curLen);
//                                            } catch (IOException ex) {
//                                                Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
//                                            }
//                                        }
//                                        writer.value(sb.toString());
//                                    } catch (SQLException ex) {
//                                        Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
//                                    }
//
//                                } else if (obj instanceof oracle.sql.ANYDATA
//                                        || obj instanceof oracle.sql.BFILE
//                                        || obj instanceof oracle.sql.BLOB
//                                        || obj instanceof oracle.sql.RAW) {
//                                    writer.value(null);
//                                } else {
//                                    writer.value(obj == null ? null : obj.toString());
//                                }
//                            }
//                        }
//                        writer.newLine();
//                    }
//                    cache = null;
//                    if (!report.isWasData()) {
//                        break;
//                    } else {
//                        isRowVisible = report.isCsvRowVisible();
//                    }
//
//                }
//            }
        //}
    }

    @Override
    protected void buildWithoutBand(final List<ReportGenData> genDataList, EReportBandType bandType) {
        if (bandType == EReportBandType.DETAIL) {
            Report report = genDataList.get(genDataList.size() - 1).report;
            buildCsv(report);
        }
    }

}
