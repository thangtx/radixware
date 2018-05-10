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
import java.io.InputStream;
import org.radixware.kernel.server.jdbc.DBOperationLoggerInterface;

public class RadixLobInputStream extends InputStream {

    private final InputStream delegate;
    private final DBOperationLoggerInterface logger;
    private final String operStr;

    public RadixLobInputStream(InputStream delegate, DBOperationLoggerInterface logger, String lobType) {
        this.delegate = delegate;
        this.logger = logger;
        this.operStr = lobType == null ? "InputStream" : (lobType + ":InputStream");
    }

    private void beforeDbOper() {
        logger.beforeDbOperation(operStr);
    }

    private void afterDbOper() {
        logger.afterDbOperation();
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
    public int read(byte[] b) throws IOException {
        beforeDbOper();
        try {
            return delegate.read(b);
        } finally {
            afterDbOper();
        }
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        beforeDbOper();
        try {
            return delegate.read(b, off, len);
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
    public int available() throws IOException {
        beforeDbOper();
        try {
            return delegate.available();
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
    public synchronized void mark(int readlimit) {
        beforeDbOper();
        try {
            delegate.mark(readlimit);
        } finally {
            afterDbOper();
        }
    }

    @Override
    public synchronized void reset() throws IOException {
        beforeDbOper();
        try {
            delegate.reset();
        } finally {
            afterDbOper();
        }
    }

    @Override
    public boolean markSupported() {
        return delegate.markSupported();
    }

}
