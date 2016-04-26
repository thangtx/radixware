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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.netbeans.api.db.explorer.DatabaseConnection;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.Presenter;
import org.radixware.kernel.common.dialogs.db.DbDialogUtils;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.sqlscript.SelectedConnectionsManager;
import org.radixware.kernel.designer.common.dialogs.utils.RadixNbEditorUtils;


public class SelectConnectionAction extends AbstractAction implements ContextAwareAction {

    public static final String SELECT_CONNECTION_ACTION = "select-connection-action";
    public static final String SELECTED_CONNECTIONS = "selected-connections";

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
        final SelectedConnectionsManager manager = actionContext.lookup(SelectedConnectionsManager.class);
        if (manager == null) {
           // throw new NullPointerException("No SelectedConnectionsManager in actionContext");
            return new UnavailableRunSqlScriptActionImpl();
        }
        return new SelectConnectionActionImpl(manager);
    }

    private static class SelectConnectionActionImpl extends AbstractAction implements Presenter.Toolbar {

        private final SelectedConnectionsManager connectionsManager;

        public SelectConnectionActionImpl(SelectedConnectionsManager connectionsManager) {
            this.connectionsManager = connectionsManager;
        }

        @Override
        public Component getToolbarPresenter() {
            return new ConnectionPanel();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //this action does nothing itself, it's created to provide special toolbar presenter
            throw new UnsupportedOperationException("Not supported yet.");
        }

        private class ConnectionPanel extends JPanel {

            private final JLabel lbSelectedConnection = new JLabel();

            public ConnectionPanel() {
                setLayout(new BorderLayout());
                setBorder(BorderFactory.createEtchedBorder());
                setBackground(Color.white);
                final JLabel lbConnection = new JLabel(NbBundle.getMessage(ConnectionPanel.class, "lb-connection"));
                add(lbConnection, BorderLayout.WEST);
                lbSelectedConnection.setMinimumSize(new Dimension(150, lbSelectedConnection.getMinimumSize().height));
                lbSelectedConnection.setPreferredSize(new Dimension(150, lbSelectedConnection.getPreferredSize().height));
                lbSelectedConnection.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
                setSelectedConnectionText("<none>");
                add(lbSelectedConnection, BorderLayout.CENTER);
                final JButton btSelectConnections = new JButton(RadixWareDesignerIcon.DIALOG.CHOOSE.getIcon());
                btSelectConnections.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        List<DatabaseConnection> selectedConnections = DbDialogUtils.selectDatabaseConnections();
                        if (selectedConnections != null) {
                            updateText(selectedConnections);
                            connectionsManager.setSelectedConnections(selectedConnections);
                        } else {
                            updateText(connectionsManager.getSelectedConnections());
                        }
                    }
                });
                RadixNbEditorUtils.processToolbarButton(btSelectConnections);
                add(btSelectConnections, BorderLayout.EAST);
                setPreferredSize(getMinimumSize());
            }

            private void setSelectedConnectionText(String text) {
                StringBuilder sb = new StringBuilder();
                sb.append("<html><B>");
                sb.append(text.replace("<", "&lt;").replace(">", "&gt;"));
                sb.append("</B></html>");
                lbSelectedConnection.setText(sb.toString());
            }

            private void updateText(List<DatabaseConnection> selectedConnections) {
                if (selectedConnections == null || selectedConnections.isEmpty()) {
                    setSelectedConnectionText("<none>");
                } else if (selectedConnections.size() > 1) {
                    setSelectedConnectionText("Multiple connections");
                } else {
                    if (selectedConnections.get(0).getJDBCConnection() != null) {
                        setSelectedConnectionText(selectedConnections.get(0).getDisplayName());
                    } else {
                        setSelectedConnectionText(selectedConnections.get(0).getDisplayName() + " <disconnected>");
                    }
                }
            }
        }
    }
}
