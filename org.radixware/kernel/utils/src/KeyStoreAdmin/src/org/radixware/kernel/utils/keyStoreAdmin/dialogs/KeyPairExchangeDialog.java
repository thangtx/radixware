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
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

import org.jdesktop.application.Action;

import org.radixware.kernel.utils.keyStoreAdmin.utils.KeyStoreAdminFileFilter;
import org.radixware.kernel.utils.keyStoreAdmin.utils.KeyStoreAdminFileSelector;
import org.radixware.kernel.utils.keyStoreAdmin.utils.KeyStoreAdminFileSelector.SelectNewFileMode;



public class KeyPairExchangeDialog extends AKeyStoreAdminDialog {

    /** Dialog type for exporting keypair */
    public static final int TYPE_EXPORT = 0;
    /** Dialog type for importing keypair */
    public static final int TYPE_IMPORT = 1;
    /** Dialog type for generating and exporting keypair */
    public static final int TYPE_GENERATE_AND_EXPORT = 2;

    private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/radixware/kernel/utils/keyStoreAdmin/resources/KeyPairExchangeDialog");

    private int type;
    private char[] outerPassword;
    private String outerPath;
    private String alias;

    private String defaultOuterPath;

    /** Creates new form KeyPairExchangeDialog */
    public KeyPairExchangeDialog(java.awt.Frame parent, int type,
            String[] aliases, String defaultAlias, char[] defaultPassword, String defaultOuterPath) {
        super(parent);
        initComponents();

        DefaultComboBoxModel model = new DefaultComboBoxModel(aliases);
        jComboBoxAlias.setModel(model);
        jComboBoxAlias.setSelectedItem(defaultAlias);

        this.type = type;
        switch (type){
            case TYPE_EXPORT:
                setTitle(bundle.getString("Dialog.Export.title"));
                jLabelNewAlias.setVisible(false);
                jTextFieldNewAlias.setVisible(false);
                break;
            case TYPE_IMPORT:
                setTitle(bundle.getString("Dialog.Import.title"));
                jLabelAlias.setVisible(false);
                jComboBoxAlias.setVisible(false);
                jPasswordFieldOuterConfirmation.setVisible(false);
                jLabelOuterPasswordConfirmation.setVisible(false);
                break;
            case TYPE_GENERATE_AND_EXPORT:
                setTitle(bundle.getString("Dialog.Export.title"));
                jLabelAlias.setVisible(false);
                jComboBoxAlias.setVisible(false);
                jTextFieldNewAlias.setEnabled(false);
                jTextFieldNewAlias.setText(defaultAlias);
                break;
        }

        this.defaultOuterPath = defaultOuterPath;
    }

    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
    public int getReturnStatus() {
        return returnStatus;
    }

    public char[] getOuterKeyStorePassword(){
        return outerPassword;
    }

    public String getOuterKeyStorePath(){
        return outerPath;
    }

    public String getAlias(){
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
        jLabelOuterPassword = new javax.swing.JLabel();
        jPasswordFieldOuter = new javax.swing.JPasswordField();
        jLabelOuterFile = new javax.swing.JLabel();
        jTextFieldOuterFile = new javax.swing.JTextField();
        jButtonOuterFile = new javax.swing.JButton();
        jLabelAlias = new javax.swing.JLabel();
        jComboBoxAlias = new javax.swing.JComboBox();
        jLabelNewAlias = new javax.swing.JLabel();
        jTextFieldNewAlias = new javax.swing.JTextField();
        jLabelOuterPasswordConfirmation = new javax.swing.JLabel();
        jPasswordFieldOuterConfirmation = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setModal(true);
        setName("Form"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/radixware/kernel/utils/keyStoreAdmin/resources/KeyPairExchangeDialog"); // NOI18N
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

        jLabelOuterPassword.setLabelFor(jPasswordFieldOuter);
        jLabelOuterPassword.setText(bundle.getString("jLabelOuterPassword.text")); // NOI18N
        jLabelOuterPassword.setName("jLabelOuterPassword"); // NOI18N

        jPasswordFieldOuter.setName("jPasswordFieldOuter"); // NOI18N

        jLabelOuterFile.setLabelFor(jTextFieldOuterFile);
        jLabelOuterFile.setText(bundle.getString("jLabelOuterFile.text")); // NOI18N
        jLabelOuterFile.setName("jLabelOuterFile"); // NOI18N

        jTextFieldOuterFile.setEditable(false);
        jTextFieldOuterFile.setName("jTextFieldOuterFile"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(org.radixware.kernel.utils.keyStoreAdmin.KeyStoreAdmin.class).getContext().getActionMap(KeyPairExchangeDialog.class, this);
        jButtonOuterFile.setAction(actionMap.get("selectFile")); // NOI18N
        jButtonOuterFile.setName("jButtonOuterFile"); // NOI18N

        jLabelAlias.setLabelFor(jComboBoxAlias);
        jLabelAlias.setText(bundle.getString("jLabelAlias.text")); // NOI18N
        jLabelAlias.setName("jLabelAlias"); // NOI18N

        jComboBoxAlias.setName("jComboBoxAlias"); // NOI18N

        jLabelNewAlias.setLabelFor(jTextFieldNewAlias);
        jLabelNewAlias.setText(bundle.getString("jLabelNewAlias.text")); // NOI18N
        jLabelNewAlias.setName("jLabelNewAlias"); // NOI18N

        jTextFieldNewAlias.setName("jTextFieldNewAlias"); // NOI18N

        jLabelOuterPasswordConfirmation.setLabelFor(jPasswordFieldOuterConfirmation);
        jLabelOuterPasswordConfirmation.setText(bundle.getString("jLabelOuterPasswordConfirmation.text")); // NOI18N
        jLabelOuterPasswordConfirmation.setName("jLabelOuterPasswordConfirmation"); // NOI18N

        jPasswordFieldOuterConfirmation.setName("jPasswordFieldOuterConfirmation"); // NOI18N

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
                        .addComponent(jLabelOuterFile)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextFieldOuterFile, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonOuterFile, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelAlias)
                            .addComponent(jLabelNewAlias))
                        .addGap(59, 59, 59)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextFieldNewAlias, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                            .addComponent(jComboBoxAlias, javax.swing.GroupLayout.Alignment.LEADING, 0, 254, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelOuterPassword)
                            .addComponent(jLabelOuterPasswordConfirmation))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPasswordFieldOuterConfirmation, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                            .addComponent(jPasswordFieldOuter, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE))))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelOuterFile)
                        .addComponent(jTextFieldOuterFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButtonOuterFile, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelOuterPassword)
                    .addComponent(jPasswordFieldOuter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelOuterPasswordConfirmation)
                    .addComponent(jPasswordFieldOuterConfirmation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelAlias)
                    .addComponent(jComboBoxAlias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldNewAlias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelNewAlias))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
            outerPassword = jPasswordFieldOuter.getPassword();
            outerPath = jTextFieldOuterFile.getText();
            if (type==TYPE_EXPORT || type==TYPE_GENERATE_AND_EXPORT)
                alias = (String)jComboBoxAlias.getSelectedItem();
            else
                alias = jTextFieldNewAlias.getText();
        }

        setVisible(false);
        dispose();
    }

        /** Checks that input values are valid */
    private boolean checkInput(){
        String messageBoxTitle = bundle.getString("JOptionPaneInputError.title");
        String message;
        
        if (jPasswordFieldOuter.getPassword().length==0){
            message = bundle.getString("KeystorePasswordIsEmpty");
            JOptionPane.showMessageDialog(this, message, messageBoxTitle, JOptionPane.ERROR_MESSAGE);
            jPasswordFieldOuter.requestFocusInWindow();
            return false;
        }

        if (jTextFieldOuterFile.getText().length()==0){
            message = bundle.getString("KeystoreFilePathIsEmpty");
            JOptionPane.showMessageDialog(this, message, messageBoxTitle, JOptionPane.ERROR_MESSAGE);
            jTextFieldOuterFile.requestFocusInWindow();
            return false;
        }

        File tmpFile = new File(jTextFieldOuterFile.getText());
        if (tmpFile.exists()){
            if (!tmpFile.isFile()){
                message = bundle.getString("NotAFileKeystore");
                JOptionPane.showMessageDialog(this, message, messageBoxTitle, JOptionPane.ERROR_MESSAGE);
                jTextFieldOuterFile.requestFocusInWindow();
                return false;
            }
        }

        switch (type){
            case TYPE_IMPORT:
                String keypairAlias = jTextFieldNewAlias.getText();
                if (keypairAlias.length()==0){
                    message = bundle.getString("AliasIsEmpty");
                    JOptionPane.showMessageDialog(this, message, messageBoxTitle, JOptionPane.ERROR_MESSAGE);
                    jTextFieldNewAlias.requestFocusInWindow();
                    return false;
                }

                for (int i = 0; i<jComboBoxAlias.getModel().getSize(); i++)
                    if (keypairAlias.equals(jComboBoxAlias.getModel().getElementAt(i))){
                        message = bundle.getString("AliasExists");
                        JOptionPane.showMessageDialog(this, message, messageBoxTitle, JOptionPane.ERROR_MESSAGE);
                        jTextFieldNewAlias.requestFocusInWindow();
                        return false;
                    }
                break;
            case TYPE_EXPORT:
            case TYPE_GENERATE_AND_EXPORT:
                if (!Arrays.equals(jPasswordFieldOuter.getPassword(), jPasswordFieldOuterConfirmation.getPassword())){
                    message = bundle.getString("WrongKeystorePasswordConfirmation");
                    JOptionPane.showMessageDialog(this, message, messageBoxTitle, JOptionPane.ERROR_MESSAGE);
                    jPasswordFieldOuterConfirmation.requestFocusInWindow();
                    return false;
                }
                break;
        }

        return true;
    }

    @Action
    public void selectFile(){
        KeyStoreAdminFileFilter filter = new KeyStoreAdminFileFilter(new String[]{"p12"}, "PKCS#12 keystore (*.p12)");
        String title = bundle.getString("openKeystoreChooser.dialogTitle");
        File selectedFile;
        if (type==TYPE_EXPORT || type==TYPE_GENERATE_AND_EXPORT){
            selectedFile = KeyStoreAdminFileSelector.selectFileForSave(this, title, new KeyStoreAdminFileFilter[]{filter}, defaultOuterPath);
        } else{
            selectedFile = KeyStoreAdminFileSelector.selectFileForOpen(this, title, new KeyStoreAdminFileFilter[]{filter}, defaultOuterPath, SelectNewFileMode.FORBIDDEN);
        }
        if (selectedFile!=null)
            jTextFieldOuterFile.setText(selectedFile.getAbsolutePath());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton jButtonOuterFile;
    private javax.swing.JComboBox jComboBoxAlias;
    private javax.swing.JLabel jLabelAlias;
    private javax.swing.JLabel jLabelNewAlias;
    private javax.swing.JLabel jLabelOuterFile;
    private javax.swing.JLabel jLabelOuterPassword;
    private javax.swing.JLabel jLabelOuterPasswordConfirmation;
    private javax.swing.JPasswordField jPasswordFieldOuter;
    private javax.swing.JPasswordField jPasswordFieldOuterConfirmation;
    private javax.swing.JTextField jTextFieldNewAlias;
    private javax.swing.JTextField jTextFieldOuterFile;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables

    private int returnStatus = RET_CANCEL;
}