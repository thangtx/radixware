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

import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.DdsAccessPartitionFamilyDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.types.Id;


class DdsApfParentReferenceProvider extends DdsReferenceProvider {

    private final DdsAccessPartitionFamilyDef apf;
    private final List<Id> correctParentTableIds;

    public DdsApfParentReferenceProvider(DdsAccessPartitionFamilyDef apf, List<Id> correctParentTableIds) {
        this.apf = apf;
        this.correctParentTableIds = correctParentTableIds;
    }

    @Override
    public boolean isTarget(RadixObject object) { // for each DdsReferenceDef
        if (!super.isTarget(object)) {
            return false;
        }

        final Definition head = apf.findHead();
        if (!(head instanceof DdsTableDef)) {
            return false;
        }

        DdsReferenceDef ref = (DdsReferenceDef) object;

        final DdsTableDef headAsTable = (DdsTableDef) head; // child table
        if (!correctParentTableIds.contains(ref.getParentTableId())) {
            return false;
        }

        final Set<DdsReferenceDef> outgoingReferences = headAsTable.collectOutgoingReferences();
        return outgoingReferences.contains(object);
    }
}
