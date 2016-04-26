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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.RadixObject;


public class FilteredSet<T extends RadixObject> implements Set<T> {

    private final Set<T> internal = new HashSet<T>();
    private final IFilter<T> filter;

    public FilteredSet(IFilter<T> filter) {
        this.filter = filter;
    }

    @Override
    public int size() {
        return internal.size();
    }

    @Override
    public boolean isEmpty() {
        return internal.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return internal.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return internal.iterator();
    }

    @Override
    public Object[] toArray() {
        return internal.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return internal.toArray(a);
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

    @Override
    public boolean containsAll(Collection<?> c) {
        return internal.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean modified = false;
        for (T e : c) {
            if (filter.isTarget(e)) {
                modified = internal.add(e);
            }
        }
        return modified;
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
    public void clear() {
        internal.clear();
    }
}
