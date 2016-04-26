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
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.radixware.kernel.common.exceptions.IllegalUsageError;


public class BidirectionalPipe extends AbstractSelectableChannel implements PipeSelectorChannel, ByteChannel {

    private static final AtomicLong SERIAL_COUNTER = new AtomicLong();
    private final CountDownLatch connectCountDownLatch = new CountDownLatch(1);
    private final BytePipe inputPipe;
    private final BytePipe outputPipe;
    private final Object writeLock = new Object();
    private final Object readLock = new Object();
    private final Set<PipeSelector> selectors = new CopyOnWriteArraySet();
    private volatile boolean connected = false;
    private final Object connectSem = new Object();
    private volatile PipeAddress addr = null;
    private final String creatorThreadName;
    protected final long serial = SERIAL_COUNTER.incrementAndGet();

    public BidirectionalPipe(final int bufferCapacity) {
        this(bufferCapacity, -1, -1);
    }

    public BidirectionalPipe(final int bufferCapacity, final long readTimeoutMillis, final long writeTimeoutMillis) {
        this(new InMemoryBytePipe(bufferCapacity, readTimeoutMillis, writeTimeoutMillis), new InMemoryBytePipe(bufferCapacity, readTimeoutMillis, writeTimeoutMillis));
    }

    public BidirectionalPipe(
            final BytePipe in,
            final BytePipe out) {
        super(PipeSelector.selectorProvider());
        this.creatorThreadName = Thread.currentThread().getName();
        this.inputPipe = in;
        this.outputPipe = out;
        inputPipe.getSink().addListener(new BytePipeSink.Listener() {
            @Override
            public void dataAvailable() {
                notifyDataAvailable();
            }
        });
        outputPipe.getSource().addListener(new BytePipeSource.Listener() {
            @Override
            public void readyToWrite() {
                notifyReadyToWrite();
            }
        });
    }

    public String getCreatorThreadName() {
        return creatorThreadName;
    }

    public BytePipe getInputPipe() {
        return inputPipe;
    }

    public BytePipe getOutputPipe() {
        return outputPipe;
    }

    protected BytePipeSink getInput() {
        return inputPipe.getSink();
    }

    protected BytePipeSource getOutput() {
        return outputPipe.getSource();
    }

    /**
     *
     * @param addr
     * @param timeoutMillis - used when mode is blocking, -1 - wait forever
     * @return
     * @throws ConnectException
     * @throws InterruptedException
     */
    public boolean connect(final PipeAddress addr, final long timeoutMillis) throws ConnectException, IOException {
        synchronized (connectSem) {
            if (this.addr != null) {
                throw new ConnectException(
                        isConnected() ? "Pipe is already connected to " : "Pipe is already connecting to " + this.addr);
            }
            this.addr = addr;
            ServerPipeRegisry.connect(addr, this);
        }
        if (isBlocking() && timeoutMillis != 0) {
            try {
                if (timeoutMillis < 0) {
                    connectCountDownLatch.await();
                } else {
                    if (!connectCountDownLatch.await(timeoutMillis, TimeUnit.MILLISECONDS)) {
                        throw new ConnectException("Connect timeout exceeded");
                    }
                }
                finishConnect();
            } catch (InterruptedException e) {
                throw new InterruptedIOException(e.getMessage());
            }
        }
        return isConnected();
    }

    void onConnect() {
        connectCountDownLatch.countDown();
        notifyOperationAvailable(SelectionKey.OP_CONNECT);
    }

    protected void notifyDataAvailable() {
        notifyOperationAvailable(SelectionKey.OP_READ);
    }

    protected void notifyReadyToWrite() {
        notifyOperationAvailable(SelectionKey.OP_WRITE);
    }

    private void notifyOperationAvailable(final int operation) {
        for (PipeSelector s : selectors) {
            final SelectionKey k = keyFor(s);
            //???: key may be cancelled before establishing connection.
            //Should we throw an exception or silently igore it?
            if (k != null) {
                if (k.isValid() && (k.interestOps() & operation) != 0) {
                    s.notify(k, operation);
                }
            }
        }
    }

    @Override
    protected void implCloseSelectableChannel() throws IOException {
        getInput().close();
        getOutput().close();
        connected = false;
    }

    @Override
    protected void implConfigureBlocking(final boolean block) throws IOException {
        //do nothing
    }

    @Override
    public int validOps() {
        return SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE;
    }

    public boolean isConnected() {
        return connected;
    }

    @Override
    public void registerSelector(final PipeSelector sel) {
        selectors.add(sel);
    }

    @Override
    public void unregisterSelector(final PipeSelector sel) {
        selectors.remove(sel);
    }

    @Override
    public int write(final ByteBuffer src) throws IOException {
        synchronized (writeLock) {
            if (isBlocking()) {
                return getOutput().write(src);
            } else {
                return getOutput().writeNow(src);
            }
        }
    }

    @Override
    public int read(final ByteBuffer dst) throws IOException {
        synchronized (readLock) {
            if (isBlocking()) {
                return getInput().read(dst);
            } else {
                return getInput().readNow(dst);
            }
        }
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
        if (connectCountDownLatch.getCount() == 0 && !isConnected()) {
            ret |= SelectionKey.OP_CONNECT;
        }
        return ret;
    }

    public void finishConnect() {
        if (connectCountDownLatch.getCount() > 0) {
            throw new IllegalStateException("Pipe is not ready to complete connection sequence yet.");
        }
        connected = true;
    }

    public void setWriteTimeout(final long timeoutMillis) {
        getOutput().setWriteTimeOut(timeoutMillis);
    }

    public void setReadTimeout(final long timeoutMillis) {
        getInput().setReadTimeOut(timeoutMillis);
    }
    private OutputStream os = null;

    public OutputStream getOutputStream() {
        if (os == null) {
            os = new OutputStream() {
                @Override
                public void write(final int b) throws IOException {
                    checkIsBlocking();
                    write(ByteBuffer.wrap(new byte[]{(byte) b}));
                }

                @Override
                public void write(final byte[] b) throws IOException {
                    checkIsBlocking();
                    write(ByteBuffer.wrap(b));
                }

                @Override
                public void write(final byte[] b, final int off, final int len) throws IOException {
                    checkIsBlocking();
                    write(ByteBuffer.wrap(b, off, len));
                }

                private void write(final ByteBuffer b) throws IOException {
                    while (b.hasRemaining()) {
                        BidirectionalPipe.this.write(b);
                    }
                }

                private void checkIsBlocking() {
                    if (!isBlocking()) {
                        throw new IllegalUsageError("Channel is not in blocking mode");
                    }
                }
            };
        }
        return os;
    }
    private InputStream is = null;

    public InputStream getInputStream() {
        if (is == null) {
            is = new InputStream() {
                @Override
                public int read() throws IOException {
                    checkIsBlocking();
                    final ByteBuffer b = ByteBuffer.allocate(1);
                    final int res = BidirectionalPipe.this.read(b);
                    if (res < 0) {
                        return res;
                    }
                    return b.get(0);
                }

                @Override
                public int read(final byte[] b) throws IOException {
                    checkIsBlocking();
                    return BidirectionalPipe.this.read(ByteBuffer.wrap(b));
                }

                @Override
                public int read(final byte[] b, final int off, final int len) throws IOException {
                    checkIsBlocking();
                    return BidirectionalPipe.this.read(ByteBuffer.wrap(b, off, len));
                }

                private void checkIsBlocking() {
                    if (!isBlocking()) {
                        throw new IllegalUsageError("Channel is not in blocking mode");
                    }
                }
            };
        }
        return is;
    }

    public PipeAddress getAddress() {
        return addr;
    }

    @Override
    public String toString() {
        return "pipe{ct=" + creatorThreadName + ",addr=" + (addr == null ? "<null>" : addr.getAddress()) + ",sn=" + serial + "}";
    }
}