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

import java.io.PrintWriter;
import java.io.StringWriter;

public final class ExceptionTextFormatter {

    /**
     * @param e
     * @return result of e.printStackTrace(...)
     */
    public static String exceptionStackToString(final Throwable e) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.close();
        return sw.toString();
    }

    public static String getExceptionMess(final Throwable e) {
        return e.getClass().getName() + (e.getMessage() != null ? ": " + e.getMessage() : "");
    }

    /**
     * @return full information about exception: message, causes, stack of
     * initial cause.
     */
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

    public static String getCurrentStackAsStr() {
        String stack = ExceptionTextFormatter.exceptionStackToString(new Exception());
        int stackStartIdx = stack.indexOf("\n", stack.indexOf("\n") + 1) + 1;
        if (stackStartIdx >= 0 && stackStartIdx < stack.length()) {
            stack = stack.substring(stackStartIdx);
        }
        return stack;
    }
}
