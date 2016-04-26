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

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.ESmppEncoding;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.mail.enums.ESecureConnection;
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
import org.radixware.kernel.server.trace.ServerTrace;
import org.radixware.kernel.server.units.AsyncEventHandlerUnit;
import org.radixware.kernel.server.units.Messages;
import org.radixware.kernel.server.units.UnitView;
import org.radixware.kernel.server.units.persocomm.interfaces.IAASNotificationCallback;
import org.radixware.kernel.server.units.persocomm.interfaces.ICommunicationUnit;
import org.radixware.kernel.server.units.persocomm.interfaces.IDatabaseConnectionAccess;
import org.radixware.kernel.server.units.persocomm.interfaces.IExtendedRadixTrace;
import org.radixware.kernel.server.units.persocomm.interfaces.IHighLevelDbQuery;
import org.radixware.kernel.server.units.persocomm.interfaces.IHighLevelDbQuery.MessageWithId;
import org.radixware.kernel.server.units.persocomm.tools.MultiLangStringWrapper;
import org.radixware.kernel.server.utils.OptionsGroup;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.schemas.personalcommunications.MessageStatistics;

public abstract class NewPCUnit<T> extends AsyncEventHandlerUnit implements EventHandler, ICommunicationUnit<T> {

    protected static final long MAX_SYNC_DURATION_MILLIS = SystemPropUtils.getIntSystemProp("rdx.pcunit.max.sync.op.duration.millis", 5000);
    protected static final String EXCEPTION_STACK_TRACE_FLOOD_KEY_PREFIX = "pc-ex-stack-";

    private static final long TIC_MILLIS = 1000;
    private static final int EXCEPTION_STACK_TRACE_PERIOD_MILLIS = SystemPropUtils.getIntSystemProp("rdx.pcunit.ex.stack.trace.period.millis", 1000 * 60 * 60);

    public Options options = null;

    private final PCUnitView view = new PCUnitView(this);
    private final IDatabaseConnectionAccess dbca;
    private final IExtendedRadixTrace trace;
    private IHighLevelDbQuery queries;

    private IAASNotificationCallback aasCallback = null;
    private ServiceManifestServerLoader manifest = null;
    private long lastSendMillis = -1, lastRecvMillis = -1;

    public NewPCUnit(Instance instModel, Long id, String title) {
        super(instModel, id, title);

        this.trace = new IExtendedRadixTrace() {
            @Override
            public void setFloodPeriod(String floodKey, long periodMillis) {
                getTrace().setFloodPeriod(floodKey, periodMillis);
            }

            @Override
            public void put(EEventSeverity severity, String localizedMessage, MultiLangStringWrapper mlsId, List<String> mlsArgs, String source, boolean isSensetive) {
                getTrace().put(severity, localizedMessage, mlsId != null ? mlsId.getMlsId() : null, mlsArgs, source, isSensetive);
            }

            @Override
            public void putFloodControlled(String floodKey, EEventSeverity severity, String localizedMessage, MultiLangStringWrapper mlsId, List<String> mlsArgs, String source, long millisOrMinusOne, boolean isSensitive, Collection<ServerTrace.ETraceDestination> targetDestinations) {
                getTrace().putFloodControlled(floodKey, severity, localizedMessage, mlsId != null ? mlsId.getMlsId() : null, mlsArgs, source, millisOrMinusOne, isSensitive, targetDestinations);
            }

            @Override
            public void put(EEventSeverity severity, String localizedMess, EEventSource source) {
                getTrace().put(severity, localizedMess, source);
            }

            @Override
            public void put(EEventSeverity severity, String code, List<String> words, String source) {
                getTrace().put(severity, code, words, source);
            }

            @Override
            public void put(EEventSeverity eEventSeverity, String string, Object object, Object object0, EEventSource eEventSource, boolean b) {
                //                getTrace().put(eEventSeverity,string,object,object0,eEventSource,b);
            }
        };

        this.dbca = new IDatabaseConnectionAccess() {
            @Override
            public IDatabaseConnectionAccess.DatabaseType getDatabaseType() {
                return IDatabaseConnectionAccess.DatabaseType.ORACLE;
            }

            @Override
            public boolean needCloseConnection() {
                return false;
            }

            @Override
            public Connection getConnection() throws SQLException {
                return NewPCUnit.this.getDbConnection();
            }

        };
        this.trace.setFloodPeriod(EXCEPTION_STACK_TRACE_FLOOD_KEY_PREFIX, EXCEPTION_STACK_TRACE_PERIOD_MILLIS);
    }

    protected NewPCUnit(Instance instModel, Long id, String title, IDatabaseConnectionAccess dbca, IExtendedRadixTrace trace) {
        super(instModel, id, title);
        this.trace = trace;
        this.dbca = dbca;
        this.trace.setFloodPeriod(EXCEPTION_STACK_TRACE_FLOOD_KEY_PREFIX, EXCEPTION_STACK_TRACE_PERIOD_MILLIS);
    }

    public IExtendedRadixTrace getTraceInterface() {
        return trace;
    }

    public IHighLevelDbQuery getDBQuery() {
        if (this.queries == null) {
            this.queries = new NewDbQueries(dbca, this);
        }
        return queries;
    }

    public MessageStatistics getTicket() {
        return null;
    }

    public abstract <T> CommunicationAdapter<T> getCommunicationAdapter(CommunicationMode mode) throws DPCRecvException, DPCSendException;

    public abstract OptionsGroup optionsGroup(final Options options);

    public abstract boolean supportsTransmitting();

    public abstract boolean supportsReceiving();

    @Override
    public void onEvent(final Event ev) {
        if (supportsTransmitting() && needProcessing() && options.sendPeriod != null && System.currentTimeMillis() >= lastSendMillis + options.sendPeriod * 1000) {
            try {
                getTraceInterface().put(EEventSeverity.DEBUG, "Start processing send queue", PCMessages.W_MLS_ID_START_SEND, null, getEventSource(), false);
                getDBQuery().processExpiredMessages();
                final int processed = sendMessages();
                getTraceInterface().put(EEventSeverity.DEBUG, "Messages sent: " + processed, PCMessages.W_MLS_ID_STOP_SEND, new ArrStr(String.valueOf(processed)), getEventSource(), false);
            } catch (SQLException e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                getTraceInterface().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + exStack, PCMessages.W_MLS_ID_ERR_IN_DB_QRY, new ArrStr(getFullTitle(), exStack), getEventSource(), false);
                try {
                    restoreDbConnection();
                } catch (InterruptedException ie) {
                    return;
                }
            } catch (DPCSendException ex) {
                final String exceptionStack = ExceptionTextFormatter.exceptionStackToString(ex);
                getTraceInterface().put(EEventSeverity.ERROR, PCMessages.SENT_MESS_ERROR + ": " + exceptionStack, PCMessages.W_MLS_ID_SENT_ERROR, new ArrStr(exceptionStack), getEventSource(), false);
            } finally {
                lastSendMillis = System.currentTimeMillis();
            }
        }
        if (supportsReceiving() && needProcessing() && options.recvPeriod != null && System.currentTimeMillis() >= lastRecvMillis + options.recvPeriod * 1000) {
            try {
                getTraceInterface().put(EEventSeverity.DEBUG, "Start receiving messages", PCMessages.W_MLS_ID_START_RECV, null, getEventSource(), false);
                final int processed = recvMessages();
                getTraceInterface().put(EEventSeverity.DEBUG, "Messages received: " + processed, PCMessages.W_MLS_ID_STOP_RECV, new ArrStr(String.valueOf(processed)), getEventSource(), false);
            } catch (SQLException e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                getTraceInterface().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + exStack, PCMessages.W_MLS_ID_ERR_IN_DB_QRY, new ArrStr(getFullTitle(), exStack), getEventSource(), false);
                try {
                    restoreDbConnection();
                } catch (InterruptedException ie) {
                    return;
                }
            } catch (DPCRecvException e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                getTraceInterface().put(EEventSeverity.ERROR, "Receive message error: " + exStack, PCMessages.W_MLS_ID_RECV_ERROR, new ArrStr(exStack), getEventSource(), false);
            } finally {
                lastRecvMillis = System.currentTimeMillis();
            }
        }
        if (needProcessing()) {
            waitDispatcherEvent(System.currentTimeMillis() + TIC_MILLIS);
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

    protected IAASNotificationCallback createPersoCommClient() {
        return new PersoCommAasClient(this, manifest);

    }

    protected IAASNotificationCallback getPersoCommClient() {
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
                return NewPCUnit.this.getDbConnection();
            }

            @Override
            protected Arte getArte() {
                return null;
            }
        };

        try {
            if ((options = getDBQuery().getOptions()) != null) {
                checkOptions(options);
                final String optionsStr = optionsGroup(options).toString();
                getTraceInterface().put(EEventSeverity.EVENT, Messages.START_OPTIONS + optionsStr, PCMessages.W_MLS_ID_START_OPTIONS, new ArrStr(getFullTitle(), optionsStr), getEventSource(), false);
            } else {
                throw new RadixError(Messages.ERR_CANT_READ_OPTIONS);
            }
        } catch (RadixError ex) {
            throw ex;
        } catch (Exception ex) {
            getTraceInterface().put(EEventSeverity.ERROR, ExceptionTextFormatter.exceptionStackToString(ex), PCMessages.W_MLS_ID_START_OPTIONS, null, getEventSource(), false);
            return false;
        }

        waitDispatcherEvent(System.currentTimeMillis());

        try {
            getDBQuery().clearOutMessageFlagAll(Long.valueOf(getId()));
        } catch (SQLException ex) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(ex);
            getTraceInterface().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + exStack, PCMessages.W_MLS_ID_ERR_IN_DB_QRY, new ArrStr(getFullTitle(), exStack), getEventSource(), false);
        }
        aasCallback = createPersoCommClient();
        lastSendMillis = -1;
        lastRecvMillis = -1;
        return true;
    }

    protected void unprepareImpl() {
        queries.closeAll();
        getDBQuery().closeAll();
        if (getDispatcher() != null) {
            getDispatcher().unsubscribeFromTimerEvents(this);
        }

        if (aasCallback != null) {
            try {
                aasCallback.close();
            } catch (IOException exc) {
            }
            aasCallback = null;
        }

        processAllEventsSuppressingRuntimeExceptions();
        manifest = null;
    }

    protected int sendMessages() throws DPCSendException, SQLException {
        int count = 0;

        try {
            try (final CommunicationAdapter<Object> adapter = getCommunicationAdapter(ICommunicationUnit.CommunicationMode.TRANSMIT);) {
                for (MessageWithId item : getDBQuery().getMessages2Send(this.getId())) {
                    adapter.setStatistics(getTicket());
                    try {
                        final Object msg = adapter.prepareMessage(item.getId(), item.getMessage());
                        if (!adapter.sendMessage(item.getId(), msg)) {
                            count++;
                            break;
                        } else {
                            count++;
                        }
                    } catch (DPCRecoverableSendException exc) {
                        markMessageAsFailed(item.getId(), false, "Recoverable error while sending message: id=(%1$d), problem is %2$s", item.getId(), exc.getMessage());
                    } catch (DPCSendException exc) {
                        markMessageAsFailed(item.getId(), true, "Unrecoverable error while sending message: id=(%1$d), problem is %2$s", item.getId(), exc.getMessage());
                    }
                }
            }
        } catch (SQLException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DPCSendException(ex.getClass().getName() + ": " + ex.getMessage(), ex);
        }
        return count;
    }

    protected void markMessageAsFailed(final Long id, final boolean isUnrecoverable, final String message, final Object... parameters) throws SQLException {
        getDBQuery().markMessageAsFailed(id, isUnrecoverable, parameters == null || parameters.length == 0 ? message : String.format(message, parameters));
    }

    protected int recvMessages() throws DPCRecvException, SQLException {
        int count = 0;
        try (final CommunicationAdapter<Object> adapter = getCommunicationAdapter(ICommunicationUnit.CommunicationMode.RECEIVE);) {
            Object message;

            while ((message = adapter.receiveMessage()) != null) {
                final Long id = getDBQuery().saveReceived(adapter.unprepareMessage(message));

                getTraceInterface().put(EEventSeverity.EVENT, "Message received", PCMessages.W_MLS_ID_MESS_RECEVIED_AND_STORED, new ArrStr(String.valueOf(id)), getEventSource(), false);
                aasCallback.invokeAfterRecv(id);
                count++;
            }
        } catch (SQLException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DPCRecvException(ex.getClass().getName() + ": " + ex.getMessage(), ex);
        }
        return count;
    }

    protected void sendCallback(final Long id, final String error) {
        getTraceInterface().put(EEventSeverity.EVENT, "Message sent", PCMessages.W_MLS_ID_SENT, new ArrStr(String.valueOf(id)), getEventSource(), false);
        try {
            aasCallback.invokeAfterSend(id, error);
        } catch (Exception ex) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(ex);
            getTraceInterface().put(EEventSeverity.ERROR, "Exception on calling afterSend: " + ExceptionTextFormatter.throwableToString(ex), PCMessages.W_MLS_ID_MOVE_TO_ARHIVE_ERROR, new ArrStr(exStack), getEventSource(), false);
        }
        try {
            getDBQuery().save2Sent(id, error);
            getTraceInterface().put(EEventSeverity.EVENT, "Message saved to archive (id = " + String.valueOf(id) + ")", PCMessages.W_MLS_ID_SAVED_TO_ARCHIVE, new ArrStr(String.valueOf(id)), getEventSource(), false);
        } catch (SQLException e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            getTraceInterface().put(EEventSeverity.ERROR, "Save message to archive error: " + exStack, PCMessages.W_MLS_ID_MOVE_TO_ARHIVE_ERROR, new ArrStr(exStack), getEventSource(), false);
        }
    }

    protected void sendCallback(final Long id, final String error, final MessageStatistics stat) {
        sendCallback(id, error);
        if (stat != null) {
            try {
                getDBQuery().saveStatistics(id, stat);
                getTraceInterface().put(EEventSeverity.EVENT, "Message saved to archive (id = " + String.valueOf(id) + ")", PCMessages.W_MLS_STAT_SAVED_TO_ARCHIVE, new ArrStr(String.valueOf(id)), getEventSource(), false);
            } catch (SQLException e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                getTraceInterface().put(EEventSeverity.ERROR, "Save message statistics to archive error: " + exStack, PCMessages.W_MLS_STAT_SAVE_TO_ARHIVE_ERROR, new ArrStr(String.valueOf(id), exStack), getEventSource(), false);
            }
        }
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

    static final public class Options {

        public Long sendPeriod, recvPeriod, sendTimeout, routingPriority, minImportance, maxImportance;
        public String sendAddress, recvAddress;
        public String smppSystemId, smppSystemType, smppPassword, smppSourceAddress;
        public Long smppSessionType, smppSourceAddressTon, smppSourceAddressNpi, smppDestinationTon, smppDestinationNpi;
        public ESmppEncoding smppEncoding;
        public String pop3Address, emailLogin, emailPassword;
        public ESecureConnection emailSecureConnection;
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

        Options() {
        }
    }
}
