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
import org.radixware.kernel.common.client.errors.CantOpenEditorError;
import org.radixware.kernel.common.client.errors.CantOpenSelectorError;
import org.radixware.kernel.common.client.errors.ModelCreationError;
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.errors.ParentRefSetterError;
import org.radixware.kernel.common.client.errors.SettingPropertyValueError;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.meta.filters.RadFilterParamDef;
import org.radixware.kernel.common.client.meta.RadParentRefPropertyDef;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.FormModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.IEntitySelectionController;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.ReportParamDialogModel;
import org.radixware.kernel.common.client.models.groupsettings.Sortings;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.client.views.IEntityEditorDialog;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.client.views.IPropEditor;
import org.radixware.kernel.common.client.views.ISelectEntityDialog;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.scml.SqmlExpression;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.eas.PropertyList;
import org.radixware.schemas.eas.SetParentRs;

public class PropertyRef extends PropertyReference {

    private org.radixware.schemas.xscml.Sqml condition = null;
    private Id initialFilterId;
    private boolean customInitialFilter;
    private Map<Id, Id> initialSortingIdByFilterId;
    private GroupModel parentGroupModel;
    private IEntitySelectionController selectionController;
    private GroupModel.SelectionListener selectionListener;
    private boolean internalValueChange;//изменение значения происходит в результате setParent у другого свойства
    private IProgressHandle pHandle = null;
    private final Map<Id, Object> propertyValues = new HashMap<>(16);

    private IProgressHandle getProgressHandle() {
        if (pHandle == null) {
            pHandle = getEnvironment().getProgressHandleManager().newStandardProgressHandle();
        }
        return pHandle;
    }

    public PropertyRef(Model owner, RadParentRefPropertyDef def) {
        super(owner, def);
    }

    public PropertyRef(Model owner, RadFilterParamDef def) {
        super(owner, def);
    }

    @Override
    public final RadPropertyDef getDefinition() {
        return super.getDefinition();
    }

    @Override
    public IPropEditor createPropertyEditor() {
        return getEnvironment().getApplication().getStandardViewsFactory().newPropRefEditor(this);
    }

    public org.radixware.schemas.xscml.Sqml getCondition() {
        return condition;
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

    public final void setCondition(org.radixware.schemas.xscml.Sqml condition) {
        this.condition = condition == null ? null : (org.radixware.schemas.xscml.Sqml) condition.copy();
    }

    public final void setCondition(SqmlExpression expression) {
        if (expression != null) {
            condition = expression.asXsqml();
        } else {
            condition = null;
        }
    }

    public final void setGroupPropertyValue(final Id propertyId, final Object value) {
        final RadSelectorPresentationDef presentation = getParentSelectorPresentation();
        if (presentation != null) {
            presentation.getPropertyDefById(propertyId);//check if property exists
        }
        propertyValues.put(propertyId, value);
    }

    public final void clearGroupPropertyValues() {
        propertyValues.clear();
    }

    public final void setEntitySelectionController(IEntitySelectionController controller) {
        selectionController = controller;
    }
    
    public final void setEntitySelectionListener(final GroupModel.SelectionListener listener){
        selectionListener = listener;
    }

    public final void setDefaultFilterId(final Id filterId) {
        initialFilterId = filterId;
        customInitialFilter = true;
    }
    
    public final void unsetDefaultFilter(){
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

    @Override
    public void setValObjectImpl(final Object value) {
        if (needForActivation()) {
            activate();
        }
        final Reference reference;
        if (value == null) {
            reference = null;
        } else if (value instanceof EntityModel) {
            final Pid pid = ((EntityModel) value).getPid();
            reference = new Reference(pid, ((EntityModel) value).getTitle());
        } else if (value instanceof Reference) {
            reference = (Reference) value;
        } else if (value instanceof String) {
            reference = new Reference(new Pid(getParentTableId(), (String) value), "");
        } else if (value instanceof Pid) {
            reference = new Reference((Pid) value, "");
        } else {
            throw new ParentRefSetterError(this, "Invalid value type \"" + value.getClass().toString() + "\"");
        }

        if (internalValue.hasSameValue(reference) && internalValue.isOwn()) {
            return;
        }
        if (((owner instanceof EntityModel)
                || (owner instanceof FormModel)
                || (owner instanceof ReportParamDialogModel))
                && !isLocal()
                && (getDefinition().getNature() != EPropNature.PARENT_PROP)
                && !internalValueChange) {
            setParent(reference);
        }//if (owner instanceof EntityModel)
        else {
            setInternalVal(reference);
        }
    }

    private PropertyList executeSetParentRequest(final Pid pid) throws ServiceClientException {
        final Model ownerModel = getOwner();
        try {
            final SetParentRs response = getEnvironment().getEasSession().setParent(ownerModel, getId(), pid);
            if (ownerModel instanceof EntityModel) {
                final EntityModel entity = (EntityModel) ownerModel;
                entity.setTitle(response.getObjectTitle());
            }
            return response.getProperties();
        } catch (InterruptedException ex) {
            return null;
        }
    }

    private void readParent(final Pid pid) throws ServiceClientException {
        final PropertyList propList = executeSetParentRequest(pid);
        if (propList != null && propList.getItemList() != null && !propList.getItemList().isEmpty()) {
            final List<PropertyValue> values = parseSetParentResponse(propList, true);
            for (int i = values.size() - 1; i >= 0; i--) {
                if (getId().equals(values.get(i).getPropertyDef().getId())) {
                    setInternalVal((Reference) values.get(i).getValue());
                    values.remove(i);
                    break;
                }
            }
            if (!values.isEmpty()) {
                ((EntityModel) getOwner()).setServerPropertyValues(values);
            }
        }
    }

    private List<PropertyValue> parseSetParentResponse(final PropertyList propList, final boolean ignoreModifiedProps) {
        final List<PropertyValue> values = new LinkedList<>();
        Property property;
        Id entityId;
        for (PropertyList.Item p : propList.getItemList()) {
            if (p.getId().equals(getId())) {
                final Reference value = (Reference) ValueConverter.easPropXmlVal2ObjVal(p, getDefinition().getType(), getParentTableId());
                values.add(new PropertyValue(getDefinition(), value));
            } else {
                try {
                    property = owner.getProperty(p.getId());
                } catch (DefinitionError error) {
                    final String msg = getEnvironment().getMessageProvider().translate("TraceMessage", "Cannot read property #%s: %s\n%s");
                    final String reason = ClientException.getExceptionReason(getEnvironment().getMessageProvider(), error);
                    final String stack = ClientException.exceptionStackToString(error);
                    getEnvironment().getTracer().debug(String.format(msg, p.getId().toString(), reason, stack));
                    continue;
                }
                if (!property.getDefinition().isReadSeparately()//отдельно-читаемые свойства здесь игнорируются
                        && (!ignoreModifiedProps || !property.isValEdited())) {
                    if (property instanceof PropertyRef) {
                        entityId = ((RadParentRefPropertyDef) property.getDefinition()).getReferencedTableId();
                    } else {
                        entityId = null;
                    }
                    final PropertyValue value = new PropertyValue(getEnvironment(), p, property.getDefinition(), entityId);
                    if (property instanceof PropertyXml) {
                        try {
                            value.refineValue(((PropertyXml) property).castValue(value.getValue()));
                        } catch (Exception ex) {
                            getEnvironment().processException(new SettingPropertyValueError(property, ex));
                        }
                    }
                    values.add(value);
                }
            }
        }
        return values;
    }

    private static void changeInternalPropertyValue(final Property property, final PropertyValue newValue) {
        final boolean sameValue = property.internalValue.hasSameValue(newValue);
        property.setInternalValue(newValue);
        if (property.isValEdited() && !sameValue) {
            //RADIX-6561, DBP-1547
            property.setValueModifiedByChangingParentRef(true);
        }
    }

    private void setParent(final Reference reference) {
        final PropertyList propList;
        final Pid pid = reference == null ? null : reference.getPid();
        try {
            propList = executeSetParentRequest(pid);
        } catch (ObjectNotFoundError error) {
            if (reference != null) {
                error.setContextReference(getEnvironment(), reference);
            }
            afterModify();//вернуть прежнее значение в редакторе свойства
            throw new ParentRefSetterError(this, error);
        } catch (ServiceClientException exception) {
            afterModify();//вернуть прежнее значение в редакторе свойства
            throw new ParentRefSetterError(this, exception);
        }
        if (propList != null && propList.getItemList() != null && !propList.getItemList().isEmpty()) {
            final List<PropertyValue> newValues = parseSetParentResponse(propList, false);
            Property property;
            for (PropertyValue newValue : newValues) {
                if (getId().equals(newValue.getPropertyDef().getId())) {
                    setInternalVal((Reference) newValue.getValue());
                } else {
                    property = owner.getProperty(newValue.getPropertyDef().getId());
                    try {
                        //RADIX-3677
                        if (newValue.getPropertyDef().getType() == EValType.PARENT_REF) {
                            //to avoid recursive setParent
                            final PropertyRef propertyRef = (PropertyRef) property;
                            propertyRef.internalValueChange = true;
                            try {
                                changeInternalPropertyValue(property, newValue);
                            } finally {
                                propertyRef.internalValueChange = false;
                            }
                        } else {
                            changeInternalPropertyValue(property, newValue);
                        }
                    } catch (Exception ex) {
                        getEnvironment().processException(new SettingPropertyValueError(property, ex));
                    }
                }
            }
            if (owner instanceof EntityModel) {
                ((EntityModel) owner).afterSetParent(this);
            } else if (owner instanceof FormModel) {
                ((FormModel) owner).afterSetParent(this);
            } else if (owner instanceof ReportParamDialogModel) {
                ((ReportParamDialogModel) owner).afterSetParent(this);
            }
        }
    }

    @Override
    public void setValueObject(final Object value) {
        setValObjectImpl(value);
    }

    @Override
    public EntityModel openEntityEditor() throws ServiceClientException, InterruptedException {
        getProgressHandle().startProgress(getEnvironment().getMessageProvider().translate("Wait Dialog", "Opening Editor..."), true);
        final EntityModel entity;
        final IEntityEditorDialog dialog;
        try {
            entity = openEntityModel();
            if (entity.getRestrictions().getIsViewRestricted()) {
                final String message = entity.getEnvironment().getMessageProvider().translate("ExplorerError", "insufficient privileges");
                throw new CantOpenEditorError(entity, message);
            } else if (!entity.canOpenEntityView()) {
                throw new CantOpenEditorError(entity, new IllegalUsageError("not sutable runtime environment"));
            }
            if (getProgressHandle().wasCanceled()) {
                throw new InterruptedException();
            }
            dialog = getEnvironment().getApplication().getStandardViewsFactory().newEntityEditorDialog(entity);
            if (getProgressHandle().wasCanceled()) {
                throw new InterruptedException();
            }
        } finally {
            getProgressHandle().finishProgress();
        }
        dialog.execDialog();
        if (dialog.entityWasChanged() && (owner instanceof EntityModel) && !isLocal()) {
            //RADIX-3157,RADIX-6806
            //need to update values of dependent properties so use of just readProperty is not  enough here
            try {
                readParent(dialog.getEntityModel().getPid());
            } catch (ServiceClientException ex) {
                final String traceMessage = ClientException.exceptionStackToString(ex);
                getEnvironment().getTracer().debug(traceMessage);
            }
        }
        return entity;
    }

    @Override
    public final boolean canModifyEntityObject(){
        if (canOpenEntityModel()){
            final RadParentRefPropertyDef propertyDef = (RadParentRefPropertyDef) getDefinition();
            return !propertyDef.getParentEditorRestrictions().getIsUpdateRestricted() && !getVal().isModificationRestricted();
        }else{
            return false;
        }        
    }
        
    public boolean canOpenParentSelector() {
        final RadSelectorPresentationDef presentation = getParentSelectorPresentation();
        return presentation != null
                && (presentation.getRuntimeEnvironmentType() == ERuntimeEnvironmentType.COMMON_CLIENT
                || presentation.getRuntimeEnvironmentType() == getEnvironment().getApplication().getRuntimeEnvironmentType());
    }
    
    public boolean canOpenGroupModel(){
        return getParentSelectorPresentation()!=null;
    }        

    public GroupModel openGroupModel() {
       
        final RadSelectorPresentationDef presentation = getParentSelectorPresentation();

        if (presentation == null) {
            final String info = getEnvironment().getMessageProvider().translate("ExplorerException", "selector presentation was not defined");
            throw new ModelCreationError(ModelCreationError.ModelType.GROUP_MODEL, null, getDefinition(), null, info);
        }        

        final GroupModel model = presentation.createModel(createGroupContext());
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
    
    public final IContext.Group createGroupContext(){
        if (isContextlessSelect()) {
            final RadSelectorPresentationDef presentation = getParentSelectorPresentation();

            if (presentation == null) {
                final String info = getEnvironment().getMessageProvider().translate("ExplorerException", "selector presentation was not defined");
                throw new ModelCreationError(ModelCreationError.ModelType.GROUP_MODEL, null, getDefinition(), null, info);
            }       
            return new IContext.ContextlessSelect(this, presentation);
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

    public RadSelectorPresentationDef getParentSelectorPresentation() {
        if (getDefinition() instanceof RadParentRefPropertyDef) {
            return ((RadParentRefPropertyDef) getDefinition()).getParentSelectorPresentation();
        } else {
            return ((RadFilterParamDef) getDefinition()).getParentSelectorPresentation();
        }
    }

    private boolean canClear() {
        return !isMandatory() && isOwnValueAcceptable(null) && getVal()!=null;
    }

    public Reference selectParent() throws InterruptedException {
        getProgressHandle().startProgress(getEnvironment().getMessageProvider().translate("Wait Dialog", "Opening Selector..."), true);
        final ISelectEntityDialog dialog;
        try {
            if (parentGroupModel == null) {
                parentGroupModel = openGroupModel();
            } else if (!setupGroupModel(parentGroupModel)) {
                return null;
            }
            if (parentGroupModel == null) {
                if (!isActivated()) {
                    activate();
                }
                return (Reference) internalValue.getValue();
            }
            if (!parentGroupModel.canOpenGroupView()) {
                throw new IllegalUsageError("Can't open selector in this environment");
            }
            //RADIX-7675 При потере фокуса в поле ввода сеттер свойства может быть еще не вызван,
            //поэтому нужно сделать принудительные вызовы сеттеров
            owner.finishEdit();
            if (!owner.beforeSelectParent(this, parentGroupModel)) {
                if (!isActivated()) {
                    activate();
                }
                return (Reference) internalValue.getValue();
            }
            if (getProgressHandle().wasCanceled()) {
                parentGroupModel.clean();
                throw new InterruptedException();
            }
            try {
                dialog = getEnvironment().getApplication().getStandardViewsFactory().newSelectEntityDialog(parentGroupModel, canClear());
            } catch (CantOpenSelectorError err) {
                if (err.getCause() instanceof ObjectNotFoundError) {
                    final ObjectNotFoundError objectNotFound = (ObjectNotFoundError) err.getCause();
                    if ((owner instanceof EntityModel) && objectNotFound.inContextOf((EntityModel) owner)) {
                        owner.showException(getEnvironment().getMessageProvider().translate("ExplorerError", "Can't Open Selector"), objectNotFound);
                        return (Reference) internalValue.getValue();
                    }
                }
                throw err;
            }
            if (getProgressHandle().wasCanceled()) {
                parentGroupModel.clean();
                throw new InterruptedException();
            }
        } finally {
            getProgressHandle().finishProgress();
        }
        final DialogResult result = dialog.execDialog();
        parentGroupModel.clean();
        if (result == DialogResult.ACCEPTED) {
            return dialog.getSelectedEntity() == null ? null : new Reference(dialog.getSelectedEntity());
        } else {
            if (!isActivated()) {
                if (needForActivation()) {
                    activate();
                } else {
                    return null;
                }
            }
            return (Reference) internalValue.getValue();
        }
    }

    public EntityModel selectEntityModel() throws InterruptedException {
        getProgressHandle().startProgress(getEnvironment().getMessageProvider().translate("Wait Dialog", "Opening Selector..."), true);
        final ISelectEntityDialog dialog;
        try {
            if (parentGroupModel == null) {
                parentGroupModel = openGroupModel();
            } else if (!setupGroupModel(parentGroupModel)) {
                return null;
            }
            if (parentGroupModel == null) {
                return null;
            }
            if (!parentGroupModel.canOpenGroupView()) {
                throw new IllegalUsageError("Can't open selector in this environment");
            }
            if (!owner.beforeSelectParent(this, parentGroupModel)) {
                return null;
            }

            if (getProgressHandle().wasCanceled()) {
                parentGroupModel.clean();
                throw new InterruptedException();
            }
            try {
                dialog = getEnvironment().getApplication().getStandardViewsFactory().newSelectEntityDialog(parentGroupModel, canClear());
            } catch (CantOpenSelectorError err) {
                if (err.getCause() instanceof ObjectNotFoundError) {
                    final ObjectNotFoundError objectNotFound = (ObjectNotFoundError) err.getCause();
                    if ((owner instanceof EntityModel) && objectNotFound.inContextOf((EntityModel) owner)) {
                        owner.showException(getEnvironment().getMessageProvider().translate("ExplorerError", "Can't Open Selector"), objectNotFound);
                        return null;
                    }
                }
                throw err;
            }
            if (getProgressHandle().wasCanceled()) {
                parentGroupModel.clean();
                throw new InterruptedException();
            }
        } finally {
            getProgressHandle().finishProgress();
        }
        final DialogResult result = dialog.execDialog();
        parentGroupModel.clean();
        return result == DialogResult.ACCEPTED ? dialog.getSelectedEntity() : null;
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
        if (selectionController != null) {
            group.setEntitySelectionController(selectionController);
        }
        if (selectionListener != null){
            group.addSelectionListener(selectionListener);
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
        return EValType.PARENT_REF;
    }

    @Override
    public final void setPredefinedValues(final List<Object> values) {
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
    
    public final Reference updateTitle(final Reference ref) throws InterruptedException, ServiceClientException{
        if (ref == null || !ref.isValid()) {
            return ref;
        }
        if (((owner instanceof EntityModel)
                || (owner instanceof FormModel)
                || (owner instanceof ReportParamDialogModel))
                && !isLocal()
                && (getDefinition().getNature() != EPropNature.PARENT_PROP) ) {
            final Id tableId = ((RadParentRefPropertyDef)getDefinition()).getReferencedTableId();
            if (!Objects.equals(ref.getPid().getTableId(),tableId)){
                final String message = "Object %1$s belongs to a different entity (%2$s)";
                throw new IllegalArgumentException(String.format(message, ref.getPid().toString(), tableId));
            }
            final PropertyList propList = executeSetParentRequest(ref.getPid());
            if (propList != null && propList.getItemList() != null && !propList.getItemList().isEmpty()) {
                for (PropertyList.Item p : propList.getItemList()) {
                    if (p.getId().equals(getId())) {
                        return (Reference)ValueConverter.easPropXmlVal2ObjVal(p, getDefinition().getType(), getParentTableId());
                    }
                }
            }
            return ref;
        }else{
            final RadSelectorPresentationDef presentation = getParentSelectorPresentation();
            if (isContextlessSelect() && presentation==null){
                final String title = ref.getPid().getDefaultEntityTitle(getEnvironment().getEasSession());
                return new Reference(ref.getPid(), title);
            }
            if (!Objects.equals(ref.getPid().getTableId(),presentation.getTableId())){
                final String message = "Object %1$s belongs to a different entity (%2$s)";
                throw new IllegalArgumentException(String.format(message, ref.getPid().toString(), presentation.getTableId()));                
            }
            final EntityObjectTitlesProvider titlesProvider = 
                new EntityObjectTitlesProvider(getEnvironment(), presentation.getTableId(), createGroupContext());
            titlesProvider.addEntityObjectPid(ref.getPid());
            return titlesProvider.getTitles().getEntityObjectReference(ref.getPid());
        }
    }
}