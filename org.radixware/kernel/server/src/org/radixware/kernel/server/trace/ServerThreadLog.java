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

import org.apache.commons.logging.Log;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.SrvRunParams;
import org.radixware.kernel.server.instance.Instance;


public class ServerThreadLog implements Log {

    private final String name;
    private final LocalTracer fallbackTracer = Instance.get().getTrace().newTracer(EEventSource.INSTANCE.getValue());

    public ServerThreadLog(final String name) {
        this.name = name;
    }

    private LocalTracer getLocalTracer() {
        if (Thread.currentThread() instanceof IServerThread) {
            return ((IServerThread) Thread.currentThread()).getLocalTracer();
        }
        return fallbackTracer;
    }

    @Override
    public boolean isDebugEnabled() {
        return isSeveretyEnabled(EEventSeverity.DEBUG) && SrvRunParams.isDetailed3rdPartyLoggingEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return isSeveretyEnabled(EEventSeverity.ERROR);
    }

    @Override
    public boolean isFatalEnabled() {
        return isSeveretyEnabled(EEventSeverity.ERROR);
    }

    @Override
    public boolean isInfoEnabled() {
        return isSeveretyEnabled(EEventSeverity.DEBUG);
    }

    @Override
    public boolean isTraceEnabled() {
        return isSeveretyEnabled(EEventSeverity.DEBUG) && SrvRunParams.isDetailed3rdPartyLoggingEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return isSeveretyEnabled(EEventSeverity.WARNING);
    }

    private boolean isSeveretyEnabled(final EEventSeverity severity) {
        if (severity == null) {
            return false;
        }
        return getLocalTracer().getMinSeverity() <= severity.getValue().longValue();
    }

    private void put(final EEventSeverity severity, final Object o, final Throwable trbl) {
        if (!isSeveretyEnabled(severity)) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(name);
        if (o != null) {
            sb.append(": ");
            sb.append(o.toString());
        }
        if (trbl != null) {
            sb.append("\nCaused by:\n");
            sb.append(ExceptionTextFormatter.throwableToString(trbl));
        }
        getLocalTracer().put(severity, sb.toString(), null, null, true);
    }

    public void event(final Object o, final Throwable t) {
        put(EEventSeverity.EVENT, o, t);
    }

    @Override
    public void trace(final Object o) {
        put(EEventSeverity.DEBUG, o, null);
    }

    @Override
    public void trace(final Object o, final Throwable thrwbl) {
        put(EEventSeverity.DEBUG, o, thrwbl);
    }

    @Override
    public void debug(final Object o) {
        put(EEventSeverity.DEBUG, o, null);
    }

    @Override
    public void debug(final Object o, final Throwable thrwbl) {
        put(EEventSeverity.DEBUG, o, thrwbl);
    }

    @Override
    public void info(Object o) {
        put(EEventSeverity.DEBUG, o, null);
    }

    @Override
    public void info(Object o, Throwable thrwbl) {
        put(EEventSeverity.DEBUG, o, thrwbl);
    }

    @Override
    public void warn(Object o) {
        put(EEventSeverity.WARNING, o, null);
    }

    @Override
    public void warn(Object o, Throwable thrwbl) {
        put(EEventSeverity.WARNING, o, thrwbl);
    }

    @Override
    public void error(Object o) {
        put(EEventSeverity.ERROR, o, null);
    }

    @Override
    public void error(Object o, Throwable thrwbl) {
        put(EEventSeverity.ERROR, o, thrwbl);
    }

    @Override
    public void fatal(Object o) {
        put(EEventSeverity.ERROR, o, null);
    }

    @Override
    public void fatal(Object o, Throwable thrwbl) {
        put(EEventSeverity.ERROR, o, thrwbl);
    }
}
