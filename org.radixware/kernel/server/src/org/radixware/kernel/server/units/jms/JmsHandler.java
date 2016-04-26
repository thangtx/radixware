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

package org.radixware.kernel.server.units.jms;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Clob;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.*;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.exceptions.AppError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.Maps;
import org.radixware.kernel.common.utils.net.JmsUtils;
import org.radixware.kernel.common.utils.net.RadixJmsConnectionOptions;
import org.radixware.schemas.jmshandler.Base;


public final class JmsHandler {

    private Destination rqQueue = null;
    private Destination rsQueue = null;
    private Connection connection = null;
    private Session session = null;
    private MessageConsumer receiver = null;
    private MessageProducer sender = null;
    private final JmsHandlerUnit unit;
    private String connectionOptions = null;
    private Map<String, String> messProps;

    public JmsHandler(final JmsHandlerUnit unit) {
        this.unit = unit;
    }

    public boolean canSend() {
        return sender != null;
    }

    public boolean canReceive() {
        return receiver != null;
    }
    static PrintStream out = null;
    static PrintStream err = null;

    static void lockStdStreams(boolean lock) {
        if (lock) {
            out = System.out;
            err = System.err;
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final PrintStream ps = new PrintStream(baos);
            System.setOut(ps);
            System.setErr(ps);
        } else {
            System.setOut(out);
            System.setErr(err);
        }
    }

    static void lockErrStream(boolean lock) {
        if (lock) {
            System.err.close();
        } else {
        }
    }

    private String getString(final Clob clob) {
        try {
            if (clob != null) {
                return clob.getSubString(1l, (int) clob.length());
            }
        } catch (Exception e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            unit.getTrace().put(EEventSeverity.ERROR, JmsHandlerMessages.ERR_ON_JMS_PROPS_PARSE + exStack, JmsHandlerMessages.MLS_ID_ERR_PROPS_PARSE, new ArrStr(""), unit.getEventSource(), false);
        }
        return null;
    }

    public boolean start() {
        boolean res = true;
        try {
            lockErrStream(true);

            messProps = JmsUtils.parseProps(getString(unit.getOptions().jmsMessProps));
            connectionOptions = getString(unit.getOptions().jmsConnectProps);

            connection = JmsHandlerUnitConnectionFactory.createConnection(new RadixJmsConnectionOptions() {
                @Override
                public String getConnectionOptions() {
                    return connectionOptions;
                }

                @Override
                public String getJmsLogin() {
                    return unit.getOptions().jmsLogin;
                }

                @Override
                public String getJmsPassword() {
                    return unit.getOptions().jmsPassword;
                }
            });

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            final String rqQueueName = unit.getOptions().isClient.booleanValue()
                    ? unit.getOptions().msRqQueueName : unit.getOptions().msRsQueueName;
            final String rsQueueName = unit.getOptions().isClient.booleanValue()
                    ? unit.getOptions().msRsQueueName : unit.getOptions().msRqQueueName;

            if (rqQueueName != null) {
                rqQueue = session.createQueue(rqQueueName);
                sender = session.createProducer(rqQueue);
            }

            if (rsQueueName != null) {
                rsQueue = session.createQueue(rsQueueName);
                receiver = session.createConsumer(rsQueue);
            }

            connection.start();
        } catch (Exception e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            unit.getTrace().put(EEventSeverity.ERROR, JmsHandlerMessages.ERR_ON_JMS_START + exStack, JmsHandlerMessages.MLS_ID_ERR_ON_JMS_START, new ArrStr(unit.getTitle(), exStack), unit.getEventSource(), false);
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (JMSException ex) {
                // do nothing\
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            rqQueue = null;
            rsQueue = null;
            connection = null;
            session = null;
            receiver = null;
            sender = null;
            res = false;
        } finally {
            lockErrStream(false);
        }
        return res;
    }

    public void stop() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (JMSException ex) {
            // do nothing
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
        }
        rqQueue = null;
        rsQueue = null;
        connection = null;
        session = null;
        receiver = null;
        sender = null;
    }

    public boolean restart() {
        stop();
        return start();
    }
    /*
     private void prepareMessage(final Message msg, final Base info) throws JMSException {
     if (info == null)
     return;
        
     if (info.isSetCorrelationId())
     msg.setJMSCorrelationID(info.getCorrelationId());
     if (info.isSetDeliveryMode())
     msg.setJMSDeliveryMode(info.getDeliveryMode().intValue());
     if (info.isSetExpiration())
     msg.setJMSExpiration(info.getExpiration());
     if (info.isSetMessageId())
     msg.setJMSMessageID(info.getMessageId());
     if (info.isSetPriority())
     msg.setJMSPriority(info.getPriority().intValue());
     if (info.isSetRedelivered())
     msg.setJMSRedelivered(info.getRedelivered());
     if (info.isSetTimestamp())
     msg.setJMSTimestamp(info.getTimestamp());
     if (info.isSetType())
     msg.setJMSType(info.getType());
        
     final Map<String, String> props = Maps.fromXml(info.getProps());
     if (props != null) {
     for (Map.Entry<String, String> e : props.entrySet()) {
     msg.setStringProperty(e.getKey(), e.getValue());
     }
     }
     }
     */

    public BytesMessage sendBytes(final byte[] bytes, final Base info) throws JMSException {
        return sendBytes(bytes, info, null);
    }

    public BytesMessage sendBytes(final byte[] bytes, final Base info, final String corrMessId) throws JMSException {
        if (sender == null) {
            throw new AppError("Jms sender not initialized");
        }
        try {
            final BytesMessage msg = session.createBytesMessage();
            msg.writeBytes(bytes);

            if (messProps != null) {
                for (Map.Entry<String, String> e : messProps.entrySet()) {
                    msg.setStringProperty(e.getKey(), e.getValue());
                }
            }

            JmsHandlerUtils.prepareMessage(msg, info);
            if (corrMessId != null) {
                msg.setJMSCorrelationID(corrMessId);
            }

            sender.send(msg);
            return msg;
        } catch (JMSException e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            unit.getTrace().put(EEventSeverity.ERROR, JmsHandlerMessages.ERR_ON_JMS_IO + exStack, JmsHandlerMessages.MLS_ID_ERR_ON_JMS_IO, new ArrStr(unit.getTitle(), exStack), unit.getEventSource(), false);
            throw e;
        }
    }

    public TextMessage sendText(final String text, final Base info) throws JMSException {
        return sendText(text, info, null);
    }

    public TextMessage sendText(final String text, final Base info, final String corrMessId) throws JMSException {
        if (sender == null) {
            throw new AppError("Jms sender not initialized");
        }
        try {
            final TextMessage msg = session.createTextMessage();
            msg.setText(text);

            if (messProps != null) {
                for (Map.Entry<String, String> e : messProps.entrySet()) {
                    msg.setStringProperty(e.getKey(), e.getValue());
                }
            }

            JmsHandlerUtils.prepareMessage(msg, info);
            if (corrMessId != null) {
                msg.setJMSCorrelationID(corrMessId);
            }

            sender.send(msg);
            return msg;
        } catch (JMSException e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            unit.getTrace().put(EEventSeverity.ERROR, JmsHandlerMessages.ERR_ON_JMS_IO + exStack, JmsHandlerMessages.MLS_ID_ERR_ON_JMS_IO, new ArrStr(unit.getTitle(), exStack), unit.getEventSource(), false);
            throw e;
        }
    }

    public void process() {
        try {
            while (!unit.getAasClientPool().isBusy()) {
                final Message msg = receiver.receiveNoWait();
                if (msg != null) {
                    unit.onMessage(msg);
                } else {
                    break;
                }
            }
        } catch (JMSException e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            unit.getTrace().put(EEventSeverity.ERROR, JmsHandlerMessages.ERR_ON_JMS_IO + exStack, JmsHandlerMessages.MLS_ID_ERR_ON_JMS_IO, new ArrStr(unit.getTitle(), exStack), unit.getEventSource(), false);
        }
    }
}