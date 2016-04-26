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
package org.radixware.kernel.server.units.mq;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.server.aio.Event;
import org.radixware.kernel.server.aio.EventDispatcher;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.sc.AasClientPool;
import org.radixware.kernel.server.trace.ServerTrace;
import org.radixware.kernel.server.units.AsyncEventHandlerUnit;
import org.radixware.kernel.server.units.Messages;
import org.radixware.kernel.server.units.UnitDescription;
import org.radixware.kernel.server.units.UnitView;
import org.radixware.kernel.server.units.mq.interfaces.IMqUnitDbQueries;
import org.radixware.kernel.server.units.mq.interfaces.IMqQueueConsumer;
import org.radixware.kernel.server.units.persocomm.interfaces.IDatabaseConnectionAccess;
import org.radixware.kernel.server.units.persocomm.interfaces.IExtendedRadixTrace;
import org.radixware.kernel.server.units.persocomm.tools.MultiLangStringWrapper;
import org.radixware.schemas.messagequeue.MqProcessRqDocument;
import org.radixware.schemas.messagequeue.MqProcessRs;

public class MqUnit extends AsyncEventHandlerUnit {

    protected static final String EXCEPTION_STACK_TRACE_FLOOD_KEY_PREFIX = "mq-ex-stack-";
    protected static final int EXCEPTION_STACK_TRACE_PERIOD_MILLIS = SystemPropUtils.getIntSystemProp("rdx.mq.ex.stack.trace.period.millis", 1000 * 60 * 60);
    private static final int TIC_MILLIS = 500;

    private final IDatabaseConnectionAccess dbca;
    private final IExtendedRadixTrace trace;
    private final MqUnitView view = new MqUnitView(this);
    private MqAasClientPool aasClientPool;
    private final List<IMqQueueConsumer> consumers = new ArrayList<>();

    private MqDbQueries.MqUnitOptions[] options;
    private IMqUnitDbQueries queries;
    private long mainMqId;

    public MqUnit(final Instance instance, final Long id, final String title) {
        super(instance, id, title);

        this.trace = new IExtendedRadixTrace() {
            @Override
            public void setFloodPeriod(String floodKey, long periodMillis) {
                getTrace().setFloodPeriod(floodKey, periodMillis);
            }

            @Override
            public void put(EEventSeverity severity, String localizedMessage, MultiLangStringWrapper mlsId, List<String> mlsArgs, String source, boolean isSensetive) {
                getTrace().put(severity, localizedMessage, mlsId.getMlsId(), mlsArgs, source, isSensetive);
            }

            @Override
            public void putFloodControlled(String floodKey, EEventSeverity severity, String localizedMessage, MultiLangStringWrapper mlsId, List<String> mlsArgs, String source, long millisOrMinusOne, boolean isSensitive, Collection<ServerTrace.ETraceDestination> targetDestinations) {
                getTrace().putFloodControlled(floodKey, severity, localizedMessage, mlsId.getMlsId(), mlsArgs, source, millisOrMinusOne, isSensitive, targetDestinations);
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
                return MqUnit.this.getDbConnection();
            }

        };
        this.trace.setFloodPeriod(EXCEPTION_STACK_TRACE_FLOOD_KEY_PREFIX, EXCEPTION_STACK_TRACE_PERIOD_MILLIS);
    }

    protected MqUnit(Instance instModel, Long id, String title, IDatabaseConnectionAccess dbca, IExtendedRadixTrace trace) {
        super(instModel, id, title);
        this.trace = trace;
        this.dbca = dbca;
        this.trace.setFloodPeriod(EXCEPTION_STACK_TRACE_FLOOD_KEY_PREFIX, EXCEPTION_STACK_TRACE_PERIOD_MILLIS);
        aasClientPool = new MqAasClientPool(this, createTracer());
    }

    public IExtendedRadixTrace getTraceInterface() {
        return trace;
    }

    public IMqUnitDbQueries getDBQuery() {
        if (this.queries == null) {
            this.queries = new MqDbQueries(dbca);
        }
        return queries;
    }

    @Override
    protected void maintenanceImpl() throws InterruptedException {
        super.maintenanceImpl();
        for (IMqQueueConsumer consumer : consumers) {
            consumer.maintenance();
        }
        aasClientPool.tryInvoke();
        aasClientPool.maintenance();
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

    public MqDbQueries.MqUnitOptions[] getOptions() {
        return options;
    }

    protected void checkOptions(final MqDbQueries.MqUnitOptions[] options) throws Exception {
        if (options == null || options.length == 0) {
            getTraceInterface().put(EEventSeverity.WARNING, "There are no linked queue processors", EEventSource.MQ_HANDLER_UNIT);
        }
    }

    @Override
    protected void setDbConnection(Connection dbConnection) {
        getDBQuery().closeAll();
        super.setDbConnection(dbConnection);
    }

    @Override
    public String getScpName() {
        if (options == null || options.length == 0) {
            return getInstance().getScpName();
        }
        return options[0].scpName;
    }

    @Override
    protected void rereadOptionsImpl() throws SQLException, InterruptedException {
        final MqDbQueries.MqUnitOptions[] newOptions = getDBQuery().readOptions(mainMqId);

        if (!Arrays.deepEquals(newOptions, getOptions())) {
            logOptionsChanged(Arrays.toString(options = newOptions));
            if (options.length > 0) {
                getAasClientPool().setOptions(new AasClientPool.AasClientPoolOptions(getOptions()[0].parallelThreads));
            }
        }
    }

    @Override
    public String getEventSource() {
        return EEventSource.MQ_HANDLER_UNIT.getValue();
    }
    
    boolean prepareImpl() {
        try {
            if ((options = getDBQuery().readOptions(mainMqId)) != null) {
                checkOptions(options);
                for (MqDbQueries.MqUnitOptions singleOpts : options) {
                    getTraceInterface().put(EEventSeverity.EVENT, Messages.START_OPTIONS + singleOpts.toString(), MqMessages.W_MLS_ID_START_OPTIONS, new ArrStr(getFullTitle(), singleOpts.toString()), getEventSource(), false);
                }
            } else {
                throw new RadixError(Messages.ERR_CANT_READ_OPTIONS);
            }
        } catch (RadixError ex) {
            throw ex;
        } catch (Exception ex) {
            getTraceInterface().put(EEventSeverity.ERROR, ExceptionTextFormatter.exceptionStackToString(ex), MqMessages.W_MLS_ID_START_OPTIONS, null, getEventSource(), false);
            return false;
        }

        for (MqDbQueries.MqUnitOptions item : getOptions()) {
            try {
                switch (item.queueKind) {
                    case KAFKA:
                        consumers.add(new MqKafkaConsumer(this, item));
                        break;
                    case AMQP:
                        consumers.add(new MqAMQPConsumer(this, item));
                        break;
                    case JMS:
                    case LOOPBACK:
                        throw new UnsupportedOperationException("Consumer for " + item.queueKind + " is not implemented yet");
                }
            } catch (IOException ex) {
                getTraceInterface().put(EEventSeverity.ERROR, ExceptionTextFormatter.exceptionStackToString(ex), MqMessages.W_MLS_ID_START_OPTIONS, null, getEventSource(), false);
                return false;
            }
        }

        aasClientPool = new MqAasClientPool(this, createTracer());
        final AasClientPool.AasClientPoolOptions aasClientPoolOpts = new AasClientPool.AasClientPoolOptions(5);
        aasClientPool.setOptions(aasClientPoolOpts);

        subscribeTimerEvent();

        return true;
    }

    protected void subscribeTimerEvent() {
        getDispatcher().waitEvent(new EventDispatcher.TimerEvent(), this, System.currentTimeMillis() + TIC_MILLIS);
    }

    public MqAasClientPool getAasClientPool() {
        return aasClientPool;
    }

    void onResponse(MqMessage message, MqProcessRs xProcessRs) {
        message.getConsumer().onResponse(message, xProcessRs);
    }

    void onFailToSendAasRequest(MqMessage message, Exception ex) {
        message.getConsumer().onFailToSendAasRequest(message, ex);
    }

    void onAasException(MqMessage message, Exception ex) {
        message.getConsumer().onAasException(message, ex);
    }

    @Override
    protected boolean requestShutdownImpl() {
        boolean result = super.requestShutdownImpl();
        if (result) {
            for (IMqQueueConsumer consumer : consumers) {
                consumer.requestStopAsync();
            }
            return true;
        } else {
            return result;
        }
    }

    void unprepareImpl() {
        for (IMqQueueConsumer item : consumers) {
            try {
                item.close();
            } catch (Exception ex) {
                logErrorOnStop(ex);
            }
        }
        consumers.clear();

        try {
            if (aasClientPool != null) {
                aasClientPool.closeAll();
            }
        } catch (Exception ex) {
            logErrorOnStop(ex);
        } finally {
            aasClientPool = null;
        }
    }

    @Override
    public String getUnitTypeTitle() {
        return MqMessages.UNIT_TYPE_TITLE;
    }

    @Override
    public Long getUnitType() {
        return EUnitType.MQ_HANDLER.getValue();
    }

    @Override
    protected UnitView newUnitView() {
        return view;
    }

    //
    //      Singleton unit support.
    //    
    @Override
    protected boolean prepareForStart() throws InterruptedException {
        mainMqId = getInstance().getMainMqUnitId(getId());
        return mainMqId > 0;
    }

    @Override
    protected String getIdForSingletonLock() {
        return "MqUnit#" + mainMqId;
    }

    @Override
    protected UnitDescription getStartedDuplicatedUnitDescription() throws SQLException {
        return getInstance().getStartedDuplicatedMqUnit(this);
    }

    @Override
    public boolean isSingletonUnit() {
        return true;
    }

    public long getParentId() {
        return mainMqId;
    }

    @Override
    public void onEvent(Event ev) {
        if (ev instanceof EventDispatcher.TimerEvent) {
            for (IMqQueueConsumer item : consumers) {
                try {
                    item.poll();
                } catch (Exception ex) {
                    getTraceInterface().put(EEventSeverity.ERROR, "Error while polling " + item.debugKey() + ": " + ExceptionTextFormatter.throwableToString(ex), EEventSource.MQ_HANDLER_UNIT);
                }
            }

            if (!isShuttingDown()) {
                subscribeTimerEvent();
            }
        }
    }

    void aboutGotResponse(final MqMessage msg) {
        getTraceInterface().put(EEventSeverity.DEBUG, "Got response for " + msg.getDebugKey(), EEventSource.MQ_HANDLER_UNIT);
    }

    void aboutCommit(final MqMessage msg) {
        getTraceInterface().put(EEventSeverity.DEBUG, "Committed " + msg.getDebugKey(), EEventSource.MQ_HANDLER_UNIT);
    }

    void aboutCommitFailure(final MqMessage msg, final Exception exc) {
        getTraceInterface().put(EEventSeverity.ERROR, "Commit failure while processing " + msg.getDebugKey() + " : " + ExceptionTextFormatter.throwableToString(exc), EEventSource.MQ_HANDLER_UNIT);
    }

    void aboutAASException(final MqMessage msg, final Exception exc) {
        getTraceInterface().put(EEventSeverity.ERROR, "AAS fault while processing " + msg.getDebugKey() + " : " + ExceptionTextFormatter.throwableToString(exc), EEventSource.MQ_HANDLER_UNIT);
    }

    void aboutSendAASException(final MqMessage msg, final Exception exc) {
        getTraceInterface().put(EEventSeverity.ERROR, "Failed to send aas request for " + msg.getDebugKey() + ": " + ExceptionTextFormatter.throwableToString(exc), EEventSource.MQ_HANDLER_UNIT);
    }

    protected void invoke(final MqMessage msg, final MqProcessRqDocument xProcessRqDoc, final long processorId) {
        getTraceInterface().put(EEventSeverity.DEBUG, "Invoking " + msg.getDebugKey(), EEventSource.MQ_HANDLER_UNIT);
        getAasClientPool().invoke(msg, xProcessRqDoc, processorId, getOptions()[0].aasCallTimeoutSec * 1000, getScpName());
    }
}
