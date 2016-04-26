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

package org.radixware.kernel.common.client.errors;

import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.xmlsoap.schemas.soap.envelope.Detail;


public class AccessViolationError extends ServiceCallFault implements IClientError{
    
    private static final long serialVersionUID = -6225415177131262827L;
    
    public AccessViolationError(final ServiceCallFault source){
        this(source.getFaultCode(), source.getFaultString(), source.getDetail());
    }        
    
    protected AccessViolationError(final String faultCode, final String faultString, final Detail detail){
        super(faultCode,faultString,detail);
    }

    @Override
    public String getTitle(final MessageProvider messageProvider) {
        return messageProvider.translate("ExplorerError", "Insufficient Privileges");
    }

    @Override
    public String getLocalizedMessage(final MessageProvider messageProvider) {
        return getMessage();
    }

    @Override
    public String getDetailMessage(final MessageProvider messageProvider) {
        return "";
    }
    
}
