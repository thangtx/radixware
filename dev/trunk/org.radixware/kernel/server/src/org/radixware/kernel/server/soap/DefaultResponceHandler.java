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

import org.radixware.kernel.common.soap.RadixSoapMessage;


public class DefaultResponceHandler implements IResponceHandler {

    private RadixSoapMessage responce;
    private Exception exception;

    @Override
    public void onResponce(RadixSoapMessage responce) {
        this.responce = responce;
    }

    @Override
    public void onException(Exception ex) {
        this.exception = ex;
    }

    public RadixSoapMessage getResponce() throws Exception {
        if (exception != null) {
            throw exception;
        }
        return responce;
    }
    
    public Exception getException() {
        return exception;
    }
}
