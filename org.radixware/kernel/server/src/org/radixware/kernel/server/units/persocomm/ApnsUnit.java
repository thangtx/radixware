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

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsDelegate;
import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.ApnsServiceBuilder;
import com.notnoop.apns.DeliveryError;
import com.notnoop.apns.EnhancedApnsNotification;
import com.notnoop.apns.PayloadBuilder;
import com.notnoop.exceptions.ApnsDeliveryErrorException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.common.exceptions.CertificateUtilsException;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.ssl.CertificateUtils;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.ExpiringSet;
import org.radixware.kernel.common.utils.Maps;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.ValueFormatter;
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

public class ApnsUnit extends PersoCommUnit implements ApnsDelegate {

    private static final int GET_INACTIVE_DEVICES_PERIOD_MILLIS = 60000;
    private static final long TEN_MINUTES = 10 * 60 * 1000;

    private final ArrayBlockingQueue<MessageStatus> statusQueue = new ArrayBlockingQueue<>(10000);
    private final List<MessageInfo> queuedMessages = new LinkedList<>();
    private final ExpiringSet<Integer> processedErrorMessages = new ExpiringSet<>(10000, TEN_MINUTES);

    private ApnsService apnsService;
    private long lastCheckInactiveDevicesMillis = 0;

    public ApnsUnit(final Instance instModel, final Long id, final String title) {
        super(instModel, id, title);
    }

    @Override
    public ICommunicationAdapter getCommunicationAdapter(CommunicationMode mode) throws IOException {
        switch (mode) {
            case TRANSMIT:
                return this.new WriteApnsCommunicationAdapter();
            default:
                throw new UnsupportedOperationException("Communication mode [" + mode + "] is not supported in the [" + this.getClass().getSimpleName() + "] adapter!");
        }
    }

    @Override
    public OptionsGroup optionsGroup(final Options options) {
        return new OptionsGroup()
                .add(PCMessages.SEND_PERIOD, options.sendPeriod)
                .add(PCMessages.APNS_PUSH_ADDRESS, options.sendAddress)
                .add(PCMessages.APNS_FEEDBACK_ADDRESS, options.recvAddress)
                .add(PCMessages.APNS_KEY_ALIAS, options.apnsKeyAlias)
                .add(PCMessages.APP_NAME_MASK, options.addressMask)
                .add(PCMessages.APNS_MAX_PARALLEL_SEND_COUNT, options.apnsMaxParallelSendCount)
                .add(PCMessages.APNS_TIME_SUCCESSFUL_MILLIS, options.apnsSendSuccessfulAfterMillis);
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
    public String getUnitTypeTitle() {
        return PCMessages.APNS_UNIT_TYPE_TITLE;
    }

    @Override
    public Long getUnitType() {
        return EUnitType.DPC_APNS.getValue();
    }

    @Override
    public void messageSent(ApnsNotification an, boolean bln) {
        statusQueue.add(MessageStatus.sent(an, bln));
        wakeUp();
    }

    @Override
    public void messageSendFailed(ApnsNotification an, Throwable thrwbl) {
        statusQueue.add(MessageStatus.failed(an, thrwbl));
        wakeUp();
    }

    @Override
    public void connectionClosed(DeliveryError de, int i) {
        statusQueue.add(MessageStatus.connectionClosed(i, de));
        wakeUp();
    }

    @Override
    public void cacheLengthExceeded(int i) {
        getTrace().put(EEventSeverity.WARNING, "Cache size for sent messages reached " + i, null, null, getEventSource(), false);
    }

    @Override
    public void notificationsResent(int i) {
        getTrace().put(EEventSeverity.DEBUG, i + " notification have been queued for resending", null, null, getEventSource(), false);
    }

    @Override
    protected void checkOptions(Options options) throws Exception {
    }

    @Override
    protected boolean prepareImpl() {
        if (super.prepareImpl()) {
            try {
                final InetSocketAddress pushAddress = ValueFormatter.parseInetSocketAddress(options.sendAddress);
                final InetSocketAddress feedbackAddress = ValueFormatter.parseInetSocketAddress(options.recvAddress);

                apnsService = new ApnsServiceBuilder()
                        .withGatewayDestination(pushAddress.getHostString(), pushAddress.getPort())
                        .withFeedbackDestination(feedbackAddress.getHostString(), feedbackAddress.getPort())
                        .withDelegate(this)
                        .withSSLContext(CertificateUtils.prepareServerSslContext(Collections.singletonList(options.apnsKeyAlias), null))
                        .build();

                lastCheckInactiveDevicesMillis = 0;
                return true;
            } catch (CertificateUtilsException ex) {
                throw new RadixError(ex.getMessage(), ex);
            }
        } else {
            return false;
        }
    }

    @Override
    protected void unprepareImpl() {
        apnsService.stop();
        super.unprepareImpl();
    }

    @Override
    protected void maintenanceImpl() throws InterruptedException {
        processSuccessful();
        super.maintenanceImpl();
        processResponses(true);
        logInactiveDevices();
    }

    protected void wakeUp() {
        getDispatcher().wakeup();
    }

    private void processSuccessful() {
        final Iterator<MessageInfo> it = queuedMessages.iterator();
        while (it.hasNext()) {
            final MessageInfo messageInfo = it.next();
            if (messageInfo.sentTimeNanos != Long.MIN_VALUE && System.nanoTime() - messageInfo.sentTimeNanos > options.apnsSendSuccessfulAfterMillis * 1000000L) {
                getTrace().debug("No errors regarding message #" + messageInfo.id + " received in " + options.apnsSendSuccessfulAfterMillis + " ms, considering as sent", getEventSource(), false);
                it.remove();
                final MessageSendResult result = new MessageSendResult((messageInfo.id), messageInfo.sentTimeNanos);
                sendCallback(result);
            }
        }
    }
    
    private boolean isRecoverableError(DeliveryError err) {
        // https://developer.apple.com/library/content/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/BinaryProviderAPI.html#//apple_ref/doc/uid/TP40008194-CH13-SW11
        int errCode = err == null ? -1 : err.code();
        boolean recoverable = errCode == 0 // No errors encountered
                || errCode == 1 // Processing error
                || errCode == 10 // Shutdown - but there is no such constant in DeliveryError
                || errCode == 254 // there no such code, but there is enum item DeliveryError.UNKNOWN
                || errCode == 255; // None (unknown)
        
        return recoverable;
    }

    private void processResponses(final boolean canTriggerSend) {
        final List<MessageStatus> responseInfos = new ArrayList<>();
        final boolean wasFull = queuedMessages.size() == options.apnsMaxParallelSendCount;

        statusQueue.drainTo(responseInfos);
        for (MessageStatus responseInfo : responseInfos) {
            if (processedErrorMessages.contains(responseInfo.messageId)) {
                getTrace().debug("Duplicate message status update: " + responseInfo.toString(), getEventSource(), false);
                processedErrorMessages.remove(responseInfo.messageId);
                continue; // response was recived both by messageSendFailed() and connectionClosed() callbacks
            }
            
            final boolean responseToTest = responseInfo.messageId == 0;

            getTrace().debug("Message status updated (" + (responseInfo.sent ? "sent" : "not sent") + "): " + responseInfo.toString(), getEventSource(), false);

            if (!responseToTest) {
                MessageInfo messageInfo = null;
                final Iterator<MessageInfo> it = queuedMessages.iterator();

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

                final ApnsDeliveryErrorException apnsEx = responseInfo.exception instanceof ApnsDeliveryErrorException
                        ? (ApnsDeliveryErrorException)responseInfo.exception : null;
                final DeliveryError apnsErr = Utils.nvlOf(apnsEx == null ? null : apnsEx.getDeliveryError(), responseInfo.deliveryError);
                
                if (responseInfo.sent) {
                    if (messageInfo != null) {
                        messageInfo.sentTimeNanos = System.nanoTime();
                    }
                } else if (apnsErr != null) {
                    final DPCSendException sendException = new DPCSendException("Unable to send message: APNs status code: " + apnsErr, responseInfo.exception);
                    sendException.setRecoverable(isRecoverableError(apnsErr));
                    if (messageInfo != null) {
                        processSendError(sendException, messageInfo);
                    } else {
                        getTrace().put(EEventSeverity.ERROR, "APNS returned status code " + apnsErr + " for unexpected message with id " + responseInfo.messageId, EEventSource.PERSOCOMM_UNIT);
                    }
                } else if (responseInfo.exception != null) {
                    final DPCSendException sendException = responseInfo.exception instanceof DPCSendException ? (DPCSendException) responseInfo.exception : new DPCSendException("Error: " + responseInfo.exception.getMessage(), responseInfo.exception);
                    if (messageInfo != null) {
                        processSendError(sendException, messageInfo);
                    } else {
                        getTrace().put(EEventSeverity.ERROR, "Unexpected exception while sending #" + responseInfo.messageId + ": " + ExceptionTextFormatter.throwableToString(sendException), null, null, getEventSource(), false);
                    }
                } else if (responseInfo.connectionClosed) {
                    getTrace().put(EEventSeverity.ERROR, "APNs closed connection during message #" + responseInfo.messageId + " processing: " + responseInfo.deliveryError, null, null, getEventSource(), false);
                    if (messageInfo != null) {
                        final DPCRecoverableSendException sendException = new DPCRecoverableSendException("APNs closed connection: " + responseInfo.deliveryError);
                        processSendError(sendException, messageInfo);
                    }
                }
                
                if (!responseInfo.sent) { // there can be preliminary 'sent' response for actually erroneous message
                    processedErrorMessages.add(responseInfo.messageId);
                }
            }
        }

        if (wasFull && canTriggerSend && queuedMessages.size() < options.apnsMaxParallelSendCount) {
            triggerSend();
        }
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

    protected class WriteApnsCommunicationAdapter implements ICommunicationAdapter {

        public WriteApnsCommunicationAdapter() throws IOException {
            apnsService.testConnection();
        }

        private MessageInfo prepareMessage(MessageWithMeta messageWithMeta) throws DPCSendException {
            try {
                final Long messageId = messageWithMeta.id;
                final MessageDocument md = messageWithMeta.xDoc;
                
                final PushNotificationAddress xAddress = PushNotificationAddressDocument.Factory.parse(md.getMessage().getAddressTo()).getPushNotificationAddress();
                final PushNotificationMessage xMessage = PushNotificationMessageDocument.Factory.parse(md.getMessage().getBody()).getPushNotificationMessage();
                final PayloadBuilder builder = APNS.newPayload();

                if (xMessage.isSetBody() || xMessage.isSetTitle() || xMessage.isSetBadge()) {                
                    if (xMessage.isSetTitle()) {
                        builder.alertTitle(xMessage.getTitle());
                    }
                    if (xMessage.isSetBody()) {
                        builder.alertBody(xMessage.getBody());
                    }
                    if (xMessage.isSetBadge()) {
                        builder.badge(xMessage.getBadge().intValue());
                    }                    
                    if (xMessage.isSetIcon()) {
                        builder.launchImage(xMessage.getIcon());
                    }
                    if (xMessage.isSetSound()) {
                        builder.sound(xMessage.getSound());
                    }
                } else {
                    builder.instantDeliveryOrSilentNotification();
                }
                if (xMessage.isSetData()) {
                    builder.customFields(Maps.fromXml(xMessage.getData()));
                }

                final MessageInfo info = new MessageInfo(messageWithMeta, new EnhancedApnsNotification(messageId.intValue(), (int) (xMessage.isSetTimeToLiveSec() ? (System.currentTimeMillis() / 1000 + xMessage.getTimeToLiveSec()) : Integer.MAX_VALUE), xAddress.getReceiverId(), builder.build()));

                getTrace().debug("Sending: \n" + info.xNotification, getEventSource(), true);

                return info;
            } catch (XmlException ex) {
                throw new DPCSendException(ex.getMessage(), ex);
            }
        }

        @Override
        public MessageSendResult sendMessage(MessageWithMeta messageWithMeta) throws DPCSendException {
            final MessageInfo msg = prepareMessage(messageWithMeta);
            apnsService.push(msg.xNotification);
            queuedMessages.add(msg);
            return MessageSendResult.UNKNOWN;
        }

        @Override
        public MessageDocument receiveMessage() throws DPCRecvException {
            throw new IllegalStateException("This method can't be called for adapter to transmit");
        }

        @Override
        public void close() throws IOException {
        }

        @Override
        public boolean isPersistent() {
            return false;
        }
    }

    private static class MessageInfo extends MessageWithMeta {

        public final EnhancedApnsNotification xNotification;
        public long sentTimeNanos = Long.MIN_VALUE;

        public MessageInfo(MessageWithMeta messageWithMeta, EnhancedApnsNotification notification) {
            super(messageWithMeta);
            this.xNotification = notification;
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
            this.messageId = messageId;
            this.deliveryError = deliveryError;
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
