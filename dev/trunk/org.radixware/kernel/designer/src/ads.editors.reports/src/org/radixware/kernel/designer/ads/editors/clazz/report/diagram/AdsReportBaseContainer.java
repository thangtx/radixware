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

import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.IOException;
import java.util.List;
import java.util.*;
import javax.swing.SwingUtilities;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidget;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidgetContainer;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.IReportWidgetContainer;
import org.radixware.kernel.common.enums.EReportCellType;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.palette.AdsReportAddNewItemAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.palette.AdsReportPaletteDragAndDropHandler;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;

public class AdsReportBaseContainer extends AdsReportSelectableWidget implements DropTargetListener, IReportCellContainer {

    private ReportLayoutProcessor layout;
    public static final Color NAME_COLOR = new Color(230, 230, 230);
    protected IReportWidgetContainer widgetContainer;
    private final Map<AdsReportWidget, AdsReportSelectableWidget> cell2Widget = new HashMap<>();
    private Rectangle insertionPlaceRect = null;

    private final RadixObjects.ContainerChangesListener containerChangeListener = new RadixObjects.ContainerChangesListener() {

        @Override
        public void onEvent(final RadixObjects.ContainerChangedEvent e) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    update();
                    if (e.changeType == RadixObjects.EChangeType.ENLARGE) {
                        final AdsReportWidget cell = (AdsReportWidget) e.object;
                        final AdsReportSelectableWidget cellWidget = findCellWidget(cell);
                        if (cellWidget != null) {
                            AdsReportWidgetUtils.selectCell(cellWidget);
                            layout.justifyLayout();
                        }
                    }
                    repaint();
                }
            });
        }
    };

    public AdsReportBaseContainer(final AdsReportFormDiagram diagram, final AdsReportBand band) {
        super(diagram, band);
        init(band);
    }

    public AdsReportBaseContainer(final AdsReportFormDiagram diagram, final AdsReportWidgetContainer band) {
        super(diagram, band);
        init(band);
    }

    private void init(final IReportWidgetContainer band) {
        this.widgetContainer = band;
        setOpaque(true);
        band.getWidgets().getContainerChangesSupport().addEventListener(containerChangeListener);
        setDropTarget(new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this));
        layout = ReportLayoutProcessor.Factory.newInstance(this);
    }

    @Override
    public void updateLayout() {
        if (layout.getType() != widgetContainer.getLayout()) {
            layout = ReportLayoutProcessor.Factory.newInstance(this);
            removeOldCells();
            for (org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidget cell : widgetContainer.getWidgets()) {
                cell.setColumn(-2);
                cell.setRow(-2);
            }
            updateCells();
            layout.justifyLayout();
            fireSelectionChanged();
        }
    }

    @Override
    public IReportWidgetContainer getReportWidgetContainer() {
        return widgetContainer;
    }

    @Override
    public ReportLayoutProcessor getReportLayout() {
        return layout;
    }

    protected void removeOldCells() {
        final Iterator<Map.Entry<AdsReportWidget, AdsReportSelectableWidget>> iterator = cell2Widget.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<AdsReportWidget, AdsReportSelectableWidget> entry = iterator.next();
            final AdsReportWidget cell = entry.getKey();
            if (!widgetContainer.getWidgets().contains(cell)) {
                final AdsReportSelectableWidget cellWidget = entry.getValue();
                iterator.remove();
                remove(cellWidget);
                if (cellWidget.isSelected()) {
                    fireSelectionChanged();
                }
                layout.justifyLayout();
            }
        }
    }

    public AdsReportSelectableWidget findCellWidget(final AdsReportWidget cell) {
        return cell2Widget.get(cell);
    }

    public void addCellWidget(final AdsReportSelectableWidget cellWidget) {
        cell2Widget.put(cellWidget.getCell(), cellWidget);
        add(cellWidget);
    }

    private AdsReportSelectableWidget findOrCreateCellWidget(final org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidget cell) {
        AdsReportSelectableWidget result = findCellWidget(cell);
        if (result == null) {
            Point point = new Point(MmUtils.mm2px(cell.getLeftMm()), MmUtils.mm2px(cell.getTopMm()));
            result = AdsReportSelectableWidget.Factory.newInstance(getDiagram(), cell);
            if (cell.getColumn() == -1 || cell.getRow() == -1) {
                ReportLayoutProcessor.InsertionInfo info = layout.indexFromPoint(point, result);
                layout.updateIndex(result, info);
            }
            cell2Widget.put(cell, result);
            add(result);
        } else if (cell.getColumn() == -2 || cell.getRow() == -2) {
            Point point = new Point(MmUtils.mm2px(cell.getLeftMm()), MmUtils.mm2px(cell.getTopMm()));
            ReportLayoutProcessor.InsertionInfo info = layout.indexFromPoint(point, result);
            layout.updateIndex(result, info);
        }
        return result;
    }

    protected void updateCells() {
        if (widgetContainer != null && widgetContainer.getWidgets() != null && !widgetContainer.getWidgets().isEmpty()) {
            for (org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidget cell : widgetContainer.getWidgets()) {
                final AdsReportSelectableWidget cellWidget = findOrCreateCellWidget(cell);
                cellWidget.update();
            }
        }
    }

    private void paintName(final Graphics g) {
        final Font font = getFont().deriveFont(Font.BOLD, MmUtils.mm2px(7));
        g.setFont(font);
        final FontMetrics fontMetrics = getFontMetrics(font);
        g.setColor(NAME_COLOR);
        final String bandName = widgetContainer.getName();
        g.drawString(bandName, getWidth() / 2 - fontMetrics.stringWidth(bandName) / 2, fontMetrics.getHeight());
    }

    private void paintGrid(final Graphics g) {
        if (!AdsReportFormDiagramOptions.getDefault().isShowGrid()) {
            return;
        }

        if (getDiagramMode() == AdsReportForm.Mode.GRAPHICS) {

            g.setColor(Color.GRAY);

            final int widthPx = getWidth();
            final int heightPx = getHeight();

            for (double xMm = AdsReportBand.GRID_SIZE_MM; true; xMm += AdsReportBand.GRID_SIZE_MM) {
                final int x = MmUtils.mm2px(xMm);
                if (x >= widthPx) {
                    break;
                }
                for (double yMm = AdsReportBand.GRID_SIZE_MM; true; yMm += AdsReportBand.GRID_SIZE_MM) {
                    final int y = MmUtils.mm2px(yMm);
                    if (y >= heightPx) {
                        break;
                    }
                    g.fillRect(x, y, 1, 1);
                }
            }
        } else {

            final int widthPx = getWidth();
            final int heightPx = getHeight();

            Graphics2D g2d = (Graphics2D) g.create();
            try {
                g2d.setColor(Color.GRAY);

                for (int xCol = AdsReportBand.GRID_SIZE_SYMBOLS; true; xCol += AdsReportBand.GRID_SIZE_SYMBOLS) {
                    final int x = TxtUtils.columns2Px(xCol);
                    if (x >= widthPx) {
                        break;
                    }

                    for (int yRow = AdsReportBand.GRID_SIZE_SYMBOLS; true; yRow += AdsReportBand.GRID_SIZE_SYMBOLS) {
                        final int y = TxtUtils.rows2Px(yRow);
                        if (y >= heightPx) {
                            break;
                        }

                        g2d.fillRect(x, y, 1, 1);
                    }

                }

            } finally {
                g2d.dispose();
            }
        }
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        paintBackground(g);
        paintName(g);
        paintGrid(g);
    }

    protected void hightlinghInsertionPlaceRect(final Graphics2D g2) {
        if (insertionPlaceRect != null) {
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLUE);
            g2.drawRect(insertionPlaceRect.x, insertionPlaceRect.y, insertionPlaceRect.width, insertionPlaceRect.height);
            g2.setStroke(new BasicStroke());
        }
    }

    public void setInsertionPlaceRect(final Rectangle rect) {
        if (isDragExit) {
            return;
        }

        insertionPlaceRect = rect;
        if (rect == null) {
            this.repaint();
        }
    }

    public AdsReportFormDiagram getOwnerFormDiagram() {
        final AdsReportBandWidget ownerBandWidget = getOwnerBandWidget();
        if (ownerBandWidget != null) {
            return ownerBandWidget.getOwnerFormDiagram();
        } else {
            return null;
        }
    }

    @Override
    public Collection<AdsReportSelectableWidget> getCellWidgets() {
        return cell2Widget.values();
    }

    @SuppressWarnings("rawtypes")
    private AdsReportAddNewItemAction findItemFactory(final Transferable t) {
        if (t.isDataFlavorSupported(AdsReportPaletteDragAndDropHandler.REPORT_PALETTE_DATA_FLAVOR)) {
            try {
                return (AdsReportAddNewItemAction) t.getTransferData(AdsReportPaletteDragAndDropHandler.REPORT_PALETTE_DATA_FLAVOR);
            } catch (UnsupportedFlavorException | IOException cause) {
                throw new IllegalStateException(cause);
            }
        }

        final List<ClipboardSupport.Transfer> transfers = ClipboardSupport.getTransfers(t);
        if (transfers != null && transfers.size() == 1) {
            final ClipboardSupport.Transfer transfer = transfers.get(0);
            final RadixObject radixObject = transfer.getInitialObject();
            if (radixObject instanceof AdsPropertyDef) {
                final AdsPropertyDef prop = (AdsPropertyDef) radixObject;
                final AdsReportClassDef report = widgetContainer.getOwnerForm().getOwnerReport();
                if (prop.getOwnerClass() == report) {
                    return AdsReportAddNewItemAction.Factory.newAddPropertyCellAction(prop.getId());
                }
            }
        }

        return null;
    }

    private boolean isDragExit = false;//учитывается при подсвечивании места вставки новой ячейки

    @Override
    public void dragEnter(final DropTargetDragEvent e) {
        final AdsReportAddNewItemAction newItemAction = findItemFactory(e.getTransferable());
        if (newItemAction != null) {
            if (newItemAction instanceof AdsReportAddNewItemAction.AddNewCellAction) {
                EReportCellType cellType = ((AdsReportAddNewItemAction.AddNewCellAction) newItemAction).getCellType();
                if (getDiagram().getMode() == AdsReportForm.Mode.TEXT) {
                    switch (cellType) {
                        case IMAGE:
                        case DB_IMAGE:
                        case CHART:
                            setCursor(DragSource.DefaultMoveNoDrop);
                            e.rejectDrag();
                            return;
                    }
                }
            }
            setCursor(DragSource.DefaultMoveDrop);
        } else {
            setCursor(DragSource.DefaultMoveNoDrop);
            e.rejectDrag();
        }
    }

    @Override
    public void dragExit(final DropTargetEvent e) {
        setInsertionPlaceRect(null);
        isDragExit = true;
        setCursor(Cursor.getDefaultCursor());
    }

    @Override
    public void dragOver(final DropTargetDragEvent e) {
        isDragExit = false;
        ReportLayoutProcessor.InsertionInfo info = calcInsertionPlace(null, null, e.getLocation());
        if (!equals(info.getParent())) {
            setInsertionPlaceRect(null);
            return;
        }
        Rectangle oldRect = insertionPlaceRect != null ? new Rectangle(insertionPlaceRect) : null;
        if (oldRect != null && insertionPlaceRect != null
                && !(oldRect.x == insertionPlaceRect.x && oldRect.y == insertionPlaceRect.y
                && oldRect.height == insertionPlaceRect.height && oldRect.width == insertionPlaceRect.width)) {
            setInsertionPlaceRect(null);
        }
        Graphics2D g2 = (Graphics2D) this.getGraphics();
        hightlinghInsertionPlaceRect(g2);
    }

    @Override
    public void drop(final DropTargetDropEvent e) {
        setInsertionPlaceRect(null);
        setCursor(Cursor.getDefaultCursor());

        final AdsReportAddNewItemAction newItemAction = findItemFactory(e.getTransferable());
        if (newItemAction != null) {
            e.dropComplete(true);
            boolean wasAdd = false;
            try {
                blockUndoRedo(true);
                wasAdd = newItemAction.addNewItem(widgetContainer, e.getLocation().x, e.getLocation().y);
                EditorsManager.getDefault().open(widgetContainer.getOwnerForm().getOwnerReport()); // switch focuce to editor from pallette
            } finally {
                blockUndoRedo(false);
                if (wasAdd) {
                    widgetContainer.getOwnerForm().setEditState(RadixObject.EEditState.MODIFIED);
                }
            }
        } else {
            e.dropComplete(false);
        }
    }

    void blockUndoRedo(final boolean block) {
        if (getOwnerFormDiagram() != null && getOwnerFormDiagram().getUndoRedo() != null
                && block != getOwnerFormDiagram().getUndoRedo().isBlocked()) {
            getOwnerFormDiagram().getUndoRedo().block(block);
        }
    }

    @Override
    public void dropActionChanged(final DropTargetDragEvent dtde) {
    }

    @Override
    public Collection<AdsReportSelectableWidget> getSelectedWidgets() {
        List<AdsReportSelectableWidget> result = new ArrayList<>();
        for (AdsReportSelectableWidget widget : getCellWidgets()) {
            if (widget.isSelected()) {
                result.add(widget);
            }
            if (widget instanceof AdsReportCellContainerWidget) {
                result.addAll(((AdsReportCellContainerWidget) widget).getSelectedWidgets());
            }
        }
        return result;
    }

    public ReportLayoutProcessor.InsertionInfo calcInsertionPlace(final AdsReportSelectableWidget cellWidget,
            final ReportLayoutProcessor.InsertionInfo insertionInfo, Point point) {
        if (point == null && cellWidget != null) {
            point = new Point(MmUtils.mm2px(cellWidget.getCell().getLeftMm()), MmUtils.mm2px(cellWidget.getCell().getTopMm()));
        }
        ReportLayoutProcessor.InsertionInfo newInfo = getReportLayout().indexFromPoint(point, cellWidget);

        if (insertionInfo != null && newInfo != null && cellWidget != null) {
            AdsReportBaseContainer parent = insertionInfo.getParent() == null ? (AdsReportBaseContainer) cellWidget.getOwnerWidget() : insertionInfo.getParent();
            AdsReportBaseContainer newParent = newInfo.getParent() == null ? (AdsReportBaseContainer) cellWidget.getOwnerWidget() : newInfo.getParent();
            if (newParent != null && parent != null && !newParent.equals(parent)) {
                parent.setInsertionPlaceRect(null);
            }
        }
        //insertionInfo=newInfo;
        if (newInfo != null) {
            AdsReportBaseContainer p = newInfo.getParent() == null && cellWidget != null ? (AdsReportBaseContainer) cellWidget.getOwnerWidget() : newInfo.getParent();
            if (p != null) {
                if (cellWidget != null)//если перетаскиваем существующую ячейку, а не добавляем новую
                {
                    p.isDragExit = false;
                }
                p.getReportLayout().highlightInsertPlace(newInfo);
            }
        }
        return newInfo;
    }

}
