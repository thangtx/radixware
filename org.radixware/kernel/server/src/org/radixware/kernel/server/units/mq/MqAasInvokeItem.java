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
package org.radixware.kernel.server.units.mq;

import java.util.Map;
import org.radixware.kernel.server.aio.AadcAffinity;
import org.radixware.kernel.server.sc.AasInvokeItem;
import org.radixware.schemas.aasWsdl.InvokeDocument;

/**
 *
 * @author dsafonov
 */
public class MqAasInvokeItem extends AasInvokeItem {

    private final MqMessage mqMessage;

    public MqAasInvokeItem(MqMessage mqMessage, InvokeDocument invokeDoc, Map<String, String> headers, int timeoutMillis, String scp, boolean keepConnect, final AadcAffinity aadcAffinity) {
        super(invokeDoc, headers, timeoutMillis, scp, keepConnect, aadcAffinity);
        this.mqMessage = mqMessage;
    }

    public MqMessage getMqMessage() {
        return mqMessage;
    }

}
