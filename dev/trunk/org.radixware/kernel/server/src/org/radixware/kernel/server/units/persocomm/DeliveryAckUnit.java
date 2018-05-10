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

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import org.apache.commons.lang.StringUtils;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EPersoCommDeliveryStatus;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.common.exceptions.ServiceCallSendException;
import org.radixware.kernel.common.exceptions.ServiceConnectTimeout;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.exceptions.ShouldNeverHappenError;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.aio.Event;
import org.radixware.kernel.server.aio.ServiceServer;
import org.radixware.kernel.server.exceptions.SqlNoDataFound;
import org.radixware.kernel.server.exceptions.SqlResourceBusy;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.jdbc.IExtendedDbQueries;
import org.radixware.kernel.server.sap.Sap;
import org.radixware.kernel.server.units.persocomm.interfaces.ICommunicationAdapter;
import org.radixware.kernel.server.utils.OptionsGroup;
import org.radixware.schemas.nethub.ExceptionEnum;
import org.radixware.schemas.persocommdelivery.DeliveryItem;
import org.radixware.schemas.persocommdelivery.DeliveryMess;
import org.radixware.schemas.persocommdelivery.DeliveryRq;
import org.radixware.schemas.persocommdeliveryWsdl.DeliveryDocument;

public class DeliveryAckUnit extends PersoCommUnit {
    
    public DeliveryAckUnit(final Instance instModel, final Long id, final String title) {
        super(instModel, id, title);
    }

    @Override
    public ICommunicationAdapter getCommunicationAdapter(CommunicationMode mode) throws IOException {
        return null;
    }

    @Override
    public OptionsGroup optionsGroup(Options options) {
        return new OptionsGroup();
    }

    @Override
    public boolean supportsTransmitting() {
        return true;
    }

    @Override
    public boolean supportsReceiving() {
        return true;
    }

    @Override
    protected void checkOptions(Options options) throws Exception {
    }

    @Override
    public String getUnitTypeTitle() {
        return PCMessages.DELIVERY_ACK_UNIT_TYPE_TITLE;
    }

    @Override
    public Long getUnitType() {
        return EUnitType.DPC_DELIVERY_ACK.getValue();
    }
    
    @Override
    protected boolean startImpl() throws Exception {
        boolean ok = super.startImpl();
        if (ok) {
            service = new DeliverySap();
            ok = service.start(getDbConnection());
        }
        return ok;
    }
    
    @Override
    protected void stopImpl() {
        if (service != null) {
            try {
                service.stop();
            } catch (Throwable t) {
                logErrorOnStop(t);
            } finally {
                service = null;
            }
        }
        super.stopImpl();
    }
    
    private static final int NON_COMMITED_MESSAGES_QUEUE_SIZE_LIMIT = 5000;
    private static final int SEND_CALLBACK_NOT_DONE_QUEUE_SIZE_LIMIT = 100000;
    
    private static final long NON_COMMITED_MESSAGES_WAITING_PERION_MILLIS = 120000;
    
    private static final long READ_MESSAGES_READY_FOR_CALLBACK_PERIOD_MILLIS = 60000;
    private static final long READ_EXPIRED_MESSAGES_PERIOD_MILLIS = 5000;
    private static final long SEND_CALLBACK_NOT_DONE_CHECK_PERION_MILLIS = 5000;
    private static final long NON_COMMITED_MESSAGES_CHECK_PERION_MILLIS = 10000;
    private static final long SAP_SELF_CHECK_PERION_MILLIS = 60000;
    
    private PeriodicalDueTimeChecker readMessagesReadyForCallbackChecker = new PeriodicalDueTimeChecker(READ_MESSAGES_READY_FOR_CALLBACK_PERIOD_MILLIS);
    private PeriodicalDueTimeChecker readExpiredMessagesChecker = new PeriodicalDueTimeChecker(READ_EXPIRED_MESSAGES_PERIOD_MILLIS);
    private PeriodicalDueTimeChecker sendCallbackNotDoneChecker = new PeriodicalDueTimeChecker(SEND_CALLBACK_NOT_DONE_CHECK_PERION_MILLIS);
    private PeriodicalDueTimeChecker nonCommitedMessagesChecker = new PeriodicalDueTimeChecker(NON_COMMITED_MESSAGES_CHECK_PERION_MILLIS);
    private PeriodicalDueTimeChecker sapSelfCheckChecker = new PeriodicalDueTimeChecker(SAP_SELF_CHECK_PERION_MILLIS);
    
    private final Queue<MessageDeliveryInfo> nonCommitedList = new ArrayDeque<>();
    private final Queue<MessageDeliveryInfo> sendCallbackNotDoneList = new ArrayDeque<>();
    private final Queue<Long> callbackQueue = new ArrayDeque<>();
    
    private DeliverySap service = null;
    
    private boolean processIncomingMessage(MessageDeliveryInfo mdi) {
        while (true) {
            try {
                SentMessageData message = getDBQuery().readSentMessage(mdi.messageId);
                final boolean isSendCallbackNotDoneYet = StringUtils.isNotBlank(message.deliveryCallbackClassName)
                        && StringUtils.isNotBlank(message.deliveryCallbackMethodName)
                        && message.deliveryCallbackRequired == null;
                boolean isStillTracking = message.isStillTracking();
                debug("processIncomingMessage: #" + mdi.messageId + ": read: isSendCallbackNotDoneYet = " + isSendCallbackNotDoneYet + ", isStillTracking = " + isStillTracking);
                if (isSendCallbackNotDoneYet) {
                    if (isStillTracking) {
                        try {
                            getDBQuery().lockSentMessage(mdi.messageId);
                            debug("processIncomingMessage: #" + mdi.messageId + ": lock: done");
                            getDBQuery().setMessageDeliveryStatus(mdi.messageId, mdi.deliveryStatus);
                            getDbConnection().commit();
                        } catch (SqlResourceBusy ex) {
                            debug("processIncomingMessage: #" + mdi.messageId + ": lock: SqlResourceBusy");
                        }
                    }
                    debug("processIncomingMessage: #" + mdi.messageId + ": sendCallbackNotDoneList.add");
                    mdi = mdi.sendCallbackNotDone(message.deliveryExpTimeMillis);
                    if (mdi.waitExpired()) {
                        debug("processIncomingMessage: #" + mdi.messageId + ": sendCallbackNotDoneList - waiting period expired");
                    } else {
                        sendCallbackNotDoneList.add(mdi);
                    }
                    if (sendCallbackNotDoneList.size() > SEND_CALLBACK_NOT_DONE_QUEUE_SIZE_LIMIT) {
                        final MessageDeliveryInfo old = sendCallbackNotDoneList.poll();
                        debug("processIncomingMessage: sendCallbackNotDoneList.size exceeds limit, removing oldest (messageId = " + old.messageId);
                    }
                } else if (isStillTracking) {
                    message = getDBQuery().lockSentMessage(mdi.messageId);
                    isStillTracking = message.isStillTracking();
                    debug("processIncomingMessage: #" + mdi.messageId + ": lock: isStillTracking = " + isStillTracking);
                    if (isStillTracking) {
                        getDBQuery().setMessageDeliveryStatus(mdi.messageId, mdi.deliveryStatus);
                        debug("processIncomingMessage: #" + mdi.messageId + ": callbackQueue.add");
                        callbackQueue.add(mdi.messageId);
                    }
                    getDbConnection().commit();
                }
                debug("processIncomingMessage: #" + mdi.messageId + ": done");
                return true;
            } catch (SqlNoDataFound ex) {
                mdi = mdi.notFound(NON_COMMITED_MESSAGES_WAITING_PERION_MILLIS);
                if (mdi.problem == MessageDeliveryInfo.Problem.NonCommited) {
                    if (mdi.waitExpired()) {
                        debug("processIncomingMessage: #" + mdi.messageId + ": SqlNoDataFound - waiting period expired");
                    } else {
                        debug("processIncomingMessage: #" + mdi.messageId + ": SqlNoDataFound - nonCommitedList.add");
                        nonCommitedList.add(mdi);
                        if (nonCommitedList.size() > NON_COMMITED_MESSAGES_QUEUE_SIZE_LIMIT) {
                            final MessageDeliveryInfo old = nonCommitedList.poll();
                            debug("processIncomingMessage: SqlNoDataFound - nonCommitedList.size exceeds limit, removing oldest (messageId = " + old.messageId);
                        }
                    }
                } else {
                    debug("processIncomingMessage: #" + mdi.messageId + ": SqlNoDataFound - was deleted");
                }
                return true;
            } catch (SqlResourceBusy ex) {
                debug("processIncomingMessage: #" + mdi.messageId + ": SqlResourceBusy - return");
                return true;
            } catch (SQLException ex) {
                if (!processSqlException(ex)) {
                    return false;
                }
            } catch (Exception e) {
                throw new ShouldNeverHappenError(e);
            }
        }
    }
    
    private void readMessagesReadyForDeliveryCallback() {
        if (!readMessagesReadyForCallbackChecker.isDueTime() || !needProcessing()) {
            return;
        }

        try (IExtendedDbQueries.IDbCursor<SentMessageData> cursor = getDBQuery().getMessages2DeliveryCallback()) {
            for (SentMessageData message : cursor.list(SentMessageData.class)) {
                debug("readMessagesReadyForDeliveryCallback: #" + message.messageId + ": callbackQueue.add");
                callbackQueue.add(message.messageId);
            }
        } catch (SQLException ex) {
            processSqlException(ex);
        } catch (Exception e) {
            throw new ShouldNeverHappenError(e);
        }
    }
    
    private void readExpiredMessages() {
        if (!readExpiredMessagesChecker.isDueTime() || !needProcessing()) {
            return;
        }
        
        try (IExtendedDbQueries.IDbCursor<SentMessageData> cursor = getDBQuery().getMessagesWithExpiredDelivery()) {
            for (SentMessageData msg : cursor.list(SentMessageData.class)) {
                debug("readExpiredMessages: #" + msg.messageId + ": read: deliveryCallbackRequired = " + msg.deliveryCallbackRequired + ", deliveryStatus = " + msg.deliveryStatus);
                try {
                    msg = getDBQuery().lockSentMessage(msg.messageId);
                    debug("readExpiredMessages: #" + msg.messageId + ": lock: deliveryCallbackRequired = " + msg.deliveryCallbackRequired + ", deliveryStatus = " + msg.deliveryStatus);
                    if (msg.deliveryCallbackRequired == 1 && msg.deliveryStatus == EPersoCommDeliveryStatus.TRACKING
                            && getDBQuery().setMessageDeliveryExpired(msg.messageId)) { // was actually updated
                        debug("readExpiredMessages: #" + msg.messageId + ": updated + callbackQueue.add");
                        callbackQueue.add(msg.messageId);
                    }
                    getDbConnection().commit();
                } catch (SqlNoDataFound ex) {
                    debug("readExpiredMessages: #" + msg.messageId + ": lock: SqlNoDataFound");
                } catch (SqlResourceBusy ex) {
                    debug("readExpiredMessages: #" + msg.messageId + ": lock: SqlResourceBusy");
                }
            }
        } catch (SQLException ex) {
            processSqlException(ex);
        } catch (Exception e) {
            throw new ShouldNeverHappenError(e);
        }
    }
    
    private void processWithSnapshot(PeriodicalDueTimeChecker timesUpChecker, Queue<MessageDeliveryInfo> data) {
        if (!timesUpChecker.isDueTime() || !needProcessing()) {
            return;
        }
        
        final List<MessageDeliveryInfo> snapshot = new ArrayList<>(data);
        data.clear();
        for (MessageDeliveryInfo msg : snapshot) {
            if (!processIncomingMessage(msg)) {
                return;
            }
        }
    }

    private void processSendCallbackNonDoneMessages() {
        processWithSnapshot(sendCallbackNotDoneChecker, sendCallbackNotDoneList);
    }

    private void processNonCommitedMessages() {
        processWithSnapshot(nonCommitedMessagesChecker, nonCommitedList);
    }
    
    private void invokeAfterDelivery(long messageId, EPersoCommDeliveryStatus status) {
        try {
            debug("Invoking 'after delivery' callback for message #" + messageId);
            aasCallback.invokeAfterDelivery(messageId, status);
            debug("'After delivery' callback for message #" + messageId + " done without exceptions");
            callbackQueue.poll();
        } catch (ServiceConnectTimeout | ServiceCallSendException ex) {
            error("Failed to invoke 'after delivery' callback: " + ExceptionTextFormatter.throwableToString(ex));
        } catch (Exception ex) {
            error("'After delivery' callback for message #" + messageId + " done with exception:\n" + ExceptionTextFormatter.throwableToString(ex));
            callbackQueue.poll();
        }
    }
    
    private void processCallbackQueue() {
        Long messageId;
        while ((messageId = callbackQueue.peek()) != null && needProcessing()) { // and not timeslice exceeded (?)
            try {
                SentMessageData msg = getDBQuery().lockMessageJustBeforeDeliveryCallback(messageId);
                getDBQuery().markMessageJustBeforeDeliveryCallback(messageId);
                getDbConnection().commit();
                invokeAfterDelivery(messageId, msg.deliveryStatus);
            } catch (SqlNoDataFound ex) {
                debug("processCallbackQueue: message #" + messageId + " not found - already processed by another unit");
                callbackQueue.poll();
            } catch (SqlResourceBusy ex) {
                debug("processCallbackQueue: message #" + messageId + " is locked - processing by another unit");
                callbackQueue.poll();
            } catch (SQLException ex) {
                if (!processSqlException(ex)) {
                    return;
                }
            } catch (Exception e) {
                throw new ShouldNeverHappenError(e);
            }
        }
    }
    
    private void sapSelfCheck() {
        if (sapSelfCheckChecker.isDueTime() && needProcessing()) {
            service.dbSapSelfCheck();
        }
    }
    
    @Override
    public void onEvent(Event ev) {
        readMessagesReadyForDeliveryCallback();
        readExpiredMessages();
        processSendCallbackNonDoneMessages();
        processNonCommitedMessages();
        processCallbackQueue();
        sapSelfCheck();
        if (needProcessing()) {
            waitDispatcherEvent(System.currentTimeMillis() + 100);
        }
    }
    
    
    class DeliverySap extends Sap {
        
        public static final String SERVICE_WSDL = "http://schemas.radixware.org/persocommdelivery.wsdl";
        
        DeliverySap() {
            super(
                    getDispatcher(),
                    getTrace().newTracer(EEventSource.PERSOCOMM_UNIT.getValue()),
                    10, //maxSeanceCount
                    30, //rqWaitTimeout
                    "PersoCommDeliverySap#" + options.deliveryAckSapId
            );
        }

        @Override
        public long getId() {
            return options.deliveryAckSapId;
        }

        @Override
        protected boolean isShuttingDown() {
            return DeliveryAckUnit.this.isShuttingDown();
        }

        @Override
        protected void restoreDbConnection() throws InterruptedException {
            DeliveryAckUnit.this.restoreDbConnection();
        }

        @Override
        protected void process(ServiceServer.InvocationEvent event) {
            final XmlObject xml = event.rqEnvBodyContent;
            if (xml instanceof DeliveryDocument || xml instanceof DeliveryMess) {
                final DeliveryDocument doc = xml instanceof DeliveryDocument ? (DeliveryDocument)xml : null;
                final DeliveryMess mess = doc != null ? doc.getDelivery() : (DeliveryMess)xml;
                final DeliveryRq rq = mess.getDeliveryRq();
                final List<DeliveryItem> list = rq.getDeliveryItemList();
                for (DeliveryItem item: list) {
                    final long messageId = item.getMessageId();
                    final EPersoCommDeliveryStatus status = item.getDeliveryStatus();
                    final MessageDeliveryInfo deliveredMsg = new MessageDeliveryInfo(messageId, status);
                    if (!processIncomingMessage(deliveredMsg)) {
                        return;
                    }
                }
                final DeliveryDocument rsDoc = DeliveryDocument.Factory.newInstance();
                rsDoc.addNewDelivery().addNewDeliveryRs();
                event.seance.response(rsDoc, true);
            } else {
                event.seance.response(new ServiceProcessServerFault(ExceptionEnum.INVALID_REQUEST.toString(), "Request \"" + event.rqEnvBodyContent.getClass().getName() + "\" is not supported by \"" + SERVICE_WSDL + "\" service", null, null), true);
            }
        }
    }
    
    class PeriodicalDueTimeChecker {
        final long intervalMillis;
        long nextTimeMillis;
        
        PeriodicalDueTimeChecker(long intervalMillis) {
            this(intervalMillis, Long.MIN_VALUE);
        }
        
        PeriodicalDueTimeChecker(long intervalMillis, long startTimeMillis) {
            this.intervalMillis = intervalMillis;
            this.nextTimeMillis = startTimeMillis;
        }
        
        boolean isDueTime() {
            final long now = System.currentTimeMillis();
            final boolean res = now >= nextTimeMillis;
            if (res) {
                nextTimeMillis = now + intervalMillis;
            }
            return res;
        }
    }
    
}
