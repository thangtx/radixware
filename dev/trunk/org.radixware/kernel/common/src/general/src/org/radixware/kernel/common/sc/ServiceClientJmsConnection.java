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

package org.radixware.kernel.common.sc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.SelectableChannel;
import org.radixware.kernel.common.sc.ServiceClient.Port;
import org.radixware.kernel.common.utils.net.JmsSapConnection;


class ServiceClientJmsConnection implements SyncClientConnection {

    private final Port port;
    private JmsSapConnection jmsSapConnection;
    private SapClientOptions sap;

    public ServiceClientJmsConnection(final Port port, final SapClientOptions sapOptions) {
        this.port = port;
        this.sap = sapOptions;
    }

    @Override
    public void connect() throws IOException {
        jmsSapConnection = new JmsSapConnection();
        jmsSapConnection.connect(sap);
    }

    @Override
    public void close() throws IOException {
        if (jmsSapConnection != null) {
            jmsSapConnection.close();
        }
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        if (jmsSapConnection == null) {
            return null;
        }
        return jmsSapConnection.getOutputStream();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (jmsSapConnection == null) {
            return null;
        }
        return jmsSapConnection.getInputStream();
    }

    @Override
    public void setReadTimeOut(int timeoutMillis) throws IOException {
        if (jmsSapConnection != null) {
            jmsSapConnection.setReadTimeOut(timeoutMillis);
        }
    }

    @Override
    public String toString() {
        return jmsSapConnection != null ? jmsSapConnection.toString() : "Non-connected JMS Connection";
    }

    @Override
    public SelectableChannel getSelectableChannel() {
        return null;
    }

    @Override
    public SapClientOptions getSapOptions() {
        return sap;
    }
}