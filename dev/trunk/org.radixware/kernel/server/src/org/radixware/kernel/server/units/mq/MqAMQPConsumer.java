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
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.server.units.mq.MqAMQPConsumer.AMQPMqMessage;
import org.radixware.kernel.server.units.mq.interfaces.IMqQueueConsumer;
import org.radixware.schemas.messagequeue.MqProcessRqDocument;
import org.radixware.schemas.messagequeue.MqProcessRs;

public class MqAMQPConsumer implements IMqQueueConsumer<AMQPMqMessage,String> {
    public static final int QUEUE_DELAY_ON_FAIL_MILLIS = 60000;
    public static final int DEFAULT_BROKER_PORT = 5672;
    public static final long SLEEP_BEFORE_FORCE_WAKEUP_MILLIS = SystemPropUtils.getLongSystemProp("rdx.amqp.consumer.sleep.before.forced.close.millis", 5000);
    public static final int DEFAULT_POLL_TIMEOUT_MILLIS = SystemPropUtils.getIntSystemProp("rdx.mq.amqp.default.poll.timeout.millis", 100);

    private final MqUnit unit;
    private final MqDbQueries.MqUnitOptions options;
    private final MqMessageRepo<AMQPMqMessage, String> mr;
    private final Connection connection;
    private final Channel channel;
    private final QueueingConsumer consumer;
    private final long processorId;
    private final String debugKey;

    private volatile boolean shutdownSignalCaught;
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

        try{connection = factory.newConnection();
            channel = connection.createChannel();
            consumer = new QueueingConsumer(channel);
            channel.basicConsume(options.queueName, false, consumer);
            mr = new MqMessageRepo(this);
        } catch (TimeoutException ex) {
            throw new IOException(ex);
        }
        debugKey = "AMQP '" + options.queueId + ") " + options.queueTitle + "'";
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
                    getMessageRepo().storeMessage(new AMQPMqMessage(unit, this, delivery));
                    wereStored = true;
                }
            }
        }
        if (unit.isShuttingDown()) {
            shutdownSignalCaught = true;
        }
        return wereStored;
    }

    @Override
    public void onResponse(final AMQPMqMessage message, final MqProcessRs xProcessRs) {
        unit.aboutGotResponse(message);        
        try{channel.basicAck(message.getCargo().getEnvelope().getDeliveryTag(), false);
            unit.aboutCommit(message);        
        } catch (IOException ex) {
            unit.aboutCommitFailure(message,ex);            
        }
        getMessageRepo().remove((AMQPMqMessage) message);
        getMessageRepo().releaseQueue((AMQPMqMessage) message);
    }

    @Override
    public void onFailToSendAasRequest(final AMQPMqMessage message, final Exception ex) {
        unit.aboutSendAASException(message,ex);
        getMessageRepo().releaseQueueAfter((AMQPMqMessage) message,QUEUE_DELAY_ON_FAIL_MILLIS);
    }

    @Override
    public void onAasException(final AMQPMqMessage message, final Exception ex) {
        unit.aboutAASException(message,ex);
        getMessageRepo().releaseQueueAfter((AMQPMqMessage) message,QUEUE_DELAY_ON_FAIL_MILLIS);
    }

    @Override
    public void requestStopAsync() {
        final Thread stopperThread = new Thread(unit.getFullTitle() + " consumer stopper " + Utils.readableTime(System.currentTimeMillis())) {
            @Override
            public void run() {
                try {
                    Thread.sleep(SLEEP_BEFORE_FORCE_WAKEUP_MILLIS);
                } catch (InterruptedException ex) {
                    return;
                }
                if (!shutdownSignalCaught) {
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
                    if (msg.getPartitionId() != null) {
                        xMessage.ensureMeta().setMessageId(String.valueOf(msg.getPartitionId()));
                    }

                    xMessage.ensureMeta().setDebugKey(msg.getDebugKey());
                    xMessage.ensureBody().setByteBody(msg.getCargo().getBody());

                    unit.invoke(msg, xProcessRqDoc, processorId);
                }
            }
        }
    }

    @Override
    public void pause(String parameter) {
//        canSelectMessages = false;
    }

    @Override
    public void resume(String parameter) {
//        canSelectMessages = true;
    }

    @Override
    public void pausePolling() {
        canSelectMessages = false;
    }

    @Override
    public void resumePolling() {
        canSelectMessages = true;
    }

    
    protected MqMessageRepo<AMQPMqMessage, String> getMessageRepo() {
        return mr;
    }

    @Override
    public String debugKey() {
        return debugKey;
    }

    public static class AMQPMqMessage extends MqMessage<Delivery, String> {

        public AMQPMqMessage(final MqUnit unit, final IMqQueueConsumer consumer, final Delivery cargo) {
            super(unit, consumer, cargo);
        }

        @Override
        public String getDebugKey() {
            return "delivery-tag=" + getCargo().getEnvelope().getDeliveryTag();
        }

        @Override
        public String getPartitionId() {
            return getCargo().getEnvelope().getRoutingKey();
        }
    }
}
