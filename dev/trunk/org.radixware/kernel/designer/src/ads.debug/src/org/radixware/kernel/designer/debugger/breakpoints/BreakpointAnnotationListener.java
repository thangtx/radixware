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

package org.radixware.kernel.designer.debugger.breakpoints;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.netbeans.api.debugger.Breakpoint;
import org.netbeans.api.debugger.DebuggerManager;

import org.netbeans.api.debugger.DebuggerManagerAdapter;

public class BreakpointAnnotationListener extends DebuggerManagerAdapter implements PropertyChangeListener {

    private Map<JmlBreakpoint, JmlBreakpointAnnotation> breakpointToAnnotation = new HashMap<JmlBreakpoint, JmlBreakpointAnnotation>();
    private static volatile BreakpointAnnotationListener instance = null;

    public BreakpointAnnotationListener() {
        if (instance == null) {
            instance = this;
            DebuggerManager.getDebuggerManager().addDebuggerListener(instance);
        }
    }

    static BreakpointAnnotationListener getInstance() {
        if (instance == null) {
            instance = new BreakpointAnnotationListener();
        }
        return instance;
    }

    @Override
    public String[] getProperties() {
        return new String[]{DebuggerManager.PROP_BREAKPOINTS};
    }
    private final BreakpointsStore knownBreakpoints = BreakpointsStore.getInstance();

    @Override
    public void breakpointAdded(Breakpoint b) {
        if (this == instance) {
            if (!(b instanceof JmlBreakpoint)) {
                return;
            }
            addAnnotation((JmlBreakpoint) b);
            synchronized (knownBreakpoints) {
                knownBreakpoints.addBreakpoint((JmlBreakpoint) b);
            }
        }
    }

    @Override
    public void breakpointRemoved(Breakpoint b) {
        if (this == instance) {
            if (!(b instanceof JmlBreakpoint)) {
                return;
            }
            removeAnnotation((JmlBreakpoint) b);
            synchronized (knownBreakpoints) {
                knownBreakpoints.removeBreakpoint((JmlBreakpoint) b);
            }
        }
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source and
     * the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Breakpoint.PROP_ENABLED.equals(evt.getPropertyName())) {
            return;
        }
        if (evt.getSource() instanceof DebuggerManager) {
            DebuggerManager manager = (DebuggerManager) evt.getSource();
            Breakpoint[] breakpoints = manager.getBreakpoints();
            Set<JmlBreakpoint> allJmlBps = new HashSet<JmlBreakpoint>();
            for (Breakpoint bp : breakpoints) {
                if (bp instanceof JmlBreakpoint) {
                    JmlBreakpoint jmlbp = (JmlBreakpoint) bp;
                    if (!breakpointToAnnotation.containsKey(jmlbp)) {
                        addAnnotation(jmlbp);
                    } else {
                        allJmlBps.add(jmlbp);
                    }
                }
            }
            List<JmlBreakpoint> toRemove = new LinkedList<JmlBreakpoint>();
            for (JmlBreakpoint bp : breakpointToAnnotation.keySet()) {
                if (!allJmlBps.contains(bp)) {
                    toRemove.add(bp);
                }
            }
            for (JmlBreakpoint bp : toRemove) {
                removeAnnotation(bp);
            }
        }
    }

    private class EnablementListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getSource() instanceof JmlBreakpoint) {
                JmlBreakpointAnnotation annotation = breakpointToAnnotation.get((JmlBreakpoint) evt.getSource());
                annotation.rereadProperties();
            }
        }
    }
    private final EnablementListener enableListener = new EnablementListener();

    @SuppressWarnings("unchecked")
    void addAnnotation(JmlBreakpoint b) {
        if (b.getLine() != null) {
            if (!breakpointToAnnotation.containsKey(b)) {
                breakpointToAnnotation.put(
                        b,
                        new JmlBreakpointAnnotation(
                        b.isEnabled()
                        ? JmlBreakpointAnnotation.BREAKPOINT_ANNOTATION_TYPE
                        : JmlBreakpointAnnotation.DISABLED_BREAKPOINT_ANNOTATION_TYPE,
                        b));
            } else {
                JmlBreakpointAnnotation a = breakpointToAnnotation.get(b);
                a.rereadProperties();
            }
            b.addPropertyChangeListener(
                    Breakpoint.PROP_ENABLED,
                    enableListener);
        }
    }

    private void removeAnnotation(JmlBreakpoint b) {
        JmlBreakpointAnnotation annotation = breakpointToAnnotation.remove(b);
        if (annotation == null) {
            return;
        }
        annotation.detach();
        b.removePropertyChangeListener(Breakpoint.PROP_ENABLED, enableListener);
    }

    @Override
    public Breakpoint[] initBreakpoints() {
        if (this == instance) {
            List<JmlBreakpoint> initialized = knownBreakpoints.initBreakpoints();
            return initialized.toArray(new Breakpoint[initialized.size()]);
        } else {
            return new Breakpoint[0];
        }
    }
}
