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
package org.radixware.kernel.server.units.netport;

import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.radixware.kernel.server.units.Messages;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.enums.EClientAuthentication;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EPortSecurityProtocol;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.CompositeInetSocketAddress;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.ValueFormatter;
import org.radixware.kernel.server.IDbQueries;
import org.radixware.kernel.server.jdbc.RadixPreparedStatement;
import org.radixware.kernel.server.trace.TraceProfiles;

final class DbQueries implements IDbQueries {

    NetPortHandlerUnit unit;

    DbQueries(final NetPortHandlerUnit unit) {
        this.unit = unit;
    }
    private PreparedStatement qryReadChannelOptions = null;
    private PreparedStatement qryReadAllChannelsOptions = null;
    private PreparedStatement qryReadUnitOptions = null;
    private PreparedStatement qrySetChannelConnectedState = null;
    private PreparedStatement qrySetCurSeanceCount = null;

    public Map<Long, NetChannelOptions> readAllChannelOptions() throws InterruptedException, SQLException {
        final Map<Long, NetChannelOptions> allOptions = new HashMap<>();
        try {
            if (qryReadAllChannelsOptions == null) {
                final Connection dbConnection = unit.getDbConnection();
                if (dbConnection == null) {
                    return null;
                }
                qryReadAllChannelsOptions = dbConnection.prepareStatement(getOptionsQueryString(false));
            }
            qryReadAllChannelsOptions.setLong(1, unit.getId());
            final ResultSet rs = qryReadAllChannelsOptions.executeQuery();
            try {
                while (rs.next()) {
                    allOptions.put(rs.getLong("id"), getOptions(rs));
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
            unit.getTrace().put(EEventSeverity.ERROR, Messages.ERR_CANT_READ_OPTIONS + ": \n" + exStack, Messages.MLS_ID_ERR_CANT_READ_OPTIONS, new ArrStr("<all channels>", exStack), unit.getEventSource(), false);
        }
        return allOptions;
    }

    final NetChannelOptions readChannelOptions(final NetChannel channel) throws SQLException, InterruptedException {
        try {
            if (qryReadChannelOptions == null) {
                final Connection dbConnection = unit.getDbConnection();
                if (dbConnection == null) {
                    return null;
                }
                qryReadChannelOptions = dbConnection.prepareStatement(getOptionsQueryString(true));
            }
            qryReadChannelOptions.setLong(1, channel.id);
            final ResultSet rs = qryReadChannelOptions.executeQuery();
            try {
                if (rs.next()) {
                    return getOptions(rs);
                } else {
                    throw new IllegalUsageError("Unknown Net Channel #" + String.valueOf(channel.id));
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
            unit.getTrace().put(EEventSeverity.ERROR, Messages.ERR_CANT_READ_OPTIONS + ": \n" + exStack, Messages.MLS_ID_ERR_CANT_READ_OPTIONS, new ArrStr(channel.getTitle(), exStack), unit.getEventSource(), false);
        }
        return null;
    }

    private NetChannelOptions getOptions(final ResultSet rs) throws SQLException {
        Long maxSesCount = rs.getLong("maxSessionCount");
        if (rs.wasNull()) {
            maxSesCount = null;
        }

        final Long recvTimeout;
        final long recvToSec = rs.getLong("recvTimeout");
        if (rs.wasNull()) {
            recvTimeout = null;
        } else {
            recvTimeout = Long.valueOf(recvToSec * 1000);
        }

        final Long sendTimeout;
        final long sendToSec = rs.getLong("sendTimeout");
        if (rs.wasNull()) {
            sendTimeout = null;
        } else {
            sendTimeout = Long.valueOf(sendToSec * 1000);
        }

        final Long keepConnectTimeout;
        final long keepToSec = rs.getLong("keepConnectTimeout");
        if (rs.wasNull()) {
            keepConnectTimeout = null;
        } else {
            keepConnectTimeout = Long.valueOf(keepToSec * 1000);
        }

        final long securityProtocol = rs.getLong("securityProtocol");
        final String serverKeyAliases = rs.getString("serverKeyAliases");
        final String clientCertAliases = rs.getString("clientCertAliases");
        final long checkClientCert = rs.getLong("checkClientCert");

        InetSocketAddress address, remoteAddress;
        if (rs.getBoolean("isListener")) {
            address = ValueFormatter.parseInetSocketAddress(rs.getString("address"));
            remoteAddress = null;
        } else {
            final CompositeInetSocketAddress addr = ValueFormatter.parseCompositeInetSocketAddress(rs.getString("address"));
            address = addr.getLocalAddress();
            remoteAddress = addr.getRemoteAddress();
        }

        return new NetChannelOptions(
                address, remoteAddress,
                maxSesCount,
                recvTimeout,
                sendTimeout,
                keepConnectTimeout,
                new TraceProfiles(rs.getString("dbTraceProfile"), rs.getString("fileTraceProfile"), rs.getString("guiTraceProfile")),
                rs.getBoolean("isListener"),
                rs.getBoolean("isConnectReadyNtfOn"),
                rs.getBoolean("isDisconnectNtfOn"),
                EPortSecurityProtocol.getForValue(securityProtocol),
                (serverKeyAliases != null ? Collections.unmodifiableList(ArrStr.fromValAsStr(serverKeyAliases)) : null),
                (clientCertAliases != null ? Collections.unmodifiableList(ArrStr.fromValAsStr(clientCertAliases)) : null),
                EClientAuthentication.getForValue(checkClientCert));

    }

    private String getOptionsQueryString(boolean individual) {
        return "select nl.id, nl.address, nl.maxSessionCount, nl.recvTimeout, nl.sendTimeout, nl.keepConnectTimeout, "
                + "nl.dbTraceProfile, nl.fileTraceProfile, nl.guiTraceProfile, nl.isListener, "
                + "nl.isConnectReadyNtfOn, nl.isDisconnectNtfOn, nl.securityProtocol, "
                + "nl.serverKeyAliases, nl.clientCertAliases, nl.checkClientCert "
                + "from rdx_netchannel nl "
                + "where "
                + (individual ? "nl.id = ?" : "nl.unitId = ?");
    }

    @Override
    public final void closeAll() {
        if (qryReadChannelOptions != null) {
            close(qryReadChannelOptions);
            qryReadChannelOptions = null;
        }
        if (qrySetCurSeanceCount != null) {
            close(qrySetCurSeanceCount);
            qrySetCurSeanceCount = null;
        }
        if (qryReadUnitOptions != null) {
            close(qryReadUnitOptions);
            qryReadUnitOptions = null;
        }
        if (qrySetChannelConnectedState != null) {
            close(qrySetChannelConnectedState);
            qrySetChannelConnectedState = null;
        }

        if (qryReadAllChannelsOptions != null) {
            close(qryReadAllChannelsOptions);
            qryReadAllChannelsOptions = null;
        }
    }

    void setCurSeanceCount(final NetChannel channel, final int activeSeanceCount) throws SQLException {
        final Connection dbConnection = unit.getDbConnection();
        if (dbConnection == null) {
            return;
        }
        if (qrySetCurSeanceCount == null) {
            qrySetCurSeanceCount = dbConnection.prepareStatement("update rdx_netchannel set curSessionCount = ? where id = ?");
            ((RadixPreparedStatement) qrySetCurSeanceCount).setExecuteBatch(100);
        }
        qrySetCurSeanceCount.setInt(1, activeSeanceCount);
        qrySetCurSeanceCount.setLong(2, channel.id);
        qrySetCurSeanceCount.executeUpdate();
    }

    public NetPortHandlerUnit.Options readUnitOptions() throws InterruptedException, SQLException {
        try {
            if (qryReadUnitOptions == null) {
                final Connection dbConnection = unit.getDbConnection();
                if (dbConnection == null) {
                    return null;
                }
                qryReadUnitOptions = dbConnection.prepareStatement(
                        "select sapId, maxAasSeancesCount from rdx_netporthandler where id = ?");
            }
            qryReadUnitOptions.setLong(1, unit.getId());
            final ResultSet rs = qryReadUnitOptions.executeQuery();
            try {
                if (rs.next()) {
                    return new NetPortHandlerUnit.Options(rs.getLong("sapId"), rs.getInt("maxAasSeancesCount"));
                } else {
                    throw new IllegalUsageError("Unknown NetPortHandler #" + String.valueOf(unit.getId()));
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

    private void close(final Statement qry) {
        try {
            qry.close();
        } catch (SQLException e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            unit.getTrace().put(EEventSeverity.WARNING, Messages.ERR_ON_DB_CONNECTION_CLOSE + ": \n" + exStack, Messages.MLS_ID_ERR_ON_DB_CONNECTION_CLOSE, new ArrStr(unit.getFullTitle(), exStack), unit.getEventSource(), false);
        }
    }
}
