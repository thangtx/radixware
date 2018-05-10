/*
 * Copyright (c) 2008-2018, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.jdbcdrivers.oracle;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleOCIFailover;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleSavepoint;
import oracle.jdbc.aq.AQDequeueOptions;
import oracle.jdbc.aq.AQEnqueueOptions;
import oracle.jdbc.aq.AQMessage;
import oracle.jdbc.aq.AQNotificationRegistration;
import oracle.jdbc.dcn.DatabaseChangeRegistration;
import oracle.jdbc.pool.OracleConnectionCacheCallback;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.BINARY_DOUBLE;
import oracle.sql.BINARY_FLOAT;
import oracle.sql.BLOB;
import oracle.sql.CLOB;
import oracle.sql.DATE;
import oracle.sql.INTERVALDS;
import oracle.sql.INTERVALYM;
import oracle.sql.NUMBER;
import oracle.sql.SQLName;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;
import oracle.sql.TIMESTAMP;
import oracle.sql.TIMESTAMPLTZ;
import oracle.sql.TIMESTAMPTZ;
import oracle.sql.TypeDescriptor;
import java.util.logging.Logger;
import org.radixware.kernel.common.cache.ObjectCache;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.server.jdbc.ARTEReducedInterface;
import org.radixware.kernel.server.jdbc.DbOperationLoggerFactoryInterface;
//import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.jdbc.DBOperationLoggerInterface;
import org.radixware.kernel.server.jdbc.IRadixConnectionListener;
import org.radixware.kernel.common.cache.ObjectCacheInterface;
import org.radixware.kernel.server.jdbc.RadixCallableStatement;
import org.radixware.kernel.server.jdbc.RadixConnection;
import org.radixware.kernel.server.jdbc.RadixPreparedStatement;
import org.radixware.kernel.server.jdbc.Stmt;
import org.radixware.kernel.common.enums.EDatabaseType;
import org.radixware.kernel.server.jdbc.EDbOperationType;
import org.radixware.kernel.server.jdbc.wrappers.RadixJdbcWrapper;
import org.radixware.kernel.server.jdbc.wrappers.IRadixJdbcWrapper;


public class RadixConnection4ORACLE implements RadixConnection, OracleConnection {
    public static final boolean LOG_CLOSE_FROM_NON_ARTE_THREAD = SystemPropUtils.getBooleanSystemProp("rdx.log.close.dbquery.outside.arte", false);

    private static final long DELAY_BEFORE_FIRST_DISCONNECT_CHECK_MILLIS = 10000;
    private static final String PREPARE_EMPTYSTATEMENT = "emptyStatement ";
    private static final String PREPARE_PREFIX = "prepare ";
    private static final String WITH_KEY_PREFIX = "withKey ";
    //
    private final OracleConnection delegate;
    private final DBOperationLoggerInterface logger;
    private final IRadixJdbcWrapper objWrapper;
    private final Object curOperationSem = new Object();
    //guarded by curOperationSem
    private DbOperationInternalInfo curOperationInternalInfo = null;
    //guarded by curOperationSem
    private boolean forciblyClosed = false;
    private String closeReason;
    private String sqlWhenForciblyClosed;
    private boolean wasDeactivated = false;
    //NotThreadSafe
    private boolean wereWriteOperations;
    private boolean dbLoggingAllowed = true;
    private final ObjectCacheInterface objectCache = new ObjectCache();
    private String lastSql;
    //@GuardedBy statementsToClose
    private final List<PreparedStatement> statementsToClose = new ArrayList<>();
    private final List<IRadixConnectionListener> connectionListeners = new ArrayList<>();

    private OracleCallableStatement qryPut = null, qryBulkPut = null;
    private StructDescriptor eventRecordStructDesc;
    private ArrayDescriptor eventRecordArrDesc;
    private final String shortDesc;
    private final String schema;
    private String sid;
    private String serial;
    private String operationDescription;

    public RadixConnection4ORACLE(final Connection delegate, final DbOperationLoggerFactoryInterface loggerFactory, final String schema) {
        this.delegate = (OracleConnection) delegate;
        this.shortDesc = calcShortDesc();
        this.logger = loggerFactory.getLogger(this);
        this.objWrapper = new OracleJdbcWrapper(logger);
        this.schema = schema;
        readSessionInfo();
    }

    private String calcShortDesc() {
        try (PreparedStatement ps = delegate.prepareStatement("select sid, serial# from v$session where audsid = userenv('SESSIONID')")) {
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return delegate.getMetaData().getURL() + "[sid=" + rs.getString("sid") + ",serial#=" + rs.getString("serial#") + "]";
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String getShortDesc() {
        return shortDesc;
    }

    /**
     *
     * @return previous value
     */
    @Override
    public boolean setDbLoggingAllowed(boolean dbLoggingAllowed) {
        final boolean oldVal = this.dbLoggingAllowed;
        this.dbLoggingAllowed = dbLoggingAllowed;
        return oldVal;
    }

    @Override
    public boolean isDbLoggingAllowed() {
        return dbLoggingAllowed;
    }

    @Override
    public int closeSchedulledStatements() {
        final List<PreparedStatement> snapshot = new ArrayList<>();
        synchronized (statementsToClose) {
            snapshot.addAll(statementsToClose);
            statementsToClose.clear();
        }

        for (PreparedStatement statement : snapshot) {
            try {
                statement.close();
                if (LOG_CLOSE_FROM_NON_ARTE_THREAD) {
//                    final Instance instance = Instance.get();
//                    instance.getTrace().put(EEventSeverity.WARNING, Thread.currentThread().getName() + " closed statement " + statement, EEventSource.INSTANCE);
                    logger.getRadixTrace().put(EEventSeverity.WARNING, Thread.currentThread().getName() + " closed statement " + statement, EEventSource.INSTANCE);
                }
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.WARNING,"Error while closing statement schedulled for close", ex);
            }
        }
        return snapshot.size();

    }

    @Override
    public void registerConnectionListener(final IRadixConnectionListener listener) {
        if (listener != null) {
            connectionListeners.add(listener);
        }
    }

    @Override
    public void unregisterConnectionListener(final IRadixConnectionListener listener) {
        if (listener != null) {
            connectionListeners.remove(listener);
        }
    }

    @Override
    public boolean scheduleStatementToClose(final PreparedStatement statement) {
        try {
            if (statement.getConnection() == this) {
                synchronized (statementsToClose) {
                    statementsToClose.add(statement);
                }
            }
        } catch (SQLException ex) {
            return false;
        }
        return false;
    }

    @Override
    public boolean closeIfClientDisconnected() throws SQLException {
        synchronized (curOperationSem) {
            if (forciblyClosed) {
                return false;
            }
            if (curOperationInternalInfo == null || curOperationInternalInfo.getInfo().getArte() == null) {
                return false;
            }
            if (System.currentTimeMillis() - curOperationInternalInfo.getStartMillis() < DELAY_BEFORE_FIRST_DISCONNECT_CHECK_MILLIS) {
                return false;
            }
            if (curOperationInternalInfo.getInfo().getArte().needBreak()) {
                final String clientDescr = curOperationInternalInfo.getInfo().getArte().getRequestClientId() == null ? "<null>" : curOperationInternalInfo.getInfo().getArte().getRequestClientId();
                return forciblyClose("client '" + clientDescr + "' disconnected");
            }
        }
        return false;
    }

    @Override
    public boolean forciblyClose(final String closeReason) throws SQLException {
        synchronized (curOperationSem) {
            final String curSql = logger.getExecutingSql();
            final EEventSeverity messageSeverity = curOperationInternalInfo == null ? null : (curOperationInternalInfo.suppressForciblyCloseErrors() ? EEventSeverity.DEBUG : EEventSeverity.WARNING);
            try {
                cancel();
            } catch (Exception ex) {
                if (messageSeverity != null) {
//                    curOperationInternalInfo.getInfo().getArte().getInstance().getTrace().put(messageSeverity, "Error while trying to cancel current db operation on hanging " + curOperationInternalInfo.getInfo().getArte().getProcessorThread().getName() + ": " + ExceptionTextFormatter.exceptionStackToString(ex), EEventSource.INSTANCE);
                    curOperationInternalInfo.getInfo().getArte().getRadixTrace().put(messageSeverity, "Error while trying to cancel current db operation on hanging " + curOperationInternalInfo.getInfo().getArte().getProcessorThread().getName() + ": " + ExceptionTextFormatter.exceptionStackToString(ex), EEventSource.INSTANCE);
                }
            }
            try {
                rollback();
            } catch (Exception ex) {
                if (messageSeverity != null) {
                    curOperationInternalInfo.getInfo().getArte().getRadixTrace().put(messageSeverity, "Error while trying to rollback current db transaction on hanging " + curOperationInternalInfo.getInfo().getArte().getProcessorThread().getName() + ": " + ExceptionTextFormatter.exceptionStackToString(ex), EEventSource.INSTANCE);
                }
            }
            try {
                close();
                forciblyClosed = true;
                this.closeReason = closeReason;
                sqlWhenForciblyClosed = curSql;
                return true;
            } catch (Exception ex) {
                if (messageSeverity != null) {
                    curOperationInternalInfo.getInfo().getArte().getRadixTrace().put(messageSeverity, "Error while trying to close db connection for hanging " + curOperationInternalInfo.getInfo().getArte().getProcessorThread().getName() + ": " + ExceptionTextFormatter.exceptionStackToString(ex), EEventSource.INSTANCE);
                }
            }
        }
        return false;
    }

    @Override
    public boolean isForciblyClosed() {
        synchronized (curOperationSem) {
            return forciblyClosed;
        }
    }

    @Override
    public String getForcedCloseReason() {
        synchronized (curOperationSem) {
            return closeReason;
        }
    }

    @Override
    public String getSqlWhenForciblyClosed() {
        synchronized (curOperationSem) {
            return sqlWhenForciblyClosed;
        }
    }

    @Override
    public void setLastSql(String lastSql) {
        this.lastSql = lastSql;
    }

    @Override
    public String getLastSql() {
        return lastSql;
    }

    public boolean deactivateArteIfQueryIsLonger(final long thresholdMs) {
        synchronized (curOperationSem) {
            if (curOperationInternalInfo == null || curOperationInternalInfo.getInfo().getArte() == null) {
                return false;
            }
            if (System.currentTimeMillis() - curOperationInternalInfo.getStartMillis() > thresholdMs) {
                curOperationInternalInfo.getInfo().getArte().markInactive("long db query");
                wasDeactivated = true;
                return true;
            }
        }
        return false;
    }

    protected void beforeDbOperation(final DbOperationInfo operationInfo) {
        if (operationInfo != null) {
            if (!operationInfo.isReadOnly()) {
                wereWriteOperations = true;
            }
            synchronized (curOperationSem) {
                if (forciblyClosed) {
                    logger.throwDatabaseError(getClosedMessage(), null);
                }
                curOperationInternalInfo = new DbOperationInternalInfo(operationInfo, System.currentTimeMillis());
            }
        }
    }

    protected void afterDbOperation(final DbOperationInfo operationInfo, boolean wasException) {
        ARTEReducedInterface arteToActivate = null;
        try {
            synchronized (curOperationSem) {
                if (wasDeactivated) {
                    wasDeactivated = false;
                    arteToActivate = curOperationInternalInfo != null && curOperationInternalInfo.getInfo() != null ? curOperationInternalInfo.getInfo().getArte() : null;
                }
                curOperationInternalInfo = null;
                if (forciblyClosed && !wasException) {
                    logger.throwDatabaseError(getClosedMessage(), null);
                }
            }
        } finally {
            if (arteToActivate != null) {
                arteToActivate.markActive("db query end");
            }
        }
    }

    private String getClosedMessage() {
        return "Database connection was forcibly closed" + (closeReason == null ? "" : " (" + closeReason + ")");
    }

    @Override
    public boolean wereWriteOperations() {
        return wereWriteOperations;
    }

    private void afterCommitOrRollbackSafe() {
        wereWriteOperations = false;
    }

    private void callBeforeCommitHandlers() {
        try {
            for (IRadixConnectionListener listener : connectionListeners) {
                try {
                    listener.beforeDbCommit();
                } catch (Throwable t) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE,"Exception on calling before commit handler: " + t.getMessage(), t);
                }
            }
        } catch (Throwable t) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,"Exception on calling before commit handlers: " + t.getMessage(), t);
        }
    }

    private void callAfterCommitHandlers() {
        try {
            for (IRadixConnectionListener listener : connectionListeners) {
                try {
                    listener.afterDbCommit();
                } catch (Throwable t) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE,"Exception on calling after commit handler: " + t.getMessage(), t);
                }
            }
        } catch (Throwable t) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,"Exception on calling after commit handlers: " + t.getMessage(), t);
        }
    }

    private void callCommitErrorHandlers(final SQLException ex) {
        try {
            for (IRadixConnectionListener listener : connectionListeners) {
                try {
                    listener.afterDbCommit();
                } catch (Throwable t) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE,"Exception on calling commit error handler: " + t.getMessage(), t);
                }
            }
        } catch (Throwable t) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,"Exception on calling commit error handlers: " + t.getMessage(), t);
        }
    }

    private void callBeforeRollbackHandlers(final Savepoint sp) {
        try {
            for (IRadixConnectionListener listener : connectionListeners) {
                try {
                    listener.beforeDbRollback(sp, sp == null ? null : sp.getSavepointName(), sp == null ? 0 : tryCalcSpNesting(sp.getSavepointName()));
                } catch (Throwable t) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE,"Exception on calling before rollback handler: " + t.getMessage(), t);
                }
            }
        } catch (Throwable t) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,"Exception on calling before rollback handlers: " + t.getMessage(), t);
        }
    }

    private void callAfterRollbackHandlers(final Savepoint sp) {
        try {
            for (IRadixConnectionListener listener : connectionListeners) {
                try {
                    listener.afterDbRollback(sp, sp == null ? null : sp.getSavepointName(), sp == null ? 0 : tryCalcSpNesting(sp.getSavepointName()));
                } catch (Throwable t) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE,"Exception on calling after rollback handler: " + t.getMessage(), t);
                }
            }
        } catch (Throwable t) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,"Exception on calling after rollback handlers: " + t.getMessage(), t);
        }
    }

    private long tryCalcSpNesting(final String savepointId) {
        try {
            return logger.getArteInterface().staticCalcSpNesting(savepointId);
        } catch (Exception ex) {
            return -1;
        }
    }

    private void callRollbackErrorHandlers(final Savepoint sp, final SQLException ex) {
        try {
            for (IRadixConnectionListener listener : connectionListeners) {
                try {
                    listener.onDbRollbackError(sp, sp == null ? null : sp.getSavepointName(), sp == null ? 0 : tryCalcSpNesting(sp.getSavepointName()), ex);
                } catch (Throwable t) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE,"Exception on calling rollback error handler: " + t.getMessage(), t);
                }
            }
        } catch (Throwable t) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,"Exception on calling rollback error handlers: " + t.getMessage(), t);
        }
    }

    @Override
    public void commit(final EnumSet<CommitOption> es) throws SQLException {
        callBeforeCommitHandlers();
        try {
            logger.beforeDbOperation("commit with options", EDbOperationType.OTHER);
            try {
                delegate.commit(es);
            } finally {
                afterCommitOrRollbackSafe();
                logger.afterDbOperation();
            }
        } catch (SQLException ex) {
            callCommitErrorHandlers(ex);
            throw ex;
        }
        callAfterCommitHandlers();
    }

    @Override
    public void archive(int i, int i1, String string) throws SQLException {
        delegate.archive(i, i1, string);
    }

    @Override
    public void openProxySession(int i, Properties prprts) throws SQLException {
        delegate.openProxySession(i, prprts);
    }

    @Override
    public boolean getAutoClose() throws SQLException {
        return delegate.getAutoClose();
    }

    @Override
    public int getDefaultExecuteBatch() {
        return delegate.getDefaultExecuteBatch();
    }

    @Override
    public int getDefaultRowPrefetch() {
        return delegate.getDefaultRowPrefetch();
    }

    @Override
    public Object getDescriptor(String string) {
        return delegate.getDescriptor(string);
    }

    @Override
    public String[] getEndToEndMetrics() throws SQLException {
        return delegate.getEndToEndMetrics();
    }

    @Override
    public short getEndToEndECIDSequenceNumber() throws SQLException {
        return delegate.getEndToEndECIDSequenceNumber();
    }

    @Override
    public boolean getIncludeSynonyms() {
        return delegate.getIncludeSynonyms();
    }

    @Override
    public boolean getRestrictGetTables() {
        return delegate.getRestrictGetTables();
    }

    @Override
    public Object getJavaObject(String string) throws SQLException {
        return delegate.getJavaObject(string);
    }

    @Override
    public boolean getRemarksReporting() {
        return delegate.getRemarksReporting();
    }

    @Override
    public String getSQLType(Object o) throws SQLException {
        return delegate.getSQLType(o);
    }

    @Override
    public int getStmtCacheSize() {
        return delegate.getStmtCacheSize();
    }

    @Override
    public short getStructAttrCsId() throws SQLException {
        return delegate.getStructAttrCsId();
    }

    @Override
    public String getUserName() throws SQLException {
        return delegate.getUserName();
    }

    @Override
    public String getCurrentSchema() throws SQLException {
        return delegate.getCurrentSchema();
    }

    @Override
    public boolean getUsingXAFlag() {
        return delegate.getUsingXAFlag();
    }

    @Override
    public boolean getXAErrorFlag() {
        return delegate.getXAErrorFlag();
    }

    @Override
    public int pingDatabase() throws SQLException {
        return delegate.pingDatabase();
    }

    @Override
    public int pingDatabase(int i) throws SQLException {
        return delegate.pingDatabase(i);
    }

    @Override
    public void putDescriptor(String string, Object o) throws SQLException {
        delegate.putDescriptor(string, o);
    }

    @Override
    public void registerSQLType(String string, Class type) throws SQLException {
        delegate.registerSQLType(string, type);
    }

    @Override
    public void registerSQLType(String string, String string1) throws SQLException {
        delegate.registerSQLType(string, string1);
    }

    @Override
    public void setAutoClose(boolean bln) throws SQLException {
        delegate.setAutoClose(bln);
    }

    @Override
    public void setDefaultExecuteBatch(int i) throws SQLException {
        delegate.setDefaultExecuteBatch(i);
    }

    @Override
    public void setDefaultRowPrefetch(int i) throws SQLException {
        delegate.setDefaultRowPrefetch(i);
    }

    @Override
    public void setEndToEndMetrics(String[] strings, short s) throws SQLException {
        delegate.setEndToEndMetrics(strings, s);
    }

    @Override
    public void setIncludeSynonyms(boolean bln) {
        delegate.setIncludeSynonyms(bln);
    }

    @Override
    public void setRemarksReporting(boolean bln) {
        delegate.setRemarksReporting(bln);
    }

    @Override
    public void setRestrictGetTables(boolean bln) {
        delegate.setRestrictGetTables(bln);
    }

    @Override
    public void setStmtCacheSize(int i) throws SQLException {
        delegate.setStmtCacheSize(i);
    }

    @Override
    public void setStmtCacheSize(int i, boolean bln) throws SQLException {
        delegate.setStmtCacheSize(i, bln);
    }

    @Override
    public void setStatementCacheSize(int i) throws SQLException {
        delegate.setStatementCacheSize(i);
    }

    @Override
    public int getStatementCacheSize() throws SQLException {
        return delegate.getStatementCacheSize();
    }

    @Override
    public void setImplicitCachingEnabled(boolean bln) throws SQLException {
        delegate.setImplicitCachingEnabled(bln);
    }

    @Override
    public boolean getImplicitCachingEnabled() throws SQLException {
        return delegate.getImplicitCachingEnabled();
    }

    @Override
    public void setExplicitCachingEnabled(boolean bln) throws SQLException {
        delegate.setExplicitCachingEnabled(bln);
    }

    @Override
    public boolean getExplicitCachingEnabled() throws SQLException {
        return delegate.getExplicitCachingEnabled();
    }

    @Override
    public void purgeImplicitCache() throws SQLException {
        delegate.purgeImplicitCache();
    }

    @Override
    public void purgeExplicitCache() throws SQLException {
        delegate.purgeExplicitCache();
    }

    @Override
    public RadixPreparedStatement4ORACLE getStatementWithKey(String string) throws SQLException {
        return new RadixPreparedStatement4ORACLE(this, (OraclePreparedStatement) delegate.getStatementWithKey(string), WITH_KEY_PREFIX + string, logger);
    }

    @Override
    public RadixCallableStatement4ORACLE getCallWithKey(String string) throws SQLException {
        return new RadixCallableStatement4ORACLE(this, (OracleCallableStatement) delegate.getCallWithKey(string), WITH_KEY_PREFIX + string, logger);
    }

    @Override
    public void setUsingXAFlag(boolean bln) {
        delegate.setUsingXAFlag(bln);
    }

    @Override
    public void setXAErrorFlag(boolean bln) {
        delegate.setXAErrorFlag(bln);
    }

    @Override
    public void shutdown(DatabaseShutdownMode dsm) throws SQLException {
        delegate.shutdown(dsm);
    }

    @Override
    public void startup(String string, int i) throws SQLException {
        delegate.startup(string, i);
    }

    @Override
    public void startup(DatabaseStartupMode dsm) throws SQLException {
        delegate.startup(dsm);
    }

    @Override
    public RadixPreparedStatement4ORACLE prepareStatementWithKey(String string) throws SQLException {
        return new RadixPreparedStatement4ORACLE(this, (OraclePreparedStatement) delegate.prepareStatementWithKey(string), WITH_KEY_PREFIX + string, logger);
    }

    @Override
    public RadixCallableStatement4ORACLE prepareCallWithKey(String string) throws SQLException {
        return new RadixCallableStatement4ORACLE(this, (RadixCallableStatement4ORACLE) delegate.prepareCallWithKey(string), WITH_KEY_PREFIX + string, logger);
    }

    @Override
    public void setCreateStatementAsRefCursor(boolean bln) {
        delegate.setCreateStatementAsRefCursor(bln);
    }

    @Override
    public boolean getCreateStatementAsRefCursor() {
        return delegate.getCreateStatementAsRefCursor();
    }

    @Override
    public void setSessionTimeZone(String string) throws SQLException {
        delegate.setSessionTimeZone(string);
    }

    @Override
    public String getSessionTimeZone() {
        return delegate.getSessionTimeZone();
    }

    @Override
    public String getSessionTimeZoneOffset() throws SQLException {
        return delegate.getSessionTimeZoneOffset();
    }

    @Override
    public Properties getProperties() {
        return delegate.getProperties();
    }

    @Override
    public Connection _getPC() {
        return delegate._getPC();
    }

    @Override
    public boolean isLogicalConnection() {
        return delegate.isLogicalConnection();
    }

    @Override
    public void registerTAFCallback(OracleOCIFailover oocif, Object o) throws SQLException {
        delegate.registerTAFCallback(oocif, o);
    }

    @Override
    public OracleConnection unwrap() {
        return delegate.unwrap();
    }

    @Override
    public void setWrapper(OracleConnection oc) {
        delegate.setWrapper(oc);
    }

    @Override
    public oracle.jdbc.internal.OracleConnection physicalConnectionWithin() {
        return delegate.physicalConnectionWithin();
    }

    @Override
    public OracleSavepoint oracleSetSavepoint() throws SQLException {
        return delegate.oracleSetSavepoint();
    }

    @Override
    public OracleSavepoint oracleSetSavepoint(String string) throws SQLException {
        return delegate.oracleSetSavepoint(string);
    }

    @Override
    public void oracleRollback(OracleSavepoint os) throws SQLException {
        callBeforeRollbackHandlers(os);
        try {
            logger.beforeDbOperation("ora rollback sp", EDbOperationType.OTHER);
            try {
                delegate.oracleRollback(os);
            } finally {
                afterCommitOrRollbackSafe();
                logger.afterDbOperation();
            }
        } catch (SQLException ex) {
            callRollbackErrorHandlers(os, ex);
            throw ex;
        }
        callAfterRollbackHandlers(os);
    }

    @Override
    public void oracleReleaseSavepoint(OracleSavepoint os) throws SQLException {
        delegate.oracleReleaseSavepoint(os);
    }

    @Override
    public void close(Properties prprts) throws SQLException {
        delegate.close(prprts);
    }

    @Override
    public void close(int i) throws SQLException {
        delegate.close(i);
    }

    @Override
    public boolean isProxySession() {
        return delegate.isProxySession();
    }

    @Override
    public void applyConnectionAttributes(Properties prprts) throws SQLException {
        delegate.applyConnectionAttributes(prprts);
    }

    @Override
    public Properties getConnectionAttributes() throws SQLException {
        return delegate.getConnectionAttributes();
    }

    @Override
    public Properties getUnMatchedConnectionAttributes() throws SQLException {
        return delegate.getUnMatchedConnectionAttributes();
    }

    @Override
    public void registerConnectionCacheCallback(OracleConnectionCacheCallback occc, Object o, int i) throws SQLException {
        delegate.registerConnectionCacheCallback(occc, o, i);
    }

    @Override
    public void setConnectionReleasePriority(int i) throws SQLException {
        delegate.setConnectionReleasePriority(i);
    }

    @Override
    public int getConnectionReleasePriority() throws SQLException {
        return delegate.getConnectionReleasePriority();
    }

    @Override
    public void setPlsqlWarnings(String string) throws SQLException {
        delegate.setPlsqlWarnings(string);
    }

    @Override
    public AQNotificationRegistration[] registerAQNotification(String[] strings, Properties[] prprtss, Properties prprts) throws SQLException {
        return delegate.registerAQNotification(strings, prprtss, prprts);
    }

    @Override
    public void unregisterAQNotification(AQNotificationRegistration aqnr) throws SQLException {
        delegate.unregisterAQNotification(aqnr);
    }

    @Override
    public AQMessage dequeue(String string, AQDequeueOptions aqdo, byte[] bytes) throws SQLException {
        return delegate.dequeue(string, aqdo, bytes);
    }

    @Override
    public AQMessage dequeue(String string, AQDequeueOptions aqdo, String string1) throws SQLException {
        return delegate.dequeue(string, aqdo, string1);
    }

    @Override
    public void enqueue(String string, AQEnqueueOptions aqeo, AQMessage aqm) throws SQLException {
        delegate.enqueue(string, aqeo, aqm);
    }

    @Override
    public DatabaseChangeRegistration registerDatabaseChangeNotification(Properties prprts) throws SQLException {
        return delegate.registerDatabaseChangeNotification(prprts);
    }

    @Override
    public DatabaseChangeRegistration getDatabaseChangeRegistration(int i) throws SQLException {
        return delegate.getDatabaseChangeRegistration(i);
    }

    @Override
    public void unregisterDatabaseChangeNotification(DatabaseChangeRegistration dcr) throws SQLException {
        delegate.unregisterDatabaseChangeNotification(dcr);
    }

    @Override
    public void unregisterDatabaseChangeNotification(int i, String string, int i1) throws SQLException {
        delegate.unregisterDatabaseChangeNotification(i, string, i1);
    }

    @Override
    public void unregisterDatabaseChangeNotification(int i) throws SQLException {
        delegate.unregisterDatabaseChangeNotification(i);
    }

    @Override
    public void unregisterDatabaseChangeNotification(long l, String string) throws SQLException {
        delegate.unregisterDatabaseChangeNotification(l, string);
    }

    @Override
    public ARRAY createARRAY(String string, Object o) throws SQLException {
        return delegate.createARRAY(string, o);
    }

    @Override
    public BINARY_DOUBLE createBINARY_DOUBLE(double d) throws SQLException {
        return delegate.createBINARY_DOUBLE(d);
    }

    @Override
    public BINARY_FLOAT createBINARY_FLOAT(float f) throws SQLException {
        return delegate.createBINARY_FLOAT(f);
    }

    @Override
    public DATE createDATE(Date date) throws SQLException {
        return delegate.createDATE(date);
    }

    @Override
    public DATE createDATE(Time time) throws SQLException {
        return delegate.createDATE(time);
    }

    @Override
    public DATE createDATE(Timestamp tmstmp) throws SQLException {
        return delegate.createDATE(tmstmp);
    }

    @Override
    public DATE createDATE(Date date, Calendar clndr) throws SQLException {
        return delegate.createDATE(date, clndr);
    }

    @Override
    public DATE createDATE(Time time, Calendar clndr) throws SQLException {
        return delegate.createDATE(time, clndr);
    }

    @Override
    public DATE createDATE(Timestamp tmstmp, Calendar clndr) throws SQLException {
        return delegate.createDATE(tmstmp, clndr);
    }

    @Override
    public DATE createDATE(String string) throws SQLException {
        return delegate.createDATE(string);
    }

    @Override
    public INTERVALDS createINTERVALDS(String string) throws SQLException {
        return delegate.createINTERVALDS(string);
    }

    @Override
    public INTERVALYM createINTERVALYM(String string) throws SQLException {
        return delegate.createINTERVALYM(string);
    }

    @Override
    public NUMBER createNUMBER(boolean bln) throws SQLException {
        return delegate.createNUMBER(bln);
    }

    @Override
    public NUMBER createNUMBER(byte b) throws SQLException {
        return delegate.createNUMBER(b);
    }

    @Override
    public NUMBER createNUMBER(short s) throws SQLException {
        return delegate.createNUMBER(s);
    }

    @Override
    public NUMBER createNUMBER(int i) throws SQLException {
        return delegate.createNUMBER(i);
    }

    @Override
    public NUMBER createNUMBER(long l) throws SQLException {
        return delegate.createNUMBER(l);
    }

    @Override
    public NUMBER createNUMBER(float f) throws SQLException {
        return delegate.createNUMBER(f);
    }

    @Override
    public NUMBER createNUMBER(double d) throws SQLException {
        return delegate.createNUMBER(d);
    }

    @Override
    public NUMBER createNUMBER(BigDecimal bd) throws SQLException {
        return delegate.createNUMBER(bd);
    }

    @Override
    public NUMBER createNUMBER(BigInteger bi) throws SQLException {
        return delegate.createNUMBER(bi);
    }

    @Override
    public NUMBER createNUMBER(String string, int i) throws SQLException {
        return delegate.createNUMBER(string, i);
    }

    @Override
    public TIMESTAMP createTIMESTAMP(Date date) throws SQLException {
        return delegate.createTIMESTAMP(date);
    }

    @Override
    public TIMESTAMP createTIMESTAMP(DATE date) throws SQLException {
        return delegate.createTIMESTAMP(date);
    }

    @Override
    public TIMESTAMP createTIMESTAMP(Time time) throws SQLException {
        return delegate.createTIMESTAMP(time);
    }

    @Override
    public TIMESTAMP createTIMESTAMP(Timestamp tmstmp) throws SQLException {
        return delegate.createTIMESTAMP(tmstmp);
    }

    @Override
    public TIMESTAMP createTIMESTAMP(String string) throws SQLException {
        return delegate.createTIMESTAMP(string);
    }

    @Override
    public TIMESTAMPTZ createTIMESTAMPTZ(Date date) throws SQLException {
        return delegate.createTIMESTAMPTZ(date);
    }

    @Override
    public TIMESTAMPTZ createTIMESTAMPTZ(Date date, Calendar clndr) throws SQLException {
        return delegate.createTIMESTAMPTZ(date, clndr);
    }

    @Override
    public TIMESTAMPTZ createTIMESTAMPTZ(Time time) throws SQLException {
        return delegate.createTIMESTAMPTZ(time);
    }

    @Override
    public TIMESTAMPTZ createTIMESTAMPTZ(Time time, Calendar clndr) throws SQLException {
        return delegate.createTIMESTAMPTZ(time, clndr);
    }

    @Override
    public TIMESTAMPTZ createTIMESTAMPTZ(Timestamp tmstmp) throws SQLException {
        return delegate.createTIMESTAMPTZ(tmstmp);
    }

    @Override
    public TIMESTAMPTZ createTIMESTAMPTZ(Timestamp tmstmp, Calendar clndr) throws SQLException {
        return delegate.createTIMESTAMPTZ(tmstmp, clndr);
    }

    @Override
    public TIMESTAMPTZ createTIMESTAMPTZ(String string) throws SQLException {
        return delegate.createTIMESTAMPTZ(string);
    }

    @Override
    public TIMESTAMPTZ createTIMESTAMPTZ(String string, Calendar clndr) throws SQLException {
        return delegate.createTIMESTAMPTZ(string, clndr);
    }

    @Override
    public TIMESTAMPTZ createTIMESTAMPTZ(DATE date) throws SQLException {
        return delegate.createTIMESTAMPTZ(date);
    }

    @Override
    public TIMESTAMPLTZ createTIMESTAMPLTZ(Date date, Calendar clndr) throws SQLException {
        return delegate.createTIMESTAMPLTZ(date, clndr);
    }

    @Override
    public TIMESTAMPLTZ createTIMESTAMPLTZ(Time time, Calendar clndr) throws SQLException {
        return delegate.createTIMESTAMPLTZ(time, clndr);
    }

    @Override
    public TIMESTAMPLTZ createTIMESTAMPLTZ(Timestamp tmstmp, Calendar clndr) throws SQLException {
        return delegate.createTIMESTAMPLTZ(tmstmp, clndr);
    }

    @Override
    public TIMESTAMPLTZ createTIMESTAMPLTZ(String string, Calendar clndr) throws SQLException {
        return delegate.createTIMESTAMPLTZ(string, clndr);
    }

    @Override
    public TIMESTAMPLTZ createTIMESTAMPLTZ(DATE date, Calendar clndr) throws SQLException {
        return delegate.createTIMESTAMPLTZ(date, clndr);
    }

    @Override
    public void cancel() throws SQLException {
        delegate.cancel();
    }

    @Override
    public void abort() throws SQLException {
        delegate.abort();
    }

    @Override
    public TypeDescriptor[] getAllTypeDescriptorsInCurrentSchema() throws SQLException {
        return delegate.getAllTypeDescriptorsInCurrentSchema();
    }

    @Override
    public TypeDescriptor[] getTypeDescriptorsFromListInCurrentSchema(String[] strings) throws SQLException {
        return delegate.getTypeDescriptorsFromListInCurrentSchema(strings);
    }

    @Override
    public TypeDescriptor[] getTypeDescriptorsFromList(String[][] strings) throws SQLException {
        return delegate.getTypeDescriptorsFromList(strings);
    }

    @Override
    public String getDataIntegrityAlgorithmName() throws SQLException {
        return delegate.getDataIntegrityAlgorithmName();
    }

    @Override
    public String getEncryptionAlgorithmName() throws SQLException {
        return delegate.getEncryptionAlgorithmName();
    }

    @Override
    public String getAuthenticationAdaptorName() throws SQLException {
        return delegate.getAuthenticationAdaptorName();
    }

    @Override
    public boolean isUsable() {
        return delegate.isUsable();
    }

    @Override
    public void setDefaultTimeZone(TimeZone tz) throws SQLException {
        delegate.setDefaultTimeZone(tz);
    }

    @Override
    public TimeZone getDefaultTimeZone() throws SQLException {
        return delegate.getDefaultTimeZone();
    }

    @Override
    public void setApplicationContext(String string, String string1, String string2) throws SQLException {
        delegate.setApplicationContext(string, string1, string2);
    }

    @Override
    public void clearAllApplicationContext(String string) throws SQLException {
        delegate.clearAllApplicationContext(string);
    }

    @Override
    public Statement createStatement() throws SQLException {
        logger.beforeDbOperation(PREPARE_EMPTYSTATEMENT, EDbOperationType.OTHER);
        try {
            return new RadixStatement4ORACLE(this, delegate.createStatement(),logger);
        } finally {
            logger.afterDbOperation();
        }
    }

    @Override
    public RadixPreparedStatement prepareStatement(String sql) throws SQLException {
        logger.beforeDbOperation(sql, EDbOperationType.PREPARE);
        try {
            return new RadixPreparedStatement4ORACLE(this, delegate.prepareStatement(sql), sql, logger);
        } finally {
            logger.afterDbOperation();
        }
    }

    @Override
    public RadixCallableStatement prepareCall(String sql) throws SQLException {
        logger.beforeDbOperation(sql, EDbOperationType.PREPARE);
        try {
            return new RadixCallableStatement4ORACLE(this, delegate.prepareCall(sql), sql, logger);
        } finally {
            logger.afterDbOperation();
        }
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return delegate.nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        delegate.setAutoCommit(autoCommit);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return delegate.getAutoCommit();
    }

    @Override
    public void commit() throws SQLException {
        callBeforeCommitHandlers();
        try {
            logger.beforeDbOperation("commit", EDbOperationType.OTHER);
            try {
                delegate.commit();
            } finally {
                afterCommitOrRollbackSafe();
                logger.afterDbOperation();
            }
        } catch (SQLException ex) {
            callCommitErrorHandlers(ex);
            throw ex;
        }
        callAfterCommitHandlers();
        objectCache.maintenance();
    }

    @Override
    public void rollback() throws SQLException {
        callBeforeRollbackHandlers(null);
        try {
            logger.beforeDbOperation("rollback", EDbOperationType.OTHER);
            try {
                delegate.rollback();
            } finally {
                afterCommitOrRollbackSafe();
                logger.afterDbOperation();
            }
        } catch (SQLException ex) {
            callRollbackErrorHandlers(null, ex);
            throw ex;
        }
        callAfterRollbackHandlers(null);
    }

    @Override
    public ObjectCacheInterface getObjectCache() {
        return objectCache;
    }

    @Override
    public void close() throws SQLException {
        if (qryPut != null) {
            qryPut.close();
            qryPut = null;
        }
        if (qryBulkPut != null) {
            qryBulkPut.close();
            qryBulkPut = null;
        }
//        eventRecordStructDesc = null;
//        eventRecordArrDesc = null;
        delegate.close();
        objectCache.clear();

    }

    @Override
    public boolean isClosed() throws SQLException {
        return delegate.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return delegate.getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        delegate.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return delegate.isReadOnly();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        delegate.setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        return delegate.getCatalog();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        delegate.setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return delegate.getTransactionIsolation();
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
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        logger.beforeDbOperation(PREPARE_EMPTYSTATEMENT, EDbOperationType.OTHER);
        try {
            return new RadixStatement4ORACLE(this, delegate.createStatement(resultSetType, resultSetConcurrency),logger);
        } finally {
            logger.afterDbOperation();
        }
    }

    @Override
    public RadixPreparedStatement4ORACLE prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        logger.beforeDbOperation(sql, EDbOperationType.PREPARE);
        try {
            return new RadixPreparedStatement4ORACLE(this, delegate.prepareStatement(sql, resultSetType, resultSetConcurrency), sql, logger);
        } finally {
            logger.afterDbOperation();
        }

    }

    @Override
    public RadixCallableStatement4ORACLE prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        logger.beforeDbOperation(sql, EDbOperationType.PREPARE);
        try {
            return new RadixCallableStatement4ORACLE(this, delegate.prepareCall(sql, resultSetType, resultSetConcurrency), sql, logger);
        } finally {
            logger.afterDbOperation();
        }
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return delegate.getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        delegate.setTypeMap(map);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        delegate.setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        return delegate.getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return delegate.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return delegate.setSavepoint(name);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        callBeforeRollbackHandlers(savepoint);
        try {
            logger.beforeDbOperation("rollback sp", EDbOperationType.OTHER);
            try {
                delegate.rollback(savepoint);
            } finally {
                afterCommitOrRollbackSafe();
                logger.afterDbOperation();
            }
        } catch (SQLException ex) {
            callRollbackErrorHandlers(savepoint, ex);
            throw ex;
        }
        callAfterRollbackHandlers(savepoint);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        delegate.releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        logger.beforeDbOperation(PREPARE_EMPTYSTATEMENT, EDbOperationType.PREPARE);
        try {
            return new RadixStatement4ORACLE(this, delegate.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability),logger);
        } finally {
            logger.afterDbOperation();
        }
    }

    @Override
    public RadixPreparedStatement4ORACLE prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        logger.beforeDbOperation(sql, EDbOperationType.PREPARE);
        try {
            return new RadixPreparedStatement4ORACLE(this, delegate.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability), sql, logger);
        } finally {
            logger.afterDbOperation();
        }
    }

    @Override
    public RadixCallableStatement4ORACLE prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        logger.beforeDbOperation(sql, EDbOperationType.PREPARE);
        try {
            return new RadixCallableStatement4ORACLE(this, delegate.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability), sql, logger);
        } finally {
            logger.afterDbOperation();
        }
    }

    @Override
    public RadixPreparedStatement4ORACLE prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        logger.beforeDbOperation(PREPARE_PREFIX + sql);
        try {
            return new RadixPreparedStatement4ORACLE(this, delegate.prepareStatement(sql, autoGeneratedKeys), sql, logger);
        } finally {
            logger.afterDbOperation();
        }
    }

    @Override
    public RadixPreparedStatement4ORACLE prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        logger.beforeDbOperation(sql, EDbOperationType.PREPARE);
        try {
            return new RadixPreparedStatement4ORACLE(this, delegate.prepareStatement(sql, columnIndexes), sql, logger);
        } finally {
            logger.afterDbOperation();
        }
    }

    @Override
    public RadixPreparedStatement4ORACLE prepareStatement(String sql, String[] columnNames) throws SQLException {
        logger.beforeDbOperation(sql, EDbOperationType.PREPARE);
        try {
            return new RadixPreparedStatement4ORACLE(this, delegate.prepareStatement(sql, columnNames), sql, logger);
        } finally {
            logger.afterDbOperation();
        }
    }

    @Override
    public Clob createClob() throws SQLException {
        return objWrapper.wrapClob(delegate.createClob());
    }

    @Override
    public Blob createBlob() throws SQLException {
        return objWrapper.wrapBlob(delegate.createBlob());
    }

    @Override
    public NClob createNClob() throws SQLException {
        return delegate.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return delegate.createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return delegate.isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        delegate.setClientInfo(name, value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        delegate.setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return delegate.getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return delegate.getClientInfo();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return delegate.createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return delegate.createStruct(typeName, attributes);
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        delegate.setSchema(schema);
    }

    @Override
    public String getSchema() throws SQLException {
        return delegate.getSchema();
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        delegate.abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        delegate.setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return delegate.getNetworkTimeout();
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
    public Array createOracleArray(String string, Object o) throws SQLException {
        return delegate.createOracleArray(string, o);
    }

    @Override
    public Long writeEventLogItem(EventLogItemWrapper item) throws SQLException {
        if (qryPut == null) {
            final String sql = "begin ?:=rdx_trace.put_internal(?,?,?,?,?,?,?,?,?,?); end;";
            qryPut = (OracleCallableStatement) prepareCall(sql);
        }
        ((CallableStatement) qryPut).registerOutParameter(1, java.sql.Types.INTEGER);
        qryPut.setString(2, item.getItem().getCode());
        qryPut.setStringForClob(3, item.getWordsStr());//RADIX-4581
        qryPut.setString(4, item.getItem().getSource());
        qryPut.setLong(5, item.getItem().getSeverity().getValue().longValue());
        qryPut.setString(6, item.getItem().getContextTypes());
        qryPut.setString(7, item.getItem().getContextIds());
        qryPut.setTimestamp(8, item.getItem().getTime());
        qryPut.setString(9, item.getItem().getUserName());
        qryPut.setString(10, item.getItem().getStationName());
        qryPut.setLong(11, item.getItem().isSensitive() ? 1 : 0);
        qryPut.execute();
        return ((CallableStatement) qryPut).getLong(1);
    }

    @Override
    public Long writeEventLogItems(List<EventLogItemWrapper> itemList) throws SQLException {
        if (itemList == null || itemList.isEmpty()) {
            return null;
        }
        if (qryBulkPut == null) {
            qryBulkPut = (OracleCallableStatement) prepareCall("begin ?:=rdx_trace.put_records(?); end;");
        }

        if (eventRecordStructDesc == null) {
            eventRecordStructDesc = StructDescriptor.createDescriptor(new SQLName(schema, "RDX_EVENT_LOG_RECORD", (OracleConnection) delegate), delegate);
        }

        if (eventRecordArrDesc == null) {
            eventRecordArrDesc = ArrayDescriptor.createDescriptor(new SQLName(schema, "RDX_EVENT_LOG_RECORDS", (OracleConnection) delegate), delegate);
        }

        final Object[] recordsArray = new Object[itemList.size()];
        int idx = 0;
        for (EventLogItemWrapper itemWrapper : itemList) {
            final EventLogItem item = itemWrapper.getItem();
            final STRUCT recordStruct = new STRUCT(eventRecordStructDesc, this, new Object[]{
                item.getCode(),
                itemWrapper.getWordsStr(),
                item.getSource(),
                item.getSeverity().getValue().longValue(),
                item.getContextTypes(),
                item.getContextIds(),
                item.getTime(),
                item.getUserName(),
                item.getStationName(),
                (item.isSensitive() ? 1 : 0)
            });
            recordsArray[idx] = recordStruct;
            idx++;
        }
        final ARRAY records = new ARRAY(eventRecordArrDesc, this, recordsArray);
        qryBulkPut.setARRAY(2, records);
        ((CallableStatement) qryBulkPut).registerOutParameter(1, java.sql.Types.INTEGER);
        qryBulkPut.execute();
        return ((CallableStatement) qryBulkPut).getLong(1);
    }

    @Override
    public Clob createTemporaryClob() throws SQLException {
        return objWrapper.wrapClob(CLOB.createTemporary(delegate, true, CLOB.DURATION_SESSION));
    }

    @Override
    public Blob createTemporaryBlob() throws SQLException {
        return objWrapper.wrapBlob(BLOB.createTemporary(delegate, true, BLOB.DURATION_SESSION));
    }
    
    @Override
    public boolean isTemporaryClob(final Clob clob) throws SQLException {
        if (clob == null) {
            throw new IllegalArgumentException("Clob to test can't be null");
        }
        else {
            return ((CLOB)clob).isTemporary();
        }
    }

    @Override
    public boolean isTemporaryBlob(Blob blob) throws SQLException {
        if (blob == null) {
            throw new IllegalArgumentException("Blob to test can't be null");
        }
        else {
            return ((BLOB)blob).isTemporary();
        }
    }

    @Override
    public void freeTemporaryClob(Clob clob) throws SQLException {
        if (clob == null) {
            throw new IllegalArgumentException("Clob to free can't be null");
        }
        else {
            ((CLOB)clob).freeTemporary();
        }
    }

    @Override
    public void freeTemporaryBlob(Blob blob) throws SQLException {
        if (blob == null) {
            throw new IllegalArgumentException("Blob to free can't be null");
        }
        else {
            ((BLOB)blob).freeTemporary();
        }
    }
    
    @Override
    public EDatabaseType getDatabaseType() {
        return EDatabaseType.ORACLE;
    }

    @Override
    public String normalizeSQLString(Stmt statement) {
        if (statement == null) {
            throw new IllegalArgumentException("Statement can't be null");
        } else {
            return normalizeSQLString(statement.getText(), statement.getTypes());
        }
    }

    @Override
    public String normalizeSQLString(final String statement, final int... types) {
        if (statement == null) {
            throw new IllegalArgumentException("Statement can't be null");
        } else {
            return statement;
        }
    }

    @Override
    public RadixPreparedStatement prepareStatement(Stmt statement) throws SQLException {
        return prepareStatement(normalizeSQLString(statement));
    }

    @Override
    public CallableStatement prepareCall(Stmt statement) throws SQLException {
        return prepareCall(normalizeSQLString(statement));
    }

    @Override
    public PreparedStatement prepareStatement(Stmt statement, int cursorType, int concurrency) throws SQLException {
        return prepareStatement(normalizeSQLString(statement), cursorType, concurrency);
    }

    @Override
    public IRadixJdbcWrapper getJdbcWrapper() {
        return objWrapper;
    }
    
    private void readSessionInfo() {
        try (PreparedStatement ps = delegate.prepareStatement("select sid, serial# from v$session where audsid = userenv('SESSIONID')")) {
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                sid = rs.getString("sid");
                serial = rs.getString("serial#");
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String getSid() {
        return sid;
    }

    @Override
    public String getSerial() {
        return serial;
    }
    
    @Override
    public String getOperationDescription() {
        return operationDescription;
    }
    
    @Override
    public void setOperationDescription(String operationDescription) {
        this.operationDescription = operationDescription;
    }
    
    static class OracleJdbcWrapper extends RadixJdbcWrapper {

        public OracleJdbcWrapper(DBOperationLoggerInterface logger) {
            super(logger, null);
        }

        @Override
        public Blob wrapBlob(Blob blob) {
            if (blob instanceof BLOB) {
                return new RadixBLOB4Oracle((BLOB)blob, logger);
            }
            return super.wrapBlob(blob);
        }

        @Override
        public Clob wrapClob(Clob clob) {
            if (clob instanceof CLOB) {
                return new RadixCLOB4Oracle((CLOB)clob, logger);
            }
            return super.wrapClob(clob);
        }
        
    }
    
    private static class DbOperationInternalInfo {

        private final DbOperationInfo info;
        private final long startMillis;

        public DbOperationInternalInfo(DbOperationInfo info, long startMillis) {
            this.info = info;
            this.startMillis = startMillis;
        }

        public DbOperationInfo getInfo() {
            return info;
        }

        public long getStartMillis() {
            return startMillis;
        }

        public boolean suppressForciblyCloseErrors() {
            if (info != null && info.getArte() != null) {
                return info.getArte().suppressDbForciblyCloseErrors();
            }
            return false;
        }
    }

    public static class DbOperationInfo {

        private final ARTEReducedInterface arte;
        private final boolean readOnly;

        public DbOperationInfo(final ARTEReducedInterface arte, final boolean readOnly) {
            this.readOnly = readOnly;
            this.arte = arte;
        }

        public ARTEReducedInterface getArte() {
            return arte;
        }

        public boolean isReadOnly() {
            return readOnly;
        }
    }
}
