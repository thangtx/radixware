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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.NClob;
import java.sql.PreparedStatement;
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
import oracle.sql.BINARY_DOUBLE;
import oracle.sql.BINARY_FLOAT;
import oracle.sql.DATE;
import oracle.sql.INTERVALDS;
import oracle.sql.INTERVALYM;
import oracle.sql.NUMBER;
import oracle.sql.TIMESTAMP;
import oracle.sql.TIMESTAMPLTZ;
import oracle.sql.TIMESTAMPTZ;
import oracle.sql.TypeDescriptor;
import org.apache.commons.logging.LogFactory;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.dbq.DbQuery;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.instance.ObjectCache;

public class RadixConnection implements OracleConnection {

    private static final long DELAY_BEFORE_FIRST_DISCONNECT_CHECK_MILLIS = 10000;
    private static final String PREPARE_PREFIX = "prepare ";
    private static final String WITH_KEY_PREFIX = "withKey ";
    //
    private final OracleConnection delegate;
    private final DbOperationLogger logger = new DbOperationLogger(this);
    private final Object curOperationSem = new Object();
    //guarded by curOperationSem
    private DbOperationInternalInfo curOperationInternalInfo = null;
    //guarded by curOperationSem
    private boolean forciblyClosed = false;
    private String closeReason;
    private String sqlWhenForciblyClosed;
    //NotThreadSafe
    private boolean wereWriteOperations;
    private boolean dbLoggingAllowed = true;
    private final ObjectCache objectCache = new ObjectCache();
    //@GuardedBy statementsToClose
    private final List<PreparedStatement> statementsToClose = new ArrayList<>();

    public RadixConnection(final OracleConnection delegate) {
        this.delegate = delegate;
    }

    /**
     *
     * @return previous value
     */
    public boolean setDbLoggingAllowed(boolean dbLoggingAllowed) {
        final boolean oldVal = this.dbLoggingAllowed;
        this.dbLoggingAllowed = dbLoggingAllowed;
        return oldVal;
    }

    public boolean isDbLoggingAllowed() {
        return dbLoggingAllowed;
    }

    public int closeSchedulledStatements() {
        final List<PreparedStatement> snapshot = new ArrayList<>();
        synchronized (statementsToClose) {
            snapshot.addAll(statementsToClose);
            statementsToClose.clear();
        }

        for (PreparedStatement statement : snapshot) {
            try {
                statement.close();
                if (DbQuery.LOG_CLOSE_FROM_NON_ARTE_THREAD) {
                    final Instance instance = Instance.get();
                    instance.getTrace().put(EEventSeverity.WARNING, Thread.currentThread().getName() + " closed statement " + statement, EEventSource.INSTANCE);
                }
            } catch (Exception ex) {
                LogFactory.getLog(getClass()).warn("Error while closing statement schedulled for close", ex);
            }
        }
        return snapshot.size();

    }

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
                final String clientDescr = curOperationInternalInfo.getInfo().getArte().getRequest() == null ? "<null>" : curOperationInternalInfo.getInfo().getArte().getRequest().getClientId();
                return forciblyClose("client '" + clientDescr + "' disconnected");
            }
        }
        return false;
    }

    public boolean forciblyClose(final String closeReason) throws SQLException {
        synchronized (curOperationSem) {
            final String curSql = logger.getExecutingSql();
            try {
                cancel();
            } catch (Exception ex) {
                curOperationInternalInfo.getInfo().getArte().getInstance().getTrace().put(EEventSeverity.WARNING, "Error while trying to cancel current db operation on hanging " + curOperationInternalInfo.getInfo().getArte().getProcessorThread().getName() + ": " + ExceptionTextFormatter.exceptionStackToString(ex), EEventSource.INSTANCE);
            }
            try {
                rollback();
            } catch (Exception ex) {
                curOperationInternalInfo.getInfo().getArte().getInstance().getTrace().put(EEventSeverity.WARNING, "Error while trying to rollback current db transaction on hanging " + curOperationInternalInfo.getInfo().getArte().getProcessorThread().getName() + ": " + ExceptionTextFormatter.exceptionStackToString(ex), EEventSource.INSTANCE);
            }
            try {
                close();
                forciblyClosed = true;
                this.closeReason = closeReason;
                sqlWhenForciblyClosed = curSql;
                return true;
            } catch (Exception ex) {
                curOperationInternalInfo.getInfo().getArte().getInstance().getTrace().put(EEventSeverity.WARNING, "Error while trying to close db connection for hanging " + curOperationInternalInfo.getInfo().getArte().getProcessorThread().getName() + ": " + ExceptionTextFormatter.exceptionStackToString(ex), EEventSource.INSTANCE);
            }
        }
        return false;
    }

    public boolean isForciblyClosed() {
        synchronized (curOperationSem) {
            return forciblyClosed;
        }
    }

    public String getForcedCloseReason() {
        synchronized (curOperationSem) {
            return closeReason;
        }
    }

    public String getSqlWhenForciblyClosed() {
        synchronized (curOperationSem) {
            return sqlWhenForciblyClosed;
        }
    }

    protected void beforeDbOperation(final DbOperationInfo operationInfo) {
        if (operationInfo != null) {
            if (!operationInfo.isReadOnly()) {
                wereWriteOperations = true;
            }
            synchronized (curOperationSem) {
                if (forciblyClosed) {
                    throw new DatabaseError(getClosedMessage(), null);
                }
                curOperationInternalInfo = new DbOperationInternalInfo(operationInfo, System.currentTimeMillis());
            }
        }
    }

    protected void afterDbOperation(final DbOperationInfo operationInfo, boolean wasException) {
        synchronized (curOperationSem) {
            curOperationInternalInfo = null;
            if (forciblyClosed && !wasException) {
                throw new DatabaseError(getClosedMessage(), null);
            }
        }
    }

    private String getClosedMessage() {
        return "Database connection was forcibly closed" + (closeReason == null ? "" : " (" + closeReason + ")");
    }

    public boolean wereWriteOperations() {
        return wereWriteOperations;
    }

    private void afterCommitOrRollbackNoExceptions() {
        wereWriteOperations = false;
    }

    @Override
    public void commit(final EnumSet<CommitOption> es) throws SQLException {
        logger.beforeDbOperation("commit with options");
        try {
            delegate.commit(es);
        } finally {
            afterCommitOrRollbackNoExceptions();
            logger.afterDbOperation();
        }
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
    public RadixPreparedStatement getStatementWithKey(String string) throws SQLException {
        return new RadixPreparedStatement(this, (OraclePreparedStatement) delegate.getStatementWithKey(string), WITH_KEY_PREFIX + string);
    }

    @Override
    public RadixCallableStatement getCallWithKey(String string) throws SQLException {
        return new RadixCallableStatement(this, (OracleCallableStatement) delegate.getCallWithKey(string), WITH_KEY_PREFIX + string);
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
    public RadixPreparedStatement prepareStatementWithKey(String string) throws SQLException {
        return new RadixPreparedStatement(this, (OraclePreparedStatement) delegate.prepareStatementWithKey(string), WITH_KEY_PREFIX + string);
    }

    @Override
    public RadixCallableStatement prepareCallWithKey(String string) throws SQLException {
        return new RadixCallableStatement(this, (RadixCallableStatement) delegate.prepareCallWithKey(string), WITH_KEY_PREFIX + string);
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
        logger.beforeDbOperation("ora rollback sp");
        try {
            delegate.oracleRollback(os);
        } finally {
            afterCommitOrRollbackNoExceptions();
            logger.afterDbOperation();
        }
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
        return delegate.createStatement();
    }

    @Override
    public RadixPreparedStatement prepareStatement(String sql) throws SQLException {
        logger.beforeDbOperation(PREPARE_PREFIX + sql);
        try {
            return new RadixPreparedStatement(this, (OraclePreparedStatement) delegate.prepareStatement(sql), sql);
        } finally {
            logger.afterDbOperation();
        }
    }

    @Override
    public RadixCallableStatement prepareCall(String sql) throws SQLException {
        logger.beforeDbOperation(PREPARE_PREFIX + sql);
        try {
            return new RadixCallableStatement(this, (OracleCallableStatement) delegate.prepareCall(sql), sql);
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
        logger.beforeDbOperation("Commit");
        try {
            delegate.commit();
        } finally {
            afterCommitOrRollbackNoExceptions();
            logger.afterDbOperation();
        }
        objectCache.maintenance();
    }

    @Override
    public void rollback() throws SQLException {
        logger.beforeDbOperation("rollback");
        try {
            delegate.rollback();
        } finally {
            afterCommitOrRollbackNoExceptions();
            logger.afterDbOperation();
        }
    }

    public ObjectCache getObjectCache() {
        return objectCache;
    }

    @Override
    public void close() throws SQLException {
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
        Statement result = delegate.createStatement(resultSetType, resultSetConcurrency);
        if (result instanceof OracleCallableStatement) {
            return new RadixCallableStatement(this, (OracleCallableStatement) result, null);
        } else {
            return new RadixPreparedStatement(this, (OraclePreparedStatement) result, null);
        }
    }

    @Override
    public RadixPreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        logger.beforeDbOperation(PREPARE_PREFIX + sql);
        try {
            return new RadixPreparedStatement(this, (OraclePreparedStatement) delegate.prepareStatement(sql, resultSetType, resultSetConcurrency), sql);
        } finally {
            logger.afterDbOperation();
        }

    }

    @Override
    public RadixCallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        logger.beforeDbOperation(PREPARE_PREFIX + sql);
        try {
            return new RadixCallableStatement(this, (OracleCallableStatement) delegate.prepareCall(sql, resultSetType, resultSetConcurrency), sql);
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
        logger.beforeDbOperation("rollback sp");
        try {
            delegate.rollback(savepoint);
        } finally {
            afterCommitOrRollbackNoExceptions();
            logger.afterDbOperation();
        }
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        delegate.releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return delegate.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public RadixPreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        logger.beforeDbOperation(PREPARE_PREFIX + sql);
        try {
            return new RadixPreparedStatement(this, (OraclePreparedStatement) delegate.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability), sql);
        } finally {
            logger.afterDbOperation();
        }
    }

    @Override
    public RadixCallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        logger.beforeDbOperation(PREPARE_PREFIX + sql);
        try {
            return new RadixCallableStatement(this, (OracleCallableStatement) delegate.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability), sql);
        } finally {
            logger.afterDbOperation();
        }
    }

    @Override
    public RadixPreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        logger.beforeDbOperation(PREPARE_PREFIX + sql);
        try {
            return new RadixPreparedStatement(this, (OraclePreparedStatement) delegate.prepareStatement(sql, autoGeneratedKeys), sql);
        } finally {
            logger.afterDbOperation();
        }
    }

    @Override
    public RadixPreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        logger.beforeDbOperation(PREPARE_PREFIX + sql);
        try {
            return new RadixPreparedStatement(this, (OraclePreparedStatement) delegate.prepareStatement(sql, columnIndexes), sql);
        } finally {
            logger.afterDbOperation();
        }
    }

    @Override
    public RadixPreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        logger.beforeDbOperation(PREPARE_PREFIX + sql);
        try {
            return new RadixPreparedStatement(this, (OraclePreparedStatement) delegate.prepareStatement(sql, columnNames), sql);
        } finally {
            logger.afterDbOperation();
        }
    }

    @Override
    public Clob createClob() throws SQLException {
        return delegate.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return delegate.createBlob();
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
    }

    public static class DbOperationInfo {

        private final Arte arte;
        private final boolean readOnly;

        public DbOperationInfo(final Arte arte, final boolean readOnly) {
            this.readOnly = readOnly;
            this.arte = arte;
        }

        public Arte getArte() {
            return arte;
        }

        public boolean isReadOnly() {
            return readOnly;
        }
    }
}
