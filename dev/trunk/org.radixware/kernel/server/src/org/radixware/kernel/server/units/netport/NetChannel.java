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

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import org.radixware.kernel.common.enums.EEventContextType;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.ELinkLevelProtocolKind;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.server.SrvRunParams;
import org.radixware.kernel.server.aio.Event;
import org.radixware.kernel.server.aio.EventHandler;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.trace.FileLogOptions;
import org.radixware.kernel.server.trace.ServerTrace;
import org.radixware.kernel.server.trace.Trace;
import org.radixware.kernel.server.trace.TraceProfiles;
import org.radixware.kernel.server.units.Messages;
import org.radixware.kernel.server.units.ViewModel;
import org.radixware.kernel.server.units.ServerItemView;

/**
 * Адаптер TCP поток <=> указанный frame
 *
 * Сетевые события и пакеты передаются прикладному классу с id =
 * protocolHandlerClassGuid
 *
 *
 */
abstract class NetChannel implements EventHandler, ViewModel {

    protected final NetPortHandlerUnit unit;
    protected final NetChannelServerTrace trace;
    protected final long id;
    protected final String title;
    protected final ELinkLevelProtocolKind linkLevelProtocolKind;
    protected final String inFrame;
    protected final String outFrame;
    private boolean needUpdateDbStats = false;
    private ServerItemView view = null;
    protected NetChannelOptions options = null;
    private boolean isStopped = true;

    protected NetChannel(
            final NetPortHandlerUnit unit,
            final long id,
            final String title,
            final ELinkLevelProtocolKind linkLevelProtocolKind,
            final String inFrame,
            final String outFrame) {
        this.unit = unit;
        this.id = id;
        this.title = title;
        this.linkLevelProtocolKind = linkLevelProtocolKind;
        this.inFrame = inFrame;
        this.outFrame = outFrame;
        trace = new NetChannelServerTrace(this);
        trace.init();
        trace.setProfiles(TraceProfiles.DEFAULT);
        trace.enterContext(EEventContextType.NET_CHANNEL, String.valueOf(id), null);
        trace.setDbConnection(unit.getDbConnection());
    }

    @Override
    public ServerTrace getTrace() {
        return trace;
    }

    public ServerItemView getView() {
        if (view == null && SrvRunParams.getIsGuiOn()) {
            view = ServerItemView.Factory.newInstance(this);
        }
        return view;
    }

    public ServerItemView getViewIfCreated() {
        return view;
    }

    @Override
    public String getTitle() {
        return title;
    }

    boolean start() throws SQLException, InterruptedException {
        options = unit.getNetPortHandlerDbQueries().readChannelOptions(this);
        if (options == null) {
            throw new RadixError(Messages.ERR_CANT_READ_OPTIONS);
        }
        getTrace().setProfiles(options.traceProfiles);
        getTrace().startFileLogging(getFileLogOptions());
        if (!startImpl()) {
            return false;
        }
        final String o = options.toString();
        getTrace().put(EEventSeverity.EVENT, NetPortHandlerMessages.CHANNEL_STARTED + o, NetPortHandlerMessages.MLS_ID_CHANNEL_STARTED, new ArrStr(getTitle(), o), EEventSource.UNIT_PORT.getValue(), false);
        isStopped = false;
        setNeedUpdateDbStats(true);
        return true;
    }

    public boolean isStarted() {
        return !isStopped;
    }

    void stop() {
        if (isStopped) {
            return;
        }
        try {
            closeImpl();
            for (Seance seance : unit.getSeances().getActiveSeances(this)) {
                seance.close(Seance.ECloseMode.GENERATE_ON_DISCONNECT_EVENT);
            }
            getTrace().put(EEventSeverity.EVENT, NetPortHandlerMessages.CHANNEL_STOPPED, NetPortHandlerMessages.MLS_ID_CHANNEL_STOPPED, new ArrStr(getTitle()), EEventSource.UNIT_PORT.getValue(), false);
            unit.getTrace().put(EEventSeverity.EVENT, NetPortHandlerMessages.CHANNEL_STOPPED + ": \"" + getTitle() + "\"", NetPortHandlerMessages.MLS_ID_CHANNEL_STOPPED, new ArrStr(getTitle()), EEventSource.NET_PORT_HANDLER, false);
            getTrace().flush();
        } finally {
            getTrace().stopFileLogging();
        }
        isStopped = true;
        setNeedUpdateDbStats(true);
    }

    public void maintenance() {
        getTrace().flush();
    }

    public final int getActiveSeanceCount() {
        return unit.getSeances().getActiveSeanceCount(this);
    }

    protected void applyOptions(final NetChannelOptions newOptions) {
        final boolean bNeedSocketRestart = !Utils.equals(options.address, newOptions.address);
        final boolean bTraceProfileChanged = !Utils.equals(options.traceProfiles, newOptions.traceProfiles);
        options = newOptions;
        if (bNeedSocketRestart) {
            restartImpl();
        }
        if (bTraceProfileChanged) {
            getTrace().setProfiles(options.traceProfiles);
        }
    }

    protected boolean rereadOptions(final Map<Long, NetChannelOptions> preloadedOptoins) throws SQLException, InterruptedException {
        NetChannelOptions newOptions = preloadedOptoins == null ? null : preloadedOptoins.get(id);
        if (newOptions == null) {
            newOptions = unit.getNetPortHandlerDbQueries().readChannelOptions(this);
        }
        if (newOptions == null || newOptions.equals(options)) {
            return false;
        }
        final String newOptionsStr = newOptions.toString();
        getTrace().put(EEventSeverity.EVENT, Messages.OPTIONS_CHANGED + newOptionsStr, Messages.MLS_ID_OPTIONS_CHANGED, new ArrStr(getTitle(), newOptionsStr), EEventSource.UNIT_PORT.getValue(), false);
        applyOptions(newOptions);
        return true;
    }

    protected abstract void restartImpl();

    protected abstract void closeImpl();

    protected abstract boolean startImpl();

    @Override
    public void onEvent(final Event ev) {
    }

    NetChannelOptions getOptions() {
        return options;
    }

    public NetPortHandlerUnit getUnit() {
        return unit;
    }

    @Override
    public long getSeqNumber() {
        return id;
    }

    @Override
    public Connection getDbConnection() {
        return unit.getDbConnection();
    }

    @Override
    public void setDbConnection(final Connection dbConnection) {
        getTrace().setDbConnection(dbConnection);
    }

    private FileLogOptions getFileLogOptions() {
        final FileLogOptions unitOpt = unit.getFileLogOptions();
        if (unitOpt == null) {
            return null;
        }
        return new FileLogOptions(
                new File(new File(unitOpt.getDir(), "net_channels"), "channel_#" + getSeqNumber()),
                "channel_#" + getSeqNumber(),
                unitOpt.getMaxFileSizeBytes(),
                unitOpt.getRotationCount(),
                unitOpt.isRotateDaily());
    }

    void applyNewFileLogOptions() {
        getTrace().changeFileLoggingOptions(getFileLogOptions());
    }

    public void setNeedUpdateDbStats(boolean needUpdate) {
        this.needUpdateDbStats = needUpdate;
        if (needUpdateDbStats) {
            unit.getSeances().trySetCurSeancesCountInDb();
        }
    }

    public boolean isNeedUpdateDbStats() {
        return needUpdateDbStats;
    }

    private static class NetChannelServerTrace extends ServerTrace {

        private final NetChannel channel;

        public NetChannelServerTrace(NetChannel channel) {
            this.channel = channel;
        }

        @Override
        protected Trace createDbTrace(Instance instance, LocalTracer traceErrorTracer) {
            return new NetChannelDbTrace(channel);
        }

        public void init() {
            initLogs(channel.getUnit().getInstance(), channel.getUnit().createTracer());
        }

    }

    private static class NetChannelDbTrace extends Trace {

        public NetChannelDbTrace(NetChannel netChannel) {
            super(netChannel.getUnit().getInstance(), null);
            dbLog = netChannel.unit.getChannelsDbLog();
        }

        @Override
        protected void initDbLog(Connection dbConnection, LocalTracer tracer) {
            //done in constructor
        }
    }
}
