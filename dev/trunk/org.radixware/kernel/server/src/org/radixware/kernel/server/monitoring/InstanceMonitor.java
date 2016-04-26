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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.instance.arte.IArteRequest;
import org.radixware.kernel.server.types.ProfileStatisticEntry;
import org.radixware.kernel.server.units.Unit;
import org.radixware.kernel.server.units.UnitState;

/**
 * InstanceMonitor uses dedicated thread to process accumulated data. Methods
 * {@linkplain  start()} and {@linkplain shutdown()} are used to control the
 * state of this thread.
 *
 */
public class InstanceMonitor extends AbstractMonitor {

    //constants
    public static final String MONITORING_VIRTUAL_SERVICE = "[mvs]";
    public static final String MONITORED_UNIT_SERVICE_PREFIX = MONITORING_VIRTUAL_SERVICE + "monitored-unit#";
    private static final int PROCESS_WAIT_TIME_MILLIS = 5 * 1000;
    private static final String OVERALL_INSTANCE = MONITORING_VIRTUAL_SERVICE + "overall-instance-arte-sessions-counter";
    private static final ArteSessionCounter EMPTY_COUNTER = new ArteSessionCounter();
    //semaphores
    private final Object profileSem = new Object();
    private final Object sectionsSettingsSem = new Object();
    private final Object tranTimeSettingsSem = new Object();
    private final Object arteSessionsSem = new Object();
    private final Object arteSessionsSettingsSem = new Object();
    private final Object arteInstCountSem = new Object();
    //final fields
    //@GuardedBy profileSem
    private final List<RegisteredItem<ProfileStatisticEntry>> sectionsStatQueue = new ArrayList<>();
    //@GuardedBy profileSem
    private final List<RegisteredItem<TransactionTimeStatistic>> tranTimeStatQueue = new ArrayList<>();
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
    private final Map<EMetric, TranTimeStat> tranMetric2stat = new EnumMap<>(EMetric.class);
    //@GUardedBy sectionsSettingsSem
    private final Map<String, SectionMetricsData> section2data = new HashMap<>();
    private volatile MetricParameters arteInstCountMetricParams = null;
    //@GuardedBy arteInstCountSem
    private int arteInstCount = 0;
    //@GuardedBy arteInstCountSem    
    private final List<MetricRecord> arteInstanceCountRecords = new ArrayList<>();
    private final ProcessorThread processorThread;
    private final Instance instance;
    private volatile boolean countProfileStatistic = false;
    private volatile Collection<String> monitoredSections = Collections.emptyList();
    private final MetricDescription arteSessCntMetricDescription;
    private final MetricDescription instCpuUsageMetricDescription;
    private final MetricDescription codeCacheMemUsageMetricDescription;
    private final MetricDescription permGenMetricDescription;
    private final MetricDescription heapMetricDescpription;
    private final CpuUsageCounter cpuUsageCounter;
    private final CodeCacheMemUsageCounter codeCacheMemUsageCounter = new CodeCacheMemUsageCounter();
    private final PermGenMemUsageCounter permGenMemUsageCounter = new PermGenMemUsageCounter();
    private final HeapMemUsageCounter heapMemUsageCounter = new HeapMemUsageCounter();
    private final IntegralCountStat activeArteStat = new IntegralCountStat(new MetricParameters(null, -1, Instance.WRITE_BASIC_STATS_PERIOD_MILLIS));
    private final ArteSessionCounter overallActiveSessionsCounter = new ArteSessionCounter();

    public InstanceMonitor(final Instance instance) {
        super(null, instance.getTrace(), EEventSource.INSTANCE.getValue());
        this.instance = instance;
        tranMetric2stat.put(EMetric.PROFILING_PERCENT_CPU, null);
        tranMetric2stat.put(EMetric.PROFILING_PERCENT_DB, null);
        tranMetric2stat.put(EMetric.PROFILING_PERCENT_EXT, null);
        this.processorThread = new ProcessorThread("Instance #" + instance.getId() + " -Monitor-Processor-Thread");
        arteSessCntMetricDescription = new MetricDescription(EMetric.INSTANCE_ARTE_SESSION_CNT, SensorCoordinates.forInstance(instance.getId()));
        instCpuUsageMetricDescription = new MetricDescription(EMetric.INSTANCE_CPU_USAGE, SensorCoordinates.forInstance(instance.getId()));
        codeCacheMemUsageMetricDescription = new MetricDescription(EMetric.INSTANCE_MEMORY_CODE_CACHE, SensorCoordinates.forInstance(instance.getId()));
        permGenMetricDescription = new MetricDescription(EMetric.INSTANCE_MEMORY_PERM_GEN, SensorCoordinates.forInstance(instance.getId()));
        heapMetricDescpription = new MetricDescription(EMetric.INSTANCE_MEMORY_HEAP, SensorCoordinates.forInstance(instance.getId()));
        if (CpuUsageCounter.isSupported()) {
            cpuUsageCounter = new CpuUsageCounter();
        } else {
            cpuUsageCounter = null;
        }
    }

    public void start() {
        processorThread.start();
        activeArteStat.start(new RegisteredItem<>(System.currentTimeMillis(), 0));
        service2counter.put(OVERALL_INSTANCE, overallActiveSessionsCounter);
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
            synchronized (profileSem) {
                synchronized (tranTimeSettingsSem) {
                    toWrite.addAll(flushTranTimeStat(flushTimeMillis));
                }
                synchronized (sectionsSettingsSem) {
                    toWrite.addAll(flushSectionsStat(flushTimeMillis));
                }
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
            synchronized (flushCompletedSem) {
                flushCompletedSem.wait(timeOutMillis);
            }
            synchronized (processSem) {
                flushWaiters--;
            }
        }
    }

    public void checkHungUnits() throws SQLException {
        final Collection<MonitoringDbQueries.MonitoredUnitStatus> unitStatuses = getDbQueries().getMonitoredUnitStatuses(instance.getId());
        final List<MetricRecord> recordsToWrite = new ArrayList<>();
        final long curTimeMillis = System.currentTimeMillis();
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
        for (MonitoringDbQueries.MonitoredUnitStatus status : unitId2status.values()) {
            if (status.getCheckTime() == null) {
                continue;
            }
            final MetricDescription desc = new MetricDescription(EMetric.UNIT_HANG, SensorCoordinates.forUnit(status.getUnitId()));
            if (curTimeMillis - status.getCheckTime().getTime() >= Unit.DB_I_AM_ALIVE_TIMEOUT_MILLIS) {
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
        increaseArteSessionsCount(request, 1);
    }

    private void increaseArteSessionsCount(final IArteRequest request, final int diff) {
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
            activeArteStat.append(new RegisteredItem<>(curTimeMillis, overallActiveSessionsCounter.count));
        }
    }

    public void writeBasicStats() {
        double averageActiveArteCount = -1;
        synchronized (arteSessionsSem) {
            final IStatValue val = activeArteStat.flushAndGetLastValue();
            if (val != null) {
                averageActiveArteCount = val.getAvg();
            }
        }
        int arteInstCountVal;
        synchronized (arteInstCountSem) {
            arteInstCountVal = arteInstCount;
        }
        if (averageActiveArteCount >= 0 && arteInstCountVal >= 0) {
            getDbQueries().writeBasicInstanceStats(instance.getId(), arteInstCountVal, averageActiveArteCount);
        }
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

    public void arteSessionFinished(final IArteRequest request) {
        increaseArteSessionsCount(request, -1);
    }

    /**
     * @NotThreadSafe should be called only from instance thread
     */
    public void maintenance() {
        if (cpuUsageCounter != null) {
            cpuUsageCounter.tic();
        }
        codeCacheMemUsageCounter.tic();
        heapMemUsageCounter.tic();
        permGenMemUsageCounter.tic();
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
        updateCountProfileStatistic();
        readArteInstanceSettings();
        readResourceUsageCountersSettings();
    }

    private void readResourceUsageCountersSettings() throws SQLException {
        if (cpuUsageCounter != null) {
            cpuUsageCounter.setParameters(getDbQueries().readMetricParameters(instCpuUsageMetricDescription));
        }
        codeCacheMemUsageCounter.setParameters(getDbQueries().readMetricParameters(codeCacheMemUsageMetricDescription));
        permGenMemUsageCounter.setParameters(getDbQueries().readMetricParameters(permGenMetricDescription));
        heapMemUsageCounter.setParameters(getDbQueries().readMetricParameters(heapMetricDescpription));
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

    private void updateCountProfileStatistic() {
        boolean newCountProfileStatistic = false;
        for (Map.Entry<EMetric, TranTimeStat> entry : tranMetric2stat.entrySet()) {
            if (entry.getValue() != null && entry.getValue().getState() == EMetricState.ACTIVE) {
                newCountProfileStatistic = true;
                break;
            }
        }
        countProfileStatistic = newCountProfileStatistic;
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
        for (EMetric metric : tranMetric2stat.keySet()) {
            newParams.put(metric, getDbQueries().readMetricParameters(new MetricDescription(metric.getKindName(), metric.getMetricType(), SensorCoordinates.forInstance(instance.getId()))));
        }
        final long curTimeMillis = System.currentTimeMillis();
        MetricParameters params;
        synchronized (tranTimeSettingsSem) {
            for (Map.Entry<EMetric, TranTimeStat> entry : tranMetric2stat.entrySet()) {
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
        return countProfileStatistic;
    }

    public Collection<String> getMonitoredSections() {
        return monitoredSections;
    }

    public void registerStatistic(final TransactionTimeStatistic timeStat, final Collection<ProfileStatisticEntry> sectionsStat) {
        synchronized (profileSem) {
            final long curTimeMillis = System.currentTimeMillis();
            if (timeStat != null) {
                addTranTimeStat(timeStat, curTimeMillis);
            }
            if (sectionsStat != null && !sectionsStat.isEmpty()) {
                addSectionsStat(sectionsStat, curTimeMillis);
            }
        }
    }

    /**
     * should be called under synchronization on profileSem
     */
    private void addTranTimeStat(final TransactionTimeStatistic timeStat, final long timeMillis) {
        tranTimeStatQueue.add(new RegisteredItem<>(timeMillis, timeStat));
    }

    /**
     * should be called under synchronization on profileSem
     */
    private void addSectionsStat(final Collection<ProfileStatisticEntry> sectionsStat, final long timeMillis) {
        if (sectionsStat != null && !sectionsStat.isEmpty()) {
            for (final ProfileStatisticEntry entry : sectionsStat) {
                this.sectionsStatQueue.add(new RegisteredItem<>(timeMillis, entry));
            }
        }
    }

    /**
     * should be called under synchronization on profileSem and
     * sectionsSettingsSem
     */
    private void packSectionsStat() {
        final Map<String, List<RegisteredItem<ProfileStatisticEntry>>> profileEntries = new HashMap<>();
        List<RegisteredItem<ProfileStatisticEntry>> list;
        for (final RegisteredItem<ProfileStatisticEntry> item : sectionsStatQueue) {
            list = profileEntries.get(item.val.getSectionId());
            if (list == null) {
                list = new ArrayList<>();
                profileEntries.put(item.val.getSectionId(), list);
            }
            list.add(item);
        }
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
        sectionsStatQueue.clear();
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
     * should be called under synchronization on profileSem and
     * tranTimeSettingsSem
     */
    private List<MetricRecord> flushTranTimeStat(final long flushTimeMillis) {
        packTranTimeStat();
        final List<MetricRecord> toWrite = new ArrayList<>();
        for (final Map.Entry<EMetric, TranTimeStat> entry : tranMetric2stat.entrySet()) {
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

    /**
     * should be called under synchronization on profileSem and
     * tranTimeSettingsSem
     */
    private void packTranTimeStat() {
        for (final TranTimeStat stat : tranMetric2stat.values()) {
            if (stat != null) {
                stat.appendAll(tranTimeStatQueue);
            }
        }
        tranTimeStatQueue.clear();
    }

    /**
     * Should be called under synchronization on profileSem.
     */
    private List<MetricRecord> flushSectionsStat(final long flushTimeMillis) {
        packSectionsStat();
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
        sectionsStatQueue.clear();
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

    private static class TranTimeStat extends AbstractStat<TransactionTimeStatistic> {

        private long curTotalNanos = 0;
        private long curNanos = 0;

        public TranTimeStat(final MetricParameters parameters, final long curPeriodStartMillis) {
            super(parameters, curPeriodStartMillis);
        }

        @Override
        public StatValue closePeriod() {
            double val = curTotalNanos > 0 ? 100. * curNanos / curTotalNanos : 0.;
            return new StatValue(val, val, val);
        }

        @Override
        public void newPeriodStarted() {
            curNanos = 0;
            curTotalNanos = 0;
        }

        @Override
        protected void appendInternal(final RegisteredItem<TransactionTimeStatistic> entry) {
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
            curTotalNanos += entry.val.getCpuNanos() + entry.val.getDbNanos() + entry.val.getExtNanos();
            curNanos += val;
        }
    }

    private static abstract class ResourceUsageCounter {

        private IntegralCountStat stat;
        private boolean zeroStartElementAdded = false;

        public void tic() {
            int currentValue = getCurrentValue();
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
    private static class CpuUsageCounter extends ResourceUsageCounter {

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

    private static class CodeCacheMemUsageCounter extends ResourceUsageCounter {

        @Override
        protected int getCurrentValue() {
            final List<MemoryPoolMXBean> memPoolMxBeans = ManagementFactory.getMemoryPoolMXBeans();
            for (MemoryPoolMXBean poolBean : memPoolMxBeans) {
                final String lowerName = poolBean.getName().toLowerCase();
                if (poolBean.isValid()) {
                    if (lowerName.contains("code") && lowerName.contains("cache")) {
                        return getMemoryUsagePercent(poolBean.getUsage());
                    }
                }
            }
            return -1;
        }
    }

    private static class PermGenMemUsageCounter extends ResourceUsageCounter {

        @Override
        protected int getCurrentValue() {
            final List<MemoryPoolMXBean> memPoolMxBeans = ManagementFactory.getMemoryPoolMXBeans();
            for (MemoryPoolMXBean poolBean : memPoolMxBeans) {
                final String lowerName = poolBean.getName().toLowerCase();
                if (poolBean.isValid()) {
                    if (lowerName.contains("perm") && lowerName.contains("gen")) {
                        return getMemoryUsagePercent(poolBean.getUsage());
                    }
                }
            }
            return -1;
        }
    }

    private static class HeapMemUsageCounter extends ResourceUsageCounter {

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

    private static interface IUriCalculator {

        public String getUri(final MetricParameters parameters);
    }
}
