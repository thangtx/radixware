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

/*
 * ItemPropertiesPanel.java
 *
 * Created on Apr 27, 2009, 6:47:26 PM
 */
package org.radixware.kernel.designer.ads.editors.exploreritems;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsChildRefExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsEntityExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsNodeExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParentRefExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsSelectorExplorerItemDef;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.designer.ads.common.dialogs.AccessEditPanel;
import org.radixware.kernel.designer.ads.editors.base.EnvSelectorPanel;
import org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel;


public class ItemPropertiesPanel extends javax.swing.JPanel {

    private AdsNodeExplorerItemDef node;
    private JCheckBox showInTreeCheck = new JCheckBox(NbBundle.getMessage(ItemPropertiesPanel.class, "NodeItem-ShowInTreeTip"));
    private DefinitionLinkEditPanel classEditor = new DefinitionLinkEditPanel();
    private EditorPresentationsChooser editorPresentationsChooser = new EditorPresentationsChooser();
    private DefinitionLinkEditPanel referenceViewer = new DefinitionLinkEditPanel();
    private ChooseExplorerItemTargetPanel targetPanel = new ChooseExplorerItemTargetPanel();
    private AccessEditPanel accessPanel = new AccessEditPanel();
    private EnvSelectorPanel envSelector = new EnvSelectorPanel();
    private GridBagLayout gbl = new GridBagLayout();
    private GridBagConstraints c = new GridBagConstraints();

    /**
     * Creates new form ItemPropertiesPanel
     */
    public ItemPropertiesPanel() {
        initComponents();
        setLayout(gbl);
        ActionListener showListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!ItemPropertiesPanel.this.isUpdate) {
                    boolean isSelected = ItemPropertiesPanel.this.showInTreeCheck.isSelected();
                    ItemPropertiesPanel.this.node.setVisibleInTree(isSelected);
                }
            }
        };
        showInTreeCheck.addActionListener(showListener);
        ChangeListener targetListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ItemPropertiesPanel.this.onTargetChanged();
            }
        };
        targetPanel.addChangeListener(targetListener);
    }

    private void onTargetChanged() {
        if (!isUpdate) {
            if (node instanceof AdsSelectorExplorerItemDef) {
                classEditor.open(node.findReferencedEntityClass(), node.getClassId());
                if (node.getClass().equals(AdsChildRefExplorerItemDef.class)) {
                    referenceViewer.open(((AdsChildRefExplorerItemDef) node).findChildReference(), ((AdsChildRefExplorerItemDef) node).getChildReferenceId());
                }
            } else {
                referenceViewer.open(((AdsParentRefExplorerItemDef) node).findParentReference(), ((AdsParentRefExplorerItemDef) node).getParentReferenceId());
            }
            changeSupport.fireChange();
        }
    }

    private void setupCommonLook() {
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.gridwidth = 1;
        c.insets = new Insets(10, 10, 0, 10);
        gbl.setConstraints(showInTreeCheck, c);
        add(showInTreeCheck);
        c.gridy++;

        AdsExplorerItemDef ovr = node.getHierarchy().findOverridden().get();
        if (ovr != null) {
            JLabel label = new JLabel("Overrides: ");
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0.0;
            c.weighty = 0.0;
            c.gridwidth = 1;
            c.gridx = 2;
            c.insets = new Insets(10, 10, 0, 0);
            gbl.setConstraints(label, c);
            add(label);

            DefinitionLinkEditPanel link = new DefinitionLinkEditPanel();
            link.open(ovr, ovr.getId());
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            c.weighty = 0.0;
            c.gridx = 3;
            c.gridwidth = 2;
            c.insets = new Insets(10, 10, 0, 10);
            gbl.setConstraints(link, c);
            add(link);
            c.gridy++;
        }
        ovr = node.getHierarchy().findOverwritten().get();
        if (ovr != null) {
            JLabel label = new JLabel("Overwrites: ");
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0.0;
            c.weighty = 0.0;
            c.gridx = 2;
            c.gridwidth = 1;
            c.insets = new Insets(10, 10, 0, 0);
            gbl.setConstraints(label, c);
            add(label);
            DefinitionLinkEditPanel link = new DefinitionLinkEditPanel();
            //link.open(ovr, ovr.getId());            
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            c.weighty = 0.0;
            c.gridwidth = 2;
            c.gridx = 3;
            c.insets = new Insets(10, 10, 0, 10);
            gbl.setConstraints(link, c);
            add(link);
            c.gridy++;
        }
        c.gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 0;
        c.anchor = GridBagConstraints.WEST;        
        c.weightx = 0.0;
        c.weighty = 0.0;
        javax.swing.JLabel accessLabel = new javax.swing.JLabel(NbBundle.getMessage(ItemPropertiesPanel.class, "AccessBoxTip"));
        gbl.setConstraints(accessLabel, c);
        add(accessLabel);
        
        c.gridx = 1;
        c.insets = new Insets(10, 0, 0, 10);
        gbl.setConstraints(accessPanel, c);
        add(accessPanel);
        c.gridy++;        
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy++;
        if (!node.getClass().equals(AdsParentRefExplorerItemDef.class)) {
            JLabel classLabel = new JLabel(NbBundle.getMessage(ItemPropertiesPanel.class, "NodeItem-ClassTip"));
            c.insets = new Insets(10, 10, 0, 10);
            c.gridx = 0;
            c.gridwidth = GridBagConstraints.RELATIVE;
            c.weightx = 0.0;
            c.weighty = 0.0;
            gbl.setConstraints(classLabel, c);
            add(classLabel);

            c.gridx = 1;
            c.insets = new Insets(10, 0, 0, 10);
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.weightx = 1.0;
            c.weighty = 0.0;
            gbl.setConstraints(classEditor, c);
            classEditor.open(node.findReferencedEntityClass(), node.getClassId());
            classEditor.setEnabled(!readonly);
            add(classEditor);
            c.gridy++;
        }

        c.gridx = 0;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.insets = new Insets(10, 10, 0, 10);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gbl.setConstraints(envSelector, c);
        add(envSelector);
        c.gridy++;

        targetPanel.open(node);
        targetPanel.setEnabled(!readonly);
        c.gridwidth = 1;
        JLabel commonlabel = new JLabel(targetPanel.getLabelText() + ":");
        c.gridx = 0;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.insets = new Insets(10, 10, 0, 10);
        gbl.setConstraints(commonlabel, c);
        add(commonlabel);

        c.gridx = 1;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 0, 10);
        gbl.setConstraints(targetPanel, c);
        add(targetPanel);
    }

    private void setupSelectorItemLook() {
        c.gridx = 0;
        if (node.getClass().equals(AdsChildRefExplorerItemDef.class)) {
            c.gridy ++;
            JLabel refLabel = new JLabel(NbBundle.getMessage(ItemPropertiesPanel.class, "RefExplorerItem-Title"));
            c.gridwidth = GridBagConstraints.RELATIVE;
            c.weightx = 0.0;
            c.weighty = 0.0;
            c.insets = new Insets(10, 10, 0, 10);
            gbl.setConstraints(refLabel, c);
            add(refLabel);

            c.gridx = 1;
            c.weightx = 1.0;
            c.weighty = 0.0;
            c.insets = new Insets(10, 0, 0, 10);
            c.gridwidth = GridBagConstraints.REMAINDER;
            gbl.setConstraints(referenceViewer, c);
            add(referenceViewer);

            AdsChildRefExplorerItemDef item = (AdsChildRefExplorerItemDef) node;
            referenceViewer.open(item.findChildReference(), item.getChildReferenceId());
        }
    }

    private void setupParentRefLook() {
        AdsParentRefExplorerItemDef item = (AdsParentRefExplorerItemDef) node;

        JLabel refLabel = new JLabel(NbBundle.getMessage(ItemPropertiesPanel.class, "RefExplorerItem-Title"));
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.gridx = 0;
        c.gridy ++;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.insets = new Insets(10, 10, 0, 10);
        gbl.setConstraints(refLabel, c);
        add(refLabel);

        c.gridx = 1;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.insets = new Insets(10, 0, 0, 10);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gbl.setConstraints(referenceViewer, c);
        add(referenceViewer);

        referenceViewer.open(item.findParentReference(), item.getParentReferenceId());
        referenceViewer.setEnabled(!readonly);

        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy ++;
        c.insets = new Insets(10, 10, 0, 10);
        gbl.setConstraints(editorPresentationsChooser, c);
        add(editorPresentationsChooser);

        editorPresentationsChooser.open(item);
    }
    private boolean readonly = false;
    private boolean isUpdate = false;

    public void open(final AdsNodeExplorerItemDef node) {
        this.node = node;
        this.readonly = node.isReadOnly();
        removeAll();
        c = new GridBagConstraints();
        isUpdate = true;
        setupCommonLook();
        showInTreeCheck.setSelected(node.isVisibleInTree());
        showInTreeCheck.setEnabled(!readonly);
        if (node.getClass().equals(AdsEntityExplorerItemDef.class)
                || node.getClass().equals(AdsChildRefExplorerItemDef.class)) {
            setupSelectorItemLook();
        } else if (node.getClass().equals(AdsParentRefExplorerItemDef.class)) {
            setupParentRefLook();
        } else {
            throw new RadixError(NbBundle.getMessage(SelectorItemPropsPanel.class, "Paragraph-Errors-WrongRadixObject") + node.getClass().getName());
        }
        accessPanel.open(node);
        envSelector.open(node);
        isUpdate = false;
    }

    public void update() {
        readonly = node.isReadOnly();
        isUpdate = true;

        showInTreeCheck.setSelected(node.isVisibleInTree());
        showInTreeCheck.setEnabled(!readonly);
        classEditor.update();
        classEditor.setEnabled(!readonly);
        referenceViewer.update();
        referenceViewer.setEnabled(!readonly);
        editorPresentationsChooser.update();
        targetPanel.update();
        targetPanel.setEnabled(!readonly);

        accessPanel.open(node);
        envSelector.open(node);
        isUpdate = false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    private ChangeSupport changeSupport = new ChangeSupport(this);

    public void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }
}
