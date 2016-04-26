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

import java.nio.ByteBuffer;
import org.radixware.kernel.common.utils.net.AioUtils;
import org.radixware.kernel.common.utils.net.EBufferState;


public class ByteBufferWrapper {

    private ByteBuffer buffer;
    private EBufferState bufferState;

    public ByteBufferWrapper() {
        this(1024);
    }

    public ByteBufferWrapper(final int initialCapacity) {
        buffer = ByteBuffer.allocate(initialCapacity);
        bufferState = EBufferState.FOR_WRITE;
    }

    /**
     * @return ByteBuffer in state suitable for getting bytes from it
     */
    public ByteBuffer getForRead() {
        ensureState(EBufferState.FOR_READ);
        return buffer;
    }

    /**
     * @return ButeBuffer in state suitable for writing at most {@code count}
     * bytes to it
     */
    public ByteBuffer getForWrite(final int count) {
        ensureReadyForWrite(count);
        return buffer;
    }

    public int appendFrom(final ByteBufferWrapper src) {
        if (src == null) {
            return 0;
        }

        final int cnt = src.bytesCount();
        if (cnt == 0) {
            return 0;
        }
        ensureReadyForWrite(cnt);
        buffer.put(src.getForRead());
        return cnt;
    }

    /**
     * @return stored bytes count
     */
    public int bytesCount() {
        if (bufferState == EBufferState.FOR_WRITE) {
            return buffer.position();
        } else {
            return buffer.limit() - buffer.position();
        }
    }

    public boolean hasData() {
        return bytesCount() > 0;
    }

    public boolean isFull() {
        return bytesCount() == capacity();
    }

    public int capacity() {
        return buffer.capacity();
    }

    public void clear() {
        buffer.position(0);
        buffer.limit(buffer.capacity());
        bufferState = EBufferState.FOR_WRITE;
    }

    /**
     * Gets all stored bytes without removing them
     */
    public byte[] getStoredBytes() {
        return getBytesImpl(bytesCount(), false);
    }

    /**
     * Gets specified count of bytes and removes them from buffer.
     * <br>
     * If
     * {@code count<0 then count=0}, if {@code count>bytesCount() then count=bytesCount()}
     */
    public byte[] extractBytes(int count) {
        return getBytesImpl(count, true);
    }

    private byte[] getBytesImpl(int count, boolean remove) {
        if (count < 0) {
            count = 0;
        }
        if (count > bytesCount()) {
            count = bytesCount();
        }
        ensureState(EBufferState.FOR_READ);
        final byte[] result = new byte[count];
        buffer.get(result);
        if (!remove) {
            buffer.position(count);
        }
        return result;
    }

    public byte[] getJustExtractedBytes(int extracted) {
        if (bufferState == EBufferState.FOR_READ) {
            return getJustProcessedBytes(extracted);
        } else {
            return new byte[]{};
        }
    }

    public byte[] getJustAppendedBytes(int appended) {
        if (bufferState == EBufferState.FOR_WRITE) {
            return getJustProcessedBytes(appended);
        } else {
            return new byte[]{};
        }
    }

    private byte[] getJustProcessedBytes(int count) {
        if (count > buffer.position()) {
            return new byte[]{};
        }
        buffer.position(buffer.position() - count);
        final byte[] processedBytes = new byte[count];
        buffer.get(processedBytes);
        return processedBytes;
    }

    private void ensureReadyForWrite(final int count) {
        ensureState(EBufferState.FOR_WRITE);
        buffer = AioUtils.ensureBufferCapacity(buffer, count);
    }

    private void ensureState(final EBufferState newState) {
        if (bufferState == EBufferState.FOR_READ && newState == EBufferState.FOR_WRITE) {
            buffer.compact();
        } else if (bufferState == EBufferState.FOR_WRITE && newState == EBufferState.FOR_READ) {
            buffer.flip();
        }
        bufferState = newState;
    }

    @Override
    public String toString() {
        return "ByteBufferWrapper{bytesCount=" + bytesCount() + ", capacity=" + capacity() + "}";
    }
}
