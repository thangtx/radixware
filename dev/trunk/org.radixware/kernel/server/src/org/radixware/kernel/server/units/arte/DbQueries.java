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

package org.radixware.kernel.server.units.arte;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.IDbQueries;
import org.radixware.kernel.server.sap.ReadSapOptionsQuery;
import org.radixware.kernel.server.units.Messages;


final class DbQueries implements IDbQueries {

    private final ArteUnit unit;
    private ReadSapOptionsQuery readSapOptsQry = null;
    private PreparedStatement qryReadOptions = null;
    private PreparedStatement qryWriteBasicStats = null;

    DbQueries(final ArteUnit unit) {
        this.unit = unit;
    }

    void writeBasicStats() throws SQLException {
        if (unit.getDbConnection() != null) {
            if (qryWriteBasicStats == null) {
                qryWriteBasicStats = unit.getDbConnection().prepareStatement("update rdx_arteunit set avgActiveArteCount=? where id = ?");
                qryWriteBasicStats.setLong(2, unit.getId());
            }
            qryWriteBasicStats.setDouble(1, unit.getAvgActiveArteCount());
            qryWriteBasicStats.execute();
        }
    }

    final ArteUnitOptions readOptions() throws SQLException, InterruptedException {
        try {
            if (qryReadOptions == null) {
                final Connection dbConnection = unit.getDbConnection();
                if (dbConnection == null) {
                    return null;
                }
                qryReadOptions = dbConnection.prepareStatement(
                        "select u.higharteinstcount, u.sapid, u.threadPriority "
                        + "from rdx_arteunit u "
                        + "where u.id = ?");
            }
            final ArteUnitOptions options = new ArteUnitOptions();
            qryReadOptions.setLong(1, unit.getId());
            final ResultSet rs = qryReadOptions.executeQuery();
            try {
                if (rs.next()) {
                    options.sapId = rs.getInt("sapid");
                    options.highArteInstCount = rs.getInt("higharteinstcount");
                    options.defaultPriority = rs.getInt("threadPriority");
                    if (readSapOptsQry == null) {
                        readSapOptsQry = new ReadSapOptionsQuery(unit.getDbConnection());
                    }
                    options.sapOptions = readSapOptsQry.readOptions(options.sapId);
                } else {
                    throw new IllegalUsageError("Unknown ARTE unit #" + String.valueOf(unit.getId()));
                }
            } finally {
                rs.close();
            }
            return options;
            //} catch (SQLException e) {
            //	traceError(Messages.ERR_IN_DB_QRY.toString() +": \n" + ExceptionTextFormatter.exceptionStackToString(e));
        } catch (Throwable e) {
            if (e instanceof InterruptedException || Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            if (e instanceof SQLException) {
                throw (SQLException) e;
            }
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            unit.getTrace().put(EEventSeverity.ERROR, Messages.ERR_CANT_READ_OPTIONS + ": \n" + exStack, Messages.MLS_ID_ERR_CANT_READ_OPTIONS, new ArrStr(unit.getFullTitle(), exStack), unit.getEventSource(), false);
        }
        return null;

    }

    @Override
    public final void closeAll() {
        closeQry(qryReadOptions);
        qryReadOptions = null;
        closeQry(qryWriteBasicStats);
        qryWriteBasicStats = null;
        if (readSapOptsQry != null) {
            readSapOptsQry.close();
            readSapOptsQry = null;
        }
    }

    private void closeQry(final PreparedStatement qry) {
        if (qry != null) {
            try {
                qry.close();
            } catch (SQLException e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                unit.getTrace().put(EEventSeverity.WARNING, Messages.ERR_ON_DB_CONNECTION_CLOSE + ": \n" + exStack, Messages.MLS_ID_ERR_ON_DB_CONNECTION_CLOSE, new ArrStr(unit.getFullTitle(), exStack), unit.getEventSource(), false);
            }
        }
    }
}