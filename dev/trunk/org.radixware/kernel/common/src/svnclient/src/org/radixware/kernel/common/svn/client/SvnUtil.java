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
package org.radixware.kernel.common.svn.client;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author akrylov
 */
public class SvnUtil {

    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            hexString.append(Integer.toHexString(0xFF & bytes[i]));
        }
        return hexString.toString().toUpperCase();
    }

    public static int readStreamData(InputStream inputStream, byte[] buffer, int offset, int length) throws IOException {
        int count = 0;
        while (length > 0) {
            int r = inputStream.read(buffer, offset + count, length);
            if (r < 0) {
                if (count == 0) {
                    count = -1;
                }
                break;
            }

            count += r;
            length -= r;
        }
        return count;   
    }

    public static Date parseDate(String value) {

        if (value == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return format.parse(value);
        } catch (ParseException ex) {
            return null;
        }
    }

    public static String toHexDigest(byte[] digest) {
        if (digest == null) {
            return null;
        }

        StringBuffer hexDigest = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            appendHexNumber(hexDigest, digest[i]);
        }
        return hexDigest.toString();
    }

    public static String toHexDigest(MessageDigest digest) {
        if (digest == null) {
            return null;
        }
        byte[] result = digest.digest();
        StringBuffer hexDigest = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            appendHexNumber(hexDigest, result[i]);
        }
        return hexDigest.toString();
    }

    public static void appendHexNumber(StringBuffer target, byte b) {
        int lo = b & 0xf;
        int hi = (b >> 4) & 0xf;
        target.append(HEX[hi]);
        target.append(HEX[lo]);
    }

    public static String getHexNumberFromByte(byte b) {
        int lo = b & 0xf;
        int hi = (b >> 4) & 0xf;
        return Integer.toHexString(hi) + Integer.toHexString(lo);
    }
    private static char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
}
