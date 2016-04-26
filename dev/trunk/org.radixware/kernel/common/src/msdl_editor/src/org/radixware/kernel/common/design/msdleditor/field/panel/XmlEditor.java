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
 * XmlEditor.java
 *
 * Created on 28.10.2009, 16:17:02
 */
package org.radixware.kernel.common.design.msdleditor.field.panel;

import java.awt.Color;


public class XmlEditor extends javax.swing.JPanel {
    public enum EXmlEditorStates {
        EParseSuccess,
        EParseFailure,
        EMergeSuccess,
        EMergeFailure;
        private static boolean isSuccessfull(EXmlEditorStates s) {
            if (s == EParseSuccess || s == EMergeSuccess)
                return true;
            return false;
        }
    }

    /** Creates new form XmlEditor */
    public XmlEditor() {
        initComponents();
    }

    public void setValue(String value) {
        jEditorPane1.setText(value);
    }

    public String getValue() {
        return jEditorPane1.getText();
    }
    
    /**
     * Indicate Parse or Merge status via label.
     * @param state Operation state (taken from respective enumeration)
     */
    public void setStatus(EXmlEditorStates state) {
            jLabelStatus.setVisible(true);
            String text;
            switch (state) {
                case EParseSuccess:
                    text = "<Parsed successfully>";
                    break;
                case EParseFailure:
                    text = "<Parse failure>";
                    break;
                case EMergeSuccess:
                    text = "<Merged successfuly>";
                    break;
                case EMergeFailure:
                    text = "<Merge failure>";
                    break;
                default:
                    text = "";
            }
            
            Color c;
            if (EXmlEditorStates.isSuccessfull(state)) {
                float f[] = new float[3];
                Color.RGBtoHSB(27, 172, 11, f);
                c = Color.getHSBColor(f[0], f[1], f[2]);
            } 
            else {
                c = Color.RED;
            }
            
            jLabelStatus.setText(text);
            jLabelStatus.setForeground(c);
        }

    public void resetStatus() {
        jLabelStatus.setText("<Status>");
        jLabelStatus.setForeground(Color.BLACK);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelText = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jLabelStatus = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabelText.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/radixware/kernel/common/design/msdleditor/field/panel/Bundle"); // NOI18N
        jLabelText.setText(bundle.getString("TestPanel.jLabel1.text")); // NOI18N

        jEditorPane1.setContentType("text/xml");
        jScrollPane1.setViewportView(jEditorPane1);

        jLabelStatus.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabelStatus.setText("<Status>");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabelText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 477, Short.MAX_VALUE)
                .addComponent(jLabelStatus))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelText)
                    .addComponent(jLabelStatus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabelStatus;
    private javax.swing.JLabel jLabelText;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
