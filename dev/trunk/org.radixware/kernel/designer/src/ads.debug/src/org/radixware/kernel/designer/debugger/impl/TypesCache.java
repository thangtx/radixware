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

package org.radixware.kernel.designer.debugger.impl;

import com.sun.jdi.ReferenceType;
import java.util.Map;
import java.util.WeakHashMap;
import org.radixware.kernel.designer.debugger.RadixDebugger;


public class TypesCache {

    private final RadixDebugger debugger;
    private final Map<ReferenceType, ReferenceTypeWrapper> types = new WeakHashMap<ReferenceType, ReferenceTypeWrapper>();

    public TypesCache(RadixDebugger debugger) {
        this.debugger = debugger;
    }

    public ReferenceTypeWrapper getReferenceType(ReferenceType type) {
        synchronized (types) {
            ReferenceTypeWrapper w = types.get(type);
            if (w == null) {
                w = new ReferenceTypeWrapper(debugger, type);
                types.put(type, w);
            }
            return w;
        }
    }
}
