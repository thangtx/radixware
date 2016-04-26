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

package org.radixware.kernel.common.utils.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SocketClientChannel implements ClientChannel {

    private volatile SocketChannel channel;
    private final InetSocketAddress localAddress;

    public SocketClientChannel(InetSocketAddress localAddress) {
        this.localAddress = localAddress;
    }

    @Override
    public boolean connect(SapAddress sapAddress, int timeoutMillis) throws IOException {
        if (isOpened()) {
            throw new IllegalStateException("Client channel is already connected");
        }
        if (timeoutMillis > 0) {
            throw new IllegalArgumentException("Socket client channel doesn't support positive timout");
        }
        channel = SocketChannel.open();
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
        if (localAddress != null) {
            if (localAddress.isUnresolved()) {
                throw new IOException("Can't resolve client socket's address: " + localAddress);
            }
            channel.socket().bind(localAddress);
        }
        if (timeoutMillis == 0) {
            channel.configureBlocking(false);
        } else {
            channel.configureBlocking(true);
        }
        return channel.connect(sapAddress.getRemoteInetAddress());
    }

    @Override
    public boolean isOpened() {
        return channel != null && channel.isOpen();
    }

    @Override
    public SelectableChannel getSelectableChannel() {
        return channel;
    }

    @Override
    public void finishConnect() throws IOException {
        channel.finishConnect();
    }

    @Override
    public void close() throws IOException {
        if (channel != null) {
            try {
                final Socket socket = channel.socket();
                if (socket != null) {
                    try {
                        socket.shutdownInput();
                    } catch (IOException ex) {
                        //do nothing
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                    }
                    try {
                        socket.shutdownOutput();
                    } catch (IOException e) {
                        //do nothing
                    }
                    socket.close();
                } else {
                    channel.close();
                }
            } finally {
                channel.close();
            }
        }
    }

    @Override
    public String toString() {
        return String.valueOf(channel);
    }
}
