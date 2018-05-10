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

package org.radixware.kernel.server.jdbc;

import org.radixware.kernel.common.cache.ObjectCacheInterface;
import java.net.URI;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.radixware.kernel.common.enums.EDatabaseType;


public interface RadixDataSource extends DataSource, AutoCloseable {
    /**
     * <p>Get database type for this data source</p>
     * @return database type
     */
    EDatabaseType getDatabaseType();
    
    /**
     * <p>Test the plugin can serve the given connection string uri</p>
     * @param uri uri to test
     * @return true if the uri can be served by this plugin
     */
    boolean canServeURI(URI uri);
    
    /**
     * <p>Set connection string uri for the given plugin</p>
     * @param uri connection string uri to set
     */
    void setUri(URI uri);
    
    /**
     * <p>Set user for the given plugin</p>
     * @param user user to set
     */
    void setUser(String user);
    
    /**
     * <p>Set password for the given plugin</p>
     * @param password password to set
     */
    void setPassword(String password);
    
    /**
     * <p>Set operation logger</p>
     * @param logger logger to set
     */
    void setLogger(DbOperationLoggerFactoryInterface logger);
    
    /**
     * <p>Set database schema</p>
     * @param dbSchema 
     */
    void setDbSchema(String dbSchema);

    /**
     * <p>Close datasource and plugin</p>
     * @throws SQLException 
     */
    @Override
    void close() throws SQLException;
}
