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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadParentRefPropertyDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskConstSet;
import org.radixware.kernel.common.client.meta.mask.EditMaskDateTime;
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.common.client.meta.mask.EditMaskNum;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.client.meta.mask.EditMaskTimeInterval;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEnumDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableIndexDef;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsInnateColumnPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParentPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParentRefPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsServerSidePropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.ParentRefProperty;
import org.radixware.kernel.common.defs.ads.clazz.members.ParentReferenceInfo;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyPresentation;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.client.env.KernelIcon;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDetailColumnPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.ColumnProperty;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;


final class SqmlColumnDefImpl extends SqmlDefinitionImpl implements ISqmlColumnDef {

    private final AdsServerSidePropertyDef propertyDef;
    private final DdsColumnDef columnDef;
    private final Id ownerId;
    private List<ISqmlColumnDef> referenceColumns;
    private Map<Id, ISqmlEnumDef> enumDefs;

    public SqmlColumnDefImpl(final IClientEnvironment environment, final AdsServerSidePropertyDef adsProperty, final Id ownerId) {
        super(environment, adsProperty);
        propertyDef = adsProperty;
        columnDef = null;
        this.ownerId = ownerId;
    }

    public SqmlColumnDefImpl(final IClientEnvironment environment, final DdsColumnDef ddsColumn, final Id ownerId) {
        super(environment, ddsColumn);
        columnDef = ddsColumn;
        propertyDef = null;
        this.ownerId = ownerId;
    }

    @Override
    public EValType getType() {
        return propertyDef == null ? columnDef.getValType() : propertyDef.getValue().getType().getTypeId();
    }

    public boolean isEnumType() {
        if (propertyDef == null) {
            return false;
        } else {
            final AdsType type = propertyDef.getValue().getType().resolve(propertyDef).get();
            return (type instanceof AdsEnumType);
        }
    }

    public Id getDetailColumnId() {
        if (propertyDef instanceof AdsDetailColumnPropertyDef) {
            ((AdsDetailColumnPropertyDef) propertyDef).getColumnInfo().getColumnId();
        } else {
            if (columnDef.getOwnerTable().isDetailTable()) {
                return columnDef.getId();
            }
        }
        return null;
    }

    @Override
    public Collection<ISqmlEnumDef> getEnums() {
        if (enumDefs == null) {
            initEnumDefs();
        }
        return enumDefs.values();
    }

    private void initEnumDefs() {
        if (propertyDef == null) {
            if (columnDef.getOwnerTable().isDetailTable()) {
                //Для таблицы деталей нужно найти публикации колонок с типизацией набором констант            
                final DdsReferenceDef masterReference = columnDef.getOwnerTable().findMasterReference();
                final Id masterTableId = masterReference.getParentTableId();
                final Id entityClassId = Id.Factory.changePrefix(masterTableId, EDefinitionIdPrefix.ADS_ENTITY_CLASS);
                //Идентификаторы прямых потомков базовой сущности
                final Collection<ISqmlTableDef> subClasses =
                        findSubClassesForMasterReference(entityClassId, masterReference.getId());
                enumDefs = new HashMap<>();
                for (ISqmlTableDef tableDef : subClasses) {
                    findPropertyWithEnum(tableDef);
                }
            } else {
                enumDefs = Collections.emptyMap();
            }
        } else {
            final AdsType type = propertyDef.getValue().getType().resolve(propertyDef).get();
            if (type instanceof AdsEnumType) {
                enumDefs = new HashMap<>();
                final ISqmlEnumDef enumDef = new SqmlEnumDefImpl(environment, ((AdsEnumType) type).getSource());
                enumDefs.put(enumDef.getId(), enumDef);
            } else {
                enumDefs = Collections.emptyMap();
            }
        }
    }

    private Collection<ISqmlTableDef> findSubClassesForMasterReference(final Id entityClassId, final Id masterReferenceId) {
        final Collection<ISqmlTableDef> result = new LinkedList<>();
        final Collection<Id> descendantIds =
                environment.getDefManager().getRepository().getDirectDescenders(entityClassId);
        for (Id descendantId : descendantIds) {
            final ISqmlTableDef descendant = environment.getSqmlDefinitions().findTableById(descendantId);
            if (descendant instanceof SqmlTableDefImpl) {
                final Collection<Id> detailReferenceIds =
                        ((SqmlTableDefImpl) descendant).getDetailReferenceIds();
                if (detailReferenceIds.contains(masterReferenceId)) {
                    result.add(descendant);
                    final Collection<Id> subClassesIds =
                            environment.getDefManager().getRepository().getDescendantsRecursively(descendantId);
                    for (Id classId : subClassesIds) {
                        final ISqmlTableDef subClass =
                                environment.getSqmlDefinitions().findTableById(classId);
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
            if (column instanceof SqmlColumnDefImpl) {
                final SqmlColumnDefImpl columnImpl = (SqmlColumnDefImpl) column;
                if (columnImpl.propertyDef instanceof AdsDetailColumnPropertyDef) {
                    final Id detailColumnId =
                            ((AdsDetailColumnPropertyDef) columnImpl.propertyDef).getColumnInfo().getColumnId();
                    if (Objects.equals(getId(), detailColumnId)) {
                        for (ISqmlEnumDef enumDef : column.getEnums()) {
                            if (!enumDefs.containsKey(enumDef.getId())) {
                                enumDefs.put(enumDef.getId(), enumDef);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public String getTitle() {
        if (propertyDef == null) {
            return columnDef.getName();
        } else {
            if (propertyDef.getPresentationSupport().getPresentation().getTitleId() == null) {
                return propertyDef.getName();
            }
            return checkTitle(propertyDef.getPresentationSupport().getPresentation().getTitle(environment.getLanguage()));
        }
    }

    @Override
    public ClientIcon getIcon() {
        return KernelIcon.getInstance(RadixObjectIcon.getForValType(getType()));
    }

    @Override
    public boolean isNotNull() {
        if (propertyDef == null) {
            return columnDef.isNotNull();
        } else {
            return propertyDef.getPresentationSupport().getPresentation().getEditOptions().isNotNull();
        }
    }

    @Override
    public EditMask getEditMask() {
        if (propertyDef == null) {
            final EditMask mask = EditMask.newInstance(columnDef.getValType());
            if (columnDef.getValType() == EValType.STR) {
                ((EditMaskStr) mask).setMaxLength(columnDef.getLength());
            } else if (columnDef.getValType() == EValType.INT) {
                if (columnDef.getLength() > 0) {
                    final char[] digits = new char[columnDef.getLength()];
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
            } else if (columnDef.getValType() == EValType.NUM) {
                final EditMaskNum maskNum = (EditMaskNum) mask;
                if (columnDef.getLength() > 0) {
                    char[] digits = new char[columnDef.getLength()];
                    for (int i = 0; i < digits.length; i++) {
                        digits[i] = '9';
                    }
                    maskNum.setMaxValue(new BigDecimal(String.valueOf(digits)));
                } else {
                    maskNum.setMaxValue(null);
                }
                if (columnDef.getPrecision() > 0) {
                    maskNum.setPrecision(columnDef.getPrecision());
                }
            }
            return mask;
        } else {
            if (enumDefs == null) {
                initEnumDefs();
            }
            if (!enumDefs.isEmpty()) {
                final ISqmlEnumDef enumDef = enumDefs.values().iterator().next();
                return new EditMaskConstSet(enumDef.getId(), null, null, null);
            }
            final PropertyPresentation propertyPresentation =
                    propertyDef.getPresentationSupport().getPresentation();

            final EValType valType = propertyDef.getValue().getType().getTypeId();
            if (propertyPresentation == null
                    || propertyPresentation.getEditOptions() == null
                    || propertyPresentation.getEditOptions().getEditMask() == null) {
                return EditMask.newInstance(valType);
            }
            org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMask adsEditMask =
                    propertyPresentation.getEditOptions().getEditMask();
            if (adsEditMask instanceof org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskStr) {
                final Integer maxLen =
                        ((org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskStr) adsEditMask).getDbMaxLen();
                final EditMaskStr result = new EditMaskStr();
                if (maxLen != null) {
                    result.setMaxLength(maxLen);
                }
                return result;
            } else if (adsEditMask instanceof org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskInt) {
                final Long maxVal =
                        ((org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskInt) adsEditMask).getDbMaxValue();
                final EditMaskInt result = new EditMaskInt();
                if (maxVal != null) {
                    result.setMaxValue(maxVal.longValue());
                }
                return result;
            } else if (adsEditMask instanceof org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskNum) {
                final BigDecimal maxVal =
                        ((org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskNum) adsEditMask).getDbMaxValue();
                final Byte precision =
                        ((org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskNum) adsEditMask).getPrecision();
                final EditMaskNum result = new EditMaskNum();
                if (maxVal != null) {
                    result.setMaxValue(maxVal);
                }
                if (precision != null) {
                    result.setPrecision(precision.intValue());
                }
                return result;
            } else if (adsEditMask instanceof org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskDateTime) {
                final String mask =
                        ((org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskDateTime) adsEditMask).getMask();
                final EditMaskDateTime result = new EditMaskDateTime();
                result.setCustomPattern(mask);
                return result;
            } else if (adsEditMask instanceof org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskTimeInterval) {
                final String mask =
                        ((org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskTimeInterval) adsEditMask).getMask();
                final org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskTimeInterval.Scale scale =
                        ((org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskTimeInterval) adsEditMask).getScale();
                final EditMaskTimeInterval result = new EditMaskTimeInterval(scale, mask);
                return result;
            } else {
                return EditMask.newInstance(valType);
            }
        }
    }
    /*
     private AdsSelectorPresentationDef findSelectorPresentation(){
     if (propertyDef!=null &&
     propertyDef.getPresentationSupport().getPresentation() instanceof ParentRefPropertyPresentation
     ){
     final ParentRefPropertyPresentation propertyPresentation =
     (ParentRefPropertyPresentation)propertyDef.getPresentationSupport().getPresentation();
     if (propertyPresentation!=null)
     return propertyPresentation.getParentSelect().findParentSelectorPresentation();
     }
     return null;
     }
     */

    private RadSelectorPresentationDef findSelectorPresentationDef() {
        if (propertyDef != null) {
            final Id classId = propertyDef.getOwnerClass().getId();
            final RadClassPresentationDef classDef = environment.getDefManager().getClassPresentationDef(classId);
            if (classDef.isPropertyDefExistsById(propertyDef.getId())) {
                final RadParentRefPropertyDef parentRefProp =
                        (RadParentRefPropertyDef) classDef.getPropertyDefById(propertyDef.getId());
                final RadSelectorPresentationDef presentation = parentRefProp.getParentSelectorPresentation();
                if (presentation != null) {
                    return presentation;
                }
            }
            return classDef.getDefaultSelectorPresentation();
        }
        return null;
    }

    @Override
    public Id getSelectorPresentationClassId() {
        final RadSelectorPresentationDef selectorPresentation = findSelectorPresentationDef();
        return selectorPresentation == null ? null : selectorPresentation.getId();
    }

    @Override
    public Id getSelectorPresentationId() {
        final RadSelectorPresentationDef selectorPresentation = findSelectorPresentationDef();
        return selectorPresentation == null ? null : selectorPresentation.getId();
    }

    @Override
    public ISqmlTableDef getOwnerTable() {
        return environment.getSqmlDefinitions().findTableById(ownerId);
    }

    @Override
    public Id getOwnerTableId() {
        if (propertyDef instanceof AdsInnateColumnPropertyDef) {
            return ((AdsInnateColumnPropertyDef) propertyDef).findTable().getId();
        } else {
            return columnDef == null ? propertyDef.getOwnerClass().getId() : columnDef.getOwnerTable().getId();
        }

    }

    @Override
    public Id getReferencedTableId() {
        if (propertyDef != null) {
            AdsPropertyDef finalProperty = propertyDef;
            while (finalProperty instanceof AdsParentPropertyDef) {
                finalProperty = ((AdsParentPropertyDef) finalProperty).getParentInfo().findOriginalProperty();
            }
            if (finalProperty instanceof AdsParentRefPropertyDef) {
                return ((AdsParentRefPropertyDef) finalProperty).findReferencedEntityClass().findTable(propertyDef).getId();
            }
        }
        return null;
    }

    @Override
    public boolean hasProperty() {
        return propertyDef != null;
    }

    @Override
    public ISqmlTableIndexDef getReferenceIndex() {
        if (propertyDef instanceof ParentRefProperty) {
            final ParentReferenceInfo referenceInfo = ((ParentRefProperty) propertyDef).getParentReferenceInfo();
            final DdsReferenceDef referenceDef = referenceInfo == null ? null : referenceInfo.findParentReference();
            final DdsIndexDef indexDef = referenceDef == null ? null : referenceDef.findParentIndex();
            return indexDef == null ? null : new SqmlTableIndexImpl(environment, indexDef);
        }
        return null;
    }

    @Override
    public List<ISqmlColumnDef> getReferenceColumns() {
        if (referenceColumns == null) {
            referenceColumns = new ArrayList<ISqmlColumnDef>();
            if (propertyDef instanceof ParentRefProperty) {
                final ParentReferenceInfo referenceInfo = ((ParentRefProperty) propertyDef).getParentReferenceInfo();
                final DdsReferenceDef referenceDef = referenceInfo == null ? null : referenceInfo.findParentReference();
                if (referenceDef != null) {
                    final DdsReferenceDef.ColumnsInfoItems columnsInfo = referenceDef.getColumnsInfo();
                    final ISqmlTableDef thisTable = getOwnerTable();
                    ISqmlColumnDef childColumn;
                    for (DdsReferenceDef.ColumnsInfoItem item : columnsInfo) {
                        childColumn = thisTable.getColumns().getColumnById(item.getChildColumnId());
                        if (childColumn == null) {//column in details
                            final Id ownerTableId = item.getChildColumn().getOwnerTable().getId();
                            final ISqmlTableDef ownerTable = environment.getSqmlDefinitions().findTableById(ownerTableId);
                            childColumn = ownerTable.getColumns().getColumnById(item.getChildColumnId());
                        }
                        if (childColumn != null) {
                            referenceColumns.add(childColumn);
                        }
                    }
                }
            }
        }
        return referenceColumns;
    }

    @Override
    public boolean isDeprecatedDdsDef() {
        if (columnDef instanceof ColumnProperty) {
            DdsColumnDef ddsColumn = ((ColumnProperty) columnDef).getColumnInfo().findColumn();
            return ddsColumn.isDeprecated();
        }
        return false;
    }
}
