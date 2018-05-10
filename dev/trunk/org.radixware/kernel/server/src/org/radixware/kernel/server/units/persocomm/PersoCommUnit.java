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

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import org.apache.commons.lang.StringUtils;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.ESmppEncoding;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.mail.enums.EMailSecureConnection;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.server.aio.Event;
import org.radixware.kernel.server.aio.EventDispatcher;
import org.radixware.kernel.server.aio.EventHandler;
import org.radixware.kernel.server.aio.ServiceManifestServerLoader;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.exceptions.DPCRecvException;
import org.radixware.kernel.server.exceptions.DPCSendException;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.common.enums.EDatabaseType;
import org.radixware.kernel.common.enums.EPersoCommDeliveryStatus;
import org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy;
import org.radixware.kernel.common.exceptions.ServiceCallSendException;
import org.radixware.kernel.common.exceptions.ServiceConnectTimeout;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.server.units.AsyncEventHandlerUnit;
import org.radixware.kernel.server.units.Messages;
import org.radixware.kernel.server.units.UnitView;
import org.radixware.kernel.server.jdbc.IDatabaseConnectionAccess;
import org.radixware.kernel.server.jdbc.IExtendedDbQueries;
import org.radixware.kernel.server.monitoring.PersoCommMonitor;
import org.radixware.kernel.server.units.persocomm.interfaces.ICommunicationAdapter;
import org.radixware.kernel.server.utils.OptionsGroup;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.schemas.personalcommunications.MessageDocument;
import org.radixware.schemas.personalcommunications.MessageStatistics;

public abstract class PersoCommUnit extends AsyncEventHandlerUnit implements EventHandler {

    protected static final long MAX_SYNC_DURATION_MILLIS = SystemPropUtils.getIntSystemProp("rdx.pcunit.max.sync.op.duration.millis", 30000);
    protected static final String EXCEPTION_STACK_TRACE_FLOOD_KEY_PREFIX = "pc-ex-stack-";

    private static final long TIC_MILLIS = 1000;
    private static final long YIELD_PERIOD_MILLIS = 30000L;
    private static final int EXCEPTION_STACK_TRACE_PERIOD_MILLIS = SystemPropUtils.getIntSystemProp("rdx.pcunit.ex.stack.trace.period.millis", 1000 * 60 * 60);
    private static final long DEFAULT_THROTTLE_PERIOD_MILLIS = SystemPropUtils.getLongSystemProp("rdx.pcunit.default.throttle.period.millis", 30000L);
    
    public Options options = null;

    private final PCUnitView view = new PCUnitView(this);
    private final IDatabaseConnectionAccess dbca;
    private PersocommDbQueries queries;

    protected PersoCommAasClient aasCallback = null;
    private ServiceManifestServerLoader manifest = null;
    private long lastSendMillis = -1;
    private long lastRecvMillis = -1;
    private long lastYieldMillis = System.currentTimeMillis();
    private long throttleUntilMillis = Long.MIN_VALUE;
    private boolean lastSendTimeSliceExceeded = false;

    private EPersoCommDeliveryStatus initialDeliveryStatus = null;
    protected final MessageDocument deliveryReceiptDocument = MessageDocument.Factory.newInstance();

    final PersoCommMonitor monitor;

    public PersoCommUnit(Instance instModel, Long id, String title) {
        super(instModel, id, title);

        this.dbca = new IDatabaseConnectionAccess() {
            @Override
            public EDatabaseType getDatabaseType() {
                return EDatabaseType.ORACLE;
            }

            @Override
            public Connection getConnection() throws SQLException {
                return PersoCommUnit.this.getDbConnection();
            }

        };
        getTrace().setFloodPeriod(EXCEPTION_STACK_TRACE_FLOOD_KEY_PREFIX, EXCEPTION_STACK_TRACE_PERIOD_MILLIS);
        monitor = new PersoCommMonitor(this);
    }

    public PersocommDbQueries getDBQuery() {
        if (this.queries == null) {
            this.queries = new PersocommDbQueries(dbca, this);
        }
        return queries;
    }

    @Override
    public boolean isSingletonUnit() {
        return true;
    }

    @Override
    protected String getIdForSingletonLock() {
        return "ChannelUnit#" + getPrimaryUnitId();
    }

    @Override
    public boolean isSingletonByPrimary() {
        return true;
    }

    public abstract ICommunicationAdapter getCommunicationAdapter(CommunicationMode mode) throws IOException;

    public abstract OptionsGroup optionsGroup(final Options options);

    public abstract boolean supportsTransmitting();

    public abstract boolean supportsReceiving();
    
    protected void debug(String message) {
        getTrace().debug(message, getEventSource(), false);
    }
    
    protected void event(String message) {
        getTrace().put(EEventSeverity.EVENT, message, null, null, getEventSource(), false);
    }
    
    protected void error(String message) {
        getTrace().put(EEventSeverity.ERROR, message, null, null, getEventSource(), false);
    }
    
    protected boolean processSqlException(SQLException e) {
        final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
        getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + exStack, PCMessages.W_MLS_ID_ERR_IN_DB_QRY.getMlsId(), new ArrStr(getFullTitle(), exStack), getEventSource(), false);
        try {
            restoreDbConnection();
            monitor.setConnection(getDbConnection());
        } catch (InterruptedException ie) {
            return false;
        }
        return true;
    }
    
    private SendCallbackDispatcher sendCallbackDispatcher;
    private Thread sendCallbackThread;
    
    DeliveryCallbackDispatcher deliveryCallbackDispatcher;
    Thread deliveryCallbackThread;
    
    @Override
    public void onEvent(final Event ev) {
        
        // Reliable send callback - read messages from db with sendCallbackRequired = 1 and add them to "Send Callback Queue"
        if (supportsTransmitting() && needProcessing() && sendCallbackDispatcher.needReadFromDb()) {
            try {
                // on startup and in case of queue overflow
                sendCallbackDispatcher.readFromDbIfRequired();
                monitor.tic();
            } catch (SQLException e) {
                if (!processSqlException(e)) {
                    return;
                }
            } catch (IOException ex) {
                final String exceptionStack = ExceptionTextFormatter.throwableToString(ex);
                getTrace().put(EEventSeverity.ERROR, PCMessages.UNEXPECTED_ERROR + ": " + exceptionStack, PCMessages.W_MLS_UNEXPECTED_ERROR.getMlsId(), new ArrStr(exceptionStack), getEventSource(), false);
            }
        }
        
        if (!isThrottling() && supportsTransmitting() && needProcessing() && options.sendPeriod != null
                && (lastSendTimeSliceExceeded || System.currentTimeMillis() >= lastSendMillis + options.sendPeriod * 1000)) {
            lastSendTimeSliceExceeded = false;
            try {
                getTrace().put(EEventSeverity.DEBUG, "Start processing send queue", PCMessages.W_MLS_ID_START_SEND.getMlsId(), null, getEventSource(), false);
                getDBQuery().processExpiredMessages();
                final int processed = sendMessages();
                getTrace().put(EEventSeverity.DEBUG, "Messages sent: " + processed, PCMessages.W_MLS_ID_STOP_SEND.getMlsId(), new ArrStr(String.valueOf(processed)), getEventSource(), false);
                monitor.tic();
            } catch (SQLException e) {
                if (!processSqlException(e)) {
                    return;
                }
            } catch (IOException ex) {
                final String exceptionStack = ExceptionTextFormatter.throwableToString(ex);
                getTrace().put(EEventSeverity.ERROR, PCMessages.UNEXPECTED_ERROR + ": " + exceptionStack, PCMessages.W_MLS_UNEXPECTED_ERROR.getMlsId(), new ArrStr(exceptionStack), getEventSource(), false);
            } finally {
                lastSendMillis = System.currentTimeMillis();
            }
        }
        if (!isThrottling() && supportsReceiving() && needProcessing() && options.recvPeriod != null && System.currentTimeMillis() >= lastRecvMillis + options.recvPeriod * 1000) {
            try {
                getTrace().put(EEventSeverity.DEBUG, "Start receiving messages", PCMessages.W_MLS_ID_START_RECV.getMlsId(), null, getEventSource(), false);
                final int processed = recvMessages();
                getTrace().put(EEventSeverity.DEBUG, "Messages received: " + processed, PCMessages.W_MLS_ID_STOP_RECV.getMlsId(), new ArrStr(String.valueOf(processed)), getEventSource(), false);
                monitor.tic();
            } catch (SQLException e) {
                if (!processSqlException(e)) {
                    return;
                }
            } catch (DPCRecvException e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                getTrace().put(EEventSeverity.ERROR, "Receive message error: " + exStack, PCMessages.W_MLS_ID_RECV_ERROR.getMlsId(), new ArrStr(exStack), getEventSource(), false);
            } finally {
                lastRecvMillis = System.currentTimeMillis();
            }
        }
        if (needProcessing()) {
            if (System.currentTimeMillis() >= lastYieldMillis + YIELD_PERIOD_MILLIS) {
                getDispatcher().yield();
                lastYieldMillis = System.currentTimeMillis();
            }
            waitDispatcherEvent(System.currentTimeMillis() + (lastSendTimeSliceExceeded ? 0 : TIC_MILLIS));
        }
    }

    @Override
    protected UnitView newUnitView() {
        return view;
    }

    protected abstract void checkOptions(final Options options) throws Exception;

    protected boolean needProcessing() {
        return !isShuttingDown();
    }

    protected void waitDispatcherEvent(final long timeMillis) {
        getDispatcher().waitEvent(new EventDispatcher.TimerEvent(), this, timeMillis);
    }

    protected PersoCommAasClient createPersoCommClient() {
        return new PersoCommAasClient(this, manifest);

    }

    protected PersoCommAasClient getPersoCommClient() {
        return aasCallback;
    }

    @Override
    protected boolean startImpl() throws Exception {
        if (!super.startImpl()) {
            return false;
        } else {
            return prepareImpl();
        }
    }

    @Override
    protected void stopImpl() {
        unprepareImpl();
        super.stopImpl();
    }

    protected boolean prepareImpl() {
        manifest = new ServiceManifestServerLoader() {
            @Override
            protected Connection getDbConnection() {
                return PersoCommUnit.this.getDbConnection();
            }

            @Override
            protected Arte getArte() {
                return null;
            }
        };

        try {
            if ((options = getDBQuery().getUnitOptions()) != null) {
                checkOptions(options);
                final boolean needTracking = supportsReceiving() && options.deliveryTrackingPolicy != EPersoCommDeliveryTrackingPolicy.NONE;
                initialDeliveryStatus = needTracking ? EPersoCommDeliveryStatus.TRACKING : null;
                final OptionsGroup optionsGroup = optionsGroup(options);
                if (getPrimaryUnitId() != getId()) {
                    optionsGroup.add(Messages.PRIMARY_UNIT, getPrimaryUnitId());
                }
                final String optionsStr = optionsGroup.toString();
                getTrace().put(EEventSeverity.EVENT, Messages.START_OPTIONS + optionsStr, PCMessages.W_MLS_ID_START_OPTIONS.getMlsId(), new ArrStr(getFullTitle(), optionsStr), getEventSource(), false);
                monitor.setConnection(getDbConnection());
                monitor.rereadSettings();
            } else {
                throw new RadixError(Messages.ERR_CANT_READ_OPTIONS);
            }
        } catch (RadixError ex) {
            throw ex;
        } catch (Exception ex) {
            getTrace().put(EEventSeverity.ERROR, ExceptionTextFormatter.throwableToString(ex), null, null, getEventSource(), false);
            return false;
        }

        waitDispatcherEvent(System.currentTimeMillis());

        try {
            getDBQuery().clearOutMessageFlagAll(getPrimaryUnitId());
        } catch (SQLException ex) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(ex);
            getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + exStack, PCMessages.W_MLS_ID_ERR_IN_DB_QRY.getMlsId(), new ArrStr(getFullTitle(), exStack), getEventSource(), false);
        }
        aasCallback = createPersoCommClient();
        
        sendCallbackDispatcher = new SendCallbackDispatcher();
        sendCallbackThread = new Thread(sendCallbackDispatcher, "PersoCommSendCallbackThread for unit #" + getId());
        sendCallbackThread.start();
        
        lastSendMillis = -1;
        lastRecvMillis = -1;
        return true;
    }

    protected void unprepareImpl() {
        if (sendCallbackDispatcher != null) {
            sendCallbackDispatcher.stopping = true;
            sendCallbackDispatcher = null;
        }
        
        if (deliveryCallbackDispatcher != null) {
            deliveryCallbackDispatcher.stopping = true;
            deliveryCallbackDispatcher = null;
        }
        
        try {
            monitor.flush();
        } catch (SQLException ex) {
            final String exStack = ExceptionTextFormatter.throwableToString(ex);
            getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(getFullTitle(), exStack), getEventSource(), false);
        }
        
        if (sendCallbackThread != null) {
            sendCallbackThread.interrupt();
            sendCallbackThread = null;
        }
        
        if (deliveryCallbackThread != null) {
            deliveryCallbackThread.interrupt();
            deliveryCallbackThread = null;
        }

        if (queries != null) {
            queries.closeAll();
            getDBQuery().closeAll();
        }
        if (getDispatcher() != null) {
            getDispatcher().unsubscribeFromTimerEvents(this);
        }

        if (aasCallback != null) {
            try {
                aasCallback.close();
            } catch (Exception exc) {
                if (!(exc instanceof IOException)) {
                    throw exc;
                }
            }
            aasCallback = null;
        }
        
        processAllEventsSuppressingRuntimeExceptions();
        manifest = null;
    }

    protected int sendMessages() throws SQLException, IOException {
        int count = 0;
        ICommunicationAdapter adapter = null;
        boolean forceAdapterClose = false;
        final long startMillis = System.currentTimeMillis();
        try {
            try (IExtendedDbQueries.IDbCursor<MessageWithMeta> cursor = getDBQuery().getMessages2Send(this.getPrimaryUnitId())) {
                for (MessageWithMeta item : cursor.list(MessageWithMeta.class)) {
                    try {
                        try {
                            if (adapter == null) {
                                adapter = getCommunicationAdapter(CommunicationMode.TRANSMIT);
                            }
                        } catch (Throwable t) {
                            if (t instanceof DPCRecoverableSendException) {
                                throw (DPCRecoverableSendException) t;
                            } else {
                                throw new DPCRecoverableSendException("Unable to prepare send connection: " + t.getMessage(), t);
                            }
                        }
                        getDBQuery().markMessageAsInProcess(item.id);
                        try {
                            adapter.sendMessage(item);
                            getDbConnection().commit();
                            count++;
                            lastSendTimeSliceExceeded = System.currentTimeMillis() - startMillis > MAX_SYNC_DURATION_MILLIS;
                            if (lastSendTimeSliceExceeded || isShutdownRequested() || Thread.interrupted()) {
                                break;
                            }
                        } catch (Throwable t) {
                            if (t instanceof DPCSendException) {
                                throw (DPCSendException) t;
                            }
                            throw new DPCSendException("Unable to send message #" + item.id, t);
                        }
                    } catch (DPCSendException exc) {
                        boolean wasIoException = exc.wasIoException();
                        Throwable cause = exc.getCause();
                        while (!wasIoException && cause != null) {
                            if (cause instanceof IOException) {
                                wasIoException = true;
                            }
                            cause = cause.getCause();
                        }
                        if (wasIoException) {
                            forceAdapterClose = true;
                            exc.setRecoverable(true);
                        }
                        processSendError(exc, item);
                    }
                    getDbConnection().commit();
                    if (adapter == null || forceAdapterClose || isThrottling()) {//error on create
                        break;
                    }
                }
                getDbConnection().commit();
            } catch (Exception ex) {
                if (ex instanceof IOException) {
                    throw (IOException) ex;
                }
                throw new IOException("Error on sending messages", ex);
            }
        } finally {
            if (adapter != null && (!adapter.isPersistent() || forceAdapterClose)) {
                try {
                    adapter.close();
                } catch (Exception ex) {
                    getTrace().put(EEventSeverity.WARNING, "Error while closing send connection: " + ExceptionTextFormatter.throwableToString(ex), null, null, getEventSource(), false);
                }
            }
        }

        return count;
    }

    protected void processSendError(DPCSendException sendException, MessageWithMeta message) {
        final String exMessage = ExceptionTextFormatter.getExceptionMess(sendException);
        final String exStack = ExceptionTextFormatter.exceptionStackToString(sendException);

        boolean recoverable = sendException != null && sendException.isRecoverable();
        long throttlePeriodMillis = sendException == null ? 0 : sendException.getThrottlePeriodMillis();
        throttlePeriodMillis = throttlePeriodMillis == -1 ? DEFAULT_THROTTLE_PERIOD_MILLIS : throttlePeriodMillis;
        throttleUntilMillis = throttlePeriodMillis == 0 ? Long.MIN_VALUE : System.currentTimeMillis() + throttlePeriodMillis;

        getTrace().put(EEventSeverity.ERROR, (recoverable ? "Recoverable" : "Unrecoverable") + " error while sending message #" + message.id + "(error message: '" + exMessage + "')", PCMessages.W_MLS_ID_SENT_ERROR.getMlsId(), new ArrStr("message #" + message.id + ": " + exMessage), getEventSource(), false);
        getTrace().putFloodControlled(getExceptionStackFloodKey(exStack), EEventSeverity.ERROR, "Exception stack on sending message #" + message.id + ": " + exStack, null, null, getEventSource(), -1, false, null);
        
        final boolean sendCallbackRequired = !recoverable
                && StringUtils.isNotBlank(message.sendCallbackClassName)
                && StringUtils.isNotBlank(message.sendCallbackMethodName);
        String sendCallbackErrorText = null;
        
        try {
            if (recoverable) {
                final long baseForwardTimeMillis = Utils.nvlOf(message.baseForwardTimeMillis, System.currentTimeMillis());
                final long lastForwardTimeMillis = Utils.nvlOf(message.lastForwardTimeMillis, baseForwardTimeMillis);
                final long prevBaseForwardTimeMillis = Math.max(baseForwardTimeMillis, lastForwardTimeMillis);
                final long nextBaseForwardTimeMillis = prevBaseForwardTimeMillis + options.forwardDelaySec * 1000;
                final boolean needForward = nextBaseForwardTimeMillis <= System.currentTimeMillis();
                final long newBaseForwardTimeMillis = needForward ? System.currentTimeMillis() : prevBaseForwardTimeMillis;
                
                getTrace().put(EEventSeverity.DEBUG,
                            "System.currentTimeMillis()         = " + System.currentTimeMillis()
                        + "\nmessage.getBaseForwardTimeMillis() = " + message.baseForwardTimeMillis
                        + "\nmessage.getLastForwardTimeMillis() = " + message.lastForwardTimeMillis
                        + "\nbaseForwardTimeMillis              = " + baseForwardTimeMillis
                        + "\nlastForwardTimeMillis              = " + lastForwardTimeMillis
                        + "\nprevBaseForwardTimeMillis          = " + prevBaseForwardTimeMillis
                        + "\nnextBaseForwardTimeMillis          = " + nextBaseForwardTimeMillis
                        + "\nnewBaseForwardTimeMillis           = " + newBaseForwardTimeMillis
                        + "\nneedForward = " + needForward, EEventSource.PERSOCOMM_UNIT);

                markMessageAsFailed(message.id, false, newBaseForwardTimeMillis, "Recoverable error while sending message: id=(%1$d), problem is %2$s", message.id, sendException.getMessage());

                if (needForward) {
                    try {
                        final Long newUnitId = getPersoCommClient().invokeFindChannel(message.id, getPrimaryUnitId(), options.routingPriority);
                        if (newUnitId != null && newUnitId != getPrimaryUnitId()) {
                            getDBQuery().assignNewChannel(message.id, newUnitId);
                            getTrace().put(EEventSeverity.EVENT, "Message #" + message.id + " was forwarded to unit #" + newUnitId + " after sending error", EEventSource.PERSOCOMM_UNIT);
                        }
                    } catch (Exception ex) {
                        getTrace().put(EEventSeverity.WARNING, "Exception while trying to forward unsent message #" + message.id + " to another unit: " + ExceptionTextFormatter.throwableToString(ex), EEventSource.PERSOCOMM_UNIT);
                    }
                }
            } else {
                markMessageAsFailed(message.id, true, null, "Unrecoverable error while sending message: id=(%1$d), problem is %2$s", message.id, sendException.getMessage());
                if (sendCallbackRequired) {
                    getDBQuery().save2Sent(message.id, null, null, 1, null);
                }
                sendCallbackErrorText = "Unrecoverable error while sending message #" + message.id + "; error message: '" + exMessage + "'; stack: '" + exStack + "'";
            }
            getDbConnection().commit();
            monitor.registerNotSentMessage();
        } catch (SQLException ex) {
            getTrace().put(EEventSeverity.ERROR, "Unexpected SQL excaption: " + ExceptionTextFormatter.throwableToString(ex), EEventSource.PERSOCOMM_UNIT);
            try {
                restoreDbConnection();
                monitor.setConnection(getDbConnection());
            } catch (InterruptedException ex1) {
                throw new RuntimeException(ex1);
            }
        }
        if (sendCallbackRequired) {
            sendCallbackDispatcher.add(new SendCallbackData(message.id, sendCallbackErrorText));
        }
    }

    private String getExceptionStackFloodKey(final String stack) {
        return EXCEPTION_STACK_TRACE_FLOOD_KEY_PREFIX + Objects.hashCode(stack);
    }

    protected void markMessageAsFailed(final Long id, final boolean isUnrecoverable, final Long baseForwardTimeMillis, final String message, final Object... parameters) throws SQLException {
        getDBQuery().markMessageAsFailed(id, isUnrecoverable, baseForwardTimeMillis, parameters == null || parameters.length == 0 ? message : String.format(message, parameters));
    }

    protected int recvMessages() throws DPCRecvException, SQLException {
        int count = 0;
        try {
            final ICommunicationAdapter adapter = getCommunicationAdapter(CommunicationMode.RECEIVE);
            boolean forceAdapterClose = false;
            try {
                MessageDocument message;
                while ((message = adapter.receiveMessage()) != null) {
                    if (message != deliveryReceiptDocument) {
                        final String descr = "from=" + (message.getMessage() == null ? null : message.getMessage().getAddressFrom());
                        getTrace().put(EEventSeverity.DEBUG, "Message received, " + descr, PCMessages.W_MLS_ID_MESS_RECEVIED_AND_STORED.getMlsId(), new ArrStr(descr), getEventSource(), false);
                        if (message.getMessage() != null && !message.getMessage().isSetChannelId()) {
                            message.getMessage().setChannelId(getPrimaryUnitId());
                        }
                        final long beginNanos = System.nanoTime();
                        aasCallback.invokeAfterRecv(message);
                        final long procDurationNanos = System.nanoTime() - beginNanos;
                        monitor.regiesteReceivedMessage(procDurationNanos);
                        count++;
                    }
                }
            } catch (DPCRecvException exc) {
                if (exc.getCause() instanceof IOException) {
                    forceAdapterClose = true;
                }
                throw exc;
            } finally {
                if (adapter != null && (!adapter.isPersistent() || forceAdapterClose)) {
                    try {
                        adapter.close();
                    } catch (Exception ex) {
                        getTrace().put(EEventSeverity.WARNING, "Exception while closing sending connection: " + ExceptionTextFormatter.throwableToString(ex), null, null, getEventSource(), false);
                    }
                }
            }
        } catch (Exception ex) {
            if (ex instanceof DPCRecvException) {
                throw (DPCRecvException) ex;
            }
            throw new DPCRecvException(ex.getClass().getName() + ": " + ex.getMessage(), ex);
        }
        return count;
    }

    protected MessageSendResult sendCallback(final MessageSendResult result) {
        final MessageWithMeta message = result.getMessageWithMeta();
        final Long id = result.messageId;
        final MessageStatistics stat = result.getStatistics();
        String smppMessageId = result.getSmppMessageId();
        getTrace().put(EEventSeverity.DEBUG, "Message sent (id = " + id + ")", PCMessages.W_MLS_ID_SENT.getMlsId(), new ArrStr(String.valueOf(id)), getEventSource(), false);
        try {
            monitor.registerSentMessage(result);
        } catch (Exception ex) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(ex);
            getTrace().put(EEventSeverity.ERROR, "Exception on calling afterSend: " + ExceptionTextFormatter.throwableToString(ex), PCMessages.W_MLS_ID_MOVE_TO_ARHIVE_ERROR.getMlsId(), new ArrStr(exStack), getEventSource(), false);
        }
        
        final boolean sendCallbackRequired = message != null
                && StringUtils.isNotBlank(message.sendCallbackClassName)
                && StringUtils.isNotBlank(message.sendCallbackMethodName);
        final Integer sendCallbackRequiredInt = sendCallbackRequired ? 1 : null;
        final boolean deliveryCallbackRequired = message != null
                && StringUtils.isNotBlank(message.deliveryCallbackClassName)
                && StringUtils.isNotBlank(message.deliveryCallbackMethodName);
        final Integer deliveryCallbackRequiredInt = !sendCallbackRequired && deliveryCallbackRequired ? 1 : null;
        final EPersoCommDeliveryStatus deliveryStatus = deliveryCallbackRequired ? EPersoCommDeliveryStatus.TRACKING : initialDeliveryStatus;
        
        try {
            getDBQuery().save2Sent(id, deliveryStatus, smppMessageId, sendCallbackRequiredInt, deliveryCallbackRequiredInt);
            getTrace().put(EEventSeverity.DEBUG, "Message moved to archive (id = " + String.valueOf(id) + ")", PCMessages.W_MLS_ID_SAVED_TO_ARCHIVE.getMlsId(), new ArrStr(String.valueOf(id)), getEventSource(), false);
        } catch (SQLException e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            getTrace().put(EEventSeverity.ERROR, "Save message to archive error: " + exStack, PCMessages.W_MLS_ID_MOVE_TO_ARHIVE_ERROR.getMlsId(), new ArrStr(exStack), getEventSource(), false);
        }
        if (stat != null) {
            try {
                getDBQuery().saveStatistics(id, stat);
            } catch (SQLException e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                getTrace().put(EEventSeverity.ERROR, "Save message statistics to archive error: " + exStack, PCMessages.W_MLS_STAT_SAVE_TO_ARHIVE_ERROR.getMlsId(), new ArrStr(String.valueOf(id), exStack), getEventSource(), false);
            }
        }
        
        try {
            getDbConnection().commit();
        } catch (SQLException ex) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(ex);
            getTrace().put(EEventSeverity.ERROR, "Commit error: " + exStack, EEventSource.PERSOCOMM_UNIT);
        }
        
        if (sendCallbackRequired) {
            sendCallbackDispatcher.add(new SendCallbackData(id, null));
        }
        return result;
    }

    protected void triggerSend() {
        lastSendMillis = 0;
        ensureTimerEventNow();
    }

    protected void triggerReceive() {
        lastRecvMillis = 0;
        ensureTimerEventNow();
    }

    protected String getTempDirName() {
        return "pcunit_" + getId() + "_tmp";
    }

    protected File getTempDir() {
        final File dir = RadixLoader.getInstance().createTempFileWithExactName(getTempDirName());
        dir.mkdir();
        return dir;
    }
    
    private boolean isThrottling() {
        return System.currentTimeMillis() < throttleUntilMillis;
    }

    static final public class Options {

        public Long sendPeriod, recvPeriod, sendTimeout, routingPriority, minImportance, maxImportance;
        public String sendAddress, recvAddress;
        public String smppSystemId, smppSystemType, smppPassword, smppSourceAddress;
        public Long smppSessionType, smppSourceAddressTon, smppSourceAddressNpi, smppDestinationTon, smppDestinationNpi;
        public ESmppEncoding smppEncoding;
        public String pop3Address, emailLogin, emailPassword;
        public EMailSecureConnection emailSecureConnection;
        public String addressTemplate, subjectTemplate;
        public String encoding, fileFormat, kind;
        public String twitterConsumerToken, twitterConsumerSecret;
        public String twitterAccessToken, twitterAccessSecret;
        public String gcmApiKey;
        public String apnsKeyAlias;
        public String addressMask;
        public String messAddressRegexp;
        public Long apnsSuccessfulAfterMillis;
        public int apnsMaxParallelSendCount = 50;
        public int apnsSendSuccessfulAfterMillis = 5000;
        public EPersoCommDeliveryTrackingPolicy deliveryTrackingPolicy;
        public int deliveryTrackingPeriod;
        public int deliveryTrackingRetryPeriod;
        public int forwardDelaySec;
        public String wnsClientId, wnsClientSecret;
        public Long deliveryAckSapId;

        Options() {
        }
    }
    
    
    abstract class CallbackDispatcher<T> implements Runnable {
    }

    abstract class EventBasedCallbackDispatcher<T> extends CallbackDispatcher<T> {
        
        public final ArrayBlockingQueue<T> callbackQueue = new ArrayBlockingQueue<>(10000);
        public volatile boolean wasQueueOverflow = true;
        public volatile boolean stopping = false;

        public abstract String getCallbackType();

        public abstract boolean invokeImpl(T item) throws Exception;
        public abstract void onSuccess(T item) throws InterruptedException; 

        public abstract IExtendedDbQueries.IDbCursor<T> getCursor() throws Exception;
        public abstract Class<T> getDataClass();

        public boolean needReadFromDb() {
            return wasQueueOverflow && callbackQueue.isEmpty();
        }

        public void readFromDbIfRequired() throws SQLException, IOException {
            if (needReadFromDb()) {
                try {
                    debug("Start reading items from DB for " + getCallbackType() + " callback");
                    wasQueueOverflow = readFromDbImpl(getDataClass());
                    debug("Done reading items from DB for " + getCallbackType() + " callback, wasQueueOverflow = " + wasQueueOverflow);
                } catch (SQLException ex) {
                    throw ex;
                } catch (Exception ex) {
                    if (ex instanceof IOException) {
                        throw (IOException) ex;
                    }
                    throw new IOException("Error on reading item from DB for " + getCallbackType() + " callback", ex);
                }
            }
        }
        
        public boolean readFromDbImpl(Class<T> dataClass) throws Exception {
            try (IExtendedDbQueries.IDbCursor<T> cursor = getCursor()) {
                for (T item : cursor.list(dataClass)) {
                    if (!addNoWait(item)) {
                        return true;
                    }
                }
            }
            return false;
        }

        protected void addBlocking(T item) throws InterruptedException {
            callbackQueue.put(item);
        }

        protected boolean addNoWait(T item) {
            return callbackQueue.offer(item);
        }

        public void add(T item) {
            if (!wasQueueOverflow) {
                wasQueueOverflow = !addNoWait(item);
            }
        }

        public boolean invoke(T item) {
            try {
                return invokeImpl(item);
            } catch (ServiceConnectTimeout | ServiceCallSendException ex) {
                getTrace().put(EEventSeverity.ERROR, "Failed to invoke '" + getCallbackType() + "' callback: " + ExceptionTextFormatter.throwableToString(ex), EEventSource.PERSOCOMM_UNIT);
                return false;
            } catch (Exception ex) {
                getTrace().put(EEventSeverity.ERROR, "Error occured during '" + getCallbackType() + "' callback: " + ExceptionTextFormatter.throwableToString(ex), EEventSource.PERSOCOMM_UNIT);
                return true;
            }
        }

        @Override
        public void run() {
            while (!stopping && !Thread.interrupted()) {
                try {
                    final T item = callbackQueue.peek();
                    if (item == null) {
                        Thread.sleep(100);
                    } else {
                        final boolean ok = invoke(item);
                        if (ok) {
                            callbackQueue.poll();
                            onSuccess(item);
                        }
                    }
                } catch (InterruptedException ex) {
                    stopping = true;
                }
            }
        }

    }

    class SendCallbackDispatcher extends EventBasedCallbackDispatcher<SendCallbackData> {

        @Override
        public String getCallbackType() {
            return "after send";
        }

        @Override
        public boolean invokeImpl(SendCallbackData item) throws Exception {
            debug("Invoke '" + getCallbackType() + "' callback for message #" + item.messageId);
            aasCallback.invokeAfterSend(item.messageId, item.error);
            return true;
        }
        
        @Override
        public void onSuccess(SendCallbackData item) throws InterruptedException {
        }
  
        @Override
        public Class<SendCallbackData> getDataClass() {
            return SendCallbackData.class;
        }

        @Override
        public IExtendedDbQueries.IDbCursor<SendCallbackData> getCursor() throws Exception {
            return getDBQuery().getMessages2SendCallback(getPrimaryUnitId());
        }
    }
    
    class DeliveryCallbackDispatcher extends EventBasedCallbackDispatcher<MessageDeliveryInfo> {

        @Override
        public String getCallbackType() {
            return "after delivery";
        }

        @Override
        public boolean invokeImpl(MessageDeliveryInfo item) throws Exception {
            debug("Invoke '" + getCallbackType() + "' callback for message #" + item.messageId);
            try {
                final SentMessageData message = getDBQuery().readSentMessage(item.messageId);
                if (message.isStillTracking()) {
                    aasCallback.invokeAfterDelivery(item.messageId, item.deliveryStatus);
                }
            } catch (SQLException ex) {
            }
            return true;
        }

        @Override
        public void onSuccess(MessageDeliveryInfo item) throws InterruptedException {
        }

        @Override
        public Class<MessageDeliveryInfo> getDataClass() {
            return null;
        }

        @Override
        public IExtendedDbQueries.IDbCursor<MessageDeliveryInfo> getCursor() throws Exception {
            return null;
        }
    }

}
