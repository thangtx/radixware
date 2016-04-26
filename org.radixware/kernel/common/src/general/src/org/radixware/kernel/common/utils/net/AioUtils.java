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

package org.radixware.kernel.common.utils.net;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;

import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.utils.io.pipe.PipeSelector;


public final class AioUtils {

    private AioUtils() {
        //singletone
    }

    /**
     * Selector with support of internal pipe
     *
     * @return
     * @throws IOException
     * @throws RadixError
     */
    public static RadixServerSelector openSelector() throws IOException, RadixError {
        // a try to work around http://bugs.sun.com/view_bug.do?bug_id=6427854
        for (int i = 0; i < 100; i++) {
            try {
                final Selector defaultSelector = Selector.open();
                return new RadixServerSelector(new PipeSelector(), defaultSelector);
            } catch (NullPointerException e) {
                try {
                    Thread.sleep(i * 10);
                } catch (InterruptedException ex) {
                    throw new InterruptedIOException();
                }
            }
        }
        throw new RadixError("Can't open Selector due to http://bugs.sun.com/view_bug.do?bug_id=6427854");
    }

    public static ByteBuffer ensureBufferCapacity(final ByteBuffer buf, final int avail) {
        int inc = avail - buf.remaining();
        if (inc <= 0) {
            return buf;
        }
        inc = (inc + 0x03ff) & ~0x03ff; //round up
        final ByteBuffer b = ByteBuffer.allocate(buf.capacity() + inc);
        buf.flip();
        b.put(buf);
        return b;
    }
    
}
