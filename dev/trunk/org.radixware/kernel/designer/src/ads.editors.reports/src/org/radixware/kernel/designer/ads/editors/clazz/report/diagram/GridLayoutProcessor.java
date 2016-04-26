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
import java.util.Collection;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidget;
import org.radixware.kernel.common.enums.EReportLayout;

public class GridLayoutProcessor extends ReportLayoutProcessor {

    GridLayoutProcessor(final AdsReportBaseContainer widgetContainer) {
        super(widgetContainer);
    }

    @Override
    public void justifyLayout() {
        double height, width;
        if (reportWidgetContainer.getCell() == null) {
            height = MmUtils.px2mm(reportWidgetContainer.getHeight());
            width = MmUtils.px2mm(reportWidgetContainer.getWidth());
        } else {
            height = reportWidgetContainer.getCell().getHeightMm();
            width = reportWidgetContainer.getCell().getWidthMm();
        }
        final int[] grigSize = getGridSize();
        final int columnCnt = grigSize[0];
        final int rowCnt = grigSize[1];
        if (columnCnt > 0 || rowCnt > 0) {
            final double rowSpace = rowCnt > 0 ? height / rowCnt : height;
            final double columnSpace = columnCnt > 0 ? width / columnCnt : width;
            for (AdsReportSelectableWidget cellWidget : reportWidgetContainer.getCellWidgets()) {
                if (!cellWidget.isDragged()) {
                    final AdsReportWidget cell = cellWidget.getCell();
                    final int y = cell.getRow();
                    final int x = cell.getColumn();
                    final int columnSpan = cell.getColumnSpan();
                    final int rowSpan = cell.getRowSpan();
                    cell.setLeftMm(columnSpace * x);
                    cell.setHeightMm(rowSpace * rowSpan);
                    cell.setTopMm(rowSpace * y);
                    cell.setWidthMm(columnSpace * columnSpan);
                    if (cellWidget instanceof AdsReportBaseContainer) {
                        ((AdsReportBaseContainer) cellWidget).getReportLayout().justifyLayout();
                    }
                }
            }
        }
    }

    private int[] getGridSize() {
        int rowCount = 0, columnCount = 0;
        for (AdsReportSelectableWidget w : reportWidgetContainer.getCellWidgets()) {
            if (w != null) {
                final AdsReportWidget cell = w.getCell();
                if ((cell.getRow() + cell.getRowSpan() - 1) > rowCount) {
                    rowCount = cell.getRow() + cell.getRowSpan() - 1;
                }
                if (cell.getColumn() + cell.getColumnSpan() - 1 > columnCount) {
                    columnCount = cell.getColumn() + cell.getColumnSpan() - 1;
                }
            }
        }
        rowCount = checkOnDraggedRows(rowCount + 1);
        columnCount = checkOnDraggedColumns(columnCount + 1);
        if (rowCount <= 0 && columnCount <= 0 && reportWidgetContainer.getCellWidgets().size() > 0) {
            columnCount = reportWidgetContainer.getCellWidgets().size();
        }
        return new int[]{columnCount, rowCount};
    }

    @Override
    void updateIndex(final AdsReportSelectableWidget widget, final InsertionInfo info) {
        if (info != null && widget != null) {
            final int[] idx = info.getIdx();
            if (idx == null) {
                return;
            }
            final APPEND_MODE mode = info.getMode();
            if (info.isAdding()) {
                add(widget, reportWidgetContainer);
            }
            final int row = idx[1];
            final int column = idx[0];
            shiftCells(widget, mode, column, row, 1);
            widget.getCell().setRowSpan(1);
            widget.getCell().setColumnSpan(1);
            widget.getCell().setRow(row);
            widget.getCell().setColumn(column);
        }
    }

    @Override
    void changeSpan(final AdsReportSelectableWidget cellWidget, final int oldWidth, final int oldHeight, final boolean isUp, final boolean isLeft) {
        changeVerticalSpan(cellWidget, oldHeight, isUp);
        changeHorizontalSpan(cellWidget, oldWidth, isLeft);
        justifyLayout();
    }

    @Override
    InsertionInfo indexFromPoint(final Point p, final AdsReportSelectableWidget curcell) {
        if (p == null) {
            return null;
        }
        final int[] idx = new int[]{0, 0};
        final Collection<AdsReportSelectableWidget> cellWidgets = reportWidgetContainer.getCellWidgets();
        final int size = cellWidgets.size();
        if (size == 0) {
            return new InsertionInfo(idx, APPEND_MODE.HORIZONTAL, reportWidgetContainer);
        }

        final int[] grigSize = getGridSize();
        final int columnCnt = grigSize[0], rowCnt = grigSize[1];
        //List<Size> columnsSize=getColumnsSize( columnCnt,curcell);
        //List<Size> rowsSize=getRowsSize(rowCnt,curcell);
        if (reportWidgetContainer.getDiagramMode() == AdsReportForm.Mode.GRAPHICS) {
            double height, width;
            final double top = curcell == null || reportWidgetContainer.getCell() == null ? 0 : reportWidgetContainer.getCell().getTopMm();
            final double left = curcell == null || reportWidgetContainer.getCell() == null ? 0 : reportWidgetContainer.getCell().getLeftMm();
            if (reportWidgetContainer.getCell() == null) {
                height = MmUtils.px2mm(reportWidgetContainer.getHeight());
                width = MmUtils.px2mm(reportWidgetContainer.getWidth());
            } else {
                height = reportWidgetContainer.getCell().getHeightMm();
                width = reportWidgetContainer.getCell().getWidthMm();
            }
            final double xMm = MmUtils.px2mm(p.x);
            final double yMm = MmUtils.px2mm(p.y);
            Integer columnIndex = getIndexMM(xMm - left, width, columnCnt);
            //Integer columnIndex=getIndex(columnsSize,  p.x);
            if (columnIndex < 0) {
                if (curcell != null && curcell.getCell().getColumn() >= 0) {
                    columnIndex = curcell.getCell().getColumn();
                } else {
                    columnIndex = 0;
                }
            }
            Integer rowIndex = getIndexMM(yMm - top, height, rowCnt);
            //Integer rowIndex=getIndex(rowsSize,  p.y);
            if (rowIndex < 0) {
                if (curcell != null && curcell.getCell().getRow() >= 0) {
                    rowIndex = curcell.getCell().getRow();
                } else {
                    rowIndex = 0;
                }
            }
            idx[0] = columnIndex.intValue();
            idx[1] = rowIndex.intValue();

            AdsReportSelectableWidget widget = findWidgetByIndex(idx[0], idx[1], curcell);
            final InsertionInfo info = checkOnAdd(curcell, widget, p);
            if (info != null) {
                //info.setMode(APPEND_MODE.ADD);
                info.setIsAdding(true);
                return info;
            }
            if (widget == null) {
                //return APPEND_MODE.REPLACE;
                return new InsertionInfo(idx, APPEND_MODE.REPLACE, reportWidgetContainer);
            }
            final Rectangle r = widget.getBounds();
            r.y = r.y - AdsReportSelectableWidget.IDENT;
            r.x = r.x - AdsReportSelectableWidget.IDENT;//учитываем отступ
            r.height = r.height + AdsReportSelectableWidget.IDENT * 2;
            r.width = r.width + AdsReportSelectableWidget.IDENT * 2;//учитываем отступ
            double a = ((double) r.height) / r.width;
            double b = r.y - a * r.x;
            double y1 = a * p.x + b;

            a = -((double) r.height) / r.width;
            b = r.y + r.height - a * r.x;
            double y2 = p.x * a + b;
            APPEND_MODE mode;
            int pointY = curcell == null ? p.y : p.y - reportWidgetContainer.getY();
            if (pointY > y1 && pointY > y2) {
                idx[1] = widget.getCell().getRow() + widget.getCell().getRowSpan();
                widget = findWidgetByIndex(idx[0], idx[1], curcell);
                mode = widget == null && idx[1] < rowCnt ? APPEND_MODE.REPLACE : APPEND_MODE.VERTICAL;
            } else if (pointY <= y1 && pointY <= y2) {
                idx[1] = widget.getCell().getRow();
                widget = findWidgetByIndex(idx[0], idx[1], curcell);
                mode = widget == null ? APPEND_MODE.REPLACE : APPEND_MODE.VERTICAL;
            } else if (pointY > y1 && pointY <= y2) {
                idx[0] = widget.getCell().getColumn();
                widget = findWidgetByIndex(idx[0], idx[1], curcell);
                mode = widget == null ? APPEND_MODE.REPLACE : APPEND_MODE.HORIZONTAL;
            } else {
                idx[0] = widget.getCell().getColumn() + widget.getCell().getColumnSpan();
                widget = findWidgetByIndex(idx[0], idx[1], curcell);
                mode = widget == null && idx[0] < columnCnt ? APPEND_MODE.REPLACE : APPEND_MODE.HORIZONTAL;
            }
            return new InsertionInfo(idx, mode, reportWidgetContainer);
        } else {
            int height, width;
            final int top = curcell == null || reportWidgetContainer.getCell() == null ? 0 : reportWidgetContainer.getCell().getTopRow();
            final int left = curcell == null || reportWidgetContainer.getCell() == null ? 0 : reportWidgetContainer.getCell().getLeftColumn();
            if (reportWidgetContainer.getCell() == null) {
                height = TxtUtils.px2Rows(reportWidgetContainer.getHeight());
                width = TxtUtils.px2Columns(reportWidgetContainer.getWidth());
            } else {
                height = reportWidgetContainer.getCell().getHeightRows();
                width = reportWidgetContainer.getCell().getWidthCols();
            }
            final int xCol = TxtUtils.px2Columns(p.x);
            final int yRow = TxtUtils.px2Rows(p.y);
            Integer columnIndex = getIndexTxt(xCol - left, width, columnCnt);
            //Integer columnIndex=getIndex(columnsSize,  p.x);
            if (columnIndex < 0) {
                if (curcell != null && curcell.getCell().getColumn() >= 0) {
                    columnIndex = curcell.getCell().getColumn();
                } else {
                    columnIndex = 0;
                }
            }
            Integer rowIndex = getIndexTxt(yRow - top, height, rowCnt);
            //Integer rowIndex=getIndex(rowsSize,  p.y);
            if (rowIndex < 0) {
                if (curcell != null && curcell.getCell().getRow() >= 0) {
                    rowIndex = curcell.getCell().getRow();
                } else {
                    rowIndex = 0;
                }
            }
            idx[0] = columnIndex.intValue();
            idx[1] = rowIndex.intValue();

            AdsReportSelectableWidget widget = findWidgetByIndex(idx[0], idx[1], curcell);
            final InsertionInfo info = checkOnAdd(curcell, widget, p);
            if (info != null) {
                //info.setMode(APPEND_MODE.ADD);
                info.setIsAdding(true);
                return info;
            }
            if (widget == null) {
                //return APPEND_MODE.REPLACE;
                return new InsertionInfo(idx, APPEND_MODE.REPLACE, reportWidgetContainer);
            }
            final Rectangle r = widget.getBounds();
            r.y = r.y - AdsReportSelectableWidget.IDENT;
            r.x = r.x - AdsReportSelectableWidget.IDENT;//учитываем отступ
            r.height = r.height + AdsReportSelectableWidget.IDENT * 2;
            r.width = r.width + AdsReportSelectableWidget.IDENT * 2;//учитываем отступ
            int a = Math.round(((float) r.height) / r.width);
            int b = r.y - a * r.x;
            int y1 = a * p.x + b;

            a = Math.round(-((float) r.height) / r.width);
            b = r.y + r.height - a * r.x;
            int y2 = p.x * a + b;
            APPEND_MODE mode;
            int pointY = curcell == null ? p.y : p.y - reportWidgetContainer.getY();
            if (pointY > y1 && pointY > y2) {
                idx[1] = widget.getCell().getRow() + widget.getCell().getRowSpan();
                widget = findWidgetByIndex(idx[0], idx[1], curcell);
                mode = widget == null && idx[1] < rowCnt ? APPEND_MODE.REPLACE : APPEND_MODE.VERTICAL;
            } else if (pointY <= y1 && pointY <= y2) {
                idx[1] = widget.getCell().getRow();
                widget = findWidgetByIndex(idx[0], idx[1], curcell);
                mode = widget == null ? APPEND_MODE.REPLACE : APPEND_MODE.VERTICAL;
            } else if (pointY > y1 && pointY <= y2) {
                idx[0] = widget.getCell().getColumn();
                widget = findWidgetByIndex(idx[0], idx[1], curcell);
                mode = widget == null ? APPEND_MODE.REPLACE : APPEND_MODE.HORIZONTAL;
            } else {
                idx[0] = widget.getCell().getColumn() + widget.getCell().getColumnSpan();
                widget = findWidgetByIndex(idx[0], idx[1], curcell);
                mode = widget == null && idx[0] < columnCnt ? APPEND_MODE.REPLACE : APPEND_MODE.HORIZONTAL;
            }
            return new InsertionInfo(idx, mode, reportWidgetContainer);
        }
    }

    @Override
    EReportLayout getType() {
        return EReportLayout.GRID;
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
            final int[] grigSize = getGridSize();
            final int columnCnt = grigSize[0];
            final int rowCnt = grigSize[1];
            double x = 0, y = 0;
            if (columnCnt > 0 || rowCnt > 0) {
                final double rowSpace = rowCnt > 0 ? height / rowCnt : height;
                final double columnSpace = columnCnt > 0 ? width / columnCnt : width;
                x = MmUtils.snapToGrid(columnSpace * idx[0]);
                y = MmUtils.snapToGrid(rowSpace * idx[1]);
                width = info.getMode() == ReportLayoutProcessor.APPEND_MODE.HORIZONTAL ? 0 : columnSpace;
                height = info.getMode() == ReportLayoutProcessor.APPEND_MODE.VERTICAL ? 0 : rowSpace;
            }
            int xPx = x == 0 ? 1 : MmUtils.mm2px(x);
            xPx = idx[0] == columnCnt ? xPx - 1 : xPx;
            int yPx = y == 0 ? 1 : MmUtils.mm2px(y);
            yPx = idx[1] == rowCnt ? yPx - 1 : yPx;
            final Rectangle rect = new Rectangle(xPx, yPx, MmUtils.mm2px(width), MmUtils.mm2px(height));
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
            final int[] grigSize = getGridSize();
            final int columnCnt = grigSize[0];
            final int rowCnt = grigSize[1];
            int x = 0, y = 0;
            if (columnCnt > 0 || rowCnt > 0) {
                final int rowSpace = rowCnt > 0 ? Math.round((float) height / rowCnt) : height;
                final int columnSpace = columnCnt > 0 ? Math.round((float) width / columnCnt) : width;
                x = TxtUtils.snapToGrid(columnSpace * idx[0]);
                y = TxtUtils.snapToGrid(rowSpace * idx[1]);
                width = info.getMode() == ReportLayoutProcessor.APPEND_MODE.HORIZONTAL ? 0 : columnSpace;
                height = info.getMode() == ReportLayoutProcessor.APPEND_MODE.VERTICAL ? 0 : rowSpace;
            }
            int xPx = x == 0 ? 1 : TxtUtils.columns2Px(x);
            xPx = idx[0] == columnCnt ? xPx - 1 : xPx;
            int yPx = y == 0 ? 1 : TxtUtils.rows2Px(y);
            yPx = idx[1] == rowCnt ? yPx - 1 : yPx;
            final Rectangle rect = new Rectangle(xPx, yPx, TxtUtils.columns2Px(width), TxtUtils.rows2Px(height));
            reportWidgetContainer.setInsertionPlaceRect(rect);
        }
    }

    @Override
    EReportLayout type() {
        return EReportLayout.GRID;
    }
}
