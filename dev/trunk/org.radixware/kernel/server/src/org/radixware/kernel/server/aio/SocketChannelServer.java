/*
 * Copyright (c) 2008-2017, Compass Plus Limited. All rights reserved.
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
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import org.radixware.kernel.common.enums.EClientAuthentication;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EPortSecurityProtocol;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.Utils;

/**
 * SocketChannelServerSeance
 *

 */
public class SocketChannelServer implements EventHandler {

//	---------------------------------------- EVENTS -------------------------------------------------------------------	
    static public class ConnectEvent extends Event {

        final public SocketChannelServer source;
        final public SocketChannelServerSeance seance;

        public ConnectEvent(SocketChannelServer source) {
            this.source = source;
            this.seance = null;
        }

        public ConnectEvent(SocketChannelServer source, SocketChannelServerSeance seance) {
            this.source = source;
            this.seance = seance;
        }

        @Override
        public Object getSource() {
            return source;
        }

        @Override
        public boolean matchs(Object o) {
            return o.getClass() == getClass() && source == ((ConnectEvent) o).source;
        }
    }

//	------------------------------------ Public -----------------------------------------------------	
    public SocketChannelServer(
            final EventDispatcher dispatcher, 
            final LocalTracer tracer, 
            final InetSocketAddress myAddress, 
            final int maxSeanceCount, 
            final String recvFrameFormat,
            final String sendFrameFormat, 
            final SSLContext sslContext, 
            final EClientAuthentication sslClientAuth, 
            final Collection<String> cipherSuites, 
            final EPortSecurityProtocol securityProtocol) {
        
        
        this.tracer = tracer;
        this.dispatcher = dispatcher;
        this.myAddress = myAddress;
        this.maxSeanceCount = maxSeanceCount;
        this.recvFrameFormat = recvFrameFormat;
        this.sendFrameFormat = sendFrameFormat;
        this.sslContext = sslContext;
        this.sslClientAuth = sslClientAuth;
        this.cipherSuites = cipherSuites;
        this.securityProtocol = sslContext == null ? EPortSecurityProtocol.NONE : Utils.nvlOf(securityProtocol, EPortSecurityProtocol.SSL);
        
    }

    public void start() throws IOException {
        serverSocket = ServerSocketChannel.open();
        serverSocket.configureBlocking(false);
        serverSocket.socket().bind(myAddress, 128);
        dispatcher.waitEvent(new EventDispatcher.AcceptEvent(serverSocket), this, -1);
    }

    public void stop() {
        dispatcher.unsubscribe(new EventDispatcher.AcceptEvent(serverSocket));
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException ex) {
                //do nothing
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }
    }

    public SocketChannelServerSeance createSeance(final SocketChannel acceptedSocket) throws IOException {
        final SocketChannelServerSeance s = new SocketChannelServerSeance(this, acceptedSocket);
        if (sslContext != null) {
            s.initSsl(sslContext, false, sslClientAuth, cipherSuites, securityProtocol);
        }
        return s;
    }
//	--------------------------------- Private ----------------------------------------------------
    protected final EventDispatcher dispatcher;
    protected final LocalTracer tracer;
    protected final InetSocketAddress myAddress;
    protected final List<SocketChannelServerSeance> seances = new ArrayList<SocketChannelServerSeance>();
    protected final String recvFrameFormat;
    protected final String sendFrameFormat;
    private final int maxSeanceCount;
    private ServerSocketChannel serverSocket = null;
    private final SSLContext sslContext;
    private final EClientAuthentication sslClientAuth;
    private final Collection<String> cipherSuites;
    private final EPortSecurityProtocol securityProtocol;

    @Override
    public void onEvent(Event event) {
        if (event.getClass() == EventDispatcher.AcceptEvent.class) {
            SocketChannelServerSeance seance = null;
            SocketChannel acceptedSocket = null;
            try {
                acceptedSocket = serverSocket.accept();
                acceptedSocket.configureBlocking(false);
                try {
                    acceptedSocket.socket().setTcpNoDelay(true);
                } catch (IOException ex) {
                    //Microsoft bug
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
                try {
                    acceptedSocket.socket().setKeepAlive(true);
                } catch (IOException ex) {
                    //Vista bug 
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
                tracer.debug("Connection accepted from " + acceptedSocket.socket().getRemoteSocketAddress(), false);
                seance = createSeance(acceptedSocket);
                if (seances.size() >= maxSeanceCount) {
                    tracer.put(EEventSeverity.WARNING, "Server seance limit overflow for " + acceptedSocket.socket().getRemoteSocketAddress(), null, null, false);
                    seance.close();
                } else if (!dispatcher.notify(new ConnectEvent(this, seance))) {
                    tracer.put(EEventSeverity.WARNING, "Unexpected connection on port server from " + acceptedSocket.socket().getRemoteSocketAddress(), null, null, false);
                    seance.close();
                } else {
                    seances.add(seance);
                }
            } catch (IOException e) {
                tracer.put(EEventSeverity.ERROR, "Socket accept error: " + e, null, null, false);
                if (acceptedSocket != null) {
                    try {
                        acceptedSocket.close();
                    } catch (IOException ex) {
                        //do nothing
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                    }
                }
                if (seance != null) {
                    seance.close();
                }
            }
            dispatcher.waitEvent(new EventDispatcher.AcceptEvent(serverSocket), this, -1);
        } else {
            throw new RadixError("Invalid event " + event);
        }
    }
}
