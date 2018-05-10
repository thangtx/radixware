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

import java.awt.Window;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.ValueFormatter;
import org.radixware.kernel.server.aio.Event;
import org.radixware.kernel.server.aio.EventDispatcher.ConnectEvent;
import org.radixware.kernel.server.instance.ResourceRegistry;
import org.radixware.kernel.server.instance.SocketResourceRegistryItem;
import org.radixware.schemas.aas.InvokeRs;
import org.radixware.schemas.netporthandler.ConnectRq;
import org.radixware.schemas.netporthandler.ExceptionEnum;
import org.radixware.schemas.netporthandler.ProcessRq;
import org.radixware.schemas.netporthandlerWsdl.ConnectDocument;

public final class NetClient extends NetChannel {

    private final NetClientQueue queue = new NetClientQueue();
    private final LocalTracer invokeTracer;
    private final Map<SocketChannel, Seance> connectingSeances = new HashMap<>();
    private int lastWrittenBusySessionCount = -1;
    private long lastQueueItemId = 0;

    public NetClient(
            final NetPortHandlerUnit unit,
            final long id,
            final String title) {
        super(unit, id, title);
        invokeTracer = trace.newTracer(EEventSource.UNIT_PORT.getValue());
    }

    @Override
    public Window getParentView() {
        return unit.getView().getDialog();
    }

    @Override
    protected String calcViewStatus() {
        return String.format("Seances: %04d; Busy: %04d; Queue: %04d",
                getActiveSeancesCount(),
                getBusySeancesCount(),
                queue.size());
    }

    @Override
    protected void restartImpl() {
    }

    public void trace(final String mess) {
        trace.debug(mess, EEventSource.UNIT_PORT, false);
    }

    @Override
    protected void closeImpl() {
        for (NetClientQueueItem item : queue.listQueue()) {
            try {
                if (item.getSapSeance() != null && item.getSapSeance().seance.isConnected()) {
                    item.getSapSeance().response(new ServiceProcessServerFault(ExceptionEnum.SERVER_EXCEPTION.toString(), "Channel is stopped", null, null));
                }
            } catch (Throwable t) {
                //ignore
            }
        }
        queue.clear();
    }

    @Override
    protected boolean startImpl() {
        lastWrittenBusySessionCount = -1;
        lastQueueItemId = 0;
        return true;
    }

    public long getNextQueueItemId() {
        return ++lastQueueItemId;
    }

    private void responseQueueOverflow(final NetPortSapSeance sapSeance) {
        sapSeance.response(new ServiceProcessServerFault(ExceptionEnum.SERVER_EXCEPTION.toString(), "Channel queue overflow", null, null));
    }

    public void onProcessRq(final NetPortSapSeance sapSeance) throws Exception {
        final ProcessRq xRq = ((ProcessRq) sapSeance.xmlRq);
        if (options.isSyncMode) {
            xRq.setIsRecvSync(true);
        }
        if (!queue.add(new NetClientQueueItem(this, xRq, sapSeance))) {
            responseQueueOverflow(sapSeance);
        }
    }

    public void onConnectRq(final NetPortSapSeance sapSeance) throws Exception {
        final ConnectRq xRq = ((ConnectRq) sapSeance.xmlRq);
        if (options.isSyncMode) {
            xRq.setIsConnectSync(true);
            xRq.setIsRecvSync(true);
        }
        if ((xRq.isSetIsConnectSync() && xRq.getIsConnectSync())
                || (xRq.isSetIsRecvSync() && xRq.getIsRecvSync())) {
            if (!queue.add(new NetClientQueueItem(this, xRq, sapSeance))) {
                responseQueueOverflow(sapSeance);
            }
        } else {
            if (!queue.add(new NetClientQueueItem(this, xRq, new NetPortCallbackTarget(xRq.getCallbackPid(), xRq.getCallbackWid())))) {
                responseQueueOverflow(sapSeance);
            } else {
                sapSeance.response(prepareAsyncConnectResponse());
            }
        }
    }

    private ConnectDocument prepareAsyncConnectResponse() {
        final ConnectDocument xDoc = ConnectDocument.Factory.newInstance();
        xDoc.addNewConnect().addNewConnectRs();
        return xDoc;
    }

    @Override
    public void maintenance() {
        maintenanceTimedOutItems();
        closeWithDisconnectedCaller();
        super.maintenance();
    }

    public void processQueue() {
        for (Seance s : unit.getSeances().getActiveSeances(this)) {
            s.tryScheduledClose();
        }

        int busyCount = getBusySeancesCount();

        for (NetClientQueueItem item : queue.listQueue()) {
            if (busyCount < options.maxSessionCount) {
                Seance seance;
                try {
                    seance = captureOrCreate(item);
                } catch (Exception ex) {
                    if (item.getSapSeance() != null) {
                        item.getSapSeance().response(ex);
                        queue.remove(item, "capture error");
                    }
                    continue;
                }
                if (seance != null) {
                    queue.remove(item, "captured");
                    busyCount++;
                }
            }
            if (busyCount >= options.maxSessionCount) {
                break;
            }
        }

        if (getOptions().isBusySessionCountOn && busyCount != lastWrittenBusySessionCount) {
            try {
                getUnit().getNetPortHandlerDbQueries().setCurSeanceCount(new NetPortDbQueries.ChannelStateInfo(id, getActiveSeancesCount(), busyCount));
                getUnit().getDbConnection().commit();
            } catch (Exception ex) {
                getUnit().getTrace().put(EEventSeverity.ERROR, "Unable to update status of network channel '" + getTitle() + "' in DB: " + ExceptionTextFormatter.throwableToString(ex), EEventSource.NET_PORT_HANDLER);
            }
        }
        if (options.isSyncMode) {
            getUnit().getNetPortHandlerMonitor().updateBusySeancesCount(this);
        }
    }
    
    protected void closeWithDisconnectedCaller() {
        for (Seance s : unit.getSeances().getActiveSeances(this)) {
            s.tryCloseIfCallerDisconnected();
        }
    }

    protected void maintenanceTimedOutItems() {
        final List<NetClientQueueItem> timedOut = queue.removeTimedOut();
        for (NetClientQueueItem item : timedOut) {
            if (item.getSapSeance() != null) {
                item.getSapSeance().responseCaptureTimeout();
            } else {
                unit.getAasClientsPool().invokeOnConnectTimeout(
                        createInvokeSource("Channel#" + id + ":CaptureSeanceTimeout" + item.getCallbackTarget()),
                        id,
                        item.getEffectiveServerAddr(),
                        item.getEffectiveClientAddr(),
                        null,
                        null,
                        item.getCallbackTarget().getCallbackPid(),
                        item.getCallbackTarget().getCallbackWid(),
                        null);
            }
        }
    }

    private INetPortInvokeSource createInvokeSource(final String rqDescr) {
        return new INetPortInvokeSource() {

            @Override
            public LocalTracer getTracer() {
                return invokeTracer;
            }

            @Override
            public void onAasInvokeResponse(InvokeRs xRs) {
                //ignore - only connect timeouts are reported here
            }

            @Override
            public void onAasInvokeException(ServiceClientException ex) {
                getTracer().put(EEventSeverity.ERROR, "Unable to send '" + rqDescr + "' to AAS: " + ExceptionTextFormatter.throwableToString(ex), null, null, false);
            }

            @Override
            public NetPortHandlerUnit getUnit() {
                return unit;
            }

            @Override
            public String getInvokeSourceDesc() {
                return rqDescr;
            }
        };
    }

    private Seance captureOrCreate(final NetClientQueueItem item) throws Exception {

        final long curTimeMillis = System.currentTimeMillis();

        final InetSocketAddress remoteAddress;
        if (item.getServerAddress() != null) {
            remoteAddress = ValueFormatter.parseInetSocketAddress(item.getServerAddress());
        } else {
            remoteAddress = item.getChannelConnectAddressSnapshot();
        }

        InetSocketAddress localAddress;
        if (item.getClientAddress() != null) {
            localAddress = ValueFormatter.parseInetSocketAddress(item.getClientAddress());
        } else {
            localAddress = item.getChannelBindAddressSnapshot();
        }
        if (!item.mustOpenNewConnection()) {
            for (Seance s : unit.getSeances().getActiveSeances(this)) {
                if (!s.isBusy()) {

                    if (!s.getPort().isConnected()) {
                        s.close(Seance.ECloseMode.SILENT);//not sure why SILENT, legacy behavior
                        continue;
                    }

                    boolean matches = false;
                    if (item.getSid() != null) {
                        matches = item.getSid().equals(s.sid);
                    } else {
                        try {
                            if (s.getPort().getChannel().getRemoteAddress().equals(remoteAddress)
                                    && (localAddress == null || s.getPort().getChannel().getLocalAddress().equals(localAddress))) {
                                matches = true;
                            }
                        } catch (Exception ex) {
                            trace.put(EEventSeverity.WARNING, "Unable to check if seance matches request, closing seance. Error: " + ExceptionTextFormatter.throwableToString(ex), EEventSource.UNIT_PORT);
                            s.close(Seance.ECloseMode.SILENT);
                            continue;
                        }
                    }
                    if (matches) {
                        item.markCaptured(curTimeMillis);
                        item.markConnected(curTimeMillis);
                        s.addTask(new SeanceTaskClientQueueItem(item));
                        s.processTasks(false);
                        return s;
                    }
                }
            }
        }

        if (item.getOperation() == EChannelOperation.CONNECT) {
            while (unit.getSeances().getActiveSeances(this).size() >= options.maxSessionCount) {
                long minCreateTimeMillis = Long.MAX_VALUE;
                Seance oldestFreeSeance = null;
                for (Seance s : unit.getSeances().getActiveSeances(this)) {
                    if (!s.isBusy() && s.getCreateTimeMillis() < minCreateTimeMillis) {
                        oldestFreeSeance = s;
                        minCreateTimeMillis = s.getCreateTimeMillis();
                    }
                }
                if (oldestFreeSeance != null) {
                    trace("Closing free seance '" + oldestFreeSeance.getConnectionDesc() + "' to free up slot for new request");
                    oldestFreeSeance.close(Seance.ECloseMode.SILENT);
                }
            }
            if (unit.getSeances().getActiveSeances(this).size() < options.maxSessionCount) {
                SocketChannel clientSocketChannel = null;
                Seance s = null;
                try {
                    clientSocketChannel = SocketChannel.open();

                    unit.getInstance().getResourceRegistry().register(new SocketResourceRegistryItem(getResourceKeyPrefix() + ResourceRegistry.SEPARATOR + "pendingTo[" + remoteAddress + "]@" + System.identityHashCode(clientSocketChannel), clientSocketChannel, null, unit.getThisRunAliveChecker()));

                    clientSocketChannel.configureBlocking(false);

                    try {
                        clientSocketChannel.socket().setTcpNoDelay(true);
                    } catch (IOException ex) {
                        //Microsoft bug
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                    }
                    try {
                        clientSocketChannel.socket().setKeepAlive(options.useKeepAlive);
                    } catch (IOException ex) {
                        //Vista bug
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                    }

                    if (localAddress == null) {
                        localAddress = new InetSocketAddress(0);
                    }

                    if (localAddress.isUnresolved()) {
                        throw new IOException("Can't resolve client socket's address: " + localAddress);
                    }

                    clientSocketChannel.socket().bind(localAddress);
                    getTrace().put(EEventSeverity.DEBUG, "Connecting to " + remoteAddress + " from " + clientSocketChannel.socket().getLocalAddress(), null, null, EEventSource.UNIT_PORT.getValue(), false);
                    s = unit.getSeances().openNew(this, clientSocketChannel);
                    s.generateSid(remoteAddress.getHostString(), remoteAddress.getPort());
                    unit.getSeances().registerSeance(s);
                    item.markCaptured(curTimeMillis);
                    connectingSeances.put(clientSocketChannel, s);
                    s.addTask(new SeanceTaskClientQueueItem(item));
                    if (clientSocketChannel.connect(remoteAddress)) {
                        onEvent(new ConnectEvent(clientSocketChannel));
                    } else {
                        getTrace().put(EEventSeverity.DEBUG, "Client port waits for connect to " + remoteAddress + " from " + clientSocketChannel.socket().getLocalAddress() + " (SID: " + s.sid + ")", null, null, EEventSource.UNIT_PORT.getValue(), false);
                        final Long leftForConnectMillis = item.getLeftForConnectMillis(System.currentTimeMillis());
                        if (leftForConnectMillis != null && leftForConnectMillis >= 0) {
                            unit.getDispatcher().waitEvent(new ConnectEvent(clientSocketChannel), this, System.currentTimeMillis() + leftForConnectMillis);
                        } else {
                            unit.getDispatcher().waitEvent(new ConnectEvent(clientSocketChannel), this, -1);
                        }
                    }

                } catch (Throwable e) {
                    final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                    getTrace().put(EEventSeverity.ERROR, NetPortHandlerMessages.ERR_ON_SOCKET_START + exStack, NetPortHandlerMessages.MLS_ID_ERR_ON_SOCKET_START, new ArrStr(getTitle(), exStack), EEventSource.UNIT_PORT.getValue(), false);
                    if (clientSocketChannel != null) {
                        connectingSeances.remove(clientSocketChannel);
                        closeChannel(clientSocketChannel);
                    }
                    if (s != null) {
                        unit.getSeances().unregisterSeance(s);
                    }
                    throw e;
                }
                return s;
            }
        }
        return null;
    }

    private void closeChannel(SocketChannel channel) {
        if (channel != null) {
            try {
                channel.close();
            } catch (IOException ex) {
                //do nothing
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }
    }

    @Override
    public void seanceUnregistered(Seance seance) {
        super.seanceUnregistered(seance);
        for (ISeanceTask task : seance.tasks) {
            try {
                if (task.getSapSeance() != null && task.getSapSeance().seance != null && task.getSapSeance().seance.isConnected()) {
                    task.getSapSeance().response(new ServiceProcessServerFault(ExceptionEnum.SERVER_EXCEPTION.toString(), "Seance-processor has been closed", null, null));
                }
            } catch (Throwable t) {
                //ignore
            }
        }
        for (NetClientQueueItem item : queue.listQueue()) {
            try {
                if (item.getSid() != null
                        && item.getSid().equals(seance.sid)) {
                    if (item.getSapSeance() != null
                            && item.getSapSeance().seance != null
                            && item.getSapSeance().seance.isConnected()) {
                        item.getSapSeance().response(new ServiceProcessServerFault(ExceptionEnum.SERVER_EXCEPTION.toString(), "Target seance has been closed", null, null));
                    }
                    queue.remove(item, "target seance closed");
                }
            } catch (Throwable t) {
                //ignore
            }
        }
    }

    @Override
    public void onEvent(final Event ev) {
        if (ev.getClass() == ConnectEvent.class) {
            final SocketChannel clientSocketChannel = (SocketChannel) ev.getSource();
            final Seance s = connectingSeances.remove(clientSocketChannel);
            if (s == null) {
                getTrace().put(EEventSeverity.ERROR, "Unable to find connecting seance for " + clientSocketChannel, null, null, EEventSource.UNIT_PORT.getValue(), false);
                closeChannel(clientSocketChannel);
                return;
            }
            final SeanceTaskClientQueueItem task = ((SeanceTaskClientQueueItem) s.listTasks().get(0));
            if (task == null) {
                getTrace().put(EEventSeverity.ERROR, "Unable to find queue item for " + s.sid, null, null, EEventSource.UNIT_PORT.getValue(), false);
                s.close(Seance.ECloseMode.SILENT);
                return;
            }
            if (ev.isExpired) {
                getTrace().put(EEventSeverity.ERROR, s.sid + ": connect timeout", null, null, EEventSource.UNIT_PORT.getValue(), false);
                if (task.isConnectSync() || task.isReceiveSync()) {
                    try {
                        task.getSapSeance().responseConnectTimeout();
                    } finally {
                        s.close(Seance.ECloseMode.SILENT);
                    }
                } else {
                    s.onConnectTimeout();
                }
            } else {
                try {
                    clientSocketChannel.finishConnect();
                    unit.getInstance().getResourceRegistry().unregister(unit.getInstance().getResourceRegistry().findByTarget(clientSocketChannel));
                    unit.getInstance().getResourceRegistry().register(new SocketResourceRegistryItem(ResourceRegistry.buildConnectedSocketChannelKey(getResourceKeyPrefix(), clientSocketChannel), clientSocketChannel, null, unit.getThisRunAliveChecker()));
                    task.getItem().markConnected(System.currentTimeMillis());
                    task.updateReceiveTimeout();
                    s.port.updateDescription();
                    getTrace().put(EEventSeverity.DEBUG, "Client port connected: " + s.port.getShortDescription() + "(SID: " + s.sid + ")", null, null, EEventSource.UNIT_PORT.getValue(), false);
                    s.open();
                    s.processTasks(true);
                } catch (Throwable e) {
                    getTrace().put(EEventSeverity.ERROR, "Client port connection establishment error: " + e, null, null, EEventSource.UNIT_PORT.getValue(), false);
                    s.close(Seance.ECloseMode.SILENT);
                }
            }
        } else {
            super.onEvent(ev);
        }
    }
}
