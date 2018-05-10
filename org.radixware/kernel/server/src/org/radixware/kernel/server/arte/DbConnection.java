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
package org.radixware.kernel.server.arte;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import java.sql.Connection;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.exceptions.ArteInitializationError;
import org.radixware.kernel.server.jdbc.RadixConnection;

public class DbConnection {

    private final Arte arte;
    private RadixConnection connection = null;

    public java.sql.Connection get() {
        return connection;
    }

    public RadixConnection getRadixConnection() {
        return connection;
    }

    DbConnection(final Arte arte) {
        this.arte = arte;
    }

    public Clob createTemporaryClob() throws SQLException {
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_LOB_TMP_CREATE);
        try {
            final Clob res = connection.createTemporaryClob();
            arte.getCache().registerTemporaryClob(res);
            return res;
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_LOB_TMP_CREATE);
        }
    }

    public Blob createTemporaryBlob() throws SQLException {
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_LOB_TMP_CREATE);
        try {
            final Blob res = connection.createTemporaryBlob();
            arte.getCache().registerTemporaryBlob(res);
            return res;
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_LOB_TMP_CREATE);
        }
    }

    public void freeTemporaryBlob(final Blob blob) {
        freeTemporaryBlobs(Collections.singleton(blob));
    }

    public void freeTemporaryClob(final Clob clob) {
        freeTemporaryClobs(Collections.singleton(clob));
    }

    public void freeTemporaryBlobs(final Collection<java.sql.Blob> blobs) {
        arte.getCache().freeTemporaryBlobs(blobs);
    }

    public void freeTemporaryClobs(final Collection<java.sql.Clob> clobs) {
        arte.getCache().freeTemporaryClobs(clobs);
    }

    public void registerTemporaryStatement(final Statement stmt) {
        arte.getCache().registerTemporaryStatement(stmt);
    }

    public void unregisterTemporaryStatement(final Statement stmt) {
        arte.getCache().unregisterTemporaryStatement(stmt);
    }

    public final void close() {
        arte.getTrace().setDbConnection(null);
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            //do nothing
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
        }
        connection = null;
    }

    final void init(final Connection c) {
        try {
            connection = (RadixConnection) c;
            if (connection == null || connection.isClosed()) {
                throw new ArteInitializationError("Can't open connection to database", null);
            }
            connection.setAutoCommit(false);
            arte.getTrace().setDbConnection(connection);
        } catch (SQLException e) {
            throw new ArteInitializationError("Can't open connection to database: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }

    void traceSqlWarnings() {
        try {
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB);
            try {
                SQLWarning sqlWarning = get().getWarnings();
                while (sqlWarning != null) {
                    arte.getTrace().put(EEventSeverity.WARNING, "ARTE cause SQL Warning: " + arte.getTrace().exceptionStackToString(sqlWarning), EEventSource.ARTE);
                    sqlWarning = sqlWarning.getNextWarning();
                }
                get().clearWarnings();
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB);
            }
        } catch (SQLException e) {
            arte.getTrace().put(EEventSeverity.WARNING, "Can't get SQL warnings : " + arte.getTrace().exceptionStackToString(e), EEventSource.ARTE_DB);
        }
    }
}
