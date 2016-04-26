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

package org.radixware.kernel.designer.common.dialogs.scmlnb.library;

import org.radixware.kernel.designer.common.dialogs.utils.EditorOpenInfo;
import org.openide.util.Lookup;
import org.openide.util.lookup.ProxyLookup;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.types.Id;


public class ProxyModalOpenInfo extends EditorOpenInfo {

    public ProxyModalOpenInfo(EditorOpenInfo parentInfo, boolean readOnly, Lookup lookup) {
        super(readOnly, new ProxyLookup(lookup, parentInfo.getLookup()));
    }

    public ProxyModalOpenInfo(EditorOpenInfo parentInfo, Lookup lookup) {
        this(parentInfo, parentInfo.isReadOnly(), lookup);
    }
}
