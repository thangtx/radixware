/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.designer.debugger;

import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.Event;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.netbeans.spi.debugger.ContextProvider;
import org.netbeans.spi.debugger.ui.AttachType;
import org.netbeans.spi.debugger.ui.Controller;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.debugger.impl.JVMEventHandler;

/**
 *
 * @author akrylov
 */
@AttachType.Registration(displayName = "RadixWare Designer")
public class RadixDebuggerAttachType extends AttachType {

    private AttachController controller = new AttachController();

    @Override
    public JComponent getCustomizer() {
        return new AttachOptions(controller);
    }

    @Override
    public Controller getController() {
        return controller;
    }

   
    class AttachController implements Controller {

        private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

        @Override
        public boolean ok() {
            final BranchesLoadService[] service = new BranchesLoadService[1];
            service[0] = new BranchesLoadService(new Runnable() {

                @Override
                public void run() {
                    List<Branch> branches = service[0].getBranches();
                    if (branches.isEmpty()) {
                        return;
                    } else {
                        RadixDebugger.attach(branches.get(0));
                    }
                }
            });
            service[0].load();

            return true;
        }

        @Override
        public boolean cancel() {
            return true;
        }

        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener pl) {
            changeSupport.addPropertyChangeListener(pl);
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener pl) {
            changeSupport.removePropertyChangeListener(pl);
        }

        private void firePropertyChange(String propName, Object oldVal, Object newVal) {
            changeSupport.firePropertyChange(propName, oldVal, newVal);
        }

    }

}
