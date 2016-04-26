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


import org.apache.xmlbeans.XmlObject;

import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.presentations.RadClassPresentationDef;
import org.radixware.kernel.server.types.EntityGroup;
import org.radixware.schemas.eas.ListInstantiatableClassesMess;
import org.radixware.schemas.eas.ListInstantiatableClassesRq;
import org.radixware.schemas.eas.ListInstantiatableClassesRs;
import org.radixware.schemas.easWsdl.ListInstantiatableClassesDocument;
import org.radixware.schemas.netporthandler.ExceptionEnum;

final class ListInstantiatableClassesRequest extends ObjectRequest {

    ListInstantiatableClassesRequest(final ExplorerAccessService presenter) {
        super(presenter);
    }

    final ListInstantiatableClassesDocument process(final ListInstantiatableClassesMess request) throws ServiceProcessFault, InterruptedException {
        final ListInstantiatableClassesRq rqParams = request.getListInstantiatableClassesRq();
        final DdsTableDef tab = getEntityTable(rqParams);
        final PresentationOptions presOptions = getPresentationOptions(rqParams, null, true, false, null);
        getArte().switchToReadonlyTransaction();//it's readonly request
        if (presOptions.context == null || presOptions.context.getCreationEditorPresentationIds() == null || presOptions.context.getCreationEditorPresentationIds().isEmpty()) {
            throw new ServiceProcessClientFault(ExceptionEnum.APPLICATION_ERROR.toString(), "Can't determinate CreationEditorPresentation from context.\nContext is undefined or invalid.", null, null);
        }
        final RadClassDef classDef = presOptions.context.getClassDef() != null ? presOptions.context.getClassDef() : getArte().getDefManager().getClassDef(RadClassDef.getEntityClassIdByTableId(tab.getId()));
        final RadClassPresentationDef classPresDef = classDef.getPresentation();
        final EntityGroup entGrp = getArte().getGroupHander(tab.getId());
        presOptions.initEntGrp(entGrp, null, null, null, null);        
        final ListInstantiatableClassesDocument res = ListInstantiatableClassesDocument.Factory.newInstance();
        final ListInstantiatableClassesRs rsStruct = res.addNewListInstantiatableClasses().addNewListInstantiatableClassesRs();
        final org.radixware.schemas.eas.InstantiatableClasses classesXml = rsStruct.addNewClasses();
        InstantiatableClassesCollector.collectInstantiatableClasses(classPresDef, entGrp, presOptions, classesXml);
        return res;
    }
    
    @Override
    void prepare(final XmlObject rqXml) throws ServiceProcessServerFault, ServiceProcessClientFault {
        prepare(((ListInstantiatableClassesMess) rqXml).getListInstantiatableClassesRq());
    }

    @Override
    XmlObject process(final XmlObject rq) throws ServiceProcessFault, InterruptedException {
        final ListInstantiatableClassesDocument doc = process((ListInstantiatableClassesMess) rq);
        postProcess(rq, doc.getListInstantiatableClasses().getListInstantiatableClassesRs());
        return doc;
    }
}
