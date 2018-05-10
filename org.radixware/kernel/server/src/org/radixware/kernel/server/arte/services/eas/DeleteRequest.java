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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.enums.ESelectionMode;
import org.radixware.kernel.common.enums.EValType;

import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.exceptions.DeleteCascadeConfirmationRequiredException;
import org.radixware.kernel.server.exceptions.DeleteCascadeRestrictedException;
import org.radixware.kernel.server.exceptions.EntityObjectNotExistsError;
import org.radixware.kernel.server.exceptions.FilterParamNotDefinedException;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.server.types.PresentationEntityAdapter;
import org.radixware.kernel.server.types.presctx.PresentationContext;
import org.radixware.schemas.eas.DeleteMess;
import org.radixware.schemas.eas.DeleteRejections;
import org.radixware.schemas.eas.DeleteRq;
import org.radixware.schemas.eas.DeleteRs;
import org.radixware.schemas.eas.ExceptionEnum;
import org.radixware.schemas.easWsdl.DeleteDocument;

final class DeleteRequest extends ObjectRequest {

    DeleteRequest(final ExplorerAccessService presenter) {
        super(presenter);
    }

    @SuppressWarnings("ThrowableResultIgnored")
    final DeleteDocument process(final DeleteMess request) throws ServiceProcessFault, InterruptedException {        
        final DeleteRq rqParams = request.getDeleteRq();
        final RadClassDef classDef = getClassDef(rqParams);
        final boolean bCascade = rqParams.isSetCascade() ? rqParams.getCascade() : false;
        final boolean selected = 
            rqParams.isSetSelectedObjects() && rqParams.getSelectedObjects().getObjectPidList()!=null;
        final boolean single = !selected && rqParams.isSetPID();        
        final PresentationOptions presOptions = getPresentationOptions(rqParams, classDef, !single, true, rqParams.getPresentation());
        final DeleteDocument document = DeleteDocument.Factory.newInstance();
        final DeleteRs response = document.addNewDelete().addNewDeleteRs();
        if (single){
            try{
                deleteSingleObject(classDef, presOptions, rqParams.getPID(), bCascade);
            }catch(Throwable e){
                throw EasFaults.exception2Fault(getArte(), e, "Can't delete object");
            }
        }else if (selected){
            if (presOptions.selectorPresentation == null) {
                throw EasFaults.newParamRequiedFault("Presentation", rqParams.getDomNode().getNodeName());
            }
            int numberOfDeletedObjects = 0;
            final Group group = getGroup(rqParams, classDef, presOptions, false);                            
            final PresentationContext presCtx = getPresentationContext(getArte(), presOptions.context, group.entityGroup);            
            final DeleteRejections rejections = DeleteRejections.Factory.newInstance();
            final Set<String> subDelEntitiesList = new HashSet<>();
            final Set<String> subNullEntitiesList = new HashSet<>();
            final Set<String> confirmationMessagesList = new HashSet<>();
            if (rqParams.getSelectedObjects().getSelectionMode()==ESelectionMode.INCLUSION){
                for (String pidAsStr: rqParams.getSelectedObjects().getObjectPidList()){
                    final Entity entity;
                    final RadEditorPresentationDef curPres;
                    final PresentationEntityAdapter curPresEntAdapter;
                    try {
                        entity = getArte().getEntityObject(new Pid(getArte(), classDef.getEntityId(), pidAsStr));            
                        curPresEntAdapter = getArte().getPresentationAdapter(entity);
                        curPresEntAdapter.setPresentationContext(presCtx);                    
                        curPres = getActualEditorPresentation(curPresEntAdapter, presOptions.selectorPresentation.getEditorPresentations(), true);                        
                    }catch(EntityObjectNotExistsError error){
                        if (Objects.equals(error.getKeyPres(),"PID="+pidAsStr)){                            
                            continue;
                        }else{
                            writeException(pidAsStr, null, error, rejections);
                        }
                        continue;
                    }catch (Throwable exception) {
                        writeException(pidAsStr, null, exception, rejections);
                        continue;
                    }
                    final PresentationOptions objectPresOptions =  
                        new PresentationOptions(this, presOptions.context, presOptions.selectorPresentation, curPres);
                    if (curPres.getTotalRestrictions(entity).getIsDeleteRestricted()
                        || curPresEntAdapter.getAdditionalRestrictions(curPres).getIsDeleteRestricted()){
                        writeDeleteOperationRestricted(entity, objectPresOptions, rejections);
                        continue;
                    }
                    final String title = entity.calcTitle(curPres.getObjectTitleFormat());//calc title object before delete
                    try{
                        deleteSingleObject(entity, objectPresOptions, bCascade);
                        numberOfDeletedObjects++;
                    }catch(DeleteCascadeConfirmationRequiredException exception){
                        if (exception.getSubDelEntities().isEmpty() && exception.getSubNullEntities().isEmpty()){
                            final String confirmationMessage = exception.getMessage();
                            confirmationMessagesList.add(confirmationMessage==null ? "" : confirmationMessage);
                        }else{
                            subDelEntitiesList.addAll(exception.getSubDelEntities());
                            subNullEntitiesList.addAll(exception.getSubNullEntities());
                        }
                    }catch(DeleteCascadeRestrictedException exception){
                        writeDeleteCascadeRestrictedException(entity, objectPresOptions, exception, rejections);
                    }catch(Throwable exception){                        
                        writeException(pidAsStr, title, exception, rejections);
                    }
                }
            }else if (rqParams.getSelectedObjects().getSelectionMode()==ESelectionMode.EXCLUSION){
                final Iterator<Entity> entities = group.entityGroup.iterator();
                while (entities.hasNext()){
                    final Entity curEntity = entities.next();
                    final String pidAsStr = curEntity.getPid().toString();
                    final RadEditorPresentationDef curPres;
                    final PresentationEntityAdapter curPresEntAdapter;
                    try{
                        curPresEntAdapter = getArte().getPresentationAdapter(curEntity);
                        curPresEntAdapter.setPresentationContext(presCtx);                    
                        curPres = getActualEditorPresentation(curPresEntAdapter, presOptions.selectorPresentation.getEditorPresentations(), true);
                    }catch(EntityObjectNotExistsError error){
                        if (Objects.equals(error.getKeyPres(),"PID="+pidAsStr)){
                            continue;
                        }else{
                            writeException(pidAsStr, null, error, rejections);
                        }
                        continue;
                    }catch (Throwable exception) {
                        writeException(pidAsStr, null, exception, rejections);
                        continue;
                    }
                    final PresentationOptions objectPresOptions =  
                        new PresentationOptions(this, presOptions.context, presOptions.selectorPresentation, curPres);                   
                    if (curPres.getTotalRestrictions(curEntity).getIsDeleteRestricted()
                        || curPresEntAdapter.getAdditionalRestrictions(curPres).getIsDeleteRestricted()){
                        writeDeleteOperationRestricted(curEntity, objectPresOptions, rejections);
                        continue;
                    }
                    final String title = curEntity.calcTitle(curPres.getObjectTitleFormat());//calc object title before delete
                    try{
                        deleteSingleObject(curEntity, objectPresOptions, bCascade);
                        numberOfDeletedObjects++;
                    }catch(DeleteCascadeConfirmationRequiredException exception){
                        if (exception.getSubDelEntities().isEmpty() && exception.getSubNullEntities().isEmpty()){
                            final String confirmationMessage = exception.getMessage();
                            confirmationMessagesList.add(confirmationMessage==null ? "" : confirmationMessage);
                        }else{
                            subDelEntitiesList.addAll(exception.getSubDelEntities());
                            subNullEntitiesList.addAll(exception.getSubNullEntities());
                        }                        
                    }catch(DeleteCascadeRestrictedException exception){
                        writeDeleteCascadeRestrictedException(curEntity, objectPresOptions, exception, rejections);
                    }catch(Throwable exception){                        
                        writeException(pidAsStr, title, exception, rejections);
                    }
                }
            }
            if (!subDelEntitiesList.isEmpty() || !subNullEntitiesList.isEmpty() || !confirmationMessagesList.isEmpty()){
                final DeleteCascadeConfirmationRequiredException exception = 
                    new DeleteCascadeConfirmationRequiredException(getArte(), confirmationMessagesList, subDelEntitiesList, subNullEntitiesList);
                throw EasFaults.exception2Fault(getArte(), exception, "Can't delete objects");
            }
            if (rejections.getRejectionList()!=null && !rejections.getRejectionList().isEmpty()){
                response.setRejections(rejections);
            }
            response.setNumberOfDeletedObjects(numberOfDeletedObjects);
        }else{
            final Group group;
            try {
                if (presOptions.selectorPresentation == null) {
                    throw EasFaults.newParamRequiedFault("Presentation", rqParams.getDomNode().getNodeName());
                }
                group = getGroup(rqParams, classDef, presOptions, false);
                if (presOptions.getSelRestrictions(group.entityGroup).getIsDeleteAllRestricted()) {
                    throw EasFaults.newAccessViolationFault(getArte(), Messages.MLS_ID_INSUF_PRIV_TO_DELETE_GROUP, null);
                }
                group.entityGroup.delete(bCascade);
            } catch (FilterParamNotDefinedException e) {
                final String preprocessedExStack = getArte().getTrace().exceptionStackToString(e);
                throw new ServiceProcessClientFault(ExceptionEnum.MISSING_FILTER_PARAM.toString(), e.getMessage(), e, preprocessedExStack);
            } catch (Throwable e) {
                throw EasFaults.exception2Fault(getArte(), e, "Can't delete objects");
            }
            //RADIX-4400
            if (getUsrActionsIsTraced()) {
                //"User '%1' deleted group of '%2'  in presentation '%3' within context '%4' with  filter '%5'"
                getArte().getTrace().put(
                        Messages.MLS_ID_EAS_DELETE_GRP,
                        new ArrStr(
                        getArte().getUserName(),
                        String.valueOf(group.entityGroup.getSelectionClassDef().getTitle()) + " (#" + group.entityGroup.getSelectionClassDef().getId().toString() + ")",
                        String.valueOf(presOptions.selectorPresentation.getName()) + " (#" + presOptions.selectorPresentation.getId().toString() + ")",
                        String.valueOf(presOptions.context),
                        group.getFilterInfo()));            
            }
        }
        return document;
    }        
    
    private void writeDeleteCascadeRestrictedException(final Entity entity, final PresentationOptions presOpts, final DeleteCascadeRestrictedException exception, final DeleteRejections rejections){
        final DeleteRejections.Rejection rejection = rejections.addNewRejection();
        rejection.setObjectPid(entity.getPid().toString());
        rejection.setObjectTitle(entity.calcTitle(presOpts.editorPresentation.getObjectTitleFormat()));
        rejection.setDeleteCascadeRestriction(exception.getChildEntityTitle());        
    }
    
    private void writeDeleteOperationRestricted(final Entity entity, final PresentationOptions presOpts, final DeleteRejections rejections){                
        final DeleteRejections.Rejection rejection = rejections.addNewRejection();        
        rejection.setObjectPid(entity.getPid().toString());
        rejection.setObjectTitle(entity.calcTitle(presOpts.editorPresentation.getObjectTitleFormat()));
        rejection.addNewDeleteOperationRestricted();
    }
    
    private void writeException(final String pidAsStr, final String title, final Throwable exception, final DeleteRejections rejections){
        final DeleteRejections.Rejection rejection = rejections.addNewRejection();
        rejection.setObjectPid(pidAsStr);
        if (title!=null){
            rejection.setObjectTitle(title);
        }
        final org.radixware.schemas.faultdetail.Exception exceptionXml = rejection.addNewException();
        exceptionXml.setMessage(exception.getMessage());
        exceptionXml.setClass1(exception.getClass().getName());
        exceptionXml.setStack(ExceptionTextFormatter.exceptionStackToString(exception));
    }
        
    private void deleteSingleObject(final Entity entity, final PresentationOptions presOptions, final boolean cascade) throws Exception{
        entity.setReadRights(true);
        presOptions.assertEdPresIsAccessible(entity);
        if (presOptions.context instanceof ObjPropContext) {
            final ObjPropContext c = (ObjPropContext) presOptions.context;
            if (c.getContextPropertyDef().getValType() == EValType.OBJECT) {
                c.assertOwnerPropIsEditable();
            }
        }
        if (presOptions.editorPresentation == null) {
            throw EasFaults.newParamRequiedFault("Presentation", "DeleteRq");
        }
        if (presOptions.editorPresentation.getTotalRestrictions(entity).getIsDeleteRestricted()) {
            throw EasFaults.newAccessViolationFault(getArte(), Messages.MLS_ID_INSUF_PRIV_TO_DELETE_OBJECT, null);
        }
        final PresentationEntityAdapter presAdapter = getArte().getPresentationAdapter(entity);
        final PresentationContext presCtx = getPresentationContext(getArte(), presOptions.context, null);
        presAdapter.setPresentationContext(presCtx);
        presOptions.assertEdPresIsAccessible(presAdapter);
        if (presAdapter.getAdditionalRestrictions(presOptions.editorPresentation).getIsDeleteRestricted()){
            throw EasFaults.newAccessViolationFault(getArte(), Messages.MLS_ID_INSUF_PRIV_TO_DELETE_OBJECT, null);
        }
        if ((presOptions.context instanceof ObjPropContext)
                && ((ObjPropContext) presOptions.context).getContextPropertyDef().getValType() == EValType.OBJECT) { //RADIX-2762: if we are deleting user prop val then we must do it via owner.update()
            try {
                final ObjPropContext c = (ObjPropContext) presOptions.context;
                if (!c.getContextPropOwner().isInDatabase(false)) {
                    throw EasFaults.newCantSaveObjPropValUntilOwnerSaved();
                }
                //RADIX-2990: c.getObject() is unregistered, let's get a new handler
                final Entity owner = getArte().getEntityObject(c.getContextPropOwner().getPid());                        
                final PresentationEntityAdapter ownerAdapter = getArte().getPresentationAdapter(owner); //see TWRBS-1516
                final PresentationContext ownerPresCtx = getPresentationContext(getArte(), c.getContextObjContext(), null);
                ownerAdapter.setPresentationContext(ownerPresCtx);  
                c.assertOwnerPropIsEditable(ownerAdapter);
                ownerAdapter.setPropHasOwnVal(c.getPropertyId(), false);
                ownerAdapter.update();
            } catch (RuntimeException e) {
                throw EasFaults.exception2Fault(getArte(), e, "Can't clear object value of UserProperty");
            }
        } else {
            presAdapter.delete(cascade);
        }
        if (getUsrActionsIsTraced()) {
            //"User '%1'deleted '%2' with PID = '%3'", EAS, Debug
            getArte().getTrace().put(
                    Messages.MLS_ID_EAS_DELETE,
                    new ArrStr(
                    getArte().getUserName(),
                    String.valueOf(entity.getRadMeta().getTitle()) + " (#" + entity.getRadMeta().getId().toString() + ")",
                    entity.getPid().toString()));
        }        
    }
    
    private void deleteSingleObject(final RadClassDef classDef, 
                              final PresentationOptions presOptions, 
                              final String pidAsStr,
                              final boolean cascade) throws Exception{
        
        final Entity entity;
        try {
            entity = getArte().getEntityObject(new Pid(getArte(), classDef.getEntityId(), pidAsStr));            
        } catch (Throwable e) {
            throw EasFaults.exception2Fault(getArte(), e, "Can't get a class instance");
        }
        deleteSingleObject(entity, presOptions, cascade);
    }

    @Override
    void prepare(final XmlObject rqXml) throws ServiceProcessServerFault, ServiceProcessClientFault {
        super.prepare(rqXml);
        prepare(((DeleteMess) rqXml).getDeleteRq());
    }

    @Override
    XmlObject process(final XmlObject rq) throws ServiceProcessFault, InterruptedException {
        DeleteDocument doc = null;
        try{
            doc = process((DeleteMess) rq);
        }finally{
            postProcess(rq, doc==null ? null : doc.getDelete().getDeleteRs());
        }
        return doc;
    }
}
