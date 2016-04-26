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


public class FindResult {

    public final int first;
    public final int last;
    public final int length;
    public final CharSequence sequence;

    public FindResult(CharSequence sequence, int first, int length) {
        this.first = first;
        this.last = length > 0 ? first + length - 1 : -1;
        this.sequence = sequence;
        this.length = length;
    }

    public boolean isFound() {
        return true;
    }

    @Override
    public String toString() {
        return "'" + sequence.subSequence(first, last + 1).toString() + "' : first = " + first + ", last = " + last + ", len = " + length;
    }

    private static class EmptyResult extends FindResult {

        EmptyResult() {
            super(null, -1, 0);
        }

        @Override
        public boolean isFound() {
            return false;
        }
    }

    public static final FindResult EMPTY_RESULT = new EmptyResult();
}
