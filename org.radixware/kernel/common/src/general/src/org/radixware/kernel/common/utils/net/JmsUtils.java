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

package org.radixware.kernel.common.utils.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.jms.*;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.enums.EHttpParameter;
import org.radixware.kernel.common.enums.EJmsMessageFormat;
import org.radixware.kernel.common.enums.EJmsMessageProperty;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.kernel.common.utils.HttpFormatter;
import org.radixware.kernel.common.utils.SoapFormatter;
import org.xmlsoap.schemas.soap.envelope.EnvelopeDocument;
import org.xmlsoap.schemas.soap.envelope.Fault;


public class JmsUtils {

    public static String JMS_MESSAGE_ID = "jms-message-id";
    public static String JMS_MESSAGE_CORRELATION_ID = "jms-corresponding-message-id";
    public static final String JMS_BINDING_VER = "1.0";//fixed value required by spec: http://www.w3.org/TR/soapjms/
    public static final String HTTP_SOAP_ATTR_PREFIX = "HttpSoapAttr";

    public static Map<String, String> parseProps(final String propsString) {
        Map<String, String> props = null;
        if (propsString != null) {
            props = new HashMap<>();
            final String[] items = propsString.split("\n");
            if (items != null) {
                for (String item : items) {
                    int indexOfEq = item.indexOf('=');
                    if (indexOfEq != -1) {
                        props.put(item.substring(0, indexOfEq).trim(), item.substring(indexOfEq + 1).trim());
                    }
                }
            }
        }
        return props;
    }

    public static byte[] convertJmsMessageToHttpSoap(final Message message) throws IOException {
        return convertJmsMessageToHttpSoap(message, null);
    }

    public static byte[] convertJmsMessageToHttpSoap(final Message message, final Map<String, String> additionalHeaderAttrs) throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        String messageText = null;
        byte[] messageBytes = null;
        try {
            final Map<String, String> headerAttrs = new HashMap<>();

            final EJmsMessageProperty[] propsToTransfer = new EJmsMessageProperty[]{
                EJmsMessageProperty.SOAPJMS_CONTENT_TYPE,
                EJmsMessageProperty.SOAPJMS_REQUEST_URI,
                EJmsMessageProperty.SOAPJMS_SOAP_ACTION,
                null};
            final EHttpParameter[] correspondingAttrs = new EHttpParameter[]{
                EHttpParameter.HTTP_CONTENT_TYPE_ATTR,
                EHttpParameter.HTTP_REQ_PATH_PARAM,
                EHttpParameter.HTTP_SOAPACTION_ATTR,
                EHttpParameter.HTTP_CONNECTION_ATTR};

            transferJmsPropsToHttpAttrs(message, headerAttrs, propsToTransfer, correspondingAttrs);

            final String targetService = message.getStringProperty(EJmsMessageProperty.SOAPJMS_TARGET_SERVICE.getValue());
            if (targetService != null && !targetService.isEmpty()) {
                headerAttrs.put(EJmsMessageProperty.SOAPJMS_TARGET_SERVICE.getValue(), targetService);
            }

            if (headerAttrs.containsKey(EHttpParameter.HTTP_SOAPACTION_ATTR.getValue())) {
                final String soapAction = headerAttrs.get(EHttpParameter.HTTP_SOAPACTION_ATTR.getValue());
                //radix soap engine always wraps soapaction with double quotes. Don't do it twice.
                if (soapAction != null && soapAction.startsWith("\"") && soapAction.endsWith("\"")) {
                    headerAttrs.put(EHttpParameter.HTTP_SOAPACTION_ATTR.getValue(), soapAction.substring(1, soapAction.length() - 1));
                }
            }

            headerAttrs.put(JMS_MESSAGE_ID, message.getJMSMessageID());
            headerAttrs.put(JMS_MESSAGE_CORRELATION_ID, message.getJMSCorrelationID());

            boolean keepAlive = false;

            if (additionalHeaderAttrs != null) {
                for (Map.Entry<String, String> entry : additionalHeaderAttrs.entrySet()) {
                    headerAttrs.put(entry.getKey(), entry.getValue());
                }
            }

            for (Map.Entry<String, String> entry : headerAttrs.entrySet()) {
                if (EHttpParameter.HTTP_CONNECTION_ATTR.getValue().equals(entry.getKey()) && (entry.getValue() != null && "keep-alive".equals(entry.getValue().toLowerCase().trim()))) {
                    keepAlive = true;
                }
            }

            final XmlObject rq;
            if (message instanceof TextMessage) {
                messageText = ((TextMessage) message).getText();
                rq = getRq(EnvelopeDocument.Factory.parse(messageText));
            } else {
                int len = (int) ((BytesMessage) message).getBodyLength();
                messageBytes = new byte[len];
                int readCount = 0;
                while (readCount < len) {
                    readCount += ((BytesMessage) message).readBytes(messageBytes);
                }
                rq = getRq(EnvelopeDocument.Factory.parse(new ByteArrayInputStream(messageBytes)));
            }
            SoapFormatter.sendRequest(bos, rq, headerAttrs, "localhost", keepAlive);
            return bos.toByteArray();
        } catch (XmlException | JMSException ex) {
            if (messageBytes != null) {//byte message
                messageText = Hex.encode(messageBytes);
            }
            throw new IOException("Error on translating JMS message to http SOAP format" + (messageText == null ? "" : "\nMessage:\n" + messageText), ex);
        }
    }

    private static XmlObject getRq(final EnvelopeDocument envelopeDoc) {
        return envelopeDoc.getEnvelope().getBody();
    }

    public static Message convertHttpSoapMessageToJms(final byte[] httpSoapMessage, final Session session, final EJmsMessageFormat format) throws IOException, JMSException {
        final Message responseMessage;
        final Map<String, String> headerAttrs = new HashMap<String, String>();
        final byte[] messageContent = HttpFormatter.readMessage(new ByteArrayInputStream(httpSoapMessage), headerAttrs);
        if (format == EJmsMessageFormat.TEXT) {
            responseMessage = session.createTextMessage(new String(messageContent, HttpFormatter.DEFAULT_HTTP_CONTENT_CHAR_SET));
        } else {
            BytesMessage byteMessage = session.createBytesMessage();
            byteMessage.writeBytes(messageContent);
            responseMessage = byteMessage;
        }
        final XmlObject responseDocument = SoapFormatter.parseRequest(messageContent);
        if (responseDocument instanceof Fault) {
            responseMessage.setBooleanProperty(EJmsMessageProperty.SOAPJMS_IS_FAULT.getValue(), true);
        }

        final String httpRequestStr = headerAttrs.get("");
        if (httpRequestStr != null) {
            final String[] reqTokens = httpRequestStr.split(" ");
            if (reqTokens != null && reqTokens.length == 3) {
                headerAttrs.put(EHttpParameter.HTTP_REQ_PATH_PARAM.getValue(), reqTokens[1]);
            }
        }

        responseMessage.setStringProperty(EJmsMessageProperty.SOAPJMS_CONTENT_TYPE.getValue(), HttpFormatter.SOAP_CONTENT_TYPE);
        responseMessage.setStringProperty(EJmsMessageProperty.SOAPJMS_BINDING_VERSION.getValue(), JMS_BINDING_VER);
        final EHttpParameter[] propsToTransfer = new EHttpParameter[]{
            EHttpParameter.HTTP_SOAPACTION_ATTR,
            EHttpParameter.HTTP_REQ_PATH_PARAM,
            EHttpParameter.HTTP_CONNECTION_ATTR};
        final EJmsMessageProperty[] correspondingProps = new EJmsMessageProperty[]{
            EJmsMessageProperty.SOAPJMS_SOAP_ACTION,
            EJmsMessageProperty.SOAPJMS_REQUEST_URI,
            null};
        transferHttpAttrsToJmsProps(responseMessage, headerAttrs, propsToTransfer, correspondingProps);
        return responseMessage;
    }

    public static String encodeHttpAttrName(final EHttpParameter parameter) {
        if (parameter == null) {
            return null;
        }
        return HTTP_SOAP_ATTR_PREFIX + parameter.getValue();
    }

    private static void transferJmsPropsToHttpAttrs(final Message message, final Map<String, String> map, final EJmsMessageProperty[] properties, final EHttpParameter[] correspondingAttributes) throws JMSException {
        for (int i = 0; i < properties.length; i++) {
            final String propValue;
            if (properties[i] != null) {
                propValue = message.getStringProperty(properties[i].getValue());
            } else {
                propValue = message.getStringProperty(HTTP_SOAP_ATTR_PREFIX + correspondingAttributes[i].getValue());
            }
            if (propValue != null) {
                map.put(correspondingAttributes[i].getValue(), propValue);
            }
        }
    }

    private static void transferHttpAttrsToJmsProps(final Message message, final Map<String, String> attrsMap, final EHttpParameter[] attrsToTransfer, final EJmsMessageProperty[] correspondingProps) throws JMSException {
        if (attrsToTransfer != null) {
            for (int i = 0; i < attrsToTransfer.length; i++) {
                if (attrsMap.containsKey(attrsToTransfer[i].getValue())) {
                    if (correspondingProps[i] != null) {
                        message.setStringProperty(correspondingProps[i].getValue(), attrsMap.get(attrsToTransfer[i].getValue()));
                    } else {
                        message.setStringProperty(HTTP_SOAP_ATTR_PREFIX + attrsToTransfer[i].getValue(), attrsMap.get(attrsToTransfer[i].getValue()));
                    }
                }
            }
        }
    }
}
