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
import org.radixware.kernel.common.client.utils.ISecretStore;
import org.radixware.kernel.common.enums.EAuthType;


final class EasSessionPwdParameters extends AbstractEasSessionParameters{
    
    public EasSessionPwdParameters(final String userName, final String stationName){
        super(userName,stationName);
    }
    
    public PwdTokenCalculator createTokenCalculator(final ISecretStore secretStore){
        return new PwdTokenCalculator(secretStore);
    }
    
    public PwdTokenCalculator createTokenCalculator(final String pwd){
        return new PwdTokenCalculator(getUserName(), pwd);
    }
    
    public PwdTokenCalculator createTokenCalculator(final byte[] pwdHash){
        return new PwdTokenCalculator(pwdHash);
    }

    @Override
    public EAuthType getAuthType() {
        return EAuthType.PASSWORD;
    }        

    @Override
    public boolean hasUserCerts() {
        return false;
    }

    @Override
    public X509Certificate[] getUserCertificates() {
        return null;
    }

    @Override
    public EasSessionPwdParameters createCopy(final String newUserName) {
        return new EasSessionPwdParameters(newUserName==null ? getUserName() : newUserName, getStationName());
    }
        
}
