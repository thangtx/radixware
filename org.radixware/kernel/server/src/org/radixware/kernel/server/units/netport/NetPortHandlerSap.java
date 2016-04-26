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

import org.radixware.kernel.server.sap.Sap;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.HttpFormatter;
import org.radixware.kernel.server.aio.ServiceServer;
import org.radixware.schemas.netporthandler.ApplySettingsMess;
import org.radixware.schemas.netporthandler.ChannelControlMess;
import org.radixware.schemas.netporthandler.CloseMess;
import org.radixware.schemas.netporthandler.CloseRq;
import org.radixware.schemas.netporthandler.ConnectMess;
import org.radixware.schemas.netporthandler.ConnectRq;
import org.radixware.schemas.netporthandler.ExceptionEnum;
import org.radixware.schemas.netporthandler.ProcessMess;
import org.radixware.schemas.netporthandler.ProcessRq;
import org.radixware.schemas.netporthandler.ProcessRs;
import org.radixware.schemas.netporthandlerWsdl.ApplySettingsDocument;
import org.radixware.schemas.netporthandlerWsdl.ChannelControlDocument;
import org.radixware.schemas.netporthandlerWsdl.CloseDocument;
import org.radixware.schemas.netporthandlerWsdl.ProcessDocument;
import org.radixware.schemas.systeminstancecontrol.ActionStateEnum;

/**
 * Класс реализующий сервис отложенной обработки пакетов.
 *
 *
 *
 */
final class NetPortHandlerSap extends Sap {

    private final NetPortHandlerUnit unit;

    public NetPortHandlerSap(final NetPortHandlerUnit unit) {
        super(unit.getDispatcher(), unit.getTrace().newTracer(EEventSource.NET_PORT_HANDLER_SERVICE.getValue()), 100, 10);
        this.unit = unit;
    }

    @Override
    protected void process(final ServiceServer.InvocationEvent event) {
        try {
            if (event.rqEnvBodyContent instanceof ProcessMess) {
                final ProcessRq rq = ((ProcessMess) event.rqEnvBodyContent).getProcessRq();
                final Seance s = unit.getSeances().findSeance(rq.getSID());
                if (s == null) {
                    throw new ServiceProcessClientFault(ExceptionEnum.SEANCE_NOT_FOUND.toString(), "Seance #" + String.valueOf(rq.getSID()) + " is not found", null, null);
                }
                if (s.state != Seance.State.CONNECTING && s.state != Seance.State.RECEIVING && s.state != Seance.State.RECEIVING_SYNC) {
                    event.seance.response(new ServiceProcessFault(ServiceProcessFault.FAULT_CODE_SERVER_BUSY, ExceptionEnum.SERVER_EXCEPTION.toString(), "Seance #" + s.sid + " is busy now", null, null), false);
                    return;
                }
                s.registerSapSeance(event.seance, event.rqEnvBodyContent, HttpFormatter.getKeepConnectionAlive(event.rqFrameAttrs));
                s.processSapSeances(false);
            } else if (event.rqEnvBodyContent instanceof ConnectMess) {
                final ConnectRq rq = ((ConnectMess) event.rqEnvBodyContent).getConnectRq();
                final Long clientId = rq.getClientId();
                if (clientId == null) {
                    throw new NullPointerException("Client ID is not defined");
                }
                final NetChannel channel = unit.getChannels().findChannel(clientId.longValue(), true);
                if (channel == null || !(channel instanceof NetClient)) {
                    throw new ServiceProcessClientFault(ExceptionEnum.SEANCE_NOT_FOUND.toString(), "Net channel #" + String.valueOf(rq.getClientId()) + " is not found", null, null);
                }
                final Seance s = ((NetClient) channel).connect(rq.getRemoteAddress(), rq.getLocalAddress(), rq.getConnectTimeout());
                if (s == null) {
                    throw new ServiceProcessClientFault(ExceptionEnum.SEANCE_NOT_FOUND.toString(), "Seance for net channel #" + String.valueOf(rq.getClientId()) + " is not found", null, null);
                }
                if (s.state != Seance.State.CONNECTING && s.state != Seance.State.RECEIVING && s.state != Seance.State.RECEIVING_SYNC) {
                    event.seance.response(new ServiceProcessFault(ServiceProcessFault.FAULT_CODE_SERVER_BUSY, ExceptionEnum.SERVER_EXCEPTION.toString(), "Seance #" + s.sid + " is busy now", null, null), false);
                    return;
                }

                if (s.state == Seance.State.CONNECTING) {
                    final boolean isRecvSync = rq.isSetIsRecvSync() ? rq.getIsRecvSync().booleanValue() : false;
                    final boolean isConnectSync = rq.isSetIsConnectSync() ? rq.getIsConnectSync().booleanValue() : false;
                    if (!isRecvSync && !isConnectSync) {
                        final ProcessDocument rsDoc = ProcessDocument.Factory.newInstance();
                        final ProcessRs rs = rsDoc.addNewProcess().addNewProcessRs();
                        event.seance.response(rsDoc, HttpFormatter.getKeepConnectionAlive(event.rqFrameAttrs));
                    }
                }

                s.registerSapSeance(event.seance, event.rqEnvBodyContent, HttpFormatter.getKeepConnectionAlive(event.rqFrameAttrs));
                s.processSapSeances(false);
            } else if (event.rqEnvBodyContent instanceof CloseMess) {
                final CloseRq rq = ((CloseMess) event.rqEnvBodyContent).getCloseRq();
                final Long closeDelaySec = rq.isSetCloseDelay() ? rq.getCloseDelay() : new Long(-1);
                final Seance s = unit.getSeances().findSeance(((CloseMess) event.rqEnvBodyContent).getCloseRq().getSID());
                if (s != null) {
                    s.close(Seance.ECloseMode.GENERATE_ON_DISCONNECT_EVENT, closeDelaySec);
                }
                final CloseDocument closeDoc = CloseDocument.Factory.newInstance();
                closeDoc.addNewClose().addNewCloseRs();
                event.seance.response(closeDoc, HttpFormatter.getKeepConnectionAlive(event.rqFrameAttrs));
            } else if (event.rqEnvBodyContent instanceof ApplySettingsMess) {
                unit.applyChannelsSettings();
                final ApplySettingsDocument applyDoc = ApplySettingsDocument.Factory.newInstance();
                applyDoc.addNewApplySettings().addNewApplySettingsRs();
                event.seance.response(applyDoc, HttpFormatter.getKeepConnectionAlive(event.rqFrameAttrs));
            } else if (event.rqEnvBodyContent instanceof ChannelControlMess) {
                final ChannelControlMess mess = (ChannelControlMess) event.rqEnvBodyContent;
                ActionStateEnum.Enum result = null;
                if (mess.isSetChannelControlRq()) {
                    if (mess.getChannelControlRq().isSetStart()) {
                        final NetChannel channel = unit.getChannels().findChannel(mess.getChannelControlRq().getStart().getChannelId());
                        if (channel == null) {
                            unit.getChannels().reread();
                        }
                        if (channel == null) {
                            throw new ServiceProcessClientFault(ExceptionEnum.CHANNEL_NOT_FOUND.toString(), "Channel #" + mess.getChannelControlRq().getStart().getChannelId() + " is not found", null, null);
                        }
                        if (channel.isStarted()) {
                            result = ActionStateEnum.DONE;
                        } else {
                            if (channel.start()) {
                                result = ActionStateEnum.DONE;
                            } else {
                                result = ActionStateEnum.FAILED;
                            }
                        }
                    }
                    if (mess.getChannelControlRq().isSetStop()) {
                        final NetChannel channel = unit.getChannels().findChannel(mess.getChannelControlRq().getStop().getChannelId());
                        if (channel == null) {
                            throw new ServiceProcessClientFault(ExceptionEnum.CHANNEL_NOT_FOUND.toString(), "Channel #" + mess.getChannelControlRq().getStart().getChannelId() + " is not found", null, null);
                        }
                        if (channel.isStarted()) {
                            channel.stop();
                        }
                        result = ActionStateEnum.DONE;
                    }
                }
                unit.getSeances().setCurSeancesCountInDbForcibly();
                final ChannelControlDocument controlDoc = ChannelControlDocument.Factory.newInstance();
                if (result == null) {
                    result = ActionStateEnum.FAILED;
                }
                controlDoc.addNewChannelControl().addNewChannelControlRs().setResult(result);
                event.seance.response(controlDoc, HttpFormatter.getKeepConnectionAlive(event.rqFrameAttrs));
            } else {
                throw new ServiceProcessClientFault(ExceptionEnum.INVALID_REQUEST.toString(), "Request \"" + event.rqEnvBodyContent.getClass().getName() + "\" is not supported by \"" + SERVICE_WSDL + "\" service", null, null);
            }
        } catch (InterruptedException e) {
            return;
        } catch (Throwable e) {
            event.seance.response(errorToFault(e), false, null);
        }
    }

    private ServiceProcessFault errorToFault(final Throwable e) {
        if (e instanceof ServiceProcessFault) {
            return (ServiceProcessFault) e;
        }
        final String exceptionStack = ExceptionTextFormatter.exceptionStackToString(e);
        return new ServiceProcessServerFault(ExceptionEnum.SERVER_EXCEPTION.toString(), "Unhandled exception: " + exceptionStack, e, exceptionStack);
    }
    public static final String SERVICE_WSDL = "http://schemas.radixware.org/netporthandler.wsdl";
    public static final String SERVICE_XSD = "http://schemas.radixware.org/netporthandler.xsd";

    @Override
    protected boolean isShuttingDown() {
        return unit.isShuttingDown();
    }

    @Override
    public long getId() {
        return unit.getSapId();
    }

    @Override
    protected void restoreDbConnection() throws InterruptedException {
        unit.restoreDbConnection();
    }
}
