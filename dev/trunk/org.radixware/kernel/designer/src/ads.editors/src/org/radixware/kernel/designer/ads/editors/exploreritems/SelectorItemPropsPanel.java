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
 * SelectorItemPropsPanel.java
 *
 * Created on Apr 22, 2009, 10:12:36 AM
 */

package org.radixware.kernel.designer.ads.editors.exploreritems;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.border.CompoundBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsChildRefExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsEntityExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsSelectorExplorerItemDef;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.ads.common.lookup.AdsChildRefExplorerItemLookupSupport;
import org.radixware.kernel.designer.ads.common.lookup.AdsEntityExplorerItemLookupSupport;
import org.radixware.kernel.designer.ads.common.lookup.AdsSelectorExplorerItemLookupSupport;
import org.radixware.kernel.designer.common.dialogs.components.ComponentTitledBorder;


public class SelectorItemPropsPanel extends javax.swing.JPanel {

    private AdsSelectorExplorerItemDef item;
    private boolean isClassCatalogInherited = false;

    /** Creates new form SelectorItemPropsPanel */
    public SelectorItemPropsPanel() {
        initComponents();
        ActionListener classCatalogListener = new ActionListener(){

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!SelectorItemPropsPanel.this.isUpdate){
                boolean isSelected = SelectorItemPropsPanel.this.classCatalogCheck.isSelected();
                SelectorItemPropsPanel.this.item.setClassCatalogInherited(isSelected);
                SelectorItemPropsPanel.this.updateClassCatalogItems();
            }
        }

        };
        classCatalogCheck.addActionListener(classCatalogListener);

        ActionListener inheritListener = new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!SelectorItemPropsPanel.this.isUpdate){
                    boolean isSelected = SelectorItemPropsPanel.this.inheritRestrictions.isSelected();
                    SelectorItemPropsPanel.this.item.setRestrictionInherited(isSelected);
                    SelectorItemPropsPanel.this.restrictionsPanel.update();//open(SelectorItemPropsPanel.this.item,
                                                                    //   SelectorItemPropsPanel.this.context);
                }
            }

        };
        inheritRestrictions.addActionListener(inheritListener);

        classCatalogEditor.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (!isUpdate && !isClassCatalogUpdate){
                    Id newId = classCatalogEditor.getDefinitionId();
                    item.setCreationClassCatalogId(newId);
                }
            }

        });
    }

    private AdsSelectorExplorerItemLookupSupport context;
    private boolean readonly = false;
    public void open(final AdsSelectorExplorerItemDef item){
        this.item = item;
        this.readonly = item.isReadOnly();
        if (item.getClass().equals(AdsEntityExplorerItemDef.class))
            context = AdsEntityExplorerItemLookupSupport.Factory.newInstance((AdsEntityExplorerItemDef)item);
        else if (item.getClass().equals(AdsChildRefExplorerItemDef.class))
            context = AdsChildRefExplorerItemLookupSupport.Factory.newInstance((AdsChildRefExplorerItemDef)item);
        else
            throw new RadixError(NbBundle.getMessage(SelectorItemPropsPanel.class, "Paragraph-Errors-WrongRadixObject")+item.getClass().getName());

        isUpdate = true;
        updateClassCatalogItems();
        inheritRestrictions.setSelected(item.isRestrictionInherited());
        inheritRestrictions.setEnabled(!readonly);
        restrictionsPanel.open(item, context);
        isUpdate = false;
    }

    private boolean isUpdate = false;
    public void update(){
        if (item != null){
            isUpdate = true;
            updateClassCatalogItems();
            restrictionsPanel.update();
            inheritRestrictions.setEnabled(!readonly);
            isUpdate = false;
        }
    }

    private boolean isClassCatalogUpdate = false;
    private void updateClassCatalogItems(){
        isClassCatalogUpdate = true;
        ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(item, context.getAvailableClassCatalogsProvider());
        isClassCatalogInherited = item.isClassCatalogInherited();
        classCatalogCheck.setSelected(isClassCatalogInherited);
        classCatalogCheck.setEnabled(!readonly);
        classCatalogEditor.open(cfg, item.findCreationClassCatalog().get(), item.getCreationClassCatalogId());
        classCatalogEditor.setClearable(true);
        classCatalogEditor.setEnabled(!isClassCatalogInherited && !readonly);
        isClassCatalogUpdate = false;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        inheritRestrictions = new javax.swing.JCheckBox();
        classCatalogCheck = new javax.swing.JCheckBox();
        restrictionsPanel = new org.radixware.kernel.designer.ads.editors.exploreritems.AdsRestrictionsPanel();
        classCatalogHandler = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        classCatalogEditor = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();

        inheritRestrictions.setText(org.openide.util.NbBundle.getMessage(SelectorItemPropsPanel.class, "SelectorRestrictions-InheritTip")); // NOI18N

        classCatalogCheck.setText(org.openide.util.NbBundle.getMessage(SelectorItemPropsPanel.class, "SelectorItem-InheritClassCatalog")); // NOI18N

        setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 10);
        add(restrictionsPanel, gridBagConstraints);
        CompoundBorder iBorder = new CompoundBorder(BorderFactory.createEtchedBorder(),
            BorderFactory.createEmptyBorder(0, 10, 10, 10));
        restrictionsPanel.setBorder(new ComponentTitledBorder(inheritRestrictions, restrictionsPanel, iBorder));

        jLabel2.setText(org.openide.util.NbBundle.getMessage(SelectorItemPropsPanel.class, "SelectorItem-ClassCatalogTip")); // NOI18N

        javax.swing.GroupLayout classCatalogHandlerLayout = new javax.swing.GroupLayout(classCatalogHandler);
        classCatalogHandler.setLayout(classCatalogHandlerLayout);
        classCatalogHandlerLayout.setHorizontalGroup(
            classCatalogHandlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(classCatalogHandlerLayout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(classCatalogEditor, javax.swing.GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE))
        );
        classCatalogHandlerLayout.setVerticalGroup(
            classCatalogHandlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(classCatalogHandlerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(classCatalogHandlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(classCatalogEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(classCatalogHandler, gridBagConstraints);
        CompoundBorder clBorder = new CompoundBorder(BorderFactory.createEtchedBorder(),
            BorderFactory.createEmptyBorder(0, 10, 0, 10));
        classCatalogHandler.setBorder(new ComponentTitledBorder(classCatalogCheck, classCatalogHandler, clBorder));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox classCatalogCheck;
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel classCatalogEditor;
    private javax.swing.JPanel classCatalogHandler;
    private javax.swing.JCheckBox inheritRestrictions;
    private javax.swing.JLabel jLabel2;
    private org.radixware.kernel.designer.ads.editors.exploreritems.AdsRestrictionsPanel restrictionsPanel;
    // End of variables declaration//GEN-END:variables

}
