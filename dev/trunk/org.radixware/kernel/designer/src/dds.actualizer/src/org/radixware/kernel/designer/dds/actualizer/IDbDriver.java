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

package org.radixware.kernel.designer.dds.actualizer;


import java.sql.*;

public interface IDbDriver {

    IResult getColumns(String tableName) throws SQLException;

    IResult getColChecks(String tableName) throws SQLException;

    IResult getOrigValue(String value) throws SQLException;

    IResult getIndices(String tableName, String pkIndName) throws SQLException;

    IResult getIndChecks(String tableName, String pkIndName) throws SQLException;

    IResult getIndCols(String tableName, String indexName) throws SQLException;

    IResult getTriggers(String tableName) throws SQLException;

    IResult getTrgCols() throws SQLException;

    IResult getRefs(String tableNames) throws SQLException;

    IResult getRefCols(String constrName) throws SQLException;

    IResult getSequence(String sequenceName) throws SQLException;

    IResult getTable(String tableName) throws SQLException;

    IResult getPK(String tableName) throws SQLException;

    IResult getPKIndex(String pkIndName) throws SQLException;

    IResult getPKCols(String pkName) throws SQLException;

    IResult getViewQuery(String viewName) throws SQLException;

    IResult getViewOption(String viewName) throws SQLException;

}