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

package org.radixware.kernel.explorer.webdriver.input;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEventLoop;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QTimerEvent;
import com.trolltech.qt.gui.QApplication;
import org.radixware.kernel.explorer.webdriver.WebDrvSession;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;

final class ActionsDispatcher extends QObject {

    private static final class DispatchTickActionsEvent extends QEvent {

        private final int tick;
        private final int duration;

        public DispatchTickActionsEvent(final int tick, final int duration) {
            super(QEvent.Type.User);
            this.tick = tick;
            this.duration = duration;
        }

        public int getTick() {
            return tick;
        }
        
        public int getDuration(){
            return duration;                    
        }
        
    }
    private final QEventLoop dispatchEventLoop = new QEventLoop(this);
    private final WebDrvSession session;
    private final InputActions actions;
    private int timerId;
    private QEvent postponedEvent;
    private WebDrvException dispatchError;

    public ActionsDispatcher(final WebDrvSession session, InputActions actions) {
        this.session = session;
        this.actions = actions;
    }

    public void dispatch(int duration) throws WebDrvException {
        QApplication.postEvent(this, new DispatchTickActionsEvent(0, duration));
        dispatchEventLoop.exec();
        if (dispatchError != null) {
            final WebDrvException exception = dispatchError;
            dispatchError = null;
            throw exception;
        }
    }

    public void stop() {
        dispatchEventLoop.exit();
    }

    public void dispatchTick(final int tick, final int pause, final int duration) {
        final QEvent event = new DispatchTickActionsEvent(tick, duration);
        if (pause < 1) {
            QApplication.postEvent(this, event);
        } else {
            postponedEvent = event;
            timerId = startTimer(timerId);
        }
    }

    @Override
    protected void timerEvent(QTimerEvent event) {
        if (event.timerId() == timerId) {
            event.accept();
            killTimer(timerId);
            timerId = 0;
            QApplication.postEvent(this, postponedEvent);
            postponedEvent = null;
        } else {
            super.timerEvent(event);
        }
    }

    @Override
    protected void customEvent(final QEvent event) {
        if (event instanceof DispatchTickActionsEvent) {
            final DispatchTickActionsEvent dispatchEvent = (DispatchTickActionsEvent)event;
            dispatchEvent.accept();
            try {
                actions.dispatch(this, session, dispatchEvent.getTick(), dispatchEvent.getDuration());
            } catch (WebDrvException exception) {
                dispatchError = exception;
                dispatchEventLoop.exit();
            }
        } else {
            super.customEvent(event);
        }
    }

}
