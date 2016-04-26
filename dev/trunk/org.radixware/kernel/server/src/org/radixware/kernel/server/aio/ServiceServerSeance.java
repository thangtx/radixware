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

package org.radixware.kernel.server.aio;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EHttpParameter;
import org.radixware.kernel.common.enums.ESoapMessageType;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.soap.IServerSoapMessageProcessor;
import org.radixware.kernel.common.soap.ProcessException;
import org.radixware.kernel.common.soap.RadixSoapHelper;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.SoapFormatter;
import org.radixware.kernel.common.utils.SoapFormatter.FaultDetailWriter;
import org.radixware.kernel.server.aio.ChannelPort.Frame;

/**
 * Cеанс сервера сервиса. Создается в ServiceServer при получении запроса.
 * Закрывается при разрыве соединения, ошибках, а также после передачи ответа
 * если не keepConnect.
 *

 */
public class ServiceServerSeance implements EventHandler {

//	------------------------------------ Public -----------------------------------------------------	
    public ServiceServerSeance(ServiceServer parent, ChannelPort port, int rqWaitTimeout) {
        this.parent = parent;
        this.port = port;
        this.rqWaitTimeoutMillis = rqWaitTimeout * 1000;
        parent.dispatcher.waitEvent(new SocketChannelPort.ReceiveEvent(port), this, System.currentTimeMillis() + this.rqWaitTimeoutMillis);
        parent.dispatcher.waitEvent(new SocketChannelPort.DisconnectEvent(port), this, -1);
        parent.tracer.debug("Service server seance opened", false);
        port.startRead();
    }

    public void response(ServiceProcessFault exception, boolean keepConnect) {
        response(exception, null, keepConnect, null);
    }

    public void response(ServiceProcessFault exception, FaultDetailWriter faultWriter, boolean keepConnect) {
        if (faultWriter != null) {
            exception = new ServiceProcessFault(exception.code, exception.reason, exception.getMessage(), exception.getCause(), exception.preprocessedCauseStack, faultWriter);
        }
        response(exception, null, keepConnect, null);
    }

    public void response(ServiceProcessFault exception, boolean keepConnect, List<SoapFormatter.ResponseTraceItem> traceBuffer) {
        response(exception, null, keepConnect, traceBuffer);
    }

    public void response(XmlObject rsEnvBodyDoc, boolean keepConnect) {
        response(null, rsEnvBodyDoc, keepConnect, null);
    }

    private void response(ServiceProcessFault exception, XmlObject rsEnvBodyDoc, boolean keepConnect, List<SoapFormatter.ResponseTraceItem> traceBuffer) {
        try {
            if (processor == null) {
                throw new IllegalStateException("There was no request");
            }
            if (port == null) {
                parent.tracer.put(EEventSeverity.ERROR, "Can't send service response: connection is closed", null, null, false);
                return;
            }
            Frame frame = new Frame();
            if (exception != null) {
                try {
                    frame.packet = processor.wrapFault(exception, traceBuffer);
                } catch (ProcessException ex) {
                    parent.tracer.put(EEventSeverity.WARNING, "Error while preparing fault message: " + ExceptionTextFormatter.throwableToString(ex), null, null, false);
                    frame.packet = SoapFormatter.prepareFault(exception.code, exception.reason, exception.getFaultDetailWriter() == null ? new SoapFormatter.DefaultFaultDetailWriter(exception.getMessage(), exception.getCause(), null) : exception.getFaultDetailWriter(), traceBuffer);
                }
                frame.attrs = new HashMap<>(parent.faultFrameAttrs);
                RadixSoapHelper.logFaultSent(exception.code, exception.reason, exception.getMessage(), port.getShortDescription(), parent.tracer);
            } else {
                try {
                    frame.packet = processor.wrapResponce(rsEnvBodyDoc);
                } catch (ProcessException ex) {
                    parent.tracer.put(EEventSeverity.ERROR, "Error while forming responce: " + ExceptionTextFormatter.throwableToString(ex), null, null, false);
                }
                frame.attrs = new HashMap<>(parent.rsFrameAttrs);
                QName objQName = rsEnvBodyDoc.schemaType().getDocumentElementName();
                frame.attrs.put(EHttpParameter.HTTP_SOAPACTION_ATTR.getValue(), objQName.getNamespaceURI() + "#" + objQName.getLocalPart());
//                if (parent.tracer.getMinSeverity() <= EEventSeverity.DEBUG.getValue().longValue()) {
//                    parent.tracer.debug("Service response sent: " + rsEnvBodyDoc.xmlText(), true);
//                }
                RadixSoapHelper.logMessageSent(ESoapMessageType.RESPONSE, rsEnvBodyDoc, port.getShortDescription(), parent.tracer);
            }
            frame.attrs.put(EHttpParameter.HTTP_CONNECTION_ATTR.getValue(), keepConnect ? "keep-alive" : "close");
            try {
                if (port.isConnected()) {
                    port.send(frame.packet, frame.attrs);
                } else {
                    parent.tracer.put(EEventSeverity.ERROR, "Can't send service response: client have disconnected", null, null, false);
                    close();
                    return;
                }
            } catch (IOException e) {
                parent.tracer.put(EEventSeverity.ERROR, "Can't send service response: " + e, null, null, false);
                close();
                return;
            }
            if (!keepConnect || rqWaitTimeoutMillis <= 0) {
                close();
            } else {
                parent.tracer.debug("Service server seance waits next request", false);
                nextRequestWaiting = true;
                parent.dispatcher.waitEvent(new SocketChannelPort.ReceiveEvent(port), this, System.currentTimeMillis() + rqWaitTimeoutMillis);
                //port.startRead();
            }
        } finally {
            processor = null;
        }
    }

    public void close() {
        if (port == null) {
            return;
        }
        parent.dispatcher.unsubscribe(new SocketChannelPort.ReceiveEvent(port));
        parent.dispatcher.unsubscribe(new SocketChannelPort.DisconnectEvent(port));
        port.close();
        parent.seances.remove(this);
        parent.tracer.debug("Service server seance closed", false);
        port = null;
    }

    public boolean isConnected() {
        return port != null && port.isConnected();
    }
    //	--------------------------------- Private ----------------------------------------------------
    private final ServiceServer parent;
    private ChannelPort port;
    private final int rqWaitTimeoutMillis;
    private boolean nextRequestWaiting = false;
    private IServerSoapMessageProcessor processor = null;

    @Override
    public void onEvent(Event event) {
        if (event.getClass() == ChannelPort.ReceiveEvent.class) {
            if (event.isExpired) {
                if (!nextRequestWaiting) {
                    parent.tracer.put(EEventSeverity.WARNING, "Service request wait timeout", null, null, false);
                }
                close();
            } else {
                if (processor != null) {
                    throw new IllegalStateException("Request is already received");
                }
                processor = parent.messageProcessorFactory.createProcessor();
                SocketChannelPort.Frame frame = ((SocketChannelPort.ReceiveEvent) event).frame;
                XmlObject rq;
                try {
                    rq = processor.unwrapRequest(frame.packet);
                } catch (ProcessException e) {
                    parent.tracer.put(EEventSeverity.ERROR, "Invalid service request: " + ExceptionTextFormatter.exceptionStackToString(e), null, null, false);
                    close();
                    return;
                }
                if (parent.tracer.getMinSeverity() <= EEventSeverity.DEBUG.getValue().longValue()) {
                    RadixSoapHelper.logMessageReceived(ESoapMessageType.REQUEST, rq, port.getShortDescription(), parent.tracer);
                }
                if (!parent.dispatcher.notify(new ServiceServer.InvocationEvent(parent, this, rq, frame.attrs))) {
                    parent.tracer.put(EEventSeverity.WARNING, "Service request is not processed", null, null, false);
                    response(new ServiceProcessFault(ServiceProcessFault.FAULT_CODE_SERVER, "ServerMalfunction", "Request is not processed", null, null), false);
                }
            }
        } else if (event.getClass() == ChannelPort.DisconnectEvent.class) {
            close();
        } else {
            throw new RadixError("Invalid event " + event);
        }
    }
}
