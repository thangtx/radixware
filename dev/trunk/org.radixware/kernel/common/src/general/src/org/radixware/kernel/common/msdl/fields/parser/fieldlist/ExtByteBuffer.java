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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.kernel.common.utils.SystemPropUtils;

public class ExtByteBuffer {

    public ArrayList<Byte> shieldedList;
    public Byte shield;
    public boolean isHex;
    private final String MAX_BUFFER_SIZE_PROP = "RDX_MSDL_MAX_BUFFER_SIZE_BYTES";
    private final int MAX_BUFFER_SIZE
            = SystemPropUtils.getIntSystemProp(MAX_BUFFER_SIZE_PROP, 1024 * 1024 * 40);
    private final int INITIAL_BUFFER_SIZE = 32;
    private final float EXTEND_FACTOR = 1.5f;

    protected ByteBuffer bf;

    public ByteBuffer getByteBuffer() {
        return bf;
    }

    public ExtByteBuffer() {
        bf = ByteBuffer.allocate(INITIAL_BUFFER_SIZE);
    }

    public void extPut(ByteBuffer from) {
        int mult = 1;
        if (shieldedList != null && shield != null) {
            mult = isHex ? 3 : 2;
        }
        if (from.remaining() * mult > (bf.capacity() - bf.position())) {
            extend(bf.position() + from.remaining() * mult + 4);
        }
        if (shieldedList != null && shield != null) {
            while (from.remaining() > 0) {
                byte b = from.get();
                if (findShielded(b)) {
                    bf.put(shield);
                    if (isHex) {
                        byte[] sh = null;
                        sh = Hex.encode(new byte[]{b}).getBytes(StandardCharsets.US_ASCII);
                        bf.put(sh[0]);
                        bf.put(sh[1]);
                    } else {
                        bf.put(b);
                    }
                } else {
                    bf.put(b);
                }
            }
        } else {
            bf.put(from);
        }
    }

    public void extPut(byte b) {
        if ((bf.capacity() - bf.position() < 4)) {
            extend(bf.position() + 8);
        }
        if (shieldedList != null && shield != null && findShielded(b)) {
            bf.put(shield);
            if (isHex) {
                byte[] sh = null;
                sh = Hex.encode(new byte[]{b}).getBytes(StandardCharsets.US_ASCII);
                bf.put(sh[0]);
                bf.put(sh[1]);
            } else {
                bf.put(b);
            }
        } else {
            bf.put(b);
        }
    }

    private void extend(int minimalSize) {
        final int optimalSize = (int) (bf.capacity() * EXTEND_FACTOR);
        final int newSize = Math.max(optimalSize, minimalSize);
        if (MAX_BUFFER_SIZE < newSize) {
            throw new IllegalArgumentException("Maximum buffer size exceeded");
        }
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
