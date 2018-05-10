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

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

/**
 *
 * Abstract modal dialog which supports the state label on its bottom
 * and tracks the corresponding "OK"/"Cancel" buttons state
 */
public abstract class StateAbstractDialog extends ModalDisplayer {

    public static abstract class StateAbstractPanel extends JPanel{

        public boolean isOk(){
            return !stateManager.isErrorneous();
        }

        //update stateManager here: stateManager.setError(..) or stateManager.ok(); and fire change event
        public abstract void check();

        protected ChangeSupport changeSupport = new ChangeSupport(this);

        public final void addChangeListener(ChangeListener l) {
            changeSupport.addChangeListener(l);
        }

        public final void removeChangeListener(ChangeListener l) {
            changeSupport.removeChangeListener(l);
        }

        protected StateManager stateManager = new StateManager(this);
        
        public String getMessage() {
            return stateManager.getMessage();
        }
    }

    public StateAbstractDialog(final StateAbstractPanel panel, final String title) {
        super(panel, title);

        panel.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
               updateDialogDescriptor();
            }
        });

        updateDialogDescriptor();
    }

    private void updateDialogDescriptor(){
        getDialogDescriptor().setValid( ((StateAbstractPanel)getComponent()).isOk());
    }

}
