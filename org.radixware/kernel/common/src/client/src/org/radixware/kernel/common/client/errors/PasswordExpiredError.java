/** Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
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

import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.auth.PasswordRequirements;
import org.radixware.kernel.common.client.auth.ClientAuthUtils;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.schemas.eas.ExceptionEnum;
import org.radixware.schemas.eas.PasswordRequirementsDocument;


public final class PasswordExpiredError extends EasError implements IClientError{
    
    private static final long serialVersionUID = -2532925392596056592L;
    
    private final PasswordRequirementsDocument requirementsDoc;
    
    public PasswordExpiredError(final PasswordRequirementsDocument document, final ServiceCallFault fault){
        super("password has expired",fault);
        this.requirementsDoc = document;
    }
    
    public PasswordExpiredError(final ServiceCallFault fault){
        super("password has expired",fault);
        final String message = fault.getMessage();
        final PasswordRequirementsDocument document;
        try{
            document = PasswordRequirementsDocument.Factory.parse(message);
        }catch(XmlException exception){//NOPMD            
            requirementsDoc = null;
            return;
        }
        requirementsDoc = document;
    }
    
    public PasswordExpiredError(){
        super("",new ServiceCallFault(ServiceProcessFault.FAULT_CODE_SERVER, ExceptionEnum.SHOULD_CHANGE_PASSWORD.toString(), null));
        requirementsDoc = null;
    }
    
    public PasswordRequirements getPasswordRequirements(final String userName){        
        return ClientAuthUtils.getPasswordRequirements(requirementsDoc.getPasswordRequirements(), userName);
    }

    @Override
    public String getTitle(final MessageProvider messageProvider) {
        return messageProvider.translate("ClientSessionException", "Connection Problem");
    }

    @Override
    public String getLocalizedMessage(final MessageProvider messageProvider) {
        return messageProvider.translate("ExplorerError", "Your password has expired");
    }

    @Override
    public String getDetailMessage(final MessageProvider messageProvider) {
        return null;
    }

}
