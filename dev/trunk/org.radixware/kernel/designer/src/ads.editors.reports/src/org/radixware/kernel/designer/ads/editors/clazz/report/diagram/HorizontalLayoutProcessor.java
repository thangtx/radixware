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

public class HorizontalLayoutProcessor extends ReportLayoutProcessor {

    protected HorizontalLayoutProcessor(final AdsReportBaseContainer reportWidgetContainer) {
        super(reportWidgetContainer);
    }

    @Override
    public void justifyLayout() {
        if (reportWidgetContainer.getDiagramMode() == AdsReportForm.Mode.GRAPHICS) {
            final double width, height;//=eportWidgetContainer.getWidth();
            if (reportWidgetContainer.getCell() != null) {
                height = reportWidgetContainer.getCell().getHeightMm();
                width = reportWidgetContainer.getCell().getWidthMm();
            } else {
                height = MmUtils.px2mm(reportWidgetContainer.getHeight());
                width = MmUtils.px2mm(reportWidgetContainer.getWidth());
            }
            final int widgetCnt = getColumnCount();
            if (widgetCnt > 0) {
                final double space = width / widgetCnt;
                for (AdsReportSelectableWidget cellWidget : reportWidgetContainer.getCellWidgets()) {
                    if (!cellWidget.isDragged()) {
                        final AdsReportWidget reportWidget = cellWidget.getCell();
                        final int index = reportWidget.getColumn();
                        final int span = reportWidget.getColumnSpan();
                        reportWidget.setLeftMm(/*MmUtils.px2mm(*/space * index);
                        reportWidget.setWidthMm(/*MmUtils.px2mm(*/space * span);
                        reportWidget.setTopMm(0);
                        reportWidget.setHeightMm(/*MmUtils.px2mm(reportWidgetContainer.getHeight())*/height);
                        if (cellWidget instanceof AdsReportBaseContainer) {
                            ((AdsReportBaseContainer) cellWidget).getReportLayout().justifyLayout();
                        }
                    }
                }
            }
        } else {
            int width, height;
            if (reportWidgetContainer.getCell() != null) {
                height = reportWidgetContainer.getCell().getHeightRows();
                width = reportWidgetContainer.getCell().getWidthCols();
            } else {
                height = TxtUtils.px2Rows(reportWidgetContainer.getHeight());
                width = TxtUtils.px2Columns(reportWidgetContainer.getWidth());
            }
            final int widgetCnt = getColumnCount();
            if (widgetCnt > 0) {
                final float space = (float) width / (float) widgetCnt;
                for (AdsReportSelectableWidget cellWidget : reportWidgetContainer.getCellWidgets()) {
                    if (!cellWidget.isDragged()) {
                        final AdsReportWidget reportWidget = cellWidget.getCell();
                        final int index = reportWidget.getColumn();
                        final int span = reportWidget.getColumnSpan();
                        reportWidget.setLeftColumn(Math.round(space * index));
                        reportWidget.setWidthCols(Math.round(space * span));
                        reportWidget.setTopRow(0);
                        reportWidget.setHeightRows(/*MmUtils.px2mm(reportWidgetContainer.getHeight())*/height);
                        if (cellWidget instanceof AdsReportBaseContainer) {
                            ((AdsReportBaseContainer) cellWidget).getReportLayout().justifyLayout();
                        }
                    }
                }
            }
        }
    }

    private int getColumnCount() {
        int columnCount = 0;
        for (AdsReportSelectableWidget w : reportWidgetContainer.getCellWidgets()) {
            if (w != null) {
                final AdsReportWidget cell = w.getCell();
                if (cell.getColumn() + cell.getColumnSpan() - 1 > columnCount) {
                    columnCount = cell.getColumn() + cell.getColumnSpan() - 1;
                }
            }
        }
        columnCount = checkOnDraggedColumns(columnCount + 1);
        if (columnCount == 0) {
            columnCount = reportWidgetContainer.getCellWidgets().size();
        }
        return columnCount;
    }

    @Override
    void updateIndex(final AdsReportSelectableWidget curWidget, final InsertionInfo info) {
        if (info != null && curWidget != null) {
            final int[] idx = info.getIdx();
            if (idx == null) {
                return;
            }
            final APPEND_MODE mode = info.getMode();
            if (info.isAdding()) {
                add(curWidget, reportWidgetContainer);
            }
            final int column = idx[0];

            shiftCells(curWidget, mode, column, 0, 1);
            curWidget.getCell().setColumnSpan(1);
            curWidget.getCell().setColumn(column);
            curWidget.getCell().setRow(0);
        }
    }

    @Override
    InsertionInfo indexFromPoint(final Point p, final AdsReportSelectableWidget curcell) {
        if (p == null) {
            return null;
        }
        final int[] idx = new int[]{0, 0};
        //Collection<AdsReportSelectableWidget> cellWidgets = reportWidgetContainer.getCellWidgets();
        final int size = getColumnCount();
        if (size == 0) {
            return new InsertionInfo(idx, APPEND_MODE.HORIZONTAL, reportWidgetContainer);
        }
        //List<Size> columnsSize=getColumnsSize(size,curcell);
        //Integer columnIndex=getIndex(columnsSize,  p.x);
        if (reportWidgetContainer.getDiagramMode() == AdsReportForm.Mode.GRAPHICS) {
            double containerWidth;
            final double containerPos = curcell == null || reportWidgetContainer.getCell() == null ? 0 : reportWidgetContainer.getCell().getLeftMm();
            if (reportWidgetContainer.getCell() == null) {
                containerWidth = MmUtils.px2mm(reportWidgetContainer.getWidth());
            } else {
                containerWidth = reportWidgetContainer.getCell().getWidthMm();
            }
            final double xMm = MmUtils.px2mm(p.x) - containerPos;
            Integer columnIndex = getIndexMM(xMm, containerWidth, size);
            if (columnIndex < 0) {
                if (curcell != null && curcell.getCell().getColumn() >= 0) {
                    columnIndex = curcell.getCell().getColumn();
                } else {
                    columnIndex = 0;
                }
            }
            final AdsReportSelectableWidget widget = findWidgetByIndex(columnIndex, curcell);
            final InsertionInfo info = checkOnAdd(curcell, widget, p);
            if (info != null) {
                info.setIsAdding(true);
                return info;
            }
            if (widget == null) {
                idx[0] = columnIndex.intValue();
                return new InsertionInfo(idx, APPEND_MODE.REPLACE, reportWidgetContainer);
            } else {
                columnIndex = widget.getCell().getColumn();//for case when span>1
            }

            //Rectangle r = widget.getBounds();//new Rectangle(MmUtils.mm2px(widget.getCell().getLeftMm()),MmUtils.mm2px(widget.getCell().getTopMm()),MmUtils.mm2px(widget.getCell().getWidthMm()),MmUtils.mm2px(widget.getCell().getHeightMm()));
            //r.x=r.x-AdsReportSelectableWidget.IDENT; r.width=r.width+AdsReportSelectableWidget.IDENT*2;//учитываем отступ
            final double x = widget.getCell().getLeftMm();
            final double width = widget.getCell().getWidthMm();
            if (xMm <= x + width / 2 && xMm >= x) {
                idx[0] = columnIndex;
            } else if (xMm > x + width / 2 && xMm <= x + width) {
                idx[0] = columnIndex + widget.getCell().getColumnSpan();
            }
            return new InsertionInfo(idx, APPEND_MODE.HORIZONTAL, reportWidgetContainer);
        } else {
            int containerWidth;
            final int containerPos = curcell == null || reportWidgetContainer.getCell() == null ? 0 : reportWidgetContainer.getCell().getLeftColumn();
            if (reportWidgetContainer.getCell() == null) {
                containerWidth = TxtUtils.px2Columns(reportWidgetContainer.getWidth());
            } else {
                containerWidth = reportWidgetContainer.getCell().getWidthCols();
            }
            final int xCol = TxtUtils.px2Columns(p.x) - containerPos;
            Integer columnIndex = getIndexTxt(xCol, containerWidth, size);
            if (columnIndex < 0) {
                if (curcell != null && curcell.getCell().getColumn() >= 0) {
                    columnIndex = curcell.getCell().getColumn();
                } else {
                    columnIndex = 0;
                }
            }
            final AdsReportSelectableWidget widget = findWidgetByIndex(columnIndex, curcell);
            final InsertionInfo info = checkOnAdd(curcell, widget, p);
            if (info != null) {
                info.setIsAdding(true);
                return info;
            }
            if (widget == null) {
                idx[0] = columnIndex.intValue();
                return new InsertionInfo(idx, APPEND_MODE.REPLACE, reportWidgetContainer);
            } else {
                columnIndex = widget.getCell().getColumn();//for case when span>1
            }

            //Rectangle r = widget.getBounds();//new Rectangle(MmUtils.mm2px(widget.getCell().getLeftMm()),MmUtils.mm2px(widget.getCell().getTopMm()),MmUtils.mm2px(widget.getCell().getWidthMm()),MmUtils.mm2px(widget.getCell().getHeightMm()));
            //r.x=r.x-AdsReportSelectableWidget.IDENT; r.width=r.width+AdsReportSelectableWidget.IDENT*2;//учитываем отступ
            final int x = widget.getCell().getLeftColumn();
            final int width = widget.getCell().getWidthCols();
            if (xCol <= x + width / 2 && xCol >= x) {
                idx[0] = columnIndex;
            } else if (xCol > x + width / 2 && xCol <= x + width) {
                idx[0] = columnIndex + widget.getCell().getColumnSpan();
            }
            return new InsertionInfo(idx, APPEND_MODE.HORIZONTAL, reportWidgetContainer);
        }
    }

    /*  @Override
     void updateIndex(AdsReportSelectableWidget curWidget,Point point) {
     int[] idx=indexFromPoint(point, curWidget) ;
     if(idx==null) return;
     int column=idx[0];
     AdsReportSelectableWidget widget=findWidgetByIndex( column, curWidget);
     if(checkOnAdd( curWidget, widget))
     return;
     for(AdsReportSelectableWidget w:reportWidgetContainer.getCellWidgets()){
     AdsReportWidget cell=w.getCell();
     if(!w.equals(curWidget) && cell.getColumn()>=column){
     cell.setColumn(cell.getColumn()+1);
     w.update();
     }
     }
     curWidget.getCell().setColumn(column);
     }
    
     private int[] indexFromPoint(Point p, AdsReportSelectableWidget curcell) {
     Collection<AdsReportSelectableWidget> cellWidgets = reportWidgetContainer.getCellWidgets();
     int size = cellWidgets.size();
     if(size>0){
     for(AdsReportSelectableWidget w:reportWidgetContainer.getCellWidgets()){
     if(!curcell.equals(w)){
     if(w!=null){
     Rectangle r = w.getBounds();
     AdsReportWidget cell=w.getCell();
     int index=cell.getColumn();
     if (index == 0 && r.x + r.width / 2 >= p.x) {
     return new int[]{0, 0};
     } else if (index == size - 1 && r.x  + r.width  / 2 < p.x) {
     return new int[]{size, 0};
     } else {
     if (p.x<= r.x  +  r.width / 2 && p.x >= r.x  ){
     return new int[]{index, 0};
     }else if (p.x >r.x  +  r.width / 2  && p.x < r.x  +  r.width ) {
     return new int[]{index+1, 0};
     }
     }
     }
     }
     }
     }
     return size<=1? new int[]{0, 0}: null;
     }*/
    private AdsReportSelectableWidget findWidgetByIndex(final int column, final AdsReportSelectableWidget curcell) {
        for (AdsReportSelectableWidget w : reportWidgetContainer.getCellWidgets()) {
            final AdsReportWidget cell = w.getCell();
            if (!w.equals(curcell) && (cell.getColumn() <= column && cell.getColumn() + cell.getColumnSpan() > column)) {
                return w;
            }
        }
        return null;
    }

    @Override
    EReportLayout getType() {
        return EReportLayout.HORIZONTAL;
    }

    @Override
    public void highlightInsertPlace(final InsertionInfo info) {
        if (reportWidgetContainer.getDiagramMode() == AdsReportForm.Mode.GRAPHICS) {
            double height, width;
            if (reportWidgetContainer.getCell() != null) {
                height = reportWidgetContainer.getCell().getHeightMm();
                width = reportWidgetContainer.getCell().getWidthMm();
            } else {
                height = MmUtils.px2mm(reportWidgetContainer.getHeight());
                width = MmUtils.px2mm(reportWidgetContainer.getWidth());
            }

            final int[] idx = info.getIdx();
            final int columnCnt = getColumnCount();
            double x = 0;
            if (columnCnt > 0) {
                final double columnSpace = columnCnt > 0 ? width / columnCnt : width;
                x = MmUtils.snapToGrid(columnSpace * idx[0]);
            }
            int xPx = x == 0 ? 1 : MmUtils.mm2px(x);
            xPx = idx[0] == columnCnt ? xPx - 1 : xPx;
            width = columnCnt == 0 ? width : 0;
            final Rectangle rect = new Rectangle(xPx, 0, MmUtils.mm2px(width), MmUtils.mm2px(height));
            reportWidgetContainer.setInsertionPlaceRect(rect);
        } else {
            int height, width;
            if (reportWidgetContainer.getCell() != null) {
                height = reportWidgetContainer.getCell().getHeightRows();
                width = reportWidgetContainer.getCell().getWidthCols();
            } else {
                height = TxtUtils.px2Rows(reportWidgetContainer.getHeight());
                width = TxtUtils.px2Columns(reportWidgetContainer.getWidth());
            }

            final int[] idx = info.getIdx();
            final int columnCnt = getColumnCount();
            int x = 0;
            if (columnCnt > 0) {
                final int columnSpace = columnCnt > 0 ? Math.round((float) width / columnCnt) : width;
                x = TxtUtils.snapToGrid(columnSpace * idx[0]);
            }
            int xPx = x == 0 ? 1 : TxtUtils.columns2Px(x);
            xPx = idx[0] == columnCnt ? xPx - 1 : xPx;
            width = columnCnt == 0 ? width : 0;
            final Rectangle rect = new Rectangle(xPx, 0, TxtUtils.columns2Px(width), TxtUtils.rows2Px(height));
            reportWidgetContainer.setInsertionPlaceRect(rect);
        }
    }

    @Override
    void changeSpan(final AdsReportSelectableWidget cellWidget, final int oldWidth, final int oldHeight, final boolean isUp, final boolean isLeft) {
        changeHorizontalSpan(cellWidget, oldWidth, isLeft);
        justifyLayout();
    }

    @Override
    EReportLayout type() {
        return EReportLayout.HORIZONTAL;
    }
}