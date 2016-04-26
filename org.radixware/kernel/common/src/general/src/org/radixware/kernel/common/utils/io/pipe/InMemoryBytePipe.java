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

import org.radixware.kernel.common.utils.net.EBufferState;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeoutException;

/**
 * Pipe for transferring bytes between two threads. Current implementation
 * assumes that each end of the pipe will be used only by one thread, i.e. there
 * would be only one writing thread and one reading thread.
 *
 */
public class InMemoryBytePipe implements BytePipe {

    //guarded by bufferLock
    private ByteBuffer buffer;
    //guarded by bufferLock
    private EBufferState bufferState = EBufferState.FOR_WRITE;
    //guarded by bufferLock
    private boolean wasAllocated = false;
    private final Object bufferLock = new Object();
    private volatile long readTimeOutMillis;
    private volatile long writeTimeOutMillis;
    private final BytePipeSource input;
    private final BytePipeSink output;
    private volatile boolean sinkClosed = false;
    private volatile boolean sourceClosed = false;
    private final List<BytePipeSink.Listener> sinkListeners = new CopyOnWriteArrayList<>();
    private final List<BytePipeSource.Listener> sourceListeners = new CopyOnWriteArrayList<>();
    private volatile int availableBytesCount = 0;
    private final int capacity;

    public InMemoryBytePipe(final int capacity) {
        this(capacity, -1, -1);
    }

    public InMemoryBytePipe(final int capacity, final long readTimeOutMillis, final long writeTimeOutMillis) {
        this.readTimeOutMillis = readTimeOutMillis;
        this.writeTimeOutMillis = writeTimeOutMillis;
        this.capacity = capacity;
        input = new SourceEnd();
        output = new SinkEnd();
    }

    /**
     * Should be called only under synchronization on bufferLock
     */
    private void assertAllocated() {
        if (wasAllocated || (sinkClosed && sourceClosed)) {
            return;
        }
        if (buffer != null) {
            throw new IllegalStateException("Buffer is already allocated, however allocated flag was not set");
        }
        buffer = ByteBuffer.allocate(capacity);
        wasAllocated = true;
    }

    @Override
    public BytePipeSource getSource() {
        return input;
    }

    @Override
    public BytePipeSink getSink() {
        return output;
    }

    private class SourceEnd implements BytePipeSource {

        @Override
        public int write(final ByteBuffer data) throws IOException {
            return write(data, -1);
        }

        @Override
        public int write(final ByteBuffer data, final long timeOutMillis) throws IOException {
            if (sourceClosed) {
                throw new ClosedChannelException();
            }
            synchronized (bufferLock) {
                assertAllocated();
                ensureState(EBufferState.FOR_WRITE);
                if (sinkClosed) {
                    throw new IOException("Sink is already closed");
                }
                if (!buffer.hasRemaining() && timeOutMillis != 0) {
                    try {
                        awaitAvailable(bufferState, timeOutMillis);
                    } catch (InterruptedException ex) {
                        throw new IOException(ex);
                    }
                }
                final int transferedBytesCount = Math.min(buffer.remaining(), data.remaining());
                if (transferedBytesCount > 0) {
                    final int oldDataLimit = data.limit();
                    data.limit(data.position() + transferedBytesCount);
                    buffer.put(data);
                    data.limit(oldDataLimit);
                    availableBytesCount = buffer.position();
                    notifyDataAvailable();
                }
                bufferLock.notifyAll();
                return transferedBytesCount;
            }
        }

        @Override
        public int writeNow(final ByteBuffer data) throws IOException {
            return write(data, 0);
        }

        @Override
        public void setWriteTimeOut(final long timeOutMillis) {
            writeTimeOutMillis = timeOutMillis;
        }

        private void notifyDataAvailable() {
            for (BytePipeSink.Listener listener : sinkListeners) {
                listener.dataAvailable();
            }
        }

        @Override
        public void close() throws IOException {
            if (!sourceClosed) {
                sourceClosed = true;
                pipeEndClosed();
                notifyDataAvailable();
            }
        }

        @Override
        public void addListener(final Listener listener) {
            sourceListeners.add(listener);
        }

        @Override
        public void removeListener(final Listener listener) {
            sourceListeners.remove(listener);
        }

        @Override
        public boolean isReadyForWrite() {
            return availableBytesCount < capacity;
        }
    }

    private class SinkEnd implements BytePipeSink {

        @Override
        public int read(final ByteBuffer destination) throws IOException {
            return read(destination, -1);
        }

        @Override
        public int read(final ByteBuffer destination, final long timeOutMillis) throws IOException {
            if (sinkClosed) {
                throw new ClosedChannelException();
            }
            synchronized (bufferLock) {
                assertAllocated();
                ensureState(EBufferState.FOR_READ);
                if (sourceClosed && !buffer.hasRemaining()) {
                    return -1;
                }
                if (!buffer.hasRemaining() && timeOutMillis != 0) {
                    try {
                        awaitAvailable(bufferState, timeOutMillis);
                    } catch (InterruptedException ex) {
                        throw new IOException(ex);
                    }
                }
                if (sourceClosed && !buffer.hasRemaining()) {
                    return -1;
                }
                int bytesRead = Math.min(buffer.remaining(), destination.remaining());
                if (bytesRead > 0) {
                    final int oldBufferLimit = buffer.limit();
                    buffer.limit(buffer.position() + bytesRead);
                    destination.put(buffer);
                    buffer.limit(oldBufferLimit);
                    buffer.compact();
                    buffer.flip();
                    availableBytesCount = buffer.limit();
                    notifyReadyToWrite();
                }
                bufferLock.notifyAll();
                return bytesRead;
            }
        }

        @Override
        public int readNow(final ByteBuffer destination) throws IOException {
            return read(destination, 0);
        }

        @Override
        public void setReadTimeOut(final long timeOutMillis) {
            readTimeOutMillis = timeOutMillis;
        }

        @Override
        public void addListener(final Listener listener) {
            sinkListeners.add(listener);
        }

        @Override
        public void removeListener(final Listener listener) {
            sinkListeners.remove(listener);
        }

        @Override
        public void close() throws IOException {
            if (!sinkClosed) {
                sinkClosed = true;
                pipeEndClosed();
            }
        }

        private void notifyReadyToWrite() {
            for (BytePipeSource.Listener listener : sourceListeners) {
                listener.readyToWrite();
            }
        }

        @Override
        public boolean isDataAvailable() {
            if (sourceClosed) {
                return true;//read -1 on disconnect
            }
            return availableBytesCount > 0;
        }
    }

    /**
     * waits until the buffer will have remaining bytes in the given state, i.e.
     * will be ready to perform corresponding operation, at most timeOutMillis.
     * Should be called under synchronization on bufferLock
     */
    private void awaitAvailable(final EBufferState state, final long timeOutMillis) throws InterruptedException, IOException {
        if (timeOutMillis == 0) {
            return;
        }
        final Timeout timeout;
        final boolean throwExceptionOnTimeOut;
        if (timeOutMillis == -1) {
            timeout = new Timeout(getDefaultTimeOut(state));
            throwExceptionOnTimeOut = true;
        } else {
            timeout = new Timeout(timeOutMillis);
            throwExceptionOnTimeOut = false;
        }
        ensureState(state);
        while (!buffer.hasRemaining() && !timeout.isExpired()) {
            final long millisLeft = timeout.millisLeft();
            if (millisLeft != 0) {
                if (millisLeft < 0) {
                    bufferLock.wait();//synchronization is done by the caller method
                } else {
                    bufferLock.wait(millisLeft);//synchronization is done by the caller method    
                }
                ensureState(state);//could be changed during wait()
                if (state == EBufferState.FOR_READ) {
                    if (sinkClosed) {
                        throw new ClosedChannelException();
                    }
                    if (sourceClosed) {
                        return;
                    }
                } else {
                    if (sourceClosed) {
                        throw new ClosedChannelException();
                    }
                    if (sinkClosed) {
                        throw new IOException("Sink is already closed");
                    }
                }
            }
        }
        if (!buffer.hasRemaining() && throwExceptionOnTimeOut) {
            throw new SocketTimeoutException("Timeout on read from pipe");
        }
    }

    private long getDefaultTimeOut(final EBufferState state) {
        switch (state) {
            case FOR_READ:
                return readTimeOutMillis;
            case FOR_WRITE:
                return writeTimeOutMillis;
            default:
                throw new IllegalArgumentException("Unknown state: " + state);
        }
    }

    /**
     * Sets buffer's position and limit to appropriate values. Should be called
     * under synchronization on bufferLock. Also performs check for closed
     * buffer.
     */
    private void ensureState(final EBufferState state) throws IOException {
        if (buffer == null) {//both ends of pipe are closed
            return;
        }
        if (bufferState == EBufferState.FOR_READ && state == EBufferState.FOR_WRITE) {
            buffer.position(buffer.limit());
            buffer.limit(buffer.capacity());
        } else if (bufferState == EBufferState.FOR_WRITE && state == EBufferState.FOR_READ) {
            buffer.flip();
        }
        bufferState = state;
    }

    private void pipeEndClosed() throws IOException {
        synchronized (bufferLock) {
            if (sinkClosed && sourceClosed) {
                if (buffer != null) {
                    buffer = null;//do not hold memory
                }
            }
            //notify waiting threads
            bufferLock.notifyAll();
        }
    }

    @Override
    public String toString() {
        return "InMemoryBytePipe@" + Integer.toHexString(hashCode());
    }
}
