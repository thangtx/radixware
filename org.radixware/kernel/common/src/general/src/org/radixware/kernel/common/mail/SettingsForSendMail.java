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
package org.radixware.kernel.common.mail;

import java.util.prefs.Preferences;
import org.radixware.kernel.common.mail.enums.EMailAuthentication;
import org.radixware.kernel.common.mail.enums.EMailSecureConnection;

public class SettingsForSendMail extends SettingsMail {

    private static final String PREFERENCE_NAME_DEFAULT_HOST = "SettingsForSendMail.host";
    private static final String PREFERENCE_NAME_DEFAULT_PORT = "SettingsForSendMail.port";
    private static final String PREFERENCE_NAME_DEFAULT_SECURE_CONNECTION = "SettingsForSendMail.secureConnection";
    private static final String PREFERENCE_NAME_DEFAULT_AUTHENTICATION = "SettingsForSendMail.authentication";
    private static final String PREFERENCE_NAME_DEFAULT_USER = "SettingsForSendMail.user";
    private static final String PREFERENCE_NAME_DEFAULT_REMEMBER_PASSWORD = "SettingsForSendMail.rememberPassword";
    private static final String PREFERENCE_NAME_DEFAULT_PASSWORD = "SettingsForSendMail.password";
    private static final String PREFERENCE_NAME_DEFAULT_CRYPT_PASSWORD = "SettingsForSendMail.cryptPassword";
    private static final String PREFERENCE_NAME_DEFAULT_SENDER_ADDRESS = "SettingsForSendMail.sender";
    String defaultSenderAddress;

    public static final SettingsForSendMail getInstance() {
        SettingsForSendMail settings = new SettingsForSendMail();
        settings.host = Preferences.userNodeForPackage(SettingsForSendMail.class).get(PREFERENCE_NAME_DEFAULT_HOST, settings.host);
        settings.port = Preferences.userNodeForPackage(SettingsForSendMail.class).getInt(PREFERENCE_NAME_DEFAULT_PORT, settings.port);
        settings.secureConnection = EMailSecureConnection.getForValue(Preferences.userNodeForPackage(SettingsForSendMail.class).get(PREFERENCE_NAME_DEFAULT_SECURE_CONNECTION, settings.secureConnection.getValue()));
        settings.authentication = EMailAuthentication.getForValue(Preferences.userNodeForPackage(SettingsForSendMail.class).get(PREFERENCE_NAME_DEFAULT_AUTHENTICATION, settings.authentication.getValue()));
        if (settings.authentication != EMailAuthentication.NONE) {
            settings.user = Preferences.userNodeForPackage(SettingsForSendMail.class).get(PREFERENCE_NAME_DEFAULT_USER, settings.user);
            settings.rememberPassword = Preferences.userNodeForPackage(SettingsForSendMail.class).getBoolean(PREFERENCE_NAME_DEFAULT_REMEMBER_PASSWORD, settings.rememberPassword);
            if (settings.rememberPassword) {
                settings.password = Preferences.userNodeForPackage(SettingsForSendMail.class).get(PREFERENCE_NAME_DEFAULT_PASSWORD, settings.password);
                settings.cryptPassword = Preferences.userNodeForPackage(SettingsForSendMail.class).getBoolean(PREFERENCE_NAME_DEFAULT_CRYPT_PASSWORD, false);
                if (settings.password != null && settings.cryptPassword) {
                    settings.password = MailSecuritySettings.decrypt(settings.password);
                }
            }
        }
        settings.defaultSenderAddress = Preferences.userNodeForPackage(SettingsForSendMail.class).get(PREFERENCE_NAME_DEFAULT_SENDER_ADDRESS, settings.defaultSenderAddress);
        return settings;
    }

    public SettingsForSendMail() {
        port = 25;
        defaultSenderAddress = "";
    }

    public String getDefaultSenderAddress() {
        return defaultSenderAddress;
    }

    public void storeHost(String host) {
        this.host = host;
        store(SettingsForSendMail.PREFERENCE_NAME_DEFAULT_HOST, host);
    }

    public void storePort(int port) {
        this.port = port;
        store(SettingsForSendMail.PREFERENCE_NAME_DEFAULT_PORT, port);
    }

    public void storeSecureConnection(EMailSecureConnection secureConnection) {
        this.secureConnection = secureConnection;
        store(SettingsForSendMail.PREFERENCE_NAME_DEFAULT_SECURE_CONNECTION, secureConnection.getValue());
    }

    public void storeAuthentication(EMailAuthentication authentication) {
        this.authentication = authentication;
        store(SettingsForSendMail.PREFERENCE_NAME_DEFAULT_AUTHENTICATION, authentication.getValue());
    }

    public void storeUser(String user) {
        this.user = user;
        store(SettingsForSendMail.PREFERENCE_NAME_DEFAULT_USER, user);
    }

    public void storeRememberPassword(boolean rememberPassword) {
        this.rememberPassword = rememberPassword;
        store(SettingsForSendMail.PREFERENCE_NAME_DEFAULT_REMEMBER_PASSWORD, rememberPassword);
        if (!rememberPassword) {
            removeStorage(SettingsForSendMail.PREFERENCE_NAME_DEFAULT_PASSWORD);
        }
    }

    public void storePassword(String password) {
        this.password = password;
        password = MailSecuritySettings.encrypt(password);
        store(PREFERENCE_NAME_DEFAULT_CRYPT_PASSWORD, true);
        store(SettingsForSendMail.PREFERENCE_NAME_DEFAULT_PASSWORD, password);
    }

    public void storeDefaultSenderAddress(String defaultSenderAddress) {
        this.defaultSenderAddress = defaultSenderAddress;
        store(SettingsForSendMail.PREFERENCE_NAME_DEFAULT_SENDER_ADDRESS, defaultSenderAddress);
    }

    private void store(String key, String value) {
        final Preferences node = Preferences.userNodeForPackage(SettingsForSendMail.class);
        if (value != null) {
            node.put(key, value);
        } else {
            removeStorage(key);
        }
    }

    private void store(String key, Boolean value) {
        final Preferences node = Preferences.userNodeForPackage(SettingsForSendMail.class);
        if (value != null) {
            node.putBoolean(key, value);
        } else {
            removeStorage(key);
        }
    }

    private void store(String key, Integer value) {
        final Preferences node = Preferences.userNodeForPackage(SettingsForSendMail.class);
        if (value != null) {
            node.putInt(key, value);
        } else {
            removeStorage(key);
        }
    }

    private void removeStorage(String key) {
        final Preferences node = Preferences.userNodeForPackage(SettingsForSendMail.class);        
        node.remove(key);        
    }
}
