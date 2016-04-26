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
package org.radixware.kernel.server.aio;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.nio.channels.UnresolvedAddressException;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.SSLContext;
import javax.wsdl.WSDLException;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EHttpParameter;
import org.radixware.kernel.common.enums.EPortSecurityProtocol;
import org.radixware.kernel.common.exceptions.*;
import org.radixware.kernel.common.sc.SapClientOptions;
import org.radixware.kernel.common.soap.IClientMessageProcessorFactory;
import org.radixware.kernel.common.soap.IClientSoapMessageProcessor;
import org.radixware.kernel.common.soap.ProcessException;
import org.radixware.kernel.common.ssl.CertificateUtils;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.HttpFormatter;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.common.utils.io.pipe.BidirectionalPipe;
import org.radixware.kernel.common.utils.net.ClientChannel;
import org.radixware.kernel.common.utils.net.JmsClientChannel;
import org.radixware.kernel.common.utils.net.PipeClientChannel;
import org.radixware.kernel.common.utils.net.SocketClientChannel;
import org.radixware.kernel.server.units.Messages;

/**
 * Cеанс клиента сервиса. Возникает при вызове ServiceClient.invoke. При
 * получении ответа или ошибке генерирует событие ResponseEvent. Закрывается при
 * разрыве соединения, ошибках, а также при получении ответа, если не
 * keepConnect. Если после получения ответа connected()==true, то в сеансе можно
 * посылать следующий запрос (см. invoke)
 *
 *
 */
public class ServiceClientSeance implements EventHandler {

    private static enum EGenerateDisonnect {

        YES,
        NO;
    }

    private static enum ERemoveFromParent {

        YES,
        NO;
    }

    static {
        final String keepConnectionTimeout = System.getProperty("rdx.sc.async.keepconnect.millis");
        KEEP_CONNECTION_MILLIS = keepConnectionTimeout == null || keepConnectionTimeout.isEmpty() ? 4000 : Integer.parseInt(keepConnectionTimeout);
    }
    private static final long MAX_DELAY_BEFORE_RETRY_MILLIS = SystemPropUtils.getLongSystemProp("rdx.sc.async.max.delay.before.retry.millis", 5000);
    private static int KEEP_CONNECTION_MILLIS;
    private static final int MAX_VALUE_OF_CONNECT_ATTEMPTS = 100;
    private final AsyncServiceClient parent;
    private XmlObject rqEnvBodyDoc;
    private Class resultClass;
    private Map<String, String> additionalRequestAttrs;
    private boolean keepConnect;
    private long maxFinishTime;
    private ClientChannel clientChannel;
    private ChannelPort port;
    private SapClientOptions sap;
    private IClientSoapMessageProcessor messageProcessor;
    private IClientMessageProcessorFactory processorFactory;
    private final Map<String, KeptConnection> keptConnections = new HashMap<>();
    private boolean busy = false;
    private int reconnectAttempts = 0;
    private long invokeTimeMillis = 0;
    private long lastSendTimeMillis = 0;

    public ServiceClientSeance(AsyncServiceClient parent) {
        this.parent = parent;
        this.port = null;
        this.clientChannel = null;
    }

    public void invoke(XmlObject rqEvBodyDoc_, Class resultClass_, boolean keepConnect_, int timeoutMillis) {
        invoke(rqEvBodyDoc_, resultClass_, null, keepConnect_, timeoutMillis);
    }

    public void invoke(XmlObject rqEvBodyDoc_, Class resultClass_, Map<String, String> requestParams, boolean keepConnect_, int timeoutMillis) {
        busy = true;
        rqEnvBodyDoc = rqEvBodyDoc_;
        resultClass = resultClass_;
        keepConnect = keepConnect_;
        maxFinishTime = System.currentTimeMillis() + timeoutMillis;
        reconnectAttempts = 0;
        this.additionalRequestAttrs = requestParams;
        invokeTimeMillis = System.currentTimeMillis();
        lastSendTimeMillis = 0;
        if (port == null) {
            connect(true);
        } else {
            call();
        }
    }

    public boolean connected() {
        return port != null && port.isConnected();
    }

    public boolean busy() {
        return busy;
    }

    public void close() {
        close(EGenerateDisonnect.YES, ERemoveFromParent.YES);
    }

    /**
     * @param generateDisconnect if true, then this close() will lead to
     * notify(ResponceEvent("server is disconnected))"
     * @param removeFromParent
     */
    private void close(EGenerateDisonnect generateDisconnect, ERemoveFromParent removeFromParent) {
        try {
            if (clientChannel != null) {
                parent.dispatcher.unsubscribe(new EventDispatcher.ConnectEvent(clientChannel.getSelectableChannel()));
            }
            parent.dispatcher.unsubscribeFromTimerEvents(this);//unsubscribe from retry
            if (port == null) {
                closeAndClearClientChannel();
                return;
            }
            parent.dispatcher.unsubscribe(new SocketChannelPort.ReceiveEvent(port));
            if (generateDisconnect == EGenerateDisonnect.NO) {
                parent.dispatcher.unsubscribe(new SocketChannelPort.DisconnectEvent(port));
                //else
                //подписка сработает в port.close() и удалится
            }
            port.close();
            port = null;
            closeAndClearClientChannel();
        } finally {
            if (removeFromParent == ERemoveFromParent.YES) {
                parent.seances.remove(this);
                for (KeptConnection keptConn : keptConnections.values()) {
                    keptConn.discard();
                }
                keptConnections.clear();
                parent.tracer.debug("Service client seance closed", false);
            }
        }
    }

    private void closeAndClearClientChannel() {
        if (clientChannel != null) {
            try {
                clientChannel.close();
            } catch (IOException e) {
                //do nothing
            }
            clientChannel = null;
        }
    }
//	--------------------------------- Private ----------------------------------------------------

    public AsyncServiceClient getParent() {
        return parent;
    }

    private void connect(boolean enableBlockedSaps) {
        connect(enableBlockedSaps, null);
    }

    private void connect(boolean enableBlockedSaps, ChannelPort callerPort) {
        if (port != null || clientChannel != null) {
            throw new RadixError("Client seance already connected");
        }

        while (true) {
            SapClientOptions oldSap = sap;
            sap = parent.getSap(enableBlockedSaps);
            if (sap == null) {
                if (enableBlockedSaps) {
                    close(EGenerateDisonnect.NO, ERemoveFromParent.YES);
                    responseNotify(new NoSapsAvailableException("There are no available SAPs"));
                } else {
                    if (reconnectAttempts < MAX_VALUE_OF_CONNECT_ATTEMPTS) {
                        reconnectAttempts++;
                    }
                    parent.tracer.debug("All SAPs are blocked, retry after delay", false);
                    parent.dispatcher.waitEvent(new EventDispatcher.TimerEvent(), this, System.currentTimeMillis() + getDelayMillisBeforeNextRetry());
                }
                break;
            }
            Exception exOnConnect = null;
            try {
                parent.tracer.debug("Try SAP '" + sap.getName() + "'", false);
                final KeptConnection keptConnection = keptConnections.remove(sap.getConnectionId());
                if (keptConnection != null && keptConnection.bringBack()) {
                    clientChannel = keptConnection.clientChannel;
                    port = keptConnection.port;
                    parent.tracer.debug("Using kept connection for SAP '" + sap.getName() + "' (" + port.getShortDescription() + ")", false);
                    afterPortInitialized(port != callerPort);
                } else {
                    switch (sap.getAddress().getKind()) {
                        case INET_SOCKET_ADDRESS:
                            clientChannel = new SocketClientChannel(parent.myAddress);
                            break;
                        case INTERNAL_PIPE_ADDRESS:
                            clientChannel = new PipeClientChannel();
                            break;
                        case JMS_ADDRESS:
                            clientChannel = new JmsClientChannel();
                            break;
                        default:
                            throw new IllegalArgumentError("Unsupported SAP address: " + sap.getAddress().getKind());

                    }
                    if (clientChannel.connect(sap.getAddress(), 0)) {
                        sap.unblock();
                        afterConnect();
                    } else {
                        parent.tracer.debug("Service client wait for connect", false);
                        parent.dispatcher.waitEvent(new EventDispatcher.ConnectEvent(clientChannel.getSelectableChannel()), this, maxFinishTime);
                    }
                }
                break;
            } catch (IOException | UnresolvedAddressException e) {
                exOnConnect = e;
            }
            if (exOnConnect != null) {
                parent.tracer.debug("SAP '" + sap.getName() + "' connection error: " + ExceptionTextFormatter.exceptionStackToString(exOnConnect), false);
                sap.block();
                if (System.currentTimeMillis() > maxFinishTime) {
                    close(EGenerateDisonnect.NO, ERemoveFromParent.YES);
                    responseNotify(new ServiceCallTimeout("Connection time out"));
                    break;
                } else {
                    close(EGenerateDisonnect.NO, ERemoveFromParent.NO);
                }
                enableBlockedSaps = false;
            }
        }
    }

    private long getDelayMillisBeforeNextRetry() {
        long result;
        if (reconnectAttempts <= 2) {
            result = 500;
        } else if (reconnectAttempts <= 5) {
            result = 1000;
        } else {
            result = 5000;
        }
        if (result > MAX_DELAY_BEFORE_RETRY_MILLIS) {
            result = MAX_DELAY_BEFORE_RETRY_MILLIS;
        }
        return delayOrLeftBeforeFinishTime(result);
    }

    private long delayOrLeftBeforeFinishTime(final long delayMillis) {
        return Math.min(delayMillis, Math.max(0, maxFinishTime - System.currentTimeMillis()));
    }

    private void call() {
        try {
            try {
                messageProcessor = processorFactory.createProcessor();
            } catch (Exception ex) {
                throw new IOException(ex);
            }
            final byte[] packet;
            try {
                packet = messageProcessor.wrapRequest(rqEnvBodyDoc);
            } catch (ProcessException ex) {
                throw new IOException(ex);
            }
            final QName objQName = rqEnvBodyDoc.schemaType().getDocumentElementName();
            final Map<String, String> requestAttrs = new HashMap<>(parent.rqFrameAttrs);
            if (additionalRequestAttrs != null) {
                requestAttrs.putAll(additionalRequestAttrs);
            }
            if (!requestAttrs.containsKey(EHttpParameter.HTTP_SOAPACTION_ATTR.getValue())) {
                requestAttrs.put(EHttpParameter.HTTP_SOAPACTION_ATTR.getValue(), objQName.getNamespaceURI() + "#" + objQName.getLocalPart());
            }

            requestAttrs.put(EHttpParameter.HTTP_CONNECTION_ATTR.getValue(), keepConnect ? "keep-alive" : "close");
            lastSendTimeMillis = lastSendTimeMillis == 0 ? invokeTimeMillis : System.currentTimeMillis();
            port.send(packet, requestAttrs);
            if (parent.tracer.getMinSeverity() <= EEventSeverity.DEBUG.getValue().longValue()) {
                parent.tracer.debug("Service request sent to " + clientChannel + ": " + rqEnvBodyDoc.xmlText(), true);
            }
        } catch (IOException e) {
            close(EGenerateDisonnect.NO, ERemoveFromParent.YES);
            responseNotify(new ServiceCallSendException("Service invocation error: " + ExceptionTextFormatter.getExceptionMess(e), e));
            return;
        }
        parent.dispatcher.waitEvent(new SocketChannelPort.ReceiveEvent(port), this, maxFinishTime);
    }

    private void responseNotify(final XmlObject rs) {
        parent.dispatcher.notify(new ResponseEvent(this, null, rs));
        busy = false;
    }

    private void responseNotify(final ServiceClientException exception) {
        parent.dispatcher.notify(new ResponseEvent(this, exception, null));
        busy = false;
    }

    private void afterConnect() throws IOException {
        boolean isSsl = sap.getSecurityProtocol() == EPortSecurityProtocol.SSL;
        parent.tracer.debug("SAP '" + sap.getName() + "' connected over " + (isSsl ? "secure" : "plaintext") + " connection (" + clientChannel + ")", false);
        reconnectAttempts = 0;
        if (clientChannel instanceof SocketClientChannel) {
            if (isSsl) {
                SSLContext sslContext;
                try {
                    sslContext = CertificateUtils.prepareServerSslContext(sap.getClientKeyAliases(), sap.getServerCertAliases());
                } catch (Throwable e) {
                    throw new IOException(Messages.ERR_CANT_CREATE_SSLCONTEXT + e.toString(), e);
                }
                port = new SocketChannelPort(parent.dispatcher, parent.tracer, (SocketChannel) clientChannel.getSelectableChannel(), ChannelPort.FRAME_HTTP_RS, ChannelPort.FRAME_HTTP_RQ);
                port.initSsl(sslContext, true, null, sap.getCipherSuites());
            } else {
                port = new SocketChannelPort(parent.dispatcher, parent.tracer, (SocketChannel) clientChannel.getSelectableChannel(), ChannelPort.FRAME_HTTP_RS, ChannelPort.FRAME_HTTP_RQ);
            }
        } else if (clientChannel instanceof PipeClientChannel) {
            port = new PipeChannelPort(parent.dispatcher, parent.tracer, (BidirectionalPipe) clientChannel.getSelectableChannel(), ChannelPort.FRAME_HTTP_RS, ChannelPort.FRAME_HTTP_RQ);
        } else if (clientChannel instanceof JmsClientChannel) {
            port = new JmsChannelPort((JmsClientChannel) clientChannel, parent.dispatcher, parent.tracer);
        }
        afterPortInitialized(true);
    }

    private void afterPortInitialized(boolean forceStartRead) throws IOException {
        if (forceStartRead) {
            port.startRead();
        }
        try {
            processorFactory = parent.createMessageProcessorFactory(sap);
        } catch (WSDLException ex) {
            throw new IOException("Error while reading wsdl data", ex);
        }
        parent.dispatcher.waitEvent(new SocketChannelPort.DisconnectEvent(port), this, -1);
        call();
    }

    @Override
    public void onEvent(Event event) {
        if (event.getClass() == EventDispatcher.ConnectEvent.class) {
            String exMess = null;
            if (!event.isExpired) {
                try {
                    clientChannel.finishConnect();
                    afterConnect();
                    return;
                } catch (IOException e) {
                    //go retry
                    exMess = ExceptionTextFormatter.getExceptionMess(e);
                }
                //expired or error - retry
            }
            sap.block();
            if (System.currentTimeMillis() < maxFinishTime) {
                close(EGenerateDisonnect.NO, ERemoveFromParent.NO);
                parent.tracer.put(EEventSeverity.EVENT, "Connection to SAP '" + sap.getName() + "' failed" + (exMess != null ? " (" + exMess + ")" : "") + ", retry", null, null, false);
                connect(false);
            } else {
                close(EGenerateDisonnect.NO, ERemoveFromParent.YES);
                responseNotify(new ServiceCallTimeout("Connection time out"));
            }
        } else if (event.getClass() == EventDispatcher.TimerEvent.class) {
            connect(true);
        } else if (event.getClass() == SocketChannelPort.ReceiveEvent.class) {
            if (event.isExpired) {
                close(EGenerateDisonnect.NO, ERemoveFromParent.YES);
                responseNotify(new ServiceCallTimeout("Response wait timeout"));
            } else {
                SocketChannelPort.Frame frame = ((SocketChannelPort.ReceiveEvent) event).frame;

                boolean wasRealResponce = false;
                SapClientOptions currentSap = sap;

                try {
                    boolean doKeepConnect = false;
                    if (keepConnect && frame.attrs != null) {
                        doKeepConnect = HttpFormatter.getKeepConnectionAlive(frame.attrs);
                    }

                    try {
                        final XmlObject rs = unwrapMessage(frame);
                        final ChannelPort portSnapshot = port;//port is cleared in keepConnection()
                        if (doKeepConnect) {
                            keepConnection();
                            close(EGenerateDisonnect.NO, ERemoveFromParent.NO);
                        } else {
                            close(EGenerateDisonnect.NO, ERemoveFromParent.YES);
                        }
                        final long curMillis = System.currentTimeMillis();
                        parent.tracer.debug("Received response from " + sap.getName() + "(" + portSnapshot.getShortDescription() + ", spent in AAS: " + (curMillis - lastSendTimeMillis) + " ms, spent on busy processing: " + (lastSendTimeMillis - invokeTimeMillis), false);
                        wasRealResponce = true;
                        responseNotify(rs);
                    } catch (ServiceCallException e) {
                        close(EGenerateDisonnect.NO, ERemoveFromParent.YES);
                        responseNotify(e);
                    } catch (ServiceCallFault e) {
                        boolean isRetryIndicator = false;
                        if (e.getFaultCode() != null) {
                            if (e.getFaultCode().equals(ServiceProcessFault.FAULT_CODE_SERVER_BUSY)) {
                                parent.notifySapUnusable(sap.getName(), "busy");
                                isRetryIndicator = true;
                            } else if (e.getFaultCode().equals(ServiceProcessFault.FAULT_CODE_SERVER_SHUTDOWN)) {
                                parent.notifySapUnusable(sap.getName(), "shutting down");
                                isRetryIndicator = true;
                            }
                        }
                        if (!isRetryIndicator) {
                            if (!doKeepConnect) {
                                close(EGenerateDisonnect.NO, ERemoveFromParent.YES);
                            }
                            wasRealResponce = true;
                            responseNotify(e);
                        } else {
                            if (doKeepConnect) {
                                keepConnection();
                            }
                            close(EGenerateDisonnect.NO, ERemoveFromParent.NO);
                            sap.blockOnBusy(); //short block
                            connect(false, (ChannelPort) event.getSource()); //retry
                        }
                    }
                } finally {
                    if (wasRealResponce) {
                        currentSap.setNotBusy();
                    }
                }
            }
        } else if (event.getClass() == SocketChannelPort.DisconnectEvent.class) {
            close(EGenerateDisonnect.NO, ERemoveFromParent.YES);
            responseNotify(new ServiceCallException("Service server disconnected"));
        } else {
            throw new RadixError("Invalid event " + event);
        }
    }

    private void keepConnection() {
        if (port == null || clientChannel == null) {
            return;
        }
        if (parent == null || parent.dispatcher == null) {
            return;
        }
        try {
            parent.dispatcher.unsubscribeFromSource(port);
            parent.dispatcher.unsubscribeFromSource(clientChannel.getSelectableChannel(), false);
            port.setAutoReadContinuation(false);
            final KeptConnection keptConnection = new KeptConnection(clientChannel, port, KEEP_CONNECTION_MILLIS);
            port = null;
            clientChannel = null;
            final KeptConnection oldKeptConnection = keptConnections.put(sap.getConnectionId(), keptConnection);
            if (oldKeptConnection != null) {
                oldKeptConnection.discard();
            }
        } catch (RuntimeException ex) {
            parent.tracer.put(EEventSeverity.WARNING, "Unable to keep connection to " + sap.getName() + " :" + ExceptionTextFormatter.throwableToString(ex), null, null, false);
        }
    }

    private XmlObject unwrapMessage(final SocketChannelPort.Frame frame) throws ServiceCallFault, ServiceCallRecvException {
        try {
            try {
                if (messageProcessor == null) {
                    throw new ProcessException("There is no message processor for incoming data");
                }
                return messageProcessor.unwrapResponce(frame.packet, resultClass);
            } finally {
                messageProcessor = null;
            }
        } catch (ProcessException e1) {
            throw new ServiceCallRecvException("Can't parse response: " + String.valueOf(e1.getMessage()), e1);
        }
    }

    static public class ResponseEvent extends Event {

        final public ServiceClientSeance source;
        final public ServiceClientException exception;
        final public XmlObject response;

        public ResponseEvent(ServiceClientSeance source) {
            this.source = source;
            this.exception = null;
            this.response = null;
        }

        public ResponseEvent(ServiceClientSeance source, ServiceClientException exception, XmlObject response) {
            this.source = source;
            this.exception = exception;
            this.response = response;
        }

        @Override
        public Object getSource() {
            return source;
        }

        @Override
        public boolean matchs(Object o) {
            return o.getClass() == getClass() && source == ((ServiceClientSeance.ResponseEvent) o).source;
        }
    }

    private class KeptConnection implements EventHandler {

        private final ChannelPort port;
        private final ClientChannel clientChannel;
        private final long finishTimeMillis;
        private boolean closeHappened = false;

        public KeptConnection(final ClientChannel channel, final ChannelPort port, final long timeoutMillis) {
            this.port = port;
            this.clientChannel = channel;
            finishTimeMillis = timeoutMillis > 0 ? System.currentTimeMillis() + timeoutMillis : -1;
            final EventDispatcher.ReadEvent readEvent = new EventDispatcher.ReadEvent(clientChannel.getSelectableChannel(), null);
            readEvent.ignorableOnClose = true;
            parent.dispatcher.waitEvent(readEvent, this, -1);
        }

        @Override
        public void onEvent(Event ev) {
            if (ev instanceof EventDispatcher.ReadEvent) {
                //error or unexpected data - close anyway
                discard();
                closeHappened = true;
            } else {
                parent.tracer.put(EEventSeverity.WARNING, "Unsupported event received: " + ev, null, null, false);
            }
        }

        public boolean bringBack() {
            if (closeHappened) {
                return false;
            }
            if ((finishTimeMillis > 0 && finishTimeMillis < System.currentTimeMillis())
                    || !port.isConnected()) {
                if (!port.isConnected() && port.getStackOnCloseHolder() != null) {
                    parent.tracer.put(EEventSeverity.WARNING, "Channel that should be alive is already closed: " + ExceptionTextFormatter.throwableToString(port.getStackOnCloseHolder()), null, null, false);
                }
                discard();
                return false;
            }
            parent.dispatcher.unsubscribeFromSource(clientChannel.getSelectableChannel(), false);
            port.setAutoReadContinuation(true);
            return true;
        }

        public void discard() {
            if (clientChannel != null) {
                parent.dispatcher.unsubscribe(clientChannel.getSelectableChannel());
                try {
                    clientChannel.close();
                } catch (IOException ex) {
                    //ignore
                }
            }
        }
    }
}
