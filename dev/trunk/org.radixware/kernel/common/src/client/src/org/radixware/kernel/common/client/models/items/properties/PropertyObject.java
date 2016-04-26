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

import java.util.List;
import org.radixware.kernel.common.client.enums.EEntityCreationResult;
import org.radixware.kernel.common.client.errors.ModelCreationError;
import org.radixware.kernel.common.client.exceptions.ModelException;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadEditorPresentationDef;
import org.radixware.kernel.common.client.meta.RadParentRefPropertyDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.types.InstantiatableClass;
import org.radixware.kernel.common.client.types.InstantiatableClasses;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.client.views.IEntityEditorDialog;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.client.views.IPropEditor;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;

public class PropertyObject extends PropertyReference {

    private IProgressHandle phHandle = null;
    private boolean canDelete = true, canCreate=true;
    private Boolean isUserFunctionObject;

    public PropertyObject(final Model owner, final RadParentRefPropertyDef def) {
        super(owner, def);
    }

    @Override
    public final RadParentRefPropertyDef getDefinition() {
        return (RadParentRefPropertyDef)super.getDefinition();
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
        final Reference value = getVal();
        return !isReadonly() 
               && value!=null 
               && !value.isBroken() 
               && !value.isDeleteRestricted()
               && ((EntityModel)getOwner()).isExists() 
               && canDelete;
    }        
    
    public final boolean canCreate(){
        if (isReadonly() || owner instanceof EntityModel==false || !((EntityModel)owner).isExists() ||
           !getDefinition().isObjectCreationPresentationDefined()){
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

    @Override
    public boolean canModifyEntityObject() {
        if (!isReadonly() && canOpenEntityModel()){            
            return !getVal().isModificationRestricted();
        }else{
            return false;
        }
    }
    
    private boolean isUserFunctionObject(){
        if (isUserFunctionObject==null){
            final RadParentRefPropertyDef propertyDef = getDefinition();
            final Id referencedClassId = propertyDef.getReferencedClassId();
            final RadClassPresentationDef referencedClassDef = 
                getEnvironment().getDefManager().getClassPresentationDef(referencedClassId);        
            isUserFunctionObject = Boolean.valueOf(referencedClassDef.isUserFunction());
        }
        return isUserFunctionObject.booleanValue();
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
        getProgressHandle().startProgress(getOpenEditorMessage(), true);
        EntityModel entity;
        final IEntityEditorDialog dialog;
        try {
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
                if (getProgressHandle().wasCanceled()) {
                    throw new InterruptedException();
                }
                final IWidget parentWidget = owner.getView() == null ? getEnvironment().getMainWindow() : owner.getView();
                final List<InstantiatableClass> classes = InstantiatableClasses.getClasses(getEnvironment(), epd.getTableId(), context.toXml());
                final InstantiatableClass classOfEntity = 
                    InstantiatableClasses.selectClass(getEnvironment(), 
                                                      parentWidget, 
                                                      classes, 
                                                      getId().toString(),
                                                      propertyDef.autoSortInstantiatableClasses());

                if (classOfEntity == null) {
                    return null;//Создание было отменено
                }
                if (getProgressHandle().wasCanceled()) {
                    throw new InterruptedException();
                }
                classOfEntityId=  classOfEntity.getId();
            }else{
                classOfEntityId=src.getClassId();
            }
            //RADIX-7675 При потере фокуса в поле ввода сеттер свойства может быть еще не вызван,
            //поэтому нужно сделать принудительные вызовы сеттеров            
            owner.finishEdit();
            entity = EntityModel.openPrepareCreateModel(epd, classOfEntityId, src, propertyDef.getObjectCreationPresentationIds(), context);
            if (entity == null) {
                return null;
            }
            if (getProgressHandle().wasCanceled()) {
                throw new InterruptedException();
            }
            if (entity.getCustomViewId() == null
                && entity.getEditorPresentationDef().getEditorPages().getTopLevelPages().isEmpty()) {
                //RADIX-2567
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
            else {
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
            getProgressHandle().finishProgress();
        }
        if (dialog!=null){
            dialog.execDialog();
            entity = dialog.getEntityModel();                    
        }
        if (entity.isExists()) {
            //after create property already have this value. Update operation is not need.
            final PropertyValue newServerValue = new PropertyValue(serverValue);
            newServerValue.setValue(new Reference(entity.getPid(), entity.getTitle()));
            setServerValue(newServerValue);
            ((EntityModel) owner).afterChangePropertyObject(this);
            return entity;
        } else {
            return null;
        }
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
        setValueTitle(dialog.getEntityModel().getTitle());
        return entity;
    }

    @Override
    public IPropEditor createPropertyEditor() {
        return getEnvironment().getApplication().getStandardViewsFactory().newPropObjectEditor(this);
    }
    
    @Override
    public boolean canOpenEntityModelView(){
        return super.canOpenEntityModelView()
               && (!isUserFunctionObject() || getEnvironment().getApplication().isExtendedMetaInformationAccessible());
    }

    @Override
    public final EValType getType() {
        return EValType.OBJECT;
    }
}
