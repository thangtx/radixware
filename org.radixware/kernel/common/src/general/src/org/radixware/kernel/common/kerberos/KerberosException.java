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

package org.radixware.kernel.common.kerberos;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.security.auth.login.LoginException;
import org.ietf.jgss.GSSException;


public final class KerberosException extends Exception{
    
    private final static String KRB_EXCEPTION_CLASS_NAME = "sun.security.krb5.KrbException";
    private final static String RETURN_CODE_METHOD_NAME = "returnCode";
    
    static final long serialVersionUID = 4142866378200453542L;
    
    //kerberos return codes (RFC-1510)

    public static final int KDC_ERR_NONE                 =  0;   //No error
    public static final int KDC_ERR_NAME_EXP             =  1;   //Client's entry in database expired
    public static final int KDC_ERR_SERVICE_EXP          =  2;   //Server's entry in database has expired
    public static final int KDC_ERR_BAD_PVNO             =  3;   //Requested protocol version number not supported
    public static final int KDC_ERR_C_OLD_MAST_KVNO      =  4;   //Client's key encrypted in old master key
    public static final int KDC_ERR_S_OLD_MAST_KVNO      =  5;   //Server's key encrypted in old master key
    public static final int KDC_ERR_C_PRINCIPAL_UNKNOWN  =  6;   //Client not found in Kerberos database
    public static final int KDC_ERR_S_PRINCIPAL_UNKNOWN  =  7;   //Server not found in Kerberos database
    public static final int KDC_ERR_PRINCIPAL_NOT_UNIQUE =  8;   //Multiple principal entries in database
    public static final int KDC_ERR_NULL_KEY             =  9;   //The client or server has a null key
    public static final int KDC_ERR_CANNOT_POSTDATE      = 10;   //Ticket not eligible for postdating
    public static final int KDC_ERR_NEVER_VALID          = 11;   //Requested start time is later than end time
    public static final int KDC_ERR_POLICY               = 12;   //KDC policy rejects request
    public static final int KDC_ERR_BADOPTION            = 13;   //KDC cannot accommodate requested option
    public static final int KDC_ERR_ETYPE_NOSUPP         = 14;   //KDC has no support for encryption type
    public static final int KDC_ERR_SUMTYPE_NOSUPP       = 15;   //KDC has no support for checksum type
    public static final int KDC_ERR_PADATA_TYPE_NOSUPP   = 16;   //KDC has no support for padata type
    public static final int KDC_ERR_TRTYPE_NOSUPP        = 17;   //KDC has no support for transited type
    public static final int KDC_ERR_CLIENT_REVOKED       = 18;   //Clients credentials have been revoked
    public static final int KDC_ERR_SERVICE_REVOKED      = 19;   //Credentials for server have been revoked
    public static final int KDC_ERR_TGT_REVOKED          = 20;   //TGT has been revoked
    public static final int KDC_ERR_CLIENT_NOTYET        = 21;   //Client not yet valid - try again later
    public static final int KDC_ERR_SERVICE_NOTYET       = 22;   //Server not yet valid - try again later
    public static final int KDC_ERR_KEY_EXPIRED          = 23;   //Password has expired - change password to reset
    public static final int KDC_ERR_PREAUTH_FAILED       = 24;   //Pre-authentication information was invalid
    public static final int KDC_ERR_PREAUTH_REQUIRED     = 25;   //Additional pre-authentication required
    public static final int KRB_AP_ERR_BAD_INTEGRITY     = 31;   //Integrity check on decrypted field failed
    public static final int KRB_AP_ERR_TKT_EXPIRED       = 32;   //Ticket expired
    public static final int KRB_AP_ERR_TKT_NYV           = 33;   //Ticket not yet valid
    public static final int KRB_AP_ERR_REPEAT            = 34;   //Request is a replay
    public static final int KRB_AP_ERR_NOT_US            = 35;   //The ticket isn't for us
    public static final int KRB_AP_ERR_BADMATCH          = 36;   //Ticket and authenticator don't match
    public static final int KRB_AP_ERR_SKEW              = 37;   //Clock skew too great
    public static final int KRB_AP_ERR_BADADDR           = 38;   //Incorrect net address
    public static final int KRB_AP_ERR_BADVERSION        = 39;   //Protocol version mismatch
    public static final int KRB_AP_ERR_MSG_TYPE          = 40;   //Invalid msg type
    public static final int KRB_AP_ERR_MODIFIED          = 41;   //Message stream modified
    public static final int KRB_AP_ERR_BADORDER          = 42;   //Message out of order
    public static final int KRB_AP_ERR_BADKEYVER         = 44;   //Specified version of key is not available
    public static final int KRB_AP_ERR_NOKEY             = 45;   //Service key not available
    public static final int KRB_AP_ERR_MUT_FAIL          = 46;   //Mutual authentication failed
    public static final int KRB_AP_ERR_BADDIRECTION      = 47;   //Incorrect message direction
    public static final int KRB_AP_ERR_METHOD            = 48;   //Alternative authentication method required
    public static final int KRB_AP_ERR_BADSEQ            = 49;   //Incorrect sequence number in message
    public static final int KRB_AP_ERR_INAPP_CKSUM       = 50;   //Inappropriate type of checksum in message
    public static final int KRB_ERR_RESPONSE_TOO_BIG     = 52;   //Response too big for UDP, retry with TCP
    public static final int KRB_ERR_GENERIC              = 60;   //Generic error (description in e-text)
    public static final int KRB_ERR_FIELD_TOOLONG        = 61;   //Field is too long for this implementation
    public static final int KRB_CRYPTO_NOT_SUPPORT       = 100;    //Client does not support this crypto type
    public static final int KRB_AP_ERR_NOREALM           = 62;
    public static final int KRB_AP_ERR_GEN_CRED          = 63;    
    
    final int krbReturnCode;
    final boolean loginException;
    
    public KerberosException(final Exception cause){
        super(cause);
        krbReturnCode = findKrbReturnCode(cause);
        loginException = cause instanceof LoginException;
    }
    
    private static int findKrbReturnCode(final Exception exception){
        final Class krbExceptionClass;
        try{
            krbExceptionClass = Class.forName(KRB_EXCEPTION_CLASS_NAME);            
        }catch(ClassNotFoundException | LinkageError e){//NOPMD
            return -1;
        }
        final Method returnCode;
        try{
            returnCode = krbExceptionClass.getDeclaredMethod(RETURN_CODE_METHOD_NAME);
        }catch(NoSuchMethodException | SecurityException e){//NOPMD
            return -1;
        }
        for (Throwable error=exception; error!=null; error=error.getCause()){            
            if (krbExceptionClass.isInstance(error)){
                final Object invokationResult;
                try{                    
                    invokationResult = returnCode.invoke(error);
                }catch(IllegalArgumentException | IllegalAccessException | InvocationTargetException e){//NOPMD
                    return -1;
                }
                return invokationResult instanceof Integer ? (Integer)invokationResult : -1;
            }            
        }
        return -1;
    }        
    
    public final boolean isLoginException(){
        return loginException;
    }
    
    public final int getKerberosReturnCode(){
        return krbReturnCode;
    }
    
    public final int getGSSMajorCode(){
        return getCause() instanceof GSSException ? ((GSSException)getCause()).getMajor() : -1;
    }
    
    public final int getGSSMinorCode(){
        return getCause() instanceof GSSException ? ((GSSException)getCause()).getMinor() : -1;
    }
    
    public final String getGSSMajorString(){
        return getCause() instanceof GSSException ? ((GSSException)getCause()).getMajorString() : null;
    }

    public final String getGSSMinorString(){
        return getCause() instanceof GSSException ? ((GSSException)getCause()).getMinorString() : null;
    }

    @Override
    public String getMessage() {
        return getCause().getMessage();
    }

    @Override
    public String getLocalizedMessage() {
        return getCause().getLocalizedMessage();
    }                
}
