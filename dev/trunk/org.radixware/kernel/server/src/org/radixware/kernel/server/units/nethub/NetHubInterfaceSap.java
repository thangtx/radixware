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

import org.radixware.schemas.nethub.ExceptionEnum;
import org.radixware.schemas.nethub.Invoke;
import org.radixware.schemas.nethub.InvokeRq;
import org.radixware.schemas.nethub.Reconnect;
import org.radixware.schemas.nethub.Send;
import org.radixware.schemas.nethub.SendRq;
import org.radixware.schemas.nethubWsdl.ReconnectDocument;
import org.radixware.schemas.nethubWsdl.SendDocument;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Bin;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.HttpFormatter;
import org.radixware.kernel.common.utils.Maps;
import org.radixware.kernel.common.utils.SoapFormatter;
import org.radixware.kernel.common.utils.SoapFormatter.FaultDetailWriter;
import org.radixware.kernel.common.utils.SoapFormatter.ResponseTraceItem;
import org.radixware.kernel.server.aio.Event;
import org.radixware.kernel.server.aio.EventDispatcher;
import org.radixware.kernel.server.aio.EventHandler;
import org.radixware.kernel.server.aio.ServiceServer.InvocationEvent;
import org.radixware.kernel.server.aio.ServiceServerSeance;
import org.radixware.kernel.server.sap.Sap;
import org.radixware.schemas.nethubWsdl.InvokeDocument;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xmlsoap.schemas.soap.envelope.Fault;


final class NetHubInterfaceSap extends Sap {

    private static final String COMP_NAME = "Service Server";
    private final NetHubUnit unit;

    public NetHubInterfaceSap(final NetHubUnit unit) {
        super(unit.getDispatcher(), unit.getTrace().newTracer(unit.getEventSource(), COMP_NAME), 100, 30);
        this.unit = unit;
    }
    Map<Bin, ServiceServerSeance> seansesByMess = new HashMap<Bin, ServiceServerSeance>();
    Map<Bin, Long> seansesTimeoutByMess = new HashMap<Bin, Long>();
    Map<Bin, Stan> stanByMess = new HashMap<Bin, Stan>();
    Map<Bin, Map<String, String>> parsingVarsByMess = new HashMap<Bin, Map<String, String>>();

    @Override
    public void stop() {
        super.stop();
        clearSeances();
    }

    @Override
    protected void process(InvocationEvent event) {
        try {
            if (event.rqEnvBodyContent instanceof Invoke) {
                if (unit.getOptions().outSeanceCnt != null && seansesByMess.size() >= unit.getOptions().outSeanceCnt.longValue()) {
                    final String limitStr = String.valueOf(unit.getOptions().outSeanceCnt);
                    unit.getTrace().put(EEventSeverity.ERROR, NetHubMessages.ERR_OUT_RQ_LIMIT_EXEEDED + ": " + limitStr, NetHubMessages.MLS_ID_ERR_OUT_RQ_LIMIT_EXEEDED, new ArrStr(limitStr, unit.getTitle()), unit.getEventSource(), false);
                    throw new ServiceProcessClientFault(ExceptionEnum.OUT_RQ_LIMIT_EXEEDED.toString(), "Output request count limit exeeded", null, null);
                }
                final InvokeRq rq = ((Invoke) event.rqEnvBodyContent).getInvokeRq();
                unit.traceDebug("Request for external system received (STAN = " + rq.getStan() + ")", COMP_NAME, false);
                final Bin mess = Bin.wrap(rq.getBody());
                final Stan stan = new Stan(rq.getStan().longValue());
                unit.getExtPort().send(rq.getBody(), "Request");
                seansesByMess.put(mess, event.seance);
                stanByMess.put(mess, stan);
                parsingVarsByMess.put(mess, Maps.fromXml(rq.getParsingVars()));
                final Long recvTimeoutMillis = unit.getOptions().extPortOptions.recvTimeoutMillis;
                if (recvTimeoutMillis != null) {
                    final Long deadlineMillis = new Long(System.currentTimeMillis() + recvTimeoutMillis.longValue());
                    seansesTimeoutByMess.put(mess, deadlineMillis);
                    unit.getDispatcher().waitEvent(new EventDispatcher.TimerEvent(), new TimerListener(), deadlineMillis);
                }
            } else if (event.rqEnvBodyContent instanceof Reconnect) {
                unit.traceDebug("Reconnect with external system command received", COMP_NAME, false);
                unit.getExtPort().reconnect();
                final ReconnectDocument rsDoc = ReconnectDocument.Factory.newInstance();
                rsDoc.addNewReconnect().addNewReconnectRs();
                event.seance.response(rsDoc, HttpFormatter.getKeepConnectionAlive(event.rqFrameAttrs));
            } else if (event.rqEnvBodyContent instanceof Send) {
                unit.traceDebug("Message for external system received", COMP_NAME, false);
                final SendRq rq = ((Send) event.rqEnvBodyContent).getSendRq();
                unit.getExtPort().send(rq.getBody(), "Message");
                final SendDocument rsDoc = SendDocument.Factory.newInstance();
                rsDoc.addNewSend().addNewSendRs();
                event.seance.response(rsDoc, HttpFormatter.getKeepConnectionAlive(event.rqFrameAttrs));
            } else {
                throw new ServiceProcessClientFault(ExceptionEnum.INVALID_REQUEST.toString(), "Request \"" + event.rqEnvBodyContent.getClass().getName() + "\" is not supported by \"" + SERVICE_WSDL + "\" service", null, null);
            }
        } catch (Throwable e) {
            event.seance.response(errorToFault(e), false);
        }
    }

    void clearSeances() {
        for (ServiceServerSeance s : seansesByMess.values()) {
            s.close();
        }
        seansesByMess.clear();
        seansesTimeoutByMess.clear();
        stanByMess.clear();
        parsingVarsByMess.clear();
    }

    boolean onResponse(final byte[] rqMess, final byte[] rsMess) {
        if (rqMess == null) {
            return false;
        }
        final Bin rq = Bin.wrap(rqMess);
        final ServiceServerSeance s = seansesByMess.get(rq);
        if (s != null) {
            seansesTimeoutByMess.remove(rq);
            seansesByMess.remove(rq);
            stanByMess.remove(rq);
            parsingVarsByMess.remove(rq);
            if (s.isConnected()) {
                final InvokeDocument rsDoc = InvokeDocument.Factory.newInstance();
                rsDoc.addNewInvoke().addNewInvokeRs().setBody(rsMess);
                s.response(rsDoc, true);
                return true;
            }
        }
        return false;
    }

    /*    
    boolean onFaultResponse(final byte[] rqMess, final Throwable e) {
    if (rqMess == null)
    return false;
    final Bin rq = Bin.wrap(rqMess);
    final ServiceServerSeance s = seansesByMess.get(rq);
    if (s != null) {
    seansesTimeoutByMess.remove(rq);
    seansesByMess.remove(rq);
    stanByMess.remove(rq);
    parsingVarsByMess.remove(rq);
    if (s.isConnected()) {
    NetHubDetailWriter faultWriter = null;
    if (e instanceof ServiceCallFault) {
    final ServiceCallFault ex = (ServiceCallFault)e;
    faultWriter = new NetHubDetailWriter(ex.getMessage(), ex.getCauseExMessage(), ex.getCauseExClass(), ex.getCauseExStack());
    }
    s.response(errorToFault(e), faultWriter, false);
    return true;
    }
    }
    return false;
    }
     */
    private ServiceProcessFault errorToFault(final Throwable e) {
        if (e instanceof ServiceProcessFault) {
            return (ServiceProcessFault) e;
        }
        return new ServiceProcessServerFault(ExceptionEnum.SERVER_EXCEPTION.toString(), "Unhandled exception: " + ExceptionTextFormatter.exceptionStackToString(e), e, null);
    }
    public static final String SERVICE_WSDL = "http://schemas.radixware.org/nethub.wsdl";
    public static final String SERVICE_XSD = "http://schemas.radixware.org/nethub.xsd";

    @Override
    protected boolean isShuttingDown() {
        return unit.isShuttingDown();
    }

    @Override
    public long getId() {
        return unit.getOptions().sapId;
    }

    @Override
    protected void restoreDbConnection() throws InterruptedException {
        unit.restoreDbConnection();
    }

    private class TimerListener implements EventHandler {

        @Override
        public void onEvent(final Event ev) {
            if (ev instanceof EventDispatcher.TimerEvent) {
                final long time = System.currentTimeMillis();
                final List<Bin> seancesToRemove = new LinkedList<Bin>();
                for (Map.Entry<Bin, Long> deadline : seansesTimeoutByMess.entrySet()) {
                    if (time >= deadline.getValue().longValue()) {// Timeout
                        final Bin mess = deadline.getKey();
                        final ServiceServerSeance s = seansesByMess.get(mess);
                        final String stanStr = String.valueOf(stanByMess.get(mess));
                        unit.getTrace().put(EEventSeverity.WARNING, NetHubMessages.SAP_RECV_TIMEOUT + " (STAN =" + stanStr + ")", NetHubMessages.MLS_ID_SAP_RECV_TIMEOUT, new ArrStr(stanStr), unit.getEventSource(), false);
                        if (s.isConnected()) {
                            s.response(new ServiceProcessServerFault(ExceptionEnum.EXT_SYS_CALL_TIMEOUT.toString(), "External system call timeout", null, null), false);
                        }
                        s.close();
                        seancesToRemove.add(mess); // to avoid concurrent modification
                    }
                }
                for (Bin mess : seancesToRemove) {// to avoid concurrent modification
                    seansesByMess.remove(mess);
                    seansesTimeoutByMess.remove(mess);
                    stanByMess.remove(mess);
                    parsingVarsByMess.remove(mess);
                }
            } else {
                throw new IllegalUsageError("Invalid event " + ev);
            }
        }
    }

    static class NetHubDetailWriter implements FaultDetailWriter {

        private final String faultMessage;
        private final String causeMessage;
        private final String causeClassName;
        private final String causeStack;

        public NetHubDetailWriter(
                final String faultMessage,
                final String causeMessage,
                final String causeClassName,
                final String causeStack) {
            this.faultMessage = faultMessage;
            this.causeMessage = causeMessage;
            this.causeClassName = causeClassName;
            this.causeStack = causeStack;
        }

        @Override
        public void writeTo(final Fault fault, final List<ResponseTraceItem> traceItems) {
            final Node detail = fault.addNewDetail().getDomNode();
            final Element message = detail.getOwnerDocument().createElementNS(SoapFormatter.FLT_DET_XSD, "Message");
            message.appendChild(detail.getOwnerDocument().createTextNode(faultMessage));
            detail.appendChild(message);
            final Element exXml = detail.getOwnerDocument().createElementNS(SoapFormatter.FLT_DET_XSD, "Exception");
            final Element messXml = detail.getOwnerDocument().createElementNS(SoapFormatter.FLT_DET_XSD, "Message");
            messXml.appendChild(detail.getOwnerDocument().createTextNode(causeMessage));
            exXml.appendChild(messXml);
            final Element classXml = detail.getOwnerDocument().createElementNS(SoapFormatter.FLT_DET_XSD, "Class");
            classXml.appendChild(detail.getOwnerDocument().createTextNode(causeClassName));
            exXml.appendChild(classXml);
            final Element stackXml = detail.getOwnerDocument().createElementNS(SoapFormatter.FLT_DET_XSD, "Stack");
            stackXml.appendChild(detail.getOwnerDocument().createTextNode(causeStack));
            exXml.appendChild(stackXml);
            detail.appendChild(exXml);
        }
    }
}