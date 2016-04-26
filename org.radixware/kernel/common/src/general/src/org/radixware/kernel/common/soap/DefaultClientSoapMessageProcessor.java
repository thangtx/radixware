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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.utils.SoapFormatter;


public class DefaultClientSoapMessageProcessor implements IClientSoapMessageProcessor {

    @Override
    public byte[] wrapRequest(XmlObject requestPayload) throws ProcessException {
        return SoapFormatter.prepareMessage(requestPayload);
    }

    @Override
    public XmlObject unwrapResponce(byte[] responceData, final Class resultClass) throws ProcessException, ServiceCallFault {
        try {
            return SoapFormatter.parseResponse(responceData, resultClass);
        } catch (IOException ex) {
            throw new ProcessException(ex);
        }
    }
}
