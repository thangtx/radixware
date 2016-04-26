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

package org.radixware.kernel.designer.common.dialogs.components.state;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;


public class StateManager {

    public enum State {

        OK,
        ERROR,
        WARNING;
    }
    private JComponent owner;
    State state;
    String message;

    public StateManager(JComponent owner) {
        this.owner = owner;
    }

    public void error(String message) {
        this.state = State.ERROR;
        this.message = message == null ? "<Null Error>" : message;
        invokeUpdate();
    }

    public void warning(String message) {
        this.state = State.WARNING;
        this.message = message == null ? "<Null Warning>" : message;
        invokeUpdate();
    }

    public void ok() {
        this.state = State.OK;
        this.message = null;
        invokeUpdate();
    }
    private void invokeUpdate(){
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                update();
            }
        });
    }

    public boolean isErrorneous() {
        return state == State.ERROR;
    }

    private void update() {
        IStateDisplayer d = IStateDisplayer.Locator.findInstance(owner);
        if (d != null) {
            d.getStateContext().update(this);
        }
    }
}
