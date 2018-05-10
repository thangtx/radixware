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

package org.radixware.kernel.server.units.jms;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EJmsMessageFormat;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.jdbc.IDbQueries;
import org.radixware.kernel.server.jdbc.AbstractDbQueries;
import org.radixware.kernel.server.units.Messages;

/**
 * JmsHandlerUnit's database queries  
 * 
 *
 */
final class JmsDbQueries extends AbstractDbQueries {

    final private JmsHandlerUnit unit;

    private JmsDbQueries() {
        this(null);
    }
    
    JmsDbQueries(final JmsHandlerUnit unit) {
        this.unit = unit;
    }
    private PreparedStatement qryReadOptions = null;
    private static final String qryReadOptionsSQL = "select jmsMessFormat, jmsConnectProps, jmsMessProps, jmsLogin, "
                                                  + "jmsPassword, msRqQueueName, msRsQueueName, inSeanceCnt, outSeanceCnt, rsTimeout, isClient, sapId "
                                                  + "from rdx_jmshandler where id = ?";

    final JmsHandlerUnit.Options readOptions() throws SQLException, InterruptedException {
        try {
            if (qryReadOptions == null) {
                qryReadOptions = unit.getDbConnection().prepareStatement(qryReadOptionsSQL);
            }
            qryReadOptions.setLong(1, unit.getId());
            
            try(final ResultSet rs = qryReadOptions.executeQuery()) {
                if (rs.next()) {
                    final Long jmsMessFormat;
                    final long jmsMessFormatl = rs.getLong("jmsMessFormat");
                    if (rs.wasNull()) {
                        jmsMessFormat = null;
                    } else {
                        jmsMessFormat = Long.valueOf(jmsMessFormatl);
                    }

                    final Long inSeanceCnt;
                    final long inSeanceCntl = rs.getLong("inSeanceCnt");
                    if (rs.wasNull()) {
                        inSeanceCnt = null;
                    } else {
                        inSeanceCnt = Long.valueOf(inSeanceCntl);
                    }

                    final Long outSeanceCnt;
                    final long outSeanceCntl = rs.getLong("outSeanceCnt");
                    if (rs.wasNull()) {
                        outSeanceCnt = null;
                    } else {
                        outSeanceCnt = Long.valueOf(outSeanceCntl);
                    }

                    final Long rsTimeout;
                    final long rsTimeoutl = rs.getLong("rsTimeout");
                    if (rs.wasNull()) {
                        rsTimeout = null;
                    } else {
                        rsTimeout = Long.valueOf(rsTimeoutl);
                    }

                    return new JmsHandlerUnit.Options(
                            EJmsMessageFormat.getForValue(jmsMessFormat),
                            rs.getClob("jmsConnectProps"),
                            rs.getClob("jmsMessProps"),
                            rs.getString("jmsLogin"),
                            rs.getString("jmsPassword"),
                            rs.getString("msRqQueueName"),
                            rs.getString("msRsQueueName"),
                            rs.getBoolean("isClient"),
                            inSeanceCnt,
                            outSeanceCnt,
                            rsTimeout,
                            rs.getLong("sapId"));
                } else {
                    throw new IllegalUsageError("Unknown JmsHanlder unit #" + String.valueOf(unit.getId()));
                }
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

    @Override
    public void prepareAll() throws SQLException {
        prepareAll(unit.getDbConnection());
    }
}
