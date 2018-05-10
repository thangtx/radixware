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
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.server.SrvRunParams;
import org.radixware.kernel.server.aio.AadcAffinity;
import org.radixware.kernel.server.aio.Event;
import org.radixware.kernel.server.aio.EventHandler;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.instance.ResourceRegistry;
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
public abstract class NetChannel implements EventHandler, ViewModel {

    protected final NetPortHandlerUnit unit;
    protected final NetChannelServerTrace trace;
    protected final long id;
    protected String title;
    private boolean needUpdateDbStats = false;
    private ServerItemView view = null;
    protected NetChannelOptions options = null;
    private boolean isStopped = true;
    private String viewStatus = null;
    private INetChannelAffinityHandler aadcAffinityHandler;

    protected NetChannel(
            final NetPortHandlerUnit unit,
            final long id,
            final String title) {
        this.unit = unit;
        this.id = id;
        this.title = title;
        trace = new NetChannelServerTrace(this);
        trace.setOwnerDescription("NetChannel[" + id + "]");
        trace.init();
        trace.setProfiles(TraceProfiles.DEFAULT);
        trace.enterContext(EEventContextType.NET_CHANNEL, String.valueOf(id), null);
        trace.setDbConnection(unit.getDbConnection());
    }

    @Override
    public ServerTrace getTrace() {
        return trace;
    }

    public long getId() {
        return id;
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
    public String getViewStatus() {
        return viewStatus;
    }

    public void seanceUnregistered(final Seance seance) {
    }

    @Override
    public String getTitle() {
        return title;
    }

    public String getResourceKeyPrefix() {
        return unit.getResourceKeyPrefix() + ResourceRegistry.SEPARATOR + "NetChannel[" + id + "]";
    }

    boolean start() throws SQLException, InterruptedException {
        try {
            options = unit.getNetPortHandlerDbQueries().readChannelOptions(this);

            if (options == null) {
                throw new RadixError(Messages.ERR_CANT_READ_OPTIONS);
            }

            title = options.title;
            if (options.bindAddress == null && options.connectAddress == null) {
                throw new IllegalStateException("Unable to start net channel '" + getTitle() + "': invalid address '" + options.addressString + "'");
            }
            getTrace().setProfiles(options.traceProfiles);
            getTrace().startFileLogging(getFileLogOptions());

            unit.getInstance().getResourceRegistry().closeAllWithKeyPrefix(getResourceKeyPrefix(), "channel start");
            
            aadcAffinityHandler = createAadcAffinityHandler();

            if (!startImpl()) {
                return false;
            }
            final String o = options.toString();
            getTrace().put(EEventSeverity.EVENT, NetPortHandlerMessages.CHANNEL_STARTED + o, NetPortHandlerMessages.MLS_ID_CHANNEL_STARTED, new ArrStr(getTitle(), o), EEventSource.UNIT_PORT.getValue(), false);
            isStopped = false;
            setNeedUpdateDbStats(true);
            return true;
        } catch (Throwable e) {
            if (e instanceof InterruptedException) {
                throw (InterruptedException) e;
            }
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            unit.getTrace().put(EEventSeverity.ERROR, NetPortHandlerMessages.ERR_ON_CHANNEL_START + " \"" + getTitle() + "\": \n" + exStack, NetPortHandlerMessages.MLS_ID_ERR_ON_CHANNEL_START, new ArrStr(getTitle(), exStack), EEventSource.NET_PORT_HANDLER, false);
        }
        return false;
    }
    
    private INetChannelAffinityHandler createAadcAffinityHandler() {
        if (options.aadcAffinityHandlerClassName != null && unit.getInstance().getAadcManager().isInAadc()) {
            try {
                Class c = this.getClass().getClassLoader().loadClass(options.aadcAffinityHandlerClassName);
                return (INetChannelAffinityHandler) c.newInstance();
            } catch (Throwable t) {
                getTrace().put(EEventSeverity.WARNING, "Unable to create AADC affinity handler: " + ExceptionTextFormatter.throwableToString(t), EEventSource.NET_PORT_HANDLER);
            } 
        }
        return null;
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
        if (SrvRunParams.getIsGuiOn() && getViewIfCreated() != null) {
            viewStatus = calcViewStatus();
        }
    }
    
    protected AadcAffinity getAadcAffinity(Seance seance, Seance.FramePacket packet) {
        if (aadcAffinityHandler == null) {
            return null;
        }
        return aadcAffinityHandler.getAadcAffinity(seance, packet == null ? null : packet.packet, packet == null ? null : packet.headers);
    }

    protected abstract String calcViewStatus();

    public final int getActiveSeancesCount() {
        return unit.getSeances().getActiveSeanceCount(this);
    }

    public int getBusySeancesCount() {
        int result = 0;
        for (Seance s : unit.getSeances().getActiveSeances(this)) {
            if (s.isBusy()) {
                result++;
            }
        }
        return result;
    }

    protected void applyOptions(final NetChannelOptions newOptions) {
        final boolean bNeedSocketRestart = !Utils.equals(options.bindAddress, newOptions.bindAddress);
        final boolean bTraceProfileChanged = !Utils.equals(options.traceProfiles, newOptions.traceProfiles);
        options = newOptions;
        if (bNeedSocketRestart) {
            restartImpl();
        }
        if (bTraceProfileChanged) {
            getTrace().setProfiles(options.traceProfiles);
        }
    }
    
    public void beforInvokeOnRecv(final Seance seance) {
        
    }
    
    public void afterInvokeOnRecv(final Seance seance) {
        
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
                unitOpt.isRotateDaily(),
                unitOpt.isWriteContextToFile());
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

    protected static class NetChannelServerTrace extends ServerTrace {

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
