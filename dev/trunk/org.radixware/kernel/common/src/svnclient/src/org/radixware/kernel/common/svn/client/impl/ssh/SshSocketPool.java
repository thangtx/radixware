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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.radixware.kernel.common.svn.RadixSvnException;

import org.radixware.kernel.common.svn.client.SvnCredentials;

public class SshSocketPool {

    private static final long RESET_TIMEOUT = 10 * 1000;

    private final Map<String, SshSocket> openedSockets = new HashMap<>();
    private final Timer resetWorkerDriver;

    public SshSocketPool() {
        resetWorkerDriver = new Timer(true);
        resetWorkerDriver.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (openedSockets) {
                    Collection<SshSocket> sockets = new ArrayList<>(openedSockets.values());
                    for (SshSocket socket : sockets) {
                        if (socket.reset()) {
                            openedSockets.remove(socket.getId());
                        }
                    }
                }
            }
        }, RESET_TIMEOUT, RESET_TIMEOUT);

    }

    public void shutdown() {
        synchronized (openedSockets) {
            Collection<String> socketKeys = new ArrayList<>(openedSockets.keySet());
            for (String key : socketKeys) {
                SshSocket socket = openedSockets.remove(key);
                if (socket != null) {
                    socket.dispose();
                }
            }
        }
    }

    public SshSession openSession(String host, int port, SvnCredentials credentials) throws RadixSvnException {

        final SshSocket socket = new SshSocket(host, port, credentials);

        SshSession session = null;
        final String socketKey = socket.getId();
        int attemptCount = 0;
        while (session == null) {
            attemptCount++;
            SshSocket sshHost;
            synchronized (openedSockets) {
                sshHost = openedSockets.get(socketKey);
                if (sshHost == null) {
                    sshHost = socket;
                    openedSockets.put(socketKey, socket);
                }
            }

            try {
                session = sshHost.openSession();
            } catch (RadixSvnException e) {
                // host has been removed from the pool.
                synchronized (openedSockets) {
                    openedSockets.remove(socketKey);
                }
                if (attemptCount > 5) {
                    throw e;
                } else {
                    continue;
                }
            }
            break;
        }

        return session;
    }

}
