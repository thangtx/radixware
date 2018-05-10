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
package org.radixware.kernel.server.types;

import java.util.Iterator;

/**
 *
 * @author npopov
 */
public abstract class AbstractEntityCursorIterator<T extends Entity> implements Iterator<T>, AutoCloseable {

    private final Cursor cursor;
    private Boolean hasNext;

    public AbstractEntityCursorIterator(final Cursor cursor) {
        this.cursor = cursor;
    }

    abstract public T getEntity(Cursor cursor);

    @Override
    public boolean hasNext() {
        if (hasNext == null) {
            hasNext = cursor.next();
        }
        return hasNext;
    }

    @Override
    public T next() {
        if (hasNext()) {
            hasNext = null;
            return getEntity(cursor);
        }
        return null;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported for this type of cursor.");
    }

    @Override
    public void close() {
        cursor.close();
    }

    public static void closeIterator(Iterator<?> iter) {
        if (iter instanceof AbstractEntityCursorIterator) {
            ((AbstractEntityCursorIterator) iter).close();
        }
    }

}
