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

package org.radixware.kernel.server.jdbcdrivers.oracle;

import java.io.PrintWriter;
import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import oracle.jdbc.pool.OracleDataSource;
import org.radixware.kernel.server.jdbc.DbOperationLoggerFactoryInterface;
import org.radixware.kernel.server.jdbc.RadixDataSource;
import org.radixware.kernel.common.enums.EDatabaseType;


public class RadixDataSource4ORACLE implements RadixDataSource {
    private final OracleDataSource ods;
    private DbOperationLoggerFactoryInterface logger = null;
    private String dbSchema = "";
    
    public RadixDataSource4ORACLE() throws SQLException {
        this.ods = new OracleDataSource();
    }
    
    @Override
    public void close() throws SQLException {
        ods.close();
    }

    @Override
    public void setUser(final String user) {
        if (user == null || user.isEmpty()) {
            throw new IllegalArgumentException("User can't be null or empty!");
        }
        else {
            ods.setUser(user);
        }
    }

    @Override
    public void setPassword(final String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password can't be null or empty!");
        }
        else {
            ods.setPassword(password);
        }
    }

    @Override
    public void setUri(final URI uri) {
        if (uri == null) {
            throw new IllegalArgumentException("Connection string URI can't be null!");
        }
        else {
            ods.setURL(uri.toString());
        }
    }

    @Override
    public void setLogger(final DbOperationLoggerFactoryInterface logger) {
        if (logger == null) {
            throw new IllegalArgumentException("Logger can't be null!");
        }
        else {
            this.logger = logger;
        }
    }

    @Override
    public void setDbSchema(final String dbSchema) {
        if (dbSchema == null || dbSchema.isEmpty()) {
            throw new IllegalArgumentException("Database schema can't be null or empty!");
        }
        else {
            this.dbSchema = dbSchema;
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        final Connection conn = ods.getConnection();
        
        conn.setAutoCommit(false);
        return new RadixConnection4ORACLE(conn,logger,dbSchema);
    }

    @Override
    public Connection getConnection(final String username, final String password) throws SQLException {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("User name can't be null or empty!");
        }
        else if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password can't be null or empty!");
        }
        else {
            final Connection conn = ods.getConnection(username, password);
            
            conn.setAutoCommit(false);
            return new RadixConnection4ORACLE(conn,logger,dbSchema);
        }
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return ods.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        ods.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        ods.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return ods.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return ods.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return ods.isWrapperFor(iface);
    }

    @Override
    public boolean canServeURI(final URI uri) {
        if (uri == null) {
            return false;
        }
        else if (!"jdbc".equals(uri.getScheme())) {
            return false;
        }
        else {
            return uri.getSchemeSpecificPart().startsWith("oracle");
        }
    }

    @Override
    public EDatabaseType getDatabaseType() {
        return EDatabaseType.ORACLE;
    }
}
