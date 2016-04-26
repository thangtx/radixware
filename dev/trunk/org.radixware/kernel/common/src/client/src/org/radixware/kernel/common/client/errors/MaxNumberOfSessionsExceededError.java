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

import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.schemas.eas.UserSessionsDocument;


public final class MaxNumberOfSessionsExceededError extends EasError implements IClientError{
    
    private static final long serialVersionUID = 4012253175310829866L;
    
    private final UserSessionsDocument userSessionsDoc;

    public MaxNumberOfSessionsExceededError(final UserSessionsDocument userSessionsDoc, final ServiceCallFault fault){
        super("The maximum number of opened sessions for this account has been exceeded",fault);
        this.userSessionsDoc = userSessionsDoc;
    }
    
    public int getMaxNumberOfSessions(){
        return userSessionsDoc.getUserSessions().getMaxNumber();
    }
    
    public List<org.radixware.schemas.eas.SessionDescription> getOpenedSessions(){
        final List<org.radixware.schemas.eas.SessionDescription> result = 
                userSessionsDoc.getUserSessions().getItemList();
        return result==null ? Collections.<org.radixware.schemas.eas.SessionDescription>emptyList() : result;
    }    

    @Override
    public String getTitle(final MessageProvider messageProvider) {
        return messageProvider.translate("ClientSessionException", "Connection Problem");
    }

    @Override
    public String getLocalizedMessage(final MessageProvider messageProvider) {
        return messageProvider.translate("SelectEasSessionDialog", "The maximum number of opened sessions for this account has been exceeded");
    }

    @Override
    public String getDetailMessage(final MessageProvider messageProvider) {
        return null;
    }
}