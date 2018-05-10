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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.msdl.fields.parser.ParseUtil;
import org.radixware.kernel.common.utils.Hex;

public class DataSourceByteBuffer extends ExtByteBuffer implements IDataSourceArray {

    public DataSourceByteBuffer(ByteBuffer bf) {
        this.bf = bf;
    }

    public DataSourceByteBuffer(byte[] arr) {
        bf = ByteBuffer.wrap(arr);
    }

    @Override
    public byte get() {
        byte b = bf.get();
        if (shieldedList != null && shield != null && b == shield && bf.hasRemaining()) {
            b = bf.get();
            int d = Character.digit((char) b, 16);
            if (isHex && d != -1 && bf.hasRemaining()) {
                byte[] sh = new byte[2];
                byte b2 = bf.get();
                sh[0] = b;
                sh[1] = b2;
                String h = null;
                h = new String(sh, StandardCharsets.US_ASCII);
                return Hex.decode(h)[0];
            }
        }
        return b;
    }

    @Override
    public byte[] getAll() throws IOException {
        ExtByteBuffer buffer = new ExtByteBuffer();
        while (hasAvailable()) {
            buffer.extPut(get());
        }
        return ParseUtil.extractByteBufferContent(buffer.flip());
    }

    @Override
    public int available() throws IOException {
        return bf.remaining();
    }

    @Override
    public boolean hasAvailable() throws IOException {
        return bf.remaining() > 0;
    }

    @Override
    public boolean hasAvailable(int len) throws IOException {
        return bf.remaining() >= len;
    }

    @Override
    public byte[] get(int len) throws IOException {
        ExtByteBuffer buffer = new ExtByteBuffer();
        for (int index = 0; index < len; index++) {
            buffer.extPut(get());
        }
        return ParseUtil.extractByteBufferContent(buffer.flip());
    }

    @Override
    public ByteBuffer getByteBuffer() {
        return bf;
    }

    @Override
    public void prepend(IDataSourceArray other) throws IOException {
        if (this == other) {
            return;
        }
        int inOther = other.available();
        if ((bf.position() < inOther) || (bf.position() >= inOther && bf.capacity() < inOther)) {
            throw new IOException("Cannot prepend this buffer");
        }
        int insertPos = bf.position() > inOther ? bf.position() - inOther : 0;
        bf.position(insertPos);
        bf.mark();
        bf.put(other.getAll());
        bf.reset();
    }

    @Override
    public int getPosition() {
        return bf.position();
    }

    @Override
    public void setShield(byte b) {
        shield = b;
    }

    @Override
    public void setShieldedList(ArrayList<Byte> list) {
        shieldedList = list;
    }

    @Override
    public void setShieldIsHex(boolean isHex) {
        this.isHex = isHex;
    }

    @Override
    public String toString() {
        return bf.toString();
    }
}
