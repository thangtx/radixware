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
package org.radixware.kernel.server.monitoring;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.logging.LogFactory;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.instance.arte.IArteRequest;
import org.radixware.kernel.server.types.ProfileStatisticEntry;
import org.radixware.kernel.server.units.Unit;
import org.radixware.kernel.server.units.UnitState;
import org.radixware.kernel.server.utils.IPriorityResourceManager;

/**
 * InstanceMonitor uses dedicated thread to process accumulated data. Methods
 * {@linkplain  start()} and {@linkplain shutdown()} are used to control the
 * state of this thread.
 *
 */
public class InstanceMonitor extends AbstractMonitor {

    private static final boolean ABS_TIME = SystemPropUtils.getBooleanSystemProp("rdx.arte.time.abs", false);

    //constants
    public static final String MONITORING_VIRTUAL_SERVICE = "[mvs]";
    public static final String MONITORED_UNIT_SERVICE_PREFIX = MONITORING_VIRTUAL_SERVICE + "monitored-unit#";
    private static final int PROCESS_WAIT_TIME_MILLIS = 5 * 1000;
    private static final String OVERALL_INSTANCE = MONITORING_VIRTUAL_SERVICE + "overall-instance-arte-sessions-counter";
    private static final ArteSessionCounter EMPTY_COUNTER = new ArteSessionCounter();
    //semaphores
    private final Object sectionsSettingsSem = new Object();
    private final Object tranTimeSettingsSem = new Object();
    private final Object arteSessionsSem = new Object();
    private final Object arteSessionsSettingsSem = new Object();
    private final Object arteInstCountSem = new Object();
    //final fields
    private volatile ArteStatsRegistrationPool curArteStatsPool;
    //@GuardedBy arteSessionsSem
    private final Map<String, ArteSessionCounter> service2counter = new HashMap<>();
    //@GuardedBy arteSessionsSesttingsSem
    private final Map<String, IntegralCountStat> service2stat = new HashMap<>();
    //@GuardedBy arteSessionsSesttingsSem
    private MetricParameters allServicesParameters = null;//if there is a metric with serviceUri=null (all services)
    //@GuardedBy arteSessionsSesttingsSem
    private final Set<String> definedByAllServices = new HashSet<>();//services for wich settings are read from metric with serviceUri = null
    //@GuardedBy arteSessionsSesttingsSem
    private MetricParameters allUnitsParameters = null;//if there is a metric for unit arte sessions with unitId == null
    //@GuardedBy arteSessionsSesttingsSem
    private final Set<String> definedByAllUnits = new HashSet<>();//services for wich settings are read from unit arte sessions metric with unitId == null
    //@GuardedBy tranTimeSettingsSem
    private final Map<EMetric, TranTimeStat> waitMetric2stat = new EnumMap<>(EMetric.class);
    //@GUardedBy sectionsSettingsSem
    private final Map<String, SectionMetricsData> section2data = new HashMap<>();
    private volatile MetricParameters arteInstCountMetricParams = null;
    //@GuardedBy arteInstCountSem
    private int arteInstCount = 0;
    //@GuardedBy arteInstCountSem    
    private final List<MetricRecord> arteInstanceCountRecords = new ArrayList<>();
    private final ProcessorThread processorThread;
    private final Instance instance;
    private volatile boolean countWaitStats = false;
    private volatile Collection<String> monitoredSections = Collections.emptyList();
    private final MetricDescription arteSessCntMetricDescription;
    private final MetricDescription instCpuUsageMetricDescription;
    private final MetricDescription codeCacheMemUsageMetricDescription;
    private final MetricDescription permGenMetricDescription;
    private final MetricDescription metaSpaceMetricDescription;
    private final MetricDescription heapMetricDescription;
    private final MetricDescription aadcLagMetricDescription;
    private final CpuUsageCounter cpuUsageCounter;
    private final CodeCacheMemUsageCounter codeCacheMemUsageCounter = new CodeCacheMemUsageCounter();
    private final PermGenMemUsageCounter permGenMemUsageCounter = new PermGenMemUsageCounter();
    private final MetaSpaceMemUsageCounter metaSpaceMemUsageCounter;
    private final HeapMemUsageCounter heapMemUsageCounter = new HeapMemUsageCounter();
    private final AadcLagCounter aadcLagCounter = new AadcLagCounter();
    private final IntegralCountStat activeArte10SecStat = new IntegralCountStat(new MetricParameters(null, -1, Instance.WRITE_BASIC_STATS_PERIOD_MILLIS));
    private final CpuUsageCounter cpuUsage10SecCounter;
    private final HostCpuUsageCounter hostCpuUsage10SecCounter;
    private final HeapMemUsageCounter heapUsage10SecCounter;
    private final ArteSessionCounter overallActiveSessionsCounter = new ArteSessionCounter();
    private final ChainStat arteProcessTimeMsStat = new ChainStat(1000, 5000);
    private final ChainStat arteProcessTimeCpuMsStat = new ChainStat(1000, 5000);
    private final ChainStat arteProcessTimeDbMsStat = new ChainStat(1000, 5000);
    private final ChainStat arteProcessTimeExtMsStat = new ChainStat(1000, 5000);
    private final ChainStat arteProcessTimeOtherMsStat = new ChainStat(1000, 5000);
    private final ChainStat slidingActiveArteStat = new ChainStat(1000, 5000);
    private volatile double avgActiveArteCount10Sec = 0;
    private volatile int lastCpuUsage10SecPercent;
    private volatile int lastHeapUsage10SecPercent;
    private volatile int lastHostCpuUsage10SecPercent;
    private volatile long totalArteRequests;
    private final AtomicInteger arteActivations = new AtomicInteger();

    public InstanceMonitor(final Instance instance) {
        super(null, instance.getTrace(), EEventSource.INSTANCE.getValue());
        this.instance = instance;
        waitMetric2stat.put(EMetric.PROFILING_PERCENT_CPU, null);
        waitMetric2stat.put(EMetric.PROFILING_PERCENT_DB, null);
        waitMetric2stat.put(EMetric.PROFILING_PERCENT_EXT, null);
        this.processorThread = new ProcessorThread("Instance #" + instance.getId() + " -Monitor-Processor-Thread");
        arteSessCntMetricDescription = new MetricDescription(EMetric.INSTANCE_ARTE_SESSION_CNT, SensorCoordinates.forInstance(instance.getId()));
        instCpuUsageMetricDescription = new MetricDescription(EMetric.INSTANCE_CPU_USAGE, SensorCoordinates.forInstance(instance.getId()));
        codeCacheMemUsageMetricDescription = new MetricDescription(EMetric.INSTANCE_MEMORY_CODE_CACHE, SensorCoordinates.forInstance(instance.getId()));
        permGenMetricDescription = new MetricDescription(EMetric.INSTANCE_MEMORY_PERM_GEN, SensorCoordinates.forInstance(instance.getId()));
        metaSpaceMetricDescription = new MetricDescription(EMetric.INSTANCE_MEMORY_META_SPACE, SensorCoordinates.forInstance(instance.getId()));
        heapMetricDescription = new MetricDescription(EMetric.INSTANCE_MEMORY_HEAP, SensorCoordinates.forInstance(instance.getId()));
        aadcLagMetricDescription = new MetricDescription(EMetric.INSTANCE_AADC_LAG, SensorCoordinates.forInstance(instance.getId()));
        if (CpuUsageCounter.isSupported()) {
            cpuUsageCounter = new CpuUsageCounter();
            cpuUsage10SecCounter = new CpuUsageCounter();
            cpuUsage10SecCounter.setParameters(new MetricParameters(null, -1, Instance.WRITE_BASIC_STATS_PERIOD_MILLIS));
        } else {
            cpuUsageCounter = null;
            cpuUsage10SecCounter = null;
        }
        if (HostCpuUsageCounter.isSupported()) {
            hostCpuUsage10SecCounter = new HostCpuUsageCounter();
            hostCpuUsage10SecCounter.setParameters(new MetricParameters(null, -1, Instance.WRITE_BASIC_STATS_PERIOD_MILLIS));
        } else {
            hostCpuUsage10SecCounter = null;
        }

        heapUsage10SecCounter = new HeapMemUsageCounter();
        heapUsage10SecCounter.setParameters(new MetricParameters(null, -1, Instance.WRITE_BASIC_STATS_PERIOD_MILLIS));
        metaSpaceMemUsageCounter = MetaSpaceMemUsageCounter.isSupported() ? new MetaSpaceMemUsageCounter() : null;
    }

    public void start() {
        processorThread.start();
        activeArte10SecStat.start(new RegisteredItem<>(System.currentTimeMillis(), 0));
        service2counter.put(OVERALL_INSTANCE, overallActiveSessionsCounter);
        final RegisteredItem<Long> zeroItem = new RegisteredItem<>(System.currentTimeMillis(), 0l);
        for (int i = 0; i < slidingActiveArteStat.getMaxChainSize(); i++) {
            slidingActiveArteStat.append(zeroItem);
        }
        curArteStatsPool = new ArteStatsRegistrationPool();
    }

    public void shutdown() throws InterruptedException {
        if (processorThread.isAlive()) {
            if (!processorThread.awaitTermination(5000)) {//XXX: why 5000?
                processorThread.interrupt();
            }
        }
    }

    private class ProcessorThread extends Thread {

        private final Object processSem = new Object();
        private final Object flushCompletedSem = new Object();
        private volatile boolean isShuttingDown = false;
        private volatile int flushWaiters = 0;

        public ProcessorThread(final String name) {
            super(name);
        }

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {
                    synchronized (processSem) {
                        if (flushWaiters == 0) {
                            processSem.wait(PROCESS_WAIT_TIME_MILLIS);
                        }
                    }
                    flushThreadSafe();
                    if (flushWaiters > 0) {
                        synchronized (flushCompletedSem) {
                            flushCompletedSem.notifyAll();
                        }
                    }
                    if (isShuttingDown) {
                        return;
                    }
                } catch (InterruptedException ex) {
                    return;
                }
            }
        }

        /**
         * requests termination and waits for it at most timeOutMillis.
         *
         * @return true if thread was successfully terminated, false otherwise
         */
        public boolean awaitTermination(final long timeOutMillis) throws InterruptedException {
            isShuttingDown = true;
            synchronized (processSem) {
                processSem.notifyAll();
            }
            join(timeOutMillis);
            return !isAlive();
        }

        private void flushThreadSafe() {
            final long flushTimeMillis = System.currentTimeMillis();
            final List<MetricRecord> toWrite = new ArrayList<>();

            packArteStats();

            synchronized (tranTimeSettingsSem) {
                toWrite.addAll(flushTranTimeStat(flushTimeMillis));
            }
            synchronized (sectionsSettingsSem) {
                toWrite.addAll(flushSectionsStat(flushTimeMillis));
            }
            synchronized (arteSessionsSem) {
                synchronized (arteSessionsSettingsSem) {
                    toWrite.addAll(flushArteSessionsStat(flushTimeMillis));
                }
            }
            synchronized (arteInstCountSem) {
                toWrite.addAll(arteInstanceCountRecords);
                arteInstanceCountRecords.clear();
            }
            getWriter().add(toWrite);
        }

        public void awaitFlush(final long timeOutMillis) throws InterruptedException {
            synchronized (processSem) {
                flushWaiters++;
                processSem.notifyAll();
            }
            try {
                synchronized (flushCompletedSem) {
                    flushCompletedSem.wait(timeOutMillis);
                }
            } finally {
                synchronized (processSem) {
                    flushWaiters--;
                }
            }
        }
    }

    public void checkHungUnits() throws SQLException {
        final Collection<MonitoringDbQueries.MonitoredUnitStatus> unitStatuses = getDbQueries().getMonitoredUnitStatuses(instance.getId());
        final List<MetricRecord> recordsToWrite = new ArrayList<>();
        final Map<Long, MonitoringDbQueries.MonitoredUnitStatus> unitId2status = new HashMap<>();
        for (MonitoringDbQueries.MonitoredUnitStatus status : unitStatuses) {
            if (unitId2status.containsKey(status.getUnitId())) {
                if (!status.isGeneric()) {
                    unitId2status.put(status.getUnitId(), status);
                }
            } else {
                unitId2status.put(status.getUnitId(), status);
            }
        }
        final long curTimeMillis = System.currentTimeMillis();
        for (MonitoringDbQueries.MonitoredUnitStatus status : unitId2status.values()) {
            if (status.getCheckTime() == null) {
                continue;
            }
            final MetricDescription desc = new MetricDescription(EMetric.UNIT_HANG, SensorCoordinates.forUnit(status.getUnitId()));
            Timestamp dgTime = instance.getAadcManager().getUnitSelfCheckTime(status.getUnitId());
            if (!instance.isUnitRunning(status.getUnitId(), status.getDbCurMillis(), status.getCheckTime())) {
                if (status.isHung() != Boolean.TRUE && status.isStarted()) {
                    recordsToWrite.add(new MetricRecord(new MetricParameters(desc, status.getTypeId(), -1), new EventValue(0., 1., curTimeMillis)));
                }
            } else {
                if (status.isHung() == Boolean.TRUE) {
                    recordsToWrite.add(new MetricRecord(new MetricParameters(desc, status.getTypeId(), -1), new EventValue(1., 0., curTimeMillis)));
                }
            }
        }
        getWriter().add(recordsToWrite);
    }

    private void flushFromInstanceThread() {
        final long currentTimeMillis = System.currentTimeMillis();
        if (cpuUsageCounter != null) {
            getWriter().add(cpuUsageCounter.flush(currentTimeMillis));
        }
        getWriter().add(codeCacheMemUsageCounter.flush(currentTimeMillis));
        getWriter().add(heapMemUsageCounter.flush(currentTimeMillis));
        getWriter().add(permGenMemUsageCounter.flush(currentTimeMillis));
        if (metaSpaceMemUsageCounter != null) {
            getWriter().add(metaSpaceMemUsageCounter.flush(currentTimeMillis));
        }
        getWriter().add(aadcLagCounter.flush(currentTimeMillis));
    }

    @Override
    public void beforeWriterFlush() {
        try {
            flushFromInstanceThread();
            processorThread.awaitFlush(500);
        } catch (InterruptedException ex) {
            //do nothing
        }
    }

    /**
     * Notify monitor that arte instances count was increased to {@code  count}.
     *
     * @param count can be negative
     */
    public void increaseArteInstCount(final int count) {
        synchronized (arteInstCountSem) {
            arteInstCount += count;
            final MetricParameters params = arteInstCountMetricParams;
            if (params != null) {
                arteInstanceCountRecords.add(new MetricRecord(params, new EventValue((double) (arteInstCount - count), (double) arteInstCount, System.currentTimeMillis())));
            }
        }
    }

    public void setArteInstCount(final int count) {
        synchronized (arteInstCountSem) {
            final MetricParameters params = arteInstCountMetricParams;
            if (params != null) {
                arteInstanceCountRecords.add(new MetricRecord(params, new EventValue((double) (arteInstCount), (double) count, System.currentTimeMillis())));
            }
            arteInstCount = count;
        }
    }

    public void arteSessionStarted(final IArteRequest request) {
        increaseArteSessionsCount(request, 1, null);
        totalArteRequests++;
    }

    public void arteSessionFinished(final IArteRequest request, final ArteWaitStats waitStats) {
        increaseArteSessionsCount(request, -1, waitStats);
    }

    private void increaseArteSessionsCount(final IArteRequest request, final int diff, final ArteWaitStats waitStats) {
        final String serviceUri = getSeviceUri(request);
        final String unitService = getUnitService(request);
        final long curTimeMillis = System.currentTimeMillis();
        synchronized (arteSessionsSem) {
            if (serviceUri != null) {
                increaseCounterForSevice(serviceUri, diff, curTimeMillis);
            }
            if (unitService != null) {
                increaseCounterForSevice(unitService, diff, curTimeMillis);
            }
            increaseCounterForSevice(OVERALL_INSTANCE, diff, curTimeMillis);
            slidingActiveArteStat.append(new RegisteredItem<>(curTimeMillis, (long) overallActiveSessionsCounter.count));
            activeArte10SecStat.append(new RegisteredItem<>(curTimeMillis, overallActiveSessionsCounter.count));
            if (waitStats != null) {
                arteProcessTimeMsStat.append(waitStats.totalNanos() / 1000000);
                arteProcessTimeCpuMsStat.append(waitStats.getCpuNanos() / 1000000);
                arteProcessTimeDbMsStat.append(waitStats.getDbNanos() / 1000000);
                arteProcessTimeExtMsStat.append(waitStats.getExtNanos() / 1000000);
                arteProcessTimeOtherMsStat.append(waitStats.getOtherNanos() / 1000000);
            }
        }
    }

    public void arteActivated(final Arte arte) {
        arteActivations.incrementAndGet();
    }

    public void arteDeactivated(final Arte arte) {
    }

    public int getArteActivations() {
        return arteActivations.get();
    }

    public int getAvgArteProcessTimeMs() {
        synchronized (arteSessionsSem) {
            final IStatValue stat = arteProcessTimeMsStat.getStat();
            if (stat != null) {
                return (int) stat.getAvg();
            }
        }
        return 0;
    }

    public int getAvgArteCpuProcessTimeMs() {
        synchronized (arteSessionsSem) {
            final IStatValue stat = arteProcessTimeCpuMsStat.getStat();
            if (stat != null) {
                return (int) stat.getAvg();
            }
        }
        return 0;
    }

    public int getAvgArteDbProcessTimeMs() {
        synchronized (arteSessionsSem) {
            final IStatValue stat = arteProcessTimeDbMsStat.getStat();
            if (stat != null) {
                return (int) stat.getAvg();
            }
        }
        return 0;
    }

    public int getAvgArteExtProcessTimeMs() {
        synchronized (arteSessionsSem) {
            final IStatValue stat = arteProcessTimeExtMsStat.getStat();
            if (stat != null) {
                return (int) stat.getAvg();
            }
        }
        return 0;
    }

    public int getAvgArteOtherProcessTimeMs() {
        synchronized (arteSessionsSem) {
            final IStatValue stat = arteProcessTimeOtherMsStat.getStat();
            if (stat != null) {
                return (int) stat.getAvg();
            }
        }
        return 0;
    }

    public int getCurActiveArteCount() {
        return overallActiveSessionsCounter.count;
    }

    public double getAvgActiveArteCount() {
        return avgActiveArteCount10Sec;
    }

    public double getSlidingAvgActiveArteCount() {
        synchronized (arteSessionsSem) {
            return slidingActiveArteStat.getStat().getAvg();
        }
    }

    public long getTotalArteRequests() {
        return totalArteRequests;
    }

    public void writeBasicStats() {
        synchronized (arteSessionsSem) {
            final IStatValue val = activeArte10SecStat.flushAndGetLastValue();
            if (val != null) {
                avgActiveArteCount10Sec = val.getAvg();
            }
        }
        int arteInstCountVal;
        synchronized (arteInstCountSem) {
            arteInstCountVal = arteInstCount;
        }
        if (avgActiveArteCount10Sec >= 0 && arteInstCountVal >= 0) {
            getDbQueries().writeBasicInstanceStats(instance.getId(), arteInstCountVal, avgActiveArteCount10Sec);
        }
        final long curTimeMillis = System.currentTimeMillis();
        lastCpuUsage10SecPercent = (int) getLastCounterAvgValue(cpuUsage10SecCounter, curTimeMillis);
        lastHeapUsage10SecPercent = (int) getLastCounterAvgValue(heapUsage10SecCounter, curTimeMillis);
        lastHostCpuUsage10SecPercent = (int) getLastCounterAvgValue(hostCpuUsage10SecCounter, curTimeMillis);
    }

    public int getLastCpuUsage10SecPercent() {
        return lastCpuUsage10SecPercent;
    }

    public int getLastHeapUsage10SecPercent() {
        return lastHeapUsage10SecPercent;
    }

    public int getLastHostCpuUsage10SecPercent() {
        return lastHostCpuUsage10SecPercent;
    }

    private double getLastCounterAvgValue(final AIntCounter counter, final long flushTimeMillis) {
        if (counter == null) {
            return -1;
        }
        final List<MetricRecord> records = counter.flush(flushTimeMillis);
        if (records != null && !records.isEmpty() && records.get(records.size() - 1).getStatValue() != null) {
            return records.get(records.size() - 1).getStatValue().getAvg();
        }
        return -1;
    }

    private String getSeviceUri(final IArteRequest request) {
        if (request != null && request.getUnit() != null) {
            return request.getUnit().getServiceUri();
        }
        return null;
    }

    private String getUnitService(final IArteRequest request) {
        if (request != null && request.getUnit() != null) {
            return encodeUnitById(request.getUnit().getId());
        }
        return null;
    }

    private String encodeUnitById(final long unitId) {
        return MONITORED_UNIT_SERVICE_PREFIX + unitId;
    }

    /**
     * @NotThreadSafe should be called only from instance thread
     */
    public void maintenance() {
        if (cpuUsageCounter != null) {
            cpuUsageCounter.tic();
            cpuUsage10SecCounter.tic();
        }
        if (hostCpuUsage10SecCounter != null) {
            hostCpuUsage10SecCounter.tic();
        }
        heapUsage10SecCounter.tic();
        codeCacheMemUsageCounter.tic();
        heapMemUsageCounter.tic();
        permGenMemUsageCounter.tic();
        if (metaSpaceMemUsageCounter != null) {
            metaSpaceMemUsageCounter.tic();
        }
        aadcLagCounter.tic();
        packArteStats();
    }

    /**
     * should be called under synchronization on arteSessionsSem
     */
    private void increaseCounterForSevice(final String service, final int diff, final long timeMillis) {
        ArteSessionCounter counter = service2counter.get(service);
        if (counter == null) {
            counter = new ArteSessionCounter();
            service2counter.put(service, counter);
        }
        counter.increase(diff, timeMillis);
    }

    @Override
    public void rereadSettings() throws SQLException {
        readSectionsSettings();
        readTranSettings();
        readArteSessionsSettings();
        updateCountWaitStats();
        readArteInstanceSettings();
        readMetricsSettings();
    }

    private void readMetricsSettings() throws SQLException {
        if (cpuUsageCounter != null) {
            cpuUsageCounter.setParameters(getDbQueries().readMetricParameters(instCpuUsageMetricDescription));
        }
        codeCacheMemUsageCounter.setParameters(getDbQueries().readMetricParameters(codeCacheMemUsageMetricDescription));
        permGenMemUsageCounter.setParameters(getDbQueries().readMetricParameters(permGenMetricDescription));
        if (metaSpaceMemUsageCounter != null) {
            metaSpaceMemUsageCounter.setParameters(getDbQueries().readMetricParameters(metaSpaceMetricDescription));
        }
        heapMemUsageCounter.setParameters(getDbQueries().readMetricParameters(heapMetricDescription));
        aadcLagCounter.setParameters(getDbQueries().readMetricParameters(aadcLagMetricDescription));
    }

    private void readArteInstanceSettings() throws SQLException {
        MetricParameters newArteInstCountMetricParams = getDbQueries().readMetricParameters(new MetricDescription(EMetric.INSTANCE_ARTE_INST_CNT.getKindName(), EMetricType.EVENT, SensorCoordinates.forInstance(instance.getId())));
        boolean initRecordRequired = newArteInstCountMetricParams != null && (arteInstCountMetricParams == null || newArteInstCountMetricParams.getTypeId() != arteInstCountMetricParams.getTypeId()) && instance.getArtePool() != null;
        arteInstCountMetricParams = newArteInstCountMetricParams;
        if (initRecordRequired) {
            synchronized (arteInstCountSem) {
                if (arteInstanceCountRecords.isEmpty() || arteInstanceCountRecords.get(arteInstanceCountRecords.size() - 1).getParameters().getTypeId() != arteInstCountMetricParams.getTypeId()) {
                    arteInstanceCountRecords.add(new MetricRecord(arteInstCountMetricParams, new EventValue((double) (0), (double) arteInstCount, System.currentTimeMillis())));
                }
            }
        }
    }

    private void updateCountWaitStats() {
        boolean newCountWaitStats = false;
        for (Map.Entry<EMetric, TranTimeStat> entry : waitMetric2stat.entrySet()) {
            if (entry.getValue() != null && entry.getValue().getState() == EMetricState.ACTIVE) {
                newCountWaitStats = true;
                break;
            }
        }
        countWaitStats = newCountWaitStats;
    }

    private Collection<String> getStartedUnitIds() {
        final List<String> result = new ArrayList<>();
        for (Unit unit : instance.getUnits()) {
            if (unit.getState() == UnitState.STARTED) {
                result.add(String.valueOf(unit.getId()));
            }
        }
        return result;
    }

    private void readArteSessionsSettings() {
        try {

            final MetricParameters overallParameters = getDbQueries().readMetricParameters(arteSessCntMetricDescription);
            final Collection<MetricParameters> monitoredServices = getDbQueries().getMonitoredServices(instance.getId());
            final Collection<MetricParameters> monitoredUnits = getDbQueries().getMonitoredArteUnits(instance.getId());
            final Collection<String> existingServiceUris = getDbQueries().getExistingServiceUris();
            final Collection<String> startedUnitIds = getStartedUnitIds();
            final Set<String> activeServices = new HashSet<>();
            synchronized (arteSessionsSettingsSem) {
                final IntegralCountStat overallStat = service2stat.get(OVERALL_INSTANCE);
                if (overallParameters != null) {
                    if (overallStat == null) {
                        service2stat.put(OVERALL_INSTANCE, new IntegralCountStat(overallParameters));
                    } else {
                        overallStat.setParameters(overallParameters);
                    }
                    activeServices.add(OVERALL_INSTANCE);
                } else if (overallStat != null) {
                    overallStat.setState(EMetricState.DISABLED);
                }

                allServicesParameters = null;
                for (final MetricParameters serviceParams : monitoredServices) {
                    if (serviceParams.getDescription().getSensorCoordinates().getServiceUri() == null) {
                        if (allServicesParameters == null || serviceParams.getDescription().getSensorCoordinates().getInstanceId() != null) {
                            allServicesParameters = serviceParams;
                        }
                    }
                }

                applyParameters(allServicesParameters, monitoredServices, definedByAllServices, activeServices, new IUriCalculator() {
                    @Override
                    public String getUri(MetricParameters parameters) {
                        return parameters.getDescription().getSensorCoordinates().getServiceUri();
                    }
                });
                for (String entry : definedByAllServices) {
                    if (!existingServiceUris.contains(entry)) {
                        activeServices.remove(entry);
                    }
                }

                allUnitsParameters = null;
                for (final MetricParameters serviceParams : monitoredUnits) {
                    if (serviceParams.getDescription().getSensorCoordinates().getUnitId() == null) {
                        allUnitsParameters = serviceParams;
                    }
                }

                applyParameters(allUnitsParameters, monitoredUnits, definedByAllUnits, activeServices, new IUriCalculator() {
                    @Override
                    public String getUri(MetricParameters parameters) {
                        if (parameters.getDescription().getSensorCoordinates().getUnitId() == null) {
                            return null;
                        }
                        return encodeUnitById(parameters.getDescription().getSensorCoordinates().getUnitId());
                    }
                });

                for (String entry : definedByAllUnits) {
                    if (!startedUnitIds.contains(entry.substring(MONITORED_UNIT_SERVICE_PREFIX.length()))) {
                        activeServices.remove(entry);
                    }
                }

                for (Map.Entry<String, IntegralCountStat> entry : service2stat.entrySet()) {
                    if (!activeServices.contains(entry.getKey())) {
                        entry.getValue().setState(EMetricState.DISABLED);
                    }
                }
            }
        } catch (SQLException ex) {
            instance.getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + ExceptionTextFormatter.throwableToString(ex), null, null, EEventSource.INSTANCE, false);
        }
    }

    private void applyParameters(final MetricParameters groupParameters, final Collection<MetricParameters> parametersList, final Set<String> definedByGroupParameters, final Set<String> activeServices, final IUriCalculator uriCalculator) {
        if (groupParameters != null) {
            for (String serviceUri : definedByGroupParameters) {
                activeServices.add(serviceUri);
                final IntegralCountStat stat = service2stat.get(serviceUri);
                if (stat != null) {
                    stat.setParameters(groupParameters);
                }
            }
        } else {
            definedByGroupParameters.clear();
        }

        for (final MetricParameters parameters : parametersList) {
            final String serviceName = uriCalculator.getUri(parameters);
            if (serviceName != null) {
                final IntegralCountStat stat = service2stat.get(serviceName);
                definedByGroupParameters.remove(serviceName);
                activeServices.add(serviceName);
                if (stat != null) {
                    stat.setParameters(parameters);
                } else {
                    final IntegralCountStat integralCountStat = new IntegralCountStat(parameters);
                    service2stat.put(serviceName, integralCountStat);
                }
            }
        }
    }

    private void readTranSettings() throws SQLException {
        final Map<EMetric, MetricParameters> newParams = new EnumMap<>(EMetric.class);
        for (EMetric metric : waitMetric2stat.keySet()) {
            newParams.put(metric, getDbQueries().readMetricParameters(new MetricDescription(metric.getKindName(), metric.getMetricType(), SensorCoordinates.forInstance(instance.getId()))));
        }
        final long curTimeMillis = System.currentTimeMillis();
        MetricParameters params;
        synchronized (tranTimeSettingsSem) {
            for (Map.Entry<EMetric, TranTimeStat> entry : waitMetric2stat.entrySet()) {
                params = newParams.get(entry.getKey());
                if (params != null) {
                    if (entry.getValue() == null) {
                        entry.setValue(new TranTimeStat(params, curTimeMillis));
                    } else {
                        entry.getValue().setParameters(params);
                    }
                } else if (entry.getValue() != null) {
                    entry.getValue().setState(EMetricState.DISABLED);
                }
            }
        }
    }

    public boolean isCountProfileStatistic() {
        return countWaitStats;
    }

    public Collection<String> getMonitoredSections() {
        return monitoredSections;
    }

    public void registerStatistic(final Arte arte, final ArteWaitStats timeStat, final Collection<ProfileStatisticEntry> sectionsStat) {
        final int attempts = 1000000;
        for (int i = 0; i < attempts; i++) {
            final ArteStatsRegistrationPool curPool = curArteStatsPool;
            if (curPool != null && curPool.register(arte, new ArteStatsPortion(timeStat, sectionsStat, -1))) {
                return;
            }
        }
        LogFactory.getLog(InstanceMonitor.class).warn("Unable to register ARTE statistic in " + attempts + " attempts");
    }

    /**
     * should be called under synchronization on arteSessionsSem and
     * arteSessionsSettingsSem
     */
    private List<MetricRecord> flushArteSessionsStat(final long curTimeMillis) {
        final List<MetricRecord> result = new ArrayList<>();
        final Set<String> services = new HashSet<>(service2counter.keySet());
        services.addAll(service2stat.keySet());
        for (String service : services) {
            IntegralCountStat stat = service2stat.get(service);
            ArteSessionCounter counter = service2counter.get(service);
            if (counter == null) {
                counter = EMPTY_COUNTER;
            }
            if (stat == null) {
                if (allServicesParameters != null && !isVirtualMonitoringService(service)) {
                    stat = new IntegralCountStat(null);//parameters will be filled few lines below
                    definedByAllServices.add(service);
                }
                if (allUnitsParameters != null && isVirtualUnitService(service)) {
                    stat = new IntegralCountStat(null);//parameters will be filled few lines below
                    definedByAllUnits.add(service);
                }
            }
            if (stat != null) {
                if (definedByAllServices.contains(service)) {
                    stat.setParameters(parametersForSerice(service, allServicesParameters));
                } else if (definedByAllUnits.contains(service)) {
                    stat.setParameters(parametersForUnitService(service, allUnitsParameters));
                }
                if (!stat.isStarted()) {
                    //initialize stat with current time and current arte sessons count
                    final IntegralCountStat newStat = new IntegralCountStat(stat.getParameters());
                    service2stat.put(service, newStat);
                    newStat.start(new RegisteredItem<>(curTimeMillis, counter.count));
                } else {
                    stat.appendAll(counter.history);
                    stat.flush(curTimeMillis);
                    result.addAll(stat.popRecords());
                }
                if (stat.getState() == EMetricState.DISABLED) {
                    service2stat.remove(service);
                }
            }
            counter.history.clear();
        }
        return result;
    }

    private MetricParameters parametersForSerice(final String service, final MetricParameters genericParameters) {
        return new MetricParameters(
                new MetricDescription(
                        genericParameters.getDescription().getKindName(),
                        EMetricType.STATISTIC,
                        SensorCoordinates.forInstanceService(Long.valueOf(instance.getId()), service)),
                genericParameters.getTypeId(),
                genericParameters.getPeriodMillis());
    }

    private MetricParameters parametersForUnitService(final String virtualUnitService, final MetricParameters genericParameters) {
        return new MetricParameters(
                new MetricDescription(
                        genericParameters.getDescription().getKindName(),
                        EMetricType.STATISTIC,
                        SensorCoordinates.forUnit(Long.valueOf(virtualUnitService.substring(MONITORED_UNIT_SERVICE_PREFIX.length())))),
                genericParameters.getTypeId(),
                genericParameters.getPeriodMillis());
    }

    private boolean isVirtualMonitoringService(final String service) {
        return service == null ? false : service.startsWith(MONITORING_VIRTUAL_SERVICE);
    }

    private boolean isVirtualUnitService(final String service) {
        return service != null && service.startsWith(MONITORED_UNIT_SERVICE_PREFIX);
    }

    /**
     * should be called under synchronization on tranTimeSettingsSem
     */
    private List<MetricRecord> flushTranTimeStat(final long flushTimeMillis) {
        final List<MetricRecord> toWrite = new ArrayList<>();
        for (final Map.Entry<EMetric, TranTimeStat> entry : waitMetric2stat.entrySet()) {
            if (entry.getValue() != null) {
                entry.getValue().flush(flushTimeMillis);
                toWrite.addAll(entry.getValue().popRecords());
                if (entry.getValue().getState() == EMetricState.DISABLED) {
                    entry.setValue(null);
                }
            }
        }
        return toWrite;
    }

    private void packArteStats() {

        final ArteStatsRegistrationPool curPool = curArteStatsPool;
        curArteStatsPool = new ArteStatsRegistrationPool();

        try {
            Thread.sleep(1);//let current writers to finish
        } catch (InterruptedException ex) {
            //ignore
        }

        curPool.close();

        final long closeMillis = System.currentTimeMillis();

        final List<ArteWaitStats> waitStats = curPool.getTotalWaitStats();
        final List<ProfileStatisticEntry> sectionStats = curPool.getTotalProfileStats();

        synchronized (tranTimeSettingsSem) {
            for (final TranTimeStat stat : waitMetric2stat.values()) {
                if (stat != null) {
                    for (ArteWaitStats stats : waitStats) {
                        stat.append(new RegisteredItem<>(closeMillis, stats));
                    }
                }
            }
        }

        final Map<String, List<RegisteredItem<ProfileStatisticEntry>>> profileEntries = new HashMap<>();
        List<RegisteredItem<ProfileStatisticEntry>> list;
        for (final ProfileStatisticEntry item : sectionStats) {
            list = profileEntries.get(item.getSectionId());
            if (list == null) {
                list = new ArrayList<>();
                profileEntries.put(item.getSectionId(), list);
            }
            list.add(new RegisteredItem<>(closeMillis, item));
        }
        synchronized (sectionsSettingsSem) {
            for (Map.Entry<String, List<RegisteredItem<ProfileStatisticEntry>>> entry : profileEntries.entrySet()) {
                final String sectionName;
                int colonIndex = entry.getKey().indexOf(":");
                if (colonIndex >= 0) {
                    sectionName = entry.getKey().substring(0, colonIndex);
                } else {
                    sectionName = entry.getKey();
                }
                SectionMetricsData sectionData = section2data.get(sectionName);
                if (sectionData != null) {
                    sectionData.append(entry.getValue());
                }
            }
        }
    }

    /**
     * Should be called under synchronization on profileSem.
     */
    private List<MetricRecord> flushSectionsStat(final long flushTimeMillis) {
        final List<MetricRecord> toWrite = new ArrayList<>();
        final List<String> disabledSections = new ArrayList<>();
        SectionMetricsData data;
        for (final Map.Entry<String, SectionMetricsData> entry : section2data.entrySet()) {
            data = entry.getValue();
            data.flush(toWrite, flushTimeMillis);
            if (data.getCurrentStats().isEmpty()) {
                disabledSections.add(entry.getKey());
            }
        }
        for (final String disabledSection : disabledSections) {
            section2data.remove(disabledSection);
        }
        return toWrite;
    }

    /**
     * Should be called only from instance main thread
     *
     * @NonThreadSafe
     */
    private void readSectionsSettings() {
        try {
            final Collection<MetricParameters> monitoredSectionMetrics = getDbQueries().getMonitoredSections(instance.getId());
            final long curTimeMillis = System.currentTimeMillis();
            synchronized (sectionsSettingsSem) {
                for (final SectionMetricsData metricData : section2data.values()) {
                    for (AbstractStat stat : metricData.getCurrentStats()) {
                        stat.setState(EMetricState.DISABLED);
                    }
                }
                SectionMetricsData data;
                for (final MetricParameters params : monitoredSectionMetrics) {
                    data = section2data.get(params.getDescription().getSensorCoordinates().getSectionName());
                    if (data != null) {
                        boolean found = false;
                        for (AbstractStat stat : data.getCurrentStats()) {
                            if (stat.getParameters().getDescription().getKindName().equals(params.getDescription().getKindName())) {
                                stat.setState(EMetricState.ACTIVE);
                                stat.setParameters(params);
                                found = true;
                            }
                        }
                        if (!found) {
                            data.addStat(params, curTimeMillis);
                        }
                    } else {
                        data = new SectionMetricsData();
                        data.addStat(params, curTimeMillis);
                        section2data.put(params.getDescription().getSensorCoordinates().getSectionName(), data);
                    }
                }
                monitoredSections = Collections.unmodifiableList(new ArrayList<>(section2data.keySet()));
            }
        } catch (SQLException ex) {
            instance.getTrace().put(EEventSeverity.ERROR, Messages.ERR_ON_GET_MONITORED_SECTIONS + ExceptionTextFormatter.throwableToString(ex), null, null, EEventSource.INSTANCE, false);
            synchronized (sectionsSettingsSem) {
                monitoredSections = Collections.emptyList();
                section2data.clear();
            }
        }
    }

    private class SectionMetricsData {

        private DurationStat durationStat;
        private CountStat countStat;
        private FrequencyPerSecondStat freqStat;

        public List<AbstractStat> getCurrentStats() {
            final List<AbstractStat> stats = new ArrayList<AbstractStat>();
            if (durationStat != null) {
                stats.add(durationStat);
            }
            if (countStat != null) {
                stats.add(countStat);
            }
            if (freqStat != null) {
                stats.add(freqStat);
            }
            return stats;
        }

        private void addStat(final MetricParameters params, final long curTimeMillis) {
            if (EMetric.PROFILING_CNT.getKindName().equals(params.getDescription().getKindName())) {
                countStat = new CountStat(params, curTimeMillis);
            } else if (EMetric.PROFILING_DURATION.getKindName().equals(params.getDescription().getKindName())) {
                durationStat = new DurationStat(params, curTimeMillis);
            } else if (EMetric.PROFILING_FREQ.getKindName().equals(params.getDescription().getKindName())) {
                freqStat = new FrequencyPerSecondStat(params, curTimeMillis);
            } else {
                instance.getTrace().put(EEventSeverity.WARNING, String.format(Messages.ERR_UNSUPPORTED_METRIC_KIND, params.getDescription().getKindName()), null, null, EEventSource.INSTANCE, false);
            }
        }

        public void append(final List<RegisteredItem<ProfileStatisticEntry>> data) {
            if (data == null) {
                return;
            }
            if (durationStat != null) {
                durationStat.appendAll(data);
            }
            for (RegisteredItem<ProfileStatisticEntry> entry : data) {
                if (countStat != null) {
                    countStat.append(new RegisteredItem<>(entry.regTimeMillis, (double) entry.val.getCount()));
                }
                if (freqStat != null) {
                    freqStat.append(new RegisteredItem<>(entry.regTimeMillis, (double) entry.val.getCount()));
                }
            }
        }

        public void flush(final List<MetricRecord> toWrite, final long flushTimeMillis) {
            for (AbstractStat stat : getCurrentStats()) {
                stat.flush(flushTimeMillis);
                toWrite.addAll(stat.popRecords());
            }
            if (countStat != null && countStat.getState() == EMetricState.DISABLED) {
                countStat = null;
            }
            if (freqStat != null && freqStat.getState() == EMetricState.DISABLED) {
                freqStat = null;
            }
            if (durationStat != null && durationStat.getState() == EMetricState.DISABLED) {
                durationStat = null;
            }
        }
    }

    private static class TranTimeStat extends AbstractStat<ArteWaitStats> {

        private long curTotalNanos = 0;
        private long curNanos = 0;

        public TranTimeStat(final MetricParameters parameters, final long curPeriodStartMillis) {
            super(parameters, curPeriodStartMillis);
        }

        @Override
        public StatValue closePeriod() {
            double val = curTotalNanos > 0 ? (ABS_TIME ? curNanos / 1000000 : 100. * curNanos / curTotalNanos) : 0.;
            return new StatValue(val, val, val);
        }

        @Override
        public void newPeriodStarted() {
            curNanos = 0;
            curTotalNanos = 0;
        }

        @Override
        protected void appendInternal(final RegisteredItem<ArteWaitStats> entry) {
            final double val;
            if (getParameters().getDescription().getKindName().equals(EMetric.PROFILING_PERCENT_CPU.getKindName())) {
                val = entry.val.getCpuNanos();
            } else if (getParameters().getDescription().getKindName().equals(EMetric.PROFILING_PERCENT_DB.getKindName())) {
                val = entry.val.getDbNanos();
            } else if (getParameters().getDescription().getKindName().equals(EMetric.PROFILING_PERCENT_EXT.getKindName())) {
                val = entry.val.getExtNanos();
            } else {
                val = 0;//should not happen
            }
            curTotalNanos += entry.val.totalNanos() - entry.val.getIdleNanos();
            curNanos += val;
        }
    }

    private static abstract class AIntCounter {

        private IntegralCountStat stat;
        private boolean zeroStartElementAdded = false;
        private static final String CUR_VALUE_CALC_ERROR_FLOOD_PREFIX = "ResourceUsageValueCalcError";

        public void tic() {
            int currentValue = -1;
            try {
                currentValue = getCurrentValue();
            } catch (Throwable t) {
                final String floodKey = CUR_VALUE_CALC_ERROR_FLOOD_PREFIX + ":" + getClass().getName();
                Instance.get().getTrace().setFloodPeriod(floodKey, 10 * 60 * 1000);
                Instance.get().getTrace().putFloodControlled(floodKey, EEventSeverity.WARNING, "Unable to calculate current value for " + getClass().getName() + ": " + ExceptionTextFormatter.throwableToString(t), null, null, EEventSource.INSTANCE.getValue(), -1, false, null);
            }
            if (currentValue != -1 && stat != null) {
                if (!stat.isStarted()) {
                    stat.start(new RegisteredItem<>(System.currentTimeMillis(), currentValue));
                } else {
                    stat.append(new RegisteredItem<>(System.currentTimeMillis(), currentValue));
                }
            }
        }

        /**
         * Get current resource usage value, -1 if undefined. Called once per
         * tic. -1 values are ignored.
         */
        protected abstract int getCurrentValue();

        public List<MetricRecord> flush(final long flushTimeMillis) {
            if (stat == null) {
                return Collections.emptyList();
            }
            stat.flush(flushTimeMillis);
            if (!zeroStartElementAdded) {
                //insert zero element first so metric control task could know that instance was restarted
                final List<MetricRecord> collectedRecords = stat.popRecords();
                if (collectedRecords != null && !collectedRecords.isEmpty()) {
                    zeroStartElementAdded = true;
                    final List<MetricRecord> records = new ArrayList<>();
                    records.add(new MetricRecord(collectedRecords.get(0).getParameters(), new StatValue(0d, 0d, 0d, collectedRecords.get(0).getStatValue().getBegTimeMillis(), collectedRecords.get(0).getStatValue().getBegTimeMillis())));
                    records.addAll(collectedRecords);
                    return records;
                } else {
                    return collectedRecords;
                }
            } else {
                return stat.popRecords();
            }
        }

        public void setParameters(final MetricParameters parameters) {
            if (parameters == null) {
                stat = null;//disable monitoring
            } else {
                if (stat != null) {
                    stat.setParameters(parameters);
                } else {
                    stat = new IntegralCountStat(parameters);
                }
            }

        }
    }

    /**
     * @NotThreadSafe
     */
    private static class CpuUsageCounter extends AIntCounter {

        private long lastControlSystemNanos = -1;
        private long lastConrolCpuNanos = -1;
        private final Object mxBeanObject;
        private final Method processCpuNanoTimeGetter;

        public CpuUsageCounter() {
            final Object[] objectAndMethod = getMxBeanObjectAndCpuNanoTimeGetterMethod();
            mxBeanObject = objectAndMethod[0];
            processCpuNanoTimeGetter = (Method) objectAndMethod[1];
            processCpuNanoTimeGetter.setAccessible(true);
        }

        @Override
        protected int getCurrentValue() {
            try {
                final long currentSystemNanos = System.nanoTime();
                final long currentCpuNanos = (Long) processCpuNanoTimeGetter.invoke(mxBeanObject);
                final int jvmCpuTimePercent;
                if (lastControlSystemNanos != -1) {
                    jvmCpuTimePercent = (int) (100 * (currentCpuNanos - lastConrolCpuNanos) / (currentSystemNanos - lastControlSystemNanos) / Runtime.getRuntime().availableProcessors());
                } else {
                    jvmCpuTimePercent = -1;
                }
                lastControlSystemNanos = currentSystemNanos;
                lastConrolCpuNanos = currentCpuNanos;
                return jvmCpuTimePercent;
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                //should not happen
            }
            return -1;
        }

        public static boolean isSupported() {
            return getMxBeanObjectAndCpuNanoTimeGetterMethod() != null;
        }

        private static Object[] getMxBeanObjectAndCpuNanoTimeGetterMethod() {
            final OperatingSystemMXBean mxBean = ManagementFactory.getOperatingSystemMXBean();
            try {
                final Method method = mxBean.getClass().getMethod("getProcessCpuTime");
                if (method.getReturnType() == Long.TYPE) {
                    return new Object[]{mxBean, method};
                }
            } catch (NoSuchMethodException | SecurityException ex) {
                //skip to return null in the end
            }
            return null;
        }
    }

    private static class HostCpuUsageCounter extends AIntCounter {

        private final Object mxBeanObject;
        private final Method getterMethod;

        public HostCpuUsageCounter() {
            final Object[] objectAndMethod = getMxBeanObjectAndHostCpuLoadPercentGetterMethod();
            mxBeanObject = objectAndMethod[0];
            getterMethod = (Method) objectAndMethod[1];
            getterMethod.setAccessible(true);
        }

        @Override
        protected int getCurrentValue() {
            try {
                final double load = (Double) getterMethod.invoke(mxBeanObject);
                return (int) (load * 100);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                //should not happen
            }
            return -1;
        }

        public static boolean isSupported() {
            return getMxBeanObjectAndHostCpuLoadPercentGetterMethod() != null;
        }

        private static Object[] getMxBeanObjectAndHostCpuLoadPercentGetterMethod() {
            final OperatingSystemMXBean mxBean = ManagementFactory.getOperatingSystemMXBean();
            try {
                final Method method = mxBean.getClass().getMethod("getSystemCpuLoad");
                if (method.getReturnType() == Double.TYPE) {
                    return new Object[]{mxBean, method};
                }
            } catch (NoSuchMethodException | SecurityException ex) {
                //skip to return null in the end
            }
            return null;
        }
    }

    private static abstract class AbstractMemoryPoolBeanCounter extends AIntCounter {

        protected static final long MBYTE_SIZE_IN_BYTES = 1024 * 1024;

        private static MemoryPoolMXBean getMemoryPoolBeanByNameParts(String[] nameParts) {
            final List<MemoryPoolMXBean> memPoolMxBeans = ManagementFactory.getMemoryPoolMXBeans();
            for (MemoryPoolMXBean poolBean : memPoolMxBeans) {
                final String lowerName = poolBean.getName().toLowerCase();
                if (poolBean.isValid()) {
                    boolean containsAll = true;
                    for (String namePart : nameParts) {
                        containsAll &= lowerName.contains(namePart);
                    }
                    if (containsAll) {
                        return poolBean;
                    }
                }
            }
            return null;
        }

        abstract String[] getBeanNameParts();

        protected final MemoryPoolMXBean bean = getMemoryPoolBeanByNameParts(getBeanNameParts());

        @Override
        protected int getCurrentValue() {
            return bean == null ? -1 : getMemoryUsagePercent(bean.getUsage());
        }
    }

    private static class CodeCacheMemUsageCounter extends AbstractMemoryPoolBeanCounter {

        @Override
        String[] getBeanNameParts() {
            return new String[]{"code", "cache"};
        }
    }

    private static class PermGenMemUsageCounter extends AbstractMemoryPoolBeanCounter {

        @Override
        String[] getBeanNameParts() {
            return new String[]{"perm", "gen"};
        }
    }

    private static class MetaSpaceMemUsageCounter extends AbstractMemoryPoolBeanCounter {

        protected static final boolean isSupported() {
            return new MetaSpaceMemUsageCounter().bean != null;
        }

        @Override
        String[] getBeanNameParts() {
            return new String[]{"meta", "space"};
        }

        @Override
        protected int getCurrentValue() {
            return bean == null ? -1 : (int) (bean.getUsage().getUsed() / MBYTE_SIZE_IN_BYTES);
        }
    }

    private static class HeapMemUsageCounter extends AIntCounter {

        @Override
        protected int getCurrentValue() {
            return getMemoryUsagePercent(ManagementFactory.getMemoryMXBean().getHeapMemoryUsage());
        }
    }

    private static int getMemoryUsagePercent(final MemoryUsage memoryUsage) {
        if (memoryUsage == null) {
            return -1;
        }
        return (int) (memoryUsage.getUsed() * 100 / memoryUsage.getMax());
    }

    private static class ArteSessionCounter {

        private int count = 0;
        private final List<RegisteredItem<Integer>> history = new ArrayList<>();

        public void increase(final int diff, final long timeMillis) {
            count += diff;
            history.add(new RegisteredItem<>(timeMillis, count));
        }
    }

    private static class AadcLagCounter extends AIntCounter {

        @Override
        protected int getCurrentValue() {
            try {
                return Instance.get().getAadcManager().getCurrentLag();
            } catch (SQLException ex) {
                return -1;
            }
        }
    }

    private static interface IUriCalculator {

        public String getUri(final MetricParameters parameters);
    }

    private static class ArteStatsPortion {

        public final long timestampMillis;
        public final ArteWaitStats waitStats;
        public final List<ProfileStatisticEntry> profileEntries;

        public ArteStatsPortion(ArteWaitStats waitStats, Collection<ProfileStatisticEntry> profileEntries, long timestampMillis) {
            this.timestampMillis = timestampMillis;
            this.waitStats = waitStats;
            this.profileEntries = profileEntries == null ? null : new ArrayList<>(profileEntries);
        }

    }

    private static class ArteStatsRegistrationPool {

        private final ConcurrentHashMap<Arte, List<ArteStatsPortion>> arteStatsMap = new ConcurrentHashMap<>();
        private final List<ArteWaitStats> totalWaitStats = new ArrayList<>();
        private final List<ProfileStatisticEntry> totalProfileStats = new ArrayList<>();
        private final AtomicInteger writers = new AtomicInteger();

        //@ThreadSafe
        public synchronized void close() {
            final int attempts = 1000000;
            for (int i = 0; i < attempts; i++) {
                if (writers.compareAndSet(0, Integer.MIN_VALUE)) {
                    break;
                }
                if (i == attempts - 1) {
                    LogFactory.getLog(InstanceMonitor.class).warn("Unable to close arte stats pool properly");
                }
            }

            Iterator<Map.Entry<Arte, List<ArteStatsPortion>>> it = arteStatsMap.entrySet().iterator();

            while (it.hasNext()) {
                final Map.Entry<Arte, List<ArteStatsPortion>> entry = it.next();
                List<ArteStatsPortion> dataList = entry.getValue();
                List<ArteStatsPortion> copyList = null;
                if (dataList != null) {
                    synchronized (dataList) {
                        copyList = new ArrayList<>(dataList);
                        dataList.clear();
                    }
                }
                if (copyList != null) {
                    for (ArteStatsPortion portion : copyList) {
                        if (portion.waitStats != null) {
                            totalWaitStats.add(portion.waitStats);
                        }
                        if (portion.profileEntries != null) {
                            for (ProfileStatisticEntry stat : portion.profileEntries) {
                                totalProfileStats.add(stat);
                            }
                        }
                    }
                }
            }

        }

        public synchronized List<ArteWaitStats> getTotalWaitStats() {
            return totalWaitStats;
        }

        public synchronized List<ProfileStatisticEntry> getTotalProfileStats() {
            return totalProfileStats;
        }

        /**
         * @ThreadSafe @return true record has been successfully registered,
         * false if pool is already closed;
         */
        public boolean register(final Arte arte, final ArteStatsPortion portion) {
            if (writers.incrementAndGet() > 0) {
                try {
                    final List<ArteStatsPortion> newList = new ArrayList<>();
                    newList.add(portion);
                    final List<ArteStatsPortion> existingList = arteStatsMap.putIfAbsent(arte, newList);
                    if (existingList != null) {
                        synchronized (existingList) {
                            existingList.add(portion);
                        }
                    }
                    return true;
                } finally {
                    writers.decrementAndGet();
                }
            }
            return false;
        }
    }
}
