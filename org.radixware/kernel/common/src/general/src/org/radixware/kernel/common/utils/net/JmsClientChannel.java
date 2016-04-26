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
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.util.concurrent.ArrayBlockingQueue;
import org.apache.commons.logging.LogFactory;
import org.radixware.kernel.common.sc.SapClientOptions;
import org.radixware.kernel.common.utils.io.pipe.BidirectionalPipe;

/**
 * Adapter for {@link JmsSapConnection} to be used as {@link ClientChannel} in
 * asynchronous service client (only in Server environment)
 *
 */
public class JmsClientChannel implements ClientChannel {

    private JmsSapConnection jmsSapConnection;
    //to emulate selectable channel behaviour
    private volatile BidirectionalPipe fakePipe;
    private ArrayBlockingQueue<DataContainer> pendingData = new ArrayBlockingQueue<>(1);

    @Override
    public boolean connect(SapAddress sapAddress, int timeoutMillis) throws IOException {
        jmsSapConnection = new JmsSapConnection(new JmsSapConnection.AsyncResponceHandler() {
            @Override
            public void onResponse(byte[] httpSoapData) {
                onData(new DataContainer(httpSoapData));
            }

            private void onData(final DataContainer data) {
                try {
                    pendingData.put(data);
                    try {
                        fakePipe.getInputPipe().getSource().write(ByteBuffer.allocate(1));
                    } catch (IOException ex) {
                        LogFactory.getLog(JmsClientChannel.class).error("Error on JMS response", ex);
                    }
                } catch (InterruptedException ex) {
                    throw new IllegalStateException("Interrupted while waiting for message to be processed", ex);
                }
            }

            @Override
            public void onReceiveError(IOException exception) {
                onData(new DataContainer(exception));
            }
        }, JmsSapConnection.EStartConnectionMode.START_IMMEDIATELY);
        final SapClientOptions sapClientOptions = new SapClientOptions();
        sapClientOptions.setAddress(sapAddress);
        jmsSapConnection.connect(sapClientOptions);
        fakePipe = new BidirectionalPipe(1) {
            @Override
            public boolean isConnected() {
                return true;
            }
        };
        fakePipe.configureBlocking(false);
        return true;
    }

    public byte[] takePendingMessageData() throws IOException {
        try {
            //mark pipe as empty so no read events will be generated further
            fakePipe.read(ByteBuffer.allocate(1));
            final DataContainer dataContainer = pendingData.take();
            if (dataContainer.exception != null) {
                throw dataContainer.exception;
            }
            return dataContainer.data;
        } catch (InterruptedException ex) {
            throw new IOException(ex);
        }

    }

    public void send(final byte[] httpSoapMessageData) throws IOException {
        jmsSapConnection.getOutputStream().write(httpSoapMessageData);
        jmsSapConnection.getOutputStream().flush();
    }

    @Override
    public void finishConnect() throws IOException {
        //do nothing
    }

    @Override
    public boolean isOpened() {
        return jmsSapConnection != null && jmsSapConnection.isStarted();
    }

    @Override
    public SelectableChannel getSelectableChannel() {
        return fakePipe;
    }

    @Override
    public void close() throws IOException {
        if (jmsSapConnection != null) {
            try {
                jmsSapConnection.close();
            } finally {
                jmsSapConnection = null;
                fakePipe = null;
            }
        }
    }

    private static class DataContainer {

        public final byte[] data;
        public final IOException exception;

        public DataContainer(byte[] data) {
            this.data = data;
            this.exception = null;
        }

        public DataContainer(IOException throwable) {
            this.exception = throwable;
            this.data = null;
        }
    }

    @Override
    public String toString() {
        return jmsSapConnection == null ? "<null>" : jmsSapConnection.toString();
    }
}
