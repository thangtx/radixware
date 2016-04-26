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
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.enums.EReportLayout;

public class FreeLayoutProcessor extends ReportLayoutProcessor {

    FreeLayoutProcessor(final AdsReportBaseContainer widgetContainer) {
        super(widgetContainer);
    }

    @Override
    void justifyLayout() {
        //
    }

    // @Override
    // void updateIndex(AdsReportSelectableWidget cell, Point point,boolean canAdd) {
    //throw new UnsupportedOperationException("Not supported yet.");
    // }
    @Override
    EReportLayout getType() {
        return EReportLayout.FREE;
    }

    // @Override
    // APPEND_MODE indexFromPoint(int[] idx, Point p, AdsReportSelectableWidget curcell,boolean canAdd) {
    //     return APPEND_MODE.ADD;
    // }
    @Override
    InsertionInfo indexFromPoint(final Point p, final AdsReportSelectableWidget curcell) {
        if (p == null) {
            return null;
        }
        for (AdsReportSelectableWidget widget : reportWidgetContainer.getCellWidgets()) {
            if (widget instanceof AdsReportCellContainerWidget) {
                InsertionInfo info = indexFromPointFromContainer(p, widget, curcell);
                if (info != null) {
                    return info;
                }
            }
        }
        return new InsertionInfo(null, null, null);
    }

    private InsertionInfo indexFromPointFromContainer(final Point p, AdsReportSelectableWidget widget, final AdsReportSelectableWidget curcell) {
        int x = widget.getX();
        int y = widget.getY();
        int width = x + widget.getWidth();
        int height = y + widget.getHeight();
        if (widget.getParent() != null && widget.getParent() instanceof AdsReportCellContainerWidget) {
            if (widget.getDiagramMode() == AdsReportForm.Mode.GRAPHICS) {
                final double[] parentPos = getContainetPositionMM((AdsReportCellContainerWidget) widget.getParent());
                x += MmUtils.mm2px(parentPos[0]);
                width += MmUtils.mm2px(parentPos[0]);
                y += MmUtils.mm2px(parentPos[1]);
                height += MmUtils.mm2px(parentPos[1]);
            } else {
                final int[] parentPos = getContainetPositionTxt((AdsReportCellContainerWidget) widget.getParent());
                x += TxtUtils.columns2Px(parentPos[0]);
                width += TxtUtils.columns2Px(parentPos[0]);
                y += TxtUtils.rows2Px(parentPos[1]);
                height += TxtUtils.rows2Px(parentPos[1]);
            }
        }
        if (!widget.equals(curcell) && p.x >= x && p.x <= width && p.y >= y && p.y <= height) {
            final InsertionInfo info = ((AdsReportCellContainerWidget) widget).getReportLayout().indexFromPoint(p, curcell);
            if (info != null) {
                info.setIsAdding(true);
                if (info.getParent() == null) {
                    info.setParent((AdsReportCellContainerWidget) widget);
                }
            }
            return info;
        }
        return null;
    }

    @Override
    void updateIndex(final AdsReportSelectableWidget widget, final InsertionInfo info) {

        if (info != null && widget != null && info.isAdding() && reportWidgetContainer != null && reportWidgetContainer.getCell() != null) {
            if (widget.getDiagramMode() == AdsReportForm.Mode.GRAPHICS) {
                final double[] containerPos = getContainetPositionMM(reportWidgetContainer);
                widget.getCell().setTopMm(widget.getCell().getTopMm() - containerPos[1]);
                widget.getCell().setLeftMm(widget.getCell().getLeftMm() - containerPos[0]);
                add(widget, reportWidgetContainer);
            } else {
                final int[] containerPos = getContainetPositionTxt(reportWidgetContainer);
                widget.getCell().setTopRow(widget.getCell().getTopRow() - containerPos[1]);
                widget.getCell().setLeftColumn(widget.getCell().getLeftColumn() - containerPos[0]);
                add(widget, reportWidgetContainer);
            }
        }
    }

    @Override
    void highlightInsertPlace(final InsertionInfo info) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    void changeSpan(final AdsReportSelectableWidget cellWidget, final int oldWidth, final int oldHeight, final boolean isUp, final boolean isLeft) {
    }

    @Override
    EReportLayout type() {
        return EReportLayout.FREE;
    }

}
