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


public class TemporaryPasswordExpiredError extends EasError implements IClientError{
    
    private static final long serialVersionUID = 5142790353578286715L;
    
    private final boolean onCreate;        
    
    public TemporaryPasswordExpiredError(final boolean onCreateSession, final ServiceCallFault source){
        super("Temporary password has expired. It is impossible to continue work.",source);
        onCreate = onCreateSession;
    }
    
    @Override
    public String getTitle(final MessageProvider messageProvider) {        
        return messageProvider.translate("ClientSessionException", "Connection Problem");
    }

    @Override
    public String getLocalizedMessage(final MessageProvider messageProvider) {
        if (onCreate){
            return messageProvider.translate("ExplorerError", "Temporary password has expired");
        }else{
            return messageProvider.translate("ExplorerError", "Temporary password has expired. It is impossible to continue work.");
        }
    }

    @Override
    public String getDetailMessage(final MessageProvider messageProvider) {
        return null;
    }    

}
