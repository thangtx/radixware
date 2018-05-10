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


public class SqmlIsNotAccessibleError extends Error implements IClientError{
    
    private static final long serialVersionUID = 3053757717299162331L;
    
    public SqmlIsNotAccessibleError(){
        
    }

    @Override
    public String getTitle(final MessageProvider messageProvider) {
        return messageProvider.translate("ExplorerError", "Operation is Not Accessible");
    }

    @Override
    public String getLocalizedMessage(final MessageProvider messageProvider) {
        return messageProvider.translate("ExplorerError", "This operation is not accessible because application was started with \"-noSqml\" argument.\nRemove this argument and try again.");
    }

    @Override
    public String getDetailMessage(final MessageProvider messageProvider) {
        return null;
    }

}
