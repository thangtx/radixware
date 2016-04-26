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
package org.radixware.kernel.common.design.msdleditor.field.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.apache.xmlbeans.XmlCursor;
import org.radixware.kernel.common.design.msdleditor.AbstractEditItem;
import org.radixware.kernel.common.msdl.EFieldType;

import org.radixware.kernel.common.msdl.EFieldsFormat;
import org.radixware.kernel.common.msdl.IFieldTemplateTextFieldRetriever;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.common.msdl.MsdlStructureFields;
import org.radixware.kernel.common.msdl.RootMsdlScheme;
import org.radixware.kernel.common.msdl.enums.EEncoding;
import org.radixware.kernel.common.msdl.fields.StructureFieldModel;
import org.radixware.kernel.common.msdl.fields.parser.structure.SmioFieldStructure;
import org.radixware.schemas.msdl.AnyField;
import org.radixware.schemas.msdl.Field;
import org.radixware.schemas.msdl.SimpleField;
import org.radixware.schemas.msdl.Structure;
import org.radixware.kernel.common.msdl.fields.parser.SmioField;

public class FieldHeaderPanel extends AbstractEditItem implements ActionListener {

    private AnyField field;
    private Field f;
    private MsdlField msdlField;
    boolean extIdIsHex;
    boolean opened = false;
    IFieldTemplateTextFieldRetriever templateFieldRetriever = null;
    private JPanel functionsPanel = null;

    public FieldHeaderPanel() {
        initComponents();
        jCheckBoxArray.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                jCheckBoxArrayStateChanged(e);
            }
        });
        if (!canBeUnion()) {
            jCheckBoxUnion.setVisible(false);
        }
        //hexPanel1.setMultiByteMode(true);
    }

    public void setDbfViewMode(boolean dbfMode) {
        additionalSettingsPanel.setVisible(!dbfMode);
    }

    private Field getField() {
        return msdlField.getFieldModel().getField();
    }

    private AnyField getAnyField() {
        return msdlField.getField();
    }

    public void open(MsdlField msdlField, JPanel functions, IFieldTemplateTextFieldRetriever templateRetriever) {
        super.open(msdlField);
        f = msdlField.getFieldModel().getField();
        field = msdlField.getField();
        opened = false;
        this.msdlField = msdlField;
        RootMsdlScheme rootMsdlScheme = msdlField.getRootMsdlScheme();
        jComboBoxFieldType.setModel(rootMsdlScheme.getFieldList(rootMsdlScheme.isDbf()));
        additionalSettingsPanel.removeAll();
        if (functions != null) {
            functionsPanel = functions;
            addFunctionsPanel();
        }
        additionalSettingsPanel.invalidate();
        opened = false;
        this.jComboBoxFieldType.getModel().setSelectedItem(msdlField.getType());
        hexPanel1.addActionListener(this);
        DocumentListener dl = new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                commentChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                commentChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                commentChanged();
            }
        };
        jTextAreaComment.getDocument().addDocumentListener(dl);
        DocumentListener nl = new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                nameChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                nameChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                nameChanged();
            }
        };
        jTextFieldName.getDocument().addDocumentListener(nl);

        //append template searcher panel if appropriate
        if (canBeTemplateInstance(this.msdlField)) {
            if (this.templateFieldRetriever == null) {
                this.templateFieldRetriever = templateRetriever;
                addTemplatePanel();
            }
        }
        opened = true;
        update();
    }

    private boolean canBeTemplateInstance(MsdlField f) {
        return f.getFieldModel().getType() == EFieldType.STRUCTURE
                || f.getFieldModel().getType() == EFieldType.CHOICE;
    }

    @Override
    public void update() {
        opened = false;
        jTextFieldName.setText(getMsdlField().getName());
        jCheckBoxRequired.getModel().setSelected(getField().getIsRequired());
        jCheckBoxNullable.setVisible(getField() instanceof SimpleField);
        jCheckBoxUnion.setVisible(canBeUnion());
        additionalSettingsPanel.removeAll();

        if (canBeArray()) {
            jCheckBoxArray.setSelected(getField().isSetIsFieldArray1() && getField().getIsFieldArray1().booleanValue());
        }

        if (getField() instanceof SimpleField) {
            SimpleField simpleField = (SimpleField) getField();
            jCheckBoxNullable.getModel().setSelected(simpleField.isSetIsNilable() && simpleField.getIsNilable());
        }
        if (f.isSetComment()) {
            jTextAreaComment.setText(f.getComment().newCursor().getTextValue());
        } else {
            jTextAreaComment.setText("");
        }

        if (canBeUnion()) {
            jCheckBoxUnion.setSelected(getField().isSetIsFieldUnion() && getField().getIsFieldUnion().booleanValue());
        }

        if (getField().isSetAbstract() && getField().getAbstract().booleanValue()) {
            jCheckBoxAbstract.setSelected(true);
        }

        updateExtId();
        opened = true;
        if (functionsPanel != null) {
            addFunctionsPanel();
        }
        if (this.templateFieldRetriever != null) {
            addTemplatePanel();
        }
        super.update();
    }

    @Override
    public void setReadOnly(boolean isReadOnly) {
        super.setReadOnly(isReadOnly);
        testFieldButton.setEnabled(true);
        jTextAreaComment.setEnabled(!isReadOnly());
    }

    public void updateExtId() {
        MsdlField parentMsdlField = msdlField.getParentMsdlField();
        if (parentMsdlField.getType() == EFieldType.STRUCTURE
                && msdlField.getContainer() instanceof MsdlStructureFields
                && (((StructureFieldModel) parentMsdlField.getFieldModel()).getStructureType() == EFieldsFormat.FIELD_NAMING
                || ((StructureFieldModel) parentMsdlField.getFieldModel()).getStructureType() == EFieldsFormat.BERTLV)) {
            Structure.Field f = (Structure.Field) getAnyField();
            hexPanel1.setValue(f.getExtId(), EEncoding.getInstanceForHexViewType(f.getExtIdViewType()));
            hexPanel1.setVisible(true);
            jLabelExtId.setVisible(true);
        } else {
            hexPanel1.setVisible(false);
            jLabelExtId.setVisible(false);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jTextFieldName = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaComment = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jComboBoxFieldType = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jCheckBoxRequired = new javax.swing.JCheckBox();
        jCheckBoxNullable = new javax.swing.JCheckBox();
        hexPanel1 = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.HexPanel();
        jLabelExtId = new javax.swing.JLabel();
        additionalSettingsPanel = new javax.swing.JPanel();
        testFieldButton = new javax.swing.JButton();
        jCheckBoxArray = new javax.swing.JCheckBox();
        jCheckBoxUnion = new javax.swing.JCheckBox();
        jCheckBoxAbstract = new javax.swing.JCheckBox();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/radixware/kernel/common/design/msdleditor/field/panel/Bundle"); // NOI18N
        setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("FieldHeaderPanel.border.title"))); // NOI18N
        setLayout(new java.awt.GridBagLayout());

        jTextFieldName.setPreferredSize(new java.awt.Dimension(220, 19));
        jTextFieldName.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTextFieldNameCaretUpdate(evt);
            }
        });
        jTextFieldName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldNameKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 0.8;
        add(jTextFieldName, gridBagConstraints);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setLabelFor(jTextFieldName);
        java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("org/radixware/kernel/common/design/msdleditor/Bundle"); // NOI18N
        jLabel1.setText(bundle1.getString("NAME")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(jLabel1, gridBagConstraints);

        jTextAreaComment.setColumns(20);
        jTextAreaComment.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jTextAreaComment.setRows(3);
        jTextAreaComment.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTextAreaCommentCaretUpdate(evt);
            }
        });
        jScrollPane1.setViewportView(jTextAreaComment);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.43;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        add(jScrollPane1, gridBagConstraints);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText(bundle1.getString("COMMENT")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(jLabel2, gridBagConstraints);

        jComboBoxFieldType.setMaximumRowCount(9);
        jComboBoxFieldType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxFieldTypeItemStateChanged(evt);
            }
        });
        jComboBoxFieldType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxFieldTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.43;
        add(jComboBoxFieldType, gridBagConstraints);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText(bundle1.getString("TYPE")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(jLabel3, gridBagConstraints);

        jCheckBoxRequired.setText(bundle1.getString("REQUIRED")); // NOI18N
        jCheckBoxRequired.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxRequiredActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        add(jCheckBoxRequired, gridBagConstraints);

        jCheckBoxNullable.setText(bundle1.getString("NULLABLE")); // NOI18N
        jCheckBoxNullable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxNullableActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        add(jCheckBoxNullable, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.43;
        add(hexPanel1, gridBagConstraints);

        jLabelExtId.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelExtId.setText(bundle.getString("FieldHeaderPanel.jLabelExtId.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(jLabelExtId, gridBagConstraints);

        additionalSettingsPanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.43;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(additionalSettingsPanel, gridBagConstraints);

        testFieldButton.setText(bundle.getString("RootHeaderPanel.jButtonTest.text")); // NOI18N
        testFieldButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testFieldButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        add(testFieldButton, gridBagConstraints);

        jCheckBoxArray.setText(bundle.getString("FieldHeaderPanel.jCheckBoxArray.text_1")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        add(jCheckBoxArray, gridBagConstraints);

        jCheckBoxUnion.setText(bundle.getString("FieldHeaderPanel.text")); // NOI18N
        jCheckBoxUnion.setName(""); // NOI18N
        jCheckBoxUnion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unionCheckBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        add(jCheckBoxUnion, gridBagConstraints);

        jCheckBoxAbstract.setText(bundle.getString("FieldHeaderPanel.jCheckBoxAbstract.text")); // NOI18N
        jCheckBoxAbstract.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxAbstractActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        add(jCheckBoxAbstract, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

private void jComboBoxFieldTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxFieldTypeActionPerformed
    if (!opened) {
        return;
    }
    EFieldType newType = (EFieldType) jComboBoxFieldType.getModel().getSelectedItem();
    msdlField.setType(newType);
    f = msdlField.getFieldModel().getField();
    this.jCheckBoxNullable.setVisible(f instanceof SimpleField);
}//GEN-LAST:event_jComboBoxFieldTypeActionPerformed

private void jComboBoxFieldTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxFieldTypeItemStateChanged

}//GEN-LAST:event_jComboBoxFieldTypeItemStateChanged

private void jCheckBoxRequiredActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxRequiredActionPerformed
    if (!opened) {
        return;
    }
    f.setIsRequired(jCheckBoxRequired.getModel().isSelected());
    msdlField.getModel().setModified();
}//GEN-LAST:event_jCheckBoxRequiredActionPerformed

private void jCheckBoxNullableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxNullableActionPerformed
    if (!opened) {
        return;
    }
    ((SimpleField) f).setIsNilable(jCheckBoxNullable.getModel().isSelected());
    msdlField.getModel().setModified();

}//GEN-LAST:event_jCheckBoxNullableActionPerformed

private void jTextFieldNameCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTextFieldNameCaretUpdate
}//GEN-LAST:event_jTextFieldNameCaretUpdate

    private void nameChanged() {
        if (!opened) {
            return;
        }
        msdlField.setName(jTextFieldName.getText());
        msdlField.getModel().setModified();

    }
private void jTextFieldNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNameKeyReleased
}//GEN-LAST:event_jTextFieldNameKeyReleased

    private TestDialog testDialog = null;

    private TestDialog getTestDialog() {
        if (testDialog == null) {
            Frame frm = (Frame) Window.getOwnerlessWindows()[0];
            testDialog = new TestDialog(frm, true);
            testDialog.pack();
            testDialog.setLocationRelativeTo(null);
        }
        return testDialog;
    }

private void testFieldButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testFieldButtonActionPerformed
    getTestDialog().open(msdlField.getFieldModel(), msdlField.getQualifiedName());
}//GEN-LAST:event_testFieldButtonActionPerformed

private void jTextAreaCommentCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTextAreaCommentCaretUpdate
}//GEN-LAST:event_jTextAreaCommentCaretUpdate

    private void unionCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unionCheckBoxActionPerformed
        if (!opened) {
            return;
        }

        if (canBeUnion()) {
            f.setIsFieldUnion(Boolean.valueOf(jCheckBoxUnion.getModel().isSelected()));
            msdlField.getModel().setModified();
        }
    }//GEN-LAST:event_unionCheckBoxActionPerformed

    private void jCheckBoxAbstractActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxAbstractActionPerformed
        if (!opened) {
            return;
        }

        boolean abstractSelected = jCheckBoxAbstract.isSelected();
        getField().setAbstract(new Boolean(abstractSelected));
        msdlField.getModel().setModified();
    }//GEN-LAST:event_jCheckBoxAbstractActionPerformed

    private void commentChanged() {
        if (!opened) {
            return;
        }
        if (f.isSetComment()) {
            if (jTextAreaComment.getText().equals("")) {
                f.unsetComment();
            } else {
                XmlCursor c = f.getComment().newCursor();
                if (!jTextAreaComment.getText().equals(c.getTextValue())) {
                    c.setTextValue(jTextAreaComment.getText());
                }
                c.dispose();
            }
        } else {
            if (!jTextAreaComment.getText().equals("")) {
                f.addNewComment();
                f.getComment().newCursor().setTextValue(jTextAreaComment.getText());
            }
        }
        msdlField.getModel().setModified();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel additionalSettingsPanel;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.HexPanel hexPanel1;
    private javax.swing.JCheckBox jCheckBoxAbstract;
    private javax.swing.JCheckBox jCheckBoxArray;
    private javax.swing.JCheckBox jCheckBoxNullable;
    private javax.swing.JCheckBox jCheckBoxRequired;
    private javax.swing.JCheckBox jCheckBoxUnion;
    private javax.swing.JComboBox jComboBoxFieldType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelExtId;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextAreaComment;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JButton testFieldButton;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!opened) {
            return;
        }
        ((Structure.Field) field).setExtId(hexPanel1.getValue());
        ((Structure.Field) field).setExtIdViewType(hexPanel1.getViewEncoding().getValue());
        msdlField.setModified();
    }

    //WARNING: Public Morozov
    public void setArrayEnabled(boolean val) {
        jCheckBoxArray.setEnabled(val);
    }

    public boolean canBeArray() {
        boolean ret = false;
        EFieldType parentType = msdlField.getParentMsdlField().getFieldModel().getType();
        SmioField parentField = msdlField.getParentMsdlField().getFieldModel().getParser();
        if (parentType == EFieldType.STRUCTURE && parentField instanceof SmioFieldStructure) {
            SmioFieldStructure parentStructure = (SmioFieldStructure) parentField;
            EFieldsFormat parentFieldFormat = parentStructure.getStructureType();
            if (parentFieldFormat == EFieldsFormat.FIELD_NAMING || parentFieldFormat == EFieldsFormat.BERTLV) {
                ret = true;
            }
        }
        return ret;
    }

    private void jCheckBoxArrayStateChanged(ItemEvent e) {
        if (canBeArray()) {
            boolean modified = false;
            if (e.getStateChange() == ItemEvent.DESELECTED && (f.isSetIsFieldArray1() && f.getIsFieldArray1().booleanValue())) {
                f.setIsFieldArray1(false);
                modified = true;
            } else {
                if (!f.isSetIsFieldArray1() || !f.getIsFieldArray1().booleanValue()) {
                    f.setIsFieldArray1(true);
                    modified = true;
                }
            }
            if (modified) {
                msdlField.getModel().setModified();
            }
        }
    }

    private boolean canBeUnion() {
        boolean ret = false;
        if (msdlField == null) {
            return false;
        }

        EFieldType parentType = msdlField.getParentMsdlField().getFieldModel().getType();
        SmioField parentField = msdlField.getParentMsdlField().getFieldModel().getParser();
        boolean parentIsStructure = parentType == EFieldType.STRUCTURE;
        boolean parentHasNamedFields = false;

        if (parentField instanceof SmioFieldStructure) {
            parentHasNamedFields = ((SmioFieldStructure) parentField).getStructureType() == EFieldsFormat.FIELD_NAMING;
        }

        boolean currentFieldIsStructure = msdlField.getFieldModel() instanceof StructureFieldModel;
        ret = parentIsStructure && parentHasNamedFields && currentFieldIsStructure;
        return ret;
    }

    private void jCheckBoxUnionStateChanged(ItemEvent e) {
        if (canBeUnion()) {
            boolean modified = false;
            if (e.getStateChange() == ItemEvent.DESELECTED && (f.isSetIsFieldUnion() && f.getIsFieldUnion().booleanValue())) {
                f.setIsFieldUnion(false);
                modified = true;
            } else {
                if (!f.isSetIsFieldUnion() || !f.getIsFieldUnion().booleanValue()) {
                    f.setIsFieldUnion(true);
                    modified = true;
                }
            }
            if (modified) {
                msdlField.getModel().setModified();
            }
        }
    }

    private void addTemplatePanel() {
        if (additionalSettingsPanel.getComponents().length < 1){
            additionalSettingsPanel.add(this.templateFieldRetriever.getTemplateTextField(),BorderLayout.PAGE_START);
        } else {
            additionalSettingsPanel.add(this.templateFieldRetriever.getTemplateTextField(),BorderLayout.CENTER);
        }
    }

    private void addFunctionsPanel() {
        additionalSettingsPanel.add(functionsPanel,BorderLayout.PAGE_START);
    }
}
