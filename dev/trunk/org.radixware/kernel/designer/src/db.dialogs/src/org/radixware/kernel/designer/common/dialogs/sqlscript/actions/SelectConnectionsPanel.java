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

package org.radixware.kernel.designer.common.dialogs.sqlscript.actions;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.netbeans.api.db.explorer.ConnectionListener;
import org.netbeans.api.db.explorer.ConnectionManager;
import org.netbeans.api.db.explorer.DatabaseConnection;
import org.netbeans.api.db.explorer.DatabaseException;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.sqmlnb.DatabaseParameters;
import org.radixware.kernel.designer.common.dialogs.utils.ModalObjectEditor;


@SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
public class SelectConnectionsPanel extends ModalObjectEditor<List<DatabaseConnection>> {

    private static final Icon ICON_CONNECTED = new ImageIcon(Thread.currentThread().getContextClassLoader().getResource("org/netbeans/modules/db/resources/connection.gif"));
    private static final Icon ICON_DISCONNECTED = new ImageIcon(Thread.currentThread().getContextClassLoader().getResource("org/netbeans/modules/db/resources/connectionDisconnected.gif"));
    private final DefaultListModel connectionsListModel;
    private final boolean mustBeConnected;
    //private final boolean allowMultiple;
    private final String title;

    /**
     * Creates new form SelectConnectionsPanel
     */
    public SelectConnectionsPanel(String title, boolean allowMultiple, boolean mustBeConnected) {
        initComponents();
        this.title = title;
        this.mustBeConnected = mustBeConnected;
        //this.allowMultiple = allowMultiple;
        btAdd.setIcon(RadixWareIcons.CREATE.ADD.getIcon());
        btRemove.setIcon(RadixWareIcons.DELETE.DELETE.getIcon());
        ConnectionManager.getDefault().addConnectionListener(new ConnectionListener() {
            @Override
            public void connectionsChanged() {
                updateList();
            }
        });
        connectionsListModel = new DefaultListModel();
        connectionsList.setModel(connectionsListModel);
        connectionsList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                DatabaseConnection dbConn = (DatabaseConnection) value;
                setText(dbConn.getDisplayName());
                setToolTipText(dbConn.toString());
                if (dbConn.getJDBCConnection() != null) {
                    setIcon(ICON_CONNECTED);
                } else {
                    setIcon(ICON_DISCONNECTED);
                }
                return this;
            }
        });
        connectionsList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateButtons();
            }
        });
        connectionsList.getSelectionModel().setSelectionMode(allowMultiple ? ListSelectionModel.MULTIPLE_INTERVAL_SELECTION : ListSelectionModel.SINGLE_SELECTION);
        updateList();
        updateButtons();
    }

    private void updateButtons() {
        if (connectionsList.getSelectedIndex() == -1) {
            btRemove.setEnabled(false);
        } else {
            btRemove.setEnabled(true);
        }
        if (connectionsList.getSelectedIndices().length == 1) {
            final DatabaseConnection dbConn = (DatabaseConnection) connectionsList.getSelectedValue();
            boolean connected = dbConn.getJDBCConnection() != null;
            btConnect.setEnabled(!connected);
            btDisconnect.setEnabled(connected);
            btProperties.setEnabled(true);
        } else {
            btConnect.setEnabled(false);
            btDisconnect.setEnabled(false);
            btProperties.setEnabled(false);
        }

        setCorrect(true);
        for (Object dbConnObject : connectionsList.getSelectedValues()) {
            DatabaseConnection dbConn = (DatabaseConnection) dbConnObject;
            if (mustBeConnected && dbConn.getJDBCConnection() == null) {
                setCorrect(false);
                break;
            }
        }
    }

    private void updateList() {
        Set<DatabaseConnection> selectedConnections = new HashSet<DatabaseConnection>();
        for (Object connection : connectionsList.getSelectedValues()) {
            selectedConnections.add((DatabaseConnection) connection);
        }
        connectionsListModel.removeAllElements();
        for (DatabaseConnection connecton : ConnectionManager.getDefault().getConnections()) {
            connectionsListModel.addElement(connecton);
            if (selectedConnections.contains(connecton)) {
                connectionsList.addSelectionInterval(connectionsListModel.getSize() - 1, connectionsListModel.getSize() - 1);
            }
        }
    }

    public List<DatabaseConnection> getSelectedConnections() {
        final List<DatabaseConnection> selectedConnections = new ArrayList<DatabaseConnection>();
        for (Object connection : connectionsList.getSelectedValues()) {
            selectedConnections.add((DatabaseConnection) connection);
        }
        return selectedConnections;
    }

    public void setSelectedConnections(List<DatabaseConnection> connections) {
        for (int i = 0; i < connectionsList.getModel().getSize(); i++) {
            if (connections.contains((DatabaseConnection) connectionsList.getModel().getElementAt(i))) {
                connectionsList.addSelectionInterval(i, i);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        connectionsList = new javax.swing.JList();
        btAdd = new javax.swing.JButton();
        btRemove = new javax.swing.JButton();
        btConnect = new javax.swing.JButton();
        btDisconnect = new javax.swing.JButton();
        btProperties = new javax.swing.JButton();

        connectionsList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(connectionsList);

        btAdd.setText(org.openide.util.NbBundle.getMessage(SelectConnectionsPanel.class, "SelectConnectionsPanel.btAdd.text")); // NOI18N
        btAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddActionPerformed(evt);
            }
        });

        btRemove.setText(org.openide.util.NbBundle.getMessage(SelectConnectionsPanel.class, "SelectConnectionsPanel.btRemove.text")); // NOI18N
        btRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRemoveActionPerformed(evt);
            }
        });

        btConnect.setText(org.openide.util.NbBundle.getMessage(SelectConnectionsPanel.class, "SelectConnectionsPanel.btConnect.text")); // NOI18N
        btConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btConnectActionPerformed(evt);
            }
        });

        btDisconnect.setText(org.openide.util.NbBundle.getMessage(SelectConnectionsPanel.class, "SelectConnectionsPanel.btDisconnect.text")); // NOI18N
        btDisconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDisconnectActionPerformed(evt);
            }
        });

        btProperties.setText(org.openide.util.NbBundle.getMessage(SelectConnectionsPanel.class, "SelectConnectionsPanel.btProperties.text")); // NOI18N
        btProperties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btPropertiesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btDisconnect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btRemove, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btConnect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btProperties, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btRemove)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btConnect)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btDisconnect)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btProperties)
                        .addGap(0, 47, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddActionPerformed
        final Set<DatabaseConnection> existingConnections = new HashSet<DatabaseConnection>();
        for (int i = 0; i < connectionsList.getModel().getSize(); i++) {
            existingConnections.add((DatabaseConnection) connectionsList.getModel().getElementAt(i));
        }
        ConnectionManager.getDefault().showAddConnectionDialog(null);
        for (int i = 0; i < connectionsList.getModel().getSize(); i++) {
            if (!existingConnections.contains((DatabaseConnection) connectionsList.getModel().getElementAt(i))) {
                connectionsList.getSelectionModel().addSelectionInterval(i, i);
            }
        }

    }//GEN-LAST:event_btAddActionPerformed

    private void btRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRemoveActionPerformed
        if (connectionsList.getSelectedValues().length == 0) {
            return;
        }
        if (connectionsList.getSelectedValues().length == 1) {
            final boolean remove = DialogUtils.messageConfirmation("Dou you want to remove connection \"" + connectionsList.getSelectedValue() + "\"?");
            if (remove) {
                try {
                    ConnectionManager.getDefault().removeConnection((DatabaseConnection) connectionsList.getSelectedValue());
                } catch (DatabaseException ex) {
                    DialogUtils.messageError(ex);
                }
            }
        } else {
            final boolean remove = DialogUtils.messageConfirmation("Dou you want to remove all selected connections?");
            if (remove) {
                try {
                    for (Object connection : connectionsList.getSelectedValues()) {
                        ConnectionManager.getDefault().removeConnection((DatabaseConnection) connection);
                    }
                } catch (DatabaseException ex) {
                    DialogUtils.messageError(ex);
                }
            }
        }
        updateList();
    }//GEN-LAST:event_btRemoveActionPerformed

    private void connectSelected() {
        if (connectionsList.getSelectedValue() != null) {
            final DatabaseConnection dbConn = (DatabaseConnection) connectionsList.getSelectedValue();
            dbConn.getPassword();//to prevent deadlock
            if (dbConn.getJDBCConnection() == null) {
                ConnectionManager.getDefault().showConnectionDialog(dbConn);
            }
        }
        updateList();
        updateButtons();
    }

    private void disconnectSelected() {
        if (connectionsList.getSelectedValue() != null) {
            ConnectionManager.getDefault().disconnect((DatabaseConnection) connectionsList.getSelectedValue());
        }
        updateList();
        updateButtons();
    }

    private void btDisconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDisconnectActionPerformed
        disconnectSelected();
    }//GEN-LAST:event_btDisconnectActionPerformed

    private void btConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btConnectActionPerformed
        connectSelected();
    }//GEN-LAST:event_btConnectActionPerformed

    private void btPropertiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btPropertiesActionPerformed
        if (connectionsList.getSelectedIndices().length == 1) {
            final DatabaseConnection connection = (DatabaseConnection) connectionsList.getSelectedValue();
            ConnectionManager.getDefault().showConnectionDialog(connection);
            if (connection.getJDBCConnection() != null) {
                final DatabaseParameters newParams = DatabaseParameters.edit(new DatabaseParameters(DatabaseParameters.load(connection.getName()), connection.getJDBCConnection()));
                if (newParams != null) {
                    DatabaseParameters.save(newParams, connection.getName());
                }
            }
            updateButtons();
        }
    }//GEN-LAST:event_btPropertiesActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btConnect;
    private javax.swing.JButton btDisconnect;
    private javax.swing.JButton btProperties;
    private javax.swing.JButton btRemove;
    private javax.swing.JList connectionsList;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    @Override
    protected String getTitle() {
        return title;
    }

    @Override
    protected void applyChanges() {
        return;
    }

    @Override
    protected void afterOpen() {
        connectionsList.clearSelection();
        if (getObject() != null) {
            for (DatabaseConnection connection : getObject()) {
                if (connectionsListModel.contains(connection)) {
                    final int idx = connectionsListModel.lastIndexOf(connection);
                    connectionsList.getSelectionModel().addSelectionInterval(idx, idx);
                }
            }
        }
    }

    @Override
    protected void setReadOnly(boolean readOnly) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
