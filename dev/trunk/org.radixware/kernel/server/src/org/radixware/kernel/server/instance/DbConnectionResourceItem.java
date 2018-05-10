/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.instance;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Callable;

/**
 *
 * @author dsafonov
 */
public class DbConnectionResourceItem extends SimpleResourceRegistryItem {

    private final Connection connection;

    public DbConnectionResourceItem(final String key, final Connection connection, final String description, final Callable<Boolean> holderAliveChecker) {
        super(key, new Closeable() {

            @Override
            public void close() throws IOException {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    throw new IOException(ex);
                }
            }
        }, description, holderAliveChecker);
        if (connection == null) {
            throw new IllegalArgumentException("connection can't be null");
        }
        this.connection = connection;
    }

    @Override
    public boolean isClosed() {
        try {
            return connection.isClosed();
        } catch (SQLException ex) {
            return false;
        }
    }

    @Override
    public Object getTarget() {
        return connection;
    }
    
}
