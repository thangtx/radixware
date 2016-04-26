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
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.kernel.utils.keyStoreAdmin.KeyStoreAdminException;
import org.radixware.kernel.utils.keyStoreAdmin.utils.Crypto;



public class InputDesKeyComponentDialog extends AKeyStoreAdminDialog {

    /** A return status code - returned if NEXT COMPONENT button has been pressed */
    public static final int RET_NEXT_COMPONENT = 2;
    
    private static final int COMPONENT_LENGTH = 16;
    
    private byte[] component;
    
    private static final ResourceBundle bundle = ResourceBundle.getBundle("org/radixware/kernel/utils/keyStoreAdmin/resources/InputDesKeyComponentDialog");

    /** Creates new form InputDesKeyComponentDialog */
    public InputDesKeyComponentDialog(java.awt.Frame parent, int componentSeqNumber) {
        super(parent);
        initComponents();
        
        setTitle(String.format(bundle.getString("Dialog.title"), componentSeqNumber));
        
        AbstractDocument doc = new PlainDocument();
        doc.setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(DocumentFilter.FilterBypass bypass, int offset, String string, AttributeSet attr) throws javax.swing.text.BadLocationException {
                bypass.insertString(offset, string.substring(0, Math.min(string.length(), COMPONENT_LENGTH*2-bypass.getDocument().getLength())), attr);
                updateComponentInfo(bypass.getDocument().getLength());
            }

            @Override
            public void remove(DocumentFilter.FilterBypass bypass, int offset, int length) throws BadLocationException {
                bypass.remove(offset, length);
                updateComponentInfo(bypass.getDocument().getLength());
            }

            @Override
            public void replace(DocumentFilter.FilterBypass bypass, int offset, int length, String string, AttributeSet attr) throws BadLocationException {
                for (byte b: string.getBytes()){
                    if (!Hex.isHexDigit(b))
                        return;
                }
                bypass.replace(offset, length, string.substring(0, Math.min(string.length(), COMPONENT_LENGTH*2-(bypass.getDocument().getLength()-length))), attr);
                updateComponentInfo(bypass.getDocument().getLength());
            }
        });
        jPasswordFieldComponent.setDocument(doc);
    }
    
    private void updateComponentInfo(int componentLength){
        jTextFieldComponentLength.setText(Integer.toString(componentLength));
        if (componentLength==COMPONENT_LENGTH*2){
            try{
                final byte[] checkValue = Crypto.calc3desKeyCheckValue(Hex.decode(String.valueOf(jPasswordFieldComponent.getPassword())));
                jTextFieldComponentCheckValue.setText(Hex.encode(checkValue).substring(0, 6));
            } catch (KeyStoreAdminException e){
                System.out.println(e.getMessage());
            }
        } else
            jTextFieldComponentCheckValue.setText("");
    }

    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
    public int getReturnStatus() {
        return returnStatus;
    }
    
    public byte[] getComponent(){
        return component;
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
        jLabelComponent = new javax.swing.JLabel();
        jPasswordFieldComponent = new javax.swing.JPasswordField();
        jLabelComponentLength = new javax.swing.JLabel();
        jLabelComponentCheckValue = new javax.swing.JLabel();
        jTextFieldComponentLength = new javax.swing.JTextField();
        jTextFieldComponentCheckValue = new javax.swing.JTextField();
        jButtonNextComponent = new javax.swing.JButton();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/radixware/kernel/utils/keyStoreAdmin/resources/InputDesKeyComponentDialog"); // NOI18N
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

        jLabelComponent.setLabelFor(jPasswordFieldComponent);
        jLabelComponent.setText(bundle.getString("jLabelComponent.txt")); // NOI18N
        jLabelComponent.setName("jLabelComponent"); // NOI18N

        jPasswordFieldComponent.setName("jPasswordFieldComponent"); // NOI18N

        jLabelComponentLength.setLabelFor(jTextFieldComponentLength);
        jLabelComponentLength.setText(bundle.getString("jLabelComponentLength.text")); // NOI18N
        jLabelComponentLength.setName("jLabelComponentLength"); // NOI18N

        jLabelComponentCheckValue.setLabelFor(jTextFieldComponentCheckValue);
        jLabelComponentCheckValue.setText(bundle.getString("jLabelComponentCheckValue.text")); // NOI18N
        jLabelComponentCheckValue.setName("jLabelComponentCheckValue"); // NOI18N

        jTextFieldComponentLength.setEditable(false);
        jTextFieldComponentLength.setName("jTextFieldComponentLength"); // NOI18N

        jTextFieldComponentCheckValue.setEnabled(false);
        jTextFieldComponentCheckValue.setName("jTextFieldComponentCheckValue"); // NOI18N

        jButtonNextComponent.setText(bundle.getString("jButtonNextComponent.text")); // NOI18N
        jButtonNextComponent.setName("jButtonNextComponent"); // NOI18N
        jButtonNextComponent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNextComponentActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButtonNextComponent)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(okButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelComponent)
                            .addComponent(jLabelComponentLength)
                            .addComponent(jLabelComponentCheckValue))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPasswordFieldComponent, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jTextFieldComponentCheckValue, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTextFieldComponentLength, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelComponent)
                    .addComponent(jPasswordFieldComponent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelComponentLength)
                    .addComponent(jTextFieldComponentLength, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelComponentCheckValue)
                    .addComponent(jTextFieldComponentCheckValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton)
                    .addComponent(jButtonNextComponent))
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

    private void jButtonNextComponentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNextComponentActionPerformed
        doClose(RET_NEXT_COMPONENT);
    }//GEN-LAST:event_jButtonNextComponentActionPerformed
    
    protected void doClose(int retStatus) {
        returnStatus = retStatus;
        if (returnStatus==RET_OK || returnStatus==RET_NEXT_COMPONENT){
            if (!checkInput())
                return;

            component = Hex.decode(String.valueOf(jPasswordFieldComponent.getPassword()));
        }
        setVisible(false);
        dispose();
    }
    
    /** Checks that input values are valid */
    private boolean checkInput(){
        String dialogTitle = bundle.getString("JOptionPaneInputError.title");

        if (jPasswordFieldComponent.getPassword().length!=COMPONENT_LENGTH*2){
            String dialogMessage = String.format(bundle.getString("ComponentIsIncomplete"), jPasswordFieldComponent.getPassword().length, COMPONENT_LENGTH*2);
            JOptionPane.showMessageDialog(this, dialogMessage, dialogTitle, JOptionPane.ERROR_MESSAGE);
            jPasswordFieldComponent.requestFocusInWindow();
            return false;
        }

        return true;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton jButtonNextComponent;
    private javax.swing.JLabel jLabelComponent;
    private javax.swing.JLabel jLabelComponentCheckValue;
    private javax.swing.JLabel jLabelComponentLength;
    private javax.swing.JPasswordField jPasswordFieldComponent;
    private javax.swing.JTextField jTextFieldComponentCheckValue;
    private javax.swing.JTextField jTextFieldComponentLength;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
}
