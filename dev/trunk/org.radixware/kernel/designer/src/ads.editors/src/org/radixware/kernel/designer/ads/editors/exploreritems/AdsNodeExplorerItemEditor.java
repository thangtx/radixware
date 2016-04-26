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

package org.radixware.kernel.designer.ads.editors.exploreritems;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.common.AdsCondition;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsNodeExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsSelectorExplorerItemDef;
import org.radixware.kernel.designer.ads.editors.presentations.ConditionsPanel;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.IRadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


public class AdsNodeExplorerItemEditor extends RadixObjectEditor<AdsNodeExplorerItemDef> {

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<AdsNodeExplorerItemDef> {

        @Override
        public IRadixObjectEditor<AdsNodeExplorerItemDef> newInstance(AdsNodeExplorerItemDef node) {
            return new AdsNodeExplorerItemEditor(node);
        }
    }

    /**
     * Creates new form AdsEntityExplorerItemEditorView
     */
    public AdsNodeExplorerItemEditor(AdsNodeExplorerItemDef node) {
        super(node);

        initComponents();
        commonScroll = new JScrollPane();
        mainTabs = new JTabbedPane();
        commonPage = new JPanel();
        commonPage.setLayout(new BorderLayout());
        commonScroll.setViewportView(commonPage);


        ChangeListener itemListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                final AdsNodeExplorerItemDef node = getNodeExplorerItem();
                AdsNodeExplorerItemEditor.this.inheritedPanel.open(node);
                AdsNodeExplorerItemEditor.this.conditionPanel.update();
            }
        };
        itemPropertiesPanel.addChangeListener(itemListener);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    private JScrollPane commonScroll;
    private JTabbedPane mainTabs;
    private JPanel commonPage;
    private InheritedComponentsPanel inheritedPanel = new InheritedComponentsPanel();
    private ItemPropertiesPanel itemPropertiesPanel = new ItemPropertiesPanel();
    private ConditionsPanel conditionPanel;

    public AdsNodeExplorerItemDef getNodeExplorerItem() {
        return getRadixObject();
    }

    @Override
    public boolean open(OpenInfo openInfo) {
        final AdsNodeExplorerItemDef node = getNodeExplorerItem();

        removeAll();
        commonPage.removeAll();
        mainTabs.removeAll();

        commonPage.add(itemPropertiesPanel, BorderLayout.NORTH);
        itemPropertiesPanel.open(node);
        commonPage.add(inheritedPanel, BorderLayout.CENTER);
        inheritedPanel.open(node);

        if (node instanceof AdsSelectorExplorerItemDef) {
            add(mainTabs, BorderLayout.CENTER);
            mainTabs.add(NbBundle.getMessage(AdsNodeExplorerItemEditor.class, "ExplorerItem-CommonTip"), commonScroll);

            conditionPanel = new ConditionsPanel();
            conditionPanel.open((AdsSelectorExplorerItemDef) node);
            mainTabs.add(conditionPanel.getName(), conditionPanel);

            RadixObject target = openInfo.getTarget();
            AdsCondition condition = ((AdsSelectorExplorerItemDef) node).getCondition();
            if (target.equals(condition.getFrom())
                    || target.equals(condition.getWhere())
                    || condition.getFrom().isParentOf(target)
                    || condition.getWhere().isParentOf(target)) {
                mainTabs.setSelectedComponent(conditionPanel);
            }
        } else {
            add(commonScroll, BorderLayout.CENTER);
        }

        return super.open(openInfo);
    }

    @Override
    public void update() {
        itemPropertiesPanel.update();
        inheritedPanel.update();
        if (conditionPanel != null) {
            conditionPanel.update();
        }
    }
}
