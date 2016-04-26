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
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.SSLContext;

import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.ServiceCallException;
import org.radixware.kernel.common.exceptions.ServiceCallTimeout;
import org.radixware.kernel.common.sc.SapClientOptions;
import org.radixware.kernel.common.sc.ServiceClient;
import org.radixware.kernel.common.ssl.CertificateUtils;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.aio.Event;
import org.radixware.kernel.server.aio.EventDispatcher.TimerEvent;
import org.radixware.kernel.server.aio.ServiceManifestServerLoader;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.units.AsyncEventHandlerUnit;
import org.radixware.kernel.server.units.Messages;
import org.radixware.kernel.server.units.UnitDescription;
import org.radixware.kernel.server.units.UnitView;
import org.radixware.schemas.aasWsdl.InvokeDocument;

public final class JobSchedulerUnit extends AsyncEventHandlerUnit {

    private enum EScheduleResult {

        ERROR,
        NORMAL,
        POSTPONED;
    }
    public static final String TASK_TABLE_ID = "tblWZB7K4HLJPOBDCIUAALOMT5GDM";
    static final int DEFAULT_DELAY_BEFORE_AAS_ACTUALIZATION_SEC = 5;
    private static final int SCHEDULING_PERIOD_MILLIS = 10000;
    private static final int SINGLE_ACTUALIZATION_TIMEOUT_SEC = 2;
    private static final int MAX_ACTUALIZATION_DURATION_MILLIS = SCHEDULING_PERIOD_MILLIS - SINGLE_ACTUALIZATION_TIMEOUT_SEC * 1000 - 1000;
    private static final int DURATION_WARNING_INTERVAL_MILLIS = 5 * 60000;
    private static final int WARN_ON_ACTUALIZE_EX_DELAY_MILLIS = 60000;
    //warning generation algoritm relays on SCHEDULING_PERIOD value
    //to generate warning only when there is no chance that warning conditions
    //will disapppear at the next scheduling iteration (still executing task will
    //finish its execution or something). However, there is no gurantee that delay between
    //scheduling iterations will be exactly equals to SCHEDULING_PERIOD, so
    //possible error should be assumed.
    private static final long UPPER_SCHEDULING_DELAY_ESTIMATION_MILLIS = (long) (SCHEDULING_PERIOD_MILLIS * 1.1);
    /**
     * @NotThreadsafe should be accessed only from unit thread
     */
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yy");
    private final DbQueries jobShedDbQueries;
    /**
     * Id of the parent scheduler. If this scheduler has no parent, then it is
     * parent scheduler itself.
     */
    private volatile long parentSchedulerId;
    private final Map<TaskInfo, Long> task2lastWarnTimeMillis = new HashMap<>();
    private final Set<RunningTask> timedOutOnActualize = new HashSet<>();
    private ServiceClient serviceClient;
    private long lastSuccessfulActualizeMillis = 0;

    public JobSchedulerUnit(final Instance instModel, final Long id, final String title) {
        super(instModel, id, title);
        jobShedDbQueries = new DbQueries(this);
    }

    @Override
    protected boolean prepareForStart() throws InterruptedException {
        parentSchedulerId = getInstance().getMainSchedulerUnitId(getId());
        if (parentSchedulerId == -1) {
            postponeStart("Main scheduler unit is not defined");
            return false;
        }
        return true;
    }

    @Override
    protected String getIdForSingletonLock() {
        return "JobSchedulerUnit#" + parentSchedulerId;
    }

    @Override
    protected UnitDescription getStartedDuplicatedUnitDescription() throws SQLException {
        return getInstance().getStartedDuplicatedScheduler(this);
    }

    @Override
    protected boolean startImpl() throws Exception {

        if (super.startImpl()) {
            serviceClient = new ServiceClient(createTracer(), null) {
                private static final long CACHED_SAP_LIVE_TIME_MILLIS = 30000;
                final ServiceManifestServerLoader manifestLoader = new ServiceManifestServerLoader() {
                    @Override
                    protected Connection getDbConnection() {
                        return JobSchedulerUnit.this.getDbConnection();
                    }
                };

                @Override
                protected List<SapClientOptions> refresh(Long systemId, Long currentInstanceId, String serviceUri) throws ServiceCallException {
                    return manifestLoader.readSaps(systemId, currentInstanceId, serviceUri, getScpName(), CACHED_SAP_LIVE_TIME_MILLIS);
                }

                @Override
                protected boolean isSslPossible() {
                    return true;
                }

                @Override
                protected SSLContext prepareSslContext(SapClientOptions sap) throws Exception {
                    return CertificateUtils.prepareServerSslContext(sap.getClientKeyAliases(), sap.getServerCertAliases());
                }
            };
            getDispatcher().waitEvent(new TimerEvent(), this, roundToFiveSeconds(System.currentTimeMillis()));
            lastSuccessfulActualizeMillis = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    @Override
    protected void stopImpl() {
        try {
            serviceClient.close();
        } catch (Throwable t) {
            logErrorOnStop(t);
        }
        super.stopImpl();
    }

    @Override
    protected UnitView newUnitView() {
        return new UnitView(this);
    }

//	TIC event
    @Override
    public void onEvent(final Event ev) {
        if (ev instanceof TimerEvent) {
            final long currentExecMillis = System.currentTimeMillis();
            workIteration();
            if (!isShuttingDown()) {
                getDispatcher().waitEvent(new TimerEvent(), this, roundToFiveSeconds(currentExecMillis + SCHEDULING_PERIOD_MILLIS));
            }
        } else {
            throw new IllegalUsageError("Invalid event " + ev);
        }
    }

    public long getParentSchedulerId() {
        return parentSchedulerId;
    }

    private long roundToFiveSeconds(final long millis) {
        final long millisToTrim = millis + 2500;
        return millisToTrim - millisToTrim % 5000;
    }

//Event source
    @Override
    public String getEventSource() {
        return EEventSource.JOB_SCHEDULER.getValue();
    }

//db 	
    @Override
    protected void setDbConnection(final Connection dbConnection) {
        jobShedDbQueries.closeAll();
        super.setDbConnection(dbConnection);
    }

    final DbQueries getDbQueries() {
        return jobShedDbQueries;
    }

    private void workIteration() {
        schedule();
        try {
            checkExecutionDuration();
        } catch (SQLException ex) {
            final String exStack = ExceptionTextFormatter.throwableToString(ex);
            getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(exStack), getEventSource(), false);
        }
        try {
            actualizeStatuses();
        } catch (SQLException ex) {
            final String exStack = ExceptionTextFormatter.throwableToString(ex);
            getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(exStack), getEventSource(), false);
        }
    }

    /**
     * @NotThreadsafe should be accessed only from unit thread
     *
     */
    private void schedule() {
        Collection<PendingTask> pendingJobs;
        if (!isShuttingDown()) {
            try {
                pendingJobs = getDbQueries().loadPendingJobs();
            } catch (Throwable e) {
                if (e instanceof InterruptedException || Thread.currentThread().isInterrupted()) {
                    return;
                }
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(exStack), getEventSource(), false);
                if (e instanceof SQLException) {
                    try {
                        restoreDbConnection();
                    } catch (InterruptedException ex) {
                        return;
                    }
                }
                return;
            }
            if (pendingJobs.isEmpty()) {
                return;
            }
            getTrace().debug(String.valueOf(pendingJobs.size()) + " tasks must be scheduled, start scheduling", getEventSource(), false);
            try {
                int scheduledCount = 0;
                for (PendingTask job : pendingJobs) {
                    if (isShuttingDown()) {
                        return;
                    }
                    final EScheduleResult scheduleResult = schedule(job);
                    switch (scheduleResult) {
                        case ERROR:
                            return;//wait next TIC
                        case NORMAL:
                            scheduledCount++;
                    }
                }
                getTrace().debug(scheduledCount + " task" + (scheduledCount != 1 ? "s" : "") + " scheduled", getEventSource(), false);
            } catch (InterruptedException e) {
                //return
            }
        }
    }

    private void actualizeStatuses() throws SQLException {
        getTrace().put(EEventSeverity.DEBUG, "Actualize task statuses by related jobs", EEventSource.AAS);
        int actualizaedByRelatedJobs = 0;
        try {
            actualizaedByRelatedJobs = getDbQueries().actualizeTasksByRelatedJobs();
        } finally {
            getTrace().put(EEventSeverity.DEBUG, actualizaedByRelatedJobs + " tasks were actualized by related jobs", EEventSource.AAS);
        }

        getTrace().put(EEventSeverity.DEBUG, "Actualize task statuses via AAS", EEventSource.AAS);
        int actualizedByAas = 0;
        try {
            final long startTimeMillis = System.currentTimeMillis();
            boolean executePreviouslyTimedOut = false;
            final Set<RunningTask> lastTimedOut = new HashSet<>();
            final List<RunningTask> tasksToActualize = new ArrayList<>(getDbQueries().getRunningTasksForAasActualization());
            InvokeDocument invokeDoc = InvokeDocument.Factory.newInstance();
            invokeDoc.addNewInvoke().addNewInvokeRq().addNewParameters().addNewItem().addNewRef().setEntityId("tblWZB7K4HLJPOBDCIUAALOMT5GDM");
            invokeDoc.getInvoke().getInvokeRq().setClassId(Id.Factory.loadFrom("adc2YO4JA7JKJGGBLTARIEQHUSHGI"));
            invokeDoc.getInvoke().getInvokeRq().setMethodId("mthZ4NPQVVAKBHRFDLVCNCTNCZ4RA");
            for (int i = tasksToActualize.size() - 1; i >= 0; i--) {
                if (System.currentTimeMillis() - startTimeMillis > MAX_ACTUALIZATION_DURATION_MILLIS) {
                    break;
                }
                if (timedOutOnActualize.contains(tasksToActualize.get(i)) == executePreviouslyTimedOut) {
                    invokeDoc.getInvoke().getInvokeRq().getParameters().getItemList().get(0).getRef().setPID(String.valueOf(tasksToActualize.get(i).taskId));
                    try {
                        final InvokeDocument respDoc = (InvokeDocument) serviceClient.invokeService(invokeDoc, InvokeDocument.class, 1l, (long) getInstance().getId(), "http://schemas.radixware.org/aas.wsdl", MAX_ACTUALIZATION_DURATION_MILLIS / 1000 + 1, SINGLE_ACTUALIZATION_TIMEOUT_SEC);
                        if (respDoc.getInvoke().getInvokeRs().isSetReturnValue() && respDoc.getInvoke().getInvokeRs().getReturnValue().getBool() == Boolean.TRUE) {

                            getTrace().enterContext(EEventContextType.TASK, tasksToActualize.get(i).taskId, null);
                            try {
                                getTrace().put(EEventSeverity.WARNING, String.format(JobSchedulerMessages.WARN_TASK_HAS_BEEN_ACTUALIZED, tasksToActualize.get(i).getFullTitle()), JobSchedulerMessages.MLS_ID_TASK_HAS_BEEN_ACTUALIZED, new ArrStr(tasksToActualize.get(i).getFullTitle()), EEventSource.JOB_SCHEDULER, false);
                            } finally {
                                getTrace().leaveContext(EEventContextType.TASK, tasksToActualize.get(i).taskId, null);
                            }
                            actualizedByAas++;
                        }
                        lastSuccessfulActualizeMillis = System.currentTimeMillis();
                    } catch (Exception ex) {
                        if (System.currentTimeMillis() - lastSuccessfulActualizeMillis > WARN_ON_ACTUALIZE_EX_DELAY_MILLIS) {
                            getTrace().put(EEventSeverity.WARNING, "Exception on autoactualization of task #" + tasksToActualize.get(i).taskId + " '" + tasksToActualize.get(i).title + "':" + ExceptionTextFormatter.throwableToString(ex), EEventSource.JOB_SCHEDULER);
                            lastSuccessfulActualizeMillis = System.currentTimeMillis();
                        }
                        if (ex instanceof InterruptedException) {
                            return;
                        } else if (ex instanceof ServiceCallTimeout) {
                            lastTimedOut.add(tasksToActualize.get(i));
                        }
                    }
                }
                tasksToActualize.remove(i);
                if (i == 0 && !executePreviouslyTimedOut) {
                    executePreviouslyTimedOut = true;
                    i = tasksToActualize.size();
                }
            }
            timedOutOnActualize.clear();
            timedOutOnActualize.addAll(lastTimedOut);
        } finally {
            getTrace().put(EEventSeverity.DEBUG, actualizedByAas + " tasks were actualized via AAS", EEventSource.AAS);
        }
    }

    private void checkExecutionDuration() throws SQLException {
        final Collection<TaskInfo> curLongRunningTasks = getDbQueries().getLongRunningTasks();
        final Collection<TaskInfo> toRemove = new ArrayList<>();
        for (Map.Entry<TaskInfo, Long> entry : task2lastWarnTimeMillis.entrySet()) {
            boolean found = false;
            for (TaskInfo info : curLongRunningTasks) {
                if (info.id == entry.getKey().id && info.lastSchedulingTimeMillis == entry.getKey().lastSchedulingTimeMillis) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                toRemove.add(entry.getKey());
            }

        }
        for (TaskInfo infoToRemove : toRemove) {
            task2lastWarnTimeMillis.remove(infoToRemove);
        }
        for (TaskInfo info : curLongRunningTasks) {
            Long lastWarnTimeMillis = task2lastWarnTimeMillis.get(info);
            if (lastWarnTimeMillis == null || System.currentTimeMillis() - lastWarnTimeMillis > DURATION_WARNING_INTERVAL_MILLIS) {
                getTrace().enterContext(EEventContextType.TASK, info.id, null);
                try {
                    getTrace().put(EEventSeverity.WARNING, String.format(JobSchedulerMessages.WARN_TASK_IS_RUNNING_FOR_TOO_LONG, info.toString(), info.expectedDurationSec), JobSchedulerMessages.MLS_ID_TASK_IS_RUNNING_FOR_TO_LONG, new ArrStr(info.toString(), String.valueOf(info.expectedDurationSec)), EEventSource.JOB_SCHEDULER.getValue(), false);
                } finally {
                    getTrace().leaveContext(EEventContextType.TASK, info.id, null);
                }
                task2lastWarnTimeMillis.put(info, System.currentTimeMillis());
            }
        }
    }

    /**
     * @NotThreadsafe should be accessed only from unit thread
     */
    EScheduleResult schedule(final PendingTask job) throws InterruptedException {
        try {

            final boolean scheduledOrExecuting = job.status == ETaskStatus.SCHEDULED
                    || job.status == ETaskStatus.EXECUTING
                    || job.status == ETaskStatus.CANCELLING || job.hasRelatedJobs;

            final String jobDescription
                    = "#" + job.taskId
                    + " '" + job.title + "', prevExecTime: "
                    + (job.lastExecTime != null ? timeFormat.format(job.lastExecTime) : "null")
                    + ", curExecTime: "
                    + (job.execTime != null ? timeFormat.format(job.execTime) : null);

            if (scheduledOrExecuting) {
                boolean needWarning = false;
                final boolean expiredJobIsTryingToCatchUp = job.execTime.getTime() < job.schedulingTime.getTime() && job.expiredPolicy == EExpiredJobExecPolicy.ALL.getValue();
                if (job.execTime.getTime() - job.schedulingTime.getTime() < UPPER_SCHEDULING_DELAY_ESTIMATION_MILLIS && (job.lastExecTime != null && job.lastExecTime.getTime() != job.execTime.getTime())) {
                    if (!expiredJobIsTryingToCatchUp) {
                        if (job.status == ETaskStatus.SCHEDULED) {
                            needWarning = true;
                        }
                        if ((job.status == ETaskStatus.EXECUTING || job.status == ETaskStatus.CANCELLING) && !job.skipIfExecuting) {
                            needWarning = true;
                        }
                    }
                }

                if (needWarning) {
                    getTrace().put(EEventSeverity.WARNING, String.format(JobSchedulerMessages.WARN_TASK_IS_ALREADY_SCHEDULED, job.taskId, job.title), JobSchedulerMessages.MLS_ID_TASK_IS_ALREADY_SCHEDULED, new ArrStr(String.valueOf(job.taskId), job.title), EEventSource.JOB_SCHEDULER, false);
                } else {
                    String skipReason = "it has status '" + job.status + "'";
                    if (job.hasRelatedJobs) {
                        skipReason = " and has related jobs";
                    }
                    EEventSeverity severity = EEventSeverity.DEBUG;
                    if (job.status == ETaskStatus.EXECUTING && job.skipIfExecuting) {
                        skipReason = skipReason + " and task.skipIfExecuting is set to true";
                    }
                    if (expiredJobIsTryingToCatchUp) {
                        skipReason = skipReason + " and it is expired and trying to catch up";
                    }
                    if ((job.status == ETaskStatus.SUCCESS
                            || job.status == ETaskStatus.CANCELLED
                            || job.status == ETaskStatus.FAILED
                            || job.status == ETaskStatus.SKIPPED) && job.hasRelatedJobs) {
                        skipReason = skipReason + ", but it's still has related jobs in job queue";
                        severity = EEventSeverity.ERROR;
                    }
                    getTrace().put(severity.getValue(), "Scheduling of the task " + jobDescription + " was postponed, because " + skipReason, null, null, getEventSource(), false);
                }
                return EScheduleResult.POSTPONED;
            }

            getTrace().debug("Try to schedule task " + jobDescription, getEventSource(), false);
            if (!getDbQueries().shedule(job)) {
                getTrace().debug("Can't schedule task " + jobDescription, getEventSource(), false);
                return EScheduleResult.ERROR;
            }
            final String idStr = String.valueOf(job.taskId);
            final String timeStr = timeFormat.format(job.execTime);
            getTrace().put(EEventSeverity.DEBUG, JobSchedulerMessages.SCHD_JOB + " #" + idStr + JobSchedulerMessages._SCHEDULED_TO_BE_EXEC_AT_ + timeStr, JobSchedulerMessages.MLS_ID_SCHD_JOB_SCHEDULED, new ArrStr(idStr, timeStr), getEventSource(), false);
            return EScheduleResult.NORMAL;
        } catch (Throwable e) {
            if (e instanceof InterruptedException || Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(exStack), getEventSource(), false);
            if (e instanceof SQLException) {
                restoreDbConnection();
            }
        }
        return EScheduleResult.ERROR;
    }

    @Override
    public String getUnitTypeTitle() {
        return JobSchedulerMessages.JOB_SCHD_UNIT_TYPE_TITLE;
    }

    @Override
    public Long getUnitType() {
        return EUnitType.JOB_SCHEDULER.getValue();
    }

    @Override
    public boolean isSingletonUnit() {
        return true;
    }

    static class TaskInfo {

        public final long id;
        public final String title;
        public final int expectedDurationSec;
        public final long lastSchedulingTimeMillis;

        public TaskInfo(long id, String title, int expectedDurationSec, long lastSchedulingTimeMillis) {
            this.id = id;
            this.title = title;
            this.expectedDurationSec = expectedDurationSec;
            this.lastSchedulingTimeMillis = lastSchedulingTimeMillis;
        }

        @Override
        public String toString() {
            return "#" + id + " '" + title + "'";
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 83 * hash + (int) (this.id ^ (this.id >>> 32));
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
            final TaskInfo other = (TaskInfo) obj;
            if (this.id != other.id) {
                return false;
            }
            return true;
        }
    }
}
