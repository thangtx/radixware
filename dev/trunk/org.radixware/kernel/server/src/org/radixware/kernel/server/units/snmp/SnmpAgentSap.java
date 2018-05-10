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

package org.radixware.kernel.server.units.snmp;

import org.radixware.kernel.common.utils.HttpFormatter;
import org.radixware.kernel.server.aio.ServiceServer;
import org.radixware.kernel.server.sap.Sap;
import org.radixware.schemas.snmpagent.NotifyMess;
import org.radixware.schemas.snmpagentWsdl.NotifyDocument;


public final class SnmpAgentSap extends Sap {

    public static final String SERVICE_WSDL = "http://schemas.radixware.org/snmpagent.wsdl";
    private final SnmpAgentUnit unit;

    public SnmpAgentSap(final SnmpAgentUnit unit) {
        super(
                unit.getDispatcher(),
                unit.createTracer(),
                3, //maxSeanceCount
                10, //rqWaitTimeout
                unit.getResourceKeyPrefix() + "/sausap"
                );
        this.unit = unit;
    }

    @Override
    protected void process(final ServiceServer.InvocationEvent event) {
        try {
            if (event.rqEnvBodyContent instanceof NotifyMess) {
                unit.getAgent().sendNotifications(((NotifyMess) event.rqEnvBodyContent).getNotifyRq());
                NotifyDocument rsDoc = NotifyDocument.Factory.newInstance();
                rsDoc.addNewNotify().addNewNotifyRs();
                event.seance.response(rsDoc, HttpFormatter.getKeepConnectionAlive(event.rqFrameAttrs));
            }
        } catch (Throwable e) {
            logErrorInService(e, SERVICE_WSDL);
            event.seance.response(throwableToFault(e), false, null);
        }
    }

    @Override
    protected boolean isShuttingDown() {
        return unit.isShuttingDown();
    }

    @Override
    protected void restoreDbConnection() throws InterruptedException {
        unit.restoreDbConnection();
    }

    @Override
    public long getId() {
        return unit.getOptions().sapId;
    }
}
