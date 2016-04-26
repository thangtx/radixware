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

package org.radixware.kernel.common.client.models.items.properties;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.radixware.kernel.common.client.eas.EntityObjectTitles;
import org.radixware.kernel.common.client.eas.EntityObjectTitlesProvider;
import org.radixware.kernel.common.client.errors.ModelCreationError;
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.meta.RadParentRefPropertyDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.FormModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.ReportParamDialogModel;
import org.radixware.kernel.common.client.models.groupsettings.Sortings;
import org.radixware.kernel.common.client.types.ArrRef;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.scml.SqmlExpression;
import org.radixware.kernel.common.types.Id;

public class PropertyArrRef extends PropertyArr<ArrRef> {

    private org.radixware.schemas.xscml.Sqml condition = null;
    private Id initialFilterId;
    private boolean customInitialFilter;
    private Map<Id, Id> initialSortingIdByFilterId;
    private final Map<Id, Object> propertyValues = new HashMap<>(16);

    public PropertyArrRef(final Model owner, final RadParentRefPropertyDef propDef) {
        super(owner, propDef);
    }

    @Override
    public final RadParentRefPropertyDef getDefinition() {
        return (RadParentRefPropertyDef) super.getDefinition();
    }

    @Override
    public Class<?> getValClass() {
        return ArrRef.class;
    }

    @Override
    public void setValObjectImpl(Object x) {
        setInternalVal((ArrRef) x);
    }

    public ArrRef getVal() {
        return (ArrRef) getValueObject();
    }

    public boolean isGroupPropertyValueDefined(final Id propertyId) {
        return propertyValues.containsKey(propertyId);
    }

    public Object getGroupPropertyValue(final Id propertyId) {
        return propertyValues.get(propertyId);
    }

    public Map<Id, Object> getGroupPropertyValues() {
        return Collections.unmodifiableMap(propertyValues);
    }

    public org.radixware.schemas.xscml.Sqml getCondition() {
        return condition;
    }

    public final void setCondition(final org.radixware.schemas.xscml.Sqml condition) {
        this.condition = condition == null ? null : (org.radixware.schemas.xscml.Sqml) condition.copy();
    }

    public final void setCondition(final SqmlExpression expression) {
        if (expression != null) {
            condition = expression.asXsqml();
        } else {
            condition = null;
        }
    }

    public final void setGroupPropertyValue(final Id propertyId, final Object value) {
        final RadSelectorPresentationDef presentation = getDefinition().getParentSelectorPresentation();
        if (presentation != null) {
            presentation.getPropertyDefById(propertyId);//check if property exists
        }
        propertyValues.put(propertyId, value);
    }

    public final void clearGroupPropertyValues() {
        propertyValues.clear();
    }

    public final void setDefaultFilterId(final Id filterId) {
        initialFilterId = filterId;
        customInitialFilter = true;
    }

    public final void unsetDefaultFilter() {
        initialFilterId = null;
        customInitialFilter = false;
    }

    public final void setDefaultSortingId(final Id sortingId, final Id filterId) {
        if (initialSortingIdByFilterId == null) {
            initialSortingIdByFilterId = new HashMap<>();
        }
        initialSortingIdByFilterId.put(filterId, sortingId);
    }

    public final boolean isDefinedDefaultFilterId() {
        return customInitialFilter;
    }

    public final Id getDefaultFilterId() {
        return initialFilterId;
    }

    public final boolean isDefinedDefaultSortingId(final Id filterId) {
        return initialSortingIdByFilterId != null && initialSortingIdByFilterId.containsKey(filterId);
    }

    public final Id getDefaultSortingId(final Id filterId) {
        return initialSortingIdByFilterId == null ? null : initialSortingIdByFilterId.get(filterId);
    }

    public GroupModel openGroupModel() {
        final RadSelectorPresentationDef presentation = getDefinition().getParentSelectorPresentation();

        if (presentation == null) {
            final String info = getEnvironment().getMessageProvider().translate("ExplorerException", "selector presentation was not defined");
            throw new ModelCreationError(ModelCreationError.ModelType.GROUP_MODEL, null, getDefinition(), null, info);
        }

        final GroupModel model = presentation.createModel(createContext());
        try {
            if (!setupGroupModel(model)) {
                return null;
            }
        } catch (InterruptedException exception) {
            return null;
        }
        return model;
    }
    
    private boolean isContextlessSelect(){
        return isLocal() ||
               ((owner instanceof EntityModel==false) && (owner instanceof FormModel==false) && (owner instanceof ReportParamDialogModel==false));
    }
    
    public final IContext.Group createContext(){        
        if (isContextlessSelect()) {
            final RadSelectorPresentationDef presentation = getDefinition().getParentSelectorPresentation();

            if (presentation == null) {
                final String info = getEnvironment().getMessageProvider().translate("ExplorerException", "selector presentation was not defined");
                throw new ModelCreationError(ModelCreationError.ModelType.GROUP_MODEL, null, getDefinition(), null, info);
            }            
            return new IContext.ContextlessSelect(getOwner(), presentation);
        } else if (owner instanceof EntityModel) {
            return new IContext.ParentSelect((EntityModel) owner, this);
        } else if (owner instanceof FormModel) {
            return new IContext.FormSelect((FormModel) owner, this);
        } else if (owner instanceof ReportParamDialogModel) {
            return new IContext.ReportSelect((ReportParamDialogModel) owner, this);
        } else {
            throw new UnsupportedOperationException("Failed to create context for \'"+owner.getClass().getName()+"\' owner");
        }
    }

    public boolean canOpenGroupModel() {
        return getDefinition().getParentSelectorPresentation() != null;
    }

    public boolean canOpenParentSelector() {
        final RadSelectorPresentationDef presentation = getDefinition().getParentSelectorPresentation();
        return presentation != null
                && (presentation.getRuntimeEnvironmentType() == ERuntimeEnvironmentType.COMMON_CLIENT
                || presentation.getRuntimeEnvironmentType() == getEnvironment().getApplication().getRuntimeEnvironmentType());
    }

    private boolean setupGroupModel(final GroupModel group) throws InterruptedException {
        if (condition != null) {
            try {
                group.setCondition(condition);
            } catch (ObjectNotFoundError err) {
                owner.showException(getEnvironment().getMessageProvider().translate("ExplorerError", "Can't Open Selector"), err);
                return false;
            } catch (ServiceClientException ex) {
                group.showException(ex);
                return false;
            }
        }
        for (Map.Entry<Id, Object> propertyValue : propertyValues.entrySet()) {
            group.getProperty(propertyValue.getKey()).setValueObject(propertyValue.getValue());
        }
        if (customInitialFilter) {
            group.getFilters().setDefaultFilterId(initialFilterId);
        }
        if (initialSortingIdByFilterId != null) {
            final Sortings sortings = group.getSortings();
            for (Map.Entry<Id, Id> entry : initialSortingIdByFilterId.entrySet()) {
                sortings.setDefaultSortingId(entry.getValue(), entry.getKey());
            }
        }
        return true;
    }

    @Override
    public final EValType getType() {
        return EValType.ARR_REF;
    }

    @Override
    public final void setPredefinedValuesForArrayItem(final List<Object> values) {
        if (values == null) {
            super.setPredefinedValues(null);
        } else {
            final List<Object> result = new LinkedList<>();
            for (Object value : values) {
                if (value instanceof Pid) {
                    result.add(new Reference((Pid) value));
                } else {
                    result.add(value);
                }
            }
            super.setPredefinedValues(result);
        }
    }

    public final ArrRef updateTitles(final ArrRef value) throws InterruptedException, ServiceClientException {
        if (value == null || value.isEmpty()) {
            return value;
        }
        final Id tableId = getDefinition().getReferencedTableId();
        final EntityObjectTitlesProvider titlesProvider;
        if (getDefinition().getParentSelectorPresentation() == null) {
            titlesProvider =
                new EntityObjectTitlesProvider(getEnvironment(), tableId);
        } else {
            titlesProvider =
                new EntityObjectTitlesProvider(getEnvironment(), tableId, createContext());
        }
        for (Reference ref : value) {
            if (ref != null && ref.getPid() != null) {
                if (!Objects.equals(ref.getPid().getTableId(), tableId)) {
                    final String message = "Object %1$s belongs to a different entity (%2$s)";
                    throw new IllegalArgumentException(String.format(message, ref.getPid().toString(), tableId));
                }
                titlesProvider.addEntityObjectReference(ref);
            }
        }
        final EntityObjectTitles titles = titlesProvider.getTitles();
        final ArrRef newValues = new ArrRef();
        for (Reference ref : value) {
            if (ref == null || ref.getPid() == null) {
                newValues.add(ref);
            } else {
                newValues.add(titles.getEntityObjectReference(ref.getPid()));
            }
        }
        return newValues;
    }
}