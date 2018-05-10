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
package org.radixware.kernel.utils.traceview.utils;

import java.io.IOException;
import java.io.InputStream;

public class ProgressInputStream extends InputStream {

    private final InputStream stream;
    private long size = 0;
    private final long fileLength;

    public ProgressInputStream(InputStream stream, long fileLength) {
        this.stream = stream;
        this.fileLength = fileLength;
    }

    public int getProgressInPercentage() {
        return (int) (size / (fileLength / 100));
    }

    @Override
    public int read() throws IOException {
        int read = stream.read();
        if (read != -1) {
            size++;
        }
        return read;
    }

    @Override
    public boolean markSupported() {
        return stream.markSupported();
    }

    @Override
    public synchronized void reset() throws IOException {
        stream.reset();
    }

    @Override
    public synchronized void mark(int readlimit) {
        stream.mark(readlimit);
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }

    @Override
    public int available() throws IOException {
        return stream.available();
    }

    @Override
    public long skip(long n) throws IOException {
        return stream.skip(n);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int read = stream.read(b, off, len);
        if (read != -1) {
            size += read;
        }
        return read;
    }

    @Override
    public int read(byte[] b) throws IOException {
        int read = stream.read(b);
        if (read != -1) {
            size += read;
        }
        return read;
    }

}
