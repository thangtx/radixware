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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import org.radixware.kernel.common.enums.EEventContextType;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.ETaskStatus;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.jdbc.DontCheckDbQuery;
import org.radixware.kernel.server.instance.JobCheckTimeUpdater;
import org.radixware.kernel.server.jdbc.AbstractDbQueries;
import org.radixware.kernel.server.units.Messages;

final class JobExecutorDbQueries extends AbstractDbQueries {

    private static final int MAX_JOB_INACTIVE_SEC = JobCheckTimeUpdater.UPDATE_INTERVAL_SECONDS * JobExecutorUnit.MAX_SKIPPED_JOB_CHECKS;

    private static enum EHungJobType {

        TASK_INITIAL,
        OTHER_RESTARTABLE,
        OTHER_NONRESTARTABLE
    }
    private final JobExecutorUnit unit;

    private PreparedStatement qryReadOptions = null;
    private static final String qryReadOptionsSQL = "select parallelCnt, aboveNormalDelta, highDelta, veryHighDelta, criticalDelta, "
            + "1000*execPeriod execPeriodMillis "
            + "from rdx_js_jobexecutorunit "
            + "where id = ?";

    private PreparedStatement qryCurJobs = null;
    private static final String qryCurJobsSQL = "select "
            + "ID, TITLE, DUETIME, cast(sysTimestamp as timestamp) systs, curPriority, priority, scpName, unlockCount, threadPoolClassGuid, threadPoolPid, threadKey "
            + "from RDX_JS_JOBQUEUE "
            + "where DUETIME < sysdate + (?/1000/60/60/24) and processorTitle is null and nvl(executorId,-1)=? "
            + "and (? is null or aadcMemberId = ?) "
            + "and (? = 0 or taskId is null) "
            + "order by CURPRIORITY desc, DUETIME ";

    private PreparedStatement qryCurJobsCount = null;
    private static final String qryCurJobsCountSQL = "select "
            + "count(ID)"
            + "from RDX_JS_JOBQUEUE "
            + "where DUETIME <= sysdate and nvl(executorId,-1)=? "
            + "and (? is null or aadcMemberId = ?) "
            + "and (? = 0 or taskId is null) ";

    private PreparedStatement qryWriteBasicStats = null;
    private static final String qryWriteBasicStatsSQL = "update rdx_js_jobexecutorunit set avgExecCount=?, avgWaitCount=? where id = ?";

    private PreparedStatement qryDoPriorityBoosting = null;
    private static final String qryDoPriorityBoostingSQL = "Update RDX_JS_JobQueue "
            + "set CurPriority = rdx_js_job.calcPriority(priority, curPriority, priorityBoostingSpeed, dueTime) "
            + "where dueTime<sysdate and priorityBoostingSpeed>0";

    private PreparedStatement qryGetAadcMemberJobExecutors = null;
    private static final String qryGetAadcMemberJobExecutorsSQL = "select u.id, u.started, u.selfCheckTimeMillis from rdx_unit u, rdx_instance i where u.instanceId = i.id and u.type=" + EUnitType.JOB_EXECUTOR.getValue() + " and nvl(i.targetExecutorId, -1) <> u.id and i.aadcMemberId=?";

    @DontCheckDbQuery
    private PreparedStatement qryGetRestartableHungJobs = null;

    @DontCheckDbQuery
    private PreparedStatement qryGetNonRestartableHungJobs = null;

    @DontCheckDbQuery
    private PreparedStatement qryGetHungTaskJobs = null;

    private static final String qryGetHungTaskSQL = "select id, title, status, faultMess, finishExecTime, sysdate from rdx_js_task where id = ? for update skip locked";
    private PreparedStatement qryGetHungTask = null;

//    private JobExecutorDbQueries() {
//        this(null);
//    }
    JobExecutorDbQueries(final JobExecutorUnit unit) {
        this.unit = unit;
    }

    final JobExecutorUnit.Options readOptions() throws SQLException, InterruptedException {
        try {
            final Connection dbConnection = unit.getDbConnection();
            if (dbConnection != null) {
                if (qryReadOptions == null) {
                    qryReadOptions = dbConnection.prepareStatement(qryReadOptionsSQL);
                }

                qryReadOptions.setLong(1, unit.getId());
                try (final ResultSet rs = qryReadOptions.executeQuery()) {
                    if (rs.next()) {
                        int parallelCount = rs.getInt("parallelCnt");
                        if (parallelCount < 1) {
                            parallelCount = 1;
                        }
                        int aboveNormalDelta = rs.getInt("aboveNormalDelta");
                        if (aboveNormalDelta < 0) {
                            aboveNormalDelta = 0;
                        }
                        int highDelta = rs.getInt("highDelta");
                        if (highDelta < 0) {
                            highDelta = 0;
                        }
                        int veryHighDelta = rs.getInt("veryHighDelta");
                        if (veryHighDelta < 0) {
                            veryHighDelta = 0;
                        }
                        int criticalDelta = rs.getInt("veryHighDelta");
                        if (criticalDelta < 0) {
                            criticalDelta = 0;
                        }
                        int execPeriodMillis = rs.getInt("execPeriodMillis");
                        if (execPeriodMillis < 0) {
                            execPeriodMillis = 0;
                        }
                        return new JobExecutorUnit.Options(execPeriodMillis, parallelCount, aboveNormalDelta, highDelta, veryHighDelta, criticalDelta);
                    } else {
                        throw new IllegalUsageError("Unknown Job Executor Unit unit #" + String.valueOf(unit.getId()));
                    }
                }
            }
        } catch (Throwable e) {
            if (e instanceof InterruptedException || Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            if (e instanceof SQLException) {
                throw (SQLException) e;
            }
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            unit.getTrace().put(EEventSeverity.ERROR, Messages.ERR_CANT_READ_OPTIONS + ": \n" + exStack, Messages.MLS_ID_ERR_CANT_READ_OPTIONS, new ArrStr(unit.getFullTitle(), exStack), unit.getEventSource(), false);
        }
        return new JobExecutorUnit.Options();//default options
    }

    final JobIter loadCurJobs() throws SQLException {
        if (qryCurJobs == null) {
            final Connection dbConnection = unit.getDbConnection();
            if (dbConnection == null) {
                return null;
            }
            qryCurJobs = dbConnection.prepareStatement(qryCurJobsSQL);
            if (unit.isLocal()) {
                qryCurJobs.setLong(2, unit.getId());
            } else {
                qryCurJobs.setLong(2, -1);
            }
            final Integer aadcMemberId = unit.getInstance().getAadcInstMemberId();
            if (aadcMemberId != null) {
                qryCurJobs.setInt(3, aadcMemberId);
                qryCurJobs.setInt(4, aadcMemberId);
            } else {
                qryCurJobs.setNull(3, Types.INTEGER);
                qryCurJobs.setNull(4, Types.INTEGER);
            }
            qryCurJobs.setInt(5, unit.getInstance().isAadcTestedMember() ? 1 : 0); //exclude tasks
        }
        qryCurJobs.setLong(1, unit.getExecPeriodMillis());
        return new JobIter(qryCurJobs.executeQuery());
    }

    final int getCurJobsCount() throws SQLException {
        if (qryCurJobsCount == null) {
            final Connection dbConnection = unit.getDbConnection();
            if (dbConnection == null) {
                return -1;
            }
            qryCurJobsCount = dbConnection.prepareStatement(qryCurJobsCountSQL);
            if (unit.isLocal()) {
                qryCurJobsCount.setLong(1, unit.getId());
            } else {
                qryCurJobsCount.setLong(1, -1);
            }
            final Integer aadcMemberId = unit.getInstance().getAadcInstMemberId();
            if (aadcMemberId != null) {
                qryCurJobsCount.setInt(2, aadcMemberId);
                qryCurJobsCount.setInt(3, aadcMemberId);
            } else {
                qryCurJobsCount.setNull(2, Types.INTEGER);
                qryCurJobsCount.setNull(3, Types.INTEGER);
            }
            qryCurJobsCount.setInt(4, unit.getInstance().isAadcTestedMember() ? 1 : 0); //exclude tasks
        }
        final ResultSet rs = qryCurJobsCount.executeQuery();
        try {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } finally {
            rs.close();
        }
        return -1;
    }

    public boolean aadcMemberHasRunningJobExecutors(final int aadcMemberId) throws SQLException {
        if (unit.getDbConnection() == null || unit.getDbConnection().isClosed()) {
            return false;
        }
        if (qryGetAadcMemberJobExecutors == null || qryGetAadcMemberJobExecutors.isClosed() || qryGetAadcMemberJobExecutors.getConnection().isClosed()) {
            qryGetAadcMemberJobExecutors = unit.getDbConnection().prepareStatement(qryGetAadcMemberJobExecutorsSQL);
        }
        qryGetAadcMemberJobExecutors.setInt(1, aadcMemberId);
        try (final ResultSet rs = qryGetAadcMemberJobExecutors.executeQuery()) {
            while (rs.next()) {
                if (unit.getInstance().isUnitRunning(rs.getInt("id"), System.currentTimeMillis(), rs.getBoolean("started") ? new Timestamp(rs.getLong("selfCheckTimeMillis")) : null)) {
                    return true;
                }
            }
        }
        return false;
    }

    private PreparedStatement prepareGetHungJobs(EHungJobType hungJobType) throws SQLException {
        PreparedStatement result = unit.getDbConnection().prepareStatement("select id, title, processorTitle, nvl((RDX_UTILS.getUnixEpochMillis() - selfchecktimemillis) / 1000,(sysdate - selfchecktime) * 24 * 3600) inactiveSec, taskId from rdx_js_jobqueue where"
                + " processorTitle is not null and ((selfchecktime is not null and (sysdate - selfchecktime) * 24 * 3600 > ?) and (selfchecktimemillis is null or (RDX_UTILS.getUnixEpochMillis() - selfchecktimemillis) > ?)) and allowRerun " + (hungJobType == EHungJobType.OTHER_RESTARTABLE ? " > 0" : (" = 0 and RDX_JS_JOB.isTaskExecuteCall(className, methodName) = " + (hungJobType == EHungJobType.TASK_INITIAL ? "1" : "0")))
                + " and " + (unit.isLocal() ? " executorId=? " : " executorId is null")
                + " and nvl(aadcMemberId, 0)=?"
                + " for update of processorTitle skip locked", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        result.setInt(1, MAX_JOB_INACTIVE_SEC);
        result.setInt(2, MAX_JOB_INACTIVE_SEC * 1000);
        Integer instAadcId = unit.getInstance().getAadcInstMemberId();
        if (instAadcId == null) {
            instAadcId = 0;
        }
        if (unit.isLocal()) {
            result.setLong(3, unit.getId());
            result.setInt(4, instAadcId);
        } else {
            result.setInt(3, instAadcId);
        }

        return result;
    }

    private void restartRestartableHungJobs() throws SQLException {
        if (unit.getDbConnection() != null) {
            if (qryGetRestartableHungJobs == null) {
                qryGetRestartableHungJobs = prepareGetHungJobs(EHungJobType.OTHER_RESTARTABLE);
            }
            try (ResultSet rs = qryGetRestartableHungJobs.executeQuery()) {
                while (rs.next()) {
                    unit.jobQueueUtils.unlockJob(rs.getLong("id"));
                    final String jobTitle = getJobTitleForLog(rs.getLong("id"), rs.getString("title"));
                    unit.getTrace().put(EEventSeverity.WARNING, String.format(JobExecutorMessages.HUNG_JOB_UNLOCKED, jobTitle, rs.getString("processorTitle"), MAX_JOB_INACTIVE_SEC), JobExecutorMessages.MLS_HUNG_JOB_UNLOCKED, new ArrStr(jobTitle, String.valueOf(rs.getString("processorTitle")), String.valueOf(MAX_JOB_INACTIVE_SEC)), EEventSource.JOB_EXECUTOR, false);
                }
                unit.getDbConnection().commit();
            } catch (SQLException | RuntimeException ex) {
                try {
                    unit.getDbConnection().rollback();
                } catch (Exception ex1) {
                    //ignore
                }
                throw ex;
            }
        }
    }

    private void reportJobRemoved(final long jobId, final String jobTitle, final String processorTitle, final Long taskId) {
        final String titleForLog = getJobTitleForLog(jobId, jobTitle);
        if (taskId != null) {
            unit.getTrace().enterContext(EEventContextType.TASK, taskId, null);
        }
        try {
            unit.getTrace().put(EEventSeverity.ERROR, String.format(JobExecutorMessages.HUNG_JOB_REMOVED, titleForLog, processorTitle, MAX_JOB_INACTIVE_SEC), JobExecutorMessages.MLS_HUNG_JOB_REMOVED, new ArrStr(titleForLog, String.valueOf(processorTitle), String.valueOf(MAX_JOB_INACTIVE_SEC)), EEventSource.JOB_EXECUTOR, false);
        } finally {
            if (taskId != null) {
                unit.getTrace().leaveContext(EEventContextType.TASK, taskId, null);
            }
        }

    }

    private String getJobTitleForLog(final long jobId, final String jobTitle) {
        return "#" + jobId + " '" + jobTitle + "'";
    }

    private void removeNonRestartableHungJobs() throws SQLException {
        if (unit.getDbConnection() != null) {
            if (qryGetNonRestartableHungJobs == null) {
                qryGetNonRestartableHungJobs = prepareGetHungJobs(EHungJobType.OTHER_NONRESTARTABLE);
            }
            try {
                try (ResultSet rs = qryGetNonRestartableHungJobs.executeQuery()) {
                    while (rs.next()) {
                        rs.deleteRow();
                        Long taskId = rs.getLong("taskId");
                        if (rs.wasNull()) {
                            taskId = null;
                        }
                        reportJobRemoved(rs.getLong("id"), rs.getString("title"), rs.getString("processorTitle"), taskId);
                    }
                }
                unit.getDbConnection().commit();
            } catch (SQLException | RuntimeException ex) {
                try {
                    unit.getDbConnection().rollback();
                } catch (Exception ex1) {
                    //ignore
                }
                throw ex;
            }
        }
    }

    private void failHungTasks() throws SQLException {
        if (unit.getDbConnection() != null) {
            if (qryGetHungTaskJobs == null) {
                qryGetHungTaskJobs = prepareGetHungJobs(EHungJobType.TASK_INITIAL);
            }
            if (qryGetHungTask == null) {
                qryGetHungTask = unit.getDbConnection().prepareStatement(qryGetHungTaskSQL, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            }
            try (ResultSet jobRs = qryGetHungTaskJobs.executeQuery()) {
                while (jobRs.next()) {
                    qryGetHungTask.setLong(1, jobRs.getLong("taskId"));
                    try (ResultSet taskRs = qryGetHungTask.executeQuery()) {
                        if (taskRs.next()) {
                            final String status = taskRs.getString("status");
                            if (!(ETaskStatus.EXECUTING.getValue().equals(status) || ETaskStatus.CANCELLING.getValue().equals(status))) {
                                unit.getTrace().put(EEventSeverity.WARNING, "Task #" + jobRs.getLong("taskId") + " '" + taskRs.getString("title") + "' that has hung related jobs has unexpected status '" + status + "'. Hung jobs will be removed, but status will not be updated.", EEventSource.JOB_EXECUTOR);
                            } else {
                                taskRs.updateString("status", ETaskStatus.CANCELLING.getValue().equals(status) ? ETaskStatus.CANCELLED.getValue() : ETaskStatus.FAILED.getValue());
                                taskRs.updateString("faultMess", "Executing job hanged");
                                taskRs.updateTimestamp("finishExecTime", taskRs.getTimestamp("sysdate"));
                                taskRs.updateRow();
                            }
                            jobRs.deleteRow();
                            reportJobRemoved(jobRs.getLong("id"), jobRs.getString("title"), jobRs.getString("processorTitle"), jobRs.getLong("taskId"));
                        }
                    }
                }
                unit.getDbConnection().commit();
            } catch (SQLException | RuntimeException ex) {
                try {
                    unit.getDbConnection().rollback();
                } catch (Exception ex1) {
                    //ignore
                }
                throw ex;
            }
        }
    }

    void processHungJobs() throws SQLException {
        if (unit.getDbConnection() == null) {
            return;
        }
        failHungTasks();
        restartRestartableHungJobs();
        removeNonRestartableHungJobs();
    }

    final void doPriorityBoosting() throws SQLException {
        final Connection dbConnection = unit.getDbConnection();
        if (dbConnection == null) {
            return;
        }
        if (qryDoPriorityBoosting == null) {
            qryDoPriorityBoosting = dbConnection.prepareStatement(qryDoPriorityBoostingSQL);
        }
        qryDoPriorityBoosting.executeUpdate();
        dbConnection.commit();
    }

    final void writeBasicStats() throws SQLException {
        final Connection connection = unit.getDbConnection();
        if (connection != null) {
            if (qryWriteBasicStats == null) {
                qryWriteBasicStats = connection.prepareStatement(qryWriteBasicStatsSQL);
                qryWriteBasicStats.setLong(3, unit.getId());
            }
            qryWriteBasicStats.setDouble(1, ((JobExecutorMonitor) unit.getMonitor()).getAvgExecJobCount());
            qryWriteBasicStats.setDouble(2, ((JobExecutorMonitor) unit.getMonitor()).getAvgWaitJobCount());
            qryWriteBasicStats.execute();
        }
    }

    @Override
    public final void closeAll() {
        closeQry(qryReadOptions);
        qryReadOptions = null;

        closeQry(qryCurJobs);
        qryCurJobs = null;

        closeQry(qryDoPriorityBoosting);
        qryDoPriorityBoosting = null;

        closeQry(qryCurJobsCount);
        qryCurJobsCount = null;

        closeQry(qryWriteBasicStats);
        qryWriteBasicStats = null;

        closeQry(qryGetRestartableHungJobs);
        qryGetRestartableHungJobs = null;

        closeQry(qryGetNonRestartableHungJobs);
        qryGetNonRestartableHungJobs = null;

        closeQry(qryGetHungTaskJobs);
        qryGetHungTaskJobs = null;

        closeQry(qryGetHungTask);
        qryGetHungTask = null;

        closeQry(qryGetAadcMemberJobExecutors);
        qryGetAadcMemberJobExecutors = null;

    }

    @Override
    public void prepareAll() throws SQLException {
        prepareAll(unit.getDbConnection());
    }

    private void closeQry(final PreparedStatement qry) {
        if (qry == null) {
            return;
        }
        try {
            qry.close();
        } catch (SQLException e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            unit.getTrace().put(EEventSeverity.WARNING, Messages.ERR_ON_DB_CONNECTION_CLOSE + ": \n" + exStack, Messages.MLS_ID_ERR_ON_DB_CONNECTION_CLOSE, new ArrStr(unit.getFullTitle(), exStack), unit.getEventSource(), false);
        }
    }

    final class JobIter {

        private final ResultSet rs;

        protected JobIter(final ResultSet rs) {
            this.rs = rs;
        }

        Job next() {
            try {
                while (rs.next()) {
                    try {
                        final Job job = new Job(
                                rs.getLong("ID"),
                                rs.getString("TITLE"),
                                rs.getTimestamp("DUETIME").getTime() - rs.getTimestamp("systs").getTime(),
                                rs.getInt("curPriority"),
                                rs.getString("scpName"),
                                rs.getInt("unlockCount"),
                                rs.getString("threadPoolClassGuid") == null ? null : new ThreadKey(
                                        rs.getString("threadPoolClassGuid"),
                                        rs.getString("threadPoolPid"),
                                        rs.getInt("threadKey")
                                ));
                        final Job alreadyRunningJob = unit.getExecutingJob(job.id);
                        if (alreadyRunningJob != null) {
                            if (alreadyRunningJob.unlockCount == job.unlockCount) {
                                continue;
                            } else {
                                unit.setThreadExecuting(job.getThreadKey(), false);
                            }
                        }
                        return job;
                    } catch (Throwable e) {
                        final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                        unit.getTrace().put(EEventSeverity.ERROR, JobExecutorMessages.ERR_CANT_LOAD_JOB + ": \n" + exStack, JobExecutorMessages.MLS_ID_ERR_CANT_LOAD_JOB, new ArrStr(unit.getFullTitle()), unit.getEventSource(), false);
                    }
                }
            } catch (SQLException e) {
                close();
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                unit.getTrace().put(EEventSeverity.ERROR, JobExecutorMessages.ERR_CANT_LOAD_JOB + ": \n" + exStack, JobExecutorMessages.MLS_ID_ERR_CANT_LOAD_JOB, new ArrStr(unit.getFullTitle()), unit.getEventSource(), false);
            }
            return null;
        }

        void close() {
            try {
                rs.close();
            } catch (SQLException e) {
                //do nothing
            }
        }
    }
}
