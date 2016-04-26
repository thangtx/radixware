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
import org.radixware.kernel.server.units.persocomm.interfaces.IDatabaseConnectionAccess;
import org.radixware.kernel.server.units.persocomm.interfaces.IExtendedRadixTrace;
import org.radixware.kernel.server.utils.OptionsGroup;
import org.radixware.schemas.personalcommunications.MessageDocument;
import org.radixware.schemas.personalcommunications.MessageStatistics;
import org.radixware.schemas.personalcommunications.PushNotificationAddress;
import org.radixware.schemas.personalcommunications.PushNotificationAddressDocument;
import org.radixware.schemas.personalcommunications.PushNotificationMessage;
import org.radixware.schemas.personalcommunications.PushNotificationMessageDocument;

public class NewGcmUnit extends NewPCUnit {

    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final String AUTH_HEADER = "Authorization";
    private static final String DEFAULT_SOUND_NAME = "default";

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

    public NewGcmUnit(final Instance instModel, final Long id, final String title) {
        super(instModel, id, title);
    }

    public NewGcmUnit(final Instance instModel, final Long id, final String title, final IDatabaseConnectionAccess dbca, final IExtendedRadixTrace trace) {
        super(instModel, id, title, dbca, trace);
    }

    @Override
    public CommunicationAdapter getCommunicationAdapter(final CommunicationMode mode) throws DPCRecvException, DPCSendException {
        switch (mode) {
            case TRANSMIT:
                try {
                    return this.new WriteGcmCommunicationAdapter(new URL(options.sendAddress), options.gcmApiKey);
                } catch (MalformedURLException ex) {
                    throw new DPCSendException(ex.getMessage());
                }
            case RECEIVE:
                return this.new ReadGcmCommunicationAdapter();
            default:
                throw new UnsupportedOperationException("Communication mode [" + mode + "] is not supported in the [" + this.getClass().getSimpleName() + "] adapter!");
        }
    }

    @Override
    public OptionsGroup optionsGroup(final Options options) {
        return new OptionsGroup().add(PCMessages.SEND_PERIOD, options.sendPeriod)
                .add(PCMessages.GCM_API_KEY, getMaskedApiKey())
                .add(PCMessages.APP_NAME_MASK, options.addressMask);
    }

    @Override
    protected void checkOptions(Options options) throws Exception {
        if (options.sendAddress == null || options.sendAddress.isEmpty()) {
            throw new IllegalArgumentException("Missing send address for Gcm unit!");
        } else if (options.gcmApiKey == null || options.gcmApiKey.isEmpty()) {
            throw new IllegalArgumentException("Missing API key for Gcm unit!");
        } else {
            try {
                new URL(options.sendAddress);
            } catch (MalformedURLException exc) {
                throw new IllegalArgumentException("Invalid URL format [" + options.sendAddress + "] for send address: " + exc.getMessage());
            }
        }
    }

    @Override public boolean supportsTransmitting() {return true;}
    @Override public boolean supportsReceiving() {return false;}
    
    @Override
    public String getUnitTypeTitle() {
        return PCMessages.GCM_UNIT_TYPE_TITLE;
    }

    @Override
    public Long getUnitType() {
        return EUnitType.DPC_GCM.getValue();
    }

    protected HttpURLConnection getConnection(final URL address) throws IOException {
        return (HttpURLConnection) address.openConnection(address.getProtocol().equals("https") ? getInstance().getHttpsProxyObject() : getInstance().getHttpProxyObject());
    }

    private String getMaskedApiKey() {
        if (options.gcmApiKey == null) {
            return null;
        } else {
            final int clearHalfLen = options.gcmApiKey.length() > 16 ? 4 : options.gcmApiKey.length() > 8 ? 2 : options.gcmApiKey.length() > 2 ? 1 : 0;
            final StringBuilder sb = new StringBuilder();

            for (int i = 0; i < options.gcmApiKey.length(); i++) {
                sb.append(i < clearHalfLen || i >= options.gcmApiKey.length() - clearHalfLen ? options.gcmApiKey.charAt(i) : "*");
            }
            return sb.toString();
        }
    }

    protected class WriteGcmCommunicationAdapter implements CommunicationAdapter<JSONObject> {

        private final URL sendAddress;
        private final String authToken;
        private MessageStatistics stat;

        public WriteGcmCommunicationAdapter(final URL sendAddress, final String autiToken) {
            this.authToken = autiToken;
            this.sendAddress = sendAddress;
        }

        @Override
        public JSONObject prepareMessage(Long messageId, MessageDocument md) throws DPCSendException {
            final PushNotificationAddress xAddress;
            final PushNotificationMessage xMessage;

            try {
                xAddress = PushNotificationAddressDocument.Factory.parse(md.getMessage().getAddressTo()).getPushNotificationAddress();
            } catch (XmlException ex) {
                throw new DPCSendException("Invalid push notification address [" + md.getMessage().getAddressTo() + "]: " + ex.getMessage(), ex);
            }
            try {
                xMessage = PushNotificationMessageDocument.Factory.parse(md.getMessage().getBody()).getPushNotificationMessage();
            } catch (XmlException ex) {
                throw new DPCSendException("Invalid push notification message [" + md.getMessage().getBody() + "]: " + ex.getMessage(), ex);
            }

            final JSONObject jsonObj = new JSONObject();

            if (xAddress.isSetIsGroup()) {
                if (xAddress.getReceiverId() == null) {
                    throw new DPCSendException("ReceiverId must be defined");
                } else {
                    jsonObj.put(FIELD_TO, xAddress.getReceiverId());
                }
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

        @Override
        public boolean sendMessage(Long messageId, JSONObject msg) throws DPCSendException {
            final String jsonString = msg.toJSONString();
            HttpURLConnection conn = null;

            getTraceInterface().put(EEventSeverity.DEBUG, "Sending: \n" + jsonString + "\n\nAuthToken: " + authToken, null, null, getEventSource(), true);

            try {
                conn = getConnection(sendAddress);
                final byte[] data = jsonString.getBytes("UTF-8");

                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setFixedLengthStreamingMode(data.length);
                conn.setRequestProperty(EHttpParameter.HTTP_CONTENT_TYPE_ATTR.getValue(), JSON_CONTENT_TYPE);
                conn.setRequestProperty(AUTH_HEADER, "key=" + authToken);

                try (final OutputStream os = conn.getOutputStream()) {
                    os.write(data);
                    os.flush();
                }

                if (conn.getResponseCode() != 200) {
                    final String response = IOUtils.toString(conn.getErrorStream(), "UTF-8");

                    getTraceInterface().put(EEventSeverity.ERROR, response == null ? "<empty error message>" : response, null, null, getEventSource(), true);
                    throw new DPCSendException("Unable to send message: " + conn.getResponseCode() + " " + conn.getResponseMessage());
                } else {
                    final String response = IOUtils.toString(conn.getInputStream(), "UTF-8");

                    getTraceInterface().put(EEventSeverity.DEBUG, "Response: \n:" + (response == null ? "<empty response>" : response), null, null, getEventSource(), true);
                    sendCallback(messageId, response);
                    return true;
                }
            } catch (IOException ex) {
                getTraceInterface().put(EEventSeverity.DEBUG, ExceptionTextFormatter.exceptionStackToString(ex), null, null, getEventSource(), false);
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            return false;
        }

        @Override
        public void setStatistics(MessageStatistics stat) throws DPCSendException {
            this.stat = stat;
        }

        @Override
        public MessageStatistics getStatistics() throws DPCSendException {
            return stat;
        }

        @Override
        public JSONObject receiveMessage() throws DPCRecvException {
            throw new IllegalStateException("This method can't be called for adapter to receive");
        }

        @Override
        public MessageDocument unprepareMessage(JSONObject msg) throws DPCRecvException {
            throw new IllegalStateException("This method can't be called for adapter to receive");
        }

        @Override
        public void close() throws IOException {
        }
    }

    protected class ReadGcmCommunicationAdapter implements CommunicationAdapter<JSONObject> {

        private MessageStatistics stat;

        public ReadGcmCommunicationAdapter() {
        }

        @Override
        public JSONObject prepareMessage(Long messageId, MessageDocument md) throws DPCSendException {
            throw new IllegalStateException("This method can't be called for adapter to receive");
        }

        @Override
        public boolean sendMessage(Long messageId, JSONObject msg) throws DPCSendException {
            throw new IllegalStateException("This method can't be called for adapter to receive");
        }

        @Override
        public void setStatistics(MessageStatistics stat) throws DPCSendException {
            this.stat = stat;
        }

        @Override
        public MessageStatistics getStatistics() throws DPCSendException {
            return stat;
        }

        @Override
        public JSONObject receiveMessage() throws DPCRecvException {
            return null;
        }

        @Override
        public MessageDocument unprepareMessage(JSONObject msg) throws DPCRecvException {
            return null;
        }

        @Override
        public void close() throws IOException {
        }
    }
}
