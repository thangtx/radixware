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

package org.radixware.kernel.starter.log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.SimpleLog;
import org.radixware.kernel.starter.radixloader.RadixClassLoader;

/**
 * This class implements mechanism that gives possibility to applications
 * started via RadixWare Starter to handle log events from Apache loggers.
 * <p>
 * The main principle is:
 * <ul>
 * <li>
 * In Starter's main() method we configure Apache LogFactory to use this Log
 * implementation.
 * </li>
 * <li> {@link StarterLog} has static setter for {@link DelegateLogFactory}.
 * This setter may be called by the application to provide its
 * DelegateLogFactory to create loggers.
 * </li>
 * <li>
 * When Apache LogFactory creates instance of StarterLog, StarterLog tries to
 * create delegate logger via DelegateLogFactory. If there is no
 * DelegateLogFactory, or {@link  DelegateLogFactory#create(java.lang.String)}
 * throws an exception, then fallback logger that writes everything to
 * {@code std.err} will be used.
 * </li>
 * <li>
 * When delegate logger will throw an exception while processing log event, this
 * event will be handled by fallback logger.
 * </li>
 * </ul>
 * </p>
 *
 */
public class StarterLog implements Log {

    //used via reflection
    private static volatile Object factory;
    //log for writing errors that may happen during reflection calls
    private static final Log errLog = new SimpleLog(StarterLog.class.getName());
    public static final Level EVENT_LEVEL = Level.parse(String.valueOf(Level.INFO.intValue() + 1));

    public static void setFactory(final DelegateLogFactory factory) {
        final Class starterLogClass = getStarterLogClass();
        try {
            final Field factoryField = starterLogClass.getDeclaredField("factory");
            factoryField.setAccessible(true);
            factoryField.set(null, factory);
        } catch (NoSuchFieldException ex) {
            errLog.error("Can not set factory", ex);
        } catch (SecurityException ex) {
            errLog.error("Can not set factory", ex);
        } catch (IllegalAccessException ex) {
            errLog.error("Can not set factory", ex);
        } catch (IllegalArgumentException ex) {
            errLog.error("Can not set factory", ex);
        }
    }

    private static Class getStarterLogClass() {
        ClassLoader cl = StarterLog.class.getClassLoader();
        while (isRadixLoader(cl)) {
            cl = cl.getParent();
        }
        try {
            return cl.loadClass(StarterLog.class.getName());
        } catch (ClassNotFoundException ex) {
            errLog.error("Can not obtain toplevel StarterLog class", ex);
            return StarterLog.class;
        }
    }

    private static boolean isRadixLoader(ClassLoader cl) {
        if (cl == null) {
            return false;
        }
        while (cl != null && !cl.getClass().getName().equals(RadixClassLoader.class.getName())) {
            cl = cl.getParent();
        }
        return cl != null;
    }
    //delegate logger, obtained from factory
    private final Logger delegate;
    private final SimpleLog fallbackLogger;

    public StarterLog(final String name) {
        delegate = createDelegateLogger(name);
        //std.err logger
        fallbackLogger = new SimpleLog(name);
    }

    /**
     * Explicitly sets level for this log instance. Passing null level takes no
     * effect.
     *
     * @param level
     */
    public void setLevel(Level level) {
        if (level != null) {
            if (delegate != null) {
                delegate.setLevel(level);
            }
            fallbackLogger.setLevel(getSimpeLogLevel(level));
        }
    }

    private int getSimpeLogLevel(Level javaLogLevel) {
        if (javaLogLevel.intValue() < Level.INFO.intValue()) {
            return SimpleLog.LOG_LEVEL_DEBUG;
        }
        if (javaLogLevel.intValue() < Level.WARNING.intValue()) {
            return SimpleLog.LOG_LEVEL_INFO;
        }
        if (javaLogLevel.intValue() < Level.SEVERE.intValue()) {
            return SimpleLog.LOG_LEVEL_WARN;
        }
        if (javaLogLevel.intValue() < Level.OFF.intValue()) {
            return SimpleLog.LOG_LEVEL_ERROR;
        }
        return SimpleLog.LOG_LEVEL_OFF;
    }

    private Logger createDelegateLogger(final String name) {
        final Class starterLogClass = getStarterLogClass();
        try {
            final Field delegateFactoryField = starterLogClass.getDeclaredField("factory");
            delegateFactoryField.setAccessible(true);
            Object delegateFactory = delegateFactoryField.get(null);
            if (delegateFactory != null) {
                try {
                    final Method createMethod = delegateFactory.getClass().getMethod("createJavaLogger", String.class);
                    createMethod.setAccessible(true);
                    return (Logger) createMethod.invoke(delegateFactory, name);
                } catch (InvocationTargetException ex) {
                    if (ex.getCause() instanceof ClassCircularityError) {
                        errLog.debug("Attempt to use Log while loading Log implementation class, using fallback implementation");
                        return null;
                    } else {
                        errLog.error("Error while creating delegate logger", ex.getCause());
                        return null;
                    }
                } catch (Throwable t) {
                    errLog.error("Error while creating delegate logger", t);
                    return null;
                }
            }
        } catch (IllegalArgumentException ex) {
            errLog.error("Can not get factory", ex);
        } catch (IllegalAccessException ex) {
            errLog.error("Can not get factory", ex);
        } catch (NoSuchFieldException ex) {
            errLog.error("Can not get factory", ex);
        } catch (SecurityException ex) {
            errLog.error("Can not get factory", ex);
        }
        return null;
    }

    public void warn(Object o, Throwable thrwbl) {
        try {
            delegate.log(Level.WARNING, o == null ? "" : o.toString(), thrwbl);
        } catch (RuntimeException t) {
            fallbackLogger.warn(o, thrwbl);
        }
    }

    public void warn(Object o) {
        try {
            delegate.log(Level.WARNING, o == null ? "" : o.toString());
        } catch (RuntimeException t) {
            fallbackLogger.warn(o);
        }
    }

    public void trace(Object o, Throwable thrwbl) {
        try {
            delegate.log(Level.FINEST, o == null ? "" : o.toString(), thrwbl);
        } catch (RuntimeException t) {
            fallbackLogger.trace(o, thrwbl);
        }
    }

    public void trace(Object o) {
        try {
            delegate.log(Level.FINEST, o == null ? "" : o.toString());
        } catch (RuntimeException t) {
            fallbackLogger.trace(o);
        }
    }

    public boolean isWarnEnabled() {
        try {
            return delegate.isLoggable(Level.WARNING);
        } catch (RuntimeException t) {
            return fallbackLogger.isWarnEnabled();
        }
    }

    public boolean isTraceEnabled() {
        try {
            return delegate.isLoggable(Level.FINEST);
        } catch (RuntimeException t) {
            return fallbackLogger.isTraceEnabled();
        }
    }

    public boolean isInfoEnabled() {
        try {
            return delegate.isLoggable(Level.INFO);
        } catch (RuntimeException t) {
            return fallbackLogger.isInfoEnabled();
        }
    }

    public boolean isFatalEnabled() {
        try {
            return delegate.isLoggable(Level.SEVERE);
        } catch (RuntimeException t) {
            return fallbackLogger.isFatalEnabled();
        }
    }

    public boolean isErrorEnabled() {
        try {
            return delegate.isLoggable(Level.SEVERE);
        } catch (RuntimeException t) {
            return fallbackLogger.isErrorEnabled();
        }
    }

    public boolean isDebugEnabled() {
        try {
            return delegate.isLoggable(Level.FINE);
        } catch (RuntimeException t) {
            return fallbackLogger.isDebugEnabled();

        }
    }

    public void info(Object o, Throwable thrwbl) {
        try {
            delegate.log(Level.INFO, o == null ? "" : o.toString(), thrwbl);
        } catch (RuntimeException t) {
            fallbackLogger.info(o, thrwbl);
        }
    }

    public void info(Object o) {
        try {
            delegate.log(Level.INFO, o == null ? "" : o.toString());
        } catch (RuntimeException t) {
            fallbackLogger.info(o);
        }
    }

    public void event(Object o, Throwable t) {
        try {
            delegate.log(EVENT_LEVEL, o == null ? "" : o.toString(), t);
        } catch (RuntimeException re) {
            fallbackLogger.info(o, t);
        }
    }

    public void fatal(Object o, Throwable thrwbl) {
        try {
            delegate.log(Level.SEVERE, o == null ? "" : o.toString(), thrwbl);
        } catch (RuntimeException t) {
            fallbackLogger.fatal(o, thrwbl);
        }
    }

    public void fatal(Object o) {
        try {
            delegate.log(Level.SEVERE, o == null ? "" : o.toString());
        } catch (RuntimeException t) {
            fallbackLogger.fatal(o);
        }
    }

    public void error(Object o, Throwable thrwbl) {
        try {
            delegate.log(Level.SEVERE, o == null ? "" : o.toString(), thrwbl);
        } catch (RuntimeException t) {
            fallbackLogger.error(o, thrwbl);
        }
    }

    public void error(Object o) {
        try {
            delegate.log(Level.SEVERE, o == null ? "" : o.toString());
        } catch (RuntimeException t) {
            fallbackLogger.error(o);
        }
    }

    public void debug(Object o, Throwable thrwbl) {
        try {
            delegate.log(Level.FINE, o == null ? "" : o.toString(), thrwbl);
        } catch (RuntimeException t) {
            fallbackLogger.debug(o, thrwbl);
        }
    }

    public void debug(Object o) {
        try {
            delegate.log(Level.FINE, o == null ? "" : o.toString());
        } catch (RuntimeException t) {
            fallbackLogger.debug(o);
        }
    }
}
