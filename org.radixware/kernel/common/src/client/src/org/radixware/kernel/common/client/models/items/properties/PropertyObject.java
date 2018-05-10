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

import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.enums.EEntityCreationResult;
import org.radixware.kernel.common.client.errors.ModelCreationError;
import org.radixware.kernel.common.client.exceptions.ModelException;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadEditorPresentationDef;
import org.radixware.kernel.common.client.meta.RadParentRefPropertyDef;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.types.InstantiatableClass;
import org.radixware.kernel.common.client.types.InstantiatableClasses;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.client.types.ResolvableReference;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.client.views.IEntityEditorDialog;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.client.views.IPropEditor;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.EReferencedObjectActions;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.eas.PresentableObject;

public class PropertyObject extends PropertyReference {
    
    private final static class NewObjectReference extends Reference{
        
        private static final long serialVersionUID = 3682882556508353826L;
        
        public NewObjectReference(final Reference reference){
            super(reference);
        }

        @Override
        public boolean isValid() {
            return !isBroken();
        }
    }

    private IProgressHandle phHandle = null;
    private boolean canDelete = true, canCreate=true;
    private List<InstantiatableClass> instantiatableClasses;
    private Boolean isUserFunctionObject;
    private EntityModel preparedForCreate;

    public PropertyObject(final Model owner, final RadParentRefPropertyDef def) {
        super(owner, def);
    }

    @Override
    public final RadParentRefPropertyDef getDefinition() {
        return (RadParentRefPropertyDef)super.getDefinition();
    }    

    @Override
    public void setServerValue(final PropertyValue value) {
        if (value.getValue() instanceof EntityModel){
            final EntityModel objValue = (EntityModel)value.getValue();
            if (((EntityModel)owner).isNew()){
                setPreparedForCreate(objValue);
            }else{
                final PropertyValue convertedValue = new PropertyValue(value);
                convertedValue.setValue(new ResolvableReference(objValue));
                super.setServerValue(convertedValue);
            }
        }else{
            super.setServerValue(value);
        }
    }
    
    @Override
    public void setValObjectImpl(final Object obj) {
        if (obj instanceof EntityModel){
            final EntityModel objValue = (EntityModel)obj;
            if (((EntityModel)owner).isNew()){
                setPreparedForCreate(objValue);
            }else{
                super.setValObjectImpl(new Reference(objValue));
            }
        }else{
            super.setValObjectImpl(obj);
        }
    }    
        
    private IProgressHandle getProgressHandle() {
        synchronized (this) {
            if (phHandle == null) {
                phHandle = getEnvironment().getProgressHandleManager().newStandardProgressHandle();
            }
            return phHandle;
        }
    }
    
    public final void setCanDelete(final boolean canDelete){
        if (this.canDelete!=canDelete){
            final boolean resultBefore = canDelete();
            this.canDelete = canDelete;
            if (resultBefore!=canDelete()){
                afterModify();
            }
        }
    }
    
    public final void setCanCreate(final boolean canCreate){
        if (this.canCreate!=canCreate){
            final boolean resultBefore = canCreate();
            this.canCreate = canCreate;
            if (resultBefore!=canCreate()){
                afterModify();
            }
        }
    }    
    
    public final boolean canDelete(){
        if (preparedForCreate==null){
            final Reference value = getVal();
            return !isReadonly() 
                   && value!=null 
                   && !value.isBroken() 
                   && !value.isDeleteRestricted()
                   && canDelete;
        }else{
            return true;
        }
    }        
    
    public final boolean canCreate(){
        if (isReadonly() || owner instanceof EntityModel==false|| !getDefinition().isObjectCreationPresentationDefined() || preparedForCreate!=null){
            return false;
        }
        final ERuntimeEnvironmentType environmentType = 
            getDefinition().getObjectCreationPresentation().getRuntimeEnvironmentType();
        if (environmentType==ERuntimeEnvironmentType.COMMON_CLIENT
             || environmentType==getEnvironment().getApplication().getRuntimeEnvironmentType()){
            return canCreate;
        }else{
            return false;
        }
    }
    
    public final boolean canCopy(){
        final Reference value = getVal();
        return value!=null
               && !value.isBroken()
               && !isReadonly()
               && (((EntityModel)owner).isExists() || preparedForCreate!=null)
               && !getReferencedClassDef().getRestrictions().getIsCopyRestricted();
    }        
    
    public final boolean canPaste(final EntityModel src){
        if (src.isExists()){
            return autoSelectCreationClassId(src)!=null;
        }else{
            return false;
        }
    }

    @Override
    public boolean canModifyEntityObject() {
        if (preparedForCreate!=null){
            return true;
        }else if (!isReadonly() && canOpenEntityModel()){
            return !getVal().isModificationRestricted();
        }else{
            return false;
        }
    }
    
    private boolean isUserFunctionObject(){
        if (isUserFunctionObject==null){            
            isUserFunctionObject = Boolean.valueOf(getReferencedClassDef().isUserFunction());
        }
        return isUserFunctionObject.booleanValue();
    }
    
    private RadClassPresentationDef getReferencedClassDef(){
        final Id referencedClassId = getDefinition().getReferencedClassId();
        return getEnvironment().getDefManager().getClassPresentationDef(referencedClassId);                
    }
    
    @Override
    public boolean isReadonly() {
        return super.isReadonly()
               || (isUserFunctionObject() && !getEnvironment().getApplication().isExtendedMetaInformationAccessible());
    }
    
    private String getOpenEditorMessage(){
        if (isUserFunctionObject()){
            final String waitDialogMessage = 
                getEnvironment().getMessageProvider().translate("Wait Dialog", "Open editor of user-defined function \"%1$s\"");
            return String.format(waitDialogMessage, getTitle());
        }else{
            return
                getEnvironment().getMessageProvider().translate("Wait Dialog", "Opening Editor...");
        }        
    }
    
    public EntityModel create() throws ServiceClientException, InterruptedException {
        return create(null);
    }

    public EntityModel create(final EntityModel src) throws ServiceClientException, InterruptedException {
        if (preparedForCreate!=null){
            return preparedForCreate;
        }
        final boolean ownerWasNotCreated = ((EntityModel) owner).isNew();
        final IProgressHandle progressHandle = getProgressHandle();        
        progressHandle.startProgress(getOpenEditorMessage(), true);
        EntityModel entity;
        final IEntityEditorDialog dialog;
        try {
            entity = createPrepareCreateModel(null, src, progressHandle);
            if (progressHandle.wasCanceled()) {
                throw new InterruptedException();
            }
            if (entity==null){
                return null;
            }
            if (entity.getCustomViewId() == null
                && entity.getEditorPresentationDef().getEditorPages().getTopLevelPages().isEmpty()) {
                //RADIX-2567
                if (ownerWasNotCreated){                    
                    setPreparedForCreate(entity);
                    return entity;
                }else{
                    dialog = null;
                    try{
                        if (entity.create()!= EEntityCreationResult.SUCCESS){
                            return null;
                        }
                    }
                    catch(ModelException exception){
                        final String errorMessageTitle = getEnvironment().getMessageProvider().translate("Editor", "Failed to create entity");
                        entity.showException(errorMessageTitle, exception);
                        return null;
                    }
                }
            } else {
                dialog = getEnvironment().getApplication().getStandardViewsFactory().newEntityEditorDialog(entity);
                if (getProgressHandle().wasCanceled()) {
                    throw new InterruptedException();
                }
                ((EntityModel) owner).beforeOpenPropertyObjectEditor(this, entity);
                if (getProgressHandle().wasCanceled()) {
                    throw new InterruptedException();
                }
            }
        } finally {
            progressHandle.finishProgress();
        }
        if (dialog!=null){
            entity = dialog.execDialog()==IDialog.DialogResult.ACCEPTED ? dialog.getEntityModel() : null;
        }//else entity!=null
        if (ownerWasNotCreated){
            if (entity!=null){
                setPreparedForCreate(entity);
            }            
        }else if (entity!=null && entity.isExists()) {
            //after create property already have this value. Update operation is not need.
            final PropertyValue newServerValue = new PropertyValue(serverValue);
            newServerValue.setValue(new Reference(entity.getPid(), entity.getTitle()));
            setServerValue(newServerValue);
            ((EntityModel) owner).afterChangePropertyObject(this);
        }
        return entity;
    }
    
    private void setPreparedForCreate(final EntityModel entity){        
        final EnumSet<EReferencedObjectActions> allowedActions = EnumSet.noneOf(EReferencedObjectActions.class);
        final PropertyValue newValue;
        if (serverValue==null){
            newValue = new PropertyValue(getDefinition(),null);
        }else{
            newValue = new PropertyValue(serverValue);
        }
        if (entity==null){
            newValue.setValue(null);
        }else{
            if (!entity.canOpenEntityView()){
                allowedActions.remove(EReferencedObjectActions.VIEW);
            }
            if (entity.getRestrictions().getIsUpdateRestricted()){
                allowedActions.remove(EReferencedObjectActions.MODIFY);
            }
            if (entity.getRestrictions().getIsDeleteRestricted()){
                allowedActions.remove(EReferencedObjectActions.DELETE);
            }            
            newValue.setValue( new Reference(null, getNewObjectTitle(entity), null, allowedActions, entity.getClassId()) );
        }        
        serverValue  = newValue;
        internalValue = new PropertyValue(newValue);
        setValEdited(true);
        preparedForCreate = entity;
        if (entity==null){
            setValueTitle(getEditMask().getNoValueStr(getEnvironment().getMessageProvider()));
        }else{
            setValueTitle(getNewObjectTitle(entity));
        }
        ((EntityModel) owner).afterChangePropertyObject(this);        
        applyChangesInPreparedForCreateModel();
        afterModify();
    }
    
    public EntityModel initPrepareCreateModel(final Id classId, final EntityModel src, final IProgressHandle progressHandle) throws InterruptedException, ServiceClientException{        
        final EntityModel entityModel = createPrepareCreateModel(classId, src, progressHandle);
        if (entityModel==null){
            return null;
        }else{
            setPreparedForCreate(entityModel);
            return preparedForCreate;
        }
    }    
    
    @Override
    public Object getValObjectImpl() {
        if (preparedForCreate==null || internalValue.getValue()==null){
            return super.getValObjectImpl();
        }else{
            final Reference value = (Reference)internalValue.getValue();
            if (preparedForCreate==null){
                return new Reference(value);
            }else{
                return new NewObjectReference(value);
            }
        }
    }    
    
    @Override
    public ValidationResult validateValue() {
        if (preparedForCreate==null || internalValue.getValue()==null){
            return super.validateValue();
        }else{
            return ValidationResult.ACCEPTABLE;
        }        
    }    
    
    private EntityModel createPrepareCreateModel(final Id classId, final EntityModel src, final IProgressHandle progressHandle) throws InterruptedException, ServiceClientException{
        final boolean ownerWasNotCreated = ((EntityModel) owner).isNew();
        EntityModel entity;
        final IEntityEditorDialog dialog;
        if (!(owner instanceof EntityModel)) {
            final String info = getEnvironment().getMessageProvider().translate("ExplorerError", "owner of object property must be entity model");
            throw new ModelCreationError(ModelCreationError.ModelType.ENTITY_MODEL_FOR_NEW, owner.getDefinition(), getDefinition(), owner.getContext(), info);
        }

        final IContext.ObjectPropCreating context = new IContext.ObjectPropCreating((EntityModel) owner, getDefinition().getId());

        final RadParentRefPropertyDef propertyDef = getDefinition();
        final RadEditorPresentationDef epd = propertyDef.getObjectCreationPresentation();

        if (epd==null){
            final String message = getEnvironment().getMessageProvider().translate("ExplorerException", "Can't load  creation presentation for definition:\n%s");
            throw new ModelCreationError(ModelCreationError.ModelType.ENTITY_MODEL, null, propertyDef, context, String.format(message, propertyDef.getDescription()));                
        }

        final Id classOfEntityId;
        if(src==null){
            if (classId==null){
                if (progressHandle!=null && progressHandle.wasCanceled()) {
                    throw new InterruptedException();
                }
                final IWidget parentWidget = owner.getView() == null ? getEnvironment().getMainWindow() : owner.getView();
                final List<InstantiatableClass> classes = getInstantiatableClasses();
                final InstantiatableClass classOfEntity = 
                    InstantiatableClasses.selectClass(getEnvironment(), 
                                                      parentWidget, 
                                                      classes, 
                                                      getId().toString(),
                                                      propertyDef.autoSortInstantiatableClasses());

                if (classOfEntity == null) {
                    return null;//Создание было отменено
                }
                if (progressHandle!=null && progressHandle.wasCanceled()) {
                    throw new InterruptedException();
                }
                classOfEntityId=  classOfEntity.getId();
            }else{
                classOfEntityId = classId;
            }
        }else{
            classOfEntityId=autoSelectCreationClassId(src);
            if (classOfEntityId==null){
                return null;
            }
        }        
        //RADIX-7675 При потере фокуса в поле ввода сеттер свойства может быть еще не вызван,
        //поэтому нужно сделать принудительные вызовы сеттеров            
        owner.finishEdit();        
        return
            EntityModel.openPrepareCreateModel(epd, classOfEntityId, src, propertyDef.getObjectCreationPresentationIds(), context);        
    }
    
    public void delete() throws ServiceClientException, InterruptedException{
        if (preparedForCreate!=null){
            setPreparedForCreate(null);
        }else if (getValueObject() != null){
            if (getInheritableValue()==null){
                final EntityModel entity = openEntityModel();
                if (entity.delete(false)) {
                    final PropertyValue serverValue = new PropertyValue(getDefinition(),                            
                                                                                                    null,//value
                                                                                                    false,//isOwn
                                                                                                    false,//isDefined
                                                                                                    false//isReadonly
                                                                                                    );
                    setServerValue(serverValue);
                    ((EntityModel) getOwner()).afterChangePropertyObject(this);
                }
            }else{
                setValueObject(null);
            }
        }
    }

    @Override
    public EntityModel openEntityModel() throws ServiceClientException, InterruptedException {
        return preparedForCreate==null ? super.openEntityModel() : preparedForCreate;
    }        

    @Override
    public EntityModel openEntityEditor() throws ServiceClientException, InterruptedException {
        final EntityModel entity;
        final IEntityEditorDialog dialog;        
        getProgressHandle().startProgress(getOpenEditorMessage(), true);
        try {
            entity = openEntityModel();
            if (!entity.canOpenEntityView()){
                throw new IllegalUsageError("Failed to open object editor in this environment");
            }            
            ((EntityModel) owner).beforeOpenPropertyObjectEditor(this, entity);
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
        if (preparedForCreate==null){
            setValueTitle(dialog.getEntityModel().getTitle());
            return dialog.getEntityModel();
        }else{
            preparedForCreate = dialog.getEntityModel();
            setValueTitle(getNewObjectTitle(preparedForCreate));
            return preparedForCreate;
        }
    }
    
    @Override
    public IPropEditor createPropertyEditor() {
        return getEnvironment().getApplication().getStandardViewsFactory().newPropObjectEditor(this);
    }
    
    @Override
    public boolean canOpenEntityModelView(){
        if (preparedForCreate==null){
            return super.canOpenEntityModelView()
                   && (!isUserFunctionObject() || getEnvironment().getApplication().isExtendedMetaInformationAccessible());
        }else{
            return (!isUserFunctionObject() || getEnvironment().getApplication().isExtendedMetaInformationAccessible());
        }
    }
        
    @Override
    public final EValType getType() {
        return EValType.OBJECT;
    }
    
    @Override
    public void writeValue2Xml(final org.radixware.schemas.eas.Property xmlProp) {
        if (preparedForCreate==null){            
            super.writeValue2Xml(xmlProp);
        }else{
            xmlProp.setId(getId());
            final PresentableObject value = xmlProp.addNewObj();
            value.addNewPresentation().setId(preparedForCreate.getEditorPresentationDef().getId());
            preparedForCreate.writeToXml(value, true);
            for (Property property: preparedForCreate.getActiveProperties()){
                if (property.getType()==EValType.OBJECT){
                    property.writeValue2Xml(value.getProperties().addNewItem());
                }
            }      
        }
    }
    
    public final void applyChangesInPreparedForCreateModel(){
        if (preparedForCreate!=null){
            for (Property property: preparedForCreate.getActiveProperties()){
                if (!property.isLocal() && property.getType()!=EValType.OBJECT && property.internalValue instanceof NoValue==false){
                    final PropertyValue newServerValue = new PropertyValue(property.internalValue);
                    newServerValue.setReadonly(false);
                    property.setServerValue(property.internalValue);
                }
            }
        }
    }
    
    protected String getNewObjectTitle(final EntityModel newEntityObject){
        final String messageTemplate = getEnvironment().getMessageProvider().translate("ExplorerMessage", "%1$s (new object)");
        return String.format(messageTemplate, newEntityObject.getDefinition().getTitle());
    }
    
    protected List<InstantiatableClass> getInstantiatableClasses() throws ServiceClientException, InterruptedException{
        if (instantiatableClasses==null){
            final IContext.ObjectPropCreating context = new IContext.ObjectPropCreating((EntityModel) owner, getDefinition().getId());
            
            final RadEditorPresentationDef epd = getDefinition().getObjectCreationPresentation();
            instantiatableClasses = InstantiatableClasses.getClasses(getEnvironment(), epd.getTableId(), context.toXml());
        }
        return instantiatableClasses;
    }
    
    protected Id autoSelectCreationClassId(final EntityModel src){
        final RadClassPresentationDef classDef = src.getClassPresentationDef();           
        final Id actualClassId = getDefinition().getReferencedClassId();
        if (classDef.getId().equals(actualClassId)){
            return actualClassId;
        }
        final RadClassPresentationDef actualClassDef =
                getEnvironment().getDefManager().getClassPresentationDef(actualClassId);
        if (actualClassDef.isAncestorOf(classDef)){
            final List<InstantiatableClass> classes;
            try{
                classes = getInstantiatableClasses();
            }catch(ServiceClientException ex){
                getEnvironment().getTracer().error(ex);
                return null;
            }catch(InterruptedException ex){
                return null;
            }
            final InstantiatableClass instantiatableClass = InstantiatableClasses.autoSelectClass(classes);
            if (instantiatableClass==null){
                return null;
            }
            if (classDef.getId().equals(instantiatableClass.getId())){
                return classDef.getId();
            }
            final RadClassPresentationDef instantiatableClassDef;
            try{
               instantiatableClassDef = getEnvironment().getDefManager().getClassPresentationDef(instantiatableClass.getId());
            }catch(DefinitionError error){
                getEnvironment().getTracer().debug(error);
                return null;
            }
            if (instantiatableClassDef.isAncestorOf(classDef)){
                return instantiatableClassDef.getId();
            }
        }
        return null;
    }
}
