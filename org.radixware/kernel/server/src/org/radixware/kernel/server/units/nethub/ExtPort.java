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

package org.radixware.kernel.server.units.nethub;

import java.io.IOException;
import java.net.InetSocketAddress;

import java.net.SocketAddress;
import java.util.Arrays;
import java.util.Objects;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.exceptions.AppError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.CompositeInetSocketAddress;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.kernel.common.utils.ValueFormatter;
import org.radixware.kernel.server.aio.*;


final class ExtPort implements EventHandler {

    private static final int CLIENT_SOCKET_CONNECT_TRY_PERIOD_MILLIS = 1000;
    
    private static final String COMP_NAME = "External Port";

    final NetHubUnit unit;
    private Options options;
    private LocalTracer tracer;

    private boolean portStartScheduled = false;
    private boolean lastConnectedStatus = false;
    private boolean scheduleRestartOnDisconnect = true;
    
    private SocketChannelPort port = null;
    private SocketChannelClientPort[] clientPortsPool = null;
    private int currentClientPort = 0;
    
    ExtPort(final NetHubUnit unit) {
        this.unit = unit;
        tracer = unit.getTrace().newTracer(unit.getEventSource(), COMP_NAME);
    }
    
    private boolean switchClientPort() {
        if (clientPortsPool != null && clientPortsPool.length > 1) {
            final boolean wasLastVariant = currentClientPort == clientPortsPool.length - 1;
            
            currentClientPort = lastConnectedStatus == true ? 0 : (currentClientPort+1) % clientPortsPool.length;
            port = clientPortsPool[currentClientPort];
            
            return !wasLastVariant;
        } else
            return false;
    }

    SocketAddress getRemoteAddress() {
        if (port != null && port.getChannel() != null && port.getChannel().socket() != null) {
            return port.getChannel().socket().getRemoteSocketAddress();
        }
        return null;
    }

    final boolean isConnected() {
        return port != null && port.isConnected();
    }

    final void reconnect() throws IOException {
        if (port == null) {
            throw new IllegalUsageError("ExtPort is closed");
        }
        if (port instanceof SocketChannelServerPort) {
            final SocketChannelServerPort srvPort = (SocketChannelServerPort) port;
            srvPort.stop();
            unit.onDisconnect();
            portStartScheduled = true;
            ensureImmediateTimerEvent();
        } else if (port instanceof SocketChannelClientPort) {
            scheduleRestartClientSocket(true);
        }
    }

    final void send(final byte[] mess,String messName) throws IOException {
        if (port == null || !port.isConnected()) {
            tracer.debug("Try to send message to external system when connection is closed", false);
            return;
        }
        if (mess == null)
            return;

        port.send(mess);
        tracer.debug(messName + " sent to the external system",  false);
        tracer.debug("Sent - " + Hex.encode(mess),  true);
    }

    boolean start() {
        try {
            lastConnectedStatus = false;
            portStartScheduled = false;
            options = unit.getOptions().extPortOptions;
            if (options.isServer) {
                port = new SocketChannelServerPort(unit.getDispatcher(), tracer, options.addresses[0], options.frame, options.frame); 
                //TODO TWRBS-936 SSL support
                ((SocketChannelServerPort) port).start();
            } else {
                port = new SocketChannelClientPort(unit.getDispatcher(), tracer, options.addresses[0], CLIENT_SOCKET_CONNECT_TRY_PERIOD_MILLIS, options.localAddresses[0], options.frame, options.frame); 
                clientPortsPool = new SocketChannelClientPort[options.addresses.length];
                clientPortsPool[0]=(SocketChannelClientPort)port;
                for(int i=1; i<options.addresses.length;i++) {
                    clientPortsPool[i] = new SocketChannelClientPort(unit.getDispatcher(), tracer, options.addresses[i], CLIENT_SOCKET_CONNECT_TRY_PERIOD_MILLIS, options.localAddresses[i], options.frame, options.frame); 
                }
                //TODO TWRBS-936 SSL support
                ((SocketChannelClientPort) port).start();
                unit.getDispatcher().waitEvent(new ChannelPort.DisconnectEvent( port), this, -1); // for client port switching in case of connection failure
            }
            unit.getDispatcher().waitEvent(new ChannelPort.ConnectEvent( port), this, -1);
                   
            return true;
        } catch (IOException e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            
            tracer.put(EEventSeverity.ERROR, NetHubMessages.ERR_ON_SOCKET_START + exStack, NetHubMessages.MLS_ID_ERR_ON_SOCKET_START, new ArrStr(unit.getTitle(), exStack),  false);
            return false;
        }
    }

    void stop() {
        if (port == null) {
            return;
        }
        port.close();
        stopRecv();
        unit.getDispatcher().unsubscribe(new SocketChannelPort.DisconnectEvent(port));
        if (port instanceof SocketChannelServerPort) {
            final SocketChannelServerPort srvPort = (SocketChannelServerPort) port;
            unit.getDispatcher().unsubscribe(new SocketChannelServerPort.ConnectEvent(srvPort));
            srvPort.stop();
        } else if (port instanceof SocketChannelClientPort) {
            final SocketChannelClientPort clientPort = (SocketChannelClientPort) port;
            unit.getDispatcher().unsubscribe(new SocketChannelClientPort.ConnectEvent(clientPort));
            clientPort.stop();

        }
        portStartScheduled = false;
        port = null;
    }

    boolean restart() {
        tracer.debug("Restarting the socket",  false);
        stop();
        return start();
    }

    private void stopRecv() {
        unit.getDispatcher().unsubscribe(new SocketChannelPort.ReceiveEvent(port));
    }

    private void startRecv() {
        unit.getDispatcher().waitEvent(new SocketChannelPort.ReceiveEvent(port), this, -1);
    }

    @Override
    public void onEvent(Event ev) {
        if (ev instanceof ChannelPort.ConnectEvent) {
            lastConnectedStatus = true;
            
            if (port instanceof SocketChannelServerPort) //for SocketChannelClientPort disconnect is already waited 
                unit.getDispatcher().waitEvent(new SocketChannelPort.DisconnectEvent(port), this, -1);
            startRecv();
            SocketAddress extAddress = null;
            extAddress = getRemoteAddress();
            unit.onConnect(extAddress);
            final String extAddrStr = String.valueOf(extAddress);
            unit.getTrace().put(EEventSeverity.EVENT, NetHubMessages.EV_EXT_HOST_CONNECTED + extAddrStr, NetHubMessages.MLS_ID_EV_EXT_HOST_CONNECTED, new ArrStr(extAddrStr), unit.getEventSource(), false);
        } else if (ev instanceof SocketChannelPort.ReceiveEvent) {
            final SocketChannelPort.ReceiveEvent event = (SocketChannelPort.ReceiveEvent) ev;
            if (event.isExpired) {
                unit.getTrace().put(EEventSeverity.WARNING, NetHubMessages.EXTPORT_RECV_TIMEOUT, NetHubMessages.MLS_ID_EXTPORT_RECV_TIMEOUT, new ArrStr("?", "?"), unit.getEventSource(), false);
            } else {
                tracer.debug("Packet received from the external system", false);
                byte[] mess = event.frame.packet;
                if (mess != null){
                    tracer.debug("Received - " + Hex.encode(event.frame.packet), true);
                    unit.processExtRq(mess);
                }
            }
            if(port.isConnected()) //во время обработки запроса возможно выполнение reconnect, в этом случае не надо ждать сообщений
                startRecv();
        } else if (ev instanceof SocketChannelPort.DisconnectEvent) {
            try {
                if(lastConnectedStatus) {
                    unit.getTrace().put(EEventSeverity.WARNING, NetHubMessages.EV_EXT_HOST_DISCONNECTED, NetHubMessages.MLS_ID_EV_EXT_HOST_DISCONNECTED, null, unit.getEventSource(), false);
                    stopRecv();
                    unit.onDisconnect();
                    if (unit.getAdsUnitState() != AdsUnitState.BEFORE_STOP_PROCESSED) {
                        if (port instanceof SocketChannelServerPort) {
                            unit.getDispatcher().waitEvent(new SocketChannelServerPort.ConnectEvent((SocketChannelServerPort) port), this, -1);
                        }
                    }
                } 
                
                if (unit.getAdsUnitState() != AdsUnitState.BEFORE_STOP_PROCESSED && port instanceof SocketChannelClientPort && !unit.isShuttingDown()) {
                    if (scheduleRestartOnDisconnect) {
                        scheduleRestartClientSocket(lastConnectedStatus);
                    }
                }
                    
            } finally {
                lastConnectedStatus = false;
            }
        } else if (ev instanceof EventDispatcher.TimerEvent) { // client socket reconnect is requested
            if (portStartScheduled && port != null && unit.getAdsUnitState() != AdsUnitState.BEFORE_STOP_PROCESSED ) {
                try {
                    if (port instanceof SocketChannelClientPort) {
                        //в случае отсутствия соединения клиентский порт будет изредка писать об ошибках
                        //tracer.debug("Start client socket: "+ String.valueOf(currentClientPort)+" - "+String.valueOf(options.addresses[currentClientPort]), false);
                        ((SocketChannelClientPort) port).start();
                        unit.getDispatcher().waitEvent(new SocketChannelClientPort.ConnectEvent((SocketChannelClientPort) port), this, -1);
                        unit.getDispatcher().waitEvent(new SocketChannelClientPort.DisconnectEvent((SocketChannelClientPort) port), this, -1); // for client port switching in case of connection failure
                    } else {
                        try {
                            ((SocketChannelServerPort) port).start();
                        } catch (IOException ex) {
                            throw new RuntimeException("Error on starting server socket", ex);
                        }
                    }
                } finally {
                    portStartScheduled = false;
                }
            }
        } else {
            throw new IllegalUsageError("Invalid event " + ev);
        }
    }
    
    private void ensureImmediateTimerEvent() {
        unit.getDispatcher().unsubscribeFromTimerEvents(this);
        unit.getDispatcher().waitEvent(new EventDispatcher.TimerEvent(), this, System.currentTimeMillis());
    }
    
    private void scheduleRestartClientSocket(boolean immediate) {
        scheduleRestartOnDisconnect = false;
        try {
            if (portStartScheduled) {
                if (immediate) {//make sure that reconnect will be done immediately
                    ensureImmediateTimerEvent();
                }
                return;
            }
            port.stop(); //для предотвращения переконнекта в SocketChannelPort
            unit.getDispatcher().unsubscribe(new ChannelPort.ConnectEvent(port));
            unit.getDispatcher().unsubscribe(new ChannelPort.DisconnectEvent(port));
            unit.getDispatcher().unsubscribeFromTimerEvents(port);//todo: port should unsubscribe from timerEvents in port.stop() itself
            boolean portSwitched = switchClientPort();
            portStartScheduled = true;
            unit.getDispatcher().waitEvent(new EventDispatcher.TimerEvent(), this, System.currentTimeMillis() + ((immediate || portSwitched)? 0 : CLIENT_SOCKET_CONNECT_TRY_PERIOD_MILLIS));
        } finally {
            scheduleRestartOnDisconnect = true;
        }
    }

    static final class Options {

        final InetSocketAddress[] addresses;
        final InetSocketAddress[] localAddresses;
        final boolean isServer;
        final String frame;
        final Long recvTimeoutMillis;

        Options(
                final String strAddress,
                final boolean isServer,
                final String frame,
                final Long recvTimeoutMillis) {
                    if (strAddress == null)
                        throw new AppError("Address is not specified for external port");
                    
                    String[] strPortAddresses = strAddress.split(";");
                    this.addresses = new InetSocketAddress[strPortAddresses.length];
                    this.localAddresses = new InetSocketAddress[strPortAddresses.length];
                    for(int i=0;i< strPortAddresses.length;i++) {

                        CompositeInetSocketAddress cAddr = ValueFormatter.parseCompositeInetSocketAddress(strPortAddresses[i]);
                        this.addresses[i] = cAddr.getRemoteAddress();
                        this.localAddresses[i]=cAddr.getLocalAddress();
                    }

                    this.isServer = isServer;
                    this.frame = frame;
                    this.recvTimeoutMillis = recvTimeoutMillis;
        }

        @Override
        public boolean equals(final Object o) {
            if (o == null || !(o instanceof Options)) {
                return false;
            }
            final Options opt = (Options) o;
            return Arrays.equals(addresses, opt.addresses) &&
                   Arrays.equals(localAddresses,opt.localAddresses) &&
                   (isServer == opt.isServer) &&
                   (frame == null ? opt.frame == null : frame.equals(opt.frame)) &&
                   (recvTimeoutMillis == null ? opt.recvTimeoutMillis == null : recvTimeoutMillis.equals(opt.recvTimeoutMillis));
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 83 * hash + Arrays.deepHashCode(this.addresses);
            hash = 83 * hash + Arrays.deepHashCode(this.localAddresses);
            hash = 83 * hash + (this.isServer ? 1 : 0);
            hash = 83 * hash + Objects.hashCode(this.frame);
            hash = 83 * hash + Objects.hashCode(this.recvTimeoutMillis);
            return hash;
        }

        @Override
        public String toString() {
            String addressString = "";
            if (addresses != null) {
                for(int i=0; i<addresses.length; i++) {
                    addressString += String.valueOf(addresses[i]) + (localAddresses[i] != null ? " < " + String.valueOf(localAddresses[i]) : "") + "; ";
                }
            } else
                addressString = "null";
            return "{\n\t" +
                    (isServer ? NetHubMessages.SRV_SOCKET : NetHubMessages.CLIENT_SOCKET) + ": " + addressString + "\n\t" +
                    NetHubMessages.FRAME + (frame == null ? "" : frame) + "; \n\t" +
                    NetHubMessages.RECV_TIMEOUT_SEC + (recvTimeoutMillis == null ? "-" : String.valueOf(recvTimeoutMillis.longValue() / 1000)) + "; \n" +
                    "}";
        }
    }
}
