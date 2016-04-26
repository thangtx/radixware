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
package org.radixware.kernel.server.units.persocomm;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.ESmppEncoding;
import org.radixware.kernel.common.exceptions.RadixError;
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
import org.radixware.kernel.server.units.AsyncEventHandlerUnit;
import org.radixware.kernel.server.units.Messages;
import org.radixware.kernel.server.units.UnitView;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.schemas.personalcommunications.MessageDocument;
import org.radixware.schemas.personalcommunications.MessageStatistics;

public abstract class PCUnit extends AsyncEventHandlerUnit implements EventHandler {

    private static final long TIC_MILLIS = 1000;
    private static final int EXCEPTION_STACK_TRACE_PERIOD_MILLIS = SystemPropUtils.getIntSystemProp("rdx.pcunit.ex.stack.trace.period.millis", 1000 * 60 * 60);
    public static final long MAX_SYNC_DURATION_MILLIS = SystemPropUtils.getIntSystemProp("rdx.pcunit.max.sync.op.duration.millis", 5000);
    private static final String EXCEPTION_STACK_TRACE_FLOOD_KEY_PREFIX = "pc-ex-stack-";
    //
    public Options options;
    protected final DbQueries queries;
    protected final Connection conn;
    protected MessageStatistics ticket;
    private ServiceManifestServerLoader manifestLoader;
    protected PersoCommAasClient aasClient = null;
    private long lastSendMillis = -1;
    private long lastRecvMillis = -1;

    static final public class Options {

        public Long sendPeriod, recvPeriod, sendTimeout, routingPriority, minImportance, maxImportance;
        public String sendAddress, recvAddress;
        public String smppSystemId, smppSystemType, smppPassword, smppSourceAddress;
        public Long smppSourceAddressTon, smppSourceAddressNpi, smppDestinationTon, smppDestinationNpi;
        public ESmppEncoding smppEncoding;
        public String pop3Address, emailLogin, emailPassword;
        public String addressTemplate, subjectTemplate;
        public String encoding, fileFormat, kind;
        public String twitterConsumerToken, twitterConsumerSecret;
        public String twitterAccessToken, twitterAccessSecret;
        public String gcmApiKey;
        public String apnsKeyAlias;
        public String addressMask;
        public int apnsMaxParallelSendCount = 50;
        public int apnsSendSuccessfulAfterMillis = 5000;

        Options() {
        }
    }

    protected PCUnit(final Instance instance, final Long id, final String title) {
        super(instance, id, title);
        this.conn = null;
        queries = new DbQueries(this);
        getTrace().setFloodPeriod(EXCEPTION_STACK_TRACE_FLOOD_KEY_PREFIX, EXCEPTION_STACK_TRACE_PERIOD_MILLIS);
    }

    @Override
    public Connection getDbConnection() {
        return conn != null ? conn : super.getDbConnection();
    }

    protected void moveMessToArchive(final Long id) {
        try {
            queries.saveOutMessage(id, null);
            getTrace().put(EEventSeverity.EVENT, "Message saved to archive (id = " + String.valueOf(id) + ")", PCMessages.MLS_ID_SAVED_TO_ARCHIVE, new ArrStr(String.valueOf(id)), getEventSource(), false);
        } catch (SQLException e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            getTrace().put(EEventSeverity.ERROR, "Save message to archive error: " + exStack, PCMessages.MLS_ID_MOVE_TO_ARHIVE_ERROR, new ArrStr(exStack), getEventSource(), false);
        }
    }

    @Override
    protected UnitView newUnitView() {
        return new UnitView(this);
    }

    @Override
    protected boolean startImpl() throws Exception {
        if (!super.startImpl()) {
            return false;
        }
        manifestLoader = new ServiceManifestServerLoader() {
            @Override
            protected Connection getDbConnection() {
                return PCUnit.this.getDbConnection();
            }

            @Override
            protected Arte getArte() {
                return null;
            }
        };
        options = queries.readOptions();
        if (options == null) {
            throw new RadixError(Messages.ERR_CANT_READ_OPTIONS);
        }
        try {
            checkOptions();
        } catch (Exception ex) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(ex);
            getTrace().put(EEventSeverity.ERROR, exStack, null, null, getEventSource(), false);
            return false;
        }
        final String optionsStr = optionsToString();
        getTrace().put(EEventSeverity.EVENT, Messages.START_OPTIONS + optionsStr, Messages.MLS_ID_START_OPTIONS, new ArrStr(getFullTitle(), optionsStr), getEventSource(), false);
        getDispatcher().waitEvent(new EventDispatcher.TimerEvent(), this, System.currentTimeMillis());
        try {
            queries.clearOutMessageFlagAll(Long.valueOf(getId()));
        } catch (SQLException ex) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(ex);
            getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(getFullTitle(), exStack), getEventSource(), false);
        }
        aasClient = new PersoCommAasClient(this, manifestLoader);
        lastSendMillis = -1;
        lastRecvMillis = -1;
        return true;
    }

    protected void checkOptions() throws Exception {
    }

    protected boolean getIsEnableRecv() {
        return true;
    }

    protected File getTempDir() {
        final File dir = RadixLoader.getInstance().createTempFileWithExactName(getTempDirName());
        dir.mkdir();
        return dir;
    }

    private String getTempDirName() {
        return "pcunit_" + getId() + "_tmp";
    }

    protected void triggerSend() {
        lastSendMillis = 0;
        ensureTimerEventNow();
    }

    protected void triggerReceive() {
        lastRecvMillis = 0;
        ensureTimerEventNow();
    }

    @Override
    public void onEvent(final Event ev) {
        if (!isShuttingDown() && options.sendPeriod != null && System.currentTimeMillis() >= lastSendMillis + options.sendPeriod * 1000) {
            lastSendMillis = System.currentTimeMillis();
            try {
                getTrace().put(EEventSeverity.DEBUG, "Start processing send queue", PCMessages.MLS_ID_START_SEND, null, getEventSource(), false);
                queries.processExpiredMessages();
                sendMessages();
                getTrace().put(EEventSeverity.DEBUG, "Send queue processing finished", PCMessages.MLS_ID_STOP_SEND, new ArrStr("0"), getEventSource(), false);
            } catch (SQLException e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(getFullTitle(), exStack), getEventSource(), false);
                try {
                    restoreDbConnection();
                } catch (InterruptedException ie) {
                    return;
                }
            } catch (DPCSendException ex) {
                final String exceptionStack = ExceptionTextFormatter.exceptionStackToString(ex);
                getTrace().put(EEventSeverity.ERROR, PCMessages.SENT_MESS_ERROR + ": " + exceptionStack, PCMessages.SENT_MESS_ERROR, new ArrStr(exceptionStack), getEventSource(), false);
            }
        }
        if (!isShuttingDown() && options.recvPeriod != null && System.currentTimeMillis() >= lastRecvMillis + options.recvPeriod * 1000) {
            lastRecvMillis = System.currentTimeMillis();
            try {
                getTrace().put(EEventSeverity.DEBUG, "Start receiving messages", PCMessages.MLS_ID_START_RECV, null, getEventSource(), false);
                recvMessages();
                getTrace().put(EEventSeverity.DEBUG, "Receiving messages finished", PCMessages.MLS_ID_STOP_RECV, new ArrStr("0"), getEventSource(), false);
            } catch (DPCRecvException e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                getTrace().put(EEventSeverity.ERROR, "Receive message error: " + exStack, PCMessages.MLS_ID_RECV_ERROR, new ArrStr(exStack), getEventSource(), false);
            }
        }
        if (!isShuttingDown()) {
            getDispatcher().waitEvent(new EventDispatcher.TimerEvent(), this, System.currentTimeMillis() + TIC_MILLIS);
        }
    }

    @Override
    protected void stopImpl() {
        queries.closeAll();
        if (getDispatcher() != null) {
            getDispatcher().unsubscribeFromTimerEvents(this);
        }

        if (aasClient != null) {
            aasClient.close();
            aasClient = null;
        }

        processAllEventsSuppressingRuntimeExceptions();

        super.stopImpl();
        manifestLoader = null;
    }

    public Long recvMessage(final MessageDocument m) {
        getTrace().put(EEventSeverity.EVENT, "Message received", PCMessages.MLS_ID_MESS_RECEIVED, null, getEventSource(), false);
        Long id = 0L;
        try {
            id = queries.saveMessage(m);
            getTrace().put(EEventSeverity.EVENT, "Message moved to archive", PCMessages.MLS_ID_SAVED_TO_ARCHIVE, new ArrStr(String.valueOf(id)), getEventSource(), false);
        } catch (SQLException e) {
            final String exStack = ExceptionTextFormatter.getExceptionMess(e);//ExceptionTextFormatter.exceptionStackToString(e);

            getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(getFullTitle(), exStack), getEventSource(), false);
            try {
                restoreDbConnection();
            } catch (InterruptedException e1) {
                return 0L;
            }
            getTrace().put(EEventSeverity.ERROR, "Save message to archive error: " + exStack, PCMessages.MLS_ID_MOVE_TO_ARHIVE_ERROR, null, getEventSource(), false);
            return id;
        }
        try {
            aasClient.invokeAfterRecv(id);
        } catch (Exception ex) {
            getTrace().put(EEventSeverity.ERROR, "Exception on calling receive handler: " + ExceptionTextFormatter.throwableToString(ex), null, null, getEventSource(), false);
        }
        return id;
    }

    private String getExceptionStackFloodKey(final String stack) {
        return EXCEPTION_STACK_TRACE_FLOOD_KEY_PREFIX + Objects.hashCode(stack);
    }

    public void sendMessage(final MessageDocument m, final Long id) throws SQLException {
        getTrace().put(EEventSeverity.EVENT, "Message from send queue loaded (id = " + String.valueOf(id) + ")", PCMessages.MLS_ID_LOADED_FROM_SENT_QUEUE, new ArrStr(String.valueOf(id)), getEventSource(), false);
        try {
            if ((ticket = getMessageTicket()) != null) {
                send(m, id, ticket);
            } else {
                send(m, id);
            }
        } catch (DPCSendException e) {
            processSendError(id, m, e);
        }
    }

    protected void processSendError(final long id, final MessageDocument xMessageDoc, final DPCSendException e) throws SQLException {
        queries.clearOutMessageFlag(id);
        try {
            final Long newUnitId = queries.forwardMessage(xMessageDoc, id); // try to forward message to another unit
            if (newUnitId != null) {
                final String exMessage = ExceptionTextFormatter.getExceptionMess(e);//ExceptionTextFormatter.exceptionStackToString(e); TWRBS-6105
                getTrace().put(EEventSeverity.ERROR, "Sending error, message was forwarded to unit #" + newUnitId + ": " + exMessage, PCMessages.MLS_ID_SENT_ERROR, new ArrStr(String.valueOf(exMessage)), getEventSource(), false);
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                getTrace().putFloodControlled(getExceptionStackFloodKey(exStack), EEventSeverity.ERROR, "Exception stack: " + exStack, null, null, getEventSource(), -1, false, null);
                return;
            }
        } catch (SQLException ex) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(ex);
            getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(getFullTitle(), exStack), getEventSource(), false);
        }
        final String exMess = ExceptionTextFormatter.getExceptionMess(e);//ExceptionTextFormatter.exceptionStackToString(e);
        getTrace().put(EEventSeverity.ERROR, "Sending error (error message: '" + exMess + "')", PCMessages.MLS_ID_SENT_ERROR, new ArrStr(String.valueOf(exMess)), getEventSource(), false);

        final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
        getTrace().putFloodControlled(getExceptionStackFloodKey(exStack), EEventSeverity.ERROR, "Exception stack: " + exStack, null, null, getEventSource(), -1, false, null);
    }

    public void sendCallback(final Long id, final String error) {
        getTrace().put(EEventSeverity.EVENT, "Message sent", PCMessages.MLS_ID_SENT, new ArrStr(String.valueOf(id)), getEventSource(), false);
        try {
            aasClient.invokeAfterSend(id, error);
        } catch (Exception ex) {
            getTrace().put(EEventSeverity.ERROR, "Exception on calling afterSend: " + ExceptionTextFormatter.throwableToString(ex), null, null, getEventSource(), false);
        }
        moveMessToArchive(id);
    }

    @Override
    protected void setDbConnection(final Connection dbConnection) {
        queries.closeAll();
        super.setDbConnection(dbConnection);
        if (dbConnection != null) {
            try {
                queries.clearOutMessageFlagAll(Long.valueOf(getId()));
            } catch (SQLException ex) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(ex);
                getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(getFullTitle(), exStack), getEventSource(), false);
            }
        }
    }

    protected void sendMessages() throws DPCSendException {
        try {
            queries.readAndSendMessages();
        } catch (SQLException e) {
            throw new DPCSendException(e.getMessage(), e);
        }
    }

    public boolean canSend() {
        return true;
    }

    public abstract String optionsToString();

    protected MessageStatistics getMessageTicket() {
        return null;
    }

    protected void send(MessageDocument m, Long id, MessageStatistics ticket) throws DPCSendException {
        send(m, id);
    }

    protected abstract void send(MessageDocument m, Long id) throws DPCSendException;

    protected abstract void recvMessages() throws DPCRecvException;
}
