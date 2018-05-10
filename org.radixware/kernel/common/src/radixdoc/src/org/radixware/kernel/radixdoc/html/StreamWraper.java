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


package org.radixware.kernel.radixdoc.html;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;


public interface StreamWraper<T extends Closeable> extends Closeable {
    T getStream();
}

final class FileOutputStreamWraper implements StreamWraper<OutputStream> {

    private final OutputStream stream;

    public FileOutputStreamWraper(OutputStream stream) {
        this.stream = stream;
    }
    
    @Override
    public OutputStream getStream() {
        return stream;
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }
}

final class ZipOutputStreamWraper implements StreamWraper<OutputStream> {

    private final ZipOutputStream stream;

    public ZipOutputStreamWraper(ZipOutputStream stream) {
        this.stream = stream;
    }
    
    @Override
    public OutputStream getStream() {
        return getZipStream();
    }
    
    ZipOutputStream getZipStream() {
        return stream;
    }

    @Override
    public void close() throws IOException {
        stream.closeEntry();
    }
}