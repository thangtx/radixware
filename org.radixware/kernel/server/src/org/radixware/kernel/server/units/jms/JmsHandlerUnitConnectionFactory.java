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

import org.radixware.kernel.common.utils.net.RadixJmsConnectionOptions;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.radixware.kernel.common.utils.net.JmsUtils;


public class JmsHandlerUnitConnectionFactory {

    public static final String INITIAL_CONTEXT_FACTORY = Context.INITIAL_CONTEXT_FACTORY;
    public static final String PROVIDER_URL = Context.PROVIDER_URL;
    public static final String CONNECTION_FACTORY_NAME = "connectionFactoryName";
    public static final String CONNECTION_FACTORY_CLASS = "connectionFactoryClass";



    public static Connection createConnection(final RadixJmsConnectionOptions options) throws Exception {
        Map<String, String> connectProps = JmsUtils.parseProps(options.getConnectionOptions());
        final ConnectionFactory factory;
        if (connectProps != null) {
            if (connectProps.containsKey(INITIAL_CONTEXT_FACTORY) && connectProps.containsKey(CONNECTION_FACTORY_NAME)) {
                final Properties p = new Properties();
                p.put(INITIAL_CONTEXT_FACTORY, connectProps.get(INITIAL_CONTEXT_FACTORY));
		if (connectProps.containsKey(PROVIDER_URL))
                    p.put(PROVIDER_URL, connectProps.get(PROVIDER_URL));
                final InitialContext ctx = new InitialContext(p);
                factory = (ConnectionFactory) ctx.lookup(connectProps.get(CONNECTION_FACTORY_NAME));
            } else if (connectProps.containsKey(CONNECTION_FACTORY_CLASS)) {
                final ClassLoader classLoader = JmsHandlerUnitConnectionFactory.class.getClassLoader();
                final Class<?> clazz = classLoader.loadClass(connectProps.get(CONNECTION_FACTORY_CLASS));
                factory = (ConnectionFactory) clazz.newInstance();
            } else {
                factory = new com.sun.messaging.QueueConnectionFactory();
            }
        } else {
            factory = new com.sun.messaging.QueueConnectionFactory();
        }

        if (connectProps != null) {
            try {
                Method setPropMethod = factory.getClass().getMethod("setProperty", String.class, String.class);
                for (Map.Entry<String, String> e : connectProps.entrySet()) {
                    setPropMethod.invoke(factory, e.getKey(), e.getValue());
                }
            } catch (NoSuchMethodException | SecurityException ex) {
                //do nothing
            }

        }
        final Connection connection;
        if (options.getJmsLogin() != null || options.getJmsPassword() != null) {
            connection = factory.createConnection(options.getJmsLogin(), options.getJmsPassword());
        } else {
            connection = factory.createConnection();
        }
        return connection;
    }
}
