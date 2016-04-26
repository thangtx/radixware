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

package org.radixware.kernel.explorer.env.session;

import java.io.File;


import org.radixware.kernel.common.client.eas.connections.ConnectionOptions;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.schemas.connections.ConnectionsDocument.Connections.Connection;

public final class Connections extends org.radixware.kernel.common.client.eas.connections.Connections {

    public Connections(IClientEnvironment environment, final File workPath) {
        super(environment, workPath);
    }

    @Override
    protected ConnectionOptions createConnection(IClientEnvironment env, Connection xmlConnection, boolean isLocal) {
        return new org.radixware.kernel.explorer.env.session.ConnectionOptions(env, xmlConnection, isLocal);
    }

    @Override
    protected ConnectionOptions createConnection(IClientEnvironment env, String connectionName) {
        return new org.radixware.kernel.explorer.env.session.ConnectionOptions(env, connectionName);
    }

    @Override
    protected ConnectionOptions createConnection(IClientEnvironment environment, ConnectionOptions source, String connectionName) {
        return new org.radixware.kernel.explorer.env.session.ConnectionOptions(environment, source, connectionName);
    }
}
