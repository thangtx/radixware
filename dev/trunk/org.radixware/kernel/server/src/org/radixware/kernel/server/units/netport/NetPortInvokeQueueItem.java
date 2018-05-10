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
import org.radixware.kernel.server.aio.AadcAffinity;
import org.radixware.kernel.server.sc.AasInvokeItem;
import org.radixware.schemas.aasWsdl.InvokeDocument;


public class NetPortInvokeQueueItem extends AasInvokeItem {
    
    public final INetPortInvokeSource source;
    public final long createTimeMillis = System.currentTimeMillis();
    public final long id;
    public String debugTitle;

    public NetPortInvokeQueueItem(final INetPortInvokeSource invokeSource, final InvokeDocument invokeXml, final Map<String, String> invokeHeaders, final boolean keepConnect, final AadcAffinity aadcAffinity) {
        super(invokeXml, invokeHeaders, NetPortAasClient.AAS_INVOKE_TIMEOUT_MILLIS, invokeSource.getUnit().getScpName(), keepConnect, aadcAffinity);
        this.source = invokeSource;
        id = invokeSource.getUnit().getNextItemId();
        debugTitle = "#" + id + "(" + source.getInvokeSourceDesc() + ")" + (aadcAffinity != null ? " AADC.affinity.key = " + aadcAffinity.getAffinityKey() : "");
    }
}
