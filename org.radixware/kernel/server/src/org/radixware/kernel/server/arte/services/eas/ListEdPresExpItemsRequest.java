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

import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.PresentationEntityAdapter;
import org.radixware.kernel.server.types.Restrictions;
import org.radixware.kernel.server.types.presctx.PresentationContext;
import org.radixware.schemas.eas.ListEdPresVisibleExpItemsMess;
import org.radixware.schemas.eas.ListEdPresVisibleExpItemsRq;
import org.radixware.schemas.eas.ListEdPresVisibleExpItemsRs;
import org.radixware.schemas.easWsdl.ListEdPresVisibleExpItemsDocument;

final class ListEdPresExpItemsRequest extends ObjectRequest {

    public ListEdPresExpItemsRequest(final ExplorerAccessService presenter) {
        super(presenter);
    }

    final ListEdPresVisibleExpItemsDocument process(final ListEdPresVisibleExpItemsMess request) throws ServiceProcessFault, InterruptedException {
        //trace("ListEdPresVisibleExpItemsRequest.process() started");
        final ListEdPresVisibleExpItemsRq rqParams = request.getListEdPresVisibleExpItemsRq();
        final RadClassDef classDef = getClassDef(rqParams);
        
        final PresentationOptions presOptions = getPresentationOptions(rqParams, classDef, false, false, rqParams.getPresentation());
        getArte().switchToReadonlyTransaction();//it's readonly request
        final Entity entity = getObject(classDef, rqParams);
        presOptions.assertEdPresIsAccessible(entity);
        
        

        final ListEdPresVisibleExpItemsDocument res = ListEdPresVisibleExpItemsDocument.Factory.newInstance();
        final ListEdPresVisibleExpItemsRs rsStruct = res.addNewListEdPresVisibleExpItems().addNewListEdPresVisibleExpItemsRs();
        
        final PresentationEntityAdapter presEntAdapter = getArte().getPresentationAdapter(entity);
        final PresentationContext presCtx = getPresentationContext(getArte(), presOptions.context, null);
        presEntAdapter.setPresentationContext(presCtx);
        presOptions.assertEdPresIsAccessible(presEntAdapter);
        final Restrictions additionalRestrictions = presEntAdapter.getAdditionalRestrictions(presOptions.editorPresentation);
        writeAccessibleExplorerItems(entity, presOptions.editorPresentation, additionalRestrictions, rsStruct.addNewVisibleExplorerItems());
                
        return res;
    }
    
    
    @Override
    void prepare(final XmlObject rqXml) throws ServiceProcessServerFault, ServiceProcessClientFault {
        super.prepare(rqXml);
        prepare(((ListEdPresVisibleExpItemsMess) rqXml).getListEdPresVisibleExpItemsRq());
    }

    @Override
    XmlObject process(final XmlObject rq) throws ServiceProcessFault, InterruptedException {
        ListEdPresVisibleExpItemsDocument doc = null;
        try{
            doc = process((ListEdPresVisibleExpItemsMess) rq);
        }finally{
            postProcess(rq, doc==null ? null : doc.getListEdPresVisibleExpItems().getListEdPresVisibleExpItemsRs());
        }
        return doc;
    }
}
