/*
 * Copyright (c) 2008-2018, Compass Plus Limited. All rights reserved.
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
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import org.apache.commons.lang.StringUtils;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.server.exceptions.MQSubscriptionException;
import org.radixware.kernel.server.units.mq.MqJmsConsumer.JmsMqMessage;
import org.radixware.kernel.server.units.mq.interfaces.IMqQueueConsumer;
import org.radixware.schemas.messagequeue.MqProcessRqDocument;
import org.radixware.schemas.messagequeue.MqProcessRs;

public class MqJmsConsumer implements IMqQueueConsumer<JmsMqMessage, MqJmsConsumer.JmsMqPartition>, JmsDelegate.JmsUnit {
    
    private static final int POLL_TIMEOUT_MILLIS = SystemPropUtils.getIntSystemProp("rdx.mq.jms.poll.timeout.millis", -1);
    
    private final JmsDelegate delegate;
    
    private final long processorId;
    private final MqUnit unit;
    private final MqMessageRepo<JmsMqMessage, JmsMqPartition> messageRepo;
    private final String debugKey;
    private boolean canSelectMessages = true;
    private boolean closed = false;
    private final MqDbQueries.MqUnitOptions options;
    
    @Override
    public void logDebug(String s) {
        unit.getTrace().put(EEventSeverity.DEBUG, s, EEventSource.MQ_HANDLER_UNIT);
    }
    
    @Override
    public void logEvent(String s) {
        unit.getTrace().put(EEventSeverity.EVENT, s, EEventSource.MQ_HANDLER_UNIT);
    }
    
    @Override
    public void logError(String s) {
        unit.getTrace().put(EEventSeverity.ERROR, s, EEventSource.MQ_HANDLER_UNIT);
    }

    public MqJmsConsumer(final MqUnit unit, final MqDbQueries.MqUnitOptions options) throws IOException {
        this.processorId = options.processorId;
        this.unit = unit;
        messageRepo = new MqMessageRepo<>(this, unit, 1);
        debugKey = "JMS '" + options.queueId + ") " + options.queueTitle + "'";
        this.options = options;
        this.delegate = new JmsDelegate(this, "Unit #" + unit.getId());
        
        try {
            delegate.createJndiContext(options.jndiInitialContextFactory, options.jndiProviderUrl, options.jndiOptions);
            delegate.createConnectionFactory(options.jmsJndiConnFactoryName, options.jmsConnFactoryClassName, options.jmsConnFactoryOptions);
            
            delegate.startAntiFreeze();
            delegate.createConnection(options.login, options.password, options.jmsClientId);
            delegate.createSession();
            delegate.createTopic(options.jndiQueueName, options.queueName);
            delegate.createConsumer(options.jmsSubscriptionName);
        } catch (Exception ex) {
            delegate.closeSilent();
            throw new IOException(ex);
        } finally {
            delegate.stopAntiFreeze();
        }
    }
    
    @Override
    public MqDbQueries.MqUnitOptions getOptions() {
        return options;
    }

    @Override
    public void close() throws IOException {
        try {
            messageRepo.close();
            delegate.close();
        } catch (JMSException ex) {
            throw new IOException(ex);
        } finally {
            closed = true;
        }
    }

    @Override
    public boolean isClosed() {
        return closed;
    }
    
    @Override
    public boolean poll() throws Exception {
        Message message = null;
        
        if (!unit.isShuttingDown() && canSelectMessages && (message = delegate.poll(POLL_TIMEOUT_MILLIS)) != null) {
            if (message instanceof TextMessage || message instanceof BytesMessage) {
                final JmsMqMessage msg = new JmsMqMessage(unit, this, message, System.currentTimeMillis());
                unit.logMessageReceived(msg);
                messageRepo.storeMessage(msg);
            } else {
                message.acknowledge();
                throw new MQSubscriptionException("Unsupported message class: " + message.getClass().getCanonicalName());
            }
        }
        
        return message != null;
    }

    @Override
    public void maintenance() {
        pushMessages();
    }
    
    private void pushMessages() {
        if (!unit.isShuttingDown()) {
            while (unit.getAasClientPool().getFreeCount() > 0) {
                final JmsMqMessage msg = messageRepo.getAnyMessage();

                if (msg == null) {
                    break;
                } else {
                    final Message jmsMsg = msg.getCargo();
                    
                    final MqProcessRqDocument xProcessRqDoc = MqProcessRqDocument.Factory.newInstance();
                    final org.radixware.schemas.messagequeue.MqMessage xMessage = xProcessRqDoc.addNewMqProcessRq().addNewMessage();

                    xMessage.ensureMeta().setQueueName(options.queueName);
                    
                    String messageId;
                    try {
                        messageId = jmsMsg.getJMSMessageID();
                    } catch (JMSException ex) {
                        messageId = null;
                    }
                    
                    if (StringUtils.isNotEmpty(messageId)) {
                        xMessage.ensureMeta().setMessageId(messageId);
                    }

                    xMessage.ensureMeta().setDebugKey(msg.getDebugKey());
                    if (msg.hasBodyStr()) {
                        xMessage.ensureBody().setStrBody(msg.getBodyStr());
                    } else {
                        xMessage.ensureBody().setByteBody(msg.getBodyBytes());
                    }

                    unit.invoke(msg, xProcessRqDoc, processorId);
                }
            }
        }
    }

    @Override
    public void onResponse(JmsMqMessage message, MqProcessRs xProcessRs) {
        try {
            message.getCargo().acknowledge();
            unit.logCommit(message);
        } catch (JMSException ex) {
            unit.onCommitFailure(message, ex);
            return;
        }
        messageRepo.remove(message);
        messageRepo.releaseQueue(message);
        unit.subscribeImmediateTimerEvent();
    }

    @Override
    public void onFailToSendAasRequest(JmsMqMessage message, Exception ex) {
        messageRepo.releaseQueueAfter(message, MqUnit.PAUSE_AFTER_INVOKE_EX_MILLIS);
    }

    @Override
    public void onAasException(JmsMqMessage message, Exception ex) {
        messageRepo.releaseQueueAfter(message, MqUnit.PAUSE_AFTER_PROCESSING_EX_MILLIS);
    }

    @Override
    public void requestStopAsync() {
        // do nothing
    }

    @Override
    public void pause(JmsMqPartition parameter) {
    }

    @Override
    public void resume(JmsMqPartition parameter) {
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

    @Override
    public String debugKey() {
        return debugKey;
    }

    public static final class JmsMqPartition {
        public static final JmsMqPartition INSTANCE = new JmsMqPartition();
        
        private JmsMqPartition() {
        }

        @Override
        public String toString() {
            return "JmsMqPartition";
        }
    }
    
    public static class JmsMqMessage extends MqMessage<Message, JmsMqPartition> {

        private final static ThreadLocal<SimpleDateFormat> sdf = new ThreadLocal<SimpleDateFormat>() {
            @Override
            protected SimpleDateFormat initialValue() {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            }
        };
        
        private final String debugKey;
        
        public JmsMqMessage(MqUnit unit, IMqQueueConsumer consumer, Message cargo, long regTimeMillis) throws JMSException {
            super(unit, consumer, cargo, regTimeMillis);
            debugKey = "(JMSMessageID = " + cargo.getJMSMessageID() + ", JMSType = " + cargo.getJMSType()
                    + ", JMSPriority = " + cargo.getJMSPriority() + ", JMSRedelivered = " + cargo.getJMSRedelivered()
                    + ", JMSTimestamp = " + (cargo.getJMSTimestamp() == 0 ? "none" : sdf.get().format(new Date(cargo.getJMSTimestamp()))) + ")";
        }

        @Override
        public JmsMqPartition getPartitionId() {
            return JmsMqPartition.INSTANCE;
        }

        @Override
        public String getDebugKey() {
            return debugKey;
        }

        @Override
        public byte[] getBodyBytes() {
            final Message m = getCargo();
            
            try {
                if (m instanceof TextMessage) {
                    final TextMessage tm = (TextMessage)m;
                    return tm.getText().getBytes();
                } else {
                    final BytesMessage bm = (BytesMessage) m;
                    final int length = (int)(bm.getBodyLength() & 0x7FFFFFFFL);
                    final byte[] result = new byte[length];
                    bm.readBytes(result);
                    return result;
                }
            } catch (JMSException ex) {
                throw new RuntimeException(ex);
            }
        }
        
        @Override
        public boolean hasBodyStr() {
            final Message m = getCargo();
            return m instanceof TextMessage;
        }
        
        @Override
        public String getBodyStr() {
            if (hasBodyStr()) {
                try {
                    final TextMessage message = (TextMessage) getCargo();
                    return message.getText();
                } catch (JMSException ex) {
                    throw new RuntimeException(ex);
                }
            }
            return null;
        }
    }
}
