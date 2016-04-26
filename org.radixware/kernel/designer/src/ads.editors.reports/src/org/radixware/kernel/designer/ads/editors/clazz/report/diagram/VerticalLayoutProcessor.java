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
package org.radixware.kernel.designer.ads.editors.clazz.report.diagram;

import java.awt.Point;
import java.awt.Rectangle;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidget;
import org.radixware.kernel.common.enums.EReportLayout;

public class VerticalLayoutProcessor extends ReportLayoutProcessor {

    protected VerticalLayoutProcessor(final AdsReportBaseContainer widgetContainer) {
        super(widgetContainer);
    }

    @Override
    public void justifyLayout() {

        if (reportWidgetContainer.getDiagramMode() == AdsReportForm.Mode.GRAPHICS) {
            double height, width;//=reportWidgetContainer.getHeight();
            if (reportWidgetContainer.getCell() == null) {
                height = MmUtils.px2mm(reportWidgetContainer.getHeight());
                width = MmUtils.px2mm(reportWidgetContainer.getWidth());
            } else {
                height = reportWidgetContainer.getCell().getHeightMm();
                width = reportWidgetContainer.getCell().getWidthMm();
            }
            final int widgetCnt = getRowCount();
            if (widgetCnt > 0) {
                final double space = height / widgetCnt;
                for (AdsReportSelectableWidget cellWidget : reportWidgetContainer.getCellWidgets()) {
                    if (!cellWidget.isDragged()) {
                        final AdsReportWidget reportWidget = cellWidget.getCell();
                        final int index = cellWidget.getCell().getRow();
                        final int span = reportWidget.getRowSpan();
                        reportWidget.setLeftMm(0);
                        reportWidget.setHeightMm(/*MmUtils.px2mm(*/space * span);
                        reportWidget.setTopMm(/*MmUtils.px2mm(*/space * index);
                        reportWidget.setWidthMm(/*MmUtils.px2mm(reportWidgetContainer.getWidth())*/width);
                        if (cellWidget instanceof AdsReportBaseContainer) {
                            ((AdsReportBaseContainer) cellWidget).getReportLayout().justifyLayout();
                        }
                    }
                }
            }
        } else {
            int height, width;//=reportWidgetContainer.getHeight();
            if (reportWidgetContainer.getCell() == null) {
                height = TxtUtils.px2Rows(reportWidgetContainer.getHeight());
                width = TxtUtils.px2Columns(reportWidgetContainer.getWidth());
            } else {
                height = reportWidgetContainer.getCell().getHeightRows();
                width = reportWidgetContainer.getCell().getWidthCols();
            }
            final int widgetCnt = getRowCount();
            if (widgetCnt > 0) {
                final float space = (float) height / (float) widgetCnt;
                for (AdsReportSelectableWidget cellWidget : reportWidgetContainer.getCellWidgets()) {
                    if (!cellWidget.isDragged()) {
                        final AdsReportWidget reportWidget = cellWidget.getCell();
                        final int index = cellWidget.getCell().getRow();
                        final int span = reportWidget.getRowSpan();
                        reportWidget.setLeftMm(0);
                        reportWidget.setHeightRows(Math.round(space * span));
                        reportWidget.setTopRow(/*MmUtils.px2mm(*/Math.round(space * index));
                        reportWidget.setWidthCols(/*MmUtils.px2mm(reportWidgetContainer.getWidth())*/width);
                        if (cellWidget instanceof AdsReportBaseContainer) {
                            ((AdsReportBaseContainer) cellWidget).getReportLayout().justifyLayout();
                        }
                    }
                }
            }
        }
    }

    private int getRowCount() {
        int rowCount = 0;
        for (AdsReportSelectableWidget w : reportWidgetContainer.getCellWidgets()) {
            if (w != null) {
                final AdsReportWidget cell = w.getCell();
                if (cell.getRow() + cell.getRowSpan() - 1 > rowCount) {
                    rowCount = cell.getRow() + cell.getRowSpan() - 1;
                }
            }
        }
        rowCount = checkOnDraggedRows(rowCount + 1);
        return rowCount;
    }

    @Override
    void updateIndex(AdsReportSelectableWidget curWidget, InsertionInfo info) {
        if (info != null && curWidget != null) {
            int[] idx = info.getIdx();
            if (idx == null) {
                return;
            }
            APPEND_MODE mode = info.getMode();
            if (info.isAdding()) {
                add(curWidget, reportWidgetContainer);
            }
            int row = idx[1];
            shiftCells(curWidget, mode, 0, row, 1);
            /*if(mode!=APPEND_MODE.REPLACE){
             for(AdsReportSelectableWidget w:reportWidgetContainer.getCellWidgets()){
             AdsReportWidget cell=w.getCell();
             if(mode==APPEND_MODE.VERTICAL && cell.getRow()>=row ){
             cell.setRow(cell.getRow()+1);
             }
             }
             }*/
            curWidget.getCell().setRowSpan(1);
            curWidget.getCell().setRow(row);
            curWidget.getCell().setColumn(0);
        }
    }

    @Override
    InsertionInfo indexFromPoint(final Point p, final AdsReportSelectableWidget curcell) {
        if (p == null) {
            return null;
        }
        final int[] idx = new int[]{0, 0};
        final int size = getRowCount();
        if (size == 0) {
            return new InsertionInfo(idx, APPEND_MODE.VERTICAL, reportWidgetContainer);
        }
        if (reportWidgetContainer.getDiagramMode() == AdsReportForm.Mode.GRAPHICS) {
            double containerHeight;
            final double containerPos = curcell == null || reportWidgetContainer.getCell() == null ? 0 : reportWidgetContainer.getCell().getTopMm();
            if (reportWidgetContainer.getCell() == null) {
                containerHeight = MmUtils.px2mm(reportWidgetContainer.getHeight());
            } else {
                containerHeight = reportWidgetContainer.getCell().getHeightMm();
            }
            final double yMm = MmUtils.px2mm(p.y) - containerPos;
            Integer rowIndex = getIndexMM(yMm, containerHeight, size);
            if (rowIndex < 0) {
                if (curcell != null && curcell.getCell().getRow() >= 0) {
                    rowIndex = curcell.getCell().getRow();
                } else {
                    rowIndex = 0;
                }
            }
            final AdsReportSelectableWidget widget = findWidgetByIndex(rowIndex, curcell);
            final InsertionInfo info = checkOnAdd(curcell, widget, p);
            if (info != null) {
                info.setIsAdding(true);
                return info;
            }
            if (widget == null) {
                idx[1] = rowIndex.intValue();
                return new InsertionInfo(idx, APPEND_MODE.REPLACE, reportWidgetContainer);
            } else {
                rowIndex = widget.getCell().getRow();//for case when rowSpan>1
            }

            final double y = widget.getCell().getTopMm();//r.y-AdsReportSelectableWidget.IDENT; 
            final double height = widget.getCell().getHeightMm();//r.height+AdsReportSelectableWidget.IDENT*2;//учитываем отступ
            if (yMm <= y + height / 2 && yMm >= y) {
                idx[1] = rowIndex;
            } else if (yMm > y + height / 2 && yMm <= y + height) {
                idx[1] = rowIndex + widget.getCell().getRowSpan();
            }
            return new InsertionInfo(idx, APPEND_MODE.VERTICAL, reportWidgetContainer);
        } else {
            int containerHeight;
            final int containerPos = curcell == null || reportWidgetContainer.getCell() == null ? 0 : reportWidgetContainer.getCell().getTopRow();

            if (reportWidgetContainer.getCell() == null) {
                containerHeight = TxtUtils.px2Rows(reportWidgetContainer.getHeight());
            } else {
                containerHeight = reportWidgetContainer.getCell().getHeightRows();
            }
            final int yRows = TxtUtils.px2Rows(p.y) - containerPos;
            Integer rowIndex = getIndexTxt(yRows, containerHeight, size);
            if (rowIndex < 0) {
                if (curcell != null && curcell.getCell().getRow() >= 0) {
                    rowIndex = curcell.getCell().getRow();
                } else {
                    rowIndex = 0;
                }
            }
            final AdsReportSelectableWidget widget = findWidgetByIndex(rowIndex, curcell);
            final InsertionInfo info = checkOnAdd(curcell, widget, p);
            if (info != null) {
                info.setIsAdding(true);
                return info;
            }
            if (widget == null) {
                idx[1] = rowIndex.intValue();
                return new InsertionInfo(idx, APPEND_MODE.REPLACE, reportWidgetContainer);
            } else {
                rowIndex = widget.getCell().getRow();//for case when rowSpan>1
            }

            final int y = widget.getCell().getTopRow();//r.y-AdsReportSelectableWidget.IDENT; 
            final int height = widget.getCell().getHeightRows();//r.height+AdsReportSelectableWidget.IDENT*2;//учитываем отступ
            if (yRows <= y + height / 2 && yRows >= y) {
                idx[1] = rowIndex;
            } else if (yRows > y + height / 2 && yRows <= y + height) {
                idx[1] = rowIndex + widget.getCell().getRowSpan();
            }
            return new InsertionInfo(idx, APPEND_MODE.VERTICAL, reportWidgetContainer);
        }
    }

    private AdsReportSelectableWidget findWidgetByIndex(final int row, final AdsReportSelectableWidget curcell) {
        for (AdsReportSelectableWidget w : reportWidgetContainer.getCellWidgets()) {
            final AdsReportWidget cell = w.getCell();
            if (!w.equals(curcell) && (cell.getRow() <= row && cell.getRow() + cell.getRowSpan() > row)) {
                return w;
            }
        }
        return null;
    }

    @Override
    EReportLayout getType() {
        return EReportLayout.VERTICAL;
    }

    @Override
    public void highlightInsertPlace(final InsertionInfo info) {
        if (reportWidgetContainer.getDiagramMode() == AdsReportForm.Mode.GRAPHICS) {
            double height, width;
            if (reportWidgetContainer.getCell() == null) {
                height = MmUtils.px2mm(reportWidgetContainer.getHeight());
                width = MmUtils.px2mm(reportWidgetContainer.getWidth());
            } else {
                height = reportWidgetContainer.getCell().getHeightMm();
                width = reportWidgetContainer.getCell().getWidthMm();
            }

            final int[] idx = info.getIdx();
            final int rowCnt = getRowCount();
            double y = 0;
            if (rowCnt > 0) {
                double rowSpace = rowCnt > 0 ? height / rowCnt : height;
                y = MmUtils.snapToGrid(rowSpace * idx[1]);
            }
            int yPx = y == 0 ? 1 : MmUtils.mm2px(y);
            yPx = idx[1] == rowCnt ? yPx - 1 : yPx;
            height = rowCnt == 0 ? height : 0;
            final Rectangle rect = new Rectangle(0, yPx, MmUtils.mm2px(width), MmUtils.mm2px(height));
            reportWidgetContainer.setInsertionPlaceRect(rect);
        } else {
            int height, width;
            if (reportWidgetContainer.getCell() == null) {
                height = TxtUtils.px2Rows(reportWidgetContainer.getHeight());
                width = TxtUtils.px2Columns(reportWidgetContainer.getWidth());
            } else {
                height = reportWidgetContainer.getCell().getHeightRows();
                width = reportWidgetContainer.getCell().getWidthCols();
            }

            final int[] idx = info.getIdx();
            final int rowCnt = getRowCount();
            int y = 0;
            if (rowCnt > 0) {
                int rowSpace = rowCnt > 0 ? Math.round(height / rowCnt) : height;
                y = TxtUtils.snapToGrid(rowSpace * idx[1]);
            }
            int yPx = y == 0 ? 1 : TxtUtils.rows2Px(y);
            yPx = idx[1] == rowCnt ? yPx - 1 : yPx;
            height = rowCnt == 0 ? height : 0;
            final Rectangle rect = new Rectangle(0, yPx, TxtUtils.columns2Px(width), TxtUtils.rows2Px(height));
            reportWidgetContainer.setInsertionPlaceRect(rect);
        }
    }

    @Override
    void changeSpan(final AdsReportSelectableWidget cellWidget, final int oldWidth, final int oldHeight, final boolean isUp, final boolean isLeft) {
        changeVerticalSpan(cellWidget, oldHeight, isUp);
        justifyLayout();
    }

    @Override
    EReportLayout type() {
        return EReportLayout.VERTICAL;
    }
}