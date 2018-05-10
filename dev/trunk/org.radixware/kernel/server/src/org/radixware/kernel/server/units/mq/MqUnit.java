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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
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
import org.radixware.kernel.server.instance.ResourceRegistry;
import org.radixware.kernel.server.instance.SimpleResourceRegistryItem;
import org.radixware.kernel.common.enums.EDatabaseType;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.kernel.server.jdbc.RadixConnection;
import org.radixware.kernel.server.monitoring.AbstractMonitor;
import org.radixware.kernel.server.monitoring.MonitorFactory;
import org.radixware.kernel.server.sc.AasClientPool;
import org.radixware.kernel.server.units.AsyncEventHandlerUnit;
import org.radixware.kernel.server.units.Messages;
import org.radixware.kernel.server.units.UnitView;
import org.radixware.kernel.server.units.mq.interfaces.IMqUnitDbQueries;
import org.radixware.kernel.server.units.mq.interfaces.IMqQueueConsumer;
import org.radixware.kernel.server.jdbc.IDatabaseConnectionAccess;
import org.radixware.schemas.messagequeue.MqProcessRqDocument;
import org.radixware.schemas.messagequeue.MqProcessRs;

public class MqUnit extends AsyncEventHandlerUnit {

    protected static final String EXCEPTION_STACK_TRACE_FLOOD_KEY_PREFIX = "mq-ex-stack-";
    protected static final int EXCEPTION_STACK_TRACE_PERIOD_MILLIS = SystemPropUtils.getIntSystemProp("rdx.mq.ex.stack.trace.period.millis", 1000 * 60 * 60);
    public static final long PAUSE_AFTER_PROCESSING_EX_MILLIS = SystemPropUtils.getIntSystemProp("rdx.mq.pause.after.proc.fail.millis", 1000);
    public static final long PAUSE_AFTER_INVOKE_EX_MILLIS = SystemPropUtils.getIntSystemProp("rdx.mq.pause.after.invoke.fail.millis", 1000);
    private static final int TIC_MILLIS = 500;
    public static final String PARTITION_NONE = "none";

    private final IDatabaseConnectionAccess dbca;
    private final MqUnitView view = new MqUnitView(this);
    private MqAasClientPool aasClientPool;
    private final List<IMqQueueConsumer> consumers = new ArrayList<>();
    private boolean stopAfterCommitFailureRequested = false;

    private MqDbQueries.MqUnitOptions[] options;
    private IMqUnitDbQueries queries;
    private long curMessageSeq;

    public MqUnit(final Instance instance, final Long id, final String title) {
        super(instance, id, title, new MonitorFactory() {

            @Override
            public AbstractMonitor createMonitor(Object target) {
                return new MqUnitMonitor((MqUnit) target);
            }

        });

        this.dbca = new IDatabaseConnectionAccess() {
            @Override
            public EDatabaseType getDatabaseType() {
                return EDatabaseType.ORACLE;
            }

            @Override
            public Connection getConnection() throws SQLException {
                return MqUnit.this.getDbConnection();
            }

        };
        getTrace().setFloodPeriod(EXCEPTION_STACK_TRACE_FLOOD_KEY_PREFIX, EXCEPTION_STACK_TRACE_PERIOD_MILLIS);
    }

    @Override
    public MqUnitMonitor getMonitor() {
        return (MqUnitMonitor) super.getMonitor();
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
        curMessageSeq = 0;
        if (!super.startImpl()) {
            return false;
        } else {
            return prepareImpl();
        }
    }

    public long curMessageSeq() {
        return curMessageSeq;
    }

    public long nextMessageSeq() {
        return ++curMessageSeq;
    }

    @Override
    protected void stopImpl() {
        unprepareImpl();
        super.stopImpl();
    }

    public MqDbQueries.MqUnitOptions getOptions() {
        return options == null || options.length == 0 ? null : options[0];
    }

    public MqDbQueries.MqUnitOptions[] getOptionsArr() {
        return options;
    }

    protected void checkOptions(final MqDbQueries.MqUnitOptions[] options) throws Exception {
        if (options == null || options.length == 0) {
            getTrace().put(EEventSeverity.WARNING, "There are no linked queue processors", EEventSource.MQ_HANDLER_UNIT);
        }
    }

    @Override
    protected void setDbConnection(RadixConnection dbConnection) {
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
        final MqDbQueries.MqUnitOptions[] newOptions = getDBQuery().readOptions(getPrimaryUnitId());

        if (!Arrays.deepEquals(newOptions, getOptionsArr())) {
            boolean needRestart = false;
            if (newOptions != null && newOptions.length > 0 && getOptionsArr() != null && getOptionsArr().length > 0) {
                if (!Objects.equals(newOptions[0].brokerAddress, getOptionsArr()[0].brokerAddress)
                        || !Objects.equals(newOptions[0].queueName, getOptionsArr()[0].queueName)
                        || !Objects.equals(newOptions[0].consumerKey, getOptionsArr()[0].consumerKey)
                        || !Objects.equals(newOptions[0].partitionName, getOptionsArr()[0].partitionName)
                        || !Objects.equals(newOptions[0].processorId, getOptionsArr()[0].processorId)) {
                    needRestart = true;
                }
            }
            logOptionsChanged(Arrays.toString(options = newOptions));
            if (options.length > 0) {
                getAasClientPool().setOptions(new AasClientPool.AasClientPoolOptions(getOptionsArr()[0].parallelThreads));
            }
            if (needRestart) {
                requestStopAndPostponedRestart("important options changed", System.currentTimeMillis());
            }
        }
    }

    @Override
    public String getEventSource() {
        return EEventSource.MQ_HANDLER_UNIT.getValue();
    }

    boolean prepareImpl() {
        try {
            if ((options = getDBQuery().readOptions(getPrimaryUnitId())) != null) {
                checkOptions(options);
                for (MqDbQueries.MqUnitOptions singleOpts : options) {
                    getTrace().put(EEventSeverity.EVENT, Messages.START_OPTIONS + singleOpts.toString(), MqMessages.W_MLS_ID_START_OPTIONS.getMlsId(), new ArrStr(getFullTitle(), singleOpts.toString()), getEventSource(), false);
                }
            } else {
                throw new RadixError(Messages.ERR_CANT_READ_OPTIONS);
            }
        } catch (RadixError ex) {
            throw ex;
        } catch (Exception ex) {
            getTrace().put(EEventSeverity.ERROR, ExceptionTextFormatter.exceptionStackToString(ex), MqMessages.W_MLS_ID_START_OPTIONS.getMlsId(), null, getEventSource(), false);
            return false;
        }

        if (!ensureConsumers()) {
            return false;
        }

        aasClientPool = new MqAasClientPool(this, createTracer());
        int initAasSeances = 1;
        if (!consumers.isEmpty()) {
            initAasSeances = consumers.get(0).getOptions().parallelThreads;
        }
        final AasClientPool.AasClientPoolOptions aasClientPoolOpts = new AasClientPool.AasClientPoolOptions(initAasSeances);
        aasClientPool.setOptions(aasClientPoolOpts);

        subscribeTimerEvent();

        stopAfterCommitFailureRequested = false;

        return true;
    }

    protected boolean ensureConsumers() {
        for (MqDbQueries.MqUnitOptions item : getOptionsArr()) {
            boolean exists = false;
            for (IMqQueueConsumer consumer : consumers) {
                if (item.processorId == consumer.getOptions().processorId) {
                    exists = true;
                    break;
                }
            }
            if (exists) {
                continue;
            }
            getTrace().put(EEventSeverity.DEBUG, "Creating consumer for '" + item.queueId + ") " + item.queueTitle + "'", EEventSource.MQ_HANDLER_UNIT);
            try {
                IMqQueueConsumer newConsumer = null;
                switch (item.queueKind) {
                    case KAFKA:
                        newConsumer = new MqKafkaConsumer(this, item);
                        break;
                    case AMQP:
                        newConsumer = new MqAMQPConsumer(this, item);
                        break;
                    case JMS:
                        newConsumer = new MqJmsConsumer(this, item);
                        break;
                    case LOOPBACK:
                        throw new UnsupportedOperationException("Consumer for " + item.queueKind + " is not implemented yet");
                }
                if (newConsumer != null) {
                    getInstance().getResourceRegistry().register(new MqConsumerResource(newConsumer, getResourceKeyPrefix() + ResourceRegistry.SEPARATOR + newConsumer.debugKey(), null, getThisRunAliveChecker()));
                    consumers.add(newConsumer);
                }
                final String queueTitle = item.queueId + ") " + item.queueTitle;
                getTrace().put(EEventSeverity.EVENT, String.format(MqMessages.CONNECTED_TO_QUEUE, queueTitle), MqMessages.W_MLS_ID_CONNECTED_TO_QUEUE.getMlsId(), new ArrStr(queueTitle), EEventSource.MQ_HANDLER_UNIT.getValue(), false);
            } catch (IOException ex) {
                final String floodKey = "MqConsumerCreate/" + item.processorId;
                getTrace().setFloodPeriod(floodKey, 60 * 1000);
                getTrace().putFloodControlled(floodKey, EEventSeverity.ERROR, "Error creating consumer: " + ExceptionTextFormatter.exceptionStackToString(ex), null, null, getEventSource(), -1, false, null);
                return false;
            }
        }
        return true;
    }

    void logMessageReceived(final MqMessage message) {
        if (getTrace().getMinSeverity() <= EEventSeverity.DEBUG.getValue()) {
            getTrace().put(EEventSeverity.DEBUG, "Received message '" + message.getDebugKey() + "', effective partition: " + message.getPartitionId(), EEventSource.MQ_HANDLER_UNIT);
            getTrace().put(EEventSeverity.DEBUG, "Message body (bytes): " + Hex.encode(message.getBodyBytes()), null, null, EEventSource.MQ_HANDLER_UNIT.getValue(), true);
            if (message.hasBodyStr()) {
                getTrace().put(EEventSeverity.DEBUG, "Message body (String): " + message.getBodyStr(), null, null, EEventSource.MQ_HANDLER_UNIT.getValue(), true);
            }
        }
    }

    protected void subscribeTimerEvent() {
        getDispatcher().waitEvent(new EventDispatcher.TimerEvent(), this, System.currentTimeMillis() + TIC_MILLIS);
    }
    
    public void subscribeImmediateTimerEvent() {
        getDispatcher().unsubscribeFromTimerEvents(this);
        getDispatcher().waitEvent(new EventDispatcher.TimerEvent(), this, System.currentTimeMillis());
    }

    public MqAasClientPool getAasClientPool() {
        return aasClientPool;
    }

    public void onResponse(MqMessage message, MqProcessRs xProcessRs, long durationMillis) {
        logGotResponse(message);
        message.getConsumer().onResponse(message, xProcessRs);
        getMonitor().onProcessFinished(durationMillis);
    }

    public void onFailToSendAasRequest(MqMessage message, Exception ex) {
        logSendAASException(message, ex);
        message.getConsumer().onFailToSendAasRequest(message, ex);
    }

    public void onAasException(MqMessage message, Exception ex, long durationMillis) {
        logAASException(message, ex);
        message.getConsumer().onAasException(message, ex);
        getMonitor().onProcessFinished(durationMillis);
    }

    public void onCommitFailure(MqMessage mq, Exception ex) {
        logCommitFailure(mq, ex);
        if (!stopAfterCommitFailureRequested) {
            requestStopAndPostponedRestart("commit failure", System.currentTimeMillis() + 5000);
            stopAfterCommitFailureRequested = true;
        }
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

    @Override
    protected String getIdForSingletonLock() {
        return "MqUnit#" + getPrimaryUnitId();
    }

    @Override
    public boolean isSingletonByPrimary() {
        return true;
    }

    @Override
    public boolean isSingletonUnit() {
        return true;
    }

    @Override
    public void onEvent(Event ev) {
        if (ev instanceof EventDispatcher.TimerEvent) {
            ensureConsumers();
            final List<IMqQueueConsumer> toClose = new ArrayList<>();
            for (IMqQueueConsumer item : consumers) {
                try {
                    item.poll();
                } catch (Exception ex) {
                    getTrace().put(EEventSeverity.ERROR, "Error while polling " + item.debugKey() + ": " + ExceptionTextFormatter.throwableToString(ex), EEventSource.MQ_HANDLER_UNIT);
                    toClose.add(item);
                }
            }

            if (!isShuttingDown()) {
                for (IMqQueueConsumer consumer : toClose) {
                    try {
                        consumer.close();
                    } catch (Exception ex) {
                        getTrace().put(EEventSeverity.ERROR, "Error while closing cosumer: " + ExceptionTextFormatter.throwableToString(ex), EEventSource.MQ_HANDLER_UNIT);
                    }
                }
                consumers.removeAll(toClose);
                subscribeTimerEvent();
            }
        }
    }

    void logGotResponse(final MqMessage msg) {
        getTrace().put(EEventSeverity.DEBUG, "Got response for " + msg.getDebugKey(), EEventSource.MQ_HANDLER_UNIT);
    }

    void logCommit(final MqMessage msg) {
        getTrace().put(EEventSeverity.DEBUG, "Committed " + msg.getDebugKey(), EEventSource.MQ_HANDLER_UNIT);
    }

    void logCommitFailure(final MqMessage msg, final Exception exc) {
        getTrace().put(EEventSeverity.ERROR, "Commit failure while processing " + msg.getDebugKey() + " : " + ExceptionTextFormatter.throwableToString(exc), EEventSource.MQ_HANDLER_UNIT);
    }

    void logAASException(final MqMessage msg, final Exception exc) {
        getTrace().put(EEventSeverity.ERROR, "AAS fault while processing " + msg.getDebugKey() + " : " + ExceptionTextFormatter.throwableToString(exc), EEventSource.MQ_HANDLER_UNIT);
    }

    void logSendAASException(final MqMessage msg, final Exception exc) {
        getTrace().put(EEventSeverity.ERROR, "Failed to send aas request for " + msg.getDebugKey() + ": " + ExceptionTextFormatter.throwableToString(exc), EEventSource.MQ_HANDLER_UNIT);
    }

    protected void invoke(final MqMessage msg, final MqProcessRqDocument xProcessRqDoc, final long processorId) {
        getMonitor().onProcessStart();
        getTrace().put(EEventSeverity.DEBUG, "Invoking " + msg.getDebugKey(), EEventSource.MQ_HANDLER_UNIT);
        getAasClientPool().invoke(msg, xProcessRqDoc, processorId, getOptionsArr()[0].aasCallTimeoutSec * 1000, getScpName());
    }

    private static class MqConsumerResource extends SimpleResourceRegistryItem {

        private final IMqQueueConsumer consumer;

        public MqConsumerResource(IMqQueueConsumer consumer, String key, String description, Callable<Boolean> holderAliveChecker) {
            super(key, consumer, description, holderAliveChecker);
            this.consumer = consumer;
        }

        @Override
        public boolean isClosed() {
            return consumer.isClosed();
        }

        @Override
        public Object getTarget() {
            return consumer;
        }

    }
}
