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

package org.radixware.kernel.designer.eas.dialogs;

import org.radixware.kernel.common.client.eas.IEasClient;
import org.radixware.kernel.common.client.eas.connections.ConnectionOptions;
import org.radixware.kernel.common.exceptions.CertificateUtilsException;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.KeystoreControllerException;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.eas.client.DesignerClientEnvironment;
import org.radixware.kernel.designer.eas.client.DesignerConnections;

import org.radixware.kernel.designer.eas.client.EasClientProvider;


public class LogonDialog {

    private String userName;
    private String password;
    private ConnectionOptions connection;
    private DesignerClientEnvironment env;

    public LogonDialog(DesignerClientEnvironment env) {
        this.env = env;
    }

    public ConnectionOptions getConnection() {
        return connection;
    }

    public void setConnection(ConnectionOptions connection) {
        this.connection = connection;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    DesignerConnections getConnections() {
        return (DesignerConnections) env.getConnections();
    }

    public boolean show() {
        LoginPanel panel = new LoginPanel(this);
        ModalDisplayer displayer = new ModalDisplayer(panel, "User Login");
        if (displayer.showModal()) {
            this.userName = panel.getUserName();
            this.password = panel.getPassword();
            this.connection = panel.getConnection();
            return true;
        } else {
            return false;
        }
    }

    public IEasClient createEasClient() throws IllegalUsageError, KeystoreControllerException, CertificateUtilsException {
        EasClientProvider provider = new EasClientProvider(env, getConnection());
        return provider.createEasClient(getPassword().toCharArray());
    }
}
