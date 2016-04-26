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

  
public class CredentialsWasNotDefinedError extends WrongPasswordError implements IClientError{
    
    private static final long serialVersionUID = -1126171473269279536L;

    @Override
    public String getTitle(final MessageProvider messageProvider) {
        return messageProvider.translate("ExplorerMessage", "Authentication problem");
    }

    @Override
    public String getLocalizedMessage(final MessageProvider messageProvider) {
        return messageProvider.translate("ExplorerMessage", "Unable to get user credentials");
    }

    @Override
    public String getDetailMessage(MessageProvider messageProvider) {
        return null;
    }

}
