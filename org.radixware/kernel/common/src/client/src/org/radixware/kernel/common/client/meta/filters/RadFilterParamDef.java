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
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.KernelIcon;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.RadParentRefPropertyDef;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskConstSet;
import org.radixware.kernel.common.client.meta.mask.EditMaskRef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameterPersistentValue;
import org.radixware.kernel.common.client.utils.ClientValueFormatter;
import org.radixware.kernel.common.client.views.IPropEditorDialog;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EEditPossibility;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.enums.EPropertyValueStorePossibility;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.xscml.Sqml;


public class RadFilterParamDef extends RadPropertyDef implements ISqmlParameter {

    protected RadPropertyDef target;//инициализировать в конструкторе нельзя - может превести к рекурсии
    private final Id targetTableId, targetPropertyId;
    private long changeValueTimestamp = System.currentTimeMillis();
    protected Id referencedClassId;
    protected Id parentSelectorPresentationId;
    protected Boolean useDropDownList;
    
    public RadFilterParamDef(
            final Id id, //собственный идентификатор
            final String name, //собственное имя
            final Id titleId, //если перекрыт заголовок - идентификатор строки
            final Id ownerId, //идентификатор класса (aec), в котором определен фильтр
            final Id tableId,
            final Id propertyId,
            final EValType type,
            final Id constSetId,
            final ValAsStr defaultValue,
            //Editing
            final boolean isMandatory,
            final boolean storeHistory,
            final boolean isCustomDialog,
            final Id customDialogId,
            final boolean isCustomEditOnly,
            final EditMask editMask,
            final Id nullStringId,
            final boolean isMemo,
            //Parent selector
            final Id referencedClassId,
            final Id parentSelectorPresentationId) {
        this(id, //собственный идентификатор
             name, //собственное имя
             titleId, //если перекрыт заголовок - идентификатор строки
             ownerId, //идентификатор класса (aec), в котором определен фильтр
             tableId,
             propertyId,
             type,
             constSetId,
             defaultValue,
            //Editing
            isMandatory,
            storeHistory,
            isCustomDialog,
            customDialogId,
            isCustomEditOnly,
            editMask,
            nullStringId,
            isMemo,
            //Parent selector
            referencedClassId,
            parentSelectorPresentationId,
            null);
    }    

    public RadFilterParamDef(
            final Id id, //собственный идентификатор
            final String name, //собственное имя
            final Id titleId, //если перекрыт заголовок - идентификатор строки
            final Id ownerId, //идентификатор класса (aec), в котором определен фильтр
            final Id tableId,
            final Id propertyId,
            final EValType type,
            final Id constSetId,
            final ValAsStr defaultValue,
            //Editing
            final boolean isMandatory,
            final boolean storeHistory,
            final boolean isCustomDialog,
            final Id customDialogId,
            final boolean isCustomEditOnly,
            final EditMask editMask,
            final Id nullStringId,
            final boolean isMemo,
            //Parent selector
            final Id referencedClassId,
            final Id parentSelectorPresentationId,
            final Boolean useDropDownList) {

        this(id,
            name,
            titleId,
            ownerId,
            tableId,
            propertyId,
            type,
            constSetId,
            defaultValue,
            //Editing
            isMandatory,
            isMandatory,//arrayItemIsMandatory
            storeHistory,
            isCustomDialog,
            customDialogId,
            isCustomEditOnly,
            editMask,
            nullStringId,
            nullStringId,//arrayItemNullStringId
            null,//emptyArrayStringId (onlyForArrayProperties)
            false,//isDuplicatesEnabled (onlyForArrayProperties)
            -1,//minArrayItemsCount
            -1,//maxArrayItemsCount
            1,//firstArrayItemIndex
            isMemo,            
            //Parent selector
            referencedClassId,
            parentSelectorPresentationId,
            useDropDownList);
    }    
    
    public RadFilterParamDef(
            final Id id, //собственный идентификатор
            final String name, //собственное имя
            final Id titleId, //если перекрыт заголовок - идентификатор строки
            final Id ownerId, //идентификатор класса (aec), в котором определен фильтр
            final Id tableId,
            final Id propertyId,
            final EValType type,
            final Id constSetId,
            final ValAsStr defaultValue,
            //Editing
            final boolean isMandatory,
            final boolean arrayItemIsMandatory,
            final boolean storeHistory,
            final boolean isCustomDialog,
            final Id customDialogId,
            final boolean isCustomEditOnly,
            final EditMask editMask,
            final Id nullStringId,
            final Id arrayItemNullStringId,
            final Id emptyArrayStringId,// (onlyForArrayProperties)
            final boolean isDuplicatesEnabled,// (onlyForArrayProperties)
            final int minArrayItemsCount,
            final int maxArrayItemsCount,
            final int firstArrayItemIndex,
            final boolean isMemo,
            //Parent selector
            final Id referencedClassId,
            final Id parentSelectorPresentationId,
            final Boolean useDropDownList) {

        super(id,
                name,
                titleId,//titleId
                null,//hintId
                ownerId,//titleOwnerId

                type,
                EPropNature.SQL_CLASS_PARAMETER,
                propertyId == null ? 0L : 0xFFFFFF,
                null,//ownerEntityId,
                null,//origPropId,
                constSetId,
                false,//isReadSeparately

                false,//isInheritable
                null, //valInheritanceMark
                defaultValue,//initValue

                EEditPossibility.ALWAYS,
                ERuntimeEnvironmentType.COMMON_CLIENT,
                isMandatory,
                arrayItemIsMandatory,
                false,//storeHistory
                isCustomDialog,
                customDialogId,
                isCustomEditOnly,
                editMask,
                nullStringId,
                arrayItemNullStringId,
                emptyArrayStringId,
                isDuplicatesEnabled,
                minArrayItemsCount,
                maxArrayItemsCount,
                firstArrayItemIndex,
                isMemo,
                false, 
                EPropertyValueStorePossibility.NONE//canBeUsedInSorting
                );
        targetTableId = tableId;
        targetPropertyId = propertyId;
        this.referencedClassId = referencedClassId;
        this.parentSelectorPresentationId = parentSelectorPresentationId;
        this.useDropDownList = useDropDownList;
    }    

    public boolean isPredefined() {
        return true;
    }

    protected RadPropertyDef getTargetProperty() {
        if (target == null && targetTableId != null && targetPropertyId != null) {
            final RadClassPresentationDef classDef = getDefManager().
                    getClassPresentationDef(targetTableId);
            target = classDef.getPropertyDefById(targetPropertyId);
        }
        return target;
    }

    @Override
    public Id getBasePropertyId() {
        return getTargetProperty() == null ? null : getTargetProperty().getId();
    }

    @Override
    public boolean hasTitle() {
        if (inheritanceMask.isTitleInherited()) {
            if (getBasePropertyId() != null) {
                return getTargetProperty().hasTitle();
            } else if (type == EValType.PARENT_REF || type == EValType.ARR_REF) {
                final RadSelectorPresentationDef presentation = getParentSelectorPresentation();
                if (presentation != null) {
                    final RadClassPresentationDef classDef = presentation.getClassPresentation();
                    return type == EValType.ARR_REF ? classDef.hasGroupTitle() : classDef.hasObjectTitle();
                }
            }
        }
        return super.hasTitle();
    }    

    @Override
    public String getTitle(final IClientEnvironment environment) {
        if (inheritanceMask.isTitleInherited()) {
            getInheritedTitle(environment);
        }
        return super.getTitle(environment);
    }        

    @Override
    public String getTitle() {
        if (inheritanceMask.isTitleInherited()) {
            getInheritedTitle(IClientEnvironment.Locator.getEnvironment());
        }
        return super.getTitle();
    }
    
    private String getInheritedTitle(final IClientEnvironment environment){
        if (getBasePropertyId() != null) {
            return getTargetProperty().getTitle(environment);
        } else if (type == EValType.PARENT_REF || type == EValType.ARR_REF) {
            final RadSelectorPresentationDef presentation = getParentSelectorPresentation();
            if (presentation != null) {
                final RadClassPresentationDef classDef = presentation.getClassPresentation();
                final String referencedClassObjectTitle = type == EValType.ARR_REF ? classDef.getGroupTitle() : classDef.getObjectTitle();
                if (environment==null){
                    return referencedClassObjectTitle;
                }else{
                    return ClientValueFormatter.decapitalizeIfNecessary(environment, referencedClassObjectTitle);
                }
            }
        }
        return super.getTitle(environment);
    }

    @Override
    public RadEnumPresentationDef getConstSet() {
        return getBasePropertyId() != null ? getTargetProperty().getConstSet() : super.getConstSet();
    }

    @Override
    public IPropEditorDialog getPropEditorDialog(IClientEnvironment env) {
        if (inheritanceMask.isEditingInherited() && getBasePropertyId() != null) {
            return getTargetProperty().getPropEditorDialog(env);
        }
        return super.getPropEditorDialog(env);
    }

    @Override
    public EditMask getEditMask() {
        if (type == EValType.PARENT_REF || type == EValType.ARR_REF) {
            final EditMaskRef editMask = new EditMaskRef(getParentSelectorPresentationId());
            if (useDropDownList==null){
                final RadPropertyDef prop = getTargetProperty();
                if (prop!=null 
                    && prop.getEditMask() instanceof EditMaskRef
                    && ((EditMaskRef)prop.getEditMask()).isUseDropDownList()){
                    editMask.setUseDropDownList(true);
                }
            }else if (Boolean.TRUE==useDropDownList){
                editMask.setUseDropDownList(true);
            }
            return editMask;
        }   
        if (inheritanceMask.isEditingInherited() && getBasePropertyId() != null) {
            return getTargetProperty().getEditMask();
        }
        return super.getEditMask();
    }

    @Override
    public EEditPossibility getEditPossibility() {
        return EEditPossibility.ALWAYS;
    }

    @Override
    public String getNullString() {
        if (inheritanceMask.isEditingInherited() && getBasePropertyId() != null) {
            return getTargetProperty().getNullString();
        }
        return super.getNullString();
    }

    @Override
    public boolean customDialog() {
        if (inheritanceMask.isEditingInherited() && getBasePropertyId() != null) {
            return getTargetProperty().customDialog();
        }
        return super.customDialog();
    }

    @Override
    public boolean isCustomEditOnly() {
        if (inheritanceMask.isEditingInherited() && getBasePropertyId() != null) {
            return getTargetProperty().isCustomEditOnly();
        }
        return super.isCustomEditOnly();
    }

    @Override
    public boolean isMandatory() {
        if (inheritanceMask.isEditingInherited() && getBasePropertyId() != null) {
            return getTargetProperty().isMandatory();
        }
        return super.isMandatory();
    }

    @Override
    public boolean isMemo() {
        if (inheritanceMask.isEditingInherited() && getBasePropertyId() != null) {
            return getTargetProperty().isMemo();
        }
        return super.isMemo();
    }

    @Override
    public boolean storeHistory() {
        if (inheritanceMask.isEditingInherited() && getBasePropertyId() != null) {
            return getTargetProperty().storeHistory();
        }
        return super.storeHistory();
    }

    @Override
    public String getHint() {
        if (inheritanceMask.isToolTipInherited() && getBasePropertyId() != null) {
            return getTargetProperty().getHint();
        }
        return super.getHint();
    }

    @Override
    public Id getReferencedTableId(){
        if (getBasePropertyId()==null){
            final RadSelectorPresentationDef selectorPresentation = getParentSelectorPresentation();
            return selectorPresentation==null ? null : selectorPresentation.getTableId();
        }
        else if (getTargetProperty() instanceof RadParentRefPropertyDef){
            return ((RadParentRefPropertyDef)getTargetProperty()).getReferencedTableId();
        }
        return null;
	}

    public RadSelectorPresentationDef getParentSelectorPresentation() {
        if (inheritanceMask.isParentSelectorInherited() && getBasePropertyId() != null) {
            return ((RadParentRefPropertyDef) getTargetProperty()).getParentSelectorPresentation();
        }
        if (parentSelectorPresentationId == null) {
            if (referencedClassId != null) {
                final RadClassPresentationDef presentationClass = getDefManager().getClassPresentationDef(referencedClassId);
                return presentationClass.getDefaultSelectorPresentation();
            } else {
                return null;
            }
        } else {
            return getDefManager().getSelectorPresentationDef(parentSelectorPresentationId);
        }
    }

    @Override
    public String getDescription() {
        final String desc = getApplication().getMessageProvider().translate("DefinitionDescribtion", "filter parameter %s owner definition #%s");
        return String.format(desc, super.getDescription(), getOwnerClassId());
    }

    @Override
    public String getShortName() {
        return getName();
    }

    @Override
    public String getFullName() {
        return getName();
    }

    @Override
    public String getDisplayableText(EDefinitionDisplayMode mode) {
        return mode == EDefinitionDisplayMode.SHOW_TITLES ? getTitle() : getShortName();
    }

    @Override
    public ClientIcon getIcon() {
        return KernelIcon.getInstance(AdsDefinitionIcon.SQL_CLASS_TYPIFIED_PARAMETER);
    }

    @Override
    public Id getEnumId() {
        return getConstSet() == null ? null : getConstSet().getId();
    }

    @Override
    public Id getParentSelectorPresentationClassId() {
        return getParentSelectorPresentation() == null ? null : getParentSelectorPresentation().getOwnerClassId();
    }

    @Override
    public Id getParentSelectorPresentationId() {
        return getParentSelectorPresentation() == null ? null : getParentSelectorPresentation().getId();
    }

    @Override
    public Sqml getParentSelectorAdditionalCondition() {
        return null;
    }        

    @Override
    public boolean canHavePersistentValue() {
        return false;
    }

    @Override
    public ISqmlParameterPersistentValue getPersistentValue() {
        return null;
    }

    @Override
    public void setPersistentValue(ISqmlParameterPersistentValue value) {
        throw new IllegalUsageError(String.format("Parameter %s #%s can not have persistent value", getName(), getId().toString()));
    }

    @Override
    public String getModuleName() {
        return "";
    }

    @Override
    public Id[] getIdPath() {
        throw new UnsupportedOperationException("Not supported yet.");
    }        
    
    final void updateChangeValueTimestamp(){
        changeValueTimestamp = System.currentTimeMillis();
    }
    
    final long getChangeValueTimestamp(){
        return changeValueTimestamp;
    }
    
    public static void writeToXml(final ISqmlParameter parameter, final org.radixware.schemas.groupsettings.FilterParameters parameters){
        final org.radixware.schemas.groupsettings.FilterParameters.Parameter param = parameters.addNewParameter();
        final org.radixware.schemas.groupsettings.CustomFilterParameter xmlParameter =
                param.addNewCustomParameter();
        xmlParameter.setId(parameter.getId());
        xmlParameter.setDefinitionType(EDefType.FILTER_PARAM);
        xmlParameter.setTitle(parameter.getTitle());
        xmlParameter.setValType(parameter.getType());
        xmlParameter.setNotNull(parameter.isMandatory());
        final EditMask mask = parameter.getEditMask();
        if (mask != null && mask.getType() != null) {
            mask.writeToXml(xmlParameter.addNewEditMask());
            if (mask instanceof EditMaskConstSet){
                final Id enumId = ((EditMaskConstSet)mask).getEnumId();
                if (enumId!=null){
                    xmlParameter.setEnumId(enumId);
                }
            }
            if (mask instanceof EditMaskRef){
                xmlParameter.setUseDropDownList(((EditMaskRef)mask).isUseDropDownList());
            }
        }
        final String nullValueTitle = parameter.getNullString();
        if (nullValueTitle != null) {
            xmlParameter.setNullTitle(nullValueTitle);
        }
        final ValAsStr defaultVal = parameter.getInitialVal();
        if (defaultVal != null) {
            xmlParameter.setDefaultVal(defaultVal.toString());
        }  
        final Id referencedTableId = parameter.getReferencedTableId();
        if (referencedTableId != null) {
            xmlParameter.setReferencedClassId(Id.Factory.changePrefix(referencedTableId, EDefinitionIdPrefix.ADS_ENTITY_CLASS));
        }
        final Id parentSelPresId = parameter.getParentSelectorPresentationId();
        if (parentSelPresId!=null){
            xmlParameter.setParentSelectorPresentationId(parentSelPresId);
        }
    }
}