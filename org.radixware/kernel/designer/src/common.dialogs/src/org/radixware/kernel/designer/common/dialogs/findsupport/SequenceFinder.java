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

import java.util.Iterator;
import org.radixware.kernel.common.utils.PropertyStore;


abstract class SequenceFinder extends AbstractFinder {

    private final FindCursor findCursor;
    private final IFinderImplFactory finderImplFactory;

    public SequenceFinder(PropertyStore options, IFinderImplFactory finderImplFactory) {
        super(options);

        findCursor = new FindCursor(getOptions());

        assert finderImplFactory != null;

        this.finderImplFactory = finderImplFactory;
    }

    @Override
    public final FindResult findNext(boolean move) {
        if (!isValid() || getCursor().isComplete()) {
            return FindResult.EMPTY_RESULT;
        }

        FindResult result = createForwardFinderImpl().find();
        if (result.isFound() && move) {
            getCursor().setPosition(result.last + 1);
        }

        return result;
    }

    @Override
    public final FindResult findBack(boolean move) {
        if (!isValid() || getCursor().isComplete()) {
            return FindResult.EMPTY_RESULT;
        }

        FindResult result = createBackwardFinderImpl().find();

        if (result.isFound() && move) {
            getCursor().setPosition(result.first - 1);
        }

        return result;
    }

    @Override
    public boolean isValid() {
        if (options != null && findCursor.isValid()) {
            String serachPattern = options.get(Options.SEARCH_PATTER, String.class);
            return serachPattern != null && !serachPattern.isEmpty();
        }
        return false;
    }

    @Override
    public final FindCursor getCursor() {
        return findCursor;
    }

    @Override
    public FindResult findFirst(int first, int last) {
        if (!isValid()) {
            return FindResult.EMPTY_RESULT;
        }
        return createActiveFinderImpl().findFirst(first, last);
    }

    private IFinderImpl createForwardFinderImpl() {
        return finderImplFactory.createForwardFinder(new FindOptions(options, findCursor));
    }

    private IFinderImpl createBackwardFinderImpl() {
        return finderImplFactory.createBackwardFinder(new FindOptions(options, findCursor));
    }

    protected final IFinderImpl createActiveFinderImpl() {
        if (options.get(Options.BACK_SEARCH, Boolean.FALSE)) {
            return createBackwardFinderImpl();
        } else {
            return createForwardFinderImpl();
        }
    }

    protected interface IFinderImplFactory {

        IFinderImpl createForwardFinder(FindOptions findOptions);

        IFinderImpl createBackwardFinder(FindOptions findOptions);
    }

    protected static abstract class SequenceFinderImpl implements IFinderImpl {

        private FindOptions findOptions;

        public SequenceFinderImpl(FindOptions findOptions) {
            this.findOptions = findOptions;
        }

        @Override
        public final FindResult find() {
            if (!findOptions.isValid()) {
                return FindResult.EMPTY_RESULT;
            }
            return findImpl();
        }

        protected boolean leftFree(int pos) {
            if (!getOptions().wholeWords) {
                return true;
            }

            if (getOptions().sequenceLen == 0 || pos == 0) {
                return true;
            }
            char leftChar = getOptions().sequence.charAt(pos - 1);
            return !Character.isJavaIdentifierPart(leftChar);
        }

        protected boolean rightFree(int pos) {
            if (!getOptions().wholeWords) {
                return true;
            }

            if (getOptions().sequenceLen == 0 || pos == getOptions().sequenceLen - 1) {
                return true;
            }
            char rightChar = getOptions().sequence.charAt(pos + 1);
            return !Character.isJavaIdentifierPart(rightChar);
        }

        public FindOptions getOptions() {
            return findOptions;
        }

        protected abstract FindResult findImpl();
    }

    protected static abstract class SequenceFinderIterator implements Iterator<FindResult> {

        protected final IFinder finder;
        protected FindResult next;

        public SequenceFinderIterator(IFinder finder) {
            this.finder = finder;
            next = finder.find();
        }

        @Override
        public boolean hasNext() {
            return next.isFound();
        }

        @Override
        public FindResult next() {
            FindResult current = next;

            next = finder.find();

            return current;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove operation not supported.");
        }
    }
}
