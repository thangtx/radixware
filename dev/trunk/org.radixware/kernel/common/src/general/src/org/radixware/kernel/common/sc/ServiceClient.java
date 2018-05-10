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
package org.radixware.kernel.common.sc;

import org.radixware.kernel.common.soap.ISyncClientSoapEngine;
import org.radixware.kernel.common.soap.ISyncClientSoapEngineFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.*;
import javax.net.ssl.SSLContext;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.enums.EAadcMember;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.ESoapMessageType;
import org.radixware.kernel.common.exceptions.*;
import org.radixware.kernel.common.soap.DefaultSyncClientSoapEngine;
import org.radixware.kernel.common.soap.RadixSoapHelper;
import org.radixware.kernel.common.soap.RadixSoapMessage;
import org.radixware.kernel.common.ssl.CertificateUtils;
import org.radixware.kernel.common.ssl.KeystoreController;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.SoapFormatter.ResponseTraceItem;
import org.radixware.kernel.common.utils.*;
import org.radixware.kernel.common.utils.net.AioUtils;
import org.w3c.dom.Node;
import org.xmlsoap.schemas.soap.envelope.Detail;

public abstract class ServiceClient {

    private final int KEEP_SOCKET_FOR_CALLBACK_TIME_MILLIS = 2 * 24 * 60 * 60 * 1000; //2 days
    protected static final int REFRESH_PERIOD_MILLIS = 60 * 1000; //1 min
    protected String scpName = null;
    protected final InetSocketAddress myAddress;
    //protected Socket connectedSocket = null; //by BAO: it's not safe to store this pointer, use port.takeKeptSocket() instead
    protected final LocalTracer tracer;
    private final KeystoreController keystoreController;
    private char[] keyPassword = null;
    private final HashMap<String, Port> ports = new HashMap<>();
    private final ISyncClientSoapEngineFactory soapEngineFactory;
    private long aadcMemberStickTimeoutMillis = 0;

    protected ServiceClient(final LocalTracer tracer, final InetSocketAddress myAddress) {
        this(tracer, myAddress, null, null);
    }

    protected ServiceClient(final LocalTracer tracer, final InetSocketAddress myAddress,
            final KeystoreController keystoreController, final char[] keyPassword) {
        this(tracer, myAddress, keystoreController, keyPassword, null);
    }

    protected ServiceClient(final LocalTracer tracer,
            final InetSocketAddress myAddress,
            final KeystoreController keystoreController,
            final char[] keyPassword,
            final ISyncClientSoapEngineFactory soapEngineFactory) {
        this.tracer = tracer;
        this.myAddress = myAddress;
        this.keystoreController = keystoreController;
        this.keyPassword = keyPassword;
        this.soapEngineFactory = soapEngineFactory;
    }

    public void setAadcMemberStickTimeoutMillis(long aadcMemberStickTimeoutMillis) {
        this.aadcMemberStickTimeoutMillis = aadcMemberStickTimeoutMillis;
    }

    public long getAadcMemberStickTimeoutMillis() {
        return aadcMemberStickTimeoutMillis;
    }

    public void setScpName(final String scpName) throws ServiceCallException {
        if (Utils.equals(this.scpName, scpName)) {
            return;
        }
        this.scpName = scpName;
        for (Port p : ports.values()) {
            p.refresh();
        }
    }

    public void close() {
        for (Port p : ports.values()) {
            p.close();
        }
    }
    
    
    @Deprecated
    public XmlObject invokeService(final XmlObject rqEnvBody, final Class resultClass, Long systemId, final Long thisInstanceId, final String serviceUri, int keepConnectTimeSec, int timeoutSec) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        return invokeService(rqEnvBody, null, resultClass, systemId, thisInstanceId, serviceUri, keepConnectTimeSec, timeoutSec);
    }

    @Deprecated
    public XmlObject invokeService(final XmlObject rqEnvBody, final Map<String, String> soapRequestParams, final Class resultClass, Long systemId, final Long thisInstanceId, final String serviceUri, int keepConnectTimeSec, int timeoutSec) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        return invokeService(rqEnvBody, soapRequestParams, resultClass, systemId, thisInstanceId, serviceUri, null, keepConnectTimeSec, timeoutSec);
    }

    @Deprecated
    public XmlObject invokeService(final XmlObject rqEnvBody, final Map<String, String> soapRequestParams, final Class resultClass, Long systemId, final Long thisInstanceId, final String serviceUri, final List<SapClientOptions> additionalSaps, final int keepConnectTimeSec, final int timeoutSec) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        return invokeService(rqEnvBody, soapRequestParams, resultClass, systemId, thisInstanceId, serviceUri, additionalSaps, keepConnectTimeSec, timeoutSec, -1);
    }

    @Deprecated
    public XmlObject invokeService(final XmlObject rqEnvBody, final Class resultClass, Long systemId, final Long thisInstanceId, final String serviceUri, int keepConnectTimeSec, int receiveTimeoutSec, int connectTimeoutSec) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        return invokeService(rqEnvBody, null, resultClass, systemId, thisInstanceId, serviceUri, keepConnectTimeSec, receiveTimeoutSec, connectTimeoutSec);
    }

    @Deprecated
    public XmlObject invokeService(final XmlObject rqEnvBody, final Map<String, String> soapRequestParams, final Class resultClass, Long systemId, final Long thisInstanceId, final String serviceUri, int keepConnectTimeSec, int receiveTimoutSec, int connectTimeoutSec) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        return invokeService(rqEnvBody, soapRequestParams, resultClass, systemId, thisInstanceId, serviceUri, null, keepConnectTimeSec, receiveTimoutSec, connectTimeoutSec);
    }

    @Deprecated
    public XmlObject invokeService(final XmlObject rqEnvBody, final Map<String, String> soapRequestParams, final Class resultClass, Long systemId, final Long thisInstanceId, final String serviceUri, final List<SapClientOptions> additionalSaps, final int keepConnectTimeSec, final int receiveTimeoutSec, final int connectTimeoutSec) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        RadixSoapMessage message = new RadixSoapMessage(rqEnvBody, ESoapMessageType.REQUEST, soapRequestParams, resultClass, systemId, thisInstanceId, serviceUri, additionalSaps, null, keepConnectTimeSec, receiveTimeoutSec, connectTimeoutSec);
        return invokeService(message);
    }

    @Deprecated
    public XmlObject invokeService(final RadixSoapMessage message) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        return doSend(message, null);
    }

    public XmlObject invokeService(final XmlObject rqEnvBody, final Class resultClass, Long systemId, final Long thisInstanceId, final String serviceUri, int keepConnectTimeSec, int timeoutSec, final EAadcMember targetAadcMember) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        return invokeService(rqEnvBody, null, resultClass, systemId, thisInstanceId, serviceUri, keepConnectTimeSec, timeoutSec, targetAadcMember);
    }

    public XmlObject invokeService(final XmlObject rqEnvBody, final Map<String, String> soapRequestParams, final Class resultClass, Long systemId, final Long thisInstanceId, final String serviceUri, int keepConnectTimeSec, int timeoutSec, final EAadcMember targetAadcMember) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        return invokeService(rqEnvBody, soapRequestParams, resultClass, systemId, thisInstanceId, serviceUri, null, keepConnectTimeSec, timeoutSec, targetAadcMember);
    }

    public XmlObject invokeService(final XmlObject rqEnvBody, final Map<String, String> soapRequestParams, final Class resultClass, Long systemId, final Long thisInstanceId, final String serviceUri, final List<SapClientOptions> additionalSaps, final int keepConnectTimeSec, final int timeoutSec, final EAadcMember targetAadcMember) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        return invokeService(rqEnvBody, soapRequestParams, resultClass, systemId, thisInstanceId, serviceUri, additionalSaps, keepConnectTimeSec, timeoutSec, -1, targetAadcMember);
    }

    public XmlObject invokeService(final XmlObject rqEnvBody, final Class resultClass, Long systemId, final Long thisInstanceId, final String serviceUri, int keepConnectTimeSec, int receiveTimeoutSec, int connectTimeoutSec, final EAadcMember targetAadcMember) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        return invokeService(rqEnvBody, null, resultClass, systemId, thisInstanceId, serviceUri, keepConnectTimeSec, receiveTimeoutSec, connectTimeoutSec, targetAadcMember);
    }

    public XmlObject invokeService(final XmlObject rqEnvBody, final Map<String, String> soapRequestParams, final Class resultClass, Long systemId, final Long thisInstanceId, final String serviceUri, int keepConnectTimeSec, int receiveTimoutSec, int connectTimeoutSec, final EAadcMember targetAadcMember) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        return invokeService(rqEnvBody, soapRequestParams, resultClass, systemId, thisInstanceId, serviceUri, null, keepConnectTimeSec, receiveTimoutSec, connectTimeoutSec, targetAadcMember);
    }

    public XmlObject invokeService(final XmlObject rqEnvBody, final Map<String, String> soapRequestParams, final Class resultClass, Long systemId, final Long thisInstanceId, final String serviceUri, final List<SapClientOptions> additionalSaps, final int keepConnectTimeSec, final int receiveTimeoutSec, final int connectTimeoutSec, final EAadcMember targetAadcMember) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        RadixSoapMessage message = new RadixSoapMessage(rqEnvBody, ESoapMessageType.REQUEST, soapRequestParams, resultClass, systemId, thisInstanceId, serviceUri, additionalSaps, null, keepConnectTimeSec, receiveTimeoutSec, connectTimeoutSec);
        return invokeService(message, targetAadcMember);
    }

    public XmlObject invokeService(final RadixSoapMessage message, final EAadcMember targetAadcMember) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        return doSend(message, targetAadcMember);
    }

    private String getDestinationInfo(final RadixSoapMessage message, final SyncClientConnection connection) {
        return String.format("%s for SCP '%s' via SAP '%s' (%s)", generateKey(message.getSystemId(), message.getServiceUri()), scpName, connection.getSapOptions().getName(), connection);
    }

    private XmlObject doSend(final RadixSoapMessage message, final EAadcMember targetAadcMember) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        final String key = generateKey(message.getSystemId(), message.getServiceUri());
        if (tracer != null) {
            tracer.debug("Sending " + message.getType().getValue() + " to " + key, false);
        }
        final Port port = getPortForKey(message.getSystemId(), message.getThisInstanceId(), message.getServiceUri(), message.getType() == ESoapMessageType.REQUEST || message.getType() == ESoapMessageType.FAULT);
        if (port == null) {
            throw new ServiceCallSendException("Can't send " + message.getType().getValue() + " to " + key + " for SCP '" + scpName + "': no connection");
        }

        final long startMillis = System.currentTimeMillis();

        final int receiveTimeoutMillis = message.getReceiveTimeoutSec() * 1000;
        final int connectTimeoutMillis = message.getConnectTimeoutSec() <= 0 ? receiveTimeoutMillis : message.getConnectTimeoutSec() * 1000;

        final int keepConnectTimeMillis = message.getKeepConnectTimeSec() * 1000;
        port.onInvokeStart();
        while (true) {
            boolean isCallbackResponce = message.getType() == ESoapMessageType.CALLBACK_RESPONCE || message.getType() == ESoapMessageType.FAULT;

            final Integer targetAadcMemberId = targetAadcMember == null || targetAadcMember == EAadcMember.ANY ? null : targetAadcMember.getValue().intValue();

            final KeptConnection keptConnection = port.takeKeptConnection(isCallbackResponce ? KEEP_SOCKET_FOR_CALLBACK_TIME_MILLIS : keepConnectTimeMillis, targetAadcMemberId);

            SyncClientConnection connection = keptConnection != null ? keptConnection.getConnection() : null;
            final boolean keptSocketUsed = connection != null;

            if (connection == null && isCallbackResponce) {
                throw new ServiceCallSendException("Can't send " + message.getType().getValue() + " to " + key + " for SCP '" + scpName + "': server has disconnected");
            }

            ISyncClientSoapEngine soapEngine = keptConnection != null ? keptConnection.getSoapEngine() : null;
            final long connectAttemptTimeMillis = System.currentTimeMillis();

            if (connectTimeoutMillis > 0 && connectAttemptTimeMillis - startMillis >= connectTimeoutMillis) {
                throw port.createConnectTimeoutEx();
            }

            if (connection == null) {
                connection = port.connect(connectTimeoutMillis <= 0 ? -1 : startMillis + connectTimeoutMillis - connectAttemptTimeMillis, message.getAdditionalSaps(), targetAadcMemberId);
            }

            final String connectionInfo = connection.toString();
            message.setDestinationInfo(getDestinationInfo(message, connection));

            if (message.getConnectTimeoutSec() <= 0) {
                message.setSpentReceiveMillis((int) (System.currentTimeMillis() - startMillis));
            } else {
                message.setSpentReceiveMillis(0);
            }

            boolean toClose = true;
            try {
                try {
                    if (soapEngine == null && soapEngineFactory != null) {
                        try {
                            soapEngine = soapEngineFactory.create(connection.getSapOptions(), tracer);
                        } catch (Exception ex) {
                            if (tracer != null) {
                                tracer.put(EEventSeverity.ERROR, "Unable to create soap engine for " + message.getDestinationInfo() + ": " + ExceptionTextFormatter.throwableToString(ex), null, null, false);
                            }
                            connection.getSapOptions().block();
                        }
                    }
                    if (soapEngine == null) {
                        soapEngine = new DefaultSyncClientSoapEngine(tracer);
                    }
                    final RadixSoapMessage responceMessage = soapEngine.invoke(message, connection);
                    if (responceMessage == null) {
                        throw new ServiceCallRecvException("No responce received from " + message.getDestinationInfo());
                    }
                    if (HttpFormatter.getKeepConnectionAlive(responceMessage.getAttrs()) && (message.getType() != ESoapMessageType.REQUEST || message.getKeepConnectTimeSec() > 0)) {
                        toClose = false;
                    }
                    port.afterResponse();
                    if (tracer != null) {
                        tracer.debug("Received response from " + key, false);
                    }
                    return responceMessage.isEnvelopeMess() ? responceMessage.getEnvDocument() : responceMessage.getBodyDocument();
                } catch (ServiceCallSendException ex) {
                    if (message.getType() == ESoapMessageType.REQUEST && keptSocketUsed) {
                        tracer.debug(String.format("Unable to send %s to %s through kept connection, will retry with new connection. Error: %s", message.getType().getValue(), connectionInfo, ExceptionTextFormatter.throwableToString(ex)), false);
                        continue;
                    }
                    throw ex;
                } catch (ServiceCallFault e) {
                    RadixSoapHelper.logFaultReceived(e, connectionInfo, tracer);
                    final boolean canRetry = message.getType() == ESoapMessageType.REQUEST && (connectTimeoutMillis <= 0 || System.currentTimeMillis() - startMillis < connectTimeoutMillis);
                    if (e.getFaultCode().startsWith(ServiceProcessFault.FAULT_CODE_SERVER_BUSY)
                            || ServiceProcessFault.FAULT_CODE_SERVER_SHUTDOWN.equals(e.getFaultCode())) {
                        if (canRetry) {
                            tracer.debug(String.format("SAP %s is %s, retry", connectionInfo, getReasonFromBusyFault(e.getFaultCode())), false);
                            port.lastUsedSap.blockOnBusy(e.getFaultCode(), extractAvailableVersion(e.getDetail())); //short block
                        } else {
                            throw new ServiceConnectTimeout(RadixSoapHelper.createCallTimeoutMessage(message));
                        }
                    } else {
                        port.afterResponse();
                        throw e;
                    }
                }
            } finally {
                if (toClose) {
                    try {
                        connection.close();
                    } catch (IOException e1) {
                    }
                } else {
                    port.keepConnection(connection, soapEngine, message.getKeepConnectTimeSec());
                }
            }
        }
    }

    private String getReasonFromBusyFault(final String faultCode) {
        if (faultCode.length() <= ServiceProcessClientFault.FAULT_CODE_SERVER_BUSY.length()) {
            return "busy";
        }
        return "busy (" + faultCode.substring(ServiceProcessFault.FAULT_CODE_SERVER_BUSY.length() + 1) + ")";
    }

    public XmlObject sendCallbackResponse(final XmlObject rsEnvBody, final Class invokeResultClass, Long invokedSystemId, final Long thisInstanceId, final String invokedServiceUri, int timeoutSec) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        final RadixSoapMessage message = new RadixSoapMessage(rsEnvBody, ESoapMessageType.CALLBACK_RESPONCE, null, invokeResultClass, invokedSystemId, thisInstanceId, invokedServiceUri, null, null, -1, timeoutSec, -1);
        return doSend(message, null);
    }

    public XmlObject sendCallbackFault(final String faultCode, final String faultString, final String faultMessage, final Throwable cause, final String preprocessedCauseStack, final List<ResponseTraceItem> traceBuffer, final Class invokeResultClass, Long invokedSystemId, final Long thisInstanceId, final String invokedServiceUri, int timeoutSec) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        final RadixSoapMessage message = RadixSoapHelper.createFaultMessage(faultCode, faultString, faultMessage, cause, preprocessedCauseStack, traceBuffer);
        message.setSystemId(invokedSystemId);
        message.setThisInstanceId(thisInstanceId);
        message.setServiceUri(invokedServiceUri);
        message.setReceiveTimeoutSec(timeoutSec);
        return doSend(message, null);
    }

    protected void onAllSapsBusy(FailedSapsInfo info) throws InterruptedException, ServiceCallException {
        onAllSapsBusy(info.getBusyCount());
    }

    protected void onAllSapsBusy(int availableButBusyCount) throws InterruptedException, ServiceCallException {
        Thread.sleep(500);
    }

    protected boolean checkChangeAadcMemberBeforeStickTimeout(final Integer oldMemberId) {
        return false;
    }

    protected SSLContext prepareSslContext(final SapClientOptions sap) throws Exception {
        return CertificateUtils.prepareSslContext(keystoreController, keyPassword);
    }

    protected boolean isSslPossible() {
        return keystoreController != null;
    }

    public void maintenance() {
        for (Port port : ports.values()) {
            if (port != null) {
                port.maintenance();
            }
        }
    }

    /**
     * It is not safe to use this socket for refresh because invoke is not
     * reinterable (known by experience).
     *
     * @param systemId
     * @param serviceUri
     * @return
     * @throws ServiceCallException
     */
    abstract protected List<SapClientOptions> refresh(Long systemId, Long currentInstanceId, String serviceUri) throws ServiceCallException;
//------------------------------------------------    

    static class Port {

        private static final long FORCED_UNBLOCK_INTERVAL_MILLIS = 1000;
        private static final long WARN_ON_CONNECT_ERR_INTERVAL_MILLIS = 2000;
        List<SapClientOptions> saps = new ArrayList<>();
        long lastRefreshTime;
        final ServiceClient self;
        final Long systemId;
        final String serviceUri;
        private SapClientOptions lastUsedSap;
        private KeptConnection keptConnection = null;
        private long lastConnectionTimeMillis = 0;
        private long lastResponseTimeMillis = 0;
        private Integer lastResponseAadcMemberId = null;
        private long lastWarnOnConnectExMillis = 0;
        private Selector keptSocketSelector = null;
        private SelectionKey keptSocketSelectionKey;
        private final ByteBuffer buffer = ByteBuffer.allocate(256);
        private final Long thisInstanceId;
        private long lastForcedUnblockMillis = -1;
        int requestedKeepConnectTimeSec = 0;

        Port(ServiceClient self, Long systemId, Long thisInstanceId, String serviceUri) throws ServiceCallException {
            this.self = self;
            this.systemId = systemId;
            this.serviceUri = serviceUri;
            this.thisInstanceId = thisInstanceId;
            refresh();
        }

        public void afterResponse() {
            lastUsedSap.setNotBusy();
            lastResponseTimeMillis = System.currentTimeMillis();
            lastResponseAadcMemberId = lastUsedSap.getAadcMemberId();
        }

        public void onInvokeStart() {
            lastForcedUnblockMillis = -1;
        }

        void close() {
            if (keptConnection != null) {
                if (keptConnection.connection != null) {
                    try {
                        closeConnection(keptConnection.connection);
                    } catch (IOException ex) {
                        //do nothing
                    }
                }
                keptConnection = null;
            }
            if (keptSocketSelector != null) {
                try {
                    keptSocketSelector.close();
                } catch (IOException ex) {
                    //do nothing
                }
                keptSocketSelector = null;
            }
        }

        final void refresh() throws ServiceCallException {
            final List<SapClientOptions> new_saps = self.refresh(systemId, thisInstanceId, serviceUri);
            for (SapClientOptions sap : new_saps) {
                for (SapClientOptions s : saps) {
                    if (s.getName() != null && s.getName().equals(sap.getName())) {
                        sap.copyBlockStateFrom(s);
                        break;
                    }
                }
            }
            saps = new_saps;
            lastRefreshTime = System.currentTimeMillis();
        }

        private void closeConnection(SyncClientConnection connection) throws IOException {
            try {
                if (keptSocketSelectionKey != null) {
                    try {
                        keptSocketSelectionKey.cancel();
                        if (keptSocketSelector != null) {
                            keptSocketSelector.selectNow();
                        }
                    } finally {
                        keptSocketSelectionKey = null;
                    }
                }
            } finally {
                if (connection != null) {
                    try {
                        if (connection.getSelectableChannel() != null) {
                            connection.getSelectableChannel().configureBlocking(true);
                        }
                    } finally {
                        connection.close();
                    }
                }
            }
        }

        void maintenance() {
            if (keptConnection != null && requestedKeepConnectTimeSec >= 0 && System.currentTimeMillis() > lastConnectionTimeMillis + requestedKeepConnectTimeSec * 1000) {
                final KeptConnection expiredKeptConnection = takeKeptConnection(0, null);
                if (expiredKeptConnection != null && expiredKeptConnection.getConnection() != null) {
                    try {
                        expiredKeptConnection.getConnection().close();
                    } catch (IOException ex) {
                    }
                }
            }
        }

        KeptConnection takeKeptConnection(final int lifeTimeMillis, final Integer targetAadcMemberId) {
            if (keptConnection == null) {
                return null;
            }
            try {
                KeptConnection s = keptConnection;
                keptConnection = null;
                if (lifeTimeMillis >= 0 && System.currentTimeMillis() > lastConnectionTimeMillis + lifeTimeMillis && (targetAadcMemberId != null && !Objects.equals(targetAadcMemberId, s.connection.getSapOptions().getAadcMemberId()))) {
                    try {
                        closeConnection(s.connection);
                    } catch (IOException e) {
                    }
                    return null;
                }
                //check disconnect
                try {
                    if (keptSocketSelector == null || keptSocketSelector.selectNow() == 0) {
                        return s;
                    }
                    Set<SelectionKey> readyKeys = keptSocketSelector.selectedKeys();
                    Iterator<SelectionKey> readyItor = readyKeys.iterator();
                    while (readyItor.hasNext()) {
                        readyItor.next();
                        readyItor.remove();
                        while (true) {
                            if (((ReadableByteChannel) keptSocketSelectionKey.channel()).read(buffer) <= 0) {
                                try {
                                    closeConnection(s.connection);
                                } catch (IOException e) {
                                }
                                return null;
                            }
                            buffer.clear();  //skip
                        }
                    }
                    return s;
                } catch (IOException e) {
                    try {
                        closeConnection(s.connection);
                    } catch (IOException e1) {
                    }
                    return null;
                }
            } finally {
                try { //stop wait disconnect
                    if (keptSocketSelectionKey != null) {
                        keptSocketSelectionKey.cancel();
                        keptSocketSelectionKey.channel().configureBlocking(true);
                        if (keptSocketSelector != null) {
                            keptSocketSelector.selectNow();
                        }
                        keptSocketSelectionKey = null;
                    }
                } catch (IOException e) {
                }
            }
        }

        void keepConnection(final SyncClientConnection s, final ISyncClientSoapEngine soapEngine, final int keepConnectTimeSec) {
            keptConnection = new KeptConnection(s, soapEngine);
            requestedKeepConnectTimeSec = keepConnectTimeSec;
            //wait for disconnect
            try {
                if (s.getSelectableChannel() != null) {
                    s.getSelectableChannel().configureBlocking(false);
                    if (keptSocketSelector == null) {
                        keptSocketSelector = AioUtils.openSelector();
                    }
                    keptSocketSelectionKey = s.getSelectableChannel().register(keptSocketSelector, SelectionKey.OP_READ);

                }
            } catch (IOException e) {
                keptConnection = null;
            }
        }

        SyncClientConnection connect(final long timeOutMillis, final List<SapClientOptions> additionalSaps, final Integer targetAadcMemberId) throws ServiceCallTimeout,
                ServiceCallException,
                InterruptedException {
            final long startTimeMillis = System.currentTimeMillis();
            if (startTimeMillis - lastRefreshTime > REFRESH_PERIOD_MILLIS) {
                refresh();
            }

            lastWarnOnConnectExMillis = System.currentTimeMillis();//do not report warning on the first attempt
            boolean forceUnblockAllowed = lastForcedUnblockMillis == -1;//allow unblock on the first attempt
            Integer initialStickedAadcMemberId = null;
            while (true) {
                final long timeSpentMillis = (int) (System.currentTimeMillis() - startTimeMillis);
                if (timeOutMillis > 0 && timeSpentMillis >= timeOutMillis) {
                    throw createConnectTimeoutEx();
                }
                boolean forceUnblock = false;
                if (forceUnblockAllowed && (System.currentTimeMillis() - lastForcedUnblockMillis > FORCED_UNBLOCK_INTERVAL_MILLIS)) {
                    forceUnblock = true;
                    lastForcedUnblockMillis = System.currentTimeMillis();
                    forceUnblockAllowed = false;
                }

                Integer effectiveAadcMemberId = targetAadcMemberId;
                if (effectiveAadcMemberId == null
                        && lastResponseAadcMemberId != null
                        && self.getAadcMemberStickTimeoutMillis() > 0
                        && System.currentTimeMillis() - lastResponseTimeMillis < self.getAadcMemberStickTimeoutMillis()) {
                    effectiveAadcMemberId = lastResponseAadcMemberId;
                    if (initialStickedAadcMemberId == null) {
                        initialStickedAadcMemberId = effectiveAadcMemberId;
                    }
                }

                final List<SapClientOptions> sapsToCheck = getSapsToCheck(additionalSaps);

                final SapClientOptions sap = selectSap(sapsToCheck, forceUnblock, effectiveAadcMemberId);

                if (sap != null) {
                    final int originalTimeoutMillis = sap.getConnectTimeoutMillis();
                    try {
                        sap.setConnectTimeoutMillis(timeOutMillis <= 0 ? originalTimeoutMillis : (int) Math.min(originalTimeoutMillis, timeOutMillis - timeSpentMillis));
                        final SyncClientConnection connection = connect(sap);
                        if (connection != null) {
                            sap.unblock();
                            lastUsedSap = sap;
                            lastConnectionTimeMillis = System.currentTimeMillis();
                            return connection;
                        }
                    } finally {
                        sap.setConnectTimeoutMillis(originalTimeoutMillis);
                    }
                }

                if (sap == null) {
                    final List<FailedSapInfo> infos = new ArrayList<>();
                    boolean wasAadcMemberMismatch = false;
                    for (SapClientOptions checkedSap : sapsToCheck) {
                        boolean aadcMemberMismatch = effectiveAadcMemberId != null && !Objects.equals(effectiveAadcMemberId, checkedSap.getAadcMemberId());
                        wasAadcMemberMismatch |= aadcMemberMismatch;
                        infos.add(new FailedSapInfo(
                                checkedSap.getAddress().toString(),
                                aadcMemberMismatch ? null : !checkedSap.wasBlockedBecauseUnavailable(),
                                !aadcMemberMismatch && checkedSap.wasBlockedBecauseBusy() ? checkedSap.getBusyFaultCode() : null,
                                checkedSap.getAvailableVersionOnBusy(),
                                checkedSap.getAadcMemberId()));
                    }
                    if (wasAadcMemberMismatch && self.checkChangeAadcMemberBeforeStickTimeout(effectiveAadcMemberId)) {
                        lastResponseAadcMemberId = null;
                        continue;
                    }
                    self.onAllSapsBusy(new FailedSapsInfo(infos));
                    refresh();
                    forceUnblockAllowed = true;
                }
            }
        }

        private SyncClientConnection createConnection(final SapClientOptions sapOpts) {
            switch (sapOpts.getAddress().getKind()) {
                case INET_SOCKET_ADDRESS:
                    return new SocketConnection(this, sapOpts);
                case INTERNAL_PIPE_ADDRESS:
                    return new PipeConnection(this, sapOpts);
                case JMS_ADDRESS:
                    return new ServiceClientJmsConnection(this, sapOpts);
                default:
                    throw new IllegalArgumentException("Unsupported SAP channel type: " + sapOpts.getAddress().getKind());
            }

        }

        private SyncClientConnection connect(final SapClientOptions sap) throws InterruptedException {
            final String sapInfo = String.format("'%s' - %s", sap.getName(), String.valueOf(sap.getAddress()));
            boolean bCloseSocket = true;
            final SyncClientConnection connection = createConnection(sap);
            String fullErrorMessage = null;
            try {
                connection.connect();
                self.tracer.debug("Connection to sap established " + sapInfo, false);
                bCloseSocket = false;
                return connection;
            } catch (UnableToBindToLocalAddressException ex) {
                final String message = "SAP %s  has been blocked due to exception:\n %s";
                fullErrorMessage = String.format(message, sapInfo, ExceptionTextFormatter.exceptionStackToString(ex));
            } catch (UnknownHostException ex) {
                final String message = "SAP %s  has been blocked: Cannot resolve host: '%s'";
                fullErrorMessage = String.format(message, sapInfo, ex.getMessage());
            } catch (IOException ex) {
                if (Thread.interrupted()) {
                    throw new InterruptedException("Unable to connect to SAP " + sapInfo + ": thread is interrupted. IO error message: " + ex.getMessage());
                }
                fullErrorMessage = "SAP " + sapInfo + " has been blocked due to exception: " + ExceptionTextFormatter.throwableToString(ex);
            } finally {
                if (bCloseSocket) {
                    try {
                        if (connection != null) {
                            connection.close();
                        }
                    } catch (IOException e) {
                        //do nothing
                    }
                }
            }
            if (fullErrorMessage == null) {
                fullErrorMessage = "Unable to connect to SAP " + sapInfo + ": unknown";
            }
            if (System.currentTimeMillis() - lastWarnOnConnectExMillis > WARN_ON_CONNECT_ERR_INTERVAL_MILLIS) {
                self.tracer.put(EEventSeverity.WARNING, fullErrorMessage, null, null, false);
                lastWarnOnConnectExMillis = System.currentTimeMillis();
            } else {
                self.tracer.debug(fullErrorMessage, false);
            }
            sap.block();
            return null;
        }

        private List<SapClientOptions> getSapsToCheck(final List<SapClientOptions> additionalSaps) throws ServiceCallException {
            if (saps.isEmpty()) {
                refresh();
            }

            final List<SapClientOptions> sapsToCheck;
            if (additionalSaps == null || additionalSaps.isEmpty()) {
                sapsToCheck = saps;
            } else {
                sapsToCheck = new ArrayList<>(saps);
                sapsToCheck.addAll(additionalSaps);
            }
            return sapsToCheck;
        }

        private SapClientOptions selectSap(final List<SapClientOptions> sapsToCheck, final boolean enableBlockedSaps, final Integer targetAadcMemberId) throws ServiceCallException, InterruptedException {

            if (sapsToCheck == null || sapsToCheck.isEmpty()) {
                throw new NoSapsAvailableException("There are no available SAPs of service " + systemId.toString() + "@" + serviceUri + " for SCP '" + self.scpName + "'");
            }
            long sumPriority = 0;
            long curTime = System.currentTimeMillis();
            for (SapClientOptions s : sapsToCheck) {
                if (s.getBlockTime() > 0) {
                    if (curTime > s.getBlockTime()) {
                        s.unblock();
                    } else {
                        continue;
                    }
                }
                if (targetAadcMemberId==null || targetAadcMemberId.equals(s.getAadcMemberId())){
                    sumPriority += s.getPriority();
                }
            }
            if (sumPriority > 0) {
                long p = (long) (Math.random() * sumPriority);
                for (SapClientOptions s : sapsToCheck) {
                    if (s.getBlockTime() > 0 
                        || (targetAadcMemberId!=null && !targetAadcMemberId.equals(s.getAadcMemberId()))) {
                        continue;
                    }
                    p -= s.getPriority();
                    if (p < 0) {
                        return s;
                    }
                }
            }
            if (!enableBlockedSaps) {
                return null;
            }
            sumPriority = 0;
            for (SapClientOptions s : sapsToCheck) {                
                if (targetAadcMemberId==null || targetAadcMemberId.equals(s.getAadcMemberId())){
                    s.unblock();
                    sumPriority += s.getPriority();
                }
            }
            long p = (long) (Math.random() * sumPriority);
            for (SapClientOptions s : sapsToCheck) {
                if (targetAadcMemberId==null || targetAadcMemberId.equals(s.getAadcMemberId())){
                    p -= s.getPriority();
                    if (p < 0) {
                        return s;
                    }
                }
            }
            return null;
        }

        private ServiceConnectTimeout createConnectTimeoutEx() {
            return new ServiceConnectTimeout("Service " + systemId.toString() + "@" + serviceUri + " connect timeout");
        }
    }

    private static class KeptConnection {

        private final SyncClientConnection connection;
        private final ISyncClientSoapEngine soapEngine;

        public KeptConnection(SyncClientConnection connection, ISyncClientSoapEngine soapEngine) {
            this.connection = connection;
            this.soapEngine = soapEngine;
        }

        public SyncClientConnection getConnection() {
            return connection;
        }

        public ISyncClientSoapEngine getSoapEngine() {
            return soapEngine;
        }
    }

    private String generateKey(Long systemId, String serviceUri) {
        return systemId.toString() + "@" + serviceUri;
    }

    private Port getPortForKey(final Long systemId, final Long thisInstanceId, final String serviceUri, final boolean bAutoOpen) throws ServiceCallException {
        final String key = generateKey(systemId, serviceUri);
        Port port = ports.get(key);
        if (port == null && bAutoOpen) {
            port = new Port(this, systemId, thisInstanceId, serviceUri);
            ports.put(key, port);
        }
        return port;
    }

    public List<SapClientOptions> getSaps(final Long systemId, final Long thisInstanceId, final String serviceUri) throws ServiceCallException {
        final Port port = getPortForKey(systemId, thisInstanceId, serviceUri, true);
        port.refresh();
        return port.saps;
    }

    public static String getInetConnectionHostAddress(Object connection) throws IOException {
        if (connection instanceof Socket) {
            return ((Socket) connection).getInetAddress().getHostAddress() + ":" + ((Socket) connection).getPort();
        } else {
            return "";
        }
    }

    public static Long extractAvailableVersion(final Detail detail) {
        if (detail != null) {
            try {
                final Node detNode = detail.getDomNode();
                for (int i = 0; i < detNode.getChildNodes().getLength(); i++) {
                    Node childNode = detNode.getChildNodes().item(i);
                    if (childNode.getNodeType() == Node.ELEMENT_NODE
                            && childNode.getLocalName().equals(ServiceProcessFault.AVAILABLE_VERSION)) {
                        return Long.valueOf(childNode.getChildNodes().item(0).getNodeValue().trim());
                    }
                }
            } catch (Exception ex) {
                return null;
            }
        }
        return null;
    }

}
