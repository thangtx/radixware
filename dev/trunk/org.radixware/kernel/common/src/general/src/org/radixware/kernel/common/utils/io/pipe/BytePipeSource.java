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

package org.radixware.kernel.common.utils.io.pipe;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;


public interface BytePipeSource extends Closeable {

    /**
     * Writes data from the specified ByteBuffer. This function will block until
     * some data could be written.
     *
     * @param data data to write
     * @return number of bytes written, possibly zero
     * @throws IOException
     */
    int write(final ByteBuffer data) throws IOException;

    /**
     * Writes data from the specified ByteBuffer. This function will block until
     * some data could be written, but no longer then specified timeout.
     *
     * @param data data to write
     * @param timeOutMillis timeOut in milliseconds. If timeOutMillis will be
     * less then or equals to zero, the function will act as following:
     * <ul>
     * <li>timeOutMillis &lt 0 : the function will behave the same as
     * {@linkplain write(ByteBuffer)}
     * <li>timeOutMillis == 0 : the function will behave the same as
     * {@linkplain writeNow(ByteBuffer)}
     * </ul>
     *
     * @return number of bytes written, possibly zero
     * @throws IOException
     */
    int write(final ByteBuffer data, final long timeOutMillis) throws IOException;

    /**
     * Writes data from the specified ByteBuffer immediately. If it's not
     * possible at the moment, the function will simply return zero.
     *
     * @param data data to write
     * @return number of bytes written, possibly zero
     * @throws IOException
     */
    int writeNow(final ByteBuffer data) throws IOException;

    /**
     * Set timeout for write in blocking mode
     *
     * @param timeOutMillis timeout in milliseconds
     */
    void setWriteTimeOut(final long timeOutMillis);

    boolean isReadyForWrite();

    void addListener(Listener listener);

    void removeListener(Listener listener);

    public static interface Listener {

        public void readyToWrite();
    }
}
