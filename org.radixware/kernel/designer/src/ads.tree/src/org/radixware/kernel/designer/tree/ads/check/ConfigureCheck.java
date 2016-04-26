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
package org.radixware.kernel.designer.tree.ads.check;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.db.explorer.ConnectionManager;
import org.netbeans.api.db.explorer.DatabaseConnection;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.builder.check.common.CheckOptions;
import org.radixware.kernel.common.dialogs.db.DbDialogUtils;
import org.radixware.kernel.designer.common.dialogs.build.ConfigureBuild;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

public class ConfigureCheck extends javax.swing.JPanel implements ConfigureBuild.IConfigureCheck {

    /**
     * Creates new form ConfigureCheck
     */
    private DatabaseConnection dbConnection;

    public ConfigureCheck() {
        initComponents();
        btChooseConnection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        DatabaseConnection c = DbDialogUtils.selectDatabaseConnection(true);
                        if (c != null) {
                            setDbConnection(c);
                        }
                    }
                });
            }
        });
        chCheckSql.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateEnabled();
                saveCheckSQL(chCheckSql.isSelected());
            }
        });
        DatabaseConnection c = restoreDbConnection();
        if (c != null) {
            setDbConnection(c);
        }
        SpinnerNumberModel model = new SpinnerNumberModel(restoreMaxQueryCount(), 1, 10, 1);
        edQueryCount.setModel(model);
        model.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                saveMaxQueryCount((int) edQueryCount.getModel().getValue());
            }
        });
        chCheckSql.setSelected(restoreCheckSQL());
        chCheckModules.setSelected(restoreCheckModules());
        chCheckModules.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCheckModules(chCheckModules.isSelected());
            }
        });

        chFullScan.setSelected(restoreCheckAllPathes());
        chFullScan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCheckAllPathes(chFullScan.isSelected());
            }
        });
        chCheckDoc.setSelected(restoreCheckDoc());
        chCheckDoc.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                saveCheckDoc(chCheckDoc.isSelected());
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        updateEnabled();

                    }
                });

            }
        });
        chCheckUdsDoc.setSelected(restoreCheckUDSDoc());
        chCheckUdsDoc.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                saveCheckUDSDoc(chCheckUdsDoc.isSelected());
            }
        });
        updateEnabled();
    }

    private void updateEnabled() {
        boolean enabled = chCheckSql.isSelected();
        edConnection.setEnabled(enabled);
        edQueryCount.setEnabled(enabled);
        btChooseConnection.setEnabled(enabled);
        chCheckUdsDoc.setEnabled(chCheckDoc.isSelected());
    }

    private void setDbConnection(DatabaseConnection c) {
        if (c != null) {
            saveDbConnection(c);
            dbConnection = c;
            edConnection.setText(c.getName());
        } else {
            saveDbConnection(null);
            dbConnection = null;
            edConnection.setText("<Not specified>");
        }
    }

    private void saveDbConnection(DatabaseConnection c) {

        if (c == null) {
            Preferences.userRoot().put("RadixWareDesignerCheckDbConnectionName", null);
        } else {
            Preferences.userRoot().put("RadixWareDesignerCheckDbConnectionName", c.getName());
        }
        try {
            Preferences.userRoot().sync();
        } catch (BackingStoreException ex) {
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
        }

    }

    private DatabaseConnection restoreDbConnection() {
        String name = Preferences.userRoot().get("RadixWareDesignerCheckDbConnectionName", null);
        if (name == null || name.isEmpty()) {
            return null;
        }
        return ConnectionManager.getDefault().getConnection(name);
    }

    public static CheckOptions configureCheck() {
        final ConfigureCheck panel = new ConfigureCheck();
        ModalDisplayer displayer = new ModalDisplayer(panel, "Check Options") {
            @Override
            protected boolean canClose() {
                return panel.isValidInteractive();
            }
        };
        if (displayer.showModal()) {
            CheckOptions options = new CheckOptions(panel.chCheckSql.isSelected(), panel.chFullScan.isSelected(), panel.chCheckSql.isSelected() ? panel.getJdbcConnection() : null);
            options.setMaxSqlQueryVariants((int) panel.edQueryCount.getModel().getValue());
            options.setCheckModuleDependences(panel.chCheckModules.isSelected());
            options.setCheckDocumentation(panel.chCheckDoc.isSelected());
            options.setCheckUDSRelatedDocumentationOnly(panel.chCheckUdsDoc.isSelected());
            return options;

        } else {
            return null;
        }
    }

    @Override
    public boolean isValidInteractive() {
        if (chCheckSql.isSelected()) {
            if (getJdbcConnection() == null) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.messageError("Database connection must be specified");
                    }
                });
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private int restoreMaxQueryCount() {
        return Preferences.userRoot().getInt("RadixWareDesignerCheckOptionsMaxSqlQueryCount", 10);
    }

    private void saveMaxQueryCount(int count) {
        Preferences.userRoot().putInt("RadixWareDesignerCheckOptionsMaxSqlQueryCount", count);
        try {
            Preferences.userRoot().sync();
        } catch (BackingStoreException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private boolean restoreCheckAllPathes() {
        return Preferences.userRoot().getBoolean("RadixWareDesignerCheckOptionsCheckAllOvrPathes", true);
    }

    private void saveCheckAllPathes(boolean check) {
        Preferences.userRoot().putBoolean("RadixWareDesignerCheckOptionsCheckAllOvrPathes", check);
        try {
            Preferences.userRoot().sync();
        } catch (BackingStoreException ex) {
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
        }
    }

    private boolean restoreCheckSQL() {
        return Preferences.userRoot().getBoolean("RadixWareDesignerCheckOptionsCheckSQL", true);
    }

    private boolean restoreCheckDoc() {
        return Preferences.userRoot().getBoolean("RadixWareDesignerCheckOptionsCheckDoc", false);
    }

    private boolean restoreCheckUDSDoc() {
        return Preferences.userRoot().getBoolean("RadixWareDesignerCheckOptionsCheckDocUDS", false);
    }

    private void saveCheckSQL(boolean check) {
        Preferences.userRoot().putBoolean("RadixWareDesignerCheckOptionsCheckSQL", check);
        try {
            Preferences.userRoot().sync();
        } catch (BackingStoreException ex) {
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
        }
    }

    private void saveCheckDoc(boolean check) {
        Preferences.userRoot().putBoolean("RadixWareDesignerCheckOptionsCheckDoc", check);
        try {
            Preferences.userRoot().sync();
        } catch (BackingStoreException ex) {
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
        }
    }

    private void saveCheckUDSDoc(boolean check) {
        Preferences.userRoot().putBoolean("RadixWareDesignerCheckOptionsCheckDocUDS", check);
        try {
            Preferences.userRoot().sync();
        } catch (BackingStoreException ex) {
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
        }
    }

    private boolean restoreCheckModules() {
        return Preferences.userRoot().getBoolean("RadixWareDesignerCheckOptionsCheckModules", false);
    }

    private void saveCheckModules(boolean check) {
        Preferences.userRoot().putBoolean("RadixWareDesignerCheckOptionsCheckModules", check);
        try {
            Preferences.userRoot().sync();
        } catch (BackingStoreException ex) {
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
        }
    }

    public Connection getJdbcConnection() {
        if (dbConnection == null) {
            return null;
        } else {
            if (dbConnection.getJDBCConnection() != null) {
                return dbConnection.getJDBCConnection();
            } else {
                ConnectionManager.getDefault().showConnectionDialog(dbConnection);
                return dbConnection.getJDBCConnection();

            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        chFullScan = new javax.swing.JCheckBox();
        chCheckSql = new javax.swing.JCheckBox();
        edQueryCount = new javax.swing.JSpinner();
        lbMaxQueryCount = new javax.swing.JLabel();
        edConnection = new javax.swing.JTextField();
        lbDbConnection = new javax.swing.JLabel();
        btChooseConnection = new javax.swing.JButton();
        chCheckModules = new javax.swing.JCheckBox();
        chCheckDoc = new javax.swing.JCheckBox();
        chCheckUdsDoc = new javax.swing.JCheckBox();

        org.openide.awt.Mnemonics.setLocalizedText(chFullScan, org.openide.util.NbBundle.getMessage(ConfigureCheck.class, "ConfigureCheck.chFullScan.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(chCheckSql, org.openide.util.NbBundle.getMessage(ConfigureCheck.class, "ConfigureCheck.chCheckSql.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(lbMaxQueryCount, org.openide.util.NbBundle.getMessage(ConfigureCheck.class, "ConfigureCheck.lbMaxQueryCount.text")); // NOI18N

        edConnection.setText(org.openide.util.NbBundle.getMessage(ConfigureCheck.class, "ConfigureCheck.edConnection.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(lbDbConnection, org.openide.util.NbBundle.getMessage(ConfigureCheck.class, "ConfigureCheck.lbDbConnection.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(btChooseConnection, org.openide.util.NbBundle.getMessage(ConfigureCheck.class, "ConfigureCheck.btChooseConnection.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(chCheckModules, org.openide.util.NbBundle.getMessage(ConfigureCheck.class, "ConfigureCheck.chCheckModules.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(chCheckDoc, org.openide.util.NbBundle.getMessage(ConfigureCheck.class, "ConfigureCheck.chCheckDoc.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(chCheckUdsDoc, org.openide.util.NbBundle.getMessage(ConfigureCheck.class, "ConfigureCheck.chCheckUdsDoc.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbMaxQueryCount, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edQueryCount, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(edConnection)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btChooseConnection))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbDbConnection)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chFullScan)
                            .addComponent(chCheckSql)
                            .addComponent(chCheckModules)
                            .addComponent(chCheckDoc))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(chCheckUdsDoc)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chCheckSql)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbDbConnection)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edConnection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btChooseConnection))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbMaxQueryCount)
                    .addComponent(edQueryCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chFullScan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chCheckModules)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chCheckDoc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chCheckUdsDoc)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btChooseConnection;
    private javax.swing.JCheckBox chCheckDoc;
    private javax.swing.JCheckBox chCheckModules;
    private javax.swing.JCheckBox chCheckSql;
    private javax.swing.JCheckBox chCheckUdsDoc;
    private javax.swing.JCheckBox chFullScan;
    private javax.swing.JTextField edConnection;
    private javax.swing.JSpinner edQueryCount;
    private javax.swing.JLabel lbDbConnection;
    private javax.swing.JLabel lbMaxQueryCount;
    // End of variables declaration//GEN-END:variables

    @Override
    public CheckOptions createCheckOptions() {
        if (isValidInteractive()) {
            CheckOptions options = new CheckOptions(chCheckSql.isSelected(), chFullScan.isSelected(), chCheckSql.isSelected() ? getJdbcConnection() : null);
            options.setMaxSqlQueryVariants((int) edQueryCount.getModel().getValue());
            options.setCheckModuleDependences(chCheckModules.isSelected());
            options.setCheckDocumentation(chCheckDoc.isSelected());
            options.setCheckUDSRelatedDocumentationOnly(chCheckUdsDoc.isSelected());
            return options;
        } else {
            return null;
        }
    }
}
