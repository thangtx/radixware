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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.openide.util.WeakListeners;


public class ThreadsCollector {

    private ThreadsCache threadsCache;
    private PropertyChangeListener changesInThreadsListener;
    private final Map<ThreadWrapper, ThreadStateListener> threadStateListeners = new WeakHashMap<ThreadWrapper, ThreadStateListener>();
    private final List<ThreadWrapper> threads = new ArrayList<ThreadWrapper>();
    private PropertyChangeSupport propChangeSupport = new PropertyChangeSupport(this);

    public ThreadsCollector(ThreadsCache threadsCache) {
        this.threadsCache = threadsCache;
        List<ThreadWrapper> allThreads = threadsCache.getAllThreads();
        synchronized (threads) {
            threads.addAll(allThreads);
        }
        changesInThreadsListener = new ChangesInThreadsListener();
        threadsCache.addPropertyChangeListener(WeakListeners.propertyChange(changesInThreadsListener, threadsCache));
        for (ThreadWrapper thread : allThreads) {
            watchThread(thread);
        }
    }

    public List<ThreadWrapper> getAllThreads() {
        synchronized (threads) {
            return Collections.unmodifiableList(new ArrayList<ThreadWrapper>(threads));
        }
    }

    private void watchThread(ThreadWrapper thread) {
        synchronized (threadStateListeners) {
            if (!threadStateListeners.containsKey(thread)) {
                threadStateListeners.put(thread, new ThreadStateListener(thread));
            }
        }
    }

    public boolean isSomeThreadRunning() {
        for (ThreadWrapper thread : getAllThreads()) {
            if (!thread.isSuspended() && !thread.isMethodInvoking()) {
                return true;
            }
        }
        return false;
    }

    public boolean isSomeThreadSuspended() {
        for (ThreadWrapper thread : getAllThreads()) {
            if (thread.isSuspended() || thread.isMethodInvoking()) {
                return true;
            }
        }
        return false;
    }

    private class ThreadStateListener implements PropertyChangeListener {

        //private ThreadWrapper thread;
        public ThreadStateListener(ThreadWrapper thread) {
            thread.addPropertyChangeListener(WeakListeners.propertyChange(this, thread));
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (ThreadWrapper.PROPERTY_NAME_SUSPENDED.equals(evt.getPropertyName())) {
                if ("methodInvoke".equals(evt.getPropagationId())) {
                    return; // Ignore events associated with method invocations
                }
                ThreadWrapper thread = (ThreadWrapper) evt.getSource();
                if (thread.isSuspended()) {
                    propChangeSupport.firePropertyChange(ThreadWrapper.PROPERTY_NAME_SUSPENDED, null, thread);
                } else {
                    propChangeSupport.firePropertyChange(ThreadWrapper.PROPERTY_NAME_RESUMED, null, thread);
                }
            }
        }
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

    private class ChangesInThreadsListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String propertyName = evt.getPropertyName();
            if (ThreadsCache.THREADS_CACHE_PROPERTY_NAME_THREAD_STARTED.equals(propertyName)) {
                ThreadWrapper thread = (ThreadWrapper) evt.getNewValue();
                watchThread(thread);
                synchronized (threads) {
                    if (!threads.contains(thread)) {
                        threads.add(thread); // Could be already added in constructor...
                    }
                }
                propChangeSupport.firePropertyChange(ThreadWrapper.PROPERTY_NAME_STARTED, evt.getOldValue(), evt.getNewValue());
            } else if (ThreadsCache.THREADS_CACHE_PROPERTY_NAME_THREAD_DIED.equals(propertyName)) {
                ThreadWrapper thread = (ThreadWrapper) evt.getOldValue();
                synchronized (threads) {
                    threads.remove(thread);
                }
                propChangeSupport.firePropertyChange(ThreadWrapper.PROPERTY_NAME_DIED, evt.getOldValue(), evt.getNewValue());
            } else if (ThreadsCache.THREADS_CACHE_PROPERTY_NAME_THREAD_GROUP_ADDED.equals(propertyName)) {
                propChangeSupport.firePropertyChange(ThreadGroupWrapper.PROP_NAME_ADDED, evt.getOldValue(), evt.getNewValue());
            }
        }
    }
}
