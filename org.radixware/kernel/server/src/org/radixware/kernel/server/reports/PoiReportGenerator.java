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
package org.radixware.kernel.server.reports;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.poi.hssf.record.PaletteRecord;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportAbstractAppearance.Font;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportGroup;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidget;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidgetContainer;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsSubReport;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.IReportWidgetContainer;
import org.radixware.kernel.common.enums.EReportBandType;
import static org.radixware.kernel.common.enums.EReportBorderStyle.DASHED;
import static org.radixware.kernel.common.enums.EReportBorderStyle.DOTTED;
import static org.radixware.kernel.common.enums.EReportBorderStyle.SOLID;
import static org.radixware.kernel.common.enums.EReportCellHAlign.CENTER;
import static org.radixware.kernel.common.enums.EReportCellHAlign.JUSTIFY;
import static org.radixware.kernel.common.enums.EReportCellHAlign.LEFT;
import static org.radixware.kernel.common.enums.EReportCellHAlign.RIGHT;
import org.radixware.kernel.common.enums.EReportCellType;
import static org.radixware.kernel.common.enums.EReportCellVAlign.BOTTOM;
import static org.radixware.kernel.common.enums.EReportCellVAlign.MIDDLE;
import org.radixware.kernel.common.enums.EReportExportFormat;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Base64;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.html.AdjustedCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.html.CellContents;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.html.CellParagraph;
import org.radixware.kernel.common.enums.EReportBorderStyle;
import org.radixware.kernel.server.reports.fo.CellsAdjuster;
import org.radixware.kernel.server.reports.fo.ChartBuilder;
import org.radixware.kernel.server.types.Report;

public class PoiReportGenerator extends AbstractReportGenerator {

    private Workbook workbook;
    private Sheet spreadsheet;
    private Map<Id, CellsAdjuster.Guides> reportsColumnGuides = new HashMap<>();
    private Map<Id, Map<String, BandInfo>> bandRowsInfo = new HashMap<>();
    private short lastUsedColorIndex = PaletteRecord.FIRST_COLOR_INDEX;
    private byte[] usedColor = new byte[PaletteRecord.FIRST_COLOR_INDEX + PaletteRecord.STANDARD_PALETTE_SIZE];
    private static final int MAX_CELL_LENGTH = 32767;
    private static final int UNIT_IN_POINT_HEIGHT = 23;
    private static final int MAX_WIDTH = 256;
    private static final double FONT_WIDTH = 5.5;
    private static final double POINT_IN_MM = 0.35;
    private Report report;
    private int rowIndex = 0;
    private double MM_TO_INCH = 0.0393700787;

    //private Map<AdsReportBand,List<AdsReportWidget>> cellsByBand=new HashMap<>();
    private class BandInfo {

        private CellsAdjuster.Guides rowGuides;
        private List<MergeStructure> mergeStructure;
        private List<AdsReportWidget> cells;
        private EReportBandType bandType;

        BandInfo(final CellsAdjuster.Guides rowGuides, final List<MergeStructure> mergeStructure) {
            this.rowGuides = rowGuides;
            this.mergeStructure = mergeStructure;
        }

        BandInfo(final List<AdsReportWidget> cells) {
            this.cells = cells;
        }

        void setRowGuides(final CellsAdjuster.Guides rowGuides) {
            this.rowGuides = rowGuides;
        }

        void setMergeStructure(final List<MergeStructure> mergeStructure) {
            this.mergeStructure = mergeStructure;
        }

        CellsAdjuster.Guides getRowGuides() {
            return rowGuides;
        }

        List<AdsReportWidget> getCells() {
            return cells;
        }

        List<MergeStructure> getMergeStructure() {
            return mergeStructure;
        }
    }

    private class MergeStructure {

        final int startRow;
        final int endRow;
        final int startColumn;
        final int endColum;

        MergeStructure(final int startRow, final int endRow, final int startColumn, final int endColum) {
            this.startRow = startRow;
            this.endRow = endRow;
            this.startColumn = startColumn;
            this.endColum = endColum;
        }
    }


    public PoiReportGenerator(final Report report, ReportStateInfo stateInfo) {
        this(report, stateInfo, false);
    }

    public PoiReportGenerator(final Report report, ReportStateInfo stateInfo, boolean xlsx) {
        super(report, EReportExportFormat.OOXML, stateInfo);
        this.report = report;
        workbook = xlsx ? new XSSFWorkbook() : new HSSFWorkbook();
        spreadsheet = workbook.createSheet("new sheet");
    }

    @Override
    protected boolean isFormattingSupported() {
        return true;
    }

    @Override
    protected Map<AdsReportCell, AdjustedCell> adjustBand(final AdsReportBand band) throws ReportGenerationException {
        final CellsAdjuster cellsAdjuster = new CellsAdjuster();

        cellsAdjuster.adjustCellContent(band);
        cellsAdjuster.adjustCellsHeight(band);

        return cellsAdjuster.getAdjustedCellContents();
    }

    @Override
    protected void calcTableStructure(final AdsReportForm form) {
        calcTableStructure(report.getId(), form);
    }
    
    protected void calcTableStructure(final Id reportId, final AdsReportForm form) {
        List<AdsReportWidget> cells = getReportCells(form, reportId);
        reportsColumnGuides.put(reportId, CellsAdjuster.findHorizontalGuides(cells));
        calcBandInfo(form, reportId);
    }
    
    private List<AdsReportWidget> getReportCells(final AdsReportForm form, final Id reportId) {
        List<AdsReportWidget> cells = new ArrayList<>();
        getAllBandCells(form.getTitleBands(), reportId, cells);
        getAllBandCells(form.getColumnHeaderBands(), reportId, cells);
        getAllBandCells(form.getDetailBands(), reportId, cells);
        getAllBandCells(form.getSummaryBands(), reportId, cells);
        for (int i = 0; i < form.getGroups().size(); i++) {
            AdsReportGroup group = form.getGroups().get(i);
            getAllBandCells(group.getHeaderBand(), reportId, cells);
            getAllBandCells(group.getFooterBand(), reportId, cells);
        }
        return cells;
    }

    private void calcBandInfo(final AdsReportForm form, final Id reportId) {
        calcBandInfo(form.getTitleBands(), reportId);
        calcBandInfo(form.getColumnHeaderBands(), reportId);
        calcBandInfo(form.getDetailBands(), reportId);
        calcBandInfo(form.getSummaryBands(), reportId);
        for (AdsReportGroup group : form.getGroups()) {
            calcBandInfo(group.getHeaderBand(), reportId);
            calcBandInfo(group.getFooterBand(), reportId);
        }
    }

    private void calcBandInfo(final AdsReportForm.Bands bands, final Id reportId) {
        for (AdsReportBand band : bands) {
            calcBandInfo(band, reportId);
        }
    }

    private void calcBandInfo(final AdsReportBand band, final Id reportId) {
        if (band != null) {
            Map<String, BandInfo> map = bandRowsInfo.get(reportId);
            BandInfo bundInfo = map.get(band.getName());
            if (bundInfo != null) {
                getBandRowsInfo(reportId, bundInfo);
            }
        }
    }

    private void getAllBandCells(final AdsReportForm.Bands bands, final Id reportId, List<AdsReportWidget> cells) {
        for (AdsReportBand band : bands) {
            getAllBandCells(band, reportId, cells);
        }
    }

    private void getAllBandCells(final AdsReportBand band, final Id reportId, List<AdsReportWidget> cells) {

        if (band != null) {
            List<? extends AdsReportWidget> bandCells = getBandCells(band);
            cells.addAll(bandCells);
            if (bandRowsInfo.containsKey(reportId)) {
                Map<String, BandInfo> map = bandRowsInfo.get(reportId);
                map.put(band.getName(), new BandInfo(new ArrayList<>(bandCells)));
            } else {
                Map<String, BandInfo> map = new HashMap<>();
                map.put(band.getName(), new BandInfo(new ArrayList<>(bandCells)));
                bandRowsInfo.put(reportId, map);
            }

        }
    }

    protected void adjustCellsPosition(final AdsReportForm.Bands container) {
        for (AdsReportBand band : container) {
            adjustCellsPosition(band);
        }
    }

    @Override
    protected void adjustCellsPosition(final AdsReportBand container) {
        final CellsAdjuster cellsAdjuster = new CellsAdjuster();
        cellsAdjuster.adjustCellsPosition(container);
    }

    private void getBandRowsInfo(final Id reportId, final BandInfo bandInfo) {
        List<AdsReportWidget> cells = new ArrayList<>();
        cells.addAll(bandInfo.getCells());
        CellsAdjuster.Guides rowGuides = CellsAdjuster.findVerticalGuides(cells);
        
        List<MergeStructure> mergeStructureList = new ArrayList<>();
        CellsAdjuster.Guides columnGuides = reportsColumnGuides.get(reportId);
        if (columnGuides == null) {
            return;
        }
        for (AdsReportWidget w : cells) {
            int startColumn = 0, endColumn = 0, startRow = 0, endRow = 0;
            for (int i = 0; i < rowGuides.size(); i++) {
                CellsAdjuster.Guide rowGuide = rowGuides.get(i);
                if (rowGuide.getNextCells().contains(w)) {
                    startRow = i;
                } else if (rowGuide.getPrevCells().contains(w)) {
                    endRow = i;
                    break;
                }
            }
            for (int j = 0; j < columnGuides.size(); j++) {
                CellsAdjuster.Guide columnGuide = columnGuides.get(j);
                if (columnGuide.getNextCells().contains(w)) {
                    startColumn = j;
                } else if (columnGuide.getPrevCells().contains(w)) {
                    endColumn = j;
                    break;
                }
            }
            if (((endRow - startRow) > 1 || (endColumn - startColumn) > 1) && endRow >= startRow && endColumn >= startColumn) {
                MergeStructure mergeStructure = new MergeStructure(startRow, endRow == startRow? endRow : endRow - 1, 
                        startColumn, endColumn == startColumn? endColumn : endColumn - 1);
                mergeStructureList.add(mergeStructure);
            }
        }
        bandInfo.setMergeStructure(mergeStructureList);
        bandInfo.setRowGuides(rowGuides);
    }

    private List<AdsReportCell> getBandCells(final IReportWidgetContainer cellsContainer) {
        List<AdsReportCell> cells = new ArrayList<>();
        for (AdsReportWidget w : cellsContainer.getWidgets()) {
            if (w.isReportContainer()) {
                cells.addAll(getBandCells((AdsReportWidgetContainer) w));
            } else {
                cells.add((AdsReportCell) w);
            }
        }

        return cells;
    }

    @Override
    protected void viewBand(final List<ReportGenData> genDataList, final AdsReportBand band, final Map<AdsReportCell, AdjustedCell> adjustedCellContents, ReportGenData currentGenData) throws ReportGenerationException {
        if (band.getType() == EReportBandType.PAGE_FOOTER || band.getType() == EReportBandType.PAGE_HEADER) {
            return;
        }
        Id reportId = genDataList.get(genDataList.size() - 1).reportId;
        Map<String, BandInfo> map = bandRowsInfo.get(reportId);
        if (map == null) {
            AdsReportForm form = genDataList.get(genDataList.size() - 1).form;
            calcTableStructure(reportId, form);
            map = bandRowsInfo.get(reportId);
        }
        BandInfo bundInfo = map == null? null : map.get(band.getName());
        int bandStartRow = rowIndex;
        List<Integer> autoSizeColumn = new ArrayList<>();
        if (bundInfo != null) {
            Double rowHeight;
            CellsAdjuster.Guides rowGuides = bundInfo.getRowGuides();
            CellsAdjuster.Guides columnGuides = reportsColumnGuides.get(reportId);
            if (columnGuides == null) {
                return;
            }
            for (int i = 0; i < rowGuides.size() - 1; i++) {
                rowHeight = null;
                CellsAdjuster.Guide rowGuide = rowGuides.get(i);
                Row row = spreadsheet.createRow(rowIndex);
                if (report.getId().equals(reportId)) {
                    for (AdsReportWidget w : rowGuide.getNextCells()) {
                        for (int j = 0; j < columnGuides.size() - 1; j++) {
                            CellsAdjuster.Guide columnGuide = columnGuides.get(j);
                            if (columnGuide.getNextCells().contains(w)) {
                                Cell cell = row.createCell(j);
                                final AdjustedCell adjustedCell = adjustedCellContents == null ? null : adjustedCellContents.get((AdsReportCell) w);

                                viewCell(cell, w, adjustedCell, bundInfo, rowIndex - bandStartRow);
                                if (((AdsReportCell) w).isAdjustWidth() && !autoSizeColumn.contains(j)) {
                                    autoSizeColumn.add(j);
                                } else {
                                    CellsAdjuster.Guide nextColumnGuide = columnGuides.get(j + 1);
                                    double width = nextColumnGuide.getPos() - columnGuide.getPos();
                                    spreadsheet.setColumnWidth(cell.getColumnIndex(), (int) (ChartBuilder.mm2px(width) / FONT_WIDTH * MAX_WIDTH));
                                }
                                if (rowHeight == null || rowHeight > w.getHeightMm()) {
                                    rowHeight = w.getHeightMm();
                                }
                                break;
                            }
                        }
                    }
                } else {
                    for (int j = 0; j < columnGuides.size() - 1; j++) {
                        CellsAdjuster.Guide columnGuide = columnGuides.get(j);
                        Cell cell = row.createCell(j);
                        for (AdsReportWidget bw : band.getWidgets()) {
                            if (bw.getTopMm() == rowGuide.getPos() && bw.getLeftMm() == columnGuide.getPos()) {
                                final AdjustedCell adjustedCell = adjustedCellContents == null ? null : adjustedCellContents.get((AdsReportCell) bw);
                                viewCell(cell, bw, adjustedCell, bundInfo, rowIndex - bandStartRow);

                                if (((AdsReportCell) bw).isAdjustWidth() && !autoSizeColumn.contains(j)) {
                                    autoSizeColumn.add(j);
                                } else {
                                    CellsAdjuster.Guide nextColumnGuide = columnGuides.get(j + 1);
                                    double width = nextColumnGuide.getPos() - columnGuide.getPos();
                                    spreadsheet.setColumnWidth(cell.getColumnIndex(), (int) (ChartBuilder.mm2px(width) / FONT_WIDTH * MAX_WIDTH));
                                }
                                if (rowHeight == null || rowHeight > bw.getHeightMm()) {
                                    rowHeight = bw.getHeightMm();
                                }
                                break;
                            }
                        }

                    }
                }
                if (rowHeight != null) {
                    row.setHeight((short) Math.round(rowHeight / POINT_IN_MM * UNIT_IN_POINT_HEIGHT));
                }
                rowIndex++;
            }

            for (MergeStructure ms : bundInfo.getMergeStructure()) {
                Row row = spreadsheet.getRow(ms.startRow + bandStartRow);
                Cell cellStart = row.getCell(ms.startColumn);
                CellStyle cellStyle = cellStart.getCellStyle();
                for (int i = ms.startRow + bandStartRow; i <= ms.endRow + bandStartRow; i++) {
                    Row r = spreadsheet.getRow(i);
                    for (int j = ms.startColumn; j <= ms.endColum; j++) {
                        Cell cell = r.getCell(j);
                        if (cell == null) {
                            cell = r.createCell(j);
                        }
                        cell.setCellStyle(cellStyle);
                    }
                }
                spreadsheet.addMergedRegion(new CellRangeAddress(ms.startRow + bandStartRow, ms.endRow + bandStartRow, ms.startColumn, ms.endColum));

            }
        }
        for (Integer column : autoSizeColumn) {
            spreadsheet.autoSizeColumn(column, false);
        }
    }

    private void viewCell(final Cell cell, final AdsReportWidget w, final AdjustedCell adjustedCell, BandInfo bundInfo, int rowInBand) throws ReportGenerationException {
        final AdsReportCell reportCell = (AdsReportCell) w;
        if (!reportCell.isVisible()) {
            return;
        }
        if ((reportCell.getCellType() == EReportCellType.IMAGE || reportCell.getCellType() == EReportCellType.DB_IMAGE || reportCell.getCellType() == EReportCellType.CHART)) {
            int mergeRow = 0, mergeColumn = 0;
            for (MergeStructure ms : bundInfo.getMergeStructure()) {
                if (ms.startColumn == cell.getColumnIndex() && ms.startRow == rowInBand) {
                    mergeRow = ms.endRow - ms.startRow;
                    mergeColumn = ms.endColum - ms.startColumn;
                }
            }
            viewImageCell(cell, reportCell, mergeRow, mergeColumn);
        } else {
            viewTextCell(cell, reportCell, adjustedCell);
        }
    }

    private void viewImageCell(final Cell cell, final AdsReportCell reportCell, final int mergeRow, final int mergeColumn) {
        String content = reportCell.getRunTimeContent();
        if (content != null && !content.isEmpty()) {

            if (reportCell.getCellType() == EReportCellType.CHART) {
                createChart(content, cell, reportCell, mergeRow, mergeColumn);
            } else if (reportCell.getCellType() == EReportCellType.IMAGE) {
                createImage(content, cell, reportCell, mergeRow, mergeColumn);
            }
        }
    }

    private final List<FontDescription> knownFonts = new ArrayList<>(10);
    private final List<StyleDescription> knownStyles = new ArrayList<>(10);

    private class FontDescription {

        private final String name;
        private org.apache.poi.ss.usermodel.Font font;
        private final short heightInPoints;
        private final boolean isItalic;
        private final boolean isBold;
        private final boolean isLineThrough;
        private final byte underLine;
        private final org.apache.poi.ss.usermodel.Color cellFgColor;

        public FontDescription(Font font, org.apache.poi.ss.usermodel.Color cellFgColor) {
            this.name = font.getName();
            this.heightInPoints = (short) ChartBuilder.mm2px(font.getSizeMm());
            this.isItalic = font.isItalic();
            this.isBold = font.isBold();
            this.isLineThrough = font.isLineThrough();
            this.underLine = font.isUnderline() ? HSSFFont.U_SINGLE : HSSFFont.U_NONE;
            this.cellFgColor = cellFgColor;
        }

        private org.apache.poi.ss.usermodel.Font getFont() {
            if (font == null) {
                font = workbook.createFont();
                font.setFontName(name);
                font.setFontHeightInPoints(heightInPoints);
                font.setItalic(isItalic);
                font.setBold(isBold);
                font.setStrikeout(isLineThrough);
                font.setUnderline(underLine);
                font.setColor(getColorIndex(cellFgColor));

            }
            return font;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof FontDescription) {
                final FontDescription another = (FontDescription) obj;
                if (!Utils.equals(name, another.name)) {
                    return false;
                }
                if (heightInPoints != another.heightInPoints) {
                    return false;
                }
                if (isItalic != another.isItalic) {
                    return false;
                }
                if (isBold != another.isBold) {
                    return false;
                }
                if (isLineThrough != another.isLineThrough) {
                    return false;
                }
                if (underLine != another.underLine) {
                    return false;
                }
                if (!colorEquals(cellFgColor, another.cellFgColor)) {
                    return false;
                }
                return true;
            } else {
                return false;
            }
        }
    }

    private static boolean colorEquals(org.apache.poi.ss.usermodel.Color one, org.apache.poi.ss.usermodel.Color another) {
        if (one == null && another != null) {
            return false;
        }
        if (one != null && another == null) {
            return false;
        }
        if (one == null && another == null) {
            return true;
        }
        if (one instanceof HSSFColor && another instanceof HSSFColor) {
            final short[] thisTriplet = ((HSSFColor) one).getTriplet();
            final short[] anotherTriplet = ((HSSFColor) another).getTriplet();
            return Arrays.equals(thisTriplet, anotherTriplet);
        } else if (one instanceof XSSFColor && another instanceof XSSFColor) {
            final String thisColor = ((XSSFColor) one).getARGBHex();
            final String anotherColor = ((XSSFColor) another).getARGBHex();
            return thisColor.equals(anotherColor);
        } else {
            return false;
        }
    }

    private static final boolean DEBUG = false;

    private FontDescription findFont(FontDescription template) {
        for (FontDescription font : knownFonts) {
            if (font.equals(template)) {
                if (DEBUG) {
                    System.out.println("HSS font matched. Total style count: " + knownFonts.size());
                }
                return font;
            }
        }

        knownFonts.add(template);
        if (DEBUG) {
            System.out.println("HSS font added. Total fount count: " + knownFonts.size());
        }
        return template;
    }

    private StyleDescription findStyle(StyleDescription template) {
        for (StyleDescription style : knownStyles) {
            if (style.equals(template)) {
                if (DEBUG) {
                    System.out.println("HSS style matched. Total style count: " + knownFonts.size());
                }
                return style;
            }
        }
        knownStyles.add(template);
        if (DEBUG) {
            System.out.println("HSS style added. Total style count: " + knownFonts.size());
        }
        return template;
    }

    private static short getColorIndex(org.apache.poi.ss.usermodel.Color color) {
        if (color instanceof HSSFColor) {
            return ((HSSFColor) color).getIndex();
        } else if (color instanceof XSSFColor) {
            return ((XSSFColor) color).getIndexed();
        }
        return 0;
    }

    private class StyleDescription {

        private class Border {

            private boolean left, top, right, bottom;
            private org.apache.poi.ss.usermodel.Color colorLeft, colorTop, colorRight, colorBottom;
            private short thicknessLeft, thicknessTop, thicknessRight, thicknessBottom;

            public Border() {
            }

            public void setLeft(org.apache.poi.ss.usermodel.Color color, short thickness) {
                this.left = true;
                colorLeft = color;
                thicknessLeft = thickness;
            }

            public void setTop(org.apache.poi.ss.usermodel.Color color, short thickness) {
                this.top = true;
                colorTop = color;
                thicknessTop = thickness;
            }

            public void setRight(org.apache.poi.ss.usermodel.Color color, short thickness) {
                this.right = true;
                colorRight = color;
                thicknessRight = thickness;
            }

            public void setBottom(org.apache.poi.ss.usermodel.Color color, short thickness) {
                this.bottom = true;
                colorBottom = color;
                thicknessBottom = thickness;
            }

            public boolean equals(Object obj) {
                if (obj instanceof Border) {
                    final Border another = (Border) obj;
                    if (thicknessTop != another.thicknessTop) {
                        return false;
                    }
                    if (thicknessLeft != another.thicknessLeft) {
                        return false;
                    }
                    if (thicknessBottom != another.thicknessBottom) {
                        return false;
                    }
                    if (thicknessRight != another.thicknessRight) {
                        return false;
                    }
                    if (!colorEquals(colorTop, another.colorTop)) {
                        return false;
                    }
                    if (!colorEquals(colorLeft, another.colorLeft)) {
                        return false;
                    }
                    if (!colorEquals(colorBottom, another.colorBottom)) {
                        return false;
                    }
                    if (!colorEquals(colorRight, another.colorRight)) {
                        return false;
                    }
                    if (this.left != another.left) {
                        return false;
                    }
                    if (this.right != another.right) {
                        return false;
                    }
                    if (this.top != another.top) {
                        return false;
                    }
                    if (this.bottom != another.bottom) {
                        return false;
                    }

                    return true;
                } else {
                    return false;
                }
            }

        }
        private org.apache.poi.ss.usermodel.Color cellBgColor;

        private boolean wrapText;
        private Border border;
        private short verticalAlignment;
        private short alignment;
        private FontDescription font;
        private CellStyle style;

        public void setWrapText(boolean wrapText) {
            this.wrapText = wrapText;
        }

        public void setVerticalAlignment(short verticalAlignment) {
            this.verticalAlignment = verticalAlignment;
        }

        public void setAlignment(short alignment) {
            this.alignment = alignment;
        }

        public void setFont(FontDescription font) {
            this.font = font;
        }

        private CellStyle getStyle() {
            if (style == null) {
                style = workbook.createCellStyle();
                style.setWrapText(wrapText);
                if (cellBgColor != null) {
                    style.setFillForegroundColor(getColorIndex(cellBgColor));
                    style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                }
                if (font != null) {
                    style.setFont(font.getFont());
                }
                if (border != null) {
                    if (border.left) {
                        style.setLeftBorderColor(getColorIndex(border.colorLeft));
                        style.setBorderLeft(border.thicknessLeft);
                    }
                    if (border.right) {
                        style.setRightBorderColor(getColorIndex(border.colorRight));
                        style.setBorderRight(border.thicknessRight);
                    }
                    if (border.top) {
                        style.setTopBorderColor(getColorIndex(border.colorTop));
                        style.setBorderTop(border.thicknessTop);
                    }
                    if (border.bottom) {
                        style.setBottomBorderColor(getColorIndex(border.colorBottom));
                        style.setBorderBottom(border.thicknessBottom);
                    }
                }
                style.setVerticalAlignment(verticalAlignment);
                style.setAlignment(alignment);
            }
            return style;
        }

        public void setCellBgColor(org.apache.poi.ss.usermodel.Color cellBgColor) {
            this.cellBgColor = cellBgColor;
        }

        public Border setBorder() {
            return this.border = new Border();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof StyleDescription) {
                StyleDescription another = (StyleDescription) obj;
                if (this.wrapText != another.wrapText) {
                    return false;
                }
                if (!colorEquals(cellBgColor, another.cellBgColor)) {
                    return false;
                }
                if (this.font == null && another.font != null) {
                    return false;
                }
                if (this.font != null) {
                    if (another.font == null) {
                        return false;
                    }
                    if (!this.font.equals(another.font)) {
                        return false;
                    }
                }
                if (this.border == null && another.border != null) {
                    return false;
                }
                if (this.border != null) {
                    if (another.border == null) {
                        return false;
                    }
                    if (!this.border.equals(another.border)) {
                        return false;
                    }
                }
                if (this.verticalAlignment != another.verticalAlignment) {
                    return false;
                }
                if (this.alignment != another.alignment) {
                    return false;
                }
                return true;
            } else {
                return false;
            }
        }
    }
    
    private short getBorderStyle(EReportBorderStyle style) {
        switch (style) {
            case DASHED:
                return HSSFCellStyle.BORDER_DASHED;
            case DOTTED:
                return HSSFCellStyle.BORDER_DOTTED;
            case SOLID:
                return HSSFCellStyle.BORDER_THIN;
            default:
                return HSSFCellStyle.BORDER_NONE;
        }
    }

    private void viewTextCell(Cell cell, AdsReportCell w, final AdjustedCell adjustedCell) throws ReportGenerationException {
        final AdsReportCell reportCell = (AdsReportCell) w;
        String content = reportCell.getRunTimeContent();
        StyleDescription cellStyle = new StyleDescription();
        cellStyle.setWrapText(((AdsReportCell) w).isWrapWord());
        org.apache.poi.ss.usermodel.Color cellBgColor = null;
        Color bg = w.getBgColor();
        if (bg != Color.WHITE) {
            cellBgColor = calcColor(bg);
        }
        org.apache.poi.ss.usermodel.Color cellFgColor = calcColor(w.getFgColor());
        if (cellBgColor != null) {
            cellStyle.setCellBgColor(cellBgColor);
        }

        FontDescription hSSFFont = createFont(w.getFont(), cellFgColor);
        cellStyle.setFont(hSSFFont);

        if (w.getBorder() != null && w.getBorder().isDisplayed()) {
            //ChartBuilder.mm2px( w.getBorder().getThicknessMm());


            StyleDescription.Border border = cellStyle.setBorder();
            if (w.getBorder().isOnLeft()) {
                border.setLeft(calcColor(w.getBorder().getLeftColor()), getBorderStyle(w.getBorder().getLeftStyle()));
            }
            if (w.getBorder().isOnRight()) {
                border.setRight(calcColor(w.getBorder().getRightColor()), getBorderStyle(w.getBorder().getRightStyle()));
            }
            if (w.getBorder().isOnTop()) {
                border.setTop(calcColor(w.getBorder().getTopColor()), getBorderStyle(w.getBorder().getTopStyle()));
            }
            if (w.getBorder().isOnBottom()) {
                border.setBottom(calcColor(w.getBorder().getBottomColor()), getBorderStyle(w.getBorder().getBottomStyle()));
            }
        }

        switch (((AdsReportCell) w).getVAlign()) {
            case MIDDLE:
                cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                break;
            case BOTTOM:
                cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_BOTTOM);
                break;
            default:
                cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        }

        switch (reportCell.getHAlign()) {
            case LEFT:
                cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                break;
            case CENTER:
                cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                break;
            case RIGHT:
                cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                break;
            case JUSTIFY:
                cellStyle.setAlignment(HSSFCellStyle.ALIGN_JUSTIFY);
                break;
        }
        cell.setCellStyle(findStyle(cellStyle).getStyle());

        if (content != null && content.length() >= MAX_CELL_LENGTH) {
            content = new String(content.substring(0, MAX_CELL_LENGTH - 1));
        }
        if (adjustedCell == null) {
            cell.setCellValue(content);
        } else {
            HSSFRichTextString richString = new HSSFRichTextString(content);
            int index = 0;
            for (int j = 0; j < adjustedCell.getParagraphsCount(); j++) {
                CellParagraph cellParagraph = adjustedCell.getParagraph(j);
                for (int i = 0; i < cellParagraph.getLinesCount(); i++) {
                    List<CellContents> cellContents = cellParagraph.getContentsByLine(i);
                    for (CellContents cellContent : cellContents) {
                        String text = cellContent.getText();
                        if ((index + text.length()) >= MAX_CELL_LENGTH) {
                            text = new String(text.substring(0, MAX_CELL_LENGTH - index - 1));
                        }
                        org.apache.poi.ss.usermodel.Color color = cellFgColor;
                        if (cellContent.getFgColor() != null) {
                            color = calcColor(cellContent.getFgColor());
                        }
                        richString.applyFont(index, index + text.length(), createFont(cellContent.getFont(), color).getFont());
                        index += text.length();
                        if (index >= MAX_CELL_LENGTH) {
                            break;
                        }
                    }
                    if (index >= MAX_CELL_LENGTH) {
                        break;
                    }
                }
                if (index >= MAX_CELL_LENGTH) {
                    break;
                }
            }
            cell.setCellValue(richString);
        }
    }

    private FontDescription createFont(final Font font, final org.apache.poi.ss.usermodel.Color cellFgColor) {
        FontDescription desc = new FontDescription(font, cellFgColor);
        return findFont(desc);
    }
    
    private org.apache.poi.ss.usermodel.Color calcColor(final Color color) throws ReportGenerationException {
        HSSFPalette palette = ((HSSFWorkbook) workbook).getCustomPalette();
        HSSFColor result = palette.findColor((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());
        if (result == null) {
            while (usedColor != null && usedColor[lastUsedColorIndex] == 1) {
                lastUsedColorIndex++;
                if (lastUsedColorIndex >= PaletteRecord.FIRST_COLOR_INDEX + PaletteRecord.STANDARD_PALETTE_SIZE) {
                    usedColor = null;
                    break;
                }
            }
            if (usedColor == null || lastUsedColorIndex >= PaletteRecord.FIRST_COLOR_INDEX + PaletteRecord.STANDARD_PALETTE_SIZE) {
                throw new ReportGenerationException("The number of colors exceeds the XLS limit");
            }
            palette.setColorAtIndex(lastUsedColorIndex, (byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());
            usedColor[lastUsedColorIndex]  = 1;
            result = palette.getColor(lastUsedColorIndex);
        } else {
            if (usedColor != null) {
                usedColor[result.getIndex()]  = 1;
            }
        }
        return result;
    }

    private void createImage(final String url, final Cell cell, final AdsReportWidget w, final int mergeRow, final int mergeColumn) {
        try {
            String data;
            if (url.startsWith("url('")) {
                int end = url.lastIndexOf("'");
                if (end > 0) {
                    data = url.substring(5, end);
                } else {
                    data = url.substring(5);
                }
            } else {
                data = url;
                if (data.startsWith("\"") && data.endsWith("\"")) {//if not url - mean this is a file;
                    data = data.substring(1, data.length() - 1);
                }
                data = "file:" + data;
            }
            byte[] imageContent = null;
            if (data.startsWith("data:")) {
                int mimeDescEnd = data.indexOf(";");
                if (mimeDescEnd < 5) {
                    return;
                }
                String mimeType = data.substring(5, mimeDescEnd);
                int encodingEnd = data.indexOf(',', mimeDescEnd + 1);
                if (encodingEnd <= mimeDescEnd + 1) {
                    return;
                }
                String encoding = data.substring(mimeDescEnd + 1, encodingEnd);
                if (!"base64".equals(encoding)) {
                    return;
                }
                imageContent = Base64.decode(data.substring(encodingEnd + 1));
                if (imageContent != null && "image/svg".equals(mimeType)) {
                    imageContent = svgToPNG(new ByteArrayInputStream(imageContent), w);
                }
            } else {
                URL urlObj = new URL(data);
                InputStream in = urlObj.openStream();
                try {
                    if (data.endsWith("svg")) {
                        imageContent = svgToPNG(in, w);
                    } else {
                        imageContent = FileUtils.readBinaryStream(in);
                    }
                } finally {
                    try {
                        in.close();
                    } catch (IOException ex) {

                    }
                }
            }
            if (imageContent != null) {
                image(imageContent, cell, mergeRow, mergeColumn);
            }

        } catch (URISyntaxException ex) {
            Logger.getLogger(PoiReportGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(PoiReportGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PoiReportGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TranscoderException ex) {
            Logger.getLogger(PoiReportGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void createChart(final String path, final Cell cell, final AdsReportWidget w, final int mergeRow, final int mergeColumn) {
//        final byte[] content = svgToJPEG(path, w);
//        image(content, cell, mergeRow, mergeColumn);
        createImage(path, cell, w, mergeRow, mergeColumn);
    }

    private void image(final byte[] pictureData, final Cell cell, final int mergeRow, final int mergeColumn) {
        int row = cell.getRowIndex();
        int column = cell.getColumnIndex();
        int my_picture_id = workbook.addPicture(pictureData, Workbook.PICTURE_TYPE_PNG);
        Drawing drawing = spreadsheet.createDrawingPatriarch();
        ClientAnchor my_anchor = new HSSFClientAnchor();

        my_anchor.setCol1(column);
        my_anchor.setRow1(row);
        if (mergeColumn > 0) {
            my_anchor.setCol2(column + mergeColumn + 1);
        }
        if (mergeRow > 0) {
            my_anchor.setRow2(row + mergeRow + 1);
        }
        Picture my_picture = drawing.createPicture(my_anchor, my_picture_id);
        my_picture.resize(1, 1);
    }

    private byte[] svgToPNG(final InputStream inputStream, final AdsReportWidget w) throws TranscoderException, IOException, URISyntaxException {
        ImageTranscoder t = new PNGTranscoder();
        final float height = (float) w.getHeightMm() / (float) POINT_IN_MM * UNIT_IN_POINT_HEIGHT;
        final float width = (float) ChartBuilder.mm2px((float) w.getWidthMm()) / (float) FONT_WIDTH * MAX_WIDTH;
        t.addTranscodingHint(ImageTranscoder.KEY_MAX_HEIGHT, new Float(height));
        t.addTranscodingHint(ImageTranscoder.KEY_MAX_WIDTH, new Float(width));
        TranscoderInput input = new TranscoderInput(inputStream);

        final ByteArrayOutputStream ostream = new ByteArrayOutputStream();
        final TranscoderOutput output = new TranscoderOutput(ostream);
        t.transcode(input, output);
        ostream.flush();
        return ostream.toByteArray();
    }

    @Override
    public void generateReport(final IReportFileController controller) throws ReportGenerationException {
        final IReportFileController writerManager = new DefaultReportFileController(controller) {

            @Override
            public void afterCreateFile(Report report, File file, OutputStream output) throws ReportGenerationException {
                super.afterCreateFile(report, file, output);
                knownStyles.clear();
                knownFonts.clear();
                rowIndex = 0;
                workbook = new HSSFWorkbook();
                spreadsheet = workbook.createSheet("new sheet");
            }

            @Override
            public void beforeCloseFile(Report report, File file, OutputStream output) throws ReportGenerationException {
                try {
                    workbook.write(output);
                } catch (IOException ex) {
                    throw new ReportGenerationException(ex);
                }
                workbook = null;
                spreadsheet = null;
                super.beforeCloseFile(report, file, output);
            }

        };

        super.generateReport(writerManager);
    }
}
