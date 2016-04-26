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

import java.security.ProviderException;
import javax.security.auth.login.FailedLoginException;
import org.radixware.kernel.common.client.errors.IClientError;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.exceptions.KeystoreControllerException;


public final class Pkcs11Exception extends KeystoreControllerException implements IClientError{
    
    static final long serialVersionUID = -8404437892282405041L;
    private final boolean wrongPassword;
    private final String message;
    private String providerException;        
    
    public Pkcs11Exception(final String message,final Throwable cause){
        super(cause!=null && (message==null || message.isEmpty()) ? cause.getMessage() : message, cause);
        this.message = message;
        boolean failedLoginException = false;
        if (cause!=null){
            for (Throwable reason=cause.getCause(); reason!=null;reason=reason.getCause()){
                if (reason instanceof FailedLoginException){
                    failedLoginException = true;
                    break;
                }else if (reason instanceof ProviderException){
                    if (reason.getMessage()!=null && !reason.getMessage().isEmpty()){
                        providerException = reason.getMessage();
                    }
                    //need for message of last ProviderException in stack
                }
            }
        }
        wrongPassword = failedLoginException;
    }    
    
    public Pkcs11Exception(final Throwable cause){
        this(null,cause);
    }
    
    public boolean wrongPassword(){
        return wrongPassword;
    }

    @Override
    public String getTitle(final MessageProvider messageProvider) {
        if (wrongPassword){
            return messageProvider.translate("PKCS11", "Wrong Password");
        }else{
            return messageProvider.translate("PKCS11", "Hardware Security Module Error");
        }
    }

    @Override
    public String getLocalizedMessage(final MessageProvider messageProvider) {
        if (message!=null && !message.isEmpty()){
            return message;
        }else if (providerException != null){
            return messageProvider.translate("PKCS11", "Unable to receive data from hardware security module");
        }else if (wrongPassword){        
            return messageProvider.translate("PKCS11", "Wrong password to hardware security module");
        }else{            
            return messageProvider.translate("PKCS11", "Unable to receive data from hardware security module.\nCheck device availability");
        }
    }

    @Override
    public String getDetailMessage(final MessageProvider messageProvider) {
        if (providerException!=null){
            return providerException;
        }else{
            return wrongPassword || getCause()==null ? "" : getCause().getMessage();
        }
    }       
}
