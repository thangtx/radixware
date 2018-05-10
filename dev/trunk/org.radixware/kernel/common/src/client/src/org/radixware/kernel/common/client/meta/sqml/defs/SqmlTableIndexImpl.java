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
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.KernelIcon;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableIndexDef;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.types.Id;
import org.xml.sax.Attributes;


final class SqmlTableIndexImpl extends SqmlDefinitionImpl implements ISqmlTableIndexDef {
    
    private final boolean isPrimaryKey;
    private final Id[] columnIds;
    private final Id ownerTableId;
    
    public SqmlTableIndexImpl(final SqmlModule module, final Attributes attributes, final Id ownerTableId){
        super(module,attributes);
        final int isPrimaryKeyAttrIndex = attributes.getIndex("IsPrimaryKey");
        isPrimaryKey = isPrimaryKeyAttrIndex>-1 ? "true".equals(attributes.getValue(isPrimaryKeyAttrIndex)) : false;
        columnIds = parseArrId(attributes.getValue("IndexColumns"));
        this.ownerTableId = ownerTableId;
    }

    @Override
    public ClientIcon getIcon() {
        return KernelIcon.getInstance(DdsDefinitionIcon.INDEX);
    }

    @Override
    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    @Override
    public List<ISqmlColumnDef> getColumns() {
        if (columnIds==null || columnIds.length==0l){
            return Collections.emptyList();
        }else{
            final List<ISqmlColumnDef> columns = new LinkedList<>();
            final ISqmlTableDef ownerTable = getEnvironment().getSqmlDefinitions().findTableById(ownerTableId);
            ISqmlColumnDef column;
            for (Id columnId: columnIds){
                column = ownerTable.getColumns().getColumnById(columnId);
                if (column!=null){
                    columns.add(column);
                }
            }
            return columns;
        }
    }

    @Override
    public Id[] getIdPath() {
        return new Id[]{ownerTableId, getId()};
    }
    
    @Override
    public String getFullName() {
        return getShortName();
    }
}
