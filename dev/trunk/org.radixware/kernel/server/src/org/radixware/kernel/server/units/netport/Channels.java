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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.ELinkLevelProtocolKind;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.SrvRunParams;
import org.radixware.kernel.server.units.Messages;
import org.radixware.kernel.server.units.ServerItemView;

final class Channels extends CopyOnWriteArrayList<NetChannel> {

    private final NetPortHandlerUnit unit;

    Channels(final NetPortHandlerUnit unit) {
        this.unit = unit;
    }

    public void load() {
        try {
            final Connection dbConnection = unit.getDbConnection();
            if (dbConnection == null) {
                return;
            }
            try (PreparedStatement qry = dbConnection.prepareStatement(
                    "select id, title, linkLevelProtocolKind, requestframe, responseframe, isListener from rdx_netchannel where unitid = ? and use = 1 order by id")) {
                qry.setLong(1, unit.getId());
                final ResultSet rs = qry.executeQuery();
                try {
                    while (rs.next()) {
                        if (rs.getBoolean("isListener")) {
                            add(new NetListener(
                                    unit,
                                    rs.getLong("id"),
                                    rs.getString("title"),
                                    ELinkLevelProtocolKind.getForValue(Long.valueOf(rs.getLong("linkLevelProtocolKind"))),
                                    rs.getString("requestframe"),
                                    rs.getString("responseframe")));
                        } else {
                            add(new NetClient(
                                    unit,
                                    rs.getLong("id"),
                                    rs.getString("title"),
                                    ELinkLevelProtocolKind.getForValue(Long.valueOf(rs.getLong("linkLevelProtocolKind"))),
                                    rs.getString("requestframe"),
                                    rs.getString("responseframe")));
                        }

                    }
                } finally {
                    rs.close();
                }
            }
        } catch (SQLException ex) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(ex);
            unit.getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(exStack), unit.getEventSource(), false);
        }
    }

    public boolean start() throws InterruptedException {
        boolean isAtLeastOneStarted = false;
        for (NetChannel l : this) {
            try {
                if (l.start()) {
                    isAtLeastOneStarted = true;
                    unit.getTrace().put(EEventSeverity.EVENT, NetPortHandlerMessages.CHANNEL_STARTED + " \"" + l.getTitle() + "\"", NetPortHandlerMessages.MLS_ID_CHANNEL_STARTED, new ArrStr(l.getTitle(), l.getOptions().toString()), EEventSource.NET_PORT_HANDLER, false);
                }
            } catch (Throwable e) {
                if (e instanceof InterruptedException || Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                unit.getTrace().put(EEventSeverity.ERROR, NetPortHandlerMessages.ERR_ON_CHANNEL_START + " \"" + l.getTitle() + "\": \n" + exStack, NetPortHandlerMessages.MLS_ID_ERR_ON_CHANNEL_START, new ArrStr(l.getTitle(), exStack), EEventSource.NET_PORT_HANDLER, false);
            }
        }
        return isEmpty() || isAtLeastOneStarted;
    }

    public void setDbConnection(final Connection db) {
        for (NetChannel l : this) {
            l.setDbConnection(db);
        }
    }

    public void stop() {
        for (NetChannel l : this) {
            try {
                l.stop();
                //} catch (InterruptedException e) {
                //continue stopping
            } catch (Throwable e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                unit.getTrace().put(EEventSeverity.ERROR, NetPortHandlerMessages.ERR_ON_CHANNEL_STOP + " \"" + l.getTitle() + "\": \n" + exStack, NetPortHandlerMessages.MLS_ID_ERR_ON_CHANNEL_STOP, new ArrStr(l.getTitle(), exStack), EEventSource.NET_PORT_HANDLER, false);
            }
        }
    }

    public NetChannel findChannel(long id, boolean reread) {
        final NetChannel l = findChannel(id);
        if (l != null || !reread) {
            return l;
        }

        try {
            reread();
        } catch (Throwable e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            unit.getTrace().put(EEventSeverity.ERROR, NetPortHandlerMessages.ERR_ON_CHANNELS_REREAD + ": \n" + exStack, NetPortHandlerMessages.MLS_ID_ERR_ON_CHANNELS_REREAD, new ArrStr(exStack), EEventSource.NET_PORT_HANDLER, false);
        }

        return findChannel(id);
    }

    public NetChannel findChannel(long id) {
        for (NetChannel l : this) {
            if (l.id == id) {
                return l;
            }
        }
        return null;
    }

    @Override
    public void clear() {
        stop();
        if (SrvRunParams.getIsGuiOn()) {
            for (NetChannel l : this) {
                final ServerItemView view = l.getViewIfCreated();
                if (view != null) {
                    view.dispose();
                }
            }
        }
        super.clear();
    }

    public synchronized void reread() throws SQLException, InterruptedException {
        final Channels newChannels = new Channels(unit);
        newChannels.load();

        Map<Long, NetChannelOptions> allOptions = unit.getNetPortHandlerDbQueries().readAllChannelOptions();

        for (int i = 0; i < newChannels.size(); i++) {
            final NetChannel newChannel = newChannels.get(i);
            final NetChannel channel = findChannel(newChannel.id);
            if (channel != null) {
                newChannels.set(i, channel);
                channel.rereadOptions(allOptions);
            } else {
                newChannel.setDbConnection(unit.getDbConnection());
                newChannel.start();
            }
        }

        for (NetChannel channel : this) {
            final NetChannel newChannel = newChannels.findChannel(channel.id);
            if (newChannel == null) {
                channel.stop();
                if (SrvRunParams.getIsGuiOn()) {
                    channel.getView().dispose();
                }
            }
        }

        super.clear();
        super.addAll(newChannels);
    }
}
