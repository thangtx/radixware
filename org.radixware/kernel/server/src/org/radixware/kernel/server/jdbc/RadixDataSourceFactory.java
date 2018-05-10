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

import java.net.URI;
import java.sql.Connection;
import java.util.ServiceLoader;
import org.radixware.kernel.server.SrvRunParams;

public class RadixDataSourceFactory {
    private static final String ORACLE_PREFIX = "jdbc:oracle:";
    private static final ServiceLoader loader = ServiceLoader.load(RadixDataSource.class,RadixDataSourceFactory.class.getClassLoader());
    
    public static RadixDataSource getDataSource(URI databaseUri) {
        if (databaseUri == null || !databaseUri.getScheme().toLowerCase().equals("jdbc")) {
            throw new IllegalArgumentException("Database driver connection string is null or isn't a valid 'jdbc' URI. Check presence of the database driver connection string in your configuration file(s) or command line");
        }
        else {
            if (databaseUri.toString().toLowerCase().startsWith(ORACLE_PREFIX)) {
                databaseUri = URI.create(ORACLE_PREFIX+databaseUri.toString().substring(ORACLE_PREFIX.length()));
            }
            for (Object item : loader) {
                if ((item instanceof RadixDataSource) && (((RadixDataSource)item).canServeURI(databaseUri))) {
                    ((RadixDataSource)item).setUri(databaseUri);
                    ((RadixDataSource)item).setLogger(new DbOperationLoggerFactoryInterface() {
                            @Override
                            public DBOperationLoggerInterface getLogger(Connection conn) {
                                return new DbOperationLogger(conn);
                            }
                    }
                    );
                    ((RadixDataSource)item).setDbSchema(SrvRunParams.getDbSchema().toUpperCase());
                    return (RadixDataSource) item;
                }
            }
            throw new IllegalArgumentException("No database plugin was detected to serve ["+databaseUri+"] URI. Note that URI is a case-sensitive entity!");
        }
    }
}
