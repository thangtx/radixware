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

package org.radixware.kernel.designer.common.general.editors;

import org.openide.util.Lookup;
import org.radixware.kernel.common.defs.RadixObject;


public class OpenInfo implements Lookup.Provider {

    private final RadixObject target;
    private final Lookup lookup;

    public OpenInfo(RadixObject target) {
        this.target = target;
        this.lookup = Lookup.EMPTY;
    }

    public OpenInfo(RadixObject target, Lookup lookup) {
        this.target = target;
        this.lookup = lookup;
    }

    public RadixObject getTarget() {
        return target;
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }
}
