package org.radixware.kernel.designer.ads.editors.clazz.report.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.enums.EReportTextFormat;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportFormDiagram;

public class TextFormatAction extends AbstractAction {

    private final AdsReportFormDiagram diagram;
    private final EReportTextFormat format;

    public TextFormatAction(AdsReportFormDiagram diagram, EReportTextFormat format) {
        super(format.getValue() + " Text");
        setEnabled(enabled);
        this.diagram = diagram;
        this.format = format;
        switch(format){
            case PLAIN:
                putValue(Action.SMALL_ICON, RadixWareIcons.REPORT.PLAIN_TEXT.getIcon());
                break;
            case RICH:
                putValue(Action.SMALL_ICON, RadixWareIcons.MLSTRING_EDITOR.VIEW_HTML.getIcon());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final List<RadixObject> selectedObjects = diagram.getSelectedObjects();
        for (RadixObject radixObject : selectedObjects) {
            if (radixObject instanceof AdsReportCell) {
                final AdsReportCell cell = (AdsReportCell) radixObject;
                cell.setTextFormat(format);
            }
        }
    }

}
