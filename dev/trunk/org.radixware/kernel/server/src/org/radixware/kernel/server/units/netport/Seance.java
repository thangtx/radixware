/*
 * Copyright (c) 2008-2017, Compass Plus Limited. All rights reserved.
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import org.radixware.kernel.common.enums.EClientAuthentication;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EHttpParameter;
import org.radixware.kernel.common.enums.EPortSecurityProtocol;
import org.radixware.kernel.common.exceptions.*;
import org.radixware.kernel.common.ssl.CertificateUtils;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.HttpFormatter;
import org.radixware.kernel.common.utils.Maps;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.net.SocketServerChannel;
import org.radixware.kernel.server.aio.ChannelPort.Frame;
import org.radixware.kernel.server.aio.Event;
import org.radixware.kernel.server.aio.EventDispatcher.ConnectEvent;
import org.radixware.kernel.server.aio.EventHandler;
import org.radixware.kernel.server.aio.SocketChannelPort;
import org.radixware.kernel.server.arte.services.aas.AasValueConverter;
import org.radixware.schemas.aas.InvokeRq;
import org.radixware.schemas.aas.InvokeRq.Parameters;
import org.radixware.schemas.aas.InvokeRs;
import org.radixware.schemas.aasWsdl.InvokeDocument;
import org.radixware.schemas.netporthandler.OnConnectRsDocument.OnConnectRs;
import org.radixware.schemas.netporthandler.OnConnectTimeoutRsDocument.OnConnectTimeoutRs;
import org.radixware.schemas.netporthandler.OnRecvRsDocument.OnRecvRs;
import org.radixware.schemas.netporthandler.OnRecvTimeoutRsDocument.OnRecvTimeoutRs;
import org.radixware.schemas.netporthandler.*;
import org.radixware.schemas.netporthandlerWsdl.ConnectDocument;
import org.radixware.schemas.netporthandlerWsdl.ProcessDocument;
import org.radixware.schemas.types.MapStrStr;

public abstract class Seance implements EventHandler, INetPortInvokeSource {

    protected static enum State {

        NEW,
        CONNECTING,
        INVOKING_HANDLER_ON_CONNECT,
        INVOKING_HANDLER_ON_CONNECT_TIMEOUT,
        RECEIVING,
        RECEIVING_SYNC,
        INVOKING_HANDLER_ON_RECV,
        INVOKING_HANDLER_ON_RECV_TIMEOUT,
        INVOKING_HANDLER_ON_DISCONNECT,
        CLOSED
    }

    static enum ECloseMode {

        SILENT,
        GENERATE_ON_DISCONNECT_EVENT
    }

    String sid;
    protected final NetPortHandlerUnit nphUnit;
    protected final NetChannel channel;
    protected final SocketChannelPort port;
    State state = State.NEW;
    protected NetPortCallbackTarget callbackTarget;
    protected String serverAddr = null;
    protected String clientAddr = null;
    protected final LinkedList<ISeanceTask> tasks = new LinkedList<>();
    protected final LinkedList<FramePacket> receivedPackets = new LinkedList<>();
    private final LocalTracer aasTracer;
    protected String remoteAddrPres;
    protected final NetChannelOptions options;
    private long inboundPacketSeq;
    private long outboundPacketSeq;
    private long closeTimeMillis = -1;
    private boolean disconnected = false;
    private boolean httpCloseRequested = false;
    private final long createTimeMillis = System.currentTimeMillis();

    Seance(
            final NetPortHandlerUnit unit,
            final NetChannel netChannel,
            final SocketChannel channel,
            final String recvFrameFormat,
            final String sendFrameFormat) throws IOException, SQLException {
        aasTracer = netChannel.getTrace().newTracer(EEventSource.AAS_CLIENT.getValue());
        channel.configureBlocking(false);
        options = netChannel.getOptions();

        this.port = new SocketChannelPort(unit.getDispatcher(), netChannel.getTrace().newTracer(EEventSource.UNIT_PORT.getValue()), channel, recvFrameFormat, sendFrameFormat);
        this.nphUnit = unit;
        this.channel = netChannel;

        state = State.NEW;
    }

    public long getCreateTimeMillis() {
        return createTimeMillis;
    }

    public void generateSid(final String remoteHost, final int remotePort) {
        sid = SidGen.newSid(channel.id, remoteHost, String.valueOf(remotePort));
    }

    public void addTask(final ISeanceTask task) {
        this.tasks.add(task);
    }

    public List<ISeanceTask> listTasks() {
        return new ArrayList<>(tasks);
    }

    protected long nextInboundSeq() {
        return ++inboundPacketSeq;
    }

    public String getSid() {
        return sid;
    }

    public NetChannel getChannel() {
        return channel;
    }
    
    protected long nextOutboundSeq() {
        return ++outboundPacketSeq;
    }

    protected long currentInboundSeq() {
        return inboundPacketSeq;
    }

    protected long currentOutboundSeq() {
        return outboundPacketSeq;
    }

    void cancelClose() {
        closeTimeMillis = -1;
    }

    final void close(final ECloseMode mode) {
        close(mode, -1);
    }

    public void maintenance() {
        if (!port.isConnected()
                && state != State.CLOSED
                && state != State.INVOKING_HANDLER_ON_DISCONNECT
                && state != State.CONNECTING) {
            close(ECloseMode.GENERATE_ON_DISCONNECT_EVENT);
        }
    }

    void close(final ECloseMode mode, final long closeDelaySec) {
        if (state == State.CLOSED) {
            return;
        }

        if (closeDelaySec > 0) {
            closeTimeMillis = System.currentTimeMillis() + 1000 * closeDelaySec;
            return;
        }

        terminateTasks();
        nphUnit.getSeances().unregisterSeance(this);
        nphUnit.getDispatcher().unsubscribe(new SocketChannelPort.DisconnectEvent(port));
        nphUnit.getDispatcher().unsubscribe(new SocketChannelPort.ReceiveEvent(port));
        try {
            port.close();
        } catch (Throwable ex) {
            //do nothing
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
        }
        if (mode == ECloseMode.GENERATE_ON_DISCONNECT_EVENT) {
            onDisconnect();
            return;
        }

        receivedPackets.clear();
        state = State.CLOSED;
        nphUnit.getNetPortHandlerMonitor().seanceClosed(this);
    }

    final void terminateTasks() {
        if (state == State.CLOSED) {
            return;
        }

        for (ISeanceTask task : tasks) {
            if (task.getSapSeance() != null) {
                try {
                    task.getSapSeance().response(new ServiceProcessServerFault(ExceptionEnum.SERVER_EXCEPTION.toString(), "Netport seance was unexpectedly closed", null, null));
                } catch (Throwable e) {
                    if (e instanceof InterruptedException || Thread.currentThread().isInterrupted()) {
                        return;
                    }
                    final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                    channel.getTrace().put(EEventSeverity.ERROR, remoteAddrPres + ": " + NetPortHandlerMessages.ERR_ON_SOCKET_IO + exStack, NetPortHandlerMessages.MLS_ID_ERR_ON_SOCKET_IO, new ArrStr(remoteAddrPres, channel.getTitle(), exStack), EEventSource.UNIT_PORT.getValue(), false);
                }
            }
        }

        tasks.clear();
    }

    void connecting() throws IOException, SQLException {
        if (state != State.NEW) {
            throw new IllegalUsageError("Seance is already opened");
        }
        callbackTarget = null;
        state = State.CONNECTING;
    }

    boolean isBusy() {
        return !tasks.isEmpty() || state != State.RECEIVING;
    }

    boolean isConnecting() {
        return state == State.CONNECTING;
    }

    void open() throws IOException, SQLException {
        if (state != State.NEW && state != State.CONNECTING) {
            throw new IllegalUsageError("Seance is already opened");
        }

        if (options.securityProtocol.isTls()) {
            SSLContext sslContext = null;
            EClientAuthentication clientAuthentication = options.clientAuthentication;
            try {
                sslContext = CertificateUtils.prepareServerSslContext(options.serverKeyAliases, options.clientCertAliases);
            } catch (CertificateUtilsException e) {
                throw new IOException(NetPortHandlerMessages.ERR_CANT_CREATE_SSLCONTEXT + e.toString(), e);
            }
            if (sslContext != null) {
                port.initSsl(sslContext, channel instanceof NetClient/*false*/, clientAuthentication, SocketServerChannel.SUITE_ANY_STRONG, options.securityProtocol);
            }
        }

        port.updateDescription();

        remoteAddrPres = sid;
        nphUnit.getDispatcher().waitEvent(new SocketChannelPort.DisconnectEvent(port), this, 0);
        port.startRead();
        startRecv();

        nphUnit.getSeances().registerSeance(this);
        callbackTarget = null;

        if (isListener()) {
            serverAddr = port.getChannel().socket().getLocalSocketAddress().toString();
            clientAddr = port.getChannel().socket().getRemoteSocketAddress().toString();
        } else {
            serverAddr = port.getChannel().socket().getRemoteSocketAddress().toString();
            clientAddr = port.getChannel().socket().getLocalSocketAddress().toString();
        }
        state = State.RECEIVING;
        nphUnit.getNetPortHandlerMonitor().seanceOpened(this);
    }

    private boolean invokingRecvOrRecvTimeoutHandler() {
        return state == State.INVOKING_HANDLER_ON_RECV || state == State.INVOKING_HANDLER_ON_RECV_TIMEOUT;
    }

    public boolean tryScheduledClose() {
        if (closeTimeMillis > 0 && System.currentTimeMillis() > closeTimeMillis && !isBusy()) {
            close(state == State.INVOKING_HANDLER_ON_DISCONNECT ? ECloseMode.SILENT : ECloseMode.GENERATE_ON_DISCONNECT_EVENT);
            return true;
        }
        return false;
    }

    public boolean tryCloseIfCallerDisconnected() {
        if (tasks != null && !tasks.isEmpty()) {
            final ISeanceTask task = tasks.get(0);
            if (task.getSapSeance() != null && task.getSapSeance().seance != null && !task.getSapSeance().seance.isConnected()) {
                trace(EEventSeverity.WARNING, "Closing '" + sid + "' due to caller disconnect during synchronous operation", false);
                close(ECloseMode.SILENT);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onEvent(final Event ev) {
        if (state == State.CLOSED) {
            return;
        }
        if (ev instanceof SocketChannelPort.DisconnectEvent && ((SocketChannelPort.DisconnectEvent) ev).getSource() == port) {
            if (invokingRecvOrRecvTimeoutHandler()) {
                disconnected = true;
            } else {
                close(state == State.INVOKING_HANDLER_ON_DISCONNECT ? ECloseMode.SILENT : ECloseMode.GENERATE_ON_DISCONNECT_EVENT);
            }
        } else if (ev instanceof SocketChannelPort.ReceiveEvent && ((SocketChannelPort.ReceiveEvent) ev).getSource() == port) {
            recvImpl((SocketChannelPort.ReceiveEvent) ev);
        } else {
            throw new IllegalUsageError("Invalid event " + ev);
        }
    }

    protected void onConnect() throws IOException {
        if (state == State.CLOSED) {
            return;
        }
        debug("Connected: " + port.getShortDescription(), false);
        if (channel.getOptions().isConnectReadyNtfOn) {
            getAasClientPool().invokeOnConnect(this, channel.id, serverAddr, clientAddr, port.getCertCn(), sid, channel.getAadcAffinity(this, null));
            state = State.INVOKING_HANDLER_ON_CONNECT;
        } else {
            state = State.RECEIVING;
            startRecv();
        }
    }

    protected void onConnectTimeout() {
        if (state == State.CLOSED) {
            return;
        }
        debug("Connect timeout", false);
        getAasClientPool().invokeOnConnectTimeout(this, channel.id, serverAddr, clientAddr, port.getCertCn(), sid, callbackTarget == null ? null : callbackTarget.getCallbackPid(), callbackTarget == null ? null : callbackTarget.getCallbackWid(), null);
        state = State.INVOKING_HANDLER_ON_CONNECT_TIMEOUT;
    }

    public void onAasInvokeException(final ServiceClientException exception) {
        if (state == State.CLOSED) {
            return;
        }
        String exStack;
        if (exception instanceof ServiceCallFault) {
            exStack = ((ServiceCallFault) exception).getCauseExMessage() + ":\n" + ((ServiceCallFault) exception).getCauseExStack();
        } else {
            exStack = ExceptionTextFormatter.exceptionStackToString(exception);
        }
        if (state == State.INVOKING_HANDLER_ON_RECV) {
            channel.afterInvokeOnRecv(this);
        }
        channel.getTrace().put(EEventSeverity.ERROR, remoteAddrPres + ": " + NetPortHandlerMessages.ERR_ON_NET_PORT_RQ_PROC + exStack, NetPortHandlerMessages.MLS_ID_ERR_ON_NET_PORT_RQ_PROC, new ArrStr(remoteAddrPres, channel.getTitle(), exStack), EEventSource.UNIT_PORT.getValue(), false);
        close(state == State.INVOKING_HANDLER_ON_DISCONNECT ? ECloseMode.SILENT : ECloseMode.GENERATE_ON_DISCONNECT_EVENT);
    }

    public void onAasInvokeResponse(final InvokeRs rs) {
        if (state == State.CLOSED) {
            return;
        }
        if (channel.getTrace().getMinSeverity(EEventSource.UNIT_PORT) <= EEventSeverity.DEBUG.getValue().longValue()) {
            debug("AAS response: " + rs.xmlText(), true);
        }
        try {
            switch (state) {
                case INVOKING_HANDLER_ON_CONNECT:
                    onConnectResp((OnConnectRs) AasValueConverter.aasXmlVal2ObjVal(null, rs.getReturnValue()));
                    break;
                case INVOKING_HANDLER_ON_CONNECT_TIMEOUT:
                    onConnectTimeoutResp((OnConnectTimeoutRs) AasValueConverter.aasXmlVal2ObjVal(null, rs.getReturnValue()));
                    break;
                case INVOKING_HANDLER_ON_RECV:
                    onRecvResp((OnRecvRs) AasValueConverter.aasXmlVal2ObjVal(null, rs.getReturnValue()));
                    break;
                case INVOKING_HANDLER_ON_RECV_TIMEOUT:
                    onRecvTimeoutResp((OnRecvTimeoutRs) AasValueConverter.aasXmlVal2ObjVal(null, rs.getReturnValue()));
                    break;
                case INVOKING_HANDLER_ON_DISCONNECT:
                    close(ECloseMode.SILENT);
                    break;
                default:
                    throw new RadixError("Unexpected response: " + rs.xmlText());
            }
            if (channel.getOptions().isSyncMode) {
                channel.getUnit().getNetPortHandlerMonitor().updateBusySeancesCount(channel);
            }
        } catch (Throwable e) {
            if (e instanceof InterruptedException || Thread.currentThread().isInterrupted()) {
                return;
            }
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            channel.getTrace().put(EEventSeverity.ERROR, remoteAddrPres + ": " + exStack, null, null, EEventSource.UNIT_PORT.getValue(), false);
            close(ECloseMode.GENERATE_ON_DISCONNECT_EVENT);
        }
    }

    private void onRecvResp(final OnRecvRs rs) throws IOException, SQLException {
        channel.afterInvokeOnRecv(this);

        if (state == State.CLOSED) {
            return;
        }
        if (disconnected) {
            close(ECloseMode.GENERATE_ON_DISCONNECT_EVENT, -1);
            return;
        }
        if (rs.isSetUndelivered() && rs.getUndelivered() != null && rs.getUndelivered()) {
            callbackTarget = null;
            getTracer().put(EEventSeverity.WARNING, "onRecvRs contains UNDELIVERED flag which is no longer supported", null, null, false);
            state = State.RECEIVING;
            return;
        }
        callbackTarget = new NetPortCallbackTarget(rs.getCallbackPid(), rs.getCallbackWid());
        final Long closeDelaySec = rs.isSetCloseDelay() ? rs.getCloseDelay() : null;
        final boolean isSetPacket = isSetSendPacket(rs.getSendPacket(), rs.getSendPackedHeaders());
        if (isSetPacket) {
            send(new FramePacket(String.valueOf(nextOutboundSeq()), rs.getSendPacket(), rs.getSendPackedHeaders()), closeDelaySec, rs.getRecvTimeout());
            if (state == State.INVOKING_HANDLER_ON_RECV) //is not modified by send()
            {
                state = State.RECEIVING;
            }
        } else {
            state = State.RECEIVING;
            if (closeDelaySec != null) {
                close(ECloseMode.GENERATE_ON_DISCONNECT_EVENT, closeDelaySec);
            }
        }
        if (httpCloseRequested) {
            close(ECloseMode.GENERATE_ON_DISCONNECT_EVENT);
        } else if (state == State.RECEIVING) {
            if (receivedPackets.isEmpty()) {
                if (!isSetPacket) //recv timeout is already set in send 
                {
                    startRecv(rs.getRecvTimeout());
                }
            } else {
                onRecv();
            }
        }
    }

    private void onRecvTimeoutResp(final OnRecvTimeoutRs rs) throws IOException, SQLException {
        if (state == State.CLOSED) {
            return;
        }

        if (disconnected) {
            close(ECloseMode.GENERATE_ON_DISCONNECT_EVENT, -1);
            return;
        }

        final boolean isSetPacket = rs == null ? false : isSetSendPacket(rs.getSendPacket(), rs.getSendPackedHeaders());

        if (rs != null) {
            callbackTarget = new NetPortCallbackTarget(rs.getCallbackPid(), rs.getCallbackWid());

            final Long closeDelaySec = rs.isSetCloseDelay() ? rs.getCloseDelay() : null;

            if (isSetPacket) {
                send(new FramePacket(String.valueOf(nextOutboundSeq()), rs.getSendPacket(), rs.getSendPackedHeaders()), closeDelaySec, rs.getRecvTimeout());
                if (state == State.INVOKING_HANDLER_ON_RECV_TIMEOUT) //is not modified by send()
                {
                    state = State.RECEIVING;
                }
            } else {
                state = State.RECEIVING;
                if (closeDelaySec != null) {
                    close(ECloseMode.GENERATE_ON_DISCONNECT_EVENT, closeDelaySec);
                }
            }
        } else {
            state = state = State.RECEIVING;
        }
        if (state == State.RECEIVING) {
            if (receivedPackets.isEmpty()) {
                if (!isSetPacket) //recv timeout is already set in send                 
                {
                    startRecv(rs == null ? -1 : rs.getRecvTimeout());
                }
            } else {
                onRecv();
            }
        }
    }

    protected void onConnectResp(final OnConnectRs rs) throws IOException, SQLException {
        if (state == State.CLOSED) {
            return;
        }

        callbackTarget = new NetPortCallbackTarget(rs.getCallbackPid(), rs.getCallbackWid());

        final Long closeDelaySec = rs.isSetCloseDelay() ? rs.getCloseDelay() : null;
        final boolean isSetPacket = isSetSendPacket(rs.getSendPacket(), rs.getSendPackedHeaders());
        if (isSetPacket) {
            send(new FramePacket(String.valueOf(nextOutboundSeq()), rs.getSendPacket(), rs.getSendPackedHeaders()), closeDelaySec, rs.getRecvTimeout());
            if (state == State.INVOKING_HANDLER_ON_CONNECT) //is not modified by send()
            {
                state = State.RECEIVING;
            }
        } else {
            state = State.RECEIVING;
            if (closeDelaySec != null) {
                close(ECloseMode.GENERATE_ON_DISCONNECT_EVENT, closeDelaySec);
            }
        }
        if (state == State.RECEIVING) {
            if (receivedPackets.isEmpty()) {
                if (!isSetPacket) //recv timeout is already set in send
                {
                    startRecv(rs.getRecvTimeout());
                }
            } else {
                onRecv();
            }
        }
    }

    protected void onConnectTimeoutResp(final OnConnectTimeoutRs rs) throws IOException, SQLException {
        if (state == State.CLOSED) {
            return;
        }

        final Long connectTimeoutSec = rs.isSetConnectTimeout() ? rs.getConnectTimeout() : null;
        if (connectTimeoutSec != null) {
            channel.getTrace().put(EEventSeverity.EVENT, "Client port waits for connect", null, null, EEventSource.UNIT_PORT.getValue(), false);
            if (connectTimeoutSec > 0) {
                nphUnit.getDispatcher().waitEvent(new ConnectEvent(port.getChannel()), this, System.currentTimeMillis() + connectTimeoutSec * 1000);
            } else {
                nphUnit.getDispatcher().waitEvent(new ConnectEvent(port.getChannel()), this, -1);
            }
        } else {
            close(ECloseMode.GENERATE_ON_DISCONNECT_EVENT);
        }
    }

    void send(final FramePacket sendPacket, final Long closeDelaySec, final Long recvTimeoutSec) {
        if (state == State.NEW || state == State.CLOSED) {
            throw new IllegalUsageError("Wrong state: " + state);
        }
        debug("sending packet #" + sendPacket.id + " to " + port.getShortDescription(), false);
        if (channel.getTrace().getMinSeverity() == EEventSeverity.DEBUG.getValue().longValue()) {
            debug("sending packet #" + sendPacket.id + " body: " + new String(sendPacket.packet != null ? sendPacket.packet : new byte[]{}), true);
        }
        if (sendPacket.packet == null && sendPacket.headers != null && !sendPacket.headers.isEmpty()) {
            sendPacket.packet = new byte[]{};
        }
        try {
            sendImpl(sendPacket, closeDelaySec, recvTimeoutSec);
        } catch (Throwable e) {
            if (e instanceof InterruptedException || Thread.currentThread().isInterrupted()) {
                return;
            }
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            channel.getTrace().put(EEventSeverity.ERROR, remoteAddrPres + ": " + NetPortHandlerMessages.ERR_ON_SOCKET_IO + exStack, NetPortHandlerMessages.MLS_ID_ERR_ON_SOCKET_IO, new ArrStr(remoteAddrPres, channel.getTitle(), exStack), EEventSource.UNIT_PORT.getValue(), false);
            close(ECloseMode.GENERATE_ON_DISCONNECT_EVENT);
        }
    }

    private void logPacketReceived(final FramePacket framePacket) {
        if (channel.getTrace().getMinSeverity() == EEventSeverity.DEBUG.getValue().longValue()) {
            debug("received packet #" + framePacket.id + " from " + port.getShortDescription(), false);
        }
    }

    protected void recvImpl(final SocketChannelPort.ReceiveEvent ev) {
        boolean closeRequestedByRemote = false;
        if (ev.isExpired) {
            channel.getTrace().newTracer(EEventSource.NET_PORT_HANDLER.getValue()).put(EEventSeverity.WARNING, sid + ": " + NetPortHandlerMessages.RECV_TIMEOUT, NetPortHandlerMessages.MLS_ID_RECV_TIMEOUT, new ArrStr(sid), false);
        } else {
            if (ev.frame != null && ev.frame.attrs != null && "close".equalsIgnoreCase(ev.frame.attrs.get(EHttpParameter.HTTP_CONNECTION_ATTR.getValue()))) {
                closeRequestedByRemote = true;
            }
        }
        if (state == State.RECEIVING_SYNC) {
            final ISeanceTask task = tasks.pollFirst();
            if (ev.isExpired) {
                task.getSapSeance().responseReceiveTimeout();
            } else {
                final FramePacket receivedPacket = new FramePacket(String.valueOf(nextInboundSeq()), ev.frame);
                logPacketReceived(receivedPacket);
                if (task.getOperation() == EChannelOperation.PROCESS) {
                    final ProcessDocument rsDoc = ProcessDocument.Factory.newInstance();
                    final ProcessRs rs = rsDoc.addNewProcess().addNewProcessRs();
                    rs.setReceivedPacket(receivedPacket.packet);
                    rs.setReceivedPackedHeaders(Maps.toXml(receivedPacket.headers));
                    task.getSapSeance().response(rsDoc);
                } else if (task.getOperation() == EChannelOperation.CONNECT) {
                    final ConnectDocument rsDoc = ConnectDocument.Factory.newInstance();
                    final ConnectRs rs = rsDoc.addNewConnect().addNewConnectRs();
                    rs.setSID(sid);
                    rs.setReceivedPacket(receivedPacket.packet);
                    rs.setReceivedPackedHeaders(Maps.toXml(receivedPacket.headers));

                    task.getSapSeance().response(rsDoc);
                }
            }
            if (closeRequestedByRemote) {
                close(ECloseMode.GENERATE_ON_DISCONNECT_EVENT);
            } else {
                state = State.RECEIVING;
                if (task.getCloseDelaySec() != null) {
                    close(ECloseMode.GENERATE_ON_DISCONNECT_EVENT, task.getCloseDelaySec());
                }
                if (state == State.RECEIVING) {//if state was not changed by close
                    processTasks(false);
                }
            }
        } else {
            if (ev.isExpired) {
                onRecvTimeout();
            } else {
                receivedPackets.add(new FramePacket(String.valueOf(nextInboundSeq()), ev.frame));
                logPacketReceived(receivedPackets.getLast());
                if (closeRequestedByRemote) {
                    httpCloseRequested = true;
                    getTracer().debug("'close' header is present in packet", false);
                }
                onRecv();
            }
        }
        if (channel.getOptions().isSyncMode) {
            channel.getUnit().getNetPortHandlerMonitor().updateBusySeancesCount(channel);
        }
    }

    protected void sendImpl(final FramePacket sendPacket, final Long closeDelaySec, final Long recvTimeoutSec) throws IOException {
        if (port.getChannel() == null) {
            throw new IOException("Channel is allready closed");
        }
        port.send(sendPacket.packet, prepareHeaders(sendPacket.headers, closeDelaySec));
        if (closeDelaySec != null) {
            close(ECloseMode.GENERATE_ON_DISCONNECT_EVENT, closeDelaySec);
        }
        if (closeDelaySec == null || closeDelaySec > 0) {
            startRecv(recvTimeoutSec);
        }
        if (channel.getOptions().isSyncMode) {
            channel.getUnit().getNetPortHandlerMonitor().updateBusySeancesCount(channel);
        }
    }

    private Map<String, String> prepareHeaders(final Map<String, String> headers, final Long closeDelaySec) {
        if (channel.getOptions().isOutHttp && (closeDelaySec == null || closeDelaySec > 0) && (headers == null || !headers.containsKey(EHttpParameter.HTTP_CONNECTION_ATTR.getValue()))) {
            Map<String, String> map = headers;
            if (map == null) {
                map = new HashMap<>();
            }
            map.put(EHttpParameter.HTTP_CONNECTION_ATTR.getValue(), "keep-alive");
            return map;
        }
        return headers;
    }

    protected final void startRecv() {
        startRecv(Long.valueOf(-1));
    }

    protected final void startRecv(Long recvTimeoutSec) {
        final Event ev = new SocketChannelPort.ReceiveEvent(port);
        nphUnit.getDispatcher().unsubscribe(ev);
        if (recvTimeoutSec != null) {
            nphUnit.getDispatcher().waitEvent(ev, this, recvTimeoutSec >= 0 ? System.currentTimeMillis() + recvTimeoutSec.longValue() * 1000 : -1);
            debug("receiving" + (recvTimeoutSec < 0 ? " (no timeout)" : " (timeout = " + recvTimeoutSec + " sec)"), false);
        } else {
            nphUnit.getDispatcher().waitEvent(ev, this, -1);
            debug("receiving (no timeout)", false);
        }
    }

    protected void logPacketBody(final FramePacket framePacket) {
        if (channel.getTrace().getMinSeverity() == EEventSeverity.DEBUG.getValue().longValue()) {
            debug("packet # " + framePacket.id + " body: " + new String(framePacket.packet), true);
        }
    }

    protected void onRecv() {
        startRecv();
        cancelClose();
        if (state != State.RECEIVING) {
            return;
        }
        if (receivedPackets.isEmpty()) {
            throw new IllegalUsageError("No received packets");
        }

        final FramePacket next = receivedPackets.removeFirst();

        final byte[] packet = next.packet;

        channel.beforInvokeOnRecv(this);
        debug("processing inbound packet #" + next.id, false);
        getAasClientPool().invokeOnRecv(
                this,
                getHeadersForAasInvoke(next.headers),
                channel.id,
                serverAddr,
                clientAddr,
                port.getCertCn(),
                sid,
                callbackTarget == null ? null : callbackTarget.getCallbackPid(),
                callbackTarget == null ? null : callbackTarget.getCallbackWid(),
                Maps.toXml(next.headers),
                packet,
                port.isConnected(),
                channel.getAadcAffinity(this, next));
        state = State.INVOKING_HANDLER_ON_RECV;
    }

    private Map<String, String> getHeadersForAasInvoke(final Map<String, String> receivedHeaders) {
        if (receivedHeaders == null || receivedHeaders.isEmpty() || System.getProperties().containsKey("radix.nph.seance.no.headers.copy")) {
            return null;
        }
        final int radixPriority = HttpFormatter.getRadixPriorityFromHeaders(receivedHeaders);
        if (radixPriority >= 0) {
            return HttpFormatter.appendPriorityHeaderAttr(null, radixPriority);
        }
        return null;
    }

    protected void onRecvTimeout() {
        if (state != State.RECEIVING) {
            return;
        }
        startRecv();
        cancelClose();
        debug("receive timeout", false);
        getAasClientPool().invokeOnRecvTimeout(
                this,
                channel.id,
                serverAddr,
                clientAddr,
                port.getCertCn(),
                sid,
                callbackTarget == null ? null : callbackTarget.getCallbackPid(),
                callbackTarget == null ? null : callbackTarget.getCallbackWid(),
                channel.getAadcAffinity(this, null));
        state = State.INVOKING_HANDLER_ON_RECV_TIMEOUT;
    }

    private void onDisconnect() {
        if (state == State.CLOSED) {
            return;
        }
        if (channel.getOptions().isDisconnectNtfOn) {
            debug("disconnected", false);
            state = State.INVOKING_HANDLER_ON_DISCONNECT;
            getAasClientPool().invokeOnDisconnect(
                    this,
                    channel.id,
                    serverAddr,
                    clientAddr,
                    port.getCertCn(),
                    sid,
                    callbackTarget == null ? null : callbackTarget.getCallbackPid(),
                    callbackTarget == null ? null : callbackTarget.getCallbackWid(),
                    channel.getAadcAffinity(this, null));
            state = State.INVOKING_HANDLER_ON_DISCONNECT;
        } else {
            close(ECloseMode.SILENT);
        }
    }

    final void processTasks(boolean justConnected) {
        if (state != State.RECEIVING) {
            return;
        }

        while (!tasks.isEmpty()) {
            final ISeanceTask task = tasks.getFirst();

            if (task.getSapSeance() != null && !task.getSapSeance().seance.isConnected()) {
                nphUnit.getTrace().put(EEventSeverity.ERROR, "Error while processing next task (" + task.getOperation() + ") for " + sid + ": seance is allready closed", null, null, EEventSource.UNIT_PORT.getValue(), false);
                tasks.removeFirst();
                continue;
            }

            cancelClose();

            try {
                //in case of sync recv close delay should be applied after receiving response
                Long effectiveCloseDelaySec = task.isReceiveSync() ? null : task.getCloseDelaySec();
                if (task.getOperation() == EChannelOperation.PROCESS) {
                    callbackTarget = task.getCallbackTarget();
                    if (isSetSendPacket(task.getSendPacket(), task.getSendPacketHeaders())) {
                        send(new FramePacket(String.valueOf(nextOutboundSeq()), task.getSendPacket(), task.getSendPacketHeaders()), effectiveCloseDelaySec, task.getReceiveTimeoutSec());
                    } else {
                        if (task.getCloseDelaySec() != null) {
                            close(Seance.ECloseMode.GENERATE_ON_DISCONNECT_EVENT, task.getCloseDelaySec());
                        }
                    }

                    if (task.isReceiveSync()) {
                        state = State.RECEIVING_SYNC;
                        return;
                    }
                    final ProcessDocument rsDoc = ProcessDocument.Factory.newInstance();
                    rsDoc.addNewProcess().addNewProcessRs();
                    task.getSapSeance().response(rsDoc);
                } else if (task.getOperation() == EChannelOperation.CONNECT) {
                    callbackTarget = task.getCallbackTarget();
                    if (isSetSendPacket(task.getSendPacket(), task.getSendPacketHeaders())) {
                        send(new FramePacket(String.valueOf(nextOutboundSeq()), task.getSendPacket(), task.getSendPacketHeaders()), effectiveCloseDelaySec, task.getReceiveTimeoutSec());
                    } else {
                        if (task.getCloseDelaySec() != null) {
                            close(Seance.ECloseMode.GENERATE_ON_DISCONNECT_EVENT, task.getCloseDelaySec());
                        }
                    }

                    if (task.isReceiveSync()) {
                        state = State.RECEIVING_SYNC;
                        return;
                    }

                    if (!task.isConnectSync() && justConnected) {
                        tasks.removeFirst();
                        onConnect();
                        return;
                    }

                    final ConnectDocument rsDoc = ConnectDocument.Factory.newInstance();
                    final ConnectRs rs = rsDoc.addNewConnect().addNewConnectRs();
                    rs.setSID(sid);
                    task.getSapSeance().response(rsDoc);
                }
            } catch (Throwable e) {
                task.getSapSeance().response(e);
            }
            tasks.removeFirst();
        }
    }

    private NetPortAasClientPool getAasClientPool() {
        return nphUnit.getAasClientsPool();
    }

    public String getConnectionDesc() {
        return remoteAddrPres;
    }

    protected void debug(final String mess, final boolean isSensitive) {
        trace(EEventSeverity.DEBUG, remoteAddrPres + ": " + mess, isSensitive);
    }

    protected void error(final String mess, final boolean isSensitive) {
        trace(EEventSeverity.ERROR, mess, isSensitive);
    }

    protected void trace(final EEventSeverity severity, final String mess, final boolean isSensitive) {
        trace(severity, mess, null, null, EEventSource.UNIT_PORT.getValue(), isSensitive);
    }

    protected void trace(final EEventSeverity severity, final String mess, final String code, final List<String> words, final String eventSource, final boolean isSensitive) {
        channel.getTrace().put(severity, mess, code, words, eventSource, isSensitive);
    }

    protected final boolean isListener() {
        return options.isListener;
    }

    protected boolean isClient() {
        return !options.isListener;
    }

    public SocketChannelPort getPort() {
        return port;
    }

    @Override
    public NetPortHandlerUnit getUnit() {
        return nphUnit;
    }

    private boolean isSetSendPacket(final byte[] sendPacket, MapStrStr sendPacketHeaders) {
        return (sendPacket != null) || (sendPacket == null && sendPacketHeaders != null && !sendPacketHeaders.getEntryList().isEmpty());
    }

    @Override
    public String getInvokeSourceDesc() {
        return getConnectionDesc();
    }

    @Override
    public LocalTracer getTracer() {
        return aasTracer;
    }

    static class FramePacket {

        public byte packet[] = null;
        public Map<String, String> headers = null;
        public final String id;

        public FramePacket(String id) {
            this.id = id;
        }

        public FramePacket(final String id, final Frame frame) {
            this(id);
            this.packet = frame.packet;
            this.headers = frame.attrs;
        }

        public FramePacket(final String id, final byte[] packet, final Map<String, String> headers) {
            this(id);
            this.packet = packet;
            this.headers = headers;
        }

        public FramePacket(final String id, final byte[] packet, final MapStrStr xHeaders) {
            this(id);
            this.packet = packet;
            this.headers = Maps.fromXml(xHeaders);
        }
    }

    private static final class SidGen {

        private static final AtomicLong seq = new AtomicLong();

        static String newSid(final long channelId, final String host, final String port) {
            final String datePart = new SimpleDateFormat("MMddHHmmss").format(new Date());
            return channelId + "-" + datePart + "-" + String.valueOf(seq.incrementAndGet()) + "-" + host + "-" + port;
        }
    }
}
