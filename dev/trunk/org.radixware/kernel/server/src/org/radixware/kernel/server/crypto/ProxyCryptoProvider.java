/*
 * Copyright (c) 2008-2017, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.crypto;

import java.security.Provider;
import java.security.Security;
import org.radixware.kernel.common.types.ArrStr;

public class ProxyCryptoProvider extends Provider {
    private static final String ID = "RadixProxyCryptoProvider";
    private static final ProxyCryptoProvider PROVIDER = new ProxyCryptoProvider();
    private static final String NULL_ALIAS = new String(new byte[16]);
    static final ArrStr NULL_ALIASES = new ArrStr(NULL_ALIAS);
    
    public static final String getProxyCryptoProviderId() {
        return ID;
    }
    
    static {
        PROVIDER.putService(new Provider.Service(PROVIDER, "TrustManagerFactory", ID, ProxyTrustManagerFactorySpi.class.getCanonicalName(), null, null));
        PROVIDER.putService(new Provider.Service(PROVIDER, "KeyManagerFactory", ID, ProxyKeyManagerFactorySpi.class.getCanonicalName(), null, null));
        PROVIDER.putService(new Provider.Service(PROVIDER, "KeyStore", ID, ProxyKeyStoreSpi.class.getCanonicalName(), null, null));
        Security.addProvider(PROVIDER);
    }
    
    private ProxyCryptoProvider() {
        super(ID, 1.0, "To restrict available key material by aliases for provider clients");
    }
}
