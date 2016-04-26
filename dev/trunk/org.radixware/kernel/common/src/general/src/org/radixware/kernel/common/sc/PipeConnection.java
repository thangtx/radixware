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
import org.radixware.kernel.common.utils.io.pipe.BidirectionalPipe;
import org.radixware.kernel.common.utils.io.pipe.InMemoryBytePipe;


class PipeConnection implements SyncClientConnection {

    private BidirectionalPipe pipe;
    private final ServiceClient.Port port;
    private final SapClientOptions sap;

    public PipeConnection(final ServiceClient.Port port, final SapClientOptions sapOptions) {
        this.port = port;
        this.sap = sapOptions;
    }

    @Override
    public void connect() throws IOException {
        pipe = new BidirectionalPipe(new InMemoryBytePipe(1 << 15), new InMemoryBytePipe(1 << 15));
        pipe.configureBlocking(true);
        pipe.connect(sap.getAddress().getPipeAddress(), sap.getConnectTimeoutMillis());
    }

    @Override
    public void close() throws IOException {
        if (pipe != null) {
            pipe.close();
        }
        pipe = null;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        if (pipe == null) {
            return null;
        }
        return pipe.getOutputStream();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (pipe == null) {
            return null;
        }
        return pipe.getInputStream();
    }

    @Override
    public SelectableChannel getSelectableChannel() {
        return pipe;
    }

    @Override
    public void setReadTimeOut(int timeoutMillis) throws IOException {
        if (pipe != null) {
            pipe.setReadTimeout(timeoutMillis);
        }
    }

    @Override
    public String toString() {
        return pipe != null ? pipe.toString() : "Non-connected pipe connection";
    }

    @Override
    public SapClientOptions getSapOptions() {
        return sap;
    }
}
