/*
 * Copyright (c) 2008-2018, Compass Plus Limited. All rights reserved.
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.server.exceptions.DPCRecvException;
import org.radixware.kernel.server.exceptions.DPCSendException;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.units.persocomm.interfaces.ICommunicationAdapter;
import org.radixware.kernel.server.utils.OptionsGroup;
import org.radixware.schemas.personalcommunications.MessageDocument;
import org.radixware.schemas.personalcommunications.PushNotificationAddress;
import org.radixware.schemas.personalcommunications.PushNotificationAddressDocument;
import org.radixware.schemas.personalcommunications.PushNotificationMessage;
import org.radixware.schemas.personalcommunications.PushNotificationMessageDocument;

public class WnsUnit extends PersoCommUnit {

    public WnsUnit(final Instance instModel, final Long id, final String title) {
        super(instModel, id, title);
    }

    @Override
    public ICommunicationAdapter getCommunicationAdapter(CommunicationMode mode) throws IOException {
        switch (mode) {
            case TRANSMIT:
                return new WnsCommunicationAdapter(options.wnsClientId, options.wnsClientSecret);
        }
        throw new UnsupportedOperationException("Communication mode [" + mode + "] is not supported in the [" + this.getClass().getSimpleName() + "] adapter!");
    }

    @Override
    public OptionsGroup optionsGroup(Options options) {
        return new OptionsGroup()
                .add(PCMessages.SEND_PERIOD, options.sendPeriod)
                .add(PCMessages.WNS_CLIENT_ID, options.wnsClientId);
    }

    @Override
    public boolean supportsTransmitting() {
        return true;
    }

    @Override
    public boolean supportsReceiving() {
        return false;
    }

    @Override
    protected void checkOptions(Options options) throws Exception {
        if (StringUtils.isBlank(options.wnsClientId)) {
            throw new IllegalArgumentException("WNS Client Id not defined");
        } else if (StringUtils.isBlank(options.wnsClientSecret)) {
            throw new IllegalArgumentException("WNS Client Secret not defined");
        }
    }

    @Override
    public String getUnitTypeTitle() {
        return PCMessages.WNS_UNIT_TYPE_TITLE;
    }

    @Override
    public Long getUnitType() {
        return EUnitType.DPC_WNS.getValue();
    }

    static final class KeyValues {

        static final int MAX = 5;
        int cnt = 0;
        final String[] keys = new String[MAX];
        final String[] values = new String[MAX];

        KeyValues() {
        }

        KeyValues(String key, String value) {
            add(key, value);
        }

        KeyValues add(String key, Object value) {
            if (value != null) {
                keys[cnt] = key;
                values[cnt++] = value.toString();
            }
            return this;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < cnt; ++i) {
                sb.append(i == 0 ? "" : "&").append(keys[i]).append('=').append(values[i]);
            }
            return sb.toString();
        }

        void setRequestProperties(URLConnection connection) {
            for (int i = 0; i < cnt; ++i) {
                connection.setRequestProperty(keys[i], values[i]);
            }
        }
    }

    static class SimpleHttpException extends IOException {

        final SimpleHttpClient httpClient;
        final int status;

        public SimpleHttpException(String message, IOException cause, SimpleHttpClient httpClient, int status) {
            super(message, cause);
            this.httpClient = httpClient;
            this.status = status;
        }

        public SimpleHttpException(IOException cause, SimpleHttpClient httpClient, int status) {
            super(cause);
            this.httpClient = httpClient;
            this.status = status;
        }
    }

    class SimpleHttpClient {

        HttpsURLConnection conn = null;

        SimpleHttpClient(String address, Proxy proxy, KeyValues queryParams) throws IOException {
            final String fullUrl = queryParams == null ? address : address + "?" + queryParams;
            //getTrace().put(EEventSeverity.DEBUG, "SimpleHttpClient: fullUrl = " + fullUrl, EEventSource.PERSOCOMM_UNIT);
            final URL url = new URL(fullUrl);
            conn = (HttpsURLConnection) url.openConnection(proxy);

        }

        private SimpleHttpClient init(boolean doOutput, String method, KeyValues requestProps) throws IOException {
            //getTrace().put(EEventSeverity.DEBUG, "SimpleHttpClient.init: requestProps = " + requestProps, EEventSource.PERSOCOMM_UNIT);
            conn.setDoOutput(doOutput);
            conn.setRequestMethod(method);
            if (requestProps != null) {
                requestProps.setRequestProperties(conn);
            }
            return this;
        }

        SimpleHttpClient initPost(KeyValues requestProps) throws IOException {
            return init(true, "POST", requestProps);
        }

        private void processIoException(IOException ex) throws IOException {
            final int rc = conn.getResponseCode();
            try (final InputStream es = conn.getErrorStream()) {
                if (es != null) {
                    final String message = IOUtils.toString(es, "UTF-8");
                    throw new SimpleHttpException(message, ex, this, rc);
                }
            }
            throw new SimpleHttpException(ex, this, rc);
        }

        String doGet() throws IOException {
            String result = null;
            try (final InputStream is = conn.getInputStream()) {
                result = IOUtils.toString(is, "UTF-8");
                //getTrace().put(EEventSeverity.DEBUG, "SimpleHttpClient.doGet: result = " + result, EEventSource.PERSOCOMM_UNIT);
            } catch (IOException ex) {
                processIoException(ex);
            }
            final int rc = conn.getResponseCode();

            if (rc != HttpURLConnection.HTTP_OK) {
                throw new SimpleHttpException(result, null, this, rc);
            }
            return result;
        }

        String doPost(String requestStr) throws IOException {
            getTrace().put(EEventSeverity.DEBUG, "SimpleHttpClient.doPost: requestStr = \n" + requestStr, null, null, getEventSource(), true);
            try (final OutputStream os = conn.getOutputStream()) {
                IOUtils.write(requestStr, os, "UTF-8");
            } catch (IOException ex) {
                processIoException(ex);
            }

            return doGet();
        }
        
        String post(KeyValues requestProps, String requestStr) throws IOException {
            initPost(requestProps);
            return doPost(requestStr);
        }

        String getHeaderField(String name) {
            return conn.getHeaderField(name);
        }
    }

    static class AccessToken {

        public String token_type;
        public String access_token;

        public AccessToken() {
        }

        @Override
        public String toString() {
            return token_type + " " + access_token;
        }

        static AccessToken fromString(String s) throws IOException {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            final AccessToken accessToken = mapper.readValue(s, AccessToken.class);
            return accessToken;
        }
    }

    protected final class WnsCommunicationAdapter implements ICommunicationAdapter {

        final String clientId;
        final String clientSecret;
        private AccessToken accessToken;

        private final String[] DEBUG_HEADER_FIELDS = new String[]{
            "X-WNS-Debug-Trace",
            "X-WNS-DeviceConnectionStatus",
            "X-WNS-Error-Description",
            "X-WNS-Msg-ID",
            "X-WNS-Status"
        };

        private static final String MIME_TYPE_URL_ENCODED = "application/x-www-form-urlencoded";
        private static final String MIME_TYPE_OCTET_STREAM = "application/octet-stream";

        private static final String FIELD_GRANT_TYPE = "grant_type";
        private static final String FIELD_CLIENT_ID = "client_id";
        private static final String FIELD_SCOPE = "scope";
        private static final String FIELD_CLIENT_SECRET = "client_secret";

        private static final String ACCESS_TOKEN_URL = "https://login.live.com/accesstoken.srf";
        private final static String GRANT_TYPE = "client_credentials";
        private final static String SCOPE = "notify.windows.com";

        void doAccessTokenRequest() throws IOException {
            final Proxy proxy = getInstance().getHttpsProxyObject();
            final KeyValues headers = new KeyValues(HttpHeaders.CONTENT_TYPE, MIME_TYPE_URL_ENCODED);
            final SimpleHttpClient client = new SimpleHttpClient(ACCESS_TOKEN_URL, proxy, null);
            final KeyValues request = new KeyValues(FIELD_GRANT_TYPE, GRANT_TYPE)
                    .add(FIELD_CLIENT_ID, clientId)
                    .add(FIELD_CLIENT_SECRET, clientSecret)
                    .add(FIELD_SCOPE, SCOPE);
            String response = client.post(headers, request.toString());
            accessToken = AccessToken.fromString(response);
        }

        private static final String HEADER_XWNS_TTL = "X-WNS-TTL";
        private static final String HEADER_AUTHORIZATION = "Authorization";
        private static final String HEADER_XWNS_TYPE = "X-WNS-Type";
        private static final String XWNS_TYPE_RAW = "wns/raw";

        void doPushRequest(String deviceToken, String pushData, Long ttl) throws IOException {
            final Proxy proxy = getInstance().getHttpsProxyObject();
            final KeyValues headers = new KeyValues(HttpHeaders.CONTENT_TYPE, MIME_TYPE_OCTET_STREAM)
                    .add(HEADER_XWNS_TYPE, XWNS_TYPE_RAW)
                    .add(HEADER_AUTHORIZATION, accessToken)
                    .add(HEADER_XWNS_TTL, ttl);
            final SimpleHttpClient client = new SimpleHttpClient(deviceToken, proxy, null);
            client.post(headers, pushData);
        }

        public WnsCommunicationAdapter(String clientId, String clientSecret) throws IOException {
            this.clientId = clientId;
            this.clientSecret = clientSecret;
        }
        
        private String preparePushData(Long messageId, PushNotificationMessage xMessage) throws DPCSendException {
            if (!xMessage.isSetBody() && !xMessage.isSetData()) {
                throw new DPCSendException("WNS push notification content (neither Body nor Data) not set");
            }
            
            final Map<String, Object> pushFields = new HashMap<>();

            final String[] stdFieldTitles = new String[]{"rdx.messageId", "rdx.badge", "rdx.body", "rdx.clickAction", "rdx.color", "rdx.icon", "rdx.sound", "rdx.tag", "rdx.title"};
            final boolean[] isStdFieldSet = new boolean[]{
                true, xMessage.isSetBadge(), xMessage.isSetBody(), xMessage.isSetClickAction(), xMessage.isSetColor(),
                xMessage.isSetIcon(), xMessage.isSetSound(), xMessage.isSetTag(), xMessage.isSetTitle()
            };
            final Object[] stdFieldValues = new Object[]{
                messageId, xMessage.getBadge(), xMessage.getBody(), xMessage.getClickAction(), xMessage.getColor(),
                xMessage.getIcon(), xMessage.getSound(), xMessage.getTag(), xMessage.getTitle()
            };
            for (int i = 0; i < stdFieldTitles.length; ++i) {
                if (isStdFieldSet[i]) {
                    pushFields.put(stdFieldTitles[i], stdFieldValues[i]);
                }
            }

            if (xMessage.isSetData()) {
                final List<org.radixware.schemas.types.MapStrStr.Entry> entries = xMessage.getData().getEntryList();
                for (org.radixware.schemas.types.MapStrStr.Entry entry : entries) {
                    if (StringUtils.isBlank(entry.getKey())) {
                        throw new DPCSendException("Message.Data.Entry[x].Key should not be blank");
                    }
                    pushFields.put(entry.getKey(), entry.getValue());
                }
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            try {
                final String pushData = mapper.writeValueAsString(pushFields);
                return pushData;
            } catch (JsonProcessingException ex) {
                throw new DPCSendException("Unable to send message: JSON serialization failed");
            }

        }

        @Override
        public MessageSendResult sendMessage(MessageWithMeta messageWithMeta) throws DPCSendException {
            final Long messageId = messageWithMeta.id;
            final MessageDocument md = messageWithMeta.xDoc;
            final MessageSendResult result = new MessageSendResult(messageId)
                    .setMessageWithMeta(messageWithMeta);

            final PushNotificationAddress xAddress;
            final PushNotificationMessage xMessage;

            try {
                xAddress = PushNotificationAddressDocument.Factory.parse(md.getMessage().getAddressTo()).getPushNotificationAddress();
            } catch (XmlException ex) {
                throw new DPCSendException("Invalid push notification address [" + md.getMessage().getAddressTo() + "]: " + ex.getMessage(), ex);
            }
            if (StringUtils.isBlank(xAddress.getReceiverId())) {
                throw new DPCSendException("WNS Channel URI (Receiver Id) not set");
            }

            try {
                xMessage = PushNotificationMessageDocument.Factory.parse(md.getMessage().getBody()).getPushNotificationMessage();
            } catch (XmlException ex) {
                throw new DPCSendException("Invalid push notification message [" + md.getMessage().getBody() + "]: " + ex.getMessage(), ex);
            }
            
            final String deviceUri = xAddress.getReceiverId();
            final String pushData = preparePushData(messageId, xMessage);
            final Long ttl = xMessage.getTimeToLiveSec();

            boolean accessTokenExpired = false;
            while (true) { // second try in case of access token expiration
                try {
                    if (accessTokenExpired || accessToken == null) {
                        doAccessTokenRequest();
                    }
                    doPushRequest(deviceUri, pushData, ttl);
                    return sendCallback(result);
                } catch (SimpleHttpException ex) {
                    final StringBuilder debugHeaderFields = new StringBuilder();
                    for (String field : DEBUG_HEADER_FIELDS) {
                        final String fieldValue = ex.httpClient.getHeaderField(field);
                        if (fieldValue != null) {
                            debugHeaderFields.append("\n").append(field).append(": ").append(fieldValue);
                        }
                    }
                    if (StringUtils.isNotBlank(debugHeaderFields.toString())) {
                        getTrace().put(EEventSeverity.DEBUG, "X-WNS Debug header fields:" + debugHeaderFields.toString(), EEventSource.PERSOCOMM_UNIT);
                    }

                    switch (ex.status) {
                        // https://msdn.microsoft.com/library/windows/apps/hh465435.aspx#WNSResponseCodes
                        case 400: // Bad Request
                            if (accessTokenExpired || accessToken == null) {
                                throw new DPCRecoverableSendException("Unable to send message: 400 Bad Request - The authentication failed").throttle();
                            }
                            throw new DPCSendException("Unable to send message: 400 Bad Request - One or more headers were specified incorrectly or conflict with another header");
                        case 401: // Unauthorized
                            if (accessTokenExpired) {
                                throw new DPCRecoverableSendException("Unable to send message: 401 Unauthorized - The cloud service did not present a valid authentication ticket. The OAuth ticket may be invalid").throttle();
                            } else {
                                accessTokenExpired = true;
                                break;
                            }
                        case 403: // Forbidden
                            throw new DPCSendException("Unable to send message: 403 Forbidden - The cloud service is not authorized to send a notification to this URI even though they are authenticated.").throttle();
                        case 404: // Not Found
                            throw new DPCSendException("Unable to send message: 404 Not Found - The channel URI is not valid or is not recognized by WNS.");
                        case 406: // Not Acceptable
                            throw new DPCRecoverableSendException("Unable to send message: 406 Not Acceptable - The cloud service exceeded its throttle limit").throttle();
                        case 410: // Gone
                            throw new DPCSendException("Unable to send message: 410 Gone - The channel expired");
                        case 413: // Request Entity Too Large
                            throw new DPCSendException("Unable to send message: 413 Request Entity Too Large - The notification payload exceeds the 5000 byte size limit");
                        case 500: // Internal Server Error
                            throw new DPCRecoverableSendException("Unable to send message: 500 Internal Server Error - An internal failure caused notification delivery to fail").throttle();
                        case 503: // Service Unavailable
                            throw new DPCRecoverableSendException("Unable to send message: 503 Service Unavailable - The server is currently unavailable").throttle();
                        default:
                            throw new DPCRecoverableSendException("Unable to send message: HTTP status code: " + ex.status).throttle();
                    }
                } catch (IOException ex) { // Transmission-related
                    throw new DPCRecoverableSendException("Unable to send message #" + messageId + ": I/O error occured", ex);
                }
            }

        }

        @Override
        public MessageDocument receiveMessage() throws DPCRecvException, SQLException {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public boolean isPersistent() {
            return false;
        }

        @Override
        public void close() throws IOException {
        }

    }

}
