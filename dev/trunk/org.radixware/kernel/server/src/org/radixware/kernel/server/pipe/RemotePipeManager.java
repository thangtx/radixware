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

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base32;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.io.pipe.BidirectionalPipe;

import org.radixware.kernel.common.utils.io.pipe.IRemotePipeManager;
import org.radixware.kernel.common.utils.io.pipe.PipeAddress;
import org.radixware.kernel.common.utils.io.pipe.ServerPipe;
import org.radixware.kernel.common.utils.io.pipe.ServerPipeRegisry;
import org.radixware.kernel.common.utils.net.SapAddress;
import org.radixware.kernel.common.utils.net.SocketServerChannel;
import org.radixware.kernel.server.aio.ChannelPort;
import org.radixware.kernel.server.aio.Event;
import org.radixware.kernel.server.aio.EventDispatcher;
import org.radixware.kernel.server.aio.EventHandler;
import org.radixware.kernel.server.aio.SocketChannelPort;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.trace.ServerTrace;

/**
  * {@link RemotePipeManager} purpose is to provide a network proxy for
  * {@link BidirectionalPipe}. Client's {@link BidirectionalPipe} could be
  * linked to remote {@link PipeAddress} using method {@link connect()},
  * which checks if there is existing {@link RemotePipeTunnel} to the target
  * {@link PipeAddress}, and creates one if it does not exist, then
  * {@link BidirectionalPipe} is connected to remote {@link ServerPipe} through
  * {@link RemotePipeTunnel}.
  */
public class RemotePipeManager extends Thread implements EventHandler, IRemotePipeManager {
    private final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(getClass());

    /** Tracer component name */
    private static final String COMP_NAME = "Remote Pipe Manager";
    /** Maximum wait time for remote connection ready */
    private static final long MAX_START_WAIT_MILLIS = 10000;

    /** Header name for Instance Id of the remote connection target */
    public static final String ATTR_INSTANCE_ID = "instanceid";
    /** Header name for Address of the remote connection target */
    public static final String ATTR_ADDRESS = "address";
    /** Header name for Seance Id of the remote connection */
    public static final String ATTR_SEANCE_ID = "seanceid";
    /** Receive format for remote connection frames */
    public static final String RECV_FORMAT = ChannelPort.FRAME_HTTP_RQ;
    /** Send format for remote connection frames */
    public static final String SEND_FORMAT = ChannelPort.FRAME_HTTP_RS;

    /** Seq for {@link RemotePipeManager} instances numeration */
    private static final AtomicInteger REMOTE_PIPE_MANAGER_ID = new AtomicInteger();

    private final Instance instance;
    private final EventDispatcher dispatcher;
    private final ServerTrace trace;
    private final LocalTracer tracer;
    private volatile boolean stopRequested = false;
    private final SocketServerChannel serverChannel;

    /** Latch to track manager start process */
    private final CountDownLatch startLatch = new CountDownLatch(1);
    /** Map for active {@link RemotePipeTunnel} objects, stored by their corresponding {@link PipeAddress} */
    private final Map<RemotePipeTunnelAddress, RemotePipeTunnel> remotePipesByAddress = new HashMap<>();
    
    /** Creation of {@link RemotePipeTunnel} have to be processed in an
     * {@link EventDispatcher} loop, so {@link ConnectRequest} is used to
     * provide async response by calling await, which will wait for request
     * processing in {@link RemotePipeManager#processConnectRequests()}
     * called from {@link RemotePipeManager#run()}
     */
    private static class ConnectRequest {
        private final RemotePipeTunnelAddress tunnelAddress;
        private final CountDownLatch latch = new CountDownLatch(1);
        private volatile RemotePipeTunnel remotePipeTunnel;
        private PipeAddress pipeAddress;
        private BidirectionalPipe pipe;
        private ConnectRequest(RemotePipeTunnelAddress pipeAddress) {
            this.tunnelAddress = pipeAddress;
        }
        private ConnectRequest(RemotePipeTunnel remotePipeTunnel, PipeAddress pipeAddress, BidirectionalPipe pipe) {
            this.tunnelAddress = null;
            this.remotePipeTunnel = remotePipeTunnel;
            this.pipeAddress = pipeAddress;
            this.pipe = pipe;
        }
        public RemotePipeTunnelAddress getTunnelAddress() {
            return tunnelAddress;
        }
        RemotePipeTunnel await() throws InterruptedException {
            latch.await();
            return remotePipeTunnel;
        }
        void reply(RemotePipeTunnel remotePipeTunnel) {
            this.remotePipeTunnel = remotePipeTunnel;
            latch.countDown();
        }
    }
    private final ConcurrentLinkedDeque<ConnectRequest> connectRequests = new ConcurrentLinkedDeque<>();
    /**
     * This method is called from event loop {@link run()}, so operations on 
     * {@link EventDispatcher} will be executed in the single thread
     * @throws IOException 
     */
    private void processConnectRequests() throws IOException {
        while (true) {  // TODO: limit maximum execution time for single processConnectRequests call
            ConnectRequest connectRequest = connectRequests.poll();
            if (connectRequest == null) {//TODO break on timeout
                break;
            }
            final RemotePipeTunnelAddress tunnelAddress = connectRequest.getTunnelAddress();
            if (tunnelAddress == null) {
                connectRequest.remotePipeTunnel.connect(connectRequest.pipeAddress, connectRequest.pipe);
                connectRequest.reply(connectRequest.remotePipeTunnel);
            } else {
                connectRequest.reply(new RemotePipeTunnel(tunnelAddress, this, dispatcher, tracer));
            }
        };
    }

    /**
     * {@link RemotePipeManager} constructor initializes tracers, opens
     * {@link SocketServerChannel} on random available port and configures
     * {@link EventDispatcher} to get notifications on {@link AcceptEvent}
     * @param instance - owner Radix Instance
     */
    public RemotePipeManager(Instance instance) {
        if (LOG.isTraceEnabled()) LOG.trace("---------- REMOTE MANAGER - init RemotePipeManager for instance="+instance.getFullTitle());
        this.trace = ServerTrace.Factory.newInstance(instance);//TODO: refactor trace to separate thread and queue
        tracer = trace.newTracer(COMP_NAME);
        this.instance = instance;
        setName(getClass().getSimpleName()+REMOTE_PIPE_MANAGER_ID.incrementAndGet());//instance.getId());
        try {
            dispatcher = new EventDispatcher();
        } catch (IOException e) {
            throw new RuntimeException("Could not start remote pipe manager", e);
        }
        InetSocketAddress address = new InetSocketAddress(0);//TODO: take address from instance options
        serverChannel = new SocketServerChannel();
        final SelectableChannel sc;
        try {
            serverChannel.open(new SapAddress(address));
            sc = serverChannel.getSelectableChannel();
            sc.configureBlocking(false);
        } catch (IOException e) {
            throw new RuntimeException("Could not start listening on server channel", e);
        }
        dispatcher.waitEvent(new EventDispatcher.AcceptEvent(sc), this, -1);
        LOG.info("---------- REMOTE MANAGER - init completed RemotePipeManager for instance="+instance.getFullTitle());
    }

    public Instance getInstance() {
        return instance;
    }

    @Override
    public int getInstanceId() {
        return instance.getId();
    }
    
    public int getPort() {
        final SocketAddress localAddress;
        try {
            final ServerSocketChannel serverSocket = (ServerSocketChannel) serverChannel.getSelectableChannel();
            if (serverSocket==null) {
                return 0;
            }
            localAddress = serverSocket.getLocalAddress();
        } catch (IOException ex) {
            Logger.getLogger(RemotePipeManager.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
        if (localAddress instanceof InetSocketAddress) {
            return ((InetSocketAddress)localAddress).getPort();
        } else {
            return 0;
        }
    }
    
    @Override
    public void onEvent(Event ev) {
        if (LOG.isTraceEnabled()) LOG.trace("----- REMOTE MANAGER - event ("+instance.getId()+"): "+ev+" on "+ev.getSource());
        if (ev.getClass() == EventDispatcher.AcceptEvent.class) {
            if (LOG.isTraceEnabled()) LOG.trace("----- REMOTE MANAGER - CONNECT CHANNEL PORT EVENT "+ev+" to "+ev.getSource());
            ChannelPort serverPort = null;
            Closeable acceptedSocket = null;
            ServerSocketChannel sc = (ServerSocketChannel) ((EventDispatcher.AcceptEvent)ev).getSource();
            try {
                final SocketChannel s = sc.accept();
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
                serverPort = new SocketChannelPort(dispatcher, tracer, s, SocketChannelPort.FRAME_HTTP_RQ, SocketChannelPort.FRAME_HTTP_RS);
                dispatcher.waitEvent(new ChannelPort.ReceiveEvent(serverPort), this, -1);
                dispatcher.waitEvent(new ChannelPort.DisconnectEvent(serverPort), this, -1);
                serverPort.startRead();
            } catch (IOException e) {
                tracer.put(EEventSeverity.ERROR, "Socket accept error: " + e, null, null, false);
                if (acceptedSocket != null) {
                    try {
                        acceptedSocket.close();
                    } catch (IOException e1) {
                        //do nothing
                    }
                }
                if (serverPort != null) {
                    serverPort.close();
                }
            } finally {
                dispatcher.waitEvent(new EventDispatcher.AcceptEvent(sc), this, -1);
            }
        } else if (ev.getClass() == EventDispatcher.ConnectEvent.class) {
            if (LOG.isTraceEnabled()) LOG.trace("----- REMOTE MANAGER - CONNECT DISPATCHER EVENT "+ev+" to "+ev.getSource());
            try {
                SocketChannel clientSocket = (SocketChannel) ev.getSource();
                clientSocket.finishConnect();
            } catch (IOException ex) {
                Logger.getLogger(RemotePipeManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (ev.getClass() == ChannelPort.ReceiveEvent.class) { //&& ev.getSource().getClass() == SocketChannelServerPort.class
            dispatcher.waitEvent(new ChannelPort.ReceiveEvent((ChannelPort) ev.getSource()), this, -1);
            ChannelPort.Frame frame = ((ChannelPort.ReceiveEvent) ev).frame;
            if (LOG.isTraceEnabled()) LOG.trace("-->-- REMOTE MANAGER - received data on server side "+ev.getSource()+" : "+(frame==null?frame:new String(frame.rowData, StandardCharsets.UTF_8).replaceAll("[\n\r]", " ")));
            if (frame==null) {
                return;
            }
            final String seanceIdStr = frame.attrs.get(ATTR_SEANCE_ID);
            int seanceId = Integer.valueOf(seanceIdStr);
            String address = new String(new Base32().decode(frame.attrs.get(ATTR_ADDRESS).toUpperCase()), StandardCharsets.UTF_8);
            int instanceId = Integer.valueOf(frame.attrs.get(ATTR_INSTANCE_ID));
            PipeAddress pipeAddress = new PipeAddress(address);
            BidirectionalPipe pipe = new BidirectionalPipe(1000);
            try {
                ServerPipeRegisry.connect(pipeAddress, pipe);
                pipe.write(ByteBuffer.wrap(frame.packet));//TODO: refactor to async events
                ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1000);
                pipe.read(byteBuffer);
                byteBuffer.flip();
                byte[] bytesArray = new byte[byteBuffer.remaining()];
                byteBuffer.get(bytesArray, 0, bytesArray.length);
                Map<String, String> attrs = new HashMap<>();
                attrs.put(RemotePipeManager.ATTR_ADDRESS, new Base32().encodeAsString(address.getBytes(StandardCharsets.UTF_8)).toLowerCase());
                attrs.put(RemotePipeManager.ATTR_SEANCE_ID, Integer.toString(seanceId));
                attrs.put(RemotePipeManager.ATTR_INSTANCE_ID, Integer.toString(instanceId));
                ((ChannelPort)((ChannelPort.ReceiveEvent) ev).getSource()).send(bytesArray, attrs);
                if (LOG.isTraceEnabled()) LOG.trace("---<- REMOTE MANAGER - sent response on server side "+ev.getSource()+" : "+new String(bytesArray, StandardCharsets.UTF_8).replaceAll("[\n\r]", " "));
            } catch (IOException ex) {
                Logger.getLogger(RemotePipeManager.class.getName()).log(Level.SEVERE, null, ex);
                // TODO: send failure back to client
            }
        } else if (ev.getClass() == ChannelPort.DisconnectEvent.class) {
            if (LOG.isTraceEnabled()) LOG.trace("----- REMOTE MANAGER - DISCONNECT EVENT "+ev+" to "+ev.getSource());
        } else {
            LOG.info("----- REMOTE MANAGER - UNKNOWN EVENT "+ev+" to "+ev.getSource()+" ");
        }
    }

    /**
     * Bind operation on RemotePipeManager has no effects
     */
    @Override
    public void bind(PipeAddress addr, ServerPipe server) {
        //TODO:remove this method
        LOG.info("----- REMOTE MANAGER - Bind "+addr+ " to "+server);
    }

    /**
     * Free operation on RemotePipeManager has no effects
     */
    @Override
    public void free(PipeAddress addr) {
        //TODO:remove this method
        LOG.info("----- REMOTE MANAGER - Free remote pipe "+addr);
    }


    @Override
    public void connect(PipeAddress pipeAddress, BidirectionalPipe pipe) {
        if (LOG.isTraceEnabled()) LOG.trace("----- REMOTE MANAGER - Remote pipe manager connect "+pipeAddress+" to "+pipe);
        RemotePipeTunnelAddress tunnelAddress = new RemotePipeTunnelAddress(pipeAddress.getRemoteHost(), pipeAddress.getRemotePort(), pipeAddress.getRemoteInstanceId());
    	RemotePipeTunnel remotePipeTunnel = remotePipesByAddress.get(tunnelAddress);
        //TODO: refactor to process requests on remote pipe manager thread
        // Identify remote pipe tunnel connection for the given tunnel address
        if (remotePipeTunnel == null) {
            synchronized (remotePipesByAddress) {
                remotePipeTunnel = remotePipesByAddress.get(tunnelAddress);
                if (remotePipeTunnel == null) { // Double check in case if it is assigned prior entering synchronized
                    try {
                        // RemotePipeTunnel calls EventDispatcher.waitEvent,
                        // so it is created via deque polled in processConnectRequests
                        final ConnectRequest connectRequest = new ConnectRequest(tunnelAddress);
                        connectRequests.add(connectRequest);
                        dispatcher.wakeup();
                        remotePipeTunnel = connectRequest.await(); //new RemotePipeTunnel(pipeAddress, this, dispatcher, tracer);
                        
                        remotePipesByAddress.put(tunnelAddress, remotePipeTunnel);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(RemotePipeManager.class.getName()).log(Level.SEVERE, null, ex);
                        return;
                    }
                }
            }
        }
        if (LOG.isTraceEnabled()) LOG.trace("----- REMOTE MANAGER - connect "+pipe+ " to "+pipeAddress +" through "+remotePipeTunnel);
        // Connect given pipe through pipe tunnel connection.
        // remotePipeTunnel.connect should be called from EventDispatcher loop,
        // as it creates RemotePipeSeance, which calls EventDispatcher.waitEvent
        // so it is issued through deque, polled from processConnectRequests
        final ConnectRequest connectPipeRequest = new ConnectRequest(remotePipeTunnel, pipeAddress, pipe);
        connectRequests.add(connectPipeRequest);
        dispatcher.wakeup();
        try {
            connectPipeRequest.await();
        } catch (InterruptedException ex) {
            Logger.getLogger(RemotePipeManager.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
    }
    public BidirectionalPipe getPipe(PipeAddress pipeAddress, long seanceId) {
        RemotePipeTunnelAddress tunnelAddress = new RemotePipeTunnelAddress(
                pipeAddress.getRemoteHost(), pipeAddress.getRemotePort(), pipeAddress.getRemoteInstanceId());
    	RemotePipeTunnel remotePipe = remotePipesByAddress.get(tunnelAddress);
        return remotePipe.get(seanceId);
    }
    public boolean awaitStart() throws InterruptedException {
	return startLatch.await(MAX_START_WAIT_MILLIS, TimeUnit.MILLISECONDS);
    }
    
    @Override
    public void run() {
        try {
            startLatch.countDown();
            LOG.info("---------- REMOTE MANAGER - started  RemotePipeManager on "+getPort()+" for instance="+instance.getFullTitle());
            while (!stopRequested) {
                if (isInterrupted()) {
                    break;
                }
                try {
                    processConnectRequests();
                    dispatcher.process();
                } catch (Exception ex) {
                    System.out.println("Exception in server.dispatcher.process: " + ExceptionTextFormatter.throwableToString(ex));
                    throw new IllegalStateException(ex);
                }
            }
        } finally {
            dispatcher.close();
        }
    }

    @Override
    public void interrupt() {
        try {
            shutdown();
        } finally {
            super.interrupt();
        }
    }

    public void shutdown() {
        //TODO: add graceful shutdown logic (async)
        final int port = getPort();
        try {
            serverChannel.close();
        } catch (IOException e) {
            LOG.error("Error while shutting down RemotePipeManager", e);
        }
        LOG.info("---------- REMOTE MANAGER - stopped RemotePipeManager on "+port+" for instance="+instance.getFullTitle());
        for(RemotePipeTunnel tunnel: remotePipesByAddress.values()) {
            tunnel.close();
        }
    }

}
