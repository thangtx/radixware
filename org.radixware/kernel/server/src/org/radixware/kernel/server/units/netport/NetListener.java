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
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.ELinkLevelProtocolKind;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.aio.Event;
import org.radixware.kernel.server.aio.EventDispatcher;
import org.radixware.kernel.server.aio.EventDispatcher.AcceptEvent;
import org.radixware.kernel.server.instance.ResourceRegistry;
import org.radixware.kernel.server.instance.ServerSocketResourceRegistryItem;
import org.radixware.kernel.server.instance.SocketResourceRegistryItem;

final class NetListener extends NetChannel {

    private ServerSocketChannel serverSocketChannel = null;
    private String lastBindAddress = null;

    public NetListener(
            final NetPortHandlerUnit unit,
            final long id,
            final String title) {
        super(unit, id, title);
    }

    @Override
    public Window getParentView() {
        return unit.getView().getDialog();
    }

    @Override
    protected String calcViewStatus() {
        return String.format("Seances: %04d; Busy: %04d;",
                getActiveSeancesCount(),
                getBusySeancesCount());
    }
    


    @Override
    protected boolean rereadOptions(final Map<Long, NetChannelOptions> preloadedOptions) throws SQLException, InterruptedException {
        if (!super.rereadOptions(preloadedOptions)) {
            if (serverSocketChannel == null && isStarted()) {
                restartImpl();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void restartImpl() {
        getTrace().put(EEventSeverity.DEBUG, "Restarting server socket", null, null, EEventSource.UNIT_PORT.getValue(), false);
        closeImpl();
        startImpl();
    }

    @Override
    public void maintenance() {
        super.maintenance();
        if (serverSocketChannel != null && serverSocketChannel.socket() != null && serverSocketChannel.socket().isClosed()) {
            getTrace().put(EEventSeverity.ERROR, "Server socket of channel " + getTitle() + " (" + lastBindAddress + ") was unexpectedly closed, restarting", null, null, unit.getEventSource(), false);
            restartImpl();
        }
    }

    @Override
    protected void closeImpl() {
        if (serverSocketChannel != null) {
            unit.getDispatcher().unsubscribe(new EventDispatcher.AcceptEvent(serverSocketChannel));
            try {
                serverSocketChannel.close();
            } catch (IOException ex) {
                //do nothing
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            } finally {
                serverSocketChannel = null;
            }
        }
    }

    @Override
    protected boolean startImpl() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            unit.getInstance().getResourceRegistry().register(new ServerSocketResourceRegistryItem(ResourceRegistry.buildServerSocketKey(getResourceKeyPrefix(), String.valueOf(options.bindAddress)), serverSocketChannel, null, unit.getThisRunAliveChecker()));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(options.bindAddress, 128);
            lastBindAddress = options.bindAddress.toString();
            waitAccept();
            return true;
        } catch (IOException e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            getTrace().put(EEventSeverity.ERROR, NetPortHandlerMessages.ERR_ON_SOCKET_START + exStack, NetPortHandlerMessages.MLS_ID_ERR_ON_SOCKET_START, new ArrStr(getTitle(), exStack), EEventSource.UNIT_PORT.getValue(), false);
            unit.getTrace().put(EEventSeverity.ERROR, NetPortHandlerMessages.ERR_ON_SOCKET_START + "(#" + id + "), " + exStack, NetPortHandlerMessages.MLS_ID_ERR_ON_SOCKET_START, new ArrStr(getTitle(), exStack), EEventSource.UNIT_PORT.getValue(), false);
            closeImpl();
            return false;
        }
    }

    private void waitAccept() {
        unit.getDispatcher().waitEvent(new EventDispatcher.AcceptEvent(serverSocketChannel), this, -1);
    }

    @Override
    public void onEvent(final Event ev) {
        if (ev instanceof AcceptEvent) {
            SocketChannel acceptedSocketChannel = null;
            boolean doClose = true;
            try {
                if (serverSocketChannel != null) {
                    acceptedSocketChannel = serverSocketChannel.accept();
                    unit.getInstance().getResourceRegistry().register(new SocketResourceRegistryItem(ResourceRegistry.buildConnectedSocketChannelKey(getResourceKeyPrefix(), acceptedSocketChannel), acceptedSocketChannel, null, unit.getThisRunAliveChecker()));
                    acceptedSocketChannel.configureBlocking(true);
                    //if(options.recvTimeoutMillis != null)
                    //	acceptedSocketChannel.socket().setSoTimeout(options.recvTimeoutMillis.intValue());
                    try {
                        acceptedSocketChannel.socket().setTcpNoDelay(true);
                    } catch (IOException ex) {
                        //Microsoft bug
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                    }
                    try {
                        acceptedSocketChannel.socket().setKeepAlive(options.useKeepAlive);
                    } catch (java.net.SocketException ex) {
                        //Vista bug
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                    }
                    try {
                        doClose = false;
                        final Seance s = unit.getSeances().openNew(this, acceptedSocketChannel);
                        if (s == null) {
                            doClose = true;
                        } else {
                            s.onConnect();
                        }
                    } catch (Throwable e) {
                        doClose = true;
                        if (!(e instanceof InterruptedException) && !Thread.currentThread().isInterrupted()) {
                            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                            getTrace().put(EEventSeverity.ERROR, NetPortHandlerMessages.ERR_ON_NET_PORT_RQ_PROC + exStack, NetPortHandlerMessages.MLS_ID_ERR_ON_NET_PORT_RQ_PROC, new ArrStr(getTitle(), exStack), EEventSource.UNIT_PORT.getValue(), false);
                        }
                    }
                }
            } catch (Throwable e) {
                doClose = true;
                if (!(e instanceof InterruptedException) && !Thread.currentThread().isInterrupted()) {
                    final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                    getTrace().put(EEventSeverity.ERROR, NetPortHandlerMessages.ERR_ON_SOCKET_IO + exStack, NetPortHandlerMessages.MLS_ID_ERR_ON_SOCKET_IO, new ArrStr(getTitle(), exStack), EEventSource.UNIT_PORT.getValue(), false);
                }
            } finally {
                if (acceptedSocketChannel != null && doClose) {
                    try {
                        acceptedSocketChannel.socket().shutdownInput();
                    } catch (Throwable ex) {
                        //do nothing
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                    }
                    try {
                        acceptedSocketChannel.socket().shutdownOutput();
                    } catch (Throwable ex) {
                        //do nothing
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                    }
                    try {
                        acceptedSocketChannel.socket().close();
                    } catch (Throwable ex) {
                        //do nothing
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                    }
                    try {
                        acceptedSocketChannel.close();
                    } catch (Throwable ex) {
                        //do nothing
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                    }
                }
            }
            if (!unit.isShuttingDown() && serverSocketChannel != null) {
                waitAccept();
            }
        } else {
            throw new IllegalUsageError("Invalid event " + ev);
        }
    }
}
