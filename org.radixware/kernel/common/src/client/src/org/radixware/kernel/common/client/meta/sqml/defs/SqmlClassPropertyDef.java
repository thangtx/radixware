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

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.KernelIcon;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadParentRefPropertyDef;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskConstSet;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEnumDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableIndexDef;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.types.Id;
import org.xml.sax.Attributes;

final class SqmlClassPropertyDef extends SqmlDefinitionImpl implements ISqmlColumnDef{
    
    final static class ChildColumnInfo{
        
        public Id tableId;
        public Id columnId;
        
        public ChildColumnInfo(final Id tableId, final Id colId){
            this.tableId = tableId;
            columnId = colId;
        }
        
    }
        
    private final String qualifiedName;
    private final String ownerClassName;
    private final EValType valType;
    private final Id enumId;
    private final Id detailColumnId;
    private final Id titleId;
    private final Id titleOwnerId;
    private final Id ownerClassId;
    private final Id ownerTableId;
    private final Id referencedTableId;
    private final boolean isNotNull;
    private final boolean isInnateColumn;
    private final EditMask editMask;
    private final ISqmlTableIndexDef parentIndex;
    private final List<ChildColumnInfo> childColumns;
    private List<ISqmlColumnDef> referenceColumns;
    
    public SqmlClassPropertyDef(final SqmlModule module, 
                                                final Attributes attributes,
                                                final EditMask editMask,
                                                final ISqmlTableIndexDef parentIndex,
                                                final List<ChildColumnInfo> childColumns,
                                                final Id ownerClassId,
                                                final String ownerClassName){
        super(module, attributes);
        final int qNameAttrIndex = attributes.getIndex("QualifiedName");
        qualifiedName = qNameAttrIndex>-1 ? attributes.getValue(qNameAttrIndex) : null;
        valType = SqmlDefinitionImpl.parseValType(attributes.getValue("ValType"));
        enumId = SqmlDefinitionImpl.parseOptionalId(attributes, "EnumId");
        detailColumnId = SqmlDefinitionImpl.parseOptionalId(attributes, "DetailColumnId");
        titleId = SqmlDefinitionImpl.parseOptionalId(attributes, "TitleId");
        titleOwnerId = SqmlDefinitionImpl.parseOptionalId(attributes, "TitleOwnerId");
        isNotNull = SqmlDefinitionImpl.parseOptionalBoolean(attributes, "NotNull", false);
        this.ownerClassId = SqmlDefinitionImpl.parseOptionalId(attributes, "OwnerClassId", ownerClassId);
        isInnateColumn = SqmlDefinitionImpl.parseOptionalBoolean(attributes, "InnateColumn", true);
        if (isInnateColumn){
            final Id defaultOwnerTableId = Id.Factory.changePrefix(this.ownerClassId, EDefinitionIdPrefix.DDS_TABLE);
            ownerTableId = SqmlDefinitionImpl.parseOptionalId(attributes, "OwnerTableId", defaultOwnerTableId);
        }else{
            ownerTableId = null;
        }
        referencedTableId = SqmlDefinitionImpl.parseOptionalId(attributes, "ReferencedTableId");        
        if (enumId==null){
            this.editMask = editMask==null && valType!=null ? EditMask.newInstance(valType) : editMask;
        }else{
            this.editMask = new EditMaskConstSet(enumId);
        }
        this.parentIndex = parentIndex;
        this.childColumns = childColumns==null || childColumns.isEmpty() ? null : childColumns;
        this.ownerClassName = ownerClassName;
    }
    
    public Id getDetailColumnId(){
        return detailColumnId;
    }

    @Override
    public ClientIcon getIcon() {
        return KernelIcon.getInstance(RadixObjectIcon.getForValType(valType));
    }

    @Override
    public EValType getType() {
        return valType;
    }

    @Override
    public Collection<ISqmlEnumDef> getEnums() {
        if (enumId==null){
            return Collections.emptyList();
        }else{
            final ISqmlEnumDef enumDef = getEnvironment().getSqmlDefinitions().findEnumById(enumId);
            if (enumDef==null){
                return Collections.emptyList();
            }else{
                return Collections.singletonList(enumDef);
            }
        }
    }

    @Override
    public EditMask getEditMask() {
        return editMask;
    }

    @Override
    public boolean isNotNull() {
        return isNotNull;
    }

    @Override
    public boolean hasProperty() {
        return true;
    }
    
    private RadSelectorPresentationDef findSelectorPresentationDef() {
        try{
            final RadClassPresentationDef classDef = getEnvironment().getDefManager().getClassPresentationDef(ownerClassId);
            if (classDef.isPropertyDefExistsById(getId())) {
                final RadPropertyDef propDef = classDef.getPropertyDefById(getId());
                if (propDef instanceof RadParentRefPropertyDef){
                    return ((RadParentRefPropertyDef) propDef).getParentSelectorPresentation();
                }else{
                    return classDef.getDefaultSelectorPresentation();
                }
            }else{
                return classDef.getDefaultSelectorPresentation();
            }
            
        }catch(DefinitionNotFoundError error){
            return null;
        }
    }    

    @Override
    public Id getSelectorPresentationClassId() {
        final RadSelectorPresentationDef selectorPresentation = findSelectorPresentationDef();
        return selectorPresentation == null ? null : selectorPresentation.getOwnerClassId();
    }

    @Override
    public Id getSelectorPresentationId() {
        final RadSelectorPresentationDef selectorPresentation = findSelectorPresentationDef();
        return selectorPresentation == null ? null : selectorPresentation.getId();
    }

    @Override
    public Id getReferencedTableId() {
        return referencedTableId;
    }

    @Override
    public ISqmlTableIndexDef getReferenceIndex() {
        return parentIndex;
    }

    @Override
    public List<ISqmlColumnDef> getReferenceColumns() {
        if (childColumns==null || childColumns.isEmpty()){
            return Collections.emptyList();
        }
        if (referenceColumns==null){
            referenceColumns = new LinkedList<>();
            ISqmlTableDef childTable;
            ISqmlColumnDef childColumn;
            for (ChildColumnInfo childColumnInfo: childColumns){
                childTable = getEnvironment().getSqmlDefinitions().findTableById(childColumnInfo.tableId);
                childColumn = childTable==null ? null : childTable.getColumns().getColumnById(childColumnInfo.columnId);
                if (childColumn!=null){
                    referenceColumns.add(childColumn);
                }
            }
        }
        return referenceColumns;
    }

    @Override
    public ISqmlTableDef getOwnerTable() {
        return getEnvironment().getSqmlDefinitions().findTableById(ownerClassId);
    }

    @Override
    public Id getOwnerTableId() {
        return ownerTableId;
        //return getOwnerTable().getId();
    }

    @Override
    public boolean isDeprecatedDdsDef() {
        return false;
    }

    @Override
    public String getFullName() {
        if (qualifiedName==null){
            return getModuleName()+"::"+ownerClassName+":"+getShortName();
        }else{
            return qualifiedName;
        }
    }

    @Override
    public String getTitle() {
        return titleOwnerId==null ? getTitle(ownerClassId,titleId) : getTitle(titleOwnerId, titleId);
    }

    @Override
    public Id[] getIdPath() {
        return new Id[]{ownerClassId, getId()};
    }    
}
