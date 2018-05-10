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

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;

/**
 * Class which represent a line of the report cells, which are snapped to each
 * other by the left border. The coordinate of the left border for each cell is
 * the sum of the widths of the previous cells. If the cell in the report is
 * located on another line, then before the addition of this cell, the
 * {@link org.radixware.kernel.server.reports.AbstractReportCellsLine#wrap() wrap}
 * method must be called.
 */
public class ReportCellsLine {

    private static final boolean IS_CONSIDER_PADDINGS = true;

    private final List<WrapPoint> wrapPoints = new ArrayList<>();
    private final List<ReportCellsLineElement> elements = new ArrayList<>();

    private double endPoint;
    
    public ReportCellsLine() {        
    }
    
    public ReportCellsLine(ReportCellsLine source) {
        for (ReportCellsLineElement element : source.elements) {
            this.elements.add(new ReportCellsLineElement(element.left, element.top, element.width, element.cell, element.padding));            
        }
            
        for (WrapPoint wrapPoint : source.wrapPoints) {
            this.wrapPoints.add(new WrapPoint(wrapPoint.point, wrapPoint.padding));
        }
            
        this.endPoint = source.endPoint;        
    }

    public List<AdsReportCell> getCells() {
        List<AdsReportCell> result = new ArrayList<>();
        for (ReportCellsLineElement element : elements) {
            result.add(element.getCell().cell);
        }

        return result;
    }

    public void addCell(CellWrapper cell) {
        ReportCellsLineElement element;
        boolean isNeedWrap = false;

        if (elements.isEmpty()) {
            element = new ReportCellsLineElement(cell.getLeft(), cell.getTop(), cell.getWidth(), cell, 0.);
        } else {
            ReportCellsLineElement prevElement = elements.get(elements.size() - 1);
            double padding = 0.;

            if (prevElement.getCell().getLeft() >= cell.getLeft()) {
                isNeedWrap = true;
            } else {
                padding = IS_CONSIDER_PADDINGS ? cell.getLeft() - (prevElement.getCell().getLeft() + prevElement.getCell().getWidth()) : 0.;
            }

            element = new ReportCellsLineElement(prevElement.getLeft() + prevElement.getWidth(), cell.getTop(), cell.getWidth(), cell, padding);
        }

        elements.add(element);
        if (isNeedWrap) {
            wrap();
        }

        endPoint = calcElementsWidth(elements);
    }

    public boolean removeCell(AdsReportCell cell) {
        int destCellIndex = indexOf(cell);
        if (destCellIndex == -1) {
            return false;
        }

        List<ReportCellsLineElement> newElements = new ArrayList<>();

        for (int i = 0; i < destCellIndex; i++) {
            newElements.add(elements.get(i));
        }

        boolean isFirst = true;
        for (int i = destCellIndex + 1; i < elements.size(); i++) {
            ReportCellsLineElement prevElement = isFirst ? elements.get(i - 1) : newElements.get(newElements.size() - 1);
            ReportCellsLineElement curElement = elements.get(i);

            double left = isFirst ? prevElement.getLeft() : prevElement.getLeft() + prevElement.getWidth();
            double top = curElement.getTop();
            double width = curElement.getWidth();
            double padding = curElement.getPadding();

            isFirst = false;

            for (WrapPoint wrapPoint : wrapPoints) {
                if (curElement.getLeft() == wrapPoint.point && !curElement.cell.cell.isChangeTopOnMoving()) {
                    left = curElement.getLeft();
                    break;
                }
                
                double paddingsWidth = getPaddingsWidthToWrapPoint(newElements, wrapPoint);                
                if ((left + paddingsWidth < wrapPoint.point) && (left + width + padding > wrapPoint.point + paddingsWidth)) {
                    left = wrapPoint.point;
                    padding = 0.;
                    break;
                }
            }

            newElements.add(new ReportCellsLineElement(left, top, width, curElement.getCell(), padding));
        }

        for (int i = destCellIndex; i < newElements.size(); i++) {
            ReportCellsLineElement curElement = newElements.get(i);
            ReportCellsLineElement prevElement = i == 0 ? elements.get(i) : newElements.get(i - 1);

            CellWrapper curCell = curElement.getCell();
            WrapPoint curWrapPoint = getLeftWrapPoint(curElement);
            WrapPoint oldWrapPoint = getLeftWrapPoint(elements.get(i + 1));

            double newLeft = (curWrapPoint == null ? curElement.getLeft() : (curElement.getLeft() - curWrapPoint.point + curWrapPoint.padding)) + curElement.getPadding();
            double newTop = elements.get(i).getTop();

            if (curWrapPoint != null) {
                if (isElementOnWrapPoint(curElement, curWrapPoint)) {
                    if (isElementOnWrapPoint(elements.get(i + 1), curWrapPoint)) {
                        continue;
                    } else {
                        ReportCellsLineElement prevWrapPointElement = findWrapPointElement(elements, curWrapPoint);
                        newTop = prevWrapPointElement.getTop();
                    }
                }
            }

            if (curWrapPoint != oldWrapPoint) {
                WrapPoint prevOldWrapPoint = getLeftWrapPoint(elements.get(i));
                if (prevOldWrapPoint == oldWrapPoint) {
                    newTop = prevElement.getTop();
                }
            }

            curCell.setLeft(newLeft);
            
            if (curCell.cell.isChangeTopOnMoving()) {
                curCell.setTop(newTop);

                curElement.setTop(newTop);
            }
        }

        elements.clear();
        for (ReportCellsLineElement element : newElements) {
            elements.add(element);
        }

        return true;
    }

    public void adjustElementsWidth() {
        WrapPoint prevPoint = null;
        for (WrapPoint curPoint : wrapPoints) {
            adjustElementsWidth(getElementsBetweenWrapPoints(prevPoint, curPoint), curPoint.point);
            prevPoint = curPoint;
        }
        adjustElementsWidth(getElementsBetweenWrapPoints(prevPoint, null), endPoint);
    }

    private void adjustElementsWidth(List<ReportCellsLineElement> elements, double targetWidth) {
        if (elements.isEmpty()) {
            return;
        }

        double currentWidth = calcElementsWidth(elements);

        ReportCellsLineElement prevElement = null;
        for (ReportCellsLineElement element : elements) {
            double elementFraction = element.getWidth() / currentWidth;
            double newWidth = targetWidth * elementFraction;

            CellWrapper cell = element.getCell();
            cell.setWidth(newWidth);
            element.setWidth(cell.getWidth()); // сделано для txt отчетов (newWidth может быть вещественным)
            if (prevElement != null) {
                element.setLeft(prevElement.getLeft() + prevElement.getWidth());

                WrapPoint curWrapPoint = getLeftWrapPoint(element);
                double newLeft = (curWrapPoint == null ? element.getLeft() : (element.getLeft() - curWrapPoint.point + curWrapPoint.padding)) + element.getPadding();
                element.getCell().setLeft(newLeft);
            }

            prevElement = element;
        }
    }

    public void applyManualWidth() {
        ReportCellsLineElement prevElement = null;
        for (ReportCellsLineElement element : elements) {
            CellWrapper cell = element.getCell();
            cell.applyWidthSettings();
            element.setWidth(cell.getWidth());
            if (prevElement != null) {
                WrapPoint curWrapPoint = getLeftWrapPoint(element);                
                if (curWrapPoint != null && isElementOnWrapPoint(element, curWrapPoint)) {
                    int index = wrapPoints.indexOf(curWrapPoint);
                    WrapPoint newWrapPoint = new WrapPoint(prevElement.getLeft() + prevElement.getWidth(), curWrapPoint.padding);
                    
                    wrapPoints.remove(curWrapPoint);
                    wrapPoints.add(index, newWrapPoint);
                    
                    curWrapPoint = newWrapPoint;
                }
                
                element.setLeft(prevElement.getLeft() + prevElement.getWidth());
                double newLeft = (curWrapPoint == null ? element.getLeft() : (element.getLeft() - curWrapPoint.point + curWrapPoint.padding)) + element.getPadding();
                element.getCell().setLeft(newLeft);               
            }

            prevElement = element;
        }
    }

    public boolean containsCell(AdsReportCell cell) {
        for (ReportCellsLineElement element : elements) {
            if (element.getCell().cell == cell) {
                return true;
            }
        }

        return false;
    }

    public int indexOf(AdsReportCell cell) {
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).getCell().cell == cell) {
                return i;
            }
        }

        return -1;
    }

    private WrapPoint wrap() {
        if (elements.isEmpty() || elements.size() == 1) {
            return null;
        } else {
            ReportCellsLineElement oldLastElement = elements.get(elements.size() - 2);

            CellWrapper lastCell = elements.get(elements.size() - 1).getCell();

            double point = oldLastElement.getLeft() + oldLastElement.getWidth();
            double padding = lastCell.getLeft();

            WrapPoint result = new WrapPoint(point, padding);

            wrapPoints.add(result);

            return result;
        }
    }

    private WrapPoint getLeftWrapPoint(ReportCellsLineElement element) {
        for (WrapPoint wrapPoint : wrapPoints) {
            if (wrapPoint.point <= element.getLeft()) {
                return wrapPoint;
            }
        }
        return null;
    }

    private boolean isElementOnWrapPoint(ReportCellsLineElement element, WrapPoint wrapPoint) {
        return element.getLeft() == wrapPoint.point;
    }

    private ReportCellsLineElement findWrapPointElement(List<ReportCellsLineElement> elements, WrapPoint wrapPoint) {
        for (ReportCellsLineElement element : elements) {
            if (element.getLeft() == wrapPoint.point) {
                return element;
            }
        }

        return null;
    }

    private double getPaddingsWidthToWrapPoint(List<ReportCellsLineElement> elements, WrapPoint wrapPoint) {
        if (!IS_CONSIDER_PADDINGS) {
            return 0.;
        }

        double result = 0.;
        for (ReportCellsLineElement element : elements) {
            if (element.getLeft() < wrapPoint.point) {
                result += element.getPadding();
            }
        }

        return result;
    }

    private List<ReportCellsLineElement> getElementsBetweenWrapPoints(WrapPoint from, WrapPoint to) {
        List<ReportCellsLineElement> result = new ArrayList<>();
        if (elements.isEmpty()) {
            return result;
        }

        int startIndex = from == null ? 0 : elements.indexOf(findWrapPointElement(elements, from));
        int endIndex = to == null ? elements.size() : elements.indexOf(findWrapPointElement(elements, to));               

        for (int i = startIndex == -1 ? elements.size() : startIndex; i < (endIndex == -1 ? elements.size() : endIndex); i++) {
            result.add(elements.get(i));
        }

        return result;
    }

    private double calcElementsWidth(List<ReportCellsLineElement> elements) {
        double result = 0;

        for (ReportCellsLineElement element : elements) {
            result += element.getWidth();
        }

        return result;
    }

    /**
     * A basic element of the
     * {@link org.radixware.kernel.server.reports.ReportCellsLine AbstractReportCellsLine}
     * which based on
     * {@link org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell AdsReportCell}.
     * It is used to store cell coordinates relative to the total length of the
     * line (without wraps).
     */
    private static class ReportCellsLineElement {

        private final CellWrapper cell;
        private final double padding;

        private double left;
        private double top;
        private double width;

        public ReportCellsLineElement(double left, double top, double width, CellWrapper cell, double padding) {
            this.left = left;
            this.top = top;
            this.width = width;
            this.cell = cell;
            this.padding = padding;
        }

        public double getLeft() {
            return left;
        }

        public void setLeft(double left) {
            this.left = left;
        }

        public double getTop() {
            return top;
        }

        public void setTop(double top) {
            this.top = top;
        }

        public double getWidth() {
            return width;
        }

        public void setWidth(double width) {
            this.width = width;
        }

        public CellWrapper getCell() {
            return cell;
        }

        public double getPadding() {
            return padding;
        }
    }

    private static class WrapPoint {

        public final double point;
        public final double padding;

        public WrapPoint(double point, double padding) {
            this.point = point;
            this.padding = padding;
        }
    }
}
