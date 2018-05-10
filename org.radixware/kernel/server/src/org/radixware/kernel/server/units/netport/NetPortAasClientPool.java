/*
 * Copyright (c) 2008-2018, Compass Plus Limited. All rights reserved.
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
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.ServiceConnectTimeout;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.aio.AadcAffinity;
import org.radixware.kernel.server.sc.AasClientPool;
import org.radixware.kernel.server.sc.SingleSeanceAasClient;
import org.radixware.schemas.aas.InvokeRq;
import org.radixware.schemas.aas.InvokeRq.Parameters;
import org.radixware.schemas.aasWsdl.InvokeDocument;
import org.radixware.schemas.types.MapStrStr;

class NetPortAasClientPool extends AasClientPool<NetPortInvokeQueueItem> {

    private static final Id NET_CHANNEL_CLASS_ID = Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM");
    private static final String ON_CONNECT_METHOD = "mthI5YABQPZBVGDBIRUZ4DGWHYKKY";
    private static final String ON_CONNECT_TIMEOUT_METHOD = "mthFR5YPKF6Z5GRNJDWHMETZW4NY4";
    private static final String ON_RECV_METHOD = "mthDPLQGAWSPVG2HDLYDVRGKFWPZ4";
    private static final String ON_RECV_TIMEOUT_METHOD = "mthUEI3NFHBGNHSHMPJ33RIRMAZKI";
    private static final String ON_DISCONNECT_METHOD = "mth6RAQH773VRD7VGRLMWFGKG4IAA";
    private final NetPortHandlerUnit unit;

    public NetPortAasClientPool(final NetPortHandlerUnit unit) {
        super(unit.createTracer());
        this.unit = unit;
    }

    @Override
    public void invoke(NetPortInvokeQueueItem item) {
        logAdd(item);
        unit.onQueuePut();
        super.invoke(item);
    }

    @Override
    protected void beforeDoInvoke(NetPortInvokeQueueItem item) {
        logInvoke(item);
    }

    @Override
    protected void onQueueOverflow(NetPortInvokeQueueItem item) {
        if (item != null) {
            item.source.onAasInvokeException(new ServiceConnectTimeout("AAS invocation queue overflow"));
        }
    }

    private void logAdd(final NetPortInvokeQueueItem item) {
        if (item != null) {
            final int queueSize = getQueueSize();
            unit.getTrace().debug("Adding item " + item.debugTitle + " , queue size: " + queueSize + ", waited: " + (System.currentTimeMillis() - item.createTimeMillis) + " ms", EEventSource.NET_PORT_HANDLER_QUEUE, false);
        }
    }

    private void logInvoke(final NetPortInvokeQueueItem item) {
        if (item != null) {
            unit.getTrace().debug("Invoking item " + item.debugTitle + " , queue size: " + getQueueSize() + ", waited: " + (System.currentTimeMillis() - item.createTimeMillis) + " ms", EEventSource.NET_PORT_HANDLER_QUEUE, false);
        }
    }

    @Override
    protected void onTimeoutInQueue(NetPortInvokeQueueItem item) {
        if (item != null) {
            item.source.onAasInvokeException(new ServiceConnectTimeout("Timeout during waiting in queue"));
        }
    }
    
    @Override
    public void tryInvoke() {
        super.tryInvoke();
        unit.getNetPortHandlerMonitor().updateQueueSize(getQueueSize());
    }

    @Override
    protected SingleSeanceAasClient<NetPortInvokeQueueItem> createAasClient() {
        return new NetPortAasClient(unit);
    }

    public void invokeOnConnectTimeout(
            final INetPortInvokeSource invokeSource,
            final long channelId,
            final String serverAddr,
            final String clientAddr,
            final String certCn,
            final String sid,
            final String callbackPid,
            final String callbackWid,
            final AadcAffinity aadcAffinity
    ) {
        final InvokeDocument rq = InvokeDocument.Factory.newInstance();
        final InvokeRq invXml = rq.addNewInvoke().addNewInvokeRq();
        invXml.setClassId(NET_CHANNEL_CLASS_ID);
        invXml.setMethodId(ON_CONNECT_TIMEOUT_METHOD);
        final InvokeRq.Parameters paramsXml = invXml.addNewParameters();
        paramsXml.addNewItem().setInt(channelId);
        paramsXml.addNewItem().setStr(serverAddr);
        paramsXml.addNewItem().setStr(clientAddr);
        paramsXml.addNewItem().setStr(certCn);
        paramsXml.addNewItem().setStr(sid);
        paramsXml.addNewItem().setStr(callbackPid);
        paramsXml.addNewItem().setStr(callbackWid);
        invoke(new NetPortInvokeQueueItem(invokeSource, rq, null, true, aadcAffinity));
    }

    public void invokeOnConnect(
            final INetPortInvokeSource invokeSource,
            final long channelId,
            final String serverAddress,
            final String clientAddress,
            final String certCn,
            final String sid,
            final AadcAffinity aadcAffinity) {
        final InvokeDocument rq = InvokeDocument.Factory.newInstance();
        final InvokeRq invXml = rq.addNewInvoke().addNewInvokeRq();
        invXml.setClassId(NET_CHANNEL_CLASS_ID);
        invXml.setMethodId(ON_CONNECT_METHOD);
        final Parameters paramsXml = invXml.addNewParameters();
        paramsXml.addNewItem().setInt(channelId);
        paramsXml.addNewItem().setStr(serverAddress);
        paramsXml.addNewItem().setStr(clientAddress);
        paramsXml.addNewItem().setStr(certCn);
        paramsXml.addNewItem().setStr(sid);
        invoke(new NetPortInvokeQueueItem(invokeSource, rq, null, true, aadcAffinity));
    }

    public void invokeOnRecv(final INetPortInvokeSource invokeSource,
            final Map<String, String> invokeHeaders,
            final long channelId,
            final String serverAddr,
            final String clientAddr,
            final String certCn,
            final String sid,
            final String callbackPid,
            final String callbackWid,
            final MapStrStr headers,
            final byte[] packet,
            final boolean connected,
            final AadcAffinity aadcAffinity) {
        final InvokeDocument rq = InvokeDocument.Factory.newInstance();
        final InvokeRq invXml = rq.addNewInvoke().addNewInvokeRq();
        invXml.setClassId(NET_CHANNEL_CLASS_ID);
        invXml.setMethodId(ON_RECV_METHOD);
        final Parameters paramsXml = invXml.addNewParameters();
        paramsXml.addNewItem().setInt(channelId);
        paramsXml.addNewItem().setStr(serverAddr);
        paramsXml.addNewItem().setStr(clientAddr);
        paramsXml.addNewItem().setStr(certCn);
        paramsXml.addNewItem().setStr(sid);
        paramsXml.addNewItem().setStr(callbackPid);
        paramsXml.addNewItem().setStr(callbackWid);
        paramsXml.addNewItem().setMapStrStr(headers);
        paramsXml.addNewItem().setBin(packet);
        paramsXml.addNewItem().setBool(connected);
        invoke(new NetPortInvokeQueueItem(invokeSource, rq, invokeHeaders, true, aadcAffinity));
    }

    public void invokeOnRecvTimeout(final INetPortInvokeSource invokeSource,
            final long channelId,
            final String serverAddr,
            final String clientAddr,
            final String portCn,
            final String sid,
            final String callbackPid,
            final String callbackWid,
            final AadcAffinity aadcAffinity) {
        final InvokeDocument rq = InvokeDocument.Factory.newInstance();
        final InvokeRq invXml = rq.addNewInvoke().addNewInvokeRq();
        invXml.setClassId(NET_CHANNEL_CLASS_ID);
        invXml.setMethodId(ON_RECV_TIMEOUT_METHOD);
        final Parameters paramsXml = invXml.addNewParameters();
        paramsXml.addNewItem().setInt(channelId);
        paramsXml.addNewItem().setStr(serverAddr);
        paramsXml.addNewItem().setStr(clientAddr);
        paramsXml.addNewItem().setStr(portCn);
        paramsXml.addNewItem().setStr(sid);
        paramsXml.addNewItem().setStr(callbackPid);
        paramsXml.addNewItem().setStr(callbackWid);
        invoke(new NetPortInvokeQueueItem(invokeSource, rq, null, true, aadcAffinity));
    }

    public void invokeOnDisconnect(final INetPortInvokeSource invokeSource,
            final long channelId,
            final String serverAddr,
            final String clientAddr,
            final String portCn,
            final String sid,
            final String callbackPid,
            final String callbackWid,
            final AadcAffinity aadcAffinity) {
        final InvokeDocument rq = InvokeDocument.Factory.newInstance();
        final InvokeRq invXml = rq.addNewInvoke().addNewInvokeRq();
        invXml.setClassId(NET_CHANNEL_CLASS_ID);
        invXml.setMethodId(ON_DISCONNECT_METHOD);
        final Parameters paramsXml = invXml.addNewParameters();
        paramsXml.addNewItem().setInt(channelId);
        paramsXml.addNewItem().setStr(serverAddr);
        paramsXml.addNewItem().setStr(clientAddr);
        paramsXml.addNewItem().setStr(portCn);
        paramsXml.addNewItem().setStr(sid);
        paramsXml.addNewItem().setStr(callbackPid);
        paramsXml.addNewItem().setStr(callbackWid);
        invoke(new NetPortInvokeQueueItem(invokeSource, rq, null, true, aadcAffinity));
    }

}
