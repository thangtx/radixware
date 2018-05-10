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

import java.awt.Color;
import java.awt.Graphics;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidget;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidgetContainer;

public class AdsReportSelectableWidget extends AdsReportAbstractSelectableWidget {

    public static final int EDGE_SIZE_PX = 5;
    private static final Color SNAP_COLOR = Color.RED.darker();
    protected final AdsReportWidget reportWidget;
    private final CellListener cellListener;
    private boolean dragged = false;
    public static final int IDENT = 1;
    //  private final RadixEventSource stateSupport = new RadixEventSource(); // support to listen undo/redo possibility
    // private final SelectionChangeSupport changesSupport = new SelectionChangeSupport();

    public boolean isDragged() {
        return dragged;
    }

    public void setDragged(final boolean isDragged) {
        this.dragged = isDragged;
    }

    public AdsReportSelectableWidget(final AdsReportFormDiagram diagram, final AdsReportWidget reportWidget) {
        super(diagram, reportWidget);
        this.reportWidget = reportWidget;
        setOpaque(true);
        cellListener = new CellListener(this);
        addMouseListener(cellListener);
        addMouseMotionListener(cellListener);
        addKeyListener(cellListener);
    }

    protected AdsReportSelectableWidget(final AdsReportFormDiagram diagram, final AdsReportBand band) {
        super(diagram, band);
        this.reportWidget = null;
        cellListener = null;
    }

//    public void setSelected(final boolean selected, boolean requestFocus) {
//        //if(reportWidget==null)return;
//        if (this.selected != selected) {
//            this.selected = selected;
//            if (requestFocus && selected) {
//                requestFocus();
//            }
//            repaint();
//            fireSelectionChanged(new SelectionEvent(this, selected));
//        }
//        if (this instanceof AdsReportBaseContainer && !selected) {
//            for (AdsReportSelectableWidget widget : ((AdsReportBaseContainer) this).getCellWidgets()) {
//                widget.setSelected(selected);
//            }
//        }
//
//        if (reportWidget != null) {
//            final AdsReportBaseContainer bandSubWidget = (AdsReportBaseContainer) getParent();//getOwnerBandWidget().bandSubWidget;
//            if (bandSubWidget != null) {
//                bandSubWidget.setComponentZOrder(this, 0);
//            }
//        }
//    }

    @Override
    public void update() {
        if (reportWidget == null) {
            return;
        }
        final int ident = reportWidget.isReportContainer() ? 0 : IDENT;

        if (getDiagramMode() == AdsReportForm.Mode.GRAPHICS) {
            final int leftPx = MmUtils.mm2px(reportWidget.getLeftMm()) + ident;
            final int topPx = MmUtils.mm2px(reportWidget.getTopMm()) + ident;
            setLocation(leftPx, topPx);

            final int widthPx = MmUtils.mm2px(reportWidget.getWidthMm()) - ident * 2;
            final int heightPx = MmUtils.mm2px(reportWidget.getHeightMm()) - ident * 2;
            setSize(widthPx, heightPx);
        }else{
            final int leftPx = TxtUtils.columns2Px(reportWidget.getLeftColumn()) + ident;
            final int topPx = TxtUtils.rows2Px(reportWidget.getTopRow()) + ident;
            setLocation(leftPx, topPx);

            final int widthPx = TxtUtils.columns2Px(reportWidget.getWidthCols()) - ident * 2;
            final int heightPx = TxtUtils.rows2Px(reportWidget.getHeightRows()) - ident * 2;
            setSize(widthPx, heightPx);
        }
    }

    protected void paintBackground(final Graphics g) {
        if (reportWidget != null) {

            final int width = getWidth();
            final int height = getHeight();

            g.setColor(reportWidget.getBgColor());
            g.fillRect(0, 0, width, height);
        }
    }

    private static void drawEdge(final Graphics g, final int x, final int y, final boolean snap) {
        g.setColor(snap ? SNAP_COLOR : Color.GRAY);
        g.drawRect(x, y, EDGE_SIZE_PX, EDGE_SIZE_PX);
        g.setColor(Color.WHITE);
        g.fillRect(x + 1, y + 1, EDGE_SIZE_PX - 1, EDGE_SIZE_PX - 1);
    }

    protected void paintEdges(final Graphics g) {
        if (reportWidget == null) {
            return;
        }
        final int width = getWidth() - 1;
        final int height = getHeight() - 1;

        if (isSelected()) {
            g.setColor(Color.GRAY);
            g.drawRect(EDGE_SIZE_PX / 2, EDGE_SIZE_PX / 2, width - EDGE_SIZE_PX + 1, height - EDGE_SIZE_PX + 1);
            //g.drawRect(0, 0, width, height);

            boolean isSnapTopEdge = false;
            boolean isSnapBottomEdge = false;
            if (!reportWidget.isReportContainer()) {
                isSnapTopEdge = ((AdsReportCell) reportWidget).isSnapTopEdge();
                isSnapBottomEdge = ((AdsReportCell) reportWidget).isSnapBottomEdge();
            }
            // top
            drawEdge(g, (width - EDGE_SIZE_PX) / 2, 0, isSnapTopEdge);
            // right
            drawEdge(g, width - EDGE_SIZE_PX, (height - EDGE_SIZE_PX) / 2, false);
            // bottom
            drawEdge(g, (width - EDGE_SIZE_PX) / 2, height - EDGE_SIZE_PX, isSnapBottomEdge);
            // left
            drawEdge(g, 0, (height - EDGE_SIZE_PX) / 2, false);
            // top left
            drawEdge(g, 0, 0, isSnapTopEdge);
            // top right
            drawEdge(g, width - EDGE_SIZE_PX, 0, isSnapTopEdge);
            // bottom left
            drawEdge(g, 0, height - EDGE_SIZE_PX, isSnapBottomEdge);
            // bottom right
            drawEdge(g, width - EDGE_SIZE_PX, height - EDGE_SIZE_PX, isSnapBottomEdge);
        } else {
            if (!reportWidget.isReportContainer()) {
                g.setColor(((AdsReportCell) reportWidget).isSnapTopEdge() ? SNAP_COLOR : Color.GRAY);
                //else
                //   g.setColor(Color.GRAY);

                // top left
                g.drawLine(0, 0, EDGE_SIZE_PX, 0);
                g.drawLine(0, 0, 0, EDGE_SIZE_PX);

                // top right
                g.drawLine(width - EDGE_SIZE_PX, 0, width, 0);
                g.drawLine(width, 0, width, EDGE_SIZE_PX);

                //g.setColor(reportWidget.isSnapBottomEdge() ? SNAP_COLOR : Color.GRAY);            
                if (reportWidget.isReportContainer()) {
                    g.setColor(Color.GRAY);
                } else {
                    g.setColor(((AdsReportCell) reportWidget).isSnapBottomEdge() ? SNAP_COLOR : Color.GRAY);
                }

                // bottom left
                g.drawLine(0, height, EDGE_SIZE_PX, height);
                g.drawLine(0, height - EDGE_SIZE_PX, 0, height);

                // bottom right
                g.drawLine(width - EDGE_SIZE_PX, height, width, height);
                g.drawLine(width, height - EDGE_SIZE_PX, width, height);
            }
        }
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
    }

    public IReportCellContainer getOwnerWidget() {
        return (IReportCellContainer) getParent();
    }

    public AdsReportBandWidget getOwnerBandWidget() {
        AbstractAdsReportWidget parent = (AbstractAdsReportWidget) getParent();
        while (!(parent instanceof AdsReportBandWidget) && parent != null) {
            parent = (AbstractAdsReportWidget) parent.getParent();
        }
        return /*parent!=null ?*/ (AdsReportBandWidget) parent;
    }

    public AdsReportWidget getCell() {
        return reportWidget;
    }

    @Override
    protected void onSelected(boolean selected) {
        repaint();
    }

    @Override
    public RadixObject getRadixObject() {
        AdsReportWidget widget = getCell();
        return widget == null? super.getRadixObject() : widget;
    }

    public static final class Factory {

        private Factory() {
        }

        public static AdsReportSelectableWidget newInstance(final AdsReportFormDiagram diagram, final AdsReportWidget reportWidget) {
            AdsReportSelectableWidget result;
            if (reportWidget instanceof org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell) {
                result = new AdsReportCellWidget(diagram, reportWidget);
            } else {
                result = new AdsReportCellContainerWidget(diagram, (AdsReportWidgetContainer) reportWidget);
            }
            return result;
        }
    }
}
