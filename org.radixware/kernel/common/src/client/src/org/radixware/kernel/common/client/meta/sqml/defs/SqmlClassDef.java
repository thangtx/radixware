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

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.KernelIcon;
import org.radixware.kernel.common.client.meta.sqml.ISqmlSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlSelectorPresentations;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableColumns;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableIndices;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableReferences;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;
import org.xml.sax.Attributes;

final class SqmlClassDef extends SqmlDefinitionImpl implements ISqmlTableDef{
    
    private final SqmlTableColumns properties;
    private final SqmlSelectorPresentations presentations;
    private final boolean hasDetails;
    private final List<Id> detailReferenceIds;
    private final Id[] arrDetIds;
    private final Id tableId;
    private final Id titleId;    
    private SqmlTableDef linkedTable;
    private SqmlTableReferences references;
    
    public SqmlClassDef(final SqmlModule module, 
                                  final Attributes attributes,
                                  final SqmlTableColumns properties,
                                  final SqmlSelectorPresentations presentations){
        super(module, attributes);
        this.properties = properties;
        this.presentations = presentations;
        hasDetails = SqmlDefinitionImpl.parseOptionalBoolean(attributes, "HasDetails", false);
        tableId = SqmlDefinitionImpl.parseOptionalId(attributes, "TableId", Id.Factory.changePrefix(getId(), EDefinitionIdPrefix.DDS_TABLE));
        titleId = SqmlDefinitionImpl.parseOptionalId(attributes, "TitleId");        
        arrDetIds = SqmlDefinitionImpl.parseArrId(attributes.getValue("AllowedDetails"));
        final Id[] arrDetRefIds = SqmlDefinitionImpl.parseArrId(attributes.getValue("AllowedDetailRefs"));
        if (arrDetRefIds ==null || arrDetRefIds .length==0){
            detailReferenceIds = Collections.emptyList();
        }else{
            detailReferenceIds = Arrays.asList(arrDetRefIds);
        }
        for (ISqmlSelectorPresentationDef presentation: presentations){
            if (presentation instanceof SqmlSelectorPresentationImpl){
                ((SqmlSelectorPresentationImpl)presentation).ownerClass = this;
            }
        }
    }
    
    public SqmlTableDef getLinkedTable(){
        return linkedTable;
    }
    
    public void linkWithTable(final SqmlTableDef tableDef){
        linkedTable = tableDef;
        properties.linkWithTableColumns(tableDef.getColumns());
        references = null;
    }

    public List<Id> getDetailReferenceIds(){
        return Collections.unmodifiableList(detailReferenceIds);
    }    
    
    @Override
    public ClientIcon getIcon() {
        if (getId().getPrefix() == EDefinitionIdPrefix.ADS_APPLICATION_CLASS) {
            return KernelIcon.getInstance(AdsDefinitionIcon.CLASS_APPLICATION);
        }
        return KernelIcon.getInstance(DdsDefinitionIcon.TABLE);
    }

    @Override
    public Id getTableId() {
        return tableId;
    }

    @Override
    public ISqmlTableColumns getColumns() {
        return properties;
    }

    @Override
    public ISqmlTableReferences getReferences() {
        if (references==null){
            final List<ISqmlTableReferences> referencesFromDetailTables = new LinkedList<>();
            if (arrDetIds!=null && arrDetIds.length>0){
                for (Id detailTableId: arrDetIds){
                    final ISqmlTableDef detailTable = getEnvironment().getSqmlDefinitions().findTableById(detailTableId);
                    if (detailTable!=null){
                        referencesFromDetailTables.add(detailTable.getReferences());
                    }
                }
            }
            if (linkedTable==null){
                references = new SqmlTableReferences(null, referencesFromDetailTables);                
            }else{
                references = new SqmlTableReferences(linkedTable.getReferences(), referencesFromDetailTables);
            }
        }
        return references;        
    }

    @Override
    public ISqmlTableIndices getIndices() {
        return linkedTable==null ? SqmlTableIndices.EMPTY : linkedTable.getIndices();
    }

    @Override
    public ISqmlSelectorPresentations getSelectorPresentations() {
        return presentations==null ? SqmlSelectorPresentations.EMPTY : presentations;
    }        

    @Override
    public ISqmlTableDef createCopyWithAlias(final String alias) {
        return new SqmlTableWithAlias(this, alias);
    }

    @Override
    public boolean hasEntityClass() {
        return true;
    }

    @Override
    public boolean hasDetails() {
        return hasDetails;
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
        return linkedTable==null ? super.isDeprecated() : linkedTable.isDeprecated();
    }

    @Override
    public String getTitle() {
        return getTitle(titleId);
    }
    
}
