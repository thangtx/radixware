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

package org.radixware.kernel.common.soap;

import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EHttpParameter;
import org.radixware.kernel.common.enums.ESoapMessageType;
import static org.radixware.kernel.common.enums.ESoapMessageType.CALLBACK_RESPONCE;
import static org.radixware.kernel.common.enums.ESoapMessageType.FAULT;
import static org.radixware.kernel.common.enums.ESoapMessageType.REQUEST;
import org.radixware.kernel.common.enums.ESoapOption;
import org.radixware.kernel.common.exceptions.ServiceCallRecvException;
import org.radixware.kernel.common.exceptions.ServiceCallSendException;
import org.radixware.kernel.common.exceptions.ServiceCallTimeout;
import org.radixware.kernel.common.sc.ServiceClient;
import org.radixware.kernel.common.sc.SyncClientConnection;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.kernel.common.utils.HttpFormatter;
import org.radixware.kernel.common.utils.SoapFormatter;
import org.radixware.kernel.common.utils.XmlObjectProcessor;


public class RadixSoapHelper {

    private static final int MIN_RECEIVE_TIMEOUT_MILLIS;
    private static final String MIN_RECEIVE_TIMEOUT_PROP_NAME = "radix.soap.min.receive.timeout";

    static {
        int minReceiveTimeoutMillis = -1;
        final String prop = System.getProperty(MIN_RECEIVE_TIMEOUT_PROP_NAME);
        if (prop != null) {
            try {
                minReceiveTimeoutMillis = Integer.parseInt(prop);
            } catch (Exception ex) {
                Logger.getLogger(RadixSoapHelper.class.getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }
        MIN_RECEIVE_TIMEOUT_MILLIS = minReceiveTimeoutMillis <= 0 ? 100 : minReceiveTimeoutMillis;
    }

    /**
     * Sends messageBytes as http frame. Pointer to RadixSoapMessage is used
     * only for obtaining meta information.
     *
     * @throws IOException
     */
    public static void send(final RadixSoapMessage messageMeta, final byte[] messageBytes, final SyncClientConnection connection, final LocalTracer tracer) throws IOException {
        final OutputStream os = connection.getOutputStream();
        if (messageMeta.getAttrs() == null) {
            messageMeta.setAttrs(new HashMap<String, String>());
        }
        messageMeta.getAttrs().put(EHttpParameter.HTTP_HOST_ATTR.getValue(), ServiceClient.getInetConnectionHostAddress(connection));
        send(messageMeta, messageBytes, os, connection.toString(), tracer, isDirtyDataLoggingEnabled(connection));
    }

    public static void send(final RadixSoapMessage messageMeta, final byte[] messageBytes, final OutputStream os, final String connectionInfo, final LocalTracer tracer, boolean logDirtyData) throws IOException {
        try {
            switch (messageMeta.getType()) {
                case REQUEST:
                    SoapFormatter.sendRequest(messageBytes, os, messageMeta.getAttrs(), null, messageMeta.getKeepConnectTimeSec() > 0);
                    break;
                case CALLBACK_RESPONCE:
                case RESPONSE:
                    SoapFormatter.sendResponse(os, messageBytes, messageMeta.getAttrs());
                    break;
                case FAULT:
                    SoapFormatter.sendFault(messageBytes, os);
            }

            os.flush();

            if (tracer != null && tracer.getMinSeverity() <= EEventSeverity.DEBUG.getValue()) {
                logMessageSent(messageMeta.getType(), messageMeta.getBodyDocument(), connectionInfo, logDirtyData ? messageBytes : null, tracer);
            }
        } catch (IOException ex) {
            throw new RadixSoapIOExceptionOnSend(ex);
        }
    }

    public static byte[] receiveResponceData(final SyncClientConnection connection, final RadixSoapMessage outMessageMeta, final Map<String, String> responceAttrs, final LocalTracer tracer) throws IOException {
        try {
            final int receiveTimeoutMillis;
            if (outMessageMeta.getReceiveTimeoutSec() <= 0) {
                receiveTimeoutMillis = -1;
            } else {
                receiveTimeoutMillis = Math.max(outMessageMeta.getReceiveTimeoutSec() * 1000 - outMessageMeta.getSpentReceiveMillis(), MIN_RECEIVE_TIMEOUT_MILLIS);
            }
            connection.setReadTimeOut(receiveTimeoutMillis);
            final byte[] data = HttpFormatter.readMessage(connection.getInputStream(), responceAttrs, tracer);
            if (tracer != null && isDirtyDataLoggingEnabled(connection)) {
                logDataReceived(ESoapMessageType.RESPONSE, data, connection.toString(), tracer);
            }
            return data;
        } catch (IOException ex) {
            throw new RadixSoapIOExceptionOnReceive(ex);
        }
    }

    public static void logDataReceived(final ESoapMessageType type, final byte[] data, final String connectionInfo, final LocalTracer tracer) {
        if (tracer != null && tracer.getMinSeverity() <= EEventSeverity.DEBUG.getValue()) {
            tracer.debug(type + " data received from " + connectionInfo + ": " + Hex.encode(data), true);
        }
    }

    public static void logDataSent(final ESoapMessageType type, final byte[] data, final String connectionInfo, final LocalTracer tracer) {
        if (tracer != null && tracer.getMinSeverity() <= EEventSeverity.DEBUG.getValue()) {
            tracer.debug(type + " data sent to " + connectionInfo + ": " + Hex.encode(data), true);
        }
    }

    public static void logFaultSent(final String faultCode, final String faultReason, final String faultMessage, final String connectionInfo, final LocalTracer tracer) {
        if (tracer != null && tracer.getMinSeverity() <= EEventSeverity.DEBUG.getValue()) {
            tracer.debug(ESoapMessageType.FAULT + " sent to " + connectionInfo + ": " + String.valueOf(faultCode) + "-" + String.valueOf(faultReason) + "\n" + String.valueOf(faultMessage), true);
        }
    }

    public static void logMessageSent(final ESoapMessageType type, final XmlObject message, final String connectionInfo, final LocalTracer tracer) {
        logMessageSent(type, message, connectionInfo, null, tracer);
    }

    public static void logMessageSent(final ESoapMessageType type, final XmlObject message, final String connectionInfo, final byte[] completeMessage, final LocalTracer tracer) {
        if (tracer != null && tracer.getMinSeverity() <= EEventSeverity.DEBUG.getValue()) {
            tracer.debug(type + " (" + getSoapBodyRootElementName(message) + ") sent to " + connectionInfo + ":\nPayload:\n" + XmlObjectProcessor.toSeparateXml(message) + (completeMessage != null ? "\nComplete message: " + Hex.encode(completeMessage) : ""), true);//RADIX-1469
        }
    }

    public static void logMessageReceived(final ESoapMessageType type, final XmlObject rs, final String connectionInfo, final LocalTracer tracer) {
        logMessageReceived(type, rs, connectionInfo, null, tracer);
    }

    public static void logMessageReceived(final ESoapMessageType type, final XmlObject rs, final String connectionInfo, final byte[] completeMessage, final LocalTracer tracer) {
        if (tracer != null && tracer.getMinSeverity() <= EEventSeverity.DEBUG.getValue()) {
            tracer.debug(type + " (" + getSoapBodyRootElementName(rs) + ") received from " + connectionInfo + ":\nPayload:\n" + XmlObjectProcessor.toSeparateXml(rs) + (completeMessage != null ? "\nComplete message: " + Hex.encode(completeMessage) : ""), true);//RADIX-1469
        }
    }

    public static RadixSoapMessage createMessageFromResponce(final XmlObject rs, final Map<String, String> responceAttrs, final RadixSoapMessage outMessage) {
        return new RadixSoapMessage(rs, ESoapMessageType.RESPONSE, responceAttrs, outMessage.getResultClass(), outMessage.getSystemId(), outMessage.getThisInstanceId(), outMessage.getServiceUri(), null, outMessage.getDestinationInfo(), outMessage.getKeepConnectTimeSec(), outMessage.getReceiveTimeoutSec(), outMessage.getConnectTimeoutSec());
    }

    public static RadixSoapMessage createFaultMessage(final String faultCode, final String faultString, final String faultMessage, final Throwable cause, final String preprocessedCauseStack, final List<SoapFormatter.ResponseTraceItem> traceBuffer) {
        final XmlObject faultDocument = SoapFormatter.prepareFaultDocumentWithoutEnvelope(new QName(faultCode), faultString, new SoapFormatter.DefaultFaultDetailWriter(faultMessage, cause, preprocessedCauseStack), traceBuffer);
        return new RadixSoapMessage(faultDocument, ESoapMessageType.FAULT, null, null, null, null, null, null, null, -1, -1, -1);
    }

    public static void processIOException(final IOException e, final ESoapMessageType operationType, final String destinationInfo) throws InterruptedException, ServiceCallTimeout, ServiceCallRecvException, ServiceCallSendException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }
        Throwable realCause = null;
        if (e instanceof RadixSoapIOException) {
            realCause = e.getCause();
        }
        if (realCause == null) {
            realCause = e;
        }
        if (realCause instanceof ServiceCallTimeout) {
            throw (ServiceCallTimeout) realCause;
        }
        if (realCause instanceof SocketTimeoutException) {
            throw new ServiceCallTimeout(createCallTimeoutMessage(operationType, destinationInfo), realCause);
        } else {
            final boolean onSend = e instanceof RadixSoapIOExceptionOnSend;
            final String exMess = "Error " + (onSend ? "on" : "after") + " sending " + operationType + " to " + destinationInfo + "': " + realCause.getMessage();
            if (onSend) {
                throw new ServiceCallSendException(exMess, realCause);
            } else {
                throw new ServiceCallRecvException(exMess, realCause);
            }
        }
    }

    public static boolean isDirtyDataLoggingEnabled(final SyncClientConnection connection) {
        if (connection.getSapOptions() != null) {
            return isDirtyDataLoggingEnabled(connection.getSapOptions().getAdditionalAttrs());
        }
        return false;
    }

    public static boolean isDirtyDataLoggingEnabled(final Map<String, String> attrs) {
        if (attrs == null) {
            return false;
        }
        return Boolean.TRUE.toString().equals(attrs.get(ESoapOption.LOG_DIRTY_DATA.getValue()));
    }

    private static String getSoapBodyRootElementName(final XmlObject xmlObj) {
        try {
            if (xmlObj.getDomNode().getLocalName() != null && !("Body".equals(xmlObj.getDomNode().getLocalName()) && SoapFormatter.SOAP_NS.equals(xmlObj.getDomNode().getNamespaceURI()))) {
                return xmlObj.getDomNode().getLocalName();//if xmlObj is not document
            }
            return xmlObj.getDomNode().getChildNodes().item(0).getLocalName();//if xmlObj is document
        } catch (RuntimeException ex) {
            return "<unknown>";
        }
    }

    public static String createCallTimeoutMessage(final RadixSoapMessage message) {
        return createCallTimeoutMessage(message.getType(), message.getDestinationInfo());
    }

    public static String createCallTimeoutMessage(final ESoapMessageType operationType, final String destinationInfo) {
        return "Timeout on sending " + operationType + "  to " + destinationInfo;
    }
}