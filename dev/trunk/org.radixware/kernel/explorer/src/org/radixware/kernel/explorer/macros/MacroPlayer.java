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

package org.radixware.kernel.explorer.macros;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QTimerEvent;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.macros.actions.IMacroAction;


public final class MacroPlayer extends QObject {

    public final Signal0 finished = new Signal0();
    private final IClientEnvironment environment;

    public MacroPlayer(final IClientEnvironment environment) {
        this.environment = environment;
    }
    private final static int TIME_INTERVAL_MILLS = 64;

    private static class DoNextAction extends QEvent {

        public DoNextAction() {
            super(QEvent.Type.User);
        }
    }
    private Macros currentMacros;
    private int currentActionIndex, timer;

    @Override
    protected void timerEvent(final QTimerEvent event) {
        if (event.timerId() == timer && !environment.getEasSession().isBusy()) {
            final IMacroAction action = currentMacros.getActions().get(currentActionIndex);
            if (!action.canExecuteNow()) {
                return;
            }
            try {
                action.execute(null);
                currentActionIndex++;
                if (currentActionIndex >= currentMacros.getActions().size()) {
                    killTimer(timer);
                    timer = 0;
                    finished.emit();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                killTimer(timer);
                timer = 0;
                finished.emit();
            }
        } else {
            super.timerEvent(event);
        }
    }

    public void runMacros(final Macros macros) {
        currentMacros = macros;
        currentActionIndex = 0;
        timer = startTimer(TIME_INTERVAL_MILLS);
    }
}
