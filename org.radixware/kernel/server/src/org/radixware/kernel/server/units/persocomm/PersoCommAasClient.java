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
package org.radixware.kernel.server.units.persocomm;

import org.radixware.kernel.common.exceptions.ServiceCallException;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceCallTimeout;
import org.radixware.schemas.aas.InvokeRq;
import org.radixware.schemas.aasWsdl.InvokeDocument;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.aio.ServiceManifestLoader;
import org.radixware.kernel.server.arte.services.aas.ArteAccessService;
import org.radixware.kernel.server.sc.UnitServicesClient;
import org.radixware.kernel.server.units.persocomm.interfaces.IAASNotificationCallback;
import org.radixware.schemas.aas.Value;

final class PersoCommAasClient extends UnitServicesClient implements IAASNotificationCallback {

    private static final int KEEP_CONNECT_TIME_SEC = 4;
    private static final int INVOKE_TIMEOUT_SEC = 30;
    //
    final private static Id UNIT_ENTITY_ID = Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E");
    final private static String SEND_MTH = "mthNMJCQXCMOZFQ3PDBQVZF6KD3LQ";
    //
    final private static Id OUT_MSG_ENTITY_ID = Id.Factory.loadFrom("tblGBOAYN4PUTOBDANVABIFNQAABA");
    final private static String AFTER_SEND_MTH = "mthLSF5WNNQQNFRJKWZG3TIS5UE7Y";

    public PersoCommAasClient(final PCUnit unit, final ServiceManifestLoader manifestLoader) {
        super(unit, manifestLoader);
    }

    public PersoCommAasClient(final NewPCUnit unit, final ServiceManifestLoader manifestLoader) {
        super(unit, manifestLoader);
    }

    @Override
    public void invokeAfterRecv(final Long id) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        final String pid = String.valueOf(id);
        final InvokeDocument invokeXml = InvokeDocument.Factory.newInstance();
        final InvokeRq invokeRq = invokeXml.addNewInvoke().addNewInvokeRq();
        invokeRq.setEntityId(Id.Factory.loadFrom("tblLXLOGLESUTOBDANVABIFNQAABA"));
        invokeRq.setMethodId("mth7WLCDJCSB5AQROIIEBZEYLSVK4");
        invokeRq.setPID(pid);
        doInvoke(invokeXml);
    }

    @Override
    public void invokeSend() throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        final String pid = String.valueOf(unit.getId());
        final InvokeDocument invokeXml = InvokeDocument.Factory.newInstance();
        final InvokeRq invokeRq = invokeXml.addNewInvoke().addNewInvokeRq();
        invokeRq.setEntityId(UNIT_ENTITY_ID);
        invokeRq.setPID(String.valueOf(pid));
        invokeRq.setMethodId(SEND_MTH);
        doInvoke(invokeXml);
    }

    @Override
    public void invokeAfterSend(final Long id, final String error) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        final String pid = String.valueOf(id);
        final InvokeDocument invokeXml = InvokeDocument.Factory.newInstance();
        final InvokeRq invokeRq = invokeXml.addNewInvoke().addNewInvokeRq();
        invokeRq.setEntityId(OUT_MSG_ENTITY_ID);
        invokeRq.setPID(pid);
        final InvokeRq.Parameters parameters = invokeRq.addNewParameters();
        final Value value = parameters.addNewItem();
        value.setStr(error);
        invokeRq.setMethodId(AFTER_SEND_MTH);
        doInvoke(invokeXml);
    }

    private void doInvoke(final InvokeDocument xRq) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        setScpName(unit.getScpName());
        invokeService(xRq, InvokeDocument.class, 1l, (long) unit.getInstance().getId(), ArteAccessService.SERVICE_WSDL, KEEP_CONNECT_TIME_SEC, INVOKE_TIMEOUT_SEC);
    }
}
