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
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.schemas.netporthandler.ProcessMess;
import org.radixware.schemas.netporthandler.ProcessRq;
import org.radixware.schemas.types.MapStrStr;

/**
 *
 * @author dsafonov
 */
public class SeanceTaskProcess implements ISeanceTask {

    private final NetPortSapSeance sapSeance;
    private final ProcessRq processRq;

    public SeanceTaskProcess(NetPortSapSeance sapSeance) {
        this.sapSeance = sapSeance;
        this.processRq = ((ProcessRq) sapSeance.xmlRq);
        if (processRq.isSetIsRecvSync() && !processRq.isSetRecvTimeout()) {
            sapSeance.unit.getTrace().put(EEventSeverity.WARNING, "ProcessRq contains RecvSync flag, but not contains RecvTimeout. RecvSync will be ignored", EEventSource.UNIT_PORT);
        }
    }

    @Override
    public NetPortSapSeance getSapSeance() {
        return sapSeance;
    }

    @Override
    public NetPortCallbackTarget getCallbackTarget() {
        return new NetPortCallbackTarget(processRq.getCallbackPid(), processRq.getCallbackWid());
    }

    @Override
    public EChannelOperation getOperation() {
        return EChannelOperation.PROCESS;
    }

    @Override
    public XmlObject getRq() {
        return processRq;
    }

    @Override
    public boolean isConnectSync() {
        return false;
    }

    @Override
    public boolean isReceiveSync() {
        return processRq.isSetIsRecvSync() && processRq.getIsRecvSync() && processRq.isSetRecvTimeout();
    }

    @Override
    public Long getReceiveTimeoutSec() {
        return processRq.getRecvTimeout();
    }

    @Override
    public Long getCloseDelaySec() {
        return processRq.getCloseDelay();
    }

    @Override
    public byte[] getSendPacket() {
        return processRq.getSendPacket();
    }

    @Override
    public MapStrStr getSendPacketHeaders() {
        return processRq.getSendPackedHeaders();
    }

}
