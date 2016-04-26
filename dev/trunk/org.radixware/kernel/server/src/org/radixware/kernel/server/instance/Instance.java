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

import org.radixware.kernel.common.repository.DbConfiguration;
import org.radixware.kernel.common.kerberos.KerberosUtils;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.pool.OracleDataSource;
import org.apache.commons.logging.Log;

import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.server.SrvRunParams.DecryptionException;
import org.radixware.kernel.server.SrvRunParams.UnableToLoadOptionsFromFile;
import org.radixware.kernel.server.aio.Event;
import org.radixware.kernel.server.aio.EventDispatcher;
import org.radixware.kernel.server.aio.EventHandler;
import org.radixware.kernel.server.aio.EventDispatcher.TimerEvent;
import org.radixware.kernel.server.exceptions.InvalidInstanceState;
import org.radixware.kernel.server.trace.ServerTrace;
import org.radixware.kernel.server.units.Unit;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.LogRecord;
import javax.sql.DataSource;
import oracle.jdbc.OracleConnection;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.spi.RootLogger;
import org.apache.xmlbeans.XmlOptions;
import org.radixware.kernel.common.defs.RadixObjectInitializationPolicy;
import org.radixware.kernel.common.defs.utils.ISeverityByCodeCalculator;
import org.radixware.kernel.common.enums.EDbSessionOwnerType;
import org.radixware.kernel.common.enums.EEventContextType;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EOptionMode;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.common.exceptions.KernelVersionError;
import org.radixware.kernel.common.exceptions.KeystoreControllerException;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.kerberos.KerberosCredentials;
import org.radixware.kernel.common.kerberos.KrbServiceOptions;
import org.radixware.kernel.common.repository.DbOptionValue;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.ssl.KeystoreController;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.ValueFormatter;
import org.radixware.kernel.license.ILicenseEnvironment;
import org.radixware.kernel.license.LicenseManager;
import org.radixware.kernel.server.RadixLoaderActualizer;
import org.radixware.kernel.server.SrvRunParams;
import org.radixware.kernel.server.SrvRunParams.ConfigFileNotSpecifiedException;
import org.radixware.kernel.server.arte.LoadErrorsLog;
import org.radixware.kernel.server.arte.Release;
import org.radixware.kernel.server.instance.arte.ArteInstance;
import org.radixware.kernel.server.instance.arte.ArtesDbLogFlusher;
import org.radixware.kernel.server.instance.arte.ArtePool;
import org.radixware.kernel.server.jdbc.RadixConnection;
import org.radixware.kernel.server.monitoring.InstanceMonitor;
import org.radixware.kernel.server.trace.*;
import org.radixware.kernel.server.units.UnitDescription;
import org.radixware.kernel.server.units.job.executor.JobExecutorUnit;
import org.radixware.kernel.server.units.job.scheduler.JobSchedulerUnit;
import org.radixware.kernel.server.units.mq.MqUnit;
import org.radixware.kernel.server.utils.PriorityResourceManager;
import org.radixware.kernel.starter.Starter;
import org.radixware.kernel.starter.log.DelegateLogFactory;
import org.radixware.kernel.starter.log.StarterLog;
import org.radixware.kernel.starter.meta.FileMeta;
import org.radixware.kernel.starter.meta.LayerMeta;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.kernel.starter.radixloader.RadixLoaderException;
import org.radixware.kernel.starter.radixloader.RadixURLTool;
import org.radixware.kernel.starter.utils.SystemTools;

public class Instance implements EventHandler {

    private static final boolean XBEANS_SYNCRONIZATION_OFF = SystemPropUtils.getBooleanSystemProp("rdx.xbeans.sync.off", true);
//	static options

    private static final int ONE_SECOND_MILLIS = 1000;
    private static final int FIVE_SECONDS_MILLIS = 5 * ONE_SECOND_MILLIS;
    private static final int TEN_SECONDS_MILLIS = 10 * ONE_SECOND_MILLIS;
    private static final int TWENTY_SECONDS_MILLIS = 20 * ONE_SECOND_MILLIS;
    private static final int THIRTY_SECONDS_MILLIS = 30 * ONE_SECOND_MILLIS;
    private static final int ONE_MINUTE_MILLIS = 60 * ONE_SECOND_MILLIS;
    private static final int FIVE_MINUTES_MILLIS = 5 * ONE_MINUTE_MILLIS;
    //periods
    //each second
    private static final int TIC_MILLIS = ONE_SECOND_MILLIS;
    //each 5 seconds
    private static final int FILE_LOGS_MAINTENANCE_PERIOD_MILLIS = FIVE_SECONDS_MILLIS;
    private static final int DB_RECONNECT_TRY_PERIOD_MILLIS = FIVE_SECONDS_MILLIS;
    //each 10 seconds
    private static final int ACTUALIZE_PERIOD_MILLIS = TEN_SECONDS_MILLIS;
    private static final int MONITOR_FLUSH_PERIOD_MILLIS = TEN_SECONDS_MILLIS;
    private static final int OBJECT_CACHE_MAINTENANCE_PERIOD_MILLIS = TEN_SECONDS_MILLIS;
    private static final int WRITE_ARTE_STATE_PERIOD_MILLIS = TEN_SECONDS_MILLIS;
    public static final int WRITE_BASIC_STATS_PERIOD_MILLIS = TEN_SECONDS_MILLIS;
    //each 20 seconds
    private static final int DB_I_AM_ALIVE_PERIOD_MILLIS = TWENTY_SECONDS_MILLIS;
    public static final int DB_I_AM_ALIVE_TIMEOUT_MILLIS = 3 * DB_I_AM_ALIVE_PERIOD_MILLIS;
    //each minute
    private static final int OPTIONS_REREAD_PERIOD_MILLIS = ONE_MINUTE_MILLIS;
    private static final int KEYSTORE_REREAD_PERIOD_MILLIS = ONE_MINUTE_MILLIS;
    private static final int MONITOR_REREAD_SETTINGS_PERIOD_MILLIS = ONE_MINUTE_MILLIS;
    private static final int SEVERITY_PREPROCESSING_MAP_REREAD_MILLIS = ONE_MINUTE_MILLIS;
    //each 5 minutes
    private static final int DB_CONFIGURATION_REREAD_PERIOD_MILLIS = FIVE_MINUTES_MILLIS;
    private static final int CHECK_DB_TIME_PERIOD_MILLIS = FIVE_MINUTES_MILLIS;
    //other
    private static final int CHECK_UNIT_HANG_PERIOD_MILLIS = Unit.DB_I_AM_ALIVE_PERIOD_MILLIS / 2;
    private static final int SLEEP_ON_ARTE_POOL_LOAD_WAIT_MILLIS = ONE_SECOND_MILLIS;
    private static final int MAX_WAIT_FOR_ARTE_POOL_LOAD_MILLIS = THIRTY_SECONDS_MILLIS;
    //timeouts
    private static final int SHUTDOWN_TIMEOUT_MILLIS = ONE_MINUTE_MILLIS;
    //memory usage levels
    private static final int MEM_USAGE_EV_PERCENT = 80;
    private static final int MEM_USAGE_WARN_PERCENT = 90;
    private static final int MEM_USAGE_ERR_PERCENT = 95;
    private static final int MEM_USAGE_ALARM_PERCENT = 98;
    //
    private static final String TDUMPS_DIR_NAME = "tdumps";
    //
    private static volatile Instance INSTANCE_GLOBAL_POINTER = null;
    //
    private static final int COUNT_OF_LOGGED_LOCAL_FILE_OVERRIDES = SystemPropUtils.getIntSystemProp("rdx.count.of.logged.local.file.overrides", 10);
    //
    private volatile String proxyOraUser = null;
    private volatile OracleDataSource oraDataSource = null;
    private final List<InstanceListener> listeners;
    private final Object controlSem = new Object();
    private String stopOsCommand = null;
    private volatile InstanceOptions options;
    private volatile InstanceMonitor monitor;
    private volatile ArtePool artePool;
    private volatile List<Callable> stopIterations;
    private volatile ArtesDbLogFlusher artesDbLogFlusher = null;
    private volatile InstanceProfiler profiler = null;
    private volatile ISeverityByCodeCalculator severityPreprocessor = null;
    private volatile DbConfiguration dbConfiguration = null;
    private volatile InstanceThread thread = null;
    private final JobCheckTimeUpdater jobCheckTimeUpdater;
    private final DbQueries dbQueries;
    private volatile long localSensitiveTracingFinishMillis = 0;
    private volatile boolean isActualizeVerSchedulled = false;
    private long oldVersion = -1;
    private final List<RadixLoaderActualizer.DdsVersionWarning> reportedDdsWarnings = new LinkedList<>();
    private volatile InstanceView view = null;
    private volatile boolean bShuttingDown = false;
    private String host;
    private String name;
    private String pid;
    private volatile InstanceState state = InstanceState.STOPPED;
    private final ServerTrace trace;
    private int id;
    private volatile Connection dbConnection;
    private volatile EventDispatcher dispatcher = null;
    private volatile InstanceRcSap service = null;
    private final UnitsPool units;
    //Kerberos credentials
    private final Object krbSem = new Object();
    private volatile KrbServiceOptions currentKrbOptions;
    private volatile KerberosCredentials serviceCreds;
    //
    private final List<WeakReference<FileLog>> fileLogRegistry = new CopyOnWriteArrayList<>();
    private final ObjectCache objectCache = new ObjectCache();
    private volatile ThreadDumpWriter threadDumpWriter;
    private volatile ArteStateWriter arteStateWriter;
    private volatile ArteWatchDog arteWatchDog;
    private volatile ArteWaitStatsCollector arteWaitStatsCollector;
    private final AtomicLong lastArteAutoShutdownTimeMillisHolder = new AtomicLong(0);
    private volatile Long targetJobExecutorId = null;
    private volatile boolean oraImplicitCacheEnabled = false;
    private volatile int oraImplicitCacheSize = 0;
    private final ReleasePool releasePool;
    private volatile PriorityResourceManager activeArteResourceManager;
    private final ArrayBlockingQueue<UnitCommandResponse> unitCommandResponsesQueue = new ArrayBlockingQueue<>(1000);
    private final AtomicBoolean immediateMaintenanceRequested = new AtomicBoolean();
    private final SingletonUnitSessionLock singletonUnitLock;
    private volatile long latestVerson = -1;
    private volatile Map<String, byte[]> lastCapturedLicenseFiles = null;

    public Instance() {
        //Host name
        try {
            final InetAddress localHost = InetAddress.getLocalHost();
            host = localHost.getCanonicalHostName() + " (" + localHost.getHostAddress() + ")";
        } catch (UnknownHostException e) {
            host = "localhost";
        }
        try {
            pid = String.valueOf(SystemTools.getCurrentProcessPid());
        } catch (Exception ex) {
            pid = "<unknown>";
        }
        trace = ServerTrace.Factory.newInstance(this);
        listeners = new CopyOnWriteArrayList<>();
        dbQueries = new DbQueries(this);
        singletonUnitLock = new SingletonUnitSessionLock(this);
        units = new UnitsPool(this);
        jobCheckTimeUpdater = new JobCheckTimeUpdater(this);
        releasePool = new ReleasePool(this);
    }

    public final void start(final OracleDataSource oraDataSource, final String proxyOraUser, final Connection dbConnection, final int instanceId, final String instanceName) throws SQLException {
        synchronized (controlSem) {
            if (getState() != InstanceState.STOPPED) {
                throw new InvalidInstanceState(Messages.INSTANCE_IS_NOT_STOPPED);
            }
            getView();// create view to see trace
            thread = new InstanceThread(this, oraDataSource, proxyOraUser, dbConnection, instanceId, instanceName);
            bShuttingDown = false;

            stopIterations = createStopIterations();
            thread.start();
            this.proxyOraUser = proxyOraUser;
        }
    }

    public AtomicLong getLastArteAutoShutdownTimeMillisHolder() {
        return lastArteAutoShutdownTimeMillisHolder;
    }

    private long memUsagePercent(final MemoryUsage usage) {
        return usage.getUsed() * 100 / usage.getMax();
    }

    private void resetMemoryUsageCounters() {
        lastPermGenUsageSeverity = EEventSeverity.DEBUG;
        lastOldGenUsageSeverity = EEventSeverity.DEBUG;
        lastNonHeapUsageSeverity = EEventSeverity.DEBUG;
        lastHeapUsageSeverity = EEventSeverity.DEBUG;
    }

    private void configureLoggers() {
        StarterLog.setFactory(new DelegateLogFactoryImpl());
        //clear cached loggers to ensure that all log events will be handled by
        //our log implementation
        LogFactory.releaseAll();
        FileLog.setFactory(new FileLogFactory() {
            @Override
            public FileLog createFileLog(FileLogOptions options) {
                final FileLog fileLog = new FileLog(options);
                fileLogRegistry.add(new WeakReference<>(fileLog));
                return fileLog;
            }
        });
        RootLogger.getRootLogger().removeAllAppenders();
        RootLogger.getRootLogger().setLevel(org.apache.log4j.Level.ALL);
        RootLogger.getRootLogger().addAppender(new RadixLog4jAppender());
    }

    private static class DelegateLogFactoryImpl extends DelegateLogFactory {

        @Override
        public Log createApacheLog(final String name) {
            return new ServerThreadLog(name);
        }
    }

    private static class LoggerAdapter extends Logger {

        private final ServerTrace trace;

        public LoggerAdapter(final ServerTrace trace) {
            super(null, null);
            this.trace = trace;
        }

        @Override
        public void log(LogRecord record) {
            final EEventSeverity severity = getSeverity(record);
            if (severity != null) {
                trace.put(severity, record.getMessage(), EEventSource.INSTANCE);
            }
        }

        private EEventSeverity getSeverity(final LogRecord record) {
            if (record == null) {
                return null;
            }
            if (record.getLevel().intValue() < Level.INFO.intValue()) {
                return EEventSeverity.DEBUG;
            }
            if (record.getLevel().intValue() < Level.WARNING.intValue()) {
                return EEventSeverity.EVENT;
            }
            if (record.getLevel().intValue() < Level.SEVERE.intValue()) {
                return EEventSeverity.WARNING;
            }
            return EEventSeverity.ERROR;
        }
    }

    public boolean isShuttingDown() {
        if (bShuttingDown) {
            return true;
        }
        final InstanceView v = getView();
        return v != null && v.disposing;
    }

    public void stop(final String reason) {
        trace.put(EEventSeverity.EVENT, Messages.TRY_SHUTDOWN_INSTANCE + reason, Messages.MLS_ID_TRY_SHUTDOWN_INSTANCE, new ArrStr(getFullTitle(), reason), EEventSource.INSTANCE, false);
        synchronized (controlSem) {
            if (thread == null || thread.getState() == Thread.State.TERMINATED || thread.isInterrupted()) {
                return;
            }
            InstanceState state = getState();
            if (state != InstanceState.STARTED && state != InstanceState.STARTING) {
                throw new InvalidInstanceState(Messages.INSTANCE_IS_NOT_STARTED);
            }
            onStartShuttingDown();
            if (!softStop()) {
                hardStop();
            }
        }
    }

    private void onStartShuttingDown() {
        bShuttingDown = true;
    }

    private boolean softStop() {

        if (dispatcher != null) {
            dispatcher.wakeup();
        }
        try {
            thread.join(SHUTDOWN_TIMEOUT_MILLIS);
        } catch (Throwable e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            trace.put(EEventSeverity.ERROR, Messages.ERR_ON_INSTANCE_SHUTDOWN + exStack, Messages.MLS_ID_ERR_ON_INSTANCE_SHUTDOWN, new ArrStr(getFullTitle(), exStack), EEventSource.INSTANCE, false);
        }

        if (thread == null || !thread.isAlive()) {
            return true;
        }

        return false;
    }

    private void hardStop() {
        trace.put(EEventSeverity.WARNING, Messages.TRY_INTERRUPT_INSTANCE, Messages.MLS_ID_TRY_INTERRUPT_INSTANCE, new ArrStr(getFullTitle()), EEventSource.INSTANCE, false);
        thread.interrupt();//hard stop
        try {
            thread.join();
        } catch (InterruptedException ex) {
            //return;
        }
    }

    public ReleasePool getReleasePool() {
        return releasePool;
    }

    public InstanceProfiler getProfiler() {
        return profiler;
    }

    public ISeverityByCodeCalculator getSeverityPreprocessor() {
        return severityPreprocessor;
    }

//	 overridable implementation of instance thread run():  try {startImpl(); runImpl();}finally{stopImpl()} 	
    /**
     * Called only from InstanceThread
     *
     * @param oraDataSource
     * @param proxyOraUser
     * @param dbConnection
     * @param instanceId
     * @param instanceName
     * @throws Exception
     */
    protected void startImpl(final OracleDataSource oraDataSource, final String proxyOraUser, final Connection dbConnection, final int instanceId, final String instanceName) throws Exception {
        id = instanceId;

        resetMemoryUsageCounters();

        configureLoggers();

        trace.enterContext(EEventContextType.SYSTEM_INSTANCE, Long.valueOf(id), null);

        objectCache.clear();

        name = instanceName;

        profiler = new InstanceProfiler(this);

        Thread.currentThread().setName(getFullTitle());

        monitor = new InstanceMonitor(this);
        monitor.start();

        setDbSessionInfo((long) id, getDbSessionOwnerTypeString(), null, dbConnection, trace);
        setDbConnection(oraDataSource, proxyOraUser, dbConnection);

        try {
            readImplicitCacheSettings();
        } catch (SQLException ex) {
            getTrace().put(EEventSeverity.ERROR, "Unable to read cache settings: " + ExceptionTextFormatter.throwableToString(ex), EEventSource.INSTANCE);
        }

        //should be called after setDbConnection(...)
        monitor.rereadSettings();

        lastOptionsRereadMillis = System.currentTimeMillis();

        curTraceOptions = dbQueries.readTraceOptions();

        trace.setProfiles(curTraceOptions.getProfiles());
        trace.startFileLogging(curTraceOptions.getLogOptions());

        if (SrvRunParams.getConfigFile() != null) {
            final String absPath = (new File(SrvRunParams.getConfigFile())).getAbsolutePath();
            trace.put(EEventSeverity.EVENT, String.format(Messages.USING_CONFIG_FILE, absPath), Messages.MLS_ID_USING_CONFIG_FILE, new ArrStr(absPath), EEventSource.INSTANCE, false);
        }

        trace.put(EEventSeverity.EVENT, Messages.USING_ + RadixLoader.getInstance().getDescription(), Messages.MLS_ID_USING_, new ArrStr(RadixLoader.getInstance().getDescription()), EEventSource.INSTANCE, false);
        trace.put(EEventSeverity.EVENT, Messages.STARTER_TEMP_DIR + SystemTools.getTmpDir().getAbsolutePath(), Messages.MLS_ID_STARTER_TEMP_DIR, new ArrStr(SystemTools.getTmpDir().getAbsolutePath()), EEventSource.INSTANCE, false);

        logLocalFileReplacements();

        trace.put(EEventSeverity.EVENT, Messages.TRACE_PROFILE_CHANGED + String.valueOf(curTraceOptions.getProfiles()), Messages.MLS_ID_TRACE_PROFILE_CHANGED, new ArrStr(getFullTitle(), curTraceOptions.getProfiles().toString()), EEventSource.INSTANCE, false);
        trace.put(EEventSeverity.EVENT, Messages.FILE_TRACE_OPTIONS_CHANGED + String.valueOf(curTraceOptions.getLogOptions()), Messages.MLS_ID_FILE_TRACE_OPTIONS_CHANGED, new ArrStr(getFullTitle(), curTraceOptions.getLogOptions().toString()), EEventSource.INSTANCE, false);

        updateLocalSensitiveTracingFinishMillis();

        initKeystore();

        initLicenseClient();

        options = getDbQueries().readOptions();
        final String optionsStr = options.toString();
        getTrace().put(EEventSeverity.EVENT, Messages.START_OPTIONS + optionsStr, Messages.MLS_ID_START_OPTIONS, new ArrStr(getFullTitle(), optionsStr), EEventSource.INSTANCE, false);

        actualizeLoader();

        checkLocalStarterVersion();

        rereadDbConfiguration();

        RadixURLTool.setAppURLStreamHandlerFactory(new ServerURLStreamHandlerFactory());

        threadDumpWriter = new ThreadDumpWriter(getTDumpsDir(curTraceOptions.getLogOptions()), curTraceOptions.getLogOptions().getName(), getTrace().newTracer(EEventSource.INSTANCE.getValue()));
        threadDumpWriter.start();

        arteStateWriter = new ArteStateWriter(this);
        arteStateWriter.start();

        jobCheckTimeUpdater.start();

        RadixObjectInitializationPolicy.set(new RadixObjectInitializationPolicy(true));

        if (options.isUseActiveArteLimits()) {
            activeArteResourceManager = new PriorityResourceManager();
            activeArteResourceManager.setOptions(options.getActiveArteLimitsOptions());
        } else {
            activeArteResourceManager = null;
        }

        lastArteAutoShutdownTimeMillisHolder.set(0);
        artePool = new ArtePool(this);
        artePool.onInstanceOptionsChanged();
        artePool.load();

        waitForArtePoolToLoad();

        artesDbLogFlusher = new ArtesDbLogFlusher(this);
        artesDbLogFlusher.start();

        if (!SrvRunParams.getIsDisableArteDbConnCloseOnClientDisconnect()) {
            arteWatchDog = new ArteWatchDog(this);
            arteWatchDog.start();
        }

        if (SystemPropUtils.getBooleanSystemProp("rdx.disable.async.wait.stats.collector", false)) {
            arteWaitStatsCollector = new ArteWaitStatsCollector(this);
            arteWaitStatsCollector.start();
        }

        if (!SrvRunParams.getIsDevelopmentMode()) {
            units.setUnitsNotStartedInDb();
        }

        units.loadAll();

        tryEnableLocalJobExecutorMode();//should be called between units.loadAll() and units.startAll()

        units.startLoaded(Messages.INSTANCE_START);

        unitCommandResponsesQueue.clear();

        immediateMaintenanceRequested.set(false);

        dispatcher = new EventDispatcher();

        service = new InstanceRcSap(this, dispatcher);
        service.start(getDbConnection());

        dispatcher.waitEvent(new TimerEvent(), this, System.currentTimeMillis() + TIC_MILLIS);

        compareLocalAndDbTime();
        lastCheckDbTimeMillis = System.currentTimeMillis();

        checkPidDiscoveryEnabled();

        stopOsCommand = getDbQueries().getStopOsCommand(); //loading stopOsCommand it will be used if during stop there is no db connection

        final String startOsCommand = getDbQueries().getStartOsCommand();
        if (startOsCommand != null && !startOsCommand.isEmpty()) {
            execOsCommand(startOsCommand);
        }

        if (XBEANS_SYNCRONIZATION_OFF) {
            hackAndTurnOffXbeansSyncronization();
        }
    }

    private void logLocalFileReplacements() {
        final Map<String, String> replacements = RadixLoader.getInstance().getLocalFiles().getAllReplacements();
        if (replacements == null || replacements.isEmpty()) {
            return;
        }
        EEventSeverity entryLogSeverity = EEventSeverity.EVENT;
        if (replacements.size() > COUNT_OF_LOGGED_LOCAL_FILE_OVERRIDES) {
            trace.put(EEventSeverity.EVENT, "More than " + COUNT_OF_LOGGED_LOCAL_FILE_OVERRIDES + " files are locally overridden, full list will be logged with DEBUG level", EEventSource.INSTANCE);
            entryLogSeverity = EEventSeverity.DEBUG;
        }
        for (Map.Entry<String, String> entry : replacements.entrySet()) {

            boolean exists = false;
            try {
                exists = new File(entry.getValue()).exists();
            } catch (Exception ex) {
                //ignore
            }
            if (exists) {
                trace.put(entryLogSeverity, "Replaced '" + entry.getKey() + "' with '" + entry.getValue() + "'", EEventSource.INSTANCE);
            } else {
                trace.put(EEventSeverity.WARNING, "Replacement of '" + entry.getKey() + "' with '" + entry.getValue() + "' is invalid, local file does not exists", EEventSource.INSTANCE);
            }
        }

    }

    private void hackAndTurnOffXbeansSyncronization() {
        try {
            final Field emptyOptsField = XmlOptions.class.getDeclaredField("EMPTY_OPTIONS");
            emptyOptsField.setAccessible(true);
            final XmlOptions emptyOpts = (XmlOptions) emptyOptsField.get(null);
            final Map map = new HashMap();
            map.put(XmlOptions.UNSYNCHRONIZED, null);
            final Field mapField = XmlOptions.class.getDeclaredField("_map");
            mapField.setAccessible(true);
            mapField.set(emptyOpts, map);
        } catch (Exception ex) {
            getTrace().put(EEventSeverity.WARNING, "Unable to turn off xbeans syncronization: " + ExceptionTextFormatter.throwableToString(ex), EEventSource.INSTANCE);
        }
    }

    public Proxy getHttpProxyObject() {
        final InstanceOptions optionsSnapshot = options;
        if (optionsSnapshot != null && optionsSnapshot.getHttpProxy() != null) {
            return new Proxy(Proxy.Type.HTTP, ValueFormatter.parseInetSocketAddress(optionsSnapshot.getHttpProxy()));
        }
        return Proxy.NO_PROXY;
    }

    public Proxy getHttpsProxyObject() {
        final InstanceOptions optionsSnapshot = options;
        if (optionsSnapshot != null && optionsSnapshot.getHttpsProxy() != null) {
            return new Proxy(Proxy.Type.HTTP, ValueFormatter.parseInetSocketAddress(optionsSnapshot.getHttpsProxy()));
        }
        return Proxy.NO_PROXY;
    }

    private void checkLocalStarterVersion() {
        final String localStarterVer = Starter.getRootStarterVersion();
        final String repoStarterVer = Starter.getVersion();
        if (!Objects.equals(localStarterVer, repoStarterVer)) {
            trace.put(EEventSeverity.WARNING, String.format(Messages.INVALID_LOCAL_STARTER_VERSION, localStarterVer, repoStarterVer), Messages.MLS_ID_INVALID_LOCAL_STARTER_VERSION, new ArrStr(localStarterVer, repoStarterVer), EEventSource.INSTANCE, false);
        }
    }

    private void waitForArtePoolToLoad() throws InterruptedException {
        final long artePoolLoadStartTimeMillis = System.currentTimeMillis();
        while (artePool.getArteCountInState(ArteInstance.EState.INIT) > 0) {
            Thread.sleep(SLEEP_ON_ARTE_POOL_LOAD_WAIT_MILLIS);
            if (System.currentTimeMillis() - artePoolLoadStartTimeMillis > MAX_WAIT_FOR_ARTE_POOL_LOAD_MILLIS) {
                break;
            }
        }
    }

    private void readImplicitCacheSettings() throws SQLException {
        try (final PreparedStatement ps = getDbConnection().prepareStatement("select s.useOraImplStmtCache s_use, s.oraImplStmtCacheSize s_size, i.useOraImplStmtCache i_use, i.oraImplStmtCacheSize i_size from rdx_system s, rdx_instance i where s.id = 1 and i.id=?")) {
            ps.setLong(1, getId());
            try (final ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    oraImplicitCacheEnabled = rs.getInt("i_use") > 0;
                    if (rs.wasNull()) {
                        oraImplicitCacheEnabled = rs.getInt("s_use") > 0;
                    }
                    oraImplicitCacheSize = rs.getInt("i_size");
                    if (rs.wasNull()) {
                        oraImplicitCacheSize = rs.getInt("s_size");
                    }
                } else {
                    getTrace().put(EEventSeverity.ERROR, "Unable to read cache settings: empty result set", EEventSource.INSTANCE);
                }
            }
        }

    }

    public boolean enqueueResponseToUnitCommand(final UnitCommandResponse response) {
        if (response != null) {
            if (unitCommandResponsesQueue.offer(response)) {
                getTrace().put(EEventSeverity.DEBUG, "Response to command " + response.getRequestCommand().getTraceId() + " enqueued", EEventSource.INSTANCE);
                immediateMaintenanceRequested.set(true);
                dispatcher.wakeup();
                return true;
            } else {
                getTrace().put(EEventSeverity.ERROR, "Unable to enqueue response to unit command" + response.getRequestCommand().getTraceId(), EEventSource.INSTANCE_SERVICE);
            }
        }
        return false;
    }

    public PriorityResourceManager getActiveArteResourceManager() {
        return activeArteResourceManager;
    }

    public boolean isUseActiveArteLimits() {
        return options.isUseActiveArteLimits();
    }

    public boolean isOraImplicitCacheEnabled() {
        return oraImplicitCacheEnabled;
    }

    public int getOraImplicitCacheSize() {
        return oraImplicitCacheSize;
    }

    private void initKeystore() throws SQLException, InterruptedException {
        KeystoreController.setServerKeystoreType(dbQueries.getKeystoreType());
        String keystorePath = expand(dbQueries.getKeystorePath());
        KeystoreController.setServerKeystorePath(keystorePath);
        if (keystorePath != null) {
            keystorePath = KeystoreController.getServerKeystoreAbsolutePath(keystorePath);
        }
        trace.debug("Server keystore absolute path: " + (keystorePath != null ? keystorePath : "NULL"), EEventSource.INSTANCE, false);
        try {
            //[RADIX-4380] check server's keystore availability
            KeystoreController.checkServerKeystoreAvailability(dbQueries.getKeystoreType(), keystorePath);
        } catch (KeystoreControllerException e) {
            final String message = (KeystoreController.isIncorrectPasswordException(e) ? Messages.INCORRECT_KEYSTORE_PASSWORD : e.getMessage());
            trace.put(EEventSeverity.ERROR, Messages.ERR_WHILE_CHECKING_KEYSTORE + message, null, null, EEventSource.INSTANCE, false);
        }

    }

    private void initLicenseClient() {
        LicenseManager.touch();
        LicenseManager.setDelegateLogger(new LoggerAdapter(trace));
        LicenseManager.start(new ILicenseEnvironment() {

            @Override
            public Map<String, byte[]> getLicenseFilesByLayerURIs() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public String getLicenseServerAddress() {
                return SrvRunParams.getLicenseServerAddress();
            }

            @Override
            public long getLatestVersion() {
                return latestVerson;
            }
        });
    }

    public int convertRadixPriorityToSystemPriority(int appThreadPrioirity) {
        InstanceOptions optionsSnapshot = options;
        if (optionsSnapshot != null) {
            return optionsSnapshot.convertRadixPriorityToSystemPriority(appThreadPrioirity);
        }
        return Thread.NORM_PRIORITY;
    }

    private void tryEnableLocalJobExecutorMode() {
        targetJobExecutorId = null;
        Long candidateId = null;

        if (SrvRunParams.isUseLocalJobExecutor()) {
            for (Unit unit : units) {
                if (Objects.equals(unit.getUnitType(), EUnitType.JOB_EXECUTOR.getValue())) {
                    candidateId = unit.getId();
                    break;
                }
            }
        }

        try {
            try (PreparedStatement st = getDbConnection().prepareStatement("update rdx_instance set targetExecutorId=? where id=?")) {
                if (candidateId == null) {
                    st.setNull(1, Types.INTEGER);
                } else {
                    st.setLong(1, candidateId);
                }
                st.setLong(2, id);
                st.execute();
            }
            targetJobExecutorId = candidateId;
            if (targetJobExecutorId != null) {
                trace.put(EEventSeverity.WARNING, String.format(Messages.LOCAL_JOB_EXECUTOR_IS_USED, "#" + candidateId), EEventSource.INSTANCE);
            }
        } catch (SQLException ex) {
            final String exStack = ExceptionTextFormatter.throwableToString(ex);
            trace.put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + ":\n" + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(exStack), EEventSource.INSTANCE, false);
        }

    }

    private String expand(final String string) {
        final Map<String, String> map = new HashMap<>();
        map.putAll(System.getenv());
        for (String key : System.getProperties().stringPropertyNames()) {
            if (key != null) {
                map.put(key, System.getProperty(key));
            }
        }
        StrSubstitutor substitutor = new StrSubstitutor(map, "${", "}");
        return substitutor.replace(string);
    }

    public List<SrvRunParams.OptionProcessingResult> applyConfigFileOptions(final List<String> options) throws ConfigFileNotSpecifiedException, UnableToLoadOptionsFromFile, DecryptionException {
        final List<SrvRunParams.OptionProcessingResult> readResults = SrvRunParams.rereadFromFile(options, trace.newTracer(EEventSource.INSTANCE.getValue()));
        final List<SrvRunParams.OptionProcessingResult> processingResults = new ArrayList<>();
        for (final SrvRunParams.OptionProcessingResult readResult : readResults) {
            if (SrvRunParams.DETAILED_3RD_PARTY_LOGGING.equals(readResult.getOptionName()) && readResult.isNoSuchOption()) {
                SrvRunParams.setDetailed3rdPartyLoggingEnabled(false);
                processingResults.add(new SrvRunParams.OptionProcessingResult(readResult.getOptionName(), true, ""));
                continue;
            }
            if (!readResult.isSucceed()) {
                processingResults.add(readResult);
                continue;
            }
            try {
                if (SrvRunParams.SENS_TRC_FINISH_TIME.equals(readResult.getOptionName())) {
                    updateLocalSensitiveTracingFinishMillis();
                } else if (SrvRunParams.DB_PWD.equals(readResult.getOptionName())) {
                    SrvRunParams.recieveDbPwd(new SrvRunParams.PasswordReciever() {
                        @Override
                        public void recievePassword(final String password) {
                            oraDataSource.setPassword(password);
                        }
                    });
                } else if (SrvRunParams.USER.equals(readResult.getOptionName())) {
                    oraDataSource.setUser(SrvRunParams.getUser());
                } else if (SrvRunParams.EXTERNAL_AUTH.equals(readResult.getOptionName())) {
                    oraDataSource.setUser("");
                    oraDataSource.setPassword("");
                } else if (SrvRunParams.DETAILED_3RD_PARTY_LOGGING.equals(readResult.getOptionName())) {
                    //ok
                } else {
                    throw new IllegalArgumentException("Illegal option to apply: \"" + readResult.getOptionName() + "\"");
                }
                processingResults.add(readResult);
            } catch (RuntimeException ex) {
                processingResults.add(new SrvRunParams.OptionProcessingResult(readResult.getOptionName(), false, ex.getMessage()));
            }
        }
        return processingResults;
    }

    private void rereadDbConfiguration() {
        DbConfiguration newDbConfiguration;
        try {
            newDbConfiguration = dbQueries.readDbConfiguration();
        } catch (SQLException ex) {
            final String exStack = ExceptionTextFormatter.throwableToString(ex);
            trace.put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + ":\n" + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(exStack), EEventSource.INSTANCE, false);
            newDbConfiguration = new DbConfiguration(Layer.TargetDatabase.ORACLE_DB_TYPE, Layer.TargetDatabase.MIN_ORACLE_VERSION, Collections.<DbOptionValue>singletonList(new DbOptionValue(Layer.TargetDatabase.PARTITIONING_OPTION, EOptionMode.ENABLED)));
        }
        if (!Utils.equals(dbConfiguration, newDbConfiguration)) {
            dbConfiguration = newDbConfiguration;
            getTrace().put(EEventSeverity.EVENT, String.format(Messages.DB_CONFIG_CHANGED, dbConfiguration.toString()), Messages.MLS_ID_DB_CONFIG_CHANGED, new ArrStr(dbConfiguration.toString()), EEventSource.INSTANCE, false);
        }
    }

    public Long getTargetJobExecutorId() {
        return targetJobExecutorId;
    }

    public DbConfiguration getDbConfiguration() {
        return dbConfiguration;
    }

    public final void initKerberos() {
        synchronized (krbSem) {
            if (serviceCreds != null) {
                serviceCreds.dispose();
                serviceCreds = null;
            }
            currentKrbOptions = options.getKerberosOptions();
            final KrbLoginEventsPrinter printer = new KrbLoginEventsPrinter(getTrace(), getFullTitle());
            serviceCreds
                    = KerberosUtils.createCredentials(currentKrbOptions, printer);
        }
    }

    public final KrbServiceOptions getKerberosServiceOptions() {
        synchronized (krbSem) {
            return currentKrbOptions;
        }
    }

    public KerberosCredentials getKerberosServiceCredentials() {
        synchronized (krbSem) {
            if (serviceCreds == null) {//was not inited yet
                final KrbServiceOptions krbOptions;
                try {
                    krbOptions = getDbQueries().readKerberosOptions();
                } catch (InterruptedException exception) {
                    return null;
                }
                if (krbOptions == null) {
                    return null;
                }
                if (!krbOptions.equals(currentKrbOptions)) {
                    currentKrbOptions = krbOptions;
                    final KrbLoginEventsPrinter printer = new KrbLoginEventsPrinter(getTrace(), getFullTitle());
                    serviceCreds = KerberosUtils.createCredentials(currentKrbOptions, printer);
                }
            }
            return serviceCreds;
        }
    }

    private void execOsCommand(final String cmd) throws InterruptedException, IOException {
        getTrace().debug("Executing OS Command:  " + cmd, EEventSource.INSTANCE, false);
        final Process p = Runtime.getRuntime().exec(cmd);
        final int res = p.waitFor();
        getTrace().debug("OS command execution result: " + res, EEventSource.INSTANCE, false);
        try {
            p.getErrorStream().close();
        } catch (IOException e) {
            //do nothing
        }
        try {
            p.getOutputStream().close();
        } catch (IOException e) {
            //do nothing
        }
        try {
            p.getInputStream().close();
        } catch (IOException e) {
            //do nothing
        }
        p.destroy();
    }

    /**
     * Called only from InstanceThread
     *
     * @throws InterruptedException
     */
    protected void runImpl() throws InterruptedException {
        while (!Thread.interrupted()) {
            if (isShuttingDown()) {
                service.stop();
                return;
            }
            try {
                dispatcher.process();
                if (immediateMaintenanceRequested.getAndSet(false)) {
                    dispatcher.unsubscribeFromTimerEvents(this);
                    dispatcher.waitEvent(new TimerEvent(), this, System.currentTimeMillis());
                }
            } catch (IOException e) {
                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                getTrace().put(EEventSeverity.ERROR, Messages.ERR_OF_SERVICE + exStack, Messages.MLS_ID_ERR_OF_SERVICE, new ArrStr(getFullTitle(), exStack), EEventSource.INSTANCE_SERVICE, false);
            }
        }
    }

    public InstanceMonitor getInstanceMonitor() {
        return monitor;
    }

    /**
     * Called only from InstanceThread
     */
    protected void stopImpl() {
        for (Callable iterations : stopIterations) {
            stopIteration(iterations);
        }
    }

    public long getControlServiceSapId() {
        return options.getSapId();
    }

    public ObjectCache getObjectCache() {
        return objectCache;
    }

    public static Instance get() {
        return INSTANCE_GLOBAL_POINTER;
    }

    public JobCheckTimeUpdater getJobCheckTimeUpdater() {
        return jobCheckTimeUpdater;
    }

    protected List<Callable> createStopIterations() {
        final List<Callable> iterations = new ArrayList<Callable>();
        iterations.add(new Callable() {
            @Override
            public Object call() {
                if (service != null) {
                    service.stop();
                    service = null;
                }
                return null;
            }
        });
        iterations.add(new Callable() {
            @Override
            public Object call() {
                if (dispatcher != null) {
                    dispatcher.close();
                    dispatcher = null;

                }
                return null;
            }
        });

        iterations.add(new Callable() {
            @Override
            public Object call() {
                units.unloadAll(Messages.INSTANCE_STOP);
                return null;
            }
        });
        iterations.add(new Callable() {
            @Override
            public Object call() throws InterruptedException {
                if (artePool != null) {
                    try {
                        artePool.shutdown();
                    } finally {
                        artePool = null;
                    }
                }
                return null;
            }
        });
        iterations.add(new Callable() {
            @Override
            public Object call() {
                if (artesDbLogFlusher != null) {
                    artesDbLogFlusher.interrupt();
                    try {
                        artesDbLogFlusher.join(2000);
                    } catch (InterruptedException ex) {
                        //ignore
                    }
                    artesDbLogFlusher = null;

                }
                return null;
            }
        });
        iterations.add(new Callable() {
            @Override
            public Object call() {
                if (arteWatchDog != null) {
                    arteWatchDog.requestStop();
                    try {
                        arteWatchDog.join(2000);
                    } catch (InterruptedException ex) {
                        //ignore
                    }
                    arteWatchDog = null;

                }
                return null;
            }
        });
        iterations.add(new Callable() {
            @Override
            public Object call() {
                if (arteWaitStatsCollector != null) {
                    arteWaitStatsCollector.requestStop();
                    try {
                        arteWaitStatsCollector.join(2000);
                    } catch (InterruptedException ex) {
                        //ignore
                    }
                    arteWaitStatsCollector = null;

                }
                return null;
            }
        });
        iterations.add(new Callable() {
            @Override
            public Object call() {
                jobCheckTimeUpdater.stop();
                return null;
            }
        });
        iterations.add(new Callable() {
            @Override
            public Object call() {
                objectCache.clear();
                return null;
            }
        });
        iterations.add(new Callable() {
            @Override
            public Object call() {
                final CountDownLatch latch = LicenseManager.stop();
                try {
                    latch.await(10, TimeUnit.SECONDS);
                } catch (InterruptedException ex) {
                    //skip
                }
                LicenseManager.setDelegateLogger(null);
                return null;
            }
        });
        iterations.add(new Callable() {
            @Override
            public Object call() {
                if (profiler != null) {
                    profiler.close();
                    profiler = null;
                }
                return null;
            }
        });
        iterations.add(new Callable() {
            @Override
            public Object call() {
                singletonUnitLock.releaseAllLocks();
                return null;
            }
        });

        iterations.add(new Callable() {
            @Override
            public Object call() {
                threadDumpWriter.requestStop();
                try {
                    threadDumpWriter.join(1000);
                } catch (InterruptedException ex) {
                    //ignore
                } finally {
                    threadDumpWriter = null;
                }
                return null;
            }
        });

        iterations.add(new Callable() {
            @Override
            public Object call() {
                arteStateWriter.requestStop();
                try {
                    arteStateWriter.join(1000);
                } catch (InterruptedException ex) {
                    //ignore
                } finally {
                    arteStateWriter = null;
                }
                return null;
            }
        });

        iterations.add(new Callable() {
            @Override
            public Object call() throws IOException, SQLException {
                synchronized (krbSem) {
                    if (serviceCreds != null) {
                        serviceCreds.dispose();
                        serviceCreds = null;
                    }
                }
                return null;
            }
        });

        iterations.add(new Callable() {
            @Override
            public Object call() throws IOException, SQLException {
                RootLogger.getRootLogger().removeAllAppenders();
                return null;
            }
        });
        iterations.add(new Callable() {
            @Override
            public Object call() throws IOException, SQLException {
                StarterLog.setFactory(null);
                LogFactory.releaseAll();
                return null;
            }
        });
        iterations.add(new Callable() {
            @Override
            public Object call() throws InterruptedException, SQLException {
                if (monitor != null) {
                    monitor.shutdown();
                    monitor.flush();
                }
                return null;
            }
        });
        iterations.add(new Callable() {
            @Override
            public Object call() {
                trace.leaveContext(EEventContextType.SYSTEM_INSTANCE, Long.valueOf(id), null);
                return null;
            }
        });
        iterations.add(new Callable() {
            @Override
            public Object call() {
                FileLog.setFactory(null);
                fileLogRegistry.clear();
                return null;
            }
        });
        iterations.add(new Callable() {
            @Override
            public Object call() {
                RadixURLTool.setAppURLStreamHandlerFactory(null);
                return null;
            }
        });
        iterations.add(new Callable() {
            @Override
            public Object call() throws IOException, SQLException {
                if (getDbConnection() != null) { //DBP-1607 if stopping while instance is trying to restore db connection then dbConnection == null
                    //if we have a db connection then let's load actual stopOsCommand
                    stopOsCommand = getDbQueries().getStopOsCommand();
                }
                if (stopOsCommand != null && !stopOsCommand.isEmpty()) {
                    try {
                        execOsCommand(stopOsCommand);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Instance.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                return null;
            }
        });
        return iterations;
    }

    private void stopIteration(final Callable r) {
        try {
            r.call();
        } catch (Throwable e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            getTrace().put(EEventSeverity.ERROR, Messages.ERR_ON_INSTANCE_STOP + exStack, Messages.MLS_ID_ERR_ON_INSTANCE_STOP, new ArrStr(getFullTitle(), exStack), EEventSource.INSTANCE, false);
        }
    }

    public int getId() {
        if (id == 0) {
            throw new IllegalUsageError("Instance is not started");
        }
        return id;
    }

    public String getFullTitle() {
        if (id == 0) {
            return "";
        }
        return Messages.TITLE_INSTANCE + " #" + String.valueOf(id) + " - " + name;
    }

    public InstanceState getState() {
        return state;
    }

    /**
     * Load unit if it exists.
     *
     * @param unitId
     * @return Unit or null if such unit doesn't exist.
     */
    Unit tryToLoadUnit(final int unitId) throws InterruptedException {
        return units.tryToLoadUnit(unitId);
    }

    public void requestArtePoolReload(String reason) {
        artePool.requestReload();
        if (reason == null) {
            reason = "null";
        }
        trace.put(EEventSeverity.EVENT, String.format(Messages.ARTE_POOL_RELOAD_REQUESTED, reason), Messages.MLS_ID_RELOAD_ARTE_POOL_REQUESTED, new ArrStr(reason), EEventSource.INSTANCE, false);
    }

    void setState(final InstanceState newState) throws InterruptedException {
        final InstanceState oldState = state;
        state = newState;

        if (state == InstanceState.STARTED) {
            while (!isShuttingDown()) {
                if (Thread.interrupted()) {
                    throw new InterruptedException("Interrupted while updating database status to " + state.toString());
                }
                try {
                    dbQueries.setDbStartedState(true);
                    break;
                } catch (Throwable ex) {
                    if (ex instanceof InterruptedException) {
                        throw (InterruptedException) ex;
                    }
                    final String exStack = ExceptionTextFormatter.exceptionStackToString(ex);
                    trace.put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + ":\n" + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(exStack), EEventSource.INSTANCE, false);
                    restoreDbConnection();
                }
            }
        }
        if (state == InstanceState.STOPPED) {
            try {
                if (dbConnection != null) {
                    dbQueries.setDbStartedState(false);
                }
            } catch (Throwable ex) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(ex);
                trace.put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + ":\n" + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(exStack), EEventSource.INSTANCE, false);
            }
        }

        fireStateChanged(oldState);
        final String title = getFullTitle();
        trace.put(EEventSeverity.EVENT, Messages.getStateMessage(newState) + (title.length() != 0 ? " \"" + title + "\"" : "") + ", host: " + host + ", PID: " + pid, Messages.getStateMessageMslId(newState), new ArrStr(title, host + ", PID: " + pid), EEventSource.INSTANCE, false);
    }

    public ServerTrace getTrace() {
        return trace;
    }

    public ThreadDumpWriter getThreadDumpWriter() {
        return threadDumpWriter;
    }

    public ArteStateWriter getArteStateWriter() {
        return arteStateWriter;
    }

    public Connection getDbConnection() {
        return dbConnection;
    }

    /**
     * Called only from InstanceThread
     *
     * @throws InterruptedException
     */
    final void setDbConnection(final OracleDataSource oraDataSource, final String proxyOraUser, final Connection dbConnection) {
        this.oraDataSource = oraDataSource;
        this.proxyOraUser = proxyOraUser;
        trace.setDbConnection(dbConnection);
        dbQueries.closeAll();
        singletonUnitLock.closeAllDbQueries();
        if (service != null) {
            service.setDbConnection(dbConnection);
        }
        if (monitor != null) {
            monitor.setConnection(dbConnection);
        }
        try {
            if (this.dbConnection != null && this.dbConnection != dbConnection) {
                this.dbConnection.close();
            }
        } catch (SQLException e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            trace.put(EEventSeverity.WARNING, Messages.ERR_ON_DB_CONNECTION_CLOSE + ": \n" + exStack, null, null, EEventSource.INSTANCE, false);
        }
        this.dbConnection = dbConnection;
    }

    public Connection openNewDbConnection(final String ownerType, final Long ownerId) throws SQLException {
        return openNewDbConnection((long) id, ownerType, ownerId, oraDataSource, proxyOraUser, trace, isOraImplicitCacheEnabled(), getOraImplicitCacheSize());
    }

    public Connection openNewUnitDbConnection(final Unit unit) throws SQLException {
        return openNewDbConnection(EDbSessionOwnerType.UNIT.getValue(), unit.getId());
    }

    public Connection openNewArteDbConnection(final ArteInstance arte) throws SQLException {
        return openNewDbConnection(EDbSessionOwnerType.ARTE.getValue(), arte.getSeqNumber());
    }

    private Connection openNewInstanceDbConnection() throws SQLException {
        return openNewDbConnection(getDbSessionOwnerTypeString(), null);
    }

    private String getDbSessionOwnerTypeString() {
        return "on " + host;
    }

    public static final Connection openNewDbConnection(
            final Long instanceId,
            final String ownerType,
            final Long ownerId,
            final OracleDataSource oraDataSrc,
            final String proxyOraUser,
            final ServerTrace trace,
            final boolean useOraImplStatementsCache,
            final int oraImplStatementsCacheSize) throws SQLException {
        final RadixConnection c = new RadixConnection((oracle.jdbc.OracleConnection) oraDataSrc.getConnection());
        if (proxyOraUser != null) {
            try (PreparedStatement st = c.prepareStatement("alter session set current_schema = \"" + proxyOraUser + "\"")) {
                st.execute();
            }
        }
        c.setAutoCommit(false);
        if (c instanceof OracleConnection && useOraImplStatementsCache) {
            ((OracleConnection) c).setImplicitCachingEnabled(true);
            ((OracleConnection) c).setStatementCacheSize(oraImplStatementsCacheSize);
        }
        setDbSessionInfo(instanceId, ownerType, ownerId, c, trace);
        return c;

    }

    public static final Connection openNewDbConnection(
            final Long instanceId,
            final String ownerType,
            final Long ownerId,
            final OracleDataSource oraDataSrc,
            final String proxyOraUser,
            final ServerTrace trace) throws SQLException {
        return openNewDbConnection(instanceId, ownerType, ownerId, oraDataSrc, proxyOraUser, trace, false, 0);
    }

    private static void setDbSessionInfo(final Long instanceId,
            final String sessionOwnerType,
            final Long sessionOwnerId,
            final Connection connection,
            final ServerTrace trace) {
        try {
            try (PreparedStatement st = connection.prepareStatement("begin RDX_Environment.init(?,?,?); end;")) {
                if (instanceId != null) {
                    st.setLong(1, instanceId);
                } else {
                    st.setNull(1, Types.INTEGER);
                }
                if (sessionOwnerType != null) {
                    st.setString(2, sessionOwnerType);
                } else {
                    st.setNull(2, Types.VARCHAR);
                }
                if (sessionOwnerId != null) {
                    st.setLong(3, sessionOwnerId);
                } else {
                    st.setNull(3, Types.INTEGER);
                }
                st.execute();
                connection.commit();
            }
        } catch (SQLException e) {
            if (trace != null) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                trace.put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + ":\n" + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(exStack), EEventSource.INSTANCE, false);
            }
        }
    }

    public void registerListener(final InstanceListener listener) {
        listeners.add(listener);
    }

    public void unregisterListener(final InstanceListener listener) {
        listeners.remove(listener);
    }

    private void fireStateChanged(final InstanceState oldState) {
        for (InstanceListener listener : listeners) {
            listener.stateChanged(this, oldState, state);
        }
    }

    final DbQueries getDbQueries() {
        return dbQueries;
    }

    public List<Unit> getUnits() {
        return Collections.unmodifiableList(units);
    }

    public List<List<Unit>> getUnitsByPriorities() {
        return units.sortByPriorities(getUnits());
    }

    Unit findUnit(final int unitId) {
        if (getState() != InstanceState.STARTED) {
            throw new InvalidInstanceState(Messages.INSTANCE_IS_NOT_STARTED);
        }
        return units.findUnit(unitId);
    }

    void startAllUnits(final String reason) throws InterruptedException {
        synchronized (controlSem) {
            if (getState() != InstanceState.STARTED) {
                throw new InvalidInstanceState(Messages.INSTANCE_IS_NOT_STARTED);
            }
            units.startAll(reason);
        }
    }

    boolean restartAllUnits(final String reason) throws InterruptedException {
        synchronized (controlSem) {
            if (getState() != InstanceState.STARTED) {
                throw new InvalidInstanceState(Messages.INSTANCE_IS_NOT_STARTED);
            }
            return units.restartAll(reason);
        }
    }

    boolean stopAllUnits(final String reason) {
        synchronized (controlSem) {
            if (getState() != InstanceState.STARTED) {
                throw new InvalidInstanceState(Messages.INSTANCE_IS_NOT_STARTED);
            }
            return units.stopAll(reason);
        }
    }
//maintance
    private long lastActualizeMillis = 0;
    private long lastCheckMemoryUsageMillis = 0;
    private long lastOptionsRereadMillis = 0;
    private long lastDbIAmAliveMillis = 0;
    private long lastProfileFlushMillis = 0;
    private long lastServerKeystoreCheckMillis = 0;
    private long lastMonitorRereadSettingsMillis = 0;
    private long lastMonitorFlushMillis = 0;
    private long lastCheckUnitsMillis = 0;
    private long lastFileLogsMaintenanceMillis = 0;
    private long lastSeverityMapRereadMillis = 0;
    private long lastDbConfigRereadMillis = 0;
    private long lastObjectCacheMaintenanceMillis = 0;
    private long lastBasicStatsWriteMillis = 0;
    private long lastWriteArteStateMillis = 0;
    private long lastCheckDbTimeMillis = 0;
    private EEventSeverity lastPermGenUsageSeverity = EEventSeverity.DEBUG;
    private EEventSeverity lastOldGenUsageSeverity = EEventSeverity.DEBUG;
    private EEventSeverity lastNonHeapUsageSeverity = EEventSeverity.DEBUG;
    private EEventSeverity lastHeapUsageSeverity = EEventSeverity.DEBUG;
    private volatile TraceOptions curTraceOptions = null;

    public void requestMaintenance(String reason) {
        lastActualizeMillis = 0;
        lastCheckMemoryUsageMillis = 0;
        lastOptionsRereadMillis = 0;
        lastDbIAmAliveMillis = 0;
        lastProfileFlushMillis = 0;
        lastServerKeystoreCheckMillis = 0;
        lastMonitorRereadSettingsMillis = 0;
        lastMonitorFlushMillis = 0;
        lastCheckUnitsMillis = 0;
        lastFileLogsMaintenanceMillis = 0;
        lastSeverityMapRereadMillis = 0;
        lastDbConfigRereadMillis = 0;
        lastBasicStatsWriteMillis = 0;
        lastCheckDbTimeMillis = 0;
        units.requestMaintenance();
        if (reason == null) {
            reason = "null";
        }
        trace.put(EEventSeverity.EVENT, String.format(Messages.INSTANCE_MAINTENANCE_REQUESTED, reason), Messages.MLS_ID_INSTANCE_MAINTENANCE_REQUESTED, new ArrStr(reason), EEventSource.INSTANCE, false);
    }

    protected void maintenance() throws InterruptedException {
        try {

            final List<UnitCommandResponse> unitCommandResponses = new ArrayList<>();
            unitCommandResponsesQueue.drainTo(unitCommandResponses);
            service.respondToUnitCommands(unitCommandResponses);

            artePool.maintenance();

            final long curMillis = System.currentTimeMillis();
            if (curMillis - lastDbIAmAliveMillis >= Instance.DB_I_AM_ALIVE_PERIOD_MILLIS) {
                dbQueries.dbIAmStillAlive();
                service.dbSapSelfCheck();
                lastDbIAmAliveMillis = curMillis;
            }

            if (curMillis - lastWriteArteStateMillis >= Instance.WRITE_ARTE_STATE_PERIOD_MILLIS) {
                getArteStateWriter().requestUpdate();
                lastWriteArteStateMillis = curMillis;
            }

            if (curMillis - lastOptionsRereadMillis >= Instance.OPTIONS_REREAD_PERIOD_MILLIS) {
                rereadTraceOptions();
                service.rereadOptions();
                final InstanceOptions newOptions = getDbQueries().readOptions();
                if (!options.equals(newOptions)) {
                    final String newOptionsStr = newOptions.toString();
                    getTrace().put(EEventSeverity.EVENT, Messages.OPTIONS_CHANGED + newOptionsStr, Messages.MLS_ID_OPTIONS_CHANGED, new ArrStr(getFullTitle(), newOptionsStr), EEventSource.INSTANCE, false);
                    if (newOptions.isUseActiveArteLimits()) {
                        if (activeArteResourceManager == null) {
                            activeArteResourceManager = new PriorityResourceManager();
                        }
                        activeArteResourceManager.setOptions(newOptions.getActiveArteLimitsOptions());
                    }
                    options = newOptions;
                    artePool.onInstanceOptionsChanged();
                }
                lastOptionsRereadMillis = curMillis;
                checkLocalStarterVersion();//to persistently cry about wrong starter version
            }
            if (isActualizeVerSchedulled || curMillis - lastActualizeMillis >= Instance.ACTUALIZE_PERIOD_MILLIS) {
                if (isActualizeVerSchedulled || options.getAutoActualizeVer()) {
                    if (isActualizeVerSchedulled) {
                        trace.put(EEventSeverity.EVENT, Messages.CHECK_FOR_UPDATES, Messages.MLS_ID_CHECK_FOR_UPDATES, new ArrStr(getFullTitle()), EEventSource.INSTANCE, false);
                    }
                    actualizeLoader();
                }
                lastActualizeMillis = curMillis;
            }

            units.maintenance();

            if (curMillis - lastProfileFlushMillis >= options.getProfilePeriodMillis()) {
                getProfiler().flush();
                lastProfileFlushMillis = curMillis;
            }

            if (curMillis - lastServerKeystoreCheckMillis >= Instance.KEYSTORE_REREAD_PERIOD_MILLIS) {
                if (KeystoreController.checkServerKeystoreModified()) {
                    trace.put(EEventSeverity.EVENT, Messages.SERVER_KEYSTORE_WAS_REREAD, null, null, EEventSource.INSTANCE, false);
                }
                lastServerKeystoreCheckMillis = curMillis;
            }

            if (curMillis - lastFileLogsMaintenanceMillis >= Instance.FILE_LOGS_MAINTENANCE_PERIOD_MILLIS) {
                fileLogsMaintenance();
                lastFileLogsMaintenanceMillis = curMillis;
            }

            if (monitor != null) {
                monitor.maintenance();

                if (curMillis - lastCheckUnitsMillis >= Instance.CHECK_UNIT_HANG_PERIOD_MILLIS) {
                    monitor.checkHungUnits();
                    lastCheckUnitsMillis = curMillis;
                }

                if (curMillis - lastMonitorFlushMillis >= Instance.MONITOR_FLUSH_PERIOD_MILLIS) {
                    monitor.flush();
                    lastMonitorFlushMillis = curMillis;
                }

                if (curMillis - lastMonitorRereadSettingsMillis >= Instance.MONITOR_REREAD_SETTINGS_PERIOD_MILLIS) {
                    monitor.rereadSettings();
                    lastMonitorRereadSettingsMillis = curMillis;
                }

                if (curMillis - lastBasicStatsWriteMillis >= Instance.WRITE_BASIC_STATS_PERIOD_MILLIS) {
                    monitor.writeBasicStats();
                    lastBasicStatsWriteMillis = curMillis;
                }
            }

            if (options.isMemCheckOn()
                    && curMillis - lastCheckMemoryUsageMillis >= options.getMemCheckPeriodMillis()) {
                checkMemoryUsage();
                lastCheckMemoryUsageMillis = curMillis;
            }

            if (curMillis - lastSeverityMapRereadMillis > SEVERITY_PREPROCESSING_MAP_REREAD_MILLIS) {
                severityPreprocessor = new ISeverityByCodeCalculator() {
                    private final Map<String, EEventSeverity> codeToSeverity = dbQueries.getSeverityByCodeMap();

                    @Override
                    public EEventSeverity getEventSeverityByCode(String code) {
                        if (code == null) {
                            return null;
                        }
                        return codeToSeverity.get(code);
                    }
                };
                lastSeverityMapRereadMillis = curMillis;
            }

            if (curMillis - lastDbConfigRereadMillis > DB_CONFIGURATION_REREAD_PERIOD_MILLIS) {
                rereadDbConfiguration();
                lastDbConfigRereadMillis = curMillis;
            }

            if (curMillis - lastObjectCacheMaintenanceMillis > OBJECT_CACHE_MAINTENANCE_PERIOD_MILLIS) {
                objectCache.maintenance();
                lastObjectCacheMaintenanceMillis = curMillis;
            }

            if (curMillis - lastCheckDbTimeMillis > CHECK_DB_TIME_PERIOD_MILLIS) {
                compareLocalAndDbTime();
                lastCheckDbTimeMillis = curMillis;
            }

            final boolean krbOptionsChanged;
            synchronized (krbSem) {
                krbOptionsChanged = serviceCreds != null && !options.getKerberosOptions().equals(currentKrbOptions);
            }
            if (krbOptionsChanged) {
                initKerberos();
            }
            getTrace().flush();
        } catch (SQLException e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            trace.put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + ":\n" + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(exStack), EEventSource.INSTANCE, false);
            restoreDbConnection();
        }
    }

    private void fileLogsMaintenance() {
        final List<WeakReference<FileLog>> toRemove = new ArrayList<>();
        for (WeakReference<FileLog> ref : fileLogRegistry) {
            final FileLog log = ref.get();
            if (log != null) {
                try {
                    log.maintenance();
                } catch (Exception ex) {
                    trace.put(EEventSeverity.ERROR, String.format(Messages.ERR_ON_FILE_LOG_MAINTENANCE, log.getName(), ExceptionTextFormatter.throwableToString(ex)), Messages.MLS_ID_ERR_ON_FILE_LOG_MAINTENANCE, new ArrStr(log.getName(), ExceptionTextFormatter.throwableToString(ex)), EEventSource.INSTANCE, false);
                }
            } else {
                toRemove.add(ref);
            }
        }
        if (!toRemove.isEmpty()) {
            fileLogRegistry.removeAll(toRemove);
        }
    }

    private void checkMemoryUsage() {
        final MemoryMXBean allMemoryBean = ManagementFactory.getMemoryMXBean();
        boolean bNeedGc = (memUsagePercent(allMemoryBean.getHeapMemoryUsage()) > Instance.MEM_USAGE_EV_PERCENT)
                || (memUsagePercent(allMemoryBean.getNonHeapMemoryUsage()) > Instance.MEM_USAGE_EV_PERCENT);
        final List<MemoryPoolMXBean> memPoolMxBeans = ManagementFactory.getMemoryPoolMXBeans();
        for (MemoryPoolMXBean poolBean : memPoolMxBeans) {
            if (bNeedGc) {
                break;
            }
            bNeedGc = memUsagePercent(poolBean.getUsage()) > Instance.MEM_USAGE_EV_PERCENT;
        }
        if (bNeedGc) {
            trace.debug("Starting explicit garbage collection", EEventSource.INSTANCE, false);
            System.gc();//NOPMD
            trace.debug("Explicit garbage collection finished", EEventSource.INSTANCE, false);
        }
        lastHeapUsageSeverity = checkMemoryUsageImpl(allMemoryBean.getHeapMemoryUsage(), lastHeapUsageSeverity, "Heap");
        lastNonHeapUsageSeverity = checkMemoryUsageImpl(allMemoryBean.getNonHeapMemoryUsage(), lastNonHeapUsageSeverity, "Non-Heap");
        for (MemoryPoolMXBean poolBean : memPoolMxBeans) {
            final String lowerName = poolBean.getName().toLowerCase();
            if (poolBean.isValid()) {
                if (lowerName.contains("perm") && lowerName.contains("gen")) {
                    lastPermGenUsageSeverity = checkMemoryUsageImpl(poolBean.getUsage(), lastPermGenUsageSeverity, "Perm Gen");
                } else if (lowerName.contains("old") && lowerName.contains("gen")) {
                    lastOldGenUsageSeverity = checkMemoryUsageImpl(poolBean.getUsage(), lastOldGenUsageSeverity, "Old Gen");
                }

            }
        }
    }

    private EEventSeverity checkMemoryUsageImpl(final MemoryUsage usage, final EEventSeverity lastSeverity, final String memoryType) {
        if (usage == null || usage.getMax() == -1) {
            return EEventSeverity.DEBUG;
        }
        final long currentPercent = memUsagePercent(usage);
        final EEventSeverity currentSeverity = getSeveretyForMemUsageLevel(currentPercent);

        if (currentSeverity == lastSeverity) {
            return currentSeverity;
        }
        final String currentConsumption = currentPercent + "%";

        final ArrStr words = new ArrStr(memoryType, currentConsumption);

        if (currentSeverity.getValue() < lastSeverity.getValue()) {
            final String message = MessageFormat.format(Messages.MEMORY_CONSUMPTION_DECREASED_TO, memoryType, currentConsumption);
            trace.put(EEventSeverity.EVENT, message, Messages.MLS_ID_MEMORY_CONSUMPTION_DECREASED_TO, words, EEventSource.INSTANCE, false);
        } else {
            final String message = MessageFormat.format(Messages.MEMORY_CONSUMPTION_INCREASED_TO, memoryType, currentConsumption);
            trace.put(currentSeverity, message, memUsageSevToEventCode(currentSeverity), words, EEventSource.INSTANCE, false);
        }
        return currentSeverity;
    }

    private String memUsageSevToEventCode(final EEventSeverity severity) {
        if (severity == null) {
            return null;
        }
        switch (severity) {
            case EVENT:
                return Messages.MLS_ID_MEMORY_CONSUMPTION_INCREASED_TO_EVENT;
            case WARNING:
                return Messages.MLS_ID_MEMORY_CONSUMPTION_INCREASED_TO_WARNING;
            case ERROR:
                return Messages.MLS_ID_MEMORY_CONSUMPTION_INCREASED_TO_ERROR;
            case ALARM:
                return Messages.MLS_ID_MEMORY_CONSUMPTION_INCREASED_TO_ALARM;
            default:
                return null;
        }
    }

    private EEventSeverity getSeveretyForMemUsageLevel(final long percent) {
        if (percent < Instance.MEM_USAGE_EV_PERCENT) {
            return EEventSeverity.DEBUG;
        }
        if (percent < Instance.MEM_USAGE_WARN_PERCENT) {
            return EEventSeverity.EVENT;
        }
        if (percent < Instance.MEM_USAGE_ERR_PERCENT) {
            return EEventSeverity.WARNING;
        }
        if (percent < Instance.MEM_USAGE_ALARM_PERCENT) {
            return EEventSeverity.ERROR;
        }
        return EEventSeverity.ALARM;
    }

    /**
     * Called only from InstanceThread
     *
     * @throws InterruptedException
     */
    void restoreDbConnection() throws InterruptedException {
        trace.put(EEventSeverity.EVENT, Messages.TRY_RESTORE_DB_CONNECTION, null, null, EEventSource.INSTANCE, false);
        setDbConnection(oraDataSource, proxyOraUser, null);
        while (!isShuttingDown()) {
            try {
                setDbConnection(oraDataSource, proxyOraUser, openNewInstanceDbConnection());
                final String title = getFullTitle();
                trace.put(EEventSeverity.EVENT, Messages.DB_CONNECTION_RESTORED, Messages.MLS_ID_DB_CONNECTION_RESTORED, new ArrStr(title), EEventSource.INSTANCE, false);
                break;
            } catch (SQLException e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                trace.put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + ":\n" + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(exStack), EEventSource.INSTANCE, false);
                Thread.sleep(DB_RECONNECT_TRY_PERIOD_MILLIS);
            }
        }
    }
    /**
     * @NotThreadsafe Should be accessed only from instance's thread
     */
    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm dd/MM/yy");

    /**
     * @NotThreadsafe shoud be accessed only from instance thread
     */
    private void rereadTraceOptions() throws SQLException, InterruptedException {
        final TraceOptions traceOptions = dbQueries.readTraceOptions();
        if (traceOptions.equals(curTraceOptions)) {
            return;
        }
        final boolean bProfilesChanged = !curTraceOptions.getProfiles().equals(traceOptions.getProfiles());
        final boolean bLogOptionsChanged = !curTraceOptions.getLogOptions().equals(traceOptions.getLogOptions());
        final boolean bSensTracing = curTraceOptions.isGlobalSensitiveTracingOn() != traceOptions.isGlobalSensitiveTracingOn();
        final long oldSensTraceFinishTime = getSensitiveTracingFinishMillis();
        curTraceOptions = traceOptions;
        if (bProfilesChanged) {
            trace.put(EEventSeverity.EVENT, Messages.TRACE_PROFILE_CHANGED + String.valueOf(traceOptions.getProfiles()), Messages.MLS_ID_TRACE_PROFILE_CHANGED, new ArrStr(getFullTitle(), traceOptions.getProfiles().toString()), EEventSource.INSTANCE, false);
            trace.setProfiles(traceOptions.getProfiles());
        }
        if (bLogOptionsChanged) {
            trace.put(EEventSeverity.EVENT, Messages.FILE_TRACE_OPTIONS_CHANGED + String.valueOf(curTraceOptions.getLogOptions()), Messages.MLS_ID_FILE_TRACE_OPTIONS_CHANGED, new ArrStr(getFullTitle(), curTraceOptions.getLogOptions().toString()), EEventSource.INSTANCE, false);
            trace.changeFileLoggingOptions(traceOptions.getLogOptions());
            applyNewFileLogOptions();
            threadDumpWriter.setTdumpsDir(getTDumpsDir(traceOptions.getLogOptions()));
        }
        if (bSensTracing) {
            if (curTraceOptions.isGlobalSensitiveTracingOn()) {
                trace.put(EEventSeverity.EVENT, Messages.GLOBAL_SENS_TRC_ON, Messages.MLS_ID_GLOBAL_SENS_TRC_ON, null, EEventSource.INSTANCE, false);
            } else {
                trace.put(EEventSeverity.EVENT, Messages.GLOBAL_SENS_TRC_OFF, Messages.MLS_ID_GLOBAL_SENS_TRC_OFF, null, EEventSource.INSTANCE, false);
            }
        }
        if (getSensitiveTracingFinishMillis() != oldSensTraceFinishTime && getSensitiveTracingFinishMillis() > System.currentTimeMillis()) {
            logSensitiveTraceTimeChanged();
        }
    }

    public Collection<String> getArteLoadedClasses() {
        final Set<String> result = new HashSet<>();
        for (ArteInstance arteInst : getArtePool().getInstances(false)) {
            result.addAll(arteInst.getClassesLoadedByArteClassLoader());
        }
        return result;
    }

    private File getTDumpsDir(final FileLogOptions fileLogOptions) {
        return new File(fileLogOptions.getDir(), TDUMPS_DIR_NAME);
    }

//TIC event
    /**
     * Called only from InstanceThread
     *
     * @param ev
     */
    @Override
    public void onEvent(final Event ev) {
        if (ev instanceof TimerEvent) {
            try {
                maintenance();
                if (!isShuttingDown()) {
                    dispatcher.waitEvent(new TimerEvent(), this, System.currentTimeMillis() + TIC_MILLIS);
                }
            } catch (InterruptedException e) {
                //do not wait next tic
            }
        } else {
            throw new IllegalUsageError("Invalid event " + ev);
        }
    }

    protected InstanceView newInstanceView() {
        return InstanceView.newInstanceView(this);
    }

    public final InstanceView getView() {
        if (view == null && SrvRunParams.getIsGuiOn()) {
            view = newInstanceView();
        }
        return view;
    }

    public final void disposeView() {
        if (view != null) {
            view.dispose();
        }
    }

    public final void stopAndUnloadUnit(final Unit unit, final String reason) {
        if (units.stopUnit(unit, reason)) {
            units.unloadUnit(unit);
        }
    }

    public final SingletonUnitSessionLock getSingletonUnitLock() {
        return singletonUnitLock;
    }

    public long getLatestVersion() {
        return latestVerson;
    }

    void actualizeLoader() {
        isActualizeVerSchedulled = false;
        try {
            final List<RadixLoaderActualizer.DdsVersionWarning> ddsWarnings = new LinkedList<>();
            final Connection db = getDbConnection();
            RadixLoaderActualizer.getInstance().actualize(db, ddsWarnings, !options.getAutoActualizeVer());
            final long curVer = RadixLoader.getInstance().getCurrentRevision();
            latestVerson = curVer;
            final boolean wereReportedDdsWarnigs = !reportedDdsWarnings.isEmpty();
            if (oldVersion != curVer) {
                final String curVerStr = String.valueOf(curVer) + " (" + RadixLoader.getInstance().getCurrentRevisionMeta().getLayerVersionsString() + ")";
                trace.put(EEventSeverity.EVENT, Messages.VER_SWITCHED_ + curVerStr, Messages.MLS_ID_VER_SWITCHED, new ArrStr(getFullTitle(), curVerStr), EEventSource.INSTANCE, false);
                oldVersion = curVer;
                reportedDdsWarnings.clear();
            }
            if (ddsWarnings.isEmpty() && wereReportedDdsWarnigs) {
                trace.put(EEventSeverity.EVENT, Messages.DB_IS_ACTUAL, Messages.MLS_ID_DB_IS_ACTUAL, new ArrStr(getFullTitle()), EEventSource.INSTANCE, false);
                reportedDdsWarnings.clear();
            } else {
                for (RadixLoaderActualizer.DdsVersionWarning w : ddsWarnings) {
                    if (!reportedDdsWarnings.contains(w)) {
                        if (w.getUpgradeToVer() != null || w.getUpgrateStartTime() != null) {
                            if (w.isDbStructCompatible()) {
                                trace.put(EEventSeverity.WARNING, MessageFormat.format(Messages.WARNING_DDS_UPGRADE, w.getLayerUri(), w.getUpgradeToVer(), w.getUpgrateStartTime()), Messages.MLS_ID_WARNING_DDS_UPGRADE, new ArrStr(getFullTitle(), w.getLayerUri(), w.getUpgradeToVer(), w.getUpgrateStartTime()), EEventSource.INSTANCE, false);
                            } else {
                                trace.put(EEventSeverity.ERROR, MessageFormat.format(Messages.ERR_DDS_UNCOMPATIBLE_UPGRADE, w.getLayerUri(), w.getUpgradeToVer(), w.getUpgrateStartTime()), Messages.MLS_ID_ERR_DDS_UNCOMPATIBLE_UPGRADE, new ArrStr(getFullTitle(), w.getLayerUri(), w.getUpgradeToVer(), w.getUpgrateStartTime()), EEventSource.INSTANCE, false);
                            }
                        } else {
                            if (w.isDbStructCompatible()) {
                                trace.put(EEventSeverity.WARNING, MessageFormat.format(Messages.ERR_DDS_VER, w.getLayerUri(), w.getServerVer(), w.getDbVer()), Messages.MLS_ID_ERR_DDS_VER, new ArrStr(getFullTitle(), w.getLayerUri(), w.getServerVer(), w.getDbVer()), EEventSource.INSTANCE, false);
                            } else {
                                trace.put(EEventSeverity.ERROR, MessageFormat.format(Messages.ERR_UNCOMPATIBLE_DDS_VER, w.getLayerUri(), w.getServerVer(), w.getDbCompatibleVersions()), Messages.MLS_ID_ERR_UNCOMPATIBLE_DDS_VER, new ArrStr(getFullTitle(), w.getLayerUri(), w.getServerVer(), w.getDbCompatibleVersions()), EEventSource.INSTANCE, false);
                            }
                        }
                        reportedDdsWarnings.add(w);
                    }
                }
            }
            captureLicenseFilesIfNecessary();
        } catch (RadixLoaderException ex) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(ex);
            trace.put(EEventSeverity.ERROR, Messages.ERR_ON_LOADER_ACTUALIZE + ": \n" + exStack, Messages.MLS_ID_ERR_ON_LOADER_ACTUALIZE, new ArrStr(getFullTitle(), exStack), EEventSource.INSTANCE, false);
        } catch (KernelVersionError ex) {
            trace.put(EEventSeverity.WARNING, String.format(Messages.ERR_KERNEL_VER, ex.getLoadedVersion(), ex.getActualVersion()), Messages.MLS_ID_ERR_KERNEL_VER, new ArrStr(getFullTitle(), String.valueOf(ex.getLoadedVersion()), String.valueOf(ex.getActualVersion())), EEventSource.INSTANCE, false);
            restartServer("kernel version was changed");
        }
    }
    
    private void captureLicenseFilesIfNecessary() {
        if (SrvRunParams.getLicenseServerAddress() == null) {
            final Map<String, byte[]> uriToLicense = new HashMap<>();
            for (LayerMeta layerMeta : RadixLoader.getInstance().getCurrentRevisionMeta().getAllLayersSortedFromBottom()) {
                final FileMeta fileMeta = RadixLoader.getInstance().getCurrentRevisionMeta().findFile(layerMeta.getUri() + "/licenses.xml");
                if (fileMeta != null) {
                    try {
                        final byte[] content = RadixLoader.getInstance().readFileData(fileMeta, RadixLoader.getInstance().getCurrentRevisionMeta());
                        uriToLicense.put(layerMeta.getUri(), content);
                    } catch (IOException ex) {
                        boolean oldUsed = false;
                        if (lastCapturedLicenseFiles != null) {
                            final byte[] prevContent = lastCapturedLicenseFiles.get(layerMeta.getUri());
                            if (prevContent != null) {
                                uriToLicense.put(layerMeta.getUri(), prevContent);
                                oldUsed = true;
                            }
                        }
                        trace.put(EEventSeverity.ERROR, "Unable to load license file for layer '" + layerMeta.getUri() + "'" + (oldUsed ? ", using previously loaded license data" : "")  + ": " + ExceptionTextFormatter.throwableToString(ex), EEventSource.INSTANCE);
                    }
                }
            }
        }
    }

    protected void restartServer(final String reason) {
        Starter.mustRestart(SrvRunParams.getRestartParams());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    stop("restart (" + reason + ")");
                } catch (Exception ex) {
                    //ignore
                }
                if (getState() == InstanceState.STOPPED) {
                    if (SrvRunParams.getIsGuiOn()) {
                        getView().dispose();
                    }
                    LicenseManager.destroy();
                }
            }
        }, "Old instance killer").start();
    }

    public UnitDescription getStartedUnitId(final Long unitType) throws SQLException {
        return getDbQueries().getStartedUnitId(unitType);
    }

    public long getMainSchedulerUnitId(final long schedulerUnitId) {
        try {
            return dbQueries.getParentSchedulerId(schedulerUnitId);
        } catch (SQLException ex) {
            final String exStack = ExceptionTextFormatter.throwableToString(ex);
            trace.put(EEventSeverity.ERROR, org.radixware.kernel.server.instance.Messages.ERR_IN_DB_QRY + ":\n" + exStack, org.radixware.kernel.server.instance.Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(exStack), EEventSource.INSTANCE, false);
        }
        return -1;
    }

    public long getMainMqUnitId(final long mqUnit) {
        try {
            return dbQueries.getParentMqUnitId(mqUnit);
        } catch (SQLException ex) {
            final String exStack = ExceptionTextFormatter.throwableToString(ex);
            trace.put(EEventSeverity.ERROR, org.radixware.kernel.server.instance.Messages.ERR_IN_DB_QRY + ":\n" + exStack, org.radixware.kernel.server.instance.Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(exStack), EEventSource.INSTANCE, false);
        }
        return -1;
    }

    public UnitDescription getStartedDuplicatedScheduler(final JobSchedulerUnit unit) throws SQLException {
        if (getState() != InstanceState.STARTED && getState() != InstanceState.STARTING) {
            throw new InvalidInstanceState(Messages.INSTANCE_IS_NOT_STARTED);
        }
        return getDbQueries().getStartedDuplicatedScheduler(unit.getParentSchedulerId());
    }

    public UnitDescription getStartedDuplicatedExecutor(final JobExecutorUnit unit) throws SQLException {
        if (getState() != InstanceState.STARTED && getState() != InstanceState.STARTING) {
            throw new InvalidInstanceState(Messages.INSTANCE_IS_NOT_STARTED);
        }
        return getDbQueries().getStartedDuplicatedExecutorUnitId(unit);
    }

    public UnitDescription getStartedDuplicatedMqUnit(final MqUnit unit) throws SQLException {
        if (getState() != InstanceState.STARTED && getState() != InstanceState.STARTING) {
            throw new InvalidInstanceState(Messages.INSTANCE_IS_NOT_STARTED);
        }
        return getDbQueries().getStartedDuplicatedMqUnit(unit.getParentId());
    }

    boolean startUnit(final Unit unit, final String reason) throws InterruptedException {
        return units.startUnit(unit, reason);
    }

    boolean stopUnit(final Unit unit, final String reason) {
        return units.stopUnit(unit, reason);
    }

    public FileLogOptions getFileLogOptions() {
        final TraceOptions options = curTraceOptions; //saving mutable field value
        if (options == null) {
            return null;
        }
        return options.getLogOptions();
    }

    public boolean isGlobalSensitiveTracingOn() {
        final TraceOptions options = curTraceOptions; //saving mutable field value
        if (options == null) {
            return false;
        }
        return options.isGlobalSensitiveTracingOn();
    }

    public long getLocalSensitiveTracingFinishMillis() {
        return localSensitiveTracingFinishMillis;
    }

    public void updateLocalSensitiveTracingFinishMillis() {
        final long oldTimeMillis = getSensitiveTracingFinishMillis();
        this.localSensitiveTracingFinishMillis = SrvRunParams.getLocalSensitiveTracingFinishMillis();
        if (oldTimeMillis != getSensitiveTracingFinishMillis() && getSensitiveTracingFinishMillis() > System.currentTimeMillis()) {
            logSensitiveTraceTimeChanged();
        }
    }

    private void logSensitiveTraceTimeChanged() {
        getTrace().put(EEventSeverity.WARNING, Messages.SENSITIVE_TRACE_TIME_IS_SET_TO + " " + new Date(getSensitiveTracingFinishMillis()), EEventSource.INSTANCE);
    }

    public long getSensitiveTracingFinishMillis() {
        if (isGlobalSensitiveTracingOn()) {
            return getLocalSensitiveTracingFinishMillis();
        }
        return 0;
    }

    private void checkPidDiscoveryEnabled() {
        final Set<Integer> runningProcesses = SystemTools.getRunningProcessPids();
        if (runningProcesses == null || runningProcesses.isEmpty() || !runningProcesses.contains(SystemTools.getCurrentProcessPid())) {
            getTrace().put(EEventSeverity.WARNING, Messages.WARN_RUNNING_PIDS_DISCOVERY_BROKEN, Messages.MLS_ID_WARN_RUNNING_PIDS_DISCOVERY_BROKEN, null, EEventSource.INSTANCE, false);
        }
    }

    private void compareLocalAndDbTime() throws SQLException {
        final Connection db = getDbConnection();
        if (db == null) {
            return;
        }
        final PreparedStatement st = db.prepareStatement("select to_char(systimestamp, 'yyyy-mm-dd hh24:mi:ss'), to_char( current_timestamp, 'yyyy-mm-dd hh24:mi:ss') from dual");
        try {
            final ResultSet rs = st.executeQuery();
            try {
                final long javaTime = System.currentTimeMillis();
                rs.next();
                final Timestamp dbSysTime = Timestamp.valueOf(rs.getString(1));
                final Timestamp dbCurrentTime = Timestamp.valueOf(rs.getString(2));
                final DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
                final String javaTimeStr = format.format(new Timestamp(javaTime));
                if (Math.abs(dbSysTime.getTime() - javaTime) > 60000) {
                    final String dbSysTimeStr = format.format(dbSysTime);
                    trace.put(EEventSeverity.ERROR, MessageFormat.format(Messages.ERR_APP_DB_MISTIMING, javaTimeStr, "systimestamp: " + dbSysTimeStr), Messages.MLS_ID_ERR_APP_DB_MISTIMING, new ArrStr(getFullTitle(), javaTimeStr, "systimestamp: " + dbSysTimeStr), EEventSource.INSTANCE, false);
                }
                if (Math.abs(dbCurrentTime.getTime() - javaTime) > 60000) {
                    final String dbCurrentTimeStr = format.format(dbCurrentTime);
                    trace.put(EEventSeverity.WARNING, MessageFormat.format(Messages.WARN_APP_DB_LOCAL_MISTIMING, javaTimeStr, "current_timestamp: " + dbCurrentTimeStr), Messages.MLS_ID_WARNING_APP_DB_LOCAL_MISTIMING, new ArrStr(getFullTitle(), javaTimeStr, "current_timestamp: " + dbCurrentTimeStr), EEventSource.INSTANCE, false);
                }
            } finally {
                rs.close();
            }
        } finally {
            st.close();
        }
    }

    public final DataSource getDataSource() {
        return oraDataSource;
    }

    public boolean getAutoActualizeVer() {
        return options.getAutoActualizeVer();
    }

    void scheduleActualizeVer() {
        isActualizeVerSchedulled = true;
    }

    void scheduleRefreshUnitsList() {
        units.scheduleRefreshUnitsList();
    }

    public String getScpName() {
        return options.getScpName();
    }

    public Long getArteInstLiveTimeMin() {
        return options.getArteInstLiveTimeMin();
    }

    public int getMinArteInstCount() {
        return options.getMinArteInstCount();
    }

    public int getNormalArteInstCount() {
        return options.getNormalArteInstCount();
    }

    public int getAboveNormalArteInstCount() {
        return options.getAboveNormalArteInstCount();
    }

    public int getHighArteInstCount() {
        return options.getHighArteInstCount();
    }

    public int getVeryHighArteInstCount() {
        return options.getVeryHighArteInstCount();
    }

    public int getCriticalArteInstCount() {
        return options.getCriticalArteInstCount();
    }

    public int getTotalMaxArteInstCount() {
        return options.getNormalArteInstCount() + options.getAboveNormalArteInstCount()
                + options.getHighArteInstCount() + options.getVeryHighArteInstCount()
                + options.getCriticalArteInstCount();
    }

    public String getHttpProxy() {
        return options.getHttpProxy();
    }

    public String getHttpsProxy() {
        return options.getHttpsProxy();
    }

    public ArtePool getArtePool() {
        return artePool;
    }

    public void applyNewFileLogOptions() {
        if (artePool != null) {
            for (ArteInstance i : artePool.getInstances(false)) {
                i.applyNewFileLogOptions();
            }
        }
        if (units != null) {
            for (Unit u : units) {
                u.applyNewFileLogOptions();
            }
        }
    }

    public static FileLogOptions readFileLogOptions(final long id, final Connection connection) throws Exception {
        final Instance fakeInstance = new Instance() {//dirty
            @Override
            public int getId() {
                return SrvRunParams.getInstanceId();
            }

            @Override
            public Connection getDbConnection() {
                return connection;
            }
        };
        final DbQueries queries = new DbQueries(fakeInstance);
        try {
            return queries.readTraceOptions().getLogOptions();//could throw NullPointerException on accessing to fakeInstance.getTrace() on unexpected error
        } finally {
            queries.closeAll();
        }
    }

    public static boolean isRunning(final long id, final Connection connection) throws SQLException {
        final int timeoutSeconds = Instance.DB_I_AM_ALIVE_TIMEOUT_MILLIS / 1000;
        final PreparedStatement st = connection.prepareStatement("select 1 from rdx_instance where started = 1 and (sysdate < selfchecktime + numtodsinterval(?, 'SECOND')) and id = ?");
        try {
            st.setInt(1, timeoutSeconds);
            st.setLong(2, id);
            final ResultSet rs = st.executeQuery();
            try {
                if (rs.next()) {
                    return true;
                }
            } finally {
                rs.close();
            }
        } finally {
            st.close();
        }
        return false;
    }

    public static class ReleasePool {

        //@GuardedBy this
        private Release latestRelease;//strong ref to latest release
        //@GuaredBy this
        private final Map<Long, ReleaseLoader> loadedReleases = new HashMap<>();
        private final Instance instance;

        public ReleasePool(Instance instance) {
            this.instance = instance;
        }

        public Release get(final Long version) throws ReleaseLoadException {
            ReleaseLoader loader;
            synchronized (this) {
                loader = loadedReleases.get(version);
                if (loader == null) {
                    loader = new ReleaseLoader(version.longValue(), new LoadErrorsLog() {
                        @Override
                        protected void logError(Id defId, RadixError err) {
                            instance.getTrace().put(EEventSeverity.ERROR, "Unable to load definition '" + defId + "': " + ExceptionTextFormatter.throwableToString(err), EEventSource.DEF_MANAGER);
                        }
                    });
                    loadedReleases.put(version, loader);
                }
            }
            final Release release = loader.get();
            synchronized (this) {
                if (latestRelease == null || latestRelease.getRevisionMeta().getNum() < release.getRevisionMeta().getNum()) {
                    latestRelease = release;
                }
            }
            return release;
        }

        public synchronized void clear() {
            latestRelease = null;
            loadedReleases.clear();
        }
    }

    public static class ServerURLStreamHandlerFactory implements URLStreamHandlerFactory {

        public static final String DATASCHEME_PROTOCOL_PREFIX = "datascheme";

        @Override
        public URLStreamHandler createURLStreamHandler(String protocol) {
            if (DATASCHEME_PROTOCOL_PREFIX.equals(protocol)) {
                return new StreamHandlerImpl();
            }
            return null;
        }

        private static class StreamHandlerImpl extends URLStreamHandler {

            @Override
            protected URLConnection openConnection(URL u) throws IOException {
                if (!(Thread.currentThread() instanceof IConnectionProvider)) {
                    throw new IOException("Thread " + Thread.currentThread().getName() + " is not a db connection provider");
                }

                File file = RadixLoader.getInstance().getTempFile(u.toString());
                if (file != null) {
                    return new URLConnectionImpl(file, u);
                }

                final Connection dbConnection = ((IConnectionProvider) Thread.currentThread()).getConnection();

                if (dbConnection == null) {
                    throw new IOException("Db connection is null");
                }

                final String delimString = "://";
                final String urlString = u.toString();
                int delimIndex = urlString.indexOf(delimString);
                if (delimIndex < 0) {
                    throw new IOException("Ivalid url: " + u);
                }
                final String uri = urlString.substring(delimIndex + delimString.length());
                final byte[] data = readSchemeData(uri, dbConnection);
                if (data == null) {
                    throw new IOException("Unable to load " + u.toString());
                }
                return new URLConnectionImpl(RadixLoader.getInstance().getOrCreateTempFile(u.toString(), data), u);
            }

            private byte[] readSchemeData(final String uri, final Connection dbConnection) throws IOException {
                try {
                    try (PreparedStatement ps = dbConnection.prepareStatement("select scheme from rdx_sb_datascheme where uri = ?")) {
                        ps.setString(1, uri);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                final ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                IOUtils.copy(rs.getClob(1).getCharacterStream(), bos, "UTF-8");
                                return bos.toByteArray();
                            }
                        }
                    }
                } catch (SQLException ex) {
                    throw new IOException(ex);
                }
                return null;
            }

            private static class URLConnectionImpl extends URLConnection {

                private final File file;

                public URLConnectionImpl(File file, URL url) {
                    super(url);
                    this.file = file;
                }

                @Override
                public InputStream getInputStream() throws IOException {
                    return new FileInputStream(file);
                }

                @Override
                public void connect() throws IOException {
                }
            }
        }
    }
}
