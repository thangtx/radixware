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

package org.radixware.kernel.common.msdl.fields.parser.datasource;

import org.radixware.kernel.common.msdl.fields.parser.fieldlist.ExtByteBuffer;
import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 *
 * @author npopov
 */
public class ExtByteBufferReadWrite extends ExtByteBuffer {

    public int remaining() {
        return bf.remaining();
    }
    
    public int capacity() {
        return bf.capacity();
    }

    public byte get() {
        return bf.get();
    }

    public ByteBuffer get(byte[] arr) {
        return bf.get(arr);
    }

    public int limit() {
        return bf.limit();
    }

    public Buffer limit(int newLimit) {
        return bf.limit(newLimit);
    }

    public Buffer clear() {
        return bf.clear();
    }

    public void put(byte b) {
        extPut(b);
    }
    
    public void put(byte[] arr) {
        put(arr, 0, arr.length);
    }

    public void put(byte[] arr, int off, int len) {
        extPut(ByteBuffer.wrap(arr, off, len));
    }

    public void put(ByteBuffer buf) {
        extPut(buf);
    }

    public ByteBuffer compact() {
        return bf.compact();
    }

}
