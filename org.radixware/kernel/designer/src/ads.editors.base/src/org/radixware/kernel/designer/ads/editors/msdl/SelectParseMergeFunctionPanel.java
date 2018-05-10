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

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
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
        final ListCellRenderer renderer = new FunctionListRenderer();
        final ItemListener itemSelected = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    changeSelectedItemColor((JComboBox) e.getSource(), (PreprocessorFunctionItem) e.getItem());
                }
            }
        };
        
        modelParse.addElement(notDefined);
        modelMerge.addElement(notDefined);
        jComboBoxParseFunction.setModel(modelParse);
        jComboBoxMergeFunction.setModel(modelMerge);
        
        jComboBoxParseFunction.addItemListener(itemSelected);
        jComboBoxMergeFunction.addItemListener(itemSelected);
        
        jComboBoxParseFunction.setRenderer(renderer);
        jComboBoxMergeFunction.setRenderer(renderer);
        
        jComboBoxParseFunction.setSelectedItem(notDefined);
        jComboBoxMergeFunction.setSelectedItem(notDefined);
        DefaultComboBoxModel modelAdvisor = null;
        if(fieldModel.getField() instanceof ChoiceField) {
            modelAdvisor = new DefaultComboBoxModel();
            modelAdvisor.addElement(notDefined);
            advisorFunctionCombo.setModel(modelAdvisor);
            advisorFunctionCombo.setRenderer(renderer);
            advisorFunctionCombo.addItemListener(itemSelected);
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
            final String mergeFuncName = field.getFieldModel().getField().getMergeFunctionName();
            final String parseFuncName = field.getFieldModel().getField().getParseFunctionName();
            final String choiceFuncName = isChoice ? ((ChoiceField)fieldModel.getField()).getSelectorAdvisorFunctionName() : null;
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
            
            fillFunctionComboBox(modelMerge, write, mergeFuncName);
            fillFunctionComboBox(modelParse, read, parseFuncName);
            fillFunctionComboBox(modelAdvisor, advice, choiceFuncName);            
        }
        else {
            jComboBoxMergeFunction.setEnabled(false);
            jComboBoxParseFunction.setEnabled(false);
        }
        opened = true;
    }
    
    private void changeSelectedItemColor(JComboBox comboBox, PreprocessorFunctionItem funcItem) {
        if (funcItem.failed) {
            comboBox.setForeground(Color.red);
        } else {
            comboBox.setForeground(Color.black);
        }
    }
    
    private static void fillFunctionComboBox(DefaultComboBoxModel model, PreprocessorFunctionItem selectedItem, String funcName) {
        if (selectedItem != null) {
            model.setSelectedItem(selectedItem);
        } else if (funcName != null) {
            final PreprocessorFunctionItem notFoundItem = new PreprocessorFunctionItem(null, funcName);
            model.addElement(notFoundItem);
            model.setSelectedItem(notFoundItem);
        }
    }
    
    private static class FunctionListRenderer extends DefaultListCellRenderer {
        
        @Override
        public Component getListCellRendererComponent(JList<?> list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {

            final Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            final PreprocessorFunctionItem funcItem = (PreprocessorFunctionItem) value;
            if (funcItem.failed) {
                c.setForeground(Color.red);
            }
            return c;
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

        jComboBoxParseFunction = new javax.swing.JComboBox();
        jComboBoxMergeFunction = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
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

        advisorLabel.setText(org.openide.util.NbBundle.getMessage(SelectParseMergeFunctionPanel.class, "SelectParseMergeFunctionPanel.advisorLabel.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(advisorLabel))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(7, 7, 7)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBoxParseFunction, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jComboBoxMergeFunction, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(advisorFunctionCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
    // End of variables declaration//GEN-END:variables
}
