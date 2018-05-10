package org.radixware.kernel.designer.ads.editors.clazz.report.action;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportFormDiagram;

public class AdsReportVisibilityAction extends AbstractAction {
    
    private final AdsReportFormDiagram diagram;
    private final AdsReportForm.Mode mode;

    public AdsReportVisibilityAction(AdsReportFormDiagram diagram, AdsReportForm.Mode mode) {
        this.diagram = diagram;
        this.mode = mode;
        if (mode == null){
            putValue(Action.NAME, "Always");
            putValue(Action.SMALL_ICON, RadixWareIcons.REPORT.SHOW_ALWAYS.getIcon());
        } else if (mode == AdsReportForm.Mode.TEXT){
            putValue(Action.NAME, "In Text Mode Only");
            putValue(Action.SMALL_ICON, RadixWareIcons.REPORT.SHOW_TEXT.getIcon());
        } else if (mode == AdsReportForm.Mode.GRAPHICS){
            putValue(Action.NAME, "In Graphical Mode Mnly");
            putValue(Action.SMALL_ICON, RadixWareIcons.REPORT.SHOW_GRAPHICS.getIcon());
        }
    }
    
    

    @Override
    public void actionPerformed(ActionEvent e) {
        final List<RadixObject> selectedObjects = diagram.getSelectedObjects();
        for (RadixObject radixObject : selectedObjects) {
            if (radixObject instanceof AdsReportCell) {
                final AdsReportCell cell = (AdsReportCell) radixObject;
                cell.setPreferredMode(mode);
            }
        }
    }
    
}
