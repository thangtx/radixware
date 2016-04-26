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


public final class SignatureException extends ServiceCallException implements IClientError{
    
    static final long serialVersionUID = 6275410362461387083L;
    
    public static enum EReason{
        NO_CERT,
        REQUESTED_CERT_NOT_FOUND,
        FAILED_TO_CALC_CERT_HASH,
        FAILED_TO_GET_CERT_DN,
        USER_CANCELED,
        FAILED_TO_SIGN,
        UNEXPECTED_AJAX_REQUEST,
        UNSUPPORTED_OPERATION
    }
    
    private final EReason reason;
    
    public SignatureException(final EReason reason, final String localizedMessage, final Throwable cause){
        super(localizedMessage,cause);        
        this.reason = reason;
    }
    
    public SignatureException(final EReason reason, final String localizedMessage){
        this(reason,localizedMessage,null);
    }
    
    public EReason getReason(){
        return reason;
    }
    
    @Override
    public String getTitle(MessageProvider messageProvider) {
        return messageProvider.translate("ExplorerMessage", "Connection Error");
    }

    @Override
    public String getLocalizedMessage(MessageProvider messageProvider) {
        return messageProvider.translate("ExplorerError", "Unable to identify the user");
    }

    @Override
    public String getDetailMessage(MessageProvider messageProvider) {
        return getMessage();
    }
    
}
