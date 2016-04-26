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
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.common.exceptions.CertificateUtilsException;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.ssl.CertificateUtils;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.Maps;
import org.radixware.kernel.common.utils.ValueFormatter;
import org.radixware.kernel.server.exceptions.DPCRecvException;
import org.radixware.kernel.server.exceptions.DPCSendException;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.units.Messages;
import org.radixware.kernel.server.units.persocomm.interfaces.IDatabaseConnectionAccess;
import org.radixware.kernel.server.units.persocomm.interfaces.IExtendedDbQueries.IDbCursor;
import org.radixware.kernel.server.units.persocomm.interfaces.IExtendedRadixTrace;
import org.radixware.kernel.server.utils.OptionsGroup;
import org.radixware.schemas.personalcommunications.MessageDocument;
import org.radixware.schemas.personalcommunications.MessageStatistics;
import org.radixware.schemas.personalcommunications.PushNotificationAddress;
import org.radixware.schemas.personalcommunications.PushNotificationAddressDocument;
import org.radixware.schemas.personalcommunications.PushNotificationMessage;
import org.radixware.schemas.personalcommunications.PushNotificationMessageDocument;


public class NewApnsUnit extends NewPCUnit implements ApnsDelegate {

    private static final String DEFAULT_SOUND_NAME = "default";
    private static final int GET_INACTIVE_DEVICES_PERIOD_MILLIS = 60000;

    private final ArrayBlockingQueue<MessageStatus> statusQueue = new ArrayBlockingQueue<>(10000);
    private final List<MessageInfo> queuedMessages = new LinkedList<>();

    private ApnsService apnsService;
    private long lastCheckInactiveDevicesMillis = 0;

    public NewApnsUnit(final Instance instModel, final Long id, final String title) {
        super(instModel, id, title);
    }

    public NewApnsUnit(final Instance instModel, final Long id, final String title, final IDatabaseConnectionAccess dbca, final IExtendedRadixTrace trace) {
        super(instModel, id, title, dbca, trace);
    }

    @Override
    public CommunicationAdapter getCommunicationAdapter(CommunicationMode mode) throws DPCRecvException, DPCSendException {
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

    @Override public boolean supportsTransmitting() {return true;}
    @Override public boolean supportsReceiving() {return false;}
    
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
        getTraceInterface().put(EEventSeverity.WARNING, "Cache size for sent messages reached " + i, null, null, getEventSource(), false);
    }

    @Override
    public void notificationsResent(int i) {
        getTraceInterface().put(EEventSeverity.DEBUG, i + " notification have been queued for resending", null, null, getEventSource(), false);
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
            if (messageInfo.sentTimeMillis > 0 && System.currentTimeMillis() - messageInfo.sentTimeMillis > options.apnsSendSuccessfulAfterMillis) {
                getTrace().debug("No errors regarding message #" + messageInfo.id + " received in " + options.apnsSendSuccessfulAfterMillis + " ms, considering as sent", getEventSource(), false);
                it.remove();
                sendCallback((long) messageInfo.id, null);
            }
        }
    }

    private void processResponses(final boolean canTriggerSend) {
        final List<MessageStatus> responseInfos = new ArrayList<>();
        final boolean wasFull = queuedMessages.size() == options.apnsMaxParallelSendCount;

        statusQueue.drainTo(responseInfos);
        for (MessageStatus responseInfo : responseInfos) {
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
        }

        if (wasFull && canTriggerSend && queuedMessages.size() < options.apnsMaxParallelSendCount) {
            triggerSend();
        }
    }

    private void logInactiveDevices() {
        if (System.currentTimeMillis() - lastCheckInactiveDevicesMillis > GET_INACTIVE_DEVICES_PERIOD_MILLIS) {
            lastCheckInactiveDevicesMillis = System.currentTimeMillis();
            getTraceInterface().put(EEventSeverity.DEBUG, "Getting list of inactive devices", null, null, getEventSource(), false);
            try {
                final Map<String, Date> devices = apnsService.getInactiveDevices();
                if (devices != null) {
                    for (Map.Entry<String, Date> entry : devices.entrySet()) {
                        getTraceInterface().put(EEventSeverity.WARNING, "Device token '" + entry.getKey() + "' became invalid on " + entry.getValue(), null, null, getEventSource(), false);
                    }
                }
            } catch (Exception ex) {
                getTraceInterface().put(EEventSeverity.WARNING, "Can't get inactive devices: " + ExceptionTextFormatter.exceptionStackToString(ex), null, null, getEventSource(), false);
            }

        }
    }

    protected void processSendError(final long id, final MessageDocument xMessageDoc, final DPCSendException e) throws SQLException {
        final String exMessage = ExceptionTextFormatter.getExceptionMess(e);
        final String exStack = ExceptionTextFormatter.exceptionStackToString(e);

        getDBQuery().clearOutMessageFlag(id);
        try{final Long[] channels = getDBQuery().getSuitableChannels(options.kind, xMessageDoc.getMessage().getAddressTo(), xMessageDoc.getMessage().getImportance(), getId(), options.routingPriority);

            if (channels.length > 0) {
                getDBQuery().assignNewChannel(id, channels[0]);
                getTraceInterface().put(EEventSeverity.ERROR, "Sending error, message was forwarded to unit #" + channels[0] + ": " + exMessage, PCMessages.W_MLS_ID_SENT_ERROR, new ArrStr(String.valueOf(exMessage)), getEventSource(), false);
                getTraceInterface().putFloodControlled(getExceptionStackFloodKey(exStack), EEventSeverity.ERROR, "Exception stack: " + exStack, null, null, getEventSource(), -1, false, null);
                return;
            }
        } catch (Exception ex) {
            final String exStackNew = ExceptionTextFormatter.exceptionStackToString(ex);
            getTraceInterface().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + exStackNew, PCMessages.W_MLS_ID_ERR_IN_DB_QRY, new ArrStr(getFullTitle(), exStackNew), getEventSource(), false);
        }
        getTraceInterface().put(EEventSeverity.ERROR, "Sending error (error message: '" + exMessage + "')", PCMessages.W_MLS_ID_SENT_ERROR, new ArrStr(exMessage), getEventSource(), false);
        getTraceInterface().putFloodControlled(getExceptionStackFloodKey(exStack), EEventSeverity.ERROR, "Exception stack: " + exStack, null, null, getEventSource(), -1, false, null);
    }

    private String getExceptionStackFloodKey(final String stack) {
        return EXCEPTION_STACK_TRACE_FLOOD_KEY_PREFIX + Objects.hashCode(stack);
    }

    protected class WriteApnsCommunicationAdapter implements CommunicationAdapter<MessageInfo> {

        private MessageStatistics stat;

        public WriteApnsCommunicationAdapter() throws DPCSendException {
            try {
                apnsService.testConnection();
            } catch (NetworkIOException ex) {
                throw new DPCSendException("APN service is unreachable", ex);
            }
        }

        @Override
        public MessageInfo prepareMessage(Long messageId, MessageDocument md) throws DPCSendException {
            try {
                final PushNotificationAddress xAddress = PushNotificationAddressDocument.Factory.parse(md.getMessage().getAddressTo()).getPushNotificationAddress();
                final PushNotificationMessage xMessage = PushNotificationMessageDocument.Factory.parse(md.getMessage().getBody()).getPushNotificationMessage();
                final PayloadBuilder builder = APNS.newPayload();

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

                final MessageInfo info = new MessageInfo(messageId, md, getStatistics(), new EnhancedApnsNotification(messageId.intValue(), (int) (xMessage.isSetTimeToLiveSec() ? (System.currentTimeMillis() / 1000 + xMessage.getTimeToLiveSec()) : Integer.MAX_VALUE), xAddress.getReceiverId(), builder.build()));

                getTrace().debug("Sending: \n" + info.xNotification, getEventSource(), true);

                return info;
            } catch (XmlException ex) {
                throw new DPCSendException(ex.getMessage(), ex);
            }
        }

        @Override
        public boolean sendMessage(Long messageId, MessageInfo msg) throws DPCSendException {
            apnsService.push(msg.xNotification);
            queuedMessages.add(msg);
            return true;
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
        public MessageInfo receiveMessage() throws DPCRecvException {
            throw new IllegalStateException("This method can't be called for adapter to transmit");
        }

        @Override
        public MessageDocument unprepareMessage(MessageInfo msg) throws DPCRecvException {
            throw new IllegalStateException("This method can't be called for adapter to transmit");
        }

        @Override
        public void close() throws IOException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }

    private static class MessageInfo {

        public final long id;
        public final MessageDocument xMessage;
        public final MessageStatistics xStatistics;
        public final EnhancedApnsNotification xNotification;

        public long sentTimeMillis = -1;

        public MessageInfo(long id, MessageDocument xMessage, MessageStatistics statistics, EnhancedApnsNotification notification) {
            this.id = id;
            this.xMessage = xMessage;
            this.xStatistics = statistics;
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
