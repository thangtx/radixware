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
package org.radixware.kernel.server.instance;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.logging.LogFactory;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.trace.LocalTracerThreadGroup;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.Pair;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.server.dbq.OraExCodes;
import org.radixware.kernel.server.instance.arte.ArteInstance;
import static org.radixware.kernel.server.instance.arte.ArteInstance.EState.BUSY;
import static org.radixware.kernel.server.instance.arte.ArteInstance.EState.FREE;
import static org.radixware.kernel.server.instance.arte.ArteInstance.EState.INIT;
import org.radixware.kernel.server.instance.arte.IArteRequest;
import org.radixware.kernel.server.jdbc.DelegateDbQueries;
import org.radixware.kernel.server.jdbc.Stmt;
import org.radixware.kernel.server.jdbc.IDbQueries;
import org.radixware.kernel.server.jdbc.RadixConnection;
import org.radixware.kernel.server.jdbc.RadixPreparedStatement;
import org.radixware.kernel.server.trace.IServerThread;

public class ArteStateWriter extends Thread {
    private static final int VALIDATE_DB_CONNECTION_TIMEOUT_SEC = 5;
    private static final int VALIDATE_DB_CONNECTION_PERIOD_MILLIS = 10000;
    private final Instance instance;
    private final IDbQueries delegate = new DelegateDbQueries(this, null);

    private volatile boolean stopRequested = false;
    private Connection dbConnection;
    
    private static final String clearStStmtSQL = "DELETE FROM RDX_ARTEINSTANCE WHERE INSTANCEID=?";
    private static final Stmt clearStStmt = new Stmt(clearStStmtSQL,Types.BIGINT);
    private PreparedStatement clearSt;
    
    private static final String insertStStmtSQL = "INSERT INTO RDX_ARTEINSTANCE (INSTANCEID, SERIAL, SEQ, STATE, UNITID, SID, BUSYTIMEMILLIS, LIFEMILLIS) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final Stmt insertStStmt = new Stmt(insertStStmtSQL,Types.BIGINT,Types.BIGINT,Types.BIGINT,Types.VARCHAR,Types.BIGINT,Types.BIGINT,Types.BIGINT,Types.BIGINT);
    private PreparedStatement insertSt;
    
    private long threadsStateGatherPeriodMillis = 0;
    private long threadsStateForcedGatherPeriodMillis = 0;
    private boolean forceIterationAfterSettingsChange = false;
    
    private final Object curHandleSem = new Object();
    private long lastValidateDbConnection = 0;
    private UpdateRequestHandle curHandle;

    private ArteStateWriter() {
        this.instance = null;
    }
    
    public ArteStateWriter(Instance instance) {
        this.instance = instance;
        setDaemon(true);
        setName("ArteStateWriter");
        try {
            openDbConnection();
        } catch (SQLException ex) {
            LogFactory.getLog(getClass()).debug("Error on db connection opening", ex);
        }
    }

    private void openDbConnection() throws SQLException {
        dbConnection = instance.openNewDbConnection(getName(), null);
    }

    private void closeDbConnection() {
        if (dbConnection != null) {
            try {
                dbConnection.close();
            } catch (SQLException ex) {
                LogFactory.getLog(getClass()).debug("Error on db connection closing", ex);
            } finally {
                dbConnection = null;
                clearSt = null;
                insertSt = null;
                insertInstanceStateHistory = null;
                insertThreadStack = null;
                updateThreadStackUsageTime = null;
            }
        }
    }

    private void checkDbConnection() throws SQLException {
        final long curMillis = System.currentTimeMillis();
        if (dbConnection == null || dbConnection.isClosed()) {
            closeDbConnection();
            openDbConnection();
        } else if (curMillis - lastValidateDbConnection >= VALIDATE_DB_CONNECTION_PERIOD_MILLIS) {
            if (!dbConnection.isValid(VALIDATE_DB_CONNECTION_TIMEOUT_SEC)) {
                closeDbConnection();
                openDbConnection();
            }
            lastValidateDbConnection = curMillis;
        }
    }

    public UpdateRequestHandle requestUpdate() {
        synchronized (curHandleSem) {
            if (curHandle == null) {
                curHandle = new UpdateRequestHandle();
                curHandleSem.notifyAll();
            }
            return curHandle;
        }
    }

    @Override
    public void run() {
        try {
            while (!stopRequested && !Thread.currentThread().isInterrupted()) {
                try {
                    final UpdateRequestHandle handle;
                    synchronized (curHandleSem) {
                        if (curHandle != null) {
                            handle = curHandle;
                        } else {
                            curHandleSem.wait();
                            handle = curHandle;
                        }
                    }
                    if (handle != null) {
                        handle.setResult(doFillArteTable());
                        gatherAndStoreThreadsState();
                    }
                } catch (InterruptedException ex) {
                    return;
                } finally {
                    synchronized (curHandleSem) {
                        curHandle = null;
                    }
                }
            }
        } finally {
            closeDbConnection();
        }
    }

    public void requestStop() {
        stopRequested = true;
        interrupt();
    }

    private boolean doFillArteTable() {
        try {
            checkDbConnection();
            fillArtePoolTable(dbConnection);
            return true;
        } catch (Exception ex) {
            instance.getTrace().putFloodControlled("ArteStateWriterSqlEx",
                    EEventSeverity.WARNING,
                    "Error on updating ARTE pool state: " + ExceptionTextFormatter.throwableToString(ex),
                    null, null, EEventSource.INSTANCE.getValue(), -1, false, null);
            if (dbConnection != null) {
                try {
                    dbConnection.rollback();
                } catch (Exception ex1) {
                    LogFactory.getLog(getClass()).error("Error on ArteStateWriter rollback", ex1);
                }
            }
        }
        return false;
    }

    private void fillArtePoolTable(final Connection connection) throws SQLException {
        if (connection == null) {
            throw new SQLException("Connection is null");
        }

        if (clearSt == null) {
            clearSt = ((RadixConnection)connection).prepareStatement(clearStStmt);
        }
        clearSt.setLong(1, instance.getId());
        clearSt.execute();

        if (insertSt == null) {
            insertSt = ((RadixConnection)connection).prepareStatement(insertStStmt);
            ((RadixPreparedStatement)insertSt).setExecuteBatch(200);
        }
        final long curTime = System.currentTimeMillis();
        for (ArteInstance arteInst : instance.getArtePool().getInstances(false)) {
            insertSt.setLong(1, instance.getId());
            insertSt.setLong(2, arteInst.getSerial());
            insertSt.setLong(3, arteInst.getSeqNumber());

            final Long sid = arteInst.getDbSessionId();
            if (sid != null) {
                insertSt.setLong(6, sid);
            } else {
                insertSt.setNull(6, java.sql.Types.INTEGER);
            }

            //Default values. May be changed if state = BUSY.
            insertSt.setNull(5, java.sql.Types.INTEGER);
            insertSt.setNull(7, java.sql.Types.INTEGER);

            String arteState = "";
            switch (arteInst.getState()) {
                case FREE:
                    arteState = ArteInstance.EState.FREE.name();
                    break;
                case BUSY:
                    final IArteRequest rq = arteInst.getRequest();
                    if (rq != null) {
                        arteState = ArteInstance.EState.BUSY.name();
                        insertSt.setLong(5, rq.getUnit().getId());
                        insertSt.setLong(7, System.currentTimeMillis() - rq.getCreateTimeMillis());
                    }
                    break;
                case INIT:
                    arteState = ArteInstance.EState.INIT.name();
                    break;
                default:
                    throw new IllegalStateException("Not defined state of ARTE: " + arteInst.getState());
            }
            insertSt.setString(4, arteState);
            insertSt.setLong(8, curTime - arteInst.getStartTime());

            insertSt.execute();
        }
        ((RadixPreparedStatement)insertSt).sendBatch();
        connection.commit();
    }
    
    public static class UpdateRequestHandle {

        private boolean result;
        private final CountDownLatch latch = new CountDownLatch(1);

        public boolean await(final long timeoutMillis) throws InterruptedException {
            if (latch.await(timeoutMillis, TimeUnit.MILLISECONDS)) {
                return result;
            } else {
                return false;
            }
        }

        protected void setResult(final boolean result) {
            this.result = result;
            latch.countDown();
        }
    }
    
    // Instance Threads State History - state record related fields, methods, classes
    
    private final class ThreadStateInfo {

        private final Thread thread;
        private final long threadId;
        private final ArrayBlockingQueue<InstanceThreadStateRecord> records = new ArrayBlockingQueue<>(100);
        AtomicLong lastRegTimeMillis = new AtomicLong(System.currentTimeMillis());

        private ThreadStateInfo(final Thread thread) {
            this.thread = thread;
            this.threadId = thread.getId();
        }

        private InstanceThreadStateRecord createRecord(ThreadInfo threadInfo) {
            final InstanceThreadStateRecord record = InstanceThreadStateRecord.create(thread, threadInfo);
            return record;
        }

        private void register(InstanceThreadStateRecord record) {
            if (record != null) {
                records.offer(record);
            }
        }

        private long getActualLastRegTimeMillis(long now, long boundRegTimeMillis, long otherLastRegTimeMillis) {
            long actualLastRegTimeMillis = lastRegTimeMillis.get();
            actualLastRegTimeMillis = Math.max(actualLastRegTimeMillis, otherLastRegTimeMillis);
            if (actualLastRegTimeMillis <= boundRegTimeMillis) {
                actualLastRegTimeMillis = lastRegTimeMillis.getAndSet(now);
            }
            return actualLastRegTimeMillis;
        }
        
        private void forciblyRegisterIfRequired(long now, long boundRegTimeMillis, ThreadInfo threadInfo) {
            final long actualRegTimeMillis = getActualLastRegTimeMillis(now, boundRegTimeMillis, Long.MIN_VALUE);
            if (actualRegTimeMillis <= boundRegTimeMillis) {
                register(createRecord(threadInfo));
            }
        }
    }

    private final ConcurrentHashMap<Long, ThreadStateInfo> threadsInfo = new ConcurrentHashMap<>(1000);
    
    public static ThreadLocal<ThreadStateSelfRegistrator> nonIServerThreadRegistrators = new ThreadLocal<ThreadStateSelfRegistrator>() {
        @Override
        protected ThreadStateSelfRegistrator initialValue() {
            return new ThreadStateSelfRegistrator(Thread.currentThread());
        }
    };
    
    private static final String insertInstanceStateHistoryStmtSQL = "insert into RDX_SM_INSTANCESTATEHISTORY (instanceId, regTimeMillis, threadId, threadKind, forced, name, ancestorThreadId, unitId, arteSeq, arteSerial, dbSid, dbSerial, traceContexts, cpuDiffMillis, dbDiffMillis, extDiffMillis, queueDiffMillis, uptimeSec, stackDigest, lockName, lockOwnerName, rqStartTimeMillis, extData) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,  ?, ?, ?)";
    private static final Stmt insertInstanceStateHistoryStmt = new Stmt(insertInstanceStateHistoryStmtSQL, Types.BIGINT, Types.BIGINT, Types.BIGINT, Types.VARCHAR, Types.BOOLEAN, Types.VARCHAR, Types.BIGINT, Types.BIGINT, Types.BIGINT, Types.BIGINT, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.BIGINT, Types.BIGINT, Types.BIGINT, Types.BIGINT, Types.BIGINT, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.BIGINT, Types.VARCHAR);
    private RadixPreparedStatement insertInstanceStateHistory;
    
    private List<InstanceThreadStateRecord> instanceThreadStateRecords = new ArrayList<>();
    private long lastGatherAndStoreThreadsStateTimeMillis = System.currentTimeMillis();
    private long lastForciblyGatherThreadsStateTimeMillis = System.currentTimeMillis();
    
    private void gatherAndStoreThreadsState() {
        try {
            threadsStateGatherPeriodMillis = instance.getThreadsStateGatherPeriodSec() * 1000L;
            threadsStateForcedGatherPeriodMillis = instance.getThreadsStateForcedGatherPeriodSec() * 1000L;
            threadsStateGatherPeriodMillis = threadsStateGatherPeriodMillis != 0 ? threadsStateGatherPeriodMillis : Long.MAX_VALUE;
            threadsStateForcedGatherPeriodMillis = threadsStateForcedGatherPeriodMillis != 0 ? threadsStateForcedGatherPeriodMillis : Long.MAX_VALUE;
            final long minPeriodMillis = Math.min(threadsStateGatherPeriodMillis, threadsStateForcedGatherPeriodMillis);
            forceIterationAfterSettingsChange = minPeriodMillis == Long.MAX_VALUE &&
                    (!newStacks.isEmpty() || !refreshStackUsageTimeMillis.isEmpty() || !threadsInfo.isEmpty() || !lastStackUsageTimeMillis.isEmpty());
            if (!forceIterationAfterSettingsChange && System.currentTimeMillis() - lastGatherAndStoreThreadsStateTimeMillis < minPeriodMillis) {
                return;
            }
            
            forciblyGatherThreadsState(); // from instance options
            
            prepareInstanceThreadsStateRecords();
            storeNewStacks(); // immediately
            storeInstanceThreadsStateInDb(); // immediately
            dbConnection.commit();
            
            storeLastStackUsageTime(); // hourly
            removeTerminatedThreadsInfo(); // immediately
            cleanupLastStackUsageTime(); // daily
            lastGatherAndStoreThreadsStateTimeMillis = System.currentTimeMillis();
        } catch (Throwable ex) {
            instance.getTrace().putFloodControlled("ArteStateWriterSqlEx_ThreadsState",
                    EEventSeverity.WARNING,
                    "Error on gathering instance threads state: " + ExceptionTextFormatter.throwableToString(ex),
                    null, null, EEventSource.INSTANCE.getValue(), -1, false, null);
            if (dbConnection != null) {
                try {
                    dbConnection.rollback();
                } catch (Exception ex1) {
                    LogFactory.getLog(getClass()).error("Error on gathering instance threads state", ex1);
                }
            }
        }
    }

    private void forciblyGatherThreadsState() {
        if (System.currentTimeMillis() - lastForciblyGatherThreadsStateTimeMillis < threadsStateForcedGatherPeriodMillis) {
            return;
        }

        final Thread[] threads = LocalTracerThreadGroup.findLocalTracerThreadGroup().getRootLocalTracerThreadGroup().getAllThreads();
        final long[] threadIds = new long[threads.length];
        for (int i = 0; i < threads.length; ++i) {
            threadIds[i] = threads[i].getId();
        }
        
        final ThreadMXBean mxb = ManagementFactory.getThreadMXBean();
        final ThreadInfo[] threadInfos = mxb.getThreadInfo(threadIds, 512);
        
        for (int i = 0; i < threads.length; ++i) {
            if (threadInfos[i] != null && threadInfos[i].getThreadState() != State.TERMINATED) {
                forciblyGatherTreadState(threads[i], threadInfos[i]);
            }
        }
        
        lastForciblyGatherThreadsStateTimeMillis = System.currentTimeMillis();
    }
    
    private void forciblyGatherTreadState(Thread thread, ThreadInfo threadInfo) {
        final ThreadStateInfo tsi = getThreadStateInfo(thread);
        final long now = System.currentTimeMillis();
        final long boundTimeMillis = now - threadsStateForcedGatherPeriodMillis;
        tsi.forciblyRegisterIfRequired(now, boundTimeMillis, threadInfo);
    }
    
    private void prepareInstanceThreadsStateRecords() {
        for (ThreadStateInfo tsi : threadsInfo.values()) {
            tsi.records.drainTo(instanceThreadStateRecords);
        }
        for (InstanceThreadStateRecord rec : instanceThreadStateRecords) {
            preprocessStack(rec.stackDigestSha1, rec.stackTop, rec.stack);
        }
    }

    private void storeInstanceThreadsStateInDb() throws SQLException {
        if (instanceThreadStateRecords.isEmpty()) {
            return;
        }
        
        checkDbConnection();
        if (insertInstanceStateHistory == null) {
            insertInstanceStateHistory = ((RadixConnection) dbConnection).prepareStatement(insertInstanceStateHistoryStmt);
            insertInstanceStateHistory.setExecuteBatch(200);
        }
        
        final String digestPrefix = getAadcMemberIdAsStr();
        for (InstanceThreadStateRecord rec: instanceThreadStateRecords) {
            preprocessStack(rec.stackDigestSha1, rec.stackTop, rec.stack);
            rec.fillInsertStatementParams(insertInstanceStateHistory, digestPrefix);
            insertInstanceStateHistory.execute();
        }
        insertInstanceStateHistory.sendBatch();
        dbConnection.commit();
        instanceThreadStateRecords.clear();
    }

    private void removeTerminatedThreadsInfo() {
        for (ThreadStateInfo info : threadsInfo.values()) {
            if (info.thread.getState() == State.TERMINATED) {
                threadsInfo.remove(info.threadId);
            }
        }
    }
    
    public static void gatherCurrentThreadState() {
        final Thread thread = Thread.currentThread();
        final ThreadStateSelfRegistrator registrator = thread instanceof IServerThread
                ? ((IServerThread) thread).getThreadStateSelfRegistrator()
                : nonIServerThreadRegistrators.get();
        registrator.selfRegisterIfRequired();
    }
    
    private ThreadStateInfo getThreadStateInfo(Thread thread) {
        final long threadId = thread.getId();
        ThreadStateInfo threadInfo = threadsInfo.get(threadId);
        if (threadInfo == null) {
            threadsInfo.putIfAbsent(threadId, new ThreadStateInfo(thread));
            threadInfo = threadsInfo.get(threadId);
        }
        return threadInfo;
    }
    
    public long getActualLastRegTimeMillis(long now, long boundRegTimeMillis, long lastRegTimeMillis, Thread thread) {
        ThreadStateInfo tsi = getThreadStateInfo(thread);
        return tsi.getActualLastRegTimeMillis(now, boundRegTimeMillis, lastRegTimeMillis);
    }

    public void registerRecord(long threadId, InstanceThreadStateRecord record) {
        ThreadStateInfo tsi = threadsInfo.get(threadId);
        tsi.register(record);
    }
    
    // Instance State History - stack related methods
    private static final boolean useMergeForStackDataInsert = SystemPropUtils.getBooleanSystemProp("rdx.instance.thread.state.stack.data.insert.use.merge", false);
    
    private static final String mergeThreadStackStmtSQL = "merge into RDX_SM_StackData using dual on (digest = ?) \n"
            + "when not matched then insert (digest, stackTop, compressedStack, lastUsageTimeMillis) values (?, ?, ?, ?)";
    private static final Stmt mergeThreadStackStmt = new Stmt(mergeThreadStackStmtSQL, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.BLOB, Types.BIGINT);
    private static final String insertThreadStackStmtSQL = "insert into RDX_SM_StackData (digest, stackTop, compressedStack, lastUsageTimeMillis) values (?, ?, ?, ?)";
    private static final Stmt insertThreadStackStmt = new Stmt(insertThreadStackStmtSQL, Types.VARCHAR, Types.VARCHAR, Types.BLOB, Types.BIGINT);
    private RadixPreparedStatement insertThreadStack;

    private static final String updateThreadStackUsageTimeStmtSQL = "update RDX_SM_StackData set lastUsageTimeMillis = ? where digest = ?";
    private static final Stmt updateThreadStackUsageTimeStmt = new Stmt(updateThreadStackUsageTimeStmtSQL, Types.BIGINT, Types.VARCHAR);
    private RadixPreparedStatement updateThreadStackUsageTime;
    
    private static final long STACK_USAGE_TIME_UPDATE_INTERVAL_MILLIS = 3600000L; // hour should be fine
    private static final long STACK_USAGE_TIME_CLEANUP_INTERVAL_MILLIS = 86400000L; // day should be fine
    private static final long STACK_USAGE_TIME_OBSOLETED_INTERVAL_MILLIS = 86400000L; // day enough?

    private final HashMap<String, Long> lastStackUsageTimeMillis = new HashMap<>(10000);
    private final HashMap<String, Long> refreshStackUsageTimeMillis = new HashMap<>(10000);
    private final HashMap<String, Pair<String, String>> newStacks = new HashMap<>(1000); // digest -> (stackTop, stack)

    private long lastStackUsageTimeUpdateMillis = System.currentTimeMillis();
    private long lastStackUsageCleanupTimeMillis = System.currentTimeMillis();
    
    private void preprocessStack(String digest, String stackTop, String stack) {
        if (lastStackUsageTimeMillis.put(digest, System.currentTimeMillis()) == null) {
            newStacks.put(digest, new Pair<>(stackTop, stack));
        } else {
            refreshStackUsageTimeMillis.put(digest, System.currentTimeMillis());
        }
    }
    
    private void storeStack(String digest, String stackTop, byte[] compressedStack, long lastUsageTimeMillis) throws SQLException {
        int idx = 1;
        if (useMergeForStackDataInsert) {
            insertThreadStack.setString(idx++, digest);
        }
        insertThreadStack.setString(idx++, digest);
        insertThreadStack.setString(idx++, stackTop);
        insertThreadStack.setBytes(idx++, compressedStack);
        insertThreadStack.setLong(idx++, lastUsageTimeMillis);
        
        try {
            insertThreadStack.execute();
        } catch (SQLException ex) {
            if (ex.getErrorCode() != OraExCodes.UNIQUE_CONSTRAINT_VIOLATED) {
                throw ex;
            }
        }
        dbConnection.commit();
    }
    
    private void storeNewStacks() throws SQLException {
        if (newStacks.isEmpty()) {
            return;
        }
        
        checkDbConnection();
        if (insertThreadStack == null) {
            final Stmt actualStmt = useMergeForStackDataInsert ? mergeThreadStackStmt : insertThreadStackStmt;
            insertThreadStack = ((RadixConnection)dbConnection).prepareStatement(actualStmt);
        }
        
        final String digestPrefix = getAadcMemberIdAsStr();
        for (Map.Entry<String, Pair<String,String>> newStackEntry: newStacks.entrySet()) {
            final String digest = newStackEntry.getKey();
            final String stackTop = newStackEntry.getValue().getFirst();
            final String stack = newStackEntry.getValue().getSecond();
            final byte[] compressedStack = Utils.compressString(stack);
            final long lastUsageTimeMillis = lastStackUsageTimeMillis.get(digest);
            storeStack(digestPrefix + digest, stackTop, compressedStack, lastUsageTimeMillis);
        }
        
        newStacks.clear();
    }
    
    private void storeLastStackUsageTime() throws SQLException {
        if (!forceIterationAfterSettingsChange && System.currentTimeMillis() < lastStackUsageTimeUpdateMillis + STACK_USAGE_TIME_UPDATE_INTERVAL_MILLIS) {
            return;
        }
        
        checkDbConnection();
        if (updateThreadStackUsageTime == null) {
            updateThreadStackUsageTime = ((RadixConnection)dbConnection).prepareStatement(updateThreadStackUsageTimeStmt);
            updateThreadStackUsageTime.setExecuteBatch(200);
        }
        final String digestPrefix = getAadcMemberIdAsStr();
        for (Map.Entry<String, Long> entry: refreshStackUsageTimeMillis.entrySet()) {
            final String digest = entry.getKey();
            final Long lastUsageTimeMillis = entry.getValue();
            updateThreadStackUsageTime.setLong(1, lastUsageTimeMillis);
            updateThreadStackUsageTime.setString(2, digestPrefix + digest);
            updateThreadStackUsageTime.execute();
        }
        updateThreadStackUsageTime.sendBatch();
        dbConnection.commit();
        refreshStackUsageTimeMillis.clear();
        lastStackUsageTimeUpdateMillis = System.currentTimeMillis();
    }
    
    private void cleanupLastStackUsageTime() {
        if (!forceIterationAfterSettingsChange && System.currentTimeMillis() < lastStackUsageCleanupTimeMillis + STACK_USAGE_TIME_CLEANUP_INTERVAL_MILLIS) {
            return;
        }
        lastStackUsageCleanupTimeMillis = System.currentTimeMillis();

        final Set<Map.Entry<String, Long>> entries = new HashSet<>(lastStackUsageTimeMillis.entrySet());
        for (Map.Entry<String, Long> entry : entries) {
            if (entry.getValue() + STACK_USAGE_TIME_OBSOLETED_INTERVAL_MILLIS < System.currentTimeMillis()) {
                lastStackUsageTimeMillis.remove(entry.getKey());
            }
        }
    }
    
    private String getAadcMemberIdAsStr() {
        String result = "";
        if (instance.getAadcManager() != null) {
            final int memberId = instance.getAadcManager().getMemberId();
            result = (memberId == 1 ? "1" : (memberId == 2 ? "2" : "") );
        }
        return result;
    }
}
