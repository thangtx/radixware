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
    }

    MethodParametersPanel.ParameterModel getParameter() {
        nameEditor.commit();
        typeEditor.commit();

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
        java.awt.GridBagConstraints gridBagConstraints;

        lblName = new javax.swing.JLabel();
        txtName = nameEditor.getEditorComponent();
        lblType = new javax.swing.JLabel();
        chbVar = new javax.swing.JCheckBox();
        typePanel = typeEditor.getEditorComponent();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
        setLayout(new java.awt.GridBagLayout());

        lblName.setText(org.openide.util.NbBundle.getMessage(NewMethodParameterPanel.class, "NewMethodParameterPanel.lblName.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        add(lblName, gridBagConstraints);

        txtName.setText(org.openide.util.NbBundle.getMessage(NewMethodParameterPanel.class, "NewMethodParameterPanel.txtName.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 8, 0);
        add(txtName, gridBagConstraints);

        lblType.setText(org.openide.util.NbBundle.getMessage(NewMethodParameterPanel.class, "NewMethodParameterPanel.lblType.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        add(lblType, gridBagConstraints);

        chbVar.setText(org.openide.util.NbBundle.getMessage(NewMethodParameterPanel.class, "NewMethodParameterPanel.chbVar.text")); // NOI18N
        chbVar.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chbVarItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 8, 0);
        add(chbVar, gridBagConstraints);

        typePanel.setLayout(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 8, 0);
        add(typePanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void chbVarItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chbVarItemStateChanged
        parameterModel.setVariable(chbVar.isSelected());
    }//GEN-LAST:event_chbVarItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chbVar;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblType;
    private javax.swing.JTextField txtName;
    private javax.swing.JPanel typePanel;
    // End of variables declaration//GEN-END:variables

}
