/*
 * Copyright (c) 2008-2018, Compass Plus Limited. All rights reserved.
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
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EDatabaseType;
import org.radixware.kernel.server.jdbc.wrappers.IRadixJdbcWrapper;

public interface RadixConnection extends Connection {
    /**
     * <p>Get database type for this connection</p>
     * @return database type
     */
    EDatabaseType getDatabaseType();
    
    /**
     * <p>This interface describes log record for logs stored in database</p>
     */
    public interface EventLogItem {
        Timestamp getTime();
        EEventSeverity getSeverity();
        String getCode();
        List<String> getWords();
        String getSource();
        String getContextTypes();
        String getContextIds();
        String getUserName();
        String getStationName();
        boolean isSensitive();
    }    
    
    /**
     * <p>This interface describes a wrapper for the database log record</p>
     */
    public interface EventLogItemWrapper {
        EventLogItem getItem();
        String getWordsStr();
    }

    /**
     * <p>Put one record to the database log</p>
     * @param item item to put
     * @return last event id
     * @throws SQLException 
     */
    Long writeEventLogItem(EventLogItemWrapper item) throws SQLException;
    
    /**
     * <p>Put list of the records database log</p>
     * @param itemList item list to put
     * @return last event id
     * @throws SQLException 
     */
    Long writeEventLogItems(List<EventLogItemWrapper> itemList)throws SQLException;
    
    /**
     * <p>Prepare common SQL string to the database-specific form</p>
     * @param statement statement descriptor with the SQL string
     * @return normalized SQL string
     */
    String normalizeSQLString(Stmt statement);
    
    /**
     * <p>Prepare common SQL string to the database-specific form</p>
     * @param statement SQL statement string
     * @param types parameter's types
     * @return normalized SQL string
     */
    String normalizeSQLString(String statement, int... types);

    /**
     * <p>Prepare statement</p.
     * @param statement statement to prepare
     * @return prepared statement
     * @throws SQLException 
     */
    RadixPreparedStatement prepareStatement(Stmt statement) throws SQLException;

    /**
     * <p>Prepare statement</p.
     * @param statement statement to prepare
     * @param cursorType cursor type
     * @param concurrency concurrency
     * @return prepared statement
     * @throws SQLException 
     */
    PreparedStatement prepareStatement(Stmt statement,int cursorType, int concurrency) throws SQLException;
    
    /**
     * <p>Prepare callable statement</p.
     * @param statement statement to prepare
     * @return prepared statement
     * @throws SQLException 
     */
    CallableStatement prepareCall(Stmt statement) throws SQLException;
    
    /**
     * <p>Create temporary CLOB for the database</p>
     * @return temporary Clob created
     * @throws SQLException 
     */
    Clob createTemporaryClob() throws SQLException;
    
    /**
     * <p>Create temporary BLOB for the database</p>
     * @return temporary Blob created
     * @throws SQLException 
     */
    Blob createTemporaryBlob() throws SQLException;
    
    /**
     * <p>Test that the given Clob is temporaty</p>
     * @param clob clob to test
     * @return true if the Clob is temporary
     * @throws SQLException if the Clob is not exists or ia dead
     */
    boolean isTemporaryClob(Clob clob) throws SQLException;
    
    /**
     * <p>Test that the given Blob is temporaty</p>
     * @param blob Blob to test
     * @return true if the Blob is temporary
     * @throws SQLException if the Blob is not exists or ia dead
     */
    boolean isTemporaryBlob(Blob blob) throws SQLException;

    /**
     * <p>Free temporary Clob was created by {@link #createTemporaryClob()}</p>
     * @param clob Clob to free
     * @throws SQLException 
     */
    void freeTemporaryClob(Clob clob) throws SQLException;
    
    /**
     * <p>Free temporary Blob was created by {@link #createTemporaryBlob()}</p>
     * @param blob Blob to free
     * @throws SQLException 
     */
    void freeTemporaryBlob(Blob clob) throws SQLException;
    
    /**
     * <p>Register connection listener for the database</p>
     * @param listener listener to register
     */
    void registerConnectionListener(IRadixConnectionListener listener);
    
    /**
     * <p>Unregister connection listener for the database</p>
     * @param listener listener to unregister
     */
    void unregisterConnectionListener(IRadixConnectionListener listener);

    /**
     * <p>Forcibly close database connection</p>
     * @param reason reason for closing. Can't be null
     * @return true of closing is forced
     * @throws SQLException 
     */
    boolean forciblyClose(String reason) throws SQLException;
    
    /**
     * <p>Is this connection forcibly closed</p>
     * @return true if yes
     */
    boolean isForciblyClosed();
    
    /**
     * <p>Get force reason by forcibly closed connection</p>
     * @return reason description (can be null)
     */
    String getForcedCloseReason();

    /**
     * <p>Get SQL String which is corrupted by forced closing</p>
     * @return SQl string corrpted (can be null)
     */
    String getSqlWhenForciblyClosed();
    
    /**
     * <p>Is logging of executed SQL statements allowed</p>
     * @return true if yes
     */
    boolean isDbLoggingAllowed();
    
    /**
     * <p>Allow/disallow logging of executed SQL statements</p>
     * @param allowed mode for logging (true - allow logging, false - disallow logging)
     * @return previous logging mode
     */
    boolean setDbLoggingAllowed(boolean allowed);
    
    /**
     * <p>Assign statement for nearest closing operation</p>
     * @param statement statement to assign
     * @return true if assignment is successful
     */   
    boolean scheduleStatementToClose(PreparedStatement statement);
    
    /**
     * <p>Close all statements scheduled for closing</p>
     * @return amount of statement closed
     */
    int closeSchedulledStatements();
    
    /**
     * <p>Close connection if and only if remote client which initiated processing of the
     * request on server has disconnected</p>
     * @return true if disconnection was successful
     * @throws SQLException 
     */
    boolean closeIfClientDisconnected() throws SQLException;
    
    /**
     * <p>Checks if there were write operations since last commit</p>
     * @return true if has
     */
    boolean wereWriteOperations();
    
    /**
     * <p>Cancel the actual operation on this connection</p>
     * @throws SQLException 
     */
    void cancel() throws SQLException;
    
    /**
     * <p>Get last executed SQL</p>
     * @return SQL string associated
     */
    String getLastSql();
    
    /**
     * <p>Set last executed SQL</p>
     * @param lastSql SQL string to associate
     */
    void setLastSql(String lastSql);
    
    /**
     * <p>Can this connection implements implicit caching</p>
     * @return true if yes
     * @throws SQLException 
     */
    boolean getImplicitCachingEnabled() throws SQLException;
    
    /**
     * <p>Allow/disallow implicit caching on this connection</p>
     * @param enabled caching mode (true - allow, false - disallow)
     * @throws SQLException 
     */
    void setImplicitCachingEnabled(boolean enabled) throws SQLException;
    
    /**
     * <p>Get cache size</p>
     * @return cache size. Can be 0
     * @throws SQLException 
     */
    int getStatementCacheSize() throws SQLException;
    
    /**
     * <p>Set cache size</p>
     * @param cacheSize cache size. Need be nonnegative
     * @throws SQLException 
     */
    void setStatementCacheSize(int cacheSize) throws SQLException;
    
    /**
     * <p>Get ObjectCache instance</p>
     * @return ObjectCache instance. Can be null
     */
    public ObjectCacheInterface getObjectCache();
    
    /**
     * Deactivates owner ARTE (if any) if there is a currently running sql query and it's duration
     * has exceeded provided threshold.
     * @param thresholdMs max duration of the DB request before deactivation in milliseconds.
     * @return true if ARTE was deactivated, false otherwise.
     */
    public boolean deactivateArteIfQueryIsLonger(final long thresholdMs);
    
    public String getShortDesc();
    
    public IRadixJdbcWrapper getJdbcWrapper();
    
    public String getSid();
    
    public String getSerial();
    
    String getOperationDescription();
    
    void setOperationDescription(String operationDescription);

}
