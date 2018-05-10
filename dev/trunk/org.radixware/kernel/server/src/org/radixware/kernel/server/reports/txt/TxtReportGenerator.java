/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.reports.txt;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidget;
import org.radixware.kernel.common.enums.EReportCellHAlign;
import org.radixware.kernel.common.enums.EReportExportFormat;
import org.radixware.kernel.server.reports.AbstractReportGenerator;
import org.radixware.kernel.server.reports.DefaultReportFileController;
import org.radixware.kernel.server.reports.IReportFileController;
import org.radixware.kernel.server.reports.ReportGenerationException;
import org.radixware.kernel.server.reports.ReportStateInfo;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.html.AdjustedCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.html.CellContents;
import org.radixware.kernel.common.enums.EReportBandType;
import org.radixware.kernel.server.reports.ReportColumnsAdjuster;
import org.radixware.kernel.server.types.Report;

/**
 *
 * @author akrylov
 */
public class TxtReportGenerator extends AbstractReportGenerator {

    private PrintStream writer = System.out;
    private int curPageHeight = 0;
    private int pageHeight = 0;
    private boolean isFirstPage = true;
    private final String encoding;
    private TxtFooterWriter footerWriter;
    

    public TxtReportGenerator(Report report, String encoding, ReportStateInfo stateInfo) {
        super(report, EReportExportFormat.TXT, AdsReportForm.Mode.TEXT, stateInfo);
        this.encoding = encoding;
    }

    public TxtReportGenerator(Report report) {
        this(report, null, null);
    }

    @Override
    protected boolean isFormattingSupported() {
        return true;
    }

    @Override
    protected Map<AdsReportCell, AdjustedCell> adjustBand(AdsReportBand band) throws ReportGenerationException {
        final CellsAdjuster cellsAdjuster = new CellsAdjuster();

        cellsAdjuster.adjustCellContent(band);
        int height = cellsAdjuster.adjustCellsHeight(band);

        if (band.isAutoHeight()) {
            band.setHeightRows(height);
        }
        return cellsAdjuster.getAdjustedCellContents();
    }

    @Override
    protected void adjustCellsPosition(AdsReportBand container) {
        final CellsAdjuster cellsAdjuster = new CellsAdjuster();
        cellsAdjuster.adjustCellsPosition(container);
    }

    @Override
    protected void calcTableStructure(AdsReportForm form) {
        super.calcTableStructure(form);
    }

    @Override
    protected void viewBand(List<ReportGenData> genDataList, AdsReportBand band, Map<AdsReportCell, AdjustedCell> adjustedCellContents, ReportGenData currentGenData) throws ReportGenerationException {

        List<AdsReportWidget> widgets = band.getWidgets().list();
        List<AdsReportCell> matchedWidgets = new ArrayList<>();

        for (int currentRow = 0, currentRowInPage = 0;; currentRow++, currentRowInPage++) {
            if (widgets.isEmpty() && currentRow >= band.getHeightRows()) {
                break;
            }
            for (int i = 0; i < widgets.size();) {
                AdsReportWidget w = widgets.get(i);
                int top = w.getTopRow();
                if (top > currentRow) {
                    i++;
                    continue;
                }
                int end = top + w.getHeightRows() - 1;
                if (end < currentRow) {
                    widgets.remove(i);
                    continue;
                } else {
                    i++;
                }
                matchedWidgets.add((AdsReportCell) w);
            }
            Collections.sort(matchedWidgets, new Comparator<AdsReportWidget>() {

                @Override
                public int compare(AdsReportWidget o1, AdsReportWidget o2) {
                    return o1.getLeftColumn() > o2.getLeftColumn() ? 1 : o1.getLeftColumn() < o2.getLeftColumn() ? -1 : 0;
                }
            });
            int currHeight = getCurHeightRows();
            if (!matchedWidgets.isEmpty()) {
                if (band.isMultiPage()) {
                    if (currHeight + currentRowInPage >= getPageHeight(band)) {
                        currentRowInPage = 0;
                        finishPage(genDataList);
                        newPage(genDataList);
                    }
                }
            }
            //if (currentRowInPage > 0/* || matchedWidgets.isEmpty()*/) {
            if (matchedWidgets.isEmpty() || currentRowInPage != 0) {
                println();
            }
            //}
            if (!matchedWidgets.isEmpty()) {
                //     if (!newLineStarted) {
                //  println();
                //        newLineStarted = false;
                //    }
                int col = 0;
                for (AdsReportCell cell : matchedWidgets) {
                    while (col < cell.getLeftColumn()) {
                        print(' ');
                        col++;
                    }
                    int lineInCell = currentRow - cell.getTopRow();
                    AdjustedCell adjusted = adjustedCellContents == null ? null : adjustedCellContents.get(cell);
                    String content = "";
                    
                    if (adjusted == null || adjusted.getLinesCount() <= lineInCell) {
                        if (lineInCell == 0) {
                            content = cell.getRunTimeContent();
                        }
                    } else {
                        List<CellContents> listOfContents = adjusted.getContentByLineInCell(lineInCell);
                        StringBuilder sb = new StringBuilder();
                        for (CellContents sc : listOfContents) {
                            sb.append(sc.getText());
                        }
                        content = sb.toString();
                    }
                    content = content.replaceAll("[\r\n]", ""); //npopov (RADIX-11761): trim line-separators 
                    if (content.length() > cell.getWidthCols()) {
                        content = content.substring(0, cell.getWidthCols());
                    }
                    EReportCellHAlign align = cell.getHAlign();
                    int diff = cell.getWidthCols() - content.length();
                    if (!cell.isUseTxtPadding()) {
                        diff = 0;
                        align = EReportCellHAlign.LEFT;
                    }
                    StringBuilder result = new StringBuilder();
                    switch (align) {
                        case CENTER:
                            String trimmed = content.trim();
                            diff += content.length() - trimmed.length();
                            content = trimmed;
                            int prefix = diff / 2;
                            for (int i = 0; i < prefix; i++) {
                                result.append(' ');
                            }
                            result.append(content);
                            for (int i = result.length(); i < cell.getWidthCols(); i++) {
                                result.append(' ');
                            }
                            break;
                        case RIGHT:
                            trimmed = content.trim();
                            diff += content.length() - trimmed.length();
                            content = trimmed;
                            for (int i = 0; i < diff; i++) {
                                result.append(' ');
                            }
                            result.append(content);
                            break;
                        default:
                            result.append(content);
                            for (int i = 0; i < diff; i++) {
                                result.append(' ');
                            }
                    }
                    content = result.toString();
                    print(content);
                    col += content.length();
                }
                matchedWidgets.clear();
            }
        }
    }

//    @Override
//    public void generateReport(OutputStream stream) throws ReportGenerationException {
//        this.writer = new PrintStream(stream);
//        super.generateReport(stream);
//    }
    private class DebugStream extends OutputStream {

        OutputStream out;
        private ByteArrayOutputStream dummy = new ByteArrayOutputStream();

        public DebugStream(OutputStream out) {
            this.out = out;

        }

        @Override
        public void write(int b) throws IOException {
            out.write(b);
            dummy.write(b);
        }

        @Override
        public void flush() throws IOException {
            out.flush();
        }

        @Override
        public void close() throws IOException {
            out.close();
        }

        @Override
        public String toString() {
            byte[] bytes = dummy.toByteArray();
            return new String(bytes, 0, bytes.length);
        }

    }
    private DebugStream debug = null;

    @Override
    public void generateReport(final IReportFileController controller) throws ReportGenerationException {
        final IReportFileController writerManager = new DefaultReportFileController(controller) {

            @Override
            public void afterCreateFile(Report report, File file, OutputStream output) throws ReportGenerationException {
                super.afterCreateFile(report, file, output);
                if (encoding == null) {
                    writer = new PrintStream(debug = new DebugStream(output));
                } else {
                    try {
                        writer = new PrintStream(debug = new DebugStream(output), false, encoding);
                    } catch (UnsupportedEncodingException ex) {
                        writer = new PrintStream(debug = new DebugStream(output));
                    }
                }
                writeNewLine = false;
            }

            @Override
            public void beforeCloseFile(Report report, File file, OutputStream output) throws ReportGenerationException {
                if (writer != null) {
                    writer.flush();
                }
                super.beforeCloseFile(report, file, output);
            }

        };

        super.generateReport(writerManager);
    }

    boolean writeNewLine = false;

    private void println() {
        curPageHeight++;
        if (writeNewLine) {
            writer.println();
        }
        writeNewLine = true;

    }

    private void print(char c) {
        if (writeNewLine) {
            writer.println();
            writeNewLine = false;
        }
        writer.print(c);
    }

    private void print(String str) {
        if (writeNewLine) {
            writer.println();
            writeNewLine = false;
        }
        writer.print(str);
    }

    private int getPageHeight(final AdsReportBand band) {
        if (band.getType() == EReportBandType.PAGE_FOOTER) {
            return getRootForm().getPageHeightRows();
        }
        if (pageHeight == 0) {
            pageHeight = getRootForm().getPageHeightRows() - getCurPageFooterHeightRows();
        }
        return pageHeight;
    }

    @Override
    protected void newPage(final List<ReportGenData> genDataList) throws ReportGenerationException {
        if (!isFirstPage) {
            print('\f');
        } else {
            isFirstPage = false;
        }
        curPageHeight = 0;
        pageHeight = 0;
        super.newPage(genDataList);
        adjustFooter(genDataList);
    }

    @Override
    protected void buildPageFooter(List<ReportGenData> genDataList) throws ReportGenerationException {
        if (footerWriter == null) {
            super.buildPageFooter(genDataList);
        } else {
            String footer = footerWriter.toString();
            if (!footer.isEmpty()) {
                print(footer);
            }
        }
    }
    
    

    @Override
    protected void finishPage(List<ReportGenData> genDataList) throws ReportGenerationException {
        if (!genDataList.isEmpty()) {
            int footerSize = getCurPageFooterHeightRows();
            while (curPageHeight + footerSize < getRootForm().getPageHeightRows()) {
                println();
            }
        }
        super.finishPage(genDataList); 
        writeNewLine = false;
        if (footerWriter != null) {
            footerWriter.close();
            footerWriter = null;
        }
    }

    @Override
    protected void adjustCellsToColumns(AdsReportBand container) {
        ReportColumnsAdjuster adjuster = new ReportColumnsAdjuster(container, columnsSettings, new TxtCellWrapperFactory());
        adjuster.adjustColumnsBySettings();
    }

    @Override
    protected boolean isFlowDependent() {
        return super.isFlowDependent(); //To change body of generated methods, choose Tools | Templates.
    }

    protected void adjustFooter(List<ReportGenData> genDataList) {
        PrintStream curWriter = writer;
        int curHeight = getCurHeightRows();
        int cPageHeight = curPageHeight;
        int height = pageHeight;
        boolean newLine = writeNewLine;
        try {
            if (encoding == null) {
                writer = TxtFooterWriter.Factory.newInstance();
            } else {
                try {
                    writer = TxtFooterWriter.Factory.newInstance(encoding);
                } catch (UnsupportedEncodingException ex) {
                    writer = TxtFooterWriter.Factory.newInstance();
                }
            }
            super.buildPageFooter(genDataList);
        } catch (Throwable t) {
        } finally {
            footerWriter = (TxtFooterWriter) writer;
            setFooterHeight(curPageHeight, 0);
            footerWriter.flush();
            writer = curWriter;
            setCurHeightRows(curHeight);
            curPageHeight = cPageHeight;
            pageHeight = height;
            writeNewLine = newLine;
        }
    }

    @Override
    protected void closeAllResources() {
        super.closeAllResources();
        if (footerWriter != null) {
            footerWriter.close();
        }
    }
    
    
}
