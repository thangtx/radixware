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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.radixware.kernel.common.utils.PropertyStore;


class GeneralFinder extends SequenceFinder {

    public GeneralFinder(PropertyStore properteis) {
        super(properteis, new GeneralFinderImplFactory());
    }

    @Override
    public Iterator<FindResult> iterator() {
        return new GeneralFinderIterator(this);
    }

    @Override
    public IFinder createFinder(PropertyStore options) {
        return new GeneralFinder(options);
    }

    private static final class GeneralFinderImplFactory implements IFinderImplFactory {

        @Override
        public IFinderImpl createForwardFinder(FindOptions findOptions) {
            if (findOptions.regExpSearch) {
                return new RegExpForwardFinder(findOptions);
            }
            return new SimpleForwardFinder(findOptions);
        }

        @Override
        public IFinderImpl createBackwardFinder(FindOptions findOptions) {
            if (findOptions.regExpSearch) {
                return new RegExpBackwardFinder(findOptions);
            }
            return new SimpleBackwardFinder(findOptions);
        }
    }

    private static abstract class SimpleFinderImpl extends SequenceFinderImpl {

        public SimpleFinderImpl(FindOptions findOptions) {
            super(findOptions);
        }

        boolean compareChars(char ch1, char ch2, boolean matchCase) {
            if (matchCase) {
                return ch1 == ch2;
            }
            return Character.toLowerCase(ch1) == Character.toLowerCase(ch2);
        }

        boolean comparePattern(int start) {
            FindOptions options = getOptions();

            for (int i = 1; i < options.patternLen; ++i) {
                final int seqIndex = start + i;
                if (options.sequenceLen <= seqIndex || !compareChars(options.sequence.charAt(seqIndex), options.pattern.charAt(i), options.matchCase)) {
                    return false;
                }
            }
            return rightFree(start + options.patternLen - 1);
        }
    }

    private static class SimpleForwardFinder extends SimpleFinderImpl {

        public SimpleForwardFinder(FindOptions findOptions) {
            super(findOptions);
        }

        @Override
        public FindResult findFirst(int first, int stop) {
            first = findFirstChar(first, stop);
            while (first != -1) {
                if (comparePattern(first)) {
                    return new FindResult(getOptions().sequence, first, getOptions().patternLen);
                }

                first = findFirstChar(first + 1, stop);
            }
            return FindResult.EMPTY_RESULT;
        }

        private int findFirstChar(int start, int stop) {
            FindOptions options = getOptions();
            for (int i = start; i <= stop - options.patternLen + 1; ++i) {
                if (leftFree(i) && compareChars(options.sequence.charAt(i), options.firstPatternChar, options.matchCase)) {
                    return i;
                }
            }
            return -1;
        }

        @Override
        public FindResult findImpl() {
            int currPosition = getOptions().currPosition;
            FindResult result = findFirst(currPosition, getOptions().last);
            if (!result.isFound() && getOptions().cyclicSearch) {
                result = findFirst(getOptions().first, currPosition);
            }
            return result;
        }
    }

    private static class SimpleBackwardFinder extends SimpleFinderImpl {

        public SimpleBackwardFinder(FindOptions findOptions) {
            super(findOptions);
        }

        @Override
        public FindResult findFirst(int first, int stop) {
            stop = findFirstChar(first, stop);
            while (stop != -1) {
                if (comparePattern(stop)) {
                    return new FindResult(getOptions().sequence, stop, getOptions().patternLen);
                }

                stop = findFirstChar(first, stop + getOptions().patternLen - 2);
            }
            return FindResult.EMPTY_RESULT;
        }

        private int findFirstChar(int start, int stop) {
            for (int i = stop - getOptions().patternLen + 1; i >= start; --i) {
                if (leftFree(i) && compareChars(getOptions().sequence.charAt(i), getOptions().firstPatternChar, getOptions().matchCase)) {
                    return i;
                }
            }
            return -1;
        }

        @Override
        public FindResult findImpl() {
            int currPosition = getOptions().currPosition;
            FindResult result = findFirst(getOptions().first, currPosition);
            if (!result.isFound() && getOptions().cyclicSearch) {
                result = findFirst(currPosition, getOptions().last);
            }
            return result;
        }
    }

    private static abstract class RegExpFinderImpl extends SequenceFinderImpl {

        protected Pattern pattern;
        private static final Pattern EMPTY_PATTERN = Pattern.compile("");

        public RegExpFinderImpl(FindOptions findOptions) {
            super(findOptions);

            int flag = Pattern.MULTILINE;
            if (!getOptions().matchCase) {
                flag = flag | Pattern.CASE_INSENSITIVE;
            }

            try {
                pattern = Pattern.compile(getOptions().pattern.toString(), flag);
            } catch (PatternSyntaxException exception) {
                // if syntax error, set default pattern
                pattern = EMPTY_PATTERN;
            }
        }

        FindResult find(Matcher matcher, int first) {
            assert matcher != null : "Null matcher";

            if (matcher != null && matcher.find()) {
                int start = matcher.start() + first,
                    end = matcher.end() + first;

                if (start < getOptions().sequenceLen) {
                    return new FindResult(getOptions().sequence, start, end - start);
                }
            }
            return FindResult.EMPTY_RESULT;
        }

        Matcher getMatcher(int first, int last) {
            CharSequence sequence = getOptions().sequence;
            if (first >= sequence.length()) {
                return null;
            }
            return pattern.matcher(sequence.subSequence(first, last + 1));
        }
    }

    private static class RegExpForwardFinder extends RegExpFinderImpl {

        public RegExpForwardFinder(FindOptions findOptions) {
            super(findOptions);
        }

        @Override
        public FindResult findImpl() {

            int currPosition = getOptions().currPosition;

            FindResult result = find(getMatcher(currPosition, getOptions().last), currPosition);

            if (!result.isFound() && getOptions().cyclicSearch) {
                int first = getOptions().first;
                result = find(getMatcher(first, currPosition), first);
            }
            return result;
        }

        @Override
        public FindResult findFirst(int first, int last) {
            return find(getMatcher(first, last), first);
        }
    }

    private static class RegExpBackwardFinder extends RegExpFinderImpl {

        public RegExpBackwardFinder(FindOptions findOptions) {
            super(findOptions);
        }

        @Override
        public FindResult findImpl() {

            return FindResult.EMPTY_RESULT;

//            IFindCursor cursor = getCursor();
//            int position = cursor.getPosition();
//
//            FindResult result, curr;
//            Matcher matcher = getMatcher(position, cursor.getLast() + 1);
//
//            do {
//                result = find(matcher, position);
//                if (result.isFound()) {
//                    curr = result;
//                    position = result.last + 1;
//                }
//            } while (result.isFound());
//
//            if (!result.isFound()) {
//                position = cursor.getFirst();
//                result = find(matcher, position);
//            }
//            return result;
        }

        @Override
        public FindResult findFirst(int first, int last) {
            return FindResult.EMPTY_RESULT;
        }
    }

    private static class GeneralFinderIterator extends SequenceFinderIterator {

        private static IFinder createFinder(IFinder source) {
            PropertyStore options = new PropertyStore(source.getOptions());
            options.set(Options.WRAP_AROUND, Boolean.FALSE);
            options.remove(Options.CURRENT_POSITION);

            return new GeneralFinder(options);
        }

        public GeneralFinderIterator(IFinder source) {
            super(createFinder(source));
        }
    }
}
