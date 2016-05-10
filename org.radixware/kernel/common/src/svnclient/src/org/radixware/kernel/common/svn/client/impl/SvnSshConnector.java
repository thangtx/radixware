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

import com.trilead.ssh2.StreamGobbler;
import com.trilead.ssh2.crypto.PEMDecoder;
import org.radixware.kernel.common.svn.client.SvnConnector;

import java.io.*;
import org.radixware.kernel.common.svn.RadixSvnException;

import org.radixware.kernel.common.svn.client.SvnCredentials;
import org.radixware.kernel.common.svn.client.SvnRepository;
import org.radixware.kernel.common.svn.client.impl.ssh.SshSession;
import org.radixware.kernel.common.svn.client.impl.ssh.SshSocketPool;

/**
 * @version 1.3
 * @author TMate Software Ltd.
 */
public class SvnSshConnector extends SvnConnector {

    private static final SshSocketPool openedSessions = new SshSocketPool();

    private SshSession sshSession;
    private InputStream input;
    private OutputStream output;
    private boolean wasClosed = false;

    public SvnSshConnector() {

    }

    @Override
    public void open(SvnRepository repository) throws RadixSvnException {
        String realm = repository.getLocation().getScheme() + "://" + repository.getLocation().getHost();
        if (repository.getLocation().getPort() >= 0) {
            realm += ":" + repository.getLocation().getPort();
        }
        if (repository.getLocation().getUserInfo() != null && !"".equals(repository.getLocation().getUserInfo())) {
            realm = repository.getLocation().getUserInfo() + "@" + realm;
        }

        SshSession connection = null;

        String userName;
        SvnCredentials credentials = repository.getCredentials();
        String host = repository.getLocation().getHost();
        int port = repository.getLocation().getPort();
        if (port < 0) {
            port = 22;
        }
        userName = credentials.getUserName();
        char[] privateKey = credentials.getPrivateKey();

        char[] passphrase = credentials.getPassPhrase();
        if (passphrase != null && passphrase.length == 0) {
            passphrase = null;
        }
        char[] password = credentials.getPassword(repository.getLocation().toString());
        if (password != null && password.length == 0) {
            password = null;
        }

        if (privateKey != null && !checkPrivateKey(privateKey, passphrase)) {
            if (password == null) {
                throw new RadixSvnException("Invalid credentials");
            }
            privateKey = null;
        }

        connection = openedSessions.openSession(host, port, credentials);

        if (connection == null) {
            throw new RadixSvnException("Unable to connect to " + repository.getLocation().toString());
        }

        try {
            sshSession = connection;
            sshSession.execCommand("svnserve -t");
            output = new BufferedOutputStream(sshSession.getIn(), 16 * 1024);
            input = new BufferedInputStream(sshSession.getOut(), 16 * 1024);
            new StreamGobbler(sshSession.getErr());
            return;
        } catch (IOException e) {
            close();
            throw new RadixSvnException("Unable to connect to " + repository.getLocation().toString(), e);

        }
    }

    public static boolean checkPrivateKey(char[] privateKey, String passphrase) {
        return checkPrivateKey(privateKey, passphrase != null ? passphrase.toCharArray() : null);
    }

    public static boolean checkPrivateKey(char[] privateKey, char[] passphrase) {
        try {
            PEMDecoder.decode(privateKey, passphrase != null ? new String(passphrase) : null);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public void close() {
        wasClosed = true;
        if (input != null) {
            try {
                input.close();
            } catch (IOException ex) {
            }
            input = null;
        }
        if (output != null) {
            try {
                output.close();
            } catch (IOException ex) {
            }
            output = null;
        }
        if (sshSession != null) {
            sshSession.close();
            sshSession = null;
        }
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return input;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return output;
    }

    public boolean isConnected() throws RadixSvnException {
        return sshSession != null && !isStale();
    }

    @Override
    public boolean isStale() {
        if (sshSession == null) {
            return true;
        }
        try {
            sshSession.ping();
        } catch (IOException e) {
            return true;
        }
        return false;
    }

    public static void shutdown() {
        openedSessions.shutdown();
    }
}
