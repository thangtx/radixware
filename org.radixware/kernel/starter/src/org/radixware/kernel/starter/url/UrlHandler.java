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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Set;
import org.radixware.kernel.starter.log.SafeLogger;

public class UrlHandler extends URLStreamHandler {

    @Override
    protected URLConnection openConnection(final URL url) throws IOException {
        return new URLConnection(url) {
            class MethodInfo {

                final Method method;
                final Object loader;
                final String archive;
                final String name;
                final Object revision_meta;
                final long revision_num;

                public MethodInfo(Method method, Object loader, String archive, String name, Object revision_meta, long revision_num) {
                    this.method = method;
                    this.loader = loader;
                    this.archive = archive;
                    this.name = name;
                    this.revision_meta = revision_meta;
                    this.revision_num = revision_num;
                }
            }

            @Override
            public void connect() throws IOException {
                final MethodInfo info = getFinalMethod("readResourceData");
                try {
                    final byte[] data = (byte[]) info.method.invoke(info.loader, new Object[]{info.name, null, info.archive, info.revision_meta});
                    if (data == null) {
                        throw new IOException("Starter URL handler: can't find " + info.name + " in " + info.archive + ", revision " + info.revision_num);
                    }

                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException ex) {
                    throw new IOException("Starter URL handler: can't load " + info.name + " from " + info.archive + ", revision " + info.revision_num, ex);
                }
            }

            @Override
            public InputStream getInputStream() throws IOException {
                final MethodInfo info = getFinalMethod("readResourceData");
                try {
                    final byte[] data = (byte[]) info.method.invoke(info.loader, new Object[]{info.name, null, info.archive, info.revision_meta});
                    if (data != null) {
                        return new ByteArrayInputStream(data);
                    }
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException ex) {
                    throw new IOException("Starter URL handler: can't load " + info.name + " from " + info.archive + ", revision " + info.revision_num, ex);
                }
                throw new IOException("Starter URL handler: can't find " + info.name + " in " + info.archive + ", revision " + info.revision_num);
            }

            private MethodInfo getFinalMethod(String methodName) throws IOException {
                String protocol = getURL().getProtocol();
                long revision_num = 0;
                if (protocol.startsWith(UrlFactory.STARTER_PROTOCOL_PREFIX)) {
                    String numberStr = protocol.substring(UrlFactory.STARTER_PROTOCOL_PREFIX.length()).toLowerCase();
                    if (!numberStr.isEmpty() && Character.isDigit(numberStr.charAt(0))) {
                        try {
                            revision_num = Long.parseLong(numberStr);
                        } catch (NumberFormatException e) {
                            throw new IOException("Invalid revision number format: " + protocol);
                        }
                    } else {
//                   
                        long order = 1;
                        for (int i = numberStr.length() - 1; i >= 0; i--) {
                            long digit;
                            char c = numberStr.charAt(i);
                            switch (c) {
                                case 'a':
                                    digit = 0;
                                    break;
                                case 'b':
                                    digit = 1;
                                    break;
                                case 'c':
                                    digit = 2;
                                    break;
                                case 'd':
                                    digit = 3;
                                    break;
                                case 'e':
                                    digit = 4;
                                    break;
                                case 'f':
                                    digit = 5;
                                    break;
                                case 'g':
                                    digit = 6;
                                    break;
                                case 'h':
                                    digit = 7;
                                    break;
                                case 'i':
                                    digit = 8;
                                    break;
                                case 'j':
                                    digit = 9;
                                    break;
                                default:
                                    throw new IllegalStateException("Unexpeced symbol in encoded revision number");
                            }
                            revision_num += digit * order;
                            order *= 10;
                        }
                    }
                } else {
                    throw new IOException("Unsupported protocol: " + protocol);
                }

                String[] filePathComponents = getURL().getFile().split(";");
                String archive;
                String name;
                if (filePathComponents.length > 1) {
                    archive = filePathComponents[0];
                    if (archive.startsWith("/")) {
                        archive = archive.substring(1);
                    }
                    name = filePathComponents[1];
                } else if (filePathComponents.length > 0) {
                    archive = null;
                    name = filePathComponents[0];
                    if (name.startsWith("/")) {
                        name = name.substring(1);
                    }
                } else {
                    archive = null;
                    name = null;
                }
                try {
                    Object loader = findRadixLoader();
                    final Object revision_meta = loader.getClass().getMethod("findRevisionMeta", new Class[]{long.class}).invoke(loader, new Object[]{revision_num});
                    if (revision_meta != null) {
                        return new MethodInfo(loader.getClass().getMethod(methodName, new Class[]{String.class, Set.class, String.class, revision_meta.getClass()}), loader, archive, name, revision_meta, revision_num);
                    }
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                    throw new IOException("Starter URL handler: can't load " + name + " from " + archive + ", revision " + revision_num, ex);
                }
                throw new IOException("Starter URL handler: can't find " + name + " in " + archive + ", revision " + revision_num);
            }
        };
    }

    private Object findRadixLoader() {
        Object loader = null;
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl != null) {
            try {
                Method method = cl.getClass().getDeclaredMethod("getRadixLoader", new Class[]{});
                if (method != null) {
                    method.setAccessible(true);
                    loader = method.invoke(cl);
                    if (loader != null) {
                        method = loader.getClass().getMethod("isClosed", new Class[0]);
                        if (method != null) {
                            method.setAccessible(true);
                            Object result = method.invoke(loader, new Object[0]);
                            if (result instanceof Boolean) {
                                if (((Boolean) result).booleanValue()) {
                                    loader = null;
                                }
                            } else {
                                loader = null;
                            }
                        }
                    }
                }
            } catch (NoSuchMethodException e) {
                loader = null;
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
                SafeLogger.getInstance().debug(UrlHandler.class, "Unable to get radix loader instance", e);
                loader = null;
            }
        }
        if (loader == null) {
            loader = UrlFactory.getRadixLoader();
        }
        return loader;
    }

    @Override
    protected void parseURL(URL u, String spec, int start, int limit) {
        //   String init = u.getFile();
        super.parseURL(u, spec, start, limit);

//
//        if ("starter".equals(u.getProtocol())) {
//            StringTokenizer st = new StringTokenizer(u.getFile(), ";");
//            String next = st.nextToken();
//            try {
//                Long.valueOf(next);
//            } catch (NumberFormatException e) {
//                //try to restore revision
//                System.out.println("Revision loss");
//            }
//        }
//
    }

    @Override
    protected void setURL(URL u, String protocol, String host, int port, String authority, String userInfo, String path, String query, String ref) {
//        if ("starter".equals(protocol)) {
//            StringTokenizer st = new StringTokenizer(path, ";");
//            String next = st.nextToken();
//            try {
//                Long.valueOf(next);
//            } catch (NumberFormatException e) {
//                StringTokenizer st2 = new StringTokenizer(u.getFile(), ";");
//                String version = st2.nextToken();
//                if (path.startsWith("/")) {
//                    path = path.substring(1);
//                }
//                path = version + "; ;" + path;
//                st2 = new StringTokenizer(path, ";");
//                System.out.println(st2.nextToken());
//                System.out.println(st2.nextToken());
//                System.out.println(st2.nextToken());
//            }
//        }
        super.setURL(u, protocol, host, port, authority, userInfo, path, query, ref);
    }
}
