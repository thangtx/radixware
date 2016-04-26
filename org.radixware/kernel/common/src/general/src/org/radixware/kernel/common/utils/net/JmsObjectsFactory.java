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

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.*;
import javax.naming.InitialContext;
import org.radixware.kernel.common.enums.EJmsOption;


public class JmsObjectsFactory {

    private final ConnectionFactory connectionFactory;
    private final Destination jndiInboundDestination;
    private final Destination jndiDefaultOutboundDestination;
    private final String inboundQueueName;
    private final String defaultOutboundQueueName;
    private final Map<String, String> options;

    public JmsObjectsFactory(final String optionsString) throws InvalidJmsConfigurationException {
        try {
            if (optionsString == null || optionsString.isEmpty()) {
                throw new InvalidJmsConfigurationException("Options for constructing JMS objects should be defined");
            }
            options = JmsUtils.parseProps(optionsString);
            if (options.containsKey(EJmsOption.JNDI_INITIAL_CONTEXT_FACTORY.getValue())
                    && options.containsKey(EJmsOption.CONNECTION_FACTORY_JNDI_NAME.getValue())) {
                final Properties p = new Properties();
                p.put(EJmsOption.JNDI_INITIAL_CONTEXT_FACTORY.getValue(), options.get(EJmsOption.JNDI_INITIAL_CONTEXT_FACTORY.getValue()));
                if (options.containsKey(EJmsOption.JNDI_PROVIDER_URL.getValue())) {
                    p.put(EJmsOption.JNDI_PROVIDER_URL.getValue(), options.get(EJmsOption.JNDI_PROVIDER_URL.getValue()));
                }
                final InitialContext ctx = new InitialContext(p);
                connectionFactory = (ConnectionFactory) ctx.lookup(options.get(EJmsOption.CONNECTION_FACTORY_JNDI_NAME.getValue()));
                if (connectionFactory == null) {
                    throw new InvalidJmsConfigurationException("There is no connection factory with JNDI name '" + EJmsOption.CONNECTION_FACTORY_JNDI_NAME.getValue() + "'");
                }
                applyOptionsToFactory(connectionFactory, options);
                if (options.containsKey(EJmsOption.INBOUND_DESTINATION_JNDI_NAME.getValue())) {
                    final String inboundDestName = options.get(EJmsOption.INBOUND_DESTINATION_JNDI_NAME.getValue());
                    jndiInboundDestination = (Destination) ctx.lookup(inboundDestName);
                    if (jndiInboundDestination == null) {
                        throw new InvalidJmsConfigurationException("There is no destination with JNDI name '" + inboundDestName + "'");
                    }
                    inboundQueueName = null;
                } else if (options.containsKey(EJmsOption.INBOUND_QUEUE_NAME.getValue())) {
                    inboundQueueName = options.get(EJmsOption.INBOUND_QUEUE_NAME.getValue());
                    if (inboundQueueName == null) {
                        throw new InvalidJmsConfigurationException("Inbound queue name can not be null");
                    }
                    jndiInboundDestination = null;
                } else {
                    throw new InvalidJmsConfigurationException("Inbound destination JNDI name or inbound queue name should be defined");
                }
                if (options.containsKey(EJmsOption.OUTBOUND_DESTINATION_JNDI_NAME.getValue())) {
                    final String defaultOutboundDestName = EJmsOption.OUTBOUND_DESTINATION_JNDI_NAME.getValue();
                    jndiDefaultOutboundDestination = (Destination) ctx.lookup(defaultOutboundDestName);
                    if (jndiDefaultOutboundDestination == null) {
                        throw new InvalidJmsConfigurationException("There is no destination with JNDI name '" + defaultOutboundDestName + "'");
                    }
                    defaultOutboundQueueName = null;
                } else if (options.containsKey(EJmsOption.OUTBOUND_QUEUE_NAME.getValue())) {
                    defaultOutboundQueueName = options.get(EJmsOption.OUTBOUND_QUEUE_NAME.getValue());
                    if (defaultOutboundQueueName == null) {
                        throw new InvalidJmsConfigurationException("Outbound queue name can not be null");
                    }
                    jndiDefaultOutboundDestination = null;
                } else {
                    jndiDefaultOutboundDestination = null;
                    defaultOutboundQueueName = null;
                }
            } else if (options.containsKey(EJmsOption.CONNECTION_FACTORY_CLASS_NAME.getValue())) {
                final ClassLoader classLoader = JmsObjectsFactory.class.getClassLoader();
                final Class<?> clazz = classLoader.loadClass(options.get(EJmsOption.CONNECTION_FACTORY_CLASS_NAME.getValue()));
                connectionFactory = (ConnectionFactory) clazz.newInstance();
                applyOptionsToFactory(connectionFactory, options);
                if (options.containsKey(EJmsOption.INBOUND_QUEUE_NAME.getValue())) {
                    inboundQueueName = options.get(EJmsOption.INBOUND_QUEUE_NAME.getValue());
                    if (inboundQueueName == null) {
                        throw new InvalidJmsConfigurationException("Inbound queue name can not be null");
                    }
                    jndiInboundDestination = null;
                } else {
                    throw new InvalidJmsConfigurationException("Inbound queue name should be defined");
                }
                if (options.containsKey(EJmsOption.OUTBOUND_QUEUE_NAME.getValue())) {
                    defaultOutboundQueueName = options.get(EJmsOption.OUTBOUND_QUEUE_NAME.getValue());
                    if (defaultOutboundQueueName == null) {
                        throw new InvalidJmsConfigurationException("Outbound queue name can not be null");
                    }
                    jndiDefaultOutboundDestination = null;
                } else {
                    jndiDefaultOutboundDestination = null;
                    defaultOutboundQueueName = null;
                }
            } else {
                throw new InvalidJmsConfigurationException("JNDI settings for discovering ConnectionFactory or connection factory class name should be defined");
            }
        } catch (InvalidJmsConfigurationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InvalidJmsConfigurationException(ex);
        }

    }

    public Connection createConnection() throws JMSException {
        final String jmsLogin = options.get(EJmsOption.JMS_LOGIN.getValue());
        final String jmsPassword = options.get(EJmsOption.JMS_PASSWORD.getValue());
        if ((jmsLogin != null && !jmsLogin.isEmpty()) || (jmsPassword != null && !jmsPassword.isEmpty())) {
            return connectionFactory.createConnection(jmsLogin, jmsPassword);
        } else {
            return connectionFactory.createConnection();
        }
    }

    public Destination createResponseDestination(final Session session, final Message message) throws JMSException {
        if (message != null && message.getJMSReplyTo() != null) {
            return message.getJMSReplyTo();
        }
        if (jndiDefaultOutboundDestination != null) {
            return jndiInboundDestination;
        }
        if (defaultOutboundQueueName != null) {
            return session.createQueue(defaultOutboundQueueName);
        }
        return null;
    }

    public Destination createRequestDestination(final Session session) throws JMSException {
        if (jndiInboundDestination != null) {
            return jndiInboundDestination;
        }
        return session.createQueue(inboundQueueName);
    }

    private void applyOptionsToFactory(final ConnectionFactory factory, final Map<String, String> options) {
        if (factory != null && options != null) {
            try {
                Method setPropMethod = factory.getClass().getMethod("setProperty", String.class, String.class);
                for (Map.Entry<String, String> e : options.entrySet()) {
                    setPropMethod.invoke(factory, e.getKey(), e.getValue());
                }
            } catch (Exception ex) {
                //do nothing
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }
    }

    public static class InvalidJmsConfigurationException extends Exception {

        public InvalidJmsConfigurationException(Throwable cause) {
            super(cause);
        }

        public InvalidJmsConfigurationException(String message, Throwable cause) {
            super(message, cause);
        }

        public InvalidJmsConfigurationException(String message) {
            super(message);
        }
    }
}
