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

package org.radixware.kernel.server.arte.services.eas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.enums.EAutoUpdateReason;
import org.radixware.kernel.common.enums.EEntityInitializationPhase;

import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.enums.EEntityLockMode;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.meta.clazzes.IRadRefPropertyDef;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.clazzes.RadParentPropDef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.server.types.PresentationEntityAdapter;
import org.radixware.kernel.server.types.PropValHandler;
import org.radixware.kernel.server.types.PropValHandlersByIdMap;
import org.radixware.kernel.server.types.presctx.PresentationContext;
import org.radixware.schemas.eas.CreateMess;
import org.radixware.schemas.eas.CreateRq;
import org.radixware.schemas.eas.CreateRs;
import org.radixware.schemas.eas.PropertyList;
import org.radixware.schemas.easWsdl.CreateDocument;

final class CreateRequest extends ObjectRequest {
    
    private final static class CreationResult{
        
        private final Entity entity;
        private final RadEditorPresentationDef edPres;
        
        public CreationResult(final Entity entity, final RadEditorPresentationDef edPres){
            this.entity = entity;
            this.edPres = edPres;
        }
        
        public Entity getNewObject(){
            return entity;
        }
        
        public RadEditorPresentationDef getEditingPresentation(){
            return edPres;
        }
        
    }
    
    private final static class ParentPropsFilter implements PropValLoadFilter{
        
        private final List<Entity> createdObjects;
        private final Entity entity;
        
        public ParentPropsFilter(final Entity entity, final List<Entity> createdObjects){
            this.createdObjects = createdObjects;
            this.entity = entity;
        }

        @Override
        public boolean skip(final PropVal val) {
            if (val.xmlProp.isSetObj()){
                return true;
            }else if (val.prop instanceof RadParentPropDef && !createdObjects.isEmpty()) {
                final List<IRadRefPropertyDef> refs = ((RadParentPropDef) val.prop).getRefProps();
                Entity currentEntity = entity;
                Object propRefVal;
                for (IRadRefPropertyDef ref: refs){
                    propRefVal =  currentEntity.getProp(ref.getId());
                    if (propRefVal instanceof Entity){
                        if (createdObjects.contains(propRefVal)){
                            return true;
                        }else{
                            currentEntity = (Entity)propRefVal;
                        }
                    }else{
                        return false;
                    }
                }
            }
            return false;
        }
        
    }

    CreateRequest(final ExplorerAccessService presenter) {
        super(presenter);
    }

    final CreateDocument process(final CreateMess request) throws ServiceProcessFault, InterruptedException {
        //trace("CreateRequest.process() started");
        //IN
        final CreateRq rqParams = request.getCreateRq();
        final RadClassDef classDef = getClassDef(rqParams);
        final org.radixware.schemas.eas.Object dataXml = rqParams.getData();
        if (dataXml == null) {
            throw EasFaults.newParamRequiedFault("Data", rqParams.getDomNode().getNodeName());
        }
        final PropertyList propsXml = dataXml.getProperties();
        if (propsXml == null) {
            throw EasFaults.newParamRequiedFault("Properties", dataXml.getDomNode().getNodeName());
        }        
        final PresentationOptions presOptions = getPresentationOptions(rqParams, classDef, false, false, rqParams.getPresentation());
        if (presOptions.context instanceof ObjPropContext) {
            final ObjPropContext c = (ObjPropContext) presOptions.context;
            if (c.getContextPropertyDef().getValType() == EValType.OBJECT) {
                c.assertOwnerPropIsEditable();
            }
        }
        final Entity src;
        if (dataXml.isSetSrcPID()) {
            try {
                src = getArte().getEntityObject(new Pid(getArte(), classDef.getEntityId(), dataXml.getSrcPID()));
            } catch (Throwable e) {
                throw EasFaults.exception2Fault(getArte(), e, "Can't get the source object class instance");
            }
        }else{
            src = null;
        }

        final Entity entity;
        try {
            entity = (Entity) getArte().newObject(classDef.getId());
        } catch (RuntimeException e) {
            throw EasFaults.exception2Fault(getArte(), e, "Can't get a class instance");
        }
        final List<Entity> createdObjects = new LinkedList<>();
        final CreationResult result = 
            createEntityObject(entity, src, presOptions, propsXml, rqParams.getDomNode().getNodeName(),createdObjects);
        final CreateDocument res = CreateDocument.Factory.newInstance();
        final CreateRs rsStruct = res.addNewCreate().addNewCreateRs();
        if (result==null){
            return res;
        }else{
            rsStruct.setPID(result.getNewObject().getPid().toString());
            rsStruct.setTitle(result.getNewObject().calcTitle(result.getEditingPresentation().getObjectTitleFormat()));
            rsStruct.addNewPresentation().setId(result.getEditingPresentation().getId());            
        }
        return res;
    }
    
    private void createUserPropObject(final Entity ownerObject,
                                                        final Id ownerObjectPresId,
                                                        final Context ownerContext,
                                                        final Id propId, 
                                                        final org.radixware.schemas.eas.PresentableObject valueXml,
                                                        final List<Entity> createdObjects) throws ServiceProcessFault, InterruptedException{       
        final Id classId = valueXml.getClassId();
        final RadClassDef classDef;
        try {
            classDef = getArte().getDefManager().getClassDef(classId);
        } catch (DefinitionNotFoundError e) {
            throw EasFaults.newDefWithIdNotFoundFault("Class", "ClassId", classId);
        }
        final ObjPropContext context  = new ObjPropContext(this, 
                                                                                         propId, 
                                                                                         ownerObjectPresId, 
                                                                                         ownerObject,
                                                                                         ownerContext);
        final PresentationOptions presOptions = 
            getPresentationOptions(context, classDef, false, false, valueXml.getPresentation(), "");
        final Entity src;
        if (valueXml.isSetSrcPID()) {
            try {
                src = getArte().getEntityObject(new Pid(getArte(), classDef.getEntityId(), valueXml.getSrcPID()));
            } catch (Throwable e) {
                throw EasFaults.exception2Fault(getArte(), e, "Can't get the source object class instance");
            }
        }else{
            src = null;
        }

        final Entity entity;
        try {
            entity = (Entity) getArte().newObject(classDef.getId());
        } catch (RuntimeException e) {
            throw EasFaults.exception2Fault(getArte(), e, "Can't get a class instance");
        }                
        
        final PropertyList propsXml = valueXml.getProperties();
        if (propsXml == null) {
            throw EasFaults.newParamRequiedFault("Properties", valueXml.getDomNode().getNodeName());
        }
        createEntityObject(entity, src, presOptions, propsXml, valueXml.getDomNode().getNodeName(), createdObjects);
    }
    
    private CreationResult createEntityObject(final Entity entity, 
                                                   final Entity src, 
                                                   final PresentationOptions presOptions,
                                                   final PropertyList propsXml,
                                                   final String xmlNodeName,
                                                   final List<Entity> createdObjects) throws ServiceProcessFault, InterruptedException{
        //there is no use to check EdPres restriction before inserting object in DB (i.e. until Access Control Partition are not defined)
        //if (context.editorPresentation.getDefRestrictions(getArte()).getIsCreateRestricted())
        //	throw EasFaults.newAccessViolationFault("create object");        
        presOptions.assertEdPresIsAccessible(entity);

        final List<RadPropDef> updatedProps = new ArrayList<>();
        final RadEditorPresentationDef pres = presOptions.editorPresentation;
        if (pres == null) {
            throw EasFaults.newParamRequiedFault("Presentation", xmlNodeName);
        }        
        final PresentationEntityAdapter presEntAdapter = getArte().getPresentationAdapter(entity);
        final PresentationContext presContext = getPresentationContext(getArte(), presOptions.context, null);
        presEntAdapter.setPresentationContext(presContext);        
        presOptions.assertEdPresIsAccessible(presEntAdapter);
        final List<PropertyList.Item> propItemXmls = propsXml.getItemList();
        loadPropsFromXml(propItemXmls, presEntAdapter, pres, updatedProps, null, new ParentPropsFilter(entity,createdObjects), EReadonlyPropModificationPolicy.ALLOWED /*we can not say if property modified in editor or by PrepareCreate :(, it should be allowed to modify readonly props in prepare create*/);
        final Map<Id,org.radixware.schemas.eas.PresentableObject> userPropVals = new HashMap<>();
        for (PropertyList.Item xmlProp: propItemXmls){
            if (xmlProp.isSetObj() 
                && (!xmlProp.getObj().isSetPID() || xmlProp.getObj().getPID()==null)//RADIX-12963 ignoring existing object. It must be updated in separate update request
                ){
                userPropVals.put(xmlProp.getId(), xmlProp.getObj());
            }
        }        
        final PropValHandlersByIdMap initPropValsById = new PropValHandlersByIdMap();
        for (RadPropDef updatedProp : updatedProps) {
            final Id id = updatedProp.getId();
            initPropValsById.put(updatedProp.getId(), new PropValHandler(entity.getPropHasOwnVal(id), entity.getProp(id)));
        }
        initNewObject(entity, 
                              presOptions.context, 
                              pres, 
                              initPropValsById, 
                              src, 
                              EEntityInitializationPhase.INTERACTIVE_CREATION,
                              userPropVals.keySet());
        final Entity result;
        try {
            if ((presOptions.context instanceof ObjPropContext)
                    && ((ObjPropContext) presOptions.context).getContextPropertyDef().getValType() == EValType.OBJECT) {
                try {
                    final ObjPropContext c = (ObjPropContext) presOptions.context;
                    if (!c.getContextPropOwner().isInDatabase(false)) {
                        throw EasFaults.newCantSaveObjPropValUntilOwnerSaved();
                    }
                    //RADIX-2990: c.getObject() is unregistered, let's get a new handler
                    final Entity owner = getArte().getEntityObject(c.getContextPropOwner().getPid());
                    final PresentationEntityAdapter ownerAdapter = getArte().getPresentationAdapter(owner); //see TWRBS-1516
                    final PresentationContext ownerContext = getPresentationContext(getArte(), c.getContextObjContext(), null);
                    ownerAdapter.setPresentationContext(ownerContext);
                    c.assertOwnerPropIsEditable(ownerAdapter);
                    presEntAdapter.createAsUserPropertyValue(ownerAdapter, c.getPropertyId());
                    result = (Entity) ownerAdapter.getProp(c.getPropertyId()); //adapter could create another instance
                } catch (Exception e) {
                    throw EasFaults.exception2Fault(getArte(), e, "Can't register new object as UserProperty value");
                }
            } else {
                result = presEntAdapter.create(src);
            }
            if (result==null || result.isDiscarded()){
                return null;
            }else{
                result.setReadRights(true);
                getArte().updateDatabase(EAutoUpdateReason.PREPARE_COMMIT);
                result.reread(EEntityLockMode.NONE, null);
            }
        } catch (RuntimeException e) {
            throw EasFaults.exception2Fault(getArte(), e, "Error on create");
        }        
        
        final RadEditorPresentationDef editingPres;//editor presentation of existing object
        if (presOptions.selectorPresentation == null) {
            editingPres = getActualEditorPresentation(presEntAdapter, Collections.singletonList(pres), true);
        } else {
            editingPres = getActualEditorPresentation(presEntAdapter, presOptions.selectorPresentation.getEditorPresentations(), true);
        }                        
        
        final PresentationEntityAdapter resultPresEntAdapter = getArte().getPresentationAdapter(result);
        resultPresEntAdapter.setPresentationContext(presContext);        
        if (editingPres.getTotalRestrictions(result).getIsCreateRestricted() ||
            resultPresEntAdapter.getAdditionalRestrictions(editingPres).getIsCreateRestricted()) {
            throw EasFaults.newAccessViolationFault(getArte(), Messages.MLS_ID_INSUF_PRIV_TO_CREATE_OBJECT, null);
        }                        

        //RADIX-4400 "User '%1' created '%2' with PID = '%3'", EAS, Debug
        if (getUsrActionsIsTraced()) {
            getArte().getTrace().put(
                    Messages.MLS_ID_EAS_CREATE,
                    new ArrStr(
                    getArte().getUserName(),
                    String.valueOf(result.getRadMeta().getTitle()) + " (#" + result.getRadMeta().getId().toString() + ")",
                    result.getPid().toString()));
        }                
        createdObjects.add(result);
        //now we can create user property objects if any
        for (Map.Entry<Id,org.radixware.schemas.eas.PresentableObject> entry: userPropVals.entrySet()){            
            if (result.getProp(entry.getKey()) instanceof Entity){
                if (entry.getValue().getProperties()!=null){
                    updateUserPropObject((Entity)result.getProp(entry.getKey()), 
                                                      entry.getValue(),
                                                      resultPresEntAdapter,
                                                      editingPres.getId(),
                                                      presOptions.context,
                                                      entry.getKey(),
                                                      createdObjects
                                                      );
                }
            }else{
                createUserPropObject(result, editingPres.getId(), presOptions.context, entry.getKey(), entry.getValue(), createdObjects);
            }
        }
        
        return new CreationResult(result, editingPres);
    }
    
    private void updateUserPropObject(final Entity userPropObject,                                                          
                                                         final org.radixware.schemas.eas.PresentableObject xmlObject,
                                                         final PresentationEntityAdapter ownerAdapter,
                                                         final Id ownerObjectPresId,
                                                         final Context ownerContext,
                                                         final Id propId,
                                                         final List<Entity> createdObjects
                                                         ) throws ServiceProcessFault, InterruptedException{
        final Id classId = xmlObject.getClassId();
        final RadClassDef classDef;
        try {
            classDef = getArte().getDefManager().getClassDef(classId);
        } catch (DefinitionNotFoundError e) {
            throw EasFaults.newDefWithIdNotFoundFault("Class", "ClassId", classId);
        }
        final Entity ownerObject = ownerAdapter.getRawEntity();
        final ObjPropContext context  = new ObjPropContext(this, 
                                                                                         propId, 
                                                                                         ownerObjectPresId, 
                                                                                         ownerObject,
                                                                                         ownerContext);
        final PresentationOptions presOptions = 
            getPresentationOptions(context, classDef, false, false, xmlObject.getPresentation(), "");        
        RadEditorPresentationDef pres = presOptions.editorPresentation;
        if (pres == null) {
            throw EasFaults.newParamRequiedFault("Presentation", xmlObject.getDomNode().getNodeName());
        }
        final PresentationEntityAdapter presEntAdapter = getArte().getPresentationAdapter(userPropObject);
        final PresentationContext presContext = getPresentationContext(getArte(), context, null);
        presEntAdapter.setPresentationContext(presContext);
        presOptions.assertEdPresIsAccessible(presEntAdapter);
        final PropertyList propsXml = xmlObject.getProperties();        
        final List<PropertyList.Item> propItemXmls = propsXml.getItemList();        
        loadPropsFromXml(propItemXmls, presEntAdapter, pres, null, null, new ParentPropsFilter(userPropObject, createdObjects), EReadonlyPropModificationPolicy.ALLOWED /*we can not say if property modified in editor or by PrepareCreate :(, it should be allowed to modify readonly props in prepare create*/);
        final Map<Id,org.radixware.schemas.eas.PresentableObject> userPropVals = new HashMap<>();
        for (PropertyList.Item xmlProp: propItemXmls){
            if (xmlProp.isSetObj() 
                && (!xmlProp.getObj().isSetPID() || xmlProp.getObj().getPID()==null)//RADIX-12963 ignoring existing object. It must be updated in separate update request
                ){
                userPropVals.put(xmlProp.getId(), xmlProp.getObj());
            }
        }
        
        if (userPropObject.isModified()){
            if (ownerAdapter.beforeUpdatePropObject(propId, userPropObject)) {
                presEntAdapter.update();
                ownerAdapter.afterUpdatePropObject(propId, userPropObject);
            }
            
            pres = getActualEditorPresentation(presEntAdapter, Collections.singletonList(pres), true);
            if (pres.getTotalRestrictions(userPropObject).getIsCreateRestricted() ||
                presEntAdapter.getAdditionalRestrictions(pres).getIsCreateRestricted()) {
                throw EasFaults.newAccessViolationFault(getArte(), Messages.MLS_ID_INSUF_PRIV_TO_CREATE_OBJECT, null);
            }            
        }
        
        createdObjects.add(userPropObject);
        
        for (Map.Entry<Id,org.radixware.schemas.eas.PresentableObject> entry: userPropVals.entrySet()){            
            if (userPropObject.getProp(entry.getKey()) instanceof Entity){
                if (entry.getValue().getProperties()!=null){
                    updateUserPropObject((Entity)userPropObject.getProp(entry.getKey()), 
                                                      entry.getValue(),
                                                      presEntAdapter,
                                                      pres.getId(),
                                                      presOptions.context,
                                                      entry.getKey(),
                                                      createdObjects
                                                      );
                }
            }else{
                createUserPropObject(userPropObject, pres.getId(), presOptions.context, entry.getKey(), entry.getValue(),createdObjects);
            }
        }        
    }

    @Override
    void prepare(final XmlObject rqXml) throws ServiceProcessServerFault, ServiceProcessClientFault {        
        super.prepare(rqXml);
        prepare(((CreateMess) rqXml).getCreateRq());
    }

    @Override
    XmlObject process(final XmlObject rq) throws ServiceProcessFault, InterruptedException {
        CreateDocument doc = null;
        try{
            doc = process((CreateMess) rq);
        }finally{        
            postProcess(rq, doc==null ? null : doc.getCreate().getCreateRs());
        }
        return doc;
    }
}