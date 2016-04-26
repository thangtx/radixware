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

package org.radixware.kernel.common.msdl.fields.parser.fieldlist;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.utils.Hex;


public class ExtByteBuffer {

    public ArrayList<Byte> shieldedList;
    public Byte shield;
    public boolean isHex;

    protected ByteBuffer bf;

    public ByteBuffer getByteBuffer() {
        return bf;
    }

    public ExtByteBuffer() {
        bf = ByteBuffer.allocate(4);
    }

    public void extPut(ByteBuffer from) {
        if (from.remaining()*2 > (bf.capacity() - bf.position())) {
            extend(bf.position() + from.remaining()*2 + 4);
        }
        if (shieldedList!=null && shield !=null) {
            while (from.remaining() > 0) {
                byte b = from.get();
                if (findShielded(b)) {
                    bf.put(shield);
                    if (isHex) {
                        byte[] sh = null;
                        try {
                            sh = Hex.encode(new byte[]{b}).getBytes("US-ASCII");
                        } catch (UnsupportedEncodingException ex) {
                            Logger.getLogger(ExtByteBuffer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        bf.put(sh[0]);
                        bf.put(sh[1]);
                    }
                    else {
                        bf.put(b);
                    }
                }
                else
                    bf.put(b);
            }
        }
        else
            bf.put(from);
    }

    public void extPut(byte b) {
        if ((bf.capacity() - bf.position() < 4)) {
            extend(bf.position() + 8);
        }
        if (shieldedList!=null && shield !=null && findShielded(b)) {
            bf.put(shield);
            if (isHex) {
                byte[] sh = null;
                try {
                    sh = Hex.encode(new byte[]{b}).getBytes("US-ASCII");
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(ExtByteBuffer.class.getName()).log(Level.SEVERE, null, ex);
                }
                bf.put(sh[0]);
                bf.put(sh[1]);
            }
            else {
                bf.put(b);
            }
        }
        else
            bf.put(b);
    }

    private void extend (int newSize) {
        ByteBuffer bf2 = ByteBuffer.allocate(newSize);
        bf.flip();
        bf2.put(bf);
        bf = bf2;
    }

    private boolean findShielded(byte b) {
        for (Byte cur : shieldedList) {
            if (cur == b) {
                return true;
            }
        }
        return false;
    }

    public ByteBuffer flip() {
        bf.flip();
        return bf;
    }

}
