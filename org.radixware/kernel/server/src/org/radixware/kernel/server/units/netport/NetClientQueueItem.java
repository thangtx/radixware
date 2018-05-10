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

import java.net.InetSocketAddress;
import org.apache.xmlbeans.XmlObject;
import org.radixware.schemas.netporthandler.ConnectRq;
import org.radixware.schemas.netporthandler.ProcessRq;
import org.radixware.schemas.types.MapStrStr;

/**
 *
 * @author dsafonov
 */
public class NetClientQueueItem {

    private final NetPortSapSeance sapSeance;
    private final NetPortCallbackTarget callbackTarget;
    private final long createdMillis = System.currentTimeMillis();
    private final ProcessRq processRq;
    private final ConnectRq connectRq;
    private final NetClient netClient;
    private long connectedMillis = -1;
    private long capturedMillis = -1;
    private final String serverAddress;
    private final String clientAddress;
    private final InetSocketAddress channelConnectAddress;
    private final InetSocketAddress channelBindAddress;
    private final long id;

    public NetClientQueueItem(final NetClient netClient, final XmlObject xRq, final NetPortCallbackTarget callbackTarget) {
        this(netClient, xRq, null, callbackTarget);
    }

    public NetClientQueueItem(final NetClient netClient, final XmlObject xRq, final NetPortSapSeance sapSeance) {
        this(netClient, xRq, sapSeance, null);
    }

    private NetClientQueueItem(final NetClient netClient, final XmlObject xRq, final NetPortSapSeance sapSeance, final NetPortCallbackTarget callbackTarget) {
        this.netClient = netClient;
        this.sapSeance = sapSeance;
        this.processRq = (xRq instanceof ProcessRq) ? ((ProcessRq) xRq) : null;
        this.connectRq = (xRq instanceof ConnectRq) ? ((ConnectRq) xRq) : null;
        if (processRq == null && connectRq == null) {
            throw new IllegalArgumentException("Unsupported rq: " + xRq.getClass().getName());
        }
        this.callbackTarget = callbackTarget == null ? (processRq != null ? new NetPortCallbackTarget(processRq.getCallbackPid(), processRq.getCallbackWid()) : new NetPortCallbackTarget(connectRq.getCallbackPid(), connectRq.getCallbackWid())) : callbackTarget;
        if (connectRq != null) {
            clientAddress = connectRq.getLocalAddress();
            serverAddress = connectRq.getRemoteAddress();
        } else {
            clientAddress = null;
            serverAddress = null;
        }
        channelBindAddress = netClient.options.bindAddress;
        channelConnectAddress = netClient.options.connectAddress;
        this.id = netClient.getNextQueueItemId();
    }

    public boolean mustOpenNewConnection() {
        return connectRq != null && connectRq.isSetCanReuse() && !connectRq.getCanReuse();
    }

    public String getSid() {
        return processRq == null ? null : processRq.getSID();
    }

    public void markCaptured(final long curTimeMillis) {
        if (capturedMillis >= 0) {
            throw new IllegalStateException("already captured");
        }
        capturedMillis = curTimeMillis;
        netClient.trace("Captured " + this);
    }

    public void markConnected(final long curTimeMillis) {
        if (capturedMillis < 0) {
            throw new IllegalStateException("markConnected called before markCaptured");
        }
        if (connectedMillis >= 0) {
            throw new IllegalStateException("already connected");
        }
        connectedMillis = curTimeMillis;
        netClient.trace("Connected " + this);
    }

    public XmlObject getRqDoc() {
        return processRq != null ? processRq : connectRq;
    }

    public NetPortSapSeance getSapSeance() {
        return sapSeance;
    }

    public NetPortCallbackTarget getCallbackTarget() {
        return callbackTarget;
    }

    public Long getConnectTimeoutSec() {
        if (connectRq != null && connectRq.isSetConnectTimeout()) {
            return connectRq.getConnectTimeout();
        }
        return null;
    }

    public long getReceiveTimeoutSec() {
        if (connectRq != null) {
            if (connectRq.isSetRecvTimeout()) {
                return connectRq.getRecvTimeout();
            }
            return -1;
        } else if (processRq.isSetRecvTimeout()) {
            return processRq.getRecvTimeout();
        } else {
            return -1;
        }
    }

    public long getCreateTimeMillis() {
        return createdMillis;
    }

    public boolean isConnected() {
        return connectedMillis >= 0;
    }

    public boolean isTimedOut(final long curTimeMillis) {
        if (!isConnected()) {
            final Long leftForConnectMillis = getLeftForConnectMillis(curTimeMillis);
            if (leftForConnectMillis == null || leftForConnectMillis < 0) {
                return false;
            }
            return leftForConnectMillis == 0;
        } else {
            return false;//isTimedOut is related to queue item in queue, and queue item should be removed from queue when onConnect is called.
        }
    }

    public NetClient getNetClient() {
        return netClient;
    }

    public EChannelOperation getOperation() {
        return connectRq != null ? EChannelOperation.CONNECT : EChannelOperation.PROCESS;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public InetSocketAddress getChannelConnectAddressSnapshot() {
        return channelConnectAddress;
    }

    public InetSocketAddress getChannelBindAddressSnapshot() {
        return channelBindAddress;
    }

    public String getEffectiveServerAddr() {
        return serverAddress == null ? channelConnectAddress.toString() : serverAddress;
    }

    public String getEffectiveClientAddr() {
        return clientAddress == null ? (channelBindAddress == null ? null : channelBindAddress.toString()) : clientAddress;
    }

    public Long getLeftForConnectMillis(final long curTimeMillis) {
        if (isConnected()) {
            throw new IllegalStateException("already connected");
        }
        if (getConnectTimeoutSec() == null) {
            if (getReceiveTimeoutSec() < 0) {
                return -1l;
            } else {
                return Math.max(0, createdMillis + getReceiveTimeoutSec() * 1000 - curTimeMillis);
            }
        } else if (getConnectTimeoutSec() < 0) {
            return -1l;
        } else {
            return Math.max(0, createdMillis + getConnectTimeoutSec() * 1000 - curTimeMillis);
        }
    }

    public Long getLeftForReceiveMillis(final long curTimeMillis) {
        if (!isConnected()) {
            throw new IllegalStateException("not connected");
        }
        if (getReceiveTimeoutSec() < 0) {
            return -1l;
        }
        return connectedMillis + getReceiveTimeoutSec() * 1000 - curTimeMillis;
    }

    public boolean isConnectSync() {
        return connectRq != null && connectRq.isSetIsConnectSync() && connectRq.getIsConnectSync();
    }

    public boolean isReceiveSync() {
        if (connectRq != null) {
            return connectRq.isSetIsRecvSync() && connectRq.getIsRecvSync();
        } else {
            return processRq.isSetIsRecvSync() && processRq.getIsRecvSync();
        }
    }

    public Long getCloseDelaySec() {
        if (processRq != null) {
            return processRq.getCloseDelay();
        } else {
            return connectRq.getCloseDelay();
        }
    }

    public byte[] getSendPacket() {
        if (processRq != null) {
            return processRq.getSendPacket();
        } else {
            return connectRq.getSendPacket();
        }
    }

    public MapStrStr getSendPacketHeaders() {
        if (processRq != null) {
            return processRq.getSendPackedHeaders();
        } else {
            return connectRq.getSendPackedHeaders();
        }
    }

    private String getOpAttrs() {
        if (getOperation() == EChannelOperation.CONNECT) {
            return "to=" + getEffectiveServerAddr() + ", from=" + getEffectiveClientAddr();
        }
        if (getOperation() == EChannelOperation.PROCESS) {
            return "sid=" + getSid();
        }
        return null;
    }

    @Override
    public String toString() {
        final String opAttrs = getOpAttrs();
        final String opAttrsPart = opAttrs == null || opAttrs.isEmpty() ? "" : "(" + getOpAttrs() + ")";
        return "NetClientQueueItem(id=" + id + ",op=" + getOperation() + opAttrsPart + ",created=" + (netClient.unit.formatTimestamp(createdMillis)) + ")";
    }

}
