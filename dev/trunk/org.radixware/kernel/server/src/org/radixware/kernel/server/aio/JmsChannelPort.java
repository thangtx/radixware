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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.InvalidDataException;
import org.radixware.kernel.common.utils.net.JmsClientChannel;

/**
 * {@link ChannelPort} to be used in conduction with {@link JmsClientChannel} to
 * provide JMS support for asynchronous server communication model. Supports
 * only HTTP frame type.
 *
 */
public class JmsChannelPort extends ChannelPort {

    private final JmsClientChannel connectionChannel;

    public JmsChannelPort(final JmsClientChannel connection, EventDispatcher dispatcher, LocalTracer tracer) {
        super(dispatcher, tracer, ChannelPort.FRAME_HTTP_RS, ChannelPort.FRAME_HTTP_RQ);
        this.connectionChannel = connection;
        setShortDescription(String.valueOf(connectionChannel));
    }

    @Override
    public void onEvent(Event event) {
        if (event.getClass() == EventDispatcher.ReadEvent.class) {
            try {
                final byte[] data = connectionChannel.takePendingMessageData();
                logDataReceived(data);
                recvBuffer.getForWrite(data.length).put(data);
                try {
                    try {
                        recvBuffer.getForRead().mark();
                        final Frame frame = getFrame();
                        logFrameRecieved(frame);
                        if (!dispatcher.notify(new ReceiveEvent(this, frame))) {
                            tracer.put(EEventSeverity.WARNING, "Unexpected data received", null, null, false);
                        }
                    } catch (InvalidDataException ex) {
                        tracer.put(EEventSeverity.WARNING, "Unexpected data received", null, null, false);
                    }
                } finally {
                    recvBuffer.clear();
                }
            } catch (IOException e) {
                tracer.put(EEventSeverity.ERROR, "Socket read error: " + ExceptionTextFormatter.throwableToString(e), null, null, false);
                close(ECloseMode.FORCED);
                return;
            }
            if (isAutoReadContinuation()) {
                startRead();
            }
        } else {
            throw new RadixError("Invalid event " + event);
        }
    }

    @Override
    protected void closeUnderlyingChannel() {
        if (connectionChannel != null) {
            try {
                connectionChannel.close();
            } catch (IOException ex) {
                //do nothing
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }
    }

    @Override
    public void send(byte[] packet, Map<String, String> frameAttrs) throws IOException {
        final Frame frame = new Frame();
        frame.packet = packet;
        frame.attrs = frameAttrs;
        putFrame(frame);
        connectionChannel.send(sendBuffer.extractBytes(sendBuffer.bytesCount()));
    }

    @Override
    WritableByteChannel getOutChannel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    ReadableByteChannel getInChannel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    SelectableChannel getSelectableOutChannel() {
        return connectionChannel.getSelectableChannel();

    }

    @Override
    SelectableChannel getSelectableInChannel() {
        return connectionChannel.getSelectableChannel();
    }

    @Override
    public boolean isConnected() {
        return connectionChannel.isOpened();
    }
}
