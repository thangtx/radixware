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
 * SelectParseMergeFunctionPanel.java
 *
 * Created on 26.02.2009, 12:13:50
 */
package org.radixware.kernel.designer.ads.editors.msdl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.msdl.fields.AbstractFieldModel;
import org.radixware.kernel.common.design.msdleditor.Messages;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.schemas.msdl.Field;
import org.radixware.schemas.msdl.ChoiceField;


public class SelectParseMergeFunctionPanel extends javax.swing.JPanel {

    private Field field;
    private AbstractFieldModel fieldModel;
    private boolean opened = false;
    private boolean isChoice = false;

    /** Creates new form SelectParseMergeFunctionPanel */
    public SelectParseMergeFunctionPanel() {
        initComponents();
    }

    public void open(MsdlField field, SupportSelectPreprocessorFunctionList support) {
        opened = false;
        this.fieldModel = field.getFieldModel();
        this.field = fieldModel.getField();
        DefaultComboBoxModel modelParse= new DefaultComboBoxModel();
        DefaultComboBoxModel modelMerge = new DefaultComboBoxModel();
        PreprocessorFunctionItem notDefined = new PreprocessorFunctionItem(Messages.NOT_DEFINED, null);
        modelParse.addElement(notDefined);
        modelMerge.addElement(notDefined);
        jComboBoxParseFunction.setModel(modelParse);
        jComboBoxMergeFunction.setModel(modelMerge);
        jComboBoxParseFunction.setSelectedItem(notDefined);
        jComboBoxMergeFunction.setSelectedItem(notDefined);
        DefaultComboBoxModel modelAdvisor = null;
        if(fieldModel.getField() instanceof ChoiceField) {
            modelAdvisor = new DefaultComboBoxModel();
            modelAdvisor.addElement(notDefined);
            advisorFunctionCombo.setModel(modelAdvisor);
            advisorFunctionCombo.setSelectedItem(notDefined);
            isChoice = true;
        }
        else {
           advisorFunctionCombo.setVisible(false);
           advisorLabel.setVisible(false);
        }
        
        if (support!= null && support.isSetClass()) {
            jComboBoxMergeFunction.setEnabled(true);
            jComboBoxParseFunction.setEnabled(true);
            ArrayList<PreprocessorFunctionItem> list = support.getFunctionList();
            PreprocessorFunctionItem write = null;
            PreprocessorFunctionItem read = null;
            PreprocessorFunctionItem advice = null;
            for (PreprocessorFunctionItem cur : list) {
                if (cur.getId().equals(field.getFieldModel().getField().getMergeFunctionName())) {
                    write = cur;
                }
                if (cur.getId().equals(field.getFieldModel().getField().getParseFunctionName())) {
                    read = cur;
                }
                if(isChoice) {
                    ChoiceField choiceField = (ChoiceField)fieldModel.getField();
                    if(cur.getId().equals(choiceField.getSelectorAdvisorFunctionName())) {
                        advice = cur;
                    }
                    if(cur.isAdvisor)
                        modelAdvisor.addElement(cur);
                    if(advice != null && cur.isAdvisor) {
                        advisorFunctionCombo.setSelectedItem(advice);
                    }
                    advisorFunctionCombo.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            selectorAdvisorActionPerformed(e);
                        }
                    });
                }
                if(!cur.isAdvisor) {
                    modelParse.addElement(cur);
                    modelMerge.addElement(cur);
                }
            }
            if (write != null)
                jComboBoxMergeFunction.setSelectedItem(write);
            if (read != null)
                jComboBoxParseFunction.setSelectedItem(read);
            if(advice != null)
                advisorFunctionCombo.setSelectedItem(advice);
        }
        else {
            jComboBoxMergeFunction.setEnabled(false);
            jComboBoxParseFunction.setEnabled(false);
        }
        opened = true;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBoxParseFunction = new javax.swing.JComboBox();
        jComboBoxMergeFunction = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        advisorLabel = new javax.swing.JLabel();
        advisorFunctionCombo = new javax.swing.JComboBox();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/radixware/kernel/common/design/msdleditor/field/panel/Bundle"); // NOI18N
        setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("SelectParseMergeFunctionPanel.border.title"))); // NOI18N

        jComboBoxParseFunction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxParseFunctionActionPerformed(evt);
            }
        });

        jComboBoxMergeFunction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxMergeFunctionActionPerformed(evt);
            }
        });

        java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("org/radixware/kernel/designer/ads/editors/msdl/Bundle"); // NOI18N
        jLabel1.setText(bundle1.getString("SelectParseMergeFunctionPanel.jLabel1.text")); // NOI18N

        jLabel2.setText(bundle1.getString("SelectParseMergeFunctionPanel.jLabel2.text")); // NOI18N

        jLabel3.setText(bundle1.getString("SelectParseMergeFunctionPanel.jLabel3.text")); // NOI18N

        jLabel4.setText(bundle1.getString("SelectParseMergeFunctionPanel.jLabel4.text")); // NOI18N

        advisorLabel.setText(org.openide.util.NbBundle.getMessage(SelectParseMergeFunctionPanel.class, "SelectParseMergeFunctionPanel.advisorLabel.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addComponent(jComboBoxMergeFunction, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(10, 10, 10)
                        .addComponent(jComboBoxParseFunction, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(advisorLabel))
                        .addContainerGap(193, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(advisorFunctionCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(0, 0, 0)
                .addComponent(jLabel4)
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxParseFunction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBoxMergeFunction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(5, 5, 5)
                .addComponent(advisorLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(advisorFunctionCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getAccessibleContext().setAccessibleName(bundle.getString("PREPROCESSOR")); // NOI18N
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxParseFunctionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxParseFunctionActionPerformed
        if (!opened)
            return;
        PreprocessorFunctionItem item = (PreprocessorFunctionItem) jComboBoxParseFunction.getSelectedItem();
        field.setParseFunctionName(item.getId());
        fieldModel.setEditState(EEditState.MODIFIED);
    }//GEN-LAST:event_jComboBoxParseFunctionActionPerformed

    private void jComboBoxMergeFunctionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxMergeFunctionActionPerformed
        if (!opened)
            return;
        PreprocessorFunctionItem item = (PreprocessorFunctionItem) jComboBoxMergeFunction.getSelectedItem();
        field.setMergeFunctionName(item.getId());
        fieldModel.setEditState(EEditState.MODIFIED);
    }//GEN-LAST:event_jComboBoxMergeFunctionActionPerformed

     private void selectorAdvisorActionPerformed(java.awt.event.ActionEvent evt) {                                                
        if (!opened || !isChoice)
            return;
        PreprocessorFunctionItem item = (PreprocessorFunctionItem) advisorFunctionCombo.getSelectedItem();

        ((ChoiceField)field).setSelectorAdvisorFunctionName(item.getId());
        fieldModel.setEditState(EEditState.MODIFIED);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox advisorFunctionCombo;
    private javax.swing.JLabel advisorLabel;
    private javax.swing.JComboBox jComboBoxMergeFunction;
    private javax.swing.JComboBox jComboBoxParseFunction;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables
}
