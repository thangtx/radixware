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

package org.radixware.kernel.server.soap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.cxf.ws.addressing.EndpointReferenceType;
import org.radixware.kernel.common.sc.SyncClientConnection;
import org.radixware.kernel.common.soap.RadixSoapHelper;
import org.radixware.kernel.common.soap.RadixSoapMessage;
import org.radixware.kernel.common.trace.LocalTracer;


public class SyncClientConduitDelegate implements IConduitDelegate {

    private final SyncClientConnection connection;
    private final RadixSoapMessage message;
    private final LocalTracer tracer;
    private final Map<String, String> responceHeaderAttrs = new HashMap<>();
    private byte[] responceData;

    public SyncClientConduitDelegate(SyncClientConnection connection, RadixSoapMessage message, LocalTracer tracer) {
        this.connection = connection;
        this.message = message;
        this.tracer = tracer;
    }

    @Override
    public void setResponceDataListener(IResponceDataListener listener) {
        listener.onResponce(responceData);
    }

    @Override
    public void sendRequest(EndpointReferenceType endpointInfo, byte[] requestData) throws IOException {
        if (message.getAttrs() == null) {
            message.setAttrs(new HashMap<String, String>());
        }
        ServerSoapUtils.setupRequestPath(endpointInfo, message.getAttrs());
        RadixSoapHelper.send(message, requestData, connection, tracer);
        responceData = RadixSoapHelper.receiveResponceData(connection, message, responceHeaderAttrs, tracer);
    }
}
