package org.radixware.kernel.designer.ads.editors.clazz.report.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.utils.AdsReportMarginMm;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.utils.AdsReportMarginTxt;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.ads.editors.clazz.report.MarginPanel;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportFormDiagram;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

public class AdsReportMarginAction extends AbstractAction {
    private final AdsReportFormDiagram diagram;

    public AdsReportMarginAction(AdsReportFormDiagram diagram) {
        super("MarginAction", RadixWareIcons.REPORT.MARGIN.getIcon());
        this.diagram = diagram;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        final List<RadixObject> selectedObjects = diagram.getSelectedObjects();
        final List<AdsReportCell> cells = new ArrayList<>();
        for (RadixObject radixObject : selectedObjects) {
            if (radixObject instanceof AdsReportCell) {
                cells.add((AdsReportCell) radixObject);
            }
        }
        if (!cells.isEmpty()) {
            final AdsReportCell cell = cells.get(0);
            final MarginPanel panel = new MarginPanel();
            AdsReportMarginMm adsReportMarginMm = null;
            AdsReportMarginTxt adsReportMarginTxt = null;
            if (diagram.getMode() == AdsReportForm.Mode.TEXT){
                adsReportMarginTxt = cell.getMarginTxt().copy(false);
                panel.open(adsReportMarginTxt);
            } else {
                adsReportMarginMm = cell.getMarginMm().copy(false);
                panel.open(adsReportMarginMm);
            }
            if (!panel.isEnabled()){
                return;
            }
            final ModalDisplayer modalDisplayer = new ModalDisplayer(panel, "Margins");
            if (modalDisplayer.showModal()) {
                for (AdsReportCell c : cells){
                    if (adsReportMarginMm != null){
                        c.setMarginTopMm(adsReportMarginMm.getTopMm());
                        c.setMarginLeftMm(adsReportMarginMm.getLeftMm());
                        c.setMarginBottomMm(adsReportMarginMm.getBottomMm());
                        c.setMarginRightMm(adsReportMarginMm.getRightMm());
                    }
                    
                    if (adsReportMarginTxt != null){
                        c.setMarginTopRows(adsReportMarginTxt.getTopRows());
                        c.setMarginLeftCols(adsReportMarginTxt.getLeftCols());
                        c.setMarginBottomRows(adsReportMarginTxt.getBottomRows());
                        c.setMarginRightCols(adsReportMarginTxt.getRightCols());
                    }
                }
            }
        }
    }
    
}
