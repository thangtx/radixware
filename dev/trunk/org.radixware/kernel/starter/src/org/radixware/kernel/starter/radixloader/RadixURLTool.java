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

package org.radixware.kernel.starter.radixloader;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLStreamHandlerFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.starter.Starter;
import org.radixware.kernel.starter.url.UrlFactory;


public class RadixURLTool {

    public static void setUrlStreamHandlerFactory(URLStreamHandlerFactory factory) {
        unsetRegisteredFactory();
        URL.setURLStreamHandlerFactory(factory);
    }

    private static URLStreamHandlerFactory getRegisteredFactory() {
        Field f;
        try {
            f = URL.class.getDeclaredField("factory");
            if (f != null) {
                f.setAccessible(true);
                return (URLStreamHandlerFactory) f.get(null);
            }
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchFieldException ex) {
            throw new RuntimeException(ex);
        } catch (SecurityException ex) {
            throw new RuntimeException(ex);
        }

        return null;
    }

    private static void unsetRegisteredFactory() {
        Field f;
        try {
            f = URL.class.getDeclaredField("factory");
            if (f != null) {
                f.setAccessible(true);
                f.set(null, null);
            }
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchFieldException ex) {
            throw new RuntimeException(ex);
        } catch (SecurityException ex) {
            throw new RuntimeException(ex);
        }

    }

    public static void setUrlStreamHandlerFactory(Class<?> factoryClass) {
        try {
            Object instance = null;
            try {
                Constructor c = factoryClass.getConstructor(URLStreamHandlerFactory.class);
                if (c != null) {
                    try {
                        instance = c.newInstance(getRegisteredFactory());
                    } catch (InstantiationException ex) {
                    } catch (IllegalAccessException ex) {
                    } catch (IllegalArgumentException ex) {
                    } catch (InvocationTargetException ex) {
                    }
                }
            } catch (NoSuchMethodException ex) {
            } catch (SecurityException ex) {
            }
            if (instance == null) {
                instance = factoryClass.newInstance();
            }
            setUrlStreamHandlerFactory((URLStreamHandlerFactory) instance);
        } catch (InstantiationException ex) {
        } catch (IllegalAccessException ex) {
        }
    }

    public static void setAppURLStreamHandlerFactory(final URLStreamHandlerFactory factory) {
        try {
            final Class urlFactoryClass = Starter.getRootStarterClassLoader().loadClass(UrlFactory.class.getName());
            urlFactoryClass.getMethod("setAppFactory", URLStreamHandlerFactory.class).invoke(null, factory);
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException ex) {
        }
    }
}
