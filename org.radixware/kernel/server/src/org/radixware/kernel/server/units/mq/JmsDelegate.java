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
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.commons.lang.StringUtils;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.net.JmsObjectsFactory;

public class JmsDelegate {
    
    final JmsUnit unit;
    final String title;
    Context jndiCtx = null;
    boolean useTopic = true;

    ConnectionFactory connFactory = null;
    Connection connection = null;
    public Session session = null;
    Destination topic = null;
    public MessageConsumer consumer = null;
    public MessageProducer producer = null;
    private final ExecutorService antiFreezeExecutor;
    
    private static final boolean ANTI_FREEZE_DEBUG_ENABLED = SystemPropUtils.getBooleanSystemProp("rdx.jms.antifreeze.debug", false);
    private static final long ANTI_FREEZE_TIMEOUT_MILLIS = SystemPropUtils.getLongSystemProp("rdx.jms.antifreeze.timeout.millis", 30000L);

    class AntiFreezeRunnable implements Runnable {
        
        private final long timeoutMillis = ANTI_FREEZE_TIMEOUT_MILLIS;
        private final CountDownLatch latch = new CountDownLatch(1);
        private volatile boolean cancelled = false;
        private volatile boolean creatorThreadInterrupted = false;
        private final Thread creatorThread;
        
        AntiFreezeRunnable(Thread srcThread) {
            this.creatorThread = srcThread;
        }

        @Override
        public void run() {
            logDebug("[jms antifreeze] started");
            try {
                latch.await(timeoutMillis, TimeUnit.MILLISECONDS);
            } catch (InterruptedException ex) {
                logDebug("[jms antifreeze] interrupted");
                return;
            }
            if (!cancelled) {
                try {
                    logDebug("[jms antifreeze] creatorThread.interrupt() ....");
                    creatorThreadInterrupted = true;
                    creatorThread.interrupt();
                    logDebug("[jms antifreeze] connection.stop() ...");
                    connection.stop();
                    logDebug("[jms antifreeze] connection.close() ...");
                    connection.close();
                    logDebug("[jms antifreeze] done");
                } catch (JMSException ex) {
                    logDebug("[jms antifreeze] done with exception: " + ExceptionTextFormatter.exceptionStackToString(ex));
                }
            } else {
                logDebug("[jms antifreeze] cancelled");
            }
        }
        
        public void cancel() {
            cancelled = true;
            latch.countDown();
            if (creatorThreadInterrupted) {
                Thread.interrupted();
            }
        }
        
    }
    
    static class AntiFreezeThread extends Thread {
        AntiFreezeThread(Runnable runnable, String title) {
            super(runnable, title);
        }
    }
    
    static class AntiFreezeThreadFactory implements ThreadFactory {
        final String title;
        
        AntiFreezeThreadFactory(String title) {
            this.title = title;
        }

        @Override
        public Thread newThread(Runnable r) {
            final Thread t = new AntiFreezeThread(r, "JMS antifreeze for '" + title + "'");
            t.setDaemon(true);
            return t;
        }
        
    }
    
    private AntiFreezeRunnable antiFreezeRunnable = new AntiFreezeRunnable(Thread.currentThread());
    
    public JmsDelegate(JmsUnit unit, final String title) {
        this.unit = unit;
        this.title = title;
        final ThreadFactory threadFactory = new AntiFreezeThreadFactory(title);
        this.antiFreezeExecutor = Executors.newFixedThreadPool(1, threadFactory);
    }
    
    @Override
    protected void finalize() throws Throwable {
        antiFreezeExecutor.shutdown();
        super.finalize();
    }
    
    private void logDebug(String s) {
        if (ANTI_FREEZE_DEBUG_ENABLED) {
            unit.logDebug(s);
        }
    }
    
    private void logEvent(String s) {
        unit.logEvent(s);
    }
    
    private void logError(String s) {
        unit.logError(s);
    }
    
    public void createJndiContext(String initialContextFactory, String providerUrl, Map<String, String> options) {
        if (StringUtils.isNotBlank(initialContextFactory) && StringUtils.isNotBlank(providerUrl)) {
            try {
                Hashtable<String, String> envProps = new Hashtable<>();
                envProps.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
                envProps.put(Context.PROVIDER_URL, providerUrl);
                if (options != null) {
                    envProps.putAll(options);
                }
                jndiCtx = new InitialContext(envProps);
                logEvent("JNDI InitialContext created with properties: " + envProps);
            } catch (NamingException ex) {
                logError("JNDI InitialContext creation failed:\n" + ExceptionTextFormatter.exceptionStackToString(ex));
                logEvent("JNDI options will not be used due to error");
            }
        }
    }
    
    public void createConnectionFactory(String jndiLookupName, String factoryClassName, Map<String, String> factoryOptions) throws NamingException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        String failMessage = null;
        if (jndiCtx != null && (StringUtils.isNotBlank(jndiLookupName) || StringUtils.isBlank(factoryClassName))) {
            final String lookupName = StringUtils.defaultIfBlank(jndiLookupName, "ConnectionFactory");
            final Object obj = jndiCtx.lookup(lookupName);
            if (obj == null) {
                failMessage = "JNDI context does not contain (connFactory == null) ConnectionFactory: lookupName = '" + lookupName + "'";
            } else if (obj instanceof ConnectionFactory) {
                connFactory = (ConnectionFactory)obj;
                logEvent("ConnectionFactory created via JNDI: lookupName = '" + lookupName + "'");
            } else {
                failMessage = "JNDI context contains wrong (not instance of ConnectionFactory) ConnectionFactory class: " + obj.getClass().getCanonicalName();
            }
        }
        
        if (connFactory == null && StringUtils.isNotBlank(factoryClassName)) {
            final ClassLoader classLoader = JmsDelegate.class.getClassLoader();
            final Class<?> clazz = classLoader.loadClass(factoryClassName);
            if (ConnectionFactory.class.isAssignableFrom(clazz)) {
                connFactory = (ConnectionFactory) clazz.newInstance();
                logEvent("ConnectionFactory created via Reflection: className = '" + factoryClassName + "'");
            } else {
                throw new IOException("Wrong class (not instance of ConnectionFactory): " + factoryClassName);
            }
        } else if (connFactory == null) {
            throw new IOException(Utils.nvlOf(failMessage, "Impossible to create Connection Factory: not enough settings"));
        }
        if (connFactory instanceof TopicConnectionFactory && connFactory instanceof QueueConnectionFactory) {
            useTopic = SystemPropUtils.getBooleanSystemProp("rdx.jms.use.topic.in.doubt", true);
        } else {
            useTopic = connFactory instanceof TopicConnectionFactory;
        }
        
        
        logEvent("ConnectionFactory is " + (useTopic ? "TopicConnectionFactory" : "QueueConnectionFactory"));
        
        JmsObjectsFactory.applyOptionsToFactory(connFactory, factoryOptions);
    }
    
    public void startAntiFreeze() {
        antiFreezeRunnable = new AntiFreezeRunnable(Thread.currentThread());
        antiFreezeExecutor.submit(antiFreezeRunnable);
    }
    
    public void stopAntiFreeze() {
        antiFreezeRunnable.cancel();
    }
    
    public void createConnection(String userName, String password, String clientId) throws JMSException {
        if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(password)) {
            connection = connFactory.createConnection(userName, password);
            logEvent("Connection created with (userName, password)");
        } else {
            connection = connFactory.createConnection();
            logEvent("Connection created");
        }
        
        if (StringUtils.isNotBlank(clientId)) {
            connection.setClientID(clientId);
            logEvent("setting clientId: '" + clientId + "'");
        }
        connection.start();
        logEvent("Connection started");
    }
    
    public void createSession() throws JMSException {
        session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        logEvent("Session created");
    }
    
    public void createTopic(String jndiLookupName, String topicName) throws NamingException, JMSException, IOException {
        String failMessage = null;
        if (jndiCtx != null && StringUtils.isNotBlank(jndiLookupName)) {
            topic = (Destination) jndiCtx.lookup(jndiLookupName);
            if (topic == null) {
                failMessage = "JNDI context does not contain (topic == null) topic: lookupName = '" + jndiLookupName + "'";
            } else {
                logEvent("Topic created via JNDI: lookupName = '" + jndiLookupName + "'");
            }
        }
        
        if (topic == null && StringUtils.isNotBlank(topicName)) {
            topic = useTopic ? session.createTopic(topicName) : session.createQueue(topicName);
            logEvent("Topic created explicitly: topicName = '" + topicName + "'");
        } else if (topic == null) {
            throw new IOException(Utils.nvlOf(failMessage, "Impossible to create topic: not enough settings"));
        }
    }
    
    public void createConsumer(String subscriptionName) throws JMSException {
        if (useTopic && StringUtils.isNotBlank(subscriptionName) && StringUtils.isNotBlank(connection.getClientID())) {
            consumer = session.createDurableSubscriber((Topic) topic, subscriptionName);
            logEvent("Durable consumer created: subscriptionName = '" + subscriptionName + "', clientId = '" + connection.getClientID() + "'");
        } else {
            consumer = session.createConsumer(topic);
            logEvent("Consumer created");
        }
    }
    
    public void createProducer() throws JMSException {
        producer = session.createProducer(topic);
        logEvent("Publisher created");
    }
    
    public Message poll(int timeoutMillis) throws JMSException {
        final Message msg = timeoutMillis > 0
                ? consumer.receive(timeoutMillis)
                : consumer.receiveNoWait();
        return msg;
    }
    
    public void closeSilent() {
        try {
            session.close();
        } catch (Throwable ex) {
        }
        
        try {
            connection.close();
        } catch (Throwable ex) {
        }
    }
    
    public void close() throws JMSException {
        startAntiFreeze();
        try {
            if (producer != null) {
                producer.close();
                producer = null;
            }
            if (consumer != null) {
                consumer.close();
                consumer = null;
            }
            if (session != null) {
                session.close();
                session = null;
            }
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } finally {
            stopAntiFreeze();
        }
    }
    
    public interface JmsUnit {
        void logDebug(String mess);
        void logEvent(String mess);
        void logError(String mess);
    }
}
