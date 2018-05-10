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
import java.util.Collections;
import java.util.List;

import org.apache.xmlbeans.XmlObject;

import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.enums.EAutoUpdateReason;
import org.radixware.kernel.common.enums.EEditPossibility;
import org.radixware.kernel.common.enums.EEntityLockMode;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef;
import org.radixware.kernel.server.types.Entity;
import org.radixware.schemas.eas.PropertyList;
import org.radixware.schemas.eas.UpdateMess;
import org.radixware.schemas.eas.UpdateRq;
import org.radixware.schemas.eas.UpdateRs;
import org.radixware.schemas.easWsdl.UpdateDocument;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.types.PresentationEntityAdapter;
import org.radixware.kernel.server.types.presctx.PresentationContext;

final class UpdateRequest extends ObjectRequest {

    UpdateRequest(final ExplorerAccessService presenter) {
        super(presenter);
    }

    final UpdateDocument process(final UpdateMess request) throws ServiceProcessFault, InterruptedException {
        //IN
        final UpdateRq rqParams = request.getUpdateRq();
        final RadClassDef classDef = getClassDef(rqParams);
        final PresentationOptions presOptions = getPresentationOptions(rqParams, classDef, false, false, rqParams.getEditorPresentation());
        final Entity entity = getObject(classDef, rqParams);
        if (presOptions.editorPresentation == null) {
            throw EasFaults.newParamRequiedFault("EditorPresentation", rqParams.getDomNode().getNodeName());
        }
        //presOptions.checkContextEditable(); //RADIX-2901: property can be readonly but its destination is editable
        presOptions.assertEdPresIsAccessible(entity);
        RadEditorPresentationDef edPres = presOptions.editorPresentation;
        if (edPres.getTotalRestrictions(entity).getIsUpdateRestricted()) {
            throw EasFaults.newAccessViolationFault(getArte(), Messages.MLS_ID_INSUF_PRIV_TO_UPDATE_OBJECT, null);
        }

        final RadSelectorPresentationDef selPres = (rqParams.isSetSelectorPresentation() ? getSelPres(rqParams.getSelectorPresentation(), entity.getRadMeta()) : null);

        final org.radixware.schemas.eas.Object dataXml = rqParams.getNewData();
        final PropertyList propsXml = dataXml.getProperties();
        final List<PropertyList.Item> propItemXmls = propsXml.getItemList();
        //загрузим свойства
        final ArrayList<RadPropDef> updatedProps = new ArrayList<>();        
        final PresentationEntityAdapter presEntAdapter = getArte().getPresentationAdapter(entity);        
        final PresentationContext presCtx = getPresentationContext(getArte(), presOptions.context ,null);
        presEntAdapter.setPresentationContext(presCtx);
        presOptions.assertEdPresIsAccessible(presEntAdapter);
        if (presEntAdapter.getAdditionalRestrictions(edPres).getIsUpdateRestricted()){
            throw EasFaults.newAccessViolationFault(getArte(), Messages.MLS_ID_INSUF_PRIV_TO_UPDATE_OBJECT, null);
        }
        try {
            loadPropsFromXml(propItemXmls, presEntAdapter, edPres, updatedProps, null, NULL_PROP_LOAD_FILTER, EReadonlyPropModificationPolicy.FORBIDDEN);
            checkPropsUpdatableInPres(entity, updatedProps, edPres);
            if (//see TWRBS-1516
                    (presOptions.context instanceof ObjPropContext)
                    && ((ObjPropContext) presOptions.context).getContextPropertyDef().getValType() == EValType.OBJECT) {
                try {
                    final ObjPropContext c = (ObjPropContext) presOptions.context;
                    if (!c.getContextPropOwner().isInDatabase(false)) {
                        throw EasFaults.newCantSaveObjPropValUntilOwnerSaved();
                    }
                    //RADIX-2990: c.getObject() is unregistered, let's get a new handler
                    final Entity owner = getArte().getEntityObject(c.getContextPropOwner().getPid());
                    final PresentationEntityAdapter ownerAdapter = getArte().getPresentationAdapter(owner); //see TWRBS-1516

                    if (ownerAdapter.beforeUpdatePropObject(c.getPropertyId(), entity)) {
                        presEntAdapter.update();
                        ownerAdapter.afterUpdatePropObject(c.getPropertyId(), entity);
                    }
                } catch (RuntimeException e) {
                    throw EasFaults.exception2Fault(getArte(), e, "Can't clear object value of UserProperty");
                }
            } else {
                presEntAdapter.update();
            }

            getArte().updateDatabase(EAutoUpdateReason.PREPARE_COMMIT);//на случай если обработчик afterCreate правил this или join-ы
            entity.reread(EEntityLockMode.NONE, null);// на случай если тригерами что-то меняется
        } catch (Throwable e) {
            throw EasFaults.exception2Fault(getArte(), e, "Error on update");
        }
        edPres = getActualEditorPresentation(presEntAdapter, Collections.singletonList(edPres), true);
        if (edPres.getTotalRestrictions(entity).getIsUpdateRestricted()) {
            throw EasFaults.newAccessViolationFault(getArte(), Messages.MLS_ID_INSUF_PRIV_TO_UPDATE_OBJECT, null);
        }
        if (presEntAdapter.getAdditionalRestrictions(edPres).getIsUpdateRestricted()){
            throw EasFaults.newAccessViolationFault(getArte(), Messages.MLS_ID_INSUF_PRIV_TO_UPDATE_OBJECT, null);
        }
        checkPropsAccessibleInPres(updatedProps, edPres);
        checkPropsUpdatableInPres(entity, updatedProps, edPres);

        final boolean bWithLOBValues;
        if (rqParams.isSetRespWithLOBValues()) {
            bWithLOBValues = rqParams.getRespWithLOBValues();
        } else {
            bWithLOBValues = true;
        }

        final UpdateDocument res = UpdateDocument.Factory.newInstance();
        final UpdateRs rsStruct = res.addNewUpdate().addNewUpdateRs();

        final Collection<RadPropDef> usedPropDefs;
        if (selPres == null) {
            usedPropDefs = edPres.getUsedPropDefs(entity.getPresentationMeta());
        } else {
            usedPropDefs = new ArrayList<RadPropDef>(edPres.getUsedPropDefs(entity.getPresentationMeta()));
            for (final RadPropDef propDef : selPres.getUsedPropDefs(entity.getPresentationMeta())) {
                if (!usedPropDefs.contains(propDef)) {
                    usedPropDefs.add(propDef);
                }
            }
        }
        writePresentableObject(rsStruct.addNewData(), presEntAdapter, presOptions, bWithLOBValues, edPres, usedPropDefs, false);
        //RADIX-4400 "User '%1' updated '%2' with PID = '%3'", EAS, Debug
        if (getUsrActionsIsTraced()) {
            getArte().getTrace().put(
                    Messages.MLS_ID_EAS_UPDATE,
                    new ArrStr(
                    getArte().getUserName(),
                    String.valueOf(entity.getRadMeta().getTitle()) + " (#" + entity.getRadMeta().getId().toString() + ")",
                    entity.getPid().toString()));
        }

        return res;
    }

    private final void checkPropsUpdatableInPres(final Entity entity, final List<RadPropDef> props, final RadPresentationDef pres) throws ServiceProcessClientFault {
        EEditPossibility propEdPsblty;
        for (RadPropDef prop : props) {
            propEdPsblty = pres.getPropEditPossibilityByPropId(entity.getPresentationMeta(), prop.getId());
            if (propEdPsblty == EEditPossibility.NEVER || propEdPsblty == EEditPossibility.ON_CREATE) {
                throw EasFaults.newAccessViolationFault(getArte(), Messages.MLS_ID_INSUF_PRIV_TO_UPDATE_PROPERTY, "\"" + prop.getName() + "\" (#" + prop.getId() + ")");
            }
        }
    }

    @Override
    void prepare(final XmlObject rqXml) throws ServiceProcessServerFault, ServiceProcessClientFault {
        super.prepare(rqXml);
        prepare(((UpdateMess) rqXml).getUpdateRq());
    }

    @Override
    XmlObject process(final XmlObject rq) throws ServiceProcessFault, InterruptedException {
        UpdateDocument doc = null;
        try{
            doc = process((UpdateMess) rq);
        }finally{
            postProcess(rq, doc==null ? null : doc.getUpdate().getUpdateRs());
        }
        return doc;
    }
}
