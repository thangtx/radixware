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
import org.radixware.kernel.common.utils.Utils;

public class RadFilterUserParamDef extends RadFilterParamDef implements ISqmlModifiableParameter {

    final static class Factory {

        private Factory() {
        }

        public static RadFilterUserParamDef newInstance(final RadPropertyDef targetProperty, final boolean allowPersistentValue) {
            final RadFilterUserParamDef parameter =
                    new RadFilterUserParamDef(Id.Factory.newInstance(EDefinitionIdPrefix.USER_FILTER_PARAMETER),
                    null, targetProperty, null, null, null, allowPersistentValue);
            parameter.wasModified = true;
            return parameter;
        }

        public static RadFilterUserParamDef newInstance(final ISqmlColumnDef column, final boolean allowPersistentValue) {
            final RadFilterUserParamDef parameter =
                    new RadFilterUserParamDef(Id.Factory.newInstance(EDefinitionIdPrefix.USER_FILTER_PARAMETER),
                    column.getShortName(), null, column, null, null, allowPersistentValue);
            parameter.wasModified = true;
            return parameter;
        }

        public static RadFilterUserParamDef newInstance(final EValType valType, final boolean allowPersistentValue) {
            final RadFilterUserParamDef parameter =
                    new RadFilterUserParamDef(Id.Factory.newInstance(EDefinitionIdPrefix.USER_FILTER_PARAMETER),
                    "Filter param", valType, false, null, null, null, null, null, allowPersistentValue);
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
                if (classDef.isPropertyDefExistsById(basePropertyId)) {
                    final RadPropertyDef baseProperty = classDef.getPropertyDefById(basePropertyId);
                    return new RadFilterUserParamDef(propertyBasedParam.getId(),
                            propertyBasedParam.getTitle(),
                            baseProperty,
                            null,
                            isMandatory,
                            defaultVal,
                            allowPersistentValue);
                } else {
                    return new RadFilterUserParamDef(propertyBasedParam.getId(),
                            propertyBasedParam.getTitle(),
                            classDef.getId(),
                            basePropertyId,
                            isMandatory,
                            defaultVal,
                            allowPersistentValue,
                            environment);
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
                final ValAsStr defaultVal = customFilterParam.isSetDefaultVal() ? ValAsStr.Factory.loadFrom(customFilterParam.getDefaultVal()) : null;
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
                        allowPersistentValue);
            }
        }
    }
    private String userTitle, nullValueTitle;//заголовок пользовательского параметра
    private EValType type = null;
    private ValAsStr defaultVal;
    private Boolean mandatory;
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
            final boolean allowPersistentValue) {
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
                false,//storeHistory
                false,//isCustomDialog
                null,//customDialogId
                false,//isCustomEditOnly
                null,//editMaskStr
                null,//nullStringId
                false,//isMemo
                //Parent selector
                null,// referencedClassId,
                null);
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
            final IClientEnvironment environment) {
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
                null);
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
                null);
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
            final boolean allowPersistentValue) {
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
                false,//storeHistory
                false,//isCustomDialog
                null,//customDialogId
                false,//isCustomEditOnly
                null,//editMaskStr
                null,//nullStringId
                false,//isMemo
                //Parent selector
                referencedClassId,// referencedClassId,
                parentSelectorPresentationId);
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
    public String getTitle() {
        if (!isValid()) {
            if (targetId == null) {
                return "??? " + targetColumnId.toString() + " ???";
            } else {
                return "??? " + targetId.toString() + " ???";
            }
        } else if (userTitle == null) {
            return target == null ? getName() : target.getTitle();
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
        if (!isValid()) {
            return new EditMaskNone();
        } else if (target != null) {
            return target.getEditMask();
        } else if (getSqmlColumn() != null) {
            return EditMask.newCopy(getSqmlColumn().getEditMask());
        }
        final EditMask resultMask = mask == null ? EditMask.newInstance(getType()) : EditMask.newCopy(mask);
        if (nullValueTitle != null) {
            resultMask.setNoValueStr(nullValueTitle);
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
        if (!Utils.equals(nullTitle, getNullString())) {
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
        if (!Utils.equals(value, defaultVal)) {
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
        if (!Utils.equals(classId, referencedClassId) || !Utils.equals(presentationId, parentSelectorPresentationId)) {
            referencedClassId = classId;
            parentSelectorPresentationId = presentationId;
            wasModified = true;
        }
    }

    @Override
    public void setValType(final EValType valType, final EditMask editMask, final boolean isMandatory, final String nullTitle) {
        if (valType == null) {
            throw new NullPointerException("target property must be not null");
        }
        if (valType != getType() || !editMaskEquals(editMask, mask) || mandatory == null || isMandatory != mandatory.booleanValue()
                || !Utils.equals(nullTitle, nullValueTitle) || target != null) {
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
            copy = new RadFilterUserParamDef(getId(), userTitle, targetOwnerId, targetColumnId, mandatory, defaultVal, allowPersistentValue, environment);
        } else if (target != null || getSqmlColumn() != null) {
            copy = new RadFilterUserParamDef(getId(), userTitle, target, getSqmlColumn(), mandatory, defaultVal, allowPersistentValue);
        } else {
            copy = new RadFilterUserParamDef(getId(), userTitle,
                    type,
                    isMandatory(),
                    mask != null ? EditMask.newCopy(mask) : null,
                    nullValueTitle,
                    defaultVal,
                    referencedClassId,
                    parentSelectorPresentationId,
                    allowPersistentValue);
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
            parameter.setPropertyId(targetId);
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
