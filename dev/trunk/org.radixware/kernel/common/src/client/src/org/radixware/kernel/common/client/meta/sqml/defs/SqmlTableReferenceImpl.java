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

import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.KernelIcon;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableReference;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.types.Id;
import org.xml.sax.Attributes;


abstract class SqmlTableReferenceImpl extends SqmlDefinitionImpl implements ISqmlTableReference {
        
    private final Id ownerTableId;    

    public SqmlTableReferenceImpl(final SqmlModule module, final Attributes attributes, final Id ownerTableId){
        super(module, attributes);        
        this.ownerTableId = ownerTableId;
    }

    @Override
    public ClientIcon getIcon(){
        return KernelIcon.getInstance(DdsDefinitionIcon.REFERENCE);
    }        

    @Override
    public final ISqmlTableDef findReferencedTable() {
        return getEnvironment().getSqmlDefinitions().findTableById(getReferencedTableId());
    }        
    
    protected final Id getOwnerTableId(){
        return ownerTableId;
    }
}
