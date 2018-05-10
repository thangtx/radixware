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
 * create getDelegate() logger via DelegateLogFactory. If there is no
 * DelegateLogFactory, or {@link  DelegateLogFactory#create(java.lang.String)}
 * throws an exception, then fallback logger that writes everything to
 * {@code std.err} will be used.
 * </li>
 * <li>
 * When getDelegate() logger will throw an exception while processing log event,
 * this event will be handled by fallback logger.
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
    private static final String SAFE_LOG_MODE_INDICATOR = "[rdx.safelogging.on]";
    private static final Logger NULL_LOGGER = new NullLogger();
    private static final int MIN_REPORTED_LOGGABLE_LEVEL_IN_SAFEMODE = Integer.getInteger("rdx.starger.min.reported.loggable.level.in.safemode", SimpleLog.LOG_LEVEL_INFO);

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

    /**
     * In safe mode all messages are redirected to asynchronous SafeLogger to
     * prevent loading of any classes inside logging methods. Current
     * implementation assumes that this method will only be used by one thread
     * at time (i.e. there is some external exclusive lock which will be
     * captured before using this method).
     *
     */
    public static boolean setSafeModeForCurrentThread(final boolean safeMode) {
        final boolean prevSafeMode = isCurrentThreadInSafeMode();
        if (!prevSafeMode && safeMode) {
            Thread.currentThread().setName(Thread.currentThread().getName() + SAFE_LOG_MODE_INDICATOR);
        }
        if (prevSafeMode && !safeMode) {
            Thread.currentThread().setName(Thread.currentThread().getName().replace(SAFE_LOG_MODE_INDICATOR, ""));
        }
        return prevSafeMode;
    }

    public static boolean isCurrentThreadInSafeMode() {
        return Thread.currentThread().getName().contains(SAFE_LOG_MODE_INDICATOR);
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
    //getDelegate() logger, obtained from factory
    private volatile Logger lazyDelegate;
    //@GuardedBy this
    private final SimpleLog fallbackLogger;
    private final String name;

    public StarterLog(final String name) {
        fallbackLogger = new SimpleLog(name);
        this.name = name;
    }

    private Logger getDelegate() {
        final Logger snapshot = lazyDelegate;
        if (snapshot != null && snapshot != NULL_LOGGER) {
            return snapshot;
        }
        if (isCurrentThreadInSafeMode()) {
            return null;
        }
        synchronized (this) {
            if (lazyDelegate != null && lazyDelegate != NULL_LOGGER) {
                return lazyDelegate;
            }
            lazyDelegate = createDelegateLogger(name);
            if (lazyDelegate == null) {
                lazyDelegate = NULL_LOGGER;
                return null;
            }
            return lazyDelegate;
        }
    }

    /**
     * Explicitly sets level for this log instance. Passing null level takes no
     * effect.
     *
     * @param level
     */
    public void setLevel(Level level) {
        if (level != null) {
            if (getDelegate() != null) {
                getDelegate().setLevel(level);
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
        if (isCurrentThreadInSafeMode()) {
            SafeLogger.getInstance().warningFromSource(name, o, thrwbl);
        } else {
            try {
                getDelegate().log(Level.WARNING, o == null ? "" : o.toString(), thrwbl);
            } catch (RuntimeException t) {
                fallbackLogger.warn(o, thrwbl);
            }
        }
    }

    public void warn(Object o) {
        if (isCurrentThreadInSafeMode()) {
            SafeLogger.getInstance().warningFromSource(name, o);
        } else {
            try {
                getDelegate().log(Level.WARNING, o == null ? "" : o.toString());
            } catch (RuntimeException t) {
                fallbackLogger.warn(o);
            }
        }
    }

    public void trace(Object o, Throwable thrwbl) {
        if (isCurrentThreadInSafeMode()) {
            SafeLogger.getInstance().debugFromSource(name, o, thrwbl);
        } else {
            try {
                getDelegate().log(Level.FINEST, o == null ? "" : o.toString(), thrwbl);
            } catch (RuntimeException t) {
                fallbackLogger.trace(o, thrwbl);
            }
        }
    }

    public void trace(Object o) {
        if (isCurrentThreadInSafeMode()) {
            SafeLogger.getInstance().debugFromSource(name, o);
        } else {
            try {
                getDelegate().log(Level.FINEST, o == null ? "" : o.toString());
            } catch (RuntimeException t) {
                fallbackLogger.trace(o);
            }
        }
    }

    public boolean isWarnEnabled() {
        if (isCurrentThreadInSafeMode()) {
            return MIN_REPORTED_LOGGABLE_LEVEL_IN_SAFEMODE <= SimpleLog.LOG_LEVEL_WARN;
        }
        try {
            return getDelegate().isLoggable(Level.WARNING);
        } catch (RuntimeException t) {
            return fallbackLogger.isWarnEnabled();
        }
    }

    public boolean isTraceEnabled() {
        if (isCurrentThreadInSafeMode()) {
            return MIN_REPORTED_LOGGABLE_LEVEL_IN_SAFEMODE <= SimpleLog.LOG_LEVEL_TRACE;
        }
        try {
            return getDelegate().isLoggable(Level.FINEST);
        } catch (RuntimeException t) {
            return fallbackLogger.isTraceEnabled();
        }
    }

    public boolean isInfoEnabled() {
        if (isCurrentThreadInSafeMode()) {
            return MIN_REPORTED_LOGGABLE_LEVEL_IN_SAFEMODE <= SimpleLog.LOG_LEVEL_INFO;
        }
        try {
            return getDelegate().isLoggable(Level.INFO);
        } catch (RuntimeException t) {
            return fallbackLogger.isInfoEnabled();
        }
    }

    public boolean isFatalEnabled() {
        if (isCurrentThreadInSafeMode()) {
            return MIN_REPORTED_LOGGABLE_LEVEL_IN_SAFEMODE <= SimpleLog.LOG_LEVEL_FATAL;
        }
        try {
            return getDelegate().isLoggable(Level.SEVERE);
        } catch (RuntimeException t) {
            return fallbackLogger.isFatalEnabled();
        }
    }

    public boolean isErrorEnabled() {
        if (isCurrentThreadInSafeMode()) {
            return MIN_REPORTED_LOGGABLE_LEVEL_IN_SAFEMODE <= SimpleLog.LOG_LEVEL_ERROR;
        }
        try {
            return getDelegate().isLoggable(Level.SEVERE);
        } catch (RuntimeException t) {
            return fallbackLogger.isErrorEnabled();
        }
    }

    public boolean isDebugEnabled() {
        if (isCurrentThreadInSafeMode()) {
            return MIN_REPORTED_LOGGABLE_LEVEL_IN_SAFEMODE <= SimpleLog.LOG_LEVEL_DEBUG;
        }
        try {
            return getDelegate().isLoggable(Level.FINE);
        } catch (RuntimeException t) {
            return fallbackLogger.isDebugEnabled();

        }
    }

    public void info(Object o, Throwable thrwbl) {
        if (isCurrentThreadInSafeMode()) {
            SafeLogger.getInstance().infoFromSource(name, o, thrwbl);
        } else {
            try {
                getDelegate().log(Level.INFO, o == null ? "" : o.toString(), thrwbl);
            } catch (RuntimeException t) {
                fallbackLogger.info(o, thrwbl);
            }
        }
    }

    public void info(Object o) {
        if (isCurrentThreadInSafeMode()) {
            SafeLogger.getInstance().infoFromSource(name, o);
        } else {
            try {
                getDelegate().log(Level.INFO, o == null ? "" : o.toString());
            } catch (RuntimeException t) {
                fallbackLogger.info(o);
            }
        }
    }

    public void event(Object o, Throwable t) {
        if (isCurrentThreadInSafeMode()) {
            SafeLogger.getInstance().eventFromSource(name, o, t);
        } else {
            try {
                getDelegate().log(EVENT_LEVEL, o == null ? "" : o.toString(), t);
            } catch (RuntimeException re) {
                fallbackLogger.info(o, t);
            }
        }
    }

    public void fatal(Object o, Throwable thrwbl) {
        if (isCurrentThreadInSafeMode()) {
            SafeLogger.getInstance().errorFromSource(name, o, thrwbl);
        } else {
            try {
                getDelegate().log(Level.SEVERE, o == null ? "" : o.toString(), thrwbl);
            } catch (RuntimeException t) {
                fallbackLogger.fatal(o, thrwbl);
            }
        }
    }

    public void fatal(Object o) {
        if (isCurrentThreadInSafeMode()) {
            SafeLogger.getInstance().errorFromSource(name, o);
        } else {
            try {
                getDelegate().log(Level.SEVERE, o == null ? "" : o.toString());
            } catch (RuntimeException t) {
                fallbackLogger.fatal(o);
            }
        }
    }

    public void error(Object o, Throwable thrwbl) {
        if (isCurrentThreadInSafeMode()) {
            SafeLogger.getInstance().errorFromSource(name, o, thrwbl);
        } else {
            try {
                getDelegate().log(Level.SEVERE, o == null ? "" : o.toString(), thrwbl);
            } catch (RuntimeException t) {
                fallbackLogger.error(o, thrwbl);
            }
        }
    }

    public void error(Object o) {
        if (isCurrentThreadInSafeMode()) {
            SafeLogger.getInstance().errorFromSource(name, o);
        } else {
            try {
                getDelegate().log(Level.SEVERE, o == null ? "" : o.toString());
            } catch (RuntimeException t) {
                fallbackLogger.error(o);
            }
        }
    }

    public void debug(Object o, Throwable thrwbl) {
        if (isCurrentThreadInSafeMode()) {
            SafeLogger.getInstance().debugFromSource(name, o, thrwbl);
        } else {
            try {
                getDelegate().log(Level.FINE, o == null ? "" : o.toString(), thrwbl);
            } catch (RuntimeException t) {
                fallbackLogger.debug(o, thrwbl);
            }
        }
    }

    public void debug(Object o) {
        if (isCurrentThreadInSafeMode()) {
            SafeLogger.getInstance().debugFromSource(name, o);
        } else {
            try {
                getDelegate().log(Level.FINE, o == null ? "" : o.toString());
            } catch (RuntimeException t) {
                fallbackLogger.debug(o);
            }
        }
    }

    private static class NullLogger extends Logger {

        public NullLogger() {
            super(null, null);
        }

    }
}
