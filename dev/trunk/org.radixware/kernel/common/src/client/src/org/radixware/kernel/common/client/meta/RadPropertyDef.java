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

import java.lang.reflect.Constructor;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.errors.CantLoadCustomViewError;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskConstSet;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.client.views.IPropEditorDialog;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EEditMaskEnumCorrection;
import org.radixware.kernel.common.enums.EEditMaskEnumOrder;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EEditPossibility;
import org.radixware.kernel.common.enums.EPropAttrInheritance;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.enums.EPropertyValueStorePossibility;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;

/**
 * хранит метаинформацию для свойства. Конечные классы у презентации и сущности
 * реализуют метод getInheritedFrom.
 *
 */
public class RadPropertyDef extends TitledDefinition {

    static protected final class PropertyInheritance {

        public static final PropertyInheritance EMPTY = new PropertyInheritance();
        private final EnumSet<EPropAttrInheritance> inheritedAttributes;

        private PropertyInheritance() {
            inheritedAttributes = EnumSet.noneOf(EPropAttrInheritance.class);
        }

        public PropertyInheritance(final long inheritanceMask) {
            inheritedAttributes = EPropAttrInheritance.fromBitField(inheritanceMask);
        }

        public boolean isTitleInherited() {
            return inheritedAttributes.contains(EPropAttrInheritance.TITLE);
        }

        public boolean isToolTipInherited() {
            return inheritedAttributes.contains(EPropAttrInheritance.HINT);
        }

        public boolean isEditingInherited() {
            return inheritedAttributes.contains(EPropAttrInheritance.EDITING);
        }

        public boolean isParentSelectorInherited() {
            return inheritedAttributes.contains(EPropAttrInheritance.PARENT_SELECTOR);
        }
    };
    protected final EPropNature nature;
    protected final PropertyInheritance inheritanceMask;
    private RadEnumPresentationDef constSet;
    private final Id ownerClassId;
    private final boolean readSeparately;
    private final Object valInheritableMark;
    private final boolean isInheritable;
    protected final EValType type;
    private RadPropertyDef origProp = null;
    private final Id ownerEntityId;//Для UserProp, ParentProp, DetailProp – сущность, которой принадлежит свойство
    private final Id origPropId;//Для ParentProp, DetailProp – ид. оригинального свойства
    private final ValAsStr initVal;
    //Editing
    private final boolean isMemo;
    private final boolean isDuplicatesEnabled;
    private final EEditPossibility editPossibility;
    private final ERuntimeEnvironmentType editingEnvironment;
    private final boolean mandatory;
    private final boolean arrayItemMandatory;
    private final boolean storeHistory;
    private final boolean customDialog;
    private final Id customDialogId;
    private final boolean customEditOnly;
    private final EditMask editMask;
    private final Id nullStringId;
    private final Id arrayItemNullStringId;
    private final Id emptyArrayStringId;
    private final Id hintId;
    private final boolean canBeUsedInSorting;
    private String nullString = null;
    private String arrayItemNullString = null;
    private String hint;
    private RadPropertyDef finalProp;
    private final int firstArrayItemIndex;
    private final int minArrayItemsCount;
    private final int maxArrayItemsCount;
    private final boolean isDeprecated;
    private EPropertyValueStorePossibility storePossibility;

    public RadPropertyDef(final Id id,
            final String name,
            final Id titleId,
            final Id hintStringId,
            final Id titleOwnerId,//ID of owner class
            final EValType type,
            final EPropNature nature,
            final long inheritanceMask,
            final Id ownerEntityId,//Для UserProp, ParentProp, DetailProp – сущность, которой принадлежит свойство
            final Id origPropId,//Для ParentProp, DetailProp – ид. оригинального свойства
            final Id constSetId,
            final boolean readSeparately,
            final boolean isInheritable,
            final ValAsStr valInheritableMark,
            final ValAsStr initVal,
            final EEditPossibility editPossibility,
            final ERuntimeEnvironmentType environment,
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
            final boolean isDuplicatesEnabled, //onlyForArrayProperties
            final int minArrayItemsCount,//onlyForArrayProperties
            final int maxArrayItemsCount,//onlyForArrayProperties
            final int firstArrayItemIndex,//onlyForArrayProperties
            final boolean isMemo,
            final boolean canBeUsedInSorting,
            final EPropertyValueStorePossibility storePossibility) {
        this(id,
                name,
                titleId,
                hintStringId,
                titleOwnerId,//ID of owner class

                type,
                nature,
                inheritanceMask,
                ownerEntityId,//Для UserProp, ParentProp, DetailProp – сущность, которой принадлежит свойство
                origPropId,//Для ParentProp, DetailProp – ид. оригинального свойства
                constSetId,
                readSeparately,
                isInheritable,
                valInheritableMark,
                initVal,
                editPossibility,
                environment,
                mandatory,
                arrayItemIsMandatory,//onlyForArrayProperties
                storeHistory,
                customDialog,
                customDialogId,
                customEditOnly,
                editMask,
                nullStringId,
                arrayItemNullStringId,//onlyForArrayProperties
                emptyArrayStringId,//onlyForArrayProperties            
                isDuplicatesEnabled, //onlyForArrayProperties
                minArrayItemsCount,//onlyForArrayProperties
                maxArrayItemsCount,//onlyForArrayProperties
                firstArrayItemIndex,//onlyForArrayProperties
                isMemo,
                canBeUsedInSorting,
                storePossibility,
                false);
    }

    public RadPropertyDef(final Id id,
            final String name,
            final Id titleId,
            final Id hintStringId,
            final Id titleOwnerId,//ID of owner class

            final EValType type,
            final EPropNature nature,
            final long inheritanceMask,
            final Id ownerEntityId,//Для UserProp, ParentProp, DetailProp – сущность, которой принадлежит свойство
            final Id origPropId,//Для ParentProp, DetailProp – ид. оригинального свойства
            final Id constSetId,
            final boolean readSeparately,
            final boolean isInheritable,
            final ValAsStr valInheritableMark,
            final ValAsStr initVal,
            final EEditPossibility editPossibility,
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
            final boolean isDuplicatesEnabled, //onlyForArrayProperties
            final int minArrayItemsCount,//onlyForArrayProperties
            final int maxArrayItemsCount,//onlyForArrayProperties
            final int firstArrayItemIndex,//onlyForArrayProperties
            final boolean isMemo,
            final boolean canBeUsedInSorting) {
        this(id,
                name,
                titleId,
                hintStringId,
                titleOwnerId,
                type,
                nature,
                inheritanceMask,
                ownerEntityId,//Для UserProp, ParentProp, DetailProp – сущность, которой принадлежит свойство
                origPropId,//Для ParentProp, DetailProp – ид. оригинального свойства
                constSetId,
                readSeparately,
                isInheritable,
                valInheritableMark,
                initVal,
                editPossibility,
                ERuntimeEnvironmentType.COMMON_CLIENT,
                mandatory,
                arrayItemIsMandatory,//onlyForArrayProperties
                storeHistory,
                customDialog,
                customDialogId,
                customEditOnly,
                editMask,
                nullStringId,
                arrayItemNullStringId,//onlyForArrayProperties
                emptyArrayStringId,//onlyForArrayProperties            
                isDuplicatesEnabled, //onlyForArrayProperties
                minArrayItemsCount,//onlyForArrayProperties
                maxArrayItemsCount,//onlyForArrayProperties
                firstArrayItemIndex,//onlyForArrayProperties
                isMemo,
                canBeUsedInSorting, EPropertyValueStorePossibility.NONE);

    }

    public RadPropertyDef(final Id id,
            final String name,
            final Id titleId,
            final Id hintStringId,
            final Id titleOwnerId,//ID of owner class

            final EValType type,
            final EPropNature nature,
            final long inheritanceMask,
            final Id ownerEntityId,//Для UserProp, ParentProp, DetailProp – сущность, которой принадлежит свойство
            final Id origPropId,//Для ParentProp, DetailProp – ид. оригинального свойства
            final Id constSetId,
            final boolean readSeparately,
            final boolean isInheritable,
            final ValAsStr valInheritableMark,
            final ValAsStr initVal,
            final EEditPossibility editPossibility,
            final ERuntimeEnvironmentType environment,
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
            final boolean isDuplicatesEnabled, //onlyForArrayProperties
            final int minArrayItemsCount,//onlyForArrayProperties
            final int maxArrayItemsCount,//onlyForArrayProperties
            final int firstArrayItemIndex,//onlyForArrayProperties
            final boolean isMemo,
            final boolean canBeUsedInSorting,
            final EPropertyValueStorePossibility storePossibility,
            final boolean isDeprecated) {
        super(id, name, titleOwnerId, titleId);
        this.hintId = hintStringId;
        ownerClassId = titleOwnerId;
        this.isDeprecated = isDeprecated;
        this.type = type;
        this.nature = nature;
        if (inheritanceMask == 0) {
            this.inheritanceMask = PropertyInheritance.EMPTY;
        } else {
            this.inheritanceMask = new PropertyInheritance(inheritanceMask);
        }
        this.ownerEntityId = ownerEntityId;
        this.origPropId = origPropId;
        this.readSeparately = readSeparately;

        this.isInheritable = isInheritable;
        this.valInheritableMark = ValAsStr.fromStr(valInheritableMark, getInheritableMarkValType(type));
        this.initVal = initVal;

        this.editPossibility = editPossibility;
        this.editingEnvironment = environment;
        this.mandatory = mandatory;
        this.arrayItemMandatory = arrayItemIsMandatory;
        this.storeHistory = storeHistory;
        this.customDialog = customDialog;
        this.customDialogId = customDialogId;
        this.customEditOnly = customEditOnly;
        this.isMemo = isMemo;
        this.isDuplicatesEnabled = isDuplicatesEnabled;
        this.firstArrayItemIndex = firstArrayItemIndex;
        this.minArrayItemsCount = minArrayItemsCount;
        this.maxArrayItemsCount = maxArrayItemsCount;
        this.canBeUsedInSorting = canBeUsedInSorting;

        if (constSetId != null) {
            try {
                constSet = getDefManager().getEnumPresentationDef(constSetId);
            } catch (DefinitionError err) {
                final String mess = getApplication().getMessageProvider().translate("TraceMessage", "Cannot get enumeration definition #%s for property %s");
                getApplication().getTracer().error(String.format(mess, constSetId, this.toString()), err);
                constSet = null;
            }
        } else {
            constSet = null;
        }

        this.editMask = editMask != null ? editMask : createDefaultEditMask();
        this.nullStringId = nullStringId;
        this.arrayItemNullStringId = arrayItemNullStringId;
        this.emptyArrayStringId = emptyArrayStringId;
        if (storePossibility == null) {
            this.storePossibility = EPropertyValueStorePossibility.NONE;
        } else {
            this.storePossibility = storePossibility;
        }
    }

    public RadPropertyDef(final Id id,
            final String name,
            final Id titleId,
            final Id hintStringId,
            final Id titleOwnerId,//ID of owner class
            final EValType type,
            final EPropNature nature,
            final long inheritanceMask,
            final Id ownerEntityId,//Для UserProp, ParentProp, DetailProp – сущность, которой принадлежит свойство
            final Id origPropId,//Для ParentProp, DetailProp – ид. оригинального свойства
            final Id constSetId,
            final boolean readSeparately,
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
            final boolean isDuplicatesEnabled, //onlyForArrayProperties
            final int minArrayItemsCount,//onlyForArrayProperties
            final int maxArrayItemsCount,//onlyForArrayProperties
            final int firstArrayItemIndex,//onlyForArrayProperties
            final boolean isMemo,
            final boolean canBeUsedInSorting) {
        this(id,
                name,
                titleId,
                hintStringId,
                titleOwnerId,//ID of owner class
                type,
                nature,
                inheritanceMask,
                ownerEntityId,//Для UserProp, ParentProp, DetailProp – сущность, которой принадлежит свойство
                origPropId,//Для ParentProp, DetailProp – ид. оригинального свойства
                constSetId,
                readSeparately,
                isInheritable,
                valInheritableMark,
                initVal,
                editPossibility,
                editingEnvironment,
                mandatory,
                arrayItemIsMandatory,//onlyForArrayProperties
                storeHistory,
                customDialog,
                customDialogId,
                customEditOnly,
                editMask,
                nullStringId,
                arrayItemNullStringId,//onlyForArrayProperties
                emptyArrayStringId,//onlyForArrayProperties            
                isDuplicatesEnabled, //onlyForArrayProperties
                minArrayItemsCount,//onlyForArrayProperties
                maxArrayItemsCount,//onlyForArrayProperties
                firstArrayItemIndex,//onlyForArrayProperties
                isMemo,
                canBeUsedInSorting, EPropertyValueStorePossibility.NONE);
    }

    private static EValType getInheritableMarkValType(final EValType propertyType) {
        if (propertyType == EValType.XML) {
            //У типизированного CLOB-свойства маркер наследования может быть не xml.
            return EValType.STR;
        }
        return ValueConverter.serverValType2ClientValType(propertyType);
    }

    //GETTERS
    public final Id getOwnerClassId() {
        return ownerClassId;
    }

    @Override
    public boolean hasTitle() {
        if (inheritanceMask.isTitleInherited() && getNature() != EPropNature.VIRTUAL) {
            if (isFormProperty()) {
                final RadFormDef owner = getDefManager().getFormDef(ownerClassId);
                for (RadFormDef base = owner.getBaseFormDef(); base != null; base = base.getBaseFormDef()) {
                    if (base.isPropertyDefExistsById(getId())) {
                        return base.getPropertyDefById(getId()).hasTitle();
                    }
                }
            } else if (isClassPresentationProperty()) {
                final RadClassPresentationDef owner = getDefManager().getClassPresentationDef(ownerClassId);
                for (RadClassPresentationDef base = owner.getAncestorClassPresentationDef(); base != null; base = base.getAncestorClassPresentationDef()) {
                    if (base.isPropertyDefExistsById(getId())) {
                        return base.getPropertyDefById(getId()).hasTitle();
                    }
                }
            }
            return getNature() == EPropNature.PARENT_PROP ? getOrigProp().hasTitle() : super.hasTitle();
        } else {
            return super.hasTitle();
        }
    }
    
    public String getTitle(final IClientEnvironment environment){
        return getTitleImpl();
    }

    @Override
    public String getTitle() {
        return getTitleImpl();
    }
    
    private String getTitleImpl(){
        if (inheritanceMask.isTitleInherited() && getNature() != EPropNature.VIRTUAL) {
            String titleStr = null;
            if (isFormProperty()) {
                final RadFormDef owner = getDefManager().getFormDef(ownerClassId);
                for (RadFormDef base = owner.getBaseFormDef(); base != null && titleStr == null; base = base.getBaseFormDef()) {
                    if (base.isPropertyDefExistsById(getId())) {
                        titleStr = base.getPropertyDefById(getId()).getTitle();
                    }
                }
            } else if (isClassPresentationProperty()) {
                final RadClassPresentationDef owner = getDefManager().getClassPresentationDef(ownerClassId);
                for (RadClassPresentationDef base = owner.getAncestorClassPresentationDef(); base != null && titleStr == null; base = base.getAncestorClassPresentationDef()) {
                    if (base.isPropertyDefExistsById(getId())) {
                        titleStr = base.getPropertyDefById(getId()).getTitle();
                    }
                }
            }
            if (titleStr == null) {
                return getNature() == EPropNature.PARENT_PROP ? getOrigProp().getTitle() : super.getTitle();
            } else {
                return titleStr;
            }

        } else {
            return super.getTitle();
        }        
    }

    public String getHint() {
        if (hint == null) {
            if (inheritanceMask.isToolTipInherited() && getNature() != EPropNature.VIRTUAL) {
                if (isFormProperty()) {
                    RadFormDef owner = getDefManager().getFormDef(ownerClassId);
                    for (RadFormDef base = owner.getBaseFormDef(); base != null && hint == null; base = base.getBaseFormDef()) {
                        if (base.isPropertyDefExistsById(getId())) {
                            hint = base.getPropertyDefById(getId()).getHint();
                        }
                    }
                } else if (isClassPresentationProperty()) {
                    RadClassPresentationDef owner = getDefManager().getClassPresentationDef(ownerClassId);
                    for (RadClassPresentationDef base = owner.getAncestorClassPresentationDef(); base != null && hint == null; base = base.getAncestorClassPresentationDef()) {
                        if (base.isPropertyDefExistsById(getId())) {
                            hint = base.getPropertyDefById(getId()).getHint();
                        }
                    }
                }
                if (hint == null) {
                    hint = getNature() == EPropNature.PARENT_PROP ? getOrigProp().getHint() : getSelfHint();
                }
            } else {
                hint = getSelfHint();
            }
        }
        return hint;
    }

    private String getSelfHint() {
        if (hintId != null) {
            try {
                return getDefManager().getMlStringValue(ownerClassId, hintId);
            } catch (DefinitionError err) {
                final String mess = getApplication().getMessageProvider().translate("TraceMessage", "Cannot get string with id \"%s\" for %s");
                getApplication().getTracer().error(String.format(mess, hintId, this.getDescription()), err);
                return "";
            }
        } else {
            return "";
        }
    }

    protected RadPropertyDef getFinalProperty() {
        if (finalProp == null) {
            if (inheritanceMask.isEditingInherited() && getNature() != EPropNature.VIRTUAL) {
                RadPropertyDef baseProperty = null;
                if (isFormProperty()) {
                    final RadFormDef owner = getDefManager().getFormDef(ownerClassId);
                    for (RadFormDef base = owner.getBaseFormDef(); base != null; base = base.getBaseFormDef()) {
                        if (base.isPropertyDefExistsById(getId())) {
                            baseProperty = base.getPropertyDefById(getId());
                            break;
                        }
                    }
                } else if (isClassPresentationProperty()) {
                    final RadClassPresentationDef owner = getDefManager().getClassPresentationDef(getOwnerClassId());
                    for (RadClassPresentationDef base = owner.getAncestorClassPresentationDef(); base != null; base = base.getAncestorClassPresentationDef()) {
                        if (base.isPropertyDefExistsById(getId())) {
                            baseProperty = base.getPropertyDefById(getId());
                            break;
                        }
                    }
                }
                if (baseProperty==null && getNature() == EPropNature.PARENT_PROP){
                    baseProperty = getOrigProp();
                }                
                finalProp = baseProperty==null ? this : baseProperty.getFinalProperty();
            } else {
                finalProp = this;
            }
        }
        return finalProp;
    }
    
    
    protected final boolean isFormProperty() {
        return ownerClassId.getPrefix() == EDefinitionIdPrefix.ADS_FORM_HANDLER_CLASS;
    }
    
    protected final boolean isReportProperty(){
        return ownerClassId.getPrefix() == EDefinitionIdPrefix.REPORT || ownerClassId.getPrefix() == EDefinitionIdPrefix.USER_DEFINED_REPORT;
    }

    protected final boolean isClassPresentationProperty() {
        return ownerClassId.getPrefix() == EDefinitionIdPrefix.ADS_ENTITY_CLASS
                || ownerClassId.getPrefix() == EDefinitionIdPrefix.ADS_APPLICATION_CLASS;
    }

    public EPropNature getNature() {
        return nature;
    }

    public RadEnumPresentationDef getConstSet() {
        return constSet;
    }

    public boolean isReadSeparately() {
        return readSeparately;
    }

    public boolean isInheritable() { // значение свойства может быть унаследовано
        return isInheritable;
    }

    public Object getValInheritanceMark() {
        return valInheritableMark;
    }

    public boolean isDefineable() { // значение свойства может быть не пределено (UserProp)
        return nature == EPropNature.USER;
    }

    public ValAsStr getInitialVal() {//Начальное значение свойства (UserProp)
        return initVal;
    }

    public EValType getType() {
        return type;
    }

    public boolean isMemo() {
        return getFinalProperty().isMemo;
    }

    public boolean isDuplicatesEnabled() {
        return getFinalProperty().isDuplicatesEnabled;
    }

    public boolean storeHistory() {
        return getFinalProperty().storeHistory;
    }

    public EEditPossibility getEditPossibility() {
        return getFinalProperty().editPossibility;
    }

    public ERuntimeEnvironmentType getEditingEnvironmentType() {
        return getFinalProperty().editingEnvironment;
    }

    public boolean isMandatory() {
        return getFinalProperty().mandatory;
    }

    public boolean isArrayItemMandatory() {
        return getFinalProperty().arrayItemMandatory;
    }

    public boolean customDialog() {
        return getFinalProperty().customDialog && getFinalProperty().customDialogId != null;
    }

    public boolean isCustomEditOnly() {
        return getFinalProperty().customDialog && getFinalProperty().customEditOnly;
    }

    private EditMask createDefaultEditMask() {
        if (constSet != null) {
            return new EditMaskConstSet(constSet.getId(), EEditMaskEnumOrder.BY_ORDER, EEditMaskEnumCorrection.NONE, null);
        }
        return EditMask.newInstance(type);
    }

    public EditMask getEditMask() {
        EditMask mask = getFinalProperty().editMask;
        mask = mask == null ? null : EditMask.newCopy(mask);
        if (mask != null) {
            mask.setNoValueStr(getNullString());
            mask.setArrayItemNoValueStr(getArrayItemNullString());
            mask.setEmptyArrayString(getEmptyArrayString());
        }
        return mask;
    }

    public String getNullString() {
        final Id finalNullStringId = getFinalProperty().nullStringId,
                finalOwnerClassId = getFinalProperty().ownerClassId;

        if (finalNullStringId != null) {
            try {
                return getDefManager().getMlStringValue(finalOwnerClassId, finalNullStringId);
            } catch (DefinitionError err) {
                final String mess = getApplication().getMessageProvider().translate("TraceMessage", "Cannot get null string #%s for property %s");
                getApplication().getTracer().error(String.format(mess, finalNullStringId, this.toString()), err);
                return getApplication().getMessageProvider().translate("Value", "<not defined>");
            }
        } else {
            return getApplication().getMessageProvider().translate("Value", "<not defined>");
        }
    }

    public String getArrayItemNullString() {
        final Id finalNullStringId = getFinalProperty().arrayItemNullStringId,
                finalOwnerClassId = getFinalProperty().ownerClassId;
        if (finalNullStringId != null) {
            try {
                return getDefManager().getMlStringValue(finalOwnerClassId, finalNullStringId);
            } catch (DefinitionError err) {
                final String mess = getApplication().getMessageProvider().translate("TraceMessage", "Cannot get array item null string #%s for property %s");
                getApplication().getTracer().error(String.format(mess, finalNullStringId, this.toString()), err);
                return getApplication().getMessageProvider().translate("Value", "<not defined>");
            }
        } else {
            return getApplication().getMessageProvider().translate("Value", "<not defined>");
        }
    }

    public String getEmptyArrayString() {
        final Id finalEmptyStringId = getFinalProperty().emptyArrayStringId,
                finalOwnerClassId = getFinalProperty().ownerClassId;
        if (finalEmptyStringId != null) {
            try {
                return getDefManager().getMlStringValue(finalOwnerClassId, finalEmptyStringId);
            } catch (DefinitionError err) {
                final String mess = getApplication().getMessageProvider().translate("TraceMessage", "Cannot get empty array label #%s for property %s");
                getApplication().getTracer().error(String.format(mess, finalEmptyStringId, this.toString()), err);
                return getApplication().getMessageProvider().translate("Value", "<empty>");
            }
        } else {
            return getApplication().getMessageProvider().translate("Value", "<empty>");
        }
    }

    /**
     * Для свойств ParentProp возвращает конечное свойство
     *
     * @return
     */
    public RadPropertyDef getOrigProp() {
        if (ownerEntityId != null && origPropId != null) {
            if (origProp == null) {
                RadClassPresentationDef ownerEntity = getDefManager().getClassPresentationDef(ownerEntityId);
                origProp = ownerEntity.getPropertyDefById(origPropId).getOrigProp();
            }
            return origProp;
        } else {
            return this;
        }
    }

    public IPropEditorDialog getPropEditorDialog(IClientEnvironment env) {
        if (inheritanceMask.isEditingInherited() && getNature() == EPropNature.PARENT_PROP) {
            return getOrigProp().getPropEditorDialog(env);
        }
        final Id finalCustomDialogId = getFinalProperty().customDialogId;
        if (finalCustomDialogId != null) {
            try {
                Class<IPropEditorDialog> dialogClass = getDefManager().getPropertyEditorDialogClass(finalCustomDialogId);
                Constructor<IPropEditorDialog> c = dialogClass.getConstructor(IClientEnvironment.class);


                return c.newInstance(env);
            } catch (Exception e) {
                throw new CantLoadCustomViewError(this, e);
            }
        }
        return null;
    }

    public int getFirstArrayItemIndex() {
        return this.firstArrayItemIndex;
    }

    public int getMinArrayItemsCount() {
        return this.minArrayItemsCount;
    }

    public int getMaxArrayItemsCount() {
        return this.maxArrayItemsCount;
    }

    public static boolean isReference(EValType type) {
        return type == EValType.PARENT_REF || type == EValType.OBJECT;
    }

    @Override
    public String getDescription() {
        final String desc = getApplication().getMessageProvider().translate("DefinitionDescribtion", "property %s, owner definition is #%s");
        return String.format(desc, super.getDescription(), getOwnerClassId());
    }

    public EPropertyValueStorePossibility getPropertyValueStorePossibility() {
        return storePossibility == null ? EPropertyValueStorePossibility.NONE : storePossibility;
    }

    public boolean canBeUsedInSorting() {
        return canBeUsedInSorting;
    }

    public static boolean isPredefinedValuesSupported(final EValType valType, final EEditMaskType editMaskType) {
        return ((editMaskType != EEditMaskType.ENUM && editMaskType != EEditMaskType.LIST) || valType.isArrayType())
                && valType != EValType.BOOL && valType != EValType.OBJECT && valType != EValType.XML;
    }

    public boolean isDeprecated() {
        return isDeprecated;
    }
}
