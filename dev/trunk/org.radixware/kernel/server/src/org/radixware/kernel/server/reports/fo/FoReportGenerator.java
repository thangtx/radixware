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

import org.radixware.kernel.common.defs.ads.clazz.sql.report.html.CellContents;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.html.AdjustedCell;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportAbstractAppearance;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportAbstractAppearance.Border;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportAbstractAppearance.Font;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportDbImageCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidget;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.IReportWidgetContainer;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.html.CellParagraph;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.utils.AdsReportUtils;
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
import org.radixware.kernel.server.reports.ReportColumnsAdjuster;
import org.radixware.kernel.server.reports.ReportGenerationException;
import org.radixware.kernel.server.reports.ReportStateInfo;
import org.radixware.kernel.server.types.Report;
import org.w3c.dom.svg.SVGAnimatedLength;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGLength;
import org.w3c.dom.svg.SVGSVGElement;

public class FoReportGenerator extends AbstractReportGenerator {

    private static final String PAGE_MASTER_NAME = "page";
    private FoRoot root = null;
    private FoPageSequence page = null;
    private FoFlow flow = null;
    private boolean someBandOnPageDisplayed = false;
    private double curPageHeight = 0;
    private double drawnBandHeight = 0;//высота отрисованной части полосы
    private AdsReportBand drawnBand = null;//сама не отрисованная полоса
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

    public static double[] getBorderSize(AdsReportAbstractAppearance container, boolean wasSeparated) {
        double vBorder = 0;
        double hBorder = 0;
        if (container != null) {
            Border border = container.getBorder();
            if (border != null) {
                if (border.getOnLeft()) {
                    hBorder += border.getLeftThicknessMm();
                }
                if (border.getOnRight()) {
                    hBorder += border.getRightThicknessMm();
                }
                if (border.getOnTop() && !wasSeparated) {
                    vBorder += border.getTopThicknessMm();
                }
                if (border.getOnBottom()) {
                    vBorder += border.getBottomThicknessMm();
                }
            }
        }
        return new double[]{vBorder, hBorder};
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
//            final FoBlockContainer.BorderStyle foBorderStyle = borderStyleToFoBorderStyle(border.getStyle());
//            final double borderThicknesPts = border.getThicknessMm();
//            final double borderThicknessMm = border.getThicknessMm();
//            final String borderColor = color2FoColor(border.getColor());

//            if (border.isOnLeft() && border.isOnRight() && border.isOnTop() && border.isOnBottom() && wrappingState == CellWrappingState.NO_WRAPPED) {
//                blockContainer.setBorder(foBorderStyle, borderColor, borderThicknesPts);
//                top += borderThicknessMm;
//                left += borderThicknessMm;
//                width -= 2 * borderThicknessMm;     // deprecated: FOP draw top border inside the cell, but other - outside cell,
//                height -= 2 * borderThicknessMm;    // and if top border presented, the bottom border moved twice.
//            } else {
                if (border.isOnLeft()) {
                    double borderThicknessMm = border.getLeftThicknessMm();
                    blockContainer.setBorderLeft(
                            borderStyleToFoBorderStyle(border.getLeftStyle()), 
                            color2FoColor(border.getLeftColor()), 
                            borderThicknessMm);
                    left += borderThicknessMm;
                    width -= borderThicknessMm;
                }
                if (border.isOnRight()) {
                    double borderThicknessMm = border.getRightThicknessMm();
                    blockContainer.setBorderRight(
                            borderStyleToFoBorderStyle(border.getRightStyle()), 
                            color2FoColor(border.getRightColor()), 
                            borderThicknessMm);
                    width -= borderThicknessMm;
                }
                if (border.isOnTop() && (wrappingState == CellWrappingState.NO_WRAPPED || wrappingState == CellWrappingState.FIRST_PART_WRAPPED_CELL)) {
                    double borderThicknessMm = border.getTopThicknessMm();
                    blockContainer.setBorderTop(
                            borderStyleToFoBorderStyle(border.getTopStyle()), 
                            color2FoColor(border.getTopColor()), 
                            borderThicknessMm);
                    top += borderThicknessMm;
                    height -= borderThicknessMm;
                }
                if (border.isOnBottom() && (wrappingState == CellWrappingState.NO_WRAPPED || wrappingState == CellWrappingState.LAST_PART_WRAPPED_CELL)) {
                    double borderThicknessMm = border.getBottomThicknessMm();
                    blockContainer.setBorderBottom(
                            borderStyleToFoBorderStyle(border.getBottomStyle()), 
                            color2FoColor(border.getBottomColor()), 
                            borderThicknessMm);
                    height -= borderThicknessMm;
                }
//            }
        }

        blockContainer.setLeft(left);
        blockContainer.setTop(top);
        blockContainer.setWidth(width);
        blockContainer.setHeight(height);
    }

    private boolean viewCell(final AdsReportCell cell, AdjustedCell adjustedCell, final AdsReportBand band, FoBlockContainer bandBlockContainer/*, boolean wrapping, boolean isNewPageRequired */, ReportGenData currentGenData) throws XMLStreamException, ReportGenerationException {
        if (!cell.isVisible()) {
            return false;
        }
        double cellTopMm = cell.getTopMm();

        FoBlockContainer cellBlockContainer;
        final String content = cell.getRunTimeContent();
        double marginMm = calcCellTopMargin(cell, adjustedCell.wasSeparated());
        if ((cell.getCellType() == EReportCellType.IMAGE || cell.getCellType() == EReportCellType.DB_IMAGE || cell.getCellType() == EReportCellType.CHART)
                && content != null && !content.isEmpty()) {

            double[] bordersSize = getBorderSize(cell, adjustedCell.wasSeparated());
            double vBorder = bordersSize[0];
            double hBorder = bordersSize[1];
            cellBlockContainer = createCellContainer(cell, bandBlockContainer, cell.getHeightMm(), cellTopMm + getCurHeightMm(), marginMm, calcCellTopBorder(cell, adjustedCell), CellWrappingState.NO_WRAPPED, currentGenData);
            lastBandPartHeight = cellTopMm + cell.getHeightMm();

            FoBlock block = cellBlockContainer.addNewBlock();
            block.begin();
            block.setLeftMargin(cell.getMarginLeftMm());
            block.setRightMargin(cell.getMarginRightMm());
            block.setFontSize(0);
            
            FoExternalGraphic externalGraphic = block.addNewExternalGraphic();
            externalGraphic.begin();
            externalGraphic.setSrc(content);

            double imageWidth = cell.getWidthMm() - cell.getMarginLeftMm() - cell.getMarginRightMm() - hBorder;
            double imageHeight = cell.getHeightMm() - cell.getMarginTopMm() - cell.getMarginBottomMm() - vBorder;
            externalGraphic.setWidthMm(imageWidth);
            externalGraphic.setHeightMm(imageHeight);
            if (cell.getCellType() == EReportCellType.DB_IMAGE) {
                AdsReportDbImageCell dbImageCell = (AdsReportDbImageCell) cell;
                String mimeType = dbImageCell.getMimeType();           
                
                externalGraphic.setContentWidth(String.valueOf(imageWidth) + "mm");
                externalGraphic.setContentHeight(String.valueOf(imageHeight) + "mm");
                        
                if (dbImageCell.getScaleType() == EImageScaleType.FIT_TO_CONTAINER || dbImageCell.getScaleType() == EImageScaleType.CROP || dbImageCell.getScaleType() == EImageScaleType.RESIZE_CONTAINER) {
                    externalGraphic.setVerticalAlign(cell.getVAlign());
                    externalGraphic.setHorizontalAlign(cell.getHAlign());
                    externalGraphic.writeAttribute("padding", "0mm");
                    externalGraphic.writeAttribute("margin", "0mm");
                    if (mimeType != null && mimeType.contains("/svg")) {
                        externalGraphic.setScaling(FoExternalGraphic.SCALING_UNIFORM);
                    }
                } else {
                    externalGraphic.setVerticalAlign(EReportCellVAlign.MIDDLE);
                    externalGraphic.setHorizontalAlign(EReportCellHAlign.CENTER);
                    if (mimeType != null && mimeType.contains("/svg")) {
                        externalGraphic.setScaling(FoExternalGraphic.SCALING_NONUNIFORM);
                    }
                }

            } else {
                externalGraphic.setContentWidth(FoExternalGraphic.SCALE_TO_FIT);
                externalGraphic.setContentHeight(FoExternalGraphic.SCALE_TO_FIT);
            }
            externalGraphic.end();
            block.end();
        } else {
            boolean isJustify = cell.getHAlign() == EReportCellHAlign.JUSTIFY;
            cellBlockContainer = createCellContainer(cell, bandBlockContainer, cell.getHeightMm(), cellTopMm + getCurHeightMm(), marginMm, calcCellTopBorder(cell, adjustedCell), CellWrappingState.NO_WRAPPED/*wrapping && cell.getTopMm()==0 ? CellWrappingState.LAST_PART_WRAPPED_CELL:CellWrappingState.NO_WRAPPED*/, 
                    currentGenData);
            for (CellParagraph cellParagraph : adjustedCell) {
                for (int i = 0; i < cellParagraph.getLinesCount(); i++) {
                    addLine(cellBlockContainer, adjustedCell, cellParagraph, i, isJustify, cell.getLineSpacingMm(),cell.getMarginLeftMm(),cell.getMarginRightMm());
                }
            }
            if (isDataBand(band)) {
                lastBandPartHeight = cellTopMm + cell.getHeightMm();
            }
            if (cellBlockContainer != null && adjustedCell.getParagraphsCount() == 0) {
                cellBlockContainer.addEmptyBlock(); // for xml validation
            }
        }
        if (cellBlockContainer != null) {
            setVerticalMargin(cellBlockContainer, cell.getMarginBottomMm()/*, cell.getLineSpacingMm()*/);
            cellBlockContainer.end();
        }
        return true;
    }
    
    private void addLine(FoBlockContainer cellBlockContainer, AdjustedCell adjustedCell, CellParagraph paragraph, int line, boolean isJustify, double lineSpasing,double marginLeft,double marginRight) throws XMLStreamException, ReportGenerationException {
        FoBlockContainer lineBlockContainer = cellBlockContainer.addNewBlockContainer();        
        lineBlockContainer.begin();
        lineBlockContainer.setLineHeight(adjustedCell.getLineHeight(paragraph, line, lineSpasing));       
      
        FoBlock lineBlock = addBlockToBlockContainer(lineBlockContainer, isJustify && paragraph.isLastLineInParagraph(line));
        
        lineBlock.setLeftMargin(marginLeft);
        lineBlock.setRightMargin(marginRight);
        
        if (isJustify) {
            lineBlock.setTextJustify();
        }
        
        List<CellContents> contents = paragraph.getContentsByLine(line);
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

    private double calcCellTopMargin(final AdsReportCell cell, boolean wrapping) {
        if (wrapping) {
            return 0;
        } else {
            return cell.getMarginTopMm();
        }
    }
    
    private double[] calcLinesOnCurPage(final AdsReportCell cell, final AdjustedCell adjustedCell, final double textTopMm, final double textBottomMm, final double wrappedLine) {
        return isImage(cell) ? new double[]{0, 0, cell.getHeightMm()} : calcLinesOnCurPage(adjustedCell, textTopMm, textBottomMm, cell.getLineSpacingMm(), wrappedLine);
    }

    /**
     * 
     * @param adjustedCell
     * @param textTopMm
     * @param lineSpasing
     * @param wrappedLine
     * @return array with the following information.
     *          [0] - last paragraph number on the current page
     *          [1] - last line in the paragraph on the current page
     *          [2] - resulting height
     */
    private double[] calcLinesOnCurPage(final AdjustedCell adjustedCell, final double textTopMm, final double textBottomMm, final  double lineSpasing, final double wrappedLine) {
        int i = 0, p;
        double lineHeigh;
        double heigh = 0;
        int paragraphsCount = adjustedCell.getParagraphsCount();
        double outerWidth = textTopMm;
        for (p = 0; p < paragraphsCount; p++) {
            CellParagraph paragraph = adjustedCell.getParagraph(p);
            boolean adjusted = false;
            int lineCount = paragraph.getLinesCount();
            for (i = 0; i < lineCount; i++) {
                lineHeigh = adjustedCell.getLineHeight(paragraph, i, lineSpasing);
                heigh += lineHeigh;
                //for last line in cell
                if ((p == (paragraphsCount - 1)) && (i == (lineCount - 1))) {
                    outerWidth += textBottomMm;
                }
                if ((heigh + outerWidth) > wrappedLine) {
                    heigh = heigh - lineHeigh;//-lineSpasing;
                    adjusted = true;
                    break;
                }
            }
            if (adjusted) {
                break;
            }
        }
        return new double[]{p >= paragraphsCount ? p-1 : p, i, heigh};
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

    private void addInLineToBlock(CellContents c, FoBlock block) throws XMLStreamException, ReportGenerationException {
        FoInline inlineForTextDecoration = null;
        final FoInline inline = block.addNewInline();
        inline.begin();       
//        inline.setBackgroundColor("#00FF00");
//        inline.setMargin(0);
        if (c.getBgColor() != null) {
            String bgColor = color2FoColor(c.getBgColor());
            inline.setBackgroundColor(bgColor);
        }
        if (c.getFgColor() != null) {
            String fgColor = color2FoColor(c.getFgColor());
            inline.setColor(fgColor);
        }
        if (c.getFont() != null) {
            org.apache.fop.fonts.Font fopFont = CellsAdjuster.lookupFopFont(c.getFont());
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
    protected void viewBand(List<ReportGenData> genDataList, AdsReportBand band, Map<AdsReportCell, AdjustedCell> adjustedCellContents, ReportGenData currentGenData) throws ReportGenerationException {
        try {
            //drawnBandHeight=0;
            bandNumber++;
            double height = isBandGoIntoPage(band) ? band.getHeightMm() : (getPageHeight() - getCurHeightMm());
            List<AdsReportCell> widgetList = getCells(band);
            FoBlockContainer bandBlockContainer = createBandContainer(band, height, currentGenData);
            ZebraStripingInfo info = ranges.get(band);
            if (info != null && drawnBand != band) {
                    info.nextCorrection();
            }
            viewCells(band, bandBlockContainer, widgetList, adjustedCellContents, genDataList, currentGenData);
            if (drawnBand == band){
                if (drawnBandHeight > 0) {
                    band.setHeightMm(band.getHeightMm()  - drawnBandHeight);
                }
                drawnBandHeight = 0;
                drawnBand = null;
            }
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

    private void viewCells(final AdsReportBand band, FoBlockContainer bandBlockContainer, List<AdsReportCell> widgetList, Map<AdsReportCell, AdjustedCell> adjustedCellContents, final List<ReportGenData> genDataList, ReportGenData currentGenData) throws XMLStreamException, ReportGenerationException {
        boolean someCellDisplayed = false;
        drawnBandHeightOnCurPage = Double.MAX_VALUE;
        double d;
        if (drawnBand == band){
            d = drawnBandHeight;
        } else {
            d = 0; 
        }
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
                if (viewCell(upperCell, adjustedCell, band, bandBlockContainer/*,falseadjustedCell.wasWrapped(),isNewPageRequired*/, currentGenData)) {
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
            wrapCells(bandBlockContainer, adjustedCellContents, wrappedCells, cellsForNextPage, d, currentGenData);
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
            viewOnNewPage(band, genDataList, adjustedCellContents, cellsForNextPage, currentGenData);
        }
        someBandOnPageDisplayed = true;
    }

    private void wrapCells(FoBlockContainer bandBlockContainer, final Map<AdsReportCell, AdjustedCell> adjustedCellContents, final List<AdsReportCell> wrappedCells, final List<AdsReportCell> cellsForNextPage, double d, ReportGenData currentGenData) throws XMLStreamException, ReportGenerationException {
        List<AdsReportCell> cellsWrappenonNextPage = new LinkedList<>();
        cellsWrappenonNextPage.addAll(viewWrappedCells(bandBlockContainer, adjustedCellContents, wrappedCells, d, currentGenData));
        if (delta != 0) {
            for (AdsReportCell c : cellsForNextPage) {
                c.setTopMm(c.getTopMm() + delta);
            }
        }
        cellsForNextPage.addAll(0, cellsWrappenonNextPage);
    }

    private void viewOnNewPage(final AdsReportBand band, final List<ReportGenData> genDataList, Map<AdsReportCell, AdjustedCell> adjustedCellContents, List<AdsReportCell> cellsForNextPage, ReportGenData currentGenData) throws ReportGenerationException, XMLStreamException {
        double d = drawnBandHeight;
        int num = bandNumber % 2;
        finishPage(genDataList);
        newPage(genDataList);
        bandNumber = num;
        drawnBandHeight = d;
        drawnBand = band;
        double bandHeight = band.getHeightMm() - drawnBandHeight;
        bandHeight = (getCurHeightMm() + bandHeight > getPageHeight())
                ? (getPageHeight() - getCurHeightMm()) : bandHeight;
        FoBlockContainer bandBlockContainer = createBandContainer(band, bandHeight, currentGenData);
        viewCells(band, bandBlockContainer, cellsForNextPage, adjustedCellContents, genDataList, currentGenData);
    }

    private double delta = 0.0;

    private List<AdsReportCell> viewWrappedCells(FoBlockContainer bandBlockContainer, final Map<AdsReportCell, AdjustedCell> adjustedCellContents, final List<AdsReportCell> wrappedCells, double d, ReportGenData currentGenData) throws XMLStreamException, ReportGenerationException {
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
                int paragraphsCount = isImage(cell) ? 0 : adjustedCell.getParagraphsCount();
                
                double marginMm = calcCellTopMargin(cell, adjustedCell.wasSeparated());
                double topBorderThickness = 0;
                double textBottomMm = cell.getMarginBottomMm();
                Border border = cell.getBorder();
                if (border != null) {
                    border = border.copy(false);
                    if (adjustedCell.wasSeparated()) {
                        border.setOnTop(false);
                    } else {
                        topBorderThickness += border.getOnTop()? border.getTopThicknessMm(): 0;
                    }
                    textBottomMm += border.getOnBottom()? border.getBottomThicknessMm(): 0;
                }
                double textTopMm = marginMm/*cell.getMarginTopMm()*/ + cellTopMm + topBorderThickness;

                double[] linesInfo = calcLinesOnCurPage(cell, adjustedCell, textTopMm, 0, wrappedLine); //???-?? ????? ?? ??????? ??????, ??????? ?????????? ?? ??????? ????????                  
                int paragraphCntOnCurPage = (int) linesInfo[0];
                int lineCntOnCurPage = (int) linesInfo[1];
                CellParagraph cellParagraph = adjustedCell.getParagraphsCount() > 0 ? adjustedCell.getParagraph(paragraphCntOnCurPage) : null;
                int lineCount = cellParagraph == null ? 0 : cellParagraph.getLinesCount();
                boolean isTransfer = isTransfer(adjustedCell, paragraphCntOnCurPage, lineCntOnCurPage);
                double cellDisplayHeightMm = wrappedLine - cellTopMm;
                double cellActualHeightMm = isTransfer ? linesInfo[2] + marginMm/*calcCellMargin(cell, adjustedCell.wasWrapped())*/ : cellDisplayHeightMm;
                double addHeight = (isTransfer)
                        || (paragraphCntOnCurPage == paragraphsCount && lineCntOnCurPage == lineCount && (cellActualHeightMm + cellTopMm) == wrappedLine) ? (cellActualHeightMm + cellTopMm) - minWrappedLine : 0;

                FoBlockContainer cellBlockContainer = null;
                /*double q=0.0;
                 if(hasBottomBorder(cell) && (cell.getHeightMm()-cellHeightMm)>0){//for bottom border
                 q=1.0;
                 } */
                if (border != null) {
                    border.setOnBottom(false);
                }
                if (paragraphCntOnCurPage > 0 || lineCntOnCurPage > 0) {  //view cell 
                    cellBlockContainer = createCellContainer(cell, bandBlockContainer, cellDisplayHeightMm/*+q*/, cellTopMm + getCurHeightMm(), marginMm, border, true, CellWrappingState.NO_WRAPPED /* adjustedCell.wasWrapped() &&  cell.getTopMm()==0 ? CellWrappingState.MIDDLE_PART_WRAPPED_CELL : CellWrappingState.FIRST_PART_WRAPPED_CELL*/,
                            currentGenData);
                    boolean isJustify = cell.getHAlign() == EReportCellHAlign.JUSTIFY;
                    for (int i = 0; i <= paragraphCntOnCurPage; i++) {
                        CellParagraph paragraph = adjustedCell.getParagraph(i);
                        int size = paragraph.getLinesCount();
                        if (i == paragraphCntOnCurPage) {
                            size = lineCntOnCurPage;
                        }
                        for (int j = 0; j < size; j++) {
                            addLine(cellBlockContainer, adjustedCell, paragraph, j, isJustify, cell.getLineSpacingMm(),cell.getMarginLeftMm(),cell.getMarginRightMm());
                        }
                    }
                } else {
                    if (cellDisplayHeightMm == 0) {//lineCount>0 && lineCntOnCurPage==0 && cellDisplayHeightMm<(adjustedCell.getLineFontSize(0)+cell.getLineSpacingMm()+cell.getMarginTopMm())){
                        bandBlockContainer.addEmptyBlock();
                    } else {
                        cellBlockContainer = createCellContainer(cell, bandBlockContainer, cellDisplayHeightMm/*+q*/, cellTopMm + getCurHeightMm(), marginMm, border, CellWrappingState.NO_WRAPPED /* adjustedCell.wasWrapped() && cell.getTopMm()==0 ? CellWrappingState.MIDDLE_PART_WRAPPED_CELL : CellWrappingState.FIRST_PART_WRAPPED_CELL*/,
                                currentGenData);
                    }
                }
                adjustedCell.setWasWrapped(true);
                adjustedCell.setWasSeparated(cellActualHeightMm > 0 || isTransfer);
                if (isTransfer) {   //calc wrapped part
                    if (!isHeightCalc) {
                        isHeightCalc = true;
                        drawnBandHeightOnCurPage = cellTopMm + cellDisplayHeightMm /*+ getRootForm().getMargin().getTopMm()*/;
                    }
                    if (cellBlockContainer != null && (paragraphCntOnCurPage > 0 || lineCntOnCurPage > 0)) {//???? ??????????? ?????? ?? ??????? ??????,?? ????????? ????-????????? ??????
                        int size = lineCntOnCurPage == lineCount ? paragraphCntOnCurPage + 1 : paragraphCntOnCurPage;
                        for (int i = 0; i < size; i++) {
                            adjustedCell.removeFirst();
                        }
                        if (cellParagraph != null){
                            for (int i = 0; i < lineCntOnCurPage; i++) {
                                cellParagraph.removeFirst();
                            }
                        }
                        cell.setTopMm(0);
                        //cell.setMarginTopMm(0);
                        cell.setHeightMm(cell.getHeightMm() - cellActualHeightMm + addHeight);
                    } else {
                        adjustedCell.setWasSeparated(false);
                        cell.setTopMm(0);
//                        marginMm = marginMm/*cell.getMarginTopMm()*/ - cellActualHeightMm;
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
                        adjustedCell.clear();
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
                double marginMm = calcCellTopMargin(cell, adjustedCell.wasSeparated());
                double textBottomMm = cell.getMarginBottomMm();
                Border border = cell.getBorder();
                if (border != null) {
                    marginMm += border.getOnTop()? border.getTopThicknessMm(): 0;
                    textBottomMm += border.getOnBottom()? border.getBottomThicknessMm(): 0;
                }
                double textTopMm = marginMm/*cell.getMarginTopMm()*/ + topMm;
                double pageHeight = getPageHeight() - getCurHeightMm();
                double[] linesInfo = calcLinesOnCurPage(cell, adjustedCell, textTopMm, textBottomMm, pageHeight);
                if (linesInfo[0] > 0 || (linesInfo[0] == 0 && linesInfo[1] > 0)) {
                    if (cell.isAdjustHeight()) {
                        double cellContentHeigh = linesInfo[2] + marginMm/*calcCellMargin(cell, adjustedCell.wasWrapped())*/;
                        double bottomMm = cellContentHeigh + topMm;
                        if ((bottomMm > wrappedLine)) {
                            wrappedLine = bottomMm;
                        }
                        double contentHeight = adjustedCell.getContentHeight(cell.getLineSpacingMm()) + marginMm/* cell.getMarginTopMm()*/ + cell.getMarginBottomMm();
                        if (bottomMm < minWrappedLine && 
                                isTransfer(adjustedCell, (int) linesInfo[0], (int) linesInfo[1])
                                && contentHeight == cell.getHeightMm()) {
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
                        if ((adjustedCell.getParagraphsCount() != 0) && (textTopMm > wrappedLine)) {
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
    
    private boolean isTransfer(AdjustedCell adjustedCell, int paragraphCntOnCurPage, int lineCntOnCurPage) {
        int paragraphsCount = adjustedCell.getParagraphsCount();
        CellParagraph cellParagraph = paragraphsCount > 0 ? adjustedCell.getParagraph(paragraphCntOnCurPage) : null;
        int lineCount = cellParagraph == null ? 0 : cellParagraph.getLinesCount();
        return paragraphsCount > 0 && 
                        (paragraphCntOnCurPage < (paragraphsCount - 1) || paragraphCntOnCurPage == (paragraphsCount - 1) && lineCntOnCurPage < lineCount);
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

    private FoBlockContainer createBandContainer(final AdsReportBand band, final double height, ReportGenData currentGenData) throws XMLStreamException {
        final FoBlockContainer bandBlockContainer = flow.addNewBlockContainer();
        bandBlockContainer.begin();

        final String bandBgColor = (band.isBgColorInherited() ? null : color2FoColor(band.getBgColor()));
        final String bandFgColor = (band.isFgColorInherited() ? null : color2FoColor(band.getFgColor()));

        final AdsReportForm rootForm = getRootForm();
        double leftMm = rootForm.getMargin().getLeftMm();
        double rightMm = rootForm.getMargin().getRightMm();
        if (currentGenData instanceof SubReportGenData){
            SubReportGenData genData = (SubReportGenData) currentGenData;
            leftMm = genData.getMargin().getLeftMm();
            rightMm = genData.getMargin().getRightMm();
        }
        setBlockContainerParams(bandBlockContainer,
                    leftMm,
                    getCurHeightMm(),
                    rootForm.getPageWidthMm() - leftMm - rightMm,
                    height,
                    bandBgColor,
                    bandFgColor,
                    band.getBorder(),
                    band.getFont(), CellWrappingState.NO_WRAPPED);
        //boolean someCellDisplayed = false;
        return bandBlockContainer;
    }
    private final Map<AdsReportBand, ZebraStripingInfo> ranges = new HashMap<>();
    private int bandNumber = 0;

    @Override
    protected void newPage(List<ReportGenData> genDataList) throws ReportGenerationException {
        super.newPage(genDataList);
        cleanupRanges();
        bandNumber = 0;
    }

    private void cleanupRanges() {
        for (AdsReportBand band : ranges.keySet()){
            ZebraStripingInfo info = ranges.get(band);
            info.cleanupRanges();
        }
    }
    
    private String getColor(final AdsReportCell cell, double top, boolean storeColor){
        Color zebraColor = cell.getAltBgColor();
        boolean ignoreZebra = zebraColor == null;
        
        if (!ignoreZebra) {
            AdsReportBand band = cell.getOwnerBand();
            ZebraStripingInfo info = ranges.get(band);
            if (info == null) {
                info = new ZebraStripingInfo();
                ranges.put(band, info);
            }
            if (info.getLogicalRowIndexByCellTop(cell, top, band.isInsideAltColor(), storeColor) % 2 == 0) {
                return XmlColor.mergeColor(zebraColor);
            }
        }
        return cell.isBgColorInherited() ? null : color2FoColor(cell.getBgColor());
    }
    
    private FoBlockContainer createCellContainer(final AdsReportCell cell, FoBlockContainer bandBlockContainer, double height, double top, double topMargin, Border border, CellWrappingState wrappingState, ReportGenData currentGenData) throws XMLStreamException {
        return createCellContainer(cell, bandBlockContainer, height, top, topMargin, border, false, wrappingState, currentGenData);
    }
    
    private FoBlockContainer createCellContainer(final AdsReportCell cell, FoBlockContainer bandBlockContainer, double height, double top, double topMargin, Border border, boolean storeColor, CellWrappingState wrappingState, ReportGenData currentGenData) throws XMLStreamException {
        final FoBlockContainer cellBlockContainer = bandBlockContainer.addNewBlockContainer();
        cellBlockContainer.begin();

        final String cellBgColor = getColor(cell, top, storeColor);
        
        final String cellFgColor = cell.isFgColorInherited() ? null : color2FoColor(cell.getFgColor());

        final AdsReportForm rootForm = getRootForm();
        double leftMm = rootForm.getMargin().getLeftMm();
        double rightMm = rootForm.getMargin().getRightMm();
        if (currentGenData instanceof SubReportGenData){
            SubReportGenData genData = (SubReportGenData) currentGenData;
            leftMm = genData.getMargin().getLeftMm();
            rightMm = genData.getMargin().getRightMm();
        }

        setBlockContainerParams(cellBlockContainer,
                cell.getLeftMm() + leftMm,
                top,
                cell.getWidthMm(),
                height,
                cellBgColor,
                cellFgColor,
                border,
                cell.getFont(),
                wrappingState);

        //cellBlockContainer.setMargin(cell.getMarginMm()); 
//        cellBlockContainer.setLeftMargin(cell.getMarginLeftMm());
//        cellBlockContainer.setRightMargin(cell.getMarginRightMm());

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
            double height = mostBottomCellPos;         
            Border b = band.getBorder();
            double bottomBorder = b == null ? 0 : b.getOnBottom() ? b.getBottomThicknessMm() : 0;
            double topBorder = b == null ? 0 : b.getOnTop() ? b.getTopThicknessMm() : 0;

            if (height == 0.0) {
                height += topBorder;
            } else {
                height = Math.max(topBorder, height);
            }
            height += bottomBorder;
            band.setHeightMm(height);
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
    protected byte[] setupDbImageCellSize(AdsReportDbImageCell cell, String mimeType, byte[] imageData, boolean dbImageResizeSupressed) {
        if (mimeType == null || imageData == null || imageData.length == 0) {
            return imageData;
        }
        double[] bordersSize = getBorderSize(cell, false);
        double vBorder = bordersSize[0];
        double hBorder = bordersSize[1];
        double imageWidth = cell.getWidthMm() - cell.getMarginLeftMm() - cell.getMarginRightMm() - hBorder;
        double imageHeight = cell.getHeightMm() - cell.getMarginTopMm() - cell.getMarginBottomMm() - vBorder;
        EImageScaleType scaleType = cell.getScaleType();
        if (!mimeType.contains("/svg")) {
            try {
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
                        double[] requestedSize = resizeContainer(cell, imageWidth, imageHeight, requestedWidth, requestedHeight, vBorder);
                        return !dbImageResizeSupressed && cell.isResizeImage() ? scaleImageToSizeMM(image, requestedSize[0], requestedSize[1]) : imageData;
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
                        return !dbImageResizeSupressed && cell.isResizeImage() ? scaleImageToSizeMM(image, requestedWidth, requestedHeight) : imageData;
                    case SCALE_TO_CONTAINER://breaks image ratio   
                        cell.setAdjustHeightMm(imageHeight);
                        cell.setAdjustHeight(true);
                        return !dbImageResizeSupressed && cell.isResizeImage() ? scaleImageToSizeMM(image, imageWidth, imageHeight) : imageData;
                }

            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, "Unable to decode db image", ex);
                //ignore
            }
        } else {
            if (scaleType == EImageScaleType.RESIZE_CONTAINER){
                try (InputStream is = new ByteArrayInputStream(imageData)) {
                    String parser = XMLResourceDescriptor.getXMLParserClassName();
                    SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
                    SVGDocument svgDocument = (SVGDocument) factory.createDocument(null, is);
                    final SVGSVGElement rootElement = svgDocument.getRootElement();
                    final UserAgentAdapter userAgentAdapter = new UserAgentAdapter();
                    final BridgeContext bridgeContext = new BridgeContext(userAgentAdapter);
                    final GVTBuilder builder = new GVTBuilder();
                    final GraphicsNode graphicsNode = builder.build(bridgeContext, svgDocument);
                    Rectangle2D bounds = graphicsNode.getBounds();
                    final double originalWidth = getLengthMM(rootElement.getWidth(), bounds == null? 0: bounds.getWidth());
                    final double originalHeight = getLengthMM(rootElement.getHeight(), bounds == null? 0: bounds.getHeight());    
                    resizeContainer(cell, imageWidth, imageHeight, originalWidth, originalHeight, vBorder);
                } catch (IOException ex) {
                    Logger.getLogger(FoReportGenerator.class.getName()).log(Level.SEVERE, "Unable to decode db image", ex);
                }
            } else {
                cell.setAdjustHeightMm(imageHeight);
                cell.setAdjustHeight(true);
            }
        }
        
        return imageData;
    }
    
    private double[] resizeContainer(AdsReportDbImageCell cell, double imageWidth, double imageHeight, 
            double requestedWidth, double requestedHeight, double vBorder) {
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
        return new double[]{requestedWidth, requestedHeight};
    }
    
    private static double getLengthMM(SVGAnimatedLength animatedLength, double defaults) {
        final SVGLength length = animatedLength.getBaseVal();
        final int initType = length.getUnitType();
        final double width = length.getValueInSpecifiedUnits();
        switch (initType) {
            case SVGLength.SVG_LENGTHTYPE_PX:
            case SVGLength.SVG_LENGTHTYPE_NUMBER:
                return AdsReportUtils.pts2mm(width);
            case SVGLength.SVG_LENGTHTYPE_PT:
                //A pt is 1/72 of an in, and a px is 1/96 of an in. A px is therefore 0.75 pt
                return AdsReportUtils.pts2mm(width / 0.75);
            case SVGLength.SVG_LENGTHTYPE_MM:
                return width;
            case SVGLength.SVG_LENGTHTYPE_CM:
                return width * 100;
            default:
                return AdsReportUtils.pts2mm(defaults);
        }
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
        ranges.clear();
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

    @Override
    protected void beforeCloseGroup(ReportGenData reportGenData, int groupIndex) {
        super.beforeCloseGroup(reportGenData, groupIndex);
        if (!ranges.isEmpty()) {
            for (AdsReportBand band : ranges.keySet()) {
                if (band.getType() == EReportBandType.DETAIL && reportGenData.form.getBands().contains(band)) {
                    ZebraStripingInfo info = ranges.get(band);
                    info.clear();
                }
            }
        }
    }

    @Override
    protected void afterSubReportClose(SubReportGenData subReportGenData) {
        super.afterSubReportClose(subReportGenData);
        if (!ranges.isEmpty()) {
            Iterator<AdsReportBand> bands = ranges.keySet().iterator();
            while (bands.hasNext()) {
                AdsReportBand band = bands.next();
                if (subReportGenData.form.getBands().contains(band)) {
                    bands.remove();
                }
            }
        }
    }
    private Border calcCellTopBorder(final AdsReportCell cell, AdjustedCell adjustedCell) {
        Border border = cell.getBorder();
        if (border != null) {
            border = border.copy(false);
            if (adjustedCell.wasSeparated()) {//|| adjustedCell.wasWrapped()
                border.setOnTop(false);
            }
        }
        return border;
    }

    @Override
    protected void adjustCellsToColumns(AdsReportBand container) {
        if (columnsSettings != null) {
            ReportColumnsAdjuster adjuster = new ReportColumnsAdjuster(container, columnsSettings, new FoCellWrapperFactory());
            adjuster.adjustColumnsBySettings();
        }
    }        
}
