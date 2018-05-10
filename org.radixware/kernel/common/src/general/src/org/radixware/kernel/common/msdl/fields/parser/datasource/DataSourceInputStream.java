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
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.msdl.fields.parser.ParseUtil;
import org.radixware.kernel.common.utils.Hex;

public class DataSourceInputStream implements IDataSource {

    public ArrayList<Byte> shieldedList;
    public Byte shield;
    public boolean isHex;
    private final ExtByteBufferReadWrite bf = new ExtByteBufferReadWrite();
    private final InputStream is;
    final private IInputDataSource source;

    public DataSourceInputStream(InputStream is) {
        this.is = is;
        bf.limit(0);
        source = new DefaultDataSource();
    }

    private interface IInputDataSource {

        int get() throws IOException;
    }

    private class DefaultDataSource implements IInputDataSource {

        @Override
        public int get() throws IOException {
            if (bf.remaining() > 0) {
                return bf.get();
            }
            return is.read();
        }
    }

    @Override
    public byte get() throws IOException {
        byte b = (byte) source.get();
        if (shieldedList != null && shield != null && b == shield && hasAvailable()) {
            b = (byte) source.get();
            int d = Character.digit((char) b, 16);
            if (isHex && d != -1 && hasAvailable()) {
                byte[] sh = new byte[2];
                byte b2 = (byte) source.get();
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
    public boolean hasAvailable() throws IOException {
        if (bf.remaining() > 0) {
            return true;
        }
        final int b = is.read();
        if (b != -1) {
            bf.clear();
            bf.put((byte) b);
            bf.flip();
            return true;
        } else {
            return false;
        }
    }

    private void append(byte[] arr, int count) {
        bf.compact();
        bf.put(arr, 0, count);
        bf.flip();
    }

    private int readFromStream(byte[] resultArr, int needToReadBytesCnt) throws IOException {
        int allReadedBytesCnt = 0;
        int wasReadedBytesCnt;
        while (true) {
            final byte[] tempArr = new byte[needToReadBytesCnt - allReadedBytesCnt];
            wasReadedBytesCnt = is.read(tempArr);
            if (wasReadedBytesCnt == -1) {
                return allReadedBytesCnt != 0 ? allReadedBytesCnt : -1;
            }
            System.arraycopy(tempArr, 0, resultArr, allReadedBytesCnt, wasReadedBytesCnt);
            allReadedBytesCnt += wasReadedBytesCnt;
            if (allReadedBytesCnt == needToReadBytesCnt) {
                return allReadedBytesCnt;
            }
        }
    }

    @Override
    public boolean hasAvailable(int len) throws IOException {
        if (len <= 0) {
            throw new IllegalArgumentException("Illegal length parameter value");
        }
        if (bf.remaining() >= len) {
            return true;
        }

        final int byteToReadCnt = len - bf.remaining();
        final byte[] readFromStream = new byte[byteToReadCnt];
        final int byteWasReaded = readFromStream(readFromStream, byteToReadCnt);
        if (byteWasReaded != byteToReadCnt) {
            if (byteWasReaded > 0) {
                append(readFromStream, byteWasReaded);
            }
            return false;
        }

        append(readFromStream, byteWasReaded);
        return true;
    }

    @Override
    public byte[] get(int len) throws IOException {
        if (len <= 0) {
            throw new IllegalArgumentException("Illegal length parameter value");
        }
        ExtByteBuffer buffer = new ExtByteBuffer();
        for (int index = 0; index < len; index++) {
            buffer.extPut(get());
        }
        return ParseUtil.extractByteBufferContent(buffer.flip());
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
    public void prepend(IDataSourceArray other) throws IOException {
        if ((other.available() + bf.remaining()) > bf.capacity()) {
            throw new IOException("Cannot prepend this buffer");
        }

        final byte[] arrRemaining = new byte[bf.remaining()];
        bf.get(arrRemaining);

        bf.clear();
        bf.put(other.getByteBuffer());
        bf.put(arrRemaining);
        bf.flip();
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
}
