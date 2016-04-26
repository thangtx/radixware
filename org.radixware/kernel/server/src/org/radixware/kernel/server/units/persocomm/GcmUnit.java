/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.units.persocomm;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.xmlbeans.XmlException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EHttpParameter;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.Maps;
import org.radixware.kernel.server.exceptions.DPCRecvException;
import org.radixware.kernel.server.exceptions.DPCSendException;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.utils.OptionsGroup;
import org.radixware.schemas.personalcommunications.MessageDocument;
import org.radixware.schemas.personalcommunications.PushNotificationAddress;
import org.radixware.schemas.personalcommunications.PushNotificationAddressDocument;
import org.radixware.schemas.personalcommunications.PushNotificationMessage;
import org.radixware.schemas.personalcommunications.PushNotificationMessageDocument;

/**
 * Google Cloud Messaging connector
 *
 * @author dsafonov
 */
public class GcmUnit extends PCUnit {

    private static final String FIELD_TO = "to";
    private static final String FIELD_REGISTRATION_IDS = "registration_ids";
    private static final String FIELD_COLLAPSE_KEY = "collapse_key";
    private static final String FIELD_TIME_TO_LIVE_SEC = "time_to_live";
    private static final String FIELD_DELAY_WHILE_IDLE = "delay_while_idle";
    private static final String FIELD_CONTENT_AVAILABLE = "content_available";
    private static final String FIELD_DATA = "data";
    private static final String FIELD_NOTIFICATION = "notification";
    private static final String FIELD_NTF_TITLE = "title";
    private static final String FIELD_NTF_BODY = "body";
    private static final String FIELD_NTF_SOUND = "sound";
    private static final String FIELD_NTF_ICON = "icon";
    private static final String FIELD_NTF_TAG = "tag";
    private static final String FIELD_NTF_COLOR = "color";
    private static final String FIELD_NTF_CLICK_ACTION = "click_action";
    private static final String DEFAULT_SOUND_NAME = "default";
    private static final String AUTH_HEADER = "Authorization";
    private static final String JSON_CONTENT_TYPE = "application/json";

    public GcmUnit(Instance instance, Long id, String title) {
        super(instance, id, title);
    }

    private String getMaskedApiKey() {
        if (options.gcmApiKey == null) {
            return null;
        }
        int clearHalfLen = 0;
        if (options.gcmApiKey.length() > 16) {
            clearHalfLen = 4;
        } else if (options.gcmApiKey.length() > 8) {
            clearHalfLen = 2;
        } else if (options.gcmApiKey.length() > 2) {
            clearHalfLen = 1;
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < options.gcmApiKey.length(); i++) {
            if (i < clearHalfLen || i >= options.gcmApiKey.length() - clearHalfLen) {
                sb.append(options.gcmApiKey.charAt(i));
            } else {
                sb.append("*");
            }
        }
        return sb.toString();
    }

    @Override
    public String optionsToString() {
        final OptionsGroup og = new OptionsGroup();
        og.add(PCMessages.SEND_PERIOD, options.sendPeriod)
                .add(PCMessages.GCM_API_KEY, getMaskedApiKey())
                .add(PCMessages.APP_NAME_MASK, options.addressMask);
        return og.toString();
    }

    @Override
    protected void send(MessageDocument m, Long id) throws DPCSendException {
        try {
            doSend(options.gcmApiKey, createSendJSONObj(m));
            sendCallback(id, null);
        } catch (Exception ex) {
            if (ex instanceof DPCSendException) {
                throw (DPCSendException) ex;
            }
            throw new DPCSendException("Unable to send message to GCM", ex);
        }
    }

    private JSONObject createSendJSONObj(final MessageDocument m) throws DPCSendException, XmlException {
        final PushNotificationAddress xAddress = PushNotificationAddressDocument.Factory.parse(m.getMessage().getAddressTo()).getPushNotificationAddress();
        final PushNotificationMessage xMessage = PushNotificationMessageDocument.Factory.parse(m.getMessage().getBody()).getPushNotificationMessage();

        final JSONObject jsonObj = new JSONObject();

        if (xAddress.isSetIsGroup()) {
            if (xAddress.getReceiverId() == null) {
                throw new DPCSendException("ReceiverId must be defined");
            }
            jsonObj.put(FIELD_TO, xAddress.getReceiverId());
        } else if (xAddress.getReceiverId() != null || (xAddress.isSetAdditionalReceivers() && !xAddress.getAdditionalReceivers().getReceiverIdList().isEmpty())) {
            final List<String> allReceivers = new ArrayList<>();
            if (xAddress.getReceiverId() != null) {
                allReceivers.add(xAddress.getReceiverId());
            }
            if (xAddress.isSetAdditionalReceivers()) {
                allReceivers.addAll(xAddress.getAdditionalReceivers().getReceiverIdList());
            }
            if (allReceivers.size() == 1) {
                jsonObj.put(FIELD_TO, allReceivers.get(0));
            } else {
                final JSONArray receivers = new JSONArray();
                receivers.addAll(allReceivers);
                jsonObj.put(FIELD_REGISTRATION_IDS, receivers);
            }
        } else {
            throw new DPCSendException("Receiver not defined");
        }

        if (xMessage.isSetCollapseKey()) {
            jsonObj.put(FIELD_COLLAPSE_KEY, xMessage.getCollapseKey());
        }

        if (xMessage.isSetData()) {
            //on Android messages with data wake up application by default,
            //but on iOS this flag should be set to wake up application.
            jsonObj.put(FIELD_CONTENT_AVAILABLE, true);
        }

        if (xMessage.isSetTimeToLiveSec()) {
            jsonObj.put(FIELD_TIME_TO_LIVE_SEC, xMessage.getTimeToLiveSec());
        }

        if (xMessage.isSetDelayWhileIdle()) {
            jsonObj.put(FIELD_DELAY_WHILE_IDLE, true);
        }

        if (xMessage.isSetBody() || xMessage.isSetTitle()) {
            JSONObject notificationObj = new JSONObject();
            if (xMessage.isSetTitle()) {
                notificationObj.put(FIELD_NTF_TITLE, xMessage.getTitle());
            }
            if (xMessage.isSetBody()) {
                notificationObj.put(FIELD_NTF_BODY, xMessage.getBody());
            }
            if (xMessage.isSetIcon()) {
                notificationObj.put(FIELD_NTF_ICON, xMessage.getIcon());
            }
            if (xMessage.isSetClickAction()) {
                notificationObj.put(FIELD_NTF_CLICK_ACTION, xMessage.getClickAction());
            }
            if (xMessage.isSetSound()) {
                notificationObj.put(FIELD_NTF_SOUND, DEFAULT_SOUND_NAME);
            }
            if (xMessage.isSetTag()) {
                notificationObj.put(FIELD_NTF_TAG, xMessage.getTag());
            }
            if (xMessage.isSetColor()) {
                notificationObj.put(FIELD_NTF_COLOR, xMessage.getColor());
            }
            jsonObj.put(FIELD_NOTIFICATION, notificationObj);
        }

        if (xMessage.isSetData()) {
            jsonObj.put(FIELD_DATA, Maps.fromXml(xMessage.getData()));
        }

        return jsonObj;
    }

    private void doSend(final String authToken, final JSONObject jsonObj) throws MalformedURLException, IOException, XmlException, DPCSendException {
        getTrace().put(EEventSeverity.DEBUG, "Sending: \n" + jsonObj.toJSONString() + "\n\nAuthToken: " + authToken, null, null, getEventSource(), true);
        final Proxy proxyObject = options.sendAddress.startsWith("https") ? getInstance().getHttpsProxyObject() : getInstance().getHttpProxyObject();
        final HttpURLConnection conn = (HttpURLConnection) new URL(options.sendAddress).openConnection(proxyObject);
        final byte[] bytes = jsonObj.toJSONString().getBytes("UTF-8");
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setFixedLengthStreamingMode(bytes.length);
        conn.setRequestMethod("POST");
        conn.setRequestProperty(EHttpParameter.HTTP_CONTENT_TYPE_ATTR.getValue(), JSON_CONTENT_TYPE);
        conn.setRequestProperty(AUTH_HEADER, "key=" + authToken);
        try (OutputStream out = conn.getOutputStream()) {
            out.write(bytes);
        }

        String response = null;
        if (conn.getResponseCode() != 200) {
            try {
                response = IOUtils.toString(conn.getErrorStream(), "UTF-8");
            } catch (IOException ex) {
                getTrace().put(EEventSeverity.DEBUG.getValue(), ExceptionTextFormatter.exceptionStackToString(ex), null, null, getEventSource(), false);
            }
            getTrace().put(EEventSeverity.ERROR, response == null ? "<empty error message>" : response, null, null, getEventSource(), true);
            throw new DPCSendException("Unable to send message: " + conn.getResponseCode() + " " + conn.getResponseMessage());
        } else {
            try {
                response = IOUtils.toString(conn.getInputStream(), "UTF-8");
            } catch (IOException ex) {
                getTrace().put(EEventSeverity.DEBUG.getValue(), ExceptionTextFormatter.exceptionStackToString(ex), null, null, getEventSource(), false);
            }
            getTrace().put(EEventSeverity.DEBUG, "Response: \n:" + (response == null ? "<empty response>" : response), null, null, getEventSource(), true);
        }

    }

    @Override
    protected void recvMessages() throws DPCRecvException {

    }

    @Override
    public String getUnitTypeTitle() {
        return PCMessages.GCM_UNIT_TYPE_TITLE;
    }

    @Override
    public Long getUnitType() {
        return EUnitType.DPC_GCM.getValue();
    }

}
