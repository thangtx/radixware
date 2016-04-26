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
package org.radixware.kernel.common.svn.client.impl.ssh;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.InteractiveCallback;
import com.trilead.ssh2.ServerHostKeyVerifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.client.SvnCredentials;

public class SshSocket {

    private static final int LIFECICLE_LENGTH_MILLIS = 600 * 1000;
    private static final int SESSION_POOL_SIZE = 10;

    private final String sshHostName;
    private final int sshPort;

    private boolean myIsDisposed;

    private final List<SshConnection> connectionPool = new LinkedList<>();

    private final SvnCredentials credentials;
    private final String id;

    public SshSocket(String host, int port, SvnCredentials credentials) {
        this.sshHostName = host;
        this.credentials = credentials;
        this.id = credentials.computeId(host, port);
        this.sshPort = port;
    }

    public boolean reset() {
        synchronized (this) {
            boolean wasEmpty = connectionPool.isEmpty();
            long time = System.currentTimeMillis();
            for (SshConnection connection : new ArrayList<>(connectionPool)) {
                if (connection.isEmpty()) {
                    if (connectionPool.size() == 1) {
                        long timeout = time - connection.getLastAccessTime();
                        if (timeout >= LIFECICLE_LENGTH_MILLIS) {
                            connection.close();
                            connectionPool.remove(connection);
                        }
                    } else {
                        connection.close();
                        connectionPool.remove(connection);
                    }
                }
            }
            if (connectionPool.isEmpty() && !wasEmpty) {
                dispose();
                return true;
            }
            return false;
        }
    }

    public boolean isDisposed() {
        return myIsDisposed;
    }

    public void dispose() {
        synchronized (this) {
            myIsDisposed = true;

            for (SshConnection connection : connectionPool) {
                connection.close();
            }
            connectionPool.clear();
        }
    }

    public String getId() {
        return id;
    }

    public SshSession openSession() throws RadixSvnException {
        try {
            SshSession session = useExistingConnection();
            if (session != null) {
                return session;
            }
            SshConnection newConnection = null;

            session = useExistingConnection();
            if (session != null) {
                return session;
            }
            newConnection = openConnection();

            if (newConnection != null) {
                synchronized (this) {
                    if (isDisposed()) {
                        newConnection.close();
                        throw new SshHostDisposedException();
                    }
                    connectionPool.add(newConnection);
                    return newConnection.openSession();
                }
            }
            throw new RadixSvnException("Cannot establish SSH connection with " + sshHostName + ":" + sshPort);
        } catch (IOException ex) {
            throw new RadixSvnException("Cannot establish SSH connection with " + sshHostName + ":" + sshPort, ex);
        }
    }

    private SshSession useExistingConnection() throws IOException {
        synchronized (this) {
            if (isDisposed()) {
                throw new SshHostDisposedException();
            }
            for (Iterator<SshConnection> connections = connectionPool.iterator(); connections.hasNext();) {
                final SshConnection connection = connections.next();

                if (connection.getSessionsCount() < SESSION_POOL_SIZE) {
                    try {
                        return connection.openSession();
                    } catch (IOException e) {
                        // this connection has been closed by server.
                        if (e.getMessage() != null && e.getMessage().contains("connection is closed")) {
                            connection.close();
                            connections.remove();
                        } else {
                            throw e;
                        }
                    }
                }
            }
        }
        return null;
    }

    private SshConnection openConnection() throws RadixSvnException, IOException {
        Connection connection = new Connection(sshHostName, sshPort);
        final int timeout = 5 * 60 * 1000;
        final int connectTimeout = 30 * 1000;
        connection.connect(new ServerHostKeyVerifier() {
            @Override
            public boolean verifyServerHostKey(String hostname, int port, String serverHostKeyAlgorithm, byte[] serverHostKey) throws Exception {
                return true;
            }
        }, connectTimeout, timeout, timeout);

        boolean authenticated = false;

        final String password = credentials.getPassword(sshHostName) == null ? null : String.valueOf(credentials.getPassword(sshHostName));
        final String passphrase = credentials.getPassPhrase() == null ? null : String.valueOf(credentials.getPassPhrase());

        if (!authenticated && credentials.getPrivateKey() != null) {
            authenticated = connection.authenticateWithPublicKey(credentials.getUserName(), credentials.getPrivateKey(), passphrase);
        }
        if (!authenticated && credentials.getPassword(sshHostName) != null) {
            String[] methods = connection.getRemainingAuthMethods(credentials.getUserName());
            for (int i = 0; !authenticated && i < methods.length; i++) {
                if (null != methods[i]) {
                    switch (methods[i]) {
                        case "password":
                            authenticated = connection.authenticateWithPassword(credentials.getUserName(), password);
                            break;
                        case "keyboard-interactive":
                            authenticated = connection.authenticateWithKeyboardInteractive(credentials.getUserName(), new InteractiveCallback() {
                                @Override
                                public String[] replyToChallenge(String name, String instruction, int numPrompts, String[] prompt, boolean[] echo) throws Exception {
                                    String[] reply = new String[numPrompts];
                                    for (int i = 0; i < reply.length; i++) {
                                        reply[i] = password;
                                    }
                                    return reply;
                                }
                            });
                            break;
                    }
                }
            }
        }
        if (!authenticated) {
            connection.close();
            throw new SshAuthenticationException("Credentials rejected by SSH server.");
        }
        return new SshConnection(this, connection);
    }

    @Override
    public String toString() {
        return credentials.getUserName() + "@" + sshHostName + ":" + sshPort + ":" + connectionPool.size();
    }

}
