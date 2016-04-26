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

package org.radixware.kernel.common.defs.dds.providers;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.DdsDefinitions;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.dds.DdsSegment;

/**
 * Top DDS Definitions in DDS model.
 */
public class DdsModelItemVisitorProvider extends DdsVisitorProvider {

    @Override
    public boolean isTarget(RadixObject radixObject) {
        return true;

    }

    @Override
    public boolean isContainer(RadixObject object) {
        return (object instanceof Branch) ||
                (object instanceof Layer) ||
                (object instanceof DdsSegment) ||
                (object instanceof DdsModule) ||
                (object instanceof DdsModelDef) ||
                (object instanceof DdsDefinitions);
    }
}
