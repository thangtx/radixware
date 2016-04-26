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

import com.sun.mail.util.BASE64DecoderStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Base64 {

    public static String encode(final byte[] bytes) {
        return new String(org.apache.xmlbeans.impl.util.Base64.encode(bytes));
    }

    public static byte[] decode(final String str) {
        return org.apache.xmlbeans.impl.util.Base64.decode(str.getBytes());
    }

    public static void decode(final InputStream in, final OutputStream out) throws IOException {
        BASE64DecoderStream decoder = new com.sun.mail.util.BASE64DecoderStream(in);
        try {
            FileUtils.copyStream(in, out);
        } finally {
            try {
                decoder.close();
            } catch (IOException ex) {
                Logger.getLogger(Base64.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
