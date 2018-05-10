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

package org.radixware.kernel.common.client.meta.mask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.RadParentRefPropertyDef;
import org.radixware.kernel.common.client.meta.RadPresentationDef;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.models.items.properties.PropertyArrRef;
import org.radixware.kernel.common.client.models.items.properties.PropertyRef;
import org.radixware.kernel.common.client.types.ArrRef;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.scml.SqmlExpression;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.types.MapStrStr;
import org.radixware.schemas.types.MapStrStr.Entry;


public class EditMaskRef extends EditMask {

    private Id selectorPresentationId;
    private List<Id> editorPresentationIds;
    private org.radixware.schemas.xscml.Sqml condition;
    private Id defaultFilterId;
    private boolean defaultFilterDefined = false;
    private Map<Id, Id> defaultSortingIdByFilterId;
    private boolean useDropDownList;

    public EditMaskRef() {
        super();
    }

    public EditMaskRef(final Id selectorPresentationId) {
        super();
        this.selectorPresentationId = selectorPresentationId;
    }

    public EditMaskRef(final Id selectorPresentationId, final Id editorPresentationId) {
        super();
        this.selectorPresentationId = selectorPresentationId;
        setEditorPresentationId(editorPresentationId);
    }

    public EditMaskRef(final Id selectorPresentationId, final Id[] editorPresentationIds) {
        super();
        this.selectorPresentationId = selectorPresentationId;
        if (editorPresentationIds != null) {
            setEditorPresentationIds(new ArrayList<>(Arrays.asList(editorPresentationIds)));
        }
    }
    
    public EditMaskRef(final Id selectorPresentationId, 
                       final Id[] editorPresentationIds,
                       final boolean useDropDownList) {
        super();
        this.selectorPresentationId = selectorPresentationId;
        this.useDropDownList = useDropDownList;
        if (editorPresentationIds != null) {
            setEditorPresentationIds(new ArrayList<>(Arrays.asList(editorPresentationIds)));
        }
    }    

    public EditMaskRef(final EditMaskRef copy) {
        super();
        this.condition = copy.condition;
        this.defaultFilterDefined = copy.defaultFilterDefined;
        this.defaultFilterId = copy.defaultFilterId;
        this.defaultSortingIdByFilterId = copy.defaultSortingIdByFilterId;
        this.editorPresentationIds = copy.editorPresentationIds;
        this.selectorPresentationId = copy.selectorPresentationId;
        this.useDropDownList = copy.useDropDownList;
    }

    protected EditMaskRef(final org.radixware.schemas.editmask.EditMaskRef editMask) {
        super();
        setCondition(editMask.isSetCondition() ? editMask.getCondition() : null);
        this.defaultFilterDefined = editMask.getDefaultFilterDefined();
        this.defaultFilterId = editMask.getDefaultFilterId();
        this.editorPresentationIds = editMask.getEditorPresentationIds();
        this.selectorPresentationId = editMask.getSelectorPresentationId();
        this.defaultSortingIdByFilterId = getDefaultSortingIdByFilterIdFromMask(editMask);
        this.useDropDownList = editMask.getUseDropDownList();
    }

    private Map<Id, Id> getDefaultSortingIdByFilterIdFromMask(final org.radixware.schemas.editmask.EditMaskRef editMask) {
        if (editMask == null) {
            return null;//Collections.EMPTY_MAP;
        } else {
            Map<Id, Id> map = null;
            if (editMask.getDefaultSortingIdByFilterId() != null) {
                map = new HashMap<>();
                for (Entry e : editMask.getDefaultSortingIdByFilterId().getEntryList()) {
                    if (e != null) {
                        map.put(Id.Factory.loadFrom(e.getKey()), Id.Factory.loadFrom(e.getValue()));
                    }
                }
            }
            return map;
        }
    }

    /*private static SqmlExpression getValueSqml(final String val) {
     SqmlExpression ret = new SqmlExpression();
     XmlString sql = XmlString.Factory.newInstance();
     sql.setStringValue(val);
     ret.append((SqmlExpression) sql);
     return ret;
     }*/
    public Id getSelectorPresentationId() {
        if (owner instanceof PropertyRef) {
            return ((PropertyRef) owner).getParentSelectorPresentation().getId();
        } else if (owner instanceof PropertyArrRef) {
            final RadSelectorPresentationDef presentation = 
                ((PropertyArrRef) owner).getParentSelectorPresentation();
            return presentation==null ? null : presentation.getId();
        } else {
            return selectorPresentationId;
        }
    }

    public final void setSelectorPresentationId(final Id selectorPresentationId) {
        if (owner instanceof PropertyRef || owner instanceof PropertyArrRef) {
            throw new UnsupportedOperationException("Cant change selector presentation for propertyRef");
        } else {
            this.selectorPresentationId = selectorPresentationId;
        }
    }

    public List<Id> getEditorPresentationIds() {
        if (owner instanceof PropertyRef) {
            return ((RadParentRefPropertyDef) ((PropertyRef) owner).getDefinition()).getObjectEditorPresentationIds();
        } else if (owner instanceof PropertyArrRef) {
            final RadPropertyDef propertyDef = ((PropertyArrRef) owner).getDefinition();
            if (propertyDef instanceof RadParentRefPropertyDef){
                return ((RadParentRefPropertyDef)propertyDef).getObjectEditorPresentationIds();
            }else{
                return Collections.<Id>emptyList();
            }
        }
        if (editorPresentationIds == null) {
            return new ArrayList<>();
        }        
        return new ArrayList<>(editorPresentationIds);
    }

    public final void setEditorPresentationId(final Id editorPresentationId) {
        if (owner instanceof PropertyRef || owner instanceof PropertyArrRef) {
            throw new UnsupportedOperationException("Cant change editor presentation for propertyRef");
        } else if (editorPresentationId == null) {
            editorPresentationIds = null;
        }else{
            if (this.editorPresentationIds == null) {
                this.editorPresentationIds = new LinkedList<>();
            }else{
                this.editorPresentationIds.clear();
            }
            this.editorPresentationIds.add(editorPresentationId);            
        }
    }

    public final void setEditorPresentationIds(final List<Id> idList) {
        if (owner instanceof PropertyRef || owner instanceof PropertyArrRef) {
            throw new UnsupportedOperationException("Cant change editor presentation for propertyRef");
        } else if (idList != null && !idList.isEmpty()) {
            if (this.editorPresentationIds == null) {
                this.editorPresentationIds = new LinkedList<>();
            }
            this.editorPresentationIds.clear();
            for (Id id : idList) {
                if (id != null) {
                    this.editorPresentationIds.add(id);
                }
            }
        }else{
            editorPresentationIds = null;
        }
    }

    public org.radixware.schemas.xscml.Sqml getCondition() {
        if (owner instanceof PropertyRef) {
            return ((PropertyRef) owner).getCondition();
        }
        if (owner instanceof PropertyArrRef) {
            return ((PropertyArrRef) owner).getCondition();
        }
        return condition == null ? null : (org.radixware.schemas.xscml.Sqml) condition.copy();
    }

    public final void setCondition(final org.radixware.schemas.xscml.Sqml condition) {
        if (owner instanceof PropertyRef) {
            ((PropertyRef) owner).setCondition(condition);
        } else if (owner instanceof PropertyArrRef) {
            ((PropertyArrRef) owner).setCondition(condition);
        } else {
            this.condition = condition == null ? null : (org.radixware.schemas.xscml.Sqml) condition.copy();
        }
    }

    public void setCondition(final SqmlExpression sqmlExpression) {
        if (owner instanceof PropertyRef) {
            ((PropertyRef) owner).setCondition(sqmlExpression);
        } else if (owner instanceof PropertyArrRef) {
            ((PropertyArrRef) owner).setCondition(sqmlExpression);
        } else {
            this.condition = sqmlExpression == null ? null : sqmlExpression.asXsqml();
        }
    }

    public Id getDefaultFilterId() {
        if (owner instanceof PropertyRef) {
            return ((PropertyRef) owner).getDefaultFilterId();
        }
        if (owner instanceof PropertyArrRef) {
            return ((PropertyArrRef) owner).getDefaultFilterId();
        }
        return defaultFilterId;
    }

    public boolean isDefinedDefaultFilter() {
        if (owner instanceof PropertyRef) {
            return ((PropertyRef) owner).isDefinedDefaultFilterId();
        }
        if (owner instanceof PropertyArrRef) {
            return ((PropertyArrRef) owner).isDefinedDefaultFilterId();
        }
        return defaultFilterDefined;
    }

    public final void setDefaultFilterId(final Id defaultFilter) {
        if (owner instanceof PropertyRef) {
            ((PropertyRef) owner).setDefaultFilterId(defaultFilter);
        } else if (owner instanceof PropertyArrRef) {
            ((PropertyArrRef) owner).setDefaultFilterId(defaultFilter);
        } else {
            defaultFilterDefined = true;
            defaultFilterId = defaultFilter;
        }
    }

    public void unsetDefaultFilter() {
        if (owner instanceof PropertyRef) {
            ((PropertyRef) owner).unsetDefaultFilter();
        } else if (owner instanceof PropertyArrRef) {
            ((PropertyArrRef) owner).unsetDefaultFilter();
        } else {
            defaultFilterDefined = false;
            defaultFilterId = null;
        }
    }

    public Id getDefaultSortingId(final Id filterId) {
        if (owner instanceof PropertyRef) {
            return ((PropertyRef) owner).getDefaultSortingId(filterId);
        } else if (owner instanceof PropertyArrRef) {
            return ((PropertyArrRef) owner).getDefaultSortingId(filterId);
        } else {
            return defaultSortingIdByFilterId == null || defaultSortingIdByFilterId.isEmpty()
                    ? null : defaultSortingIdByFilterId.get(filterId);
        }
    }

    public boolean isDefinedDefaultSorting(final Id filterId) {
        if (owner instanceof PropertyRef) {
            return ((PropertyRef) owner).isDefinedDefaultSortingId(filterId);
        } else if (owner instanceof PropertyArrRef) {
            return ((PropertyArrRef) owner).isDefinedDefaultSortingId(filterId);
        } else {
            return defaultSortingIdByFilterId == null || defaultSortingIdByFilterId.isEmpty()
                    ? false : defaultSortingIdByFilterId.containsKey(filterId);
        }
    }

    public void setDefaultSortingId(final Id sortingId, final Id filterId) {
        if (owner instanceof PropertyRef) {
            ((PropertyRef) owner).setDefaultSortingId(sortingId, filterId);
        } else if (owner instanceof PropertyArrRef) {
            ((PropertyArrRef) owner).setDefaultSortingId(sortingId, filterId);
        } else {
            if (defaultSortingIdByFilterId == null) {
                defaultSortingIdByFilterId = new HashMap<>();
            }
            if (sortingId != null && filterId != null) {
                defaultSortingIdByFilterId.put(filterId, sortingId);
            }
        }
    }

    public Map<Id, Id> getDefaultSortingIdByFilterId() {
        return defaultSortingIdByFilterId;
    }

    public boolean isUseDropDownList() {
        return useDropDownList;
    }

    public void setUseDropDownList(boolean useDropDownList) {
        this.useDropDownList = useDropDownList;
        afterModify();
    }
    
    public Id getReferencedTableId(final IClientEnvironment environment){
        final RadPresentationDef presentation;
        if (getSelectorPresentationId()==null){
            if (getEditorPresentationIds().isEmpty()){
                return null;
            }else{
                final Id presentationId = getEditorPresentationIds().get(0);
                presentation = 
                        environment.getDefManager().getEditorPresentationDef(presentationId);                                
            }
        }else{
            presentation = 
                environment.getDefManager().getSelectorPresentationDef(getSelectorPresentationId());            
        }
        return presentation.getTableId();
    }

    @Override
    public String toStr(final IClientEnvironment environment, final Object o) {
        if (o == null) {
            return getNoValueStr(environment.getMessageProvider());
        } else if (o instanceof ArrRef) {
            return arrToStr(environment, (ArrRef) o);
        } else if (o instanceof Reference) {
            return ((Reference) o).toString();
        }
        return o.toString();
    }

    @Override
    public ValidationResult validate(final IClientEnvironment environment, final Object value) {
        if (value == null) {
            return ValidationResult.ACCEPTABLE;
        }
        if (value instanceof Arr) {
            return validateArray(environment, (Arr) value);
        }
        if (value instanceof Reference) {
            if (((Reference) value).isValid()) {
                return ValidationResult.ACCEPTABLE;
            } else {
                return ValidationResult.Factory.newInvalidResult(InvalidValueReason.NO_REASON);
            }
        }
        return ValidationResult.Factory.newInvalidResult(InvalidValueReason.Factory.createForInvalidValueType(environment));
    }

    @Override
    public EEditMaskType getType() {
        return EEditMaskType.OBJECT_REFERENCE;
    }

    @Override
    public EnumSet<EValType> getSupportedValueTypes() {
        return EnumSet.of(EValType.PARENT_REF, EValType.ARR_REF);
    }

    @Override
    public void writeToXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskRef editMaskRef = editMask.addNewReference();
        if (condition != null) {
            editMaskRef.setCondition(condition);
        }
        if (defaultFilterId != null) {
            editMaskRef.setDefaultFilterId(defaultFilterId);
        }
        if (defaultSortingIdByFilterId != null) {
            MapStrStr sortingOnFilter = editMaskRef.addNewDefaultSortingIdByFilterId();//how to clear old one?
            for (Map.Entry<Id, Id> entry : defaultSortingIdByFilterId.entrySet()) {
                MapStrStr.Entry e = sortingOnFilter.addNewEntry();
                if (entry.getValue() != null) {
                    if (entry.getKey()!=null){
                        e.setKey(entry.getKey().toString());
                    }
                    e.setValue(entry.getValue().toString());
                }
            }
        }
        if (editorPresentationIds != null) {
            editMaskRef.setEditorPresentationIds(editorPresentationIds);
        }
        if (selectorPresentationId != null) {
            editMaskRef.setSelectorPresentationId(selectorPresentationId);
        }        
        editMaskRef.setDefaultFilterDefined(defaultFilterDefined);
        if (useDropDownList){
            editMaskRef.setUseDropDownList(true);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 22 * hash + Objects.hashCode(this.condition);
        hash = 22 * hash + Objects.hashCode(this.defaultFilterDefined);
        hash = 22 * hash + Objects.hashCode(this.defaultFilterId);
        hash = 22 * hash + Objects.hashCode(this.defaultSortingIdByFilterId);
        hash = 22 * hash + Objects.hashCode(this.editorPresentationIds);
        hash = 22 * hash + Objects.hashCode(this.selectorPresentationId);
        hash = 22 * hash + Objects.hashCode(this.useDropDownList);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EditMaskRef other = (EditMaskRef) obj;

        if (!Objects.equals(this.condition, other.condition)) {
            return false;
        }
        if (!Objects.equals(this.defaultFilterDefined, other.defaultFilterDefined)) {
            return false;
        }
        if (!Objects.equals(this.defaultFilterId, other.defaultFilterId)) {
            return false;
        }
        if (!Objects.equals(this.defaultSortingIdByFilterId, other.defaultSortingIdByFilterId)) {
            return false;
        }
        if (!Objects.equals(this.editorPresentationIds, other.editorPresentationIds)) {
            return false;
        }
        if (!Objects.equals(this.selectorPresentationId, other.selectorPresentationId)) {
            return false;
        }
        if (!Objects.equals(this.useDropDownList, other.useDropDownList)) {
            return false;
        }        
        return super.equals(obj);
    }
    
    public boolean sameSelection(final EditMaskRef otherMask){
        if (this.equals(otherMask)){
            return true;
        }
        if (otherMask==null){
            return false;
        }
        if (!Objects.equals(getSelectorPresentationId(), otherMask.getSelectorPresentationId())){
            return false;
        }
        if (!Objects.equals(getCondition(), otherMask.getCondition())){
            return false;
        }
        if (!Objects.equals(getDefaultFilterId(), otherMask.getDefaultFilterId())){
            return false;
        }
        final Id defaultFilterId = getDefaultFilterId();
        if (!Objects.equals(getDefaultSortingId(defaultFilterId), otherMask.getDefaultSortingId(defaultFilterId))){
            return false;
        }
        return true;        
    }
}
