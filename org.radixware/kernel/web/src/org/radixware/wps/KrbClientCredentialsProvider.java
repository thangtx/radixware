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

package org.radixware.wps;

import java.util.HashMap;
import java.util.Map;
import javax.security.auth.callback.CallbackHandler;
import org.ietf.jgss.GSSCredential;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.IKerberosCredentialsProvider;
import org.radixware.kernel.common.client.errors.KerberosError;
import org.radixware.kernel.common.kerberos.KerberosCredentials;
import org.radixware.kernel.common.kerberos.KerberosException;
import org.radixware.kernel.common.kerberos.KerberosLoginConfiguration;


final class KrbClientCredentialsProvider implements IKerberosCredentialsProvider {
    
    private final CallbackHandler loginCallbackHandler;
    private final Map<String,String> loginConfigParameters = new HashMap<>(8);
    private final String cpn;
    private String spn;
    private final boolean directAuth;
    private final GSSCredential delegCred;
    
    KrbClientCredentialsProvider(final WpsEnvironment environment, final String userName, final char[] password){
        loginCallbackHandler = new KrbLoginCallbackHandler(environment, password);
        loginConfigParameters.put("useTicketCache", "false");
        loginConfigParameters.put("renewTGT","false");
        loginConfigParameters.put("principal", userName);
        cpn = userName;
        directAuth = true;
        delegCred = null;
    }
    
    KrbClientCredentialsProvider(final WebServerRunParams.KrbWpsOptions options, final GSSCredential delegCred){
        loginCallbackHandler = null;        
        cpn = options.getKerberosPrincipal().getName();
        this.delegCred = delegCred;
        directAuth = delegCred!=null;
        final String keyTab = options.getKeyTabPath();
        loginConfigParameters.put("isInitiator", "true");
        loginConfigParameters.put("useKeyTab", "true");
        if (keyTab!=null && !keyTab.isEmpty()){
            loginConfigParameters.put("keyTab", keyTab);
        }
        loginConfigParameters.put("useTicketCache", "false");
        loginConfigParameters.put("principal", cpn);        
    }
    
    public void setServicePrincipalName(final String spn){
        this.spn = spn;
    }

    @Override
    public KerberosCredentials createCredentials(IClientEnvironment environment) throws InterruptedException, KerberosError {
        final KerberosLoginConfiguration loginConfig = 
            KerberosLoginConfiguration.Factory.newInstance("Wps", loginConfigParameters, loginCallbackHandler);
        try{
            if (delegCred==null){
                return KerberosCredentials.Factory.newClientCredentials(cpn,
                                                                            spn,
                                                                            loginConfig,
                                                                            GSSCredential.DEFAULT_LIFETIME);
            }else{
                return KerberosCredentials.Factory.wrapDelegatedCredential(spn, loginConfig, delegCred);
            }
        }catch(KerberosException exception){
            throw new KerberosError(exception);
        }
    }
    
    public String getUserName(){
        return cpn;
    }
    
    public boolean isDirectAuthentication(){
        return directAuth;
    }
}
