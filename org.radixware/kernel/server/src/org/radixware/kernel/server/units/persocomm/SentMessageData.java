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

import org.radixware.kernel.common.enums.EPersoCommDeliveryStatus;

public class SentMessageData {
    
    public long messageId;
    public Long sendCallbackRequired;
    public Long deliveryCallbackRequired;
    public String deliveryCallbackClassName;
    public String deliveryCallbackMethodName;
    public EPersoCommDeliveryStatus deliveryStatus;
    public long deliveryExpTimeMillis;
    
    public SentMessageData() {
        this(Long.MIN_VALUE, null, null, null, null, null, Long.MIN_VALUE);
    }

    public SentMessageData(
            long messageId,
            Long sendCallbackRequired,
            Long deliveryCallbackRequired,
            String deliveryCallbackClassName,
            String deliveryCallbackMethodName,
            EPersoCommDeliveryStatus deliveryStatus,
            long deliveryExpTimeMillis
    ) {
        this.messageId = messageId;
        this.sendCallbackRequired = sendCallbackRequired;
        this.deliveryCallbackRequired = deliveryCallbackRequired;
        this.deliveryCallbackClassName = deliveryCallbackClassName;
        this.deliveryCallbackMethodName = deliveryCallbackMethodName;
        this.deliveryStatus = deliveryStatus;
        this.deliveryExpTimeMillis = deliveryExpTimeMillis;
        
    }
    
    public boolean isStillTracking() {
        return deliveryStatus == EPersoCommDeliveryStatus.TRACKING
                || (deliveryCallbackRequired != null && deliveryCallbackRequired == 2);
    }
    
}
