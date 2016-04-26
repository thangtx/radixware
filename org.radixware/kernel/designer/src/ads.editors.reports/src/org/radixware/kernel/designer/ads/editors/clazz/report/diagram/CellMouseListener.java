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

import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPopupMenu;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidget;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidgetContainer;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.IReportWidgetContainer;
import org.radixware.kernel.common.enums.EReportCellType;
import org.radixware.kernel.common.enums.EReportLayout;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.ReportLayoutProcessor.InsertionInfo;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;

class CellMouseListener extends WidgetMouseListener {

    private final AdsReportSelectableWidget cellWidget;
    private InsertionInfo insertionInfo = null;
    private AdsReportFormUndoRedo undoRedo;

    public CellMouseListener(final AdsReportSelectableWidget cellWidget) {
        this.cellWidget = cellWidget;
    }

    private AdsReportFormUndoRedo getUndoRedo() {
        if (undoRedo == null && cellWidget.getOwnerBandWidget() != null && cellWidget.getOwnerBandWidget().getOwnerFormDiagram() != null) {
            final AdsReportFormDiagram formDiagram = cellWidget.getOwnerBandWidget().getOwnerFormDiagram();
            undoRedo = formDiagram.getUndoRedo();
        }
        return undoRedo;
    }

    @Override
    protected void select(final boolean expand) {
        final AdsReportBandWidget bandWidget = cellWidget.getOwnerBandWidget();
        if (!bandWidget.isSelected()) {
            AdsReportWidgetUtils.selectBand(bandWidget);
        }

        if (expand) {
            cellWidget.setSelected(!cellWidget.isSelected());
        } else {
            AdsReportSelectableWidget selectWidget = cellWidget;
            if (cellWidget.isSelected() && cellWidget.getParent() != null && (cellWidget.getParent() instanceof AdsReportBaseContainer)
                    && !bandWidget.equals(cellWidget.getParent().getParent())) {
                selectWidget = (AdsReportSelectableWidget) cellWidget.getParent();
            }
            AdsReportWidgetUtils.unselectCellsAndSubReports(bandWidget);
            selectWidget.setSelected(true);
        }
    }

    @Override
    protected void edit() {
        cellWidget.edit();
    }

    private enum Edge {

        TOP,
        RIGHT,
        BOTTOM,
        LEFT,
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }
    private double xMm, yMm;
    private int xCol, yRow;
    private Edge edge;
    private int oldWidth;
    private int oldHeight;

    @Override
    public void mousePressed(final MouseEvent e) {
        super.mousePressed(e);

        if (cellWidget == null) {
            return;
        }

        if (cellWidget.getDiagramMode() == AdsReportForm.Mode.GRAPHICS) {
            int x = e.getX();
            int y = e.getY();
            xMm = MmUtils.px2mm(x);
            yMm = MmUtils.px2mm(y);
            edge = null;

            final int width = cellWidget.getWidth();
            final int height = cellWidget.getHeight();
            final int EDGE = AdsReportCellWidget.EDGE_SIZE_PX + 1;

            oldWidth = width;
            oldHeight = height;
            if (cellWidget.isSelected()) {
                if (x <= EDGE && y < EDGE) {
                    edge = Edge.TOP_LEFT;
                } else if (x >= width - EDGE && y < EDGE) {
                    edge = Edge.TOP_RIGHT;
                } else if (x <= EDGE && y >= height - EDGE) {
                    edge = Edge.BOTTOM_LEFT;
                } else if (x >= width - EDGE && y >= height - EDGE) {
                    edge = Edge.BOTTOM_RIGHT;
                } else if (x >= (width - EDGE) / 2 && x <= (width + EDGE) / 2 && y <= EDGE) {
                    edge = Edge.TOP;
                } else if (y >= (height - EDGE) / 2 && y <= (height + EDGE) / 2 && x >= width - EDGE) {
                    edge = Edge.RIGHT;
                } else if (x >= (width - EDGE) / 2 && x <= (width + EDGE) / 2 && y >= height - EDGE) {
                    edge = Edge.BOTTOM;
                } else if (y >= (height - EDGE) / 2 && y <= (height + EDGE) / 2 && x <= EDGE) {
                    edge = Edge.LEFT;
                }
            }
        } else {
            int x = e.getX();
            int y = e.getY();
            xCol = TxtUtils.px2Columns(x);
            yRow = TxtUtils.px2Rows(y);
            edge = null;

            final int width = cellWidget.getWidth();
            final int height = cellWidget.getHeight();
            final int EDGE = AdsReportCellWidget.EDGE_SIZE_PX + 1;

            oldWidth = width;
            oldHeight = height;
            if (cellWidget.isSelected()) {
                if (x <= EDGE && y < EDGE) {
                    edge = Edge.TOP_LEFT;
                } else if (x >= width - EDGE && y < EDGE) {
                    edge = Edge.TOP_RIGHT;
                } else if (x <= EDGE && y >= height - EDGE) {
                    edge = Edge.BOTTOM_LEFT;
                } else if (x >= width - EDGE && y >= height - EDGE) {
                    edge = Edge.BOTTOM_RIGHT;
                } else if (x >= (width - EDGE) / 2 && x <= (width + EDGE) / 2 && y <= EDGE) {
                    edge = Edge.TOP;
                } else if (y >= (height - EDGE) / 2 && y <= (height + EDGE) / 2 && x >= width - EDGE) {
                    edge = Edge.RIGHT;
                } else if (x >= (width - EDGE) / 2 && x <= (width + EDGE) / 2 && y >= height - EDGE) {
                    edge = Edge.BOTTOM;
                } else if (y >= (height - EDGE) / 2 && y <= (height + EDGE) / 2 && x <= EDGE) {
                    edge = Edge.LEFT;
                }
            }
        }
    }

    private List<AdsReportSelectableWidget> graggedItems = new ArrayList<>();
    private boolean dragging = false;

    @Override
    public void mouseDragged(final MouseEvent e) {
        super.mouseDragged(e);
        if (getUndoRedo() != null && !undoRedo.isBlocked()) {
            undoRedo.block(true);
        }
        //if(edge==null){
        if (!cellWidget.isSelected()) {
            return;
        } else {
            prepareDragged();
        }
        //}
        if (cellWidget.getDiagramMode() == AdsReportForm.Mode.GRAPHICS) {

            // calculate offset in mm
            double dXMm = /*dXMm+ */ MmUtils.px2mm(e.getX()) - xMm;
            double dYMm = /*dYMm+ */ MmUtils.px2mm(e.getY()) - yMm;

            // calculate position on band (prepare to snap to grid)
            final AdsReportWidget cell = cellWidget.getCell();
            double posXMm = cell.getLeftMm() + dXMm;
            //if (edge == Edge.TOP_RIGHT || edge == Edge.BOTTOM_RIGHT || edge == Edge.RIGHT) {
            //    posXMm += cell.getWidthMm();
            //}
            double posYMm = cell.getTopMm() + dYMm;
        //if (edge == Edge.BOTTOM_LEFT || edge == Edge.BOTTOM_RIGHT || edge == Edge.BOTTOM) {
            //    posYMm += cell.getHeightMm();
            //}

            // snap to grid
            posXMm = MmUtils.snapToGrid(posXMm);
            posYMm = MmUtils.snapToGrid(posYMm);

            //AdsReportBandWidget bandWidget=cellWidget.getOwnerBandWidget();
            //boolean applyToAllCells=bandWidget.getReportLayout().type()== EReportLayout.FREE;
            // perform
            if (edge == Edge.TOP_RIGHT || edge == Edge.BOTTOM_RIGHT || edge == Edge.RIGHT) {
                final double oldWidthMm = cell.getWidthMm();
                for (AdsReportSelectableWidget sibling : graggedItems) {
                    if (sibling.isSelected()) {
                        AdsReportWidget widget = sibling.getCell();
                        double pos = MmUtils.snapToGrid(widget.getLeftMm() + dXMm + widget.getWidthMm());
                        pos = Math.max(pos, widget.getLeftMm() + AdsReportBand.GRID_SIZE_MM);
                        final double newWidthMm = MmUtils.roundToTenth(pos - widget.getLeftMm());
                        widget.setWidthMm(newWidthMm);
                    }
                }
                xMm += (cell.getWidthMm() - oldWidthMm);
            } else {
                posXMm = Math.max(0, posXMm);
                if (edge == Edge.TOP_LEFT || edge == Edge.BOTTOM_LEFT || edge == Edge.LEFT) {
                    posXMm = Math.min(posXMm, cell.getLeftMm() + cell.getWidthMm() - AdsReportBand.GRID_SIZE_MM);
                    double dWidthMm = cell.getLeftMm() - posXMm;
                    for (AdsReportSelectableWidget sibling : graggedItems) {
                        if (sibling.isSelected()) {
                            AdsReportWidget widget = sibling.getCell();
                            double newWidthMm = Math.max(widget.getWidthMm() + dWidthMm, AdsReportBand.GRID_SIZE_MM);
                            newWidthMm = MmUtils.roundToTenth(newWidthMm);
                            //double newWidthMm=MmUtils.roundToTenth(widget.getWidthMm() + dWidthMm);
                            if (newWidthMm > AdsReportBand.GRID_SIZE_MM) {
                                final double newLeftMm = MmUtils.roundToTenth(widget.getLeftMm() - dWidthMm);
                                widget.setLeftMm(newLeftMm);
                            }
                            widget.setWidthMm(newWidthMm);
                        }
                    }
                } else if (edge == null) {
                    double dMm = posXMm - cell.getLeftMm();
                    for (AdsReportSelectableWidget sibling : graggedItems/*bandWidget.getCellWidgets()*/) {
                        if (sibling.isSelected()) {
                            final double newLeftMm = MmUtils.roundToTenth(sibling.getCell().getLeftMm() + dMm);
                            sibling.getCell().setLeftMm(newLeftMm);
                        }
                    }
                }
            }

            if (edge == Edge.BOTTOM_LEFT || edge == Edge.BOTTOM_RIGHT || edge == Edge.BOTTOM) {
                final double oldHeightMm = cell.getHeightMm();
                for (AdsReportSelectableWidget sibling : graggedItems) {
                    if (sibling.isSelected()) {
                        AdsReportWidget widget = sibling.getCell();
                        double pos = MmUtils.snapToGrid(widget.getTopMm() + dYMm + widget.getHeightMm());
                        pos = Math.max(pos, widget.getTopMm() + AdsReportBand.GRID_SIZE_MM);
                        final double newHeightMm = MmUtils.roundToTenth(pos - widget.getTopMm());
                        widget.setHeightMm(newHeightMm);
                    }
                }
                yMm += (cell.getHeightMm() - oldHeightMm);
            } else {
                posYMm = Math.max(0, posYMm);
                if (edge == Edge.TOP_LEFT || edge == Edge.TOP_RIGHT || edge == Edge.TOP) {
                    posYMm = Math.min(posYMm, cell.getTopMm() + cell.getHeightMm() - AdsReportBand.GRID_SIZE_MM);
                    double dMm = cell.getTopMm() - posYMm;
                    for (AdsReportSelectableWidget sibling : graggedItems) {
                        if (sibling.isSelected()) {
                            AdsReportWidget widget = sibling.getCell();

                            double newHeightMm = Math.max(widget.getHeightMm() + dMm, AdsReportBand.GRID_SIZE_MM);
                            newHeightMm = MmUtils.roundToTenth(newHeightMm);
                            //final double newHeightMm = MmUtils.roundToTenth(widget.getHeightMm() + dMm);
                            if (newHeightMm > AdsReportBand.GRID_SIZE_MM) {
                                final double newTopMm = MmUtils.roundToTenth(widget.getTopMm() - dMm);
                                widget.setTopMm(newTopMm);
                            }
                            widget.setHeightMm(newHeightMm);
                        }
                    }
                } else if (edge == null) {
                    double dMm = posYMm - cell.getTopMm();
                    for (AdsReportSelectableWidget sibling : graggedItems/*bandWidget.getCellWidgets()*/) {
                        if (sibling.isSelected()) {
                            final double newTopMm = MmUtils.roundToTenth(sibling.getCell().getTopMm() + dMm);
                            sibling.getCell().setTopMm(newTopMm);
                        }
                    }
                }
            }
            if (edge == null) {
                final IReportCellContainer ownerWidget = cellWidget.getOwnerWidget();
                insertionInfo = ((AdsReportBaseContainer) ownerWidget).calcInsertionPlace(cellWidget, insertionInfo, null);
            }
        } else {
            // calculate offset in symbols
            int dXCols = TxtUtils.px2Columns(e.getX()) - xCol;
            int dYRows = TxtUtils.px2Rows(e.getY()) - yRow;

            // calculate position on band (prepare to snap to grid)
            final AdsReportWidget cell = cellWidget.getCell();
            int posXCols = cell.getLeftColumn() + dXCols;
            int posYRows = cell.getTopRow() + dYRows;

            // snap to grid
            posXCols = TxtUtils.snapToGrid(posXCols);
            posYRows = TxtUtils.snapToGrid(posYRows);

            //AdsReportBandWidget bandWidget=cellWidget.getOwnerBandWidget();
            //boolean applyToAllCells=bandWidget.getReportLayout().type()== EReportLayout.FREE;
            // perform
            if (edge == Edge.TOP_RIGHT || edge == Edge.BOTTOM_RIGHT || edge == Edge.RIGHT) {
                final int oldWidthCol = cell.getWidthCols();
                for (AdsReportSelectableWidget sibling : graggedItems) {
                    if (sibling.isSelected()) {
                        AdsReportWidget widget = sibling.getCell();
                        int pos = TxtUtils.snapToGrid(widget.getLeftColumn() + dXCols + widget.getWidthCols());
                        pos = Math.max(pos, widget.getLeftColumn() + AdsReportBand.GRID_SIZE_SYMBOLS);
                        final int newWidthCol = TxtUtils.roundToTenth(pos - widget.getLeftColumn());
                        widget.setWidthCols(newWidthCol);
                    }
                }
                xCol += (cell.getWidthCols() - oldWidthCol);
            } else {
                posXCols = Math.max(0, posXCols);
                if (edge == Edge.TOP_LEFT || edge == Edge.BOTTOM_LEFT || edge == Edge.LEFT) {
                    posXCols = Math.min(posXCols, cell.getLeftColumn() + cell.getWidthCols() - AdsReportBand.GRID_SIZE_SYMBOLS);
                    int dWidthCol = cell.getLeftColumn() - posXCols;
                    for (AdsReportSelectableWidget sibling : graggedItems) {
                        if (sibling.isSelected()) {
                            AdsReportWidget widget = sibling.getCell();
                            int newWidthCol = Math.max(widget.getWidthCols() + dWidthCol, AdsReportBand.GRID_SIZE_SYMBOLS);
                            newWidthCol = TxtUtils.roundToTenth(newWidthCol);
                            //double newWidthMm=MmUtils.roundToTenth(widget.getWidthMm() + dWidthMm);
                            if (newWidthCol > AdsReportBand.GRID_SIZE_SYMBOLS) {
                                final int newLeftMm = TxtUtils.roundToTenth(widget.getLeftColumn() - dWidthCol);
                                widget.setLeftColumn(newLeftMm);
                            }
                            widget.setWidthMm(newWidthCol);
                        }
                    }
                } else if (edge == null) {
                    int dCol = posXCols - cell.getLeftColumn();
                    for (AdsReportSelectableWidget sibling : graggedItems/*bandWidget.getCellWidgets()*/) {
                        if (sibling.isSelected()) {
                            final int newLeftCol = TxtUtils.roundToTenth(sibling.getCell().getLeftColumn() + dCol);
                            sibling.getCell().setLeftColumn(newLeftCol);
                        }
                    }
                }
            }

            if (edge == Edge.BOTTOM_LEFT || edge == Edge.BOTTOM_RIGHT || edge == Edge.BOTTOM) {
                final int oldHeightRows = cell.getHeightRows();
                for (AdsReportSelectableWidget sibling : graggedItems) {
                    if (sibling.isSelected()) {
                        AdsReportWidget widget = sibling.getCell();
                        int pos = TxtUtils.snapToGrid(widget.getTopRow() + dYRows + widget.getHeightRows());
                        pos = Math.max(pos, widget.getTopRow() + AdsReportBand.GRID_SIZE_SYMBOLS);
                        final int newHeightRow = TxtUtils.roundToTenth(pos - widget.getTopRow());
                        widget.setHeightRows(newHeightRow);
                    }
                }
                yRow += (cell.getHeightRows() - oldHeightRows);
            } else {
                posYRows = Math.max(0, posYRows);
                if (edge == Edge.TOP_LEFT || edge == Edge.TOP_RIGHT || edge == Edge.TOP) {
                    posYRows = Math.min(posYRows, cell.getTopRow() + cell.getHeightRows() - AdsReportBand.GRID_SIZE_SYMBOLS);
                    int dRows = cell.getTopRow() - posYRows;
                    for (AdsReportSelectableWidget sibling : graggedItems) {
                        if (sibling.isSelected()) {
                            AdsReportWidget widget = sibling.getCell();

                            int newHeightRows = Math.max(widget.getHeightRows() + dRows, AdsReportBand.GRID_SIZE_SYMBOLS);
                            newHeightRows = TxtUtils.roundToTenth(newHeightRows);
                            //final double newHeightMm = MmUtils.roundToTenth(widget.getHeightMm() + dMm);
                            if (newHeightRows > AdsReportBand.GRID_SIZE_SYMBOLS) {
                                final int newTopRow = TxtUtils.roundToTenth(widget.getTopRow() - dRows);
                                widget.setTopRow(newTopRow);
                            }
                            widget.setHeightRows(newHeightRows);
                        }
                    }
                } else if (edge == null) {
                    int dRows = posYRows - cell.getTopRow();
                    for (AdsReportSelectableWidget sibling : graggedItems/*bandWidget.getCellWidgets()*/) {
                        if (sibling.isSelected()) {
                            final int newTopRow = TxtUtils.roundToTenth(sibling.getCell().getTopRow() + dRows);
                            sibling.getCell().setTopRow(newTopRow);
                        }
                    }
                }
            }
            if (edge == null) {
                final IReportCellContainer ownerWidget = cellWidget.getOwnerWidget();
                insertionInfo = ((AdsReportBaseContainer) ownerWidget).calcInsertionPlace(cellWidget, insertionInfo, null);
            }
        }
    }

    private void prepareDragged() {
        final AdsReportBandWidget bandWidget = cellWidget.getOwnerBandWidget();
        final AdsReportBaseContainer parent = (AdsReportBaseContainer) cellWidget.getOwnerWidget();
        if (parent != null && !dragging) {
            dragging = true;
            graggedItems.clear();
            if (!parent.equals(bandWidget.bandSubWidget) && edge == null) {
                for (AdsReportSelectableWidget sibling : parent.getCellWidgets()) {
                    if (sibling.isSelected()) {
                        sibling.setDragged(true);
                        graggedItems.add(sibling);
                        ((Container) parent).remove(sibling);
                        ((AdsReportWidgetContainer) parent.getCell()).getWidgets().remove(sibling.getCell());

                        if (cellWidget.getDiagramMode() == AdsReportForm.Mode.GRAPHICS) {
                            sibling.getCell().setLeftMm(parent.getCell().getLeftMm() + sibling.getCell().getLeftMm());
                            sibling.getCell().setTopMm(parent.getCell().getTopMm() + sibling.getCell().getTopMm());
                        } else {
                            sibling.getCell().setLeftColumn(parent.getCell().getLeftColumn() + sibling.getCell().getLeftColumn());
                            sibling.getCell().setTopRow(parent.getCell().getTopRow() + sibling.getCell().getTopRow());
                        }
                        bandWidget.bandSubWidget.getReportWidgetContainer().getWidgets().add(sibling.getCell());
                        bandWidget.bandSubWidget.addCellWidget(cellWidget);
                    }
                }
                parent.getReportLayout().justifyLayout();
            } else {
                if (parent.getReportLayout().getType() == EReportLayout.FREE) {
                    for (AdsReportSelectableWidget sibling : parent.getCellWidgets()) {
                        if (sibling.isSelected()) {
                            sibling.setDragged(true);
                            graggedItems.add(sibling);
                        }
                    }
                } else {
                    graggedItems.add(cellWidget);
                }
                parent.getReportLayout().justifyLayout();
            }
        }
    }

    @Override
    protected void popup(final Component component, final int x, final int y) {
        if (!cellWidget.isSelected()) {
            AdsReportWidgetUtils.selectCell(cellWidget);
        }
        final JPopupMenu popupMenu = DialogUtils.createPopupMenu(cellWidget);
        if (cellWidget.getCell() != null && cellWidget.getCell().isReportContainer()) {
            popupMenu.addSeparator();
            for (EReportCellType cellType : EReportCellType.values()) {
                if (cellWidget.getOwnerBandWidget().getOwnerFormDiagram().getMode() == AdsReportForm.Mode.TEXT) {
                    switch (cellType) {
                        case IMAGE:
                        case DB_IMAGE:
                        case CHART:
                            continue;
                    }
                }
                popupMenu.add(new BandMouseListener.AddCellAction(cellWidget.getOwnerBandWidget(), (IReportWidgetContainer) cellWidget.getCell(), cellType, x, y));
            }
        }
        popupMenu.show(component, x, y);
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        dragging = false;
        for (AdsReportSelectableWidget w : graggedItems) {
            if (w.isDragged()) {
                w.setDragged(false);
            }
        }

        final IReportCellContainer ownerWidget = insertionInfo != null && insertionInfo.getParent() != null ? insertionInfo.getParent() : cellWidget.getOwnerWidget();
        if (ownerWidget != null) {
            if (edge == null) {//cell was moved
                ownerWidget.getReportLayout().updateIndex(cellWidget, insertionInfo);
                adjust(ownerWidget);
            } else {   //cell shape was changed
                ownerWidget.getReportLayout().changeSpan(cellWidget, oldWidth, oldHeight, edge == Edge.TOP, edge == Edge.LEFT);
                for (AdsReportSelectableWidget sibling : graggedItems) {
                    if (sibling.getCell().isReportContainer()) {
                        adjust((AdsReportCellContainerWidget) sibling);
                    }
                }
                //if(cellWidget instanceof AdsReportCellContainerWidget){
                //    adjust((AdsReportCellContainerWidget)cellWidget);
                //}              
            }
        }
        if (getUndoRedo() != null && undoRedo.isBlocked()) {
            undoRedo.block(false);
            cellWidget.getCell().getOwnerForm().setEditState(RadixObject.EEditState.MODIFIED);
        }
        insertionInfo = null;
    }

    private void adjust(final IReportCellContainer ownerWidget) {
        //ownerWidget.getReportLayout().updateIndex(cellWidget,insertionInfo);
        if (ownerWidget instanceof AdsReportBandWidget) {
            ((AdsReportBandWidget) ownerWidget).bandSubWidget.setInsertionPlaceRect(null);
            ((AdsReportBandWidget) ownerWidget).bandSubWidget.getReportLayout().justifyLayout();
        } else if (ownerWidget instanceof AdsReportBaseContainer) {
            ((AdsReportBaseContainer) ownerWidget).setInsertionPlaceRect(null);
            ((AdsReportBaseContainer) ownerWidget).getReportLayout().justifyLayout();
        }
    }
}
