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

package org.radixware.kernel.server.units;

import java.sql.Connection;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.trace.IRadixTrace;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.trace.IServerThread;

public class UnitThread extends Thread implements IServerThread {

    private static final long DELAY_BEFORE_START_AFTER_FAILURE = 5000;
    private final Unit unit;
    private final LocalTracer localTracer;

    UnitThread(final Unit unit) {
        super();
        this.unit = unit;
        setName(unit.getFullTitle());
        setContextClassLoader(unit.getClass().getClassLoader());
        localTracer = unit.getTrace().newTracer(unit.getEventSource());
    }

    @Override
    public Connection getConnection() {
        return unit.getDbConnection();
    }

    @Override
    public IRadixTrace getTrace() {
        return unit.getTrace();
    }

    @Override
    public LocalTracer getLocalTracer() {
        return localTracer;
    }

    @Override
    public final void run() {
        boolean needPostponeBecauseStartImplReturnedFalse = false;// RADIX-3114
        try {
            if (unit.startImpl()) {// RADIX-3114
                unit.setState(UnitState.STARTED, null);
                unit.runImpl();
            } else {
                needPostponeBecauseStartImplReturnedFalse = true;
            }
        } catch (Throwable e) {
            if (e instanceof InterruptedException || Thread.currentThread().isInterrupted()) {
                unit.getTrace().put(EEventSeverity.EVENT, Messages.UNIT_THREAD_INTERRUPTED, Messages.MLS_ID_UNIT_THREAD_INTERRUPTED, new ArrStr(unit.getFullTitle()), unit.getEventSource(), false);
            } else {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                unit.getTrace().put(EEventSeverity.ERROR, Messages.UNIT_THREAD_INTERRUPTED_BY_EXCEPTION + exStack, Messages.MLS_ID_UNIT_THREAD_INTERRUPTED_BY_EXCEPTION, new ArrStr(unit.getFullTitle(), exStack), unit.getEventSource(), false);
            }
        } finally {
            interrupted();
            try {
                unit.setState(UnitState.STOPPING, null);
                unit.stopImpl();
            } catch (Throwable e) {
                logExceptionOnStop(e);
            } finally {
                try {
                    unit.setState(UnitState.STOPPED, null);
                    if (needPostponeBecauseStartImplReturnedFalse) {
                        unit.postponeStart(Messages.UNABLE_TO_START_UNIT);
                    } else if (!unit.isShutdownRequested() || unit.isPostponeStartAfterStopRequested()) {
                        //an unexpected failure or intended interruption for postponed start
                        unit.requestStopAndPostponedRestart(Messages.UNIT_THREAD_UNEXPECTED_STOP, System.currentTimeMillis() + DELAY_BEFORE_START_AFTER_FAILURE);
                    }
                    unit.setDbConnection(null);
                } catch (Throwable e) {
                    logExceptionOnStop(e);
                }
            }
            unit.getTrace().stopFileLogging();
        }
    }

    private void logExceptionOnStop(Throwable e) {
        final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
        unit.getTrace().put(EEventSeverity.ERROR, Messages.ERR_ON_UNIT_STOP + exStack, Messages.MLS_ID_ERR_ON_UNIT_STOP, new ArrStr(unit.getFullTitle(), exStack), unit.getEventSource(), false);
    }
}
