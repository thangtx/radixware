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

import java.awt.Color;

/**
 * Color<=>XML converter.
 */
public class XmlColor {

    /**
     * Parse color from HTML format to ATW Color.
     * For example: #FFFFFF => Color.WHITE;
     * @return AWT color or null if xColor is null or has invalid format.
     */
    public static Color parseColor(String xColor) {
        if (xColor == null || xColor.length() != 7) {
            return null;
        }
        try {
            String colorInHex = xColor.substring(1);
            int rgb = Integer.parseInt(colorInHex, 16);
            Color result = new Color(rgb);
            return result;
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    /**
     * Gets a hex string for an integer.  Ensures the result is always two characters long, which is not
     * true of Integer.toHexString().
     *
     * @param r an integer < 255
     * @return a 2 character hexadecimal string
     */
    private static String hexToStringPad2(int r) {
        String s = Integer.toHexString(r);

        if (s.length() == 1) {
            s = '0' + s;
        }

        return s;
    }

    /**
     * Convert AWT color to HTML format.
     * For example: Color.WHITE => #FFFFFF;
     */
    public static String mergeColor(Color color) {
        return "#" + hexToStringPad2(color.getRed()) + hexToStringPad2(color.getGreen()) + hexToStringPad2(color.getBlue());
    }
}
