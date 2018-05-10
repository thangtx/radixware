package org.radixware.kernel.designer.ads.editors.documentation;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.radixware.kernel.common.defs.ads.doc.AdsDocDef;
import org.radixware.kernel.common.defs.ads.doc.DocResource;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;

public class DocumentationTabbedPanel extends JTabbedPane {

    private DocumentationObjectPanel objectPanel;
    private DocumentationResourcePanel resourcePanel;

    public DocumentationTabbedPanel(AdsModule module) {

        // object
        objectPanel = new DocumentationObjectPanel(module);
        addTab(objectPanel, "Object");

        // resource
        resourcePanel = new DocumentationResourcePanel(module);
        addTab(resourcePanel, "Resource");
    }

    private void addTab(JPanel panel, String title) {
        panel.setAlignmentX(0.0f);
        this.addTab(title, panel);
    }

    public void update() {
        objectPanel.update();
        resourcePanel.update();
    }

    public void open(OpenInfo openInfo) {

        // object
        if (openInfo.getTarget() instanceof AdsDocDef) {
            setSelectedComponent(objectPanel);
            objectPanel.open(openInfo);
        }

        // resource
        if (openInfo.getTarget() instanceof DocResource) {
            setSelectedComponent(resourcePanel);
            resourcePanel.open(openInfo);
        }
    }

}

