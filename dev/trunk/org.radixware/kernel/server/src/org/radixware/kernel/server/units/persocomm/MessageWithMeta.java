/*
 * Copyright (c) 2008-2018, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.units.persocomm;

import org.radixware.schemas.personalcommunications.MessageDocument;

public class MessageWithMeta {

    public final long id;
    public final MessageDocument xDoc;
    public final Long baseForwardTimeMillis;
    public final Long lastForwardTimeMillis;
    public final String sendCallbackClassName;
    public final String sendCallbackMethodName;
    public final String deliveryCallbackClassName;
    public final String deliveryCallbackMethodName;

    public MessageWithMeta(
            long id, MessageDocument xDoc, Long baseForwardTimeMillis, Long lastForwardTimeMillis, String sendCallbackClassName, String sendCallbackMethodName, String deliveryCallbackClassName, String deliveryCallbackMethodName) {
        this.id = id;
        this.xDoc = xDoc;
        this.baseForwardTimeMillis = baseForwardTimeMillis;
        this.lastForwardTimeMillis = lastForwardTimeMillis;
        this.sendCallbackClassName = sendCallbackClassName;
        this.sendCallbackMethodName = sendCallbackMethodName;
        this.deliveryCallbackClassName = deliveryCallbackClassName;
        this.deliveryCallbackMethodName = deliveryCallbackMethodName;
    }
    
    public MessageWithMeta(MessageWithMeta other) {
        this(other.id, other.xDoc, other.baseForwardTimeMillis, other.lastForwardTimeMillis, other.sendCallbackClassName, other.sendCallbackMethodName, other.deliveryCallbackClassName, other.deliveryCallbackMethodName);
    }
    
    public MessageWithMeta() {
        this(0, null, null, null, null, null, null, null);
    }
    
}
