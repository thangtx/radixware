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

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.utils.Hex;


public class DataSourceInputStream implements IDataSource {

    public ArrayList<Byte> shieldedList;
    public Byte shield;
    public boolean isHex;
    private ByteBuffer bf = ByteBuffer.allocate(1024);
    private InputStream is;
    private  int position = 0;

    public DataSourceInputStream(InputStream is) throws IOException {
        this.is = is;
        bf.mark();
    }

    @Override
    public int getPosition() {
        return bf.position() + position;
    }
    
    private interface IInputDataSource {
        int get() throws IOException;
        void get(byte [] outArray) throws IOException;
    }
    
    private class ByteBufferInputDataSource implements IInputDataSource {
        @Override
        public int get() throws IOException {
            bf.flip();
            return bf.get();
        }

        @Override
        public void get(byte[] outArray) throws IOException {
            bf.flip();
            bf.get(outArray);
        }
        
    }
    
    private class InputStreamInputSource implements IInputDataSource {
        @Override
        public int get() throws IOException{
            position++;
            return is.read();
        }

        @Override
        public void get(byte[] outArray) throws IOException {
            int read = is.read(outArray);
            position += read;
        }
        
    }

    @Override
    public byte get() throws IOException {
        IInputDataSource source;
        if (bf.position() == 0) {
            source = new InputStreamInputSource();
            
        }
        else {
            source = new ByteBufferInputDataSource();
        }
        byte b = (byte) source.get();
        return readShieldedByte(b, source);
    }

    @Override
    public byte[] getAll() throws IOException {
        byte[] allInStream = new byte[is.available()];
        is.read(allInStream);
        byte[] all = null;
        if (bf.position() > 0) {
            bf.flip();
            byte[] fromBuf = new byte[bf.limit()];
            bf.get(fromBuf);

            int allLen = fromBuf.length + allInStream.length;
            all = new byte[allLen];

            int i = 0;
            for (; i < fromBuf.length; i++) {
                all[i] = fromBuf[i];
            }

            int j = 0;
            for (; i < allLen; i++) {
                all[i] = allInStream[j++];
            }
            bf.limit(bf.capacity());
        } else {
            all = allInStream;
        }
        return all;
    }

    @Override
    public int available() throws IOException {
        return is.available() + bf.position();
    }

    @Override
    public byte[] get(int len) throws IOException {
        byte[] res = new byte[len];
        int remaining = len;
        bf.flip();
        if (bf.limit() > 0) {
            if (len > bf.limit()) {
                remaining = len - bf.limit();
                bf.get(res, 0, bf.limit());
                bf.rewind();
                bf.limit(bf.capacity());
            }
        }

        if (remaining > 0) {
            byte[] remBuf = new byte[remaining];
            is.read(remBuf);
            int j = 0;
            for (int i = len - remaining - 1; i < len; i++) {
                res[i] = remBuf[j++];
            }
        }
        return res;
    }

    @Override
    public ByteBuffer getByteBuffer() throws IOException {
        int avail = is.available();
        byte[] res = new byte[avail];
        is.read(res);
        ByteBuffer out = ByteBuffer.allocate(avail + bf.position());
        out.put(bf);
        out.put(res);
        return out;
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
    public void prepend(IDataSource other) throws IOException {
        bf.mark();
        bf.put(other.getByteBuffer());
        bf.reset();
    }
    
    private byte readShieldedByte(byte b, IInputDataSource source) throws IOException {
        if (shieldedList != null && shield != null && b == shield) {
            if (isHex) {
                return readHexShieldedByte(source);
            } else {
                b = (byte) source.get();
            }
            b = (byte) source.get();
        }
        return b;
    }

    private byte readHexShieldedByte(IInputDataSource inp) throws IOException {
        byte[] sh = new byte[2];
        inp.get(sh);
        String h = null;
        try {
            h = new String(sh, "US-ASCII");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(DataSourceByteBuffer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Hex.decode(h)[0];
    }
}
