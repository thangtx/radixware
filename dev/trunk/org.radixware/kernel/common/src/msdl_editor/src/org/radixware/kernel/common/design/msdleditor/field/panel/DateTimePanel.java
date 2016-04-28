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
 * DateTimePanel.java
 *
 * Created on 03.12.2008, 17:49:16
 */

package org.radixware.kernel.common.design.msdleditor.field.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.radixware.kernel.common.design.msdleditor.AbstractEditItem;
import org.radixware.kernel.common.design.msdleditor.DefaultLayout;
import org.radixware.kernel.common.design.msdleditor.enums.EDateTimeFormat;
import org.radixware.kernel.common.msdl.enums.EEncoding;
import org.radixware.kernel.common.msdl.fields.AbstractFieldModel;
import org.radixware.kernel.common.msdl.fields.DateTimeFieldModel;
import org.radixware.schemas.msdl.DateTimeField;



public class DateTimePanel extends AbstractEditItem implements ActionListener {

    private DateTimeField field;
    private AbstractFieldModel fieldModel;
    private boolean opened;
    /** Creates new form DateTimePanel */

    public DateTimePanel() {
        initComponents();
        ArrayList<JLabel> l = new ArrayList<JLabel>();
        l.add(jLabel1);
        l.add(jLabel2);
        ArrayList<JComponent> e = new ArrayList<JComponent>();
        e.add(dateTimeFormatPanel1);
        e.add(dateTimePatternPanel1);
        DefaultLayout.doLayout(jPanel1, l, e,true);
    }

    public void open(DateTimeFieldModel fieldModel) {
        super.open(fieldModel.getMsdlField());
        opened = false;
        this.field = (DateTimeField)fieldModel.getField();
        this.fieldModel = fieldModel;

        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement(EEncoding.NONE);
        model.addElement(EEncoding.ASCII);
        model.addElement(EEncoding.BCD);
        model.addElement(EEncoding.CP1251);
        model.addElement(EEncoding.CP1252);
        model.addElement(EEncoding.CP866);
        model.addElement(EEncoding.EBCDIC);
        model.addElement(EEncoding.UTF8);
        simpleFieldPanel1.setEncodingComboBoxModel(model);
        simpleFieldPanel1.open(fieldModel, EEncoding.getInstance(fieldModel.getEncoding()));

        dateTimeFormatPanel1.addActionListener(this);
        dateTimeFormatPanel1.getSetParentPanel().addActionListener(this);
        dateTimePatternPanel1.addActionListener(this);
        dateTimePatternPanel1.getSetParentPanel().addActionListener(this);
        update();
        opened = true;
    }

    @Override
    public void update() {
        opened = false;
        simpleFieldPanel1.update();
        dateTimeFormatPanel1.setFormat(EDateTimeFormat.getInstance(field.getFormat()), 
                                       EDateTimeFormat.getInstance(fieldModel.getDateTimeFormat(false)));
        dateTimePatternPanel1.setPattern(field.getPattern(), fieldModel.getDateTimePattern(false));
        dateTimePatternPanel1.setVisible(dateTimeFormatPanel1.getComboBoxValue() == EDateTimeFormat.STR);
        jLabel2.setVisible(dateTimeFormatPanel1.getComboBoxValue() == EDateTimeFormat.STR);
        opened = true;
        super.update();
    }
    
    private void save() {
        field.setFormat(dateTimeFormatPanel1.getFormat().getValue());
        field.setPattern(dateTimePatternPanel1.getString());
        dateTimePatternPanel1.setVisible(dateTimeFormatPanel1.getComboBoxValue() == EDateTimeFormat.STR);
        jLabel2.setVisible(dateTimeFormatPanel1.getComboBoxValue() == EDateTimeFormat.STR);
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
        dateTimeFormatPanel1 = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.DateTimeFormatPanel();
        dateTimePatternPanel1 = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.DateTimePatternPanel();
        jLabel2 = new javax.swing.JLabel();
        simpleFieldPanel1 = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.SimpleFieldPanel();

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/radixware/kernel/common/design/msdleditor/Bundle"); // NOI18N
        jLabel1.setText(bundle.getString("FORMAT")); // NOI18N

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("org/radixware/kernel/common/design/msdleditor/field/panel/Bundle"); // NOI18N
        jLabel2.setText(bundle1.getString("DateTimePanel.jLabel2.text")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dateTimeFormatPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                    .addComponent(dateTimePatternPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                    .addComponent(dateTimeFormatPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                    .addComponent(dateTimePatternPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(simpleFieldPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.DateTimeFormatPanel dateTimeFormatPanel1;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.DateTimePatternPanel dateTimePatternPanel1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.SimpleFieldPanel simpleFieldPanel1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!opened)
            return;
        save();
    }

}