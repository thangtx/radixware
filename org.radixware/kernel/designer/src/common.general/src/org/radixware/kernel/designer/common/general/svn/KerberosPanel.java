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
package org.radixware.kernel.designer.common.general.svn;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.event.ListDataListener;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.svn.client.SvnAuthType;

final class KerberosPanel extends javax.swing.JPanel {

    KerberosPanel() {
        initComponents();
        edAuthType.setModel(authTypeModel);
        edAuthType.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                updateAuthType(((AuthTypeComboBoxModel.Item) edAuthType.getSelectedItem()).type);
            }
        });
        this.btChooseSSHKeyFile.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Choose Key File");
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    edKeyFilePath.setText(chooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        edAuthType = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        edKeyFilePath = new javax.swing.JTextField();
        btChooseSSHKeyFile = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        edUserName = new javax.swing.JTextField();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(KerberosPanel.class, "KerberosPanel.jLabel3.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(KerberosPanel.class, "KerberosPanel.jLabel4.text")); // NOI18N

        edKeyFilePath.setText(org.openide.util.NbBundle.getMessage(KerberosPanel.class, "KerberosPanel.edKeyFilePath.text")); // NOI18N
        edKeyFilePath.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        edKeyFilePath.setEnabled(false);

        org.openide.awt.Mnemonics.setLocalizedText(btChooseSSHKeyFile, org.openide.util.NbBundle.getMessage(KerberosPanel.class, "KerberosPanel.btChooseSSHKeyFile.text")); // NOI18N
        btChooseSSHKeyFile.setEnabled(false);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(KerberosPanel.class, "KerberosPanel.jLabel1.text_1")); // NOI18N

        edUserName.setText(org.openide.util.NbBundle.getMessage(KerberosPanel.class, "KerberosPanel.edUserName.text")); // NOI18N
        edUserName.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(edKeyFilePath, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btChooseSSHKeyFile))
                    .addComponent(edUserName, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                    .addComponent(edAuthType, 0, 335, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edAuthType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edKeyFilePath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btChooseSSHKeyFile)
                    .addComponent(jLabel4))
                .addContainerGap(104, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    void load() {
        authType = SVN.SVNPreferences.getAuthType();
//        jRealmTextField.setText(Preferences.userNodeForPackage(SVN.class).get(PreferencesNames.PREF_KERBEROS_REALM, ""));
//        jKDCTextField.setText(Preferences.userNodeForPackage(SVN.class).get(PreferencesNames.PREF_KERBEROS_KDC, ""));
//        jUseTicketCacheCheckBox.setSelected(Preferences.userNodeForPackage(SVN.class).get(PreferencesNames.PREF_KERBEROS_USE_TICKET_CACHE, Boolean.TRUE.toString()).equals(Boolean.TRUE.toString()));
        edKeyFilePath.setText(SVN.SVNPreferences.getSSHKeyFilePath());
        authTypeModel.setAuthType(authType);
        updateAuthType(authType);
        edUserName.setText(SVN.SVNPreferences.getUserName());
    }

    void store() {
        SVN.SVNPreferences.setSSHKeyFilePath(edKeyFilePath.getText());
        SVN.SVNPreferences.setAuthType(authType);
        SVN.SVNPreferences.setUserName(edUserName.getText());
    }

    boolean valid() {
        return true;
    }

    private class AuthTypeComboBoxModel implements ComboBoxModel {

        private class Item {

            private final SvnAuthType type;

            public Item(SvnAuthType type) {
                this.type = type;
            }

            @Override
            public String toString() {
                return type.getName();
            }
        }
        Item cur;
        private final Item[] items = new Item[SvnAuthType.values().length];

        void setAuthType(SvnAuthType type) {
            for (int i = 0; i < items.length; i++) {
                if (items[i].type == type) {
                    setSelectedItem(items[i]);
                    break;
                }
            }

        }

        AuthTypeComboBoxModel() {
            for (int i = 0; i < items.length; i++) {
                items[i] = new Item(SvnAuthType.values()[i]);
            }
        }

        @Override
        public void setSelectedItem(Object anItem) {
            cur = (Item) anItem;
        }

        @Override
        public Object getSelectedItem() {
            return cur;
        }

        @Override
        public int getSize() {
            return items.length;
        }

        @Override
        public Object getElementAt(int index) {
            return items[index];
        }

        @Override
        public void addListDataListener(ListDataListener l) {
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btChooseSSHKeyFile;
    private javax.swing.JComboBox edAuthType;
    private javax.swing.JTextField edKeyFilePath;
    private javax.swing.JTextField edUserName;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables
    private AuthTypeComboBoxModel authTypeModel = new AuthTypeComboBoxModel();
    private SvnAuthType authType = SvnAuthType.SSH_PASSWORD;

    private void updateAuthType(SvnAuthType authtype) {
        edKeyFilePath.setEnabled(false);
        btChooseSSHKeyFile.setEnabled(false);
        this.authType = authtype;
        if (authtype == SvnAuthType.SSH_KEY_FILE || authtype == SvnAuthType.SSL) {
            edKeyFilePath.setEnabled(true);
            btChooseSSHKeyFile.setEnabled(true);
        }
    }
}
