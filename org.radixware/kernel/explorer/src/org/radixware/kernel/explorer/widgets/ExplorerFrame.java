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

package org.radixware.kernel.explorer.widgets;

import com.trolltech.qt.core.QTimerEvent;
import com.trolltech.qt.core.Qt.WindowFlags;
import com.trolltech.qt.core.Qt.WindowType;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.widgets.IPeriodicalTask;
import org.radixware.kernel.common.client.widgets.TimerEventHandler;


public class ExplorerFrame extends QFrame implements org.radixware.kernel.common.client.widgets.IWidget {

    private final IClientEnvironment environment;

    public ExplorerFrame(IClientEnvironment environment, QPrivateConstructor p) {
        super(p);
        this.environment = environment;
    }

    public ExplorerFrame(IClientEnvironment environment, QWidget parent, WindowFlags f) {
        super(parent, f);
        this.environment = environment;
    }

    public ExplorerFrame(IClientEnvironment environment) {
        this.environment = environment;
    }

    public ExplorerFrame(IClientEnvironment environment, QWidget parent) {
        super(parent);
        this.environment = environment;
    }

    public ExplorerFrame(IClientEnvironment environment, QWidget parent, WindowType[] f) {
        super(parent, f);
        this.environment = environment;
    }

    public IClientEnvironment getEnvironment() {
        return environment;
    }

    @Override
    public String getObjectName() {
        return objectName();
    }

    @Override
    public boolean isDisposed() {
        return nativeId()==0;
    }
    
    private final TimersController timers = new TimersController();
    
    public IPeriodicalTask startTimer(final TimerEventHandler handler, final int interval) {
        return timers.startTimer(handler, interval, this);
    }

    @Override
    public IPeriodicalTask startTimer(final TimerEventHandler handler) {
        return timers.startTimer(handler, this);
    }

    @Override
    public void killTimer(final IPeriodicalTask task) {
        timers.killTimer(task, this);
    }

    @Override
    protected void timerEvent(final QTimerEvent event) {
        if (!timers.processTimerEvent(event)){
            super.timerEvent(event);
        }
    }

    @Override
    protected void closeEvent(QCloseEvent event) {
        timers.clearTimers();
        super.closeEvent(event);
    }    
}
