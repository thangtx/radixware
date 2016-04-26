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

import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.sql.SQLException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.server.aio.Event;
import org.radixware.kernel.server.aio.EventDispatcher;
import org.radixware.kernel.server.aio.EventDispatcher.TimerEvent;
import org.radixware.kernel.server.aio.ServiceManifestLoader;
import org.radixware.kernel.server.aio.ServiceManifestServerLoader;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.monitoring.AbstractMonitor;
import org.radixware.kernel.server.monitoring.ChainStat;
import org.radixware.kernel.server.monitoring.IStatValue;
import org.radixware.kernel.server.monitoring.IntegralCountStat;
import org.radixware.kernel.server.monitoring.MetricParameters;
import org.radixware.kernel.server.monitoring.MonitorFactory;
import org.radixware.kernel.server.monitoring.RegisteredItem;
import org.radixware.kernel.server.trace.DbLog;
import org.radixware.kernel.server.units.AsyncEventHandlerUnit;
import org.radixware.kernel.server.units.UnitView;
import org.radixware.kernel.server.utils.OptionsGroup;

public final class NetPortHandlerUnit extends AsyncEventHandlerUnit {

    private static final int TIC_MILLIS = 1000;
    private final DbQueries netPortHndlDbQueries;
    private final Channels netChannels;
    private final Seances seances;
    private final NetPortAasClientPool aasClientsPool;
    private NetPortHandlerSap service = null;
    private Options options;
    private ServiceManifestLoader manifestLoader = null;
    private ChannelsDbLog channelsDbLog = null;
    private long itemIdSequence;
    private final ChainStat totalWaitStat = new ChainStat(1000, 5000);
    private final ChainStat aasWaitStat = new ChainStat(1000, 5000);
    private final IntegralCountStat activeAasSeancesStat = new IntegralCountStat(new MetricParameters(null, 01, TIC_MILLIS));
    private final ObjectName jmxNphStateName;
    private NetPortHandlerMXBean jmxNphStateBean;
    private volatile NetPortHandlerState jmxState = new NetPortHandlerState(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    private long lastQueueStatCalcMillis;
    private int queuePuts;
    private int queueRemoves;
    private int activeAasSeancesCount;

    public NetPortHandlerUnit(final Instance instModel, final Long id, final String title) {
        super(instModel, id, title, new MonitorFactory() {
            @Override
            public AbstractMonitor createMonitor(Object target) {
                return new NetPortHandlerMonitor((NetPortHandlerUnit) target);
            }
        });
        netPortHndlDbQueries = new DbQueries(this);
        netChannels = new Channels(this);
        seances = new Seances(this);
        aasClientsPool = new NetPortAasClientPool(this);
        try {
            jmxNphStateName = new ObjectName("org.radixware:00=SystemUnit,01=" + getId() + ",name=NetPortHandlerState");
        } catch (MalformedObjectNameException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public void appendAasWait(final long waitMillis) {
        aasWaitStat.append(waitMillis);
    }

    public void appendTotalWait(final long waitMillis) {
        totalWaitStat.append(waitMillis);
    }

    public void onQueuePut() {
        queuePuts++;
    }

    public void onQueueRemove() {
        queueRemoves++;
    }

    public void onAasRqStarted() {
        activeAasSeancesStat.append(new RegisteredItem<>(System.currentTimeMillis(), ++activeAasSeancesCount));
    }

    public void onAasRqFinished() {
        activeAasSeancesStat.append(new RegisteredItem<>(System.currentTimeMillis(), --activeAasSeancesCount));
    }

    ServiceManifestLoader getManifestLoader() {
        return manifestLoader;
    }

    public NetPortHandlerMonitor getNetPortHandlerMonitor() {
        return (NetPortHandlerMonitor) getMonitor();
    }

    Options getOptions() {
        return options;
    }

    public int getQueueSize() {
        return aasClientsPool.getQueueSize();
    }

    public long getNextItemId() {
        return ++itemIdSequence;
    }

    @Override
    protected boolean startImpl() throws Exception {
        if (!super.startImpl()) {
            return false;
        }

        options = netPortHndlDbQueries.readUnitOptions();

        logOptionsChanged(options.toString());

        itemIdSequence = 0;

        manifestLoader = new ServiceManifestServerLoader() {
            @Override
            protected Connection getDbConnection() {
                return NetPortHandlerUnit.this.getDbConnection();
            }

            @Override
            protected Arte getArte() {
                return null;
            }
        };

        final Connection dbConnection = getDbConnection();
        if (dbConnection == null) {
            return false;
        }

        channelsDbLog = new ChannelsDbLog(dbConnection, createTracer(), null);

        service = new NetPortHandlerSap(this);

        if (!service.start(dbConnection)) {
            return false;
        }

        netChannels.load();

        if (!netChannels.start()) {
            return false;
        }

        seances.setCurSeancesCountInDbForcibly();

        getDispatcher().waitEvent(new EventDispatcher.TimerEvent(), this, System.currentTimeMillis());

        queuePuts = 0;
        queueRemoves = 0;
        lastQueueStatCalcMillis = System.currentTimeMillis();
        activeAasSeancesCount = 0;
        activeAasSeancesStat.start(new RegisteredItem<Integer>(System.currentTimeMillis(), 0));

        final MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        if (mbs != null) {
            if (mbs.isRegistered(jmxNphStateName)) {
                mbs.unregisterMBean(jmxNphStateName);
            }
            mbs.registerMBean(new NetPortHandlerMXBean() {

                @Override
                public NetPortHandlerState getNetPortHandlerState() {
                    return jmxState;
                }
            }, jmxNphStateName);
        }

        return true;
    }

    @Override
    protected void stopImpl() {
        if (service != null) {
            service.stop();
            service = null;
        }
        seances.closeAll();
        if (netChannels != null) {
            netChannels.stop();
        }
        processAllEventsSuppressingRuntimeExceptions();
        seances.setCurSeancesCountInDbForcibly();
        if (netChannels != null) {
            netChannels.clear();
        }

        channelsDbLog.doFlush();

        aasClientsPool.closeAll();
        manifestLoader = null;
        super.stopImpl();
    }

    @Override
    protected void maintenanceImpl() throws InterruptedException {
        super.maintenanceImpl();
        for (NetChannel channel : getChannels()) {
            channel.maintenance();
        }
        aasClientsPool.tryInvoke();
        aasClientsPool.maintenance();
    }

    public DbLog getChannelsDbLog() {
        return channelsDbLog;
    }

    public long getSapId() {
        return options.sapId;
    }

    @Override
    protected UnitView newUnitView() {
        return new NetPortHandlerUnitView(this);
    }

    @Override
    protected void setDbConnection(final Connection dbConnection) {
        netPortHndlDbQueries.closeAll();

        if (channelsDbLog != null) {
            channelsDbLog.setDbConnection(dbConnection);
        }

        if (service != null) {
            service.setDbConnection(dbConnection);
        }
        netChannels.setDbConnection(dbConnection);

        super.setDbConnection(dbConnection);
    }

    @Override
    public String getEventSource() {
        return EEventSource.NET_PORT_HANDLER.getValue();
    }

    @Override
    protected void rereadOptionsImpl() throws SQLException, InterruptedException {
        //for (NetChannel l : netChannels) {
        //    l.rereadOptions();
        //}
        netChannels.reread();
        final Options newOptions = netPortHndlDbQueries.readUnitOptions();
        if (!newOptions.equals(options)) {
            logOptionsChanged(newOptions.toString());
        }
        options = newOptions;
        service.rereadOptions();
        if (!service.isStarted()) //RADIX-3114
        {
            requestStopAndPostponedRestart("unable to restart service");
        }
    }

    void applyChannelsSettings() throws InterruptedException {
        getTrace().put(EEventSeverity.DEBUG, "All channels restart requested", null, null, EEventSource.NET_PORT_HANDLER, false);
        synchronized (netChannels) {
            netChannels.clear();
            netChannels.load();
            if (!netChannels.start()) //RADIX-3114
            {
                requestStopAndPostponedRestart("unable to restart all channels");
            }
        }
    }

    @Override
    public void applyNewFileLogOptions() {
        super.applyNewFileLogOptions();
        for (NetChannel l : getChannels()) {
            l.applyNewFileLogOptions();
        }
    }

    NetChannel[] getNetChannelsSnapshot() {
        return netChannels.toArray(new NetChannel[]{});
    }

    DbQueries getNetPortHandlerDbQueries() {
        return netPortHndlDbQueries;
    }

    final Channels getChannels() {
        return netChannels;
    }

    final Seances getSeances() {
        return seances;
    }
    private long lastSapDbIAmAliveMillis = 0;

    @Override
    public void onEvent(final Event ev) {
        if (ev instanceof TimerEvent) {
            final long curMillis = System.currentTimeMillis();
            //sap selfcheck
            if (service != null && curMillis - lastSapDbIAmAliveMillis >= DB_I_AM_ALIVE_PERIOD_MILLIS) {
                service.dbSapSelfCheck();
                lastSapDbIAmAliveMillis = curMillis;
            }
            if (!isShuttingDown()) {
                getDispatcher().waitEvent(new EventDispatcher.TimerEvent(), this, System.currentTimeMillis() + TIC_MILLIS);
            }
            getSeances().trySetCurSeancesCountInDb();
            updateStats();
            channelsDbLog.doFlush();
        } else {
            throw new IllegalUsageError("Invalid event " + ev);
        }
    }

    private void updateStats() {
        final long curMillis = System.currentTimeMillis();
        if (curMillis <= lastQueueStatCalcMillis) {
            return;
        }
        final int queuePutsPerSecond = (int) (queuePuts * 1000. / (curMillis - lastQueueStatCalcMillis));
        final int queueRemovesPerSecond = (int) (queueRemoves * 1000. / (curMillis - lastQueueStatCalcMillis));
        queuePuts = 0;
        queueRemoves = 0;
        lastQueueStatCalcMillis = curMillis;
        final IStatValue aasTimeStat = aasWaitStat.getStat();
        final IStatValue totalTimeStat = totalWaitStat.getStat();
        final IStatValue aasSeancesStat = activeAasSeancesStat.flushAndGetLastValue();
        jmxState = new NetPortHandlerState(
                aasTimeStat.getMin().intValue(),
                aasTimeStat.getMax().intValue(),
                (int) aasTimeStat.getAvg(),
                totalTimeStat.getMin().intValue(),
                totalTimeStat.getMax().intValue(),
                (int) totalTimeStat.getAvg(),
                getQueueSize(),
                getCpuUsagePercent(),
                queuePutsPerSecond,
                queueRemovesPerSecond,
                aasSeancesStat == null ? 0 : aasSeancesStat.getMin().intValue(),
                aasSeancesStat == null ? 0 : aasSeancesStat.getMax().intValue(),
                aasSeancesStat == null ? 0 : (int) aasSeancesStat.getAvg()
        );
    }

    @Override
    public String getStatus() {
        final IStatValue aasStat = aasWaitStat.getStat();
        final IStatValue totalStat = totalWaitStat.getStat();
        return String.format("Queue: %04d; Aas: %05.0f/%05.0f/%05.0f; Total: %05.0f/%05.0f/%05.0f; CPU: .%02d",
                getQueueSize(),
                aasStat.getMin(),
                aasStat.getAvg(),
                aasStat.getMax(),
                totalStat.getMin(),
                totalStat.getAvg(),
                totalStat.getMax(),
                getCpuUsagePercent());
    }

    @Override
    public String getUnitTypeTitle() {
        return NetPortHandlerMessages.NPH_UNIT_TYPE_TYTLE;
    }

    @Override
    public Long getUnitType() {
        return EUnitType.NET_PORT_HANDLER.getValue();
    }

    NetPortAasClientPool getAasClientsPool() {
        return aasClientsPool;
    }

    static final class Options {

        final long sapId;
        final int maxAasSeancesCount;

        public Options(long sapId, int maxAasSeancesCount) {
            this.sapId = sapId;
            this.maxAasSeancesCount = maxAasSeancesCount;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 79 * hash + (int) (this.sapId ^ (this.sapId >>> 32));
            hash = 79 * hash + this.maxAasSeancesCount;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Options other = (Options) obj;
            if (this.sapId != other.sapId) {
                return false;
            }
            if (this.maxAasSeancesCount != other.maxAasSeancesCount) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return new OptionsGroup().add(NetPortHandlerMessages.MAX_AAS_SEANCES, maxAasSeancesCount).toString();
        }

    }

    private static class ChannelsDbLog extends DbLog {

        private boolean flushScheduled = false;

        public ChannelsDbLog(Connection dbConnection, LocalTracer tracer, Arte arte) {
            super(dbConnection, tracer, arte);
        }

        @Override
        public void flush() {
            flushScheduled = true;
        }

        public void flushIfScheduled() {
            if (flushScheduled) {
                doFlush();
            }
        }

        public void doFlush() {
            super.flush();
        }

    }
}
