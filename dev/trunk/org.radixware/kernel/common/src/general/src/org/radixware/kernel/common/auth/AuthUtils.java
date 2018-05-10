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

package org.radixware.kernel.common.auth;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.radixware.kernel.common.exceptions.RadixError;


public class AuthUtils {        

    public static enum PwdWeakness {
        NONE,
        TOO_SHORT,
        NO_NUMERIC_CHARS,
        NO_ALPHABETIC_CHARS,
        ALL_ALPHABETIC_CHARS_IN_SAME_CASE,
        NO_SPECIAL_CHARS,
        SAME_AS_USER_NAME,
        STARTS_WITH_FORBIDDEN_CHARS,
        ENDS_WITH_FORBIDDEN_CHARS,
        CONTAIN_FORBIDDEN_CHARS,
        FORBIDDEN
    }
    
    public static class PwdWeaknessCheckResult{
        
        public static final PwdWeaknessCheckResult VALID = new PwdWeaknessCheckResult(PwdWeakness.NONE);
        
        private final PwdWeakness weakness;
        private final String details;
        
        private PwdWeaknessCheckResult(final PwdWeakness weakness){
            this.weakness = weakness;
            details = null;
        }
        
        private PwdWeaknessCheckResult(final PwdWeakness weakness, final String details){
            this.weakness = weakness;
            this.details = details;
        }

        public PwdWeakness getWeakness() {
            return weakness;
        }

        public String getDetails() {
            return details;
        }                
    }
    
    private static final int PWD_HASH_MAX_LENGTH = 16;

    /**
     * Check if password is allowed by PCI-DSS PCI-DSS Requirment 8.5.10 Require
     * a minimum password length of at least seven characters. PCI-DSS
     * Requirment 8.5.11 Use passwords containing both numeric and alphabetic
     * characters.
     *
     * @param password
     * @return check results
     */
    public static final PwdWeakness checkPwdWeakness(final String password) {
        return checkPwdWeakness(password, Long.valueOf(7), true, true);
    }

    public static final PwdWeakness checkPwdWeakness(final String password, final Long minLen, final boolean mustContainAChar, final boolean mustContainNChar) {
        return checkPwdWeakness(password, new PasswordRequirements(minLen == null ? -1 : minLen.intValue(), mustContainAChar, mustContainNChar));
    }
    
    public static final PwdWeakness checkPwdWeakness(final String password, 
                                                     final Long minLen, 
                                                     final boolean mustContainAChar, 
                                                     final boolean mustContainACharInMixedCase, 
                                                     final boolean mustContainNChar,
                                                     final boolean mustContainSChar) {
        final int pwMinLen = minLen == null ? -1 : minLen.intValue();
        return checkPwdWeakness(password, new PasswordRequirements(pwMinLen, mustContainAChar, mustContainACharInMixedCase, mustContainNChar, mustContainSChar, null, null));
    }

    /**
     *
     * @param password
     * @param minLen - if null do not check password length
     * @param mustContainAChar
     * @param mustContainNChar
     * @return check results
     */
    public static final PwdWeakness checkPwdWeakness(final String password, final PasswordRequirements requirements) {
        if (requirements == null) {
            return PwdWeakness.NONE;
        }
        
        final String userName = requirements.getUserName();
        if (userName!=null && !userName.isEmpty() && userName.equals(password)){
            return PwdWeakness.SAME_AS_USER_NAME;
        }        

        if (requirements.getBlackList() != null) {
            String regex;
            for (String blackPwd : requirements.getBlackList()) {
                if (password.equals(blackPwd)){
                    return PwdWeakness.FORBIDDEN;
                }else{
                    regex = ("\\Q" + blackPwd + "\\E").replace("*", "\\E.*\\Q");
                    if (password.matches(regex)){
                        if (blackPwd.startsWith(blackPwd.replace("*", ""))){
                            return PwdWeakness.STARTS_WITH_FORBIDDEN_CHARS;
                        }else if (blackPwd.endsWith(blackPwd.replace("*", ""))){
                            return PwdWeakness.ENDS_WITH_FORBIDDEN_CHARS;                    
                        }else{
                            return PwdWeakness.CONTAIN_FORBIDDEN_CHARS;                    
                        }                    
                    }
                }
            }
        }
        final long minLength = requirements.getMinLength();
        if (minLength > 0 && password.length() < minLength) {
            return PwdWeakness.TOO_SHORT;
        }
        if (requirements.mustContainLetter() 
            || requirements.mustContainDigit()
            || requirements.mustContainSpecialChar()) {
            boolean hasAChars = false, hasNChars = false, hasSChars = false, 
                    hasACharsInLowerCase = false, hasACharsInUpperCase = false;
            for (char c : password.toCharArray()) {
                if (Character.isLetter(c)){
                    hasAChars = true;
                    hasACharsInLowerCase = hasACharsInLowerCase || Character.isLowerCase(c);
                    hasACharsInUpperCase = hasACharsInUpperCase || Character.isUpperCase(c);
                }else if (Character.isDigit(c)){
                    hasNChars = true;
                }else{
                    hasSChars = true;
                }
            }
            //PCI-DSS: 8.5.11 Use passwords containing both numeric and alphabetic characters.
            if (requirements.mustContainLetter()) {
                if (!hasAChars){
                    return PwdWeakness.NO_ALPHABETIC_CHARS;
                }
                if (requirements.mustContainLetterInMixedCase() && (!hasACharsInUpperCase || !hasACharsInLowerCase)){
                    return PwdWeakness.ALL_ALPHABETIC_CHARS_IN_SAME_CASE;
                }
            }
            if (requirements.mustContainDigit() && !hasNChars) {
                return PwdWeakness.NO_NUMERIC_CHARS;
            }
            if (requirements.mustContainSpecialChar() && !hasSChars){
                return PwdWeakness.NO_SPECIAL_CHARS;
            }
        }
        return PwdWeakness.NONE;
    }
    
    public static final Collection<PwdWeaknessCheckResult> collectPwdWeakness(final String password, final PasswordRequirements requirements) {    
        final List<PwdWeaknessCheckResult> result = new LinkedList<>();
        if (requirements == null) {
            return result;
        }
        
        final String userName = requirements.getUserName();
        if (userName!=null && !userName.isEmpty() && userName.equals(password)){
            result.add(new PwdWeaknessCheckResult( PwdWeakness.SAME_AS_USER_NAME, userName ));
        }        

        if (requirements.getBlackList() != null) {
            String regex;
            for (String blackPwd : requirements.getBlackList()) {
                if (password.equals(blackPwd)){
                    result.add(new PwdWeaknessCheckResult(PwdWeakness.FORBIDDEN));
                }else{
                    regex = ("\\Q" + blackPwd + "\\E").replace("*", "\\E.*\\Q");
                    if (password.matches(regex)){
                        final String forbiddenChars = blackPwd.replace("*", " ").trim();
                        final PwdWeakness weakness;
                        if (blackPwd.startsWith(blackPwd.replace("*", ""))){
                            weakness = PwdWeakness.STARTS_WITH_FORBIDDEN_CHARS;
                        }else if (blackPwd.endsWith(blackPwd.replace("*", ""))){
                            weakness = PwdWeakness.ENDS_WITH_FORBIDDEN_CHARS;                    
                        }else{
                            weakness = PwdWeakness.CONTAIN_FORBIDDEN_CHARS;                    
                        }
                        result.add(new PwdWeaknessCheckResult(weakness, forbiddenChars));
                        break;
                    }
                }
            }
        }
        final long minLength = requirements.getMinLength();
        if (minLength > 0 && password.length() < minLength) {
            result.add(new PwdWeaknessCheckResult(PwdWeakness.TOO_SHORT, String.valueOf(minLength)));
        }
        
        if (requirements.mustContainLetter() 
            || requirements.mustContainDigit()
            || requirements.mustContainSpecialChar()) {
            boolean hasAChars = false, hasNChars = false, hasSChars = false, 
                    hasACharsInLowerCase = false, hasACharsInUpperCase = false;
            for (char c : password.toCharArray()) {
                if (Character.isLetter(c)){
                    hasAChars = true;
                    hasACharsInLowerCase = hasACharsInLowerCase || Character.isLowerCase(c);
                    hasACharsInUpperCase = hasACharsInUpperCase || Character.isUpperCase(c);
                }else if (Character.isDigit(c)){
                    hasNChars = true;
                }else{
                    hasSChars = true;
                }
            }
            //PCI-DSS: 8.5.11 Use passwords containing both numeric and alphabetic characters.
            if (requirements.mustContainLetter()) {
                if (!hasAChars){
                    result.add(new PwdWeaknessCheckResult(PwdWeakness.NO_ALPHABETIC_CHARS));
                }
                if (requirements.mustContainLetterInMixedCase() && (!hasACharsInUpperCase || !hasACharsInLowerCase)){
                    result.add(new PwdWeaknessCheckResult(PwdWeakness.ALL_ALPHABETIC_CHARS_IN_SAME_CASE));
                }
            }
            if (requirements.mustContainDigit() && !hasNChars) {
                result.add(new PwdWeaknessCheckResult(PwdWeakness.NO_NUMERIC_CHARS));
            }
            if (requirements.mustContainSpecialChar() && !hasSChars){
                result.add(new PwdWeaknessCheckResult(PwdWeakness.NO_SPECIAL_CHARS));
            }
        }
        return result;
    }                

    @Deprecated//Use PasswordHash class instead
    public static final byte[] calcPwdHash(final String userName, final String password) {
        try {
            final MessageDigest sha1 = MessageDigest.getInstance("SHA1");
            sha1.reset();
            sha1.update((userName.toUpperCase() + "-" + password).getBytes("UTF-16"));
            final byte[] byteHash = sha1.digest();
            if (byteHash.length <= PWD_HASH_MAX_LENGTH) {
                return byteHash;
            } else {
                final byte[] pwdHash = new byte[PWD_HASH_MAX_LENGTH];
                System.arraycopy(byteHash, 0, pwdHash, 0, PWD_HASH_MAX_LENGTH);
                return pwdHash;
            }
        } catch (UnsupportedEncodingException e) {
            throw new RadixError(e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            throw new RadixError(e.getMessage(), e);
        }
    }
    
    @Deprecated//Use PasswordHash class instead
    public static final byte[] calcPwdHash(final String userName, final char[] password) {
        try {
            final MessageDigest sha1 = MessageDigest.getInstance("SHA1");
            sha1.reset();
            final CharBuffer cb = CharBuffer.allocate(userName.length() + password.length + 1);
            cb.append(userName.toUpperCase());
            cb.append('-');
            for (int i = 0; i < password.length; i++) {
                cb.append(password[i]);
            }
            cb.position(0);
            final ByteBuffer bb = Charset.forName("UTF-16").encode(cb);
            final byte[] bufferContent = bb.array();
            final byte[] actualContent = new byte[bb.limit()];
            System.arraycopy(bufferContent, bb.arrayOffset(), actualContent, 0, bb.limit());
            try {
                sha1.update(actualContent);
                final byte[] byteHash = sha1.digest();
                if (byteHash.length <= PWD_HASH_MAX_LENGTH) {
                    return byteHash;
                } else {
                    final byte[] pwdHash = new byte[PWD_HASH_MAX_LENGTH];
                    System.arraycopy(byteHash, 0, pwdHash, 0, PWD_HASH_MAX_LENGTH);
                    Arrays.fill(byteHash, (byte) 0);
                    return pwdHash;
                }
            } finally {
                Arrays.fill(bufferContent, (byte) 0);
                Arrays.fill(actualContent, (byte) 0);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RadixError(e.getMessage(), e);
        }
    }    

    public static final byte[] calcPwdToken(final byte[] challenge, final byte[] pwdHash) {
        if (challenge == null) {
            throw new IllegalArgumentException("challenge is null");
        }
        if (challenge.length != 8) {
            throw new IllegalArgumentException("Invalid challenge length: " + challenge.length);
        }
        return encrypt(challenge, pwdHash);
    }

    public static final byte[] encryptNewPwdHash(final byte[] newPwdHash, final byte[] oldPwdHash) {
        if (newPwdHash == null) {
            throw new IllegalArgumentException("newPwdHash is null");
        }
        return encrypt(newPwdHash, oldPwdHash, true);
    }

    public static final byte[] decryptNewPwdHash(final byte[] newPwdHashCryptogram, final byte[] oldPwdHash) {
        if (newPwdHashCryptogram == null) {
            throw new IllegalArgumentException("newPwdHashCryptogram is null");
        }
        return decrypt(newPwdHashCryptogram, oldPwdHash, true);
    }

    public static final byte[] encrypt(final byte[] data, final byte[] keyData, final boolean paddingEnabled) {
        if (keyData==null){
            throw new IllegalArgumentException("encryption key is null");
        }else if (keyData.length==TRIPLE_DES_KEY_LEN){
            return encrypt_decrypt(data, keyData, Cipher.ENCRYPT_MODE, paddingEnabled);
        }else if (keyData.length==PWD_HASH_MAX_LENGTH){
            return encrypt_decrypt(data, create3DesKeyDataFromPwdHash(keyData), Cipher.ENCRYPT_MODE, paddingEnabled);
        }else if (keyData.length>TRIPLE_DES_KEY_LEN && keyData.length<=2*TRIPLE_DES_KEY_LEN){
            byte[] key = new byte[TRIPLE_DES_KEY_LEN];
            System.arraycopy(keyData, 0, key, 0, TRIPLE_DES_KEY_LEN);
            byte[] result;
            try{
                result = encrypt_decrypt(data, key, Cipher.ENCRYPT_MODE, paddingEnabled);
            }finally{
                Arrays.fill(key, (byte)0);
            }
            System.arraycopy(keyData, keyData.length-TRIPLE_DES_KEY_LEN, key, 0, TRIPLE_DES_KEY_LEN);
            try{
                return encrypt_decrypt(result, key, Cipher.ENCRYPT_MODE, paddingEnabled);
            }finally{
                Arrays.fill(key, (byte)0);
            }
        }else{
            throw new IllegalArgumentException("Invalid encryption key length: " + keyData.length);
        }
    }
    
    public static final byte[] encrypt(final byte[] data, final byte[] keyData) {
        return encrypt(data, keyData, false);
    }

    public static final byte[] decrypt(final byte[] data, final byte[] keyData, final boolean paddingEnabled) {
        if (keyData==null){
            throw new IllegalArgumentException("encryption key is null");
        }else if (keyData.length==TRIPLE_DES_KEY_LEN){
            return encrypt_decrypt(data, keyData, Cipher.DECRYPT_MODE, paddingEnabled);
        }else if (keyData.length==PWD_HASH_MAX_LENGTH){
            return encrypt_decrypt(data, create3DesKeyDataFromPwdHash(keyData), Cipher.DECRYPT_MODE, paddingEnabled);
        }else if (keyData.length>TRIPLE_DES_KEY_LEN && keyData.length<=2*TRIPLE_DES_KEY_LEN){
            byte[] key = new byte[TRIPLE_DES_KEY_LEN];
            System.arraycopy(keyData, keyData.length-TRIPLE_DES_KEY_LEN, key, 0, TRIPLE_DES_KEY_LEN);
            byte[] result;
            try{
                result = encrypt_decrypt(data, key, Cipher.DECRYPT_MODE, paddingEnabled);
            }finally{
                Arrays.fill(key, (byte)0);
            }            
            System.arraycopy(keyData, 0, key, 0, TRIPLE_DES_KEY_LEN);            
            try{
                return encrypt_decrypt(result, key, Cipher.DECRYPT_MODE, paddingEnabled);
            }finally{
                Arrays.fill(key, (byte)0);
            }
        }else{
            throw new IllegalArgumentException("Invalid encryption key length: " + keyData.length);
        }        
    }
    
    public static final byte[] decrypt(final byte[] data, final byte[] keyData) {
        return decrypt(data, keyData, false);
    }

    private static final byte[] encrypt_decrypt(final byte[] data, final byte[] tripleDesKey, final int cipherMode, final boolean paddingEnabled) {        
        if (tripleDesKey.length!=TRIPLE_DES_KEY_LEN){
            throw new IllegalArgumentException("Invalid encryption key length: " + tripleDesKey.length);
        }
        try {
            final SecretKeySpec key = new SecretKeySpec(tripleDesKey, "DESede");
            final Cipher encripter;
            if (paddingEnabled){
                encripter = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            }else{
                encripter = Cipher.getInstance("DESede/CBC/NoPadding");
            }
            final IvParameterSpec ivParameterSpec = new IvParameterSpec(new byte[]{0, 0, 0, 0, 0, 0, 0, 0});
            encripter.init(cipherMode, key, ivParameterSpec);
            return encripter.doFinal(data);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RadixError(e.getMessage(), e);
        } catch (IllegalBlockSizeException e) {
            throw new RadixError(e.getMessage(), e);
        } catch (BadPaddingException e) {
            throw new RadixError(e.getMessage(), e);
        } catch (NoSuchPaddingException e) {
            throw new RadixError(e.getMessage(), e);
        } catch (InvalidKeyException e) {
            throw new RadixError(e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            throw new RadixError(e.getMessage(), e);
        }
    }
    
    private static final byte DES_KEY_LEN = 8;
    private static final byte TRIPLE_DES_KEY_LEN = 3 * DES_KEY_LEN;

    private static final byte[] create3DesKeyDataFromPwdHash(final byte[] keyBytes) {
        if (keyBytes == null) {
            throw new IllegalArgumentException("keyBytes is null");
        }
        if (keyBytes.length==PWD_HASH_MAX_LENGTH){//sha-1 pwd hash
            final byte[] keyData = new byte[TRIPLE_DES_KEY_LEN];
            System.arraycopy(keyBytes, 0, keyData, 0, keyBytes.length);
            System.arraycopy(keyData, 0, keyData, TRIPLE_DES_KEY_LEN - DES_KEY_LEN, DES_KEY_LEN);
            return keyData;
        }else if (keyBytes.length>=TRIPLE_DES_KEY_LEN){
            final byte[] keyData = new byte[TRIPLE_DES_KEY_LEN];
            System.arraycopy(keyBytes, 0, keyData, 0, TRIPLE_DES_KEY_LEN);
            return keyData;
        }else{
            throw new IllegalArgumentException("Invalid pwdHash length: " + keyBytes.length);
        }
    }
}
