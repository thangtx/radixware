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

import java.util.*;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.MimeConstants;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportAbstractAppearance.Font;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportDbImageCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidget;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidgetContainer;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.IReportWidgetContainer;
import org.radixware.kernel.common.enums.EReportCellType;
import org.radixware.kernel.common.enums.EReportLayout;
import org.radixware.kernel.server.reports.ReportGenerationException;

public class CellsAdjuster {

    private static final double MM_TO_PS_COEFFICIENT = 2834.64567; // from FOP sources
    private static final double PS_TO_MM_COEFFICIENT = 1.0 / MM_TO_PS_COEFFICIENT;
    private static org.apache.fop.fonts.FontInfo fontInfoCache = null;

    private static org.apache.fop.fonts.FontInfo getFontInfo() throws ReportGenerationException {
        if (fontInfoCache == null) {
            try {
                final FOUserAgent foUserAgent = FoUserAgentFactory.getFoUserAgent(MimeConstants.MIME_PDF);
                final org.apache.fop.render.Renderer renderer = foUserAgent.getRendererFactory().createRenderer(foUserAgent, MimeConstants.MIME_PDF);
                final org.apache.fop.fonts.FontInfo fontInfo = new org.apache.fop.fonts.FontInfo();
                renderer.setupFontInfo(fontInfo);
                fontInfoCache = fontInfo;
            } catch (Throwable ex) {
                throw new ReportGenerationException(ex);
            }
        }
        return fontInfoCache;
    }

    public org.apache.fop.fonts.Font lookupFopFont(final Font font) throws ReportGenerationException {
        final org.apache.fop.fonts.FontInfo fontInfo = getFontInfo();

        final int font_weight = (font.isBold() ? org.apache.fop.fonts.Font.WEIGHT_BOLD : org.apache.fop.fonts.Font.WEIGHT_NORMAL);
        final String style = (font.isItalic() ? org.apache.fop.fonts.Font.STYLE_ITALIC : org.apache.fop.fonts.Font.STYLE_NORMAL);
        final int fontHeightPs = (int) (MM_TO_PS_COEFFICIENT * font.getSizeMm());

        final org.apache.fop.fonts.FontTriplet triplet = fontInfo.fontLookup(font.getName(), style, font_weight);
        final org.apache.fop.fonts.Font fopFont = fontInfo.getFontInstance(triplet, fontHeightPs);

        return fopFont;
    }

    public static double getCharWidthMm(final char c, final org.apache.fop.fonts.Font fopFont) {
        final int charWidthPs = fopFont.getCharWidth(c);
        return PS_TO_MM_COEFFICIENT * charWidthPs;
    }

    private double calcCellWidthMm(final AdsReportCell cell) throws ReportGenerationException {
        //List<CellContents> cellContant = HtmlParser.getCellContant(cell);
        AdjustedCell adjustedCell = adjustedCellContents.get(cell);

        double lineWidthMm = 0.0, maxWidthMm = 0.0;
        //int line=0;
        for (int l = 0; l < adjustedCell.getLineCount(); l++) {
            List<CellContents> list = adjustedCell.getContentsByLine(l);
            for (int j = 0; j < list.size(); j++) {
                CellContents cont = list.get(j);
                final Font font = cont.getFont();
                final org.apache.fop.fonts.Font fopFont = lookupFopFont(font);

                String content = cont.getText();//cell.getRunTimeContent();
                if (content != null) {
                    //boolean wasNewLine=false;
                    int prevPos = 0;
                    for (int i = 0; i < content.length(); i++) {
                        final char c = content.charAt(i);

                        if (c == '\n') {
                            lineWidthMm = 0.0;
                            //wordWidthMm = 0.0;
                            //insPos = 0;                            
                            //wasNewLine=true;
                            maxWidthMm = Math.max(maxWidthMm, lineWidthMm);

                            cont.setText(new String(content.substring(prevPos, i + 1)));
                            /*if(l!=line){
                             adjustedCell.changeLine(cont, l, line);
                             }
                             line++;
                             CellContents secondContent=new CellContents(cont);
                             secondContent.setText(new String(content.substring(i+1)));
                             adjustedCell.addCellContent(secondContent, line); */
                            splitContent(adjustedCell, new int[]{i + 1, j}, l);
                            prevPos = i + 1;
                            //break;
                        } else {
                            double charWidthMm = getCharWidthMm(c, fopFont);
                            lineWidthMm += charWidthMm;
                        }
                    }
                }
            }
        }
        //if(adjustedCellContents==null){
        //   adjustedCellContents=new HashMap<>();
        //}
        //cellContaint.put(cell, adjustedCell);
        return lineWidthMm;
    }

    public Map<AdsReportCell, AdjustedCell> getAdjustedCellContents() {
        return adjustedCellContents;
    }

    private Map<AdsReportCell, AdjustedCell> adjustedCellContents = new HashMap<>();

    private void wrapWords(final AdsReportCell cell) throws ReportGenerationException {
        AdjustedCell adjustedCell = adjustedCellContents.get(cell);
        //String runTimeContent="";
        StringBuilder sb = new StringBuilder();
        for (int l = 0; l < adjustedCell.getLineCount(); l++) {//cycle by lines
            List<CellContents> list = adjustedCell.getContentsByLine(l);
            double lineWidthMm = 0.0, wordWidthMm = 0.0;
            double maxWidthMm = cell.getWidthMm() - /*cell.getMarginMm() * 2*/ (cell.getMarginLeftMm() + cell.getMarginRightMm());
            int insPos[] = new int[]{0, 0};
            for (int j = 0; j < list.size(); j++) {//cycle by cell contants                
                CellContents content = list.get(j);
                final Font font = content.getFont();
                final org.apache.fop.fonts.Font fopFont = lookupFopFont(font);

                //double lineWidthMm = 0.0, wordWidthMm = 0.0;
                //double maxWidthMm = cell.getWidthMm() - /*cell.getMarginMm() * 2*/(cell.getMarginLeftMm()+cell.getMarginRightMm());
                //int insPos = 0;
                boolean inQuotes = false;
                //int lastSignificantCharPos = 0;

                String text = content.getText();//cell.getRunTimeContent();
                if (text != null) {
                    for (int i = 0; i < text.length(); i++) {
                        final char c = text.charAt(i);

                        //if (c != '\n' && c != '\r' && c != '\t' && c != ' ') {
                        //    lastSignificantCharPos = i;
                        // }
                        if (c == '\n') {
                            i++;
                            insPos[0] = i;
                            insPos[1] = j;
                            splitContent(adjustedCell, insPos, l);
                            lineWidthMm = 0.0;
                            wordWidthMm = 0.0;
                            insPos[0] = 0;
                            break;
                        } else {
                            double charWidthMm = getCharWidthMm(c, fopFont);
                            if (lineWidthMm > 0 && lineWidthMm + charWidthMm > maxWidthMm && c != ' ') {//??????? ?? ????????? ??????
                                if (insPos[0] == 0) {
                                    insPos[0] = i;
                                    insPos[1] = j;
                                    wordWidthMm = 0;
                                }
                                //content = text.substring(0, insPos) + "\n" + text.substring(insPos);
                                //prevPos=i+1;
                                splitContent(adjustedCell, insPos, l);

                                i++;
                                insPos[0] = 0;
                                lineWidthMm = wordWidthMm;
                                break;
                            }

                            lineWidthMm += charWidthMm;

                            if (c == '(' || c == '{' || c == '[' || c == '$' || (c == '"' && !inQuotes)) {
                                wordWidthMm = charWidthMm;
                                insPos[0] = i;
                                insPos[1] = j;
                            } else if (c == ' ' || c == ';' /*|| c == ',' || c == ':' || c == '.'*/ || c == ')' || c == ']' || c == '}' || c == '!' || c == '?' || c == '%' || (c == '"' && inQuotes) || c == '-') {
                                wordWidthMm = 0;
                                insPos[0] = i + 1;
                                insPos[1] = j;
                            } else if ((c == ',' || c == ':' || c == '.') && (!isNumber(text, i + 1))) {
                                wordWidthMm = 0;
                                insPos[0] = i + 1;
                                insPos[1] = j;
                            } else {
                                wordWidthMm += charWidthMm;
                            }

                            if (c == '"') {
                                inQuotes = !inQuotes;
                            }
                        }
                    }

                    //if (lastSignificantCharPos < text.length()) {
                    //    text = text.substring(0, lastSignificantCharPos + 1);
                    //}
                    // sb.append(text);
                    //runTimeContent+=text;
                }
            }
        }
        for (int l = 0; l < adjustedCell.getLineCount(); l++) {
            List<CellContents> list = adjustedCell.getContentsByLine(l);
            for (int j = 0; j < list.size(); j++) {
                sb.append(list.get(j).getText());
            }
        }
        cell.setRunTimeContent(sb.toString());
    }

    private void splitContent(AdjustedCell adjustedCell,/*CellContents content,*/ int[] insPos, int line/*,int contentIndex*/) {
        int charIndex = insPos[0];
        int contentIndex = insPos[1];

        CellContents content = adjustedCell.getContentsByLine(line).get(contentIndex);
        String text = content.getText();
        content.setText(new String(text.substring(0, charIndex)));

        CellContents secondContent = new CellContents(content);
        secondContent.setText(new String(text.substring(charIndex)));
        int newContentIndex = 0;
        adjustedCell.insertCellContent(newContentIndex, secondContent, line + 1);

        for (int i = contentIndex + 1; i < adjustedCell.getContentsByLine(line).size(); i++) {
            CellContents c = adjustedCell.getContentsByLine(line).get(i);
            newContentIndex++;
            adjustedCell.moveToNexLine(c, line, newContentIndex);
            i--;
        }
    }

    private boolean isNumber(String content, int charPos) {
        if ((charPos < content.length()) && ((content.charAt(charPos) == '0')
                || (content.charAt(charPos) == '1') || (content.charAt(charPos) == '2')
                || (content.charAt(charPos) == '3') || (content.charAt(charPos) == '4')
                || (content.charAt(charPos) == '5') || (content.charAt(charPos) == '6')
                || (content.charAt(charPos) == '7') || (content.charAt(charPos) == '8')
                || (content.charAt(charPos) == '9'))) {
            return true;
        }
        return false;
    }

    public static int calcLineCount(final AdsReportCell cell) {
        int lineCount = 1;
        final String content = cell.getRunTimeContent();

        if (content != null) {
            for (int i = 0; i < content.length(); i++) {
                if (content.charAt(i) == '\n') {
                    lineCount++;
                }
            }
        }
        return lineCount;
    }

    public void adjustBands(List<AdsReportBand> bands) throws ReportGenerationException {
        List<AdsReportWidget> widgets = new LinkedList<>();
        for (AdsReportBand band : bands) {
            widgets.addAll(band.getWidgets().list());
        }
        final Guides guides = findHorizontalGuides(widgets);
        //
        resizeWidgetsWidth(guides);
    }

    public void adjustCells(final AdsReportBand band) throws ReportGenerationException {
        adjustCellContent(band);
        adjustCellsWidth(band);
        adjustCellsHeight(band);
    }

    public void adjustCellContent(final IReportWidgetContainer band) throws ReportGenerationException {//
        for (AdsReportWidget widget : band.getWidgets()) {
            if (!widget.isReportContainer()) {
                AdsReportCell cell = (AdsReportCell) widget;
                List<CellContents> cellContant = HtmlParser.getCellContant((AdsReportCell) cell);
                AdjustedCell adjustedCell = new AdjustedCell();

                List<CellContents> line = new ArrayList<>();
                for (CellContents content : cellContant) {
                    createLines(content, line, adjustedCell);
                }
                if (!line.isEmpty()) {
                    adjustedCell.getLineCellContents().add(line);
                }
                adjustedCellContents.put(cell, adjustedCell);

                /*System.out.println(cell.getRunTimeContent());
                 int lineNumb=0;
                 for(List<CellContents> line:adjustedCell.getLineCellContents()){
                 System.out.println(lineNumb);
                 lineNumb++;
                 for(int i=0;i<line.size();i++){
                 System.out.println(line.get(i).getText());
                 }
                 }*/
            } else {
                adjustCellContent((IReportWidgetContainer) widget);
            }
        }
    }

    private void createLines(CellContents content, List<CellContents> line, AdjustedCell adjustedCell) {
        String text = content.getText();
        int index = text.indexOf('\n');
        if (text != null && index != -1) {
            CellContents prev = new CellContents(content);
            prev.setText(new String(text.substring(0, index + 1)));
            line.add(prev);
            adjustedCell.getLineCellContents().add(new ArrayList<>(line));

            line.clear();
            CellContents next = new CellContents(content);
            next.setText(new String(text.substring(index + 1)));
            createLines(next, line, adjustedCell);
            //line.add(next);
        } else {
            line.add(content);
        }

    }

    public void adjustCellsPosition(AdsReportBand container) {
        if (container != null) {
            for (AdsReportWidget cell : container.getWidgets()) {
                adjustCellsPosition(cell);
            }
        }
    }

    private void adjustCellsPosition(AdsReportWidget widget) {
        //boolean needAdjustWidth=false;
        if (widget.isReportContainer()) {
            IReportWidgetContainer container = (IReportWidgetContainer) widget;
            for (AdsReportWidget cell : container.getWidgets()) {
                cell.setTopMm(cell.getTopMm() + widget.getTopMm());
                cell.setLeftMm(cell.getLeftMm() + widget.getLeftMm());
                adjustCellsPosition(cell);
            }
        }
        //return needAdjustWidth;
    }

    public double adjustCellsHeight(final IReportWidgetContainer band) throws ReportGenerationException {
        RadixObjects<AdsReportWidget> widgets = band.getWidgets();
        final Guides guides = findVerticalGuides(widgets.list());
        double height;
        for (AdsReportWidget widget : widgets) {
            if (widget.isReportContainer()) {
                height = adjustCellsHeight((IReportWidgetContainer) widget);
                if (height != 0.0) {
                    widget.setHeightMm(height);
                }
            } else {
                AdsReportCell cell = (AdsReportCell) widget;
                if (cell.isVisible()) {
                    adjustCellHeight(cell);
                } else {
                    widget.setHeightMm(0.0);
                }
            }
        }

        height = resizeWidgetsHeight(band, guides);
        for (AdsReportWidget widget : widgets) {
            if (widget.isReportContainer()) {
                resizeContainerHeight((AdsReportWidgetContainer) widget, ((AdsReportWidgetContainer) widget).getHeightMm());
            }
        }
        return height;
    }

    private double resizeWidgetsHeight(IReportWidgetContainer widgetContainter, final Guides guides) throws ReportGenerationException {
        double height = 0.0;
        for (Guide guide : guides) {
            final double bottomInUpper = guide.findLastInPrevVisible();
            for (AdsReportWidget cell : guide.prevCells) {
                cell.setHeightMm(bottomInUpper - cell.getTopMm());
            }
            double delta = (bottomInUpper - guide.lastInPrev);
            if (delta != 0) {
                for (AdsReportWidget cell : guide.nextCells) {
                    setCellTopPosition(cell, delta);
                }
            }
            height = bottomInUpper;
        }
        if (widgetContainter instanceof AdsReportWidgetContainer) {
            height = height - ((AdsReportWidgetContainer) widgetContainter).getTopMm();
        }
        return height;
    }

    private void setCellTopPosition(AdsReportWidget widget, double delta) {
        widget.setTopMm(widget.getTopMm() + delta);
        if (widget.isReportContainer()) {
            IReportWidgetContainer container = (IReportWidgetContainer) widget;
            for (AdsReportWidget childWidget : container.getWidgets()) {
                setCellTopPosition(childWidget, delta);
            }
        }
    }

    private void resizeContainerHeight(final IReportWidgetContainer container, double height) throws ReportGenerationException {
        final Guides guides = findVerticalGuides(container.getWidgets().list());
        double lastPos = guides.get(guides.size() - 1).pos - guides.get(0).pos;
        double delta = height - lastPos;
        if (delta > 0) {
            resizeCellsHeight(container, delta);
        } else {
            for (AdsReportWidget widget : container.getWidgets()) {
                if (widget.isReportContainer()) {
                    resizeContainerHeight((AdsReportWidgetContainer) widget, ((AdsReportWidgetContainer) widget).getHeightMm());
                }
            }
        }
    }

    public void adjustCellsWidth(final AdsReportBand band) throws ReportGenerationException {
        if (isNeedResizeToMin(band.getWidgets())) {
            double width = calcMinWidth(band);//приводим к min размерам
            for (AdsReportWidget w : band.getWidgets()) {//приводим размеры контейнеров в соответствие с содержимым
                if (w.isReportContainer()) {
                    resizeContainerWidth((AdsReportWidgetContainer) w, ((AdsReportWidgetContainer) w).getWidthMm());
                }
            }
            double pageWidth = band.getOwnerForm().getPageWidthMm() - band.getOwnerForm().getMargin().getLeftMm() - band.getOwnerForm().getMargin().getRightMm();
            if (width != pageWidth && width != 0.0) {
                double delta = pageWidth - width;
                resizeCellsWidth(band, delta, false);
            } //else if(width>pageWidth){            
                /*double widgetSpace=width/band.getWidgets().size();
             for (AdsReportAbstractWidget widget : band.getWidgets()) {
             if(widget.isReportContainer()){

             }else{
             AdsReportCell cell=(AdsReportCell)widget;
             if(cell.getWidthMm()>widgetSpace && cell.getCellType()!=EReportCellType.CHART &&
             cell.getCellType()!=EReportCellType.DB_IMAGE){
             cell                   
             }
             }
             }*/
        }
    }

    private boolean isNeedResizeToMin(RadixObjects<AdsReportWidget> widgets) {
        for (AdsReportWidget widget : widgets) {
            if (!widget.isReportContainer()) {
                AdsReportCell cell = (AdsReportCell) widget;
                if (cell.isAdjustWidth()) {
                    return true;
                }
            } else {
                if (widget.isAdjustWidth() || isNeedResizeToMin(((IReportWidgetContainer) widget).getWidgets())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void resizeContainerWidth(final IReportWidgetContainer container, double width) throws ReportGenerationException {
        final Guides guides = findHorizontalGuides(container.getWidgets().list());
        double lastPos = guides.get(guides.size() - 1).pos - guides.get(0).pos;
        double delta = width - lastPos;
        if (delta > 0) {
            resizeCellsWidth(container, delta, true);
        } else {
            for (AdsReportWidget widget : container.getWidgets()) {
                if (widget.isReportContainer()) {
                    resizeContainerWidth((AdsReportWidgetContainer) widget, ((AdsReportWidgetContainer) widget).getWidthMm());
                }
            }
        }
    }

    private double calcMinWidth(IReportWidgetContainer container) throws ReportGenerationException {
        final Guides guides = findHorizontalGuides(container.getWidgets().list());
        resizeToMinimalWidth(container.getWidgets());
        return calcMinContainerWidth(container, guides);
    }

    private void resizeToMinimalWidth(RadixObjects<AdsReportWidget> widgets) throws ReportGenerationException {
        for (AdsReportWidget widget : widgets) {
            if (widget.isReportContainer()) {
                AdsReportWidgetContainer container = (AdsReportWidgetContainer) widget;
                double width = calcMinWidth(container);
                if (container.getOwnerWidget() != null && container.getOwnerWidget().getLayout() == EReportLayout.FREE) {
                    if (width != container.getWidthMm() && width != 0.0) {
                        double delta = container.getWidthMm() - width;
                        resizeCellsWidth(container, delta, false);
                    }
                } else {
                    if (width != 0.0) {
                        container.setWidthMm(width);
                    }
                }
            }
        }
    }

    private double calcMinContainerWidth(IReportWidgetContainer widgetContainter, final Guides guides) throws ReportGenerationException {
        double width = 0.0;
        RadixObjects<AdsReportWidget> widgets = widgetContainter.getWidgets();
        if (widgetContainter instanceof AdsReportWidgetContainer) {
            calcIsContainerAdjustWidth((AdsReportWidgetContainer) widgetContainter, guides);
        }
        EReportLayout layout = widgetContainter.getLayout();
        if (layout == EReportLayout.HORIZONTAL || layout == EReportLayout.GRID) {
            for (AdsReportWidget cell : widgets) {
                adjustCellWidth(cell);
            }
            width = resizeWidgetsWidth(widgetContainter, guides);
        } else if (layout == EReportLayout.VERTICAL) {
            for (AdsReportWidget cell : widgets) {
                adjustCellWidth(cell);
                width = Math.max(width, cell.getWidthMm());
            }
            for (AdsReportWidget cell : widgets) {
                cell.setWidthMm(width);
            }
        } else if (widgetContainter instanceof AdsReportWidgetContainer) {
            width = ((AdsReportWidgetContainer) widgetContainter).getWidthMm();
        }
        return width;
    }

    private void calcIsContainerAdjustWidth(AdsReportWidgetContainer container, final Guides guides) {
        if (container.getLayout() == EReportLayout.VERTICAL) {
            for (AdsReportWidget w : container.getWidgets()) {
                if (w.isAdjustWidth()) {
                    container.setAdjustWidth(true);
                    break;
                }
            }
        } else if (container.getLayout() == EReportLayout.HORIZONTAL) {
            boolean isAdjust = true;
            for (AdsReportWidget w : container.getWidgets()) {
                if (!w.isAdjustWidth()) {
                    isAdjust = false;
                    break;
                }
            }
            container.setAdjustWidth(isAdjust);
        } else if (container.getLayout() == EReportLayout.GRID) {
            boolean isAdjust = false;
            for (Guide g : guides) {
                if (!g.prevCells.isEmpty()) {
                    isAdjust = false;
                    for (AdsReportWidget w : g.prevCells) {
                        if (w.isAdjustWidth()) {
                            isAdjust = true;
                            break;
                        }
                    }
                    if (!isAdjust) {
                        break;
                    }
                }
            }
            container.setAdjustWidth(isAdjust);
        }
    }

    private double resizeWidgetsWidth(final Guides guides) throws ReportGenerationException {
        double width = 0.0;
        for (Guide guide : guides) {
            double bottomInUpper = guide.findLastInPrevVisible();
            for (AdsReportWidget cell : guide.prevCells) {
                cell.setWidthMm(bottomInUpper - cell.getLeftMm());
            }
            double delta = (bottomInUpper - guide.lastInPrev);
            if (delta != 0) {
                for (AdsReportWidget cell : guide.nextCells) {
                    setCellLeftPosition(cell, delta);
                }
            }
            width = bottomInUpper;
        }
        return width;
    }

    private double resizeWidgetsWidth(IReportWidgetContainer widgetContainter, final Guides guides) throws ReportGenerationException {
        double width = resizeWidgetsWidth(guides);//0.0
        //resizeWidgetsWidth(guides);
        if (widgetContainter instanceof AdsReportWidgetContainer) {
            width = width - ((AdsReportWidgetContainer) widgetContainter).getLeftMm();
        }
        return width;
    }

    private void setCellLeftPosition(AdsReportWidget widget, double delta) {
        widget.setLeftMm(widget.getLeftMm() + delta);
        if (widget.isReportContainer()) {
            IReportWidgetContainer container = (IReportWidgetContainer) widget;
            for (AdsReportWidget childWidget : container.getWidgets()) {
                setCellLeftPosition(childWidget, delta);
            }
        }
    }

    public void resizeCellsWidth(IReportWidgetContainer widgetContainter, double delta, boolean resizeInAnyCase) throws ReportGenerationException {
        if (delta == 0.0) {
            return;/* 0.0;*/

        }
        final Guides guides = findHorizontalGuides(widgetContainter.getWidgets().list());
        int n = resizeInAnyCase ? guides.size() - 1 : getResizableGuidesCnt(guides);
        if (n > 0) {
            double addingSpace = delta / n;
            for (AdsReportWidget cell : widgetContainter.getWidgets()) {
                if (cell.isReportContainer() && !cell.isAdjustWidth()) {
                    cell.setWidthMm(cell.getWidthMm() + addingSpace * cell.getColumnSpan());
                    resizeCellsWidth((IReportWidgetContainer) cell, addingSpace * cell.getColumnSpan(), resizeInAnyCase);
                } else {
                    Guide g = guides.findOrCreate(cell.getLeftMm() + cell.getWidthMm(), true);
                    if (resizeInAnyCase || g.canResize()) {
                        cell.setWidthMm(cell.getWidthMm() + addingSpace * cell.getColumnSpan());
                    }
                    //resizeCellWidth((AdsReportCell)cell,addingSpace);
                }
            }
            resizeWidgetsWidth(widgetContainter, guides);
        }
    }

    public void resizeCellsHeight(IReportWidgetContainer widgetContainter, double delta/*,boolean resizeInAnyCase*/) throws ReportGenerationException {
        if (delta == 0.0) {
            return;/* 0.0;*/

        }
        final Guides guides = findVerticalGuides(widgetContainter.getWidgets().list());
        int n =/*resizeInAnyCase ? guides.size()-1 :*/ getResizableGuidesCnt(guides);
        if (n > 0) {
            double addingSpace = delta / n;
            for (AdsReportWidget cell : widgetContainter.getWidgets()) {
                if (cell.isReportContainer() && !cell.isAdjustHeight()) {
                    cell.setHeightMm(cell.getHeightMm() + addingSpace);
                    resizeCellsHeight((IReportWidgetContainer) cell, addingSpace/*,resizeInAnyCase*/);
                } else {
                    Guide g = guides.findOrCreate(cell.getTopMm() + cell.getHeightMm(), true);
                    if (/*resizeInAnyCase || */g.canResize()) {
                        cell.setHeightMm(cell.getHeightMm() + addingSpace);
                    }
                }
            }
            resizeWidgetsHeight(widgetContainter, guides);
        }
    }

    private int getResizableGuidesCnt(final Guides guides) {
        int n = 0;
        for (Guide guide : guides) {
            if (guide.canResize()) {
                n = n + guide.getSnap();
            }
        }
        return /*n-1==0? 1 :*/ n - 1;
    }

    /**
     * Wrap words and calc cell height by line count, if required.
     */
    private void adjustCellHeight(final AdsReportCell cell) throws ReportGenerationException {
        if (cell.isWrapWord()) {
            wrapWords(cell);
        }
        if (cell.getCellType() == EReportCellType.CHART && ((AdsReportChartCell) cell).isPlotMinSizeEnable()) {
            AdsReportChartCell chartCell = (AdsReportChartCell) cell;
            cell.setHeightMm(chartCell.getAdjustHeightMm());
        } else if (cell.getCellType() == EReportCellType.DB_IMAGE) {
            //ignore for this type if cell. size is already adjusted
            //cell.setHeightMm(chartCell.getAdjustHeightMm());
            AdsReportDbImageCell chartCell = (AdsReportDbImageCell) cell;
            cell.setHeightMm(chartCell.getAdjustHeightMm());
        } else if (cell.isAdjustHeight()) {
            AdjustedCell adjustedCell = adjustedCellContents.get(cell);
            double contentHeightMm = 0;
            for (int i = 0; i < adjustedCell.getLineCount(); i++) {
                contentHeightMm += adjustedCell.getLineFontSize(i);
            }
            final int contentLineCount = adjustedCell.getLineCount();
            cell.setHeightMm(/*cell.getMarginMm() * 2 */cell.getMarginTopMm() + cell.getMarginBottomMm() + contentHeightMm/*cell.getFont().getSizeMm() * contentLineCount*/ + cell.getLineSpacingMm() * (contentLineCount - 1));
        }
    }

    private boolean adjustCellWidth(final AdsReportWidget widget) throws ReportGenerationException {
        if (!widget.isReportContainer()) {
            AdsReportCell cell = (AdsReportCell) widget;
            if (cell.isAdjustWidth()) {
                if (cell.getCellType() == EReportCellType.DB_IMAGE) {
                    AdsReportDbImageCell chartCell = (AdsReportDbImageCell) cell;
                    cell.setWidthMm(chartCell.getAdjustWidthMm());
                } else {
                    cell.setWidthMm(/*cell.getMarginMm() * 2 */cell.getMarginLeftMm() + cell.getMarginRightMm() + calcCellWidthMm(cell));
                }
                return true;
            } else {
                if (cell.getCellType() == EReportCellType.CHART && ((AdsReportChartCell) cell).isPlotMinSizeEnable()) {
                    AdsReportChartCell chartCell = (AdsReportChartCell) cell;
                    cell.setWidthMm(chartCell.getAdjustWidthMm());
                } else {
                    cell.setWidthMm(AdsReportCell.MINIMAL_WIDTH);
                }
                return false;
            }
        }
        return false;
    }

    public static Guides findVerticalGuides(List<AdsReportWidget> widgets) {
        final Guides guides = new Guides();
        for (AdsReportWidget cell : widgets) {
            final double top = cell.getTopMm();
            Guide guide = guides.findOrCreate(top, false);
            guide.nextCells.add(cell);

            final double bottom = cell.getTopMm() + cell.getHeightMm();
            guide = guides.findOrCreate(bottom, false);
            guide.prevCells.add(cell);
            if (guide.canResize /*&& !cell.isReportContainer()*/ && cell.isAdjustHeight()) {
                guide.setCanResize(false);
            }
        }
        for (Guide guide : guides) {
            guide.lastInPrev = guide.findLastInPrev();
        }
        Collections.sort(guides);
        return guides;
    }

    public static Guides findHorizontalGuides(List<AdsReportWidget> widgets) {
        final Guides guides = new Guides();
        for (AdsReportWidget widget : widgets) {
            final double left = widget.getLeftMm();
            Guide guide = guides.findOrCreate(left, true);
            guide.nextCells.add(widget);

            final double right = widget.getLeftMm() + widget.getWidthMm();
            guide = guides.findOrCreate(right, true);
            guide.prevCells.add(widget);
            if (guide.canResize && widget.isAdjustWidth()) {
                guide.setCanResize(false);
            }
        }
        for (Guide guide : guides) {
            guide.lastInPrev = guide.findLastInPrev();
        }
        Collections.sort(guides);
        for (AdsReportWidget w : widgets) {
            if (w.getColumnSpan() > 1) {
                for (int i = 0; i < guides.size(); i++) {
                    Guide prevGuide = guides.get(i);
                    int index = 0;
                    if (prevGuide.prevCells.contains(w)) {
                        for (int j = i - 1; j >= 0; j--) {
                            Guide nextGuide = guides.get(j);
                            if (nextGuide.nextCells.contains(w)) {
                                break;
                            }
                            index = index + nextGuide.getSnap();
                        }
                        prevGuide.setSnap(w.getColumnSpan() - index);
                    }
                }

            }
        }
        return guides;
    }

    public static class Guides extends ArrayList<Guide> {

        public Guide findOrCreate(double top, boolean isHorizontal) {
            for (Guide guide : this) {
                if (guide.pos == top || Math.abs(guide.pos - top) <= 1) {
                    return guide;
                }
            }
            final Guide guide = new Guide(top, isHorizontal);
            add(guide);
            return guide;
        }
    }

    public static class Guide implements Comparable {

        private final List<AdsReportWidget> nextCells = new ArrayList<>();
        private final List<AdsReportWidget> prevCells = new ArrayList<>();
        private final double pos;
        private double lastInPrev = 0.0;
        private final boolean isHorizontal;
        private boolean canResize = true;
        private int snap = 1;

        public Guide(double pos, boolean isHorizontal) {
            this.pos = pos;
            this.isHorizontal = isHorizontal;
        }

        public List<AdsReportWidget> getNextCells() {
            return nextCells;
        }

        public List<AdsReportWidget> getPrevCells() {
            return prevCells;
        }

        public double getPos() {
            return pos;
        }

        public boolean isHorizontal() {
            return isHorizontal;
        }

        public boolean canResize() {
            return canResize;
        }

        public void setCanResize(boolean canResize) {
            this.canResize = canResize;
        }

        @Override
        public int compareTo(Object o) {
            final Guide another = (Guide) o;
            return Double.compare(pos, another.pos);
        }

        public double findLastInPrev() {
            double result = pos;
            for (AdsReportWidget cell : prevCells) {
                final double bottom = isHorizontal ? cell.getLeftMm() + cell.getWidthMm() : cell.getTopMm() + cell.getHeightMm();
                result = Math.max(result, bottom);
            }
            return result;
        }

        public double findLastInPrevVisible() {
            double bottomResult = pos;
            if (!prevCells.isEmpty()) {
                for (AdsReportWidget cell : prevCells) {
                    if (cell instanceof AdsReportCell && ((AdsReportCell) cell).isVisible()) {
                        final double bottom = isHorizontal ? cell.getLeftMm() + cell.getWidthMm() : cell.getTopMm() + cell.getHeightMm();
                        bottomResult = Math.min(bottom, bottomResult);
                    }
                }
            }
            double topResult = -1;
            boolean isVisible = false;
            for (AdsReportWidget cell : prevCells) {
                final double top = isHorizontal ? cell.getLeftMm() : cell.getTopMm();
                final double bottom = isHorizontal ? cell.getLeftMm() + cell.getWidthMm() : cell.getTopMm() + cell.getHeightMm();
                bottomResult = Math.max(bottomResult, bottom);
                topResult = Math.max(topResult, top);
                if (!isVisible && (cell instanceof AdsReportCell) && ((AdsReportCell) cell).isVisible()) {
                    isVisible = true;
                }
            }
            return isVisible || prevCells.isEmpty() ? bottomResult : topResult;
        }

        public int getSnap() {
            return snap;
        }

        public void setSnap(int snap) {
            this.snap = snap;
        }
    }

    /* public void adjustCells(RadixObjects<AdsReportAbstractWidget> widgets,EReportLayout layout) throws ReportGenerationException {
     if(layout==EReportLayout.VERTICAL || layout==EReportLayout.GRID){
     final Guides guides = findVerticalGuides(widgets);
     for (AdsReportAbstractWidget widget : widgets) {
     adjustCellHeight(widget);
     }
     for (Guide guide : guides) {
     final double bottomInUpper = guide.findLastInPrev();
     for (AdsReportAbstractWidget cell : guide.prevCells) {
     cell.setHeightMm(bottomInUpper - cell.getTopMm());
     }
     double delta = (bottomInUpper - guide.lastInPrev);
     for (AdsReportAbstractWidget cell : guide.nextCells) {
     cell.setTopMm(cell.getTopMm() + delta);
     }
     }
     }
     if(layout==EReportLayout.HORIZONTAL || layout==EReportLayout.GRID){
     final Guides guides = findHorizontalGuides(widgets);
     for (AdsReportAbstractWidget cell : widgets) {
     adjustCellWidth(cell);
     }
     for (Guide guide : guides) {
     final double bottomInUpper = guide.findLastInPrev();
     for (AdsReportAbstractWidget cell : guide.prevCells) {
     cell.setHeightMm(bottomInUpper - cell.getLeftMm());
     }
     double delta = (bottomInUpper - guide.lastInPrev);
     for (AdsReportAbstractWidget cell : guide.nextCells) {
     cell.setTopMm(cell.getLeftMm() + delta);
     }
     }
     }
     }*/
}
