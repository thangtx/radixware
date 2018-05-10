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

class MessageDeliveryInfo {
    final long messageId;
    final EPersoCommDeliveryStatus deliveryStatus;
    final Problem problem;
    final long waitUntilMillis;
    
    private MessageDeliveryInfo(long messageId, EPersoCommDeliveryStatus deliveryStatus, Problem problem, long waitUntilMillis) {
        this.messageId = messageId;
        this.deliveryStatus = deliveryStatus;
        this.problem = problem;
        this.waitUntilMillis = waitUntilMillis;
    }

    public MessageDeliveryInfo(long messageId, EPersoCommDeliveryStatus deliveryStatus) {
        this(messageId, deliveryStatus, Problem.None, Long.MAX_VALUE);
    }
    
    public boolean waitExpired() {
        return System.currentTimeMillis() >= waitUntilMillis;
    }
    
    public MessageDeliveryInfo notFound(long waitTimeMillis) {
        if (problem == Problem.None) {
            return new MessageDeliveryInfo(messageId, deliveryStatus, Problem.NonCommited, System.currentTimeMillis() + waitTimeMillis);
        } else if (problem == Problem.SendCallbackNotDone) {
            return new MessageDeliveryInfo(messageId, deliveryStatus, Problem.Deleted, Long.MIN_VALUE);
        }
        return this;
    }
    
    public MessageDeliveryInfo sendCallbackNotDone(long expTimeMillis) {
        if (problem == Problem.None) {
            return new MessageDeliveryInfo(messageId, deliveryStatus, Problem.SendCallbackNotDone, expTimeMillis);
        }
        return this;
    }
    
    static enum Problem {
        None, NonCommited, SendCallbackNotDone, Deleted
    }

}
