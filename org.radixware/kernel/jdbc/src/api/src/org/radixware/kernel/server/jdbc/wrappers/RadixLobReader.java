/*
 * Copyright (c) 2008-2017, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.jdbc.wrappers;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import org.radixware.kernel.server.jdbc.DBOperationLoggerInterface;

public class RadixLobReader extends Reader {

    private final Reader delegate;
    private final DBOperationLoggerInterface logger;
    private final String operStr;

    public RadixLobReader(Reader delegate, DBOperationLoggerInterface logger, String lobType) {
        this.delegate = delegate;
        this.logger = logger;
        this.operStr = lobType == null ? "Reader" : (lobType + ":Reader");
    }

    private void beforeDbOper() {
        logger.beforeDbOperation(operStr);
    }

    private void afterDbOper() {
        logger.afterDbOperation();
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        beforeDbOper();
        try {
            return delegate.read(cbuf, off, len);
        } finally {
            afterDbOper();
        }
    }

    @Override
    public void close() throws IOException {
        beforeDbOper();
        try {
            delegate.close();
        } finally {
            afterDbOper();
        }
    }

    @Override
    public int read(CharBuffer target) throws IOException {
        beforeDbOper();
        try {
            return delegate.read(target);
        } finally {
            afterDbOper();
        }
    }

    @Override
    public int read() throws IOException {
        beforeDbOper();
        try {
            return delegate.read();
        } finally {
            afterDbOper();
        }
    }

    @Override
    public int read(char[] cbuf) throws IOException {
        beforeDbOper();
        try {
            return delegate.read(cbuf);
        } finally {
            afterDbOper();
        }
    }

    @Override
    public long skip(long n) throws IOException {
        beforeDbOper();
        try {
            return delegate.skip(n);
        } finally {
            afterDbOper();
        }
    }

    @Override
    public boolean ready() throws IOException {
        beforeDbOper();
        try {
            return delegate.ready();
        } finally {
            afterDbOper();
        }
    }

    @Override
    public boolean markSupported() {
        return delegate.markSupported();
    }

    @Override
    public void mark(int readAheadLimit) throws IOException {
        beforeDbOper();
        try {
            delegate.mark(readAheadLimit);
        } finally {
            afterDbOper();
        }
    }

    @Override
    public void reset() throws IOException {
        beforeDbOper();
        try {
            delegate.reset();
        } finally {
            afterDbOper();
        }
    }

}
