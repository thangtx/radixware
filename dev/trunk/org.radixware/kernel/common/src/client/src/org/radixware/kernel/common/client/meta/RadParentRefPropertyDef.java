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
package org.radixware.kernel.common.client.meta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskRef;
import org.radixware.kernel.common.client.types.Restrictions;
import org.radixware.kernel.common.client.utils.ClientValueFormatter;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EEditPossibility;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.enums.EPropertyValueStorePossibility;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;

public class RadParentRefPropertyDef extends RadPropertyDef {

    private final Id referencedClassId;
    private final Id referencedTableId;
    private final Id[] objectEditorPresentationIds;
    private final Id[] objectCreationPresentationIds;
    private final Id parentSelectorPresentationId;
    private final Restrictions parentSelectorRestrictions;
    private final Restrictions parentEditorRestrictions;
    private final boolean useDropDownList;
    private RadParentRefPropertyDef finalProp;
    private EditMaskRef inheritedEditMask;
    private EditMaskRef ownEditMask;
    private final Id ownerClassId;
    private final boolean isAutoSortInstantiatableClasses;
//Id, String, null, Id, EValType, EPropNature, int, null, null, boolean, boolean, null, null, EEditPossibility, boolean, boolean, boolean, null, boolean, null, null, boolean, Id, Id, null, null, null, int, int

    public List<Id> getObjectCreationPresentationIds() {
        return objectCreationPresentationIds == null ? Collections.<Id>emptyList() : Arrays.<Id>asList(objectCreationPresentationIds);
    }

    public RadParentRefPropertyDef(
            final Id id,
            final String name,
            final Id titleId,
            final Id hintId,
            final Id titleOwnerId,
            final EValType type,
            final EPropNature nature,
            final long inheritanceMask,
            final Id ownerEntityId,//Для UserProp, ParentProp, DetailProp – сущность, которой принадлежит свойство
            final Id origPropId,//Для ParentProp, DetailProp – ид. оригинального свойства

            final boolean isInheritable,
            final ValAsStr valInheritableMark,
            final ValAsStr initVal,
            final EEditPossibility editPossibility,
            final ERuntimeEnvironmentType editingEnvironment,
            final boolean mandatory,
            final boolean arrayItemIsMandatory,//onlyForArrayProperties
            final boolean storeHistory,
            final boolean customDialog,
            final Id customDialogId,
            final boolean customEditOnly,
            final EditMask editMask,
            final Id nullStringId,
            final Id arrayItemNullStringId,//onlyForArrayProperties
            final Id emptyArrayStringId,//onlyForArrayProperties
            final boolean isDuplicatesEnabled,//onlyForArrayProperties
            final int minArrayItemsCount,//onlyForArrayProperties
            final int maxArrayItemsCount,//onlyForArrayProperties
            final int firstArrayItemIndex,//onlyForArrayProperties            
            final Id referencedClassId,
            final Id referencedTableId,
            final Id[] objectEditorPresentationIds,
            final Id[] objectCreationPresentationIds,
            //								с учетом презентации селектора по умолчанию:
            final Id parentSelectorPresentationId,
            final long parentSelectorRestrictionsMask,
            final long parentEditorRestrictionsMask) {
        this(id,
                name,
                titleId,
                hintId,
                titleOwnerId,
                type,
                nature,
                inheritanceMask,
                ownerEntityId,
                origPropId,
                isInheritable,
                valInheritableMark,
                initVal,
                editPossibility,
                editingEnvironment,
                mandatory,
                arrayItemIsMandatory,
                storeHistory,
                customDialog,
                customDialogId,
                customEditOnly,
                editMask,
                nullStringId,
                arrayItemNullStringId,
                emptyArrayStringId,
                isDuplicatesEnabled,
                minArrayItemsCount,
                maxArrayItemsCount,
                firstArrayItemIndex,
                referencedClassId,
                referencedTableId,
                objectEditorPresentationIds,
                objectCreationPresentationIds,
                parentSelectorPresentationId,
                parentSelectorRestrictionsMask,
                parentEditorRestrictionsMask,
                false,
                false,
                false);
    }

    public RadParentRefPropertyDef(
            final Id id,
            final String name,
            final Id titleId,
            final Id hintId,
            final Id titleOwnerId,
            final EValType type,
            final EPropNature nature,
            final long inheritanceMask,
            final Id ownerEntityId,//Для UserProp, ParentProp, DetailProp – сущность, которой принадлежит свойство
            final Id origPropId,//Для ParentProp, DetailProp – ид. оригинального свойства

            final boolean isInheritable,
            final ValAsStr valInheritableMark,
            final ValAsStr initVal,
            final EEditPossibility editPossibility,
            final ERuntimeEnvironmentType editingEnvironment,
            final boolean mandatory,
            final boolean arrayItemIsMandatory,//onlyForArrayProperties
            final boolean storeHistory,
            final boolean customDialog,
            final Id customDialogId,
            final boolean customEditOnly,
            final EditMask editMask,
            final Id nullStringId,
            final Id arrayItemNullStringId,//onlyForArrayProperties
            final Id emptyArrayStringId,//onlyForArrayProperties
            final boolean isDuplicatesEnabled,//onlyForArrayProperties
            final int minArrayItemsCount,//onlyForArrayProperties
            final int maxArrayItemsCount,//onlyForArrayProperties
            final int firstArrayItemIndex,//onlyForArrayProperties            
            final Id referencedClassId,
            final Id referencedTableId,
            final Id[] objectEditorPresentationIds,
            final Id[] objectCreationPresentationIds,
            //								с учетом презентации селектора по умолчанию:
            final Id parentSelectorPresentationId,
            final long parentSelectorRestrictionsMask,
            final long parentEditorRestrictionsMask,
            final boolean isDeprecated) {
        this(id,
                name,
                titleId,
                hintId,
                titleOwnerId,
                type,
                nature,
                inheritanceMask,
                ownerEntityId,
                origPropId,
                isInheritable,
                valInheritableMark,
                initVal,
                editPossibility,
                editingEnvironment,
                mandatory,
                arrayItemIsMandatory,
                storeHistory,
                customDialog,
                customDialogId,
                customEditOnly,
                editMask,
                nullStringId,
                arrayItemNullStringId,
                emptyArrayStringId,
                isDuplicatesEnabled,
                minArrayItemsCount,
                maxArrayItemsCount,
                firstArrayItemIndex,
                referencedClassId,
                referencedTableId,
                objectEditorPresentationIds,
                objectCreationPresentationIds,
                parentSelectorPresentationId,
                parentSelectorRestrictionsMask,
                parentEditorRestrictionsMask,
                isDeprecated,
                false,
                false);
    }
    
    public RadParentRefPropertyDef(
            final Id id,
            final String name,
            final Id titleId,
            final Id hintId,
            final Id titleOwnerId,
            final EValType type,
            final EPropNature nature,
            final long inheritanceMask,
            final Id ownerEntityId,//Для UserProp, ParentProp, DetailProp – сущность, которой принадлежит свойство
            final Id origPropId,//Для ParentProp, DetailProp – ид. оригинального свойства

            final boolean isInheritable,
            final ValAsStr valInheritableMark,
            final ValAsStr initVal,
            final EEditPossibility editPossibility,
            final ERuntimeEnvironmentType editingEnvironment,
            final boolean mandatory,
            final boolean arrayItemIsMandatory,//onlyForArrayProperties
            final boolean storeHistory,
            final boolean customDialog,
            final Id customDialogId,
            final boolean customEditOnly,
            final EditMask editMask,
            final Id nullStringId,
            final Id arrayItemNullStringId,//onlyForArrayProperties
            final Id emptyArrayStringId,//onlyForArrayProperties
            final boolean isDuplicatesEnabled,//onlyForArrayProperties
            final int minArrayItemsCount,//onlyForArrayProperties
            final int maxArrayItemsCount,//onlyForArrayProperties
            final int firstArrayItemIndex,//onlyForArrayProperties            
            final Id referencedClassId,
            final Id referencedTableId,
            final Id[] objectEditorPresentationIds,
            final Id[] objectCreationPresentationIds,
            //								с учетом презентации селектора по умолчанию:
            final Id parentSelectorPresentationId,
            final long parentSelectorRestrictionsMask,
            final long parentEditorRestrictionsMask,
            final boolean isDeprecated,
            final boolean isAutoSortInstantiatableClasses) {
        this(id,
            name,
            titleId,
            hintId,
            titleOwnerId,
            type,
            nature,
            inheritanceMask,
            ownerEntityId,
            origPropId,
            isInheritable,
            valInheritableMark,
            initVal,
            editPossibility,
            editingEnvironment,
            mandatory,
            arrayItemIsMandatory,
            storeHistory,
            customDialog,
            customDialogId,
            customEditOnly,
            editMask,
            nullStringId,
            arrayItemNullStringId,
            emptyArrayStringId,
            isDuplicatesEnabled,
            minArrayItemsCount,
            maxArrayItemsCount,
            firstArrayItemIndex,
            referencedClassId,
            referencedTableId,
            objectEditorPresentationIds,
            objectCreationPresentationIds,
            parentSelectorPresentationId,
            parentSelectorRestrictionsMask,
            parentEditorRestrictionsMask,
            isDeprecated,
            isAutoSortInstantiatableClasses,
            false);       
    }    
    
    public RadParentRefPropertyDef(
            final Id id,
            final String name,
            final Id titleId,
            final Id hintId,
            final Id titleOwnerId,
            final EValType type,
            final EPropNature nature,
            final long inheritanceMask,
            final Id ownerEntityId,//Для UserProp, ParentProp, DetailProp – сущность, которой принадлежит свойство
            final Id origPropId,//Для ParentProp, DetailProp – ид. оригинального свойства

            final boolean isInheritable,
            final ValAsStr valInheritableMark,
            final ValAsStr initVal,
            final EEditPossibility editPossibility,
            final ERuntimeEnvironmentType editingEnvironment,
            final boolean mandatory,
            final boolean arrayItemIsMandatory,//onlyForArrayProperties
            final boolean storeHistory,
            final boolean customDialog,
            final Id customDialogId,
            final boolean customEditOnly,
            final EditMask editMask,
            final Id nullStringId,
            final Id arrayItemNullStringId,//onlyForArrayProperties
            final Id emptyArrayStringId,//onlyForArrayProperties
            final boolean isDuplicatesEnabled,//onlyForArrayProperties
            final int minArrayItemsCount,//onlyForArrayProperties
            final int maxArrayItemsCount,//onlyForArrayProperties
            final int firstArrayItemIndex,//onlyForArrayProperties            
            final Id referencedClassId,
            final Id referencedTableId,
            final Id[] objectEditorPresentationIds,
            final Id[] objectCreationPresentationIds,
            //								с учетом презентации селектора по умолчанию:
            final Id parentSelectorPresentationId,
            final long parentSelectorRestrictionsMask,
            final long parentEditorRestrictionsMask,
            final boolean isDeprecated,
            final boolean isAutoSortInstantiatableClasses,
            final boolean useDropDownList) {
        super(id,
                name,
                titleId,
                hintId,
                titleOwnerId,
                type,
                nature,
                inheritanceMask,
                ownerEntityId,
                origPropId,
                null,//constSetId
                false,//readSeparately

                isInheritable,
                valInheritableMark,
                initVal,
                editPossibility,
                editingEnvironment,
                mandatory,
                arrayItemIsMandatory,
                storeHistory,
                customDialog,
                customDialogId,
                customEditOnly,
                new EditMaskRef(parentSelectorPresentationId, objectEditorPresentationIds, useDropDownList),//editmask
                nullStringId,
                arrayItemNullStringId,
                emptyArrayStringId,
                isDuplicatesEnabled,
                minArrayItemsCount,
                maxArrayItemsCount,
                firstArrayItemIndex,
                false,//isMemo
                false,//canBeUsedInSorting
                EPropertyValueStorePossibility.NONE,
                isDeprecated);
        ownerClassId = titleOwnerId;
        this.referencedClassId = referencedClassId;
        this.referencedTableId = referencedTableId;
        if (this.inheritanceMask.isParentSelectorInherited()) {
            this.parentSelectorPresentationId = null;
        } else {
            this.parentSelectorPresentationId = parentSelectorPresentationId;
        }
        this.useDropDownList = useDropDownList;
        parentSelectorRestrictions = Restrictions.Factory.newInstance(ERestriction.fromBitField(parentSelectorRestrictionsMask), null);
        parentEditorRestrictions = Restrictions.Factory.newInstance(ERestriction.fromBitField(parentEditorRestrictionsMask), null);

        this.objectEditorPresentationIds = objectEditorPresentationIds;
        this.objectCreationPresentationIds = objectCreationPresentationIds;
        this.isAutoSortInstantiatableClasses = isAutoSortInstantiatableClasses;          
    }    

    private RadParentRefPropertyDef getFinalParentRefProperty() {
        if (finalProp == null) {
            if (inheritanceMask.isParentSelectorInherited()) {
                if (isFormProperty()) {
                    final RadFormDef owner = getApplication().getDefManager().getFormDef(getOwnerClassId());
                    for (RadFormDef base = owner.getBaseFormDef(); base != null && finalProp == null; base = base.getBaseFormDef()) {
                        if (base.isPropertyDefExistsById(getId())) {
                            finalProp = (RadParentRefPropertyDef) base.getPropertyDefById(getId());
                        }
                    }
                } else if (getNature() == EPropNature.SQL_CLASS_PARAMETER
                        || getNature() == EPropNature.VIRTUAL
                        || isReportProperty()) {
                    finalProp = this;
                } else {
                    final RadClassPresentationDef owner = getApplication().getDefManager().getClassPresentationDef(getOwnerClassId());
                    for (RadClassPresentationDef parent = owner.getAncestorClassPresentationDef(); parent != null && finalProp == null; parent = parent.getAncestorClassPresentationDef()) {
                        if (parent.isPropertyDefExistsById(getId())) {
                            final RadParentRefPropertyDef basePropRef =
                                    (RadParentRefPropertyDef) parent.getPropertyDefById(getId());
                            if (!basePropRef.inheritanceMask.isParentSelectorInherited()) {
                                finalProp = basePropRef;
                            }
                        }
                    }
                }
                if (finalProp == null) {
                    finalProp = getNature() == EPropNature.PARENT_PROP ? (RadParentRefPropertyDef) getOrigProp() : this;
                }
            } else {
                finalProp = this;
            }
        }
        return finalProp;
    }

    @Override
    public boolean hasTitle() {
        if (inheritanceMask.isTitleInherited()) {
            if (getNature()==EPropNature.PARENT_PROP){
                return getOrigProp().hasTitle();
            }else if (isFormProperty()) {
                final RadFormDef owner = getApplication().getDefManager().getFormDef(ownerClassId);
                if (owner.getBaseFormDef() == null
                        || !owner.getBaseFormDef().isPropertyDefExistsById(getId())) {//in this case inherit title from referenced class
                    final RadClassPresentationDef classDef = getReferencedClassPresentation();
                    if (classDef == null) {
                        return super.hasTitle();
                    } else {
                        return type.isArrayType() ? classDef.hasGroupTitle() : classDef.hasObjectTitle();
                    }
                }
            } else if (getNature() == EPropNature.SQL_CLASS_PARAMETER
                    || getNature() == EPropNature.VIRTUAL
                    || isReportProperty()) {
                final RadClassPresentationDef classDef = getReferencedClassPresentation();
                if (classDef == null) {
                    return super.hasTitle();
                } else {
                    return type.isArrayType() ? classDef.hasGroupTitle() : classDef.hasObjectTitle();
                }
            } else {
                final RadClassPresentationDef owner = getApplication().getDefManager().getClassPresentationDef(ownerClassId);
                if (owner.getAncestorClassPresentationDef() == null
                        || !owner.getAncestorClassPresentationDef().isPropertyDefExistsById(getId())) {//in this case inherit title from referenced class
                    final RadClassPresentationDef classDef = getReferencedClassPresentation();
                    if (classDef == null) {
                        return super.hasTitle();
                    } else {
                        return type.isArrayType() ? classDef.hasGroupTitle() : classDef.hasObjectTitle();
                    }
                }
            }
        }
        return super.hasTitle();
    }

    @Override
    public EditMaskRef getEditMask() {
        final RadParentRefPropertyDef finalProperty = getFinalParentRefProperty();
        if (finalProperty == this) {
            if (parentSelectorPresentationId == null) {
                if (inheritedEditMask == null) {
                    RadSelectorPresentationDef defaultSelector = null;
                    try {
                        final RadClassPresentationDef presentationClass =
                                getApplication().getDefManager().getClassPresentationDef(referencedClassId);
                        defaultSelector = presentationClass.getDefaultSelectorPresentation();
                    } catch (DefinitionError error) {
                        //it may be unsupported environment
                        Logger.getLogger(getClass().getName()).log(Level.FINE, error.getMessage(), error);
                    }
                    final Id selectorPresentationId = defaultSelector == null ? null : defaultSelector.getId();
                    inheritedEditMask = new EditMaskRef(selectorPresentationId, objectEditorPresentationIds);
                    inheritedEditMask.setNoValueStr(getNullString());
                    inheritedEditMask.setArrayItemNoValueStr(getArrayItemNullString());
                    inheritedEditMask.setEmptyArrayString(getEmptyArrayString());
                    if (useDropDownList){
                        inheritedEditMask.setUseDropDownList(true);
                    }                    
                }
                return inheritedEditMask;
            } else {
                if (ownEditMask == null) {
                    ownEditMask = new EditMaskRef(parentSelectorPresentationId, objectCreationPresentationIds);                    
                    ownEditMask.setNoValueStr(getNullString());
                    ownEditMask.setArrayItemNoValueStr(getArrayItemNullString());
                    ownEditMask.setEmptyArrayString(getEmptyArrayString());
                    if (useDropDownList){
                        ownEditMask.setUseDropDownList(true);
                    }
                }
                return ownEditMask;
            }
        } else {
            return finalProperty.getEditMask();
        }
    }    
    
    @Override
    public String getTitle(final IClientEnvironment environment){
        if (inheritanceMask.isTitleInherited()) {
            return getInheritedTitle(environment);
        } else {
            return super.getTitle();
        }        
    }    

    @Override
    public String getTitle() {
        if (inheritanceMask.isTitleInherited()) {
            return getInheritedTitle(IClientEnvironment.Locator.getEnvironment());
        } else {
            return super.getTitle();
        }
    }
    
    private String getInheritedTitle(final IClientEnvironment environment){
        if (getNature()==EPropNature.PARENT_PROP){
            return getOrigProp().getTitle();
        } else if (isFormProperty()) {        
            final RadFormDef owner = getApplication().getDefManager().getFormDef(ownerClassId);
            if (owner.getBaseFormDef() == null
                    || !owner.getBaseFormDef().isPropertyDefExistsById(getId())) {//in this case inherit title from referenced class
                return getReferencedClassObjectTitle(environment);
            }
        } else if (getNature() == EPropNature.SQL_CLASS_PARAMETER
                   || getNature() == EPropNature.VIRTUAL
                   || isReportProperty()
                   ) {
            return getReferencedClassObjectTitle(environment);
        } else {
            final RadClassPresentationDef owner = getApplication().getDefManager().getClassPresentationDef(ownerClassId);
            if (owner.getAncestorClassPresentationDef() == null
                    || !owner.getAncestorClassPresentationDef().isPropertyDefExistsById(getId())) {//in this case inherit title from referenced class
                return getReferencedClassObjectTitle(environment);
            }
        }
        return super.getTitle();
    }

    private String getReferencedClassObjectTitle(final IClientEnvironment environment) {
        final RadClassPresentationDef classDef = getReferencedClassPresentation();
        if (classDef == null) {
            return null;
        } else {
            final String referencedClassObjectTitle = type.isArrayType() ? classDef.getGroupTitle() : classDef.getObjectTitle();
            if (environment==null){
                return referencedClassObjectTitle;
            }else{
                return ClientValueFormatter.decapitalizeIfNecessary(environment, referencedClassObjectTitle);
            }
        }
    }

    private RadClassPresentationDef getReferencedClassPresentation() {
        Id classId = getReferencedClassId();
        if (classId == null) {
            classId = getReferencedTableId();
        }
        return classId != null ? getApplication().getDefManager().getClassPresentationDef(classId) : null;
    }

    public RadEditorPresentationDef getObjectCreationPresentation() {
        final RadParentRefPropertyDef finalPropertyDef = (RadParentRefPropertyDef) getFinalProperty();
        if (finalPropertyDef.objectCreationPresentationIds == null
                || finalPropertyDef.objectCreationPresentationIds.length == 0) {
            return null;
        }
        for (Id editorPresentationId : finalPropertyDef.objectCreationPresentationIds) {
            try {
                final RadEditorPresentationDef editorPresentation =
                        getApplication().getDefManager().getEditorPresentationDef(editorPresentationId);
                final ERuntimeEnvironmentType environmentType =
                        editorPresentation.getRuntimeEnvironmentType();
                if (environmentType == ERuntimeEnvironmentType.COMMON_CLIENT
                        || environmentType == getApplication().getRuntimeEnvironmentType()) {
                    return editorPresentation;
                }
            } catch (DefinitionError ex) {
                //it may be unsupported environment
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }
        return null;
    }

    public boolean isObjectCreationPresentationDefined() {
        final RadParentRefPropertyDef finalPropertyDef = (RadParentRefPropertyDef) getFinalProperty();
        if (finalPropertyDef.objectCreationPresentationIds == null
                || finalPropertyDef.objectCreationPresentationIds.length == 0) {
            return false;
        }
        for (Id editorPresentationId : finalPropertyDef.objectCreationPresentationIds) {
            try {
                final RadEditorPresentationDef editorPresentation =
                        getApplication().getDefManager().getEditorPresentationDef(editorPresentationId);
                final ERuntimeEnvironmentType environmentType =
                        editorPresentation.getRuntimeEnvironmentType();
                if (environmentType == ERuntimeEnvironmentType.COMMON_CLIENT
                        || environmentType == getApplication().getRuntimeEnvironmentType()) {
                    return true;
                }
            } catch (DefinitionError ex) {
                //it may be unsupported environment
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }
        return false;
    }

    public boolean isObjectEditorPresentationDefined() {
        final Id[] finalObjectEditorPresentationIds =
                ((RadParentRefPropertyDef) getFinalProperty()).objectEditorPresentationIds;
        if (finalObjectEditorPresentationIds == null || finalObjectEditorPresentationIds.length == 0) {
            return false;
        }
        for (Id editorPresentationId : finalObjectEditorPresentationIds) {
            try {
                getApplication().getDefManager().getEditorPresentationDef(editorPresentationId);
                return true;
            } catch (DefinitionError ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }
        return false;
    }

    public List<Id> getObjectEditorPresentationIds() {
        final Id[] finalObjectEditorPresentationIds =
                ((RadParentRefPropertyDef) getFinalProperty()).objectEditorPresentationIds;
        if (finalObjectEditorPresentationIds == null || finalObjectEditorPresentationIds.length == 0) {
            return Collections.emptyList();
        } else {
            return Arrays.asList(finalObjectEditorPresentationIds);
        }
    }

    public RadSelectorPresentationDef getParentSelectorPresentation() {
        final Id finalParentSelectorPresentationId = getFinalParentRefProperty().parentSelectorPresentationId,
                finalReferencedClassId = getFinalParentRefProperty().referencedClassId;
        try {
            if (finalParentSelectorPresentationId == null) {
                final RadClassPresentationDef presentationClass;
                presentationClass = getApplication().getDefManager().getClassPresentationDef(finalReferencedClassId);
                return presentationClass.getDefaultSelectorPresentation();
            } else {
                return getApplication().getDefManager().getSelectorPresentationDef(finalParentSelectorPresentationId);
            }
        } catch (DefinitionError error) {
            return null;//it may be unsupported environment
        }
    }

    public Restrictions getParentSelectorRestrictions() {
        if (getNature() == EPropNature.PARENT_PROP) {
            return ((RadParentRefPropertyDef) getOrigProp()).getParentSelectorRestrictions();
        }
        return parentSelectorRestrictions;
    }

    public Restrictions getParentEditorRestrictions() {
        return parentEditorRestrictions;
    }

    public Id getReferencedTableId() {
        if (inheritanceMask.isParentSelectorInherited() && getNature() == EPropNature.PARENT_PROP) {
            return ((RadParentRefPropertyDef) getOrigProp()).getReferencedTableId();
        }
        return referencedTableId;
    }

    public Id getReferencedClassId() {
        if (inheritanceMask.isParentSelectorInherited() && getNature() == EPropNature.PARENT_PROP) {
            return ((RadParentRefPropertyDef) getOrigProp()).getReferencedClassId();
        }
        return referencedClassId;
    }
    
    public boolean autoSortInstantiatableClasses(){
        return isAutoSortInstantiatableClasses;
    }

    @Override
    public String getDescription() {
        final String desc = getApplication().getMessageProvider().translate("DefinitionDescribtion", "parent reference property %s, owner definition is #%s, refer to #%s");
        return String.format(desc, toString(), getOwnerClassId(), getReferencedClassId());
    }
}