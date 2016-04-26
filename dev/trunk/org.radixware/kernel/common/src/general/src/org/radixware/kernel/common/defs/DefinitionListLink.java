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
import java.util.List;


public abstract class DefinitionListLink<T extends Definition> extends AbstractDefinitionLink {

    private volatile long supportedChangeVersion = -1;

    protected abstract List<T> search();
    private volatile WeakReference<List<T>> ref = null;

    public final List<T> find() {
        if (isDisabled()) {            
            return search();
        }
        final long globalVersion = RadixObject.globalChangeVersion.longValue();

        if (supportedChangeVersion == globalVersion) {
            List<T> result = ref.get();
            if (result == null) {//list was collected
                result = search();
                ref = new WeakReference<>(result);
            }
            return result;
        } else {
            List<T> result = search();
            ref = new WeakReference<>(result);
            supportedChangeVersion = globalVersion;
            return result;
        }
    }
}
