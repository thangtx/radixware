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
package org.radixware.kernel.common.client.meta.filters;

import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.KernelIcon;
import org.radixware.kernel.common.client.errors.NoDefinitionWithSuchIdError;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.RadParentRefPropertyDef;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskConstSet;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.client.meta.mask.EditMaskRef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinitions;
import org.radixware.kernel.common.client.meta.sqml.ISqmlModifiableParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameterPersistentValue;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.views.IPropEditorDialog;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.xscml.Sqml;

public class RadFilterUserParamDef extends RadFilterParamDef implements ISqmlModifiableParameter {
    
    private final static int DEFAULT_MIN_ARRAY_ITEMS_COUNT = 1;
    private final static int DEFAULT_MAX_ARRAY_ITEMS_COUNT = 1000;

    final static class Factory {

        private Factory() {
        }

        public static RadFilterUserParamDef newInstance(final RadPropertyDef targetProperty, final boolean allowPersistentValue) {
            final RadFilterUserParamDef parameter =
                    new RadFilterUserParamDef(Id.Factory.newInstance(EDefinitionIdPrefix.USER_FILTER_PARAMETER),
                    null, targetProperty, null, null, null, allowPersistentValue, null, null);
            parameter.wasModified = true;
            return parameter;
        }

        public static RadFilterUserParamDef newInstance(final ISqmlColumnDef column, final boolean allowPersistentValue) {
            final RadFilterUserParamDef parameter =
                    new RadFilterUserParamDef(Id.Factory.newInstance(EDefinitionIdPrefix.USER_FILTER_PARAMETER),
                    column.getShortName(), null, column, null, null, allowPersistentValue, null, null);
            parameter.wasModified = true;
            return parameter;
        }

        public static RadFilterUserParamDef newInstance(final EValType valType, final boolean allowPersistentValue) {
            final RadFilterUserParamDef parameter =       
                    new RadFilterUserParamDef(Id.Factory.newInstance(EDefinitionIdPrefix.USER_FILTER_PARAMETER),
                                              "Filter param",
                                              valType,
                                              false,//isMandatory
                                              null,//editMask
                                              null,//nullTitle
                                              null,//defaultValue
                                              null,//referencedClassId
                                              null,//parentSelectorPresentationId
                                              null,
                                              allowPersistentValue,
                                              false,//useDropDownList,
                                              DEFAULT_MIN_ARRAY_ITEMS_COUNT,
                                              DEFAULT_MAX_ARRAY_ITEMS_COUNT
                                              );
            parameter.wasModified = true;
            return parameter;
        }

        static RadFilterUserParamDef loadFromXml(IClientEnvironment environment, org.radixware.schemas.groupsettings.FilterParameters.Parameter parameter, final boolean allowPersistentValue) {
            if (parameter.getPropertyBasedParamter() != null) {
                final org.radixware.schemas.groupsettings.PropertyBasedFilterParameter propertyBasedParam =
                        parameter.getPropertyBasedParamter();
                final RadClassPresentationDef classDef = environment.getDefManager().getClassPresentationDef(propertyBasedParam.getOwnerClassId());
                final Id basePropertyId = propertyBasedParam.getPropertyId();
                final ValAsStr defaultVal = propertyBasedParam.isSetDefaultVal() ? ValAsStr.Factory.loadFrom(propertyBasedParam.getDefaultVal()) : null;
                final Boolean isMandatory = propertyBasedParam.isSetNotNull() ? propertyBasedParam.getNotNull() : null;
                final Boolean isUseDropDownList = propertyBasedParam.isSetUseDropDownList() ? propertyBasedParam.getUseDropDownList() : null;
                final Sqml parentSelectorCondition = propertyBasedParam.getParentSelectorCondition();
                if (classDef.isPropertyDefExistsById(basePropertyId)) {
                    final RadPropertyDef baseProperty = classDef.getPropertyDefById(basePropertyId);
                    return new RadFilterUserParamDef(propertyBasedParam.getId(),
                            propertyBasedParam.getTitle(),
                            baseProperty,
                            null,
                            isMandatory,
                            defaultVal,
                            allowPersistentValue,
                            isUseDropDownList,
                            parentSelectorCondition);
                } else {
                    return new RadFilterUserParamDef(propertyBasedParam.getId(),
                            propertyBasedParam.getTitle(),
                            classDef.getId(),
                            basePropertyId,
                            isMandatory,
                            defaultVal,
                            allowPersistentValue,
                            environment,
                            isUseDropDownList,
                            parentSelectorCondition);
                }

            } else {
                final org.radixware.schemas.groupsettings.CustomFilterParameter customFilterParam =
                        parameter.getCustomParameter();
                final EValType paramType = customFilterParam.getValType();
                final EditMask editMask;
                if (customFilterParam.getEditMask() == null) {
                    editMask = customFilterParam.getEnumId() == null ? null : new EditMaskConstSet(customFilterParam.getEnumId());
                } else {
                    final org.radixware.schemas.editmask.RadixEditMaskDocument editMaskDoc =
                            org.radixware.schemas.editmask.RadixEditMaskDocument.Factory.newInstance();
                    editMaskDoc.setRadixEditMask((org.radixware.schemas.editmask.EditMask) customFilterParam.getEditMask().copy());
                    if (customFilterParam.getEditMask().isSetEnum()) {
                        final Id enumId = customFilterParam.getEnumId();
                        editMask = enumId == null ? null : EditMask.loadEditMaskConstSetFrom(enumId, editMaskDoc);
                    } else if (customFilterParam.getEditMask().isSetList()) {
                        editMask = EditMask.loadEditMaskList(environment, paramType, null, editMaskDoc);
                    } else {
                        editMask = EditMask.loadFrom(editMaskDoc);
                    }
                }
                final Boolean isUseDropDownList = customFilterParam.isSetUseDropDownList() ? customFilterParam.getUseDropDownList() : null;
                final ValAsStr defaultVal = customFilterParam.isSetDefaultVal() ? ValAsStr.Factory.loadFrom(customFilterParam.getDefaultVal()) : null;
                final int minArrayItemsCount = customFilterParam.isSetMinArrayItemsCount() ? customFilterParam.getMinArrayItemsCount() : DEFAULT_MIN_ARRAY_ITEMS_COUNT;
                final int maxArrayItemsCount = customFilterParam.isSetMaxArrayItemsCount() ? customFilterParam.getMaxArrayItemsCount() : DEFAULT_MAX_ARRAY_ITEMS_COUNT;
                return new RadFilterUserParamDef(
                        customFilterParam.getId(),
                        customFilterParam.getTitle(),
                        paramType,
                        customFilterParam.getNotNull(),
                        editMask,
                        customFilterParam.getNullTitle(),
                        defaultVal,
                        customFilterParam.getReferencedClassId(),
                        customFilterParam.getParentSelectorPresentationId(),
                        customFilterParam.getParentSelectorCondition(),
                        allowPersistentValue,
                        isUseDropDownList,
                        minArrayItemsCount,
                        maxArrayItemsCount);
            }
        }
    }
    private String userTitle, nullValueTitle;//заголовок пользовательского параметра
    private EValType type = null;
    private ValAsStr defaultVal;
    private Boolean mandatory;
    private Boolean useDropDownList;
    private Sqml parentSelectorAdditionalCondition;
    private int minArrayItemsCount = DEFAULT_MIN_ARRAY_ITEMS_COUNT;
    private int maxArrayItemsCount = DEFAULT_MAX_ARRAY_ITEMS_COUNT;
    private EditMask mask = null;
    private boolean wasModified;
    private ISqmlColumnDef column;
    private ISqmlParameterPersistentValue persistentValue;
    private boolean isValid = true;
    private final boolean allowPersistentValue;
    private Id targetColumnId;    
    private final Id targetOwnerId;
    private final Id targetId;
    private IClientEnvironment environment;

    //Конструктор по свойству
    private RadFilterUserParamDef(
            final Id id,
            final String title, //если = null, будет браться из targetProperty
            final RadPropertyDef targetProperty, //не равно null. только свойства из базовой entity            
            final ISqmlColumnDef targetColumn, //не равно null. только колонки из базовой entity
            final Boolean isMandatory,
            final ValAsStr defaultValue, //значение по-умолчанию может быть null
            final boolean allowPersistentValue,            
            final Boolean useDropDownList,
            final Sqml parentSelectorAdditionalCondition) {
        super(id,
                "",//name          //собственное имя (берется из тега)
                null,//titleId
                null,//ownerId
                targetProperty == null ? null : targetProperty.getOwnerClassId(),//tableId - targetProperty уже задан
                targetProperty == null ? null : targetProperty.getId(),//propertyId - targetProperty уже задан
                targetProperty == null ? targetColumn.getType() : targetProperty.getType(),
                null,//constSetId
                defaultValue,
                //Editing
                false,//isMandatory
                targetProperty == null ? true : targetProperty.isArrayItemMandatory(),
                false,//storeHistory
                false,//isCustomDialog
                null,//customDialogId
                false,//isCustomEditOnly
                null,//editMaskStr
                null,//nullStringId
                null,//arrayItemNullStringId
                null,//emptyArrayStringId (onlyForArrayProperties)
                targetProperty==null ? false : targetProperty.isDuplicatesEnabled(),
                targetProperty==null ? DEFAULT_MIN_ARRAY_ITEMS_COUNT : targetProperty.getMinArrayItemsCount(),
                targetProperty==null ? DEFAULT_MAX_ARRAY_ITEMS_COUNT : targetProperty.getMaxArrayItemsCount(),
                targetProperty==null ? 1 : targetProperty.getFirstArrayItemIndex(),                
                false,//isMemo
                
                //Parent selector
                null,// referencedClassId,
                null,//parentSelectorPresentationId
                useDropDownList);
        userTitle = title;
        defaultVal = defaultValue;
        if (targetColumn != null) {
            targetColumnId = targetColumn.getId();
            setBaseProperty(targetColumn);
        } else {
            targetColumnId = null;
            target = targetProperty;
        }
        if (isMandatory == null) {
            if (target != null) {
                mandatory = target.isMandatory();
            } else if (column != null) {
                mandatory = column.isNotNull();
            }
        } else {
            mandatory = isMandatory;
        }
        this.allowPersistentValue = allowPersistentValue;
        targetOwnerId = null;
        targetId = null;
        this.useDropDownList = useDropDownList;
        this.parentSelectorAdditionalCondition = 
            parentSelectorAdditionalCondition==null ? null : (Sqml)parentSelectorAdditionalCondition.copy();        
    }

    //Конструктор для параметра по колонке таблицы
    private RadFilterUserParamDef(
            final Id id,
            final String title, //если = null, будет браться из targetProperty
            final Id targetOwnerId, //не равно null.
            final Id targetColumnId, //не равно null.
            final Boolean isMandatory,
            final ValAsStr defaultValue, //значение по-умолчанию может быть null
            final boolean allowPersistentValue,
            final IClientEnvironment environment,
            final Boolean useDropDownList,
            final Sqml parentSelectorAdditionalCondition) {
        super(id,
                "",//name          //собственное имя (берется из тега)
                null,//titleId
                null,//ownerId
                null,//tableId
                null,//targetProperty
                EValType.ANY,//getValType перекрыт
                null,//constSetId
                defaultValue,
                //Editing
                false,//isMandatory
                false,//storeHistory
                false,//isCustomDialog
                null,//customDialogId
                false,//isCustomEditOnly
                null,//editMaskStr
                null,//nullStringId
                false,//isMemo
                //Parent selector
                null,// referencedClassId,
                null,
                useDropDownList);
        this.environment = environment;
        userTitle = title;
        defaultVal = defaultValue;
        targetId = null;
        this.targetColumnId = targetColumnId;
        this.targetOwnerId = targetOwnerId;
        if (isMandatory != null) {
            mandatory = isMandatory;
        }
        this.allowPersistentValue = allowPersistentValue;
        this.useDropDownList = useDropDownList;
        this.parentSelectorAdditionalCondition = 
            parentSelectorAdditionalCondition==null ? null : (Sqml)parentSelectorAdditionalCondition.copy();        
    }

    //Конструктор испорченного параметра (когда не найдено свойство)
    private RadFilterUserParamDef(
            final Id id,
            final Id targetOwnerId,
            final Id targetId) {
        super(id,
                targetId.toString(),//name
                null,//titleId
                null,//ownerId
                null,
                null,
                EValType.STR,
                null,//constSetId
                null,
                //Editing
                false,//isMandatory
                false,//storeHistory
                false,//isCustomDialog
                null,//customDialogId
                false,//isCustomEditOnly
                null,//editMaskStr
                null,//nullStringId
                false,//isMemo
                //Parent selector
                null,// referencedClassId,
                null,
                false);
        allowPersistentValue = false;
        this.targetOwnerId = targetOwnerId;
        this.targetColumnId = null;
        this.targetId = targetId;
    }

    //Конструктор по заданным атрибутам
    protected RadFilterUserParamDef(
            final Id id,
            final String title, //Заголовок параметра, не может быть null
            final EValType valType,//не может быть null
            final boolean isMandatory,
            final EditMask editMask,//маска может быть null
            final String nullTitle,//Строка, для показа null-значения
            final ValAsStr defaultValue, //значение по-умолчанию может быть null
            final Id referencedClassId,
            final Id parentSelectorPresentationId,
            final Sqml parentSelectorAdditionalCondition,
            final boolean allowPersistentValue,
            final Boolean useDropDownList,
            final int minArrayItemsCount,
            final int maxArrayItemsCount) {
        super(id,
                "",//name          //собственное имя (берется из тега)
                null,//titleId
                null,//ownerId
                null,//tableId - targetProperty уже задан
                null,//propertyId - targetProperty уже задан
                valType,
                null,//constSetId
                defaultValue,
                //Editing
                false,//isMandatory
                true,//arrayItemIsMandatory
                false,//storeHistory
                false,//isCustomDialog
                null,//customDialogId
                false,//isCustomEditOnly
                null,//editMaskStr
                null,//nullStringId
                null,//arrayItemNullStringId
                null,//emptyArrayStringId (onlyForArrayProperties)
                false,//isDuplicatesEnabled (onlyForArrayProperties)
                minArrayItemsCount,//minArrayItemsCount
                maxArrayItemsCount,//maxArrayItemsCount
                1,//firstArrayItemIndex                
                false,//isMemo
                //Parent selector
                referencedClassId,// referencedClassId,
                parentSelectorPresentationId,
                useDropDownList);
        if (valType == null) {
            throw new NullPointerException("valType must be not null'");
        }
        if (title == null) {
            throw new NullPointerException("title must be not null'");
        }
        target = null;
        type = valType;
        userTitle = title;
        nullValueTitle = nullTitle;
        mask = editMask != null ? EditMask.newCopy(editMask) : null;
        checkMaskIsValid();
        mandatory = isMandatory;
        defaultVal = defaultValue;
        this.allowPersistentValue = allowPersistentValue;
        this.targetOwnerId = null;
        this.targetColumnId = null;
        this.targetId = null;
        this.minArrayItemsCount = minArrayItemsCount;
        this.maxArrayItemsCount = maxArrayItemsCount;
        this.parentSelectorAdditionalCondition = 
            parentSelectorAdditionalCondition==null ? null : (Sqml)parentSelectorAdditionalCondition.copy();
    }        

    @Override
    public boolean isPredefined() {
        return false;
    }

    public boolean isValid() {
        if (column == null && targetColumnId != null) {
            getSqmlColumn();
        }
        return isValid && (targetId == null || target != null);
    }

    @Override
    protected RadPropertyDef getTargetProperty() {
        return target;
    }

    @Override
    public Id getBasePropertyId() {
        if (target != null) {
            return target.getId();
        } else if (getSqmlColumn() != null) {
            return getSqmlColumn().getId();
        } else {
            return null;
        }
    }

    @Override
    public EValType getType() {
        if (target != null) {
            return target.getType();
        } else if (getSqmlColumn() != null) {
            return getSqmlColumn().getType();
        } else {
            return type;
        }
    }

    @Override
    public String getTitle(final IClientEnvironment environment) {
        return getTitleImpl(environment);
    }
        
    @Override
    public String getTitle() {
        return getTitleImpl(null);
    }
    
    private String getTitleImpl(final IClientEnvironment environment){
        if (!isValid()) {
            if (targetId == null) {
                return "??? " + targetColumnId.toString() + " ???";
            } else {
                return "??? " + targetId.toString() + " ???";
            }
        } else if (userTitle == null) {
            return target == null ? getName() : target.getTitle(environment);
        } else {
            return userTitle;
        }        
    }

    @Override
    public boolean hasTitle() {
        return userTitle != null || (target != null && target.hasTitle());
    }

    @Override
    public RadEnumPresentationDef getConstSet() {
        if (target != null) {
            return target.getConstSet();
        }
        final EditMask editMask = getEditMask();
        if (editMask.getType() == EEditMaskType.ENUM) {
            return editMask.getRadEnumPresentationDef(getApplication());
        }
        return null;
    }

    @Override
    public IPropEditorDialog getPropEditorDialog(IClientEnvironment env) {
        return target != null ? target.getPropEditorDialog(env) : null;
    }

    @Override
    public EditMask getEditMask() {
        final EditMask resultMask;
        if (!isValid()) {
            resultMask = new EditMaskNone();
        } else if (target != null) {
            resultMask = target.getEditMask();
        } else if (getSqmlColumn() != null) {
            resultMask = EditMask.newCopy(getSqmlColumn().getEditMask());
        }else{
            resultMask = mask == null ? EditMask.newInstance(getType()) : EditMask.newCopy(mask);
            if (nullValueTitle != null) {
                resultMask.setNoValueStr(nullValueTitle);
            }
        }
        if (getUseDropDownList()!=null && resultMask instanceof EditMaskRef){
            ((EditMaskRef)resultMask).setUseDropDownList(getUseDropDownList());
        }
        return resultMask;
    }

    @Override
    public String getNullString() {
        if (target != null) {
            return target.getNullString();
        } else {
            return nullValueTitle == null ? super.getNullString() : nullValueTitle;
        }
    }

    @Override
    public boolean customDialog() {
        return target != null ? target.customDialog() : false;
    }

    @Override
    public boolean isCustomEditOnly() {
        return target != null ? target.isCustomEditOnly() : false;
    }

    @Override
    public boolean isMandatory() {
        if (mandatory == null) {
            if (targetColumnId != null && column == null && getSqmlColumn() != null) {
                mandatory = getSqmlColumn().isNotNull();
            } else {
                return false;
            }
        }
        return mandatory;
    }

    @Override
    public boolean isMemo() {
        return target != null ? target.isMemo() : false;
    }

    @Override
    public boolean storeHistory() {
        return target != null ? target.storeHistory() : false;
    }

    @Override
    public Id getReferencedTableId() {
        if (target != null && (target instanceof RadParentRefPropertyDef)) {
            return ((RadParentRefPropertyDef) target).getReferencedTableId();
        } else {
            if (referencedClassId != null) {
                final RadClassPresentationDef classPresentation =
                        getDefManager().getClassPresentationDef(referencedClassId);
                return classPresentation.getTableId();
            } else {
                return null;
            }
        }
    }
    
    @Override
    public Sqml getParentSelectorAdditionalCondition(){
        return parentSelectorAdditionalCondition==null ? null : (Sqml)parentSelectorAdditionalCondition.copy();
    }

    @Override
    public ValAsStr getInitialVal() {
        return defaultVal;
    }

    public final boolean wasModified() {
        return wasModified;
    }

    @Override
    protected RadPropertyDef getFinalProperty() {
        return this;
    }

    @Override
    public ERuntimeEnvironmentType getEditingEnvironmentType() {
        return ERuntimeEnvironmentType.COMMON_CLIENT;
    }

    /**
     * сеттеры
     *
     */
    @Override
    public void setTitle(final String title) {
        if (title == null && target == null) {
            throw new NullPointerException("title must be not null");
        }
        if (title != null && !title.equals(userTitle)) {
            userTitle = title;
            wasModified = true;
        }
    }

    @Override
    public void setNullString(final String nullTitle) {
        if (!Objects.equals(nullTitle, getNullString())) {
            nullValueTitle = nullTitle;
            wasModified = true;
        }
    }

    @Override
    public void setEditMask(final EditMask editMask) {
        if (!editMaskEquals(editMask, mask)) {
            mask = editMask == null ? null : EditMask.newCopy(editMask);
            checkMaskIsValid();
            wasModified = true;
        }
    }

    @Override
    public void setInitialValue(final ValAsStr value) {
        if (!Objects.equals(value, defaultVal)) {
            defaultVal = value;
            updateChangeValueTimestamp();
            wasModified = true;
        }
    }

    @Override
    public void setMandatory(final boolean isMandatory) {
        if (mandatory == null || mandatory.booleanValue() != isMandatory) {
            mandatory = isMandatory;
            wasModified = true;
        }
    }

    private void setTargetProperty(final RadPropertyDef property) {
        if (property == null) {
            throw new NullPointerException("target property must be not null");
        }
        if (target == null || property.getId() != target.getId()) {
            target = property;
            mandatory = target.isMandatory();
            targetColumnId = null;
            column = null;
            updateChangeValueTimestamp();
            wasModified = true;
        }
    }

    private void setTargetColumn(final ISqmlColumnDef targetColumn) {
        if (targetColumn == null) {
            throw new NullPointerException("target property must be not null");
        }

        if ((target == null || targetColumn.getId() != target.getId())
                && (getSqmlColumn() == null || targetColumn.getId() != getSqmlColumn().getId())) {
            column = targetColumn;
            mandatory = targetColumn.isNotNull();
            target = null;
            wasModified = true;
        }
    }

    @Override
    public final void setBaseProperty(ISqmlColumnDef column) {
        if (column == null) {
            throw new NullPointerException("base property must be not null");
        }

        final Id classDefId = column.getOwnerTable().getId();
        final RadClassPresentationDef classDef;
        try {
            classDef = getDefManager().getClassPresentationDef(classDefId);
        } catch (DefinitionError error) {
            setTargetColumn(column);
            return;
        }

        if (classDef.isPropertyDefExistsById(column.getId())) {
            setTargetProperty(classDef.getPropertyDefById(column.getId()));
        } else {
            setTargetColumn(column);
        }
    }

    @Override
    public void setParentSelectorPresentation(final Id classId, final Id presentationId) {
        if (!Objects.equals(classId, referencedClassId) || !Objects.equals(presentationId, parentSelectorPresentationId)) {
            referencedClassId = classId;
            parentSelectorPresentationId = presentationId;
            wasModified = true;
        }
    }
    
    @Override
    public void setParentSelectorAdditionalCondition(final Sqml condition){
        if (!Objects.equals(condition==null ? null : condition.xmlText(), parentSelectorAdditionalCondition==null ? null : parentSelectorAdditionalCondition.xmlText())){
            parentSelectorAdditionalCondition = condition==null ? null : (Sqml)condition.copy();
            wasModified = true;
        }
    }

    @Override
    public void setUseDropDownList(final Boolean useDropDownList) {
        if (!Objects.equals(this.useDropDownList, useDropDownList)){
            this.useDropDownList = useDropDownList;
            wasModified = true;
        }
    }

    @Override
    public int getMinArrayItemsCount() {
        return target==null ? minArrayItemsCount : target.getMinArrayItemsCount();
    }
        
    @Override
    public void setMinArrayItemsCount(final int count) {
        if (target==null && minArrayItemsCount!=count){
            minArrayItemsCount = count;
            wasModified = true;
        }
    }

    @Override
    public int getMaxArrayItemsCount() {
        return target==null ? maxArrayItemsCount : target.getMaxArrayItemsCount();
    }
        
    @Override
    public void setMaxArrayItemsCount(final int count) {
        if (target==null && maxArrayItemsCount!=count){
            maxArrayItemsCount = count;
            wasModified = true;
        }
    }        

    @Override
    public Boolean getUseDropDownList() {
        return useDropDownList;
    }
       

    @Override
    public void setValType(final EValType valType, final EditMask editMask, final boolean isMandatory, final String nullTitle) {
        if (valType == null) {
            throw new IllegalArgumentException("value type must be not null");
        }
        if (valType != getType() || !editMaskEquals(editMask, mask) || mandatory == null || isMandatory != mandatory.booleanValue()
                || !Objects.equals(nullTitle, nullValueTitle) || target != null) {
            target = null;
            column = null;
            type = valType;
            mask = editMask != null ? EditMask.newCopy(editMask) : EditMask.newInstance(type);
            checkMaskIsValid();
            nullValueTitle = nullTitle;
            mandatory = isMandatory;
            updateChangeValueTimestamp();
            wasModified = true;
        }
    }

    private void checkMaskIsValid() {
        if (target == null && getSqmlColumn() == null && mask != null && !mask.getSupportedValueTypes().contains(type)) {
            throwMaskIsInvalid();
        }
    }

    private void throwMaskIsInvalid() {
        final String typeStr = getType().getName();
        final String maskClass = mask.getClass().getSimpleName();
        throw new IllegalArgumentException("edit mask of class \"" + maskClass + "\" is not acceptable for type " + typeStr);
    }

    private static boolean editMaskEquals(final EditMask mask1, final EditMask mask2) {
        if (mask1 == null || mask2 == null) {
            return mask1 == mask2;
        }
        if (mask1.getType() == null) {
            return mask2.getType() == null;
        } else if (mask2.getType() == null) {
            return false;
        }
        org.radixware.schemas.editmask.RadixEditMaskDocument document =
                org.radixware.schemas.editmask.RadixEditMaskDocument.Factory.newInstance();
        mask1.writeToXml(document.addNewRadixEditMask());
        final String mask1AsStr = document.xmlText();
        document =
                org.radixware.schemas.editmask.RadixEditMaskDocument.Factory.newInstance();
        mask2.writeToXml(document.addNewRadixEditMask());
        final String mask2AsStr = document.xmlText();
        return mask1AsStr.equals(mask2AsStr);
    }

    public RadFilterUserParamDef copy() {
        final RadFilterUserParamDef copy;
        if (!isValid()) {
            copy = new RadFilterUserParamDef(getId(), targetOwnerId, targetId);
        } else if (targetColumnId != null && column == null) {
            copy = new RadFilterUserParamDef(getId(), userTitle, targetOwnerId, targetColumnId, mandatory, defaultVal, allowPersistentValue, environment, useDropDownList, parentSelectorAdditionalCondition);
        } else if (target != null || getSqmlColumn() != null) {
            copy = new RadFilterUserParamDef(getId(), userTitle, target, getSqmlColumn(), mandatory, defaultVal, allowPersistentValue, useDropDownList, parentSelectorAdditionalCondition);
        } else {
            copy = new RadFilterUserParamDef(getId(), userTitle,
                    type,
                    isMandatory(),
                    mask != null ? EditMask.newCopy(mask) : null,
                    nullValueTitle,
                    defaultVal,
                    referencedClassId,
                    parentSelectorPresentationId,
                    parentSelectorAdditionalCondition,
                    allowPersistentValue,
                    useDropDownList,
                    minArrayItemsCount,
                    maxArrayItemsCount);
        }
        if (persistentValue != null) {
            copy.persistentValue = persistentValue.copy();
        }

        return copy;
    }

    /*
     * Сохранение - восстановление атрибутов
     */
    private RadFilterUserParamDef copy;
    private boolean saveModifiedState;

    @Override
    public void saveState() {
        copy = copy();
        saveModifiedState = wasModified;
    }

    @Override
    public void restoreState() {
        if (copy != null && isValid()) {
            setTitle(copy.userTitle);
            setInitialValue(copy.defaultVal);
            if (copy.target != null) {
                setTargetProperty(copy.target);
                setMandatory(copy.isMandatory());
            } else if (copy.getSqmlColumn() != null) {
                setTargetColumn(copy.getSqmlColumn());
                setMandatory(copy.isMandatory());
            } else {
                setValType(copy.type, copy.mask, copy.isMandatory(), copy.nullValueTitle);
                setParentSelectorPresentation(copy.referencedClassId, copy.parentSelectorPresentationId);
            }
            if (copy.persistentValue != null) {
                persistentValue = copy.persistentValue.copy();
            } else {
                persistentValue = null;
            }
            if (copy.target==null){
                minArrayItemsCount = copy.getMinArrayItemsCount();
                maxArrayItemsCount = copy.getMaxArrayItemsCount();
            }
            if (copy.parentSelectorAdditionalCondition==null){
                parentSelectorAdditionalCondition=null;
            }else{
                parentSelectorAdditionalCondition = (Sqml)copy.parentSelectorAdditionalCondition.copy();
            }
            wasModified = saveModifiedState;
        }
    }

    final void switchToFixedState() {
        wasModified = false;
    }
    
    public void writeToXml(final org.radixware.schemas.groupsettings.FilterParameters parameters) {
        final org.radixware.schemas.groupsettings.FilterParameters.Parameter param = parameters.addNewParameter();
        if (!isValid()) {
            final org.radixware.schemas.groupsettings.PropertyBasedFilterParameter parameter =
                    param.addNewPropertyBasedParamter();
            parameter.setId(getId());
            parameter.setDefinitionType(EDefType.FILTER_PARAM);
            parameter.setOwnerClassId(targetOwnerId);
            if (targetId!=null){
                parameter.setPropertyId(targetId);
            }
        } else if (target != null || getSqmlColumn() != null) {
            final org.radixware.schemas.groupsettings.PropertyBasedFilterParameter parameter =
                    param.addNewPropertyBasedParamter();
            parameter.setId(getId());
            parameter.setDefinitionType(EDefType.FILTER_PARAM);
            parameter.setTitle(userTitle);
            parameter.setNotNull(isMandatory());
            parameter.setOwnerClassId(target == null ? getSqmlColumn().getOwnerTableId() : target.getOwnerClassId());
            parameter.setPropertyId(target == null ? getSqmlColumn().getId() : target.getId());
            if (defaultVal != null) {
                parameter.setDefaultVal(defaultVal.toString());
            }
            if (useDropDownList!=null && (getType()==EValType.ARR_REF || getType()==EValType.PARENT_REF)){
                parameter.setUseDropDownList(useDropDownList);
            }
            if (parentSelectorAdditionalCondition!=null && (getType()==EValType.ARR_REF || getType()==EValType.PARENT_REF)){
                parameter.setParentSelectorCondition(parentSelectorAdditionalCondition);
            }            
        } else {
            final org.radixware.schemas.groupsettings.CustomFilterParameter parameter =
                    param.addNewCustomParameter();
            parameter.setId(getId());
            parameter.setDefinitionType(EDefType.FILTER_PARAM);
            parameter.setTitle(userTitle);
            parameter.setValType(type);
            parameter.setNotNull(isMandatory());
            if (getConstSet() != null) {
                parameter.setEnumId(getConstSet().getId());
            }
            if (mask != null && mask.getType() != null) {
                mask.writeToXml(parameter.addNewEditMask());
            }
            if (nullValueTitle != null) {
                parameter.setNullTitle(nullValueTitle);
            }
            if (defaultVal != null) {
                parameter.setDefaultVal(defaultVal.toString());
            }
            if (referencedClassId != null) {
                parameter.setReferencedClassId(referencedClassId);
            }
            if (parentSelectorPresentationId != null) {
                parameter.setParentSelectorPresentationId(parentSelectorPresentationId);
            }
            if (parentSelectorAdditionalCondition!=null && (getType()==EValType.ARR_REF || getType()==EValType.PARENT_REF)){
                parameter.setParentSelectorCondition(parentSelectorAdditionalCondition);
            }
            if (useDropDownList!=null && (getType()==EValType.ARR_REF || getType()==EValType.PARENT_REF)){
                parameter.setUseDropDownList(useDropDownList);
            }            
        }
    }

    @Override
    public String getFullName() {
        return getTitle();
    }

    @Override
    public String getShortName() {
        return getTitle();
    }

    @Override
    public ClientIcon getIcon() {
        if (isValid()) {
            switch (getType()) {
                case ARR_CHAR:
                case ARR_CLOB:
                case ARR_STR:
                case CHAR:
                case CLOB:
                case STR:
                    return KernelIcon.getInstance(AdsDefinitionIcon.SQL_CLASS_LITERAL_PARAMETER);
                case ARR_REF:
                case PARENT_REF:
                    return KernelIcon.getInstance(AdsDefinitionIcon.SQL_CLASS_PK_PARAMETER);
                default:
                    return KernelIcon.getInstance(AdsDefinitionIcon.SQL_CLASS_CUSTOM_PARAMETER);
            }
        } else {
            return ClientIcon.TraceLevel.ERROR;
        }
    }

    @Override
    public boolean canHavePersistentValue() {
        return allowPersistentValue;
    }

    @Override
    public void setPersistentValue(final ISqmlParameterPersistentValue value) {
        persistentValue = value;
    }

    @Override
    public ISqmlParameterPersistentValue getPersistentValue() {
        return persistentValue;
    }

    @Override
    public boolean isDeprecated() {
        return super.isDeprecated() ? true : getSqmlColumn() != null && getSqmlColumn().isDeprecated();
    }

    private ISqmlColumnDef getSqmlColumn() {
        if (target == null && column == null) {
            if (targetOwnerId != null && targetColumnId != null && isValid) {
                final ISqmlDefinitions definitions = environment.getSqmlDefinitions();
                final ISqmlTableDef table = definitions.findTableById(targetOwnerId);
                if (table == null) {
                    final String reason = environment.getMessageProvider().translate("SqmlEditor", "table or entity #%s not found");
                    final String formattedReason = String.format(reason, targetOwnerId.toString());
                    final String message =
                            environment.getMessageProvider().translate("ExplorerError", "Can't load custom filter parameter #%s:\n%s");
                    environment.getTracer().error(String.format(message, getId().toString(), formattedReason));
                    isValid = false;
                }
                column = table.getColumns().getColumnById(targetColumnId);
                if (column == null) {
                    final RadClassPresentationDef classDef;
                    try {
                        classDef = environment.getDefManager().getClassPresentationDef(targetOwnerId);
                        final NoDefinitionWithSuchIdError error =
                                new NoDefinitionWithSuchIdError(classDef, NoDefinitionWithSuchIdError.SubDefinitionType.PROPERTY, targetColumnId);
                        final String reason = error.getLocalizedMessage(environment.getMessageProvider());
                        final String message =
                                environment.getMessageProvider().translate("ExplorerError", "Can't load custom filter parameter #%s");
                        environment.getTracer().error(String.format(message, getId().toString(), reason));
                    } catch (DefinitionError error) {
                        final String reason = environment.getMessageProvider().translate("SqmlEditor", "table or entity #%s not found");
                        final String formattedReason = String.format(reason, targetOwnerId.toString());
                        environment.getTracer().error(formattedReason, error);
                    }
                    isValid = false;
                    mandatory = Boolean.FALSE;
                }
            }
        }
        return column;
    }
    
    public boolean isBasedOnPresentableProperty(){
        return target!=null;
    }
}
