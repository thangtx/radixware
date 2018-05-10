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

package org.radixware.kernel.common.client.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.starter.Starter;
import org.radixware.kernel.starter.radixloader.RadixLoader;


public final class CryptoUtils {
    
    private static final byte DES_KEY_LEN = 8;
    private static final byte TRIPLE_DES_KEY_LEN = 3 * DES_KEY_LEN;    
    
    private CryptoUtils(){        
    }
    
    public static  byte[] generateSeed() throws UnsupportedEncodingException {
        final StringBuffer seedBuffer = new StringBuffer();
        seedBuffer.append(System.getProperty("user.name"));
        seedBuffer.append(System.getProperty("os.version"));
        if (Starter.getProductName() != null) {
            seedBuffer.append(Starter.getProductName());
        }
        if (RadixLoader.getInstance() != null && RadixLoader.getInstance().getRepositoryUri() != null) {
            seedBuffer.append(RadixLoader.getInstance().getRepositoryUri());
        }
        seedBuffer.append(System.getProperty("java.version"));
        return seedBuffer.toString().getBytes("UTF-8");
    }
    
    public static byte[] encrypt3Des(final byte[] data, final byte[] key){
        final byte[] tripleDesKey;
        try{
            tripleDesKey = get3DesKey(key);
        }catch(UnsupportedEncodingException ex){
            throw new RadixError("Failed to encrypt data ", ex);
        }
        try{
            return encrypt_decrypt(data, tripleDesKey, Cipher.ENCRYPT_MODE);
        }finally{
            Arrays.fill(tripleDesKey, (byte)0);
        }
    }
    
    public static byte[] encrypt3Des(final byte[] data, final ISecretStore secretStore){
        final byte[] encryptedKey = secretStore.getSecret();
        final byte[] key;
        try{
            key = new TokenProcessor().decryptBytes(encryptedKey);
        }finally{
            Arrays.fill(encryptedKey, (byte)0);
        }
        try{
            return encrypt3Des(data, key);
        }finally{
            Arrays.fill(key, (byte)0);
        }
    }    
    
    public static byte[] decrypt3Des(final byte[] data, final byte[] key){
        final byte[] tripleDesKey;
        try{
            tripleDesKey = get3DesKey(key);
        }catch(UnsupportedEncodingException ex){
            throw new RadixError("Failed to decrypt data ", ex);
        }
        try{
            return encrypt_decrypt(data, tripleDesKey, Cipher.DECRYPT_MODE);
        }finally{
            Arrays.fill(tripleDesKey, (byte)0);
        }
    }    
    
    public static byte[] decrypt3Des(final byte[] data, final ISecretStore secretStore){
        final byte[] encryptedKey = secretStore.getSecret();
        final byte[] key;
        try{
            key = new TokenProcessor().decryptBytes(encryptedKey);
        }finally{
            Arrays.fill(encryptedKey, (byte)0);
        }
        try{
            return decrypt3Des(data, key);
        }finally{
            Arrays.fill(key, (byte)0);
        }
    }     
    
    private static byte[] get3DesKey(final byte[] key) throws UnsupportedEncodingException{
        final byte[] key3Des = new byte[TRIPLE_DES_KEY_LEN];
        System.arraycopy(generateSeed(), 0, key3Des, 0, TRIPLE_DES_KEY_LEN);
        System.arraycopy(key, 0, key3Des, 0, Math.min(key.length, TRIPLE_DES_KEY_LEN));
        return key3Des;
    }        
    
    private static final byte[] encrypt_decrypt(final byte[] data, final byte[] tripleDesKey, final int cipherMode) {
        if (tripleDesKey.length!=TRIPLE_DES_KEY_LEN){
            throw new IllegalArgumentException("Invalid encryption key length: " + tripleDesKey.length);
        }
        try {
            final SecretKeySpec key = new SecretKeySpec(tripleDesKey, "DESede");
            final Cipher encripter = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            final IvParameterSpec ivParameterSpec = new IvParameterSpec(new byte[]{0, 0, 0, 0, 0, 0, 0, 0});
            encripter.init(cipherMode, key, ivParameterSpec);
            return encripter.doFinal(data);
        } catch (InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RadixError(e.getMessage(), e);
        }
    }
    
}
