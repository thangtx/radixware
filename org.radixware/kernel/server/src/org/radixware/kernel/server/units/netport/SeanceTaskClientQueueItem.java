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
package org.radixware.kernel.server.units.netport;

import org.apache.xmlbeans.XmlObject;
import org.radixware.schemas.types.MapStrStr;

/**
 *
 * @author dsafonov
 */
public class SeanceTaskClientQueueItem implements ISeanceTask {

    private final NetClientQueueItem item;
    private long receiveTimeoutSec = -1;

    public SeanceTaskClientQueueItem(NetClientQueueItem item) {
        this.item = item;
        updateReceiveTimeout();
    }

    public final void updateReceiveTimeout() {
        if (item.isConnected()) {
            final Long leftForReceiveMillis = item.getLeftForReceiveMillis(System.currentTimeMillis());
            if (leftForReceiveMillis == null || leftForReceiveMillis < 0) {
                receiveTimeoutSec = -1;
            } else {
                receiveTimeoutSec = Math.max(1, (long) Math.floor(leftForReceiveMillis / 1000. + 0.5));
            }
        }
    }

    public NetClientQueueItem getItem() {
        return item;
    }

    @Override
    public NetPortSapSeance getSapSeance() {
        return item.getSapSeance();
    }

    @Override
    public NetPortCallbackTarget getCallbackTarget() {
        return item.getCallbackTarget();
    }

    @Override
    public EChannelOperation getOperation() {
        return item.getOperation();
    }

    @Override
    public XmlObject getRq() {
        return item.getRqDoc();
    }

    @Override
    public boolean isConnectSync() {
        return item.isConnectSync();
    }

    @Override
    public boolean isReceiveSync() {
        return item.isReceiveSync();
    }

    @Override
    public Long getReceiveTimeoutSec() {
        return receiveTimeoutSec;
    }

    @Override
    public Long getCloseDelaySec() {
        return item.getCloseDelaySec();
    }

    @Override
    public byte[] getSendPacket() {
        return item.getSendPacket();
    }

    @Override
    public MapStrStr getSendPacketHeaders() {
        return item.getSendPacketHeaders();
    }

}
