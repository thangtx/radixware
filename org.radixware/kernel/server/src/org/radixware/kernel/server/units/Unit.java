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
package org.radixware.kernel.server.units;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.SwingUtilities;
import org.radixware.kernel.server.exceptions.InvalidUnitState;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.trace.ServerTrace;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.LogFactory;
import org.radixware.kernel.common.enums.EEventContextType;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.trace.TraceItem;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.server.SrvRunParams;
import org.radixware.kernel.server.aio.ServiceManifestLoader;
import org.radixware.kernel.server.aio.ServiceManifestServerLoader;
import org.radixware.kernel.server.instance.DbConnectionResourceItem;
import org.radixware.kernel.server.instance.ResourceRegistry;
import org.radixware.kernel.server.instance.UnitCommand;
import org.radixware.kernel.server.instance.UnitCommandResponse;
import org.radixware.kernel.server.instance.aadc.AadcManager;
import org.radixware.kernel.server.jdbc.RadixConnection;
import org.radixware.kernel.server.monitoring.AbstractMonitor;
import org.radixware.kernel.server.monitoring.MonitorFactory;
import org.radixware.kernel.server.monitoring.MonitoringUtils;
import org.radixware.kernel.server.trace.FileLogOptions;
import org.radixware.kernel.server.trace.TraceProfiles;

public abstract class Unit {

    public static final String UNIT_INTERRUPTION_ALLOWED_PROPERTY_NAME = "rdx.unit.interruption.allowed";
    private static final int WAIT_NORMAL_STOP_ON_ABORT_MILLIS = SystemPropUtils.getIntSystemProp("rdx.unit.wait.normal.stop.on.abort.millis", 5000);
    private static final int WAIT_STOP_ON_ABORT_AFTER_RESOURCE_RELEASE_MILLIS = SystemPropUtils.getIntSystemProp("rdx.unit.wait.stop.on.abort.after.resource.release.millis", 5000);
    private static final int OPTIONS_REREAD_PERIOD_MILLIS = 60 * 1000;//1 min
    public static final int DB_I_AM_ALIVE_PERIOD_MILLIS = 60 * 1000;//1 min
    public static final int DG_I_AM_ALIVE_PERIOD_MILLIS = 30 * 1000;//0.5 min
    public static final int DB_I_AM_ALIVE_TIMEOUT_MILLIS = 3 * DB_I_AM_ALIVE_PERIOD_MILLIS;
    private static final int SHUTDOWN_TIMEOUT_MILLIS = 60000;//1 min  
    private static final int INTERRUPT_TIMEOUT_MILLIS = 3 * 60000;//3 min;
    private static final int DB_RECONNECT_TRY_PERIOD_MILLIS = 1 * 1000;//1 sec
    private static final int MAX_DB_RECONNECT_TRY_ATTEMPTS = 1;
    static final long DELAY_BEFORE_START_AFTER_FAILURE = 5000;
    private static final int MONITORING_SETTINGS_REREAD_PERIOD_MILLIS = 60000;
    private static final int MONITORING_FLUSH_PERIOD_MILLIS = 30000;
    private static final int STATUS_IN_VIEW_UPDATE_PERIOD_MILLIS = 500;
    private static final int CPU_USAGE_UPDATE_PERIOD_MILLIS = 1000;
    private volatile boolean bShuttingDown = false;
    private volatile boolean bShutdownRequested = false;
    private volatile String postponeStartAfterStopReason;
    private volatile String lastPostponeStartReason = null;
    private volatile long timeForPostponedStartMillis = -1;
    private volatile long lastStoppingStateTimeMillis = -1;
    private final UnitsDbQueries dbQueries;
    private volatile RadixConnection dbConnection = null;
    private final Object controlSem = new Object();
    private AbstractMonitor monitor;
    private final MonitorFactory<AbstractMonitor, Unit> monitorFactory;
    private final long id;
    private final List<UnitListener> listeners;
    private final ServerTrace trace;
    private volatile CommonOptions commonOptions = null;
    private volatile String scpName = null;
    private final String title;
    private volatile UnitThread thread = null;
    private volatile UnitState state = UnitState.STOPPED;
    private final ArrayBlockingQueue<UnitCommand> commandsQueue = new ArrayBlockingQueue<>(1000);
    private ServiceManifestLoader serviceManifestLoader;
    private volatile int cpuUsagePercent = -1;
    private long lastCpuUsedNanos = -1;
    private long lastCpuUsageUpdateNanos = -1;
    private volatile boolean aborted;
    private long primaryUnitId = -1;
    private boolean postponedDueToDuplicate;
    private String acquiredSingletoneUnitLockId = null;
    private final SimpleDateFormat timestampFormat = new SimpleDateFormat(TraceItem.getTimeFormat());
    private final AadcManager aadcManager;
    private int failedDbReconnectAttempts = 0;

    protected Unit() {   // Use this constructor for testing only!
        if (!SrvRunParams.isInTestEnvironment()) {
            throw new IllegalStateException("Attempt to use Unit() in the production environment! It need be used only for the test purposes!");
        } else {
            trace = null;
            instance = null;
            this.id = -1;
            listeners = new CopyOnWriteArrayList<>();
            dbQueries = null;
            this.title = "";
            this.monitorFactory = null;
            aadcManager = null;
        }
    }

    protected Unit(final Instance instModel, final Long id, final String title, final MonitorFactory<AbstractMonitor, Unit> monitorFactory) {
        trace = ServerTrace.Factory.newInstance(instModel, "SystemUnit[" + id + "]");
        instance = instModel;
        this.id = id.longValue();
        listeners = new CopyOnWriteArrayList<>();
        dbQueries = new UnitsDbQueries(this);
        this.title = title;
        this.monitorFactory = monitorFactory;
        this.aadcManager = instModel.getAadcManager();
    }

    protected Unit(final Instance instModel, final Long id, final String title) {
        this(instModel, id, title, null);
    }

    public BigDecimal getPriority() {
        if (getUnitType() == null) {
            return null;
        }
        return Factory.getUnitLoadOrderByType().get(getUnitType());
    }

    public boolean enqueueUnitCommand(final UnitCommand command) {
        if (commandsQueue.offer(command)) {
            return true;
        } else {
            return false;
        }
    }

    public Callable<Boolean> getThisRunAliveChecker() {
        return new Callable<Boolean>() {
            private final UnitThread unitThread = thread;

            @Override
            public Boolean call() throws Exception {
                return unitThread.isAlive() && !unitThread.isAborted();
            }

        };
    }

    protected AadcManager getAadcManager() {
        return aadcManager;
    }

    public String getResourceKeyPrefix() {
        return "Unit[" + getId() + "]";
    }

    protected Object getControlSemaphore() {
        return controlSem;
    }

    public AbstractMonitor getMonitor() {
        return monitor;
    }

    protected String getIdForSingletonLock() {
        return getUnitType().toString();
    }

    public long getPrimaryUnitId() {
        return primaryUnitId;
    }

    public String formatTimestamp(final long timeMillis) {
        return timestampFormat.format(new Date(timeMillis));
    }

    protected boolean prepareForStart() throws Exception {
        if (isSingletonUnit() && isSingletonByPrimary()) {
            primaryUnitId = getInstance().getPrimaryUnitId(getId());
            if (primaryUnitId <= 0) {
                throw new IllegalStateException("Unable to determine primary unit for '" + getTitle() + "'");
            }
        }

        return true;
    }

    protected UnitDescription getStartedDuplicatedUnitDescription() throws SQLException {
        if (isSingletonByPrimary()) {
            return instance.getStartedUnitIdByPrimary(getPrimaryUnitId());
        }
        return instance.getStartedUnitIdByTypeAndAadcMember(getUnitType());
    }

    public void releaseAcquiredSingletoneUnitLock() {
        synchronized (controlSem) {
            if (acquiredSingletoneUnitLockId != null) {
                instance.getSingletonUnitLock().releaseLock(acquiredSingletoneUnitLockId);
                acquiredSingletoneUnitLockId = null;
            }
        }
    }

    private boolean acquireSingletoneUnitLock(final String lockId) {
        synchronized (controlSem) {
            if (instance.getSingletonUnitLock().lock(lockId)) {
                acquiredSingletoneUnitLockId = lockId;
                return true;
            }
            return false;
        }
    }

    public final boolean start(final String reason) throws InterruptedException {
        synchronized (controlSem) {
            if (getState() == UnitState.STARTED || getState() == UnitState.STARTING) {
                return true;
            }
            if (getState() != UnitState.STOPPED && getState() != UnitState.START_POSTPONED) {
                throw new InvalidUnitState(Messages.UNIT_IS_NOT_STOPPED);
            }
            timeForPostponedStartMillis = -1;
            postponedDueToDuplicate = false;
            boolean preparedForStart = false;
            try {
                preparedForStart = prepareForStart();
            } catch (Exception ex) {
                postponeStart("Unable to prepare for start: " + ex.getMessage(), System.currentTimeMillis() + DELAY_BEFORE_START_AFTER_FAILURE);
                if (ex instanceof RuntimeException) {
                    throw (RuntimeException) ex;
                }
                throw new IllegalStateException("Unable to prepare unit '" + getTitle() + "' for start", ex);
            }
            if (preparedForStart) {
                if (isSingletonUnit()) {
                    final SingletoneStartCheckResult checkResult = checkSingletoneStart();
                    if (checkResult.getPostponeStartReason() != null) {
                        releaseAcquiredSingletoneUnitLock();
                        if (checkResult.startedUnit != null) {
                            postponedDueToDuplicate = true;
                        }
                        postponeStart(checkResult.getPostponeStartReason(), checkResult.getPlannedRestartTimeMillis());
                        return false;
                    }
                }
                getView();
                thread = new UnitThread(this);
                bShuttingDown = false;
                bShutdownRequested = false;
                setState(UnitState.STARTING, reason);
                try {
                    thread.start();
                } catch (RuntimeException ex) {
                    //some problem during thread start
                    postponeStart("Unable to start unit's #" + id + " thread: " + ExceptionTextFormatter.throwableToString(ex));
                    return false;
                }
                return true;
            } else {
                return false;
            }
        }
    }

    protected SingletoneStartCheckResult checkSingletoneStart() {
        final int fastRestartDelayMillis = 10000;

        try {
            UnitDescription startedUnitDescFastCheck = getStartedDuplicatedUnitDescription();
            if (startedUnitDescFastCheck != null) {
                return new SingletoneStartCheckResult(getDuplicatedUnitExistsReason(startedUnitDescFastCheck), -1, startedUnitDescFastCheck);
            }
        } catch (SQLException ex) {
            return new SingletoneStartCheckResult(ExceptionTextFormatter.throwableToString(ex), System.currentTimeMillis() + fastRestartDelayMillis, null);
        }

        final String lockId = getIdForSingletonLock();
        if (acquireSingletoneUnitLock(lockId)) {
            UnitDescription startedUnitDesc = null;
            String postponeStartReason = null;
            try {
                startedUnitDesc = getStartedDuplicatedUnitDescription();
                if (startedUnitDesc != null) {
                    postponeStartReason = getDuplicatedUnitExistsReason(startedUnitDesc);
                }
            } catch (SQLException e) {
                return new SingletoneStartCheckResult(ExceptionTextFormatter.exceptionStackToString(e), System.currentTimeMillis() + fastRestartDelayMillis, startedUnitDesc);
            }
            if (postponeStartReason != null) {
                return new SingletoneStartCheckResult(postponeStartReason, -1, startedUnitDesc);
            }
            return new SingletoneStartCheckResult(null, -1, null);
        } else {
            return new SingletoneStartCheckResult("Unable to acquire singleton unit lock '" + lockId + "' for unit #" + id, -1, null);
        }
    }

    private String getDuplicatedUnitExistsReason(final UnitDescription startedUnitDesc) {
        return String.format(Messages.UNIT_ID_IS_ALREADY_RUNNING, startedUnitDesc.getUnitId(), startedUnitDesc.getInstanceId(), startedUnitDesc.getInstanceTitle());
    }

    public boolean isPostponedStartRequiredImmediately() {
        final boolean result = (timeForPostponedStartMillis > 0 && timeForPostponedStartMillis < System.currentTimeMillis());
        return result;
    }

    public String getLastPostponeStartReason() {
        return lastPostponeStartReason;
    }

    public final void postponeStart(final String reason) throws InterruptedException {
        postponeStart(reason, -1);
    }

    /**
     * Synchronous request for postponed start.
     *
     * @param reason reason for postpone. Should always contain meaningful
     * information.
     * @param startTimeMillis time at which unit must be started (absolute, in
     * milliseconds). By default, attempt to start postponed unit is taken each
     * minute. In case unit wants to restart earlier, it can request it via this
     * parameter.
     * @throws InterruptedException
     */
    public final void postponeStart(final String reason, final long startTimeMillis) throws InterruptedException {
        synchronized (controlSem) {
            if (getState() != UnitState.STOPPED && getState() != UnitState.START_POSTPONED) {
                throw new InvalidUnitState(Messages.UNIT_IS_NOT_STOPPED);
            }
            setState(UnitState.START_POSTPONED, reason);
            final String reasonExplanation = (reason != null ? " (" + reason + ")" : "");
            if (postponedDueToDuplicate) {
                final String floodKey = getPostponedDueToDuplicateFloodKey();
                getInstance().getTrace().setFloodPeriod(floodKey, 60 * 60000);
                getInstance().getTrace().putFloodControlled(floodKey, EEventSeverity.EVENT, String.format(Messages.UNIT_START_POSPONED, "'" + getFullTitle() + "'") + reasonExplanation, Messages.MLS_ID_UNIT_START_POSPONED_WITH_REASON, new ArrStr(getFullTitle(), reason), EEventSource.UNIT.getValue(), -1, false, null);
            } else {
                getInstance().getTrace().put(EEventSeverity.EVENT, String.format(Messages.UNIT_START_POSPONED, "'" + getFullTitle() + "'") + reasonExplanation, Messages.MLS_ID_UNIT_START_POSPONED_WITH_REASON, new ArrStr(getFullTitle(), reason), EEventSource.UNIT, false);
            }
            lastPostponeStartReason = reason;
            if (startTimeMillis != -1) {
                if (timeForPostponedStartMillis == -1) {
                    timeForPostponedStartMillis = startTimeMillis;
                } else {
                    timeForPostponedStartMillis = Math.min(timeForPostponedStartMillis, startTimeMillis);
                }
            }
        }
    }

    protected String getPostponedDueToDuplicateFloodKey() {
        return "UnitPostponedDueToDuplicate[" + getId() + "]";
    }

    /**
     * @return true if shutdown process was started.
     */
    public final boolean isShuttingDown() {
        return bShuttingDown;
    }

    /**
     * stop() for stopped unit does nothing
     *
     * @return
     * @throws InterruptedException
     */
    public boolean stop(final String reason) throws InterruptedException {
        synchronized (controlSem) {
            if (getState() == UnitState.START_POSTPONED) {
                setStopForDead("stop in START POSTPONED state");
                return true;
            }

            if (getState() == UnitState.STOPPED) {
                return true;
            }

            final String reasonSuffix = getReasonSuffix(reason);
            getTrace().put(EEventSeverity.EVENT, Messages.TRY_SHUTDOWN_UNIT + reasonSuffix, Messages.MLS_ID_TRY_SHUTDOWN_UNIT, new ArrStr(getFullTitle() + reasonSuffix), getEventSource(), false);

            final String stopDeadReason = "set STOPPED status in database for dead unit";

            if (thread == null || thread.getState() == Thread.State.TERMINATED) {
                setStopForDead(stopDeadReason);
                return true;
            }

            //soft stop
            requestShutdown();
            try {
                thread.join(SHUTDOWN_TIMEOUT_MILLIS);
            } catch (Throwable e) {
                logErrorOnStop(e);
            }
            if (thread.getState() == Thread.State.TERMINATED) {
                setStopForDead(stopDeadReason);
                return true;
            }

            //hard stop
            thread.interrupt();
            thread.join(INTERRUPT_TIMEOUT_MILLIS);
            return getState() == UnitState.STOPPED;
        }
    }

    public boolean abortAndUnload(final String reason) {
        synchronized (controlSem) {
            try {
                getTrace().put(EEventSeverity.ERROR, "Aborting unit '" + getTitle() + "', reason: " + reason, null, null, EEventSource.UNIT.getValue(), false);
                getTrace().flush();
            } catch (Exception ex) {
                //ignore
            }

            if (thread != null) {
                requestShutdown();

                try {
                    thread.join(WAIT_NORMAL_STOP_ON_ABORT_MILLIS);
                } catch (InterruptedException ex) {
                    //ignore
                }
            }

            getInstance().getResourceRegistry().closeAllWithKeyPrefix(getResourceKeyPrefix(), "unit abort, reason: " + reason);

            if (thread != null) {
                thread.setAborted(true);
                requestShutdown();
                thread.interrupt();

                try {
                    thread.join(WAIT_STOP_ON_ABORT_AFTER_RESOURCE_RELEASE_MILLIS);
                } catch (InterruptedException ex) {
                    //ignore
                }
            }

            try {
                trace.stopFileLogging();
            } catch (Exception ex) {
                //ignore
            }

            setStopForDead("settings STOPPED status in db for dead unit");

            if (SrvRunParams.getIsGuiOn() && getViewNoCreate() != null) {
                getViewNoCreate().dispose();
            }
            aborted = true;
            getInstance().unloadUnit(this);
            getInstance().getTrace().put(EEventSeverity.ERROR, "Unit '" + getTitle() + "' aborted and unloaded, reason: " + reason, null, null, EEventSource.INSTANCE.getValue(), false);
            return true;
        }
    }

    public boolean isAborted() {
        return aborted;
    }

    private void setStopForDead(final String reason) {
        Connection connection = null;
        try {
            try {
                connection = instance.openNewUnitDbConnection(this);
                setDbConnection((RadixConnection) connection);
                try (PreparedStatement ps = connection.prepareStatement("select (started+postponed) from rdx_unit where id=?")) {
                    ps.setLong(1, getId());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next() && rs.getBoolean(1)) {
                            setState(UnitState.STOPPED, reason);
                        }
                    }
                }
            } catch (Throwable t) {
                //ignore
            } finally {
                try {
                    setDbConnection(null);
                } catch (Throwable t) {
                    //ignore
                }
            }
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Throwable t) {
                    //ignore
                }
            }
        }
    }

    protected void logErrorOnStop(final Throwable ex) {
        final String exStack = ExceptionTextFormatter.exceptionStackToString(ex);
        getTrace().put(EEventSeverity.ERROR, Messages.ERR_ON_UNIT_SHUTDOWN + exStack, Messages.MLS_ID_ERR_ON_UNIT_SHUTDOWN, new ArrStr(getFullTitle(), exStack), getEventSource(), false);
    }

    protected void logOptionsChanged(final String optionsStr) {
        getTrace().put(EEventSeverity.EVENT, Messages.OPTIONS_CHANGED + optionsStr, Messages.MLS_ID_OPTIONS_CHANGED, new ArrStr(getFullTitle(), optionsStr), getEventSource(), false);
    }

    public final boolean restart(final String reason) throws InterruptedException {
        synchronized (controlSem) {
            if (stop(reason)) {
                return start(reason);
            }
            return false;
        }
    }

// overridable implementation of instance thread run():  try {if (startImpl()) {runImpl();}}finally{stopImpl()}
    /**
     * Called only from UnitThread if true is returned then runImpl() and
     * stopImpl() are called if Exception is thrown then it is traced and
     * stopImpl() is called if false is returned then stopImpl() is called and
     * unit start is postponed
     *
     * @throws Exception
     */
    protected boolean startImpl() throws Exception {
        postponeStartAfterStopReason = null;
        failedDbReconnectAttempts = 0;
        getInstance().getResourceRegistry().closeAllWithKeyPrefix(getResourceKeyPrefix(), "unit start");
        final Connection connection = instance.openNewUnitDbConnection(this);
        try {
            if (monitorFactory != null) {
                monitor = monitorFactory.createMonitor(this);
            }
            setDbConnection((RadixConnection) connection);
            if (monitor != null) {
                monitor.rereadSettings();
            }
            commandsQueue.clear();
            rereadCommonOptions();

            if (instance.isAadcTestedMember() && commonOptions.useInAadcTestMode == false) {
                throw new IllegalStateException("Unable to start unit not configured to use in AADC test mode in AADC test mode");
            }
            if (!instance.isAadcTestedMember() && commonOptions.useInAadcTestMode == true) {
                throw new IllegalStateException("Unable to start unit configured to use in AADC test mode not in AADC test mode");
            }

            final FileLogOptions fileLogOptions = getFileLogOptions();
            getTrace().startFileLogging(fileLogOptions);
            getTrace().clearContextStack();
            getTrace().enterContext(EEventContextType.SYSTEM_UNIT, Long.valueOf(id), null);
            getTrace().put(EEventSeverity.EVENT, Messages.FILE_TRACE_OPTIONS_CHANGED + "\"" + String.valueOf(fileLogOptions) + "\"", Messages.MLS_ID_FILE_TRACE_OPTIONS_CHANGED, new ArrStr(getFullTitle(), String.valueOf(fileLogOptions)), getEventSource(), false);

            serviceManifestLoader = new ServiceManifestServerLoader() {

                @Override
                protected Connection getDbConnection() {
                    return Unit.this.getDbConnection();
                }
            };

            getTrace().clearFloodSettingsAndStats();

            return true;
        } catch (Exception e) {
            try {
                connection.close();
            } catch (SQLException ex2) {
                //do nothing
            }
            throw e;
        }
    }

    public void requestPostponeStartAfterStop(final String postponeStartReason) {
        postponeStartAfterStopReason = postponeStartReason == null ? "unknown" : postponeStartReason;
    }

    public boolean isPostponeStartAfterStopRequested() {
        return postponeStartAfterStopReason != null;
    }

    public String getPostponeStartAfterStopReason() {
        return postponeStartAfterStopReason;
    }

    public ServiceManifestLoader getUnitServiceManifestLoader() {
        return serviceManifestLoader;
    }

    public FileLogOptions getFileLogOptions() {
        final FileLogOptions instOpt = getInstance().getFileLogOptions();
        if (instOpt == null) {
            return null;
        }
        return new FileLogOptions(
                new File(new File(instOpt.getDir(), "units"), "unit_#" + getId()),
                "unit_#" + getId(),
                instOpt.getMaxFileSizeBytes(),
                instOpt.getRotationCount(),
                instOpt.isRotateDaily(),
                instOpt.isWriteContextToFile());
    }

    public void applyNewFileLogOptions() {
        final FileLogOptions newOptions = getFileLogOptions();
        getTrace().changeFileLoggingOptions(newOptions);
        getTrace().put(EEventSeverity.EVENT, Messages.FILE_TRACE_OPTIONS_CHANGED + "\"" + String.valueOf(newOptions) + "\"", Messages.MLS_ID_FILE_TRACE_OPTIONS_CHANGED, new ArrStr(getFullTitle(), String.valueOf(newOptions)), getEventSource(), false);
    }

    /**
     * Called only from UnitThread
     *
     * @throws Exception
     */
    protected final void runImpl() throws InterruptedException {
        while (!isShuttingDown()) {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            maintenance();
        }
    }

    protected final void requestShutdown() {
        bShutdownRequested = true;
        if (requestShutdownImpl()) {
            setShutdownStarted();
        }
    }

    protected final void setShutdownStarted() {
        bShuttingDown = true;
    }

    /**
     * @return true if shutdown was requested. It doesn't mean that shutdown was
     * actually started. Some units (NetHub, ISO8583 in TX) may want to delay
     * actual shutdown at the time of shutdown request. To check if shutdown is
     * actually started use {@linkplain isShuttingDown()}
     */
    public final boolean isShutdownRequested() {
        return bShutdownRequested;
    }

    @SuppressWarnings("unused")
    protected boolean requestShutdownImpl() {
        return true;
    }
    //
    private long lastDbIAmAliveMillis = 0;
    private long lastDgIAmAliveMillis = 0;
    private long lastOptionsRereadMillis = 0;
    private long lastMonitoringSettingsRereadMillis = 0;
    private long lastMonitoringFlushMillis = 0;
    private long lastBasicStatsWriteMillis = 0;
    private long lastStatusInViewUpdateMillis = 0;
    private long lastCpuUsageUpdateMillis = 0;

    /**
     * Called only from UnitThread
     *
     * @throws Exception
     */
    private void maintenance() throws InterruptedException {
        try {
            final long curMillis = System.currentTimeMillis();
            //selfcheck
            if (curMillis - lastDbIAmAliveMillis >= DB_I_AM_ALIVE_PERIOD_MILLIS) {
                if (!dbQueries.dbIAmStillAlive(false)) {
                    getTrace().putFloodControlled("unit-selfcheck-failed-[" + getId() + "]", EEventSeverity.WARNING, String.format(Messages.UNABLE_TO_UPDATE_SELFCHECK_TIME, getFullTitle()), Messages.MLS_ID_UNABLE_TO_UPDATE_SELFCHECK_TIME, new ArrStr(getFullTitle()), EEventSource.UNIT.getValue(), -1, false, null);
                } else {
                    lastDbIAmAliveMillis = curMillis;
                }
            }
            if (curMillis - lastDgIAmAliveMillis >= DG_I_AM_ALIVE_PERIOD_MILLIS) {
                instance.getAadcManager().unitIsAlive(id, curMillis);
                lastDgIAmAliveMillis = curMillis;
            }
            //unit commands
            processCommands();
            //options
            if (curMillis - lastOptionsRereadMillis >= OPTIONS_REREAD_PERIOD_MILLIS) {
                rereadCommonOptions();
                rereadOptionsImpl();

                lastOptionsRereadMillis = curMillis;
            }
            if (curMillis - lastBasicStatsWriteMillis >= Instance.WRITE_BASIC_STATS_PERIOD_MILLIS) {
                lastBasicStatsWriteMillis = curMillis;
                writeBasicStats();
                if (getDbConnection() != null) {
                    getDbConnection().commit();
                }
            }
            if (monitor != null) {
                if (curMillis - lastMonitoringFlushMillis >= MONITORING_FLUSH_PERIOD_MILLIS) {
                    monitor.flush();
                    lastMonitoringFlushMillis = curMillis;
                }

                if (curMillis - lastMonitoringSettingsRereadMillis >= MONITORING_SETTINGS_REREAD_PERIOD_MILLIS) {
                    monitor.rereadSettings();
                    lastMonitoringSettingsRereadMillis = curMillis;
                }
            }
            if (curMillis - lastStatusInViewUpdateMillis >= STATUS_IN_VIEW_UPDATE_PERIOD_MILLIS) {
                final UnitView viewShanshot = getViewNoCreate();
                if (viewShanshot != null) {
                    viewShanshot.setStatus(getStatus());
                }
                lastStatusInViewUpdateMillis = curMillis;
            }

            if (curMillis - lastCpuUsageUpdateMillis >= CPU_USAGE_UPDATE_PERIOD_MILLIS) {
                updateCpuUsage();
                lastCpuUsageUpdateMillis = curMillis;
            }

        } catch (SQLException e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(getFullTitle(), exStack), getEventSource(), false);
            restoreDbConnection();
        }
        maintenanceImpl();
        getTrace().flush();
    }

    public String getStatus() {
        return "CPU %: " + cpuUsagePercent;
    }

    public int getCpuUsagePercent() {
        return cpuUsagePercent;
    }

    private void updateCpuUsage() {
        long newCpuUsedNanos = MonitoringUtils.getCurrentThreadCpuTime();
        if (newCpuUsedNanos > 0) {

            long nanoTime = System.nanoTime();
            if (lastCpuUsedNanos > 0) {
                cpuUsagePercent = (int) (100 * (Math.max(newCpuUsedNanos - lastCpuUsedNanos, 0)) / (Math.max(nanoTime - lastCpuUsageUpdateNanos, 1)));
                if (cpuUsagePercent > 99) {
                    cpuUsagePercent = 99;
                }
            }
            lastCpuUsedNanos = newCpuUsedNanos;
            lastCpuUsageUpdateNanos = nanoTime;
        }
    }

    private void processCommands() {
        final List<UnitCommand> commands = new ArrayList<>();
        commandsQueue.drainTo(commands);
        for (UnitCommand command : commands) {
            try {
                getTrace().debug("Processing command " + command.getTraceId(), EEventSource.UNIT, false);
                processCommand(command);
            } catch (RuntimeException ex) {
                getTrace().put(EEventSeverity.ERROR, "Exception on processing command: " + ExceptionTextFormatter.throwableToString(ex), EEventSource.UNIT);
            }
        }
    }

    protected void processCommand(final UnitCommand unitCommand) {
        Instance.get().enqueueResponseToUnitCommand(new UnitCommandResponse(unitCommand, new ServiceClientException("Command is not supported")));
    }

    protected void clearBasicStats() {
    }

    protected void writeBasicStats() throws SQLException, InterruptedException {
    }

    protected void rereadOptionsImpl() throws SQLException, InterruptedException {
    }

    /**
     * Called only from UnitThread
     *
     * @throws InterruptedException
     */
    protected abstract void maintenanceImpl() throws InterruptedException;

    /**
     * Called only from UnitThread
     */
    protected void stopImpl() {
        try {
            clearBasicStats();
            writeBasicStats();
        } catch (Exception ex) {
            logErrorOnStop(ex);
        }
        if (monitor != null) {
            try {
                monitor.flush();
            } catch (SQLException ex) {
                final String exStack = ExceptionTextFormatter.throwableToString(ex);
                getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(getFullTitle(), exStack), getEventSource(), false);
            }
            monitor = null;
        }
        try {
            getTrace().leaveContext(EEventContextType.SYSTEM_UNIT, Long.valueOf(id), null);
        } catch (IllegalUsageError e) {
            //start could failed before entering context
        }
        getTrace().setDbConnection(null);
        scpName = null;
        commonOptions = null;
        //do not close db connection because it will be used by UnitThread to update unit state in DB
        //after it DB connection will be closed by UnitThread
    }

    public long getId() {
        if (id == 0) {
            throw new IllegalUsageError("Unit is not started");
        }
        return id;
    }

    public String getFullTitle() {
        return Messages.TITLE_UNIT + " #" + String.valueOf(getId()) + " - " + getUnitTypeTitle() + " '" + title + "'";
    }

    public String getTitle() {
        return title;
    }

    public abstract String getUnitTypeTitle();

    public abstract Long getUnitType();

// db connection	
    public Connection getDbConnection() {
        return dbConnection;
    }

    public RadixConnection getRadixDbConnection() {
        return dbConnection;
    }

    /**
     * Called only from UnitThread
     */
    protected void setDbConnection(final RadixConnection dbConnection) {
        trace.setDbConnection(dbConnection);
        dbQueries.closeAll();
        try {
            if (this.dbConnection != null) {
                this.dbConnection.close();
            }
        } catch (SQLException e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            getTrace().put(EEventSeverity.WARNING, Messages.ERR_ON_DB_CONNECTION_CLOSE + ": \n" + exStack, Messages.MLS_ID_ERR_ON_DB_CONNECTION_CLOSE, new ArrStr(getFullTitle(), exStack), getEventSource(), false);
        }
        this.dbConnection = dbConnection;
        if (monitor != null) {
            monitor.setConnection(dbConnection);
        }
        if (dbConnection != null) {
            getInstance().getResourceRegistry().register(new DbConnectionResourceItem(ResourceRegistry.buildDbConnectionKey(getResourceKeyPrefix(), dbConnection), dbConnection, null, getThisRunAliveChecker()));
        }
    }

    /**
     * Should be called only from UnitThread
     *
     * @throws InterruptedException
     */
    public void restoreDbConnection() throws InterruptedException {
        if (isAborted()) {
            throw new InterruptedException();
        }
        getTrace().debug("Try to restore DB Connection", getEventSource(), false);
        setDbConnection(null);
        while (!isShuttingDown()) {
            try {
                setDbConnection((RadixConnection) instance.openNewUnitDbConnection(this));
                getTrace().put(EEventSeverity.EVENT, Messages.DB_CONNECTION_RESTORED, Messages.MLS_ID_DB_CONNECTION_RESTORED, new ArrStr(getFullTitle()), getEventSource(), false);
                if (isSingletonUnit()) {
                    final Connection instConn = instance.getDbConnection();
                    if (instConn != null && !instConn.isClosed()) {
                        final SingletoneStartCheckResult checkStartResult = checkSingletoneStart();
                        if ((checkStartResult.getStartedUnit() == null || checkStartResult.getStartedUnit().getUnitId() != getId())
                                && checkStartResult.getPostponeStartReason() != null) {
                            requestStopAndPostponedRestartFromUnitThread(checkStartResult.getPostponeStartReason());
                            throw new IllegalStateException(checkStartResult.getPostponeStartReason());
                        }
                        try {
                            dbQueries.dbIAmStillAlive(true);
                        } catch (Exception ex) {
                            getDbConnection().close();
                        } finally {
                            releaseAcquiredSingletoneUnitLock();
                        }
                    } else {
                        throw new SQLException("Unable to perform singletone unit check: instance database connection is closed");
                    }
                }
                failedDbReconnectAttempts = 0;
                break;
            } catch (SQLException e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(getFullTitle(), exStack), getEventSource(), false);
                if (failedDbReconnectAttempts < MAX_DB_RECONNECT_TRY_ATTEMPTS) {
                    failedDbReconnectAttempts++;
                    Thread.sleep(DB_RECONNECT_TRY_PERIOD_MILLIS);
                } else {
                    requestStopAndPostponedRestartFromUnitThread("no database connection");
                    return;
                }
            }
        }
    }

// instance	
    public Instance getInstance() {
        return instance;
    }
    private final Instance instance;

// view 	
    abstract protected UnitView newUnitView();
    private UnitView view = null;

    public final UnitView getView() {
        if (view == null && SrvRunParams.getIsGuiOn()) {
            view = newUnitView();
        }
        return view;
    }

    public UnitView getViewNoCreate() {
        return view;
    }

    public final void disposeView() {
        if (view != null) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    view.dispose();
                }
            });
        }
    }

    public LocalTracer createTracer() {
        return getTrace().newTracer(getEventSource());
    }

//Event source 
    public String getEventSource() {
        return EEventSource.UNIT.getValue();
    }

//	state	
    public UnitState getState() {
        return state;
    }

    final void setState(final UnitState newState, final String reason) throws InterruptedException {
        final UnitState oldState = state;

        if (newState == UnitState.STARTED) {
            while (!isShuttingDown()) {
                try {
                    dbQueries.setDbStartedState(true, true);
                    break;
                } catch (SQLException ex) {
                    final String exStack = ExceptionTextFormatter.exceptionStackToString(ex);
                    getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(getFullTitle(), exStack), getEventSource(), false);
                    restoreDbConnection();
                }
            }
        } else if (newState == UnitState.STOPPED && getDbConnection() != null) {
            try {
                dbQueries.setDbStartedState(false, true);
            } catch (SQLException ex) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(ex);
                getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(getFullTitle(), exStack), getEventSource(), false);
            }
        } else if (newState == UnitState.START_POSTPONED) {
            try {
                getInstance().setUnitStartPostponedInDb(getId(), true);
            } catch (SQLException ex) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(ex);
                getInstance().getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(getFullTitle(), exStack), getEventSource(), false);
            }
        } else if (newState == UnitState.STOPPING) {
            lastStoppingStateTimeMillis = System.currentTimeMillis();
        }

        if (newState != UnitState.START_POSTPONED) {//POSTPONED START is handled separately
            final String title_ = getFullTitle();
            final String reasonStr = getReasonSuffix(reason);
            ServerTrace traceToPut = state == UnitState.STARTING ? getInstance().getTrace() : getTrace();
            traceToPut.put(EEventSeverity.EVENT, Messages.getStateMessage(newState) + " " + (title_.length() != 0 ? " \"" + title_ + "\"" : "") + reasonStr, Messages.getStateMessageMslId(newState), new ArrStr(title_ + reasonStr), getEventSource(), false);
        }

        state = newState;
        fireStateChanged(oldState);
    }

    private String getReasonSuffix(final String reason) {
        return reason == null || reason.isEmpty() ? "" : " (" + reason + ")";
    }

//Trace	
    public ServerTrace getTrace() {
        return trace;
    }

    public long getLastStoppingStateTimeMillis() {
        return lastStoppingStateTimeMillis;
    }

    public void setLastStoppingStateTimeMillis(long lastStoppingStateTimeMillis) {
        this.lastStoppingStateTimeMillis = lastStoppingStateTimeMillis;
    }

//Options
    final static class CommonOptions {

        final TraceProfiles traceProfiles;
        final String scpName;
        final boolean useInAadcTestMode;

        public CommonOptions(TraceProfiles traceProfiles, String scpName, boolean useInAadcTestMode) {
            this.traceProfiles = traceProfiles;
            this.scpName = scpName;
            this.useInAadcTestMode = useInAadcTestMode;
        }

    }

    /**
     * Called only from UnitThread
     */
    private void rereadCommonOptions() throws SQLException, InterruptedException {
        final CommonOptions newOptions = dbQueries.readCommonOptions();
        if (commonOptions == null || !newOptions.traceProfiles.equals(commonOptions.traceProfiles)) {
            getTrace().put(EEventSeverity.EVENT, Messages.TRACE_PROFILE_CHANGED + "\"" + String.valueOf(newOptions.traceProfiles) + "\"", Messages.MLS_ID_TRACE_PROFILE_CHANGED, new ArrStr(getFullTitle(), String.valueOf(newOptions.traceProfiles)), getEventSource(), false);
            trace.setProfiles(newOptions.traceProfiles);
        }
        if (!Utils.equals(newOptions.scpName, scpName)) {
            scpName = newOptions.scpName;
            final String scpPres = scpName == null ? "" : scpName;
            getTrace().put(EEventSeverity.EVENT, Messages.SCP_CHANGED + "\"" + scpPres + "\"", Messages.MLS_ID_SCP_CHANGED, new ArrStr(getFullTitle(), scpPres), getEventSource(), false);
        }
        commonOptions = newOptions;
    }

    public TraceProfiles getTraceProfiles() {
        if (commonOptions != null) {
            return commonOptions.traceProfiles;
        }
        return null;
    }

    public boolean isSingletonUnit() {
        return false;
    }

    public boolean isSingletonByPrimary() {
        return false;
    }

    public void registerListener(final UnitListener listener) {
        listeners.add(listener);
    }

    public void unregisterListener(final UnitListener listener) {
        listeners.remove(listener);
    }

    private final void fireStateChanged(final UnitState oldState) {
        for (UnitListener listener : listeners) {
            listener.stateChanged(this, oldState, state);
        }
    }

    public String getScpName() {
        return scpName;
    }

    protected void requestStopAndPostponedRestart(final String reason) {
        requestStopAndPostponedRestart(reason, -1);
    }

    protected void requestStopAndPostponedRestartFromUnitThread(final String reason) {
        requestPostponeStartAfterStop(reason);
        if (!isShutdownRequested()) {
            requestShutdown();
        }
    }

    /**
     * Asynchronous request to stop and postpone restart.
     *
     * @param reason short explanation of the reason for restart. Used for
     * logging. Can (but should not) be null.
     * @param absoluteRestartTimeMillis requested time for postponed
     * start(absolute). See
     * {@linkplain Unit#postponeStart(java.lang.String, long)}.
     */
    protected void requestStopAndPostponedRestart(final String reason, final long absoluteRestartTimeMillis) { //for RADIX-3114
        new Thread() {
            @Override
            public void run() {
                setName("Unit #" + String.valueOf(Unit.this.getId()) + " Restarter");
                try {
                    if (Unit.this.stop(reason)) {
                        postponeStart("Restart postponed after unit stop" + ((reason == null || reason.isEmpty()) ? "" : ". Reason: " + reason), absoluteRestartTimeMillis);
                    }
                } catch (Exception ex) {
                    LogFactory.getLog(Unit.class).warn("Unable to restart unit '" + Unit.this.getFullTitle() + "'", ex);
                }

            }
        }.start();
    }

    protected static class SingletoneStartCheckResult {

        private final String postponeStartReason;
        private final long plannedRestartTimeMillis;
        private final UnitDescription startedUnit;

        public SingletoneStartCheckResult(String postponeReason, long plannedRestartTimeMillis, UnitDescription startedUnit) {
            this.postponeStartReason = postponeReason;
            this.plannedRestartTimeMillis = plannedRestartTimeMillis;
            this.startedUnit = startedUnit;
        }

        public UnitDescription getStartedUnit() {
            return startedUnit;
        }

        public String getPostponeStartReason() {
            return postponeStartReason;
        }

        public long getPlannedRestartTimeMillis() {
            return plannedRestartTimeMillis;
        }

    }
}
