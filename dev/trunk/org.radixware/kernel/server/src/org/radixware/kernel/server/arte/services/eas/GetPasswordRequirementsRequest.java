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
import org.radixware.schemas.eas.GetPasswordRequirementsMess;
import org.radixware.schemas.eas.GetPasswordRequirementsRs;
import org.radixware.schemas.easWsdl.GetPasswordRequirementsDocument;

final class GetPasswordRequirementsRequest extends SessionRequest {

    GetPasswordRequirementsRequest(final ExplorerAccessService presenter) {
        super(presenter);
    }

    public final GetPasswordRequirementsDocument process(final GetPasswordRequirementsMess request) throws ServiceProcessClientFault, ServiceProcessServerFault, InterruptedException {
        getArte().switchToReadonlyTransaction();//it's readonly request        
        final GetPasswordRequirementsDocument res = GetPasswordRequirementsDocument.Factory.newInstance();
        final GetPasswordRequirementsRs rsStruct = res.addNewGetPasswordRequirements().addNewGetPasswordRequirementsRs();
        ExplorerAccessService.writePasswordRequirements(getArte(), rsStruct.addNewRequirements());
        return res;
    }

    @Override
    void prepare(final XmlObject rqXml) throws ServiceProcessServerFault, ServiceProcessClientFault {
        prepare(((GetPasswordRequirementsMess) rqXml).getGetPasswordRequirementsRq());
    }

    @Override
    protected String getUsrDbTraceProfile() {
        return null;
    }

    @Override
    protected boolean getUsrActionsIsTraced() {
        return false;
    }

    @Override
    XmlObject process(final XmlObject rq) throws ServiceProcessFault, InterruptedException {
        final GetPasswordRequirementsDocument doc = process((GetPasswordRequirementsMess) rq);
        postProcess(rq, doc.getGetPasswordRequirements().getGetPasswordRequirementsRs());
        return doc;
    }    
}
