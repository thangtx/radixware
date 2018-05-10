package org.radixware.kernel.designer.ads.editors.clazz.report.diagram.selection;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidget;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportAbstractSelectableWidget;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportBandWidget;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportBaseContainer;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportFormDiagram;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportFormUndoRedo;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportSelectableWidget;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.IReportCellContainer;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.MmUtils;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.ReportLayoutProcessor;

public class SelectableWidgetKeyListener  implements KeyListener{
    private final AdsReportFormDiagram formDiagram;
    private Map<AdsReportSelectableWidget, ReportLayoutProcessor.InsertionInfo> insertionInfos = new HashMap<>();
    private List<AdsReportSelectableWidget> moveWidgets = new ArrayList<>();

    public SelectableWidgetKeyListener(AdsReportFormDiagram formDiagram) {
        this.formDiagram = formDiagram;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (formDiagram == null){
            return;
        }
        int code = e.getKeyCode();
        if (code != KeyEvent.VK_DOWN
                && code != KeyEvent.VK_UP
                && code != KeyEvent.VK_LEFT
                && code != KeyEvent.VK_RIGHT
                || e.isAltDown() || e.isShiftDown() && e.isControlDown()) {
            return;
        }
        AdsReportFormUndoRedo undoRedo = formDiagram.getUndoRedo();
        if (undoRedo != null && !undoRedo.isBlocked()) {
            undoRedo.block(true);
        }

        List<AdsReportAbstractSelectableWidget> widgets = formDiagram.getSelectedWidgets();
        if (widgets.isEmpty()){
            return;
        }
        
        moveWidgets.clear();
        AdsReportForm form = formDiagram.getForm();
        if (formDiagram.getDiagramMode() == AdsReportForm.Mode.GRAPHICS) {
            double step = form != null ? form.getGridSizeMm() : AdsReportForm.DEFAULT_GRID_SIZE_MM;
            double dMmX = 0;
            double dMmY = 0;
            switch (code) {
                case KeyEvent.VK_DOWN:
                    if (e.isShiftDown()) {
                        dMmY += 5 * step;
                    } else if (e.isControlDown()) {
                        dMmY += 2 * step;
                    } else {
                        dMmY += step;
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (e.isShiftDown()) {
                        dMmY -= 5 * step;
                    } else if (e.isControlDown()) {
                        dMmY -= 2 * step;
                    } else {
                        dMmY -= step;
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (e.isShiftDown()) {
                        dMmX -= 5 * step;
                    } else if (e.isControlDown()) {
                        dMmX -= 2 * step;
                    } else {
                        dMmX -= step;
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (e.isShiftDown()) {
                        dMmX += 5 * step;
                    } else if (e.isControlDown()) {
                        dMmX += 2 * step;
                    } else {
                        dMmX += step;
                    }
                    break;
            }
            for (AdsReportAbstractSelectableWidget widget : widgets){
                if (widget instanceof AdsReportSelectableWidget && widget.isSelected()){
                    AdsReportSelectableWidget cellWidget = (AdsReportSelectableWidget) widget;
                    moveWidgets.add(cellWidget);
                    AdsReportWidget cell = cellWidget.getCell();
                    double newLeftMm = Math.max(0, cell.getLeftMm() + dMmX);
                    newLeftMm = MmUtils.roundToTenth(newLeftMm);
                    cell.setLeftMm(newLeftMm);
                    
                    double newTopMm = Math.max(0, cell.getTopMm() + dMmY);
                    newTopMm = MmUtils.roundToTenth(newTopMm);
                    cell.setTopMm(newTopMm);
                    final IReportCellContainer ownerWidget = cellWidget.getOwnerWidget();
                    ReportLayoutProcessor.InsertionInfo insertionInfo = ((AdsReportBaseContainer) ownerWidget).calcInsertionPlace(cellWidget, insertionInfos.get(cellWidget), null);
                    if (insertionInfo != null){
                        insertionInfos.put(cellWidget, insertionInfo);
                    } else {
                        insertionInfos.remove(cellWidget);
                    }
                }
            }
            
        }
        e.consume();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (formDiagram == null){
            return;
        }
        for (AdsReportSelectableWidget w : moveWidgets) {
            if (w.isDragged()) {
                w.setDragged(false);
            }
        }
        for (AdsReportSelectableWidget cellWidget : insertionInfos.keySet()) {
            ReportLayoutProcessor.InsertionInfo insertionInfo = insertionInfos.get(cellWidget);
            final IReportCellContainer ownerWidget = insertionInfo != null && insertionInfo.getParent() != null ? insertionInfo.getParent() : cellWidget.getOwnerWidget();
            if (ownerWidget != null) {
                ownerWidget.getReportLayout().updateIndex(cellWidget, insertionInfo);
                AdsReportBaseContainer container;
                if (ownerWidget instanceof AdsReportBandWidget) {
                    container = ((AdsReportBandWidget) ownerWidget).getBandSubWidget();
                } else {
                    container = (AdsReportBaseContainer) ownerWidget;
                }
                container.setInsertionPlaceRect(null);
                container.getReportLayout().justifyLayout();
            }
        }
        
        AdsReportFormUndoRedo undoRedo = formDiagram.getUndoRedo();
        if (undoRedo != null && !undoRedo.isBlocked()) {
            undoRedo.block(false);
            formDiagram.getForm().setEditState(RadixObject.EEditState.MODIFIED);
        }
        insertionInfos.clear();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
