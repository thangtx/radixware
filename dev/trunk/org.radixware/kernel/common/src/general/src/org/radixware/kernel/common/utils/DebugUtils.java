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

package org.radixware.kernel.common.utils;

import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.logging.LogFactory;


public class DebugUtils {

    private static final AtomicLong INIT_TIME = new AtomicLong();
    
    public static void suppressException(final Throwable ex) {
        LogFactory.getLog(DebugUtils.class).debug("Suppressed exception", ex);
    }

    public static String getDebugTime() {
        final long curTimeMillis = System.currentTimeMillis();
        long initTimeMillis;
        if (INIT_TIME.compareAndSet(0, curTimeMillis)) {
            initTimeMillis = curTimeMillis;
        } else {
            initTimeMillis = INIT_TIME.get();
        }
        long diffMillis = curTimeMillis - initTimeMillis;
        return String.format("%03d.%03d", diffMillis / 1000, diffMillis % 1000);
    }

    public static class HistLogger {

        private final String[] histLog;
        private int histIdx = 0;
        private int histCount = 1;

        public HistLogger() {
            this(30);
        }

        public HistLogger(int logCount) {
            histLog = new String[logCount];
        }

        public void hist(final String mess) {
            histLog[histIdx] = (histCount++) + " " + getDebugTime() + " " + mess + "                                   \n" + ExceptionTextFormatter.throwableToString(new Exception());
            histIdx = (histIdx + 1) % histLog.length;
        }
    }
}
