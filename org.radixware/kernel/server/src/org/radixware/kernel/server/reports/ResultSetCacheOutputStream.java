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
package org.radixware.kernel.server.reports;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;

public class ResultSetCacheOutputStream extends FileOutputStream {

    private static final String EXCEPTION_MESSAGE = "Report result set cache free space is exhausted: {0} byte(s) of {1} used";

    private final ResultSetCacheSizeController sizeController;

    public ResultSetCacheOutputStream(File file, ResultSetCacheSizeController sizeController) throws FileNotFoundException {
        super(file);
        this.sizeController = sizeController;
    }

    @Override
    public void write(int b) throws IOException {
        if (sizeController.decreaseAvailableSpace(1)) {
            super.write(b);
        } else {
            throw new IOException(MessageFormat.format(EXCEPTION_MESSAGE, sizeController.getUsedSpace(), sizeController.getMaxResultSetCacheSize()));
        }
    }

    @Override
    public void write(byte[] b) throws IOException {
        if (sizeController.decreaseAvailableSpace(b.length)) {
            super.write(b);
        } else {
            throw new IOException(MessageFormat.format(EXCEPTION_MESSAGE, sizeController.getUsedSpace(), sizeController.getMaxResultSetCacheSize()));
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (sizeController.decreaseAvailableSpace(len)) {
            super.write(b, off, len);
        } else {
            throw new IOException(MessageFormat.format(EXCEPTION_MESSAGE, sizeController.getUsedSpace(), sizeController.getMaxResultSetCacheSize()));
        }
    }

    @Override
    public void flush() throws IOException {
        super.flush();
    }

    @Override
    public void close() throws IOException {
        super.close();
    }
}
