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
 * AdsPanelPropertyRestrictions.java
 *
 * Created on 04.09.2009, 10:00:54
 */
package org.radixware.kernel.designer.ads.editors.property;

import java.util.List;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.ParentRefPropertyPresentation;
import org.radixware.kernel.common.defs.ads.common.Restrictions;
import org.radixware.kernel.common.enums.ERestriction;


public class AdsPanelPropertyRestrictions extends javax.swing.JPanel {

    /**
     * Creates new form AdsPanelPropertyRestrictions
     */
    public AdsPanelPropertyRestrictions() {
        initComponents();
    }
    private Restrictions selRest = null;
    private Restrictions edRest = null;
    AdsPropertyDef prop = null;
    ParentRefPropertyPresentation parentRefPropertyPresentation = null;
    boolean isReadOnly;

    public void open(AdsPropertyDef prop,
            ParentRefPropertyPresentation parentRefPropertyPresentation,
            Restrictions selRest,
            Restrictions edRest,
            boolean isReadOnly) {
        this.prop = prop;
        this.parentRefPropertyPresentation = parentRefPropertyPresentation;
        this.selRest = selRest;
        this.edRest = edRest;
        this.isReadOnly = isReadOnly;
        update();
    }

    public void update() {
        if (prop == null
                || parentRefPropertyPresentation == null
                || selRest == null
                || edRest == null) {
            return;
        }
        updateRestricionPanel();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel14 = new javax.swing.JPanel();
        jcbUpdate2 = new javax.swing.JCheckBox();
        jcbCommands2 = new javax.swing.JCheckBox();
        chbEditorView = new javax.swing.JCheckBox();
        jPanel11 = new javax.swing.JPanel();
        jcbRunEditor = new javax.swing.JCheckBox();
        jcbCreate = new javax.swing.JCheckBox();
        jcbDelete = new javax.swing.JCheckBox();
        jcbDeleteAll = new javax.swing.JCheckBox();
        jcbUpdate = new javax.swing.JCheckBox();
        jcbMultipleCopy = new javax.swing.JCheckBox();
        jcbCommands = new javax.swing.JCheckBox();

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AdsPanelPropertyRestrictions.class, "AdsPanelPropertyRestrictions.jPanel14.border.title"))); // NOI18N

        jcbUpdate2.setText(org.openide.util.NbBundle.getMessage(AdsPanelPropertyRestrictions.class, "AdsPanelPropertyRestrictions.jcbUpdate2.text")); // NOI18N
        jcbUpdate2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbUpdate2ActionPerformed(evt);
            }
        });

        jcbCommands2.setText(org.openide.util.NbBundle.getMessage(AdsPanelPropertyRestrictions.class, "AdsPanelPropertyRestrictions.jcbCommands2.text")); // NOI18N
        jcbCommands2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbCommands2ActionPerformed(evt);
            }
        });

        chbEditorView.setText(org.openide.util.NbBundle.getMessage(AdsPanelPropertyRestrictions.class, "AdsPanelPropertyRestrictions.chbEditorView.text")); // NOI18N
        chbEditorView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chbEditorViewActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jcbUpdate2)
                    .addComponent(jcbCommands2)
                    .addComponent(chbEditorView))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(jcbUpdate2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcbCommands2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chbEditorView)
                .addContainerGap())
        );

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AdsPanelPropertyRestrictions.class, "AdsPanelPropertyRestrictions.jPanel11.border.title"))); // NOI18N

        jcbRunEditor.setText(org.openide.util.NbBundle.getMessage(AdsPanelPropertyRestrictions.class, "AdsPanelPropertyRestrictions.jcbRunEditor.text")); // NOI18N
        jcbRunEditor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbRunEditorActionPerformed(evt);
            }
        });

        jcbCreate.setText(org.openide.util.NbBundle.getMessage(AdsPanelPropertyRestrictions.class, "AdsPanelPropertyRestrictions.jcbCreate.text")); // NOI18N
        jcbCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbCreateActionPerformed(evt);
            }
        });

        jcbDelete.setText(org.openide.util.NbBundle.getMessage(AdsPanelPropertyRestrictions.class, "AdsPanelPropertyRestrictions.jcbDelete.text")); // NOI18N
        jcbDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbDeleteActionPerformed(evt);
            }
        });

        jcbDeleteAll.setText(org.openide.util.NbBundle.getMessage(AdsPanelPropertyRestrictions.class, "AdsPanelPropertyRestrictions.jcbDeleteAll.text")); // NOI18N
        jcbDeleteAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbDeleteAllActionPerformed(evt);
            }
        });

        jcbUpdate.setText(org.openide.util.NbBundle.getMessage(AdsPanelPropertyRestrictions.class, "AdsPanelPropertyRestrictions.jcbUpdate.text")); // NOI18N
        jcbUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbUpdateActionPerformed(evt);
            }
        });

        jcbMultipleCopy.setText(org.openide.util.NbBundle.getMessage(AdsPanelPropertyRestrictions.class, "AdsPanelPropertyRestrictions.jcbMultipleCopy.text")); // NOI18N
        jcbMultipleCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbMultipleCopyActionPerformed(evt);
            }
        });

        jcbCommands.setText(org.openide.util.NbBundle.getMessage(AdsPanelPropertyRestrictions.class, "AdsPanelPropertyRestrictions.jcbCommands.text")); // NOI18N
        jcbCommands.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbCommandsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jcbRunEditor)
                    .addComponent(jcbCreate)
                    .addComponent(jcbDelete)
                    .addComponent(jcbDeleteAll))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jcbMultipleCopy)
                    .addComponent(jcbCommands)
                    .addComponent(jcbUpdate))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcbRunEditor)
                    .addComponent(jcbMultipleCopy))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcbCreate)
                    .addComponent(jcbCommands))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcbDelete)
                    .addComponent(jcbUpdate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcbDeleteAll))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 24, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private boolean isDeniedInSelector(ERestriction flag, AdsSelectorPresentationDef sp) {
        boolean ownVal = selRest.isDenied(flag);
        if (sp != null) {
            return ownVal || sp.getRestrictions(false).isDenied(flag);
        } else {
            return ownVal;
        }
    }

    private void updateRestricionPanel() {
        if (parentRefPropertyPresentation == null) {
            return;
        }

        AdsSelectorPresentationDef sp = parentRefPropertyPresentation.getParentSelect().findParentSelectorPresentation();

        if (selRest == null) {
            jcbRunEditor.setSelected(true);
            jcbCreate.setSelected(true);
            jcbDelete.setSelected(true);
            jcbDeleteAll.setSelected(true);
            jcbUpdate.setSelected(true);
            jcbMultipleCopy.setSelected(true);
            jcbCommands.setSelected(true);
        } else {
            jcbRunEditor.setSelected(isDeniedInSelector(ERestriction.RUN_EDITOR, sp));
            jcbCreate.setSelected(isDeniedInSelector(ERestriction.CREATE, sp));
            jcbDelete.setSelected(isDeniedInSelector(ERestriction.DELETE, sp));
            jcbDeleteAll.setSelected(isDeniedInSelector(ERestriction.DELETE_ALL, sp));
            jcbUpdate.setSelected(isDeniedInSelector(ERestriction.UPDATE, sp));
            jcbMultipleCopy.setSelected(isDeniedInSelector(ERestriction.MULTIPLE_COPY, sp));
            jcbCommands.setSelected(isDeniedInSelector(ERestriction.ANY_COMMAND, sp));
        }

        if (edRest == null) {
            jcbUpdate2.setSelected(true);
            jcbCommands2.setSelected(true);
            chbEditorView.setSelected(true);
        } else {
            jcbUpdate2.setSelected(edRest.isDenied(ERestriction.UPDATE));
            jcbCommands2.setSelected(edRest.isDenied(ERestriction.ANY_COMMAND));
            chbEditorView.setSelected(edRest.isDenied(ERestriction.VIEW));
        }
        // System.out.println("Ed >> " + String.valueOf(edRest.toBitField()));
        //System.out.println("Sel >> " + String.valueOf(selRest.toBitField()));


        if (selRest == null) {
            jcbRunEditor.setEnabled(false);
            jcbCreate.setEnabled(false);
            jcbDelete.setEnabled(false);
            jcbDeleteAll.setEnabled(false);
            jcbUpdate.setEnabled(false);
            jcbMultipleCopy.setEnabled(false);
            jcbCommands.setEnabled(false);
        } else {



            jcbRunEditor.setEnabled(
                    !isReadOnly
                    && //selRest.canDeny(ERestriction.RUN_EDITOR) &&
                    //selRest.canAllow(ERestriction.RUN_EDITOR)
                    sp != null
                    && !sp.getRestrictions(false).isDenied(ERestriction.RUN_EDITOR));
            jcbCreate.setEnabled(
                    !isReadOnly
                    && //selRest.canDeny(ERestriction.CREATE) &&
                    //selRest.canAllow(ERestriction.CREATE)
                    sp != null
                    && !sp.getRestrictions(false).isDenied(ERestriction.CREATE));


            jcbDelete.setEnabled(!isReadOnly
                    && //selRest.canDeny(ERestriction.DELETE) &&
                    //selRest.canAllow(ERestriction.DELETE)
                    sp != null
                    && !sp.getRestrictions(false).isDenied(ERestriction.DELETE));
            jcbDeleteAll.setEnabled(!isReadOnly
                    && jcbDelete.isEnabled() && !jcbDelete.isSelected()
                    && //selRest.canDeny(ERestriction.DELETE_ALL) &&
                    //selRest.canAllow(ERestriction.DELETE_ALL)
                    sp != null
                    && !sp.getRestrictions(false).isDenied(ERestriction.DELETE_ALL));
            jcbUpdate.setEnabled(!isReadOnly
                    && //selRest.canDeny(ERestriction.UPDATE) &&
                    //selRest.canAllow(ERestriction.UPDATE)
                    sp != null
                    && !sp.getRestrictions(false).isDenied(ERestriction.UPDATE));
            jcbMultipleCopy.setEnabled(!isReadOnly
                    && //selRest.canDeny(ERestriction.MULTIPLE_COPY) &&
                    //selRest.canAllow(ERestriction.MULTIPLE_COPY)
                    sp != null
                    && !sp.getRestrictions(false).isDenied(ERestriction.MULTIPLE_COPY));
            jcbCommands.setEnabled(!isReadOnly
                    && //selRest.canDeny(ERestriction.ANY_COMMAND) &&
                    //selRest.canAllow(ERestriction.ANY_COMMAND)
                    sp != null
                    && !sp.getRestrictions(false).isDenied(ERestriction.ANY_COMMAND));
        }

        if (edRest == null) {
            jcbUpdate2.setEnabled(false);
            jcbCommands2.setEnabled(false);
            chbEditorView.setEnabled(false);
        } else {
            final List<AdsEditorPresentationDef> editorPresentations = parentRefPropertyPresentation.getEditOptions().findObjectEditorPresentations();
            jcbUpdate2.setEnabled(!isReadOnly
                    && //edRest.canDeny(ERestriction.UPDATE) && edRest.canAllow(ERestriction.UPDATE)
                    //ep != null && !ep.getRestrictions().isDenied(ERestriction.UPDATE)
                    editorPresentations != null && !editorPresentations.isEmpty());

            jcbCommands2.setEnabled(!isReadOnly
                    && //edRest.canDeny(ERestriction.ANY_COMMAND) &&
                    //edRest.canAllow(ERestriction.ANY_COMMAND)
                    editorPresentations != null && !editorPresentations.isEmpty() //                    ep != null && (!ep.getRestrictions().isDenied(ERestriction.ANY_COMMAND)
                    //                    || ep.getRestrictions().getEnabledCommandIds()!=null
                    //                    && !ep.getRestrictions().getEnabledCommandIds().isEmpty())
                    );

            chbEditorView.setEnabled(!isReadOnly
                    && //!ep.getRestrictions().isDenied(ERestriction.VIEW)
                    editorPresentations != null && !editorPresentations.isEmpty());
        }

    }

    private void jcbUpdate2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbUpdate2ActionPerformed
        if (jcbUpdate2.isSelected()) {
            edRest.deny(ERestriction.UPDATE);
        } else {
            edRest.allow(ERestriction.UPDATE);
        }
        prop.setEditState(EEditState.MODIFIED);
        updateRestricionPanel();
}//GEN-LAST:event_jcbUpdate2ActionPerformed

    private void jcbCommands2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbCommands2ActionPerformed
        if (jcbCommands2.isSelected()) {
            edRest.deny(ERestriction.ANY_COMMAND);
        } else {
            edRest.allow(ERestriction.ANY_COMMAND);
        }
        prop.setEditState(EEditState.MODIFIED);
        updateRestricionPanel();
}//GEN-LAST:event_jcbCommands2ActionPerformed

    private void jcbRunEditorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbRunEditorActionPerformed
        if (jcbRunEditor.isSelected()) {
            selRest.deny(ERestriction.RUN_EDITOR);
        } else {
            selRest.allow(ERestriction.RUN_EDITOR);
        }
        updateRestricionPanel();
}//GEN-LAST:event_jcbRunEditorActionPerformed

    private void jcbCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbCreateActionPerformed
        if (jcbCreate.isSelected()) {
            selRest.deny(ERestriction.CREATE);
        } else {
            selRest.allow(ERestriction.CREATE);
        }
        updateRestricionPanel();
}//GEN-LAST:event_jcbCreateActionPerformed

    private void jcbDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbDeleteActionPerformed
        if (jcbDelete.isSelected()) {
            selRest.deny(ERestriction.DELETE);
        } else {
            selRest.allow(ERestriction.DELETE);
        }
        prop.setEditState(EEditState.MODIFIED);
        updateRestricionPanel();
}//GEN-LAST:event_jcbDeleteActionPerformed

    private void jcbDeleteAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbDeleteAllActionPerformed
        if (jcbDeleteAll.isSelected()) {
            selRest.deny(ERestriction.DELETE_ALL);
        } else {
            selRest.allow(ERestriction.DELETE_ALL);
        }
        prop.setEditState(EEditState.MODIFIED);
        updateRestricionPanel();
}//GEN-LAST:event_jcbDeleteAllActionPerformed

    private void jcbUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbUpdateActionPerformed
        if (jcbUpdate.isSelected()) {
            selRest.deny(ERestriction.UPDATE);
        } else {
            selRest.allow(ERestriction.UPDATE);
        }
        prop.setEditState(EEditState.MODIFIED);
        updateRestricionPanel();
}//GEN-LAST:event_jcbUpdateActionPerformed

    private void jcbMultipleCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbMultipleCopyActionPerformed
        if (jcbMultipleCopy.isSelected()) {
            selRest.deny(ERestriction.MULTIPLE_COPY);
        } else {
            selRest.allow(ERestriction.MULTIPLE_COPY);
        }
        prop.setEditState(EEditState.MODIFIED);
        updateRestricionPanel();
}//GEN-LAST:event_jcbMultipleCopyActionPerformed

    private void jcbCommandsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbCommandsActionPerformed
        if (jcbCommands.isSelected()) {
            selRest.deny(ERestriction.ANY_COMMAND);
        } else {
            selRest.allow(ERestriction.ANY_COMMAND);
        }
        prop.setEditState(EEditState.MODIFIED);
        updateRestricionPanel();
}//GEN-LAST:event_jcbCommandsActionPerformed

    private void chbEditorViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chbEditorViewActionPerformed
        if (chbEditorView.isSelected()) {
            edRest.deny(ERestriction.VIEW);
        } else {
            edRest.allow(ERestriction.VIEW);
        }
        prop.setEditState(EEditState.MODIFIED);
        updateRestricionPanel();
    }//GEN-LAST:event_chbEditorViewActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chbEditorView;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JCheckBox jcbCommands;
    private javax.swing.JCheckBox jcbCommands2;
    private javax.swing.JCheckBox jcbCreate;
    private javax.swing.JCheckBox jcbDelete;
    private javax.swing.JCheckBox jcbDeleteAll;
    private javax.swing.JCheckBox jcbMultipleCopy;
    private javax.swing.JCheckBox jcbRunEditor;
    private javax.swing.JCheckBox jcbUpdate;
    private javax.swing.JCheckBox jcbUpdate2;
    // End of variables declaration//GEN-END:variables
}
