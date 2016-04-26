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

import java.util.List;

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.enums.EDefType;

import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.meta.presentations.RadExplorerRootDef;
import org.radixware.schemas.eas.ListVisibleExplorerItemsMess;
import org.radixware.schemas.eas.ListVisibleExplorerItemsRq;
import org.radixware.schemas.eas.ListVisibleExplorerItemsRs;
import org.radixware.schemas.eas.ListVisibleExplorerItemsRs.VisibleExplorerItems;
import org.radixware.schemas.easWsdl.ListVisibleExplorerItemsDocument;

final class ListVisibleExplorerItemsRequest extends SessionRequest {

    public ListVisibleExplorerItemsRequest(final ExplorerAccessService presenter) {
        super(presenter);
    }

    final ListVisibleExplorerItemsDocument process(final ListVisibleExplorerItemsMess request) throws ServiceProcessFault, InterruptedException {
        //trace("ListEdPresVisibleExpItemsRequest.process() started");
        final ListVisibleExplorerItemsRq rqParams = request.getListVisibleExplorerItemsRq();
        if (rqParams.getExplorerRoot()==null){
            throw EasFaults.newParamRequiedFault("ExplorerRoot", "ListVisibleExplorerItemsRq");
        }
        final ListVisibleExplorerItemsDocument res = ListVisibleExplorerItemsDocument.Factory.newInstance();
        final ListVisibleExplorerItemsRs rsStruct = res.addNewListVisibleExplorerItems().addNewListVisibleExplorerItemsRs();
        final VisibleExplorerItems xItems = rsStruct.addNewVisibleExplorerItems();

        final Id explorerRootId = rqParams.getExplorerRoot().getId();
        final RadExplorerRootDef explorerRoot;
        try {
            explorerRoot = 
                (RadExplorerRootDef)getArte().getDefManager().getExplorerParagraphDef(explorerRootId);
        } catch (ClassCastException | DefinitionNotFoundError e) {
            throw EasFaults.newDefWithIdNotFoundFault("ExplorerRoot", "ListVisibleExpItemsRq", explorerRootId);
        }
        if (!explorerRoot.getCurUserCanAccess()) {
            throw EasFaults.newDefinitionAccessViolationFault(getArte(), 
                                                              Messages.MLS_ID_INSUF_PRIV_TO_ACCESS_EXPLORER_ROOT, 
                                                              "\"" + explorerRoot.getTitle(getArte()) + "\" (#" + explorerRootId + ")",
                                                              EDefType.PARAGRAPH,
                                                              new Id[]{explorerRootId});
        }
        final List<Id> accessibleExplItems = getAccessibleExplorerItems(explorerRoot);              
        for (Id explorerItemId: accessibleExplItems){
            xItems.addNewItem().setId(explorerItemId);
        }            
        return res;
    }

    @Override
    void prepare(final XmlObject rqXml) throws ServiceProcessServerFault, ServiceProcessClientFault {        
        prepare(((ListVisibleExplorerItemsMess) rqXml).getListVisibleExplorerItemsRq());
    }

    @Override
    XmlObject process(final XmlObject rq) throws ServiceProcessFault, InterruptedException {
        final ListVisibleExplorerItemsDocument doc = process((ListVisibleExplorerItemsMess) rq);
        postProcess(rq, doc.getListVisibleExplorerItems().getListVisibleExplorerItemsRs());
        return doc;
    }
}
