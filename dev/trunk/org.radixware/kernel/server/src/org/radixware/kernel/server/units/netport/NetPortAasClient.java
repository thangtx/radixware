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

package org.radixware.kernel.server.units.netport;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.trace.LocalTracerProxy;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.units.AasClient;
import org.radixware.schemas.aas.InvokeRs;

/**
 * Client for transferring messages from network channels to AAS. There should
 * be at most one AAS seance at the given point of time. Parallelism is archived
 * by using a number of different clients. During request invocation given
 * NetPortAasClient become linked to caller channel and transfers all trace
 * messages to channel's trace.
 *
 */
public class NetPortAasClient extends AasClient {

    public static final int AAS_INVOKE_TIMEOUT_MILLIS = 24 * 60 * 60 * 1000; //1 day
    private NetPortInvokeQueueItem invokeItem;
    private long invokeMillis;

    public NetPortAasClient(NetPortHandlerUnit unit) {
        super(unit, unit.getManifestLoader(), new LocalTracerProxy(null, unit.createTracer()), unit.getDispatcher());
    }

    public void invoke(final NetPortInvokeQueueItem item) {
        invokeItem = item;
        ((LocalTracerProxy) tracer).setBackendTracer(item.seance.getAasClientTracer());
        invokeMillis = System.currentTimeMillis();
        ((NetPortHandlerUnit) unit).onAasRqStarted();
        invoke(invokeItem.invokeXml, invokeItem.invokeHeaders, invokeItem.keepConnect, AAS_INVOKE_TIMEOUT_MILLIS, unit.getScpName());
    }

    public boolean busy() {
        return !seances.isEmpty() && seances.get(0).busy() || invokeItem != null;
    }

    @Override
    protected void onInvokeResponse(InvokeRs rs) {
        if (invokeItem != null) {
            try {
                logResponse("response");
                invokeItem.seance.onAasInvokeResponse(rs);
            } finally {
                invokeItem = null;
            }
        } else {
            unit.getTrace().put(EEventSeverity.WARNING, "Unable to find handler for response: " + rs, EEventSource.NET_PORT_HANDLER);
        }
    }
    
    private void logResponse(String description) {
        final long aasWait = System.currentTimeMillis() - invokeMillis;
        final long totalWait = System.currentTimeMillis() - invokeItem.createTimeMillis;
        unit.getTrace().debug("Got " + description + " to item " + invokeItem.debugTitle + " , waited: " + aasWait + " ms, total: " + totalWait + " ms, queue: " + ((NetPortHandlerUnit) unit).getQueueSize(), EEventSource.NET_PORT_HANDLER, false);
        ((NetPortHandlerUnit) unit).appendAasWait(aasWait);
        ((NetPortHandlerUnit) unit).appendTotalWait(totalWait);
        ((NetPortHandlerUnit) unit).onAasRqFinished();
    }

    @Override
    protected void onInvokeException(ServiceClientException exception) {
        if (invokeItem != null) {
            try {
                logResponse("exception");
                invokeItem.seance.onAasInvokeException(exception);
            } finally {
                invokeItem = null;
            }
        } else {
            unit.getTrace().put(EEventSeverity.WARNING, "Unable to find handler for response: " + ExceptionTextFormatter.throwableToString(exception), EEventSource.NET_PORT_HANDLER);
        }
    }
}
