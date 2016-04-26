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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.radixware.kernel.server.IDbQueries;

/**
 * <p>This interface describes db queries with advanced functionality. Each statement identifies by in the repository by it's integer number. Not need to be permanent sequential numbers in the repository, but unique.</p>
 */
public interface IExtendedDbQueries extends IDbQueries {
    /**
     * <p>This interface describes database cursor to read data. Pattern to use is:</p>
     * <code>
     * try(final IDbCursor<SomeShit> curs = query(clazz,id,....)) {
     *      for (SomeSheet item  : curs.list(clazz)) {
     *          // Do something...
     *      }
     * }
     * </code>
     */
    public interface IDbCursor<T> extends AutoCloseable {
        /**
         * <p>Is the cursor empty</p>
         * @return true if empty
         * @throws SQLException 
         */
        boolean isEmpty() throws SQLException;
        
        /**
         * <p>Get total records read.</p>
         * @return total records read. Inside the for-each clause will be incremented on the every iteration
         * @throws SQLException 
         */
        int totalRead() throws SQLException;
        
        /**
         * <p>Get record set from the cursor</p>
         * @param <T> awaited data type
         * @param awaited awaited data type class
         * @return record set to use in the for-each clause. Always is not null, but can be empty
         * @throws SQLException 
         */
        <T> Iterable<T> list(Class<T> awaited) throws SQLException;
        
        /**
         * <p>Get record collection form the cursor</p>
         * @param <T> awaited data type
         * @param awaited awaited data type class
         * @return record collection
         * @throws SQLException 
         */
        <T> List<T> collection(Class<T> awaited) throws SQLException;
        
        /**
         * <p>Get actual record id. Can be used inside for-each clause of the list(...) call</p>
         * @return actual id
         * @throws SQLException 
         */
        Long actualId() throws SQLException;
    }
    
    /**
     * <p>This interface describes callback to call from cursor processing loop</p>
     */
    public interface IDbCallback<T> {
        /**
         * <p>Process actual record from the cursor</p>
         * @param recordId record id
         * @param record record data
         * @return true if need continue processing, false otherwise
         * @throws SQLException 
         */
        boolean process(Long recordId, T record) throws SQLException;
    }
    
    /**
     * <p>Get repository size</p>
     * @return repository size
     */
    int size();
    
    /**
     * <p>Get all available identifiers for the given repository</p>
     * @return all available identifiers
     * @throws SQLException 
     */
    Iterable<Integer> getAllIdentifiers() throws SQLException;

    /**
     * <p>Get only really prepared and not closed identifiers for the given repository</p>
     * @return 
     */
    Iterable<Integer> getUsedIdentifiers() throws SQLException;

    /**
     * <p>Get prepared statement text by it's identifier</p>
     * @param statement statement id
     * @return statement text. Should be neither null nor empty
     * @throws IllegalArgumentException if statement id is not exists
     */
    String getStatementText(int statement);
    
    /**
     * <p>Get prepared statement by it's identifier</p>
     * @param statement statement id
     * @return prepared statement. If it isn't prepared or was closed earlier, will be prepared automatically.
     * @throws IllegalArgumentException if statement id is not exists
     * @throws SQLException
     */
    PreparedStatement getPreparedStatement(int statement) throws SQLException;
    
    /**
     * <p>Get really prepared and not closed statements in the statement repository</p>
     * @return prepared statements. Only really prepared and not closed statements will be available in the list
     * @throws SQLException
     */
    Iterable<PreparedStatement> getUsedStatements() throws SQLException;

    /**
     * <p>Bind and execute prepared statement in update mode</p>
     * @param statement statement id
     * @param parameters parameters to bind
     * @throws IllegalArgumentException if statement id is not exists
     * @throws SQLException 
     */
    void execute(int statement,Object... parameters) throws SQLException;
    
    /**
     * <p>Bind and execute prepared statement in query mode</p>
     * @param <T> data type to read data to
     * @param awaited awaited data type
     * @param statement statement id
     * @param parameters parameters to bind
     * @return interface cursor. Never be null
     * @throws IllegalArgumentException if statement id is not exists
     * @throws SQLException 
     */
    <T> IDbCursor<T> query(Class<T> awaited,int statement, Object... parameters) throws SQLException;

    /**
     * <p>Bind and execute prepared statement in query mode</p>
     * @param <T> data type to read data to
     * @param awaited awaited data type
     * @param fill structure to fill on every row
     * @param statement statement id
     * @param parameters parameters to bind
     * @return interface cursor. Never be null
     * @throws IllegalArgumentException if statement id is not exists
     * @throws SQLException 
     */
    <T> IDbCursor<T> query(Class<T> awaited, T fill, int statement, Object... parameters) throws SQLException;

    /**
     * <p>Bind and execute prepared statement in query mode</p>
     * @param <T> data type to read data to
     * @param awaited awaited data type
     * @param callback callback to call on every record was read
     * @param statement statement id
     * @param parameters parameters to bind
     * @return number of records processed as fact
     * @throws SQLException 
     */
    <T> int query(Class<T> awaited, IDbCallback<T> callback, int statement, Object... parameters) throws SQLException;

    /**
     * <p>Bind and execute prepared statement in query mode</p>
     * @param <T> data type to read data to
     * @param awaited awaited data type
     * @param callback callback to call on every record was read
     * @param fill structure to fill on every row
     * @param statement statement id
     * @param parameters parameters to bind
     * @return number of records processed as fact
     * @throws SQLException 
     */
    <T> int query(Class<T> awaited, IDbCallback<T> callback, T fill, int statement, Object... parameters) throws SQLException;
    
    /**
     * <p>Execute prepared statement and test, that it's result set is not empty</p>
     * @param statement statement id
     * @param parameters parameters to bind
     * @return true if result set is not empty
     * @throws SQLException 
     */
    boolean exists(int statement,Object... parameters) throws SQLException;

    /**
     * <p>Execute prepared statement and load exactly one record from the result set</p>
     * @param <T> data type to read data to
     * @param awaited awaited data type
     * @param statement statement id
     * @param parameters parameters to bind
     * @return data record readed
     * @throws IllegalStateException if result set not contains exactly one record
     * @throws SQLException
     */
    <T> T load(Class<T> awaited,int statement, Object... parameters) throws SQLException;

    /**
     * <p>Execute prepared statement and load exactly one record from the result set</p>
     * @param <T> data type to read data to
     * @param awaited awaited data type
     * @param fill structure to fill
     * @param statement statement id
     * @param parameters parameters to bind
     * @return data record readed
     * @throws IllegalStateException if result set not contains exactly one record
     * @throws SQLException
     */
    <T> T load(Class<T> awaited, T fill, int statement, Object... parameters) throws SQLException;
}
