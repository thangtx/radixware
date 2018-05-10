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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.ELinkLevelProtocolKind;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.units.Messages;

final class Seances {

    private static final long WRITE_SEANCES_COUNT_IN_DB_MIN_INTERVAL_MILLISECONDS = 5000;
    private final NetPortHandlerUnit unit;
    private final ConcurrentHashMap<String, Seance> seancesBySid = new ConcurrentHashMap<String, Seance>();
    private long lastDbModTime = 0;

    Seances(final NetPortHandlerUnit unit) {
        this.unit = unit;
    }

    void closeAll() {
        for (Map.Entry<String, Seance> entry : new HashSet<>(seancesBySid.entrySet())) {
            try {
                entry.getValue().close(Seance.ECloseMode.GENERATE_ON_DISCONNECT_EVENT);
            } catch (Throwable t) {
                unit.getTrace().put(EEventSeverity.ERROR, "Error while closing seance '" + entry.getKey() + "': " + ExceptionTextFormatter.throwableToString(t), EEventSource.NET_PORT_HANDLER);
            }
        }
        if (!seancesBySid.isEmpty()) {
            unit.getTrace().put(EEventSeverity.ERROR, "Not all seances have been closed", EEventSource.NET_PORT_HANDLER);
        }
    }

    Seance openNew(final NetChannel netChannel, final SocketChannel channel) throws IOException, SQLException {
        if (netChannel.getOptions().maxSessionCount != null) {
            if (getActiveSeanceCount(netChannel) >= netChannel.getOptions().maxSessionCount.longValue()) {
                netChannel.getTrace().put(EEventSeverity.ERROR, NetPortHandlerMessages.ERR_MAX_SESSION_COUNT_EXCEEDED + ": " + netChannel.getOptions().maxSessionCount.toString(), NetPortHandlerMessages.MLS_ID_ERR_MAX_SESSION_COUNT_EXCEEDED, new ArrStr(netChannel.getOptions().maxSessionCount.toString()), EEventSource.UNIT_PORT.getValue(), false);
                return null;
            }
        }
        final Seance s = netChannel.options.linkLevelProtocolKind == ELinkLevelProtocolKind.POS2 ? new Pos2Seance(unit, netChannel, channel) : new FramerSeance(unit, netChannel, channel);
        if (netChannel instanceof NetClient) {
            s.connecting();
        } else {
            s.generateSid(((InetSocketAddress) channel.getRemoteAddress()).getHostString(), ((InetSocketAddress) channel.getRemoteAddress()).getPort());
            s.open();
        }
        return s;
    }

    void registerSeance(final Seance seance) {
        seancesBySid.put(seance.sid, seance);
    }

    void unregisterSeance(final Seance seance) {
        seancesBySid.remove(seance.sid);
        seance.channel.seanceUnregistered(seance);
    }

    Seance findSeance(final String sid) {
        return seancesBySid.get(sid);
    }

    void setCurSeancesCountInDbForAll() {
        setCurSeancesCountInDb(true);
    }
    
    void setCurSeancesCountInDbForChanged() {
        setCurSeancesCountInDb(false);
    }

    public void maintenance() {
        for (Seance s : new ArrayList<>(seancesBySid.values())) {
            try {
                s.maintenance();
            } catch (Exception ex) {
                unit.getTrace().put(EEventSeverity.ERROR, "Error on maintenance seance " + s.getConnectionDesc() + ": " + ExceptionTextFormatter.throwableToString(ex), EEventSource.NET_PORT_HANDLER);
            }
        }
    }

    private void setCurSeancesCountInDb(final boolean forced) {
        if (unit.getNetPortHandlerDbQueries() == null || unit.getDbConnection() == null) {
            return;
        }
        lastDbModTime = System.currentTimeMillis();
        final List<NetPortDbQueries.ChannelStateInfo> infos = new ArrayList<>(unit.getChannels().size());
        for (NetChannel channel : unit.getChannels()) {
            if (forced || channel.isNeedUpdateDbStats()) {
                final int sessionCount = channel.isStarted() ? getActiveSeanceCount(channel) : -1;
                final int busyCount = (channel.getOptions() != null && channel.getOptions().isBusySessionCountOn) ? channel.getBusySeancesCount() : -1;
                infos.add(new NetPortDbQueries.ChannelStateInfo(channel.id, sessionCount, busyCount));
                channel.setNeedUpdateDbStats(false);
            }
        }
        try {
            unit.getNetPortHandlerDbQueries().updateChannelsState(infos);
            unit.getDbConnection().commit();
        } catch (SQLException ex) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(ex);
            unit.getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + ": " + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(exStack), EEventSource.UNIT_PORT.getValue(), false);
        }
    }

    void trySetCurSeancesCountInDb() {
        if (System.currentTimeMillis() - lastDbModTime > WRITE_SEANCES_COUNT_IN_DB_MIN_INTERVAL_MILLISECONDS) {
            setCurSeancesCountInDb(false);
        }
    }

    public int getActiveSeanceCount(final NetChannel channel) {
        int count = 0;
        for (Seance s : seancesBySid.values()) {
            if (s.channel == channel && s.state != Seance.State.NEW && s.state != Seance.State.CONNECTING && s.state != Seance.State.CLOSED) {
                count++;
            }
        }
        return count;
    }

    public Seance getActiveSeance(final NetClient channel) {
        for (Seance s : seancesBySid.values()) {
            if (s.channel == channel) {
                return s;
            }
        }
        return null;
    }

    public List<Seance> getActiveSeances(final NetChannel channel) {
        final List<Seance> result = new ArrayList<>();
        for (Seance s : seancesBySid.values()) {
            if (s.channel == channel) {
                result.add(s);
            }
        }
        return result;
    }
}
