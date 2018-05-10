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

package org.radixware.kernel.server.units.nethub;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.jdbc.AbstractDbQueries;
import org.radixware.kernel.server.units.Messages;

/**
 * NetHubUnit's database queries
 *
 *
 */
final class NetHubDbQueries extends AbstractDbQueries {

    NetHubUnit unit;

    private NetHubDbQueries(){
        this(null);
    }
    
    NetHubDbQueries(final NetHubUnit unit) {
        this.unit = unit;
    }
    private PreparedStatement qryReadOptions = null;
    private static final String qryReadOptionsSQL = "select inSeanceCnt, outSeanceCnt, echoTestPeriod, reconnectNoEchoCnt, "
                                                  + "extPortIsServer, extPortAddress, extPortFrame, outTimeout, toProcessStart, toProcessStop, toProcessConnect, toProcessDisconnect, "
                                                  + "toProcessDuplicatedRq, toProcessUncorrelatedRs, sapId "
                                                  + "from rdx_nethub where id = ?";

    final NetHubUnit.Options readOptions() throws SQLException, InterruptedException {
        try {
            if (qryReadOptions == null) {
                qryReadOptions = unit.getDbConnection().prepareStatement(qryReadOptionsSQL);
            }
            qryReadOptions.setLong(1, unit.getId());
            final ResultSet rs = qryReadOptions.executeQuery();
            try {
                if (rs.next()) {
                    final Long echoTestPeriod;
                    final long echoTestPeriodSec = rs.getLong("echoTestPeriod");
                    if (rs.wasNull()) {
                        echoTestPeriod = null;
                    } else {
                        echoTestPeriod = Long.valueOf(echoTestPeriodSec * 1000);
                    }

                    final Long reconnectNoEchoCnt;
                    final long reconnectNoEchoCntl = rs.getLong("reconnectNoEchoCnt");
                    if (rs.wasNull()) {
                        reconnectNoEchoCnt = null;
                    } else {
                        reconnectNoEchoCnt = Long.valueOf(reconnectNoEchoCntl);
                    }

                    final Long recvTimeout;
                    final long recvToSec = rs.getLong("outTimeout");
                    if (rs.wasNull()) {
                        recvTimeout = null;
                    } else {
                        recvTimeout = Long.valueOf(recvToSec * 1000);
                    }

                    return new NetHubUnit.Options(
                            rs.getLong("inSeanceCnt"),
                            rs.getLong("outSeanceCnt"),
                            echoTestPeriod,
                            reconnectNoEchoCnt,
                            rs.getBoolean("toProcessStart"),
                            rs.getBoolean("toProcessStop"),
                            rs.getBoolean("toProcessConnect"),
                            rs.getBoolean("toProcessDisconnect"),
                            rs.getBoolean("toProcessDuplicatedRq"),
                            rs.getBoolean("toProcessUncorrelatedRs"),
                            rs.getLong("sapId"),
                            new ExtPort.Options(
                            rs.getString("extPortAddress"),
                            rs.getBoolean("extPortIsServer"),
                            rs.getString("extPortFrame"),
                            recvTimeout));
                } else {
                    throw new IllegalUsageError("Unknown NetHub unit #" + String.valueOf(unit.getId()));
                }
            } finally {
                rs.close();
            }
        } catch (Throwable e) {
            if (e instanceof InterruptedException || Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            if (e instanceof SQLException) {
                throw (SQLException) e;
            }
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            unit.getTrace().put(EEventSeverity.ERROR, Messages.ERR_CANT_READ_OPTIONS + ": \n" + exStack, Messages.MLS_ID_ERR_CANT_READ_OPTIONS, new ArrStr(unit.getTitle(), exStack), unit.getEventSource(), false);
        }
        return null;

    }

    @Override
    public void prepareAll() throws SQLException {
        prepareAll(unit.getDbConnection());
    }    
    
    // close
    public final void closeAll() {
        if (qryReadOptions != null) {
            try {
                qryReadOptions.close();
            } catch (SQLException e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                unit.getTrace().put(EEventSeverity.WARNING, Messages.ERR_ON_DB_CONNECTION_CLOSE + ": \n" + exStack, Messages.MLS_ID_ERR_ON_DB_CONNECTION_CLOSE, new ArrStr(unit.getTitle(), exStack), unit.getEventSource(), false);
            }
            qryReadOptions = null;
        }
    }

    final void setConnected(boolean connected) {
        PreparedStatement qrySetConnected = null;
        PreparedStatement qryResetOutSessionCount = null;
        try {
            qrySetConnected = unit.getDbConnection().prepareStatement(
                    "DECLARE PRAGMA AUTONOMOUS_TRANSACTION;"
                    + "BEGIN UPDATE RDX_NETHUB SET CONNECTED=? WHERE ID=?;"
                    + "COMMIT; END;");

            qrySetConnected.setBoolean(1, connected);
            qrySetConnected.setLong(2, unit.getId());
            qrySetConnected.executeUpdate();
            if (connected) {
                qryResetOutSessionCount = unit.getDbConnection().prepareStatement(
                        "DECLARE PRAGMA AUTONOMOUS_TRANSACTION;"
                        + "BEGIN UPDATE RDX_NETHUB SET CUROUTSESSIONCNT=0 WHERE ID=?;"
                        + "COMMIT; END;");

                qryResetOutSessionCount.setLong(1, unit.getId());
                qryResetOutSessionCount.executeUpdate();
            }
        } catch (Throwable e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            unit.getTrace().put(EEventSeverity.ERROR, NetHubMessages.ERR_ON_SOCKET_IO + ": \n" + exStack, null, new ArrStr(unit.getTitle(), exStack), unit.getEventSource(), false);
        } finally {
            if (qrySetConnected != null) {
                try {
                    qrySetConnected.close();
                } catch (SQLException ex) {
                    // do nothing
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
            if (qryResetOutSessionCount != null) {
                try {
                    qryResetOutSessionCount.close();
                } catch (SQLException ex) {
                    // do nothing
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
        }
    }
}