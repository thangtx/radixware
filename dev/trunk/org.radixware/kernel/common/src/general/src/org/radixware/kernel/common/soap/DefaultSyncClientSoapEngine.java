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

package org.radixware.kernel.common.soap;

import java.io.IOException;
import java.util.HashMap;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceCallRecvException;
import org.radixware.kernel.common.exceptions.ServiceCallSendException;
import org.radixware.kernel.common.exceptions.ServiceCallTimeout;
import org.radixware.kernel.common.sc.SyncClientConnection;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.SoapFormatter;


public class DefaultSyncClientSoapEngine implements ISyncClientSoapEngine {

    private final LocalTracer tracer;

    public DefaultSyncClientSoapEngine(LocalTracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public RadixSoapMessage invoke(final RadixSoapMessage message, final SyncClientConnection connection) throws ServiceCallTimeout, ServiceCallFault, InterruptedException, ServiceCallSendException, ServiceCallRecvException {
        final String connectionInfo = connection.toString();
        final String destinationInfo = message.getDestinationInfo();
        try {
            RadixSoapHelper.send(message, SoapFormatter.prepareMessage(message.getBodyDocument()), connection, tracer);

            final HashMap<String, String> headerAttrs = new HashMap<>();

            final byte[] responceData = RadixSoapHelper.receiveResponceData(connection, message, headerAttrs, tracer);

            final XmlObject rs = SoapFormatter.parseResponse(responceData, message.getResultClass());

            final RadixSoapMessage responceMessage = RadixSoapHelper.createMessageFromResponce(rs, headerAttrs, message);

            RadixSoapHelper.logMessageReceived(responceMessage.getType(), responceMessage.getBodyDocument(), connectionInfo, tracer);

            return responceMessage;
        } catch (IOException ex) {
            RadixSoapHelper.processIOException(ex, message.getType(), destinationInfo);
            throw new ServiceCallRecvException("Error", ex);//shouldn't happen, helper always throws exception.
        }
    }
}
