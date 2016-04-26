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

import java.util.HashMap;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;


public class KerberosLoginConfiguration extends javax.security.auth.login.Configuration {
    
    private final Map<String,String> parameters = new HashMap<>();
    private final String sectionName;
    private final CallbackHandler callbackHandler;
    private LoginContext loginContext;
    
    private KerberosLoginConfiguration(final String sectionName, 
                                       final Map<String,String> paremeters, 
                                       final CallbackHandler callbackHandler
                                      ){
        this.parameters.putAll(paremeters);
        this.sectionName = sectionName;
        this.callbackHandler = callbackHandler;
    }
    
    public static class Factory{
        private Factory(){
            
        }
        
        public static KerberosLoginConfiguration newInstance(final String sectionName, 
                                                             final Map<String,String> parameters, 
                                                             final CallbackHandler callbackHandler){
            return new KerberosLoginConfiguration(sectionName, parameters, callbackHandler);
        }
        
        public static KerberosLoginConfiguration newInstance(final String sectionName, final Map<String,String> parameters){
            return new KerberosLoginConfiguration(sectionName, parameters, null);
        }                
    }

    @Override
    public AppConfigurationEntry[] getAppConfigurationEntry(final String name) {
        if (sectionName.equals(name)){
            Map<String, String> map = new HashMap<>();
            map.putAll(parameters);
            return new AppConfigurationEntry[] {
                new AppConfigurationEntry("com.sun.security.auth.module.Krb5LoginModule", AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, map) };
        }
        return null;
    }
    
    public Subject doLogin(final boolean useSubjectCredsOnly) throws LoginException{
        if (loginContext!=null){
            doLogout();
        }
        System.setProperty("javax.security.auth.useSubjectCredsOnly", String.valueOf(useSubjectCredsOnly));
        if (callbackHandler==null){
            loginContext = new LoginContext(sectionName,null,null,this);
        }else{
            loginContext = new LoginContext(sectionName,null,callbackHandler,this);
        }
        loginContext.login();
        return loginContext.getSubject();
    }
    
    public void doLogout() throws LoginException{
        if (loginContext!=null){
            loginContext.logout();
            loginContext = null;
        }
    }
}