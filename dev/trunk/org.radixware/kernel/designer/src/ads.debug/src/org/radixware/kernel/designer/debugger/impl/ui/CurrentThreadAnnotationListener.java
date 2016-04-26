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

import java.beans.Customizer;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.netbeans.api.debugger.DebuggerEngine;
import org.netbeans.api.debugger.DebuggerManager;
import org.netbeans.api.debugger.DebuggerManagerAdapter;
import org.netbeans.api.debugger.Session;



import org.openide.util.Exceptions;
import org.openide.util.RequestProcessor;
import org.radixware.kernel.designer.debugger.RadixDebugger;
import org.radixware.kernel.designer.debugger.impl.CallStackFrame;
import org.radixware.kernel.designer.debugger.impl.ThreadWrapper;
import org.radixware.kernel.designer.debugger.impl.ThreadsCache;

public class CurrentThreadAnnotationListener extends DebuggerManagerAdapter {

    private static final int ANNOTATION_SCHEDULE_TIME = 100;
    // annotation for current line
    private transient Object currentPC;
    private final transient Object currentPCLock = new Object();
    private transient boolean currentPCSet = false;
    private ThreadWrapper currentThread;
    private RadixDebugger currentDebugger;
    private AllThreadsAnnotator allThreadsAnnotator;

    @Override
    public String[] getProperties() {
        return new String[]{DebuggerManager.PROP_CURRENT_ENGINE};
    }

    /**
     * Listens RadixDebuggerEngineImpl and DebuggerManager.
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        String propertyName = e.getPropertyName();
        if (DebuggerManager.PROP_CURRENT_ENGINE.equals(propertyName)) {
            updateCurrentDebugger((DebuggerEngine) e.getNewValue());
            updateCurrentThread();
            annotate();
        } else if (ThreadsCache.PROPERTY_NAME_CURRENT_THREAD.equals(propertyName)) {
            updateCurrentThread((ThreadWrapper) e.getNewValue());
            annotate();
        } else if (ThreadWrapper.PROPERTY_NAME_CURRENT_FRAME.equals(propertyName)) {

            showCurrentFrame((CallStackFrame) e.getNewValue());
        } else if (ThreadsCache.THREADS_CACHE_PROPERTY_NAME_THREAD_STARTED.equals(propertyName)) {
            synchronized (this) {
                if (allThreadsAnnotator != null) {
                    allThreadsAnnotator.add((ThreadWrapper) e.getNewValue());
                }
            }
        }
    }

    // helper methods ..........................................................
    private synchronized void updateCurrentDebugger(DebuggerEngine currentEngine) {
        RadixDebugger newDebugger;
        if (currentEngine == null) {
            newDebugger = null;
        } else {
            newDebugger = currentEngine.lookupFirst(null, RadixDebugger.class);
        }
        if (currentDebugger == newDebugger) {
            return;
        }

        if (currentDebugger != null) {
            currentDebugger.getThreadsCache().removePropertyChangeListener(this);
        }
        if (allThreadsAnnotator != null) {
            allThreadsAnnotator.cancel();
            allThreadsAnnotator = null;
        }

        if (newDebugger != null) {
            currentDebugger.getThreadsCache().addPropertyChangeListener(this);
            allThreadsAnnotator = new AllThreadsAnnotator(newDebugger);
        }
        currentDebugger = newDebugger;
        if (currentThread != null && allThreadsAnnotator != null) {
            allThreadsAnnotator.remove(currentThread);
            currentThread = null;
        }
        updateCurrentThread();
    }

    private synchronized void updateCurrentThread() {
        updateCurrentThread(currentDebugger != null ? currentDebugger.getCurrentThread() : null);
    }

    private synchronized void updateCurrentThread(ThreadWrapper newCurrentThread) {
        AllThreadsAnnotator allThreadsAnnotator;
        ThreadWrapper oldCurrent = null;
        ThreadWrapper newCurrent = null;
        synchronized (this) {
            oldCurrent = currentThread;
            // get current thread
            if (currentDebugger != null) {
                currentThread = newCurrentThread;
                newCurrent = currentThread;
            } else {
                currentThread = null;
            }
            allThreadsAnnotator = this.allThreadsAnnotator;
            if (allThreadsAnnotator != null) {
                if (oldCurrent != null) {
                    allThreadsAnnotator.annotate(oldCurrent, false);
                }
                if (newCurrent != null) {
                    allThreadsAnnotator.annotate(newCurrent, true);
                }
            }
        }
    }

    /**
     * Annotates current thread or removes annotations.
     */
    private void annotate() {
        // 1) no current thread => remove annotations
        final RadixDebugger debugger;
        final ThreadWrapper thread;
        synchronized (this) {
            debugger = currentDebugger;
            if ((currentThread == null) || (!debugger.isSuspended())) {
                synchronized (currentPCLock) {
                    currentPCSet = false; // The annotation is goint to be removed
                }
                removeAnnotations();
                return;
            }
            thread = currentThread;
        }
        Session s;
        try {
            s = (Session) debugger.getClass().getMethod("getSession").invoke(debugger);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
            s = null;
        }
        RequestProcessor rProcessor = null;
        if (s != null) {
            rProcessor = s.lookupFirst(null, RequestProcessor.class);
        }
        if (rProcessor == null) {
            rProcessor = this.rp;
        }
        rProcessor.post(new Runnable() {
            @Override
            public void run() {
                annotate(debugger, thread);
            }
        });
    }

    private void annotate(RadixDebugger debugger, final ThreadWrapper currentThread) {
        // 1) no current thread => remove annotations
        List<CallStackFrame> stack;
        final CallStackFrame csf;
        final String language;

        // 2) get call stack & Line

        stack = currentThread.getStackFrames();
        if (stack.isEmpty()) {
            synchronized (currentPCLock) {
                currentPCSet = false; // The annotation is goint to be removed
            }
            removeAnnotations();
            return;
        }
        csf = debugger.getCurrentThread().getCurrentFrame();
        Session s;
        try {
            s = (Session) debugger.getClass().getMethod("getSession").invoke(debugger);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
            s = null;
        }
        language = (s != null) ? s.getCurrentLanguage() : null;


        synchronized (currentPCLock) {
            currentPCSet = true;
        }
    }

    private void showCurrentFrame(final CallStackFrame frame) {
        if (frame == null) {
            return;
        }

        RadixDebugger dbg;

    }
    // do not need synchronization, called in a 1-way RP
    private HashMap stackAnnotations = new HashMap();
    private final RequestProcessor rp = new RequestProcessor("Debugger Thread Annotation Refresher");
    // currently waiting / running refresh task
    // there is at most one
    private RequestProcessor.Task taskRemove;
    private RequestProcessor.Task taskAnnotate;
    private CallStackFrame[] stackToAnnotate;

    private void removeAnnotations() {
        synchronized (rp) {
            if (taskRemove == null) {
                taskRemove = rp.create(new RemoveAnnotationsTask());
            }
        }
        taskRemove.schedule(ANNOTATION_SCHEDULE_TIME);
    }

    private void annotateCallStack(
            CallStackFrame[] stack) {
        synchronized (rp) {
            if (taskRemove != null) {
                taskRemove.cancel();
            }
            this.stackToAnnotate = stack;

            if (taskAnnotate == null) {
                taskAnnotate = rp.create(new AnnotateCallStackTask());
            }
        }
        taskAnnotate.schedule(ANNOTATION_SCHEDULE_TIME);
    }

    private class RemoveAnnotationsTask implements Runnable {

        public void run() {

            stackAnnotations.clear();
        }
    }

    private class AnnotateCallStackTask implements Runnable {

        public void run() {
            CallStackFrame[] stack;

            synchronized (rp) {
                if (stackToAnnotate == null) {
                    return; // Nothing to do
                }
                stack = stackToAnnotate;
                stackToAnnotate = null;

            }
            HashMap newAnnotations = new HashMap();
            int i, k = stack.length;

            Iterator iter = stackAnnotations.values().iterator();

            stackAnnotations = newAnnotations;
        }
    }

    private class AllThreadsAnnotator implements Runnable, PropertyChangeListener {

        private boolean active = true;
        private final RadixDebugger debugger;
        private final Map<ThreadWrapper, Object> threadAnnotations = new HashMap<ThreadWrapper, Object>();
        private final Set<ThreadWrapper> threadsToAnnotate = new HashSet<ThreadWrapper>();
        private final Map<ThreadWrapper, FutureAnnotation> futureAnnotations = new HashMap<ThreadWrapper, FutureAnnotation>();
        private final Set<Object> annotationsToRemove = new HashSet<Object>();
        private final RequestProcessor.Task task;

        public AllThreadsAnnotator(RadixDebugger debugger) {
            this.debugger = debugger;
            RequestProcessor rp;
            try {
                rp = ((Session) debugger.getClass().getMethod("getSession").invoke(debugger)).lookupFirst(null, RequestProcessor.class);
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
                rp = null;
            }
            if (rp != null) {
                task = rp.create(this);
            } else {
                task = CurrentThreadAnnotationListener.this.rp.create(this);
            }


            for (ThreadWrapper t : debugger.getThreadsCache().getAllThreads()) {
                add(t);
            }
        }

        public void add(ThreadWrapper t) {
            ((Customizer) t).addPropertyChangeListener(this);

            annotate(t);
        }

        public void remove(ThreadWrapper t) {
            ((Customizer) t).removePropertyChangeListener(this);

            synchronized (this) {
                Object annotation = threadAnnotations.remove(t);
                if (annotation != null) {
                    threadsToAnnotate.remove(t);
                    annotationsToRemove.add(annotation);
                    task.schedule(ANNOTATION_SCHEDULE_TIME);
                }
            }
        }

        public synchronized void cancel() {
            active = false;
            //System.err.println("AllThreadsAnnotator("+Integer.toHexString(debugger.hashCode())+").CANCEL");
            for (ThreadWrapper t : new HashSet<ThreadWrapper>(threadAnnotations.keySet())) {
                remove(t);
            }
        }

        public void propertyChange(PropertyChangeEvent evt) {
            synchronized (this) {
                if (!active) {
                    ((Customizer) evt.getSource()).removePropertyChangeListener(this);
                    return;
                }
            }
            ThreadWrapper t = (ThreadWrapper) evt.getSource();
            annotate(t);
        }

        private void annotate(ThreadWrapper t) {
            annotate(t, t == debugger.getCurrentThread());
        }

        private void annotate(ThreadWrapper t, boolean isCurrentThread) {
            //System.err.println("annotate("+t+", "+isCurrentThread+")");
            synchronized (this) {
                Object annotation = threadAnnotations.remove(t);
                //System.err.println("SCHEDULE removal of "+annotation+" for "+t);
                if (annotation != null) {
                    threadsToAnnotate.remove(t);
                    annotationsToRemove.add(annotation);
                    task.schedule(ANNOTATION_SCHEDULE_TIME);
                }
                if (!isCurrentThread) {
                    threadsToAnnotate.add(t);
                    FutureAnnotation future = futureAnnotations.get(t);
                    if (future == null) {
                        future = new FutureAnnotation(t);
                    }
                    threadAnnotations.put(t, future);
                    futureAnnotations.put(t, future);
                    task.schedule(ANNOTATION_SCHEDULE_TIME);
                    //System.err.println("SCHEDULE annotation of "+t+", have future = "+future);
                }
            }
        }

        @Override
        public void run() {
            Set<Object> annotationsToRemove;
            Set<ThreadWrapper> threadsToAnnotate;
            Map<ThreadWrapper, FutureAnnotation> futureAnnotations;

            synchronized (this) {
                //System.err.println("TASK threadsToAnnotate: "+this.threadsToAnnotate);
                annotationsToRemove = new HashSet<Object>(this.annotationsToRemove);
                this.annotationsToRemove.clear();
                threadsToAnnotate = new HashSet<ThreadWrapper>(this.threadsToAnnotate);
                this.threadsToAnnotate.clear();
                futureAnnotations = new HashMap<ThreadWrapper, FutureAnnotation>(this.futureAnnotations);
                this.futureAnnotations.clear();
            }
            for (Object annotation : annotationsToRemove) {
                if (annotation instanceof FutureAnnotation) {
                    annotation = ((FutureAnnotation) annotation).getAnnotation();
                    if (annotation == null) {
                        continue;
                    }
                }
            }
            Map<ThreadWrapper, Object> threadAnnotations = new HashMap<ThreadWrapper, Object>();
            Set<ThreadWrapper> removeFutures = new HashSet<ThreadWrapper>();
            Session s;
            try {
                s = (Session) debugger.getClass().getMethod("getSession").invoke(debugger);
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
                s = null;
            }
            String language = (s != null) ? s.getCurrentLanguage() : null;
            for (ThreadWrapper t : threadsToAnnotate) {
                Object annotation;

            }
            synchronized (this) {
                this.threadAnnotations.keySet().removeAll(removeFutures);
                this.threadAnnotations.putAll(threadAnnotations);

            }
        }

        private final class FutureAnnotation {

            private ThreadWrapper thread;
            private Object annotation;

            public FutureAnnotation(ThreadWrapper thread) {
                this.thread = thread;
            }

            public ThreadWrapper getThread() {
                return thread;
            }

            public void setAnnotation(Object annotation) {
                this.annotation = annotation;
            }

            public Object getAnnotation() {
                return annotation;
            }

            @Override
            public String toString() {
                return "Future annotation (" + annotation + ") for " + thread;
            }
        }
    }
}
