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
import java.util.List;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.utils.SoapFormatter;


public class DefaultServerSoapMessageProcessor implements IServerSoapMessageProcessor {

    @Override
    public byte[] wrapResponce(XmlObject messagePayload) throws ProcessException {
        return SoapFormatter.prepareMessage(messagePayload);
    }

    @Override
    public byte[] wrapFault(ServiceProcessFault fault, List<SoapFormatter.ResponseTraceItem> traceBuffer) {
        return SoapFormatter.prepareFault(fault.code, fault.reason, fault.getFaultDetailWriter() == null ? new SoapFormatter.DefaultFaultDetailWriter(fault.getMessage(), fault.getCause(), null) : fault.getFaultDetailWriter(), traceBuffer);
    }

    @Override
    public XmlObject unwrapRequest(byte[] dirtyData) throws ProcessException {
        try {
            return SoapFormatter.parseSoapRequest(dirtyData);
        } catch (IOException ex) {
            throw new ProcessException(ex);
        }
    }
}
