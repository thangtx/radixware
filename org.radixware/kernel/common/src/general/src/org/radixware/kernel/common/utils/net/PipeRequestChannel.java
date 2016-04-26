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
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.SelectableChannel;
import org.radixware.kernel.common.utils.io.pipe.BidirectionalPipe;


public class PipeRequestChannel implements RequestChannel {

    private final BidirectionalPipe pipe;

    public PipeRequestChannel(final BidirectionalPipe pipe) {
        this.pipe = pipe;
    }

    @Override
    public void close() throws IOException {
        pipe.close();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return pipe.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return pipe.getOutputStream();
    }

    public BidirectionalPipe getPipe() {
        return pipe;
    }

    @Override
    public void setReadTimeout(int timeoutMillis) throws IOException {
        pipe.setReadTimeout(timeoutMillis);
    }

    @Override
    public String getRemoteAddress() {
        if (pipe != null) {
            return "pipe" + "@" + System.identityHashCode(pipe);
        }
        return "<none>"; 
    }

    @Override
    public String getRemoteAddressUnchangingPart() {
        if (pipe != null) {
            return pipe.getCreatorThreadName();
        }
        return "<none>"; 
    }

    @Override
    public String getLocalAddress() {
        if (pipe != null && pipe.getAddress()!= null) {
            return pipe.getAddress().toString();
        }
        return "<none>";
    }

    @Override
    public SelectableChannel getSelectableChannel() {
        return pipe;
    }
}
