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

package org.radixware.kernel.common.build.xbeans;


public class XmlEscapeStr {

    private XmlEscapeStr() {
    }

    /**
     * Convert string with characters that cannot be used in XML document into
     * valid XML string. If character cannot be used in XML it will be replaced
     * with backslash and character code in UTF-8 (for hexadecimal digits).
     * Backslash character in string escaped by preceding it with another
     * backslash. http://en.wikipedia.org/wiki/Valid_characters_in_XML
     *
     * @param s string that may contains illegal characters
     * @return encoded string with valid XML characters
     */
    public static String getSafeXmlString(final String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        final StringBuilder safeStringBuilder = new StringBuilder();
        final char[] chars = s.toCharArray();
        final int count = chars.length;
        for (int i = 0; i < count; i++) {
            final char c = chars[i];
            if (isBadXmlChar(c)) {
                safeStringBuilder.append('\\');
                String hexNumber = Integer.toHexString(c).toUpperCase();
                for (int j = hexNumber.length(); j < 4; j++) {
                    safeStringBuilder.append('0');
                }
                safeStringBuilder.append(hexNumber);
            } else if (c == '\\') {
                safeStringBuilder.append("\\\\");

            } else {
                safeStringBuilder.append(c);
            }
        }
        return safeStringBuilder.toString();
    }

    /**
     * Convert string with escaped characters into source string. Backslash
     * character and followed character code in UTF-8 (for hexadecimal digits)
     * will be replaced with single character. Double backslash characters will
     * be replaced with single backslash.
     * http://en.wikipedia.org/wiki/Valid_characters_in_XML
     *
     * @param s string from XML document
     * @return decoded string with unescaped characters
     */
    public static String parseSafeXmlString(final String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        final StringBuilder sourceStringBuilder = new StringBuilder();
        final char[] chars = s.toCharArray();
        final char[] hexNumber = new char[4];
        int digitIndex = 0;
        boolean escape = false;
        for (int i = 0, count = chars.length; i < count; i++) {
            final char c = chars[i];
            if (escape) {
                if (Character.digit(c, 16) > -1) {
                    hexNumber[digitIndex] = c;
                    digitIndex++;
                    if (digitIndex == 4) {//build character from code point
                        final int charCode = Integer.parseInt(String.valueOf(hexNumber), 16);
                        final char[] parsedChars = Character.toChars(charCode);
                        if (parsedChars.length != 1) {//malformed sequence - save as is
                            sourceStringBuilder.append('\\');
                            sourceStringBuilder.append(hexNumber, 0, 4);
                        } else {
                            sourceStringBuilder.append(parsedChars[0]);
                        }
                        digitIndex = 0;
                        escape = false;
                    }
                } else if (c == '\\' && digitIndex == 0) {
                    sourceStringBuilder.append('\\');
                    escape = false;
                } else {//malformed sequence - save as is
                    sourceStringBuilder.append('\\');
                    if (digitIndex > 0) {
                        sourceStringBuilder.append(hexNumber, 0, digitIndex);
                        digitIndex = 0;
                    }
                    sourceStringBuilder.append(c);
                    escape = false;
                }
            } else if (c == '\\') {
                escape = true;
            } else {
                sourceStringBuilder.append(c);
            }
        }
        if (escape) {//malformed sequence - save as is
            sourceStringBuilder.append('\\');
            if (digitIndex > 0) {
                sourceStringBuilder.append(hexNumber, 0, digitIndex);
            }
        }
        return sourceStringBuilder.toString();
    }

    /**
     * Check if character cannot be used in XML document.
     *
     * @param c character to check for
     * @return true if character cannot be used in XML
     */
    private static boolean isBadXmlChar(final char c) {
        return !(c == 0x09 || c == 0x0A || c == 0x0D || c >= 0x20 && c <= 0xD7FF || c >= 0xE000 && c <= 0xFFFD);
    }
}
