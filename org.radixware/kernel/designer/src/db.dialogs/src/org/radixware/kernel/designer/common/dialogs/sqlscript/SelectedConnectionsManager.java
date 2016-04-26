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

package org.radixware.kernel.designer.common.dialogs.sqlscript;

import java.util.EventListener;
import java.util.List;
import javax.swing.event.ChangeListener;
import javax.swing.text.JTextComponent;
import org.netbeans.api.db.explorer.ConnectionListener;
import org.netbeans.api.db.explorer.ConnectionManager;
import org.netbeans.api.db.explorer.DatabaseConnection;
import org.netbeans.editor.WeakEventListenerList;
import org.openide.nodes.Node;


public class SelectedConnectionsManager implements Node.Cookie {

    private static final String SELECTED_CONNECTIONS_COOKIE = "selected-connection-cookie";
    private WeakEventListenerList listeners = new WeakEventListenerList();
    private List<DatabaseConnection> selectedConnections;

    public SelectedConnectionsManager() {
        ConnectionManager.getDefault().addConnectionListener(new ConnectionListener() {

            @Override
            public void connectionsChanged() {
                if(selectedConnections == null) {
                    return;
                }
                final DatabaseConnection[] existingConnections = ConnectionManager.getDefault().getConnections();
                boolean changed = false;
                for (int i = 0; i < selectedConnections.size(); i++) {
                    final DatabaseConnection selectedConnection = selectedConnections.get(i);
                    boolean exists = false;
                    for (int j = 0; j < existingConnections.length; j++) {
                        if (existingConnections[j] == selectedConnection) {
                            exists = true;
                        }
                    }
                    if (!exists) {
                        selectedConnections.remove(i);
                        changed = true;
                        i--;
                    }
                }
                if (changed) {
                    notifyListeners();
                }
            }
        });
    }

    private void notifyListeners() {
        for (EventListener listener : listeners.getListeners(ChangeListener.class)) {
            ((ChangeListener) listener).stateChanged(null);
        }
    }

    public List<DatabaseConnection> getSelectedConnections() {
        return selectedConnections;
    }

    public void setSelectedConnections(List<DatabaseConnection> selectedConnections) {
        this.selectedConnections = selectedConnections;
        notifyListeners();
    }

    public void addWeakListener(ChangeListener listener) {
        listeners.add(ChangeListener.class, listener);
    }

    public void removeListener(ChangeListener listener) {
        listeners.remove(ChangeListener.class, listener);
    }

//    public static SelectedConnectionsManager getInstance(JTextComponent tc) {
//        final Object sccObj = tc.getClientProperty(SELECTED_CONNECTIONS_COOKIE);
//        if (sccObj != null) {
//            return (SelectedConnectionsManager) sccObj;
//        }
//        final SelectedConnectionsManager scc = new SelectedConnectionsManager();
//        tc.putClientProperty(SELECTED_CONNECTIONS_COOKIE, sccObj);
//        return scc;
//    }
}
