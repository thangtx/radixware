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
package org.radixware.kernel.server.units.job.executor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.sc.SapClientOptions;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.aio.Event;
import org.radixware.kernel.server.aio.EventDispatcher.TimerEvent;
import org.radixware.kernel.server.aio.ServiceManifestLoader;
import org.radixware.kernel.server.aio.ServiceManifestServerLoader;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.arte.JobQueue;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.instance.aadc.AadcManager;
import org.radixware.kernel.server.jdbc.RadixConnection;
import org.radixware.kernel.server.monitoring.AbstractMonitor;
import org.radixware.kernel.server.monitoring.MonitorFactory;
import org.radixware.kernel.server.units.AsyncEventHandlerUnit;
import org.radixware.kernel.server.units.Messages;
import org.radixware.kernel.server.units.Unit;
import org.radixware.kernel.server.units.UnitDescription;
import org.radixware.kernel.server.units.UnitView;

/**
 * Job Executor
 *
 *
 */
public final class JobExecutorUnit extends AsyncEventHandlerUnit {

    public static final int MAX_SKIPPED_JOB_CHECKS = 10;
    private final static long ON_BUSY_SLEEP_TIME_MILLIS = 10;
    private final static long ON_ALL_LAST_RQ_FAULT_SLEEP_TIME_MILLIS = 500;
    private static final long RESTORE_CONNECTION_RETRY_INTERVAL_MILLIS = 3000;
    private static final long MAX_CONTINUOUS_EXECUTION_TIME_MILLIS = 5000;
    private static final long PROCESS_HUNG_JOBS_INTERVAL_MILLIS = 5000;
    private final JobExecutorDbQueries jobExecDbQueries;
    private long sleepOnAllLastRqFaultedMillis = 0;
    private ServiceManifestLoader manifestLoader = null;
    private Options options;
    private final Map<Long, Job> executingJobs = new HashMap<>();
    private final Set<ThreadKey> executingThreads = new HashSet<>();
    boolean isDoCurJobsSheduled = false;
    private AasClientPool aasClientPool = null;
    private long lastRestoreConnectionMillis = 0;
    private long lastProcessHungJobsMillis = 0;
    private boolean restoreConnectionScheduled = false;
    final JobQueue.Utils jobQueueUtils;
    private final AadcManager aadcManager;

    public JobExecutorUnit(final Instance instModel, final Long id, final String title) {
        super(instModel, id, title, new MonitorFactory<AbstractMonitor, Unit>() {
            @Override
            public AbstractMonitor createMonitor(final Unit unit) {
                return new JobExecutorMonitor(unit);
            }
        });
        jobExecDbQueries = new JobExecutorDbQueries(this);
        jobQueueUtils = new JobQueue.Utils(getDbConnection());
        aadcManager = instModel.getAadcManager();
    }

    ServiceManifestLoader getManifestLoader() {
        return manifestLoader;
    }

    Options getOptions() {
        return options;
    }

    @Override
    protected boolean startImpl() throws Exception {
        if (!super.startImpl()) {
            return false;
        }
        manifestLoader = new ServiceManifestServerLoader() {

            @Override
            public List<SapClientOptions> readSaps(Long systemId, Long thisInstanceId, String serviceUri, String scpName, long maxCachedValAgeMillis) {
                return super.readSaps(systemId, thisInstanceId, serviceUri, scpName, maxCachedValAgeMillis); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            protected Integer getTargetAadcMemberId() {
                return getInstance().getAadcInstMemberId();
            }

            @Override
            protected Connection getDbConnection() {
                return JobExecutorUnit.this.getDbConnection();
            }

            @Override
            protected Arte getArte() {
                return null;
            }
        };
        aasClientPool = new AasClientPool(this);
        executingJobs.clear();
        sleepOnAllLastRqFaultedMillis = 0;
        options = jobExecDbQueries.readOptions();
        aasClientPool.onUnitOptionsChanged();
        jobQueueUtils.setDbConnection(getDbConnection());
        final String optionsStr = options.toString();
        getTrace().put(EEventSeverity.EVENT, Messages.START_OPTIONS + (isLocal() ? "\nLOCAL MODE\n" : "") + optionsStr, Messages.MLS_ID_START_OPTIONS, new ArrStr(getFullTitle(), optionsStr), getEventSource(), false);
        getDispatcher().waitEvent(new TimerEvent(), this, System.currentTimeMillis());
        lastRestoreConnectionMillis = 0;
        lastProcessHungJobsMillis = 0;
        restoreConnectionScheduled = false;
        return true;
    }

    void processAllEventsForAasClientPoolStop() {
        processAllEventsSuppressingRuntimeExceptions();
    }

    @Override
    protected void stopImpl() {
        if (aasClientPool != null) {
            aasClientPool.stop();
            aasClientPool = null;
        }
        if (executingJobs != null) {
            if (!executingJobs.isEmpty()) {
                getTrace().put(EEventSeverity.ERROR, "All AAS seances are closed, but there are unfinished jobs left", EEventSource.JOB_EXECUTOR);
            }
            executingJobs.clear();
        }
        options = null;
        manifestLoader = null;
        jobQueueUtils.setDbConnection(null);
        super.stopImpl();
    }

    @Override
    protected UnitView newUnitView() {
        return new JobExecutorUnitView(this);
    }

    @Override
    public void onEvent(final Event ev) {
        if (ev instanceof TimerEvent) {
            isDoCurJobsSheduled = true;
        } else {
            throw new IllegalUsageError("Invalid event " + ev);
        }
    }

    @Override
    public String getUnitTypeTitle() {
        return JobExecutorMessages.JOB_EXECUTOR_UNIT_TYPE_TITLE;
    }

    @Override
    public Long getUnitType() {
        return EUnitType.JOB_EXECUTOR.getValue();
    }

    @Override
    protected void writeBasicStats() throws SQLException, InterruptedException {
        getDbQueries().writeBasicStats();
    }

    @Override
    protected void rereadOptionsImpl() throws SQLException, InterruptedException {
        final Options newOptions = jobExecDbQueries.readOptions();
        if (newOptions == null || newOptions.equals(options)) {
            return;
        }
        options = newOptions;
        aasClientPool.onUnitOptionsChanged();
        final String newOptionsStr = newOptions.toString();
        getTrace().put(EEventSeverity.EVENT, Messages.OPTIONS_CHANGED + newOptionsStr, Messages.MLS_ID_OPTIONS_CHANGED, new ArrStr(getFullTitle(), newOptionsStr), getEventSource(), false);
    }

    long getExecPeriodMillis() {
        return options.execPeriodMillis;
    }

    @Override
    public String getEventSource() {
        return EEventSource.JOB_EXECUTOR.getValue();
    }

    @Override
    protected void setDbConnection(final RadixConnection dbConnection) {
        jobExecDbQueries.closeAll();
        jobQueueUtils.setDbConnection(dbConnection);
        super.setDbConnection(dbConnection);
    }

    final JobExecutorDbQueries getDbQueries() {
        return jobExecDbQueries;
    }

    @Override
    protected void maintenanceImpl() throws InterruptedException {
        isDoCurJobsSheduled = false;
        super.maintenanceImpl();
        if (isDoCurJobsSheduled && !isShutdownRequested()) {
            try {
                if (restoreConnectionScheduled) {
                    restoreDbConnection();
                    lastRestoreConnectionMillis = System.currentTimeMillis();
                }
                doCurJobs();
                if (System.currentTimeMillis() - lastProcessHungJobsMillis > PROCESS_HUNG_JOBS_INTERVAL_MILLIS) {
                    try {
                        getDbQueries().processHungJobs();
                    } finally {
                        lastProcessHungJobsMillis = System.currentTimeMillis();
                    }
                }
                lastRestoreConnectionMillis = 0;
                restoreConnectionScheduled = false;
            } catch (SQLException | DatabaseError ex) {
                logSqlException(ex);
                if (System.currentTimeMillis() - lastRestoreConnectionMillis > RESTORE_CONNECTION_RETRY_INTERVAL_MILLIS) {
                    getDispatcher().waitEvent(new TimerEvent(), this, System.currentTimeMillis());
                } else {
                    getDispatcher().waitEvent(new TimerEvent(), this, lastRestoreConnectionMillis + RESTORE_CONNECTION_RETRY_INTERVAL_MILLIS);
                }
                restoreConnectionScheduled = true;
            }
        }
    }

    @Override
    protected UnitDescription getStartedDuplicatedUnitDescription() throws SQLException {
        return getInstance().getStartedDuplicatedExecutor(this);
    }

    public boolean isLocal() {
        return getInstance().getTargetJobExecutorId() != null && getInstance().getTargetJobExecutorId() == getId();
    }

    @Override
    protected String getIdForSingletonLock() {
        if (isLocal()) {
            return "LocalJobExecutorUnit#" + getId();
        } else if (aadcManager.isInAadc()) {
            return "JobExecutor-AADC#" + aadcManager.getMemberId();
        } else {
            return super.getIdForSingletonLock();
        }
    }

    void doCurJobs() throws SQLException {
        final long curExecutionStartMillis = System.currentTimeMillis();
        while (!isShuttingDown()) {
            if (sleepOnAllLastRqFaultedMillis == 0 && (getAasClientPool().isAllRqFromLastIterationFaulted())) {
                //all request sent last time returned with faults
                //let's sleep a bit to let AAS restore themselfs
                sleepOnAllLastRqFaultedMillis = System.currentTimeMillis() + ON_ALL_LAST_RQ_FAULT_SLEEP_TIME_MILLIS;
                getDispatcher().waitEvent(new TimerEvent(), this, sleepOnAllLastRqFaultedMillis);
                getDispatcher().waitEvent(new TimerEvent(), this, sleepOnAllLastRqFaultedMillis);
                return;
            } else if (sleepOnAllLastRqFaultedMillis != 0) {
                if (sleepOnAllLastRqFaultedMillis > System.currentTimeMillis()) {
                    return; // proceed sleeping
                }				//after sleepOnOnAllLastRqFaulted we should try to send rq one more time
                sleepOnAllLastRqFaultedMillis = 0;
            }
            JobExecutorDbQueries.JobIter jobs = null;
            getAasClientPool().newIteration();
            try {
                final long jobsLoadTimeMillis = System.currentTimeMillis();
                jobs = getDbQueries().loadCurJobs();
                if (isShuttingDown()) {
                    return;
                }
                long sleepTimeMillis = options.execPeriodMillis;
                Job job;
                if (jobs != null && (job = jobs.next()) != null) {
                    while (job != null && !isShuttingDown()) {
                        if (!isThreadExecuting(job.getThreadKey())) {
                            if (job.delayMillis > 0) {
                                sleepTimeMillis = Math.min(sleepTimeMillis, job.delayMillis);
                            } else {
                                final AasClientPool.EInvokeResult result = getAasClientPool().invoke(job);
                                if (result == AasClientPool.EInvokeResult.OK) {
                                    setJobIsExecuting(job);
                                } else if (result == AasClientPool.EInvokeResult.ALL_CLIENTS_ARE_BUSY) {
                                    final int waitingJobsCnt = getDbQueries().getCurJobsCount();
                                    if (waitingJobsCnt != -1) {
                                        ((JobExecutorMonitor) getMonitor()).setWaitingJobsCount(waitingJobsCnt);
                                    }
                                    //let's sleep a bit to allow IO event be handled
                                    getDispatcher().waitEvent(new TimerEvent(), this, System.currentTimeMillis() + ON_BUSY_SLEEP_TIME_MILLIS);
                                    return;
                                } else {
                                    //unable to execute job, try next
                                }
                            }
                        }
                        job = jobs.next();
                    }
                }
                if (!isShuttingDown()) {
                    ((JobExecutorMonitor) getMonitor()).setWaitingJobsCount(0);
                    final long nextTicMillis = jobsLoadTimeMillis + sleepTimeMillis;
                    final long curTimeMillis = System.currentTimeMillis();
                    if (nextTicMillis > curTimeMillis || curTimeMillis - curExecutionStartMillis > MAX_CONTINUOUS_EXECUTION_TIME_MILLIS) { //let's shedule next TIC
//                        getAasClientPool().disconnectAllFree();
                        waitNextIteration(nextTicMillis);
                        return; // now we can sleep a bit
                    } else {
                        continue; //let's do it right now
                    }
                }
            } finally {
                if (jobs != null) {
                    jobs.close();
                }
            }
        }
    }

    private void waitNextIteration(final long nextIterationScheduledTimeMillis) {
        final long nearestIterationTimeMillis = getDispatcher().getNearestTimerSubscription(this);
        if (nearestIterationTimeMillis == -1 || nextIterationScheduledTimeMillis < nearestIterationTimeMillis) {
            getDispatcher().waitEvent(new TimerEvent(), this, nextIterationScheduledTimeMillis);
        }
    }

    private void logSqlException(final Throwable ex) {
        final String exStack = ExceptionTextFormatter.exceptionStackToString(ex);
        getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(getFullTitle(), exStack), getEventSource(), false);
    }

    @Override
    public boolean isSingletonUnit() {
        return true;
    }

    AasClientPool getAasClientPool() {
        return aasClientPool;
    }

    final void setJobIsExecuting(final Job job) {
        executingJobs.put(job.id, job);
        setThreadExecuting(job.threadKey, true);
        ((JobExecutorMonitor) getMonitor()).jobStarted();
    }

    private boolean isThreadExecuting(final ThreadKey threadKey) {
        return threadKey == null ? false : executingThreads.contains(threadKey);
    }

    void setThreadExecuting(final ThreadKey key, final boolean executing) {
        if (key != null) {
            if (executing) {
                executingThreads.add(key);
            } else {
                executingThreads.remove(key);
            }
        }
    }

    final Job getExecutingJob(final long id) {
        return executingJobs.get(id);
    }

    final void setJobDone(final Job job) {
        Job executingJob = executingJobs.get(job.id);
        if (executingJob != null) {
            //job can be rescheduled, in this case response can be
            //received later then new execution has been started 
            //and we don't want to mark new (and unfinished) job as done.
            if (executingJob.unlockCount == job.unlockCount) {
                executingJobs.remove(job.id);
                setThreadExecuting(job.threadKey, false);
            }
            ((JobExecutorMonitor) getMonitor()).jobDone();
        }
    }

    long getExecutingJobsCount() {
        final AasClientPool pool = getAasClientPool();
        if (pool != null) {
            return pool.getBusyCount();
        }
        return 0;
    }

    //Options 	
    static final class Options {

        final int parallelCount;
        final int aboveNormalDelta;
        final int highDelta;
        final int veryHighDelta;
        final int criticalDelta;
        final int execPeriodMillis;

        public Options(
                int execPeriodMillis,
                int parallelCount,
                int aboveNormalDelta,
                int highDelta,
                int veryHighDelta,
                int criticalDelta) {
            this.parallelCount = parallelCount;
            this.aboveNormalDelta = aboveNormalDelta;
            this.highDelta = highDelta;
            this.veryHighDelta = veryHighDelta;
            this.criticalDelta = criticalDelta;
            this.execPeriodMillis = execPeriodMillis;
        }

        public Options() {
            this(100, 10, 0, 0, 0, 0);
        }

        @Override
        public String toString() {
            return "{\n\t"
                    + JobExecutorMessages.EXEC_PERIOD + ": " + String.valueOf((double) execPeriodMillis / 1000) + "; \n\t"
                    + JobExecutorMessages.PARALLEL_COUNT + ": " + String.valueOf(parallelCount) + "; \n\t"
                    + JobExecutorMessages.ABOVE_NORMAL_DELTA + ": " + String.valueOf(aboveNormalDelta) + "; \n\t"
                    + JobExecutorMessages.HIGH_DELTA + ": " + String.valueOf(highDelta) + "; \n\t"
                    + JobExecutorMessages.VERY_HIGH_DELTA + ": " + String.valueOf(veryHighDelta) + "; \n\t"
                    + JobExecutorMessages.CRITICAL_DELTA + ": " + String.valueOf(criticalDelta) + "; \n\t"
                    + "}";
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 43 * hash + this.parallelCount;
            hash = 43 * hash + this.aboveNormalDelta;
            hash = 43 * hash + this.highDelta;
            hash = 43 * hash + this.veryHighDelta;
            hash = 43 * hash + this.criticalDelta;
            hash = 43 * hash + this.execPeriodMillis;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Options other = (Options) obj;
            if (this.parallelCount != other.parallelCount) {
                return false;
            }
            if (this.aboveNormalDelta != other.aboveNormalDelta) {
                return false;
            }
            if (this.highDelta != other.highDelta) {
                return false;
            }
            if (this.veryHighDelta != other.veryHighDelta) {
                return false;
            }
            if (this.criticalDelta != other.criticalDelta) {
                return false;
            }
            if (this.execPeriodMillis != other.execPeriodMillis) {
                return false;
            }
            return true;
        }
    }
}
