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
package org.radixware.kernel.server.units.arte;

import java.io.BufferedInputStream;
import java.io.EOFException;
import org.radixware.kernel.common.utils.net.JmsServerChannel;
import org.radixware.kernel.common.utils.net.PipeServerChannel;
import org.radixware.kernel.common.utils.net.ServerChannel;
import org.radixware.kernel.common.utils.net.SocketServerChannel;
import org.radixware.kernel.common.utils.net.RequestChannel;
import org.radixware.kernel.common.utils.net.SocketRequestChannel;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.Socket;
import java.nio.channels.CancelledKeyException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import org.apache.commons.logging.LogFactory;

import org.radixware.kernel.common.enums.EClientAuthentication;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EPortSecurityProtocol;
import org.radixware.kernel.common.enums.EPriority;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.common.exceptions.CertificateUtilsException;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.ssl.CertificateUtils;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.HttpFormatter;
import org.radixware.kernel.common.utils.SoapFormatter;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.common.utils.net.SapAddress;
import org.radixware.kernel.common.utils.net.SocketServerChannel.SecurityOptions;
import org.radixware.kernel.common.utils.net.SslUtils;
import org.radixware.kernel.server.SrvRunParams;
import org.radixware.kernel.server.aio.Event;
import org.radixware.kernel.server.aio.EventDispatcher;
import org.radixware.kernel.server.aio.EventDispatcher.AcceptEvent;
import org.radixware.kernel.server.aio.EventDispatcher.TimerEvent;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.instance.UnitCommand;
import org.radixware.kernel.server.instance.UnitCommandResponse;
import org.radixware.kernel.server.instance.arte.ArtePool;
import org.radixware.kernel.server.instance.arte.IArteRequest;
import org.radixware.kernel.server.instance.arte.IArteRequestCallback;
import org.radixware.kernel.server.instance.arte.ArtePool.InsufficientArteInstanceCountException;
import org.radixware.kernel.server.monitoring.IStatValue;
import org.radixware.kernel.server.monitoring.IntegralCountStat;
import org.radixware.kernel.server.monitoring.MetricParameters;
import org.radixware.kernel.server.monitoring.RegisteredItem;
import org.radixware.kernel.server.sap.SapOptions;
import org.radixware.kernel.server.sap.SapQueries;
import org.radixware.kernel.server.soap.ServerSoapUtils;
import org.radixware.kernel.server.units.AsyncEventHandlerUnit;
import org.radixware.kernel.server.units.Messages;
import org.radixware.kernel.server.units.UnitView;
import org.radixware.kernel.server.utils.PriorityResourceManager;
import org.radixware.schemas.types.NumDocument;
import org.radixware.schemas.types.StrDocument;

/**
 * Модуль ARTE. Принимает соединение на серверный сокет и отдает его свободной
 * инстанции ARTE.
 *
 */
public class ArteUnit extends AsyncEventHandlerUnit {

    public int tempLimit = 10000;

    private static enum EConnectionState {

        NEW,
        KEPT;
    }

    private static final int REUSE_SOCKET_TIMEOUT_MILLIS = SystemPropUtils.getIntSystemProp("rdx.unit.arte.keepconnect.millis", 60000);
    //
    private static final int TIC_MILLIS = 1000;
    private static final int MAX_SCHEDULED_CHANNELS = 10000;
    private static final int MIN_INSUF_ARTE_MESSAGE_INTERVAL = 2000;
    private static final int READ_HEADER_TIMEOUT_MILLIS = 30000;
    private static final int SO_TIMEOUT_DURING_SSL_HANDSHAKE_MILLIS = 30000;
    private static final int MAX_HTTP_HEADER_LEN_BYTES = 2048;
    //
    private static final int POKE_PREFETCHER_INTERVAL_MILLIS = 30000;
    private static final int PREFETCHER_INIT_THREADS = 2;
    private static final int PREFETCHER_MAX_THREADS = SystemPropUtils.getIntSystemProp("rdx.arte.unit.max.prefetch.threads", 200);
    private static final int PREFETCHER_THREAD_LIVE_TIME_SEC = 60;
    private static final int PREFETCHER_QUEUE_SIZE = 1000;
    private static final int PREFETCHER_MAX_GROW_STEP = 10;
    private static final int PREFETCH_START_TIMEOUT_MILLIS = 20000;
    private static final int PREFETCHER_START_NEW_THREAD_AFTER_WAIT_MILLIS = 1000;
    private static final int ADJUST_PREFETCHER_PERIOD_MILLIS = 1000;
    //
    private static final String GET_STATUS_HTTP_RQ = "GET /status HTTP/1.1";
    private static final String STATUS_ALIVE = "alive";
    //
    private volatile ArteUnitOptions options;
    private volatile ServerChannel serverChannel;
    private volatile SSLContext sslContext = null;
    private volatile CountDownLatch shutdownLatch = null;//used to wait for currently processed requests during shutdown
    private final AtomicInteger activeArteCount = new AtomicInteger(0);
    private final DbQueries arteUnitDbQueries;
    private final ArrayBlockingQueue<RequestChannel> channelsToClose = new ArrayBlockingQueue<>(MAX_SCHEDULED_CHANNELS);
    private final ArrayBlockingQueue<RequestChannel> channelsToReuse = new ArrayBlockingQueue<>(MAX_SCHEDULED_CHANNELS);
    private final List<RequestChannel> freeChannels = new ArrayList<>();
    private ThreadPoolExecutor prefetcher = null;
    private volatile boolean isWaitingForRequestsToFinish = false;
    private volatile boolean allowKeepConnectOnBusy = false;
    //statistics
    private final IntegralCountStat activeArteStat = new IntegralCountStat(new MetricParameters(null, -1, Instance.WRITE_BASIC_STATS_PERIOD_MILLIS));
    private double lastAvgActiveArteCount = 0;
    //maintenance
    private long lastDbSelfCheckTimeMillis = 0;
    private long lastPokePrefetcherMillis = 0;
    private long lastAdjustPrefetcherMillis = 0;
    //
    private final AtomicLong lastInsufArteNotifyMillis = new AtomicLong();
    //
    private volatile Thread unitThread;

    public ArteUnit(final Instance instModel, final Long id, final String title) {
        super(instModel, id, title);
        arteUnitDbQueries = new DbQueries(this);
    }

    @Override
    public String getUnitTypeTitle() {
        return ArteUnitMessages.ARTE_UNIT_TYPE_TITLE;
    }

    @Override
    public Long getUnitType() {
        return EUnitType.ARTE.getValue();
    }

    final DbQueries getArteUnitDbQueries() {
        return arteUnitDbQueries;
    }

    @Override
    protected void processCommand(UnitCommand unitCommand) {
        try {
            //test for RADIX-7311
            final StrDocument xCommand = unitCommand.getRequest(StrDocument.class);
            if ("load".equals(xCommand.getStr())) {
                final NumDocument numDoc = NumDocument.Factory.newInstance();
                numDoc.setNum(BigDecimal.valueOf(activeArteCount.doubleValue()));
                getInstance().enqueueResponseToUnitCommand(new UnitCommandResponse(unitCommand, numDoc));
                return;
            }
            super.processCommand(unitCommand);
        } catch (Exception ex) {
            getInstance().enqueueResponseToUnitCommand(new UnitCommandResponse(unitCommand, ex));
        }
    }

    @Override
    protected boolean startImpl() throws Exception {
        if (!super.startImpl()) {
            return false;
        }
        unitThread = Thread.currentThread();
        options = arteUnitDbQueries.readOptions();
        if (options == null) {
            throw new RadixError(Messages.ERR_CANT_READ_OPTIONS);
        }
        if (options.sapOptions.getEasKerberosAuthPolicy() != EClientAuthentication.None) {
            getInstance().initKerberos();
        }

        logOptionsChanged(options.toString());
        if (!startServerSap()) {
            return false;
        }

        getDispatcher().waitEvent(new EventDispatcher.TimerEvent(), this, System.currentTimeMillis() + DB_I_AM_ALIVE_PERIOD_MILLIS);

        shutdownLatch = new CountDownLatch(1);
        isWaitingForRequestsToFinish = false;

        freeChannels.clear();
        channelsToClose.clear();
        channelsToReuse.clear();

        activeArteCount.set(0);
        activeArteStat.start(new RegisteredItem<>(System.currentTimeMillis(), 0));
        lastAvgActiveArteCount = 0;
        lastInsufArteNotifyMillis.set(0);

        prefetcher = new ThreadPoolExecutor(PREFETCHER_INIT_THREADS,
                PREFETCHER_MAX_THREADS,
                PREFETCHER_THREAD_LIVE_TIME_SEC,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(PREFETCHER_QUEUE_SIZE),
                new ThreadFactory() {
                    private final AtomicLong prefetcherId = new AtomicLong(0);

                    @Override
                    public Thread newThread(Runnable r) {
                        final Thread t = new Thread(r);
                        t.setName(getFullTitle() + " prefetcher #" + prefetcherId.incrementAndGet() + " " + new Timestamp(System.currentTimeMillis()).toString());
                        return t;
                    }
                });
        prefetcher.allowCoreThreadTimeOut(true);
        prefetcher.setRejectedExecutionHandler(new RejectedExecutionHandler() {

            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                getTrace().putFloodControlled("arte-rejected-prefetch", EEventSeverity.WARNING, Thread.currentThread().getName() + " is out of prefetcher threads", null, null, getEventSource(), -1, false, null);
            }
        });

        allowKeepConnectOnBusy = !System.getProperties().containsKey("rdx.unit.arte.disable.keepconnect.on.busy");

        return true;
    }

    protected void logSockCloseError(final Exception ex) {
        final String exStack = ExceptionTextFormatter.throwableToString(ex);
        getTrace().put(EEventSeverity.WARNING, ArteUnitMessages.ERR_ON_ACCEPTED_SOCKET_CLOSE + exStack, ArteUnitMessages.MLS_ID_ERR_ON_ACCEPTED_SOCKET_CLOSE, new ArrStr(getFullTitle(), exStack), getEventSource(), false);
    }

    protected void closeChannelAsync(final RequestChannel requestChannel, final boolean forced) {
        //unsubscribe should be done in unit thread because getDispatcher is not thread safe
        if (requestChannel != null && requestChannel.getSelectableChannel() != null) {
            getDispatcher().unsubscribe(requestChannel.getSelectableChannel());
        }
        prefetcher.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    closeChannel(requestChannel, forced);
                } catch (InterruptedException ex) {
                    logSockCloseError(ex);
                }
            }
        });
    }

    protected void closeChannel(final RequestChannel requestChannel) throws InterruptedException {
        closeChannel(requestChannel, false);
    }

    protected void closeChannel(final RequestChannel requestChannel, final boolean forced) throws InterruptedException {
        try {
            if (requestChannel != null && requestChannel.getSelectableChannel() != null && Thread.currentThread() == unitThread) {
                getDispatcher().unsubscribe(requestChannel.getSelectableChannel());
            }
            if (!forced && requestChannel instanceof SocketRequestChannel) {
                final Socket socket = ((SocketRequestChannel) requestChannel).getSocket();
                if (socket instanceof SSLSocket) {
                    //[RADIX-4390]
                    //SSLSocket requires i/o streams to stay open until session is gracefully closed in close().
                    //Otherwise client will receive SSLException: Inbound closed before receiving peer's close_notify.
                    try {
                        //channel must be put into blocking mode because data exchange
                        //will take place during ssl session closing
                        socket.getChannel().configureBlocking(true);
                    } catch (IOException e) {
                    }
                } else {
                    try {
                        socket.shutdownOutput();
                    } catch (Throwable e) {
                        if (e instanceof InterruptedException || Thread.currentThread().isInterrupted()) {
                            throw new InterruptedException();
                        }
                        if (!(e instanceof IOException)) {
                            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                            getTrace().put(EEventSeverity.WARNING, ArteUnitMessages.ERR_ON_ACCEPTED_SOCKET_CLOSE + exStack, ArteUnitMessages.MLS_ID_ERR_ON_ACCEPTED_SOCKET_CLOSE, new ArrStr(getFullTitle(), exStack), getEventSource(), false);
                        }
                    }
                    try {
                        socket.shutdownInput();
                    } catch (Throwable e) {
                        if (e instanceof InterruptedException || Thread.currentThread().isInterrupted()) {
                            throw new InterruptedException();
                        }
                        if (!(e instanceof IOException)) {
                            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                            getTrace().put(EEventSeverity.WARNING, ArteUnitMessages.ERR_ON_ACCEPTED_SOCKET_CLOSE + exStack, ArteUnitMessages.MLS_ID_ERR_ON_ACCEPTED_SOCKET_CLOSE, new ArrStr(getFullTitle(), exStack), getEventSource(), false);
                        }
                    }
                }
            }
            if (requestChannel != null && requestChannel.getSelectableChannel() != null) {
                if (requestChannel.getSelectableChannel().isOpen() && !requestChannel.getSelectableChannel().isBlocking()) {
                    requestChannel.getSelectableChannel().configureBlocking(true);
                }
            }
            if (forced) {
                requestChannel.getSelectableChannel().close();
            }
            requestChannel.close();//if forced close happened just double check
            getTrace().put(EEventSeverity.DEBUG, "Connection closed: " + requestChannel.getRemoteAddress(), EEventSource.ARTE_UNIT);
        } catch (Throwable e) {
            if (e instanceof InterruptedException || Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            getTrace().put(EEventSeverity.WARNING, ArteUnitMessages.ERR_ON_ACCEPTED_SOCKET_CLOSE + exStack, ArteUnitMessages.MLS_ID_ERR_ON_ACCEPTED_SOCKET_CLOSE, new ArrStr(getFullTitle(), exStack), getEventSource(), false);
        }
    }

    private boolean startServerSap() throws InterruptedException {
        try {
            if (options.sapOptions.getSoapServiceOptions() != null && options.sapOptions.getSoapServiceOptions().getWsdlSource() != null) {
                //preload wsdl
                ServerSoapUtils.getWsdlUrl(options.sapOptions.getSoapServiceOptions(), getDbConnection(), getTrace().newTracer(EEventSource.ARTE_UNIT.getValue()));
            }
            if (getAddress().getKind() == SapAddress.EKind.INET_SOCKET_ADDRESS) {
                serverChannel = new SocketServerChannel();
            } else if (getAddress().getKind() == SapAddress.EKind.INTERNAL_PIPE_ADDRESS) {
                serverChannel = new PipeServerChannel();
            } else if (getAddress().getKind() == SapAddress.EKind.JMS_ADDRESS) {
                serverChannel = new JmsServerChannel(getTrace().newTracer(EEventSource.UNIT.getValue()));
            }
            serverChannel.open(getAddress());
            serverChannel.getSelectableChannel().configureBlocking(false);
            getDispatcher().waitEvent(new EventDispatcher.AcceptEvent(serverChannel.getSelectableChannel()), this, -1);
            getDispatcher().waitEvent(new EventDispatcher.TimerEvent(), this, System.currentTimeMillis() + DB_I_AM_ALIVE_PERIOD_MILLIS);
            dbSapIsActive();
            getTrace().put(EEventSeverity.EVENT, ArteUnitMessages.SOCKET_LISTENS + getAddress().toString(), ArteUnitMessages.MLS_ID_SOCKET_LISTENS, new ArrStr(getFullTitle(), getAddress().toString()), getEventSource(), false);
            return true;
        } catch (IOException e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            getTrace().put(EEventSeverity.ERROR, ArteUnitMessages.ERR_ON_SOCKET_START + exStack, ArteUnitMessages.MLS_ID_ERR_ON_SOCKET_START, new ArrStr(getFullTitle(), exStack), getEventSource(), false);
            closeServerSocket();
            return false;
        }
    }

    private void dbSapIsActive() throws InterruptedException {
        while (!isShuttingDown()) {
            try {
                final Connection connection = getDbConnection();
                SapQueries.setDbActiveState(getSapId(), connection, true);
                connection.commit();
                return;
            } catch (SQLException e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(getFullTitle(), exStack), getEventSource(), false);
                restoreDbConnection();
            }
        }
    }

    private void dbSapIsNotActive() {
        final Connection dbConnection = getDbConnection();
        if (dbConnection == null ||//DBP-1607 if stopping while unit is trying to restore db connection then dbConnection == null
                options == null) { //RADIX-4366
            return;
        }
        try {
            SapQueries.setDbActiveState(getSapId(), dbConnection, false);
            dbConnection.commit();
        } catch (SQLException e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(getFullTitle(), exStack), getEventSource(), false);
        }
    }

    @Override
    protected void stopImpl() {
        closeServerSocket();

        channelsToClose.addAll(freeChannels);
        freeChannels.clear();
        channelsToReuse.drainTo(channelsToClose);
        processChannelsToClose();

        isWaitingForRequestsToFinish = true;
        if (activeArteCount.get() > 0) {
            try {
                shutdownLatch.await();//wait until all active requests are finished
            } catch (InterruptedException ex) {
                logErrorOnStop(ex);
            }
        }

        channelsToClose.addAll(freeChannels);
        freeChannels.clear();
        channelsToReuse.drainTo(channelsToClose);
        final List<RequestChannel> toForciblyClose = new ArrayList<>(channelsToClose);
        processChannelsToClose();//normal close will be backed up by forced close 

        try {
            terminatePrefetcher();
        } catch (Exception ex) {
            logErrorOnStop(ex);
        }

        for (RequestChannel requestChannel : toForciblyClose) {
            try {
                if (requestChannel != null
                        && requestChannel.getSelectableChannel() != null
                        && requestChannel.getSelectableChannel().isOpen()) {
                    closeChannel(requestChannel, true);
                }
            } catch (Exception ex) {
                logErrorOnStop(ex);
            }
        }

        unitThread = null;

        super.stopImpl();
    }

    private void terminatePrefetcher() {
        if (prefetcher != null) {
            try {
                prefetcher.shutdown();
                if (!prefetcher.awaitTermination(30, TimeUnit.SECONDS)) {
                    prefetcher.shutdownNow();
                }
            } catch (InterruptedException ex) {
                prefetcher.shutdownNow();
            }
            prefetcher = null;
        }
    }

    @Override
    protected void setDbConnection(final Connection dbConnection) {
        arteUnitDbQueries.closeAll();
        super.setDbConnection(dbConnection);
    }

    private void closeServerSocket() {
        dbSapIsNotActive();
        if (serverChannel != null) {
            if (serverChannel.getSelectableChannel() != null) {
                getDispatcher().unsubscribe(new EventDispatcher.AcceptEvent(serverChannel.getSelectableChannel()));
            }
            try {
                serverChannel.close();
            } catch (IOException e) {
                logErrOnSocketClose(e);
            } finally {
                serverChannel = null;
                sslContext = null;
            }
        }
    }

    private void logErrOnSocketClose(final Exception ex) {
        final String exStack = ExceptionTextFormatter.exceptionStackToString(ex);
        getTrace().put(EEventSeverity.WARNING, ArteUnitMessages.ERR_ON_SOCKET_CLOSE + exStack, ArteUnitMessages.MLS_ID_ERR_ON_SOCKET_CLOSE, new ArrStr(getFullTitle(), exStack), getEventSource(), false);
    }

    public String getServiceUri() {
        if (options == null || options.sapOptions == null) {
            return null;
        }
        return options.sapOptions.getServiceUri();
    }

    /**
     * Valid values are from 1 to 9. Other values should be treated as 'default'
     *
     * @return
     */
    public int getRequestsPriority() {
        ArteUnitOptions optionsSnapshot = options;
        if (optionsSnapshot == null) {
            return -1;
        }
        return optionsSnapshot.defaultPriority;
    }

    public long getSapId() {
        if (options == null) {
            return 0;
        }
        return options.sapId;
    }

    protected int getHighArteInstCount() {
        if (options == null) {
            return 0;
        }
        return options.highArteInstCount;
    }

    @Override
    public UnitView newUnitView() {
        return new ArteUnitView(this);
    }

    public List<String> getTrustedCertAliases() {
        if (options == null) {
            return Collections.emptyList();
        }
        return options.sapOptions.getClientCertAliases();
    }

    public final SapAddress getAddress() {
        return options.sapOptions.getAddress();
    }

    public EClientAuthentication getEasKerberosAuthPolicy() {
        return options == null ? EClientAuthentication.None : options.sapOptions.getEasKerberosAuthPolicy();
    }

    public int getActiveArteCount() {
        return activeArteCount.get();
    }

    double getAvgActiveArteCount() {
        IStatValue value;
        synchronized (activeArteStat) {
            value = activeArteStat.flushAndGetLastValue();
        }
        if (value != null) {
            lastAvgActiveArteCount = value.getAvg();
        }
        return lastAvgActiveArteCount;
    }

//maintenance
    @Override
    protected void maintenanceImpl() throws InterruptedException {

        processChannelsToReuse();
        processChannelsToClose();

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        super.maintenanceImpl();

        if (serverChannel instanceof JmsServerChannel) {
            getDispatcher().notify(new AcceptEvent(null));
        }
    }

    private void processChannelsToClose() {
        final List<RequestChannel> toClose = new ArrayList<>();
        channelsToClose.drainTo(toClose);
        for (RequestChannel requestChannel : toClose) {
            try {
                closeChannelAsync(requestChannel, false);
            } catch (Exception ex) {
                getTrace().put(EEventSeverity.ERROR, ExceptionTextFormatter.exceptionStackToString(ex), null, null, getEventSource(), false);
            }
        }
    }

    private void processChannelsToReuse() throws InterruptedException {
        final List<RequestChannel> toReuse = new ArrayList<>();
        channelsToReuse.drainTo(toReuse);
        if (!toReuse.isEmpty()) {
            final long finishTimeMillis = System.currentTimeMillis() + REUSE_SOCKET_TIMEOUT_MILLIS;
            for (RequestChannel requestChannel : toReuse) {
                try {
                    boolean doClose = true;
                    try {
                        if (requestChannel.getSelectableChannel().isOpen()) {
                            try {
                                requestChannel.getSelectableChannel().configureBlocking(false);
                                getDispatcher().waitEvent(new EventDispatcher.ReadEvent(requestChannel.getSelectableChannel(), null), this, finishTimeMillis);
                                freeChannels.add(requestChannel);
                                doClose = false;
                            } catch (CancelledKeyException e) {
                                //канал только что освободился, в селекторе еще осталась прошлая регистрация
                                //следующий select из dispatcher::process() ее почистит
                                //continue;
                                doClose = true;
                            }
                        }
                    } finally {
                        if (doClose) {
                            closeChannelAsync(requestChannel, false);
                        }
                    }
                } catch (Exception ex) {
                    getTrace().put(EEventSeverity.ERROR, ExceptionTextFormatter.exceptionStackToString(ex), null, null, getEventSource(), false);
                }
            }
        }
    }

    private void logErrOnSocketIO(final Throwable ex) {
        final String exStack = ExceptionTextFormatter.exceptionStackToString(ex);
        getTrace().put(EEventSeverity.ERROR, ArteUnitMessages.ERR_ON_SOCKET_IO + exStack, ArteUnitMessages.MLS_ID_ERR_ON_SOCKET_IO, new ArrStr(getFullTitle(), exStack), getEventSource(), false);
    }

    private void adjustPrefetcher() {
        if (prefetcher.getCorePoolSize() > PREFETCHER_INIT_THREADS && prefetcher.getActiveCount() <= PREFETCHER_INIT_THREADS) {
            prefetcher.setCorePoolSize(PREFETCHER_INIT_THREADS);
            return;
        }
        final long curMillis = System.currentTimeMillis();
        int waitingCount = 0;
        final List<PrefetcherRunnable> toRemove = new ArrayList<>();
        for (Runnable runnable : prefetcher.getQueue()) {
            if (runnable instanceof PrefetcherRunnable) {
                final PrefetcherRunnable prefetcherRunnable = (PrefetcherRunnable) runnable;
                if (curMillis - prefetcherRunnable.createTimeMillis > PREFETCHER_START_NEW_THREAD_AFTER_WAIT_MILLIS) {
                    if (curMillis - prefetcherRunnable.createTimeMillis > PREFETCH_START_TIMEOUT_MILLIS) {
                        toRemove.add(prefetcherRunnable);
                    } else {
                        waitingCount++;
                    }
                }
            }
        }
        for (PrefetcherRunnable pr : toRemove) {
            if (prefetcher.getQueue().remove(pr)) {
                try {
                    requestCloseChannel(pr.getRawRequestChannel());
                } catch (InterruptedException ex) {
                    LogFactory.getLog(getClass()).warn("error", ex);
                }
            }
        }
        if (waitingCount > 0 && prefetcher.getCorePoolSize() < PREFETCHER_MAX_THREADS) {
            final int growStep = Math.min(waitingCount, PREFETCHER_MAX_GROW_STEP);
            prefetcher.setCorePoolSize(prefetcher.getCorePoolSize() + growStep);
        }
    }

    @Override
    public void onEvent(final Event ev) {
        if (ev instanceof TimerEvent) {
            final long curMillis = System.currentTimeMillis();
            //sap selfcheck
            if (curMillis - lastDbSelfCheckTimeMillis >= DB_I_AM_ALIVE_PERIOD_MILLIS) {
                try {
                    final Connection dbConnection = getDbConnection();
                    if (dbConnection != null) {
                        SapQueries.setDbSelfCheck(getSapId(), dbConnection);
                        dbConnection.commit();
                    }
                } catch (SQLException e) {
                    final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                    getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(getFullTitle(), exStack), getEventSource(), false);
                }
                lastDbSelfCheckTimeMillis = curMillis;
            }
            if (curMillis - lastPokePrefetcherMillis > POKE_PREFETCHER_INTERVAL_MILLIS) {
                prefetcher.execute(new Runnable() {

                    @Override
                    public void run() {
                        //noop
                    }
                });
                lastPokePrefetcherMillis = curMillis;
            }
            if (curMillis - lastAdjustPrefetcherMillis > ADJUST_PREFETCHER_PERIOD_MILLIS) {
                adjustPrefetcher();
                lastAdjustPrefetcherMillis = curMillis;
            }

            if (!isShuttingDown()) {
                getDispatcher().waitEvent(new EventDispatcher.TimerEvent(), this, System.currentTimeMillis() + TIC_MILLIS);
            }
        } else if (ev instanceof AcceptEvent) {
            RequestChannel acceptedChannel = null;
            boolean doClose = true;
            try {
                acceptedChannel = serverChannel.acceptRequest();
                if (acceptedChannel == null) {
                    throw new IOException("Accept event is reported, but there is no accepted connection. Maybe, client has disconnected.");
                }
                processRequest(acceptedChannel, EConnectionState.NEW);
                doClose = false;
            } catch (Throwable e) {
                doClose = true;
                if (!(e instanceof InterruptedException) && !Thread.currentThread().isInterrupted()) {
                    logErrOnSocketIO(e);
                }
            } finally {
                if (acceptedChannel != null && doClose) {
                    closeChannelAsync(acceptedChannel, false);
                }
            }
            if (!isShuttingDown() && !Thread.currentThread().isInterrupted() && serverChannel.isOpened()) {
                getDispatcher().waitEvent(new EventDispatcher.AcceptEvent(serverChannel.getSelectableChannel()), this, -1);
            }
        } else if (ev instanceof EventDispatcher.ReadEvent) {
            final RequestChannel requestChannel = findKeptChannel((EventDispatcher.ReadEvent) ev);
            if (requestChannel == null) {
                getTrace().put(EEventSeverity.WARNING, "Unable to find kept channel for read event", EEventSource.ARTE_UNIT);
            } else {
                freeChannels.remove(requestChannel);
                boolean doClose = ev.isExpired;
                try {
                    if (!doClose) {
                        try {
                            getDispatcher().unsubscribe(requestChannel.getSelectableChannel());
                            requestChannel.getSelectableChannel().configureBlocking(true);
                            processRequest(requestChannel, EConnectionState.KEPT);
                        } catch (Throwable e) {
                            doClose = true;
                            if (!(e instanceof InterruptedException) && !Thread.currentThread().isInterrupted()) {
                                logErrOnSocketIO(e);
                            }
                        }
                    }
                } finally {
                    if (doClose) {
                        closeChannelAsync(requestChannel, false);
                    }
                }
            }
        } else {
            throw new IllegalUsageError("Invalid event " + ev);
        }
    }

    private RequestChannel findKeptChannel(final EventDispatcher.ReadEvent ev) {
        if (ev == null || ev.getSource() == null) {
            return null;
        }
        for (RequestChannel channel : freeChannels) {
            if (ev.getSource() == channel.getSelectableChannel()) {
                return channel;
            }
        }
        return null;
    }

    private void processRequest(final RequestChannel rawChannel, final EConnectionState connectionState) throws CertificateUtilsException {
        if (connectionState == EConnectionState.NEW) {
            getTrace().put(EEventSeverity.DEBUG, "Connection accepted: " + rawChannel.getRemoteAddress(), EEventSource.ARTE_UNIT);
        } else {
            getTrace().put(EEventSeverity.DEBUG, "Read from kept connection: " + rawChannel.getRemoteAddress(), EEventSource.ARTE_UNIT);
        }
        final boolean shouldInitSsl;
        final SecurityOptions securityOptions;
        if (connectionState == EConnectionState.NEW && options.sapOptions.getSecurityProtocol() == EPortSecurityProtocol.SSL) {
            shouldInitSsl = true;
            if (sslContext == null) {
                sslContext = CertificateUtils.prepareServerSslContext(options.sapOptions.getServerKeyAliases(), options.sapOptions.getClientCertAliases());
            }
            securityOptions = createSecurityOptions(sslContext, options.sapOptions);
        } else {
            shouldInitSsl = false;
            securityOptions = null;
        }
        prefetcher.execute(new PrefetcherRunnable() {

            @Override
            public RequestChannel getRawRequestChannel() {
                return rawChannel;
            }

            @Override
            public void run() {
                try {
                    boolean autoClose = true;
                    RequestChannel channel = rawChannel;
                    try {
                        channel.getSelectableChannel().configureBlocking(true);

                        if (shouldInitSsl) {
                            channel.setReadTimeout(SO_TIMEOUT_DURING_SSL_HANDSHAKE_MILLIS);
                            channel = new SocketRequestChannel(SslUtils.createSslSocket(((SocketRequestChannel) rawChannel).getSocket(), sslContext, securityOptions));
                        }

                        channel.setReadTimeout(READ_HEADER_TIMEOUT_MILLIS);

                        BufferedInputStream bis = new BufferedInputStream(channel.getInputStream());
                        bis.mark(MAX_HTTP_HEADER_LEN_BYTES);
                        Map<String, String> headers = new HashMap<>();
                        HttpFormatter.readHeader(bis, headers);

                        final String httpRqString = headers.get(HttpFormatter.REQUEST_LINE_HEADER_KEY);

                        final boolean keepAlive = HttpFormatter.getKeepConnectionAlive(headers);

                        if (httpRqString != null && httpRqString.startsWith(GET_STATUS_HTTP_RQ)) {
                            notifyStatus(channel, STATUS_ALIVE, keepAlive);
                            if (keepAlive) {
                                freeChannel(channel);
                                autoClose = false;
                            }
                            return;
                        }

                        boolean keepConnectOnBusy = keepAlive && allowKeepConnectOnBusy;

                        bis.reset();
                        int priority = HttpFormatter.getRadixPriorityFromHeaders(headers);
                        if (priority == -1) {
                            priority = getRequestsPriority();
                        }
                        final long version = SrvRunParams.getIsEasVerChecksOn() ? HttpFormatter.getRadixVersionFromHeaders(headers) : -1;
                        final ArteRequest request = new ArteRequest(ArteUnit.this, channel, bis, priority, new ArteCallback(ArteUnit.this), version, headers);
                        final int newUsedArteCount = activeArteCount.incrementAndGet();
                        if (newUsedArteCount <= options.highArteInstCount && newUsedArteCount <= tempLimit) {
                            boolean wasException = false;
                            try {
                                final ArtePool.ArteCaptureInfo captureInfo = getInstance().getArtePool().process(request);
                                getTrace().put(EEventSeverity.DEBUG, "Request handed to ARTE #" + captureInfo.arteInstance.getSeqNumber() + " (priority=" + priorityAsStr(priority) + ", rq version=" + version + ", process version: " + captureInfo.actualVersion + ", conn: " + rawChannel.getRemoteAddress() + ")", EEventSource.ARTE_UNIT);
                                autoClose = false;
                            } catch (InsufficientArteInstanceCountException t) {
                                wasException = true;
                                notifyInstanceArteLimitExceeded(channel, keepConnectOnBusy);
                                autoClose = false;
                            } catch (InterruptedException | RuntimeException ex) {
                                wasException = true;
                                throw ex;
                            } finally {
                                if (wasException) {
                                    activeArteCount.decrementAndGet();
                                }
                            }
                        } else {
                            activeArteCount.decrementAndGet();
                            final long lastWarningMillis = lastInsufArteNotifyMillis.get();
                            if (System.currentTimeMillis() - lastWarningMillis > MIN_INSUF_ARTE_MESSAGE_INTERVAL) {
                                if (lastInsufArteNotifyMillis.compareAndSet(lastWarningMillis, System.currentTimeMillis())) {
                                    getTrace().put(EEventSeverity.WARNING, ArteUnitMessages.ARTE_INST_USAGE_LIMIT_EXCEEDED, ArteUnitMessages.MLS_ID_ARTE_INST_USAGE_LIMIT_EXCEEDED, null, EEventSource.ARTE_UNIT, false);
                                }
                            }
                            notifyUnitArteLimitExceeded(channel, keepConnectOnBusy);
                            autoClose = false;
                        }
                    } finally {
                        if (autoClose) {
                            requestCloseChannel(channel);
                        }
                    }
                } catch (IOException ex) {
                    if (!(ex instanceof EOFException && connectionState == EConnectionState.KEPT)) {
                        logErrOnSocketIO(ex);
                    }
                } catch (InterruptedException ex) {
                    //ignore
                }
            }
        });
    }

    private String priorityAsStr(final int priority) {
        try {
            return EPriority.getForValue(Long.valueOf(priority)).getName();
        } catch (Exception ex) {
            return String.valueOf(priority);
        }
    }

    private SecurityOptions createSecurityOptions(final SSLContext contextSnapshot, final SapOptions sapOptionsSnapshot) {
        return new SecurityOptions() {

            @Override
            public EPortSecurityProtocol getSecurityProtocol() {
                return sapOptionsSnapshot.getSecurityProtocol();
            }

            @Override
            public SSLContext getSSLContext() throws CertificateUtilsException {
                return contextSnapshot;
            }

            @Override
            public EClientAuthentication getClientAuthMode() {
                return sapOptionsSnapshot.getCheckClientCert();
            }

            @Override
            public List<String> getCipherSuites() {
                return sapOptionsSnapshot.getCipherSuites();
            }
        };
    }

    @Override
    protected void clearBasicStats() {
        synchronized (activeArteStat) {
            activeArteStat.start(new RegisteredItem<>(System.currentTimeMillis(), 0));
            lastAvgActiveArteCount = 0;
        }
        super.clearBasicStats();
    }

    @Override
    protected void writeBasicStats() throws SQLException, InterruptedException {
        arteUnitDbQueries.writeBasicStats();
    }

    @Override
    protected void rereadOptionsImpl() throws SQLException, InterruptedException {
        final ArteUnitOptions newOptions = arteUnitDbQueries.readOptions();
        if (newOptions == null || newOptions.equals(options)) {
            //если серверный сокет не открыт пытаемся его запустить, даже если настройки не изменились
            if (!serverChannel.isOpened()) {
                restartServerSocket();
            }
            return;
        }
        logOptionsChanged(newOptions.toString());
        applyOptions(newOptions);
        if (newOptions.sapOptions.getEasKerberosAuthPolicy() != EClientAuthentication.None) {
            getInstance().initKerberos();
        }
    }

    private void applyOptions(final ArteUnitOptions newOptions) throws InterruptedException {
        final boolean bNeedSocketRestart = (!serverChannel.isOpened() || options.sapId != newOptions.sapId || !options.sapOptions.equals(newOptions.sapOptions));
        final boolean bNeedRecreateSslContext = (options.sapOptions.getKeystoreModificationTime() < newOptions.sapOptions.getKeystoreModificationTime());
        options = newOptions;

        if (bNeedRecreateSslContext) {
            try {
                sslContext = CertificateUtils.prepareServerSslContext(options.sapOptions.getServerKeyAliases(), options.sapOptions.getClientCertAliases());
                getTrace().put(EEventSeverity.EVENT, ArteUnitMessages.SERVER_KEYSTORE_WAS_REREAD, null, null, getEventSource(), false);
            } catch (CertificateUtilsException e) {
                getTrace().put(EEventSeverity.ERROR, ArteUnitMessages.SSL_CONTEXT_CREATION_ERROR + ExceptionTextFormatter.exceptionStackToString(e), null, null, getEventSource(), false);
            }
        }

        if (bNeedSocketRestart) {
            restartServerSocket();
        }

    }

    private void restartServerSocket() throws InterruptedException {
        getTrace().debug("Restarting server socket", getEventSource(), false);
        closeServerSocket();
        if (!startServerSap()) //RADIX-3114
        {
            requestStopAndPostponedRestart("unable to restart server socket");
        }
    }

//	Event source
    @Override
    public String getEventSource() {
        return EEventSource.ARTE_UNIT.getValue();
    }

    public void freeChannel(final RequestChannel requestChannel) throws InterruptedException {
        if (requestChannel == null) {
            return;
        }
        if (isShuttingDown() || !requestChannel.getSelectableChannel().isOpen()) {
            requestCloseChannel(requestChannel);
        } else {
            channelsToReuse.put(requestChannel);
        }
        getDispatcher().wakeup();
    }

    public void requestCloseChannel(final RequestChannel requestChannel) throws InterruptedException {
        if (requestChannel != null) {
            channelsToClose.put(requestChannel);
            final EventDispatcher dispatcher = getDispatcher();
            if (dispatcher != null) {
                dispatcher.wakeup();
            }
        }
    }

    private void notifyInstanceArteLimitExceeded(final RequestChannel channel, boolean keepConnect) throws IOException, InterruptedException {
        sendFault(channel, ServiceProcessFault.FAULT_CODE_SERVER_BUSY, "Instance Arte Limit Exceeded", new SoapFormatter.DefaultFaultDetailWriter("Service Access Point at " + getAddress().toString() + " is busy (instance ARTE usage limit exceeded)", null, null), keepConnect);
    }

    private void notifyUnitArteLimitExceeded(final RequestChannel channel, boolean keepConnect) throws IOException, InterruptedException {
        sendFault(channel, ServiceProcessFault.FAULT_CODE_SERVER_BUSY, "Unit Arte Limit Exceeded", new SoapFormatter.DefaultFaultDetailWriter("Service Access Point at " + getAddress().toString() + " is busy (unit ARTE usage limit exceeded)", null, null), keepConnect);
    }

    private void notifyStatus(final RequestChannel channel, final String status, final boolean keepConnect) {
        try {
            final OutputStream out = channel.getOutputStream();
            final byte[] rsBody = status.getBytes("UTF-8");
            final String header = HttpFormatter.prepareResponseHeader("200", keepConnect, null, rsBody.length);
            out.write(header.getBytes("UTF-8"));
            out.write(rsBody);
            out.flush();
            getTrace().put(EEventSeverity.DEBUG, "Processed status request: " + channel.getRemoteAddress(), EEventSource.ARTE_UNIT);
        } catch (IOException ex) {
            //ignore
        }
    }

    private void sendFault(final RequestChannel channel, final String faultCode, final String faultString, final SoapFormatter.FaultDetailWriter faultWriter, final boolean keepConnect) {
        boolean canKeepConnect = true;
        try {
            final OutputStream out = channel.getOutputStream();
            SoapFormatter.sendFault(SoapFormatter.prepareFault(faultCode, faultString, faultWriter, null), out, keepConnect);
            getTrace().put(EEventSeverity.DEBUG, "Fault '" + faultString + "' sent: " + channel.getRemoteAddress(), EEventSource.ARTE_UNIT);
        } catch (IOException ex) {
            canKeepConnect = false;
            //ignore
        } finally {
            if (keepConnect && canKeepConnect) {
                try {
                    freeChannel(channel);
                } catch (InterruptedException ex) {
                    //ignore
                }
            } else {
                try {
                    requestCloseChannel(channel);
                } catch (InterruptedException ex) {
                    //ignore
                }
            }
        }
    }

    private static class ArteCallback implements IArteRequestCallback {

        private final CountDownLatch shutdownLatch;
        private final ArteUnit unit;

        public ArteCallback(final ArteUnit unit) {
            this.unit = unit;
            this.shutdownLatch = unit.shutdownLatch;
        }

        @Override
        public void sessionStarted() {
            synchronized (unit.activeArteStat) {
                unit.activeArteStat.append(new RegisteredItem<>(System.currentTimeMillis(), unit.activeArteStat.getCurrentValue() + 1));
            }
        }

        @Override
        public void sessionFinished() {
            synchronized (unit.activeArteStat) {
                unit.activeArteStat.append(new RegisteredItem<>(System.currentTimeMillis(), unit.activeArteStat.getCurrentValue() - 1));
            }
            final int nowUsedCount = unit.activeArteCount.decrementAndGet();
            if (nowUsedCount == 0 && unit != null && unit.isWaitingForRequestsToFinish) {
                shutdownLatch.countDown();
            }
        }
    }

    private static class ArteRequest implements IArteRequest {

        private final ArteUnit unit;
        private final IArteRequestCallback callback;
        private final RequestChannel requestChannel;
        private final SapOptions sapOptions;
        private final int radixPriority;
        private final InputStream overriddenInput;
        private volatile PriorityResourceManager.Ticket countTicket;
        private volatile PriorityResourceManager.Ticket activeTicket;
        private final long version;
        private final Map<String, String> headers;
        private final long createTimeMillis = System.currentTimeMillis();

        public ArteRequest(final ArteUnit unit, final RequestChannel channel, final InputStream overridenInput, final int radixPriority, final IArteRequestCallback callback, final long version, final Map<String, String> headers) {
            this.unit = unit;
            this.callback = callback;
            this.requestChannel = channel;
            this.sapOptions = unit.options.sapOptions;//take snapshot of the mutable field
            this.radixPriority = radixPriority;
            this.overriddenInput = overridenInput;
            this.version = version;
            this.headers = Collections.unmodifiableMap(headers == null ? new HashMap<String, String>() : headers);
        }

        @Override
        public IArteRequestCallback getCallback() {
            return callback;
        }

        @Override
        public ArteUnit getUnit() {
            return unit;
        }

        @Override
        public RequestChannel getRequestChannel() {
            return requestChannel;
        }

        @Override
        public String getClientId() {
            return requestChannel.getRemoteAddressUnchangingPart();
        }

        @Override
        public SapOptions getSapOptions() {
            return sapOptions;
        }

        @Override
        public int getRadixPriority() {
            return radixPriority;
        }

        @Override
        public InputStream getOverriddenInput() {
            return overriddenInput;
        }

        @Override
        public PriorityResourceManager.Ticket getCountTicket() {
            return countTicket;
        }

        @Override
        public void setCountTicket(PriorityResourceManager.Ticket ticket) {
            this.countTicket = ticket;
        }

        @Override
        public PriorityResourceManager.Ticket getActiveTicket() {
            return activeTicket;
        }

        @Override
        public void setActiveTicket(PriorityResourceManager.Ticket ticket) {
            this.activeTicket = ticket;
        }

        @Override
        public long getCreateTimeMillis() {
            return createTimeMillis;
        }

        @Override
        public long getVersion() {
            return version;
        }

        @Override
        public Map<String, String> getHeaders() {
            return headers;
        }

    }

    private static abstract class PrefetcherRunnable implements Runnable {

        public final long createTimeMillis = System.currentTimeMillis();

        public abstract RequestChannel getRawRequestChannel();
    }
}
