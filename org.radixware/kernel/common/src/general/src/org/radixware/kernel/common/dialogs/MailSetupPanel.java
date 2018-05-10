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

package org.radixware.kernel.common.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;
import javax.mail.MessagingException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.radixware.kernel.common.enums.EMailPriority;
import org.radixware.kernel.common.exceptions.ExceptionHandler;
import org.radixware.kernel.common.mail.Letter;
import org.radixware.kernel.common.mail.Sender;
import org.radixware.kernel.common.mail.SettingsForSendMail;
import org.radixware.kernel.common.mail.enums.EMailAuthentication;
import org.radixware.kernel.common.mail.enums.EMailSecureConnection;


public class MailSetupPanel extends javax.swing.JPanel {

    private SettingsForSendMail settings;
    private ActionListener authenticationListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!isUpdate) {
                Object selected = jAuthentication.getSelectedItem();
                if (selected != null && selected instanceof EMailAuthentication) {
                    boolean enable = !((EMailAuthentication) selected).equals(EMailAuthentication.NONE);
                    enableAuthenticationFields(enable);
                }
            }
        }
    };

    public MailSetupPanel() {
        settings = SettingsForSendMail.getInstance();
        initComponents();
        jAuthentication.addActionListener(authenticationListener);
        enableAuthenticationFields(!settings.getAuthentiication().equals(EMailAuthentication.NONE));

        btTest.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        String address = JOptionPane.showInputDialog("Enter reciever email address", Preferences.userNodeForPackage(MailSetupPanel.class).get("TEST_ADDRESS", ""));
                        if (address != null) {
                            Preferences.userNodeForPackage(MailSetupPanel.class).put("TEST_ADDRESS", address);
                            Letter letter = new Letter();
                            letter.setSubject("RadixWare email settings test");
                            letter.setTextContent("Ok!");
                            letter.setAddressTo(address);
                            letter.setPriority(EMailPriority.NORMAL);
                            try {
                                SettingsForSendMail testSettings = SettingsForSendMail.getInstance();
                                store(testSettings);
                                Sender.send(testSettings, letter, timeout, 
                                        new ExceptionHandler() {
                                                @Override
                                                public void process(Throwable ex) {
                                                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                                                }
                                            }
                                        );
                            } catch (AuthenticationCancelledException ex) {
                                JOptionPane.showMessageDialog(null, "Authentication cancelled by user", "Error", JOptionPane.ERROR_MESSAGE);
                            } catch (MessagingException ex) {
                                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                });
            }
        });
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jRememberPasswordCheckBox = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jSelfEMail = new javax.swing.JTextField();
        jServer = new javax.swing.JTextField();
        jPort = new javax.swing.JTextField();
        jSecureConnection = new javax.swing.JComboBox();
        jAuthentication = new javax.swing.JComboBox();
        jUser = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        userLabel = new javax.swing.JLabel();
        btTest = new javax.swing.JButton();
        passwordLabel = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();

        jRememberPasswordCheckBox.setText("Remember password");

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/radixware/kernel/common/dialogs/Bundle"); // NOI18N
        jLabel1.setText(bundle.getString("MailSetup-SelfMail")); // NOI18N

        jLabel2.setText(bundle.getString("MailSetup-SMTPServer")); // NOI18N

        jLabel3.setText(bundle.getString("MailSetup-SMTPPort")); // NOI18N

        jLabel4.setText(bundle.getString("MailSetup-UseSecureConnection")); // NOI18N

        jLabel5.setText(bundle.getString("MailSetup-Authentication")); // NOI18N

        userLabel.setText(bundle.getString("MailSetup-User")); // NOI18N

        btTest.setText(bundle.getString("MailSetup-Test")); // NOI18N

        passwordLabel.setText(bundle.getString("MailSetup-Password")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(userLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btTest)
                    .addComponent(jSelfEMail, javax.swing.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE)
                    .addComponent(jServer, javax.swing.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE)
                    .addComponent(jPort, javax.swing.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE)
                    .addComponent(jSecureConnection, 0, 490, Short.MAX_VALUE)
                    .addComponent(jAuthentication, 0, 490, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jUser, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(passwordLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(passwordField, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSelfEMail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jServer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSecureConnection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jAuthentication, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userLabel)
                    .addComponent(passwordLabel)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btTest)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btTest;
    private javax.swing.JComboBox jAuthentication;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField jPort;
    private javax.swing.JCheckBox jRememberPasswordCheckBox;
    private javax.swing.JComboBox jSecureConnection;
    private javax.swing.JTextField jSelfEMail;
    private javax.swing.JTextField jServer;
    private javax.swing.JTextField jUser;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JLabel userLabel;
    // End of variables declaration//GEN-END:variables

    private void enableAuthenticationFields(boolean enable) {
        jUser.setEnabled(enable);
        userLabel.setEnabled(enable);
        passwordField.setEnabled(enable);
        passwordLabel.setEnabled(enable);
    }
    private boolean isUpdate = false;

    public void load() {
        isUpdate = true;

        jServer.setText(settings.getHost());
        jPort.setText(Integer.toString(settings.getPort()));
        while (jSecureConnection.getItemCount() > 0) {
            jSecureConnection.removeItemAt(0);
        }
        for (EMailSecureConnection con : EMailSecureConnection.values()) {
            jSecureConnection.addItem(con);
        }
        jSecureConnection.setSelectedItem(settings.getSecureconnection());
        while (jAuthentication.getItemCount() > 0) {
            jAuthentication.removeItemAt(0);
        }
        for (EMailAuthentication auth : EMailAuthentication.values()) {
            jAuthentication.addItem(auth);
        }
        jAuthentication.setSelectedItem(settings.getAuthentiication());
        jUser.setText(settings.getUser());

        passwordField.setText(settings.getPassword());

        jSelfEMail.setText(settings.getDefaultSenderAddress());

        enableAuthenticationFields(!settings.getAuthentiication().equals(EMailAuthentication.NONE));

        isUpdate = false;
    }

    public void store() {
        store(settings);
    }

    private void store(SettingsForSendMail settings) {
        settings.storeHost(jServer.getText());
        settings.storePort(Integer.parseInt(jPort.getText()));
        settings.storeSecureConnection((EMailSecureConnection) jSecureConnection.getSelectedItem());
        settings.storeAuthentication((EMailAuthentication) jAuthentication.getSelectedItem());
        settings.storeUser(jUser.getText());

        char[] password = passwordField.getPassword();
        settings.storeRememberPassword(password != null && password.length > 0);
        settings.storePassword(String.valueOf(password));

        settings.storeDefaultSenderAddress(jSelfEMail.getText());
    }

    public boolean valid() {
        return true;
    }
    
    private long timeout = 10;

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
    
    
    
}
