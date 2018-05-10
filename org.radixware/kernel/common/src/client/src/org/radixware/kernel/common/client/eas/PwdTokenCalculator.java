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
import org.radixware.kernel.common.auth.PasswordHash;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.utils.ISecretStore;
import org.radixware.kernel.common.client.utils.TokenProcessor;
import org.radixware.kernel.common.exceptions.IllegalUsageError;


class PwdTokenCalculator implements ITokenCalculator {

    private final PasswordHash pwdHash;
    private final ISecretStore secretStore;
    private final PasswordHash.Algorithm hashAlgo;

    public PwdTokenCalculator(final String userName, final String password, final PasswordHash.Algorithm hashAlgorithm) {
        hashAlgo = hashAlgorithm;
        pwdHash = PasswordHash.Factory.newInstance(userName, password);
        secretStore = null;
    }
    
    public PwdTokenCalculator(final PasswordHash pwdHash, final PasswordHash.Algorithm hashAlgorithm) {
        this.pwdHash = pwdHash;
        secretStore = null;
        hashAlgo = hashAlgorithm;
    }
    
    public PwdTokenCalculator(final ISecretStore secretStore, final PasswordHash.Algorithm hashAlgorithm) {
        this.secretStore = secretStore;
        hashAlgo = hashAlgorithm;
        pwdHash = null;
    }
    
    private PasswordHash getPwdHashFromSecret(){
        final byte[] encryptedData = secretStore.getSecret();
        final byte[] data = new TokenProcessor().decryptBytes(encryptedData);
        final PasswordHash result;
        try{
            result = PasswordHash.Factory.fromBytes(data);
        }finally{
            Arrays.fill(data, (byte)0);
            Arrays.fill(encryptedData, (byte)0);
        }        
        return result;
    }
    
    public SecurityToken calcToken(final byte[] token, final PasswordHash.Algorithm algo) {
        final PasswordHash.Algorithm hashAlgorithm = algo==null ? hashAlgo : algo;
        if (hashAlgorithm==null){
            throw new IllegalUsageError("Hash algorithm was not defined");
        }
        final PasswordHash hash = secretStore==null ? pwdHash : getPwdHashFromSecret();
        final SecurityToken result;
        try{
            result = new SecurityToken( AuthUtils.calcPwdToken( token, hash.getBytes(hashAlgorithm) ) );
        }finally{
            if (secretStore!=null){
                hash.erase();
            }
        }
        return result;
    }
    
    public PasswordHash.Algorithm getPasswordHashAlgorithm(){
        return hashAlgo;
    }

    @Override
    public SecurityToken calcToken(final byte[] token) {
        return calcToken(token, null);
    }

    @Override
    public byte[] createEncryptedHashForNewPassword(final PasswordHash newPwdHash) {
        return createEncryptedHashForNewPassword(newPwdHash, null);
    }
    
    public byte[] createEncryptedHashForNewPassword(final PasswordHash newPwdHash, final PasswordHash.Algorithm pwdHashAlgo) {
        final PasswordHash.Algorithm hashAlgorithm = pwdHashAlgo==null ? hashAlgo : pwdHashAlgo;
        if (hashAlgorithm==null){
            throw new IllegalUsageError("Hash algorithm was not defined");
        }        
        final byte[] hashData = newPwdHash.export();
        try{
            final PasswordHash hash = secretStore==null ? pwdHash : getPwdHashFromSecret();
            try{
                return AuthUtils.encryptNewPwdHash(hashData, hash.getBytes(hashAlgorithm));
            }finally{
                if (secretStore!=null){
                    hash.erase();
                }
            }
        }finally{
            Arrays.fill(hashData, (byte)0);
        }
    }
    
    @Override
    public void dispose() {
        if (pwdHash!=null){
            pwdHash.erase();
        }        
        if (secretStore!=null){
            secretStore.clearSecret();
        }
    }

    @Override
    public ITokenCalculator copy(final IClientEnvironment environment) {
        if (secretStore==null){
            return new PwdTokenCalculator(pwdHash, hashAlgo);
        }else{
            return new PwdTokenCalculator(secretStore, hashAlgo);
        }
    }
    
    public PwdTokenCalculator createCopyForHashAlgo(final PasswordHash.Algorithm algo){
        if (secretStore==null){
            return new PwdTokenCalculator(pwdHash, algo);
        }else{
            return new PwdTokenCalculator(secretStore, algo);
        }
    }
    
    public byte[] encryptData(final byte[] data, final PasswordHash.Algorithm pwdHashAlgo){
        final PasswordHash.Algorithm hashAlgorithm = pwdHashAlgo==null ? hashAlgo : pwdHashAlgo;
        if (hashAlgorithm==null){
            throw new IllegalUsageError("Hash algorithm was not defined");
        }        
        final PasswordHash hash = secretStore==null ? pwdHash : getPwdHashFromSecret();
        try{
            return AuthUtils.encrypt(data, hash.getBytes(hashAlgorithm), true);
        }finally{
            if (secretStore!=null){
                hash.erase();
            }            
        }
    }
    
    public byte[] decryptData(final byte[] encryptedData, final PasswordHash.Algorithm pwdHashAlgo){
        final PasswordHash.Algorithm hashAlgorithm = pwdHashAlgo==null ? hashAlgo : pwdHashAlgo;
        if (hashAlgorithm==null){
            throw new IllegalUsageError("Hash algorithm was not defined");
        }        
        final PasswordHash hash = secretStore==null ? pwdHash : getPwdHashFromSecret();
        try{
            return AuthUtils.decrypt(encryptedData, hash.getBytes(hashAlgorithm), true);
        }finally{
            if (secretStore!=null){
                hash.erase();
            }            
        }
    }
}
