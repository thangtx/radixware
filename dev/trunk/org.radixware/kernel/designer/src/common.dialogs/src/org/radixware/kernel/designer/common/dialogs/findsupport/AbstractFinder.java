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

package org.radixware.kernel.designer.common.dialogs.findsupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.radixware.kernel.common.utils.PropertyStore;


public abstract class AbstractFinder implements IFinder {

    private static class EmptyFinder implements IFinder {

        private static class EmptyFinderIterator implements Iterator<FindResult> {

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public FindResult next() {
                return FindResult.EMPTY_RESULT;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported operation.");
            }
        }

        private static class EmptyFindCursor implements IFindCursor {

            @Override
            public CharSequence getSequence() {
                return "";
            }

            @Override
            public void setPosition(int currPos) {
            }

            @Override
            public int getPosition() {
                return -1;
            }

            @Override
            public int getFirst() {
                return -1;
            }

            @Override
            public int getLast() {
                return -1;
            }

            @Override
            public int getValidPosition(int pos) {
                return -1;
            }

            @Override
            public boolean isValid() {
                return false;
            }

            @Override
            public void move(int sh) {
            }

            @Override
            public void reset() {
            }
        }

        private static final Iterator<FindResult> emptyIterator = new EmptyFinderIterator();

        private static final IFindCursor emptyCursor = new EmptyFindCursor();

        private static final PropertyStore properties = new PropertyStore();

        @Override
        public FindResult findNext(boolean move) {
            return FindResult.EMPTY_RESULT;
        }

        @Override
        public FindResult findBack(boolean move) {
            return FindResult.EMPTY_RESULT;
        }

        @Override
        public boolean isValid() {
            return false;
        }

        @Override
        public Iterator<FindResult> iterator() {
            return emptyIterator;
        }

        @Override
        public List<FindResult> findAll() {
            return Collections.<FindResult>emptyList();
        }

        @Override
        public PropertyStore getOptions() {
            return properties;
        }

        @Override
        public FindResult find() {
            return FindResult.EMPTY_RESULT;
        }

        @Override
        public FindResult find(boolean move) {
            return FindResult.EMPTY_RESULT;
        }

        @Override
        public IFindCursor getCursor() {
            return emptyCursor;
        }

        @Override
        public FindResult findFirst(int first, int last) {
            return FindResult.EMPTY_RESULT;
        }

        @Override
        public IFinder createFinder(PropertyStore options) {
            return this;
        }
    }

    public static class Options {

        protected Options() {
        }
        public static final String SEQUENCE = "sequence";
        public static final String SEARCH_PATTER = "search-pattern";
        public static final String START_POSITION = "start-position";
        public static final String STOP_POSITION = "stop-position";
        public static final String CURRENT_POSITION = "current-position";
        public static final String SEARCH_SELECTION = "search-selection";
        public static final String WRAP_AROUND = "wrap-around";
        public static final String MATCH_CASE = "match-case";
        public static final String WHOLE_WORDS = "whole-words";
        public static final String BACK_SEARCH = "back-search";
        public static final String REGEXP_SEARCH = "reg-exp-search";
        public static final String REPLACE_STRING = "replace-string";
        public static final String INCREMENTAL_SEARCH = "incremental-search";
    }

    public static final IFinder EMPTY_FINDER = new EmptyFinder();

    protected final PropertyStore options;

    public AbstractFinder(PropertyStore options) {
        assert options != null;
        this.options = options != null ? new PropertyStore(options) : new PropertyStore();
    }

    @Override
    public FindResult find() {
        return find(true);
    }

    @Override
    public FindResult find(boolean move) {
        boolean backSearch = options.get(Options.BACK_SEARCH, Boolean.FALSE);
        if (backSearch) {
            return findBack(move);
        } else {
            return findNext(move);
        }
    }

    @Override
    public List<FindResult> findAll() {
        Iterator<FindResult> iterator = iterator();
        if (iterator != null) {
            List<FindResult> results = new ArrayList<FindResult>();
            while (iterator.hasNext()) {
                results.add(iterator.next());
            }
            return results;
        }
        return Collections.<FindResult>emptyList();
    }

    @Override
    public final PropertyStore getOptions() {
        return options;
    }
}
