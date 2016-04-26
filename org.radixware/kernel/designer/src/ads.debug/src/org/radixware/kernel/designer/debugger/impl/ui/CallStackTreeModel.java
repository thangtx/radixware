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

package org.radixware.kernel.designer.debugger.impl.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.netbeans.spi.debugger.ContextProvider;
import org.netbeans.spi.viewmodel.ModelEvent;
import org.radixware.kernel.designer.debugger.impl.CallStackFrame;
import org.radixware.kernel.designer.debugger.impl.ThreadWrapper;
import org.radixware.kernel.designer.debugger.impl.ThreadsCache;


public class CallStackTreeModel extends AbstractTreeModel {

    private final Listener listener;

    public CallStackTreeModel(ContextProvider contextProvider) {
        super(contextProvider, 300);
        this.listener = new Listener();
        debugger.getThreadsCache().addPropertyChangeListener(listener);
        ThreadWrapper currentThread = debugger.getCurrentThread();
        if (currentThread != null) {
            currentThread.addPropertyChangeListener(listener);
        }
    }

    private class Listener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (ThreadsCache.PROPERTY_NAME_CURRENT_THREAD.equals(evt.getPropertyName())) {
                ThreadWrapper oldCurrentThread = (ThreadWrapper) evt.getOldValue();
                if (oldCurrentThread != null) {
                    oldCurrentThread.removePropertyChangeListener(this);
                }
                ThreadWrapper newCurrentThread = (ThreadWrapper) evt.getNewValue();
                if (newCurrentThread != null) {
                    newCurrentThread.addPropertyChangeListener(this);
                }
                fireModelChange();
            } else if (ThreadWrapper.PROPERTY_NAME_SUSPENDED.equals(evt.getPropertyName())) {
                fireModelChange();
            } else if (ThreadWrapper.PROPERTY_NAME_CURRENT_FRAME.equals(evt.getPropertyName())) {
                final List<Object> nodes = new LinkedList<Object>();

                CallStackFrame old = (CallStackFrame) evt.getOldValue();
                if (old != null) {
                    nodes.add(old);
                }
                CallStackFrame another = (CallStackFrame) evt.getNewValue();
                if (another != null) {
                    nodes.add(another);
                }
                fireNodeChange(nodes, ModelEvent.NodeChanged.DISPLAY_NAME_MASK);
            }
        }
    }
    private final Object updateLock = new Object();

    @Override
    protected List<Object> getChildren(Object parent) {
        synchronized (updateLock) {
            List<Object> result;
            if (parent == ROOT) {
                ThreadWrapper ct = debugger.getCurrentThread();
                if (ct == null || !ct.isSuspended()) {
                    result = Collections.singletonList((Object) "No information to display at this time");
                } else {
                    result = new ArrayList<Object>(ct.getStackFrames());
                }
            } else {
                result = Collections.emptyList();
            }
            return result;
        }
    }
}
