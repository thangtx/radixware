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
 * IntPanel.java
 *
 * Created on 03.12.2008, 14:57:03
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
import org.radixware.kernel.common.msdl.fields.IntFieldModel;
import org.radixware.kernel.common.msdl.enums.EEncoding;
import org.radixware.schemas.msdl.IntField;


public class IntPanel extends AbstractEditItem implements ActionListener {
    private IntField field;
    private AbstractFieldModel fieldModel;
    boolean opened = false;
    private JLabel labelEnum = null;
    private JPanel enumPanel = null;
    
    /** Creates new form IntPanel */

    public IntPanel() {
        initComponents();
        DefaultComboBoxModel smodel = new DefaultComboBoxModel();
        smodel.addElement(EEncoding.NONE);
        smodel.addElement(EEncoding.HEX);
        smodel.addElement(EEncoding.BCD);
        smodel.addElement(EEncoding.ASCII);
        smodel.addElement(EEncoding.CP1251);
        smodel.addElement(EEncoding.CP1252);
        smodel.addElement(EEncoding.EBCDIC);
        smodel.addElement(EEncoding.CP866);
        smodel.addElement(EEncoding.UTF8);
        smodel.addElement(EEncoding.BIGENDIANBIN);
        smodel.addElement(EEncoding.LITTLEENDIANBIN);
        simpleFieldPanel1.setEncodingComboBoxModel(smodel);
    }

    private void arrange() {
        ArrayList<JLabel> l = new ArrayList<JLabel>();
        l.add(jLabel1);
        l.add(jLabel2);
        l.add(jLabel3);
        if (labelEnum != null)
            l.add(labelEnum);
        ArrayList<JComponent> e = new ArrayList<JComponent>();
        e.add(extCharPanelPlusSign);
        e.add(extCharPanelMinusSign);
        e.add(jTextField1);
        if (enumPanel != null)
            e.add(enumPanel);
        DefaultLayout.doLayout(jPanel1, l, e,true);
    }

    public void open(IntFieldModel fieldModel, JPanel enumPanel) {
        super.open(fieldModel.getMsdlField());
        this.enumPanel = enumPanel;
        if (enumPanel != null) {
            labelEnum = new javax.swing.JLabel();
            labelEnum.setText("Enum"); // NOI18N
        }
        arrange();
        this.field = (IntField)fieldModel.getField();
        this.fieldModel = fieldModel;
        simpleFieldPanel1.open(fieldModel, EEncoding.getInstance(fieldModel.getIntEncoding(false)));
        update();
        DocumentListener dl = new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                defaultValueChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                defaultValueChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                defaultValueChanged();
            }
        };
        jTextField1.getDocument().addDocumentListener(dl);
        extCharPanelPlusSign.addActionListener(this);
        extCharPanelMinusSign.addActionListener(this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        simpleFieldPanel1 = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.SimpleFieldPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        extCharPanelPlusSign = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtCharPanel();
        jLabel2 = new javax.swing.JLabel();
        extCharPanelMinusSign = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtCharPanel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/radixware/kernel/common/design/msdleditor/Bundle"); // NOI18N
        jLabel1.setText(bundle.getString("PLUS_SIGN")); // NOI18N

        jLabel2.setText(bundle.getString("MINUS_SIGN")); // NOI18N

        jLabel3.setText(bundle.getString("DEFAULT_VALUE")); // NOI18N

        java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("org/radixware/kernel/common/design/msdleditor/field/panel/Bundle"); // NOI18N
        jTextField1.setText(bundle1.getString("IntPanel.jTextField1.text")); // NOI18N
        jTextField1.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTextField1CaretUpdate(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(4, 4, 4)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(extCharPanelPlusSign, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                    .addComponent(extCharPanelMinusSign, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(extCharPanelPlusSign, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(extCharPanelMinusSign, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(simpleFieldPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(simpleFieldPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTextField1CaretUpdate
    }//GEN-LAST:event_jTextField1CaretUpdate

    private void defaultValueChanged() {
        if (!opened)
            return;
        if (jTextField1.getText().equals(""))
            field.setDefaultVal(null);
        else
            try {
                field.setDefaultVal(Long.parseLong(jTextField1.getText()));
            }
            catch (java.lang.NumberFormatException e) {
//                field.setDefaultVal(null);
//                jFormattedTextFieldDefaultVal.setText("");
            }
        fieldModel.setModified();
    }

    @Override
    public void update() {
        opened = false;
        extCharPanelPlusSign.setCharacter(field.getPlusSign(), fieldModel.getPlusSign(false));
        extCharPanelMinusSign.setCharacter(field.getMinusSign(), fieldModel.getMinusSign(false));
        if (field.getDefaultVal() != null)
            jTextField1.setText(field.getDefaultVal().toString());
        else
            jTextField1.setText("");
        super.update();
        opened = true;
    }

    private void save() {
        field.setPlusSign(extCharPanelPlusSign.getCharacter());
        field.setMinusSign(extCharPanelMinusSign.getCharacter());
        fieldModel.setModified();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtCharPanel extCharPanelMinusSign;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtCharPanel extCharPanelPlusSign;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.SimpleFieldPanel simpleFieldPanel1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!opened)
            return;
        save();
    }

}
