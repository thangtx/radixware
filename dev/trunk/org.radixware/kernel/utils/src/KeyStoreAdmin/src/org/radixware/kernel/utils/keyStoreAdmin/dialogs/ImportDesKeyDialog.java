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

package org.radixware.kernel.utils.keyStoreAdmin.dialogs;

import java.util.ResourceBundle;
import javax.swing.JOptionPane;



public class ImportDesKeyDialog extends AKeyStoreAdminDialog {

    final private String[] aliases;
    private String alias;
    
    private static final ResourceBundle bundle = ResourceBundle.getBundle("org/radixware/kernel/utils/keyStoreAdmin/resources/GenerateDesKeyDialog");
    
    /** Creates new form ImportDesKeyDialog */
    public ImportDesKeyDialog(java.awt.Frame parent, String[] aliases, String checkValue) {
        super(parent);
        initComponents();
        jTextFieldCheckValue.setText(checkValue);
        
        this.aliases = aliases;
    }

    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
    public int getReturnStatus() {
        return returnStatus;
    }
    
    public String getAlias() {
        return alias;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jLabelAlias = new javax.swing.JLabel();
        jTextFieldAlias = new javax.swing.JTextField();
        jLabelCheckValue = new javax.swing.JLabel();
        jTextFieldCheckValue = new javax.swing.JTextField();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/radixware/kernel/utils/keyStoreAdmin/resources/ImportDesKeyDialog"); // NOI18N
        setTitle(bundle.getString("Dialog.title")); // NOI18N
        setModal(true);
        setName("Form"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        okButton.setText(bundle.getString("okButton.text")); // NOI18N
        okButton.setName("okButton"); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText(bundle.getString("cancelButton.text")); // NOI18N
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jLabelAlias.setLabelFor(jTextFieldAlias);
        jLabelAlias.setText(bundle.getString("jLabelAlias.text")); // NOI18N
        jLabelAlias.setName("jLabelAlias"); // NOI18N

        jTextFieldAlias.setName("jTextFieldAlias"); // NOI18N

        jLabelCheckValue.setLabelFor(jTextFieldCheckValue);
        jLabelCheckValue.setText(bundle.getString("jLabelCheckValue.text")); // NOI18N
        jLabelCheckValue.setName("jLabelCheckValue"); // NOI18N

        jTextFieldCheckValue.setEditable(false);
        jTextFieldCheckValue.setName("jTextFieldCheckValue"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelAlias, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelCheckValue)
                                .addGap(16, 16, 16)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldAlias, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                            .addComponent(jTextFieldCheckValue, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE))))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelAlias)
                    .addComponent(jTextFieldAlias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCheckValue)
                    .addComponent(jTextFieldCheckValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton))
                .addContainerGap())
        );

        getRootPane().setDefaultButton(okButton);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        doClose(RET_OK);
    }//GEN-LAST:event_okButtonActionPerformed
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog
    
    protected void doClose(int retStatus) {
        returnStatus = retStatus;
        if (returnStatus==RET_OK){
            if (!checkInput())
                return;

            alias = jTextFieldAlias.getText();
        }
        setVisible(false);
        dispose();
    }
    
    /** Checks that input values are valid */
    private boolean checkInput(){
        String dialogTitle = bundle.getString("JOptionPaneInputError.title");

        if (jTextFieldAlias.getText().length()==0){
            String dialogMessage = bundle.getString("AliasIsEmpty");
            JOptionPane.showMessageDialog(this, dialogMessage, dialogTitle, JOptionPane.ERROR_MESSAGE);
            jTextFieldAlias.requestFocusInWindow();
            return false;
        }

        for (String existingAlias: aliases){
            if (jTextFieldAlias.getText().equals(existingAlias)){
                String dialogMessage = String.format(bundle.getString("AliasExists"), existingAlias);
                int confirm = JOptionPane.showConfirmDialog(this, dialogMessage, dialogTitle, JOptionPane.YES_NO_OPTION);
                if (confirm==JOptionPane.NO_OPTION){
                    jTextFieldAlias.requestFocusInWindow();
                    return false;
                }
                break;
            }
        }

        return true;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel jLabelAlias;
    private javax.swing.JLabel jLabelCheckValue;
    private javax.swing.JTextField jTextFieldAlias;
    private javax.swing.JTextField jTextFieldCheckValue;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
}
