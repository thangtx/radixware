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

package org.radixware.kernel.designer.dds.actualizer.oracle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.radixware.kernel.designer.dds.actualizer.IResult;


class OracleResult implements IResult {

    private final ResultSet resultSet;
    private final Statement stmt;

    public OracleResult(Connection connection, String query) throws SQLException {
        stmt = connection.createStatement();
        resultSet = stmt.executeQuery(query);
    }

    @Override
    public void close() throws SQLException {
        resultSet.close();
        stmt.close();
    }

    @Override
    public int getInt(String name) throws SQLException {
        return resultSet.getInt(name);
    }

    @Override
    public String getString(String name) throws SQLException {
        return resultSet.getString(name);
    }

    @Override
    public boolean next() throws SQLException {
        return resultSet.next();
    }
}
