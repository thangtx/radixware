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

package org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.AbstractListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.*;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.module.ModuleDefinitions;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixResourceBundle;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.OverridePanel.OverrideList;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.palette.DialogCreatorItem;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;


public class DialogDuplicatorPanel extends EditorDialog.EditorPanel<AdsAppObject> {

    private static final Id DIALOG_ID = Id.Factory.loadFrom("aclMSJV23P2XTNRDF5NABIFNQAABA");

    public DialogDuplicatorPanel(AdsAppObject node, final PropertyChangeListener listener) {
        super(node);
        initComponents();

        classId = node.getPropByName("class");
        adminPresentationId = node.getPropByName("adminPresentationId");        
        presentationIds = node.getPropByName("presentationIds");
        submitVariants = node.getPropByName("submitVariants");
        sourceDialog = node.getPropByName("sourceDialog");
        sourceBlockId = node.getPropByName("sourceBlockId");
        form = node.getPropByName("form");
        
        textObjectClass.setEditable(false);
        textObjectClass.addChooseButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdsVisitorProvider provider = new AdsVisitorProvider() {
                    @Override
                    public boolean isTarget(RadixObject object) {
                        if (object instanceof AdsEntityObjectClassDef) {
                            AdsClassDef cl = (AdsClassDef)object;
                            while (cl != null && !DIALOG_ID.equals(cl.getId())) {
                                cl = cl.getInheritance().findSuperClass().get();
                            }
                            return cl != null && cl != object;
                        }
                        return false;
                    };
                };
                
                AdsEntityObjectClassDef oldClassDef = classDef;
                ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(obj, provider);
                AdsEntityObjectClassDef def = (AdsEntityObjectClassDef)ChooseDefinition.chooseDefinition(cfg);
                if (def != null && def != classDef) {
                    classDef = def;
                    textObjectClass.setValue(classDef.getQualifiedName());

                    presentations.clear();
                    listPresentations.updateUI();

                    listener.propertyChange(new PropertyChangeEvent(this, "classDef", oldClassDef, classDef));
                    update();
                }
            }
        });

        textObjectClass.addResetButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdsEntityObjectClassDef oldClassDef = classDef;

                classDef = null;
                textObjectClass.setEmpty();

                presentations.clear();
                listPresentations.updateUI();

                listener.propertyChange(new PropertyChangeEvent(this, "classDef", oldClassDef, classDef));
                update();
            }
        });
        
        textAdminPresentation.setEditable(false);
        textAdminPresentation.addChooseButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (classDef == null)
                    return;
/*
                AdsVisitorProvider provider = new AdsVisitorProvider() {
                    @Override
                    public boolean isTarget(RadixObject object) {
                        if (object instanceof AdsEditorPresentationDef) {
                            return ((AdsEditorPresentationDef)object).getOwnerClass() == classDef;
                        }
                        return false;
                    };
                };
                ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(obj, provider);
*/                
                ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(classDef.getPresentations().getEditorPresentations().get(ExtendableDefinitions.EScope.ALL));
                AdsEditorPresentationDef def = (AdsEditorPresentationDef)ChooseDefinition.chooseDefinition(cfg);
                if (def != null) {
                    adminPresentationDef = def;
                    textAdminPresentation.setValue(adminPresentationDef.getName());
                }
            }
        });

        textAdminPresentation.addResetButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminPresentationDef = null;
                textAdminPresentation.setEmpty();
            }
        });
        
        textSourceBlock.setEditable(false);
        textSourceBlock.addChooseButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdsVisitorProvider provider = new AdsVisitorProvider() {
                    @Override
                    public boolean isContainer(RadixObject object) {
                        return (object instanceof AdsSegment)
                                || (object instanceof AdsModule)
                                || (object instanceof ModuleDefinitions)
                                || (object instanceof AdsDefinitions)
                                || (object instanceof RadixObjects)
                                || (object instanceof AdsAlgoClassDef && object.equals(obj.getOwnerClass()))
                                || (object instanceof AdsPage)
                                || (object instanceof AdsScopeObject)
                                || (object instanceof AdsCatchObject)
                                || (object instanceof AdsAppObject)
                                || (object instanceof AdsIncludeObject);
                    }
                    
                    @Override
                    public boolean isTarget(RadixObject object) {
                        if (!(object instanceof AdsAppObject))
                            return false;
                        return DialogCreatorItem.CLASS_NAME.equals(((AdsAppObject)object).getClazz());
                    };
                };
                
                ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(obj, provider);
                AdsAppObject obj = (AdsAppObject)ChooseDefinition.chooseDefinition(cfg);
                if (obj != null) {
                    textSourceBlock.setValue(obj.getName());
                    sourceBlock = obj;
                    updateSubmits(false);
                    update();
                }
            }
        });
        
        textSourceBlock.addResetButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textSourceBlock.setEmpty();
                sourceBlock = null;
                updateSubmits(false);
                update();
            }
        });
        
        textObjectClass.setEnabled(!node.isReadOnly());
        textAdminPresentation.setEnabled(!node.isReadOnly());
        textSourceBlock.setEnabled(!node.isReadOnly());
        btAdd.setEnabled(!node.isReadOnly());
        btDel.setEnabled(!node.isReadOnly());
        listPresentations.setEnabled(!node.isReadOnly());
        panelSubmit.setEnabled(!node.isReadOnly());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelSubmit = new org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.SubmitPanel();
        textObjectClass = new org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.ExtTextField();
        labelObjectClass = new javax.swing.JLabel();
        panelAvailablePresentations = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listPresentations = new javax.swing.JList();
        btAdd = new javax.swing.JButton();
        btDel = new javax.swing.JButton();
        btDown = new javax.swing.JButton();
        btUp = new javax.swing.JButton();
        btnOverridePs = new javax.swing.JToggleButton();
        btnOverrideSv = new javax.swing.JToggleButton();
        textSourceBlock = new org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.ExtTextField();
        labelSourceBlock = new javax.swing.JLabel();
        textAdminPresentation = new org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.ExtTextField();
        labelAdminPresentation = new javax.swing.JLabel();
        btnOverrideAdminPs = new javax.swing.JToggleButton();

        setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.inactiveCaption));
        setMinimumSize(new java.awt.Dimension(500, 430));
        setPreferredSize(new java.awt.Dimension(500, 430));
        setRequestFocusEnabled(false);

        labelObjectClass.setText(RadixResourceBundle.getMessage(DialogDuplicatorPanel.class, "DialogDuplicatorPanel.labelObjectClass.text_1")); // NOI18N

        panelAvailablePresentations.setBorder(javax.swing.BorderFactory.createTitledBorder(RadixResourceBundle.getMessage(DialogDuplicatorPanel.class, "DialogCreatorPanel.panelAvailablePresentations.text"))); // NOI18N

        listPresentations.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(listPresentations);

        btAdd.setIcon(RadixWareDesignerIcon.CREATE.ADD.getIcon());
        btAdd.setToolTipText(RadixResourceBundle.getMessage(DialogDuplicatorPanel.class, "FormEditorPanel.btAdd.text")); // NOI18N
        btAdd.setFocusable(false);
        btAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddActionPerformed(evt);
            }
        });

        btDel.setIcon(RadixWareDesignerIcon.DELETE.DELETE.getIcon());
        btDel.setToolTipText(RadixResourceBundle.getMessage(DialogDuplicatorPanel.class, "FormEditorPanel.btDel.text")); // NOI18N
        btDel.setFocusable(false);
        btDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDelActionPerformed(evt);
            }
        });

        btDown.setIcon(RadixWareDesignerIcon.ARROW.MOVE_DOWN.getIcon());
        btDown.setToolTipText(RadixResourceBundle.getMessage(DialogDuplicatorPanel.class, "DialogDuplicatorPanel.btDown.toolTipText")); // NOI18N
        btDown.setFocusable(false);
        btDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDownActionPerformed(evt);
            }
        });

        btUp.setIcon(RadixWareDesignerIcon.ARROW.MOVE_UP.getIcon());
        btUp.setToolTipText(RadixResourceBundle.getMessage(DialogDuplicatorPanel.class, "DialogDuplicatorPanel.btUp.toolTipText")); // NOI18N
        btUp.setFocusable(false);
        btUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUpActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAvailablePresentationsLayout = new javax.swing.GroupLayout(panelAvailablePresentations);
        panelAvailablePresentations.setLayout(panelAvailablePresentationsLayout);
        panelAvailablePresentationsLayout.setHorizontalGroup(
            panelAvailablePresentationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAvailablePresentationsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAvailablePresentationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btDown, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btUp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btDel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE))
        );
        panelAvailablePresentationsLayout.setVerticalGroup(
            panelAvailablePresentationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAvailablePresentationsLayout.createSequentialGroup()
                .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btDel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btUp, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btDown, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
            .addComponent(jScrollPane1)
        );

        btnOverridePs.setIcon(RadixWareDesignerIcon.WORKFLOW.DIALOG_UNSET.getIcon());
        btnOverridePs.setText(org.openide.util.NbBundle.getMessage(DialogDuplicatorPanel.class, "DialogDuplicatorPanel.btnOverridePs.text")); // NOI18N
        btnOverridePs.setToolTipText(org.openide.util.NbBundle.getMessage(DialogDuplicatorPanel.class, "DialogDuplicatorPanel.btnOverridePs.toolTipText")); // NOI18N
        btnOverridePs.setFocusPainted(false);
        btnOverridePs.setMaximumSize(new java.awt.Dimension(15, 15));
        btnOverridePs.setMinimumSize(new java.awt.Dimension(15, 15));
        btnOverridePs.setPreferredSize(new java.awt.Dimension(15, 15));
        btnOverridePs.setSelectedIcon(RadixWareDesignerIcon.WORKFLOW.DIALOG_SET.getIcon());
        btnOverridePs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOverridePsActionPerformed(evt);
            }
        });

        btnOverrideSv.setIcon(RadixWareDesignerIcon.WORKFLOW.DIALOG_UNSET.getIcon());
        btnOverrideSv.setText(org.openide.util.NbBundle.getMessage(DialogDuplicatorPanel.class, "DialogDuplicatorPanel.btnOverrideSv.text")); // NOI18N
        btnOverrideSv.setToolTipText(org.openide.util.NbBundle.getMessage(DialogDuplicatorPanel.class, "DialogDuplicatorPanel.btnOverrideSv.toolTipText")); // NOI18N
        btnOverrideSv.setFocusPainted(false);
        btnOverrideSv.setMaximumSize(new java.awt.Dimension(15, 15));
        btnOverrideSv.setMinimumSize(new java.awt.Dimension(15, 15));
        btnOverrideSv.setPreferredSize(new java.awt.Dimension(15, 15));
        btnOverrideSv.setSelectedIcon(RadixWareDesignerIcon.WORKFLOW.DIALOG_SET.getIcon());
        btnOverrideSv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOverrideSvActionPerformed(evt);
            }
        });

        labelSourceBlock.setText(RadixResourceBundle.getMessage(DialogDuplicatorPanel.class, "DialogDuplicatorPanel.labelSourceBlock.text")); // NOI18N

        labelAdminPresentation.setText(RadixResourceBundle.getMessage(DialogDuplicatorPanel.class, "DialogDuplicatorPanel.labelAdminPresentation.text")); // NOI18N

        btnOverrideAdminPs.setIcon(RadixWareDesignerIcon.WORKFLOW.DIALOG_UNSET.getIcon());
        btnOverrideAdminPs.setText(org.openide.util.NbBundle.getMessage(DialogDuplicatorPanel.class, "DialogDuplicatorPanel.btnOverrideAdminPs.text")); // NOI18N
        btnOverrideAdminPs.setToolTipText(org.openide.util.NbBundle.getMessage(DialogDuplicatorPanel.class, "DialogDuplicatorPanel.btnOverrideAdminPs.toolTipText")); // NOI18N
        btnOverrideAdminPs.setFocusPainted(false);
        btnOverrideAdminPs.setMaximumSize(new java.awt.Dimension(15, 15));
        btnOverrideAdminPs.setMinimumSize(new java.awt.Dimension(15, 15));
        btnOverrideAdminPs.setPreferredSize(new java.awt.Dimension(15, 15));
        btnOverrideAdminPs.setSelectedIcon(RadixWareDesignerIcon.WORKFLOW.DIALOG_SET.getIcon());
        btnOverrideAdminPs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOverrideAdminPsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelSubmit, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
                    .addComponent(panelAvailablePresentations, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(60, 60, 60)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(labelObjectClass)
                                    .addComponent(labelSourceBlock)))
                            .addComponent(labelAdminPresentation))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textSourceBlock, javax.swing.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)
                            .addComponent(textObjectClass, javax.swing.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)
                            .addComponent(textAdminPresentation, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnOverrideSv, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnOverridePs, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnOverrideAdminPs, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textObjectClass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelObjectClass))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(textAdminPresentation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(labelAdminPresentation))
                    .addComponent(btnOverrideAdminPs, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textSourceBlock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelSourceBlock))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelAvailablePresentations, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOverridePs, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelSubmit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnOverrideSv, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddActionPerformed
        // TODO add your handling code here:
        if (classDef == null)
            return;
        int idx = listPresentations.getSelectedIndex();
/*
        AdsVisitorProvider provider = new AdsVisitorProvider() {
            @Override
            public boolean isTarget(RadixObject object) {
                if (object instanceof AdsEditorPresentationDef) {
                    return ((AdsEditorPresentationDef)object).getOwnerClass() == classDef;
                }
                return false;
            };
        };
        ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(obj, provider);
*/        
        ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(classDef.getPresentations().getEditorPresentations().get(ExtendableDefinitions.EScope.ALL));
        AdsEditorPresentationDef p = (AdsEditorPresentationDef)ChooseDefinition.chooseDefinition(cfg);
        if (p != null) {
            int i = presentations.indexOf(p);
            if (i >= 0)
                listPresentations.setSelectedIndex(i);
            else {
                model.add(idx, p);
                listPresentations.setSelectedIndex(idx);
            }
            update();
        }
    }//GEN-LAST:event_btAddActionPerformed

    private void btDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDelActionPerformed
        // TODO add your handling code here:
        int idx = listPresentations.getSelectedIndex();
        if (idx >= 0) {
            model.del(idx);
            if (idx == model.getSize())
                idx--;
            if (idx >= 0)
                listPresentations.setSelectedIndex(idx);
            update();
        }
    }//GEN-LAST:event_btDelActionPerformed

    private void btDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDownActionPerformed
        // TODO add your handling code here:
        int idx = listPresentations.getSelectedIndex();
        if (idx >= 0 && idx < model.getSize()-1) {
            model.swap(idx, idx+1);
            listPresentations.setSelectedIndex(idx+1);
            update();
        }
}//GEN-LAST:event_btDownActionPerformed

    private void btUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUpActionPerformed
        // TODO add your handling code here:
        int idx = listPresentations.getSelectedIndex();
        if (idx > 0 && idx < model.getSize()) {
            model.swap(idx, idx-1);
            listPresentations.setSelectedIndex(idx-1);
            update();
        }
}//GEN-LAST:event_btUpActionPerformed

    private void btnOverridePsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOverridePsActionPerformed
        // TODO add your handling code here:
        ovr.setOverride("presentationIds", btnOverridePs.isSelected());
        ovr.save();
        update();
    }//GEN-LAST:event_btnOverridePsActionPerformed

    private void btnOverrideSvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOverrideSvActionPerformed
        // TODO add your handling code here:
        ovr.setOverride("submitVariants", btnOverrideSv.isSelected());
        ovr.save();
        updateSubmits(false);
        update();
    }//GEN-LAST:event_btnOverrideSvActionPerformed

    private void btnOverrideAdminPsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOverrideAdminPsActionPerformed
        // TODO add your handling code here:
        ovr.setOverride("adminPresentationId", btnOverrideAdminPs.isSelected());
        ovr.save();
        update();
    }//GEN-LAST:event_btnOverrideAdminPsActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btDel;
    private javax.swing.JButton btDown;
    private javax.swing.JButton btUp;
    private javax.swing.JToggleButton btnOverrideAdminPs;
    private javax.swing.JToggleButton btnOverridePs;
    private javax.swing.JToggleButton btnOverrideSv;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelAdminPresentation;
    private javax.swing.JLabel labelObjectClass;
    private javax.swing.JLabel labelSourceBlock;
    private javax.swing.JList listPresentations;
    private javax.swing.JPanel panelAvailablePresentations;
    private org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.SubmitPanel panelSubmit;
    private org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.ExtTextField textAdminPresentation;
    private org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.ExtTextField textObjectClass;
    private org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.ExtTextField textSourceBlock;
    // End of variables declaration//GEN-END:variables

    private AdsAppObject.Prop classId;
    private AdsAppObject.Prop adminPresentationId;
    private AdsAppObject.Prop presentationIds;
    private AdsAppObject.Prop submitVariants;
    private AdsAppObject.Prop sourceDialog;
    private AdsAppObject.Prop sourceBlockId;
    private AdsAppObject.Prop form;

    private AdsAppObject sourceBlock = null;
    private AdsEntityObjectClassDef classDef = null;
    private AdsEditorPresentationDef adminPresentationDef = null;
    private List<AdsEditorPresentationDef> presentations = new ArrayList<AdsEditorPresentationDef>();

    private Model model;
    private OverrideList ovr;

    @Override
    public void init() {
        if (classId.getValue() != null)
            classDef = (AdsEntityObjectClassDef)AdsSearcher.Factory.newAdsDefinitionSearcher(obj).findById(Id.Factory.loadFrom(classId.getValue().toString())).get();
        if (classDef != null)
            textObjectClass.setValue(classDef.getQualifiedName());
        else
            textObjectClass.setEmpty();

        if (adminPresentationId.getValue() != null && classDef != null)
            adminPresentationDef = classDef.getPresentations().getEditorPresentations().findById(Id.Factory.loadFrom(String.valueOf(adminPresentationId.getValue())), EScope.ALL).get();
        if (adminPresentationDef != null)
            textAdminPresentation.setValue(adminPresentationDef.getName());
        else
            textAdminPresentation.setEmpty();
        
        if (sourceBlockId.getValue() != null) {
            final Id id = Id.Factory.loadFrom(sourceBlockId.getValue().toString());
            final IVisitor visitor = new IVisitor() {
                @Override
                public void accept(RadixObject radixObject) {
                    if (radixObject instanceof AdsAppObject) {
                        if (((AdsAppObject) radixObject).getId().equals(id)) {
                            sourceBlock = (AdsAppObject) radixObject;
                        }
                    }
                }
            };

            obj.getOwnerClass().visit(visitor, VisitorProviderFactory.createDefaultVisitorProvider());        
        }
        if (sourceBlock != null) {
            textSourceBlock.setValue(sourceBlock.getName());
        } else {
            textSourceBlock.setEmpty();            
        }

        if (presentationIds.getValue() != null && classDef != null) {
            StringTokenizer t = new StringTokenizer(String.valueOf(presentationIds.getValue()), "\n");
            while (t.hasMoreTokens()) {
                AdsEditorPresentationDef p = classDef.getPresentations().getEditorPresentations().findById(Id.Factory.loadFrom(t.nextToken()), EScope.ALL).get();
                if (p != null)
                    presentations.add(p);
            }
        }
        listPresentations.setModel(model = new Model(presentations));
        listPresentations.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting())
                    return;
                update();
            }
        });
    }

    private void updateSubmits(boolean init) {
        if (sourceBlock != null && !ovr.isOverride("submitVariants")) {
            final AdsAppObject.Prop[] props = {
                sourceBlock.getPropByName("submitVariants"),
                sourceBlock.getPropByName("submitVariantsTitle"),
                sourceBlock.getPropByName("submitVariantsNeedConfirm"),
                sourceBlock.getPropByName("submitVariantsNeedUpdate"),
                sourceBlock.getPropByName("submitVariantsVisible")
            };
            for (AdsAppObject.Prop p: props) {
                obj.getPropByName(p.getName()).setValue(p.getValue());
            }
        }
        if (init || !ovr.isOverride("submitVariants"))
            panelSubmit.init(obj);
    }
    
    @Override
    public void activate() {
        ovr = new OverrideList(obj);
        updateSubmits(true);
        update();
    }

    @Override
    public void apply() {
        classId.setValue(classDef != null ? String.valueOf(classDef.getId()) : null);
        sourceDialog.setType(AdsTypeDeclaration.Factory.newInstance(EValType.OBJECT, classDef == null ? Id.Factory.loadFrom("aclMSJV23P2XTNRDF5NABIFNQAABA") : classDef.getId()));
        adminPresentationId.setValue(adminPresentationDef != null ? String.valueOf(adminPresentationDef.getId()) : null);
        sourceBlockId.setValue(sourceBlock != null ? sourceBlock.getId().toString() : null);
        form.setType(AdsTypeDeclaration.Factory.newInstance(EValType.OBJECT, classDef == null ? Id.Factory.loadFrom("aclMSJV23P2XTNRDF5NABIFNQAABA") : classDef.getId()));
        
        String v = "";
        for (int i=0; i<presentations.size(); i++) {
            v += String.valueOf(presentations.get(i).getId());
            if (i < presentations.size() - 1)
                v += "\n";
        }
        presentationIds.setValue(v);
        panelSubmit.apply();
    }

    @Override
    public String getTitle() {
        return RadixResourceBundle.getMessage(getClass(), "CTL_AdditionalProperties");
    }

    private void update() {
        int idx = listPresentations.getSelectedIndex();
        if (model.getSize() == 0)
            idx = -1;
        else {
            idx = Math.min(idx, model.getSize() - 1);
            idx = Math.max(idx, 0);
        }
        if (idx != listPresentations.getSelectedIndex())
            listPresentations.setSelectedIndex(idx);

        btnOverridePs.setEnabled(!obj.isReadOnly());
        btnOverridePs.setSelected(ovr.isOverride("presentationIds"));

        btnOverrideAdminPs.setEnabled(!obj.isReadOnly());
        btnOverrideAdminPs.setSelected(ovr.isOverride("adminPresentationId"));
        
        btAdd.setEnabled(ovr.isOverride("presentationIds") && !obj.isReadOnly());
        btDel.setEnabled(ovr.isOverride("presentationIds") && !obj.isReadOnly() && idx >= 0 && idx < model.getSize());
        btUp.setEnabled(ovr.isOverride("presentationIds") && !obj.isReadOnly() && idx > 0 && idx < model.getSize());
        btDown.setEnabled(ovr.isOverride("presentationIds") && !obj.isReadOnly() && idx >= 0 && idx < model.getSize()-1);
        listPresentations.setEnabled(ovr.isOverride("presentationIds") && !obj.isReadOnly());

        btnOverrideSv.setEnabled(!obj.isReadOnly());
        btnOverrideSv.setSelected(ovr.isOverride("submitVariants"));
        panelSubmit.setEnabled(ovr.isOverride("submitVariants") && !obj.isReadOnly());

        textObjectClass.setEnabled(!obj.isReadOnly());
        textSourceBlock.setEnabled(!obj.isReadOnly());
    }

    private class Model extends AbstractListModel {

        private final List<AdsEditorPresentationDef> values;

        public Model(List<AdsEditorPresentationDef> values) {
            this.values = values;
        }

        @Override
        public Object getElementAt(int i) {
            return values.get(i).getName();
        }

        @Override
        public int getSize() {
            return values.size();
        }

        public void fireContentChanged(int idx) {
            super.fireContentsChanged(this, idx, idx);
        }

        void add(int idx, AdsEditorPresentationDef value) {
            if (idx < 0 || idx >= getSize())
                idx = 0;
            values.add(idx, value);
            fireIntervalAdded(this, idx, idx);
        }

        void del(int idx) {
            if (idx >= 0 && idx < getSize()) {
                values.remove(idx);
                fireIntervalRemoved(this, idx, idx);
            }
        }

        void swap(int idx1, int idx2) {
            if (idx1 >= 0 && idx1 < getSize() && idx2 >= 0 && idx2 < getSize()) {
                AdsEditorPresentationDef p1 = values.get(idx1);
                AdsEditorPresentationDef p2 = values.get(idx2);
                values.set(idx1, p2);
                values.set(idx2, p1);
                fireContentChanged(idx1);
                fireContentChanged(idx2);
            }
        }
    }

    @Override
    public RadixIcon getIcon() {
        return RadixWareIcons.EDIT.PROPERTIES_PLUS;
    }
}
