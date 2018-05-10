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

import java.io.IOException;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.exceptions.ServiceCallSendException;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.exceptions.ServiceConnectTimeout;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.SoapFormatter;
import org.radixware.kernel.server.sc.AasInvokeItem;
import org.radixware.kernel.server.sc.SingleSeanceAasClient;
import org.radixware.schemas.aas.InvokeRs;
import org.radixware.schemas.messagequeue.MqProcessRs;

/**
 *
 * @author dsafonov
 */
public class MqAasClient extends SingleSeanceAasClient<MqAasInvokeItem> {

    private final MqUnit mqUnit;

    public MqAasClient(MqUnit unit) {
        super(unit, unit.getUnitServiceManifestLoader(), unit.createTracer(), unit.getDispatcher());
        this.mqUnit = unit;
    }

    @Override
    protected void onInvokeResponseImpl(InvokeRs rs) {
        try {
            mqUnit.onResponse(getItem().getMqMessage(), (MqProcessRs) SoapFormatter.getInnerContent(rs.getReturnValue().getXml()), getLastInvokeDurationMillis());
        } catch (IOException ex) {
            tracer.put(EEventSeverity.ERROR, "Got unexpected empty response from AAS: " + ExceptionTextFormatter.throwableToString(ex), null, null, false);
        }
    }

    @Override
    protected void onInvokeExceptionImpl(ServiceClientException exception) {
        if (exception instanceof ServiceCallSendException || exception instanceof ServiceConnectTimeout) {
            mqUnit.onFailToSendAasRequest(getItem().getMqMessage(), exception);
        } else {
            mqUnit.onAasException(getItem().getMqMessage(), exception, getLastInvokeDurationMillis());
        }
    }
}
