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
 * InheritedComponentsPanel.java
 *
 * Created on Apr 27, 2009, 6:28:25 PM
 */
package org.radixware.kernel.designer.ads.editors.exploreritems;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsNodeExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParentRefExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsSelectorExplorerItemDef;
import org.radixware.kernel.designer.common.dialogs.components.ComponentTitledBorder;
import org.radixware.kernel.designer.common.dialogs.components.InheritableTitlePanel;


final class InheritedComponentsPanel extends JPanel {

    private AdsNodeExplorerItemDef node;
    private InheritableTitlePanel titlesPane = new InheritableTitlePanel();
    private InheritableTitlePanel usualTitlesPane = new InheritableTitlePanel();
    private SelectorItemPropsPanel selectorProps = new SelectorItemPropsPanel();
    private JCheckBox parentRefRestrictionsCheck;
    private CommonRestrictionsPanel parentRefRestrictionsPanel = new CommonRestrictionsPanel();

    private JPanel content = new JPanel();
    /** Creates new form InheritedComponentsPanel */
    public InheritedComponentsPanel() {
        initComponents();
        setLayout(new BorderLayout());
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
    }

    private void setupLook(boolean isSelectorItem) {
        removeAll();
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        javax.swing.JPanel titleContainer = new javax.swing.JPanel(gbl);
        c.insets = new Insets(10, 10, 0, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        if (isSelectorItem) {
            gbl.setConstraints(titlesPane, c);
            titleContainer.add(titlesPane);
        } else {
            gbl.setConstraints(usualTitlesPane, c);
            titleContainer.add(usualTitlesPane);
        }

        titleContainer.setAlignmentX(0);
        titleContainer.setAlignmentY(0);
        content.add(titleContainer);//, BorderLayout.NORTH);
        if (isSelectorItem) {
            selectorProps.setAlignmentX(0);
            selectorProps.setAlignmentY(0);
            content.add(selectorProps);//, BorderLayout.CENTER);
        } else {
            addParentRefRestrictionsPanel();
        }

        add(content, BorderLayout.NORTH);
    }

    private void addParentRefRestrictionsPanel() {

        javax.swing.JPanel restPanel = new javax.swing.JPanel();
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        restPanel.setLayout(gbl);
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTH;
        c.insets = new Insets(10, 10, 10, 10);

        parentRefRestrictionsCheck = new JCheckBox(NbBundle.getMessage(InheritedComponentsPanel.class, "SelectorRestrictions-InheritTip"));
        parentRefRestrictionsCheck.setSelected(node.isRestrictionInherited());
        ActionListener checkListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isSelected = InheritedComponentsPanel.this.parentRefRestrictionsCheck.isSelected();
                InheritedComponentsPanel.this.node.setRestrictionInherited(isSelected);
                InheritedComponentsPanel.this.parentRefRestrictionsPanel.setReadonly(isSelected);
                InheritedComponentsPanel.this.parentRefRestrictionsPanel.open(InheritedComponentsPanel.this.node, CommonRestrictionsPanel.PARENT_REF_EXPLORER_ITEM);
            }
        };
        parentRefRestrictionsCheck.addActionListener(checkListener);
        parentRefRestrictionsCheck.setEnabled(!readonly);
        parentRefRestrictionsPanel.setReadonly(readonly || node.isRestrictionInherited());
        parentRefRestrictionsPanel.open(node, CommonRestrictionsPanel.PARENT_REF_EXPLORER_ITEM);
        CompoundBorder pfBorder = new CompoundBorder(BorderFactory.createEtchedBorder(), BorderFactory.createEmptyBorder(0, 10, 10, 10));
        parentRefRestrictionsPanel.setBorder(new ComponentTitledBorder(parentRefRestrictionsCheck, parentRefRestrictionsPanel, pfBorder));

        gbl.setConstraints(parentRefRestrictionsPanel, c);
        restPanel.add(parentRefRestrictionsPanel);

//        javax.swing.JPanel temp = new javax.swing.JPanel(new BorderLayout());
//        temp.add(restPanel, BorderLayout.NORTH);
        restPanel.setAlignmentX(0);
        restPanel.setAlignmentY(0);
        content.add(restPanel);//, BorderLayout.CENTER);
    }
    private boolean readonly = false;

    public void open(final AdsNodeExplorerItemDef node) {
        this.node = node;
        this.readonly = node.isReadOnly();
        boolean isSelectorItem = node instanceof AdsSelectorExplorerItemDef;
        if (isSelectorItem) {
            selectorProps.open((AdsSelectorExplorerItemDef) node);
            titlesPane.open(node);
        } else {
            updateTitlesPane();
            usualTitlesPane.setReadonly(readonly);
        }
        setupLook(isSelectorItem);
    }

    private void updateTitlesPane() {
        usualTitlesPane.open(node);
    }

    public void update() {
        readonly = node.isReadOnly();
        if (node instanceof AdsParentRefExplorerItemDef) {
            updateTitlesPane();
            usualTitlesPane.setReadonly(readonly);
            parentRefRestrictionsCheck.setSelected(node.isRestrictionInherited());
            parentRefRestrictionsCheck.setEnabled(!readonly);
            parentRefRestrictionsPanel.setReadonly(readonly || node.isRestrictionInherited());
            parentRefRestrictionsPanel.update();
        } else if (node instanceof AdsSelectorExplorerItemDef){
            selectorProps.update();
            titlesPane.update();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
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
}
