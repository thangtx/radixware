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

package org.radixware.kernel.license;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LicenseManagerLog {

    private static Logger delegate = null;

    static {
        delegate = new Logger("license.client.fallback.log", null) {

            private final ConsoleHandler handler = new ConsoleHandler();

            @Override
            public void log(LogRecord record) {
                if (record.getLevel().intValue() >= Level.WARNING.intValue()) {
                    handler.publish(record);
                }
            }
        };
    }

    static synchronized void setDelegate(final Logger delegateLogger) {
        delegate = delegateLogger;
    }

    public static synchronized void warning(final String message) {
        if (delegate != null) {
            delegate.log(Level.WARNING, message);
        }
    }

    public static synchronized void event(final String message) {
        if (delegate != null) {
            delegate.log(Level.INFO, message);
        }
    }

    public static void error(final String message) {
        error(message, null);
    }

    public static synchronized void error(final String message, final Throwable t) {
        if (delegate != null) {
            if (t == null) {
                delegate.log(Level.SEVERE, message);
            } else {
                delegate.log(Level.SEVERE, String.format("%s: %s", message, throwableToString(t)));
            }
        }
    }

    public static String throwableToString(final Throwable throwable) {
        final StringBuffer sb = new StringBuffer();
        sb.append(throwable.getMessage());
        Throwable last = throwable;
        for (Throwable cause = throwable.getCause(); cause != null; cause = cause.getCause()) {
            sb.append("\nCause:\n");
            sb.append(cause.getMessage());
            last = cause;
        }
        final String stackAsStr = exceptionStackToString(last);
        sb.append("\nStack:\n");
        sb.append(stackAsStr);
        return sb.toString();
    }

    public static String exceptionStackToString(final Throwable e) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.close();
        return sw.toString();
    }
}
