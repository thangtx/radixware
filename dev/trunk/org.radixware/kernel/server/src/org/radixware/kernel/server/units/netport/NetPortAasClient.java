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
package org.radixware.kernel.server.units.netport;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.trace.LocalTracerProxy;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.sc.SingleSeanceAasClient;
import org.radixware.schemas.aas.InvokeRs;

/**
 * Client for transferring messages from network channels to AAS. There should
 * be at most one AAS seance at the given point of time. Parallelism is archived
 * by using a number of different clients. During request invocation given
 * NetPortAasClient become linked to caller channel and transfers all trace
 * messages to channel's trace.
 *
 */
public class NetPortAasClient extends SingleSeanceAasClient<NetPortInvokeQueueItem> {

    public static final int AAS_INVOKE_TIMEOUT_MILLIS = 24 * 60 * 60 * 1000; //1 day

    public NetPortAasClient(NetPortHandlerUnit unit) {
        super(unit, unit.getManifestLoader(), new LocalTracerProxy(null, unit.createTracer()), unit.getDispatcher());
    }

    @Override
    public void invoke(final NetPortInvokeQueueItem item) {
        ((LocalTracerProxy) tracer).setBackendTracer(item.source.getTracer());
        ((NetPortHandlerUnit) unit).onAasRqStarted();
        
        final NetPortHandlerUnit nphUnit = (NetPortHandlerUnit)unit;
        final long waited = System.currentTimeMillis() - item.createTimeMillis;
        nphUnit.getNetPortHandlerMonitor().aasRequestStarted(waited);
        
        super.invoke(item);
    }

    @Override
    protected void onInvokeResponseImpl(InvokeRs rs) {
        if (getItem() != null) {
            logResponse(getItem(), "response");
            getItem().source.onAasInvokeResponse(rs);
        } else {
            unit.getTrace().put(EEventSeverity.WARNING, "Unable to find handler for response: " + rs, EEventSource.NET_PORT_HANDLER);
        }
    }

    private void logResponse(final NetPortInvokeQueueItem item, String description) {
        final NetPortHandlerUnit nphUnit = (NetPortHandlerUnit) unit;
        final int queueSize = nphUnit.getQueueSize();
        final long pureWait = getLastInvokeDurationMillis();
        final long totalWait = System.currentTimeMillis() - item.createTimeMillis;
        nphUnit.getNetPortHandlerMonitor().aasResponseReceived(pureWait, totalWait);
        unit.getTrace().debug("Got " + description + " to item " + item.debugTitle + " , waited: " + pureWait + " ms, total: " + totalWait + " ms, queue: " + queueSize, EEventSource.NET_PORT_HANDLER_QUEUE, false);
        nphUnit.appendAasWait(pureWait);
        nphUnit.appendTotalWait(totalWait);
        nphUnit.onAasRqFinished();
    }

    @Override
    protected void onInvokeExceptionImpl(ServiceClientException exception) {
        if (getItem() != null) {
            logResponse(getItem(), "exception");
            getItem().source.onAasInvokeException(exception);
        } else {
            unit.getTrace().put(EEventSeverity.WARNING, "Unable to find handler for response: " + ExceptionTextFormatter.throwableToString(exception), EEventSource.NET_PORT_HANDLER);
        }
    }
}
