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
package org.radixware.kernel.common.svn;

import java.util.HashMap;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;
import org.radixware.kernel.common.enums.ESvnAuthType;
import org.radixware.kernel.common.kerberos.KerberosConfig;
import org.radixware.kernel.common.kerberos.PreferencesNames;
import org.radixware.kernel.common.svn.client.SvnAuthType;

public class SVN extends org.radixware.kernel.common.svn.utils.SVN {

    static {
        // Kerberos setup
        setKerberosRealm();
        setKerberosKDC();
        setKerberosConfiguration();
        Preferences.userNodeForPackage(SVN.class).addPreferenceChangeListener(new PreferenceChangeListener() {
            @Override
            public void preferenceChange(PreferenceChangeEvent evt) {
                if (PreferencesNames.PREF_KERBEROS_REALM.equals(evt.getKey())) {
                    setKerberosRealm();
                } else if (PreferencesNames.PREF_KERBEROS_KDC.equals(evt.getKey())) {
                    setKerberosKDC();
                } else if (PreferencesNames.PREF_KERBEROS_USE_TICKET_CACHE.equals(evt.getKey())) {
                    setKerberosConfiguration();
                }
            }
        });
    }

    public static void updatePreferences() {
        setKerberosRealm();
        setKerberosKDC();
        setKerberosConfiguration();
    }

    private static void setKerberosRealm() {
        KerberosConfig.setRealm(Preferences.userNodeForPackage(SVN.class).get(PreferencesNames.PREF_KERBEROS_REALM, ""));
    }

    private static void setKerberosKDC() {
        KerberosConfig.setKdc(Preferences.userNodeForPackage(SVN.class).get(PreferencesNames.PREF_KERBEROS_KDC, ""));
    }

    private static void setKerberosConfiguration() {
        String use_ticket_cache = Preferences.userNodeForPackage(SVN.class).get(PreferencesNames.PREF_KERBEROS_USE_TICKET_CACHE, Boolean.TRUE.toString());
        HashMap<String, String> krb_params = new HashMap<String, String>();
        krb_params.put("useTicketCache", use_ticket_cache);
        KerberosConfig.setConfiguration(krb_params);
    }

    public static SvnAuthType getForAuthType(ESvnAuthType authType) {
        if (authType == null) {
            return null;
        }
        switch (authType) {
            case NONE:
                return SvnAuthType.NONE;
            case SSH_KEY_FILE:
                return SvnAuthType.SSH_KEY_FILE;
            case SSH_PASSWORD:
                return SvnAuthType.SSH_PASSWORD;
            case SSL:
                return SvnAuthType.SSL;
            case SVN_PASSWORD:
                return SvnAuthType.SVN_PASSWORD;
            default:
                return null;
        }
    }

    public static ESvnAuthType getByAuthType(SvnAuthType authType) {
        if (authType == null) {
            return null;
        }
        switch (authType) {
            case NONE:
                return ESvnAuthType.NONE;
            case SSH_KEY_FILE:
                return ESvnAuthType.SSH_KEY_FILE;
            case SSH_PASSWORD:
                return ESvnAuthType.SSH_PASSWORD;
            case SSL:
                return ESvnAuthType.SSL;
            case SVN_PASSWORD:
                return ESvnAuthType.SVN_PASSWORD;
            default:
                return null;
        }
    }
}
