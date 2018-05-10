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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.KernelIcon;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.common.client.meta.mask.EditMaskNum;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEnumDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableIndexDef;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.xml.sax.Attributes;


final class SqmlTableColumnDef extends SqmlDefinitionImpl implements ISqmlColumnDef{
        
    private final EValType valType;
    private final boolean isNotNull;
    private final int length;
    private final int precision;
    private Collection<ISqmlEnumDef> enumDefs;
    private EditMask mask;
    SqmlTableDef ownerTable;
    
    public SqmlTableColumnDef(final SqmlModule module, final Attributes attributes){
        super(module,attributes);
        valType  = parseValType(attributes.getValue("ValType"));
        final int isNotNullAttrIndex = attributes.getIndex("NotNull");
        isNotNull = isNotNullAttrIndex>-1 ? "true".equals(attributes.getValue(isNotNullAttrIndex)) : false;
        final int precisionAttrIndex = attributes.getIndex("Precision");
        precision = precisionAttrIndex>-1 ? Integer.parseInt(attributes.getValue(precisionAttrIndex)) : -1;
        length = Integer.parseInt(attributes.getValue("Length"));
    }

    @Override
    public ClientIcon getIcon() {
        return KernelIcon.getInstance(RadixObjectIcon.getForValType(getType()));
    }

    @Override
    public EValType getType() {
        return valType;
    }

    @Override
    public Collection<ISqmlEnumDef> getEnums() {
        if (enumDefs == null) {
            initEnumDefs();
        }
        return enumDefs;
    }

    @Override
    public EditMask getEditMask() {
        if (mask==null){
            mask = EditMask.newInstance(valType);
            if (valType == EValType.STR) {
                ((EditMaskStr) mask).setMaxLength(length);
            } else if (valType == EValType.INT) {
                if (length > 0) {
                    final char[] digits = new char[length];
                    for (int i = 0; i < digits.length; i++) {
                        digits[i] = '9';
                    }
                    final BigInteger request = new BigInteger(String.valueOf(digits));
                    final BigInteger max_long = new BigInteger(String.valueOf(Long.MAX_VALUE));
                    final long result = request.min(max_long).longValue();
                    final EditMaskInt maskInt = (EditMaskInt) mask;
                    maskInt.setMaxValue(result);
                    maskInt.setMinValue(-result);
                }
            } else if (valType == EValType.NUM) {
                final EditMaskNum maskNum = (EditMaskNum) mask;
                if (length > 0) {
                    char[] digits = new char[length];
                    for (int i = 0; i < digits.length; i++) {
                        digits[i] = '9';
                    }
                    maskNum.setMaxValue(new BigDecimal(String.valueOf(digits)));
                } else {
                    maskNum.setMaxValue(null);
                }
                if (precision > 0) {
                    maskNum.setPrecision(precision);
                }
            }
        }
        return mask;
    }

    @Override
    public boolean isNotNull() {
        return isNotNull;
    }

    @Override
    public boolean hasProperty() {
        return false;
    }

    @Override
    public Id getSelectorPresentationClassId() {
        return null;
    }

    @Override
    public Id getSelectorPresentationId() {
        return null;
    }

    @Override
    public Id getReferencedTableId() {
        return null;
    }

    @Override
    public ISqmlTableIndexDef getReferenceIndex() {
        return null;
    }

    @Override
    public List<ISqmlColumnDef> getReferenceColumns() {
        return Collections.emptyList();
    }

    @Override
    public ISqmlTableDef getOwnerTable() {
        return getEnvironment().getSqmlDefinitions().findTableById(ownerTable.getId());
    }    

    @Override
    public Id getOwnerTableId() {
        return ownerTable.getId();
    }

    @Override
    public Id[] getIdPath() {
        return new Id[]{ownerTable.getId(), getId()};
    }        

    @Override
    public String getFullName() {
        return getModuleName()+"::"+ownerTable.getShortName()+":"+getShortName();
    }        

    @Override
    public boolean isDeprecatedDdsDef() {
        return isDeprecated();
    }

    private void initEnumDefs() {
        if (ownerTable.getMasterTableId()!=null) {
            //Для таблицы деталей нужно найти публикации колонок с типизацией набором констант            
            final Id masterTableId = ownerTable.getMasterTableId();
            final Id entityClassId = Id.Factory.changePrefix(masterTableId, EDefinitionIdPrefix.ADS_ENTITY_CLASS);
            //Идентификаторы прямых потомков базовой сущности
            final Collection<ISqmlTableDef> subClasses =
                    findSubClassesForMasterReference(entityClassId, ownerTable.getMasterReferenceId());
            enumDefs = new LinkedList<>();
            for (ISqmlTableDef tableDef : subClasses) {
                findPropertyWithEnum(tableDef);
            }
        } else {
            enumDefs = Collections.<ISqmlEnumDef>emptyList();
        }
    }
    
    private Collection<ISqmlTableDef> findSubClassesForMasterReference(final Id entityClassId, final Id masterReferenceId) {
        final Collection<ISqmlTableDef> result = new LinkedList<>();
        final Collection<Id> descendantIds =
                getEnvironment().getDefManager().getRepository().getDirectDescenders(entityClassId);
        for (Id descendantId : descendantIds) {
            final ISqmlTableDef descendant = getEnvironment().getSqmlDefinitions().findTableById(descendantId);
            if (descendant instanceof SqmlClassDef) {
                final Collection<Id> detailReferenceIds =
                        ((SqmlClassDef) descendant).getDetailReferenceIds();
                if (detailReferenceIds.contains(masterReferenceId)) {
                    result.add(descendant);
                    final Collection<Id> subClassesIds =
                            getEnvironment().getDefManager().getRepository().getDescendantsRecursively(descendantId);
                    for (Id classId : subClassesIds) {
                        final ISqmlTableDef subClass =
                                getEnvironment().getSqmlDefinitions().findTableById(classId);
                        if (subClass != null) {
                            result.add(subClass);
                        }
                    }
                }
            }
        }
        return result;
    }
    
    private void findPropertyWithEnum(final ISqmlTableDef table) {
        for (ISqmlColumnDef column : table.getColumns()) {
            if (column instanceof SqmlClassPropertyDef) {
                final Id detailColumnId = ((SqmlClassPropertyDef)column).getDetailColumnId();
                if (detailColumnId!=null && detailColumnId.equals(getId())) {
                    for (ISqmlEnumDef enumDef : column.getEnums()) {
                        if (!enumDefs.contains(enumDef)) {
                            enumDefs.add(enumDef);
                        }
                    }
                }                
            }
        }
    }        
}
