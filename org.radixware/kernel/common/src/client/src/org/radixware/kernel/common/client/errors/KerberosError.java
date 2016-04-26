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

package org.radixware.kernel.common.client.errors;

import java.util.Arrays;
import java.util.List;
import javax.security.auth.login.LoginException;
import org.ietf.jgss.GSSException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.kerberos.KerberosCredentials;
import org.radixware.kernel.common.kerberos.KerberosException;


public final class KerberosError extends AuthError implements IClientError{
        
    static final long serialVersionUID = 3035988412737034108L;
    
    private static final List<Integer> KNOWN_ERRORS = Arrays.asList(KerberosException.KRB_AP_ERR_GEN_CRED,
                                                                    KerberosException.KDC_ERR_S_PRINCIPAL_UNKNOWN,
                                                                    KerberosException.KRB_AP_ERR_TKT_EXPIRED,
                                                                    KerberosException.KRB_AP_ERR_BAD_INTEGRITY,
                                                                    KerberosException.KDC_ERR_PREAUTH_FAILED,
                                                                    KerberosException.KDC_ERR_C_PRINCIPAL_UNKNOWN
                                                                   );
    
    private final String spn;
    private final int krbReturnCode;
    
    private KerberosError(final Exception exception, final String spn){
        super(null,exception);        
        this.spn = spn;
        krbReturnCode = findKerberosReturnCode(exception);
    }
    
    public KerberosError(final Exception exception, final KerberosCredentials krbCreds){
        this(exception, krbCreds==null ? "" : krbCreds.getRemotePrincipalName());
    }
    
    public KerberosError(final Exception exception){
        this(exception, "");
    }
    
    private static int findKerberosReturnCode(final Exception exception){
        for (Throwable ex=exception; ex!=null; ex=ex.getCause()){
            if (ex instanceof KerberosException){
                return ((KerberosException)ex).getKerberosReturnCode();
            }else if (ex instanceof GSSException){
                return new KerberosException((GSSException)ex).getKerberosReturnCode();
            }else if (ex instanceof LoginException){
                return new KerberosException((LoginException)ex).getKerberosReturnCode();
            }
        }
        return -1;
    }
    
        
    @Override
    public String getTitle(final MessageProvider messageProvider) {        
        return messageProvider.translate("ExplorerMessage", "Can't establish connection");
    }

    @Override
    public String getLocalizedMessage(final MessageProvider messageProvider) {
        switch (krbReturnCode){
            case KerberosException.KRB_AP_ERR_GEN_CRED:
            case KerberosException.KDC_ERR_S_PRINCIPAL_UNKNOWN:{
                final String message = 
                    messageProvider.translate("ExplorerError", "Unable to identify the user.\nService with principal name \"%1s\" is not registered");
                return String.format(message, spn);
            }
            case KerberosException.KRB_AP_ERR_TKT_EXPIRED:{
                final String message = 
                    messageProvider.translate("ExplorerError", "Unable to identify the user.\nAuthentication information was expired. You must to reconnect");
                return message;
            }
            case KerberosException.KRB_AP_ERR_BAD_INTEGRITY:
            case KerberosException.KDC_ERR_PREAUTH_FAILED:
            case KerberosException.KDC_ERR_C_PRINCIPAL_UNKNOWN:{
                return messageProvider.translate("ExplorerError", "Authentication information is invalid");
            }                
            default:
                return messageProvider.translate("ExplorerError", "Unable to identify the user");
        }
    }

    @Override
    public String getDetailMessage(final MessageProvider messageProvider) {
        return KNOWN_ERRORS.contains(krbReturnCode) ? "" : getLocalizedMessage(messageProvider);
    }
    
    public int getKerberosReturnCode(){
        return krbReturnCode;
    }
}
