/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.common.client.errors;

import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.exceptions.ServiceCallFault;


public class CantDeleteSubobjectsFault extends ServiceCallFault implements IClientError{
    
    static final long serialVersionUID = -6222554793631274355L;
    
    private final String message;

    public CantDeleteSubobjectsFault(final ServiceCallFault source){
        super(source.getFaultCode(), source.getFaultString(), source.getDetail());
        message = source.getMessage();
    }

    @Override
    public String getTitle(final MessageProvider messageProvider) {
        return messageProvider.translate("Editor", "Failed to delete object");
    }

    @Override
    public String getLocalizedMessage(final MessageProvider messageProvider) {
        return message;
    }

    @Override
    public String getDetailMessage(final MessageProvider messageProvider) {
        return null;
    }
    
    
}
