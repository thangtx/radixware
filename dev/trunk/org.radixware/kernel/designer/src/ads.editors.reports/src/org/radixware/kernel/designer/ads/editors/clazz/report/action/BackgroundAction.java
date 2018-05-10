package org.radixware.kernel.designer.ads.editors.clazz.report.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportAbstractAppearance;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.ads.editors.clazz.report.AdsReportBackgroundPanel;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportFormDiagram;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

public class BackgroundAction extends AbstractAction{
    private final AdsReportFormDiagram diagram;

    public BackgroundAction(AdsReportFormDiagram diagram) {
        super("Background", RadixWareIcons.EDIT.BACKGROUND.getIcon());
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
            final AdsReportBackgroundPanel panel = new AdsReportBackgroundPanel();
            panel.open(cell);
            final ModalDisplayer modalDisplayer = new ModalDisplayer(panel, "Background");
            if (modalDisplayer.showModal()) {
                for (AdsReportCell c : cells){
                    panel.apply(c);
                }
            }
        }
    }
    
    @Override
    public boolean isEnabled() {
        if (diagram != null && diagram.getMode() == AdsReportForm.Mode.TEXT) {
            return false;
        }
        return super.isEnabled();
    }
}
