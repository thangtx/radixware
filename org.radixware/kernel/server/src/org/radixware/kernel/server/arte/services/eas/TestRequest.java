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
import org.radixware.schemas.eas.TestMess;
import org.radixware.schemas.eas.TestRs;
import org.radixware.schemas.easWsdl.TestDocument;

final class TestRequest extends SessionRequest {

    TestRequest(final ExplorerAccessService presenter) {
        super(presenter);
    }

    public final TestDocument process(final TestMess request) throws ServiceProcessClientFault, ServiceProcessServerFault {
        getArte().switchToReadonlyTransaction();//it's readonly request
        final long sessionId = request.getTestRq().getSessionId();
        final TestDocument res = TestDocument.Factory.newInstance();
        final TestRs rsStruct = res.addNewTest().addNewTestRs();
        rsStruct.setChallenge(presenter.getChallenge(request, sessionId, true, null));
        return res;
    }

    @Override
    void prepare(final XmlObject rqXml) throws ServiceProcessServerFault, ServiceProcessClientFault {
        return;
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
    XmlObject process(final XmlObject rq) throws ServiceProcessFault {
        return process((TestMess) rq);
    }
}