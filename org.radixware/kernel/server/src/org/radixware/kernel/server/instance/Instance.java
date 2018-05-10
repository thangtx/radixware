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

import org.radixware.kernel.common.cache.ObjectCache;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.LogRecord;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.sql.DataSource;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.spi.RootLogger;
import org.apache.xmlbeans.XmlOptions;
import org.radixware.kernel.common.defs.RadixObjectInitializationPolicy;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.defs.utils.ISeverityByCodeCalculator;
import org.radixware.kernel.common.enums.EAadcMember;
import org.radixware.kernel.common.enums.EDatabaseType;
import org.radixware.kernel.common.enums.EDbSessionOwnerType;
import org.radixware.kernel.common.enums.EEventContextType;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EOptionMode;
import org.radixware.kernel.common.enums.EPriority;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.common.exceptions.KernelVersionError;
import org.radixware.kernel.common.exceptions.KeystoreControllerException;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.kerberos.KerberosCredentials;
import org.radixware.kernel.common.kerberos.KrbServiceOptions;
import org.radixware.kernel.common.repository.DbOptionValue;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.soap.RadixSoapMessage;
import org.radixware.kernel.common.ssl.KeystoreController;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.ValueFormatter;
import org.radixware.kernel.license.ILicenseEnvironment;
import org.radixware.kernel.license.ILicenseInfo;
import org.radixware.kernel.license.LicenseManager;
import org.radixware.kernel.server.SrvRunParams;
import org.radixware.kernel.server.SrvRunParams.ConfigFileNotSpecifiedException;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.arte.LoadErrorsLog;
import org.radixware.kernel.server.arte.Release;
import org.radixware.kernel.server.instance.aadc.AadcManager;
import org.radixware.kernel.server.instance.arte.ArteInstance;
import org.radixware.kernel.server.instance.arte.ArtesDbLogFlusher;
import org.radixware.kernel.server.instance.arte.ArtePool;
import org.radixware.kernel.server.instance.arte.IArteRequest;
import org.radixware.kernel.server.jdbc.DelegateDbQueries;
import org.radixware.kernel.server.jdbc.Stmt;
import org.radixware.kernel.server.jdbc.IDbQueries;
import org.radixware.kernel.server.jdbc.RadixConnection;
import org.radixware.kernel.server.jdbc.RadixDataSource;
import org.radixware.kernel.server.monitoring.InstanceMonitor;
import org.radixware.kernel.server.pipe.RemotePipeManager;
import org.radixware.kernel.server.trace.*;
import org.radixware.kernel.server.units.UnitDescription;
import org.radixware.kernel.server.units.job.executor.JobExecutorUnit;
import org.radixware.kernel.server.utils.IPriorityResourceManager;
import org.radixware.kernel.server.utils.NonblockingPriorityResourceManager;
import org.radixware.kernel.server.utils.SynchronizedPriorityResourceManager;
import org.radixware.kernel.starter.Starter;
import org.radixware.kernel.starter.log.DelegateLogFactory;
import org.radixware.kernel.starter.log.StarterLog;
import org.radixware.kernel.starter.meta.FileMeta;
import org.radixware.kernel.starter.meta.LayerMeta;
import org.radixware.kernel.starter.meta.RevisionMeta;
import org.radixware.kernel.starter.radixloader.ERevisionMetaType;
import org.radixware.kernel.starter.radixloader.IRepositoryEntry;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.kernel.starter.radixloader.RadixLoaderException;
import org.radixware.kernel.starter.radixloader.RadixURLTool;
import org.radixware.kernel.starter.radixloader.ReplacementEntry;
import org.radixware.kernel.starter.radixloader.ReplacementFile;
import org.radixware.kernel.starter.utils.SystemTools;

public class Instance implements EventHandler {

    private static final boolean XBEANS_SYNCRONIZATION_OFF = SystemPropUtils.getBooleanSystemProp("rdx.xbeans.sync.off", true);
    private static final boolean USE_CLASSIC_ARTE_ACTIVITY_MANAGER = SystemPropUtils.getBooleanSystemProp("rdx.use.classic.arte.activity.manager", false);
    private static final String INTERNAL_MAINTENANCE_REASON = "$$$internal_maintenance_request$$$";
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
    private static final int ACTUALIZE_PERIOD_MILLIS = ONE_SECOND_MILLIS;
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
    private static final int LICENSE_EXPIRATION_CHECK_PERIOD_MILLIS = ONE_MINUTE_MILLIS;
    //each 5 minutes
    private static final int DB_CONFIGURATION_REREAD_PERIOD_MILLIS = FIVE_MINUTES_MILLIS;
    private static final int CHECK_DB_TIME_PERIOD_MILLIS = FIVE_MINUTES_MILLIS;
    //other
    private static final int CHECK_UNIT_HANG_PERIOD_MILLIS = Unit.DB_I_AM_ALIVE_PERIOD_MILLIS / 2;
    private static final int SLEEP_ON_ARTE_POOL_LOAD_WAIT_MILLIS = ONE_SECOND_MILLIS;
    private static final int MAX_WAIT_FOR_ARTE_POOL_LOAD_MILLIS = THIRTY_SECONDS_MILLIS;
    //timeouts
    private static final int SHUTDOWN_TIMEOUT_SEC = ONE_MINUTE_MILLIS / 1000;
    //memory usage levels
    private static final int MEM_USAGE_EV_PERCENT = 80;
    private static final int MEM_USAGE_WARN_PERCENT = 90;
    private static final int MEM_USAGE_ERR_PERCENT = 95;
    private static final int MEM_USAGE_ALARM_PERCENT = 98;
    //
    private static final String TDUMPS_DIR_NAME = "tdumps";
    //
    private static volatile Instance INSTANCE_GLOBAL_POINTER = new Instance();
    //
    private static final int COUNT_OF_LOGGED_LOCAL_FILE_OVERRIDES = SystemPropUtils.getIntSystemProp("rdx.count.of.logged.local.file.overrides", 100);

    // SQL statements inside the module:    
    private static final String qryReadImplicitCacheSettingsStmtSQL = "select s.useOraImplStmtCache s_use, s.oraImplStmtCacheSize s_size, i.useOraImplStmtCache i_use, i.oraImplStmtCacheSize i_size from rdx_system s, rdx_instance i where s.id = 1 and i.id=?";
    private static final Stmt qryReadImplicitCacheSettingsStmt = new Stmt(qryReadImplicitCacheSettingsStmtSQL, Types.BIGINT);

    private static final String qryTryEnableLocalJobExecutorModeStmtSQL = "update rdx_instance set targetExecutorId=? where id=?";
    private static final Stmt qryTryEnableLocalJobExecutorModeStmt = new Stmt(qryTryEnableLocalJobExecutorModeStmtSQL, Types.BIGINT, Types.BIGINT);

    private static final String qrySetDbSessionInfoStmtSQL = "begin RDX_Environment.init(?,?,?); end;";
    private static final Stmt qrySetDbSessionInfoStmt = new Stmt(qrySetDbSessionInfoStmtSQL, Types.BIGINT, Types.VARCHAR, Types.BIGINT);

    private static final String qryCompareLocalAndDbTimeStmtSQL = "select to_char(systimestamp, 'yyyy-mm-dd hh24:mi:ss'), to_char( current_timestamp, 'yyyy-mm-dd hh24:mi:ss') from dual";
    private static final Stmt qryCompareLocalAndDbTimeStmt = new Stmt(qryCompareLocalAndDbTimeStmtSQL);

    private static final String qryIsRunningStmtSQL = "select 1 from rdx_instance where started = 1 and ((sysdate < selfchecktime + numtodsinterval(?, 'SECOND')) or (RDX_UTILS.getUnixEpochMillis() < selfCheckTimeMillis + ?)) and id = ?";
    private static final Stmt qryIsRunningStmt = new Stmt(qryIsRunningStmtSQL, Types.INTEGER, Types.INTEGER, Types.BIGINT);

    private static final String qrySelectSchemaStmtSQL = "select scheme from rdx_sb_datascheme where uri = ?";
    private static final Stmt qrySelectSchemaStmt = new Stmt(qrySelectSchemaStmtSQL, Types.VARCHAR);

    //
    private volatile String restartReason;
    private volatile int actualRestartDelaySec = -1;
    private volatile Long stopTimeoutOnRestartSec = null;
    private final Runnable restartRunnable = new Runnable() {
        @Override
        public void run() {
            if (actualRestartDelaySec >= 0) {
                try {
                    Thread.sleep(actualRestartDelaySec * 1000l);
                } catch (InterruptedException ex) {
                    //skip
                }
            }
            try {
                final String reason = "restart (" + restartReason + "), delay: " + actualRestartDelaySec + " sec." + (stopTimeoutOnRestartSec == null ? "" : ", timeout: " + stopTimeoutOnRestartSec + " sec.");
                stop(reason, stopTimeoutOnRestartSec == null ? Integer.MAX_VALUE : stopTimeoutOnRestartSec.intValue());
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
    };

    //
    private volatile String proxyOraUser = null;
    private volatile DataSource radixDataSource = null;
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
    private final InstanceDbQueries dbQueries;
    private volatile long localSensitiveTracingFinishMillis = 0;
    private volatile boolean isActualizeVerSchedulled = false;
    private long oldVersion = -1;
    private final List<RadixLoaderActualizer.DdsVersionWarning> reportedDdsWarnings = new LinkedList<>();
    private volatile InstanceView view = null;
    private volatile boolean bShuttingDown = false;
    private String host;
    private String name;
    private String pid;
    private volatile long pidNumber = -1;
    private volatile InstanceState state = InstanceState.STOPPED;
    private final InstanceServerTrace trace;
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
    private volatile IPriorityResourceManager activeArteResourceManager;
    private final ArrayBlockingQueue<UnitCommandResponse> unitCommandResponsesQueue = new ArrayBlockingQueue<>(1000);
    private final AtomicBoolean immediateMaintenanceRequested = new AtomicBoolean();
    private volatile SingletonUnitSessionLock singletonUnitLock;
    private volatile long latestVersion = -1;
    private String keyStorePath;
    private volatile InstanceJMXState instanceJmxState = new InstanceJMXState(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    private volatile List<ResourceRegistryItemJMXInfo> resourceRegistryItemsForJMX = new ArrayList<>();
    private ObjectName instanceJmxStateName;
    private final InstanceLoadHistory loadHistory = new InstanceLoadHistory(30);
    private final ResourceRegistry resourceRegistry;
    private volatile boolean ggStandbyMode = false;
    private final CountDownLatch ggStandbyModeOffLatch = new CountDownLatch(1);
    private final IDbQueries delegate = new DelegateDbQueries(this, null);
    private AadcManager aadcManager;
    private boolean kernelVersionChanged = false;
    private volatile VersionInfo curVersionInfo;
    private volatile VersionInfo repoVersionInfo;
    private volatile DdsVersionInfo ddsVersionInfo;
    private volatile long startTimeMillis;
    private volatile RevisionMeta lastAcceptableRevMeta;
    private boolean autoUpdateEnabled = false;
    private long killArteProcessingNonLatestVersionTime = 0;
    private volatile Arte upgraderArte = null;
    private volatile int artePoolLoadPercentNormal = 0;
    private volatile int artePoolLoadPercentAboveNormal = 0;
    private volatile int artePoolLoadPercentHigh = 0;
    private volatile int artePoolLoadPercentVeryHigh = 0;
    private volatile int artePoolLoadPercentCritical = 0;
    private Map<Long, Long> lastWrittenUnitCheckTimeMillis = new ConcurrentHashMap<>();
    private RemotePipeManager remotePipeManager;
    private volatile boolean hostInfoWritedToDatabase = false;
    private volatile AtomicReference<CountDownLatch> rereadVersionsLatchRef = new AtomicReference<>();
    private final Map<String, TraceProfileInfo> desc2TraceProfileInfo = new ConcurrentHashMap<>();

    public Instance() {
        //Host name
        try {
            final InetAddress localHost = InetAddress.getLocalHost();
            host = localHost.getCanonicalHostName() + " (" + localHost.getHostAddress() + ")";
        } catch (UnknownHostException e) {
            host = "localhost";
        }
        try {
            pidNumber = SystemTools.getCurrentProcessPid();
            pid = String.valueOf(pidNumber);
        } catch (Exception ex) {
            pid = "<unknown>";
        }
        trace = new InstanceServerTrace(this);
        listeners = new CopyOnWriteArrayList<>();
        dbQueries = new InstanceDbQueries(this);
        units = new UnitsPool(this);
        jobCheckTimeUpdater = new JobCheckTimeUpdater(this);
        releasePool = new ReleasePool(this);
        resourceRegistry = new ResourceRegistry(trace.newTracer(EEventSource.INSTANCE.getValue()));
    }

    public final void start(final RadixDataSource radixDataSource, final String proxyOraUser, final Connection dbConnection, final int instanceId, final String instanceName) throws SQLException {
        synchronized (controlSem) {
            if (getState() != InstanceState.STOPPED) {
                throw new InvalidInstanceState(Messages.INSTANCE_IS_NOT_STOPPED);
            }
            trace.beforeInstanceStart(instanceId);
            getView();// create view to see trace
            thread = new InstanceThread(this, radixDataSource, proxyOraUser, dbConnection, instanceId, instanceName);
            bShuttingDown = false;

            stopIterations = createStopIterations();
            thread.start();
            this.proxyOraUser = proxyOraUser;
        }
    }

    public void preloadNextVersion() {
        if (getAutoActualizeVer()) {
            throw new IllegalStateException("Can't start using new app version when autoupdate is enabled");
        }
        RadixLoaderActualizer.getInstance().preloadNextAsync();
    }

    public void stopUsingCurAppVersion(final Long hardStopDelaySec) {
        if (getAutoActualizeVer()) {
            throw new IllegalStateException("Can't start using new app version when autoupdate is enabled");
        }
        artePool.setAcceptableVersions(-1, -1);
        if (hardStopDelaySec != null) {
            killArteProcessingNonLatestVersionTime = System.currentTimeMillis() + hardStopDelaySec * 1000;
        }
    }

    public void startUsingNextAppVersion() {
        if (getAutoActualizeVer()) {
            throw new IllegalStateException("Can't start using new app version when autoupdate is enabled");
        }
        if (artePool == null) {
            return;
        }
        artePool.setAcceptableVersions(latestVersion, latestVersion);
        lastAcceptableRevMeta = RadixLoader.getInstance().getCurrentRevisionMeta();
        killArteProcessingNonLatestVersionTime = 0;
        rereadVersionsInfo();
    }

    public VersionInfo getCurVersionInfo() {
        return curVersionInfo;
    }

    public VersionInfo rereadAndGetRepoVersionInfo(int rereadTimeoutMillis) throws TimeoutException {
        CountDownLatch requestLatch = rereadVersionsLatchRef.get();
        while (requestLatch == null) {
            requestLatch = new CountDownLatch(1);
            if (!rereadVersionsLatchRef.compareAndSet(null, requestLatch)) {
                requestLatch = rereadVersionsLatchRef.get();
            }
        }
        try {
            if (!requestLatch.await(rereadTimeoutMillis, TimeUnit.MILLISECONDS)) {
                throw new TimeoutException();
            }
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        return repoVersionInfo;
    }

    public VersionInfo getRepoVersionInfo() {
        return repoVersionInfo;
    }

    public DdsVersionInfo getDdsVersionInfo() {
        return ddsVersionInfo;
    }

    public long getLastMaxAcceptableRevision() {
        final RevisionMeta lastAccMetaSnapshot = lastAcceptableRevMeta;
        if (lastAccMetaSnapshot == null) {
            return -1;
        }
        return lastAccMetaSnapshot.getNum();
    }

    private void rereadVersionsInfo() {
        final VersionInfo prevCurVerInfo = curVersionInfo;
        readVersionsInfo();
        if (!Objects.equals(prevCurVerInfo, curVersionInfo)) {
            try {
                getDbQueries().writeVersionInfo(curVersionInfo);
            } catch (Exception ex) {
                getTrace().put(EEventSeverity.ERROR, "Unable to write version info to database: " + ExceptionTextFormatter.throwableToString(ex), EEventSource.INSTANCE);
            }
        }

    }

    private void readVersionsInfo() {
        try {
            final long curRevision = lastAcceptableRevMeta.getNum();
            curVersionInfo = new VersionInfo(lastAcceptableRevMeta.getKernelLayerVersionsString(), lastAcceptableRevMeta.getAppLayerVersionsString(), curRevision);
            ddsVersionInfo = getDbQueries().readDdsVersionInfo();

            final long latestRevision = RadixLoader.getInstance().getLatestRevision();

            if (repoVersionInfo != null && repoVersionInfo.getRevision() == latestRevision) {
                return;
            }

            if (latestRevision == curRevision) {
                repoVersionInfo = curVersionInfo;
                return;
            }

            final RevisionMeta latestRevMeta = RadixLoader.getInstance().getRevisionMeta(latestRevision, ERevisionMetaType.LAYERS_ONLY);
            repoVersionInfo = new VersionInfo(latestRevMeta.getKernelLayerVersionsString(), latestRevMeta.getAppLayerVersionsString(), latestRevMeta.getNum());
        } catch (Throwable t) {
            getTrace().put(EEventSeverity.ERROR, "Unable to read versions info: " + ExceptionTextFormatter.throwableToString(t), EEventSource.INSTANCE);
        }
    }

    public long getPidNumber() {
        return pidNumber;
    }

    public CountDownLatch getGgStandbyModeOffLatch() {
        return ggStandbyModeOffLatch;
    }

    public boolean isGgStandbyMode() {
        return ggStandbyMode;
    }

    public ResourceRegistry getResourceRegistry() {
        return resourceRegistry;
    }

    public InstanceLoadHistory getLoadHistory() {
        return loadHistory;
    }

    public AadcManager getAadcManager() {
        return aadcManager;
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

    public void setUpgraderArte(Arte upgraderArte) {
        this.upgraderArte = upgraderArte;
    }

    public boolean isShuttingDown() {
        if (bShuttingDown) {
            return true;
        }
        final InstanceView v = getView();
        return v != null && v.disposing;
    }

    public void stop(final String reason) {
        stop(reason, SHUTDOWN_TIMEOUT_SEC);
    }

    public void stop(final String reason, int timeoutSec) {
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
            if (!softStop(timeoutSec)) {
                hardStop();
            }
        }
    }

    private void onStartShuttingDown() {
        bShuttingDown = true;
    }

    private boolean softStop(final int timeoutSec) {

        if (dispatcher != null) {
            dispatcher.wakeup();
        }
        try {
            thread.join(timeoutSec * 1000l);
        } catch (Throwable e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            trace.put(EEventSeverity.ERROR, Messages.ERR_ON_INSTANCE_SHUTDOWN + exStack, Messages.MLS_ID_ERR_ON_INSTANCE_SHUTDOWN, new ArrStr(getFullTitle(), exStack), EEventSource.INSTANCE, false);
        }

        if (thread == null || !thread.isAlive()) {
            return true;
        }

        return false;
    }

    private void logErrorOnStop(final Throwable ex) {
        trace.put(EEventSeverity.WARNING, "Error on stop: " + ex, EEventSource.INSTANCE);
    }

    private void hardStop() {
        trace.put(EEventSeverity.WARNING, Messages.TRY_INTERRUPT_INSTANCE, Messages.MLS_ID_TRY_INTERRUPT_INSTANCE, new ArrStr(getFullTitle()), EEventSource.INSTANCE, false);
        final ArtePool pool = getArtePool();
        if (pool != null) {
            for (ArteInstance arteInstance : pool.getInstances(false)) {
                try {
                    arteInstance.closeDbConnectionForcibly("hard stop");
                } catch (Exception ex) {
                    logErrorOnStop(ex);
                }
            }
        }
        getResourceRegistry().closeAllForcibly();
        try {
            thread.join(2000);
        } catch (InterruptedException ex) {

        }
        thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException ex) {
            //return;
        }
    }

    public String getKeyStorePath() {
        return keyStorePath;
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

    public long getStartTimeMillis() {
        return startTimeMillis;
    }

    public void setLastWrittenUnitCheckTimeMillis(final long unitId, final long checkTimeMillis) {
        lastWrittenUnitCheckTimeMillis.put(unitId, checkTimeMillis);
    }

    public Long getLastWrittenUnitCheckTimeMillis(final long unitId) {
        return lastWrittenUnitCheckTimeMillis.get(unitId);
    }

//	 overridable implementation of instance thread run():  try {startImpl(); runImpl();}finally{stopImpl()} 	
    /**
     * Called only from InstanceThread
     *
     * @param radixDataSource
     * @param proxyOraUser
     * @param dbConnection
     * @param instanceId
     * @param instanceName
     * @throws Exception
     */
    protected void startImpl(final RadixDataSource radixDataSource, final String proxyOraUser, final Connection dbConnection, final int instanceId, final String instanceName) throws Exception {
        id = instanceId;

        startTimeMillis = System.currentTimeMillis();

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
        setDbConnection(radixDataSource, proxyOraUser, dbConnection);

        try {
            readImplicitCacheSettings();
        } catch (SQLException ex) {
            getTrace().put(EEventSeverity.ERROR, "Unable to read cache settings: " + ExceptionTextFormatter.throwableToString(ex), EEventSource.INSTANCE);
        }

        ggStandbyMode = dbQueries.readGgStandbyMode();
        if (ggStandbyMode) {
            getTrace().put(EEventSeverity.WARNING, "System is in a standby mode, units start will be postponed", null, null, EEventSource.INSTANCE, false);
        } else {
            ggStandbyModeOffLatch.countDown();
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

        logLocalFileReplacements(true);

        trace.put(EEventSeverity.EVENT, Messages.TRACE_PROFILE_CHANGED + String.valueOf(curTraceOptions.getProfiles()), Messages.MLS_ID_TRACE_PROFILE_CHANGED, new ArrStr(getFullTitle(), curTraceOptions.getProfiles().toString()), EEventSource.INSTANCE, false);
        trace.put(EEventSeverity.EVENT, Messages.FILE_TRACE_OPTIONS_CHANGED + String.valueOf(curTraceOptions.getLogOptions()), Messages.MLS_ID_FILE_TRACE_OPTIONS_CHANGED, new ArrStr(getFullTitle(), curTraceOptions.getLogOptions().toString()), EEventSource.INSTANCE, false);

        updateLocalSensitiveTracingFinishMillis();

        initKeystore();

        initLicenseClient();

        options = getDbQueries().readOptions();
        final String optionsStr = options.toString();
        getTrace().put(EEventSeverity.EVENT, Messages.START_OPTIONS + optionsStr, Messages.MLS_ID_START_OPTIONS, new ArrStr(getFullTitle(), optionsStr), EEventSource.INSTANCE, false);

        autoUpdateEnabled = SrvRunParams.getIsDevelopmentMode() && options.getAutoActualizeVer();

        actualizeLoader(true);
        getDbQueries().writeVersionInfo(curVersionInfo);
        writeHostInfo();

        checkLocalStarterVersion();

        rereadDbConfiguration();

        RadixURLTool.setAppURLStreamHandlerFactory(new ServerURLStreamHandlerFactory());

        threadDumpWriter = new ThreadDumpWriter(getTDumpsDir(curTraceOptions.getLogOptions()), curTraceOptions.getLogOptions().getName(), getTrace().newTracer(EEventSource.INSTANCE.getValue()));
        threadDumpWriter.start();

        arteStateWriter = new ArteStateWriter(this);
        arteStateWriter.start();

        jobCheckTimeUpdater.start();

//        remotePipeManager = new RemotePipeManager(this);
//        remotePipeManager.start();//TODO: shutdown
        aadcManager = new AadcManager(this);
        if (aadcManager.isInAadc()) {
            aadcManager.rereadOptions();
        }

        singletonUnitLock = new SingletonUnitSessionLock(this);

        RadixObjectInitializationPolicy.set(new RadixObjectInitializationPolicy(true));
        AdsUserFuncDef.Lookup.setSplitLookupsByThread(true);

        if (options.isUseActiveArteLimits()) {
            activeArteResourceManager = createArteActivityResourceManager();
            activeArteResourceManager.setOptions(options.getActiveArteLimitsOptions());
        } else {
            activeArteResourceManager = null;
        }

        lastArteAutoShutdownTimeMillisHolder.set(0);
        artePool = new ArtePool(this);
        artePool.onInstanceOptionsChanged();

        if (autoUpdateEnabled) {
            artePool.setAcceptableVersions(0, Long.MAX_VALUE);
        } else {
            artePool.setAcceptableVersions(latestVersion, latestVersion);
        }

        getTrace().put(EEventSeverity.EVENT, Messages.LOADING_ARTE_POOL, Messages.MLS_ID_LOADING_ARTE_POOL, null, EEventSource.INSTANCE.getValue(), false);

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

        killArteProcessingNonLatestVersionTime = 0;

        artePoolLoadPercentNormal = 0;
        artePoolLoadPercentAboveNormal = 0;
        artePoolLoadPercentHigh = 0;
        artePoolLoadPercentVeryHigh = 0;
        artePoolLoadPercentCritical = 0;

        units.setUnitsNotStartedInDb();

        units.loadAll();

        tryEnableLocalJobExecutorMode();//should be called between units.loadAll() and units.startAll()

        if (!ggStandbyMode) {
            units.startLoaded(Messages.INSTANCE_START);
        }

        unitCommandResponsesQueue.clear();

        immediateMaintenanceRequested.set(false);

        dispatcher = new EventDispatcher();

        service = new InstanceRcSap(this, dispatcher);

        if (!ggStandbyMode) {
            service.start(getDbConnection());
        }

        dispatcher.waitEvent(new TimerEvent(), this, System.currentTimeMillis() + TIC_MILLIS);

        compareLocalAndDbTime();
        lastCheckDbTimeMillis = System.currentTimeMillis();

        checkPidDiscoveryEnabled();

        if (instanceJmxStateName == null) {
            instanceJmxStateName = new ObjectName("org.radixware:00=SystemInstance,01=" + getId() + ",name=InstanceState");
        }

        final MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        if (mbs != null) {
            if (mbs.isRegistered(instanceJmxStateName)) {
                mbs.unregisterMBean(instanceJmxStateName);
            }
            mbs.registerMBean(new InstanceMXBean() {

                @Override
                public InstanceJMXState getInstanceState() {
                    return instanceJmxState;
                }

                @Override
                public ResourceRegistryJMXState getResourceRegistry() {
                    return new ResourceRegistryJMXState(resourceRegistryItemsForJMX.toArray(new ResourceRegistryItemJMXInfo[]{}));
                }

                @Override
                public String findResourcesByKeyRegex(String keyRegex) {
                    return "selected " + ResourceRegistry.resourcesAsStr(resourceRegistry.findItemsByKeyRegex(keyRegex));
                }

                @Override
                public String closeResourceRegistryItemsByKeyPrefix(String keyPattern, final boolean doClose) {
                    return "closed " + (doClose ? "" : " (dry run) ") + ResourceRegistry.resourcesAsStr(resourceRegistry.closeByKey(keyPattern, "JMX request", doClose));
                }

            }, instanceJmxStateName);
        }

        loadHistory.clear();

        stopOsCommand = getDbQueries().getStopOsCommand(); //loading stopOsCommand it will be used if during stop there is no db connection

        final String startOsCommand = getDbQueries().getStartOsCommand();
        if (startOsCommand != null && !startOsCommand.isEmpty()) {
            execOsCommand(startOsCommand);
        }

        if (XBEANS_SYNCRONIZATION_OFF) {
            hackAndTurnOffXbeansSyncronization();
        }

        RadixSoapMessage.setDefaultThisInstanceId((long) getId());

        rereadVersionsLatchRef.set(null);

        requestMaintenance(INTERNAL_MAINTENANCE_REASON);
    }

    private IPriorityResourceManager createArteActivityResourceManager() {
        if (USE_CLASSIC_ARTE_ACTIVITY_MANAGER) {
            return new SynchronizedPriorityResourceManager();
        } else {
            return new NonblockingPriorityResourceManager();
        }
    }

    private void logLocalFileReplacements(boolean notifyAboutUnversioned) {
        try {
            final long curRevNum = RadixLoader.getInstance().getCurrentRevision();
            final List<ReplacementFile> usedReplacements = RadixLoader.getInstance().getLocalFiles().getUsedReplacements(curRevNum);
            if (!usedReplacements.isEmpty()) {
                EEventSeverity entryLogSeverity = EEventSeverity.EVENT;
                int replacementsCnt = 0;
                for (ReplacementFile repFile : usedReplacements) {
                    replacementsCnt += repFile.getSize();
                }
                if (replacementsCnt > COUNT_OF_LOGGED_LOCAL_FILE_OVERRIDES) {
                    trace.put(EEventSeverity.EVENT, "More than " + COUNT_OF_LOGGED_LOCAL_FILE_OVERRIDES + " files are locally overridden, full list will be logged with DEBUG level", EEventSource.INSTANCE);
                    entryLogSeverity = EEventSeverity.DEBUG;
                }
                for (ReplacementFile repFile : usedReplacements) {
                    for (ReplacementEntry entry : repFile.getEntries()) {
                        boolean exists = false;
                        try {
                            exists = new File(entry.getLocal()).exists();
                        } catch (Exception ex) {
                            //ignore
                        }
                        if (exists) {
                            trace.put(entryLogSeverity, "Replaced '" + entry.getRemote() + "' with '" + repFile.getHumanReadableLocal(entry.getRemote()) + "'", EEventSource.INSTANCE);
                        } else {
                            trace.put(EEventSeverity.WARNING, "Replacement of '" + entry.getRemote() + "' with '" + repFile.getHumanReadableLocal(entry.getRemote()) + "' is invalid, local file does not exists", EEventSource.INSTANCE);
                        }
                    }
                }
            }

            final List<RadixLoader.LocalFiles.ConflictInfo> conflicts = RadixLoader.getInstance().getLocalFiles().getReplacementConflicts();
            for (RadixLoader.LocalFiles.ConflictInfo conflict : conflicts) {
                trace.put(EEventSeverity.WARNING, "Replacement conflict between files: '" + conflict.getRepFile().getFilePath() + "' and '" + conflict.getOtherFile().getFilePath() + "' both contains following entries:\n" + conflict.getConflictEntries(), EEventSource.INSTANCE);
            }

            final List<ReplacementFile> uncompatibleReplacements = RadixLoader.getInstance().getLocalFiles().getUncompatibleReplacements();
            for (ReplacementFile repFile : uncompatibleReplacements) {
                trace.put(EEventSeverity.WARNING, "Replacement file '" + repFile.getFilePath() + "' (version: " + repFile.getVersions() + ") not compatible with current version: " + RadixLoader.getInstance().getCurrentRevisionMeta().getAppLayerVersionsString(), EEventSource.INSTANCE);
            }

            if (notifyAboutUnversioned) {
                for (ReplacementFile repFile : RadixLoader.getInstance().getLocalFiles().getAllReplacementsEx()) {
                    if (repFile.isUnversioned()) {
                        if (repFile.fromLocalFileList()) {
                            trace.put(EEventSeverity.WARNING, "Local file list API is deprecated. Consider use -localReplacementDir starter parameter. Replacement file '" + repFile.getFilePath(), EEventSource.INSTANCE);
                        } else {
                            trace.put(EEventSeverity.WARNING, "Replacement file '" + repFile.getFilePath() + "' does not contains compatibleVersions information", EEventSource.INSTANCE);
                        }
                    }
                }
            }
        } catch (RadixLoaderException ex) {
            trace.put(EEventSeverity.ERROR, "Unable to load replacements info: \n" + ExceptionTextFormatter.exceptionStackToString(ex), EEventSource.INSTANCE);
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
        try (final PreparedStatement ps = ((RadixConnection) getDbConnection()).prepareStatement(qryReadImplicitCacheSettingsStmt)) {
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

    public IPriorityResourceManager getActiveArteResourceManager() {
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
        keyStorePath = expand(dbQueries.getKeystorePath());
        if (keyStorePath == null) {
            return;
        }
        if (!SrvRunParams.getIsDevelopmentMode()) {
            try {
                if (!(new File(keyStorePath).exists())) {
                    throw new IOException("File '" + keyStorePath + "' not found");
                }
            } catch (Exception ex) {
                trace.put(EEventSeverity.ERROR, Messages.ERR_WHILE_CHECKING_KEYSTORE + " " + ex.getMessage(), null, null, EEventSource.INSTANCE, false);
            }
        }
        KeystoreController.setServerKeystorePath(keyStorePath);
        if (keyStorePath != null) {
            keyStorePath = KeystoreController.getServerKeystoreAbsolutePath(keyStorePath);
        }
        try {
            //[RADIX-4380] check server's keystore availability
            KeystoreController.checkServerKeystoreAvailability(dbQueries.getKeystoreType(), keyStorePath);
            trace.put(EEventSeverity.EVENT, String.format(Messages.KEYSTORE_LOADER_FROM, keyStorePath), Messages.MLS_ID_KEYSTORE_LOADED_FROM, new ArrStr(keyStorePath), EEventSource.INSTANCE, false);
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
                return null;
            }

            @Override
            public String getLicenseServerAddress() {
                return null;
            }

            @Override
            public long getLatestVersion() {
                return latestVersion;
            }
        });
    }

    public String getLicenseSetAsStr() {
        return "<license set is no longer available at run time>";
    }

    public List<ILicenseInfo> getLicenseSet() {
        return Collections.emptyList();
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
            try (final PreparedStatement st = ((RadixConnection) getDbConnection()).prepareStatement(qryTryEnableLocalJobExecutorModeStmt)) {
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
                            ((RadixDataSource) radixDataSource).setPassword(password);
                        }
                    });
                } else if (SrvRunParams.USER.equals(readResult.getOptionName())) {
                    ((RadixDataSource) radixDataSource).setUser(SrvRunParams.getUser());
                } else if (SrvRunParams.EXTERNAL_AUTH.equals(readResult.getOptionName())) {
                    ((RadixDataSource) radixDataSource).setUser("");
                    ((RadixDataSource) radixDataSource).setPassword("");
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
            newDbConfiguration = new DbConfiguration(EDatabaseType.ORACLE, Layer.TargetDatabase.MIN_ORACLE_VERSION, Collections.<DbOptionValue>singletonList(new DbOptionValue(Layer.TargetDatabase.PARTITIONING_OPTION, EOptionMode.ENABLED)));
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
        while (true) {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
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
        final List<Callable> iterations = new ArrayList<>();
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
                if (singletonUnitLock != null) {
                    try {
                        singletonUnitLock.releaseAllLocks();
                    } finally {
                        singletonUnitLock = null;
                    }
                }
                return null;
            }
        });

        iterations.add(new Callable() {
            @Override
            public Object call() {
                if (threadDumpWriter != null) {
                    threadDumpWriter.requestStop();
                    try {
                        threadDumpWriter.join(1000);
                    } catch (InterruptedException ex) {
                        //ignore
                    } finally {
                        threadDumpWriter = null;
                    }
                }
                return null;
            }
        });

        iterations.add(new Callable() {
            @Override
            public Object call() {
                if (arteStateWriter != null) {
                    arteStateWriter.requestStop();
                    try {
                        arteStateWriter.join(1000);
                    } catch (InterruptedException ex) {
                        //ignore
                    } finally {
                        arteStateWriter = null;
                    }
                }
                return null;
            }
        });

        iterations.add(new Callable() {
            @Override
            public Object call() throws IOException, SQLException {
                if (getAadcManager().isInAadc()) {
                    tryMoveJobsToOtherAadcMember();
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
            public Object call() throws InterruptedException, SQLException {
                resourceRegistry.closeAllForcibly();
                return null;
            }
        });
        iterations.add(new Callable() {
            @Override
            public Object call() throws InterruptedException, SQLException {
                if (aadcManager != null) {
                    aadcManager.stop();
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

    private void tryMoveJobsToOtherAadcMember() {
        try {
            final String thisMemberInstances = "select i.id from rdx_instance i where i.aadcMemberId=? order by id asc";
            Connection connection = getDbConnection();
            boolean temporaryConnection = false;
            if (connection == null || connection.isClosed()) {
                connection = openNewInstanceDbConnection();
                temporaryConnection = true;
            }
            try {
                try (final PreparedStatement ps = connection.prepareStatement(thisMemberInstances)) {
                    ps.setInt(1, getAadcManager().getMemberId());
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            if (rs.getLong("id") == getId()) {
                                continue;
                            }
                            final String lockInstance = "select i.id, i.started, i.selfCheckTimeMillis from rdx_instance i where i.id=? for update  wait 10";
                            try (PreparedStatement lockPs = connection.prepareStatement(lockInstance)) {
                                lockPs.setLong(1, rs.getLong("id"));
                                try (final ResultSet lockRs = lockPs.executeQuery()) {
                                    if (lockRs.next()) {
                                        if (lockRs.getBoolean("started") && Instance.isActive(new Timestamp(lockRs.getLong("selfCheckTimeMillis")))) {
                                            connection.commit();
                                            return;
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
                final String qryMoveUnpinnedJobsToOtherAadcMemberSQL = "update rdx_js_jobqueue j set j.aadcMemberId=? where (select t.aadcMemberId from rdx_js_task t where t.id = j.taskId) is null and j.aadcMemberId = ?";

                final PreparedStatement qryMoveUnpinnedJobsToOtherAadcMember = connection.prepareStatement(qryMoveUnpinnedJobsToOtherAadcMemberSQL);

                int otherAadcMemberId = getAadcManager().getMemberId() == 1 ? 2 : 1;
                qryMoveUnpinnedJobsToOtherAadcMember.setInt(1, otherAadcMemberId);
                qryMoveUnpinnedJobsToOtherAadcMember.setInt(2, getAadcManager().getMemberId());
                int count = qryMoveUnpinnedJobsToOtherAadcMember.executeUpdate();

                connection.commit();
                getTrace().put(EEventSeverity.EVENT, String.format(Messages.JOBS_MOVED_TO_OTHER_AADC_MEMBER, count, otherAadcMemberId, getAadcManager().getMemberId()), EEventSource.INSTANCE);
            } catch (Exception ex) {
                try {
                    connection.rollback();
                } catch (Exception ex1) {
                    //do nothing
                }
                throw ex;
            } finally {
                if (temporaryConnection) {
                    connection.close();
                }
            }
        } catch (Exception ex) {
            getTrace().put(EEventSeverity.ERROR, "Unable to move unpinned jobs to other AADC member during stop of the last instance on member " + getAadcManager().getMemberId() + " : " + ExceptionTextFormatter.throwableToString(ex), EEventSource.INSTANCE);
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

        if (state == InstanceState.STARTED || state == InstanceState.STARTING) {
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
        trace.put(EEventSeverity.EVENT, Messages.getStateMessage(newState) + (title.length() != 0 ? " \"" + title + "\"" : "") + ", host: " + host + ", PID: " + pid + (SrvRunParams.getIsDevelopmentMode() ? ", development mode ON" + (SrvRunParams.getIsHotSwapMode() ? ", hot swap mode ON" : "") : ""), Messages.getStateMessageMslId(newState), new ArrStr(title, host + ", PID: " + pid + (SrvRunParams.getIsDevelopmentMode() ? ", development mode ON" + (SrvRunParams.getIsHotSwapMode() ? ", hot swap mode ON" : "") : "")), EEventSource.INSTANCE, false);
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
    final void setDbConnection(final RadixDataSource radixDataSource, final String proxyOraUser, final Connection dbConnection) {
        this.radixDataSource = radixDataSource;
        this.proxyOraUser = proxyOraUser;
        trace.setDbConnection(dbConnection);
        dbQueries.closeAll();
        if (singletonUnitLock != null) {
            singletonUnitLock.closeAllDbQueries();
        }
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
        return openNewDbConnection((long) id, ownerType, ownerId, (RadixDataSource) radixDataSource, proxyOraUser, trace, isOraImplicitCacheEnabled(), getOraImplicitCacheSize());
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

    public static final RadixConnection openNewDbConnection(
            final Long instanceId,
            final String ownerType,
            final Long ownerId,
            final RadixDataSource oraDataSrc,
            final String proxyOraUser,
            final ServerTrace trace,
            final boolean useOraImplStatementsCache,
            final int oraImplStatementsCacheSize) throws SQLException {
        final RadixConnection c = (RadixConnection) oraDataSrc.getConnection();
        if (proxyOraUser != null) {
            try (PreparedStatement st = c.prepareStatement("alter session set current_schema = \"" + proxyOraUser + "\"")) {//bind variables are not supported in DDL
                st.execute();
            }
        }
        c.setAutoCommit(false);
        if (useOraImplStatementsCache) {
            c.setImplicitCachingEnabled(true);
            c.setStatementCacheSize(oraImplStatementsCacheSize);
        }
        setDbSessionInfo(instanceId, ownerType, ownerId, c, trace);
        return c;

    }

    public static final Connection openNewDbConnection(
            final Long instanceId,
            final String ownerType,
            final Long ownerId,
            final RadixDataSource oraDataSrc,
            final String proxyOraUser,
            final ServerTrace trace) throws SQLException {
        return openNewDbConnection(instanceId, ownerType, ownerId, oraDataSrc, proxyOraUser, trace, false, 0);
    }

    private static void setDbSessionInfo(final Long instanceId,
            final String sessionOwnerType,
            final Long sessionOwnerId,
            final Connection connection,
            final ServerTrace trace) {
        try (final PreparedStatement st = ((RadixConnection) connection).prepareStatement(qrySetDbSessionInfoStmt)) {
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

    final public InstanceDbQueries getDbQueries() {
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
        if (reason != INTERNAL_MAINTENANCE_REASON) {
            if (reason == null) {
                reason = "null";
            }
            trace.put(EEventSeverity.EVENT, String.format(Messages.INSTANCE_MAINTENANCE_REQUESTED, reason), Messages.MLS_ID_INSTANCE_MAINTENANCE_REQUESTED, new ArrStr(reason), EEventSource.INSTANCE, false);
        }
    }

    protected void maintenance() throws InterruptedException {
        final long curMillis = System.currentTimeMillis();
        try {

            if (!ggStandbyMode) {
                final List<UnitCommandResponse> unitCommandResponses = new ArrayList<>();
                unitCommandResponsesQueue.drainTo(unitCommandResponses);
                service.respondToUnitCommands(unitCommandResponses);
            }

            if (ggStandbyMode) {
                ggStandbyMode = dbQueries.readGgStandbyMode();
                if (!ggStandbyMode) {
                    getTrace().put(EEventSeverity.EVENT, "Standby mode has been switched off, starting units", null, null, EEventSource.INSTANCE, false);
                    ggStandbyModeOffLatch.countDown();
                    units.startLoaded("Standby mode has been switched off");
                }
            }

            aadcManager.maintenance();

            artePool.maintenance();

            if (killArteProcessingNonLatestVersionTime != 0 && killArteProcessingNonLatestVersionTime <= System.currentTimeMillis()) {
                for (ArteInstance inst : artePool.getInstances(false)) {
                    if (inst.getArte() != upgraderArte) {
                        final IArteRequest requestSnapshot = inst.getRequest();
                        if (requestSnapshot != null && requestSnapshot.getVersion() < getLatestVersion()) {
                            try {
                                ((RadixConnection) inst.getDbConnection()).forciblyClose("timeout for finishing current work during upgrade expired");
                                getTrace().put(EEventSeverity.ERROR, "'" + inst.getThread().getName() + "' database connection has been closed to terminate long running work during version switch (timeout expired)", EEventSource.INSTANCE);
                            } catch (Exception ex) {
                                getTrace().put(EEventSeverity.ERROR, "Unable to close '" + inst.getThread().getName() + "' database connection to terminate long running work during version switch (timeout expired): " + ExceptionTextFormatter.throwableToString(ex), EEventSource.INSTANCE);
                            }
                        }
                    }
                }
                killArteProcessingNonLatestVersionTime = 0;
            }

            if (curMillis - lastDbIAmAliveMillis >= Instance.DB_I_AM_ALIVE_PERIOD_MILLIS) {
                dbQueries.dbIAmStillAlive();
                aadcManager.instanceIsAlive(getId(), curMillis);
                if (!ggStandbyMode) {
                    service.dbSapSelfCheck();
                }
                lastDbIAmAliveMillis = curMillis;
            }

            if (curMillis - lastWriteArteStateMillis >= Instance.WRITE_ARTE_STATE_PERIOD_MILLIS) {
                getArteStateWriter().requestUpdate();
                lastWriteArteStateMillis = curMillis;
            }

            if (curMillis - lastOptionsRereadMillis >= Instance.OPTIONS_REREAD_PERIOD_MILLIS) {
                rereadTraceOptions();
                if (!ggStandbyMode) {
                    service.rereadOptions();
                }
                final InstanceOptions newOptions = getDbQueries().readOptions();
                if (!options.equals(newOptions)) {
                    final String newOptionsStr = newOptions.toString();
                    getTrace().put(EEventSeverity.EVENT, Messages.OPTIONS_CHANGED + newOptionsStr, Messages.MLS_ID_OPTIONS_CHANGED, new ArrStr(getFullTitle(), newOptionsStr), EEventSource.INSTANCE, false);
                    if (newOptions.isUseActiveArteLimits()) {
                        if (activeArteResourceManager == null) {
                            final IPriorityResourceManager manager = createArteActivityResourceManager();
                            manager.setOptions(newOptions.getActiveArteLimitsOptions());
                            activeArteResourceManager = manager;
                        } else {
                            activeArteResourceManager.setOptions(newOptions.getActiveArteLimitsOptions());
                        }
                    }
                    options = newOptions;
                    artePool.onInstanceOptionsChanged();
                }
                lastOptionsRereadMillis = curMillis;
                checkLocalStarterVersion();//to persistently cry about wrong starter version
                resourceRegistry.maintenance();
                if (aadcManager.isInAadc()) {
                    aadcManager.rereadOptions();
                }
            }

            final CountDownLatch rereadVersionsRequestLatch = rereadVersionsLatchRef.getAndSet(null);
            if (rereadVersionsRequestLatch != null) {
                rereadVersionsInfo();
                rereadVersionsRequestLatch.countDown();
            }

            if (isActualizeVerSchedulled || curMillis - lastActualizeMillis >= Instance.ACTUALIZE_PERIOD_MILLIS) {
                boolean tryUpdateVersion = false;
                if (isActualizeVerSchedulled || autoUpdateEnabled) {
                    if (isActualizeVerSchedulled) {
                        trace.put(EEventSeverity.EVENT, Messages.CHECK_FOR_UPDATES, Messages.MLS_ID_CHECK_FOR_UPDATES, new ArrStr(getFullTitle()), EEventSource.INSTANCE, false);
                    }
                    tryUpdateVersion = true;
                }
                actualizeLoader(tryUpdateVersion);
                lastActualizeMillis = curMillis;
            }

            units.maintenance();

            if (curMillis - lastProfileFlushMillis >= options.getProfilePeriodMillis()) {
                getProfiler().flush();
                lastProfileFlushMillis = curMillis;
            }

            if (curMillis - lastServerKeystoreCheckMillis >= Instance.KEYSTORE_REREAD_PERIOD_MILLIS) {
                if (keyStorePath != null) {
                    if (KeystoreController.checkServerKeystoreModified()) {
                        trace.put(EEventSeverity.EVENT, Messages.SERVER_KEYSTORE_WAS_REREAD, null, null, EEventSource.INSTANCE, false);
                    }
                    lastServerKeystoreCheckMillis = curMillis;
                }
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
                    updateLoadHistory();
                    lastBasicStatsWriteMillis = curMillis;
                }

                updateLoadStats();
            }

            updateJmxState();

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

    private void updateLoadStats() {

        final double curActiveArteCount = monitor.getSlidingAvgActiveArteCount();

        artePoolLoadPercentCritical = normalizeLoadPercent(curActiveArteCount
                / (options.getNormalArteInstCount()
                + options.getAboveNormalArteInstCount()
                + options.getHighArteInstCount()
                + options.getVeryHighArteInstCount()
                + options.getCriticalArteInstCount()) * 100 + 0.5);

        artePoolLoadPercentVeryHigh = normalizeLoadPercent(curActiveArteCount
                / (options.getNormalArteInstCount()
                + options.getAboveNormalArteInstCount()
                + options.getHighArteInstCount()
                + options.getVeryHighArteInstCount()) * 100 + 0.5);

        artePoolLoadPercentHigh = normalizeLoadPercent(curActiveArteCount
                / (options.getNormalArteInstCount()
                + options.getAboveNormalArteInstCount()
                + options.getHighArteInstCount()) * 100 + 0.5);

        artePoolLoadPercentAboveNormal = normalizeLoadPercent(curActiveArteCount
                / (options.getNormalArteInstCount()
                + options.getAboveNormalArteInstCount()) * 100 + 0.5);

        artePoolLoadPercentNormal = normalizeLoadPercent(curActiveArteCount
                / (options.getNormalArteInstCount()) * 100 + 0.5);

    }

    private int normalizeLoadPercent(final double percent) {
        if (percent < 0) {
            return 0;
        }
        if (percent > 100) {
            return 100;
        }
        return (int) percent;
    }

    private void updateLoadHistory() {
        if (monitor != null) {
            loadHistory.store(monitor.getLastCpuUsage10SecPercent(), monitor.getLastHostCpuUsage10SecPercent(), monitor.getLastHeapUsage10SecPercent(), (int) (monitor.getAvgActiveArteCount() + 0.5));

        }
    }

    public int getArtePoolLoadPercent(final int priority) {
        if (priority == EPriority.NORMAL.getValue().intValue()) {
            return artePoolLoadPercentNormal;
        } else if (priority == EPriority.ABOVE_NORMAL.getValue().intValue()) {
            return artePoolLoadPercentAboveNormal;
        } else if (priority == EPriority.HIGH.getValue().intValue()) {
            return artePoolLoadPercentHigh;
        } else if (priority == EPriority.VERY_HIGH.getValue().intValue()) {
            return artePoolLoadPercentVeryHigh;
        } else if (priority == EPriority.CRITICAL.getValue().intValue()) {
            return artePoolLoadPercentCritical;
        }
        return artePoolLoadPercentNormal;
    }

    private void updateJmxState() {
        if (monitor != null) {
            final ArteWatchDog watchDog = arteWatchDog;
            int deactivationsByLongDbQuery = 0;
            if (watchDog != null) {
                deactivationsByLongDbQuery = watchDog.getDeactivationsByLongDbQuery();
            }
            final List<ArteInstance> arteInstances = artePool.getInstances(true);
            instanceJmxState = new InstanceJMXState(arteInstances.size(),
                    monitor.getAvgActiveArteCount(),
                    monitor.getAvgArteProcessTimeMs(),
                    monitor.getAvgArteCpuProcessTimeMs(),
                    monitor.getAvgArteDbProcessTimeMs(),
                    monitor.getAvgArteExtProcessTimeMs(),
                    monitor.getAvgArteOtherProcessTimeMs(),
                    deactivationsByLongDbQuery,
                    monitor.getTotalArteRequests(),
                    activeArteResourceManager == null ? -1 : activeArteResourceManager.getCapturedTicketsCount(EPriority.NORMAL.getValue().intValue()),
                    monitor.getArteActivations(),
                    artePoolLoadPercentNormal
            );
        }
        final List<ResourceRegistryItemJMXInfo> infos = new ArrayList<>();
        for (IResourceRegistryItem item : resourceRegistry.getItems()) {
            infos.add(new ResourceRegistryItemJMXInfo(item.getKey(), item.getDescription(), item.isClosed()));
        }
        Collections.sort(infos, new Comparator<ResourceRegistryItemJMXInfo>() {

            @Override
            public int compare(ResourceRegistryItemJMXInfo o1, ResourceRegistryItemJMXInfo o2) {
                if (o1.getKey() == null) {
                    return o2.getKey() == null ? 0 : -1;
                }
                if (o2.getKey() == null) {
                    return 1;
                }
                return o1.getKey().compareTo(o2.getKey());
            }

        });
        resourceRegistryItemsForJMX = infos;
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
        boolean bNeedGc = (memUsagePercent(allMemoryBean.getHeapMemoryUsage()) > Instance.MEM_USAGE_WARN_PERCENT)
                || (memUsagePercent(allMemoryBean.getNonHeapMemoryUsage()) > Instance.MEM_USAGE_WARN_PERCENT);
        final List<MemoryPoolMXBean> memPoolMxBeans = ManagementFactory.getMemoryPoolMXBeans();
        for (MemoryPoolMXBean poolBean : memPoolMxBeans) {
            if (bNeedGc) {
                break;
            }
            bNeedGc = memUsagePercent(poolBean.getUsage()) > Instance.MEM_USAGE_WARN_PERCENT;
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
        setDbConnection((RadixDataSource) radixDataSource, proxyOraUser, null);
        while (!isShuttingDown()) {
            try {
                setDbConnection((RadixDataSource) radixDataSource, proxyOraUser, openNewInstanceDbConnection());
                final String title = getFullTitle();
                trace.put(EEventSeverity.EVENT, Messages.DB_CONNECTION_RESTORED, Messages.MLS_ID_DB_CONNECTION_RESTORED, new ArrStr(title), EEventSource.INSTANCE, false);
                break;
            } catch (SQLException e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                trace.put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + ":\n" + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(exStack), EEventSource.INSTANCE, false);
                artePool.maintenance();//so ARTE's can discover that db connection has been possibly closed
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

    public InstanceView getViewIfCreated() {
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

    public void unloadUnit(final Unit unit) {
        units.unloadUnit(unit);
    }

    public final SingletonUnitSessionLock getSingletonUnitLock() {
        return singletonUnitLock;
    }

    public long getLatestVersion() {
        return latestVersion;
    }

    void actualizeLoader(final boolean updateVersion) {
        final boolean forcedActualize = isActualizeVerSchedulled;
        isActualizeVerSchedulled = false;
        try {
            final List<RadixLoaderActualizer.DdsVersionWarning> ddsWarnings = new LinkedList<>();
            final Connection db = getDbConnection();
            RadixLoaderActualizer.getInstance().actualize(db, ddsWarnings, updateVersion, !autoUpdateEnabled);
            final long curVer = RadixLoader.getInstance().getCurrentRevision();
            if (latestVersion != -1 && latestVersion != curVer) {
                logLocalFileReplacements(false);
            }
            latestVersion = curVer;
            if (lastAcceptableRevMeta == null || autoUpdateEnabled) {
                lastAcceptableRevMeta = RadixLoader.getInstance().getCurrentRevisionMeta();
                rereadVersionsInfo();
            }
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
        } catch (KernelVersionError ex) {
            if (!kernelVersionChanged) {
                trace.put(EEventSeverity.WARNING, String.format(Messages.ERR_KERNEL_VER, ex.getLoadedVersion(), ex.getActualVersion()), Messages.MLS_ID_ERR_KERNEL_VER, new ArrStr(getFullTitle(), String.valueOf(ex.getLoadedVersion()), String.valueOf(ex.getActualVersion())), EEventSource.INSTANCE, false);
                restartServer("kernel version was changed", forcedActualize ? 0 : options.getDelayBeforeAutoRestartSec());
                kernelVersionChanged = true;
            }
        } catch (Throwable t) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(t);
            trace.put(EEventSeverity.ERROR, Messages.ERR_ON_LOADER_ACTUALIZE + ": \n" + exStack, Messages.MLS_ID_ERR_ON_LOADER_ACTUALIZE, new ArrStr(getFullTitle(), exStack), EEventSource.INSTANCE, false);
        }
    }

    protected void restartServer(final String reason) {
        restartServer(reason, 0);
    }

    protected void restartServer(final String reason, int delaySec) {
        restartServer(reason, delaySec, (long) Integer.MAX_VALUE);
    }

    protected void restartServer(final String reason, int delaySec, Long stopTimeoutSec) {
        actualRestartDelaySec = Math.max(delaySec, 0);
        stopTimeoutOnRestartSec = stopTimeoutSec;
        Starter.mustRestart(SrvRunParams.getRestartParams());
        restartReason = reason;
        new Thread(restartRunnable, "Old instance killer").start();
    }

    public void setUnitStartPostponedInDb(final long unitId, final boolean postponed) throws SQLException {
        if (getState() != InstanceState.STARTED && getState() != InstanceState.STARTING) {
            throw new InvalidInstanceState(Messages.INSTANCE_IS_NOT_STARTED);
        }
        getDbQueries().setUnitStartPostponedInDb(unitId, postponed);
    }

    public UnitDescription getStartedUnitIdByTypeAndAadcMember(final Long unitType) throws SQLException {
        if (getState() != InstanceState.STARTED && getState() != InstanceState.STARTING) {
            throw new InvalidInstanceState(Messages.INSTANCE_IS_NOT_STARTED);
        }
        return getDbQueries().getStartedUnitId(unitType, getAadcInstMemberId());
    }

    public long getPrimaryUnitId(final long unitId) throws SQLException {
        if (getState() != InstanceState.STARTED && getState() != InstanceState.STARTING) {
            throw new InvalidInstanceState(Messages.INSTANCE_IS_NOT_STARTED);
        }
        return dbQueries.getPrimaryUnitId(unitId);
    }

    public UnitDescription getStartedUnitIdByPrimary(final long primaryUnitId) throws SQLException {
        if (getState() != InstanceState.STARTED && getState() != InstanceState.STARTING) {
            throw new InvalidInstanceState(Messages.INSTANCE_IS_NOT_STARTED);
        }
        return getDbQueries().getStartedDuplicatedUnitForPrimary(primaryUnitId);
    }

    public UnitDescription getStartedDuplicatedExecutor(final JobExecutorUnit unit) throws SQLException {
        if (getState() != InstanceState.STARTED && getState() != InstanceState.STARTING) {
            throw new InvalidInstanceState(Messages.INSTANCE_IS_NOT_STARTED);
        }
        return getDbQueries().getStartedDuplicatedExecutorUnitId(unit, getAadcInstMemberId());
    }

    boolean startUnit(final Unit unit, final String reason) throws InterruptedException {
        return units.startUnit(unit, reason);
    }

    boolean stopUnit(final Unit unit, final String reason) {
        return units.stopUnit(unit, reason);
    }

    public FileLogOptions getFileLogOptions() {
        final TraceOptions opts = curTraceOptions; //saving mutable field value
        if (opts == null) {
            return null;
        }
        return opts.getLogOptions();
    }

    public boolean isGlobalSensitiveTracingOn() {
        final TraceOptions opts = curTraceOptions; //saving mutable field value
        if (opts == null) {
            return false;
        }
        return opts.isGlobalSensitiveTracingOn();
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
        try (final PreparedStatement st = ((RadixConnection) db).prepareStatement(qryCompareLocalAndDbTimeStmt);
                final ResultSet rs = st.executeQuery()) {

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
        }
    }

    public final DataSource getDataSource() {
        return radixDataSource;
    }

    public boolean getAutoActualizeVer() {
        return autoUpdateEnabled;
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

    public Integer getAadcSysMemberId() {
        return options.getAadcSysMemberId();
    }

    public Integer getAadcSysTestedMemberId() {
        return options.getAadcSysTestedMemberId();
    }

    public boolean isAadcTestedMember() {
        return options.getAadcSysTestedMemberId() != null && options.getAadcSysTestedMemberId() == options.getAadcSysMemberId();
    }

    public Integer getAadcInstMemberId() {
        return options.getAadcInstMemberId();
    }

    public int getAadcAffinityTimeoutSec() {
        return options.getAadcAffinityTimeoutSec();
    }

    public EAadcMember getAadcInstMember() {
        if (options.getAadcInstMemberId() == null) {
            return null;
        }
        if (options.getAadcInstMemberId() == 1) {
            return EAadcMember.FIRST;
        } else {
            return EAadcMember.SECOND;
        }
    }

    public String getAadcDgAddress() {
        return options.getAadcDgAddress();
    }

    public int getAadcCommitedLockExp() {
        return options.getAadcCommitedLockExp();
    }

    public String getAadcUnlockTables() {
        return options.getAadcUnlockTables();
    }

    public int getThreadsStateGatherPeriodSec() {
        return options.getThreadsStateGatherPeriodSec();
    }

    public int getThreadsStateForcedGatherPeriodSec() {
        return options.getThreadsStateForcedGatherPeriodSec();
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
        final InstanceDbQueries queries = new InstanceDbQueries(fakeInstance);
        try {
            return queries.readTraceOptions().getLogOptions();//could throw NullPointerException on accessing to fakeInstance.getTrace() on unexpected error
        } finally {
            queries.closeAll();
        }
    }

    public static boolean isRunning(final long id, final Connection connection) throws SQLException {
        try (final PreparedStatement st = ((RadixConnection) connection).prepareStatement(qryIsRunningStmt)) {
            st.setInt(1, Instance.DB_I_AM_ALIVE_TIMEOUT_MILLIS / 1000);
            st.setInt(2, Instance.DB_I_AM_ALIVE_TIMEOUT_MILLIS);
            st.setLong(3, id);
            try (final ResultSet rs = st.executeQuery()) {
                return rs.next();
            }
        }
    }

    public static boolean isActive(final Timestamp dbSelfcheckTimestamp) {
        return dbSelfcheckTimestamp != null && dbSelfcheckTimestamp.getTime() + Instance.DB_I_AM_ALIVE_TIMEOUT_MILLIS > System.currentTimeMillis();
    }

    public boolean isUnitRunning(long unitId, long curMillis, Timestamp lastDbCheckTime) {
        long t0 = curMillis - Unit.DB_I_AM_ALIVE_TIMEOUT_MILLIS;
        Timestamp dgTime = getAadcManager().getUnitSelfCheckTime(unitId);
        return (lastDbCheckTime != null && lastDbCheckTime.getTime() > t0) || (dgTime != null && dgTime.getTime() > t0);
    }

    private void writeHostInfo() {
        if (hostInfoWritedToDatabase) {
            return;
        }
        final int cpuCoreCount = Runtime.getRuntime().availableProcessors();
        final String hostName = org.radixware.kernel.common.utils.SystemTools.getHostName();
        final List<String> ip4Addresses = org.radixware.kernel.common.utils.SystemTools.getIp4Addresses();
        final String ip4AddressesStr = ip4Addresses.isEmpty() ? "<unavailable>" : "[" + StringUtils.join(ip4Addresses, ", ") + "]";
        if (hostName == null) {
            trace.put(EEventSeverity.WARNING, "Failed to get host name", EEventSource.INSTANCE);
        }
        if (ip4Addresses.isEmpty()) {
            trace.put(EEventSeverity.WARNING, "Failed to get IPv4 addresses", EEventSource.INSTANCE);
        }

        trace.put(EEventSeverity.EVENT, "Host into: { cpuCoreCount: " + cpuCoreCount + ", hostName: " + Utils.nvlOf(hostName, "<unavailable>") + ", IPv4 addresses: " + ip4AddressesStr + " }", EEventSource.INSTANCE);
        try {
            getDbQueries().writeHostInfo(cpuCoreCount, hostName, ip4Addresses);
            hostInfoWritedToDatabase = hostName != null && !ip4Addresses.isEmpty();
        } catch (SQLException ex) {
            trace.put(EEventSeverity.ERROR, "Failed to write host info to database: " + ExceptionTextFormatter.exceptionStackToString(ex), EEventSource.INSTANCE);
        }
    }

    public void registerTraceProfileInfos(final Collection<TraceProfileInfo> infos) {
        if (infos != null) {
            for (TraceProfileInfo info : infos) {
                desc2TraceProfileInfo.put(info.getDescription(), info);
            }
        }
    }

    public Collection<TraceProfileInfo> getTraceProfileInfos() {
        return desc2TraceProfileInfo.values();
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
                    loader = new ReleaseLoader(version, new LoadErrorsLog() {
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
                try (PreparedStatement ps = ((RadixConnection) dbConnection).prepareStatement(qrySelectSchemaStmt)) {
                    ps.setString(1, uri);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            IOUtils.copy(rs.getClob(1).getCharacterStream(), bos, "UTF-8");
                            return bos.toByteArray();
                        } else {
                            return null;
                        }
                    }
                } catch (SQLException ex) {
                    throw new IOException(ex);
                }
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

    private static enum ELicenseExpirationCategory {

        EXPIRED(0l, Messages.EXPIRED, -1),
        ONE_MINUTE(60 * 1000l, Messages.ONE_MINUTE, 0),
        TEN_MINUTES(10 * 60 * 1000l, Messages.TEN_MINUTES, 1),
        ONE_HOUR(60 * 60 * 1000l, Messages.ONE_HOUR, 2),
        ONE_DAY(24 * 60 * 60 * 1000l, Messages.ONE_DAY, 3),
        X_DAYS(-1, Messages.DAYS, 4);

        private final long leftMillis;
        private final String leftAsString;
        public final int order;

        private ELicenseExpirationCategory(final long leftMillis, final String leftAsString, final int order) {
            this.leftMillis = leftMillis;
            this.leftAsString = leftAsString;
            this.order = order;
        }

        public long getLeftMillis() {
            return leftMillis == -1 ? SrvRunParams.getLicenseWarnDays() * 24l * 60 * 60 * 1000 : leftMillis;
        }

        public String getLeftAsString() {
            return leftAsString;
        }

        public int getOrder() {
            return order;
        }
    }

    private static class ExpirationNotificationInfo {

        public final String licenseName;
        public long lastControlNanos;
        public long lastLeftMillis;
        public final Set<ELicenseExpirationCategory> givenNotifications;

        public ExpirationNotificationInfo(String licenseName) {
            this.licenseName = licenseName;
            givenNotifications = new TreeSet<>(new Comparator<ELicenseExpirationCategory>() {

                @Override
                public int compare(ELicenseExpirationCategory o1, ELicenseExpirationCategory o2) {
                    if (Objects.equals(o1, o2)) {
                        return 0;
                    }
                    if (o1 != null && o2 == null) {
                        return 1;
                    }
                    if (o1 == null && o2 != null) {
                        return -1;
                    }
                    return o1.getOrder() - o2.getOrder();
                }
            });
        }

    }

    protected static class InstanceServerTrace extends ServerTrace {

        private boolean inited = false;
        private final Instance instance;

        public InstanceServerTrace(Instance instance) {
            this.instance = instance;
        }

        public void beforeInstanceStart(int instanceId) {
            if (!inited) {
                setOwnerDescription("SystemInstance[" + instanceId + "]");
                initLogs(instance, newTracer(EEventSource.INSTANCE.getValue()));
                inited = true;
            }
        }

    }

    private static class LicenseExpirationNotificator {

        private final Map<String, ExpirationNotificationInfo> notifyState = new HashMap<>();

        public void resetAll() {
            notifyState.clear();
        }

        public boolean resetIfChanged(final String licName, final long controlNanos, final long leftMillis) {
            final ExpirationNotificationInfo info = notifyState.get(licName);
            if (info == null) {
                return false;//not changed, just new;
            }
            final long curFinishRelativeMillis = controlNanos / 1000000l + leftMillis;
            final long prevFinishRelativeMillis = info.lastControlNanos / 1000000l + info.lastLeftMillis;
            if (curFinishRelativeMillis != prevFinishRelativeMillis) {
                notifyState.remove(licName);
                return true;
            }
            return false;
        }

        public ELicenseExpirationCategory getLastCategory(final String licName) {
            final ExpirationNotificationInfo info = notifyState.get(licName);
            if (info != null) {
                return info.givenNotifications.isEmpty() ? null : info.givenNotifications.iterator().next();
            }
            return null;
        }

        public void resetXDaysNotifications() {
            for (ExpirationNotificationInfo info : notifyState.values()) {
                info.givenNotifications.remove(ELicenseExpirationCategory.X_DAYS);
            }
        }

        public ELicenseExpirationCategory notify(final String licName, final long controlNanos, final long leftMillis) {
            for (ELicenseExpirationCategory category : ELicenseExpirationCategory.values()) {
                if (leftMillis <= category.getLeftMillis()) {
                    final ExpirationNotificationInfo info = notifyState.get(licName);
                    if (info == null || info.givenNotifications.isEmpty() || category.getOrder() < info.givenNotifications.iterator().next().getOrder()) {
                        final ExpirationNotificationInfo newInfo = new ExpirationNotificationInfo(licName);
                        newInfo.lastControlNanos = controlNanos;
                        newInfo.lastLeftMillis = leftMillis;
                        notifyState.put(licName, newInfo);
                        return category;
                    }
                }
            }
            return null;
        }

    }
}
