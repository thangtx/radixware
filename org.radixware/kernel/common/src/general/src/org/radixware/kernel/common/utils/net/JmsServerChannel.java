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

package org.radixware.kernel.common.utils.net;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.*;
import javax.jms.*;
import org.apache.commons.logging.LogFactory;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.Guid;
import org.radixware.kernel.common.utils.io.pipe.PipeAddress;


public class JmsServerChannel extends PipeServerChannel {

    private javax.jms.Connection connection;
    private javax.jms.Session session;
    private MessageConsumer consumer;
    private final LocalTracer tracer;
    private final SapAddress pipeSapAddress = new SapAddress(new PipeAddress("jms2sap://" + Guid.generateGuid()));
    final Map<String, WeakReference<JmsClientPipe>> keptConnections = new HashMap<>();

    public JmsServerChannel(final LocalTracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public void open(final SapAddress sapAddress) throws IOException {
        try {
            final JmsObjectsFactory factory = new JmsObjectsFactory(sapAddress.getJmsAddress().getOptions());
            connection = factory.createConnection();
            session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            consumer = session.createConsumer(factory.createRequestDestination(session));
            consumer.setMessageListener(new MessageListener() {

                @Override
                public void onMessage(Message msg) {
                    try {
                        if (msg instanceof TextMessage || msg instanceof BytesMessage) {
                            removeDiscardedKeptConnections();
                            final String key = keyForMessage(msg);
                            JmsClientPipe clientPipe = null;
                            if (keptConnections.containsKey(key)) {
                                clientPipe = keptConnections.get(key).get();
                            }
                            if (clientPipe == null || !clientPipe.offerMessage(msg)) {
                                clientPipe = new JmsClientPipe(msg, connection, factory, tracer);
                                keptConnections.put(key, new WeakReference<>(clientPipe));
                                clientPipe.configureBlocking(false);
                                clientPipe.connect(pipeSapAddress.getPipeAddress(), -1);
                            }
                        } else {
                            tracer.put(EEventSeverity.ERROR, "Only text and byte messages are supported by RadixWare JMS channel", null, null, false);
                        }
                    } catch (Exception ex) {//onMessage should not throw any exceptions
                        tracer.put(EEventSeverity.ERROR, ExceptionTextFormatter.throwableToString(ex), null, null, false);
                    }
                }
            });
            super.open(pipeSapAddress);
            connection.setExceptionListener(new ExceptionListener() {

                @Override
                public void onException(JMSException jmse) {
                    LogFactory.getLog(JmsSapConnection.class).error("JMS error", jmse);
                }
            });
            connection.start();
        } catch (Exception ex) {
            close();
            throw new IOException("Error on JMS listener start", ex);
        }
    }

    private String keyForMessage(final Message message) throws JMSException {
        if (message.getJMSCorrelationID() == null || message.getJMSCorrelationID().isEmpty()) {
            return message.getJMSMessageID();
        }
        return message.getJMSCorrelationID();
    }

    private void removeDiscardedKeptConnections() {
        final Iterator<Map.Entry<String, WeakReference<JmsClientPipe>>> it = keptConnections.entrySet().iterator();
        while (it.hasNext()) {
            if (it.next().getValue().get() == null) {
                it.remove();
            }
        }
    }

    @Override
    public void close() throws IOException {
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException ex) {
                throw new IOException("Error while closing JMS connection", ex);
            } finally {
                connection = null;
                session = null;
                consumer = null;
                super.close();
            }
        }
    }
}
