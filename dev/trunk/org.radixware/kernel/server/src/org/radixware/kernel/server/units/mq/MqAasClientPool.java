/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.units.mq;

import java.util.Objects;
import org.radixware.kernel.common.enums.EMqProcOrder;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.aio.AadcAffinity;
import org.radixware.kernel.server.sc.AasClientPool;
import org.radixware.kernel.server.sc.SingleSeanceAasClient;
import org.radixware.schemas.aasWsdl.InvokeDocument;
import org.radixware.schemas.messagequeue.MqProcessRqDocument;

/**
 *
 * @author dsafonov
 */
public class MqAasClientPool extends AasClientPool {

    private final MqUnit mqUnit;

    public MqAasClientPool(MqUnit mqUnit, LocalTracer tracer) {
        super(tracer);
        this.mqUnit = mqUnit;
    }

    public void invoke(final MqMessage msg, final MqProcessRqDocument xProcessRqDoc, final long processorId, final int timeout, final String scpName) {
        final InvokeDocument xInvokeDoc = InvokeDocument.Factory.newInstance();
        
        xInvokeDoc.addNewInvoke().addNewInvokeRq();
        xInvokeDoc.getInvoke().getInvokeRq().setClassId(Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"));
        xInvokeDoc.getInvoke().getInvokeRq().setPID(String.valueOf(processorId));
        xInvokeDoc.getInvoke().getInvokeRq().setMethodId("mthXRTT576Z7FA2NPFSNC2IHGK6GI");
        xInvokeDoc.getInvoke().getInvokeRq().addNewParameters().addNewItem().setXml(xProcessRqDoc);
        
        AadcAffinity affinity;
        if (mqUnit.getInstance().getAadcInstMemberId() == null || mqUnit.getOptions().procOrder == EMqProcOrder.ARBITRARY) {
            affinity = null;
        } else if (mqUnit.getOptions().procOrder == EMqProcOrder.SEQUENTIAL) {
            affinity = new AadcAffinity(0, 60000);
        } else {
            affinity = new AadcAffinity(Objects.hashCode(msg.getPartitionId()));
        }

        invoke(new MqAasInvokeItem(msg, xInvokeDoc, null, timeout, scpName, true, affinity));
    }    
    
    @Override
    protected SingleSeanceAasClient createAasClient() {
        return new MqAasClient(mqUnit);
    }
}
