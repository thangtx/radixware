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

import com.sun.jdi.ClassNotLoadedException;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.InternalException;
import com.sun.jdi.InvalidTypeException;
import com.sun.jdi.InvocationException;
import com.sun.jdi.Method;
import com.sun.jdi.ObjectCollectedException;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VMDisconnectedException;
import com.sun.jdi.Value;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.ClassPrepareEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.event.ThreadDeathEvent;
import com.sun.jdi.event.ThreadStartEvent;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.event.VMStartEvent;
import com.sun.jdi.request.EventRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.debugger.DebuggerManager;
import org.openide.util.RequestProcessor;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.debugger.RadixDebugger;


public class JVMEventHandler {

    private static final class HandlerTask {

        private RequestProcessor.Task task;
        private final Thread[] threadPtr;

        HandlerTask(RequestProcessor.Task task, Thread[] threadPtr) {
            this.task = task;
            this.threadPtr = threadPtr;
        }

        void cancel() {
            if (!task.cancel()) {
                synchronized (threadPtr) {
                    if (threadPtr[0] == null) {
                        try {
                            threadPtr.wait();
                        } catch (InterruptedException ex) {
                        }
                    }
                }
                Thread t = threadPtr[0];
                if (t != null) {
                    t.interrupt();
                    task.waitFinished();
                }
            }
        }
    }
    private final Thread thread;
    private boolean stopped = false;
    private boolean canInterrupt;
    private Map<ThreadReference, HandlerTask> eventHandlers = new HashMap<ThreadReference, HandlerTask>();
    private final List<EventSet> parallelEvents = new LinkedList<EventSet>();
    private final VirtualMachine jvm;
    private final RadixDebugger debugger;

    public JVMEventHandler(final RadixDebugger debugger, final VirtualMachine jvm, final EventHandler startHandler, final Runnable finalizer) {
        this.jvm = jvm;
        this.debugger = debugger;
        this.thread = new Thread(new Runnable() {
            @Override
            public void run() {
                loop:
                for (;;) {
                    try {
                        EventSet eventSet = null;
                        try {
                            eventSet = jvm.eventQueue().remove();
                        } catch (InterruptedException ex) {
                            continue;
                        }
                        if (eventSet == null) {
                            synchronized (JVMEventHandler.this) {
                                if (stopped) {
                                    break;
                                }
                            }
                            continue;
                        }
                        boolean resume = true;
                        ThreadReference thread = null;
                        boolean isThreadEvent = false;
                        for (Event e : eventSet) {
                            if (e instanceof ThreadStartEvent) {
                                thread = findThreadForEvent(e);
                                if (thread != null) {
                                    debugger.getThreadsCache().threadStarted(thread);
                                    isThreadEvent = true;
                                }

                            } else if (e instanceof ThreadDeathEvent) {
                                thread = findThreadForEvent(e);
                                if (thread != null) {
                                    debugger.getThreadsCache().threadDied(thread);
                                    isThreadEvent = true;
                                }
                            } else if (e instanceof VMStartEvent) {
                                if (startHandler != null) {
                                    if (!startHandler.processEvent(e)) {
                                        resume = false;
                                    }
                                }
                            }
                            if (thread != null) {
                                while (thread.suspendCount() > 1) {
                                    thread.resume();
                                }
                                break;
                            }
                        }
                        Set<ThreadReference> resumedThreads = new HashSet<ThreadReference>();
                        if (shouldIgnoreEvents(eventSet, resumedThreads)) {
                            resume = true;
                        } else {
                            Map<Event, EventHandler> eventsToProcess = new HashMap<Event, EventHandler>();
                            for (Event e : eventSet) {
                                EventRequest r = e.request();
                                if (r != null) {
                                    EventHandler handler = (EventHandler) r.getProperty("RadixWareHandlerForJvmEvent");
                                    if (handler instanceof PreconditionEventHandler) {
                                        if (((PreconditionEventHandler) handler).precondition(e)) {
                                            eventsToProcess.put(e, handler);
                                        }
                                    } else {
                                        eventsToProcess.put(e, handler);
                                    }
                                }
                            }

                            if (eventsToProcess.isEmpty()) {
                                if (!resume) {
                                    continue;
                                }
                            } else {
                                for (Event e : eventSet) {
                                    if ((e instanceof VMDeathEvent) || (e instanceof VMDisconnectEvent)) {
                                        synchronized (JVMEventHandler.this) {
                                            stopped = true;
                                        }
                                        break loop;
                                    }
                                    if ((e instanceof VMStartEvent)) {
                                        if (startHandler != null) {
                                            if (!startHandler.processEvent(e)) {
                                                resume = false;
                                            }
                                        }
                                        continue;
                                    }
                                    EventHandler handler = null;

                                    if (e.request() != null) {
                                        handler = eventsToProcess.get(e);
                                    }
                                    if (handler != null) {
                                        try {
                                            if (!handler.processEvent(e)) {
                                                resume = false;
                                            }
                                        } catch (VMDisconnectedException exc) {
                                            synchronized (JVMEventHandler.this) {
                                                stopped = true;
                                            }
                                            break loop;
                                        } catch (Exception ex) {
                                            DialogUtils.messageError(ex);
                                        }
                                    }
                                }
                            }
                        }
                        int suspendPolicy = getSuspendPolicy(eventSet);
                        if (resume) {
                            if (suspendPolicy == EventRequest.SUSPEND_ALL) {
                                debugger.resumeAll();
                            }
                            if (thread != null) {
                                ThreadWrapper wrapper = debugger.getThread(thread);
                                if (wrapper != null) {
                                    wrapper.resumed();
                                }
                            }
                            eventSet.resume();
                        } else {
                            DebuggerManager.getDebuggerManager().setCurrentSession(debugger.getSession());
                            debugger.setStopped(thread, suspendPolicy == EventRequest.SUSPEND_ALL);
                        }
                    } catch (VMDisconnectedException e) {
                        synchronized (JVMEventHandler.this) {
                            stopped = true;
                        }
                        break loop;
                    } catch (ObjectCollectedException e) {
                        continue;
                    } catch (Exception e) {
                        DialogUtils.messageError(e);
                    }
                }

                if (finalizer != null) {
                    finalizer.run();
                }


            }
        });
    }

    public void start() {
        thread.start();
    }

    private static int getSuspendPolicy(EventSet eventSet) {
        try {
            return eventSet.suspendPolicy();
        } catch (InternalException e) {
            throw wrapVMException("JVM internal error", e);
        } catch (ObjectCollectedException e) {
            throw objectCollected("VM Event Set", e);
        } catch (VMDisconnectedException e) {
            throw vmDisconnected(e);
        } catch (RuntimeException e) {
            throw wrapVMException("JVM runtime error", e);
        }
    }

    private static ThreadReference findThreadForEvent(Event e) {
        if (e instanceof LocatableEvent) {
            return ((LocatableEvent) e).thread();
        } else if (e instanceof ClassPrepareEvent) {
            return ((ClassPrepareEvent) e).thread();
        } else if (e instanceof ThreadStartEvent) {
            return ((ThreadStartEvent) e).thread();
        } else if (e instanceof ThreadDeathEvent) {
            return ((ThreadDeathEvent) e).thread();
        }
        return null;
    }

    private static RuntimeException wrapVMException(String message, Throwable e) {
        RadixError ex = new RadixError(message, e);
        DialogUtils.messageError(ex);
        return ex;
    }

    private static RuntimeException vmDisconnected(Throwable e) {
        return wrapVMException("Disconnected from target VM", e);
    }

    private static RuntimeException objectCollected(String name, Throwable e) {
        return wrapVMException("Object already collected by GC: " + name, e);
    }
    private final List<ThreadReference> threadsForMethodInvocation = new LinkedList<ThreadReference>();

    private boolean shouldIgnoreEvents(EventSet eventSet, Set<ThreadReference> resumedThreads) {
        ThreadReference tref = null;
        for (Event e : eventSet) {
            if (e instanceof LocatableEvent) {
                tref = findThreadForEvent(e);
            }
        }
        if (tref != null) {
            synchronized (threadsForMethodInvocation) {
                if (threadsForMethodInvocation.contains(tref)) {
                    return true;
                }
            }
        }
        int suspendPolicy = getSuspendPolicy(eventSet);
        if (suspendPolicy == EventRequest.SUSPEND_ALL) {
            synchronized (threadsForMethodInvocation) {
                for (ThreadReference tr : threadsForMethodInvocation) {
                    try {
                        resumeThread(tr);
                        resumedThreads.add(tr);
                    } catch (ObjectCollectedException | IllegalStateException ex) {
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                    }
                }
            }
        }
        return false;
    }

    private static void resumeThread(ThreadReference tref) {
        try {
            tref.resume();
        } catch (com.sun.jdi.InternalException ex) {
            throw wrapVMException("JVM internal exception on resuming thread " + tref.name(), ex);
        } catch (com.sun.jdi.VMDisconnectedException ex) {
            throw vmDisconnected(ex);
        } catch (com.sun.jdi.ObjectCollectedException ex) {
            throw objectCollected(tref.name(), ex);
        } catch (java.lang.IllegalThreadStateException ex) {
            throw new IllegalStateException();
        }
    }

    public interface EventHandler {

        public boolean processEvent(Event event);
    }

    public interface PreconditionEventHandler extends EventHandler {

        public boolean precondition(Event event);
    }

    public void stop() {
        synchronized (this) {
            if (stopped) {
                return; // Do not interrupt the thread when we're stopped
            }
            stopped = true;
            if (canInterrupt) {
                thread.interrupt();
            }
        }
    }

    public void registerEventHandler(EventRequest rq, EventHandler eh) {
        rq.putProperty("RadixWareHandlerForJvmEvent", eh);
    }

    private class MethodInvokeInfo {

        private final ObjectReference thisRef;
        private final Method method;
        private final Value[] arguments;
        private Value result = null;
        private Throwable exception = null;

        public MethodInvokeInfo(ObjectReference thisRef, Method method, Value[] arguments) {
            this.thisRef = thisRef;
            this.method = method;
            this.arguments = arguments;
        }

        private void invokeMethod(ThreadReference thread) {
            try {

                ObjectReference ref = thisRef;

                try {
                    synchronized (threadsForMethodInvocation) {
                        threadsForMethodInvocation.add(thread);
                    }
                    result = ref.invokeMethod(thread, method, Arrays.asList(arguments), ObjectReference.INVOKE_SINGLE_THREADED);
                } finally {
                    synchronized (threadsForMethodInvocation) {
                        threadsForMethodInvocation.remove(thread);
                    }
                }

            } catch (InvalidTypeException ex) {
                exception = ex;
            } catch (ClassNotLoadedException ex) {
                exception = ex;
            } catch (IncompatibleThreadStateException ex) {
                exception = ex;
            } catch (InvocationException ex) {
                exception = ex;
            } catch (InternalException ex) {
                exception = ex;
            } catch (VMDisconnectedException ex) {
                exception = ex;
            } catch (ObjectCollectedException ex) {
                exception = ex;
            }
        }
    }

    public Object invokeMethod(ObjectReference thisRef, Method method, Value[] arguments) {
        ThreadReference tr = debugger.getCurrentThread().getThreadReference();
        MethodInvokeInfo info = new MethodInvokeInfo(thisRef, method, arguments);
        info.invokeMethod(tr);
        if (info.exception != null) {
            return ObjectReferenceWrapper.NOT_AVAILABLE_VALUE;
        } else {
            return info.result;
        }
    }
//    public void notifyMethodInvoking(ThreadReference tr) {
//        if (Thread.currentThread() == thread) {
//            startEventHandlerThreadFor(tr);
//        }
//        synchronized (threadsForMethodInvocation) {
//            threadsForMethodInvocation.add(tr);
//        }
//    }
//
//    public void notifyMethodInvokeDone(ThreadReference tr) {
//        if (Thread.currentThread() == thread) {
//            HandlerTask task = eventHandlers.remove(tr);
//            if (task != null) {
//                task.cancel();
//            }
//        }
//        synchronized (threadsForMethodInvocation) {
//            threadsForMethodInvocation.remove(tr);
//        }
//    }
//    private RequestProcessor eventHandler = null;
//
//    private void startEventHandlerThreadFor(final ThreadReference tr) {
//        RequestProcessor rp;
//        synchronized (this) {
//            if (eventHandler == null) {
//                eventHandler = new RequestProcessor("Debugger Event Handler", 10);  // NOI18N
//            }
//            rp = eventHandler;
//        }
//        final Thread[] threadPtr = new Thread[]{null};
//        RequestProcessor.Task task = rp.post(new Runnable() {
//
//            @Override
//            public void run() {
//                synchronized (threadPtr) {
//                    threadPtr[0] = Thread.currentThread();
//                    threadPtr.notifyAll();
//                }
//                EventQueue eventQueue;
//                try {
//                    eventQueue = tr.virtualMachine().eventQueue();
//                } catch (InternalException ex) {
//                    return;
//                } catch (VMDisconnectedException ex) {
//                    return;
//                }
//                for (;;) {
//                    EventSet eventSet;
//                    try {
//                        eventSet = eventQueue.remove();
//
//                        Set<ThreadReference> ignoredThreads = new HashSet<ThreadReference>();
//                        if (shouldIgnoreEvents(eventSet, ignoredThreads)) {
//                            eventSet.resume();
//                        } else {
//                            synchronized (parallelEvents) {
//                                parallelEvents.add(eventSet);
//                            }
//                        }
//
//                    } catch (InterruptedException ex) {
//                        return;
//                    } catch (InternalException ex) {
//                        //Exceptions.printStackTrace(ex);
//                        // Ignore
//                        continue;
//                    } catch (VMDisconnectedException ex) {
//                        return;
//                    }
//
//                }
//            }
//        }, 500);
//        eventHandlers.put(tr, new HandlerTask(task, threadPtr));
//    }
}
