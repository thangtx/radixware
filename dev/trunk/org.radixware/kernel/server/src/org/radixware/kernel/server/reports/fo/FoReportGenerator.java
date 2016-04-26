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
package org.radixware.kernel.server.reports.fo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportAbstractAppearance.Border;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportAbstractAppearance.Font;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportDbImageCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidget;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.IReportWidgetContainer;
import org.radixware.kernel.common.enums.EImageScaleType;
import org.radixware.kernel.common.enums.EReportBandType;
import org.radixware.kernel.common.enums.EReportBorderStyle;
import org.radixware.kernel.common.enums.EReportCellHAlign;
import org.radixware.kernel.common.enums.EReportCellType;
import org.radixware.kernel.common.enums.EReportCellVAlign;
import org.radixware.kernel.common.enums.EReportExportFormat;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.XmlColor;
import org.radixware.kernel.server.reports.AbstractReportGenerator;
import org.radixware.kernel.server.reports.ReportGenerationException;
import org.radixware.kernel.server.reports.ReportStateInfo;
import org.radixware.kernel.server.types.Report;

public class FoReportGenerator extends AbstractReportGenerator {

    private static final String PAGE_MASTER_NAME = "page";
    private FoRoot root = null;
    private FoPageSequence page = null;
    private FoFlow flow = null;
    private boolean someBandOnPageDisplayed = false;
    private double curPageHeight = 0;
    private double drawnBandHeight = 0;//высота отрисованной части полосы
    private double drawnBandHeightOnCurPage = 0;

    enum CellWrappingState {

        NO_WRAPPED, FIRST_PART_WRAPPED_CELL, MIDDLE_PART_WRAPPED_CELL, LAST_PART_WRAPPED_CELL
    };

    public FoReportGenerator(final Report report, EReportExportFormat format, ReportStateInfo stateInfo) {
        super(report, format, stateInfo);
    }

    public FoReportGenerator(final Report report, EReportExportFormat format) {
        this(report, format, null);
    }

    private static FoBlockContainer.BorderStyle borderStyleToFoBorderStyle(EReportBorderStyle style) {
        switch (style) {
            case DASHED:
                return FoBlockContainer.BorderStyle.DASHED;
            case DOTTED:
                return FoBlockContainer.BorderStyle.DOTTED;
            case SOLID:
                return FoBlockContainer.BorderStyle.SOLID;
            default:
                return FoBlockContainer.BorderStyle.NONE;
        }
    }

    @Override
    protected boolean isFormattingSupported() {
        return true;
    }

    private static String color2FoColor(Color color) {
        return XmlColor.mergeColor(color); // same format
    }

    private static void setBlockContainerParams(final FoBlockContainer blockContainer, double left, double top, double width, double height, final String bgColor, final String fgColor, final Border border, final Font font, CellWrappingState wrappingState) throws XMLStreamException {
        blockContainer.setAbsolutePositionFixed();

        if (fgColor != null) {
            blockContainer.setColor(fgColor);
        }

        if (bgColor != null) {
            blockContainer.setBackgroundColor(bgColor);
        }

        if (font != null) {
            blockContainer.setFontFamily(font.getName());
            blockContainer.setFontSize(font.getSizeMm());
            if (font.isItalic()) {
                blockContainer.setFontStyleItalic();
            } else {
                blockContainer.setFontStyleNormal();
            }
            if (font.isBold()) {
                blockContainer.setFontWeightBold();
            } else {
                blockContainer.setFontWeightNormal();
            }
        }

        if (border != null && border.isDisplayed()) {
            final FoBlockContainer.BorderStyle foBorderStyle = borderStyleToFoBorderStyle(border.getStyle());
            final double borderThicknessMm = border.getThicknessMm();
            final String borderColor = color2FoColor(border.getColor());

            if (border.isOnLeft() && border.isOnRight() && border.isOnTop() && border.isOnBottom() && wrappingState == CellWrappingState.NO_WRAPPED) {
                blockContainer.setBorder(foBorderStyle, borderColor, border.getThicknessMm());
                top += borderThicknessMm;
                left += borderThicknessMm;
                width -= 2 * borderThicknessMm;     // deprecated: FOP draw top border inside the cell, but other - outside cell,
                height -= 2 * borderThicknessMm;    // and if top border presented, the bottom border moved twice.
            } else {
                if (border.isOnLeft()) {
                    blockContainer.setBorderLeft(foBorderStyle, borderColor, borderThicknessMm);
                    left += borderThicknessMm;
                    width -= borderThicknessMm;
                }
                if (border.isOnRight()) {
                    blockContainer.setBorderRight(foBorderStyle, borderColor, borderThicknessMm);
                    width -= borderThicknessMm;
                }
                if (border.isOnTop() && (wrappingState == CellWrappingState.NO_WRAPPED || wrappingState == CellWrappingState.FIRST_PART_WRAPPED_CELL)) {
                    blockContainer.setBorderTop(foBorderStyle, borderColor, borderThicknessMm);
                    top += borderThicknessMm;
                    height -= borderThicknessMm;
                }
                if (border.isOnBottom() && (wrappingState == CellWrappingState.NO_WRAPPED || wrappingState == CellWrappingState.LAST_PART_WRAPPED_CELL)) {
                    blockContainer.setBorderBottom(foBorderStyle, borderColor, borderThicknessMm);
                    height -= borderThicknessMm;
                }
            }
        }

        blockContainer.setLeft(left);
        blockContainer.setTop(top);
        blockContainer.setWidth(width);
        blockContainer.setHeight(height);
    }

//    private boolean viewCellBorder(final AdsReportCell cell, final FoBlockContainer bandBlockContainer) throws XMLStreamException {
//        if (!cell.isVisible() || !cell.getBorder().isDisplayed()) {
//            return false;
//        }
//
//        final FoBlockContainer cellBlockContainer = bandBlockContainer.addNewBlockContainer();
//        cellBlockContainer.begin();
//
//        final AdsReportForm rootForm = getRootForm();
//
//        setBlockContainerParams(cellBlockContainer,
//                cell.getLeftMm() + rootForm.getMargin().getLeftMm(),
//                cell.getTopMm() + getCurHeight(),
//                cell.getWidthMm(),
//                cell.getHeightMm(),
//                null /*bcolor*/,
//                null /*fcolor*/,
//                cell.getBorder(),
//                null /*font*/);
//
//        cellBlockContainer.addEmptyBlock(); // for xml validation
//
//        cellBlockContainer.end();
//        return true;
//    }  
    private boolean viewCell(final AdsReportCell cell, AdjustedCell adjustedCell, final AdsReportBand band, FoBlockContainer bandBlockContainer/*, boolean wrapping, boolean isNewPageRequired */) throws XMLStreamException, ReportGenerationException {
        if (!cell.isVisible()) {
            return false;
        }
        double cellTopMm = cell.getTopMm();

        FoBlockContainer cellBlockContainer;
        final String content = cell.getRunTimeContent();
        double marginMm = calcCellMargin(cell, adjustedCell.wasSeparated());
        if ((cell.getCellType() == EReportCellType.IMAGE || cell.getCellType() == EReportCellType.DB_IMAGE || cell.getCellType() == EReportCellType.CHART)
                && content != null && !content.isEmpty()) {

            cellBlockContainer = createCellContainer(cell, bandBlockContainer, cell.getHeightMm(), cellTopMm + getCurHeightMm(), marginMm, CellWrappingState.NO_WRAPPED);
            lastBandPartHeight = cellTopMm + cell.getHeightMm();

            FoBlock block = cellBlockContainer.addNewBlock();
            block.begin();
            FoExternalGraphic externalGraphic = block.addNewExternalGraphic();
            externalGraphic.begin();
            externalGraphic.setSrc(content);

            double vBorder = 0;
            double hBorder = 0;
            if (cell.getBorder() != null) {
                double t = cell.getBorder().getThicknessMm();
                if (cell.getBorder().getOnLeft()) {
                    hBorder += t;
                }
                if (cell.getBorder().getOnRight()) {
                    hBorder += t;
                }
                if (cell.getBorder().getOnTop()) {
                    vBorder += t;
                }
                if (cell.getBorder().getOnBottom()) {
                    vBorder += t;
                }
            }
            double imageWidth = cell.getWidthMm() - cell.getMarginLeftMm() - cell.getMarginRightMm() - hBorder;
            double imageHeight = cell.getHeightMm() - cell.getMarginTopMm() - cell.getMarginBottomMm() - vBorder;
            externalGraphic.setWidthMm(imageWidth);
            externalGraphic.setHeightMm(imageHeight);
            if (cell.getCellType() == EReportCellType.DB_IMAGE) {

                externalGraphic.setContentWidth(String.valueOf(imageWidth) + "mm");
                externalGraphic.setContentHeight(String.valueOf(imageHeight) + "mm");
                if (((AdsReportDbImageCell) cell).getScaleType() == EImageScaleType.FIT_TO_CONTAINER || ((AdsReportDbImageCell) cell).getScaleType() == EImageScaleType.CROP || ((AdsReportDbImageCell) cell).getScaleType() == EImageScaleType.RESIZE_CONTAINER) {
                    externalGraphic.setVerticalAlign(cell.getVAlign());
                    externalGraphic.setHorizontalAlign(cell.getHAlign());
                    externalGraphic.writeAttribute("padding", "0mm");
                    externalGraphic.writeAttribute("margin", "0mm");
                } else {
                    externalGraphic.setVerticalAlign(EReportCellVAlign.MIDDLE);
                    externalGraphic.setHorizontalAlign(EReportCellHAlign.CENTER);
                }

            } else {
                externalGraphic.setContentWidth(FoExternalGraphic.SCALE_TO_FIT);
                externalGraphic.setContentHeight(FoExternalGraphic.SCALE_TO_FIT);
            }
            externalGraphic.end();
            block.end();
        } else {
            boolean isJustify = cell.getHAlign() == EReportCellHAlign.JUSTIFY;
            cellBlockContainer = createCellContainer(cell, bandBlockContainer, cell.getHeightMm(), cellTopMm + getCurHeightMm(), marginMm, CellWrappingState.NO_WRAPPED/*wrapping && cell.getTopMm()==0 ? CellWrappingState.LAST_PART_WRAPPED_CELL:CellWrappingState.NO_WRAPPED*/);
            for (int i = 0; i < adjustedCell.getLineCount(); i++) {
                addLine(cellBlockContainer, adjustedCell, i, isJustify, cell.getLineSpacingMm());
            }
            if (isDataBand(band)) {
                lastBandPartHeight = cellTopMm + cell.getHeightMm();
            }
            if (cellBlockContainer != null && adjustedCell.getLineCount() == 0) {
                cellBlockContainer.addEmptyBlock(); // for xml validation
            }
        }
        if (cellBlockContainer != null) {
            setVerticalMargin(cellBlockContainer, cell.getMarginBottomMm()/*, cell.getLineSpacingMm()*/);
            cellBlockContainer.end();
        }
        return true;
    }

    private void addLine(FoBlockContainer cellBlockContainer, AdjustedCell adjustedCell, int line, boolean isJustify, double lineSpasing) throws XMLStreamException {
        FoBlockContainer lineBlockContainer = cellBlockContainer.addNewBlockContainer();
        lineBlockContainer.begin();
        lineBlockContainer.setHeight(adjustedCell.getLineFontSize(line) + lineSpasing);
     //   lineBlockContainer.setArabic(true);
        FoBlock lineBlock = addBlockToBlockContainer(lineBlockContainer, isJustify && line == adjustedCell.getLineCount() - 1);
        if (isJustify) {
            lineBlock.setTextJustify();
        }
        List<CellContents> contents = adjustedCell.getContentsByLine(line);
        for (int j = 0; j < contents.size(); j++) {
            CellContents c = contents.get(j);
            addInLineToBlock(c, lineBlock);
        }
        lineBlock.end();
        lineBlockContainer.end();
    }

    private boolean isDataBand(final AdsReportBand band) {
        return band != null && band.getType() != EReportBandType.PAGE_HEADER && band.getType() != EReportBandType.PAGE_FOOTER;
    }

    private double calcCellMargin(final AdsReportCell cell, boolean wrapping) {
        if (wrapping) {
            return 0;
        } else {
            return cell.getMarginTopMm();
        }
    }

    private double[] calcLinesOnCurPage(final AdsReportCell cell, final AdjustedCell adjustedCell, final double textTopMm, final double wrappedLine) {
        return isImage(cell) ? new double[]{0, cell.getHeightMm()} : calcLinesOnCurPage(adjustedCell, textTopMm, cell.getLineSpacingMm(), wrappedLine);
    }

    private double[] calcLinesOnCurPage(final AdjustedCell adjustedCell, final double textTopMm, double lineSpasing, final double wrappedLine) {
        int i, end = adjustedCell.getLineCount();
        double lineHeigh;
        double heigh = 0;
        for (i = 0; i < end; i++) {
            lineHeigh = adjustedCell.getLineFontSize(i) + lineSpasing;
            heigh += lineHeigh;
            if ((heigh + textTopMm) > wrappedLine) {
                heigh = heigh - lineHeigh;//-lineSpasing;
                break;
            }
        }
        return new double[]{i, heigh};
    }

    private double calcContentHeight(final AdjustedCell adjustedCell, double lineSpasing) {
        int end = adjustedCell.getLineCount();
        double heigh = 0, lineHeigh;
        for (int i = 0; i < end; i++) {
            lineHeigh = adjustedCell.getLineFontSize(i) + lineSpasing;
            heigh += lineHeigh;
        }
        return heigh - lineSpasing;
    }

    private FoBlock addBlockToBlockContainer(FoBlockContainer cellBlockContainer, boolean isLastCellJustify) throws XMLStreamException {
        final FoBlock block = cellBlockContainer.addNewBlock();
        block.begin();
        block.setWrapOptionNoWrap();
        if (isLastCellJustify) {
            block.setNoTextJustify();
        }
        block.setWhiteSpaceTreatment();
        block.setNoWhiteSpaceCollapse();
        return block;
    }

    private boolean isArabic(String text) {
        String textWithoutSpace = text.trim().replaceAll(" ", ""); //to ignore whitepace

        for (int i = 0; i < textWithoutSpace.length();) {
            int c = textWithoutSpace.codePointAt(i);
            if (c >= 0x0600 && c <= 0x06E0) {
                return true;
            }
            i += Character.charCount(c);
        }
        return false;
    }

    private void addInLineToBlock(CellContents c, FoBlock block) throws XMLStreamException {
        FoInline inlineForTextDecoration = null;
        final FoInline inline = block.addNewInline();
        inline.begin();
        if (c.getBgColor() != null) {
            String bgColor = color2FoColor(c.getBgColor());
            inline.setBackgroundColor(bgColor);
        }
        if (c.getFgColor() != null) {
            String fgColor = color2FoColor(c.getFgColor());
            inline.setColor(fgColor);
        }
        if (c.getFont() != null) {
            inline.setFontFamily(c.getFont().getName());
            inline.setFontSize(c.getFont().getSizeMm());
            if (c.getFont().getItalic()) {
                inline.setFontStyleItalic();
            }
            if (c.getFont().getBold()) {
                inline.setFontWeightBold();
            }
            if (c.getFont().isSetVerticalAlign()) {
                inline.setVerticalAlign(c.getFont().getVerticalAlign());
            }
            if (c.getFont().getUnderline() || c.getFont().getLineThrough()) {
                if (c.getFont().getUnderline() && c.getFont().getLineThrough()) {
                    inline.setUnderlineText();
                    inlineForTextDecoration = block.addNewInline();
                    inlineForTextDecoration.begin();
                    inlineForTextDecoration.setLineThroughText();
                } else if (c.getFont().getUnderline()) {
                    inline.setUnderlineText();
                } else if (c.getFont().getLineThrough()) {
                    inline.setLineThroughText();
                }
            }
        }
        if (c.getText() != null) {
            String text = c.getText();            
            if (inlineForTextDecoration != null) {
                inlineForTextDecoration.setText(text);
                inlineForTextDecoration.end();
            } else {
                inline.setText(text);
            }
        } else if (inlineForTextDecoration != null) {
            inlineForTextDecoration.end();
        }
        inline.end();
    }

    private boolean isCellGoIntoPage(final AdsReportCell cell, final double cellTopMm) {
        return (getCurHeightMm() + cell.getHeightMm() + cellTopMm <= getPageHeight());
    }

    private boolean isBandGoIntoPage(AdsReportBand band) {
        return (getCurHeightMm() + band.getHeightMm() <= getPageHeight()) || band.getType() == EReportBandType.PAGE_FOOTER;//! (band.isMultiPage()&&(getCurHeight() + band.getHeightMm() > getPageHeight())&&(!isFirstDataBandOnPage())) ;
    }

    private double getPageHeight() {
        if (curPageHeight == 0) {
            double footerHeight = 0;
            for (AdsReportBand footer : getRootForm().getPageFooterBands()) {
                footerHeight += (footer != null && footer.isVisible() ? footer.getHeightMm() : 0);
            }

            curPageHeight = getRootForm().getPageHeightMm() - getRootForm().getMargin().getBottomMm() - footerHeight;
        }
        return curPageHeight;
    }

    @Override
    protected void viewBand(final List<ReportGenData> genDataList, final AdsReportBand band, Map<AdsReportCell, AdjustedCell> adjustedCellContents) throws ReportGenerationException {
        try {
            //drawnBandHeight=0;
            double height = isBandGoIntoPage(band) ? band.getHeightMm() : (getPageHeight() - getCurHeightMm());
            List<AdsReportCell> widgetList = getCells(band);
            FoBlockContainer bandBlockContainer = createBandContainer(band, height);
            viewCells(band, bandBlockContainer, widgetList, adjustedCellContents, genDataList);
            if (drawnBandHeight > 0) {
                band.setHeightMm(band.getHeightMm() - drawnBandHeight);
            }
            drawnBandHeight = 0;
            bandBlockContainer.end();
        } catch (XMLStreamException ex) {
            throw new ReportGenerationException(ex);
        }

    }

    private AdsReportCell findUpperCell(List<AdsReportCell> widgetList) {
        if (widgetList.isEmpty()) {
            return null;
        }
        AdsReportCell upperCell = widgetList.get(0);
        for (int j = 0; j < widgetList.size(); j++) {
            if (upperCell.getTopMm() > widgetList.get(j).getTopMm()) {
                upperCell = widgetList.get(j);
            }
        }
        return upperCell;
    }

    private List<AdsReportCell> getSnappedCells(List<AdsReportCell> widgetList, double d) {
        List<AdsReportCell> snappedCells = new ArrayList<>();
        for (int i = 0; i < widgetList.size(); i++) {
            AdsReportCell c = widgetList.get(i);
            if (c.isSnapBottomEdge() && (c.getTopMm() + c.getHeightMm()) == d) {
                snappedCells.add(c);
            }
        }
        return snappedCells;
    }

    private void viewCells(final AdsReportBand band, FoBlockContainer bandBlockContainer, List<AdsReportCell> widgetList, Map<AdsReportCell, AdjustedCell> adjustedCellContents, final List<ReportGenData> genDataList) throws XMLStreamException, ReportGenerationException {
        boolean someCellDisplayed = false;
        drawnBandHeightOnCurPage = Double.MAX_VALUE;
        double d = drawnBandHeight;
        List<AdsReportCell> cellsForNextPage = new LinkedList<>();
        List<AdsReportCell> wrappedCells = new LinkedList<>();
        while (!widgetList.isEmpty()) {
            boolean isFirstOnPage = false;
            AdsReportCell upperCell = findUpperCell(widgetList);
            double cellTopMm = upperCell.getTopMm() <= 0 ? upperCell.getTopMm() : upperCell.getTopMm() - d;
            boolean isNewPageRequired = band.isMultiPage()
                    && !isCellGoIntoPage(upperCell, cellTopMm);

            List<AdsReportCell> snappedCells = new ArrayList<>();
            if (upperCell.isSnapBottomEdge() && isNewPageRequired) {
                snappedCells = getSnappedCells(widgetList, upperCell.getTopMm() + upperCell.getHeightMm());
                widgetList.removeAll(snappedCells);
            } else {
                snappedCells.add(upperCell);
                widgetList.remove(upperCell);
            }

            if (!isNewPageRequired) {
                upperCell.setTopMm(cellTopMm);
                AdjustedCell adjustedCell = adjustedCellContents.get(upperCell);
                if (viewCell(upperCell, adjustedCell, band, bandBlockContainer/*,falseadjustedCell.wasWrapped(),isNewPageRequired*/)) {
                    someCellDisplayed = true;
                }
            } else {
                //boolean isContainImage=isContainImage(snappedCells);
                if (!band.isCellWrappingEnabled()/* || isContainImage*/) {
                    for (AdsReportCell c : snappedCells) {
                        if (isFirstDataBandOnPage() && ((c.getTopMm() - d) <= 0)) {
                            isFirstOnPage = true;
                            break;
                        }
                        if (getPageHeight() > ((c.getTopMm() - d) + getCurHeightMm())) {
                            if ((drawnBandHeightOnCurPage > c.getTopMm() - d) /*|| (drawnBandHeightOnCurPage==0 && !isTopCalc)*/) {
                                drawnBandHeightOnCurPage = c.getTopMm() - d;
                            }
                        }
                    }
                    if (!isFirstOnPage) {
                        for (AdsReportCell c : snappedCells) {
                            AdjustedCell adjustedCell = adjustedCellContents.get(c);
                            if (getPageHeight() > ((c.getTopMm() - d) + getCurHeightMm())) {
                                if (adjustedCell.wasWrapped()) {
                                    wrappedCells.add(c);
                                } else {
                                    cellsForNextPage.add(c);
                                    adjustedCell.setWasWrapped(true);
                                }
                            } else {
                                cellsForNextPage.add(c);
                            }
                        }
                    } else {
                        wrappedCells.addAll(snappedCells);
                    }
                } else {
                    for (AdsReportCell c : snappedCells) {
                        double topMm = c.getTopMm() == 0 ? 0 : c.getTopMm() - d;
                        if (getPageHeight() > (topMm + getCurHeightMm())) {
                            wrappedCells.add(c);
                        } else {
                            cellsForNextPage.add(c);
                        }
                    }
                }
            }
        }
        if (!wrappedCells.isEmpty()) {
            wrapCells(bandBlockContainer, adjustedCellContents, wrappedCells, cellsForNextPage, d);
            someCellDisplayed = true;
        }
        if (!someCellDisplayed) {
            bandBlockContainer.addEmptyBlock(); // for xml validation
        }
        if (!cellsForNextPage.isEmpty()) {
            drawnBandHeightOnCurPage = drawnBandHeightOnCurPage == Double.MAX_VALUE ? 0 : drawnBandHeightOnCurPage;
            drawnBandHeight += drawnBandHeightOnCurPage;
            if (bandBlockContainer != null) {
                bandBlockContainer.end();
            }
            viewOnNewPage(band, genDataList, adjustedCellContents, cellsForNextPage);
        }
        someBandOnPageDisplayed = true;
    }

    private void wrapCells(FoBlockContainer bandBlockContainer, final Map<AdsReportCell, AdjustedCell> adjustedCellContents, final List<AdsReportCell> wrappedCells, final List<AdsReportCell> cellsForNextPage, double d) throws XMLStreamException, ReportGenerationException {
        List<AdsReportCell> cellsWrappenonNextPage = new LinkedList<>();
        cellsWrappenonNextPage.addAll(viewWrappedCells(bandBlockContainer, adjustedCellContents, wrappedCells, d));
        if (delta != 0) {
            for (AdsReportCell c : cellsForNextPage) {
                c.setTopMm(c.getTopMm() + delta);
            }
        }
        cellsForNextPage.addAll(0, cellsWrappenonNextPage);
    }

    private void viewOnNewPage(final AdsReportBand band, final List<ReportGenData> genDataList, Map<AdsReportCell, AdjustedCell> adjustedCellContents, List<AdsReportCell> cellsForNextPage) throws ReportGenerationException, XMLStreamException {
        double d = drawnBandHeight;
        finishPage(genDataList);
        newPage(genDataList);

        drawnBandHeight = d;
        double bandHeight = band.getHeightMm() - drawnBandHeight;
        bandHeight = (getCurHeightMm() + bandHeight > getPageHeight())
                ? (getPageHeight() - getCurHeightMm()) : bandHeight;
        FoBlockContainer bandBlockContainer = createBandContainer(band, bandHeight);
        viewCells(band, bandBlockContainer, cellsForNextPage, adjustedCellContents, genDataList);
    }

    private double delta = 0.0;

    private List<AdsReportCell> viewWrappedCells(FoBlockContainer bandBlockContainer, final Map<AdsReportCell, AdjustedCell> adjustedCellContents, final List<AdsReportCell> wrappedCells, double d) throws XMLStreamException, ReportGenerationException {
        List<AdsReportCell> cellsForNextPage = new ArrayList<>();
        double[] wrapLines = calcWrapLines(adjustedCellContents, wrappedCells, d);
        double wrappedLine = wrapLines[1];
        double minWrappedLine = wrapLines[0];
        delta = wrappedLine - minWrappedLine;

        boolean isHeightCalc = false;
        for (AdsReportCell cell : wrappedCells) {
            double cellTopMm = cell.getTopMm() <= 0 ? cell.getTopMm() : cell.getTopMm() - d;
            if (getPageHeight() > (cellTopMm + getCurHeightMm())) {
                AdjustedCell adjustedCell = adjustedCellContents.get(cell);
                int lineCount = isImage(cell) ? 0 : adjustedCell.getLineCount();
                double marginMm = calcCellMargin(cell, adjustedCell.wasSeparated());
                double textTopMm = marginMm/*cell.getMarginTopMm()*/ + cellTopMm;

                double[] linesInfo = calcLinesOnCurPage(cell, adjustedCell, textTopMm, wrappedLine); //???-?? ????? ?? ??????? ??????, ??????? ?????????? ?? ??????? ????????                  
                int lineCntOnCurPage = (int) linesInfo[0];
                double cellDisplayHeightMm = wrappedLine - cellTopMm;
                double cellActualHeightMm = lineCntOnCurPage > 0 && lineCntOnCurPage < lineCount ? linesInfo[1] + marginMm/*calcCellMargin(cell, adjustedCell.wasWrapped())*/ : cellDisplayHeightMm;
                double addHeight = (lineCntOnCurPage > 0 && lineCntOnCurPage < lineCount)
                        || (lineCntOnCurPage == lineCount && (cellActualHeightMm + cellTopMm) == wrappedLine) ? (cellActualHeightMm + cellTopMm) - minWrappedLine : 0;

                FoBlockContainer cellBlockContainer = null;
                /*double q=0.0;
                 if(hasBottomBorder(cell) && (cell.getHeightMm()-cellHeightMm)>0){//for bottom border
                 q=1.0;
                 } */
                if (lineCntOnCurPage > 0) {  //view cell                      
                    cellBlockContainer = createCellContainer(cell, bandBlockContainer, cellDisplayHeightMm/*+q*/, cellTopMm + getCurHeightMm(), marginMm, CellWrappingState.NO_WRAPPED /* adjustedCell.wasWrapped() &&  cell.getTopMm()==0 ? CellWrappingState.MIDDLE_PART_WRAPPED_CELL : CellWrappingState.FIRST_PART_WRAPPED_CELL*/);
                    boolean isJustify = cell.getHAlign() == EReportCellHAlign.JUSTIFY;
                    for (int i = 0; i < lineCntOnCurPage; i++) {
                        addLine(cellBlockContainer, adjustedCell, i, isJustify, cell.getLineSpacingMm());
                    }
                } else {
                    if (cellDisplayHeightMm == 0) {//lineCount>0 && lineCntOnCurPage==0 && cellDisplayHeightMm<(adjustedCell.getLineFontSize(0)+cell.getLineSpacingMm()+cell.getMarginTopMm())){
                        bandBlockContainer.addEmptyBlock();
                    } else {
                        cellBlockContainer = createCellContainer(cell, bandBlockContainer, cellDisplayHeightMm/*+q*/, cellTopMm + getCurHeightMm(), marginMm, CellWrappingState.NO_WRAPPED /* adjustedCell.wasWrapped() && cell.getTopMm()==0 ? CellWrappingState.MIDDLE_PART_WRAPPED_CELL : CellWrappingState.FIRST_PART_WRAPPED_CELL*/);
                    }
                }
                adjustedCell.setWasWrapped(true);
                adjustedCell.setWasSeparated(cellActualHeightMm > 0);
                if (lineCntOnCurPage < lineCount) {   //calc wrapped part
                    if (!isHeightCalc) {
                        isHeightCalc = true;
                        drawnBandHeightOnCurPage = cellTopMm + cellDisplayHeightMm /*+ getRootForm().getMargin().getTopMm()*/;
                    }
                    if (cellBlockContainer != null && lineCntOnCurPage > 0) {//???? ??????????? ?????? ?? ??????? ??????,?? ????????? ????-????????? ??????
                        for (int i = 0; i < lineCntOnCurPage; i++) {
                            adjustedCell.getLineCellContents().remove(0);
                        }
                        cell.setTopMm(0);
                        //cell.setMarginTopMm(0);
                        cell.setHeightMm(cell.getHeightMm() - cellActualHeightMm + addHeight);
                    } else {
                        cell.setTopMm(0);
                        marginMm = marginMm/*cell.getMarginTopMm()*/ - cellActualHeightMm;
                        cell.setMarginTopMm(marginMm < 0 ? 0 : marginMm);
                        cell.setHeightMm(cell.getHeightMm() - cellActualHeightMm + addHeight);
                    }
                    cellsForNextPage.add(cell);
                } else {
                    if (!isHeightCalc) {
                        isHeightCalc = true;
                        drawnBandHeightOnCurPage = cellTopMm + cellDisplayHeightMm /*+ getRootForm().getMargin().getTopMm()*/;
                    }
                    if (!isImage(cell)) {
                        adjustedCell.getLineCellContents().clear();
                        cell.setTopMm(0);
                        cell.setHeightMm(cell.getHeightMm() - cellActualHeightMm + addHeight);
                        cellsForNextPage.add(cell);
                    } else {
                        //if(!isHeightCalc){
                        //    isHeightCalc=true;                      
                        //    drawnBandHeightOnCurPage = cellTopMm + cellDisplayHeightMm; 
                        //}
                        if (cellDisplayHeightMm == 0) {
                            cell.setTopMm(0);
                            cell.setHeightMm(cell.getHeightMm() - cellActualHeightMm + addHeight);
                            cellsForNextPage.add(cell);
                        }
                    }
                    lastBandPartHeight = cellTopMm + cellDisplayHeightMm;
                }
                if (cellBlockContainer != null) {
                    setVerticalMargin(cellBlockContainer, cell.getMarginBottomMm()/*, cell.getLineSpacingMm()*/);
                    cellBlockContainer.end();
                }
            } else {
                cellsForNextPage.add(cell);
            }
        }
        return cellsForNextPage;
    }

    private double[] calcWrapLines(final Map<AdsReportCell, AdjustedCell> adjustedCellContents, final List<AdsReportCell> wrappedCells, double d) {
        double wrappedLine = 0;
        double iconHeight = 0;
        double minWrappedLine = Double.MAX_VALUE;
        boolean noWrap = false;
        for (AdsReportCell cell : wrappedCells) {
            double topMm = cell.getTopMm() == 0 ? 0 : cell.getTopMm() - d;
            if (getPageHeight() > (topMm + getCurHeightMm())) {
                AdjustedCell adjustedCell = adjustedCellContents.get(cell);
                //double topMm = cell.getTopMm() == 0 ? 0 : cell.getTopMm() - d;
                double marginMm = calcCellMargin(cell, adjustedCell.wasSeparated());
                double textTopMm = marginMm/*cell.getMarginTopMm()*/ + topMm;
                double pageHeight = getPageHeight() - getCurHeightMm();
                double[] linesInfo = calcLinesOnCurPage(cell, adjustedCell, textTopMm, pageHeight);
                if (linesInfo[0] > 0) {
                    if (cell.isAdjustHeight()) {
                        double cellContentHeigh = linesInfo[1] + marginMm/*calcCellMargin(cell, adjustedCell.wasWrapped())*/;
                        double bottomMm = cellContentHeigh + topMm;
                        if ((bottomMm > wrappedLine)) {
                            wrappedLine = bottomMm;
                        }
                        double contentHeight = calcContentHeight(adjustedCell, cell.getLineSpacingMm()) + marginMm/* cell.getMarginTopMm()*/ + cell.getMarginBottomMm();
                        if (bottomMm < minWrappedLine && linesInfo[0] < adjustedCell.getLineCount() && contentHeight == cell.getHeightMm()) {
                            minWrappedLine = bottomMm;
                        }
                    } else {
                        wrappedLine = pageHeight;
                    }
                } else if (isFirstDataBandOnPage() && (adjustedCell.wasWrapped()) && topMm == 0) {
                    if (isImage(cell)) {
                        if (iconHeight < topMm + cell.getHeightMm()) {
                            iconHeight = topMm + cell.getHeightMm();
                        }
                    } else if (textTopMm < pageHeight) {
                        if ((adjustedCell.getLineCount() != 0) && (textTopMm > wrappedLine)) {
                            wrappedLine = textTopMm;
                        }
                    }
                } else {
                    if ((cell.getTopMm() - d) > wrappedLine) {
                        wrappedLine = cell.getTopMm() - d;
                    } else if (/*(getPageHeight()-getCurHeight() < cell.getMarginTopMm())*/!isFirstDataBandOnPage() && (/*cell.getMarginTopMm()*/marginMm < getPageHeight())) {
                        noWrap = true;
                    }
                }
            }
        }
        if (iconHeight > 0 && wrappedLine < iconHeight) {
            wrappedLine = iconHeight;
        }
        wrappedLine = wrappedLine == 0 && !noWrap ? getPageHeight() - getCurHeightMm() : wrappedLine;

        minWrappedLine = minWrappedLine == Double.MAX_VALUE ? wrappedLine : minWrappedLine;
        return new double[]{minWrappedLine, wrappedLine};
    }

    private boolean isImage(AdsReportCell cell) {
        return (cell.getCellType() == EReportCellType.IMAGE || cell.getCellType() == EReportCellType.DB_IMAGE || cell.getCellType() == EReportCellType.CHART);
    }

    private List<AdsReportCell> getCells(IReportWidgetContainer cellContainer) {
        List<AdsReportCell> widgetList = new LinkedList<>();
        for (AdsReportWidget widget : cellContainer.getWidgets()) {
            if (widget.isReportContainer()) {
                widgetList.addAll(getCells((IReportWidgetContainer) widget));
            } else {
                AdsReportCell cell = (AdsReportCell) widget;
                widgetList.add(cell);
            }
        }
        return widgetList;
    }

    private FoBlockContainer createBandContainer(final AdsReportBand band, final double height) throws XMLStreamException {
        final FoBlockContainer bandBlockContainer = flow.addNewBlockContainer();
        bandBlockContainer.begin();

        final String bandBgColor = (band.isBgColorInherited() ? null : color2FoColor(band.getBgColor()));
        final String bandFgColor = (band.isFgColorInherited() ? null : color2FoColor(band.getFgColor()));

        final AdsReportForm rootForm = getRootForm();
        setBlockContainerParams(bandBlockContainer,
                rootForm.getMargin().getLeftMm(),
                getCurHeightMm(),
                rootForm.getPageWidthMm() - rootForm.getMargin().getLeftMm() - rootForm.getMargin().getRightMm(),
                height,
                bandBgColor,
                bandFgColor,
                band.getBorder(),
                band.getFont(), CellWrappingState.NO_WRAPPED);
        //boolean someCellDisplayed = false;
        return bandBlockContainer;
    }
    private final List<Double> ranges = new LinkedList<>();
    private int correction = 1;

    @Override
    protected void newPage(List<ReportGenData> genDataList) throws ReportGenerationException {
        super.newPage(genDataList);
        cleanupRanges();
    }

    private void cleanupRanges() {
        correction = ranges.size() % 2 == 0 ? 1 : 2;
        ranges.clear();
    }

    private int getLogicalRowIndexByCellTop(double top) {
        if (ranges.isEmpty()) {
            ranges.add(top);
            return correction;
        } else {
            for (int i = ranges.size() - 1; i >= 0; i--) {
                double height = ranges.get(i);
                if (top > height) {
                    if (i == ranges.size() - 1) {
                        ranges.add(top);
                        return ranges.size() + correction - 1;
                    } else {//we will not change any settings and just return index of match +1
                        return i + correction;
                    }
                } else if (top == height) {
                    return i + correction;
                }
            }
            return correction;
        }
    }

    private FoBlockContainer createCellContainer(final AdsReportCell cell, FoBlockContainer bandBlockContainer, double height, double top, double topMargin, CellWrappingState wrappingState) throws XMLStreamException {
        final FoBlockContainer cellBlockContainer = bandBlockContainer.addNewBlockContainer();
        cellBlockContainer.begin();

        final String cellBgColor;
        Color zebraColor = cell.getAltBgColor();
        boolean ignoreZebra = zebraColor == null;

        if (!ignoreZebra && getLogicalRowIndexByCellTop(top) % 2 == 0) {
            cellBgColor = XmlColor.mergeColor(zebraColor);
        } else {
            cellBgColor = cell.isBgColorInherited() ? null : color2FoColor(cell.getBgColor());
        }

        final String cellFgColor = cell.isFgColorInherited() ? null : color2FoColor(cell.getFgColor());

        final AdsReportForm rootForm = getRootForm();

        setBlockContainerParams(cellBlockContainer,
                cell.getLeftMm() + rootForm.getMargin().getLeftMm(),
                top,
                cell.getWidthMm(),
                height,
                cellBgColor,
                cellFgColor,
                cell.getBorder(),
                cell.getFont(),
                wrappingState);

        //cellBlockContainer.setMargin(cell.getMarginMm()); 
        cellBlockContainer.setLeftMargin(cell.getMarginLeftMm());
        cellBlockContainer.setRightMargin(cell.getMarginRightMm());

        //final Font cellFont = cell.getFont();
        //cellBlockContainer.setLineHeight(cellFont.getSizeMm()+cell.getLineSpacingMm());
        if (cell.isClipContent()) {
            cellBlockContainer.setOverflow(FoBlockContainer.OverflowType.HIDDEN);
        } else {
            cellBlockContainer.setOverflow(FoBlockContainer.OverflowType.VISIBLE);
        }

        if (cell.getCellType() != EReportCellType.DB_IMAGE) {

            switch (cell.getVAlign()) {
                case MIDDLE:
                    cellBlockContainer.setDisplayAlign(FoBlockContainer.DisplayAlignType.CENTER);
                    break;
                case BOTTOM:
                    cellBlockContainer.setDisplayAlign(FoBlockContainer.DisplayAlignType.AFTER);
                    break;
                default:
                    cellBlockContainer.setDisplayAlign(FoBlockContainer.DisplayAlignType.BEFORE);
            }

            switch (cell.getHAlign()) {
                case LEFT:
                    cellBlockContainer.setTextAlign(FoBlockContainer.TextAlignType.START);
                    break;
                case CENTER:
                    cellBlockContainer.setTextAlign(FoBlockContainer.TextAlignType.CENTER);
                    break;
                case RIGHT:
                    cellBlockContainer.setTextAlign(FoBlockContainer.TextAlignType.END);
                    break;
                case JUSTIFY:
                    cellBlockContainer.setTextJustify();
                    break;
            }
            setVerticalMargin(cellBlockContainer, topMargin/*cell.getMarginTopMm()*/);
        }

        return cellBlockContainer;

    }

    private void setVerticalMargin(FoBlockContainer cellBlockContainer, double marginMm/*,double lineHeightMm*/) throws XMLStreamException {
        final FoBlock block = cellBlockContainer.addNewBlock();
        block.begin();
        block.setTopMargin(marginMm);
        block.end();
    }

    @Override
    protected boolean isFlowDependent() {
        return false;
    }

    private void addEmptyBand() throws ReportGenerationException {
        try {
            final FoBlockContainer bandBlockContainer = flow.addNewBlockContainer();
            bandBlockContainer.begin();
            bandBlockContainer.setAbsolutePositionFixed();

            final AdsReportForm rootForm = getRootForm();
            bandBlockContainer.setLeft(rootForm.getMargin().getLeftMm());
            bandBlockContainer.setWidth(rootForm.getPageWidthMm() - rootForm.getMargin().getLeftMm() - rootForm.getMargin().getRightMm());
            bandBlockContainer.setTop(getCurHeightMm());
            bandBlockContainer.setHeight(0.0);

            bandBlockContainer.addEmptyBlock(); // for xml validation
            bandBlockContainer.end();
        } catch (XMLStreamException ex) {
            throw new ReportGenerationException(ex);
        }
    }

    @Override
    protected void finishPage(final List<ReportGenData> genDataList) throws ReportGenerationException {
        if (!someBandOnPageDisplayed) {
            addEmptyBand();
        }
        super.finishPage(genDataList);
        try {
            page.end();
            flow.end();
            page = null;
            flow = null;
        } catch (XMLStreamException ex) {
            throw new ReportGenerationException(ex);
        }
    }

    @Override
    protected void buildPageHeader(List<ReportGenData> genDataList) throws ReportGenerationException {
        try {
            curPageHeight = 0;
            page = root.addNewPageSequence();
            page.begin();
            page.setMasterReference(PAGE_MASTER_NAME);

            flow = page.addNewFlow();
            flow.begin();
            flow.setFlowName("xsl-region-body");
        } catch (XMLStreamException ex) {
            throw new ReportGenerationException(ex);
        }
        someBandOnPageDisplayed = false;
        super.buildPageHeader(genDataList);
    }

    @Override
    protected void buildPageFooter(List<ReportGenData> genDataList) throws ReportGenerationException {
        FoFlow currentFlow = flow;
        try {
            flow = new FoFlow();
            flow.begin();
            flow.setFlowName("dummy");
            double curHeight = getCurHeightMm();
            super.buildPageFooter(genDataList);
            double footerHeight = getCurHeightMm() - curHeight;
            setFooterHeight(0, footerHeight);
            setCurHeightMm(getRootForm().getPageHeightMm() - getRootForm().getMargin().getBottomMm() - getCurPageFooterHeightMm());
            flow.end();
            flow = currentFlow;
            super.buildPageFooter(genDataList);
            setCurHeightMm(curHeight);
        } catch (XMLStreamException ex) {
            //should never occurs
        }
    }

    private double getMostBottomCellPosition(final AdsReportBand band) {
        double result = 0.0;
        boolean first = true;
        for (AdsReportWidget cell : band.getWidgets()) {
            final double cellBottomPos = cell.getTopMm() + cell.getHeightMm();
            if (first) {
                result = cellBottomPos;
                first = false;
            } else {
                result = Math.max(cellBottomPos, result);
            }
        }
        return result;
    }

    /**
     * if height of band is automatically - set band height as most bottom cell
     * position, otherwise - increment band height by increment of most bottom
     * cell position.
     */
    @Override
    protected Map<AdsReportCell, AdjustedCell> adjustBand(AdsReportBand band) throws ReportGenerationException {
        final CellsAdjuster cellsAdjuster = new CellsAdjuster();

        if (band.isAutoHeight()) {
            cellsAdjuster.adjustCells(band);
            final double mostBottomCellPos = getMostBottomCellPosition(band);
            band.setHeightMm(mostBottomCellPos);
        } else {
            final double oldMostBottomCellPos = getMostBottomCellPosition(band);
            cellsAdjuster.adjustCells(band);
            final double newMostBottomCellPos = getMostBottomCellPosition(band);
            band.setHeightMm(band.getHeightMm() + newMostBottomCellPos - oldMostBottomCellPos);
        }
        return cellsAdjuster.getAdjustedCellContents();
    }

    @Override
    protected void adjustCellsPosition(AdsReportBand container) {
        final CellsAdjuster cellsAdjuster = new CellsAdjuster();
        cellsAdjuster.adjustCellsPosition(container);
    }

    private static final double INCH2MM = 25.4;
    private static final double DPMM = 1.0 * FopReportGenerator.DEFAULT_REPORT_DPI / INCH2MM; // dot per millimeter

    public static int mm2px(double mm) {
        return (int) (mm * DPMM * 1.0);
    }

    public static double px2mm(int px) {
        return (double) px / DPMM;
    }

    @Override
    protected byte[] setupDbImageCellSize(AdsReportDbImageCell cell, String mimeType, byte[] imageData) {
        if (mimeType == null || imageData == null || imageData.length == 0) {
            return imageData;
        }
        if (!mimeType.endsWith("svg")) {

            try {
                double vBorder = 0;
                double hBorder = 0;
                if (cell.getBorder() != null) {
                    double t = cell.getBorder().getThicknessMm();
                    if (cell.getBorder().getOnLeft()) {
                        hBorder += t;
                    }
                    if (cell.getBorder().getOnRight()) {
                        hBorder += t;
                    }
                    if (cell.getBorder().getOnTop()) {
                        vBorder += t;
                    }
                    if (cell.getBorder().getOnBottom()) {
                        vBorder += t;
                    }
                }
                EImageScaleType scaleType = cell.getScaleType();
                double imageWidth = cell.getWidthMm() - cell.getMarginLeftMm() - cell.getMarginRightMm() - hBorder;
                double imageHeight = cell.getHeightMm() - cell.getMarginTopMm() - cell.getMarginBottomMm() - vBorder;
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageData));
                double requestedWidth = px2mm(image.getWidth());
                double requestedHeight = px2mm(image.getHeight());
                switch (scaleType) {
                    case CROP://CROP if image larger than container
                        //create sub image that fits container size;
                        double w = Math.min(imageWidth, requestedWidth);
                        double h = Math.min(imageHeight, requestedHeight);
                        final double left;
                        final double top;
                        //apply alignment settings
                        switch (cell.getHAlign()) {
                            case CENTER:
                                left = (requestedWidth - w) / 2;
                                break;
                            case RIGHT:
                                left = requestedWidth - w;
                                break;
                            default:
                                left = 0;
                                break;
                        }
                        switch (cell.getVAlign()) {
                            case MIDDLE:
                                top = (requestedHeight - h) / 2;
                                break;
                            case BOTTOM:
                                top = requestedHeight - h;
                                break;
                            default:
                                top = 0;
                                break;
                        }
                        cell.setAdjustHeightMm(imageHeight);
                        cell.setAdjustHeight(true);
                        return getSubImage(image, left, top, w, h);
                    case RESIZE_CONTAINER:
                        if (requestedWidth == 0) {
                            requestedHeight = cell.getMarginTopMm() + cell.getMarginBottomMm() + vBorder;
                        } else {
                            double ratio = requestedHeight / requestedWidth;
                            if (requestedWidth > imageWidth) {
                                requestedWidth = imageWidth;
                                requestedHeight = requestedWidth * ratio + cell.getMarginTopMm() + cell.getMarginBottomMm() + vBorder;
                            }
                        }
                        cell.setAdjustHeightMm(requestedHeight);
                        cell.setAdjustHeight(true);
                        return scaleImageToSizeMM(image, requestedWidth, requestedHeight);
                    case FIT_TO_CONTAINER:
                        //compute max width and height
                        if (requestedWidth == 0 || requestedHeight == 0) {
                            return imageData;
                        }
                        double scaleFactor = imageWidth / requestedWidth;
                        if (scaleFactor * requestedHeight > imageHeight) {
                            scaleFactor = imageHeight / requestedHeight;
                        }
                        requestedWidth *= scaleFactor;
                        requestedHeight *= scaleFactor;
                        cell.setAdjustHeightMm(imageHeight);
                        cell.setAdjustHeight(true);
                        return scaleImageToSizeMM(image, requestedWidth, requestedHeight);
                    case SCALE_TO_CONTAINER://breaks image ratio   
                        cell.setAdjustHeightMm(imageHeight);
                        cell.setAdjustHeight(true);
                        return scaleImageToSizeMM(image, imageWidth, imageHeight);
                }

            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, "Unable to decode db image", ex);
                //ignore
            }
        }
        return imageData;
    }

    private byte[] scaleImageToSizeMM(Image image, double widthMM, double heightMM) throws IOException {
        int cellWidth = mm2px(Math.floor(widthMM));
        int cellHeight = mm2px(Math.floor(heightMM));
        Image scaledImage = image.getScaledInstance(cellWidth, cellHeight, Image.SCALE_SMOOTH);
        BufferedImage target = new BufferedImage(cellWidth, cellHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = target.createGraphics();
        try {
            graphics.drawImage(scaledImage, 0, 0, null);
        } finally {
            graphics.dispose();
        }
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(target, "png", out);
        File outFile = new File("/home/akrylov/BUG/WireCardEditor/WDB_converted.png");
        ImageIO.write(target, "png", outFile);
        return out.toByteArray();
    }

    private byte[] getSubImage(Image image, double leftMM, double topMM, double widthMM, double heightMM) throws IOException {
        int cellWidth = mm2px(Math.round(widthMM));
        int cellHeight = mm2px(Math.round(heightMM));
        int leftPx = mm2px(leftMM);
        int topPx = mm2px(topMM);
        BufferedImage target = new BufferedImage(cellWidth, cellHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = target.createGraphics();
        try {
            graphics.drawImage(image,
                    0,
                    0,
                    cellWidth,
                    cellHeight,
                    leftPx,
                    topPx,
                    leftPx + cellWidth,
                    topPx + cellHeight, null);
        } finally {
            graphics.dispose();
        }
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(target, "png", out);
        //DEBUG
//        File io = new File("/home/akrylov/reports/testimage/target.png");
//        io.getParentFile().mkdirs();
//        FileUtils.writeBytes(io, out.toByteArray());
        return out.toByteArray();
    }

    private void addPageMaster() throws XMLStreamException {
        final FoLayoutMasterSet layout = root.addNewLayoutMasterSet();
        layout.begin();

        final FoSimplePageMaster pageMaster = layout.addNewSimplePageMaster();
        pageMaster.begin();

        final AdsReportForm rootForm = getRootForm();

        pageMaster.setMasterName(PAGE_MASTER_NAME);
        pageMaster.setPageWidth(rootForm.getPageWidthMm());
        final String pageHeight = (isInfiniteHeight() ? "0000000000.00mm" : FoObject.toFopMm(rootForm.getPageHeightMm())); // for post-write
        pageMaster.setPageHeight(pageHeight);

        final FoRegionBody regionBody = pageMaster.addNewRegionBody();
        regionBody.begin();

        regionBody.setMarginLeft(rootForm.getMargin().getLeftMm());
        regionBody.setMarginRight(rootForm.getMargin().getRightMm());
        regionBody.setMarginTop(rootForm.getMargin().getTopMm());
        regionBody.setMarginBottom(rootForm.getMargin().getBottomMm());

        final String formBgColor = color2FoColor(rootForm.getBgColor());
        regionBody.setBackgroundColor(formBgColor);

        regionBody.end();
        pageMaster.end();
        layout.end();
    }

//    private void postWritePageHeight(final File foFile) throws IOException {
//        if (isInfiniteHeight()) {
//            final RandomAccessFile randomAccessFile = new RandomAccessFile(foFile, "rw");
//            try {
//                final String pageSize = FoObject.toFopMm(getCurHeight());
//                randomAccessFile.seek(224 - pageSize.length());
//                randomAccessFile.writeBytes(pageSize);
//            } finally {
//                randomAccessFile.close();
//            }
//        }
//    }
    private void openRoot(OutputStream foStream) throws XMLStreamException {
        final XMLStreamWriter foXmlStreamWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(foStream, FileUtils.XML_ENCODING);
        root = new FoRoot(foXmlStreamWriter);
        root.begin();

        final AdsReportForm rootForm = getRootForm();
        final String formFgColor = color2FoColor(rootForm.getFgColor());
        root.setColor(formFgColor);

        addPageMaster();
    }

    private void closeRoot() throws XMLStreamException {
        root.end();
    }

//    @Override
//    public void generateReport(final OutputStream stream) throws ReportGenerationException {
//        try {
//            openRoot(stream);
//            super.generateReport(stream);
//            closeRoot();
//            //postWritePageHeight(file);
//        } catch (XMLStreamException ex) {
//            throw new ReportGenerationException(ex);
//        }
//    }
    @Override
    protected void newFile(List<ReportGenData> genDataList, OutputStream stream) throws ReportGenerationException {
        super.newFile(genDataList, stream);
        try {
            openRoot(stream);
        } catch (XMLStreamException ex) {
            throw new ReportGenerationException(ex);
        }
    }

    @Override
    protected OutputStream getOutputStream(File reportFile) throws IOException {
        final FileOutputStream out = new FileOutputStream(reportFile);
        final ZipEntry e = new ZipEntry("report.fo");

        ZipOutputStream zout = new ZipOutputStream(out);
        zout.putNextEntry(e);
        return zout;
    }

    @Override
    protected void finishFile(List<ReportGenData> genDataList) throws ReportGenerationException {
        try {
            closeRoot();
        } catch (XMLStreamException ex) {
            throw new ReportGenerationException(ex);
        }
        super.finishFile(genDataList);
    }

}
