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

package org.radixware.kernel.common.utils;

import java.util.ArrayList;

public class CircularBuffer<T> {

    private final ArrayList<T> data;
    private final int maxSize;
    private int head = 0;

    public CircularBuffer(final int maxSize) {
        data = new ArrayList<>(maxSize);
        this.maxSize = maxSize;
    }

    public T get(final int index) {
        rangeCheck(index);
        return data.get(convert(index));
    }

    public void add(final T el) {
        if (data.size() < maxSize) {
            data.add(el);
        } else {
            data.set(head, el);
            head = (head + 1) % maxSize;
        }
    }

    public int size() {
        return data.size();
    }

    public void clear() {
        data.clear();
        head = 0;
    }

    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder("{");
        for (int i = 0; i < size(); i++) {
            if (i > 0) {
                s.append('\'');
            }
            s.append(get(i).toString());
        }
        s.append('}');
        return s.toString();
    }

    private void rangeCheck(int index) {
        if (index >= data.size() || index < 0) {
            throw new IndexOutOfBoundsException(
                    "Index: " + index + ", Size: " + data.size());
        }
    }

    private int convert(int index) {
        return (index + head) % maxSize;
    }
}