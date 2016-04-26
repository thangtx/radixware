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

import java.io.*;
import java.nio.ByteBuffer;
import javax.jms.*;
import org.apache.commons.logging.LogFactory;
import org.radixware.kernel.common.enums.EJmsMessageFormat;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.sc.SapClientOptions;


public class JmsSapConnection {

    public enum EStartConnectionMode {

        START_IMMEDIATELY,
        START_MANUALLY;
    }
    /**
     * null if this connection should receive response message synchronously
     */
    private final AsyncResponceHandler asyncResponseHandler;
    private final EStartConnectionMode startConnectionMode;
    private Connection connection;
    private Session session;
    private MessageProducer producer;
    private MessageConsumer consumer;
    private Destination responseDestination;
    private Destination requestDestination;
    private boolean started = false;
    private long readTimeOutMillis = -1;
    private InputStream inputStream;
    private OutputStream outputStream;

    public JmsSapConnection() {
        this(null, EStartConnectionMode.START_IMMEDIATELY);
    }

    public JmsSapConnection(AsyncResponceHandler asyncResponseHandler, final EStartConnectionMode startConnectionMode) {
        this.asyncResponseHandler = asyncResponseHandler;
        this.startConnectionMode = startConnectionMode;
    }

    public void connect(SapClientOptions sap) throws IOException {
        try {
            final JmsObjectsFactory factory = new JmsObjectsFactory(sap.getAddress().getJmsAddress().getOptions());
            connection = factory.createConnection();
            session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            requestDestination = factory.createRequestDestination(session);
            producer = session.createProducer(requestDestination);
            responseDestination = session.createTemporaryQueue();
            consumer = session.createConsumer(responseDestination);
            if (asyncResponseHandler != null) {
                consumer.setMessageListener(new MessageListener() {
                    @Override
                    public void onMessage(Message msg) {
                        try {
                            try {
                                msg.acknowledge();
                            } catch (JMSException ex) {
                                throw new IOException(ex);
                            }
                            final byte[] data = messageToHttpSoap(msg);
                            asyncResponseHandler.onResponse(data);
                        } catch (IOException ex) {
                            asyncResponseHandler.onReceiveError(ex);
                        }
                    }
                });
            } else {
                inputStream = new ProxyInputStream();
            }
            outputStream = new ProxyOutputStream();
            connection.setExceptionListener(new ExceptionListener() {
                @Override
                public void onException(JMSException jmse) {
                    LogFactory.getLog(JmsSapConnection.class).error("JMS error", jmse);
                }
            });
            if (startConnectionMode == EStartConnectionMode.START_IMMEDIATELY) {
                startConnection();
            }
        } catch (JmsObjectsFactory.InvalidJmsConfigurationException | JMSException ex) {
            throw new IOException(ex);
        }
    }

    public void startConnection() throws JMSException {
        if (started) {
            return;
        }
        if (connection == null) {
            throw new java.lang.IllegalStateException("Not connected");
        }
        connection.start();
        started = true;
    }

    public void close() throws IOException {
        if (connection == null) {
            return;
        }
        try {
            connection.close();
        } catch (JMSException ex) {
            throw new IOException(ex);
        } finally {
            connection = null;
            session = null;
            producer = null;
            consumer = null;
            inputStream = null;
            outputStream = null;
            responseDestination = null;
            requestDestination = null;
            started = false;
        }
    }

    public OutputStream getOutputStream() throws IOException {
        checkStarted();
        return outputStream;
    }

    public InputStream getInputStream() throws IOException {
        if (isAsyncMode()) {
            throw new IllegalUsageError("Connection is in async receive mode");
        }
        checkStarted();
        return inputStream;
    }

    public boolean isStarted() {
        return started;
    }

    private void checkStarted() {
        if (!started) {
            throw new java.lang.IllegalStateException("Jms connection is not started");
        }
    }

    private boolean isAsyncMode() {
        return asyncResponseHandler != null;
    }

    public void setReadTimeOut(int timeoutMillis) throws IOException {
        if (isAsyncMode()) {
            throw new IllegalUsageError("Connection is in async receive mode");
        }
        readTimeOutMillis = timeoutMillis;

    }

    private class ProxyOutputStream extends OutputStream {

        private final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        private String firstMessageId = null;

        @Override
        public void write(int b) throws IOException {
            bos.write(b);
        }

        @Override
        public void flush() throws IOException {
            try {
                if (bos.size() > 0) {
                    final Message requestMessage;
                    try {
                        requestMessage = JmsUtils.convertHttpSoapMessageToJms(bos.toByteArray(), session, EJmsMessageFormat.BIN);
                    } catch (EOFException ex) {
                        return;//not all data has been written, wait for next attempt
                    }
                    try {
                        requestMessage.setJMSReplyTo(responseDestination);
                        if (firstMessageId != null) {
                            requestMessage.setJMSCorrelationID(firstMessageId);
                        }
                        producer.send(requestMessage);
                        if (firstMessageId == null) {
                            firstMessageId = requestMessage.getJMSMessageID();
                        }
                    } finally {
                        bos.reset();
                    }
                }
            } catch (JMSException ex) {
                throw new IOException(ex);
            }
        }
    }

    private class ProxyInputStream extends InputStream {

        private ByteBuffer pendingMessageBuf;

        @Override
        public int read() throws IOException {
            if (pendingMessageBuf == null || !pendingMessageBuf.hasRemaining()) {
                final Message message;
                try {
                    if (readTimeOutMillis >= 0) {
                        message = consumer.receive(readTimeOutMillis);
                    } else {
                        message = consumer.receive();
                    }
                } catch (JMSException ex) {
                    throw new IOException(ex);
                }
                if (message == null) {
                    throw new IOException("JMS message recieve timeout");
                }
                try {
                    message.acknowledge();
                } catch (JMSException ex) {
                    throw new IOException(ex);
                }
                pendingMessageBuf = ByteBuffer.wrap(messageToHttpSoap(message));
            }
            return pendingMessageBuf.get();
        }
    }

    @Override
    public String toString() {
        return started ? "JMS{rq_queue=" + requestDestination.toString() + ", rs_queue=" + responseDestination.toString() + "}" : "JMS{non_started}";

    }

    private static byte[] messageToHttpSoap(final Message message) throws IOException {
        return JmsUtils.convertJmsMessageToHttpSoap(message);
    }

    public static interface AsyncResponceHandler {

        public void onResponse(final byte[] httpSoapData);

        public void onReceiveError(final IOException exception);
    }
}
