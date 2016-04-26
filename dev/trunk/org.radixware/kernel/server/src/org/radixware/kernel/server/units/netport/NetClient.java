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
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.ELinkLevelProtocolKind;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.ValueFormatter;
import org.radixware.kernel.server.SrvRunParams;
import org.radixware.kernel.server.aio.Event;
import org.radixware.kernel.server.aio.EventDispatcher.ConnectEvent;
import org.radixware.kernel.server.units.Messages;
import org.radixware.kernel.server.units.ServerItemView;
import org.radixware.kernel.server.units.netport.Seance.SapSeance;
import org.radixware.schemas.netporthandler.ConnectMess;
import org.radixware.schemas.netporthandler.ConnectRq;

final class NetClient extends NetChannel {

    public NetClient(
            final NetPortHandlerUnit unit,
            final long id,
            final String title,
            final ELinkLevelProtocolKind linkLevelProtocolKind,
            final String inFrame,
            final String outFrame) {
        super(unit, id, title, linkLevelProtocolKind, inFrame, outFrame);
    }

    @Override
    public Window getParentView() {
        return unit.getView().getDialog();
    }

    @Override
    protected void restartImpl() {
    }

    @Override
    protected void closeImpl() {
    }

    @Override
    protected boolean startImpl() {
        return true;
    }

    public Seance getSeance() {
        return unit.getSeances().getActiveSeance(this);
    }

    public Seance connect(String remoteAddr, String localAddr, Long connectTimeoutSec) throws Throwable {
        InetSocketAddress remoteAddress = ValueFormatter.parseInetSocketAddress(remoteAddr);
        if (remoteAddress == null) {
            remoteAddress = options.remoteAddress;
        }

        Seance s = getSeance();
        if (s != null) {
            try {
                if (s.getPort().isConnected() && s.getPort().getChannel().getRemoteAddress().equals(remoteAddress)) {
                    return s;
                }
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            s.close(Seance.ECloseMode.SILENT);
        }

        SocketChannel clientSocketChannel = null;
        try {
            clientSocketChannel = SocketChannel.open();
            clientSocketChannel.configureBlocking(false);

            try {
                clientSocketChannel.socket().setTcpNoDelay(true);
            } catch (IOException ex) {
                //Microsoft bug
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            try {
                clientSocketChannel.socket().setKeepAlive(true);
            } catch (IOException ex) {
                //Vista bug
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }

            InetSocketAddress localAddress = ValueFormatter.parseInetSocketAddress(localAddr);
            if (localAddress == null) {
                localAddress = options.address;
            }
            if (localAddress == null) {
                localAddress = new InetSocketAddress(0);
            }

            if (localAddress.isUnresolved()) {
                throw new IOException("Can't resolve client socket's address: " + localAddress);
            }

            getTrace().put(EEventSeverity.EVENT, "Client port connecting to " + remoteAddress + " from " + localAddress, null, null, EEventSource.UNIT_PORT.getValue(), false);

            clientSocketChannel.socket().bind(localAddress);
            s = unit.getSeances().openNew(this, clientSocketChannel);

            if (clientSocketChannel.connect(remoteAddress)) {
                onEvent(new ConnectEvent(clientSocketChannel));
            } else {
                getTrace().put(EEventSeverity.EVENT, "Client port waits for connect", null, null, EEventSource.UNIT_PORT.getValue(), false);
                if (connectTimeoutSec != null && connectTimeoutSec > 0) {
                    unit.getDispatcher().waitEvent(new ConnectEvent(clientSocketChannel), this, System.currentTimeMillis() + connectTimeoutSec * 1000);
                } else {
                    unit.getDispatcher().waitEvent(new ConnectEvent(clientSocketChannel), this, -1);
                }
            }

        } catch (Throwable e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            getTrace().put(EEventSeverity.ERROR, NetPortHandlerMessages.ERR_ON_SOCKET_START + exStack, NetPortHandlerMessages.MLS_ID_ERR_ON_SOCKET_START, new ArrStr(getTitle(), exStack), EEventSource.UNIT_PORT.getValue(), false);
            closeChannel(clientSocketChannel);
            unit.getSeances().unregisterSeance(s);
            throw e;
        }

        return s;
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
    public void onEvent(final Event ev) {
        if (ev.getClass() == ConnectEvent.class) {
            final SocketChannel clientSocketChannel = (SocketChannel) ev.getSource();
            final Seance s = getSeance();
            if (ev.isExpired) {
                getTrace().put(EEventSeverity.ERROR, "Client port connect timeout", null, null, EEventSource.UNIT_PORT.getValue(), false);
                if (s != null) {
                    final SapSeance seance = s.sapSeances.getFirst();
                    final ConnectRq rq = ((ConnectMess) seance.rqDoc).getConnectRq();
                    final boolean isConnectSync = rq.isSetIsConnectSync() ? rq.getIsConnectSync().booleanValue() : false;
                    final boolean isRecvSync = rq.isSetIsRecvSync() ? rq.getIsRecvSync().booleanValue() : false;
                    if (isRecvSync || isConnectSync) {
                        try {
                            seance.responseConnectTimeout();
                        } finally {
                            s.sapSeances.removeFirst();
                            s.close(Seance.ECloseMode.SILENT);
                        }
                    } else {
                        s.onConnectTimeout();
                    }
                }
            } else {
                try {
                    clientSocketChannel.finishConnect();
                    getTrace().put(EEventSeverity.EVENT, "Client port connected", null, null, EEventSource.UNIT_PORT.getValue(), false);
                    if (s != null) {
                        s.open();
                        s.processSapSeances(true);
                    }
                } catch (Throwable e) {
                    getTrace().put(EEventSeverity.ERROR, "Client port connection establishment error: " + e, null, null, EEventSource.UNIT_PORT.getValue(), false);
                    if (s != null) {
                        s.close(Seance.ECloseMode.SILENT);
                    }
                }
            }
        } else {
            super.onEvent(ev);
        }
    }
}
