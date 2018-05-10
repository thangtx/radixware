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
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.xmlbeans.XmlObject;

import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.enums.EEntityInitializationPhase;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.types.IKernelCharEnum;
import org.radixware.kernel.common.types.IKernelEnum;
import org.radixware.kernel.common.types.IKernelIntEnum;
import org.radixware.kernel.common.types.IKernelStrEnum;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.exceptions.EntityObjectNotExistsError;
import org.radixware.kernel.server.exceptions.WrongPidFormatError;
import org.radixware.kernel.server.meta.clazzes.IRadPropWriteAccessor;
import org.radixware.kernel.server.meta.clazzes.IRadRefPropertyDef;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.clazzes.RadDetailParentRefPropDef;
import org.radixware.kernel.server.meta.clazzes.RadParentPropDef;
import org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.meta.presentations.RadClassPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.IRadClassInstance;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.server.types.PresentationEntityAdapter;
import org.radixware.kernel.server.types.PropValHandler;
import org.radixware.kernel.server.types.PropValHandlersByIdMap;
import org.radixware.kernel.server.types.presctx.PresentationContext;
import org.radixware.schemas.eas.Actions;
import org.radixware.schemas.eas.Definition;
import org.radixware.schemas.eas.ExceptionEnum;
import org.radixware.schemas.eas.PropertyList;
import org.radixware.schemas.eas.SetParentMess;
import org.radixware.schemas.eas.SetParentRq;
import org.radixware.schemas.eas.SetParentRs;
import org.radixware.schemas.easWsdl.SetParentDocument;

final class SetParentRequest extends ObjectRequest {
    
    private final static Object EXCEPTION_MARKER = new Object();

    SetParentRequest(final ExplorerAccessService presenter) {
        super(presenter);
    }

    final SetParentDocument process(final SetParentMess request) throws ServiceProcessFault, InterruptedException {
        final SetParentRq rqParams = request.getSetParentRq();
        final SetParentDocument res = SetParentDocument.Factory.newInstance();
        final SetParentRs rsStruct = res.addNewSetParent().addNewSetParentRs();        
        if (rqParams.isSetForm()){
            final org.radixware.schemas.eas.Form xmlForm = rqParams.getForm();
            final PropertyList propsXml = 
                processForClassInstance(rqParams, xmlForm.getProperties(), getFormHandler(xmlForm, false) );
            rsStruct.setProperties(propsXml);
            return res;
        }else if (rqParams.isSetReport()){
            final org.radixware.schemas.eas.Report xmlReport = rqParams.getReport();
            final PropertyList propsXml = 
                processForClassInstance(rqParams, xmlReport.getProperties(), getReport(xmlReport, false) );
            rsStruct.setProperties(propsXml);
            return res;
        }
        final RadClassDef classDef = getClassDef(rqParams);
        final PresentationOptions presOptions = getPresentationOptions(rqParams, classDef, false, false, rqParams.getEditorPresentation());
        final RadEditorPresentationDef edPres = presOptions.editorPresentation;
        final Entity entity;
        Entity src = null;
        try {
            if (rqParams.isSetPID()) {
                entity = getArte().getEntityObject(new Pid(getArte(), classDef.getEntityId(), rqParams.getPID()));
                //getArte().unregisterExistingEntityObject(entity); // сносим регистрацию, чтобы Arte не видело изменений, которые комитить рано
            } else {
                if (rqParams.isSetSrcPID()) {
                    try {
                        src = getArte().getEntityObject(new Pid(getArte(), classDef.getEntityId(), rqParams.getSrcPID()));
                    } catch (Throwable e) {
                        throw EasFaults.exception2Fault(getArte(), e, "Can't get the source object class instance");
                    }
                }
                entity = (Entity) getArte().newObject(classDef.getId());
            }
        } catch (Throwable e) {
            throw EasFaults.exception2Fault(getArte(), e, "Can't get a class instance");
        }

        //presOptions.checkContextEditable(); //RADIX-2901: property can be readonly but its destination is editable
        presOptions.assertEdPresIsAccessible(entity);
        //RADIX-3157: setParent can be used for actualizing the parent properties of a readonly object (after parent has been edited)
        //if (presOptions.editorPresentation.getTotalRestrictions(entity).getIsUpdateRestricted()) {
        //	throw EasFaults.newAccessViolationFault("edit object");
        //}

        final Definition ptXml = rqParams.getParentTitleProperty();
        if (ptXml == null) {
            throw EasFaults.newParamRequiedFault("ParentTitleProperty", rqParams.getDomNode().getNodeName());
        }
        final RadPropDef ptProp = entity.getRadMeta().getPropById(ptXml.getId());
        if (!(ptProp instanceof IRadRefPropertyDef)) {
            throw new ServiceProcessClientFault(ExceptionEnum.APPLICATION_ERROR.toString(), "Only parent reference property is allowed in SetParent query", null, null);
        }
//RADIX-3157: setParent can be used for actualizing of parent properties of readonly object (after parent has been edited)
//		final EEditPossibility ptPropEdPossibility = edPres.getPropEditPossibilityByPropId(entity.getPresentationMeta(), ptProp.getId());
//		if (ptPropEdPossibility == EEditPossibility.NEVER ||
//				(entity.isInDatabase(false) ? ptPropEdPossibility == EEditPossibility.ON_CREATE : ptPropEdPossibility == EEditPossibility.ONLY_EXISTING)) {
//			throw EasFaults.newAccessViolationFault("edit property \"" + ptProp.getName() + "\" (#" + ptProp.getId() + ")");
//		}
        final DdsReferenceDef ptRef = getParentRef(ptProp);
        final PropValLoadFilter parentPropFilter = new PropValLoadFilter() {

            @Override
            public boolean skip(final PropVal val) {
                //to ignore old parent prop vals that will be changed after setting new FK
                if (val.prop instanceof RadParentPropDef) {
                    IRadRefPropertyDef p = ((RadParentPropDef) val.prop).getRefProps().get(0);
                    return p == ptProp || (ptRef != null && ptRef == p.getReference());
                }
                return false;
            }
        };        
        
        final PropValHandlersByIdMap receivedPropValsById = new PropValHandlersByIdMap();
        writeCurData2Map(entity.getRadMeta(), edPres, rqParams.getCurrentData(), receivedPropValsById, parentPropFilter);
        final boolean isNewEntity = !entity.isInDatabase(false);
        if (isNewEntity) {
            //making a copy of received propVals as initPropVals could be modified by init
            final PropValHandlersByIdMap initPropValsById = new PropValHandlersByIdMap(receivedPropValsById);
            initNewObject(entity, presOptions.context, edPres, initPropValsById, src, EEntityInitializationPhase.TEMPLATE_EDITING);
        }

        final RadSelectorPresentationDef selPres = (rqParams.isSetSelectorPresentation() ? getSelPres(rqParams.getSelectorPresentation(), entity.getRadMeta()) : null);        
        final PresentationEntityAdapter presEntAdapter = getArte().getPresentationAdapter(entity);        
        final PresentationContext presCtx = getPresentationContext(getArte(), presOptions.context, null);
        presEntAdapter.setPresentationContext(presCtx);
        presOptions.assertEdPresIsAccessible(presEntAdapter);
        final PropValLoadFilter parentRefFilter = new PropValLoadFilter() {
            @Override
            public boolean skip(final PropVal val) {
                return val.prop==ptProp || parentPropFilter.skip(val);
            }
        };
        //TWRBS-4192 значение непосредственно самого parentRef свойства должно устанавливаться только один раз и самым последним.
        writeCurData2Entity(presEntAdapter, edPres, rqParams.getCurrentData(), null, parentRefFilter);
        final PropValHandler ptPropValHandler = receivedPropValsById.get(ptProp.getId());
        if (ptPropValHandler.getIsOwnValue()) {
            final Object receivedVal = ptPropValHandler.getValue();
            if (receivedVal instanceof Entity && !((Entity)receivedVal).isInDatabase(true)){//RADIX-8476
                throw new EntityObjectNotExistsError(((Entity)receivedVal).getPid());
            }
            final boolean isPropertyReadonly = ptProp.getAccessor() instanceof IRadPropWriteAccessor==false;            
            if (isPropertyReadonly){
                //RADIX-6996 Если свойство только для чтения,
                //и его значение не меняется то выполнять сеттер не нужно 
                //(например, при обновлении текущего заголовка объекта сущности).
                boolean sameValue;
                try{
                    final Object curVal = presEntAdapter.getProp(ptProp.getId());
                    sameValue = Objects.equals(normalizeVal(receivedVal), normalizeVal(curVal));
                }catch(EntityObjectNotExistsError error){
                    sameValue = false;
                }
                if (!sameValue){
                    //Все равно вызываем метод изменения значения readonly свойства 
                    //т.к. он может быть перекрыт.
                    presEntAdapter.setProp(ptProp.getId(), receivedVal);
                }
            }else{
                presEntAdapter.setProp(ptProp.getId(), receivedVal);
            }
        } else {
            presEntAdapter.setPropHasOwnVal(ptProp.getId(), false);
        }
        getArte().switchToReadonlyTransaction(); // to prevent commiting of changes before saving in editor
        final Collection<RadPropDef> propDefs = selPres != null ? selPres.getUsedPropDefs(entity.getPresentationMeta()) : edPres.getUsedPropDefs(entity.getPresentationMeta());
        final Collection<RadPropDef> propsToReturn = new ArrayList<>(propDefs.size()); //list of properties to return
        // adding columns from the same join
        RadPropDef radRefPropDef;
        final Collection<Id> modifiedPropIds = entity.getPersistentModifiedPropIds(); //DBP-1539 return all modified props because setParentRef can be overriden
        for (RadPropDef prop : propDefs) { //fill propToReturnIds
            if (modifiedPropIds.contains(prop.getId())) { //DBP-1539 return all modified props because setParentRef can be overriden
                if (!receivedPropValsById.containsKey(prop.getId())) {
                    propsToReturn.add(prop);
                    continue;
                }
                final Object receivedVal = receivedPropValsById.get(prop.getId());
                final Object curVal = presEntAdapter.getProp(prop.getId());
                if (receivedVal == null ? curVal != null : !normalizeVal(receivedVal).equals(normalizeVal(curVal))) {
                    propsToReturn.add(prop);
                    continue;
                }
            }
            //adding columns from the same join
            try {
                if (prop instanceof RadParentPropDef) {
                    radRefPropDef = entity.getRadMeta().getPropById(((RadParentPropDef) prop).getRefProps().get(0).getId());
                } else if (prop instanceof IRadRefPropertyDef) {
                    radRefPropDef = prop;
                } else {
                    radRefPropDef = null;
                }
                if (radRefPropDef != null) {
                    if ( //Презентационное свойство использует тот же DacRefProp
                            radRefPropDef.equals(ptProp)
                            || //DacParentRef - разные, но начинаются с одной и той же связи
                            isDacRefPropUseRef(radRefPropDef, ptRef)
                            || //...или была косвенная модификация значения свойства-ссылки 
                            //(например, в сеттере ptProp) TWRBS-4192
                            modifiedPropIds.contains(radRefPropDef.getId())) {
                        propsToReturn.add(prop);
                    }
                }
            } catch (DefinitionNotFoundError e) {//only dbu properties can't be parent props
                continue;
            }

        }
        //adding columns wich use modified referenceProps for value inheritance
        for (RadPropDef prop : propDefs) { //fill propToReturnIds
            if (propsToReturn.contains(prop)) {
                continue;
            }
            if (prop.getIsValInheritable()) {
                RadPropDef.ValInheritancePath path = entity.getPropValInheritancePath(prop.getId());
                if (path==null){
                    path = getFirstPossiblePropValInheritancePath(entity, prop.getId());
                }
                if (path != null) {
                    final List<Id> refPropIds = path.getRefPropIds();
                    if (!refPropIds.isEmpty() && propsToReturn.contains(entity.getRadMeta().getPropById(refPropIds.get(0)))) {
                        propsToReturn.add(prop);
                    }
                }
            }
        }

        final boolean withBlobProps = rqParams.isSetRespWithLOBValues() ? rqParams.getRespWithLOBValues() : true;
        writeProps(rsStruct.addNewProperties(), presEntAdapter, withBlobProps, edPres, presOptions.context, propsToReturn, isNewEntity);
        if (!isNewEntity) {
            try {
                rsStruct.setObjectTitle(entity.calcTitle(edPres.getObjectTitleFormat()));
            } catch (Throwable e) {
                throw EasFaults.exception2Fault(getArte(), e, "Can't generate the title");
            }
        }
        try {
            final Actions disActXml = rsStruct.addNewDisabledActions();
            if (writeDisbaledObjActions(disActXml, presEntAdapter, edPres, presOptions, null) == 0) {
                rsStruct.unsetDisabledActions();
            }
        } catch (Throwable e) {
            throw EasFaults.exception2Fault(getArte(), e, "Can't check object command is disabled");
        }
        //trace("SetParentRequest.process() finished");
        return res;
    }
    
    private PropertyList processForClassInstance(final SetParentRq rqParams, final PropertyList clientPropListXml, final IRadClassInstance classInstance) throws ServiceProcessFault, InterruptedException{        
        getArte().switchToReadonlyTransaction();
        final Definition ptXml = rqParams.getParentTitleProperty();
        if (ptXml == null) {
            throw EasFaults.newParamRequiedFault("ParentTitleProperty", rqParams.getDomNode().getNodeName());
        }
        final RadClassDef classMeta = classInstance.getRadMeta();
        final RadPropDef ptProp = classMeta.getPropById(ptXml.getId());
        if (!(ptProp instanceof IRadRefPropertyDef)) {
            throw new ServiceProcessClientFault(ExceptionEnum.APPLICATION_ERROR.toString(), "Only parent reference property is allowed in SetParent query", null, null);
        }
        final PropValHandlersByIdMap receivedPropValsById = new PropValHandlersByIdMap();
        {//чтение и установка значений свойств, которые прислал клиент
            final DdsReferenceDef ptRef = getParentRef(ptProp);
            final PropValLoadFilter parentPropFilter = new PropValLoadFilter() {

                @Override
                public boolean skip(final PropVal val) {
                    if (val.prop==ptProp){
                        return true;
                    }
                    //to ignore old parent prop vals that will be changed after setting new FK                
                    if (val.prop instanceof RadParentPropDef) {
                        IRadRefPropertyDef p = ((RadParentPropDef) val.prop).getRefProps().get(0);
                        return p == ptProp || (ptRef != null && ptRef == p.getReference());
                    }
                    return false;
                }
            };                            
            writeCurData2Map(classMeta, classMeta.getProps(), clientPropListXml, receivedPropValsById, parentPropFilter);
            for (Map.Entry<Id, PropValHandler> propVal : receivedPropValsById.entrySet()) {
                final Id propId = propVal.getKey();
                final RadPropDef prop = classMeta.getPropById(propId);
                if (prop.getAccessor() instanceof IRadPropWriteAccessor) {
                    classInstance.setProp(propId, propVal.getValue().getValue());
                }
            }
        }
                
        final Map<Id,Object> initialProps = new HashMap<>();//Значения свойств до изменения значения свойства-ссылки
        final RadClassPresentationDef classPresentation = classInstance.getRadMeta().getPresentation();
        for (RadPropDef propDef: classInstance.getRadMeta().getProps()){
            if (classPresentation.getPropPresById(propDef.getId())!=null){
                initialProps.put(propDef.getId(), getNormalizedPropertyValue(classInstance,propDef.getId()) );
            }
        }
                
        {//Изменение значения свойства-ссылки
            final PropValHandler ptPropValHandler;
            {//чтение значения свойства ссылки, которое прислал клиент
                final PropValLoadFilter parentPropFilter = new PropValLoadFilter() {
                    @Override
                    public boolean skip(final PropVal val) {
                        return val.prop!=ptProp;
                    };
                };
                final PropValHandlersByIdMap propValsById = new PropValHandlersByIdMap();
                writeCurData2Map(classMeta, classMeta.getProps(), clientPropListXml, propValsById, parentPropFilter);            
                ptPropValHandler = propValsById.get(ptProp.getId());
                if (ptPropValHandler==null){
                    throw new ServiceProcessClientFault(ExceptionEnum.APPLICATION_ERROR.toString(), "Value of parent reference property was not received", null, null);
                }
            }
            classInstance.setProp(ptProp.getId(),ptPropValHandler.getValue());
        }
        
        final List<Id>  modifiedPropIds = new LinkedList<>();
        {//Поиск свойств, которые были изменены при установке значения свойства-ссылки
            for (Map.Entry<Id,Object> entry: initialProps.entrySet()){
                if (!Objects.equals(entry.getValue(), getNormalizedPropertyValue(classInstance, entry.getKey()) )){
                    modifiedPropIds.add(entry.getKey());
                }
            }
        }
        
        final PropertyList resultPropsList = PropertyList.Factory.newInstance(); //list of properties to return
        {//Заполнение списка свойств, которые нужно передать клиенту
            for (Id propId: initialProps.keySet()){
                final RadPropDef prop = classMeta.getPropById(propId);                
                if ( modifiedPropIds.contains(propId) ){
                    final Object currentVal = classInstance.getProp(propId);
                    if (!receivedPropValsById.containsKey(propId)) {                        
                        writeCurPropValTo(resultPropsList, classInstance, prop, currentVal);
                        continue;
                    }
                    final Object receivedVal = receivedPropValsById.get(prop.getId());                    
                    if (!Objects.equals(normalizeVal(currentVal), normalizeVal(receivedVal))) {
                        writeCurPropValTo(resultPropsList, classInstance, prop, currentVal);
                        continue;
                    }                                        
                }
                RadPropDef radRefPropDef;
                try {
                    if (prop instanceof RadParentPropDef) {
                        radRefPropDef = classMeta.getPropById(((RadParentPropDef) prop).getRefProps().get(0).getId());
                    } else if (prop instanceof IRadRefPropertyDef) {
                        radRefPropDef = prop;
                    } else {
                        radRefPropDef = null;
                    }
                    if (radRefPropDef != null) {
                        if ( //Презентационное свойство использует тот же DacRefProp
                                radRefPropDef.equals(ptProp)
                                || //...или была косвенная модификация значения свойства-ссылки 
                                //(например, в сеттере ptProp) TWRBS-4192
                                modifiedPropIds.contains(radRefPropDef.getId())) {  
                            final Object currentVal = classInstance.getProp(propId);
                            writeCurPropValTo(resultPropsList, classInstance, prop, currentVal);
                        }
                    }
                } catch (DefinitionNotFoundError e) {//only dbu properties can't be parent props
                    continue;
                }                                        
            }
        }
        
        return resultPropsList;
    }
    
    private Object getNormalizedPropertyValue(final IRadClassInstance classInstance, final Id propertyId){
        try{
            return normalizeVal(classInstance.getProp(propertyId));
        }catch(RuntimeException exception){
            return EXCEPTION_MARKER;//NOPMD
        }
    }
    
    private void writeCurPropValTo(final PropertyList curPropsXml, final IRadClassInstance classInstance, final RadPropDef prop, final Object propVal) {
        final PropertyList.Item propXml = curPropsXml.addNewItem();
        propXml.setId(prop.getId());
        propXml.setIsOwnVal(true);
        propXml.setIsDefined(true); //TODO: if props influencing inheritance are also changed - its not correct
        final EValType valType = prop.getValType();
        ParentInfo ptValInfo = null;
        if (valType == EValType.PARENT_REF || valType == EValType.OBJECT) {
            try {
                final Entity refVal = (Entity) propVal;
                ptValInfo = getParentInfo(classInstance, prop, refVal, null);
            } catch (EntityObjectNotExistsError e) {
                ptValInfo = getParentInfo(classInstance, prop, e, null);
            } catch (WrongPidFormatError error) {
                ptValInfo = getParentInfo(classInstance, prop, new EntityObjectNotExistsError(error), null);
            }
        }
        EasValueConverter.objVal2EasPropXmlVal(propVal, ptValInfo, valType, propXml);
    }    

    private boolean isDacRefPropUseRef(final RadPropDef radRefPropDef, final DdsReferenceDef ptRef) {
        if (ptRef == null) {
            return false;
        }
        if (radRefPropDef instanceof RadInnateRefPropDef) {
            final RadInnateRefPropDef refProp = (RadInnateRefPropDef) radRefPropDef;
            if (refProp.getReference() == ptRef) {
                return true;
            }
        } else if (radRefPropDef instanceof RadDetailParentRefPropDef) {
            final RadDetailParentRefPropDef refProp = (RadDetailParentRefPropDef) radRefPropDef;
            if (refProp.getReference() == ptRef) {
                return true;
            }
        }
        return false;
    }

    @Override
    void prepare(final XmlObject rqXml) throws ServiceProcessServerFault, ServiceProcessClientFault {
        super.prepare(rqXml);
        prepare(((SetParentMess) rqXml).getSetParentRq());
    }

    @Override
    XmlObject process(final XmlObject rq) throws ServiceProcessFault, InterruptedException {
        SetParentDocument doc = null;
        try{
            doc = process((SetParentMess) rq);
        }finally{
            postProcess(rq, doc==null ? null : doc.getSetParent().getSetParentRs());
        }
        return doc;
    }

    private Object normalizeVal(final Object val) {
        if (val == null) {
            return null;
        }
        if (val instanceof Pid) {
            return getArte().getEntityObject((Pid) val);
        }
        if (val instanceof IKernelEnum) {
            if (val instanceof IKernelStrEnum) {
                return ((IKernelStrEnum) val).getValue();
            }
            if (val instanceof IKernelCharEnum) {
                return ((IKernelCharEnum) val).getValue();
            }
            if (val instanceof IKernelIntEnum) {
                return ((IKernelIntEnum) val).getValue();
            }
        }
        return val;
    }
}
