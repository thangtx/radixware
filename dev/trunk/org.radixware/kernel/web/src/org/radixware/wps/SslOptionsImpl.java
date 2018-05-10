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

import org.radixware.kernel.common.client.eas.connections.ConnectionOptions;
import org.radixware.kernel.common.enums.EKeyStoreType;
import org.radixware.schemas.connections.ConnectionsDocument;

public class SslOptionsImpl extends ConnectionOptions.SslOptions {

    public SslOptionsImpl(SslOptionsImpl sslOptions) {
        super(sslOptions);
    }
    
    public SslOptionsImpl(String trustStoreFilePath, boolean trustStorePathIsRelative, char[] trustStorePassword, boolean useSSLAuth) {
        super(trustStoreFilePath, trustStorePathIsRelative, trustStorePassword, useSSLAuth);
    }
    
    public SslOptionsImpl(ConnectionsDocument.Connections.Connection.SSLOptions sslOptions) {
        super(sslOptions);
    }
    
    @Override
    public EKeyStoreType getKeyStoreType() {
        return null;
    }
    
    @Override
    public ConnectionOptions.SslOptions copy() {
        return new SslOptionsImpl(this);
    }
    
}
