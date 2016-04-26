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

package org.radixware.kernel.common.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.RadixObject;


public class FilteredList<T extends RadixObject> implements List<T> {

    private final IFilter<T> filter;
    private final List<T> internal;

    public FilteredList(IFilter<T> filter) {
        this(new LinkedList<T>(), filter);
    }

    public FilteredList(List<T> internal, IFilter<T> filter) {
        this.internal = internal;
        this.filter = filter;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return internal.toArray(a);
    }

    @Override
    public Object[] toArray() {
        return internal.toArray();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return internal.subList(fromIndex, toIndex);
    }

    @Override
    public int size() {
        return internal.size();
    }

    @Override
    public T set(int index, T element) {
        if (filter.isTarget(element)) {
            return internal.set(index, element);
        } else {
            return internal.get(index);
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return internal.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return internal.removeAll(c);
    }

    @Override
    public T remove(int index) {
        return internal.remove(index);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return internal.listIterator(index);
    }

    @Override
    public ListIterator<T> listIterator() {
        return internal.listIterator();
    }

    @Override
    public int lastIndexOf(Object o) {
        return internal.lastIndexOf(o);
    }

    @Override
    public Iterator<T> iterator() {
        return internal.iterator();
    }

    @Override
    public boolean isEmpty() {
        return internal.isEmpty();
    }

    @Override
    public int indexOf(Object o) {
        return internal.indexOf(o);
    }

    @Override
    public int hashCode() {
        return internal.hashCode();
    }

    @Override
    public T get(int index) {
        return internal.get(index);
    }

    @Override
    public boolean equals(Object o) {
        return internal.equals(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return internal.containsAll(c);
    }

    @Override
    public boolean contains(Object o) {
        return internal.contains(o);
    }

    @Override
    public void clear() {
        internal.clear();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        Iterator<? extends T> iter = c.iterator();
        boolean added = false;
        int theindex = index;
        while (iter.hasNext()) {
            T e = iter.next();
            if (filter.isTarget(e)) {
                internal.add(theindex, e);
                theindex++;
                added = true;
            }
        }
        return added;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        Iterator<? extends T> iter = c.iterator();
        boolean added = false;
        while (iter.hasNext()) {
            T e = iter.next();
            if (filter.isTarget(e)) {
                internal.add(e);
                added = true;
            }
        }
        return added;
    }

    @Override
    public void add(int index, T element) {
        if (filter.isTarget(element)) {
            internal.add(index, element);
        }
    }

    @Override
    public boolean add(T e) {
        if (filter.isTarget(e)) {
            return internal.add(e);
        } else {
            return false;
        }
    }

    @Override
    public boolean remove(Object o) {
        return internal.remove(o);
    }
}
