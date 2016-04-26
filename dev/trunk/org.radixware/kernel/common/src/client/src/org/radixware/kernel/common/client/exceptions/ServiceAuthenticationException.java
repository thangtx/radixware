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

package org.radixware.kernel.common.client.exceptions;

import org.radixware.kernel.common.client.errors.IClientError;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.exceptions.ServiceCallException;


public class ServiceAuthenticationException extends ServiceCallException implements IClientError{
    
    static final long serialVersionUID = -300351119947604463L;
    
    public ServiceAuthenticationException(){
        super("EAS-service authentication failure");
    }

    @Override
    public String getTitle(MessageProvider messageProvider) {
        return messageProvider.translate("ExplorerMessage", "Connection Error");
    }

    @Override
    public String getLocalizedMessage(MessageProvider messageProvider) {
        return messageProvider.translate("ExplorerMessage", "Failed to authenticate server");
    }

    @Override
    public String getDetailMessage(MessageProvider messageProvider) {
        return "";
    }    
}
