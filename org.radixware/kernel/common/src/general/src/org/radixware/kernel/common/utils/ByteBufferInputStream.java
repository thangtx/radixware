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

package org.radixware.kernel.common.utils;

import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * InputStream wrapper for ByteBuffer
 */

public class ByteBufferInputStream extends InputStream {

    public ByteBufferInputStream(final ByteBuffer b) {
        this.buffer = b;
    }

    /**
     * Read the first byte from the wrapped buffer.
     */
    @Override
    public int read() {
        if (buffer.hasRemaining()){
           return 0xff & buffer.get();
        } else {
           return -1;
        }
    }


    /**
     * Read the bytes from the wrapped buffer.
     */
    @Override
    public int read(final byte[] to) {
        return read(to, 0, to.length);
    }


    /**
     * Read the first byte of the wrapped buffer.
     */
    @Override
    public int read(final byte[] to, final int offset, final int len) {
        if (buffer.hasRemaining()) {
            final int lenToRead;
            if (len > available()) {
                lenToRead = available();
            } else {
                lenToRead = len;
            }
            buffer.get(to, offset, lenToRead);
            return lenToRead;
        } else {
            return -1;
        }

    }

    /**
     * Return the available bytes count
     */
    @Override
    public int available() {
        return buffer.remaining();
    }


    @Override
    public void close() {
        //do nothing
    }

    /**
     * Return true if mark is supported.
     * @return always false - as mark is not implemented
     */
    @Override
    public boolean markSupported() {
        return false;
    }

    /**
     * The wrapped buffer
     */
    private final ByteBuffer buffer;
}
