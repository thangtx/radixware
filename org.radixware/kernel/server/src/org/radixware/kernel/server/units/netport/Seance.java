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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.enums.EClientAuthentication;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EPortSecurityProtocol;
import org.radixware.kernel.common.exceptions.*;
import org.radixware.kernel.common.ssl.CertificateUtils;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.HttpFormatter;
import org.radixware.kernel.common.utils.Maps;
import org.radixware.kernel.common.utils.net.SocketServerChannel;
import org.radixware.kernel.server.aio.ChannelPort.Frame;
import org.radixware.kernel.server.aio.Event;
import org.radixware.kernel.server.aio.EventDispatcher.ConnectEvent;
import org.radixware.kernel.server.aio.EventHandler;
import org.radixware.kernel.server.aio.ServiceServerSeance;
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

public abstract class Seance implements EventHandler {

    private static final Id NET_CHANNEL_CLASS_ID = Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM");
    private static final String ON_CONNECT_METHOD = "mthI5YABQPZBVGDBIRUZ4DGWHYKKY";
    private static final String ON_CONNECT_TIMEOUT_METHOD = "mthFR5YPKF6Z5GRNJDWHMETZW4NY4";
    private static final String ON_RECV_METHOD = "mthDPLQGAWSPVG2HDLYDVRGKFWPZ4";
    private static final String ON_RECV_TIMEOUT_METHOD = "mthUEI3NFHBGNHSHMPJ33RIRMAZKI";
    private static final String ON_DISCONNECT_METHOD = "mth6RAQH773VRD7VGRLMWFGKG4IAA";

    //options
    protected static enum State {

        NEW,
        CONNECTING,
        //CONNECTING_SYNC,
        INVOKING_HANDLER_ON_CONNECT,
        INVOKING_HANDLER_ON_CONNECT_TIMEOUT,
        RECEIVING,
        RECEIVING_SYNC,
        INVOKING_HANDLER_ON_RECV,
        INVOKING_HANDLER_ON_RECV_TIMEOUT,
        INVOKING_HANDLER_ON_DISCONNECT,
        CLOSED
    }

    final String sid;
    protected final NetPortHandlerUnit nphUnit;
    protected final NetChannel channel;
    protected final SocketChannelPort port;
    State state = State.NEW;
    protected String callbackPid = null;
    protected String callbackWid = null;
    protected String serverAddr = null;
    protected String clientAddr = null;
    protected final LinkedList<SapSeance> sapSeances = new LinkedList<SapSeance>();
    private byte[] undeliveredReceivedPacket = null;
    protected final LinkedList<FramePacket> receivedPackets = new LinkedList<FramePacket>();
    private final LocalTracer aasTracer;
    protected String remoteAddrPres;
    protected final NetChannelOptions options;
    private long inboundPacketSeq;
    private long outboundPacketSeq;

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

        final InetSocketAddress addr = (InetSocketAddress) (netChannel.getOptions().isListener ? channel.getRemoteAddress() : channel.getLocalAddress());

        sid = SidGen.newSid(netChannel.id, String.valueOf(addr.getAddress()), String.valueOf(addr.getPort()));
        state = State.NEW;
    }

    protected long nextInboundSeq() {
        return ++inboundPacketSeq;
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

    private static String getRemoteSocketPres(final SocketChannel channel) {
        if (channel.isConnected()) {
            return NetPortHandlerMessages.CONNECTION_WITH + String.valueOf(channel.socket().getRemoteSocketAddress());
        } else {
            return NetPortHandlerMessages.CONNECTION_WITH + "???";
        }
    }

    final byte[] popUndeliveredReceivedPacket() {
        final byte[] res = undeliveredReceivedPacket;
        undeliveredReceivedPacket = null;
        return res;
    }

    final void pushUndeliveredReceivedPacket(final byte[] packet) {
        if (undeliveredReceivedPacket != null) {
            throw new IllegalUsageError("Previous undelivered packet is not processed");
        }
        undeliveredReceivedPacket = packet;
    }

    static enum ECloseMode {

        SILENT,
        GENERATE_ON_DISCONNECT_EVENT
    }

    void cancelClose() {
        nphUnit.getDispatcher().unsubscribe(new DisconnectRequest(port));
    }

    final void close(final ECloseMode mode) {
        close(mode, -1);
    }

    void close(final ECloseMode mode, final long closeDelaySec) {
        if (state == State.CLOSED) {
            return;
        }

        final Event ev = new DisconnectRequest(port);
        nphUnit.getDispatcher().unsubscribe(ev);
        if (closeDelaySec > 0) {
            nphUnit.getDispatcher().waitEvent(ev, this, System.currentTimeMillis() + closeDelaySec * 1000);
            return;
        }

        // Close all SAP seances
        closeSapSeances();
        undeliveredReceivedPacket = null;
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

    void connecting() throws IOException, SQLException {
        if (state != State.NEW) {
            throw new IllegalUsageError("Seance is already opened");
        }

        nphUnit.getSeances().registerSeance(this);
        callbackPid = null;
        callbackWid = null;
        state = State.CONNECTING;
    }

    void open() throws IOException, SQLException {
        if (state != State.NEW && state != State.CONNECTING) {
            throw new IllegalUsageError("Seance is already opened");
        }

        if (options.securityProtocol == EPortSecurityProtocol.SSL) {
            SSLContext sslContext = null;
            EClientAuthentication clientAuthentication = options.clientAuthentication;
            try {
                sslContext = CertificateUtils.prepareServerSslContext(options.serverKeyAliases, options.clientCertAliases);
            } catch (CertificateUtilsException e) {
                throw new IOException(NetPortHandlerMessages.ERR_CANT_CREATE_SSLCONTEXT + e.toString(), e);
            }
            if (sslContext != null) {
                port.initSsl(sslContext, channel instanceof NetClient/*false*/, clientAuthentication, SocketServerChannel.SUITE_ANY_STRONG);
            }
        }

        remoteAddrPres = sid;
        nphUnit.getDispatcher().waitEvent(new SocketChannelPort.DisconnectEvent(port), this, 0);
        port.startRead();
        startRecv();

        nphUnit.getSeances().registerSeance(this);
        callbackPid = null;
        callbackWid = null;

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

    @Override
    public void onEvent(final Event ev) {
        if (state == State.CLOSED) {
            return;
        }
        if (ev instanceof DisconnectRequest && ((DisconnectRequest) ev).getSource() == port) {
            close(state == State.INVOKING_HANDLER_ON_DISCONNECT ? ECloseMode.SILENT : ECloseMode.GENERATE_ON_DISCONNECT_EVENT);
        } else if (ev instanceof SocketChannelPort.DisconnectEvent && ((SocketChannelPort.DisconnectEvent) ev).getSource() == port) {
            close(state == State.INVOKING_HANDLER_ON_DISCONNECT ? ECloseMode.SILENT : ECloseMode.GENERATE_ON_DISCONNECT_EVENT);
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
        debug("Connected", false);
        if (channel.getOptions().isConnectReadyNtfOn) {
            final InvokeDocument rq = InvokeDocument.Factory.newInstance();
            final InvokeRq invXml = rq.addNewInvoke().addNewInvokeRq();
            invXml.setClassId(NET_CHANNEL_CLASS_ID);
            invXml.setMethodId(ON_CONNECT_METHOD);
            final Parameters paramsXml = invXml.addNewParameters();
            paramsXml.addNewItem().setInt(channel.id);
            paramsXml.addNewItem().setStr(serverAddr);
            paramsXml.addNewItem().setStr(clientAddr);
            paramsXml.addNewItem().setStr(port.getCertCn());
            paramsXml.addNewItem().setStr(sid);
            getAasClientPool().invoke(this, rq, null, true);
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
        final InvokeDocument rq = InvokeDocument.Factory.newInstance();
        final InvokeRq invXml = rq.addNewInvoke().addNewInvokeRq();
        invXml.setClassId(NET_CHANNEL_CLASS_ID);
        invXml.setMethodId(ON_CONNECT_TIMEOUT_METHOD);
        final Parameters paramsXml = invXml.addNewParameters();
        paramsXml.addNewItem().setInt(channel.id);
        paramsXml.addNewItem().setStr(serverAddr);
        paramsXml.addNewItem().setStr(clientAddr);
        paramsXml.addNewItem().setStr(port.getCertCn());
        paramsXml.addNewItem().setStr(sid);
        paramsXml.addNewItem().setStr(callbackPid);
        paramsXml.addNewItem().setStr(callbackWid);
        getAasClientPool().invoke(this, rq, null, true);
        debug("Connect timeout", false);
        state = State.INVOKING_HANDLER_ON_CONNECT_TIMEOUT;
    }

    protected void onAasInvokeException(final ServiceClientException exception) {
        if (state == State.CLOSED) {
            return;
        }
        String exStack;
        if (exception instanceof ServiceCallFault) {
            exStack = ((ServiceCallFault) exception).getCauseExMessage() + ":\n" + ((ServiceCallFault) exception).getCauseExStack();
        } else {
            exStack = ExceptionTextFormatter.exceptionStackToString(exception);
        }

        channel.getTrace().put(EEventSeverity.ERROR, remoteAddrPres + ": " + NetPortHandlerMessages.ERR_ON_NET_PORT_RQ_PROC + exStack, NetPortHandlerMessages.MLS_ID_ERR_ON_NET_PORT_RQ_PROC, new ArrStr(remoteAddrPres, channel.getTitle(), exStack), EEventSource.UNIT_PORT.getValue(), false);
        close(state == State.INVOKING_HANDLER_ON_DISCONNECT ? ECloseMode.SILENT : ECloseMode.GENERATE_ON_DISCONNECT_EVENT);
    }

    protected void onAasInvokeResponse(final InvokeRs rs) {
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
        if (state == State.CLOSED) {
            return;
        }
        if (rs.isSetUndelivered() && rs.getUndelivered() != null && rs.getUndelivered().booleanValue()) {
            callbackPid = null;
            callbackWid = null;
            pushUndeliveredReceivedPacket(receivedPackets.get(0).packet);
            receivedPackets.remove(0);//??????????????? ?????????????????????
            state = State.RECEIVING;
            return;
        }
        receivedPackets.remove(0);//??????????????? ???????????????????????????
        callbackPid = rs.getCallbackPid();
        callbackWid = rs.getCallbackWid();

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

    private void onRecvTimeoutResp(final OnRecvTimeoutRs rs) throws IOException, SQLException {
        if (state == State.CLOSED || rs == null) {
            return;
        }
        callbackPid = rs.getCallbackPid();
        callbackWid = rs.getCallbackWid();

        final Long closeDelaySec = rs.isSetCloseDelay() ? rs.getCloseDelay() : null;
        final boolean isSetPacket = isSetSendPacket(rs.getSendPacket(), rs.getSendPackedHeaders());
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

    protected void onConnectResp(final OnConnectRs rs) throws IOException, SQLException {
        //??????????????????????????? ??? Pos2Seance, ?????????????????????????????? ??????????????????????????????????????? ??????????????? ??????????????????
        if (state == State.CLOSED) {
            return;
        }
        callbackPid = rs.getCallbackPid();
        callbackWid = rs.getCallbackWid();

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
        debug("sending packet #" + sendPacket.id + " to connection", false);
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
            debug("received packet #" + framePacket.id, false);
        }
    }

    protected void recvImpl(final SocketChannelPort.ReceiveEvent ev) {
        if (ev.isExpired) {
            channel.getTrace().newTracer(EEventSource.NET_PORT_HANDLER.getValue()).put(EEventSeverity.WARNING, NetPortHandlerMessages.RECV_TIMEOUT, NetPortHandlerMessages.MLS_ID_RECV_TIMEOUT, null, false);
        }
        if (state == State.RECEIVING_SYNC) {
            final SapSeance seance = sapSeances.pollFirst();
            if (ev.isExpired) {
                seance.responseReceiveTimeout();
            } else {
                final FramePacket receivedPacket = new FramePacket(String.valueOf(nextInboundSeq()), ev.frame);
                logPacketReceived(receivedPacket);
                if (seance.rqDoc instanceof ProcessMess) {
                    final ProcessDocument rsDoc = ProcessDocument.Factory.newInstance();
                    final ProcessRs rs = rsDoc.addNewProcess().addNewProcessRs();
                    rs.setReceivedPacket(receivedPacket.packet);
                    rs.setReceivedPackedHeaders(Maps.toXml(receivedPacket.headers));
                    seance.response(rsDoc);
                } else if (seance.rqDoc instanceof ConnectMess) {
                    final ConnectDocument rsDoc = ConnectDocument.Factory.newInstance();
                    final ConnectRs rs = rsDoc.addNewConnect().addNewConnectRs();
                    rs.setSID(sid);
                    rs.setReceivedPacket(receivedPacket.packet);
                    rs.setReceivedPackedHeaders(Maps.toXml(receivedPacket.headers));
                    seance.response(rsDoc);
                }
            }
            state = State.RECEIVING;
            processSapSeances(false);
        } else {
            if (ev.isExpired) {
                onRecvTimeout();
            } else {
                receivedPackets.add(new FramePacket(String.valueOf(nextInboundSeq()), ev.frame));
                logPacketReceived(receivedPackets.getLast());
                onRecv();
            }
        }
    }

    protected void sendImpl(final FramePacket sendPacket, final Long closeDelaySec, final Long recvTimeoutSec) throws IOException {
        if (port.getChannel() == null) {
            throw new IOException("Channel is allready closed");
        }
        port.send(sendPacket.packet, sendPacket.headers);
        if (closeDelaySec != null) {
            close(ECloseMode.GENERATE_ON_DISCONNECT_EVENT, closeDelaySec);
        }
        if (closeDelaySec == null || closeDelaySec > 0) {
            startRecv(recvTimeoutSec);
        }
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
        final byte[] packet = receivedPackets.get(0).packet;

        debug("processing inbound packet #" + receivedPackets.get(0).id, false);

        final InvokeDocument rq = InvokeDocument.Factory.newInstance();
        final InvokeRq invXml = rq.addNewInvoke().addNewInvokeRq();
        invXml.setClassId(NET_CHANNEL_CLASS_ID);
        invXml.setMethodId(ON_RECV_METHOD);
        final Parameters paramsXml = invXml.addNewParameters();
        paramsXml.addNewItem().setInt(channel.id);
        paramsXml.addNewItem().setStr(serverAddr);
        paramsXml.addNewItem().setStr(clientAddr);
        paramsXml.addNewItem().setStr(port.getCertCn());
        paramsXml.addNewItem().setStr(sid);
        paramsXml.addNewItem().setStr(callbackPid);
        paramsXml.addNewItem().setStr(callbackWid);
        paramsXml.addNewItem().setMapStrStr(Maps.toXml(receivedPackets.get(0).headers));
        paramsXml.addNewItem().setBin(packet);
        paramsXml.addNewItem().setBool(Boolean.valueOf(port.isConnected()));
        getAasClientPool().invoke(this, rq, getHeadersForAasInvoke(receivedPackets.get(0).headers), true);
        //listener.getView().trace(EEventSeverity.DEBUG, "Invoke AAS: " + invXml.xmlText(), null, null, EEventSource.UNIT_PORT.getValue(), true);
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
        final InvokeDocument rq = InvokeDocument.Factory.newInstance();
        final InvokeRq invXml = rq.addNewInvoke().addNewInvokeRq();
        invXml.setClassId(NET_CHANNEL_CLASS_ID);
        invXml.setMethodId(ON_RECV_TIMEOUT_METHOD);
        final Parameters paramsXml = invXml.addNewParameters();
        paramsXml.addNewItem().setInt(channel.id);
        paramsXml.addNewItem().setStr(serverAddr);
        paramsXml.addNewItem().setStr(clientAddr);
        paramsXml.addNewItem().setStr(port.getCertCn());
        paramsXml.addNewItem().setStr(sid);
        paramsXml.addNewItem().setStr(callbackPid);
        paramsXml.addNewItem().setStr(callbackWid);
        getAasClientPool().invoke(this, rq, null, true);
        debug("receive timeout", false);
        state = State.INVOKING_HANDLER_ON_RECV_TIMEOUT;
    }

    private void onDisconnect() {
        if (state == State.CLOSED) {
            return;
        }
        if (channel.getOptions().isDisconnectNtfOn) {
            final InvokeDocument rq = InvokeDocument.Factory.newInstance();
            final InvokeRq invXml = rq.addNewInvoke().addNewInvokeRq();
            invXml.setClassId(NET_CHANNEL_CLASS_ID);
            invXml.setMethodId(ON_DISCONNECT_METHOD);
            final Parameters paramsXml = invXml.addNewParameters();
            paramsXml.addNewItem().setInt(channel.id);
            paramsXml.addNewItem().setStr(serverAddr);
            paramsXml.addNewItem().setStr(clientAddr);
            paramsXml.addNewItem().setStr(port.getCertCn());
            paramsXml.addNewItem().setStr(sid);
            paramsXml.addNewItem().setStr(callbackPid);
            paramsXml.addNewItem().setStr(callbackWid);
            getAasClientPool().invoke(this, rq, null, true);
            //listener.getView().trace(EEventSeverity.DEBUG, "Invoke AAS: " + invXml.xmlText(), null, null, EEventSource.UNIT_PORT.getValue());
            debug("disconnected", false);
            state = State.INVOKING_HANDLER_ON_DISCONNECT;
        } else {
            close(ECloseMode.SILENT);
        }
    }

    final void registerSapSeance(final ServiceServerSeance seance, final XmlObject rqDoc, final boolean keepConnect) {
        sapSeances.add(new SapSeance(seance, rqDoc, keepConnect));
    }

    final boolean processSapSeances(boolean justConnected) {
        if (state != State.RECEIVING) {
            return false;
        }

        while (!sapSeances.isEmpty()) {
            cancelClose();
            final SapSeance seance = sapSeances.getFirst();

            if (!seance.seance.isConnected()) {
                nphUnit.getTrace().put(EEventSeverity.ERROR, "Seance is allready closed", null, null, EEventSource.UNIT_PORT.getValue(), false);
                sapSeances.removeFirst();
                continue;
            }

            try {
                if (seance.rqDoc instanceof ProcessMess) {
                    final ProcessRq rq = ((ProcessMess) seance.rqDoc).getProcessRq();
                    final Seance s = nphUnit.getSeances().findSeance(rq.getSID());
                    if (s == null) {
                        throw new ServiceProcessClientFault(ExceptionEnum.SEANCE_NOT_FOUND.toString(), "Seance #" + String.valueOf(((ProcessMess) seance.rqDoc).getProcessRq().getSID()) + " is not found", null, null);
                    }

                    s.callbackPid = rq.getCallbackPid();
                    s.callbackWid = rq.getCallbackWid();

                    final Long recvTimeoutSec = rq.isSetRecvTimeout() ? rq.getRecvTimeout() : null;
                    final Long closeDelaySec = rq.isSetCloseDelay() ? rq.getCloseDelay() : null;
                    if (isSetSendPacket(rq.getSendPacket(), rq.getSendPackedHeaders())) {
                        s.send(new FramePacket(String.valueOf(nextOutboundSeq()), rq.getSendPacket(), rq.getSendPackedHeaders()), closeDelaySec, recvTimeoutSec);
                    } else {
                        if (closeDelaySec != null) {
                            s.close(Seance.ECloseMode.GENERATE_ON_DISCONNECT_EVENT, closeDelaySec);
                        }
                    }

                    final boolean isRecvSync = rq.isSetIsRecvSync() ? rq.getIsRecvSync().booleanValue() : false;
                    if (isRecvSync && recvTimeoutSec != null) {
                        state = State.RECEIVING_SYNC;
                        return true;
                    }

                    final ProcessDocument rsDoc = ProcessDocument.Factory.newInstance();
                    final ProcessRs rs = rsDoc.addNewProcess().addNewProcessRs();

                    final byte[] packet = s.popUndeliveredReceivedPacket();
                    if (packet != null) {
                        rs.setUndeliveredPacket(packet);
                    }
                    seance.response(rsDoc);
                } else if (seance.rqDoc instanceof ConnectMess) {
                    final ConnectRq rq = ((ConnectMess) seance.rqDoc).getConnectRq();
                    final NetChannel netChannel = nphUnit.getChannels().findChannel(rq.getClientId().longValue(), true);
                    if (netChannel == null || !(netChannel instanceof NetClient)) {
                        throw new ServiceProcessClientFault(ExceptionEnum.SEANCE_NOT_FOUND.toString(), "Net channel #" + String.valueOf(rq.getClientId()) + " is not found", null, null);
                    }
                    final Seance s = ((NetClient) netChannel).getSeance();
                    if (s == null) {
                        throw new ServiceProcessClientFault(ExceptionEnum.SEANCE_NOT_FOUND.toString(), "Seance for net channel #" + String.valueOf(rq.getClientId()) + " is not found", null, null);
                    }

                    s.callbackPid = rq.getCallbackPid();
                    s.callbackWid = rq.getCallbackWid();

                    final Long recvTimeoutSec = rq.isSetRecvTimeout() ? rq.getRecvTimeout() : null;
                    final Long closeDelaySec = rq.isSetCloseDelay() ? rq.getCloseDelay() : null;
                    if (isSetSendPacket(rq.getSendPacket(), rq.getSendPackedHeaders())) {
                        s.send(new FramePacket(String.valueOf(nextOutboundSeq()), rq.getSendPacket(), rq.getSendPackedHeaders()), closeDelaySec, recvTimeoutSec);
                    } else {
                        if (closeDelaySec != null) {
                            s.close(Seance.ECloseMode.GENERATE_ON_DISCONNECT_EVENT, closeDelaySec);
                        }
                    }

                    final boolean isRecvSync = rq.isSetIsRecvSync() ? rq.getIsRecvSync().booleanValue() : false;
                    final boolean isConnectSync = rq.isSetIsConnectSync() ? rq.getIsConnectSync().booleanValue() : false;
                    if (recvTimeoutSec == null) {
                        if (isConnectSync) {
                            // response
                        } else {
                            if (justConnected) {
                                onConnect();
                                return false;
                            } else {
                                // response
                            }
                        }
                    } else {
                        if (isRecvSync) {
                            state = State.RECEIVING_SYNC;
                            return true;
                        } else {
                            if (justConnected) {
                                return false;
                            } else {
                                // response
                            }
                        }
                    }

                    final ConnectDocument rsDoc = ConnectDocument.Factory.newInstance();
                    final ConnectRs rs = rsDoc.addNewConnect().addNewConnectRs();
                    rs.setSID(s.sid);
                    seance.response(rsDoc);
                }
            } catch (Throwable e) {
                seance.response(e);
            }
            sapSeances.removeFirst();
        }
        return false;
    }

    final void closeSapSeances() {
        if (state == State.CLOSED) {
            return;
        }

        for (SapSeance seance : sapSeances) {
            try {
                seance.response(new ServiceProcessServerFault(ExceptionEnum.SERVER_EXCEPTION.toString(), "Netport seance was unexpectedly closed", null, null));
            } catch (Throwable e) {
                if (e instanceof InterruptedException || Thread.currentThread().isInterrupted()) {
                    return;
                }
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                channel.getTrace().put(EEventSeverity.ERROR, remoteAddrPres + ": " + NetPortHandlerMessages.ERR_ON_SOCKET_IO + exStack, NetPortHandlerMessages.MLS_ID_ERR_ON_SOCKET_IO, new ArrStr(remoteAddrPres, channel.getTitle(), exStack), EEventSource.UNIT_PORT.getValue(), false);
            }
        }

        sapSeances.clear();
    }

    LocalTracer getAasClientTracer() {
        return aasTracer;
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

    private boolean isSetSendPacket(final byte[] sendPacket, MapStrStr sendPacketHeaders) {
        return (sendPacket != null) || (sendPacket == null && sendPacketHeaders != null && !sendPacketHeaders.getEntryList().isEmpty());
    }

    static class SapSeance {

        public ServiceServerSeance seance = null;
        public XmlObject rqDoc = null;
        public boolean keepConnect = false;

        public SapSeance(final ServiceServerSeance seance, final XmlObject rqDoc, final boolean keepConnect) {
            this.seance = seance;
            this.rqDoc = rqDoc;
            this.keepConnect = keepConnect;
        }

        public void response(final XmlObject rsDoc) {
            seance.response(rsDoc, keepConnect);
        }

        public void response(final Throwable e) {
            if (e instanceof ServiceProcessFault) {
                seance.response((ServiceProcessFault) e, keepConnect);
            } else {
                final String exceptionStack = ExceptionTextFormatter.exceptionStackToString(e);
                seance.response(new ServiceProcessServerFault(ExceptionEnum.SERVER_EXCEPTION.toString(), "Unhandled exception: " + exceptionStack, e, exceptionStack), false);
            }
        }

        public void responseReceiveTimeout() {
            seance.response(new ServiceProcessServerFault(ExceptionEnum.RECEIVE_TIMEOUT.toString(), "Receive timeout", null, null), false);
        }

        public void responseConnectTimeout() {
            seance.response(new ServiceProcessServerFault(ExceptionEnum.CONNECT_TIMEOUT.toString(), "Connect timeout", null, null), false);
        }
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

    protected static class DisconnectRequest extends Event {

        SocketChannelPort source;

        public DisconnectRequest(SocketChannelPort source) {
            this.source = source;
        }

        @Override
        public Object getSource() {
            return source;
        }

        @Override
        public boolean matchs(Object o) {
            return o.getClass() == getClass() && source == ((DisconnectRequest) o).source;
        }
    };

    private static final class SidGen {

        private static final AtomicLong seq = new AtomicLong();

        static String newSid(final long channelId, final String host, final String port) {
            final String datePart = new SimpleDateFormat("MMddHHmmss").format(new Date());
            return channelId + "-" + datePart + "-" + String.valueOf(seq.incrementAndGet()) + "-" + host + "-" + port;
        }
    }
}
