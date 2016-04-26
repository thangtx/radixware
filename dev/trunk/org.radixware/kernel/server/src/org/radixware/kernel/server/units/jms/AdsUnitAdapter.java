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

import java.sql.SQLException;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.exceptions.ServiceCallException;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceCallTimeout;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.arte.services.aas.AasValueConverter;
import org.radixware.kernel.server.units.AasClient;
import org.radixware.kernel.server.units.nethub.NetHubUnitTracer;
import org.radixware.schemas.aas.InvokeRq;
import org.radixware.schemas.aas.InvokeRs;
import org.radixware.schemas.aasWsdl.InvokeDocument;
import org.radixware.schemas.jmshandler.MessageDocument;


final class AdsUnitAdapter extends AasClient {

    private static final String COMP_NAME = "Service Client"; 
    private static final String RQ_METHOD_NAME = "onRequest"; 
    private static final String RQ_METHOD_ID = "mth5QSCSEQ3ZJFXVKMUX7VRMSKGLQ";
    
    private final JmsHandlerUnit jmsUnit;
    private Message msg;
    
    AdsUnitAdapter(final JmsHandlerUnit jmsUnit) throws SQLException {
        super(jmsUnit, jmsUnit.getManifestLoader(), jmsUnit.getTrace().newTracer(jmsUnit.getEventSource(), COMP_NAME), jmsUnit.getDispatcher());
        this.jmsUnit = jmsUnit;
    }

    void onRequest(final Message msg) throws JMSException {
        final byte[] data;
        if (msg instanceof BytesMessage) {
            final BytesMessage m = (BytesMessage)msg;
            data = new byte[(int)m.getBodyLength()];            
            m.readBytes(data);            
        } else if (msg instanceof TextMessage) {
            final TextMessage m = (TextMessage)msg;
            data = m.getText().getBytes();
        } else {
            data = String.valueOf(msg).getBytes();
        }
        
        final MessageDocument xDoc = MessageDocument.Factory.newInstance();
        xDoc.addNewMessage();
        
        JmsHandlerUtils.prepareRequest(xDoc.getMessage(), msg);
        xDoc.getMessage().setData(data);
                
        this.msg = msg;
        jmsUnit.traceDebug("Calling " + RQ_METHOD_NAME + " method", COMP_NAME, false);
        invoke(prepareDacUnitAdapterInvokeWithMessParam(RQ_METHOD_ID, xDoc), true, JmsHandlerUnit.DEFAULT_AAS_CALL_TIMEOUT_MILLIS);
    }
    
    @Override
    protected void onInvokeResponse(final InvokeRs rs) {
        jmsUnit.getAasClientPool().freeClient(this);
        jmsUnit.traceDebug("Response from " + RQ_METHOD_NAME + " method received", COMP_NAME, false);
        jmsUnit.traceDebug("Response received value: " + rs.xmlText(), COMP_NAME, true);
        
        if (jmsUnit.canSend() && rs.isSetReturnValue() && rs.getReturnValue().isSetXml()) {
            try {
                final org.radixware.schemas.jmshandler.Message xMess = (org.radixware.schemas.jmshandler.Message)AasValueConverter.aasXmlVal2ObjVal(null, rs.getReturnValue());
                final byte[] data = xMess.getData();
                if (data != null) {
                    if (msg instanceof BytesMessage) {
                        jmsUnit.sendBytes(data, xMess, msg.getJMSMessageID());
                    } else {
                        jmsUnit.sendText(new String(data), xMess, msg.getJMSMessageID());
                    }
                }
            } catch (JMSException e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                unit.getTrace().put(EEventSeverity.ERROR, JmsHandlerMessages.ERR_ON_JMS_IO + exStack, JmsHandlerMessages.MLS_ID_ERR_ON_JMS_IO, new ArrStr(unit.getTitle(), exStack), unit.getEventSource(), false);
            }
        }
    }

    @Override
    protected void onInvokeException(final ServiceClientException exception) {
        jmsUnit.getAasClientPool().freeClient(this);
        String mess;
        if (exception instanceof ServiceCallFault) {
            mess = ((ServiceCallFault)exception).getCauseExMessage();
        } else if (exception instanceof ServiceCallTimeout || exception instanceof ServiceCallException)
            mess = exception.getMessage();
        else
            mess = ExceptionTextFormatter.exceptionStackToString(exception);
        jmsUnit.traceError("Exception during calling " + RQ_METHOD_NAME + " method received: " + mess, COMP_NAME);        
    }
    
    private InvokeDocument prepareDacUnitAdapterInvoke(final String mhId) {
        final InvokeDocument invokeXml = InvokeDocument.Factory.newInstance();
        final InvokeRq rq = invokeXml.addNewInvoke().addNewInvokeRq();
        rq.setPID(String.valueOf(jmsUnit.getId()));
        rq.setEntityId(JmsHandlerUnit.HANDLER_ENTITY_ID);
        rq.setMethodId(mhId);
        return invokeXml;
    }

    private InvokeDocument prepareDacUnitAdapterInvokeWithMessParam(final String mhId, final XmlObject mess) {
        final InvokeDocument invokeXml = prepareDacUnitAdapterInvoke(mhId);
        final InvokeRq rq = invokeXml.getInvoke().getInvokeRq();
        rq.addNewParameters().addNewItem().setXml(mess);
        return invokeXml;
    }    
}
