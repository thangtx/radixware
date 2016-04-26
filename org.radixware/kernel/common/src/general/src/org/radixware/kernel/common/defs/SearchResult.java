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

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;


public abstract class SearchResult<T> {

    private static final Empty EMPTY = new Empty();

    public static class Empty<D> extends SearchResult<D> {

        private Empty() {
        }

        @Override
        public D get() {
            return null;
        }

        @Override
        public java.util.List<D> all() {
            return Collections.emptyList();
        }

        @Override
        public boolean isMultiple() {
            return false;
        }

        @Override
        public void save(Collection<? super D> collection) {
            //do nothing
        }

        @Override
        public void iterate(Acceptor<D> acceptor) {
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
    }

    public static <T> SearchResult<T> empty() {
        return EMPTY;
    }

    public static <T> SearchResult<T> list(java.util.List<T> list) {
        return new List<>(list);
    }

    public static <T> SearchResult<T> single(T instance) {
        return new Single<>(instance);
    }

    public static class Single<D> extends SearchResult<D> {

        private final D result;

        protected Single(D result) {
            this.result = result;
        }

        @Override
        public D get() {
            return result;
        }

        @Override
        public java.util.List<D> all() {
            return Collections.singletonList(result);
        }

        @Override
        public boolean isMultiple() {
            return false;
        }

        @Override
        public void save(Collection<? super D> collection) {
            if (collection != null && result != null) {
                collection.add(result);
            }
            //do nothing
        }

        @Override
        public void iterate(Acceptor<D> acceptor) {
            acceptor.accept(result);
        }
    }

    public static class List<D> extends SearchResult<D> {

        private final java.util.List<D> result;

        protected List(java.util.List<D> result) {
            this.result = result == null ? Collections.<D>emptyList() : result;
        }

        @Override
        public D get() {
            return result.isEmpty() ? null : result.get(0);
        }

        @Override
        public void iterate(Acceptor<D> acceptor) {
            for (D d : result) {
                acceptor.accept(d);
            }
        }

        @Override
        protected java.util.List<D> getCachedResult() {
            return result;
        }

        @Override
        public boolean isMultiple() {
            return result.size() > 1;
        }

        @Override
        public void save(Collection<? super D> collection) {
            if (collection != null) {
                for (D r : result) {
                    collection.add(r);
                }
            }
        }

        @Override
        public boolean isEmpty() {
            return result.isEmpty();
        }
    }

    public abstract static class Proxy<S, D> extends SearchResult<D> {

        private final SearchResult<S> src;
        private D single;
        private java.util.List<D> all;

        public Proxy(SearchResult<S> src) {
            this.src = src;
        }

        @Override
        public D get() {
            if (single == null) {
                single = convert(src.get());
            }
            return single;
        }

        @Override
        public java.util.List<D> all() {
            if (all == null) {
                all = new LinkedList<>();
                for (S s : src.all()) {
                    D d = convert(s);
                    if (d != null) {
                        all.add(d);
                    }
                }
            }
            return all;
        }

        @Override
        public boolean isMultiple() {
            return src.isMultiple();
        }

        @Override
        public void save(Collection<? super D> collection) {
            for (D d : all()) {
                collection.add(d);
            }
        }

        protected abstract D convert(S src);

        @Override
        public void iterate(final Acceptor<D> acceptor) {
            src.iterate(new Acceptor<S>() {

                @Override
                public void accept(S object) {
                    acceptor.accept(convert(object));
                }
            });
        }
    }

    public abstract T get();

    public boolean isEmpty() {
        return get() == null;
    }

    public interface GetAdvisor<T> {

        public T advise(SearchResult<T> result, T getResult);
    }

    public T get(GetAdvisor<T> advisor) {
        T result = get();
        return advisor == null ? result : advisor.advise(this, result);        
    }

    public java.util.List<T> all() {
        java.util.List<T> result = getCachedResult();
        if (result != null) {
            return result;
        }

        final java.util.List<T> test = new LinkedList<>();
        iterate(new Acceptor<T>() {

            @Override
            public void accept(T object) {
                test.add(object);
            }
        });
        setCachedResult(test);
        return test;
    }

    protected java.util.List<T> getCachedResult() {
        return null;
    }

    protected void setCachedResult(java.util.List<T> list) {
    }

    public interface Acceptor<T> {

        public void accept(T object);
    }

    public abstract void iterate(Acceptor<T> acceptor);

    public abstract boolean isMultiple();

    public void save(final Collection<? super T> collection) {
        iterate(new Acceptor<T>() {

            @Override
            public void accept(T object) {
                collection.add(object);
            }
        });
    }

    public static class CheckForDuplicatesAdvisor<T extends RadixObject> implements SearchResult.GetAdvisor<T> {

        private final RadixObject context;
        private final IProblemHandler problemHandler;

        public CheckForDuplicatesAdvisor(RadixObject context, IProblemHandler problemHandler) {
            this.context = context;
            this.problemHandler = problemHandler;
        }

        private String getName(T getResult) {
            String name;
            if (getResult instanceof RadixObject) {
                name = ((RadixObject) getResult).getQualifiedName();
            } else {
                name = getResult.toString();
            }
            return name;
        }

        @Override
        public T advise(SearchResult<T> result, T getResult) {
            if (result.isMultiple()) {
                StringBuilder builder = new StringBuilder();

                builder.append("Reference to ").append(getName(getResult)).append(" is ambigous. Candidates are:  ");
                boolean first = true;
                for (T t : result.all()) {
                    if (first) {
                        first = false;
                    } else {
                        builder.append(", ");
                    }
                    builder.append(getName(t));
                }
                problemHandler.accept(RadixProblem.Factory.newError(context, builder.toString()));
            }
            return getResult;
        }
    }
}
