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

import java.util.List;
import org.radixware.kernel.server.units.nethub.errors.NetHubFormatError;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.net.SocketAddress;
import java.util.concurrent.CopyOnWriteArrayList;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.aio.Event;
import org.radixware.kernel.server.aio.EventDispatcher;
import org.radixware.kernel.server.aio.EventDispatcher.TimerEvent;
import org.radixware.kernel.server.aio.EventHandler;
import org.radixware.kernel.server.aio.ServiceManifestLoader;
import org.radixware.kernel.server.aio.ServiceManifestServerLoader;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.exceptions.UnsupportedUnitTypeException;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.units.AsyncEventHandlerUnit;
import org.radixware.kernel.server.units.Messages;
import org.radixware.kernel.server.units.UnitView;
import org.radixware.schemas.aas.InvokeRq;
import org.radixware.schemas.aasWsdl.InvokeDocument;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.nethub.OnRecvRsDocument.OnRecvRs;


public final class NetHubUnit extends AsyncEventHandlerUnit implements EventHandler {

    static final int DEFAULT_AAS_CALL_TIMEOUT_MILLIS = 60 * 1000;//1 min
    static final Id HANDLER_ENTITY_ID = Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E");
    private static final int TIC_MILLIS = 500; //0.5 sec
    private volatile long lastRecvTimeMillis = -1;
    private volatile Options options;
    
    /**
     * @NotThreadSafe.
     * Should be accessed from unit's thread only!
     */
    private volatile AasClientPool aasClientPool;
    private final List<ExtPortListener> extPortListeners = new CopyOnWriteArrayList<ExtPortListener>();

    public final void traceDebug(String mess, String component, boolean isSensetive) {
        if (getTrace().getMinSeverity() == EEventSeverity.DEBUG.getValue().longValue()) {
            getTrace().debug((component != null ? component + ": " : "") + mess, getEventSource(), isSensetive);
        }
    }

    public final void traceError(String mess, String component) {
        getTrace().put(EEventSeverity.ERROR, (component != null ? component + ": " : "") + mess, null, null, getEventSource(), false);
    }

    @Override
    public String getUnitTypeTitle() {
        return NetHubMessages.UNIT_TYPE_TITLE;
    }
    
    @Override
    public Long getUnitType() {
        return EUnitType.NET_HUB.getValue();
    }
    
    void requestStopAndPostponeRestartFromAdminAdapter(final String reason) {
        requestStopAndPostponedRestart(reason);
    }

    static final class Options {

        final Long inSeanceCnt;
        final Long outSeanceCnt;
        final Long echoTestPeriodMillis;
        final Long reconnectNoEchoCnt;
        final boolean toProcessStart;
        final boolean toProcessStop;
        final boolean toProcessConnect;
        final boolean toProcessDisconnect;
        final boolean toProcessDuplicatedRq;
        final boolean toProcessUncorrelatedRs;                
        final ExtPort.Options extPortOptions;
        final long sapId;

        Options(
                final Long inSeanceCnt,
                final Long outSeanceCnt,
                final Long echoTestPeriodMillis,
                final Long reconnectNoEchoCnt,
                final boolean toProcessStart,
                final boolean toProcessStop,
                final boolean toProcessConnect,
                final boolean toProcessDisconnect,
                final boolean toProcessDuplicatedRq,
                final boolean toProcessUncorrelatedRs,
                final long sapId,
                final ExtPort.Options extPortOptions) {
            this.inSeanceCnt = inSeanceCnt;
            this.outSeanceCnt = outSeanceCnt;
            this.echoTestPeriodMillis = echoTestPeriodMillis;
            this.reconnectNoEchoCnt = reconnectNoEchoCnt;
            this.toProcessStart = toProcessStart;
            this.toProcessStop = toProcessStop;
            this.toProcessConnect = toProcessDisconnect;
            this.toProcessDisconnect = toProcessDisconnect;
            this.toProcessDuplicatedRq = toProcessDuplicatedRq;
            this.toProcessUncorrelatedRs = toProcessUncorrelatedRs;
            this.extPortOptions = extPortOptions;
            this.sapId = sapId;
        }

        @Override
        public boolean equals(final Object o) {
            if (o == null || !(o instanceof Options)) {
                return false;
            }
            final Options opt = (Options) o;
            return (inSeanceCnt == null ? opt.inSeanceCnt == null : inSeanceCnt.equals(opt.inSeanceCnt))
                    && (outSeanceCnt == null ? opt.outSeanceCnt == null : outSeanceCnt.equals(opt.outSeanceCnt))
                    && (echoTestPeriodMillis == null ? opt.echoTestPeriodMillis == null : echoTestPeriodMillis.equals(opt.echoTestPeriodMillis))
                    && (reconnectNoEchoCnt == null ? opt.reconnectNoEchoCnt == null : reconnectNoEchoCnt.equals(opt.reconnectNoEchoCnt))
                    && (toProcessStart == opt.toProcessStart)
                    && (toProcessStop == opt.toProcessStop)                    
                    && (toProcessConnect == opt.toProcessConnect)
                    && (toProcessDisconnect == opt.toProcessDisconnect)
                    && (toProcessDuplicatedRq == opt.toProcessDuplicatedRq)
                    && (toProcessUncorrelatedRs == opt.toProcessUncorrelatedRs)
                    && (sapId == opt.sapId)
                    && (extPortOptions == null ? opt.extPortOptions == null : extPortOptions.equals(opt.extPortOptions));
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 29 * hash + (this.inSeanceCnt != null ? this.inSeanceCnt.hashCode() : 0);
            hash = 29 * hash + (this.outSeanceCnt != null ? this.outSeanceCnt.hashCode() : 0);
            hash = 29 * hash + (this.echoTestPeriodMillis != null ? this.echoTestPeriodMillis.hashCode() : 0);
            hash = 29 * hash + (this.reconnectNoEchoCnt != null ? this.reconnectNoEchoCnt.hashCode() : 0);
            hash = 29 * hash + (this.extPortOptions != null ? this.extPortOptions.hashCode() : 0);
            hash = 29 * hash + (this.toProcessStart ? 1 : 0);
            hash = 29 * hash + (this.toProcessStop ? 1 : 0);
            hash = 29 * hash + (this.toProcessConnect ? 1 : 0);
            hash = 29 * hash + (this.toProcessDisconnect ? 1 : 0);
            hash = 29 * hash + (this.toProcessDuplicatedRq ? 1 : 0);
            hash = 29 * hash + (this.toProcessUncorrelatedRs ? 1 : 0);
            hash = 29 * hash + (int) this.sapId;
            return hash;
        }

        @Override
        public String toString() {
            return "{\n\t"
                    + NetHubMessages.IN_REQ_COUNT + (inSeanceCnt == null ? "-" : String.valueOf(inSeanceCnt)) + "; \n\t"
                    + NetHubMessages.OUT_REQ_COUNT + (outSeanceCnt == null ? "-" : String.valueOf(outSeanceCnt)) + "; \n\t"
                    + NetHubMessages.ECHO_TEST_PERIOD_SEC + (echoTestPeriodMillis == null ? "-" : String.valueOf(echoTestPeriodMillis.longValue() / 1000)) + "; \n\t"
                    + NetHubMessages.RECONNECT_NO_ECHO_COUNT + (reconnectNoEchoCnt == null ? "-" : reconnectNoEchoCnt) + "; \n\t"
                    + NetHubMessages.EXT_PORT_OPTIONS + String.valueOf(extPortOptions).replace("\t", "\t\t").replace("}", "\t}") + " \n"
                    + "}";
        }
    }
    private final DbQueries nhDbQueries;
    private final ExtPort extPort;
    private volatile NetHubInterfaceSap service = null;

    public NetHubUnit(final Instance instModel, final Long id, final String title) throws UnsupportedUnitTypeException {
        super(instModel, id, title);
        nhDbQueries = new DbQueries(this);
        extPort = new ExtPort(this);
    }

    final AasClientPool getAasClientPool() {
        return aasClientPool;
    }

    final NetHubInterfaceSap getService() {
        return service;
    }
    
    final ExtPort getExtPort() {
        return extPort;
    }

    final void registerExtPortListener(ExtPortListener lst) {
        extPortListeners.add(lst);
    }

    final void removeExtPortListener(ExtPortListener lst) {
        extPortListeners.remove(lst);
    }
    private ServiceManifestLoader manifestLoader = null;

    ServiceManifestLoader getManifestLoader() {
        return manifestLoader;
    }

    @Override
    protected boolean startImpl() throws Exception {
        adsHandlerState = AdsUnitState.INITED;
        if (!super.startImpl()) {
            return false;
        }
        options = nhDbQueries.readOptions();
        manifestLoader = new ServiceManifestServerLoader() {

            @Override
            protected Connection getDbConnection() {
                return NetHubUnit.this.getDbConnection();
            }

            @Override
            protected Arte getArte() {
                return null;
            }
        };
        aasClientPool = new AasClientPool(this);
        service = new NetHubInterfaceSap(this);
        if (!service.start(getDbConnection())) {
            return false;
        }
        final String optionsStr = options.toString();
        getTrace().put(EEventSeverity.EVENT, Messages.START_OPTIONS + optionsStr, Messages.MLS_ID_START_OPTIONS, new ArrStr(getTitle(), optionsStr), getEventSource(), false);
        getDispatcher().waitEvent(new EventDispatcher.TimerEvent(), this, System.currentTimeMillis());
        return true;
    }

    @Override
    protected void stopImpl() {
        extPort.stop();
        if (service != null) {
            service.stop();
            service = null;
        }
        if (aasClientPool != null) {
            aasClientPool.stop();
            aasClientPool = null;
        }
        manifestLoader = null;
        super.stopImpl();
        adsHandlerState = AdsUnitState.INITED;
    }

    @Override
    protected boolean requestShutdownImpl() {
        //TWRBS-855: Should be accessed from unit's thread only!
        //TWRBS-855: aasClientPool.onBeforeStop();
        if (AdsUnitState.RUNNING == adsHandlerState) //beforeStart was called succesfully
        {
            adsHandlerState = AdsUnitState.BEFORE_STOP_REQUESTED;
            return false;
        } else {
            return super.requestShutdownImpl();
        }
    }


    protected final void onBeforeStopProcessed() {
        adsHandlerState = AdsUnitState.BEFORE_STOP_PROCESSED;
        final boolean wasConnected = extPort.isConnected();
        extPort.stop();
        if (!wasConnected) {//wasn't connected there is no need to call onDisconnect and wait for response
            requestShutdown();
        }
    }
    
    protected final void requestShutdownByAdmin() {
        requestShutdown();
    }

    protected final void onOnDiconnectDuringStopProcessed() {
        requestShutdown();
    }

    protected final void onBeforeStartProcessed() {
        adsHandlerState = AdsUnitState.RUNNING;
        if (!extPort.start()) {
            requestStopAndPostponedRestart("unable to start ext port");
        }
    }

    @Override
    protected UnitView newUnitView() {
        return new NetHubUnitView(this);
    }

    @Override
    protected void setDbConnection(final Connection dbConnection) {
        nhDbQueries.closeAll();
        if (service != null) {
            service.setDbConnection(dbConnection);
        }
        super.setDbConnection(dbConnection);
    }

    @Override
    public String getEventSource() {
        return EEventSource.NET_HUB_HANDLER.getValue();
    }

    @Override
    protected void rereadOptionsImpl() throws SQLException, InterruptedException {
        final Options newOptions = nhDbQueries.readOptions();
        if (newOptions == null || newOptions.equals(options)) {
            return;
        }
        final boolean bNeedPortRestart = !Utils.equals(options.extPortOptions, newOptions.extPortOptions);
        options = newOptions;
        final String newOptionsStr = newOptions.toString();
        getTrace().put(EEventSeverity.EVENT, Messages.OPTIONS_CHANGED + newOptionsStr, Messages.MLS_ID_OPTIONS_CHANGED, new ArrStr(getTitle(), newOptionsStr), getEventSource(), false);
        service.rereadOptions();
        if (!service.isStarted()) {
            requestStopAndPostponedRestart("unable to restart service");
        }
        if (bNeedPortRestart && !extPort.restart()) {
            requestStopAndPostponedRestart("unable to restart ext port");
        }
    }

    DbQueries getNetHubDbQueries() {
        return nhDbQueries;
    }

    Options getOptions() {
        return options;
    }
    private long lastSapDbIAmAliveMillis = 0;

    @Override
    public void onEvent(final Event ev) {
        if (ev instanceof TimerEvent) {
            switch (getAdsUnitState()) {
                case BEFORE_STOP_REQUESTED:
                    adsHandlerState = AdsUnitState.BEFORE_STOP_CALLED;
                    aasClientPool.beforeStop();//TWRBS-855: Should be accessed from unit's thread only!
                    break;
                case INITED:
                    adsHandlerState = AdsUnitState.BEFORE_START_CALLED;
                    aasClientPool.beforeStart();
                    break;
            }
            final long curMillis = System.currentTimeMillis();
            //sap selfcheck
            if (curMillis - lastSapDbIAmAliveMillis >= DB_I_AM_ALIVE_PERIOD_MILLIS) {
                service.dbSapSelfCheck();
                lastSapDbIAmAliveMillis = curMillis;
            }
            if (getOptions().echoTestPeriodMillis != null && getOptions().reconnectNoEchoCnt != null
                    && lastRecvTimeMillis + getOptions().echoTestPeriodMillis.longValue() < curMillis
                    && extPort.isConnected() && adsHandlerState == AdsUnitState.RUNNING) {
                if (lastRecvTimeMillis != -1) {
                    aasClientPool.onInactivityTimer();
                }
                lastRecvTimeMillis = curMillis;
            }
            if (!isShuttingDown()) {
                getDispatcher().waitEvent(new EventDispatcher.TimerEvent(), this, System.currentTimeMillis() + TIC_MILLIS);
            }
        } else {
            throw new IllegalUsageError("Invalid event " + ev);
        }
    }

    void onConnect(SocketAddress remoteAddress) {
        for (ExtPortListener lst : extPortListeners) {
            lst.onConnect(remoteAddress);
        }
        aasClientPool.onConnect();
    }

    void onDisconnect() {
        aasClientPool.onDisconnect();
        if (service != null) {
            service.clearSeances();
        }
        for (ExtPortListener lst : extPortListeners) {
            lst.onDisconnect();
        }
    }

    void processExtRs(final byte[] mess) {
        getTrace().debug("Response received from AAS", getEventSource(), false);
        if (mess != null && mess.length > 0) {
            sendToExtPort(mess, "Response");
        }
    }
    private String lastUniqueKey;

    void processExtRq(final byte[] mess) {
        lastRecvTimeMillis = System.currentTimeMillis();
        for (ExtPortListener lst : extPortListeners) {
            lst.onRecv(mess);
        }
        aasClientPool.onAfterRecv(mess, service.parsingVarsByMess);
    }
    
    void onAfterRecvProcessed(final byte[] mess, final OnRecvRs rs) {
        try {
            final boolean isRequest = rs.getIsRequest().booleanValue();
            if (isRequest) {
                final String uniqueKey = rs.getUniqueKey();
                if (Utils.equals(uniqueKey, lastUniqueKey)) {
                    getTrace().put(EEventSeverity.ERROR, NetHubMessages.ERR_DUPLICATE_RQ + ": KEY=" + uniqueKey, NetHubMessages.MLS_ID_ERR_DUPLICATE_RQ, new ArrStr(getTitle(), uniqueKey), getEventSource(), false);
                    aasClientPool.onInvalidMessage(mess, AdsUnitAdminAdapter.InvalidMessageType.DUPLICATE);
                } else {
                    lastUniqueKey = uniqueKey;
                    getTrace().debug("Request received from the external system: KEY=" + uniqueKey, getEventSource(), false);
                    aasClientPool.onRequest(mess, uniqueKey);
                }
            } else {
                final byte[] rqMess = rs.getRqMess();
                getTrace().debug("Response received from the external system: mess=" + Hex.encode(mess), getEventSource(), false);
                if (!service.onResponse(rqMess, mess)) {
                    getTrace().put(EEventSeverity.WARNING, NetHubMessages.ERR_LATE_RS, NetHubMessages.MLS_ID_ERR_LATE_RS, new ArrStr(getTitle()), getEventSource(), false);
                    aasClientPool.onInvalidMessage(mess, AdsUnitAdminAdapter.InvalidMessageType.UNCORRELATED);
                }
            }
        } catch (Throwable ex) {
            final String exStack;
            if (ex instanceof NetHubFormatError) {
                aasClientPool.onInvalidMessage(mess, AdsUnitAdminAdapter.InvalidMessageType.UNCORRELATED);
                exStack = ExceptionTextFormatter.getExceptionMess(ex);
            } else {
                exStack = ExceptionTextFormatter.exceptionStackToString(ex);
            }
            getTrace().put(EEventSeverity.ERROR, NetHubMessages.ERR_ON_EXT_RQ_PROC + exStack, NetHubMessages.MLS_ID_ERR_ON_EXT_RQ_PROC, new ArrStr(getTitle(), exStack), getEventSource(), false);
        }
    }
    
//DacUnit AAS clients tools
    final InvokeDocument prepareDacUnitAdapterInvoke(final String mhId) {
        final InvokeDocument invokeXml = InvokeDocument.Factory.newInstance();
        final InvokeRq rq = invokeXml.addNewInvoke().addNewInvokeRq();
        rq.setPID(String.valueOf(getId()));
        //rq.setMode(ModeEnum.DEV); //FIXME rq.setMode(DbpValueConverter.cDacMode2AasDacMode(unit.getDacMode()));
        rq.setEntityId(HANDLER_ENTITY_ID);
        rq.setMethodId(mhId);
        return invokeXml;
    }

    final InvokeDocument prepareDacUnitAdapterInvokeWithMessParam(final String mhId, final byte[] mess) {
        final InvokeDocument invokeXml = prepareDacUnitAdapterInvoke(mhId);
        final InvokeRq rq = invokeXml.getInvoke().getInvokeRq();
        rq.addNewParameters().addNewItem().setBin(mess);
        return invokeXml;
    }

    void sendToExtPort(final byte[] mess, String messName) {
        try {
            for (ExtPortListener lst : extPortListeners) {
                lst.onSend(mess);
            }
            getExtPort().send(mess, messName);
        } catch (IOException ex) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(ex);
            getTrace().put(EEventSeverity.ERROR, NetHubMessages.ERR_ON_SOCKET_IO + exStack, NetHubMessages.MLS_ID_ERR_ON_SOCKET_IO, new ArrStr(getTitle(), exStack), getEventSource(), false);
        }
    }
    volatile AdsUnitState adsHandlerState = AdsUnitState.INITED;

    AdsUnitState getAdsUnitState() {
        return adsHandlerState;
    }
}
