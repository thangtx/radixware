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

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringEscapeUtils;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EMqProcOrder;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.server.units.mq.MqAMQPConsumer.AMQPMqMessage;
import org.radixware.kernel.server.units.mq.interfaces.IMqQueueConsumer;
import org.radixware.schemas.messagequeue.MqMessageMeta;
import org.radixware.schemas.messagequeue.MqProcessRqDocument;
import org.radixware.schemas.messagequeue.MqProcessRs;
import org.radixware.schemas.types.MapStrStr;

public class MqAMQPConsumer implements IMqQueueConsumer<AMQPMqMessage, String> {

    public static final int DEFAULT_BROKER_PORT = 5672;
    public static final int DEFAULT_POLL_TIMEOUT_MILLIS = SystemPropUtils.getIntSystemProp("rdx.mq.amqp.default.poll.timeout.millis", 100);
    private static final String PARTITION_SOURCE_HEADER_PREFIX = "header";
    private static final String PARTITION_SOURCE_TYPE_SEPARATOR = "header";

    private final MqUnit unit;
    private final MqDbQueries.MqUnitOptions options;
    private final MqMessageRepo<AMQPMqMessage, String> mr;
    private final Connection connection;
    private final Channel channel;
    private final QueueingConsumer consumer;
    private final long processorId;
    private final String debugKey;

    private final CountDownLatch shutdownSignalCaughtLatch = new CountDownLatch(1);
    private volatile boolean canSelectMessages = true;

    public MqAMQPConsumer(final MqUnit unit, final MqDbQueries.MqUnitOptions options) throws IOException {
        this.unit = unit;
        this.options = options;
        this.processorId = options.processorId;

        final ConnectionFactory factory = new ConnectionFactory();
        final String[] broker = options.brokerAddress.split(":");

        factory.setHost(broker[0]);
        factory.setPort(broker.length > 1 ? Integer.valueOf(broker[1]) : DEFAULT_BROKER_PORT);

        if (options.login != null && !options.login.isEmpty()) {
            factory.setUsername(options.login);
            if (options.password != null && !options.password.isEmpty()) {
                factory.setPassword(options.password);
            }
        }
        
        try {
            if (options.prefetchCount > 0) {
                mr = new MqMessageRepo(this, unit, options.prefetchCount);
            } else {
                mr = new MqMessageRepo(this, unit);
            }
            connection = factory.newConnection();
            try {
                channel = connection.createChannel();
                consumer = new QueueingConsumer(channel);
                if (mr.getCachedMessagesHighwater() > 0) {
                    channel.basicQos(mr.getCachedMessagesHighwater());
                }
                channel.basicConsume(options.queueName, false, consumer);
            } catch (Exception ex) {
                try {
                    connection.abort();
                } catch (Exception ex1) {
                    unit.getTrace().debug("Exception while aborting connection: " + ExceptionTextFormatter.throwableToString(ex1), EEventSource.MQ_HANDLER_UNIT, false);
                }
                throw ex;
            }
        } catch (TimeoutException ex) {
            throw new IOException(ex);
        }
        debugKey = "AMQP '" + options.queueId + ") " + options.queueTitle + "'";
    }

    @Override
    public MqDbQueries.MqUnitOptions getOptions() {
        return options;
    }

    @Override
    public void close() throws IOException {
        try {//channel.abort();
            channel.close();
            //connection.abort();
            connection.close();
        } catch (TimeoutException ex) {
            Logger.getLogger(MqAMQPConsumer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean poll() throws Exception {
        boolean wereStored = false;

        if (!unit.isShuttingDown() && canSelectMessages) {
            Delivery delivery;

            while ((delivery = consumer.nextDelivery(DEFAULT_POLL_TIMEOUT_MILLIS)) != null) {
                if (delivery.getBody() != null) {
                    final AMQPMqMessage msg = new AMQPMqMessage(unit, this, delivery, System.currentTimeMillis());
                    unit.logMessageReceived(msg);
                    getMessageRepo().storeMessage(msg);
                    wereStored = true;

                }
            }
        }
        if (unit.isShuttingDown()) {
            shutdownSignalCaughtLatch.countDown();
        }
        return wereStored;
    }

    @Override
    public void onResponse(final AMQPMqMessage message, final MqProcessRs xProcessRs) {
        try {
            channel.basicAck(message.getCargo().getEnvelope().getDeliveryTag(), false);
            unit.logCommit(message);
        } catch (IOException ex) {
            unit.onCommitFailure(message, ex);
            return;
        }
        getMessageRepo().remove((AMQPMqMessage) message);
        getMessageRepo().releaseQueue((AMQPMqMessage) message);
    }

    @Override
    public void onFailToSendAasRequest(final AMQPMqMessage message, final Exception ex) {
        getMessageRepo().releaseQueueAfter((AMQPMqMessage) message, MqUnit.PAUSE_AFTER_INVOKE_EX_MILLIS);
    }

    @Override
    public void onAasException(final AMQPMqMessage message, final Exception ex) {
        getMessageRepo().releaseQueueAfter((AMQPMqMessage) message, MqUnit.PAUSE_AFTER_PROCESSING_EX_MILLIS);
    }

    @Override
    public boolean isClosed() {
        return !connection.isOpen();
    }

    @Override
    public void requestStopAsync() {
        final Thread stopperThread = new Thread(unit.getFullTitle() + " consumer stopper " + Utils.readableTime(System.currentTimeMillis())) {
            @Override
            public void run() {
                boolean abort = true;
                try {
                    if (shutdownSignalCaughtLatch.await(SLEEP_BEFORE_FORCE_WAKEUP_MILLIS, TimeUnit.MILLISECONDS)) {
                        abort = false;
                    }
                } catch (InterruptedException ex) {
                    //then abort
                }
                if (abort) {
                    try {
                        channel.abort();

                    } catch (Exception ex) {
                        //do nothing
                    }
                    try {
                        connection.abort();
                    } catch (Exception ex) {
                        //do nothing
                    }
                }
            }
        };
        stopperThread.setDaemon(true);
        stopperThread.start();
    }

    @Override
    public void maintenance() {
        pushMessages();
    }

    private void pushMessages() {
        if (!unit.isShuttingDown()) {
            while (unit.getAasClientPool().getFreeCount() > 0) {
                final MqAMQPConsumer.AMQPMqMessage msg = getMessageRepo().getAnyMessage();

                if (msg == null) {
                    break;
                } else {
                    final MqProcessRqDocument xProcessRqDoc = MqProcessRqDocument.Factory.newInstance();
                    final org.radixware.schemas.messagequeue.MqMessage xMessage = xProcessRqDoc.addNewMqProcessRq().addNewMessage();

                    xMessage.ensureMeta().setQueueName(options.queueName);
                    xMessage.getMeta().setDebugKey(msg.getDebugKey());
                    if (msg.getCargo().getProperties().getMessageId() != null) {
                        xMessage.getMeta().setMessageId(msg.getCargo().getProperties().getMessageId());
                    }
                    if (msg.getCargo().getProperties().getContentEncoding() != null) {
                        xMessage.getMeta().setEncoding(msg.getCargo().getProperties().getContentEncoding());
                    }
                    if (msg.getCargo().getProperties().getPriority() != null) {
                        xMessage.getMeta().setPriority(msg.getCargo().getProperties().getPriority().longValue());
                    }

                    final MqMessageMeta.AmqpSpecific specific = xMessage.getMeta().ensureAmqpSpecific();

                    specific.setExchange(msg.getCargo().getEnvelope().getExchange());
                    specific.setDeliveryTag(msg.getCargo().getEnvelope().getDeliveryTag());
                    specific.setRoutingKey(msg.getCargo().getEnvelope().getRoutingKey());
                    specific.setIsRedeliver(msg.getCargo().getEnvelope().isRedeliver());
                    specific.setAppId(msg.getCargo().getProperties().getAppId());
                    specific.setClassName(msg.getCargo().getProperties().getClassName());
                    specific.setClusterId(msg.getCargo().getProperties().getClusterId());
                    specific.setContentType(msg.getCargo().getProperties().getContentType());
                    specific.setCorrelationId(msg.getCargo().getProperties().getCorrelationId());
                    specific.setExpiration(msg.getCargo().getProperties().getExpiration());

                    if (msg.getCargo().getProperties().getExpiration() != null) {
                        try {
                            xMessage.getMeta().setTimeToLiveSec(Long.valueOf(msg.getCargo().getProperties().getExpiration()) / 1000);
                        } catch (RuntimeException ex) {
                            unit.getTrace().put(EEventSeverity.WARNING, "Unable to convert expiration '" + msg.getCargo().getProperties().getExpiration() + "' to seconds", EEventSource.MQ_HANDLER_UNIT);
                        }
                    }

                    specific.setReplyTo(msg.getCargo().getProperties().getReplyTo());
                    if (msg.getCargo().getProperties().getDeliveryMode() != null) {
                        specific.setDeliveryMode(msg.getCargo().getProperties().getDeliveryMode().longValue());
                    }
                    specific.setType(msg.getCargo().getProperties().getType());
                    specific.setUserId(msg.getCargo().getProperties().getUserId());
                    if (msg.getCargo().getProperties().getTimestamp() != null) {
                        specific.setTimestamp(new Timestamp(msg.getCargo().getProperties().getTimestamp().getTime()));
                    }

                    if (msg.getCargo().getProperties().getHeaders() != null) {
                        xMessage.ensureMeta().ensureProperties();
                        for (Map.Entry<String, Object> entry : msg.getCargo().getProperties().getHeaders().entrySet()) {
                            final MapStrStr.Entry xEntry = xMessage.getMeta().getProperties().addNewEntry();
                            xEntry.setKey(StringEscapeUtils.escapeXml(entry.getKey()));
                            xEntry.setValue(entry.getValue() == null ? null : StringEscapeUtils.escapeXml(entry.getValue().toString()));
                        }
                    }

                    xMessage.ensureBody().setByteBody(msg.getCargo().getBody());

                    unit.invoke(msg, xProcessRqDoc, processorId);
                }
            }
        }
    }

    @Override
    public void pause(String parameter) {
    }

    @Override
    public void resume(String parameter) {
    }

    @Override
    public boolean canPausePartition() {
        return false;
    }

    @Override
    public boolean canPausePolling() {
        return true;
    }
    
    @Override
    public void pausePolling() {
        canSelectMessages = false;
    }

    @Override
    public void resumePolling() {
        canSelectMessages = true;
    }
    
    @Override
    public boolean canRestoreOffset() {
        return false;
    }

    @Override
    public void rememberOffset(MqMessage mess) {
    }

    @Override
    public boolean isOffsetRemembered(MqMessage mess) {
        return false;
    }

    protected MqMessageRepo<AMQPMqMessage, String> getMessageRepo() {
        return mr;
    }

    @Override
    public String debugKey() {
        return debugKey;
    }

    public static class AMQPMqMessage extends MqMessage<Delivery, String> {

        final String partitionId;

        public AMQPMqMessage(final MqUnit unit, final IMqQueueConsumer consumer, final Delivery cargo, final long regTimeMillis) {
            super(unit, consumer, cargo, regTimeMillis);
            if (unit.getOptions().procOrder == null || unit.getOptions().procOrder == EMqProcOrder.SEQUENTIAL) {
                partitionId = MqUnit.PARTITION_NONE;
            } else if (unit.getOptions().procOrder == EMqProcOrder.PARTITION_SEQUENTIAL) {
                final String decodedPartitionId = decodePartitionId(cargo, unit.getOptions().partitionSource);
                partitionId = decodedPartitionId == null ? MqUnit.PARTITION_NONE : decodedPartitionId;
            } else {
                partitionId = String.valueOf(unit.nextMessageSeq());
            }

        }

        @Override
        public String getDebugKey() {
            return "id=" + getCargo().getProperties().getMessageId() + ",delivery-tag=" + getCargo().getEnvelope().getDeliveryTag();
        }

        @Override
        public String getPartitionId() {
            return partitionId;
        }

        @Override
        public byte[] getBodyBytes() {
            return getCargo().getBody();
        }

    }

    private static String decodePartitionId(final Delivery delivery, final String partitionSource) {
        if (!isHeaderPartitionSource(partitionSource)) {
            return null;
        }
        final String headerName = decodeHeaderPartitionSource(partitionSource);
        final Object headerObj = delivery.getProperties().getHeaders() == null ? null : delivery.getProperties().getHeaders().get(headerName);
        return headerObj == null ? null : headerObj.toString();
    }

    public static boolean isHeaderPartitionSource(final String partitionSource) {
        return partitionSource != null && partitionSource.startsWith(PARTITION_SOURCE_HEADER_PREFIX + PARTITION_SOURCE_TYPE_SEPARATOR);
    }

    public static String decodeHeaderPartitionSource(final String partitionSource) {
        if (isHeaderPartitionSource(partitionSource)) {
            return partitionSource.substring(PARTITION_SOURCE_HEADER_PREFIX.length() + PARTITION_SOURCE_TYPE_SEPARATOR.length());
        }
        return null;
    }

    public static String encodeHeaderPartitionSource(final String headerName) {
        if (headerName == null) {
            return null;
        }
        return PARTITION_SOURCE_HEADER_PREFIX + PARTITION_SOURCE_TYPE_SEPARATOR + headerName;
    }

}
