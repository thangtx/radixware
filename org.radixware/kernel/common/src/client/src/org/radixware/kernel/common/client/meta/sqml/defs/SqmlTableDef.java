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

import java.util.List;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.KernelIcon;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDetailTableReference;
import org.radixware.kernel.common.client.meta.sqml.ISqmlSelectorPresentations;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableColumns;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableIndices;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableReferences;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.types.Id;
import org.xml.sax.Attributes;


final class SqmlTableDef extends SqmlDefinitionImpl implements ISqmlTableDef{
    
    private final Id masterTableId;
    private final Id masterReferenceId;
    private final ISqmlTableIndices indices;
    private final ISqmlTableColumns columns;
    private final List<SqmlOutgoingTableReferenceImpl> outgouingReferences;
    private final SqmlRepository repository;
    private ISqmlTableReferences references;
    
    public SqmlTableDef(final SqmlModule module,            
                                   final Attributes attributes,
                                   final SqmlTableIndices indices,
                                   final Id masterReferenceId,
                                   final List<SqmlOutgoingTableReferenceImpl> outgouingReferences,
                                   final SqmlTableColumns columns,
                                   final SqmlRepository repository){
        super(module,attributes);
        masterTableId = SqmlDefinitionImpl.parseOptionalId(attributes, "MasterTableId");
        this.masterReferenceId = masterReferenceId;
        this.outgouingReferences = outgouingReferences;
        this.indices = indices;
        this.columns = columns;
        for (ISqmlColumnDef column: columns){
            if (column instanceof SqmlTableColumnDef){
                ((SqmlTableColumnDef)column).ownerTable = this;
            }
        }
        this.repository = repository;
    }
        
    public Id getMasterTableId(){
        return masterTableId;
    }
    
    public Id getMasterReferenceId(){
        return masterReferenceId;
    }
    
    @Override
    public ClientIcon getIcon() {
        return KernelIcon.getInstance(DdsDefinitionIcon.TABLE);
    }

    @Override
    public Id getTableId() {
        return getId();
    }

    @Override
    public ISqmlTableColumns getColumns() {
        return columns;
    }

    @Override
    public ISqmlTableReferences getReferences() {
        if (references==null){
            final List<ISqmlDetailTableReference> detailReferences = repository.getDetailReferencesForMasterTable(getId());
            references = new SqmlTableReferences(detailReferences, outgouingReferences);
        }
        return references;
    }

    @Override
    public ISqmlTableIndices getIndices() {
        return indices;
    }

    @Override
    public ISqmlSelectorPresentations getSelectorPresentations() {
        return SqmlSelectorPresentations.EMPTY;
    }        

    @Override
    public ISqmlTableDef createCopyWithAlias(final String alias) {
        return new SqmlTableWithAlias(this,alias);
    }

    @Override
    public boolean hasEntityClass() {
        return false;
    }

    @Override
    public boolean hasDetails() {
        return false;
    }

    @Override
    public boolean hasAlias() {
        return false;
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public boolean isDeprecatedDdsDef() {
        return super.isDeprecated();
    }

}
