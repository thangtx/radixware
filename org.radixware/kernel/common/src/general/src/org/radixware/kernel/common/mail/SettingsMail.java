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

import org.radixware.kernel.common.mail.enums.EAuthentication;
import org.radixware.kernel.common.mail.enums.ESecureConnection;

public abstract class SettingsMail {

    protected String host;
    protected int port;
    protected ESecureConnection secureConnection;
    protected EAuthentication authentication;
    protected String user;
    protected boolean rememberPassword;
    protected boolean cryptPassword;
    protected String password;

    protected SettingsMail() {
        secureConnection = ESecureConnection.NONE;
        authentication = EAuthentication.NONE;
        rememberPassword = false;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ESecureConnection getSecureconnection() {
        return secureConnection;
    }

    public void setSecureconnection(ESecureConnection secureConnection) {
        this.secureConnection = secureConnection;
    }
    
    public EAuthentication getAuthentiication() {
        return authentication;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberPassword() {
        return rememberPassword;
    }
}
