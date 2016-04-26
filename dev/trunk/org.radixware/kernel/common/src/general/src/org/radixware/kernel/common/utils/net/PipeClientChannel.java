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
import java.nio.channels.SelectableChannel;
import org.radixware.kernel.common.utils.io.pipe.BidirectionalPipe;
import org.radixware.kernel.common.utils.io.pipe.InMemoryBytePipe;


public class PipeClientChannel implements ClientChannel {

    private volatile BidirectionalPipe pipe;

    @Override
    public boolean connect(SapAddress sapAddress, int timeoutMillis) throws IOException {
        if (isOpened()) {
            throw new IllegalStateException("Channel is already connected");
        }
        pipe = new BidirectionalPipe(new InMemoryBytePipe(0x10000, -1, -1), new InMemoryBytePipe(0x10000, -1, -1));
        if (timeoutMillis == 0) {
            pipe.configureBlocking(false);
        } else {
            pipe.configureBlocking(true);
        }
        return pipe.connect(sapAddress.getPipeAddress(), timeoutMillis);
    }

    @Override
    public void finishConnect() throws IOException {
        pipe.finishConnect();
    }

    @Override
    public boolean isOpened() {
        return (pipe != null && pipe.isConnected());
    }

    @Override
    public SelectableChannel getSelectableChannel() {
        return pipe;
    }

    @Override
    public void close() throws IOException {
        if (pipe != null) {
            pipe.close();
            pipe = null;
        }
    }

    @Override
    public String toString() {
        return pipe == null ? "<null>" : pipe.getCreatorThreadName() + "@" + System.identityHashCode(pipe);
    }
}
