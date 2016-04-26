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

import java.util.Arrays;
import org.radixware.kernel.common.auth.AuthUtils;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.utils.ISecretStore;
import org.radixware.kernel.common.client.utils.TokenProcessor;


class PwdTokenCalculator implements ITokenCalculator {

    private final byte[] pwdHash;
    private final ISecretStore secretStore;

    public PwdTokenCalculator(final String userName, final String password) {
        pwdHash = AuthUtils.calcPwdHash(userName, password);
        secretStore = null;
    }
    
    public PwdTokenCalculator(final byte[] pwdHash) {
        this.pwdHash = pwdHash;
        secretStore = null;
    }
    
    public PwdTokenCalculator(final ISecretStore secretStore) {
        pwdHash = null;
        this.secretStore = secretStore;
    }
    
    private byte[] getPwdHashFromSecret(){
        return new TokenProcessor().decryptBytes(secretStore.getSecret());
    }

    @Override
    public SecurityToken calcToken(final byte[] token) {
        if (secretStore==null){
            return new SecurityToken(AuthUtils.calcPwdToken(token, pwdHash));
        }else{
            final byte[] secretPwdHash = getPwdHashFromSecret();
            try{
                return new SecurityToken(AuthUtils.calcPwdToken(token, secretPwdHash));
            }finally{
                Arrays.fill(secretPwdHash, (byte)0);
            }
        }
    }

    @Override
    public byte[] createEncryptedHashForNewPassword(final String userName, final char[] newPassword) {
        final byte[] newHash = AuthUtils.calcPwdHash(userName, newPassword);       
        try{
            if (secretStore==null){
                return AuthUtils.encryptNewPwdHash(newHash, pwdHash);
            }else{
                final byte[] secretPwdHash = getPwdHashFromSecret();
                try{
                    return AuthUtils.encryptNewPwdHash(newHash, secretPwdHash);
                }finally{
                    Arrays.fill(secretPwdHash, (byte)0);
                }                
            }
        }finally{
            Arrays.fill(newHash,(byte)0);
        }
    }

    @Override
    public void dispose() {
    }

    @Override
    public ITokenCalculator copy(final IClientEnvironment environment) {
        return secretStore==null ? new PwdTokenCalculator(pwdHash) : new PwdTokenCalculator(secretStore);
    }
    
    public byte[] encryptData(final byte[] data){
        if (secretStore==null){
            return AuthUtils.encrypt(data, pwdHash);
        }else{
            final byte[] secretPwdHash = getPwdHashFromSecret();
            try{
                return AuthUtils.encrypt(data, secretPwdHash);
            }finally{
                Arrays.fill(secretPwdHash, (byte)0);
            }            
        }
    }
    
    public byte[] decryptData(final byte[] encryptedData){
        if (secretStore==null){
            return AuthUtils.decrypt(encryptedData, pwdHash);
        }else{
            final byte[] secretPwdHash = getPwdHashFromSecret();
            try{
                return AuthUtils.decrypt(encryptedData, secretPwdHash);
            }finally{
                Arrays.fill(secretPwdHash, (byte)0);
            }            
        }        
    }
}
