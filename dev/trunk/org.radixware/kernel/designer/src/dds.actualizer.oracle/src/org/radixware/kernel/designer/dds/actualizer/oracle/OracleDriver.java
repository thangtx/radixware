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


import java.sql.*;
import org.radixware.kernel.designer.dds.actualizer.IResult;

public class OracleDriver implements org.radixware.kernel.designer.dds.actualizer.IDbDriver {

    private final Connection connection;

    public OracleDriver(Connection connection) throws SQLException {
        this.connection = connection;
    }

    @Override
    public IResult getColumns(String tableName) throws SQLException {
        return new OracleResult(connection, "select COLUMN_NAME, DATA_TYPE, DATA_LENGTH, DATA_PRECISION, DATA_SCALE, DATA_DEFAULT, NULLABLE, CHAR_USED, CHAR_LENGTH, VIRTUAL_COLUMN from USER_TAB_COLS where TABLE_NAME = '" + tableName + "' and HIDDEN_COLUMN = 'NO'");
    }

    @Override
    public IResult getColChecks(String tableName) throws SQLException {
        return new OracleResult(connection, "select C1.COLUMN_NAME, C1.CONSTRAINT_NAME, C2.SEARCH_CONDITION from USER_CONS_COLUMNS C1, USER_CONSTRAINTS C2 where C1.TABLE_NAME = '" + tableName + "' and C2.TABLE_NAME = '" + tableName + "' and C2.CONSTRAINT_TYPE = 'C' and C2.GENERATED = 'USER NAME' and C2.CONSTRAINT_NAME = C1.CONSTRAINT_NAME");
    }

    @Override
    public IResult getOrigValue(String value) throws SQLException {
        return new OracleResult(connection, "select " + value + " VALUE from DUAL");
    }

    @Override
    public IResult getIndices(String tableName, String pkIndName) throws SQLException {
        return new OracleResult(connection, "select INDEX_NAME, TABLESPACE_NAME, UNIQUENESS, VISIBILITY, INDEX_TYPE, PARTITIONED, LOGGING from USER_INDEXES where TABLE_NAME = '" + tableName + "' and INDEX_NAME != '" + pkIndName + "' and GENERATED = 'N'");
    }

    @Override
    public IResult getIndChecks(String tableName, String pkIndName) throws SQLException {
        return new OracleResult(connection, "select INDEX_NAME, CONSTRAINT_NAME, RELY, STATUS, VALIDATED, DEFERRABLE, DEFERRED from USER_CONSTRAINTS where TABLE_NAME = '" + tableName + "' and INDEX_NAME != '" + pkIndName + "' and GENERATED = 'USER NAME'");
    }

    @Override
    public IResult getIndCols(String tableName, String indexName) throws SQLException {
        return new OracleResult(connection, "select C1.COLUMN_NAME, C1.DESCEND, C2.HIDDEN_COLUMN, C2.DATA_DEFAULT from USER_IND_COLUMNS C1, USER_TAB_COLS C2 where C1.INDEX_NAME = '" + indexName + "' and C2.TABLE_NAME = '" + tableName + "' and C1.COLUMN_NAME = C2.COLUMN_NAME order by C1.COLUMN_POSITION");
    }

    @Override
    public IResult getTriggers(String tableName) throws SQLException {
        return new OracleResult(connection, "select TRIGGER_NAME, TRIGGER_TYPE, TRIGGER_BODY, TRIGGERING_EVENT from USER_TRIGGERS where TABLE_NAME = '" + tableName + "'");
    }

    @Override
    public IResult getTrgCols() throws SQLException {
        return new OracleResult(connection, "select TRIGGER_NAME, COLUMN_NAME from USER_TRIGGER_COLS where COLUMN_LIST='YES'");
    }

    @Override
    public IResult getRefs(String tableNames) throws SQLException {
        return new OracleResult(connection, "select C1.CONSTRAINT_NAME, C1.R_CONSTRAINT_NAME, C1.DELETE_RULE, C1.RELY, C1.STATUS, C1.VALIDATED, C1.DEFERRABLE, C1.DEFERRED, C1.TABLE_NAME CHILD_TABLE, C2.TABLE_NAME PARENT_TABLE, C2.INDEX_NAME PARENT_INDEX from USER_CONSTRAINTS C1, USER_CONSTRAINTS C2 where C1.CONSTRAINT_TYPE = 'R' and C1.GENERATED = 'USER NAME' and C2.GENERATED = 'USER NAME' and C2.CONSTRAINT_NAME = C1.R_CONSTRAINT_NAME and (C1.TABLE_NAME in (" + tableNames + ") or C2.TABLE_NAME in (" + tableNames + "))");
    }

    @Override
    public IResult getRefCols(String constrName) throws SQLException {
        return new OracleResult(connection, "select COLUMN_NAME, POSITION from USER_CONS_COLUMNS where CONSTRAINT_NAME = '" + constrName + "' order by POSITION");
    }

    @Override
    public IResult getSequence(String sequenceName) throws SQLException {
        return new OracleResult(connection, "select CACHE_SIZE, INCREMENT_BY, MAX_VALUE, MIN_VALUE, CYCLE_FLAG, ORDER_FLAG from USER_SEQUENCES where SEQUENCE_NAME = '" + sequenceName + "'");
    }

    @Override
    public IResult getTable(String tableName) throws SQLException {
        return new OracleResult(connection, "select TABLESPACE_NAME, TEMPORARY, DURATION from USER_TABLES where TABLE_NAME = '" + tableName + "'");
    }

    @Override
    public IResult getPK(String tableName) throws SQLException {
        return new OracleResult(connection, "select INDEX_NAME, CONSTRAINT_NAME, RELY, STATUS, VALIDATED, DEFERRABLE, DEFERRED from USER_CONSTRAINTS where TABLE_NAME = '" + tableName + "' and CONSTRAINT_TYPE = 'P' and GENERATED = 'USER NAME'");
    }

    @Override
    public IResult getPKIndex(String pkIndName) throws SQLException {
        return new OracleResult(connection, "select TABLESPACE_NAME, UNIQUENESS, VISIBILITY, INDEX_TYPE, PARTITIONED, LOGGING from USER_INDEXES where INDEX_NAME = '" + pkIndName + "'");
    }

    @Override
    public IResult getPKCols(String pkName) throws SQLException {
        return new OracleResult(connection, "select COLUMN_NAME, POSITION from USER_CONS_COLUMNS where CONSTRAINT_NAME = '" + pkName + "' order by POSITION");
    }

    @Override
    public IResult getViewQuery(String viewName) throws SQLException {
        return new OracleResult(connection, "select TEXT from USER_VIEWS where VIEW_NAME = '" + viewName + "'");
    }

    @Override
    public IResult getViewOption(String viewName) throws SQLException {
        return new OracleResult(connection, "select CONSTRAINT_TYPE from USER_CONSTRAINTS where TABLE_NAME = '" + viewName + "'");
    }
}
