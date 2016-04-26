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
 * SelectorRestrictionsPanel.java
 *
 * Created on Apr 22, 2009, 11:50:08 AM
 */
package org.radixware.kernel.designer.ads.editors.exploreritems;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.common.Restrictions;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsNodeExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsSelectorExplorerItemDef;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.designer.ads.common.lookup.AdsCommandsLookupSupport;


public class AdsRestrictionsPanel extends javax.swing.JPanel {

    /** Creates new form SelectorRestrictionsPanel */
    public AdsRestrictionsPanel() {
        initComponents();
        ActionListener anyCommandCheckListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!AdsRestrictionsPanel.this.isUpdate) {
                    if (e.getSource().equals(AdsRestrictionsPanel.this.anyCommandCheck)) {
                        boolean isSelected = AdsRestrictionsPanel.this.anyCommandCheck.isSelected();
                        if (isSelected) {
                            restrictions.deny(ERestriction.ANY_COMMAND);
                            commandsPanel.setEnabled(true);
                        } else {
                            restrictions.allow(ERestriction.ANY_COMMAND);
                            commandsPanel.setEnabled(false);
                        }
                        AdsRestrictionsPanel.this.commandsPanel.update();
                    }
                }
            }
        };
        anyCommandCheck.addActionListener(anyCommandCheckListener);
    }

    public void setReadonly(boolean readonly){
        this.readonly = readonly;
    }

    public boolean isReadonly(){
        return readonly;
    }

    private boolean readonly = false;
    private boolean isRestrictionsInherited = false;
    private Restrictions restrictions;
    protected AdsDefinition adscontext;

    public void open(final AdsDefinition adscontext, AdsCommandsLookupSupport context) {
        int type = CommonRestrictionsPanel.SELECTOR_EXPLORER_ITEM;
        if (adscontext instanceof AdsEditorPresentationDef) {
            type = CommonRestrictionsPanel.EDITOR_PRESENTATION;
            restrictions = ((AdsPresentationDef) adscontext).getRestrictions(false);
            isRestrictionsInherited = ((AdsPresentationDef) adscontext).isRestrictionsInherited();
        } else if (adscontext instanceof AdsSelectorPresentationDef) {
            type = CommonRestrictionsPanel.SELECTOR_PRESENTATION;
            restrictions = ((AdsPresentationDef) adscontext).getRestrictions(false);
            isRestrictionsInherited = ((AdsPresentationDef) adscontext).isRestrictionsInherited();
        } else if (adscontext instanceof AdsSelectorExplorerItemDef) {
            type = CommonRestrictionsPanel.SELECTOR_EXPLORER_ITEM;
            restrictions = ((AdsSelectorExplorerItemDef) adscontext).getRestrictions();
            isRestrictionsInherited = ((AdsSelectorExplorerItemDef) adscontext).isRestrictionInherited();
        } else {
            throw new RadixError(NbBundle.getMessage(AdsRestrictionsPanel.class, "Usupported definition type: " + adscontext.getClass().getName()));
        }
        this.adscontext = adscontext;
        this.readonly = isRestrictionsInherited || adscontext.isReadOnly();
        commandsPanel.open(adscontext, restrictions, context, readonly);
        checks.open(adscontext, type);
        update();
    }
    protected boolean isUpdate = false;

    public void update() {
        isUpdate = true;
        if (adscontext != null) {
            if (adscontext instanceof AdsEditorPresentationDef) {
                restrictions = ((AdsPresentationDef) adscontext).getRestrictions(false);
                isRestrictionsInherited = ((AdsPresentationDef) adscontext).isRestrictionsInherited();
            } else if (adscontext instanceof AdsSelectorPresentationDef) {
                restrictions = ((AdsPresentationDef) adscontext).getRestrictions(false);
                isRestrictionsInherited = ((AdsPresentationDef) adscontext).isRestrictionsInherited();
            } else if (adscontext instanceof AdsSelectorExplorerItemDef) {
                restrictions = ((AdsSelectorExplorerItemDef) adscontext).getRestrictions();
                isRestrictionsInherited = ((AdsSelectorExplorerItemDef) adscontext).isRestrictionInherited();
            } else {
                throw new RadixError(NbBundle.getMessage(AdsRestrictionsPanel.class, "Usupported definition type: " + adscontext.getClass().getName()));
            }

            this.readonly = (isRestrictionsInherited || adscontext.isReadOnly()) && readonly;
            anyCommandCheck.setSelected(restrictions.isDenied(ERestriction.ANY_COMMAND));
            anyCommandCheck.setEnabled(!readonly && restrictions.canAllow(ERestriction.ANY_COMMAND) && restrictions.canDeny(ERestriction.ANY_COMMAND));
            commandsPanel.setResctrictions(restrictions);
            commandsPanel.setReadonly(readonly || !anyCommandCheck.isSelected());
            commandsPanel.update();
            checks.setRestrictions(restrictions);
            checks.setIsResctrictionsInhertied(isRestrictionsInherited);
            checks.setReadonly(readonly);
            checks.update();
        }
        isUpdate = false;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.readonly = !enabled;
        update();
        super.setEnabled(enabled);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        anyCommandCheck = new javax.swing.JCheckBox();
        commandsPanel = new org.radixware.kernel.designer.ads.editors.exploreritems.EnabledCommandPanel();
        checks = new org.radixware.kernel.designer.ads.editors.exploreritems.CommonRestrictionsPanel();

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AdsRestrictionsPanel.class, "SelectorRestrictions-CommandsTitle"))); // NOI18N

        anyCommandCheck.setText(org.openide.util.NbBundle.getMessage(AdsRestrictionsPanel.class, "SelectorRestrictions-CommandsCheckTip")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(commandsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                    .addComponent(anyCommandCheck))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(anyCommandCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(commandsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout checksLayout = new javax.swing.GroupLayout(checks);
        checks.setLayout(checksLayout);
        checksLayout.setHorizontalGroup(
            checksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 158, Short.MAX_VALUE)
        );
        checksLayout.setVerticalGroup(
            checksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 139, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(checks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(checks, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox anyCommandCheck;
    private org.radixware.kernel.designer.ads.editors.exploreritems.CommonRestrictionsPanel checks;
    private org.radixware.kernel.designer.ads.editors.exploreritems.EnabledCommandPanel commandsPanel;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
