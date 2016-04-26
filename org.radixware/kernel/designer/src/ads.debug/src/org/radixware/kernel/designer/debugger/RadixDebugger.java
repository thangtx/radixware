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

import com.sun.jdi.*;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.request.StepRequest;
import java.io.File;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.prefs.Preferences;
import org.netbeans.api.debugger.*;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.spi.debugger.ContextProvider;
import org.netbeans.spi.debugger.DebuggerEngineProvider;
import org.openide.text.Line;
import org.openide.util.Cancellable;
import org.openide.util.RequestProcessor;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.debugger.breakpoints.RadixBreakpoint;
import org.radixware.kernel.designer.debugger.impl.*;

public class RadixDebugger {

    static String getDefaultDebugee() {
        return Preferences.userNodeForPackage(RadixDebugger.class).get("DEBUGGEE", "Server");
    }

    static void setDefaultDebugee(String d) {
        if ("Server".equals(d) || "Explorer".equals(d)) {
            Preferences.userNodeForPackage(RadixDebugger.class).put("DEBUGGEE", d);
        }
    }

    private static boolean isDebugServer() {
        return getDefaultDebugee().equals("Server");
    }
    public static final String SESSION_ID = "RadixWare-Debug-Session";
    public static final String ENGINE_ID = "RadixWare-Debug-Session/Jml";
    private RadixDebuggerEngineProvider radixDebuggerEngine;
    private Session session;
    private ThreadsCache threadsCache;
    private final ReadWriteLock accessLock = new ReentrantReadWriteLock();
    private final RequestProcessor requestProcessor = new RequestProcessor("RadixDebuggerRequestProcessor");
    private final List<BreakpointHandler> breakpoints = new LinkedList<BreakpointHandler>();
    private final NameResolver nameResolver;
    private final TypesCache typesCache = new TypesCache(this);
    private final DebuggerManagerListener debuggerManagerListener = new DebuggerManagerAdapter() {
        @Override
        public void breakpointAdded(Breakpoint breakpoint) {
            if (breakpoint instanceof RadixBreakpoint) {
                registerBreakpointHandler(new BreakpointHandler(RadixDebugger.this, (RadixBreakpoint) breakpoint));
            }
        }

        @Override
        public void breakpointRemoved(Breakpoint breakpoint) {
            if (breakpoint instanceof RadixBreakpoint) {
                unregisterBreakPoint((RadixBreakpoint) breakpoint);
            }
        }
    };

    private void registerBreakpointHandler(BreakpointHandler bp) {
        synchronized (breakpoints) {
            breakpoints.add(bp);
            attachBreakPoint(bp);
        }
    }

    private void attachBreakPoint(BreakpointHandler bp) {
        if (handler != null && jvm != null) {
            bp.initialize(jvm, handler);
        }
    }

    private void unregisterBreakPoint(RadixBreakpoint bp) {
        synchronized (breakpoints) {
            BreakpointHandler bph = findBreakpointHandler(bp);

            if (bph != null) {
                bph.dispose();
                breakpoints.remove(bph);
            }
        }
    }

    private BreakpointHandler findBreakpointHandler(RadixBreakpoint bp) {
        for (BreakpointHandler h : breakpoints) {
            if (h.getBreakpoint() == bp) {
                return h;
            }
        }
        return null;
    }

    private void initializeBreakpoints() {
        synchronized (breakpoints) {
            Breakpoint[] all = DebuggerManager.getDebuggerManager().getBreakpoints();

            for (Breakpoint bp : all) {
                if (bp instanceof RadixBreakpoint) {
                    BreakpointHandler bph = findBreakpointHandler((RadixBreakpoint) bp);
                    if (bph == null) {
                        registerBreakpointHandler(new BreakpointHandler(this, (RadixBreakpoint) bp));
                    }
                }
            }

            for (BreakpointHandler h : breakpoints) {
                h.initialize(jvm, handler);
            }
        }
    }
    private final Lock stepOperationLock = new ReentrantLock();
    private boolean isInStep = false;

    public TypesCache getTypesCache() {
        return typesCache;
    }

    public void step(int step) {
        if (stepOperationLock.tryLock()) {
            try {
                if (isInStep) {
                    return;
                }

                ThreadWrapper thread = null;
                synchronized (currentThreadLock) {
                    if (getCurrentThread() != null) {
                        thread = getCurrentThread();
                    }
                }
                if (thread != null) {
                    final List<StepRequest> stepRequests;
                    if (jvm != null && jvm.eventRequestManager() != null && jvm.eventRequestManager().stepRequests() != null) {
                        stepRequests = new ArrayList<>(jvm.eventRequestManager().stepRequests());
                    } else {
                        stepRequests = Collections.emptyList();
                    }
                    ThreadReference threadRef = thread.getThreadReference();

                    for (StepRequest rq : stepRequests) {
                        if (rq.thread().equals(threadRef)) {
                            try {
                                rq.virtualMachine().eventRequestManager().deleteEventRequest(rq);
                            } catch (RuntimeException e) {
                                //ignore
                            }
                        }
                    }

                    if (jvm != null && jvm.eventRequestManager() != null) {
                        final StepRequest request = jvm.eventRequestManager().createStepRequest(threadRef, StepRequest.STEP_LINE, step);
                        request.addCountFilter(1);

                        JVMEventHandler.EventHandler h = new JVMEventHandler.EventHandler() {
                            @Override
                            public boolean processEvent(Event event) {
                                try {
                                    stepOperationLock.lock();
                                    StepEvent e = (StepEvent) event;
                                    e.request().disable();
                                    jvm.eventRequestManager().deleteEventRequest(e.request());
                                    isInStep = false;
                                    return false;
                                } finally {
                                    stepOperationLock.unlock();
                                }
                            }
                        };
                        handler.registerEventHandler(request, h);
                        request.enable();
                        isInStep = true;
                        resume();
                    }

                }
            } finally {
                stepOperationLock.unlock();
            }
        }
    }

    public RadixDebugger(ContextProvider contextProvider) {
        for (DebuggerEngineProvider p : contextProvider.lookup(null, DebuggerEngineProvider.class)) {
            if (p instanceof RadixDebuggerEngineProvider) {
                radixDebuggerEngine = (RadixDebuggerEngineProvider) p;
            }
        }
        session = contextProvider.lookupFirst(null, Session.class);
        if (radixDebuggerEngine == null) {
            throw new RadixError("No compatible debugger engine found");
        }
        threadsCache = new ThreadsCache(this);
        nameResolver = new NameResolver();

        DebuggerManager.getDebuggerManager().addDebuggerListener(debuggerManagerListener);
    }

    public NameResolver getNameResolver() {
        return nameResolver;
    }

    public RequestProcessor getRequestProcessor() {
        return requestProcessor;
    }

    public Session getSession() {
        return session;
    }

    public VirtualMachine getVirtualMachine() {
        return jvm;
    }

    public ThreadsCache getThreadsCache() {
        return threadsCache;
    }

    public JVMEventHandler getEventHandler() {
        return handler;
    }

    public ReadWriteLock getAccessLock() {
        return accessLock;
    }

    public void setStopped(ThreadReference currentThread, boolean allSuspended) {
        try {
            if (allSuspended) {
                List<ThreadReference> threads = jvm.allThreads();
                for (ThreadReference thread : threads) {
                    while (thread.suspendCount() > 1) {
                        thread.resume();
                    }
                }
                suspendAll(null);
            } else {
                List<ThreadReference> threads = jvm.allThreads();
                for (ThreadReference thread : threads) {
                    ThreadWrapper wrapper = threadsCache.getThreadWrapper(thread);
                    if (wrapper.isSuspended()) {
                        while (thread.suspendCount() > 1) {
                            thread.resume();
                        }
                        wrapper.suspended();
                    }
                }
            }
        } finally {
            ThreadWrapper current = getCurrentThread();
            isSuspended = true;
            if (current != null) {
                current.updateCurrentFrame();
                CallStackFrame frame = current.getCurrentFrame();
                if (frame != null) {
                    Line line = frame.getLine();
                    //can not open desired location
                    if (line == null) {
                        step(StepRequest.STEP_OUT);
                    }
                }
            }

        }
    }

    public static void debugServer(Branch branch) {
        debug(branch, true);
    }

    public static void debugExplorer(Branch branch) {
        debug(branch, false);
    }

    public static boolean attach(Branch branch) {
        RadixDebuggerConnector connector = new RadixDebuggerAttachConnector(branch);
        return attach(connector);
    }

    public static boolean attach(RadixDebuggerConnector connector) {
        DebuggerEngine[] engines = DebuggerManager.getDebuggerManager().startDebugging(DebuggerInfo.create(RadixDebuggerAttachConnector.ID,
                new Object[]{connector}));
        if (engines == null || engines.length == 0) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean launch(RadixDebuggerConnector connector) {
        DebuggerEngine[] engines = DebuggerManager.getDebuggerManager().startDebugging(DebuggerInfo.create(RadixDebuggerLaunchingConnector.ID,
                new Object[]{connector}));
        if (engines == null || engines.length == 0) {
            return false;
        } else {
            return true;
        }
    }

    private static void debug(Branch branch, boolean server) {
        File branchDir = branch.getDirectory();
        File staterJar = new File(branchDir, "org.radixware/kernel/starter/bin/dist/starter.jar");
        if (!staterJar.isFile()) {
            DialogUtils.messageError("Can not find starter binary");
            return;
        }
        StartupInfoProfile currentProfile = StartupInfoManager.getInstance().getCurrentProfile(branch);

        StartupInfo info = server ? currentProfile.getInfo(StartupInfo.EEnvironment.SERVER) : currentProfile.getInfo(StartupInfo.EEnvironment.EXPLORER);
        String jvmArgs = info.getJVMArguments();
        String topLayerUri = info.getTopLayerURI();
        String appArgs = info.getAppArguments();

        if (topLayerUri == null || topLayerUri.isEmpty()) {
            topLayerUri = branch.getLayers().findTop().get(0).getURI();
        }
        String commandLine = info.getStarterArguments() + " -topLayerUri=" + topLayerUri;
        if (!commandLine.contains("-topLayerUri")) {
            commandLine += " -topLayerUri=" + topLayerUri;
        }
        if (server) {
            commandLine += " org.radixware.kernel.server.Server ";
        } else {
            commandLine += " org.radixware.kernel.explorer.Explorer ";
        }
        commandLine += appArgs;

        String classpath = info.getClassPath();
        String workDir = info.getWorkDir();

        RadixDebuggerConnector connector;

        if (classpath == null || classpath.isEmpty()) {
            connector = new RadixDebuggerLaunchingJarConnector(branch, staterJar.getAbsolutePath(), jvmArgs, commandLine, !server, workDir);
        } else {
            classpath = staterJar.getAbsolutePath() + getClassPathDelimiter() + classpath;
            connector = new RadixDebuggerLaunchingClassPathConnector(branch, classpath, jvmArgs, commandLine, !server, workDir);
        }

        launch(connector);
    }

    private static String getClassPathDelimiter() {
        String osName = System.getProperty("os.name");
        boolean windows = osName != null && osName.toLowerCase().indexOf("windows") >= 0;

        if (windows) {
            return ";";
        }
        return ":";
    }

    public static void debug(Branch branch) {
        debug(branch, isDebugServer());
    }

    public ThreadWrapper getThread(ThreadReference tref) {
        return null;
    }

    public void resumeAll() {
    }

    public void suspendAll(Set<ThreadReference> ignoreThreads) {
        threadsCache.suspendAll(ignoreThreads);
    }
    private VirtualMachine jvm;
    private JVMEventHandler handler;
    private boolean isSuspended = false;
    private boolean isExplorerDebug;

    public boolean isSuspended() {
        return isSuspended;
    }

    public String getDescription() {
        return this.getNameResolver().getBranch().getName() + " (" + (isExplorerDebug ? "Explorer" : "Server") + ")";
    }
    private DebuggerSessionConsole console = null;

    public void setVirtualMachine(VirtualMachine vm, JVMEventHandler handler, boolean isExplorerDebug) {
        synchronized (this) {
            if (jvm != null) {
                return;
            }
            this.isExplorerDebug = isExplorerDebug;
            if (indicator != null) {
                indicator.finish();
            }
            final String message = getDescription();
            indicator = ProgressHandleFactory.createHandle("Debugging " + message, new Cancellable() {
                private boolean cancelled = false;

                @Override
                public boolean cancel() {
                    if (cancelled) {
                        return true;
                    } else {
                        if (DialogUtils.messageConfirmation("Are you sure to cancel debugging " + message)) {
                            cancelled = true;
                            finish();
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            });

            console = new DebuggerSessionConsole("Debugging " + message, vm);
            indicator.start();
            this.jvm = vm;
            this.handler = handler;
            threadsCache.updateVirtualMachine();
            initializeBreakpoints();
        }
    }
    private boolean finishing = false;

    public void suspend() {

        synchronized (this) {
            if (isSuspended) {
                return;
            }
            if (jvm != null) {
                try {
                    jvm.suspend();
                    List<ThreadReference> threads = jvm.allThreads();
                    for (ThreadReference t : threads) {
                        try {
                            while (t.suspendCount() > 1) {
                                t.resume();
                            }
                        } catch (IllegalThreadStateException | ObjectCollectedException | InternalException e) {
                        }
                    }
                } catch (VMDisconnectedException | InternalException e) {
                    return;
                } finally {
                    isSuspended = true;
                }
            }
            suspendAll(null);
        }

    }
    private final Object currentThreadLock = new Object();
    private ThreadWrapper currentThread = null;

    public void setCurrentThread(ThreadWrapper thread) {
        synchronized (currentThreadLock) {
            ThreadWrapper oldCurrentThread = currentThread;
            this.currentThread = thread;
            threadsCache.notifyCurrentThreadChange(oldCurrentThread, currentThread);
        }
    }

    public ThreadWrapper getCurrentThread() {
        synchronized (currentThreadLock) {
            return currentThread;
        }
    }

    public void resume() {
        synchronized (this) {
            try {
                stepOperationLock.lock();
                isInStep = false;
            } finally {
                stepOperationLock.unlock();
            }
            if (!isSuspended) {
                return;
            }
            if (jvm != null) {
                List<ThreadWrapper> allThreads = threadsCache.getAllThreads();

                List<ThreadWrapper> threadsToResume = new ArrayList<>();
                for (ThreadWrapper t : allThreads) {
                    if (t.isSuspended()) {
                        threadsToResume.add(t);
                    }
                }
                try {
                    jvm.resume();
                } catch (VMDisconnectedException | InternalException e) {
                } finally {
                    isSuspended = false;
                    for (ThreadWrapper t : threadsToResume) {
                        t.resumed();
                    }
                }
            }
        }
    }
    private ProgressHandle indicator;

    public void finish() {
        synchronized (this) {
            if (finishing) {
                return;
            }
            finishing = true;
        }
        try {

            DebuggerEngine.Destructor destructor = radixDebuggerEngine.getDestructor();
            if (destructor != null) {
                destructor.killEngine();
            }
            if (jvm == null) {
                return;
            }
            handler.stop();
            threadsCache.finish();

            jvm.exit(0);
            jvm.dispose();
        } catch (com.sun.jdi.VMDisconnectedException e) {
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            synchronized (this) {
                jvm = null;
                handler = null;
                finishing = false;
                if (console != null) {
                    console.detach();
                    console = null;
                }
                if (indicator != null) {
                    indicator.finish();
                }
            }
        }
    }
    private Lock invocationLock = new ReentrantLock();

    public Object invokeMethod(ObjectReference thisRef, Method method, Value[] arguments) {

        try {
            invocationLock.lock();
            return handler.invokeMethod(thisRef, method, arguments);
        } finally {
            getCurrentThread().invalidateFrameList();
            invocationLock.unlock();
        }
    }
}
