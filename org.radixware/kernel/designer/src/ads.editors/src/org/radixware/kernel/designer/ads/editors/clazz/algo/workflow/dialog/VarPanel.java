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

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.*;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.designer.common.dialogs.choosetype.ChooseType;


public class VarPanel extends EditorDialog.EditorPanel<AdsVarObject> {

    public VarPanel(AdsVarObject node) {
        super(node);
        initComponents();

        textName.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                obj.setName(textName.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                obj.setName(textName.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                obj.setName(textName.getText());
            }
        });

        chkPersistent.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                obj.setValPersistent(chkPersistent.isSelected());
            }
        });

        valueEditor.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                final ValAsStr defaultValue = valueEditor.getValue();
                obj.setValue(defaultValue);
            }
        });

        textName.setEnabled(!node.isReadOnly());
        textType.setEnabled(!node.isReadOnly());
        buttonType.setEnabled(!node.isReadOnly());
        valueEditor.setEnabled(!node.isReadOnly());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelName = new javax.swing.JLabel();
        textName = new javax.swing.JTextField();
        textType = new javax.swing.JTextField();
        labelType = new javax.swing.JLabel();
        buttonType = new javax.swing.JButton();
        chkPersistent = new javax.swing.JCheckBox();
        valueEditor = new org.radixware.kernel.designer.common.dialogs.components.PropertyValueEditPanel();
        labelValue = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.inactiveCaption));
        setMaximumSize(new java.awt.Dimension(400, 120));
        setMinimumSize(new java.awt.Dimension(400, 120));
        setPreferredSize(new java.awt.Dimension(400, 120));
        setRequestFocusEnabled(false);

        labelName.setText(org.openide.util.NbBundle.getMessage(VarPanel.class, "VarPanel.labelName.text")); // NOI18N

        textType.setEditable(false);

        labelType.setText(org.openide.util.NbBundle.getMessage(VarPanel.class, "VarPanel.labelType.text")); // NOI18N

        buttonType.setText(org.openide.util.NbBundle.getMessage(VarPanel.class, "VarPanel.buttonType.text")); // NOI18N
        buttonType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonTypeActionPerformed(evt);
            }
        });

        chkPersistent.setText(org.openide.util.NbBundle.getMessage(VarPanel.class, "VarPanel.chkPersistent.text")); // NOI18N
        chkPersistent.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        labelValue.setText(org.openide.util.NbBundle.getMessage(VarPanel.class, "VarPanel.labelValue.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelName)
                    .addComponent(labelType)
                    .addComponent(labelValue))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textName, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                            .addComponent(textType, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonType, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(valueEditor, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                            .addComponent(chkPersistent))
                        .addGap(41, 41, 41))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelName)
                    .addComponent(textName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelType)
                    .addComponent(buttonType))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkPersistent)
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(valueEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelValue))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonTypeActionPerformed
        AdsTypeDeclaration type = ChooseType.getInstance().chooseType(new ChooseType.DefaultTypeFilter(obj.getOwnerClass(), obj));
        if (type != null) {
            obj.setType(type);
            textType.setText(type.getName(obj));

            final AdsType t = type.resolve(obj).get();
            if (t instanceof AdsEnumType) {
                valueEditor.setEnum(((AdsEnumType) t).getSource(), null);
            } else
                valueEditor.setDefaultValue(type.getTypeId());

            update();
        }
}//GEN-LAST:event_buttonTypeActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonType;
    private javax.swing.JCheckBox chkPersistent;
    private javax.swing.JLabel labelName;
    private javax.swing.JLabel labelType;
    private javax.swing.JLabel labelValue;
    private javax.swing.JTextField textName;
    private javax.swing.JTextField textType;
    private org.radixware.kernel.designer.common.dialogs.components.PropertyValueEditPanel valueEditor;
    // End of variables declaration//GEN-END:variables

    @Override
    public void init() {
        textName.setText(obj.getName());

        chkPersistent.setSelected(obj.isValPersistent());

        AdsTypeDeclaration type = obj.getType();
        textType.setText(type.getName(obj));

        final AdsType t = type.resolve(obj).get();
        if (t instanceof AdsEnumType) {
            valueEditor.setEnum(((AdsEnumType) t).getSource(), obj.getValue());
        } else
            valueEditor.setValue(type.getTypeId(), obj.getValue());

        update();
    }

    @Override
    public void apply() {
    }

    private void update() {
        AdsTypeDeclaration type = obj.getType();
/*
        boolean canEdit =
                type.getTypeId() == EValType.ARR_BOOL ||
                type.getTypeId() == EValType.ARR_CHAR ||
                type.getTypeId() == EValType.ARR_DATE_TIME ||
                type.getTypeId() == EValType.ARR_INT ||
                type.getTypeId() == EValType.ARR_NUM ||
                type.getTypeId() == EValType.ARR_STR ||
                type.getTypeId() == EValType.BOOL ||
                type.getTypeId() == EValType.CHAR ||
                type.getTypeId() == EValType.DATE_TIME ||
                type.getTypeId() == EValType.INT ||
                type.getTypeId() == EValType.NUM ||
                type.getTypeId() == EValType.STR ||
                type.getTypeId() == EValType.JAVA_TYPE;
        valueEditor.setVisible(canEdit);
 */
        boolean canSave =
                type.getTypeId() == EValType.BIN ||
                type.getTypeId() == EValType.BOOL ||
                type.getTypeId() == EValType.CHAR ||
                type.getTypeId() == EValType.DATE_TIME ||
                type.getTypeId() == EValType.INT ||
                type.getTypeId() == EValType.NUM ||
                type.getTypeId() == EValType.STR ||
                type.getTypeId() == EValType.BLOB ||
                type.getTypeId() == EValType.CLOB ||
                type.getTypeId() == EValType.ARR_BIN ||
                type.getTypeId() == EValType.ARR_BOOL ||
                type.getTypeId() == EValType.ARR_CHAR ||
                type.getTypeId() == EValType.ARR_DATE_TIME ||
                type.getTypeId() == EValType.ARR_INT ||
                type.getTypeId() == EValType.ARR_NUM ||
                type.getTypeId() == EValType.ARR_STR ||
                type.getTypeId() == EValType.JAVA_TYPE ||
                type.getTypeId() == EValType.XML ||
                type.getTypeId() == EValType.OBJECT ||
                type.getTypeId() == EValType.USER_CLASS ||
                type.getTypeId() == EValType.PARENT_REF ||
                type.getTypeId() == EValType.JAVA_CLASS;
        chkPersistent.setEnabled(canSave);
        if (!canSave) {
            obj.setValPersistent(false);
            chkPersistent.setSelected(false);
        }
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(getClass(), "CTL_VarPanel");
    }
}
