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

package org.radixware.kernel.designer.debugger.impl;

import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.Location;
import com.sun.jdi.Method;
import com.sun.jdi.ObjectCollectedException;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VMDisconnectedException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.debugger.DebuggerManager;
import org.openide.util.Exceptions;
import org.radixware.kernel.designer.debugger.RadixDebugger;


public class ThreadWrapper {

    public static final String PROPERTY_NAME_SUSPENDED = "thread-suspended-property";
    public static final String PROPERTY_NAME_RESUMED = "thread-resumed-property";
    public static final String PROPERTY_NAME_STARTED = "thread-started-property";
    public static final String PROPERTY_NAME_DIED = "thread-died-property";
    public static final String PROPERTY_NAME_CURRENT_FRAME = "current-frame";
    final ReadWriteLock accessLock = new ReentrantReadWriteLock();
    private final WeakReference<ThreadReference> ref;
    private final Map<StackFrame, CallStackFrame> frames = new WeakHashMap<StackFrame, CallStackFrame>();
    private final PropertyChangeSupport propChangeSupport = new PropertyChangeSupport(this);
    private RadixDebugger debugger;
    private CallStackFrame currentFrame;

    public ThreadWrapper(RadixDebugger debugger, ThreadReference ref) {
        this.ref = new WeakReference<ThreadReference>(ref);
        this.debugger = debugger;
    }

    public ThreadReference getThreadReference() {
        return this.ref.get();
    }

    public boolean isCurrent() {
        return debugger.getCurrentThread() == this;
    }

    public void setCurrent() {
        DebuggerManager.getDebuggerManager().setCurrentSession(debugger.getSession());
        debugger.setCurrentThread(this);
    }

    public void updateCurrentFrame() {
        CallStackFrame frame = getCurrentFrame();
        if (frame != null) {
            frame.setCurrent();
        }
    }

    public void availableLocations() {
        final ThreadReference thread = getThreadReference();
        if (thread != null) {
            try {
                final StackFrame frame = thread.frame(0);
                final Method method = frame.location().method();
                if (method != null) {
                    final List<Location> locations = method.locationsOfLine(frame.location().lineNumber());
                    if (locations != null) {
                        System.out.println(locations.size());
                    }
                }
            } catch (Throwable ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }
    }

    public CallStackFrame getCurrentFrame() {
        if (isSuspended()) {
            synchronized (this) {
                if (currentFrame == null) {
                    try {
                        ThreadReference thread = getThreadReference();
                        if (thread.frameCount() > 0) {
                            currentFrame = getCallStackFrame(0);

                        }
                    } catch (IncompatibleThreadStateException ex) {
                        return null;
                    }
                }
                return currentFrame;
            }
        } else {
            return null;
        }
    }

    public void invalidateFrameList() {
        synchronized (this) {
            currentFrame = null;
            frames.clear();
        }
    }

    public CallStackFrame getTopFrame() {
        if (isSuspended()) {
            synchronized (this) {
                try {
                    ThreadReference thread = getThreadReference();
                    if (thread.frameCount() > 0) {
                        return getCallStackFrame(0);
                    }
                } catch (IncompatibleThreadStateException ex) {
                    return null;
                }

                return null;
            }
        } else {
            return null;
        }
    }

    public void setCurrentFrame(CallStackFrame frame) {
        if (frame != null && frame.getOwnerThread() == this) {
            synchronized (this) {
                CallStackFrame old = currentFrame;
                CallStackFrame current = null;
                synchronized (frames) {
                    if (frames.get(frame.getFrame()) == frame) {
                        current = this.currentFrame = frame;
                    }
                }
                if (current != null) {
                    propChangeSupport.firePropertyChange(new PropertyChangeEvent(this, PROPERTY_NAME_CURRENT_FRAME, old, current));
                }
            }
        }
    }

    private CallStackFrame getCallStackFrame(int index) {
        synchronized (frames) {
            try {
                ThreadReference thread = getThreadReference();
                if (thread == null) {
                    return null;
                }
                if (index < 0 || index >= thread.frameCount()) {
                    return null;
                }
                StackFrame frame = thread.frame(index);
                CallStackFrame sf = frames.get(frame);
                if (sf == null) {
                    sf = new CallStackFrame(debugger, this, index);
                    frames.put(frame, sf);

                }
                return sf;
            } catch (IncompatibleThreadStateException ex) {

                return null;
            }
        }
    }

    public List<CallStackFrame> getStackFrames() {
        ThreadReference thread = getThreadReference();
        if (thread == null) {
            return Collections.emptyList();
        } else {
            try {
                List<CallStackFrame> result = new LinkedList<CallStackFrame>();
                for (int i = 0, c = thread.frameCount(); i < c; i++) {
                    result.add(getCallStackFrame(i));
                }
                return result;
            } catch (Throwable e) {
                return Collections.emptyList();
            }
        }
    }

    public String getDisplayName() {

        try {
            ThreadReference thread = getThreadReference();
            if (thread == null) {
                return "<Collected Thread>";
            } else {
                return thread.name();
            }
        } catch (ObjectCollectedException e) {
            return "<Collected Thread>";
        } catch (VMDisconnectedException e) {
            return "vm disconnected";
        }
    }

    public String getStateName() {
        try {
            ThreadReference groupRef = getThreadReference();
            if (groupRef != null) {
                int status = groupRef.status();
                switch (status) {
                    case ThreadReference.THREAD_STATUS_ZOMBIE:
                        return "Zombie";
                    case ThreadReference.THREAD_STATUS_RUNNING:
                        return "Running";
                    case ThreadReference.THREAD_STATUS_SLEEPING:
                        return "Sleeping";
                    case ThreadReference.THREAD_STATUS_MONITOR:
                    case ThreadReference.THREAD_STATUS_WAIT:
                        return "Waiting";
                    case ThreadReference.THREAD_STATUS_NOT_STARTED:
                        return "Not Started";
                    default:
                        return "Unknown";
                }
            } else {
                return "<Collected Thread Group>";
            }
        } catch (VMDisconnectedException e) {
            return "vm disconnected";
        }
    }

    public boolean isRunning() {
        try {
            ThreadReference groupRef = getThreadReference();
            if (groupRef != null) {
                return groupRef.status() == ThreadReference.THREAD_STATUS_RUNNING;
            } else {
                return false;
            }
        } catch (VMDisconnectedException e) {
            return false;
        } catch (ObjectCollectedException e) {
            return false;
        }
    }

    public boolean isSuspended() {
        try {
            ThreadReference threadRef = getThreadReference();
            if (threadRef != null) {
                return threadRef.isSuspended();
            } else {
                return false;
            }
        } catch (VMDisconnectedException e) {
            return false;
        } catch (ObjectCollectedException e) {
            return false;
        }
    }

    public boolean isMethodInvoking() {
        return false;
    }

    private void cleanUp() {
        synchronized (this) {
            currentFrame = null;
            synchronized (frames) {
                frames.clear();
            }
        }
    }

    public void suspended() {
        cleanUp();
        PropertyChangeEvent e = new PropertyChangeEvent(this, PROPERTY_NAME_SUSPENDED, false, true);
        fireEvent(e);
    }

    public void resumed() {
        cleanUp();
        PropertyChangeEvent e = new PropertyChangeEvent(this, PROPERTY_NAME_SUSPENDED, true, false);
        fireEvent(e);
    }

    void fireEvent(final PropertyChangeEvent e) {
        Lock lock = accessLock.writeLock();
        try {
            lock.lock();
            propChangeSupport.firePropertyChange(e);
        } finally {
            lock.unlock();
        }
    }

    public boolean cleanBeforeResume() {
        return true;
    }

    public synchronized void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propChangeSupport.removePropertyChangeListener(propertyName, listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        propChangeSupport.removePropertyChangeListener(listener);
    }

    public synchronized void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        propChangeSupport.addPropertyChangeListener(listener);
    }
}
