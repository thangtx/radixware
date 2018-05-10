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
package org.radixware.kernel.common.defs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.radixware.kernel.common.defs.ClipboardSupport.DuplicationResolver.Resolution;
import org.radixware.kernel.common.defs.ClipboardSupport.Transfer;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEventSource;

/**
 * Generic container for subobjects with type T. Supports subset of list
 * operations on objects: <ul> <li>add (with ownership check)</li> <li>remove
 * (by index or by pointer)</li> <li>clear</li> </ul> Also this container
 * provides notification and visitor supports.
 *
 */
public class RadixObjects<T extends RadixObject> extends RadixObject implements Iterable<T> {

    /**
     * Collection change type
     */
    public enum EChangeType {

        /**
         * Collection was enlarged.
         */
        ENLARGE,
        /**
         * Collection was shrinked.
         */
        SHRINK,
        /**
         * Collection was modified.
         */
        MODIFY,
        /**
         * Collection items were swaped.
         */
        SWAP;
    }

    /**
     * Modification event, triggered by all operations modifing the container.
     * To receive the event instantiate implement interface
     * {@linkplain ContainerChangesListener} and register implementation
     * instance using {@linkplain #getContainerChangesSupport()
     * }
     *
     */
    public static final class ContainerChangedEvent extends RadixEvent {

        public final RadixObject object;
        public final RadixObject object2;
        public final EChangeType changeType;

        public ContainerChangedEvent(RadixObject object, EChangeType changeType) {
            this.object = object;
            this.object2 = null;
            this.changeType = changeType;
        }

        public ContainerChangedEvent(RadixObject object, RadixObject object2, EChangeType changeType) {
            this.object = object;
            this.object2 = object2;
            this.changeType = changeType;
        }
    }

    /**
     * Interface for objects container event listener
     */
    public interface ContainerChangesListener extends IRadixEventListener<ContainerChangedEvent> {
    }

    /**
     * Event source class for objects container
     */
    public static class ContainerChangeSupport extends RadixEventSource<ContainerChangesListener, ContainerChangedEvent> {
    }
    private final ContainerChangeSupport changesSupport = new ContainerChangeSupport();
    private final List<T> list = new CopyOnWriteArrayList<>();

    protected RadixObjects(RadixObject container) {
        super.setContainer(container);
        assert (container != null);
    }

    protected RadixObjects() { // for extendable definitions
        super();
    }

    protected RadixObjects(String name) {
        super(name);
    }

    public T last() {
        synchronized (this) {
            return isEmpty() ? null : get(size() - 1);
        }
    }

    public T first() {
        synchronized (this) {
            return isEmpty() ? null : get(0);
        }
    }

    /**
     * Called after add object in the list and before fire
     * ContainerChangedEvent. Designed for overriding.
     *
     * @param object added object
     */
    protected void onAdd(T object) {
    }

    private void fireChanges(T object, EChangeType changeType) {
        changesSupport.fireEvent(new ContainerChangedEvent(object, changeType));
    }

    private void fireChanges(T object, T object2, EChangeType changeType) {
        changesSupport.fireEvent(new ContainerChangedEvent(object, object2, changeType));
    }
    
    protected void afterAdd(T object) {
        try {
            RadixObject container = getContainer();
            if (container != null) {
                object.setContainer(this);
            }
        } catch (RadixError e) {
            list.remove(object);
            throw e;
        }

        onAdd(object);
    }
    
    protected void afterRemove(T object) {
        if (this.getContainer() != null) {
            object.setContainer(null);
        }
    }

    /**
     * Adds new object to the container. Set this collection as object
     * container. Returns true if any modifications occured, false otherwise
     * Fires {@linkplain ContainerChangedEvent}
     *
     * @throws RadixError if given object is already has container.
     */
    public void add(T object) {
        if (object == null) {
            throw new RadixObjectError("Attemp to add null into RadixObjects", this);
        }

        synchronized (this) {
            list.add(object);
            afterAdd(object);
        }

        setEditState(Definition.EEditState.MODIFIED);
        fireChanges(object, EChangeType.ENLARGE);
    }

    /**
     * Adds new object to the container. Returns true if any modifications
     * occured, false otherwise Fires {@linkplain ContainerChangedEvent
     *
     * @throws RadixError if given object is already has container.
     */
    public void add(int index, T object) {
        if (object == null) {
            throw new RadixObjectError("Attemp to add null into RadixObjects", this);
        }

        synchronized (this) {
            if (index < 0) {
                list.add(0, object);
            } else if (index >= size()) {
                list.add(object);
            } else {
                list.add(index, object);
            }
            afterAdd(object);
        }

        setEditState(Definition.EEditState.MODIFIED);
        fireChanges(object, EChangeType.ENLARGE);
    }

    /**
     * Called after object removed from list and before fire
     * CollectionChangedEvent. Designed for overriding.
     *
     * @param object removed object
     */
    protected void onRemove(T object) {
    }

    /**
     * Removes definition from container Returns true if any modifications
     * occured, false otherwise Fires {@linkplain DefinitionListChangedEvent
     */
    public boolean remove(T object) {
        if (object == null) {
            throw new RadixObjectError("Attemp to remove null from RadixObjects", this);
        }

        boolean removed;

        synchronized (this) {
            removed = list.remove(object);
            if (removed) {
                onRemove(object);
            }
        }

        if (removed) {
            afterRemove(object);

            setEditState(Definition.EEditState.MODIFIED);
            fireChanges(object, EChangeType.SHRINK);
        }

        return removed;
    }

    /**
     * Removes object from container by geven index Returns true if any
     * modifications occured, false otherwise Fires {@linkplain DefinitionListChangedEvent
     */
    public T remove(int index) {
        T object = get(index);
        remove(object);
        return object;
    }

    /**
     * Called after clear and before fire CollectionChangedEvent Designed for
     * overriding.
     */
    protected void onClear() {
    }

    /**
     * Clears container. Fires {@linkplain DefinitionListChangedEvent with null in definition field
     */
    public void clear() {
        final List<T> removed;

        synchronized (this) {
            if (list.isEmpty()) {
                return;
            }

            removed = new ArrayList<>(list);

            list.clear();

            for (T object : removed) {
                onRemove(object);
            }

            onClear();
        }

        if (this.getContainer() != null) {
            for (T object : removed) {
                object.setContainer(null);
            }
        }

        setEditState(Definition.EEditState.MODIFIED);
        fireChanges(null, EChangeType.SHRINK);
    }

    public T get(int index) {
        return list.get(index);
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public synchronized void swap(int idx1, int idx2) {
        final T obj1, obj2;

        synchronized (this) {
            obj1 = list.get(idx1);
            obj2 = list.get(idx2);
            if (idx1 == idx2) { // after - check for out of range
                return;
            }
            list.set(idx1, obj2);
            list.set(idx2, obj1);
        }

        setEditState(Definition.EEditState.MODIFIED);
        fireChanges(obj1, obj2, EChangeType.SWAP);
    }

    /**
     * Reorder all children with a given permutation.
     *
     * @param perm permutation with the length of current nodes. The permutation
     * lists the new positions of the original nodes, that is, for nodes
     * <code>[A,B,C,D]</code> and permutation <code>[0,3,1,2]</code>, the final
     * order would be <code>[A,C,D,B]</code>.
     * @exception IllegalArgumentException if the permutation is not valid
     */
    public void reorder(int[] perm) {
        synchronized (this) {
            int perm2[] = perm;
            int size = size();
            /**
             * Protect collection from resorting operation initialized by
             * multiple collection displayer
             */
            if (perm.length > size) {
                perm2 = new int[size];
                int j = 0;
                for (int i = 0; i < perm.length; i++) {
                    if (perm[i] < size) {//invalid range
                        perm2[j] = perm[i];
                        j++;
                        if (j == size) {
                            break;
                        }
                    }
                }
                if (j < size) {
                    return;
                }
            }
            final List<T> old = list();
            for (int i = 0; i < size; i++) {
                list.set(perm2[i], old.get(i));
            }
        }
        setEditState(Definition.EEditState.MODIFIED);
        fireChanges(null, EChangeType.MODIFY);
    }

    public void moveUp(int idx) {
        swap(idx, idx - 1);
    }

    public void moveDown(int idx) {
        swap(idx, idx + 1);
    }

    /**
     * Returns list content of container
     */
    public List<T> list() {
        return new ArrayList<>(list);
    }

    public List<T> list(IFilter<T> filter) {
        if (filter == null) {
            throw new NullPointerException();
        }
        final ArrayList<T> res = new ArrayList<>(list.size());
        for (T obj : list) {
            if (filter.isTarget(obj)) {
                res.add(obj);
            }
        }
        return res;
    }

    public boolean contains(T object) {
        if (object != null) {
            return list.contains(object);
        } else {
            return false;
        }

    }

    /**
     * Returns list changes support instance - registry for list event listeners
     * Usage example:
     * @code myDefinitions.getListChangesSupport().addEventListner(myListener);
     */
    public ContainerChangeSupport getContainerChangesSupport() {
        return changesSupport;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        for (T def : list) {
            def.visit(visitor, provider);
        }
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    public <R extends T> T overwrite(R sourceDef) {
        throw new RadixObjectError("Operation not supported: Overwrite.", this);
    }

    public int indexOf(T object) {
        return list.indexOf(object);
    }

    protected ClipboardSupport.CanPasteResult canPaste(List<Transfer> transfers, ClipboardSupport.DuplicationResolver resolver) {
        return ClipboardSupport.CanPasteResult.NO;
    }

    protected abstract class RadixObjectsClipboardSupport extends ClipboardSupport<RadixObjects> {

        public RadixObjectsClipboardSupport() {
            super(RadixObjects.this);
        }

        @Override
        public boolean canCopy() {
            return false;
        }

        @Override
        public CanPasteResult canPaste(List<Transfer> transfers, DuplicationResolver resolver) {
            return RadixObjects.this.canPaste(transfers, resolver);
        }

        @Override
        @SuppressWarnings("unchecked")
        public void paste(List<Transfer> transfers, DuplicationResolver resolver) {
            checkForCanPaste(transfers, resolver);
            try {
                resolver.enterPasteMode();
                for (Transfer transfer : transfers) {
                    if (!transfer.beforePaste(RadixObjects.this)) {
                        continue;
                    }
                    final RadixObject objectInClipboard = transfer.getObject();
                    RadixObject duplicate = findDuplicate(objectInClipboard);
                    if (duplicate != null) {
                        if (resolver.resolveDuplication(objectInClipboard, duplicate) == Resolution.CANCEL) {
                            continue;
                        } else {
                            RadixObjects.this.remove((T) duplicate);
                        }
                    }

                    RadixObjects.this.add((T) objectInClipboard);
                }
            } finally {
                resolver.leavePasteMode();
            }
        }

        protected abstract RadixObject findDuplicate(RadixObject objectInClipboard);
    }

    @Override
    public ClipboardSupport<? extends RadixObject> getClipboardSupport() {
        return new RadixObjectsClipboardSupport() {
            @Override
            protected RadixObject findDuplicate(RadixObject objectInClipboard) {
                return null;
            }
        };
    }

    @Override
    public String getToolTip() {
        return "";
    }

    @Override
    protected boolean isQualifiedNamePart() {
        return false;
    }
}
