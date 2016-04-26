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

package org.radixware.wps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.connections.ConnectionOptions;
import org.radixware.kernel.common.client.types.ExplorerRoot;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.schemas.connections.ConnectionsDocument.Connections.Connection;
import org.radixware.wps.dialogs.ChooseRootDialog;
import org.radixware.wps.dialogs.ConnectionEditor;
import org.radixware.wps.dialogs.ConnectionsManagerDialog;


public class Connections extends org.radixware.kernel.common.client.eas.connections.Connections {

    private static class ConnectionOptionsImpl extends ConnectionOptions {
        
        public ConnectionOptionsImpl(IClientEnvironment environment, Connection connection, boolean isLocal) {
            super(environment, connection, isLocal);
        }

        public ConnectionOptionsImpl(IClientEnvironment environment, ConnectionOptions source, String connectionName) {
            super(environment, source, connectionName);
        }
        
        private ConnectionOptionsImpl(IClientEnvironment environment, ConnectionOptions source, String connectionName, boolean isReadOnly) {
            super(environment, source, connectionName, isReadOnly);
        }        
        

        public ConnectionOptionsImpl(IClientEnvironment environment, String connectionName) {
            super(environment, connectionName);
        }

        @Override
        protected int showSelectRootDialog(final IClientEnvironment env, final List<ExplorerRoot> explorerRoots, final int currentSelection) {
            final ChooseRootDialog dialog = new ChooseRootDialog(((WpsEnvironment) env).getDialogDisplayer(), explorerRoots, currentSelection);
            if (dialog.execDialog() == DialogResult.ACCEPTED) {
                return dialog.getSelectedIndex();
            }
            return -1;
        }

        @Override
        public boolean edit(List<String> existingConnections) {
            ConnectionEditor editor = new ConnectionEditor((WpsEnvironment) getEnvironment(), this);
            return editor.execDialog() == DialogResult.ACCEPTED;
        }

        @Override
        public ConnectionOptions createUnmodifableCopy() {
            final ConnectionOptionsImpl connection = new ConnectionOptionsImpl(getEnvironment(), this, null, true);
            connection.id = this.id;
            return connection;
        }
    }

    public Connections(IClientEnvironment environment) {
        super(environment, FileFinder.findFile(WebServerRunParams.getConnectionsFile()));
    }

//    public void configure() {
//        ConnectionsManagerDialog dialog = new ConnectionsManagerDialog(env);
//        if (dialog.execDialog() == DialogResult.ACCEPTED) {
//            this.setConnections(dialog.getConnections());
//        }
//    }
    @Override
    protected ConnectionOptions createConnection(IClientEnvironment env, Connection xmlConnection, boolean isLocal) {
        return new ConnectionOptionsImpl(env, xmlConnection, isLocal);
    }

    @Override
    protected ConnectionOptions createConnection(IClientEnvironment env, String connectionName) {
        return new ConnectionOptionsImpl(env, connectionName);
    }

    @Override
    public ConnectionOptions createConnection(IClientEnvironment environment, ConnectionOptions source, String connectionName) {
        return new ConnectionOptionsImpl(environment, source, connectionName);
    }

    public List<ConnectionOptions> getConnections() {
        List<ConnectionOptions> list = new ArrayList<>(size());
        for (Iterator<ConnectionOptions> iter = iterator(); iter.hasNext();) {
            list.add(iter.next());
        }
        return list;
    }

    public List<String> getConnectionNames() {
        List<String> list = new ArrayList<>(size());
        for (Iterator<ConnectionOptions> iter = iterator(); iter.hasNext();) {
            list.add(iter.next().getName());
        }
        return list;
    }

    public void configure() {
        ConnectionsManagerDialog dialog = new ConnectionsManagerDialog((WpsEnvironment) getEnvironment());
        if (dialog.execDialog() == DialogResult.ACCEPTED) {
            this.store();
        }
    }

    public ConnectionOptions getConnectionByUserName(String userName) {
        if (user2connection == null) {
            return null;
        } else {
            String connectionid = user2connection.get(userName);

            ConnectionOptions def = null;
            for (Iterator<ConnectionOptions> iter = iterator(); iter.hasNext();) {
                ConnectionOptions opts = iter.next();
                if (connectionid != null && connectionid.equals(opts.getName())) {
                    return opts;
                }
                if (defaultConnectionName != null && defaultConnectionName.equals(opts.getName())) {
                    def = opts;
                }
            }

            return def == null ? null : new ConnectionOptionsImpl(getEnvironment(), def, def.getName());
        }
    }
}
