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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import org.radixware.kernel.server.jdbc.DBOperationLoggerInterface;
import org.radixware.kernel.server.jdbc.RadixResultSet;

public class RadixJdbcWrapper implements IRadixJdbcWrapper {

    protected final DBOperationLoggerInterface logger;
    protected final String queryStr;

    public RadixJdbcWrapper(DBOperationLoggerInterface logger, String queryStr) {
        this.logger = logger;
        this.queryStr = queryStr;
    }

    @Override
    public RadixResultSet wrapResultSet(ResultSet rs) {
        return rs == null ? null : new WrappingResultSet(rs, this);
    }

    @Override
    public Blob wrapBlob(Blob blob) {
        return blob == null ? null : new RadixBlob(blob, logger);
    }

    @Override
    public Clob wrapClob(Clob clob) {
        return clob == null ? null : new RadixClob(clob, logger);
    }

    @Override
    public Object wrapObject(Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof Blob) {
            return wrapBlob((Blob) obj);
        } else if (obj instanceof Clob) {
            return wrapClob((Clob) obj);
        }
        return obj;
    }

    @Override
    public <T> T wrapObject(T obj, Class<T> type) {
        if (obj == null) {
            return obj;
        } else if (Clob.class.isAssignableFrom(type) || Blob.class.isAssignableFrom(type)) {
            return (T) wrapObject(obj);
        }
        return obj;
    }

    @Override
    public Reader wrapReader(Reader r) {
        return r == null ? null : new BufferedReader(new RadixLobReader(r, logger, queryStr));
    }

    @Override
    public Writer wrapWriter(Writer w) {
        return w == null ? null : new BufferedWriter(new RadixLobWriter(w, logger, queryStr));
    }

    @Override
    public InputStream wrapStream(InputStream s) {
        return s == null ? null : new BufferedInputStream(new RadixLobInputStream(s, logger, queryStr));
    }

    @Override
    public OutputStream wrapStream(OutputStream s) {
        return s == null ? null : new BufferedOutputStream(new RadixLobOutputStream(s, logger, queryStr));
    }
}
