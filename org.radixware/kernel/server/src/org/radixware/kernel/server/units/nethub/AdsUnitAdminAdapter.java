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
import org.radixware.kernel.common.exceptions.ServiceCallException;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceCallTimeout;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.schemas.aas.InvokeRs;
import org.radixware.schemas.aasWsdl.InvokeDocument;


final class AdsUnitAdminAdapter extends org.radixware.kernel.server.units.AasClient {

    private static final String COMP_NAME = "Service Client";

    final static class InvalidMessageType {

        final static long UNCORRELATED = 1;
        final static long DUPLICATE = 2;
    }
    private final NetHubUnit nhUnit;
    private static final int MAX_TRY_COUNT = 10;
    private static final String ON_INACTIVITY_TIMER_MH_ID = "mth4UZXEJLOWBHSHPVDBJ52MD35DA";

    final void onInactivityTimer() {
        nhUnit.traceDebug("Calling onInactivityTimer method", COMP_NAME, false);
        invoke(nhUnit.prepareDacUnitAdapterInvoke(ON_INACTIVITY_TIMER_MH_ID));
    }
    ;    
    
    private static final String ON_CONNECT_MH_ID = "mth5POAOKEZZZBZVDZITYHWB2RPDI";

    final void onConnect() {
        nhUnit.traceDebug("Calling onConnect method", COMP_NAME, false);
        invoke(nhUnit.prepareDacUnitAdapterInvoke(ON_CONNECT_MH_ID));
    }
    ;
    private static final String ON_DISCONNECT_MH_ID = "mthCKEBJQQXHFCLRKM2UQOS3NHNII";

    final void onDisconnect() {
        nhUnit.traceDebug("Disconnect event processing...", COMP_NAME, false);
        //drop all, notify on disconnect
        closeActiveSeances();
        queue.clear();

        nhUnit.traceDebug("Calling onDisconnect method", COMP_NAME, false);
        invoke(nhUnit.prepareDacUnitAdapterInvoke(ON_DISCONNECT_MH_ID));
    }
    ;

    private static final String BEFORE_START_MH_ID = "mthLMYZXCJVQFCDROJZUSHFGIDRWQ";

    final void beforeStart() {
        nhUnit.traceDebug("Calling beforeStart method", COMP_NAME, false);
        invoke(nhUnit.prepareDacUnitAdapterInvoke(BEFORE_START_MH_ID));
    }
    ;

    private static final String BEFORE_STOP_MH_ID = "mth3AO5UTYDKRGRHHNN2XUZLIGXKY";

    final void beforeStop() {
        nhUnit.traceDebug("Calling beforeStop method", COMP_NAME, false);
        invoke(nhUnit.prepareDacUnitAdapterInvoke(BEFORE_STOP_MH_ID));
    }

    AdsUnitAdminAdapter(final NetHubUnit unit) throws SQLException {
        super(unit, unit.getManifestLoader(), unit.getTrace().newTracer(unit.getEventSource(), COMP_NAME), unit.getDispatcher());
        this.nhUnit = unit;
        queue = new LinkedList<InvokeDocument>();
    }
    final List<InvokeDocument> queue;

    private void invoke(final InvokeDocument invXml) {
        queue.add(invXml);
        if (queue.size() == 1) {
            invoke();
        }
    }
    private int tryCount = 0;

    private void invoke() {
        tryCount = 0;
        super.invoke(queue.get(0), true, Integer.MAX_VALUE);
    }

    private String getMethodNameFromQueue() {
        String methodName = "unknown";
        if (!queue.isEmpty()) {
            final String methodId = queue.get(0).getInvoke().getInvokeRq().getMethodId();
            if (ON_INACTIVITY_TIMER_MH_ID.equals(methodId)) {
                methodName = "onInactivityTimer";
            } else if (ON_CONNECT_MH_ID.equals(methodId)) {
                methodName = "onConnect";
            } else if (ON_DISCONNECT_MH_ID.equals(methodId)) {
                methodName = "onDisconnect";
            } else if (BEFORE_START_MH_ID.equals(methodId)) {
                methodName = "beforeStart";
            } else if (BEFORE_STOP_MH_ID.equals(methodId)) {
                methodName = "beforeStop";
            } else {
                methodName += " " + methodId;
            }
        }

        return methodName;
    }

    @Override
    protected void onInvokeResponse(final InvokeRs rs) {
        nhUnit.traceDebug("Response from " + getMethodNameFromQueue() + " method received", COMP_NAME, false);
        nhUnit.traceDebug("Response received value: " + rs.xmlText(), COMP_NAME, true);

        final InvokeDocument invokeXml = queue.get(0);
        queue.remove(0);

        if (nhUnit.getAdsUnitState() == AdsUnitState.BEFORE_STOP_CALLED) {
            nhUnit.onBeforeStopProcessed();
        } else if (nhUnit.getAdsUnitState() == AdsUnitState.BEFORE_STOP_PROCESSED) {
            // it can be response for onDisconnect only, now we can go on stopping
            //netUnit.doRequestShutdown();
            nhUnit.onOnDiconnectDuringStopProcessed();
        } else if (nhUnit.getAdsUnitState() == AdsUnitState.BEFORE_START_CALLED) {
            nhUnit.onBeforeStartProcessed();
        } else {
            if (rs.isSetReturnValue() && rs.getReturnValue().isSetBin()) {
                nhUnit.sendToExtPort(rs.getReturnValue().getBin(), "Response");
            }
            if (!queue.isEmpty()) {
                invoke();
            }
        }
    }

    @Override
    protected void onInvokeException(final ServiceClientException exception) {
        final String mess;
        if (exception instanceof ServiceCallFault) {
            final ServiceCallFault fault = ((ServiceCallFault) exception);
            mess = fault.getCauseExMessage() + "\nStack:\n" + fault.getCauseExStack();
        } else if (exception instanceof ServiceCallTimeout || exception instanceof ServiceCallException) {
            mess = exception.getMessage();
        } else {
            mess = ExceptionTextFormatter.exceptionStackToString(exception);
        }
        final String methodName = getMethodNameFromQueue();
        nhUnit.traceError("Exception during calling " + methodName + " method received: " + mess, COMP_NAME);

        if (++tryCount <= MAX_TRY_COUNT) {
            nhUnit.traceDebug("Recall " + methodName + " method", COMP_NAME, false);
            super.invoke(queue.get(0), false, NetHubUnit.DEFAULT_AAS_CALL_TIMEOUT_MILLIS);
            return;
        }
        queue.remove(0);

        if (//if stop is already started let's finish
                nhUnit.getAdsUnitState() == AdsUnitState.BEFORE_STOP_REQUESTED
                || nhUnit.getAdsUnitState() == AdsUnitState.BEFORE_STOP_CALLED
                || nhUnit.getAdsUnitState() == AdsUnitState.BEFORE_STOP_PROCESSED) {
            nhUnit.requestShutdownByAdmin();
        } else {
            if (!isStopped()) {
                final String reason =  "Can't complete " + methodName + " method";
                nhUnit.traceDebug(reason + ". Shedule unit restart.", COMP_NAME, false);
                nhUnit.requestStopAndPostponeRestartFromAdminAdapter(reason);
            }
        }
    }
}
