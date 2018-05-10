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

package org.radixware.kernel.designer.ads.method.profile;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.designer.common.dialogs.components.TypeEditorComponent;
import org.radixware.kernel.designer.common.dialogs.components.TypeEditorComponent.TypeContext;
import org.radixware.kernel.designer.common.dialogs.components.values.NameEditorComponent;
import org.radixware.kernel.designer.common.dialogs.components.values.ValueChangeEvent;
import org.radixware.kernel.designer.common.dialogs.components.values.ValueChangeListener;


final class NewMethodParameterPanel extends javax.swing.JPanel {

    private final ChangeSupport changeSupport = new ChangeSupport(this);
    private final NameEditorComponent nameEditor = new NameEditorComponent();
    private final TypeEditorComponent typeEditor = new TypeEditorComponent();

    public NewMethodParameterPanel() {
        initComponents();

        typeEditor.addValueChangeListener(new ValueChangeListener<AdsTypeDeclaration>() {

            @Override
            public void valueChanged(ValueChangeEvent<AdsTypeDeclaration> e) {
                changeSupport.fireChange();
            }
        });

        nameEditor.addValueChangeListener(new ValueChangeListener<String>() {

            @Override
            public void valueChanged(ValueChangeEvent<String> e) {
                changeSupport.fireChange();
            }
        });
        
        descriptionEditor1.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                changeSupport.fireChange();
            }
        });
    }

    private MethodParametersPanel.ParameterModel parameterModel;
    public void open(AdsMethodDef method) {
        parameterModel = new MethodParametersPanel.ParameterModel(MethodParameter.Factory.newTemporaryInstance(method, txtName.getText(), AdsTypeDeclaration.UNDEFINED, chbVar.isSelected()));

        typeEditor.open(new TypeContext(parameterModel.getParameter(), method) {

            @Override
            public void commit(AdsTypeDeclaration type) {
                parameterModel.setType(type);
            }
        });

        nameEditor.open(new NameEditorComponent.NamedContext() {

            @Override
            public String getName() {
                return parameterModel.getName();
            }

            @Override
            public void setName(String name) {
                parameterModel.setName(name);
            }

            @Override
            public boolean isValidName(String name) {
                return name != null && !name.isEmpty();
            }
        }, "arg");
        
        descriptionEditor1.open(parameterModel.getDescriptionModel());
    }

    MethodParametersPanel.ParameterModel getParameter() {
        nameEditor.commit();
        typeEditor.commit();
        descriptionEditor1.commit();

        return parameterModel;
    }

    boolean isComplete() {
        return nameEditor.isSetValue() && typeEditor.isSetValue();
    }

    public void removeChangeListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }

    public void addChangeListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblName = new javax.swing.JLabel();
        txtName = nameEditor.getEditorComponent();
        lblType = new javax.swing.JLabel();
        chbVar = new javax.swing.JCheckBox();
        typePanel = typeEditor.getEditorComponent();
        descriptionEditor1 = new org.radixware.kernel.designer.common.dialogs.components.description.DescriptionEditor();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));

        lblName.setText(org.openide.util.NbBundle.getMessage(NewMethodParameterPanel.class, "NewMethodParameterPanel.lblName.text")); // NOI18N

        txtName.setText(org.openide.util.NbBundle.getMessage(NewMethodParameterPanel.class, "NewMethodParameterPanel.txtName.text")); // NOI18N

        lblType.setText(org.openide.util.NbBundle.getMessage(NewMethodParameterPanel.class, "NewMethodParameterPanel.lblType.text")); // NOI18N

        chbVar.setText(org.openide.util.NbBundle.getMessage(NewMethodParameterPanel.class, "NewMethodParameterPanel.chbVar.text")); // NOI18N
        chbVar.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chbVarItemStateChanged(evt);
            }
        });

        typePanel.setMinimumSize(new java.awt.Dimension(0, 20));
        typePanel.setLayout(null);

        descriptionEditor1.setMinimumSize(new java.awt.Dimension(61, 100));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lblName)
                .addGap(4, 4, 4)
                .addComponent(txtName))
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(lblType)
                .addGap(7, 7, 7)
                .addComponent(typePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(chbVar, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(descriptionEditor1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(lblName))
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblType, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(typePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addComponent(chbVar)
                .addGap(1, 1, 1)
                .addComponent(descriptionEditor1, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                .addGap(8, 8, 8))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void chbVarItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chbVarItemStateChanged
        parameterModel.setVariable(chbVar.isSelected());
    }//GEN-LAST:event_chbVarItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chbVar;
    private org.radixware.kernel.designer.common.dialogs.components.description.DescriptionEditor descriptionEditor1;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblType;
    private javax.swing.JTextField txtName;
    private javax.swing.JPanel typePanel;
    // End of variables declaration//GEN-END:variables

}
