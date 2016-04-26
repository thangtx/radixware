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
package org.radixware.kernel.server.reports.txt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidget;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidgetContainer;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.IReportWidgetContainer;
import org.radixware.kernel.common.enums.EReportCellType;
import org.radixware.kernel.common.enums.EReportLayout;
import org.radixware.kernel.server.reports.ReportGenerationException;
import org.radixware.kernel.server.reports.fo.AdjustedCell;
import org.radixware.kernel.server.reports.fo.CellContents;
import org.radixware.kernel.server.reports.fo.HtmlParser;

class CellsAdjuster {
 
    private int calcCellWidthCols(final AdsReportCell cell) throws ReportGenerationException {
        //List<CellContents> cellContant = HtmlParser.getCellContant(cell);
        AdjustedCell adjustedCell = adjustedCellContents.get(cell);

        int lineWidthCol = 0, maxWidthCol = 0;
        //int line=0;
        for (int l = 0; l < adjustedCell.getLineCount(); l++) {
            List<CellContents> list = adjustedCell.getContentsByLine(l);
            for (int j = 0; j < list.size(); j++) {
                CellContents cont = list.get(j);
                String content = cont.getText();
                if (content != null) {
                    int prevPos = 0;
                    for (int i = 0; i < content.length(); i++) {
                        final char c = content.charAt(i);
                        if (c == '\n') {
                            lineWidthCol = 0;
                            maxWidthCol = Math.max(maxWidthCol, lineWidthCol);

                            cont.setText(new String(content.substring(prevPos, i + 1)));
                            splitContent(adjustedCell, new int[]{i + 1, j}, l);
                            prevPos = i + 1;
                            //break;
                        } else {
                            lineWidthCol++;
                        }
                    }
                }
            }
        }
        return lineWidthCol;
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
            int lineWidthCol = 0, wordWidthCol = 0;
            int maxWidthCol = cell.getWidthCols() - (cell.getMarginLeftCols() + cell.getMarginRightCols());
            int insPos[] = new int[]{0, 0};
            for (int j = 0; j < list.size(); j++) {//cycle by cell contants                
                CellContents content = list.get(j);
                boolean inQuotes = false;

                String text = content.getText();
                if (text != null) {
                    for (int i = 0; i < text.length(); i++) {
                        final char c = text.charAt(i);
                        if (c == '\n') {
                            i++;
                            insPos[0] = i;
                            insPos[1] = j;
                            splitContent(adjustedCell, insPos, l);
                            lineWidthCol = 0;
                            wordWidthCol = 0;
                            insPos[0] = 0;
                            break;
                        } else {
                            int charWidthCol = 1;
                            if (lineWidthCol > 0 && lineWidthCol + charWidthCol > maxWidthCol && c != ' ') {
                                if (insPos[0] == 0) {
                                    insPos[0] = i;
                                    insPos[1] = j;
                                    wordWidthCol = 0;
                                }                                
                                splitContent(adjustedCell, insPos, l);
                                i++;
                                insPos[0] = 0;
                                lineWidthCol = wordWidthCol;
                                break;
                            }

                            lineWidthCol += charWidthCol;

                            if (c == '(' || c == '{' || c == '[' || c == '$' || (c == '"' && !inQuotes)) {
                                wordWidthCol = charWidthCol;
                                insPos[0] = i;
                                insPos[1] = j;
                            } else if (c == ' ' || c == ';' /*|| c == ',' || c == ':' || c == '.'*/ || c == ')' || c == ']' || c == '}' || c == '!' || c == '?' || c == '%' || (c == '"' && inQuotes) || c == '-') {
                                wordWidthCol = 0;
                                insPos[0] = i + 1;
                                insPos[1] = j;
                            } else if ((c == ',' || c == ':' || c == '.') && (!isNumber(text, i + 1))) {
                                wordWidthCol = 0;
                                insPos[0] = i + 1;
                                insPos[1] = j;
                            } else {
                                wordWidthCol += charWidthCol;
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

    private void splitContent(AdjustedCell adjustedCell, int[] insPos, int line) {
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
                cell.setTopRow(cell.getTopRow() + widget.getTopRow());
                cell.setLeftColumn(cell.getLeftColumn() + widget.getLeftColumn());
                adjustCellsPosition(cell);
            }
        }
        //return needAdjustWidth;
    }

    public int adjustCellsHeight(final IReportWidgetContainer band) throws ReportGenerationException {
        RadixObjects<AdsReportWidget> widgets = band.getWidgets();
        final Guides guides = findVerticalGuides(widgets.list());
        int height;
        for (AdsReportWidget widget : widgets) {
            if (widget.isReportContainer()) {
                height = adjustCellsHeight((IReportWidgetContainer) widget);
                if (height != 0) {
                    widget.setHeightRows(height);
                }
            } else {
                AdsReportCell cell = (AdsReportCell) widget;
                if (cell.isVisible()) {
                    adjustCellHeight(cell);
                } else {
                    widget.setHeightRows(0);
                }
            }
        }

        height = resizeWidgetsHeight(band, guides);
        for (AdsReportWidget widget : widgets) {
            if (widget.isReportContainer()) {
                resizeContainerHeight((AdsReportWidgetContainer) widget, ((AdsReportWidgetContainer) widget).getHeightRows());
            }
        }
        return height;
    }

    private int resizeWidgetsHeight(IReportWidgetContainer widgetContainter, final Guides guides) throws ReportGenerationException {
        int height = 0;
        for (Guide guide : guides) {
            final int bottomInUpper = guide.findLastInPrevVisible();
            for (AdsReportWidget cell : guide.prevCells) {
                cell.setHeightRows(bottomInUpper - cell.getTopRow());
            }
            int delta = (bottomInUpper - guide.lastInPrev);
            if (delta != 0) {
                for (AdsReportWidget cell : guide.nextCells) {
                    setCellTopPosition(cell, delta);
                }
            }
            height = bottomInUpper;
        }
        if (widgetContainter instanceof AdsReportWidgetContainer) {
            height = height - ((AdsReportWidgetContainer) widgetContainter).getTopRow();
        }
        return height;
    }

    private void setCellTopPosition(AdsReportWidget widget, int delta) {
        widget.setTopRow(widget.getTopRow() + delta);
        if (widget.isReportContainer()) {
            IReportWidgetContainer container = (IReportWidgetContainer) widget;
            for (AdsReportWidget childWidget : container.getWidgets()) {
                setCellTopPosition(childWidget, delta);
            }
        }
    }

    private void resizeContainerHeight(final IReportWidgetContainer container, int height) throws ReportGenerationException {
        final Guides guides = findVerticalGuides(container.getWidgets().list());
        int lastPos = guides.get(guides.size() - 1).pos - guides.get(0).pos;
        int delta = height - lastPos;
        if (delta > 0) {
            resizeCellsHeight(container, delta);
        } else {
            for (AdsReportWidget widget : container.getWidgets()) {
                if (widget.isReportContainer()) {
                    resizeContainerHeight((AdsReportWidgetContainer) widget, ((AdsReportWidgetContainer) widget).getHeightRows());
                }
            }
        }
    }

    public void adjustCellsWidth(final AdsReportBand band) throws ReportGenerationException {
        if (isNeedResizeToMin(band.getWidgets())) {
            int width = calcMinWidth(band);//приводим к min размерам
            for (AdsReportWidget w : band.getWidgets()) {//приводим размеры контейнеров в соответствие с содержимым
                if (w.isReportContainer()) {
                    resizeContainerWidth((AdsReportWidgetContainer) w, ((AdsReportWidgetContainer) w).getWidthCols());
                }
            }
            int pageWidth = band.getOwnerForm().getPageWidthCols() - band.getOwnerForm().getMargin().getLeftCols() - band.getOwnerForm().getMargin().getRightCols();
            if (width != pageWidth && width != 0.0) {
                int delta = pageWidth - width;
                resizeCellsWidth(band, delta, false);
            }
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

    private void resizeContainerWidth(final IReportWidgetContainer container, int width) throws ReportGenerationException {
        final Guides guides = findHorizontalGuides(container.getWidgets().list());
        int lastPos = guides.get(guides.size() - 1).pos - guides.get(0).pos;
        int delta = width - lastPos;
        if (delta > 0) {
            resizeCellsWidth(container, delta, true);
        } else {
            for (AdsReportWidget widget : container.getWidgets()) {
                if (widget.isReportContainer()) {
                    resizeContainerWidth((AdsReportWidgetContainer) widget, ((AdsReportWidgetContainer) widget).getWidthCols());
                }
            }
        }
    }

    private int calcMinWidth(IReportWidgetContainer container) throws ReportGenerationException {
        final Guides guides = findHorizontalGuides(container.getWidgets().list());
        resizeToMinimalWidth(container.getWidgets());
        return calcMinContainerWidth(container, guides);
    }

    private void resizeToMinimalWidth(RadixObjects<AdsReportWidget> widgets) throws ReportGenerationException {
        for (AdsReportWidget widget : widgets) {
            if (widget.isReportContainer()) {
                AdsReportWidgetContainer container = (AdsReportWidgetContainer) widget;
                int width = calcMinWidth(container);
                if (container.getOwnerWidget() != null && container.getOwnerWidget().getLayout() == EReportLayout.FREE) {
                    if (width != container.getWidthCols() && width != 0.0) {
                        int delta = container.getWidthCols() - width;
                        resizeCellsWidth(container, delta, false);
                    }
                } else {
                    if (width != 0) {
                        container.setWidthCols(width);
                    }
                }
            }
        }
    }

    private int calcMinContainerWidth(IReportWidgetContainer widgetContainter, final Guides guides) throws ReportGenerationException {
        int width = 0;
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
                width = Math.max(width, cell.getWidthCols());
            }
            for (AdsReportWidget cell : widgets) {
                cell.setWidthCols(width);
            }
        } else if (widgetContainter instanceof AdsReportWidgetContainer) {
            width = ((AdsReportWidgetContainer) widgetContainter).getWidthCols();
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

    private int resizeWidgetsWidth(final Guides guides) throws ReportGenerationException {
        int width = 0;
        for (Guide guide : guides) {
            int bottomInUpper = guide.findLastInPrevVisible();
            for (AdsReportWidget cell : guide.prevCells) {
                cell.setWidthCols(bottomInUpper - cell.getLeftColumn());
            }
            int delta = (bottomInUpper - guide.lastInPrev);
            if (delta != 0) {
                for (AdsReportWidget cell : guide.nextCells) {
                    setCellLeftPosition(cell, delta);
                }
            }
            width = bottomInUpper;
        }
        return width;
    }

    private int resizeWidgetsWidth(IReportWidgetContainer widgetContainter, final Guides guides) throws ReportGenerationException {
        int width = resizeWidgetsWidth(guides);//0.0
        //resizeWidgetsWidth(guides);
        if (widgetContainter instanceof AdsReportWidgetContainer) {
            width = width - ((AdsReportWidgetContainer) widgetContainter).getLeftColumn();
        }
        return width;
    }

    private void setCellLeftPosition(AdsReportWidget widget, int delta) {
        widget.setLeftColumn(widget.getLeftColumn() + delta);
        if (widget.isReportContainer()) {
            IReportWidgetContainer container = (IReportWidgetContainer) widget;
            for (AdsReportWidget childWidget : container.getWidgets()) {
                setCellLeftPosition(childWidget, delta);
            }
        }
    }

    public void resizeCellsWidth(IReportWidgetContainer widgetContainter, int delta, boolean resizeInAnyCase) throws ReportGenerationException {
        if (delta == 0) {
            return;/* 0.0;*/

        }
        final Guides guides = findHorizontalGuides(widgetContainter.getWidgets().list());
        int n = resizeInAnyCase ? guides.size() - 1 : getResizableGuidesCnt(guides);
        if (n > 0) {
            int addingSpace = Math.round((float) delta / n);
            for (AdsReportWidget cell : widgetContainter.getWidgets()) {
                if (cell.isReportContainer() && !cell.isAdjustWidth()) {
                    cell.setWidthCols(cell.getWidthCols() + addingSpace * cell.getColumnSpan());
                    resizeCellsWidth((IReportWidgetContainer) cell, addingSpace * cell.getColumnSpan(), resizeInAnyCase);
                } else {
                    Guide g = guides.findOrCreate(cell.getLeftColumn() + cell.getWidthCols(), true);
                    if (resizeInAnyCase || g.canResize()) {
                        cell.setWidthCols(cell.getWidthCols() + addingSpace * cell.getColumnSpan());
                    }
                    //resizeCellWidth((AdsReportCell)cell,addingSpace);
                }
            }
            resizeWidgetsWidth(widgetContainter, guides);
        }
    }

    public void resizeCellsHeight(IReportWidgetContainer widgetContainter, int delta/*,boolean resizeInAnyCase*/) throws ReportGenerationException {
        if (delta == 0) {
            return;/* 0.0;*/

        }
        final Guides guides = findVerticalGuides(widgetContainter.getWidgets().list());
        int n =/*resizeInAnyCase ? guides.size()-1 :*/ getResizableGuidesCnt(guides);
        if (n > 0) {
            int addingSpace = Math.round((float) delta / n);
            for (AdsReportWidget cell : widgetContainter.getWidgets()) {
                if (cell.isReportContainer() && !cell.isAdjustHeight()) {
                    cell.setHeightRows(cell.getHeightRows() + addingSpace);
                    resizeCellsHeight((IReportWidgetContainer) cell, addingSpace/*,resizeInAnyCase*/);
                } else {
                    Guide g = guides.findOrCreate(cell.getTopRow() + cell.getHeightRows(), true);
                    if (/*resizeInAnyCase || */g.canResize()) {
                        cell.setHeightRows(cell.getHeightRows() + addingSpace);
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
            cell.setHeightRows(0);
        } else if (cell.isAdjustHeight()) {
            AdjustedCell adjustedCell = adjustedCellContents.get(cell);
            int contentHeightRows = 0;
            for (int i = 0; i < adjustedCell.getLineCount(); i++) {
                contentHeightRows++;
            }
            cell.setHeightRows(cell.getMarginTopRows() + cell.getMarginBottomRows() + contentHeightRows);
        }
    }

    private boolean adjustCellWidth(final AdsReportWidget widget) throws ReportGenerationException {
        if (!widget.isReportContainer()) {
            AdsReportCell cell = (AdsReportCell) widget;
            if (cell.isAdjustWidth()) {
                cell.setWidthCols(cell.getMarginLeftCols() + cell.getMarginRightCols() + calcCellWidthCols(cell));
                return true;
            } else {
                if (cell.getCellType() == EReportCellType.CHART && ((AdsReportChartCell) cell).isPlotMinSizeEnable()) {
                    cell.setWidthCols(0);
                }
                return false;
            }
        }
        return false;
    }

    public static Guides findVerticalGuides(List<AdsReportWidget> widgets) {
        final Guides guides = new Guides();
        for (AdsReportWidget cell : widgets) {
            final int top = cell.getTopRow();
            Guide guide = guides.findOrCreate(top, false);
            guide.nextCells.add(cell);

            final int bottom = cell.getTopRow() + cell.getHeightRows();
            if (bottom != top) {
                guide = guides.findOrCreate(bottom, false);
                guide.prevCells.add(cell);
            }

            if (guide.canResize && cell.isAdjustHeight()) {
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
            final int left = widget.getLeftColumn();
            Guide guide = guides.findOrCreate(left, true);
            guide.nextCells.add(widget);

            final int right = widget.getLeftColumn() + widget.getWidthCols();
            if (right != left) {
                guide = guides.findOrCreate(right, true);
                guide.prevCells.add(widget);
            }
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

        public Guide findOrCreate(int top, boolean isHorizontal) {
            for (Guide guide : this) {
                if (guide.pos == top) {
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
        private final int pos;
        private int lastInPrev = 0;
        private final boolean isHorizontal;
        private boolean canResize = true;
        private int snap = 1;

        public Guide(int pos, boolean isHorizontal) {
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

        public int findLastInPrev() {
            int result = pos;
            for (AdsReportWidget cell : prevCells) {
                final int bottom = (isHorizontal ? cell.getLeftColumn() + cell.getWidthCols() : cell.getTopRow() + cell.getHeightRows());
                result = Math.max(result, bottom);
            }
            return result;
        }

        public int findLastInPrevVisible() {
            int bottomResult = pos;
            if (!prevCells.isEmpty()) {
                for (AdsReportWidget cell : prevCells) {
                    if (cell instanceof AdsReportCell && ((AdsReportCell) cell).isVisible()) {
                        final int bottom = (isHorizontal ? cell.getLeftColumn() + cell.getWidthCols() : cell.getTopRow() + cell.getHeightRows());
                        bottomResult = Math.min(bottom, bottomResult);
                    }
                }
            }
            int topResult = -1;
            boolean isVisible = false;
            for (AdsReportWidget cell : prevCells) {
                final int top = isHorizontal ? cell.getLeftColumn() : cell.getTopRow();
                final int bottom = (isHorizontal ? cell.getLeftColumn() + cell.getWidthCols() : cell.getTopRow() + cell.getHeightRows());
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
}
