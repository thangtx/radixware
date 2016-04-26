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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.SelectableChannel;


public class SocketRequestChannel implements RequestChannel {

    private final Socket socket;
    private final String remoteAddress;
    private final String localAddress;

    public SocketRequestChannel(final Socket socket) {
        this.socket = socket;
        remoteAddress = socket != null && socket.isConnected() ? socket.getRemoteSocketAddress().toString() : "<null>";
        localAddress = socket != null && socket.isConnected() ? socket.getLocalSocketAddress().toString() : "<null>";
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return socket.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return socket.getOutputStream();
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void setReadTimeout(final int timeoutMillis) throws SocketException {
        socket.setSoTimeout(timeoutMillis);
    }

    @Override
    public String getRemoteAddress() {
        return remoteAddress;
    }

    @Override
    public String getRemoteAddressUnchangingPart() {
        return socket != null && socket.isConnected() ? ((InetSocketAddress) socket.getRemoteSocketAddress()).getHostString() : "<null>";
    }

    @Override
    public String getLocalAddress() {
        return localAddress;
    }

    @Override
    public SelectableChannel getSelectableChannel() {
        return socket.getChannel();
    }
}
