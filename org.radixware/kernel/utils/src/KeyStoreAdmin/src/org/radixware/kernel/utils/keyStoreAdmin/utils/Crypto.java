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

package org.radixware.kernel.utils.keyStoreAdmin.utils;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import org.radixware.kernel.utils.keyStoreAdmin.KeyStoreAdminException;



public class Crypto {
    public static enum DesKeyLength{
        SINGLE,
        DOUBLE,
        TRIPLE;
    }
    
    public static SecretKey generateDesKey(final DesKeyLength keyLength) throws KeyStoreAdminException{
        final KeyGenerator keyGenerator;
        try{
            keyGenerator = KeyGenerator.getInstance(keyLength==DesKeyLength.SINGLE ? "DES" : "DESede");
        } catch (NoSuchAlgorithmException e){
            throw new KeyStoreAdminException(e.getMessage(), e);
        }
        
        SecretKey key = keyGenerator.generateKey();
        
        if (keyLength==DesKeyLength.DOUBLE){
            key = build3DesKey(Arrays.copyOf(key.getEncoded(), 16));
        }
        
        return key;
    }

    public static byte[] calc3desKeyCheckValue(byte[] keyData) throws KeyStoreAdminException{
        final SecretKey key = build3DesKey(keyData);
        return calc3desKeyCheckValue(key);
    }
    
    public static byte[] calc3desKeyCheckValue(Key key) throws KeyStoreAdminException{
        final byte[] zeroBlock = {0, 0, 0, 0, 0, 0, 0, 0};
        try{
            final Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(zeroBlock);
        } catch (GeneralSecurityException e){
            throw new KeyStoreAdminException("Error while calculating 3DES key check value", e);
        }
    }
    
    public static SecretKey build3DesKey(byte[] keyData) throws KeyStoreAdminException{
        if (keyData.length==0)
            throw new KeyStoreAdminException("Error while building 3DES key: key data is empty");
        final byte[] expandedKeyData = new byte[24];
        for (int i=0; i<expandedKeyData.length; i++)
            expandedKeyData[i] = keyData[i%keyData.length];
        try{
            final DESedeKeySpec keySpec = new DESedeKeySpec(expandedKeyData);
            final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
            return keyFactory.generateSecret(keySpec);
        } catch (GeneralSecurityException e){
            throw new KeyStoreAdminException("Error while building 3DES key", e);
        }
    }
}
