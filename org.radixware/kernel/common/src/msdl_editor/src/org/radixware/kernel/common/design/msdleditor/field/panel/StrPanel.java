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
 * BodyStrField.java
 *
 * Created on 14 Ноябрь 2008 г., 13:21
 */

package org.radixware.kernel.common.design.msdleditor.field.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.radixware.kernel.common.design.msdleditor.AbstractEditItem;
import org.radixware.kernel.common.design.msdleditor.DefaultLayout;
import org.radixware.kernel.common.msdl.fields.AbstractFieldModel;
import org.radixware.kernel.common.msdl.fields.StrFieldModel;
import org.radixware.kernel.common.msdl.enums.EEncoding;
import org.radixware.kernel.common.msdl.enums.EStrCharSet;
import org.radixware.schemas.msdl.StrField;


public class StrPanel extends AbstractEditItem implements ActionListener {
    private StrField field;
    private AbstractFieldModel fieldModel;
    private boolean opened = false;
    private JPanel enumPanel = null;
    private JLabel labelEnum = null;

    /** Creates new form BodyStrField */
    public StrPanel() {
        initComponents();
        DefaultComboBoxModel smodel = new DefaultComboBoxModel();
        smodel.addElement(EEncoding.NONE);
        smodel.addElement(EEncoding.ASCII);
        smodel.addElement(EEncoding.CP866);
        smodel.addElement(EEncoding.CP1251);
        smodel.addElement(EEncoding.CP1252);
        smodel.addElement(EEncoding.EBCDIC);
        smodel.addElement(EEncoding.UTF8);
        simpleFieldPanel1.setEncodingComboBoxModel(smodel);
    }

    private void arrange() {
        final ArrayList<JLabel> l = new ArrayList<>();
        l.add(jLabel1);
        if (labelEnum != null) {
            l.add(labelEnum);
        }
        l.add(jLabel2);
        l.add(jLabel3);
        final ArrayList<JComponent> e = new ArrayList<>();
        e.add(jTextField1);
        if (enumPanel != null) {
            e.add(enumPanel);
        }
        e.add(charSetPanel1);
        e.add(jTextField2);
        DefaultLayout.doLayout(jPanel1, l, e, true);
    }

    public void open(StrFieldModel fieldModel, JPanel enumPanel) {
        super.open(fieldModel.getMsdlField());
        opened = false;
        this.enumPanel = enumPanel;
        if (enumPanel != null) {
            labelEnum = new javax.swing.JLabel();
            labelEnum.setText("Enum"); // NOI18N
        }
        field = fieldModel.getField();
        arrange();
        simpleFieldPanel1.open(fieldModel, EEncoding.getInstance(fieldModel.getEncoding()));

        charSetPanel1.addActionListener(this);
        charSetPanel1.getSetParentPanel().addActionListener(this);
        
        this.fieldModel = fieldModel;
        this.field = fieldModel.getField();
        update();

        final DocumentListener dl1 = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent evt) {
                defaultValueChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent evt) {
                defaultValueChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent evt) {
                defaultValueChanged();
            }
        };
        jTextField1.getDocument().addDocumentListener(dl1);
        
        final DocumentListener dl2 = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent evt) {
                charSetExpChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent evt) {
                charSetExpChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent evt) {
                charSetExpChanged();
            }
        };
        jTextField2.getDocument().addDocumentListener(dl2);
        opened = true;
    }

    @Override
    public void update() {
        opened = false;
        simpleFieldPanel1.update();
        if (field.isSetDefaultVal()) {
            jTextField1.setText(field.getDefaultVal());
        } else {
            jTextField1.setText("");
        }
        charSetPanel1.setCharSet(EStrCharSet.getInstance(field.getCharSetType()), EStrCharSet.getInstance(fieldModel.getCharSetType()));
        if (field.isSetCharSetExp()) {
            jTextField2.setText(field.getCharSetExp());
        } else {
            jTextField2.setText("");
        }
        
        boolean overwrite = false;
        EStrCharSet charSet = charSetPanel1.getCharSet();
        if (charSet == EStrCharSet.None) {
            overwrite = true;
            charSet = EStrCharSet.getInstance(fieldModel.getCharSetType());
        }

        jTextField2.setVisible(charSet == EStrCharSet.User);
        jLabel3.setVisible(charSet == EStrCharSet.User);
        if (!overwrite && charSet == EStrCharSet.User) {
            jTextField2.setEnabled(true);
        } else {
            jTextField2.setEnabled(false);
            jTextField2.setText(fieldModel.getCharSetExp());
        }
        
        opened = true;
        super.update();
    }

    public void save() {
        boolean overwrite = false;
        EStrCharSet charSet = charSetPanel1.getCharSet();
        if (charSet != EStrCharSet.None) {
            field.setCharSetType(charSet.getName());
        } else {
            overwrite = true;
            field.setCharSetType(null);
            charSet = EStrCharSet.getInstance(fieldModel.getCharSetType());
        }
        jTextField2.setVisible(charSet == EStrCharSet.User);
        jLabel3.setVisible(charSet == EStrCharSet.User);
        if (!overwrite && charSet == EStrCharSet.User) {
            jTextField2.setEnabled(true);
        } else {
            jTextField2.setEnabled(false);
            jTextField2.setText(fieldModel.getCharSetExp());
        }
        fieldModel.setModified();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        charSetPanel1 = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.CharSetPanel();
        jTextField2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        simpleFieldPanel1 = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.SimpleFieldPanel();

        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                formFocusLost(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/radixware/kernel/common/design/msdleditor/Bundle"); // NOI18N
        jLabel1.setText(bundle.getString("DEFAULT_VALUE")); // NOI18N

        java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("org/radixware/kernel/common/design/msdleditor/field/panel/Bundle"); // NOI18N
        jTextField1.setText(bundle1.getString("StrPanel.jTextField1.text")); // NOI18N
        jTextField1.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTextField1CaretUpdate(evt);
            }
        });

        jTextField2.setText(bundle1.getString("StrPanel.jTextField2.text")); // NOI18N
        jTextField2.setToolTipText(bundle1.getString("StrPanel.jLabel3.tootip")); // NOI18N

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText(bundle1.getString("StrPanel.jLabel2.text")); // NOI18N

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText(bundle1.getString("StrPanel.jLabel3.text")); // NOI18N
        jLabel3.setToolTipText(bundle1.getString("StrPanel.jLabel3.tootip")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(charSetPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(charSetPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(40, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(simpleFieldPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(simpleFieldPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(140, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

private void formFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusLost

}//GEN-LAST:event_formFocusLost

private void jTextField1CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTextField1CaretUpdate
}//GEN-LAST:event_jTextField1CaretUpdate

    private void defaultValueChanged() {
        if (!opened) {
            return;
        }
        if (jTextField1.getText().equals("")) {
            field.setDefaultVal(null);
        } else {
            field.setDefaultVal(jTextField1.getText());
        }
        fieldModel.setModified();
    }

    private void charSetExpChanged() {
        if (!opened) {
            return;
        }
        if (jTextField2.getText().equals("")) {
            field.setCharSetExp(null);
        } else {
            field.setCharSetExp(jTextField2.getText());
        }
        fieldModel.setModified();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.CharSetPanel charSetPanel1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.SimpleFieldPanel simpleFieldPanel1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (!opened) {
            return;
        }
        save();
    }
}