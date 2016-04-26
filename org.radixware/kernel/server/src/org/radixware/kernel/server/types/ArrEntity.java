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

package org.radixware.kernel.server.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.server.arte.Arte;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.server.exceptions.EntityObjectNotExistsError;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.utils.SrvValAsStr;

public class ArrEntity<T extends Entity> extends Arr<T> {

    private static final long serialVersionUID = -7787248830081441765L;
    private final Arte arte;

    public ArrEntity(final Arte arte) {
        super();
        this.arte = arte;
    }

    public ArrEntity(final Arte arte, final Pid[] arr) {
        this(arte);
        final List<? extends T> lst = createObjects(arr);
        addAll(lst);
    }

    public ArrEntity(final Arte arte, final int initialCapacity) {
        super(initialCapacity);
        this.arte = arte;
    }

    public ArrEntity(final Arte arte, final T[] arr) {
        super(arr);
        this.arte = arte;
    }

    public ArrEntity(final Arte arte, final Collection<? extends T> c) {
        super(c);
        this.arte = arte;
    }

    public ArrEntity(final Arte arte, final Id entityId, final String[] pidsAsStrs) {
        this(arte);
        final List<? extends T> lst = createObjects(arte, entityId, pidsAsStrs);
        addAll(lst);
    }

    public ArrEntity(final Arte arte, final Id[] entityIds, final String[] pidsAsStrs) {
        this(arte);
        final List<? extends T> lst = createObjects(arte, entityIds, pidsAsStrs);
        addAll(lst);
    }

    public static final ArrEntity fromValAsStr(final Arte arte, final String valAsStr) {
        if (valAsStr == null) {
            return null;
        }
        final Arr<Pid> arrPids = new Arr<Pid>() {
            @Override
            public EValType getItemValType() {
                return ITEM_VAL_TYPE;
            }
        };
        restoreArrFromValAsStr(arrPids, valAsStr, ITEM_VAL_TYPE, new ItemAsStrParser() {
            @Override
            public Object fromStr(final String asStr) {
                return SrvValAsStr.fromStr(arte, asStr, ITEM_VAL_TYPE);
            }
        });
        final ArrEntity arr = new ArrEntity(arte);
        for (Pid pid : arrPids) {
            if (pid == null) {
                arr.add(null);
            } else {
                arr.add(new LazyRef(pid));
//                Entity ent;
//                try {
//                    ent = arte.getEntityObject(pid);
//                } catch (EntityObjectNotExistsError e) {
//                    ent = new BadRef(arte, pid); //delaying exception to allow array be restored partly
//                }
//                arr.add(ent);
            }
        }
        return arr;
    }

    public final ArrStr toArrStr() {
        final ArrStr arr = new ArrStr(size());
        for (T e : this) {
            arr.add(e == null ? null : e.getPid().toString());
        }
        return arr;
    }

    private static final <T> List<T> createObjects(final Arte arte, final Id entityId, final String[] pidsAsStrs) {
        if (pidsAsStrs == null) {
            return null;
        }
        final List<T> objs = new LinkedList<T>();
        for (int i = 0; i < pidsAsStrs.length; i++) {
            if (pidsAsStrs[i] == null) {
                objs.add(null);
            } else {
                objs.add((T) arte.getEntityObject(new Pid(arte, entityId, pidsAsStrs[i])));
            }
        }
        return objs;
    }

    private static final <T> List<T> createObjects(final Arte arte, final Id[] entityIds, final String[] pidsAsStrs) {
        if (pidsAsStrs == null) {
            return null;
        }
        final List<T> objs = new LinkedList<T>();
        for (int i = 0; i < pidsAsStrs.length; i++) {
            if (pidsAsStrs[i] == null) {
                objs.add(null);
            } else {
                objs.add((T) arte.getEntityObject(new Pid(arte, entityIds[i], pidsAsStrs[i])));
            }
        }
        return objs;
    }

    private static final <T> List<T> createObjects(final Pid[] pids) {
        if (pids == null) {
            return null;
        }
        final List<T> objs = new LinkedList<T>();
        for (Pid pid : pids) {
            if (pid == null) {
                objs.add(null);
            } else {
                objs.add((T) pid.getArte().getEntityObject(pid));
            }
        }
        return objs;
    }
    public static final EValType ITEM_VAL_TYPE = EValType.PARENT_REF;

    @Override
    public EValType getItemValType() {
        return ITEM_VAL_TYPE;
    }

    @Override
    protected String getAsStr(final int i) {
        return SrvValAsStr.toStr(arte, super.get(i), getItemValType());
    }

//override some read methods to encapsulate protection from using discarded Entities
    @Override
    public T get(final int index) {
        T res = super.get(index);

        if (res instanceof LazyRef || (res != null && (res.state.isDiscarded() || res.state.isDeleted()))) {
            final Pid pid = res.getPid();
            res = (T) arte.getEntityObject(pid);
            refine(index, (T) res);
        }

        return (T) res;
    }

    @Override
    public int indexOf(final Object o) {
        if (o == null) {
            for (int i = 0; i < size(); i++) {
                try {
                    if (get(i) == null) {
                        return i;
                    }
                } catch (EntityObjectNotExistsError e) {
                    continue;
                }
            }
        } else if (o instanceof Entity) {
            final EntityState oSt = ((Entity) o).state;
            oSt.assertAccessAllowed();
            oSt.assertNotDeleted();
            for (int i = 0; i < size(); i++) {
                final T ent;
                try {
                    ent = get(i);
                } catch (EntityObjectNotExistsError e) {
                    continue;
                }
                if (o.equals(ent)) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(final Object o) {
        if (o == null) {
            for (int i = size() - 1; i >= 0; i--) {
                try {
                    if (get(i) == null) {
                        return i;
                    }
                } catch (EntityObjectNotExistsError e) {
                    continue;
                }
            }
        } else if (o instanceof Entity) {
            final EntityState oSt = ((Entity) o).state;
            oSt.assertAccessAllowed();
            oSt.assertNotDeleted();
            for (int i = size() - 1; i >= 0; i--) {
                final T ent;
                try {
                    ent = get(i);
                } catch (EntityObjectNotExistsError e) {
                    continue;
                }
                if (o.equals(ent)) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public boolean remove(final Object o) {
        if (o instanceof Entity) {
            ((Entity) o).state.assertAccessAllowed();
        }
        final int i = indexOf(o);
        if (i < 0) {
            return false;
        }
        remove(i);
        return true;
    }

    @Override
    public Object[] toArray() {
        final Object[] arr = new Object[size()];
        for (int i = 0; i < size(); i++) {
            arr[i] = get(i);
        }
        return arr;
    }

    @Override
    public <T> T[] toArray(final T[] arr) {
        for (int i = 0; i < Math.min(size(), arr.length); i++) {
            arr[i] = (T) get(i);
        }
        if (arr.length > size()) // copied from ArrayList.toArray(T[] a);
        {
            arr[size()] = null;
        }
        return arr;
    }

    @Override
    public boolean contains(final Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<T> {

        /**
         * Index of element to be returned by subsequent call to next.
         */
        int cursor = 0;
        /**
         * Index of element returned by most recent call to next or previous.
         * Reset to -1 if this element is deleted by a call to remove.
         */
        int lastRet = -1;
        /**
         * The modCount value that the iterator believes that the backing List
         * should have. If this expectation is violated, the iterator has
         * detected concurrent modification.
         */
        int expectedModCount = modCount;

        @Override
        public boolean hasNext() {
            return cursor != size();
        }

        @Override
        public T next() {
            checkForComodification();
            try {
                T next = get(cursor);
                lastRet = cursor++;
                return next;
            } catch (IndexOutOfBoundsException e) {
                checkForComodification();
                throw new NoSuchElementException();
            } catch (EntityObjectNotExistsError e) {
                lastRet = cursor++;
                throw e;
            }
        }

        @Override
        public void remove() {
            if (lastRet == -1) {
                throw new IllegalStateException();
            }
            checkForComodification();

            try {
                ArrEntity.this.remove(lastRet);
                if (lastRet < cursor) {
                    cursor--;
                }
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }
        }

        final void checkForComodification() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    @Override
    public ListIterator<T> listIterator() {
        return listIterator(0);
    }

    @Override
    public ListIterator<T> listIterator(final int index) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }

        return new ListItr(index);
    }

    private class ListItr extends Itr implements ListIterator<T> {

        ListItr(int index) {
            cursor = index;
        }

        @Override
        public boolean hasPrevious() {
            return cursor != 0;
        }

        @Override
        public T previous() {
            checkForComodification();
            int i = cursor - 1;
            try {
                T previous = get(i);
                lastRet = cursor = i;
                return previous;
            } catch (IndexOutOfBoundsException e) {
                checkForComodification();
                throw new NoSuchElementException();
            } catch (EntityObjectNotExistsError e) {
                lastRet = cursor = i;
                throw e;
            }
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public void set(T e) {
            if (lastRet == -1) {
                throw new IllegalStateException();
            }
            checkForComodification();

            try {
                ArrEntity.this.set(lastRet, e);
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void add(T e) {
            checkForComodification();

            try {
                ArrEntity.this.add(cursor++, e);
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }

    private static class LazyRef extends Entity {

        public LazyRef(Pid pid) {
            super(pid);
        }

        @Override
        public Pid getPid() {
            return pid;
        }

        @Override
        public RadClassDef getRadMeta() {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public boolean equals(Object obj) {
            return (obj instanceof LazyRef) && Objects.equals(((LazyRef) obj).getPid(), getPid());
        }

        @Override
        public int hashCode() {
            return getPid() == null ? 0 : 31 * getPid().hashCode();
        }
    }
}
