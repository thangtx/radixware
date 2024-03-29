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
 * ChoicePanel.java
 *
 * Created on 25.03.2009, 14:20:47
 */

package org.radixware.kernel.common.design.msdleditor.field.panel;

import javax.swing.JPanel;
import org.radixware.kernel.common.design.msdleditor.AbstractEditItem;
import org.radixware.kernel.common.msdl.fields.ChoiceFieldModel;


public class ChoicePanel extends AbstractEditItem {

    /** Creates new form ChoicePanel */
    public ChoicePanel() {
        initComponents();
    }

    public void open(ChoiceFieldModel fieldModel, JPanel enumPanel) {
        super.open(fieldModel.getMsdlField());
        strPanel1.open(fieldModel.getSelector(),enumPanel);
        piecePanel1.open(fieldModel.getSelector(), fieldModel.getSelector().getField());
        update();
    }

    @Override
    public void update() {
        strPanel1.update();
        piecePanel1.update();
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        strPanel1 = new org.radixware.kernel.common.design.msdleditor.field.panel.StrPanel();
        jPanel2 = new javax.swing.JPanel();
        piecePanel1 = new org.radixware.kernel.common.design.msdleditor.piece.PiecePanel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());
        jPanel1.add(strPanel1, java.awt.BorderLayout.CENTER);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/radixware/kernel/common/design/msdleditor/field/panel/Bundle"); // NOI18N
        jTabbedPane1.addTab(bundle.getString("ChoicePanel.jPanel1.TabConstraints.tabTitle"), jPanel1); // NOI18N

        jPanel2.setLayout(new java.awt.BorderLayout());
        jPanel2.add(piecePanel1, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab(bundle.getString("ChoicePanel.jPanel2.TabConstraints.tabTitle"), jPanel2); // NOI18N

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private org.radixware.kernel.common.design.msdleditor.piece.PiecePanel piecePanel1;
    private org.radixware.kernel.common.design.msdleditor.field.panel.StrPanel strPanel1;
    // End of variables declaration//GEN-END:variables

}
