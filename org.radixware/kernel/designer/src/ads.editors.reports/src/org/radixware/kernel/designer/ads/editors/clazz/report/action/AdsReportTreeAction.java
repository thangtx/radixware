package org.radixware.kernel.designer.ads.editors.clazz.report.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JSplitPane;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportFormDiagram;
import org.radixware.kernel.designer.ads.editors.clazz.report.navigator.ReportNavigator;

public class AdsReportTreeAction extends AbstractAction {
    private AdsReportFormDiagram diagram;
    private JSplitPane jSplitPane;
    private ReportNavigator navigator;
    private int location;

    public AdsReportTreeAction(AdsReportFormDiagram diagram, JSplitPane jSplitPane, ReportNavigator navigator) {
        this.diagram = diagram;
        this.jSplitPane = jSplitPane;
        this.navigator = navigator;
        location = jSplitPane.getDividerLocation();
        this.putValue(AbstractAction.SMALL_ICON, AdsDefinitionIcon.WIDGETS.TREE.getIcon());
        this.putValue(AbstractAction.NAME, "");
        this.putValue(AbstractAction.SHORT_DESCRIPTION, "Open Form Tree");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AbstractButton abstractButton = (AbstractButton) e.getSource();
        boolean selected = abstractButton.getModel().isSelected();
        if (selected){
            jSplitPane.setLeftComponent(navigator);
            jSplitPane.setDividerLocation(location);
            navigator.open(diagram);
        } else {
            location = jSplitPane.getDividerLocation();
            jSplitPane.setLeftComponent(null);
        }
        
        
    }
    
}
