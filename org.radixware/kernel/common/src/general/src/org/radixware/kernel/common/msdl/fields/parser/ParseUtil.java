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

package org.radixware.kernel.common.msdl.fields.parser;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;

import org.radixware.kernel.common.utils.Hex;


public class ParseUtil {

    public static String createString(char c, int len) {
        StringBuffer sb = new StringBuffer(len);
        for (int i = 0; i < len; i++) {
            sb.append(c);
        }
        return new String(sb);
    }

    public static byte convertHexToByte(byte b1, byte b2) throws UnsupportedEncodingException {
        return Hex.decode(new String(new byte[]{b1, b2}, "US-ASCII"))[0];
    }

    public static byte[] convertByteToHex(byte b) {
        byte res[] = null;
        try {
            String s = Integer.toHexString(b).toUpperCase();
            res = s.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            Logger.getLogger(ParseUtil.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        return res;
    }

    public static byte[] extractByteBufferContent(ByteBuffer bf) {
        byte[] res = new byte[bf.remaining()];
        bf.get(res);
        return res;
    }

    public static XmlObject addChildToXmlObject(XmlObject parent, XmlObject child) {
        return null;
    }

    public static XmlObject getOrCreateChild(XmlObject parent) {
        return null;
    }

    public static XmlObject getChild(XmlObject parent) {
        return null;
    }

    public static byte[] concatenateArrays(byte[] head, byte[] tail) {
        byte[] ret = new byte[head.length + tail.length];
        System.arraycopy(head, 0, ret, 0, head.length);
        System.arraycopy(tail, 0, ret, head.length, tail.length);
        return ret;
    }
}
