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

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.errors.ModelCreationError;
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.meta.filters.RadFilterParamDef;
import org.radixware.kernel.common.client.meta.RadParentRefPropertyDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.FormModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.client.views.IExplorerItemView;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;

public abstract class PropertyReference extends Property {

    public PropertyReference(Model owner, RadParentRefPropertyDef def) {
        super(owner, def);
    }

    public PropertyReference(Model owner, RadFilterParamDef def) {
        super(owner, def);
    }

    protected final Id getParentTableId() {
        if (getDefinition() instanceof RadFilterParamDef) {
            return ((RadFilterParamDef) getDefinition()).getReferencedTableId();
        } else {
            return ((RadParentRefPropertyDef) getDefinition()).getReferencedTableId();
        }
    }

    @Override
    public void setServerValue(final PropertyValue value) {
        final Reference ref = (Reference) value.getValue();
        if (ref != null && ref.getPid() == null && !ref.isBroken()) {
            getEditMask().setNoValueStr(ref.getTitle());
        }
        final boolean wasActivated = isActivated();
        super.setServerValue(value);
        if (wasActivated && ref!=null){
            final Reference currentRef = getInternalVal();
            if (currentRef!=null && !Objects.equals(ref.getTitle(), currentRef.getTitle())){
                setValueTitle(ref.getTitle());
            }
        }
    }

//	неопубликованный метод, только для model.setter
    protected void setInternalVal(final Reference reference) {
        if (!(internalValue instanceof NoValue) && Reference.exactlyMatch((Reference) internalValue.getValue(), reference) && hasOwnValue()) {
            return;
        }
        setInternalValObject(reference);
        if (getSynchronizedProperty() != null) {
            ((PropertyReference) getSynchronizedProperty()).setInternalVal(reference);
        }
    }

    public Reference getServerVal() {
        if (!isActivated()) {
            activate();
        }
        return (Reference) serverValue.getValue();
    }

    public Reference getVal() {
        return (Reference) getValueObject();
    }
    
    private Reference getInternalVal(){
        return (Reference)internalValue.getValue();
    }

    public void setValueTitle(final String title) {
        final Reference currentVal = getInternalVal();
        if (currentVal!=null && !Objects.equals(title, currentVal.getTitle())) {
            final Reference newVal = new Reference(currentVal);
            newVal.setTitle(title);            
            setInternalVal(newVal);
            afterModify();
        }
    }

    @Override
    public Object getServerValObject() {
        if (isLocal()){
            throw new IllegalUsageError("This property cannot have server value (you may want to use getInitialValue method)");
        }        
        return getServerVal();
    }

    @Override
    public Class<?> getValClass() {
        return Reference.class;
    }

    @Override
    public Object getValObjectImpl() {
        if (needForActivation()) {
            activate();
        }
        final Reference ref = (Reference) internalValue.getValue();
        return ref != null && !ref.isValid() && !ref.isBroken() ? null : ref;
    }

    @Override
    public void setValObjectImpl(final Object reference) {
        setInternalVal((Reference) reference);
    }

    protected final IContext.Entity getEntityEditingContext(final boolean insertIntoTree) {
        if (insertIntoTree){
            return new IContext.ReferencedChoosenEntityEditing(this);
        }
        else if (!isLocal() && (owner instanceof EntityModel || owner instanceof FormModel)){
            return new IContext.ReferencedEntityEditing(this);
        }else{
            return new IContext.ContextlessEditing(getEnvironment(),null,owner);
        }
    }

    //Создать модель для редактора объекта
    /**
     *
     * @return модель сущности, на которую в данный момент ссылается свойство
     * @throws ServiceClientException
     * @throws InterruptedException
     */
    public EntityModel openEntityModel() throws ServiceClientException, InterruptedException {
        return openEntityModel(false);
    }
    
    public boolean canOpenEntityModelView(){
        if (canOpenEntityModel()){
            final RadParentRefPropertyDef propertyDef = (RadParentRefPropertyDef) getDefinition();
            return !propertyDef.getParentEditorRestrictions().getIsViewRestricted() && !getVal().isEditorRestricted();
        }else{
            return false;
        }
    }    
    
    public abstract boolean canModifyEntityObject();

    public boolean canOpenEntityModel() {
        if (getDefinition() instanceof RadFilterParamDef) {
            return false;
        }        
        if (getVal() == null || !getVal().isValid() || getVal().isEntityModelRestricted()){
           return false;
        }
        final RadParentRefPropertyDef propertyDef = (RadParentRefPropertyDef) getDefinition();
        return propertyDef.isObjectEditorPresentationDefined();
    }

    protected final EntityModel openEntityModel(final boolean insertIntoTree) throws ServiceClientException, InterruptedException {
        if (getDefinition() instanceof RadFilterParamDef) {
            throw new IllegalUsageError("Cannot open entity model by value of filter parameter");
        }
        final RadParentRefPropertyDef propertyDef = (RadParentRefPropertyDef) getDefinition();
        final IContext.Entity context = getEntityEditingContext(insertIntoTree);
        if (!propertyDef.isObjectEditorPresentationDefined()) {
            final String info = getEnvironment().getMessageProvider().translate("ExplorerException", "editor presentation was not defined for definition:\n%s");
            throw new ModelCreationError(ModelCreationError.ModelType.ENTITY_MODEL, null, propertyDef, context, String.format(info, propertyDef.getDescription()));
        }
        if (getVal() == null) {
            final String info = getEnvironment().getMessageProvider().translate("ExplorerException", "value of %s is not defined");
            throw new ModelCreationError(ModelCreationError.ModelType.ENTITY_MODEL, null, propertyDef, context, String.format(info, propertyDef.getDescription()));
        }

        if (getVal().isEntityModelRestricted()) {
            final String info = getEnvironment().getMessageProvider().translate("ExplorerException", "Parent entity model is not accessible");
            throw new ModelCreationError(ModelCreationError.ModelType.ENTITY_MODEL, null, propertyDef, context, String.format(info, propertyDef.getDescription()));
        }
        //RADIX-7675 При потере фокуса в поле ввода сеттер свойства может быть еще не вызван,
        //поэтому нужно сделать принудительные вызовы сеттеров
        owner.finishEdit();
        final Collection<Id> presentations = propertyDef.getObjectEditorPresentationIds();                
        final EntityModel entityModel;
        try {
            entityModel = 
                EntityModel.openModel(getVal().getPid(), propertyDef.getReferencedClassId(), presentations, context);
        } catch (ObjectNotFoundError error) {
            if (error.inContextOf(getVal())) {
                error.setContextReference(getEnvironment(), getVal());
            }
            throw error;
        }
        afterOpenEntityModel(entityModel, insertIntoTree);
        return entityModel;
    }
    
    public void afterOpenEntityModel(final EntityModel entity, final boolean forInsertIntoTree){        
    }

    abstract public EntityModel openEntityEditor() throws ServiceClientException, InterruptedException;
    
    public boolean canInsertEntityIntoTree(){
        return owner.canInsertReferencedObjectIntoTree(getDefinition().getId()) && 
               canOpenEntityModelView() &&
               !((RadParentRefPropertyDef) getDefinition()).getParentEditorRestrictions().getIsViewRestricted();
    }

    public IExplorerItemView insertEntityIntoTree() throws ServiceClientException, InterruptedException {
        final EntityModel entity = openEntityModel(true);
        if (!entity.canOpenEntityView()){            
            throw new IllegalUsageError("Can't insert object in this environment");
        }
        IExplorerItemView explorerItemView = owner.findNearestExplorerItemView();
        if (entity!=null && explorerItemView != null) {
            //RADIX-2348 Проверка был ли уже вставлен такой же объект выше по дереву.
            final List<EntityModel> parentEntityModels = explorerItemView.getParentEntityModels();
            for (EntityModel parentEntityModel: parentEntityModels){
                if (entity.getPid().equals(parentEntityModel.getPid()) &&
                    entity.getDefinition().getId().equals(parentEntityModel.getDefinition().getId())
                   ){
                    explorerItemView = parentEntityModel.findNearestExplorerItemView();
                    final String messagetitle = 
                        getEnvironment().getMessageProvider().translate("Selector", "Confirm to Change Current Explorer Item");
                    final String messageTemplate = 
                        getEnvironment().getMessageProvider().translate("Selector", "Object '%s' was already inserted.\nDo you want to make current corresponding explorer item?");
                    if (getEnvironment().messageConfirmation(messagetitle, String.format(messageTemplate,entity.getTitle()))){
                        explorerItemView.setCurrent();
                    }
                    return explorerItemView;
                }
            }            
            return explorerItemView.insertEntity(entity);
        }
        return null;
    }

    @Override
    public EnumSet<ETextOptionsMarker> getTextOptionsMarkers() {
        if (getVal() != null && getVal().isBroken()){
            final EnumSet<ETextOptionsMarker> markers = super.getTextOptionsMarkers().clone();
            markers.add(ETextOptionsMarker.BROKEN_REFERENCE);
            return markers;
        }else{
            return super.getTextOptionsMarkers();
        }
    }

    @Override
    public Reference getInitialValue() {
        return (Reference)initialValue.getCopyOfValue();
    }
    
    public final void setInitialValue(Reference value){
        if (!Reference.exactlyMatch((Reference) initialValue.getValue(), value)){
            this.setInitialValObject(value);
            if (getSynchronizedProperty()!=null){
                ((PropertyReference)getSynchronizedProperty()).setInitialValue(value);
            }
        }
    }    
}
