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
import java.util.List;

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.enums.EAutoUpdateReason;
import org.radixware.kernel.common.enums.EEntityInitializationPhase;

import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.enums.EEntityLockMode;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
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

    CreateRequest(final ExplorerAccessService presenter) {
        super(presenter);
    }

    final CreateDocument process(final CreateMess request) throws ServiceProcessFault, InterruptedException {
        //trace("CreateRequest.process() started");
        //IN
        final CreateRq rqParams = request.getCreateRq();
        final RadClassDef classDef = getClassDef(rqParams);
        assertNotUserFuncOrUserCanDevUserFunc(classDef);
        final CreateRq.Data dataXml = rqParams.getData();
        if (dataXml == null) {
            throw EasFaults.newParamRequiedFault("Data", rqParams.getDomNode().getNodeName());
        }
        final PropertyList propsXml = dataXml.getProperties();
        if (propsXml == null) {
            throw EasFaults.newParamRequiedFault("Properties", dataXml.getDomNode().getNodeName());
        }
        final List<PropertyList.Item> propItemXmls = propsXml.getItemList();
        final PresentationOptions presOptions = getPresentationOptions(rqParams, classDef, false, false, rqParams.getPresentation());
        if (presOptions.context instanceof ObjPropContext) {
            final ObjPropContext c = (ObjPropContext) presOptions.context;
            if (c.getContextPropertyDef().getValType() == EValType.OBJECT) {
                c.assertOwnerPropIsEditable();
            }
        }
        Entity src = null;
        if (dataXml.isSetSrcPID()) {
            try {
                src = getArte().getEntityObject(new Pid(getArte(), classDef.getEntityId(), dataXml.getSrcPID()));
            } catch (Throwable e) {
                throw EasFaults.exception2Fault(getArte(), e, "Can't get the source object class instance");
            }
        }

        Entity entity;
        try {
            entity = (Entity) getArte().newObject(classDef.getId());
        } catch (RuntimeException e) {
            throw EasFaults.exception2Fault(getArte(), e, "Can't get a class instance");
        }
        //there is no use to check EdPres restriction before inserting object in DB (i.e. until Access Control Partition are not defined)
        //if (context.editorPresentation.getDefRestrictions(getArte()).getIsCreateRestricted())
        //	throw EasFaults.newAccessViolationFault("create object");
        presOptions.assertEdPresIsAccessibile(entity);

        final List<RadPropDef> updatedProps = new ArrayList<RadPropDef>();
        RadEditorPresentationDef pres = presOptions.editorPresentation;
        if (pres == null) {
            throw EasFaults.newParamRequiedFault("Presentation", rqParams.getDomNode().getNodeName());
        }        
        final PresentationEntityAdapter presEntAdapter = getArte().getPresentationAdapter(entity);
        final PresentationContext presContext = getPresentationContext(getArte(), presOptions.context, null);
        presEntAdapter.setPresentationContext(presContext);
        loadPropsFromXml(propItemXmls, presEntAdapter, pres, updatedProps, null, NULL_PROP_LOAD_FILTER, EReadonlyPropModificationPolicy.ALLOWED /*we can not say if property modified in editor or by PrepareCreate :(, it should be allowed to modify readonly props in prepare create*/);

        //check prop updatable �� ������, ��� ��� ���������� prepare create �� �������� �� �������
        //� ������ ���������� ����� �������� ��������� ������, � ����� ������������

        final PropValHandlersByIdMap initPropValsById = new PropValHandlersByIdMap();
        //������������ ������������ �������� � initPropVals ��� ���
        //��� ��������� ����� ������� ��� ����� Modified
        //� init ����� �� ���������
        for (RadPropDef updatedProp : updatedProps) {
            final Id id = updatedProp.getId();
            initPropValsById.put(updatedProp.getId(), new PropValHandler(entity.getPropHasOwnVal(id), entity.getProp(id)));
        }
        initNewObject(entity, presOptions.context, pres, initPropValsById, src, EEntityInitializationPhase.INTERACTIVE_CREATION);
        try {
//			checkMandatoryProps(entity, pres);
            if ((presOptions.context instanceof ObjPropContext)
                    && ((ObjPropContext) presOptions.context).getContextPropertyDef().getValType() == EValType.OBJECT) { // ������� ������ UP ����� ������ ��� �� ���� ���������
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
                    ownerAdapter.setProp(c.getPropertyId(), entity);
                    ownerAdapter.update();
                    entity = (Entity) ownerAdapter.getProp(c.getPropertyId()); //adapter could create another instance
                } catch (Exception e) {
                    throw EasFaults.exception2Fault(getArte(), e, "Can't register new object as UserProperty value");
                }
            } else {
                entity = presEntAdapter.create(src);
            }
            if (entity!=null && !entity.isDiscarded()){
                entity.setReadRights(true);
                getArte().updateDatabase(EAutoUpdateReason.PREPARE_COMMIT);//�� ������ ���� ���������� afterCreate ������ this ��� join-�
                entity.reread(EEntityLockMode.NONE, null);
            }
        } catch (RuntimeException e) {
            throw EasFaults.exception2Fault(getArte(), e, "Error on create");
        }

        final CreateDocument res = CreateDocument.Factory.newInstance();
        final CreateRs rsStruct = res.addNewCreate().addNewCreateRs();
        if (entity==null || entity.isDiscarded()){
            return res;
        }
        rsStruct.setPID(entity.getPid().toString());
        if (presOptions.selectorPresentation == null) {
            pres = getActualEditorPresentation(presEntAdapter, Collections.singletonList(pres), true);
        } else {
            pres = getActualEditorPresentation(presEntAdapter, presOptions.selectorPresentation.getEditorPresentations(), true);
        }

        if (pres.getTotalRestrictions(entity).getIsCreateRestricted()) {
            throw EasFaults.newAccessViolationFault(getArte(), Messages.MLS_ID_INSUF_PRIV_TO_CREATE_OBJECT, null);
        }
        //checkPropsAccessibleInPres(updatedProps, pres); //DBP-1528
        rsStruct.setTitle(entity.calcTitle(pres.getObjectTitleFormat()));
        rsStruct.addNewPresentation().setId(pres.getId());

        //RADIX-4400 "User '%1' created '%2' with PID = '%3'", EAS, Debug
        if (getUsrActionsIsTraced()) {
            getArte().getTrace().put(
                    Messages.MLS_ID_EAS_CREATE,
                    new ArrStr(
                    getArte().getUserName(),
                    String.valueOf(entity.getRadMeta().getTitle()) + " (#" + entity.getRadMeta().getId().toString() + ")",
                    entity.getPid().toString()));
        }

        return res;
    }

//	private final void checkMandatoryProps(final Entity entity, final RadPresentationDef pres) throws ServiceProcessClientFault {
//		RadPropDef nullProp = null;
//		for (RadPropDef prop : entity.getRadMeta().getProps()) { // � ������ ������ ��������� ������ ParentRef-� (��� ��� ������� ����� ����� ���� ��������, ���� � ����������)
//			if (
//                pres.getPropIsNotNullByPropId(entity.getPresentationMeta(), prop.getId()) &&
//                entity.getProp(prop.getId()) == null
//            ) {
//				if (prop instanceof IRadRefPropertyDef) {
//					throw EasFaults.newPropMandatoryFault(entity.getRadMeta().getId(), prop.getId());
//				} else {
//					nullProp = prop;
//				}
//			}
//		}
//		if (nullProp != null) {
//			throw EasFaults.newPropMandatoryFault(entity.getRadMeta().getId(), nullProp.getId());
//		}
//	}
    @Override
    void prepare(final XmlObject rqXml) throws ServiceProcessServerFault, ServiceProcessClientFault {
        prepare(((CreateMess) rqXml).getCreateRq());
    }

    @Override
    XmlObject process(final XmlObject rq) throws ServiceProcessFault, InterruptedException {
        final CreateDocument doc = process((CreateMess) rq);
        postProcess(rq, doc.getCreate().getCreateRs());
        return doc;
    }
}