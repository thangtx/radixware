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
import java.io.InputStream;
import java.io.OutputStream;

import com.trilead.ssh2.ChannelCondition;
import com.trilead.ssh2.Session;

public class SshSession {

    private SshConnection connection;
    private Session impl;

    SshSession(SshConnection owner, Session session) {
        impl = session;
        connection = owner;
    }

    public void close() {
        try {
            impl.close();
            impl.waitForCondition(ChannelCondition.CLOSED, 0);
        } finally {
            connection.sessionClosed(this);
        }
    }

    public InputStream getOut() {
        return impl.getStdout();
    }

    public InputStream getErr() {
        return impl.getStderr();
    }

    public OutputStream getIn() {
        return impl.getStdin();
    }

    public Integer getExitCode() {
        return impl.getExitStatus();
    }

    public String getExitSignal() {
        return impl.getExitSignal();
    }

    public void execCommand(String command) throws IOException {
        impl.execCommand(command);
    }

    public void ping() throws IOException {
        impl.ping();
    }
}
