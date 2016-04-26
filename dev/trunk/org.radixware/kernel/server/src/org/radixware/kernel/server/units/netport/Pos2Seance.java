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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.nio.channels.SocketChannel;
import java.util.Map;
import org.radixware.kernel.server.aio.SocketChannelPort;
import org.radixware.schemas.netporthandler.OnConnectRsDocument.OnConnectRs;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.kernel.common.utils.Maps;
import org.radixware.schemas.netporthandler.ConnectMess;
import org.radixware.schemas.netporthandler.ConnectRs;
import org.radixware.schemas.netporthandler.ProcessMess;
import org.radixware.schemas.netporthandler.ProcessRs;
import org.radixware.schemas.netporthandlerWsdl.ConnectDocument;
import org.radixware.schemas.netporthandlerWsdl.ProcessDocument;

/**
 * Сетевой сеанс c LinkLevelProtocol = POS2.
 * Не синхронизируется так как работа с ним идет только с основной нити модуля
 * 
 */
final class Pos2Seance extends Seance {
	
    private static final byte SOH = 0x01;
    private static final byte STX = 0x02;
    private static final byte ETX = 0x03;
    private static final byte EOT = 0x04;
    private static final byte ENQ = 0x05;
    private static final byte ACK = 0x06;
    private static final byte NAK = 0x15;

    Pos2Seance(
            final NetPortHandlerUnit unit,
            final NetChannel netChannel,
            final SocketChannel channel) throws IOException, SQLException 
    {
        super(unit, netChannel, channel, null, null);
    }
    
    //private final static Long DEFAULT_RECV_TIMEOUT = new Long(30); //sec
    private Long lastRecvTimeoutSec = null;
    private FramePacket lastSendedPacket = null; 
    private int resendCount = 0; 

    @Override
    protected void onConnect() throws IOException {
        if (state == State.CLOSED) {
            return;
        }
        if (!channel.getOptions().isConnectReadyNtfOn) {
            //RADIX-3502
            resendCount = 2;            
            lastRecvTimeoutSec = options.recvTimeoutMillis / 1000;
            lastSendedPacket = new FramePacket(String.valueOf(nextOutboundSeq()), new byte[]{ENQ}, (Map<String, String>)null);
            debug("Writing <ENQ> (0x05)", false);
            port.send(lastSendedPacket.packet, lastSendedPacket.headers);
            state = State.RECEIVING;
            startRecv(lastRecvTimeoutSec);            
        } else
            super.onConnect();
    }

    @Override
    protected void onConnectResp(final OnConnectRs rs) throws IOException {
        if (state == State.CLOSED)
            return;
        callbackPid = rs.getCallbackPid();
        callbackWid = rs.getCallbackWid();
        lastRecvTimeoutSec = rs.getRecvTimeout();
        resendCount = 2;
        debug("Writing <ENQ> (0x05)", false);
        if (rs.isSetSendPacket()) {
            final byte[] p = rs.getSendPacket();
            if (channel.getTrace().getMinSeverity() == EEventSeverity.DEBUG.getValue().longValue()) {
                debug("Sending: " + new String(p), true);
            }
            final byte[] sendedPacket = new byte[p.length + 4];
            sendedPacket[0] = ENQ;
            pack(p, sendedPacket, 1);
            lastSendedPacket = new FramePacket(String.valueOf(nextOutboundSeq()), sendedPacket, rs.getSendPackedHeaders());
        } else {
            lastSendedPacket = new FramePacket(String.valueOf(nextOutboundSeq()), new byte[]{ENQ}, (Map<String, String>)null);
        }
        port.send(lastSendedPacket.packet, lastSendedPacket.headers);
        
        state = State.RECEIVING;
        final Long closeDelay = rs.isSetCloseDelay() ? rs.getCloseDelay() : null;
        if (closeDelay != null)
            close(ECloseMode.GENERATE_ON_DISCONNECT_EVENT, closeDelay);                
        if (state == State.RECEIVING) {
            if (!receivedPackets.isEmpty()) { // уже что-то пришло
                onRecv();
            } else
                startRecv(rs.getRecvTimeout());
        }
    }

    @Override
    protected void sendImpl(final FramePacket sendPacket, final Long closeDelay, final Long recvTimeout) throws IOException {
        resendCount  = 2;
        final byte[] sendedPacket = new byte[sendPacket.packet.length + 3];
        pack(sendPacket.packet, sendedPacket, 0);
        sendPacket.packet = sendedPacket;
        lastRecvTimeoutSec = recvTimeout;
        lastSendedPacket = sendPacket;
        port.send(lastSendedPacket.packet, lastSendedPacket.headers);
        if (closeDelay != null)
            close(ECloseMode.GENERATE_ON_DISCONNECT_EVENT, closeDelay);
        //if (closeDelay == null || closeDelay > 0) {
            startRecv(recvTimeout);
        //}
    }

    private void pack(final byte[] packet, final byte[] to, int i) {
        to[i] = STX;
        debug("Writing <STX> (0x02)", false);
        i++;
        byte lrc = 0;
        for(byte b : packet){
            to[i] = b;
            lrc ^= b;
            i++;
        }        
        if (channel.getTrace().getMinSeverity(EEventSource.UNIT_PORT) == EEventSeverity.DEBUG.getValue().longValue()) {
            debug("Writing packet. Packet HEX: " + Hex.encode(packet), true);
        }
        to[i] = ETX;
        debug("Writing <ETX> (0x03)", false);
        i++;
        lrc ^= ETX;
        to[i] = lrc;
        debug("Writing LRC: 0x" + Hex.byte2Hex(lrc), false);
    }
    
    private ByteArrayOutputStream receivedBytes = null;
    @Override
    protected void recvImpl(final SocketChannelPort.ReceiveEvent ev){
        boolean packetReceived = false; 
        try {
            if (ev.isExpired){
                receivedBytes = null;
                if (resendCount > 0 && lastSendedPacket != null) {
                    resendLastPacket();
                } else {
                    if (state == state.RECEIVING_SYNC) {
                        final SapSeance seance = sapSeances.pollFirst();
                        seance.responseReceiveTimeout();
                        state = State.RECEIVING;
                        processSapSeances(false);                    
                    } else {
                        //close(ECloseMode.GENERATE_ON_DISCONNECT_EVENT); // TWRBS-2128
                        onRecvTimeout();
                    }
                }
                return;
            }
            byte[] newReceivedBytes = ev.frame.packet;
            if (channel.getTrace().getMinSeverity(EEventSource.UNIT_PORT) == EEventSeverity.DEBUG.getValue().longValue()) {
                debug("Data read. Data HEX: " + Hex.encode(ev.frame.packet), true);
            }
PROCESS_NEW_DATA:
            while (true) {
                int i = 0;
                if (receivedBytes == null) {//пакет еще не начался {
                    int b;
                    while (true) {
                        if (i >= newReceivedBytes.length) {
                            startRecv();
                            return;
                        }
                        b = newReceivedBytes[i];
                        i++;
                        if (b == STX) {
                            debug("<STX> (0x02) parsed", false);
                            break;
                        }
                        if (b == SOH) {
                            debug("<SOH> (0x01) parsed", false);
                            break;
                        }
                        if (b == ACK) {
                            debug("<ACK> (0x06) parsed", false);
                            break;
                        }
                        if (b == NAK) {
                            debug("<NAK> (0x15) parsed", false);
                            break;
                        }
                        debug("Unexpected byte on parsing: 0x" + Hex.byte2Hex(b), true);
                    }
						
                    if (b == NAK) {
                        if (resendCount > 0 && lastSendedPacket != null) {
                            resendLastPacket();
                        } else
                            close(ECloseMode.GENERATE_ON_DISCONNECT_EVENT);
                        return;
                    }
                    if (b == ACK) {
                        resendCount = 2;
                        lastRecvTimeoutSec = options.recvTimeoutMillis / 1000;
                        lastSendedPacket = new FramePacket(String.valueOf(nextOutboundSeq()), new byte[]{EOT}, (Map<String, String>)null);
                        debug("Writing <EOT> (0x04)", false);
                        port.send(lastSendedPacket.packet, lastSendedPacket.headers);
                        startRecv(lastRecvTimeoutSec);
                        return;
                    }
                    receivedBytes = new ByteArrayOutputStream(Math.max(4096, newReceivedBytes.length-i));
                }
                receivedBytes.write(newReceivedBytes, i, newReceivedBytes.length-i);
					
                byte lrc = 0;
                final byte[] received = receivedBytes.toByteArray();
                for(int j = 0;;j++) {
                    if (j >= received.length){ // пакет не весь пришел 
                        startRecv();
                        return;
                    }
                    lrc ^= received[j];
                    if (received[j] == ETX) {
                        //packet finised
                        final byte[] packet = new byte[j];
                        System.arraycopy(received, 0, packet, 0, j);
                        if (channel.getTrace().getMinSeverity(EEventSource.UNIT_PORT) == EEventSeverity.DEBUG.getValue().longValue()) {
                            debug("Packet parsed. Packet HEX: " + Hex.encode(packet), true);
                        }
                        debug("<ETX> (0x03) parsed", false);
                        if (j+1 >= received.length) {
                            startRecv();
                            return; //waiting for LRC
                        }
                        final byte receivedLrc = received[j + 1];
                        debug("LRC parsed: 0x" + Hex.byte2Hex(receivedLrc), false);
                        if (lrc != receivedLrc){
                            debug("Wrong LRC: expected 0x" + Hex.byte2Hex(lrc) + " but got 0x" + Hex.byte2Hex(receivedLrc), false);
                            sendNak();
                            return;
                        } else {//пакет пришел
                            receivedPackets.add(new FramePacket(String.valueOf(nextInboundSeq()), packet, ev.frame.attrs));
                            receivedBytes = null;
                            packetReceived = true;//onRecv();
                            if(j+2 < received.length){// начался след пакет
                                newReceivedBytes = new byte[received.length - (j+2)];
                                System.arraycopy(received, j+2, newReceivedBytes, 0, newReceivedBytes.length);
                                continue PROCESS_NEW_DATA;
                            } else
                                return;// все пакеты получены целиком
                        }
                    }
                }
            }
        } catch (Throwable e) {
            onIoException(e);
        } finally {
            if (packetReceived) {
                if (closeRequested) { // AK said to ignore messages after ads handler requested close
                    close(ECloseMode.GENERATE_ON_DISCONNECT_EVENT);
                } else if (state == state.RECEIVING_SYNC) {
                    final SapSeance seance = sapSeances.pollFirst();
                    final FramePacket receivedPacket = receivedPackets.pollFirst();
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
                    state = State.RECEIVING;
                    processSapSeances(false);
                } else {
                    onRecv();
                }
            }
        }
    }

    private void resendLastPacket() throws IOException {
        resendCount--;
        if (port.isConnected()) {
            if (channel.getTrace().getMinSeverity(EEventSource.UNIT_PORT) == EEventSeverity.DEBUG.getValue().longValue()) {
                debug("Resending data. Data HEX: " + Hex.encode(lastSendedPacket.packet), true);
            }
            port.send(lastSendedPacket.packet, lastSendedPacket.headers);
            startRecv(lastRecvTimeoutSec);
            cancelClose();
        }
    }

    @Override
    protected void onAasInvokeException(final ServiceClientException exception) {
        if (state == State.CLOSED) {
            return;
        }
        try {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(exception);
            channel.getTrace().put(EEventSeverity.ERROR, remoteAddrPres + ": " + NetPortHandlerMessages.ERR_ON_NET_PORT_RQ_PROC + exStack, NetPortHandlerMessages.MLS_ID_ERR_ON_NET_PORT_RQ_PROC, new ArrStr(remoteAddrPres, channel.getTitle(), exStack), EEventSource.UNIT_PORT.getValue(), false);
            if (port.isConnected()) { // RADIX-2740
                sendNak();
            }
        } catch (Throwable e) {
            onIoException(e);
        }
    }

    private void sendNak() throws IOException {
        debug("Writing <NAK> (0x15)", false);
        resendCount = 2;
        lastRecvTimeoutSec = options.recvTimeoutMillis / 1000;
        lastSendedPacket = new FramePacket(String.valueOf(nextOutboundSeq()), new byte[]{NAK}, (Map<String, String>)null);
        port.send(lastSendedPacket.packet, lastSendedPacket.headers);
        receivedBytes = null;
        startRecv(lastRecvTimeoutSec);
        cancelClose();
    }

    private void onIoException(final Throwable e) {
        if (e instanceof InterruptedException || Thread.currentThread().isInterrupted()) {
            close(ECloseMode.SILENT);
            return;
        } else {
            close(ECloseMode.GENERATE_ON_DISCONNECT_EVENT);
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            channel.getTrace().put(EEventSeverity.ERROR, remoteAddrPres + ": " + NetPortHandlerMessages.ERR_ON_SOCKET_IO + exStack, NetPortHandlerMessages.MLS_ID_ERR_ON_SOCKET_IO, new ArrStr(remoteAddrPres, channel.getTitle(), exStack), EEventSource.UNIT_PORT.getValue(), false);
        }
    }

    @Override
    void cancelClose() {
        if (!closeRequested) {
            super.cancelClose();
        }
    }

    private boolean closeRequested = false;
    @Override
    void close(final ECloseMode mode, long closeDelaySec) {
        if (closeDelaySec <= 0){
            closeRequested = true;
        }
        if (closeDelaySec >= 0) {
            closeDelaySec += options.recvTimeoutMillis /1000;
        }
        super.close(mode, closeDelaySec);
    }
    
    
}
