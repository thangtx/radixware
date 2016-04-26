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

package org.radixware.kernel.server.units;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.IDbQueries;
import org.radixware.kernel.server.trace.TraceProfiles;


final class DbQueries implements IDbQueries {

    private final Unit unit;

    DbQueries(final Unit unit) {
        this.unit = unit;
    }
    private PreparedStatement qrySetDbStartedState = null;

//	status	
    @Override
    public final void closeAll() {
        try {
            if (qrySetDbStartedState != null) {
                qrySetDbStartedState.close();
            }
        } catch (SQLException e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            unit.getTrace().put(EEventSeverity.WARNING, Messages.ERR_ON_DB_CONNECTION_CLOSE + ": \n" + exStack, Messages.MLS_ID_ERR_ON_DB_CONNECTION_CLOSE, new ArrStr(unit.getFullTitle(), exStack), unit.getEventSource(), false);
        }
        try {
            if (qryReadCommonOptions != null) {
                qryReadCommonOptions.close();
            }
        } catch (SQLException e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            unit.getTrace().put(EEventSeverity.WARNING, Messages.ERR_ON_DB_CONNECTION_CLOSE + ": \n" + exStack, Messages.MLS_ID_ERR_ON_DB_CONNECTION_CLOSE, new ArrStr(unit.getFullTitle(), exStack), unit.getEventSource(), false);
        }

        qrySetDbStartedState = null;
        qryReadCommonOptions = null;
    }

    final void setDbStartedState(final boolean bStarted) throws SQLException {
        final Connection dbConnection = unit.getDbConnection();
        if (dbConnection == null) {
            return;
        }
        if (qrySetDbStartedState == null) {
            qrySetDbStartedState = dbConnection.prepareStatement("update rdx_unit set started = ?, selfchecktime = systimestamp where id = ? and instanceId = ?");
        }
        qrySetDbStartedState.setInt(1, bStarted ? 1 : 0);
        qrySetDbStartedState.setLong(2, unit.getId());
        qrySetDbStartedState.setLong(3, unit.getInstance().getId());
        if (qrySetDbStartedState.executeUpdate() != 1) {
            unit.requestShutdown();
            throw new IllegalStateException("Unit #" + unit.getId() + " does not exist in instance #" + unit.getInstance().getId());
        }
        dbConnection.commit();
    }
    private PreparedStatement qryReadCommonOptions = null;

    final Unit.CommonOptions readCommonOptions() throws SQLException, InterruptedException {
        try {
            final Connection dbConnection = unit.getDbConnection();
            if (dbConnection != null) {
                if (qryReadCommonOptions == null) {
                    qryReadCommonOptions = dbConnection.prepareStatement("select u.dbTraceProfile, u.fileTraceProfile, u.guiTraceProfile, nvl(u.scpName, i.scpName) effectiveScpName from rdx_unit u inner join rdx_instance i on i.id=u.instanceId  where u.id=?");
                    qryReadCommonOptions.setLong(1, unit.getId());
                }
                final ResultSet rs = qryReadCommonOptions.executeQuery();
                try {
                    if (rs.next()) {
                        return new Unit.CommonOptions(
                                new TraceProfiles(rs.getString("dbTraceProfile"), rs.getString("fileTraceProfile"), rs.getString("guiTraceProfile")),
                                rs.getString("effectiveScpName"));
                    } else {
                        throw new IllegalUsageError("Unknown unit #" + String.valueOf(unit.getId()));
                    }
                } finally {
                    rs.close();
                }
            }
        } catch (SQLException e) {
            throw e;
            //} catch (InterruptedException e) {
            //	throw e;
        } catch (Throwable e) {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            unit.getTrace().put(EEventSeverity.ERROR, Messages.ERR_ON_TRACE_OPTION_READING + ": \n" + exStack, Messages.MLS_ID_ERR_ON_TRACE_OPTION_READING, new ArrStr(unit.getFullTitle(), exStack), unit.getEventSource(), false);
        }
        return new Unit.CommonOptions(TraceProfiles.DEFAULT, null);//default options
    }

    final void dbIAmStillAlive() throws SQLException {
        setDbStartedState(true);
    }
}
