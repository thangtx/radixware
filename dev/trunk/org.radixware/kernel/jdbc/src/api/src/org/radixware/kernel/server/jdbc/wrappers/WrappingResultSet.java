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

import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;


class WrappingResultSet extends DelegatedRadixResultSet {

    public WrappingResultSet(ResultSet delegate, IRadixJdbcWrapper lobWrapper) {
        super(delegate, lobWrapper);
    }

    @Override
    public Object getObject(int columnIndex) throws SQLException {
        return objWrapper.wrapObject(delegate.getObject(columnIndex));
    }

    @Override
    public Object getObject(String columnLabel) throws SQLException {
        return objWrapper.wrapObject(delegate.getObject(columnLabel));
    }

    @Override
    public Blob getBlob(int columnIndex) throws SQLException {
        return objWrapper.wrapBlob(delegate.getBlob(columnIndex));
    }

    @Override
    public Clob getClob(int columnIndex) throws SQLException {
        return objWrapper.wrapClob(delegate.getClob(columnIndex));
    }

    @Override
    public Blob getBlob(String columnLabel) throws SQLException {
        return objWrapper.wrapBlob(delegate.getBlob(columnLabel));
    }

    @Override
    public Clob getClob(String columnLabel) throws SQLException {
        return objWrapper.wrapClob(delegate.getClob(columnLabel));
    }

    @Override
    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
        return objWrapper.wrapObject(delegate.getObject(columnIndex, type), type);
    }

    @Override
    public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
        return objWrapper.wrapObject(delegate.getObject(columnLabel, type), type);
    }

}
