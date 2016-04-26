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

import java.io.IOException;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SshConnection {

    private final Connection wrappedSshConnection;
    private final SshSocket sshSocket;
    private final List<SshSession> openedSessions = new LinkedList<>();
    private long lastAccessTime;

    public SshConnection(SshSocket socket, Connection connection) {
        this.sshSocket = socket;
        this.wrappedSshConnection = connection;
    }

    public SshSession openSession() throws IOException {
        synchronized (openedSessions) {
            lastAccessTime = System.currentTimeMillis();
            Session session = wrappedSshConnection.openSession();
            if (session != null) {
                SshSession sshSession = new SshSession(this, session);
                openedSessions.add(sshSession);
                return sshSession;
            }
            return null;
        }
    }

    public void sessionClosed(SshSession sshSession) {
        synchronized (openedSessions) {
            openedSessions.remove(sshSession);
        }
    }

    public boolean isEmpty() {
        synchronized (openedSessions) {
            return openedSessions.isEmpty();
        }
    }

    public int getSessionsCount() {
        synchronized (openedSessions) {
            return openedSessions.size();
        }
    }

    public void close() {
        synchronized (openedSessions) {
            for (SshSession session : new ArrayList<>(openedSessions)) {
                session.close();
            }
        }
    }

    long getLastAccessTime() {
        synchronized (openedSessions) {
            return lastAccessTime;
        }
    }
}
