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

import java.io.IOException;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.trace.IRadixTrace;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.aio.EventDispatcher;
import org.radixware.kernel.server.aio.EventHandler;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.instance.UnitCommand;
import org.radixware.kernel.server.monitoring.MonitorFactory;

public abstract class AsyncEventHandlerUnit extends Unit implements EventHandler {

    private EventDispatcher dispatcher = null;

    public AsyncEventHandlerUnit(final Instance instModel, final Long id, final String title, final MonitorFactory factory) {
        super(instModel, id, title, factory);
    }

    public AsyncEventHandlerUnit(final Instance instModel, final Long id, final String title) {
        super(instModel, id, title, null);
    }

    @Override
    protected boolean startImpl() throws Exception {
        if (super.startImpl()) {
            dispatcher = new EventDispatcher();
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void stopImpl() {
        //during stop some events can be schedulled (send before close, for example) let's process them
        if (dispatcher != null) {
            processAllEventsSuppressingRuntimeExceptions();
            dispatcher.close();
            dispatcher = null;
        }
        super.stopImpl();
    }

    @Override
    public boolean enqueueUnitCommand(UnitCommand command) {
        if (super.enqueueUnitCommand(command)) {
            getDispatcher().wakeup();
            return true;
        }
        return false;
    }

    protected void processAllEventsSuppressingRuntimeExceptions() {
        doProcessAllEvents(true);
    }

    protected void processAllEvents() {
        doProcessAllEvents(false);
    }

    private void doProcessAllEvents(boolean ignoreExceptions) {
        if (dispatcher != null) {
            while (dispatcher.getNonIgnorableOnStopEventSubscribersCount() > 0 && !Thread.currentThread().isInterrupted()) {
                try {
                    dispatcher.process();
                } catch (IOException e) {//selector internal error
                    getTrace().put(EEventSeverity.ERROR, ExceptionTextFormatter.exceptionStackToString(e), null, null, getEventSource(), false);
                    return;
                } catch (RuntimeException re) {
                    if (!ignoreExceptions) {
                        throw re;
                    } else {
                        getTrace().put(EEventSeverity.ERROR, ExceptionTextFormatter.exceptionStackToString(re), null, null, getEventSource(), false);
                    }
                }
            }
        }
    }

    @Override
    protected void maintenanceImpl() throws InterruptedException {
        try {
            dispatcher.process();
        } catch (IOException e) {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            getTrace().put(EEventSeverity.ERROR, ExceptionTextFormatter.exceptionStackToString(e), null, null, getEventSource(), false);
        }
    }

    @Override
    protected boolean requestShutdownImpl() {
        if (dispatcher != null) {
            dispatcher.wakeup();
        }
        return true;
    }

    protected void ensureTimerEventNow() {
        getDispatcher().unsubscribeFromTimerEvents(this);
        getDispatcher().waitEvent(new EventDispatcher.TimerEvent(), this, System.currentTimeMillis());
    }

    public final EventDispatcher getDispatcher() {
        return dispatcher;
    }
}
