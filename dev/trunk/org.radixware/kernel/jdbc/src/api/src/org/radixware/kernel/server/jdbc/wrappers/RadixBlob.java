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

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import org.radixware.kernel.server.jdbc.DBOperationLoggerInterface;

public class RadixBlob implements Blob {

    final Blob delegate;
    final DBOperationLoggerInterface logger;
    final IRadixJdbcWrapper wrapper;

    public RadixBlob(Blob delegate, DBOperationLoggerInterface logger) {
        this.delegate = delegate;
        this.logger = logger;
        this.wrapper = new RadixJdbcWrapper(logger, "java.sql.Blob");
    }

    private void beforeDbOper() {
        logger.beforeDbOperation("java.sql.Blob");
    }

    private void afterDbOper() {
        logger.afterDbOperation();
    }

    @Override
    public long length() throws SQLException {
        return delegate.length();
    }

    @Override
    public byte[] getBytes(long pos, int length) throws SQLException {
        beforeDbOper();
        try {
            return delegate.getBytes(pos, length);
        } finally {
            afterDbOper();
        }
    }

    @Override
    public InputStream getBinaryStream() throws SQLException {
        beforeDbOper();
        try {
            return wrapper.wrapStream(delegate.getBinaryStream());
        } finally {
            afterDbOper();
        }
    }

    @Override
    public long position(byte[] pattern, long start) throws SQLException {
        beforeDbOper();
        try {
            return delegate.position(pattern, start);
        } finally {
            afterDbOper();
        }
    }

    @Override
    public long position(Blob pattern, long start) throws SQLException {
        beforeDbOper();
        try {
            return delegate.position(pattern, start);
        } finally {
            afterDbOper();
        }
    }

    @Override
    public int setBytes(long pos, byte[] bytes) throws SQLException {
        beforeDbOper();
        try {
            return delegate.setBytes(pos, bytes);
        } finally {
            afterDbOper();
        }
    }

    @Override
    public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
        beforeDbOper();
        try {
            return delegate.setBytes(pos, bytes, offset, len);
        } finally {
            afterDbOper();
        }
    }

    @Override
    public OutputStream setBinaryStream(long pos) throws SQLException {
        beforeDbOper();
        try {
            return wrapper.wrapStream(delegate.setBinaryStream(pos));
        } finally {
            afterDbOper();
        }
    }

    @Override
    public void truncate(long len) throws SQLException {
        beforeDbOper();
        try {
            delegate.truncate(len);
        } finally {
            afterDbOper();
        }
    }

    @Override
    public void free() throws SQLException {
        beforeDbOper();
        try {
            delegate.free();
        } finally {
            afterDbOper();
        }
    }

    @Override
    public InputStream getBinaryStream(long pos, long length) throws SQLException {
        beforeDbOper();
        try {
            return wrapper.wrapStream(delegate.getBinaryStream(pos, length));
        } finally {
            afterDbOper();
        }
    }

}
