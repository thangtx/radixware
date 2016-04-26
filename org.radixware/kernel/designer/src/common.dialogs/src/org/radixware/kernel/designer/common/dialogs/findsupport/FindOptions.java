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

import org.radixware.kernel.common.utils.PropertyStore;


class FindOptions {

    public final CharSequence sequence;
    public final CharSequence pattern;
    public final int currPosition;
    public final int startPosition;
    public final int stopPosition;
    public final int patternLen;
    public final int sequenceLen;
    public final boolean matchCase;
    public final boolean blockSearch;
    public final boolean wholeWords;
    public final boolean cyclicSearch;
    public final char firstPatternChar;
    public final boolean backSearch;
    public final boolean regExpSearch;
    public final int first;
    public final int last;

    public FindOptions(PropertyStore properteis, IFindCursor cursor) {

        sequence = properteis.get(AbstractFinder.Options.SEQUENCE, "");
        pattern = properteis.get(AbstractFinder.Options.SEARCH_PATTER, "");

        patternLen = pattern.length();
        sequenceLen = sequence.length();

        blockSearch = properteis.get(AbstractFinder.Options.SEARCH_SELECTION, Boolean.FALSE);

        firstPatternChar = patternLen > 0 ? pattern.charAt(0) : '\0';

        startPosition = getInitValidPosition(properteis.get(AbstractFinder.Options.START_POSITION, 0));
        stopPosition = getInitValidPosition(properteis.get(AbstractFinder.Options.STOP_POSITION, sequenceLen - 1));

        matchCase = properteis.get(AbstractFinder.Options.MATCH_CASE, Boolean.FALSE);
        wholeWords = properteis.get(AbstractFinder.Options.WHOLE_WORDS, Boolean.FALSE);

        currPosition = cursor.getPosition();

        cyclicSearch = properteis.get(AbstractFinder.Options.WRAP_AROUND, Boolean.TRUE);

        backSearch = properteis.get(AbstractFinder.Options.BACK_SEARCH, Boolean.FALSE);

        regExpSearch = properteis.get(AbstractFinder.Options.REGEXP_SEARCH, Boolean.FALSE);

        first = getFirst();
        last = getLast();
    }

    private int getInitValidPosition(int pos) {
        return Math.max(Math.min(pos, sequenceLen - 1), 0);
    }

    private int getFirst() {
        if (blockSearch && startPosition >= 0) {
            return startPosition;
        }
        return 0;
    }

    private int getLast() {
        if (blockSearch && stopPosition < sequenceLen) {
            return stopPosition;
        }
        return sequenceLen - 1;
    }

    public final boolean isValid() {
        return patternLen > 0 && sequenceLen > 0 && currPosition < sequenceLen;
    }
}
