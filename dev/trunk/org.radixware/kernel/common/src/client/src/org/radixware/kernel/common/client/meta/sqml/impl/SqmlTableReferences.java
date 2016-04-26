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

package org.radixware.kernel.common.client.meta.sqml.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableReferences;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableReference;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.types.Id;


final class SqmlTableReferences implements ISqmlTableReferences {

    private final List<ISqmlTableReference> references = new ArrayList<>(16);

    public SqmlTableReferences(final IClientEnvironment environment, final DdsTableDef ddsTable, final List<ISqmlTableReferences> referencesFromDetailTables) {
        Collection<DdsReferenceDef> refDefs;
        if (referencesFromDetailTables==null || referencesFromDetailTables.isEmpty()){
            final IFilter<DdsReferenceDef> detailRefsFilter = new IFilter<DdsReferenceDef>(){
                @Override
                public boolean isTarget(DdsReferenceDef referenceDef) {
                    return referenceDef.getType()== DdsReferenceDef.EType.MASTER_DETAIL;
                }
            };
            refDefs = ddsTable.collectIncomingReferences(detailRefsFilter);
            for (DdsReferenceDef ddsReference : refDefs) {
                references.add(new SqmlDetailTableReferenceImpl(environment, ddsReference));
            }
        }
        refDefs = ddsTable.collectOutgoingReferences();
        final DdsReferenceDef masterReference = ddsTable.findMasterReference();        
        final Id masterTableId = masterReference==null ? null : masterReference.getParentTableId();
        for (DdsReferenceDef ddsReference : refDefs) {
            if (masterTableId==null || !masterTableId.equals(ddsReference.getParentTableId())){
                references.add(new SqmlOutgoingReferenceImpl(environment, ddsReference));
            }
        }
        for (ISqmlTableReferences detailReferences: referencesFromDetailTables){
            for (ISqmlTableReference reference: detailReferences){
                references.add(reference);
            }
        }
    }

    @Override
    public ISqmlTableReference getReferenceById(final Id referenceId) {
        for (ISqmlTableReference reference : references) {
            if (reference.getId().equals(referenceId)) {
                return reference;
            }
        }
        return null;
    }

    @Override
    public Iterator<ISqmlTableReference> iterator() {
        return references.iterator();
    }

    @Override
    public int size() {
        return references.size();
    }

    @Override
    public ISqmlTableReference get(final int idx) {
        return references.get(idx);
    }
}