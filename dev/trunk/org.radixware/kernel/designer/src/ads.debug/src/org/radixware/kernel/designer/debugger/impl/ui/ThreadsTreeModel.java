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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.netbeans.spi.debugger.ContextProvider;
import org.netbeans.spi.debugger.ui.Constants;
import org.netbeans.spi.viewmodel.ModelEvent;
import org.radixware.kernel.designer.debugger.impl.ThreadGroupWrapper;
import org.radixware.kernel.designer.debugger.impl.ThreadWrapper;
import org.radixware.kernel.designer.debugger.impl.ThreadsCache;


public class ThreadsTreeModel extends AbstractTreeModel {

    private final Listener listener;

    public ThreadsTreeModel(ContextProvider contextProvider) {
        super(contextProvider,100);
        this.listener = new Listener();
        this.debugger.getThreadsCache().addPropertyChangeListener(listener);
    }
    private Map<Object, List<Object>> parent2children = new WeakHashMap<>();

    private class Listener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (ThreadWrapper.PROPERTY_NAME_SUSPENDED.equals(evt.getPropertyName())) {
                ThreadWrapper w = (ThreadWrapper) evt.getSource();
                synchronized (childrenLock) {
                    if (parent2children.containsKey(w)) {
                        fireValueChange(w, Constants.THREAD_SUSPENDED_COLUMN_ID);
                    }
                }
            } else if (ThreadsCache.THREADS_CACHE_PROPERTY_NAME_THREAD_DIED.equals(evt.getPropertyName())) {
                ThreadWrapper tw = (ThreadWrapper) evt.getOldValue();
                synchronized (childrenLock) {
                    parent2children.remove(tw);
                    List<Object> nodes2update = new LinkedList<Object>();
                    for (Map.Entry<Object, List<Object>> node : parent2children.entrySet()) {
                        if (node.getValue() != null && node.getValue().contains(tw)) {
                            node.setValue(null);
                            nodes2update.add(node.getKey());
                        }
                    }
                    fireNodeChange(nodes2update, ModelEvent.NodeChanged.CHILDREN_MASK);
                }
            } else if (ThreadsCache.THREADS_CACHE_PROPERTY_NAME_THREAD_STARTED.equals(evt.getPropertyName())) {
                synchronized (childrenLock) {
                    parent2children.clear();
                }
                fireModelChange();
            } else if (ThreadsCache.THREADS_CACHE_PROPERTY_NAME_THREAD_GROUP_ADDED.equals(evt.getPropertyName())) {
                synchronized (childrenLock) {
                    parent2children.clear();
                }
                fireModelChange();
            } else if (ThreadsCache.PROPERTY_NAME_CURRENT_THREAD.equals(evt.getPropertyName())) {
                ThreadWrapper oldw = (ThreadWrapper) evt.getOldValue();
                final List<Object> nodes2update = new LinkedList<Object>();
                if (oldw != null) {
                    collectPathNodes(oldw, nodes2update);
                }
                ThreadWrapper neww = (ThreadWrapper) evt.getNewValue();
                if (neww != null) {
                    collectPathNodes(neww, nodes2update);
                }
                fireNodeChange(nodes2update, ModelEvent.NodeChanged.DISPLAY_NAME_MASK | ModelEvent.NodeChanged.ICON_MASK);
            }
        }
    }

    private void collectPathNodes(Object leaf, List<Object> nodes2update) {
        synchronized (childrenLock) {

            nodes2update.add(leaf);
            Object nodeToSearch = leaf;
            loop:
            while (true) {
                for (Map.Entry<Object, List<Object>> node : parent2children.entrySet()) {
                    if (node.getValue() != null && node.getValue().contains(nodeToSearch)) {
                        nodeToSearch = node.getKey();
                        if (!nodes2update.contains(nodeToSearch) && nodeToSearch != ROOT) {
                            nodes2update.add(nodeToSearch);
                        }
                        continue loop;
                    }
                }
                break;//NOPMD
            }

        }
    }
    private final Object childrenLock = new Object();

    @Override
    protected List<Object> getChildren(Object parent) {
        synchronized (childrenLock) {
            List<Object> children = parent2children.get(parent);
            if (children == null) {
                if (parent instanceof ThreadWrapper) {
                    children = Collections.emptyList();
                } else if (parent instanceof ThreadGroupWrapper) {
                    List<Object> allChildren = new LinkedList<>();
                    allChildren.addAll(((ThreadGroupWrapper) parent).getGroups());
                    allChildren.addAll(((ThreadGroupWrapper) parent).getThreads());
                    children = allChildren;
                } else if (parent == ROOT) {
                    List<Object> allChildren = new LinkedList<Object>(debugger.getThreadsCache().getGroups());
                    children = allChildren;
                } else {
                    children = Collections.emptyList();
                }
                for (Object obj : children) {
                    if (obj instanceof ThreadWrapper) {
                        ((ThreadWrapper) obj).addPropertyChangeListener(listener);
                    }
                }
                parent2children.put(parent, children);
            }
            return children;
        }
    }
}
