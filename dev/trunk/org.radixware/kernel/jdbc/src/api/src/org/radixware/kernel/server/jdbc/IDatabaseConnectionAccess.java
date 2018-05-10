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
package org.radixware.kernel.server.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import org.radixware.kernel.common.enums.EDatabaseType;

/**
 * <p>This interface describes interface to get connection and some advanced parameters about it. Usually is used to unlink getting of connection from it's implementation</p>
 */
public interface IDatabaseConnectionAccess {
    /**
     * <p>Get database type</p>
     * @return database type. Never be null
     */
    EDatabaseType getDatabaseType();
    
    /**
     * <p>Get connection to the database</p>
     * @return database connection.
     * @throws SQLException 
     */
    Connection getConnection() throws SQLException;
}
