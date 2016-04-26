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
import org.radixware.kernel.common.enums.EAuthType;


class EasSessionCertParameters extends AbstractEasSessionParameters{
    
    private final X509Certificate[] certificates;
    
    public EasSessionCertParameters(final String userName, final String stationName, final X509Certificate[] userCertificates){
        super(userName,stationName);
        final int length = userCertificates==null ? 0 :userCertificates.length;
        if (length==0){
            certificates = null;
        }else{
            certificates = new X509Certificate[length];
            System.arraycopy(userCertificates, 0, certificates, 0, length);
        }
    }
    
    public ITokenCalculator createTokenCalculator(){
        return DummyTokenCalculator.INSTANCE;
    }
        
    @Override
    public EAuthType getAuthType() {
        return EAuthType.CERTIFICATE;
    }   

    @Override
    public boolean hasUserCerts() {
        return certificates!=null;
    }

    @Override
    public X509Certificate[] getUserCertificates() {
        if (certificates==null){
            return null;
        }else{
            final int resultLength = certificates.length;
            final X509Certificate[] result = new X509Certificate[resultLength];
            System.arraycopy(certificates, 0, result, 0, resultLength);
            return result;
        }
    }

    @Override
    public EasSessionCertParameters createCopy(final String newUserName) {
        return new EasSessionCertParameters(newUserName==null ? getUserName() : newUserName, 
                                            getStationName(), certificates);
    }     
}
