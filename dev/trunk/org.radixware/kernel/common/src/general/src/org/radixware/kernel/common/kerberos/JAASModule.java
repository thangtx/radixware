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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;


public abstract class JAASModule {

    public static final String KRB_IS_INITIATOR = "isInitiator";
    public static final String KRB_STORE_KEY = "storeKey";
    public static final String KRB_USE_KEY_TAB = "useKeyTab";
    public static final String KRB_KEY_TAB = "keyTab";
    public static final String KRB_PRINCIPAL = "principal";
    public static final String KRB_USE_TICKET_CACHE = "useTicketCache";
    private Map<String, String> params;
    private final String moduleName;
    private LoginContext loginContext;

    private class DefaultCallbackHandler implements CallbackHandler {

        @Override
        public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
            for (Callback c : callbacks) {
                if (c instanceof PasswordCallback) {
                    ((PasswordCallback) c).setPassword(getPassword());
                } else if (c instanceof NameCallback) {
                    ((NameCallback) c).setName(getPrincipalName());
                }
            }
        }
    }

    public JAASModule(String moduleName, Map<String, String> parameters) {
        this.moduleName = moduleName;
        if (parameters != null) {
            params = new HashMap<>();
            params.putAll(parameters);
        }
    }

    public String getParamValue(String key) {
        if (params == null) {
            return null;
        } else {
            return params.get(key);
        }
    }

    public void setParamValue(String key, String value) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put(key, value);
    }

    protected Oid createKrbOid() throws GSSException {
        return new Oid("1.2.840.113554.1.2.2");
    }

    protected String getParamString() {
        StringBuilder content = new StringBuilder();
        content.append(moduleName).append("{\n");
        content.append("   ").
                append("com.sun.security.auth.module.Krb5LoginModule\n").
                append("required\n");

        if (params != null) {
            for (Map.Entry<String, String> e : params.entrySet()) {
                content.append("    ").
                        append(e.getKey()).
                        append('"').
                        append(e.getValue()).
                        append('"').
                        append("\n");
            }
        }

        content.append("};\n");
        return content.toString();
    }

    public void login() throws LoginException {
        if (loginContext == null) {
            File[] cfg = new File[]{null};
            String oldConfigValue = prepareSystemSettings(cfg);
            try {
                loginContext = new LoginContext(moduleName, new DefaultCallbackHandler());
                loginContext.login();
            } finally {
                System.setProperty("java.security.auth.login.config", oldConfigValue);
                if (cfg[0] != null) {
                    cfg[0].delete();
                }
            }
        }
    }

    private String prepareSystemSettings(File[] configFile) {
        String oldValue = System.getProperty("java.security.auth.login.config");
        try {
            configFile[0] = File.createTempFile("rdx", "rdx");
            try (FileOutputStream fs = new FileOutputStream(configFile[0])) {
                fs.write(getParamString().getBytes("UTF-8"));
                fs.flush();
            }
            System.setProperty("java.security.auth.login.config", configFile[0].getAbsolutePath());
        } catch (IOException e) {
            Logger.getLogger(JAASModule.class.getName()).log(Level.WARNING, "Can not store JAAS configuration", e);
        }
        return oldValue;
    }

    public void logout() throws LoginException {
        if (loginContext != null) {
            loginContext.logout();
        }
    }

    public Subject getSubject() throws LoginException {
        if (loginContext == null) {
            login();
        }
        return loginContext.getSubject();
    }

    public abstract char[] getPassword();

    public abstract String getPrincipalName();
}
