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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.exceptions.ServiceCallException;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceCallTimeout;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Bin;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.schemas.aas.InvokeRs;
import org.radixware.schemas.aasWsdl.InvokeDocument;
import org.radixware.schemas.nethub.OnRecvRqDocument;
import org.radixware.schemas.nethub.OnRecvRqDocument.OnRecvRq;
import org.radixware.schemas.nethub.OnRecvRqDocument.OnRecvRq.RqMess;
import org.radixware.schemas.nethub.OnRecvRsDocument.OnRecvRs;
import org.radixware.kernel.server.arte.services.aas.AasValueConverter;
import org.radixware.kernel.common.utils.Maps;


final class AdsUnitRecvAdapter extends org.radixware.kernel.server.units.AasClient {

    private final NetHubUnit nhUnit;
    private final static String COMP_NAME = "Service Client";
    private final List<InvokeDocument> queue = new LinkedList<InvokeDocument>();

    AdsUnitRecvAdapter(final NetHubUnit unit) throws SQLException {
        super(unit, unit.getManifestLoader(), unit.getTrace().newTracer(unit.getEventSource(), COMP_NAME), unit.getDispatcher());
        this.nhUnit = unit;
    }
    
    private static final String ON_AFTER_RECV_MH_ID = "mth3J6OGUTWVVAQ7JHAKHPDINN4IQ";

    final void onAfterRecv(final byte[] mess, final Map<Bin, Map<String, String>> requests) {
        final InvokeDocument invokeXml = nhUnit.prepareDacUnitAdapterInvoke(ON_AFTER_RECV_MH_ID);
        final OnRecvRqDocument xDoc = OnRecvRqDocument.Factory.newInstance();
        final OnRecvRq xRq = xDoc.addNewOnRecvRq();
        xRq.setMess(mess);
        for (Map.Entry<Bin, Map<String, String>> e: requests.entrySet()) {
            final RqMess xMess = xRq.addNewRqMess();
            xMess.setMess(e.getKey().get());
            xMess.setParsingVars(Maps.toXml(e.getValue()));
        }
        invokeXml.getInvoke().getInvokeRq().addNewParameters().addNewItem().setXml(xDoc);
        nhUnit.traceDebug("Calling onAfterRecv method", COMP_NAME, false);
        invoke(invokeXml);
    };    
    
    private void invoke(final InvokeDocument invXml) {
        queue.add(invXml);
        if (queue.size() == 1) {
            invoke();
        }
    }
    
    private void invoke() {
        super.invoke(queue.get(0), true, Integer.MAX_VALUE);
    }
    
    @Override
    protected void onInvokeResponse(final InvokeRs rs) {
        nhUnit.traceDebug("Response from onAfterRecv method received", COMP_NAME, false);
        nhUnit.traceDebug("Response received value: " + rs.xmlText(), COMP_NAME, true);
        
        final InvokeDocument invokeXml = queue.get(0);        
        queue.remove(0);
        
        final OnRecvRq rq = (OnRecvRq)AasValueConverter.aasXmlVal2ObjVal(null, invokeXml.getInvoke().getInvokeRq().getParameters().getItemList().get(0));
        nhUnit.onAfterRecvProcessed(rq.getMess(), (OnRecvRs) AasValueConverter.aasXmlVal2ObjVal(null, rs.getReturnValue()));
        
        if (!queue.isEmpty()) {
            invoke();
        }
    }

    @Override
    protected void onInvokeException(final ServiceClientException exception) {
        final String mess;
        if (exception instanceof ServiceCallFault) {
            final ServiceCallFault fault = ((ServiceCallFault)exception);
            mess = fault.getCauseExMessage() + "\nStack:\n" + fault.getCauseExStack();
        } else if (exception instanceof ServiceCallTimeout || exception instanceof ServiceCallException)
            mess = exception.getMessage();
        else
            mess = ExceptionTextFormatter.exceptionStackToString(exception);
        nhUnit.traceError("Exception during calling onAfterRecv method received: " + mess, COMP_NAME);
        
        final InvokeDocument invokeXml = queue.get(0);
        queue.remove(0);
        
        if (exception instanceof ServiceCallFault) {
            final OnRecvRq rq = (OnRecvRq)AasValueConverter.aasXmlVal2ObjVal(null, invokeXml.getInvoke().getInvokeRq().getParameters().getItemList().get(0));
            nhUnit.getAasClientPool().onInvalidMessage(rq.getMess(), AdsUnitAdminAdapter.InvalidMessageType.UNCORRELATED);
        }
        
        if (!queue.isEmpty()) {
            invoke();
        }        
    }
}
