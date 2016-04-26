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
 * FieldNamingPanel.java
 *
 * Created on 20.03.2009, 11:48:06
 */

package org.radixware.kernel.common.design.msdleditor.field.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.radixware.kernel.common.design.msdleditor.AbstractEditItem;
import org.radixware.kernel.common.design.msdleditor.DefaultLayout;
import org.radixware.kernel.common.msdl.enums.EEncoding;
import org.radixware.kernel.common.msdl.fields.StructureFieldModel;


public class SequentallyOrderPanel extends AbstractEditItem implements ActionListener {
    private StructureFieldModel field;
    /** Creates new form FieldNamingPanel */
    public SequentallyOrderPanel() {
        initComponents();
        ArrayList<JLabel> l = new ArrayList<JLabel>();
        l.add(jLabel1);
        ArrayList<JComponent> e = new ArrayList<JComponent>();
        e.add(fieldSeparatorPanel);
        DefaultLayout.doLayout(this, l, e,true);
    }

    public void open (StructureFieldModel field) {
        super.open(field.getMsdlField());
        this.field = field;
        fieldSeparatorPanel.addActionListener(this);
        update();
    }

    @Override
    public void update() {
        fieldSeparatorPanel.setValue(field.getStructure().getFieldSeparator(), 
                EEncoding.getInstanceForHexViewType(field.getStructure().getFieldSeparatorViewType()));
        if (fieldSeparatorPanel.getViewEncoding() == EEncoding.HEX)
            fieldSeparatorPanel.setLimit(2);
        else {
            fieldSeparatorPanel.setLimit(1);
        }
        
        super.update();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        fieldSeparatorPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.HexPanel();

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Field Separator");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fieldSeparatorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(177, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldSeparatorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.HexPanel fieldSeparatorPanel;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {
        field.getStructure().setFieldSeparator(fieldSeparatorPanel.getValue());
        field.getStructure().setFieldSeparatorViewType(fieldSeparatorPanel.getViewEncoding().getValue());
        field.setModified();
    }

}
