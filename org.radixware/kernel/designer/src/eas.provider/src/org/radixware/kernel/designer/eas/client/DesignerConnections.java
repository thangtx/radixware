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

package org.radixware.kernel.designer.eas.client;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.connections.ConnectionOptions;
import org.radixware.kernel.common.client.eas.connections.Connections;
import org.radixware.kernel.common.client.types.ExplorerRoot;
import org.radixware.schemas.connections.ConnectionsDocument;


public class DesignerConnections extends Connections {

    public DesignerConnections(IClientEnvironment ice, String string) {
        super(ice, string);
    }

    public DesignerConnections(IClientEnvironment ice, File file) {
        super(ice, file);
    }

    public DesignerConnections(IClientEnvironment ice, ConnectionOptions predefined) {
        super(ice, Collections.singletonList(predefined));
    }

    private static class ConnectionOptionsImpl extends ConnectionOptions {

        public ConnectionOptionsImpl(IClientEnvironment environment, org.radixware.schemas.connections.ConnectionsDocument.Connections.Connection connection, boolean isLocal) {
            super(environment, connection, isLocal);
        }

        public ConnectionOptionsImpl(IClientEnvironment environment, ConnectionOptions source, String connectionName) {
            super(environment, source, connectionName);
        }

        public ConnectionOptionsImpl(IClientEnvironment environment, String connectionName) {
            super(environment, connectionName);
        }
        
        private ConnectionOptionsImpl(IClientEnvironment environment, ConnectionOptions source, String connectionName, boolean isReadOnly) {
            super(environment, source, connectionName, isReadOnly);
        }        

        @Override
        protected int showSelectRootDialog(final IClientEnvironment env, final List<ExplorerRoot> explorerRoots, final int currentSelection) {
            return currentSelection;
        }

        @Override
        public boolean edit(List<String> existingConnections) {
            return false;
        }
        
        @Override
        public ConnectionOptions createUnmodifableCopy() {
            final ConnectionOptionsImpl connection = new ConnectionOptionsImpl(getEnvironment(), this, null, true);
            connection.id = this.id;
            return connection;
        }        
    }

    @Override
    protected ConnectionOptions createConnection(IClientEnvironment env, ConnectionsDocument.Connections.Connection xmlConnection, boolean isLocal) {
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
}
