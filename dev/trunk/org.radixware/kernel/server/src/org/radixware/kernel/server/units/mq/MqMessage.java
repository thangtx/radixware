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

import org.radixware.kernel.server.units.mq.interfaces.IMqQueueConsumer;

/**
 *
 * @author dsafonov
 */
public abstract class MqMessage<T,P> {

    private final MqUnit unit;
    private final IMqQueueConsumer consumer;
    private final T cargo;
    private final long regTimeMillis;

    public MqMessage(final MqUnit unit, final IMqQueueConsumer consumer, final T cargo, final long regTimeMillis) {
        this.unit = unit;
        this.consumer = consumer;
        this.cargo = cargo;
        this.regTimeMillis = regTimeMillis;
    }

    public long getRegTimeMillis() {
        return regTimeMillis;
    }
    
    public MqUnit getUnit() {
        return unit;
    }

    public IMqQueueConsumer getConsumer() {
        return consumer;
    }

    public T getCargo() {
        return cargo;
    }
    
    public abstract P getPartitionId();
    
    public abstract String getDebugKey();
    
    public abstract byte[] getBodyBytes();
    
    public boolean hasBodyStr() {
        return false;
    }
    
    public String getBodyStr() {
        return null;
    }
    
}
