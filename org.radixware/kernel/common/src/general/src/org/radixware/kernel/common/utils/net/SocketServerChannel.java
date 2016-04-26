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
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectableChannel;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SSLContext;
import org.radixware.kernel.common.enums.EClientAuthentication;
import org.radixware.kernel.common.enums.EPortSecurityProtocol;
import org.radixware.kernel.common.exceptions.CertificateUtilsException;

public class SocketServerChannel implements ServerChannel {

    public static final List<String> SUITE_ANY_STRONG = new ArrayList<>();
    public static final List<String> SUITE_ANY = new ArrayList<>();
    private volatile ServerSocket serverSocket;
    private volatile ServerSocketChannel channel;

    @Deprecated
    /**
     * Encryption should be initialized after accepting socket outside of
     * SocketServerChannel to make possible async ssl handshake
     */
    public SocketServerChannel(SecurityOptions securityOptions) {
    }

    public SocketServerChannel() {
    }

    @Override
    public boolean isOpened() {
        return serverSocket != null && !serverSocket.isClosed();
    }

    @Override
    public void open(final SapAddress sapAddress) throws IOException {
        channel = ServerSocketChannel.open();
        serverSocket = ((ServerSocketChannel) channel).socket();
        serverSocket.setReuseAddress(true);
        serverSocket.bind(new InetSocketAddress(sapAddress.getRemoteInetAddress().getAddress(), sapAddress.getRemoteInetAddress().getPort()), 128);
    }

    @Override
    public void close() throws IOException {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } finally {
                serverSocket = null;
                channel = null;
            }
        }
    }

    @Override
    public RequestChannel acceptRequest() throws IOException {
        Socket acceptedChannel = serverSocket.accept();
        try {
            acceptedChannel.setTcpNoDelay(true);
        } catch (IOException e) {
            //Microsoft bug
        }
        try {
            acceptedChannel.setKeepAlive(true);
        } catch (java.net.SocketException e) {
            //Vista bug
        }
        return new SocketRequestChannel(acceptedChannel);
    }

    @Override
    public SelectableChannel getSelectableChannel() {
        return channel;
    }

    public static interface SecurityOptions {

        public EPortSecurityProtocol getSecurityProtocol();

        public SSLContext getSSLContext() throws CertificateUtilsException;

        public EClientAuthentication getClientAuthMode();

        public List<String> getCipherSuites();
    }
}
