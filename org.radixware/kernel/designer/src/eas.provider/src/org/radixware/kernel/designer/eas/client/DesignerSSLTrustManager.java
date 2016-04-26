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

package org.radixware.kernel.designer.eas.client;

import java.security.cert.X509Certificate;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.AbstractSslTrustManager;
import org.radixware.kernel.common.client.exceptions.SslTrustManagerException;


public class DesignerSSLTrustManager extends AbstractSslTrustManager {

    public DesignerSSLTrustManager(IClientEnvironment environment, String trustStorePath, X509Certificate clientCertificate) {
        super(environment, trustStorePath, clientCertificate);
    }

    @Override
    protected Confirmation confirm(X509Certificate xc, ConfirmationReason cr) throws SslTrustManagerException {
        return Confirmation.ACCEPT;
    }
}
