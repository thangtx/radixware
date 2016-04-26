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

package org.radixware.kernel.designer.debugger.impl.ui.annotations;

import java.beans.PropertyChangeEvent;
import org.netbeans.api.debugger.DebuggerEngine;
import org.netbeans.api.debugger.DebuggerManager;
import org.netbeans.api.debugger.DebuggerManagerAdapter;
import org.netbeans.api.debugger.Session;
import org.openide.text.Line;
import org.radixware.kernel.designer.debugger.RadixDebugger;
import org.radixware.kernel.designer.debugger.impl.CallStackFrame;
import org.radixware.kernel.designer.debugger.impl.ThreadWrapper;
import org.radixware.kernel.designer.debugger.impl.ThreadsCache;


public class StoppedThreadAnnotationListener extends DebuggerManagerAdapter {

    private RadixDebugger debugger;
    private ThreadWrapper currentThread;

    public StoppedThreadAnnotationListener() {
        DebuggerManager.getDebuggerManager().addDebuggerListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final String propertyName = evt.getPropertyName();
        if (DebuggerManager.PROP_CURRENT_ENGINE.equals(propertyName)) {
            lookupDebugger((DebuggerEngine) evt.getNewValue());
        } else if (ThreadsCache.PROPERTY_NAME_CURRENT_THREAD.equals(propertyName)) {
            updateCurrentThread((ThreadWrapper) evt.getNewValue());
        } else if (ThreadWrapper.PROPERTY_NAME_RESUMED.equals(propertyName)) {
            updateAnnotations();
        } else if (ThreadWrapper.PROPERTY_NAME_SUSPENDED.equals(propertyName)) {
            updateAnnotations();
        }
    }

    @Override
    public void sessionAdded(Session session) {
        updateAnnotations();
    }

    @Override
    public void sessionRemoved(Session session) {
        updateAnnotations();
    }

    private void lookupDebugger(DebuggerEngine engine) {
        RadixDebugger newDebugger;
        if (engine == null) {
            newDebugger = null;
        } else {
            newDebugger = engine.lookupFirst(null, RadixDebugger.class);
        }
        if (debugger == newDebugger) {
            return;
        }
        if (debugger != null) {
            debugger.getThreadsCache().removePropertyChangeListener(this);
        }

        debugger = newDebugger;
        if (debugger != null) {
            debugger.getThreadsCache().addPropertyChangeListener(this);
            updateCurrentThread(debugger.getCurrentThread());
        }
    }

    private void updateCurrentThread(ThreadWrapper tw) {
        if (tw == currentThread) {
            return;
        }
        if (currentThread != null) {
            currentThread.removePropertyChangeListener(this);
        }
        currentThread = tw;
        if (currentThread != null) {
            currentThread.addPropertyChangeListener(this);
        }
        updateAnnotations();
    }
    private CurrentPCAnnotation annotation = null;

    private void updateAnnotations() {
        if (annotation != null) {
            annotation.detach();
            annotation = null;
        }
        if (currentThread != null && currentThread.isSuspended()) {
            CallStackFrame frame = currentThread.getTopFrame();
            if (frame != null) {
                Line line = frame.getLine();
                if (line != null) {
                    annotation = new CurrentPCAnnotation(line);
                }
            }
        }
    }
}
