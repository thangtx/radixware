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
import java.util.List;

import org.apache.xmlbeans.XmlObject;

import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.meta.presentations.RadClassPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.PresentationEntityAdapter;
import org.radixware.kernel.server.types.Restrictions;
import org.radixware.kernel.server.types.presctx.PresentationContext;
import org.radixware.schemas.eas.Definition;
import org.radixware.schemas.eas.EditorPages;
import org.radixware.schemas.eas.ReadMess;
import org.radixware.schemas.eas.ReadRq;
import org.radixware.schemas.eas.ReadRs;
import org.radixware.schemas.easWsdl.ReadDocument;


final class ReadRequest extends ObjectRequest {

    ReadRequest(final ExplorerAccessService presenter) {
        super(presenter);
    }

    final ReadDocument process(final ReadMess request) throws ServiceProcessFault, InterruptedException {
        final ReadRq rqParams = request.getReadRq();
        final RadClassDef classDef = getClassDef(rqParams);
        final PresentationOptions presOptions = getPresentationOptions(rqParams, classDef, false, false, null);
        getArte().switchToReadonlyTransaction();//it's readonly request
        final Entity entity = getObject(classDef, rqParams);
        if (rqParams.getPresentations() == null) {
            throw EasFaults.newParamRequiedFault("Presentations", rqParams.getDomNode().getNodeName());
        }
        final List<Definition> presXmls = rqParams.getPresentations().getItemList();
        if (presXmls.isEmpty()) {
            throw EasFaults.newParamRequiedFault("Item", rqParams.getPresentations().getDomNode().getNodeName());
        }
        final ArrayList<RadEditorPresentationDef> presentations = new ArrayList<>();
        for (Definition presXml : presXmls) {
            try {
                presentations.add(entity.getPresentationMeta().getEditorPresentationById(presXml.getId()));
            } catch (DefinitionNotFoundError e) {
                throw EasFaults.newDefWithIdNotFoundFault("Editor presentation", rqParams.getPresentations().getDomNode().getNodeName(), presXml.getId());
            }
        }
        final PresentationEntityAdapter presEntAdapter = getArte().getPresentationAdapter(entity);
        final PresentationContext presCtx = getPresentationContext(getArte(), presOptions.context, null);
        presEntAdapter.setPresentationContext(presCtx);
        final RadEditorPresentationDef pres = getActualEditorPresentation(presEntAdapter, presentations, true);

        final Collection<RadPropDef> propDefs = getPropDefs(rqParams, entity.getPresentationMeta(), pres);

        final boolean bWithLOBValues;
        if (rqParams.isSetWithLOBValues()) {
            bWithLOBValues = rqParams.getWithLOBValues();
        } else {
            bWithLOBValues = true;
        }
        final ReadDocument res = ReadDocument.Factory.newInstance();
        final ReadRs rsStruct = res.addNewRead().addNewReadRs();

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
        
        writeReadResponse(rsStruct, presEntAdapter, presOptions, bWithLOBValues, pres, propDefs);
        
        if (rqParams.getWithAccessibleExplorerItems()){
            writeAccessibleExplorerItems(entity, pres, rsStruct.addNewAccessibleExplorerItems());
        }
        
        return res;
    }

    @Override
    void prepare(final XmlObject rqXml) throws ServiceProcessServerFault, ServiceProcessClientFault {
        prepare(((ReadMess) rqXml).getReadRq());
    }

    @Override
    XmlObject process(final XmlObject rq) throws ServiceProcessFault, InterruptedException {
        final ReadDocument doc = process((ReadMess) rq);
        postProcess(rq, doc.getRead().getReadRs());
        return doc;
    }

    private Collection<RadPropDef> getPropDefs(final ReadRq rq, final RadClassPresentationDef classPres, final RadEditorPresentationDef pres) {
        if (rq.isSetProperties() && rq.getProperties().sizeOfItemArray() > 0) {
            final List<Definition> propXmls = rq.getProperties().getItemList();
            final List<RadPropDef> props = new ArrayList<>(propXmls.size());
            // request contains list of properties to return
            for (Definition propXml : propXmls) {
                if (!pres.isPropertyForbidden(propXml.getId())
                        && (classPres.getPropPresById(propXml.getId()) != null)) {
                    props.add(classPres.getClassDef().getPropById(propXml.getId()));
                }
            }
            return props;
        } else {
            return pres.getUsedPropDefs(classPres);
        }

    }
}
