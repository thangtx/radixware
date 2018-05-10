/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.common.client.meta.sqml.defs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDetailTableReference;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableReference;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableReferences;
import org.radixware.kernel.common.types.Id;


final class SqmlTableReferences implements ISqmlTableReferences{
    
    private final List<ISqmlTableReference> references = new ArrayList<>();
    
    public SqmlTableReferences(final List<ISqmlDetailTableReference> detailReferences,
                                               final List<SqmlOutgoingTableReferenceImpl> outgouingReferences){
        for (ISqmlDetailTableReference ref: detailReferences){
            references.add(ref);
        }
        for (SqmlOutgoingTableReferenceImpl ref: outgouingReferences){
            references.add(ref);
        }
    }
    
    public SqmlTableReferences(final ISqmlTableReferences tableReferences, final List<ISqmlTableReferences> referencesFromDetailTables){
        final boolean hasReferencesFromDetails = referencesFromDetailTables!=null && !referencesFromDetailTables.isEmpty();
        if (tableReferences!=null){
            for (ISqmlTableReference reference: tableReferences){
                if (!hasReferencesFromDetails || reference instanceof SqmlOutgoingTableReferenceImpl){
                    references.add(reference);
                }
            }
        }
        if (hasReferencesFromDetails){
            for (ISqmlTableReferences refs: referencesFromDetailTables){
                for (ISqmlTableReference reference: refs){
                    references.add(reference);
                }
            }
        }
    }

    @Override
    public int size() {
        return references.size();
    }

    @Override
    public ISqmlTableReference getReferenceById(final Id referenceId) {
        for (ISqmlTableReference reference: references){
            if (referenceId.equals(reference.getId())){
                return reference;
            }
        }
        return null;
    }

    @Override
    public ISqmlTableReference get(final int idx) {
        return references.get(idx);
    }

    @Override
    public Iterator<ISqmlTableReference> iterator() {
        return references.iterator();
    }

}
