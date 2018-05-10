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

package org.radixware.kernel.common.build.xbeans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListView implements List {

    TypeMapper mapper;
    List view;

    class ViewIterator implements ListIterator {

        ListIterator iter;

        public ViewIterator() {
            iter = view.listIterator();
        }

        public ViewIterator(int index) {
            iter = view.listIterator(index);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void add(Object o) {
            iter.add(mapper.convertTo(o));
        }

        @Override
        public boolean hasNext() {
            return iter.hasNext();
        }

        @Override
        public boolean hasPrevious() {
            return iter.hasPrevious();
        }

        @Override
        public Object next() {
            return mapper.convertFrom(iter.next());
        }

        @Override
        public int nextIndex() {
            return iter.nextIndex();
        }

        @Override
        public Object previous() {
            return mapper.convertFrom(iter.previous());
        }

        @Override
        public int previousIndex() {
            return iter.previousIndex();
        }

        @Override
        public void remove() {
            iter.remove();
        }

        @SuppressWarnings("unchecked")
        @Override
        public void set(Object o) {
            iter.set(mapper.convertTo(o));
        }
    }

    ListView(TypeMapper mapper, List view) {
        this.mapper = mapper;
        this.view = view;
    }

    private Collection createView(Collection c) {
        ListView lv = new ListView(mapper, new ArrayList());
        lv.addAll(c);
        return lv;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean add(Object o) {
        return view.add(mapper.convertTo(o));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void add(int index, Object element) {
        view.add(index, mapper.convertTo(element));
    }

    @Override
    public boolean addAll(Collection c) {
        boolean changed = false;
        for (Object o : c) {
            if (add(mapper.convertTo(o))) {
                changed = true;
            }
        }
        return changed;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean addAll(int index, Collection c) {
        return view.addAll(index, createView(c));
    }

    @Override
    public void clear() {
        view.clear();
    }

    @Override
    public boolean contains(Object o) {
        return view.contains(mapper.convertTo(o));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean containsAll(Collection c) {

        return view.containsAll(createView(c));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Iterable) {
            Iterator other = ((Iterable) o).iterator();
            Iterator self = iterator();
            Object otherItem;
            Object selfItem;
            do { 
                if(self.hasNext() && !other.hasNext() || !self.hasNext() && other.hasNext()) {
                    return false;
                } else if (!self.hasNext() && !other.hasNext()) {
                    return true;
                }
                otherItem = mapper.convertTo(other.next());
                selfItem = self.next();
                if (selfItem != null) {
                    if (otherItem == null || !selfItem.equals(otherItem)) {
                        return false;
                    }
                } else if (otherItem != null) {
                    return false;
                }
            } while (otherItem != null && selfItem != null);
            return true;
        }
        return false;
    }

    @Override
    public Object get(int index) {
        return mapper.convertFrom(view.get(index));
    }

    @Override
    public int hashCode() {
        return view.hashCode();
    }

    @Override
    public int indexOf(Object o) {
        return view.indexOf(mapper.convertTo(o));
    }

    @Override
    public boolean isEmpty() {
        return view.isEmpty();
    }

    @Override
    public Iterator iterator() {
        return new ViewIterator();
    }

    @Override
    public int lastIndexOf(Object o) {
        return view.lastIndexOf(o);
    }

    @Override
    public ListIterator listIterator() {
        return new ViewIterator();
    }

    @Override
    public ListIterator listIterator(int index) {
        return new ViewIterator(index);
    }

    @Override
    public boolean remove(Object o) {
        return view.remove(o);
    }

    @Override
    public Object remove(int index) {
        return mapper.convertFrom(view.remove(index));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean removeAll(Collection c) {
        return view.removeAll(createView(c));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean retainAll(Collection c) {
        return view.retainAll(createView(c));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object set(int index, Object element) {
        return view.set(index, mapper.convertTo(element));
    }

    @Override
    public int size() {
        return view.size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List subList(int fromIndex, int toIndex) {
        List l = new ArrayList();
        for (Object item : view.subList(fromIndex, toIndex)) {
            l.add(mapper.convertFrom(item));
        }
        return l;
    }

    @Override
    public Object[] toArray() {
        Object[] ret = new Object[size()];
        int index = 0;
        for (Object o : this) {
            ret[index++] = o;
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object[] toArray(Object[] a) {
        Object[] ret;
        if (a.length < size()) {
            ret = new Object[size()];
        } else {
            ret = a;
        }
        int index = 0;
        for (Object o : this) {
            ret[index++] = o;
        }
        return ret;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append('[');
        String sep = "";
        for (Object o : view) {
            b.append(sep).append(mapper.convertFrom(o));
            sep = ", ";
        }
        b.append(']');
        return b.toString();
    }
}
