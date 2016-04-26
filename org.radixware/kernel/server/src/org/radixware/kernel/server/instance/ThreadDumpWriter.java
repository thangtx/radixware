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

package org.radixware.kernel.server.instance;

import java.io.File;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.logging.LogFactory;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.server.SrvRunParams;


public class ThreadDumpWriter extends Thread {

    public static enum EThreadDumpReason {

        USER_REQUEST(2000),
        INSUF_ARTE(10000);
        private final long minIntervalMillis;

        private EThreadDumpReason(long minIntervalMillis) {
            this.minIntervalMillis = minIntervalMillis;
        }
    }
    private static final String TDUMP_PREFIX = "tdump_";
    private static final DumpRequest STOP_REQUEST = new DumpRequest();
    private static final String DATE_FORMAT_STR = "yyyy-MM-dd_HH-mm-ss-SSS";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_STR);
    //
    private volatile File tdumpsDir;
    private final LocalTracer tracer;
    private final ArrayBlockingQueue<DumpRequest> requests = new ArrayBlockingQueue<>(5);
    private final String fileNamePrefix;
    private final Map<EThreadDumpReason, AtomicLong> reason2lastRequestMillis = new EnumMap<>(EThreadDumpReason.class);
    private volatile boolean stopRequested = false;
    private final ThreadDumpsCleaner cleaner;

    /**
     * @param dir thread dumps will be written to "tdumps" directory inside
     * @param fileNamePrefix typically, string in form "instance_#1" {@code dir}
     */
    public ThreadDumpWriter(final File dir, final String fileNamePrefix, LocalTracer tracer) {
        this.tdumpsDir = dir;
        this.tracer = tracer;
        setDaemon(true);
        setName("Thread Dump Writer");
        this.fileNamePrefix = (fileNamePrefix == null ? "" : fileNamePrefix + "_") + TDUMP_PREFIX;
        for (EThreadDumpReason reason : EThreadDumpReason.values()) {
            reason2lastRequestMillis.put(reason, new AtomicLong());
        }
        cleaner = new ThreadDumpsCleaner(this);
    }

    /**
     * @param reason
     * @return
     */
    public boolean requestDump(final EThreadDumpReason reason) {
        if (reason == null || SrvRunParams.getTdumpsStoreDays() == 0) {
            return false;
        }
        final AtomicLong holder = reason2lastRequestMillis.get(reason);
        final long lastDumpMillis = holder.get();
        final long curMillis = System.currentTimeMillis();
        if (curMillis - lastDumpMillis > reason.minIntervalMillis && holder.compareAndSet(lastDumpMillis, curMillis)) {
            return requests.offer(new DumpRequest());
        }
        return false;
    }

    public void requestStop() {
        stopRequested = true;
        requests.offer(STOP_REQUEST);
        cleaner.requestStop();
    }

    @Override
    public void run() {
        cleaner.start();
        try {
            while (!stopRequested && !Thread.currentThread().isInterrupted()) {
                try {
                    final DumpRequest nextRequest = requests.take();
                    if (nextRequest == STOP_REQUEST) {
                        return;
                    }
                    doWriteDump();
                } catch (InterruptedException ex) {
                    return;
                }
            }
        } finally {
            cleaner.requestStop();
        }
    }

    public File getTdumpsDir() {
        return tdumpsDir;
    }

    public void setTdumpsDir(final File dir) {
        tdumpsDir = dir;
    }

    private void doWriteDump() {
        final File curDestDir = tdumpsDir;
        curDestDir.mkdirs();
        if (tdumpsDir != null) {
            try {
                final ThreadMXBean bean = ManagementFactory.getThreadMXBean();
                if (bean != null) {
                    final ThreadInfo[] threadInfos = bean.dumpAllThreads(true, true);
                    try (PrintWriter pw = new PrintWriter(new File(curDestDir, generateName()), "UTF-8")) {
                        for (ThreadInfo info : threadInfos) {
                            if (info == null) {
                                continue;
                            }
                            pw.print("Thread #");
                            pw.print(info.getThreadId());
                            pw.print(" - '");
                            pw.print(info.getThreadName());
                            pw.println("'");
                            pw.print("State: ");
                            pw.print(info.getThreadState());
                            if (info.getLockName() != null) {
                                pw.print(". Waiting for " + info.getLockName() + (info.getLockOwnerName() != null ? " owned by " + info.getLockOwnerName() : ""));
                            }
                            pw.println();
                            pw.println();
                            if (info.getStackTrace() != null) {
                                for (StackTraceElement ste : info.getStackTrace()) {
                                    pw.println(ste.toString());
                                }
                            }
                            pw.println();
                            pw.println();
                        }
                    }
                }
            } catch (Exception ex) {
                final String message = "Error on writing debug thread dump: " + ExceptionTextFormatter.throwableToString(ex);
                if (tracer != null) {
                    tracer.put(EEventSeverity.WARNING, message, null, null, false);
                } else {
                    LogFactory.getLog(ThreadDumpWriter.class).warn(message);
                }
            }
        }
    }

    private String generateName() {
        return fileNamePrefix + DATE_FORMAT.format(new Date());
    }

    private static class DumpRequest {
    }

    private static class ThreadDumpsCleaner extends Thread {

        private static final int SLEEP_MILLIS = 1000 * 60 * 60 * 12;
        private static final int DEFAULT_STORE_DAYS = 14;
        private volatile boolean stopRequested = false;
        private final ThreadDumpWriter writer;

        public ThreadDumpsCleaner(final ThreadDumpWriter writer) {
            setName("Thread Dumps Cleaner");
            setDaemon(true);
            this.writer = writer;
        }

        @Override
        public void run() {
            while (!stopRequested && !Thread.interrupted()) {
                try {
                    try {
                        clean(SrvRunParams.getTdumpsStoreDays() < 0 ? DEFAULT_STORE_DAYS : SrvRunParams.getTdumpsStoreDays());
                    } catch (Exception ex) {
                        if (ex instanceof InterruptedException) {
                            throw ex;
                        } else {
                            LogFactory.getLog(getClass()).warn("Error while cleaning obsolete thread dumps", ex);
                        }
                    }
                    Thread.sleep(SLEEP_MILLIS);
                } catch (InterruptedException ex) {
                    return;
                }
            }
        }

        private void clean(final int storeDays) {
            final File dir = writer.getTdumpsDir();
            if (dir != null && dir.exists()) {
                final Calendar redMark = new GregorianCalendar();
                if (storeDays > 0) {
                    redMark.set(Calendar.MILLISECOND, 0);
                    redMark.set(Calendar.SECOND, 0);
                    redMark.set(Calendar.MINUTE, 0);
                    redMark.set(Calendar.HOUR_OF_DAY, 0);
                    redMark.setTimeInMillis(redMark.getTimeInMillis() - 1000 * 60 * 60 * 24 * storeDays);
                }
                final File filesToRemove[] = dir.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        if (name.length() > DATE_FORMAT_STR.length()) {
                            final String supposedDate = name.substring(name.length() - DATE_FORMAT_STR.length(), name.length());
                            try {
                                final Date date = DATE_FORMAT.parse(supposedDate);
                                if (date.getTime() < redMark.getTimeInMillis()) {
                                    return true;
                                }
                            } catch (Exception ex) {
                                //ignore
                            }
                        }
                        return false;
                    }
                });
                if (filesToRemove != null) {
                    for (File file : filesToRemove) {
                        FileUtils.deleteFile(file);
                    }
                }
            }
        }

        public void requestStop() {
            stopRequested = true;
            interrupt();
        }
    }
}
