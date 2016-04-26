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

package org.radixware.kernel.server.units.jms;

import java.util.*;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.Maps;
import org.radixware.kernel.server.aio.Event;
import org.radixware.kernel.server.aio.EventDispatcher;
import org.radixware.kernel.server.aio.EventHandler;
import org.radixware.kernel.server.aio.ServiceServer.InvocationEvent;
import org.radixware.kernel.server.aio.ServiceServerSeance;
import org.radixware.kernel.server.sap.Sap;
import org.radixware.schemas.jmshandler.*;
import org.radixware.schemas.jmshandlerWsdl.ProcessBinDocument;
import org.radixware.schemas.jmshandlerWsdl.ProcessTextDocument;


final class JmsHandlerInterfaceSap extends Sap {

    private static final String COMP_NAME = "Service Server";
    private final JmsHandlerUnit unit;

    public JmsHandlerInterfaceSap(final JmsHandlerUnit unit) {
        super(unit.getDispatcher(), unit.getTrace().newTracer(unit.getEventSource(), COMP_NAME), 100, 30);
        this.unit = unit;
    }
    private final Map<String, Message> messById = new HashMap<String, Message>();
    private final Map<String, ServiceServerSeance> seancesById = new HashMap<String, ServiceServerSeance>();
    private final Map<String, Long> seancesTimeoutById = new HashMap<String, Long>();

    @Override
    public void stop() {
        super.stop();
        clearSeances();
    }

    @Override
    protected void process(InvocationEvent event) {
        try {
            if (event.rqEnvBodyContent instanceof ProcessBin || event.rqEnvBodyContent instanceof ProcessText) {
                if (unit.getOptions().outSeanceCnt != null && seancesById.size() >= unit.getOptions().outSeanceCnt.longValue()) {
                    final String limitStr = String.valueOf(unit.getOptions().outSeanceCnt);
                    unit.getTrace().put(EEventSeverity.ERROR, JmsHandlerMessages.ERR_OUT_RQ_LIMIT_EXEEDED + ": " + limitStr, JmsHandlerMessages.MLS_ID_ERR_OUT_RQ_LIMIT_EXEEDED, new ArrStr(limitStr, unit.getTitle()), unit.getEventSource(), false);
                    throw new ServiceProcessClientFault(ExceptionEnum.OUT_RQ_LIMIT_EXEEDED.toString(), "Output request count limit exeeded", null, null);
                }

                final Message mess;
                if (event.rqEnvBodyContent instanceof ProcessBin) {
                    final ProcessBinRq rq = ((ProcessBin) event.rqEnvBodyContent).getProcessBinRq();
                    unit.traceDebug("Request for external system received (mess = " + new String(rq.getMessage()) + ")", COMP_NAME, false);
                    mess = unit.sendBytes(rq.getMessage(), rq);
                    if (!unit.canReceive() || unit.getOptions().rsTimeout == null) {
                        event.seance.response(ProcessBinDocument.Factory.newInstance(), false);
                        return;
                    }
                } else {
                    final ProcessTextRq rq = ((ProcessText) event.rqEnvBodyContent).getProcessTextRq();
                    unit.traceDebug("Request for external system received (mess = " + rq.getMessage() + ")", COMP_NAME, false);
                    mess = unit.sendText(rq.getMessage(), rq);
                    if (!unit.canReceive() || unit.getOptions().rsTimeout == null) {
                        event.seance.response(ProcessTextDocument.Factory.newInstance(), false);
                        return;
                    }
                }

                final String messId = mess.getJMSMessageID();
                messById.put(messId, mess);
                seancesById.put(messId, event.seance);

                final Long rsTimeout = unit.getOptions().rsTimeout;
                if (rsTimeout != null) {
                    final Long deadlineMillis = new Long(System.currentTimeMillis() + rsTimeout.longValue() * 1000);
                    seancesTimeoutById.put(messId, deadlineMillis);
                    unit.getDispatcher().waitEvent(new EventDispatcher.TimerEvent(), new TimerListener(), deadlineMillis);
                }
            } else {
                throw new ServiceProcessClientFault(ExceptionEnum.INVALID_REQUEST.toString(), "Request \"" + event.rqEnvBodyContent.getClass().getName() + "\" is not supported by \"" + SERVICE_WSDL + "\" service", null, null);
            }
        } catch (Throwable e) {
            event.seance.response(errorToFault(e), false);
        }
    }

    void clearSeances() {
        for (ServiceServerSeance s : seancesById.values()) {
            s.close();
        }
        messById.clear();
        seancesById.clear();
        seancesTimeoutById.clear();
    }
    /*
     private void prepareInfo(final Base info, final Message msg) throws JMSException {
     if (msg == null)
     return;
        
     info.setCorrelationId(msg.getJMSCorrelationID());
     info.setDeliveryMode(Long.valueOf(msg.getJMSDeliveryMode()));
     info.setExpiration(msg.getJMSExpiration());
     info.setMessageId(msg.getJMSMessageID());
     info.setPriority(Long.valueOf(msg.getJMSPriority()));
     info.setRedelivered(msg.getJMSRedelivered());
     info.setTimestamp(msg.getJMSTimestamp());
     info.setType(msg.getJMSType());

     final Map<String, String> props = new HashMap<>();
     final Enumeration e = msg.getPropertyNames();
     while (e.hasMoreElements()) {
     final String name = (String)e.nextElement();
     final Object value = msg.getObjectProperty(name);
     props.put(name, value == null ? null : value.toString());
     }
     info.setProps(Maps.toXml(props));
     }
     */

    boolean onResponse(final Message msg) throws JMSException {
        if (msg == null) {
            return false;
        }

        final String corrMessId = msg.getJMSCorrelationID();
        if (corrMessId == null) {
            return false;
        }

        final ServiceServerSeance s = seancesById.get(corrMessId);
        final Message corrMess = messById.get(corrMessId);
        if (s != null && corrMess != null && corrMess.getClass() == msg.getClass()) {
            messById.remove(corrMessId);
            seancesById.remove(corrMessId);
            seancesTimeoutById.remove(corrMessId);
            if (s.isConnected()) {
                if (msg instanceof TextMessage) {
                    final TextMessage m = (TextMessage) msg;
                    final String text = m.getText();
                    final ProcessTextDocument rsDoc = ProcessTextDocument.Factory.newInstance();
                    rsDoc.addNewProcessText().addNewProcessTextRs().setMessage(text);
                    JmsHandlerUtils.prepareRequest(rsDoc.getProcessText().getProcessTextRs(), msg);
                    s.response(rsDoc, false);
                    return true;
                }
                if (msg instanceof BytesMessage) {
                    final BytesMessage m = (BytesMessage) msg;
                    final byte[] bytes = new byte[(int) m.getBodyLength()];
                    m.readBytes(bytes);
                    final ProcessBinDocument rsDoc = ProcessBinDocument.Factory.newInstance();
                    rsDoc.addNewProcessBin().addNewProcessBinRs().setMessage(bytes);
                    JmsHandlerUtils.prepareRequest(rsDoc.getProcessBin().getProcessBinRs(), msg);
                    s.response(rsDoc, false);
                    return true;
                }
            }
        }
        return false;
    }

    void onUncorrelatedResponse(final Message mess) throws JMSException {
        unit.getTrace().put(EEventSeverity.ERROR, JmsHandlerMessages.UNCORRELATED_RECV, JmsHandlerMessages.MLS_ID_ERR_UNCORRELATED_RECV, new ArrStr(mess.getJMSMessageID()), unit.getEventSource(), false);
        for (Map.Entry<String, ServiceServerSeance> e : seancesById.entrySet()) {
            final ServiceServerSeance s = e.getValue();
            if (s.isConnected()) {
                s.response(new ServiceProcessServerFault(ExceptionEnum.UNCORRELATED_RECV.toString(), "Uncorrelated message received: " + mess.toString(), null, null), false);
            }
        }
        messById.clear();
        seancesById.clear();
        seancesTimeoutById.clear();
    }

    private ServiceProcessFault errorToFault(final Throwable e) {
        if (e instanceof ServiceProcessFault) {
            return (ServiceProcessFault) e;
        }
        return new ServiceProcessServerFault(ExceptionEnum.SERVER_EXCEPTION.toString(), "Unhandled exception: " + ExceptionTextFormatter.exceptionStackToString(e), e, null);
    }
    public static final String SERVICE_WSDL = "http://schemas.radixware.org/jmshandler.wsdl";
    public static final String SERVICE_XSD = "http://schemas.radixware.org/jmshandler.xsd";

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
                final List<String> seancesToRemove = new LinkedList<String>();
                for (Map.Entry<String, Long> deadline : seancesTimeoutById.entrySet()) {
                    if (time >= deadline.getValue().longValue()) { // Timeout
                        final String messId = deadline.getKey();
                        final ServiceServerSeance s = seancesById.get(messId);
                        unit.getTrace().put(EEventSeverity.WARNING, JmsHandlerMessages.SAP_RECV_TIMEOUT, JmsHandlerMessages.MLS_ID_SAP_RECV_TIMEOUT, new ArrStr(messId), unit.getEventSource(), false);
                        if (s.isConnected()) {
                            s.response(new ServiceProcessServerFault(ExceptionEnum.EXT_SYS_CALL_TIMEOUT.toString(), "External system call timeout", null, null), false);
                        }
                        seancesToRemove.add(messId); // to avoid concurrent modification
                    }
                }
                for (String messId : seancesToRemove) { // to avoid concurrent modification
                    messById.remove(messId);
                    seancesById.remove(messId);
                    seancesTimeoutById.remove(messId);
                }
            } else {
                throw new IllegalUsageError("Invalid event " + ev);
            }
        }
    }
}