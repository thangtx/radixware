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

import java.util.HashMap;
import org.radixware.kernel.server.arte.services.eas.SessionRequest.PresentationOptions;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.types.PresentationEntityAdapter;
import org.radixware.schemas.eas.PropertyList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.xmlbeans.XmlObject;

import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.ECommandNature;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.enums.ECommandScope;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EEntityInitializationPhase;
import org.radixware.kernel.common.exceptions.AppException;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.server.exceptions.EntityObjectNotExistsError;
import org.radixware.kernel.server.exceptions.WrongPidFormatError;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.presentations.RadClassPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadCommandDef;
import org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.FormHandler;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.server.types.PropValHandler;
import org.radixware.kernel.server.types.PropValHandlersByIdMap;
import org.radixware.kernel.server.types.Restrictions;
import org.radixware.kernel.server.types.presctx.PresentationContext;
import org.radixware.schemas.eas.CommandMess;
import org.radixware.schemas.eas.CommandRq;
import org.radixware.schemas.eas.CommandRs;
import org.radixware.schemas.eas.Definition;
import org.radixware.schemas.eas.ExceptionEnum;
import org.radixware.schemas.easWsdl.CommandDocument;


final class CommandRequest extends ObjectRequest {

    public CommandRequest(final ExplorerAccessService presenter) {
        super(presenter);
    }

    final CommandDocument process(final CommandMess request) throws ServiceProcessFault, InterruptedException {
        //trace("CommandRequest.process() started");
        //IN
        final CommandRq rqParams = request.getCommandRq();
        final RadClassDef classDef = getClassDef(rqParams);
        assertNotUserFuncOrUserCanDevUserFunc(classDef);
        final Definition cmdXml = rqParams.getCommand();
        RadCommandDef cmd = null;
        try {
            cmd = classDef.getPresentation().getCommandById(cmdXml.getId());
        } catch (DefinitionNotFoundError e) {
            try { //may be it is a group command
                final RadClassPresentationDef clsPres = getArte().getGroupHander(classDef.getEntityId()).getRadMeta().getPresentation();
                if (clsPres != null) {
                    cmd = clsPres.getCommandById(cmdXml.getId());
                }
            } catch (DefinitionNotFoundError ex) {
                //it isn't group command 
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            if (cmd == null) {
                throw EasFaults.newDefWithIdNotFoundFault("Command", rqParams.getDomNode().getNodeName(), cmdXml.getId());
            }
        }
        final PresentationOptions presOptions = getPresentationOptions(rqParams, classDef, cmd.scope != ECommandScope.OBJECT && cmd.scope != ECommandScope.PROPERTY, true, rqParams.getPresentation());

        Id cmdPropId = null;

        if (cmd.scope == ECommandScope.PROPERTY) {
            if (!rqParams.isSetProperty()) {
                throw new ServiceProcessClientFault(ExceptionEnum.APPLICATION_ERROR.toString(), "Property is required for command \"" + cmd.getName() + "\" (#" + cmd.getId() + ")", null, null);
            }
            cmdPropId = rqParams.getProperty().getId();
            if (!cmd.isApplicableForProperty(cmdPropId)) {
                throw new ServiceProcessClientFault(ExceptionEnum.APPLICATION_ERROR.toString(), "Command \"" + cmd.getName() + "\" (#" + cmd.getId() + ") is not applicable for property #" + cmdPropId, null, null);
            }
        }

        Entity entity = null;
        Entity src = null;
        Group group = null;
        FormHandler form = null;
        boolean isFormCommand = false;
        try {

            if (cmd.scope == ECommandScope.OBJECT || cmd.scope == ECommandScope.PROPERTY) {
                if (classDef.getType() == EClassType.FORM_HANDLER) {
                    isFormCommand = true;
                    if (rqParams.isSetForm()) {
                        form = getFormHandler(rqParams.getForm(), true);
                    } else {
                        form = (FormHandler) getArte().getDefManager().newClassInstance(classDef.getId(), new Object[]{null});
                    }
                    if (cmd.scope == ECommandScope.PROPERTY) { //check property is defined in class
                        form.getRadMeta().getPropById(rqParams.getProperty().getId());
                    }
                } else {
                    if (rqParams.isSetPID()) {
                        entity = getArte().getEntityObject(new Pid(getArte(), classDef.getEntityId(), rqParams.getPID()));
                        entity.setReadRights(true);
                    } else {
                        if (rqParams.isSetCurrentData() && rqParams.getCurrentData().isSetSrcPID()) {
                            try {
                                src = getArte().getEntityObject(new Pid(getArte(), classDef.getEntityId(), rqParams.getCurrentData().getSrcPID()));
                            } catch (Throwable e) {
                                throw EasFaults.exception2Fault(getArte(), e, "Can't get the source object class instance");
                            }
                        }
                        entity = (Entity) getArte().newObject(classDef.getId());
                    }
                    if (cmd.scope == ECommandScope.PROPERTY) { //check property is defined in class
                        entity.getRadMeta().getPropById(rqParams.getProperty().getId());
                    }
                }
            }

        } catch (Throwable e) {
            throw EasFaults.exception2Fault(getArte(), e, "Can't get a class instance");
        }
        if (cmd.scope == ECommandScope.OBJECT || cmd.scope == ECommandScope.PROPERTY) {
            if (!isFormCommand) {
                presOptions.assertEdPresIsAccessibile(entity);
            }
        }


        //final TDbuPresentation pres = context.editorPresentation != null ? (TDbuPresentation)context.editorPresentation : context.selectorPresentation;
        PropValHandlersByIdMap newPropValsById = null;
        PropValHandlersByIdMap newPropValsByIdBak = null;
        Map<Id, Object> formPropValsById = null;
        if (cmd.scope == ECommandScope.OBJECT || cmd.scope == ECommandScope.PROPERTY) {
            if (!isFormCommand) {
                newPropValsById = new PropValHandlersByIdMap();
                writeCurData2Map(entity.getRadMeta(), presOptions.editorPresentation, rqParams.getCurrentData(), newPropValsById, NULL_PROP_LOAD_FILTER);
                newPropValsByIdBak = new PropValHandlersByIdMap();
                newPropValsByIdBak.putAll(newPropValsById);
                if (!entity.isInDatabase(false)) {
                    initNewObject(entity, presOptions.context, presOptions.editorPresentation, null, src, EEntityInitializationPhase.TEMPLATE_EDITING);
                    getArte().switchToReadonlyTransaction(); // to prevent commit of new object before it saved from edtitor
                    //entity.setReadonly(true);
                    //getArte().unregisterNewEntityObject(entity);//сносим регистрацию, что бы не создался по окончанию транзакции
                }
            } else {
                formPropValsById = new HashMap<Id, Object>(form.getRadMeta().getProps().size() * 2 + 1);
                for (RadPropDef prop : form.getRadMeta().getProps()) {
                    if (form.getRadMeta().getPresentation().getPropPresById(prop.getId()) != null) {
                        formPropValsById.put(prop.getId(), form.getProp(prop.getId()));
                    }
                }
            }
        } else {
            group = getGroup(rqParams, classDef, presOptions, false /*cmd.scope == ECommandScope.CONTEXT*/); //FIXME remove bIngnoreFilter parameter
        }

        final CommandDocument res = CommandDocument.Factory.newInstance();
        final CommandRs rsStruct = res.addNewCommand().addNewCommandRs();
        final XmlObject output = cmd.nature == ECommandNature.XML_IN_OUT ? rsStruct.addNewOutput() : null;
        final FormHandler fh;
        if (cmd.nature == ECommandNature.FORM_IN_OUT || (!isFormCommand && rqParams.isSetForm())) {
            if (!rqParams.isSetForm()) {
                throw EasFaults.newParamRequiedFault("Form", rqParams.getDomNode().getNodeName());
            }
            fh = getFormHandler(rqParams.getForm(), true);
        } else {
            fh = null;
        }
        try {
            final FormHandler.NextDialogsRequest nxDlg;
            if (cmd.scope == ECommandScope.OBJECT || cmd.scope == ECommandScope.PROPERTY) {
                if (!isFormCommand) {
                    final Id classId;
                    if (entity.getRadMeta() != null) {
                        classId = entity.getRadMeta().getId();
                    } else {
                        classId = RadClassDef.getEntityClassIdByTableId(entity.getDdsMeta().getId());
                    }                    
                    final Restrictions r = presOptions.editorPresentation.getTotalRestrictions(entity);                    
                    final PresentationEntityAdapter<? extends Entity> presentationAdapter = getArte().getPresentationAdapter(entity);
                    final PresentationContext presContext = getPresentationContext(getArte(), presOptions.context, null);
                    presentationAdapter.setPresentationContext(presContext);
                    if (r.getIsCommandRestricted(cmd.getId()) || presentationAdapter.isCommandDisabled(cmd.getId())) {                        
                        throw EasFaults.newDefinitionAccessViolationFault(getArte(), 
                                                                          Messages.MLS_ID_INSUF_PRIV_TO_EXECUTE_COMMAND, 
                                                                          "\"" + cmd.getName() + "\" (#" + cmd.getId() + ")",
                                                                          EDefType.SCOPE_COMMAND,
                                                                          new Id[]{classId,cmd.getId()});
                    }
                    if (!cmd.isApplicableForClass(classId)) {
                        throw new ServiceProcessClientFault(ExceptionEnum.APPLICATION_ERROR.toString(), "Command \"" + cmd.getName() + "\" (#" + cmd.getId() + ") is not applicable for class #" + classId, null, null);
                    }
                    if (fh != null) {
                        nxDlg = fh.onSubmit(new FormHandler.Context(entity, cmd.getId(), cmdPropId, newPropValsById));
                    } else {
                        nxDlg = presentationAdapter.execCommand(cmd.getId(), cmdPropId != null ? cmdPropId : null, request.getCommandRq().getInput(), newPropValsById, output);
                    }
                    if (!newPropValsById.isEmpty()) {
                        final PropertyList curPropsXml = rsStruct.addNewCurrentData().addNewProperties();
                        for (Map.Entry<Id, PropValHandler> propValEntry : newPropValsById.entrySet()) {
                            final Id propId = propValEntry.getKey();
                            final PropValHandler propVal = propValEntry.getValue();
                            final PropValHandler propValBak = newPropValsByIdBak.get(propId);
                            if (!newPropValsByIdBak.containsKey(propId) || // property value has been added to map by command handler
                                    (propVal == null ? propValBak != null : !propVal.equals(propValBak)) // property value has been modified in map by command handler
                                    ) {
                                writeCurPropValTo(curPropsXml, entity, presOptions.editorPresentation, propId, propVal);
                            }
                        }
                    }
                } else {
                    Id classId = form.getRadMeta().getId();
                    if (!cmd.isApplicableForClass(classId)) {
                        throw new ServiceProcessClientFault(ExceptionEnum.APPLICATION_ERROR.toString(), "Command \"" + cmd.getName() + "\" (#" + cmd.getId() + ") is not applicable for class #" + classId, null, null);
                    }

                    nxDlg = form.execCommand(cmd.getId(), cmdPropId, request.getCommandRq().getInput(), output);

                    if (formPropValsById != null && !formPropValsById.isEmpty()) {
                        PropertyList currPropsXml = null;
                        for (Map.Entry<Id, Object> e : formPropValsById.entrySet()) {
                            Object currVal = form.getProp(e.getKey());
                            if (!Utils.equals(currVal, e.getValue())) {
                                if (currPropsXml == null) {
                                    currPropsXml = rsStruct.addNewCurrentData().addNewProperties();
                                }
                                writeCurPropValTo(currPropsXml, form, e.getKey(), currVal);
                            }
                        }
                    }
                }
            } else {
                final Restrictions r = presOptions.getSelRestrictions(group.entityGroup);
                if (r.getIsCommandRestricted(cmd.getId()) || group.entityGroup.isCommandDisabled(cmd.getId())) {
                    throw EasFaults.newDefinitionAccessViolationFault(getArte(), 
                                                                      Messages.MLS_ID_INSUF_PRIV_TO_EXECUTE_COMMAND, 
                                                                      "\"" + cmd.getName() + "\" (#" + cmd.getId() + ")",
                                                                      EDefType.SCOPE_COMMAND,
                                                                      new Id[]{group.entityGroup.getSelectionClassId(),cmd.getId()});
                }
                if (fh != null) {
                    nxDlg = fh.onSubmit(new FormHandler.Context(group.entityGroup, cmd.getId()));
                } else {
                    nxDlg = group.entityGroup.execCommand(cmd.getId(), request.getCommandRq().getInput(), output);
                }
            }
            if (nxDlg != null) {
                writeTo(nxDlg, rsStruct.addNewNextDialog());
            }
        } catch (AppException e) {
            throw EasFaults.exception2Fault(getArte(), e, "Command execution raise application exception");
        } catch (Throwable e) {
            throw EasFaults.exception2Fault(getArte(), e, "Can't execute the command");
        }
        //RADIX-4400
        if (getUsrActionsIsTraced()) {
            if (cmd.scope == ECommandScope.OBJECT || cmd.scope == ECommandScope.PROPERTY) {
                //"User '%1' executed command '%2' for '%3' with PID = '%4'", EAS, Debug
                if (!isFormCommand) {
                    getArte().getTrace().put(
                            Messages.MLS_ID_EAS_COMMAND,
                            new ArrStr(
                            getArte().getUserName(),
                            String.valueOf(cmd.getName()) + " (#" + cmd.getId().toString() + ")",
                            String.valueOf(entity.getRadMeta().getTitle()) + " (#" + entity.getRadMeta().getId().toString() + ")",
                            entity.getPid().toString()));
                } else {
                    getArte().getTrace().put(
                            Messages.MLS_ID_EAS_COMMAND,
                            new ArrStr(
                            getArte().getUserName(),
                            String.valueOf(cmd.getName()) + " (#" + cmd.getId().toString() + ")",
                            String.valueOf(form.getRadMeta().getTitle()) + " (#" + form.getRadMeta().getId().toString() + ")",
                            "<no PID>"));
                }
            } else {
                //"User '%1' executed group command '%2' for '%3'  in presentation '%4' within context '%5' with  filter '%6'"
                getArte().getTrace().put(
                        Messages.MLS_ID_EAS_GRP_COMMAND,
                        new ArrStr(
                        getArte().getUserName(),
                        String.valueOf(cmd.getName()) + " (#" + cmd.getId().toString() + ")",
                        String.valueOf(group.entityGroup.getSelectionClassDef().getTitle()) + " (#" + group.entityGroup.getSelectionClassDef().getId().toString() + ")",
                        String.valueOf(presOptions.selectorPresentation.getName()) + " (#" + presOptions.selectorPresentation.getId().toString() + ")",
                        String.valueOf(presOptions.context),
                        group.getFilterInfo()));
            }
        }
        return res;
    }

    @Override
    void prepare(final XmlObject rqXml) throws ServiceProcessServerFault, ServiceProcessClientFault {
        prepare(((CommandMess) rqXml).getCommandRq());
    }

    @Override
    XmlObject process(final XmlObject rq) throws ServiceProcessFault, InterruptedException {
        final CommandDocument doc = process((CommandMess) rq);
        postProcess(rq, doc.getCommand().getCommandRs());
        return doc;
    }

    private void writeCurPropValTo(final PropertyList curPropsXml, final Entity entity, final RadEditorPresentationDef pres, final Id propId, final PropValHandler propValHandl) {
        final PropertyList.Item propXml = curPropsXml.addNewItem();
        propXml.setId(propId);
        propXml.setIsOwnVal(propValHandl.getIsOwnValue());
        propXml.setIsDefined(propValHandl.getIsOwnValue() || entity.getPropValInheritancePath(propId) != null); //TODO: if props influencing inheritance are also changed - its not correct
        final RadPropDef prop = entity.getRadMeta().getPropById(propId);
        final EValType valType = prop.getValType();
        ParentInfo ptValInfo = null;
        if (valType == EValType.PARENT_REF || valType == EValType.OBJECT) {
            try {
                final Entity refVal = (Entity) propValHandl.getValue();
                ptValInfo = getParentInfo(entity, prop, refVal, pres);
            } catch (EntityObjectNotExistsError e) {
                ptValInfo = getParentInfo(entity, prop, e, pres);
            } catch (WrongPidFormatError error){
                ptValInfo = getParentInfo(entity, prop, new EntityObjectNotExistsError(error), pres);
            }
        }
        EasValueConverter.objVal2EasPropXmlVal(propValHandl.getValue(), ptValInfo, valType, propXml);
    }

    private void writeCurPropValTo(final PropertyList curPropsXml, final FormHandler form, final Id propId, final Object propVal) {
        final PropertyList.Item propXml = curPropsXml.addNewItem();
        propXml.setId(propId);
        propXml.setIsOwnVal(true);
        propXml.setIsDefined(true); //TODO: if props influencing inheritance are also changed - its not correct
        final RadPropDef prop = form.getRadMeta().getPropById(propId);
        final EValType valType = prop.getValType();
        ParentInfo ptValInfo = null;
        if (valType == EValType.PARENT_REF || valType == EValType.OBJECT) {
            try {
                final Entity refVal = (Entity) propVal;
                ptValInfo = getParentInfo(form, prop, refVal, null);
            } catch (EntityObjectNotExistsError e) {
                ptValInfo = getParentInfo(form, prop, e, null);
            } catch (WrongPidFormatError error) {
                ptValInfo = getParentInfo(form, prop, new EntityObjectNotExistsError(error), null);
            }
        }
        EasValueConverter.objVal2EasPropXmlVal(propVal, ptValInfo, valType, propXml);
    }
}
