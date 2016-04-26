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
package org.radixware.kernel.starter.url;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

public class UrlFactory implements URLStreamHandlerFactory {

    public static final String STARTER_PROTOCOL_PREFIX = "starter-x";
    private static volatile Object radixLoader = null;
    private static volatile URLStreamHandlerFactory appFactory = null;
    private final URLStreamHandlerFactory delegate;

    public UrlFactory() {
        delegate = null;
    }

    public UrlFactory(URLStreamHandlerFactory delegate) {
        this.delegate = delegate;
    }

    static Object getRadixLoader() {
        return radixLoader;
    }

    public static void setRadixLoader(final Object radixLoader) {
        UrlFactory.radixLoader = radixLoader;
    }

    /**
     * Used via reflection in RadixURLTool
     */
    public static void setAppFactory(final URLStreamHandlerFactory factory) {
        appFactory = factory;
    }

    @Override
    public URLStreamHandler createURLStreamHandler(final String protocol) {
        final URLStreamHandlerFactory appFactorySnapshot = appFactory;
        if (appFactorySnapshot != null) {
            //just check if appFactory supports this kind of urls
            final URLStreamHandler delegateHandler = appFactorySnapshot.createURLStreamHandler(protocol);
            if (delegateHandler != null) {
                //do not use returned handler directly because java will put it in a cache
                return new ProxyHandler(protocol);
            }
        }
        if (protocol != null && protocol.startsWith(UrlFactory.STARTER_PROTOCOL_PREFIX)) {
            //"starter".equals(protocol)) {
            return new UrlHandler();
        }
        return delegate == null ? null : delegate.createURLStreamHandler(protocol);
    }

    private static class ProxyHandler extends URLStreamHandler {

        private final String protocol;

        public ProxyHandler(String protocol) {
            this.protocol = protocol;
        }

        @Override
        protected URLConnection openConnection(URL u) throws IOException {
            final URLStreamHandlerFactory appFactorySnapshot = appFactory;
            if (appFactorySnapshot != null) {
                final URLStreamHandler delegate = appFactorySnapshot.createURLStreamHandler(protocol);
                try {
                    return (URLConnection) getOpenConnectionMethod(delegate.getClass()).invoke(delegate, u);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    throw new IOException(ex);
                }
            } else {
                throw new IOException("Application URLStreamHandler factory is not defined ");
            }
        }

        private Method getOpenConnectionMethod(final Class clazz) {
            try {
                final Method m = clazz.getDeclaredMethod("openConnection", URL.class);
                if (m != null) {
                    m.setAccessible(true);
                    return m;
                }
            } catch (NoSuchMethodException | SecurityException ex) {
                //ingore;
            }
            return getOpenConnectionMethod(clazz.getSuperclass());
        }
    }

    public static final String protocolForRev(final long rev) {
        String revAsString = String.valueOf(rev);
        StringBuilder result = new StringBuilder();
        result.append(STARTER_PROTOCOL_PREFIX);
        for (int i = 0, len = revAsString.length(); i < len; i++) {
            char c = revAsString.charAt(i);
            switch (c) {
                case '0':
                    result.append('a');
                    break;
                case '1':
                    result.append('b');
                    break;
                case '2':
                    result.append('c');
                    break;
                case '3':
                    result.append('d');
                    break;
                case '4':
                    result.append('e');
                    break;
                case '5':
                    result.append('f');
                    break;
                case '6':
                    result.append('g');
                    break;
                case '7':
                    result.append('h');
                    break;
                case '8':
                    result.append('i');
                    break;
                case '9':
                    result.append('j');
                    break;
            }
        }
        return result.toString();//STARTER_PROTOCOL_PREFIX + rev;
    }
}
