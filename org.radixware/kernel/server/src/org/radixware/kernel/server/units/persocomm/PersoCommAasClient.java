/*
 * Copyright (c) 2008-2018, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.units.persocomm;

import org.radixware.kernel.common.enums.EPersoCommDeliveryStatus;
import org.radixware.kernel.common.exceptions.ServiceCallException;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceCallTimeout;
import org.radixware.schemas.aas.InvokeRq;
import org.radixware.schemas.aasWsdl.InvokeDocument;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.aio.ServiceManifestLoader;
import org.radixware.kernel.server.arte.services.aas.ArteAccessService;
import org.radixware.kernel.server.sc.UnitServicesClient;
import org.radixware.schemas.aas.Value;
import org.radixware.schemas.personalcommunications.MessageDocument;

/**
 * <p>
 * This class used to notify about sending/receiving messages</p>
 */
public final class PersoCommAasClient extends UnitServicesClient {

    private static final int KEEP_CONNECT_TIME_SEC = 4;
    private static final int INVOKE_TIMEOUT_SEC = 30;
    //
    private static final Id UNIT_ENTITY_ID = Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E");
    private static final String SEND_MTH = "mthNMJCQXCMOZFQ3PDBQVZF6KD3LQ";
    //
    private static final Id OUT_MSG_ENTITY_ID = Id.Factory.loadFrom("tblGBOAYN4PUTOBDANVABIFNQAABA");
    private static final Id SENT_MSG_ENTITY_ID = Id.Factory.loadFrom("tblOA4BGTURUTOBDANVABIFNQAABA");
    private static final Id OUT_MSG_CLASS_ID = Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA");
    private static final String AFTER_SEND_MTH = "mthYMJXBQNCLRBIVONT32C7ZAEZAY"; 
    private static final String AFTER_DELIVERY_MTH = "mthEOLVVH65ENDSPBBYY3XAOBB2OA";
    private static final String FIND_CHANNEL_MTH = "mthVOAEMIIKPJA7ZLAX5DR7DSCNLM";
    private final PersoCommUnit pcUnit;

    public PersoCommAasClient(final PersoCommUnit unit, final ServiceManifestLoader manifestLoader) {
        super(unit, manifestLoader);
        pcUnit = unit;
    }

    
    /**
     * <p>
     * Call notification after receiving message</p>
     *
     * @param xDoc received message
     * @throws ServiceCallException
     * @throws ServiceCallTimeout
     * @throws ServiceCallFault
     * @throws InterruptedException
     */
    public void invokeAfterRecv(final MessageDocument xDoc) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        final InvokeDocument invokeXml = InvokeDocument.Factory.newInstance();
        final InvokeRq invokeRq = invokeXml.addNewInvoke().addNewInvokeRq();
        invokeRq.addNewParameters().addNewItem().setXml(xDoc);
        invokeRq.setEntityId(Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"));
        invokeRq.setMethodId("mthEMLY6CZEXBCT3EW5LRCIVNEAUM");
        invokeRq.setPID(String.valueOf(pcUnit.getPrimaryUnitId()));
        doInvoke(invokeXml);
    }

    /**
     * <p>
     * Trigger sending of messages in ARTE</p>
     *
     * @throws ServiceCallException
     * @throws ServiceCallTimeout
     * @throws ServiceCallFault
     * @throws InterruptedException
     */
    public void invokeSend() throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        final String pid = String.valueOf(pcUnit.getPrimaryUnitId());
        final InvokeDocument invokeXml = InvokeDocument.Factory.newInstance();
        final InvokeRq invokeRq = invokeXml.addNewInvoke().addNewInvokeRq();
        invokeRq.setEntityId(UNIT_ENTITY_ID);
        invokeRq.setPID(String.valueOf(pid));
        invokeRq.setMethodId(SEND_MTH);
        doInvoke(invokeXml);
    }

    /**
     * <p>
     * Call notification after send message</p>
     *
     * @param id message id was sent
     * @param error error message about sending. If null or empty - sending was
     * completed successfully
     * @throws ServiceCallException
     * @throws ServiceCallTimeout
     * @throws ServiceCallFault
     * @throws InterruptedException
     */
    public void invokeAfterSend(final Long id, final String error) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        final String pid = String.valueOf(id);
        final InvokeDocument invokeXml = InvokeDocument.Factory.newInstance();
        final InvokeRq invokeRq = invokeXml.addNewInvoke().addNewInvokeRq();
        invokeRq.setEntityId(SENT_MSG_ENTITY_ID);
        invokeRq.setPID(pid);
        final InvokeRq.Parameters parameters = invokeRq.addNewParameters();
        final Value value = parameters.addNewItem();
        value.setStr(error);
        invokeRq.setMethodId(AFTER_SEND_MTH);
        doInvoke(invokeXml);
    }
    
    public void invokeAfterDelivery(long messageId, EPersoCommDeliveryStatus status) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        final InvokeDocument invokeXml = InvokeDocument.Factory.newInstance();
        final InvokeRq invokeRq = invokeXml.addNewInvoke().addNewInvokeRq();
        invokeRq.setEntityId(SENT_MSG_ENTITY_ID);
        invokeRq.setPID(String.valueOf(messageId));
        final InvokeRq.Parameters parameters = invokeRq.addNewParameters();
        final Value value = parameters.addNewItem();
        value.setStr(status.getValue());
        invokeRq.setMethodId(AFTER_DELIVERY_MTH);
        doInvoke(invokeXml);
    }

    public Long invokeFindChannel(final long id, Long prevUnitId, Long prevPriority) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        final InvokeDocument invokeXml = InvokeDocument.Factory.newInstance();
        final InvokeRq invokeRq = invokeXml.addNewInvoke().addNewInvokeRq();
        invokeRq.setClassId(OUT_MSG_CLASS_ID);
        invokeRq.setMethodId(FIND_CHANNEL_MTH);
        final InvokeRq.Parameters parameters = invokeRq.addNewParameters();
        final Value value = parameters.addNewItem();
        value.ensureRef().setEntityId(OUT_MSG_ENTITY_ID.toString());
        value.ensureRef().setPID(String.valueOf(id));
        if (prevUnitId != null) {
            parameters.addNewItem().setInt(prevUnitId);
        } else {
            parameters.addNewItem().setNil();
        }
        if (prevPriority != null) {
            parameters.addNewItem().setInt(prevPriority);
        } else {
            parameters.addNewItem().setNil();
        }
        return doInvoke(invokeXml).getInvoke().getInvokeRs().getReturnValue().getInt();
    }

    private InvokeDocument doInvoke(final InvokeDocument xRq) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        setScpName(unit.getScpName());
        return (InvokeDocument) invokeService(xRq, InvokeDocument.class, 1l, (long) unit.getInstance().getId(), ArteAccessService.SERVICE_WSDL, KEEP_CONNECT_TIME_SEC, INVOKE_TIMEOUT_SEC, null);
    }
}
