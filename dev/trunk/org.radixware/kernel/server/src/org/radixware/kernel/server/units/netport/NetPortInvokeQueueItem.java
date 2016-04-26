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

import java.util.Map;
import org.radixware.schemas.aasWsdl.InvokeDocument;


public class NetPortInvokeQueueItem {
    
    public final Seance seance;
    public final InvokeDocument invokeXml;
    public final Map<String, String> invokeHeaders;
    public final boolean keepConnect;
    public final long createTimeMillis = System.currentTimeMillis();
    public final long id;
    public String debugTitle;

    public NetPortInvokeQueueItem(Seance seance, InvokeDocument invokeXml, Map<String, String> invokeHeaders, boolean keepConnect) {
        this.seance = seance;
        this.invokeXml = invokeXml;
        this.invokeHeaders = invokeHeaders;
        this.keepConnect = keepConnect;
        id = seance.nphUnit.getNextItemId();
        debugTitle = "#" + id + "(" + seance.getConnectionDesc() + ")";
    }
}
