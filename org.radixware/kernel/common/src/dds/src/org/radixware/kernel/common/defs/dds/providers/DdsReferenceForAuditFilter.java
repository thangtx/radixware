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

import java.util.HashSet;
import java.util.Set;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;


class DdsReferenceForAuditFilter implements IFilter<DdsReferenceDef> {

    final static private Id DBA_SCHEME_ID = Id.Factory.loadFrom("tblJBWGL34ATTNBDPK5ABQJO5ADDQ"); // Radix::DBA::Scheme

    @Override
    public boolean isTarget(final DdsReferenceDef reference) {
        final Set<Id> processedTableIds = new HashSet<Id>();
        final Set<DdsReferenceDef> refs = new HashSet<DdsReferenceDef>();
        refs.add(reference);

        while (!refs.isEmpty()) {
            final DdsReferenceDef ref = refs.iterator().next();
            final DdsTableDef parentTable = ref.findParentTable(ref);
            if (parentTable != null && !processedTableIds.contains(parentTable.getId())) {
                if (Utils.equals(parentTable.getId(), DBA_SCHEME_ID)) {
                    return true;
                }
                processedTableIds.add(parentTable.getId());
                final Set<DdsReferenceDef> nextRefs = parentTable.collectOutgoingReferences();
                refs.addAll(nextRefs);
            }
            refs.remove(ref);
        }
        return false;
    }
}
