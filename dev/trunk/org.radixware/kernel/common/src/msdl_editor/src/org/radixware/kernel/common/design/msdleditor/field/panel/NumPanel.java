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
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.radixware.kernel.common.design.msdleditor.AbstractEditItem;
import org.radixware.kernel.common.design.msdleditor.DefaultLayout;
import org.radixware.kernel.common.msdl.enums.EEncoding;
import org.radixware.kernel.common.msdl.fields.AbstractFieldModel;
import org.radixware.kernel.common.msdl.fields.NumFieldModel;
import org.radixware.schemas.msdl.NumField;


public class NumPanel extends AbstractEditItem implements ActionListener {

    private NumField field;
    private AbstractFieldModel fieldModel;
    boolean opened = false;

    /** Creates new form IntPanel */
    public NumPanel() {
        initComponents();
        ArrayList<JLabel> l = new ArrayList<JLabel>();
        l.add(jLabel1);
        l.add(jLabel2);
        l.add(jLabel4);
        l.add(jLabel3);
        ArrayList<JComponent> e = new ArrayList<JComponent>();
        e.add(extCharPanelPlusSign);
        e.add(extCharPanelMinusSign);
        e.add(extCharPanelFracPoint);
        e.add(jTextField1);
        DefaultLayout.doLayout(jPanel1, l, e,true);

    }

    public void open(NumFieldModel fieldModel) {
        super.open(fieldModel.getMsdlField());
        this.field = (NumField)fieldModel.getField();
        this.fieldModel = fieldModel;
        DefaultComboBoxModel smodel = new DefaultComboBoxModel();
        smodel.addElement(EEncoding.NONE);
        smodel.addElement(EEncoding.ASCII);
        smodel.addElement(EEncoding.CP866);
        smodel.addElement(EEncoding.CP1251);
        smodel.addElement(EEncoding.CP1252);
        smodel.addElement(EEncoding.EBCDIC);
        smodel.addElement(EEncoding.EBCDIC_CP1047);
        smodel.addElement(EEncoding.UTF8);
        simpleFieldPanel1.setEncodingComboBoxModel(smodel);
        simpleFieldPanel1.open(fieldModel, EEncoding.getInstance(fieldModel.calcEncoding(false)));
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
        extCharPanelFracPoint.addActionListener(this);
    }

    @Override
    public void update() {
        opened = false;
        simpleFieldPanel1.update();
        extCharPanelPlusSign.setCharacter(field.getPlusSign(), fieldModel.getPlusSign(false));
        extCharPanelMinusSign.setCharacter(field.getMinusSign(), fieldModel.getMinusSign(false));
        extCharPanelFracPoint.setCharacter(field.getFractionalPoint(), fieldModel.getFractionalPoint(false));
        opened = false;
        if (field.isSetDefaultVal())
            jTextField1.setText(field.getDefaultVal().toString());
        else
            jTextField1.setText("");
        opened = true;
        super.update();
    }

    private void save(ActionEvent e) {
        field.setPlusSign(extCharPanelPlusSign.getCharacter());
        field.setMinusSign(extCharPanelMinusSign.getCharacter());
        field.setFractionalPoint(extCharPanelFracPoint.getCharacter());
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

        simpleFieldPanel1 = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.SimpleFieldPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        extCharPanelPlusSign = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtCharPanel();
        jLabel2 = new javax.swing.JLabel();
        extCharPanelMinusSign = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtCharPanel();
        jLabel4 = new javax.swing.JLabel();
        extCharPanelFracPoint = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtCharPanel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/radixware/kernel/common/design/msdleditor/Bundle"); // NOI18N
        jLabel1.setText(bundle.getString("PLUS_SIGN")); // NOI18N

        jLabel2.setText(bundle.getString("MINUS_SIGN")); // NOI18N

        jLabel4.setText(bundle.getString("FRACTIONAL_POINT")); // NOI18N

        jLabel3.setText(bundle.getString("DEFAULT_VALUE")); // NOI18N

        java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("org/radixware/kernel/common/design/msdleditor/field/panel/Bundle"); // NOI18N
        jTextField1.setText(bundle1.getString("NumPanel.jTextField1.text")); // NOI18N
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
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addComponent(extCharPanelMinusSign, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addComponent(extCharPanelPlusSign, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addComponent(extCharPanelFracPoint, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE))
                .addGap(27, 27, 27))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(extCharPanelPlusSign, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(extCharPanelMinusSign, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(extCharPanelFracPoint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(simpleFieldPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
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
        else {
            try {
                BigDecimal d = new BigDecimal(jTextField1.getText());
                field.setDefaultVal(d);
            }
            catch (java.lang.NumberFormatException e) {
                //jTextField1.setText("");
                //field.setDefaultVal(null);
            }
        }
        fieldModel.setModified();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtCharPanel extCharPanelFracPoint;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtCharPanel extCharPanelMinusSign;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtCharPanel extCharPanelPlusSign;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.SimpleFieldPanel simpleFieldPanel1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!opened)
            return;
        save(e);
    }

}
