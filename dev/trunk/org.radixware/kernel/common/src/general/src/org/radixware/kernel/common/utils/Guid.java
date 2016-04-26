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

import java.util.UUID;

/**
 * GUID generator.
 * GUID - globally unique identifier.
 * GUID has a string representation - random 16 bit binary data converted to Base32.
 * GUID contains generation time, MAC address, random numbers, thats gives the guarantee to globally unique per each generation.
 */
public class Guid {

    private Guid() {
    }
    private static final char[] digitPresentations = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
        'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7'
    };

    private static int intValue(final byte b) {
        return (b >= 0) ? b : (b + 256);
    }

    private static String encode(final byte[] bytes) { //base32
        final StringBuilder res = new StringBuilder((bytes.length + 7) * 8 / 5);

        int curByteIntVal, nextByteIntVal;
        int i = 0, shift = 0, base32Digit = 0;

        while (i < bytes.length) {
            curByteIntVal = intValue(bytes[i]);
            if (shift > 3) {
                if ((i + 1) < bytes.length) {
                    nextByteIntVal = intValue(bytes[i + 1]);
                } else {
                    nextByteIntVal = 0;
                }
                base32Digit = curByteIntVal & (0xFF >> shift);
                shift = (shift + 5) % 8;
                base32Digit <<= shift;
                base32Digit |= nextByteIntVal >> (8 - shift);
                i++;
            } else {
                base32Digit = (curByteIntVal >> (8 - (shift + 5))) & 0x1F;
                shift = (shift + 5) % 8;
                if (shift == 0) {
                    i++;
                }
            }
            res.append(digitPresentations[base32Digit]);
        }
        return res.toString();
    }

    /**
     * GUID - globally unique identifier.
     * GUID contains generation time, MAC address, random numbers, thats gives the guarantee to globally unique per each generation.
     * GUID in Radix has string representation - random 16 bit binary data converted to Base32.
     * @return string representation of new generated GUID.
     */
    public static String generateGuid() {
        return generateGuid(null);
    }

    public static String generateGuid(String name) {
        final UUID uuid = name == null ? UUID.randomUUID() : UUID.fromString(name);
        // final UUID uuid = UUID.randomUUID();
        long mostSignificantBits = uuid.getMostSignificantBits();
        long leastSignificantBits = uuid.getLeastSignificantBits();
        final byte[] id = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int pos = 16;
        do {
            id[--pos] = (byte) (leastSignificantBits & 0xFF);
            leastSignificantBits >>>= 8;
        } while (leastSignificantBits != 0);
        pos = 8;
        do {
            id[--pos] = (byte) (mostSignificantBits & 0xFF);
            mostSignificantBits >>>= 8;
        } while (mostSignificantBits != 0);

        final String idBase32 = encode(id);
        return idBase32;
    }
}
