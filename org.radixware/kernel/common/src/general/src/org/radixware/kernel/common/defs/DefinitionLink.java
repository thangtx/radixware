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


public abstract class DefinitionLink<T extends Definition> extends AbstractDefinitionLink {

    protected abstract T search();
    private volatile long supportedChangeVersion = -1;
    private volatile WeakReference<T> ref = null;

    public final T find() {
        if (isDisabled()) {
            return search();
        }
        long globalVersion = RadixObject.globalChangeVersion.longValue();
        final T result;
        if (supportedChangeVersion == globalVersion) {
            result = ref.get();
        } else {
            result = search();
            ref = new WeakReference<>(result);
            supportedChangeVersion = globalVersion;
        }
        return result;
    }
}
