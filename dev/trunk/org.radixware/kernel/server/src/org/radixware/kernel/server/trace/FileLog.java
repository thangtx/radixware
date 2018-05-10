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
package org.radixware.kernel.server.trace;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.radixware.kernel.common.trace.TraceItem;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.server.Server;
import org.radixware.kernel.server.instance.Instance;

public class FileLog implements Closeable {

    public static enum RotationBase {

        RECORD_TIME,
        CURRENT_TIME;
    }

    private static final boolean WRITE_CONTEXT_TO_FILE = SystemPropUtils.getBooleanSystemProp("rdx.trace.write.context.to.file", false);
    private static final boolean WRITE_SENSITIVE_TO_FILE = SystemPropUtils.getBooleanSystemProp("rdx.trace.write.sensitive.to.file", false);
    private static final DateFormat DEFAULT_CREATION_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    private static volatile FileLogFactory factory = null;
    //@GuardedBy FileLog.this
    private InternalLog internalLog = null;
    //@GuardedBy FileLog.this
    private Calendar lastLogCreationTime = null;
    private final FileLogOptions options;
    private volatile boolean closed = false;
    private final DateFormat creationDateFormat;
    private final RotationBase rotationBase;

    /**
     * Use {@linkplain FileLog#create(FileLogOptions options) } instead
     *
     * @deprecated
     */
    @Deprecated
    public FileLog(final FileLogOptions opt) {
        this(opt, DEFAULT_CREATION_DATE_FORMAT);
    }

    /**
     * Use {@linkplain FileLog#create(FileLogOptions options) } instead
     *
     * @deprecated
     */
    @Deprecated
    public FileLog(final FileLogOptions opt, final DateFormat creationDateFormat) {
        this(opt, creationDateFormat, RotationBase.CURRENT_TIME);
    }

    /**
     * Use {@linkplain FileLog#create(FileLogOptions options) } instead
     *
     * @deprecated
     */
    @Deprecated
    public FileLog(final FileLogOptions opt, final DateFormat creationDateFormat, final RotationBase rotationBase) {
        this.options = opt;
        this.creationDateFormat = creationDateFormat == null ? DEFAULT_CREATION_DATE_FORMAT : creationDateFormat;
        if (rotationBase == null) {
            throw new NullPointerException("RotationBase should not be null");
        }
        this.rotationBase = rotationBase;
        if (rotationBase == RotationBase.CURRENT_TIME) {
            try {
                recreateLog(Calendar.getInstance());
            } catch (Exception ex) {
                //Attempt to recreate log will be performed in maintenance()
            }
        }
    }

    /**
     * Use {@linkplain FileLog#create(FileLogOptions options) }
     * instead
     *
     * @deprecated
     */
    @Deprecated
    public FileLog(
            final File dir,
            final String name,
            final int maxFileSizeBytes,
            final int rotationCount,
            final boolean rotateDaily,
            final DateFormat creationDateFormat) {
        this(new FileLogOptions(dir, name, maxFileSizeBytes, rotationCount, rotateDaily, false), creationDateFormat);
    }

    /**
     * Use {@linkplain FileLog#create(FileLogOptions options) }
     * instead
     *
     * @deprecated
     */
    @Deprecated
    public FileLog(
            final File dir,
            final String name,
            final int maxFileSizeBytes,
            final int rotationCount,
            final boolean rotateDaily,
            final DateFormat creationDateFormat,
            final RotationBase rotationBase) {
        this(new FileLogOptions(dir, name, maxFileSizeBytes, rotationCount, rotateDaily, false), creationDateFormat, rotationBase);
    }

    /**
     * Use {@linkplain FileLog#create(FileLogOptions options) }
     * instead
     *
     * @deprecated
     */
    @Deprecated
    public FileLog(
            final File dir,
            final String name,
            final int maxFileSizeBytes,
            final int rotationCount,
            final boolean rotateDaily) {
        this(dir, name, maxFileSizeBytes, rotationCount, rotateDaily, DEFAULT_CREATION_DATE_FORMAT);
    }

    public static FileLog create(final FileLogOptions options) {
        final FileLogFactory currentFactory = FileLog.factory;
        if (currentFactory != null) {
            return currentFactory.createFileLog(options);
        } else {
            return new FileLog(options);
        }
    }

    /**
     * External entity (such as {@linkplain Instance}) can register
     * {@linkplain FileLogFactory} implementation to control FileLogs creation
     * (to create registry of fileLogs and perform automatic rotation from time
     * to time)
     *
     * @param factory
     */
    public static void setFactory(final FileLogFactory factory) {
        FileLog.factory = factory;
    }

    public String getName() {
        if (options == null) {
            return "<null>";
        }
        return options.getName() == null ? "<null>" : options.getName();
    }

    public DateFormat getCreationDateFormat() {
        return creationDateFormat;
    }

    public synchronized void log(final TraceItem traceItem) {
        if (traceItem == null) {
            return;
        }
        if (traceItem.isSensitive && !WRITE_SENSITIVE_TO_FILE) {
            return;
        }
        final TraceItem.EFormat[] formatOpts;
        if (WRITE_CONTEXT_TO_FILE || (options != null && options.isWriteContextToFile())) {
            formatOpts = new TraceItem.EFormat[1];
            formatOpts[0] = TraceItem.EFormat.WITH_CONTEXT;
        } else {
            formatOpts = new TraceItem.EFormat[0];
        }
        final Calendar fileDate;
        if (rotationBase == RotationBase.CURRENT_TIME || traceItem.time == -1) {
            fileDate = Calendar.getInstance();
        } else {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(traceItem.time);
            fileDate = c;
            if (internalLog == null) {
                try {
                    recreateLog(fileDate);
                } catch (IOException ex) {
                    throw new IllegalStateException("Unable to create log based on first event time whith rotation base " + rotationBase);
                }
            }
        }
        doLog(traceItem.toFormattedString(null, formatOpts), fileDate);
    }

    public synchronized void log(final String formattedMessage) {
        doLog(formattedMessage, Calendar.getInstance());
    }

    /**
     * @NotThreadSafe This method should be called when "this" is locked by
     * synchronized
     */
    private void doLog(final String formattedMessage, final Calendar fileDate) {
        if (internalLog == null) {
            return;
        }
        try {
            rotateIfNeccessaryImpl(roundToDay(fileDate));
        } catch (Exception ex) {
            return;
        }
        if (internalLog != null) {
            internalLog.log(formattedMessage);
        }
    }

    /*
     * called only under synchronization on FileLog.this
     */
    private void recreateLog(final Calendar currentTime) throws IOException {
        if (internalLog != null) {
            internalLog.close();
        }
        final String actualName;
        if (options.isRotateDaily()) {
            lastLogCreationTime = roundToDay(currentTime);
            actualName = getCreationDateFormat().format(lastLogCreationTime.getTime()) + "_" + options.getName();
        } else {
            actualName = options.getName();
        }
        internalLog = new InternalLog(options.getDir(), actualName, options.getMaxFileSizeBytes(), options.getRotationCount());
    }

    public synchronized void maintenance() throws IOException {
        maintenance(Calendar.getInstance());
    }

    protected void maintenance(final Calendar time) throws IOException {
        maintenanceImpl(roundToDay(time));
    }

    private void maintenanceImpl(final Calendar currentTimeRoundedToDay) throws IOException {
        if (internalLog == null && !closed) {
            recreateLog(currentTimeRoundedToDay);
        }
        rotateIfNeccessaryImpl(currentTimeRoundedToDay);
    }

    private void rotateIfNeccessaryImpl(final Calendar currentTimeRoundedToDay) throws IOException {
        if (internalLog == null || !options.isRotateDaily()) {
            return;
        }

        if (currentTimeRoundedToDay.after(lastLogCreationTime)) {
            recreateLog(currentTimeRoundedToDay);
        }
    }

    @Override
    public synchronized void close() {
        closed = true;
        if (internalLog != null) {
            internalLog.close();
            internalLog = null;
        }
    }

    public TraceBuffer asTraceBuffer() {
        return new TraceBuffer() {
            @Override
            public void put(final org.radixware.kernel.common.trace.TraceItem item) {
                log(item);
            }
        };
    }

    public void logOptionsChanged(final FileLogOptions newOptions) {
        log(Messages.FILE_TRACE_OPTIONS_CHANGED + String.valueOf(newOptions));
    }

    private static Calendar roundToDay(final Calendar calendar) {
        final Calendar result = Calendar.getInstance();
        result.setTimeInMillis(calendar.getTimeInMillis());
        result.set(Calendar.HOUR_OF_DAY, 0);
        result.set(Calendar.MINUTE, 0);
        result.set(Calendar.SECOND, 0);
        result.set(Calendar.MILLISECOND, 0);
        return result;
    }

    private static class InternalLog {

        private final RadixFileHandler fileHandler;
        private final Logger logger;

        InternalLog(final File dir,
                final String name,
                final int maxFileSizeBytes,
                final int rotationCount) throws IOException {
            if (!dir.mkdirs() && !dir.exists()) {
                throw new IOException("Failed to create log directory: " + dir.getAbsolutePath());
            }
            try {
                fileHandler = new RadixFileHandler(dir.getAbsolutePath() + File.separator + name + ".log.%g", maxFileSizeBytes, rotationCount, true);
            } catch (IOException | SecurityException ex) {
                throw new IOException("Failed to create log file handler: " + ExceptionTextFormatter.getExceptionMess(ex), ex);
            }
            fileHandler.setFormatter(new Formatter() {
                @Override
                public String format(final LogRecord record) {
                    return record.getMessage() + "\n";
                }
            });
            logger = Logger.getLogger(
                    FileLog.class.getName() + ":file://" + dir.getAbsolutePath() + File.separator + name);
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
        }

        public void log(final String formattedMessage) {
            logger.log(Level.ALL, formattedMessage);
        }

        public void close() {
            logger.removeHandler(fileHandler);
            fileHandler.closeWithoutWaitOnShutdown();
        }
    }

    private static class RadixFileHandler extends FileHandler {

        public RadixFileHandler(String pattern, int limit, int count, boolean append) throws IOException, SecurityException {
            super(pattern, limit, count, append);
            setEncoding("UTF-8");
        }

        @Override
        public void close() throws SecurityException {
            waitForServerToStopIfShuttingDown();
            super.close();
        }

        public synchronized void closeWithoutWaitOnShutdown() {
            super.close();
        }

        private void waitForServerToStopIfShuttingDown() {
            final Thread dumbShutdownHook = new Thread();
            boolean isShuttingDown = false;
            try {
                Runtime.getRuntime().addShutdownHook(dumbShutdownHook);
            } catch (IllegalStateException ex) {
                isShuttingDown = true;
            }
            if (isShuttingDown) {
                try {
                    Server.awaitStop();
                } catch (InterruptedException ex) {
                    //do nothing
                }
            } else {
                try {
                    Runtime.getRuntime().removeShutdownHook(dumbShutdownHook);
                } catch (IllegalStateException ex) {
                    //shutdown is in progress, do nothing
                }
            }
        }
    }
}
