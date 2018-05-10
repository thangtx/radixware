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

package org.radixware.kernel.common.client.errors;

import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.schemas.eas.SessionRestorePolicy;


public final class SessionDoesNotExistError extends EasError implements IClientError{
    
    private static final long serialVersionUID = 5441820109449798895L;
    private final SessionRestorePolicy.Enum sessionRestorePolicy;
    
    public SessionDoesNotExistError(final ServiceCallFault fault){
        super("session does not exits",fault);
        sessionRestorePolicy = parseRestoringPolicy(fault.getMessage());
    }
    
    private static SessionRestorePolicy.Enum parseRestoringPolicy(final String policy){        
        if (SessionRestorePolicy.PASSWORD_MUST_BE_ENTERED.toString().equals(policy)){
            return SessionRestorePolicy.PASSWORD_MUST_BE_ENTERED;
        }else if (SessionRestorePolicy.SAVED_PASSWORD_CAN_BE_USED.toString().equals(policy)){
            return SessionRestorePolicy.SAVED_PASSWORD_CAN_BE_USED;
        }else{
            return null;
        }
    }
    
    public SessionRestorePolicy.Enum getRestoringPolicy(){
        return sessionRestorePolicy;
    }

    @Override
    public String getTitle(final MessageProvider messageProvider) {
        return messageProvider.translate("ClientSessionException", "Connection Problem");
    }

    @Override
    public String getLocalizedMessage(final MessageProvider messageProvider) {
        return messageProvider.translate("ExplorerError", "Session does not exist");
    }

    @Override
    public String getDetailMessage(final MessageProvider messageProvider) {
        return null;
    }

}
