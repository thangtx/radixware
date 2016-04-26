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

import java.io.IOException;
import java.io.InputStream;
import org.radixware.kernel.common.exceptions.IllegalUsageError;


public final class UnreadableInputStream extends InputStream{
    final InputStream s;

    UnreadableInputStream(final InputStream s) {
        this.s = s;
    }

    InputStream getStream() {
        return s;
    }

    @Override
    public int read() throws IOException {
        if (unreadedByte >= 0) {
            final int b = (byte) unreadedByte;
            unreadedByte = -1;
            return b;
        }
        return s.read();
    }

    @Override
    public int read(final byte[] b) throws IOException {
        if (b.length > 0 && unreadedByte >= 0) {
            b[0] = (byte) unreadedByte;
            unreadedByte = -1;
            return 1;
        }
        return s.read(b);
    }

    public void unread(final byte b) {
        if (unreadedByte != -1) {
            throw new IllegalUsageError("Unread buffer overflow");
        }
        unreadedByte = b;
    }
    int unreadedByte = -1;
    
}
