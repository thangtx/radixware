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
package org.radixware.kernel.server.units.persocomm.interfaces;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * <p>This interface describes interface to get connection and some advanced parameters about it</p>
 */
public interface IDatabaseConnectionAccess {
    public enum DatabaseType {
        ORACLE
    }
    /**
     * <p>Get database type</p>
     * @return database type. Never be null
     */
    DatabaseType getDatabaseType();
    
    /**
     * <p>Need close any connections was gotten earlier</p>
     * @return true if need
     */
    boolean needCloseConnection();
    
    /**
     * <p>Get connection to the database</p>
     * @return database connection. Never be null
     * @throws SQLException 
     */
    Connection getConnection() throws SQLException;
}
