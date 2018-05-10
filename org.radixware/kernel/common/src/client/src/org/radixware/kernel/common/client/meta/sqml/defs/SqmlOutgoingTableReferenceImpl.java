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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.meta.sqml.ISqmlOutgoingReference;
import org.radixware.kernel.common.types.Id;
import org.xml.sax.Attributes;


final class SqmlOutgoingTableReferenceImpl extends SqmlTableReferenceImpl implements ISqmlOutgoingReference{
    
    private final List<String> childColumnNames;
    private final Id referencedTableId;
    
    public SqmlOutgoingTableReferenceImpl(final SqmlModule module, final Attributes attributes, final Id ownerTableId){
        super(module, attributes, ownerTableId);
        this.referencedTableId = Id.Factory.loadFrom(attributes.getValue("ReferencedTableId"));
        final String arrColumnNamesAsStr = attributes.getValue("ChildColumnNames");
        if (arrColumnNamesAsStr==null || arrColumnNamesAsStr.isEmpty()){
            childColumnNames = Collections.emptyList();
        }else{
            final String[] arrColumnNames = arrColumnNamesAsStr.split(" ");
            childColumnNames = new LinkedList<>();
            for (String columnName: arrColumnNames){
                if (columnName!=null && !columnName.trim().isEmpty()){
                    childColumnNames.add(columnName.trim());
                }
            }
        }
    }
    
    @Override
    public Id getReferencedTableId() {
        return referencedTableId;
    }    

    @Override
    public List<String> getChildColumnNames() {
        return Collections.unmodifiableList(childColumnNames);
    }        
}
