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

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import javax.jms.*;
import javax.xml.namespace.QName;
import org.radixware.kernel.common.enums.EHttpParameter;
import org.radixware.kernel.common.enums.EJmsMessageFormat;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.SoapFormatter;
import org.radixware.kernel.common.utils.SoapFormatter.ResponseTraceItem;
import org.radixware.kernel.common.utils.io.pipe.BidirectionalPipe;
import org.radixware.kernel.common.utils.io.pipe.BytePipe;
import org.radixware.kernel.common.utils.io.pipe.BytePipeSink;
import org.radixware.kernel.common.utils.io.pipe.BytePipeSink.Listener;
import org.radixware.kernel.common.utils.io.pipe.BytePipeSource;
import org.xmlsoap.schemas.soap.envelope.Fault;


class JmsClientPipe extends BidirectionalPipe {

    private static final String SOAP_TO_JMS_BINDING_SCHEMA_URI = "http://www.w3.org/2010/soapjms/";
    private static final String UNSUPPORTED_JMS_MESSAGE_FORMAT_FAULT = "unsupportedJMSMessageFormat";
    private final ArrayBlockingQueue<Message> pendingMessageQueue;

    public JmsClientPipe(final Message message, final javax.jms.Connection connection, final JmsObjectsFactory jmsObjectsFacotry, final LocalTracer tracer) throws JMSException {
        this(new ArrayBlockingQueue<>(1, false, Collections.singleton(message)), new ArrayBlockingQueue<Message>(1), connection, jmsObjectsFacotry, tracer);
    }

    private JmsClientPipe(final ArrayBlockingQueue<Message> pendingMessageQueue, final ArrayBlockingQueue<Message> messageToResponseQueue, final javax.jms.Connection connection, final JmsObjectsFactory jmsObjectsFacotry, final LocalTracer tracer) throws JMSException {
        super(new InputPipe(connection.createSession(false, Session.CLIENT_ACKNOWLEDGE), jmsObjectsFacotry, messageToResponseQueue), new OutputPipe(pendingMessageQueue, messageToResponseQueue));
        this.pendingMessageQueue = pendingMessageQueue;
        ((OutputPipe) this.getOutputPipe()).setClientPipe(this);
    }

    @Override
    public boolean isConnected() {
        return !((InputPipe) getInputPipe()).isClosed();
    }

    @Override
    public void finishConnect() {
        super.finishConnect();
    }

    public boolean offerMessage(final Message message) {
        if (!isConnected() || !pendingMessageQueue.offer(message)) {
            return false;
        }
        ((OutputPipe) getOutputPipe()).notifyDataAvailable();
        return true;
    }

    private static class OutputPipe implements BytePipe {

        private byte[] inputBytes = null;
        private int inputPos = 0;
        private final ArrayBlockingQueue<Message> pendingMessageQueue;
        private final ArrayBlockingQueue<Message> messageToResponseQueue;
        private final List<Listener> sinkListeners = new CopyOnWriteArrayList<>();
        private long readTimeoutMillis = -1;
        private JmsClientPipe clientPipe;

        public OutputPipe(final ArrayBlockingQueue<Message> pendingMessageQueue, final ArrayBlockingQueue<Message> messageToResponseQueue) throws JMSException {
            checkMessageType(pendingMessageQueue.peek());
            this.pendingMessageQueue = pendingMessageQueue;
            this.messageToResponseQueue = messageToResponseQueue;

        }

        public void notifyDataAvailable() {
            inputBytes = null;
            inputPos = 0;
            for (Listener listener : sinkListeners) {
                listener.dataAvailable();
            }
        }

        public void setClientPipe(JmsClientPipe clientPipe) {
            //store reference to clientPipe so it will not be garbage collected
            //if connection is kept
            this.clientPipe = clientPipe;
        }

        @Override
        public BytePipeSource getSource() {
            return new BytePipeSource() {
                @Override
                public int write(ByteBuffer data) throws IOException {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public int write(ByteBuffer data, long timeOutMillis) throws IOException {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public int writeNow(ByteBuffer data) throws IOException {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public void setWriteTimeOut(long timeOutMillis) {
                }

                @Override
                public boolean isReadyForWrite() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public void addListener(BytePipeSource.Listener listener) {
                }

                @Override
                public void removeListener(BytePipeSource.Listener listener) {
                }

                @Override
                public void close() throws IOException {
                }
            };
        }

        @Override
        public BytePipeSink getSink() {
            return new BytePipeSink() {
                @Override
                public int read(ByteBuffer destination) throws IOException {
                    return read(destination, -1);
                }

                @Override
                public int read(ByteBuffer destination, long timeOutMillis) throws IOException {
                    if (timeOutMillis < 0) {
                        timeOutMillis = readTimeoutMillis;
                    }
                    if (timeOutMillis < 0) {
                        timeOutMillis = Long.MAX_VALUE;
                    }
                    return readIncomingMessage(destination, timeOutMillis);
                }

                @Override
                public int readNow(ByteBuffer destination) throws IOException {
                    return read(destination, 0);
                }

                @Override
                public void setReadTimeOut(long timeOutMillis) {
                    readTimeoutMillis = timeOutMillis;
                }

                @Override
                public boolean isDataAvailable() {
                    return inputPos != -1;
                }

                @Override
                public void addListener(Listener listener) {
                    sinkListeners.add(listener);
                }

                @Override
                public void removeListener(Listener listener) {
                    sinkListeners.remove(listener);
                }

                @Override
                public void close() throws IOException {
                }
            };
        }

        private int readIncomingMessage(final ByteBuffer dst, final long timeoutMillis) throws IOException {
            if (inputBytes == null) {
                try {
                    final Message message = pendingMessageQueue.poll(timeoutMillis, TimeUnit.MILLISECONDS);
                    if (message == null) {
                        if (timeoutMillis == 0) {
                            return 0;
                        } else {
                            throw new IOException("Read timeout");
                        }
                    }
                    messageToResponseQueue.add(message);
                    inputBytes = JmsUtils.convertJmsMessageToHttpSoap(message);
                    try {
                        message.acknowledge();
                    } catch (JMSException ex) {
                        throw new IOException(ex);
                    }
                } catch (InterruptedException ex) {
                    throw new InterruptedIOException("Interrupted while waiting for next incoming message");
                }
            }
            if (inputPos == -1) {
                return -1;
            }
            int readCount = Math.min(dst.remaining(), inputBytes.length);
            dst.put(inputBytes, inputPos, readCount);
            inputPos += readCount;
            if (inputPos == inputBytes.length) {
                inputBytes = null; //not needed anymore, free memory
                inputPos = -1; //next time EOF will be returned
            }
            return readCount;
        }

        @Override
        public String toString() {
            return "JmsOutputPipe@" + Integer.toHexString(hashCode());
        }
        
    }

    private static class InputPipe implements BytePipe {

        private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        private final Session session;
        private final ArrayBlockingQueue<Message> messageToResponseQueue;
        private final JmsObjectsFactory factory;
        private boolean closed = false;

        public InputPipe(final Session session, final JmsObjectsFactory factory, final ArrayBlockingQueue<Message> messageToResponseQueue) throws JMSException {
            this.session = session;
            this.messageToResponseQueue = messageToResponseQueue;
            this.factory = factory;
        }

        public boolean isClosed() {
            return closed;
        }

        @Override
        public BytePipeSource getSource() {
            return new BytePipeSource() {
                @Override
                public int write(ByteBuffer destination) throws IOException {
                    return writeNow(destination);
                }

                @Override
                public int write(ByteBuffer destination, long timeOutMillis) throws IOException {
                    return writeNow(destination);
                }

                @Override
                public int writeNow(ByteBuffer destination) throws IOException {
                    int result = writeToResponse(destination);
                    try {
                        sendResponseMessage();
                    } catch (JMSException ex) {
                        throw new IOException(ex);
                    }
                    return result;
                }

                @Override
                public void setWriteTimeOut(long timeOutMillis) {
                }

                @Override
                public void addListener(BytePipeSource.Listener listener) {
                }

                @Override
                public void removeListener(BytePipeSource.Listener listener) {
                }

                @Override
                public void close() throws IOException {
                    if (closed) {
                        return;
                    }
                    try {
                        try {
                            session.close();
                        } catch (JMSException ex) {
                            throw new IOException(ex);
                        }
                    } finally {
                        closed = true;
                    }
                }

                @Override
                public boolean isReadyForWrite() {
                    return false;
                }
            };
        }

        @Override
        public BytePipeSink getSink() {
            return new BytePipeSink() {
                @Override
                public int read(ByteBuffer destination) throws IOException {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public int read(ByteBuffer destination, long timeOutMillis) throws IOException {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public int readNow(ByteBuffer destination) throws IOException {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public void setReadTimeOut(long timeOutMillis) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public boolean isDataAvailable() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public void addListener(Listener listener) {
                }

                @Override
                public void removeListener(Listener listener) {
                }

                @Override
                public void close() throws IOException {
                }
            };
        }

        private int writeToResponse(ByteBuffer src) throws IOException {
            final byte[] buf = new byte[1024];
            final int written = src.remaining();
            while (src.hasRemaining()) {
                final int transferedBytesCount = Math.min(buf.length, src.remaining());
                src.get(buf, 0, transferedBytesCount);
                outputStream.write(buf, 0, transferedBytesCount);
            }
            return written;
        }

        private void sendResponseMessage() throws JMSException, IOException {
            final Message message = messageToResponseQueue.peek();
            if (message == null) {
                throw new IOException("Unable to response to the null message");
            }
            final MessageProducer producer = session.createProducer(factory.createResponseDestination(session, message));
            if (producer == null) {//message.getJMSReplyTo() is null and no default responce queue defined
                return;
            }
            if (outputStream.size() == 0) {
                SoapFormatter.sendFault(outputStream, new QName(SOAP_TO_JMS_BINDING_SCHEMA_URI, UNSUPPORTED_JMS_MESSAGE_FORMAT_FAULT), "Error on message recieving", NOOP_WRITER, null);
            }
            final Message responseMessage;
            try {
                responseMessage = JmsUtils.convertHttpSoapMessageToJms(outputStream.toByteArray(), session, message instanceof TextMessage ? EJmsMessageFormat.TEXT : EJmsMessageFormat.BIN);
            } catch (EOFException ex) {
                //not all data has been written, wait for other portion
                return;
            }
            try {
                final String keepAlivePropName = JmsUtils.encodeHttpAttrName(EHttpParameter.HTTP_CONNECTION_ATTR);
                if (message.propertyExists(keepAlivePropName)) {
                    //incoming message has keep-alive flag that has been dropped off because server doesnt't support it.
                    //However client still may want to keep connection with broker, so we must restore keep-alive flag.
                    responseMessage.setStringProperty(keepAlivePropName, message.getStringProperty(keepAlivePropName));
                }
                if (message.getJMSCorrelationID() == null || message.getJMSCorrelationID().isEmpty()) {
                    responseMessage.setJMSCorrelationID(message.getJMSMessageID());
                } else {
                    responseMessage.setJMSCorrelationID(message.getJMSCorrelationID());
                }
                producer.send(responseMessage);
            } finally {
                outputStream.reset();
                messageToResponseQueue.clear();
            }

        }
        private static final SoapFormatter.FaultDetailWriter NOOP_WRITER = new SoapFormatter.FaultDetailWriter() {
            @Override
            public void writeTo(Fault fault, List<ResponseTraceItem> requestProcessingTraceItems) {
                //do nothing
            }
        };
        
        @Override
        public String toString() {
            return "JmsInputPipe@" + Integer.toHexString(hashCode());
        }
    }

    private static void checkMessageType(final Message message) throws JMSException {
        if (message == null) {
            return;
        }
        if (!(message instanceof TextMessage || message instanceof BytesMessage)) {
            throw new IllegalArgumentException("Only Text and Byte messages are supported. Actual class of the message: " + message.getClass().getName());

        }
    }
}
