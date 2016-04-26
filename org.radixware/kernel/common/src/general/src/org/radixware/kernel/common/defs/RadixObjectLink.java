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

public abstract class RadixObjectLink<T extends RadixObject> extends AbstractDefinitionLink {

    protected abstract T search();
    private volatile long supportedChangeVersion = -1;
    private volatile WeakReference<T> ref = null;
    private final Object refUpdateLock = new Object();

    public final T find() {
        if (isDisabled()) {
            return search();
        }
        long globalVersion = RadixObject.globalChangeVersion.longValue();
        final T result;
        boolean needSearch;
        synchronized (refUpdateLock) {
            needSearch = supportedChangeVersion != globalVersion || ref == null;
            if (needSearch) {
                result = search();
                ref = new WeakReference<>(result);
                supportedChangeVersion = globalVersion;
            } else {
                result = ref.get();
            }
        }
        return result;
    }

    public void reset() {
        synchronized (refUpdateLock) {
            ref = null;
        }
    }
}
