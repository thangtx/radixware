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


public abstract class HierarchyIterator<T extends RadixObject> implements Iterator<HierarchyIterator.Chain<T>> {

    public static abstract class Chain<T extends RadixObject> implements Iterable<T> {

        public static <T extends RadixObject> Chain<T> newInstance(T single) {
            if (single == null) {
                return EMPTY;
            }
            return new SingleChain<>(single);
        }

        public static <T extends RadixObject> Chain<T> newInstance(List<T> list) {
            if (list == null || list.isEmpty()) {
                return EMPTY;
            }
            return list.size() == 1 ? new SingleChain<>(list.get(0)) : new ListChain<>(list);
        }

        public static <T extends RadixObject> Chain<T> empty() {
            return EMPTY;
        }
        private static final Chain EMPTY = new SingleChain((RadixObject) null);

        private static class SingleChain<T extends RadixObject> extends Chain<T> {

            private final T single;

            public SingleChain(T single) {
                this.single = single;
            }

//            @Override
//            public List<T> list() {
//                return single == null ? Collections.<T>emptyList() : Collections.<T>singletonList(single);
//            }

            @Override
            public boolean isEmpty() {
                return single == null;
            }

            @Override
            public int size() {
                return single == null ? 0 : 1;
            }

            @Override
            public Iterator<T> iterator() {
                return new Iterator<T>() {

                    private boolean hasNext = single != null;

                    @Override
                    public boolean hasNext() {
                        return hasNext;
                    }

                    @Override
                    public T next() {
                        if (hasNext) {
                            hasNext = false;
                            return single;
                        } else {
                            return null;
                        }
                    }

                    @Override
                    public void remove() {
                    }
                };
            }

            @Override
            public T first() {
                return single;
            }

            @Override
            public T second() {
                return null;
            }

            @Override
            public T third() {
                return null;
            }

            @Override
            public T fourth() {
                return null;
            }
        }

        private static class ListChain<T extends RadixObject> extends Chain<T> {

            private final List<T> list;

            public ListChain(List<T> list) {
                if (list.size() == 1) {

                    this.list = null;
                } else {
                    this.list = new ArrayList<>(list);

                }
            }
//
//            @Override
//            public List<T> list() {
//                return list;
//            }

            @Override
            public boolean isEmpty() {
                return list.isEmpty();
            }

            @Override
            public int size() {
                return list.size();
            }

            @Override
            public Iterator<T> iterator() {
                return list.iterator();
            }

            @Override
            public T first() {
                return list.isEmpty() ? null : list.get(0);
            }

            @Override
            public T second() {
                return list.size() < 2 ? null : list.get(1);
            }

            @Override
            public T third() {
                return list.size() < 3 ? null : list.get(2);
            }

            @Override
            public T fourth() {
                return list.size() < 4 ? null : list.get(3);
            }

            public static <T extends RadixObject> Chain<T> empty() {
                return EMPTY;
            }
        }

       // public abstract List<T> list();

        public abstract boolean isEmpty();

        public abstract int size();

        @Override
        public abstract Iterator<T> iterator();

        public abstract T first();

        public abstract T second();

        public abstract T third();

        public abstract T fourth();
    }

    public enum Mode {

        FIND_FIRST,
        FIND_ALL
    }
    protected final Mode mode;

    public HierarchyIterator(Mode mode) {
        this.mode = mode;
    }

    @Override
    public final void remove() {
        throw new UnsupportedOperationException("Remove not supported on virtual collection iterator");
    }
//    public void checkState(T obj) {
//        T cur = next();
//        if (cur != obj) {
//            throw new RadixError("Invalid iterator state: " + String.valueOf(obj) + " expected but " + String.valueOf(cur) + " found");
//        }
//    }
}
