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
package org.radixware.kernel.server.instance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.jdbc.DelegateDbQueries;
import org.radixware.kernel.server.jdbc.Stmt;
import org.radixware.kernel.server.jdbc.IDbQueries;
import org.radixware.kernel.server.jdbc.RadixConnection;

public class JobCheckTimeUpdater {

    private static final String THREAD_NAME = "JobCheckTimeUpdater";
    public static final int UPDATE_INTERVAL_SECONDS = 5;
    private static final long DB_PING_INTERVAL = 30000;
    private final Map<Arte, JobInfo> arte2job = new WeakHashMap<>();
    private final Instance instance;
    private volatile UpdaterThread updaterThread;

    private JobCheckTimeUpdater() { // Only for IDbQuery testing purposes!
        this.instance = null;
        this.updaterThread = new UpdaterThread();
    }
    
    public JobCheckTimeUpdater(Instance instance) {
        this.instance = instance;
    }

    public void register(Arte arte, long jobId, Long taskId) {
        if (!Objects.equals(arte.getJobQueue().getCurrentJobId(), jobId)) {
            throw new IllegalArgumentException("Arte.currentJobId is not " + jobId + ", but " + arte.getJobQueue().getCurrentJobId());
        }
        synchronized (arte2job) {
            arte2job.put(arte, new JobInfo(jobId, taskId == null ? 0 : taskId));
        }
    }

    public void unregister(Arte arte) {
        synchronized (arte2job) {
            arte2job.remove(arte);
        }
    }

    public void start() {
        if (updaterThread == null || !updaterThread.isAlive()) {
            updaterThread = new UpdaterThread();
            updaterThread.start();
        } else {
            throw new IllegalStateException("Updates are already started");
        }
    }

    public void stop() {
        if (updaterThread != null) {
            try {
                updaterThread.stopping = true;
                updaterThread.interrupt();
                final long maxWaitMillis = 30000;
                final long waitAfterConnectionCloseMillis = 5000;
                try {
                    updaterThread.join(maxWaitMillis - waitAfterConnectionCloseMillis);
                } catch (InterruptedException ex) {
                    //ignore
                }
                if (updaterThread.isAlive()) {
                    updaterThread.closeDbConnection();
                }
                try {
                    updaterThread.join(waitAfterConnectionCloseMillis);
                } catch (InterruptedException ex) {
                    //ignore
                }
            } finally {
                updaterThread = null;
            }
        }
    }

    private class UpdaterThread extends Thread implements IDbQueries {
        
        private volatile boolean stopping = false;
        private volatile Connection dbConnection;
        
        private static final String qryLockJobStmtSQL = "select id from rdx_js_jobqueue where id = ? for update skip locked";
        private volatile Stmt qryLockJobStmt = new Stmt(qryLockJobStmtSQL,Types.BIGINT);
        private volatile PreparedStatement qryLockJob = null;
        
        private static final String qryLockTaskStmtSQL = "select id from rdx_js_task where id = ? for update skip locked";
        private volatile Stmt qryLockTaskStmt = new Stmt(qryLockTaskStmtSQL,Types.BIGINT);
        private volatile PreparedStatement qryLockTask = null;

        private static final String qryUpdateJobStmtSQL = "update rdx_js_jobqueue set selfchecktime=sysdate, selfCheckTimeMillis=RDX_UTILS.getUnixEpochMillis() where id = ?";
        private volatile Stmt qryUpdateJobStmt = new Stmt(qryUpdateJobStmtSQL,Types.BIGINT);
        private volatile PreparedStatement qryUpdateJob = null;

        private static final String qryUpdateTaskStmtSQL = "update rdx_js_task set selfchecktime=sysdate, selfCheckTimeMillis=RDX_UTILS.getUnixEpochMillis() where id = ?";        
        private volatile Stmt qryUpdateTaskStmt = new Stmt(qryUpdateTaskStmtSQL,Types.BIGINT);
        private volatile PreparedStatement qryUpdateTask = null;
        
        private final IDbQueries delegate = new DelegateDbQueries(this, null);
        
        private long lastDbActivityTimeMillis = 0;

        public UpdaterThread() {
            super(THREAD_NAME);
            setDaemon(true);
        }

        @Override
        public void run() {
            try {
                while (!stopping && !isInterrupted()) {
                    try {
                        updateCheckTimes();
                        if (System.currentTimeMillis() - lastDbActivityTimeMillis > DB_PING_INTERVAL) {
                            pingDb();
                        }
                        if (dbConnection != null) {
                            dbConnection.rollback();//If everything is ok, commit should be already done. If not - rollback is reasonable thing to do.
                        }
                        Thread.sleep(UPDATE_INTERVAL_SECONDS * 1000);
                    } catch (Throwable t) {
                        if (t instanceof InterruptedException) {
                            return;
                        }
                        try {
                            instance.getTrace().put(EEventSeverity.ERROR, "Unexpected error in " + THREAD_NAME + ": " + ExceptionTextFormatter.throwableToString(t), EEventSource.INSTANCE);
                        } catch (Throwable t1) {
                            new RuntimeException("Unable to write error from " + Thread.currentThread().getName(), t1).printStackTrace(System.err);
                            new RuntimeException("Initial error from " + Thread.currentThread().getName(), t).printStackTrace(System.err);
                        }
                    }
                }
            } catch (Throwable t) {
                instance.getTrace().put(EEventSeverity.ERROR, "Unexpected unrecoverable error in " + THREAD_NAME + ": " + ExceptionTextFormatter.throwableToString(t), EEventSource.INSTANCE);
            } finally {
                closeDbConnection();
            }
        }

        private void pingDb() {
            try {
                if (!dbConnection.isValid(5)) {
                    closeDbConnection();
                }
            } catch (Exception ex) {
                //ignore
            }
        }

        private void updateCheckTimes() {
            try {
                Connection connection = getDbConnection();
                if (connection != null) {
                    if (qryLockJob == null) {
                        qryLockJob = ((RadixConnection)connection).prepareStatement(qryLockJobStmt);
                    }
                    if (qryLockTask == null) {
                        qryLockTask = ((RadixConnection)connection).prepareStatement(qryLockTaskStmt);
                    }
                    if (qryUpdateJob == null) {
                        qryUpdateJob = ((RadixConnection)connection).prepareStatement(qryUpdateJobStmt);
                    }
                    if (qryUpdateTask == null) {
                        qryUpdateTask = ((RadixConnection)connection).prepareStatement(qryUpdateTaskStmt);
                    }
                    final List<JobInfo> toUpdate = new ArrayList<>();
                    synchronized (arte2job) {
                        for (Map.Entry<Arte, JobInfo> entry : arte2job.entrySet()) {
                            final Arte arte = entry.getKey();
                            if (arte != null && arte.getProcessorThread().isAlive() && Objects.equals(arte.getJobQueue().getCurrentJobId(), entry.getValue().jobId)) {
                                toUpdate.add(entry.getValue());
                            }
                        }
                    }
                    for (JobInfo jobInfo : toUpdate) {
                        qryLockJob.setLong(1, jobInfo.jobId);
                        try (ResultSet lockJobRs = qryLockJob.executeQuery()) {
                            lastDbActivityTimeMillis = System.currentTimeMillis();
                            if (lockJobRs.next()) {
                                qryUpdateJob.setLong(1, jobInfo.jobId);
                                qryUpdateJob.executeUpdate();
                                connection.commit();
                                if (jobInfo.taskId > 0) {
                                    qryLockTask.setLong(1, jobInfo.taskId);
                                    try (ResultSet lockTaskRs = qryLockTask.executeQuery()) {
                                        if (lockTaskRs.next()) {
                                            qryUpdateTask.setLong(1, jobInfo.taskId);
                                            qryUpdateTask.executeUpdate();
                                            connection.commit();
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            } catch (SQLException ex) {
                try {
                    instance.getTrace().put(EEventSeverity.ERROR, "Error in " + THREAD_NAME + ": " + ExceptionTextFormatter.throwableToString(ex), EEventSource.INSTANCE);
                } finally {
                    closeDbConnection();
                }
            }
        }

        private Connection getDbConnection() throws SQLException {
            if (dbConnection != null || stopping) {
                return dbConnection;
            }
            dbConnection = instance.openNewDbConnection(THREAD_NAME, null);
            return dbConnection;
        }

        private void closeDbConnection() {
            if (dbConnection != null) {
                try {
                    try {
                        dbConnection.rollback();
                    } finally {
                        dbConnection.close();
                    }
                } catch (Exception ex) {
                    //ignore
                } finally {
                    qryUpdateJob = null;
                    qryUpdateTask = null;
                    qryLockJob = null;
                    qryLockTask = null;
                    dbConnection = null;
                }
            }
        }
        
        @Override public Iterable<PreparedStatement> getPreparedStatements() {return delegate.getPreparedStatements();}
        @Override public Iterable<String> getPeparedStatementsSourceSQL() {return delegate.getPeparedStatementsSourceSQL();}
        @Override public int getPreparedStatementsCount() {return delegate.getPreparedStatementsCount();}
        @Override public int getTotalPreparedStatementsCount() {return delegate.getTotalPreparedStatementsCount();}
        @Override public void prepareAll() throws SQLException {delegate.prepareAll();}
        @Override public void prepareAll(Connection conn) throws SQLException {delegate.prepareAll(conn);}
        @Override public void closeAll() {delegate.closeAll();}
    }

    private static class JobInfo {

        public final long jobId;
        public final long taskId;

        public JobInfo(long jobId, long taskId) {
            this.jobId = jobId;
            this.taskId = taskId;
        }

    }
}
