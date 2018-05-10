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
import java.sql.Statement;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.dbq.OraExCodes;
import org.radixware.kernel.server.jdbc.AbstractDbQueries;
import org.radixware.kernel.server.trace.TraceProfiles;

final class UnitsDbQueries extends AbstractDbQueries {

    private final Unit unit;

    private UnitsDbQueries() {
        this(null);
    }

    UnitsDbQueries(final Unit unit) {
        this.unit = unit;
    }

    private PreparedStatement qryGetDbTimeMillis = null;
    private static final String qryGetDbTimeMillisSQL = " select RDX_UTILS.getUnixEpochMillis() millis from dual";

    private PreparedStatement qrySetDbStartedState = null;
    private static final String qrySetDbStartedStateSQL = "update rdx_unit set started = ?, selfchecktime = RDX_UTILS.unixEpochMsToDate(?), postponed = 0, selfCheckTimeMillis=? where id = ? and instanceId = ?";
    private PreparedStatement qryLockUnit = null;
    private static final String qryLockUnitSQL = "select 1 from rdx_unit where id = ? for update nowait";

    private PreparedStatement qryReadCommonOptions = null;
    private static final String qryReadCommonOptionsSQL = "select u.dbTraceProfile, u.fileTraceProfile, u.guiTraceProfile, u.aadcTestMode, nvl(u.scpName, i.scpName) effectiveScpName from rdx_unit u inner join rdx_instance i on i.id=u.instanceId  where u.id=?";

//	status	
    @Override
    public final void closeAll() {
        try {
            closeStatement(qrySetDbStartedState);
        } finally {
            qrySetDbStartedState = null;
        }
        try {
            closeStatement(qryReadCommonOptions);
        } finally {
            qryReadCommonOptions = null;
        }
        try {
            closeStatement(qryLockUnit);
        } finally {
            qryLockUnit = null;
        }
        try {
            closeStatement(qryGetDbTimeMillis);
        } finally {
            qryGetDbTimeMillis = null;
        }
    }

    private void closeStatement(final Statement st) {
        try {
            if (st != null && !st.getConnection().isClosed()) {
                st.close();
            }
        } catch (SQLException e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            unit.getTrace().put(EEventSeverity.WARNING, Messages.ERR_ON_DB_CONNECTION_CLOSE + ": \n" + exStack, Messages.MLS_ID_ERR_ON_DB_CONNECTION_CLOSE, new ArrStr(unit.getFullTitle(), exStack), unit.getEventSource(), false);
        }
    }

    final boolean setDbStartedState(final boolean bStarted, final boolean wait) throws SQLException {
        final Connection dbConnection = unit.getDbConnection();
        if (dbConnection == null) {
            return false;
        }

        if (!wait) {
            if (qryLockUnit == null || qryLockUnit.isClosed() || qryLockUnit.getConnection().isClosed()) {
                qryLockUnit = dbConnection.prepareStatement(qryLockUnitSQL);
            }
            qryLockUnit.setLong(1, unit.getId());
            try {
                try (final ResultSet rs = qryLockUnit.executeQuery()) {
                    if (!rs.next()) {
                        unitNotExists();
                    }
                }
            } catch (SQLException ex) {
                if (ex.getErrorCode() == OraExCodes.RESOURCE_BUSY) {
                    return false;
                }
                throw ex;
            }
        }

        if (qrySetDbStartedState == null || qrySetDbStartedState.isClosed() || qrySetDbStartedState.getConnection().isClosed()) {
            qrySetDbStartedState = dbConnection.prepareStatement(qrySetDbStartedStateSQL);
        }
        
        if (qryGetDbTimeMillis == null || qryGetDbTimeMillis.isClosed() || qryGetDbTimeMillis.getConnection().isClosed()) {
            qryGetDbTimeMillis = dbConnection.prepareStatement(qryGetDbTimeMillisSQL);
        }
        
        long dbTimeMillis;
        try (final ResultSet rs = qryGetDbTimeMillis.executeQuery()) {
            rs.next();
            dbTimeMillis = rs.getLong("millis");
        }
        qrySetDbStartedState.setInt(1, bStarted ? 1 : 0);
        qrySetDbStartedState.setLong(2, dbTimeMillis);
        qrySetDbStartedState.setLong(3, dbTimeMillis);
        qrySetDbStartedState.setLong(4, unit.getId());
        qrySetDbStartedState.setLong(5, unit.getInstance().getId());
        if (qrySetDbStartedState.executeUpdate() != 1) {
            unitNotExists();
        }
        dbConnection.commit();
        unit.getInstance().setLastWrittenUnitCheckTimeMillis(unit.getId(), dbTimeMillis);
        return true;
    }

    private void unitNotExists() {
        unit.requestShutdown();
        throw new IllegalStateException("Unit #" + unit.getId() + " does not exist in instance #" + unit.getInstance().getId());
    }

    final Unit.CommonOptions readCommonOptions() throws SQLException, InterruptedException {
        try {
            final Connection dbConnection = unit.getDbConnection();
            if (dbConnection != null) {
                if (qryReadCommonOptions == null || qryReadCommonOptions.isClosed() || qryReadCommonOptions.getConnection().isClosed()) {
                    qryReadCommonOptions = dbConnection.prepareStatement(qryReadCommonOptionsSQL);
                    qryReadCommonOptions.setLong(1, unit.getId());
                }
                try (final ResultSet rs = qryReadCommonOptions.executeQuery()) {
                    if (rs.next()) {
                        Boolean unitTestMode = rs.getBoolean("aadctestmode");
                        if (rs.wasNull()) {
                            unitTestMode = null;
                        }
                        return new Unit.CommonOptions(
                                new TraceProfiles(rs.getString("dbTraceProfile"), rs.getString("fileTraceProfile"), rs.getString("guiTraceProfile")),
                                rs.getString("effectiveScpName"),
                                unitTestMode);
                    } else {
                        throw new IllegalUsageError("Unknown unit #" + String.valueOf(unit.getId()));
                    }
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
        return new Unit.CommonOptions(TraceProfiles.DEFAULT, null, false);//default options
    }

    final boolean dbIAmStillAlive(boolean wait) throws SQLException {
        return setDbStartedState(true, wait);
    }

    @Override
    public void prepareAll() throws SQLException {
        prepareAll(unit.getDbConnection());
    }
}
