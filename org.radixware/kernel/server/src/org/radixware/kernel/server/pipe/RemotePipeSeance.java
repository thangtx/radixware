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
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base32;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.exceptions.ServiceCallTimeout;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.io.pipe.AcceptedBidirectionalPipe;
import org.radixware.kernel.common.utils.io.pipe.BidirectionalPipe;
import org.radixware.kernel.server.aio.ChannelPort;
import org.radixware.kernel.server.aio.Event;
import org.radixware.kernel.server.aio.EventDispatcher;
import org.radixware.kernel.server.aio.EventHandler;
import org.radixware.kernel.server.aio.PipeChannelPort;
import org.radixware.kernel.server.aio.SocketChannelPort;

/**
 * Represents client side of the remote connection between
 * {@link BidirectionalPipe} and {@link ServerPipe}.
 */
public class RemotePipeSeance implements EventHandler {
    private final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(getClass());
    private EventDispatcher dispatcher;

    private PipeChannelPort clientPipePort;
    private AcceptedBidirectionalPipe acceptedPipe;
    private final SocketChannelPort socketChannelPort;
    private final int seanceId;
    private final String address;
    private final int instanceId;

    /**
     * Creates a seance in the tunnel to the remote Instance.
     * Note, that this constructor is not thread-safe and should be called from
     * {@link EventDispatcher} loop.
     * @param dispatcher {@link EventDispatcher} handling operations on this tunnel
     * @param tracer {@link LocalTracer} for debugging purposes
     * @param pipe client's {@link BidirectionalPipe}
     * @param socketChannelPort tunnel's socket
     * @param address of the target {@link ServerPipe}
     * @param instanceId of the target {@link Instance}
     * @param seanceId of this seance, assigned by the calling party
     */
    public RemotePipeSeance(EventDispatcher dispatcher, LocalTracer tracer,
            BidirectionalPipe pipe, SocketChannelPort socketChannelPort, String address, int instanceId, int seanceId) {
        acceptedPipe = new AcceptedBidirectionalPipe(pipe);
        this.address = address;
        this.instanceId = instanceId;
        this.seanceId = seanceId;
        this.dispatcher = dispatcher;
        this.socketChannelPort = socketChannelPort;//TODO: do not pass socketchannelport directly
        pipe.onConnect();
        try {
            acceptedPipe.configureBlocking(false);
        } catch (IOException ex) {
            Logger.getLogger(RemotePipeManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        clientPipePort = new PipeChannelPort(dispatcher, tracer, acceptedPipe, RemotePipeManager.RECV_FORMAT, RemotePipeManager.SEND_FORMAT);
        clientPipePort.startRead();

        dispatcher.waitEvent(new ChannelPort.ReceiveEvent(clientPipePort), this, -1);
        dispatcher.waitEvent(new ChannelPort.DisconnectEvent(clientPipePort), this, -1);
    }

    private void responseNotify(final ServiceClientException exception) {
        dispatcher.notify(new ResponseEvent(this, exception, null));
    }

    @Override
    public void onEvent(Event event) {
        if (LOG.isTraceEnabled()) LOG.trace("----- SEANCE - remote pipe seance event "+event);
        if (event.getClass() == EventDispatcher.ConnectEvent.class) {
            if (!event.isExpired) {
                //expired or error - retry
                ChannelPort channelPort = (ChannelPort) event.getSource();
                channelPort.startRead();
                dispatcher.waitEvent(new ChannelPort.ReceiveEvent(channelPort), this, -1);
                dispatcher.waitEvent(new ChannelPort.DisconnectEvent(channelPort), this, -1);
            }
        } else if (event.getClass() == ChannelPort.ReceiveEvent.class) {
            if (event.isExpired) {
                responseNotify(new ServiceCallTimeout("Response wait timeout"));
            } else {
                dispatcher.waitEvent(new ChannelPort.ReceiveEvent(clientPipePort), this, -1);
                ChannelPort.Frame frame = ((ChannelPort.ReceiveEvent) event).frame;
                try {
                    if (LOG.isTraceEnabled()) LOG.trace("->--- SEANCE - Remote pipe send started on client side");
                    Map<String, String> attrs = new HashMap<>(frame.attrs);
                    attrs.put(RemotePipeManager.ATTR_ADDRESS, new Base32().encodeAsString(address.getBytes(StandardCharsets.UTF_8)).toLowerCase());
                    attrs.put(RemotePipeManager.ATTR_SEANCE_ID, Integer.toString(seanceId));
                    attrs.put(RemotePipeManager.ATTR_INSTANCE_ID, Integer.toString(instanceId));
                    socketChannelPort.send(frame.rowData, attrs);
                    if (LOG.isTraceEnabled()) LOG.trace("->--- SEANCE - Remote pipe send completed on client side");
                } catch (IOException ex) {
                    Logger.getLogger(RemotePipeSeance.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else if (event.getClass() == ChannelPort.DisconnectEvent.class) {
            if (LOG.isTraceEnabled()) LOG.trace("----- SEANCE - DISCONNECTED");
            clientPipePort.close();
            try {
                acceptedPipe.close();
            } catch (IOException ex) {
                Logger.getLogger(RemotePipeSeance.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            throw new RadixError("Invalid event " + event);
        }
    }

    public BidirectionalPipe getBiDirectionalPipe() {
        return acceptedPipe;
    }

    public long getSeanceId() {
        return seanceId;
    }
    
    /**
     * This event is used as a holder for the exception
     */
    public static class ResponseEvent extends Event {

        final public RemotePipeSeance source;
        final public ServiceClientException exception;
        final public XmlObject response;

        public ResponseEvent(RemotePipeSeance source) {
            this.source = source;
            this.exception = null;
            this.response = null;
        }

        public ResponseEvent(RemotePipeSeance source, ServiceClientException exception, XmlObject response) {
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
            return o.getClass() == getClass() && source == ((RemotePipeSeance.ResponseEvent) o).source;
        }
    }
}
