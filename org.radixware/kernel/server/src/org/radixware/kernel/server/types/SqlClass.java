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

package org.radixware.kernel.server.types;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.OraclePreparedStatement;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.arte.AdsClassLoader;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.jdbc.RadixPreparedStatement;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;

/**
 * Base class for SqlClasses (Cursor, Statement, Report, PL/SQL Procedure)
 * runtime.
 *
 */
public abstract class SqlClass {

    public interface SqlStatement {

        public void mth_stmt_execute();
    }

    public interface PlSqlProcedure {
    }
    private final Arte arte;

    protected SqlClass(final Arte arte) { // for unit tests
        this.arte = arte != null ? arte : ((AdsClassLoader) getClass().getClassLoader()).getArte();
    }

    protected SqlClass() {
        this(null);
    }

    // Published methods
    public abstract RadClassDef getRadMeta();

    public final Arte getArte() {
        return arte;
    }

    protected void executeQuery(PreparedStatement statement) throws SQLException {
        String sec = org.radixware.kernel.common.enums.ETimingSection.RDX_SQLCLASS_QRYEXEC.getValue() + ":" + getRadMeta().getName();
        arte.getProfiler().enterTimingSection(sec);
        try {
            execStatement(statement);
        } finally {
            arte.getProfiler().leaveTimingSection(sec);
        }
    }

    protected void execStatement(PreparedStatement statement) throws SQLException {
        statement.execute();
    }

    public static final class PreparedStatementsCache {

        private int batchSize = 1;

        private static class Item {

            final String sql;
            final Statement statement;
            boolean used = true;
            private int batchSize = 1;

            protected Item(final String sql, final Statement statement) {
                this.sql = sql;
                this.statement = statement;
            }

            public void setBatchSize(final int batchSize) throws SQLException {
                if (this.batchSize != batchSize) {
                    ((OraclePreparedStatement) (statement)).setExecuteBatch(batchSize);
                    this.batchSize = batchSize;
                }
            }
        }
        private static final int MAX_SIZE = 100;
        private final EClassType sqlClassType;
        private final int cacheSize;
        private final boolean isUniDirectCursor;
        private final boolean isReadOnlyCursor;
        private final List<Item> items = new ArrayList<Item>(); // usually contains only one item
        private final boolean wantCloseOnFree;

        public PreparedStatementsCache(final EClassType sqlClassType, final int cacheSize) {
            this(sqlClassType, cacheSize, true, true);
        }

        public PreparedStatementsCache(final EClassType sqlClassType, final int cacheSize, final boolean isUniDirectCursor, final boolean isReadOnlyCursor) {
            this.sqlClassType = sqlClassType;
            this.cacheSize = cacheSize;
            this.isUniDirectCursor = isUniDirectCursor;
            this.isReadOnlyCursor = isReadOnlyCursor;
            final Instance inst = Instance.get();
            if (inst != null) {
                wantCloseOnFree = inst.isOraImplicitCacheEnabled();
            } else {
                wantCloseOnFree = false;
            }
        }

        private void cleanup(final Arte arte, final boolean forceRemove) {
            try {
                for (int i = items.size() - 1; i >= 0; i--) {
                    final Item item = items.get(i);
                    if (item.statement.isClosed()) {
                        items.remove(i);
                    }
                }

                for (int i = items.size() - 1; i >= 0; i--) {
                    final Item item = items.get(i);
                    if (!item.used) {
                        boolean remove = forceRemove
                                || items.size() > cacheSize
                                || (item.batchSize <= 1 && wantCloseOnFree);
                        if (remove) {
                            if (!item.statement.isClosed()) {
                                item.statement.close();
                            }
                            items.remove(i);
                        }
                    }
                }
            } catch (java.sql.SQLException e) {
                arte.getTrace().put(EEventSeverity.ERROR, "Can't close statement: " + arte.getTrace().exceptionStackToString(e), EEventSource.ARTE_DB);
            }
        }

        public Statement findOrCreateStatementBySql(final Arte arte, final String sql) throws SQLException {
            cleanup(arte, false);

            for (Item item : items) {
                if (!item.used && item.sql != null && item.sql.equals(sql)) {
                    item.used = true; // by BAO 14.07.2010
                    item.setBatchSize(batchSize);
                    return item.statement;
                }
            }

            if (items.size() >= MAX_SIZE && items.size() >= cacheSize) {
                throw new SQLException("Too many opened statements for SQL class");
            }

            final Statement statement;
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_SQLCLASS_QRYPREPARE);
            try {
                if (this.sqlClassType == EClassType.SQL_PROCEDURE) {
                    statement = arte.getDbConnection().get().prepareCall(sql);
                } else if (this.sqlClassType == EClassType.SQL_CURSOR || this.sqlClassType == EClassType.REPORT) {
                    statement = arte.getDbConnection().get().prepareStatement(
                            sql,
                            (this.isUniDirectCursor ? java.sql.ResultSet.TYPE_FORWARD_ONLY : java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE),
                            (this.isReadOnlyCursor ? java.sql.ResultSet.CONCUR_READ_ONLY : java.sql.ResultSet.CONCUR_UPDATABLE));
                    if (isReadOnlyCursor) {
                        ((RadixPreparedStatement) statement).setReadOnly(isReadOnlyCursor);
                    }
                } else {
                    statement = arte.getDbConnection().get().prepareStatement(sql);
                }
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_SQLCLASS_QRYPREPARE);
            }

            final Item item = new Item(sql, statement);
            item.setBatchSize(batchSize);
            items.add(item);
            return statement;
        }

        public void close(final Arte arte, final Statement statement) {
            for (Item item : items) {
                if (item.statement == statement) {
                    item.used = false;
                    break;
                }
            }
            cleanup(arte, false);
        }

        private void ensureStatement() {
            if (sqlClassType != EClassType.SQL_STATEMENT) {
                throw new UnsupportedOperationException("Batching is allowed only for Sql Statements");
            }
        }

        public void setBatchSize(final int size) {
            ensureStatement();
            if (size < 1) {
                throw new IllegalArgumentException("Batch size can not be less than 1");
            }
            batchSize = size;
        }

        public void sendBatch(final Arte arte) throws SQLException {
            ensureStatement();
            cleanup(arte, false);
            for (Item item : items) {
                if (item.batchSize > 1) {
                    arte.getProfiler().enterTimingSection(ETimingSection.RDX_SQLCLASS_BATCHEXEC);
                    try {
                        ((OraclePreparedStatement) item.statement).sendBatch();
                    } finally {
                        arte.getProfiler().leaveTimingSection(ETimingSection.RDX_SQLCLASS_BATCHEXEC);
                    }
                    if (wantCloseOnFree) {
                        try {
                            item.setBatchSize(1);
                        } catch (SQLException ex) {
                            arte.getTrace().put(EEventSeverity.WARNING, "Unable to reset batch size to 1: " + ExceptionTextFormatter.throwableToString(ex), EEventSource.ARTE);
                            try {
                                item.statement.close();
                            } catch (SQLException ex2) {
                                //ignore
                                Logger.getLogger(getClass().getName()).log(Level.FINE, ex2.getMessage(), ex2);
                            }
                        }
                    }
                }
            }
            cleanup(arte, false);
        }

        public void clearCache(final Arte arte) {
            cleanup(arte, true);
        }
    }
}
