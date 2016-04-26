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


public interface BytePipeSink extends Closeable {

    /**
     * Read data to the specified buffer. This function will block until the
     * data could be read.
     *
     * @param destination destination buffer
     * @return number of bytes read, possibly zero
     * @Throws IOException
     */
    int read(final ByteBuffer destination) throws IOException;

    /**
     * Read data to the specified ByteBuffer. This function will block until
     * some data could be read, but no longer then specified timeout.
     *
     * @param destination destination buffer
     * @param timeOutMillis timeOut in milliseconds. If timeOutMillis will be
     * less then or equals to zero, the function will act as following:
     * <ul>
     * <li>timeOutMillis &lt 0 : the function will behave the same as
     * {@linkplain read(ByteBuffer)}
     * <li>timeOutMillis == 0 : the function will behave the same as
     * {@linkplain readNow(ByteBuffer)}
     * </ul>
     *
     * @return number of bytes read, possibly zero
     * @throws IOException
     */
    int read(final ByteBuffer destination, final long timeOutMillis) throws IOException;

    /**
     * Read data to the specified buffer immediately. If there is no data
     * available, this function will simply return zero.
     *
     * @param destination destination buffer
     * @return number of bytes read, possibly zero
     * @Throws IOException
     */
    int readNow(final ByteBuffer destination) throws IOException;

    /**
     * Set timeout for read in blocking mode
     *
     * @param timeOutMillis timeout in milliseconds
     */
    void setReadTimeOut(final long timeOutMillis);

    /**
     * Returns true if there is some data to read
     */
    boolean isDataAvailable();

    void addListener(Listener listener);

    void removeListener(Listener listener);

    public static interface Listener {

        public void dataAvailable();
    }
}
