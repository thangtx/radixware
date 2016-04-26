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

import java.io.File;
import java.util.Arrays;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;

import org.jdesktop.application.Action;

import org.radixware.kernel.utils.keyStoreAdmin.utils.KeyStoreAdminFileFilter;
import org.radixware.kernel.utils.keyStoreAdmin.utils.KeyStoreAdminFileSelector;
import org.radixware.kernel.utils.keyStoreAdmin.utils.KeyStoreAdminFileSelector.SelectNewFileMode;


        
public class OpenKeyStoreDialog extends AKeyStoreAdminDialog {

    private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/radixware/kernel/utils/keyStoreAdmin/resources/OpenKeyStoreDialog");

    private String keystorePath;
    private char[] password;
    private final String defaultKeystorePath;
    private final boolean createKeystore;

    /** Creates new form OpenKeyStoreDialog */
    public OpenKeyStoreDialog(java.awt.Frame parent, String defaultKeystorePath, boolean createKeystore) {
        super(parent);
        initComponents();

        this.defaultKeystorePath = defaultKeystorePath;
        this.createKeystore = createKeystore;
        if (createKeystore)
            this.setTitle(bundle.getString("Dialog.titleCreate"));
        else{
            jLabelPasswordConfirmation.setVisible(false);
            jPasswordFieldConfirmation.setVisible(false);
        }
    }

    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
    public int getReturnStatus() {
        return returnStatus;
    }

    public String getKeyStorePath(){
        return keystorePath;
    }

    public char[] getKeyStorePassword(){
        return password;
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
        jLabelKeyStoreFile = new javax.swing.JLabel();
        jTextFieldKeyStoreFile = new javax.swing.JTextField();
        jButtonKeyStoreFile = new javax.swing.JButton();
        jLabelPassword = new javax.swing.JLabel();
        jPasswordField = new javax.swing.JPasswordField();
        jLabelPasswordConfirmation = new javax.swing.JLabel();
        jPasswordFieldConfirmation = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/radixware/kernel/utils/keyStoreAdmin/resources/OpenKeyStoreDialog"); // NOI18N
        setTitle(bundle.getString("Dialog.titleOpen")); // NOI18N
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

        jLabelKeyStoreFile.setLabelFor(jTextFieldKeyStoreFile);
        jLabelKeyStoreFile.setText(bundle.getString("jLabelKeyStoreFile.text")); // NOI18N
        jLabelKeyStoreFile.setName("jLabelKeyStoreFile"); // NOI18N

        jTextFieldKeyStoreFile.setEditable(false);
        jTextFieldKeyStoreFile.setName("jTextFieldKeyStoreFile"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(org.radixware.kernel.utils.keyStoreAdmin.KeyStoreAdmin.class).getContext().getActionMap(OpenKeyStoreDialog.class, this);
        jButtonKeyStoreFile.setAction(actionMap.get("selectFile")); // NOI18N
        jButtonKeyStoreFile.setName("jButtonKeyStoreFile"); // NOI18N

        jLabelPassword.setLabelFor(jPasswordField);
        jLabelPassword.setText(bundle.getString("jLabelPassword.text")); // NOI18N
        jLabelPassword.setName("jLabelPassword"); // NOI18N

        jPasswordField.setName("jPasswordField"); // NOI18N

        jLabelPasswordConfirmation.setLabelFor(jPasswordFieldConfirmation);
        jLabelPasswordConfirmation.setText(bundle.getString("jLabelPasswordConfirmation.text")); // NOI18N
        jLabelPasswordConfirmation.setName("jLabelPasswordConfirmation"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.radixware.kernel.utils.keyStoreAdmin.KeyStoreAdmin.class).getContext().getResourceMap(OpenKeyStoreDialog.class);
        jPasswordFieldConfirmation.setText(resourceMap.getString("jPasswordFieldConfirmation.text")); // NOI18N
        jPasswordFieldConfirmation.setName("jPasswordFieldConfirmation"); // NOI18N

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
                            .addComponent(jLabelPasswordConfirmation)
                            .addComponent(jLabelPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                            .addComponent(jLabelKeyStoreFile, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextFieldKeyStoreFile, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonKeyStoreFile, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPasswordField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                            .addComponent(jPasswordFieldConfirmation, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE))))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelKeyStoreFile)
                    .addComponent(jTextFieldKeyStoreFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonKeyStoreFile, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelPassword)
                    .addComponent(jPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelPasswordConfirmation)
                    .addComponent(jPasswordFieldConfirmation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton))
                .addContainerGap())
        );

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
            password = jPasswordField.getPassword();
            keystorePath = jTextFieldKeyStoreFile.getText();
        }

        setVisible(false);
        dispose();
    }

    /** Checks that input values are valid */
    private boolean checkInput(){
        String messageBoxTitle = bundle.getString("JOptionPaneInputError.title");
        String message;

        if (jTextFieldKeyStoreFile.getText().length()==0){
            message = bundle.getString("KeystoreFilePathIsEmpty");
            JOptionPane.showMessageDialog(this, message, messageBoxTitle, JOptionPane.ERROR_MESSAGE);
            jTextFieldKeyStoreFile.requestFocusInWindow();
            return false;
        }

        if (jPasswordField.getPassword().length==0){
            message = bundle.getString("KeystorePasswordIsEmpty");
            JOptionPane.showMessageDialog(this, message, messageBoxTitle, JOptionPane.ERROR_MESSAGE);
            jPasswordField.requestFocusInWindow();
            return false;
        }

        if (createKeystore && !Arrays.equals(jPasswordField.getPassword(), jPasswordFieldConfirmation.getPassword())){
            message = bundle.getString("WrongKeystorePasswordConfirmation");
            JOptionPane.showMessageDialog(this, message, messageBoxTitle, JOptionPane.ERROR_MESSAGE);
            jPasswordFieldConfirmation.requestFocusInWindow();
            return false;
        }

        return true;
    }

    @Action
    public void selectFile(){
        KeyStoreAdminFileFilter filter = new KeyStoreAdminFileFilter(new String[]{"jceks"}, "*.jceks");
        String title = bundle.getString("openKeystoreChooser.dialogTitle");
        File selectedFile = KeyStoreAdminFileSelector.selectFileForOpen(this, title, new KeyStoreAdminFileFilter[]{filter}, defaultKeystorePath, createKeystore ? SelectNewFileMode.REQUIRED : SelectNewFileMode.FORBIDDEN);
        if (selectedFile!=null)
            jTextFieldKeyStoreFile.setText(selectedFile.getAbsolutePath());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton jButtonKeyStoreFile;
    private javax.swing.JLabel jLabelKeyStoreFile;
    private javax.swing.JLabel jLabelPassword;
    private javax.swing.JLabel jLabelPasswordConfirmation;
    private javax.swing.JPasswordField jPasswordField;
    private javax.swing.JPasswordField jPasswordFieldConfirmation;
    private javax.swing.JTextField jTextFieldKeyStoreFile;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables

    private int returnStatus = RET_CANCEL;
}
