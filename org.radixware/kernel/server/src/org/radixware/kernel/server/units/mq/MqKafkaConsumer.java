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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.kernel.common.utils.SoapFormatter;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.server.units.mq.MqKafkaConsumer.KafkaMqMessage;
import org.radixware.kernel.server.units.mq.interfaces.IMqQueueConsumer;
import org.radixware.schemas.messagequeue.MqProcessRqDocument;
import org.radixware.schemas.messagequeue.MqProcessRs;

public class MqKafkaConsumer implements IMqQueueConsumer<KafkaMqMessage, TopicPartition> {

    private final long SLEEP_BEFORE_FORCE_WAKEUP_MILLIS = SystemPropUtils.getLongSystemProp("rdx.kafka.consumer.sleep.before.forced.close.millis", 5000);

    private final int QUEUE_DELAY_ON_FAIL_MILLIS = 60000;
    private static final int DEFAULT_POLL_TIMEOUT_MILLIS = SystemPropUtils.getIntSystemProp("rdx.mq.kafka.default.poll.timeout.millis", 100);
    private static final int DEFAULT_WAKEUP_TIMEOUT_MILLIS = SystemPropUtils.getIntSystemProp("rdx.mq.kafka.default.wakeup.timeout.millis", 30000);

    private final Properties props = new Properties();
    private final KafkaConsumer<byte[], byte[]> consumer;
    private final long processorId;
    private final MqUnit unit;
    private final MqMessageRepo<KafkaMqMessage, TopicPartition> mr;
    private volatile boolean shutdownSignalCaught;
    private final ExecutorService wakeuper;
    private final String debugKey;
    private boolean canSelectMessages = true;

    public MqKafkaConsumer(final MqUnit unit, final MqDbQueries.MqUnitOptions options) {
        processorId = options.processorId;

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, options.brokerAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, options.consumerKey);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, String.valueOf(options.kafkaSessionTimeoutSec * 1000));
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        
        consumer = new KafkaConsumer(props);
        consumer.subscribe(Collections.singletonList(options.queueName));
        mr = new MqMessageRepo<KafkaMqMessage, TopicPartition>(this);
        this.unit = unit;

        wakeuper = Executors.newFixedThreadPool(1, new ThreadFactory() {

            @Override
            public Thread newThread(Runnable r) {
                final Thread t = new Thread(r, "Unit #" + unit.getId() + " KafkaConsumer '" + options.queueId + ") " + options.queueTitle + "' wakeuper");
                t.setDaemon(true);
                return t;
            }
        });

        debugKey = "Kafka '" + options.queueId + ") " + options.queueTitle + "'";
    }

    @Override
    public void close() throws IOException {
        try {
            wakeuper.shutdownNow();
        } catch (Exception ex) {
            unit.getTraceInterface().put(EEventSeverity.WARNING, "Unable to stop wakuper: " + ExceptionTextFormatter.throwableToString(ex), EEventSource.MQ_HANDLER_UNIT);
        }
        try {
            getMessageRepo().close();
            consumer.unsubscribe();
        } finally {
            consumer.close();
        }
    }

    @Override
    public boolean poll() throws Exception {
        if (!unit.isShuttingDown() && canSelectMessages) {
            final WakeupRunnable wakeupRunnable = new WakeupRunnable(DEFAULT_WAKEUP_TIMEOUT_MILLIS, consumer);
            wakeuper.submit(wakeupRunnable);
            try {
                final ConsumerRecords<byte[], byte[]> records = consumer.poll(DEFAULT_POLL_TIMEOUT_MILLIS);
                boolean wereStored = false;

                wakeupRunnable.cancel();
                for (ConsumerRecord<byte[], byte[]> record : records) {
                    final KafkaMqMessage msg = new KafkaMqMessage(unit, this, record);
                    unit.getTraceInterface().put(EEventSeverity.DEBUG, "Received message " + msg.getDebugKey(), EEventSource.MQ_HANDLER_UNIT);
                    unit.getTraceInterface().put(EEventSeverity.DEBUG, "Message body: " + Hex.encode(record.value()), null, null, EEventSource.MQ_HANDLER_UNIT, true);
                    getMessageRepo().storeMessage(msg);
                    wereStored = true;
                }
                if (!wereStored) {
                    return false;
                }
            } catch (WakeupException ex) {
                unit.getTraceInterface().put(EEventSeverity.ERROR, "Polling of the '" + debugKey() + " stuck for " + DEFAULT_WAKEUP_TIMEOUT_MILLIS + " ms and was interrupted", EEventSource.MQ_HANDLER_UNIT);
                return false;
            } finally {
                wakeupRunnable.cancel();
            }
        }

        if (unit.isShuttingDown()) {
            shutdownSignalCaught = true;
        }
        return true;
    }

    @Override
    public void maintenance() {
        pushMessages();
    }

    private void pushMessages() {
        if (!unit.isShuttingDown()) {
            while (unit.getAasClientPool().getFreeCount() > 0) {
                final KafkaMqMessage msg = getMessageRepo().getAnyMessage();

                if (msg == null) {
                    break;
                } else {
                    final MqProcessRqDocument xProcessRqDoc = MqProcessRqDocument.Factory.newInstance();

                    final org.radixware.schemas.messagequeue.MqMessage xMessage = xProcessRqDoc.addNewMqProcessRq().addNewMessage();

                    xMessage.ensureMeta().setQueueName(msg.getCargo().topic());
                    if (msg.getCargo().key() != null) {
                        xMessage.ensureMeta().setMessageId(new String(msg.getCargo().key(), SoapFormatter.UTF_8_CHARSET));
                    }

                    xMessage.ensureMeta().setDebugKey(msg.getDebugKey());
                    xMessage.ensureMeta().ensureKafkaSpecific().setPartition((long) msg.getCargo().partition());
                    xMessage.ensureMeta().ensureKafkaSpecific().setOffset(msg.getCargo().offset());

                    xMessage.ensureBody().setByteBody(msg.getCargo().value());

                    unit.invoke(msg, xProcessRqDoc, processorId);
                }
            }
        }
    }

    @Override
    public void onResponse(KafkaMqMessage message, MqProcessRs xProcessRs) {
        unit.aboutGotResponse(message);
        commit(message);
        getMessageRepo().remove((KafkaMqMessage) message);
        getMessageRepo().releaseQueue((KafkaMqMessage) message);

    }

    private void commit(final KafkaMqMessage message) {
        final ConsumerRecord<byte[], byte[]> record = ((KafkaMqMessage) message).getCargo();
        final Map<TopicPartition, OffsetAndMetadata> toCommit = new HashMap<>();

        toCommit.put(new TopicPartition(record.topic(), record.partition()), new OffsetAndMetadata(record.offset() + 1, ""));
        consumer.commitSync(toCommit);
        unit.aboutCommit(message);
    }

    @Override
    public void onFailToSendAasRequest(KafkaMqMessage message, Exception ex) {
        unit.aboutSendAASException(message, ex);
        getMessageRepo().releaseQueueAfter(message, QUEUE_DELAY_ON_FAIL_MILLIS);
    }

    @Override
    public void onAasException(final KafkaMqMessage message, final Exception ex) {
        unit.aboutAASException(message, ex);
        getMessageRepo().releaseQueueAfter(message, QUEUE_DELAY_ON_FAIL_MILLIS);
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
                        consumer.wakeup();
                    } catch (Exception ex) {
                        //do nothing
                    }
                }
            }
        };
        stopperThread.setDaemon(true);
        stopperThread.start();
    }

    protected MqMessageRepo<KafkaMqMessage, TopicPartition> getMessageRepo() {
        return mr;
    }

    @Override
    public void pause(TopicPartition tp) {
        consumer.pause(tp);
    }

    @Override
    public void resume(TopicPartition tp) {
        consumer.resume(tp);
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
    public String debugKey() {
        return debugKey;
    }

    private static class WakeupRunnable implements Runnable {

        private final int timeoutMillis;
        private final CountDownLatch latch;
        private volatile boolean cancelled = false;
        private final KafkaConsumer consumer;

        public WakeupRunnable(final int timeoutMillis, final KafkaConsumer consumer) {
            this.timeoutMillis = timeoutMillis;
            this.consumer = consumer;
            latch = new CountDownLatch(1);
        }

        public void cancel() {
            cancelled = true;
            latch.countDown();
        }

        @Override
        public void run() {
            try {
                latch.await(timeoutMillis, TimeUnit.MILLISECONDS);
            } catch (InterruptedException ex) {
                return;
            }
            if (!cancelled) {
                consumer.wakeup();
            }
        }

    }

    public static class KafkaMqMessage extends MqMessage<ConsumerRecord<byte[], byte[]>, TopicPartition> {

        private final TopicPartition tp;
        private final String debugKey;

        public KafkaMqMessage(final MqUnit unit, final IMqQueueConsumer consumer, final ConsumerRecord<byte[], byte[]> record) {
            super(unit, consumer, record);
            this.tp = new TopicPartition(getCargo().topic(), getCargo().partition());
            final StringBuilder sb = new StringBuilder();
            sb.append("(key=");
            if (record.key() != null) {
                sb.append(new String(record.key(), SoapFormatter.UTF_8_CHARSET));
            } else {
                sb.append("null");
            }
            sb.append(", partition=");
            sb.append(record.partition());
            sb.append(", offset=");
            sb.append(record.offset());
            sb.append(")");
            debugKey = sb.toString();
        }

        @Override
        public String getDebugKey() {
            return debugKey;
        }

        @Override
        public TopicPartition getPartitionId() {
            return tp;
        }
    }
}
