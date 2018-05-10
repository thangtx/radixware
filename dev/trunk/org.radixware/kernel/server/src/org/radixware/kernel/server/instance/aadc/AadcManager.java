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
package org.radixware.kernel.server.instance.aadc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import javax.cache.processor.EntryProcessor;
import javax.cache.processor.MutableEntry;
import javax.cache.Cache.Entry;
import javax.cache.processor.EntryProcessorException;
import org.apache.commons.logging.LogFactory;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.trace.IRadixTrace;
import org.radixware.kernel.common.types.Pid;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteException;
import org.apache.ignite.IgniteLogger;
import org.apache.ignite.IgniteSystemProperties;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicWriteOrderMode;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
// import org.apache.ignite.cache.eviction.lru.LruEvictionPolicy; // Ignite 2.0+
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.configuration.CacheConfiguration;
// import org.apache.ignite.configuration.DataStorageConfiguration; // Ignite 2.0+
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.lang.IgniteBiPredicate;
import org.apache.ignite.spi.IgniteSpiException;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EAadcMember;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.ArrStr;

import org.radixware.kernel.common.utils.CompositeInetSocketAddress;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.common.utils.ValueFormatter;
import org.radixware.kernel.server.aio.AadcAffinity;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.trace.ServerThreadLog;
import org.radixware.kernel.starter.Starter;
import org.radixware.kernel.starter.StarterArguments;

public class AadcManager {

    public static final String AADC_MEMBER_ATTRIBUTE_NAME = "rdx.aadc.member.id";
    public static final String INSTANCE_ID_ATTRIBUTE_NAME = "rdx.aadc.system.instance.id";
    //
    private static final long RDX_AADC_FAILURE_DETECTION_TIMEOUT_MS = SystemPropUtils.getLongSystemProp("rdx.aadc.failure.detection.timeout.ms", -1);
    private static final int RDX_AADC_MAX_PURGED_FINISHED_LOCKS_PER_ITER = SystemPropUtils.getIntSystemProp("rdx.aadc.max.purged.finished.locks.per.iter", 100000);
    private static final boolean RDX_AADC_MANAGER_DEBUG = SystemPropUtils.getBooleanSystemProp("rdx.aadc.manager.debug", false);
//    private static final long RDX_AADC_IGNITE_OFFHEAP_SIZE = SystemPropUtils.getLongSystemProp("rdx.aadc.ignite.offheap.size.mb", 256) * 1024 * 1024;  // Ignite 2.0+

    final public static byte LOCK_TYPE_TRAN = 1;
    final public static byte LOCK_TYPE_SESS = 2;
    final public static byte LOCK_TYPE_CUSTOM = 10;

    public static final long DEFAULT_AFFINITY_DURATION_MARKER = -2l;

    final String LOCK_CACHE_NAME = "AADC_LOCK";
    final String MEMBER_CACHE_NAME = "AADC_MEMBER";
    final String UNIT_CACHE_NAME = "AADC_UNIT";
    final String INSTANCE_CACHE_NAME = "AADC_INSTANCE";
    final String SAP_CACHE_NAME = "AADC_SAP";
    final String AFFINITY_CACHE_NAME = "AADC_AFFINITY";

    final private Instance instance;
    final private IRadixTrace trace;

    final private int memberId;
    final private AadcHelper helper;

    private Ignite grid;
    private IgniteCache<LockCacheKey, LockCacheVal> lockCache;
    private IgniteCache<CheckedObjCacheKey, CheckedObjCacheVal> memberCache;
    private IgniteCache<CheckedObjCacheKey, CheckedObjCacheVal> unitCache;
    private IgniteCache<CheckedObjCacheKey, CheckedObjCacheVal> instanceCache;
    private IgniteCache<CheckedObjCacheKey, CheckedObjCacheVal> sapCache;
    private IgniteCache<AffinityCacheKey, AffinityCacheVal> affinityCache;
    private volatile long defaultAffinityTimeoutMillis;
    private final MaintenanceThread maintenanceThread;
    private final AadcScnUpdateSchedulerThread scnUpdateSchedulerThread;

    private static final ThreadLocal<Random> RANDOM_THREAD_LOCAL_CACHE = new ThreadLocal<Random>() {

        @Override
        protected Random initialValue() {
            return new Random();
        }

    };

    public AadcManager(Instance instance_) throws SQLException {
        instance = instance_;
        trace = instance.getTrace();

        Integer mid = instance.getAadcInstMemberId();
        if (mid == null) {
            memberId = -1;
            helper = null;
            maintenanceThread = null;
            scnUpdateSchedulerThread = null;
            if (Starter.getAadcMemberId() > 0) {
                trace.put(EEventSeverity.ERROR, "AADC instance member ID is undefined, but starter has " + StarterArguments.AADC_MEMBER_ID + "=" + Starter.getAadcMemberId() + ", application upgrade may work unexpectedly", EEventSource.INSTANCE);
            }
            return;
        }
        memberId = mid;
        if (instance.getAadcSysMemberId() == null || memberId != instance.getAadcSysMemberId()) {
            trace.put(EEventSeverity.ERROR, "AADC instance member ID <> system member ID", EEventSource.INSTANCE);
        }
        if (Starter.getAadcMemberId() > 0 && Starter.getAadcMemberId() != memberId) {
            throw new IllegalStateException("AADC instance member id (" + memberId + ") differs from AADC member id specified in starter arguments (" + Starter.getAadcMemberId() + ")");
        }
        trace.put(EEventSeverity.DEBUG, "AADC manager initialization...", EEventSource.INSTANCE);
        initDatagrid();
        helper = new AadcHelper(this);
        maintenanceThread = new MaintenanceThread(this);
        maintenanceThread.start();
        scnUpdateSchedulerThread = new AadcScnUpdateSchedulerThread(this);
        scnUpdateSchedulerThread.start();
        trace.put(EEventSeverity.EVENT, "AADC manager initialized", EEventSource.INSTANCE);
    }

    public int getMemberId() {
        return memberId;
    }

    public int getOtherMemberId() {
        if (isInAadc()) {
            return memberId == 1 ? 2 : 1;
        }
        return 0;
    }

    public boolean isInAadc() {
        return memberId != -1;
    }

//////// INIT
    private synchronized void initDatagrid() throws SQLException {
        initGrid();
        initCaches();
    }

    private void initGrid() throws IgniteException, IgniteSpiException, SQLException {
        CompositeInetSocketAddress myAddress = ValueFormatter.parseCompositeInetSocketAddress(instance.getAadcDgAddress());
        Collection<String> gridNodeAddresses = new ArrayList<>();
        try (PreparedStatement qryAddrs = instance.getDbConnection().prepareStatement("select aadcDgAddress from RDX_Instance")) {
            try (ResultSet rs = qryAddrs.executeQuery()) {
                while (rs.next()) {
                    if (rs.getString(1) == null) {
                        continue;
                    }
                    gridNodeAddresses.add(rs.getString(1));
                }
            }
        }

        System.setProperty(IgniteSystemProperties.IGNITE_UPDATE_NOTIFIER, "false");
        System.setProperty(IgniteSystemProperties.IGNITE_QUIET, "false");
        System.setProperty(IgniteSystemProperties.IGNITE_CONSOLE_APPENDER, "false");
        System.setProperty(IgniteSystemProperties.IGNITE_NO_ASCII, "true");
        System.setProperty(IgniteSystemProperties.IGNITE_PERFORMANCE_SUGGESTIONS_DISABLED, "true");
        System.setProperty(IgniteSystemProperties.IGNITE_CACHE_KEY_VALIDATION_DISABLED, "true");
        System.setProperty(IgniteSystemProperties.IGNITE_HOME, System.getProperty("user.dir"));
        System.setProperty(IgniteSystemProperties.IGNITE_NO_SHUTDOWN_HOOK, "true");

        IgniteConfiguration cfg = new IgniteConfiguration();

        TcpDiscoverySpi spi = new TcpDiscoverySpi();
        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
        ipFinder.setAddresses(gridNodeAddresses);
        spi.setIpFinder(ipFinder);
        spi.setLocalAddress(myAddress.getRemoteAddress().getHostName());
        spi.setLocalPort(myAddress.getRemoteAddress().getPort());
        TcpCommunicationSpi commSpi = new TcpCommunicationSpi();
        commSpi.setMessageQueueLimit(1000);
        cfg.setCommunicationSpi(commSpi);
        cfg.setDiscoverySpi(spi);

        cfg.setClientMode(false);
        cfg.setPeerClassLoadingEnabled(false);

        final RadixIgniteLogger logger = new RadixIgniteLogger("");

        logger.setMinSeverity("org.apache.ignite.internal.managers.collision.GridCollisionManager", EEventSeverity.ERROR);
        logger.setMinSeverity("org.apache.ignite.spi.swapspace.noop.NoopSwapSpaceSpi", EEventSeverity.ERROR);
        logger.setMinSeverity("org.apache.ignite.spi.checkpoint.noop.NoopCheckpointSpi", EEventSeverity.ERROR);

        cfg.setGridLogger(logger);

        if (RDX_AADC_FAILURE_DETECTION_TIMEOUT_MS != -1) {
            cfg.setFailureDetectionTimeout(RDX_AADC_FAILURE_DETECTION_TIMEOUT_MS);
        }

        final Map<String, String> userAttrs = new HashMap<>();
        userAttrs.put(AADC_MEMBER_ATTRIBUTE_NAME, String.valueOf(memberId));
        userAttrs.put(INSTANCE_ID_ATTRIBUTE_NAME, String.valueOf(instance.getId()));
        cfg.setUserAttributes(userAttrs);

//        // Ignite 2.0+ off-heap storage cfg
//        DataStorageConfiguration storageCfg = new DataStorageConfiguration();
//        storageCfg.getDefaultDataRegionConfiguration().setMaxSize(RDX_AADC_IGNITE_OFFHEAP_SIZE);
//        cfg.setDataStorageConfiguration(storageCfg);
        grid = Ignition.start(cfg);
    }

    // Ignite v1.9
    private <K, V> IgniteCache<K, V> createCache(CacheConfiguration<K, V> cfg, String name, CacheWriteSynchronizationMode syncMode) {
        syncMode = syncMode != null ? syncMode : CacheWriteSynchronizationMode.PRIMARY_SYNC;
        cfg.setName(name)
                .setCacheMode(CacheMode.REPLICATED)
                .setAtomicityMode(CacheAtomicityMode.ATOMIC)
                .setAtomicWriteOrderMode(CacheAtomicWriteOrderMode.PRIMARY)
                .setWriteSynchronizationMode(syncMode);
        return grid.getOrCreateCache(cfg);
    }

//    // Ignite v2.0+
//    private <K, V> IgniteCache<K, V> createCache(CacheConfiguration<K, V> cfg, String name, CacheWriteSynchronizationMode syncMode) {
//        syncMode = syncMode != null ? syncMode : CacheWriteSynchronizationMode.PRIMARY_SYNC;
//        cfg.setName(name)
//                .setCacheMode(CacheMode.REPLICATED)
//                .setAtomicityMode(CacheAtomicityMode.ATOMIC)
//                .setWriteSynchronizationMode(syncMode);
//                .setOnheapCacheEnabled(true) // do it really affects performance?
//                .setEvictionPolicy(new LruEvictionPolicy(32768));
//        return grid.getOrCreateCache(cfg);
//    }
    private void initCaches() {
        lockCache = createCache(new CacheConfiguration<LockCacheKey, LockCacheVal>(), LOCK_CACHE_NAME, null);
        memberCache = createCache(new CacheConfiguration<CheckedObjCacheKey, CheckedObjCacheVal>(), MEMBER_CACHE_NAME, null);
        unitCache = createCache(new CacheConfiguration<CheckedObjCacheKey, CheckedObjCacheVal>(), UNIT_CACHE_NAME, null);
        instanceCache = createCache(new CacheConfiguration<CheckedObjCacheKey, CheckedObjCacheVal>(), INSTANCE_CACHE_NAME, null);
        sapCache = createCache(new CacheConfiguration<CheckedObjCacheKey, CheckedObjCacheVal>(), SAP_CACHE_NAME, null);
        affinityCache = createCache(new CacheConfiguration<AffinityCacheKey, AffinityCacheVal>(), AFFINITY_CACHE_NAME, CacheWriteSynchronizationMode.FULL_SYNC);
    }

    public Ignite getGrid() {
        return grid;
    }

    public void stop() {
        if (grid == null) {
            return;
        }
        maintenanceThread.requestStop();
        grid.close();
        grid = null;
    }

    public void rereadOptions() {
        defaultAffinityTimeoutMillis = instance.getAadcAffinityTimeoutSec() * 1000L;
    }

    //////// LOCK API
    static class AcquirerLockProcessor implements EntryProcessor<LockCacheKey, LockCacheVal, Long> {

        final int myMemberId;
        final long receivedScn;
        final String dbTranId;

        public AcquirerLockProcessor(int myMemberId, long receivedScn, String dbTranId) {
            this.myMemberId = myMemberId;
            this.receivedScn = receivedScn;
            this.dbTranId = dbTranId;
        }

        @Override
        public Long process(MutableEntry<LockCacheKey, LockCacheVal> entry, Object args[]) {
            LockCacheVal v = entry.getValue();
            if (v != null && v.getMemberId() != myMemberId) {
                if (!v.capture(receivedScn, myMemberId)) {
                    return null;
                }
            } else if (v == null) {
                v = new LockCacheVal(myMemberId);
            }
            long lockId = v.addLock(System.currentTimeMillis(), dbTranId);
            entry.setValue(v);
            return lockId;
        }
    }

    public Long acquireLock(Pid objPid, byte lockType, long receivedScn, String ownerDbTranId) {
        return lockCache.invoke(new LockCacheKey(objPid, lockType),
                new AcquirerLockProcessor(memberId, receivedScn, ownerDbTranId));
    }

    static class CommitLockProcessor implements EntryProcessor<LockCacheKey, LockCacheVal, Void> {

        final long lockId;
        final long curScn;

        public CommitLockProcessor(long lockId, long curScn) {
            this.lockId = lockId;
            this.curScn = curScn;
        }

        @Override
        public Void process(MutableEntry<LockCacheKey, LockCacheVal> entry, Object args[]) throws EntryProcessorException {
            LockCacheVal v = entry.getValue();
            if (v != null) {
                v.commitLock(lockId, curScn);
                entry.setValue(v);
            }
            return null;
        }
    }

    public void commitLock(Pid objPid, byte lockType, long lockId, long curScn) {
        lockCache.invoke(new LockCacheKey(objPid, lockType),
                new CommitLockProcessor(lockId, curScn));
    }

    static class RollbackProcessor implements EntryProcessor<LockCacheKey, LockCacheVal, Void> {

        final long lockId;

        public RollbackProcessor(long lockId) {
            this.lockId = lockId;
        }

        @Override
        public Void process(MutableEntry<LockCacheKey, LockCacheVal> entry, Object args[]) {
            LockCacheVal v = entry.getValue();
            if (v != null) {
                v.rollbackLock(lockId);
                if (!v.isFinished()) {
                    entry.setValue(v);
                } else {
                    entry.remove();
                }
            }
            return null;
        }
    }

    public void rollbackLock(Pid objPid, byte lockType, long lockId) {
        lockCache.invoke(new LockCacheKey(objPid, lockType),
                new RollbackProcessor(lockId));
    }

    public void putLockPayload(Pid objPid, byte lockType, Map<String, String> payload) {
        if (memberId < 1) {
            return;
        }
        LockCacheKey k = new LockCacheKey(objPid, lockType);
        LockCacheVal v = lockCache.get(k);
        if (v != null) {
            v.putPayload(payload);
            lockCache.put(k, v);
        }
    }

    public boolean readLockPayload(Pid objPid, byte lockType, Map<String, String> payload) {
        if (memberId < 1) {
            return false;
        }
        LockCacheVal v = lockCache.get(new LockCacheKey(objPid, lockType));
        if (v == null
                || v.getMemberId() == memberId) { //from another member only
            return false;
        }
        Map<String, String> pl = v.getPayload();
        if (pl != null) {
            payload.putAll(pl);
        }
        return true;
    }

    //////// AFFINITY API
    public AadcMemberBinding getMemberForObject(AadcAffinity affinity) {
        if (memberId < 1) {
            return null;
        }
        return affinityCache.invoke(new AffinityCacheKey(affinity.getAffinityKey()), new AffinityEntryProcessor(), 0, getEffectiveDurationMs(affinity), false);
    }

    public AadcMemberBinding registerMemberForObject(AadcAffinity affinity, int memberId) {
        if (memberId < 1) {
            return null;
        }
        return affinityCache.invoke(new AffinityCacheKey(affinity.getAffinityKey()), new AffinityEntryProcessor(), memberId, getEffectiveDurationMs(affinity), true);
    }

    private long getEffectiveDurationMs(final AadcAffinity affinity) {
        if (affinity.getExpirationDuration() == null || affinity.getExpirationDuration() == DEFAULT_AFFINITY_DURATION_MARKER) {
            return defaultAffinityTimeoutMillis;
        }
        return affinity.getExpirationDuration();
    }

    private static class AffinityEntryProcessor implements EntryProcessor<AffinityCacheKey, AffinityCacheVal, AadcMemberBinding> {

        @Override
        public AadcMemberBinding process(MutableEntry<AffinityCacheKey, AffinityCacheVal> me, Object... os) throws EntryProcessorException {
            Integer memberId = (Integer) os[0];
            Long durationMillis = (Long) os[1];
            Boolean confirmed = (Boolean) os[2];
            boolean wasTimedOut = me.getValue() != null && me.getValue().getExpirationTime() < System.currentTimeMillis();
            if (me.getValue() == null || wasTimedOut || me.getValue().getMemberId() == memberId || confirmed) {
                if (memberId == 0) {
                    memberId = RANDOM_THREAD_LOCAL_CACHE.get().nextBoolean() ? 2 : 1;
                }
                boolean changedBeforeExpiration = false;
                boolean newConfirmed = confirmed;
                if (me.getValue() != null && !wasTimedOut) {
                    boolean wasConfirmed = me.getValue().isConfirmed();
                    if (me.getValue().getMemberId() == memberId) {
                        newConfirmed |= wasConfirmed;
                    } else if (wasConfirmed) {
                        changedBeforeExpiration = true;
                    }
                }
                me.setValue(new AffinityCacheVal(memberId, durationMillis, newConfirmed));
                return new AadcMemberBinding(memberId, false, changedBeforeExpiration);
            } else {
                return new AadcMemberBinding(me.getValue().getMemberId(), me.getValue().isConfirmed(), false);
            }
        }

    }

    //////// MONITORING API
    public void unitIsAlive(long unitId, long time) {
        if (unitCache == null) {
            return;
        }
        unitCache.put(new CheckedObjCacheKey(unitId), new CheckedObjCacheVal(time));
    }

    public Timestamp getUnitSelfCheckTime(long unitId) {
        if (unitCache == null) {
            return null;
        }
        CheckedObjCacheVal v = unitCache.get(new CheckedObjCacheKey(unitId));
        if (v == null) {
            return null;
        }
        return new Timestamp(v.getLastCheckTime());
    }

    public void instanceIsAlive(long instanceId, long time) {
        if (instanceCache == null) {
            return;
        }
        instanceCache.put(new CheckedObjCacheKey(instanceId), new CheckedObjCacheVal(time));
    }

    public Timestamp getInstanceSelfCheckTime(long instanceId) {
        if (instanceCache == null) {
            return null;
        }
        CheckedObjCacheVal v = instanceCache.get(new CheckedObjCacheKey(instanceId));
        if (v == null) {
            return null;
        }
        return new Timestamp(v.getLastCheckTime());
    }

    public void sapIsAlive(long sapId, long time) {
        if (sapCache == null) {
            return;
        }
        sapCache.put(new CheckedObjCacheKey(sapId), new CheckedObjCacheVal(time));
    }

    public Timestamp getSapSelfCheckTime(long sapId) {
        if (sapCache == null) {
            return null;
        }
        CheckedObjCacheVal v = sapCache.get(new CheckedObjCacheKey(sapId));
        if (v == null) {
            return null;
        }
        return new Timestamp(v.getLastCheckTime());
    }

    public int getCurrentLag() throws SQLException {
        return instance.getDbQueries().getAadcLag(memberId);
    }

    public Map<LockCacheKey, LockCacheVal> getAllLocks() {
        if (lockCache == null) {
            return null;
        }
        Map<LockCacheKey, LockCacheVal> res = new HashMap();
        try (QueryCursor<Entry<LockCacheKey, LockCacheVal>> cursor = lockCache.query(new ScanQuery<LockCacheKey, LockCacheVal>())) {
            for (Entry<LockCacheKey, LockCacheVal> entry : cursor) {
                res.put(entry.getKey(), entry.getValue());
            }
        }
        return res;
    }

    public void deleteLock(LockCacheKey key) {
        lockCache.remove(key);
    }

    public void deleteAllLocks() {
        lockCache.removeAll();
    }

//////// MAINTENANCE
    private long lastMaintenanceMillis = 0;

    public void maintenance() throws SQLException {
        if (memberId < 1) {
            return;
        }
        final long curMillis = System.currentTimeMillis();
        if (curMillis - lastMaintenanceMillis < 1000) {
            return;
        }
        lastMaintenanceMillis = curMillis;

        //Heartbeat
        instance.getDbQueries().updateAadcState();

        //Self-check report via IM
        memberCache.put(new CheckedObjCacheKey(memberId), new CheckedObjCacheVal(curMillis));

    }

    static class LockFilter implements IgniteBiPredicate<LockCacheKey, LockCacheVal> {

        final private int memberId;
        final private boolean committed;
        final private long upperScn;
        final private long upperAge;
        final Set<String> tables;

        LockFilter(int memberId, boolean committed, long upperScn, long upperAge, Set<String> tables) {
            this.memberId = memberId;
            this.committed = committed;
            this.upperScn = upperScn;
            this.upperAge = upperAge;
            this.tables = tables;
        }

        @Override
        public boolean apply(LockCacheKey k, LockCacheVal v) {
            if (tables != null) {
                Pid pid = Pid.fromStr(k.getObjPid());
                if (!tables.contains(pid.getTableId().toString())) {
                    return false;
                }
            }
            if (v.getMemberId() != memberId) {
                return false;
            }
            if (committed) {
                return !v.hasUncommitedLocks() && v.getMaxScn() <= upperScn;
            } else {
                return v.hasOldUncommittedLocks(upperAge);
            }
        }
    }

    static class PurgeUncommitedLocksProcessor implements EntryProcessor<LockCacheKey, LockCacheVal, Boolean> {

        @Override
        public Boolean process(MutableEntry<LockCacheKey, LockCacheVal> entry, Object args[]) {
            LockCacheVal v = entry.getValue();
            boolean purged = false;
            Set<String> curDbTrans = (Set<String>) args[0];
            Set<String> curDbSessions = (Set<String>) args[1];
            if (v != null) {
                switch (entry.getKey().getlockType()) {
                    case LOCK_TYPE_TRAN:
                        purged = v.purgeInvalidLocks(curDbTrans);
                        break;
                    case LOCK_TYPE_SESS:
                        purged = v.purgeInvalidLocks(curDbSessions);
                        break;
                    default:
                        purged = v.purgeInvalidLocks(null);
                        break;
                }
                if (!v.isFinished()) {
                    entry.setValue(v);
                } else {
                    entry.remove();
                }
            }
            return purged;
        }
    }

    final Comparator<LockCacheKey> lockCacheKeyComparator = new Comparator<LockCacheKey>() {
        @Override
        public int compare(LockCacheKey o1, LockCacheKey o2) {
            if (o1.getlockType() != o2.getlockType()) {
                return Byte.compare(o1.getlockType(), o2.getlockType());
            }
            return o1.getObjPid().compareTo(o2.getObjPid());
        }
    };

    private int[] purgePartnerFinishedLocks() throws SQLException {
        int totalCount = 0;
        final int otherMemberId = memberId == 1 ? 2 : 1;
        final long recvScn = helper.getReceivedScn(instance.getDbConnection());
        LockFilter filter = new LockFilter(otherMemberId, true, recvScn, 0, null);
        final Set<LockCacheKey> toRemove = new TreeSet<>(lockCacheKeyComparator);
        try (QueryCursor<Entry<LockCacheKey, LockCacheVal>> cursor = lockCache.query(new ScanQuery<>(filter))) {
            for (Entry<LockCacheKey, LockCacheVal> entry : cursor) {
                if (toRemove.size() < RDX_AADC_MAX_PURGED_FINISHED_LOCKS_PER_ITER) {
                    toRemove.add(entry.getKey());
                }
                totalCount++;
            }
        }
        lockCache.removeAll(toRemove);
        return new int[]{toRemove.size(), totalCount - toRemove.size()};
    }

    private void purgeMyUncommittedHungLocks(final long curMillis) throws SQLException {
        final int DELAY = 60;
        final Set<String> curDbTrans = instance.getDbQueries().readCurDbTrans();
        final Set<String> curDbSessions = instance.getDbQueries().readCurDbSessions();
        LockFilter filter = new LockFilter(memberId, false, 0, curMillis - DELAY * 1000, null);
        boolean purged = false;
        try (QueryCursor<Entry<LockCacheKey, LockCacheVal>> cursor = lockCache.query(new ScanQuery<>(filter))) {
            for (Entry<LockCacheKey, LockCacheVal> entry : cursor) {
                purged |= lockCache.invoke(entry.getKey(), new PurgeUncommitedLocksProcessor(), curDbTrans, curDbSessions);
            }
        }
        if (purged) {
            trace.put(EEventSeverity.WARNING, "AADC member #" + memberId + " has lost locks; the locks purged", EEventSource.INSTANCE);
        }
    }

    private void purgeHungPartnerUncommittedLocks(long curMillis) throws SQLException {
        final int DELAY = 2 * 60;
        final int otherMemberId = memberId == 1 ? 2 : 1;
        CheckedObjCacheVal memb = memberCache.get(new CheckedObjCacheKey(otherMemberId));
        if (memb != null && memb.getLastCheckTime() > curMillis - DELAY * 1000) {
            return;
        }
        final Set<String> curDbTrans = null; //can't check DB trx; purge all hung locks
        final Set<String> curDbSessions = null; //can't check DB sessions; purge all hung locks
        LockFilter filter = new LockFilter(otherMemberId, false, 0, Long.MAX_VALUE, null);
        boolean purged = false;
        try (QueryCursor<Entry<LockCacheKey, LockCacheVal>> cursor = lockCache.query(new ScanQuery<>(filter))) {
            for (Entry<LockCacheKey, LockCacheVal> entry : cursor) {
                purged |= lockCache.invoke(entry.getKey(), new PurgeUncommitedLocksProcessor(), curDbTrans, curDbSessions);
            }
        }
        if (purged) {
            trace.put(EEventSeverity.WARNING, "AADC member #" + otherMemberId + " is inactive; uncommited locks purged", EEventSource.INSTANCE);
        }
    }

    private void purgeHungPartnerCommittedLocks(long curMillis) {
        final int otherMemberId = memberId == 1 ? 2 : 1;
        CheckedObjCacheVal memb = memberCache.get(new CheckedObjCacheKey(otherMemberId));
        if (memb != null && memb.getLastCheckTime() > curMillis - instance.getAadcCommitedLockExp() * 1000) {
            return;
        }
        final String tablesArr = instance.getAadcUnlockTables();
        if (tablesArr == null) {
            return;
        }
        Map<String, String> tables = new HashMap<>();
        for (String t : ((ArrStr) ValAsStr.fromStr(tablesArr, EValType.ARR_STR))) {
            int p = t.indexOf("/");
            final String guid, name;
            if (p > 0) {
                guid = t.substring(0, p);
                name = t.substring(p + 1);
            } else {
                guid = t;
                name = "?";
            }
            tables.put(guid, name);
        }
        LockFilter filter = new LockFilter(otherMemberId, true, Long.MAX_VALUE, 0, tables.keySet());
        try (QueryCursor<Entry<LockCacheKey, LockCacheVal>> cursor = lockCache.query(new ScanQuery<>(filter))) {
            for (Entry<LockCacheKey, LockCacheVal> entry : cursor) {
                Pid pid = Pid.fromStr(entry.getKey().getObjPid());
                lockCache.remove(entry.getKey());
                trace.put(EEventSeverity.ERROR, "AADC member #" + otherMemberId + " is inactive; commmitted lock purged for " + tables.get(pid.getTableId().toString()) + "[" + pid.toString() + "]", EEventSource.INSTANCE);
            }
        }
    }

    static class AffinityFilter implements IgniteBiPredicate<AffinityCacheKey, AffinityCacheVal> {

        final private int memberId;
        final private long curTime;

        AffinityFilter(int memberId, long curTime) {
            this.memberId = memberId;
            this.curTime = curTime;
        }

        @Override
        public boolean apply(AffinityCacheKey k, AffinityCacheVal v) {
            return v.getMemberId() == memberId && v.getExpirationTime() < curTime;
        }
    }

    final Comparator<AffinityCacheKey> affinityCacheKeyComparator = new Comparator<AffinityCacheKey>() {
        @Override
        public int compare(AffinityCacheKey o1, AffinityCacheKey o2) {
            return Long.compare(o1.getObjectKey(), o2.getObjectKey());
        }
    };

    private int[] expireAffinities(long curMillis) {
        AffinityFilter filter = new AffinityFilter(memberId, curMillis);
        int count = 0;
        final Set<AffinityCacheKey> toRemove = new TreeSet<>(affinityCacheKeyComparator);
        try (QueryCursor<Entry<AffinityCacheKey, AffinityCacheVal>> cursor = affinityCache.query(new ScanQuery<>(filter))) {
            for (Entry<AffinityCacheKey, AffinityCacheVal> entry : cursor) {
                if (count < RDX_AADC_MAX_PURGED_FINISHED_LOCKS_PER_ITER) {
                    toRemove.add(entry.getKey());
                }
                count++;
            }
        }
        affinityCache.removeAll(toRemove);
        return new int[]{toRemove.size(), count - toRemove.size()};
    }

    private boolean isGridValid() {
        return lockCache != null && !lockCache.isClosed();
    }

    private static class RadixIgniteLogger implements IgniteLogger {

        private static final boolean QUIET = SystemPropUtils.getBooleanSystemProp("rdx.ignite.logger.quiet", false);
        private static final Map<String, EEventSeverity> MIN_SEVERITY_MAP = new ConcurrentHashMap<>();
        private final ServerThreadLog serverThreadLog;

        public RadixIgniteLogger(String name) {
            this.serverThreadLog = new ServerThreadLog(name, false, MIN_SEVERITY_MAP.get(name));
        }

        public static void setMinSeverity(final String name, final EEventSeverity severity) {
            if (name != null) {
                MIN_SEVERITY_MAP.put(name, severity);
            }
        }

        @Override
        public IgniteLogger getLogger(Object ctgr) {
            if (ctgr == null) {
                return new RadixIgniteLogger("");
            }
            if (ctgr instanceof Class) {
                return new RadixIgniteLogger(((Class) ctgr).getName());
            }
            return new RadixIgniteLogger(ctgr.toString());
        }

        @Override
        public void trace(String msg) {
            serverThreadLog.trace(msg);
        }

        @Override
        public void debug(String msg) {
            serverThreadLog.debug(msg);
        }

        @Override
        public void info(String msg) {
            serverThreadLog.info(msg);
        }

        @Override
        public void warning(String msg) {
            serverThreadLog.warn(msg);
        }

        @Override
        public void warning(String msg, Throwable e) {
            serverThreadLog.warn(msg, e);
        }

        @Override
        public void error(String msg) {
            serverThreadLog.error(msg);
        }

        @Override
        public void error(String msg, Throwable e) {
            serverThreadLog.error(msg, e);
        }

        @Override
        public boolean isTraceEnabled() {
            return serverThreadLog.isTraceEnabled();
        }

        @Override
        public boolean isDebugEnabled() {
            return serverThreadLog.isDebugEnabled();
        }

        @Override
        public boolean isInfoEnabled() {
            return serverThreadLog.isInfoEnabled();
        }

        @Override
        public boolean isQuiet() {
            return QUIET;
        }

        @Override
        public String fileName() {
            return null;
        }

    }

    private static class AadcScnUpdateSchedulerThread extends Thread {

        private volatile boolean stopRequested = false;
        private final AadcManager aadcManager;
        private Connection dbConnection;

        public AadcScnUpdateSchedulerThread(AadcManager aadcManager) {
            super("AadcScnUpdateSchedulerThread");
            this.aadcManager = aadcManager;
            setDaemon(true);
        }

        @Override
        public void run() {
            final int updateIntervalSec = 10;
            final int delaySec = 20;
            final int jobLifeTimeSec = 600;
            boolean isFirst = false;
            while (!stopRequested && !Thread.interrupted()) {
                try {
                    if (dbConnection == null || dbConnection.isClosed()) {
                        dbConnection = aadcManager.instance.openNewDbConnection(getName(), null);
                    }
                    if (dbConnection != null) {

                        if (!isFirst) {
                            Thread.sleep(updateIntervalSec * 1000);
                        } else {
                            isFirst = false;
                        }

                        final String submitJobSql = "begin DBMS_SCHEDULER.CREATE_JOB("
                                + "job_name=>DBMS_SCHEDULER.GENERATE_JOB_NAME('AadcScnUpI" + aadcManager.instance.getId() + "_'),"
                                + "job_type=>'PLSQL_BLOCK',"
                                + "job_action=>'update RDX_Instance set aadcMyScn=(select current_scn from GV$DATABASE), aadcMyTime=sysTimestamp where id=" + aadcManager.instance.getId() + ";',"
                                + "start_date=>systimestamp + interval '" + delaySec + "' second,"
                                + "end_date=>systimestamp + interval '" + jobLifeTimeSec + "' second,"
                                + "enabled=>TRUE); end;";
                        try (CallableStatement c = dbConnection.prepareCall(submitJobSql)) {
                            c.execute();
                        }
                    } else {
                        LogFactory.getLog(AadcManager.class).warn("Unable to schedule AADC SCN update: no database connection");
                    }
                } catch (Throwable t) {
                    if (!stopRequested) {
                        LogFactory.getLog(AadcManager.class).warn("Exception on AADC SCN update scheduler thread", t);
                    }
                }
            }
        }

        public void requestStop() {
            stopRequested = true;
            interrupt();
        }

    }

    private static class MaintenanceThread extends Thread {

        private volatile boolean stopRequested = false;
        private final AadcManager aadcManager;

        public MaintenanceThread(AadcManager aadcManager) {
            super("AadcMaintenanceThread");
            this.aadcManager = aadcManager;
            setDaemon(true);
        }

        @Override
        public void run() {

            long lastEachMinuteMaintenanceMillis = 0;

            while (!stopRequested && !Thread.interrupted() && aadcManager.isGridValid()) {

                try {

                    final long curMillis = System.currentTimeMillis();

                    final long expireAffinitiesStartMillis = System.currentTimeMillis();

                    final int[] prgedExpAffAndLeftExpAff = aadcManager.expireAffinities(curMillis);

                    final long expireAffinitiesMillis = System.currentTimeMillis() - expireAffinitiesStartMillis;

                    final int[] prgedPrtFinLcksAndLeftFinLocks = aadcManager.purgePartnerFinishedLocks();

                    final long purgePartnerFinishedLocksMillis = System.currentTimeMillis() - expireAffinitiesStartMillis - expireAffinitiesMillis;

                    if (RDX_AADC_MANAGER_DEBUG) {
                        System.out.println(new Date() + ": expAff: " + expireAffinitiesMillis + " (" + prgedExpAffAndLeftExpAff[0] + " purged, " + prgedExpAffAndLeftExpAff[1] + " left)"
                                + ", prgPartFinLcks: " + purgePartnerFinishedLocksMillis + " (" + prgedPrtFinLcksAndLeftFinLocks[0] + " purged, " + prgedPrtFinLcksAndLeftFinLocks[1] + " left)");
                    }

                    //Purge locks
                    if (curMillis - lastEachMinuteMaintenanceMillis > 60 * 1000) {

                        lastEachMinuteMaintenanceMillis = curMillis;

                        long purgeMyUncommittedHungLocksMillis = 0;
                        long purgeHungPartnerUncommittedLocksMillis = 0;
                        long purgeHungPartnerCommittedLocksMillis = 0;

                        aadcManager.purgeMyUncommittedHungLocks(curMillis);

                        purgeMyUncommittedHungLocksMillis = System.currentTimeMillis() - lastEachMinuteMaintenanceMillis;

                        aadcManager.purgeHungPartnerUncommittedLocks(curMillis);

                        purgeHungPartnerUncommittedLocksMillis = System.currentTimeMillis() - lastEachMinuteMaintenanceMillis - purgeMyUncommittedHungLocksMillis;

                        aadcManager.purgeHungPartnerCommittedLocks(curMillis);

                        purgeHungPartnerCommittedLocksMillis = System.currentTimeMillis() - lastEachMinuteMaintenanceMillis - purgeMyUncommittedHungLocksMillis - purgeHungPartnerUncommittedLocksMillis;

                        if (RDX_AADC_MANAGER_DEBUG) {
                            System.out.println(new Date() + ": prgMyUncmtHgLcks: " + purgeMyUncommittedHungLocksMillis
                                    + ", prgHgPrtUncmtLcks: " + purgeHungPartnerUncommittedLocksMillis
                                    + ", prgHgPrtCmtLcks: " + purgeHungPartnerCommittedLocksMillis);
                        }
                    }

                    if (prgedExpAffAndLeftExpAff[1] == 0 && prgedPrtFinLcksAndLeftFinLocks[1] == 0) {
                        Thread.sleep(1000);
                    }

                } catch (Throwable t) {
                    if (!stopRequested) {
                        if (aadcManager != null && aadcManager.instance != null && aadcManager.instance.getTrace() != null) {
                            aadcManager.instance.getTrace().putFloodControlled("[AadcMaintenanceError]", EEventSeverity.ERROR, "Exception on AADC background maintenance: " + ExceptionTextFormatter.throwableToString(t), null, null, EEventSource.INSTANCE.getValue(), -1, false, null);
                        }
                    }
                }
            }
        }

        public void requestStop() {
            stopRequested = true;
            interrupt();
        }

    }
}
