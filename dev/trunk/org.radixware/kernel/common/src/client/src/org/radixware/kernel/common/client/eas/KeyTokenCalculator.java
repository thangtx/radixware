/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.common.client.eas;

import java.util.Arrays;
import org.radixware.kernel.common.auth.AuthUtils;
import org.radixware.kernel.common.auth.PasswordHash;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.utils.ISecretStore;
import org.radixware.kernel.common.client.utils.TokenProcessor;


class KeyTokenCalculator implements ITokenCalculator{
    
    private final ISecretStore secretStore;
    
    public KeyTokenCalculator(final ISecretStore secretStore){
        this.secretStore = secretStore;
    }
    
    private byte[] getKey(){
        final byte[] encryptedData = secretStore.getSecret();
        try{
            return new TokenProcessor().decryptBytes(encryptedData);
        }finally{
            Arrays.fill(encryptedData, (byte)0);
        }        
    }

    @Override
    public SecurityToken calcToken(final byte[] inToken) {
        final byte[] key = getKey();
        try{
            return new SecurityToken( AuthUtils.calcPwdToken( inToken, key ) );
        }finally{
            Arrays.fill(key, (byte)0);
        }
    }

    @Override
    public byte[] createEncryptedHashForNewPassword(final PasswordHash newPwdHash) {        
        final byte[] hashData = newPwdHash.export();
        try{
            final byte[] key = getKey();
            try{
                return AuthUtils.encryptNewPwdHash(hashData, key);
            }finally{
                Arrays.fill(key, (byte)0);
            }
        }finally{
            Arrays.fill(hashData, (byte)0);
        }
    }

    @Override
    public void dispose() {
        secretStore.clearSecret();
    }

    @Override
    public ITokenCalculator copy(final IClientEnvironment environment) {
        return new KeyTokenCalculator(secretStore);
    }

    public byte[] encryptData(final byte[] data){
        final byte[] key = getKey();
        try{
            return AuthUtils.encrypt(data, key, true);
        }finally{
            Arrays.fill(key, (byte)0);
        }
    }
    
    public byte[] decryptData(final byte[] encryptedData){
        final byte[] key = getKey();
        try{
            return AuthUtils.decrypt(encryptedData, key, true);
        }finally{
            Arrays.fill(key, (byte)0);
        }
    }    
}
