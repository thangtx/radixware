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
import org.radixware.kernel.common.enums.EEntityInitializationPhase;
import org.radixware.kernel.common.enums.EValType;

import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.server.types.PresentationEntityAdapter;
import org.radixware.kernel.server.types.PropValHandlersByIdMap;
import org.radixware.kernel.server.types.Restrictions;
import org.radixware.kernel.server.types.presctx.PresentationContext;
import org.radixware.schemas.aas.ExceptionEnum;
import org.radixware.schemas.eas.Definition;
import org.radixware.schemas.eas.EditorPages;
import org.radixware.schemas.eas.PrepareCreateMess;
import org.radixware.schemas.eas.PrepareCreateRq;
import org.radixware.schemas.eas.PrepareCreateRs;
import org.radixware.schemas.easWsdl.PrepareCreateDocument;

final class PrepareCreateRequest extends ObjectRequest {

    PrepareCreateRequest(final ExplorerAccessService presenter) {
        super(presenter);
    }

    final PrepareCreateDocument process(final PrepareCreateMess request) throws ServiceProcessFault, InterruptedException {
        final PrepareCreateRq rqParams = request.getPrepareCreateRq();
        final RadClassDef classDef = getClassDef(rqParams);
        final PresentationOptions presOptions = getPresentationOptions(rqParams, classDef, false, false, null);
        if (presOptions.context instanceof ObjPropContext) {
            final ObjPropContext c = (ObjPropContext) presOptions.context;
            if (c.getContextPropertyDef().getValType() == EValType.OBJECT) {
                c.assertOwnerPropIsEditable();
            }
        }
        Entity src = null;
        if (rqParams.isSetData() && rqParams.getData().isSetSrcPID()) {
            try {
                src = getArte().getEntityObject(new Pid(getArte(), classDef.getEntityId(), rqParams.getData().getSrcPID()));
            } catch (Throwable e) {
                throw EasFaults.exception2Fault(getArte(), e, "Can't get the source object");
            }
        }
        final Entity entity;
        try {
            entity = (Entity) getArte().newObject(classDef.getId());
        } catch (Throwable e) {
            throw EasFaults.exception2Fault(getArte(), e, "Can't get a class instance");
        }

        final List<Id> edPresIds;
        if (rqParams.getPresentations() != null && !rqParams.getPresentations().getItemList().isEmpty()) {
            final List<Definition> presXmls = rqParams.getPresentations().getItemList();
            edPresIds = new ArrayList<>(presXmls.size());
            for (Definition presXml : presXmls) {
                edPresIds.add(presXml.getId());
            }
        } else {
            if (presOptions.context == null || presOptions.context.getCreationEditorPresentationIds() == null || presOptions.context.getCreationEditorPresentationIds().isEmpty()) {
                throw new ServiceProcessClientFault(ExceptionEnum.APPLICATION_ERROR.toString(), "Can't determinate CreationEditorPresentation from context.\nContext is undefined or invalid.", null, null);
            }
            edPresIds = presOptions.context.getCreationEditorPresentationIds();
        }

        final List<RadEditorPresentationDef> presList = new ArrayList<>(edPresIds.size());
        for (Id id : edPresIds) {
            presList.add(classDef.getPresentation().getEditorPresentationById(id));
        }        
        final PresentationEntityAdapter<Entity> presEntAdapter = getArte().getPresentationAdapter(entity);
        final PresentationContext presCtx = getPresentationContext(getArte(), presOptions.context, null);
        presEntAdapter.setPresentationContext(presCtx);
        RadEditorPresentationDef pres = getActualEditorPresentation(presEntAdapter, presList, true);
        PropValHandlersByIdMap initialPropValsById = null;
        if (rqParams.isSetData()) {
            initialPropValsById = new PropValHandlersByIdMap();
            writeCurData2Map(entity.getRadMeta(), pres, rqParams.getData(), initialPropValsById, NULL_PROP_LOAD_FILTER);
        }
        initNewObject(entity, presOptions.context, pres, initialPropValsById, src, EEntityInitializationPhase.TEMPLATE_PREPARATION);
        //entity.setReadonly(true);
        getArte().switchToReadonlyTransaction(); // to prevent commit of new object before it saved from edtitor
        pres = getActualEditorPresentation(presEntAdapter, Collections.singletonList(pres), true);

        //there is no use to check EdPres restriction before inserting object in DB (i.e. until Access Control Partition are not defined)
        //if (pres.getDefRestrictions(getArte()).getIsCreateRestricted())
        //	throw EasFaults.newAccessViolationFault("create object");

        final PrepareCreateDocument res = PrepareCreateDocument.Factory.newInstance();
        final PrepareCreateRs rsStruct = res.addNewPrepareCreate().addNewPrepareCreateRs();
        final Collection<RadPropDef> usedPropDefs = pres.getUsedPropDefs(entity.getPresentationMeta());


        //RADIX-7112
        Restrictions restr = pres.getTotalRestrictions(entity);
        EditorPages enadledEditorPages = rsStruct.addNewEnabledEditorPages();

        if (restr.getIsAccessRestricted() || restr.getIsViewRestricted()) {
            enadledEditorPages.setAll(false);
        } else if (!restr.getIsAllEditPagesRestricted()) {
            enadledEditorPages.setAll(true);
        } else {
            enadledEditorPages.setAll(false);
            Collection<Id> allowedEditPages = restr.getAllowedEditPages();
            for (Id id : allowedEditPages) {
                EditorPages.Item item = enadledEditorPages.addNewItem();
                item.setId(id);
            }
        }
        //end RADIX-7112

        writeReadResponse(rsStruct, presEntAdapter, presOptions, true, pres, usedPropDefs);
        return res;
    }

    @Override
    void prepare(final XmlObject rqXml) throws ServiceProcessServerFault, ServiceProcessClientFault {
        prepare(((PrepareCreateMess) rqXml).getPrepareCreateRq());
    }

    @Override
    XmlObject process(final XmlObject rq) throws ServiceProcessFault, InterruptedException {
        final PrepareCreateDocument doc = process((PrepareCreateMess) rq);
        postProcess(rq, doc.getPrepareCreate().getPrepareCreateRs());
        return doc;
    }
}
