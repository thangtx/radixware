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

import java.io.File;
import java.lang.management.ThreadInfo;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.build.xbeans.IXBeansChangeListener;
import org.radixware.kernel.common.build.xbeans.XBeansChangeEmitterHandler;
import org.radixware.kernel.common.build.xbeans.XBeansChangeEmitterHandler.ListenersStorage;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.environment.IRadixDefManager;
import org.radixware.kernel.common.environment.IRadixEnvironment;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.trace.IRadixTrace;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.common.enums.EInstanceThreadKind;
import org.radixware.kernel.server.instance.InstanceThreadStateRecord;
import org.radixware.kernel.server.jdbc.RadixConnection;
import org.radixware.kernel.server.monitoring.ArteWaitStats;
import org.radixware.kernel.server.trace.DbLog;
import org.radixware.kernel.server.trace.FileLog;
import org.radixware.kernel.server.trace.FileLogOptions;
import org.radixware.kernel.server.trace.ServerThread;
import org.radixware.kernel.server.trace.ServerTrace;
import org.radixware.kernel.server.trace.TraceProfiles;
import org.radixware.kernel.server.units.ServerItemView;

/**
 * Инстанция ARTE.
 *
 * Threadsafe mediator used for communication between unit and arte instance
 * threads
 *
 * @Threadsafe
 *
 *
 *
 */
public class ArteInstance {

    public static enum EState {

        INIT,
        BUSY,
        FREE
    }
    //
    private static final long PING_DB_PERIOD_MILLIS = 5000;
    private static final long WAITS_HISTORY_STORE_DURATION_MILLIS = 60000;
    private static final long WAITS_HISTORY_UPDATE_INTERVAL_MILLIS = 1000;
    private static AtomicLong serialSequence = new AtomicLong();
    //
    private final long serial;
    private final ArteInstanceStatistic statistic = new ArteInstanceStatistic();
    private final Instance instance;
    private final long createTime;
    private volatile boolean isStopping = false;
    private long lastRequestMillis = 0;
    private boolean lastRequestWasMaintenance = false;
    private volatile long requestedShutdownTimeMillis = 0;
    private volatile FileLog log = null;
    private volatile IArteRequest arteRequest = null;
    private volatile ProcessThread thread = null;
    private volatile long instSeq = 0;
    private volatile ServerItemView view;
    private final ArteServerTrace trace;
    private volatile DbLog dbLog = null;
    private volatile EState state = EState.INIT;
    private final Object logSem = new Object();
    private volatile Connection dbConnection = null;
    private volatile List<Long> cachedVersions;
    private final long versionOnCreate;
    private volatile boolean started = false;
    private final LinkedList<ArteWaitsHistoryItem> waitsHistory = new LinkedList<>();
    private ArteWaitsHistoryItem lastWaitsSnapshot = null;
    private volatile Arte arte;

    public ArteInstance(final Instance instance) {
        super();
        this.instance = instance;
        trace = ServerTrace.Factory.newInstance(instance, ArteServerTrace.class);
        trace.setProfiles(
                new TraceProfiles(
                        EEventSeverity.NONE.getName(), //ARTE writes to eventlog itself (without sync)
                        EEventSeverity.NONE.getName(), //ARTE writes to eventlog itself (without sync)
                        null //default value until options are loaded
                ));
        serial = serialSequence.incrementAndGet();
        createTime = System.currentTimeMillis();
        versionOnCreate = instance.getLatestVersion();
    }

    public long getVersionOnCreate() {
        return versionOnCreate;
    }

    public void clearRequest() {
        arteRequest = null;
    }

    public long getStartTime() {
        //using create time as start time 
        //we do not need to be very precise
        //but in this way we can make this field immutable
        return createTime;
    }

    void setDbLog(final DbLog dbLog) {
        this.dbLog = dbLog;
    }

    void setView(final ServerItemView view) {
        this.view = view;
    }

    public List<String> getClassesLoadedByArteClassLoader() {
        try {
            //it's not completely thread safe, but access is read-only and nothing really bad can happen
            final ClassLoader classLoader = thread.getArte().getDefManager().getReleaseCache().getPrivateClassloader();
            final Field classesField = ClassLoader.class.getDeclaredField("classes");
            classesField.setAccessible(true);
            final Vector vectorObj = (Vector) classesField.get(classLoader);
            final Object[] classObjects = vectorObj.toArray();
            final List<String> result = new ArrayList<>(classObjects.length);
            for (Object classObj : classObjects) {
                if (classObj instanceof Class) {
                    final String simpleClassName = ((Class) classObj).getSimpleName();
                    if (simpleClassName == null
                            || simpleClassName.startsWith("usf")
                            || simpleClassName.startsWith("apl")
                            || simpleClassName.startsWith("rpu")) {
                        continue;
                    }
                    result.add(((Class) classObj).getName());
                }
            }
            return result;
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

    ArteInstanceStatistic getStatistic() {
        return statistic;
    }

    public IArteRequest getRequest() {
        return arteRequest;
    }

    public ArteServerTrace getTrace() {
        return trace;
    }

    public ServerItemView getView() {
        return view;
    }

    public void requestShutdown(final long delayMillis) {
        if (delayMillis < 0) {
            return;
        }
        requestedShutdownTimeMillis = System.currentTimeMillis() + delayMillis;
    }

    public long getRequestedShutdownTimeMillis() {
        return requestedShutdownTimeMillis;
    }

    public void start(final long instSeq) {
        this.instSeq = instSeq;
        thread = new ProcessThread(new ArteProcessor(this));
        thread.start();
    }

    public List<DbLog.Item> popAllDbLogItems() {
        final DbLog curDbLog = this.dbLog;//saving value of mutable volatile field
        if (curDbLog != null) {
            return curDbLog.popAllFromBuffer();
        }
        return Collections.emptyList();
    }

    public void updateWaitsHistory() {
        synchronized (waitsHistory) {
            final long curMillis = System.currentTimeMillis();
            if (lastWaitsSnapshot != null && curMillis - lastWaitsSnapshot.getTimestampMillis() < WAITS_HISTORY_UPDATE_INTERVAL_MILLIS) {
                return;
            }
            while (!waitsHistory.isEmpty() && (curMillis - waitsHistory.getFirst().getTimestampMillis() > WAITS_HISTORY_STORE_DURATION_MILLIS)) {
                waitsHistory.removeFirst();
            }
            final ArteWaitStats curSnapshot = thread == null ? null : (thread.getArte() == null ? null : thread.getArte().getProfiler().getWaitStatsSnapshot());
            if (curSnapshot != null) {
                if (lastWaitsSnapshot != null) {
                    waitsHistory.add(new ArteWaitsHistoryItem(lastWaitsSnapshot.getWaitStats().substractFrom(curSnapshot), curMillis));
                }
                lastWaitsSnapshot = new ArteWaitsHistoryItem(curSnapshot, curMillis);
            }
        }
    }

    public List<ArteWaitsHistoryItem> getWaitsHistory() {
        synchronized (waitsHistory) {
            return new ArrayList<>(waitsHistory);
        }
    }

    public boolean needMaintenance() {
        return (thread != null && thread.getArte() != null && thread.getArte().needMaintenance()) || System.currentTimeMillis() - lastRequestMillis > PING_DB_PERIOD_MILLIS;
    }

    public void flushWaitStats() {
        if (thread != null && thread.getArte() != null) {
            thread.getArte().getProfiler().flushWaitStatsThreadSafe();
        }
    }

    public boolean needUpdate() {
        final List<Long> cachedVersionsSnapshot = getCachedVersionsSnapshot();
        if (cachedVersionsSnapshot != null && !cachedVersionsSnapshot.isEmpty()) {
            return cachedVersionsSnapshot.get(0) < instance.getLatestVersion();
        }
        return false;
    }

    public void maintenance() {
        process(null);
    }

    /**
     * @return List of the cached versions, most recent version come first
     */
    public List<Long> getCachedVersionsSnapshot() {
        return cachedVersions;
    }

    public long getLatestCachedVersion() {
        final List<Long> cachedVerListSnapshot = getCachedVersionsSnapshot();
        if (cachedVerListSnapshot == null || cachedVerListSnapshot.isEmpty()) {
            return -1;
        }
        return cachedVerListSnapshot.get(0);
    }

    public void setCachedVersions(List<Long> cachedVersions) {
        this.cachedVersions = cachedVersions == null ? null : new ArrayList<>(cachedVersions);
    }

    public void process(final IArteRequest request) {
        synchronized (getNextRqSemaphore()) {
            if (state != EState.FREE) {
                throw new IllegalUsageError("ARTE is busy");
            }
            if (isStopped()) {
                throw new IllegalUsageError("ARTE is stopped");
            }
            if (thread.getState() != Thread.State.WAITING) {
                throw new IllegalUsageError("Wrong ARTE thread state: free instance is not waiting for request, thread state  is " + String.valueOf(thread.getState()));
            }
            setState(EState.BUSY);
            this.arteRequest = request;
            getNextRqSemaphore().notify();
            updateLastRequestInfo(request);
        }

    }

    private void updateLastRequestInfo(final IArteRequest request) {
        lastRequestMillis = System.currentTimeMillis();
        lastRequestWasMaintenance = request == null;
    }

    public boolean isLastRequestWasMaintenance() {
        return lastRequestWasMaintenance;
    }

    protected void setState(final EState state) {
        if (state == EState.FREE) {
            started = true;
        }
        this.state = state;
    }

    public boolean isStarted() {
        return started;
    }

    public EState getState() {
        return state;
    }

    public boolean isStopped() {
        return (thread.getState() == Thread.State.TERMINATED);
    }

    public boolean isStopping() {
        return isStopping;
    }

    /**
     *
     * @param waitMillis - 0 - do not wait
     * @return
     * @throws InterruptedException
     */
    public boolean stop(final long waitMillis) throws InterruptedException {
        isStopping = true;
        if (thread.getState() != Thread.State.TERMINATED) {
            Instance.get().getArtePool().requestArteInstanceStop(this, 0);
            if (waitMillis > 0) {
                thread.join(waitMillis);
            }
        }
        synchronized (logSem) {
            if (!thread.isAlive()) {
                closeFileLog();
            }
        }
        return isStopped();
    }

    void closeFileLog() {
        synchronized (logSem) {
            if (log != null) {
                log.close();
            }
            log = null;
        }
    }

    public String getTitle() {
        return Messages.TITLE_ARTE_INST + " #" + String.valueOf(instSeq);
    }

    public Thread getThread() {
        return thread;
    }

    public long getSerial() {
        return serial;
    }

    public long getLastRequestMillis() {
        return lastRequestMillis;
    }

    public long getSeqNumber() {
        return instSeq;
    }

    public Connection getDbConnection() {
        return dbConnection;
    }

    public void setDbConnection(final Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public void closeDbConnectionForcibly(final String reason) throws SQLException {
        ((RadixConnection) dbConnection).forciblyClose(reason);
    }

    public Long getDbSessionId() {
        if (thread != null) {
            final Arte arte = thread.getArte();
            if (arte != null) {
                return arte.getDbSid();
            }
        }
        return null;
    }
    private final Object nextRqSem = new Object();

    final Object getNextRqSemaphore() {
        return nextRqSem;
    }

    final FileLog getFileLog() {
        return log;
    }

    public void interruptArte() {
        thread.interrupt();
    }

    public void applyNewFileLogOptions() {
        if (thread == null) {
            return;
        }
        final FileLogOptions newOptions = getFileLogOptions();
        final FileLog oldLog;
        synchronized (logSem) {
            if (isStopping) {
                return;
            }
            oldLog = log;
            try {
                final FileLog newLog = FileLog.create(newOptions);
                log = newLog;
            } catch (RadixError e) {
                log = null;
                getTrace().put(EEventSeverity.ERROR, ExceptionTextFormatter.exceptionStackToString(e), null /*
                         * TODO MLS
                         */, null, EEventSource.ARTE, false);
            }
        }
        if (oldLog != null) {
            oldLog.logOptionsChanged(newOptions);
            oldLog.close();
        }
    }

    private FileLogOptions getFileLogOptions() {
        final FileLogOptions instanceOptions = instance.getFileLogOptions();
        if (instanceOptions == null) {
            return null;
        }
        return new FileLogOptions(
                new File(new File(instanceOptions.getDir(), "arte_instances"), "arte_instance_#" + getSeqNumber()),
                "arte_instance_#" + getSeqNumber(),
                instanceOptions.getMaxFileSizeBytes(),
                instanceOptions.getRotationCount(),
                instanceOptions.isRotateDaily(),
                instanceOptions.isWriteContextToFile());
    }

    public Instance getInstance() {
        return instance;
    }

    public void arteInited(final Arte arte) {
        this.arte = arte;
    }

    public Arte getArte() {
        return arte;
    }

    static final class Messages {

        static {
            final ResourceBundle bundle = ResourceBundle.getBundle("org.radixware.kernel.server.instance.arte.mess.messages");

            _STARTED = bundle.getString("_STARTED");
            _STOPPED = bundle.getString("_STOPPED");
            TITLE_ARTE_INST = bundle.getString("TITLE_ARTE_INST");
            _OF_UNIT_ = bundle.getString("_OF_UNIT_");
            REASON = bundle.getString("REASON");
            //ERR_ON_ACCEPTED_SOCKET_CLOSE = bundle.getString("ERR_ON_ACCEPTED_SOCKET_CLOSE");
        }
        static final String _STOPPED;
        static final String REASON;
        static final String _STARTED;
        static final String TITLE_ARTE_INST;
        //static final String ERR_ON_ACCEPTED_SOCKET_CLOSE;
        static final String _OF_UNIT_;
    }

    private final class ProcessThread extends ServerThread implements XBeansChangeEmitterHandler.ListenersStorageProvider, IArteProvider, IRadixEnvironment {

        private final XmlObjListenersStorage storage = new XmlObjListenersStorage();
        private final ArteProcessor processor;
        private volatile ArteWaitStats prevGatheredWaits = null; // for InstanceThreadStateRecord

        ProcessThread(final ArteProcessor processor) {
            super(getTitle(), processor, ArteInstance.class.getClassLoader(), trace.newTracer(EEventSource.ARTE.getValue()));
            this.processor = processor;
        }

        @Override
        public Arte getArte() {
            return processor.getArte();
        }

        @Override
        public IRadixTrace getTrace() {
            return trace;
        }

        @Override
        public Connection getConnection() {
            return getDbConnection();
        }

        @Override
        public LocalTracer getLocalTracer() {
            if (processor.getArteThreadLocalTracer() == null) {
                return localTracer;
            }
            return processor.getArteThreadLocalTracer();
        }

        @Override
        public ListenersStorage getListenersStorage() {
            return storage;
        }

        @Override
        public EIsoLanguage getClientLanguage() {
            final Arte arte = getArte();
            if (arte != null) {
                return arte.getClientLanguage();
            }
            return null;
        }

        @Override
        public List<EIsoLanguage> getLanguages() {
            final Arte arte = getArte();
            if (arte != null) {
                return arte.getLanguages();
            }
            return null;
        }

        @Override
        public IRadixDefManager getDefManager() {
            final Arte arte = getArte();
            if (arte != null) {
                return arte.getDefManager();
            }
            return null;
        }

        @Override
        public boolean isAborted() {
            return false;
        }

        private synchronized ArteWaitStats getWaitDiff() {
            final ArteWaitStats currWaits = lastWaitsSnapshot == null ? null : lastWaitsSnapshot.getWaitStats();
            final ArteWaitStats diffWaits = prevGatheredWaits == null ? null : prevGatheredWaits.substractFrom(currWaits);
            prevGatheredWaits = currWaits;
            return diffWaits;
        }

        @Override
        public InstanceThreadStateRecord getThreadStateRecord(ThreadInfo threadInfo) {
            return InstanceThreadStateRecord.createRecord(EInstanceThreadKind.ARTE, this, threadInfo, getArte(), processor.getArteInstance(), getWaitDiff(), (RadixConnection) getConnection());
        }
    }

    private static class XmlObjListenersStorage implements XBeansChangeEmitterHandler.ListenersStorage {

        private final Map<XmlObject, Map<IXBeansChangeListener, Object>> listenersMap = new WeakHashMap<>();

        @Override
        public void addListener(final XmlObject xmlObject, final IXBeansChangeListener listener) {
            Map<IXBeansChangeListener, Object> listeners = listenersMap.get(xmlObject);
            if (listeners == null) {
                listeners = new WeakHashMap<>();
                listenersMap.put(xmlObject, listeners);
            }
            listeners.put(listener, null);
        }

        @Override
        public void removeListener(final XmlObject xmlObject, final IXBeansChangeListener listener) {
            Map<IXBeansChangeListener, Object> listeners = listenersMap.get(xmlObject);
            if (listeners != null) {
                listeners.remove(listener);
            }
        }

        @Override
        public Collection<IXBeansChangeListener> getListeners(XmlObject obj) {
            final Map<IXBeansChangeListener, Object> listeners = listenersMap.get(obj);
            if (listeners != null) {
                return new ArrayList<>(listeners.keySet());
            }
            return Collections.emptyList();
        }

        @Override
        public boolean hasListeners(XmlObject obj) {
            final Map<IXBeansChangeListener, Object> listeners = listenersMap.get(obj);
            if (listeners != null && listeners.size() > 0) {
                return true;
            }
            return false;
        }
    }

}
