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

package org.radixware.kernel.server;

import java.util.Collections;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.SimpleLog;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.trace.TraceItem;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.trace.FileLog;
import org.radixware.kernel.server.trace.FileLogOptions;


public class ServerStartupLog implements Log {

    private final Log delegateLog;
    private FileLog fileDuplicator;
    private final String name;

    public ServerStartupLog(final String name) {
        this.name = name;
        delegateLog = new SimpleLog(name);
    }

    public void enableDuplicationToFile(final FileLogOptions options) {
        try {
            fileDuplicator = new FileLog(options);
        } catch (Exception ex) {
            error("Error on creating file log", ex);
        }
    }

    public void disableDuplicationToFile() {
        if (fileDuplicator != null) {
            final FileLog duplicatorSnapshot = fileDuplicator;
            fileDuplicator = null;//if exception will be thrown on duplicator closing, it shouldn't be duplicated
            try {
                duplicatorSnapshot.close();
            } catch (Exception ex) {
                error("Error while closing log file", ex);
            }
        }
    }

    private String getFormattedMessage(final EEventSeverity severity, final String text, final Throwable t) {
        final StringBuilder sb = new StringBuilder();
        if (text != null && !text.isEmpty()) {
            sb.append(text);
            if (t != null) {
                sb.append(":\n");
            }
        }
        if (t != null) {
            sb.append(ExceptionTextFormatter.throwableToString(t));
        }
        return new TraceItem(null, severity, null, Collections.singletonList(sb.toString()), name).toString();
    }

    private void duplicateToFile(final EEventSeverity severity, final Object mess) {
        duplicateToFile(severity, mess, null);
    }

    private void duplicateToFile(final EEventSeverity severity, final Object mess, final Throwable t) {
        if (fileDuplicator != null) {
            fileDuplicator.log(getFormattedMessage(severity, mess.toString(), t));
        }
    }

    @Override
    public void warn(Object o, Throwable thrwbl) {
        delegateLog.warn(o, thrwbl);
        duplicateToFile(EEventSeverity.WARNING, o, thrwbl);
    }

    @Override
    public void warn(Object o) {
        delegateLog.warn(o);
        duplicateToFile(EEventSeverity.WARNING, o);
    }

    @Override
    public void trace(Object o, Throwable thrwbl) {
        delegateLog.trace(o, thrwbl);
        duplicateToFile(EEventSeverity.EVENT, o, thrwbl);
    }

    @Override
    public void trace(Object o) {
        delegateLog.trace(o);
        duplicateToFile(EEventSeverity.EVENT, o);
    }

    @Override
    public boolean isWarnEnabled() {
        return delegateLog.isWarnEnabled();
    }

    @Override
    public boolean isTraceEnabled() {
        return delegateLog.isTraceEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return delegateLog.isInfoEnabled();
    }

    @Override
    public boolean isFatalEnabled() {
        return delegateLog.isFatalEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return delegateLog.isErrorEnabled();
    }

    @Override
    public boolean isDebugEnabled() {
        return delegateLog.isDebugEnabled();
    }

    @Override
    public void info(Object o, Throwable thrwbl) {
        delegateLog.info(o, thrwbl);
        duplicateToFile(EEventSeverity.EVENT, o, thrwbl);
    }

    @Override
    public void info(Object o) {
        delegateLog.info(o);
        duplicateToFile(EEventSeverity.EVENT, o);
    }

    @Override
    public void fatal(Object o, Throwable thrwbl) {
        delegateLog.fatal(o, thrwbl);
        duplicateToFile(EEventSeverity.ERROR, o, thrwbl);
    }

    @Override
    public void fatal(Object o) {
        delegateLog.fatal(o);
        duplicateToFile(EEventSeverity.ERROR, o);
    }

    @Override
    public void error(Object o, Throwable thrwbl) {
        delegateLog.error(o, thrwbl);
        duplicateToFile(EEventSeverity.ERROR, o, thrwbl);
    }

    @Override
    public void error(Object o) {
        delegateLog.error(o);
        duplicateToFile(EEventSeverity.ERROR, o);
    }

    @Override
    public void debug(Object o, Throwable thrwbl) {
        delegateLog.debug(o, thrwbl);
        duplicateToFile(EEventSeverity.EVENT, o, thrwbl);
    }

    @Override
    public void debug(Object o) {
        delegateLog.debug(o);
        duplicateToFile(EEventSeverity.EVENT, o);
    }
}
