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
package org.radixware.kernel.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.radixware.kernel.common.enums.EHttpParameter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.xmlsoap.schemas.soap.envelope.EnvelopeDocument;
import org.xmlsoap.schemas.soap.envelope.Fault;
import org.xmlsoap.schemas.soap.envelope.FaultDocument;

import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.trace.LocalTracer;

public class SoapFormatter {

    public static final String SOAP_NS = "http://schemas.xmlsoap.org/soap/envelope/";
    static private XmlOptions xmlSaveOptions = null;
    public static final Charset UTF_8_CHARSET = Charset.forName("UTF-8");

    static public XmlOptions getXmlSaveOptions() {
        if (xmlSaveOptions != null) {
            return xmlSaveOptions;
        }
        final XmlOptions opt = new XmlOptions();
        final HashMap<String, String> m = new HashMap<String, String>();
        m.put(SOAP_NS, "SOAP-ENV");
        m.put("http://www.w3.org/2001/XMLSchema-instance", "xsi");
        m.put("http://schemas.radixware.org/eas.xsd", "eas");
        m.put("http://schemas.radixware.org/aas.xsd", "aas");
        m.put("http://schemas.radixware.org/dbudef.xsd", "dbu");
        m.put("http://schemas.radixware.org/types.xsd", "t");
        m.put(FLT_DET_XSD, "fltdet");
        opt.setSaveSuggestedPrefixes(m);
        opt.setSaveNamespacesFirst();
        opt.setSaveAggressiveNamespaces(); //RADIX-1470: to prevent multiple declaration of same namespace,
        //            saver will do this by passing over the document twice
        opt.setUseDefaultNamespace();
        xmlSaveOptions = opt;
        return opt;
    }

    /**
     * @param mess
     * @return SOAP envelope body content
     * @throws IOException
     */
    static private XmlObject parseMessage(final byte[] mess) throws IOException {
        return getInnerContent(parseSoapMessage(mess));
    }

    public static XmlObject getInnerContent(final XmlObject xmlObj) throws IOException {
        if (xmlObj == null) {
            return null;
        }
        final XmlCursor cursor = xmlObj.newCursor();
        cursor.toFirstChild();
        final XmlObject m = cursor.getObject();
        //System.out.println("m = " + m.xmlText());
        cursor.dispose();
        if (m == null) {
            throw new IOException("Empty message");
        }
        return m;
    }

    private static XmlObject parseSoapMessage(final byte[] mess) throws IOException {
        final EnvelopeDocument envDoc;
        try {
            if (mess == null) {
                throw new XmlException("(null) soap message");
            }
            envDoc = EnvelopeDocument.Factory.parse(new ByteArrayInputStream(mess));
            //System.out.println("envDoc = " + envDoc.xmlText());
        } catch (IOException e) {
            throw new RadixError("Unexpected exception: " + ExceptionTextFormatter.exceptionStackToString(e), e);
        } catch (XmlException e) {
            String messStr;
            boolean isMessInHex = false;
            try {
                messStr = mess == null ? "null" : new String(mess, UTF_8_CHARSET);
            } catch (Exception e2) {
                messStr = Hex.encode(mess);
                isMessInHex = true;
            }
            throw new IOException("Can't parse message" + (isMessInHex ? ". \nMessage text in HEX: \n" : ": \n") + messStr + "\nParse exception: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
        return envDoc.getEnvelope().getBody();
    }

    /**
     * @param mess
     * @return SOAP envelope body content
     * @throws IOException
     */
    static public XmlObject parseSoapRequest(byte[] mess) throws IOException {
        return parseSoapMessage(mess);
    }

    /**
     * @param mess
     * @return First child of the SOAP envelope body content
     * @throws IOException
     */
    static public XmlObject parseRequest(byte[] mess) throws IOException {
        return parseMessage(mess);
    }

    /**
     * @return SOAP envelope body content, casted to, resultClass
     * @throws IOException
     * @throws ServiceCallFault - if Fault received
     */
    static public XmlObject parseResponse(byte[] mess, Class resultClass) throws IOException, ServiceCallFault {
        return parseResponse(parseMessage(mess), resultClass);
    }

    /**
     * @return SOAP envelope body content, casted to, resultClass
     * @throws IOException
     * @throws ServiceCallFault - if Fault received
     */
    static public XmlObject parseResponse(XmlObject rs, Class resultClass) throws IOException, ServiceCallFault {
        if (rs instanceof Fault) {
            final Fault fault = (Fault) rs;
            throw new ServiceCallFault(fault.getFaultcode() == null ? null : fault.getFaultcode().getLocalPart(), fault.getFaultstring(), fault.getDetail());
        }
        if (resultClass != null && !resultClass.isInstance(rs)) {
            try {								//try to reparse					
                final Class<?> c = resultClass.getClassLoader().loadClass(resultClass.getName() + "$Factory");
                final Method m = c.getMethod("parse", new Class[]{org.w3c.dom.Node.class});
                rs = (XmlObject) m.invoke(null, new Object[]{rs.getDomNode()});
            } catch (Exception e) {
                throw new IOException("Can't reparse message: \n" + (rs == null ? "null" : rs.xmlText()) + "\nParse exception: " + ExceptionTextFormatter.getExceptionMess(e), e);
            }
            if (!resultClass.isInstance(rs)) {
                throw new IOException("Invalid message class. Expected \"" + resultClass.getName() + "\" got \"" + rs.getClass().getName() + "\"");
            }
        }
        return rs;
    }

    /**
     * @param socketInputStream
     * @return SOAP envelope body content
     * @throws SocketTimeoutException
     * @throws IOException
     * @throws ServiceCallFault - if Fault received
     */
    static public XmlObject receiveRequest(final InputStream socketInputStream, final Map<String, String> headerAttrs) throws SocketTimeoutException, IOException {
        return receiveRequest(socketInputStream, headerAttrs, null);
    }

    static public XmlObject receiveRequest(final InputStream socketInputStream, final Map<String, String> headerAttrs, final LocalTracer tracer) throws SocketTimeoutException, IOException {
        return getInnerContent(receiveSoapRequest(socketInputStream, headerAttrs, tracer));
    }

    static public XmlObject receiveSoapRequest(final InputStream socketInputStream, final Map<String, String> headerAttrs, final LocalTracer tracer) throws SocketTimeoutException, IOException {
        byte[] mess = HttpFormatter.readMessage(socketInputStream, headerAttrs, tracer);
        XmlObject rs = parseSoapRequest(mess);
        return rs;
    }

    /**
     * @param socketInputStream
     * @param resultClass - expected class of SOAP envelope body
     * @return SOAP envelope body content
     * @throws SocketTimeoutException
     * @throws IOException
     * @throws ServiceCallFault - if Fault received
     */
    static public XmlObject receiveResponse(InputStream socketInputStream, final Map<String, String> headerAttrs, final Class resultClass) throws SocketTimeoutException, IOException, ServiceCallFault {
        return receiveResponse(socketInputStream, headerAttrs, resultClass, null);
    }

    static public XmlObject receiveResponse(InputStream socketInputStream, final Map<String, String> headerAttrs, final Class resultClass, final LocalTracer tracer) throws SocketTimeoutException, IOException, ServiceCallFault {
        final byte[] mess = HttpFormatter.readMessage(socketInputStream, headerAttrs, tracer);
        final XmlObject rs = parseResponse(mess, resultClass);
        return rs;
    }

    static public byte[] prepareMessage(final XmlObject rqEnvBodyDoc) {
        final ByteArrayOutputStream xmlOutputStream = new ByteArrayOutputStream(4096);
        try {
            createSoapEnvelope(rqEnvBodyDoc).save(xmlOutputStream, getXmlSaveOptions());
        } catch (IOException e) {
            throw new RadixError("Unexpected exception: " + ExceptionTextFormatter.getExceptionMess(e), e);
        } catch (ArrayIndexOutOfBoundsException e) {
            //XmlBeans bug workaround. See https://issues.apache.org/jira/browse/XMLBEANS-404
            //and https://jira.compassplus.ru/browse/RADIX-7362            
            return createSoapEnvelope(rqEnvBodyDoc).xmlText(getXmlSaveOptions()).getBytes(UTF_8_CHARSET);
        }
        return xmlOutputStream.toByteArray();
    }

    public static XmlObject createSoapEnvelope(final XmlObject bodyDoc) {
        final EnvelopeDocument envelopeDoc = EnvelopeDocument.Factory.newInstance();
        envelopeDoc.addNewEnvelope().addNewBody().set(bodyDoc);
        return envelopeDoc;
    }

    static public void sendRequest(final OutputStream socketOutputStream, final XmlObject rqEnvBodyDoc, final String host, final boolean keepConnect) throws IOException {
        sendRequest(socketOutputStream, rqEnvBodyDoc, null, host, keepConnect);
    }

    static public void sendRequest(final OutputStream socketOutputStream, final XmlObject rqEnvBodyDoc, final Map<String, String> soapRequestParams, final String host, final boolean keepConnect) throws IOException {
        sendRequest(prepareMessage(rqEnvBodyDoc), socketOutputStream, prepareHeaderAttrs(rqEnvBodyDoc, soapRequestParams), host, keepConnect);
    }

    public static Map<String, String> prepareHeaderAttrs(final XmlObject rqEnvBodyDoc, final Map<String, String> soapRequestParams) {
        final QName objQName = rqEnvBodyDoc.schemaType().getDocumentElementName();
        final String soapAction = getAttr(EHttpParameter.HTTP_SOAPACTION_ATTR.getValue(), soapRequestParams, objQName != null ? objQName.getNamespaceURI() + "#" + objQName.getLocalPart() : "");
        final Map<String, String> processedHeaderAttrs = new HashMap<>();
        if (soapRequestParams != null) {
            processedHeaderAttrs.putAll(soapRequestParams);
        }
        processedHeaderAttrs.put(EHttpParameter.HTTP_SOAPACTION_ATTR.getValue(), soapAction);
        return processedHeaderAttrs;
    }

    static public void sendRequest(final byte[] mess, final OutputStream socketOutputStream, final Map<String, String> requestParams, String host, final boolean keepConnect) throws IOException {
        final String requestUri = getAttr(EHttpParameter.HTTP_REQ_PATH_PARAM.getValue(), requestParams, "/");
        final String soapAction = getAttr(EHttpParameter.HTTP_SOAPACTION_ATTR.getValue(), requestParams, "");
        if (requestParams != null) {
            requestParams.remove(EHttpParameter.HTTP_REQ_PATH_PARAM.getValue());
            requestParams.remove(EHttpParameter.HTTP_SOAPACTION_ATTR.getValue());
            if (host == null) {
                host = requestParams.get(EHttpParameter.HTTP_HOST_ATTR.getValue());
            }
        }

        final String header = HttpFormatter.prepareSoapRequestHeader(host, requestUri, keepConnect, soapAction, requestParams, mess.length);
        writeStringToStream(header, socketOutputStream);
        socketOutputStream.write(mess);
        socketOutputStream.flush();
    }

    static public void sendResponse(final OutputStream socketOutputStream, final byte[] mess, final Map<String, String> attrs) throws IOException {
        final String soapAction = attrs == null ? "" : attrs.get(EHttpParameter.HTTP_SOAPACTION_ATTR.getValue());
        final boolean keepConnect = attrs == null ? false : HttpFormatter.KEEP_ALIVE.equals(attrs.get(EHttpParameter.HTTP_CONNECTION_ATTR.getValue()));
        final String header = HttpFormatter.prepareSoapResponseHeader(keepConnect, soapAction, attrs, mess.length);
        writeStringToStream(header, socketOutputStream);
        socketOutputStream.write(mess);
        socketOutputStream.flush();
    }

    private static void writeStringToStream(String mess, OutputStream socketOutputStream) throws IOException {
        socketOutputStream.write(mess.getBytes(UTF_8_CHARSET));
    }

    static public void sendResponse(final OutputStream socketOutputStream, final XmlObject rsEnvBodyDoc, final boolean keepConnect) throws IOException {
        final byte[] mess = prepareMessage(rsEnvBodyDoc);
        final QName objQName = rsEnvBodyDoc.schemaType().getDocumentElementName();
        final Map<String, String> attrs = new HashMap<>();
        attrs.put(EHttpParameter.HTTP_SOAPACTION_ATTR.getValue(), objQName != null ? objQName.getNamespaceURI() + "#" + objQName.getLocalPart() : "");
        if (keepConnect) {
            attrs.put(EHttpParameter.HTTP_CONNECTION_ATTR.getValue(), HttpFormatter.KEEP_ALIVE);
        }
        sendResponse(socketOutputStream, mess, attrs);
    }

    static public final class ResponseTraceItem {

        public final String level;
        public final String text;

        public ResponseTraceItem(final String level, final String text) {
            this.level = level;
            this.text = text;
        }
    }
    public static final String FLT_DET_XSD = "http://schemas.radixware.org/faultdetail.xsd";

    public static interface FaultDetailWriter {

        void writeTo(Fault fault, List<ResponseTraceItem> requestProcessingTraceItems);
    }

    public static class DefaultFaultDetailWriter implements FaultDetailWriter {

        private final String faultMessage;
        private final Throwable cause;
        private final String preprocessedCauseStack;

        public DefaultFaultDetailWriter(
                final String faultMessage,
                final Throwable cause,
                final String preprocessedCauseStack) {
            this.faultMessage = faultMessage;
            this.cause = cause;
            this.preprocessedCauseStack = preprocessedCauseStack;
        }

        @Override
        public void writeTo(final Fault fault, final List<ResponseTraceItem> traceItems) {
            final Node detail = fault.addNewDetail().getDomNode();
            final Element message = detail.getOwnerDocument().createElementNS(FLT_DET_XSD, "Message");
            message.appendChild(detail.getOwnerDocument().createTextNode(faultMessage));
            detail.appendChild(message);
            if (cause != null) {
                final Element exXml = detail.getOwnerDocument().createElementNS(FLT_DET_XSD, "Exception");
                final Element messXml = detail.getOwnerDocument().createElementNS(FLT_DET_XSD, "Message");
                messXml.appendChild(detail.getOwnerDocument().createTextNode(cause.getMessage()));
                exXml.appendChild(messXml);
                final Element classXml = detail.getOwnerDocument().createElementNS(FLT_DET_XSD, "Class");
                classXml.appendChild(detail.getOwnerDocument().createTextNode(cause.getClass().getName()));
                exXml.appendChild(classXml);
                final Element stackXml = detail.getOwnerDocument().createElementNS(FLT_DET_XSD, "Stack");
                final String causeStack;
                if (preprocessedCauseStack == null) {
                    causeStack = ExceptionTextFormatter.exceptionStackToString(cause);
                } else {
                    causeStack = preprocessedCauseStack;
                }
                stackXml.appendChild(detail.getOwnerDocument().createTextNode(causeStack));
                exXml.appendChild(stackXml);
                detail.appendChild(exXml);
            }
            if (traceItems != null && !traceItems.isEmpty()) {
                final Element trace = detail.getOwnerDocument().createElementNS(FLT_DET_XSD, "Trace");
                for (ResponseTraceItem ti : traceItems) {
                    final Element item = detail.getOwnerDocument().createElementNS(FLT_DET_XSD, "Item");
                    item.appendChild(detail.getOwnerDocument().createTextNode(XmlUtils.getSafeXmlString(ti.text)));
                    item.setAttribute("Level", ti.level);
                    trace.appendChild(item);
                }
                detail.appendChild(trace);
            }
        }
    }

    static public byte[] prepareFault(final String faultCode, final String faultString, final FaultDetailWriter faultDetailWriter, final List<ResponseTraceItem> traceBuffer) {
        return prepareFault(new QName(faultCode), faultString, faultDetailWriter, traceBuffer);
    }

    public static XmlObject prepareFaultDocument(final QName faultCode, final String faultString, final FaultDetailWriter faultDetailWriter, final List<ResponseTraceItem> traceBuffer) {
        return wrapToSoapEnvelope(prepareFaultDocumentWithoutEnvelope(faultCode, faultString, faultDetailWriter, traceBuffer));
    }

    public static FaultDocument prepareFaultDocumentWithoutEnvelope(final QName faultCode, final String faultString, final FaultDetailWriter faultDetailWriter, final List<ResponseTraceItem> traceBuffer) {
        final FaultDocument faultDoc = FaultDocument.Factory.newInstance();
        final Fault fault = faultDoc.addNewFault();
        fault.setFaultcode(faultCode);
        fault.setFaultstring(faultString);
        faultDetailWriter.writeTo(fault, traceBuffer);
        return faultDoc;
    }

    public static EnvelopeDocument wrapToSoapEnvelope(final XmlObject bodyContentObject) {
        final EnvelopeDocument envelopeDoc = EnvelopeDocument.Factory.newInstance();

        envelopeDoc.addNewEnvelope().addNewBody().set(bodyContentObject);
        return envelopeDoc;
    }

    static public byte[] prepareFault(final QName faultCode, final String faultString, final FaultDetailWriter faultDetailWriter, final List<ResponseTraceItem> traceBuffer) {
        return getBytes(prepareFaultDocument(faultCode, faultString, faultDetailWriter, traceBuffer));
    }

    static public void sendFault(final OutputStream socketOutputStream, final ServiceProcessFault flt, final List<ResponseTraceItem> traceBuffer) throws IOException {
        SoapFormatter.sendFault(socketOutputStream, flt.code, flt.reason, flt.getFaultDetailWriter(), traceBuffer);
    }

    static public void sendFault(final OutputStream socketOutputStream, final String faultCode, final String faultString, final FaultDetailWriter faultDetailWriter, final List<ResponseTraceItem> traceBuffer) throws IOException {
        sendFault(socketOutputStream, new QName(faultCode), faultString, faultDetailWriter, traceBuffer);
    }

    static public void sendFault(final OutputStream socketOutputStream, final QName faultCode, final String faultString, final FaultDetailWriter faultDetailWriter, final List<ResponseTraceItem> traceBuffer) throws IOException {
        sendFault(socketOutputStream, prepareFaultDocument(faultCode, faultString, faultDetailWriter, traceBuffer));
    }

    public static void sendFault(final byte[] mess, final OutputStream outputStream) throws IOException {
        sendFault(mess, outputStream, false);
    }

    public static void sendFault(final byte[] mess, final OutputStream outputStream, final boolean keepAlive) throws IOException {
        final String header = HttpFormatter.prepareSoapFaultHeader(null, mess.length, keepAlive);
        writeStringToStream(header, outputStream);
        outputStream.write(mess);
        outputStream.flush();
    }

    public static void sendFault(final OutputStream outputStream, final XmlObject faultDocument) throws IOException {
        final byte[] mess = getBytes(faultDocument);
        sendFault(mess, outputStream);
    }

    public static byte[] getBytes(final XmlObject xmlObject) {
        final ByteArrayOutputStream xmlOutputStream = new ByteArrayOutputStream(2048);
        try {
            xmlObject.save(xmlOutputStream, getXmlSaveOptions());
        } catch (IOException e) {
            throw new RadixError("Unexpected exception", e);
        } catch (ArrayIndexOutOfBoundsException e) {
            //XmlBeans bug workaround. See https://issues.apache.org/jira/browse/XMLBEANS-404
            //and https://jira.compassplus.ru/browse/RADIX-7362
            return xmlObject.xmlText(getXmlSaveOptions()).getBytes(UTF_8_CHARSET);
        }
        return xmlOutputStream.toByteArray();
    }

    private static String getAttr(final String name, final Map<String, String> map, final String defaultValue) {
        if (map == null || !map.containsKey(name)) {
            return defaultValue;
        }
        return map.get(name);
    }
}
