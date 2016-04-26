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

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsDelegate;
import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.ApnsServiceBuilder;
import com.notnoop.apns.DeliveryError;
import com.notnoop.apns.EnhancedApnsNotification;
import com.notnoop.apns.PayloadBuilder;
import com.notnoop.exceptions.NetworkIOException;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.common.ssl.CertificateUtils;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.Maps;
import org.radixware.kernel.common.utils.ValueFormatter;
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
 * Apple Push Notification service connector
 *
 * @author dsafonov
 */
public class ApnsUnit extends PCUnit {

    private static final String DEFAULT_SOUND_NAME = "default";
    private static final int GET_INACTIVE_DEVICES_PERIOD_MILLIS = 60000;
    private ApnsService apnsService;
    private ApnsDelegate delegate;
    private final ArrayBlockingQueue<MessageStatus> statusQueue = new ArrayBlockingQueue<>(10000);
    private final List<MessageInfo> queuedMessages = new LinkedList<>();
    private long lastCheckInactiveDevicesMillis = 0;

    public ApnsUnit(Instance instance, Long id, String title) {
        super(instance, id, title);
    }

    @Override
    public String optionsToString() {
        final OptionsGroup og = new OptionsGroup();
        og.add(PCMessages.SEND_PERIOD, options.sendPeriod)
                .add(PCMessages.APNS_PUSH_ADDRESS, options.sendAddress)
                .add(PCMessages.APNS_FEEDBACK_ADDRESS, options.recvAddress)
                .add(PCMessages.APNS_KEY_ALIAS, options.apnsKeyAlias)
                .add(PCMessages.APP_NAME_MASK, options.addressMask)
                .add(PCMessages.APNS_MAX_PARALLEL_SEND_COUNT, options.apnsMaxParallelSendCount)
                .add(PCMessages.APNS_TIME_SUCCESSFUL_MILLIS, options.apnsSendSuccessfulAfterMillis);
        return og.toString();
    }

    @Override
    protected boolean startImpl() throws Exception {
        if (!super.startImpl()) {
            return false;
        }

        delegate = new ApnsDelegateImpl(this);

        final InetSocketAddress pushAddress = ValueFormatter.parseInetSocketAddress(options.sendAddress);
        final InetSocketAddress feedbackAddress = ValueFormatter.parseInetSocketAddress(options.recvAddress);

        apnsService = new ApnsServiceBuilder()
                .withGatewayDestination(pushAddress.getHostString(), pushAddress.getPort())
                .withFeedbackDestination(feedbackAddress.getHostString(), feedbackAddress.getPort())
                .withDelegate(delegate)
                .withSSLContext(CertificateUtils.prepareServerSslContext(Collections.singletonList(options.apnsKeyAlias), null))
                .build();

        lastCheckInactiveDevicesMillis = 0;

        return true;
    }

    @Override
    protected void sendMessages() throws DPCSendException {
        try {
            apnsService.testConnection();
        } catch (NetworkIOException ex) {
            throw new DPCSendException("APN service is unreachable", ex);
        }
        super.sendMessages();
    }

    private void logInactiveDevices() {
        if (System.currentTimeMillis() - lastCheckInactiveDevicesMillis > GET_INACTIVE_DEVICES_PERIOD_MILLIS) {
            lastCheckInactiveDevicesMillis = System.currentTimeMillis();
            getTrace().put(EEventSeverity.DEBUG, "Getting list of inactive devices", null, null, getEventSource(), false);
            try {
                final Map<String, Date> devices = apnsService.getInactiveDevices();
                if (devices != null) {
                    for (Map.Entry<String, Date> entry : devices.entrySet()) {
                        getTrace().put(EEventSeverity.WARNING, "Device token '" + entry.getKey() + "' became invalid on " + entry.getValue(), null, null, getEventSource(), false);
                    }
                }
            } catch (Exception ex) {
                getTrace().put(EEventSeverity.WARNING, "Can't get inactive devices: " + ExceptionTextFormatter.exceptionStackToString(ex), null, null, getEventSource(), false);
            }

        }
    }

    @Override
    protected void send(MessageDocument m, Long id) throws DPCSendException {
        try {
            final MessageInfo info = new MessageInfo(id.intValue(), m);
            final PushNotificationAddress xAddress = PushNotificationAddressDocument.Factory.parse(m.getMessage().getAddressTo()).getPushNotificationAddress();
            final PushNotificationMessage xMessage = PushNotificationMessageDocument.Factory.parse(m.getMessage().getBody()).getPushNotificationMessage();

            PayloadBuilder builder = APNS.newPayload();
            if (xMessage.isSetTitle()) {
                builder.alertTitle(xMessage.getTitle());
            }
            if (xMessage.isSetBody()) {
                builder.alertBody(xMessage.getBody());
            }
            if (xMessage.isSetIcon()) {
                builder.launchImage(xMessage.getIcon());
            }
            if (xMessage.isSetSound()) {
                builder.sound(DEFAULT_SOUND_NAME);
            }
            if (xMessage.isSetData()) {
                builder.customFields(Maps.fromXml(xMessage.getData()));
            }

            final EnhancedApnsNotification notification = new EnhancedApnsNotification(id.intValue(), (int) (xMessage.isSetTimeToLiveSec() ? (System.currentTimeMillis() / 1000 + xMessage.getTimeToLiveSec()) : Integer.MAX_VALUE), xAddress.getReceiverId(), builder.build());

            getTrace().debug("Sending: \n" + notification, getEventSource(), true);

            apnsService.push(notification);

            queuedMessages.add(info);

        } catch (Exception ex) {
            if (ex instanceof DPCSendException) {
                throw (DPCSendException) ex;
            }
            throw new DPCSendException("Unable to send message to APNS", ex);
        }
    }

    @Override
    public boolean canSend() {
        return queuedMessages.size() < options.apnsMaxParallelSendCount;
    }

    @Override
    protected void maintenanceImpl() throws InterruptedException {
        processSuccessful();
        super.maintenanceImpl();
        processResponses(true);
        logInactiveDevices();
    }

    private void processSuccessful() {
        final Iterator<MessageInfo> it = queuedMessages.iterator();
        while (it.hasNext()) {
            final MessageInfo messageInfo = it.next();
            if (messageInfo.sentTimeMillis > 0 && System.currentTimeMillis() - messageInfo.sentTimeMillis > options.apnsSendSuccessfulAfterMillis) {
                getTrace().debug("No errors regarding message #" + messageInfo.id + " received in " + options.apnsSendSuccessfulAfterMillis + " ms, considering as sent", getEventSource(), false);
                it.remove();
                sendCallback((long) messageInfo.id, null);
            }
        }
    }

    private void processResponses(final boolean canTriggerSend) {
        final List<MessageStatus> responseInfos = new ArrayList<>();
        statusQueue.drainTo(responseInfos);
        boolean wasFull = queuedMessages.size() == options.apnsMaxParallelSendCount;
        for (MessageStatus responseInfo : responseInfos) {
            final boolean responseToTest = responseInfo.messageId == 0;

            getTrace().debug("Message status updated (" + (responseInfo.sent ? "sent" : "not sent") + "): " + responseInfo.toString(), getEventSource(), false);

            if (responseToTest) {
                continue;
            }

            final Iterator<MessageInfo> it = queuedMessages.iterator();
            MessageInfo messageInfo = null;
            while (it.hasNext()) {
                messageInfo = it.next();
                if (messageInfo.id == responseInfo.messageId) {
                    if (!responseInfo.sent) {//we are not sure if message was successfully sent, wait for timeout
                        it.remove();
                    }
                    break;
                }
                if (!it.hasNext()) {
                    messageInfo = null;
                }
            }
            if (messageInfo == null) {
                getTrace().put(EEventSeverity.WARNING, "Message #" + responseInfo.messageId + " not found in out queue upon receiving status update", null, null, getEventSource(), false);
            }

            if (responseInfo.sent) {
                if (messageInfo != null) {
                    messageInfo.sentTimeMillis = System.currentTimeMillis();
                }
            } else if (responseInfo.exception != null) {
                try {
                    processSendError(responseInfo.messageId, messageInfo != null ? messageInfo.xMessage : null, new DPCSendException("Error on sending message to APNs", responseInfo.exception));
                } catch (SQLException ex) {
                    Logger.getLogger(ApnsUnit.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (responseInfo.connectionClosed) {
                getTrace().put(EEventSeverity.ERROR, "APNs closed connection during message #" + responseInfo.messageId + "processing: " + responseInfo.deliveryError, null, null, getEventSource(), false);
                if (messageInfo != null) {
                    try {
                        processSendError(responseInfo.messageId, messageInfo.xMessage, new DPCSendException("Error status received from APNs: " + responseInfo.deliveryError));
                    } catch (SQLException ex) {
                        Logger.getLogger(ApnsUnit.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        if (wasFull && canTriggerSend && queuedMessages.size() < options.apnsMaxParallelSendCount) {
            triggerSend();
        }
    }

    @Override
    protected void recvMessages() throws DPCRecvException {

    }

    @Override
    public String getUnitTypeTitle() {
        return PCMessages.APNS_UNIT_TYPE_TITLE;
    }

    @Override
    public Long getUnitType() {
        return EUnitType.DPC_APNS.getValue();
    }

    private static class ApnsDelegateImpl implements ApnsDelegate {

        private final ApnsUnit unit;

        public ApnsDelegateImpl(ApnsUnit unit) {
            this.unit = unit;
        }

        @Override
        public void messageSent(ApnsNotification an, boolean bln) {
            unit.statusQueue.add(MessageStatus.sent(an, bln));
            unit.getDispatcher().wakeup();
        }

        @Override
        public void messageSendFailed(ApnsNotification an, Throwable thrwbl) {
            unit.statusQueue.add(MessageStatus.failed(an, thrwbl));
            unit.getDispatcher().wakeup();
        }

        @Override
        public void connectionClosed(DeliveryError de, int i) {
            unit.statusQueue.add(MessageStatus.connectionClosed(i, de));
            unit.getDispatcher().wakeup();
        }

        @Override
        public void cacheLengthExceeded(int i) {
            unit.getTrace().put(EEventSeverity.WARNING, "Cache size for sent messages reached " + i, null, null, unit.getEventSource(), false);
        }

        @Override
        public void notificationsResent(int i) {
            unit.getTrace().put(EEventSeverity.DEBUG, i + " notification have been queued for resending", null, null, unit.getEventSource(), false);
        }
    }

    private static class MessageInfo {

        public final int id;
        public final MessageDocument xMessage;
        public long sentTimeMillis = -1;

        public MessageInfo(int id, MessageDocument xMessage) {
            this.id = id;
            this.xMessage = xMessage;
        }

    }

    private static class MessageStatus {

        public final ApnsNotification notification;
        public final boolean sent;
        public final boolean afterError;
        public final Throwable exception;
        public final boolean connectionClosed;
        public final DeliveryError deliveryError;
        public final int messageId;

        public MessageStatus(ApnsNotification notification, boolean sent, boolean afterError, Throwable exception, boolean connectionClosed, DeliveryError deliveryError, int messageId) {
            this.notification = notification;
            this.sent = sent;
            this.afterError = afterError;
            this.exception = exception;
            this.connectionClosed = connectionClosed;
            this.deliveryError = deliveryError;
            this.messageId = messageId;
        }

        public static MessageStatus sent(final ApnsNotification notification, final boolean afterError) {
            return new MessageStatus(notification, true, afterError, null, false, DeliveryError.NO_ERROR, notification.getIdentifier());
        }

        public static MessageStatus failed(final ApnsNotification notification, final Throwable exception) {
            return new MessageStatus(notification, false, false, exception, false, DeliveryError.NO_ERROR, notification.getIdentifier());
        }

        public static MessageStatus connectionClosed(final int messageId, final DeliveryError error) {
            return new MessageStatus(null, false, false, null, true, error, messageId);
        }

        @Override
        public String toString() {
            return "MessageStatus{" + "notification=" + notification + ", sent=" + sent + ", afterError=" + afterError + ", exception=" + exception + ", connectionClosed=" + connectionClosed + ", deliveryError=" + deliveryError + ", messageId=" + messageId + '}';
        }

    }

}
