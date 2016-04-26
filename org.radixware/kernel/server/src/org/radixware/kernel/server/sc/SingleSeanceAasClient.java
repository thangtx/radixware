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
package org.radixware.kernel.server.sc;

import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.server.aio.EventDispatcher;
import org.radixware.kernel.server.aio.ServiceManifestLoader;
import org.radixware.kernel.server.units.AasClient;
import org.radixware.kernel.server.units.Unit;
import org.radixware.schemas.aas.InvokeRs;

/**
 *
 * @author dsafonov
 */
public abstract class SingleSeanceAasClient<T extends AasInvokeItem> extends AasClient {
    
    protected T item;

    public SingleSeanceAasClient(Unit unit, ServiceManifestLoader manifestLoader, LocalTracer tracer, EventDispatcher dispatcher) {
        super(unit, manifestLoader, tracer, dispatcher);
    }

    public boolean busy() {
        return item != null || !seances.isEmpty() && seances.get(0).busy();
    }
    
    public void invoke(final T invokeItem) {
        item = invokeItem;
        invoke(invokeItem.getInvokeDoc(), invokeItem.getHeaders(), invokeItem.isKeepConnect(), invokeItem.getTimeoutMillis(), invokeItem.getScpName());
    }

    @Override
    protected void onInvokeResponse(InvokeRs rs) {
        try {
            onInvokeResponseImpl(rs);
        } finally {
            item = null;
        }
    }

    @Override
    protected void onInvokeException(ServiceClientException exception) {
        try {
            onInvokeExceptionImpl(exception);
        } finally {
            item = null;
        }
    }
    
    public T getItem() {
        return item;
    }
    
    protected abstract void onInvokeResponseImpl(InvokeRs rs);
        
    protected abstract void onInvokeExceptionImpl(ServiceClientException ex);

}
