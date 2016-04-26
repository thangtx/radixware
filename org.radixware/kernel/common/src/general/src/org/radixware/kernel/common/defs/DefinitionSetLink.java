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

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;


public abstract class DefinitionSetLink<T extends Definition> {

    protected abstract void search(Set<T> set);
    private volatile long supportedChangeVersion = -1;
    private static volatile boolean disabled = false;
    private volatile WeakReference<Set<T>> ref = null;

    public static void disable(boolean disable) {
        if (disable != disabled) {
            disabled = disable;
            RadixObject.globalChangeVersion.incrementAndGet();
        }
    }

    public final Set<T> find() {
        if (disabled) {
            Set<T> set = new HashSet<>();
            search(set);
            return set;
        }
        long globalVersion = RadixObject.globalChangeVersion.longValue();
        final Set<T> result;
        if (supportedChangeVersion == globalVersion) {
            result = ref.get();
        } else {
            result = new HashSet<>();
            search(result);
            ref = new WeakReference<>(result);
            supportedChangeVersion = globalVersion;
        }
        return result;
    }
}
