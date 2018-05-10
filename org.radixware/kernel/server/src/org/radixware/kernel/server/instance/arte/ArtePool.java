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
package org.radixware.kernel.server.instance.arte;

import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.instance.arte.ArteInstance.EState;
import org.radixware.kernel.common.utils.net.RequestChannel;
import org.radixware.kernel.server.instance.Messages;
import org.radixware.kernel.server.instance.ThreadDumpWriter;
import org.radixware.kernel.server.sap.SapOptions;
import org.radixware.kernel.server.units.arte.ArteUnit;
import org.radixware.kernel.server.utils.IPriorityResourceManager;
import org.radixware.kernel.server.utils.SynchronizedPriorityResourceManager;

public class ArtePool {

    private static final int MAX_STARTING_COUNT = SystemPropUtils.getIntSystemProp("rdx.max.starting.arte", 10);
    private static final long OVERLOAD_TRACE_MESSAGE_INTERVAL_MILLIS = 2000;
    private static final int WARMUP_LOAD_PERCENT = Math.max(Math.min(SystemPropUtils.getIntSystemProp("rdx.arte.warmup.load.percent", 10), 100), 0);

    public static enum ECaptureResult {

        SUCCESS,
        ALL_BUSY,
        INVALID_VERSION,
        ARTE_IS_INITIALIZING;
    }
    public static final long ARTE_SHUTDOWN_INTERVAL_ON_RELOAD_MILLIS = 5000;
    private static final int MAX_ARTE_ON_MAINTENANCE_PERCENT = 10;
    private static final long SLEEP_ON_WAIT_FOR_ARTE_TO_INIT_MILLIS = 1;
    private static final long MAX_WAIT_FOR_ARTE_TO_INIT_NANOS = SystemPropUtils.getLongSystemProp("rdx.max.wait.for.arte.to.init.nanos", 1 * 1_000_000_000l);//1 s
    private final List<ArteInstance> pool;
    private final Instance instance;
    private final IdPool idPool = new IdPool();
    private final List<ArteInstance> waitingForMaintenanceQueue = new ArrayList<>();
    private final List<ArteInstance> onMaintenance = new ArrayList<>();
    private final Map<String, ArteInstance> arteInstByLastClientAddress;
    private final AtomicLong lastOverloadMessageMillis = new AtomicLong();
    private final IPriorityResourceManager priorityManager = new SynchronizedPriorityResourceManager();
    //Guarded by this
    private boolean shutdownCalled = false;
    //Guarded by this
    private long minMaxVersion = -1;
    //Guarded by this
    private long updatingToVersion = -1;
    private long minAcceptableVersion = -1;
    private long maxAcceptableVersion = -1;
    private final List<ArteInstance> onUpdate = new ArrayList<>();
    private final List<ArteInstance> toReload = new ArrayList<>();
    private final Random random = new Random();

    public ArtePool(final Instance instance) {
        this.instance = instance;
        pool = new CopyOnWriteArrayList<>();
        arteInstByLastClientAddress = new HashMap<>();
    }

    public Instance getInstance() {
        return instance;
    }

    public synchronized void load() throws Exception {
        loadMinArteInstCount();
    }

    public synchronized void setAcceptableVersions(final long minAcceptableVersion, final long maxAcceptableVersion) {
        if (this.minAcceptableVersion != minAcceptableVersion || this.maxAcceptableVersion != maxAcceptableVersion) {
            getInstance().getTrace().put(EEventSeverity.EVENT, String.format(Messages.ACCEPTABLE_VERSIONS_CHANGED, instance.getFullTitle(), minAcceptableVersion, maxAcceptableVersion), Messages.MLS_ID_ACCEPTED_VERSIONS_CHANGED, new ArrStr(instance.getFullTitle(), String.valueOf(minAcceptableVersion), String.valueOf(maxAcceptableVersion)), EEventSource.INSTANCE, false);
        }
        this.minAcceptableVersion = minAcceptableVersion;
        this.maxAcceptableVersion = maxAcceptableVersion;
    }

    public long getMinAcceptableVersion() {
        return minAcceptableVersion;
    }

    public long getMaxAcceptableVersion() {
        return maxAcceptableVersion;
    }

    public void onInstanceOptionsChanged() {
        priorityManager.setOptions(new IPriorityResourceManager.Options(instance.getNormalArteInstCount(), instance.getAboveNormalArteInstCount(), instance.getHighArteInstCount(), instance.getVeryHighArteInstCount(), instance.getCriticalArteInstCount()));
    }

    private void loadMinArteInstCount() throws InterruptedException {
        try {
            int startingCount = 0;
            for (ArteInstance arteInstance : pool) {
                if (arteInstance.getState() == EState.INIT) {
                    startingCount++;
                }
            }
            while (startingCount < MAX_STARTING_COUNT && pool.size() < instance.getMinArteInstCount()) {
                newArte();
                startingCount++;
            }
        } catch (Throwable e) {
            if (e instanceof InterruptedException || Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            instance.getTrace().put(EEventSeverity.ERROR, ArteMessages.ERR_ON_ARTE_INST_INIT + exStack, ArteMessages.MLS_ID_ERR_ON_ARTE_INST_INIT, new ArrStr(instance.getFullTitle(), exStack), EEventSource.ARTE_POOL, false);
        }
    }

    private ArteInstance newArte() throws Throwable {
        final ArteInstance arteInst = new ArteInstance(instance);
        arteInst.start(idPool.get());
        pool.add(arteInst);
        instance.getInstanceMonitor().increaseArteInstCount(1);
        return arteInst;
    }

    private String getClientKey(final IArteRequest request) {
        try {
            return request.getClientId() + "#" + request.getUnit().getServiceUri();
        } catch (Exception ex) {
            return "error-on-client-key-calculation";
        }
    }

    public synchronized void requestReload() {
        toReload.clear();
        toReload.addAll(pool);
    }

    private void updateOnMaintenanceList() {
        Iterator<ArteInstance> onMaintIt = onMaintenance.iterator();
        while (onMaintIt.hasNext()) {
            ArteInstance next = onMaintIt.next();
            if (!pool.contains(next) || next.isStopped() || next.getState() == EState.FREE || !next.isLastRequestWasMaintenance()) {
                onMaintIt.remove();
            }
        }
    }

    public int getArteCountInState(EState state) {
        int count = 0;
        for (ArteInstance arteInst : pool) {
            if (arteInst.getState() == state) {
                count++;
            }
        }
        return count;
    }

    private int getEffectiveMaxOnMaintenancePercent() {
        return MAX_ARTE_ON_MAINTENANCE_PERCENT;
    }

    public synchronized void maintenance() throws InterruptedException {
        adjust();
        doMaintenance();
        doReload();
        doUpdate();
        doUpdateStats();
    }

    private void doUpdateStats() {
        for (ArteInstance inst : pool) {
            inst.updateWaitsHistory();
        }
    }

    private void doReload() {
        if (toReload.size() > 0) {
            int nonShuttingDownAndStarted = 0;
            for (ArteInstance inst : pool) {
                if (inst.getRequestedShutdownTimeMillis() == 0 && inst.isStarted()) {
                    nonShuttingDownAndStarted++;
                }
            }
            final Iterator<ArteInstance> it = toReload.iterator();
            while (it.hasNext() && nonShuttingDownAndStarted > instance.getMinArteInstCount() / 2) {
                final ArteInstance inst = it.next();
                if (inst.isStopped() || inst.isStopping() || inst.getRequestedShutdownTimeMillis() > 0) {
                    it.remove();
                } else {
                    inst.requestShutdown(0);
                    nonShuttingDownAndStarted--;
                }
            }
        }
    }

    private void doUpdate() {
        actualizeOnUpdateList();
        actualizeMinMaxVersion();
        launchUpdateWherePossible();
    }

    private void actualizeMinMaxVersion() {
        minMaxVersion = -1;
        for (ArteInstance inst : pool) {
            final List<Long> cachedVersions = inst.getCachedVersionsSnapshot();
            final long maxVersion = cachedVersions == null || cachedVersions.isEmpty() ? inst.getVersionOnCreate() : cachedVersions.get(0);
            if (minMaxVersion == -1) {
                minMaxVersion = maxVersion;
            } else {
                minMaxVersion = Math.min(minMaxVersion, maxVersion);
            }
        }
    }

    private void launchUpdateWherePossible() {
        if (minMaxVersion < instance.getLatestVersion()) {

            updatingToVersion = instance.getLatestVersion();

            for (ArteInstance inst : pool) {
                if (getCurrentLimitForUpdate() < 1) {
                    break;
                } else {
                    if (inst.needUpdate() && inst.getState() == EState.FREE) {
                        launchUpdate(inst);
                    }
                }
            }
        } else if (updatingToVersion > 0) {
            instance.getTrace().put(EEventSeverity.EVENT, ArteMessages.ALL_ARTE_LOADED_VERSION + " #" + minMaxVersion, EEventSource.INSTANCE);
            updatingToVersion = -1;
        }
    }

    private void launchUpdate(final ArteInstance instance) {
        instance.maintenance();
        onUpdate.add(instance);
    }

    private int getCurrentLimitForUpdate() {
        return (getSize() + 1) / 2 - onMaintenance.size() - onUpdate.size();
    }

    private void actualizeOnUpdateList() {
        final Iterator<ArteInstance> iterator = onUpdate.iterator();
        while (iterator.hasNext()) {
            final ArteInstance inst = iterator.next();
            if (!pool.contains(inst) || inst.isStopped() || inst.getState() == EState.FREE || !inst.needUpdate()) {
                iterator.remove();
            }
        }
    }

    private void doMaintenance() {
        updateOnMaintenanceList();

        Iterator<ArteInstance> waitingIt = waitingForMaintenanceQueue.iterator();
        while (waitingIt.hasNext()) {
            ArteInstance next = waitingIt.next();
            if (!pool.contains(next) || next.isStopped() || next.isStopping() || !next.needMaintenance() || next.getState() != EState.FREE) {
                waitingIt.remove();
            }
        }

        for (ArteInstance inst : pool) {
            if (!waitingForMaintenanceQueue.contains(inst) && inst.getState() == EState.FREE && inst.needMaintenance()) {
                waitingForMaintenanceQueue.add(inst);
            }
        }

        int maxOnMaintenanceCount = pool.size() * getEffectiveMaxOnMaintenancePercent() / 100;
        if (maxOnMaintenanceCount < 1) {
            maxOnMaintenanceCount = 1;
        }

        while (waitingForMaintenanceQueue.size() > 0 && onMaintenance.size() < maxOnMaintenanceCount) {
            final ArteInstance inst = waitingForMaintenanceQueue.get(0);
            waitingForMaintenanceQueue.remove(inst);
            try {
                inst.maintenance();
                onMaintenance.add(inst);
            } catch (Throwable e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                instance.getTrace().put(EEventSeverity.ERROR, ArteMessages.ERR_ON_SERVICE_RQ_PROC + exStack, ArteMessages.MLS_ID_ERR_ON_SERVICE_RQ_PROC, new ArrStr(inst.getTitle(), exStack), EEventSource.ARTE, false);
            }
        }
    }

    public ArteCaptureInfo process(final IArteRequest request) throws InterruptedException, InsufficientArteInstanceCountException {
        final long startNanos = System.nanoTime();
        while (true) {
            final ArteCaptureInfo captureInfo = capture(request);
            if (captureInfo.result == ECaptureResult.SUCCESS) {
                return captureInfo;
            }
            if (captureInfo.result == ECaptureResult.ALL_BUSY || (System.nanoTime() - startNanos >= MAX_WAIT_FOR_ARTE_TO_INIT_NANOS)) {
                final long lastMessageMillis = lastOverloadMessageMillis.get();
                if (System.currentTimeMillis() - lastMessageMillis > OVERLOAD_TRACE_MESSAGE_INTERVAL_MILLIS && lastOverloadMessageMillis.compareAndSet(lastMessageMillis, System.currentTimeMillis())) {
                    instance.getTrace().put(EEventSeverity.WARNING, ArteMessages.INSUF_ARTE_COUNT, ArteMessages.MLS_ID_INSUF_ARTE_COUNT, new ArrStr(instance.getFullTitle()), EEventSource.ARTE_POOL, false);
                    instance.getThreadDumpWriter().requestDump(ThreadDumpWriter.EThreadDumpReason.INSUF_ARTE);
                }
                throw new InsufficientArteInstanceCountException(ECaptureResult.ALL_BUSY);
            }
            Thread.sleep(SLEEP_ON_WAIT_FOR_ARTE_TO_INIT_MILLIS);
        }

    }

    private synchronized ArteCaptureInfo capture(final IArteRequest request) throws InterruptedException, InsufficientArteInstanceCountException {
        if (shutdownCalled) {
            //generally, process() shouldn't be called after shutdown().
            //however, it's possible during hard stop, when instance thread
            //was interrupted while waiting for some unit to stop, and instance 
            //thread proceeded to ARTE pool shutdown 
            //when some ARTE modules are still running.
            throw new InsufficientArteInstanceCountException(ECaptureResult.ALL_BUSY);
        }
        if (minAcceptableVersion == -1) {
            throw new InsufficientArteInstanceCountException(ECaptureResult.ALL_BUSY);
        }
        if (request.getVersion() != -1 && (request.getVersion() < minAcceptableVersion || request.getVersion() > maxAcceptableVersion)) {
            throw new InsufficientArteInstanceCountException(ECaptureResult.INVALID_VERSION);
        }
        final IPriorityResourceManager.Ticket countTicket = priorityManager.requestTicketNow(request.getRadixPriority());
        if (countTicket == null) {
            return new ArteCaptureInfo(ECaptureResult.ALL_BUSY, null, -1);
        }
        boolean releaseCountTicket = true;
        try {

            final String clientKey;
            final RequestChannel requestChannel = request.getRequestChannel();
            if (requestChannel != null) {
                clientKey = getClientKey(request);
                final ArteInstance lastArteInst = arteInstByLastClientAddress.get(clientKey);
                if (lastArteInst != null) {
                    pool.remove(lastArteInst);
                    pool.add(0, lastArteInst);
                }
            } else {
                clientKey = null;
            }

            int overallOk = 0;
            final List<ArteInstance> stopped = new ArrayList<>();
            final List<ArteInstance> starting = new ArrayList<>();
            try {
                final ArtePickMode pickMode = random.nextInt(100) < WARMUP_LOAD_PERCENT ? ArtePickMode.PICK_RANDOM : ArtePickMode.PICK_FIRST;
                final ArtePicker picker = new ArtePicker(minAcceptableVersion, maxAcceptableVersion, request.getVersion(), pickMode);

                for (final ArteInstance arteInst : pool) {
                    if (arteInst.isStopped()) {
                        stopped.add(arteInst);
                        continue;
                    }
                    if (arteInst.getState() == EState.FREE) {
                        if (updatingToVersion > 0 && arteInst.needUpdate() && getCurrentLimitForUpdate() > 0) {
                            launchUpdate(arteInst);
                            continue;
                        }
                        picker.check(arteInst);
                        if (picker.isBestSelected()) {
                            break;
                        }
                    } else if (arteInst.getState() == EState.INIT) {
                        starting.add(arteInst);
                    }
                    if (isOkVer(arteInst, request.getVersion())) {
                        overallOk++;
                    }

                }
                if (picker.getSelectedArteInst() != null) {
                    final ArteInstance selectedArteInst = picker.getSelectedArteInst();
                    final Callable<Boolean> aliveChecker = new Callable<Boolean>() {

                        @Override
                        public Boolean call() throws Exception {
                            return !selectedArteInst.isStopped();
                        }
                    };
                    countTicket.setHolderAliveChecker(aliveChecker);
                    request.setCountTicket(countTicket);
                    picker.getSelectedArteInst().process(request.getVersion() == -1 ? new VersionedRequest(request, picker.getSelectedVersion()) : request);
                    releaseCountTicket = false;
                    return new ArteCaptureInfo(ECaptureResult.SUCCESS, selectedArteInst, picker.getSelectedVersion());//NOPMD
                }

            } finally {
                removeFromPool(stopped);
            }

            if (starting.isEmpty() && pool.size() < instance.getTotalMaxArteInstCount()) {
                try {
                    starting.add(newArte());
                } catch (Throwable t) {
                    //shouldn't happen
                    instance.getTrace().put(EEventSeverity.ERROR, "Error on ARTE creation: " + ExceptionTextFormatter.throwableToString(t), EEventSource.ARTE_POOL);
                }
            }

            if (overallOk == 0 && (starting.isEmpty() || request.getVersion() != maxAcceptableVersion)) {
                throw new InsufficientArteInstanceCountException(ECaptureResult.INVALID_VERSION);
            }

            if (!starting.isEmpty()) {
                return new ArteCaptureInfo(ECaptureResult.ARTE_IS_INITIALIZING, null, -1);
            } else {
                return new ArteCaptureInfo(ECaptureResult.ALL_BUSY, null, -1);
            }
        } finally {
            if (releaseCountTicket) {
                priorityManager.releaseTicket(countTicket);
            }
        }
    }

    private boolean isOkVer(final ArteInstance arteInst, final long requestVersion) {
        final List<Long> cachedVersions = arteInst.getCachedVersionsSnapshot();
        if (requestVersion == -1 || cachedVersions == null || cachedVersions.isEmpty()) {
            return true;
        }
        return cachedVersions.contains(requestVersion);
    }

    public void release(IPriorityResourceManager.Ticket ticket) {
        //priorityManager uses internal synchronization
        priorityManager.releaseTicket(ticket);
    }

    private void removeFromPool(final List<ArteInstance> removeList) {
        if (removeList == null || removeList.isEmpty()) {
            return;
        }
        pool.removeAll(removeList);
        waitingForMaintenanceQueue.removeAll(removeList);
        onMaintenance.removeAll(removeList);
        for (ArteInstance removedInstance : removeList) {
            idPool.returnToPool(removedInstance.getSeqNumber());
            try {
                //if for some reason stop has never been called,
                //give a last chance to perform cleanup (close logs, etc)
                removedInstance.stop(0);
            } catch (Throwable t) {
                //ignore
            }
        }
        instance.getInstanceMonitor().setArteInstCount(pool.size());
        final List<String> removeListClientAddress = new LinkedList<String>();
        for (Map.Entry<String, ArteInstance> entry : arteInstByLastClientAddress.entrySet()) {
            if (removeList.contains(entry.getValue())) {
                removeListClientAddress.add(entry.getKey());
            }
        }
        for (String removeClientAddress : removeListClientAddress) {
            arteInstByLastClientAddress.remove(removeClientAddress);
        }
    }

    public int getSize() {
        return pool.size();
    }

    public void shutdown() {
        shutdown(-1);
    }

    public boolean shutdown(final long waitTimeMillis) {
        return doShutdown(waitTimeMillis);
    }

    public boolean doShutdown(final long waitTimeMillis) {
        final long startMillis = System.currentTimeMillis();
        boolean arteIsRunning = false;
        synchronized (this) {
            shutdownCalled = true;
            for (ArteInstance arteInst : pool) {
                try {
                    if (!arteInst.stop(0)) {
                        arteIsRunning = true;
                    } else {
                        removeFromPool(Collections.singletonList(arteInst));
                    }
                } catch (Throwable e) {
                    final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                    instance.getTrace().put(EEventSeverity.ERROR, ArteMessages.ERR_ON_ARTE_INST_STOP + " \"" + arteInst.getTitle() + "\": " + exStack, ArteMessages.MLS_ID_ERR_ON_ARTE_INST_STOP, new ArrStr(arteInst.getTitle(), exStack), EEventSource.ARTE_POOL, false);
                }
            }
        }

        //wait for all instances to stop
        while (arteIsRunning && (waitTimeMillis < 0 || (System.currentTimeMillis() - startMillis < waitTimeMillis))) {
            synchronized (this) {
                arteIsRunning = false;
                for (ArteInstance arteInst : pool) {
                    if (!arteInst.isStopped()) {
                        arteIsRunning = true;
                        arteInst.updateWaitsHistory();
                    } else {
                        removeFromPool(Collections.singletonList(arteInst));
                    }

                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                //ignore
            }
        }
        if (!arteIsRunning) {
            synchronized (this) {
                pool.clear();
                instance.getInstanceMonitor().setArteInstCount(0);
                arteInstByLastClientAddress.clear();
                return true;
            }
        }
        return false;
    }

    public synchronized void requestArteInstanceStop(final ArteInstance arteInstance, final long delayMillis) {
        if (delayMillis < 0) {
            throw new IllegalArgumentException("Delay should be non-negative");
        }
        if (arteInstance != null) {
            arteInstance.requestShutdown(delayMillis);
            if (arteInstance.getState() == EState.FREE) {
                arteInstance.maintenance();
            }
        }
    }

    /**
     * Should be called only from maintenance under synchronized(ArtePool.this)
     *
     * @throws InterruptedException
     */
    private void adjust() throws InterruptedException {
        if (instance.isShuttingDown() || shutdownCalled) {
            return;
        }
        loadMinArteInstCount();

        final LinkedList<ArteInstance> stoppedList = new LinkedList<>();

        for (ArteInstance inst : pool) {
            if (inst.isStopped()) {
                stoppedList.add(inst);
            }
        }

        removeFromPool(stoppedList);
        stoppedList.clear();

        if (pool.size() > instance.getTotalMaxArteInstCount()) {
            for (ArteInstance arteInst : pool) {
                try {
                    if (!arteInst.isStopped()) {
                        if (arteInst.getState() == EState.BUSY) {
                            continue;
                        }
                        if (!arteInst.stop(1000)) //wait 1 sec
                        {
                            continue;
                        }
                    }
                    stoppedList.add(arteInst);
                    if (pool.size() - stoppedList.size() <= instance.getTotalMaxArteInstCount()) {
                        break;
                    }
                } catch (Throwable e) {
                    if (e instanceof InterruptedException || Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }
                    final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                    instance.getTrace().put(EEventSeverity.ERROR, ArteMessages.ERR_ON_ARTE_INST_STOP + " \"" + arteInst.getTitle() + "\": " + exStack, ArteMessages.MLS_ID_ERR_ON_ARTE_INST_STOP, new ArrStr(arteInst.getTitle(), exStack), EEventSource.ARTE_POOL, false);
                }
            }
            removeFromPool(stoppedList);
        }
    }

    public ArteInstance findArteInstBySerial(final long serial) {
        for (ArteInstance arteInst : getInstances(false)) {
            if (arteInst.getSerial() == serial) {
                return arteInst;
            }
        }
        return null;
    }

    public List<ArteInstance> getInstances(final boolean sortBySeq) {
        if (sortBySeq) {
            final ArrayList<ArteInstance> res = new ArrayList<>(pool);
            Collections.sort(res, new Comparator<ArteInstance>() {
                @Override
                public int compare(final ArteInstance i1, final ArteInstance i2) {
                    if (i1.getSeqNumber() < i2.getSeqNumber()) {
                        return -1;
                    } else if (i1.getSeqNumber() == i2.getSeqNumber()) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            });
            return Collections.unmodifiableList(res);
        } else {
            return Collections.unmodifiableList(pool);
        }
    }

    /**
     * @NotThreadSafe
     */
    private static class IdPool {

        private long counter = 0;
        private final Queue<Long> queue = new ArrayDeque<Long>();

        public long get() {
            if (queue.isEmpty()) {
                return ++counter;
            } else {
                return queue.poll();
            }
        }

        public void returnToPool(final long id) {
            queue.add(id);
        }

        public void reset() {
            queue.clear();
            counter = 0;
        }
    }

    public static class ArteCaptureInfo {

        public final ECaptureResult result;
        public ArteInstance arteInstance;
        public long actualVersion;

        public ArteCaptureInfo(final ECaptureResult result, final ArteInstance arteInstance, final long actualVersion) {
            this.result = result;
            this.arteInstance = arteInstance;
            this.actualVersion = actualVersion;
        }

    }

    private static enum ArtePickMode {

        PICK_RANDOM,
        PICK_FIRST;
    }

    private class ArtePicker {

        private ArteInstance selectedArte;
        private final long requestVersion;
        private final long minAcceptableVersion;
        private final long maxAcceptableVersion;
        private long selectedVersion = -1;
        private final List<ArteInstance> variants;

        public ArtePicker(final long minAcceptableVersion, final long maxAcceptableVersion, final long requestVersion, final ArtePickMode pickMode) {
            this.minAcceptableVersion = minAcceptableVersion;
            this.maxAcceptableVersion = maxAcceptableVersion;
            this.requestVersion = requestVersion;
            if (pickMode == ArtePickMode.PICK_RANDOM) {
                variants = new ArrayList<>();
            } else {
                variants = null;
            }
        }

        public void check(ArteInstance arteInst) {
            final List<Long> cachedVersions = arteInst.getCachedVersionsSnapshot();
            if (cachedVersions != null) {
                if (requestVersion == -1) {
                    for (Long v : cachedVersions) {
                        if (minAcceptableVersion <= v && v <= maxAcceptableVersion && v >= selectedVersion) {
                            if (v > selectedVersion) {
                                selectedVersion = v;
                                if (variants == null) {
                                    selectedArte = arteInst;
                                } else {
                                    variants.clear();
                                }
                            }
                            if (variants != null) {
                                variants.add(arteInst);
                            }
                            break;
                        }
                    }
                } else {
                    if (cachedVersions.contains(requestVersion)) {
                        if (variants != null) {
                            variants.add(arteInst);
                        } else if (selectedVersion == -1) {
                            selectedVersion = requestVersion;
                            selectedArte = arteInst;
                        }
                    }
                }
            }
        }

        public long getSelectedVersion() {
            return selectedVersion;
        }

        public ArteInstance getSelectedArteInst() {
            if (selectedArte == null && variants != null && !variants.isEmpty()) {
                selectedArte = variants.get(random.nextInt(variants.size()));
            }
            return selectedArte;
        }

        public boolean isBestSelected() {
            if (variants != null) {
                return false;
            }
            return requestVersion == -1 ? selectedVersion == maxAcceptableVersion : selectedVersion == requestVersion;
        }
    }

    public static class InsufficientArteInstanceCountException extends Exception {

        private final ECaptureResult captureResult;

        public InsufficientArteInstanceCountException(final ECaptureResult captureResult) {
            this.captureResult = captureResult;
        }

        public ECaptureResult getCaptureResult() {
            return captureResult;
        }

    }

    private static class VersionedRequest implements IArteRequest {

        private final IArteRequest delegate;
        private final long version;

        public VersionedRequest(IArteRequest delegate, long version) {
            this.delegate = delegate;
            this.version = version;
        }

        @Override
        public ArteUnit getUnit() {
            return delegate.getUnit();
        }

        @Override
        public RequestChannel getRequestChannel() {
            return delegate.getRequestChannel();
        }

        @Override
        public IArteRequestCallback getCallback() {
            return delegate.getCallback();
        }

        @Override
        public String getClientId() {
            return delegate.getClientId();
        }

        @Override
        public SapOptions getSapOptions() {
            return delegate.getSapOptions();
        }

        @Override
        public int getRadixPriority() {
            return delegate.getRadixPriority();
        }

        @Override
        public InputStream getOverriddenInput() {
            return delegate.getOverriddenInput();
        }

        @Override
        public IPriorityResourceManager.Ticket getCountTicket() {
            return delegate.getCountTicket();
        }

        @Override
        public void setCountTicket(IPriorityResourceManager.Ticket ticket) {
            delegate.setCountTicket(ticket);
        }

        @Override
        public IPriorityResourceManager.Ticket getActiveTicket() {
            return delegate.getActiveTicket();
        }

        @Override
        public void setActiveTicket(IPriorityResourceManager.Ticket ticket) {
            delegate.setActiveTicket(ticket);
        }

        @Override
        public long getCreateTimeMillis() {
            return delegate.getCreateTimeMillis();
        }

        @Override
        public long getVersion() {
            return version;
        }

        @Override
        public Map<String, String> getHeaders() {
            return delegate.getHeaders();
        }

    }
}
