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
package org.radixware.kernel.server.units;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import org.radixware.kernel.server.exceptions.InvalidUnitState;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.trace.ServerTrace;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.LogFactory;
import org.radixware.kernel.common.enums.EEventContextType;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.trace.IRadixTrace;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.server.SrvRunParams;
import org.radixware.kernel.server.aio.ServiceManifestLoader;
import org.radixware.kernel.server.aio.ServiceManifestServerLoader;
import org.radixware.kernel.server.instance.UnitCommand;
import org.radixware.kernel.server.instance.UnitCommandResponse;
import org.radixware.kernel.server.monitoring.AbstractMonitor;
import org.radixware.kernel.server.monitoring.MonitorFactory;
import org.radixware.kernel.server.trace.FileLogOptions;
import org.radixware.kernel.server.trace.TraceProfiles;

public abstract class Unit {

    public static final String UNIT_INTERRUPTION_ALLOWED_PROPERTY_NAME = "rdx.unit.interruption.allowed";
    private static final int OPTIONS_REREAD_PERIOD_MILLIS = 60 * 1000;//1 min
    public static final int DB_I_AM_ALIVE_PERIOD_MILLIS = 60 * 1000;//1 min
    public static final int DB_I_AM_ALIVE_TIMEOUT_MILLIS = 3 * DB_I_AM_ALIVE_PERIOD_MILLIS;
    private static final int SHUTDOWN_TIMEOUT_MILLIS = 60000;//1 min  
    private static final int INTERRUPT_TIMEOUT_MILLIS = 3 * 60000;//3 min;
    private static final int DB_RECONNECT_TRY_PERIOD_MILLIS = 5 * 1000;//5 sec
    private static final int MONITORING_SETTINGS_REREAD_PERIOD_MILLIS = 60000;
    private static final int MONITORING_FLUSH_PERIOD_MILLIS = 30000;
    private static final int STATUS_IN_VIEW_UPDATE_PERIOD_MILLIS = 500;
    private static final int CPU_USAGE_UPDATE_PERIOD_MILLIS = 1000;
    private volatile boolean bShuttingDown = false;
    private volatile boolean bShutdownRequested = false;
    private volatile boolean postponeStartAfterStopRequested = false;
    private volatile String lastPostponeStartReason = null;
    private volatile long timeForPostponedStartMillis = -1;
    private final DbQueries dbQueries;
    private volatile Connection dbConnection = null;
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

    protected Unit(final Instance instModel, final Long id, final String title, final MonitorFactory<AbstractMonitor, Unit> monitorFactory) {
        trace = ServerTrace.Factory.newInstance(instModel);
        instance = instModel;
        this.id = id.longValue();
        listeners = new CopyOnWriteArrayList<>();
        dbQueries = new DbQueries(this);
        this.title = title;
        this.monitorFactory = monitorFactory;
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

    protected Object getControlSemaphore() {
        return controlSem;
    }

    public AbstractMonitor getMonitor() {
        return monitor;
    }

    protected String getIdForSingletonLock() {
        return getUnitType().toString();
    }

    protected boolean prepareForStart() throws InterruptedException {
        return true;
    }

    protected UnitDescription getStartedDuplicatedUnitDescription() throws SQLException {
        return instance.getStartedUnitId(getUnitType());
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
            if (prepareForStart()) {
                if (isSingletonUnit()) {
                    final SingletoneStartCheckResult checkResult = checkSingletoneStart();
                    if (checkResult.getPostponeStartReason() != null) {
                        if (checkResult.isLockAcquired()) {
                            instance.getSingletonUnitLock().releaseLock(checkResult.getLockId());
                        }
                        postponeStart(checkResult.getPostponeStartReason(), checkResult.getPlannedRestartTimeMillis());
                        return false;
                    }
                    registerListener(new UnitListener() {
                        @Override
                        public void stateChanged(final Unit unit, final UnitState oldState, final UnitState newState) {
                            if (newState == UnitState.STARTED || newState == UnitState.STOPPED) {
                                try {
                                    instance.getSingletonUnitLock().releaseLock(checkResult.getLockId());
                                } finally {
                                    unit.unregisterListener(this);
                                }
                            }
                        }
                    });
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
        final String lockId = getIdForSingletonLock();
        if (instance.getSingletonUnitLock().lock(lockId)) {
            UnitDescription startedUnitDesc = null;
            String postponeStartReason = null;
            try {
                startedUnitDesc = getStartedDuplicatedUnitDescription();
                if (startedUnitDesc != null) {
                    postponeStartReason = String.format(Messages.UNIT_ID_IS_ALREADY_RUNNING, startedUnitDesc.getUnitId(), startedUnitDesc.getInstanceId(), startedUnitDesc.getInstanceTitle());
                }
            } catch (SQLException e) {
                postponeStartReason = ExceptionTextFormatter.exceptionStackToString(e);// release lock and postpone start
            }
            if (postponeStartReason != null) {
                return new SingletoneStartCheckResult(postponeStartReason, true, -1, startedUnitDesc, lockId);
            }
        } else {
            return new SingletoneStartCheckResult("Unable to acquire singletone unit lock '" + lockId + "' for unit #" + id, false, System.currentTimeMillis() + 5000, null, lockId);
        }
        return new SingletoneStartCheckResult(null, true, -1, null, lockId);
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
            getInstance().getTrace().put(EEventSeverity.EVENT, String.format(Messages.UNIT_START_POSPONED, "'" + getFullTitle() + "'") + reasonExplanation, Messages.MLS_ID_UNIT_START_POSPONED_WITH_REASON, new ArrStr(getFullTitle(), reason), EEventSource.UNIT, false);
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
                setState(UnitState.STOPPED, "stop in start_postponed state");
                return true;
            }

            if (getState() == UnitState.STOPPED) {
                return true;
            }

            final String reasonSuffix = getReasonSuffix(reason);
            getTrace().put(EEventSeverity.EVENT, Messages.TRY_SHUTDOWN_UNIT + reasonSuffix, Messages.MLS_ID_TRY_SHUTDOWN_UNIT, new ArrStr(getFullTitle() + reasonSuffix), getEventSource(), false);

            if (thread == null || thread.getState() == Thread.State.TERMINATED) {
                Connection connection = null;
                try {
                    try {
                        connection = instance.openNewUnitDbConnection(this);
                        setDbConnection(connection);
                        setState(UnitState.STOPPED, "Set STOPPED status to dead unit");
                        return true;
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

            //soft stop
            requestShutdown();
            try {
                thread.join(SHUTDOWN_TIMEOUT_MILLIS);
            } catch (Throwable e) {
                logErrorOnStop(e);
            }
            if (thread.getState() == Thread.State.TERMINATED) {
                return true;
            }
            getTrace().put(EEventSeverity.WARNING, Messages.TRY_INTERRUPT_UNIT, Messages.MLS_ID_TRY_INTERRUPT_UNIT, new ArrStr(getFullTitle()), getEventSource(), false);
            //hard stop
            thread.interrupt();
            thread.join(INTERRUPT_TIMEOUT_MILLIS);
            return getState() == UnitState.STOPPED;
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

    /**
     * Dangerous! Used in development mode.
     */
    public void interrupt() {
        if (isInterruptionAllowed()) {
            final UnitThread threadSnapshot = thread;
            if (threadSnapshot != null) {
                threadSnapshot.interrupt();
            }
        }
    }

    public static boolean isInterruptionAllowed() {
        return System.getProperty(UNIT_INTERRUPTION_ALLOWED_PROPERTY_NAME) != null;
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
        postponeStartAfterStopRequested = false;
        final Connection connection = instance.openNewUnitDbConnection(this);
        try {
            if (monitorFactory != null) {
                monitor = monitorFactory.createMonitor(this);
            }
            setDbConnection(connection);
            if (monitor != null) {
                monitor.rereadSettings();
            }
            commandsQueue.clear();
            rereadCommonOptions();
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

            return true;
        } catch (Exception e) {
            try {
                connection.close();
            } catch (SQLException e2) {
                //do nothing
            }
            throw e;
        }
    }

    public void setPostponeStartAfterStop(boolean postponeStart) {
        postponeStartAfterStopRequested = true;
    }

    public boolean isPostponeStartAfterStopRequested() {
        return postponeStartAfterStopRequested;
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
                instOpt.isRotateDaily());
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
        while (!Thread.interrupted() && !isShuttingDown()) {
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
                dbQueries.dbIAmStillAlive();
                lastDbIAmAliveMillis = curMillis;
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
        final ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        if (threadBean != null && threadBean.isCurrentThreadCpuTimeSupported()) {
            long newCpuUsedNanos = threadBean.getCurrentThreadCpuTime();
            long nanoTime = System.nanoTime();
            if (lastCpuUsedNanos > 0) {
                cpuUsagePercent = (int) (100 * (newCpuUsedNanos - lastCpuUsedNanos) / (nanoTime - lastCpuUsageUpdateNanos));
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

    /**
     * Called only from UnitThread
     */
    protected void setDbConnection(final Connection dbConnection) {
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
    }

    /**
     * Should be called only from UnitThread
     *
     * @throws InterruptedException
     */
    public void restoreDbConnection() throws InterruptedException {
        getTrace().debug("Try to restore DB Connection", getEventSource(), false);
        setDbConnection(null);
        while (!isShuttingDown()) {
            try {
                setDbConnection(instance.openNewUnitDbConnection(this));
                getTrace().put(EEventSeverity.EVENT, Messages.DB_CONNECTION_RESTORED, Messages.MLS_ID_DB_CONNECTION_RESTORED, new ArrStr(getFullTitle()), getEventSource(), false);
                if (isSingletonUnit()) {
                    final Connection instConn = instance.getDbConnection();
                    if (instConn != null && !instConn.isClosed()) {
                        final SingletoneStartCheckResult checkStartResult = checkSingletoneStart();
                        if ((checkStartResult.getStartedUnit() == null || checkStartResult.getStartedUnit().getUnitId() != getId())
                                && checkStartResult.getPostponeStartReason() != null) {
                            requestShutdown();
                            setPostponeStartAfterStop(true);
                            throw new IllegalStateException(checkStartResult.getPostponeStartReason());
                        }
                        dbQueries.dbIAmStillAlive();
                        instance.getSingletonUnitLock().releaseLock(checkStartResult.getLockId());
                    } else {
                        throw new SQLException("Unable to perform singletone unit check: instance database connection is closed");
                    }
                }
                break;
            } catch (SQLException e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(getFullTitle(), exStack), getEventSource(), false);
                Thread.sleep(DB_RECONNECT_TRY_PERIOD_MILLIS);
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
        state = newState;

        if (state == UnitState.STARTED) {
            while (!isShuttingDown()) {
                try {
                    dbQueries.setDbStartedState(true);
                    break;
                } catch (SQLException ex) {
                    final String exStack = ExceptionTextFormatter.exceptionStackToString(ex);
                    getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(getFullTitle(), exStack), getEventSource(), false);
                    restoreDbConnection();
                }
            }
        } else if (state == UnitState.STOPPED && getDbConnection() != null/*
                 * && oldState != UnitState.START_POSTPONED
                 */) {
            try {
                dbQueries.setDbStartedState(false);
            } catch (SQLException ex) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(ex);
                getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(getFullTitle(), exStack), getEventSource(), false);
            }
        }

        final String title_ = getFullTitle();
        final String reasonStr = getReasonSuffix(reason);
        ServerTrace traceToPut = state == UnitState.STARTING ? getInstance().getTrace() : getTrace();
        traceToPut.put(EEventSeverity.EVENT, Messages.getStateMessage(newState) + " " + (title_.length() != 0 ? " \"" + title_ + "\"" : "") + reasonStr, Messages.getStateMessageMslId(newState), new ArrStr(title_ + reasonStr), getEventSource(), false);
        fireStateChanged(oldState);
    }

    private String getReasonSuffix(final String reason) {
        return reason == null || reason.isEmpty() ? "" : " (" + reason + ")";
    }

//Trace	
    public ServerTrace getTrace() {
        return trace;
    }

//Options
    final static class CommonOptions {

        final TraceProfiles traceProfiles;
        final String scpName;

        CommonOptions(final TraceProfiles traceProfiles, final String scpName) {
            this.traceProfiles = traceProfiles;
            this.scpName = scpName;
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
        private final boolean lockAcquired;
        private final long plannedRestartTimeMillis;
        private final UnitDescription startedUnit;
        private final String lockId;

        public SingletoneStartCheckResult(String postponeReason, boolean lockAcquired, long plannedRestartTimeMillis, UnitDescription startedUnit, final String lockId) {
            this.postponeStartReason = postponeReason;
            this.lockAcquired = lockAcquired;
            this.plannedRestartTimeMillis = plannedRestartTimeMillis;
            this.startedUnit = startedUnit;
            this.lockId = lockId;
        }

        public UnitDescription getStartedUnit() {
            return startedUnit;
        }

        public String getPostponeStartReason() {
            return postponeStartReason;
        }

        public boolean isLockAcquired() {
            return lockAcquired;
        }

        public long getPlannedRestartTimeMillis() {
            return plannedRestartTimeMillis;
        }

        public String getLockId() {
            return lockId;
        }

    }
}
