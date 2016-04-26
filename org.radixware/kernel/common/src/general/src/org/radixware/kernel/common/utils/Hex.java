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

import java.nio.ByteBuffer;
import org.radixware.kernel.common.exceptions.WrongFormatError;


public class Hex {

    public static final String encode(final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return new String(org.apache.xmlbeans.impl.util.HexBin.encode(bytes));
    }

    public static final byte[] decode(final String str) {
        if (str == null) {
            return null;
        }
        final byte[] res;
        if (str.length() % 2==0){
            res = org.apache.xmlbeans.impl.util.HexBin.decode(str.getBytes());
        }else{
            res = org.apache.xmlbeans.impl.util.HexBin.decode(("0"+str).getBytes());
        }
        if (res == null) {
            throw new WrongFormatError("Wrong HEX: \"" + str + "\"");
        }
        return res;
    }

    public static boolean isHexDigit(final byte b) {
        return ((b >= '0' && b <= '9')
                || (b >= 'a' && b <= 'f')
                || (b >= 'A' && b <= 'F'));
    }

    public static final String encode(ByteBuffer bf) {
        if (bf == null) {
            return null;
        }
        int pos = bf.position();
        bf.position(0);
        byte[] arr = new byte[pos];
        bf.get(arr);
        return encode(arr);
    }

    public static final ByteBuffer decodeToByteBuffer(final String str) {
        return ByteBuffer.wrap(decode(str));
    }

    public static final String byte2Hex(int b) {
        if (b < 0) {
            b = 256 + b;
        }
        final String hex = Integer.toHexString(b).toUpperCase();
        if (hex.length() % 2 == 0) {
            return hex;
        } else {
            return "0" + hex;
        }
    }
}
