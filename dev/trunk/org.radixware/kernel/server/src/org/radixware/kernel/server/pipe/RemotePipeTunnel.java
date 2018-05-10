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
package org.radixware.kernel.server.pipe;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.io.pipe.BidirectionalPipe;
import org.radixware.kernel.common.utils.io.pipe.PipeAddress;
import org.radixware.kernel.server.aio.ChannelPort;
import org.radixware.kernel.server.aio.Event;
import org.radixware.kernel.server.aio.EventDispatcher;
import org.radixware.kernel.server.aio.EventHandler;
import org.radixware.kernel.server.aio.SocketChannelPort;

/**
 * Tunnel for remote connections. Serves as a proxy for {@link BidirectionalPipe}
 * while connecting to remote ServerPipe identified by {@link PipeAddress}.
 * A single tunnel used for multiple connections to the same remote instance 
 * (a combination of host+port+instanceId). Each proxied/tunnelled connection 
 * is represented as {@link RemotePipeSeance}.
 */
public class RemotePipeTunnel implements EventHandler {
    private final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(getClass());

    private final List<RemotePipeSeance> seances = new LinkedList<>();
    private final SocketChannelPort socketChannelPort;
    private final EventDispatcher dispatcher;
    private final LocalTracer tracer;
    private final int instanceId;
    /** Counter of the created tunnels */
    private final AtomicInteger seanceSeq = new AtomicInteger(0);
    /** Counter of connections through the tunnel */
    private final AtomicInteger conCnt = new AtomicInteger();
    /** Counter of requests through the tunnel */
    private final AtomicInteger reqCnt = new AtomicInteger();
    
    /**
     * Creates a tunnel to the remote Instance identified by {@link RemotePipeTunnelAddress}.
     * Note, that this constructor is not thread-safe and should be called from
     * {@link EventDispatcher} loop.
     * @param tunnelAddress target tunnel address of the remote {@link Instance}
     * @param remotePipeManager owner {@link RemotePipeManager}
     * @param dispatcher {@link EventDispatcher} handling operations on this tunnel
     * @param tracer {@link LocalTracer} for debugging purposes
     * @throws IOException on any network operations failures
     */
    public RemotePipeTunnel(RemotePipeTunnelAddress tunnelAddress, final RemotePipeManager remotePipeManager,
            final EventDispatcher dispatcher, final LocalTracer tracer) throws IOException {
        this.dispatcher = dispatcher;
        this.tracer = tracer;
        instanceId = tunnelAddress.getRemoteInstanceId();
        final SocketChannel clientSocket = SocketChannel.open();
        clientSocket.bind(null);
        clientSocket.configureBlocking(false);
        dispatcher.waitEvent(new EventDispatcher.ConnectEvent(clientSocket), this, -1);
        clientSocket.connect(new InetSocketAddress(tunnelAddress.getRemoteHost(), tunnelAddress.getRemotePort()));

        SocketChannelPort clientPort = new SocketChannelPort(dispatcher, tracer, clientSocket, RemotePipeManager.RECV_FORMAT, RemotePipeManager.SEND_FORMAT); //"%S%P%E", "%S%P%E");
        dispatcher.waitEvent(new ChannelPort.ConnectEvent(clientPort), this, -1);
        dispatcher.waitEvent(new ChannelPort.ReceiveEvent(clientPort), this, -1);
        socketChannelPort = clientPort;
        socketChannelPort.setAutoReadContinuation(true);
    }

    /**
     * Connects given {@link BidirectionalPipe} to remote {@link PipeAddress},
     * creates {@link RemotePipeSeance} representing this connection and puts
     * it in the seances list.
     * @param pipeAddress remote target
     * @param pipe local client pipe
     */
    public void connect(PipeAddress pipeAddress, BidirectionalPipe pipe) {
        if (LOG.isTraceEnabled()) LOG.trace("----- TUNNEL - connecting (" + instanceId + "): pipe " + pipe+" to address "+pipeAddress);
        seances.add(new RemotePipeSeance(dispatcher, tracer, pipe, socketChannelPort, pipeAddress.getAddress(), pipeAddress.getRemoteInstanceId(), seanceSeq.incrementAndGet()));
        conCnt.incrementAndGet();
    }
    
    public void close() {
        LOG.info("----- TUNNEL - closing (" + instanceId + "): connections served " + conCnt.get() + ", requests served " + reqCnt.get());
        // todo: seances should be properly notified on tunnel shutdown
    }

    @Override
    public void onEvent(Event ev) {
        if (LOG.isTraceEnabled()) LOG.trace("----- TUNNEL - event (" + instanceId + "): " + ev + " on " + ev.getSource());
        if (ev.getClass() == EventDispatcher.ConnectEvent.class) {
            try {
                SocketChannel clientSocket = (SocketChannel) ev.getSource();
                clientSocket.finishConnect();
                socketChannelPort.startRead();
            } catch (IOException ex) {
                Logger.getLogger(RemotePipeManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (ev.getClass() == ChannelPort.ConnectEvent.class) {
            final ChannelPort clientPort = (ChannelPort) ev.getSource();
            clientPort.startRead();
            dispatcher.waitEvent(new ChannelPort.ReceiveEvent(clientPort), this, -1);//System.currentTimeMillis() + timeoutMillis);
            dispatcher.waitEvent(new ChannelPort.DisconnectEvent(clientPort), this, -1);
        } else if (ev.getClass() == ChannelPort.ReceiveEvent.class) {
            SocketChannelPort clientPort = (SocketChannelPort)ev.getSource();
            dispatcher.waitEvent(new ChannelPort.ReceiveEvent(clientPort), this, -1);
            //todo isexpired
            reqCnt.incrementAndGet();
            ChannelPort.Frame frame = ((ChannelPort.ReceiveEvent)ev).frame;
            if (LOG.isTraceEnabled()) LOG.trace("-<--- TUNNEL - received frame on client side "+new String(frame.rowData, StandardCharsets.UTF_8).replaceAll("[\n\r]", " "));
            final String seanceIdStr = frame.attrs.get(RemotePipeManager.ATTR_SEANCE_ID);
            int seanceId = Integer.valueOf(seanceIdStr);
            BidirectionalPipe pipe = get(seanceId);
            try {
                pipe.write(ByteBuffer.wrap(frame.packet));
            } catch (IOException ex) {
                Logger.getLogger(RemotePipeTunnel.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (LOG.isTraceEnabled()) LOG.trace("-<--- TUNNEL - READ EVENT " + ev + " to " + ev.getSource());
        } else {
            LOG.warn("----- TUNNEL - UNKNOWN EVENT " + ev + " to " + ev.getSource());
        }
    }

    BidirectionalPipe get(long seanceId) {
        for (RemotePipeSeance pipe : seances) {
            if (pipe.getSeanceId() == seanceId) {
                return pipe.getBiDirectionalPipe();
            }
        }
        return null;
    }
    
}
