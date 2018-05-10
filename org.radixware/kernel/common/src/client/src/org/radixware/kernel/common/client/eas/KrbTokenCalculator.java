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

package org.radixware.kernel.common.client.eas;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.MessageProp;
import org.radixware.kernel.common.auth.AuthUtils;
import org.radixware.kernel.common.auth.PasswordHash;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.errors.CredentialsWasNotDefinedError;
import org.radixware.kernel.common.client.errors.KerberosError;
import org.radixware.kernel.common.client.utils.ISecretStore;
import org.radixware.kernel.common.client.utils.TokenProcessor;
import org.radixware.kernel.common.kerberos.KerberosCredentials;
import org.radixware.kernel.common.kerberos.KerberosException;


class KrbTokenCalculator implements ITokenCalculator {        

    private GSSContext context = null;    
    private final KerberosCredentials krbCreds;
    private final ISpnegoGssTokenProvider authDelegate;
    private final ISecretStore keyStorage;
    private final IClientEnvironment environment;

    public KrbTokenCalculator(final KerberosCredentials krbCreds, 
                              final ISpnegoGssTokenProvider authDelegate,
                              final IClientEnvironment environment) {
        this.krbCreds = krbCreds;
        this.authDelegate = authDelegate;
        this.keyStorage = environment.getApplication().newSecretStore();
        this.environment = environment;       
    }
    
    private void createGSSContext() throws KerberosException{        
        if (context!=null){
            try{
                context.dispose();
                context=null;
            }catch(GSSException ex){
                context=null;
            }
        }
        context = krbCreds.createSecurityContext();
    }
    
    public final void renewKerberosCredentials() throws KerberosException{
        while(true){
            try{
                krbCreds.renew();
                break;
            }catch(KerberosException exception){
                if (exception.getKerberosReturnCode()==KerberosException.KRB_AP_ERR_BAD_INTEGRITY ||
                    exception.getKerberosReturnCode()==KerberosException.KDC_ERR_PREAUTH_FAILED){
                    final String title = 
                        environment.getMessageProvider().translate("ExplorerError", "Unable to identify the user");
                    environment.messageError(title, environment.getMessageProvider().translate("ExplorerError", "Password is Invalid!"));
                }else if ("Cancelled by user".equals(exception.getMessage())){
                    throw new CredentialsWasNotDefinedError();
                }else{
                    throw exception;
                }
            }
        }
    }
    
    private void processExceptionOnCreateContext(final KerberosException exception) throws KerberosError{
        if (exception.getKerberosReturnCode()==KerberosException.KRB_AP_ERR_TKT_EXPIRED){
            try{
                renewKerberosCredentials();
                createGSSContext();
            }catch(KerberosException newException){
                throw new KerberosError(newException, krbCreds);
            }
        }else{
            throw new KerberosError(exception,krbCreds);
        }
    }
    
    private byte[] processExceptionOnGetInitialToken(final KerberosException exception) throws KerberosError{        
        if (exception.getKerberosReturnCode()==KerberosException.KRB_AP_ERR_TKT_EXPIRED){
            try{
                renewKerberosCredentials();
                createGSSContext();
                return calcHandshakeToken(new byte[]{});                
            }catch(GSSException | KerberosException newException){
                throw new KerberosError(newException, krbCreds);
            }
        }else{
            throw new KerberosError(exception, krbCreds);
        }
    }
    
    public byte[] decrypt(final byte[] encData) throws GSSException{
        return context.unwrap(encData, 0, encData.length, new MessageProp(true));
    }

    @Override
    public SecurityToken calcToken(final byte[] inToken) {
        if (inToken == null || inToken.length == 0 || context==null) {
            if (authDelegate!=null){
                authDelegate.asyncRequestForInitialGssToken();
            }
            try {
                createGSSContext();
            } catch (KerberosException exception) {
                processExceptionOnCreateContext(exception);
            }
        }
        final byte[] resultToken;
        final byte[] encKey;
        if (context.isEstablished()) {
            if (authDelegate==null){
                try {
                    resultToken = context.wrap(inToken, 0, inToken.length, new MessageProp(true));
                } catch (GSSException exception) {
                    throw new KerberosError(exception, krbCreds);
                }
            }else{
                resultToken = calcSpnegoToken(inToken);
                if (resultToken==null){
                    return null;
                }
            }
            return new SecurityToken(resultToken);
        } else {
            try {
                resultToken = calcHandshakeToken(inToken);
            } catch (KerberosException exception) {
                if (inToken==null || inToken.length==0){
                    return new SecurityToken(processExceptionOnGetInitialToken(exception));
                }else{
                    throw new KerberosError(exception, krbCreds);
                }
            } catch (GSSException exception){
                throw new KerberosError(exception, krbCreds);
            }
            if (context.isEstablished()){
                final byte[] rawKey = new byte[16];
                new SecureRandom().nextBytes(rawKey);
                try{
                    encKey = context.wrap(rawKey, 0, rawKey.length, new MessageProp(true));
                }catch(GSSException exception){
                    throw new KerberosError(exception, krbCreds);    
                }
                keyStorage.setSecret(new TokenProcessor().encrypt(rawKey));
                Arrays.fill(rawKey, (byte)0);
                if (authDelegate==null){
                    return new SecurityToken(null, encKey);
                }
                final byte[] spnegoToken = calcSpnegoToken(null);
                return spnegoToken==null ? null : new SecurityToken(spnegoToken , encKey);
            }else{
                return new SecurityToken(resultToken, null);
            }            
        }
    }
    
    ISecretStore getKeyStorage(){
        return keyStorage;
    }
    
    private byte[] calcSpnegoToken(final byte[] inToken){
        final byte[] outToken = authDelegate.receiveGssToken(inToken);
        if (outToken==null){
            return null;
        }
        try {
            return context.wrap(outToken, 0, outToken.length, new MessageProp(true));
        } catch (GSSException exception) {
            throw new KerberosError(exception, krbCreds);
        }        
    }
    
    private byte[] calcHandshakeToken(final byte[] inToken) throws KerberosException, GSSException{        
        final byte[] outToken = krbCreds.getNextHandshakeToken(context, inToken);
        if (context.isEstablished()) {
            return context.wrap(inToken, 0, inToken.length, new MessageProp(true));
        } else {
            return outToken;
        }        
    }

    @Override
    public byte[] createEncryptedHashForNewPassword(final PasswordHash newPwdHash) {
        try {
            final byte[] hashData = newPwdHash.export();
            try{
                return context.wrap(hashData, 0, hashData.length, new MessageProp(true));
            }finally{
                Arrays.fill(hashData, (byte)0);
            }
        } catch (GSSException exception) {
            throw new KerberosError(exception, krbCreds);
        }            
    }
    
    public void dispose(final boolean disposeDelegatedCredentials){
        try {
            context.dispose();
        } catch (GSSException ex) {
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
        }
        if (!krbCreds.isCredentialsDelegated() || disposeDelegatedCredentials){
            krbCreds.dispose();
        }
        if (authDelegate!=null){
            authDelegate.completeAuthentication();
        }        
    }

    @Override
    public void dispose() {
        dispose(true);
    }

    @Override
    public ITokenCalculator copy(final IClientEnvironment environment) {
        return new KrbTokenCalculator(krbCreds,authDelegate,environment);
    }
}
