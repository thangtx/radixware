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
import java.io.Reader;
import java.io.Writer;
import java.sql.Clob;
import java.sql.SQLException;
import org.radixware.kernel.server.jdbc.DBOperationLoggerInterface;

public class RadixClob implements Clob {

    final Clob delegate;
    final DBOperationLoggerInterface logger;
    final IRadixJdbcWrapper wrapper;

    public RadixClob(Clob delegate, DBOperationLoggerInterface logger) {
        this.delegate = delegate;
        this.logger = logger;
        this.wrapper = new RadixJdbcWrapper(logger, "java.sql.Clob");
    }

    private void beforeDbOper() {
        logger.beforeDbOperation("java.sql.Clob");
    }

    private void afterDbOper() {
        logger.afterDbOperation();
    }

    @Override
    public long length() throws SQLException {
        return delegate.length();
    }

    @Override
    public String getSubString(long pos, int length) throws SQLException {
        beforeDbOper();
        try {
            return delegate.getSubString(pos, length);
        } finally {
            afterDbOper();
        }
    }
    
    public String getString() throws SQLException {
        return getSubString(1, (int)length());
    }    

    @Override
    public Reader getCharacterStream() throws SQLException {
        beforeDbOper();
        try {
            return wrapper.wrapReader(delegate.getCharacterStream());
        } finally {
            afterDbOper();
        }
    }

    @Override
    public InputStream getAsciiStream() throws SQLException {
        beforeDbOper();
        try {
            return wrapper.wrapStream(delegate.getAsciiStream());
        } finally {
            afterDbOper();
        }
    }

    @Override
    public long position(String searchstr, long start) throws SQLException {
        beforeDbOper();
        try {
            return delegate.position(searchstr, start);
        } finally {
            afterDbOper();
        }
    }

    @Override
    public long position(Clob searchstr, long start) throws SQLException {
        beforeDbOper();
        try {
            return delegate.position(searchstr, start);
        } finally {
            afterDbOper();
        }
    }

    @Override
    public int setString(long pos, String str) throws SQLException {
        beforeDbOper();
        try {
            return delegate.setString(pos, str);
        } finally {
            afterDbOper();
        }
    }

    @Override
    public int setString(long pos, String str, int offset, int len) throws SQLException {
        beforeDbOper();
        try {
            return delegate.setString(pos, str, offset, len);
        } finally {
            afterDbOper();
        }
    }

    @Override
    public OutputStream setAsciiStream(long pos) throws SQLException {
        beforeDbOper();
        try {
            return wrapper.wrapStream(delegate.setAsciiStream(pos));
        } finally {
            afterDbOper();
        }
    }

    @Override
    public Writer setCharacterStream(long pos) throws SQLException {
        beforeDbOper();
        try {
            return wrapper.wrapWriter(delegate.setCharacterStream(pos));
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
    public Reader getCharacterStream(long pos, long length) throws SQLException {
        beforeDbOper();
        try {
            return wrapper.wrapReader(delegate.getCharacterStream(pos, length));
        } finally {
            afterDbOper();
        }
    }

}
