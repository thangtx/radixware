/*
 * Copyright (c) 2008-2017, Compass Plus Limited. All rights reserved.
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

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.protocol.SecurityProtocol;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EPortSecurityProtocol;
import org.radixware.kernel.common.exceptions.KeystoreControllerException;
import org.radixware.kernel.common.ssl.KeystoreController;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.SoapFormatter;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.net.SslUtils;
import org.radixware.kernel.server.crypto.ProxyCryptoProvider;
import org.radixware.kernel.server.units.mq.MqKafkaConsumer.KafkaMqMessage;
import org.radixware.kernel.server.units.mq.interfaces.IMqQueueConsumer;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.schemas.messagequeue.MqProcessRqDocument;
import org.radixware.schemas.messagequeue.MqProcessRs;

public class MqKafkaConsumer implements IMqQueueConsumer<KafkaMqMessage, TopicPartition> {

    private static final int DEFAULT_POLL_TIMEOUT_MILLIS = SystemPropUtils.getIntSystemProp("rdx.mq.kafka.default.poll.timeout.millis", 100);
    private static final int DEFAULT_WAKEUP_TIMEOUT_MILLIS = SystemPropUtils.getIntSystemProp("rdx.mq.kafka.default.wakeup.timeout.millis", 30000);
    private static final String KAFKA_CONSUMER_PROP_PREFIX = "rdx.mq.kafka.consumer.prop.";

    private final Properties props = new Properties();
    private final KafkaConsumer<byte[], byte[]> consumer;
    private final long processorId;
    private final MqUnit unit;
    private final MqMessageRepo<KafkaMqMessage, TopicPartition> mr;
    private CountDownLatch shutdownSignalCaughtLatch = new CountDownLatch(1);
    private final ExecutorService wakeuper;
    private final String debugKey;
    private final MqDbQueries.MqUnitOptions options;
    private volatile boolean closed = false;
    private final Map<TopicPartition, Long> partition2Offset = new HashMap<>();

    public MqKafkaConsumer(final MqUnit unit, final MqDbQueries.MqUnitOptions options) throws IOException {
        processorId = options.processorId;

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, options.brokerAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, options.consumerKey);
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, String.valueOf(options.maxPartitionFetchBytes));
        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, String.valueOf(1));

        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, String.valueOf(options.kafkaSessionTimeoutSec * 1000));
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, String.valueOf(options.kafkaSessionTimeoutSec * 1000 / 3));
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArrayDeserializer");

        if (options.kafkaBrokerKerberosName != null) {
            props.put(SaslConfigs.SASL_KERBEROS_SERVICE_NAME, options.kafkaBrokerKerberosName);
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, SecurityProtocol.SASL_PLAINTEXT.name);
        }

        if (options.kafkaBrokerKerberosName == null && options.securityProtocol.isTls()) {
            KeystoreController controller = null;
            try {
                controller = KeystoreController.getServerKeystoreController();
            } catch (KeystoreControllerException ex) {
                throw new IOException("Invalid Instance Key Store Controller state", ex);
            }

            final String ksName = controller.getKeyStoreName();
            if (ksName.isEmpty()) {
                throw new IOException("Instance Key Store not iniitialized");
            }

            File fakeKeyStoreFile = RadixLoader.getInstance().createTempFile("KafkaFakeKeyStoreFile");
            try {
                fakeKeyStoreFile.createNewFile();
            } catch (IOException ex) {
                throw new IOException("Fake Key Store file creation failure", ex);
            }

            final String RADIX_CRYPTO = ProxyCryptoProvider.getProxyCryptoProviderId();
            final String fakeKeyStorePath = fakeKeyStoreFile.getAbsolutePath();

            final String serverCertAliases = options.serverCertAliases != null
                    ? new ArrStr(options.serverCertAliases).toString() : "null";
            final String clientKeyAliases = options.clientKeyAliases != null
                    ? new ArrStr(options.clientKeyAliases).toString() : "null";

            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
            if (options.securityProtocol == EPortSecurityProtocol.TLSv1_2) {
                props.put(SslConfigs.SSL_ENABLED_PROTOCOLS_CONFIG, "TLSv1.2");
            }

            props.put(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, RADIX_CRYPTO);
            props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, fakeKeyStorePath);
            props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, serverCertAliases);

            props.put(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, RADIX_CRYPTO);
            props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, fakeKeyStorePath);
            props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, clientKeyAliases);

            final List<String> cipherSuitesList = options.cipherSuites == null
                    ? SslUtils.getStrongCipherSuitesAsStrList(options.securityProtocol)
                    : options.cipherSuites;
            if (!cipherSuitesList.isEmpty()) {
                final String cipherSuites = StringUtils.join(cipherSuitesList, ",");
                props.put(SslConfigs.SSL_CIPHER_SUITES_CONFIG, cipherSuites);
            }

            props.put(SslConfigs.SSL_TRUSTMANAGER_ALGORITHM_CONFIG, RADIX_CRYPTO);
            props.put(SslConfigs.SSL_KEYMANAGER_ALGORITHM_CONFIG, RADIX_CRYPTO);
        }

        final String addPropPrefix = KAFKA_CONSUMER_PROP_PREFIX + serverAddressToPropPart(options.brokerAddress) + ".";

        for (Map.Entry entry : System.getProperties().entrySet()) {
            final String keyStr = String.valueOf(entry.getKey());
            if (keyStr.startsWith(addPropPrefix)) {
                props.put(keyStr.substring(addPropPrefix.length()), String.valueOf(entry.getValue()));

            }
        }

        consumer = new KafkaConsumer(props);
        consumer.subscribe(Collections.singletonList(options.queueName), new ConsumerRebalanceListener() {

            private void logChange(final String changeStr) {
                unit.getTrace().put(EEventSeverity.EVENT, String.format(MqMessages.ASSIGNED_PARTITIONS_CHANGED, changeStr), MqMessages.W_MLS_ID_ASSIGNED_PARTITIONS_CHANGED.getMlsId(), new ArrStr(changeStr), EEventSource.MQ_HANDLER_UNIT.getValue(), false);
            }

            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                if (partitions != null && !partitions.isEmpty()) {
                    logChange("- " + Arrays.toString(partitions.toArray()));
                }
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                if (partitions != null && !partitions.isEmpty()) {
                    logChange("+ " + Arrays.toString(partitions.toArray()));
                }
            }
        });
        mr = new MqMessageRepo<>(this, unit);
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
        this.options = options;
    }

    private String serverAddressToPropPart(final String serverAddress) {
        if (serverAddress == null) {
            return null;
        }
        return serverAddress.replace(".", "_").replace(":", "_");
    }

    @Override
    public MqDbQueries.MqUnitOptions getOptions() {
        return options;
    }

    @Override
    public void close() throws IOException {
        try {
            wakeuper.shutdownNow();
        } catch (Exception ex) {
            unit.getTrace().put(EEventSeverity.WARNING, "Unable to stop wakuper: " + ExceptionTextFormatter.throwableToString(ex), EEventSource.MQ_HANDLER_UNIT);
        }
        try {
            getMessageRepo().close();
            consumer.unsubscribe();
        } finally {
            consumer.close();
            closed = true;
        }
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public boolean poll() throws Exception {
        if (!unit.isShuttingDown()) {
            final WakeupRunnable wakeupRunnable = new WakeupRunnable(DEFAULT_WAKEUP_TIMEOUT_MILLIS, consumer);
            wakeuper.submit(wakeupRunnable);
            try {
                final ConsumerRecords<byte[], byte[]> records = consumer.poll(DEFAULT_POLL_TIMEOUT_MILLIS);
                boolean wereStored = false;
                wakeupRunnable.cancel();
                final long regTimeMillis = System.currentTimeMillis();
                for (ConsumerRecord<byte[], byte[]> record : records) {
                    final KafkaMqMessage msg = new KafkaMqMessage(unit, this, record, regTimeMillis);
                    unit.logMessageReceived(msg);
                    getMessageRepo().storeMessage(msg);
                    wereStored = true;
                }
                if (!wereStored) {
                    return false;
                }
            } catch (WakeupException ex) {
                unit.getTrace().put(EEventSeverity.ERROR, "Polling of the '" + debugKey() + " stuck for " + DEFAULT_WAKEUP_TIMEOUT_MILLIS + " ms and was interrupted", EEventSource.MQ_HANDLER_UNIT);
                return false;
            } finally {
                wakeupRunnable.cancel();
            }
        }

        if (unit.isShuttingDown()) {
            shutdownSignalCaughtLatch.countDown();
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
        try {
            commit(message);
        } catch (Exception ex) {
            unit.onCommitFailure(message, ex);
            return;
        }
        getMessageRepo().remove((KafkaMqMessage) message);
        getMessageRepo().releaseQueue((KafkaMqMessage) message);
    }

    private void commit(final KafkaMqMessage message) {
        final ConsumerRecord<byte[], byte[]> record = ((KafkaMqMessage) message).getCargo();
        final Map<TopicPartition, OffsetAndMetadata> toCommit = new HashMap<>();
        toCommit.put(new TopicPartition(record.topic(), record.partition()), new OffsetAndMetadata(record.offset() + 1, ""));
        consumer.commitSync(toCommit);
        unit.logCommit(message);
    }

    @Override
    public void onFailToSendAasRequest(KafkaMqMessage message, Exception ex) {
        getMessageRepo().releaseQueueAfter(message, MqUnit.PAUSE_AFTER_INVOKE_EX_MILLIS);
    }

    @Override
    public void onAasException(final KafkaMqMessage message, final Exception ex) {
        getMessageRepo().releaseQueueAfter(message, MqUnit.PAUSE_AFTER_PROCESSING_EX_MILLIS);
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
    public boolean canPausePartition() {
        return true;
    }

    @Override
    public void pause(TopicPartition tp) {
        consumer.pause(Collections.singletonList(tp));
    }

    @Override
    public void resume(TopicPartition tp) {
        if (partition2Offset.get(tp) != null) {
            final long off = partition2Offset.get(tp);
            consumer.seek(tp, off);
            partition2Offset.remove(tp);
        }
        consumer.resume(Collections.singletonList(tp));
    }

    @Override
    public boolean canPausePolling() {
        return false;
    }

    @Override
    public void pausePolling() {
    }

    @Override
    public void resumePolling() {
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

        public KafkaMqMessage(final MqUnit unit, final IMqQueueConsumer consumer, final ConsumerRecord<byte[], byte[]> record, final long regTimeMillis) {
            super(unit, consumer, record, regTimeMillis);
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
        public byte[] getBodyBytes() {
            return getCargo().value();
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

    @Override
    public boolean canRestoreOffset() {
        return true;
    }

    @Override
    public void rememberOffset(MqMessage mess) {
        if (mess == null) {
            throw new NullPointerException("Message must be not null");
        }
        final KafkaMqMessage kMess = (KafkaMqMessage) mess;
        if (partition2Offset.get(kMess.getPartitionId()) == null) {
            partition2Offset.put(kMess.getPartitionId(), kMess.getCargo().offset());
        }
    }

    @Override
    public boolean isOffsetRemembered(MqMessage mess) {
        if (mess == null) {
            throw new NullPointerException("Message must be not null");
        }
        final KafkaMqMessage kMess = (KafkaMqMessage) mess;
        return partition2Offset.get(kMess.getPartitionId()) != null;
    }
    
}
