/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.common.svn.client.auth;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Arrays;

/**
 *
 * @author akrylov
 */
public class EncodingUtil {

    public static byte[] getBytes(final char[] data, String charset) {
        if (data == null) {
            return new byte[0];
        }
        final CharBuffer cb = CharBuffer.wrap(data);
        Charset chrst;
        try {
            chrst = Charset.forName(charset);
        } catch (UnsupportedCharsetException e) {
            chrst = Charset.defaultCharset();
        }
        try {
            ByteBuffer bb = chrst.newEncoder().encode(cb);
            final byte[] bytes = new byte[bb.limit()];
            bb.get(bytes);
            if (bb.hasArray()) {
                clearArray(bb.array());
            }
            return bytes;
        } catch (CharacterCodingException e) {
        }

        final byte[] bytes = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            bytes[i] = (byte) (data[i] & 0xFF);
        }
        return bytes;
    }

    public static char[] copyOf(char[] source) {
        final char[] copy = source != null ? new char[source.length] : null;
        if (copy != null) {
            System.arraycopy(source, 0, copy, 0, source.length);
        }
        return copy;

    }

    public static char[] getChars(byte[] data, String charset) {
        return getChars(data, 0, data != null ? data.length : 0, charset);
    }

    public static char[] getChars(byte[] data, int offset, int length, String charset) {
        if (data == null) {
            return new char[0];
        }
        Charset chrst;
        try {
            chrst = Charset.forName(charset);
        } catch (UnsupportedCharsetException e) {
            chrst = Charset.defaultCharset();
        }
        try {
            CharBuffer cb = chrst.newDecoder().decode(ByteBuffer.wrap(data, offset, length));
            final char[] chars = new char[cb.limit()];
            cb.get(chars);
            return chars;
        } catch (CharacterCodingException e) {
        }
        final char[] chars = new char[data.length];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) data[i];
        }
        return chars;
    }

    public static void clearArray(byte[] array) {
        if (array == null) {
            return;
        }
        for (int i = 0; i < array.length; i++) {
            array[i] = 0;
        }
        Arrays.fill(array, (byte) 0xFF);
    }

    public static void clearArray(char[] array) {
        if (array == null) {
            return;
        }
        Arrays.fill(array, '\0');
    }

}
