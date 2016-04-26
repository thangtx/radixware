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


class FindCursor implements IFindCursor {

    private int currPosition = 0;
    private final PropertyStore options;

    public FindCursor(PropertyStore options) {
        this.options = options;

        assert options != null : "Empty find options";

        reset();
    }

    @Override
    public final void setPosition(int currPos) {
        if (options.get(AbstractFinder.Options.WRAP_AROUND, Boolean.TRUE)) {
            if (currPos > getLast()) {
                this.currPosition = getFirst();
            } else if (currPos < getFirst()) {
                this.currPosition = getLast();
            } else {
                this.currPosition = currPos;
            }
        } else {
            this.currPosition = getValidPosition(currPos);
        }
    }

    @Override
    public final int getPosition() {
        return getValidPosition(currPosition);
    }

    @Override
    public CharSequence getSequence() {
        return options.get(AbstractFinder.Options.SEQUENCE, "");
    }

    @Override
    public final int getFirst() {
        final boolean blockSearch = options.get(AbstractFinder.Options.SEARCH_SELECTION, Boolean.FALSE);
        final int startPosition = options.get(AbstractFinder.Options.START_POSITION, 0);

        if (blockSearch) {
            return getAvaliablePosition(startPosition);
        }
        return 0;
    }

    @Override
    public final int getLast() {
        final boolean blockSearch = options.get(AbstractFinder.Options.SEARCH_SELECTION, Boolean.FALSE);
        final int stopPosition = options.get(AbstractFinder.Options.STOP_POSITION, getSequence().length() - 1);

        if (blockSearch) {
            return getAvaliablePosition(stopPosition);
        }
        return getSequence().length() - 1;
    }

    private int getAvaliablePosition(int pos) {
        return Math.max(Math.min(pos, getSequence().length() - 1), 0);
    }

    @Override
    public final int getValidPosition(int pos) {
        return Math.max(Math.min(pos, maxPos()), minPos());
    }

    @Override
    public boolean isValid() {
        return getSequence().length() > 0;
    }

    @Override
    public void move(int sh) {
        setPosition(currPosition + sh);
    }

    @Override
    public final void reset() {

        final Integer currPos = options.get(AbstractFinder.Options.CURRENT_POSITION, Integer.class);
        if (currPos == null) {
            if (options.get(AbstractFinder.Options.BACK_SEARCH, Boolean.FALSE)) {
                setPosition(getLast());
            } else {
                setPosition(getFirst());
            }
        } else {
            setPosition(getValidPosition(currPos.intValue()));
        }
    }

    private int minPos() {
        if (options.get(AbstractFinder.Options.WRAP_AROUND, Boolean.TRUE)
            || !options.get(AbstractFinder.Options.BACK_SEARCH, Boolean.FALSE)) {
            return getFirst();
        }
        return getFirst() - 1;
    }

    private int maxPos() {
        if (options.get(AbstractFinder.Options.WRAP_AROUND, Boolean.TRUE)
            || options.get(AbstractFinder.Options.BACK_SEARCH, Boolean.FALSE)) {
            return getLast();
        }
        return getLast() + 1;
    }

    public boolean isComplete() {
        if (options.get(AbstractFinder.Options.WRAP_AROUND, Boolean.TRUE)) {
            return false;
        }

        if (options.get(AbstractFinder.Options.BACK_SEARCH, Boolean.FALSE)) {
            return getPosition() < getFirst();
        }

        return getPosition() > getLast();
    }
}
