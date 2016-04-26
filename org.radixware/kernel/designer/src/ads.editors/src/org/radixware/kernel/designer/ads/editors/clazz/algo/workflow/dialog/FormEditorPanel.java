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
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.AbstractListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.*;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixResourceBundle;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;


public class FormEditorPanel extends EditorDialog.EditorPanel<AdsAppObject> {

    public FormEditorPanel(AdsAppObject node) {
        super(node);
        initComponents();

        classId = node.getPropByName("objClassId");
        creationPresentationId = node.getPropByName("creationPresentationId");
        adminPresentationId = node.getPropByName("adminPresentationId");
        presentationIds = node.getPropByName("presentationIds");
        submitVariants = node.getPropByName("submitVariants");
        object = node.getPropByName("object");

        textObjectClass.setEditable(false);
        textObjectClass.addChooseButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdsVisitorProvider provider = new AdsVisitorProvider() {
                    @Override
                    public boolean isTarget(RadixObject object) {
                        return object instanceof AdsEntityObjectClassDef;
                    };
                };
                ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(obj, provider);
                AdsEntityObjectClassDef def = (AdsEntityObjectClassDef)ChooseDefinition.chooseDefinition(cfg);
                if (def != null) {
                    objectDef = def;
                    textObjectClass.setValue(objectDef.getQualifiedName());

                    presentationDef = null;
                    textNewPresentation.setEmpty();
                    
                    adminPresentationDef = null;
                    textAdminPresentation.setEmpty();

                    presentations.clear();
                    listPresentations.updateUI();

                    update();
                }
            }
        });

        textObjectClass.addResetButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                objectDef = null;
                textObjectClass.setEmpty();

                presentationDef = null;
                textNewPresentation.setEmpty();

                adminPresentationDef = null;
                textAdminPresentation.setEmpty();
                
                presentations.clear();
                listPresentations.updateUI();

                update();
            }
        });

        textNewPresentation.setEditable(false);
        textNewPresentation.addChooseButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (objectDef == null)
                    return;
/*                
                AdsVisitorProvider provider = new AdsVisitorProvider() {
                    @Override
                    public boolean isTarget(RadixObject object) {
                        if (object instanceof AdsEditorPresentationDef) {
                            return ((AdsEditorPresentationDef)object).getOwnerClass() == objectDef;
                        }
                        return false;
                    };
                };
                ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(obj, provider);
*/                
                ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(objectDef.getPresentations().getEditorPresentations().get(ExtendableDefinitions.EScope.ALL));
                AdsEditorPresentationDef def = (AdsEditorPresentationDef)ChooseDefinition.chooseDefinition(cfg);
                if (def != null) {
                    presentationDef = def;
                    textNewPresentation.setValue(presentationDef.getName());
                }
            }
        });

        textNewPresentation.addResetButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presentationDef = null;
                textNewPresentation.setEmpty();
            }
        });

        textAdminPresentation.setEditable(false);
        textAdminPresentation.addChooseButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (objectDef == null)
                    return;
/*
                AdsVisitorProvider provider = new AdsVisitorProvider() {
                    @Override
                    public boolean isTarget(RadixObject object) {
                        if (object instanceof AdsEditorPresentationDef) {
                            return ((AdsEditorPresentationDef)object).getOwnerClass() == objectDef;
                        }
                        return false;
                    };
                };
                ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(obj, provider);
*/
                ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(objectDef.getPresentations().getEditorPresentations().get(ExtendableDefinitions.EScope.ALL));
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
        
        panelSubmit.setEnabled(!node.isReadOnly());
        textObjectClass.setEnabled(!node.isReadOnly());
        textNewPresentation.setEnabled(!node.isReadOnly());
        textAdminPresentation.setEnabled(!node.isReadOnly());
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
        labelNewPresentation = new javax.swing.JLabel();
        textNewPresentation = new org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.ExtTextField();
        panelAvailablePresentations = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listPresentations = new javax.swing.JList();
        btAdd = new javax.swing.JButton();
        btDel = new javax.swing.JButton();
        btUp = new javax.swing.JButton();
        btDown = new javax.swing.JButton();
        textAdminPresentation = new org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.ExtTextField();
        labelAdminPresentation = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.inactiveCaption));
        setMinimumSize(new java.awt.Dimension(500, 430));
        setPreferredSize(new java.awt.Dimension(500, 430));
        setRequestFocusEnabled(false);

        labelObjectClass.setText(RadixResourceBundle.getMessage(FormEditorPanel.class, "FormEditorPanel.labelObjectClass.text")); // NOI18N

        labelNewPresentation.setText(RadixResourceBundle.getMessage(FormEditorPanel.class, "FormEditorPanel.labelNewPresentation.text")); // NOI18N

        panelAvailablePresentations.setBorder(javax.swing.BorderFactory.createTitledBorder(RadixResourceBundle.getMessage(FormEditorPanel.class, "FormEditorPanel.panelAvailablePresentations.text"))); // NOI18N

        listPresentations.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(listPresentations);

        btAdd.setIcon(RadixWareDesignerIcon.CREATE.ADD.getIcon());
        btAdd.setToolTipText(RadixResourceBundle.getMessage(FormEditorPanel.class, "FormEditorPanel.btAdd.text")); // NOI18N
        btAdd.setFocusable(false);
        btAdd.setMaximumSize(new java.awt.Dimension(30, 20));
        btAdd.setMinimumSize(new java.awt.Dimension(30, 20));
        btAdd.setPreferredSize(new java.awt.Dimension(30, 20));
        btAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddActionPerformed(evt);
            }
        });

        btDel.setIcon(RadixWareDesignerIcon.DELETE.DELETE.getIcon());
        btDel.setToolTipText(RadixResourceBundle.getMessage(FormEditorPanel.class, "FormEditorPanel.btDel.text")); // NOI18N
        btDel.setFocusable(false);
        btDel.setMaximumSize(new java.awt.Dimension(30, 20));
        btDel.setMinimumSize(new java.awt.Dimension(30, 20));
        btDel.setPreferredSize(new java.awt.Dimension(30, 20));
        btDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDelActionPerformed(evt);
            }
        });

        btUp.setIcon(RadixWareDesignerIcon.ARROW.MOVE_UP.getIcon());
        btUp.setToolTipText(RadixResourceBundle.getMessage(FormEditorPanel.class, "FormEditorPanel.btUp.toolTipText")); // NOI18N
        btUp.setFocusable(false);
        btUp.setMaximumSize(new java.awt.Dimension(30, 20));
        btUp.setMinimumSize(new java.awt.Dimension(30, 20));
        btUp.setPreferredSize(new java.awt.Dimension(30, 20));
        btUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUpActionPerformed(evt);
            }
        });

        btDown.setIcon(RadixWareDesignerIcon.ARROW.MOVE_DOWN.getIcon());
        btDown.setToolTipText(RadixResourceBundle.getMessage(FormEditorPanel.class, "FormEditorPanel.btDown.toolTipText")); // NOI18N
        btDown.setFocusable(false);
        btDown.setMaximumSize(new java.awt.Dimension(30, 9));
        btDown.setMinimumSize(new java.awt.Dimension(30, 9));
        btDown.setPreferredSize(new java.awt.Dimension(30, 20));
        btDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDownActionPerformed(evt);
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
                    .addComponent(btAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btDel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1))
        );
        panelAvailablePresentationsLayout.setVerticalGroup(
            panelAvailablePresentationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAvailablePresentationsLayout.createSequentialGroup()
                .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btDel, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btUp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btDown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        labelAdminPresentation.setText(RadixResourceBundle.getMessage(FormEditorPanel.class, "FormEditorPanel.labelAdminPresentation.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelAvailablePresentations, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelSubmit, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(labelObjectClass)
                                .addComponent(labelNewPresentation))
                            .addComponent(labelAdminPresentation))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textAdminPresentation, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(textObjectClass, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                            .addComponent(textNewPresentation, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE))))
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelNewPresentation)
                    .addComponent(textNewPresentation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textAdminPresentation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelAdminPresentation))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelAvailablePresentations, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelSubmit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddActionPerformed
        // TODO add your handling code here:
        if (objectDef == null)
            return;
        int idx = listPresentations.getSelectedIndex();
/*        
        AdsVisitorProvider provider = new AdsVisitorProvider() {
            @Override
            public boolean isTarget(RadixObject object) {
                if (object instanceof AdsEditorPresentationDef) {
                    return ((AdsEditorPresentationDef)object).getOwnerClass() == objectDef;
                }
                return false;
            };
        };
        ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(obj, provider);
*/
        ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(objectDef.getPresentations().getEditorPresentations().get(ExtendableDefinitions.EScope.ALL));
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

    private void btUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUpActionPerformed
        // TODO add your handling code here:
        int idx = listPresentations.getSelectedIndex();
        if (idx > 0 && idx < model.getSize()) {
            model.swap(idx, idx-1);
            listPresentations.setSelectedIndex(idx-1);
            update();
        }
}//GEN-LAST:event_btUpActionPerformed

    private void btDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDownActionPerformed
        // TODO add your handling code here:
        int idx = listPresentations.getSelectedIndex();
        if (idx >= 0 && idx < model.getSize()-1) {
            model.swap(idx, idx+1);
            listPresentations.setSelectedIndex(idx+1);
            update();
        }
}//GEN-LAST:event_btDownActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btDel;
    private javax.swing.JButton btDown;
    private javax.swing.JButton btUp;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelAdminPresentation;
    private javax.swing.JLabel labelNewPresentation;
    private javax.swing.JLabel labelObjectClass;
    private javax.swing.JList listPresentations;
    private javax.swing.JPanel panelAvailablePresentations;
    private org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.SubmitPanel panelSubmit;
    private org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.ExtTextField textAdminPresentation;
    private org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.ExtTextField textNewPresentation;
    private org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.ExtTextField textObjectClass;
    // End of variables declaration//GEN-END:variables

    private AdsAppObject.Prop classId;
    private AdsAppObject.Prop creationPresentationId;
    private AdsAppObject.Prop adminPresentationId;
    private AdsAppObject.Prop presentationIds;
    private AdsAppObject.Prop object;

    private AdsAppObject.Prop submitVariants;

    private AdsEntityObjectClassDef objectDef = null;
    private AdsEditorPresentationDef presentationDef = null;
    private AdsEditorPresentationDef adminPresentationDef = null;
    private List<AdsEditorPresentationDef> presentations = new ArrayList<AdsEditorPresentationDef>();

    private Model model;

    @Override
    public void init() {
        if (classId.getValue() != null)
            objectDef = (AdsEntityObjectClassDef)AdsSearcher.Factory.newAdsDefinitionSearcher(obj).findById(Id.Factory.loadFrom(classId.getValue().toString())).get();
        if (objectDef != null)
            textObjectClass.setValue(objectDef.getQualifiedName());
        else
            textObjectClass.setEmpty();

        if (creationPresentationId.getValue() != null && objectDef != null)
            presentationDef = objectDef.getPresentations().getEditorPresentations().findById(Id.Factory.loadFrom(String.valueOf(creationPresentationId.getValue())), EScope.ALL).get();
        if (presentationDef != null)
            textNewPresentation.setValue(presentationDef.getName());
        else
            textNewPresentation.setEmpty();

        if (adminPresentationId.getValue() != null && objectDef != null)
            adminPresentationDef = objectDef.getPresentations().getEditorPresentations().findById(Id.Factory.loadFrom(String.valueOf(adminPresentationId.getValue())), EScope.ALL).get();
        if (adminPresentationDef != null)
            textAdminPresentation.setValue(adminPresentationDef.getName());
        else
            textAdminPresentation.setEmpty();
        
        if (presentationIds.getValue() != null && objectDef != null) {
            StringTokenizer t = new StringTokenizer(String.valueOf(presentationIds.getValue()), "\n");
            while (t.hasMoreTokens()) {
                AdsEditorPresentationDef p = objectDef.getPresentations().getEditorPresentations().findById(Id.Factory.loadFrom(t.nextToken()), EScope.ALL).get();
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

        panelSubmit.init(obj);
        update();
    }

    @Override
    public void apply() {
        classId.setValue(objectDef != null ? String.valueOf(objectDef.getId()) : null);
        creationPresentationId.setValue(presentationDef != null ? String.valueOf(presentationDef.getId()) : null);
        adminPresentationId.setValue(adminPresentationDef != null ? String.valueOf(adminPresentationDef.getId()) : null);
        object.setType(AdsTypeDeclaration.Factory.newInstance(EValType.OBJECT, objectDef == null ? Id.Factory.loadFrom("pdcEntity____________________") : objectDef.getId()));

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
        btAdd.setEnabled(!obj.isReadOnly());
        btDel.setEnabled(!obj.isReadOnly() && idx >= 0 && idx < model.getSize());
        btUp.setEnabled(!obj.isReadOnly() && idx > 0 && idx < model.getSize());
        btDown.setEnabled(!obj.isReadOnly() && idx >= 0 && idx < model.getSize()-1);
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
