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

import java.awt.Component;
import java.awt.Container;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import javax.swing.JComponent;


public interface IStateDisplayer {

    static final String STATE_DISPLAYER_PROPERTy_NAME = "RadixDialogStateDisplayerInstance";

    static abstract class Locator {

        static IStateDisplayer findInstance(JComponent c) {
            Component parent = c;
            while (parent instanceof JComponent) {
                Object registeredInstance = ((JComponent) parent).getClientProperty(STATE_DISPLAYER_PROPERTy_NAME);
                if (registeredInstance instanceof IStateDisplayer) {
                    return (IStateDisplayer) registeredInstance;
                }
                parent = parent.getParent();
            }
            return null;
        }

        public static void register(IStateDisplayer displayer, Container c) {
            if (c instanceof JComponent) {
                ((JComponent) c).putClientProperty(STATE_DISPLAYER_PROPERTy_NAME, displayer);
            }
        }

        public static void unregister(IStateDisplayer displayer, Container c) {
            if (c instanceof JComponent) {
                Object registeredInstance = ((JComponent) c).getClientProperty(STATE_DISPLAYER_PROPERTy_NAME);
                if (registeredInstance == displayer) {
                    ((JComponent) c).putClientProperty(STATE_DISPLAYER_PROPERTy_NAME, null);
                }
            }
        }
    }

    public abstract class StateContext {

        public abstract void updateMessage(StateManager.State state, String newMessage);
        public ArrayList<WeakReference<StateManager>> refs = new ArrayList<WeakReference<StateManager>>();
        public synchronized void reset(){
            this.refs.clear();
        }
        public synchronized void update(StateManager source) {
            ArrayList<StateManager> list = new ArrayList<StateManager>();

            boolean isNew = true;
            for (WeakReference<StateManager> ref : refs) {
                StateManager m = ref.get();
                if (m != null && m.state != StateManager.State.OK) {
                    list.add(m);
                    if (m == source) {
                        isNew = false;
                    }
                }
            }

            if (isNew) {
                list.add(source);
            }
            StateManager error = null;
            StateManager warning = null;

            for (StateManager m : list) {
                if (m.state == StateManager.State.ERROR) {
                    error = m;
                    break;
                } else if (m.state == StateManager.State.WARNING) {
                    warning = m;
                }
            }

            if (error != null) {
                updateMessage(StateManager.State.ERROR, error.message);
            } else {
                if (warning != null) {
                    updateMessage(StateManager.State.WARNING, warning.message);
                } else {
                    updateMessage(StateManager.State.OK, "");
                }
            }
            refs.clear();
            for (StateManager m : list) {
                refs.add(new WeakReference<StateManager>(m));
            }
        }
    }

    public StateContext getStateContext();
}
