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
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.radixware.kernel.server.units.mq.interfaces.IMqQueueProducer;
import org.radixware.schemas.messagequeue.MqMessageBody;
import org.radixware.schemas.messagequeue.MqMessageMeta;

public class MqKafkaProducer implements IMqQueueProducer {
    private final String queueName;
    private final int partitionId;
    private final Producer<String, byte[]>  producer;
    private String debugKey;

    public MqKafkaProducer(final String server) {
        this(server,null,-1);
    }
    
    public MqKafkaProducer(final String server, final String queueName, final int partitionId) {
        this.queueName = queueName;
        this.partitionId = partitionId;
        final Properties props = new Properties();
        
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");
        producer = new KafkaProducer<String,byte[]>(props);        
        debugKey = "Kafka '" + queueName + "'";
    }    

    @Override
    public void send(MqMessageMeta meta, MqMessageBody body) throws IOException {
        producer.send(new ProducerRecord<String, byte[]>(queueName == null ? meta.getQueueName() : queueName, 
                                                         partitionId == -1 ? meta.getKafkaSpecific().getPartition().intValue() : partitionId, 
                                                         meta.getMessageId(),
                                                         body.getByteBody()));
    }

    @Override
    public void close() throws Exception {
        producer.close();
    }
    
}
