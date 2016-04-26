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

import com.sun.jdi.InternalException;
import com.sun.jdi.ObjectCollectedException;
import com.sun.jdi.ThreadGroupReference;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VMDisconnectedException;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.ThreadDeathEvent;
import com.sun.jdi.event.ThreadStartEvent;
import com.sun.jdi.request.InvalidRequestStateException;
import com.sun.jdi.request.ThreadDeathRequest;
import com.sun.jdi.request.ThreadStartRequest;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.designer.debugger.RadixDebugger;


public class ThreadsCache implements JVMEventHandler.EventHandler {

    public static final String THREADS_CACHE_PROPERTY_NAME_THREAD_STARTED = "rdx-single-thread-started";
    public static final String THREADS_CACHE_PROPERTY_NAME_THREAD_DIED = "rdx-single-thread-died";
    public static final String THREADS_CACHE_PROPERTY_NAME_THREAD_GROUP_ADDED = "rdx-thread-group-added";
    public static final String PROPERTY_NAME_CURRENT_THREAD = "radixware-debugger-property-current-thread";
    private final RadixDebugger debugger;
    private final Object REFRESH_LOCK = new Object();
    private final Object FIRE_CAHNGE_LOCK = new Object();
    private boolean canFireChanges = false;
    private final Map<ThreadGroupReference, List<ThreadGroupReference>> groupsByOwnerGroup = new HashMap<ThreadGroupReference, List<ThreadGroupReference>>();
    private final Map<ThreadGroupReference, List<ThreadReference>> threadsByGroup = new HashMap<ThreadGroupReference, List<ThreadReference>>();
    private List<ThreadReference> threadsList = new LinkedList<ThreadReference>();
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private final ThreadsCollector threadsCollector;

    public ThreadsCache(RadixDebugger debugger) {
        this.debugger = debugger;
        this.threadsCollector = new ThreadsCollector(this);
    }

    private void initGroups(final ThreadGroupReference group) {
        try {
            final List<ThreadGroupReference> threadGroups = new ArrayList<ThreadGroupReference>(group.threadGroups());
            final List<ThreadReference> groupThreads = new ArrayList<ThreadReference>(group.threads());
            groupsByOwnerGroup.put(group, threadGroups);
            threadsByGroup.put(group, groupThreads);
            for (ThreadGroupReference g : threadGroups) {
                initGroups(g);
            }
        } catch (ObjectCollectedException ex) {
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
        }
    }

    public void finish() {
        synchronized (REFRESH_LOCK) {
            this.threadsList.clear();
            this.groupsByOwnerGroup.clear();
            this.threadsByGroup.clear();
            this.threadWrappers.clear();
            this.groupWrappers.clear();
        }
    }

    private void upload(VirtualMachine vm) {
        threadsList = new ArrayList<ThreadReference>(vm.allThreads());
        List<ThreadGroupReference> topLevelGroups;
        groupsByOwnerGroup.put(null, topLevelGroups = new ArrayList<ThreadGroupReference>(vm.topLevelThreadGroups()));
        for (ThreadGroupReference group : topLevelGroups) {
            initGroups(group);
        }
        final List<ThreadReference> mainThreads = new ArrayList<ThreadReference>();
        threadsByGroup.put(null, mainThreads);
        for (final ThreadReference thread : threadsList) {
            try {
                if (thread.threadGroup() == null) {
                    mainThreads.add(thread);
                }
            } catch (ObjectCollectedException | IllegalThreadStateException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }
    }

    public void updateVirtualMachine() {
        List<ThreadReference> _allThreads;
        List<ThreadGroupReference> _allGroups;
        synchronized (REFRESH_LOCK) {
            VirtualMachine jvm = debugger.getVirtualMachine();
            if (jvm == null) {
                return;
            }
            try {
                ThreadStartRequest tsr = jvm.eventRequestManager().createThreadStartRequest();
                tsr.setSuspendPolicy(ThreadStartRequest.SUSPEND_NONE);
                ThreadDeathRequest tdr = jvm.eventRequestManager().createThreadDeathRequest();
                tdr.setSuspendPolicy(ThreadStartRequest.SUSPEND_NONE);
                debugger.getEventHandler().registerEventHandler(tsr, this);
                debugger.getEventHandler().registerEventHandler(tdr, this);
                tsr.enable();
                tdr.enable();
                upload(jvm);
            } catch (VMDisconnectedException e) {
                jvm = null;
            } catch (InternalException e) {
                jvm = null;
            } catch (ObjectCollectedException e) {
                jvm = null;
            } catch (InvalidRequestStateException e) {
                jvm = null;
            }
            _allThreads = new ArrayList<ThreadReference>(threadsList);
            _allGroups = getAllGroups();

        }
        for (ThreadReference t : _allThreads) {
            propertyChangeSupport.firePropertyChange(THREADS_CACHE_PROPERTY_NAME_THREAD_STARTED, null, getThreadWrapper(t));
        }
        for (ThreadGroupReference g : _allGroups) {
            propertyChangeSupport.firePropertyChange(THREADS_CACHE_PROPERTY_NAME_THREAD_GROUP_ADDED, null, g);
        }
        synchronized (FIRE_CAHNGE_LOCK) {
            canFireChanges = true;
            FIRE_CAHNGE_LOCK.notifyAll();
        }
    }

    private void refresh() {
        List<ThreadReference> newThreads;
        List<ThreadReference> oldThreads;
        List<ThreadGroupReference> newGroups = null;

        synchronized (REFRESH_LOCK) {
            VirtualMachine jvm = debugger.getVirtualMachine();
            if (jvm == null) {
                return;
            }
            List<ThreadReference> newAllThreads;
            try {

                newAllThreads = new ArrayList<ThreadReference>(jvm.allThreads());

            } catch (InternalException ex) {
                return;
            } catch (VMDisconnectedException ex) {
                return;
            }

            newThreads = new ArrayList<ThreadReference>(newAllThreads);
            newThreads.removeAll(threadsList);
            oldThreads = new ArrayList<ThreadReference>(threadsList);
            oldThreads.removeAll(newAllThreads);

            for (ThreadReference thread : newThreads) {
                ThreadGroupReference group;
                try {
                    group = thread.threadGroup();
                } catch (InternalException ex) {
                    continue;
                } catch (VMDisconnectedException ex) {
                    return;
                } catch (IllegalThreadStateException ex) {
                    continue;
                } catch (ObjectCollectedException ocex) {
                    continue;
                }
                if (group != null) {
                    try {
                        if (newGroups == null) {
                            newGroups = createSubGroups(group);
                        } else {
                            newGroups.addAll(createSubGroups(group));
                        }
                    } catch (ObjectCollectedException ex) {
                        try {
                            if (thread.isCollected()) {
                                continue;
                            }
                        } catch (InternalException ex1) {
                            continue;
                        } catch (VMDisconnectedException ex1) {
                            return;
                        } catch (ObjectCollectedException ex1) {
                            continue;
                        }
                    }
                }
                List<ThreadReference> groupThreads = threadsByGroup.get(group);
                if (groupThreads != null && !groupThreads.contains(thread)) { // could be added by init()
                    groupThreads.add(thread);
                }
            }
            threadsList.addAll(newThreads);

            // Remove old threads:
            for (ThreadReference thread : oldThreads) {
                ThreadGroupReference group;
                try {
                    group = thread.threadGroup();
                } catch (InternalException ex) {
                    group = null;
                } catch (VMDisconnectedException ex) {
                    return;
                } catch (IllegalThreadStateException ex) {
                    group = null;
                } catch (ObjectCollectedException ocex) {
                    group = null;
                }
                List<ThreadReference> groupThreads;
                if (group != null) {
                    groupThreads = threadsByGroup.get(group);
                } else {
                    groupThreads = null;
                    for (List<ThreadReference> testThreads : threadsByGroup.values()) {
                        if (testThreads.contains(thread)) {
                            groupThreads = testThreads;
                        }
                    }
                }
                if (groupThreads != null) {
                    groupThreads.remove(thread);
                }
            }
            threadsList.removeAll(oldThreads);
        }

        //notify listeners about changes
        synchronized (FIRE_CAHNGE_LOCK) {
            if (!canFireChanges) {
                try {
                    FIRE_CAHNGE_LOCK.wait();
                } catch (InterruptedException ex) {
                }
            }
        }
        if (newGroups != null) {
            for (ThreadGroupReference g : newGroups) {
                propertyChangeSupport.firePropertyChange(THREADS_CACHE_PROPERTY_NAME_THREAD_GROUP_ADDED, null, g);
            }
        }
        for (ThreadReference thread : newThreads) {
            propertyChangeSupport.firePropertyChange(THREADS_CACHE_PROPERTY_NAME_THREAD_STARTED, null, getThreadWrapper(thread));
        }
        for (ThreadReference thread : oldThreads) {
            propertyChangeSupport.firePropertyChange(THREADS_CACHE_PROPERTY_NAME_THREAD_DIED, getThreadWrapper(thread), null);
        }
    }

    private List<ThreadGroupReference> getAllGroups() {
        synchronized (REFRESH_LOCK) {
            List<ThreadGroupReference> groupsList = new ArrayList<ThreadGroupReference>();
            collectAllGroups(null, groupsList);
            return groupsList;
        }
    }

    private void collectAllGroups(ThreadGroupReference g, List<ThreadGroupReference> groupList) {
        synchronized (REFRESH_LOCK) {
            List<ThreadGroupReference> gs = groupsByOwnerGroup.get(g);
            if (gs != null) {
                groupList.addAll(gs);
                for (ThreadGroupReference gg : gs) {
                    collectAllGroups(gg, groupList);
                }
            }
        }
    }

    private List<ThreadGroupReference> createSubGroups(ThreadGroupReference group) {
        List<ThreadGroupReference> list = new ArrayList<ThreadGroupReference>(group.threadGroups());
        List<ThreadGroupReference> oldGroups = groupsByOwnerGroup.get(group);
        if (oldGroups == null) {
            groupsByOwnerGroup.put(group, list);
            return list;
        } else {
            for (Iterator<ThreadGroupReference> iter = oldGroups.iterator(); iter.hasNext();) {
                ThreadGroupReference ref = iter.next();
                if (!list.contains(ref)) {
                    iter.remove();
                }
            }
            List<ThreadGroupReference> added = new LinkedList<ThreadGroupReference>();

            for (ThreadGroupReference ref : list) {
                if (!oldGroups.contains(ref)) {
                    oldGroups.add(ref);
                    added.add(ref);
                }
            }
            return added;
        }
    }

    @Override
    public boolean processEvent(Event event) {
        if (event instanceof ThreadStartEvent) {
            ThreadReference thread;
            ThreadGroupReference group;
            try {
                thread = ((ThreadStartEvent) event).thread();
                group = thread.threadGroup();
            } catch (InternalException ex) {
                return true;
            } catch (VMDisconnectedException ex) {
                return true;
            } catch (IllegalThreadStateException ex) {
                return true;
            } catch (ObjectCollectedException ocex) {
                return true;
            }
            List<ThreadGroupReference> addedGroups = null;
            synchronized (this) {
                if (group != null) {
                    try {
                        addedGroups = createSubGroups(group);
                    } catch (ObjectCollectedException ex) {
                        try {
                            if (thread.isCollected()) {
                                return true;
                            }
                        } catch (InternalException ex1) {
                            return true;
                        } catch (VMDisconnectedException ex1) {
                            return true;
                        } catch (ObjectCollectedException ex1) {
                            return true;
                        }
                    }
                }
                List<ThreadReference> groupThreads = threadsByGroup.get(group);
                if (groupThreads != null && !groupThreads.contains(thread)) {
                    groupThreads.add(thread);
                }
                if (!threadsList.contains(thread)) {
                    threadsList.add(thread);
                }
            }
            synchronized (FIRE_CAHNGE_LOCK) {
                if (!canFireChanges) {
                    try {
                        FIRE_CAHNGE_LOCK.wait();
                    } catch (InterruptedException ex) {
                    }
                }
            }
            if (addedGroups != null) {
                for (ThreadGroupReference g : addedGroups) {
                    propertyChangeSupport.firePropertyChange(THREADS_CACHE_PROPERTY_NAME_THREAD_GROUP_ADDED, null, g);
                }
            }
            propertyChangeSupport.firePropertyChange(THREADS_CACHE_PROPERTY_NAME_THREAD_STARTED, null, getThreadWrapper(thread));
        }
        if (event instanceof ThreadDeathEvent) {
            ThreadReference thread;
            ThreadGroupReference group;
            try {
                thread = ((ThreadDeathEvent) event).thread();
            } catch (InternalException ex) {
                return true;
            } catch (VMDisconnectedException ex) {
                return true;
            }
            try {
                group = thread.threadGroup();
            } catch (InternalException ex) {
                return true;
            } catch (VMDisconnectedException ex) {
                return true;
            } catch (IllegalThreadStateException ex) {
                group = null;
            } catch (ObjectCollectedException ocex) {
                group = null;
            }
            synchronized (this) {
                List<ThreadReference> groupThreads;
                if (group != null) {
                    groupThreads = threadsByGroup.get(group);
                } else {
                    groupThreads = null;
                    for (List<ThreadReference> testThreads : threadsByGroup.values()) {
                        if (testThreads.contains(thread)) {
                            groupThreads = testThreads;
                        }
                    }
                }
                if (groupThreads != null) {
                    groupThreads.remove(thread);
                }
                threadsList.remove(thread);
            }
            synchronized (FIRE_CAHNGE_LOCK) {
                if (!canFireChanges) {
                    try {
                        FIRE_CAHNGE_LOCK.wait();
                    } catch (InterruptedException ex) {
                    }
                }
            }
            ThreadWrapper wrapper = getThreadWrapper(thread);
            synchronized (threadWrappers) {
                threadWrappers.remove(thread);
            }
            propertyChangeSupport.firePropertyChange(THREADS_CACHE_PROPERTY_NAME_THREAD_DIED, wrapper, null);
        }
        return true;
    }

    public void suspendAll(Set<ThreadReference> ignoreThreads) {
        List<PropertyChangeEvent> allEvents = new LinkedList<PropertyChangeEvent>();
        List<ThreadReference> allthreads = debugger.getVirtualMachine().allThreads();
        for (ThreadReference ref : allthreads) {
            if (ignoreThreads != null && ignoreThreads.contains(ref)) {
                continue;
            }
            ThreadWrapper w = getThreadWrapper(ref);
            if (w != null) {
                w.suspended();
            }
        }
    }

    public ThreadWrapper ensureThreadIsCached(ThreadReference thread) {
        boolean threadCached = false;
        synchronized (REFRESH_LOCK) {
            threadCached = threadsList.contains(thread);
        }
        if (!threadCached) {
            refresh();
        }
        return getThreadWrapper(thread);
    }

    public void threadStarted(ThreadReference thread) {
        boolean threadCached = false;
        synchronized (REFRESH_LOCK) {
            threadCached = threadsList.contains(thread);
        }
        if (!threadCached) {
            refresh();
        }
    }

    public void threadDied(ThreadReference thread) {
        boolean threadCached = false;
        synchronized (REFRESH_LOCK) {
            threadCached = threadsList.contains(thread);
        }
        if (threadCached) {
            refresh();
        }
    }

    public void notifyCurrentThreadChange(ThreadWrapper oldCurrentThread, ThreadWrapper currentThread) {
        propertyChangeSupport.firePropertyChange(new PropertyChangeEvent(this, PROPERTY_NAME_CURRENT_THREAD, oldCurrentThread, currentThread));
    }

    public List<ThreadGroupReference> getTopLevelGroups() {
        synchronized (REFRESH_LOCK) {
            return groupsByOwnerGroup.get(null);
        }
    }

    public List<ThreadGroupReference> getSubGroups(ThreadGroupReference parent) {
        synchronized (REFRESH_LOCK) {
            return groupsByOwnerGroup.get(parent);
        }
    }

    public List<ThreadReference> getGroupThreads(ThreadGroupReference parent) {
        synchronized (REFRESH_LOCK) {
            return threadsByGroup.get(parent);
        }
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }
    private final WeakHashMap<ThreadGroupReference, ThreadGroupWrapper> groupWrappers = new WeakHashMap<ThreadGroupReference, ThreadGroupWrapper>();
    private final WeakHashMap<ThreadReference, ThreadWrapper> threadWrappers = new WeakHashMap<ThreadReference, ThreadWrapper>();

    public List<ThreadGroupWrapper> getSubGroups(ThreadGroupWrapper group) {
        synchronized (REFRESH_LOCK) {
            ThreadGroupReference ref = group.getThreadGroupReference();
            if (ref == null) {
                return Collections.emptyList();
            } else {
                synchronized (groupWrappers) {
                    List<ThreadGroupReference> subgroups = getSubGroups(ref);
                    if (subgroups == null) {
                        return Collections.emptyList();
                    }
                    List<ThreadGroupWrapper> result = new LinkedList<ThreadGroupWrapper>();
                    for (ThreadGroupReference sg : subgroups) {
                        ThreadGroupWrapper w = groupWrappers.get(sg);
                        if (w == null) {
                            w = new ThreadGroupWrapper(debugger, sg);
                            groupWrappers.put(sg, w);
                        }
                        result.add(w);
                    }
                    return result;
                }
            }
        }
    }

    public List<ThreadGroupWrapper> getGroups() {
        synchronized (REFRESH_LOCK) {
            List<ThreadGroupReference> subgroups = getTopLevelGroups();
            if (subgroups == null) {
                return Collections.emptyList();
            }
            synchronized (groupWrappers) {
                List<ThreadGroupWrapper> result = new LinkedList<ThreadGroupWrapper>();
                for (ThreadGroupReference sg : subgroups) {
                    ThreadGroupWrapper w = groupWrappers.get(sg);
                    if (w == null) {
                        w = new ThreadGroupWrapper(debugger, sg);
                        groupWrappers.put(sg, w);
                    }
                    result.add(w);
                }
                return result;
            }
        }
    }

    public List<ThreadWrapper> getThreads(ThreadGroupWrapper group) {
        synchronized (REFRESH_LOCK) {
            ThreadGroupReference ref = group.getThreadGroupReference();
            if (ref == null) {
                return Collections.emptyList();
            } else {

                List<ThreadReference> groupThreads = getGroupThreads(ref);
                if (groupThreads == null) {
                    return Collections.emptyList();
                }
                List<ThreadWrapper> result = new LinkedList<ThreadWrapper>();
                for (ThreadReference gt : groupThreads) {
                    result.add(getThreadWrapper(gt));
                }
                return result;

            }
        }
    }

    public List<ThreadWrapper> getAllThreads() {
        synchronized (REFRESH_LOCK) {
            List<ThreadWrapper> result = new LinkedList<ThreadWrapper>();
            for (ThreadReference ref : threadsList) {
                result.add(getThreadWrapper(ref));
            }
            return result;
        }
    }

    public ThreadsCollector getThreadsCollector() {
        return threadsCollector;
    }

    public ThreadWrapper getThreadWrapper(ThreadReference thread) {
        synchronized (threadWrappers) {
            ThreadWrapper w = threadWrappers.get(thread);
            if (w == null) {
                w = new ThreadWrapper(debugger, thread);
                threadWrappers.put(thread, w);
            }
            return w;
        }
    }
}
