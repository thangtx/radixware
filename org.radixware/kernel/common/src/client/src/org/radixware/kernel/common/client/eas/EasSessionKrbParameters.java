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

package org.radixware.kernel.common.client.eas;

import java.security.cert.X509Certificate;
import org.radixware.kernel.common.auth.PasswordHash;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.enums.EAuthType;
import org.radixware.kernel.common.kerberos.KerberosCredentials;


final class EasSessionKrbParameters extends EasSessionCertParameters{
    
    private final IKerberosCredentialsProvider credsProvider;
    private final ISpnegoGssTokenProvider spnegoAuthDelegate;
    
    private EasSessionKrbParameters(final String userName, 
                                   final String stationName, 
                                   final IKerberosCredentialsProvider krbCredentialsProvider,
                                   final ISpnegoGssTokenProvider authDelegate,
                                   final X509Certificate[] userCertificates,
                                   final PasswordHash.Algorithm hashAlgo,
                                   final boolean isWebDriverEnabled){
        super(userName, stationName, userCertificates, hashAlgo, isWebDriverEnabled);
        credsProvider = krbCredentialsProvider;
        spnegoAuthDelegate = authDelegate;
    }
    
    public EasSessionKrbParameters(final String userName, 
                                   final String stationName, 
                                   final IKerberosCredentialsProvider krbCredentialsProvider,
                                   final ISpnegoGssTokenProvider authDelegate,
                                   final X509Certificate[] userCertificates,
                                   final boolean isWebDriverEnabled){
        this(userName,stationName,krbCredentialsProvider,authDelegate,userCertificates,null, isWebDriverEnabled);
    }    
            
    public KrbTokenCalculator createTokenCalculator(final IClientEnvironment environment) throws InterruptedException{
        final KerberosCredentials krbCreds = credsProvider.createCredentials(environment);
        return new KrbTokenCalculator(krbCreds, spnegoAuthDelegate, environment);        
    }
        
    @Override
    public EAuthType getAuthType() {
        return EAuthType.KERBEROS;
    }    
    
    @Override
    public EasSessionKrbParameters createCopy(final String newUserName, final PasswordHash.Algorithm newHashAlgorithm) {
        return new EasSessionKrbParameters(newUserName==null ? getUserName() : newUserName, 
                                           getStationName(), 
                                           credsProvider,
                                           spnegoAuthDelegate,
                                           getUserCertificates(),
                                           newHashAlgorithm==null ? getPwdHashAlgorithm() : newHashAlgorithm,
                                           isWebDriverEnabled());
    }
}
