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

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.server.units.mq.interfaces.IMqQueueProducer;
import org.radixware.schemas.messagequeue.MqMessageBody;
import org.radixware.schemas.messagequeue.MqMessageMeta;

public class MqAMQPProducer implements IMqQueueProducer { 
    private final String queueName;
    private final Connection connection;
    private final Channel channel;
    private final String debugKey;

    public MqAMQPProducer(final String server) throws IOException {
        this(server,null,null,null);
    }
    
    public MqAMQPProducer(final String server, final String queueName) throws IOException {
        this(server,queueName,null,null);
    }
    
    public MqAMQPProducer(final String server, final String queueName, final String user, final String password) throws IOException {
        this.queueName = queueName;
        final ConnectionFactory factory = new ConnectionFactory();
        final String[] broker = server.split(":");

        factory.setHost(broker[0]);
        factory.setPort(broker.length > 1 ? Integer.valueOf(broker[1]) : MqAMQPConsumer.DEFAULT_BROKER_PORT);
        
        if (user != null && !user.isEmpty()) {
            factory.setUsername(user);
            if (password != null && !password.isEmpty()) {
                factory.setPassword(password);
            }
        }

        try{connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (TimeoutException ex) {
            throw new IOException(ex);
        }
        debugKey = "AMQP '" + queueName + "'";
    }    

    @Override
    public void send(MqMessageMeta meta, MqMessageBody body) throws IOException {
        channel.basicPublish("", queueName == null ? meta.getQueueName() : queueName, null, body.getByteBody());
    }

    @Override
    public void close() throws Exception {
        try{channel.close();
            connection.close();
        } catch (TimeoutException ex) {
            Logger.getLogger(MqAMQPConsumer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
