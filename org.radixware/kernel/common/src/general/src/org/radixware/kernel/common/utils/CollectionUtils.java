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

import java.util.Collection;
import java.util.List;


public class CollectionUtils {

    public static <T> T getLast(Collection<T> collection) {
        if (collection == null || collection.isEmpty()) {
            return null;
        }

        T last = null;
        for (final T item : collection) {
            last = item;
        }
        return last;
    }

    public static <T> T getLast(List<T> collection) {
        if (collection == null || collection.isEmpty()) {
            return null;
        }
        return collection.get(collection.size() - 1);
    }

    public static <T> T getLast(T[] array) {
        if (array == null || array.length == 0) {
            return null;
        }
        return array[array.length - 1];
    }
}
