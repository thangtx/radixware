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

package org.radixware.kernel.starter.utils;

import java.util.HashMap;
import javax.security.auth.login.AppConfigurationEntry;


public class KerberosConfig extends javax.security.auth.login.Configuration {

    static private HashMap<String, String> params;

    public static void setRealm(String realm) {
        if (realm != null && realm.length() != 0) {
            java.lang.System.setProperty("java.security.krb5.realm", realm);
        } else {
            java.lang.System.clearProperty("java.security.krb5.realm");
        }
    }

    public static void setKdc(String kdc) {
        if (kdc != null && kdc.length() != 0) {
            java.lang.System.setProperty("java.security.krb5.kdc", kdc);
        } else {
            java.lang.System.clearProperty("java.security.krb5.kdc");
        }
    }

    public static void setConfiguration(HashMap<String, String> params) {
        if (params != null && !params.isEmpty()) {
            setParams(params);
            java.security.Security.setProperty("login.configuration.provider", "org.radixware.kernel.starter.utils.KerberosConfig");
            java.security.Security.setProperty("auth.login.defaultCallbackHandler", "com.sun.security.auth.callback.DialogCallbackHandler");
        }
    }

    static void setParams(HashMap<String, String> params) {
        KerberosConfig.params = params;
    }

    public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
        if (name.equals("com.sun.security.jgss.krb5.initiate")) {
            HashMap<String, String> map = new HashMap<String, String>();
            for (String key : params.keySet()) {
                map.put(key, params.get(key));
            }
            return new AppConfigurationEntry[]{
                new AppConfigurationEntry("com.sun.security.auth.module.Krb5LoginModule", AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, map)};
        }
        return null;
    }
}
