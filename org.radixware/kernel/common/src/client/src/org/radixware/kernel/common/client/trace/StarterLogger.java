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

package org.radixware.kernel.common.client.trace;

import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.enums.EEventSeverity;


class StarterLogger implements org.apache.commons.logging.Log{

    private final ClientTracer tracer;
    private final String name;

    StarterLogger(final String name, final ClientTracer tracer){
        this.name = name;
        this.tracer = tracer;
    }

    @Override
    public boolean isDebugEnabled() {
        return isSeveretyEnabled(EEventSeverity.DEBUG);
    }

    @Override
    public boolean isErrorEnabled() {
        return isSeveretyEnabled(EEventSeverity.ERROR);
    }

    @Override
    public boolean isFatalEnabled() {
        return isSeveretyEnabled(EEventSeverity.ALARM);
    }

    @Override
    public boolean isInfoEnabled() {
        return isSeveretyEnabled(EEventSeverity.DEBUG);
    }

    @Override
    public boolean isTraceEnabled() {
        return isSeveretyEnabled(EEventSeverity.DEBUG);
    }

    @Override
    public boolean isWarnEnabled() {
        return isSeveretyEnabled(EEventSeverity.WARNING);
    }

    private boolean isSeveretyEnabled(final EEventSeverity severity) {
        if (severity == null) {
            return false;
        }
        return tracer.getProfile().getMinSeverity()<= severity.getValue().longValue();
    }

    private void put(final EEventSeverity severity, final Object o, final Throwable trbl) {
        final StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(name);
        if (o != null) {
            messageBuilder.append(": ");
            messageBuilder.append(String.valueOf(o));
        }
        if (trbl != null) {
            messageBuilder.append("\nCaused by:\n");
            messageBuilder.append(ClientException.exceptionStackToString(trbl));
        }
        tracer.put(severity, messageBuilder.toString());
    }


    @Override
    public void trace(final Object o) {
        put(EEventSeverity.DEBUG,o, null);
    }

    @Override
    public void trace(final Object o, final Throwable thrwbl) {
        put(EEventSeverity.DEBUG,o, thrwbl);
    }

    @Override
    public void debug(final Object o) {
        put(EEventSeverity.DEBUG,o, null);
    }

    @Override
    public void debug(final Object o, final Throwable thrwbl) {
        put(EEventSeverity.DEBUG,o, thrwbl);
    }

    @Override
    public void info(final Object o) {
        put(EEventSeverity.DEBUG,o, null);
    }

    @Override
    public void info(final Object o, final Throwable thrwbl) {
        put(EEventSeverity.DEBUG,o, thrwbl);
    }

    @Override
    public void warn(final Object o) {
        put(EEventSeverity.WARNING,o, null);
    }

    @Override
    public void warn(final Object o, final Throwable thrwbl) {
        put(EEventSeverity.WARNING,o, thrwbl);
    }

    @Override
    public void error(final Object o) {
        put(EEventSeverity.ERROR,o, null);
    }

    @Override
    public void error(final Object o, final Throwable thrwbl) {
        put(EEventSeverity.ERROR,o, thrwbl);
    }

    @Override
    public void fatal(final Object o) {
        put(EEventSeverity.ALARM,o, null);
    }

    @Override
    public void fatal(final Object o, final Throwable thrwbl) {
        put(EEventSeverity.ALARM,o, thrwbl);
    }
}
