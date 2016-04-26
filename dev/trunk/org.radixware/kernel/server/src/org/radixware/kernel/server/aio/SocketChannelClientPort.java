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
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.trace.LocalTracer;

public class SocketChannelClientPort extends SocketChannelPort {

    private static final long RETRY_LOG_PERIOD_MILLIS = 10000;

//------------------------------------ Public -----------------------------------------------------	
    public SocketChannelClientPort(EventDispatcher dispatcher, LocalTracer tracer, InetSocketAddress serverAddress, int connectTryPeriod, InetSocketAddress myAddress, String recvFrameFormat, String sendFrameFormat) {
        super(dispatcher, tracer, null, recvFrameFormat, sendFrameFormat);
        this.myAddress = myAddress;
        this.serverAddress = serverAddress;
        this.connectTryPeriod = connectTryPeriod;
    }
    private boolean started = false;
    long lastRetryLogTime = 0;

    public void start() {
        started = true;
        connect();
    }

    @Override
    public void stop() {
        if (channel != null) {
            sendBuffer.clear();
            super.close(ECloseMode.FORCED);
            dispatcher.unsubscribeFromTimerEvents(this);
        }
        started = false;
        super.stop();
    }

    public void reconnect() {
        if (channel != null) {
            sendBuffer.clear();
            super.close(ECloseMode.FORCED);
            connect();
        }
    }

    @Override
    public void close() {
        super.close(ECloseMode.FORCED);
        if (started) {
            dispatcher.waitEvent(new EventDispatcher.TimerEvent(), this, System.currentTimeMillis() + connectTryPeriod);
        }
    }
//	--------------------------------- Private ----------------------------------------------------
    private final InetSocketAddress myAddress;
    private final InetSocketAddress serverAddress;
    private final int connectTryPeriod;

    private void connect() {
        if (channel != null) {
            throw new RadixError("Client channel already exists");
        }
        try {
            reset();
            channel = SocketChannel.open();
            channel.configureBlocking(false);
            try {
                channel.socket().setTcpNoDelay(true);
            } catch (IOException ex) {
                //Microsoft bug
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            try {
                channel.socket().setKeepAlive(true);
            } catch (IOException ex) {
                //Vista bug
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            if (myAddress != null) {
                if (myAddress.isUnresolved()) {
                    throw new IOException("Can't resolve client socket's address: " + myAddress);
                }
                if (myAddress.getPort() > 0) {
                    channel.socket().setReuseAddress(true);
                }
                channel.socket().bind(myAddress);
            }
            if (channel.connect(serverAddress)) {
                afterConnect();
            } else {
                dispatcher.waitEvent(new EventDispatcher.ConnectEvent(channel), this, System.currentTimeMillis() + connectTryPeriod + 10000);
            }
            isClosed = false;
        } catch (IOException e) {
            tracer.debug("Client port connection error: " + e, false);
            close(); //schedule retry
        }
    }

    private void afterConnect() {
        tracer.debug("Client port connected", false);
        updateDescription();
        dispatcher.notify(new ConnectEvent(this));
        startRead();
    }

    @Override
    public void onEvent(final Event event) {
        if (event.getClass() == EventDispatcher.ConnectEvent.class) {
            if (event.isExpired) {
                tracer.debug("Client port connection timeout", false);
                close(); //schedule retry

            } else {
                try {
                    channel.finishConnect();
                    afterConnect();
                    lastRetryLogTime = 0;
                } catch (IOException e) {
                    if (System.currentTimeMillis() - lastRetryLogTime > RETRY_LOG_PERIOD_MILLIS) {
                        tracer.debug("Client port connection establishment error: " + e, false);
                        lastRetryLogTime = System.currentTimeMillis();
                    }
                    close(); //schedule retry

                }
            }
        } else if (event.getClass() == EventDispatcher.TimerEvent.class) {
            if (started && channel == null) {
                connect();
            }
        } else {
            super.onEvent(event);
        }
    }
}