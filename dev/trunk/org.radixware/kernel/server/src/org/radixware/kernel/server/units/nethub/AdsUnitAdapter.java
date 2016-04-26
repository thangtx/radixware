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

package org.radixware.kernel.server.units.nethub;

import java.sql.SQLException;
import org.radixware.kernel.common.exceptions.ServiceCallException;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceCallTimeout;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.schemas.aas.InvokeRs;
import org.radixware.schemas.aasWsdl.InvokeDocument;


final class AdsUnitAdapter extends org.radixware.kernel.server.units.AasClient {

    private NetHubUnit nhUnit;
    private static final String COMP_NAME = "Service Client";
    private String curUniqueKey = null;
    private String mthId = null;

    AdsUnitAdapter(final NetHubUnit unit) throws SQLException {
        super(unit, unit.getManifestLoader(), unit.getTrace().newTracer(unit.getEventSource(), COMP_NAME), unit.getDispatcher());
        this.nhUnit = unit;
    }

    private static final String ON_RQ_MH_ID = "mthJPQKNSOSMNDQDDXP63J7HRWGXE";

    final void onRequest(final byte[] mess, final String uniqueKey) {
        mthId = ON_RQ_MH_ID;
        curUniqueKey = uniqueKey;
        nhUnit.traceDebug("Calling onRequest method" + (uniqueKey != null ? ": KEY=" + uniqueKey : ""), COMP_NAME, false);
        invoke(nhUnit.prepareDacUnitAdapterInvokeWithMessParam(ON_RQ_MH_ID, mess), true, NetHubUnit.DEFAULT_AAS_CALL_TIMEOUT_MILLIS);
    }
    
    private static final String ON_INV_MESS_MH_ID = "mth7NQSQ3LO4RC6XKR4PA3J56MUXQ";

    final void onInvalidMessage(final byte[] mess, final long type) {
        mthId = ON_INV_MESS_MH_ID;
        final InvokeDocument invokeXml = nhUnit.prepareDacUnitAdapterInvokeWithMessParam(ON_INV_MESS_MH_ID, mess);
        invokeXml.getInvoke().getInvokeRq().getParameters().addNewItem().setInt(type);
        nhUnit.traceDebug("Calling onInvalidMessage method", COMP_NAME, false);
        invoke(invokeXml, true, NetHubUnit.DEFAULT_AAS_CALL_TIMEOUT_MILLIS);
    };    
    
    private String getMethodName() {
        String methodName = "unknown";
        if (ON_RQ_MH_ID.equals(mthId))
            methodName = "onRequest";
        else if (ON_INV_MESS_MH_ID.equals(mthId))
            methodName = "onInvalidMessage";
        return methodName;
    }
    
    @Override
    protected void onInvokeResponse(final InvokeRs rs) {
        nhUnit.traceDebug("Response from " + getMethodName() + " method received" + (curUniqueKey != null ? ": KEY=" + curUniqueKey : ""), COMP_NAME, false);
        nhUnit.traceDebug("Response received value: " + rs.xmlText(), COMP_NAME, true);
        nhUnit.getAasClientPool().freeClient(this);
        if (rs.isSetReturnValue() && rs.getReturnValue().isSetBin()) {
            nhUnit.processExtRs(rs.getReturnValue().getBin());
        }
    }

    @Override
    protected void onInvokeException(final ServiceClientException exception) {
        nhUnit.getAasClientPool().freeClient(this);
        final String mess;
        if (exception instanceof ServiceCallFault) {
            final ServiceCallFault fault = ((ServiceCallFault)exception);
            mess = fault.getCauseExMessage() + "\nStack:\n" + fault.getCauseExStack();
        } else if (exception instanceof ServiceCallTimeout || exception instanceof ServiceCallException)
            mess = exception.getMessage();
        else
            mess = ExceptionTextFormatter.exceptionStackToString(exception);
        nhUnit.traceError("Exception during calling " + getMethodName() + " method " + (curUniqueKey != null ? "KEY=" + curUniqueKey + ") " : "") + "received: " + mess, COMP_NAME);
    }
}
