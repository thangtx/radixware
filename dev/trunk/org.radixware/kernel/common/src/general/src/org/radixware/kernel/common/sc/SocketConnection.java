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
package org.radixware.kernel.common.sc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import org.radixware.kernel.common.enums.EPortSecurityProtocol;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.ssl.KeystoreController;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.common.utils.net.SapAddress;
import org.radixware.kernel.common.utils.net.SslUtils;

class SocketConnection implements SyncClientConnection {

    private static final boolean REUSE_ADDRESS_ON_SERVICE_CLIENT_SOCKETS = SystemPropUtils.getBooleanSystemProp("rdx.service.client.reuse.client.socket", false);

    private Socket socket;
    final ServiceClient.Port port;
    private final SapClientOptions sap;

    public SocketConnection(final ServiceClient.Port port, final SapClientOptions sapOptions) {
        this.sap = sapOptions;
        this.port = port;
    }

    @Override
    public void connect() throws IOException {
        InetSocketAddress localInetSocketAddress = sap.getAddress().getLocalInetAddress();
        if (localInetSocketAddress == null) {
            localInetSocketAddress = port.self.myAddress;
        }
        socket = newSocket(localInetSocketAddress);
        //added by yremizov check if InetSocketAddress resolved
        if (sap.getAddress().getKind() == SapAddress.EKind.INET_SOCKET_ADDRESS && sap.getAddress().getRemoteInetAddress().isUnresolved()) {
            final String hostname = sap.getAddress().getRemoteInetAddress().getHostName();
            final InetAddress addr = InetAddress.getByName(hostname);
            sap.setAddress(new SapAddress(new InetSocketAddress(addr, sap.getAddress().getRemoteInetAddress().getPort()), sap.getAddress().getLocalInetAddress()));
        }
        socket.connect(new InetSocketAddress(sap.getAddress().getRemoteInetAddress().getAddress(), sap.getAddress().getRemoteInetAddress().getPort()), sap.getConnectTimeoutMillis());
        if (sap.getSecurityProtocol().isTls()) {
            if (!port.self.isSslPossible()) {
                throw new IOException("Unable to use ssl connection");
            }
            SSLContext sslContext;
            try {
                sslContext = port.self.prepareSslContext(sap);
            } catch (Exception e) {
                final String exceptionMessage;
                if (KeystoreController.isIncorrectPasswordException(e)) {
                    exceptionMessage = "Invalid KeyStore password";
                } else {
                    exceptionMessage = ExceptionTextFormatter.throwableToString(e);
                }
                throw new IOException("Cannot create SSLContext: " + exceptionMessage);
            }
            socket = sslContext.getSocketFactory().createSocket(socket, socket.getInetAddress().getHostAddress(), socket.getPort(), true);
            SslUtils.ensureTlsVersion(sap.getSecurityProtocol(), socket, true);
        }
    }

    private Socket newSocket(final InetSocketAddress localAddress) throws UnableToBindToLocalAddressException {
        SocketChannel channel = null;
        try {
            channel = SocketChannel.open();
            channel.configureBlocking(true);
            final Socket newSocket = channel.socket();
            try {
                newSocket.setTcpNoDelay(true);
            } catch (IOException ex) {
                //Microsoft bug
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            try {
                newSocket.setKeepAlive(true);
            } catch (IOException e) {
                //Vista bug
            }
            if (REUSE_ADDRESS_ON_SERVICE_CLIENT_SOCKETS) {
                newSocket.setReuseAddress(true);
            }
        } catch (IOException e) {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException ex) {
                    //do nothing
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
            throw new IllegalUsageError("Can't setup client socket", e);
        }
        if (localAddress != null) {
            try {
                if (localAddress.isUnresolved()) {
                    throw new IOException("Can't resolve client socket's address: " + localAddress);
                }
                channel.socket().bind(new InetSocketAddress(localAddress.getAddress(), 0));
            } catch (IOException e) {
                try {
                    channel.close();
                } catch (IOException ex) {
                    //do nothing
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
                throw new UnableToBindToLocalAddressException("Can't bind client socket address " + localAddress, e);
            }
        }
        return channel.socket();
    }

    @Override
    public void close() throws IOException {
        if (socket instanceof SSLSocket) {
            socket.getChannel().configureBlocking(true);
        }
        if (socket != null) {
            socket.close();
            socket = null;
        }
    }

    @Override
    public SelectableChannel getSelectableChannel() {
        if (socket == null) {
            return null;
        }
        return socket.getChannel();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        if (socket == null) {
            return null;
        }
        return socket.getOutputStream();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (socket == null) {
            return null;
        }
        return socket.getInputStream();
    }

    @Override
    public void setReadTimeOut(int timeoutMillis) throws IOException {
        if (socket != null) {
            socket.setSoTimeout(Math.max(0, timeoutMillis));
        }
    }

    @Override
    public String toString() {
        return socket != null ? socket.toString() : "Non-connected socket connection";
    }

    @Override
    public SapClientOptions getSapOptions() {
        return sap;
    }
}
