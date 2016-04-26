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

package org.radixware.kernel.explorer.errors;

import org.radixware.kernel.common.client.errors.AuthError;
import org.radixware.kernel.common.client.errors.IAlarm;
import org.radixware.kernel.common.client.errors.IClientError;
import org.radixware.kernel.common.client.localization.MessageProvider;


public final class Pkcs11TokenRemovedError extends AuthError implements IClientError, IAlarm{
    
    static final long serialVersionUID = 2307005511445849605L;
    
    public Pkcs11TokenRemovedError(){
        super(null,null);
    }

    @Override
    public String getTitle(final MessageProvider messageProvider) {
        return messageProvider.translate("PKCS11", "Hardware Security Module Error");
    }

    @Override
    public String getLocalizedMessage(final MessageProvider messageProvider) {
        return 
            messageProvider.translate("PKCS11", "Token has been removed. The Explorer will disconnect now.");
    }

    @Override
    public String getMessage() {
        return "";
    }        

    @Override
    public String getDetailMessage(final MessageProvider messageProvider) {
        return "";
    }    
}
