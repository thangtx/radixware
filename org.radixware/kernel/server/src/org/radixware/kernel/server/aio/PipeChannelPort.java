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

package org.radixware.kernel.server.aio;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.WritableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.io.pipe.BidirectionalPipe;


public class PipeChannelPort extends ChannelPort implements EventHandler {

    protected BidirectionalPipe pipe;

    public PipeChannelPort(final EventDispatcher dispatcher, final LocalTracer tracer, final BidirectionalPipe pipe, final String recvFrameFormat, final String sendFrameFormat) {
        super(dispatcher, tracer, recvFrameFormat, sendFrameFormat);
        this.pipe = pipe;
        setShortDescription(String.valueOf(pipe));
    }

    @Override
    public void closeUnderlyingChannel() {
        try {
            if (pipe != null) {
                pipe.close();
                pipe = null;
            }
        } catch (IOException ex) {
            //do nothing
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
        }
    }

    public BidirectionalPipe getPipe() {
        return pipe;
    }

    @Override
    public boolean isConnected() {
        return !isClosed && pipe != null && pipe.isOpen() && pipe.isConnected();
    }

    @Override
    WritableByteChannel getOutChannel() {
        return pipe;
    }

    @Override
    ReadableByteChannel getInChannel() {
        return pipe;
    }

    @Override
    SelectableChannel getSelectableOutChannel() {
        return pipe;
    }

    @Override
    public SelectableChannel getSelectableInChannel() {
        return pipe;
    }
}