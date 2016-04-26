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
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;

public class SocketChannelServerPort extends SocketChannelPort {

//------------------------------------ Public -----------------------------------------------------	
    public SocketChannelServerPort(EventDispatcher dispatcher, LocalTracer tracer, InetSocketAddress myAddress, String recvFrameFormat, String sendFrameFormat) {
        super(dispatcher, tracer, null, recvFrameFormat, sendFrameFormat);
        this.myAddress = myAddress;
    }

    public void start() throws IOException {
        reset();
        serverSocket = ServerSocketChannel.open();
        serverSocket.configureBlocking(false);
        if (myAddress.isUnresolved()) {
            throw new IOException("Can't resolve server socket's address: " + myAddress);
        }
        serverSocket.socket().setReuseAddress(true);
        serverSocket.socket().bind(myAddress, 1);
        dispatcher.waitEvent(new EventDispatcher.AcceptEvent(serverSocket), this, -1);
    }

    @Override
    public void stop() {
        disconnect();
        if (serverSocket != null) {
            dispatcher.unsubscribe(new EventDispatcher.AcceptEvent(serverSocket));
            try {
                serverSocket.close();
            } catch (IOException ex) {
                //do nothing
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            serverSocket = null;
        }
        super.stop();
    }

    public void disconnect() {
        sendBuffer.clear();
        if (channel != null) {
            super.close(ECloseMode.FORCED);
        }
    }
//	--------------------------------- Private ----------------------------------------------------
    private ServerSocketChannel serverSocket;
    private final InetSocketAddress myAddress;

    @Override
    public void onEvent(Event event) {
        if (event.getClass() == EventDispatcher.AcceptEvent.class) {
            try {
                final SocketChannel acceptedSocket = serverSocket.accept();
                if (channel != null) {
                    acceptedSocket.close();
                    tracer.put(EEventSeverity.WARNING, "Server port connection is busy", null, null, false);
                } else {
                    reset();
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
                    channel = acceptedSocket;
                    updateDescription();

                    if (!dispatcher.notify(new ConnectEvent(this))) {
                        super.close();
                        tracer.put(EEventSeverity.WARNING, "Unexpected connection on server port", null, null, false);
                    } else {
                        startRead();
                    }
                }
            } catch (IOException e) {
                tracer.put(EEventSeverity.ERROR, "Socket accept error: " + ExceptionTextFormatter.exceptionStackToString(e), null, null, false);
            }
            if (serverSocket != null) {
                dispatcher.waitEvent(new EventDispatcher.AcceptEvent(serverSocket), this, -1);
            }
        } else {
            super.onEvent(event);
        }
    }

    public ServerSocketChannel getServerSocket() {
        return serverSocket;
    }
}