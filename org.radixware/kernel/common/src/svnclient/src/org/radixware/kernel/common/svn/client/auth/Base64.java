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

/**
 *
 * @author akrylov
 */
class Base64 {

    public static String byteArrayToBase64(byte[] a) {
        return byteArrayToBase64(a, false);
    }

    public static String byteArrayToAltBase64(byte[] a) {
        return byteArrayToBase64(a, true);
    }

    private static String byteArrayToBase64(byte[] a, boolean alternate) {
        int aLen = a.length;
        int numFullGroups = aLen / 3;
        int numBytesInPartialGroup = aLen - 3 * numFullGroups;
        int resultLen = 4 * ((aLen + 2) / 3);
        StringBuffer result = new StringBuffer(resultLen);
        char[] intToAlpha = (alternate ? intToAltBase64 : intToBase64);

        // Translate all full groups from byte array elements to SVNBase64
        int inCursor = 0;
        for (int i = 0; i < numFullGroups; i++) {
            int byte0 = a[inCursor++] & 0xff;
            int byte1 = a[inCursor++] & 0xff;
            int byte2 = a[inCursor++] & 0xff;
            result.append(intToAlpha[byte0 >> 2]);
            result.append(intToAlpha[(byte0 << 4) & 0x3f | (byte1 >> 4)]);
            result.append(intToAlpha[(byte1 << 2) & 0x3f | (byte2 >> 6)]);
            result.append(intToAlpha[byte2 & 0x3f]);
        }

        // Translate partial group if present
        if (numBytesInPartialGroup != 0) {
            int byte0 = a[inCursor++] & 0xff;
            result.append(intToAlpha[byte0 >> 2]);
            if (numBytesInPartialGroup == 1) {
                result.append(intToAlpha[(byte0 << 4) & 0x3f]);
                result.append("==");
            } else {
                // assert numBytesInPartialGroup == 2;
                int byte1 = a[inCursor++] & 0xff;
                result.append(intToAlpha[(byte0 << 4) & 0x3f | (byte1 >> 4)]);
                result.append(intToAlpha[(byte1 << 2) & 0x3f]);
                result.append('=');
            }
        }
        // assert inCursor == a.length;
        // assert result.length() == resultLen;
        return result.toString();
    }

    private static final char intToBase64[] = {'A', 'B', 'C', 'D', 'E', 'F',
        'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
        'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
        'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
        't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5',
        '6', '7', '8', '9', '+', '/'};

    private static final char intToAltBase64[] = {'!', '"', '#', '$', '%',
        '&', '\'', '(', ')', ',', '-', '.', ':', ';', '<', '>', '@', '[',
        ']', '^', '`', '_', '{', '|', '}', '~', 'a', 'b', 'c', 'd', 'e',
        'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
        's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4',
        '5', '6', '7', '8', '9', '+', '?'};

    /**
     * Translates the specified SVNBase64 string (as per
     * Preferences.get(byte[])) into a byte array.
     *
     * @throws IllegalArgumentException if <tt>s</tt> is not a valid SVNBase64
     * string.
     */
    public static int base64ToByteArray(StringBuffer s, byte[] buffer) {
        try {
            return base64ToByteArray(s, buffer, false);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Invalid SVNBase64 string: " + s.toString(), e);
        }
    }

    public static int base64ToByteArray(char[] s, byte[] buffer) {
        try {
            return base64ToByteArray(s, buffer, false);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Invalid SVNBase64 string: " + s.toString(), e);
        }
    }

    public static StringBuffer normalizeBase64(StringBuffer in) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < in.length(); i++) {
            if (Character.isWhitespace(in.charAt(i))) {
                continue;
            }
            result.append(in.charAt(i));
        }
        return result;
    }

    private static int base64ToByteArray(StringBuffer sb, byte[] result, boolean alternate) {
        return base64ToByteArray(sb.toString().toCharArray(), result, alternate);
    }

    private static int base64ToByteArray(char[] s, byte[] result, boolean alternate) {
        byte[] alphaToInt = (alternate ? altBase64ToInt : base64ToInt);
        int sLen = s.length;
        int numGroups = sLen / 4;
        if (4 * numGroups != sLen) {
            numGroups++;
            sLen = 4 * numGroups;
        }

        int missingBytesInLastGroup = 0;
        int numFullGroups = numGroups;
        if (sLen != 0) {
            if (charAt(s, sLen - 1) == '=') {
                missingBytesInLastGroup++;
                numFullGroups--;
            }
            if (charAt(s, sLen - 2) == '=') {
                missingBytesInLastGroup++;
            }
        }
        int resultLength = 3 * numGroups - missingBytesInLastGroup;

        // Translate all full groups from base64 to byte array elements
        int inCursor = 0, outCursor = 0;
        for (int i = 0; i < numFullGroups; i++) {
            int ch0 = alphaToInt[charAt(s, inCursor++)];
            int ch1 = alphaToInt[charAt(s, inCursor++)];
            int ch2 = alphaToInt[charAt(s, inCursor++)];
            int ch3 = alphaToInt[charAt(s, inCursor++)];
            result[outCursor++] = (byte) ((ch0 << 2) | (ch1 >> 4));
            result[outCursor++] = (byte) ((ch1 << 4) | (ch2 >> 2));
            result[outCursor++] = (byte) ((ch2 << 6) | ch3);
        }

        // Translate partial group, if present
        if (missingBytesInLastGroup != 0) {
            int ch0 = alphaToInt[charAt(s, inCursor++)];
            int ch1 = alphaToInt[charAt(s, inCursor++)];
            result[outCursor++] = (byte) ((ch0 << 2) | (ch1 >> 4));

            if (missingBytesInLastGroup == 1) {
                int ch2 = alphaToInt[charAt(s, inCursor++)];
                result[outCursor++] = (byte) ((ch1 << 4) | (ch2 >> 2));
            }
        }
        return resultLength;
    }

    private static char charAt(char[] array, int index) {
        if (index >= array.length) {
            return '=';
        }
        return array[index];
    }

    /**
     * Translates the specified character, which is assumed to be in the "Base
     * 64 Alphabet" into its equivalent 6-bit positive integer.
     *
     * @throws IllegalArgumentException or ArrayOutOfBoundsException if c is not
     * in the SVNBase64 Alphabet.
     */
    /*
     * private static int base64toInt(char c, byte[] alphaToInt) { int result =
     * alphaToInt[c]; if (result < 0) throw new
     * IllegalArgumentException("Illegal character " + c); return result; }
     */
    /**
     * This array is a lookup table that translates unicode characters drawn
     * from the "SVNBase64 Alphabet" (as specified in Table 1 of RFC 2045) into
     * their 6-bit positive integer equivalents. Characters that are not in the
     * SVNBase64 alphabet but fall within the bounds of the array are translated
     * to -1.
     */
    private static final byte base64ToInt[] = {-1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1,
        -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
        13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1,
        -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
        41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51};

    /**
     * This array is the analogue of base64ToInt, but for the nonstandard
     * variant that avoids the use of uppercase alphabetic characters.
     */
    private static final byte altBase64ToInt[] = {-1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, -1,
        62, 9, 10, 11, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 12, 13,
        14, -1, 15, 63, 16, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 17, -1, 18,
        19, 21, 20, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39,
        40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 22, 23, 24, 25};

}
