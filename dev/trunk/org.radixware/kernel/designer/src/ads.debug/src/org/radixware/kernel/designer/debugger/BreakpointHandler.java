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

package org.radixware.kernel.designer.debugger;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.Location;
import com.sun.jdi.Method;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VMDisconnectedException;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.BreakpointEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.MethodEntryRequest;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.radixware.kernel.designer.debugger.breakpoints.JmlBreakpoint;
import org.radixware.kernel.designer.debugger.breakpoints.MethodBreakpoint;
import org.radixware.kernel.designer.debugger.breakpoints.RadixBreakpoint;

import org.radixware.kernel.designer.debugger.impl.JVMEventHandler;
import org.radixware.kernel.designer.debugger.impl.ThreadWrapper;


class BreakpointHandler implements JVMEventHandler.PreconditionEventHandler {

    private final RadixBreakpoint bp;
    private List<EventRequest> requests = null;
    private ClassPrepareRequest cpRequest;
    private RadixDebugger debugger;

    BreakpointHandler(RadixDebugger debugger, RadixBreakpoint bp) {
        this.debugger = debugger;
        this.bp = bp;
        this.bp.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (JmlBreakpoint.PROP_ENABLED.equals(evt.getPropertyName())) {
                    synchronized (BreakpointHandler.this) {
                        if (requests != null) {
                            for (EventRequest request : requests) {
                                try {
                                    if (((Boolean) evt.getNewValue()).booleanValue()) {
                                        request.enable();
                                    } else {
                                        request.disable();
                                    }
                                } catch (VMDisconnectedException e) {
                                    request = null;
                                    cpRequest = null;
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public RadixBreakpoint getBreakpoint() {
        return bp;
    }

    public void initialize(final VirtualMachine vm, final JVMEventHandler eh) {
        if (bp.isInvalid()) {
            RadixDebuggerConsole.getInstance().getIO().getErr().println("Not able to submit breakpoint " + bp.getDisplayName() + ". Breakpoint is invalid");
            return;
        } else {
            synchronized (this) {

                if (bp.isInvalid()) {
                    RadixDebuggerConsole.getInstance().getIO().getErr().println("Not able to submit breakpoint " + bp.getDisplayName() + ". Breakpoint is invalid");
                    return;
                }

                if (bp instanceof MethodBreakpoint) {
                    if (requests == null) {

                        MethodEntryRequest request = vm.eventRequestManager().createMethodEntryRequest();
                        for (String s : bp.getClassName()) {
                            request.addClassFilter(s);
                        }
                        this.requests = new LinkedList<EventRequest>();
                        this.requests.add(request);
                        eh.registerEventHandler(request, this);
                        requests = Collections.singletonList((EventRequest) request);
                    }
                } else {

                    List<ReferenceType> classes = new ArrayList<ReferenceType>(vm.allClasses());
                    int matchCount = 0;
                    int tryCount = 0;
                    final List<String> matchNames = bp.getClassName();
                    loop:
                    for (ReferenceType clazz : classes) {
                        for (String className : matchNames) {
                            if (clazz.name().startsWith(className)) {
                                System.out.println(clazz.name());
                                tryCount++;
                                try {

                                    JmlBreakpoint jmlBp = (JmlBreakpoint) bp;
                                    List<Location> locations = clazz.locationsOfLine("Jml", jmlBp.getSourceName(), jmlBp.getLineNumber() + 1);
                                    if (!locations.isEmpty()) {
                                        Location location = locations.get(0);
                                        if (requests != null) {
                                            for (EventRequest rq : requests) {

                                                if (((BreakpointRequest) rq).location().equals(location)) {
                                                    continue loop;
                                                }
                                            }
                                        }

                                        BreakpointRequest request = vm.eventRequestManager().createBreakpointRequest(location);
                                        request.setSuspendPolicy(BreakpointRequest.SUSPEND_ALL);
                                        eh.registerEventHandler(request, this);
                                        if (jmlBp.isEnabled()) {
                                            request.enable();
                                        }
                                        if (requests == null) {
                                            requests = new LinkedList<>();
                                        }
                                        requests.add(request);
                                        matchCount++;
                                    }

                                } catch (AbsentInformationException ex) {
                                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                                }
                            }
                        }
                    }
                    if (tryCount > 0 && matchCount == 0) {
                        RadixDebuggerConsole.getInstance().getIO().getErr().println("Not able to submit breakpoint " + bp.getDisplayName() + ". No executable location to match breakpoint location");
                    }
                    if (cpRequest == null) {
                        cpRequest = vm.eventRequestManager().createClassPrepareRequest();
                        for (String s : bp.getClassName()) {
                            cpRequest.addClassFilter(s + "*");
                        }
                        eh.registerEventHandler(cpRequest, new JVMEventHandler.EventHandler() {
                            @Override
                            public boolean processEvent(Event event) {
                                if (event.request() == cpRequest) {
                                    initialize(vm, eh);
                                }
                                return true;
                            }
                        });
                        cpRequest.enable();
                    }
                }
            }
        }
    }

    public void dispose() {
        synchronized (this) {
            if (requests != null) {
                for (EventRequest request : requests) {
                    try {
                        request.disable();
                        request.virtualMachine().eventRequestManager().deleteEventRequest(request);
                    } catch (Throwable ex) {
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                    }
                }
                requests = null;
            }
            if (cpRequest != null) {
                try {
                    cpRequest.virtualMachine().eventRequestManager().deleteEventRequest(cpRequest);
                } catch (Throwable ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
                cpRequest = null;
            }
        }
    }

    @Override
    public boolean precondition(Event event) {
        EventRequest rq = event.request();
        if (requests != null && requests.contains(rq)) {
            if (event instanceof MethodEntryEvent) {
                MethodEntryEvent e = (MethodEntryEvent) event;
                Method method = e.method();
                return ((MethodBreakpoint) bp).isMatchToMethod(method);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean processEvent(Event event) {
        if (event instanceof BreakpointEvent) {
            try {
                BreakpointEvent e = (BreakpointEvent) event;
                ThreadWrapper wrapper = debugger.getThreadsCache().ensureThreadIsCached(e.thread());
                if (wrapper != null) {
                    debugger.setCurrentThread(wrapper);
                }
                return false;
            } catch (RuntimeException e) {
                return true;
            }
        } else {
            return true;
        }
    }
}
