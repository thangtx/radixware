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

import org.radixware.kernel.common.client.meta.sqml.ISqmlDetailTableReference;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.types.Id;
import org.xml.sax.Attributes;


final class SqmlDetailTableReferenceImpl extends SqmlTableReferenceImpl implements ISqmlDetailTableReference{
    
    public SqmlDetailTableReferenceImpl(final SqmlModule module, 
                                                            final Attributes attributes, 
                                                            final Id ownerTableId){
        super(module, attributes, ownerTableId);
    }

    @Override
    public String getFullName() {                
        return getModuleName()+"::"+getShortName();
    }

    @Override
    public Id getReferencedTableId() {
        return getOwnerTableId();
    }

    @Override
    public String getModuleName() {
        final ISqmlTableDef table = findReferencedTable();
        return table==null ? super.getModuleName() : table.getModuleName();        
    }
            
    @Override
    public String getTitle() {
        final ISqmlTableDef table = findReferencedTable();
        if (table==null){
            return getShortName();
        }else{
            final String template = getEnvironment().getMessageProvider().translate("SqmlEditor", "%1$s details");
            return String.format(template, table.getTitle());
        }
    }
    
}
