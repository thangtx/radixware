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

import org.radixware.kernel.common.utils.net.SapAddress;
import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLContext;

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.enums.EClientAuthentication;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EHttpParameter;
import org.radixware.kernel.common.enums.EPortSecurityProtocol;
import org.radixware.kernel.common.exceptions.CertificateUtilsException;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.soap.IServerMessageProcessorFactory;
import org.radixware.kernel.common.soap.IServerSoapMessageProcessor;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.SoapFormatter;
import org.radixware.kernel.common.utils.net.JmsServerChannel;
import org.radixware.kernel.common.utils.io.pipe.BidirectionalPipe;
import org.radixware.kernel.common.utils.io.pipe.ServerPipe;
import org.radixware.kernel.common.utils.net.PipeServerChannel;
import org.radixware.kernel.common.utils.net.ServerChannel;
import org.radixware.kernel.common.utils.net.SocketServerChannel;
import org.radixware.kernel.server.soap.DefaultServerMessageProcessorFactory;

public class ServiceServer implements EventHandler {

    public static class InvocationEvent extends Event {

        final public ServiceServer source;
        final public ServiceServerSeance seance;
        final public XmlObject rqEnvBodyContent;
        final public Map<String, String> rqFrameAttrs;

        public InvocationEvent(ServiceServer source) {
            this.source = source;
            this.rqEnvBodyContent = null;
            this.seance = null;
            this.rqFrameAttrs = null;
        }

        public InvocationEvent(ServiceServer source, ServiceServerSeance seance, XmlObject rqEnvBodyContent, Map<String, String> rqFrameAttrs) {
            this.source = source;
            this.seance = seance;
            this.rqEnvBodyContent = rqEnvBodyContent;
            this.rqFrameAttrs = rqFrameAttrs;
        }

        @Override
        public Object getSource() {
            return source;
        }

        @Override
        public boolean matchs(Object o) {
            return o.getClass() == getClass() && source == ((InvocationEvent) o).source;
        }
    }
    protected EventDispatcher dispatcher;
    protected LocalTracer tracer;
    protected SapAddress myAddress;
    protected Map<String, String> rsFrameAttrs = new HashMap<>();
    protected Map<String, String> faultFrameAttrs = new HashMap<>();
    protected List<ServiceServerSeance> seances = new ArrayList<>();
    private final int maxSeanceCount;
    private final int rqWaitTimeout;
    private ServerChannel serverChannel;
    private final SSLContext sslContext;
    private final EClientAuthentication sslClientAuth;
    private final List<String> cipherSuites;
    protected final IServerMessageProcessorFactory messageProcessorFactory;

    /**
     * @param dispatcher
     * @param tracer
     * @param myAddress
     * @param maxSeanceCount
     * @param rqWaitTimeout in seconds
     * @param sslContext prepared SSLContext; if NULL, server will use plaintext
     * connection
     * @param sslClientAuth
     */
    public ServiceServer(
            final EventDispatcher dispatcher,
            final LocalTracer tracer,
            final SapAddress myAddress,
            final int maxSeanceCount,
            final int rqWaitTimeout,
            final SSLContext sslContext,
            final EClientAuthentication sslClientAuth,
            final List<String> cipherSuites) {
        this(dispatcher, tracer, myAddress, maxSeanceCount, rqWaitTimeout, sslContext, sslClientAuth, cipherSuites, null);
    }

    /**
     * @param dispatcher
     * @param tracer
     * @param myAddress
     * @param maxSeanceCount
     * @param rqWaitTimeout in seconds
     * @param sslContext prepared SSLContext; if NULL, server will use plaintext
     * connection
     * @param sslClientAuth
     */
    public ServiceServer(
            final EventDispatcher dispatcher,
            final LocalTracer tracer,
            final SapAddress myAddress,
            final int maxSeanceCount,
            final int rqWaitTimeout,
            final SSLContext sslContext,
            final EClientAuthentication sslClientAuth,
            final List<String> cipherSuites,
            final IServerMessageProcessorFactory messageProcessorFactory) {
        this.tracer = tracer;
        this.dispatcher = dispatcher;
        this.myAddress = myAddress;
        this.maxSeanceCount = maxSeanceCount;
        this.rqWaitTimeout = rqWaitTimeout;
        this.sslContext = sslContext;
        this.sslClientAuth = sslClientAuth;
        rsFrameAttrs.put(EHttpParameter.HTTP_RESP_STATUS_PARAM.getValue(), "200");
        rsFrameAttrs.put(EHttpParameter.HTTP_RESP_REASON_PARAM.getValue(), "OK");
        rsFrameAttrs.put(EHttpParameter.HTTP_CONTENT_TYPE_ATTR.getValue(), "text/xml; charset=\"UTF-8\"");
        faultFrameAttrs.put(EHttpParameter.HTTP_RESP_STATUS_PARAM.getValue(), "500");
        faultFrameAttrs.put(EHttpParameter.HTTP_RESP_REASON_PARAM.getValue(), "Internal Server Error");
        faultFrameAttrs.put(EHttpParameter.HTTP_CONTENT_TYPE_ATTR.getValue(), "text/xml; charset=\"UTF-8\"");
        this.cipherSuites = cipherSuites;
        this.messageProcessorFactory = messageProcessorFactory == null ? new DefaultServerMessageProcessorFactory(null, tracer, null) : messageProcessorFactory;
    }

    public void start() throws IOException {
        if (myAddress.getKind() == SapAddress.EKind.INET_SOCKET_ADDRESS) {
            serverChannel = new SocketServerChannel(new SocketServerChannel.SecurityOptions() {
                @Override
                public EPortSecurityProtocol getSecurityProtocol() {
                    return sslContext == null ? EPortSecurityProtocol.NONE : EPortSecurityProtocol.SSL;
                }

                @Override
                public SSLContext getSSLContext() throws CertificateUtilsException {
                    return sslContext;
                }

                @Override
                public EClientAuthentication getClientAuthMode() {
                    return sslClientAuth;
                }

                @Override
                public List<String> getCipherSuites() {
                    return cipherSuites;
                }
            });
        } else if (myAddress.getKind() == SapAddress.EKind.INTERNAL_PIPE_ADDRESS) {
            serverChannel = new PipeServerChannel();
        } else if (myAddress.getKind() == SapAddress.EKind.JMS_ADDRESS) {
            serverChannel = new JmsServerChannel(tracer);
        } else {
            throw new IllegalArgumentException("Unsupported channel type: " + myAddress.getKind());
        }
        serverChannel.open(myAddress);
        serverChannel.getSelectableChannel().configureBlocking(false);
        dispatcher.waitEvent(new EventDispatcher.AcceptEvent(serverChannel.getSelectableChannel()), this, -1);
    }

    public void stop() {
        if (serverChannel != null) {
            dispatcher.unsubscribe(new EventDispatcher.AcceptEvent(serverChannel.getSelectableChannel()));
            try {
                serverChannel.close();
            } catch (IOException e) {
                //do nothing
            }
        }
        while (!seances.isEmpty()) {
            seances.get(0).close();
        }
        serverChannel = null;
    }

    public ServiceServerSeance createSeance(ChannelPort port) {
        return new ServiceServerSeance(this, port, rqWaitTimeout);
    }

    @Override
    public void onEvent(Event event) {
        if (event.getClass() == EventDispatcher.AcceptEvent.class) {
            ChannelPort port = null;
            Closeable acceptedSocket = null;
            try {
                if (myAddress.getKind() == SapAddress.EKind.INET_SOCKET_ADDRESS) {
                    final SocketChannel s = ((ServerSocketChannel) serverChannel.getSelectableChannel()).accept();
                    acceptedSocket = s;
                    s.configureBlocking(false);
                    try {
                        s.socket().setTcpNoDelay(true);
                    } catch (IOException e) {
                        //Microsoft bug
                    }
                    try {
                        s.socket().setKeepAlive(true);
                    } catch (IOException e) {
                        //Vista bug
                    }
                    tracer.debug("Connection accepted from " + s.socket().getRemoteSocketAddress(), false);
                    port = new SocketChannelPort(dispatcher, tracer, s, SocketChannelPort.FRAME_HTTP_RQ, SocketChannelPort.FRAME_HTTP_RS);
                    if (sslContext != null) {
                        port.initSsl(sslContext, false, sslClientAuth, cipherSuites);
                    }
                } else {
                    final BidirectionalPipe p;
                    p = ((ServerPipe) serverChannel.getSelectableChannel()).accept(-1);
                    if (p == null) {
                        throw new IOException("Accept event is reported, but there is no accepted connection. Maybe, client has disconnected.");
                    }
                    acceptedSocket = p;
                    p.configureBlocking(false);
                    tracer.debug("Connection accepted via internal pipe", false);
                    port = new PipeChannelPort(dispatcher, tracer, p, SocketChannelPort.FRAME_HTTP_RQ, SocketChannelPort.FRAME_HTTP_RS);
                }
                if (seances.size() >= maxSeanceCount) {
                    tracer.put(EEventSeverity.WARNING, "Service seance limit overflow", null, null, false);
                    SocketChannelPort.Frame frame = new SocketChannelPort.Frame();
                    frame.packet = SoapFormatter.prepareFault(ServiceProcessFault.FAULT_CODE_SERVER_BUSY, "ServerBusy", new SoapFormatter.DefaultFaultDetailWriter("Service Access Point at " + myAddress + " is busy", null, null), null);
                    frame.attrs = new HashMap<>(faultFrameAttrs);
                    frame.attrs.put(EHttpParameter.HTTP_CONNECTION_ATTR.getValue(), "close");
                    port.send(frame.packet, frame.attrs);
                    port.close();
                } else {
                    seances.add(createSeance(port));
                }
            } catch (IOException e) {
                tracer.put(EEventSeverity.ERROR, "Socket accept error: " + e, null, null, false);
                if (acceptedSocket != null) {
                    try {
                        acceptedSocket.close();
                    } catch (IOException e1) {
                        //do nothing
                    }
                }
                if (port != null) {
                    port.close();
                }
            }
            dispatcher.waitEvent(new EventDispatcher.AcceptEvent(serverChannel.getSelectableChannel()), this, -1);
        } else {
            throw new RadixError("Invalid event " + event);
        }
    }

    public List<ServiceServerSeance> getSeances() {
        return new ArrayList<>(seances);
    }

}
