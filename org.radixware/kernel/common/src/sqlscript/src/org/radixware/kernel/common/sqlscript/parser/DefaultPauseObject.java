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

package org.radixware.kernel.common.sqlscript.parser;

import java.util.logging.Level;
import java.util.logging.Logger;


public class DefaultPauseObject implements IPauseObject {

    private boolean blocked = false;
    private boolean stepMode = false;

    @Override
    public boolean isStepMode() {
        return stepMode;
    }

    @Override
    public synchronized void setStepMode(boolean stepMode) {
        this.stepMode = stepMode;
        this.blocked  = stepMode;
    }

    @Override
    public synchronized void waitForNextStep() {
        if (!stepMode) {
            return;
        }
        while (blocked) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(IPauseObject.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        blocked = true;//restoring block
    }

    @Override
    public synchronized void allowNextStep() {
        if (stepMode) {
            blocked = false;
            notifyAll();
        } else {
            throw new IllegalStateException("There is no point to call allowNextStep() in non-step mode");
        }
    }
}
