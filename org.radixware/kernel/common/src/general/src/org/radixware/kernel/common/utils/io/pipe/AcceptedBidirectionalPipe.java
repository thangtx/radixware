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
package org.radixware.kernel.common.utils.io.pipe;

import java.io.IOException;
import java.nio.channels.SelectionKey;

public class AcceptedBidirectionalPipe extends BidirectionalPipe {

    private volatile boolean connected = true;//connected after construction
    private final PipeAddress address;
    private final String clientPipeDesc;
    private final String clientPipeCreatorThreadName;

    public AcceptedBidirectionalPipe(final BidirectionalPipe clientPipe) {
        super(clientPipe.getOutputPipe(), clientPipe.getInputPipe());
        this.address = clientPipe.getAddress();
        this.clientPipeDesc = clientPipe.toString();
        this.clientPipeCreatorThreadName = clientPipe.getCreatorThreadName();
    }

    @Override
    public PipeAddress getAddress() {
        return address;
    }

    @Override
    public void finishConnect() {
        //do nothing
    }

    @Override
    protected void implCloseSelectableChannel() throws IOException {
        super.implCloseSelectableChannel();
        connected = false;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public int readyOps() {
        int ret = 0;
        if (isConnected() && getInput().isDataAvailable()) {
            ret |= SelectionKey.OP_READ;
        }
        if (isConnected() && getOutput().isReadyForWrite()) {
            ret |= SelectionKey.OP_WRITE;
        }
        return ret;
    }

    @Override
    public int validOps() {
        return SelectionKey.OP_READ | SelectionKey.OP_WRITE;
    }

    @Override
    public String toString() {
        return "srv_" + clientPipeDesc;
    }

    @Override
    public String getCreatorThreadName() {
        return clientPipeCreatorThreadName;
    }

}
