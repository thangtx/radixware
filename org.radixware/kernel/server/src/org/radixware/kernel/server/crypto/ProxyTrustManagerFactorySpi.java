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

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.ArrayList;
import java.util.Collections;
import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactorySpi;
import org.apache.commons.lang.StringUtils;
import org.radixware.kernel.common.exceptions.CertificateUtilsException;
import org.radixware.kernel.common.ssl.CertificateUtils;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.DebugLog;

public class ProxyTrustManagerFactorySpi extends TrustManagerFactorySpi {
    
    private ArrStr aliases = null;

    @Override
    protected void engineInit(KeyStore ks) throws KeyStoreException {
        final ArrayList<String> aliasesList = Collections.list(ks.aliases());
        final boolean nullAliases = aliasesList.equals(ProxyCryptoProvider.NULL_ALIASES);
        aliases = nullAliases ? null : new ArrStr(aliasesList);
        LocalTracer.debugNonSensitive("ProxyTrustManagerFactorySpi.engineInit(): aliases = " + StringUtils.join(aliases, ";"));
    }

    @Override
    protected void engineInit(ManagerFactoryParameters mfp) throws InvalidAlgorithmParameterException {
    }

    @Override
    protected TrustManager[] engineGetTrustManagers() {
        DebugLog.funcIn("ProxyTrustManagerFactorySpi.engineGetTrustManagers");
        TrustManager[] managers = new TrustManager[0];
        DebugLog.log("aliases = " + StringUtils.join(aliases, ";"));
        
        try {
            managers = CertificateUtils.createServerTrustManagers(aliases);
        } catch (CertificateUtilsException ex) {
            LocalTracer.debugNonSensitive("ProxyTrustManagerFactorySpi.engineGetTrustManagers() CertificateUtilsException:\n" + ex);
            DebugLog.log("CertificateUtilsException:\n" + ex);
        }
        LocalTracer.debugNonSensitive("ProxyTrustManagerFactorySpi.engineGetTrustManagers(): managers.length = " + managers.length);
        DebugLog.funcOut("return managers (length = " + managers.length + ")");
        return managers;
    }
}
