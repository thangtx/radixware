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

package org.radixware.kernel.server.units;

import java.util.Map;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.server.aio.Event;
import org.radixware.kernel.server.aio.EventDispatcher;
import org.radixware.kernel.server.aio.EventHandler;
import org.radixware.kernel.server.aio.AsyncServiceClient;
import org.radixware.kernel.server.aio.ServiceClientSeance;
import org.radixware.schemas.aas.InvokeMess;
import org.radixware.schemas.aas.InvokeRs;
import org.radixware.schemas.aasWsdl.InvokeDocument;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.server.aio.ServiceManifestLoader;
import org.radixware.kernel.server.arte.services.aas.ArteAccessService;


public abstract class AasClient extends AsyncServiceClient implements EventHandler {

    private static final Long THIS_SYSTEM_ID = Long.valueOf(1);
    protected Unit unit;
    private boolean isStopping = false;

    public AasClient(final Unit unit, final ServiceManifestLoader manifestLoader, final LocalTracer tracer, final EventDispatcher dispatcher) {
        super(
                dispatcher,
                tracer,
                manifestLoader,
                THIS_SYSTEM_ID,
                Long.valueOf(unit.getInstance().getId()),
                ArteAccessService.SERVICE_WSDL,
                unit.getScpName(),
                null//myAddress
                );
        this.unit = unit;
    }

    public void stop() {
        isStopping = true;
        closeActiveSeances();
        unit = null;
    }

    protected boolean isStopped() {
        return unit == null;
    }

    protected void invoke(final InvokeDocument invokeXml, final boolean keepConnect, final int aasInvokeTimeoutMillis) {
        invoke(invokeXml, keepConnect, aasInvokeTimeoutMillis, unit.getScpName());
    }
    
    protected void invoke(final InvokeDocument invokeXml, final boolean keepConnect, final int aasInvokeTimeoutMillis, final String scpName) {
        invoke(invokeXml, null, keepConnect, aasInvokeTimeoutMillis, scpName);
    }

    protected void invoke(final InvokeDocument invokeXml, final Map<String, String> headers, final boolean keepConnect, final int aasInvokeTimeoutMillis, final String scpName) {
        if (isStopped()) {
            throw new IllegalUsageError("AAS client is stopped");
        }
        if (invokeXml.getInvoke() == null || invokeXml.getInvoke().getInvokeRq() == null) {
            throw new WrongFormatError("Wrong format of AAS invokeXml: invokeRq is null", null);
        }
        setScpName(scpName);
        invoke(invokeXml, InvokeMess.class, headers, keepConnect, aasInvokeTimeoutMillis, this);
    }

    @Override
    public void onEvent(final Event ev) {
        if (ev instanceof ServiceClientSeance.ResponseEvent) {
            if (isStopping) {
                return;
            }
            final ServiceClientSeance.ResponseEvent event = (ServiceClientSeance.ResponseEvent) ev;
            if (event.exception != null) {
                onInvokeException(event.exception);
            } else {
                onInvokeResponse(((InvokeMess) event.response).getInvokeRs());
            }
        } else {
            throw new IllegalUsageError("Invalid event " + ev);
        }
    }

    protected abstract void onInvokeResponse(InvokeRs rs);

    protected abstract void onInvokeException(ServiceClientException exception);
}
