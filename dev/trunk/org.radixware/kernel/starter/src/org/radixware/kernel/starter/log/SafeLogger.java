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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.radixware.kernel.starter.Starter;
import static org.radixware.kernel.starter.log.StarterLog.isCurrentThreadInSafeMode;
import org.radixware.kernel.starter.utils.ExceptionTextFormatter;

/**
 * Doesn't load classes after construction. Writes messages from dedicated
 * thread.
 *
 */
public class SafeLogger {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
    private static final int MAX_RECORDS_COUNT = 10000;
    private final ArrayBlockingQueue<Record> recordsQueue = new ArrayBlockingQueue<>(MAX_RECORDS_COUNT);
    private final Thread flusherThread;
    public static final String SUPPRESS_SAFELOG_MARKER = "[rdx.safelogging.suppress]";

    private SafeLogger() {
        LogFactory.getLog(SafeLogger.class);
        final AtomicBoolean lockClassloader = new AtomicBoolean();
        flusherThread = new Thread() {

            @Override
            public void setContextClassLoader(ClassLoader cl) {
                if (lockClassloader.get()) {
                    return;
                }
                super.setContextClassLoader(cl);
            }

            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        final Record record = recordsQueue.take();
                        try {
                            if (record != null) {
                                final Log log;
                                if (record.delegate == null) {
                                    log = LogFactory.getLog(record.source == null ? SafeLogger.class.getName() : record.source);
                                } else {
                                    log = record.delegate;
                                }
                                if (log != null) {
                                    if (record.level == null || record.level == Level.FINE) {
                                        log.debug(record.message, record.throwable);
                                    } else if (record.level == Level.INFO) {
                                        log.info(record.message, record.throwable);
                                    } else if (record.level == StarterLog.EVENT_LEVEL) {
                                        RadixLogUtils.event(log, record.message, record.throwable);
                                    } else if (record.level == Level.WARNING) {
                                        log.warn(record.message, record.throwable);
                                    } else {
                                        log.error(record.message, record.throwable);
                                    }
                                    continue;
                                }
                            }
                            writeRecordToSystemErr(record);
                        } catch (Throwable t) {
                            if (t instanceof InterruptedException) {
                                return;
                            }
                            new RuntimeException("Unexpected error in SafeLog flusher thread", t).printStackTrace(System.err);
                        }
                    } catch (InterruptedException ex) {
                        return;
                    }
                }
            }
        };
        flusherThread.setName("SafeLog flusher thread for " + (Starter.isRoot() ? "root" : "app") + " starter");
        flusherThread.setDaemon(true);
        flusherThread.setContextClassLoader(getClass().getClassLoader());
        lockClassloader.set(true);
    }

    public static SafeLogger getInstance() {
        return Holder.INSTANCE;
    }

    public static void interrupt() {
        getInstance().flusherThread.interrupt();
    }

    public static boolean setSafeLogSuppressedForCurrentThread(final boolean suppress) {
        final boolean prevSuppressed = isSafeLogSuppressedForCurrentThread();
        if (!prevSuppressed && suppress) {
            Thread.currentThread().setName(Thread.currentThread().getName() + SUPPRESS_SAFELOG_MARKER);
        }
        if (prevSuppressed && !suppress) {
            Thread.currentThread().setName(Thread.currentThread().getName().replace(SUPPRESS_SAFELOG_MARKER, ""));
        }
        return prevSuppressed;
    }

    public static boolean isSafeLogSuppressedForCurrentThread() {
        return Thread.currentThread().getName().contains(SUPPRESS_SAFELOG_MARKER);
    }

    private void startThread() {
        flusherThread.start();
    }

    private void writeRecordToSystemErr(Record record) {
        if (record != null) {
            System.err.println(DATE_FORMAT.format(new Date()) + " " + record.source + " [" + record.level + "]: " + record.message + (record.throwable == null ? "" : "\n" + ExceptionTextFormatter.throwableToString(record.throwable)));
        }
    }

    public void debug(final Class clazz, final Object message) {
        debug(clazz, message, null);
    }

    public void debug(final Class clazz, final Object message, final Throwable throwable) {
        debugFromSource(clazz == null ? null : clazz.getName(), message, throwable);
    }

    public void debugFromSource(final String source, final Object message) {
        debugFromSource(source, message, null);
    }

    public void debugFromSource(final String source, final Object message, final Throwable throwable) {
        debugFromSourceWithDelegate(source, message, throwable, null);
    }

    public void debugFromSourceWithDelegate(final String source, final Object message, final Throwable throwable, final Log delegate) {
        final Record record = new Record(source, Level.FINE, message, throwable, delegate);
        writeRecord(record);
    }

    public void info(final Class clazz, final Object message) {
        info(clazz, message, null);
    }

    public void info(final Class clazz, final Object message, final Throwable throwable) {
        infoFromSource(clazz == null ? null : clazz.getName(), message, throwable);
    }

    public void infoFromSource(final String source, final Object message) {
        infoFromSource(source, message, null);
    }

    public void infoFromSource(final String source, final Object message, final Throwable throwable) {
        infoFromSourceWithDelegate(source, message, throwable, null);
    }

    public void infoFromSourceWithDelegate(final String source, final Object message, final Throwable throwable, final Log delegate) {
        final Record record = new Record(source, Level.INFO, message, throwable, delegate);
        writeRecord(record);
    }

    public void event(final Class clazz, final Object message) {
        event(clazz, message, null);
    }

    public void event(final Class clazz, final Object message, final Throwable throwable) {
        eventFromSource(clazz == null ? null : clazz.getName(), message, throwable);
    }

    public void eventFromSource(final String source, final Object message) {
        eventFromSource(source, message, null);
    }

    public void eventFromSource(final String source, final Object message, final Throwable throwable) {
        eventFromSourceWithDelegate(source, message, throwable, null);
    }

    public void eventFromSourceWithDelegate(final String source, final Object message, final Throwable throwable, final Log delegate) {
        final Record record = new Record(source, StarterLog.EVENT_LEVEL, message, throwable, delegate);
        writeRecord(record);
    }

    public void warning(final Class clazz, final Object message) {
        warning(clazz, message, null);
    }

    public void warning(final Class clazz, final Object message, final Throwable throwable) {
        warningFromSource(clazz == null ? null : clazz.getName(), message, throwable);
    }

    public void warningFromSource(final String source, final Object message) {
        warningFromSource(source, message, null);
    }

    public void warningFromSource(final String source, final Object message, final Throwable throwable) {
        warningFromSourceWithDelegate(source, message, throwable, null);
    }

    public void warningFromSourceWithDelegate(final String source, final Object message, final Throwable throwable, final Log delegate) {
        final Record record = new Record(source, Level.WARNING, message, throwable, delegate);
        writeRecord(record);
    }

    public void error(final Class clazz, final Object message) {
        error(clazz, message, null);
    }

    public void error(final Class clazz, final Object message, final Throwable throwable) {
        errorFromSource(clazz == null ? null : clazz.getName(), message, throwable);
    }

    public void errorFromSource(final String source, final Object message) {
        errorFromSource(source, message, null);
    }

    public void errorFromSource(final String source, final Object message, final Throwable throwable) {
        errorFromSourceWithDelegate(source, message, throwable, null);
    }

    public void errorFromSourceWithDelegate(final String source, final Object message, final Throwable throwable, final Log delegate) {
        final Record record = new Record(source, Level.SEVERE, message, throwable, delegate);
        writeRecord(record);
    }

    private void writeRecord(final Record record) {
        final String threadName = Thread.currentThread().getName();
        if (threadName.contains(SUPPRESS_SAFELOG_MARKER)) {
            return;
        }
        if (!recordsQueue.offer(record)) {
            writeRecordToSystemErr(record);
        }
    }

    private static class Holder {

        private static final SafeLogger INSTANCE;

        static {
            //warmup
            LogFactory.getLog(Holder.class.getName());
            Record.class.getName();
            ExceptionTextFormatter.class.getName();
            Level.class.getName();
            Date.class.getName();
            StarterLog.class.getName();
            RadixLogUtils.class.getName();

            INSTANCE = new SafeLogger();
            INSTANCE.startThread();
        }
    }

    private static class Record {

        public final String source;
        public final Level level;
        public final Object message;
        public final Throwable throwable;
        public final Log delegate;

        public Record(String source, Level level, Object message, Throwable throwable, Log delegate) {
            this.source = source;
            this.level = level;
            this.message = message;
            this.throwable = throwable;
            this.delegate = delegate;
        }
    }
}
