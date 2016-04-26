/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.common.svn.client.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URI;
import javax.net.SocketFactory;
import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.client.SvnConnector;
import org.radixware.kernel.common.svn.client.SvnRepository;

/**
 *
 * @author akrylov
 */
public class SvnPlainConnector extends SvnConnector {

    private abstract static class Gateway {

        public abstract void connect() throws IOException;

        public abstract void disconnect() throws IOException;

        public abstract InputStream getInputStream() throws IOException;

        public abstract OutputStream getOutputStream() throws IOException;

        public abstract boolean isStale();
    }

    private static class SocketGateway extends Gateway {

        private Socket socket;
        private OutputStream output;
        private InputStream input;
        private final URI location;
        private final SvnRepository repository;

        public SocketGateway(SvnRepository repository) {
            this.location = repository.getLocation();
            this.repository = repository;
        }

        @Override
        public void connect() throws IOException {
            URI location = repository.getLocation();
            if ("https".equals(location.getScheme())) {

                int port = location.getPort();
                if (port < 0) {
                    port = 443;
                }
                SSLContext context = makeSSLContext(repository);

                SSLSocket sslSocket = (SSLSocket) context.getSocketFactory().createSocket(location.getHost(), port);

                sslSocket.addHandshakeCompletedListener(new HandshakeCompletedListener() {

                    @Override
                    public void handshakeCompleted(HandshakeCompletedEvent event) {

                    }
                });
                sslSocket.startHandshake();
                socket = sslSocket;
            } else {
                int port = location.getPort();
                if (port < 0) {
                    if ("http".equals(location.getScheme())) {
                        port = 80;
                    } else if ("svn".equals(location.getScheme())) {
                        port = 3690;
                    }
                }
                socket = SocketFactory.getDefault().createSocket(location.getHost(), port);
            }
        }

        @Override
        public void disconnect() {
            if (socket != null) {
                try {
                    socket.shutdownInput();
                } catch (IOException e) {
                    //  
                }
                try {
                    socket.shutdownOutput();
                } catch (IOException e) {
                    //  
                }
                try {
                    socket.close();
                } catch (IOException ex) {
                    //IGNORE
                } finally {
                    socket = null;
                    input = null;
                    output = null;
                }
            }
        }

        @Override
        public boolean isStale() {
            return true;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            synchronized (this) {
                if (input == null) {
                    if (socket == null) {
                        throw new IOException("Not connected");
                    }
                    input = socket.getInputStream();
                }
                return input;
            }
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            synchronized (this) {
                if (output == null) {
                    if (socket == null) {
                        throw new IOException("Not connected");
                    }
                    output = socket.getOutputStream();
                }
                return output;
            }
        }
    }

    private static class HttpGateway extends Gateway {

        private final SvnRepository repository;
        private HttpURLConnection connection;

        public HttpGateway(SvnRepository repository) {
            this.repository = repository;
        }

        @Override
        public void connect() throws IOException {
            if (connection != null) {
                return;
            }

        }

        @Override
        public void disconnect() throws IOException {
            if (connection != null) {
                connection.disconnect();
                connection = null;
            }
        }

        @Override
        public InputStream getInputStream() throws IOException {
            if (connection == null) {
                throw new IOException("Not connected");
            }
            return connection.getInputStream();
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            if (connection == null) {
                throw new IOException("Not connected");
            }
            return connection.getOutputStream();
        }

        @Override
        public boolean isStale() {
            return true;
        }

    }

    private Gateway gateway;

    @Override
    public void open(SvnRepository repository) throws RadixSvnException {
        if (gateway != null) {
            return;
        }
        if ("svn".equals(repository.getLocation().getScheme())) {
            gateway = new SocketGateway(repository);
        } else {
            gateway = new HttpGateway(repository);
        }
        try {
            gateway.connect();
        } catch (IOException ex) {
            throw new RadixSvnException(ex);
        }

    }

    private static SSLContext makeSSLContext(SvnRepository repository) throws IOException {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            final KeyManager[] kms = org.radixware.kernel.common.svn.client.impl.KeyManager.getKeyManagers(repository);
            sslContext.init(kms, new TrustManager[]{new org.radixware.kernel.common.svn.client.impl.TrustManager(repository.getTrustManager())}, null);
            return sslContext;
        } catch (Throwable ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public void close() {
        if (gateway != null) {
            try {
                gateway.disconnect();
            } catch (IOException ex) {
                //just ignore this one
            }
            gateway = null;
        }
    }

    @Override
    public boolean isStale() {
        return gateway == null ? false : gateway.isStale();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (gateway == null) {
            throw new IOException("Not connected");
        }
        return gateway.getInputStream();

    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        if (gateway == null) {
            throw new IOException("Not connected");
        }
        return gateway.getOutputStream();
    }

}
