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

import java.util.LinkedList;
import java.util.List;
import org.netbeans.spi.debugger.ContextProvider;
import org.netbeans.spi.viewmodel.ModelEvent;
import org.netbeans.spi.viewmodel.ModelListener;
import org.netbeans.spi.viewmodel.TreeModel;
import org.netbeans.spi.viewmodel.UnknownTypeException;
import org.openide.util.RequestProcessor;
import org.openide.util.RequestProcessor.Task;
import org.radixware.kernel.designer.debugger.RadixDebugger;


public abstract class AbstractTreeModel implements TreeModel {

    protected RadixDebugger debugger;
    private final RequestProcessor rp = new RequestProcessor("ViewModel");
    private final int taskDelay;
    private final List<ModelListener> listeners = new LinkedList<ModelListener>();

    public AbstractTreeModel(ContextProvider contextProvider, int taskDelay) {
        if (contextProvider != null) {
            this.debugger = contextProvider.lookupFirst(null, RadixDebugger.class);
        } else {
            this.debugger = null;
        }
        this.taskDelay = taskDelay;
    }

    @Override
    public Object getRoot() {
        return ROOT;
    }

    @Override
    public Object[] getChildren(Object parent, int from, int to) throws UnknownTypeException {
        List<Object> allChildren = getChildren(parent);
        int len = to - from;
        if (len > 0) {
            Object[] result = new Object[len];
            if (from < allChildren.size()) {
                to = Math.min(to, allChildren.size());
                for (int i = from, j = 0; i < to; i++, j++) {
                    result[j] = allChildren.get(i);
                }
            }
            return result;
        } else {
            return new Object[0];
        }
    }

    @Override
    public boolean isLeaf(Object node) throws UnknownTypeException {
        List<Object> children = getChildren(node);
        return children == null || children.isEmpty();
    }

    @Override
    public int getChildrenCount(Object node) throws UnknownTypeException {
        return getChildren(node).size();
    }

    protected abstract List<Object> getChildren(Object parent);

    @Override
    public void addModelListener(ModelListener l) {
        synchronized (listeners) {
            if (!listeners.contains(l)) {
                listeners.add(l);
            }
        }
    }

    @Override
    public void removeModelListener(ModelListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }

    protected void fireModelChange() {
        Task task = rp.create(new Runnable() {

            @Override
            public void run() {
                synchronized (listeners) {
                    ModelEvent e = new ModelEvent.TreeChanged(this);
                    for (ModelListener l : listeners) {
                        l.modelChanged(e);
                    }
                }
            }
        });
        task.run();
    }

    protected void fireNodeChange(final List<Object> nodes, final int change) {
        Task task = rp.create(new Runnable() {

            @Override
            public void run() {
                for (Object node : nodes) {
                    synchronized (listeners) {
                        ModelEvent e = new ModelEvent.NodeChanged(this, node, change);
                        for (ModelListener l : listeners) {
                            l.modelChanged(e);
                        }
                    }
                }
            }
        });
        task.schedule(taskDelay);
    }

    protected void fireValueChange(final Object node, final String columnId) {
        Task task = rp.create(new Runnable() {

            @Override
            public void run() {
                synchronized (listeners) {
                    ModelEvent e = new ModelEvent.TableValueChanged(this, node, columnId);
                    for (ModelListener l : listeners) {
                        l.modelChanged(e);
                    }
                }
            }
        });
        task.schedule(taskDelay);
    }
}
