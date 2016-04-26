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

package org.radixware.kernel.common.scml;


public class LiteralWriter {

    private static final char[] ESCAPE_N = new char[]{'\\', 'n'};
//  /  private static final char[] ESCAPE_ZERO = new char[]{'\\', 'u', '0', '0', '0', '0'};
    private static final char[] ESCAPE_R = new char[]{'\\', 'r'};
    private static final char[] ESCAPE_T = new char[]{'\\', 't'};
    private static final char[] ESCAPE_F = new char[]{'\\', 'f'};
    private static final char[] ESCAPE_B = new char[]{'\\', 'b'};
    private static final char[] ESCAPE_DQ = new char[]{'\\', '"'};
    private static final char[] ESCAPE_SQ = new char[]{'\\', '\''};
    private static final char[] ESCAPE_SLASH = new char[]{'\\', '\\'};

    private static char[] createEscape(char c) {
        switch (c) {
            case '\n':
                return ESCAPE_N;
            case '\r':
                return ESCAPE_R;
            case '\t':
                return ESCAPE_T;
            case '\f':
                return ESCAPE_F;
            case '\b':
                return ESCAPE_B;
            case '\"':
                return ESCAPE_DQ;
            case '\'':
                return ESCAPE_SQ;
            case '\\':
                return ESCAPE_SLASH;
            default:
                String asHex = Integer.toHexString((int) c);
                char[] result = new char[6];
                result[0] = '\\';
                result[1] = 'u';
                for (int i = asHex.length() - 1, j = 5; j > 1; j--, i--) {
                    if (i < 0) {
                        result[j] = '0';
                    } else {
                        result[j] = asHex.charAt(i);
                    }
                }
                return result;
        }
    }

    public static final String str2Literal(String s) {
        if (s == null) {
            return "";
        }
        int length = s.length();
        StringBuilder res = new StringBuilder(length * 2);
        for (int i = 0; i < length; i++) {
            if (Character.isISOControl(s.charAt(i)) || s.charAt(i) == '"' || s.charAt(i) == '\'' || s.charAt(i) == '\\') {
                res.append(createEscape(s.charAt(i)));
            } else {
                res.append(s.charAt(i));
            }
        }
        return res.toString();
    }
}
