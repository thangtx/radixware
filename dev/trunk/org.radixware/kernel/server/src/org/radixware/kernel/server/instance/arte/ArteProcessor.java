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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.nio.channels.SelectableChannel;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLSocket;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.enums.EClientAuthentication;
import org.radixware.kernel.common.enums.EEventContextType;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EHttpParameter;
import org.radixware.kernel.common.enums.EPortSecurityProtocol;
import org.radixware.kernel.common.enums.EPriority;
import org.radixware.kernel.common.enums.ESoapMessageType;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.exceptions.*;
import org.radixware.kernel.common.sc.SapClientOptions;
import org.radixware.kernel.common.sc.ServiceClient;
import org.radixware.kernel.common.soap.IServerMessageProcessorFactory;
import org.radixware.kernel.common.soap.IServerSoapMessageProcessor;
import org.radixware.kernel.common.soap.ProcessException;
import org.radixware.kernel.common.soap.RadixSoapHelper;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.trace.TraceItem;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.HttpFormatter;
import org.radixware.kernel.common.utils.SoapFormatter;
import org.radixware.kernel.common.utils.io.pipe.PipeAddress;
import org.radixware.kernel.common.utils.net.RequestChannel;
import org.radixware.kernel.common.utils.net.SapAddress;
import org.radixware.kernel.common.utils.net.SocketRequestChannel;
import org.radixware.kernel.common.utils.net.SocketsDisconnectWatcher;
import org.radixware.kernel.server.SrvRunParams;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.arte.ArteSocket;
import org.radixware.kernel.server.arte.services.aas.ArteAccessService;
import org.radixware.kernel.server.exceptions.ArteSocketException;
import org.radixware.kernel.server.exceptions.ArteSocketTimeout;
import org.radixware.kernel.server.sap.SapOptions;
import org.radixware.kernel.server.sc.ServerServicesClient;
import org.radixware.kernel.server.soap.DefaultServerMessageProcessorFactory;
import org.radixware.kernel.server.trace.FileLog;
import org.radixware.kernel.server.trace.ServerTrace;
import org.radixware.kernel.server.trace.TraceBuffer;
import org.radixware.kernel.server.trace.TraceProfiles;
import org.radixware.kernel.server.units.ServerItemView;
import org.radixware.kernel.server.units.arte.ArteUnit;
import org.radixware.kernel.server.utils.PriorityResourceManager;
import org.radixware.schemas.aasWsdl.InvokeDocument;

public class ArteProcessor extends ArteSocket implements Runnable, IArteProvider {

    private static final String REASON_THREAD_WAS_INTERRUPTED = "thread was interrupted";
    private static final String REASON_UNEXPECTED_EXCEPTION = "unexpected exception";
    private static final String REASON_STOP_REQUESTED = "stop requested";
    private static final String REASON_LIFE_TIME_HAS_EXPIRED = "life time has expired";
    private static final String REASON_CLOSED_DB_CONNECTION = "database connection is closed";
    private static final String REASON_UNKNOWN = "unknown";
    private static final Long THIS_SYSTEM_ID = Long.valueOf(1);
    private static final long AUTO_SHUTDOWN_INTERVAL_MILLIS = ArtePool.ARTE_SHUTDOWN_INTERVAL_ON_RELOAD_MILLIS;
    private Arte arte;
    private final ArteInstance arteInstance;
    private SocketsDisconnectWatcher socketWatcher;
    private BufferedOutputStream socketOutputStream;
    private BufferedInputStream socketInputStream;
    private IServerSoapMessageProcessor messageProcessor;
    private IServerMessageProcessorFactory processorFactory;
    private SapOptions requestSapOptions;
    private boolean keepConnectionAlive;
    private ServerServicesClient servicesClient;
    volatile private String clientAddress;
    private LocalTracer arteCommunicatorTracer;
    private LocalTracer arteThreadTracer;
    private volatile TraceProfiles traceProfiles;// accessed only from ARTE thread
    private IArteRequest request;
    private Object guiTraceTargetHandler = null;
    private Object fileTraceTargetHandler = null;

    private static enum ERequestKind {

        INIT,
        UPDATE,
        NORMAL,
        MAINTENANCE;
    }

    ArteProcessor(final ArteInstance inst) {
        this.arteInstance = inst;
        traceProfiles = inst.getTrace().getProfiles();
    }
    private boolean arteInited = false;

    /**
     * Should be called only from Arte Thread
     */
    private void actualizeGuiAndFileTraceProfiles() {
        final String overridenGuiProfile = ArteServerTrace.getLastOverridenGuiProfile();
        TraceProfiles profiles = arte.getArteTraceProfiles();
        if (overridenGuiProfile != null) {
            if (!overridenGuiProfile.equals(arteInstance.getTrace().getOverridenGuiProfile())) {
                arteInstance.getTrace().doOverrideGuiProfile(overridenGuiProfile);
            }
            if (!overridenGuiProfile.equals(profiles.getGuiTraceProfile())) {
                profiles = new TraceProfiles(profiles.getDbTraceProfile(), profiles.getFileTraceProfile(), overridenGuiProfile);
            }
        }
        if (!profiles.equals(traceProfiles)) {
            traceProfiles = profiles;
            arteInstance.getTrace().setProfiles(//let's change current gui trace profile of TraceView
                    new TraceProfiles(
                            EEventSeverity.NONE.getName(), //ARTE writes to eventlog itself (without sync)
                            EEventSeverity.NONE.getName(), //ARTE writes to eventlog itself (without sync)
                            profiles.getGuiTraceProfile()));
            if (guiTraceTargetHandler != null)//если есть view с трассой
            {
                arte.getTrace().changeTargetProfile(guiTraceTargetHandler, traceProfiles.getGuiTraceProfile());
            }
            arte.getTrace().changeTargetProfile(fileTraceTargetHandler, traceProfiles.getFileTraceProfile());
        }
    }

    public TraceProfiles getTraceProfiles() {
        return traceProfiles;
    }

    public LocalTracer getArteThreadLocalTracer() {
        return arteThreadTracer;
    }

    @Override
    public Arte getArte() {
        return arte;
    }

    /**
     * Should be called only from Arte Thread
     */
    private void initArte() {
        try {
            arteInstance.setDbConnection(arteInstance.getInstance().openNewArteDbConnection(arteInstance));
            arte.init(arteInstance.getDbConnection(), this, arteInstance.getSeqNumber());
            arteInstance.setDbLog(arte.getTrace().getDbLog());
            arteInstance.applyNewFileLogOptions();
            fileTraceTargetHandler = arte.getTrace().addTargetBuffer(
                    traceProfiles.getFileTraceProfile(),
                    new TraceBuffer() {
                        @Override
                        public void put(final TraceItem item) {
                            final FileLog log = arteInstance.getFileLog();
                            if (log != null) {
                                log.log(item);
                            }
                        }
                    });
            actualizeGuiAndFileTraceProfiles();
            arteCommunicatorTracer = new LocalTracer() {
                private final EEventSource source = EEventSource.ARTE_COMMUNICATOR;

                @Override
                public void put(EEventSeverity severity, String localizedMess, String code, List<String> words, boolean isSensitive) {
                    if (code == null) {
                        arte.getTrace().put(severity, localizedMess, source.getValue(), isSensitive);
                    } else {
                        arte.getTrace().put(severity, code, words, source.getValue(), isSensitive);
                    }
                }

                @Override
                public long getMinSeverity() {
                    return arte.getTrace().getMinSeverity(source);
                }

                @Override
                public long getMinSeverity(String eventSource) {
                    return arte.getTrace().getMinSeverity(eventSource);
                }
            };
            arteThreadTracer = new LocalTracer() {
                private final EEventSource source = EEventSource.ARTE;

                @Override
                public void put(EEventSeverity severity, String localizedMess, String code, List<String> words, boolean isSensitive) {
                    if (code == null) {
                        arte.getTrace().put(severity, localizedMess, source.getValue(), isSensitive);
                    } else {
                        arte.getTrace().put(severity, code, words, source.getValue(), isSensitive);
                    }
                }

                @Override
                public long getMinSeverity() {
                    return arte.getTrace().getMinSeverity(source);
                }

                @Override
                public long getMinSeverity(String eventSource) {
                    return arte.getTrace().getMinSeverity(eventSource);
                }
            };
            Thread.currentThread().setName(Thread.currentThread().getName() + " (dbSID=" + arte.getDbSid() + ")");
            arteInited = true;
        } catch (Throwable e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            arte.getTrace().put(ArteMessages.MLS_ID_CANT_INIT_ARTE, new ArrStr(arteInstance.getTitle(), exStack));
            arteInstance.getInstance().getTrace().put(EEventSeverity.ERROR, ArteMessages.CANT_INIT_ARTE + " \"" + arteInstance.getTitle() + "\": \n" + exStack, ArteMessages.MLS_ID_CANT_INIT_ARTE, new ArrStr(arteInstance.getTitle(), exStack), EEventSource.ARTE, false);
        }
    }

    @Override
    public void run() {
        final long kernelStartMillis = System.currentTimeMillis();
        String stopReason = null;
        PriorityResourceManager.Ticket lastCountTicket = null;
        try {
            arte = new Arte(arteInstance.getInstance());
            if (SrvRunParams.getIsGuiOn()) {
                createView();//создадим view чтобы трасса не терялась
            }
            initArte();
            if (!arteInited) {
                return;
            }
            socketWatcher = new SocketsDisconnectWatcher();
            if (Thread.currentThread().isInterrupted()) {
                stopReason = REASON_THREAD_WAS_INTERRUPTED;
                return;
            }

            boolean adsInitCompleted = false;
            final String initialThreadName = Thread.currentThread().getName();
            final SimpleDateFormat rqStartProcessDateFormat = new SimpleDateFormat("HH:mm:ss.SSS");

            NEXT_RQ:
            for (;;) {
                synchronized (getNextRqSemaphore()) {
                    final ERequestKind requestKind;
                    if (adsInitCompleted) {
                        arteInstance.getStatistic().onStartWait();
                        arteInstance.setState(ArteInstance.EState.FREE);
                        if (lastCountTicket != null) {
                            arteInstance.getInstance().getArtePool().release(lastCountTicket);
                            lastCountTicket = null;
                        }
                        arteInstance.setCachedVersions(arte.getDefManager().getCachedVersions());
                        Thread.currentThread().setName(initialThreadName);
                        while (arteInstance.getState() == ArteInstance.EState.FREE) {
                            getNextRqSemaphore().wait();
                        }
                        request = arteInstance.getRequest();
                        final String rqUnit = request == null || request.getUnit() == null ? "null" : request.getUnit().getId() + ") " + request.getUnit().getTitle();
                        final String rqRemote = request == null || request.getRequestChannel() == null ? "null" : request.getRequestChannel().getRemoteAddress();
                        Thread.currentThread().setName(initialThreadName + "; rqStartTime=" + rqStartProcessDateFormat.format(new Date(System.currentTimeMillis())) + "; rqUnit='" + rqUnit + "'" + "; rqRemote='" + rqRemote + "'");
                        if (request == null) {//maintenance or update
                            if (arte.getDefManager().getCachedVersions().get(0) < arteInstance.getInstance().getLatestVersion()) {
                                request = createAdsInitRequest(arteInstance.getInstance().getLatestVersion());
                                requestKind = ERequestKind.UPDATE;
                            } else {
                                requestKind = ERequestKind.MAINTENANCE;
                            }
                        } else {
                            requestKind = ERequestKind.NORMAL;
                        }
                    } else {
                        request = createAdsInitRequest(arte.getDefManager().getLastVersion());
                        requestKind = ERequestKind.INIT;
                    }
                    arte.beforeRequestProcessing(request);
                    try {
                        arteInstance.getStatistic().onStartWork();
                        if (request != null) {
                            lastCountTicket = request.getCountTicket();
                        }
                        final RequestChannel requestChannel = request != null ? request.getRequestChannel() : null;
                        if (requestChannel != null) {
                            clientAddress = requestChannel.getRemoteAddress();
                            arte.getTrace().put(ArteMessages.MLS_ID_CLIENT_CONNECTED, new ArrStr(arteInstance.getTitle(), clientAddress));
                        }
                        arteInstance.getInstance().getInstanceMonitor().arteSessionStarted(request);
                        if (request != null && request.getCallback() != null) {
                            try {
                                request.getCallback().sessionStarted();
                            } catch (Throwable t) {
                                arteInstance.getTrace().put(EEventSeverity.ERROR, ArteMessages.ERR_ON_SESS_START_NOTIFICATION + ExceptionTextFormatter.exceptionStackToString(t), null, null, EEventSource.ARTE, false);
                            }
                        }
                        try {
                            if (Thread.currentThread().isInterrupted()) {
                                stopReason = REASON_THREAD_WAS_INTERRUPTED;
                                return;
                            }

                            int threadPriority = Thread.NORM_PRIORITY;
                            if (request != null && request.getRadixPriority() >= 1 && request.getRadixPriority() <= 9) {
                                threadPriority = arteInstance.getInstance().convertRadixPriorityToSystemPriority(request.getRadixPriority());
                            }
                            if (threadPriority != Thread.currentThread().getPriority()) {
                                Thread.currentThread().setPriority(threadPriority);
                            }

                            if (request != null && requestChannel != null) { // client request should be processed
                                if (requestKind == ERequestKind.NORMAL && arte.getInstance().isUseActiveArteLimits()) {
                                    arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_WAIT_ACTIVE);
                                    try {
                                        final PriorityResourceManager.Ticket activeTicket = arte.getInstance().getActiveArteResourceManager().requestTicket(request.getRadixPriority(), -1, PriorityResourceManager.EQueuePriority.FIRST);
                                        request.setActiveTicket(activeTicket);
                                    } finally {
                                        arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_WAIT_ACTIVE);
                                    }
                                }
                                try {
                                    socketOutputStream = new BufferedOutputStream(requestChannel.getOutputStream());
                                    if (request.getOverriddenInput() != null) {
                                        socketInputStream = new BufferedInputStream(request.getOverriddenInput());
                                    } else {
                                        socketInputStream = new BufferedInputStream(requestChannel.getInputStream());
                                    }
                                } catch (IOException e) {
                                    if (Thread.currentThread().isInterrupted()) {
                                        stopReason = REASON_THREAD_WAS_INTERRUPTED;
                                        return;
                                    }
                                    arte.getTrace().put(ArteMessages.MLS_ID_ERR_ON_SOCKET_IO, new ArrStr(arteInstance.getTitle(), ExceptionTextFormatter.exceptionStackToString(e)));
                                    stopReason = getUnexpectedExceptionReason(e);
                                    return;
                                }
                                requestSapOptions = request.getSapOptions();
                                processorFactory = new DefaultServerMessageProcessorFactory(requestSapOptions, arteCommunicatorTracer, arte.getObjectCache());
                                keepConnectionAlive = false;
                                actualizeGuiAndFileTraceProfiles();
                                if (requestChannel instanceof SocketRequestChannel && ((SocketRequestChannel) requestChannel).getSocket() instanceof SSLSocket) {
                                    setSslSession(((SSLSocket) ((SocketRequestChannel) requestChannel).getSocket()).getSession());
                                } else {
                                    setSslSession(null); //by BAO: clear cache to prevent odd usage in EAS
                                }
                                arte.processServiceRequest(arte.getService(requestKind == ERequestKind.NORMAL ? request.getUnit().getServiceUri() : ArteAccessService.SERVICE_WSDL));
                                if (requestKind == ERequestKind.INIT) {
                                    adsInitCompleted = true;
                                    arte.getTrace().put(EEventSeverity.EVENT, Thread.currentThread().getName() + " inited: system: " + arte.getSystemName() + ", db scheme: " + arte.getDatabaseScheme() + ", kernel initialization time: " + (arte.getRqProcessingStartMillis() - kernelStartMillis) + " ms, ADS initialization time: " + (System.currentTimeMillis() - arte.getRqProcessingStartMillis()) + " ms.", EEventSource.ARTE);
                                    arteInstance.getInstance().getTrace().put(EEventSeverity.EVENT, Thread.currentThread().getName() + ArteInstance.Messages._STARTED, ArteMessages.MLS_ID_XXX_STARTED, new ArrStr(Thread.currentThread().getName()), EEventSource.ARTE, false);
                                } else if (requestKind == ERequestKind.UPDATE) {
                                    arte.getTrace().enterContext(EEventContextType.SYSTEM_INSTANCE, (long) arteInstance.getInstance().getId());
                                    try {
                                        arte.getTrace().put(ArteMessages.MLS_ID_LOADED_VERSION, new ArrStr(Thread.currentThread().getName(), String.valueOf(request.getVersion()), String.valueOf(System.currentTimeMillis() - arte.getRqProcessingStartMillis())));
                                    } finally {
                                        arte.getTrace().leaveContext(EEventContextType.SYSTEM_INSTANCE, (long) arteInstance.getInstance().getId());
                                    }
                                }
                            } else { // maintenance requested
                                arte.maintenance();
                                actualizeGuiAndFileTraceProfiles();
                            }
                        } finally {

                            freeSocket();

                            if (Thread.currentThread().getPriority() != Thread.NORM_PRIORITY) {
                                Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
                            }

                            socketInputStream = null;
                            socketOutputStream = null;
                            processorFactory = null;
                            messageProcessor = null;
                            requestSapOptions = null;

                            arteInstance.getStatistic().onFinishWork(System.currentTimeMillis());
                            arteInstance.getInstance().getInstanceMonitor().arteSessionFinished(request);
                        }
                    } finally {
                        if (request != null) {
                            if (request.getActiveTicket() != null) {
                                arteInstance.getInstance().getActiveArteResourceManager().releaseTicket(request.getActiveTicket());
                            }
                            try {//invoking untrusted code, should defend from exception
                                if (request.getCallback() != null) {
                                    request.getCallback().sessionFinished();
                                }
                            } catch (Throwable t) {
                                arteInstance.getTrace().put(EEventSeverity.ERROR, ArteMessages.ERR_ON_SESS_END_NOTIFICATION + ExceptionTextFormatter.exceptionStackToString(t), null, null, EEventSource.ARTE, false);
                            }
                        }
                    }
                    final long curTimeMillis = System.currentTimeMillis();
                    if (isDbConnectionClosed()) {
                        stopReason = REASON_CLOSED_DB_CONNECTION;
                    }
                    if (Thread.currentThread().isInterrupted()) {
                        stopReason = REASON_THREAD_WAS_INTERRUPTED;
                    } else if (arteInstance.isStopping() || (arteInstance.getRequestedShutdownTimeMillis() > 0 && curTimeMillis > arteInstance.getRequestedShutdownTimeMillis())) {
                        stopReason = REASON_STOP_REQUESTED;
                        Thread.currentThread().setName(initialThreadName);
                    } else {
                        final Long liveTimeMin = arteInstance.getInstance().getArteInstLiveTimeMin();
                        if (liveTimeMin != null && liveTimeMin.longValue() > 0
                                && curTimeMillis > arteInstance.getStartTime() + liveTimeMin.longValue() * 60000) {
                            //it's time to die, but ARTE instances shouldn't die all at once. 
                            final long lastAutoShutdownTimeMills = arteInstance.getInstance().getLastArteAutoShutdownTimeMillisHolder().get();
                            if (curTimeMillis > lastAutoShutdownTimeMills + AUTO_SHUTDOWN_INTERVAL_MILLIS) {
                                if (arteInstance.getInstance().getLastArteAutoShutdownTimeMillisHolder().compareAndSet(lastAutoShutdownTimeMills, curTimeMillis)) {
                                    stopReason = REASON_LIFE_TIME_HAS_EXPIRED;
                                    Thread.currentThread().setName(initialThreadName);
                                }
                            }
                        }
                    }

                    if (stopReason != null) {
                        return;
                    }

                    arte.getDbConnection().getRadixConnection().closeSchedulledStatements();
                    getServiceClient().maintenance();
                }
            }
        } catch (Throwable e) {
            if (e instanceof InterruptedException || Thread.currentThread().isInterrupted()) {
                stopReason = REASON_THREAD_WAS_INTERRUPTED;
                return;
            }
            stopReason = getUnexpectedExceptionReason(e);
            if (isDbConnectionClosed()) {
                stopReason = stopReason == null ? REASON_CLOSED_DB_CONNECTION : stopReason + "; " + REASON_CLOSED_DB_CONNECTION;
            }
            if (arte != null) {
                arte.getTrace().put(ArteMessages.MLS_ID_ERR_ON_SERVICE_RQ_PROC, new ArrStr(arteInstance.getTitle(), ExceptionTextFormatter.exceptionStackToString(e)));
            }
            arteInstance.getInstance().getTrace().put(EEventSeverity.ERROR, ArteMessages.ERR_ON_SERVICE_RQ_PROC + ExceptionTextFormatter.exceptionStackToString(e), ArteMessages.MLS_ID_ERR_ON_SERVICE_RQ_PROC, new ArrStr(arteInstance.getTitle(), ExceptionTextFormatter.exceptionStackToString(e)), EEventSource.ARTE, false);
        } finally {
            if (lastCountTicket != null) {
                arteInstance.getInstance().getArtePool().release(lastCountTicket);
                lastCountTicket = null;
            }

            keepConnectionAlive = false;
            try {
                freeSocket();
            } catch (Exception ex) {
                logErrorOnStop(ex);
            }
            if (arte != null) {
                try {
                    arte.close();
                    arte.getTrace().delTarget(fileTraceTargetHandler);
                    arte = null;
                } catch (Exception ex) {
                    logErrorOnStop(ex);
                }
            }
            if (arteInstance.getDbConnection() != null) {
                try {
                    arteInstance.getDbConnection().close();
                } catch (Throwable e) {
                    logErrorOnStop(e);
                }
                arteInstance.setDbConnection(null);
            }
            if (servicesClient != null) {
                try {
                    servicesClient.close();
                } catch (Exception ex) {
                    logErrorOnStop(ex);
                } finally {
                    servicesClient = null;
                }
            }
            try {
                disposeView();
            } catch (Throwable t) {
                logErrorOnStop(t);
            }
            try {
                if (socketWatcher != null) {
                    socketWatcher.close();
                }
            } catch (Throwable e) {
                logErrorOnStop(e);
            } finally {
                socketWatcher = null;
            }
            if (stopReason == null) {
                stopReason = REASON_UNKNOWN;
            }
            try {
                arteInstance.getInstance().getTrace().put(EEventSeverity.EVENT, Thread.currentThread().getName() + ArteInstance.Messages._STOPPED + ". " + ArteInstance.Messages.REASON + ": " + stopReason, ArteMessages.MLS_ID_XXX_STOPPED, new ArrStr(Thread.currentThread().getName(), stopReason), EEventSource.ARTE, false);
            } finally {
                arteInstance.closeFileLog();
            }
        }
    }

    private String getUnexpectedExceptionReason(final Throwable t) {
        try {
            return t == null ? REASON_UNEXPECTED_EXCEPTION : REASON_UNEXPECTED_EXCEPTION + ": " + ExceptionTextFormatter.throwableToString(t);
        } catch (Throwable tOnGetMessage) {
            try {
                return REASON_UNEXPECTED_EXCEPTION + " ('" + t + "', exception stack calculation also has thrown exception " + tOnGetMessage + ")";
            } catch (Throwable reallyWierd) {
                return REASON_UNEXPECTED_EXCEPTION + " (unable to provide details)";
            }
        }
    }

    private IArteRequest createAdsInitRequest(final long version) {
        return new IArteRequest() {

            final long createTimeMillis = System.currentTimeMillis();
            final Map<String, String> headers = new HashMap<>();

            @Override
            public ArteUnit getUnit() {
                return null;
            }

            @Override
            public RequestChannel getRequestChannel() {
                return new RequestChannel() {
                    @Override
                    public void close() throws IOException {
                        //do nothing
                    }

                    @Override
                    public InputStream getInputStream() throws IOException {
                        return null;
                    }

                    @Override
                    public OutputStream getOutputStream() throws IOException {
                        return null;
                    }

                    @Override
                    public void setReadTimeout(int timeoutMillis) throws IOException {
                        //do nothing
                    }

                    @Override
                    public String getRemoteAddress() {
                        return "init";
                    }

                    @Override
                    public String getRemoteAddressUnchangingPart() {
                        return "init";
                    }

                    @Override
                    public String getLocalAddress() {
                        return "init";
                    }

                    @Override
                    public SelectableChannel getSelectableChannel() {
                        return null;
                    }
                };
            }

            @Override
            public IArteRequestCallback getCallback() {
                return null;
            }

            @Override
            public String getClientId() {
                return "init";
            }

            @Override
            public SapOptions getSapOptions() {
                return new SapOptions(0, new SapAddress(new PipeAddress("init")), "init", EPortSecurityProtocol.NONE, null, null, EClientAuthentication.None, null, EClientAuthentication.None, null, null);
            }

            @Override
            public int getRadixPriority() {
                return EPriority.NORMAL.getValue().intValue();
            }

            @Override
            public InputStream getOverriddenInput() {
                final StringBuilder sb = new StringBuilder();
                sb.append("POST / HTTP/1.1\r\n");
                final InvokeDocument invokeDoc = InvokeDocument.Factory.newInstance();
                invokeDoc.addNewInvoke().addNewInvokeRq();
                invokeDoc.getInvoke().getInvokeRq().setClassId(Id.Factory.loadFrom("pdcArte______________________"));
                invokeDoc.getInvoke().getInvokeRq().setMethodId("mthT25AIBSXLJBCDLJEBVCGDAEV5A");
                final String content = SoapFormatter.createSoapEnvelope(invokeDoc).xmlText();
                sb.append("Content-Length: ").append(content.length()).append("\r\n");
                sb.append("\r\n");
                sb.append(content);
                return new ByteArrayInputStream(sb.toString().getBytes(Charset.forName("UTF-8")));
            }

            @Override
            public PriorityResourceManager.Ticket getCountTicket() {
                return null;
            }

            @Override
            public void setCountTicket(PriorityResourceManager.Ticket ticket) {
                //do nothing
            }

            @Override
            public PriorityResourceManager.Ticket getActiveTicket() {
                return null;
            }

            @Override
            public void setActiveTicket(PriorityResourceManager.Ticket ticket) {
                //do nothing
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

        };
    }

    private boolean isDbConnectionClosed() {
        try {
            return arte.getDbConnection().get() == null || arte.getDbConnection().get().isClosed();
        } catch (Exception ex) {
            return false;
        }
    }

    private void logErrorOnStop(final Throwable ex) {
        try {
            final String exStack = ExceptionTextFormatter.throwableToString(ex);
            arteInstance.getInstance().getTrace().put(EEventSeverity.ERROR, ArteMessages.ERR_ON_ARTE_INST_STOP + " \"" + arteInstance.getTitle() + "\": " + exStack, ArteMessages.MLS_ID_ERR_ON_ARTE_INST_STOP, new ArrStr(arteInstance.getTitle(), exStack), EEventSource.ARTE_POOL, false);
        } catch (Throwable errOnLog) {
            //ignore
        }
    }

    /**
     * Should be called only from Arte Thread
     */
    private void freeSocket() throws InterruptedException {
        try {
            finishWatchBreak();
        } catch (IOException e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            arte.getTrace().put(ArteMessages.MLS_ID_ERR_ON_ACCEPTED_SOCKET_CLOSE, new ArrStr(arteInstance.getTitle(), exStack));
        }
        if (request != null && request.getUnit() != null) {
            if (arteInstance.getInstance().isShuttingDown() || !keepConnectionAlive) {
                request.getUnit().requestCloseChannel(request.getRequestChannel());
            } else {
                request.getUnit().freeChannel(request.getRequestChannel());
            }
            arte.getTrace().put(ArteMessages.MLS_ID_CLIENT_DISCONNECTED, new ArrStr(arteInstance.getTitle(), clientAddress));
            arteInstance.clearRequest();
        }
    }

    final Object getNextRqSemaphore() {
        return arteInstance.getNextRqSemaphore();
    }

    private void createView() {//вызывается только с нити инстанции арте
        if (arteInstance.getView() != null) {
            return;
        }
        arteInstance.setView(ServerItemView.Factory.newInstance(new ArteInstanceView(arteInstance)));
        guiTraceTargetHandler = arte.getTrace().addTargetBuffer(//to see events traced by arte.Trace
                traceProfiles.getGuiTraceProfile(), new TraceBuffer() {
                    @Override
                    public void put(final TraceItem item) {
                        arteInstance.getTrace().put(item, Collections.singleton(ServerTrace.ETraceDestination.GUI), null);
                    }
                });
    }

    private void disposeView() {//вызывается только с нити инстанции арте
        if (arteInstance.getView() != null) {
            arteInstance.getView().dispose();
            if (arte != null) {
                arte.getTrace().delTarget(guiTraceTargetHandler);
            }
            guiTraceTargetHandler = null;
        }
    }

    private boolean isLogDirtyData() {
        return RadixSoapHelper.isDirtyDataLoggingEnabled(requestSapOptions.getAdditionalAttrs());
    }

    @Override
    public XmlObject recvSoapRequest(int timeout, Map<String, String> headerAttrs) throws ArteSocketException, ArteSocketTimeout, InterruptedException {
        try {
            if (messageProcessor != null) {
                throw new IllegalStateException("Request is already received");
            }
            boolean isInitRequest = request.getUnit() == null;
            if (!isInitRequest) {
                request.getRequestChannel().setReadTimeout(timeout * 1000);
            }
            if (headerAttrs == null) {
                headerAttrs = new HashMap<>();
            }
            final byte[] dirtyData = HttpFormatter.readMessage(socketInputStream, headerAttrs, arteCommunicatorTracer);
            if (!isInitRequest && isLogDirtyData()) {
                RadixSoapHelper.logDataReceived(ESoapMessageType.REQUEST, dirtyData, clientAddress, arteCommunicatorTracer);
            }
            messageProcessor = processorFactory.createProcessor();
            final XmlObject xmlRequest = messageProcessor.unwrapRequest(dirtyData);
            keepConnectionAlive = HttpFormatter.getKeepConnectionAlive(headerAttrs);
            if (!isInitRequest && arte.getTrace().getMinSeverity(EEventSource.ARTE_COMMUNICATOR) <= EEventSeverity.DEBUG.getValue().longValue()) {
                RadixSoapHelper.logMessageReceived(ESoapMessageType.REQUEST, xmlRequest, clientAddress, arteCommunicatorTracer);
            }
            startWatchBreak();
            return xmlRequest;
        } catch (SocketTimeoutException e) {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            throw new ArteSocketTimeout("Timeout on receive request from " + getRemoteAddress(), e);
        } catch (EOFException e) {
            return null;//ignore disonnect for kept connection
        } catch (IOException | ProcessException e) {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            throw new ArteSocketException("Can't receive request from " + getRemoteAddress() + " : " + e.getMessage(), e);
        }
    }

    @Override
    public XmlObject recvRequest(final int timeout) throws ArteSocketException, ArteSocketTimeout, InterruptedException {
        try {
            return SoapFormatter.getInnerContent(recvSoapRequest(timeout, null));
        } catch (IOException ex) {
            throw new RadixError("Unable to extract conent from soap request", ex);
        }
    }

    @Override
    public void sendResponse(final XmlObject obj, final boolean keepConnect) throws ArteSocketException, InterruptedException, ArteSocketTimeout {
        //called only from arte thread so is not synchronized
        try {
            try {
                if (messageProcessor == null) {
                    throw new IllegalStateException("No request received");
                }

                if (request.getUnit() == null) { //init request
                    return;
                }

                finishWatchBreak();
                boolean logDirtyData = isLogDirtyData();

                if (logDirtyData && arte.getTrace().getMinSeverity(EEventSource.ARTE_COMMUNICATOR) <= EEventSeverity.DEBUG.getValue().longValue()) {
                    arte.getTrace().put(EEventSeverity.DEBUG, "Responce message prepared: " + obj.xmlText(), EEventSource.ARTE_COMMUNICATOR.getValue(), true);
                }

                final byte[] data = messageProcessor.wrapResponce(obj);
                final Map<String, String> attrs = new HashMap();
                if (keepConnectionAlive || keepConnect) {
                    attrs.put(EHttpParameter.HTTP_CONNECTION_ATTR.getValue(), HttpFormatter.KEEP_ALIVE);
                }

                SoapFormatter.sendResponse(socketOutputStream, data, attrs);
                if (arte.getTrace().getMinSeverity(EEventSource.ARTE_COMMUNICATOR) <= EEventSeverity.DEBUG.getValue().longValue()) {
                    if (logDirtyData) {
                        RadixSoapHelper.logDataSent(ESoapMessageType.RESPONSE, data, clientAddress, arteCommunicatorTracer);
                    } else {
                        RadixSoapHelper.logMessageSent(ESoapMessageType.RESPONSE, obj, clientAddress, arteCommunicatorTracer);
                    }
                }
            } catch (SocketTimeoutException e) {
                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }
                throw new ArteSocketTimeout("Timeout on send response to " + getRemoteAddress(), e);
            } catch (IOException | ProcessException e) {
                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }
                throw new ArteSocketException("Can't send response to " + getRemoteAddress() + " : " + e.getMessage(), e);
            }
        } finally {
            messageProcessor = null;
        }
    }

    @Override
    public void sendFault(final ServiceProcessFault flt, final List<SoapFormatter.ResponseTraceItem> trace) throws ArteSocketException, InterruptedException, ArteSocketTimeout {
        //called only from unit thread so is not synchronized
        try {
            finishWatchBreak();
            try {
                if (messageProcessor == null) {
                    throw new ProcessException("Attempt to send fault when message processor is null");
                }
                final byte[] faultBytes = messageProcessor.wrapFault(flt, trace);
                if (isLogDirtyData()) {
                    RadixSoapHelper.logDataSent(ESoapMessageType.FAULT, faultBytes, clientAddress, arteCommunicatorTracer);
                }
                SoapFormatter.sendFault(faultBytes, socketOutputStream);
                RadixSoapHelper.logFaultSent(flt.code, flt.reason, flt.getMessage(), clientAddress, arteCommunicatorTracer);
            } catch (ProcessException ex) {
                arte.getTrace().put(EEventSeverity.WARNING, "Error while preparing fault message: " + ExceptionTextFormatter.throwableToString(ex) + "\nFalut is:\n" + ExceptionTextFormatter.throwableToString(flt), EEventSource.ARTE_COMMUNICATOR);
                SoapFormatter.sendFault(socketOutputStream, new ServiceProcessServerFault("Internal error", ex.getMessage(), ex, null), null);
            }

        } catch (SocketTimeoutException e) {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            throw new ArteSocketTimeout("Timeout on send fault to " + getRemoteAddress(), e);
        } catch (IOException e) {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            throw new ArteSocketException("Can't send fault to " + getRemoteAddress() + ": " + e.getMessage(), e);
        } finally {
            messageProcessor = null;
        }
    }

    @Override
    public XmlObject invokeResource(final XmlObject obj, final Class resultClass, final int timeout) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException {
        //should be called from arte thread only so is not synchronized
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_RESOURCE_INVOKE);
        try {
            finishWatchBreak();
            SoapFormatter.sendRequest(socketOutputStream, obj, "-", true);
            request.getRequestChannel().setReadTimeout(timeout * 1000);
            final HashMap<String, String> headerAttrs = new HashMap<>();
            final XmlObject rq = SoapFormatter.receiveResponse(socketInputStream, headerAttrs, null, arteInstance.getTrace().newTracer(EEventSource.ARTE_COMMUNICATOR.getValue()));
            keepConnectionAlive = HttpFormatter.getKeepConnectionAlive(headerAttrs);
            startWatchBreak();
            return rq;
        } catch (SocketTimeoutException e) {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            throw new ResourceUsageTimeout(e.getMessage(), e);
        } catch (IOException e) {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            throw new ResourceUsageException("Can't send request to resource to " + getRemoteAddress() + ": " + e.getMessage(), e);
        } catch (ServiceCallFault e) {
            throw new ResourceUsageException("Resource request to " + getRemoteAddress() + " has raised fault: " + e.getMessage(), e);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_RESOURCE_INVOKE);
        }
    }

    @Override
    public XmlObject invokeInternalService(final XmlObject obj, final Class resultClass, final String serviceUri, final int keepConnectTime, final int timeout) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        return invokeInternalService(obj, resultClass, serviceUri, keepConnectTime, timeout, -1);
    }

    @Override
    public XmlObject invokeInternalService(final XmlObject obj, final Class resultClass, final String serviceUri, final int keepConnectTime, final int timeout, final int connectTimeout) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        return invokeService(obj, null, resultClass, THIS_SYSTEM_ID, serviceUri, arteInstance.getInstance().getScpName(), null, keepConnectTime, timeout, connectTimeout);
    }

    @Override
    public XmlObject invokeService(final XmlObject obj, final Class resultClass, final Long systemId, final String serviceUri, final String scpName, final int keepConnectTime, final int timeout) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        return invokeService(obj, null, resultClass, systemId, serviceUri, scpName, keepConnectTime, timeout);
    }

    @Override
    public XmlObject invokeService(final XmlObject obj, final Map<String, String> soapRequestParams, final Class resultClass, final Long systemId, final String serviceUri, final String scpName, final int keepConnectTime, final int timeout) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        return invokeService(obj, soapRequestParams, resultClass, systemId, serviceUri, scpName, null, keepConnectTime, timeout);
    }

    @Override
    public XmlObject invokeService(final XmlObject obj, final Map<String, String> soapRequestParams, final Class resultClass, final Long systemId, final String serviceUri, final String scpName, final List<SapClientOptions> additionalSaps, final int keepConnectTime, final int timeout) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        return invokeService(obj, soapRequestParams, resultClass, systemId, serviceUri, scpName, additionalSaps, keepConnectTime, timeout, -1);
    }

    @Override
    public XmlObject invokeService(final XmlObject obj, final Map<String, String> soapRequestParams, final Class resultClass, final Long systemId, final String serviceUri, final String scpName, final List<SapClientOptions> additionalSaps, final int keepConnectTime, final int timeout, final int connectTimeout) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        getServiceClient().setScpName(scpName);
        return getServiceClient().invokeService(obj, soapRequestParams, resultClass, systemId, Long.valueOf(arteInstance.getInstance().getId()), serviceUri, additionalSaps, keepConnectTime, timeout, connectTimeout);
    }

    @Override
    public List<SapClientOptions> getServiceSaps(final Long systemId, final String serviceUri, final String scpName) throws ServiceCallException {
        getServiceClient().setScpName(scpName);
        return getServiceClient().getSaps(systemId, Long.valueOf(arteInstance.getInstance().getId()), serviceUri);
    }

    private ServiceClient getServiceClient() throws ServiceCallException {
        if (servicesClient == null) {
            servicesClient = new ServerServicesClient(arte,
                    new LocalTracer() {
                        @Override
                        public long getMinSeverity() {
                            return arte.getTrace().getMinSeverity();
                        }

                        @Override
                        public long getMinSeverity(final String eventSource) {
                            return arte.getTrace().getMinSeverity(eventSource);
                        }

                        @Override
                        public void put(final EEventSeverity sev, final String mess, final String code, final List<String> words, final boolean isSensitive) {
                            if (code != null) {
                                arte.getTrace().put(code, words, isSensitive);
                            } else {
                                arte.getTrace().put(sev, mess, EEventSource.ARTE_COMMUNICATOR.getValue(), isSensitive);
                            }
                        }
                    });
        }
        return servicesClient;

    }

    @Override
    public boolean breakSignaled() {
        final SelectableChannel channel = request.getRequestChannel().getSelectableChannel();
        try {
            return socketWatcher.isWatched(channel) && socketWatcher.isDisconnected(channel);
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     * @NotThreadsafe should be called from ProcessThread only
     *
     * @throws IOException
     */
    private void startWatchBreak() throws IOException {
        if (socketWatcher != null && request != null && request.getRequestChannel() != null && request.getRequestChannel().getSelectableChannel() != null) {
            socketWatcher.startDisconnectWatch(request.getRequestChannel().getSelectableChannel());
        }
    }

    /**
     * @NotThreadsafe should be called from ProcessThread only
     *
     * @throws IOException
     */
    private void finishWatchBreak() throws IOException {
        if (socketWatcher != null && request != null && request.getRequestChannel() != null && request.getRequestChannel().getSelectableChannel() != null) {
            socketWatcher.finishDisconnectWatch(request.getRequestChannel().getSelectableChannel());
        }
    }

    @Override
    public String getLocalAddress() {
        if (request == null || request.getRequestChannel() == null) {
            return null;
        }
        return request.getRequestChannel().getLocalAddress();
    }

    @Override
    public String getRemoteAddress() {
        if (request == null || request.getRequestChannel() == null) {
            return null;
        }
        return request.getRequestChannel().getRemoteAddress();
    }

    @Override
    public SocketsDisconnectWatcher getSocketsDisconnectWatcher() {
        return socketWatcher;
    }

    @Override
    public long getSapId() {
        if (request == null || request.getUnit() == null) {
            return -1;
        }
        return request.getUnit().getSapId();
    }

    @Override
    public ArteUnit getUnit() {
        return request == null ? null : request.getUnit();
    }
}
