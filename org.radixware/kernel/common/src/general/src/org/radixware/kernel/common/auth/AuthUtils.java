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
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.utils.Utils;


public class AuthUtils {

    public static enum PwdWeakness {

        /**
         * pwd is strong enough
         */
        NONE,
        TOO_SHORT,
        NO_NUMERIC_CHARS,
        NO_ALPHABETIC_CHARS,
        FROM_BLACK_LIST
    }

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

        if (requirements.getBlackList() != null) {
            for (String blackPwd : requirements.getBlackList()) {
                if (Utils.equals(blackPwd, password)) {
                    return PwdWeakness.FROM_BLACK_LIST;
                }
            }
        }

        if (requirements.getMinLength() > 0 && password.length() < requirements.getMinLength()) {
            return PwdWeakness.TOO_SHORT;
        }
        if (requirements.mustContainLetter() || requirements.mustContainDigit()) {
            boolean hasAChars = false, hasNChars = false;
            for (char c : password.toCharArray()) {
                hasAChars = hasAChars || Character.isLetter(c);
                hasNChars = hasNChars || Character.isDigit(c);
            }
            //PCI-DSS: 8.5.11 Use passwords containing both numeric and alphabetic characters.
            if (requirements.mustContainLetter() && !hasAChars) {
                return PwdWeakness.NO_ALPHABETIC_CHARS;
            }
            if (requirements.mustContainDigit() && !hasNChars) {
                return PwdWeakness.NO_NUMERIC_CHARS;
            }
        }
        return PwdWeakness.NONE;
    }
    private static final int PWD_HASH_MAX_LENGTH = 16;

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
        if (newPwdHash.length > 16) {
            throw new IllegalArgumentException("Invalid newPwdHash length: " + newPwdHash.length);
        }
        return encrypt(newPwdHash, oldPwdHash);
    }

    public static final byte[] decryptNewPwdHash(final byte[] newPwdHashCryptogram, final byte[] oldPwdHash) {
        if (newPwdHashCryptogram == null) {
            throw new IllegalArgumentException("newPwdHashCryptogram is null");
        }
        return decrypt(newPwdHashCryptogram, oldPwdHash);
    }

    public static final byte[] encrypt(final byte[] data, final byte[] keyData) {
        return encrypt_decrypt(data, keyData, Cipher.ENCRYPT_MODE);
    }

    public static final byte[] decrypt(final byte[] data, final byte[] keyData) {
        return encrypt_decrypt(data, keyData, Cipher.DECRYPT_MODE);
    }

    private static final byte[] encrypt_decrypt(final byte[] data, final byte[] keyData, final int cipherMode) {
        try {
            final SecretKeySpec key = new SecretKeySpec(create3DesKeyData(keyData), "DESede");
            final Cipher encripter = Cipher.getInstance("DESede/CBC/NoPadding");
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

    private static final byte[] create3DesKeyData(final byte[] pwdHash) {
        if (pwdHash == null) {
            throw new IllegalArgumentException("pwdHash is null");
        }
        if (pwdHash.length > PWD_HASH_MAX_LENGTH) {
            throw new IllegalArgumentException("Invalid pwdHash length: " + pwdHash.length);
        }
        final byte[] keyData = new byte[TRIPLE_DES_KEY_LEN];
        System.arraycopy(pwdHash, 0, keyData, 0, pwdHash.length);
        System.arraycopy(keyData, 0, keyData, TRIPLE_DES_KEY_LEN - DES_KEY_LEN, DES_KEY_LEN);
        return keyData;
    }
}
