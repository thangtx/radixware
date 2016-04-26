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
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.WeakHashMap;
import org.netbeans.spi.debugger.ContextProvider;
import org.radixware.kernel.designer.debugger.impl.ArrayReferenceWrapper;
import org.radixware.kernel.designer.debugger.impl.CallStackFrame;
import org.radixware.kernel.designer.debugger.impl.LocalVariableWrapper;
import org.radixware.kernel.designer.debugger.impl.ObjectReferenceWrapper;
import org.radixware.kernel.designer.debugger.impl.ReferenceTypeWrapper;
import org.radixware.kernel.designer.debugger.impl.ThisReferenceWrapper;
import org.radixware.kernel.designer.debugger.impl.ThreadWrapper;
import org.radixware.kernel.designer.debugger.impl.ThreadsCache;
import org.radixware.kernel.designer.debugger.impl.ValueWrapper;
import org.radixware.kernel.designer.debugger.impl.VariableWrapper;


public class LocalsViewTreeModel extends AbstractTreeModel {

    static class PropertiesGroup {

        ObjectReferenceWrapper objectWrapper;
        private List<Object> nodes = null;

        public PropertiesGroup(ObjectReferenceWrapper objectWrapper) {
            this.objectWrapper = objectWrapper;
        }

        List<Object> getNodes() {
            synchronized (this) {
                if (nodes == null) {
                    nodes = new LinkedList<Object>();
                    nodes.addAll(objectWrapper.getProperties());
                }
                return nodes;
            }
        }

        public String getDisplayName() {
            return "Properties";
        }
    }

    static class FieldGroup {

        ObjectReferenceWrapper objectWrapper;
        ReferenceTypeWrapper typeWrapper;
        private List<Object> nodes = null;

        public FieldGroup(ObjectReferenceWrapper wrapper) {
            this.objectWrapper = wrapper;
        }

        public FieldGroup(ReferenceTypeWrapper wrapper) {
            this.typeWrapper = wrapper;
        }

        List<Object> getNodes() {
            synchronized (this) {
                if (nodes == null) {
                    nodes = new LinkedList<Object>();
                    if (typeWrapper != null) {
                        nodes.addAll(typeWrapper.getStaticFields());
                    } else {
                        nodes.addAll(objectWrapper.getInheritedFields());
                    }
                }
                return nodes;
            }
        }

        public String getDisplayName() {
            if (objectWrapper != null) {
                return "Inherited";
            } else {
                return "Static";
            }
        }
    }
    private final Listener listener;
    private final WeakHashMap<Object, List<Object>> obj2children = new WeakHashMap<Object, List<Object>>();

    public LocalsViewTreeModel(ContextProvider contextProvider) {
        super(contextProvider, 120);
        this.listener = new Listener();
        debugger.getThreadsCache().addPropertyChangeListener(listener);
        ThreadWrapper currentThread = debugger.getCurrentThread();
        if (currentThread != null) {
            currentThread.addPropertyChangeListener(listener);
        }
    }

    private void resetChildrenCache() {
        synchronized (obj2children) {
            obj2children.clear();
        }
    }

    private List<Object> getChildren(ObjectReferenceWrapper objRef) {
        List<Object> nodes = new LinkedList<Object>(objRef.getOwnFields());
        if (objRef.hasInheritedFields()) {
            nodes.add(0, new FieldGroup(objRef));
        }
        if (objRef.hasStaticFields()) {
            nodes.add(0, new FieldGroup(objRef.getTypeWrapper()));
        }
        if (objRef.hasProperties()) {
            nodes.add(0, new PropertiesGroup(objRef));
        }

        return nodes;
    }

    @Override
    protected List<Object> getChildren(Object parent) {
        synchronized (obj2children) {
            List<Object> nodes = obj2children.get(parent);
            if (nodes != null) {
                return nodes;
            }
            if (parent == ROOT) {
                ThreadWrapper thread = debugger.getCurrentThread();
                if (thread != null && thread.isSuspended()) {
                    CallStackFrame frame = thread.getCurrentFrame();
                    if (frame != null) {
                        nodes = new LinkedList<Object>();
                        ObjectReferenceWrapper thisRef = frame.getThisReference();
                        if (thisRef != null) {
                            nodes.add(thisRef);
                        } else {
                            ReferenceTypeWrapper tw = frame.getTypeReference();
                            if (tw != null) {
                                nodes.add(new FieldGroup(tw));
                            }
                        }
                        List<LocalVariableWrapper> vars = frame.getLocals();
                        nodes.addAll(vars);
                    } else {
                        nodes = Collections.emptyList();
                    }
                } else {
                    nodes = Collections.emptyList();
                }
            } else if (parent instanceof ThisReferenceWrapper) {
                nodes = getChildren((ThisReferenceWrapper) parent);
            } else if (parent instanceof VariableWrapper) {
                ValueWrapper value = ((VariableWrapper) parent).getValue();
                if (value instanceof ObjectReferenceWrapper) {
                    if (value instanceof ArrayReferenceWrapper) {
                        nodes = ((ArrayReferenceWrapper) value).getValuesOrRanges();
                    } else {
                        nodes = getChildren((ObjectReferenceWrapper) value);
                    }
                } else {
                    nodes = Collections.emptyList();
                }
            } else if (parent instanceof ArrayReferenceWrapper.ValueRange) {
                nodes = ((ArrayReferenceWrapper.ValueRange) parent).getValuesOrRanges();
            } else if (parent instanceof FieldGroup) {
                nodes = ((FieldGroup) parent).getNodes();
            } else if (parent instanceof PropertiesGroup) {
                nodes = ((PropertiesGroup) parent).getNodes();
            } else {
                nodes = Collections.emptyList();
            }
            obj2children.put(parent, nodes);
            return nodes;
        }
    }

    private class Listener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (ThreadsCache.PROPERTY_NAME_CURRENT_THREAD.equals(evt.getPropertyName())) {
                ThreadWrapper oldCurrentThread = (ThreadWrapper) evt.getOldValue();
                if (oldCurrentThread != null) {
                    oldCurrentThread.removePropertyChangeListener(this);
                }
                ThreadWrapper newCurrentThread = (ThreadWrapper) evt.getNewValue();
                if (newCurrentThread != null) {
                    newCurrentThread.addPropertyChangeListener(this);
                }
                resetChildrenCache();
                fireModelChange();
            } else if (ThreadWrapper.PROPERTY_NAME_SUSPENDED.equals(evt.getPropertyName())) {
                resetChildrenCache();
                fireModelChange();
            } else if (ThreadWrapper.PROPERTY_NAME_CURRENT_FRAME.equals(evt.getPropertyName())) {
                resetChildrenCache();
                fireModelChange();
            }
        }
    }
}
