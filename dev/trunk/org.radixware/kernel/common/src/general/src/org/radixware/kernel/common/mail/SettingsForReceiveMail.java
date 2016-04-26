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

import java.io.File;

public class SettingsForReceiveMail extends SettingsMail {

    public SettingsForReceiveMail() {
        port = 110;
    }

    String login;
    String pwd;
    String tempFilePrefix;
    File tempDir;

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return pwd;
    }

    public File getTempDir() {
        return tempDir;
    }

    public void setTempDir(File tempDir) {
        this.tempDir = tempDir;
    }

    public String getTempFilePrefix() {
        return tempFilePrefix;
    }

    public void setTempFilePrefix(String tempFilePrefix) {
        this.tempFilePrefix = tempFilePrefix;
    }

}
