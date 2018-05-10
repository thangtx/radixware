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
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactorySpi;
import javax.net.ssl.ManagerFactoryParameters;
import org.apache.commons.lang.StringUtils;
import org.radixware.kernel.common.exceptions.CertificateUtilsException;
import org.radixware.kernel.common.ssl.CertificateUtils;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.DebugLog;

public class ProxyKeyManagerFactorySpi extends KeyManagerFactorySpi {
    
    private ArrStr aliases = null;

    @Override
    protected void engineInit(KeyStore ks, char[] chars) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
        final String pwd = new String(chars);
        aliases = pwd.equals("null") ? null : ArrStr.fromValAsStr(pwd);
        LocalTracer.debugNonSensitive("ProxyKeyManagerFactorySpi.engineInit(): aliases = " + StringUtils.join(aliases, ";"));
    }

    @Override
    protected void engineInit(ManagerFactoryParameters mfp) throws InvalidAlgorithmParameterException {
    }

    @Override
    protected KeyManager[] engineGetKeyManagers() {
        DebugLog.funcIn("ProxyKeyManagerFactorySpi.engineGetKeyManagers");
        KeyManager[] managers = new KeyManager[0];
        DebugLog.log("aliases = " + StringUtils.join(aliases, ";"));
        try {
            managers = CertificateUtils.createServerKeyManagers(aliases);
        } catch (CertificateUtilsException ex) {
            LocalTracer.debugNonSensitive("ProxyKeyManagerFactorySpi.engineGetKeyManagers() CertificateUtilsException:\n" + ex);
            DebugLog.log("CertificateUtilsException:\n" + ex);
        }
        LocalTracer.debugNonSensitive("ProxyKeyManagerFactorySpi.engineGetKeyManagers(): managers.length = " + managers.length);
        DebugLog.funcOut("return managers (length = " + managers.length + ")");
        return managers;
    }
}
