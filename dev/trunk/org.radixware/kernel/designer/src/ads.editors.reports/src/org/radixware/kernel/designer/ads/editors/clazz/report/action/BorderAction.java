package org.radixware.kernel.designer.ads.editors.clazz.report.action;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportAbstractAppearance.Border;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.ads.editors.clazz.report.appearance.AdsReportBorderPanel;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportFormDiagram;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

public class BorderAction extends AbstractAction {
    private final AdsReportFormDiagram diagram;

    public BorderAction(final AdsReportFormDiagram diagram) {
        super("Borders", RadixWareIcons.BORDER.ALL_BORDER.getIcon());
        this.diagram = diagram;
    }

    @Override
    public boolean isEnabled() {
        if (diagram != null && diagram.getMode() == AdsReportForm.Mode.TEXT) {
            return false;
        }
        return super.isEnabled(); 
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (!isEnabled()){
            return;
        }
        final List<RadixObject> selectedObjects = diagram.getSelectedObjects();
        final List<AdsReportCell> cells = new ArrayList<>();
        for (RadixObject radixObject : selectedObjects) {
            if (radixObject instanceof AdsReportCell) {
                cells.add((AdsReportCell) radixObject);
            }
        }
        if (!cells.isEmpty()) {
            final AdsReportCell cell = cells.get(0);
            final AdsReportBorderPanel borderPanel = new AdsReportBorderPanel();
            borderPanel.open(cell.getBorder());
            final ModalDisplayer modalDisplayer = new ModalDisplayer(borderPanel, "Borders");
            if (modalDisplayer.showModal()) {
                for (AdsReportCell c : cells){
                    borderPanel.apply(c);
                }
            }

        }
    }
}
