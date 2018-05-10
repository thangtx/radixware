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
package org.radixware.kernel.server.jdbcdrivers.oracle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.concurrent.Callable;
import oracle.jdbc.OracleResultSetCache;
import oracle.jdbc.OracleStatement;
import oracle.jdbc.dcn.DatabaseChangeRegistration;
//import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.jdbc.DBOperationLoggerInterface;
import org.radixware.kernel.server.jdbc.EDbOperationType;
import org.radixware.kernel.server.jdbc.RadixConnection;
import org.radixware.kernel.server.jdbc.RadixStatement;
import org.radixware.kernel.server.jdbc.wrappers.IRadixJdbcWrapper;

public class RadixStatement4ORACLE implements RadixStatement, OracleStatement {

    private final OracleStatement delegate;
    private final DBOperationLoggerInterface logger;
    private boolean readOnly = false;
    private final Connection radixConnection;
    protected final IRadixJdbcWrapper objWrapper;

    public RadixStatement4ORACLE(final Connection radixConnection, final Statement delegate, final DBOperationLoggerInterface logger) {
        this.delegate = (OracleStatement) delegate;
        this.radixConnection = radixConnection;
        this.logger = logger;
        this.objWrapper = ((RadixConnection)radixConnection).getJdbcWrapper();
    }
    
    @Override
    public void close() throws SQLException {
        this.delegate.close();
    }

    public void setReadOnly(final boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public void clearDefines() throws SQLException {
        delegate.clearDefines();
    }

    @Override
    public void defineColumnType(int i, int i1) throws SQLException {
        delegate.defineColumnType(i, i1);
    }

    @Override
    public void defineColumnType(int i, int i1, int i2) throws SQLException {
        delegate.defineColumnType(i, i1, i2);
    }

    @Override
    public void defineColumnType(int i, int i1, int i2, short s) throws SQLException {
        delegate.defineColumnType(i, i1, i2, s);
    }

    @Override
    public void defineColumnTypeBytes(int i, int i1, int i2) throws SQLException {
        delegate.defineColumnTypeBytes(i, i1, i2);
    }

    @Override
    public void defineColumnTypeChars(int i, int i1, int i2) throws SQLException {
        delegate.defineColumnTypeChars(i, i1, i2);
    }

    @Override
    public void defineColumnType(int i, int i1, String string) throws SQLException {
        delegate.defineColumnType(i, i1, string);
    }

    @Override
    public int getRowPrefetch() {
        return delegate.getRowPrefetch();
    }

    @Override
    public void setResultSetCache(OracleResultSetCache orsc) throws SQLException {
        delegate.setResultSetCache(orsc);
    }

    @Override
    public void setRowPrefetch(int i) throws SQLException {
        delegate.setRowPrefetch(i);
    }

    @Override
    public int getLobPrefetchSize() {
        return delegate.getLobPrefetchSize();
    }

    @Override
    public void setLobPrefetchSize(int i) throws SQLException {
        delegate.setLobPrefetchSize(i);
    }

    @Override
    public void closeWithKey(String string) throws SQLException {
        delegate.closeWithKey(string);
    }

    @Override
    public int creationState() {
        return delegate.creationState();
    }

    @Override
    public boolean isNCHAR(int i) throws SQLException {
        return delegate.isNCHAR(i);
    }

    @Override
    public void setDatabaseChangeRegistration(DatabaseChangeRegistration dcr) throws SQLException {
        delegate.setDatabaseChangeRegistration(dcr);
    }

    @Override
    public String[] getRegisteredTableNames() throws SQLException {
        return delegate.getRegisteredTableNames();
    }

    @Override
    public long getRegisteredQueryId() throws SQLException {
        return delegate.getRegisteredQueryId();
    }

    protected <T> T executeWrappedOperation(final Callable<T> callable, final boolean actualReadOnly, final String loggedSql) throws Exception {
        if (isReadOnly() && !actualReadOnly) {
            throw new SQLException("RadixStatment is marked as readonly, but non-readonly operation is invoked");
        }
        final RadixConnection4ORACLE.DbOperationInfo info = new RadixConnection4ORACLE.DbOperationInfo(logger.getArteInterface(), actualReadOnly);
        ((RadixConnection4ORACLE)radixConnection).beforeDbOperation(info);
        boolean wasException = true;
        try {
            logger.beforeDbOperation(loggedSql, EDbOperationType.SELECT);//TODO: not always select, should introduce parameters
            try {
                final T result = callable.call();
                wasException = false;
                return result;
            } finally {
                logger.afterDbOperation();
            }
        } finally {
            ((RadixConnection4ORACLE)radixConnection).afterDbOperation(info, wasException);
        }
    }

    protected <T> T executeWrappedOperationThrowsSqlEx(final Callable<T> callable, final boolean readOnly, final String loggedSql) throws SQLException {
        try {
            return executeWrappedOperation(callable, readOnly, loggedSql);
        } catch (Exception ex) {
            if (ex instanceof SQLException) {
                throw (SQLException) ex;
            }
            throw new SQLException("Unexpected " + ex.getClass().getSimpleName() + ": " + ex.getMessage(), ex);
        }
    }

    @Override
    public ResultSet executeQuery(final String sql) throws SQLException {
        return executeWrappedOperationThrowsSqlEx(new Callable<ResultSet>() {

            @Override
            public ResultSet call() throws Exception {
                return objWrapper.wrapResultSet(delegate.executeQuery(sql));
            }
        }, readOnly, sql);
    }

    @Override
    public int executeUpdate(final String sql) throws SQLException {
        return executeWrappedOperationThrowsSqlEx(new Callable<Integer>() {

            @Override
            public Integer call() throws Exception {
                return delegate.executeUpdate(sql);
            }
        }, false, sql);
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return delegate.getMaxFieldSize();
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        delegate.setMaxFieldSize(max);
    }

    @Override
    public int getMaxRows() throws SQLException {
        return delegate.getMaxRows();
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        delegate.setMaxRows(max);
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        delegate.setEscapeProcessing(enable);
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return delegate.getQueryTimeout();
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        delegate.setQueryTimeout(seconds);
    }

    @Override
    public void cancel() throws SQLException {
        delegate.cancel();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return delegate.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        delegate.clearWarnings();
    }

    @Override
    public void setCursorName(String name) throws SQLException {
        delegate.setCursorName(name);
    }

    @Override
    public boolean execute(final String sql) throws SQLException {
        return executeWrappedOperationThrowsSqlEx(new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                return delegate.execute(sql);
            }
        }, readOnly, sql);
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return objWrapper.wrapResultSet(delegate.getResultSet());
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return delegate.getUpdateCount();
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return delegate.getMoreResults();
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        delegate.setFetchDirection(direction);
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return delegate.getFetchDirection();
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        delegate.setFetchSize(rows);
    }

    @Override
    public int getFetchSize() throws SQLException {
        return delegate.getFetchSize();
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return delegate.getResultSetConcurrency();
    }

    @Override
    public int getResultSetType() throws SQLException {
        return delegate.getResultSetType();
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        delegate.addBatch(sql);
    }

    @Override
    public void clearBatch() throws SQLException {
        delegate.clearBatch();
    }

    @Override
    public int[] executeBatch() throws SQLException {
        return executeWrappedOperationThrowsSqlEx(new Callable<int[]>() {

            @Override
            public int[] call() throws Exception {
                return delegate.executeBatch();
            }
        }, false, "executeBatch");
    }

    @Override
    public Connection getConnection() throws SQLException {
        return radixConnection;
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return delegate.getMoreResults(current);
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return delegate.getGeneratedKeys();
    }

    @Override
    public int executeUpdate(final String sql, final int autoGeneratedKeys) throws SQLException {
        return executeWrappedOperationThrowsSqlEx(new Callable<Integer>() {

            @Override
            public Integer call() throws Exception {
                return delegate.executeUpdate(sql, autoGeneratedKeys);
            }
        }, false, sql);
    }

    @Override
    public int executeUpdate(final String sql, final int[] columnIndexes) throws SQLException {
        return executeWrappedOperationThrowsSqlEx(new Callable<Integer>() {

            @Override
            public Integer call() throws Exception {
                return delegate.executeUpdate(sql, columnIndexes);
            }
        }, false, sql);
    }

    @Override
    public int executeUpdate(final String sql, final String[] columnNames) throws SQLException {
        return executeWrappedOperationThrowsSqlEx(new Callable<Integer>() {

            @Override
            public Integer call() throws Exception {
                return delegate.executeUpdate(sql, columnNames);
            }
        }, false, sql);
    }

    @Override
    public boolean execute(final String sql, final int autoGeneratedKeys) throws SQLException {
        return executeWrappedOperationThrowsSqlEx(new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                return delegate.execute(sql, autoGeneratedKeys);
            }
        }, readOnly, sql);
    }

    @Override
    public boolean execute(final String sql, final int[] columnIndexes) throws SQLException {
        return executeWrappedOperationThrowsSqlEx(new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                return delegate.execute(sql, columnIndexes);
            }
        }, readOnly, sql);

    }

    @Override
    public boolean execute(final String sql, final String[] columnNames) throws SQLException {

        return executeWrappedOperationThrowsSqlEx(new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                return delegate.execute(sql, columnNames);
            }
        }, readOnly, sql);
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return delegate.getResultSetHoldability();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return delegate.isClosed();
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        delegate.setPoolable(poolable);
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return delegate.isPoolable();
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        delegate.closeOnCompletion();
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return delegate.isCloseOnCompletion();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return delegate.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return delegate.isWrapperFor(iface);
    }

    @Override
    public void setExecuteBatch(int batchSize) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int sendBatch() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
