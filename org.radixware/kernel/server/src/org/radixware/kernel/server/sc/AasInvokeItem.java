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

import java.util.Map;
import org.radixware.schemas.aasWsdl.InvokeDocument;

/**
 *
 * @author dsafonov
 */
public class AasInvokeItem {

    private final InvokeDocument invokeDoc;
    private final Map<String, String> headers;
    private final boolean keepConnect;
    private final int timeoutMillis;
    private final String scpName;
    private final long createTimeMillis = System.currentTimeMillis();

    /**
     * This constructor does NOT copy passed parameters, don't modify them
     * afterwards
     */
    public AasInvokeItem(InvokeDocument invokeDoc, Map<String, String> headers, int timeoutMillis, String scp, boolean keepConnect) {
        this.invokeDoc = invokeDoc;
        this.headers = headers;
        this.keepConnect = keepConnect;
        this.timeoutMillis = timeoutMillis;
        this.scpName = scp;
    }

    public long getCreateTimeMillis() {
        return createTimeMillis;
    }

    public String getScpName() {
        return scpName;
    }

    public InvokeDocument getInvokeDoc() {
        return invokeDoc;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public boolean isKeepConnect() {
        return keepConnect;
    }

    public int getTimeoutMillis() {
        return timeoutMillis;
    }

}
