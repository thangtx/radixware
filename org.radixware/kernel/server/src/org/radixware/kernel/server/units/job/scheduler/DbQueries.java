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

package org.radixware.kernel.server.units.job.scheduler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.enums.EEventContextType;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.ETaskStatus;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.IDbQueries;
import org.radixware.kernel.server.units.Messages;
import org.radixware.kernel.server.units.job.scheduler.JobSchedulerUnit.TaskInfo;

final class DbQueries implements IDbQueries {

    private final JobSchedulerUnit unit;
    private PreparedStatement qrySchedule = null;
    private PreparedStatement qryJobs2Schedule = null;
    private PreparedStatement qryGetLongRunningTasks = null;
    private PreparedStatement qryActualizeByRelatedJobs = null;
    private PreparedStatement qryGetRunningTasksForAasActualization = null;

    DbQueries(final JobSchedulerUnit unit) {
        this.unit = unit;
    }

    List<RunningTask> getRunningTasksForAasActualization() throws SQLException {
        if (unit.getDbConnection() == null) {
            return Collections.emptyList();
        }
        if (qryGetRunningTasksForAasActualization == null) {
            qryGetRunningTasksForAasActualization = unit.getDbConnection().prepareStatement("select "
                    + "t.id, "
                    + "(sysdate - greatest(t.lastSchedulingTime, t.lastExecTime)) * 86400 secSinceExpectedStart, "
                    + "t.expectedDurationSec,"
                    + "t.lastSchedulingTime,"
                    + "t.title "
                    + "from rdx_js_task t "
                    + "where (t.isActualizableByRelatedJobs = 0 or parentId is not null) "
                    + "and t.status in ('" + ETaskStatus.SCHEDULED.getValue() + "', '" + ETaskStatus.EXECUTING.getValue() + "', '" + ETaskStatus.CANCELLING.getValue() + "') "
                    + "and ((sysdate - greatest(nvl(t.lastSchedulingTime, t.lastExecTime), t.lastExecTime)) * 86400) > nvl(t.expectedDurationSec, " + JobSchedulerUnit.DEFAULT_DELAY_BEFORE_AAS_ACTUALIZATION_SEC + ") "
                    + "and nvl2(t.parentId, (select t1.unitId rootUnitId from rdx_js_task t1 where parentId is null start with t1.id=t.id connect by prior t1.parentId = t1.id ), t.unitId) = ?");
            qryGetRunningTasksForAasActualization.setLong(1, unit.getParentSchedulerId());
        }
        final List<RunningTask> result = new ArrayList<>();
        try (final ResultSet rs = qryGetRunningTasksForAasActualization.executeQuery()) {
            while (rs.next()) {
                long expectedDurationSec = rs.getLong("expectedDurationSec");
                if (rs.wasNull()) {
                    expectedDurationSec = -1;
                }
                result.add(new RunningTask(rs.getLong("id"), rs.getString("title"), rs.getLong("secSinceExpectedStart"), expectedDurationSec, rs.getTimestamp("lastSchedulingTime") == null ? -1 : rs.getTimestamp("lastSchedulingTime").getTime()));
            }
        }
        return result;
    }

    int actualizeTasksByRelatedJobs() throws SQLException {
        if (unit.getDbConnection() == null) {
            return 0;
        }
        if (qryActualizeByRelatedJobs == null) {
            qryActualizeByRelatedJobs = unit.getDbConnection().prepareStatement("select t.status, t.faultMess, t.id, t.title, t.finishExecTime, sysdate from rdx_js_task t "
                    + "where t.status in ('" + ETaskStatus.SCHEDULED.getValue() + "', '" + ETaskStatus.EXECUTING.getValue() + "', '" + ETaskStatus.CANCELLING.getValue() + "') "
                    + "and t.isActualizableByRelatedJobs <> 0 "
                    + "and t.parentId is null "
                    + "and not exists (select 1 from rdx_js_jobqueue where taskId = t.id) "
                    + "and nvl2(t.parentId, (select t1.unitId rootUnitId from rdx_js_task t1 where parentId is null start with t1.id=t.id connect by prior t1.parentId = t1.id ), t.unitId) = ?"
                    + " for update of t.status skip locked", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            qryActualizeByRelatedJobs.setLong(1, unit.getParentSchedulerId());
        }
        int result = 0;

        try (ResultSet rs = qryActualizeByRelatedJobs.executeQuery()) {
            while (rs.next()) {
                rs.updateString("status", ETaskStatus.CANCELLED.getValue());
                rs.updateString("faultMess", "Cancelled by actualization (DB)");
                rs.updateTimestamp("finishExecTime", rs.getTimestamp("sysdate"));
                rs.updateRow();
                final long taskId = rs.getLong("id");
                final String taskFullTitle = "#" + taskId + " '" + rs.getString("title") + "'";
                unit.getTrace().enterContext(EEventContextType.TASK, taskId, null);
                try {
                    unit.getTrace().put(EEventSeverity.WARNING, String.format(JobSchedulerMessages.WARN_TASK_HAS_BEEN_ACTUALIZED, taskFullTitle), JobSchedulerMessages.MLS_ID_TASK_HAS_BEEN_ACTUALIZED, new ArrStr(taskFullTitle), EEventSource.JOB_SCHEDULER, false);
                } finally {
                    unit.getTrace().leaveContext(EEventContextType.TASK, taskId, null);
                }
            }
            unit.getDbConnection().commit();
        } catch (SQLException | RuntimeException ex) {
            try {
                unit.getDbConnection().rollback();
            } catch (Exception ex1) {
                //ignore
            }
        }
        return result;
    }

    final Collection<TaskInfo> getLongRunningTasks() throws SQLException {
        if (qryGetLongRunningTasks == null) {
            qryGetLongRunningTasks = unit.getDbConnection().prepareStatement("select "
                    + "id, title, expectedDurationSec, lastSchedulingTime "
                    + "from rdx_js_task "
                    + "where "
                    + "unitId = ? "
                    + "and isActive <> 0 "
                    + "and lastSchedulingTime is not null "
                    + "and status in ("
                    + "'" + ETaskStatus.EXECUTING.getValue() + "', "
                    + "'" + ETaskStatus.SCHEDULED.getValue() + "', "
                    + "'" + ETaskStatus.CANCELLING.getValue() + "') "
                    + "and expectedDurationSec is not null "
                    + "and (sysdate - greatest(lastSchedulingTime, lastExecTime)) * 86400 > expectedDurationSec");
            qryGetLongRunningTasks.setLong(1, unit.getParentSchedulerId());
        }
        final List<TaskInfo> result = new ArrayList<>();
        try (ResultSet rs = qryGetLongRunningTasks.executeQuery()) {
            while (rs.next()) {
                result.add(new TaskInfo(rs.getLong("id"), rs.getString("title"), rs.getInt("expectedDurationSec"), rs.getTimestamp("lastSchedulingTime").getTime()));
            }
        }
        return result;
    }

    final Collection<PendingTask> loadPendingJobs() throws SQLException {
        if (qryJobs2Schedule == null) {
            final Connection dbConnection = unit.getDbConnection();
            if (dbConnection == null) {
                return Collections.emptyList();
            }
            qryJobs2Schedule = dbConnection.prepareStatement(
                    "select selectedTaskId, status, title, lastexectime, skipIfExecuting, expiredPolicy, sysdate, isActualizableByRelatedJobs, "
                    + "rdx_js_job.adjustToNearestMinute(exectime) realexectime, "
                    + "(select count(*) from rdx_js_jobqueue jq where jq.taskId = selectedTaskId and rownum < 2) hasScheduledJobs "
                    + "from ( "
                    + "select "
                    + "id selectedTaskId, "
                    + "status, "
                    + "skipIfExecuting, "
                    + "title, "
                    + "expiredPolicy, "
                    + "scheduleid, "
                    + "isActualizableByRelatedJobs, "
                    + "decode(expiredPolicy,"
                    + "0, rdx_js_eventschedule.NEXT(scheduleid, greatest(sysdate, nvl(lastexectime, sysdate))),"
                    + "1, nvl2(nextPlannedTime, greatest(nextPlannedTime, nvl(rdx_js_eventschedule.prev(scheduleid, sysdate), nextPlannedTime)), null),"
                    + "2, nextPlannedTime) exectime, "
                    + "lastexectime "
                    + "from "
                    + "(select rdx_js_task.*, "
                    + "rdx_js_eventschedule.NEXT(scheduleid, nvl(lastexectime, createTime)) nextPlannedTime "
                    + "from rdx_js_task "
                    + "where "
                    + "unitid = ? and isActive <> 0 and parentId is null)"
                    + ") where "
                    + "(lastexectime is null or exectime <> lastexectime) and "
                    + "exectime < sysdate + 1/24/60"// next min
                    );
            qryJobs2Schedule.setLong(1, unit.getParentSchedulerId());
        }
        final ResultSet rs = qryJobs2Schedule.executeQuery();
        try {
            final Collection<PendingTask> jobs = new ArrayList<>(100);
            while (rs.next()) {
                final long taskId = rs.getLong("selectedTaskId");
                ETaskStatus status = null;
                try {
                    status = ETaskStatus.getForValue(rs.getString("status"));
                } catch (NoConstItemWithSuchValueError ex) {
                    //null
                }
                jobs.add(new PendingTask(
                        taskId,
                        rs.getString("title"),
                        rs.getTimestamp("realexectime"),
                        rs.getTimestamp("lastexectime"),
                        rs.getTimestamp("sysdate"),
                        status,
                        rs.getLong("hasScheduledJobs") > 0,
                        rs.getBoolean("isActualizableByRelatedJobs"),
                        rs.getBoolean("skipIfExecuting"),
                        rs.getLong("expiredPolicy")));
            }
            return Collections.unmodifiableCollection(jobs);
        } finally {
            rs.close();
        }
    }

    final boolean shedule(final PendingTask job) throws SQLException {
        final Connection dbConnection = unit.getDbConnection();
        if (dbConnection == null) {
            return false;
        }
        if (qrySchedule == null) {
            qrySchedule = dbConnection.prepareStatement("begin rdx_js_job.shedulePeriodical(?,?); end;");
        }
        qrySchedule.setLong(1, job.taskId);
        qrySchedule.setTimestamp(2, job.execTime);
        qrySchedule.execute();
        dbConnection.commit();
        return true;
    }

//close	
    @Override
    public final void closeAll() {
        if (qryJobs2Schedule != null) {
            try {
                qryJobs2Schedule.close();
            } catch (SQLException e) {
                onCloseEx(e);
            } finally {
                qryJobs2Schedule = null;
            }
        }
        if (qrySchedule != null) {
            try {
                qrySchedule.close();
            } catch (SQLException e) {
                onCloseEx(e);
            } finally {
                qrySchedule = null;
            }
        }
        if (qryGetLongRunningTasks != null) {
            try {
                qryGetLongRunningTasks.close();
            } catch (Exception e) {
                onCloseEx(e);
            } finally {
                qryGetLongRunningTasks = null;
            }
        }
        if (qryGetRunningTasksForAasActualization != null) {
            try {
                qryGetRunningTasksForAasActualization.close();
            } catch (Exception e) {
                onCloseEx(e);
            } finally {
                qryGetRunningTasksForAasActualization = null;
            }
        }
        if (qryActualizeByRelatedJobs != null) {
            try {
                qryActualizeByRelatedJobs.close();
            } catch (Exception e) {
                onCloseEx(e);
            } finally {
                qryActualizeByRelatedJobs = null;
            }
        }
    }

    private void onCloseEx(Exception e) {
        final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
        unit.getTrace().put(EEventSeverity.WARNING, Messages.ERR_ON_DB_CONNECTION_CLOSE + ": \n" + exStack, Messages.MLS_ID_ERR_ON_DB_CONNECTION_CLOSE, new ArrStr(unit.getFullTitle(), exStack), unit.getEventSource(), false);
    }
}
